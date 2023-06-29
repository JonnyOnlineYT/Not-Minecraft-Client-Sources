package net.minecraft.entity.item;

import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.walkers.ItemStackData;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapData;

public class EntityItemFrame extends EntityHanging
{
    private static final DataParameter<ItemStack> ITEM = EntityDataManager.createKey(EntityItemFrame.class, DataSerializers.OPTIONAL_ITEM_STACK);
    private static final DataParameter<Integer> ROTATION = EntityDataManager.createKey(EntityItemFrame.class, DataSerializers.VARINT);

    /** Chance for this item frame's item to drop from the frame. */
    private float itemDropChance = 1.0F;

    public EntityItemFrame(World worldIn)
    {
        super(worldIn);
    }

    public EntityItemFrame(World worldIn, BlockPos p_i45852_2_, EnumFacing p_i45852_3_)
    {
        super(worldIn, p_i45852_2_);
        updateFacingWithBoundingBox(p_i45852_3_);
    }

    protected void entityInit()
    {
        getDataManager().register(ITEM, ItemStack.EMPTY);
        getDataManager().register(ROTATION, Integer.valueOf(0));
    }

    public float getCollisionBorderSize()
    {
        return 0.0F;
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        if (isEntityInvulnerable(source))
        {
            return false;
        }
        else if (!source.isExplosion() && !getDisplayedItem().isEmpty())
        {
            if (!world.isRemote)
            {
                dropItemOrSelf(source.getEntity(), false);
                playSound(SoundEvents.ENTITY_ITEMFRAME_REMOVE_ITEM, 1.0F, 1.0F);
                setDisplayedItem(ItemStack.EMPTY);
            }

            return true;
        }
        else
        {
            return super.attackEntityFrom(source, amount);
        }
    }

    public int getWidthPixels()
    {
        return 12;
    }

    public int getHeightPixels()
    {
        return 12;
    }

    /**
     * Checks if the entity is in range to render.
     */
    public boolean isInRangeToRenderDist(double distance)
    {
        double d0 = 16.0D;
        d0 = d0 * 64.0D * Entity.getRenderDistanceWeight();
        return distance < d0 * d0;
    }

    /**
     * Called when this entity is broken. Entity parameter may be null.
     */
    public void onBroken(@Nullable Entity brokenEntity)
    {
        playSound(SoundEvents.ENTITY_ITEMFRAME_BREAK, 1.0F, 1.0F);
        dropItemOrSelf(brokenEntity, true);
    }

    public void playPlaceSound()
    {
        playSound(SoundEvents.ENTITY_ITEMFRAME_PLACE, 1.0F, 1.0F);
    }

    public void dropItemOrSelf(@Nullable Entity entityIn, boolean p_146065_2_)
    {
        if (world.getGameRules().getBoolean("doEntityDrops"))
        {
            ItemStack itemstack = getDisplayedItem();

            if (entityIn instanceof EntityPlayer)
            {
                EntityPlayer entityplayer = (EntityPlayer)entityIn;

                if (entityplayer.capabilities.isCreativeMode)
                {
                    removeFrameFromMap(itemstack);
                    return;
                }
            }

            if (p_146065_2_)
            {
                entityDropItem(new ItemStack(Items.ITEM_FRAME), 0.0F);
            }

            if (!itemstack.isEmpty() && rand.nextFloat() < itemDropChance)
            {
                itemstack = itemstack.copy();
                removeFrameFromMap(itemstack);
                entityDropItem(itemstack, 0.0F);
            }
        }
    }

    /**
     * Removes the dot representing this frame's position from the map when the item frame is broken.
     */
    private void removeFrameFromMap(ItemStack stack)
    {
        if (!stack.isEmpty())
        {
            if (stack.getItem() == Items.FILLED_MAP)
            {
                MapData mapdata = ((ItemMap)stack.getItem()).getMapData(stack, world);
                mapdata.mapDecorations.remove("frame-" + getEntityId());
            }

            stack.setItemFrame(null);
        }
    }

    public ItemStack getDisplayedItem()
    {
        return getDataManager().get(ITEM);
    }

    public void setDisplayedItem(ItemStack stack)
    {
        setDisplayedItemWithUpdate(stack, true);
    }

    private void setDisplayedItemWithUpdate(ItemStack stack, boolean p_174864_2_)
    {
        if (!stack.isEmpty())
        {
            stack = stack.copy();
            stack.func_190920_e(1);
            stack.setItemFrame(this);
        }

        getDataManager().set(ITEM, stack);
        getDataManager().setDirty(ITEM);

        if (!stack.isEmpty())
        {
            playSound(SoundEvents.ENTITY_ITEMFRAME_ADD_ITEM, 1.0F, 1.0F);
        }

        if (p_174864_2_ && hangingPosition != null)
        {
            world.updateComparatorOutputLevel(hangingPosition, Blocks.AIR);
        }
    }

    public void notifyDataManagerChange(DataParameter<?> key)
    {
        if (key.equals(ITEM))
        {
            ItemStack itemstack = getDisplayedItem();

            if (!itemstack.isEmpty() && itemstack.getItemFrame() != this)
            {
                itemstack.setItemFrame(this);
            }
        }
    }

    /**
     * Return the rotation of the item currently on this frame.
     */
    public int getRotation()
    {
        return getDataManager().get(ROTATION).intValue();
    }

    public void setItemRotation(int rotationIn)
    {
        setRotation(rotationIn, true);
    }

    private void setRotation(int rotationIn, boolean p_174865_2_)
    {
        getDataManager().set(ROTATION, Integer.valueOf(rotationIn % 8));

        if (p_174865_2_ && hangingPosition != null)
        {
            world.updateComparatorOutputLevel(hangingPosition, Blocks.AIR);
        }
    }

    public static void registerFixesItemFrame(DataFixer fixer)
    {
        fixer.registerWalker(FixTypes.ENTITY, new ItemStackData(EntityItemFrame.class, "Item"));
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        if (!getDisplayedItem().isEmpty())
        {
            compound.setTag("Item", getDisplayedItem().writeToNBT(new NBTTagCompound()));
            compound.setByte("ItemRotation", (byte) getRotation());
            compound.setFloat("ItemDropChance", itemDropChance);
        }

        super.writeEntityToNBT(compound);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        NBTTagCompound nbttagcompound = compound.getCompoundTag("Item");

        if (nbttagcompound != null && !nbttagcompound.hasNoTags())
        {
            setDisplayedItemWithUpdate(new ItemStack(nbttagcompound), false);
            setRotation(compound.getByte("ItemRotation"), false);

            if (compound.hasKey("ItemDropChance", 99))
            {
                itemDropChance = compound.getFloat("ItemDropChance");
            }
        }

        super.readEntityFromNBT(compound);
    }

    public boolean processInitialInteract(EntityPlayer player, EnumHand stack)
    {
        ItemStack itemstack = player.getHeldItem(stack);

        if (!world.isRemote)
        {
            if (getDisplayedItem().isEmpty())
            {
                if (!itemstack.isEmpty())
                {
                    setDisplayedItem(itemstack);

                    if (!player.capabilities.isCreativeMode)
                    {
                        itemstack.func_190918_g(1);
                    }
                }
            }
            else
            {
                playSound(SoundEvents.ENTITY_ITEMFRAME_ROTATE_ITEM, 1.0F, 1.0F);
                setItemRotation(getRotation() + 1);
            }
        }

        return true;
    }

    public int getAnalogOutput()
    {
        return getDisplayedItem().isEmpty() ? 0 : getRotation() % 8 + 1;
    }
}
