package optifine;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.Namespaced;

public class PlayerItemModel
{
    private Dimension textureSize;
    private boolean usePlayerTexture;
    private PlayerItemRenderer[] modelRenderers = new PlayerItemRenderer[0];
    private Namespaced textureLocation;
    private BufferedImage textureImage;
    private DynamicTexture texture;
    private Namespaced locationMissing = new Namespaced("textures/blocks/wool_colored_red.png");
    public static final int ATTACH_BODY = 0;
    public static final int ATTACH_HEAD = 1;
    public static final int ATTACH_LEFT_ARM = 2;
    public static final int ATTACH_RIGHT_ARM = 3;
    public static final int ATTACH_LEFT_LEG = 4;
    public static final int ATTACH_RIGHT_LEG = 5;
    public static final int ATTACH_CAPE = 6;

    public PlayerItemModel(Dimension p_i73_1_, boolean p_i73_2_, PlayerItemRenderer[] p_i73_3_)
    {
        textureSize = p_i73_1_;
        usePlayerTexture = p_i73_2_;
        modelRenderers = p_i73_3_;
    }

    public void render(ModelBiped p_render_1_, AbstractClientPlayer p_render_2_, float p_render_3_, float p_render_4_)
    {
        TextureManager texturemanager = Config.getTextureManager();

        if (usePlayerTexture)
        {
            texturemanager.bindTexture(p_render_2_.getLocationSkin());
        }
        else if (textureLocation != null)
        {
            if (texture == null && textureImage != null)
            {
                texture = new DynamicTexture(textureImage);
                Minecraft.getTextureManager().loadTexture(textureLocation, texture);
            }

            texturemanager.bindTexture(textureLocation);
        }
        else
        {
            texturemanager.bindTexture(locationMissing);
        }

        for (int i = 0; i < modelRenderers.length; ++i)
        {
            PlayerItemRenderer playeritemrenderer = modelRenderers[i];
            GlStateManager.pushMatrix();

            if (p_render_2_.isSneaking())
            {
                GlStateManager.translate(0.0F, 0.2F, 0.0F);
            }

            playeritemrenderer.render(p_render_1_, p_render_3_);
            GlStateManager.popMatrix();
        }
    }

    public static ModelRenderer getAttachModel(ModelBiped p_getAttachModel_0_, int p_getAttachModel_1_)
    {
        switch (p_getAttachModel_1_)
        {
            case 0:
                return p_getAttachModel_0_.bipedBody;

            case 1:
                return p_getAttachModel_0_.bipedHead;

            case 2:
                return p_getAttachModel_0_.bipedLeftArm;

            case 3:
                return p_getAttachModel_0_.bipedRightArm;

            case 4:
                return p_getAttachModel_0_.bipedLeftLeg;

            case 5:
                return p_getAttachModel_0_.bipedRightLeg;

            default:
                return null;
        }
    }

    public BufferedImage getTextureImage()
    {
        return textureImage;
    }

    public void setTextureImage(BufferedImage p_setTextureImage_1_)
    {
        textureImage = p_setTextureImage_1_;
    }

    public DynamicTexture getTexture()
    {
        return texture;
    }

    public Namespaced getTextureLocation()
    {
        return textureLocation;
    }

    public void setTextureLocation(Namespaced p_setTextureLocation_1_)
    {
        textureLocation = p_setTextureLocation_1_;
    }

    public boolean isUsePlayerTexture()
    {
        return usePlayerTexture;
    }
}
