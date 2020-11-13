package valoeghese.scalpel.audio;

import org.lwjgl.openal.AL10;

import static org.lwjgl.openal.AL10.*;

public class AudioSource {
	public AudioSource() {
		this.source = alGenSources();
	}

	private final int source;

	public void setGain(float gain) {
		alSourcef(this.source, AL_GAIN, gain);
	}

	public void setPitch(float pitch) {
		alSourcef(this.source, AL_GAIN, pitch);
	}

	public void setPosition(float x, float y, float z) {
		alSource3f(this.source, AL_POSITION, x, y, z);
	}

	public void setVelocity(float x, float y, float z) {
		alSource3f(this.source, AL_VELOCITY, x, y, z);
	}

	public void attachBufferData(AudioBuffer data) {
		alSourcei(this.source, AL_BUFFER, data.soundBuffer);
	}

	public void play() {
		alSourcePlay(this.source);
	}

	public void destroy() {
		alDeleteSources(this.source);
	}
}
