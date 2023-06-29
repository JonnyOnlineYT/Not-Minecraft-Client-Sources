package Celestial.ui.button;

import Celestial.utils.render.ClientHelper;
import Celestial.utils.render.RoundedUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.SoundEvents;
import org.lwjgl.input.Mouse;

import java.awt.*;

public class GuiMainMenuButton extends GuiButton {

    private int fade = 20;

    public GuiMainMenuButton(int buttonId, int x, int y, String buttonText) {
        this(buttonId, x, y, 200, 35, buttonText);
    }

    public GuiMainMenuButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
        super(buttonId, x, y, widthIn, heightIn, buttonText);
    }

    public static int getMouseX() {
        return Mouse.getX() * sr.getScaledWidth() / Minecraft.getMinecraft().displayWidth;
    }

    public static int getMouseY() {
        return sr.getScaledHeight() - Mouse.getY() * sr.getScaledHeight() / Minecraft.getMinecraft().displayHeight - 1;
    }

    public void drawButton(Minecraft mc, int mouseX, int mouseY, float mouseButton) {
        if (this.visible) {
            this.hovered = (mouseX >= this.xPosition && mouseY >= (this.yPosition) && mouseX < this.xPosition + this.width && mouseY < (this.yPosition) + this.height + 9);
            Color text = new Color(Color.white.getRGB());
            Color color = new Color(this.fade + 14, this.fade + 14, this.fade + 14, 200);
            if (this.hovered) {
                if (this.fade < 100)
                    this.fade += 8;
                text = Color.white;
            } else {
                if (this.fade > 20)
                    this.fade -= 8;
            }
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.blendFunc(770, 771);
            int height = this.height + 9;
            Color finalText = text;
            RoundedUtil.drawRound((float)this.xPosition, (float)this.yPosition, (float)this.width, (float)height, 5,  new Color(0, 0, 0, 0));

            mc.tenacity.drawCenteredStringWithShadow(this.displayString, this.xPosition + this.width / 2F, (this.yPosition) + (this.height - 3), text.getRGB());
            this.mouseDragged(mc, mouseX, mouseY);
        }
    }

    protected void mouseDragged(Minecraft mc, int mouseX, int mouseY) {
    }

    public void mouseReleased(int mouseX, int mouseY) {

    }

    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        return (this.enabled && this.visible && mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height + 10);
    }

    public boolean isMouseOver() {
        return this.hovered;
    }

    public void drawButtonForegroundLayer(int mouseX, int mouseY) {

    }

    public void playPressSound(SoundHandler soundHandlerIn) {
        soundHandlerIn.playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
    }

    public int getButtonWidth() {
        return this.width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}