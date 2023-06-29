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
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import ru.fluger.client.Fluger;
import ru.fluger.client.TextureEngine;

public class ImgButton
extends GuiButton {
    private ResourceLocation loc;
    public TextureEngine texture;

    public ImgButton(ResourceLocation loc, int buttonId, int x, int y, String buttonText) {
        this(loc, buttonId, x, y, 200, 35, buttonText);
        this.loc = loc;
        this.texture = new TextureEngine(loc, Fluger.scale, 32, 32);
    }

    public ImgButton(ResourceLocation loc, int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
        super(buttonId, x, y, widthIn, heightIn, buttonText);
        this.loc = loc;
        this.texture = new TextureEngine(loc, Fluger.scale, 32, 32);
    }

    public ImgButton(ResourceLocation loc, int buttonId, int x, int y, int widthIn, int heightIn, String buttonText, boolean ok) {
        super(buttonId, x, y, widthIn, heightIn, buttonText);
        this.loc = loc;
        this.texture = new TextureEngine(loc, Fluger.scale, 32, 32);
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
            this.texture.bind(this.xPosition, this.yPosition);
            mc.fontRenderer.drawString(this.displayString, (float)this.xPosition + (float)this.texture.getWidth() - 8.0f, (float)(this.yPosition + this.height / 2) + 1.0f, text.getRGB());
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

