package net.minecraft.command;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslatableComponent;

public class CommandLocate extends CommandBase
{
    /**
     * Gets the name of the command
     */
    public String getCommandName()
    {
        return "locate";
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
        return "commands.locate.usage";
    }

    /**
     * Callback for when the command is executed
     */
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        if (args.length != 1)
        {
            throw new WrongUsageException("commands.locate.usage");
        }
        else
        {
            String s = args[0];
            BlockPos blockpos = sender.getEntityWorld().func_190528_a(s, sender.getPosition(), false);

            if (blockpos != null)
            {
                sender.addChatMessage(new TranslatableComponent("commands.locate.success", s, blockpos.getX(), blockpos.getZ()));
            }
            else
            {
                throw new CommandException("commands.locate.failure", s);
            }
        }
    }

    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos)
    {
        return args.length == 1 ? CommandBase.getListOfStringsMatchingLastWord(args, "Stronghold", "Monument", "Village", "Mansion", "EndCity", "Fortress", "Temple", "Mineshaft") : Collections.emptyList();
    }
}
