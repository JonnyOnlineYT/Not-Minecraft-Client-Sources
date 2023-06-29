package net.minecraft.client.renderer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.ImageObserver;
import javax.annotation.Nullable;

public class ImageBufferDownload implements IImageBuffer
{
    private int[] imageData;
    private int imageWidth;
    private int imageHeight;

    @Nullable
    public BufferedImage parseUserSkin(BufferedImage image)
    {
        if (image == null)
        {
            return null;
        }
        else
        {
            imageWidth = 64;
            imageHeight = 64;
            int i = image.getWidth();
            int j = image.getHeight();
            int k;

            for (k = 1; imageWidth < i || imageHeight < j; k *= 2)
            {
                imageWidth *= 2;
                imageHeight *= 2;
            }

            BufferedImage bufferedimage = new BufferedImage(imageWidth, imageHeight, 2);
            Graphics graphics = bufferedimage.getGraphics();
            graphics.drawImage(image, 0, 0, null);
            boolean flag = image.getHeight() == 32 * k;

            if (flag)
            {
                graphics.setColor(new Color(0, 0, 0, 0));
                graphics.fillRect(0 * k, 32 * k, 64 * k, 32 * k);
                graphics.drawImage(bufferedimage, 24 * k, 48 * k, 20 * k, 52 * k, 4 * k, 16 * k, 8 * k, 20 * k, null);
                graphics.drawImage(bufferedimage, 28 * k, 48 * k, 24 * k, 52 * k, 8 * k, 16 * k, 12 * k, 20 * k, null);
                graphics.drawImage(bufferedimage, 20 * k, 52 * k, 16 * k, 64 * k, 8 * k, 20 * k, 12 * k, 32 * k, null);
                graphics.drawImage(bufferedimage, 24 * k, 52 * k, 20 * k, 64 * k, 4 * k, 20 * k, 8 * k, 32 * k, null);
                graphics.drawImage(bufferedimage, 28 * k, 52 * k, 24 * k, 64 * k, 0 * k, 20 * k, 4 * k, 32 * k, null);
                graphics.drawImage(bufferedimage, 32 * k, 52 * k, 28 * k, 64 * k, 12 * k, 20 * k, 16 * k, 32 * k, null);
                graphics.drawImage(bufferedimage, 40 * k, 48 * k, 36 * k, 52 * k, 44 * k, 16 * k, 48 * k, 20 * k, null);
                graphics.drawImage(bufferedimage, 44 * k, 48 * k, 40 * k, 52 * k, 48 * k, 16 * k, 52 * k, 20 * k, null);
                graphics.drawImage(bufferedimage, 36 * k, 52 * k, 32 * k, 64 * k, 48 * k, 20 * k, 52 * k, 32 * k, null);
                graphics.drawImage(bufferedimage, 40 * k, 52 * k, 36 * k, 64 * k, 44 * k, 20 * k, 48 * k, 32 * k, null);
                graphics.drawImage(bufferedimage, 44 * k, 52 * k, 40 * k, 64 * k, 40 * k, 20 * k, 44 * k, 32 * k, null);
                graphics.drawImage(bufferedimage, 48 * k, 52 * k, 44 * k, 64 * k, 52 * k, 20 * k, 56 * k, 32 * k, null);
            }

            graphics.dispose();
            imageData = ((DataBufferInt)bufferedimage.getRaster().getDataBuffer()).getData();
            setAreaOpaque(0 * k, 0 * k, 32 * k, 16 * k);

            if (flag)
            {
                doTransparencyHack(32 * k, 0 * k, 64 * k, 32 * k);
            }

            setAreaOpaque(0 * k, 16 * k, 64 * k, 32 * k);
            setAreaOpaque(16 * k, 48 * k, 48 * k, 64 * k);
            return bufferedimage;
        }
    }

    public void skinAvailable()
    {
    }

    private void doTransparencyHack(int p_189559_1_, int p_189559_2_, int p_189559_3_, int p_189559_4_)
    {
        for (int i = p_189559_1_; i < p_189559_3_; ++i)
        {
            for (int j = p_189559_2_; j < p_189559_4_; ++j)
            {
                int k = imageData[i + j * imageWidth];

                if ((k >> 24 & 255) < 128)
                {
                    return;
                }
            }
        }

        for (int l = p_189559_1_; l < p_189559_3_; ++l)
        {
            for (int i1 = p_189559_2_; i1 < p_189559_4_; ++i1)
            {
                imageData[l + i1 * imageWidth] &= 16777215;
            }
        }
    }

    /**
     * Makes the given area of the image opaque
     */
    private void setAreaOpaque(int x, int y, int width, int height)
    {
        for (int i = x; i < width; ++i)
        {
            for (int j = y; j < height; ++j)
            {
                imageData[i + j * imageWidth] |= -16777216;
            }
        }
    }
}
