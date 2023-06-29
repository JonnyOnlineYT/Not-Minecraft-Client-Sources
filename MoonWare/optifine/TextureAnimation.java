package optifine;

import java.nio.ByteBuffer;
import java.util.Properties;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.util.Namespaced;
import org.lwjgl.opengl.GL11;

public class TextureAnimation
{
    private String srcTex;
    private String dstTex;
    Namespaced dstTexLoc;
    private int dstTextId = -1;
    private int dstX;
    private int dstY;
    private int frameWidth;
    private int frameHeight;
    private TextureAnimationFrame[] frames;
    private int activeFrame;
    byte[] srcData;
    private ByteBuffer imageData;

    public TextureAnimation(String p_i97_1_, byte[] p_i97_2_, String p_i97_3_, Namespaced p_i97_4_, int p_i97_5_, int p_i97_6_, int p_i97_7_, int p_i97_8_, Properties p_i97_9_, int p_i97_10_)
    {
        srcTex = p_i97_1_;
        dstTex = p_i97_3_;
        dstTexLoc = p_i97_4_;
        dstX = p_i97_5_;
        dstY = p_i97_6_;
        frameWidth = p_i97_7_;
        frameHeight = p_i97_8_;
        int i = p_i97_7_ * p_i97_8_ * 4;

        if (p_i97_2_.length % i != 0)
        {
            Config.warn("Invalid animated texture length: " + p_i97_2_.length + ", frameWidth: " + p_i97_7_ + ", frameHeight: " + p_i97_8_);
        }

        srcData = p_i97_2_;
        int j = p_i97_2_.length / i;

        if (p_i97_9_.get("tile.0") != null)
        {
            for (int k = 0; p_i97_9_.get("tile." + k) != null; ++k)
            {
                j = k + 1;
            }
        }

        String s2 = (String)p_i97_9_.get("duration");
        int l = Config.parseInt(s2, p_i97_10_);
        frames = new TextureAnimationFrame[j];

        for (int i1 = 0; i1 < frames.length; ++i1)
        {
            String s = (String)p_i97_9_.get("tile." + i1);
            int j1 = Config.parseInt(s, i1);
            String s1 = (String)p_i97_9_.get("duration." + i1);
            int k1 = Config.parseInt(s1, l);
            TextureAnimationFrame textureanimationframe = new TextureAnimationFrame(j1, k1);
            frames[i1] = textureanimationframe;
        }
    }

    public boolean nextFrame()
    {
        if (frames.length <= 0)
        {
            return false;
        }
        else
        {
            if (activeFrame >= frames.length)
            {
                activeFrame = 0;
            }

            TextureAnimationFrame textureanimationframe = frames[activeFrame];
            ++textureanimationframe.counter;

            if (textureanimationframe.counter < textureanimationframe.duration)
            {
                return false;
            }
            else
            {
                textureanimationframe.counter = 0;
                ++activeFrame;

                if (activeFrame >= frames.length)
                {
                    activeFrame = 0;
                }

                return true;
            }
        }
    }

    public int getActiveFrameIndex()
    {
        if (frames.length <= 0)
        {
            return 0;
        }
        else
        {
            if (activeFrame >= frames.length)
            {
                activeFrame = 0;
            }

            TextureAnimationFrame textureanimationframe = frames[activeFrame];
            return textureanimationframe.index;
        }
    }

    public int getFrameCount()
    {
        return frames.length;
    }

    public boolean updateTexture()
    {
        if (dstTextId < 0)
        {
            ITextureObject itextureobject = TextureUtils.getTexture(dstTexLoc);

            if (itextureobject == null)
            {
                return false;
            }

            dstTextId = itextureobject.getGlTextureId();
        }

        if (imageData == null)
        {
            imageData = GLAllocation.createDirectByteBuffer(srcData.length);
            imageData.put(srcData);
            srcData = null;
        }

        if (!nextFrame())
        {
            return false;
        }
        else
        {
            int k = frameWidth * frameHeight * 4;
            int i = getActiveFrameIndex();
            int j = k * i;

            if (j + k > imageData.capacity())
            {
                return false;
            }
            else
            {
                imageData.position(j);
                GlStateManager.bindTexture(dstTextId);
                GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, dstX, dstY, frameWidth, frameHeight, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, imageData);
                return true;
            }
        }
    }

    public String getSrcTex()
    {
        return srcTex;
    }

    public String getDstTex()
    {
        return dstTex;
    }

    public Namespaced getDstTexLoc()
    {
        return dstTexLoc;
    }
}
