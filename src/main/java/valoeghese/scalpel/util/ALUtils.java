package valoeghese.scalpel.util;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.stb.STBVorbis;
import org.lwjgl.stb.STBVorbisInfo;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import valoeghese.scalpel.audio.AudioBuffer;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.ALC10.*;
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

	public static AudioBuffer createBuffer(ByteBuffer data) { // Based off the ogg reading example in lwjglbook
		int soundBuffer = alGenBuffers();
		soundBuffers.add(soundBuffer);

		try (MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer errorBuffer = stack.mallocInt(1);
			long decoder = STBVorbis.stb_vorbis_open_memory(data, errorBuffer, null);

			if (decoder == NULL) {
				throw new RuntimeException("Error opening OGG Vorbis decoder. Error code " + errorBuffer.get(0));
			}

			try (STBVorbisInfo info = STBVorbisInfo.malloc()) {
				STBVorbis.stb_vorbis_get_info(decoder, info);
				int channels = info.channels();
				ShortBuffer resultData = MemoryUtil.memAllocShort(STBVorbis.stb_vorbis_stream_length_in_samples(decoder));
				resultData.limit(STBVorbis.stb_vorbis_get_samples_short_interleaved(decoder, channels, resultData) * channels);
				STBVorbis.stb_vorbis_close(channels);
				return new AudioBuffer(soundBuffer, resultData, channels, info.sample_rate());
			}
		}
	}

	public static void removeSoundBuffer(int soundBuffer) {
		soundBuffers.removeInt(soundBuffer);
	}

	public static void shutdown() {
		for (int soundBuffer : soundBuffers) {
			alDeleteBuffers(soundBuffer);
		}

		alcDestroyContext(context);
		alcCloseDevice(device);
	}

	private static long device;
	private static long context;
	private static IntList soundBuffers = new IntArrayList();
}
