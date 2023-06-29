package net.minecraft.command;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.CommandBlockBaseLogic;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TranslatableComponent;
import net.minecraft.util.text.Formatting;
import net.minecraft.util.text.event.ClickEvent;

public class CommandHelp extends CommandBase
{
    private static final String[] seargeSays = {"Yolo", "Ask for help on twitter", "/deop @p", "Scoreboard deleted, commands blocked", "Contact helpdesk for help", "/testfornoob @p", "/trigger warning", "Oh my god, it's full of stats", "/kill @p[name=!Searge]", "Have you tried turning it off and on again?", "Sorry, no help today"};
    private final Random rand = new Random();

    /**
     * Gets the name of the command
     */
    public String getCommandName()
    {
        return "help";
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
        return "commands.help.usage";
    }

    public List<String> getCommandAliases()
    {
        return Arrays.asList("?");
    }

    /**
     * Callback for when the command is executed
     */
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        if (sender instanceof CommandBlockBaseLogic)
        {
            sender.addChatMessage((new TextComponent("Searge says: ")).append(seargeSays[rand.nextInt(seargeSays.length) % seargeSays.length]));
        }
        else
        {
            List<ICommand> list = getSortedPossibleCommands(sender, server);
            int i = 7;
            int j = (list.size() - 1) / 7;
            int k = 0;

            try
            {
                k = args.length == 0 ? 0 : CommandBase.parseInt(args[0], 1, j + 1) - 1;
            }
            catch (NumberInvalidException numberinvalidexception)
            {
                Map<String, ICommand> map = getCommandMap(server);
                ICommand icommand = map.get(args[0]);

                if (icommand != null)
                {
                    throw new WrongUsageException(icommand.getCommandUsage(sender));
                }

                if (MathHelper.getInt(args[0], -1) == -1 && MathHelper.getInt(args[0], -2) == -2)
                {
                    throw new CommandNotFoundException();
                }

                throw numberinvalidexception;
            }

            int l = Math.min((k + 1) * 7, list.size());
            TranslatableComponent textcomponenttranslation1 = new TranslatableComponent("commands.help.header", k + 1, j + 1);
            textcomponenttranslation1.getStyle().setColor(Formatting.DARK_GREEN);
            sender.addChatMessage(textcomponenttranslation1);

            for (int i1 = k * 7; i1 < l; ++i1)
            {
                ICommand icommand1 = list.get(i1);
                TranslatableComponent textcomponenttranslation = new TranslatableComponent(icommand1.getCommandUsage(sender));
                textcomponenttranslation.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/" + icommand1.getCommandName() + " "));
                sender.addChatMessage(textcomponenttranslation);
            }

            if (k == 0)
            {
                TranslatableComponent textcomponenttranslation2 = new TranslatableComponent("commands.help.footer");
                textcomponenttranslation2.getStyle().setColor(Formatting.GREEN);
                sender.addChatMessage(textcomponenttranslation2);
            }
        }
    }

    protected List<ICommand> getSortedPossibleCommands(ICommandSender sender, MinecraftServer server)
    {
        List<ICommand> list = server.getCommandManager().getPossibleCommands(sender);
        Collections.sort(list);
        return list;
    }

    protected Map<String, ICommand> getCommandMap(MinecraftServer server)
    {
        return server.getCommandManager().getCommands();
    }

    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos)
    {
        if (args.length == 1)
        {
            Set<String> set = getCommandMap(server).keySet();
            return CommandBase.getListOfStringsMatchingLastWord(args, set.toArray(new String[set.size()]));
        }
        else
        {
            return Collections.emptyList();
        }
    }
}
