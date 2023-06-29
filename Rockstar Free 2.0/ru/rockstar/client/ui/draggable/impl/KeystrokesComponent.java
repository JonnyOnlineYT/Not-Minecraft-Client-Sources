package ru.rockstar.client.ui.draggable.impl;

import java.awt.Color;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import ru.rockstar.Main;
import ru.rockstar.api.utils.render.ClientHelper;
import ru.rockstar.api.utils.render.DrawHelper;
import ru.rockstar.client.features.impl.display.Keystrokes;
import ru.rockstar.client.features.impl.movement.TargetStrafe;
import ru.rockstar.client.features.impl.visuals.ArmorHUD;
import ru.rockstar.client.ui.draggable.DraggableModule;
import ru.rockstar.client.ui.settings.impl.NumberSetting;

public class KeystrokesComponent extends DraggableModule {
	public int lastA = 0;
    public int lastW = 0;
    public int lastS = 0;
    public int lastD = 0;
    public int lastJ = 0;
    public int lastS2 = 0;
    public long deltaAnim;
    public static int x;
    public static int y;
    
    public KeystrokesComponent() {
        super("KeystrokesComponent", 100, 100);
    }

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
    	if (Main.instance.featureDirector.getFeatureByClass(Keystrokes.class).isToggled()) {
    		GlStateManager.pushMatrix();
            GlStateManager.enableTexture2D();
            drag.setCanRender(true);
            x = getX();
            y = getY();
            float x3 = getX();
        	float y3 = getY();
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

            float x2 = getX();
        	float y2 = getY();
        	
            if (lastA != alphaA) {
                diff = alphaA - lastA;
                lastA = (int) (lastA + diff / 40);
            }

            if (lastW != alphaW) {
                diff = alphaW - lastW;
                lastW = (int) (lastW + diff / 40);
            }

            if (lastS != alphaS) {
                diff = alphaS - lastS;
                lastS = (int) (lastS + diff / 40);
            }

            if (lastD != alphaD) {
                diff = alphaD - lastD;
                lastD = (int) (lastD + diff / 40);
            }
            
            if (lastJ != alphaJ) {
                diff = alphaJ - lastJ;
                lastJ = (int) (lastJ + diff / 40);
            }
            
            if (lastS2 != alphaS2) {
                diff = alphaS2 - lastS2;
                lastS2 = (int) (lastS2 + diff / 40);
            }
            DrawHelper.drawGradientRect1(5.0F + x2, 49.0F + y2, 25.0F + x2, 69.0F + y2, ClientHelper.getClientColor().darker().darker().darker().getRGB(), ClientHelper.getClientColor().darker().darker().getRGB());
            DrawHelper.drawRect(5.0F + x2, 49.0F + y2, 25.0F + x2, 69.0F + y2, (new Color(lastA, lastA, lastA, lastA / 2)).getRGB());
            mc.mntsb_18.drawCenteredString("A", 15.0F + x2, 56.0F + y2, -1);
            
            DrawHelper.drawGradientRect1(27.0F + x2, 27.0F + y2, 47.0F + x2, 47.0F + y2, ClientHelper.getClientColor().darker().darker().getRGB(), ClientHelper.getClientColor().darker().getRGB());
            DrawHelper.drawRect(27.0F + x2, 27.0F + y2, 47.0F + x2, 47.0F + y2, (new Color(lastW, lastW, lastW, lastW / 2)).getRGB());
            mc.mntsb_18.drawCenteredString("W", 37.0F + x2, 34.0F + y2, -1);
            
            DrawHelper.drawGradientRect1(27.0F + x2, 49.0F + y2, 47.0F + x2, 69.0F + y2, ClientHelper.getClientColor().darker().darker().getRGB(), ClientHelper.getClientColor().darker().getRGB());
            DrawHelper.drawRect(27.0F + x2, 49.0F + y2, 47.0F + x2, 69.0F + y2, (new Color(lastS, lastS, lastS, lastS / 2)).getRGB());
            mc.mntsb_18.drawCenteredString("S", 37.0F + x2, 56.0F + y2, -1);
            
            DrawHelper.drawGradientRect1(49.0F + x2, 49.0F + y2, 69.0F + x2, 69.0F + y2, ClientHelper.getClientColor().darker().getRGB(), ClientHelper.getClientColor().getRGB());
            DrawHelper.drawRect(49.0F + x2, 49.0F + y2, 69.0F + x2, 69.0F + y2, (new Color(lastD, lastD, lastD, lastD / 2).getRGB()));
            mc.mntsb_18.drawCenteredString("D", 59.0F + x2, 56.0F + y2, -1);
            GlStateManager.enableDepth();
            GlStateManager.disableLighting();
            GlStateManager.popMatrix();
    	}
        super.render(mouseX, mouseY);
    }

    @Override
    public void draw() {
        if (Main.instance.featureDirector.getFeatureByClass(Keystrokes.class).isToggled()) {
            GlStateManager.pushMatrix();
            GlStateManager.enableTexture2D();
            float x3 = getX();
        	float y3 = getY();
        	x = getX();
            y = getY();
        	
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

            if (lastA != alphaA) {
                diff = alphaA - lastA;
                lastA = (int) (lastA + diff / 40);
            }

            if (lastW != alphaW) {
                diff = alphaW - lastW;
                lastW = (int) (lastW + diff / 40);
            }

            if (lastS != alphaS) {
                diff = alphaS - lastS;
                lastS = (int) (lastS + diff / 40);
            }

            if (lastD != alphaD) {
                diff = alphaD - lastD;
                lastD = (int) (lastD + diff / 40);
            }
            
            if (lastJ != alphaJ) {
                diff = alphaJ - lastJ;
                lastJ = (int) (lastJ + diff / 40);
            }
            
            if (lastS2 != alphaS2) {
                diff = alphaS2 - lastS2;
                lastS2 = (int) (lastS2 + diff / 40);
            }
            DrawHelper.drawRect(x3, y3 + 20, x3 + 74, y3 + 74, (new Color(30, 30, 30, 255)).getRGB());
            DrawHelper.drawGradientRect1(x3 - 2, y3 + 8, x3 + 76, y3 + 22, ClientHelper.getClientColor().getRGB(), ClientHelper.getClientColor().getRGB() - 100);
            mc.mntsb_16.drawStringWithShadow("Keystrokes", x3 + 12, y3 + 13,-1);
            
            DrawHelper.drawRect(5.0F + x3, 49.0F + y3, 25.0F + x3, 69.0F + y3, (new Color(lastA, lastA, lastA, 40)).getRGB());
            mc.mntsb_18.drawCenteredString("A", 15.0F + x3, 56.0F + y3, ClientHelper.getClientColor().getRGB());
            
            DrawHelper.drawRect(27.0F + x3, 27.0F + y3, 47.0F + x3, 47.0F + y3, (new Color(lastW, lastW, lastW, 40)).getRGB());
            mc.mntsb_18.drawCenteredString("W", 37.0F + x3, 34.0F + y3, ClientHelper.getClientColor().getRGB());
            
            DrawHelper.drawRect(27.0F + x3, 49.0F + y3, 47.0F + x3, 69.0F + y3, (new Color(lastS, lastS, lastS, 40)).getRGB());
            mc.mntsb_18.drawCenteredString("S", 37.0F + x3, 56.0F + y3, ClientHelper.getClientColor().getRGB());
            
            DrawHelper.drawRect(49.0F + x3, 49.0F + y3, 69.0F + x3, 69.0F + y3, (new Color(lastD, lastD, lastD, 40)).getRGB());
            mc.mntsb_18.drawCenteredString("D", 59.0F + x3, 56.0F + y3, ClientHelper.getClientColor().getRGB());
            
            GlStateManager.enableDepth();
            GlStateManager.disableLighting();
            GlStateManager.popMatrix();
        }
        super.draw();
    }
}
