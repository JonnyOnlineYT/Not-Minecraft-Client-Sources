package optifine;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;

public class ModelSprite
{
    private ModelRenderer modelRenderer;
    private int textureOffsetX;
    private int textureOffsetY;
    private float posX;
    private float posY;
    private float posZ;
    private int sizeX;
    private int sizeY;
    private int sizeZ;
    private float sizeAdd;
    private float minU;
    private float minV;
    private float maxU;
    private float maxV;

    public ModelSprite(ModelRenderer p_i67_1_, int p_i67_2_, int p_i67_3_, float p_i67_4_, float p_i67_5_, float p_i67_6_, int p_i67_7_, int p_i67_8_, int p_i67_9_, float p_i67_10_)
    {
        modelRenderer = p_i67_1_;
        textureOffsetX = p_i67_2_;
        textureOffsetY = p_i67_3_;
        posX = p_i67_4_;
        posY = p_i67_5_;
        posZ = p_i67_6_;
        sizeX = p_i67_7_;
        sizeY = p_i67_8_;
        sizeZ = p_i67_9_;
        sizeAdd = p_i67_10_;
        minU = (float)p_i67_2_ / p_i67_1_.textureWidth;
        minV = (float)p_i67_3_ / p_i67_1_.textureHeight;
        maxU = (float)(p_i67_2_ + p_i67_7_) / p_i67_1_.textureWidth;
        maxV = (float)(p_i67_3_ + p_i67_8_) / p_i67_1_.textureHeight;
    }

    public void render(Tessellator p_render_1_, float p_render_2_)
    {
        GlStateManager.translate(posX * p_render_2_, posY * p_render_2_, posZ * p_render_2_);
        float f = minU;
        float f1 = maxU;
        float f2 = minV;
        float f3 = maxV;

        if (modelRenderer.mirror)
        {
            f = maxU;
            f1 = minU;
        }

        if (modelRenderer.mirrorV)
        {
            f2 = maxV;
            f3 = minV;
        }

        renderItemIn2D(p_render_1_, f, f2, f1, f3, sizeX, sizeY, p_render_2_ * (float) sizeZ, modelRenderer.textureWidth, modelRenderer.textureHeight);
        GlStateManager.translate(-posX * p_render_2_, -posY * p_render_2_, -posZ * p_render_2_);
    }

    public static void renderItemIn2D(Tessellator p_renderItemIn2D_0_, float p_renderItemIn2D_1_, float p_renderItemIn2D_2_, float p_renderItemIn2D_3_, float p_renderItemIn2D_4_, int p_renderItemIn2D_5_, int p_renderItemIn2D_6_, float p_renderItemIn2D_7_, float p_renderItemIn2D_8_, float p_renderItemIn2D_9_)
    {
        if (p_renderItemIn2D_7_ < 6.25E-4F)
        {
            p_renderItemIn2D_7_ = 6.25E-4F;
        }

        float f = p_renderItemIn2D_3_ - p_renderItemIn2D_1_;
        float f1 = p_renderItemIn2D_4_ - p_renderItemIn2D_2_;
        double d0 = MathHelper.abs(f) * (p_renderItemIn2D_8_ / 16.0F);
        double d1 = MathHelper.abs(f1) * (p_renderItemIn2D_9_ / 16.0F);
        BufferBuilder bufferbuilder = p_renderItemIn2D_0_.getBuffer();
        GL11.glNormal3f(0.0F, 0.0F, -1.0F);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(0.0D, d1, 0.0D).tex(p_renderItemIn2D_1_, p_renderItemIn2D_4_).endVertex();
        bufferbuilder.pos(d0, d1, 0.0D).tex(p_renderItemIn2D_3_, p_renderItemIn2D_4_).endVertex();
        bufferbuilder.pos(d0, 0.0D, 0.0D).tex(p_renderItemIn2D_3_, p_renderItemIn2D_2_).endVertex();
        bufferbuilder.pos(0.0D, 0.0D, 0.0D).tex(p_renderItemIn2D_1_, p_renderItemIn2D_2_).endVertex();
        p_renderItemIn2D_0_.draw();
        GL11.glNormal3f(0.0F, 0.0F, 1.0F);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(0.0D, 0.0D, p_renderItemIn2D_7_).tex(p_renderItemIn2D_1_, p_renderItemIn2D_2_).endVertex();
        bufferbuilder.pos(d0, 0.0D, p_renderItemIn2D_7_).tex(p_renderItemIn2D_3_, p_renderItemIn2D_2_).endVertex();
        bufferbuilder.pos(d0, d1, p_renderItemIn2D_7_).tex(p_renderItemIn2D_3_, p_renderItemIn2D_4_).endVertex();
        bufferbuilder.pos(0.0D, d1, p_renderItemIn2D_7_).tex(p_renderItemIn2D_1_, p_renderItemIn2D_4_).endVertex();
        p_renderItemIn2D_0_.draw();
        float f2 = 0.5F * f / (float)p_renderItemIn2D_5_;
        float f3 = 0.5F * f1 / (float)p_renderItemIn2D_6_;
        GL11.glNormal3f(-1.0F, 0.0F, 0.0F);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);

        for (int i = 0; i < p_renderItemIn2D_5_; ++i)
        {
            float f4 = (float)i / (float)p_renderItemIn2D_5_;
            float f5 = p_renderItemIn2D_1_ + f * f4 + f2;
            bufferbuilder.pos((double)f4 * d0, d1, p_renderItemIn2D_7_).tex(f5, p_renderItemIn2D_4_).endVertex();
            bufferbuilder.pos((double)f4 * d0, d1, 0.0D).tex(f5, p_renderItemIn2D_4_).endVertex();
            bufferbuilder.pos((double)f4 * d0, 0.0D, 0.0D).tex(f5, p_renderItemIn2D_2_).endVertex();
            bufferbuilder.pos((double)f4 * d0, 0.0D, p_renderItemIn2D_7_).tex(f5, p_renderItemIn2D_2_).endVertex();
        }

        p_renderItemIn2D_0_.draw();
        GL11.glNormal3f(1.0F, 0.0F, 0.0F);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);

        for (int j = 0; j < p_renderItemIn2D_5_; ++j)
        {
            float f7 = (float)j / (float)p_renderItemIn2D_5_;
            float f10 = p_renderItemIn2D_1_ + f * f7 + f2;
            float f6 = f7 + 1.0F / (float)p_renderItemIn2D_5_;
            bufferbuilder.pos((double)f6 * d0, 0.0D, p_renderItemIn2D_7_).tex(f10, p_renderItemIn2D_2_).endVertex();
            bufferbuilder.pos((double)f6 * d0, 0.0D, 0.0D).tex(f10, p_renderItemIn2D_2_).endVertex();
            bufferbuilder.pos((double)f6 * d0, d1, 0.0D).tex(f10, p_renderItemIn2D_4_).endVertex();
            bufferbuilder.pos((double)f6 * d0, d1, p_renderItemIn2D_7_).tex(f10, p_renderItemIn2D_4_).endVertex();
        }

        p_renderItemIn2D_0_.draw();
        GL11.glNormal3f(0.0F, 1.0F, 0.0F);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);

        for (int k = 0; k < p_renderItemIn2D_6_; ++k)
        {
            float f8 = (float)k / (float)p_renderItemIn2D_6_;
            float f11 = p_renderItemIn2D_2_ + f1 * f8 + f3;
            float f13 = f8 + 1.0F / (float)p_renderItemIn2D_6_;
            bufferbuilder.pos(0.0D, (double)f13 * d1, p_renderItemIn2D_7_).tex(p_renderItemIn2D_1_, f11).endVertex();
            bufferbuilder.pos(d0, (double)f13 * d1, p_renderItemIn2D_7_).tex(p_renderItemIn2D_3_, f11).endVertex();
            bufferbuilder.pos(d0, (double)f13 * d1, 0.0D).tex(p_renderItemIn2D_3_, f11).endVertex();
            bufferbuilder.pos(0.0D, (double)f13 * d1, 0.0D).tex(p_renderItemIn2D_1_, f11).endVertex();
        }

        p_renderItemIn2D_0_.draw();
        GL11.glNormal3f(0.0F, -1.0F, 0.0F);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);

        for (int l = 0; l < p_renderItemIn2D_6_; ++l)
        {
            float f9 = (float)l / (float)p_renderItemIn2D_6_;
            float f12 = p_renderItemIn2D_2_ + f1 * f9 + f3;
            bufferbuilder.pos(d0, (double)f9 * d1, p_renderItemIn2D_7_).tex(p_renderItemIn2D_3_, f12).endVertex();
            bufferbuilder.pos(0.0D, (double)f9 * d1, p_renderItemIn2D_7_).tex(p_renderItemIn2D_1_, f12).endVertex();
            bufferbuilder.pos(0.0D, (double)f9 * d1, 0.0D).tex(p_renderItemIn2D_1_, f12).endVertex();
            bufferbuilder.pos(d0, (double)f9 * d1, 0.0D).tex(p_renderItemIn2D_3_, f12).endVertex();
        }

        p_renderItemIn2D_0_.draw();
    }
}
