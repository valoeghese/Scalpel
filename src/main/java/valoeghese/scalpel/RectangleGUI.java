package valoeghese.scalpel;

import valoeghese.scalpel.gui.GUI;

public class RectangleGUI extends GUI {
	public RectangleGUI(int texture, float xSize, float ySize) {
		super(texture);

		this.protoVertices = new float[][]{
				{-xSize, ySize},
				{-xSize, -ySize},
				{xSize, ySize},
				{xSize, -ySize}
		};
	}

	protected float[][] protoVertices;

	public void setPosition(float xOffset, float yOffset, float windowAspect) {
		this.destroy();

		float aspect = windowAspect;

		int tl = this.vertex(xOffset + aspect * this.protoVertices[0][0], yOffset + this.protoVertices[0][1], 0, 1);
		int bl = this.vertex(xOffset + aspect * this.protoVertices[1][0], yOffset + this.protoVertices[1][1], 0, 0);
		int tr = this.vertex(xOffset + aspect * this.protoVertices[2][0], yOffset + this.protoVertices[2][1], 1, 1);
		int br = this.vertex(xOffset + aspect * this.protoVertices[3][0], yOffset + this.protoVertices[3][1], 1, 0);

		this.tri(tl, bl, br);
		this.tri(tl, tr, br);

		this.generateBuffers();
	}
}
