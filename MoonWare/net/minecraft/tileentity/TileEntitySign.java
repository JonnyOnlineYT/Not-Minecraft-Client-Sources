package net.minecraft.tileentity;

import javax.annotation.Nullable;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.Component;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.Components;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.world.World;

public class TileEntitySign extends TileEntity
{
    public final Component[] signText = {new TextComponent(""), new TextComponent(""), new TextComponent(""), new TextComponent("")};

    /**
     * The index of the line currently being edited. Only used on client side, but defined on both. Note this is only
     * really used when the > < are going to be visible.
     */
    public int lineBeingEdited = -1;
    private boolean isEditable = true;
    private EntityPlayer player;
    private final CommandResultStats stats = new CommandResultStats();

    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);

        for (int i = 0; i < 4; ++i)
        {
            String s = Component.Serializer.componentToJson(signText[i]);
            compound.setString("Text" + (i + 1), s);
        }

        stats.writeStatsToNBT(compound);
        return compound;
    }

    protected void setWorldCreate(World worldIn)
    {
        setWorldObj(worldIn);
    }

    public void readFromNBT(NBTTagCompound compound)
    {
        isEditable = false;
        super.readFromNBT(compound);
        ICommandSender icommandsender = new ICommandSender()
        {
            public String getName()
            {
                return "Sign";
            }
            public boolean canCommandSenderUseCommand(int permLevel, String commandName)
            {
                return true;
            }
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
                return world;
            }
            public MinecraftServer getServer()
            {
                return world.getInstanceServer();
            }
        };

        for (int i = 0; i < 4; ++i)
        {
            String s = compound.getString("Text" + (i + 1));
            Component itextcomponent = Component.Serializer.jsonToComponent(s);

            try
            {
                signText[i] = Components.processComponent(icommandsender, itextcomponent, null);
            }
            catch (CommandException var7)
            {
                signText[i] = itextcomponent;
            }
        }

        stats.readStatsFromNBT(compound);
    }

    @Nullable
    public SPacketUpdateTileEntity getUpdatePacket()
    {
        return new SPacketUpdateTileEntity(pos, 9, getUpdateTag());
    }

    public NBTTagCompound getUpdateTag()
    {
        return writeToNBT(new NBTTagCompound());
    }

    public boolean onlyOpsCanSetNbt()
    {
        return true;
    }

    public boolean getIsEditable()
    {
        return isEditable;
    }

    /**
     * Sets the sign's isEditable flag to the specified parameter.
     */
    public void setEditable(boolean isEditableIn)
    {
        isEditable = isEditableIn;

        if (!isEditableIn)
        {
            player = null;
        }
    }

    public void setPlayer(EntityPlayer playerIn)
    {
        player = playerIn;
    }

    public EntityPlayer getPlayer()
    {
        return player;
    }

    public boolean executeCommand(EntityPlayer playerIn)
    {
        ICommandSender icommandsender = new ICommandSender()
        {
            public String getName()
            {
                return playerIn.getName();
            }
            public Component getDisplayName()
            {
                return playerIn.getDisplayName();
            }
            public void addChatMessage(Component component)
            {
            }
            public boolean canCommandSenderUseCommand(int permLevel, String commandName)
            {
                return permLevel <= 2;
            }
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
                return playerIn.getEntityWorld();
            }
            public Entity getCommandSenderEntity()
            {
                return playerIn;
            }
            public boolean sendCommandFeedback()
            {
                return false;
            }
            public void setCommandStat(CommandResultStats.Type type, int amount)
            {
                if (world != null && !world.isRemote)
                {
                    stats.setCommandStatForSender(world.getInstanceServer(), this, type, amount);
                }
            }
            public MinecraftServer getServer()
            {
                return playerIn.getServer();
            }
        };

        for (Component itextcomponent : signText)
        {
            Style style = itextcomponent == null ? null : itextcomponent.getStyle();

            if (style != null && style.getClickEvent() != null)
            {
                ClickEvent clickevent = style.getClickEvent();

                if (clickevent.getAction() == ClickEvent.Action.RUN_COMMAND)
                {
                    playerIn.getServer().getCommandManager().executeCommand(icommandsender, clickevent.getValue());
                }
            }
        }

        return true;
    }

    public CommandResultStats getStats()
    {
        return stats;
    }
}
