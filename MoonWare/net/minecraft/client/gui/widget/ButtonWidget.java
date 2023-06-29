package net.minecraft.client.gui.widget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.Gui;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.Namespaced;
import optifine.Config;
import org.moonware.client.feature.impl.visual.util.RenderUtils2;
import org.moonware.client.helpers.Utils.RoundedUtil;
import org.moonware.client.utils.MWFont;

import java.awt.*;
import java.util.function.Consumer;

public class ButtonWidget extends Gui {
    protected static final Namespaced BUTTON_TEXTURES = new Namespaced("textures/gui/widgets.png");
    @Deprecated public int id;
    public int x;
    public int y;
    public int width;
    public int height;
    public String text;
    public Consumer<ButtonWidget> handler;
    public boolean enabled = true;
    public boolean visible = true;
    @Deprecated protected boolean hovered;
    @Deprecated
    public ButtonWidget(int id, int x, int y, String text) {
        this(id, x, y, 200, 20, text);
    }

    @Deprecated
    public ButtonWidget(int id, int x, int y, int width, int height, String text) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.text = text;
    }

    public ButtonWidget(int x, int y, String text, Consumer<ButtonWidget> handler) {
        this(x, y, 200, 20, text, handler);
    }

    public ButtonWidget(int x, int y, int width, int height, String text, Consumer<ButtonWidget> handler) {
        id = Integer.MIN_VALUE;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.text = text;
        this.handler = handler;
    }

    /**
     * Returns 0 if the button is disabled, 1 if the mouse is NOT hovering over this button and 2 if it IS hovering over
     * this button.
     */
    protected int getHoverState(boolean mouseOver) {
        int i = 1;
        if (!enabled) {
            i = 0;
        } else if (mouseOver) {
            i = 2;
        }
        return i;
    }

    public void draw(Minecraft mc, int mouseX, int mouseY, float partialTick) {
        if (!visible) return;
        hovered = (mouseX >= x && mouseY >= (y) && mouseX < x + width && mouseY < (y) + height);
        if (Config.isFastRender()) {
            if (hovered) {
                Gui.drawRect(x, y, x + width, y + height, 0x64141414);
            } else {
                Gui.drawRect(x, y, x + width, y + height, 0x500A0A0A);
            }
        } else if (hovered) {
            RenderUtils2.drawBlur(15,() -> RoundedUtil.drawGradientRound(x, y, width, height, 2, new Color(20, 20, 20, 100), new Color(20, 20, 20, 100), new Color(20, 20, 20, 100), new Color(20, 20, 20, 100)));
            RoundedUtil.drawGradientRound(x, y, width, height, 2, new Color(20, 20, 20, 100), new Color(20, 20, 20, 100), new Color(20, 20, 20, 100), new Color(20, 20, 20, 100));
        } else {
            RenderUtils2.drawBlur(15,() -> RoundedUtil.drawGradientRound(x, y + 1, width, height - 1, 2, new Color(20, 20, 20, 100), new Color(20, 20, 20, 100), new Color(20, 20, 20, 100), new Color(20, 20, 20, 100)));
            RoundedUtil.drawGradientRound(x, y, width, height, 2, new Color(10, 10, 10, 80), new Color(10, 10, 10, 80), new Color(10, 10, 10, 80), new Color(10, 10, 10, 80));
        }
        MWFont.MONTSERRAT_REGULAR.get(18).drawCenter(text, x + width / 2D, y + (height - 7.5D) / 2D, 0xFFD7D7D7);
        mouseDragged(mc, mouseX, mouseY);
    }

    @Deprecated
    protected void mouseDragged(Minecraft mc, int mouseX, int mouseY) {
    }

    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        if (!enabled || !visible || mouseX < x || mouseY < y || mouseX >= x + width || mouseY >= y + height) return false;
        if (handler != null) handler.accept(this);
        return true;
    }

    public void mouseReleased(int mouseX, int mouseY) {}

    @Deprecated
    public boolean isMouseOver() {
        return hovered;
    }

    @Deprecated
    public void drawButtonForegroundLayer(int mouseX, int mouseY) {
    }

    @Deprecated
    public void playPressSound(SoundHandler soundHandlerIn) {
        soundHandlerIn.playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
    }
}