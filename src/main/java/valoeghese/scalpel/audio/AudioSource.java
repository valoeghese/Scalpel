package valoeghese.scalpel.audio;

import static org.lwjgl.openal.AL10.*;

public class AudioSource {
	public AudioSource() {
		this.source = alGenSources();
	}

	/**
	 * @deprecated use {@linkplain AudioSource#getHandle()} instead.
	 */
	@Deprecated
	public final int source;

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

	public void setLooping(boolean looping) {
		alSourcei(this.source, AL_LOOPING, looping ? AL_TRUE : AL_FALSE);
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

	public boolean isPlaying() {
		return alGetSourcei(this.source, AL_SOURCE_STATE) == AL_PLAYING;
	}

	public boolean isLooping() {
		return alGetSourcei(this.source, AL_LOOPING) == AL_TRUE;
	}

	/**
	 * @return the underlying OpenGL source object.
	 */
	public int getHandle() {
		return this.source;
	}
}
