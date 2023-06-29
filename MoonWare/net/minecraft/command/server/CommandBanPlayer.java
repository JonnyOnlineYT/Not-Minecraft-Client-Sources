package net.minecraft.command.server;

import com.mojang.authlib.GameProfile;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.UserListBansEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslatableComponent;

public class CommandBanPlayer extends CommandBase
{
    /**
     * Gets the name of the command
     */
    public String getCommandName()
    {
        return "ban";
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
        return "commands.ban.usage";
    }

    /**
     * Check if the given ICommandSender has permission to execute this command
     */
    public boolean checkPermission(MinecraftServer server, ICommandSender sender)
    {
        return server.getPlayerList().getBannedPlayers().isLanServer() && super.checkPermission(server, sender);
    }

    /**
     * Callback for when the command is executed
     */
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        if (args.length >= 1 && args[0].length() > 0)
        {
            GameProfile gameprofile = server.getPlayerProfileCache().getGameProfileForUsername(args[0]);

            if (gameprofile == null)
            {
                throw new CommandException("commands.ban.failed", args[0]);
            }
            else
            {
                String s = null;

                if (args.length >= 2)
                {
                    s = CommandBase.getChatComponentFromNthArg(sender, args, 1).asString();
                }

                UserListBansEntry userlistbansentry = new UserListBansEntry(gameprofile, null, sender.getName(), null, s);
                server.getPlayerList().getBannedPlayers().addEntry(userlistbansentry);
                EntityPlayerMP entityplayermp = server.getPlayerList().getPlayerByUsername(args[0]);

                if (entityplayermp != null)
                {
                    entityplayermp.connection.func_194028_b(new TranslatableComponent("multiplayer.disconnect.banned"));
                }

                CommandBase.notifyCommandListener(sender, this, "commands.ban.success", args[0]);
            }
        }
        else
        {
            throw new WrongUsageException("commands.ban.usage");
        }
    }

    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos)
    {
        return args.length >= 1 ? CommandBase.getListOfStringsMatchingLastWord(args, server.getAllUsernames()) : Collections.emptyList();
    }
}
