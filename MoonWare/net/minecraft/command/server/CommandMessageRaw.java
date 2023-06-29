package net.minecraft.command.server;

import com.google.gson.JsonParseException;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Component;
import net.minecraft.util.text.Components;

public class CommandMessageRaw extends CommandBase
{
    /**
     * Gets the name of the command
     */
    public String getCommandName()
    {
        return "tellraw";
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
        return "commands.tellraw.usage";
    }

    /**
     * Callback for when the command is executed
     */
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        if (args.length < 2)
        {
            throw new WrongUsageException("commands.tellraw.usage");
        }
        else
        {
            EntityPlayer entityplayer = CommandBase.getPlayer(server, sender, args[0]);
            String s = CommandBase.buildString(args, 1);

            try
            {
                Component itextcomponent = Component.Serializer.jsonToComponent(s);
                entityplayer.addChatMessage(Components.processComponent(sender, itextcomponent, entityplayer));
            }
            catch (JsonParseException jsonparseexception)
            {
                throw CommandBase.toSyntaxException(jsonparseexception);
            }
        }
    }

    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos)
    {
        return args.length == 1 ? CommandBase.getListOfStringsMatchingLastWord(args, server.getAllUsernames()) : Collections.emptyList();
    }

    /**
     * Return whether the specified command parameter index is a username parameter.
     */
    public boolean isUsernameIndex(String[] args, int index)
    {
        return index == 0;
    }
}
