package net.minecraft.inventory;

import java.util.List;
import java.util.Random;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ContainerEnchantment extends Container
{
    /** SlotEnchantmentTable object with ItemStack to be enchanted */
    public IInventory tableInventory;

    /** current world (for bookshelf counting) */
    private final World worldPointer;
    private final BlockPos position;
    private final Random rand;
    public int xpSeed;

    /** 3-member array storing the enchantment levels of each slot */
    public int[] enchantLevels;
    public int[] enchantClue;
    public int[] worldClue;

    public ContainerEnchantment(InventoryPlayer playerInv, World worldIn)
    {
        this(playerInv, worldIn, BlockPos.ORIGIN);
    }

    public ContainerEnchantment(InventoryPlayer playerInv, World worldIn, BlockPos pos)
    {
        tableInventory = new InventoryBasic("Enchant", true, 2)
        {
            public int getInventoryStackLimit()
            {
                return 64;
            }
            public void markDirty()
            {
                super.markDirty();
                onCraftMatrixChanged(this);
            }
        };
        rand = new Random();
        enchantLevels = new int[3];
        enchantClue = new int[] { -1, -1, -1};
        worldClue = new int[] { -1, -1, -1};
        worldPointer = worldIn;
        position = pos;
        xpSeed = playerInv.player.getXPSeed();
        addSlotToContainer(new Slot(tableInventory, 0, 15, 47)
        {
            public boolean isItemValid(ItemStack stack)
            {
                return true;
            }
            public int getSlotStackLimit()
            {
                return 1;
            }
        });
        addSlotToContainer(new Slot(tableInventory, 1, 35, 47)
        {
            public boolean isItemValid(ItemStack stack)
            {
                return stack.getItem() == Items.DYE && EnumDyeColor.byDyeDamage(stack.getMetadata()) == EnumDyeColor.BLUE;
            }
        });

        for (int i = 0; i < 3; ++i)
        {
            for (int j = 0; j < 9; ++j)
            {
                addSlotToContainer(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int k = 0; k < 9; ++k)
        {
            addSlotToContainer(new Slot(playerInv, k, 8 + k * 18, 142));
        }
    }

    protected void broadcastData(IContainerListener crafting)
    {
        crafting.sendProgressBarUpdate(this, 0, enchantLevels[0]);
        crafting.sendProgressBarUpdate(this, 1, enchantLevels[1]);
        crafting.sendProgressBarUpdate(this, 2, enchantLevels[2]);
        crafting.sendProgressBarUpdate(this, 3, xpSeed & -16);
        crafting.sendProgressBarUpdate(this, 4, enchantClue[0]);
        crafting.sendProgressBarUpdate(this, 5, enchantClue[1]);
        crafting.sendProgressBarUpdate(this, 6, enchantClue[2]);
        crafting.sendProgressBarUpdate(this, 7, worldClue[0]);
        crafting.sendProgressBarUpdate(this, 8, worldClue[1]);
        crafting.sendProgressBarUpdate(this, 9, worldClue[2]);
    }

    public void addListener(IContainerListener listener)
    {
        super.addListener(listener);
        broadcastData(listener);
    }

    /**
     * Looks for changes made in the container, sends them to every listener.
     */
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();

        for (int i = 0; i < listeners.size(); ++i)
        {
            IContainerListener icontainerlistener = listeners.get(i);
            broadcastData(icontainerlistener);
        }
    }

    public void updateProgressBar(int id, int data)
    {
        if (id >= 0 && id <= 2)
        {
            enchantLevels[id] = data;
        }
        else if (id == 3)
        {
            xpSeed = data;
        }
        else if (id >= 4 && id <= 6)
        {
            enchantClue[id - 4] = data;
        }
        else if (id >= 7 && id <= 9)
        {
            worldClue[id - 7] = data;
        }
        else
        {
            super.updateProgressBar(id, data);
        }
    }

    /**
     * Callback for when the crafting matrix is changed.
     */
    public void onCraftMatrixChanged(IInventory inventoryIn)
    {
        if (inventoryIn == tableInventory)
        {
            ItemStack itemstack = inventoryIn.getStackInSlot(0);

            if (!itemstack.isEmpty() && itemstack.isItemEnchantable())
            {
                if (!worldPointer.isRemote)
                {
                    int l = 0;

                    for (int j = -1; j <= 1; ++j)
                    {
                        for (int k = -1; k <= 1; ++k)
                        {
                            if ((j != 0 || k != 0) && worldPointer.isAirBlock(position.add(k, 0, j)) && worldPointer.isAirBlock(position.add(k, 1, j)))
                            {
                                if (worldPointer.getBlockState(position.add(k * 2, 0, j * 2)).getBlock() == Blocks.BOOKSHELF)
                                {
                                    ++l;
                                }

                                if (worldPointer.getBlockState(position.add(k * 2, 1, j * 2)).getBlock() == Blocks.BOOKSHELF)
                                {
                                    ++l;
                                }

                                if (k != 0 && j != 0)
                                {
                                    if (worldPointer.getBlockState(position.add(k * 2, 0, j)).getBlock() == Blocks.BOOKSHELF)
                                    {
                                        ++l;
                                    }

                                    if (worldPointer.getBlockState(position.add(k * 2, 1, j)).getBlock() == Blocks.BOOKSHELF)
                                    {
                                        ++l;
                                    }

                                    if (worldPointer.getBlockState(position.add(k, 0, j * 2)).getBlock() == Blocks.BOOKSHELF)
                                    {
                                        ++l;
                                    }

                                    if (worldPointer.getBlockState(position.add(k, 1, j * 2)).getBlock() == Blocks.BOOKSHELF)
                                    {
                                        ++l;
                                    }
                                }
                            }
                        }
                    }

                    rand.setSeed(xpSeed);

                    for (int i1 = 0; i1 < 3; ++i1)
                    {
                        enchantLevels[i1] = EnchantmentHelper.calcItemStackEnchantability(rand, i1, l, itemstack);
                        enchantClue[i1] = -1;
                        worldClue[i1] = -1;

                        if (enchantLevels[i1] < i1 + 1)
                        {
                            enchantLevels[i1] = 0;
                        }
                    }

                    for (int j1 = 0; j1 < 3; ++j1)
                    {
                        if (enchantLevels[j1] > 0)
                        {
                            List<EnchantmentData> list = getEnchantmentList(itemstack, j1, enchantLevels[j1]);

                            if (list != null && !list.isEmpty())
                            {
                                EnchantmentData enchantmentdata = list.get(rand.nextInt(list.size()));
                                enchantClue[j1] = Enchantment.getEnchantmentID(enchantmentdata.enchantmentobj);
                                worldClue[j1] = enchantmentdata.enchantmentLevel;
                            }
                        }
                    }

                    detectAndSendChanges();
                }
            }
            else
            {
                for (int i = 0; i < 3; ++i)
                {
                    enchantLevels[i] = 0;
                    enchantClue[i] = -1;
                    worldClue[i] = -1;
                }
            }
        }
    }

    /**
     * Handles the given Button-click on the server, currently only used by enchanting. Name is for legacy.
     */
    public boolean enchantItem(EntityPlayer playerIn, int id)
    {
        ItemStack itemstack = tableInventory.getStackInSlot(0);
        ItemStack itemstack1 = tableInventory.getStackInSlot(1);
        int i = id + 1;

        if ((itemstack1.isEmpty() || itemstack1.getCount() < i) && !playerIn.capabilities.isCreativeMode)
        {
            return false;
        }
        else if (enchantLevels[id] > 0 && !itemstack.isEmpty() && (playerIn.experienceLevel >= i && playerIn.experienceLevel >= enchantLevels[id] || playerIn.capabilities.isCreativeMode))
        {
            if (!worldPointer.isRemote)
            {
                List<EnchantmentData> list = getEnchantmentList(itemstack, id, enchantLevels[id]);

                if (!list.isEmpty())
                {
                    playerIn.func_192024_a(itemstack, i);
                    boolean flag = itemstack.getItem() == Items.BOOK;

                    if (flag)
                    {
                        itemstack = new ItemStack(Items.ENCHANTED_BOOK);
                        tableInventory.setInventorySlotContents(0, itemstack);
                    }

                    for (int j = 0; j < list.size(); ++j)
                    {
                        EnchantmentData enchantmentdata = list.get(j);

                        if (flag)
                        {
                            ItemEnchantedBook.addEnchantment(itemstack, enchantmentdata);
                        }
                        else
                        {
                            itemstack.addEnchantment(enchantmentdata.enchantmentobj, enchantmentdata.enchantmentLevel);
                        }
                    }

                    if (!playerIn.capabilities.isCreativeMode)
                    {
                        itemstack1.func_190918_g(i);

                        if (itemstack1.isEmpty())
                        {
                            tableInventory.setInventorySlotContents(1, ItemStack.EMPTY);
                        }
                    }

                    playerIn.addStat(StatList.ITEM_ENCHANTED);

                    if (playerIn instanceof EntityPlayerMP)
                    {
                        CriteriaTriggers.field_192129_i.func_192190_a((EntityPlayerMP)playerIn, itemstack, i);
                    }

                    tableInventory.markDirty();
                    xpSeed = playerIn.getXPSeed();
                    onCraftMatrixChanged(tableInventory);
                    worldPointer.playSound(null, position, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.BLOCKS, 1.0F, worldPointer.rand.nextFloat() * 0.1F + 0.9F);
                }
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    private List<EnchantmentData> getEnchantmentList(ItemStack stack, int p_178148_2_, int p_178148_3_)
    {
        rand.setSeed(xpSeed + p_178148_2_);
        List<EnchantmentData> list = EnchantmentHelper.buildEnchantmentList(rand, stack, p_178148_3_, false);

        if (stack.getItem() == Items.BOOK && list.size() > 1)
        {
            list.remove(rand.nextInt(list.size()));
        }

        return list;
    }

    public int getLapisAmount()
    {
        ItemStack itemstack = tableInventory.getStackInSlot(1);
        return itemstack.isEmpty() ? 0 : itemstack.getCount();
    }

    /**
     * Called when the container is closed.
     */
    public void onContainerClosed(EntityPlayer playerIn)
    {
        super.onContainerClosed(playerIn);

        if (!worldPointer.isRemote)
        {
            func_193327_a(playerIn, playerIn.world, tableInventory);
        }
    }

    /**
     * Determines whether supplied player can use this container
     */
    public boolean canInteractWith(EntityPlayer playerIn)
    {
        if (worldPointer.getBlockState(position).getBlock() != Blocks.ENCHANTING_TABLE)
        {
            return false;
        }
        else
        {
            return playerIn.getDistanceSq((double) position.getX() + 0.5D, (double) position.getY() + 0.5D, (double) position.getZ() + 0.5D) <= 64.0D;
        }
    }

    /**
     * Take a stack from the specified inventory slot.
     */
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
    {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = inventorySlots.get(index);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index == 0)
            {
                if (!mergeItemStack(itemstack1, 2, 38, true))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (index == 1)
            {
                if (!mergeItemStack(itemstack1, 2, 38, true))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (itemstack1.getItem() == Items.DYE && EnumDyeColor.byDyeDamage(itemstack1.getMetadata()) == EnumDyeColor.BLUE)
            {
                if (!mergeItemStack(itemstack1, 1, 2, true))
                {
                    return ItemStack.EMPTY;
                }
            }
            else
            {
                if (inventorySlots.get(0).getHasStack() || !inventorySlots.get(0).isItemValid(itemstack1))
                {
                    return ItemStack.EMPTY;
                }

                if (itemstack1.hasTagCompound() && itemstack1.getCount() == 1)
                {
                    inventorySlots.get(0).putStack(itemstack1.copy());
                    itemstack1.func_190920_e(0);
                }
                else if (!itemstack1.isEmpty())
                {
                    inventorySlots.get(0).putStack(new ItemStack(itemstack1.getItem(), 1, itemstack1.getMetadata()));
                    itemstack1.func_190918_g(1);
                }
            }

            if (itemstack1.isEmpty())
            {
                slot.putStack(ItemStack.EMPTY);
            }
            else
            {
                slot.onSlotChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount())
            {
                return ItemStack.EMPTY;
            }

            slot.func_190901_a(playerIn, itemstack1);
        }

        return itemstack;
    }
}
