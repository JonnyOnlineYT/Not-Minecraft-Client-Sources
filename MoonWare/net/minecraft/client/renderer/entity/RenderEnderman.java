package net.minecraft.client.renderer.entity;

import java.util.Random;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.model.ModelEnderman;
import net.minecraft.client.renderer.entity.layers.LayerEndermanEyes;
import net.minecraft.client.renderer.entity.layers.LayerHeldBlock;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.util.Namespaced;

public class RenderEnderman extends RenderLiving<EntityEnderman>
{
    private static final Namespaced ENDERMAN_TEXTURES = new Namespaced("textures/entity/enderman/enderman.png");
    private final Random rnd = new Random();

    public RenderEnderman(RenderManager renderManagerIn)
    {
        super(renderManagerIn, new ModelEnderman(0.0F), 0.5F);
        addLayer(new LayerEndermanEyes(this));
        addLayer(new LayerHeldBlock(this));
    }

    public ModelEnderman getMainModel()
    {
        return (ModelEnderman)super.getMainModel();
    }

    /**
     * Renders the desired {@code T} type Entity.
     */
    public void doRender(EntityEnderman entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        IBlockState iblockstate = entity.getHeldBlockState();
        ModelEnderman modelenderman = getMainModel();
        modelenderman.isCarrying = iblockstate != null;
        modelenderman.isAttacking = entity.isScreaming();

        if (entity.isScreaming())
        {
            double d0 = 0.02D;
            x += rnd.nextGaussian() * 0.02D;
            z += rnd.nextGaussian() * 0.02D;
        }

        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected Namespaced getEntityTexture(EntityEnderman entity)
    {
        return ENDERMAN_TEXTURES;
    }
}
