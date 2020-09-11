package valoeghese.scalpel;

import java.util.LinkedList;
import java.util.Queue;

import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;

public abstract class ScalpelApp implements Runnable {
	protected ScalpelApp(int tickDelta) {
		this.tickDelta = tickDelta;
	}

	protected final Queue<Runnable> later = new LinkedList<>();
	private long nextUpdate = 0;
	private final int tickDelta;

	@Override
	public void run() {
		this.preInit();

		Thread t = new Thread(this::init);
		t.start();

		while (t.isAlive()) {
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			// render loading screen
			this.renderLoadingScreen();
			glfwPollEvents();
		}

		this.postInit();

		while (this.shouldRun()) {
			long timeMillis = System.currentTimeMillis();

			if (timeMillis >= this.nextUpdate) {
				this.nextUpdate = timeMillis + this.tickDelta;
				this.tick();
			}

			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			this.render();
			glfwPollEvents();
			this.postRender();
		}

		this.shutdown();
	}

	/**
	 * Runs prior to {@link ScalpelApp#init()}, on the gl thread.
	 */
	protected abstract void preInit();
	/**
	 * Use this for non-gl related setup of your project. Runs off the main thread.
	 */
	protected abstract void init();
	/**
	 * Rendering on the GL thread while init() is being run. Remember to swap buffers at the end.
	 */
	protected abstract void renderLoadingScreen();
	/**
	 * Runs after {@link ScalpelApp#init()}, and before the app load, on the gl thread.
	 */
	protected abstract void postInit();
	/**
	 * Whether the app loop should continue running. Should reference {@link Window#isOpen()} if you wish for the close button to work.
	 */
	protected abstract boolean shouldRun();
	/**
	 * Called once every [specified amount of time] in the main loop - less often than app render. Runs on the main (gl) thread.
	 */
	protected abstract void tick();
	/**
	 * Called every time the game loop runs. Put your rendering here.
	 */
	protected abstract void render();
	/**
	 * Use this for post render stuff. Swapping window buffers goes here.
	 */
	protected abstract void postRender();
	/**
	 * Called after the game loop has finished running.
	 */
	protected abstract void shutdown();

	protected void runNextQueued() {
		Runnable task = null;

		synchronized (this.later) {
			if (!this.later.isEmpty()) {
				task = this.later.remove();
			}
		}

		if (task != null) {
			task.run();
		}
	}

	public void runLater(Runnable callback) {
		synchronized (this.later) {
			this.later.add(callback);
		}
	}

	public static ScalpelApp getInstance() {
		return instance;
	}

	private static ScalpelApp instance;
}
