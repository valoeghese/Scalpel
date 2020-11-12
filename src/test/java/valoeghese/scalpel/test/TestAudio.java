package valoeghese.scalpel.test;

import valoeghese.scalpel.ScalpelApp;
import valoeghese.scalpel.Window;
import valoeghese.scalpel.audio.AudioBuffer;
import valoeghese.scalpel.audio.AudioSource;
import valoeghese.scalpel.util.ALUtils;
import valoeghese.scalpel.util.GLUtils;

import java.io.IOException;
import java.io.UncheckedIOException;

public class TestAudio extends ScalpelApp {
	private TestAudio() {
		super(100 / 20);
	}

	AudioBuffer audio;
	AudioSource source;
	private Window window;

	@Override
	protected void preInit() {
		GLUtils.initGLFW();
		this.window = new Window("TestRenderPlane", 300, 300);
		GLUtils.initGL(this.window);

		ALUtils.initAL();

		ALUtils.setListenerPosition(0, 0, 0);
		ALUtils.setListenerVelocity(0, 0, 0);

		try {
			audio = ALUtils.createBuffer("assets/sound/Test_Sound.ogg");
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
		return this.window.isOpen();
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
		this.window.destroy();
	}

	public static void main(String[] args) {
		new TestAudio().run();
	}
}
