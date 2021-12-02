package valoeghese.scalpel.scene;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.opengl.GL30.*;

public class VertexFormat {
	private VertexFormat(VertexFormat.Builder builder) {
		this.format = builder.entries.toArray(Entry[]::new);
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
	 * @return a copy of the array of entries in this vertex format. This is the specification for the format, as given in the builder which created this format.
	 */
	public Entry[] getEntries() {
		Entry[] result = new Entry[this.format.length];
		System.arraycopy(this.format, 0, result, 0, result.length);
		return result;
	}

	/**
	 * Applies this vertex format to the current buffer object.
	 */
	void applyFormat() {
		for (int index = 0; index < this.format.length; ++index) {
			this.format[index].vertexAttribPointer(index, this.stride);
			glEnableVertexAttribArray(index);
		}
	}

	private static Entry createEntry(int type, int size, int pointer, boolean normalised) {
		switch (type) {
		case GL_BYTE:
		case GL_UNSIGNED_BYTE:
		case GL_SHORT:
		case GL_UNSIGNED_SHORT:
		case GL_INT:
		case GL_UNSIGNED_INT:
			return new IEntry(type, size, pointer, normalised);
		case GL_HALF_FLOAT:
		case GL_FLOAT:
		case GL_DOUBLE: // in OpenGL 4 this will be an LEntry, and use glVertexAttribLPointer
			return new Entry(type, size, pointer, normalised);
		case 0x140C:
			throw new IllegalArgumentException("OpenGL 4.1 (which adds GL_FIXED) is not currently supported, however, feel free to open a PR to support the latest GL features.");
		default: throw new IllegalArgumentException("Unknown GL Type " + type + ". If you believe this is an error with Scalpel, please check if it has been fixed in a newer version, and if not, open an issue!");
		}
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

	public static class Builder {
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
			this.entries.add(createEntry(type, size, this.stride, normalised)); // the current stride is the offset for the pointer.
			this.stride += nBytes(type) * size;
			return this;
		}

		public VertexFormat build() {
			return new VertexFormat(this);
		}
	}

	private static class IEntry extends Entry {
		IEntry(int type, int size, int pointer, boolean normalised) {
			super(type, size, pointer, normalised);
		}

		@Override
		void vertexAttribPointer(int index, int stride) {
			glVertexAttribIPointer(index, this.size, this.type, stride, this.pointer);
		}
	}

	public static class Entry {
		private Entry(int type, int size, int pointer, boolean normalised) {
			this.type = type;
			this.size = size;
			this.pointer = pointer;
			this.normalised = normalised;
		}

		final int type;
		final int size;
		final int pointer;
		final boolean normalised;

		/**
		 * @return the OpenGL type of this entry. For example, {@code GL_FLOAT}.
		 */
		public int getType() {
			return this.type;
		}

		/**
		 * @return the size of this entry. This determines the number of the given type in this entry. For example, a size 3 float represents a {@code vec3} in GLSL.
		 */
		public int getSize() {
			return this.size;
		}

		/**
		 * @return the offset of this entry in the vertex buffer object data.
		 */
		public int getPointer() {
			return this.pointer;
		}

		/**
		 * @return whether OpenGL should normalise this entry's data.
		 */
		public boolean isNormalised() {
			return this.normalised;
		}

		void vertexAttribPointer(int index, int stride) {
			glVertexAttribPointer(index, this.size, this.type, this.normalised, stride, this.pointer);
		}
	}
}
