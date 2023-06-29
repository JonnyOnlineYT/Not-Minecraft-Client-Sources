package net.minecraft.client.renderer.entity;

import javax.annotation.Nullable;
import net.minecraft.entity.EntityAreaEffectCloud;
import net.minecraft.util.ResourceLocation;

public class RenderAreaEffectCloud extends Render<EntityAreaEffectCloud> {
   public RenderAreaEffectCloud(RenderManager manager) {
      super(manager);
   }

   @Nullable
   protected ResourceLocation getEntityTexture(EntityAreaEffectCloud entity) {
      return null;
   }
}
