package org.moonware.client.ui.components.draggable;

import net.minecraft.client.Minecraft;
import net.minecraft.util.math.MathHelper;
import org.moonware.client.utils.MWUtils;

public abstract class HudComponent {
    public int x;
    public int y;
    public int width;
    public int height;
    public boolean visible = true;
    protected transient int dragX = -1;
    protected transient int dragY = -1;
    public HudComponent(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void draw(int mouseX, int mouseY, float partialTick) {
        x = MathHelper.clamp(x, 0, Minecraft.getScaledRoundedWidth() - width);
        y = MathHelper.clamp(y, 0, Minecraft.getScaledRoundedHeight() - height);
        if (dragX == -1 || dragY == -1) return;
        x = MathHelper.clamp(mouseX - dragX, 0, Minecraft.getScaledRoundedWidth() - width);
        y = MathHelper.clamp(mouseY - dragY, 0, Minecraft.getScaledRoundedHeight() - height);
    }

    public void mousePressed(int mouseX, int mouseY, int button) {
        if (button != 0 || dragX != -1 || dragY != -1 || !MWUtils.isHovered(x, y, width, height, mouseX, mouseY)) return;
        dragX = mouseX - x;
        dragY = mouseY - y;
    }

    public void mouseReleased(int mouseX, int mouseY, int button) {
        dragX = dragY = -1;
    }

    public boolean isVisible() {
        return this.visible;
    }
    public void setVisible(boolean state) {this.visible = state;}
    public void closed() {
        dragX = dragY = -1;
    }
}