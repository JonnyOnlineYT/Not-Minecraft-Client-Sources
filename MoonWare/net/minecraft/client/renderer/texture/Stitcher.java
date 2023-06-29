package net.minecraft.client.renderer.texture;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import net.minecraft.client.renderer.StitcherException;
import net.minecraft.util.math.MathHelper;
import optifine.MathUtils;

public class Stitcher
{
    private final int mipmapLevelStitcher;
    private final Set<Stitcher.Holder> setStitchHolders = Sets.newHashSetWithExpectedSize(256);
    private final List<Stitcher.Slot> stitchSlots = Lists.newArrayListWithCapacity(256);
    private int currentWidth;
    private int currentHeight;
    private final int maxWidth;
    private final int maxHeight;

    /** Max size (width or height) of a single tile */
    private final int maxTileDimension;

    public Stitcher(int maxWidthIn, int maxHeightIn, int maxTileDimensionIn, int mipmapLevelStitcherIn)
    {
        mipmapLevelStitcher = mipmapLevelStitcherIn;
        maxWidth = maxWidthIn;
        maxHeight = maxHeightIn;
        maxTileDimension = maxTileDimensionIn;
    }

    public int getCurrentWidth()
    {
        return currentWidth;
    }

    public int getCurrentHeight()
    {
        return currentHeight;
    }

    public void addSprite(TextureAtlasSprite textureAtlas)
    {
        Stitcher.Holder stitcher$holder = new Stitcher.Holder(textureAtlas, mipmapLevelStitcher);

        if (maxTileDimension > 0)
        {
            stitcher$holder.setNewDimension(maxTileDimension);
        }

        setStitchHolders.add(stitcher$holder);
    }

    public void doStitch()
    {
        Stitcher.Holder[] astitcher$holder = setStitchHolders.toArray(new Holder[setStitchHolders.size()]);
        Arrays.sort(astitcher$holder);

        for (Stitcher.Holder stitcher$holder : astitcher$holder)
        {
            if (!allocateSlot(stitcher$holder))
            {
                String s = String.format("Unable to fit: %s, size: %dx%d, atlas: %dx%d, atlasMax: %dx%d - Maybe try a lower resolution resourcepack?", stitcher$holder.getAtlasSprite().getIconName(), stitcher$holder.getAtlasSprite().getIconWidth(), stitcher$holder.getAtlasSprite().getIconHeight(), currentWidth, currentHeight, maxWidth, maxHeight);
                throw new StitcherException(stitcher$holder, s);
            }
        }

        currentWidth = MathHelper.smallestEncompassingPowerOfTwo(currentWidth);
        currentHeight = MathHelper.smallestEncompassingPowerOfTwo(currentHeight);
    }

    public List<TextureAtlasSprite> getStichSlots()
    {
        List<Stitcher.Slot> list = Lists.newArrayList();

        for (Stitcher.Slot stitcher$slot : stitchSlots)
        {
            stitcher$slot.getAllStitchSlots(list);
        }

        List<TextureAtlasSprite> list1 = Lists.newArrayList();

        for (Stitcher.Slot stitcher$slot1 : list)
        {
            Stitcher.Holder stitcher$holder = stitcher$slot1.getStitchHolder();
            TextureAtlasSprite textureatlassprite = stitcher$holder.getAtlasSprite();
            textureatlassprite.initSprite(currentWidth, currentHeight, stitcher$slot1.getOriginX(), stitcher$slot1.getOriginY(), stitcher$holder.isRotated());
            list1.add(textureatlassprite);
        }

        return list1;
    }

    private static int getMipmapDimension(int p_147969_0_, int p_147969_1_)
    {
        return (p_147969_0_ >> p_147969_1_) + ((p_147969_0_ & (1 << p_147969_1_) - 1) == 0 ? 0 : 1) << p_147969_1_;
    }

    /**
     * Attempts to find space for specified tile
     */
    private boolean allocateSlot(Stitcher.Holder p_94310_1_)
    {
        TextureAtlasSprite textureatlassprite = p_94310_1_.getAtlasSprite();
        boolean flag = textureatlassprite.getIconWidth() != textureatlassprite.getIconHeight();

        for (int i = 0; i < stitchSlots.size(); ++i)
        {
            if (stitchSlots.get(i).addSlot(p_94310_1_))
            {
                return true;
            }

            if (flag)
            {
                p_94310_1_.rotate();

                if (stitchSlots.get(i).addSlot(p_94310_1_))
                {
                    return true;
                }

                p_94310_1_.rotate();
            }
        }

        return expandAndAllocateSlot(p_94310_1_);
    }

    /**
     * Expand stitched texture in order to make space for specified tile
     */
    private boolean expandAndAllocateSlot(Stitcher.Holder p_94311_1_)
    {
        int i = Math.min(p_94311_1_.getWidth(), p_94311_1_.getHeight());
        int j = Math.max(p_94311_1_.getWidth(), p_94311_1_.getHeight());
        int k = MathHelper.smallestEncompassingPowerOfTwo(currentWidth);
        int l = MathHelper.smallestEncompassingPowerOfTwo(currentHeight);
        int i1 = MathHelper.smallestEncompassingPowerOfTwo(currentWidth + i);
        int j1 = MathHelper.smallestEncompassingPowerOfTwo(currentHeight + i);
        boolean flag = i1 <= maxWidth;
        boolean flag1 = j1 <= maxHeight;

        if (!flag && !flag1)
        {
            return false;
        }
        else
        {
            int k1 = MathUtils.roundDownToPowerOfTwo(currentHeight);
            boolean flag2 = flag && i1 <= 2 * k1;

            if (currentWidth == 0 && currentHeight == 0)
            {
                flag2 = true;
            }

            Stitcher.Slot stitcher$slot;

            if (flag2)
            {
                if (p_94311_1_.getWidth() > p_94311_1_.getHeight())
                {
                    p_94311_1_.rotate();
                }

                if (currentHeight == 0)
                {
                    currentHeight = p_94311_1_.getHeight();
                }

                stitcher$slot = new Stitcher.Slot(currentWidth, 0, p_94311_1_.getWidth(), currentHeight);
                currentWidth += p_94311_1_.getWidth();
            }
            else
            {
                stitcher$slot = new Stitcher.Slot(0, currentHeight, currentWidth, p_94311_1_.getHeight());
                currentHeight += p_94311_1_.getHeight();
            }

            stitcher$slot.addSlot(p_94311_1_);
            stitchSlots.add(stitcher$slot);
            return true;
        }
    }

    public static class Holder implements Comparable<Stitcher.Holder>
    {
        private final TextureAtlasSprite theTexture;
        private final int width;
        private final int height;
        private final int mipmapLevelHolder;
        private boolean rotated;
        private float scaleFactor = 1.0F;

        public Holder(TextureAtlasSprite theTextureIn, int mipmapLevelHolderIn)
        {
            theTexture = theTextureIn;
            width = theTextureIn.getIconWidth();
            height = theTextureIn.getIconHeight();
            mipmapLevelHolder = mipmapLevelHolderIn;
            rotated = getMipmapDimension(height, mipmapLevelHolderIn) > getMipmapDimension(width, mipmapLevelHolderIn);
        }

        public TextureAtlasSprite getAtlasSprite()
        {
            return theTexture;
        }

        public int getWidth()
        {
            int i = rotated ? height : width;
            return getMipmapDimension((int)((float)i * scaleFactor), mipmapLevelHolder);
        }

        public int getHeight()
        {
            int i = rotated ? width : height;
            return getMipmapDimension((int)((float)i * scaleFactor), mipmapLevelHolder);
        }

        public void rotate()
        {
            rotated = !rotated;
        }

        public boolean isRotated()
        {
            return rotated;
        }

        public void setNewDimension(int p_94196_1_)
        {
            if (width > p_94196_1_ && height > p_94196_1_)
            {
                scaleFactor = (float)p_94196_1_ / (float)Math.min(width, height);
            }
        }

        public String toString()
        {
            return "Holder{width=" + width + ", height=" + height + ", name=" + theTexture.getIconName() + '}';
        }

        public int compareTo(Stitcher.Holder p_compareTo_1_)
        {
            int i;

            if (getHeight() == p_compareTo_1_.getHeight())
            {
                if (getWidth() == p_compareTo_1_.getWidth())
                {
                    if (theTexture.getIconName() == null)
                    {
                        return p_compareTo_1_.theTexture.getIconName() == null ? 0 : -1;
                    }

                    return theTexture.getIconName().compareTo(p_compareTo_1_.theTexture.getIconName());
                }

                i = getWidth() < p_compareTo_1_.getWidth() ? 1 : -1;
            }
            else
            {
                i = getHeight() < p_compareTo_1_.getHeight() ? 1 : -1;
            }

            return i;
        }
    }

    public static class Slot
    {
        private final int originX;
        private final int originY;
        private final int width;
        private final int height;
        private List<Stitcher.Slot> subSlots;
        private Stitcher.Holder holder;

        public Slot(int originXIn, int originYIn, int widthIn, int heightIn)
        {
            originX = originXIn;
            originY = originYIn;
            width = widthIn;
            height = heightIn;
        }

        public Stitcher.Holder getStitchHolder()
        {
            return holder;
        }

        public int getOriginX()
        {
            return originX;
        }

        public int getOriginY()
        {
            return originY;
        }

        public boolean addSlot(Stitcher.Holder holderIn)
        {
            if (holder != null)
            {
                return false;
            }
            else
            {
                int i = holderIn.getWidth();
                int j = holderIn.getHeight();

                if (i <= width && j <= height)
                {
                    if (i == width && j == height)
                    {
                        holder = holderIn;
                        return true;
                    }
                    else
                    {
                        if (subSlots == null)
                        {
                            subSlots = Lists.newArrayListWithCapacity(1);
                            subSlots.add(new Stitcher.Slot(originX, originY, i, j));
                            int k = width - i;
                            int l = height - j;

                            if (l > 0 && k > 0)
                            {
                                int i1 = Math.max(height, k);
                                int j1 = Math.max(width, l);

                                if (i1 >= j1)
                                {
                                    subSlots.add(new Stitcher.Slot(originX, originY + j, i, l));
                                    subSlots.add(new Stitcher.Slot(originX + i, originY, k, height));
                                }
                                else
                                {
                                    subSlots.add(new Stitcher.Slot(originX + i, originY, k, j));
                                    subSlots.add(new Stitcher.Slot(originX, originY + j, width, l));
                                }
                            }
                            else if (k == 0)
                            {
                                subSlots.add(new Stitcher.Slot(originX, originY + j, i, l));
                            }
                            else if (l == 0)
                            {
                                subSlots.add(new Stitcher.Slot(originX + i, originY, k, j));
                            }
                        }

                        for (Stitcher.Slot stitcher$slot : subSlots)
                        {
                            if (stitcher$slot.addSlot(holderIn))
                            {
                                return true;
                            }
                        }

                        return false;
                    }
                }
                else
                {
                    return false;
                }
            }
        }

        public void getAllStitchSlots(List<Stitcher.Slot> p_94184_1_)
        {
            if (holder != null)
            {
                p_94184_1_.add(this);
            }
            else if (subSlots != null)
            {
                for (Stitcher.Slot stitcher$slot : subSlots)
                {
                    stitcher$slot.getAllStitchSlots(p_94184_1_);
                }
            }
        }

        public String toString()
        {
            return "Slot{originX=" + originX + ", originY=" + originY + ", width=" + width + ", height=" + height + ", texture=" + holder + ", subSlots=" + subSlots + '}';
        }
    }
}
