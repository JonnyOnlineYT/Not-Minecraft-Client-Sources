package net.minecraft.command;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class CommandSetSpawnpoint extends CommandBase
{
    /**
     * Gets the name of the command
     */
    public String getCommandName()
    {
        return "spawnpoint";
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
        return "commands.spawnpoint.usage";
    }

    /**
     * Callback for when the command is executed
     */
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        if (args.length > 1 && args.length < 4)
        {
            throw new WrongUsageException("commands.spawnpoint.usage");
        }
        else
        {
            EntityPlayerMP entityplayermp = args.length > 0 ? CommandBase.getPlayer(server, sender, args[0]) : CommandBase.getCommandSenderAsPlayer(sender);
            BlockPos blockpos = args.length > 3 ? CommandBase.parseBlockPos(sender, args, 1, true) : entityplayermp.getPosition();

            if (entityplayermp.world != null)
            {
                entityplayermp.setSpawnPoint(blockpos, true);
                CommandBase.notifyCommandListener(sender, this, "commands.spawnpoint.success", entityplayermp.getName(), blockpos.getX(), blockpos.getY(), blockpos.getZ());
            }
        }
    }

    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos)
    {
        if (args.length == 1)
        {
            return CommandBase.getListOfStringsMatchingLastWord(args, server.getAllUsernames());
        }
        else
        {
            return args.length > 1 && args.length <= 4 ? CommandBase.getTabCompletionCoordinate(args, 1, pos) : Collections.emptyList();
        }
    }

    /**
     * Return whether the specified command parameter index is a username parameter.
     */
    public boolean isUsernameIndex(String[] args, int index)
    {
        return index == 0;
    }
}
