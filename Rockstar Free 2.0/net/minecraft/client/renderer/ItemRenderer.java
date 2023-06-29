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
import net.minecraft.world.World;
import net.minecraft.world.storage.MapData;
import optifine.Config;
import optifine.DynamicLights;
import optifine.Reflector;
import optifine.ReflectorForge;
import ru.rockstar.Main;
import ru.rockstar.api.event.EventManager;
import ru.rockstar.api.event.event.EventTransformSideFirstPerson;
import ru.rockstar.api.utils.render.ClientHelper;
import ru.rockstar.client.features.impl.combat.KillAura;
import ru.rockstar.client.features.impl.visuals.Chams;
import ru.rockstar.client.features.impl.visuals.NoRender;
import ru.rockstar.client.features.impl.visuals.SwingAnimations;
import ru.rockstar.client.features.impl.visuals.ViewModel;

import org.lwjgl.opengl.GL11;
import shadersmod.client.Shaders;

import java.awt.Color;
import java.util.Objects;

public class ItemRenderer
{
    private static final ResourceLocation RES_MAP_BACKGROUND;
    private static final ResourceLocation RES_UNDERWATER_OVERLAY;
    private final Minecraft mc;
    private ItemStack itemStackMainHand;
    private ItemStack itemStackOffHand;
    private float equippedProgressMainHand;
    private float prevEquippedProgressMainHand;
    private float equippedProgressOffHand;
    private float prevEquippedProgressOffHand;
    private final RenderManager renderManager;
    private final RenderItem itemRenderer;
    private float spin;
    
    public ItemRenderer(final Minecraft mcIn) {
        this.itemStackMainHand = ItemStack.field_190927_a;
        this.itemStackOffHand = ItemStack.field_190927_a;
        this.mc = mcIn;
        this.renderManager = mcIn.getRenderManager();
        this.itemRenderer = mcIn.getRenderItem();
    }
    
    public void renderItem(final EntityLivingBase entityIn, final ItemStack heldStack, final ItemCameraTransforms.TransformType transform) {
        this.renderItemSide(entityIn, heldStack, transform, false);
    }
    
    public void renderItemSide(final EntityLivingBase entitylivingbaseIn, final ItemStack heldStack, final ItemCameraTransforms.TransformType transform, final boolean leftHanded) {
        if (!heldStack.isEmpty()) {
            final Item item = heldStack.getItem();
            final Block block = Block.getBlockFromItem(item);
            GlStateManager.pushMatrix();
            final boolean flag = this.itemRenderer.shouldRenderItemIn3D(heldStack) && block.getBlockLayer() == BlockRenderLayer.TRANSLUCENT;
            if (flag && (!Config.isShaders() || !Shaders.renderItemKeepDepthMask)) {
                GlStateManager.depthMask(false);
            }
            this.itemRenderer.renderItem(heldStack, entitylivingbaseIn, transform, leftHanded);
            if (flag) {
                GlStateManager.depthMask(true);
            }
            GlStateManager.popMatrix();
        }
    }
    
    private void rotateArroundXAndY(final float angle, final float angleY) {
        GlStateManager.pushMatrix();
        GlStateManager.rotate(angle, 1.0f, 0.0f, 0.0f);
        GlStateManager.rotate(angleY, 0.0f, 1.0f, 0.0f);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.popMatrix();
    }
    
    private void setLightmap() {
        final AbstractClientPlayer abstractclientplayer = this.mc.player;
        int i = this.mc.world.getCombinedLight(new BlockPos(abstractclientplayer.posX, abstractclientplayer.posY + abstractclientplayer.getEyeHeight(), abstractclientplayer.posZ), 0);
        if (Config.isDynamicLights()) {
            i = DynamicLights.getCombinedLight(this.mc.getRenderViewEntity(), i);
        }
        final float f = (float)(i & 0xFFFF);
        final float f2 = (float)(i >> 16);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, f, f2);
    }
    
    private void rotateArm(final float p_187458_1_) {
        final EntityPlayerSP entityplayersp = this.mc.player;
        final float f = entityplayersp.prevRenderArmPitch + (entityplayersp.renderArmPitch - entityplayersp.prevRenderArmPitch) * p_187458_1_;
        final float f2 = entityplayersp.prevRenderArmYaw + (entityplayersp.renderArmYaw - entityplayersp.prevRenderArmYaw) * p_187458_1_;
        GlStateManager.rotate((entityplayersp.rotationPitch - f) * 0.1f, 1.0f, 0.0f, 0.0f);
        GlStateManager.rotate((entityplayersp.rotationYaw - f2) * 0.1f, 0.0f, 1.0f, 0.0f);
    }
    
    private float getMapAngleFromPitch(final float pitch) {
        float f = 1.0f - pitch / 45.0f + 0.1f;
        f = MathHelper.clamp(f, 0.0f, 1.0f);
        f = -MathHelper.cos(f * 3.1415927f) * 0.5f + 0.5f;
        return f;
    }
    
    private void renderArms() {
        if (!this.mc.player.isInvisible()) {
            GlStateManager.disableCull();
            GlStateManager.pushMatrix();
            GlStateManager.rotate(90.0f, 0.0f, 1.0f, 0.0f);
            this.renderArm(EnumHandSide.RIGHT);
            this.renderArm(EnumHandSide.LEFT);
            GlStateManager.popMatrix();
            GlStateManager.enableCull();
        }
    }
    
    private void renderArm(final EnumHandSide p_187455_1_) {
        this.mc.getTextureManager().bindTexture(this.mc.player.getLocationSkin());
        final Render<AbstractClientPlayer> render = this.renderManager.getEntityRenderObject(this.mc.player);
        final RenderPlayer renderplayer = (RenderPlayer)render;
        GlStateManager.pushMatrix();
        final float f = (p_187455_1_ == EnumHandSide.RIGHT) ? 1.0f : -1.0f;
        GlStateManager.rotate(92.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(45.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.rotate(f * -41.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.translate(f * 0.3f, -1.1f, 0.45f);
        if (p_187455_1_ == EnumHandSide.RIGHT) {
            renderplayer.renderRightArm(this.mc.player);
        }
        else {
            renderplayer.renderLeftArm(this.mc.player);
        }
        GlStateManager.popMatrix();
    }
    
    private void renderMapFirstPersonSide(final float p_187465_1_, final EnumHandSide p_187465_2_, final float p_187465_3_, final ItemStack p_187465_4_) {
        final float f = (p_187465_2_ == EnumHandSide.RIGHT) ? 1.0f : -1.0f;
        GlStateManager.translate(f * 0.125f, -0.125f, 0.0f);
        if (!this.mc.player.isInvisible()) {
            GlStateManager.pushMatrix();
            GlStateManager.rotate(f * 10.0f, 0.0f, 0.0f, 1.0f);
            this.renderArmFirstPerson(p_187465_1_, p_187465_3_, p_187465_2_);
            GlStateManager.popMatrix();
        }
        GlStateManager.pushMatrix();
        GlStateManager.translate(f * 0.51f, -0.08f + p_187465_1_ * -1.2f, -0.75f);
        final float f2 = MathHelper.sqrt(p_187465_3_);
        final float f3 = MathHelper.sin(f2 * 3.1415927f);
        final float f4 = -0.5f * f3;
        final float f5 = 0.4f * MathHelper.sin(f2 * 6.2831855f);
        final float f6 = -0.3f * MathHelper.sin(p_187465_3_ * 3.1415927f);
        GlStateManager.translate(f * f4, f5 - 0.3f * f3, f6);
        GlStateManager.rotate(f3 * -45.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.rotate(f * f3 * -30.0f, 0.0f, 1.0f, 0.0f);
        this.renderMapFirstPerson(p_187465_4_);
        GlStateManager.popMatrix();
    }
    
    private void renderMapFirstPerson(final float p_187463_1_, final float p_187463_2_, final float p_187463_3_) {
        final float f = MathHelper.sqrt(p_187463_3_);
        final float f2 = -0.2f * MathHelper.sin(p_187463_3_ * 3.1415927f);
        final float f3 = -0.4f * MathHelper.sin(f * 3.1415927f);
        GlStateManager.translate(0.0f, -f2 / 2.0f, f3);
        final float f4 = this.getMapAngleFromPitch(p_187463_1_);
        GlStateManager.translate(0.0f, 0.04f + p_187463_2_ * -1.2f + f4 * -0.5f, -0.72f);
        GlStateManager.rotate(f4 * -85.0f, 1.0f, 0.0f, 0.0f);
        this.renderArms();
        final float f5 = MathHelper.sin(f * 3.1415927f);
        GlStateManager.rotate(f5 * 20.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.scale(2.0f, 2.0f, 2.0f);
        this.renderMapFirstPerson(this.itemStackMainHand);
    }
    
    private void renderMapFirstPerson(final ItemStack stack) {
        GlStateManager.rotate(180.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.scale(0.38f, 0.38f, 0.38f);
        GlStateManager.disableLighting();
        this.mc.getTextureManager().bindTexture(ItemRenderer.RES_MAP_BACKGROUND);
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.translate(-0.5f, -0.5f, 0.0f);
        GlStateManager.scale(0.0078125f, 0.0078125f, 0.0078125f);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(-7.0, 135.0, 0.0).tex(0.0, 1.0).endVertex();
        bufferbuilder.pos(135.0, 135.0, 0.0).tex(1.0, 1.0).endVertex();
        bufferbuilder.pos(135.0, -7.0, 0.0).tex(1.0, 0.0).endVertex();
        bufferbuilder.pos(-7.0, -7.0, 0.0).tex(0.0, 0.0).endVertex();
        tessellator.draw();
        final MapData mapdata = ReflectorForge.getMapData(Items.FILLED_MAP, stack, (World)this.mc.world);
        if (mapdata != null) {
            this.mc.entityRenderer.getMapItemRenderer().renderMap(mapdata, false);
        }
        GlStateManager.enableLighting();
    }
    
    private void renderArmFirstPerson(float p_187456_1_, float p_187456_2_, EnumHandSide p_187456_3_) {
        EventTransformSideFirstPerson event = new EventTransformSideFirstPerson(p_187456_3_);
        event.call();
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
        AbstractClientPlayer abstractclientplayer = this.mc.player;
        this.mc.getTextureManager().bindTexture(abstractclientplayer.getLocationSkin());
        GlStateManager.translate(f * -1.0F, 3.6F, 3.5F);
        GlStateManager.rotate(f * 120.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate(200.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(f * -135.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.translate(f * 5.6F, 0.0F, 0.0F);
        RenderPlayer renderplayer = (RenderPlayer) this.renderManager.<AbstractClientPlayer>getEntityRenderObject(abstractclientplayer);
        GlStateManager.disableCull();

        if (flag) {
            renderplayer.renderRightArm(abstractclientplayer);
        } else {
            renderplayer.renderLeftArm(abstractclientplayer);
        }

        GlStateManager.enableCull();
    }
    
    private void transformEatFirstPerson(float p_187454_1_, EnumHandSide p_187454_2_, ItemStack p_187454_3_) {
        int i;
        EventTransformSideFirstPerson event = new EventTransformSideFirstPerson(p_187454_2_);
        event.call();
        float f = (float)this.mc.player.getItemInUseCount() - p_187454_1_ + 1.0f;
        float f1 = f / (float)p_187454_3_.getMaxItemUseDuration();
        float f3 = 1.0f - (float)Math.pow((double)f1, (double)27.0);
        int n = i = p_187454_2_ == EnumHandSide.RIGHT ? 1 : -1;
        if (f1 < 0.8f) {
            float f2 = MathHelper.abs((float)(MathHelper.cos((float)(f / 4.0f * 3.1415927f)) * 0.1f));
            GlStateManager.translate((float)0.0f, (float)f2, (float)0.0f);
        }
        GlStateManager.translate((float)(f3 * 0.6f * (float)i), (float)(f3 * -0.5f), (float)(f3 * 0.0f));
        if (Main.instance.featureDirector.getFeatureByClass(ViewModel.class).isToggled()) {
            GlStateManager.rotate((float)((float)i * f3 * 20.0f), (float)0.0f, (float)1.0f, (float)0.0f);
        } else {
            GlStateManager.rotate((float)((float)i * f3 * 90.0f), (float)0.0f, (float)1.0f, (float)0.0f);
        }
        GlStateManager.rotate((float)(f3 * 10.0f), (float)1.0f, (float)0.0f, (float)0.0f);
        GlStateManager.rotate((float)((float)i * f3 * 30.0f), (float)0.0f, (float)0.0f, (float)1.0f);
    }

    
    private void transformFirstPerson(final EnumHandSide p_187453_1_, final float p_187453_2_) {
        final float angle = (float)(System.currentTimeMillis() / (int)SwingAnimations.item360Speed.getNumberValue() % 360L);
        final int i = (p_187453_1_ == EnumHandSide.RIGHT) ? 1 : -1;
        final float f = MathHelper.sin(p_187453_2_ * p_187453_2_ * 3.1415927f);
        GlStateManager.rotate(i * (45.0f + f * -20.0f), 0.0f, 1.0f, 0.0f);
        final float f2 = MathHelper.sin(MathHelper.sqrt(p_187453_2_) * 3.1415927f);
        GlStateManager.rotate(i * f2 * -20.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.rotate(f2 * -80.0f, 1.0f, 0.0f, 0.0f);
        if (Main.instance.featureDirector.getFeatureByClass(SwingAnimations.class).isToggled() && SwingAnimations.item360.getBoolValue()) {
            if ((SwingAnimations.item360Hand.currentMode.equals("Left") && p_187453_1_ != EnumHandSide.LEFT) || (SwingAnimations.item360Hand.currentMode.equals("Right") && p_187453_1_ != EnumHandSide.RIGHT && !SwingAnimations.item360Hand.currentMode.equals("All"))) {
                return;
            }
            GlStateManager.rotate(angle, 0.0f, SwingAnimations.item360Mode.currentMode.equals("Horizontal") ? 1.0f : 0.0f, SwingAnimations.item360Mode.currentMode.equals("Vertical") ? angle : 0.0f);
        }
        else {
            GlStateManager.rotate(i * -45.0f, 0.0f, 1.0f, 0.0f);
        }
        GlStateManager.translate(0.0f, 0.02f, 0.0f);
    }
    
    private void transformSideFirstPerson(EnumHandSide p_187459_1_, float p_187459_2_) {
        EventTransformSideFirstPerson event = new EventTransformSideFirstPerson(p_187459_1_);
        event.call();
        int i = p_187459_1_ == EnumHandSide.RIGHT ? 1 : -1;
        GlStateManager.translate((float)((float)i * 0.56f), (float)(-0.52f + p_187459_2_ * -0.6f), (float)-0.72f);
    }

    
    private void translate() {
        GlStateManager.translate(-0.5f, 0.08f, 0.0f);
        GlStateManager.rotate(20.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(-80.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.rotate(20.0f, 0.0f, 1.0f, 0.0f);
    }
    
    public void renderItemInFirstPerson(final float partialTicks) {
        final AbstractClientPlayer abstractclientplayer = this.mc.player;
        final float f = abstractclientplayer.getSwingProgress(partialTicks);
        final EnumHand enumhand = (EnumHand)MoreObjects.firstNonNull((Object)abstractclientplayer.swingingHand, (Object)EnumHand.MAIN_HAND);
        final float f2 = abstractclientplayer.prevRotationPitch + (abstractclientplayer.rotationPitch - abstractclientplayer.prevRotationPitch) * partialTicks;
        final float f3 = abstractclientplayer.prevRotationYaw + (abstractclientplayer.rotationYaw - abstractclientplayer.prevRotationYaw) * partialTicks;
        boolean flag = true;
        boolean flag2 = true;
        if (abstractclientplayer.isHandActive()) {
            final ItemStack itemstack = abstractclientplayer.getActiveItemStack();
            if (!itemstack.isEmpty() && itemstack.getItem() == Items.BOW) {
                final EnumHand enumhand2 = abstractclientplayer.getActiveHand();
                flag = (enumhand2 == EnumHand.MAIN_HAND);
                flag2 = !flag;
            }
        }
        this.rotateArroundXAndY(f2, f3);
        this.setLightmap();
        this.rotateArm(partialTicks);
        GlStateManager.enableRescaleNormal();
        if (flag) {
            final float f4 = (enumhand == EnumHand.MAIN_HAND) ? f : 0.0f;
            final float f5 = 1.0f - (this.prevEquippedProgressMainHand + (this.equippedProgressMainHand - this.prevEquippedProgressMainHand) * partialTicks);
            if (!Reflector.ForgeHooksClient_renderSpecificFirstPersonHand.exists() || !Reflector.callBoolean(Reflector.ForgeHooksClient_renderSpecificFirstPersonHand, new Object[] { EnumHand.MAIN_HAND, partialTicks, f2, f4, f5, this.itemStackMainHand })) {
                this.renderItemInFirstPerson(abstractclientplayer, partialTicks, f2, EnumHand.MAIN_HAND, f4, this.itemStackMainHand, f5);
            }
        }
        if (flag2) {
            final float f6 = (enumhand == EnumHand.OFF_HAND) ? f : 0.0f;
            final float f7 = 1.0f - (this.prevEquippedProgressOffHand + (this.equippedProgressOffHand - this.prevEquippedProgressOffHand) * partialTicks);
            if (!Reflector.ForgeHooksClient_renderSpecificFirstPersonHand.exists() || !Reflector.callBoolean(Reflector.ForgeHooksClient_renderSpecificFirstPersonHand, new Object[] { EnumHand.OFF_HAND, partialTicks, f2, f6, f7, this.itemStackOffHand })) {
                this.renderItemInFirstPerson(abstractclientplayer, partialTicks, f2, EnumHand.OFF_HAND, f6, this.itemStackOffHand, f7);
            }
        }
        GlStateManager.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
    }
    
    private void transformFirstPersonItem(final float equipProgress, final float swingProgress) {
        GlStateManager.translate(0.56f, -0.44f, -0.71999997f);
        GlStateManager.translate(0.0f, equipProgress * -0.6f, 0.0f);
        GlStateManager.rotate(45.0f, 0.0f, 1.0f, 0.0f);
        final float f = MathHelper.sin(swingProgress * swingProgress * 3.1415927f);
        final float f2 = MathHelper.sin(MathHelper.sqrt(swingProgress) * 3.1415927f);
        GlStateManager.rotate(f * -20.0f, 0.0f, 0.0f, 0.0f);
        GlStateManager.rotate(f2 * -20.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.rotate(f2 * -80.0f, 0.01f, 0.0f, 0.0f);
        GlStateManager.translate(0.4f, 0.2f, 0.2f);
    }
    
    public void renderItemInFirstPerson(final AbstractClientPlayer p_187457_1_, final float p_187457_2_, final float p_187457_3_, final EnumHand p_187457_4_, final float p_187457_5_, final ItemStack p_187457_6_, final float p_187457_7_) {
        if (!Config.isShaders() || !Shaders.isSkipRenderHand(p_187457_4_)) {
            final boolean flag = p_187457_4_ == EnumHand.MAIN_HAND;
            final EnumHandSide enumhandside = flag ? p_187457_1_.getPrimaryHand() : p_187457_1_.getPrimaryHand().opposite();
            GlStateManager.pushMatrix();
            if (p_187457_6_.isEmpty()) {
                if (flag && !p_187457_1_.isInvisible()) {
                    this.renderArmFirstPerson(p_187457_7_, p_187457_5_, enumhandside);
                }
            }
            else if (p_187457_6_.getItem() instanceof ItemMap) {
                if (flag && this.itemStackOffHand.isEmpty()) {
                    this.renderMapFirstPerson(p_187457_3_, p_187457_7_, p_187457_5_);
                }
                else {
                    this.renderMapFirstPersonSide(p_187457_7_, enumhandside, p_187457_5_, p_187457_6_);
                }
            }
            else {
                final boolean flag2 = enumhandside == EnumHandSide.RIGHT;
                if (p_187457_1_.isHandActive() && p_187457_1_.getItemInUseCount() > 0 && p_187457_1_.getActiveHand() == p_187457_4_) {
                    final int j = flag2 ? 1 : -1;
                    switch (p_187457_6_.getItemUseAction()) {
                        case NONE: {
                            this.transformSideFirstPerson(enumhandside, p_187457_7_);
                            break;
                        }
                        case EAT:
                        case DRINK: {
                            this.transformEatFirstPerson(p_187457_2_, enumhandside, p_187457_6_);
                            this.transformSideFirstPerson(enumhandside, p_187457_7_);
                            break;
                        }
                        case BLOCK: {
                            this.transformSideFirstPerson(enumhandside, p_187457_7_);
                            break;
                        }
                        case BOW: {
                            this.transformSideFirstPerson(enumhandside, p_187457_7_);
                            GlStateManager.translate(j * -0.2785682f, 0.18344387f, 0.15731531f);
                            GlStateManager.rotate(-13.935f, 1.0f, 0.0f, 0.0f);
                            GlStateManager.rotate(j * 35.3f, 0.0f, 1.0f, 0.0f);
                            GlStateManager.rotate(j * -9.785f, 0.0f, 0.0f, 1.0f);
                            final float f5 = p_187457_6_.getMaxItemUseDuration() - (this.mc.player.getItemInUseCount() - p_187457_2_ + 1.0f);
                            float f6 = f5 / 20.0f;
                            f6 = (f6 * f6 + f6 * 2.0f) / 3.0f;
                            if (f6 > 1.0f) {
                                f6 = 1.0f;
                            }
                            if (f6 > 0.1f) {
                                final float f7 = MathHelper.sin((f5 - 0.1f) * 1.3f);
                                final float f8 = f6 - 0.1f;
                                final float f9 = f7 * f8;
                                GlStateManager.translate(f9 * 0.0f, f9 * 0.004f, f9 * 0.0f);
                            }
                            GlStateManager.translate(f6 * 0.0f, f6 * 0.0f, f6 * 0.04f);
                            GlStateManager.scale(1.0f, 1.0f, 1.0f + f6 * 0.2f);
                            GlStateManager.rotate(j * 45.0f, 0.0f, -1.0f, 0.0f);
                            break;
                        }
                    }
                }
                else {
                    final float f10 = -0.4f * MathHelper.sin(MathHelper.sqrt(p_187457_5_) * 3.1415927f);
                    final float f11 = 0.2f * MathHelper.sin(MathHelper.sqrt(p_187457_5_) * 6.2831855f);
                    float f12 = -0.2f * MathHelper.sin(p_187457_5_ * 3.1415927f);
                    final int i = flag2 ? 1 : -1;
                    final float equipProgress = 1.0f - (this.prevEquippedProgressMainHand + (this.equippedProgressMainHand - this.prevEquippedProgressMainHand) * p_187457_2_);
                    final float swingprogress = this.mc.player.getSwingProgress(p_187457_2_);
                    final String mode = SwingAnimations.swordAnim.getCurrentMode();
                    if (Main.instance.featureDirector.getFeatureByClass(SwingAnimations.class).isToggled() && ((this.mc.gameSettings.keyBindAttack.pressed && !SwingAnimations.auraOnly.getBoolValue()) || KillAura.target != null)) {
                        if (Main.instance.featureDirector.getFeatureByClass(SwingAnimations.class).isToggled() && ((this.mc.gameSettings.keyBindAttack.pressed && !SwingAnimations.auraOnly.getBoolValue()) || KillAura.target != null)) {
                            if (enumhandside != (this.mc.gameSettings.mainHand.equals(EnumHandSide.LEFT) ? EnumHandSide.RIGHT : EnumHandSide.LEFT)) {
                                final float smooth = swingprogress * 0.8f - swingprogress * swingprogress * 0.8f;
                                if (mode.equalsIgnoreCase("Celestial")) {
                                    this.transformFirstPersonItem(equipProgress / 3.0f, swingprogress);
                                    this.translate();
                                }
                                else if (mode.equalsIgnoreCase("Spin")) {
                                    this.transformFirstPersonItem(0.0f, 0.0f);
                                    this.translate();
                                    GlStateManager.rotate(this.spin * SwingAnimations.spinSpeed.getNumberValue(), this.spin, 0.0f, this.spin);
                                    ++this.spin;
                                }
                                else if (mode.equalsIgnoreCase("Fap")) {
                                    GlStateManager.translate(0.96f, -0.02f, -0.71999997f);
                                    GlStateManager.translate(0.0f, -0.0f, 0.0f);
                                    GlStateManager.rotate(45.0f, 0.0f, 1.0f, 0.0f);
                                    final float var3 = MathHelper.sin(0.0f);
                                    final float var4 = MathHelper.sin(MathHelper.sqrt(0.0f) * 3.1415927f);
                                    GlStateManager.rotate(var3 * -20.0f, 0.0f, 1.0f, 0.0f);
                                    GlStateManager.rotate(var4 * -20.0f, 0.0f, 0.0f, 1.0f);
                                    GlStateManager.rotate(var4 * -80.0f, 1.0f, 0.0f, 0.0f);
                                    GlStateManager.translate(-0.5f, 0.2f, 0.0f);
                                    GlStateManager.rotate(30.0f, 0.0f, 1.0f, 0.0f);
                                    GlStateManager.rotate(-80.0f, 1.0f, 0.0f, 0.0f);
                                    GlStateManager.rotate(60.0f, 0.0f, 1.0f, 0.0f);
                                    final int alpha = (int)Math.min(255L, ((System.currentTimeMillis() % 255L > 127L) ? Math.abs(Math.abs(System.currentTimeMillis()) % 255L - 255L) : (System.currentTimeMillis() % 255L)) * 2L);
                                    final float f13 = (f11 > 0.5) ? (1.0f - f11) : f11;
                                    GlStateManager.translate(0.3f, -0.0f, 0.4f);
                                    GlStateManager.rotate(0.0f, 0.0f, 0.0f, 1.0f);
                                    GlStateManager.translate(0.0f, 0.5f, 0.0f);
                                    GlStateManager.rotate(90.0f, 1.0f, 0.0f, -1.0f);
                                    GlStateManager.translate(0.6f, 0.5f, 0.0f);
                                    GlStateManager.rotate(-90.0f, 1.0f, 0.0f, -1.0f);
                                    GlStateManager.rotate(-10.0f, 1.0f, 0.0f, -1.0f);
                                    GlStateManager.rotate(-f13 * 10.0f, 10.0f, 10.0f, -9.0f);
                                    GlStateManager.rotate(10.0f, -1.0f, 0.0f, 0.0f);
                                    GlStateManager.translate(0.0, 0.0, -0.5);
                                    GlStateManager.rotate(this.mc.player.isSwingInProgress ? (-alpha / SwingAnimations.fapSmooth.getNumberValue()) : 1.0f, 1.0f, -0.0f, 1.0f);
                                    GlStateManager.translate(0.0, 0.0, 0.5);
                                }
                                else if (mode.equalsIgnoreCase("Jello")) {
                                    GlStateManager.translate(0.56f, -0.52f, -0.71999997f);
                                    GlStateManager.translate(0.0f, equipProgress * -0.15f, 0.0f);
                                    GlStateManager.rotate(45.0f, 0.0f, 1.0f, 0.0f);
                                    GlStateManager.rotate(smooth * -90.0f, 0.0f, 1.0f, 0.0f);
                                    GlStateManager.scale(0.37f, 0.37f, 0.37f);
                                    GlStateManager.translate(-0.5f, 1.0f, 0.0f);
                                    GlStateManager.rotate(30.0f, 0.0f, 1.0f, 0.0f);
                                    GlStateManager.rotate(-80.0f, 1.0f, 0.0f, 0.0f);
                                    GlStateManager.rotate(60.0f, 0.0f, 1.0f, 0.0f);
                                }
                                else if (mode.equalsIgnoreCase("Sigma")) {
                                    final float sigma = MathHelper.sin(MathHelper.sqrt(swingprogress) * 3.1415927f);
                                    GlStateManager.translate(0.56f, -0.42f, -0.71999997f);
                                    GlStateManager.translate(0.0f, equipProgress * 0.5f * -0.6f, 0.0f);
                                    GlStateManager.rotate(45.0f, 0.0f, 1.0f, 0.0f);
                                    final float sigmaHatar1 = MathHelper.sin(0.0f);
                                    final float sigmaHatar2 = MathHelper.sin(MathHelper.sqrt(0.0f) * 3.1415927f);
                                    GlStateManager.rotate(sigmaHatar1 * -20.0f, 0.0f, 1.0f, 0.0f);
                                    GlStateManager.rotate(sigmaHatar2 * -20.0f, 0.0f, 0.0f, 1.0f);
                                    GlStateManager.rotate(sigmaHatar2 * -80.0f, 1.0f, 0.0f, 0.0f);
                                    GlStateManager.scale(0.4f, 0.4f, 0.4f);
                                    GlStateManager.rotate(-sigma * 55.0f / 2.0f, -8.0f, -0.0f, 9.0f);
                                    GlStateManager.rotate(-sigma * 45.0f, 1.0f, sigma / 2.0f, -0.0f);
                                    GlStateManager.translate(-0.5f, 0.2f, 0.0f);
                                    GlStateManager.rotate(30.0f, 0.0f, 1.0f, 0.0f);
                                    GlStateManager.rotate(-80.0f, 1.0f, 0.0f, 0.0f);
                                    GlStateManager.rotate(60.0f, 0.0f, 1.0f, 0.0f);
                                    GL11.glTranslated(1.2, 0.3, 0.5);
                                    GL11.glTranslatef(-1.0f, this.mc.player.isSneaking() ? -0.1f : -0.2f, 0.2f);
                                    GlStateManager.scale(1.2f, 1.2f, 1.2f);
                                }
                                else if (mode.equalsIgnoreCase("Swank")) {
                                    GlStateManager.translate(0.56f, -0.52f, -0.9999997f);
                                    GlStateManager.translate(0.0f, 0.0f, 0.0f);
                                    this.transformFirstPersonItem(equipProgress / 2.0f, swingprogress);
                                    GlStateManager.translate(-0.7f, 0.2f, 0.0f);
                                    f12 = MathHelper.sin(MathHelper.sqrt(swingprogress) * 3.1415927f);
                                    GlStateManager.rotate(f12 * 30.0f, -f12, -0.0f, 9.0f);
                                    GlStateManager.rotate(f12 * 40.0f, 1.0f, -f12, -0.0f);
                                }
                                else if (mode.equalsIgnoreCase("Astolfo")) {
                                    GlStateManager.rotate((float)(System.currentTimeMillis() / 16L * (int)SwingAnimations.spinSpeed.getNumberValue() % 360L), 0.0f, 0.0f, -0.1f);
                                    this.transformFirstPersonItem(0.0f, 0.0f);
                                    this.translate();
                                }
                                else if (mode.equalsIgnoreCase("Big")) {
                                    GlStateManager.translate(0.56f, -0.52f, -0.71999997f);
                                    GlStateManager.translate(0.0f, 0.0f, 0.0f);
                                    GlStateManager.rotate(45.0f, 0.0f, 1.0f, 0.0f);
                                    final float sin1 = MathHelper.sin(swingprogress * swingprogress * 3.1415927f);
                                    final float sin2 = MathHelper.sin(MathHelper.sqrt(swingprogress) * 3.1415927f);
                                    GlStateManager.rotate(sin1 * -20.0f, 0.0f, 1.0f, 0.0f);
                                    GlStateManager.rotate(sin2 * -20.0f, 0.0f, 0.0f, 1.0f);
                                    GlStateManager.rotate(sin2 * -40.0f, 1.0f, 0.0f, 0.0f);
                                    GlStateManager.scale(0.8f, 0.8f, 0.8f);
                                    GlStateManager.translate(-0.5f, 0.2f, 0.0f);
                                    GlStateManager.rotate(30.0f, 0.0f, 1.0f, 0.0f);
                                    GlStateManager.rotate(-80.0f, 1.0f, 0.0f, 0.0f);
                                    GlStateManager.rotate(60.0f, 0.0f, 1.0f, 0.0f);
                                }
                                else if (mode.equalsIgnoreCase("Neutral")) {
                                    this.transformFirstPersonItem(0.0f, 0.0f);
                                    this.translate();
                                }
                                else if (mode.equalsIgnoreCase("Custom")) {
                                    this.transformFirstPersonItem(0.0f, 0.0f);
                                    if (this.mc.player.isSwingInProgress) {
                                        GlStateManager.translate(SwingAnimations.SwingX.getNumberValue(), SwingAnimations.SwingY.getNumberValue(), SwingAnimations.SwingZ.getNumberValue());
                                        GlStateManager.rotate(SwingAnimations.SwingAngle.getNumberValue(), SwingAnimations.SwingRotate1.getNumberValue(), SwingAnimations.SwingRotate2.getNumberValue(), SwingAnimations.SwingRotate3.getNumberValue());
                                        GlStateManager.scale(SwingAnimations.scale.getNumberValue(), SwingAnimations.scale.getNumberValue(), SwingAnimations.scale.getNumberValue());
                                    }
                                    else {
                                        GlStateManager.translate(SwingAnimations.x.getNumberValue(), SwingAnimations.y.getNumberValue(), SwingAnimations.z.getNumberValue());
                                        GlStateManager.rotate(SwingAnimations.angle.getNumberValue(), SwingAnimations.rotate1.getNumberValue(), SwingAnimations.rotate2.getNumberValue(), SwingAnimations.rotate3.getNumberValue());
                                        GlStateManager.scale(SwingAnimations.scale.getNumberValue(), SwingAnimations.scale.getNumberValue(), SwingAnimations.scale.getNumberValue());
                                    }
                                }
                                if (Main.instance.featureDirector.getFeatureByClass(ViewModel.class).isToggled()) {
                                    GlStateManager.scale(ViewModel.mainScaleX.getNumberValue(), ViewModel.mainScaleY.getNumberValue(), ViewModel.mainScaleZ.getNumberValue());
                                }
                                else if (Main.instance.featureDirector.getFeatureByClass(SwingAnimations.class).isToggled() && SwingAnimations.smallItem.getBoolValue()) {
                                    GlStateManager.scale(SwingAnimations.smallItemSize.getNumberValue(), SwingAnimations.smallItemSize.getNumberValue(), SwingAnimations.smallItemSize.getNumberValue());
                                }
                            }
                            else {
                                GlStateManager.translate(i * f10, f11, f12);
                                this.transformSideFirstPerson(enumhandside, p_187457_7_);
                                this.transformFirstPerson(enumhandside, p_187457_5_);
                            }
                        }
                        else {
                            this.transformSideFirstPerson(enumhandside, p_187457_7_);
                            this.transformFirstPerson(enumhandside, p_187457_5_);
                        }
                    }
                    else {
                        GlStateManager.translate(i * f10, f11, f12);
                        this.transformSideFirstPerson(enumhandside, p_187457_7_);
                        this.transformFirstPerson(enumhandside, p_187457_5_);
                    }
                }
                this.renderItemSide(p_187457_1_, p_187457_6_, flag2 ? ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND : ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND, !flag2);
            }
            GlStateManager.popMatrix();
        }
    }
    
    public void renderOverlays(final float partialTicks) {
        GlStateManager.disableAlpha();
        if (this.mc.player.isEntityInsideOpaqueBlock()) {
            IBlockState iblockstate = this.mc.world.getBlockState(new BlockPos(this.mc.player));
            BlockPos blockpos = new BlockPos(this.mc.player);
            final EntityPlayer entityplayer = this.mc.player;
            for (int i = 0; i < 8; ++i) {
                final double d0 = entityplayer.posX + ((i >> 0) % 2 - 0.5f) * entityplayer.width * 0.8f;
                final double d2 = entityplayer.posY + ((i >> 1) % 2 - 0.5f) * 0.1f;
                final double d3 = entityplayer.posZ + ((i >> 2) % 2 - 0.5f) * entityplayer.width * 0.8f;
                final BlockPos blockpos2 = new BlockPos(d0, d2 + entityplayer.getEyeHeight(), d3);
                final IBlockState iblockstate2 = this.mc.world.getBlockState(blockpos2);
                if (iblockstate2.func_191058_s()) {
                    iblockstate = iblockstate2;
                    blockpos = blockpos2;
                }
            }
            if (iblockstate.getRenderType() != EnumBlockRenderType.INVISIBLE) {
                final Object object = Reflector.getFieldValue(Reflector.RenderBlockOverlayEvent_OverlayType_BLOCK);
                if (!Reflector.callBoolean(Reflector.ForgeEventFactory_renderBlockOverlay, new Object[] { this.mc.player, partialTicks, object, iblockstate, blockpos })) {
                    this.renderBlockInHand(this.mc.getBlockRendererDispatcher().getBlockModelShapes().getTexture(iblockstate));
                }
            }
        }
        if (!this.mc.player.isSpectator()) {
            if (this.mc.player.isInsideOfMaterial(Material.WATER) && !Reflector.callBoolean(Reflector.ForgeEventFactory_renderWaterOverlay, new Object[] { this.mc.player, partialTicks })) {
                this.renderWaterOverlayTexture(partialTicks);
            }
            if (this.mc.player.isBurning() && !Reflector.callBoolean(Reflector.ForgeEventFactory_renderFireOverlay, new Object[] { this.mc.player, partialTicks })) {
                this.renderFireInFirstPerson();
            }
        }
        GlStateManager.enableAlpha();
    }
    
    private void renderBlockInHand(final TextureAtlasSprite partialTicks) {
        this.mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();
        final float f = 0.1f;
        GlStateManager.color(0.1f, 0.1f, 0.1f, 0.5f);
        GlStateManager.pushMatrix();
        final float f2 = -1.0f;
        final float f3 = 1.0f;
        final float f4 = -1.0f;
        final float f5 = 1.0f;
        final float f6 = -0.5f;
        final float f7 = partialTicks.getMinU();
        final float f8 = partialTicks.getMaxU();
        final float f9 = partialTicks.getMinV();
        final float f10 = partialTicks.getMaxV();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(-1.0, -1.0, -0.5).tex(f8, f10).endVertex();
        bufferbuilder.pos(1.0, -1.0, -0.5).tex(f7, f10).endVertex();
        bufferbuilder.pos(1.0, 1.0, -0.5).tex(f7, f9).endVertex();
        bufferbuilder.pos(-1.0, 1.0, -0.5).tex(f8, f9).endVertex();
        tessellator.draw();
        GlStateManager.popMatrix();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
    }
    
    private void renderWaterOverlayTexture(final float partialTicks) {
        if (!Config.isShaders() || Shaders.isUnderwaterOverlay()) {
            this.mc.getTextureManager().bindTexture(ItemRenderer.RES_UNDERWATER_OVERLAY);
            final Tessellator tessellator = Tessellator.getInstance();
            final BufferBuilder bufferbuilder = tessellator.getBuffer();
            final float f = this.mc.player.getBrightness();
            GlStateManager.color(f, f, f, 0.5f);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.pushMatrix();
            final float f2 = 4.0f;
            final float f3 = -1.0f;
            final float f4 = 1.0f;
            final float f5 = -1.0f;
            final float f6 = 1.0f;
            final float f7 = -0.5f;
            final float f8 = -this.mc.player.rotationYaw / 64.0f;
            final float f9 = this.mc.player.rotationPitch / 64.0f;
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
            bufferbuilder.pos(-1.0, -1.0, -0.5).tex(4.0f + f8, 4.0f + f9).endVertex();
            bufferbuilder.pos(1.0, -1.0, -0.5).tex(0.0f + f8, 4.0f + f9).endVertex();
            bufferbuilder.pos(1.0, 1.0, -0.5).tex(0.0f + f8, 0.0f + f9).endVertex();
            bufferbuilder.pos(-1.0, 1.0, -0.5).tex(4.0f + f8, 0.0f + f9).endVertex();
            tessellator.draw();
            GlStateManager.popMatrix();
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.disableBlend();
        }
    }
    
    private void renderFireInFirstPerson() {
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();
        if (!NoRender.noFire.getBoolValue()) {
        	GlStateManager.color(1.0f, 1.0f, 1.0f, 0.9f);
            GlStateManager.depthFunc(519);
            GlStateManager.depthMask(false);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            final float f = 1.0f;
            for (int i = 0; i < 2; ++i) {
                GlStateManager.pushMatrix();
                final TextureAtlasSprite textureatlassprite = this.mc.getTextureMapBlocks().getAtlasSprite("minecraft:blocks/fire_layer_1");
                this.mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
                final float f2 = textureatlassprite.getMinU();
                final float f3 = textureatlassprite.getMaxU();
                final float f4 = textureatlassprite.getMinV();
                final float f5 = textureatlassprite.getMaxV();
                final float f6 = -0.5f;
                final float f7 = 0.5f;
                final float f8 = -0.5f;
                final float f9 = 0.5f;
                final float f10 = -0.5f;
                GlStateManager.translate(-(i * 2 - 1) * 0.24f, -0.3f, 0.0f);
                GlStateManager.rotate((i * 2 - 1) * 10.0f, 0.0f, 1.0f, 0.0f);
                bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
                bufferbuilder.pos(-0.5, -0.5, -0.5).tex(f3, f5).endVertex();
                bufferbuilder.pos(0.5, -0.5, -0.5).tex(f2, f5).endVertex();
                bufferbuilder.pos(0.5, 0.5, -0.5).tex(f2, f4).endVertex();
                bufferbuilder.pos(-0.5, 0.5, -0.5).tex(f3, f4).endVertex();
                tessellator.draw();
                GlStateManager.popMatrix();
            }
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.disableBlend();
            GlStateManager.depthMask(true);
            GlStateManager.depthFunc(515);
        }
    }
    
    public void updateEquippedItem() {
        this.prevEquippedProgressMainHand = this.equippedProgressMainHand;
        this.prevEquippedProgressOffHand = this.equippedProgressOffHand;
        final EntityPlayerSP entityplayersp = this.mc.player;
        final ItemStack itemstack = entityplayersp.getHeldItemMainhand();
        final ItemStack itemstack2 = entityplayersp.getHeldItemOffhand();
        if (entityplayersp.isRowingBoat()) {
            this.equippedProgressMainHand = MathHelper.clamp(this.equippedProgressMainHand - 0.4f, 0.0f, 1.0f);
            this.equippedProgressOffHand = MathHelper.clamp(this.equippedProgressOffHand - 0.4f, 0.0f, 1.0f);
        }
        else {
            final float f = entityplayersp.getCooledAttackStrength(1.0f);
            if (Reflector.ForgeHooksClient_shouldCauseReequipAnimation.exists()) {
                final boolean flag = Reflector.callBoolean(Reflector.ForgeHooksClient_shouldCauseReequipAnimation, new Object[] { this.itemStackMainHand, itemstack, entityplayersp.inventory.currentItem });
                final boolean flag2 = Reflector.callBoolean(Reflector.ForgeHooksClient_shouldCauseReequipAnimation, new Object[] { this.itemStackOffHand, itemstack2, -1 });
                if (!flag && !Objects.equals(this.itemStackMainHand, itemstack)) {
                    this.itemStackMainHand = itemstack;
                }
                if (!flag && !Objects.equals(this.itemStackOffHand, itemstack2)) {
                    this.itemStackOffHand = itemstack2;
                }
                this.equippedProgressMainHand += MathHelper.clamp((flag ? 0.0f : (f * f * f)) - this.equippedProgressMainHand, -0.4f, 0.4f);
                this.equippedProgressOffHand += MathHelper.clamp((float)(flag2 ? 0 : 1) - this.equippedProgressOffHand, -0.4f, 0.4f);
            }
            else {
                this.equippedProgressMainHand += MathHelper.clamp((Objects.equals(this.itemStackMainHand, itemstack) ? (f * f * f) : 0.0f) - this.equippedProgressMainHand, -0.4f, 0.4f);
                this.equippedProgressOffHand += MathHelper.clamp((float)(Objects.equals(this.itemStackOffHand, itemstack2) ? 1 : 0) - this.equippedProgressOffHand, -0.4f, 0.4f);
            }
        }
        if (this.equippedProgressMainHand < 0.1f) {
            this.itemStackMainHand = itemstack;
            if (Config.isShaders()) {
                Shaders.setItemToRenderMain(this.itemStackMainHand);
            }
        }
        if (this.equippedProgressOffHand < 0.1f) {
            this.itemStackOffHand = itemstack2;
            if (Config.isShaders()) {
                Shaders.setItemToRenderOff(this.itemStackOffHand);
            }
        }
    }
    
    public void resetEquippedProgress(final EnumHand hand) {
        if (hand == EnumHand.MAIN_HAND) {
            this.equippedProgressMainHand = 0.0f;
        }
        else {
            this.equippedProgressOffHand = 0.0f;
        }
    }
    
    static {
        RES_MAP_BACKGROUND = new ResourceLocation("textures/map/map_background.png");
        RES_UNDERWATER_OVERLAY = new ResourceLocation("textures/misc/underwater.png");
    }
}
