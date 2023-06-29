package net.minecraft.command.server;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.network.play.server.SPacketSpawnPosition;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class CommandSetDefaultSpawnpoint extends CommandBase
{
    /**
     * Gets the name of the command
     */
    public String getCommandName()
    {
        return "setworldspawn";
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
        return "commands.setworldspawn.usage";
    }

    /**
     * Callback for when the command is executed
     */
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        BlockPos blockpos;

        if (args.length == 0)
        {
            blockpos = CommandBase.getCommandSenderAsPlayer(sender).getPosition();
        }
        else
        {
            if (args.length != 3 || sender.getEntityWorld() == null)
            {
                throw new WrongUsageException("commands.setworldspawn.usage");
            }

            blockpos = CommandBase.parseBlockPos(sender, args, 0, true);
        }

        sender.getEntityWorld().setSpawnPoint(blockpos);
        server.getPlayerList().sendPacketToAllPlayers(new SPacketSpawnPosition(blockpos));
        CommandBase.notifyCommandListener(sender, this, "commands.setworldspawn.success", blockpos.getX(), blockpos.getY(), blockpos.getZ());
    }

    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos)
    {
        return args.length > 0 && args.length <= 3 ? CommandBase.getTabCompletionCoordinate(args, 0, pos) : Collections.emptyList();
    }
}
