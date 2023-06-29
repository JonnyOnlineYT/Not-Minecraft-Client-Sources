package net.minecraft.tileentity;

import io.netty.buffer.ByteBuf;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCommandBlock;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandResultStats;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class TileEntityCommandBlock extends TileEntity
{
    private boolean powered;
    private boolean auto;
    private boolean conditionMet;
    private boolean sendToClient;
    private final CommandBlockBaseLogic commandBlockLogic = new CommandBlockBaseLogic()
    {
        public BlockPos getPosition()
        {
            return pos;
        }
        public Vec3d getPositionVector()
        {
            return new Vec3d((double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D);
        }
        public World getEntityWorld()
        {
            return getWorld();
        }
        public void setCommand(String command)
        {
            super.setCommand(command);
            markDirty();
        }
        public void updateCommand()
        {
            IBlockState iblockstate = world.getBlockState(pos);
            getWorld().notifyBlockUpdate(pos, iblockstate, iblockstate, 3);
        }
        public int getCommandBlockType()
        {
            return 0;
        }
        public void fillInInfo(ByteBuf buf)
        {
            buf.writeInt(pos.getX());
            buf.writeInt(pos.getY());
            buf.writeInt(pos.getZ());
        }
        public MinecraftServer getServer()
        {
            return world.getInstanceServer();
        }
    };

    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        commandBlockLogic.writeToNBT(compound);
        compound.setBoolean("powered", isPowered());
        compound.setBoolean("conditionMet", isConditionMet());
        compound.setBoolean("auto", isAuto());
        return compound;
    }

    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        commandBlockLogic.readDataFromNBT(compound);
        powered = compound.getBoolean("powered");
        conditionMet = compound.getBoolean("conditionMet");
        setAuto(compound.getBoolean("auto"));
    }

    @Nullable
    public SPacketUpdateTileEntity getUpdatePacket()
    {
        if (isSendToClient())
        {
            setSendToClient(false);
            NBTTagCompound nbttagcompound = writeToNBT(new NBTTagCompound());
            return new SPacketUpdateTileEntity(pos, 2, nbttagcompound);
        }
        else
        {
            return null;
        }
    }

    public boolean onlyOpsCanSetNbt()
    {
        return true;
    }

    public CommandBlockBaseLogic getCommandBlockLogic()
    {
        return commandBlockLogic;
    }

    public CommandResultStats getCommandResultStats()
    {
        return commandBlockLogic.getCommandResultStats();
    }

    public void setPowered(boolean poweredIn)
    {
        powered = poweredIn;
    }

    public boolean isPowered()
    {
        return powered;
    }

    public boolean isAuto()
    {
        return auto;
    }

    public void setAuto(boolean autoIn)
    {
        boolean flag = auto;
        auto = autoIn;

        if (!flag && autoIn && !powered && world != null && getMode() != TileEntityCommandBlock.Mode.SEQUENCE)
        {
            Block block = getBlockType();

            if (block instanceof BlockCommandBlock)
            {
                setConditionMet();
                world.scheduleUpdate(pos, block, block.tickRate(world));
            }
        }
    }

    public boolean isConditionMet()
    {
        return conditionMet;
    }

    public boolean setConditionMet()
    {
        conditionMet = true;

        if (isConditional())
        {
            BlockPos blockpos = pos.offset(world.getBlockState(pos).getValue(BlockCommandBlock.FACING).getOpposite());

            if (world.getBlockState(blockpos).getBlock() instanceof BlockCommandBlock)
            {
                TileEntity tileentity = world.getTileEntity(blockpos);
                conditionMet = tileentity instanceof TileEntityCommandBlock && ((TileEntityCommandBlock)tileentity).getCommandBlockLogic().getSuccessCount() > 0;
            }
            else
            {
                conditionMet = false;
            }
        }

        return conditionMet;
    }

    public boolean isSendToClient()
    {
        return sendToClient;
    }

    public void setSendToClient(boolean p_184252_1_)
    {
        sendToClient = p_184252_1_;
    }

    public TileEntityCommandBlock.Mode getMode()
    {
        Block block = getBlockType();

        if (block == Blocks.COMMAND_BLOCK)
        {
            return TileEntityCommandBlock.Mode.REDSTONE;
        }
        else if (block == Blocks.REPEATING_COMMAND_BLOCK)
        {
            return TileEntityCommandBlock.Mode.AUTO;
        }
        else
        {
            return block == Blocks.CHAIN_COMMAND_BLOCK ? TileEntityCommandBlock.Mode.SEQUENCE : TileEntityCommandBlock.Mode.REDSTONE;
        }
    }

    public boolean isConditional()
    {
        IBlockState iblockstate = world.getBlockState(getPos());
        return iblockstate.getBlock() instanceof BlockCommandBlock && iblockstate.getValue(BlockCommandBlock.CONDITIONAL).booleanValue();
    }

    /**
     * validates a tile entity
     */
    public void validate()
    {
        blockType = null;
        super.validate();
    }

    public enum Mode
    {
        SEQUENCE,
        AUTO,
        REDSTONE
    }
}
