package ru.rockstar.client.ui.espgble.impl;

import java.awt.Color;

import net.minecraft.client.renderer.GlStateManager;
import ru.rockstar.api.utils.render.ClientHelper;
import ru.rockstar.api.utils.render.DrawHelper;
import ru.rockstar.client.features.impl.display.TimerIndicator;
import ru.rockstar.client.features.impl.movement.Timer;
import ru.rockstar.client.features.impl.visuals.ESP;
import ru.rockstar.client.ui.espgble.DraggableModule;

public class EspComponent extends DraggableModule {
	int x2,y2;
    public EspComponent() {
        super("EspComponent", sr.getScaledWidth() - 100, sr.getScaledHeight() - 20);
    }
    public static int x;
    public static int y;

    @Override
    public int getWidth() {
        return 70;
    }

    @Override
    public int getHeight() {
        return 130;
    }

    @Override
    public void render(int mouseX, int mouseY) {
    	x = getX();
    	y = getY();

    		 double posX = getX();
             double posY = getY();
             double endPosX = getX() + 70;
             double endPosY = getY() + 130;
             
            //left

             DrawHelper.drawRect(posX - 1, posY, posX - 0.5, endPosY- 0.5, ESP.color.getColorValue());

             //Button
             DrawHelper.drawRect(posX, endPosY - 1, endPosX- 0.5, endPosY- 0.5,  ESP.color.getColorValue());

             //Top
             DrawHelper.drawRect(posX - 1, posY, endPosX- 0.5, posY - 0.5,  ESP.color.getColorValue());

             //Right
             DrawHelper.drawRect(endPosX - 1, posY, endPosX- 0.5, endPosY- 0.5,  ESP.color.getColorValue());
    	
        super.render(mouseX, mouseY);
    }

    @Override
    public void draw() {
    	if (!TimerIndicator.a) {
    		return;
    	}
    	GlStateManager.pushMatrix();
        GlStateManager.enableTexture2D();
        x2 = (int) getX();
        x = getX();
        y = getY();
    	y2 = (int) getY();
    	GlStateManager.pushMatrix();
    	GlStateManager.enableAlpha();
    	DrawHelper.drawRect(x2 - 1, y2, x2 + 46, y2 + 45,new Color(30,30,30,255).getRGB());
    	DrawHelper.drawGradientRect1(x2 - 3f, y2 - 2.3f, x2 + 48, y2 + 10, ClientHelper.getClientColor().getRGB(), ClientHelper.getClientColor().getRGB() - 100);
    	mc.mntsb_19.drawStringWithShadow("TIMER",x2 + 5,y2 + 1,-1);
    	DrawHelper.drawCircle(x2 + 21.5f,y2 + 27,0,400, 11, 8, false, new Color(50,50,50,150));
    	DrawHelper.drawCircle(x2 + 21.5f,y2 + 27,400 - (float) (TimerIndicator.indicatorTimer * (400 / Timer.ticks)),400 - (float) (TimerIndicator.indicatorTimer * (400 / Timer.ticks)) + 10,11, 8, false, new Color(-1));
    	DrawHelper.drawCircle(x2 + 21.5f,y2 + 27,0,400 - (float) (TimerIndicator.indicatorTimer * (400 / Timer.ticks)), 11, 8, false, ClientHelper.getClientColor());
    	GlStateManager.popMatrix();
    	GlStateManager.disableAlpha();
        GlStateManager.enableDepth();
        GlStateManager.disableLighting();
        GlStateManager.popMatrix();
        super.draw();
    }
}