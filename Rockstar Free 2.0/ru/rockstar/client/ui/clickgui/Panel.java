package ru.rockstar.client.ui.clickgui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import ru.rockstar.Main;
import ru.rockstar.api.utils.render.AnimationHelper;
import ru.rockstar.api.utils.render.BlurUtil;
import ru.rockstar.api.utils.render.ClientHelper;
import ru.rockstar.api.utils.render.DrawHelper;
import ru.rockstar.api.utils.render.RenderUtils;
import ru.rockstar.api.utils.render.RoundedUtil;
import ru.rockstar.api.utils.world.TimerHelper;
import ru.rockstar.client.features.Category;
import ru.rockstar.client.features.Feature;
import ru.rockstar.client.features.impl.display.ClickGUI;
import ru.rockstar.client.ui.clickgui.Panel;
import ru.rockstar.client.ui.clickgui.component.AnimationState;
import ru.rockstar.client.ui.clickgui.component.Component;
import ru.rockstar.client.ui.clickgui.component.DraggablePanel;
import ru.rockstar.client.ui.clickgui.component.ExpandableComponent;
import ru.rockstar.client.ui.clickgui.component.impl.ModuleComponent;

import java.awt.*;
import java.util.List;

import org.lwjgl.opengl.GL11;

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
    static boolean inPanel;
    int y2;

    public Panel(Category category, int x, int y) {
        super(null, category.name(), x, y, HEADER_WIDTH, HEADER_HEIGHT);

        int moduleY = HEADER_HEIGHT;
        this.state = AnimationState.STATIC;
        this.features = Main.instance.featureDirector.getFeaturesForCategory(category);
        for (Feature module : features) {
            this.components.add(new ModuleComponent(this, module, X_ITEM_OFFSET, moduleY, HEADER_WIDTH - (X_ITEM_OFFSET * 2), ITEM_HEIGHT));
            moduleY += ITEM_HEIGHT;
        }
        this.type = category;
    }
    
    public void setY2(int y2) {
    	this.y2 = y2;
    }
    
    public int getY2() {
    	return this.y2;
    }

    @Override
    public void drawComponent(ScaledResolution scaledResolution, int mouseX, int mouseY) {
    	TimerHelper timerHelper = new TimerHelper();
    	int color = 0;
    	int x = getX();
        int y = getY();
        setExpanded(true);
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
        if (!ClickGUI.style.getCurrentMode().equalsIgnoreCase("Rockstar New")) {
        	if (dragging) {
                setX(mouseX - prevX);
                setY(mouseY - prevY);
            }
        }
        int width = getWidth();
        int height = getHeight();
        int headerHeight;
        int heightWithExpand = getHeightWithExpand();
        headerHeight = (isExpanded() ? heightWithExpand : height);

        float extendedHeight = 2;
        int yTotal = 0;

        String mode = ClickGUI.style.getOptions();
        boolean animated = ClickGuiScreen.progress * 1 >= 1.0f;
        GlStateManager.pushMatrix();
        if (mode.equalsIgnoreCase("Rockstar")) {
        	//DrawHelper.drawRectWithGlow(x + 3, y + 13, x + width - 3, y + (headerHeight - extendedHeight) * ClickGuiScreen.progress, 7, 8, new Color(20, 20, 20,255));
        	
        //	DrawHelper.drawSmoothRect(x + 3, y + 13, x + width - 3, y + (headerHeight - extendedHeight) * ClickGuiScreen.progress, new Color(20, 20, 20,255).getRGB());
        	
         //   DrawHelper.drawGradientRect1(x + 2, y + headerHeight - extendedHeight + 1.3f - 4, x + 2 + (width - 4) * (animated ? ClickGuiScreen.progress2 * 2 : 0), y + (headerHeight - extendedHeight), new Color(color).darker().getRGB(), color);

        	if (type.getName().equalsIgnoreCase("Combat") || type.getName().equalsIgnoreCase("Movement") || type.getName().equalsIgnoreCase("Visuals")) {
        		
        		
        		RoundedUtil.drawGradientHorizontal((x - 1 + 1 - 1) * ClickGuiScreen.progress, y + 1 - 3, (width + 1 - 5 + 3) + (1 + 1 - ClickGuiScreen.progress) + 2, 18 + (headerHeight - extendedHeight) - 17 + 2, 7 , ClickGUI.clickGuiColor.currentMode.equalsIgnoreCase("Static") ?onecolor.darker() : new Color(color).darker(), ClickGUI.clickGuiColor.currentMode.equalsIgnoreCase("Static") ? onecolor : new Color(color));
        		
        		
                mc.mntsb_17.drawString(getName(), (x + 3) * ClickGuiScreen.progress, y + HEADER_HEIGHT / 2F - 3.5f, Color.WHITE.getRGB());
                DrawHelper.drawImage(new ResourceLocation("rockstar/icons/" + type.getName() + ".png"), (x + 88) * ClickGuiScreen.progress, y + 3, 10, 10, Color.WHITE);
        	} else {
        		RoundedUtil.drawGradientHorizontal((x - 1) * (1 + 1 - ClickGuiScreen.progress), y + 1 - 3, (width + 1) - (1 + 1 - ClickGuiScreen.progress) + 2, 18 + (headerHeight - extendedHeight) - 17 + 2, 7 , ClickGUI.clickGuiColor.currentMode.equalsIgnoreCase("Static") ? onecolor.darker() : new Color(color).darker(), ClickGUI.clickGuiColor.currentMode.equalsIgnoreCase("Static") ? onecolor : new Color(color));

                mc.mntsb_17.drawString(getName(), (x + 3) * (1 + 1 - ClickGuiScreen.progress), y + HEADER_HEIGHT / 2F - 3.5f, Color.WHITE.getRGB());
                DrawHelper.drawImage(new ResourceLocation("rockstar/icons/" + type.getName() + ".png"), (x + 88) * (1 + 1 - ClickGuiScreen.progress), y + 3, 10, 10, Color.WHITE);
        	}
        }
        if (mode.equalsIgnoreCase("Rockstar New")) {

        		RoundedUtil.drawRound((x - 1 + 40 - 40 * ClickGuiScreen.progress), y2 + 30 + 2 - 3 + 70 - 70 * ClickGuiScreen.progress, (width + 1) -1 + 2- 80 + 80 * ClickGuiScreen.progress, 1 - 3 + 18 + (y2 + 260 + 4) - 49 - 140 + 140 * ClickGuiScreen.progress, 7,  new Color(40,40,40, (int) (255 * ClickGuiScreen.progress)));
        		
        		DrawHelper.drawGradientRect((x - 1 - 2 + 3), y2 + 48 + 1 - 4, (x - 1 + 2 - 1)+ (width + 1) - (1 - 40 + 40 * ClickGuiScreen.progress), 1 - 3 + 18 + (y2 + 50.3f + 4) - 17 - 70 + 70 * ClickGuiScreen.progress, new Color(0,0,0,55).getRGB(), new Color(0,0,0,0).getRGB());
        		ScaledResolution sr = new ScaledResolution(mc);  
        		GlStateManager.pushMatrix();
            	GlStateManager.enable(GL11.GL_SCISSOR_TEST);
            	DrawHelper.drawNewRect(x, y, x, y, new Color(30, 30, 30, 255).getRGB());
                DrawHelper.scissorRect(0, 0, sr.getScaledWidth(), y2 + 30 + 1 - 3 + 18);

                
                Color l1 = ClickGUI.clickGuiColor.currentMode.equalsIgnoreCase("Static") ? onecolor.brighter() : new Color(color).brighter();
                Color l2 = ClickGUI.clickGuiColor.currentMode.equalsIgnoreCase("Static") ? onecolor : new Color(color);
            	
                if (ClickGUI.glow.getBoolValue()) {
                    RenderUtils.drawBlurredShadow((x), y2 + 30 + 1 - 3 + 2, 102 - 2, 22 - 2, 10, l1);
            	}
                
                RoundedUtil.drawGradientRound((x),  + 70 - 70 * ClickGuiScreen.progress + y2 + 30 + 1 - 3 + 2, 102 - 2, 22 - 2, 6, l2, l2, l2, l1);

                mc.mntsb_20.drawCenteredString(getName(), (x + 3 + 47), + 70 - 70 * ClickGuiScreen.progress + y2 + 30 + HEADER_HEIGHT / 2F - 5.5f + 1, color == -1 || onecolor.getRGB() == -1 ? Color.WHITE.getRGB() : Color.WHITE.getRGB());
        
                
                GlStateManager.disable(GL11.GL_SCISSOR_TEST);
          		GlStateManager.popMatrix();
          		
          		
          		GlStateManager.pushMatrix();
            	GlStateManager.enable(GL11.GL_SCISSOR_TEST);
            	DrawHelper.drawNewRect(x, y, x, y, new Color(30, 30, 30, 255).getRGB());
                DrawHelper.scissorRect(0, 253, sr.getScaledWidth(), 265);
                
                
                if (ClickGUI.glow.getBoolValue()) {
                    RenderUtils.drawBlurredShadow((x), 243, 102 - 2, 20 - 4, 10, l1);
            	}
                
                RoundedUtil.drawGradientRound((x), - 70 + 70 * ClickGuiScreen.progress + 243, 102 - 2, 20 - 4, 6, l2, l2, l2, l1);
        		
                
                GlStateManager.disable(GL11.GL_SCISSOR_TEST);
          		GlStateManager.popMatrix();
        		
        }
        if (mode.equalsIgnoreCase("Default Dark")) {
        	DrawHelper.drawSmoothRect(x + 3, y + 13, x + width - 3, y + (headerHeight - extendedHeight) * ClickGuiScreen.progress, new Color(0, 0, 0,185).getRGB());

            DrawHelper.drawGradientRect1(x + 2, y + headerHeight - extendedHeight + 1.3f - 4, x + (width - 2) * (animated ? ClickGuiScreen.progress2 : 0), y + headerHeight - extendedHeight, color, color - 70);

            
            DrawHelper.drawGradientRect(x - 1, y + 1, x + width + 1, y + 18 - 4, new Color(25, 25, 25, 255).getRGB(), new Color(0,0,0,255).getRGB());

            mc.mntsb_17.drawStringWithShadow(getName(), x + 12, y + HEADER_HEIGHT / 2F - 3.5f, Color.LIGHT_GRAY.getRGB());
            
            DrawHelper.drawImage(new ResourceLocation("rockstar/icons/" + type.getName() + ".png"), x + 1, y + 3, 10, 10, Color.LIGHT_GRAY);
        }
        if (mode.equalsIgnoreCase("Default Light")) {
        	DrawHelper.drawSmoothRect(x + 3, y + 13, x + width - 3, y + (headerHeight - extendedHeight) * ClickGuiScreen.progress, new Color(255, 255, 255,200).getRGB());

            DrawHelper.drawGradientRect1(x + 2, y + headerHeight - extendedHeight + 1.3f - 4, x + (width - 2) * (animated ? ClickGuiScreen.progress2 : 0), y + headerHeight - extendedHeight, color, color - 70);

            
            DrawHelper.drawGradientRect(x - 1, y + 1, x + width + 1, y + 18 - 4, new Color(200, 200, 200, 255).getRGB(), new Color(255,255,255,255).getRGB());

            mc.mntsb_17.drawStringWithShadow(getName(), x + 12, y + HEADER_HEIGHT / 2F - 3.5f, Color.LIGHT_GRAY.getRGB());
            
            DrawHelper.drawImage(new ResourceLocation("rockstar/icons/" + type.getName() + ".png"), x + 1, y + 3, 10, 10, Color.LIGHT_GRAY);
        }
        if (mode.equalsIgnoreCase("NeverLose")) {
        	DrawHelper.drawSmoothRect(x + 3, y + 14, x + width - 3, y + (headerHeight - extendedHeight) * ClickGuiScreen.progress, new Color(0, 10, 20,255).getRGB());
            DrawHelper.drawGradientRect(x - 1, y + 1, x + width + 1, y + 18 - 4, new Color(0, 60, 90,200).getRGB(), new Color(0, 60, 90,200).getRGB());
            mc.mntsb_17.drawStringWithShadow(getName(), x + 12, y + HEADER_HEIGHT / 2F - 3.5f, Color.WHITE.getRGB());
            DrawHelper.drawImage(new ResourceLocation("rockstar/icons/" + type.getName() + ".png"), x + 1, y + 3, 10, 10, new Color(0, 200, 255,255));
        }
        if (mode.equalsIgnoreCase("Clear")) {
        	DrawHelper.drawGradientRect1(x + 1, y + 14, x + width - 97, y +(headerHeight - extendedHeight) * ClickGuiScreen.progress, new Color(0, 0, 0,0).getRGB(), new Color(0, 0, 0,255).getRGB());
        	DrawHelper.drawGradientRect1(x + 98, y + 14, x + width, y + headerHeight - extendedHeight, new Color(0, 0, 0,255).getRGB(), new Color(0, 0, 0,0).getRGB());
        	DrawHelper.drawGradientRect(x + 2, y + headerHeight - extendedHeight, x + width - 2, y + headerHeight - extendedHeight + 2, new Color(0, 0, 0,255).getRGB(), new Color(0, 0, 0,0).getRGB());
        	
            DrawHelper.drawGradientRect(x - 1, y + 1, x + width + 1, y + 18 - 4, new Color(0, 0, 0,255).getRGB(), new Color(60, 60, 60,255).getRGB());
            mc.mntsb_17.drawStringWithShadow(getName(), x + 12, y + HEADER_HEIGHT / 2F - 3.5f, Color.LIGHT_GRAY.getRGB());
            DrawHelper.drawImage(new ResourceLocation("rockstar/icons/" + type.getName() + ".png"), x + 1, y + 3, 10, 10, Color.LIGHT_GRAY);
        }
        if (mode.equalsIgnoreCase("Dark")) {
        	DrawHelper.drawSmoothRect(x + 3, y + 14, x + width - 3, y + (headerHeight - extendedHeight) * ClickGuiScreen.progress, new Color(20, 20, 20,255).getRGB());
            DrawHelper.drawGradientRect(x - 1, y + 1, x + width + 1, y + 18 - 4, new Color(0, 0, 0,200).getRGB(), new Color(20, 20, 20,200).getRGB());
            mc.mntsb_17.drawStringWithShadow(getName(), x + 12, y + HEADER_HEIGHT / 2F - 3.5f, Color.LIGHT_GRAY.getRGB());
            DrawHelper.drawImage(new ResourceLocation("rockstar/icons/" + type.getName() + ".png"), x + 1, y + 3, 10, 10, Color.LIGHT_GRAY);
        }
        if (mode.equalsIgnoreCase("Light")) {
        	DrawHelper.drawSmoothRect(x + 3, y + 14, x + width - 3, y + (headerHeight - extendedHeight) * ClickGuiScreen.progress, new Color(255, 255, 255,255).getRGB());
            DrawHelper.drawGradientRect(x - 1, y + 1, x + width + 1, y + 18 - 4, new Color(245, 245, 245,250).getRGB(), new Color(245, 245, 245,250).getRGB());
            mc.mntsb_17.drawStringWithShadow(getName(), x + 12, y + HEADER_HEIGHT / 2F - 3.5f, Color.LIGHT_GRAY.getRGB());
            DrawHelper.drawImage(new ResourceLocation("rockstar/icons/" + type.getName() + ".png"), x + 1, y + 3, 10, 10, Color.LIGHT_GRAY);
        }
        GlStateManager.popMatrix();

        if (mouseY < y2 + 260 || mouseY > y2 + 30) {
    		inPanel = true;
    	} else {
    		inPanel = false;
    	}

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
