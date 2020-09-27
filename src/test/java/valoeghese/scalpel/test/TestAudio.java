package valoeghese.scalpel.test;

import valoeghese.scalpel.audio.AudioBuffer;
import valoeghese.scalpel.ScalpelApp;
import valoeghese.scalpel.audio.AudioSource;
import valoeghese.scalpel.util.ALUtils;
import valoeghese.scalpel.util.ResourceLoader;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.ByteBuffer;

public class TestAudio extends ScalpelApp {
	private TestAudio() {
		super(100 / 20);
	}

	AudioBuffer audio;
	AudioSource source;

	@Override
	protected void preInit() {
		ALUtils.initAL();

		ALUtils.setListenerPosition(0, 0, 0);
		ALUtils.setListenerVelocity(0, 0, 0);

		try {
			ByteBuffer data = ResourceLoader.loadAsByteBuffer("assets/sound/Test_Sound.ogg");
			data.flip();
			audio = ALUtils.createBuffer(data);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}

		source = new AudioSource();
		source.setGain(1.0f);
		source.setPitch(1.0f);
		source.setPosition(0, 0, 0);
		source.setVelocity(0, 0, 0);
		source.attachBufferData(audio);
	}

	@Override
	protected void init() {
	}

	@Override
	protected void renderLoadingScreen() {

	}

	@Override
	protected void postInit() {
		source.play();
	}

	@Override
	protected boolean shouldRun() {
		try {
			return (char)System.in.read() != 'q';
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	protected void tick() {

	}

	@Override
	protected void render() {

	}

	@Override
	protected void postRender() {

	}

	@Override
	protected void shutdown() {
		source.destroy();
		ALUtils.shutdown();
	}

	public static void main(String[] args) {
		new TestAudio().run();
	}
}
