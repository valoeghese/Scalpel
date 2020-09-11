package valoeghese.scalpel.test;

import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL20;
import valoeghese.scalpel.ScalpelApp;
import valoeghese.scalpel.Shader;
import valoeghese.scalpel.Window;
import valoeghese.scalpel.gui.GUI;
import valoeghese.scalpel.util.GLUtils;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;

public class TestRenderPlane extends ScalpelApp {
	private TestRenderPlane() {
		super(100 / 20);
	}

	private GUI gui;
	private Window window;
	private Shader shader;
	private final Matrix4f projection = new Matrix4f().ortho(-1.0f, 1.0f, -1.0f, 1.0f, -1.0f, 1.0f);

	@Override
	protected void preInit() {
		GLUtils.initGLFW();
		this.window = new Window("TestRenderPlane", 300, 300);
		GLUtils.initGL(this.window);
		this.shader = new Shader("assets/shader/gui_v.glsl", "assets/shader/gui_f.glsl");
		this.gui = new TestPlaneGUI(GL20.GL_STATIC_DRAW);
	}

	@Override
	protected void init() { // runs on a different thread so can't use gl
	}

	@Override
	protected void renderLoadingScreen() {
	}

	@Override
	protected void postInit() {
	}

	@Override
	protected boolean shouldRun() {
		return this.window.isOpen() && GLFW.glfwGetKey(this.window.id, GLFW.GLFW_KEY_ESCAPE) == GLFW.GLFW_RELEASE;
	}

	@Override
	protected void tick() {
	}

	@Override
	protected void render() {
		this.shader.bind();
		this.shader.uniformMat4f("projection", this.projection);
		glBindTexture(GL_TEXTURE_2D, TestPlaneGUI.TEXTURE_TO_USE);
		this.gui.render();
		glBindTexture(GL_TEXTURE_2D, 0);
		Shader.unbind();
	}

	@Override
	protected void postRender() {
		this.window.swapBuffers();
	}

	@Override
	protected void shutdown() {
		this.gui.destroy();
		this.window.destroy();
	}

	public static void main(String[] args) {
		new TestRenderPlane().run();
	}
}
