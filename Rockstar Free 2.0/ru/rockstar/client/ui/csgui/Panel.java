package ru.rockstar.client.ui.csgui;

import java.awt.Color;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import ru.rockstar.Main;
import ru.rockstar.api.utils.render.ClientHelper;
import ru.rockstar.api.utils.render.DrawHelper;
import ru.rockstar.client.features.Category;
import ru.rockstar.client.features.Feature;
import ru.rockstar.client.features.impl.display.ClickGUI;
import ru.rockstar.client.ui.csgui.component.AnimationState;
import ru.rockstar.client.ui.csgui.component.Component;
import ru.rockstar.client.ui.csgui.component.DraggablePanel;
import ru.rockstar.client.ui.csgui.component.ExpandableComponent;
import ru.rockstar.client.ui.csgui.component.impl.ModuleComponent;

public final class Panel extends DraggablePanel {
    Minecraft mc = Minecraft.getMinecraft();
    public static final int HEADER_WIDTH = 100;
    public static final int X_ITEM_OFFSET = 1;
    public double scissorBoxHeight;

    public static final int ITEM_HEIGHT = 15;
    public static final int HEADER_HEIGHT = 17;
    private final List<Feature> features;
    public Category type;
    public AnimationState state;
    private int prevX;
    private int prevY;
    private boolean dragging;

    public Panel(Category category, int x, int y) {
        super(null, category.name(), x, y, HEADER_WIDTH, HEADER_HEIGHT);

        int moduleY = HEADER_HEIGHT;
        this.state = AnimationState.STATIC;
        this.features = Main.instance.featureDirector.getFeaturesForCategory(category);
        for (Feature module : features) {
            this.components.add(new ModuleComponent(this, module, X_ITEM_OFFSET,  moduleY, HEADER_WIDTH - (X_ITEM_OFFSET * 2), ITEM_HEIGHT));
            moduleY += ITEM_HEIGHT;
        }
        this.type = category;
    }

    @Override
    public void drawComponent(ScaledResolution scaledResolution, int mouseX, int mouseY) {
    	int color = 0;

    	int x = getX();
        int y = getY();
        Color onecolor = new Color(ClickGUI.color.getColorValue());
        Color twocolor = new Color(ClickGUI.colorTwo.getColorValue());
        double speed = ClickGUI.speed.getNumberValue();
        switch (ClickGUI.clickGuiColor.currentMode) {
            case "Client":
                color = DrawHelper.fadeColor(ClientHelper.getClientColor().getRGB(), (ClientHelper.getClientColor().darker().getRGB()), (float) Math.abs(((((System.currentTimeMillis() / speed) / speed) + y * 6L / 60 * 2) % 2) - 1));
                break;
            case "Fade":
                color = DrawHelper.fadeColor(onecolor.getRGB(), onecolor.darker().getRGB(), (float) Math.abs(((((System.currentTimeMillis() / speed) / speed) + y * 6L / 60 * 2) % 2) - 1));
                break;
            case "Color Two":
                color = DrawHelper.fadeColor(onecolor.getRGB(), twocolor.getRGB(), (float) Math.abs(((((System.currentTimeMillis() / speed) / speed) + y * 6L / 60 * 2) % 2) - 1));
                break;
            case "Astolfo":
                color = DrawHelper.astolfo(true, (int) y).getRGB();
                break;
            case "Rainbow":
                color = DrawHelper.rainbow(300, 1, 1).getRGB();
                break;
            case "Category":
                Panel panel = (Panel) parent;
                color = panel.type.getColor();
                break;

        }
        if (dragging) {
            setX(mouseX - prevX);
            setY(mouseY - prevY);
        }
        int width = getWidth();
        int height = getHeight();
        int headerHeight;
        int heightWithExpand = getHeightWithExpand();
        headerHeight = (isExpanded() ? heightWithExpand : height);

        float extendedHeight = 2;
        int yTotal = 0;

        String mode = ClickGUI.style.getOptions();

        DrawHelper.drawSmoothRect(x + 7, y + 13, x + width - 7, y + headerHeight - extendedHeight + 3, new Color(0, 0, 0,185).getRGB());

        DrawHelper.drawGradientRect1(x + 5, y + headerHeight - extendedHeight + 1.3f - 4 + 3, x + width - 5, y + headerHeight - extendedHeight + 3, color, color - 70);

        DrawHelper.drawGradientRect(x + 5, y + 3, x + width -5, y + 15 - 2, new Color(25, 25, 25, 255).getRGB(), new Color(0,0,0,255).getRGB());

        mc.mntsb_16.drawStringWithShadow(getName(), x + 15, y + HEADER_HEIGHT / 2F - 2.5f, Color.LIGHT_GRAY.getRGB());
        
        DrawHelper.drawImage(new ResourceLocation("rockstar/icons/" + type.getName() + ".png"), x + 5, y + 3, 10, 10, Color.LIGHT_GRAY);



        super.drawComponent(scaledResolution, mouseX, mouseY);

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
        if (button == 0 && !this.dragging) {
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
