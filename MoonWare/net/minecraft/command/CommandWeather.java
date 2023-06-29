package net.minecraft.command;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldInfo;

public class CommandWeather extends CommandBase
{
    /**
     * Gets the name of the command
     */
    public String getCommandName()
    {
        return "weather";
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
        return "commands.weather.usage";
    }

    /**
     * Callback for when the command is executed
     */
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        if (args.length >= 1 && args.length <= 2)
        {
            int i = (300 + (new Random()).nextInt(600)) * 20;

            if (args.length >= 2)
            {
                i = CommandBase.parseInt(args[1], 1, 1000000) * 20;
            }

            World world = server.worldServers[0];
            WorldInfo worldinfo = world.getWorldInfo();

            if ("clear".equalsIgnoreCase(args[0]))
            {
                worldinfo.setCleanWeatherTime(i);
                worldinfo.setRainTime(0);
                worldinfo.setThunderTime(0);
                worldinfo.setRaining(false);
                worldinfo.setThundering(false);
                CommandBase.notifyCommandListener(sender, this, "commands.weather.clear");
            }
            else if ("rain".equalsIgnoreCase(args[0]))
            {
                worldinfo.setCleanWeatherTime(0);
                worldinfo.setRainTime(i);
                worldinfo.setThunderTime(i);
                worldinfo.setRaining(true);
                worldinfo.setThundering(false);
                CommandBase.notifyCommandListener(sender, this, "commands.weather.rain");
            }
            else
            {
                if (!"thunder".equalsIgnoreCase(args[0]))
                {
                    throw new WrongUsageException("commands.weather.usage");
                }

                worldinfo.setCleanWeatherTime(0);
                worldinfo.setRainTime(i);
                worldinfo.setThunderTime(i);
                worldinfo.setRaining(true);
                worldinfo.setThundering(true);
                CommandBase.notifyCommandListener(sender, this, "commands.weather.thunder");
            }
        }
        else
        {
            throw new WrongUsageException("commands.weather.usage");
        }
    }

    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos)
    {
        return args.length == 1 ? CommandBase.getListOfStringsMatchingLastWord(args, "clear", "rain", "thunder") : Collections.emptyList();
    }
}
