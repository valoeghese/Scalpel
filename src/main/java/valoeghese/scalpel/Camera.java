package valoeghese.scalpel;

import org.joml.AxisAngle4f;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import static org.joml.Math.cos;
import static org.joml.Math.sin;

public class Camera {
	private Matrix4f view = new Matrix4f();
	private Vector3f pos = new Vector3f();
	private float pitch = 0.0f;
	private float yaw = 0.0f;

	public void translateScene(Vector3f translate) {
		this.pos = this.pos.add(translate);
		this.rebuildView();
	}

	public void rotateYaw(float yaw) {
		this.yaw += yaw;
		this.rebuildView();
	}

	public void rotatePitch(float pitch) {
		this.pitch += pitch;

		if (this.pitch < -NINETY_DEGREES) {
			this.pitch = -NINETY_DEGREES;
		} else if (this.pitch > NINETY_DEGREES) {
			this.pitch = NINETY_DEGREES;
		}
		this.rebuildView();
	}

	private void rebuildView() {
		this.view = new Matrix4f();
		this.view.rotate(new AxisAngle4f(this.yaw, 0.0f, 1.0f, 0.0f));
		this.view.rotate(new AxisAngle4f(this.pitch, -sin(this.yaw - NINETY_DEGREES), 0.0f, cos(this.yaw - NINETY_DEGREES)));
		this.view.translate(this.pos);
	}

	/**
	 * @deprecated in favour of the {@linkplain valoeghese.scalpel.scene.Model new model system introduced in scalpel 1.3.0}. Instead, get the view with {@link Camera#getView}, set the shader transform and view with {@link Shader#uniformMat4f(String, Matrix4f)}, and then render the model.
	 */
	@Deprecated
	public void render(Model model, Matrix4f transform) {
		model.getShader().uniformMat4f("view", this.view);
		model.render(transform);
	}

	public float getYaw() {
		return this.yaw;
	}

	public float getPitch() {
		return this.pitch;
	}

	public Matrix4f getView() {
		return this.view;
	}

	public void wrapYaw() {
		double twopi = 2 * Math.PI;

		while (this.yaw > twopi) {
			this.yaw -= twopi;
		}

		while (this.yaw < 0) {
			this.yaw += twopi;
		}
	}

	private static final float NINETY_DEGREES = (float) Math.toRadians(90);

	public Vector3f getDirection() {
		double x = Math.sin(yaw) * Math.cos(pitch);
		double y = -Math.sin(pitch);
		double z = -Math.cos(yaw) * Math.cos(pitch);

		return new Vector3f((float) x, (float) y, (float) z);
	}

	public void setPitch(float f) {
		this.pitch = f;
	}

	public void setYaw(float f) {
		this.yaw = f;
	}

	public void setPos(float x, float y, float z) {
		this.pos = new Vector3f(x, y, z);
		this.rebuildView();
	}
}
