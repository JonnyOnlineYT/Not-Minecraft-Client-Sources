/*
 * Decompiled with CFR 0.150.
 */
package org.moonware.client.ui.celestialgui.component.impl.component.property.impl;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import org.moonware.client.utils.MWFont;
import org.moonware.client.feature.impl.hud.ClickGui;
import org.moonware.client.helpers.misc.TimerHelper;
import org.moonware.client.helpers.render.rect.RectHelper;
import org.moonware.client.helpers.render2.RenderHelper2;
import org.moonware.client.settings.Setting;
import org.moonware.client.settings.impl.BooleanSetting;
import org.moonware.client.ui.celestialgui.component.Component;
import org.moonware.client.ui.celestialgui.component.impl.component.property.PropertyComponent;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class BooleanPropertyComponent
extends org.moonware.client.ui.celestialgui.component.Component
implements PropertyComponent {
    public BooleanSetting setting;
    private final TimerHelper descTimer = new TimerHelper();

    public BooleanPropertyComponent(Component parent, BooleanSetting setting, float x, float y, float width, float height) {
        super(parent, setting.getName(), x, y, width, height);
        this.setting = setting;
    }

    private void renderArrow(float x, float y, int color) {
        GlStateManager.pushMatrix();
        RenderHelper2.color(color);
        GL11.glTranslatef(x, y, 0.0f);
        GL11.glLineWidth(1.5f);
        GL11.glDisable(3553);
        GL11.glBegin(3);
        GL11.glVertex2d(0.0, 0.0);
        GL11.glVertex2d(2.0, 2.0);
        GL11.glVertex2d(6.0, -2.0);
        GL11.glEnd();
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GlStateManager.popMatrix();
    }

    @Override
    public void drawComponent(ScaledResolution scaledResolution, int mouseX, int mouseY) {
        float x = (float) getX();
        float y = (float) getY();
        float width = getWidth();
        float height = getHeight();
        boolean hovered = isHovered(mouseX, mouseY);
        Color onecolor = new Color(ClickGui.color.getColor());
        Color c = new Color(onecolor.getRed(), onecolor.getGreen(), onecolor.getBlue(), 255);
        int color = c.getRGB();
        float middleHeight = getHeight() / 2.0f;
        float btnRight = x + 3.0f + middleHeight;
        RectHelper.drawRect(x, y, x + width, y + height, new Color(20, 20, 20, 160).getRGB());
        MWFont.SF_UI_DISPLAY_REGULAR.get(14).drawShadow(getName(), btnRight + 4.0f, y + middleHeight - 1.0f, -1);
        float buttonLeft = x + 2.0f;
        float buttonTop = y + middleHeight - (middleHeight / 2.0f - 1.0f);
        float buttonBottom = y + middleHeight + middleHeight / 2.0f + 2.0f;
        if (ClickGui.glow.getCurrentValue()) {
            RenderHelper2.renderBlurredShadow(new Color(color), (int)buttonLeft, (int)buttonTop - 1, 10.0, 10.0, 5);
        }
        GlStateManager.pushMatrix();
        RectHelper.drawSmoothRect(buttonLeft, buttonTop, btnRight, buttonBottom, color);
        RectHelper.drawSmoothRect(buttonLeft + 0.5f, buttonTop + 0.5f, (double)btnRight - 0.5, (double)buttonBottom - 0.5, new Color(30, 30, 30).getRGB());
        if (setting.getCurrentValue()) {
            renderArrow(buttonLeft + 1.5f, buttonTop + 4.0f, -1);
        }
        GlStateManager.popMatrix();
        if (hovered) {
            if (setting.getDesc() != null && descTimer.hasReached(250.0F)) {
                RectHelper.drawBorder(x + 120.0f, y + height / 1.5f + 3.5f, x + 138.0f + (float) Minecraft.font.getStringWidth(setting.getDesc()) - 5.0f, y + 3.5f, 0.5, new Color(30, 30, 30, 255).getRGB(), color, true);
                Minecraft.font.drawStringWithShadow(setting.getDesc(), x + 124.0f, y + height / 1.5f - 5.0f, -1);
            }
        } else {
            descTimer.reset();
        }
        if (setting.getBlock() != null) {
            ItemStack stack = new ItemStack(setting.getBlock());
            GlStateManager.pushMatrix();
            net.minecraft.client.renderer.RenderHelper.enableGUIStandardItemLighting();
            Minecraft.getRenderItem().renderItemIntoGUI(stack, x + 50.0f, y + height / 2.0f - 7.0f);
            GlStateManager.popMatrix();
        }
    }

    @Override
    public void onMouseClick(int mouseX, int mouseY, int button) {
        if (button == 0 && isHovered(mouseX, mouseY)) {
            setting.setValue(!setting.getCurrentValue());
        }
    }

    @Override
    public Setting getProperty() {
        return setting;
    }
}

