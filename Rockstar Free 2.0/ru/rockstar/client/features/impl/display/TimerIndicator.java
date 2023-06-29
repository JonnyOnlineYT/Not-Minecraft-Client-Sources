package ru.rockstar.client.features.impl.display;

import java.awt.Color;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.BlockPos;
import ru.rockstar.Main;
import ru.rockstar.api.event.EventTarget;
import ru.rockstar.api.event.event.Event2D;
import ru.rockstar.api.event.event.EventMouseKey;
import ru.rockstar.api.event.event.EventUpdate;
import ru.rockstar.api.utils.movement.MovementHelper;
import ru.rockstar.api.utils.render.ClientHelper;
import ru.rockstar.api.utils.render.DrawHelper;
import ru.rockstar.client.features.Category;
import ru.rockstar.client.features.Feature;
import ru.rockstar.client.features.impl.combat.KillAura;
import ru.rockstar.client.features.impl.movement.DamageFly;
import ru.rockstar.client.features.impl.movement.Timer;
import ru.rockstar.client.ui.settings.impl.NumberSetting;

public class TimerIndicator extends Feature {
	public static NumberSetting x;
    public static NumberSetting y;
    int x2,y2;
    public static float progress;
    private long lastMS;
    public static boolean a = false;
    public static float indicatorTimer;
    public TimerIndicator(){
        super("TimerIndicator","Показывает процент флага таймера", 0, Category.DISPLAY);
        x = new NumberSetting("Indicators X", 0, 0, 1500, 1, () -> true);
        y = new NumberSetting("Indicators Y", 0, 0, 1500, 1, () -> true);
        //addSettings(x,y);
    }
    
    @EventTarget
    public void render(Event2D render) {
    	if (this.progress >= 1.0f) {
            this.progress = 1.0f;
        }
        else {
            this.progress = (System.currentTimeMillis() - this.lastMS) / 850.0f;
        }
    	/*
    	if (a == true) {
    		GlStateManager.pushMatrix();
            GlStateManager.enableTexture2D();
            x2 = (int) TimerComponent.x;
        	y2 = (int) TimerComponent.y;
        	GlStateManager.pushMatrix();
        	GlStateManager.enableAlpha();
        	DrawHelper.drawRect(x2 - 1, y2, x2 + 46, y2 + (45) * progress,new Color(30,30,30,255).getRGB());
        	DrawHelper.drawGradientRect1(x2 - 3f, y2 - 2.3f, x2 + 48, y2 + 10, ClientHelper.getClientColor().getRGB(), ClientHelper.getClientColor().getRGB() - 100);
        	mc.mntsb_19.drawStringWithShadow("TIMER",x2 + 5,y2 + 1,-1);
        	DrawHelper.drawCircle(x2 + 21.5f,y2 + 27,0,400, 11, 8, false, new Color(50,50,50,150));
        	DrawHelper.drawCircle(x2 + 21.5f,y2 + 27,400 - (float) (indicatorTimer * (400 / Timer.ticks)),400 - (float) (indicatorTimer * (400 / Timer.ticks)) + 10,11, 8, false, new Color(-1));
        	DrawHelper.drawCircle(x2 + 21.5f,y2 + 27,0,400 - (float) (indicatorTimer * (400 / Timer.ticks)), 11, 8, false, ClientHelper.getClientColor());
        	GlStateManager.popMatrix();
        	GlStateManager.disableAlpha();
            GlStateManager.enableDepth();
            GlStateManager.disableLighting();
            GlStateManager.popMatrix();
    	}*/
    }
    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {
    	if (Main.instance.featureDirector.getFeatureByClass(Timer.class).isToggled()) {
    		indicatorTimer = Timer.tick;
    		a = true;
    	} else {
    		if (indicatorTimer >= 0 && MovementHelper.isMoving() != true && a == true) {
    			indicatorTimer -= 0.5f / (Timer.timer.getNumberValue() / 2);
    		} else if (indicatorTimer >= 0){
    			if (MovementHelper.isMoving() == true) {
    				indicatorTimer -= 0.4 / Timer.timer.getNumberValue() * 0.5f;
    			}
    		}
    		if (indicatorTimer < 0) {
    			indicatorTimer = 0;
    		}
    	}
    }
    @Override
    public void onEnable() {
    	indicatorTimer = 0;
    	this.lastMS = System.currentTimeMillis();
    	this.progress = 0.0f;
        super.onEnable();
    }
}