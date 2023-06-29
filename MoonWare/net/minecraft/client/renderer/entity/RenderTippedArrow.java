package net.minecraft.client.renderer.entity;

import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.util.Namespaced;

public class RenderTippedArrow extends RenderArrow<EntityTippedArrow>
{
    public static final Namespaced RES_ARROW = new Namespaced("textures/entity/projectiles/arrow.png");
    public static final Namespaced RES_TIPPED_ARROW = new Namespaced("textures/entity/projectiles/tipped_arrow.png");

    public RenderTippedArrow(RenderManager manager)
    {
        super(manager);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected Namespaced getEntityTexture(EntityTippedArrow entity)
    {
        return entity.getColor() > 0 ? RES_TIPPED_ARROW : RES_ARROW;
    }
}
