package valoeghese.scalpel.scene;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL30.*;

public class VertexFormat<R> {
	private VertexFormat(VertexFormat.Builder builder) {
		this.format = (Entry[]) builder.entries.toArray(Entry[]::new);
		this.stride = builder.stride;
	}

	private final Entry[] format;
	private final int stride;

	/**
	 * @return the vertex size in bytes. This is identical to the stride length.
	 */
	public int getVertexSize() {
		return this.stride;
	}

	/**
	 * Applies this vertex format to the current buffer object.
	 */
	public void applyFormat() {
		for (int index = 0; index < this.format.length; ++index) {
			Entry attribute = this.format[index];
			glVertexAttribPointer(index, attribute.size, attribute.type, attribute.normalised, this.stride, attribute.pointer);
			glEnableVertexAttribArray(index);
		}

		glVertexAttribPointer(1, 2, GL_FLOAT, false, 4 * 6, 4 * 3);
		glEnableVertexAttribArray(1);
		glVertexAttribPointer(2, 1, GL_FLOAT, false, 4 * 6, 4 * 5);
		glEnableVertexAttribArray(2);
	}

	private static int nBytes(int type) throws IllegalArgumentException {
		switch (type) {
		case GL_BYTE:
		case GL_UNSIGNED_BYTE:
			return 1;
		case GL_SHORT:
		case GL_UNSIGNED_SHORT:
		case GL_HALF_FLOAT:
			return 2;
		case GL_INT:
		case GL_UNSIGNED_INT:
		case GL_FLOAT:
			return 4;
		case GL_DOUBLE:
			return 8;
		case 0x140C:
			throw new IllegalArgumentException("OpenGL 4.1 (which adds GL_FIXED) is not currently supported, however, feel free to open a PR to support the latest GL features.");
		default: throw new IllegalArgumentException("Unknown GL Type " + type + ". If you believe this is an error with Scalpel, please check if it has been fixed in a newer version, and if not, open an issue!");
		}
	}

	public static class Builder<E> {
		private final List<Entry> entries = new ArrayList<>();
		private int stride;

		/**
		 * @param type the glsl data type. For example, {@code GL_FLOAT} or {@code GL_INT}.
		 * @param size the number of the given type. For example, add({@code GL_FLOAT, 3}) creates a vec3.
		 * @return this vertex format object.
		 */
		public VertexFormat.Builder add(int type, int size) {
			return this.add(type, size, false);
		}

		/**
		 * @param type the glsl data type. For example, {@code GL_FLOAT} or {@code GL_INT}.
		 * @param size the number of the given type. For example, add({@code GL_FLOAT, 3}) creates a vec3.
		 * @param normalised whether OpenGL should normalise the data given for this vertex parameter, assuming it is a fixed point data type.
		 * @return this vertex format object.
		 */
		public VertexFormat.Builder add(int type, int size, boolean normalised) {
			this.entries.add(new Entry(type, size, this.stride, normalised)); // the current stride is the offset for the pointer.
			this.stride += nBytes(type) * size;
			return this;
		}

		public VertexFormat build() {
			return new VertexFormat(this);
		}
	}

	static class Entry {
		private Entry(int type, int size, int pointer, boolean normalised) {
			this.type = type;
			this.size = size;
			this.pointer = pointer;
			this.normalised = normalised;
		}

		final int type;
		final int size;
		private final int pointer;
		private final boolean normalised;
	}
}
