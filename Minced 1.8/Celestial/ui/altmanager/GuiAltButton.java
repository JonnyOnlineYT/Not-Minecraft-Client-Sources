package Celestial.ui.altmanager;

import Celestial.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;

import java.awt.*;

public class GuiAltButton extends GuiButton {
    private int opacity = 40;

    public GuiAltButton(int buttonId, int x, int y, String buttonText) {
        this(buttonId, x, y, 200, 20, buttonText);
    }

    public GuiAltButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
        super(buttonId, x, y, widthIn, heightIn, buttonText);
    }

    public void drawButton(Minecraft mc, int mouseX, int mouseY, float mouseButton) {
        if (this.visible) {
            mc.getTextureManager().bindTexture(BUTTON_TEXTURES);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.hovered = (mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height);
            if (hovered) {
                if (this.opacity < 40) {
                    this.opacity += 1;
                }
            } else if (this.opacity > 22) {
                this.opacity -= 1;
            }

            boolean flag = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height; // Flag, tells if your mouse is hovering the button
            Color color = new Color(35, 35, 35, 73);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.blendFunc(770, 771);
            if (flag) {
                RenderUtils.drawRect(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition + this.height, new Color(opacity, opacity, opacity, 150).getRGB());
                mc.mntsb_15.drawCenteredString(displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - (int) 2) / 3, -1);
            } else {
                RenderUtils.drawOutlineRect(this.xPosition, this.yPosition, this.width, this.height, color, new Color(255, 255, 255, 1));
                mc.mntsb_15.drawCenteredString(displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - (int) 2) / 3, -1);
            }
            this.mouseDragged(mc, mouseX, mouseY);
        }
    }
}