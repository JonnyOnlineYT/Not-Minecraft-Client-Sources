package volcano.summer.screen.clickgui.exeter;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import volcano.summer.client.util.CustomFont;
import volcano.summer.client.util.R2DUtils;
import volcano.summer.client.value.Value;

public class BooleanButton extends Button {
	private Value property;

	public BooleanButton(final Value property) {
		super(property.getName());
		this.property = property;
		this.width = 15;
	}

	@Override
	public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
		R2DUtils.drawRect(this.x, this.y, this.x + this.width + 7.4f, this.y + this.height,
				this.getState() ? (this.isHovering(mouseX, mouseY) ? 0xFFEBE83B : 0xFFEBE83B)
						: (this.isHovering(mouseX, mouseY) ? -2007673515 : 290805077));
		ClickGui.getClickGui().guiFont.drawString(this.getLabel(), this.x + 2.3f, this.y - 1.7f,
				CustomFont.FontType.SHADOW_THIN, this.getState() ? -1 : -5592406);
	}

	@Override
	public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		if (this.isHovering(mouseX, mouseY)) {
			Minecraft.getMinecraft().getSoundHandler().playSound(
					PositionedSoundRecord.createPositionedSoundRecord(new ResourceLocation("random.click"), 1.0f));
		}
	}

	@Override
	public int getHeight() {
		return 14;
	}

	@Override
	public void toggle() {
		this.property.setValue(Boolean.valueOf(!((Boolean) this.property.getValue()).booleanValue()));
	}

	@Override
	public boolean getState() {
		return (boolean) this.property.getValue();
	}
}
