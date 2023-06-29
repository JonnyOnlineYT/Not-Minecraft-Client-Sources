package ru.rockstar.client.features.impl.display;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import javafx.animation.Interpolator;
import net.minecraft.client.renderer.GlStateManager;
import ru.rockstar.api.event.EventTarget;
import ru.rockstar.api.event.event.EventRender2D;
import ru.rockstar.api.utils.render.ClientHelper;
import ru.rockstar.api.utils.render.DrawHelper;
import ru.rockstar.client.features.Category;
import ru.rockstar.client.features.Feature;
import ru.rockstar.client.ui.draggable.impl.KeystrokesComponent;
import ru.rockstar.client.ui.settings.impl.NumberSetting;

public class Keystrokes extends Feature {

    public int lastA = 0;
    public int lastW = 0;
    public int lastS = 0;
    public int lastD = 0;
    public static float progress;
    private long lastMS;
    public int lastJ = 0;
    public int lastS2 = 0;
    public long deltaAnim;
    public static NumberSetting x;
    public static NumberSetting y;

    public Keystrokes() {
        super("Keystrokes", "Показывает нажатые клавиши", 0, Category.DISPLAY);
        deltaAnim = 0;
        x = new NumberSetting("Indicators X", 0, 0, 1500, 1, () -> true);
        y = new NumberSetting("Indicators Y", 0, 0, 1500, 1, () -> true);
        //addSettings(x,y);
    }

    @EventTarget
    public void onRender2D(EventRender2D event) {
    	if (this.progress >= 1.0f) {
            this.progress = 1.0f;
        }
        else {
            this.progress = (System.currentTimeMillis() - this.lastMS) / 850.0f;
        }
    	
    	float x2 = KeystrokesComponent.x;
    	float y2 = KeystrokesComponent.y;
    	
        boolean A = mc.gameSettings.keyBindLeft.pressed;
        boolean W = mc.gameSettings.keyBindForward.pressed;
        boolean S = mc.gameSettings.keyBindBack.pressed;
        boolean D = mc.gameSettings.keyBindRight.pressed;
        boolean J = mc.gameSettings.keyBindJump.pressed;
        boolean S2 = mc.gameSettings.keyBindSneak.pressed;
        int alphaA = A ? 255 : 0;
        int alphaW = W ? 255 : 0;
        int alphaS = S ? 255 : 0;
        int alphaD = D ? 255 : 0;
        int alphaJ = J ? 255 : 0;
        int alphaS2 = S2 ? 255 : 0;
        float diff;

        this.lastA = (int)Interpolator.LINEAR.interpolate((double)this.lastA, A ? 100 : 0, 0.15);
        this.lastW = (int)Interpolator.LINEAR.interpolate((double)this.lastW, W ? 100 : 0, 0.15);
        this.lastS = (int)Interpolator.LINEAR.interpolate((double)this.lastS, S ? 100 : 0, 0.15);
        this.lastD = (int)Interpolator.LINEAR.interpolate((double)this.lastD, D ? 100 : 0, 0.15);
        
    	GlStateManager.pushMatrix();
    	
    	
    	
        
        GlStateManager.pushMatrix();
        DrawHelper.drawGradientRect1(5.0F + x2, 49.0F + y2, 25.0F + x2, 69.0F + y2, ClientHelper.getClientColor().darker().darker().darker().getRGB(), ClientHelper.getClientColor().darker().darker().getRGB());
        mc.mntsb_18.drawCenteredString("A", 15.0F + x2, 56.0F + y2, -1);
        GlStateManager.popMatrix();
		GlStateManager.pushMatrix();
		GlStateManager.enable(GL11.GL_SCISSOR_TEST);
        DrawHelper.scissorRect(5.0F + x2, 49.5F + y2, 25.0F + x2, 69.5F + y2);
        
        DrawHelper.drawCircle(15.0F + x2, 59.0F + y2, 0, 360, lastA / 4, 1, true, new Color(255,255,255,150 - lastA));
		
		GlStateManager.disable(GL11.GL_SCISSOR_TEST);
        
        
        DrawHelper.drawGradientRect1(27.0F + x2, 27.0F + y2, 47.0F + x2, 47.0F + y2, ClientHelper.getClientColor().darker().darker().getRGB(), ClientHelper.getClientColor().darker().getRGB());
        mc.mntsb_18.drawCenteredString("W", 37.0F + x2, 34.0F + y2, -1);
        GlStateManager.popMatrix();
		GlStateManager.pushMatrix();
		GlStateManager.enable(GL11.GL_SCISSOR_TEST);
        DrawHelper.scissorRect(27.0F + x2, 27.5F + y2, 47.0F + x2, 47.5F + y2);
        
        DrawHelper.drawCircle(37.0F + x2, 37.0F + y2, 0, 360, lastW / 4, 1, true, new Color(255,255,255,150 - lastW));
		
		GlStateManager.disable(GL11.GL_SCISSOR_TEST);
        
        
        
        DrawHelper.drawGradientRect1(27.0F + x2, 49.0F + y2, 47.0F + x2, 69.0F + y2, ClientHelper.getClientColor().darker().darker().getRGB(), ClientHelper.getClientColor().darker().getRGB());
        mc.mntsb_18.drawCenteredString("S", 37.0F + x2, 56.0F + y2, -1);
        GlStateManager.popMatrix();
      		GlStateManager.pushMatrix();
      		GlStateManager.enable(GL11.GL_SCISSOR_TEST);
              DrawHelper.scissorRect(27.0F + x2, 49.5F + y2, 47.0F + x2, 69.5F + y2);
              
              DrawHelper.drawCircle(37.0F + x2, 59.0F + y2, 0, 360, lastS / 4, 1, true, new Color(255,255,255,150 - lastS));
      		
      		GlStateManager.disable(GL11.GL_SCISSOR_TEST);
              
        
        
        
        DrawHelper.drawGradientRect1(49.0F + x2, 49.0F + y2, 69.0F + x2, 69.0F + y2, ClientHelper.getClientColor().darker().getRGB(), ClientHelper.getClientColor().getRGB());
        mc.mntsb_18.drawCenteredString("D", 59.0F + x2, 56.0F + y2, -1);
       
        GlStateManager.popMatrix();
  		GlStateManager.pushMatrix();
  		GlStateManager.enable(GL11.GL_SCISSOR_TEST);
          DrawHelper.scissorRect(49.0F + x2, 49.5F + y2, 69.0F + x2, 69.5F + y2);
          
          DrawHelper.drawCircle(59.0F + x2, 59.0F + y2, 0, 360, lastD / 4, 1, true, new Color(255,255,255,150 - lastD));
  		
  		GlStateManager.disable(GL11.GL_SCISSOR_TEST);
          
    
        
  	    GlStateManager.popMatrix();
        GlStateManager.popMatrix();
    }
    
    @Override
    public void onEnable() {
    	this.lastMS = System.currentTimeMillis();
    	this.progress = 0.0f;
        super.onEnable();
    }
}
