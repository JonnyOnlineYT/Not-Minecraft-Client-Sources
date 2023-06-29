package Celestial.ui.celestun4ik;

import Celestial.Smertnix;
import Celestial.module.Module;
import Celestial.module.ModuleCategory;
import Celestial.module.impl.Render.ClickGUI;
import Celestial.ui.celestun4ik.component.impl.ModuleComponent;
import Celestial.utils.render.RenderUtils;
import Celestial.utils.render.RoundedUtil;
import Celestial.ui.celestun4ik.component.AnimationState;
import Celestial.ui.celestun4ik.component.Component;
import Celestial.ui.celestun4ik.component.DraggablePanel;
import Celestial.ui.celestun4ik.component.ExpandableComponent;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;
import java.util.List;


public final class panelcomponent extends DraggablePanel {
    Minecraft mc = Minecraft.getMinecraft();
    public static final int HEADER_WIDTH = 107;
    public static final int X_ITEM_OFFSET = 1;
    public static final int ITEM_HEIGHT = 15;
    public static final int HEADER_HEIGHT = 17;
    public List<Module> features;
    public ModuleCategory type;
    public AnimationState state;
    private int prevX;
    private int prevY;
    private boolean dragging;

    public panelcomponent(ModuleCategory category, int x, int y) {
        super(null, category.name(), x, y, HEADER_WIDTH, HEADER_HEIGHT);
        int moduleY = HEADER_HEIGHT;
        this.state = AnimationState.STATIC;
        this.features = Smertnix.instance.featureManager.getFeaturesCategory(category);
        for (Module feature : features) {
            this.components.add(new ModuleComponent(this, feature, X_ITEM_OFFSET, moduleY, HEADER_WIDTH - (X_ITEM_OFFSET * 2), ITEM_HEIGHT));
            moduleY += ITEM_HEIGHT;
        }
        this.type = category;
    }

    @Override
    public void drawComponent(ScaledResolution scaledResolution, int mouseX, int mouseY) {
        if (dragging) {
            setX(mouseX - prevX);
            setY(mouseY - prevY);
        }


        setExpanded(true);
        int x = getX();
        int y = getY();
        int width = getWidth();
        int height = getHeight();
        int headerHeight;
        int heightWithExpand = getHeightWithExpand();
        headerHeight = (isExpanded() ? heightWithExpand : height);
        float startAlpha1 = 0.14f;
        int size1 = 25;
        float left1 = x + 1.0f;
        float right1 = x + width;
        float bottom1 = y + headerHeight - 6.0f;
        float top1 = y + headerHeight - 2.0f;
        float top2 = y + 13.0f;

        Color color = new Color(ClickGUI.color.getColorValue());

        float extendedHeight = 2;
        ScaledResolution sr = new ScaledResolution(mc);


        GlStateManager.pushMatrix();
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        RenderUtils.drawRect(x, y, x, y, new Color(30, 30, 30, 255).getRGB());
        RenderUtils.scissorRect(0, 25.5f, sr.getScaledWidth(), 250);

        RoundedUtil.drawRound(x - 4, 10 + 4, width + 3, 228 - 4, 4, new Color(245,245,245, 220));

        if (ClickGUI.blur.getCurrentValue()) {
            RenderUtils.drawBlur(9, () -> {
                RoundedUtil.drawRound(x - 4 - 4, 10 + 4, width + 3 + 8, 228, 4, new Color(245,245,245, 220));
            });
        }

        RoundedUtil.drawRound(x - 4, 10 + 4, width + 3, 228 - 4, 4, new Color(245,245,245, 220));


        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        GlStateManager.popMatrix();




        GlStateManager.pushMatrix();
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        RenderUtils.drawRect(x, y, x, y, new Color(30, 30, 30, 255).getRGB());
        RenderUtils.scissorRect(0, 25.5f, sr.getScaledWidth(), 239);




    	/*
    	if (ClickGUI.blur.getBoolValue()) {
        	RenderUtils.drawShadow(5, 1, () -> {
            	RoundedUtil.drawRound(x - 4, 10, width + 3, 228, 4, new Color(245,245,245, 220));
            });
        }*/



        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        GlStateManager.popMatrix();

        GlStateManager.pushMatrix();
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        RenderUtils.drawRect(x, y, x, y, new Color(30, 30, 30, 255).getRGB());
        RenderUtils.scissorRect(0, 0, sr.getScaledWidth(), 26);


        RenderUtils.drawBlurredShadow(x - 4 - 2, 10 - 2, width + 3 + 4, 30, 7, RenderUtils.injectAlpha(new Color(ClickGUI.color.getColorValue()), 100));


        RoundedUtil.drawGradientRound(x - 4, 10, width + 3, 30, 5, new Color(ClickGUI.color.getColorValue()), new Color(ClickGUI.color.getColorValue()), new Color(ClickGUI.color2.getColorValue()).darker(), new Color(ClickGUI.color2.getColorValue()).darker());


        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        GlStateManager.popMatrix();



        mc.mntsb_18.drawCenteredStringWithShadow(getName(), x + 53.5f - 8 + 0.5f, 14 + 0.5f, Color.BLACK.getRGB());

        mc.mntsb_18.drawCenteredStringWithShadow(getName(), x + 53.5f - 8, 14, Color.WHITE.getRGB());

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
            //dragging = true;
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
