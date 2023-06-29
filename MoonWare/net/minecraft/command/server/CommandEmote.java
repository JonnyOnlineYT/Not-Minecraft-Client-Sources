package net.minecraft.command.server;

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
import net.minecraft.util.text.TranslatableComponent;

public class CommandEmote extends CommandBase
{
    /**
     * Gets the name of the command
     */
    public String getCommandName()
    {
        return "me";
    }

    /**
     * Return the required permission level for this command.
     */
    public int getRequiredPermissionLevel()
    {
        return 0;
    }

    /**
     * Gets the usage string for the command.
     */
    public String getCommandUsage(ICommandSender sender)
    {
        return "commands.me.usage";
    }

    /**
     * Callback for when the command is executed
     */
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        if (args.length <= 0)
        {
            throw new WrongUsageException("commands.me.usage");
        }
        else
        {
            Component itextcomponent = CommandBase.getChatComponentFromNthArg(sender, args, 0, !(sender instanceof EntityPlayer));
            server.getPlayerList().sendChatMsg(new TranslatableComponent("chat.type.emote", sender.getDisplayName(), itextcomponent));
        }
    }

    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos)
    {
        return CommandBase.getListOfStringsMatchingLastWord(args, server.getAllUsernames());
    }
}
