package valoeghese.scalpel;

import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallbackI;
import valoeghese.scalpel.audio.AudioBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static valoeghese.scalpel.util.GLUtils.NULL;

public class Window implements GLFWWindowSizeCallbackI {
	public Window(String title, int width, int height) {
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);

		if(System.getProperty("os.name").toLowerCase().contains("mac")){
			glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
			glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		}

		this.id = glfwCreateWindow(width, height,title, NULL, NULL);

		if (this.id == NULL) {
			throw new RuntimeException("Failed to greate GLFW window!");
		}

		glfwMakeContextCurrent(this.id);
		glfwSwapInterval(1); // vsync
		GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		glfwSetWindowPos(this.id, (vidMode.width() - width) / 2, (vidMode.height() - height) / 2);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
		glfwShowWindow(this.id);
		glfwSetWindowSizeCallback(this.id, this);

		this.width = width;
		this.height = height;
		this.aspect = (float) width / (float) height;
	}

	/**
	 * @deprecated use {@linkplain Window#getHandle()} instead.
	 */
	@Deprecated
	public final long id;
	/**
	 * @deprecated use {@linkplain Window#getWidth()} instead.
	 */
	@Deprecated
	public int width;
	/**
	 * @deprecated use {@linkplain Window#getHeight()} instead.
	 */
	@Deprecated
	public int height;
	/**
	 * @deprecated use {@linkplain Window#getAspect()} instead.
	 */
	@Deprecated
	public float aspect;

	public boolean isOpen() {
		return !glfwWindowShouldClose(this.id);
	}

	public void swapBuffers() {
		glfwSwapBuffers(this.id);
	}

	public void destroy() {
		glfwDestroyWindow(this.id);
	}

	@Override
	public void invoke(long window, int width, int height) {
		this.width = width;
		this.height = height;
		this.aspect = (float) width / (float) height;
	}

	public float[] getSelectedPositions() {
		double[] xbuf = new double[1];
		double[] ybuf = new double[1];
		org.lwjgl.glfw.GLFW.glfwGetCursorPos(this.id, xbuf, ybuf);

		float x = clampMap((float) xbuf[0], 0, this.width, -1.0f, 1.0f);
		float y = clampMap((float) ybuf[0], this.height, 0, -1.0f, 1.0f);
		return new float[] {x, y};
	}

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}

	/**
	 * @return the aspect of this window, computed as {@code (float)width / (float)height}.
	 */
	public float getAspect() {
		return this.aspect;
	}

	/**
	 * @return the underlying GLFW window object.
	 */
	public long getHandle() {
		return this.id;
	}

	private static float clampMap(float value, float min, float max, float newmin, float newmax) {
		value -= min;
		value /= (max - min);
		value = newmin + value * (newmax - newmin);

		if (value > newmax) {
			return newmax;
		} else if (value < newmin) {
			return newmin;
		} else {
			return value;
		}
	}
}
