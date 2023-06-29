package org.moonware.client.ui.clickgui;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.text.Formatting;
import org.lwjgl.opengl.GL11;
import org.moonware.client.utils.Interpolator;
import org.moonware.client.utils.MWFont;
import org.moonware.client.MoonWare;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.feature.impl.hud.ClickGui;
import org.moonware.client.helpers.Helper;
import org.moonware.client.helpers.misc.ClientHelper;
import org.moonware.client.helpers.palette.PaletteHelper;
import org.moonware.client.helpers.render.rect.RectHelper;
import org.moonware.client.helpers.render2.RenderHelper2;
import org.moonware.client.ui.clickgui.component.AnimationState;
import org.moonware.client.ui.clickgui.component.Component;
import org.moonware.client.ui.clickgui.component.DraggablePanel;
import org.moonware.client.ui.clickgui.component.ExpandableComponent;
import org.moonware.client.ui.clickgui.component.impl.ModuleComponent;

import java.awt.*;
import java.util.List;

public final class Panel extends DraggablePanel implements Helper {

    public static final int HEADER_WIDTH = 110;
    public static final int X_ITEM_OFFSET = 1;
    public static final int ITEM_HEIGHT = 15;
    public static final int HEADER_HEIGHT = 17;
    private final List<Feature> features;
    public Type type;
    public AnimationState state;
    private int prevX;
    private int prevY;
    private boolean dragging;
    private int animatehover;

    public Panel(Type category, int x, int y) {
        super(null, category.name(), x, y, HEADER_WIDTH, HEADER_HEIGHT);
        int moduleY = HEADER_HEIGHT;
        state = AnimationState.STATIC;
        features = MoonWare.featureManager.getFeaturesForCategory(category);
        animatehover =  0;
        for (Feature module : features) {
            components.add(new ModuleComponent(this, module, X_ITEM_OFFSET, moduleY, HEADER_WIDTH - (X_ITEM_OFFSET * 2), ITEM_HEIGHT));
            moduleY += ITEM_HEIGHT;
        }
        type = category;
    }

    @Override
    public void drawComponent(ScaledResolution scaledResolution, int mouseX, int mouseY) {
        components.sort(new SorterHelper());
        if (dragging) {
            setX(mouseX - prevX);
            setY(mouseY - prevY);
        }
        int x = getX();
        int y = getY();
        int width = getWidth();
        int height = getHeight();
        int headerHeight;
        int heightWithExpand = getHeightWithExpand();
        headerHeight = (isExpanded() ? heightWithExpand : height);
        int color = 0;
        Color onecolor = new Color(ClickGui.color.getColorValue());
        Color twocolor = new Color(ClickGui.colorTwo.getColorValue());
        double speed = ClickGui.speed.getNumberValue();
        switch (ClickGui.clickGuiColor.currentMode) {
            case "Client":
                color = PaletteHelper.fadeColor(ClientHelper.getClientColor().getRGB(), (ClientHelper.getClientColor().darker().getRGB()), (float) Math.abs(((((System.currentTimeMillis() / speed) / speed) + y * 6L / 60 * 2) % 2) - 1));
                break;
            case "Fade":
                color = PaletteHelper.fadeColor(onecolor.getRGB(), onecolor.darker().getRGB(), (float) Math.abs(((((System.currentTimeMillis() / speed) / speed) + y * 6L / 60F * 2) % 2) - 1));
                break;
            case "Color Two":
                color = PaletteHelper.fadeColor(onecolor.getRGB(), twocolor.getRGB(), (float) Math.abs(((((System.currentTimeMillis() / speed) / speed) + y * 6L / 60F * 2) % 2) - 1));
                break;
            case "Astolfo":
                color = PaletteHelper.astolfo(true, y).getRGB();
                break;
            case "Static":
                color = onecolor.getRGB();
                break;
            case "Rainbow":
                color = PaletteHelper.rainbow(300, 1, 1).getRGB();
                break;
        }

        float extendedHeight = 2;
        if (ClickGui.mode.currentMode.equalsIgnoreCase("Black") && isExpanded()) {
            //RectHelper.drawRectWithGlow(x, y + 16, x + width, y + (!isExpanded() ? 3 : 0) + headerHeight - extendedHeight,5,25, new Color(19, 19, 19, 255));
            if (ClickGui.panelMode.getCurrentMode().equalsIgnoreCase("Rect")) {
                //RenderHelper2.renderBlurredShadow(new Color(19, 19, 19, 255), x, y + 13, width, (!isExpanded() ? 3 : 0) + headerHeight - extendedHeight-11, 7);
                RenderHelper2.drawRainbowRoundGui(x, y, width,(!isExpanded() ? 3 : 0) + headerHeight - extendedHeight-11 + 13,3,ClickGuiScreen.escapeKeyInUse,true,true,true,2,4);
                RectHelper.drawRect(x + 1, y + 15, x + width - 1, y + 16,new Color(255,255,255,100).getRGB());
            }else{
                RenderHelper2.renderBlur(x, y + 16, width, (int) ((!isExpanded() ? 3 : 0) + headerHeight - extendedHeight), 7);
                RenderHelper2.renderBlurredShadow(new Color(16, 16, 16, 190), x, y + 16, width, (!isExpanded() ? 3 : 0) + headerHeight - extendedHeight-14, 7);
            }
            if (isExpanded()) {
                /*
                GlStateManager.pushMatrix();
                float rotY = 4;
                float rotX = 4;
                float size = 50;
                float xOffset = x;
                float yOffset = y;
                if (MathHelper.sqrt(4 * 4 + 7 * 7) < 50) {
                    float angle = (float) (Math.atan2(rotY, rotX) * 180 / Math.PI);
                    double xPos = ((size / 2) * Math.cos(Math.toRadians(angle))) + xOffset + size / 2;
                    double yy = ((size / 2) * Math.sin(Math.toRadians(angle))) + yOffset + size / 2;
                    GlStateManager.translate(xPos, yy, 0);
                    GlStateManager.rotate(angle, 0, 0, 1);
                    GlStateManager.scale(1.5, 1, 1);
                    //RectHelper.drawGradientRectBetter(x, y + 19, x + width, y + 22, new Color(31,31,31,255).getRGB(), new Color(31,31,31, 1).getRGB());
                    RectHelper.drawGradientRect(x, y + 19, x + width, y + 22, new Color(31,31,31,255).getRGB(), new Color(31,31,31, 1).getRGB());
                }
                GlStateManager.popMatrix();

                 */
            }
            //RectHelper.drawGlow(x,y - 4, x + width, y + headerHeight - extendedHeight, new Color(9,9,9,202).getRGB());
        }else if (ClickGui.mode.currentMode.equalsIgnoreCase("White")) {
        }
        //RenderHelper2.renderBlurredShadow(new Color(12, 12, 12, 215),x - 4, y - 7, width + 8, 24,5);

        //RectHelper.drawRectWithGlow(x - 2, y - 7, x + width + 2, y + 14, 5,18,new Color(12, 12, 12, 215));
        float width2 = (x + width + 6) - (x - 6);
        MWFont.GREYCLIFFCF_MEDIUM.get(30).drawCenterShadow(Formatting.BOLD + getName(),  x +  width / 2, y + HEADER_HEIGHT / 2F - 8, -1);
        //RenderHelper.drawImage(new ResourceLocation("moonware/icons/" + getName() + ".png"), x -4 , y + HEADER_HEIGHT / 2F - 13, 16,16,Color.white);
        if (isExpanded() && ClickGui.downBorder.getBoolValue()) {
            if (ClickGui.glow.getBoolValue()) {
                //RenderHelper2.renderBlurredShadow(new Color(color), x - 2, y + headerHeight - extendedHeight - 1, width + 4.5f, 2.0, (int) ClickGui.glowRadius.getCurrentValue());
            }
            //RectHelper.drawRect(x - 2, y + headerHeight - extendedHeight, x + width + 2, y + headerHeight - extendedHeight + 2, color);
        }
        RectHelper.drawGlow(x,y,x,y,Color.WHITE.getRGB());
        //RenderHelper.drawImage(new ResourceLocation("moonware/clickGuiIcons/Combat.png"), x + 2, y + HEADER_HEIGHT / 2 wF - 6, 16,16, Color.WHITE);


        super.drawComponent(scaledResolution, mouseX, mouseY);

        if (isExpanded()) {
            GL11.glEnable(GL11.GL_SCISSOR_TEST);
            RenderHelper2.scissorRect(x, y,x + width,y + getHeightWithExpand());
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
            GL11.glDisable(GL11.GL_SCISSOR_TEST);

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
        animatehover = isExpanded() ? Interpolator.linear(animatehover,height, 2f/18) : -40;
        return animatehover;
    }
}
