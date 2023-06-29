package volcano.summer.screen.clickgui.one2.elements.base;

import net.minecraft.client.Minecraft;
import volcano.summer.client.util.GuiUtils;
import volcano.summer.screen.clickgui.one2.ClickGui;
import volcano.summer.screen.clickgui.one2.elements.Element;
import volcano.summer.screen.clickgui.one2.elements.Panel;

public class OnePanel extends Panel {
	public OnePanel(final String title, final int posX, final int posY, final boolean expanded, final boolean pinned,
			final ClickGui parent) {
		super(title, posX, posY, 100, 20, expanded, pinned, parent);
	}

	@Override
	public void draw(final int mouseX, final int mouseY) {
		this.updatePanel(mouseX, mouseY);
		GuiUtils.drawBorderedRect(this.getPosX() + this.dragX, this.getPosY() + this.dragY,
				this.getPosX() + this.getWidth() + this.dragX,
				this.getPosY() + (this.isExpanded() ? this.getOpenHeight() : this.getHeight()) + this.dragY,
				this.isPinned() ? -36752 : -11184811, -13421773);
		Minecraft.getMinecraft().fontRendererObj.func_175063_a(this.getTitle(), this.getPosX() + 5 + this.dragX,
				this.getPosY() + 7 + this.dragY, -1);
		if (this.isExpanded()) {
			for (final Element element : this.getElements()) {
				element.draw(mouseX, mouseY);
			}
		}
	}

	@Override
	public void updatePanelsElements() {
		int y = 20;
		for (final Element element : this.getElements()) {
			element.setPosY(this.getPosY() + y);
			if (element instanceof ElementButton) {
				final ElementButton button = (ElementButton) element;
				button.updateMiniElements();
				y += (button.isExpanded() ? (button.getOpenHeight() + 2) : (button.getHeight() + 2));
			} else {
				y += element.getHeight() + 2;
			}
		}
		this.setOpenHeight(y);
	}
}
