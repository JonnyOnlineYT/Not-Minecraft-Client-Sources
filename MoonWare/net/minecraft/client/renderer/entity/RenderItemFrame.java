package net.minecraft.client.renderer.entity;

import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ModelManager;
import net.minecraft.client.renderer.block.model.ModelNamespaced;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.init.Items;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Namespaced;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.storage.MapData;
import optifine.Config;
import optifine.Reflector;
import optifine.ReflectorForge;

public class RenderItemFrame extends Render<EntityItemFrame>
{
    private static final Namespaced MAP_BACKGROUND_TEXTURES = new Namespaced("textures/map/map_background.png");
    private final Minecraft mc = Minecraft.getMinecraft();
    private final ModelNamespaced itemFrameModel = new ModelNamespaced("item_frame", "normal");
    private final ModelNamespaced mapModel = new ModelNamespaced("item_frame", "map");
    private final RenderItem itemRenderer;

    public RenderItemFrame(RenderManager renderManagerIn, RenderItem itemRendererIn)
    {
        super(renderManagerIn);
        itemRenderer = itemRendererIn;
    }

    /**
     * Renders the desired {@code T} type Entity.
     */
    public void doRender(EntityItemFrame entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        GlStateManager.pushMatrix();
        BlockPos blockpos = entity.getHangingPosition();
        double d0 = (double)blockpos.getX() - entity.posX + x;
        double d1 = (double)blockpos.getY() - entity.posY + y;
        double d2 = (double)blockpos.getZ() - entity.posZ + z;
        GlStateManager.translate(d0 + 0.5D, d1 + 0.5D, d2 + 0.5D);
        GlStateManager.rotate(180.0F - entity.rotationYaw, 0.0F, 1.0F, 0.0F);
        renderManager.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        BlockRendererDispatcher blockrendererdispatcher = Minecraft.getBlockRenderDispatcher();
        ModelManager modelmanager = blockrendererdispatcher.getBlockModelShapes().getModelManager();
        IBakedModel ibakedmodel;

        if (entity.getDisplayedItem().getItem() instanceof ItemMap)
        {
            ibakedmodel = modelmanager.getModel(mapModel);
        }
        else
        {
            ibakedmodel = modelmanager.getModel(itemFrameModel);
        }

        GlStateManager.pushMatrix();
        GlStateManager.translate(-0.5F, -0.5F, -0.5F);

        if (renderOutlines)
        {
            GlStateManager.enableColorMaterial();
            GlStateManager.enableOutlineMode(getTeamColor(entity));
        }

        blockrendererdispatcher.getBlockModelRenderer().renderModelBrightnessColor(ibakedmodel, 1.0F, 1.0F, 1.0F, 1.0F);

        if (renderOutlines)
        {
            GlStateManager.disableOutlineMode();
            GlStateManager.disableColorMaterial();
        }

        GlStateManager.popMatrix();
        GlStateManager.translate(0.0F, 0.0F, 0.4375F);
        renderItem(entity);
        GlStateManager.popMatrix();
        renderName(entity, x + (double)((float)entity.facingDirection.getFrontOffsetX() * 0.3F), y - 0.25D, z + (double)((float)entity.facingDirection.getFrontOffsetZ() * 0.3F));
    }

    @Nullable

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected Namespaced getEntityTexture(EntityItemFrame entity)
    {
        return null;
    }

    private void renderItem(EntityItemFrame itemFrame)
    {
        ItemStack itemstack = itemFrame.getDisplayedItem();

        if (!itemstack.isEmpty())
        {
            if (!Config.zoomMode)
            {
                Entity entity = Minecraft.player;
                double d0 = itemFrame.getDistanceSq(entity.posX, entity.posY, entity.posZ);

                if (d0 > 4096.0D)
                {
                    return;
                }
            }

            GlStateManager.pushMatrix();
            GlStateManager.disableLighting();
            boolean flag = itemstack.getItem() instanceof ItemMap;
            int i = flag ? itemFrame.getRotation() % 4 * 2 : itemFrame.getRotation();
            GlStateManager.rotate((float)i * 360.0F / 8.0F, 0.0F, 0.0F, 1.0F);

            if (!Reflector.postForgeBusEvent(Reflector.RenderItemInFrameEvent_Constructor, itemFrame, this))
            {
                if (flag)
                {
                    renderManager.renderEngine.bindTexture(MAP_BACKGROUND_TEXTURES);
                    GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
                    float f = 0.0078125F;
                    GlStateManager.scale(0.0078125F, 0.0078125F, 0.0078125F);
                    GlStateManager.translate(-64.0F, -64.0F, 0.0F);
                    MapData mapdata = ReflectorForge.getMapData(Items.FILLED_MAP, itemstack, itemFrame.world);
                    GlStateManager.translate(0.0F, 0.0F, -1.0F);

                    if (mapdata != null)
                    {
                        Minecraft.gameRenderer.getMapItemRenderer().renderMap(mapdata, true);
                    }
                }
                else
                {
                    GlStateManager.scale(0.5F, 0.5F, 0.5F);
                    GlStateManager.pushAttrib();
                    RenderHelper.enableStandardItemLighting();
                    itemRenderer.renderItem(itemstack, ItemCameraTransforms.TransformType.FIXED);
                    RenderHelper.disableStandardItemLighting();
                    GlStateManager.popAttrib();
                }
            }

            GlStateManager.enableLighting();
            GlStateManager.popMatrix();
        }
    }

    protected void renderName(EntityItemFrame entity, double x, double y, double z)
    {
        if (Minecraft.isGuiEnabled() && !entity.getDisplayedItem().isEmpty() && entity.getDisplayedItem().hasDisplayName() && renderManager.pointedEntity == entity)
        {
            double d0 = entity.getDistanceSqToEntity(renderManager.renderViewEntity);
            float f = entity.isSneaking() ? 32.0F : 64.0F;

            if (d0 < (double)(f * f))
            {
                String s = entity.getDisplayedItem().getDisplayName();
                renderLivingLabel(entity, s, x, y, z, 64);
            }
        }
    }
}
