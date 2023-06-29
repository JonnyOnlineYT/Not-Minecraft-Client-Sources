package org.moonware.client.ui.button;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.renderer.GlStateManager;
import org.moonware.client.utils.FontStorage;
import org.moonware.client.helpers.render.rect.DrawHelper;
import org.moonware.client.helpers.render2.RenderHelper2;

import java.awt.*;
import java.util.function.Consumer;

public class GuiAltButton extends ButtonWidget {

    private int fade = 20;
    private int fadeOutline = 20;
    private double scale;

    public GuiAltButton(int buttonId, int x, int y, String buttonText) {
        this(buttonId, x, y, 200, 20, buttonText);
        scale = 1.0;
    }

    public GuiAltButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
        super(buttonId, x, y, widthIn, heightIn, buttonText);
        scale = 1.0;
    }
    public GuiAltButton(int x, int y, int width, int height, String text, Consumer<ButtonWidget> handler) {
        super(x,y,width,height,text,handler);
        scale = 1.0;
    }
    public GuiAltButton(int buttonId, int x, int y, String buttonText,int scale) {
        this(buttonId, x, y, 200, 20, buttonText,scale);
        this.scale = scale;
    }

    public GuiAltButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText,int scale) {
        super(buttonId, x, y, widthIn, heightIn, buttonText);
        this.scale = scale;
    }

    public void draw(Minecraft mc, int mouseX, int mouseY, float partialTick) {
        if (visible) {
            GlStateManager.scale(scale,scale,scale);
            hovered = (mouseX >= x && mouseY >= (y) && mouseX < x + width && mouseY < (y) + height);
            Color text = new Color(215, 215, 215, 255);
            Color color = new Color(fade + 14, fade + 14, fade + 14, 120);
            if (hovered) {
                if (fade < 100) {
                    fade += 7;
                }
                text = Color.white;
            } else {
                if (fade > 20) {
                    fade -= 7;
                }
            }

            Color colorOutline = new Color(fade + 60, fade, fade, 0);
            if (hovered) {
                if (fadeOutline < 100) {
                    fadeOutline += 7;
                }
            } else {
                if (fadeOutline > 20) {
                    fadeOutline -= 7;
                }
            }
            DrawHelper.startSmooth();
            DrawHelper.enableGL2D();
            DrawHelper.enableSmoothLine(2);
            RenderHelper2.renderBlurredShadow(new Color(color.getRed(),color.getGreen(),color.getBlue(),255), x, y, width, height,7);
            DrawHelper.drawRoundedRect(x, y, x + width, y + height, 5, new Color(color.getRed(),color.getGreen(),color.getBlue(),255));
            DrawHelper.disableGL2D();
            DrawHelper.disableSmoothLine();
            DrawHelper.endSmooth();
            FontStorage.circleregular.drawCenter(this.text, x + width / 2F, y + (height - 7.5f) / 2F, text.getRGB());

        }
    }
}