package net.minecraft.entity.ai;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityAIHarvestFarmland extends EntityAIMoveToBlock
{
    /** Villager that is harvesting */
    private final EntityVillager theVillager;
    private boolean hasFarmItem;
    private boolean wantsToReapStuff;
    private int currentTask;

    public EntityAIHarvestFarmland(EntityVillager theVillagerIn, double speedIn)
    {
        super(theVillagerIn, speedIn, 16);
        theVillager = theVillagerIn;
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        if (runDelay <= 0)
        {
            if (!theVillager.world.getGameRules().getBoolean("mobGriefing"))
            {
                return false;
            }

            currentTask = -1;
            hasFarmItem = theVillager.isFarmItemInInventory();
            wantsToReapStuff = theVillager.wantsMoreFood();
        }

        return super.shouldExecute();
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        return currentTask >= 0 && super.continueExecuting();
    }

    /**
     * Updates the task
     */
    public void updateTask()
    {
        super.updateTask();
        theVillager.getLookHelper().setLookPosition((double) destinationBlock.getX() + 0.5D, destinationBlock.getY() + 1, (double) destinationBlock.getZ() + 0.5D, 10.0F, (float) theVillager.getVerticalFaceSpeed());

        if (getIsAboveDestination())
        {
            World world = theVillager.world;
            BlockPos blockpos = destinationBlock.up();
            IBlockState iblockstate = world.getBlockState(blockpos);
            Block block = iblockstate.getBlock();

            if (currentTask == 0 && block instanceof BlockCrops && ((BlockCrops)block).isMaxAge(iblockstate))
            {
                world.destroyBlock(blockpos, true);
            }
            else if (currentTask == 1 && iblockstate.getMaterial() == Material.AIR)
            {
                InventoryBasic inventorybasic = theVillager.getVillagerInventory();

                for (int i = 0; i < inventorybasic.getSizeInventory(); ++i)
                {
                    ItemStack itemstack = inventorybasic.getStackInSlot(i);
                    boolean flag = false;

                    if (!itemstack.isEmpty())
                    {
                        if (itemstack.getItem() == Items.WHEAT_SEEDS)
                        {
                            world.setBlockState(blockpos, Blocks.WHEAT.getDefaultState(), 3);
                            flag = true;
                        }
                        else if (itemstack.getItem() == Items.POTATO)
                        {
                            world.setBlockState(blockpos, Blocks.POTATOES.getDefaultState(), 3);
                            flag = true;
                        }
                        else if (itemstack.getItem() == Items.CARROT)
                        {
                            world.setBlockState(blockpos, Blocks.CARROTS.getDefaultState(), 3);
                            flag = true;
                        }
                        else if (itemstack.getItem() == Items.BEETROOT_SEEDS)
                        {
                            world.setBlockState(blockpos, Blocks.BEETROOTS.getDefaultState(), 3);
                            flag = true;
                        }
                    }

                    if (flag)
                    {
                        itemstack.func_190918_g(1);

                        if (itemstack.isEmpty())
                        {
                            inventorybasic.setInventorySlotContents(i, ItemStack.EMPTY);
                        }

                        break;
                    }
                }
            }

            currentTask = -1;
            runDelay = 10;
        }
    }

    /**
     * Return true to set given position as destination
     */
    protected boolean shouldMoveTo(World worldIn, BlockPos pos)
    {
        Block block = worldIn.getBlockState(pos).getBlock();

        if (block == Blocks.FARMLAND)
        {
            pos = pos.up();
            IBlockState iblockstate = worldIn.getBlockState(pos);
            block = iblockstate.getBlock();

            if (block instanceof BlockCrops && ((BlockCrops)block).isMaxAge(iblockstate) && wantsToReapStuff && (currentTask == 0 || currentTask < 0))
            {
                currentTask = 0;
                return true;
            }

            if (iblockstate.getMaterial() == Material.AIR && hasFarmItem && (currentTask == 1 || currentTask < 0))
            {
                currentTask = 1;
                return true;
            }
        }

        return false;
    }
}
