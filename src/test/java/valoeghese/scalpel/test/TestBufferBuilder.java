package valoeghese.scalpel.test;

import valoeghese.scalpel.util.BufferBuilder;

import java.nio.ByteBuffer;

public class TestBufferBuilder {
	public static void main(String[] args) {
		BufferBuilder builder = new BufferBuilder(6);
		builder.add(3);
		builder.add(5);

		ByteBuffer buffer = builder.getBuffer();
		buffer.rewind();

		for (int i = 0; i < 2; ++i)
			System.out.println(buffer.getInt());
	}
}
