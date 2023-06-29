package volcano.summer.screen.clickgui.one2.elements.theme;

import net.minecraft.client.Minecraft;
import volcano.summer.client.util.GuiUtils;
import volcano.summer.client.value.Value;
import volcano.summer.screen.clickgui.one2.elements.Element;

public class MiniToggle extends MiniElement {
	private Value value;

	public MiniToggle(final int posX, final int posY, final Value value, final Element parent) {
		super(posX, posY, 12 + Minecraft.getMinecraft().fontRendererObj.getStringWidth(value.getName()), 10, parent);
		this.value = value;
	}

	@Override
	public void draw(final int mouseX, final int mouseY) {
		final boolean isHover = mouseX >= this.getPosX() + this.dragX && mouseY >= this.getPosY() + this.dragY
				&& mouseX <= this.getPosX() + this.getWidth() + this.dragX
				&& mouseY <= this.getPosY() + this.getHeight() + this.dragY;
		GuiUtils.drawFineBorderedRect(this.getPosX() + this.dragX, this.getPosY() + this.dragY,
				this.getPosX() + this.getHeight() + this.dragX, this.getPosY() + this.getHeight() + this.dragY,
				isHover ? -1316805 : -12303292, -13882324);
		if ((boolean) this.value.getValue()) {
			GuiUtils.drawRect(this.getPosX() + 2.5f + this.dragX, this.getPosY() + 2.5f + this.dragY,
					this.getPosX() + this.getHeight() - 2 + this.dragX,
					this.getPosY() + this.getHeight() - 2 + this.dragY, -1316805);
		}
		Minecraft.getMinecraft().fontRendererObj.func_175063_a(this.value.getName(),
				this.getPosX() + this.getHeight() + 2 + this.dragX, this.getPosY() + 2 + this.dragY, -1);
	}

	@Override
	public void mouseClicked(final int mouseX, final int mouseY, final int button) {
		final boolean isHover = mouseX >= this.getPosX() + this.dragX && mouseY >= this.getPosY() + this.dragY
				&& mouseX <= this.getPosX() + this.getWidth() + this.dragX
				&& mouseY <= this.getPosY() + this.getHeight() + this.dragY;
		if (isHover && button == 0) {
			this.value.setValue(!(boolean) this.value.getValue());
		}
	}
}
