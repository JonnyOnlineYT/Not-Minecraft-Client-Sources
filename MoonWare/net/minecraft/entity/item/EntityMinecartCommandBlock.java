package net.minecraft.entity.item;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.CommandBlockBaseLogic;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Namespaced;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.IDataFixer;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.Component;
import net.minecraft.util.text.TextComponent;
import net.minecraft.world.World;

public class EntityMinecartCommandBlock extends EntityMinecart
{
    private static final DataParameter<String> COMMAND = EntityDataManager.createKey(EntityMinecartCommandBlock.class, DataSerializers.STRING);
    private static final DataParameter<Component> LAST_OUTPUT = EntityDataManager.createKey(EntityMinecartCommandBlock.class, DataSerializers.TEXT_COMPONENT);
    private final CommandBlockBaseLogic commandBlockLogic = new CommandBlockBaseLogic()
    {
        public void updateCommand()
        {
            getDataManager().set(COMMAND, getCommand());
            getDataManager().set(LAST_OUTPUT, getLastOutput());
        }
        public int getCommandBlockType()
        {
            return 1;
        }
        public void fillInInfo(ByteBuf buf)
        {
            buf.writeInt(getEntityId());
        }
        public BlockPos getPosition()
        {
            return new BlockPos(posX, posY + 0.5D, posZ);
        }
        public Vec3d getPositionVector()
        {
            return new Vec3d(posX, posY, posZ);
        }
        public World getEntityWorld()
        {
            return world;
        }
        public Entity getCommandSenderEntity()
        {
            return EntityMinecartCommandBlock.this;
        }
        public MinecraftServer getServer()
        {
            return world.getInstanceServer();
        }
    };

    /** Cooldown before command block logic runs again in ticks */
    private int activatorRailCooldown;

    public EntityMinecartCommandBlock(World worldIn)
    {
        super(worldIn);
    }

    public EntityMinecartCommandBlock(World worldIn, double x, double y, double z)
    {
        super(worldIn, x, y, z);
    }

    public static void registerFixesMinecartCommand(DataFixer fixer)
    {
        EntityMinecart.registerFixesMinecart(fixer, EntityMinecartCommandBlock.class);
        fixer.registerWalker(FixTypes.ENTITY, new IDataWalker()
        {
            public NBTTagCompound process(IDataFixer fixer, NBTTagCompound compound, int versionIn)
            {
                if (TileEntity.func_190559_a(TileEntityCommandBlock.class).equals(new Namespaced(compound.getString("id"))))
                {
                    compound.setString("id", "Control");
                    fixer.process(FixTypes.BLOCK_ENTITY, compound, versionIn);
                    compound.setString("id", "MinecartCommandBlock");
                }

                return compound;
            }
        });
    }

    protected void entityInit()
    {
        super.entityInit();
        getDataManager().register(COMMAND, "");
        getDataManager().register(LAST_OUTPUT, new TextComponent(""));
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    protected void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
        commandBlockLogic.readDataFromNBT(compound);
        getDataManager().set(COMMAND, getCommandBlockLogic().getCommand());
        getDataManager().set(LAST_OUTPUT, getCommandBlockLogic().getLastOutput());
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    protected void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        commandBlockLogic.writeToNBT(compound);
    }

    public EntityMinecart.Type getType()
    {
        return EntityMinecart.Type.COMMAND_BLOCK;
    }

    public IBlockState getDefaultDisplayTile()
    {
        return Blocks.COMMAND_BLOCK.getDefaultState();
    }

    public CommandBlockBaseLogic getCommandBlockLogic()
    {
        return commandBlockLogic;
    }

    /**
     * Called every tick the minecart is on an activator rail.
     */
    public void onActivatorRailPass(int x, int y, int z, boolean receivingPower)
    {
        if (receivingPower && ticksExisted - activatorRailCooldown >= 4)
        {
            getCommandBlockLogic().trigger(world);
            activatorRailCooldown = ticksExisted;
        }
    }

    public boolean processInitialInteract(EntityPlayer player, EnumHand stack)
    {
        commandBlockLogic.tryOpenEditCommandBlock(player);
        return false;
    }

    public void notifyDataManagerChange(DataParameter<?> key)
    {
        super.notifyDataManagerChange(key);

        if (LAST_OUTPUT.equals(key))
        {
            try
            {
                commandBlockLogic.setLastOutput(getDataManager().get(LAST_OUTPUT));
            }
            catch (Throwable var3)
            {
            }
        }
        else if (COMMAND.equals(key))
        {
            commandBlockLogic.setCommand(getDataManager().get(COMMAND));
        }
    }

    public boolean ignoreItemEntityData()
    {
        return true;
    }
}
