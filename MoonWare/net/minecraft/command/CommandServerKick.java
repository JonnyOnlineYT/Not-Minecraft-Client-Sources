package net.minecraft.command;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Component;
import net.minecraft.util.text.TranslatableComponent;

public class CommandServerKick extends CommandBase
{
    /**
     * Gets the name of the command
     */
    public String getCommandName()
    {
        return "kick";
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
        return "commands.kick.usage";
    }

    /**
     * Callback for when the command is executed
     */
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        if (args.length > 0 && args[0].length() > 1)
        {
            EntityPlayerMP entityplayermp = server.getPlayerList().getPlayerByUsername(args[0]);

            if (entityplayermp == null)
            {
                throw new PlayerNotFoundException("commands.generic.player.notFound", args[0]);
            }
            else
            {
                if (args.length >= 2)
                {
                    Component itextcomponent = CommandBase.getChatComponentFromNthArg(sender, args, 1);
                    entityplayermp.connection.func_194028_b(itextcomponent);
                    CommandBase.notifyCommandListener(sender, this, "commands.kick.success.reason", entityplayermp.getName(), itextcomponent.asString());
                }
                else
                {
                    entityplayermp.connection.func_194028_b(new TranslatableComponent("multiplayer.disconnect.kicked"));
                    CommandBase.notifyCommandListener(sender, this, "commands.kick.success", entityplayermp.getName());
                }
            }
        }
        else
        {
            throw new WrongUsageException("commands.kick.usage");
        }
    }

    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos)
    {
        return args.length >= 1 ? CommandBase.getListOfStringsMatchingLastWord(args, server.getAllUsernames()) : Collections.emptyList();
    }
}
