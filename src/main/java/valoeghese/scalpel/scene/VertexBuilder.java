package valoeghese.scalpel.scene;

import valoeghese.scalpel.util.BufferBuilder;

import java.nio.ByteBuffer;

public class VertexBuilder<E> {
	VertexBuilder(VertexFormat.Entry[] format) {
	}

	private final BufferBuilder parent = new BufferBuilder();
	private int vTempIndex = 0;

	public ByteBuffer getBuffer() {
		return this.parent.getBuffer();
	}

	// =============================================
	//                    NODES
	// =============================================

	public static class FloatNode<T> {
		FloatNode(T next) {
			this.next = next;
		}

		T next;
		VertexBuilder builder;

		public T add(float f) {
			this.builder.parent.add(f);
			return this.next;
		}

		public int end(float f) {
			this.builder.parent.add(f);
			return this.builder.vTempIndex++;
		}
	}

	public static class IntNode<T> {
		IntNode(T next) {
			this.next = next;
		}

		T next;
		VertexBuilder builder;

		public T add(int i) {
			this.builder.parent.add(i);
			return this.next;
		}

		public int end(int i) {
			this.builder.parent.add(i);
			return this.builder.vTempIndex++;
		}
	}

	public static class DoubleNode<T> {
		DoubleNode(T next) {
			this.next = next;
		}

		T next;
		VertexBuilder builder;

		public T add(double d) {
			this.builder.parent.add(d);
			return this.next;
		}

		public int end(double d) {
			this.builder.parent.add(d);
			return this.builder.vTempIndex++;
		}
	}

	// TODO add more nodes.
}
