package net.minecraft.client.renderer.entity.layers;

import com.google.common.collect.Maps;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Namespaced;
import optifine.Config;
import optifine.CustomItems;
import optifine.Reflector;
import optifine.ReflectorForge;
import org.moonware.client.MoonWare;
import org.moonware.client.feature.impl.visual.CustomModel;
import org.moonware.client.feature.impl.visual.EnchantmentColor;
import org.moonware.client.feature.impl.visual.NoRender;
import org.moonware.client.helpers.misc.ClientHelper;
import org.moonware.client.helpers.palette.PaletteHelper;
import shadersmod.client.Shaders;
import shadersmod.client.ShadersRender;

import java.awt.*;
import java.util.Map;

public abstract class LayerArmorBase<T extends ModelBase>
        implements LayerRenderer<EntityLivingBase> {
    protected static final Namespaced ENCHANTED_ITEM_GLINT_RES = new Namespaced("textures/misc/enchanted_item_glint.png");
    protected T modelLeggings;
    protected T modelArmor;
    private final RenderLivingBase<?> renderer;
    private float alpha = 1.0f;
    private float colorR = 1.0f;
    private float colorG = 1.0f;
    private float colorB = 1.0f;
    private boolean skipRenderGlint;
    private static final Map<String, Namespaced> ARMOR_TEXTURE_RES_MAP = Maps.newHashMap();

    public LayerArmorBase(RenderLivingBase<?> rendererIn) {
        renderer = rendererIn;
        initArmor();
    }

    @Override
    public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if ((!CustomModel.onlyMe.getCurrentValue() || entitylivingbaseIn == Minecraft.player || MoonWare.friendManager.isFriend(entitylivingbaseIn.getName()) && CustomModel.friends.getCurrentValue()) && MoonWare.featureManager.getFeatureByClass(CustomModel.class).getState() && (CustomModel.modelMode.currentMode.equals("Crazy Rabbit") || CustomModel.modelMode.currentMode.equals("2D Leon") || CustomModel.modelMode.currentMode.equals("Sonic") || CustomModel.modelMode.currentMode.equals("CupHead") || CustomModel.modelMode.currentMode.equals("Freddy Bear") || CustomModel.modelMode.currentMode.equals("Chinchilla") || CustomModel.modelMode.currentMode.equals("Freddy Bear") || CustomModel.modelMode.currentMode.equals("Amogus") || CustomModel.modelMode.currentMode.equals("Red Panda") || CustomModel.modelMode.currentMode.equals("Demon") || CustomModel.modelMode.currentMode.equals("SirenHead") || CustomModel.modelMode.currentMode.equals("Jeff Killer") || CustomModel.modelMode.currentMode.equals("Crab") || CustomModel.modelMode.currentMode.equals("2D Leon"))) {
            return;
        }
        renderArmorLayer(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, EntityEquipmentSlot.CHEST);
        renderArmorLayer(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, EntityEquipmentSlot.LEGS);
        renderArmorLayer(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, EntityEquipmentSlot.FEET);
        renderArmorLayer(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, EntityEquipmentSlot.HEAD);
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }

    private void renderArmorLayer(EntityLivingBase entityLivingBaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale, EntityEquipmentSlot slotIn) {
        ItemArmor itemarmor;
        if (MoonWare.featureManager.getFeatureByClass(NoRender.class).getState() && NoRender.armor.getCurrentValue()) {
            return;
        }
        ItemStack itemstack = entityLivingBaseIn.getItemStackFromSlot(slotIn);
        if (itemstack.getItem() instanceof ItemArmor && (itemarmor = (ItemArmor)itemstack.getItem()).getEquipmentSlot() == slotIn) {
            T t = getModelFromSlot(slotIn);
            if (Reflector.ForgeHooksClient.exists()) {
                t = getArmorModelHook(entityLivingBaseIn, itemstack, slotIn, t);
            }
            t.setModelAttributes(renderer.getMainModel());
            t.setLivingAnimations(entityLivingBaseIn, limbSwing, limbSwingAmount, partialTicks);
            setModelSlotVisible(t, slotIn);
            boolean flag = isLegSlot(slotIn);
            if (!Config.isCustomItems() || !CustomItems.bindCustomArmorTexture(itemstack, slotIn, null)) {
                if (Reflector.ForgeHooksClient_getArmorTexture.exists()) {
                    renderer.bindTexture(getArmorResource(entityLivingBaseIn, itemstack, slotIn, null));
                } else {
                    renderer.bindTexture(getArmorResource(itemarmor, flag));
                }
            }
            if (Reflector.ForgeHooksClient_getArmorTexture.exists()) {
                if (ReflectorForge.armorHasOverlay(itemarmor, itemstack)) {
                    int j = itemarmor.getColor(itemstack);
                    float f3 = (float)(j >> 16 & 0xFF) / 255.0f;
                    float f4 = (float)(j >> 8 & 0xFF) / 255.0f;
                    float f5 = (float)(j & 0xFF) / 255.0f;
                    GlStateManager.color(colorR * f3, colorG * f4, colorB * f5, alpha);
                    t.render(entityLivingBaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
                    if (!Config.isCustomItems() || !CustomItems.bindCustomArmorTexture(itemstack, slotIn, "overlay")) {
                        renderer.bindTexture(getArmorResource(entityLivingBaseIn, itemstack, slotIn, "overlay"));
                    }
                }
                GlStateManager.color(colorR, colorG, colorB, alpha);
                t.render(entityLivingBaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
                if (!(skipRenderGlint || !itemstack.hasEffect() || Config.isCustomItems() && CustomItems.renderCustomArmorEffect(entityLivingBaseIn, itemstack, t, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale))) {
                    if (MoonWare.featureManager.getFeatureByClass(NoRender.class).getState() && NoRender.glintEffect.getCurrentValue()) {
                        return;
                    }
                    renderEnchantedGlint(renderer, entityLivingBaseIn, t, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
                }
                return;
            }
            switch (itemarmor.getArmorMaterial()) {
                case LEATHER: {
                    int i = itemarmor.getColor(itemstack);
                    float f = (float)(i >> 16 & 0xFF) / 255.0f;
                    float f1 = (float)(i >> 8 & 0xFF) / 255.0f;
                    float f2 = (float)(i & 0xFF) / 255.0f;
                    GlStateManager.color(colorR * f, colorG * f1, colorB * f2, alpha);
                    t.render(entityLivingBaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
                    if (!Config.isCustomItems() || !CustomItems.bindCustomArmorTexture(itemstack, slotIn, "overlay")) {
                        renderer.bindTexture(getArmorResource(itemarmor, flag, "overlay"));
                    }
                }
                case CHAIN:
                case IRON:
                case GOLD:
                case DIAMOND: {
                    GlStateManager.color(colorR, colorG, colorB, alpha);
                    t.render(entityLivingBaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
                }
            }
            if (!(skipRenderGlint || !itemstack.isItemEnchanted() || Config.isCustomItems() && CustomItems.renderCustomArmorEffect(entityLivingBaseIn, itemstack, t, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale))) {
                if (MoonWare.featureManager.getFeatureByClass(NoRender.class).getState() && NoRender.glintEffect.getCurrentValue()) {
                    return;
                }
                renderEnchantedGlint(renderer, entityLivingBaseIn, t, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
            }
        }
    }

    public T getModelFromSlot(EntityEquipmentSlot slotIn) {
        return isLegSlot(slotIn) ? modelLeggings : modelArmor;
    }

    private boolean isLegSlot(EntityEquipmentSlot slotIn) {
        return slotIn == EntityEquipmentSlot.LEGS;
    }

    public static void renderEnchantedGlint(RenderLivingBase<?> p_188364_0_, EntityLivingBase p_188364_1_, ModelBase model, float p_188364_3_, float p_188364_4_, float p_188364_5_, float p_188364_6_, float p_188364_7_, float p_188364_8_, float p_188364_9_) {
        if (!Config.isShaders() || !Shaders.isShadowPass) {
            float f = (float)p_188364_1_.ticksExisted + p_188364_5_;
            p_188364_0_.bindTexture(ENCHANTED_ITEM_GLINT_RES);
            if (Config.isShaders()) {
                ShadersRender.renderEnchantedGlintBegin();
            }
            Minecraft.gameRenderer.setupFogColor(true);
            GlStateManager.enableBlend();
            GlStateManager.depthFunc(514);
            GlStateManager.depthMask(false);
            GlStateManager.color(0.5f, 0.5f, 0.5f, 1.0f);
            for (int i = 0; i < 2; ++i) {
                GlStateManager.disableLighting();
                GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_COLOR, GlStateManager.DestFactor.ONE);
                GlStateManager.color(0.38f, 0.19f, 0.608f, 1.0f);
                if (MoonWare.featureManager.getFeatureByClass(EnchantmentColor.class).getState()) {
                    switch (EnchantmentColor.colorMode.currentMode) {
                        case "Rainbow": {
                            Color rainbow = PaletteHelper.rainbow((int)f, 1.0f, 1.0f);
                            GlStateManager.color((float)rainbow.getRed() / 255.0f, (float)rainbow.getGreen() / 255.0f, (float)rainbow.getBlue() / 255.0f, (float)rainbow.getAlpha() / 255.0f);
                            break;
                        }
                        case "Custom": {
                            Color colorCustom = new Color(EnchantmentColor.customColor.getColor());
                            GlStateManager.color((float)colorCustom.getRed() / 255.0f, (float)colorCustom.getGreen() / 255.0f, (float)colorCustom.getBlue() / 255.0f, (float)colorCustom.getAlpha() / 255.0f);
                            break;
                        }
                        case "Client": {
                            GlStateManager.color((float)ClientHelper.getClientColor().getRed() / 255.0f, (float)ClientHelper.getClientColor().getGreen() / 255.0f, (float)ClientHelper.getClientColor().getBlue() / 255.0f, (float)ClientHelper.getClientColor().getAlpha() / 255.0f);
                        }
                    }
                }
                GlStateManager.matrixMode(5890);
                GlStateManager.loadIdentity();
                float f3 = 0.33333334f;
                GlStateManager.scale(0.33333334f, 0.33333334f, 0.33333334f);
                GlStateManager.rotate(30.0f - (float)i * 60.0f, 0.0f, 0.0f, 1.0f);
                GlStateManager.translate(0.0f, f * (0.001f + (float)i * 0.003f) * 20.0f, 0.0f);
                GlStateManager.matrixMode(5888);
                model.render(p_188364_1_, p_188364_3_, p_188364_4_, p_188364_6_, p_188364_7_, p_188364_8_, p_188364_9_);
                GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            }
            GlStateManager.matrixMode(5890);
            GlStateManager.loadIdentity();
            GlStateManager.matrixMode(5888);
            GlStateManager.enableLighting();
            GlStateManager.depthMask(true);
            GlStateManager.depthFunc(515);
            GlStateManager.disableBlend();
            Minecraft.gameRenderer.setupFogColor(false);
            if (Config.isShaders()) {
                ShadersRender.renderEnchantedGlintEnd();
            }
        }
    }

    private Namespaced getArmorResource(ItemArmor armor, boolean p_177181_2_) {
        return getArmorResource(armor, p_177181_2_, null);
    }

    private Namespaced getArmorResource(ItemArmor armor, boolean p_177178_2_, String p_177178_3_) {
        Object[] arrobject = new Object[3];
        arrobject[0] = armor.getArmorMaterial().getName();
        arrobject[1] = p_177178_2_ ? 2 : 1;
        arrobject[2] = p_177178_3_ == null ? "" : String.format("_%s", p_177178_3_);
        String s = String.format("textures/models/armor/%s_layer_%d%s.png", arrobject);
        Namespaced resourcelocation = ARMOR_TEXTURE_RES_MAP.get(s);
        if (resourcelocation == null) {
            resourcelocation = new Namespaced(s);
            ARMOR_TEXTURE_RES_MAP.put(s, resourcelocation);
        }
        return resourcelocation;
    }

    protected abstract void initArmor();

    protected abstract void setModelSlotVisible(T var1, EntityEquipmentSlot var2);

    protected T getArmorModelHook(EntityLivingBase p_getArmorModelHook_1_, ItemStack p_getArmorModelHook_2_, EntityEquipmentSlot p_getArmorModelHook_3_, T p_getArmorModelHook_4_) {
        return p_getArmorModelHook_4_;
    }

    public Namespaced getArmorResource(Entity p_getArmorResource_1_, ItemStack p_getArmorResource_2_, EntityEquipmentSlot p_getArmorResource_3_, String p_getArmorResource_4_) {
        ItemArmor itemarmor = (ItemArmor)p_getArmorResource_2_.getItem();
        String s = itemarmor.getArmorMaterial().getName();
        String s1 = "minecraft";
        int i = s.indexOf(58);
        if (i != -1) {
            s1 = s.substring(0, i);
            s = s.substring(i + 1);
        }
        Object[] arrobject = new Object[4];
        arrobject[0] = s1;
        arrobject[1] = s;
        arrobject[2] = isLegSlot(p_getArmorResource_3_) ? 2 : 1;
        arrobject[3] = p_getArmorResource_4_ == null ? "" : String.format("_%s", p_getArmorResource_4_);
        String s2 = String.format("%s:textures/models/armor/%s_layer_%d%s.png", arrobject);
        s2 = Reflector.callString(Reflector.ForgeHooksClient_getArmorTexture, p_getArmorResource_1_, p_getArmorResource_2_, s2, p_getArmorResource_3_, p_getArmorResource_4_);
        Namespaced resourcelocation = ARMOR_TEXTURE_RES_MAP.get(s2);
        if (resourcelocation == null) {
            resourcelocation = new Namespaced(s2);
            ARMOR_TEXTURE_RES_MAP.put(s2, resourcelocation);
        }
        return resourcelocation;
    }
}
