package ru.rockstar.client.ui.draggable.impl;

import java.awt.Color;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import ru.rockstar.Main;
import ru.rockstar.api.utils.render.ClientHelper;
import ru.rockstar.api.utils.render.DrawHelper;
import ru.rockstar.client.features.impl.display.DamageFlyIndicator;
import ru.rockstar.client.features.impl.display.Keystrokes;
import ru.rockstar.client.features.impl.display.TimerIndicator;
import ru.rockstar.client.features.impl.movement.DamageFly;
import ru.rockstar.client.features.impl.movement.TargetStrafe;
import ru.rockstar.client.features.impl.movement.Timer;
import ru.rockstar.client.features.impl.visuals.ArmorHUD;
import ru.rockstar.client.ui.draggable.DraggableModule;
import ru.rockstar.client.ui.settings.impl.NumberSetting;

public class DmgflyComponent extends DraggableModule {
	public static int x2;
	public static int y2;
    public DmgflyComponent() {
        super("TimerComponent", sr.getScaledWidth() - 300, sr.getScaledHeight() - 200);
    }
    public static int x;
    public static int y;

    @Override
    public int getWidth() {
        return 100;
    }

    @Override
    public int getHeight() {
        return 100;
    }

    @Override
    public void render(int mouseX, int mouseY) {
    	if (!DamageFlyIndicator.a) {
    		return;
    	}
    	GlStateManager.pushMatrix();
        GlStateManager.enableTexture2D();
    	x2 = (int) getX();
    	y2 = (int) getY();
    	GlStateManager.pushMatrix();
    	GlStateManager.enableAlpha();
    	DrawHelper.drawRect(x2 - 1, y2, x2 + 46, y2 + 45,new Color(30,30,30,255).getRGB());
    	DrawHelper.drawGradientRect1(x2 - 3f, y2 - 2.3f, x2 + 48, y2 + 10, ClientHelper.getClientColor().getRGB(), ClientHelper.getClientColor().getRGB() - 100);
    	mc.mntsb_19.drawStringWithShadow("DMGFLY",x2,y2 + 1,-1);
    	DrawHelper.drawCircle(x2 + 21.5f,y2 + 27,0,400, 11, 8, false, new Color(50,50,50,150));
    	DrawHelper.drawCircle(x2 + 21.5f,y2 + 27,400 - (float) (DamageFlyIndicator.indicatorTimer * (400 / DamageFly.ticks)),400 - (float) (DamageFlyIndicator.indicatorTimer * (400 / DamageFly.ticks)) + 10,11, 8, false, new Color(-1));
    	DrawHelper.drawCircle(x2 + 21.5f,y2 + 27,0,400 - (float) (DamageFlyIndicator.indicatorTimer * (400 / DamageFly.ticks)), 11, 8, false, ClientHelper.getClientColor());
    	GlStateManager.popMatrix();
    	GlStateManager.disableAlpha();
        GlStateManager.enableDepth();
        GlStateManager.disableLighting();
        GlStateManager.popMatrix();
        super.render(mouseX, mouseY);
    }

    @Override
    public void draw() {
    	if (!DamageFlyIndicator.a) {
    		return;
    	}
    	GlStateManager.pushMatrix();
        GlStateManager.enableTexture2D();
    	x2 = (int) getX();
    	y2 = (int) getY();
    	GlStateManager.pushMatrix();
    	GlStateManager.enableAlpha();
    	DrawHelper.drawRect(x2 - 1, y2, x2 + 46, y2 + 45,new Color(30,30,30,255).getRGB());
    	DrawHelper.drawGradientRect1(x2 - 3f, y2 - 2.3f, x2 + 48, y2 + 10, ClientHelper.getClientColor().getRGB(), ClientHelper.getClientColor().getRGB() - 100);
    	mc.mntsb_19.drawStringWithShadow("DMGFLY",x2,y2 + 1,-1);
    	DrawHelper.drawCircle(x2 + 21.5f,y2 + 27,0,400, 11, 8, false, new Color(50,50,50,150));
    	DrawHelper.drawCircle(x2 + 21.5f,y2 + 27,400 - (float) (DamageFlyIndicator.indicatorTimer * (400 / DamageFly.ticks)),400 - (float) (DamageFlyIndicator.indicatorTimer * (400 / DamageFly.ticks)) + 10,11, 8, false, new Color(-1));
    	DrawHelper.drawCircle(x2 + 21.5f,y2 + 27,0,400 - (float) (DamageFlyIndicator.indicatorTimer * (400 / DamageFly.ticks)), 11, 8, false, ClientHelper.getClientColor());
    	GlStateManager.popMatrix();
    	GlStateManager.disableAlpha();
        GlStateManager.enableDepth();
        GlStateManager.disableLighting();
        GlStateManager.popMatrix();
        super.draw();
    }
}