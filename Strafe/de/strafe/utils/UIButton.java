package de.strafe.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;

import java.awt.*;

public class UIButton extends GuiButton {

    private int fade;

    public UIButton(final int buttonId, final int x, final int y, final String buttonText) {
        this(buttonId, x, y, 200, 20, buttonText);
    }

    public UIButton(final int buttonId, final int x, final int y, final int widthIn, final int heightIn, final String buttonText) {
        super(buttonId, x, y, widthIn, heightIn, buttonText);
    }

    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if (this.visible) {
            this.hovered = (mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height);
            if (!hovered) {
                if (this.fade != 100) {
                    this.fade += 5;
                }
            }
        } else {
            if (this.fade <= 40) {
                return;
            }
            if (this.fade != 70) {
                this.fade -= 5;
            }

        }
        final Color a = new Color(0, 0, 0, this.fade);
        Gui.drawRect(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition + this.height, a.getRGB());
        this.drawCenteredString(mc.fontRendererObj, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, 0x4D0894);
    }

    @Override
    protected void drawGradientRect(int mouseX, int mouseY) {
    }

}

