/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.renderer.entity.layers;

import java.util.Random;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.boss.EntityDragon;

public class LayerEnderDragonDeath
implements LayerRenderer<EntityDragon> {
    @Override
    public void doRenderLayer(EntityDragon entityDragon, float f, float f2, float f3, float f4, float f5, float f6, float f7) {
        if (entityDragon.deathTicks > 0) {
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldRenderer = tessellator.getWorldRenderer();
            RenderHelper.disableStandardItemLighting();
            float f8 = ((float)entityDragon.deathTicks + f3) / 200.0f;
            float f9 = 0.0f;
            if (f8 > 0.8f) {
                f9 = (f8 - 0.8f) / 0.2f;
            }
            Random random = new Random(432L);
            GlStateManager.disableTexture2D();
            GlStateManager.shadeModel(7425);
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(770, 1);
            GlStateManager.disableAlpha();
            GlStateManager.enableCull();
            GlStateManager.depthMask(false);
            GlStateManager.pushMatrix();
            GlStateManager.translate(0.0f, -1.0f, -2.0f);
            int n = 0;
            while ((float)n < (f8 + f8 * f8) / 2.0f * 60.0f) {
                GlStateManager.rotate(random.nextFloat() * 360.0f, 1.0f, 0.0f, 0.0f);
                GlStateManager.rotate(random.nextFloat() * 360.0f, 0.0f, 1.0f, 0.0f);
                GlStateManager.rotate(random.nextFloat() * 360.0f, 0.0f, 0.0f, 1.0f);
                GlStateManager.rotate(random.nextFloat() * 360.0f, 1.0f, 0.0f, 0.0f);
                GlStateManager.rotate(random.nextFloat() * 360.0f, 0.0f, 1.0f, 0.0f);
                GlStateManager.rotate(random.nextFloat() * 360.0f + f8 * 90.0f, 0.0f, 0.0f, 1.0f);
                float f10 = random.nextFloat() * 20.0f + 5.0f + f9 * 10.0f;
                float f11 = random.nextFloat() * 2.0f + 1.0f + f9 * 2.0f;
                worldRenderer.begin(6, DefaultVertexFormats.POSITION_COLOR);
                worldRenderer.pos(0.0, 0.0, 0.0).color(255, 255, 255, (int)(255.0f * (1.0f - f9))).endVertex();
                worldRenderer.pos(-0.866 * (double)f11, f10, -0.5f * f11).color(255, 0, 255, 0).endVertex();
                worldRenderer.pos(0.866 * (double)f11, f10, -0.5f * f11).color(255, 0, 255, 0).endVertex();
                worldRenderer.pos(0.0, f10, 1.0f * f11).color(255, 0, 255, 0).endVertex();
                worldRenderer.pos(-0.866 * (double)f11, f10, -0.5f * f11).color(255, 0, 255, 0).endVertex();
                tessellator.draw();
                ++n;
            }
            GlStateManager.popMatrix();
            GlStateManager.depthMask(true);
            GlStateManager.disableCull();
            GlStateManager.disableBlend();
            GlStateManager.shadeModel(7424);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.enableTexture2D();
            GlStateManager.enableAlpha();
            RenderHelper.enableStandardItemLighting();
        }
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}

