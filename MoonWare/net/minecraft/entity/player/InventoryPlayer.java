package net.minecraft.entity.player;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.RecipeItemHelper;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.play.server.SPacketSetSlot;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ReportedException;
import net.minecraft.util.text.Component;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TranslatableComponent;
import net.minecraft.world.World;

public class InventoryPlayer implements IInventory
{
    public final NonNullList<ItemStack> mainInventory = NonNullList.func_191197_a(36, ItemStack.EMPTY);
    public final NonNullList<ItemStack> armorInventory = NonNullList.func_191197_a(4, ItemStack.EMPTY);
    public final NonNullList<ItemStack> offHandInventory = NonNullList.func_191197_a(1, ItemStack.EMPTY);
    private final List<NonNullList<ItemStack>> allInventories;

    /** The index of the currently held item (0-8). */
    public int currentItem;

    /** The player whose inventory this is. */
    public EntityPlayer player;
    private ItemStack itemStack;
    private int field_194017_h;

    public InventoryPlayer(EntityPlayer playerIn)
    {
        allInventories = Arrays.asList(mainInventory, armorInventory, offHandInventory);
        itemStack = ItemStack.EMPTY;
        player = playerIn;
    }

    /**
     * Returns the item stack currently held by the player.
     */
    public ItemStack getCurrentItem()
    {
        return isHotbar(currentItem) ? mainInventory.get(currentItem) : ItemStack.EMPTY;
    }

    /**
     * Get the size of the player hotbar inventory
     */
    public static int getHotbarSize()
    {
        return 9;
    }

    private boolean canMergeStacks(ItemStack stack1, ItemStack stack2)
    {
        return !stack1.isEmpty() && stackEqualExact(stack1, stack2) && stack1.isStackable() && stack1.getCount() < stack1.getMaxStackSize() && stack1.getCount() < getInventoryStackLimit();
    }

    /**
     * Checks item, NBT, and meta if the item is not damageable
     */
    private boolean stackEqualExact(ItemStack stack1, ItemStack stack2)
    {
        return stack1.getItem() == stack2.getItem() && (!stack1.getHasSubtypes() || stack1.getMetadata() == stack2.getMetadata()) && ItemStack.areItemStackTagsEqual(stack1, stack2);
    }

    /**
     * Returns the first item stack that is empty.
     */
    public int getFirstEmptyStack()
    {
        for (int i = 0; i < mainInventory.size(); ++i)
        {
            if (mainInventory.get(i).isEmpty())
            {
                return i;
            }
        }

        return -1;
    }

    public void setPickedItemStack(ItemStack stack)
    {
        int i = getSlotFor(stack);

        if (isHotbar(i))
        {
            currentItem = i;
        }
        else
        {
            if (i == -1)
            {
                currentItem = getBestHotbarSlot();

                if (!mainInventory.get(currentItem).isEmpty())
                {
                    int j = getFirstEmptyStack();

                    if (j != -1)
                    {
                        mainInventory.set(j, mainInventory.get(currentItem));
                    }
                }

                mainInventory.set(currentItem, stack);
            }
            else
            {
                pickItem(i);
            }
        }
    }

    public void pickItem(int index)
    {
        currentItem = getBestHotbarSlot();
        ItemStack itemstack = mainInventory.get(currentItem);
        mainInventory.set(currentItem, mainInventory.get(index));
        mainInventory.set(index, itemstack);
    }

    public static boolean isHotbar(int index)
    {
        return index >= 0 && index < 9;
    }

    /**
     * Finds the stack or an equivalent one in the main inventory
     */
    public int getSlotFor(ItemStack stack)
    {
        for (int i = 0; i < mainInventory.size(); ++i)
        {
            if (!mainInventory.get(i).isEmpty() && stackEqualExact(stack, mainInventory.get(i)))
            {
                return i;
            }
        }

        return -1;
    }

    public int func_194014_c(ItemStack p_194014_1_)
    {
        for (int i = 0; i < mainInventory.size(); ++i)
        {
            ItemStack itemstack = mainInventory.get(i);

            if (!mainInventory.get(i).isEmpty() && stackEqualExact(p_194014_1_, mainInventory.get(i)) && !mainInventory.get(i).isItemDamaged() && !itemstack.isItemEnchanted() && !itemstack.hasDisplayName())
            {
                return i;
            }
        }

        return -1;
    }

    public int getBestHotbarSlot()
    {
        for (int i = 0; i < 9; ++i)
        {
            int j = (currentItem + i) % 9;

            if (mainInventory.get(j).isEmpty())
            {
                return j;
            }
        }

        for (int k = 0; k < 9; ++k)
        {
            int l = (currentItem + k) % 9;

            if (!mainInventory.get(l).isItemEnchanted())
            {
                return l;
            }
        }

        return currentItem;
    }

    /**
     * Switch the current item to the next one or the previous one
     */
    public void changeCurrentItem(int direction)
    {
        if (direction > 0)
        {
            direction = 1;
        }

        if (direction < 0)
        {
            direction = -1;
        }

        for (currentItem -= direction; currentItem < 0; currentItem += 9)
        {
        }

        while (currentItem >= 9)
        {
            currentItem -= 9;
        }
    }

    /**
     * Removes matching items from the inventory.
     * @param itemIn The item to match, null ignores.
     * @param metadataIn The metadata to match, -1 ignores.
     * @param removeCount The number of items to remove. If less than 1, removes all matching items.
     * @param itemNBT The NBT data to match, null ignores.
     * @return The number of items removed from the inventory.
     */
    public int clearMatchingItems(@Nullable Item itemIn, int metadataIn, int removeCount, @Nullable NBTTagCompound itemNBT)
    {
        int i = 0;

        for (int j = 0; j < getSizeInventory(); ++j)
        {
            ItemStack itemstack = getStackInSlot(j);

            if (!itemstack.isEmpty() && (itemIn == null || itemstack.getItem() == itemIn) && (metadataIn <= -1 || itemstack.getMetadata() == metadataIn) && (itemNBT == null || NBTUtil.areNBTEquals(itemNBT, itemstack.getTagCompound(), true)))
            {
                int k = removeCount <= 0 ? itemstack.getCount() : Math.min(removeCount - i, itemstack.getCount());
                i += k;

                if (removeCount != 0)
                {
                    itemstack.func_190918_g(k);

                    if (itemstack.isEmpty())
                    {
                        setInventorySlotContents(j, ItemStack.EMPTY);
                    }

                    if (removeCount > 0 && i >= removeCount)
                    {
                        return i;
                    }
                }
            }
        }

        if (!itemStack.isEmpty())
        {
            if (itemIn != null && itemStack.getItem() != itemIn)
            {
                return i;
            }

            if (metadataIn > -1 && itemStack.getMetadata() != metadataIn)
            {
                return i;
            }

            if (itemNBT != null && !NBTUtil.areNBTEquals(itemNBT, itemStack.getTagCompound(), true))
            {
                return i;
            }

            int l = removeCount <= 0 ? itemStack.getCount() : Math.min(removeCount - i, itemStack.getCount());
            i += l;

            if (removeCount != 0)
            {
                itemStack.func_190918_g(l);

                if (itemStack.isEmpty())
                {
                    itemStack = ItemStack.EMPTY;
                }

                if (removeCount > 0 && i >= removeCount)
                {
                    return i;
                }
            }
        }

        return i;
    }

    /**
     * This function stores as many items of an ItemStack as possible in a matching slot and returns the quantity of
     * left over items.
     */
    private int storePartialItemStack(ItemStack itemStackIn)
    {
        int i = storeItemStack(itemStackIn);

        if (i == -1)
        {
            i = getFirstEmptyStack();
        }

        return i == -1 ? itemStackIn.getCount() : func_191973_d(i, itemStackIn);
    }

    private int func_191973_d(int p_191973_1_, ItemStack p_191973_2_)
    {
        Item item = p_191973_2_.getItem();
        int i = p_191973_2_.getCount();
        ItemStack itemstack = getStackInSlot(p_191973_1_);

        if (itemstack.isEmpty())
        {
            itemstack = new ItemStack(item, 0, p_191973_2_.getMetadata());

            if (p_191973_2_.hasTagCompound())
            {
                itemstack.setTagCompound(p_191973_2_.getTagCompound().copy());
            }

            setInventorySlotContents(p_191973_1_, itemstack);
        }

        int j = i;

        if (i > itemstack.getMaxStackSize() - itemstack.getCount())
        {
            j = itemstack.getMaxStackSize() - itemstack.getCount();
        }

        if (j > getInventoryStackLimit() - itemstack.getCount())
        {
            j = getInventoryStackLimit() - itemstack.getCount();
        }

        if (j == 0)
        {
            return i;
        }
        else
        {
            i = i - j;
            itemstack.func_190917_f(j);
            itemstack.func_190915_d(5);
            return i;
        }
    }

    /**
     * stores an itemstack in the users inventory
     */
    public int storeItemStack(ItemStack itemStackIn)
    {
        if (canMergeStacks(getStackInSlot(currentItem), itemStackIn))
        {
            return currentItem;
        }
        else if (canMergeStacks(getStackInSlot(40), itemStackIn))
        {
            return 40;
        }
        else
        {
            for (int i = 0; i < mainInventory.size(); ++i)
            {
                if (canMergeStacks(mainInventory.get(i), itemStackIn))
                {
                    return i;
                }
            }

            return -1;
        }
    }

    /**
     * Decrement the number of animations remaining. Only called on client side. This is used to handle the animation of
     * receiving a block.
     */
    public void decrementAnimations()
    {
        for (NonNullList<ItemStack> nonnulllist : allInventories)
        {
            for (int i = 0; i < nonnulllist.size(); ++i)
            {
                if (!nonnulllist.get(i).isEmpty())
                {
                    nonnulllist.get(i).updateAnimation(player.world, player, i, currentItem == i);
                }
            }
        }
    }

    /**
     * Adds the item stack to the inventory, returns false if it is impossible.
     */
    public boolean addItemStackToInventory(ItemStack itemStackIn)
    {
        return func_191971_c(-1, itemStackIn);
    }

    public boolean func_191971_c(int p_191971_1_, ItemStack p_191971_2_)
    {
        if (p_191971_2_.isEmpty())
        {
            return false;
        }
        else
        {
            try
            {
                if (p_191971_2_.isItemDamaged())
                {
                    if (p_191971_1_ == -1)
                    {
                        p_191971_1_ = getFirstEmptyStack();
                    }

                    if (p_191971_1_ >= 0)
                    {
                        mainInventory.set(p_191971_1_, p_191971_2_.copy());
                        mainInventory.get(p_191971_1_).func_190915_d(5);
                        p_191971_2_.func_190920_e(0);
                        return true;
                    }
                    else if (player.capabilities.isCreativeMode)
                    {
                        p_191971_2_.func_190920_e(0);
                        return true;
                    }
                    else
                    {
                        return false;
                    }
                }
                else
                {
                    int i;

                    while (true)
                    {
                        i = p_191971_2_.getCount();

                        if (p_191971_1_ == -1)
                        {
                            p_191971_2_.func_190920_e(storePartialItemStack(p_191971_2_));
                        }
                        else
                        {
                            p_191971_2_.func_190920_e(func_191973_d(p_191971_1_, p_191971_2_));
                        }

                        if (p_191971_2_.isEmpty() || p_191971_2_.getCount() >= i)
                        {
                            break;
                        }
                    }

                    if (p_191971_2_.getCount() == i && player.capabilities.isCreativeMode)
                    {
                        p_191971_2_.func_190920_e(0);
                        return true;
                    }
                    else
                    {
                        return p_191971_2_.getCount() < i;
                    }
                }
            }
            catch (Throwable throwable)
            {
                CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Adding item to inventory");
                CrashReportCategory crashreportcategory = crashreport.makeCategory("Item being added");
                crashreportcategory.addCrashSection("Item ID", Integer.valueOf(Item.getIdFromItem(p_191971_2_.getItem())));
                crashreportcategory.addCrashSection("Item data", Integer.valueOf(p_191971_2_.getMetadata()));
                crashreportcategory.setDetail("Item name", new ICrashReportDetail<String>()
                {
                    public String call() throws Exception
                    {
                        return p_191971_2_.getDisplayName();
                    }
                });
                throw new ReportedException(crashreport);
            }
        }
    }

    public void func_191975_a(World p_191975_1_, ItemStack p_191975_2_)
    {
        if (!p_191975_1_.isRemote)
        {
            while (!p_191975_2_.isEmpty())
            {
                int i = storeItemStack(p_191975_2_);

                if (i == -1)
                {
                    i = getFirstEmptyStack();
                }

                if (i == -1)
                {
                    player.dropItem(p_191975_2_, false);
                    break;
                }

                int j = p_191975_2_.getMaxStackSize() - getStackInSlot(i).getCount();

                if (func_191971_c(i, p_191975_2_.splitStack(j)))
                {
                    ((EntityPlayerMP) player).connection.sendPacket(new SPacketSetSlot(-2, i, getStackInSlot(i)));
                }
            }
        }
    }

    /**
     * Removes up to a specified number of items from an inventory slot and returns them in a new stack.
     */
    public ItemStack decrStackSize(int index, int count)
    {
        List<ItemStack> list = null;

        for (NonNullList<ItemStack> nonnulllist : allInventories)
        {
            if (index < nonnulllist.size())
            {
                list = nonnulllist;
                break;
            }

            index -= nonnulllist.size();
        }

        return list != null && !list.get(index).isEmpty() ? ItemStackHelper.getAndSplit(list, index, count) : ItemStack.EMPTY;
    }

    public void deleteStack(ItemStack stack)
    {
        for (NonNullList<ItemStack> nonnulllist : allInventories)
        {
            for (int i = 0; i < nonnulllist.size(); ++i)
            {
                if (nonnulllist.get(i) == stack)
                {
                    nonnulllist.set(i, ItemStack.EMPTY);
                    break;
                }
            }
        }
    }

    /**
     * Removes a stack from the given slot and returns it.
     */
    public ItemStack removeStackFromSlot(int index)
    {
        NonNullList<ItemStack> nonnulllist = null;

        for (NonNullList<ItemStack> nonnulllist1 : allInventories)
        {
            if (index < nonnulllist1.size())
            {
                nonnulllist = nonnulllist1;
                break;
            }

            index -= nonnulllist1.size();
        }

        if (nonnulllist != null && !nonnulllist.get(index).isEmpty())
        {
            ItemStack itemstack = nonnulllist.get(index);
            nonnulllist.set(index, ItemStack.EMPTY);
            return itemstack;
        }
        else
        {
            return ItemStack.EMPTY;
        }
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    public void setInventorySlotContents(int index, ItemStack stack)
    {
        NonNullList<ItemStack> nonnulllist = null;

        for (NonNullList<ItemStack> nonnulllist1 : allInventories)
        {
            if (index < nonnulllist1.size())
            {
                nonnulllist = nonnulllist1;
                break;
            }

            index -= nonnulllist1.size();
        }

        if (nonnulllist != null)
        {
            nonnulllist.set(index, stack);
        }
    }

    public float getStrVsBlock(IBlockState state)
    {
        float f = 1.0F;

        if (!mainInventory.get(currentItem).isEmpty())
        {
            f *= mainInventory.get(currentItem).getStrVsBlock(state);
        }

        return f;
    }

    /**
     * Writes the inventory out as a list of compound tags. This is where the slot indices are used (+100 for armor, +80
     * for crafting).
     */
    public NBTTagList writeToNBT(NBTTagList nbtTagListIn)
    {
        for (int i = 0; i < mainInventory.size(); ++i)
        {
            if (!mainInventory.get(i).isEmpty())
            {
                NBTTagCompound nbttagcompound = new NBTTagCompound();
                nbttagcompound.setByte("Slot", (byte)i);
                mainInventory.get(i).writeToNBT(nbttagcompound);
                nbtTagListIn.appendTag(nbttagcompound);
            }
        }

        for (int j = 0; j < armorInventory.size(); ++j)
        {
            if (!armorInventory.get(j).isEmpty())
            {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte)(j + 100));
                armorInventory.get(j).writeToNBT(nbttagcompound1);
                nbtTagListIn.appendTag(nbttagcompound1);
            }
        }

        for (int k = 0; k < offHandInventory.size(); ++k)
        {
            if (!offHandInventory.get(k).isEmpty())
            {
                NBTTagCompound nbttagcompound2 = new NBTTagCompound();
                nbttagcompound2.setByte("Slot", (byte)(k + 150));
                offHandInventory.get(k).writeToNBT(nbttagcompound2);
                nbtTagListIn.appendTag(nbttagcompound2);
            }
        }

        return nbtTagListIn;
    }

    /**
     * Reads from the given tag list and fills the slots in the inventory with the correct items.
     */
    public void readFromNBT(NBTTagList nbtTagListIn)
    {
        mainInventory.clear();
        armorInventory.clear();
        offHandInventory.clear();

        for (int i = 0; i < nbtTagListIn.tagCount(); ++i)
        {
            NBTTagCompound nbttagcompound = nbtTagListIn.getCompoundTagAt(i);
            int j = nbttagcompound.getByte("Slot") & 255;
            ItemStack itemstack = new ItemStack(nbttagcompound);

            if (!itemstack.isEmpty())
            {
                if (j >= 0 && j < mainInventory.size())
                {
                    mainInventory.set(j, itemstack);
                }
                else if (j >= 100 && j < armorInventory.size() + 100)
                {
                    armorInventory.set(j - 100, itemstack);
                }
                else if (j >= 150 && j < offHandInventory.size() + 150)
                {
                    offHandInventory.set(j - 150, itemstack);
                }
            }
        }
    }

    /**
     * Returns the number of slots in the inventory.
     */
    public int getSizeInventory()
    {
        return mainInventory.size() + armorInventory.size() + offHandInventory.size();
    }

    public boolean func_191420_l()
    {
        for (ItemStack itemstack : mainInventory)
        {
            if (!itemstack.isEmpty())
            {
                return false;
            }
        }

        for (ItemStack itemstack1 : armorInventory)
        {
            if (!itemstack1.isEmpty())
            {
                return false;
            }
        }

        for (ItemStack itemstack2 : offHandInventory)
        {
            if (!itemstack2.isEmpty())
            {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns the stack in the given slot.
     */
    public ItemStack getStackInSlot(int index)
    {
        List<ItemStack> list = null;

        for (NonNullList<ItemStack> nonnulllist : allInventories)
        {
            if (index < nonnulllist.size())
            {
                list = nonnulllist;
                break;
            }

            index -= nonnulllist.size();
        }

        return list == null ? ItemStack.EMPTY : list.get(index);
    }

    /**
     * Get the name of this object. For players this returns their username
     */
    public String getName()
    {
        return "container.inventory";
    }

    /**
     * Returns true if this thing is named
     */
    public boolean hasCustomName()
    {
        return false;
    }

    /**
     * Get the formatted ChatComponent that will be used for the sender's username in chat
     */
    public Component getDisplayName()
    {
        return hasCustomName() ? new TextComponent(getName()) : new TranslatableComponent(getName());
    }

    /**
     * Returns the maximum stack size for a inventory slot. Seems to always be 64, possibly will be extended.
     */
    public int getInventoryStackLimit()
    {
        return 64;
    }

    public boolean canHarvestBlock(IBlockState state)
    {
        if (state.getMaterial().isToolNotRequired())
        {
            return true;
        }
        else
        {
            ItemStack itemstack = getStackInSlot(currentItem);
            return !itemstack.isEmpty() && itemstack.canHarvestBlock(state);
        }
    }

    /**
     * returns a player armor item (as itemstack) contained in specified armor slot.
     */
    public ItemStack armorItemInSlot(int slotIn)
    {
        return armorInventory.get(slotIn);
    }

    /**
     * Damages armor in each slot by the specified amount.
     */
    public void damageArmor(float damage)
    {
        damage = damage / 4.0F;

        if (damage < 1.0F)
        {
            damage = 1.0F;
        }

        for (int i = 0; i < armorInventory.size(); ++i)
        {
            ItemStack itemstack = armorInventory.get(i);

            if (itemstack.getItem() instanceof ItemArmor)
            {
                itemstack.damageItem((int)damage, player);
            }
        }
    }

    /**
     * Drop all armor and main inventory items.
     */
    public void dropAllItems()
    {
        for (List<ItemStack> list : allInventories)
        {
            for (int i = 0; i < list.size(); ++i)
            {
                ItemStack itemstack = list.get(i);

                if (!itemstack.isEmpty())
                {
                    player.dropItem(itemstack, true, false);
                    list.set(i, ItemStack.EMPTY);
                }
            }
        }
    }

    /**
     * For tile entities, ensures the chunk containing the tile entity is saved to disk later - the game won't think it
     * hasn't changed and skip it.
     */
    public void markDirty()
    {
        ++field_194017_h;
    }

    public int func_194015_p()
    {
        return field_194017_h;
    }

    /**
     * Set the stack helds by mouse, used in GUI/Container
     */
    public void setItemStack(ItemStack itemStackIn)
    {
        itemStack = itemStackIn;
    }

    /**
     * Stack helds by mouse, used in GUI and Containers
     */
    public ItemStack getItemStack()
    {
        return itemStack;
    }

    /**
     * Don't rename this method to canInteractWith due to conflicts with Container
     */
    public boolean isUsableByPlayer(EntityPlayer player)
    {
        if (this.player.isDead)
        {
            return false;
        }
        else
        {
            return player.getDistanceSqToEntity(this.player) <= 64.0D;
        }
    }

    /**
     * Returns true if the specified ItemStack exists in the inventory.
     */
    public boolean hasItemStack(ItemStack itemStackIn)
    {
        label23:

        for (List<ItemStack> list : allInventories)
        {
            Iterator iterator = list.iterator();

            while (true)
            {
                if (!iterator.hasNext())
                {
                    continue label23;
                }

                ItemStack itemstack = (ItemStack)iterator.next();

                if (!itemstack.isEmpty() && itemstack.isItemEqual(itemStackIn))
                {
                    break;
                }
            }

            return true;
        }

        return false;
    }

    public void openInventory(EntityPlayer player)
    {
    }

    public void closeInventory(EntityPlayer player)
    {
    }

    /**
     * Returns true if automation is allowed to insert the given stack (ignoring stack size) into the given slot. For
     * guis use Slot.isItemValid
     */
    public boolean isItemValidForSlot(int index, ItemStack stack)
    {
        return true;
    }

    /**
     * Copy the ItemStack contents from another InventoryPlayer instance
     */
    public void copyInventory(InventoryPlayer playerInventory)
    {
        for (int i = 0; i < getSizeInventory(); ++i)
        {
            setInventorySlotContents(i, playerInventory.getStackInSlot(i));
        }

        currentItem = playerInventory.currentItem;
    }

    public int getField(int id)
    {
        return 0;
    }

    public void setField(int id, int value)
    {
    }

    public int getFieldCount()
    {
        return 0;
    }

    public void clear()
    {
        for (List<ItemStack> list : allInventories)
        {
            list.clear();
        }
    }

    public void func_194016_a(RecipeItemHelper p_194016_1_, boolean p_194016_2_)
    {
        for (ItemStack itemstack : mainInventory)
        {
            p_194016_1_.func_194112_a(itemstack);
        }

        if (p_194016_2_)
        {
            p_194016_1_.func_194112_a(offHandInventory.get(0));
        }
    }
}
