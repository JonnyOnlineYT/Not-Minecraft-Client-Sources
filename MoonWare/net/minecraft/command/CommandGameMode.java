package net.minecraft.command;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Component;
import net.minecraft.util.text.TranslatableComponent;
import net.minecraft.world.GameType;
import net.minecraft.world.WorldSettings;

public class CommandGameMode extends CommandBase
{
    /**
     * Gets the name of the command
     */
    public String getCommandName()
    {
        return "gamemode";
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
        return "commands.gamemode.usage";
    }

    /**
     * Callback for when the command is executed
     */
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        if (args.length <= 0)
        {
            throw new WrongUsageException("commands.gamemode.usage");
        }
        else
        {
            GameType gametype = getGameModeFromCommand(sender, args[0]);
            EntityPlayer entityplayer = args.length >= 2 ? CommandBase.getPlayer(server, sender, args[1]) : CommandBase.getCommandSenderAsPlayer(sender);
            entityplayer.setGameType(gametype);
            Component itextcomponent = new TranslatableComponent("gameMode." + gametype.getName());

            if (sender.getEntityWorld().getGameRules().getBoolean("sendCommandFeedback"))
            {
                entityplayer.addChatMessage(new TranslatableComponent("gameMode.changed", itextcomponent));
            }

            if (entityplayer == sender)
            {
                CommandBase.notifyCommandListener(sender, this, 1, "commands.gamemode.success.self", itextcomponent);
            }
            else
            {
                CommandBase.notifyCommandListener(sender, this, 1, "commands.gamemode.success.other", entityplayer.getName(), itextcomponent);
            }
        }
    }

    /**
     * Gets the Game Mode specified in the command.
     */
    protected GameType getGameModeFromCommand(ICommandSender sender, String gameModeString) throws CommandException {
        GameType gametype = GameType.parseGameTypeWithDefault(gameModeString, GameType.NOT_SET);
        return gametype == GameType.NOT_SET ? WorldSettings.getGameTypeById(CommandBase.parseInt(gameModeString, 0, GameType.values().length - 2)) : gametype;
    }

    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos)
    {
        if (args.length == 1)
        {
            return CommandBase.getListOfStringsMatchingLastWord(args, "survival", "creative", "adventure", "spectator");
        }
        else
        {
            return args.length == 2 ? CommandBase.getListOfStringsMatchingLastWord(args, server.getAllUsernames()) : Collections.emptyList();
        }
    }

    /**
     * Return whether the specified command parameter index is a username parameter.
     */
    public boolean isUsernameIndex(String[] args, int index)
    {
        return index == 1;
    }
}
