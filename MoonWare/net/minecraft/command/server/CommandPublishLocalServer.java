package net.minecraft.command.server;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.GameType;

public class CommandPublishLocalServer extends CommandBase
{
    /**
     * Gets the name of the command
     */
    public String getCommandName()
    {
        return "publish";
    }

    /**
     * Gets the usage string for the command.
     */
    public String getCommandUsage(ICommandSender sender)
    {
        return "commands.publish.usage";
    }

    /**
     * Callback for when the command is executed
     */
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        String s = server.shareToLAN(GameType.SURVIVAL, false);

        if (s != null)
        {
            CommandBase.notifyCommandListener(sender, this, "commands.publish.started", s);
        }
        else
        {
            CommandBase.notifyCommandListener(sender, this, "commands.publish.failed");
        }
    }
}
