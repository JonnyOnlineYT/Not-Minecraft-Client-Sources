package net.minecraft.command;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class CommandXP extends CommandBase
{
    /**
     * Gets the name of the command
     */
    public String getCommandName()
    {
        return "xp";
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
        return "commands.xp.usage";
    }

    /**
     * Callback for when the command is executed
     */
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        if (args.length <= 0)
        {
            throw new WrongUsageException("commands.xp.usage");
        }
        else
        {
            String s = args[0];
            boolean flag = s.endsWith("l") || s.endsWith("L");

            if (flag && s.length() > 1)
            {
                s = s.substring(0, s.length() - 1);
            }

            int i = CommandBase.parseInt(s);
            boolean flag1 = i < 0;

            if (flag1)
            {
                i *= -1;
            }

            EntityPlayer entityplayer = args.length > 1 ? CommandBase.getPlayer(server, sender, args[1]) : CommandBase.getCommandSenderAsPlayer(sender);

            if (flag)
            {
                sender.setCommandStat(CommandResultStats.Type.QUERY_RESULT, entityplayer.experienceLevel);

                if (flag1)
                {
                    entityplayer.addExperienceLevel(-i);
                    CommandBase.notifyCommandListener(sender, this, "commands.xp.success.negative.levels", i, entityplayer.getName());
                }
                else
                {
                    entityplayer.addExperienceLevel(i);
                    CommandBase.notifyCommandListener(sender, this, "commands.xp.success.levels", i, entityplayer.getName());
                }
            }
            else
            {
                sender.setCommandStat(CommandResultStats.Type.QUERY_RESULT, entityplayer.experienceTotal);

                if (flag1)
                {
                    throw new CommandException("commands.xp.failure.widthdrawXp");
                }

                entityplayer.addExperience(i);
                CommandBase.notifyCommandListener(sender, this, "commands.xp.success", i, entityplayer.getName());
            }
        }
    }

    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos)
    {
        return args.length == 2 ? CommandBase.getListOfStringsMatchingLastWord(args, server.getAllUsernames()) : Collections.emptyList();
    }

    /**
     * Return whether the specified command parameter index is a username parameter.
     */
    public boolean isUsernameIndex(String[] args, int index)
    {
        return index == 1;
    }
}
