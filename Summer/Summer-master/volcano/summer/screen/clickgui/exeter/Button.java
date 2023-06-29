package volcano.summer.screen.clickgui.exeter;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import volcano.summer.client.util.CustomFont;
import volcano.summer.client.util.R2DUtils;

public class Button extends Item {
	private boolean state;

	public Button(final String label) {
		super(label);
		this.height = 15;
	}

	@Override
	public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
		R2DUtils.drawGradientRect(this.x, this.y, this.x + this.width, this.y + this.height,
				this.getState() ? (this.isHovering(mouseX, mouseY) ? 0x90EBE83B : 0x90EBE83B)
						: (this.isHovering(mouseX, mouseY) ? -2007673515 : 861230421),
				this.getState() ? (this.isHovering(mouseX, mouseY) ? 0xFFEBE83B : 0xFFEBE83B)
						: (this.isHovering(mouseX, mouseY) ? -1722460843 : 1431655765));
		ClickGui.getClickGui().guiFont.drawString(this.getLabel(), this.x + 2.3f, this.y - 2.0f,
				CustomFont.FontType.SHADOW_THIN, this.getState() ? -1 : -5592406);
	}

	@Override
	public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
		if (mouseButton == 0 && this.isHovering(mouseX, mouseY)) {
			this.state = !this.state;
			this.toggle();
			Minecraft.getMinecraft().getSoundHandler().playSound(
					PositionedSoundRecord.createPositionedSoundRecord(new ResourceLocation("random.click"), 1.0f));
		}
	}

	public void toggle() {
	}

	public boolean getState() {
		return this.state;
	}

	@Override
	public int getHeight() {
		return 14;
	}

	protected boolean isHovering(final int mouseX, final int mouseY) {
		for (final Panel panel : ClickGui.getClickGui().getPanels()) {
			if (panel.drag) {
				return false;
			}
		}
		return mouseX >= this.getX() && mouseX <= this.getX() + this.getWidth() && mouseY >= this.getY()
				&& mouseY <= this.getY() + this.height;
	}
}
