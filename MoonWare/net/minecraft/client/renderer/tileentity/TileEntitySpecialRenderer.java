package net.minecraft.client.renderer.tileentity;

import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Namespaced;
import net.minecraft.util.text.Component;
import net.minecraft.world.World;
import net.optifine.entity.model.IEntityRenderer;

public abstract class TileEntitySpecialRenderer<T extends TileEntity> implements IEntityRenderer
{
    protected static final Namespaced[] DESTROY_STAGES = {new Namespaced("textures/blocks/destroy_stage_0.png"), new Namespaced("textures/blocks/destroy_stage_1.png"), new Namespaced("textures/blocks/destroy_stage_2.png"), new Namespaced("textures/blocks/destroy_stage_3.png"), new Namespaced("textures/blocks/destroy_stage_4.png"), new Namespaced("textures/blocks/destroy_stage_5.png"), new Namespaced("textures/blocks/destroy_stage_6.png"), new Namespaced("textures/blocks/destroy_stage_7.png"), new Namespaced("textures/blocks/destroy_stage_8.png"), new Namespaced("textures/blocks/destroy_stage_9.png")};
    protected TileEntityRendererDispatcher rendererDispatcher;
    private Class tileEntityClass;
    private Namespaced locationTextureCustom;

    public void func_192841_a(T p_192841_1_, double p_192841_2_, double p_192841_4_, double p_192841_6_, float p_192841_8_, int p_192841_9_, float p_192841_10_)
    {
        Component itextcomponent = p_192841_1_.getDisplayName();

        if (itextcomponent != null && rendererDispatcher.cameraHitResult != null && p_192841_1_.getPos().equals(rendererDispatcher.cameraHitResult.getBlockPos()))
        {
            setLightmapDisabled(true);
            drawNameplate(p_192841_1_, itextcomponent.asFormattedString(), p_192841_2_, p_192841_4_, p_192841_6_, 12);
            setLightmapDisabled(false);
        }
    }

    /**
     * Sets whether to use the light map when rendering. Disabling this allows rendering ignoring lighting, which can be
     * useful for floating text, e.g.
     */
    protected void setLightmapDisabled(boolean disabled)
    {
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);

        if (disabled)
        {
            GlStateManager.disableTexture2D();
        }
        else
        {
            GlStateManager.enableTexture2D();
        }

        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }

    protected void bindTexture(Namespaced location)
    {
        TextureManager texturemanager = rendererDispatcher.renderEngine;

        if (texturemanager != null)
        {
            texturemanager.bindTexture(location);
        }
    }

    protected World getWorld()
    {
        return rendererDispatcher.worldObj;
    }

    public void setRendererDispatcher(TileEntityRendererDispatcher rendererDispatcherIn)
    {
        rendererDispatcher = rendererDispatcherIn;
    }

    public Font getFontRenderer()
    {
        return rendererDispatcher.getFontRenderer();
    }

    public boolean isGlobalRenderer(T te)
    {
        return false;
    }

    protected void drawNameplate(T te, String str, double x, double y, double z, int maxDistance)
    {
        Entity entity = rendererDispatcher.entity;
        double d0 = te.getDistanceSq(entity.posX, entity.posY, entity.posZ);

        if (d0 <= (double)(maxDistance * maxDistance))
        {
            float f = rendererDispatcher.entityYaw;
            float f1 = rendererDispatcher.entityPitch;
            boolean flag = false;
            GameRenderer.drawNameplate(getFontRenderer(), str, (float)x + 0.5F, (float)y + 1.5F, (float)z + 0.5F, 0, f, f1, false, false);
        }
    }

    public void renderTileEntityFast(T p_renderTileEntityFast_1_, double p_renderTileEntityFast_2_, double p_renderTileEntityFast_4_, double p_renderTileEntityFast_6_, float p_renderTileEntityFast_8_, int p_renderTileEntityFast_9_, float p_renderTileEntityFast_10_, BufferBuilder p_renderTileEntityFast_11_)
    {
    }

    public Class getEntityClass()
    {
        return tileEntityClass;
    }

    public void setEntityClass(Class p_setEntityClass_1_)
    {
        tileEntityClass = p_setEntityClass_1_;
    }

    public Namespaced getLocationTextureCustom()
    {
        return locationTextureCustom;
    }

    public void setLocationTextureCustom(Namespaced p_setLocationTextureCustom_1_)
    {
        locationTextureCustom = p_setLocationTextureCustom_1_;
    }
}
