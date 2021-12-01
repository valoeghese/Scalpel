package valoeghese.scalpel.audio;

import valoeghese.scalpel.util.ALUtils;

import java.nio.ShortBuffer;

import static org.lwjgl.openal.AL10.*;

public class AudioBuffer {
	public AudioBuffer(int audioBuffer, ShortBuffer data, int channels, int sampleRate) {
		alBufferData(audioBuffer, channels == 1 ? AL_FORMAT_MONO16 : AL_FORMAT_STEREO16, data, sampleRate);
		this.soundBuffer = audioBuffer;
	}

	public AudioBuffer(int audioBuffer) {
		this.soundBuffer = audioBuffer;
	}

	/**
	 * @deprecated use {@linkplain AudioBuffer#getHandle()} instead.
	 */
	@Deprecated
	public final int soundBuffer;

	public void destroy() {
		ALUtils.removeSoundBuffer(this.soundBuffer);
		alDeleteBuffers(this.soundBuffer);
	}

	/**
	 * @return the length of the audio in this buffer, in samples
	 */
	public int getLengthSamples() {
		return alGetBufferi(this.soundBuffer, AL_SIZE) * 8 /
				(alGetBufferi(this.soundBuffer, AL_BITS) * this.getChannels());
	}

	/**
	 * @return the duration of the audio in this buffer, in seconds
	 */
	public float getDurationSeconds() {
		return (float) this.getLengthSamples() / (float) alGetBufferi(this.soundBuffer, AL_FREQUENCY);
	}

	public int getChannels() {
		return alGetBufferi(this.soundBuffer, AL_CHANNELS);
	}

	/**
	 * @return the underlying OpenGL buffer object.
	 */
	public int getHandle() {
		return this.soundBuffer;
	}
}
