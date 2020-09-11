package valoeghese.scalpel.test;

import valoeghese.scalpel.Model;
import valoeghese.scalpel.Shader;
import valoeghese.scalpel.gui.GUI;
import valoeghese.scalpel.util.ResourceLoader;
import valoeghese.scalpel.util.TextureLoader;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class TestPlaneGUI extends GUI {
	public TestPlaneGUI(int mode) {
		super(mode);
		this.vertex(-0.5f, 0.5f, -0.5f, 0.0f, 1.0f); // tl
		this.vertex(-0.5f, -0.5f, -0.5f, 0.0f, 0.0f); // bl
		this.vertex(0.5f, 0.5f, -0.5f, 1.0f, 1.0f); // tr
		this.vertex(0.5f, -0.5f, -0.5f, 1.0f, 0.0f); // br
		this.tri(0, 1, 3);
		this.tri(0, 2, 3);
		this.generateBuffers();
	}

	public static final int TEXTURE_TO_USE;

	static {
		try {
			BufferedImage image = ImageIO.read(ResourceLoader.loadURL("assets/texture/misaka.png"));
			TEXTURE_TO_USE = TextureLoader.textureARGB(image);
		} catch (IOException | RuntimeException e) {
			throw new RuntimeException("Error loading image!", e);
		}
	}
}
