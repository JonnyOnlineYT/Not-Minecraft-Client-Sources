package org.moonware.client.ui.components.draggable.impl;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import org.moonware.client.feature.impl.hud.HUD;
import org.moonware.client.ui.components.draggable.HudComponent;

public class ArmorComponent extends HudComponent {
    public ArmorComponent() {
        super(100, 350, 98, 16);
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTick) {
        setVisible(HUD.armor.get());
        super.draw(mouseX, mouseY, partialTick);
        GlStateManager.pushMatrix();
        GlStateManager.enableTexture2D();
        GlStateManager.enableDepth();
        GlStateManager.disableLighting();
        for (int i = 0; i < Minecraft.player.inventory.armorInventory.size(); i++) {
            ItemStack is = Minecraft.player.inventory.armorInventory.get(i);
            if (is.isEmpty()) continue;
            int cx = x - 90 + (8 - i) * 20 + 2;
            Minecraft.getRenderItem().zLevel += 200F;
            Minecraft.getRenderItem().renderItemAndEffectIntoGUI(is, cx, y);
            Minecraft.getRenderItem().renderItemOverlayIntoGUI(Minecraft.font, is, cx, y, "");
            Minecraft.getRenderItem().zLevel -= 200F;
        }
        GlStateManager.disableLighting();
        GlStateManager.popMatrix();
    }
}
