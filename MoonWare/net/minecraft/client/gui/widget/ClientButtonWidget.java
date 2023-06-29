package net.minecraft.client.gui.widget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.Namespaced;
import optifine.Config;
import org.moonware.client.feature.impl.visual.util.RenderUtils2;
import org.moonware.client.helpers.Utils.RoundedUtil;
import org.moonware.client.utils.MWFont;

import java.awt.*;
import java.util.function.Consumer;

public class ClientButtonWidget extends ButtonWidget {
    protected static final Namespaced BUTTON_TEXTURES = new Namespaced("textures/gui/widgets.png");
    @Deprecated
    public int id;
    public int x;
    public int y;
    public int width;
    public int height;
    public String text;
    public Consumer<ButtonWidget> handler;
    public boolean enabled = true;
    public boolean visible = true;
    @Deprecated
    protected boolean hovered;

    @Deprecated
    public ClientButtonWidget(int id, int x, int y, String text) {
        this(id, x, y, 200, 20, text);
    }

    @Deprecated
    public ClientButtonWidget(int id, int x, int y, int width, int height, String text) {
        super(id,x,y,width,height,text);
        this.id = id;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.text = text;
    }

    public ClientButtonWidget(int x, int y, String text, Consumer<ButtonWidget> handler) {
        this(x, y, 200, 20, text, handler);
    }

    public ClientButtonWidget(int x, int y, int width, int height, String text, Consumer<ButtonWidget> handler) {
        super(x,y,width,height,text,handler);
        id = Integer.MIN_VALUE;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.text = text;
        this.handler = handler;
    }

    @Override
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
            RenderUtils2.drawBlur(15, () -> RoundedUtil.drawGradientRound(x, y, width, height, 2, new Color(20, 20, 20, 100), new Color(20, 20, 20, 100), new Color(20, 20, 20, 100), new Color(20, 20, 20, 100)));
            RoundedUtil.drawGradientRound(x, y, width, height, 2, new Color(20, 20, 20, 100), new Color(20, 20, 20, 100), new Color(20, 20, 20, 100), new Color(20, 20, 20, 100));
        } else {
            RenderUtils2.drawBlur(15, () -> RoundedUtil.drawGradientRound(x, y + 1, width, height - 1, 2, new Color(20, 20, 20, 100), new Color(20, 20, 20, 100), new Color(20, 20, 20, 100), new Color(20, 20, 20, 100)));
            RoundedUtil.drawGradientRound(x, y, width, height, 2, new Color(10, 10, 10, 80), new Color(10, 10, 10, 80), new Color(10, 10, 10, 80), new Color(10, 10, 10, 80));
        }
        MWFont.MONTSERRAT_BOLD.get(17).draw(text, x  +1, y + (height - 7.5D) / 2D + 1, 0xFFD7D7D7);
        mouseDragged(mc, mouseX, mouseY);
    }
}
