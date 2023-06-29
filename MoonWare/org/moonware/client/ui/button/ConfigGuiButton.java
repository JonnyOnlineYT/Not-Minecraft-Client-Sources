package org.moonware.client.ui.button;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.SoundEvents;
import org.lwjgl.input.Mouse;
import org.moonware.client.helpers.render.rect.RectHelper;

import java.awt.*;

public class ConfigGuiButton extends ButtonWidget {
    private static final ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
    private int fade = 20;

    public ConfigGuiButton(int buttonId, int x, int y, String buttonText) {
        this(buttonId, x, y, 200, 35, buttonText);
    }

    public ConfigGuiButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
        super(buttonId, x, y, widthIn, heightIn, buttonText);
    }

    public static int getMouseX() {
        return Mouse.getX() * sr.getScaledWidth() / Minecraft.width;
    }

    public static int getMouseY() {
        return sr.getScaledHeight() - Mouse.getY() * sr.getScaledHeight() / Minecraft.height - 1;
    }

    public void draw(Minecraft mc, int mouseX, int mouseY, float partialTick) {
        if (visible) {
            int height = this.height - 31;
            hovered = (mouseX >= x + 93 && mouseY >= (y - 41) && mouseX < x + width - 43 && mouseY < height + y - 30);
            Color text = new Color(155, 155, 155, 255);
            if (!enabled) {
            } else if (hovered) {
                if (fade < 100)
                    fade += 8;
                text = Color.white;
            } else {
                if (fade > 20)
                    fade -= 8;
            }
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.blendFunc(770, 771);

            RectHelper.drawSkeetButton(x + 125, y + 2, width + x - 75, height + y);
            Minecraft.font.drawCenteredString(this.text, x + width / 2F + 25, (y) + (this.height - 73), text.getRGB());

            mouseDragged(mc, mouseX, mouseY);
        }
    }

    protected void mouseDragged(Minecraft mc, int mouseX, int mouseY) {
    }

    public void mouseReleased(int mouseX, int mouseY) {

    }

    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        int height = this.height - 31;
        return (enabled && visible && (mouseX >= x + 93 && mouseY >= (y - 45) && mouseX < x + width - 43 && mouseY < height + y - 30));
    }

    public boolean isMouseOver() {
        return hovered;
    }

    public void drawButtonForegroundLayer(int mouseX, int mouseY) {
    }

    public void playPressSound(SoundHandler soundHandlerIn) {
        soundHandlerIn.playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
    }

    public int getButtonWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}