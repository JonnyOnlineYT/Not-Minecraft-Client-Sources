package org.moonware.client.ui.titlescreen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.ButtonWidget;
import org.moonware.client.helpers.Utils.RoundedUtil;
import org.moonware.client.utils.MWFont;

import java.awt.*;
import java.util.function.Consumer;

public class TitleButton extends ButtonWidget {
    public boolean selected;
    public TitleButton(int x, int y, boolean selected, String text, Consumer<ButtonWidget> handler) {
        super(x, y, text, handler);
        this.selected = selected;
    }

    public TitleButton(int x, int y, int width, int height, boolean selected, String text, Consumer<ButtonWidget> handler) {
        super(x, y, width, height, text, handler);
        this.selected = selected;
    }

    @Override
    public void draw(Minecraft mc, int mouseX, int mouseY, float partialTick) {
        if (!visible) return;
        hovered = (mouseX >= x && mouseY >= (y) && mouseX < x + width && mouseY < (y) + height);
        if (selected) {
            RoundedUtil.drawGradientHorizontal(x, y, width, height, 2, new Color(0, 200, 255), new Color(0, 128, 255));
        } else if (hovered) {
            RoundedUtil.drawGradientRound(x, y, width, height, 2, new Color(20, 20, 20, 100), new Color(20, 20, 20, 100), new Color(20, 20, 20, 100), new Color(20, 20, 20, 100));
        } else {
            RoundedUtil.drawGradientRound(x, y, width, height, 2, new Color(0, 0, 0, 115), new Color(10, 10, 10, 179), new Color(14, 11, 11, 119), new Color(14, 14, 14, 128));
        }
        if (selected) {
            MWFont.ELEGANT_ICONS.get(39).draw("E", x + width / 48F - 2, y + (height - 9.8f) / 2F - 2, 0xFFD7D7D7);
            MWFont.MONTSERRAT_BOLD.get(21).draw(text, x + width / 6F, y + (height - 6.0f) / 2F - 2, 0xFFD7D7D7);
        } else {
            MWFont.ELEGANT_ICONS.get(30).draw("a", x + width / 48F, y + (height - 9.8f) / 2F, 0xFFD7D7D7);
            MWFont.MONTSERRAT_BOLD.get(20).draw(text, x + width / 6F, y + (height - 6.0f) / 2F, 0xFFD7D7D7);
        }
    }
}
