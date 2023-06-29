package org.moonware.client.ui.guiWithScroll;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.moonware.client.MoonWare;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.ui.guiWithScroll.component.AnimationState;
import org.moonware.client.ui.guiWithScroll.component.Component;
import org.moonware.client.ui.guiWithScroll.component.DraggablePanel;
import org.moonware.client.ui.guiWithScroll.component.ExpandableComponent;
import org.moonware.client.ui.guiWithScroll.component.impl.ModuleDescComponent;

import java.util.List;

public final class PanelToDesc extends DraggablePanel {
    Minecraft mc = Minecraft.getMinecraft();
    public static final int HEADER_WIDTH = 100;
    public static final int X_ITEM_OFFSET = 1;
    public double scissorBoxHeight;

    public static final int ITEM_HEIGHT = 22;
    public static final int HEADER_HEIGHT = 17;
    private final List<Feature> features;
    public Type type;
    public AnimationState state;
    private int prevX;
    private int prevY;
    private boolean dragging;
    static boolean inPanel;
    int y2;

    public PanelToDesc(Type category, int x, int y) {
        super(null, category.getName(), x, y, HEADER_WIDTH, HEADER_HEIGHT);

        int moduleY = HEADER_HEIGHT;
        state = AnimationState.STATIC;
        features = MoonWare.featureManager.getFeaturesForCategory(category);
        for (Feature module : features) {
            components.add(new ModuleDescComponent(this, module, X_ITEM_OFFSET, moduleY, HEADER_WIDTH - (X_ITEM_OFFSET * 2), ITEM_HEIGHT));
            moduleY += ITEM_HEIGHT;
        }
        type = category;
    }

    public void setY2(int y2) {
        this.y2 = y2;
    }

    public int getY2() {
        return y2;
    }

    @Override
    public void drawComponent(ScaledResolution scaledResolution, int mouseX, int mouseY) {
        components.sort(new SorterHelper());
        int height = getHeight();
        if (isExpanded()) {
            for (Component component : components) {
                component.setY(height);
                component.drawComponent(scaledResolution, mouseX, mouseY);
                int cHeight = component.getHeight();
                if (component instanceof ExpandableComponent) {
                    ExpandableComponent expandableComponent = (ExpandableComponent) component;
                    if (expandableComponent.isExpanded()) {
                        cHeight = expandableComponent.getHeightWithExpand() + 5;
                    }
                }
                height += cHeight;
            }
        }
    }

    @Override
    public void onPress(int mouseX, int mouseY, int button) {
        if (button == 0 && !dragging) {
            dragging = true;
            prevX = mouseX - getX();
            prevY = mouseY - getY();
        }
    }


    @Override
    public void onMouseRelease(int button) {
        super.onMouseRelease(button);
        dragging = false;
    }

    @Override
    public boolean canExpand() {
        return !features.isEmpty();
    }

    @Override
    public int getHeightWithExpand() {
        int height = getHeight();
        if (isExpanded()) {
            for (Component component : components) {
                int cHeight = component.getHeight();
                if (component instanceof ExpandableComponent) {
                    ExpandableComponent expandableComponent = (ExpandableComponent) component;
                    if (expandableComponent.isExpanded())
                        cHeight = expandableComponent.getHeightWithExpand() + 5;
                }
                height += cHeight;
            }
        }
        return height;
    }
}

