package net.minecraft.entity.item;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityPainting extends EntityHanging
{
    public EntityPainting.EnumArt art;

    public EntityPainting(World worldIn)
    {
        super(worldIn);
    }

    public EntityPainting(World worldIn, BlockPos pos, EnumFacing facing)
    {
        super(worldIn, pos);
        List<EntityPainting.EnumArt> list = Lists.newArrayList();
        int i = 0;

        for (EntityPainting.EnumArt entitypainting$enumart : EntityPainting.EnumArt.values())
        {
            art = entitypainting$enumart;
            updateFacingWithBoundingBox(facing);

            if (onValidSurface())
            {
                list.add(entitypainting$enumart);
                int j = entitypainting$enumart.sizeX * entitypainting$enumart.sizeY;

                if (j > i)
                {
                    i = j;
                }
            }
        }

        if (!list.isEmpty())
        {
            Iterator<EntityPainting.EnumArt> iterator = list.iterator();

            while (iterator.hasNext())
            {
                EntityPainting.EnumArt entitypainting$enumart1 = iterator.next();

                if (entitypainting$enumart1.sizeX * entitypainting$enumart1.sizeY < i)
                {
                    iterator.remove();
                }
            }

            art = list.get(rand.nextInt(list.size()));
        }

        updateFacingWithBoundingBox(facing);
    }

    public EntityPainting(World worldIn, BlockPos pos, EnumFacing facing, String title)
    {
        this(worldIn, pos, facing);

        for (EntityPainting.EnumArt entitypainting$enumart : EntityPainting.EnumArt.values())
        {
            if (entitypainting$enumart.title.equals(title))
            {
                art = entitypainting$enumart;
                break;
            }
        }

        updateFacingWithBoundingBox(facing);
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        compound.setString("Motive", art.title);
        super.writeEntityToNBT(compound);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        String s = compound.getString("Motive");

        for (EntityPainting.EnumArt entitypainting$enumart : EntityPainting.EnumArt.values())
        {
            if (entitypainting$enumart.title.equals(s))
            {
                art = entitypainting$enumart;
            }
        }

        if (art == null)
        {
            art = EntityPainting.EnumArt.KEBAB;
        }

        super.readEntityFromNBT(compound);
    }

    public int getWidthPixels()
    {
        return art.sizeX;
    }

    public int getHeightPixels()
    {
        return art.sizeY;
    }

    /**
     * Called when this entity is broken. Entity parameter may be null.
     */
    public void onBroken(@Nullable Entity brokenEntity)
    {
        if (world.getGameRules().getBoolean("doEntityDrops"))
        {
            playSound(SoundEvents.ENTITY_PAINTING_BREAK, 1.0F, 1.0F);

            if (brokenEntity instanceof EntityPlayer)
            {
                EntityPlayer entityplayer = (EntityPlayer)brokenEntity;

                if (entityplayer.capabilities.isCreativeMode)
                {
                    return;
                }
            }

            entityDropItem(new ItemStack(Items.PAINTING), 0.0F);
        }
    }

    public void playPlaceSound()
    {
        playSound(SoundEvents.ENTITY_PAINTING_PLACE, 1.0F, 1.0F);
    }

    /**
     * Sets the location and Yaw/Pitch of an entity in the world
     */
    public void setLocationAndAngles(double x, double y, double z, float yaw, float pitch)
    {
        setPosition(x, y, z);
    }

    /**
     * Set the position and rotation values directly without any clamping.
     */
    public void setPositionAndRotationDirect(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport)
    {
        BlockPos blockpos = hangingPosition.add(x - posX, y - posY, z - posZ);
        setPosition(blockpos.getX(), blockpos.getY(), blockpos.getZ());
    }

    public enum EnumArt
    {
        KEBAB("Kebab", 16, 16, 0, 0),
        AZTEC("Aztec", 16, 16, 16, 0),
        ALBAN("Alban", 16, 16, 32, 0),
        AZTEC_2("Aztec2", 16, 16, 48, 0),
        BOMB("Bomb", 16, 16, 64, 0),
        PLANT("Plant", 16, 16, 80, 0),
        WASTELAND("Wasteland", 16, 16, 96, 0),
        POOL("Pool", 32, 16, 0, 32),
        COURBET("Courbet", 32, 16, 32, 32),
        SEA("Sea", 32, 16, 64, 32),
        SUNSET("Sunset", 32, 16, 96, 32),
        CREEBET("Creebet", 32, 16, 128, 32),
        WANDERER("Wanderer", 16, 32, 0, 64),
        GRAHAM("Graham", 16, 32, 16, 64),
        MATCH("Match", 32, 32, 0, 128),
        BUST("Bust", 32, 32, 32, 128),
        STAGE("Stage", 32, 32, 64, 128),
        VOID("Void", 32, 32, 96, 128),
        SKULL_AND_ROSES("SkullAndRoses", 32, 32, 128, 128),
        WITHER("Wither", 32, 32, 160, 128),
        FIGHTERS("Fighters", 64, 32, 0, 96),
        POINTER("Pointer", 64, 64, 0, 192),
        PIGSCENE("Pigscene", 64, 64, 64, 192),
        BURNING_SKULL("BurningSkull", 64, 64, 128, 192),
        SKELETON("Skeleton", 64, 48, 192, 64),
        DONKEY_KONG("DonkeyKong", 64, 48, 192, 112);

        public static final int MAX_NAME_LENGTH = "SkullAndRoses".length();
        public final String title;
        public final int sizeX;
        public final int sizeY;
        public final int offsetX;
        public final int offsetY;

        EnumArt(String titleIn, int width, int height, int textureU, int textureV)
        {
            title = titleIn;
            sizeX = width;
            sizeY = height;
            offsetX = textureU;
            offsetY = textureV;
        }
    }
}
