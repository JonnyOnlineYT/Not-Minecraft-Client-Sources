package volcano.summer.screen.clickgui.exeter;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import volcano.summer.client.util.CustomFont;
import volcano.summer.client.util.R2DUtils;

public abstract class Panel {
	private final String label;
	private int x;
	private int y;
	private int x2;
	private int y2;
	private int width;
	private int height;
	private boolean open;
	public boolean drag;
	private final ArrayList<Item> items;

	public Panel(final String label, final int x, final int y, final boolean open) {
		this.items = new ArrayList<Item>();
		this.label = label;
		this.x = x;
		this.y = y;
		this.width = 88;
		this.height = 18;
		this.open = open;
		this.setupItems();
	}

	public abstract void setupItems();

	public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
		this.drag(mouseX, mouseY);
		final float totalItemHeight = this.open ? (this.getTotalItemHeight() - 2.0f) : 0.0f;
		R2DUtils.drawGradientRect(this.x, this.y - 1.5f, this.x + this.width, this.y + this.height - 6, 0xFFFFFFFF,
				-6710887);
		if (this.open) {
			R2DUtils.drawRect(this.x, this.y + 12.5f, this.x + this.width,
					this.open ? (this.y + this.height + totalItemHeight) : ((float) (this.y + this.height - 1)),
					1996488704);
		}
		ClickGui.getClickGui().guiFont.drawString(this.getLabel(), this.x + 3.0f, this.y - 4.0f,
				CustomFont.FontType.SHADOW_THIN, -1);
		if (this.open) {
			float y = this.getY() + this.getHeight() - 3.0f;
			for (final Item item : this.getItems()) {
				item.setLocation(this.x + 2.0f, y);
				item.setWidth(this.getWidth() - 4);
				item.drawScreen(mouseX, mouseY, partialTicks);
				y += item.getHeight() + 1.5f;
			}
		}
	}

	private void drag(final int mouseX, final int mouseY) {
		if (!this.drag) {
			return;
		}
		this.x = this.x2 + mouseX;
		this.y = this.y2 + mouseY;
	}

	public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
		if (mouseButton == 0 && this.isHovering(mouseX, mouseY)) {
			this.x2 = this.x - mouseX;
			this.y2 = this.y - mouseY;
			ClickGui.getClickGui().getPanels().forEach(panel -> {
				if (panel.drag) {
					panel.drag = false;
				}
				return;
			});
			this.drag = true;
			return;
		}
		if (mouseButton == 1 && this.isHovering(mouseX, mouseY)) {
			this.open = !this.open;
			Minecraft.getMinecraft().getSoundHandler().playSound(
					PositionedSoundRecord.createPositionedSoundRecord(new ResourceLocation("random.click"), 1.0f));
			return;
		}
		if (!this.open) {
			return;
		}
		this.getItems().forEach(item -> item.mouseClicked(mouseX, mouseY, mouseButton));
	}

	public void addButton(final Button button) {
		this.items.add(button);
	}

	public void mouseReleased(final int mouseX, final int mouseY, final int releaseButton) {
		if (releaseButton == 0) {
			this.drag = false;
		}
		if (!this.open) {
			return;
		}
		this.getItems().forEach(item -> item.mouseReleased(mouseX, mouseY, releaseButton));
	}

	public final String getLabel() {
		return this.label;
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	public void setOpen(final boolean open) {
		this.open = open;
	}

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}

	public boolean getOpen() {
		return this.open;
	}

	public final ArrayList<Item> getItems() {
		return this.items;
	}

	private boolean isHovering(final int mouseX, final int mouseY) {
		return mouseX >= this.getX() && mouseX <= this.getX() + this.getWidth() && mouseY >= this.getY()
				&& mouseY <= this.getY() + this.getHeight() - (this.open ? 2 : 0);
	}

	private float getTotalItemHeight() {
		float height = 0.0f;
		for (final Item item : this.getItems()) {
			height += item.getHeight() + 1.5f;
		}
		return height;
	}

	public void setX(final int dragX) {
		this.x = dragX;
	}

	public void setY(final int dragY) {
		this.y = dragY;
	}
}
