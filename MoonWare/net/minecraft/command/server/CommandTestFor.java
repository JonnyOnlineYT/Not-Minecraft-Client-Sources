package net.minecraft.command.server;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class CommandTestFor extends CommandBase
{
    /**
     * Gets the name of the command
     */
    public String getCommandName()
    {
        return "testfor";
    }

    /**
     * Return the required permission level for this command.
     */
    public int getRequiredPermissionLevel()
    {
        return 2;
    }

    /**
     * Gets the usage string for the command.
     */
    public String getCommandUsage(ICommandSender sender)
    {
        return "commands.testfor.usage";
    }

    /**
     * Callback for when the command is executed
     */
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        if (args.length < 1)
        {
            throw new WrongUsageException("commands.testfor.usage");
        }
        else
        {
            Entity entity = CommandBase.getEntity(server, sender, args[0]);
            NBTTagCompound nbttagcompound = null;

            if (args.length >= 2)
            {
                try
                {
                    nbttagcompound = JsonToNBT.getTagFromJson(CommandBase.buildString(args, 1));
                }
                catch (NBTException nbtexception)
                {
                    throw new CommandException("commands.testfor.tagError", nbtexception.getMessage());
                }
            }

            if (nbttagcompound != null)
            {
                NBTTagCompound nbttagcompound1 = CommandBase.entityToNBT(entity);

                if (!NBTUtil.areNBTEquals(nbttagcompound, nbttagcompound1, true))
                {
                    throw new CommandException("commands.testfor.failure", entity.getName());
                }
            }

            CommandBase.notifyCommandListener(sender, this, "commands.testfor.success", entity.getName());
        }
    }

    /**
     * Return whether the specified command parameter index is a username parameter.
     */
    public boolean isUsernameIndex(String[] args, int index)
    {
        return index == 0;
    }

    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos)
    {
        return args.length == 1 ? CommandBase.getListOfStringsMatchingLastWord(args, server.getAllUsernames()) : Collections.emptyList();
    }
}
