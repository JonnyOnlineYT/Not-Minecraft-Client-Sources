package org.moonware.client.qol.ui;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;
import org.moonware.client.utils.MWDrawUtils;
import org.moonware.client.utils.MWFont;
import org.moonware.client.utils.MWUtils;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class DropDownMenu<T> {
    public final List<T> items;
    public final Function<T, String> toString;
    public final Consumer<T> handler;
    public int x;
    public int y;
    public int width;
    public int height;
    protected int offset;
    public DropDownMenu(int x, int y, List<T> items, Function<T, String> toString, Consumer<T> handler) {
        this.items = Collections.unmodifiableList(items);
        this.toString = toString;
        this.x = x;
        this.y = y;
        this.handler = handler;
        for (T item : this.items) width = Math.max(width, MWFont.MONTSERRAT_SEMIBOLD.get(16F).getWidth(toString.apply(item)) + 12);
        height = Math.min(10, items.size()) * 10;
    }

    public void draw(int mouseX, int mouseY, float partialTick) {
        Gui.drawRect(x, y, x + width, y + height, 0x80702020);
        GlStateManager.enable(GL11.GL_SCISSOR_TEST);
        GlStateManager.enableBlend();
        MWDrawUtils.scaledScissors(x, y, width, height);
        for (int i = 0; i < items.size(); i++) {
            if (MWUtils.isHovered(x, y + i * 10 + offset, width, 10, mouseX, mouseY)) {
                Gui.drawRect(x, y + i * 10 + offset, x + width, y + i * 10 + offset + 10, Integer.MIN_VALUE);
            }
            MWFont.MONTSERRAT_SEMIBOLD.get(16F).drawShadow(toString.apply(items.get(i)), x + 3, y + 1 + i * 10 + offset + 2, -1);
        }
        GlStateManager.disableBlend();
        GlStateManager.disable(GL11.GL_SCISSOR_TEST);
    }

    public boolean mousePressed(int mouseX, int mouseY, int button) {
        if (!MWUtils.isHovered(x, y, width, height, mouseX, mouseY)) return false;
        int index = (mouseY - y - offset) / 10;
        if (index >= 0 && index < items.size()) handler.accept(items.get(index));
        return true;
    }

    public void mouseScrolled(int mouseX, int mouseY, int scroll) {
        if (!MWUtils.isHovered(x, y, width, height, mouseX, mouseY)) return;
        offset = MathHelper.clamp(offset + MathHelper.clamp(scroll, -1, 1) * 10, 95 - (items.size() * 10), 0);
    }
}
