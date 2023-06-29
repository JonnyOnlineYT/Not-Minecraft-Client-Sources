package net.minecraft.client.renderer.block.model;

import java.util.Arrays;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public class BakedQuadRetextured extends BakedQuad
{
    private final TextureAtlasSprite texture;
    private final TextureAtlasSprite spriteOld;

    public BakedQuadRetextured(BakedQuad quad, TextureAtlasSprite textureIn)
    {
        super(Arrays.copyOf(quad.getVertexData(), quad.getVertexData().length), quad.tintIndex, FaceBakery.getFacingFromVertexData(quad.getVertexData()), textureIn);
        texture = textureIn;
        format = quad.format;
        applyDiffuseLighting = quad.applyDiffuseLighting;
        spriteOld = quad.getSprite();
        remapQuad();
        fixVertexData();
    }

    private void remapQuad()
    {
        for (int i = 0; i < 4; ++i)
        {
            int j = format.getIntegerSize() * i;
            int k = format.getUvOffsetById(0) / 4;
            vertexData[j + k] = Float.floatToRawIntBits(texture.getInterpolatedU(spriteOld.getUnInterpolatedU(Float.intBitsToFloat(vertexData[j + k]))));
            vertexData[j + k + 1] = Float.floatToRawIntBits(texture.getInterpolatedV(spriteOld.getUnInterpolatedV(Float.intBitsToFloat(vertexData[j + k + 1]))));
        }
    }
}
