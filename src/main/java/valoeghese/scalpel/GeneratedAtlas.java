package valoeghese.scalpel;

import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GeneratedAtlas {
	public GeneratedAtlas(String name, ImageEntry[] images) {
		this(name, images, 16, 16, 16, 16);
	}

	public GeneratedAtlas(String name, ImageEntry[] images, int componentWidth, int componentHeight, int entriesSizeX, int entriesSizeY) {
		final int atlasWidth = componentWidth * entriesSizeX;
		final int atlasHeight = componentHeight * entriesSizeY;

		int x = -1;
		int y = 0;
		this.image = new BufferedImage(atlasWidth, atlasHeight, BufferedImage.TYPE_INT_ARGB);

		for (ImageEntry iEntry : images) {
			BufferedImage image = iEntry.image;
			++x;

			if (x >= entriesSizeX) {
				x = 0;
				++y;
			}

			if (y >= entriesSizeY) {
				throw new RuntimeException("Generated Atlas \"" + name + "\" is too large!");
			}

			if (image.getWidth() != componentWidth || image.getHeight() != componentHeight) {
				throw new RuntimeException("Invalidly sized component image encountered while generating atlas \"" + name + "\"! (size [" + image.getWidth() + ", " + image.getHeight() + "] does not match atlas component size [" + componentWidth + ", " + componentHeight + "]");
			}

			this.imageLocationMap.put(iEntry.name, new ImageUV(x, y));
			int startX = x * componentWidth;
			int startY = (entriesSizeY - 1 - y) * componentHeight;

			for (int xo = 0; xo < componentWidth; ++xo) {
				int totalX = startX + xo;

				for (int yo = 0; yo < componentHeight; ++yo) {
					int totalY = startY + yo;

					this.image.setRGB(totalX, totalY, image.getRGB(xo, yo));
				}
			}
		}

		this.name = name;
		System.out.println("Successfully Generated Atlas \"" + name + "\"");
	}

	public final String name;
	public final BufferedImage image;
	public final Map<String, ImageUV> imageLocationMap = new HashMap<>();

	public void dumpImage(@Nullable File file) throws IOException {
		File toWriteAt = file == null ? new File("./temp_" + name + ".png") : file;
		ImageIO.write(this.image, "png", toWriteAt);
	}

	public static class ImageEntry {
		public String name;
		public BufferedImage image;
	}

	public static class ImageUV {
		private ImageUV(int x, int y) {
			this.x = x;
			this.y = y;
		}

		private int x;
		private int y;

		public int getX() {
			return this.x;
		}

		public int getY() {
			return this.y;
		}
	}
}
