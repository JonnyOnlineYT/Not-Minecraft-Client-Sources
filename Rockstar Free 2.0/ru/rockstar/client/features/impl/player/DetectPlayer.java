package ru.rockstar.client.features.impl.player;

import java.awt.Color;

import net.minecraft.entity.player.EntityPlayer;
import ru.rockstar.api.event.EventTarget;
import ru.rockstar.api.event.event.Event2D;
import ru.rockstar.api.utils.render.DrawHelper;
import ru.rockstar.client.features.Category;
import ru.rockstar.client.features.Feature;
import ru.rockstar.client.ui.settings.impl.BooleanSetting;
import ru.rockstar.client.ui.settings.impl.ListSetting;
import ru.rockstar.client.ui.settings.impl.NumberSetting;

public class DetectPlayer extends Feature {

	public NumberSetting dist;
	public ListSetting mode;
	public BooleanSetting back = new BooleanSetting("Background", true, () -> mode.currentMode.equals("Default") || mode.currentMode.equals("Hueta"));
	
    public DetectPlayer() {
        super("TextRadar", "�������� ������� � ������������ �������", 0, Category.VISUALS);
        mode = new ListSetting("List Mode", "Default", () -> true, "Default");
        dist = new NumberSetting("Distance", 5f, 5f, 500f, 1f, () -> true);
        addSettings(dist, mode, back);
    }
    
    @EventTarget
    public void ebatkopat(Event2D render) {
    	int offset = 44;
    	float pY = -12.0F;
    	String watermark = mode.getOptions();
    	for (EntityPlayer entityPlayer : mc.world.playerEntities) {
            if (entityPlayer != mc.player) {
            	if (watermark.equalsIgnoreCase("Hueta")) {
            	if (mc.player.getDistanceToEntity(entityPlayer) <= dist.getNumberValue()) {
	                String play = entityPlayer.getName();
	                int x = (int) entityPlayer.posX;
	                int y = (int) entityPlayer.posY;
	                int z = (int) entityPlayer.posZ;
	                int cords = (int) ((int) entityPlayer.posX + entityPlayer.posY + entityPlayer.posZ);
	                int blocks = (int) mc.player.getDistanceToEntity(entityPlayer);
	                if(back.getBoolValue()) {
	                DrawHelper.drawRectWithGlow(2, 9 + offset, 142, offset + 27, 5,5, new Color(32, 32, 32, 150));
	                }
	                DrawHelper.drawRect(1f, 30, 143, 50, new Color(35,35,35).getRGB());
	                
	                DrawHelper.drawNewRect(3.5, 51.2,  140, 52.2, new Color(35,35,35).getRGB());
	                DrawHelper.drawRect(1,2,3,4, new Color(15,15,15, 120).getRGB());
	                
					mc.neverlose500_17.drawCenteredString("Enemy Players", 69, 38, -1);
	                mc.neverlose500_16.drawString(play + " (" + x + " " + y + " " + z + ")  " + blocks, 5, 15 + offset, -1);
	            	
	                offset += 20;
	                pY -= 11;
	                
            		}
            	} else if (watermark.equalsIgnoreCase("Default")) {
            		
            		if (mc.player.getDistanceToEntity(entityPlayer) <= dist.getNumberValue()) {
    	                String play = entityPlayer.getName();
    	                int x = (int) entityPlayer.posX;
    	                int y = (int) entityPlayer.posY;
    	                int z = (int) entityPlayer.posZ;
    	                int cords = (int) ((int) entityPlayer.posX + entityPlayer.posY + entityPlayer.posZ);
    	                int blocks = (int) mc.player.getDistanceToEntity(entityPlayer);
    	                
    	                if(back.getBoolValue()) {
    	                	DrawHelper.drawRect(2, 8 + offset, 142, offset + 26, new Color(32, 32, 32, 150).getRGB());
    	                }
    	                DrawHelper.drawRect(1,2,3,4, new Color(15,15,15, 120).getRGB());
    	                mc.mntsb.drawString(play + " (" + cords + ")  " + blocks, 5, 15 + offset, -1);
    	            	
    	                offset += 20;
    	                pY -= 11;
    	                
                		}
            	}
            	
            }
    	}
    	
    }
}