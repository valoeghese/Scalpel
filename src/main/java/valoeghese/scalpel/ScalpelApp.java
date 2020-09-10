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

	protected abstract void preInit();
	protected abstract void init();
	protected abstract void renderLoadingScreen();
	protected abstract void postInit();
	protected abstract boolean shouldRun();
	protected abstract void tick();
	protected abstract void render();
	protected abstract void postRender();
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
