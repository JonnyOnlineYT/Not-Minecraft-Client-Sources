package ru.rockstar.client.features.impl.display;

import java.awt.Color;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import ru.rockstar.Main;
import ru.rockstar.api.event.EventTarget;
import ru.rockstar.api.event.event.Event2D;
import ru.rockstar.api.event.event.EventUpdate;
import ru.rockstar.api.utils.render.ClientHelper;
import ru.rockstar.api.utils.render.DrawHelper;
import ru.rockstar.api.utils.render.RenderUtils;
import ru.rockstar.api.utils.render.RoundedUtil;
import ru.rockstar.client.features.Category;
import ru.rockstar.client.features.Feature;
import ru.rockstar.client.features.impl.movement.DamageFly;
import ru.rockstar.client.ui.draggable.impl.DmgflyComponent;
import ru.rockstar.client.ui.draggable.impl.InvPreviewComponent;
import ru.rockstar.client.ui.settings.impl.ListSetting;
import ru.rockstar.client.ui.settings.impl.NumberSetting;

public class InventoryPreview extends Feature {
	public static NumberSetting x;
    public static NumberSetting y;
    int x2,y2;
    public static ListSetting mode = new ListSetting("Mode", "Rockstar Styled", () -> true, "Rockstar", "Rockstar New", "Rockstar Styled");
    public static float progress;
    private long lastMS;
    public static float indicatorTimer;
    public InventoryPreview(){
        super("InventoryPreview","Показывает превью инвентаря", 0, Category.DISPLAY);
        x = new NumberSetting("Indicators X", 0, 0, 1500, 1, () -> true);
        y = new NumberSetting("Indicators Y", 0, 0, 1500, 1, () -> true);
        addSettings(mode);
    }
    
    @EventTarget
    public void ebatkopat(Event2D render) {
    	if (this.progress >= 1.0f) {
            this.progress = 1.0f;
        }
        else {
            this.progress = (System.currentTimeMillis() - this.lastMS) / 850.0f;
        }
    	if (this.mc.player == null || this.mc.world == null || !Main.instance.featureDirector.getFeatureByClass(InventoryPreview.class).isToggled()) {
            return;
        }
        if (this.mc.gameSettings.showDebugInfo) {
            return;
        }
        final float x = InvPreviewComponent.x;
        final float y = InvPreviewComponent.y;
        final float left = x;
        final float top = y;
        final float right = left + 145.0f;
        final float bottom = top + 45.0f;
        GlStateManager.pushMatrix();
        if (mode.getCurrentMode().equalsIgnoreCase("Rockstar New")) {
            RoundedUtil.drawHorizontalGradientOutlinedRoundedRectWithGlow(x - 3, y - 16.0f, right + 3 - x - 3 + 5, 23 + 40 * progress + 5, 10, 1.5f, 15, ClientHelper.getClientColor().darker(),  ClientHelper.getClientColor());
        
            this.mc.mntsb_16.drawCenteredStringWithShadow("Инвентарь", x + 32.0f + 40, y - 12.0f, -1);
        } else if (mode.getCurrentMode().equalsIgnoreCase("Rockstar")) {
            RoundedUtil.drawGradientHorizontal(x - 3, y - 16.0f, right + 3 - x - 3 + 5, 23 + 40 * progress + 5, 10,  ClientHelper.getClientColor().darker(),  ClientHelper.getClientColor());
        
            this.mc.mntsb_16.drawCenteredStringWithShadow("Инвентарь", x + 32.0f + 40, y - 12.0f, -1);
        } else if (mode.getCurrentMode().equalsIgnoreCase("Rockstar Styled")) {

            Color l1 = ClientHelper.getClientColor().brighter();
            
            Color l2 = ClientHelper.getClientColor();

        	
            RoundedUtil.drawRound(x - 3, y - 16.0f, right + 3 - x - 3 + 5, 23 + 40 * progress + 5, 10, new Color(40,40,40));
       
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
    }
    @Override
    public void onEnable() {
    	this.lastMS = System.currentTimeMillis();
    	this.progress = 0.0f;
        super.onEnable();
    }
}