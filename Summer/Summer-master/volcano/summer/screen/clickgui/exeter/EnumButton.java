package volcano.summer.screen.clickgui.exeter;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import volcano.summer.client.util.CustomFont;
import volcano.summer.client.util.R2DUtils;
import volcano.summer.client.value.ModeValue;

public class EnumButton extends Button {
	private ModeValue property;

	public EnumButton(final ModeValue property) {
		super(property.getName());
		this.property = property;
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		R2DUtils.drawRect(this.x, this.y, this.x + this.width + 7.4F, this.y + this.height,

				isHovering(mouseX, mouseY) ? 0xFFEBE83B
						: getState() ? 0xFFEBE83B : isHovering(mouseX, mouseY) ? -1721964477 : 288568115);
		ClickGui.getClickGui().guiFont.drawString(
				String.format("%s§7 %s", new Object[] { getLabel(), this.property.getStringValue() }), this.x + 2.3F,
				this.y - 1.0F, CustomFont.FontType.SHADOW_THIN, getState() ? -1 : -5592406);
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		if (isHovering(mouseX, mouseY)) {
			Minecraft.getMinecraft().getSoundHandler().playSound(
					PositionedSoundRecord.createPositionedSoundRecord(new ResourceLocation("random.click"), 1.0F));
			if (mouseButton == 0) {
				this.property.next();
			} else if (mouseButton == 1) {
				this.property.next();
			}
		}
	}

	@Override
	public int getHeight() {
		return 14;
	}

	@Override
	public void toggle() {
	}

	@Override
	public boolean getState() {
		return true;
	}
}
