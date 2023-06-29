package net.minecraft.client.renderer.entity;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.util.ResourceLocation;

public class RenderHusk extends RenderZombie {
   private static final ResourceLocation HUSK_ZOMBIE_TEXTURES = new ResourceLocation("textures/entity/zombie/husk.png");

   public RenderHusk(RenderManager p_i47204_1_) {
      super(p_i47204_1_);
   }

   protected void preRenderCallback(EntityZombie entitylivingbaseIn, float partialTickTime) {
      float f = 1.0625F;
      GlStateManager.scale(1.0625F, 1.0625F, 1.0625F);
      super.preRenderCallback(entitylivingbaseIn, partialTickTime);
   }

   protected ResourceLocation getEntityTexture(EntityZombie entity) {
      return HUSK_ZOMBIE_TEXTURES;
   }
}
