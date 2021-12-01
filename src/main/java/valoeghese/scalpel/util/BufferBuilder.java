package valoeghese.scalpel.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class BufferBuilder {
	public BufferBuilder() {
		this(1024);
	}

	public BufferBuilder(int allocatedSize) {
		this.buffer = ByteBuffer.allocateDirect(allocatedSize);
		this.buffer.order(ByteOrder.nativeOrder());
	}

	protected ByteBuffer buffer;

	protected void expandIfNecessary(int requiredNBytes) {
		if (this.buffer.remaining() < requiredNBytes) {
			// setup
			int limit = this.buffer.limit();
			int originalPos = this.buffer.position();
			ByteBuffer next = ByteBuffer.allocateDirect(limit << 1);
			next.order(ByteOrder.nativeOrder());

			// copy contents
			this.buffer.position(0);
			next.put(this.buffer);
			next.rewind();
			next.position(originalPos);

			// set
			this.buffer = next;
		}
	}

	public BufferBuilder add(byte value) {
		this.expandIfNecessary(1);
		this.buffer.put(value);
		return this;
	}

	public BufferBuilder add(short value) {
		this.expandIfNecessary(2);
		this.buffer.putShort(value);
		return this;
	}

	public BufferBuilder add(int value) {
		this.expandIfNecessary(4);
		this.buffer.putInt(value);
		return this;
	}

	public BufferBuilder add(float value) {
		this.expandIfNecessary(4);
		this.buffer.putFloat(value);
		return this;
	}

	public BufferBuilder add(double value) {
		this.expandIfNecessary(8);
		this.buffer.putDouble(value);
		return this;
	}

	public BufferBuilder add(long value) {
		this.expandIfNecessary(8);
		this.buffer.putLong(value);
		return this;
	}

	public ByteBuffer getBuffer() {
		return this.buffer;
	}
}
