package net.minecraft.command;

import net.minecraft.server.MinecraftServer;

public class CommandSetPlayerTimeout extends CommandBase
{
    /**
     * Gets the name of the command
     */
    public String getCommandName()
    {
        return "setidletimeout";
    }

    /**
     * Return the required permission level for this command.
     */
    public int getRequiredPermissionLevel()
    {
        return 3;
    }

    /**
     * Gets the usage string for the command.
     */
    public String getCommandUsage(ICommandSender sender)
    {
        return "commands.setidletimeout.usage";
    }

    /**
     * Callback for when the command is executed
     */
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        if (args.length != 1)
        {
            throw new WrongUsageException("commands.setidletimeout.usage");
        }
        else
        {
            int i = CommandBase.parseInt(args[0], 0);
            server.setPlayerIdleTimeout(i);
            CommandBase.notifyCommandListener(sender, this, "commands.setidletimeout.success", i);
        }
    }
}
