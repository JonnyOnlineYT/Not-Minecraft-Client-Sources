package ru.rockstar.client.features.impl.display;

import java.awt.Color;
import java.util.ArrayList;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextFormatting;
import ru.rockstar.Main;
import ru.rockstar.api.event.EventTarget;
import ru.rockstar.api.event.event.Event2D;
import ru.rockstar.api.event.event.EventUpdate;
import ru.rockstar.api.utils.movement.MovementHelper;
import ru.rockstar.api.utils.render.ClientHelper;
import ru.rockstar.api.utils.render.DrawHelper;
import ru.rockstar.api.utils.render.RoundedUtil;
import ru.rockstar.client.features.Category;
import ru.rockstar.client.features.Feature;
import ru.rockstar.client.features.impl.misc.StaffAlert;
import ru.rockstar.client.features.impl.movement.Timer;
import ru.rockstar.client.ui.draggable.impl.StaffComponent;
import ru.rockstar.client.ui.settings.impl.ListSetting;
import ru.rockstar.client.ui.settings.impl.NumberSetting;

public class StaffIndicator extends Feature {
    int x2,y2;
    int height;
    public static float progress;
    private long lastMS;
    public static boolean a = false;
    public static ListSetting mode = new ListSetting("Mode", "Rockstar Styled", () -> true, "Rockstar", "Rockstar New", "Rockstar Styled");
    public static float indicatorTimer;
    public StaffIndicator(){
        super("StaffIndicator","Показывает процент флага таймера", 0, Category.DISPLAY);
        addSettings(mode);
    }
    
    @EventTarget
    public void render(Event2D render) {
    		GlStateManager.pushMatrix();
            GlStateManager.enableTexture2D();
            x2 = (int) StaffComponent.x;
        	y2 = (int) StaffComponent.y;
        	GlStateManager.pushMatrix();
        	GlStateManager.enableAlpha();
        	 ArrayList<EntityPlayer> vstaff = new ArrayList();
        	
        	if (StaffIndicator.mode.currentMode.equalsIgnoreCase("Rockstar New")) {
        		RoundedUtil.drawHorizontalGradientOutlinedRoundedRectWithGlow(x2 - 2, y2, 170 + 4, 15 + height, 5, 1, 15, ClientHelper.getClientColor(), ClientHelper.getClientColor().darker());
        	
        		mc.mntsb_20.drawString("Статистика администрации", x2, y2 + 3, -1);
        	} else if (StaffIndicator.mode.currentMode.equalsIgnoreCase("Rockstar")) {
        		RoundedUtil.drawGradientRound(x2 - 2, y2, 170 + 4, 15 + height, 5, ClientHelper.getClientColor(),ClientHelper.getClientColor(), ClientHelper.getClientColor().darker(), ClientHelper.getClientColor().darker());
        	
        		mc.mntsb_20.drawString("Статистика администрации", x2, y2 + 3, -1);
        	} else if (mode.getCurrentMode().equalsIgnoreCase("Rockstar Styled")) {

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
        		if (mode.getCurrentMode().equalsIgnoreCase("Rockstar Styled")) {
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
    }
    @Override
    public void onEnable() {
    	indicatorTimer = 0;
    	this.lastMS = System.currentTimeMillis();
    	this.progress = 0.0f;
        super.onEnable();
    }
}