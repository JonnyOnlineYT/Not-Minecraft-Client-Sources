package net.minecraft.server.management;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockCommandBlock;
import net.minecraft.block.BlockStructure;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.network.play.server.SPacketPlayerListItem;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameType;
import net.minecraft.world.ILockableContainer;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class PlayerInteractionManager
{
    /** The world object that this object is connected to. */
    public World theWorld;

    /** The EntityPlayerMP object that this object is connected to. */
    public EntityPlayerMP thisPlayerMP;
    private GameType gameType = GameType.NOT_SET;

    /** True if the player is destroying a block */
    private boolean isDestroyingBlock;
    private int initialDamage;
    private BlockPos destroyPos = BlockPos.ORIGIN;
    private int curblockDamage;

    /**
     * Set to true when the "finished destroying block" packet is received but the block wasn't fully damaged yet. The
     * block will not be destroyed while this is false.
     */
    private boolean receivedFinishDiggingPacket;
    private BlockPos delayedDestroyPos = BlockPos.ORIGIN;
    private int initialBlockDamage;
    private int durabilityRemainingOnBlock = -1;

    public PlayerInteractionManager(World worldIn)
    {
        theWorld = worldIn;
    }

    public void setGameType(GameType type)
    {
        gameType = type;
        type.configurePlayerCapabilities(thisPlayerMP.capabilities);
        thisPlayerMP.sendPlayerAbilities();
        thisPlayerMP.mcServer.getPlayerList().sendPacketToAllPlayers(new SPacketPlayerListItem(SPacketPlayerListItem.Action.UPDATE_GAME_MODE, thisPlayerMP));
        theWorld.updateAllPlayersSleepingFlag();
    }

    public GameType getGameType()
    {
        return gameType;
    }

    public boolean survivalOrAdventure()
    {
        return gameType.isSurvivalOrAdventure();
    }

    /**
     * Get if we are in creative game mode.
     */
    public boolean isCreative()
    {
        return gameType.isCreative();
    }

    /**
     * if the gameType is currently NOT_SET then change it to par1
     */
    public void initializeGameType(GameType type)
    {
        if (gameType == GameType.NOT_SET)
        {
            gameType = type;
        }

        setGameType(gameType);
    }

    public void updateBlockRemoving()
    {
        ++curblockDamage;

        if (receivedFinishDiggingPacket)
        {
            int i = curblockDamage - initialBlockDamage;
            IBlockState iblockstate = theWorld.getBlockState(delayedDestroyPos);

            if (iblockstate.getMaterial() == Material.AIR)
            {
                receivedFinishDiggingPacket = false;
            }
            else
            {
                float f = iblockstate.getPlayerRelativeBlockHardness(thisPlayerMP, thisPlayerMP.world, delayedDestroyPos) * (float)(i + 1);
                int j = (int)(f * 10.0F);

                if (j != durabilityRemainingOnBlock)
                {
                    theWorld.sendBlockBreakProgress(thisPlayerMP.getEntityId(), delayedDestroyPos, j);
                    durabilityRemainingOnBlock = j;
                }

                if (f >= 1.0F)
                {
                    receivedFinishDiggingPacket = false;
                    tryHarvestBlock(delayedDestroyPos);
                }
            }
        }
        else if (isDestroyingBlock)
        {
            IBlockState iblockstate1 = theWorld.getBlockState(destroyPos);

            if (iblockstate1.getMaterial() == Material.AIR)
            {
                theWorld.sendBlockBreakProgress(thisPlayerMP.getEntityId(), destroyPos, -1);
                durabilityRemainingOnBlock = -1;
                isDestroyingBlock = false;
            }
            else
            {
                int k = curblockDamage - initialDamage;
                float f1 = iblockstate1.getPlayerRelativeBlockHardness(thisPlayerMP, thisPlayerMP.world, delayedDestroyPos) * (float)(k + 1);
                int l = (int)(f1 * 10.0F);

                if (l != durabilityRemainingOnBlock)
                {
                    theWorld.sendBlockBreakProgress(thisPlayerMP.getEntityId(), destroyPos, l);
                    durabilityRemainingOnBlock = l;
                }
            }
        }
    }

    /**
     * If not creative, it calls sendBlockBreakProgress until the block is broken first. tryHarvestBlock can also be the
     * result of this call.
     */
    public void onBlockClicked(BlockPos pos, EnumFacing side)
    {
        if (isCreative())
        {
            if (!theWorld.extinguishFire(null, pos, side))
            {
                tryHarvestBlock(pos);
            }
        }
        else
        {
            IBlockState iblockstate = theWorld.getBlockState(pos);
            Block block = iblockstate.getBlock();

            if (gameType.isAdventure())
            {
                if (gameType == GameType.SPECTATOR)
                {
                    return;
                }

                if (!thisPlayerMP.isAllowEdit())
                {
                    ItemStack itemstack = thisPlayerMP.getHeldItemMainhand();

                    if (itemstack.isEmpty())
                    {
                        return;
                    }

                    if (!itemstack.canDestroy(block))
                    {
                        return;
                    }
                }
            }

            theWorld.extinguishFire(null, pos, side);
            initialDamage = curblockDamage;
            float f = 1.0F;

            if (iblockstate.getMaterial() != Material.AIR)
            {
                block.onBlockClicked(theWorld, pos, thisPlayerMP);
                f = iblockstate.getPlayerRelativeBlockHardness(thisPlayerMP, thisPlayerMP.world, pos);
            }

            if (iblockstate.getMaterial() != Material.AIR && f >= 1.0F)
            {
                tryHarvestBlock(pos);
            }
            else
            {
                isDestroyingBlock = true;
                destroyPos = pos;
                int i = (int)(f * 10.0F);
                theWorld.sendBlockBreakProgress(thisPlayerMP.getEntityId(), pos, i);
                durabilityRemainingOnBlock = i;
            }
        }
    }

    public void blockRemoving(BlockPos pos)
    {
        if (pos.equals(destroyPos))
        {
            int i = curblockDamage - initialDamage;
            IBlockState iblockstate = theWorld.getBlockState(pos);

            if (iblockstate.getMaterial() != Material.AIR)
            {
                float f = iblockstate.getPlayerRelativeBlockHardness(thisPlayerMP, thisPlayerMP.world, pos) * (float)(i + 1);

                if (f >= 0.7F)
                {
                    isDestroyingBlock = false;
                    theWorld.sendBlockBreakProgress(thisPlayerMP.getEntityId(), pos, -1);
                    tryHarvestBlock(pos);
                }
                else if (!receivedFinishDiggingPacket)
                {
                    isDestroyingBlock = false;
                    receivedFinishDiggingPacket = true;
                    delayedDestroyPos = pos;
                    initialBlockDamage = initialDamage;
                }
            }
        }
    }

    /**
     * Stops the block breaking process
     */
    public void cancelDestroyingBlock()
    {
        isDestroyingBlock = false;
        theWorld.sendBlockBreakProgress(thisPlayerMP.getEntityId(), destroyPos, -1);
    }

    /**
     * Removes a block and triggers the appropriate events
     */
    private boolean removeBlock(BlockPos pos)
    {
        IBlockState iblockstate = theWorld.getBlockState(pos);
        iblockstate.getBlock().onBlockHarvested(theWorld, pos, iblockstate, thisPlayerMP);
        boolean flag = theWorld.setBlockToAir(pos);

        if (flag)
        {
            iblockstate.getBlock().onBlockDestroyedByPlayer(theWorld, pos, iblockstate);
        }

        return flag;
    }

    /**
     * Attempts to harvest a block
     */
    public boolean tryHarvestBlock(BlockPos pos)
    {
        if (gameType.isCreative() && !thisPlayerMP.getHeldItemMainhand().isEmpty() && thisPlayerMP.getHeldItemMainhand().getItem() instanceof ItemSword)
        {
            return false;
        }
        else
        {
            IBlockState iblockstate = theWorld.getBlockState(pos);
            TileEntity tileentity = theWorld.getTileEntity(pos);
            Block block = iblockstate.getBlock();

            if ((block instanceof BlockCommandBlock || block instanceof BlockStructure) && !thisPlayerMP.canUseCommandBlock())
            {
                theWorld.notifyBlockUpdate(pos, iblockstate, iblockstate, 3);
                return false;
            }
            else
            {
                if (gameType.isAdventure())
                {
                    if (gameType == GameType.SPECTATOR)
                    {
                        return false;
                    }

                    if (!thisPlayerMP.isAllowEdit())
                    {
                        ItemStack itemstack = thisPlayerMP.getHeldItemMainhand();

                        if (itemstack.isEmpty())
                        {
                            return false;
                        }

                        if (!itemstack.canDestroy(block))
                        {
                            return false;
                        }
                    }
                }

                theWorld.playEvent(thisPlayerMP, 2001, pos, Block.getStateId(iblockstate));
                boolean flag1 = removeBlock(pos);

                if (isCreative())
                {
                    thisPlayerMP.connection.sendPacket(new SPacketBlockChange(theWorld, pos));
                }
                else
                {
                    ItemStack itemstack1 = thisPlayerMP.getHeldItemMainhand();
                    ItemStack itemstack2 = itemstack1.isEmpty() ? ItemStack.EMPTY : itemstack1.copy();
                    boolean flag = thisPlayerMP.canHarvestBlock(iblockstate);

                    if (!itemstack1.isEmpty())
                    {
                        itemstack1.onBlockDestroyed(theWorld, iblockstate, pos, thisPlayerMP);
                    }

                    if (flag1 && flag)
                    {
                        iblockstate.getBlock().harvestBlock(theWorld, thisPlayerMP, pos, iblockstate, tileentity, itemstack2);
                    }
                }

                return flag1;
            }
        }
    }

    public EnumActionResult processRightClick(EntityPlayer player, World worldIn, ItemStack stack, EnumHand hand)
    {
        if (gameType == GameType.SPECTATOR)
        {
            return EnumActionResult.PASS;
        }
        else if (player.getCooldownTracker().hasCooldown(stack.getItem()))
        {
            return EnumActionResult.PASS;
        }
        else
        {
            int i = stack.getCount();
            int j = stack.getMetadata();
            ActionResult<ItemStack> actionresult = stack.useItemRightClick(worldIn, player, hand);
            ItemStack itemstack = actionresult.getResult();

            if (itemstack == stack && itemstack.getCount() == i && itemstack.getMaxItemUseDuration() <= 0 && itemstack.getMetadata() == j)
            {
                return actionresult.getType();
            }
            else if (actionresult.getType() == EnumActionResult.FAIL && itemstack.getMaxItemUseDuration() > 0 && !player.isHandActive())
            {
                return actionresult.getType();
            }
            else
            {
                player.setHeldItem(hand, itemstack);

                if (isCreative())
                {
                    itemstack.func_190920_e(i);

                    if (itemstack.isItemStackDamageable())
                    {
                        itemstack.setItemDamage(j);
                    }
                }

                if (itemstack.isEmpty())
                {
                    player.setHeldItem(hand, ItemStack.EMPTY);
                }

                if (!player.isHandActive())
                {
                    ((EntityPlayerMP)player).sendContainerToPlayer(player.inventoryContainer);
                }

                return actionresult.getType();
            }
        }
    }

    public EnumActionResult processRightClickBlock(EntityPlayer player, World worldIn, ItemStack stack, EnumHand hand, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if (gameType == GameType.SPECTATOR)
        {
            TileEntity tileentity = worldIn.getTileEntity(pos);

            if (tileentity instanceof ILockableContainer)
            {
                Block block1 = worldIn.getBlockState(pos).getBlock();
                ILockableContainer ilockablecontainer = (ILockableContainer)tileentity;

                if (ilockablecontainer instanceof TileEntityChest && block1 instanceof BlockChest)
                {
                    ilockablecontainer = ((BlockChest)block1).getLockableContainer(worldIn, pos);
                }

                if (ilockablecontainer != null)
                {
                    player.displayGUIChest(ilockablecontainer);
                    return EnumActionResult.SUCCESS;
                }
            }
            else if (tileentity instanceof IInventory)
            {
                player.displayGUIChest((IInventory)tileentity);
                return EnumActionResult.SUCCESS;
            }

            return EnumActionResult.PASS;
        }
        else
        {
            if (!player.isSneaking() || player.getHeldItemMainhand().isEmpty() && player.getHeldItemOffhand().isEmpty())
            {
                IBlockState iblockstate = worldIn.getBlockState(pos);

                if (iblockstate.getBlock().onBlockActivated(worldIn, pos, iblockstate, player, hand, facing, hitX, hitY, hitZ))
                {
                    return EnumActionResult.SUCCESS;
                }
            }

            if (stack.isEmpty())
            {
                return EnumActionResult.PASS;
            }
            else if (player.getCooldownTracker().hasCooldown(stack.getItem()))
            {
                return EnumActionResult.PASS;
            }
            else
            {
                if (stack.getItem() instanceof ItemBlock && !player.canUseCommandBlock())
                {
                    Block block = ((ItemBlock)stack.getItem()).getBlock();

                    if (block instanceof BlockCommandBlock || block instanceof BlockStructure)
                    {
                        return EnumActionResult.FAIL;
                    }
                }

                if (isCreative())
                {
                    int j = stack.getMetadata();
                    int i = stack.getCount();
                    EnumActionResult enumactionresult = stack.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
                    stack.setItemDamage(j);
                    stack.func_190920_e(i);
                    return enumactionresult;
                }
                else
                {
                    return stack.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
                }
            }
        }
    }

    /**
     * Sets the world instance.
     */
    public void setWorld(WorldServer serverWorld)
    {
        theWorld = serverWorld;
    }
}
