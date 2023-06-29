/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package ru.fluger.client.ui.clickgui.component.impl.component.property.impl;

import java.awt.Color;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;
import ru.fluger.client.AnimationMath;
import ru.fluger.client.UIRender;
import ru.fluger.client.feature.impl.hud.ClickGui;
import ru.fluger.client.helpers.misc.TimerHelper;
import ru.fluger.client.helpers.render.RenderHelper;
import ru.fluger.client.helpers.render.rect.RectHelper;
import ru.fluger.client.settings.Setting;
import ru.fluger.client.settings.impl.BooleanSetting;
import ru.fluger.client.ui.clickgui.component.Component;
import ru.fluger.client.ui.clickgui.component.impl.component.property.PropertyComponent;

public class BooleanPropertyComponent
extends Component
implements PropertyComponent {
    public BooleanSetting setting;
    private final TimerHelper descTimer = new TimerHelper();
    public double animation;

    public BooleanPropertyComponent(Component parent, BooleanSetting setting, float x, float y, float width, float height) {
        super(parent, setting.getName(), x, y, width, height);
        this.setting = setting;
    }

    private void renderArrow(float x, float y, int color) {
        GlStateManager.pushMatrix();
        RenderHelper.color(color);
        GL11.glTranslatef((float)x, (float)y, (float)0.0f);
        GL11.glLineWidth((float)1.5f);
        GL11.glDisable((int)3553);
        GL11.glBegin((int)3);
        GL11.glVertex2d((double)0.0, (double)0.0);
        GL11.glVertex2d((double)2.0, (double)2.0);
        GL11.glVertex2d((double)6.0, (double)-2.0);
        GL11.glEnd();
        GL11.glDisable((int)2848);
        GL11.glEnable((int)3553);
        GlStateManager.popMatrix();
    }

    @Override
    public void drawComponent(ScaledResolution scaledResolution, int mouseX, int mouseY) {
        float x = (float)this.getX();
        float y = (float)this.getY();
        float width = this.getWidth();
        float height = this.getHeight();
        boolean hovered = this.isHovered(mouseX, mouseY);
        Color onecolor = new Color(ClickGui.color.getColor());
        Color c = new Color(onecolor.getRed(), onecolor.getGreen(), onecolor.getBlue(), 255);
        int color = c.getRGB();
        float middleHeight = this.getHeight() / 2.0f;
        float btnRight = x + 3.0f + middleHeight;
        int checkbox_width = 15;
        int checkbox_height = 5;
        double max = this.setting.getCurrentValue() ? 5.0 : (double)checkbox_width;
        this.animation = AnimationMath.animate(max, this.animation, 0.05);
        RectHelper.drawRect(x, y, x + width, y + height, new Color(20, 20, 20, 0).getRGB());
        RectHelper.drawGradientRect(x, y, x + width, y + height, RenderHelper.injectAlpha(new Color(color).darker(), 120).getRGB(), RenderHelper.injectAlpha(new Color(color).darker().darker().darker().darker(), 120).getRGB());
        BooleanPropertyComponent.mc.smallfontRenderer.drawStringWithShadow(this.getName(), btnRight - 8.0f, y + this.getHeight() / 2.0f - 2.0f, -1);
        float buttonLeft = x + 2.0f;
        float buttonTop = y + middleHeight - (middleHeight / 2.0f - 1.0f);
        float buttonBottom = y + middleHeight + middleHeight / 2.0f + 2.0f;
        if (ClickGui.glow.getCurrentValue()) {
            RenderHelper.renderBlurredShadow(this.setting.getCurrentValue() ? new Color(color) : new Color(30, 30, 30), (double)((float)((int)buttonLeft) + width - 18.0f), (double)((int)buttonTop - 1), (double)(checkbox_width - 1), (double)checkbox_height, 5);
        }
        GlStateManager.pushMatrix();
        UIRender.drawRect(this.getX() + (double)this.getWidth() - (double)checkbox_width, this.getY() + (double)checkbox_height, this.getX() + (double)this.getWidth() - 4.0, this.getY() + (double)checkbox_height + 3.0, color);
        UIRender.roundedBorder((float)this.getX() + this.getWidth() - (float)checkbox_width, (float)this.getY() + (float)checkbox_height, (float)this.getX() + this.getWidth() - 4.0f, (float)this.getY() + (float)checkbox_height + 3.0f, 1.0f, color);
        UIRender.drawCircle((float)this.getX() + this.getWidth() - (float)this.animation, (float)this.getY() + 6.5f, 0.0f, 360.0f, 3.0f, true, new Color(-1));
        GlStateManager.popMatrix();
        if (hovered) {
            if (this.setting.getDesc() != null && this.descTimer.hasReached(250.0)) {
                RenderHelper.drawBlurredShadow(x + width + 20.0f, y + height / 1.5f - 5.0f, 5 + BooleanPropertyComponent.mc.fontRenderer.getStringWidth(this.setting.getDesc()), 10.0f, 20, new Color(0, 0, 0, 180));
                RectHelper.drawSmoothRect(x + width + 20.0f, y + height / 1.5f + 4.5f, x + width + 25.0f + (float)BooleanPropertyComponent.mc.fontRenderer.getStringWidth(this.setting.getDesc()), y + 6.5f, new Color(0, 0, 0, 80).getRGB());
                BooleanPropertyComponent.mc.fontRenderer.drawStringWithShadow(this.setting.getDesc(), x + width + 22.0f, y + height / 1.35f - 5.0f, -1);
            }
        } else {
            this.descTimer.reset();
        }
        if (this.setting.getBlock() != null) {
            ItemStack stack = new ItemStack(this.setting.getBlock());
            GlStateManager.pushMatrix();
            net.minecraft.client.renderer.RenderHelper.enableGUIStandardItemLighting();
            mc.getRenderItem().renderItemIntoGUI(stack, x + 50.0f, y + height / 2.0f - 7.0f);
            GlStateManager.popMatrix();
        }
    }

    @Override
    public void onMouseClick(int mouseX, int mouseY, int button) {
        if (button == 0 && this.isHovered(mouseX, mouseY)) {
            this.setting.setValue(!this.setting.getCurrentValue());
        }
    }

    @Override
    public Setting getProperty() {
        return this.setting;
    }
}

