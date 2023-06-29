package net.minecraft.command;

import java.util.Objects;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.Component;
import net.minecraft.world.World;

public class CommandSenderWrapper implements ICommandSender
{
    private final ICommandSender field_193043_a;
    @Nullable
    private final Vec3d field_194002_b;
    @Nullable
    private final BlockPos field_194003_c;
    @Nullable
    private final Integer field_194004_d;
    @Nullable
    private final Entity field_194005_e;
    @Nullable
    private final Boolean field_194006_f;

    public CommandSenderWrapper(ICommandSender p_i47599_1_, @Nullable Vec3d p_i47599_2_, @Nullable BlockPos p_i47599_3_, @Nullable Integer p_i47599_4_, @Nullable Entity p_i47599_5_, @Nullable Boolean p_i47599_6_)
    {
        field_193043_a = p_i47599_1_;
        field_194002_b = p_i47599_2_;
        field_194003_c = p_i47599_3_;
        field_194004_d = p_i47599_4_;
        field_194005_e = p_i47599_5_;
        field_194006_f = p_i47599_6_;
    }

    public static CommandSenderWrapper func_193998_a(ICommandSender p_193998_0_)
    {
        return p_193998_0_ instanceof CommandSenderWrapper ? (CommandSenderWrapper)p_193998_0_ : new CommandSenderWrapper(p_193998_0_, null, null, null, null, null);
    }

    public CommandSenderWrapper func_193997_a(Entity p_193997_1_, Vec3d p_193997_2_)
    {
        return field_194005_e == p_193997_1_ && Objects.equals(field_194002_b, p_193997_2_) ? this : new CommandSenderWrapper(field_193043_a, p_193997_2_, new BlockPos(p_193997_2_), field_194004_d, p_193997_1_, field_194006_f);
    }

    public CommandSenderWrapper func_193999_a(int p_193999_1_)
    {
        return field_194004_d != null && field_194004_d.intValue() <= p_193999_1_ ? this : new CommandSenderWrapper(field_193043_a, field_194002_b, field_194003_c, p_193999_1_, field_194005_e, field_194006_f);
    }

    public CommandSenderWrapper func_194001_a(boolean p_194001_1_)
    {
        return field_194006_f == null || field_194006_f.booleanValue() && !p_194001_1_ ? new CommandSenderWrapper(field_193043_a, field_194002_b, field_194003_c, field_194004_d, field_194005_e, p_194001_1_) : this;
    }

    public CommandSenderWrapper func_194000_i()
    {
        return field_194002_b != null ? this : new CommandSenderWrapper(field_193043_a, getPositionVector(), getPosition(), field_194004_d, field_194005_e, field_194006_f);
    }

    /**
     * Get the name of this object. For players this returns their username
     */
    public String getName()
    {
        return field_194005_e != null ? field_194005_e.getName() : field_193043_a.getName();
    }

    /**
     * Get the formatted ChatComponent that will be used for the sender's username in chat
     */
    public Component getDisplayName()
    {
        return field_194005_e != null ? field_194005_e.getDisplayName() : field_193043_a.getDisplayName();
    }

    /**
     * Send a chat message to the CommandSender
     */
    public void addChatMessage(Component component)
    {
        if (field_194006_f == null || field_194006_f.booleanValue())
        {
            field_193043_a.addChatMessage(component);
        }
    }

    /**
     * Returns {@code true} if the CommandSender is allowed to execute the command, {@code false} if not
     */
    public boolean canCommandSenderUseCommand(int permLevel, String commandName)
    {
        return (field_194004_d == null || field_194004_d.intValue() >= permLevel) && field_193043_a.canCommandSenderUseCommand(permLevel, commandName);
    }

    /**
     * Get the position in the world. <b>{@code null} is not allowed!</b> If you are not an entity in the world, return
     * the coordinates 0, 0, 0
     */
    public BlockPos getPosition()
    {
        if (field_194003_c != null)
        {
            return field_194003_c;
        }
        else
        {
            return field_194005_e != null ? field_194005_e.getPosition() : field_193043_a.getPosition();
        }
    }

    /**
     * Get the position vector. <b>{@code null} is not allowed!</b> If you are not an entity in the world, return 0.0D,
     * 0.0D, 0.0D
     */
    public Vec3d getPositionVector()
    {
        if (field_194002_b != null)
        {
            return field_194002_b;
        }
        else
        {
            return field_194005_e != null ? field_194005_e.getPositionVector() : field_193043_a.getPositionVector();
        }
    }

    /**
     * Get the world, if available. <b>{@code null} is not allowed!</b> If you are not an entity in the world, return
     * the overworld
     */
    public World getEntityWorld()
    {
        return field_194005_e != null ? field_194005_e.getEntityWorld() : field_193043_a.getEntityWorld();
    }

    @Nullable

    /**
     * Returns the entity associated with the command sender. MAY BE NULL!
     */
    public Entity getCommandSenderEntity()
    {
        return field_194005_e != null ? field_194005_e.getCommandSenderEntity() : field_193043_a.getCommandSenderEntity();
    }

    /**
     * Returns true if the command sender should be sent feedback about executed commands
     */
    public boolean sendCommandFeedback()
    {
        return field_194006_f != null ? field_194006_f.booleanValue() : field_193043_a.sendCommandFeedback();
    }

    public void setCommandStat(CommandResultStats.Type type, int amount)
    {
        if (field_194005_e != null)
        {
            field_194005_e.setCommandStat(type, amount);
        }
        else
        {
            field_193043_a.setCommandStat(type, amount);
        }
    }

    @Nullable

    /**
     * Get the Minecraft server instance
     */
    public MinecraftServer getServer()
    {
        return field_193043_a.getServer();
    }
}
