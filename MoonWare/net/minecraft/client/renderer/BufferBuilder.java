package net.minecraft.client.renderer;

import com.google.common.primitives.Floats;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Comparator;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import optifine.Config;
import optifine.RenderEnv;
import optifine.TextureUtils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;
import shadersmod.client.SVertexBuilder;

public class BufferBuilder
{
    private static final Logger LOGGER = LogManager.getLogger();
    private ByteBuffer byteBuffer;
    public IntBuffer rawIntBuffer;
    private ShortBuffer rawShortBuffer;
    public FloatBuffer rawFloatBuffer;
    public int vertexCount;
    private VertexFormatElement vertexFormatElement;
    private int vertexFormatIndex;

    /** None */
    private boolean noColor;
    public int drawMode;
    private double xOffset;
    private double yOffset;
    private double zOffset;
    private VertexFormat vertexFormat;
    private boolean isDrawing;
    private BlockRenderLayer blockLayer;
    private boolean[] drawnIcons = new boolean[256];
    private TextureAtlasSprite[] quadSprites;
    private TextureAtlasSprite[] quadSpritesPrev;
    private TextureAtlasSprite quadSprite;
    public SVertexBuilder sVertexBuilder;
    public RenderEnv renderEnv;

    public BufferBuilder(int bufferSizeIn)
    {
        if (Config.isShaders())
        {
            bufferSizeIn *= 2;
        }

        byteBuffer = GLAllocation.createDirectByteBuffer(bufferSizeIn * 4);
        rawIntBuffer = byteBuffer.asIntBuffer();
        rawShortBuffer = byteBuffer.asShortBuffer();
        rawFloatBuffer = byteBuffer.asFloatBuffer();
        SVertexBuilder.initVertexBuilder(this);
    }

    private void growBuffer(int p_181670_1_)
    {
        if (Config.isShaders())
        {
            p_181670_1_ *= 2;
        }

        if (MathHelper.roundUp(p_181670_1_, 4) / 4 > rawIntBuffer.remaining() || vertexCount * vertexFormat.getNextOffset() + p_181670_1_ > byteBuffer.capacity())
        {
            int i = byteBuffer.capacity();
            int j = i + MathHelper.roundUp(p_181670_1_, 2097152);
            LOGGER.debug("Needed to grow BufferBuilder buffer: Old size {} bytes, new size {} bytes.", Integer.valueOf(i), Integer.valueOf(j));
            int k = rawIntBuffer.position();
            ByteBuffer bytebuffer = GLAllocation.createDirectByteBuffer(j);
            byteBuffer.position(0);
            bytebuffer.put(byteBuffer);
            bytebuffer.rewind();
            byteBuffer = bytebuffer;
            rawFloatBuffer = byteBuffer.asFloatBuffer();
            rawIntBuffer = byteBuffer.asIntBuffer();
            rawIntBuffer.position(k);
            rawShortBuffer = byteBuffer.asShortBuffer();
            rawShortBuffer.position(k << 1);

            if (quadSprites != null)
            {
                TextureAtlasSprite[] atextureatlassprite = quadSprites;
                int l = getBufferQuadSize();
                quadSprites = new TextureAtlasSprite[l];
                System.arraycopy(atextureatlassprite, 0, quadSprites, 0, Math.min(atextureatlassprite.length, quadSprites.length));
                quadSpritesPrev = null;
            }
        }
    }

    public void sortVertexData(float p_181674_1_, float p_181674_2_, float p_181674_3_)
    {
        int i = vertexCount / 4;
        float[] afloat = new float[i];

        for (int j = 0; j < i; ++j)
        {
            afloat[j] = getDistanceSq(rawFloatBuffer, (float)((double)p_181674_1_ + xOffset), (float)((double)p_181674_2_ + yOffset), (float)((double)p_181674_3_ + zOffset), vertexFormat.getIntegerSize(), j * vertexFormat.getNextOffset());
        }

        Integer[] ainteger = new Integer[i];

        for (int k = 0; k < ainteger.length; ++k)
        {
            ainteger[k] = k;
        }

        Arrays.sort(ainteger, new Comparator<Integer>()
        {
            public int compare(Integer p_compare_1_, Integer p_compare_2_)
            {
                return Floats.compare(afloat[p_compare_2_.intValue()], afloat[p_compare_1_.intValue()]);
            }
        });
        BitSet bitset = new BitSet();
        int l = vertexFormat.getNextOffset();
        int[] aint = new int[l];

        for (int i1 = bitset.nextClearBit(0); i1 < ainteger.length; i1 = bitset.nextClearBit(i1 + 1))
        {
            int j1 = ainteger[i1].intValue();

            if (j1 != i1)
            {
                rawIntBuffer.limit(j1 * l + l);
                rawIntBuffer.position(j1 * l);
                rawIntBuffer.get(aint);
                int k1 = j1;

                for (int l1 = ainteger[j1].intValue(); k1 != i1; l1 = ainteger[l1].intValue())
                {
                    rawIntBuffer.limit(l1 * l + l);
                    rawIntBuffer.position(l1 * l);
                    IntBuffer intbuffer = rawIntBuffer.slice();
                    rawIntBuffer.limit(k1 * l + l);
                    rawIntBuffer.position(k1 * l);
                    rawIntBuffer.put(intbuffer);
                    bitset.set(k1);
                    k1 = l1;
                }

                rawIntBuffer.limit(i1 * l + l);
                rawIntBuffer.position(i1 * l);
                rawIntBuffer.put(aint);
            }

            bitset.set(i1);
        }

        rawIntBuffer.limit(rawIntBuffer.capacity());
        rawIntBuffer.position(getBufferSize());

        if (quadSprites != null)
        {
            TextureAtlasSprite[] atextureatlassprite = new TextureAtlasSprite[vertexCount / 4];
            int i2 = vertexFormat.getNextOffset() / 4 * 4;

            for (int j2 = 0; j2 < ainteger.length; ++j2)
            {
                int k2 = ainteger[j2].intValue();
                atextureatlassprite[j2] = quadSprites[k2];
            }

            System.arraycopy(atextureatlassprite, 0, quadSprites, 0, atextureatlassprite.length);
        }
    }

    public BufferBuilder.State getVertexState()
    {
        rawIntBuffer.rewind();
        int i = getBufferSize();
        rawIntBuffer.limit(i);
        int[] aint = new int[i];
        rawIntBuffer.get(aint);
        rawIntBuffer.limit(rawIntBuffer.capacity());
        rawIntBuffer.position(i);
        TextureAtlasSprite[] atextureatlassprite = null;

        if (quadSprites != null)
        {
            int j = vertexCount / 4;
            atextureatlassprite = new TextureAtlasSprite[j];
            System.arraycopy(quadSprites, 0, atextureatlassprite, 0, j);
        }

        return new BufferBuilder.State(aint, new VertexFormat(vertexFormat), atextureatlassprite);
    }

    public int getBufferSize()
    {
        return vertexCount * vertexFormat.getIntegerSize();
    }

    private static float getDistanceSq(FloatBuffer p_181665_0_, float p_181665_1_, float p_181665_2_, float p_181665_3_, int p_181665_4_, int p_181665_5_)
    {
        float f = p_181665_0_.get(p_181665_5_ + p_181665_4_ * 0 + 0);
        float f1 = p_181665_0_.get(p_181665_5_ + p_181665_4_ * 0 + 1);
        float f2 = p_181665_0_.get(p_181665_5_ + p_181665_4_ * 0 + 2);
        float f3 = p_181665_0_.get(p_181665_5_ + p_181665_4_ * 1 + 0);
        float f4 = p_181665_0_.get(p_181665_5_ + p_181665_4_ * 1 + 1);
        float f5 = p_181665_0_.get(p_181665_5_ + p_181665_4_ * 1 + 2);
        float f6 = p_181665_0_.get(p_181665_5_ + p_181665_4_ * 2 + 0);
        float f7 = p_181665_0_.get(p_181665_5_ + p_181665_4_ * 2 + 1);
        float f8 = p_181665_0_.get(p_181665_5_ + p_181665_4_ * 2 + 2);
        float f9 = p_181665_0_.get(p_181665_5_ + p_181665_4_ * 3 + 0);
        float f10 = p_181665_0_.get(p_181665_5_ + p_181665_4_ * 3 + 1);
        float f11 = p_181665_0_.get(p_181665_5_ + p_181665_4_ * 3 + 2);
        float f12 = (f + f3 + f6 + f9) * 0.25F - p_181665_1_;
        float f13 = (f1 + f4 + f7 + f10) * 0.25F - p_181665_2_;
        float f14 = (f2 + f5 + f8 + f11) * 0.25F - p_181665_3_;
        return f12 * f12 + f13 * f13 + f14 * f14;
    }

    public void setVertexState(BufferBuilder.State state)
    {
        rawIntBuffer.clear();
        growBuffer(state.getRawBuffer().length * 4);
        rawIntBuffer.put(state.getRawBuffer());
        vertexCount = state.getVertexCount();
        vertexFormat = new VertexFormat(state.getVertexFormat());

        if (state.stateQuadSprites != null)
        {
            if (quadSprites == null)
            {
                quadSprites = quadSpritesPrev;
            }

            if (quadSprites == null || quadSprites.length < getBufferQuadSize())
            {
                quadSprites = new TextureAtlasSprite[getBufferQuadSize()];
            }

            TextureAtlasSprite[] atextureatlassprite = state.stateQuadSprites;
            System.arraycopy(atextureatlassprite, 0, quadSprites, 0, atextureatlassprite.length);
        }
        else
        {
            if (quadSprites != null)
            {
                quadSpritesPrev = quadSprites;
            }

            quadSprites = null;
        }
    }

    public void reset()
    {
        vertexCount = 0;
        vertexFormatElement = null;
        vertexFormatIndex = 0;
        quadSprite = null;
    }

    public void begin(int glMode, VertexFormat format)
    {
        if (isDrawing)
        {
            throw new IllegalStateException("Already building!");
        }
        else
        {
            isDrawing = true;
            reset();
            drawMode = glMode;
            vertexFormat = format;
            vertexFormatElement = format.getElement(vertexFormatIndex);
            noColor = false;
            byteBuffer.limit(byteBuffer.capacity());

            if (Config.isShaders())
            {
                SVertexBuilder.endSetVertexFormat(this);
            }

            if (Config.isMultiTexture())
            {
                if (blockLayer != null)
                {
                    if (quadSprites == null)
                    {
                        quadSprites = quadSpritesPrev;
                    }

                    if (quadSprites == null || quadSprites.length < getBufferQuadSize())
                    {
                        quadSprites = new TextureAtlasSprite[getBufferQuadSize()];
                    }
                }
            }
            else
            {
                if (quadSprites != null)
                {
                    quadSpritesPrev = quadSprites;
                }

                quadSprites = null;
            }
        }
    }

    public BufferBuilder tex(double u, double v)
    {
        if (quadSprite != null && quadSprites != null)
        {
            u = quadSprite.toSingleU((float)u);
            v = quadSprite.toSingleV((float)v);
            quadSprites[vertexCount / 4] = quadSprite;
        }

        int i = vertexCount * vertexFormat.getNextOffset() + vertexFormat.getOffset(vertexFormatIndex);

        switch (vertexFormatElement.getType())
        {
            case FLOAT:
                byteBuffer.putFloat(i, (float)u);
                byteBuffer.putFloat(i + 4, (float)v);
                break;

            case UINT:
            case INT:
                byteBuffer.putInt(i, (int)u);
                byteBuffer.putInt(i + 4, (int)v);
                break;

            case USHORT:
            case SHORT:
                byteBuffer.putShort(i, (short)((int)v));
                byteBuffer.putShort(i + 2, (short)((int)u));
                break;

            case UBYTE:
            case BYTE:
                byteBuffer.put(i, (byte)((int)v));
                byteBuffer.put(i + 1, (byte)((int)u));
        }

        nextVertexFormatIndex();
        return this;
    }

    public BufferBuilder lightmap(int p_187314_1_, int p_187314_2_)
    {
        int i = vertexCount * vertexFormat.getNextOffset() + vertexFormat.getOffset(vertexFormatIndex);

        switch (vertexFormatElement.getType())
        {
            case FLOAT:
                byteBuffer.putFloat(i, (float)p_187314_1_);
                byteBuffer.putFloat(i + 4, (float)p_187314_2_);
                break;

            case UINT:
            case INT:
                byteBuffer.putInt(i, p_187314_1_);
                byteBuffer.putInt(i + 4, p_187314_2_);
                break;

            case USHORT:
            case SHORT:
                byteBuffer.putShort(i, (short)p_187314_2_);
                byteBuffer.putShort(i + 2, (short)p_187314_1_);
                break;

            case UBYTE:
            case BYTE:
                byteBuffer.put(i, (byte)p_187314_2_);
                byteBuffer.put(i + 1, (byte)p_187314_1_);
        }

        nextVertexFormatIndex();
        return this;
    }

    public void putBrightness4(int p_178962_1_, int p_178962_2_, int p_178962_3_, int p_178962_4_)
    {
        int i = (vertexCount - 4) * vertexFormat.getIntegerSize() + vertexFormat.getUvOffsetById(1) / 4;
        int j = vertexFormat.getNextOffset() >> 2;
        rawIntBuffer.put(i, p_178962_1_);
        rawIntBuffer.put(i + j, p_178962_2_);
        rawIntBuffer.put(i + j * 2, p_178962_3_);
        rawIntBuffer.put(i + j * 3, p_178962_4_);
    }

    public void putPosition(double x, double y, double z)
    {
        int i = vertexFormat.getIntegerSize();
        int j = (vertexCount - 4) * i;

        for (int k = 0; k < 4; ++k)
        {
            int l = j + k * i;
            int i1 = l + 1;
            int j1 = i1 + 1;
            rawIntBuffer.put(l, Float.floatToRawIntBits((float)(x + xOffset) + Float.intBitsToFloat(rawIntBuffer.get(l))));
            rawIntBuffer.put(i1, Float.floatToRawIntBits((float)(y + yOffset) + Float.intBitsToFloat(rawIntBuffer.get(i1))));
            rawIntBuffer.put(j1, Float.floatToRawIntBits((float)(z + zOffset) + Float.intBitsToFloat(rawIntBuffer.get(j1))));
        }
    }

    /**
     * Gets the position into the vertex data buffer at which the given vertex's color data can be found, in {@code
     * int}s.
     *  
     * @param vertexIndex The index of the vertex in question, where 0 is the last one added, 1 is the second last, etc.
     */
    public int getColorIndex(int vertexIndex)
    {
        return ((vertexCount - vertexIndex) * vertexFormat.getNextOffset() + vertexFormat.getColorOffset()) / 4;
    }

    /**
     * Modify the color data of the given vertex with the given multipliers.
     *  
     * @param vertexIndex The index of the vertex to modify, where 0 is the last one added, 1 is the second last, etc.
     */
    public void putColorMultiplier(float red, float green, float blue, int vertexIndex)
    {
        int i = getColorIndex(vertexIndex);
        int j = -1;

        if (!noColor)
        {
            j = rawIntBuffer.get(i);

            if (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN)
            {
                int k = (int)((float)(j & 255) * red);
                int l = (int)((float)(j >> 8 & 255) * green);
                int i1 = (int)((float)(j >> 16 & 255) * blue);
                j = j & -16777216;
                j = j | i1 << 16 | l << 8 | k;
            }
            else
            {
                int j1 = (int)((float)(j >> 24 & 255) * red);
                int k1 = (int)((float)(j >> 16 & 255) * green);
                int l1 = (int)((float)(j >> 8 & 255) * blue);
                j = j & 255;
                j = j | j1 << 24 | k1 << 16 | l1 << 8;
            }
        }

        rawIntBuffer.put(i, j);
    }

    private void func_192836_a(int p_192836_1_, int p_192836_2_)
    {
        int i = getColorIndex(p_192836_2_);
        int j = p_192836_1_ >> 16 & 255;
        int k = p_192836_1_ >> 8 & 255;
        int l = p_192836_1_ & 255;
        putColorRGBA(i, j, k, l);
    }

    public void putColorRGB_F(float red, float green, float blue, int vertexIndex)
    {
        int i = getColorIndex(vertexIndex);
        int j = MathHelper.clamp((int)(red * 255.0F), 0, 255);
        int k = MathHelper.clamp((int)(green * 255.0F), 0, 255);
        int l = MathHelper.clamp((int)(blue * 255.0F), 0, 255);
        putColorRGBA(i, j, k, l);
    }

    /**
     * Write the given color data of 4 bytes at the given index into the vertex data buffer, accounting for system
     * endianness.
     *  
     * @param index The index in the vertex data buffer to which the color data will be written, in {@code int}s
     */
    public void putColorRGBA(int index, int red, int green, int blue)
    {
        if (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN)
        {
            rawIntBuffer.put(index, -16777216 | blue << 16 | green << 8 | red);
        }
        else
        {
            rawIntBuffer.put(index, red << 24 | green << 16 | blue << 8 | 255);
        }
    }

    /**
     * Disables color processing.
     */
    public void noColor()
    {
        noColor = true;
    }

    public BufferBuilder color(float red, float green, float blue, float alpha)
    {
        return color((int)(red * 255.0F), (int)(green * 255.0F), (int)(blue * 255.0F), (int)(alpha * 255.0F));
    }

    public BufferBuilder color(int hex) {
        return color(hex >> 16 & 255, hex >> 8 & 255, hex & 255, hex >> 24 & 255);
    }

    public BufferBuilder color(int red, int green, int blue, int alpha)
    {
        if (noColor)
        {
            return this;
        }
        else
        {
            int i = vertexCount * vertexFormat.getNextOffset() + vertexFormat.getOffset(vertexFormatIndex);

            switch (vertexFormatElement.getType())
            {
                case FLOAT:
                    byteBuffer.putFloat(i, (float)red / 255.0F);
                    byteBuffer.putFloat(i + 4, (float)green / 255.0F);
                    byteBuffer.putFloat(i + 8, (float)blue / 255.0F);
                    byteBuffer.putFloat(i + 12, (float)alpha / 255.0F);
                    break;

                case UINT:
                case INT:
                    byteBuffer.putFloat(i, (float)red);
                    byteBuffer.putFloat(i + 4, (float)green);
                    byteBuffer.putFloat(i + 8, (float)blue);
                    byteBuffer.putFloat(i + 12, (float)alpha);
                    break;

                case USHORT:
                case SHORT:
                    byteBuffer.putShort(i, (short)red);
                    byteBuffer.putShort(i + 2, (short)green);
                    byteBuffer.putShort(i + 4, (short)blue);
                    byteBuffer.putShort(i + 6, (short)alpha);
                    break;

                case UBYTE:
                case BYTE:
                    if (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN)
                    {
                        byteBuffer.put(i, (byte)red);
                        byteBuffer.put(i + 1, (byte)green);
                        byteBuffer.put(i + 2, (byte)blue);
                        byteBuffer.put(i + 3, (byte)alpha);
                    }
                    else
                    {
                        byteBuffer.put(i, (byte)alpha);
                        byteBuffer.put(i + 1, (byte)blue);
                        byteBuffer.put(i + 2, (byte)green);
                        byteBuffer.put(i + 3, (byte)red);
                    }
            }

            nextVertexFormatIndex();
            return this;
        }
    }

    public void addVertexData(int[] vertexData)
    {
        if (Config.isShaders())
        {
            SVertexBuilder.beginAddVertexData(this, vertexData);
        }

        growBuffer(vertexData.length * 4);
        rawIntBuffer.position(getBufferSize());
        rawIntBuffer.put(vertexData);
        vertexCount += vertexData.length / vertexFormat.getIntegerSize();

        if (Config.isShaders())
        {
            SVertexBuilder.endAddVertexData(this);
        }
    }

    public void endVertex()
    {
        ++vertexCount;
        growBuffer(vertexFormat.getNextOffset());
        vertexFormatIndex = 0;
        vertexFormatElement = vertexFormat.getElement(vertexFormatIndex);

        if (Config.isShaders())
        {
            SVertexBuilder.endAddVertex(this);
        }
    }

    public BufferBuilder pos(double x, double y, double z)
    {
        if (Config.isShaders())
        {
            SVertexBuilder.beginAddVertex(this);
        }

        int i = vertexCount * vertexFormat.getNextOffset() + vertexFormat.getOffset(vertexFormatIndex);

        switch (vertexFormatElement.getType())
        {
            case FLOAT:
                byteBuffer.putFloat(i, (float)(x + xOffset));
                byteBuffer.putFloat(i + 4, (float)(y + yOffset));
                byteBuffer.putFloat(i + 8, (float)(z + zOffset));
                break;

            case UINT:
            case INT:
                byteBuffer.putInt(i, Float.floatToRawIntBits((float)(x + xOffset)));
                byteBuffer.putInt(i + 4, Float.floatToRawIntBits((float)(y + yOffset)));
                byteBuffer.putInt(i + 8, Float.floatToRawIntBits((float)(z + zOffset)));
                break;

            case USHORT:
            case SHORT:
                byteBuffer.putShort(i, (short)((int)(x + xOffset)));
                byteBuffer.putShort(i + 2, (short)((int)(y + yOffset)));
                byteBuffer.putShort(i + 4, (short)((int)(z + zOffset)));
                break;

            case UBYTE:
            case BYTE:
                byteBuffer.put(i, (byte)((int)(x + xOffset)));
                byteBuffer.put(i + 1, (byte)((int)(y + yOffset)));
                byteBuffer.put(i + 2, (byte)((int)(z + zOffset)));
        }

        nextVertexFormatIndex();
        return this;
    }

    public void putNormal(float x, float y, float z)
    {
        int i = (byte)((int)(x * 127.0F)) & 255;
        int j = (byte)((int)(y * 127.0F)) & 255;
        int k = (byte)((int)(z * 127.0F)) & 255;
        int l = i | j << 8 | k << 16;
        int i1 = vertexFormat.getNextOffset() >> 2;
        int j1 = (vertexCount - 4) * i1 + vertexFormat.getNormalOffset() / 4;
        rawIntBuffer.put(j1, l);
        rawIntBuffer.put(j1 + i1, l);
        rawIntBuffer.put(j1 + i1 * 2, l);
        rawIntBuffer.put(j1 + i1 * 3, l);
    }

    private void nextVertexFormatIndex()
    {
        ++vertexFormatIndex;
        vertexFormatIndex %= vertexFormat.getElementCount();
        vertexFormatElement = vertexFormat.getElement(vertexFormatIndex);

        if (vertexFormatElement.getUsage() == VertexFormatElement.EnumUsage.PADDING)
        {
            nextVertexFormatIndex();
        }
    }

    public BufferBuilder normal(float x, float y, float z)
    {
        int i = vertexCount * vertexFormat.getNextOffset() + vertexFormat.getOffset(vertexFormatIndex);

        switch (vertexFormatElement.getType())
        {
            case FLOAT:
                byteBuffer.putFloat(i, x);
                byteBuffer.putFloat(i + 4, y);
                byteBuffer.putFloat(i + 8, z);
                break;

            case UINT:
            case INT:
                byteBuffer.putInt(i, (int)x);
                byteBuffer.putInt(i + 4, (int)y);
                byteBuffer.putInt(i + 8, (int)z);
                break;

            case USHORT:
            case SHORT:
                byteBuffer.putShort(i, (short)((int)(x * 32767.0F) & 65535));
                byteBuffer.putShort(i + 2, (short)((int)(y * 32767.0F) & 65535));
                byteBuffer.putShort(i + 4, (short)((int)(z * 32767.0F) & 65535));
                break;

            case UBYTE:
            case BYTE:
                byteBuffer.put(i, (byte)((int)(x * 127.0F) & 255));
                byteBuffer.put(i + 1, (byte)((int)(y * 127.0F) & 255));
                byteBuffer.put(i + 2, (byte)((int)(z * 127.0F) & 255));
        }

        nextVertexFormatIndex();
        return this;
    }

    public void setTranslation(double x, double y, double z)
    {
        xOffset = x;
        yOffset = y;
        zOffset = z;
    }

    public void finishDrawing()
    {
        if (!isDrawing)
        {
            throw new IllegalStateException("Not building!");
        }
        else
        {
            isDrawing = false;
            byteBuffer.position(0);
            byteBuffer.limit(getBufferSize() * 4);
        }
    }

    public ByteBuffer getByteBuffer()
    {
        return byteBuffer;
    }

    public VertexFormat getVertexFormat()
    {
        return vertexFormat;
    }

    public int getVertexCount()
    {
        return vertexCount;
    }

    public int getDrawMode()
    {
        return drawMode;
    }

    public void putColor4(int argb)
    {
        for (int i = 0; i < 4; ++i)
        {
            func_192836_a(argb, i + 1);
        }
    }

    public void putColorRGB_F4(float red, float green, float blue)
    {
        for (int i = 0; i < 4; ++i)
        {
            putColorRGB_F(red, green, blue, i + 1);
        }
    }

    public void putSprite(TextureAtlasSprite p_putSprite_1_)
    {
        if (quadSprites != null)
        {
            int i = vertexCount / 4;
            quadSprites[i - 1] = p_putSprite_1_;
        }
    }

    public void setSprite(TextureAtlasSprite p_setSprite_1_)
    {
        if (quadSprites != null)
        {
            quadSprite = p_setSprite_1_;
        }
    }

    public boolean isMultiTexture()
    {
        return quadSprites != null;
    }

    public void drawMultiTexture()
    {
        if (quadSprites != null)
        {
            int i = Minecraft.getTextureMapBlocks().getCountRegisteredSprites();

            if (drawnIcons.length <= i)
            {
                drawnIcons = new boolean[i + 1];
            }

            Arrays.fill(drawnIcons, false);
            int j = 0;
            int k = -1;
            int l = vertexCount / 4;

            for (int i1 = 0; i1 < l; ++i1)
            {
                TextureAtlasSprite textureatlassprite = quadSprites[i1];

                if (textureatlassprite != null)
                {
                    int j1 = textureatlassprite.getIndexInMap();

                    if (!drawnIcons[j1])
                    {
                        if (textureatlassprite == TextureUtils.iconGrassSideOverlay)
                        {
                            if (k < 0)
                            {
                                k = i1;
                            }
                        }
                        else
                        {
                            i1 = drawForIcon(textureatlassprite, i1) - 1;
                            ++j;

                            if (blockLayer != BlockRenderLayer.TRANSLUCENT)
                            {
                                drawnIcons[j1] = true;
                            }
                        }
                    }
                }
            }

            if (k >= 0)
            {
                drawForIcon(TextureUtils.iconGrassSideOverlay, k);
                ++j;
            }

            if (j > 0)
            {
            }
        }
    }

    private int drawForIcon(TextureAtlasSprite p_drawForIcon_1_, int p_drawForIcon_2_)
    {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, p_drawForIcon_1_.glSpriteTextureId);
        int i = -1;
        int j = -1;
        int k = vertexCount / 4;

        for (int l = p_drawForIcon_2_; l < k; ++l)
        {
            TextureAtlasSprite textureatlassprite = quadSprites[l];

            if (textureatlassprite == p_drawForIcon_1_)
            {
                if (j < 0)
                {
                    j = l;
                }
            }
            else if (j >= 0)
            {
                draw(j, l);

                if (blockLayer == BlockRenderLayer.TRANSLUCENT)
                {
                    return l;
                }

                j = -1;

                if (i < 0)
                {
                    i = l;
                }
            }
        }

        if (j >= 0)
        {
            draw(j, k);
        }

        if (i < 0)
        {
            i = k;
        }

        return i;
    }

    private void draw(int p_draw_1_, int p_draw_2_)
    {
        int i = p_draw_2_ - p_draw_1_;

        if (i > 0)
        {
            int j = p_draw_1_ * 4;
            int k = i * 4;
            GL11.glDrawArrays(drawMode, j, k);
        }
    }

    public void setBlockLayer(BlockRenderLayer p_setBlockLayer_1_)
    {
        blockLayer = p_setBlockLayer_1_;

        if (p_setBlockLayer_1_ == null)
        {
            if (quadSprites != null)
            {
                quadSpritesPrev = quadSprites;
            }

            quadSprites = null;
            quadSprite = null;
        }
    }

    private int getBufferQuadSize()
    {
        int i = rawIntBuffer.capacity() * 4 / (vertexFormat.getIntegerSize() * 4);
        return i;
    }

    public RenderEnv getRenderEnv(IBlockAccess p_getRenderEnv_1_, IBlockState p_getRenderEnv_2_, BlockPos p_getRenderEnv_3_)
    {
        if (renderEnv == null)
        {
            renderEnv = new RenderEnv(p_getRenderEnv_1_, p_getRenderEnv_2_, p_getRenderEnv_3_);
            return renderEnv;
        }
        else
        {
            renderEnv.reset(p_getRenderEnv_1_, p_getRenderEnv_2_, p_getRenderEnv_3_);
            return renderEnv;
        }
    }

    public boolean isDrawing()
    {
        return isDrawing;
    }

    public double getXOffset()
    {
        return xOffset;
    }

    public double getYOffset()
    {
        return yOffset;
    }

    public double getZOffset()
    {
        return zOffset;
    }

    public void putColorRGBA(int p_putColorRGBA_1_, int p_putColorRGBA_2_, int p_putColorRGBA_3_, int p_putColorRGBA_4_, int p_putColorRGBA_5_)
    {
        if (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN)
        {
            rawIntBuffer.put(p_putColorRGBA_1_, p_putColorRGBA_5_ << 24 | p_putColorRGBA_4_ << 16 | p_putColorRGBA_3_ << 8 | p_putColorRGBA_2_);
        }
        else
        {
            rawIntBuffer.put(p_putColorRGBA_1_, p_putColorRGBA_2_ << 24 | p_putColorRGBA_3_ << 16 | p_putColorRGBA_4_ << 8 | p_putColorRGBA_5_);
        }
    }

    public boolean isColorDisabled()
    {
        return noColor;
    }

    public class State
    {
        private final int[] stateRawBuffer;
        private final VertexFormat stateVertexFormat;
        private TextureAtlasSprite[] stateQuadSprites;

        public State(int[] p_i5_2_, VertexFormat p_i5_3_, TextureAtlasSprite[] p_i5_4_)
        {
            stateRawBuffer = p_i5_2_;
            stateVertexFormat = p_i5_3_;
            stateQuadSprites = p_i5_4_;
        }

        public State(int[] buffer, VertexFormat format)
        {
            stateRawBuffer = buffer;
            stateVertexFormat = format;
        }

        public int[] getRawBuffer()
        {
            return stateRawBuffer;
        }

        public int getVertexCount()
        {
            return stateRawBuffer.length / stateVertexFormat.getIntegerSize();
        }

        public VertexFormat getVertexFormat()
        {
            return stateVertexFormat;
        }
    }
}
