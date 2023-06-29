package volcano.summer.screen.clickgui.exeter;

public class Item {
	private final String label;
	protected float x;
	protected float y;
	protected int width;
	protected int height;

	public Item(final String label) {
		this.label = label;
	}

	public void setLocation(final float x, final float y) {
		this.x = x;
		this.y = y;
	}

	public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
	}

	public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
	}

	public void mouseReleased(final int mouseX, final int mouseY, final int releaseButton) {
	}

	public final String getLabel() {
		return this.label;
	}

	public float getX() {
		return this.x;
	}

	public float getY() {
		return this.y;
	}

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}

	public void setWidth(final int width) {
		this.width = width;
	}

	public void setHeight(final int height) {
		this.height = height;
	}
}