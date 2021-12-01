package valoeghese.scalpel.util;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.stb.STBVorbisInfo;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import valoeghese.scalpel.audio.AudioBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.ALC10.*;
import static org.lwjgl.stb.STBVorbis.*;
import static valoeghese.scalpel.util.GLUtils.NULL;

public final class ALUtils {
	public static void initAL() {
		device = alcOpenDevice((ByteBuffer) null);

		if (device == NULL) {
			throw new RuntimeException("Unable to open the OpenAL device.");
		}

		ALCCapabilities capabilities = ALC.createCapabilities(device);

		context = alcCreateContext(device, (IntBuffer) null);

		if (context == NULL) {
			throw new RuntimeException("Unable to create the OpenAL context.");
		}

		alcMakeContextCurrent(context);
		AL.createCapabilities(capabilities);
	}

	public static void setListenerPosition(float x, float y, float z) {
		alListener3f(AL_POSITION, x, y, z);
	}

	public static void setListenerVelocity(float x, float y, float z) {
		alListener3f(AL_VELOCITY, x, y, z);
	}

	public static AudioBuffer createBuffer(String fileName) throws IOException { // https://www.youtube.com/watch?v=Mrcs9vIHSws
		int audioBuffer = alGenBuffers();

		try (STBVorbisInfo info = STBVorbisInfo.malloc()) {
			ShortBuffer pcm = null;
			final int bufferSize = 32 * 1024;

			try (MemoryStack stack = MemoryStack.stackPush()) {
				ByteBuffer vorbis = ResourceLoader.loadAsByteBuffer(fileName, bufferSize);
				IntBuffer error = stack.mallocInt(1);
				long decoder = stb_vorbis_open_memory(vorbis, error, null);

				if (decoder == NULL) {
					throw new RuntimeException("Error opening Ogg Vorbis decoder. Error code " + error.get(0));
				}

				stb_vorbis_get_info(decoder, info);

				int channels = info.channels();
				pcm = MemoryUtil.memAllocShort(stb_vorbis_stream_length_in_samples(decoder) * channels);
				pcm.limit(stb_vorbis_get_samples_short_interleaved(decoder, channels, pcm) * channels);
				stb_vorbis_close(decoder);
			}

			if (pcm == null) {
				throw new IOException("Error importing audio data!");
			}

			audioBuffers.add(audioBuffer);
			return new AudioBuffer(audioBuffer, pcm, info.channels(), info.sample_rate());
		} catch (IOException e) {
			alDeleteBuffers(audioBuffer);
			throw e;
		}
	}

	public static void removeSoundBuffer(int soundBuffer) {
		audioBuffers.removeInt(soundBuffer);
	}

	public static void shutdown() {
		for (int soundBuffer : audioBuffers) {
			alDeleteBuffers(soundBuffer);
		}

		alcDestroyContext(context);
		alcCloseDevice(device);
	}

	private static long device;
	private static long context;
	private static IntList audioBuffers = new IntArrayList();
}
