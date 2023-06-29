package net.minecraft.client.renderer.tileentity;

import com.google.common.collect.Maps;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.model.ModelShulker;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.tileentity.TileEntityBed;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityEnchantmentTable;
import net.minecraft.tileentity.TileEntityEndGateway;
import net.minecraft.tileentity.TileEntityEndPortal;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.tileentity.TileEntityPiston;
import net.minecraft.tileentity.TileEntityShulkerBox;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.tileentity.TileEntityStructure;
import net.minecraft.util.ReportedException;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import optifine.Reflector;

public class TileEntityRendererDispatcher
{
    public final Map<Class, TileEntitySpecialRenderer> mapSpecialRenderers = Maps.newHashMap();
    public static TileEntityRendererDispatcher instance = new TileEntityRendererDispatcher();
    public Font font;

    /** The player's current X position (same as playerX) */
    public static double staticPlayerX;

    /** The player's current Y position (same as playerY) */
    public static double staticPlayerY;

    /** The player's current Z position (same as playerZ) */
    public static double staticPlayerZ;
    public TextureManager renderEngine;
    public World worldObj;
    public Entity entity;
    public float entityYaw;
    public float entityPitch;
    public RayTraceResult cameraHitResult;
    public double entityX;
    public double entityY;
    public double entityZ;
    public TileEntity tileEntityRendered;
    private Tessellator batchBuffer = new Tessellator(2097152);
    private boolean drawingBatch;

    private TileEntityRendererDispatcher()
    {
        mapSpecialRenderers.put(TileEntitySign.class, new TileEntitySignRenderer());
        mapSpecialRenderers.put(TileEntityMobSpawner.class, new TileEntityMobSpawnerRenderer());
        mapSpecialRenderers.put(TileEntityPiston.class, new TileEntityPistonRenderer());
        mapSpecialRenderers.put(TileEntityChest.class, new TileEntityChestRenderer());
        mapSpecialRenderers.put(TileEntityEnderChest.class, new TileEntityEnderChestRenderer());
        mapSpecialRenderers.put(TileEntityEnchantmentTable.class, new TileEntityEnchantmentTableRenderer());
        mapSpecialRenderers.put(TileEntityEndPortal.class, new TileEntityEndPortalRenderer());
        mapSpecialRenderers.put(TileEntityEndGateway.class, new TileEntityEndGatewayRenderer());
        mapSpecialRenderers.put(TileEntityBeacon.class, new TileEntityBeaconRenderer());
        mapSpecialRenderers.put(TileEntitySkull.class, new TileEntitySkullRenderer());
        mapSpecialRenderers.put(TileEntityBanner.class, new TileEntityBannerRenderer());
        mapSpecialRenderers.put(TileEntityStructure.class, new TileEntityStructureRenderer());
        mapSpecialRenderers.put(TileEntityShulkerBox.class, new TileEntityShulkerBoxRenderer(new ModelShulker()));
        mapSpecialRenderers.put(TileEntityBed.class, new TileEntityBedRenderer());

        for (TileEntitySpecialRenderer<?> tileentityspecialrenderer : mapSpecialRenderers.values())
        {
            tileentityspecialrenderer.setRendererDispatcher(this);
        }
    }

    public <T extends TileEntity> TileEntitySpecialRenderer<T> getSpecialRendererByClass(Class <? extends TileEntity > teClass)
    {
        TileEntitySpecialRenderer<T> tileentityspecialrenderer = (TileEntitySpecialRenderer) mapSpecialRenderers.get(teClass);

        if (tileentityspecialrenderer == null && teClass != TileEntity.class)
        {
            tileentityspecialrenderer = getSpecialRendererByClass((Class<? extends TileEntity>) teClass.getSuperclass());
            mapSpecialRenderers.put(teClass, tileentityspecialrenderer);
        }

        return tileentityspecialrenderer;
    }

    @Nullable
    public <T extends TileEntity> TileEntitySpecialRenderer<T> getSpecialRenderer(@Nullable TileEntity tileEntityIn)
    {
        return tileEntityIn == null ? null : getSpecialRendererByClass(tileEntityIn.getClass());
    }

    public void prepare(World p_190056_1_, TextureManager p_190056_2_, Font p_190056_3_, Entity p_190056_4_, RayTraceResult p_190056_5_, float p_190056_6_)
    {
        if (worldObj != p_190056_1_)
        {
            setWorld(p_190056_1_);
        }

        renderEngine = p_190056_2_;
        entity = p_190056_4_;
        font = p_190056_3_;
        cameraHitResult = p_190056_5_;
        entityYaw = p_190056_4_.prevRotationYaw + (p_190056_4_.rotationYaw - p_190056_4_.prevRotationYaw) * p_190056_6_;
        entityPitch = p_190056_4_.prevRotationPitch + (p_190056_4_.rotationPitch - p_190056_4_.prevRotationPitch) * p_190056_6_;
        entityX = p_190056_4_.lastTickPosX + (p_190056_4_.posX - p_190056_4_.lastTickPosX) * (double)p_190056_6_;
        entityY = p_190056_4_.lastTickPosY + (p_190056_4_.posY - p_190056_4_.lastTickPosY) * (double)p_190056_6_;
        entityZ = p_190056_4_.lastTickPosZ + (p_190056_4_.posZ - p_190056_4_.lastTickPosZ) * (double)p_190056_6_;
    }

    public void renderTileEntity(TileEntity tileentityIn, float partialTicks, int destroyStage)
    {
        if (tileentityIn.getDistanceSq(entityX, entityY, entityZ) < tileentityIn.getMaxRenderDistanceSquared())
        {
            RenderHelper.enableStandardItemLighting();
            boolean flag = true;

            if (Reflector.ForgeTileEntity_hasFastRenderer.exists())
            {
                flag = !drawingBatch || !Reflector.callBoolean(tileentityIn, Reflector.ForgeTileEntity_hasFastRenderer);
            }

            if (flag)
            {
                int i = worldObj.getCombinedLight(tileentityIn.getPos(), 0);
                int j = i % 65536;
                int k = i / 65536;
                OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)j, (float)k);
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            }

            BlockPos blockpos = tileentityIn.getPos();
            func_192854_a(tileentityIn, (double)blockpos.getX() - staticPlayerX, (double)blockpos.getY() - staticPlayerY, (double)blockpos.getZ() - staticPlayerZ, partialTicks, destroyStage, 1.0F);
        }
    }

    /**
     * Render this TileEntity at a given set of coordinates
     */
    public void renderTileEntityAt(TileEntity tileEntityIn, double x, double y, double z, float partialTicks)
    {
        func_192855_a(tileEntityIn, x, y, z, partialTicks, 1.0F);
    }

    public void func_192855_a(TileEntity p_192855_1_, double p_192855_2_, double p_192855_4_, double p_192855_6_, float p_192855_8_, float p_192855_9_)
    {
        func_192854_a(p_192855_1_, p_192855_2_, p_192855_4_, p_192855_6_, p_192855_8_, -1, p_192855_9_);
    }

    public void func_192854_a(TileEntity p_192854_1_, double p_192854_2_, double p_192854_4_, double p_192854_6_, float p_192854_8_, int p_192854_9_, float p_192854_10_)
    {
        TileEntitySpecialRenderer<TileEntity> tileentityspecialrenderer = getSpecialRenderer(p_192854_1_);

        if (tileentityspecialrenderer != null)
        {
            try
            {
                tileEntityRendered = p_192854_1_;

                if (drawingBatch && Reflector.callBoolean(p_192854_1_, Reflector.ForgeTileEntity_hasFastRenderer))
                {
                    tileentityspecialrenderer.renderTileEntityFast(p_192854_1_, p_192854_2_, p_192854_4_, p_192854_6_, p_192854_8_, p_192854_9_, p_192854_10_, batchBuffer.getBuffer());
                }
                else
                {
                    tileentityspecialrenderer.func_192841_a(p_192854_1_, p_192854_2_, p_192854_4_, p_192854_6_, p_192854_8_, p_192854_9_, p_192854_10_);
                }

                tileEntityRendered = null;
            }
            catch (Throwable throwable)
            {
                CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Rendering Block Entity");
                CrashReportCategory crashreportcategory = crashreport.makeCategory("Block Entity Details");
                p_192854_1_.addInfoToCrashReport(crashreportcategory);
                throw new ReportedException(crashreport);
            }
        }
    }

    public void setWorld(@Nullable World worldIn)
    {
        worldObj = worldIn;

        if (worldIn == null)
        {
            entity = null;
        }
    }

    public Font getFontRenderer()
    {
        return font;
    }

    public void preDrawBatch()
    {
        batchBuffer.getBuffer().begin(7, DefaultVertexFormats.BLOCK);
        drawingBatch = true;
    }

    public void drawBatch(int p_drawBatch_1_)
    {
        renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.blendFunc(770, 771);
        GlStateManager.enableBlend();
        GlStateManager.disableCull();

        if (Minecraft.isAmbientOcclusionEnabled())
        {
            GlStateManager.shadeModel(7425);
        }
        else
        {
            GlStateManager.shadeModel(7424);
        }

        if (p_drawBatch_1_ > 0)
        {
            batchBuffer.getBuffer().sortVertexData(0.0F, 0.0F, 0.0F);
        }

        batchBuffer.draw();
        RenderHelper.enableStandardItemLighting();
        drawingBatch = false;
    }
}
