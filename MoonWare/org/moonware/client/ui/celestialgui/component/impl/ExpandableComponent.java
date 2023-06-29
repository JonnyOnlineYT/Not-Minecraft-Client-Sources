/*
 * Decompiled with CFR 0.150.
 */
package org.moonware.client.ui.celestialgui.component.impl;

import org.moonware.client.ui.celestialgui.component.Component;

public abstract class ExpandableComponent
        extends Component {
    private boolean expanded;

    public ExpandableComponent(Component parent, String name, float x, float y, float width, float height) {
        super(parent, name, x, y, width, height);
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    @Override
    public void onMouseClick(int mouseX, int mouseY, int button) {
        if (isHovered(mouseX, mouseY)) {
            onPress(mouseX, mouseY, button);
            if (canExpand() && button == 1) {
                boolean bl = expanded = !expanded;
            }
        }
        if (isExpanded()) {
            super.onMouseClick(mouseX, mouseY, button);
        }
    }

    public abstract boolean canExpand();

    public abstract int getHeightWithExpand();

    public abstract void onPress(int var1, int var2, int var3);
}

