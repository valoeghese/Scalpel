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

	public final int soundBuffer;
}
