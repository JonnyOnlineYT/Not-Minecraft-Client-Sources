package net.minecraft.client.renderer.entity;

import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.Formatting;
import net.optifine.entity.model.CustomEntityModels;
import optifine.Config;
import optifine.Reflector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;
import org.moonware.client.MoonWare;
import org.moonware.client.event.EventManager;
import org.moonware.client.event.events.impl.render.EventRenderPlayerName;
import org.moonware.client.feature.impl.visual.Chams2;
import org.moonware.client.feature.impl.visual.HitColor;
import org.moonware.client.helpers.Helper;
import org.moonware.client.helpers.misc.ClientHelper;
import org.moonware.client.helpers.render.RenderHelper;
import shadersmod.client.Shaders;

import java.nio.FloatBuffer;
import java.util.List;

public abstract class RenderLivingBase<T extends EntityLivingBase> extends Render<T> implements Helper {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final DynamicTexture TEXTURE_BRIGHTNESS = new DynamicTexture(16, 16);
    public ModelBase mainModel;
    protected FloatBuffer brightnessBuffer = GLAllocation.createDirectFloatBuffer(4);
    protected List<LayerRenderer<T>> layerRenderers = Lists.newArrayList();
    protected boolean renderMarker;
    public static float NAME_TAG_RANGE = 64.0F;
    public static float NAME_TAG_RANGE_SNEAK = 32.0F;
    public float renderLimbSwing;
    public float renderLimbSwingAmount;
    public float renderAgeInTicks;
    public float renderHeadYaw;
    public float renderHeadPitch;
    public float renderScaleFactor;
    public static final boolean animateModelLiving = Boolean.getBoolean("animate.model.living");

    public RenderLivingBase(RenderManager renderManagerIn, ModelBase modelBaseIn, float shadowSizeIn) {
        super(renderManagerIn);
        mainModel = modelBaseIn;
        shadowSize = shadowSizeIn;
    }

    public <V extends EntityLivingBase, U extends LayerRenderer<V>> boolean addLayer(U layer) {
        return layerRenderers.add((LayerRenderer<T>) layer);
    }

    public ModelBase getMainModel() {
        return mainModel;
    }

    /**
     * Returns a rotation angle that is inbetween two other rotation angles. par1 and par2 are the angles between which
     * to interpolate, par3 is probably a float between 0.0 and 1.0 that tells us where "between" the two angles we are.
     * Example: par1 = 30, par2 = 50, par3 = 0.5, then return = 40
     */
    protected float interpolateRotation(float prevYawOffset, float yawOffset, float partialTicks) {
        float f;

        for (f = yawOffset - prevYawOffset; f < -180.0F; f += 360.0F) {
        }

        while (f >= 180.0F) {
            f -= 360.0F;
        }

        return prevYawOffset + partialTicks * f;
    }

    public void transformHeldFull3DItemLayer() {
    }

    /**
     * Renders the desired {@code T} type Entity.
     */
    public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks) {
        if (!Reflector.RenderLivingEvent_Pre_Constructor.exists() || !Reflector.postForgeBusEvent(Reflector.RenderLivingEvent_Pre_Constructor, entity, this, partialTicks, x, y, z)) {
            if (animateModelLiving) {
                entity.limbSwingAmount = 1.0F;
            }

            GlStateManager.pushMatrix();
            GlStateManager.disableCull();
            mainModel.swingProgress = getSwingProgress(entity, partialTicks);
            mainModel.isRiding = entity.isRiding();

            if (Reflector.ForgeEntity_shouldRiderSit.exists()) {
                mainModel.isRiding = entity.isRiding() && entity.getRidingEntity() != null && Reflector.callBoolean(entity.getRidingEntity(), Reflector.ForgeEntity_shouldRiderSit);
            }

            mainModel.isChild = entity.isChild();

            try {
                float f = interpolateRotation(entity.prevRenderYawOffset, entity.renderYawOffset, partialTicks);
                float f1 = interpolateRotation(entity.prevRotationYawHead, entity.rotationYawHead, partialTicks);
                float f2 = f1 - f;

                if (mainModel.isRiding && entity.getRidingEntity() instanceof EntityLivingBase) {
                    EntityLivingBase entitylivingbase = (EntityLivingBase) entity.getRidingEntity();
                    f = interpolateRotation(entitylivingbase.prevRenderYawOffset, entitylivingbase.renderYawOffset, partialTicks);
                    f2 = f1 - f;
                    float f3 = MathHelper.wrapDegrees(f2);

                    if (f3 < -85.0F) {
                        f3 = -85.0F;
                    }

                    if (f3 >= 85.0F) {
                        f3 = 85.0F;
                    }

                    f = f1 - f3;

                    if (f3 * f3 > 2500.0F) {
                        f += f3 * 0.2F;
                    }

                    f2 = f1 - f;
                }

                float f7;
                if (entity == renderManager.renderViewEntity) {
                    f7 = entity.prevRotationPitchHead + (entity.rotationPitchHead - entity.prevRotationPitchHead) * partialTicks;
                } else {
                    f7 = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;
                }
                renderLivingAt(entity, x, y, z);
                float f8 = handleRotationFloat(entity, partialTicks);
                rotateCorpse(entity, f8, f, partialTicks);
                float f4 = prepareScale(entity, partialTicks);
                float f5 = 0.0F;
                float f6 = 0.0F;

                if (!entity.isRiding()) {
                    f5 = entity.prevLimbSwingAmount + (entity.limbSwingAmount - entity.prevLimbSwingAmount) * partialTicks;
                    f6 = entity.limbSwing - entity.limbSwingAmount * (1.0F - partialTicks);

                    if (entity.isChild()) {
                        f6 *= 3.0F;
                    }

                    if (f5 > 1.0F) {
                        f5 = 1.0F;
                    }
                }

                GlStateManager.enableAlpha();
                mainModel.setLivingAnimations(entity, f6, f5, partialTicks);
                mainModel.setRotationAngles(f6, f5, f8, f2, f7, f4, entity);

                if (CustomEntityModels.isActive()) {
                    renderLimbSwing = f6;
                    renderLimbSwingAmount = f5;
                    renderAgeInTicks = f8;
                    renderHeadYaw = f2;
                    renderHeadPitch = f7;
                    renderScaleFactor = f4;
                }

                if (renderOutlines) {
                    boolean flag1 = setScoreTeamColor(entity);
                    GlStateManager.enableColorMaterial();
                    GlStateManager.enableOutlineMode(getTeamColor(entity));

                    if (!renderMarker) {
                        renderModel(entity, f6, f5, f8, f2, f7, f4);
                    }

                    if (!(entity instanceof EntityPlayer) || !((EntityPlayer) entity).isSpectator()) {
                        renderLayers(entity, f6, f5, partialTicks, f8, f2, f7, f4);
                    }

                    GlStateManager.disableOutlineMode();
                    GlStateManager.disableColorMaterial();

                    if (flag1) {
                        unsetScoreTeamColor();
                    }
                } else {
                    boolean flag = setDoRenderBrightness(entity, partialTicks);
                    renderModel(entity, f6, f5, f8, f2, f7, f4);

                    if (flag) {
                        unsetBrightness();
                    }

                    GlStateManager.depthMask(true);

                    if (!(entity instanceof EntityPlayer) || !((EntityPlayer) entity).isSpectator()) {
                        renderLayers(entity, f6, f5, partialTicks, f8, f2, f7, f4);
                    }
                }

                GlStateManager.disableRescaleNormal();
            } catch (Exception exception1) {
                LOGGER.error("Couldn't render entity", exception1);
            }

            GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
            GlStateManager.enableTexture2D();
            GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
            GlStateManager.enableCull();
            GlStateManager.popMatrix();
            super.doRender(entity, x, y, z, entityYaw, partialTicks);

            if (Reflector.RenderLivingEvent_Post_Constructor.exists()) {
                Reflector.postForgeBusEvent(Reflector.RenderLivingEvent_Post_Constructor, entity, this, partialTicks, x, y, z);
            }
        }
    }

    public float prepareScale(T entitylivingbaseIn, float partialTicks) {
        GlStateManager.enableRescaleNormal();
        GlStateManager.scale(-1.0F, -1.0F, 1.0F);
        preRenderCallback(entitylivingbaseIn, partialTicks);
        float f = 0.0625F;
        GlStateManager.translate(0.0F, -1.501F, 0.0F);
        return 0.0625F;
    }

    protected boolean setScoreTeamColor(T entityLivingBaseIn) {
        GlStateManager.disableLighting();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        return true;
    }

    protected void unsetScoreTeamColor() {
        GlStateManager.enableLighting();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.enableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }

    /**
     * Renders the model in RenderLiving
     */
    protected void renderModel(T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor) {

        boolean flag = func_193115_c(entitylivingbaseIn);
        boolean flag1 = !flag && !entitylivingbaseIn.isInvisibleToPlayer(Minecraft.player);
        String mode = Chams2.chamsMode.getCurrentMode();
        boolean chamsCheck = (MoonWare.featureManager.getFeatureByClass(Chams2.class).getState() && entitylivingbaseIn instanceof EntityPlayer);

        if (flag || flag1) {
            if (!bindEntityTexture(entitylivingbaseIn))
                return;
            if (flag1) {
                GlStateManager.enableBlendProfile(GlStateManager.Profile.TRANSPARENT_MODEL);
            }
            if (chamsCheck && mode.equalsIgnoreCase("Fill")) {
                GL11.glPushMatrix();
                GlStateManager.disableBlend();
                GL11.glEnable(GL11.GL_POLYGON_OFFSET_LINE);
                GL11.glPolygonOffset(1.0F, 1000000.0F);
                OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                if (!Chams2.clientColor.getBoolValue()) {
                    RenderHelper.setColor(Chams2.colorChams.getColorValue());
                } else {
                    RenderHelper.setColor(ClientHelper.getClientColor().getRGB());
                }
                GL11.glDisable(GL11.GL_DEPTH_TEST);
                GL11.glDepthMask(false);
            }

            mainModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);

            if (chamsCheck && mode.equalsIgnoreCase("Fill")) {
                GlStateManager.disableBlend();
                GL11.glEnable(GL11.GL_DEPTH_TEST);
                GL11.glDepthMask(true);
                if (!Chams2.clientColor.getBoolValue()) {
                    RenderHelper.setColor(Chams2.colorChams.getColorValue());
                } else {
                    RenderHelper.setColor(ClientHelper.getClientColor().getRGB());
                }
                mainModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
                GL11.glEnable(GL11.GL_TEXTURE_2D);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glEnable(GL11.GL_LIGHTING);
                GL11.glPolygonOffset(1.0F, -1000000.0F);
                GL11.glDisable(GL11.GL_POLYGON_OFFSET_LINE);
                GL11.glPopMatrix();
            }

            if (chamsCheck && mode.equalsIgnoreCase("Outline")) {
                GL11.glPushMatrix();
                GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
                GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glDisable(GL11.GL_DEPTH_TEST);
                GL11.glEnable(GL11.GL_LINE_SMOOTH);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                if (!Chams2.clientColor.getBoolValue()) {
                    RenderHelper.setColor(Chams2.colorChams.getColorValue());
                } else {
                    RenderHelper.setColor(ClientHelper.getClientColor().getRGB());
                }
                GL11.glLineWidth(2);
                mainModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
                GL11.glPopAttrib();
                GL11.glPopMatrix();
            }

            if (flag1) {
                GlStateManager.disableBlendProfile(GlStateManager.Profile.TRANSPARENT_MODEL);
            }
        }
    }

    protected boolean func_193115_c(T p_193115_1_) {
        return !p_193115_1_.isInvisible() || renderOutlines;
    }

    protected boolean setDoRenderBrightness(T entityLivingBaseIn, float partialTicks) {
        return setBrightness(entityLivingBaseIn, partialTicks, true);
    }

    protected boolean setBrightness(T entitylivingbaseIn, float partialTicks, boolean combineTextures) {
        float f = entitylivingbaseIn.getBrightness();
        int i = getColorMultiplier(entitylivingbaseIn, f, partialTicks);
        boolean flag = (i >> 24 & 255) > 0;
        boolean flag1 = entitylivingbaseIn.hurtTime > 0 || entitylivingbaseIn.deathTime > 0;

        if (!flag && !flag1) {
            return false;
        } else if (!flag && !combineTextures) {
            return false;
        } else {
            GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
            GlStateManager.enableTexture2D();
            GlStateManager.glTexEnvi(8960, 8704, OpenGlHelper.GL_COMBINE);
            GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_RGB, 8448);
            GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_RGB, OpenGlHelper.defaultTexUnit);
            GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE1_RGB, OpenGlHelper.GL_PRIMARY_COLOR);
            GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_RGB, 768);
            GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND1_RGB, 768);
            GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_ALPHA, 7681);
            GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_ALPHA, OpenGlHelper.defaultTexUnit);
            GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_ALPHA, 770);
            GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
            GlStateManager.enableTexture2D();
            GlStateManager.glTexEnvi(8960, 8704, OpenGlHelper.GL_COMBINE);
            GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_RGB, OpenGlHelper.GL_INTERPOLATE);
            GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_RGB, OpenGlHelper.GL_CONSTANT);
            GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE1_RGB, OpenGlHelper.GL_PREVIOUS);
            GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE2_RGB, OpenGlHelper.GL_CONSTANT);
            GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_RGB, 768);
            GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND1_RGB, 768);
            GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND2_RGB, 770);
            GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_ALPHA, 7681);
            GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_ALPHA, OpenGlHelper.GL_PREVIOUS);
            GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_ALPHA, 770);
            brightnessBuffer.position(0);

            if (flag1) {
                if (MoonWare.featureManager.getFeatureByClass(HitColor.class).getState()) {
                    int color = HitColor.hitColor.getColorValue();
                    float alpha = (float) (color >> 24 & 255) / 255.0F;
                    float red = (float) (color >> 16 & 255) / 255.0F;
                    float green = (float) (color >> 8 & 255) / 255.0F;
                    float blue = (float) (color & 255) / 255.0F;
                    brightnessBuffer.put(red);
                    brightnessBuffer.put(green);
                    brightnessBuffer.put(blue);
                    brightnessBuffer.put(alpha);

                    if (Config.isShaders()) {
                        Shaders.setEntityColor(red, green, blue, alpha);
                    }
                } else {
                    brightnessBuffer.put(1.0F);
                    brightnessBuffer.put(0.0F);
                    brightnessBuffer.put(0.0F);
                    brightnessBuffer.put(0.3F);

                    if (Config.isShaders()) {
                        Shaders.setEntityColor(1F, 0.0F, 0.0F, 0.3F);
                    }
                }

            } else {
                float f1 = (float) (i >> 24 & 255) / 255.0F;
                float f2 = (float) (i >> 16 & 255) / 255.0F;
                float f3 = (float) (i >> 8 & 255) / 255.0F;
                float f4 = (float) (i & 255) / 255.0F;
                brightnessBuffer.put(f2);
                brightnessBuffer.put(f3);
                brightnessBuffer.put(f4);
                brightnessBuffer.put(1.0F - f1);

                if (Config.isShaders()) {
                    Shaders.setEntityColor(f2, f3, f4, 1.0F - f1);
                }
            }

            brightnessBuffer.flip();
            GlStateManager.glTexEnv(8960, 8705, brightnessBuffer);
            GlStateManager.setActiveTexture(OpenGlHelper.GL_TEXTURE2);
            GlStateManager.enableTexture2D();
            GlStateManager.bindTexture(TEXTURE_BRIGHTNESS.getGlTextureId());
            GlStateManager.glTexEnvi(8960, 8704, OpenGlHelper.GL_COMBINE);
            GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_RGB, 8448);
            GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_RGB, OpenGlHelper.GL_PREVIOUS);
            GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE1_RGB, OpenGlHelper.lightmapTexUnit);
            GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_RGB, 768);
            GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND1_RGB, 768);
            GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_ALPHA, 7681);
            GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_ALPHA, OpenGlHelper.GL_PREVIOUS);
            GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_ALPHA, 770);
            GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
            return true;
        }
    }

    protected void unsetBrightness() {
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        GlStateManager.enableTexture2D();
        GlStateManager.glTexEnvi(8960, 8704, OpenGlHelper.GL_COMBINE);
        GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_RGB, 8448);
        GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_RGB, OpenGlHelper.defaultTexUnit);
        GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE1_RGB, OpenGlHelper.GL_PRIMARY_COLOR);
        GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_RGB, 768);
        GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND1_RGB, 768);
        GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_ALPHA, 8448);
        GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_ALPHA, OpenGlHelper.defaultTexUnit);
        GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE1_ALPHA, OpenGlHelper.GL_PRIMARY_COLOR);
        GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_ALPHA, 770);
        GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND1_ALPHA, 770);
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.glTexEnvi(8960, 8704, OpenGlHelper.GL_COMBINE);
        GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_RGB, 8448);
        GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_RGB, 768);
        GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND1_RGB, 768);
        GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_RGB, 5890);
        GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE1_RGB, OpenGlHelper.GL_PREVIOUS);
        GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_ALPHA, 8448);
        GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_ALPHA, 770);
        GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_ALPHA, 5890);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.setActiveTexture(OpenGlHelper.GL_TEXTURE2);
        GlStateManager.disableTexture2D();
        GlStateManager.bindTexture(0);
        GlStateManager.glTexEnvi(8960, 8704, OpenGlHelper.GL_COMBINE);
        GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_RGB, 8448);
        GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_RGB, 768);
        GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND1_RGB, 768);
        GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_RGB, 5890);
        GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE1_RGB, OpenGlHelper.GL_PREVIOUS);
        GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_ALPHA, 8448);
        GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_ALPHA, 770);
        GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_ALPHA, 5890);
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);

        if (Config.isShaders()) {
            Shaders.setEntityColor(0.0F, 0.0F, 0.0F, 0.0F);
        }
    }

    /**
     * Sets a simple glTranslate on a LivingEntity.
     */
    protected void renderLivingAt(T entityLivingBaseIn, double x, double y, double z) {
        GlStateManager.translate((float) x, (float) y, (float) z);
    }

    protected void rotateCorpse(T entityLiving, float p_77043_2_, float p_77043_3_, float partialTicks) {
        GlStateManager.rotate(180.0F - p_77043_3_, 0.0F, 1.0F, 0.0F);

        if (entityLiving.deathTime > 0) {
            float f = ((float) entityLiving.deathTime + partialTicks - 1.0F) / 20.0F * 1.6F;
            f = MathHelper.sqrt(f);

            if (f > 1.0F) {
                f = 1.0F;
            }

            GlStateManager.rotate(f * getDeathMaxRotation(entityLiving), 0.0F, 0.0F, 1.0F);
        } else {
            String s = Formatting.strip(entityLiving.getName());

            if (s != null && ("Dinnerbone".equals(s) || "Grumm".equals(s)) && (!(entityLiving instanceof EntityPlayer) || ((EntityPlayer) entityLiving).isWearing(EnumPlayerModelParts.CAPE))) {
                GlStateManager.translate(0.0F, entityLiving.height + 0.1F, 0.0F);
                GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
            }
        }
    }

    /**
     * Returns where in the swing animation the living entity is (from 0 to 1).  Args : entity, partialTickTime
     */
    protected float getSwingProgress(T livingBase, float partialTickTime) {
        return livingBase.getSwingProgress(partialTickTime);
    }

    /**
     * Defines what float the third param in setRotationAngles of ModelBase is
     */
    protected float handleRotationFloat(T livingBase, float partialTicks) {
        return (float) livingBase.ticksExisted + partialTicks;
    }

    protected void renderLayers(T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scaleIn) {
        for (LayerRenderer<T> layerrenderer : layerRenderers) {
            boolean flag = setBrightness(entitylivingbaseIn, partialTicks, layerrenderer.shouldCombineTextures());
            layerrenderer.doRenderLayer(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scaleIn);

            if (flag) {
                unsetBrightness();
            }
        }
    }

    protected float getDeathMaxRotation(T entityLivingBaseIn) {
        return 90.0F;
    }

    /**
     * Gets an RGBA int color multiplier to apply.
     */
    protected int getColorMultiplier(T entitylivingbaseIn, float lightBrightness, float partialTickTime) {
        return 0;
    }

    /**
     * Allows the render to do state modifications necessary before the model is rendered.
     */
    protected void preRenderCallback(T entitylivingbaseIn, float partialTickTime) {
    }

    public void renderName(T entity, double x, double y, double z) {
        if (!Reflector.RenderLivingEvent_Specials_Pre_Constructor.exists() || !Reflector.postForgeBusEvent(Reflector.RenderLivingEvent_Specials_Pre_Constructor, entity, this, x, y, z)) {
            if (canRenderName(entity)) {
                double d0 = entity.getDistanceSqToEntity(renderManager.renderViewEntity);
                float f = entity.isSneaking() ? NAME_TAG_RANGE_SNEAK : NAME_TAG_RANGE;

                if (d0 < (double) (f * f)) {
                    EventRenderPlayerName eventNameTag = new EventRenderPlayerName();
                    EventManager.call(eventNameTag);

                    if (eventNameTag.isCancelled())
                        return;

                    String s = entity.getDisplayName().asFormattedString();
                    GlStateManager.alphaFunc(516, 0.1F);
                    renderEntityName(entity, x, y, z, s, d0);
                }
            }

            if (Reflector.RenderLivingEvent_Specials_Post_Constructor.exists()) {
                Reflector.postForgeBusEvent(Reflector.RenderLivingEvent_Specials_Post_Constructor, entity, this, x, y, z);
            }
        }
    }

    protected boolean canRenderName(T entity) {
        EntityPlayerSP entityplayersp = Minecraft.player;
        boolean flag = !entity.isInvisibleToPlayer(entityplayersp);

        if (entity != entityplayersp) {
            Team team = entity.getTeam();
            Team team1 = entityplayersp.getTeam();

            if (team != null) {
                Team.EnumVisible team$enumvisible = team.getNameTagVisibility();

                switch (team$enumvisible) {
                    case ALWAYS:
                        return flag;

                    case NEVER:
                        return false;

                    case HIDE_FOR_OTHER_TEAMS:
                        return team1 == null ? flag : team.isSameTeam(team1) && (team.getSeeFriendlyInvisiblesEnabled() || flag);

                    case HIDE_FOR_OWN_TEAM:
                        return team1 == null ? flag : !team.isSameTeam(team1) && flag;

                    default:
                        return true;
                }
            }
        }

        return Minecraft.isGuiEnabled() && entity != renderManager.renderViewEntity && flag && !entity.isBeingRidden();
    }

    public List<LayerRenderer<T>> getLayerRenderers() {
        return layerRenderers;
    }

    static {
        int[] aint = TEXTURE_BRIGHTNESS.getTextureData();

        for (int i = 0; i < 256; ++i) {
            aint[i] = -1;
        }

        TEXTURE_BRIGHTNESS.updateDynamicTexture();
    }
}
