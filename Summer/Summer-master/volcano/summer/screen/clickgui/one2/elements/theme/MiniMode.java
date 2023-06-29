package volcano.summer.screen.clickgui.one2.elements.theme;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import volcano.summer.client.util.GuiUtils;
import volcano.summer.client.value.ModeValue;
import volcano.summer.screen.clickgui.one2.elements.Element;

public class MiniMode extends MiniElement {

	ModeValue value;

	public MiniMode(int posX, int posY, final ModeValue value, Element parent) {
		super(posX, posY, 92, 14, parent);
		this.value = value;
	}

	@Override
	public void draw(int mouseX, int mouseY) {
		final boolean isHover = mouseX >= this.getPosX() + this.dragX && mouseY >= this.getPosY() + this.dragY
				&& mouseX <= this.getPosX() + this.getWidth() + this.dragX
				&& mouseY <= this.getPosY() + this.getHeight() + this.dragY;
		GuiUtils.drawFineBorderedRect(this.getPosX() + this.dragX, this.getPosY() + this.dragY,
				this.getPosX() + this.getWidth() + this.dragX, this.getPosY() + this.getHeight() + this.dragY,
				isHover ? -1316805 : -12303292, -13882324);
		if (this.value.getValue() != null) {
			GuiUtils.drawCentredStringWithShadow(String.valueOf("Mode: " + this.value.getStringValue()),
					(int) (this.getPosX() + this.getWidth() / 2.0 + this.dragX), this.getPosY() + 4 + this.dragY, -1);
		}
	}

	@Override
	public void mouseClicked(final int mouseX, final int mouseY, final int button) {
		Minecraft.getMinecraft().getSoundHandler().playSound(
				PositionedSoundRecord.createPositionedSoundRecord(new ResourceLocation("random.click"), 1.0F));
		final boolean isHover = mouseX >= this.getPosX() + this.dragX && mouseY >= this.getPosY() + this.dragY
				&& mouseX <= this.getPosX() + this.getWidth() + this.dragX
				&& mouseY <= this.getPosY() + this.getHeight() + this.dragY;
		if (isHover && button == 0) {
			this.value.next();
		}
	}
}
