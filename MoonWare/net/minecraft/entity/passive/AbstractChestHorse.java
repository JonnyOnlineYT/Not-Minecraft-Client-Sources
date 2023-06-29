package net.minecraft.entity.passive;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;
import net.minecraft.world.World;

public abstract class AbstractChestHorse extends AbstractHorse
{
    private static final DataParameter<Boolean> field_190698_bG = EntityDataManager.createKey(AbstractChestHorse.class, DataSerializers.BOOLEAN);

    public AbstractChestHorse(World p_i47300_1_)
    {
        super(p_i47300_1_);
        field_190688_bE = false;
    }

    protected void entityInit()
    {
        super.entityInit();
        dataManager.register(field_190698_bG, Boolean.valueOf(false));
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(getModifiedMaxHealth());
        getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.17499999701976776D);
        getEntityAttribute(AbstractHorse.JUMP_STRENGTH).setBaseValue(0.5D);
    }

    public boolean func_190695_dh()
    {
        return dataManager.get(field_190698_bG).booleanValue();
    }

    public void setChested(boolean chested)
    {
        dataManager.set(field_190698_bG, Boolean.valueOf(chested));
    }

    protected int func_190686_di()
    {
        return func_190695_dh() ? 17 : super.func_190686_di();
    }

    /**
     * Returns the Y offset from the entity's position for any entity riding this one.
     */
    public double getMountedYOffset()
    {
        return super.getMountedYOffset() - 0.25D;
    }

    protected SoundEvent getAngrySound()
    {
        super.getAngrySound();
        return SoundEvents.ENTITY_DONKEY_ANGRY;
    }

    /**
     * Called when the mob's health reaches 0.
     */
    public void onDeath(DamageSource cause)
    {
        super.onDeath(cause);

        if (func_190695_dh())
        {
            if (!world.isRemote)
            {
                dropItem(Item.getItemFromBlock(Blocks.CHEST), 1);
            }

            setChested(false);
        }
    }

    public static void func_190694_b(DataFixer p_190694_0_, Class<?> p_190694_1_)
    {
        AbstractHorse.func_190683_c(p_190694_0_, p_190694_1_);
        p_190694_0_.registerWalker(FixTypes.ENTITY, new ItemStackDataLists(p_190694_1_, "Items"));
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        compound.setBoolean("ChestedHorse", func_190695_dh());

        if (func_190695_dh())
        {
            NBTTagList nbttaglist = new NBTTagList();

            for (int i = 2; i < horseChest.getSizeInventory(); ++i)
            {
                ItemStack itemstack = horseChest.getStackInSlot(i);

                if (!itemstack.isEmpty())
                {
                    NBTTagCompound nbttagcompound = new NBTTagCompound();
                    nbttagcompound.setByte("Slot", (byte)i);
                    itemstack.writeToNBT(nbttagcompound);
                    nbttaglist.appendTag(nbttagcompound);
                }
            }

            compound.setTag("Items", nbttaglist);
        }
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
        setChested(compound.getBoolean("ChestedHorse"));

        if (func_190695_dh())
        {
            NBTTagList nbttaglist = compound.getTagList("Items", 10);
            initHorseChest();

            for (int i = 0; i < nbttaglist.tagCount(); ++i)
            {
                NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
                int j = nbttagcompound.getByte("Slot") & 255;

                if (j >= 2 && j < horseChest.getSizeInventory())
                {
                    horseChest.setInventorySlotContents(j, new ItemStack(nbttagcompound));
                }
            }
        }

        updateHorseSlots();
    }

    public boolean replaceItemInInventory(int inventorySlot, ItemStack itemStackIn)
    {
        if (inventorySlot == 499)
        {
            if (func_190695_dh() && itemStackIn.isEmpty())
            {
                setChested(false);
                initHorseChest();
                return true;
            }

            if (!func_190695_dh() && itemStackIn.getItem() == Item.getItemFromBlock(Blocks.CHEST))
            {
                setChested(true);
                initHorseChest();
                return true;
            }
        }

        return super.replaceItemInInventory(inventorySlot, itemStackIn);
    }

    public boolean processInteract(EntityPlayer player, EnumHand hand)
    {
        ItemStack itemstack = player.getHeldItem(hand);

        if (itemstack.getItem() == Items.SPAWN_EGG)
        {
            return super.processInteract(player, hand);
        }
        else
        {
            if (!isChild())
            {
                if (isTame() && player.isSneaking())
                {
                    openGUI(player);
                    return true;
                }

                if (isBeingRidden())
                {
                    return super.processInteract(player, hand);
                }
            }

            if (!itemstack.isEmpty())
            {
                boolean flag = func_190678_b(player, itemstack);

                if (!flag && !isTame())
                {
                    if (itemstack.interactWithEntity(player, this, hand))
                    {
                        return true;
                    }

                    func_190687_dF();
                    return true;
                }

                if (!flag && !func_190695_dh() && itemstack.getItem() == Item.getItemFromBlock(Blocks.CHEST))
                {
                    setChested(true);
                    func_190697_dk();
                    flag = true;
                    initHorseChest();
                }

                if (!flag && !isChild() && !isHorseSaddled() && itemstack.getItem() == Items.SADDLE)
                {
                    openGUI(player);
                    return true;
                }

                if (flag)
                {
                    if (!player.capabilities.isCreativeMode)
                    {
                        itemstack.func_190918_g(1);
                    }

                    return true;
                }
            }

            if (isChild())
            {
                return super.processInteract(player, hand);
            }
            else if (itemstack.interactWithEntity(player, this, hand))
            {
                return true;
            }
            else
            {
                mountTo(player);
                return true;
            }
        }
    }

    protected void func_190697_dk()
    {
        playSound(SoundEvents.ENTITY_DONKEY_CHEST, 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
    }

    public int func_190696_dl()
    {
        return 5;
    }
}
