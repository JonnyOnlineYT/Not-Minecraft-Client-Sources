package net.minecraft.client.renderer.block.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.pipeline.IVertexConsumer;
import net.minecraftforge.client.model.pipeline.IVertexProducer;
import optifine.Config;
import optifine.QuadBounds;
import optifine.Reflector;

public class BakedQuad implements IVertexProducer
{
    /**
     * Joined 4 vertex records, each stores packed data according to the VertexFormat of the quad. Vanilla minecraft
     * uses DefaultVertexFormats.BLOCK, Forge uses (usually) ITEM, use BakedQuad.getFormat() to get the correct format.
     */
    protected int[] vertexData;
    protected final int tintIndex;
    protected EnumFacing face;
    protected TextureAtlasSprite sprite;
    private int[] vertexDataSingle;
    protected boolean applyDiffuseLighting = Reflector.ForgeHooksClient_fillNormal.exists();
    protected VertexFormat format = DefaultVertexFormats.ITEM;
    private QuadBounds quadBounds;

    public BakedQuad(int[] p_i6_1_, int p_i6_2_, EnumFacing p_i6_3_, TextureAtlasSprite p_i6_4_, boolean p_i6_5_, VertexFormat p_i6_6_)
    {
        vertexData = p_i6_1_;
        tintIndex = p_i6_2_;
        face = p_i6_3_;
        sprite = p_i6_4_;
        applyDiffuseLighting = p_i6_5_;
        format = p_i6_6_;
        fixVertexData();
    }

    public BakedQuad(int[] vertexDataIn, int tintIndexIn, EnumFacing faceIn, TextureAtlasSprite spriteIn)
    {
        vertexData = vertexDataIn;
        tintIndex = tintIndexIn;
        face = faceIn;
        sprite = spriteIn;
        fixVertexData();
    }

    public TextureAtlasSprite getSprite()
    {
        if (sprite == null)
        {
            sprite = getSpriteByUv(getVertexData());
        }

        return sprite;
    }

    public int[] getVertexData()
    {
        fixVertexData();
        return vertexData;
    }

    public boolean hasTintIndex()
    {
        return tintIndex != -1;
    }

    public int getTintIndex()
    {
        return tintIndex;
    }

    public EnumFacing getFace()
    {
        if (face == null)
        {
            face = FaceBakery.getFacingFromVertexData(getVertexData());
        }

        return face;
    }

    public int[] getVertexDataSingle()
    {
        if (vertexDataSingle == null)
        {
            vertexDataSingle = makeVertexDataSingle(getVertexData(), getSprite());
        }

        return vertexDataSingle;
    }

    private static int[] makeVertexDataSingle(int[] p_makeVertexDataSingle_0_, TextureAtlasSprite p_makeVertexDataSingle_1_)
    {
        int[] aint = p_makeVertexDataSingle_0_.clone();
        int i = p_makeVertexDataSingle_1_.sheetWidth / p_makeVertexDataSingle_1_.getIconWidth();
        int j = p_makeVertexDataSingle_1_.sheetHeight / p_makeVertexDataSingle_1_.getIconHeight();
        int k = aint.length / 4;

        for (int l = 0; l < 4; ++l)
        {
            int i1 = l * k;
            float f = Float.intBitsToFloat(aint[i1 + 4]);
            float f1 = Float.intBitsToFloat(aint[i1 + 4 + 1]);
            float f2 = p_makeVertexDataSingle_1_.toSingleU(f);
            float f3 = p_makeVertexDataSingle_1_.toSingleV(f1);
            aint[i1 + 4] = Float.floatToRawIntBits(f2);
            aint[i1 + 4 + 1] = Float.floatToRawIntBits(f3);
        }

        return aint;
    }

    public void pipe(IVertexConsumer p_pipe_1_)
    {
        Reflector.callVoid(Reflector.LightUtil_putBakedQuad, p_pipe_1_, this);
    }

    public VertexFormat getFormat()
    {
        return format;
    }

    public boolean shouldApplyDiffuseLighting()
    {
        return applyDiffuseLighting;
    }

    private static TextureAtlasSprite getSpriteByUv(int[] p_getSpriteByUv_0_)
    {
        float f = 1.0F;
        float f1 = 1.0F;
        float f2 = 0.0F;
        float f3 = 0.0F;
        int i = p_getSpriteByUv_0_.length / 4;

        for (int j = 0; j < 4; ++j)
        {
            int k = j * i;
            float f4 = Float.intBitsToFloat(p_getSpriteByUv_0_[k + 4]);
            float f5 = Float.intBitsToFloat(p_getSpriteByUv_0_[k + 4 + 1]);
            f = Math.min(f, f4);
            f1 = Math.min(f1, f5);
            f2 = Math.max(f2, f4);
            f3 = Math.max(f3, f5);
        }

        float f6 = (f + f2) / 2.0F;
        float f7 = (f1 + f3) / 2.0F;
        TextureAtlasSprite textureatlassprite = Minecraft.getTextureMapBlocks().getIconByUV(f6, f7);
        return textureatlassprite;
    }

    protected void fixVertexData()
    {
        if (Config.isShaders())
        {
            if (vertexData.length == 28)
            {
                vertexData = expandVertexData(vertexData);
            }
        }
        else if (vertexData.length == 56)
        {
            vertexData = compactVertexData(vertexData);
        }
    }

    private static int[] expandVertexData(int[] p_expandVertexData_0_)
    {
        int i = p_expandVertexData_0_.length / 4;
        int j = i * 2;
        int[] aint = new int[j * 4];

        for (int k = 0; k < 4; ++k)
        {
            System.arraycopy(p_expandVertexData_0_, k * i, aint, k * j, i);
        }

        return aint;
    }

    private static int[] compactVertexData(int[] p_compactVertexData_0_)
    {
        int i = p_compactVertexData_0_.length / 4;
        int j = i / 2;
        int[] aint = new int[j * 4];

        for (int k = 0; k < 4; ++k)
        {
            System.arraycopy(p_compactVertexData_0_, k * i, aint, k * j, j);
        }

        return aint;
    }

    public QuadBounds getQuadBounds()
    {
        if (quadBounds == null)
        {
            quadBounds = new QuadBounds(getVertexData());
        }

        return quadBounds;
    }

    public float getMidX()
    {
        QuadBounds quadbounds = getQuadBounds();
        return (quadbounds.getMaxX() + quadbounds.getMinX()) / 2.0F;
    }

    public double getMidY()
    {
        QuadBounds quadbounds = getQuadBounds();
        return (quadbounds.getMaxY() + quadbounds.getMinY()) / 2.0F;
    }

    public double getMidZ()
    {
        QuadBounds quadbounds = getQuadBounds();
        return (quadbounds.getMaxZ() + quadbounds.getMinZ()) / 2.0F;
    }

    public boolean isFaceQuad()
    {
        QuadBounds quadbounds = getQuadBounds();
        return quadbounds.isFaceQuad(face);
    }

    public boolean isFullQuad()
    {
        QuadBounds quadbounds = getQuadBounds();
        return quadbounds.isFullQuad(face);
    }

    public boolean isFullFaceQuad()
    {
        return isFullQuad() && isFaceQuad();
    }

    public String toString()
    {
        return "vertex: " + vertexData.length / 7 + ", tint: " + tintIndex + ", facing: " + face + ", sprite: " + sprite;
    }
}
