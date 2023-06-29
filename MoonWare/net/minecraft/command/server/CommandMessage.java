package net.minecraft.command.server;

import java.util.Arrays;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Component;
import net.minecraft.util.text.TranslatableComponent;
import net.minecraft.util.text.Formatting;

public class CommandMessage extends CommandBase
{
    public List<String> getCommandAliases()
    {
        return Arrays.asList("w", "msg");
    }

    /**
     * Gets the name of the command
     */
    public String getCommandName()
    {
        return "tell";
    }

    /**
     * Return the required permission level for this command.
     */
    public int getRequiredPermissionLevel()
    {
        return 0;
    }

    /**
     * Gets the usage string for the command.
     */
    public String getCommandUsage(ICommandSender sender)
    {
        return "commands.message.usage";
    }

    /**
     * Callback for when the command is executed
     */
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        if (args.length < 2)
        {
            throw new WrongUsageException("commands.message.usage");
        }
        else
        {
            EntityPlayer entityplayer = CommandBase.getPlayer(server, sender, args[0]);

            if (entityplayer == sender)
            {
                throw new PlayerNotFoundException("commands.message.sameTarget");
            }
            else
            {
                Component itextcomponent = CommandBase.getChatComponentFromNthArg(sender, args, 1, !(sender instanceof EntityPlayer));
                TranslatableComponent textcomponenttranslation = new TranslatableComponent("commands.message.display.incoming", sender.getDisplayName(), itextcomponent.copy());
                TranslatableComponent textcomponenttranslation1 = new TranslatableComponent("commands.message.display.outgoing", entityplayer.getDisplayName(), itextcomponent.copy());
                textcomponenttranslation.getStyle().setColor(Formatting.GRAY).setItalic(Boolean.valueOf(true));
                textcomponenttranslation1.getStyle().setColor(Formatting.GRAY).setItalic(Boolean.valueOf(true));
                entityplayer.addChatMessage(textcomponenttranslation);
                sender.addChatMessage(textcomponenttranslation1);
            }
        }
    }

    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos)
    {
        return CommandBase.getListOfStringsMatchingLastWord(args, server.getAllUsernames());
    }

    /**
     * Return whether the specified command parameter index is a username parameter.
     */
    public boolean isUsernameIndex(String[] args, int index)
    {
        return index == 0;
    }
}
