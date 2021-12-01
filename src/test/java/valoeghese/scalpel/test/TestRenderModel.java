package valoeghese.scalpel.test;

import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFW;
import valoeghese.scalpel.ScalpelApp;
import valoeghese.scalpel.Shader;
import valoeghese.scalpel.Window;
import valoeghese.scalpel.scene.Model;
import valoeghese.scalpel.util.GLUtils;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;

public class TestRenderModel extends ScalpelApp {
	private TestRenderModel() {
		super(100 / 20);
	}

	private Model model;
	private final Matrix4f transform = new Matrix4f();
	private Window window;
	private Shader shader;
	private final Matrix4f projection = new Matrix4f().ortho(-1.0f, 1.0f, -1.0f, 1.0f, -1.0f, 1.0f);

	@Override
	protected void preInit() {
		GLUtils.initGLFW();
		this.window = new Window("TestRenderPlaneModel", 300, 300);
		GLUtils.initGL(this.window);
		this.shader = new Shader("assets/shader/gui_v.glsl", "assets/shader/gui_f.glsl");
		this.model = new PlaneModel(this.shader);
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
	protected void render(float tickDelta) {
		this.shader.bind();
		this.shader.uniformMat4f("projection", this.projection);
		glBindTexture(GL_TEXTURE_2D, PlaneModel.TEXTURE_TO_USE);
		this.model.render(this.transform);
		glBindTexture(GL_TEXTURE_2D, 0);
		Shader.unbind();
	}

	@Override
	protected void postRender() {
		this.window.swapBuffers();
	}

	@Override
	protected void shutdown() {
		this.model.destroy();
		this.window.destroy();
	}

	public static void main(String[] args) {
		new TestRenderModel().run();
	}
}
