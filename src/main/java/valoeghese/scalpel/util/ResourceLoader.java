package valoeghese.scalpel.util;

import org.lwjgl.BufferUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class ResourceLoader {
	public static URL loadURL(String location) {
		return ResourceLoader.class.getClassLoader().getResource(location);
	}

	public static InputStream load(String location) {
		return ResourceLoader.class.getClassLoader().getResourceAsStream(location);
	}

	public static String loadAsString(String location) throws IOException {
		InputStream is = load(location);
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();

		int nBytesRead;
		byte[] bufferBuffer = new byte[0x4000];

		while ((nBytesRead = is.read(bufferBuffer, 0, bufferBuffer.length)) != -1) {
			buffer.write(bufferBuffer, 0, nBytesRead);
		}

		is.close();
		return new String(buffer.toByteArray(), StandardCharsets.UTF_8);
	}

	public static ByteBuffer loadAsByteBuffer(String resource, int bufferSize) throws IOException { // https://www.youtube.com/watch?v=Mrcs9vIHSws
		ByteBuffer buffer;
		Path path = Paths.get(resource);

		if (Files.isReadable(path)) {
			try (SeekableByteChannel fc = Files.newByteChannel(path)) {
				buffer = BufferUtils.createByteBuffer((int) fc.size() + 1);
				int i = 0; // originally while (fc.read(buffer) != -1);

				do {
					i = fc.read(buffer);
				} while (i != -1);
			}
		} else {
			try (InputStream source = load(resource); ReadableByteChannel rbc = Channels.newChannel(source)) {
				buffer = BufferUtils.createByteBuffer(bufferSize);

				while (true) {
					int bytes = rbc.read(buffer);

					if (bytes == -1) {
						break;
					}

					if (buffer.remaining() == 0) { // extend buffer if too small lmao
						buffer.flip();
						buffer = BufferUtils.createByteBuffer(buffer.capacity() * 2).put(buffer);
					}
				}
			}
		}

		buffer.flip();
		return buffer;
	}
}
