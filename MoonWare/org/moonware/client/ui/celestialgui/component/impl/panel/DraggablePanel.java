/*
 * Decompiled with CFR 0.150.
 */
package org.moonware.client.ui.celestialgui.component.impl.panel;

import net.minecraft.client.gui.ScaledResolution;
import org.moonware.client.ui.celestialgui.component.Component;
import org.moonware.client.ui.celestialgui.component.impl.ExpandableComponent;

public abstract class DraggablePanel
        extends ExpandableComponent {
    private boolean dragging;
    private float prevX;
    private float prevY;

    public DraggablePanel(Component parent, String name, float x, float y, float width, float height) {
        super(parent, name, x, y, width, height);
        prevX = x;
        prevY = y;
    }

    @Override
    public void drawComponent(ScaledResolution scaledResolution, int mouseX, int mouseY) {
        if (dragging) {
            setX((float)mouseX - prevX);
            setY((float)mouseY - prevY);
        }
    }

    @Override
    public void onPress(int mouseX, int mouseY, int button) {
        if (button == 0) {
            dragging = true;
            prevX = (int)((double)mouseX - getX());
            prevY = (int)((double)mouseY - getY());
        }
    }

    @Override
    public void onMouseRelease(int button) {
        super.onMouseRelease(button);
        dragging = false;
    }
}

