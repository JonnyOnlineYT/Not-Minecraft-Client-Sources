package net.minecraft.client.renderer;

import com.google.common.base.MoreObjects;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.storage.MapData;
import optifine.Config;
import optifine.DynamicLights;
import optifine.Reflector;
import optifine.ReflectorForge;
import org.lwjgl.opengl.GL11;
import org.moonware.client.MoonWare;
import org.moonware.client.event.EventManager;
import org.moonware.client.event.events.impl.player.EventTransformSideFirstPerson;
import org.moonware.client.feature.impl.combat.KillAura;
import org.moonware.client.feature.impl.visual.Animations;
import org.moonware.client.feature.impl.visual.Chams2;
import org.moonware.client.feature.impl.visual.NoRender;
import org.moonware.client.helpers.misc.ClientHelper;
import org.moonware.client.helpers.render.AnimationHelper;
import org.moonware.client.helpers.render2.RenderHelper2;
import shadersmod.client.Shaders;

import java.util.Objects;

public class ItemRenderer {
    private static final Namespaced RES_MAP_BACKGROUND = new Namespaced("textures/map/map_background.png");
    private static final Namespaced RES_UNDERWATER_OVERLAY = new Namespaced("textures/misc/underwater.png");

    /**
     * A reference to the Minecraft object.
     */
    private final Minecraft mc;
    private ItemStack itemStackMainHand = ItemStack.EMPTY;
    private ItemStack itemStackOffHand = ItemStack.EMPTY;
    private float equippedProgressMainHand;
    private float prevEquippedProgressMainHand;
    private float equippedProgressOffHand;
    private float prevEquippedProgressOffHand;
    private final RenderManager renderManager;
    private final RenderItem itemRenderer;

    public ItemRenderer(Minecraft mcIn) {
        mc = mcIn;
        renderManager = Minecraft.getRenderManager();
        itemRenderer = Minecraft.getRenderItem();
    }

    public void renderItem(EntityLivingBase entityIn, ItemStack heldStack, ItemCameraTransforms.TransformType transform) {
        renderItemSide(entityIn, heldStack, transform, false);
    }

    public void renderItemSide(EntityLivingBase entitylivingbaseIn, ItemStack heldStack, ItemCameraTransforms.TransformType transform, boolean leftHanded) {
        if (!heldStack.isEmpty()) {
            Item item = heldStack.getItem();
            Block block = Block.getBlockFromItem(item);
            GlStateManager.pushMatrix();
            boolean flag = itemRenderer.shouldRenderItemIn3D(heldStack) && block.getBlockLayer() == BlockRenderLayer.TRANSLUCENT;

            if (flag && (!Config.isShaders() || !Shaders.renderItemKeepDepthMask)) {
                GlStateManager.depthMask(false);
            }

            itemRenderer.renderItem(heldStack, entitylivingbaseIn, transform, leftHanded);

            if (flag) {
                GlStateManager.depthMask(true);
            }

            GlStateManager.popMatrix();
        }
    }

    /**
     * Rotate the render around X and Y
     */
    private void rotateArroundXAndY(float angle, float angleY) {
        GlStateManager.pushMatrix();
        GlStateManager.rotate(angle, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(angleY, 0.0F, 1.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.popMatrix();
    }

    private void setLightmap() {
        AbstractClientPlayer abstractclientplayer = Minecraft.player;
        int i = Minecraft.world.getCombinedLight(new BlockPos(abstractclientplayer.posX, abstractclientplayer.posY + (double) abstractclientplayer.getEyeHeight(), abstractclientplayer.posZ), 0);

        if (Config.isDynamicLights()) {
            i = DynamicLights.getCombinedLight(Minecraft.getRenderViewEntity(), i);
        }

        float f = (float) (i & 65535);
        float f1 = (float) (i >> 16);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, f, f1);
    }

    private void rotateArm(float p_187458_1_) {
        EntityPlayerSP entityplayersp = Minecraft.player;
        float f = entityplayersp.prevRenderArmPitch + (entityplayersp.renderArmPitch - entityplayersp.prevRenderArmPitch) * p_187458_1_;
        float f1 = entityplayersp.prevRenderArmYaw + (entityplayersp.renderArmYaw - entityplayersp.prevRenderArmYaw) * p_187458_1_;
        GlStateManager.rotate((entityplayersp.rotationPitch - f) * 0.1F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate((entityplayersp.rotationYaw - f1) * 0.1F, 0.0F, 1.0F, 0.0F);
    }

    /**
     * Return the angle to render the Map
     */
    private float getMapAngleFromPitch(float pitch) {
        float f = 1.0F - pitch / 45.0F + 0.1F;
        f = MathHelper.clamp(f, 0.0F, 1.0F);
        f = -MathHelper.cos(f * (float) Math.PI) * 0.5F + 0.5F;
        return f;
    }

    private void renderArms() {
        if (!Minecraft.player.isInvisible()) {
            GlStateManager.disableCull();
            GlStateManager.pushMatrix();
            GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
            renderArm(EnumHandSide.RIGHT);
            renderArm(EnumHandSide.LEFT);
            GlStateManager.popMatrix();
            GlStateManager.enableCull();
        }
    }

    private void renderArm(EnumHandSide p_187455_1_) {
        Minecraft.getTextureManager().bindTexture(Minecraft.player.getLocationSkin());
        Render<AbstractClientPlayer> render = renderManager.getEntityRenderObject(Minecraft.player);
        RenderPlayer renderplayer = (RenderPlayer) render;
        GlStateManager.pushMatrix();
        float f = p_187455_1_ == EnumHandSide.RIGHT ? 1.0F : -1.0F;
        GlStateManager.rotate(92.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(45.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(f * -41.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.translate(f * 0.3F, -1.1F, 0.45F);

        if (p_187455_1_ == EnumHandSide.RIGHT) {
            renderplayer.renderRightArm(Minecraft.player);
        } else {
            renderplayer.renderLeftArm(Minecraft.player);
        }

        GlStateManager.popMatrix();
    }

    private void renderMapFirstPersonSide(float p_187465_1_, EnumHandSide p_187465_2_, float p_187465_3_, ItemStack p_187465_4_) {
        float f = p_187465_2_ == EnumHandSide.RIGHT ? 1.0F : -1.0F;
        GlStateManager.translate(f * 0.125F, -0.125F, 0.0F);

        if (!Minecraft.player.isInvisible()) {
            GlStateManager.pushMatrix();
            GlStateManager.rotate(f * 10.0F, 0.0F, 0.0F, 1.0F);
            renderArmFirstPerson(p_187465_1_, p_187465_3_, p_187465_2_);
            GlStateManager.popMatrix();
        }

        GlStateManager.pushMatrix();
        GlStateManager.translate(f * 0.51F, -0.08F + p_187465_1_ * -1.2F, -0.75F);
        float f1 = MathHelper.sqrt(p_187465_3_);
        float f2 = MathHelper.sin(f1 * (float) Math.PI);
        float f3 = -0.5F * f2;
        float f4 = 0.4F * MathHelper.sin(f1 * ((float) Math.PI * 2F));
        float f5 = -0.3F * MathHelper.sin(p_187465_3_ * (float) Math.PI);
        GlStateManager.translate(f * f3, f4 - 0.3F * f2, f5);
        GlStateManager.rotate(f2 * -45.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(f * f2 * -30.0F, 0.0F, 1.0F, 0.0F);
        renderMapFirstPerson(p_187465_4_);
        GlStateManager.popMatrix();
    }

    private void renderMapFirstPerson(float p_187463_1_, float p_187463_2_, float p_187463_3_) {
        float f = MathHelper.sqrt(p_187463_3_);
        float f1 = -0.2F * MathHelper.sin(p_187463_3_ * (float) Math.PI);
        float f2 = -0.4F * MathHelper.sin(f * (float) Math.PI);
        GlStateManager.translate(0.0F, -f1 / 2.0F, f2);
        float f3 = getMapAngleFromPitch(p_187463_1_);
        GlStateManager.translate(0.0F, 0.04F + p_187463_2_ * -1.2F + f3 * -0.5F, -0.72F);
        GlStateManager.rotate(f3 * -85.0F, 1.0F, 0.0F, 0.0F);
        renderArms();
        float f4 = MathHelper.sin(f * (float) Math.PI);
        GlStateManager.rotate(f4 * 20.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.scale(2.0F, 2.0F, 2.0F);
        renderMapFirstPerson(itemStackMainHand);
    }

    private void renderMapFirstPerson(ItemStack stack) {
        GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.scale(0.38F, 0.38F, 0.38F);
        GlStateManager.disableLighting();
        Minecraft.getTextureManager().bindTexture(RES_MAP_BACKGROUND);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.translate(-0.5F, -0.5F, 0.0F);
        GlStateManager.scale(0.0078125F, 0.0078125F, 0.0078125F);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(-7.0D, 135.0D, 0.0D).tex(0.0D, 1.0D).endVertex();
        bufferbuilder.pos(135.0D, 135.0D, 0.0D).tex(1.0D, 1.0D).endVertex();
        bufferbuilder.pos(135.0D, -7.0D, 0.0D).tex(1.0D, 0.0D).endVertex();
        bufferbuilder.pos(-7.0D, -7.0D, 0.0D).tex(0.0D, 0.0D).endVertex();
        tessellator.draw();
        MapData mapdata = ReflectorForge.getMapData(Items.FILLED_MAP, stack, Minecraft.world);

        if (mapdata != null) {
            Minecraft.gameRenderer.getMapItemRenderer().renderMap(mapdata, false);
        }

        GlStateManager.enableLighting();
    }

    private void renderArmFirstPerson(float p_187456_1_, float p_187456_2_, EnumHandSide p_187456_3_) {
        EventTransformSideFirstPerson event = new EventTransformSideFirstPerson(p_187456_3_);
        EventManager.call(event);
        boolean flag = p_187456_3_ != EnumHandSide.LEFT;
        float f = flag ? 1.0F : -1.0F;
        float f1 = MathHelper.sqrt(p_187456_2_);
        float f2 = -0.3F * MathHelper.sin(f1 * (float) Math.PI);
        float f3 = 0.4F * MathHelper.sin(f1 * ((float) Math.PI * 2F));
        float f4 = -0.4F * MathHelper.sin(p_187456_2_ * (float) Math.PI);
        GlStateManager.translate(f * (f2 + 0.64000005F), f3 + -0.6F + p_187456_1_ * -0.6F, f4 + -0.71999997F);
        GlStateManager.rotate(f * 45.0F, 0.0F, 1.0F, 0.0F);
        float f5 = MathHelper.sin(p_187456_2_ * p_187456_2_ * (float) Math.PI);
        float f6 = MathHelper.sin(f1 * (float) Math.PI);
        GlStateManager.rotate(f * f6 * 70.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(f * f5 * -20.0F, 0.0F, 0.0F, 1.0F);
        AbstractClientPlayer abstractclientplayer = Minecraft.player;
        Minecraft.getTextureManager().bindTexture(abstractclientplayer.getLocationSkin());
        GlStateManager.translate(f * -1.0F, 3.6F, 3.5F);
        GlStateManager.rotate(f * 120.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate(200.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(f * -135.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.translate(f * 5.6F, 0.0F, 0.0F);
        RenderPlayer renderplayer = (RenderPlayer) renderManager.<AbstractClientPlayer>getEntityRenderObject(abstractclientplayer);
        GlStateManager.disableCull();

        if (MoonWare.featureManager.getFeatureByClass(Chams2.class).getState() && Chams2.chamsMode.currentMode.equals("Fill")) {
            GL11.glPushMatrix();
            GL11.glEnable(GL11.GL_POLYGON_OFFSET_LINE);
            GL11.glPolygonOffset(1.0F, 1000000.0F);
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            if (!Chams2.clientColor.getBoolValue()) {
                org.moonware.client.helpers.render.RenderHelper.setColor(Chams2.colorChams.getColorValue());
            } else {
                org.moonware.client.helpers.render.RenderHelper.setColor(ClientHelper.getClientColor().getRGB());
            }
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glDepthMask(false);
        }

        assert renderplayer != null;
        if (flag) {
            renderplayer.renderRightArm(abstractclientplayer);
        } else {
            renderplayer.renderLeftArm(abstractclientplayer);
        }

        if (MoonWare.featureManager.getFeatureByClass(Chams2.class).getState() && Chams2.chamsMode.currentMode.equals("Fill")) {
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glDepthMask(true);
            if (!Chams2.clientColor.getBoolValue()) {
                org.moonware.client.helpers.render.RenderHelper.setColor(Chams2.colorChams.getColorValue());
            } else {
                org.moonware.client.helpers.render.RenderHelper.setColor(ClientHelper.getClientColor().getRGB());
            }
            if (flag) {
                renderplayer.renderRightArm(abstractclientplayer);
            } else {
                renderplayer.renderLeftArm(abstractclientplayer);
            }
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glPolygonOffset(1.0F, -1000000.0F);
            GL11.glDisable(GL11.GL_POLYGON_OFFSET_LINE);
            GL11.glPopMatrix();
        }

        GlStateManager.enableCull();
    }

    private void transformEatFirstPerson(float p_187454_1_, EnumHandSide p_187454_2_, ItemStack p_187454_3_) {
        EventTransformSideFirstPerson event = new EventTransformSideFirstPerson(p_187454_2_);
        EventManager.call(event);
        float f = (float) Minecraft.player.getItemInUseCount() - p_187454_1_ + 1.0F;
        float f1 = f / (float) p_187454_3_.getMaxItemUseDuration();

        if (f1 < 0.8F) {
            float f2 = MathHelper.abs(MathHelper.cos(f / 4.0F * (float) Math.PI) * 0.1F);
            GlStateManager.translate(0.0F, f2, 0.0F);
        }

        float f3 = 1.0F - (float) Math.pow(f1, 27.0D);
        int i = p_187454_2_ == EnumHandSide.RIGHT ? 1 : -1;
        GlStateManager.translate(f3 * 0.6F * (float) i, f3 * -0.5F, f3 * 0.0F);
        GlStateManager.rotate((float) i * f3 * 90.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(f3 * 10.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate((float) i * f3 * 30.0F, 0.0F, 0.0F, 1.0F);
    }

    private void transformFirstPerson(EnumHandSide p_187453_1_, float p_187453_2_) {
        float angle = System.currentTimeMillis() / 3L % 360L;
        int i = p_187453_1_ == EnumHandSide.RIGHT ? 1 : -1;
        float f = MathHelper.sin(p_187453_2_ * p_187453_2_ * (float) Math.PI);
        GlStateManager.rotate(i * (45.0F + f * -20.0F), 0.0F, 1.0F, 0.0F);
        float f1 = MathHelper.sin(MathHelper.sqrt(p_187453_2_) * (float) Math.PI);
        GlStateManager.rotate(i * f1 * -20.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate(f1 * -80.0F, 1.0F, 0.0F, 0.0F);
        if(MoonWare.featureManager.getFeatureByClass(Animations.class).getState() && Animations.itemAnimation.getBoolValue()) {
            if (Animations.itemAnim.currentMode.equals("360")) {
                GlStateManager.rotate(angle, 0.0F, 1.0F, 0.0F);
            } else if (Animations.itemAnim.currentMode.equals("Spin")) {
                GlStateManager.rotate(spin * Animations.spinSpeed.getNumberValue(), spin, 0, spin);
                spin++;
            }
        }
        GlStateManager.rotate(i * -45.0f, 0.0F, 1.0F, 0.0F);

        if (MoonWare.featureManager.getFeatureByClass(Animations.class).getState()) {
            if (Animations.smallItem.getBoolValue()) {
                GlStateManager.scale(0.7f, 0.7f, 0.7f);
            } else if (Animations.swordAnim.currentMode.equals("Custom")){
                GlStateManager.scale(Animations.scale.getNumberValue(), Animations.scale.getNumberValue(), Animations.scale.getNumberValue());
            }
        }
    }

    private void transformSideFirstPerson(EnumHandSide p_187459_1_, float p_187459_2_) {
        EventTransformSideFirstPerson event = new EventTransformSideFirstPerson(p_187459_1_);
        EventManager.call(event);
        int i = p_187459_1_ == EnumHandSide.RIGHT ? 1 : -1;
        GlStateManager.translate((float) i * 0.56F, -0.52F + p_187459_2_ * -0.6F, -0.72F);
    }

    private void translate() {
        GlStateManager.translate(-0.5f, 0.08f, 0.0f);
        GlStateManager.rotate(20.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(-80.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.rotate(20.0f, 0.0f, 1.0f, 0.0f);
        if (MoonWare.featureManager.getFeatureByClass(Animations.class).getState()) {
            if (Animations.smallItem.getBoolValue()) {
                GlStateManager.scale(0.7f, 0.7f, 0.7f);
            } else if (Animations.swordAnim.currentMode.equals("Custom")){
                GlStateManager.scale(Animations.scale.getNumberValue(), Animations.scale.getNumberValue(), Animations.scale.getNumberValue());
            }
        }
    }

    /**
     * Renders the active item in the player's hand when in first person mode.
     */
    public void renderItemInFirstPerson(float partialTicks) {
        AbstractClientPlayer abstractclientplayer = Minecraft.player;
        float f = abstractclientplayer.getSwingProgress(partialTicks);
        EnumHand enumhand = MoreObjects.firstNonNull(abstractclientplayer.swingingHand, EnumHand.MAIN_HAND);
        float f1 = abstractclientplayer.prevRotationPitch + (abstractclientplayer.rotationPitch - abstractclientplayer.prevRotationPitch) * partialTicks;
        float f2 = abstractclientplayer.prevRotationYaw + (abstractclientplayer.rotationYaw - abstractclientplayer.prevRotationYaw) * partialTicks;
        boolean flag = true;
        boolean flag1 = true;

        if (abstractclientplayer.isHandActive()) {
            ItemStack itemstack = abstractclientplayer.getActiveItemStack();

            if (!itemstack.isEmpty() && itemstack.getItem() == Items.BOW) {
                EnumHand enumhand1 = abstractclientplayer.getActiveHand();
                flag = enumhand1 == EnumHand.MAIN_HAND;
                flag1 = !flag;
            }
        }

        rotateArroundXAndY(f1, f2);
        setLightmap();
        rotateArm(partialTicks);
        GlStateManager.enableRescaleNormal();

        if (flag) {
            float f3 = enumhand == EnumHand.MAIN_HAND ? f : 0.0F;
            float f5 = 1.0F - (prevEquippedProgressMainHand + (equippedProgressMainHand - prevEquippedProgressMainHand) * partialTicks);

            if (!Reflector.ForgeHooksClient_renderSpecificFirstPersonHand.exists() || !Reflector.callBoolean(Reflector.ForgeHooksClient_renderSpecificFirstPersonHand, EnumHand.MAIN_HAND, partialTicks, f1, f3, f5, itemStackMainHand)) {
                renderItemInFirstPerson(abstractclientplayer, partialTicks, f1, EnumHand.MAIN_HAND, f3, itemStackMainHand, f5);
            }
        }

        if (flag1) {
            float f4 = enumhand == EnumHand.OFF_HAND ? f : 0.0F;
            float f6 = 1.0F - (prevEquippedProgressOffHand + (equippedProgressOffHand - prevEquippedProgressOffHand) * partialTicks);

            if (!Reflector.ForgeHooksClient_renderSpecificFirstPersonHand.exists() || !Reflector.callBoolean(Reflector.ForgeHooksClient_renderSpecificFirstPersonHand, EnumHand.OFF_HAND, partialTicks, f1, f4, f6, itemStackOffHand)) {
                renderItemInFirstPerson(abstractclientplayer, partialTicks, f1, EnumHand.OFF_HAND, f4, itemStackOffHand, f6);
            }
        }

        GlStateManager.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
    }

    private void transformFirstPersonItem(float equipProgress, float swingProgress) {

        GlStateManager.translate(0.56f, -0.44F, -0.71999997f);
        GlStateManager.translate(0.0f, equipProgress * -0.6f, 0.0f);
        GlStateManager.rotate(45.0f, 0.0f, 1.0f, 0.0f);

        float f = MathHelper.sin(swingProgress * swingProgress * 3.1415927f);
        float f2 = MathHelper.sin(MathHelper.sqrt(swingProgress) * 3.1415927f);
        GlStateManager.rotate(f * -20.0f, 0.0f, 0.0f, 0.0f);
        GlStateManager.rotate(f2 * -20.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.rotate(f2 * -80.0f, 0.01f, 0.0f, 0.0f);

        GlStateManager.translate(0.4f, 0.2f, 0.2f);
    }

    private float spin;
    private float anim;
    private float anim2;
    private float animf;
    private float animf2;
    private float animff;
    private float animff2;
    private float animfap4;
    private float animfap4t;
    private float animfap5;
    private float animfap5t;
    private float fap;
    private float fap2;
    private float fap3;
    private float fap4;
    private boolean fap4b;
    private float fap5;
    private boolean fap5b;
    private float swing2;



    public void renderItemInFirstPerson(AbstractClientPlayer p_187457_1_, float p_187457_2_, float p_187457_3_, EnumHand p_187457_4_, float p_187457_5_, ItemStack p_187457_6_, float p_187457_7_) {
        float swingprogresss = Minecraft.player.getSwingProgress(p_187457_2_);
        if (!Config.isShaders() || !Shaders.isSkipRenderHand(p_187457_4_)) {
            boolean flag = p_187457_4_ == EnumHand.MAIN_HAND;
            EnumHandSide enumhandside = flag ? p_187457_1_.getPrimaryHand() : p_187457_1_.getPrimaryHand().opposite();
            GlStateManager.pushMatrix();

            if (p_187457_6_.isEmpty()) {
                if (flag && !p_187457_1_.isInvisible()) {
                    renderArmFirstPerson(p_187457_7_, p_187457_5_, enumhandside);
                }
            } else if (p_187457_6_.getItem() instanceof ItemMap) {
                if (flag && itemStackOffHand.isEmpty()) {
                    renderMapFirstPerson(p_187457_3_, p_187457_7_, p_187457_5_);
                } else {
                    renderMapFirstPersonSide(p_187457_7_, enumhandside, p_187457_5_, p_187457_6_);
                }
            } else {
                boolean flag1 = enumhandside == EnumHandSide.RIGHT;

                if (p_187457_1_.isHandActive() && p_187457_1_.getItemInUseCount() > 0 && p_187457_1_.getActiveHand() == p_187457_4_) {
                    int j = flag1 ? 1 : -1;

                    switch (p_187457_6_.getItemUseAction()) {
                        case NONE:
                            transformSideFirstPerson(enumhandside, p_187457_7_);
                            break;

                        case EAT:
                        case DRINK:
                            transformEatFirstPerson(p_187457_2_, enumhandside, p_187457_6_);
                            transformSideFirstPerson(enumhandside, p_187457_7_);
                            break;

                        case BLOCK:
                            transformSideFirstPerson(enumhandside, p_187457_7_);
                            break;

                        case BOW:
                            transformSideFirstPerson(enumhandside, p_187457_7_);
                            GlStateManager.translate((float) j * -0.2785682F, 0.18344387F, 0.15731531F);
                            GlStateManager.rotate(-13.935F, 1.0F, 0.0F, 0.0F);
                            GlStateManager.rotate((float) j * 35.3F, 0.0F, 1.0F, 0.0F);
                            GlStateManager.rotate((float) j * -9.785F, 0.0F, 0.0F, 1.0F);
                            float f5 = (float) p_187457_6_.getMaxItemUseDuration() - ((float) Minecraft.player.getItemInUseCount() - p_187457_2_ + 1.0F);
                            float f6 = f5 / 20.0F;
                            f6 = (f6 * f6 + f6 * 2.0F) / 3.0F;

                            if (f6 > 1.0F) {
                                f6 = 1.0F;
                            }

                            if (f6 > 0.1F) {
                                float f7 = MathHelper.sin((f5 - 0.1F) * 1.3F);
                                float f3 = f6 - 0.1F;
                                float f4 = f7 * f3;
                                GlStateManager.translate(f4 * 0.0F, f4 * 0.004F, f4 * 0.0F);
                            }

                            GlStateManager.translate(f6 * 0.0F, f6 * 0.0F, f6 * 0.04F);
                            GlStateManager.scale(1.0F, 1.0F, 1.0F + f6 * 0.2F);
                            GlStateManager.rotate((float) j * 45.0F, 0.0F, -1.0F, 0.0F);
                    }
                } else {
                    float f = -0.4F * MathHelper.sin(MathHelper.sqrt(p_187457_5_) * (float) Math.PI);
                    float f1 = 0.2F * MathHelper.sin(MathHelper.sqrt(p_187457_5_) * ((float) Math.PI * 2F));
                    float f2 = -0.2F * MathHelper.sin(p_187457_5_ * (float) Math.PI);
                    int i = flag1 ? 1 : -1;
                    float equipProgress = 1.0F - (prevEquippedProgressMainHand + (equippedProgressMainHand - prevEquippedProgressMainHand) * p_187457_2_);
                    float swingprogress = Minecraft.player.getSwingProgress(p_187457_2_);
                    String mode = Animations.swordAnim.getCurrentMode();

                    if (MoonWare.featureManager.getFeatureByClass(Animations.class).getState()) {
                        if (MoonWare.featureManager.getFeatureByClass(Animations.class).getState()) {

                            if (enumhandside != EnumHandSide.LEFT) {
                                if (mode.equalsIgnoreCase("Wild3")) {

                                    GlStateManager.translate(1f, -0.1f, -0.55f);
                                    GL11.glRotatef((1.0f - swing2) * 360.0f, 1.0f, 0.0f, 0.0f);
                                    if (swing2 == 0) {
                                        swing2 = AnimationHelper.calculateCompensation(-0.1f, swing2,4,2);
                                    }else if (swing2 == -4) {
                                        swing2 = AnimationHelper.calculateCompensation(0,swing2,4,2);
                                    }
                                    translate();
                                }
                                if (mode.equalsIgnoreCase("Fap") && KillAura.target != null) {
                                    GlStateManager.translate(0.96f, -0.02f, -0.71999997f);
                                    GlStateManager.translate(0.0f, -0.0f, 0.0f);
                                    GlStateManager.rotate(45.0f, 0.0f, 1.0f, 0.0f);
                                    float var3 = MathHelper.sin(0.0f);
                                    float var4 = MathHelper.sin(MathHelper.sqrt(0.0f) * (float)Math.PI);
                                    GlStateManager.rotate(var3 * -20.0f, 0.0f, 1.0f, 0.0f);
                                    GlStateManager.rotate(var4 * -20.0f, 0.0f, 0.0f, 1.0f);
                                    GlStateManager.rotate(var4 * -80.0f, 1.0f, 0.0f, 0.0f);
                                    GlStateManager.translate(-0.5f, 0.2f, 0.0f);
                                    GlStateManager.rotate(30.0f, 0.0f, 1.0f, 0.0f);
                                    GlStateManager.rotate(-80.0f, 1.0f, 0.0f, 0.0f);
                                    GlStateManager.rotate(60.0f, 0.0f, 1.0f, 0.0f);
                                    int alpha = (int)Math.min(255L, (System.currentTimeMillis() % 255L > 127L ? Math.abs(Math.abs(System.currentTimeMillis()) % 255L - 255L) : System.currentTimeMillis() % 255L) * 2L);
                                    float f5 = (double)f1 > 0.5 ? 1.0f - f1 : f1;
                                    GlStateManager.translate(0.3f, -0.0f, 0.4f);
                                    GlStateManager.rotate(0.0f, 0.0f, 0.0f, 1.0f);
                                    GlStateManager.translate(0.0f, 0.5f, 0.0f);
                                    GlStateManager.rotate(90.0f, 1.0f, 0.0f, -1.0f);
                                    GlStateManager.translate(0.6f, 0.5f, 0.0f);
                                    GlStateManager.rotate(-90.0f, 1.0f, 0.0f, -1.0f);
                                    GlStateManager.rotate(-10.0f, 1.0f, 0.0f, -1.0f);
                                    GlStateManager.rotate(-f5 * 10.0f, 10.0f, 10.0f, -9.0f);
                                    GlStateManager.rotate(10.0f, -1.0f, 0.0f, 0.0f);
                                    GlStateManager.translate(0.0, 0.0, -0.5);
                                    GlStateManager.rotate(Minecraft.player.isSwingInProgress ? (float)(-alpha) / Animations.fapSmooth.getNumberValue() : KillAura.target != null ? (float)(-alpha) / Animations.fapSmooth.getNumberValue() : 1.0f, 1.0f, -0.0f, 1.0f);
                                    GlStateManager.translate(0.0, 0.0, 0.5);
                                }else if (mode.equalsIgnoreCase("Fap")) {
                                    GlStateManager.translate((float) i * f, f1, f2);
                                    transformSideFirstPerson(enumhandside, p_187457_7_);
                                    transformFirstPerson(enumhandside, p_187457_5_);
                                }
                                if (mode.equalsIgnoreCase("NeverHook")) {
                                    transformFirstPersonItem(equipProgress / 3, swingprogress);
                                    translate();
                                } else if (mode.equalsIgnoreCase("Spin")) {
                                    transformFirstPersonItem(0, 0);
                                    translate();
                                    GlStateManager.rotate(spin * Animations.spinSpeed.getNumberValue(), spin, 0, spin);
                                    spin++;
                                } else if (mode.equalsIgnoreCase("Astolfo")) {
                                    GlStateManager.rotate((float) (System.currentTimeMillis() / 16 * (int) Animations.spinSpeed.getNumberValue() % 360), 0.0f, 0.0f, -0.1f);
                                    transformFirstPersonItem(0, 0);
                                    translate();
                                } else if (mode.equalsIgnoreCase("Neutral")) {

                                    transformFirstPersonItem(0, 0);
                                    translate();
                                }
                                if (true) {
                                    if (mode.equalsIgnoreCase("Other-1")) {
                                        GlStateManager.translate(0.56f, -0.52f, -0.9999997f);
                                        GlStateManager.translate(0.0f, 0.0f, 0.0f);
                                        this.transformFirstPersonItem(equipProgress / 2.0f, swingprogress);
                                        GlStateManager.translate(-0.7f, 0.2f, 0.0f);
                                        f2 = MathHelper.sin(MathHelper.sqrt(swingprogress) * (float) Math.PI);
                                        GlStateManager.rotate(f2 * 30.0f, -f2, -0.0f, 9.0f);
                                        GlStateManager.rotate(f2 * 40.0f, 1.0f, -f2, -0.0f);
                                    } else if (mode.equalsIgnoreCase("Other-2")) {
                                        float sigma = MathHelper.sin(MathHelper.sqrt(swingprogress) * (float) Math.PI);
                                        GlStateManager.translate(0.56f, -0.42f, -0.71999997f);
                                        GlStateManager.translate(0.0f, 0 * 0.5f * -0.6f, 0.0f);
                                        GlStateManager.rotate(45.0f, 0.0f, 1.0f, 0.0f);
                                        float sigmaHatar1 = MathHelper.sin(0.0f);
                                        float sigmaHatar2 = MathHelper.sin(MathHelper.sqrt(0.0f) * (float) Math.PI);
                                        GlStateManager.rotate(sigmaHatar1 * -20.0f, 0.0f, 1.0f, 0.0f);
                                        GlStateManager.rotate(sigmaHatar2 * -20.0f, 0.0f, 0.0f, 1.0f);
                                        GlStateManager.rotate(sigmaHatar2 * -80.0f, 1.0f, 0.0f, 0.0f);
                                        GlStateManager.scale(0.6f, 0.6f, 0.6f);
                                        GlStateManager.rotate(sigma * 55.0f / 2.0f, -8.0f, -0.0f, 9.0f);
                                        GlStateManager.rotate(-sigma * 45.0f, 1.0f, -sigma / 2.0f, -0.0f);
                                        GlStateManager.translate(-0.5f, 0.2f, 0.0f);
                                        GlStateManager.rotate(120.0f, 0.0f, 0.0f, 0.0f);
                                        GlStateManager.rotate(220.0f, 0.0f, 0.0f, 0.0f);
                                        GlStateManager.rotate(320, 0.0f, 0.0f, 0.0f);
                                        GL11.glTranslated(1.2, 0.3, 0.5);
                                        GL11.glTranslatef(-1.0f, this.mc.player.isSneaking() ? -0.1f : -0.2f, 0.2f);
                                        GlStateManager.scale(1.2f, 1.2f, 1.2f);
                                    } else if (mode.equalsIgnoreCase("Other-3")) {
                                        float sigma = MathHelper.sin((MathHelper.sqrt(swingprogress) - 1) * (float) Math.PI);
                                        GlStateManager.translate(0.56f, -0.42f, -0.71999997f);
                                        GlStateManager.translate(0.0f, 0 * 0.5f * -0.6f, 0.0f);
                                        GlStateManager.rotate(45.0f, 0.0f, 1.0f, 0.0f);
                                        float sigmaHatar1 = MathHelper.sin(0.0f);
                                        float sigmaHatar2 = MathHelper.sin(MathHelper.sqrt(0.0f) * (float) Math.PI);
                                        GlStateManager.rotate(sigmaHatar1 * -20.0f, 0.0f, 1.0f, 0.0f);
                                        GlStateManager.rotate(sigmaHatar2 * -20.0f, 0.0f, 0.0f, 1.0f);
                                        GlStateManager.rotate(sigmaHatar2 * -80.0f, 1.0f, 0.0f, 0.0f);
                                        //GlStateManager.scale(0.4f, 0.4f, 0.4f);
                                        GlStateManager.rotate(sigma * 55.0f / 2.0f, -8.0f, -0.0f, 9.0f);
                                        GlStateManager.rotate(-sigma * 45.0f, 1.0f, -sigma / 2.0f, -0.0f);
                                        GlStateManager.translate(-0.5f, 0.2f, 0.0f);
                                        GlStateManager.rotate(120.0f, 0.0f, 0.0f, 0.0f);
                                        GlStateManager.rotate(220.0f, 0.0f, 0.0f, 0.0f);
                                        GlStateManager.rotate(320, 0.0f, 0.0f, 0.0f);
                                        GL11.glTranslated(1.2, 0.3, 0.5);
                                        GL11.glTranslatef(-1.0f, this.mc.player.isSneaking() ? -0.1f : -0.2f, 0.2f);
                                        GlStateManager.scale(1.2f, 1.2f, 1.2f);
                                    }
                                }else if (mode.equalsIgnoreCase("Other-1") || mode.equalsIgnoreCase("Other-2") || mode.equalsIgnoreCase("Other-3")){
                                    GlStateManager.translate((float) i * f, f1, f2);
                                    transformSideFirstPerson(enumhandside, p_187457_7_);
                                    transformFirstPerson(enumhandside, p_187457_5_);
                                }

                                if (mode.equalsIgnoreCase("Rotate")) {
                                    GlStateManager.translate(0.96f, -0.02f, -0.71999997f);
                                    GlStateManager.translate(0.0f, -0.0f, 0.0f);
                                    GlStateManager.rotate(45.0f, 0.0f, 1.0f, 0.0f);
                                    float var3 = MathHelper.sin(0.0f);
                                    float var4 = MathHelper.sin(MathHelper.sqrt(0.0f) * (float)Math.PI);
                                    GlStateManager.rotate(var3 * -20.0f, 0.0f, 1.0f, 0.0f);
                                    GlStateManager.rotate(var4 * -20.0f, 0.0f, 0.0f, 1.0f);
                                    GlStateManager.rotate(var4 * -80.0f, 1.0f, 0.0f, 0.0f);
                                    GlStateManager.translate(-0.5f, 0.2f, 0.0f);
                                    GlStateManager.rotate(30.0f, 0.0f, 1.0f, 0.0f);
                                    GlStateManager.rotate(-80.0f, 1.0f, 0.0f, 0.0f);
                                    GlStateManager.rotate(60.0f, 0.0f, 1.0f, 0.0f);
                                    int alpha = (int)Math.min(255L, (System.currentTimeMillis() % 255L > 127L ? Math.abs(Math.abs(System.currentTimeMillis()) % 255L - 255L) : System.currentTimeMillis() % 255L) * 2L);
                                    float f5 = (double)f1 > 0.5 ? 1.0f - f1 : f1;
                                    GlStateManager.translate(0.3f, -0.0f, 0.4f);
                                    GlStateManager.rotate(0.0f, 0.0f, 0.0f, 1.0f);
                                    GlStateManager.translate(0.0f, 0.5f, 0.0f);
                                    GlStateManager.rotate(90.0f, 1.0f, 0.0f, -1.0f);
                                    GlStateManager.translate(0.6f, 0.5f, 0.0f);
                                    GlStateManager.rotate(-90.0f, 1.0f, 0.0f, -1.0f);
                                    GlStateManager.rotate(-10.0f, 1.0f, 0.0f, -1.0f);
                                    GlStateManager.rotate(-f5 * 10.0f, 10.0f, 10.0f, -9.0f);
                                    GlStateManager.rotate(10.0f, -1.0f, 0.0f, 0.0f);
                                    GlStateManager.translate(0.0, 0.0, -0.5);
                                    GlStateManager.rotate(swingprogress < 0.20 && swingprogress > 0 ? (float)(-alpha) / Animations.fapSmooth.getNumberValue() : 1.0f, 1.0f, -0.0f, 1.0f);
                                    GlStateManager.translate(0.0, 0.0, 0.5);
                                }else if (mode.equalsIgnoreCase("Custom")) {
                                    if (Minecraft.player.ticksExisted % 1.5 == 0) {
                                        //System.out.println(swingprogress);
                                    }
                                    GlStateManager.rotate(Animations.angle.getNumberValue(), Animations.rotate.getNumberValue(), Animations.rotate2.getNumberValue(), Animations.rotate3.getNumberValue());
                                    GlStateManager.translate(Animations.x.getNumberValue(), Animations.y.getNumberValue(), Animations.z.getNumberValue());
                                    transformFirstPersonItem(equipProgress / Animations.smooth.getNumberValue(), swingprogress);
                                    translate();
                                }
                            } else {
                                GlStateManager.translate((float) i * f, f1, f2);
                                transformSideFirstPerson(enumhandside, p_187457_7_);
                                transformFirstPerson(enumhandside, p_187457_5_);
                            }
                        } else {
                            transformSideFirstPerson(enumhandside, p_187457_7_);
                            transformFirstPerson(enumhandside, p_187457_5_);
                        }
                    } else {
                        GlStateManager.translate((float) i * f, f1, f2);
                        transformSideFirstPerson(enumhandside, p_187457_7_);
                        transformFirstPerson(enumhandside, p_187457_5_);
                    }
                }
                renderItemSide(p_187457_1_, p_187457_6_, flag1 ? ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND : ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND, !flag1);
            }
            GlStateManager.popMatrix();
        }
    }

    /**
     * Renders the overlays.
     */
    public void renderOverlays(float partialTicks) {
        GlStateManager.disableAlpha();

        if (Minecraft.player.isEntityInsideOpaqueBlock()) {
            IBlockState iblockstate = Minecraft.world.getBlockState(new BlockPos(Minecraft.player));
            BlockPos blockpos = new BlockPos(Minecraft.player);
            EntityPlayer entityplayer = Minecraft.player;

            for (int i = 0; i < 8; ++i) {
                double d0 = entityplayer.posX + (double) (((float) ((i >> 0) % 2) - 0.5F) * entityplayer.width * 0.8F);
                double d1 = entityplayer.posY + (double) (((float) ((i >> 1) % 2) - 0.5F) * 0.1F);
                double d2 = entityplayer.posZ + (double) (((float) ((i >> 2) % 2) - 0.5F) * entityplayer.width * 0.8F);
                BlockPos blockpos1 = new BlockPos(d0, d1 + (double) entityplayer.getEyeHeight(), d2);
                IBlockState iblockstate1 = Minecraft.world.getBlockState(blockpos1);

                if (iblockstate1.func_191058_s()) {
                    iblockstate = iblockstate1;
                    blockpos = blockpos1;
                }
            }

            if (iblockstate.getRenderType() != EnumBlockRenderType.INVISIBLE) {
                Object object = Reflector.getFieldValue(Reflector.RenderBlockOverlayEvent_OverlayType_BLOCK);

                if (!Reflector.callBoolean(Reflector.ForgeEventFactory_renderBlockOverlay, Minecraft.player, partialTicks, object, iblockstate, blockpos)) {
                    renderBlockInHand(Minecraft.getBlockRenderDispatcher().getBlockModelShapes().getTexture(iblockstate));
                }
            }
        }

        if (!Minecraft.player.isSpectator()) {
            if (Minecraft.player.isInsideOfMaterial(Material.WATER) && !Reflector.callBoolean(Reflector.ForgeEventFactory_renderWaterOverlay, Minecraft.player, partialTicks)) {
                renderWaterOverlayTexture(partialTicks);
            }

            if (Minecraft.player.isBurning() && !Reflector.callBoolean(Reflector.ForgeEventFactory_renderFireOverlay, Minecraft.player, partialTicks)) {
                renderFireInFirstPerson();
            }
        }

        GlStateManager.enableAlpha();
    }

    /**
     * Render the block in the player's hand
     */
    private void renderBlockInHand(TextureAtlasSprite partialTicks) {
        Minecraft.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        float f = 0.1F;
        GlStateManager.color(0.1F, 0.1F, 0.1F, 0.5F);
        GlStateManager.pushMatrix();
        float f1 = -1.0F;
        float f2 = 1.0F;
        float f3 = -1.0F;
        float f4 = 1.0F;
        float f5 = -0.5F;
        float f6 = partialTicks.getMinU();
        float f7 = partialTicks.getMaxU();
        float f8 = partialTicks.getMinV();
        float f9 = partialTicks.getMaxV();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(-1.0D, -1.0D, -0.5D).tex(f7, f9).endVertex();
        bufferbuilder.pos(1.0D, -1.0D, -0.5D).tex(f6, f9).endVertex();
        bufferbuilder.pos(1.0D, 1.0D, -0.5D).tex(f6, f8).endVertex();
        bufferbuilder.pos(-1.0D, 1.0D, -0.5D).tex(f7, f8).endVertex();
        tessellator.draw();
        GlStateManager.popMatrix();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    }

    /**
     * Renders a texture that warps around based on the direction the player is looking. Texture needs to be bound
     * before being called. Used for the water overlay.
     */
    private void renderWaterOverlayTexture(float partialTicks) {
        if (!Config.isShaders() || Shaders.isUnderwaterOverlay()) {
            Minecraft.getTextureManager().bindTexture(RES_UNDERWATER_OVERLAY);
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuffer();
            float f = Minecraft.player.getBrightness();
            GlStateManager.color(f, f, f, 0.5F);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.pushMatrix();
            float f1 = 4.0F;
            float f2 = -1.0F;
            float f3 = 1.0F;
            float f4 = -1.0F;
            float f5 = 1.0F;
            float f6 = -0.5F;
            float f7 = -Minecraft.player.rotationYaw / 64.0F;
            float f8 = Minecraft.player.rotationPitch / 64.0F;
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
            bufferbuilder.pos(-1.0D, -1.0D, -0.5D).tex(4.0F + f7, 4.0F + f8).endVertex();
            bufferbuilder.pos(1.0D, -1.0D, -0.5D).tex(0.0F + f7, 4.0F + f8).endVertex();
            bufferbuilder.pos(1.0D, 1.0D, -0.5D).tex(0.0F + f7, 0.0F + f8).endVertex();
            bufferbuilder.pos(-1.0D, 1.0D, -0.5D).tex(4.0F + f7, 0.0F + f8).endVertex();
            tessellator.draw();
            GlStateManager.popMatrix();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.disableBlend();
        }
    }

    /**
     * Renders the fire on the screen for first person mode. Arg: partialTickTime
     */
    private void renderFireInFirstPerson() {
        if (MoonWare.featureManager.getFeatureByClass(NoRender.class).getState() && NoRender.fire.getBoolValue()) {
            return;
        }
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 0.9F);
        GlStateManager.depthFunc(519);
        GlStateManager.depthMask(false);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        float f = 1.0F;

        for (int i = 0; i < 2; ++i) {
            GlStateManager.pushMatrix();
            TextureAtlasSprite textureatlassprite = Minecraft.getTextureMapBlocks().getAtlasSprite("minecraft:blocks/fire_layer_1");
            Minecraft.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            float f1 = textureatlassprite.getMinU();
            float f2 = textureatlassprite.getMaxU();
            float f3 = textureatlassprite.getMinV();
            float f4 = textureatlassprite.getMaxV();
            float f5 = -0.5F;
            float f6 = 0.5F;
            float f7 = -0.5F;
            float f8 = 0.5F;
            float f9 = -0.5F;
            GlStateManager.translate((float) (-(i * 2 - 1)) * 0.24F, -0.3F, 0.0F);
            GlStateManager.rotate((float) (i * 2 - 1) * 10.0F, 0.0F, 1.0F, 0.0F);
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
            bufferbuilder.pos(-0.5D, -0.5D, -0.5D).tex(f2, f4).endVertex();
            bufferbuilder.pos(0.5D, -0.5D, -0.5D).tex(f1, f4).endVertex();
            bufferbuilder.pos(0.5D, 0.5D, -0.5D).tex(f1, f3).endVertex();
            bufferbuilder.pos(-0.5D, 0.5D, -0.5D).tex(f2, f3).endVertex();
            tessellator.draw();
            GlStateManager.popMatrix();
        }

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.disableBlend();
        GlStateManager.depthMask(true);
        GlStateManager.depthFunc(515);
    }

    public void updateEquippedItem() {
        prevEquippedProgressMainHand = equippedProgressMainHand;
        prevEquippedProgressOffHand = equippedProgressOffHand;
        EntityPlayerSP entityplayersp = Minecraft.player;
        ItemStack itemstack = entityplayersp.getHeldItemMainhand();
        ItemStack itemstack1 = entityplayersp.getHeldItemOffhand();

        if (entityplayersp.isRowingBoat()) {
            equippedProgressMainHand = MathHelper.clamp(equippedProgressMainHand - 0.4F, 0.0F, 1.0F);
            equippedProgressOffHand = MathHelper.clamp(equippedProgressOffHand - 0.4F, 0.0F, 1.0F);
        } else {
            float f = entityplayersp.getCooledAttackStrength(1.0F);

            if (Reflector.ForgeHooksClient_shouldCauseReequipAnimation.exists()) {
                boolean flag = Reflector.callBoolean(Reflector.ForgeHooksClient_shouldCauseReequipAnimation, itemStackMainHand, itemstack, entityplayersp.inventory.currentItem);
                boolean flag1 = Reflector.callBoolean(Reflector.ForgeHooksClient_shouldCauseReequipAnimation, itemStackOffHand, itemstack1, Integer.valueOf(-1));

                if (!flag && !Objects.equals(itemStackMainHand, itemstack)) {
                    itemStackMainHand = itemstack;
                }

                if (!flag && !Objects.equals(itemStackOffHand, itemstack1)) {
                    itemStackOffHand = itemstack1;
                }

                equippedProgressMainHand += MathHelper.clamp((!flag ? f * f * f : 0.0F) - equippedProgressMainHand, -0.4F, 0.4F);
                equippedProgressOffHand += MathHelper.clamp((float) (!flag1 ? 1 : 0) - equippedProgressOffHand, -0.4F, 0.4F);
            } else {
                equippedProgressMainHand += MathHelper.clamp((Objects.equals(itemStackMainHand, itemstack) ? f * f * f : 0.0F) - equippedProgressMainHand, -0.4F, 0.4F);
                equippedProgressOffHand += MathHelper.clamp((float) (Objects.equals(itemStackOffHand, itemstack1) ? 1 : 0) - equippedProgressOffHand, -0.4F, 0.4F);
            }
        }

        if (equippedProgressMainHand < 0.1F) {
            itemStackMainHand = itemstack;

            if (Config.isShaders()) {
                Shaders.setItemToRenderMain(itemStackMainHand);
            }
        }

        if (equippedProgressOffHand < 0.1F) {
            itemStackOffHand = itemstack1;

            if (Config.isShaders()) {
                Shaders.setItemToRenderOff(itemStackOffHand);
            }
        }
    }

    public void resetEquippedProgress(EnumHand hand) {
        if (hand == EnumHand.MAIN_HAND) {
            equippedProgressMainHand = 0.0F;
        } else {
            equippedProgressOffHand = 0.0F;
        }
    }
}
