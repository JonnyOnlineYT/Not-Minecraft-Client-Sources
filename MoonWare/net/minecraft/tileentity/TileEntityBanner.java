package net.minecraft.tileentity;

import com.google.common.collect.Lists;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemBanner;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.text.Component;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TranslatableComponent;
import net.minecraft.world.IWorldNameable;

public class TileEntityBanner extends TileEntity implements IWorldNameable
{
    private String field_190617_a;
    private EnumDyeColor baseColor = EnumDyeColor.BLACK;

    /** A list of all the banner patterns. */
    private NBTTagList patterns;
    private boolean patternDataSet;
    private List<BannerPattern> patternList;
    private List<EnumDyeColor> colorList;

    /**
     * This is a String representation of this banners pattern and color lists, used for texture caching.
     */
    private String patternResourceLocation;

    public void setItemValues(ItemStack stack, boolean p_175112_2_)
    {
        patterns = null;
        NBTTagCompound nbttagcompound = stack.getSubCompound("BlockEntityTag");

        if (nbttagcompound != null && nbttagcompound.hasKey("Patterns", 9))
        {
            patterns = nbttagcompound.getTagList("Patterns", 10).copy();
        }

        baseColor = p_175112_2_ ? func_190616_d(stack) : ItemBanner.getBaseColor(stack);
        patternList = null;
        colorList = null;
        patternResourceLocation = "";
        patternDataSet = true;
        field_190617_a = stack.hasDisplayName() ? stack.getDisplayName() : null;
    }

    /**
     * Get the name of this object. For players this returns their username
     */
    public String getName()
    {
        return hasCustomName() ? field_190617_a : "banner";
    }

    /**
     * Returns true if this thing is named
     */
    public boolean hasCustomName()
    {
        return field_190617_a != null && !field_190617_a.isEmpty();
    }

    /**
     * Get the formatted ChatComponent that will be used for the sender's username in chat
     */
    public Component getDisplayName()
    {
        return hasCustomName() ? new TextComponent(getName()) : new TranslatableComponent(getName());
    }

    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setInteger("Base", baseColor.getDyeDamage());

        if (patterns != null)
        {
            compound.setTag("Patterns", patterns);
        }

        if (hasCustomName())
        {
            compound.setString("CustomName", field_190617_a);
        }

        return compound;
    }

    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);

        if (compound.hasKey("CustomName", 8))
        {
            field_190617_a = compound.getString("CustomName");
        }

        baseColor = EnumDyeColor.byDyeDamage(compound.getInteger("Base"));
        patterns = compound.getTagList("Patterns", 10);
        patternList = null;
        colorList = null;
        patternResourceLocation = null;
        patternDataSet = true;
    }

    @Nullable
    public SPacketUpdateTileEntity getUpdatePacket()
    {
        return new SPacketUpdateTileEntity(pos, 6, getUpdateTag());
    }

    public NBTTagCompound getUpdateTag()
    {
        return writeToNBT(new NBTTagCompound());
    }

    /**
     * Retrieves the amount of patterns stored on an ItemStack. If the tag does not exist this value will be 0.
     */
    public static int getPatterns(ItemStack stack)
    {
        NBTTagCompound nbttagcompound = stack.getSubCompound("BlockEntityTag");
        return nbttagcompound != null && nbttagcompound.hasKey("Patterns") ? nbttagcompound.getTagList("Patterns", 10).tagCount() : 0;
    }

    public List<BannerPattern> getPatternList()
    {
        initializeBannerData();
        return patternList;
    }

    public List<EnumDyeColor> getColorList()
    {
        initializeBannerData();
        return colorList;
    }

    public String getPatternResourceLocation()
    {
        initializeBannerData();
        return patternResourceLocation;
    }

    /**
     * Establishes all of the basic properties for the banner. This will also apply the data from the tile entities nbt
     * tag compounds.
     */
    private void initializeBannerData()
    {
        if (patternList == null || colorList == null || patternResourceLocation == null)
        {
            if (!patternDataSet)
            {
                patternResourceLocation = "";
            }
            else
            {
                patternList = Lists.newArrayList();
                colorList = Lists.newArrayList();
                patternList.add(BannerPattern.BASE);
                colorList.add(baseColor);
                patternResourceLocation = "b" + baseColor.getDyeDamage();

                if (patterns != null)
                {
                    for (int i = 0; i < patterns.tagCount(); ++i)
                    {
                        NBTTagCompound nbttagcompound = patterns.getCompoundTagAt(i);
                        BannerPattern bannerpattern = BannerPattern.func_190994_a(nbttagcompound.getString("Pattern"));

                        if (bannerpattern != null)
                        {
                            patternList.add(bannerpattern);
                            int j = nbttagcompound.getInteger("Color");
                            colorList.add(EnumDyeColor.byDyeDamage(j));
                            patternResourceLocation = patternResourceLocation + bannerpattern.func_190993_b() + j;
                        }
                    }
                }
            }
        }
    }

    /**
     * Removes all the banner related data from a provided instance of ItemStack.
     */
    public static void removeBannerData(ItemStack stack)
    {
        NBTTagCompound nbttagcompound = stack.getSubCompound("BlockEntityTag");

        if (nbttagcompound != null && nbttagcompound.hasKey("Patterns", 9))
        {
            NBTTagList nbttaglist = nbttagcompound.getTagList("Patterns", 10);

            if (!nbttaglist.hasNoTags())
            {
                nbttaglist.removeTag(nbttaglist.tagCount() - 1);

                if (nbttaglist.hasNoTags())
                {
                    stack.getTagCompound().removeTag("BlockEntityTag");

                    if (stack.getTagCompound().hasNoTags())
                    {
                        stack.setTagCompound(null);
                    }
                }
            }
        }
    }

    public ItemStack func_190615_l()
    {
        ItemStack itemstack = ItemBanner.func_190910_a(baseColor, patterns);

        if (hasCustomName())
        {
            itemstack.setStackDisplayName(getName());
        }

        return itemstack;
    }

    public static EnumDyeColor func_190616_d(ItemStack p_190616_0_)
    {
        NBTTagCompound nbttagcompound = p_190616_0_.getSubCompound("BlockEntityTag");
        return nbttagcompound != null && nbttagcompound.hasKey("Base") ? EnumDyeColor.byDyeDamage(nbttagcompound.getInteger("Base")) : EnumDyeColor.BLACK;
    }
}
