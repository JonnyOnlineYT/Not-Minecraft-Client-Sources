/*
 * Decompiled with CFR 0.150.
 */
package ru.fluger.client.ui.drag.imp;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import ru.fluger.client.Fluger;
import ru.fluger.client.feature.impl.hud.HUD;
import ru.fluger.client.ui.drag.Drag;

public class ArmorHud
extends Drag {
    public ArmorHud() {
        this.setWidth(100.0f);
        this.setHeight(30.0f);
        this.name = "ArmorHud";
    }

    @Override
    public void render(int mouseX, int mouseY) {
        if (!HUD.armor.getCurrentValue()) {
            return;
        }
        GlStateManager.pushMatrix();
        GlStateManager.enableTexture2D();
        int count = 0;
        int i = (int)this.x;
        int y = (int)this.y;
        for (ItemStack is : this.mc.player.inventory.armorInventory) {
            int x = i - 90 + (9 - count) * 20 + 2;
            GlStateManager.enableDepth();
            this.mc.getRenderItem().zLevel = 200.0f;
            this.mc.getRenderItem().renderItemAndEffectIntoGUI(is, x, y);
            this.mc.getRenderItem().renderItemOverlayIntoGUI(this.mc.fontRendererObj, is, x, y, "");
            this.mc.getRenderItem().zLevel = 0.0f;
            GlStateManager.enableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            ++count;
        }
        GlStateManager.enableDepth();
        GlStateManager.disableLighting();
        GlStateManager.popMatrix();
    }

    @Override
    public void init() {
        ScaledResolution rs = new ScaledResolution(this.mc);
        int width = Fluger.scale.calc(rs.getScaledWidth());
        int height = Fluger.scale.calc(rs.getScaledHeight());
        this.x = width / 2 - 22;
        this.y = height - 58;
        super.init();
    }
}

