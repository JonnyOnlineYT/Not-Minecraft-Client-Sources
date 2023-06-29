/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Mouse
 */
package ru.fluger.client.ui.button;

import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.SoundEvents;
import org.lwjgl.input.Mouse;
import ru.fluger.client.helpers.render.RoundedUtil;

public class GuiMainMenuButton
extends GuiButton {
    private int fade = 20;
    public boolean ok = false;

    public GuiMainMenuButton(int buttonId, int x, int y, String buttonText) {
        this(buttonId, x, y, 200, 35, buttonText);
    }

    public GuiMainMenuButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
        super(buttonId, x, y, widthIn, heightIn, buttonText);
    }

    public GuiMainMenuButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText, boolean ok) {
        super(buttonId, x, y, widthIn, heightIn, buttonText);
        this.ok = ok;
    }

    public static int getMouseX() {
        return Mouse.getX() * sr.getScaledWidth() / Minecraft.getMinecraft().displayWidth;
    }

    public static int getMouseY() {
        return sr.getScaledHeight() - Mouse.getY() * sr.getScaledHeight() / Minecraft.getMinecraft().displayHeight - 1;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float mouseButton) {
        if (this.visible) {
            this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height + 10;
            Color text = new Color(-1);
            Color color = new Color(0, 100, 255, 200);
            if (this.hovered) {
                if (this.fade < 100) {
                    this.fade += 8;
                }
            } else if (this.fade > 20) {
                this.fade -= 8;
            }
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.blendFunc(770, 771);
            if (!this.ok) {
                RoundedUtil.drawRound(this.xPosition, this.yPosition, this.width, this.height + 5, 3.0f, color);
            }
            if (this.ok) {
                RoundedUtil.drawGradientRound(this.xPosition, this.yPosition, this.width, this.height + 5, 3.0f, new Color(90, 0, 200), new Color(100, 0, 255), new Color(40, 50, 220), new Color(40, 40, 255));
            }
            mc.fontRenderer.drawCenteredString(this.displayString, (float)this.xPosition + (float)this.width / 2.0f, (float)(this.yPosition + this.height / 2) + 1.0f, text.getRGB());
            this.mouseDragged(mc, mouseX, mouseY);
        }
    }

    @Override
    protected void mouseDragged(Minecraft mc, int mouseX, int mouseY) {
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY) {
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        return this.enabled && this.visible && mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height + 10;
    }

    @Override
    public boolean isMouseOver() {
        return this.hovered;
    }

    @Override
    public void drawButtonForegroundLayer(int mouseX, int mouseY) {
    }

    @Override
    public void playPressSound(SoundHandler soundHandlerIn) {
        soundHandlerIn.playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0f));
    }

    @Override
    public int getButtonWidth() {
        return this.width;
    }

    @Override
    public void setWidth(int width) {
        this.width = width;
    }
}

