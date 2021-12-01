package valoeghese.scalpel.scene;

import valoeghese.scalpel.util.BufferBuilder;

public class VertexBufferBuilder extends BufferBuilder {
	public VertexBufferBuilder() {
		super();
	}

	private int index;

	public VertexBufferBuilder(int allocatedSize) {
		super(allocatedSize);
	}

	public VertexBufferBuilder add(byte value) {
		return (VertexBufferBuilder) super.add(value);
	}

	public VertexBufferBuilder add(short value) {
		return (VertexBufferBuilder) super.add(value);
	}

	public VertexBufferBuilder add(int value) {
		return (VertexBufferBuilder) super.add(value);
	}

	public VertexBufferBuilder add(float value) {
		return (VertexBufferBuilder) super.add(value);
	}

	public VertexBufferBuilder add(double value) {
		return (VertexBufferBuilder) super.add(value);
	}

	public VertexBufferBuilder add(long value) {
		return (VertexBufferBuilder) super.add(value);
	}

	public VertexBufferBuilder pos(int x, int y, int z) {
		super.expandIfNecessary(12);
		super.buffer.putInt(x);
		super.buffer.putInt(y);
		super.buffer.putInt(z);
		return this;
	}

	public VertexBufferBuilder pos(float x, float y, float z) {
		super.expandIfNecessary(12);
		super.buffer.putFloat(x);
		super.buffer.putFloat(y);
		super.buffer.putFloat(z);
		return this;
	}

	public VertexBufferBuilder uv(float u, float v) {
		super.expandIfNecessary(8);
		super.buffer.putFloat(u);
		super.buffer.putFloat(v);
		return this;
	}

	public int next() {
		return this.index++;
	}
}
