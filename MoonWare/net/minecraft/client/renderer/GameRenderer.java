package net.minecraft.client.renderer;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.gson.JsonSyntaxException;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.MapItemRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.advancements.GuiScreenAdvancements;
import net.minecraft.client.gui.screen.LevelLoadingScreen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.client.renderer.culling.ClippingHelper;
import net.minecraft.client.renderer.culling.ClippingHelperImpl;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.client.shader.ShaderLinkHelper;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.text.TextComponent;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.Biome;
import optifine.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.glu.Project;
import org.moonware.client.MoonWare;
import org.moonware.client.event.EventManager;
import org.moonware.client.event.events.impl.render.EventFogColor;
import org.moonware.client.event.events.impl.render.EventRender3D;
import org.moonware.client.feature.impl.combat.Reach;
import org.moonware.client.feature.impl.combat.TargetHUD;
import org.moonware.client.feature.impl.hud.HUD;
import org.moonware.client.feature.impl.hud.WaterMark;
import org.moonware.client.feature.impl.misc.WeatherColor;
import org.moonware.client.feature.impl.player.NoInteract;
import org.moonware.client.feature.impl.visual.*;
import org.moonware.client.feature.impl.visual.anim.WorldRenderEvent;
import org.moonware.client.helpers.misc.ClientHelper;
import org.moonware.client.helpers.misc.TimerHelper;
import org.moonware.client.helpers.palette.PaletteHelper;
import org.moonware.client.ui.GuiConfig;
import org.moonware.client.ui.celestialgui.ClickGuiScreen;
import org.moonware.client.ui.components.draggable.HudComponent;
import org.moonware.client.ui.components.draggable.HudManager;
import org.moonware.client.ui.components.draggable.impl.*;
import org.moonware.client.ui.shader.notification.NotificationManager;
import shadersmod.client.Shaders;
import shadersmod.client.ShadersRender;

import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class GameRenderer
        implements IResourceManagerReloadListener {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Namespaced RAIN_TEXTURES = new Namespaced("textures/environment/rain.png");
    private static final Namespaced SNOW_TEXTURES = new Namespaced("textures/environment/snow.png");
    public static boolean anaglyphEnable;
    public static int anaglyphField;
    private final Minecraft mc;
    private final IResourceManager resourceManager;
    private final Random random = new Random();
    private float farPlaneDistance;
    public ItemRenderer itemRenderer;
    private final MapItemRenderer theMapItemRenderer;
    private int rendererUpdateCount;
    private Entity pointedEntity;
    private MouseFilter mouseFilterXAxis = new MouseFilter();
    private MouseFilter mouseFilterYAxis = new MouseFilter();
    private final float thirdPersonDistance = 4.0f;
    private float thirdPersonDistancePrev = 4.0f;
    private float smoothCamYaw;
    private float smoothCamPitch;
    private float smoothCamFilterX;
    private float smoothCamFilterY;
    private float smoothCamPartialTicks;
    private float fovModifierHand;
    private float fovModifierHandPrev;
    private float bossColorModifier;
    private float bossColorModifierPrev;
    private boolean cloudFog;
    private boolean renderHand = true;
    private boolean drawBlockOutline = true;
    private long timeWorldIcon;
    private long prevFrameTime = Minecraft.getSystemTime();
    private long renderEndNanoTime;
    private final DynamicTexture lightmapTexture;
    private final int[] lightmapColors;
    private final Namespaced locationLightMap;
    private boolean lightmapUpdateNeeded;
    private float torchFlickerX;
    private float torchFlickerDX;
    private int rainSoundCounter;
    private final float[] rainXCoords = new float[1024];
    private final float[] rainYCoords = new float[1024];
    private final FloatBuffer fogColorBuffer = GLAllocation.createDirectFloatBuffer(16);
    public float fogColorRed;
    public float fogColorGreen;
    public float fogColorBlue;
    private float fogColor2;
    private float fogColor1;
    private int debugViewDirection;
    private boolean debugView;
    private double cameraZoom = 1.0;
    private double cameraYaw;
    private double cameraPitch;
    private ItemStack field_190566_ab;
    private int field_190567_ac;
    private float field_190568_ad;
    private float field_190569_ae;
    public ShaderGroup theShaderGroup;
    private static final Namespaced[] SHADERS_TEXTURES;
    public static final int SHADER_COUNT;
    private int shaderIndex;
    private boolean useShader;
    public int frameCount;
    private boolean initialized;
    private World updatedWorld;
    public boolean fogStandard;
    private float clipDistance = 128.0f;
    private long lastServerTime;
    private int lastServerTicks;
    private int serverWaitTime;
    private int serverWaitTimeCurrent;
    private float avgServerTimeDiff;
    private float avgServerTickDiff;
    private long lastErrorCheckTimeMs;
    private ShaderGroup[] fxaaShaders = new ShaderGroup[10];
    private boolean loadVisibleChunks;
    private float zoomSpeed = 1.0f;
    float d3;

    public GameRenderer(Minecraft mcIn, IResourceManager resourceManagerIn) {
        shaderIndex = SHADER_COUNT;
        mc = mcIn;
        resourceManager = resourceManagerIn;
        itemRenderer = Minecraft.getItemRenderer();
        theMapItemRenderer = new MapItemRenderer(Minecraft.getTextureManager());
        lightmapTexture = new DynamicTexture(16, 16);
        locationLightMap = Minecraft.getTextureManager().getDynamicTextureLocation("lightMap", lightmapTexture);
        lightmapColors = lightmapTexture.getTextureData();
        theShaderGroup = null;
        for (int i = 0; i < 32; ++i) {
            for (int j = 0; j < 32; ++j) {
                float f = j - 16;
                float f1 = i - 16;
                float f2 = MathHelper.sqrt(f * f + f1 * f1);
                rainXCoords[i << 5 | j] = -f1 / f2;
                rainYCoords[i << 5 | j] = f / f2;
            }
        }
    }

    public boolean isShaderActive() {
        return OpenGlHelper.shadersSupported && theShaderGroup != null;
    }

    public void stopUseShader() {
        if (theShaderGroup != null) {
            theShaderGroup.deleteShaderGroup();
        }
        theShaderGroup = null;
        shaderIndex = SHADER_COUNT;
    }

    public void switchUseShader() {
        useShader = !useShader;
    }

    public void loadEntityShader(@Nullable Entity entityIn) {
        if (OpenGlHelper.shadersSupported) {
            if (theShaderGroup != null) {
                theShaderGroup.deleteShaderGroup();
            }
            theShaderGroup = null;
            if (entityIn instanceof EntityCreeper) {
                loadShader(new Namespaced("shaders/post/creeper.json"));
            } else if (entityIn instanceof EntitySpider) {
                loadShader(new Namespaced("shaders/post/spider.json"));
            } else if (entityIn instanceof EntityEnderman) {
                loadShader(new Namespaced("shaders/post/invert.json"));
            } else if (Reflector.ForgeHooksClient_loadEntityShader.exists()) {
                Reflector.call(Reflector.ForgeHooksClient_loadEntityShader, entityIn, this);
            }
        }
    }

    public void loadShader(Namespaced namespacedIn) {
        if (OpenGlHelper.isFramebufferEnabled()) {
            try {
                theShaderGroup = new ShaderGroup(Minecraft.getTextureManager(), resourceManager, Minecraft.getFramebuffer(), namespacedIn);
                theShaderGroup.createBindFramebuffers(Minecraft.width, Minecraft.height);
                useShader = true;
            }
            catch (IOException ioexception) {
                LOGGER.warn("Failed to load shader: {}", namespacedIn, ioexception);
                shaderIndex = SHADER_COUNT;
                useShader = false;
            }
            catch (JsonSyntaxException jsonsyntaxexception) {
                LOGGER.warn("Failed to load shader: {}", namespacedIn, jsonsyntaxexception);
                shaderIndex = SHADER_COUNT;
                useShader = false;
            }
        }
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {
        if (theShaderGroup != null) {
            theShaderGroup.deleteShaderGroup();
        }
        theShaderGroup = null;
        if (shaderIndex == SHADER_COUNT) {
            loadEntityShader(Minecraft.getRenderViewEntity());
        } else {
            loadShader(SHADERS_TEXTURES[shaderIndex]);
        }
    }

    public void updateRenderer() {
        if (OpenGlHelper.shadersSupported && ShaderLinkHelper.getStaticShaderLinkHelper() == null) {
            ShaderLinkHelper.setNewStaticShaderLinkHelper();
        }
        updateFovModifierHand();
        updateTorchFlicker();
        fogColor2 = fogColor1;
        thirdPersonDistancePrev = 4.0f;
        if (Minecraft.gameSettings.smoothCamera) {
            float f = Minecraft.gameSettings.mouseSensitivity * 0.6f + 0.2f;
            float f1 = f * f * f * 8.0f;
            smoothCamFilterX = mouseFilterXAxis.smooth(smoothCamYaw, 0.05f * f1);
            smoothCamFilterY = mouseFilterYAxis.smooth(smoothCamPitch, 0.05f * f1);
            smoothCamPartialTicks = 0.0f;
            smoothCamYaw = 0.0f;
            smoothCamPitch = 0.0f;
        } else {
            smoothCamFilterX = 0.0f;
            smoothCamFilterY = 0.0f;
            mouseFilterXAxis.reset();
            mouseFilterYAxis.reset();
        }
        if (Minecraft.getRenderViewEntity() == null) {
            mc.setRenderViewEntity(Minecraft.player);
        }
        Entity entity = Minecraft.getRenderViewEntity();
        double d2 = entity.posX;
        double d0 = entity.posY + (double)entity.getEyeHeight();
        double d1 = entity.posZ;
        float f2 = Minecraft.world.getLightBrightness(new BlockPos(d2, d0, d1));
        float f3 = (float) Minecraft.gameSettings.renderDistance / 16.0f;
        f3 = MathHelper.clamp(f3, 0.0f, 1.0f);
        float f4 = f2 * (1.0f - f3) + f3;
        fogColor1 += (f4 - fogColor1) * 0.1f;
        ++rendererUpdateCount;
        itemRenderer.updateEquippedItem();
        addRainParticles();
        bossColorModifierPrev = bossColorModifier;
        if (Minecraft.ingameGUI.getBossbarHud().shouldDarkenSky()) {
            bossColorModifier += 0.05f;
            if (bossColorModifier > 1.0f) {
                bossColorModifier = 1.0f;
            }
        } else if (bossColorModifier > 0.0f) {
            bossColorModifier -= 0.0125f;
        }
        if (field_190567_ac > 0) {
            --field_190567_ac;
            if (field_190567_ac == 0) {
                field_190566_ab = null;
            }
        }
    }

    public ShaderGroup getShaderGroup() {
        return theShaderGroup;
    }

    public void updateShaderGroupSize(int width, int height) {
        if (OpenGlHelper.shadersSupported) {
            if (theShaderGroup != null) {
                theShaderGroup.createBindFramebuffers(width, height);
            }
            Minecraft.renderGlobal.createBindEntityOutlineFbs(width, height);
        }
    }

    public void getMouseOver(float partialTicks) {
        Entity entity = Minecraft.getRenderViewEntity();
        if (entity != null && Minecraft.world != null) {
            double reachValue;
            Minecraft.profiler.startSection("pick");
            Minecraft.pointedEntity = null;
            double d0 = Minecraft.playerController.getBlockReachDistance();
            Minecraft.objectMouseOver = entity.rayTrace(d0, partialTicks);
            Vec3d vec3d = entity.getPositionEyes(partialTicks);
            boolean flag = false;
            double d1 = d0;
            double d = reachValue = MoonWare.featureManager.getFeatureByClass(Reach.class).getState() ? (double)Reach.reachValue.getCurrentValue() : 3.0;
            if (Minecraft.playerController.extendedReach()) {
                d0 = d1 = 6.0;
            } else if (d0 > reachValue) {
                flag = true;
            }
            if (Minecraft.objectMouseOver != null) {
                d1 = Minecraft.objectMouseOver.hitVec.distanceTo(vec3d);
            }
            Vec3d vec3d1 = entity.getLook(1.0f);
            Vec3d vec3d2 = vec3d.add(vec3d1.x * d0, vec3d1.y * d0, vec3d1.z * d0);
            pointedEntity = null;
            Vec3d vec3d3 = null;
            List<Entity> list = Minecraft.world.getEntitiesInAABBexcluding(entity, entity.getEntityBoundingBox().addCoord(vec3d1.x * d0, vec3d1.y * d0, vec3d1.z * d0).expand(1.0, 1.0, 1.0), Predicates.and(EntitySelectors.NOT_SPECTATING, new Predicate<Entity>(){

                @Override
                public boolean apply(@Nullable Entity p_apply_1_) {
                    return p_apply_1_ != null && p_apply_1_.canBeCollidedWith();
                }
            }));
            double d2 = d1;
            for (Entity entity1 : list) {
                double d3;
                if (entity1 instanceof EntityArmorStand && MoonWare.featureManager.getFeatureByClass(NoInteract.class).getState() && NoInteract.armorStands.getCurrentValue()) {
                    return;
                }
                AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().expandXyz(entity1.getCollisionBorderSize());
                RayTraceResult raytraceresult = axisalignedbb.calculateIntercept(vec3d, vec3d2);
                if (axisalignedbb.isVecInside(vec3d)) {
                    if (!(d2 >= 0.0)) continue;
                    pointedEntity = entity1;
                    vec3d3 = raytraceresult == null ? vec3d : raytraceresult.hitVec;
                    d2 = 0.0;
                    continue;
                }
                if (raytraceresult == null || !((d3 = vec3d.distanceTo(raytraceresult.hitVec)) < d2) && d2 != 0.0) continue;
                boolean flag1 = false;
                if (Reflector.ForgeEntity_canRiderInteract.exists()) {
                    flag1 = Reflector.callBoolean(entity1, Reflector.ForgeEntity_canRiderInteract);
                }
                if (!flag1 && entity1.getLowestRidingEntity() == entity.getLowestRidingEntity()) {
                    if (d2 != 0.0) continue;
                    pointedEntity = entity1;
                    vec3d3 = raytraceresult.hitVec;
                    continue;
                }
                pointedEntity = entity1;
                vec3d3 = raytraceresult.hitVec;
                d2 = d3;
            }
            if (pointedEntity != null && flag && vec3d.distanceTo(vec3d3) > reachValue) {
                pointedEntity = null;
                Minecraft.objectMouseOver = new RayTraceResult(RayTraceResult.Type.MISS, vec3d3, null, new BlockPos(vec3d3));
            }
            if (pointedEntity != null && (d2 < d1 || Minecraft.objectMouseOver == null)) {
                Minecraft.objectMouseOver = new RayTraceResult(pointedEntity, vec3d3);
                if (pointedEntity instanceof EntityLivingBase || pointedEntity instanceof EntityItemFrame) {
                    Minecraft.pointedEntity = pointedEntity;
                }
            }
            Minecraft.profiler.endSection();
        }
    }

    private void updateFovModifierHand() {
        float f = 1.0f;
        if (Minecraft.getRenderViewEntity() instanceof AbstractClientPlayer) {
            AbstractClientPlayer abstractclientplayer = (AbstractClientPlayer) Minecraft.getRenderViewEntity();
            f = abstractclientplayer.getFovModifier();
        }
        fovModifierHandPrev = fovModifierHand;
        fovModifierHand += (f - fovModifierHand) * 0.5f;
        if (fovModifierHand > 1.5f) {
            fovModifierHand = 1.5f;
        }
        if (fovModifierHand < 0.1f) {
            fovModifierHand = 0.1f;
        }
    }

    private float getFOVModifier(float partialTicks, boolean useFOVSetting) {
        IBlockState iblockstate;
        if (debugView) {
            return 90.0f;
        }
        Entity entity = Minecraft.getRenderViewEntity();
        float f = 70.0f;
        if (useFOVSetting) {
            f = Minecraft.gameSettings.fov;
            if (Config.isDynamicFov()) {
                f *= fovModifierHandPrev + (fovModifierHand - fovModifierHandPrev) * partialTicks;
            }
        }
        boolean flag = false;
        if (Minecraft.screen == null) {
            flag = GameSettings.isKeyDown(Minecraft.gameSettings.ofKeyBindZoom);
        }
        double zoomSpeedPress = 0.05;
        double zoomSpeedUNPress = 0.03;
        if (flag) {
            if (!Config.zoomMode) {
                Config.zoomMode = true;
                Minecraft.gameSettings.smoothCamera = true;
                Minecraft.renderGlobal.displayListEntitiesDirty = true;
            }
            if (zoomSpeed < 5.0f) {
                zoomSpeed = (float)((double) zoomSpeed + zoomSpeedPress * (Minecraft.frameTime * 0.1));
            }
        } else {
            if (zoomSpeed > 1.0f) {
                zoomSpeed = (float)((double) zoomSpeed - zoomSpeedUNPress * (Minecraft.frameTime * 0.1));
            }
            if (Config.zoomMode && zoomSpeed < 1.0f) {
                Config.zoomMode = false;
                Minecraft.gameSettings.smoothCamera = false;
                mouseFilterXAxis = new MouseFilter();
                mouseFilterYAxis = new MouseFilter();
                Minecraft.renderGlobal.displayListEntitiesDirty = true;
            }
        }
        f /= zoomSpeed;
        if (entity instanceof EntityLivingBase && ((EntityLivingBase)entity).getHealth() <= 0.0f) {
            float f1 = (float)((EntityLivingBase)entity).deathTime + partialTicks;
            f /= (1.0f - 500.0f / (f1 + 500.0f)) * 2.0f + 1.0f;
        }
        if ((iblockstate = ActiveRenderInfo.getBlockStateAtEntityViewpoint(Minecraft.world, entity, partialTicks)).getMaterial() == Material.WATER) {
            f = f * 60.0f / 70.0f;
        }
        return Reflector.ForgeHooksClient_getFOVModifier.exists() ? Reflector.callFloat(Reflector.ForgeHooksClient_getFOVModifier, this, entity, iblockstate, Float.valueOf(partialTicks), Float.valueOf(f)) : f;
    }

    private void hurtCameraEffect(float partialTicks) {
        if (MoonWare.featureManager.getFeatureByClass(NoRender.class).getState() && NoRender.hurt.getCurrentValue()) {
            return;
        }
        if (Minecraft.getRenderViewEntity() instanceof EntityLivingBase) {
            EntityLivingBase entitylivingbase = (EntityLivingBase) Minecraft.getRenderViewEntity();
            float f = (float)entitylivingbase.hurtTime - partialTicks;
            if (entitylivingbase.getHealth() <= 0.0f) {
                float f1 = (float)entitylivingbase.deathTime + partialTicks;
                GlStateManager.rotate(40.0f - 8000.0f / (f1 + 200.0f), 0.0f, 0.0f, 1.0f);
            }
            if (f < 0.0f) {
                return;
            }
            f /= (float)entitylivingbase.maxHurtTime;
            f = MathHelper.sin(f * f * f * f * (float)Math.PI);
            float f2 = entitylivingbase.attackedAtYaw;
            GlStateManager.rotate(-f2, 0.0f, 1.0f, 0.0f);
            GlStateManager.rotate(-f * 14.0f, 0.0f, 0.0f, 1.0f);
            GlStateManager.rotate(f2, 0.0f, 1.0f, 0.0f);
        }
    }

    private void setupViewBobbing(float partialTicks) {
        if (Minecraft.getRenderViewEntity() instanceof EntityPlayer) {
            EntityPlayer entityplayer = (EntityPlayer) Minecraft.getRenderViewEntity();
            float f = entityplayer.distanceWalkedModified - entityplayer.prevDistanceWalkedModified;
            float f1 = -(entityplayer.distanceWalkedModified + f * partialTicks);
            float f2 = entityplayer.prevCameraYaw + (entityplayer.cameraYaw - entityplayer.prevCameraYaw) * partialTicks;
            float f3 = entityplayer.prevCameraPitch + (entityplayer.cameraPitch - entityplayer.prevCameraPitch) * partialTicks;
            GlStateManager.translate(MathHelper.sin(f1 * (float)Math.PI) * f2 * 0.5f, -Math.abs(MathHelper.cos(f1 * (float)Math.PI) * f2), 0.0f);
            GlStateManager.rotate(MathHelper.sin(f1 * (float)Math.PI) * f2 * 3.0f, 0.0f, 0.0f, 1.0f);
            GlStateManager.rotate(Math.abs(MathHelper.cos(f1 * (float)Math.PI - 0.2f) * f2) * 5.0f, 1.0f, 0.0f, 0.0f);
            GlStateManager.rotate(f3, 1.0f, 0.0f, 0.0f);
        }
    }

    private void orientCamera(float partialTicks)
    {
        Entity entity = Minecraft.getRenderViewEntity();
        float f = entity.getEyeHeight();
        double d0 = entity.prevPosX + (entity.posX - entity.prevPosX) * (double)partialTicks;
        double d1 = entity.prevPosY + (entity.posY - entity.prevPosY) * (double)partialTicks + (double)f;
        double d2 = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * (double)partialTicks;

        if (entity instanceof EntityLivingBase && ((EntityLivingBase)entity).isPlayerSleeping())
        {
            f = (float)((double)f + 1.0D);
            GlStateManager.translate(0.0F, 0.3F, 0.0F);
            BlockPos blockpos = new BlockPos(entity);
            IBlockState iblockstate = Minecraft.world.getBlockState(blockpos);
            Block block = iblockstate.getBlock();

            if (Reflector.ForgeHooksClient_orientBedCamera.exists())
            {
                Reflector.callVoid(Reflector.ForgeHooksClient_orientBedCamera, Minecraft.world, blockpos, iblockstate, entity);
            }
            else if (block == Blocks.BED)
            {
                int j = iblockstate.getValue(BlockHorizontal.FACING).getHorizontalIndex();
                GlStateManager.rotate((float)(j * 90), 0.0F, 1.0F, 0.0F);
            }

            GlStateManager.rotate(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks + 180.0F, 0.0F, -1.0F, 0.0F);
            GlStateManager.rotate(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks, -1.0F, 0.0F, 0.0F);
        }
        else if (Minecraft.gameSettings.thirdPersonView > 0)
        {
            double d3 = MoonWare.featureManager.getFeatureByClass(PersonViewer.class).getState() ? PersonViewer.fovModifier.getNumberValue() : (double)(thirdPersonDistancePrev + (4.0F - thirdPersonDistancePrev) * partialTicks);
            {
                float f1;
                float f2;
                if (MoonWare.featureManager.getFeatureByClass(PersonViewer.class).getState()) {
                    f1 = entity.rotationYaw + PersonViewer.viewerYaw.getNumberValue();
                    f2 = entity.rotationPitch + PersonViewer.viewerPitch.getNumberValue();
                } else {
                    f1 = entity.rotationYaw;
                    f2 = entity.rotationPitch;
                }

                if (Minecraft.gameSettings.thirdPersonView == 2) {
                    f2 += 180.0F;
                }

                double d4 = (double)(-MathHelper.sin(f1 * 0.017453292F) * MathHelper.cos(f2 * 0.017453292F)) * d3;
                double d5 = (double)(MathHelper.cos(f1 * 0.017453292F) * MathHelper.cos(f2 * 0.017453292F)) * d3;
                double d6 = (double)(-MathHelper.sin(f2 * 0.017453292F)) * d3;

                for (int i = 0; i < 8; ++i)
                {
                    float f3 = (float)((i & 1) * 2 - 1);
                    float f4 = (float)((i >> 1 & 1) * 2 - 1);
                    float f5 = (float)((i >> 2 & 1) * 2 - 1);
                    f3 = f3 * 0.1F;
                    f4 = f4 * 0.1F;
                    f5 = f5 * 0.1F;

                    if(!(MoonWare.featureManager.getFeatureByClass(CameraNoClip.class).getState()))
                    {
                        RayTraceResult raytraceresult = Minecraft.world.rayTraceBlocks(new Vec3d(d0 + (double) f3, d1 + (double) f4, d2 + (double) f5), new Vec3d(d0 - d4 + (double) f3 + (double) f5, d1 - d6 + (double) f4, d2 - d5 + (double) f5));

                        if (raytraceresult != null) {
                            double d7 = raytraceresult.hitVec.distanceTo(new Vec3d(d0, d1, d2));

                            if (d7 < d3) {
                                d3 = d7;
                            }
                        }
                    }
                }

                if (Minecraft.gameSettings.thirdPersonView == 2)
                {
                    GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
                }

                GlStateManager.rotate(entity.rotationPitch - f2, 1.0F, 0.0F, 0.0F);
                GlStateManager.rotate(entity.rotationYaw - f1, 0.0F, 1.0F, 0.0F);
                GlStateManager.translate(0.0F, 0.0F, (float)(-d3));
                GlStateManager.rotate(f1 - entity.rotationYaw, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotate(f2 - entity.rotationPitch, 1.0F, 0.0F, 0.0F);
            }
        }
        else
        {
            GlStateManager.translate(0.0F, 0.0F, 0.05F);
        }

        if (Reflector.EntityViewRenderEvent_CameraSetup_Constructor.exists())
        {
            {
                float f6 = entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks + 180.0F;
                float f7 = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;
                float f8 = 0.0F;

                if (entity instanceof EntityAnimal)
                {
                    EntityAnimal entityanimal1 = (EntityAnimal)entity;
                    f6 = entityanimal1.prevRotationYawHead + (entityanimal1.rotationYawHead - entityanimal1.prevRotationYawHead) * partialTicks + 180.0F;
                }

                IBlockState iblockstate1 = ActiveRenderInfo.getBlockStateAtEntityViewpoint(Minecraft.world, entity, partialTicks);
                Object object = Reflector.newInstance(Reflector.EntityViewRenderEvent_CameraSetup_Constructor, this, entity, iblockstate1, partialTicks, f6, f7, f8);
                Reflector.postForgeBusEvent(object);
                f8 = Reflector.callFloat(object, Reflector.EntityViewRenderEvent_CameraSetup_getRoll);
                f7 = Reflector.callFloat(object, Reflector.EntityViewRenderEvent_CameraSetup_getPitch);
                f6 = Reflector.callFloat(object, Reflector.EntityViewRenderEvent_CameraSetup_getYaw);
                GlStateManager.rotate(f8, 0.0F, 0.0F, 1.0F);
                GlStateManager.rotate(f7, 1.0F, 0.0F, 0.0F);
                GlStateManager.rotate(f6, 0.0F, 1.0F, 0.0F);
            }
        }
        else
        {
            GlStateManager.rotate(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks, 1.0F, 0.0F, 0.0F);

            if (entity instanceof EntityAnimal)
            {
                EntityAnimal entityanimal = (EntityAnimal)entity;
                GlStateManager.rotate(entityanimal.prevRotationYawHead + (entityanimal.rotationYawHead - entityanimal.prevRotationYawHead) * partialTicks + 180.0F, 0.0F, 1.0F, 0.0F);
            }
            else
            {
                GlStateManager.rotate(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks + 180.0F, 0.0F, 1.0F, 0.0F);
            }
        }

        GlStateManager.translate(0.0F, -f, 0.0F);
        d0 = entity.prevPosX + (entity.posX - entity.prevPosX) * (double)partialTicks;
        d1 = entity.prevPosY + (entity.posY - entity.prevPosY) * (double)partialTicks + (double)f;
        d2 = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * (double)partialTicks;
        cloudFog = Minecraft.renderGlobal.hasCloudFog(d0, d1, d2, partialTicks);
    }

    public void setupCameraTransform(float partialTicks, int pass) {
        float f1;
        farPlaneDistance = Minecraft.gameSettings.renderDistance * 16;
        if (Config.isFogFancy()) {
            farPlaneDistance *= 0.95f;
        }
        if (Config.isFogFast()) {
            farPlaneDistance *= 0.83f;
        }
        GlStateManager.matrixMode(5889);
        GlStateManager.loadIdentity();
        if (Minecraft.gameSettings.anaglyph) {
            GlStateManager.translate((float)(-(pass * 2 - 1)) * 0.07f, 0.0f, 0.0f);
        }
        clipDistance = farPlaneDistance * 2.0f;
        if (clipDistance < 173.0f) {
            clipDistance = 173.0f;
        }
        if (cameraZoom != 1.0) {
            GlStateManager.translate((float) cameraYaw, (float)(-cameraPitch), 0.0f);
            GlStateManager.scale(cameraZoom, cameraZoom, 1.0);
        }
        Project.gluPerspective(getFOVModifier(partialTicks, true), (float) Minecraft.width / (float) Minecraft.height, 0.05f, clipDistance);
        GlStateManager.matrixMode(5888);
        GlStateManager.loadIdentity();
        if (Minecraft.gameSettings.anaglyph) {
            GlStateManager.translate((float)(pass * 2 - 1) * 0.1f, 0.0f, 0.0f);
        }
        hurtCameraEffect(partialTicks);
        if (Minecraft.gameSettings.viewBobbing) {
            setupViewBobbing(partialTicks);
        }
        if ((f1 = Minecraft.player.prevTimeInPortal + (Minecraft.player.timeInPortal - Minecraft.player.prevTimeInPortal) * partialTicks) > 0.0f) {
            int i = 20;
            if (Minecraft.player.isPotionActive(MobEffects.NAUSEA)) {
                i = 7;
            }
            float f2 = 5.0f / (f1 * f1 + 5.0f) - f1 * 0.04f;
            f2 *= f2;
            GlStateManager.rotate(((float) rendererUpdateCount + partialTicks) * (float)i, 0.0f, 1.0f, 1.0f);
            GlStateManager.scale(1.0f / f2, 1.0f, 1.0f);
            GlStateManager.rotate(-((float) rendererUpdateCount + partialTicks) * (float)i, 0.0f, 1.0f, 1.0f);
        }
        orientCamera(partialTicks);
        if (debugView) {
            switch (debugViewDirection) {
                case 0: {
                    GlStateManager.rotate(90.0f, 0.0f, 1.0f, 0.0f);
                    break;
                }
                case 1: {
                    GlStateManager.rotate(180.0f, 0.0f, 1.0f, 0.0f);
                    break;
                }
                case 2: {
                    GlStateManager.rotate(-90.0f, 0.0f, 1.0f, 0.0f);
                    break;
                }
                case 3: {
                    GlStateManager.rotate(90.0f, 1.0f, 0.0f, 0.0f);
                    break;
                }
                case 4: {
                    GlStateManager.rotate(-90.0f, 1.0f, 0.0f, 0.0f);
                }
            }
        }
    }

    private void renderHand(float partialTicks, int pass) {
        renderHand(partialTicks, pass, true, true, false);
    }

    public void renderHand(float p_renderHand_1_, int p_renderHand_2_, boolean p_renderHand_3_, boolean p_renderHand_4_, boolean p_renderHand_5_) {
        if (!debugView) {
            GlStateManager.matrixMode(5889);
            GlStateManager.loadIdentity();
            float f = 0.07f;
            if (Minecraft.gameSettings.anaglyph) {
                GlStateManager.translate((float)(-(p_renderHand_2_ * 2 - 1)) * 0.07f, 0.0f, 0.0f);
            }
            if (Config.isShaders()) {
                Shaders.applyHandDepth();
            }
            Project.gluPerspective(getFOVModifier(p_renderHand_1_, false), (float) Minecraft.width / (float) Minecraft.height, 0.05f, farPlaneDistance * 2.0f);
            GlStateManager.matrixMode(5888);
            GlStateManager.loadIdentity();
            if (Minecraft.gameSettings.anaglyph) {
                GlStateManager.translate((float)(p_renderHand_2_ * 2 - 1) * 0.1f, 0.0f, 0.0f);
            }
            boolean flag = false;
            if (p_renderHand_3_) {
                boolean flag1;
                GlStateManager.pushMatrix();
                hurtCameraEffect(p_renderHand_1_);
                if (Minecraft.gameSettings.viewBobbing) {
                    setupViewBobbing(p_renderHand_1_);
                }
                flag = Minecraft.getRenderViewEntity() instanceof EntityLivingBase && ((EntityLivingBase) Minecraft.getRenderViewEntity()).isPlayerSleeping();
                boolean bl = flag1 = !ReflectorForge.renderFirstPersonHand(Minecraft.renderGlobal, p_renderHand_1_, p_renderHand_2_);
                if (flag1 && Minecraft.gameSettings.thirdPersonView == 0 && !flag && !Minecraft.gameSettings.hideGUI && !Minecraft.playerController.isSpectator()) {
                    enableLightmap();
                    if (Config.isShaders()) {
                        ShadersRender.renderItemFP(itemRenderer, p_renderHand_1_, p_renderHand_5_);
                    } else {
                        itemRenderer.renderItemInFirstPerson(p_renderHand_1_);
                    }
                    disableLightmap();
                }
                GlStateManager.popMatrix();
            }
            if (!p_renderHand_4_) {
                return;
            }
            disableLightmap();
            if (Minecraft.gameSettings.thirdPersonView == 0 && !flag) {
                itemRenderer.renderOverlays(p_renderHand_1_);
                hurtCameraEffect(p_renderHand_1_);
            }
            if (Minecraft.gameSettings.viewBobbing) {
                setupViewBobbing(p_renderHand_1_);
            }
        }
    }

    public void disableLightmap() {
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        if (Config.isShaders()) {
            Shaders.disableLightmap();
        }
    }

    public void enableLightmap() {
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.matrixMode(5890);
        GlStateManager.loadIdentity();
        float f = 0.00390625f;
        GlStateManager.scale(0.00390625f, 0.00390625f, 0.00390625f);
        GlStateManager.translate(8.0f, 8.0f, 8.0f);
        GlStateManager.matrixMode(5888);
        Minecraft.getTextureManager().bindTexture(locationLightMap);
        GlStateManager.glTexParameteri(3553, 10241, 9729);
        GlStateManager.glTexParameteri(3553, 10240, 9729);
        GlStateManager.glTexParameteri(3553, 10242, 10496);
        GlStateManager.glTexParameteri(3553, 10243, 10496);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.enableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        if (Config.isShaders()) {
            Shaders.enableLightmap();
        }
    }

    private void updateTorchFlicker() {
        torchFlickerDX = (float)((double) torchFlickerDX + (Math.random() - Math.random()) * Math.random() * Math.random());
        torchFlickerDX = (float)((double) torchFlickerDX * 0.9);
        torchFlickerX += torchFlickerDX - torchFlickerX;
        lightmapUpdateNeeded = true;
    }

    private void updateLightmap(float partialTicks) {
        if (lightmapUpdateNeeded) {
            Minecraft.profiler.startSection("lightTex");
            ClientLevel world = Minecraft.world;
            if (world != null) {
                if (Config.isCustomColors() && CustomColors.updateLightmap(world, torchFlickerX, lightmapColors, Minecraft.player.isPotionActive(MobEffects.NIGHT_VISION))) {
                    lightmapTexture.updateDynamicTexture();
                    lightmapUpdateNeeded = false;
                    Minecraft.profiler.endSection();
                    return;
                }
                float f = world.getSunBrightness(1.0f);
                float f1 = f * 0.95f + 0.05f /* - (MoonWare.featureManager.getFeatureByClass(NightMode.class).getState() ? NightMode.darkModifier.getCurrentValue() : 0.0f) */;
                if (MoonWare.featureManager.getFeatureByClass(WorldColor.class).getState()) {
                    int oneColor = WorldColor.lightMapColor.getColor();
                    int color = 0;
                    switch (WorldColor.worldColorMode.currentMode) {
                        case "Client": {
                            color = ClientHelper.getClientColor().getRGB();
                            break;
                        }
                        case "Custom": {
                            color = oneColor;
                            break;
                        }
                        case "Astolfo": {
                            color = PaletteHelper.astolfo(5000.0f, 1).getRGB();
                            break;
                        }
                        case "Rainbow": {
                            color = PaletteHelper.rainbow(300, 1.0f, 1.0f).getRGB();
                        }
                    }
                    int color2 = color;
                    int r = color2 >> 16 & 0xFF;
                    int g = color2 >> 8 & 0xFF;
                    int b = color2 & 0xFF;
                    int a = color2 >> 24 & 0xFF;
                    for (int i = 0; i < 256; ++i) {
                        lightmapColors[i] = a << 24 | r << 16 | g << 8 | b;
                    }
                } else {
                    for (int i = 0; i < 256; ++i) {
                        float f2 = world.provider.getLightBrightnessTable()[i / 16] * f1;
                        float f3 = world.provider.getLightBrightnessTable()[i % 16] * (torchFlickerX * 0.1f + 1.5f);
                        if (world.getLastLightningBolt() > 0) {
                            f2 = world.provider.getLightBrightnessTable()[i / 16];
                        }
                        float f4 = f2 * (f * 0.65f + 0.35f);
                        float f5 = f2 * (f * 0.65f + 0.35f);
                        float f6 = f3 * ((f3 * 0.6f + 0.4f) * 0.6f + 0.4f);
                        float f7 = f3 * (f3 * f3 * 0.6f + 0.4f);
                        float f8 = f4 + f3;
                        float f9 = f5 + f6;
                        float f10 = f2 + f7;
                        f8 = f8 * 0.96f + 0.03f;
                        f9 = f9 * 0.96f + 0.03f;
                        f10 = f10 * 0.96f + 0.03f;
                        if (bossColorModifier > 0.0f) {
                            float f11 = bossColorModifierPrev + (bossColorModifier - bossColorModifierPrev) * partialTicks;
                            f8 = f8 * (1.0f - f11) + f8 * 0.7f * f11;
                            f9 = f9 * (1.0f - f11) + f9 * 0.6f * f11;
                            f10 = f10 * (1.0f - f11) + f10 * 0.6f * f11;
                        }
                        if (world.provider.getDimensionType().getId() == 1) {
                            f8 = 0.22f + f3 * 0.75f;
                            f9 = 0.28f + f6 * 0.75f;
                            f10 = 0.25f + f7 * 0.75f;
                        }
                        if (Reflector.ForgeWorldProvider_getLightmapColors.exists()) {
                            float[] afloat = {f8, f9, f10};
                            Reflector.call(world.provider, Reflector.ForgeWorldProvider_getLightmapColors, Float.valueOf(partialTicks), Float.valueOf(f), Float.valueOf(f2), Float.valueOf(f3), afloat);
                            f8 = afloat[0];
                            f9 = afloat[1];
                            f10 = afloat[2];
                        }
                        f8 = MathHelper.clamp(f8, 0.0f, 1.0f);
                        f9 = MathHelper.clamp(f9, 0.0f, 1.0f);
                        f10 = MathHelper.clamp(f10, 0.0f, 1.0f);
                        if (Minecraft.player.isPotionActive(MobEffects.NIGHT_VISION)) {
                            float f15 = getNightVisionBrightness(Minecraft.player, partialTicks);
                            float f12 = 1.0f / f8;
                            if (f12 > 1.0f / f9) {
                                f12 = 1.0f / f9;
                            }
                            if (f12 > 1.0f / f10) {
                                f12 = 1.0f / f10;
                            }
                            f8 = f8 * (1.0f - f15) + f8 * f12 * f15;
                            f9 = f9 * (1.0f - f15) + f9 * f12 * f15;
                            f10 = f10 * (1.0f - f15) + f10 * f12 * f15;
                        }
                        if (f8 > 1.0f) {
                            f8 = 1.0f;
                        }
                        if (f9 > 1.0f) {
                            f9 = 1.0f;
                        }
                        if (f10 > 1.0f) {
                            f10 = 1.0f;
                        }
                        float f16 = Minecraft.gameSettings.gamma;
                        float f17 = 1.0f - f8;
                        float f13 = 1.0f - f9;
                        float f14 = 1.0f - f10;
                        f17 = 1.0f - f17 * f17 * f17 * f17;
                        f13 = 1.0f - f13 * f13 * f13 * f13;
                        f14 = 1.0f - f14 * f14 * f14 * f14;
                        f8 = f8 * (1.0f - f16) + f17 * f16;
                        f9 = f9 * (1.0f - f16) + f13 * f16;
                        f10 = f10 * (1.0f - f16) + f14 * f16;
                        f8 = f8 * 0.96f + 0.03f;
                        f9 = f9 * 0.96f + 0.03f;
                        f10 = f10 * 0.96f + 0.03f;
                        if (f8 > 1.0f) {
                            f8 = 1.0f;
                        }
                        if (f9 > 1.0f) {
                            f9 = 1.0f;
                        }
                        if (f10 > 1.0f) {
                            f10 = 1.0f;
                        }
                        if (f8 < 0.0f) {
                            f8 = 0.0f;
                        }
                        if (f9 < 0.0f) {
                            f9 = 0.0f;
                        }
                        if (f10 < 0.0f) {
                            f10 = 0.0f;
                        }
                        int j = 255;
                        int k = (int)(f8 * 255.0f);
                        int l = (int)(f9 * 255.0f);
                        int i1 = (int)(f10 * 255.0f);
                        lightmapColors[i] = 0xFF000000 | k << 16 | l << 8 | i1;
                    }
                }
                lightmapTexture.updateDynamicTexture();
                lightmapUpdateNeeded = false;
                Minecraft.profiler.endSection();
            }
        }
    }

    public float getNightVisionBrightness(EntityLivingBase entitylivingbaseIn, float partialTicks) {
        int i = entitylivingbaseIn.getActivePotionEffect(MobEffects.NIGHT_VISION).getDuration();
        return i > 200 ? 1.0f : 0.7f + MathHelper.sin(((float)i - partialTicks) * (float)Math.PI * 0.2f) * 0.3f;
    }
    public TimerHelper timerHelper = new TimerHelper();
    public void updateCameraAndRender(float partialTicks, long nanoTime) {
        frameInit();
        boolean flag = Display.isActive();
        if (!(flag || !Minecraft.gameSettings.pauseOnLostFocus)) {
            if (Minecraft.getSystemTime() - prevFrameTime > 500L) {
                mc.displayInGameMenu();
            }
        } else {
            prevFrameTime = Minecraft.getSystemTime();
        }
        Minecraft.profiler.startSection("mouse");
        if (Minecraft.inGameHasFocus && flag) {
            Minecraft.mouseHelper.mouseXYChange();
            float f = Minecraft.gameSettings.mouseSensitivity * 0.6f + 0.2f;
            float f1 = f * f * f * 8.0f;
            float f2 = (float) Minecraft.mouseHelper.deltaX * f1;
            float f3 = (float) Minecraft.mouseHelper.deltaY * f1;
            int i = 1;
            if (Minecraft.gameSettings.invertMouse) {
                i = -1;
            }
            if (Minecraft.gameSettings.smoothCamera) {
                smoothCamYaw += f2;
                smoothCamPitch += f3;
                float f4 = partialTicks - smoothCamPartialTicks;
                smoothCamPartialTicks = partialTicks;
                f2 = smoothCamFilterX * f4;
                f3 = smoothCamFilterY * f4;
                Minecraft.player.setAngles(f2, f3 * (float)i);
            } else {
                smoothCamYaw = 0.0f;
                smoothCamPitch = 0.0f;
                Minecraft.player.setAngles(f2, f3 * (float)i);
            }
        }
        Minecraft.profiler.endSection();
        anaglyphEnable = Minecraft.gameSettings.anaglyph;
        ScaledResolution scaledresolution = new ScaledResolution(mc);
        int i1 = scaledresolution.getScaledWidth();
        int j1 = scaledresolution.getScaledHeight();
        int k1 = Mouse.getX() * i1 / Minecraft.width;
        int l1 = j1 - Mouse.getY() * j1 / Minecraft.height - 1;
        int i2 = Minecraft.gameSettings.limitFramerate;
        if (Minecraft.world == null) {
            GlStateManager.viewport(0, 0, Minecraft.width, Minecraft.height);
            GlStateManager.matrixMode(5889);
            GlStateManager.loadIdentity();
            GlStateManager.matrixMode(5888);
            GlStateManager.loadIdentity();
            setupOverlayRendering();
            renderEndNanoTime = System.nanoTime();
            TileEntityRendererDispatcher.instance.renderEngine = Minecraft.getTextureManager();
            TileEntityRendererDispatcher.instance.font = Minecraft.font;
        } else {
            Minecraft.profiler.startSection("level");
            int j = Math.min(Minecraft.getDebugFPS(), i2);
            j = Math.max(j, 60);
            long k = System.nanoTime() - nanoTime;
            long l = Math.max((long)(1000000000 / j / 4) - k, 0L);
            renderWorld(partialTicks, System.nanoTime() + l);
            if (mc.isSingleplayer() && timeWorldIcon < Minecraft.getSystemTime() - 1000L) {
                timeWorldIcon = Minecraft.getSystemTime();
                if (!Minecraft.getIntegratedServer().isWorldIconSet()) {
                    createWorldIcon();
                }
            }
            ScaledResolution sr = new ScaledResolution(mc);
            if (OpenGlHelper.shadersSupported) {
                Minecraft.renderGlobal.renderEntityOutlineFramebuffer();
                if (theShaderGroup != null && useShader) {
                    GlStateManager.matrixMode(5890);
                    GlStateManager.pushMatrix();
                    GlStateManager.loadIdentity();
                    theShaderGroup.loadShaderGroup(partialTicks);
                    GlStateManager.popMatrix();
                }
                Minecraft.getFramebuffer().bindFramebuffer(true);
            }
            renderEndNanoTime = System.nanoTime();
            Minecraft.profiler.endStartSection("gui");
            if (!Minecraft.gameSettings.hideGUI || Minecraft.screen != null) {
                GlStateManager.alphaFunc(516, 0.1f);
                setupOverlayRendering();
                func_190563_a(i1, j1, partialTicks);
                if (Minecraft.screen instanceof ClickGuiScreen || Minecraft.screen instanceof GuiConfig) {
                    ClickGuiScreen.callback();
                }
                Minecraft.ingameGUI.draw(partialTicks);
                if (Minecraft.gameSettings.ofShowFps && !Minecraft.gameSettings.showDebugInfo) {
                    Config.drawFps();
                }
                if (Minecraft.gameSettings.showDebugInfo) {
                    Lagometer.showLagometer(scaledresolution);
                }
            }
            Minecraft.profiler.endSection();
        }
        if (Minecraft.screen != null) {
            GlStateManager.clear(256);
            try {
                if (Reflector.ForgeHooksClient_drawScreen.exists()) {
                    Reflector.callVoid(Reflector.ForgeHooksClient_drawScreen, Minecraft.screen, k1, l1, Float.valueOf(mc.func_193989_ak()));
                } else {
                    Minecraft.screen.draw(k1, l1, mc.func_193989_ak());
                }
            }
            catch (Throwable throwable1) {
                CrashReport crashreport = CrashReport.makeCrashReport(throwable1, "Rendering screen");
                CrashReportCategory crashreportcategory = crashreport.makeCategory("Screen render details");
                crashreportcategory.setDetail("Screen name", new ICrashReportDetail<String>(){

                    @Override
                    public String call() throws Exception {
                        return Minecraft.screen.getClass().getCanonicalName();
                    }
                });
                crashreportcategory.setDetail("Mouse location", new ICrashReportDetail<String>(){

                    @Override
                    public String call() throws Exception {
                        return String.format("Scaled: (%d, %d). Absolute: (%d, %d)", k1, l1, Mouse.getX(), Mouse.getY());
                    }
                });
                crashreportcategory.setDetail("Screen size", new ICrashReportDetail<String>(){

                    @Override
                    public String call() throws Exception {
                        return String.format("Scaled: (%d, %d). Absolute: (%d, %d). Scale factor of %d", scaledresolution.getScaledWidth(), scaledresolution.getScaledHeight(), Minecraft.width, Minecraft.height, scaledresolution.getScaleFactor());
                    }
                });
                throw new ReportedException(crashreport);
            }
        }
        ScaledResolution sr = new ScaledResolution(mc);
        if (Minecraft.world == null)
            NotificationManager.renderNotification(sr);
        if (!(Minecraft.screen instanceof Gui)) {
            ClickGuiScreen.callback();
        }

        //new Blackmonster();

        //MNotify.render();
        boolean hud = MoonWare.featureManager.getFeatureByClass(HUD.class).getState();
        for (HudComponent h : HudManager.MODS.values()) {
            if (h instanceof TimerHudComponent) {
                h.setVisible(HUD.timerhud.get() && hud);
            }else if (h instanceof ArmorComponent) {
                h.setVisible(HUD.armor.get() && hud);
            }else if (h instanceof TargetHUDComponent) {
                h.setVisible(MoonWare.featureManager.getFeatureByClass(TargetHUD.class).getState());
            }else if (h instanceof PotionComponent) {
                h.setVisible(HUD.potion.get());
            }else if (h instanceof WaterMarkComponent) {
                h.setVisible(MoonWare.featureManager.getFeatureByClass(WaterMark.class).getState());
            }else if (h instanceof  RadarComponent) {
                h.setVisible(HUD.radar.get());
            }
        }
        frameFinish();
        waitForServerThread();
        Lagometer.updateLagometer();
        if (Minecraft.gameSettings.ofProfiler) {
            Minecraft.gameSettings.showDebugProfilerChart = true;
        }
    }

    public void func_191514_d(boolean p_191514_1_)
    {
        if (p_191514_1_)
        {
            GlStateManager.glFog(2918, setFogColorBuffer(0.0F, 0.0F, 0.0F, 1.0F));
        }
        else
        {
            GlStateManager.glFog(2918, setFogColorBuffer(fogColorRed, fogColorGreen, fogColorBlue, 1.0F));
        }
    }

    private void createWorldIcon() {
        if (Minecraft.renderGlobal.getRenderedChunks() > 10 && Minecraft.renderGlobal.hasNoChunkUpdates() && !Minecraft.getIntegratedServer().isWorldIconSet()) {
            BufferedImage bufferedimage = Screenshot.createScreenshot(Minecraft.width, Minecraft.height, Minecraft.getFramebuffer());
            int i = bufferedimage.getWidth();
            int j = bufferedimage.getHeight();
            int k = 0;
            int l = 0;
            if (i > j) {
                k = (i - j) / 2;
                i = j;
            } else {
                l = (j - i) / 2;
            }
            try {
                BufferedImage bufferedimage1 = new BufferedImage(64, 64, 1);
                Graphics2D graphics = bufferedimage1.createGraphics();
                graphics.drawImage(bufferedimage, 0, 0, 64, 64, k, l, k + i, l + i, null);
                graphics.dispose();
                ImageIO.write(bufferedimage1, "png", Minecraft.getIntegratedServer().getWorldIconFile());
            }
            catch (IOException ioexception1) {
                LOGGER.warn("Couldn't save auto screenshot", ioexception1);
            }
        }
    }

    public void renderStreamIndicator(float partialTicks) {
        setupOverlayRendering();
    }

    private boolean isDrawBlockOutline() {
        boolean flag;
        if (!drawBlockOutline) {
            return false;
        }
        Entity entity = Minecraft.getRenderViewEntity();
        boolean bl = flag = entity instanceof EntityPlayer && !Minecraft.gameSettings.hideGUI;
        if (flag && !((EntityPlayer)entity).capabilities.allowEdit) {
            ItemStack itemstack = ((EntityPlayer)entity).getHeldItemMainhand();
            if (Minecraft.objectMouseOver != null && Minecraft.objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK) {
                BlockPos blockpos = Minecraft.objectMouseOver.getBlockPos();
                IBlockState iblockstate = Minecraft.world.getBlockState(blockpos);
                Block block = iblockstate.getBlock();
                flag = Minecraft.playerController.getCurrentGameType() == GameType.SPECTATOR ? ReflectorForge.blockHasTileEntity(iblockstate) && Minecraft.world.getTileEntity(blockpos) instanceof IInventory : !itemstack.isEmpty() && (itemstack.canDestroy(block) || itemstack.canPlaceOn(block));
            }
        }
        return flag;
    }

    public void renderWorld(float partialTicks, long finishTimeNano) {
        updateLightmap(partialTicks);
        if (Minecraft.getRenderViewEntity() == null) {
            mc.setRenderViewEntity(Minecraft.player);
        }
        getMouseOver(partialTicks);
        if (Config.isShaders()) {
            Shaders.beginRender(mc, partialTicks, finishTimeNano);
        }
        GlStateManager.enableDepth();
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(516, 0.1f);
        Minecraft.profiler.startSection("center");
        if (Minecraft.gameSettings.anaglyph) {
            anaglyphField = 0;
            GlStateManager.colorMask(false, true, true, false);
            renderWorldPass(0, partialTicks, finishTimeNano);
            anaglyphField = 1;
            GlStateManager.colorMask(true, false, false, false);
            renderWorldPass(1, partialTicks, finishTimeNano);
            GlStateManager.colorMask(true, true, true, false);
        } else {
            renderWorldPass(2, partialTicks, finishTimeNano);
        }
        Minecraft.profiler.endSection();
    }

    private void renderWorldPass(int pass, float partialTicks, long finishTimeNano) {
        double d2;
        double d1;
        double d0;
        Entity entity;
        Frustum icamera;
        ParticleManager particlemanager;
        RenderGlobal renderglobal;
        boolean flag;
        block45: {
            block47: {
                EntityPlayer entityplayer;
                block46: {
                    flag = Config.isShaders();
                    if (flag) {
                        Shaders.beginRenderPass(pass, partialTicks, finishTimeNano);
                    }
                    renderglobal = Minecraft.renderGlobal;
                    particlemanager = Minecraft.effectRenderer;
                    boolean flag1 = isDrawBlockOutline();
                    GlStateManager.enableCull();
                    Minecraft.profiler.endStartSection("clear");
                    if (flag) {
                        Shaders.setViewport(0, 0, Minecraft.width, Minecraft.height);
                    } else {
                        GlStateManager.viewport(0, 0, Minecraft.width, Minecraft.height);
                    }
                    updateFogColor(partialTicks);
                    GlStateManager.clear(16640);
                    if (flag) {
                        Shaders.clearRenderBuffer();
                    }
                    Minecraft.profiler.endStartSection("camera");
                    setupCameraTransform(partialTicks, pass);
                    if (flag) {
                        Shaders.setCamera(partialTicks);
                    }
                    ActiveRenderInfo.updateRenderInfo(Minecraft.player, Minecraft.gameSettings.thirdPersonView == 2);
                    Minecraft.profiler.endStartSection("frustum");
                    ClippingHelper clippinghelper = ClippingHelperImpl.getInstance();
                    Minecraft.profiler.endStartSection("culling");
                    icamera = new Frustum(clippinghelper);
                    entity = Minecraft.getRenderViewEntity();
                    d0 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)partialTicks;
                    d1 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)partialTicks;
                    d2 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)partialTicks;
                    if (flag) {
                        ShadersRender.setFrustrumPosition(icamera, d0, d1, d2);
                    } else {
                        icamera.setPosition(d0, d1, d2);
                    }
                    if ((Config.isSkyEnabled() || Config.isSunMoonEnabled() || Config.isStarsEnabled()) && !Shaders.isShadowPass) {
                        setupFog(-1, partialTicks);
                        Minecraft.profiler.endStartSection("sky");
                        GlStateManager.matrixMode(5889);
                        GlStateManager.loadIdentity();
                        Project.gluPerspective(getFOVModifier(partialTicks, true), (float) Minecraft.width / (float) Minecraft.height, 0.05f, clipDistance);
                        GlStateManager.matrixMode(5888);
                        if (flag) {
                            Shaders.beginSky();
                        }
                        renderglobal.renderSky(partialTicks, pass);
                        if (flag) {
                            Shaders.endSky();
                        }
                        GlStateManager.matrixMode(5889);
                        GlStateManager.loadIdentity();
                        Project.gluPerspective(getFOVModifier(partialTicks, true), (float) Minecraft.width / (float) Minecraft.height, 0.05f, clipDistance);
                        GlStateManager.matrixMode(5888);
                    } else {
                        GlStateManager.disableBlend();
                    }
                    setupFog(0, partialTicks);
                    GlStateManager.shadeModel(7425);
                    if (entity.posY + (double)entity.getEyeHeight() < 128.0 + (double)(Minecraft.gameSettings.ofCloudsHeight * 128.0f)) {
                        renderCloudsCheck(renderglobal, partialTicks, pass, d0, d1, d2);
                    }
                    Minecraft.profiler.endStartSection("prepareterrain");
                    setupFog(0, partialTicks);
                    Minecraft.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
                    RenderHelper.disableStandardItemLighting();
                    Minecraft.profiler.endStartSection("terrain_setup");
                    checkLoadVisibleChunks(entity, partialTicks, icamera, Minecraft.player.isSpectator());
                    if (flag) {
                        ShadersRender.setupTerrain(renderglobal, entity, partialTicks, icamera, frameCount++, Minecraft.player.isSpectator());
                    } else {
                        renderglobal.setupTerrain(entity, partialTicks, icamera, frameCount++, Minecraft.player.isSpectator());
                    }
                    if (pass == 0 || pass == 2) {
                        Minecraft.profiler.endStartSection("updatechunks");
                        Lagometer.timerChunkUpload.start();
                        Minecraft.renderGlobal.updateChunks(finishTimeNano);
                        Lagometer.timerChunkUpload.end();
                    }
                    Minecraft.profiler.endStartSection("terrain");
                    Lagometer.timerTerrain.start();
                    if (Minecraft.gameSettings.ofSmoothFps && pass > 0) {
                        Minecraft.profiler.endStartSection("finish");
                        GL11.glFinish();
                        Minecraft.profiler.endStartSection("terrain");
                    }
                    GlStateManager.matrixMode(5888);
                    GlStateManager.pushMatrix();
                    GlStateManager.disableAlpha();
                    if (flag) {
                        ShadersRender.beginTerrainSolid();
                    }
                    renderglobal.renderBlockLayer(BlockRenderLayer.SOLID, partialTicks, pass, entity);
                    GlStateManager.enableAlpha();
                    if (flag) {
                        ShadersRender.beginTerrainCutoutMipped();
                    }
                    renderglobal.renderBlockLayer(BlockRenderLayer.CUTOUT_MIPPED, partialTicks, pass, entity);
                    Minecraft.getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
                    if (flag) {
                        ShadersRender.beginTerrainCutout();
                    }
                    renderglobal.renderBlockLayer(BlockRenderLayer.CUTOUT, partialTicks, pass, entity);
                    Minecraft.getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();
                    if (flag) {
                        ShadersRender.endTerrain();
                    }
                    Lagometer.timerTerrain.end();
                    GlStateManager.shadeModel(7424);
                    GlStateManager.alphaFunc(516, 0.1f);
                    if (!debugView) {
                        GlStateManager.matrixMode(5888);
                        GlStateManager.popMatrix();
                        GlStateManager.pushMatrix();
                        RenderHelper.enableStandardItemLighting();
                        Minecraft.profiler.endStartSection("entities");
                        if (Reflector.ForgeHooksClient_setRenderPass.exists()) {
                            Reflector.callVoid(Reflector.ForgeHooksClient_setRenderPass, 0);
                        }
                        renderglobal.renderEntities(entity, icamera, partialTicks);
                        if (Reflector.ForgeHooksClient_setRenderPass.exists()) {
                            Reflector.callVoid(Reflector.ForgeHooksClient_setRenderPass, -1);
                        }
                        RenderHelper.disableStandardItemLighting();
                        disableLightmap();
                    }
                    GlStateManager.matrixMode(5888);
                    GlStateManager.popMatrix();
                    if (!flag1 || Minecraft.objectMouseOver == null || entity.isInsideOfMaterial(Material.WATER)) break block45;
                    entityplayer = (EntityPlayer)entity;
                    GlStateManager.disableAlpha();
                    Minecraft.profiler.endStartSection("outline");
                    if (!Reflector.ForgeHooksClient_onDrawBlockHighlight.exists()) break block46;
                    if (Reflector.callBoolean(Reflector.ForgeHooksClient_onDrawBlockHighlight, renderglobal, entityplayer, Minecraft.objectMouseOver, 0, Float.valueOf(partialTicks))) break block47;
                }
                renderglobal.drawSelectionBox(entityplayer, Minecraft.objectMouseOver, 0, partialTicks);
            }
            GlStateManager.enableAlpha();
        }
        if (Minecraft.debugRenderer.shouldRender()) {
            boolean flag2 = GlStateManager.isFogEnabled();
            GlStateManager.disableFog();
            Minecraft.debugRenderer.renderDebug(partialTicks, finishTimeNano);
            GlStateManager.setFogEnabled(flag2);
        }
        if (!renderglobal.damagedBlocks.isEmpty()) {
            Minecraft.profiler.endStartSection("destroyProgress");
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            Minecraft.getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
            renderglobal.drawBlockDamageTexture(Tessellator.getInstance(), Tessellator.getInstance().getBuffer(), entity, partialTicks);
            Minecraft.getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();
            GlStateManager.disableBlend();
        }
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.disableBlend();
        if (!debugView) {
            enableLightmap();
            Minecraft.profiler.endStartSection("litParticles");
            if (flag) {
                Shaders.beginLitParticles();
            }
            particlemanager.renderLitParticles(entity, partialTicks);
            RenderHelper.disableStandardItemLighting();
            setupFog(0, partialTicks);
            Minecraft.profiler.endStartSection("particles");
            if (flag) {
                Shaders.beginParticles();
            }
            particlemanager.renderParticles(entity, partialTicks);
            if (flag) {
                Shaders.endParticles();
            }
            disableLightmap();
        }
        GlStateManager.depthMask(false);
        GlStateManager.enableCull();
        Minecraft.profiler.endStartSection("weather");
        if (flag) {
            Shaders.beginWeather();
        }
        renderRainSnow(partialTicks);
        if (flag) {
            Shaders.endWeather();
        }
        GlStateManager.depthMask(true);
        renderglobal.renderWorldBorder(entity, partialTicks);
        if (flag) {
            ShadersRender.renderHand0(this, partialTicks, pass);
            Shaders.preWater();
        }
        GlStateManager.disableBlend();
        GlStateManager.enableCull();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.alphaFunc(516, 0.1f);
        setupFog(0, partialTicks);
        GlStateManager.enableBlend();
        GlStateManager.depthMask(false);
        Minecraft.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        GlStateManager.shadeModel(7425);
        Minecraft.profiler.endStartSection("translucent");
        if (flag) {
            Shaders.beginWater();
        }
        renderglobal.renderBlockLayer(BlockRenderLayer.TRANSLUCENT, partialTicks, pass, entity);
        if (flag) {
            Shaders.endWater();
        }
        if (Reflector.ForgeHooksClient_setRenderPass.exists() && !debugView) {
            RenderHelper.enableStandardItemLighting();
            Minecraft.profiler.endStartSection("entities");
            Reflector.callVoid(Reflector.ForgeHooksClient_setRenderPass, 1);
            Minecraft.renderGlobal.renderEntities(entity, icamera, partialTicks);
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            Reflector.callVoid(Reflector.ForgeHooksClient_setRenderPass, -1);
            RenderHelper.disableStandardItemLighting();
        }
        GlStateManager.shadeModel(7424);
        GlStateManager.depthMask(true);
        GlStateManager.enableCull();
        GlStateManager.disableBlend();
        GlStateManager.disableFog();
        if (entity.posY + (double)entity.getEyeHeight() >= 128.0 + (double)(Minecraft.gameSettings.ofCloudsHeight * 128.0f)) {
            Minecraft.profiler.endStartSection("aboveClouds");
            renderCloudsCheck(renderglobal, partialTicks, pass, d0, d1, d2);
        }
        if (Reflector.ForgeHooksClient_dispatchRenderLast.exists()) {
            Minecraft.profiler.endStartSection("forge_render_last");
            Reflector.callVoid(Reflector.ForgeHooksClient_dispatchRenderLast, renderglobal, Float.valueOf(partialTicks));
        }
        EventRender3D event = new EventRender3D(partialTicks);
        EventManager.call(event);
        //EventManager.call(new EventPostRender3D());
        Minecraft.profiler.endStartSection("hand");
        EventManager.call(new WorldRenderEvent(partialTicks));
        if (renderHand && !Shaders.isShadowPass) {
            if (flag) {
                ShadersRender.renderHand1(this, partialTicks, pass);
                Shaders.renderCompositeFinal();
            }
            GlStateManager.clear(256);
            if (flag) {
                ShadersRender.renderFPOverlay(this, partialTicks, pass);
            } else {
                renderHand(partialTicks, pass);
            }
        }
        if (flag) {
            Shaders.endRender();
        }
    }

    private void renderCloudsCheck(RenderGlobal renderGlobalIn, float partialTicks, int pass, double p_180437_4_, double p_180437_6_, double p_180437_8_) {
        if (Minecraft.gameSettings.renderDistance >= 4 && !Config.isCloudsOff() && Shaders.shouldRenderClouds(Minecraft.gameSettings)) {
            Minecraft.profiler.endStartSection("clouds");
            GlStateManager.matrixMode(5889);
            GlStateManager.loadIdentity();
            Project.gluPerspective(getFOVModifier(partialTicks, true), (float) Minecraft.width / (float) Minecraft.height, 0.05f, clipDistance * 4.0f);
            GlStateManager.matrixMode(5888);
            GlStateManager.pushMatrix();
            setupFog(0, partialTicks);
            renderGlobalIn.renderClouds(partialTicks, pass, p_180437_4_, p_180437_6_, p_180437_8_);
            GlStateManager.disableFog();
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(5889);
            GlStateManager.loadIdentity();
            Project.gluPerspective(getFOVModifier(partialTicks, true), (float) Minecraft.width / (float) Minecraft.height, 0.05f, clipDistance);
            GlStateManager.matrixMode(5888);
        }
    }

    private void addRainParticles() {
        float f = Minecraft.world.getRainStrength(1.0f);
        if (!Config.isRainFancy()) {
            f /= 2.0f;
        }
        if (f != 0.0f && Config.isRainSplash()) {
            random.setSeed((long) rendererUpdateCount * 312987231L);
            Entity entity = Minecraft.getRenderViewEntity();
            ClientLevel world = Minecraft.world;
            BlockPos blockpos = new BlockPos(entity);
            int i = 10;
            double d0 = 0.0;
            double d1 = 0.0;
            double d2 = 0.0;
            int j = 0;
            int k = (int)(100.0f * f * f);
            if (Minecraft.gameSettings.particles == 1) {
                k >>= 1;
            } else if (Minecraft.gameSettings.particles == 2) {
                k = 0;
            }
            for (int l = 0; l < k; ++l) {
                BlockPos blockpos1 = world.getPrecipitationHeight(blockpos.add(random.nextInt(10) - random.nextInt(10), 0, random.nextInt(10) - random.nextInt(10)));
                Biome biome = world.getBiome(blockpos1);
                BlockPos blockpos2 = blockpos1.down();
                IBlockState iblockstate = world.getBlockState(blockpos2);
                if (blockpos1.getY() > blockpos.getY() + 10 || blockpos1.getY() < blockpos.getY() - 10 || !biome.canRain() || !(biome.getFloatTemperature(blockpos1) >= 0.15f)) continue;
                double d3 = random.nextDouble();
                double d4 = random.nextDouble();
                AxisAlignedBB axisalignedbb = iblockstate.getBoundingBox(world, blockpos2);
                if (iblockstate.getMaterial() != Material.LAVA && iblockstate.getBlock() != Blocks.MAGMA) {
                    if (iblockstate.getMaterial() == Material.AIR) continue;
                    if (random.nextInt(++j) == 0) {
                        d0 = (double)blockpos2.getX() + d3;
                        d1 = (double)((float)blockpos2.getY() + 0.1f) + axisalignedbb.maxY - 1.0;
                        d2 = (double)blockpos2.getZ() + d4;
                    }
                    Minecraft.world.spawnParticle(EnumParticleTypes.WATER_DROP, (double)blockpos2.getX() + d3, (double)((float)blockpos2.getY() + 0.1f) + axisalignedbb.maxY, (double)blockpos2.getZ() + d4, 0.0, 0.0, 0.0);
                    continue;
                }
                Minecraft.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, (double)blockpos1.getX() + d3, (double)((float)blockpos1.getY() + 0.1f) - axisalignedbb.minY, (double)blockpos1.getZ() + d4, 0.0, 0.0, 0.0);
            }
            if (j > 0 && random.nextInt(3) < rainSoundCounter++) {
                rainSoundCounter = 0;
                if (d1 > (double)(blockpos.getY() + 1) && world.getPrecipitationHeight(blockpos).getY() > MathHelper.floor(blockpos.getY())) {
                    Minecraft.world.playSound(d0, d1, d2, SoundEvents.WEATHER_RAIN_ABOVE, SoundCategory.WEATHER, 0.1f, 0.5f, false);
                } else {
                    Minecraft.world.playSound(d0, d1, d2, SoundEvents.WEATHER_RAIN, SoundCategory.WEATHER, 0.2f, 1.0f, false);
                }
            }
        }
    }

    protected void renderRainSnow(float partialTicks) {
        WorldProvider worldprovider;
        Object object;
        if (Reflector.ForgeWorldProvider_getWeatherRenderer.exists() && (object = Reflector.call(worldprovider = Minecraft.world.provider, Reflector.ForgeWorldProvider_getWeatherRenderer)) != null) {
            Reflector.callVoid(object, Reflector.IRenderHandler_render, Float.valueOf(partialTicks), Minecraft.world, mc);
            return;
        }
        if (MoonWare.featureManager.getFeatureByClass(WeatherColor.class).getState()) {
            int hexColor = WeatherColor.weatherColor.getColor();
            float red = (float)(hexColor >> 16 & 0xFF) / 255.0f;
            float green = (float)(hexColor >> 8 & 0xFF) / 255.0f;
            float blue = (float)(hexColor & 0xFF) / 255.0f;
            float alpha = (float)(hexColor >> 24 & 0xFF) / 255.0f;
            float f = alpha * 10.0f;
            if (f > 0.0f) {
                enableLightmap();
                Entity entity = Minecraft.getRenderViewEntity();
                ClientLevel world = Minecraft.world;
                int i = MathHelper.floor(entity.posX);
                int j = MathHelper.floor(entity.posY);
                int k = MathHelper.floor(entity.posZ);
                Tessellator tessellator = Tessellator.getInstance();
                BufferBuilder bufferbuilder = tessellator.getBuffer();
                GlStateManager.disableCull();
                GlStateManager.glNormal3f(0.0f, 1.0f, 0.0f);
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                GlStateManager.alphaFunc(516, 0.1f);
                double d0 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)partialTicks;
                double d1 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)partialTicks;
                double d2 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)partialTicks;
                int l = MathHelper.floor(d1);
                int i1 = 5;
                if (Minecraft.gameSettings.fancyGraphics) {
                    i1 = 10;
                }
                int j1 = -1;
                float f1 = (float) rendererUpdateCount + partialTicks;
                bufferbuilder.setTranslation(-d0, -d1, -d2);
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
                for (int k1 = k - i1; k1 <= k + i1; ++k1) {
                    for (int l1 = i - i1; l1 <= i + i1; ++l1) {
                        boolean check;
                        int i2 = (k1 - k + 16) * 32 + l1 - i + 16;
                        double d3 = (double) rainXCoords[i2] * 0.5;
                        double d4 = (double) rainYCoords[i2] * 0.5;
                        blockpos$mutableblockpos.setPos(l1, 0, k1);
                        Biome biome = world.getBiome(blockpos$mutableblockpos);
                        boolean bl = MoonWare.featureManager.getFeatureByClass(WeatherColor.class).getState() || (check = biome.canRain() || biome.getEnableSnow());
                        //if (bl) continue;
                        int j2 = world.getPrecipitationHeight(blockpos$mutableblockpos).getY();
                        int k2 = j - i1;
                        int l2 = j + i1;
                        if (k2 < j2) {
                            k2 = j2;
                        }
                        if (l2 < j2) {
                            l2 = j2;
                        }
                        int i3 = j2;
                        if (j2 < l) {
                            i3 = l;
                        }
                        if (k2 == l2) continue;
                        random.setSeed(l1 * l1 * 3121 + l1 * 45238971 ^ k1 * k1 * 418711 + k1 * 13761);
                        blockpos$mutableblockpos.setPos(l1, k2, k1);
                        float f2 = biome.getFloatTemperature(blockpos$mutableblockpos);
                        if (j1 != 1) {
                            if (j1 >= 0) {
                                tessellator.draw();
                            }
                            j1 = 1;
                            Minecraft.getTextureManager().bindTexture(SNOW_TEXTURES);
                            bufferbuilder.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
                        }
                        double d8 = -((float)(rendererUpdateCount & 0x1FF) + partialTicks) / 512.0f;
                        double d9 = random.nextDouble() + (double)f1 * 0.01 * (double)((float) random.nextGaussian());
                        double d10 = random.nextDouble() + (double)(f1 * (float) random.nextGaussian()) * 0.001;
                        double d11 = (double)((float)l1 + 0.5f) - entity.posX;
                        double d12 = (double)((float)k1 + 0.5f) - entity.posZ;
                        float f6 = MathHelper.sqrt(d11 * d11 + d12 * d12) / (float)i1;
                        float f5 = ((1.0f - f6 * f6) * 0.3f + 0.5f) * f;
                        blockpos$mutableblockpos.setPos(l1, i3, k1);
                        int i4 = 0xF000F0;
                        int j4 = i4 >> 16 & 0xFFFF;
                        int k4 = i4 & 0xFFFF;
                        bufferbuilder.pos((double)l1 - d3 + 0.5, l2, (double)k1 - d4 + 0.5).tex(0.0 + d9, (double)k2 * 0.25 + d8 + d10).color(red, green, blue, f5).lightmap(j4, k4).endVertex();
                        bufferbuilder.pos((double)l1 + d3 + 0.5, l2, (double)k1 + d4 + 0.5).tex(1.0 + d9, (double)k2 * 0.25 + d8 + d10).color(red, green, blue, f5).lightmap(j4, k4).endVertex();
                        bufferbuilder.pos((double)l1 + d3 + 0.5, k2, (double)k1 + d4 + 0.5).tex(1.0 + d9, (double)l2 * 0.25 + d8 + d10).color(red, green, blue, f5).lightmap(j4, k4).endVertex();
                        bufferbuilder.pos((double)l1 - d3 + 0.5, k2, (double)k1 - d4 + 0.5).tex(0.0 + d9, (double)l2 * 0.25 + d8 + d10).color(red, green, blue, f5).lightmap(j4, k4).endVertex();
                    }
                }
                if (j1 >= 0) {
                    tessellator.draw();
                }
                bufferbuilder.setTranslation(0.0, 0.0, 0.0);
                GlStateManager.enableCull();
                GlStateManager.disableBlend();
                GlStateManager.alphaFunc(516, 0.1f);
                disableLightmap();
            }
        } else {
            float f5 = Minecraft.world.getRainStrength(partialTicks);
            if (f5 > 0.0f) {
                if (Config.isRainOff()) {
                    return;
                }
                enableLightmap();
                Entity entity = Minecraft.getRenderViewEntity();
                ClientLevel world = Minecraft.world;
                int i = MathHelper.floor(entity.posX);
                int j = MathHelper.floor(entity.posY);
                int k = MathHelper.floor(entity.posZ);
                Tessellator tessellator = Tessellator.getInstance();
                BufferBuilder bufferbuilder = tessellator.getBuffer();
                GlStateManager.disableCull();
                GlStateManager.glNormal3f(0.0f, 1.0f, 0.0f);
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                GlStateManager.alphaFunc(516, 0.1f);
                double d0 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)partialTicks;
                double d1 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)partialTicks;
                double d2 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)partialTicks;
                int l = MathHelper.floor(d1);
                int i1 = 5;
                if (Config.isRainFancy()) {
                    i1 = 10;
                }
                int j1 = -1;
                float f = (float) rendererUpdateCount + partialTicks;
                bufferbuilder.setTranslation(-d0, -d1, -d2);
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
                for (int k1 = k - i1; k1 <= k + i1; ++k1) {
                    for (int l1 = i - i1; l1 <= i + i1; ++l1) {
                        int i2 = (k1 - k + 16) * 32 + l1 - i + 16;
                        double d3 = (double) rainXCoords[i2] * 0.5;
                        double d4 = (double) rainYCoords[i2] * 0.5;
                        blockpos$mutableblockpos.setPos(l1, 0, k1);
                        Biome biome = world.getBiome(blockpos$mutableblockpos);
                        if (!biome.canRain() && !biome.getEnableSnow()) continue;
                        int j2 = world.getPrecipitationHeight(blockpos$mutableblockpos).getY();
                        int k2 = j - i1;
                        int l2 = j + i1;
                        if (k2 < j2) {
                            k2 = j2;
                        }
                        if (l2 < j2) {
                            l2 = j2;
                        }
                        int i3 = j2;
                        if (j2 < l) {
                            i3 = l;
                        }
                        if (k2 == l2) continue;
                        random.setSeed(l1 * l1 * 3121 + l1 * 45238971 ^ k1 * k1 * 418711 + k1 * 13761);
                        blockpos$mutableblockpos.setPos(l1, k2, k1);
                        float f1 = biome.getFloatTemperature(blockpos$mutableblockpos);
                        if (world.getBiomeProvider().getTemperatureAtHeight(f1, j2) >= 0.15f) {
                            if (j1 != 0) {
                                if (j1 >= 0) {
                                    tessellator.draw();
                                }
                                j1 = 0;
                                Minecraft.getTextureManager().bindTexture(RAIN_TEXTURES);
                                bufferbuilder.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
                            }
                            double d5 = -((double)(rendererUpdateCount + l1 * l1 * 3121 + l1 * 45238971 + k1 * k1 * 418711 + k1 * 13761 & 0x1F) + (double)partialTicks) / 32.0 * (3.0 + random.nextDouble());
                            double d6 = (double)((float)l1 + 0.5f) - entity.posX;
                            double d7 = (double)((float)k1 + 0.5f) - entity.posZ;
                            float f2 = MathHelper.sqrt(d6 * d6 + d7 * d7) / (float)i1;
                            float f3 = ((1.0f - f2 * f2) * 0.5f + 0.5f) * f5;
                            blockpos$mutableblockpos.setPos(l1, i3, k1);
                            int j3 = ((World)world).getCombinedLight(blockpos$mutableblockpos, 0);
                            int k3 = j3 >> 16 & 0xFFFF;
                            int l3 = j3 & 0xFFFF;
                            bufferbuilder.pos((double)l1 - d3 + 0.5, l2, (double)k1 - d4 + 0.5).tex(0.0, (double)k2 * 0.25 + d5).color(1.0f, 1.0f, 1.0f, f3).lightmap(k3, l3).endVertex();
                            bufferbuilder.pos((double)l1 + d3 + 0.5, l2, (double)k1 + d4 + 0.5).tex(1.0, (double)k2 * 0.25 + d5).color(1.0f, 1.0f, 1.0f, f3).lightmap(k3, l3).endVertex();
                            bufferbuilder.pos((double)l1 + d3 + 0.5, k2, (double)k1 + d4 + 0.5).tex(1.0, (double)l2 * 0.25 + d5).color(1.0f, 1.0f, 1.0f, f3).lightmap(k3, l3).endVertex();
                            bufferbuilder.pos((double)l1 - d3 + 0.5, k2, (double)k1 - d4 + 0.5).tex(0.0, (double)l2 * 0.25 + d5).color(1.0f, 1.0f, 1.0f, f3).lightmap(k3, l3).endVertex();
                            continue;
                        }
                        if (j1 != 1) {
                            if (j1 >= 0) {
                                tessellator.draw();
                            }
                            j1 = 1;
                            Minecraft.getTextureManager().bindTexture(SNOW_TEXTURES);
                            bufferbuilder.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
                        }
                        double d8 = -((float)(rendererUpdateCount & 0x1FF) + partialTicks) / 512.0f;
                        double d9 = random.nextDouble() + (double)f * 0.01 * (double)((float) random.nextGaussian());
                        double d10 = random.nextDouble() + (double)(f * (float) random.nextGaussian()) * 0.001;
                        double d11 = (double)((float)l1 + 0.5f) - entity.posX;
                        double d12 = (double)((float)k1 + 0.5f) - entity.posZ;
                        float f6 = MathHelper.sqrt(d11 * d11 + d12 * d12) / (float)i1;
                        float f4 = ((1.0f - f6 * f6) * 0.3f + 0.5f) * f5;
                        blockpos$mutableblockpos.setPos(l1, i3, k1);
                        int i4 = (((World)world).getCombinedLight(blockpos$mutableblockpos, 0) * 3 + 0xF000F0) / 4;
                        int j4 = i4 >> 16 & 0xFFFF;
                        int k4 = i4 & 0xFFFF;
                        bufferbuilder.pos((double)l1 - d3 + 0.5, l2, (double)k1 - d4 + 0.5).tex(0.0 + d9, (double)k2 * 0.25 + d8 + d10).color(1.0f, 1.0f, 1.0f, f4).lightmap(j4, k4).endVertex();
                        bufferbuilder.pos((double)l1 + d3 + 0.5, l2, (double)k1 + d4 + 0.5).tex(1.0 + d9, (double)k2 * 0.25 + d8 + d10).color(1.0f, 1.0f, 1.0f, f4).lightmap(j4, k4).endVertex();
                        bufferbuilder.pos((double)l1 + d3 + 0.5, k2, (double)k1 + d4 + 0.5).tex(1.0 + d9, (double)l2 * 0.25 + d8 + d10).color(1.0f, 1.0f, 1.0f, f4).lightmap(j4, k4).endVertex();
                        bufferbuilder.pos((double)l1 - d3 + 0.5, k2, (double)k1 - d4 + 0.5).tex(0.0 + d9, (double)l2 * 0.25 + d8 + d10).color(1.0f, 1.0f, 1.0f, f4).lightmap(j4, k4).endVertex();
                    }
                }
                if (j1 >= 0) {
                    tessellator.draw();
                }
                bufferbuilder.setTranslation(0.0, 0.0, 0.0);
                GlStateManager.enableCull();
                GlStateManager.disableBlend();
                GlStateManager.alphaFunc(516, 0.1f);
                disableLightmap();
            }
        }
    }

    public void setupOverlayRendering() {
        ScaledResolution scaledresolution = new ScaledResolution(mc);
        GlStateManager.clear(256);
        GlStateManager.matrixMode(5889);
        GlStateManager.loadIdentity();
        GlStateManager.ortho(0.0, scaledresolution.getScaledWidth(), scaledresolution.getScaledHeight(), 0.0, 1000.0, 3000.0);
        GlStateManager.matrixMode(5888);
        GlStateManager.loadIdentity();
        GlStateManager.translate(0.0f, 0.0f, -2000.0f);
    }

    private void updateFogColor(float partialTicks)
    {
        World world = this.mc.world;
        Entity entity = this.mc.getRenderViewEntity();
        float f = 0.25F + 0.75F * (float)this.mc.gameSettings.renderDistance / 32.0F;
        f = 1.0F - (float)Math.pow((double)f, 0.25D);
        Vec3d vec3d = world.getSkyColor(this.mc.getRenderViewEntity(), partialTicks);
        vec3d = CustomColors.getWorldSkyColor(vec3d, world, this.mc.getRenderViewEntity(), partialTicks);
        float f1 = (float)vec3d.xCoord;
        float f2 = (float)vec3d.yCoord;
        float f3 = (float)vec3d.zCoord;
        Vec3d vec3d1 = world.getFogColor(partialTicks);
        vec3d1 = CustomColors.getWorldFogColor(vec3d1, world, this.mc.getRenderViewEntity(), partialTicks);
//        FogColorsEvent eventFogColor = new FogColorsEvent();
//        eventFogColor.call();
                vec3d1 = CustomColors.getWorldFogColor(vec3d1, world, Minecraft.getRenderViewEntity(), partialTicks);
        fogColorRed = (float)vec3d1.x;
        fogColorGreen = (float)vec3d1.y;
        fogColorBlue = (float)vec3d1.z;
        if (this.mc.gameSettings.renderDistance >= 4)
        {
            double d0 = MathHelper.sin(world.getCelestialAngleRadians(partialTicks)) > 0.0F ? -1.0D : 1.0D;
            Vec3d vec3d2 = new Vec3d(d0, 0.0D, 0.0D);
            float f5 = (float)entity.getLook(partialTicks).dotProduct(vec3d2);

            if (f5 < 0.0F)
            {
                f5 = 0.0F;
            }

            if (f5 > 0.0F)
            {
                float[] afloat = world.provider.calcSunriseSunsetColors(world.getCelestialAngle(partialTicks), partialTicks);

                if (afloat != null)
                {
                    f5 = f5 * afloat[3];
                    this.fogColorRed = this.fogColorRed * (1.0F - f5) + afloat[0] * f5;
                    this.fogColorGreen = this.fogColorGreen * (1.0F - f5) + afloat[1] * f5;
                    this.fogColorBlue = this.fogColorBlue * (1.0F - f5) + afloat[2] * f5;
                }
            }
        }

        this.fogColorRed += (f1 - this.fogColorRed) * f;
        this.fogColorGreen += (f2 - this.fogColorGreen) * f;
        this.fogColorBlue += (f3 - this.fogColorBlue) * f;
        float f8 = world.getRainStrength(partialTicks);

        if (f8 > 0.0F)
        {
            float f4 = 1.0F - f8 * 0.5F;
            float f10 = 1.0F - f8 * 0.4F;
            this.fogColorRed *= f4;
            this.fogColorGreen *= f4;
            this.fogColorBlue *= f10;
        }

        float f9 = world.getThunderStrength(partialTicks);

        if (f9 > 0.0F)
        {
            float f11 = 1.0F - f9 * 0.5F;
            this.fogColorRed *= f11;
            this.fogColorGreen *= f11;
            this.fogColorBlue *= f11;
        }

        IBlockState iblockstate1 = ActiveRenderInfo.getBlockStateAtEntityViewpoint(this.mc.world, entity, partialTicks);

        if (this.cloudFog)
        {
            Vec3d vec3d4 = world.getCloudColour(partialTicks);
            this.fogColorRed = (float)vec3d4.xCoord;
            this.fogColorGreen = (float)vec3d4.yCoord;
            this.fogColorBlue = (float)vec3d4.zCoord;
        }
        else if (Reflector.ForgeBlock_getFogColor.exists())
        {
            Vec3d vec3d5 = ActiveRenderInfo.projectViewFromEntity(entity, (double)partialTicks);
            BlockPos blockpos = new BlockPos(vec3d5);
            IBlockState iblockstate = this.mc.world.getBlockState(blockpos);
            Vec3d vec3d3 = (Vec3d)Reflector.call(iblockstate.getBlock(), Reflector.ForgeBlock_getFogColor, this.mc.world, blockpos, iblockstate, entity, new Vec3d((double)this.fogColorRed, (double)this.fogColorGreen, (double)this.fogColorBlue), partialTicks);
            this.fogColorRed = (float)vec3d3.xCoord;
            this.fogColorGreen = (float)vec3d3.yCoord;
            this.fogColorBlue = (float)vec3d3.zCoord;
        }
        else if (iblockstate1.getMaterial() == Material.WATER)
        {
            float f12 = 0.0F;

            if (entity instanceof EntityLivingBase)
            {
                f12 = (float)EnchantmentHelper.getRespirationModifier((EntityLivingBase)entity) * 0.2F;

                if (((EntityLivingBase)entity).isPotionActive(MobEffects.WATER_BREATHING))
                {
                    f12 = f12 * 0.3F + 0.6F;
                }
            }

            this.fogColorRed = 0.02F + f12;
            this.fogColorGreen = 0.02F + f12;
            this.fogColorBlue = 0.2F + f12;
            Vec3d vec3d7 = CustomColors.getUnderwaterColor(this.mc.world, this.mc.getRenderViewEntity().posX, this.mc.getRenderViewEntity().posY + 1.0D, this.mc.getRenderViewEntity().posZ);

            if (vec3d7 != null)
            {
                this.fogColorRed = (float)vec3d7.xCoord;
                this.fogColorGreen = (float)vec3d7.yCoord;
                this.fogColorBlue = (float)vec3d7.zCoord;
            }
        }
        else if (iblockstate1.getMaterial() == Material.LAVA)
        {
            this.fogColorRed = 0.6F;
            this.fogColorGreen = 0.1F;
            this.fogColorBlue = 0.0F;
            Vec3d vec3d6 = CustomColors.getUnderlavaColor(this.mc.world, this.mc.getRenderViewEntity().posX, this.mc.getRenderViewEntity().posY + 1.0D, this.mc.getRenderViewEntity().posZ);

            if (vec3d6 != null)
            {
                this.fogColorRed = (float)vec3d6.xCoord;
                this.fogColorGreen = (float)vec3d6.yCoord;
                this.fogColorBlue = (float)vec3d6.zCoord;
            }
        }

        float f13 = this.fogColor2 + (this.fogColor1 - this.fogColor2) * partialTicks;
        this.fogColorRed *= f13;
        this.fogColorGreen *= f13;
        this.fogColorBlue *= f13;
        double d1 = (entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)partialTicks) * world.provider.getVoidFogYFactor();

        if (entity instanceof EntityLivingBase && ((EntityLivingBase)entity).isPotionActive(MobEffects.BLINDNESS))
        {
            int i = ((EntityLivingBase)entity).getActivePotionEffect(MobEffects.BLINDNESS).getDuration();

            if (i < 20)
            {
                d1 *= (double)(1.0F - (float)i / 20.0F);
            }
            else
            {
                d1 = 0.0D;
            }
        }

        if (d1 < 1.0D)
        {
            if (d1 < 0.0D)
            {
                d1 = 0.0D;
            }

            d1 = d1 * d1;
            this.fogColorRed = (float)((double)this.fogColorRed * d1);
            this.fogColorGreen = (float)((double)this.fogColorGreen * d1);
            this.fogColorBlue = (float)((double)this.fogColorBlue * d1);
        }

        if (this.bossColorModifier > 0.0F)
        {
            float f14 = this.bossColorModifierPrev + (this.bossColorModifier - this.bossColorModifierPrev) * partialTicks;
            this.fogColorRed = this.fogColorRed * (1.0F - f14) + this.fogColorRed * 0.7F * f14;
            this.fogColorGreen = this.fogColorGreen * (1.0F - f14) + this.fogColorGreen * 0.6F * f14;
            this.fogColorBlue = this.fogColorBlue * (1.0F - f14) + this.fogColorBlue * 0.6F * f14;
        }

        if (entity instanceof EntityLivingBase && ((EntityLivingBase)entity).isPotionActive(MobEffects.NIGHT_VISION))
        {
            float f15 = this.getNightVisionBrightness((EntityLivingBase)entity, partialTicks);
            float f6 = 1.0F / this.fogColorRed;

            if (f6 > 1.0F / this.fogColorGreen)
            {
                f6 = 1.0F / this.fogColorGreen;
            }

            if (f6 > 1.0F / this.fogColorBlue)
            {
                f6 = 1.0F / this.fogColorBlue;
            }

            this.fogColorRed = this.fogColorRed * (1.0F - f15) + this.fogColorRed * f6 * f15;
            this.fogColorGreen = this.fogColorGreen * (1.0F - f15) + this.fogColorGreen * f6 * f15;
            this.fogColorBlue = this.fogColorBlue * (1.0F - f15) + this.fogColorBlue * f6 * f15;
        }

        if (this.mc.gameSettings.anaglyph)
        {
            float f16 = (this.fogColorRed * 30.0F + this.fogColorGreen * 59.0F + this.fogColorBlue * 11.0F) / 100.0F;
            float f17 = (this.fogColorRed * 30.0F + this.fogColorGreen * 70.0F) / 100.0F;
            float f7 = (this.fogColorRed * 30.0F + this.fogColorBlue * 70.0F) / 100.0F;
            this.fogColorRed = f16;
            this.fogColorGreen = f17;
            this.fogColorBlue = f7;
        }

        if (Reflector.EntityViewRenderEvent_FogColors_Constructor.exists())
        {
            Object object = Reflector.newInstance(Reflector.EntityViewRenderEvent_FogColors_Constructor, this, entity, iblockstate1, partialTicks, this.fogColorRed, this.fogColorGreen, this.fogColorBlue);
            Reflector.postForgeBusEvent(object);
            this.fogColorRed = Reflector.callFloat(object, Reflector.EntityViewRenderEvent_FogColors_getRed);
            this.fogColorGreen = Reflector.callFloat(object, Reflector.EntityViewRenderEvent_FogColors_getGreen);
            this.fogColorBlue = Reflector.callFloat(object, Reflector.EntityViewRenderEvent_FogColors_getBlue);
        }

        Shaders.setClearColor(this.fogColorRed, this.fogColorGreen, this.fogColorBlue, 0.0F);
    }

    private void setupFog(int startCoords, float partialTicks) {
        if (MoonWare.featureManager.getFeatureByClass(NoRender.class).getState() && NoRender.fog.getCurrentValue()) {
            return;
        }
        fogStandard = false;
        Entity entity = Minecraft.getRenderViewEntity();
        setupFogColor(false);
        GlStateManager.glNormal3f(0.0f, -1.0f, 0.0f);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        IBlockState iblockstate = ActiveRenderInfo.getBlockStateAtEntityViewpoint(Minecraft.world, entity, partialTicks);
        float f = -1.0f;
        if (Reflector.ForgeHooksClient_getFogDensity.exists()) {
            f = Reflector.callFloat(Reflector.ForgeHooksClient_getFogDensity, this, entity, iblockstate, Float.valueOf(partialTicks), Float.valueOf(0.1f));
        }
        if (f >= 0.0f) {
            GlStateManager.setFogDensity(f);
        } else if (entity instanceof EntityLivingBase && ((EntityLivingBase)entity).isPotionActive(MobEffects.BLINDNESS)) {
            float f2 = 5.0f;
            int i = ((EntityLivingBase)entity).getActivePotionEffect(MobEffects.BLINDNESS).getDuration();
            if (i < 20) {
                f2 = 5.0f + (farPlaneDistance - 5.0f) * (1.0f - (float)i / 20.0f);
            }
            if (Config.isShaders()) {
                Shaders.setFog(GlStateManager.FogMode.LINEAR);
            } else {
                GlStateManager.setFog(GlStateManager.FogMode.LINEAR);
            }
            if (startCoords == -1) {
                GlStateManager.setFogStart(0.0f);
                GlStateManager.setFogEnd(f2 * 0.8f);
            } else {
                GlStateManager.setFogStart(f2 * 0.25f);
                GlStateManager.setFogEnd(f2);
            }
            if (GLContext.getCapabilities().GL_NV_fog_distance && Config.isFogFancy()) {
                GlStateManager.glFogi(34138, 34139);
            }
        } else if (cloudFog) {
            if (Config.isShaders()) {
                Shaders.setFog(GlStateManager.FogMode.EXP);
            } else {
                GlStateManager.setFog(GlStateManager.FogMode.EXP);
            }
            GlStateManager.setFogDensity(0.1f);
        } else if (iblockstate.getMaterial() == Material.WATER) {
            if (Config.isShaders()) {
                Shaders.setFog(GlStateManager.FogMode.EXP);
            } else {
                GlStateManager.setFog(GlStateManager.FogMode.EXP);
            }
            if (entity instanceof EntityLivingBase) {
                if (((EntityLivingBase)entity).isPotionActive(MobEffects.WATER_BREATHING)) {
                    GlStateManager.setFogDensity(0.01f);
                } else {
                    GlStateManager.setFogDensity(0.1f - (float)EnchantmentHelper.getRespirationModifier((EntityLivingBase)entity) * 0.03f);
                }
            } else {
                GlStateManager.setFogDensity(0.1f);
            }
            if (Config.isClearWater()) {
                GlStateManager.setFogDensity(0.02f);
            }
        } else if (iblockstate.getMaterial() == Material.LAVA) {
            if (Config.isShaders()) {
                Shaders.setFog(GlStateManager.FogMode.EXP);
            } else {
                GlStateManager.setFog(GlStateManager.FogMode.EXP);
            }
            GlStateManager.setFogDensity(2.0f);
        } else {
            float value = MoonWare.featureManager.getFeatureByClass(FogColor.class).getState() ? FogColor.distance.getCurrentValue() * 50.0f : 0.0f;
            float f1 = farPlaneDistance - value;
            fogStandard = true;
            if (Config.isShaders()) {
                Shaders.setFog(GlStateManager.FogMode.LINEAR);
            } else {
                GlStateManager.setFog(GlStateManager.FogMode.LINEAR);
            }
            if (startCoords == -1) {
                GlStateManager.setFogStart(0.0f);
                GlStateManager.setFogEnd(f1);
            } else {
                GlStateManager.setFogStart(f1 * Config.getFogStart());
                GlStateManager.setFogEnd(f1);
            }
            if (GLContext.getCapabilities().GL_NV_fog_distance) {
                if (Config.isFogFancy()) {
                    GlStateManager.glFogi(34138, 34139);
                }
                if (Config.isFogFast()) {
                    GlStateManager.glFogi(34138, 34140);
                }
            }
            if (Minecraft.world.provider.doesXZShowFog((int)entity.posX, (int)entity.posZ) || Minecraft.ingameGUI.getBossbarHud().shouldCreateFog()) {
                GlStateManager.setFogStart(f1 * 0.05f);
                GlStateManager.setFogEnd(f1);
            }
            if (Reflector.ForgeHooksClient_onFogRender.exists()) {
                Reflector.callVoid(Reflector.ForgeHooksClient_onFogRender, this, entity, iblockstate, Float.valueOf(partialTicks), startCoords, Float.valueOf(f1));
            }
        }
        EventFogColor event = new EventFogColor(fogColorRed, fogColorGreen, fogColorBlue, 0);
        EventManager.call(event);
        fogColorRed = event.red / 255.0f;
        fogColorGreen = event.green / 255.0f;
        fogColorBlue = event.blue / 255.0f;
        GlStateManager.enableColorMaterial();
        GlStateManager.enableFog();
        GlStateManager.colorMaterial(1028, 4608);
    }

    public void setupFogColor(boolean p_191514_1_) {
        if (p_191514_1_) {
            GlStateManager.glFog(2918, setFogColorBuffer(0.0f, 0.0f, 0.0f, 1.0f));
        } else {
            GlStateManager.glFog(2918, setFogColorBuffer(fogColorRed, fogColorGreen, fogColorBlue, 1.0f));
        }
    }

    private FloatBuffer setFogColorBuffer(float red, float green, float blue, float alpha) {
        if (Config.isShaders()) {
            Shaders.setFogColor(red, green, blue);
        }
        fogColorBuffer.clear();
        fogColorBuffer.put(red).put(green).put(blue).put(alpha);
        fogColorBuffer.flip();
        return fogColorBuffer;
    }

    public void func_190564_k() {
        field_190566_ab = null;
        theMapItemRenderer.clearLoadedMaps();
    }

    public MapItemRenderer getMapItemRenderer() {
        return theMapItemRenderer;
    }

    private void waitForServerThread() {
        serverWaitTimeCurrent = 0;
        if (Config.isSmoothWorld() && Config.isSingleProcessor()) {
            IntegratedServer integratedserver;
            if (Minecraft.isIntegratedServerRunning() && (integratedserver = Minecraft.getIntegratedServer()) != null) {
                boolean flag = mc.isGamePaused();
                if (!flag && !(Minecraft.screen instanceof LevelLoadingScreen)) {
                    if (serverWaitTime > 0) {
                        Lagometer.timerServer.start();
                        Config.sleep(serverWaitTime);
                        Lagometer.timerServer.end();
                        serverWaitTimeCurrent = serverWaitTime;
                    }
                    long i = System.nanoTime() / 1000000L;
                    if (lastServerTime != 0L && lastServerTicks != 0) {
                        long j = i - lastServerTime;
                        if (j < 0L) {
                            lastServerTime = i;
                            j = 0L;
                        }
                        if (j >= 50L) {
                            lastServerTime = i;
                            int k = integratedserver.getTickCounter();
                            int l = k - lastServerTicks;
                            if (l < 0) {
                                lastServerTicks = k;
                                l = 0;
                            }
                            if (l < 1 && serverWaitTime < 100) {
                                serverWaitTime += 2;
                            }
                            if (l > 1 && serverWaitTime > 0) {
                                --serverWaitTime;
                            }
                            lastServerTicks = k;
                        }
                    } else {
                        lastServerTime = i;
                        lastServerTicks = integratedserver.getTickCounter();
                        avgServerTickDiff = 1.0f;
                        avgServerTimeDiff = 50.0f;
                    }
                } else {
                    if (Minecraft.screen instanceof LevelLoadingScreen) {
                        Config.sleep(20L);
                    }
                    lastServerTime = 0L;
                    lastServerTicks = 0;
                }
            }
        } else {
            lastServerTime = 0L;
            lastServerTicks = 0;
        }
    }

    private void frameInit() {
        if (!initialized) {
            TextureUtils.registerResourceListener();
            initialized = true;
        }
        Config.checkDisplayMode();
        ClientLevel world = Minecraft.world;
        if (Minecraft.screen instanceof TitleScreen) {
            updateMainMenu((TitleScreen) Minecraft.screen);
        }
        if (updatedWorld != world) {
            RandomMobs.worldChanged(updatedWorld, world);
            Config.updateThreadPriorities();
            lastServerTime = 0L;
            lastServerTicks = 0;
            updatedWorld = world;
        }
        if (!setFxaaShader(Shaders.configAntialiasingLevel)) {
            Shaders.configAntialiasingLevel = 0;
        }
    }

    private void frameFinish() {
        long i;
        if (Minecraft.world != null && (i = System.currentTimeMillis()) > lastErrorCheckTimeMs + 10000L) {
            lastErrorCheckTimeMs = i;
            int j = GlStateManager.glGetError();
            if (j != 0) {
                String s = GLU.gluErrorString(j);
                TextComponent textcomponentstring = new TextComponent(I18n.format("of.message.openglError", j, s));
                Minecraft.ingameGUI.getChatGUI().printChatMessage(textcomponentstring);
            }
        }
    }

    private void updateMainMenu(TitleScreen p_updateMainMenu_1_) {
        try {
            String s = null;
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            int i = calendar.get(5);
            int j = calendar.get(2) + 1;
            if (i == 8 && j == 4) {
                s = "Happy birthday, OptiFine!";
            }
            if (i == 14 && j == 8) {
                s = "Happy birthday, sp614x!";
            }
            if (s == null) {
                return;
            }
            Reflector.setFieldValue(p_updateMainMenu_1_, Reflector.GuiMainMenu_splashText, s);
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    public boolean setFxaaShader(int p_setFxaaShader_1_) {
        if (!OpenGlHelper.isFramebufferEnabled()) {
            return false;
        }
        if (theShaderGroup != null && theShaderGroup != fxaaShaders[2] && theShaderGroup != fxaaShaders[4]) {
            return true;
        }
        if (p_setFxaaShader_1_ != 2 && p_setFxaaShader_1_ != 4) {
            if (theShaderGroup == null) {
                return true;
            }
            theShaderGroup.deleteShaderGroup();
            theShaderGroup = null;
            return true;
        }
        if (theShaderGroup != null && theShaderGroup == fxaaShaders[p_setFxaaShader_1_]) {
            return true;
        }
        if (Minecraft.world == null) {
            return true;
        }
        loadShader(new Namespaced("shaders/post/fxaa_of_" + p_setFxaaShader_1_ + "x.json"));
        fxaaShaders[p_setFxaaShader_1_] = theShaderGroup;
        return useShader;
    }

    private void checkLoadVisibleChunks(Entity p_checkLoadVisibleChunks_1_, float p_checkLoadVisibleChunks_2_, ICamera p_checkLoadVisibleChunks_3_, boolean p_checkLoadVisibleChunks_4_) {
        int i = 201435902;
        if (loadVisibleChunks) {
            loadVisibleChunks = false;
            loadAllVisibleChunks(p_checkLoadVisibleChunks_1_, p_checkLoadVisibleChunks_2_, p_checkLoadVisibleChunks_3_, p_checkLoadVisibleChunks_4_);
            Minecraft.ingameGUI.getChatGUI().deleteChatLine(i);
        }
        if (Keyboard.isKeyDown(61) && Keyboard.isKeyDown(38)) {
            if (Minecraft.gameSettings.field_194146_ao.getKeyCode() == 38) {
                if (Minecraft.screen instanceof GuiScreenAdvancements) {
                    Minecraft.openScreen(null);
                }
                while (Keyboard.next()) {
                }
            }
            if (Minecraft.screen != null) {
                return;
            }
            loadVisibleChunks = true;
            TextComponent textcomponentstring = new TextComponent(I18n.format("of.message.loadingVisibleChunks"));
            Minecraft.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(textcomponentstring, i);
            Minecraft.actionKeyF3 = true;
        }
    }

    private void loadAllVisibleChunks(Entity p_loadAllVisibleChunks_1_, double p_loadAllVisibleChunks_2_, ICamera p_loadAllVisibleChunks_4_, boolean p_loadAllVisibleChunks_5_) {
        RenderGlobal renderglobal = Config.getRenderGlobal();
        int i = renderglobal.getCountLoadedChunks();
        long j = System.currentTimeMillis();
        Config.dbg("Loading visible chunks");
        long k = System.currentTimeMillis() + 5000L;
        int l = 0;
        boolean flag = false;
        do {
            flag = false;
            for (int i1 = 0; i1 < 100; ++i1) {
                renderglobal.displayListEntitiesDirty = true;
                renderglobal.setupTerrain(p_loadAllVisibleChunks_1_, p_loadAllVisibleChunks_2_, p_loadAllVisibleChunks_4_, frameCount++, p_loadAllVisibleChunks_5_);
                if (!renderglobal.hasNoChunkUpdates()) {
                    flag = true;
                }
                l += renderglobal.getCountChunksToUpdate();
                renderglobal.updateChunks(System.nanoTime() + 1000000000L);
                l -= renderglobal.getCountChunksToUpdate();
            }
            if (renderglobal.getCountLoadedChunks() != i) {
                flag = true;
                i = renderglobal.getCountLoadedChunks();
            }
            if (System.currentTimeMillis() <= k) continue;
            Config.log("Chunks loaded: " + l);
            k = System.currentTimeMillis() + 5000L;
        } while (flag);
        Config.log("Chunks loaded: " + l);
        Config.log("Finished loading visible chunks");
        RenderChunk.renderChunksUpdated = 0;
    }

    public static void drawNameplate(Font fontIn, String str, float x, float y, float z, int verticalShift, float viewerYaw, float viewerPitch, boolean isThirdPersonFrontal, boolean isSneaking) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        GlStateManager.glNormal3f(0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(-viewerYaw, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate((float)(isThirdPersonFrontal ? -1 : 1) * viewerPitch, 1.0f, 0.0f, 0.0f);
        GlStateManager.scale(-0.025f, -0.025f, 0.025f);
        GlStateManager.disableLighting();
        GlStateManager.depthMask(false);
        if (!isSneaking) {
            GlStateManager.disableDepth();
        }
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        int i = fontIn.getStringWidth(str) / 2;
        GlStateManager.disableTexture2D();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(-i - 1, -1 + verticalShift, 0.0).color(0.0f, 0.0f, 0.0f, 0.25f).endVertex();
        bufferbuilder.pos(-i - 1, 8 + verticalShift, 0.0).color(0.0f, 0.0f, 0.0f, 0.25f).endVertex();
        bufferbuilder.pos(i + 1, 8 + verticalShift, 0.0).color(0.0f, 0.0f, 0.0f, 0.25f).endVertex();
        bufferbuilder.pos(i + 1, -1 + verticalShift, 0.0).color(0.0f, 0.0f, 0.0f, 0.25f).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        if (!isSneaking) {
            fontIn.drawString(str, -fontIn.getStringWidth(str) / 2, verticalShift, 0x20FFFFFF);
            GlStateManager.enableDepth();
        }
        GlStateManager.depthMask(true);
        fontIn.drawString(str, -fontIn.getStringWidth(str) / 2, verticalShift, isSneaking ? 0x20FFFFFF : -1);
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.popMatrix();
    }

    public void func_190565_a(ItemStack p_190565_1_) {
        if (MoonWare.featureManager.getFeatureByClass(NoRender.class).getState() && NoRender.totem.getCurrentValue()) {
            return;
        }
        field_190566_ab = p_190565_1_;
        field_190567_ac = 40;
        field_190568_ad = random.nextFloat() * 2.0f - 1.0f;
        field_190569_ae = random.nextFloat() * 2.0f - 1.0f;
    }

    private void func_190563_a(int p_190563_1_, int p_190563_2_, float p_190563_3_) {
        if (field_190566_ab != null && field_190567_ac > 0) {
            int i = 40 - field_190567_ac;
            float f = ((float)i + p_190563_3_) / 40.0f;
            float f1 = f * f;
            float f2 = f * f1;
            float f3 = 10.25f * f2 * f1 + -24.95f * f1 * f1 + 25.5f * f2 + -13.8f * f1 + 4.0f * f;
            float f4 = f3 * (float)Math.PI;
            float f5 = field_190568_ad * (float)(p_190563_1_ / 4);
            float f6 = field_190569_ae * (float)(p_190563_2_ / 4);
            GlStateManager.enableAlpha();
            GlStateManager.pushMatrix();
            GlStateManager.pushAttrib();
            GlStateManager.enableDepth();
            GlStateManager.disableCull();
            RenderHelper.enableStandardItemLighting();
            GlStateManager.translate((float)(p_190563_1_ / 2) + f5 * MathHelper.abs(MathHelper.sin(f4 * 2.0f)), (float)(p_190563_2_ / 2) + f6 * MathHelper.abs(MathHelper.sin(f4 * 2.0f)), -50.0f);
            float f7 = 50.0f + 175.0f * MathHelper.sin(f4);
            GlStateManager.scale(f7, -f7, f7);
            GlStateManager.rotate(900.0f * MathHelper.abs(MathHelper.sin(f4)), 0.0f, 1.0f, 0.0f);
            GlStateManager.rotate(6.0f * MathHelper.cos(f * 8.0f), 1.0f, 0.0f, 0.0f);
            GlStateManager.rotate(6.0f * MathHelper.cos(f * 8.0f), 0.0f, 0.0f, 1.0f);
            Minecraft.getRenderItem().renderItem(field_190566_ab, ItemCameraTransforms.TransformType.FIXED);
            GlStateManager.popAttrib();
            GlStateManager.popMatrix();
            RenderHelper.disableStandardItemLighting();
            GlStateManager.enableCull();
            GlStateManager.disableDepth();
        }
    }

    static {
        SHADERS_TEXTURES = new Namespaced[]{new Namespaced("shaders/post/notch.json"), new Namespaced("shaders/post/fxaa.json"), new Namespaced("shaders/post/art.json"), new Namespaced("shaders/post/bumpy.json"), new Namespaced("shaders/post/blobs2.json"), new Namespaced("shaders/post/pencil.json"), new Namespaced("shaders/post/color_convolve.json"), new Namespaced("shaders/post/deconverge.json"), new Namespaced("shaders/post/flip.json"), new Namespaced("shaders/post/invert.json"), new Namespaced("shaders/post/ntsc.json"), new Namespaced("shaders/post/outline.json"), new Namespaced("shaders/post/phosphor.json"), new Namespaced("shaders/post/scan_pincushion.json"), new Namespaced("shaders/post/sobel.json"), new Namespaced("shaders/post/bits.json"), new Namespaced("shaders/post/desaturate.json"), new Namespaced("shaders/post/green.json"), new Namespaced("shaders/post/blur.json"), new Namespaced("shaders/post/wobble.json"), new Namespaced("shaders/post/blobs.json"), new Namespaced("shaders/post/antialias.json"), new Namespaced("shaders/post/creeper.json"), new Namespaced("shaders/post/spider.json")};
        SHADER_COUNT = SHADERS_TEXTURES.length;
    }
}
