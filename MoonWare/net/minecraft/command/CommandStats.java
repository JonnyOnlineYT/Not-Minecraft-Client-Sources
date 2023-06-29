package net.minecraft.command;

import com.google.common.collect.Lists;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CommandStats extends CommandBase
{
    /**
     * Gets the name of the command
     */
    public String getCommandName()
    {
        return "stats";
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
        return "commands.stats.usage";
    }

    /**
     * Callback for when the command is executed
     */
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        if (args.length < 1)
        {
            throw new WrongUsageException("commands.stats.usage");
        }
        else
        {
            boolean flag;

            if ("entity".equals(args[0]))
            {
                flag = false;
            }
            else
            {
                if (!"block".equals(args[0]))
                {
                    throw new WrongUsageException("commands.stats.usage");
                }

                flag = true;
            }

            int i;

            if (flag)
            {
                if (args.length < 5)
                {
                    throw new WrongUsageException("commands.stats.block.usage");
                }

                i = 4;
            }
            else
            {
                if (args.length < 3)
                {
                    throw new WrongUsageException("commands.stats.entity.usage");
                }

                i = 2;
            }

            String s = args[i++];

            if ("set".equals(s))
            {
                if (args.length < i + 3)
                {
                    if (i == 5)
                    {
                        throw new WrongUsageException("commands.stats.block.set.usage");
                    }

                    throw new WrongUsageException("commands.stats.entity.set.usage");
                }
            }
            else
            {
                if (!"clear".equals(s))
                {
                    throw new WrongUsageException("commands.stats.usage");
                }

                if (args.length < i + 1)
                {
                    if (i == 5)
                    {
                        throw new WrongUsageException("commands.stats.block.clear.usage");
                    }

                    throw new WrongUsageException("commands.stats.entity.clear.usage");
                }
            }

            CommandResultStats.Type commandresultstats$type = CommandResultStats.Type.getTypeByName(args[i++]);

            if (commandresultstats$type == null)
            {
                throw new CommandException("commands.stats.failed");
            }
            else
            {
                World world = sender.getEntityWorld();
                CommandResultStats commandresultstats;

                if (flag)
                {
                    BlockPos blockpos = CommandBase.parseBlockPos(sender, args, 1, false);
                    TileEntity tileentity = world.getTileEntity(blockpos);

                    if (tileentity == null)
                    {
                        throw new CommandException("commands.stats.noCompatibleBlock", blockpos.getX(), blockpos.getY(), blockpos.getZ());
                    }

                    if (tileentity instanceof TileEntityCommandBlock)
                    {
                        commandresultstats = ((TileEntityCommandBlock)tileentity).getCommandResultStats();
                    }
                    else
                    {
                        if (!(tileentity instanceof TileEntitySign))
                        {
                            throw new CommandException("commands.stats.noCompatibleBlock", blockpos.getX(), blockpos.getY(), blockpos.getZ());
                        }

                        commandresultstats = ((TileEntitySign)tileentity).getStats();
                    }
                }
                else
                {
                    Entity entity = CommandBase.getEntity(server, sender, args[1]);
                    commandresultstats = entity.getCommandStats();
                }

                if ("set".equals(s))
                {
                    String s1 = args[i++];
                    String s2 = args[i];

                    if (s1.isEmpty() || s2.isEmpty())
                    {
                        throw new CommandException("commands.stats.failed");
                    }

                    CommandResultStats.setScoreBoardStat(commandresultstats, commandresultstats$type, s1, s2);
                    CommandBase.notifyCommandListener(sender, this, "commands.stats.success", commandresultstats$type.getTypeName(), s2, s1);
                }
                else if ("clear".equals(s))
                {
                    CommandResultStats.setScoreBoardStat(commandresultstats, commandresultstats$type, null, null);
                    CommandBase.notifyCommandListener(sender, this, "commands.stats.cleared", commandresultstats$type.getTypeName());
                }

                if (flag)
                {
                    BlockPos blockpos1 = CommandBase.parseBlockPos(sender, args, 1, false);
                    TileEntity tileentity1 = world.getTileEntity(blockpos1);
                    tileentity1.markDirty();
                }
            }
        }
    }

    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos)
    {
        if (args.length == 1)
        {
            return CommandBase.getListOfStringsMatchingLastWord(args, "entity", "block");
        }
        else if (args.length == 2 && "entity".equals(args[0]))
        {
            return CommandBase.getListOfStringsMatchingLastWord(args, server.getAllUsernames());
        }
        else if (args.length >= 2 && args.length <= 4 && "block".equals(args[0]))
        {
            return CommandBase.getTabCompletionCoordinate(args, 1, pos);
        }
        else if ((args.length != 3 || !"entity".equals(args[0])) && (args.length != 5 || !"block".equals(args[0])))
        {
            if ((args.length != 4 || !"entity".equals(args[0])) && (args.length != 6 || !"block".equals(args[0])))
            {
                return (args.length != 6 || !"entity".equals(args[0])) && (args.length != 8 || !"block".equals(args[0])) ? Collections.emptyList() : CommandBase.getListOfStringsMatchingLastWord(args, getObjectiveNames(server));
            }
            else
            {
                return CommandBase.getListOfStringsMatchingLastWord(args, CommandResultStats.Type.getTypeNames());
            }
        }
        else
        {
            return CommandBase.getListOfStringsMatchingLastWord(args, "set", "clear");
        }
    }

    protected List<String> getObjectiveNames(MinecraftServer server)
    {
        Collection<ScoreObjective> collection = server.worldServerForDimension(0).getScoreboard().getScoreObjectives();
        List<String> list = Lists.newArrayList();

        for (ScoreObjective scoreobjective : collection)
        {
            if (!scoreobjective.getCriteria().isReadOnly())
            {
                list.add(scoreobjective.getName());
            }
        }

        return list;
    }

    /**
     * Return whether the specified command parameter index is a username parameter.
     */
    public boolean isUsernameIndex(String[] args, int index)
    {
        return args.length > 0 && "entity".equals(args[0]) && index == 1;
    }
}
