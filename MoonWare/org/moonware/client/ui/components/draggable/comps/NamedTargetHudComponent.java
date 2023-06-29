package org.moonware.client.ui.components.draggable.comps;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.util.math.MathHelper;
import org.moonware.client.helpers.Utils.RoundedUtil;
import org.moonware.client.ui.components.draggable.HudComponent;
import org.moonware.client.utils.FontStorage;
import org.moonware.client.utils.MWUtils;

import java.awt.*;

public abstract class NamedTargetHudComponent extends HudComponent {
    public transient String name;
    protected transient double hoverAnimation;
    public NamedTargetHudComponent(String name, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.name = name;
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTick) {
        super.draw(mouseX, mouseY, partialTick);
        hoverAnimation = MathHelper.clamp(hoverAnimation + partialTick * (MWUtils.isHovered(x, y, width, height, mouseX, mouseY) ? 0.05 : 0.03), 0, 1);
        int color = (int) (hoverAnimation * hoverAnimation * 255F) << 24 | 0x00FFFFFF;

        if(Minecraft.screen instanceof ChatScreen) {
            RoundedUtil.drawRoundOutline(x, y, width, height, 5, 1, new Color(31, 31, 31, 45), new Color(0x1BFFFFFF, true));
            FontStorage.robotoRegularFontRender.draw(name, x, y - 9, -1);
        }
    }
}
