/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 */
package ru.fluger.client.ui.clickgui.component.impl.component.property.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.awt.Color;
import net.minecraft.client.gui.ScaledResolution;
import ru.fluger.client.feature.Feature;
import ru.fluger.client.feature.impl.hud.ClickGui;
import ru.fluger.client.helpers.render.RenderHelper;
import ru.fluger.client.helpers.render.rect.RectHelper;
import ru.fluger.client.settings.Setting;
import ru.fluger.client.ui.clickgui.component.Component;
import ru.fluger.client.ui.clickgui.component.impl.component.property.PropertyComponent;

public class VisibleComponent
extends Component
implements PropertyComponent {
    public Feature feature;
    private int opacity = 120;

    public VisibleComponent(Feature feature, Component parent, float x, float y, float width, float height) {
        super(parent, "", x, y, width, height);
        this.feature = feature;
    }

    @Override
    public void drawComponent(ScaledResolution scaledResolution, int mouseX, int mouseY) {
        float x = (float)this.getX();
        float y = (float)this.getY();
        boolean hovered = this.isHovered(mouseX, mouseY);
        float height = this.getHeight();
        float width = this.getWidth();
        if (hovered) {
            if (this.opacity < 200) {
                this.opacity += 5;
            }
        } else if (this.opacity > 120) {
            this.opacity -= 5;
        }
        Color onecolor = new Color(ClickGui.color.getColor());
        Color c = new Color(onecolor.getRed(), onecolor.getGreen(), onecolor.getBlue(), 255);
        int color = c.getRGB();
        RectHelper.drawRect(x, y, x + width, y + height, new Color(20, 20, 20, 0).getRGB());
        RectHelper.drawGradientRect(x, y, x + width, y + height, RenderHelper.injectAlpha(new Color(color).darker(), 120).getRGB(), RenderHelper.injectAlpha(new Color(color).darker().darker().darker().darker(), 120).getRGB());
        VisibleComponent.mc.smallfontRenderer.drawStringWithShadow("Visible: " + (Object)ChatFormatting.GRAY + this.feature.visible, x + 4.0f, y + height / 3.0f, -1);
        super.drawComponent(scaledResolution, mouseX, mouseY);
    }

    @Override
    public void onMouseClick(int mouseX, int mouseY, int button) {
        if (this.isHovered(mouseX, mouseY) && button == 0) {
            this.feature.setVisible(!this.feature.isVisible());
        }
        super.onMouseClick(mouseX, mouseY, button);
    }

    @Override
    public void onMouseRelease(int button) {
        super.onMouseRelease(button);
    }

    @Override
    public void onKeyPress(int keyCode) {
        super.onKeyPress(keyCode);
    }

    @Override
    public Setting getProperty() {
        return null;
    }
}

