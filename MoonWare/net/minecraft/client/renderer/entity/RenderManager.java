package net.minecraft.client.renderer.entity;

import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAreaEffectCloud;
import net.minecraft.entity.EntityLeashKnot;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.*;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.projectile.*;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ReportedException;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.optifine.entity.model.CustomEntityModels;
import optifine.PlayerItemsLayer;
import optifine.Reflector;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Map;

public class RenderManager
{
    private final Map<Class, Render> entityRenderMap = Maps.newHashMap();
    private final Map<String, RenderPlayer> skinMap = Maps.newHashMap();
    private final RenderPlayer playerRenderer;

    /** Renders fonts */
    private Font textRenderer;
    public static double renderPosX;
    public static double renderPosY;
    public static double renderPosZ;
    public TextureManager renderEngine;

    /** Reference to the World object. */
    public World worldObj;

    /** RenderManager's field for the renderViewEntity */
    public Entity renderViewEntity;
    public Entity pointedEntity;
    public float playerViewY;
    public float playerViewX;

    /** Reference to the GameSettings object. */
    public GameSettings options;
    public double viewerPosX;
    public double viewerPosY;
    public double viewerPosZ;
    private boolean renderOutlines;
    private boolean renderShadow = true;

    /** whether bounding box should be rendered or not */
    private boolean debugBoundingBox;
    public Entity renderEntity;
    public Render renderRender;

    public RenderManager(TextureManager renderEngineIn, RenderItem itemRendererIn)
    {
        renderEngine = renderEngineIn;
        entityRenderMap.put(EntityCaveSpider.class, new RenderCaveSpider(this));
        entityRenderMap.put(EntitySpider.class, new RenderSpider(this));
        entityRenderMap.put(EntityPig.class, new RenderPig(this));
        entityRenderMap.put(EntitySheep.class, new RenderSheep(this));
        entityRenderMap.put(EntityCow.class, new RenderCow(this));
        entityRenderMap.put(EntityMooshroom.class, new RenderMooshroom(this));
        entityRenderMap.put(EntityWolf.class, new RenderWolf(this));
        entityRenderMap.put(EntityChicken.class, new RenderChicken(this));
        entityRenderMap.put(EntityOcelot.class, new RenderOcelot(this));
        entityRenderMap.put(EntityRabbit.class, new RenderRabbit(this));
        entityRenderMap.put(EntityParrot.class, new RenderParrot(this));
        entityRenderMap.put(EntitySilverfish.class, new RenderSilverfish(this));
        entityRenderMap.put(EntityEndermite.class, new RenderEndermite(this));
        entityRenderMap.put(EntityCreeper.class, new RenderCreeper(this));
        entityRenderMap.put(EntityEnderman.class, new RenderEnderman(this));
        entityRenderMap.put(EntitySnowman.class, new RenderSnowMan(this));
        entityRenderMap.put(EntitySkeleton.class, new RenderSkeleton(this));
        entityRenderMap.put(EntityWitherSkeleton.class, new RenderWitherSkeleton(this));
        entityRenderMap.put(EntityStray.class, new RenderStray(this));
        entityRenderMap.put(EntityWitch.class, new RenderWitch(this));
        entityRenderMap.put(EntityBlaze.class, new RenderBlaze(this));
        entityRenderMap.put(EntityPigZombie.class, new RenderPigZombie(this));
        entityRenderMap.put(EntityZombie.class, new RenderZombie(this));
        entityRenderMap.put(EntityZombieVillager.class, new RenderZombieVillager(this));
        entityRenderMap.put(EntityHusk.class, new RenderHusk(this));
        entityRenderMap.put(EntitySlime.class, new RenderSlime(this));
        entityRenderMap.put(EntityMagmaCube.class, new RenderMagmaCube(this));
        entityRenderMap.put(EntityGiantZombie.class, new RenderGiantZombie(this, 6.0F));
        entityRenderMap.put(EntityGhast.class, new RenderGhast(this));
        entityRenderMap.put(EntitySquid.class, new RenderSquid(this));
        entityRenderMap.put(EntityVillager.class, new RenderVillager(this));
        entityRenderMap.put(EntityIronGolem.class, new RenderIronGolem(this));
        entityRenderMap.put(EntityBat.class, new RenderBat(this));
        entityRenderMap.put(EntityGuardian.class, new RenderGuardian(this));
        entityRenderMap.put(EntityElderGuardian.class, new RenderElderGuardian(this));
        entityRenderMap.put(EntityShulker.class, new RenderShulker(this));
        entityRenderMap.put(EntityPolarBear.class, new RenderPolarBear(this));
        entityRenderMap.put(EntityEvoker.class, new RenderEvoker(this));
        entityRenderMap.put(EntityVindicator.class, new RenderVindicator(this));
        entityRenderMap.put(EntityVex.class, new RenderVex(this));
        entityRenderMap.put(EntityIllusionIllager.class, new RenderIllusionIllager(this));
        entityRenderMap.put(EntityDragon.class, new RenderDragon(this));
        entityRenderMap.put(EntityEnderCrystal.class, new RenderEnderCrystal(this));
        entityRenderMap.put(EntityWither.class, new RenderWither(this));
        entityRenderMap.put(Entity.class, new RenderEntity(this));
        entityRenderMap.put(EntityPainting.class, new RenderPainting(this));
        entityRenderMap.put(EntityItemFrame.class, new RenderItemFrame(this, itemRendererIn));
        entityRenderMap.put(EntityLeashKnot.class, new RenderLeashKnot(this));
        entityRenderMap.put(EntityTippedArrow.class, new RenderTippedArrow(this));
        entityRenderMap.put(EntitySpectralArrow.class, new RenderSpectralArrow(this));
        entityRenderMap.put(EntitySnowball.class, new RenderSnowball(this, Items.SNOWBALL, itemRendererIn));
        entityRenderMap.put(EntityEnderPearl.class, new RenderSnowball(this, Items.ENDER_PEARL, itemRendererIn));
        entityRenderMap.put(EntityEnderEye.class, new RenderSnowball(this, Items.ENDER_EYE, itemRendererIn));
        entityRenderMap.put(EntityEgg.class, new RenderSnowball(this, Items.EGG, itemRendererIn));
        entityRenderMap.put(EntityPotion.class, new RenderPotion(this, itemRendererIn));
        entityRenderMap.put(EntityExpBottle.class, new RenderSnowball(this, Items.EXPERIENCE_BOTTLE, itemRendererIn));
        entityRenderMap.put(EntityFireworkRocket.class, new RenderSnowball(this, Items.FIREWORKS, itemRendererIn));
        entityRenderMap.put(EntityLargeFireball.class, new RenderFireball(this, 2.0F));
        entityRenderMap.put(EntitySmallFireball.class, new RenderFireball(this, 0.5F));
        entityRenderMap.put(EntityDragonFireball.class, new RenderDragonFireball(this));
        entityRenderMap.put(EntityWitherSkull.class, new RenderWitherSkull(this));
        entityRenderMap.put(EntityShulkerBullet.class, new RenderShulkerBullet(this));
        entityRenderMap.put(EntityItem.class, new RenderEntityItem(this, itemRendererIn));
        entityRenderMap.put(EntityXPOrb.class, new RenderXPOrb(this));
        entityRenderMap.put(EntityTNTPrimed.class, new RenderTNTPrimed(this));
        entityRenderMap.put(EntityFallingBlock.class, new RenderFallingBlock(this));
        entityRenderMap.put(EntityArmorStand.class, new RenderArmorStand(this));
        entityRenderMap.put(EntityEvokerFangs.class, new RenderEvokerFangs(this));
        entityRenderMap.put(EntityMinecartTNT.class, new RenderTntMinecart(this));
        entityRenderMap.put(EntityMinecartMobSpawner.class, new RenderMinecartMobSpawner(this));
        entityRenderMap.put(EntityMinecart.class, new RenderMinecart(this));
        entityRenderMap.put(EntityBoat.class, new RenderBoat(this));
        entityRenderMap.put(EntityFishHook.class, new RenderFish(this));
        entityRenderMap.put(EntityAreaEffectCloud.class, new RenderAreaEffectCloud(this));
        entityRenderMap.put(EntityHorse.class, new RenderHorse(this));
        entityRenderMap.put(EntitySkeletonHorse.class, new RenderAbstractHorse(this));
        entityRenderMap.put(EntityZombieHorse.class, new RenderAbstractHorse(this));
        entityRenderMap.put(EntityMule.class, new RenderAbstractHorse(this, 0.92F));
        entityRenderMap.put(EntityDonkey.class, new RenderAbstractHorse(this, 0.87F));
        entityRenderMap.put(EntityLlama.class, new RenderLlama(this));
        entityRenderMap.put(EntityLlamaSpit.class, new RenderLlamaSpit(this));
        entityRenderMap.put(EntityLightningBolt.class, new RenderLightningBolt(this));
        playerRenderer = new RenderPlayer(this);
        skinMap.put("default", playerRenderer);
        skinMap.put("slim", new RenderPlayer(this, true));
        PlayerItemsLayer.register(skinMap);

        if (Reflector.RenderingRegistry_loadEntityRenderers.exists())
        {
            Reflector.call(Reflector.RenderingRegistry_loadEntityRenderers, this, entityRenderMap);
        }
    }

    public void setRenderPosition(double renderPosXIn, double renderPosYIn, double renderPosZIn)
    {
        renderPosX = renderPosXIn;
        renderPosY = renderPosYIn;
        renderPosZ = renderPosZIn;
    }

    public <T extends Entity> Render<T> getEntityClassRenderObject(Class <? extends Entity > entityClass)
    {
        Render<T> render = (Render) entityRenderMap.get(entityClass);

        if (render == null && entityClass != Entity.class)
        {
            render = getEntityClassRenderObject((Class<? extends Entity>) entityClass.getSuperclass());
            entityRenderMap.put(entityClass, render);
        }

        return render;
    }

    @Nullable
    public <T extends Entity> Render<T> getEntityRenderObject(Entity entityIn)
    {
        if (entityIn instanceof AbstractClientPlayer)
        {
            String s = ((AbstractClientPlayer)entityIn).getSkinType();
            RenderPlayer renderplayer = skinMap.get(s);
            return (Render<T>)(renderplayer != null ? renderplayer : playerRenderer);
        }
        else
        {
            return getEntityClassRenderObject(entityIn.getClass());
        }
    }

    public void cacheActiveRenderInfo(World worldIn, Font textRendererIn, Entity livingPlayerIn, Entity pointedEntityIn, GameSettings optionsIn, float partialTicks)
    {
        worldObj = worldIn;
        options = optionsIn;
        renderViewEntity = livingPlayerIn;
        pointedEntity = pointedEntityIn;
        textRenderer = textRendererIn;

        if (livingPlayerIn instanceof EntityLivingBase && ((EntityLivingBase)livingPlayerIn).isPlayerSleeping())
        {
            IBlockState iblockstate = worldIn.getBlockState(new BlockPos(livingPlayerIn));
            Block block = iblockstate.getBlock();

            if (Reflector.callBoolean(block, Reflector.ForgeBlock_isBed, iblockstate, worldIn, new BlockPos(livingPlayerIn), livingPlayerIn))
            {
                EnumFacing enumfacing = (EnumFacing)Reflector.call(block, Reflector.ForgeBlock_getBedDirection, iblockstate, worldIn, new BlockPos(livingPlayerIn));
                int i = enumfacing.getHorizontalIndex();
                playerViewY = (float)(i * 90 + 180);
                playerViewX = 0.0F;
            }
            else if (block == Blocks.BED)
            {
                int j = iblockstate.getValue(BlockHorizontal.FACING).getHorizontalIndex();
                playerViewY = (float)(j * 90 + 180);
                playerViewX = 0.0F;
            }
        }
        else
        {
            playerViewY = livingPlayerIn.prevRotationYaw + (livingPlayerIn.rotationYaw - livingPlayerIn.prevRotationYaw) * partialTicks;
            playerViewX = livingPlayerIn.prevRotationPitch + (livingPlayerIn.rotationPitch - livingPlayerIn.prevRotationPitch) * partialTicks;
        }

        if (optionsIn.thirdPersonView == 2)
        {
            playerViewY += 180.0F;
        }

        viewerPosX = livingPlayerIn.lastTickPosX + (livingPlayerIn.posX - livingPlayerIn.lastTickPosX) * (double)partialTicks;
        viewerPosY = livingPlayerIn.lastTickPosY + (livingPlayerIn.posY - livingPlayerIn.lastTickPosY) * (double)partialTicks;
        viewerPosZ = livingPlayerIn.lastTickPosZ + (livingPlayerIn.posZ - livingPlayerIn.lastTickPosZ) * (double)partialTicks;
    }

    public void setPlayerViewY(float playerViewYIn)
    {
        playerViewY = playerViewYIn;
    }

    public boolean isRenderShadow()
    {
        return renderShadow;
    }

    public void setRenderShadow(boolean renderShadowIn)
    {
        renderShadow = renderShadowIn;
    }

    public void setDebugBoundingBox(boolean debugBoundingBoxIn)
    {
        debugBoundingBox = debugBoundingBoxIn;
    }

    public boolean isDebugBoundingBox()
    {
        return debugBoundingBox;
    }

    public boolean isRenderMultipass(Entity p_188390_1_)
    {
        return getEntityRenderObject(p_188390_1_).isMultipass();
    }

    public boolean shouldRender(Entity entityIn, ICamera camera, double camX, double camY, double camZ)
    {
        Render<Entity> render = getEntityRenderObject(entityIn);
        return render != null && render.shouldRender(entityIn, camera, camX, camY, camZ);
    }

    public void renderEntityStatic(Entity entityIn, float partialTicks, boolean p_188388_3_)
    {
        if (entityIn.ticksExisted == 0)
        {
            entityIn.lastTickPosX = entityIn.posX;
            entityIn.lastTickPosY = entityIn.posY;
            entityIn.lastTickPosZ = entityIn.posZ;
        }

        double d0 = entityIn.lastTickPosX + (entityIn.posX - entityIn.lastTickPosX) * (double)partialTicks;
        double d1 = entityIn.lastTickPosY + (entityIn.posY - entityIn.lastTickPosY) * (double)partialTicks;
        double d2 = entityIn.lastTickPosZ + (entityIn.posZ - entityIn.lastTickPosZ) * (double)partialTicks;
        float f = entityIn.prevRotationYaw + (entityIn.rotationYaw - entityIn.prevRotationYaw) * partialTicks;
        int i = entityIn.getBrightnessForRender();

        if (entityIn.isBurning())
        {
            i = 15728880;
        }

        int j = i % 65536;
        int k = i / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)j, (float)k);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        doRenderEntity(entityIn, d0 - renderPosX, d1 - renderPosY, d2 - renderPosZ, f, partialTicks, p_188388_3_);
    }

    public void doRenderEntity(Entity entityIn, double x, double y, double z, float yaw, float partialTicks, boolean p_188391_10_)
    {
        Render<Entity> render = null;

        try
        {
            render = getEntityRenderObject(entityIn);

            if (render != null && renderEngine != null)
            {
                try
                {
                    render.setRenderOutlines(renderOutlines);

                    if (CustomEntityModels.isActive())
                    {
                        renderEntity = entityIn;
                        renderRender = render;
                    }

                    render.doRender(entityIn, x, y, z, yaw, partialTicks);
                }
                catch (Throwable throwable2)
                {
                    throw new ReportedException(CrashReport.makeCrashReport(throwable2, "Rendering entity in world"));
                }

                try
                {
                    if (!renderOutlines)
                    {
                        render.doRenderShadowAndFire(entityIn, x, y, z, yaw, partialTicks);
                    }
                }
                catch (Throwable throwable1)
                {
                    throw new ReportedException(CrashReport.makeCrashReport(throwable1, "Post-rendering entity in world"));
                }

                if (debugBoundingBox && !entityIn.isInvisible() && !p_188391_10_ && !Minecraft.getMinecraft().isReducedDebug())
                {
                    try
                    {
                        renderDebugBoundingBox(entityIn, x, y, z, yaw, partialTicks);
                    }
                    catch (Throwable throwable)
                    {
                        throw new ReportedException(CrashReport.makeCrashReport(throwable, "Rendering entity hitbox in world"));
                    }
                }
            }
        }
        catch (Throwable throwable3)
        {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable3, "Rendering entity in world");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Entity being rendered");
            entityIn.addEntityCrashInfo(crashreportcategory);
            CrashReportCategory crashreportcategory1 = crashreport.makeCategory("Renderer details");
            crashreportcategory1.addCrashSection("Assigned renderer", render);
            crashreportcategory1.addCrashSection("Location", CrashReportCategory.getCoordinateInfo(x, y, z));
            crashreportcategory1.addCrashSection("Rotation", Float.valueOf(yaw));
            crashreportcategory1.addCrashSection("Delta", Float.valueOf(partialTicks));
            throw new ReportedException(crashreport);
        }
    }

    public void renderMultipass(Entity p_188389_1_, float p_188389_2_)
    {
        if (p_188389_1_.ticksExisted == 0)
        {
            p_188389_1_.lastTickPosX = p_188389_1_.posX;
            p_188389_1_.lastTickPosY = p_188389_1_.posY;
            p_188389_1_.lastTickPosZ = p_188389_1_.posZ;
        }

        double d0 = p_188389_1_.lastTickPosX + (p_188389_1_.posX - p_188389_1_.lastTickPosX) * (double)p_188389_2_;
        double d1 = p_188389_1_.lastTickPosY + (p_188389_1_.posY - p_188389_1_.lastTickPosY) * (double)p_188389_2_;
        double d2 = p_188389_1_.lastTickPosZ + (p_188389_1_.posZ - p_188389_1_.lastTickPosZ) * (double)p_188389_2_;
        float f = p_188389_1_.prevRotationYaw + (p_188389_1_.rotationYaw - p_188389_1_.prevRotationYaw) * p_188389_2_;
        int i = p_188389_1_.getBrightnessForRender();

        if (p_188389_1_.isBurning())
        {
            i = 15728880;
        }

        int j = i % 65536;
        int k = i / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)j, (float)k);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        Render<Entity> render = getEntityRenderObject(p_188389_1_);

        if (render != null && renderEngine != null)
        {
            render.renderMultipass(p_188389_1_, d0 - renderPosX, d1 - renderPosY, d2 - renderPosZ, f, p_188389_2_);
        }
    }

    /**
     * Renders the bounding box around an entity when F3+B is pressed
     */
    private void renderDebugBoundingBox(Entity entityIn, double x, double y, double z, float entityYaw, float partialTicks)
    {
        GlStateManager.depthMask(false);
        GlStateManager.disableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.disableCull();
        GlStateManager.disableBlend();
        float f = entityIn.width / 2.0F;
        AxisAlignedBB axisalignedbb = entityIn.getEntityBoundingBox();
        RenderGlobal.drawBoundingBox(axisalignedbb.minX - entityIn.posX + x, axisalignedbb.minY - entityIn.posY + y, axisalignedbb.minZ - entityIn.posZ + z, axisalignedbb.maxX - entityIn.posX + x, axisalignedbb.maxY - entityIn.posY + y, axisalignedbb.maxZ - entityIn.posZ + z, 1.0F, 1.0F, 1.0F, 1.0F);
        Entity[] aentity = entityIn.getParts();

        if (aentity != null)
        {
            for (Entity entity : aentity)
            {
                double d0 = (entity.posX - entity.prevPosX) * (double)partialTicks;
                double d1 = (entity.posY - entity.prevPosY) * (double)partialTicks;
                double d2 = (entity.posZ - entity.prevPosZ) * (double)partialTicks;
                AxisAlignedBB axisalignedbb1 = entity.getEntityBoundingBox();
                RenderGlobal.drawBoundingBox(axisalignedbb1.minX - renderPosX + d0, axisalignedbb1.minY - renderPosY + d1, axisalignedbb1.minZ - renderPosZ + d2, axisalignedbb1.maxX - renderPosX + d0, axisalignedbb1.maxY - renderPosY + d1, axisalignedbb1.maxZ - renderPosZ + d2, 0.25F, 1.0F, 0.0F, 1.0F);
            }
        }

        if (entityIn instanceof EntityLivingBase)
        {
            float f1 = 0.01F;
            RenderGlobal.drawBoundingBox(x - (double)f, y + (double)entityIn.getEyeHeight() - 0.009999999776482582D, z - (double)f, x + (double)f, y + (double)entityIn.getEyeHeight() + 0.009999999776482582D, z + (double)f, 1.0F, 0.0F, 0.0F, 1.0F);
        }

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        Vec3d vec3d = entityIn.getLook(partialTicks);
        bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(x, y + (double)entityIn.getEyeHeight(), z).color(0, 0, 255, 255).endVertex();
        bufferbuilder.pos(x + vec3d.xCoord * 2.0D, y + (double)entityIn.getEyeHeight() + vec3d.yCoord * 2.0D, z + vec3d.zCoord * 2.0D).color(0, 0, 255, 255).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.enableLighting();
        GlStateManager.enableCull();
        GlStateManager.disableBlend();
        GlStateManager.depthMask(true);
    }

    /**
     * World sets this RenderManager's worldObj to the world provided
     */
    public void set(@Nullable World worldIn)
    {
        worldObj = worldIn;

        if (worldIn == null)
        {
            renderViewEntity = null;
        }
    }

    public double getDistanceToCamera(double x, double y, double z)
    {
        double d0 = x - viewerPosX;
        double d1 = y - viewerPosY;
        double d2 = z - viewerPosZ;
        return d0 * d0 + d1 * d1 + d2 * d2;
    }

    /**
     * Returns the font renderer
     */
    public Font getFontRenderer()
    {
        return textRenderer;
    }

    public void setRenderOutlines(boolean renderOutlinesIn)
    {
        renderOutlines = renderOutlinesIn;
    }

    public Map<Class, Render> getEntityRenderMap()
    {
        return entityRenderMap;
    }

    public Map<String, RenderPlayer> getSkinMap()
    {
        return Collections.unmodifiableMap(skinMap);
    }
}
