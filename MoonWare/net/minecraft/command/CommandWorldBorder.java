package net.minecraft.command;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TranslatableComponent;
import net.minecraft.world.border.WorldBorder;

public class CommandWorldBorder extends CommandBase
{
    /**
     * Gets the name of the command
     */
    public String getCommandName()
    {
        return "worldborder";
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
        return "commands.worldborder.usage";
    }

    /**
     * Callback for when the command is executed
     */
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        if (args.length < 1)
        {
            throw new WrongUsageException("commands.worldborder.usage");
        }
        else
        {
            WorldBorder worldborder = getWorldBorder(server);

            if ("set".equals(args[0]))
            {
                if (args.length != 2 && args.length != 3)
                {
                    throw new WrongUsageException("commands.worldborder.set.usage");
                }

                double d0 = worldborder.getTargetSize();
                double d2 = CommandBase.parseDouble(args[1], 1.0D, 6.0E7D);
                long i = args.length > 2 ? CommandBase.parseLong(args[2], 0L, 9223372036854775L) * 1000L : 0L;

                if (i > 0L)
                {
                    worldborder.setTransition(d0, d2, i);

                    if (d0 > d2)
                    {
                        CommandBase.notifyCommandListener(sender, this, "commands.worldborder.setSlowly.shrink.success", String.format("%.1f", d2), String.format("%.1f", d0), Long.toString(i / 1000L));
                    }
                    else
                    {
                        CommandBase.notifyCommandListener(sender, this, "commands.worldborder.setSlowly.grow.success", String.format("%.1f", d2), String.format("%.1f", d0), Long.toString(i / 1000L));
                    }
                }
                else
                {
                    worldborder.setTransition(d2);
                    CommandBase.notifyCommandListener(sender, this, "commands.worldborder.set.success", String.format("%.1f", d2), String.format("%.1f", d0));
                }
            }
            else if ("add".equals(args[0]))
            {
                if (args.length != 2 && args.length != 3)
                {
                    throw new WrongUsageException("commands.worldborder.add.usage");
                }

                double d4 = worldborder.getDiameter();
                double d8 = d4 + CommandBase.parseDouble(args[1], -d4, 6.0E7D - d4);
                long j1 = worldborder.getTimeUntilTarget() + (args.length > 2 ? CommandBase.parseLong(args[2], 0L, 9223372036854775L) * 1000L : 0L);

                if (j1 > 0L)
                {
                    worldborder.setTransition(d4, d8, j1);

                    if (d4 > d8)
                    {
                        CommandBase.notifyCommandListener(sender, this, "commands.worldborder.setSlowly.shrink.success", String.format("%.1f", d8), String.format("%.1f", d4), Long.toString(j1 / 1000L));
                    }
                    else
                    {
                        CommandBase.notifyCommandListener(sender, this, "commands.worldborder.setSlowly.grow.success", String.format("%.1f", d8), String.format("%.1f", d4), Long.toString(j1 / 1000L));
                    }
                }
                else
                {
                    worldborder.setTransition(d8);
                    CommandBase.notifyCommandListener(sender, this, "commands.worldborder.set.success", String.format("%.1f", d8), String.format("%.1f", d4));
                }
            }
            else if ("center".equals(args[0]))
            {
                if (args.length != 3)
                {
                    throw new WrongUsageException("commands.worldborder.center.usage");
                }

                BlockPos blockpos = sender.getPosition();
                double d1 = CommandBase.parseDouble((double)blockpos.getX() + 0.5D, args[1], true);
                double d3 = CommandBase.parseDouble((double)blockpos.getZ() + 0.5D, args[2], true);
                worldborder.setCenter(d1, d3);
                CommandBase.notifyCommandListener(sender, this, "commands.worldborder.center.success", d1, d3);
            }
            else if ("damage".equals(args[0]))
            {
                if (args.length < 2)
                {
                    throw new WrongUsageException("commands.worldborder.damage.usage");
                }

                if ("buffer".equals(args[1]))
                {
                    if (args.length != 3)
                    {
                        throw new WrongUsageException("commands.worldborder.damage.buffer.usage");
                    }

                    double d5 = CommandBase.parseDouble(args[2], 0.0D);
                    double d9 = worldborder.getDamageBuffer();
                    worldborder.setDamageBuffer(d5);
                    CommandBase.notifyCommandListener(sender, this, "commands.worldborder.damage.buffer.success", String.format("%.1f", d5), String.format("%.1f", d9));
                }
                else if ("amount".equals(args[1]))
                {
                    if (args.length != 3)
                    {
                        throw new WrongUsageException("commands.worldborder.damage.amount.usage");
                    }

                    double d6 = CommandBase.parseDouble(args[2], 0.0D);
                    double d10 = worldborder.getDamageAmount();
                    worldborder.setDamageAmount(d6);
                    CommandBase.notifyCommandListener(sender, this, "commands.worldborder.damage.amount.success", String.format("%.2f", d6), String.format("%.2f", d10));
                }
            }
            else if ("warning".equals(args[0]))
            {
                if (args.length < 2)
                {
                    throw new WrongUsageException("commands.worldborder.warning.usage");
                }

                if ("time".equals(args[1]))
                {
                    if (args.length != 3)
                    {
                        throw new WrongUsageException("commands.worldborder.warning.time.usage");
                    }

                    int j = CommandBase.parseInt(args[2], 0);
                    int l = worldborder.getWarningTime();
                    worldborder.setWarningTime(j);
                    CommandBase.notifyCommandListener(sender, this, "commands.worldborder.warning.time.success", j, l);
                }
                else if ("distance".equals(args[1]))
                {
                    if (args.length != 3)
                    {
                        throw new WrongUsageException("commands.worldborder.warning.distance.usage");
                    }

                    int k = CommandBase.parseInt(args[2], 0);
                    int i1 = worldborder.getWarningDistance();
                    worldborder.setWarningDistance(k);
                    CommandBase.notifyCommandListener(sender, this, "commands.worldborder.warning.distance.success", k, i1);
                }
            }
            else
            {
                if (!"get".equals(args[0]))
                {
                    throw new WrongUsageException("commands.worldborder.usage");
                }

                double d7 = worldborder.getDiameter();
                sender.setCommandStat(CommandResultStats.Type.QUERY_RESULT, MathHelper.floor(d7 + 0.5D));
                sender.addChatMessage(new TranslatableComponent("commands.worldborder.get.success", String.format("%.0f", d7)));
            }
        }
    }

    protected WorldBorder getWorldBorder(MinecraftServer server)
    {
        return server.worldServers[0].getWorldBorder();
    }

    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos)
    {
        if (args.length == 1)
        {
            return CommandBase.getListOfStringsMatchingLastWord(args, "set", "center", "damage", "warning", "add", "get");
        }
        else if (args.length == 2 && "damage".equals(args[0]))
        {
            return CommandBase.getListOfStringsMatchingLastWord(args, "buffer", "amount");
        }
        else if (args.length >= 2 && args.length <= 3 && "center".equals(args[0]))
        {
            return CommandBase.getTabCompletionCoordinateXZ(args, 1, pos);
        }
        else
        {
            return args.length == 2 && "warning".equals(args[0]) ? CommandBase.getListOfStringsMatchingLastWord(args, "time", "distance") : Collections.emptyList();
        }
    }
}
