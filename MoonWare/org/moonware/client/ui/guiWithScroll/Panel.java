package org.moonware.client.ui.guiWithScroll;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.Namespaced;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.moonware.client.utils.MWFont;
import org.moonware.client.MoonWare;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.feature.impl.hud.ClickGui;
import org.moonware.client.feature.impl.visual.util.RenderUtils2;
import org.moonware.client.helpers.Utils.RoundedUtil;
import org.moonware.client.helpers.misc.ClientHelper;
import org.moonware.client.helpers.misc.TimerHelper;
import org.moonware.client.helpers.render.rect.DrawHelper;
import org.moonware.client.helpers.render2.RenderHelper2;
import org.moonware.client.ui.guiWithScroll.component.AnimationState;
import org.moonware.client.ui.guiWithScroll.component.Component;
import org.moonware.client.ui.guiWithScroll.component.DraggablePanel;
import org.moonware.client.ui.guiWithScroll.component.ExpandableComponent;
import org.moonware.client.ui.guiWithScroll.component.impl.ModuleComponent;
import org.moonware.client.ui.guiWithScroll.component.impl.ModuleDescComponent;

import java.awt.*;
import java.util.List;

public final class Panel extends DraggablePanel {
    Minecraft mc = Minecraft.getMinecraft();
    public static final int PANEL_WIDTH = 100;
    public static final int X_ITEM_OFFSET = 1;
    public double scissorBoxHeight;

    public static final int ITEM_HEIGHT = 22;
    public static final int HEADER_HEIGHT = 25;
    private final List<Feature> features;
    public Type type;
    public AnimationState state;
    private int prevX;
    private int prevY;
    private boolean dragging;
    static boolean inPanel;
    int y2;

    public Panel(Type category, int x, int y) {
        super(null, category.getName(), x, y, PANEL_WIDTH, HEADER_HEIGHT);

        int moduleY = HEADER_HEIGHT;

        state = AnimationState.STATIC;
        features = MoonWare.featureManager.getFeaturesForCategory(category);
        for (Feature module : features) {
            components.add(new ModuleComponent(this, module, X_ITEM_OFFSET, moduleY, PANEL_WIDTH - (X_ITEM_OFFSET * 2), ITEM_HEIGHT));
            moduleY += ITEM_HEIGHT;
        }
        for (Feature module : features) {
            components2.add(new ModuleDescComponent(this, module, X_ITEM_OFFSET, moduleY, PANEL_WIDTH - (X_ITEM_OFFSET * 2), ITEM_HEIGHT));
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
        components2.sort(new SorterHelper());
    	TimerHelper timerHelper = new TimerHelper();
    	int color = 0;
    	int x = getX();
        int y = getY();
        setExpanded(true);
        Color onecolor = new Color(ClickGui.color.getColorValue());
        Color twocolor = new Color(ClickGui.colorTwo.getColorValue());
        double speed = ClickGui.speed.getNumberValue();
        switch (ClickGui.clickGuiColor.currentMode) {
            case "Client":
                color = DrawHelper.fadeColorRich(ClientHelper.getClientColor().getRGB(), (ClientHelper.getClientColor().darker().getRGB()), (float) Math.abs(((((System.currentTimeMillis() / speed) / speed) + y * 6L / 60 * 2) % 2) - 1));
                break;
            case "Fade":
                color = DrawHelper.fadeColorRich(onecolor.getRGB(), onecolor.darker().getRGB(), (float) Math.abs(((((System.currentTimeMillis() / speed) / speed) + y * 6L / 60 * 2) % 2) - 1));
                break;
            case "Color Two":
                color = DrawHelper.fadeColorRich(onecolor.getRGB(), twocolor.getRGB(), (float) Math.abs(((((System.currentTimeMillis() / speed) / speed) + y * 6L / 60 * 2) % 2) - 1));
                break;
            case "Astolfo":
                color = DrawHelper.astolfo(20, y);
                break;
            case "Rainbow":
                color = DrawHelper.rainbow(300, 1, 1);
                break;
            case "Category":
                Panel panel = (Panel) parent;
                color = panel.type.getColor();
                break;

        }
        if (!true) {
        	if (dragging) {
                setX(mouseX - prevX);
                setY(mouseY - prevY);
            }
        }
        if (Keyboard.isKeyDown(205)) {
            setX(getX() + 4);
        }
        if (Keyboard.isKeyDown(203)) {
            setX(getX() - 4);
        }
        int width = getWidth();
        int height = getHeight();
        int headerHeight;
        int heightWithExpand = getHeightWithExpand();
        headerHeight = (isExpanded() ? heightWithExpand : height);

        float extendedHeight = 2;
        int yTotal = 0;

        String mode = "Rockstar New";
        boolean animated = false;
        GlStateManager.pushMatrix();
        if (mode.equalsIgnoreCase("Rockstar New")) {

        	if (type.getName().equalsIgnoreCase("Combat") || type.getName().equalsIgnoreCase("Movement") || type.getName().equalsIgnoreCase("Visuals")) {
                //RoundedUtil.drawVerticalGradientOutlinedRoundedRect((x - 1 + 1 - 1), y2 + 30 + 1 - 3, (width + 1 - 5 + 3) + (1 + 1) + 2, 1 - 3 + 18 + (y2 + 260 + 4) - 48, 7, 1.5f, ClickGui.clickGuiColor.currentMode.equalsIgnoreCase("Static") ? onecolor.darker() : new Color(color).darker(), ClickGui.clickGuiColor.currentMode.equalsIgnoreCase("Static") ? onecolor: new Color(color));
                RenderUtils2.drawBlur(4, () -> RoundedUtil.drawRound((x - 1 + 1 - 1), y2 + 30 + 1 - 3, (width + 1 - 5 + 3) + (1 + 1) + 2, 1 - 3 + 18 + (y2 + 260 + 4) - 48, 7, new Color(255,255,255,215)));
                RoundedUtil.drawRound((x - 1 + 1 - 1), y2 + 30 + 1 - 3, (width + 1 - 5 + 3) + (1 + 1) + 2, 1 - 3 + 18 + (y2 + 260 + 4) - 48, 7, new Color(255,255,255,215));
                GL11.glEnable(GL11.GL_SCISSOR_TEST);
                RenderHelper2.scissorRect(x - 1 + 1 - 80, y2 + 30 + 1 - 3 - 8, Minecraft.width,y2 + 30 + 1 -3 + 20 - 1);
                RenderHelper2.drawRainbowRoundGui((x - 1 + 1 - 1), y2 + 30 + 1 - 3.5f, (width + 1 - 5 + 3) + (1 + 1) + 2, 1 - 3 + 18 + (y2 + 260 + 4) - 52, 7, true,true,true,true,2,4);
                GlStateManager.disable(GL11.GL_SCISSOR_TEST);
                //DrawHelper.drawGradientRect((x - 1 + 1 - 1 - 2 + 3) , y2 + 48 + 1 - 3, (x - 1 + 1 - 1 + 2 - 1)  + (width + 1 - 5 + 3) + (1 + 1 - GuiScreen.progress), 1 - 3 + 18 + (y2 + 50.3f + 4) - 17, new Color(0,0,0,55).getRGB(), new Color(0,0,0,0).getRGB());
        		
        		
        		
        		
                MWFont.MONTSERRAT_BOLD.get(24).drawCenter(getName(), (x + width / 2) , y2 + 30 + HEADER_HEIGHT / 2F - 5.5f + 1 -4, Color.WHITE.getRGB());
        	} else {
                RenderUtils2.drawBlur(4, () -> RoundedUtil.drawRound((x - 1 + 1 - 1), y2 + 30 + 1 - 3, (width + 1 - 5 + 3) + (1 + 1) + 2, 1 - 3 + 18 + (y2 + 260 + 4) - 48, 7, new Color(255,255,255,215)));
                RoundedUtil.drawRound((x - 1 + 1 - 1), y2 + 30 + 1 - 3, (width + 1 - 5 + 3) + (1 + 1) + 2, 1 - 3 + 18 + (y2 + 260 + 4) - 48, 7, new Color(255,255,255,215));
                GL11.glEnable(GL11.GL_SCISSOR_TEST);
                RenderHelper2.scissorRect(x - 1 + 1 - 80, y2 + 30 + 1 - 3 - 8, Minecraft.width,y2 + 30 + 1 -3 + 20 - 1);
                RenderHelper2.drawRainbowRoundGui((x - 1 + 1 - 1), y2 + 30 + 1 - 3.5f, (width + 1 - 5 + 3) + (1 + 1) + 2, 1 - 3 + 18 + (y2 + 260 + 4) - 52, 7, true,true,true,true,2,4);
                GlStateManager.disable(GL11.GL_SCISSOR_TEST);
                ///DrawHelper.drawGradientRect((x - 1 - 2 + 3) * (1 + 1 - GuiScreen.progress), y2 + 48 + 1 - 3, (x - 1 + 2 - 1) * (1 + 1 - GuiScreen.progress)+ (width + 1) - (1 + 1 - GuiScreen.progress), 1 - 3 + 18 + (y2 + 50.3f + 4) - 17, new Color(0,0,0,55).getRGB(), new Color(0,0,0,0).getRGB());





                MWFont.MONTSERRAT_BOLD.get(24).drawCenter(getName(), (x + width / 2) , y2 + 30 + HEADER_HEIGHT / 2F - 5.5f + 1 - 4, Color.WHITE.getRGB());
            }
        }
        Color color1;
        Color color2;
        Color color3;
        Color color4;
        Color textColor;
        //RoundedUtil.drawGradientRound((x - 1 + 1 - 1), y2 + 30 + 1 - 3, (width + 1 - 5 + 3) + (1 + 1) + 2, 1 - 3 + 18 + (y2 + 260 + 4) - 48, 7, new Color(255,255,255,200)));
        if (mode.equalsIgnoreCase("Default Dark")) {
            DrawHelper.drawSmoothRect(x + 3, y + 13, x + width - 3, y + (headerHeight - extendedHeight) , new Color(0, 0, 0, 185).getRGB());

            DrawHelper.drawGradientRect(x + 2, y + headerHeight - extendedHeight + 1.3f - 4, x + (width - 2) * (animated ? ClickGuiScreen.progress2 : 0), y + headerHeight - extendedHeight, color, color - 70);


            DrawHelper.drawGradientRect(x - 1, y + 1, x + width + 1, y + 18 - 4, new Color(25, 25, 25, 255).getRGB(), new Color(0, 0, 0, 255).getRGB());

            MWFont.MONTSERRAT_BOLD.get(18).drawShadow(getName(), x + 12, y + HEADER_HEIGHT / 2F - 3.5f, Color.LIGHT_GRAY.getRGB());

            DrawHelper.drawImage(new Namespaced("rockstar/icons/" + type.getName() + ".png"), x + 1, y + 3, 10, 10, Color.LIGHT_GRAY.getRGB());
        }
        GlStateManager.popMatrix();

        inPanel = mouseY < y2 + 260 || mouseY > y2 + 30;

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
        if (isExpanded()) {
            for (Component component : components2) {
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
