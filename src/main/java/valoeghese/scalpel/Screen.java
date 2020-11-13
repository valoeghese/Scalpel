package valoeghese.scalpel;

public abstract class Screen<T extends ScalpelApp> {
	public Screen(T game) {
		this.game = game;
	}

	protected final T game;

	public abstract void renderGUI();
	public abstract void handleMouseInput(double dx, double dy);
	public abstract void handleKeybinds();

	public void tick() {
	}

	public void onFocus() {
	}
}