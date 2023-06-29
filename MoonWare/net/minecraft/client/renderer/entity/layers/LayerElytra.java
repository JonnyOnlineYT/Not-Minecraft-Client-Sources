package net.minecraft.client.renderer.entity.layers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelElytra;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Namespaced;
import optifine.Config;
import optifine.CustomItems;
import org.moonware.client.MoonWare;
import org.moonware.client.feature.impl.visual.CustomModel;
import org.moonware.client.feature.impl.visual.NoRender;

public class LayerElytra implements LayerRenderer<EntityLivingBase>
{
    /** The basic Elytra texture. */
    private static final Namespaced TEXTURE_ELYTRA = new Namespaced("textures/entity/elytra.png");
    protected final RenderLivingBase<?> renderPlayer;

    /** The model used by the Elytra. */
    private final ModelElytra modelElytra = new ModelElytra();

    public LayerElytra(RenderLivingBase<?> p_i47185_1_)
    {
        renderPlayer = p_i47185_1_;
    }

    public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        if ((!CustomModel.onlyMe.getCurrentValue() || entitylivingbaseIn == Minecraft.player || MoonWare.friendManager.isFriend(entitylivingbaseIn.getName()) && CustomModel.friends.getCurrentValue()) && MoonWare.featureManager.getFeatureByClass(CustomModel.class).getState() && (CustomModel.modelMode.currentMode.equals("Crazy Rabbit") || CustomModel.modelMode.currentMode.equals("2D Leon") || CustomModel.modelMode.currentMode.equals("Sonic") || CustomModel.modelMode.currentMode.equals("CupHead") || CustomModel.modelMode.currentMode.equals("Freddy Bear") || CustomModel.modelMode.currentMode.equals("Chinchilla") || CustomModel.modelMode.currentMode.equals("Amogus") || CustomModel.modelMode.currentMode.equals("Red Panda") || CustomModel.modelMode.currentMode.equals("Demon") || CustomModel.modelMode.currentMode.equals("SirenHead") || CustomModel.modelMode.currentMode.equals("Jeff Killer") || CustomModel.modelMode.currentMode.equals("Crab"))) {
            return;
        }
        ItemStack itemstack = entitylivingbaseIn.getItemStackFromSlot(EntityEquipmentSlot.CHEST);

        if (itemstack.getItem() == Items.ELYTRA)
        {
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);

            if (entitylivingbaseIn instanceof AbstractClientPlayer)
            {
                AbstractClientPlayer abstractclientplayer = (AbstractClientPlayer)entitylivingbaseIn;

                if (abstractclientplayer.isPlayerInfoSet() && abstractclientplayer.getLocationElytra() != null)
                {
                    renderPlayer.bindTexture(abstractclientplayer.getLocationElytra());
                }
                else if (abstractclientplayer.hasElytraCape() && abstractclientplayer.hasPlayerInfo() && abstractclientplayer.getLocationCape() != null && abstractclientplayer.isWearing(EnumPlayerModelParts.CAPE))
                {
                    renderPlayer.bindTexture(abstractclientplayer.getLocationCape());
                }
                else
                {
                    Namespaced resourcelocation1 = TEXTURE_ELYTRA;

                    if (Config.isCustomItems())
                    {
                        resourcelocation1 = CustomItems.getCustomElytraTexture(itemstack, resourcelocation1);
                    }

                    renderPlayer.bindTexture(resourcelocation1);
                }
            }
            else
            {
                Namespaced resourcelocation = TEXTURE_ELYTRA;

                if (Config.isCustomItems())
                {
                    resourcelocation = CustomItems.getCustomElytraTexture(itemstack, resourcelocation);
                }

                renderPlayer.bindTexture(resourcelocation);
            }

            GlStateManager.pushMatrix();
            GlStateManager.translate(0.0F, 0.0F, 0.125F);
            modelElytra.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entitylivingbaseIn);
            modelElytra.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);

            if (itemstack.isItemEnchanted())
            {
                if (MoonWare.featureManager.getFeatureByClass(NoRender.class).getState() && NoRender.glintEffect.getBoolValue())
                    return;
                LayerArmorBase.renderEnchantedGlint(renderPlayer, entitylivingbaseIn, modelElytra, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
            }

            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }
    }

    public boolean shouldCombineTextures()
    {
        return false;
    }
}
