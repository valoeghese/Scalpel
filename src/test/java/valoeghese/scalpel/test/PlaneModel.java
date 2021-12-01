package valoeghese.scalpel.test;

import org.lwjgl.opengl.GL30;
import valoeghese.scalpel.Shader;
import valoeghese.scalpel.scene.Model;
import valoeghese.scalpel.scene.VertexBufferBuilder;
import valoeghese.scalpel.scene.VertexFormat;
import valoeghese.scalpel.util.ResourceLoader;
import valoeghese.scalpel.util.TextureLoader;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class PlaneModel extends Model {
	public PlaneModel() {
		super(GL30.GL_STATIC_DRAW);

		this.setVertexFormat(POS_UV);

		VertexBufferBuilder builder = new VertexBufferBuilder();
		int tl = builder.pos(-0.5f, 0.5f, -0.5f).uv(0.0f, 1.0f).next();
		int bl = builder.pos(-0.5f, -0.5f, -0.5f).uv(0.0f, 0.0f).next();
		int tr = builder.pos(0.5f, 0.5f, -0.5f).uv(1.0f, 1.0f).next();
		int br = builder.pos(0.5f, -0.5f, -0.5f).uv(1.0f, 0.0f).next();

		this.tri(tl, bl, br);
		this.tri(tl, tr, br);
		this.genVertexArrays(builder.getBuffer().flip());
	}

	private static final VertexFormat POS_UV = new VertexFormat.Builder()
			.add(GL30.GL_FLOAT, 3) // pos
			.add(GL30.GL_FLOAT, 2) // uv
			.build();
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
