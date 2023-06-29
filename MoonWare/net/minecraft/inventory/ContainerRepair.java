package net.minecraft.inventory;

import java.util.Map;
import net.minecraft.block.BlockAnvil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ContainerRepair extends Container
{
    private static final Logger LOGGER = LogManager.getLogger();

    /** Here comes out item you merged and/or renamed. */
    private final IInventory outputSlot;

    /**
     * The 2slots where you put your items in that you want to merge and/or rename.
     */
    private final IInventory inputSlots;
    private final World theWorld;
    private final BlockPos selfPosition;

    /** The maximum cost of repairing/renaming in the anvil. */
    public int maximumCost;

    /** determined by damage of input item and stackSize of repair materials */
    private int materialCost;
    private String repairedItemName;

    /** The player that has this container open. */
    private final EntityPlayer thePlayer;

    public ContainerRepair(InventoryPlayer playerInventory, World worldIn, EntityPlayer player)
    {
        this(playerInventory, worldIn, BlockPos.ORIGIN, player);
    }

    public ContainerRepair(InventoryPlayer playerInventory, World worldIn, BlockPos blockPosIn, EntityPlayer player)
    {
        outputSlot = new InventoryCraftResult();
        inputSlots = new InventoryBasic("Repair", true, 2)
        {
            public void markDirty()
            {
                super.markDirty();
                onCraftMatrixChanged(this);
            }
        };
        selfPosition = blockPosIn;
        theWorld = worldIn;
        thePlayer = player;
        addSlotToContainer(new Slot(inputSlots, 0, 27, 47));
        addSlotToContainer(new Slot(inputSlots, 1, 76, 47));
        addSlotToContainer(new Slot(outputSlot, 2, 134, 47)
        {
            public boolean isItemValid(ItemStack stack)
            {
                return false;
            }
            public boolean canTakeStack(EntityPlayer playerIn)
            {
                return (playerIn.capabilities.isCreativeMode || playerIn.experienceLevel >= maximumCost) && maximumCost > 0 && getHasStack();
            }
            public ItemStack func_190901_a(EntityPlayer p_190901_1_, ItemStack p_190901_2_)
            {
                if (!p_190901_1_.capabilities.isCreativeMode)
                {
                    p_190901_1_.addExperienceLevel(-maximumCost);
                }

                inputSlots.setInventorySlotContents(0, ItemStack.EMPTY);

                if (materialCost > 0)
                {
                    ItemStack itemstack = inputSlots.getStackInSlot(1);

                    if (!itemstack.isEmpty() && itemstack.getCount() > materialCost)
                    {
                        itemstack.func_190918_g(materialCost);
                        inputSlots.setInventorySlotContents(1, itemstack);
                    }
                    else
                    {
                        inputSlots.setInventorySlotContents(1, ItemStack.EMPTY);
                    }
                }
                else
                {
                    inputSlots.setInventorySlotContents(1, ItemStack.EMPTY);
                }

                maximumCost = 0;
                IBlockState iblockstate = worldIn.getBlockState(blockPosIn);

                if (!p_190901_1_.capabilities.isCreativeMode && !worldIn.isRemote && iblockstate.getBlock() == Blocks.ANVIL && p_190901_1_.getRNG().nextFloat() < 0.12F)
                {
                    int l = iblockstate.getValue(BlockAnvil.DAMAGE).intValue();
                    ++l;

                    if (l > 2)
                    {
                        worldIn.setBlockToAir(blockPosIn);
                        worldIn.playEvent(1029, blockPosIn, 0);
                    }
                    else
                    {
                        worldIn.setBlockState(blockPosIn, iblockstate.withProperty(BlockAnvil.DAMAGE, Integer.valueOf(l)), 2);
                        worldIn.playEvent(1030, blockPosIn, 0);
                    }
                }
                else if (!worldIn.isRemote)
                {
                    worldIn.playEvent(1030, blockPosIn, 0);
                }

                return p_190901_2_;
            }
        });

        for (int i = 0; i < 3; ++i)
        {
            for (int j = 0; j < 9; ++j)
            {
                addSlotToContainer(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int k = 0; k < 9; ++k)
        {
            addSlotToContainer(new Slot(playerInventory, k, 8 + k * 18, 142));
        }
    }

    /**
     * Callback for when the crafting matrix is changed.
     */
    public void onCraftMatrixChanged(IInventory inventoryIn)
    {
        super.onCraftMatrixChanged(inventoryIn);

        if (inventoryIn == inputSlots)
        {
            updateRepairOutput();
        }
    }

    /**
     * called when the Anvil Input Slot changes, calculates the new result and puts it in the output slot
     */
    public void updateRepairOutput()
    {
        ItemStack itemstack = inputSlots.getStackInSlot(0);
        maximumCost = 1;
        int i = 0;
        int j = 0;
        int k = 0;

        if (itemstack.isEmpty())
        {
            outputSlot.setInventorySlotContents(0, ItemStack.EMPTY);
            maximumCost = 0;
        }
        else
        {
            ItemStack itemstack1 = itemstack.copy();
            ItemStack itemstack2 = inputSlots.getStackInSlot(1);
            Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(itemstack1);
            j = j + itemstack.getRepairCost() + (itemstack2.isEmpty() ? 0 : itemstack2.getRepairCost());
            materialCost = 0;

            if (!itemstack2.isEmpty())
            {
                boolean flag = itemstack2.getItem() == Items.ENCHANTED_BOOK && !ItemEnchantedBook.getEnchantments(itemstack2).hasNoTags();

                if (itemstack1.isItemStackDamageable() && itemstack1.getItem().getIsRepairable(itemstack, itemstack2))
                {
                    int l2 = Math.min(itemstack1.getItemDamage(), itemstack1.getMaxDamage() / 4);

                    if (l2 <= 0)
                    {
                        outputSlot.setInventorySlotContents(0, ItemStack.EMPTY);
                        maximumCost = 0;
                        return;
                    }

                    int i3;

                    for (i3 = 0; l2 > 0 && i3 < itemstack2.getCount(); ++i3)
                    {
                        int j3 = itemstack1.getItemDamage() - l2;
                        itemstack1.setItemDamage(j3);
                        ++i;
                        l2 = Math.min(itemstack1.getItemDamage(), itemstack1.getMaxDamage() / 4);
                    }

                    materialCost = i3;
                }
                else
                {
                    if (!flag && (itemstack1.getItem() != itemstack2.getItem() || !itemstack1.isItemStackDamageable()))
                    {
                        outputSlot.setInventorySlotContents(0, ItemStack.EMPTY);
                        maximumCost = 0;
                        return;
                    }

                    if (itemstack1.isItemStackDamageable() && !flag)
                    {
                        int l = itemstack.getMaxDamage() - itemstack.getItemDamage();
                        int i1 = itemstack2.getMaxDamage() - itemstack2.getItemDamage();
                        int j1 = i1 + itemstack1.getMaxDamage() * 12 / 100;
                        int k1 = l + j1;
                        int l1 = itemstack1.getMaxDamage() - k1;

                        if (l1 < 0)
                        {
                            l1 = 0;
                        }

                        if (l1 < itemstack1.getMetadata())
                        {
                            itemstack1.setItemDamage(l1);
                            i += 2;
                        }
                    }

                    Map<Enchantment, Integer> map1 = EnchantmentHelper.getEnchantments(itemstack2);
                    boolean flag2 = false;
                    boolean flag3 = false;

                    for (Enchantment enchantment1 : map1.keySet())
                    {
                        if (enchantment1 != null)
                        {
                            int i2 = map.containsKey(enchantment1) ? map.get(enchantment1).intValue() : 0;
                            int j2 = map1.get(enchantment1).intValue();
                            j2 = i2 == j2 ? j2 + 1 : Math.max(j2, i2);
                            boolean flag1 = enchantment1.canApply(itemstack);

                            if (thePlayer.capabilities.isCreativeMode || itemstack.getItem() == Items.ENCHANTED_BOOK)
                            {
                                flag1 = true;
                            }

                            for (Enchantment enchantment : map.keySet())
                            {
                                if (enchantment != enchantment1 && !enchantment1.func_191560_c(enchantment))
                                {
                                    flag1 = false;
                                    ++i;
                                }
                            }

                            if (!flag1)
                            {
                                flag3 = true;
                            }
                            else
                            {
                                flag2 = true;

                                if (j2 > enchantment1.getMaxLevel())
                                {
                                    j2 = enchantment1.getMaxLevel();
                                }

                                map.put(enchantment1, Integer.valueOf(j2));
                                int k3 = 0;

                                switch (enchantment1.getRarity())
                                {
                                    case COMMON:
                                        k3 = 1;
                                        break;

                                    case UNCOMMON:
                                        k3 = 2;
                                        break;

                                    case RARE:
                                        k3 = 4;
                                        break;

                                    case VERY_RARE:
                                        k3 = 8;
                                }

                                if (flag)
                                {
                                    k3 = Math.max(1, k3 / 2);
                                }

                                i += k3 * j2;

                                if (itemstack.getCount() > 1)
                                {
                                    i = 40;
                                }
                            }
                        }
                    }

                    if (flag3 && !flag2)
                    {
                        outputSlot.setInventorySlotContents(0, ItemStack.EMPTY);
                        maximumCost = 0;
                        return;
                    }
                }
            }

            if (StringUtils.isBlank(repairedItemName))
            {
                if (itemstack.hasDisplayName())
                {
                    k = 1;
                    i += k;
                    itemstack1.clearCustomName();
                }
            }
            else if (!repairedItemName.equals(itemstack.getDisplayName()))
            {
                k = 1;
                i += k;
                itemstack1.setStackDisplayName(repairedItemName);
            }

            maximumCost = j + i;

            if (i <= 0)
            {
                itemstack1 = ItemStack.EMPTY;
            }

            if (k == i && k > 0 && maximumCost >= 40)
            {
                maximumCost = 39;
            }

            if (maximumCost >= 40 && !thePlayer.capabilities.isCreativeMode)
            {
                itemstack1 = ItemStack.EMPTY;
            }

            if (!itemstack1.isEmpty())
            {
                int k2 = itemstack1.getRepairCost();

                if (!itemstack2.isEmpty() && k2 < itemstack2.getRepairCost())
                {
                    k2 = itemstack2.getRepairCost();
                }

                if (k != i || k == 0)
                {
                    k2 = k2 * 2 + 1;
                }

                itemstack1.setRepairCost(k2);
                EnchantmentHelper.setEnchantments(map, itemstack1);
            }

            outputSlot.setInventorySlotContents(0, itemstack1);
            detectAndSendChanges();
        }
    }

    public void addListener(IContainerListener listener)
    {
        super.addListener(listener);
        listener.sendProgressBarUpdate(this, 0, maximumCost);
    }

    public void updateProgressBar(int id, int data)
    {
        if (id == 0)
        {
            maximumCost = data;
        }
    }

    /**
     * Called when the container is closed.
     */
    public void onContainerClosed(EntityPlayer playerIn)
    {
        super.onContainerClosed(playerIn);

        if (!theWorld.isRemote)
        {
            func_193327_a(playerIn, theWorld, inputSlots);
        }
    }

    /**
     * Determines whether supplied player can use this container
     */
    public boolean canInteractWith(EntityPlayer playerIn)
    {
        if (theWorld.getBlockState(selfPosition).getBlock() != Blocks.ANVIL)
        {
            return false;
        }
        else
        {
            return playerIn.getDistanceSq((double) selfPosition.getX() + 0.5D, (double) selfPosition.getY() + 0.5D, (double) selfPosition.getZ() + 0.5D) <= 64.0D;
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

            if (index == 2)
            {
                if (!mergeItemStack(itemstack1, 3, 39, true))
                {
                    return ItemStack.EMPTY;
                }

                slot.onSlotChange(itemstack1, itemstack);
            }
            else if (index != 0 && index != 1)
            {
                if (index >= 3 && index < 39 && !mergeItemStack(itemstack1, 0, 2, false))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (!mergeItemStack(itemstack1, 3, 39, false))
            {
                return ItemStack.EMPTY;
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

    /**
     * used by the Anvil GUI to update the Item Name being typed by the player
     */
    public void updateItemName(String newName)
    {
        repairedItemName = newName;

        if (getSlot(2).getHasStack())
        {
            ItemStack itemstack = getSlot(2).getStack();

            if (StringUtils.isBlank(newName))
            {
                itemstack.clearCustomName();
            }
            else
            {
                itemstack.setStackDisplayName(repairedItemName);
            }
        }

        updateRepairOutput();
    }
}
