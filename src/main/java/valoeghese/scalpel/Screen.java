package valoeghese.scalpel;

/**
 * A simple class representing a screen in the program.
 */
public abstract class Screen<T> {
	public Screen(T app) {
		this.app = app;
	}

	protected final T app;

	public abstract void render(float tickDelta);

	/**
	 * For handling mouse movement.
	 */
	public abstract void handleMouseMovement(double dx, double dy);

	/**
	 * For handling keybinds.
	 */
	public abstract void handleKeybinds();

	public void tick() {
	}

	public void onFocus() {
	}
}