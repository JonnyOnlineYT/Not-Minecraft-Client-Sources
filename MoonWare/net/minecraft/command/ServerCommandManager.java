package net.minecraft.command;

import net.minecraft.command.server.CommandBanIp;
import net.minecraft.command.server.CommandBanPlayer;
import net.minecraft.command.server.CommandBroadcast;
import net.minecraft.command.server.CommandDeOp;
import net.minecraft.command.server.CommandEmote;
import net.minecraft.command.server.CommandListBans;
import net.minecraft.command.server.CommandListPlayers;
import net.minecraft.command.server.CommandMessage;
import net.minecraft.command.server.CommandMessageRaw;
import net.minecraft.command.server.CommandOp;
import net.minecraft.command.server.CommandPardonIp;
import net.minecraft.command.server.CommandPardonPlayer;
import net.minecraft.command.server.CommandPublishLocalServer;
import net.minecraft.command.server.CommandSaveAll;
import net.minecraft.command.server.CommandSaveOff;
import net.minecraft.command.server.CommandSaveOn;
import net.minecraft.command.server.CommandScoreboard;
import net.minecraft.command.server.CommandSetBlock;
import net.minecraft.command.server.CommandSetDefaultSpawnpoint;
import net.minecraft.command.server.CommandStop;
import net.minecraft.command.server.CommandSummon;
import net.minecraft.command.server.CommandTeleport;
import net.minecraft.command.server.CommandTestFor;
import net.minecraft.command.server.CommandTestForBlock;
import net.minecraft.command.server.CommandWhitelist;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.rcon.RConConsoleSource;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.CommandBlockBaseLogic;
import net.minecraft.util.text.Component;
import net.minecraft.util.text.TranslatableComponent;
import net.minecraft.util.text.Formatting;

public class ServerCommandManager extends CommandHandler implements ICommandListener
{
    private final MinecraftServer server;

    public ServerCommandManager(MinecraftServer serverIn)
    {
        server = serverIn;
        registerCommand(new CommandTime());
        registerCommand(new CommandGameMode());
        registerCommand(new CommandDifficulty());
        registerCommand(new CommandDefaultGameMode());
        registerCommand(new CommandKill());
        registerCommand(new CommandToggleDownfall());
        registerCommand(new CommandWeather());
        registerCommand(new CommandXP());
        registerCommand(new CommandTP());
        registerCommand(new CommandTeleport());
        registerCommand(new CommandGive());
        registerCommand(new CommandReplaceItem());
        registerCommand(new CommandStats());
        registerCommand(new CommandEffect());
        registerCommand(new CommandEnchant());
        registerCommand(new CommandParticle());
        registerCommand(new CommandEmote());
        registerCommand(new CommandShowSeed());
        registerCommand(new CommandHelp());
        registerCommand(new CommandDebug());
        registerCommand(new CommandMessage());
        registerCommand(new CommandBroadcast());
        registerCommand(new CommandSetSpawnpoint());
        registerCommand(new CommandSetDefaultSpawnpoint());
        registerCommand(new CommandGameRule());
        registerCommand(new CommandClearInventory());
        registerCommand(new CommandTestFor());
        registerCommand(new CommandSpreadPlayers());
        registerCommand(new CommandPlaySound());
        registerCommand(new CommandScoreboard());
        registerCommand(new CommandExecuteAt());
        registerCommand(new CommandTrigger());
        registerCommand(new AdvancementCommand());
        registerCommand(new RecipeCommand());
        registerCommand(new CommandSummon());
        registerCommand(new CommandSetBlock());
        registerCommand(new CommandFill());
        registerCommand(new CommandClone());
        registerCommand(new CommandCompare());
        registerCommand(new CommandBlockData());
        registerCommand(new CommandTestForBlock());
        registerCommand(new CommandMessageRaw());
        registerCommand(new CommandWorldBorder());
        registerCommand(new CommandTitle());
        registerCommand(new CommandEntityData());
        registerCommand(new CommandStopSound());
        registerCommand(new CommandLocate());
        registerCommand(new CommandReload());
        registerCommand(new CommandFunction());

        if (serverIn.isDedicatedServer())
        {
            registerCommand(new CommandOp());
            registerCommand(new CommandDeOp());
            registerCommand(new CommandStop());
            registerCommand(new CommandSaveAll());
            registerCommand(new CommandSaveOff());
            registerCommand(new CommandSaveOn());
            registerCommand(new CommandBanIp());
            registerCommand(new CommandPardonIp());
            registerCommand(new CommandBanPlayer());
            registerCommand(new CommandListBans());
            registerCommand(new CommandPardonPlayer());
            registerCommand(new CommandServerKick());
            registerCommand(new CommandListPlayers());
            registerCommand(new CommandWhitelist());
            registerCommand(new CommandSetPlayerTimeout());
        }
        else
        {
            registerCommand(new CommandPublishLocalServer());
        }

        CommandBase.setCommandListener(this);
    }

    /**
     * Send an informative message to the server operators
     */
    public void notifyListener(ICommandSender sender, ICommand command, int flags, String translationKey, Object... translationArgs)
    {
        boolean flag = true;
        MinecraftServer minecraftserver = server;

        if (!sender.sendCommandFeedback())
        {
            flag = false;
        }

        Component itextcomponent = new TranslatableComponent("chat.type.admin", sender.getName(), new TranslatableComponent(translationKey, translationArgs));
        itextcomponent.getStyle().setColor(Formatting.GRAY);
        itextcomponent.getStyle().setItalic(Boolean.valueOf(true));

        if (flag)
        {
            for (EntityPlayer entityplayer : minecraftserver.getPlayerList().getPlayerList())
            {
                if (entityplayer != sender && minecraftserver.getPlayerList().canSendCommands(entityplayer.getGameProfile()) && command.checkPermission(server, sender))
                {
                    boolean flag1 = sender instanceof MinecraftServer && server.shouldBroadcastConsoleToOps();
                    boolean flag2 = sender instanceof RConConsoleSource && server.shouldBroadcastRconToOps();

                    if (flag1 || flag2 || !(sender instanceof RConConsoleSource) && !(sender instanceof MinecraftServer))
                    {
                        entityplayer.addChatMessage(itextcomponent);
                    }
                }
            }
        }

        if (sender != minecraftserver && minecraftserver.worldServers[0].getGameRules().getBoolean("logAdminCommands"))
        {
            minecraftserver.addChatMessage(itextcomponent);
        }

        boolean flag3 = minecraftserver.worldServers[0].getGameRules().getBoolean("sendCommandFeedback");

        if (sender instanceof CommandBlockBaseLogic)
        {
            flag3 = ((CommandBlockBaseLogic)sender).shouldTrackOutput();
        }

        if ((flags & 1) != 1 && flag3 || sender instanceof MinecraftServer)
        {
            sender.addChatMessage(new TranslatableComponent(translationKey, translationArgs));
        }
    }

    protected MinecraftServer getServer()
    {
        return server;
    }
}
