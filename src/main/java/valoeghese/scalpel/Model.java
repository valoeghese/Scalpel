package valoeghese.scalpel;

import it.unimi.dsi.fastutil.floats.FloatArrayList;
import it.unimi.dsi.fastutil.floats.FloatList;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import org.joml.Matrix4f;
import valoeghese.scalpel.Shader;
import valoeghese.scalpel.util.GLUtils;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.*;

public abstract class Model {
	protected Model(int mode, @Nullable Shader shader) {
		this.mode = mode;
		this.shader = shader;
	}

	private FloatList vTemp = new FloatArrayList();
	private int vTempIndex = 0;
	private IntList iTemp = new IntArrayList();
	private List<VertexArray> vertexArrays = new ArrayList<>();
	private final int mode;
	@Nullable
	private final Shader shader;
	private VertexFormat vertexFormat;

	protected void setVertexFormat(VertexFormat format) {
		this.vertexFormat = format;
	}

	@Deprecated
	protected int vertex(float x, float y, float z, float u, float v) {
		return this.vertex(x, y, z, u, v, 1.0f);
	}

	@Deprecated
	protected int vertex(float x, float y, float z, float u, float v, float light) {
		this.vTemp.add(x);
		this.vTemp.add(y);
		this.vTemp.add(z);
		this.vTemp.add(u);
		this.vTemp.add(v);
		this.vTemp.add(light);
		this.vertexFormat = LEGACY;
		return this.vTempIndex++;
	}

	protected void tri(int i0, int i1, int i2) {
		this.iTemp.add(i0);
		this.iTemp.add(i1);
		this.iTemp.add(i2);
	}

	protected void generateBuffers() {
		float[] vertices = this.vTemp.toFloatArray();
		int[] indices = this.iTemp.toIntArray();
		this.vTemp = new FloatArrayList();
		this.iTemp = new IntArrayList();

		int vbo = glGenBuffers();
		int ebo = glGenBuffers();
		int vao = glGenVertexArrays();

		glBindVertexArray(vao);

		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBufferData(GL_ARRAY_BUFFER, vertices, this.mode);

		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, this.mode);

		this.vertexFormat.applyFormat();
		glBindVertexArray(0);

		this.vertexArrays.add(new VertexArray(vbo, ebo, vao, indices.length));
	}

	public void destroy() {
		for (VertexArray array : this.vertexArrays) {
			glDeleteVertexArrays(array.vao);
			glDeleteBuffers(array.ebo);
			glDeleteBuffers(array.vbo);
		}

		this.vertexArrays = new ArrayList<>();

		this.vTempIndex = 0;
	}

	public final void render(Matrix4f transform) {
		if (this.shader != null) {
			this.shader.uniformMat4f("transform", transform);
		}

		for (VertexArray array : this.vertexArrays) {
			glBindVertexArray(array.vao);
			glDrawElements(GL_TRIANGLES, array.elementCount, GL_UNSIGNED_INT, GLUtils.NULL);
		}

		unbind();
	}

	/**
	 * @return the {@linkplain VertexArray vertex array} objects containing the opengl object ids.
	 * @apiNote only use this if you KNOW WHAT YOU ARE DOING. This allows more direct access to precise OpenGL calls.
	 */
	public Iterable<VertexArray> getOpenglHandles() {
		return this.vertexArrays;
	}

	/**
	 * @return the {@linkplain VertexArray vertex array} object containing the opengl object ids stored at this position in this model.
	 * @apiNote only use this if you KNOW WHAT YOU ARE DOING. This allows more direct access to precise OpenGL calls.
	 */
	public VertexArray getHandleAt(int index) throws ArrayIndexOutOfBoundsException {
		return this.vertexArrays.get(index);
	}

	/**
	 * Removes and destroys the {@linkplain VertexArray vertex array} object at this position.
	 * @apiNote only use this if you KNOW WHAT YOU ARE DOING. This allows more direct access to precise OpenGL calls.
	 */
	public void destroyHandleAt(int index) {
		VertexArray array = this.vertexArrays.remove(index);
		glDeleteVertexArrays(array.vao);
		glDeleteBuffers(array.ebo);
		glDeleteBuffers(array.vbo);
	}

	@Nullable
	public Shader getShader() {
		return this.shader;
	}

	public static final void unbind() {
		glBindVertexArray(0);
	}

	private static final VertexFormat LEGACY = new VertexFormat.Builder()
			.add(GL_FLOAT, 3)
			.add(GL_FLOAT, 2)
			.add(GL_FLOAT, 1)
			.build();
	/**
	 * A container containing
	 */
	public static class VertexArray {
		private VertexArray(int vbo, int ebo, int vao, int elementCount) {
			this.vbo = vbo;
			this.ebo = ebo;
			this.vao = vao;
			this.elementCount = elementCount;
		}

		private final int vbo;
		private final int ebo;
		private final int vao;
		private final int elementCount;

		/**
		 * @return the vertex buffer object opengl handle.
		 */
		public int getVBOHandle() {
			return this.vbo;
		}

		/**
		 * @return the index buffer object opengl handle.
		 */
		public int getEBOHandle() {
			return this.ebo;
		}

		/**
		 * @return the array buffer object opengl handle.
		 */
		public int getVAOHandle() {
			return this.vao;
		}

		/**
		 * @return the length of the indices buffer object array.
		 */
		public int getElementCount() {
			return this.elementCount;
		}
	}
}
