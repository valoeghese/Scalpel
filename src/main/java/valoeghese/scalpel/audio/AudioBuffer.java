package valoeghese.scalpel.audio;

import valoeghese.scalpel.util.ALUtils;

import java.nio.ShortBuffer;

import static org.lwjgl.openal.AL10.*;

public class AudioBuffer {
	public AudioBuffer(int soundBuffer, ShortBuffer data, int channels, int sampleRate) {
		alBufferData(soundBuffer, channels == 1 ? AL_FORMAT_MONO16 : AL_FORMAT_STEREO16, data, sampleRate);
		this.soundBuffer = soundBuffer;
	}

	public AudioBuffer(int soundBuffer) {
		this.soundBuffer = soundBuffer;
	}

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

	public final int soundBuffer;
}
