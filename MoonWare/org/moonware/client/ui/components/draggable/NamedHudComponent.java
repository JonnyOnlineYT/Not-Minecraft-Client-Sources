package org.moonware.client.ui.components.draggable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.util.math.MathHelper;
import org.moonware.client.utils.FontStorage;
import org.moonware.client.utils.MWUtils;
import org.moonware.client.helpers.Utils.RoundedUtil;

import java.awt.*;

public abstract class NamedHudComponent extends HudComponent {
    public transient String name;
    protected transient double hoverAnimation;
    public NamedHudComponent(String name, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.name = name;
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTick) {
        super.draw(mouseX, mouseY, partialTick);
        hoverAnimation = MathHelper.clamp(hoverAnimation + partialTick * (MWUtils.isHovered(x, y, width, height, mouseX, mouseY) ? 0.05 : -0.05), 0, 1);
        int color = (int) (hoverAnimation * hoverAnimation * 255F) << 24 | 0x00FFFFFF;
        RoundedUtil.drawRoundOutline(x, y, width, height, 5, 1, new Color(0, 0, 0, 0), new Color(color, true));
        FontStorage.robotoRegularFontRender.draw(name, x, y - 9, color);
    }
}
