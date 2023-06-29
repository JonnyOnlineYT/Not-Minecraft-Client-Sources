package ru.rockstar.client.ui.draggable.impl;

import java.awt.Color;
import java.util.ArrayList;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextFormatting;
import ru.rockstar.Main;
import ru.rockstar.api.utils.render.ClientHelper;
import ru.rockstar.api.utils.render.DrawHelper;
import ru.rockstar.api.utils.render.RoundedUtil;
import ru.rockstar.client.features.impl.display.DamageFlyIndicator;
import ru.rockstar.client.features.impl.display.PotionIndicator;
import ru.rockstar.client.features.impl.display.StaffIndicator;
import ru.rockstar.client.features.impl.misc.StaffAlert;
import ru.rockstar.client.features.impl.movement.DamageFly;
import ru.rockstar.client.ui.draggable.DraggableModule;

public class StaffComponent extends DraggableModule {
	public static int x2;
	public static int y2;
    public StaffComponent() {
        super("TimerComponent", sr.getScaledWidth() - 300, sr.getScaledHeight() - 200);
    }
    public static int x;
    public static int y;

    @Override
    public int getWidth() {
        return 100;
    }
    int height;
    @Override
    public int getHeight() {
        return 100;
    }

    @Override
    public void render(int mouseX, int mouseY) {
    	if (this.mc.player == null || this.mc.world == null || !Main.instance.featureDirector.getFeatureByClass(StaffIndicator.class).isToggled()) {
            return;
        }
    	
    	GlStateManager.pushMatrix();
        GlStateManager.enableTexture2D();
    	x2 = (int) getX();
    	y2 = (int) getY();
    	x = getX();
    	y = getY();
    	GlStateManager.pushMatrix();
    	GlStateManager.enableAlpha();
    	 ArrayList<EntityPlayer> vstaff = new ArrayList();
    	
    	if (StaffIndicator.mode.currentMode.equalsIgnoreCase("Rockstar New")) {
    		RoundedUtil.drawHorizontalGradientOutlinedRoundedRectWithGlow(x2 - 2, y2, 170 + 4, 15 + height, 5, 1, 15, ClientHelper.getClientColor(), ClientHelper.getClientColor().darker());
    	
    		mc.mntsb_20.drawString("Статистика администрации", x2, y2 + 3, -1);
    	} else if (StaffIndicator.mode.currentMode.equalsIgnoreCase("Rockstar")) {
    		RoundedUtil.drawGradientRound(x2 - 2, y2, 170 + 4, 15 + height, 5, ClientHelper.getClientColor(),ClientHelper.getClientColor(), ClientHelper.getClientColor().darker(), ClientHelper.getClientColor().darker());
    	
    		mc.mntsb_20.drawString("Статистика администрации", x2, y2 + 3, -1);
    	} else if (StaffIndicator.mode.getCurrentMode().equalsIgnoreCase("Rockstar Styled")) {

            Color l1 = ClientHelper.getClientColor().brighter();
            
            Color l2 = ClientHelper.getClientColor();

        	
            RoundedUtil.drawRound(x2 - 1, y2 - 1, 170, 15 + height + 1.5f, 5, new Color(40,40,40));
       
            //RenderUtils.drawBlurredShadow(x - 3 + 4, y - 16.0f + 4, 12, 12, 10, l1);
            
            RoundedUtil.drawGradientRound(x2 + 1, y2 + 1, 12, 12, 4, l2, l1, l1, l2);
            
            mc.mntsb_20.drawString("A", x2 + 3, y2 + 3, -1);
            
            Gui.drawRect(x2, y2 - 12 + 13 + 13.5, x2 + 1.5f + 15, y2- 12+12 + 5 + 13.5f, new Color(40,40,40).getRGB());
            
            mc.mntsb_18.drawString("Статистика администрации", x2 + 16, y2 + 4, -1);
        }
    	
    	
    	if (StaffAlert.staff.isEmpty()) {
    		if (StaffIndicator.mode.getCurrentMode().equalsIgnoreCase("Rockstar Styled")) {
    		mc.mntsb_15.drawString("Никого нету)", x2 + 3.5f, y2 + height + 7.5f, Color.GRAY.getRGB());
    		} else {
    		mc.mntsb_15.drawString("Никого нету)", x2 + 1, y2 + height + 6, Color.GRAY.getRGB());
    		}
    		height = 10;
    	} else {
    		for (EntityPlayer staff : StaffAlert.staff) {
        		mc.mntsb_15.drawStringWithShadow(GuiPlayerTabOverlay.getPlayers().contains(staff) ? staff.getDisplayName().getUnformattedText() : TextFormatting.RED + "(V) " + TextFormatting.RESET + staff.getDisplayName().getUnformattedText(), x2 + 5, y2 + ((StaffAlert.staff.indexOf(staff) + 1) * 10) + 6, Color.GRAY.getRGB());
        		height = StaffAlert.staff.size() * 10;
        	
        		if (mc.world == null ) {
        			StaffAlert.staff.remove(StaffAlert.staff.indexOf(staff));
        		}
    		}
    	}
    	
    	
    	GlStateManager.popMatrix();
    	GlStateManager.disableAlpha();
        GlStateManager.enableDepth();
        GlStateManager.disableLighting();
        GlStateManager.popMatrix();
        super.render(mouseX, mouseY);
    }

    @Override
    public void draw() {
        super.draw();
    }
}