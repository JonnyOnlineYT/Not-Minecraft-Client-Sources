package volcano.summer.screen.mainmenu;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import volcano.summer.client.util.Colors;

public class GuiMenuButton extends GuiButton {
	private ResourceLocation icon;
	float targetX;
	float currentX;

	public GuiMenuButton(final int buttonId, final int x, final int y, final int widthIn, final int heightIn,
			final String buttonText) {
		super(buttonId, x, y, widthIn, heightIn, buttonText);
		this.icon = new ResourceLocation("textures/menu/" + this.displayString.toLowerCase() + ".png");
	}

	@Override
	public void drawButton(final Minecraft mc, final int mouseX, final int mouseY) {
		if (this.visible) {
			this.hovered = (mouseX >= this.xPosition + this.width / 2 && mouseY >= this.yPosition
					&& mouseX < this.xPosition + this.width / 2 + 75 && mouseY < this.yPosition + 90);
			this.mouseDragged(mc, mouseX, mouseY);
			final int text = Colors.getColor(232);
			this.targetX = (this.hovered ? 10.0f : 0.0f);
			final float diff = (this.targetX - this.currentX) * 0.6f;
			this.currentX += diff;
			GlStateManager.pushMatrix();
			final float offset = this.xPosition + this.width / 2;
			GlStateManager.translate(offset, this.yPosition - this.currentX, 1.0f);
			GlStateManager.enableBlend();
			GlStateManager.enableAlpha();
			mc.getTextureManager().bindTexture(this.icon);
			Gui.drawModalRectWithCustomSizedTexture(0, 0, 0.0f, 0.0f, 75, 75, 75.0f, 75.0f);
			GlStateManager.func_179144_i(0);
			final FontRenderer font = mc.fontRendererObj;
			font.func_175063_a(this.displayString, 37.0f - font.getStringWidth(this.displayString) / 2.0f, 80.0f, text);
			GlStateManager.disableAlpha();
			GlStateManager.disableBlend();
			GlStateManager.popMatrix();
		}
	}

	@Override
	public boolean mousePressed(final Minecraft mc, final int mouseX, final int mouseY) {
		return this.visible && mouseX >= this.xPosition + this.width / 2 && mouseY >= this.yPosition
				&& mouseX < this.xPosition + this.width / 2 + 75 && mouseY < this.yPosition + 90;
	}
}
