/*
 * Decompiled with CFR 0.150.
 */
package org.moonware.client.ui.celestialgui.component.impl.component.property.impl;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.text.Formatting;
import org.moonware.client.utils.MWFont;
import org.moonware.client.feature.Feature;
import org.moonware.client.helpers.render.rect.RectHelper;
import org.moonware.client.settings.Setting;
import org.moonware.client.ui.celestialgui.component.Component;
import org.moonware.client.ui.celestialgui.component.impl.component.property.PropertyComponent;

import java.awt.*;

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
        float x = (float) getX();
        float y = (float) getY();
        boolean hovered = isHovered(mouseX, mouseY);
        float height = getHeight();
        float width = getWidth();
        if (hovered) {
            if (opacity < 200) {
                opacity += 5;
            }
        } else if (opacity > 120) {
            opacity -= 5;
        }
        RectHelper.drawRect(x, y, x + width, y + height, new Color(20, 20, 20, 160).getRGB());
        MWFont.SF_UI_DISPLAY_REGULAR.get(14).drawShadow("Visible: " + Formatting.GRAY + feature.visible, x + 2.0f, y + height / 3.0f, -1);
        super.drawComponent(scaledResolution, mouseX, mouseY);
    }

    @Override
    public void onMouseClick(int mouseX, int mouseY, int button) {
        if (isHovered(mouseX, mouseY) && button == 0) {
            feature.setVisible(!feature.isVisible());
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

