package ru.rockstar.client.features.impl.display;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextFormatting;
import optifine.CustomColors;
import ru.rockstar.api.event.EventTarget;
import ru.rockstar.api.event.event.Event2D;
import ru.rockstar.api.utils.render.ClientHelper;
import ru.rockstar.api.utils.render.RoundedUtil;
import ru.rockstar.client.features.Category;
import ru.rockstar.client.features.Feature;
import ru.rockstar.client.features.impl.misc.StaffAlert;
import ru.rockstar.client.ui.draggable.impl.PotionComponent;
import ru.rockstar.client.ui.draggable.impl.StaffComponent;
import ru.rockstar.client.ui.settings.impl.ListSetting;

public class PotionIndicator extends Feature {
    int x2,y2;
    int height;
    public static float progress;
    private long lastMS;
    public static boolean a = false;
    public static ListSetting mode = new ListSetting("Mode", "Rockstar Styled", () -> true, "Rockstar", "Rockstar Styled", "Rockstar New");
    public static float indicatorTimer;
    public PotionIndicator(){
        super("PotionIndicator","Показывает процент флага таймера", 0, Category.DISPLAY);
        addSettings(mode);
    }
    
    @EventTarget
    public void render(Event2D render) {
    	  if (this.mc.gameSettings.showDebugInfo) {
              return;
          }
    		GlStateManager.pushMatrix();
            GlStateManager.enableTexture2D();
            x2 = (int) PotionComponent.x;
        	y2 = (int) PotionComponent.y;
        	GlStateManager.pushMatrix();
        	GlStateManager.enableAlpha();
        	   List<PotionEffect> potions = new ArrayList<>(mc.player.getActivePotionEffects());
               potions.sort(Comparator.comparingDouble(effect -> mc.fontRendererObj.getStringWidth((Objects.requireNonNull(Potion.getPotionById(CustomColors.getPotionId(effect.getEffectName()))).getName()))));
        	if (PotionIndicator.mode.currentMode.equalsIgnoreCase("Rockstar New")) {
        		RoundedUtil.drawHorizontalGradientOutlinedRoundedRectWithGlow(x2, y2, 100, 15 + height, 5, 1, 15, ClientHelper.getClientColor(), ClientHelper.getClientColor().darker());
        	
        		mc.mntsb_20.drawCenteredString("Зелья", x2 + 50, y2 + 3, -1);
        	} else if (PotionIndicator.mode.currentMode.equalsIgnoreCase("Rockstar")) {
        		RoundedUtil.drawGradientRound(x2, y2, 100, 15 + height, 5, ClientHelper.getClientColor(),ClientHelper.getClientColor(), ClientHelper.getClientColor().darker(), ClientHelper.getClientColor().darker());
        	
        		mc.mntsb_20.drawCenteredString("Зелья", x2 + 50, y2 + 3, -1);
        	} else if (mode.getCurrentMode().equalsIgnoreCase("Rockstar Styled")) {

                Color l1 = ClientHelper.getClientColor().brighter();
                
                Color l2 = ClientHelper.getClientColor();

            	
                RoundedUtil.drawRound(x2, y2, 100, 15 + height - 0.5f, 5, new Color(40,40,40));
                
                RoundedUtil.drawGradientRound(x2 - 3 + 4 + 1, y2 - 16.0f + 4 + 14, 12, 12, 4, l2, l1, l1, l2);
                
                this.mc.i30.drawString("n", (float) (x2 + 1.5) + 2.2f, y2 - 13.0f + 3.5f + 14, -1);
                Gui.drawRect(x2 + 1, y2 - 12 + 13 + 14 , x2 + 2.5f + 15, y2- 12+12 + 5 + 14, new Color(40,40,40).getRGB());
                
           
                mc.mntsb_20.drawString("Зелья", x2 + 17, y2 + 4, -1);
            	
            }
        	
        	if (potions.isEmpty()) {
        		if (mode.getCurrentMode().equalsIgnoreCase("Rockstar Styled")) {
        		mc.mntsb_15.drawString("Ниче нету)", x2 + 4, y2 + height + 6.5f, Color.GRAY.getRGB());
        		} else {
        		mc.mntsb_15.drawString("Ниче нету)", x2 + 5, y2 + height + 6, Color.GRAY.getRGB());
        		}
        		height = 12;
        	} else {
        		 for (PotionEffect potion : potions) {
        				Potion effect = Potion.getPotionById(CustomColors.getPotionId(potion.getEffectName()));
        				 String level = I18n.format(effect.getName());
                         if (potion.getAmplifier() == 1) {
                             level = level + " " + I18n.format("enchantment.level.2");
                         } else if (potion.getAmplifier() == 2) {
                             level = level + " " + I18n.format("enchantment.level.3");
                         } else if (potion.getAmplifier() == 3) {
                             level = level + " " + I18n.format("enchantment.level.4");
                         }
                         int getPotionColor = -1;
                         if ((potion.getDuration() < 200)) {
                             getPotionColor = new Color(255, 103, 32).getRGB();
                         } else if (potion.getDuration() < 400) {
                             getPotionColor = new Color(231, 143, 32).getRGB();
                         } else if (potion.getDuration() > 400) {
                             getPotionColor = Color.GRAY.getRGB();
                         }

                         String durationString = Potion.getDurationString(potion);

                         
            		mc.mntsb_15.drawString(level, x2 + 5, y2 + ((potions.indexOf(potion) + 1) * 12) + 6, Color.GRAY.getRGB());
            		
            		mc.mntsb_15.drawString(durationString, x2 + 95 - mc.mntsb_15.getStringWidth(durationString), y2 + ((potions.indexOf(potion) + 1) * 12) + 6, getPotionColor);

            		
            		height = potions.size() * 12;
            	
            		if (mc.world == null ) {
            			potions.remove(potions.indexOf(potion));
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