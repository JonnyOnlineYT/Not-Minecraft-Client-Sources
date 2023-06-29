package ru.rockstar.client.ui.draggable.impl;

import java.awt.Color;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import ru.rockstar.Main;
import ru.rockstar.api.utils.render.ClientHelper;
import ru.rockstar.api.utils.render.DrawHelper;
import ru.rockstar.api.utils.render.RoundedUtil;
import ru.rockstar.client.features.impl.display.InventoryPreview;
import ru.rockstar.client.features.impl.display.Keystrokes;
import ru.rockstar.client.ui.draggable.DraggableModule;

public class InvPreviewComponent extends DraggableModule {
	public int lastA = 0;
    public int lastW = 0;
    public int lastS = 0;
    public int lastD = 0;
    public int lastJ = 0;
    public int lastS2 = 0;
    public long deltaAnim;
    public static int x;
    public static int y;
    
    public InvPreviewComponent() {
        super("InvPreviewComponent", 200, 200);
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
    	if (this.mc.player == null || this.mc.world == null || !Main.instance.featureDirector.getFeatureByClass(InventoryPreview.class).isToggled()) {
            return;
        }
        if (this.mc.gameSettings.showDebugInfo) {
            return;
        }
        x = getX();
        y = getY();
        final float left = x;
        final float top = y;
        final float right = left + 145.0f;
        final float bottom = top + 45.0f;
        GlStateManager.pushMatrix();
        if (InventoryPreview.mode.getCurrentMode().equalsIgnoreCase("Rockstar New")) {
            RoundedUtil.drawHorizontalGradientOutlinedRoundedRectWithGlow(x - 3, y - 16.0f, right + 3 - x - 3 + 5, 23 + 40 * InventoryPreview.progress + 5, 10, 1.5f, 15, ClientHelper.getClientColor().darker(),  ClientHelper.getClientColor());
        
            this.mc.mntsb_16.drawCenteredStringWithShadow("Инвентарь", x + 32.0f + 40, y - 12.0f, -1);
        } else if (InventoryPreview.mode.getCurrentMode().equalsIgnoreCase("Rockstar")) {
            RoundedUtil.drawGradientHorizontal(x - 3, y - 16.0f, right + 3 - x - 3 + 5, 23 + 40 * InventoryPreview.progress + 5, 10,  ClientHelper.getClientColor().darker(),  ClientHelper.getClientColor());
        
            this.mc.mntsb_16.drawCenteredStringWithShadow("Инвентарь", x + 32.0f + 40, y - 12.0f, -1);
        } else if (InventoryPreview.mode.getCurrentMode().equalsIgnoreCase("Rockstar Styled")) {

            Color l1 = ClientHelper.getClientColor().brighter();
            
            Color l2 = ClientHelper.getClientColor();

        	
            RoundedUtil.drawRound(x - 3, y - 16.0f, right + 3 - x - 3 + 5, 23 + 40 * InventoryPreview.progress + 5, 10, new Color(40,40,40));
       
            //RenderUtils.drawBlurredShadow(x - 3 + 4, y - 16.0f + 4, 12, 12, 10, l1);
            
            RoundedUtil.drawGradientRound(x - 3 + 4, y - 16.0f + 4, 12, 12, 4, l2, l1, l1, l2);
            
            this.mc.i30.drawString("l", (float) (x + 2), y - 13.0f + 3.5f, -1);
            
            Gui.drawRect(x, y - 12 + 13, x + 1.5f + 15, y- 12+12 + 5, new Color(40,40,40).getRGB());
            
            this.mc.mntsb_20.drawString("Инвентарь", x + 16, y - 12.0f + 2.5f, -1);
        }
        net.minecraft.client.renderer.RenderHelper.enableGUIStandardItemLighting();
        for (int i = 0; i < 27; ++i) {
            final ItemStack itemStack = this.mc.player.inventory.mainInventory.get(i + 9);
            final int offsetX = (int)(x + i % 9 * 16);
            final int offsetY = (int)(y + i / 9 * 16) + 2;
            this.mc.getRenderItem().renderItemAndEffectIntoGUI(itemStack, offsetX, offsetY);
            this.mc.getRenderItem().renderItemOverlayIntoGUI(this.mc.fontRendererObj, itemStack, offsetX, offsetY, null);
        }
        this.mc.getRenderItem().zLevel = 0.0f;
        net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
        GlStateManager.popMatrix();
        super.render(mouseX, mouseY);
    }

    @Override
    public void draw() {
    	if (this.mc.player == null || this.mc.world == null || !Main.instance.featureDirector.getFeatureByClass(InventoryPreview.class).isToggled()) {
            return;
        }
        if (this.mc.gameSettings.showDebugInfo) {
            return;
        }
        x = getX();
        y = getY();
        final float left = x;
        final float top = y;
        final float right = left + 145.0f;
        final float bottom = top + 45.0f;
        GlStateManager.pushMatrix();
        DrawHelper.drawRect(x - 2, y - 16.0f, x + 147.0f, y + 51.0f, new Color(30, 30, 30, 175).getRGB());
        DrawHelper.drawGradientRect1(x - 3, y - 16.0f, right + 3, y - 3, ClientHelper.getClientColor().getRGB(), ClientHelper.getClientColor().getRGB() - 100);
        this.mc.mntsb_16.drawStringWithShadow("Inventory Preview", x + 32.0f, y - 12.0f, -1);
        GlStateManager.pushMatrix();
        net.minecraft.client.renderer.RenderHelper.enableGUIStandardItemLighting();
        for (int i = 0; i < 27; ++i) {
            final ItemStack itemStack = this.mc.player.inventory.mainInventory.get(i + 9);
            final int offsetX = (int)(x + i % 9 * 16);
            final int offsetY = (int)(y + i / 9 * 16);
            this.mc.getRenderItem().renderItemAndEffectIntoGUI(itemStack, offsetX, offsetY);
            this.mc.getRenderItem().renderItemOverlayIntoGUI(this.mc.fontRendererObj, itemStack, offsetX, offsetY, null);
        }
        GlStateManager.popMatrix();
        this.mc.getRenderItem().zLevel = 0.0f;
        net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
        GlStateManager.popMatrix();
        super.draw();
    }
}
