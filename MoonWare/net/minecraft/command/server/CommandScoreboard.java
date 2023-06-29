package net.minecraft.command.server;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.EntitySelector;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.SyntaxErrorException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.scoreboard.IScoreCriteria;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TranslatableComponent;
import net.minecraft.util.text.Formatting;

public class CommandScoreboard extends CommandBase
{
    /**
     * Gets the name of the command
     */
    public String getCommandName()
    {
        return "scoreboard";
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
        return "commands.scoreboard.usage";
    }

    /**
     * Callback for when the command is executed
     */
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        if (!handleUserWildcards(server, sender, args))
        {
            if (args.length < 1)
            {
                throw new WrongUsageException("commands.scoreboard.usage");
            }
            else
            {
                if ("objectives".equalsIgnoreCase(args[0]))
                {
                    if (args.length == 1)
                    {
                        throw new WrongUsageException("commands.scoreboard.objectives.usage");
                    }

                    if ("list".equalsIgnoreCase(args[1]))
                    {
                        listObjectives(sender, server);
                    }
                    else if ("add".equalsIgnoreCase(args[1]))
                    {
                        if (args.length < 4)
                        {
                            throw new WrongUsageException("commands.scoreboard.objectives.add.usage");
                        }

                        addObjective(sender, args, 2, server);
                    }
                    else if ("remove".equalsIgnoreCase(args[1]))
                    {
                        if (args.length != 3)
                        {
                            throw new WrongUsageException("commands.scoreboard.objectives.remove.usage");
                        }

                        removeObjective(sender, args[2], server);
                    }
                    else
                    {
                        if (!"setdisplay".equalsIgnoreCase(args[1]))
                        {
                            throw new WrongUsageException("commands.scoreboard.objectives.usage");
                        }

                        if (args.length != 3 && args.length != 4)
                        {
                            throw new WrongUsageException("commands.scoreboard.objectives.setdisplay.usage");
                        }

                        setDisplayObjective(sender, args, 2, server);
                    }
                }
                else if ("players".equalsIgnoreCase(args[0]))
                {
                    if (args.length == 1)
                    {
                        throw new WrongUsageException("commands.scoreboard.players.usage");
                    }

                    if ("list".equalsIgnoreCase(args[1]))
                    {
                        if (args.length > 3)
                        {
                            throw new WrongUsageException("commands.scoreboard.players.list.usage");
                        }

                        listPlayers(sender, args, 2, server);
                    }
                    else if ("add".equalsIgnoreCase(args[1]))
                    {
                        if (args.length < 5)
                        {
                            throw new WrongUsageException("commands.scoreboard.players.add.usage");
                        }

                        addPlayerScore(sender, args, 2, server);
                    }
                    else if ("remove".equalsIgnoreCase(args[1]))
                    {
                        if (args.length < 5)
                        {
                            throw new WrongUsageException("commands.scoreboard.players.remove.usage");
                        }

                        addPlayerScore(sender, args, 2, server);
                    }
                    else if ("set".equalsIgnoreCase(args[1]))
                    {
                        if (args.length < 5)
                        {
                            throw new WrongUsageException("commands.scoreboard.players.set.usage");
                        }

                        addPlayerScore(sender, args, 2, server);
                    }
                    else if ("reset".equalsIgnoreCase(args[1]))
                    {
                        if (args.length != 3 && args.length != 4)
                        {
                            throw new WrongUsageException("commands.scoreboard.players.reset.usage");
                        }

                        resetPlayerScore(sender, args, 2, server);
                    }
                    else if ("enable".equalsIgnoreCase(args[1]))
                    {
                        if (args.length != 4)
                        {
                            throw new WrongUsageException("commands.scoreboard.players.enable.usage");
                        }

                        enablePlayerTrigger(sender, args, 2, server);
                    }
                    else if ("test".equalsIgnoreCase(args[1]))
                    {
                        if (args.length != 5 && args.length != 6)
                        {
                            throw new WrongUsageException("commands.scoreboard.players.test.usage");
                        }

                        testPlayerScore(sender, args, 2, server);
                    }
                    else if ("operation".equalsIgnoreCase(args[1]))
                    {
                        if (args.length != 7)
                        {
                            throw new WrongUsageException("commands.scoreboard.players.operation.usage");
                        }

                        applyPlayerOperation(sender, args, 2, server);
                    }
                    else
                    {
                        if (!"tag".equalsIgnoreCase(args[1]))
                        {
                            throw new WrongUsageException("commands.scoreboard.players.usage");
                        }

                        if (args.length < 4)
                        {
                            throw new WrongUsageException("commands.scoreboard.players.tag.usage");
                        }

                        applyPlayerTag(server, sender, args, 2);
                    }
                }
                else
                {
                    if (!"teams".equalsIgnoreCase(args[0]))
                    {
                        throw new WrongUsageException("commands.scoreboard.usage");
                    }

                    if (args.length == 1)
                    {
                        throw new WrongUsageException("commands.scoreboard.teams.usage");
                    }

                    if ("list".equalsIgnoreCase(args[1]))
                    {
                        if (args.length > 3)
                        {
                            throw new WrongUsageException("commands.scoreboard.teams.list.usage");
                        }

                        listTeams(sender, args, 2, server);
                    }
                    else if ("add".equalsIgnoreCase(args[1]))
                    {
                        if (args.length < 3)
                        {
                            throw new WrongUsageException("commands.scoreboard.teams.add.usage");
                        }

                        addTeam(sender, args, 2, server);
                    }
                    else if ("remove".equalsIgnoreCase(args[1]))
                    {
                        if (args.length != 3)
                        {
                            throw new WrongUsageException("commands.scoreboard.teams.remove.usage");
                        }

                        removeTeam(sender, args, 2, server);
                    }
                    else if ("empty".equalsIgnoreCase(args[1]))
                    {
                        if (args.length != 3)
                        {
                            throw new WrongUsageException("commands.scoreboard.teams.empty.usage");
                        }

                        emptyTeam(sender, args, 2, server);
                    }
                    else if ("join".equalsIgnoreCase(args[1]))
                    {
                        if (args.length < 4 && (args.length != 3 || !(sender instanceof EntityPlayer)))
                        {
                            throw new WrongUsageException("commands.scoreboard.teams.join.usage");
                        }

                        joinTeam(sender, args, 2, server);
                    }
                    else if ("leave".equalsIgnoreCase(args[1]))
                    {
                        if (args.length < 3 && !(sender instanceof EntityPlayer))
                        {
                            throw new WrongUsageException("commands.scoreboard.teams.leave.usage");
                        }

                        leaveTeam(sender, args, 2, server);
                    }
                    else
                    {
                        if (!"option".equalsIgnoreCase(args[1]))
                        {
                            throw new WrongUsageException("commands.scoreboard.teams.usage");
                        }

                        if (args.length != 4 && args.length != 5)
                        {
                            throw new WrongUsageException("commands.scoreboard.teams.option.usage");
                        }

                        setTeamOption(sender, args, 2, server);
                    }
                }
            }
        }
    }

    private boolean handleUserWildcards(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        int i = -1;

        for (int j = 0; j < args.length; ++j)
        {
            if (isUsernameIndex(args, j) && "*".equals(args[j]))
            {
                if (i >= 0)
                {
                    throw new CommandException("commands.scoreboard.noMultiWildcard");
                }

                i = j;
            }
        }

        if (i < 0)
        {
            return false;
        }
        else
        {
            List<String> list1 = Lists.newArrayList(getScoreboard(server).getObjectiveNames());
            String s = args[i];
            List<String> list = Lists.newArrayList();

            for (String s1 : list1)
            {
                args[i] = s1;

                try
                {
                    execute(server, sender, args);
                    list.add(s1);
                }
                catch (CommandException commandexception)
                {
                    TranslatableComponent textcomponenttranslation = new TranslatableComponent(commandexception.getMessage(), commandexception.getErrorObjects());
                    textcomponenttranslation.getStyle().setColor(Formatting.RED);
                    sender.addChatMessage(textcomponenttranslation);
                }
            }

            args[i] = s;
            sender.setCommandStat(CommandResultStats.Type.AFFECTED_ENTITIES, list.size());

            if (list.isEmpty())
            {
                throw new WrongUsageException("commands.scoreboard.allMatchesFailed");
            }
            else
            {
                return true;
            }
        }
    }

    protected Scoreboard getScoreboard(MinecraftServer server)
    {
        return server.worldServerForDimension(0).getScoreboard();
    }

    protected ScoreObjective convertToObjective(String name, boolean forWrite, MinecraftServer server) throws CommandException
    {
        Scoreboard scoreboard = getScoreboard(server);
        ScoreObjective scoreobjective = scoreboard.getObjective(name);

        if (scoreobjective == null)
        {
            throw new CommandException("commands.scoreboard.objectiveNotFound", name);
        }
        else if (forWrite && scoreobjective.getCriteria().isReadOnly())
        {
            throw new CommandException("commands.scoreboard.objectiveReadOnly", name);
        }
        else
        {
            return scoreobjective;
        }
    }

    protected ScorePlayerTeam convertToTeam(String p_184915_1_, MinecraftServer server) throws CommandException
    {
        Scoreboard scoreboard = getScoreboard(server);
        ScorePlayerTeam scoreplayerteam = scoreboard.getTeam(p_184915_1_);

        if (scoreplayerteam == null)
        {
            throw new CommandException("commands.scoreboard.teamNotFound", p_184915_1_);
        }
        else
        {
            return scoreplayerteam;
        }
    }

    protected void addObjective(ICommandSender sender, String[] commandArgs, int argStartIndex, MinecraftServer server) throws CommandException
    {
        String s = commandArgs[argStartIndex++];
        String s1 = commandArgs[argStartIndex++];
        Scoreboard scoreboard = getScoreboard(server);
        IScoreCriteria iscorecriteria = IScoreCriteria.INSTANCES.get(s1);

        if (iscorecriteria == null)
        {
            throw new WrongUsageException("commands.scoreboard.objectives.add.wrongType", s1);
        }
        else if (scoreboard.getObjective(s) != null)
        {
            throw new CommandException("commands.scoreboard.objectives.add.alreadyExists", s);
        }
        else if (s.length() > 16)
        {
            throw new SyntaxErrorException("commands.scoreboard.objectives.add.tooLong", s, Integer.valueOf(16));
        }
        else if (s.isEmpty())
        {
            throw new WrongUsageException("commands.scoreboard.objectives.add.usage");
        }
        else
        {
            if (commandArgs.length > argStartIndex)
            {
                String s2 = CommandBase.getChatComponentFromNthArg(sender, commandArgs, argStartIndex).asString();

                if (s2.length() > 32)
                {
                    throw new SyntaxErrorException("commands.scoreboard.objectives.add.displayTooLong", s2, Integer.valueOf(32));
                }

                if (s2.isEmpty())
                {
                    scoreboard.addScoreObjective(s, iscorecriteria);
                }
                else
                {
                    scoreboard.addScoreObjective(s, iscorecriteria).setDisplayName(s2);
                }
            }
            else
            {
                scoreboard.addScoreObjective(s, iscorecriteria);
            }

            CommandBase.notifyCommandListener(sender, this, "commands.scoreboard.objectives.add.success", s);
        }
    }

    protected void addTeam(ICommandSender sender, String[] p_184910_2_, int p_184910_3_, MinecraftServer server) throws CommandException
    {
        String s = p_184910_2_[p_184910_3_++];
        Scoreboard scoreboard = getScoreboard(server);

        if (scoreboard.getTeam(s) != null)
        {
            throw new CommandException("commands.scoreboard.teams.add.alreadyExists", s);
        }
        else if (s.length() > 16)
        {
            throw new SyntaxErrorException("commands.scoreboard.teams.add.tooLong", s, Integer.valueOf(16));
        }
        else if (s.isEmpty())
        {
            throw new WrongUsageException("commands.scoreboard.teams.add.usage");
        }
        else
        {
            if (p_184910_2_.length > p_184910_3_)
            {
                String s1 = CommandBase.getChatComponentFromNthArg(sender, p_184910_2_, p_184910_3_).asString();

                if (s1.length() > 32)
                {
                    throw new SyntaxErrorException("commands.scoreboard.teams.add.displayTooLong", s1, Integer.valueOf(32));
                }

                if (s1.isEmpty())
                {
                    scoreboard.createTeam(s);
                }
                else
                {
                    scoreboard.createTeam(s).setTeamName(s1);
                }
            }
            else
            {
                scoreboard.createTeam(s);
            }

            CommandBase.notifyCommandListener(sender, this, "commands.scoreboard.teams.add.success", s);
        }
    }

    protected void setTeamOption(ICommandSender sender, String[] p_184923_2_, int p_184923_3_, MinecraftServer server) throws CommandException
    {
        ScorePlayerTeam scoreplayerteam = convertToTeam(p_184923_2_[p_184923_3_++], server);

        if (scoreplayerteam != null)
        {
            String s = p_184923_2_[p_184923_3_++].toLowerCase(Locale.ROOT);

            if (!"color".equalsIgnoreCase(s) && !"friendlyfire".equalsIgnoreCase(s) && !"seeFriendlyInvisibles".equalsIgnoreCase(s) && !"nametagVisibility".equalsIgnoreCase(s) && !"deathMessageVisibility".equalsIgnoreCase(s) && !"collisionRule".equalsIgnoreCase(s))
            {
                throw new WrongUsageException("commands.scoreboard.teams.option.usage");
            }
            else if (p_184923_2_.length == 4)
            {
                if ("color".equalsIgnoreCase(s))
                {
                    throw new WrongUsageException("commands.scoreboard.teams.option.noValue", s, CommandBase.joinNiceStringFromCollection(Formatting.getValidValues(true, false)));
                }
                else if (!"friendlyfire".equalsIgnoreCase(s) && !"seeFriendlyInvisibles".equalsIgnoreCase(s))
                {
                    if (!"nametagVisibility".equalsIgnoreCase(s) && !"deathMessageVisibility".equalsIgnoreCase(s))
                    {
                        if ("collisionRule".equalsIgnoreCase(s))
                        {
                            throw new WrongUsageException("commands.scoreboard.teams.option.noValue", s, CommandBase.joinNiceString(Team.CollisionRule.getNames()));
                        }
                        else
                        {
                            throw new WrongUsageException("commands.scoreboard.teams.option.usage");
                        }
                    }
                    else
                    {
                        throw new WrongUsageException("commands.scoreboard.teams.option.noValue", s, CommandBase.joinNiceString(Team.EnumVisible.getNames()));
                    }
                }
                else
                {
                    throw new WrongUsageException("commands.scoreboard.teams.option.noValue", s, CommandBase.joinNiceStringFromCollection(Arrays.asList("true", "false")));
                }
            }
            else
            {
                String s1 = p_184923_2_[p_184923_3_];

                if ("color".equalsIgnoreCase(s))
                {
                    Formatting textformatting = Formatting.getValueByName(s1);

                    if (textformatting == null || textformatting.isDecoration())
                    {
                        throw new WrongUsageException("commands.scoreboard.teams.option.noValue", s, CommandBase.joinNiceStringFromCollection(Formatting.getValidValues(true, false)));
                    }

                    scoreplayerteam.setChatFormat(textformatting);
                    scoreplayerteam.setNamePrefix(textformatting.toString());
                    scoreplayerteam.setNameSuffix(Formatting.RESET.toString());
                }
                else if ("friendlyfire".equalsIgnoreCase(s))
                {
                    if (!"true".equalsIgnoreCase(s1) && !"false".equalsIgnoreCase(s1))
                    {
                        throw new WrongUsageException("commands.scoreboard.teams.option.noValue", s, CommandBase.joinNiceStringFromCollection(Arrays.asList("true", "false")));
                    }

                    scoreplayerteam.setAllowFriendlyFire("true".equalsIgnoreCase(s1));
                }
                else if ("seeFriendlyInvisibles".equalsIgnoreCase(s))
                {
                    if (!"true".equalsIgnoreCase(s1) && !"false".equalsIgnoreCase(s1))
                    {
                        throw new WrongUsageException("commands.scoreboard.teams.option.noValue", s, CommandBase.joinNiceStringFromCollection(Arrays.asList("true", "false")));
                    }

                    scoreplayerteam.setSeeFriendlyInvisiblesEnabled("true".equalsIgnoreCase(s1));
                }
                else if ("nametagVisibility".equalsIgnoreCase(s))
                {
                    Team.EnumVisible team$enumvisible = Team.EnumVisible.getByName(s1);

                    if (team$enumvisible == null)
                    {
                        throw new WrongUsageException("commands.scoreboard.teams.option.noValue", s, CommandBase.joinNiceString(Team.EnumVisible.getNames()));
                    }

                    scoreplayerteam.setNameTagVisibility(team$enumvisible);
                }
                else if ("deathMessageVisibility".equalsIgnoreCase(s))
                {
                    Team.EnumVisible team$enumvisible1 = Team.EnumVisible.getByName(s1);

                    if (team$enumvisible1 == null)
                    {
                        throw new WrongUsageException("commands.scoreboard.teams.option.noValue", s, CommandBase.joinNiceString(Team.EnumVisible.getNames()));
                    }

                    scoreplayerteam.setDeathMessageVisibility(team$enumvisible1);
                }
                else if ("collisionRule".equalsIgnoreCase(s))
                {
                    Team.CollisionRule team$collisionrule = Team.CollisionRule.getByName(s1);

                    if (team$collisionrule == null)
                    {
                        throw new WrongUsageException("commands.scoreboard.teams.option.noValue", s, CommandBase.joinNiceString(Team.CollisionRule.getNames()));
                    }

                    scoreplayerteam.setCollisionRule(team$collisionrule);
                }

                CommandBase.notifyCommandListener(sender, this, "commands.scoreboard.teams.option.success", s, scoreplayerteam.getRegisteredName(), s1);
            }
        }
    }

    protected void removeTeam(ICommandSender sender, String[] p_184921_2_, int p_184921_3_, MinecraftServer server) throws CommandException
    {
        Scoreboard scoreboard = getScoreboard(server);
        ScorePlayerTeam scoreplayerteam = convertToTeam(p_184921_2_[p_184921_3_], server);

        if (scoreplayerteam != null)
        {
            scoreboard.removeTeam(scoreplayerteam);
            CommandBase.notifyCommandListener(sender, this, "commands.scoreboard.teams.remove.success", scoreplayerteam.getRegisteredName());
        }
    }

    protected void listTeams(ICommandSender sender, String[] p_184922_2_, int p_184922_3_, MinecraftServer server) throws CommandException
    {
        Scoreboard scoreboard = getScoreboard(server);

        if (p_184922_2_.length > p_184922_3_)
        {
            ScorePlayerTeam scoreplayerteam = convertToTeam(p_184922_2_[p_184922_3_], server);

            if (scoreplayerteam == null)
            {
                return;
            }

            Collection<String> collection = scoreplayerteam.getMembershipCollection();
            sender.setCommandStat(CommandResultStats.Type.QUERY_RESULT, collection.size());

            if (collection.isEmpty())
            {
                throw new CommandException("commands.scoreboard.teams.list.player.empty", scoreplayerteam.getRegisteredName());
            }

            TranslatableComponent textcomponenttranslation = new TranslatableComponent("commands.scoreboard.teams.list.player.count", collection.size(), scoreplayerteam.getRegisteredName());
            textcomponenttranslation.getStyle().setColor(Formatting.DARK_GREEN);
            sender.addChatMessage(textcomponenttranslation);
            sender.addChatMessage(new TextComponent(CommandBase.joinNiceString(collection.toArray())));
        }
        else
        {
            Collection<ScorePlayerTeam> collection1 = scoreboard.getTeams();
            sender.setCommandStat(CommandResultStats.Type.QUERY_RESULT, collection1.size());

            if (collection1.isEmpty())
            {
                throw new CommandException("commands.scoreboard.teams.list.empty");
            }

            TranslatableComponent textcomponenttranslation1 = new TranslatableComponent("commands.scoreboard.teams.list.count", collection1.size());
            textcomponenttranslation1.getStyle().setColor(Formatting.DARK_GREEN);
            sender.addChatMessage(textcomponenttranslation1);

            for (ScorePlayerTeam scoreplayerteam1 : collection1)
            {
                sender.addChatMessage(new TranslatableComponent("commands.scoreboard.teams.list.entry", scoreplayerteam1.getRegisteredName(), scoreplayerteam1.getTeamName(), scoreplayerteam1.getMembershipCollection().size()));
            }
        }
    }

    protected void joinTeam(ICommandSender sender, String[] p_184916_2_, int p_184916_3_, MinecraftServer server) throws CommandException
    {
        Scoreboard scoreboard = getScoreboard(server);
        String s = p_184916_2_[p_184916_3_++];
        Set<String> set = Sets.newHashSet();
        Set<String> set1 = Sets.newHashSet();

        if (sender instanceof EntityPlayer && p_184916_3_ == p_184916_2_.length)
        {
            String s4 = CommandBase.getCommandSenderAsPlayer(sender).getName();

            if (scoreboard.addPlayerToTeam(s4, s))
            {
                set.add(s4);
            }
            else
            {
                set1.add(s4);
            }
        }
        else
        {
            while (p_184916_3_ < p_184916_2_.length)
            {
                String s1 = p_184916_2_[p_184916_3_++];

                if (EntitySelector.hasArguments(s1))
                {
                    for (Entity entity : CommandBase.getEntityList(server, sender, s1))
                    {
                        String s3 = CommandBase.getEntityName(server, sender, entity.getCachedUniqueIdString());

                        if (scoreboard.addPlayerToTeam(s3, s))
                        {
                            set.add(s3);
                        }
                        else
                        {
                            set1.add(s3);
                        }
                    }
                }
                else
                {
                    String s2 = CommandBase.getEntityName(server, sender, s1);

                    if (scoreboard.addPlayerToTeam(s2, s))
                    {
                        set.add(s2);
                    }
                    else
                    {
                        set1.add(s2);
                    }
                }
            }
        }

        if (!set.isEmpty())
        {
            sender.setCommandStat(CommandResultStats.Type.AFFECTED_ENTITIES, set.size());
            CommandBase.notifyCommandListener(sender, this, "commands.scoreboard.teams.join.success", set.size(), s, CommandBase.joinNiceString(set.toArray(new String[set.size()])));
        }

        if (!set1.isEmpty())
        {
            throw new CommandException("commands.scoreboard.teams.join.failure", set1.size(), s, CommandBase.joinNiceString(set1.toArray(new String[set1.size()])));
        }
    }

    protected void leaveTeam(ICommandSender sender, String[] p_184911_2_, int p_184911_3_, MinecraftServer server) throws CommandException
    {
        Scoreboard scoreboard = getScoreboard(server);
        Set<String> set = Sets.newHashSet();
        Set<String> set1 = Sets.newHashSet();

        if (sender instanceof EntityPlayer && p_184911_3_ == p_184911_2_.length)
        {
            String s3 = CommandBase.getCommandSenderAsPlayer(sender).getName();

            if (scoreboard.removePlayerFromTeams(s3))
            {
                set.add(s3);
            }
            else
            {
                set1.add(s3);
            }
        }
        else
        {
            while (p_184911_3_ < p_184911_2_.length)
            {
                String s = p_184911_2_[p_184911_3_++];

                if (EntitySelector.hasArguments(s))
                {
                    for (Entity entity : CommandBase.getEntityList(server, sender, s))
                    {
                        String s2 = CommandBase.getEntityName(server, sender, entity.getCachedUniqueIdString());

                        if (scoreboard.removePlayerFromTeams(s2))
                        {
                            set.add(s2);
                        }
                        else
                        {
                            set1.add(s2);
                        }
                    }
                }
                else
                {
                    String s1 = CommandBase.getEntityName(server, sender, s);

                    if (scoreboard.removePlayerFromTeams(s1))
                    {
                        set.add(s1);
                    }
                    else
                    {
                        set1.add(s1);
                    }
                }
            }
        }

        if (!set.isEmpty())
        {
            sender.setCommandStat(CommandResultStats.Type.AFFECTED_ENTITIES, set.size());
            CommandBase.notifyCommandListener(sender, this, "commands.scoreboard.teams.leave.success", set.size(), CommandBase.joinNiceString(set.toArray(new String[set.size()])));
        }

        if (!set1.isEmpty())
        {
            throw new CommandException("commands.scoreboard.teams.leave.failure", set1.size(), CommandBase.joinNiceString(set1.toArray(new String[set1.size()])));
        }
    }

    protected void emptyTeam(ICommandSender sender, String[] p_184917_2_, int p_184917_3_, MinecraftServer server) throws CommandException
    {
        Scoreboard scoreboard = getScoreboard(server);
        ScorePlayerTeam scoreplayerteam = convertToTeam(p_184917_2_[p_184917_3_], server);

        if (scoreplayerteam != null)
        {
            Collection<String> collection = Lists.newArrayList(scoreplayerteam.getMembershipCollection());
            sender.setCommandStat(CommandResultStats.Type.AFFECTED_ENTITIES, collection.size());

            if (collection.isEmpty())
            {
                throw new CommandException("commands.scoreboard.teams.empty.alreadyEmpty", scoreplayerteam.getRegisteredName());
            }
            else
            {
                for (String s : collection)
                {
                    scoreboard.removePlayerFromTeam(s, scoreplayerteam);
                }

                CommandBase.notifyCommandListener(sender, this, "commands.scoreboard.teams.empty.success", collection.size(), scoreplayerteam.getRegisteredName());
            }
        }
    }

    protected void removeObjective(ICommandSender sender, String name, MinecraftServer server) throws CommandException
    {
        Scoreboard scoreboard = getScoreboard(server);
        ScoreObjective scoreobjective = convertToObjective(name, false, server);
        scoreboard.removeObjective(scoreobjective);
        CommandBase.notifyCommandListener(sender, this, "commands.scoreboard.objectives.remove.success", name);
    }

    protected void listObjectives(ICommandSender sender, MinecraftServer server) throws CommandException
    {
        Scoreboard scoreboard = getScoreboard(server);
        Collection<ScoreObjective> collection = scoreboard.getScoreObjectives();

        if (collection.isEmpty())
        {
            throw new CommandException("commands.scoreboard.objectives.list.empty");
        }
        else
        {
            TranslatableComponent textcomponenttranslation = new TranslatableComponent("commands.scoreboard.objectives.list.count", collection.size());
            textcomponenttranslation.getStyle().setColor(Formatting.DARK_GREEN);
            sender.addChatMessage(textcomponenttranslation);

            for (ScoreObjective scoreobjective : collection)
            {
                sender.addChatMessage(new TranslatableComponent("commands.scoreboard.objectives.list.entry", scoreobjective.getName(), scoreobjective.getDisplayName(), scoreobjective.getCriteria().getName()));
            }
        }
    }

    protected void setDisplayObjective(ICommandSender sender, String[] p_184919_2_, int p_184919_3_, MinecraftServer server) throws CommandException
    {
        Scoreboard scoreboard = getScoreboard(server);
        String s = p_184919_2_[p_184919_3_++];
        int i = Scoreboard.getObjectiveDisplaySlotNumber(s);
        ScoreObjective scoreobjective = null;

        if (p_184919_2_.length == 4)
        {
            scoreobjective = convertToObjective(p_184919_2_[p_184919_3_], false, server);
        }

        if (i < 0)
        {
            throw new CommandException("commands.scoreboard.objectives.setdisplay.invalidSlot", s);
        }
        else
        {
            scoreboard.setObjectiveInDisplaySlot(i, scoreobjective);

            if (scoreobjective != null)
            {
                CommandBase.notifyCommandListener(sender, this, "commands.scoreboard.objectives.setdisplay.successSet", Scoreboard.getObjectiveDisplaySlot(i), scoreobjective.getName());
            }
            else
            {
                CommandBase.notifyCommandListener(sender, this, "commands.scoreboard.objectives.setdisplay.successCleared", Scoreboard.getObjectiveDisplaySlot(i));
            }
        }
    }

    protected void listPlayers(ICommandSender sender, String[] p_184920_2_, int p_184920_3_, MinecraftServer server) throws CommandException
    {
        Scoreboard scoreboard = getScoreboard(server);

        if (p_184920_2_.length > p_184920_3_)
        {
            String s = CommandBase.getEntityName(server, sender, p_184920_2_[p_184920_3_]);
            Map<ScoreObjective, Score> map = scoreboard.getObjectivesForEntity(s);
            sender.setCommandStat(CommandResultStats.Type.QUERY_RESULT, map.size());

            if (map.isEmpty())
            {
                throw new CommandException("commands.scoreboard.players.list.player.empty", s);
            }

            TranslatableComponent textcomponenttranslation = new TranslatableComponent("commands.scoreboard.players.list.player.count", map.size(), s);
            textcomponenttranslation.getStyle().setColor(Formatting.DARK_GREEN);
            sender.addChatMessage(textcomponenttranslation);

            for (Score score : map.values())
            {
                sender.addChatMessage(new TranslatableComponent("commands.scoreboard.players.list.player.entry", score.getScorePoints(), score.getObjective().getDisplayName(), score.getObjective().getName()));
            }
        }
        else
        {
            Collection<String> collection = scoreboard.getObjectiveNames();
            sender.setCommandStat(CommandResultStats.Type.QUERY_RESULT, collection.size());

            if (collection.isEmpty())
            {
                throw new CommandException("commands.scoreboard.players.list.empty");
            }

            TranslatableComponent textcomponenttranslation1 = new TranslatableComponent("commands.scoreboard.players.list.count", collection.size());
            textcomponenttranslation1.getStyle().setColor(Formatting.DARK_GREEN);
            sender.addChatMessage(textcomponenttranslation1);
            sender.addChatMessage(new TextComponent(CommandBase.joinNiceString(collection.toArray())));
        }
    }

    protected void addPlayerScore(ICommandSender sender, String[] p_184918_2_, int p_184918_3_, MinecraftServer server) throws CommandException
    {
        String s = p_184918_2_[p_184918_3_ - 1];
        int i = p_184918_3_;
        String s1 = CommandBase.getEntityName(server, sender, p_184918_2_[p_184918_3_++]);

        if (s1.length() > 40)
        {
            throw new SyntaxErrorException("commands.scoreboard.players.name.tooLong", s1, Integer.valueOf(40));
        }
        else
        {
            ScoreObjective scoreobjective = convertToObjective(p_184918_2_[p_184918_3_++], true, server);
            int j = "set".equalsIgnoreCase(s) ? CommandBase.parseInt(p_184918_2_[p_184918_3_++]) : CommandBase.parseInt(p_184918_2_[p_184918_3_++], 0);

            if (p_184918_2_.length > p_184918_3_)
            {
                Entity entity = CommandBase.getEntity(server, sender, p_184918_2_[i]);

                try
                {
                    NBTTagCompound nbttagcompound = JsonToNBT.getTagFromJson(CommandBase.buildString(p_184918_2_, p_184918_3_));
                    NBTTagCompound nbttagcompound1 = CommandBase.entityToNBT(entity);

                    if (!NBTUtil.areNBTEquals(nbttagcompound, nbttagcompound1, true))
                    {
                        throw new CommandException("commands.scoreboard.players.set.tagMismatch", s1);
                    }
                }
                catch (NBTException nbtexception)
                {
                    throw new CommandException("commands.scoreboard.players.set.tagError", nbtexception.getMessage());
                }
            }

            Scoreboard scoreboard = getScoreboard(server);
            Score score = scoreboard.getOrCreateScore(s1, scoreobjective);

            if ("set".equalsIgnoreCase(s))
            {
                score.setScorePoints(j);
            }
            else if ("add".equalsIgnoreCase(s))
            {
                score.increaseScore(j);
            }
            else
            {
                score.decreaseScore(j);
            }

            CommandBase.notifyCommandListener(sender, this, "commands.scoreboard.players.set.success", scoreobjective.getName(), s1, score.getScorePoints());
        }
    }

    protected void resetPlayerScore(ICommandSender sender, String[] p_184912_2_, int p_184912_3_, MinecraftServer server) throws CommandException
    {
        Scoreboard scoreboard = getScoreboard(server);
        String s = CommandBase.getEntityName(server, sender, p_184912_2_[p_184912_3_++]);

        if (p_184912_2_.length > p_184912_3_)
        {
            ScoreObjective scoreobjective = convertToObjective(p_184912_2_[p_184912_3_++], false, server);
            scoreboard.removeObjectiveFromEntity(s, scoreobjective);
            CommandBase.notifyCommandListener(sender, this, "commands.scoreboard.players.resetscore.success", scoreobjective.getName(), s);
        }
        else
        {
            scoreboard.removeObjectiveFromEntity(s, null);
            CommandBase.notifyCommandListener(sender, this, "commands.scoreboard.players.reset.success", s);
        }
    }

    protected void enablePlayerTrigger(ICommandSender sender, String[] p_184914_2_, int p_184914_3_, MinecraftServer server) throws CommandException
    {
        Scoreboard scoreboard = getScoreboard(server);
        String s = CommandBase.getPlayerName(server, sender, p_184914_2_[p_184914_3_++]);

        if (s.length() > 40)
        {
            throw new SyntaxErrorException("commands.scoreboard.players.name.tooLong", s, Integer.valueOf(40));
        }
        else
        {
            ScoreObjective scoreobjective = convertToObjective(p_184914_2_[p_184914_3_], false, server);

            if (scoreobjective.getCriteria() != IScoreCriteria.TRIGGER)
            {
                throw new CommandException("commands.scoreboard.players.enable.noTrigger", scoreobjective.getName());
            }
            else
            {
                Score score = scoreboard.getOrCreateScore(s, scoreobjective);
                score.setLocked(false);
                CommandBase.notifyCommandListener(sender, this, "commands.scoreboard.players.enable.success", scoreobjective.getName(), s);
            }
        }
    }

    protected void testPlayerScore(ICommandSender sender, String[] p_184907_2_, int p_184907_3_, MinecraftServer server) throws CommandException
    {
        Scoreboard scoreboard = getScoreboard(server);
        String s = CommandBase.getEntityName(server, sender, p_184907_2_[p_184907_3_++]);

        if (s.length() > 40)
        {
            throw new SyntaxErrorException("commands.scoreboard.players.name.tooLong", s, Integer.valueOf(40));
        }
        else
        {
            ScoreObjective scoreobjective = convertToObjective(p_184907_2_[p_184907_3_++], false, server);

            if (!scoreboard.entityHasObjective(s, scoreobjective))
            {
                throw new CommandException("commands.scoreboard.players.test.notFound", scoreobjective.getName(), s);
            }
            else
            {
                int i = p_184907_2_[p_184907_3_].equals("*") ? Integer.MIN_VALUE : CommandBase.parseInt(p_184907_2_[p_184907_3_]);
                ++p_184907_3_;
                int j = p_184907_3_ < p_184907_2_.length && !p_184907_2_[p_184907_3_].equals("*") ? CommandBase.parseInt(p_184907_2_[p_184907_3_], i) : Integer.MAX_VALUE;
                Score score = scoreboard.getOrCreateScore(s, scoreobjective);

                if (score.getScorePoints() >= i && score.getScorePoints() <= j)
                {
                    CommandBase.notifyCommandListener(sender, this, "commands.scoreboard.players.test.success", score.getScorePoints(), i, j);
                }
                else
                {
                    throw new CommandException("commands.scoreboard.players.test.failed", score.getScorePoints(), i, j);
                }
            }
        }
    }

    protected void applyPlayerOperation(ICommandSender sender, String[] p_184906_2_, int p_184906_3_, MinecraftServer server) throws CommandException
    {
        Scoreboard scoreboard = getScoreboard(server);
        String s = CommandBase.getEntityName(server, sender, p_184906_2_[p_184906_3_++]);
        ScoreObjective scoreobjective = convertToObjective(p_184906_2_[p_184906_3_++], true, server);
        String s1 = p_184906_2_[p_184906_3_++];
        String s2 = CommandBase.getEntityName(server, sender, p_184906_2_[p_184906_3_++]);
        ScoreObjective scoreobjective1 = convertToObjective(p_184906_2_[p_184906_3_], false, server);

        if (s.length() > 40)
        {
            throw new SyntaxErrorException("commands.scoreboard.players.name.tooLong", s, Integer.valueOf(40));
        }
        else if (s2.length() > 40)
        {
            throw new SyntaxErrorException("commands.scoreboard.players.name.tooLong", s2, Integer.valueOf(40));
        }
        else
        {
            Score score = scoreboard.getOrCreateScore(s, scoreobjective);

            if (!scoreboard.entityHasObjective(s2, scoreobjective1))
            {
                throw new CommandException("commands.scoreboard.players.operation.notFound", scoreobjective1.getName(), s2);
            }
            else
            {
                Score score1 = scoreboard.getOrCreateScore(s2, scoreobjective1);

                if ("+=".equals(s1))
                {
                    score.setScorePoints(score.getScorePoints() + score1.getScorePoints());
                }
                else if ("-=".equals(s1))
                {
                    score.setScorePoints(score.getScorePoints() - score1.getScorePoints());
                }
                else if ("*=".equals(s1))
                {
                    score.setScorePoints(score.getScorePoints() * score1.getScorePoints());
                }
                else if ("/=".equals(s1))
                {
                    if (score1.getScorePoints() != 0)
                    {
                        score.setScorePoints(score.getScorePoints() / score1.getScorePoints());
                    }
                }
                else if ("%=".equals(s1))
                {
                    if (score1.getScorePoints() != 0)
                    {
                        score.setScorePoints(score.getScorePoints() % score1.getScorePoints());
                    }
                }
                else if ("=".equals(s1))
                {
                    score.setScorePoints(score1.getScorePoints());
                }
                else if ("<".equals(s1))
                {
                    score.setScorePoints(Math.min(score.getScorePoints(), score1.getScorePoints()));
                }
                else if (">".equals(s1))
                {
                    score.setScorePoints(Math.max(score.getScorePoints(), score1.getScorePoints()));
                }
                else
                {
                    if (!"><".equals(s1))
                    {
                        throw new CommandException("commands.scoreboard.players.operation.invalidOperation", s1);
                    }

                    int i = score.getScorePoints();
                    score.setScorePoints(score1.getScorePoints());
                    score1.setScorePoints(i);
                }

                CommandBase.notifyCommandListener(sender, this, "commands.scoreboard.players.operation.success");
            }
        }
    }

    protected void applyPlayerTag(MinecraftServer server, ICommandSender sender, String[] p_184924_3_, int p_184924_4_) throws CommandException
    {
        String s = CommandBase.getEntityName(server, sender, p_184924_3_[p_184924_4_]);
        Entity entity = CommandBase.getEntity(server, sender, p_184924_3_[p_184924_4_++]);
        String s1 = p_184924_3_[p_184924_4_++];
        Set<String> set = entity.getTags();

        if ("list".equals(s1))
        {
            if (!set.isEmpty())
            {
                TranslatableComponent textcomponenttranslation = new TranslatableComponent("commands.scoreboard.players.tag.list", s);
                textcomponenttranslation.getStyle().setColor(Formatting.DARK_GREEN);
                sender.addChatMessage(textcomponenttranslation);
                sender.addChatMessage(new TextComponent(CommandBase.joinNiceString(set.toArray())));
            }

            sender.setCommandStat(CommandResultStats.Type.QUERY_RESULT, set.size());
        }
        else if (p_184924_3_.length < 5)
        {
            throw new WrongUsageException("commands.scoreboard.players.tag.usage");
        }
        else
        {
            String s2 = p_184924_3_[p_184924_4_++];

            if (p_184924_3_.length > p_184924_4_)
            {
                try
                {
                    NBTTagCompound nbttagcompound = JsonToNBT.getTagFromJson(CommandBase.buildString(p_184924_3_, p_184924_4_));
                    NBTTagCompound nbttagcompound1 = CommandBase.entityToNBT(entity);

                    if (!NBTUtil.areNBTEquals(nbttagcompound, nbttagcompound1, true))
                    {
                        throw new CommandException("commands.scoreboard.players.tag.tagMismatch", s);
                    }
                }
                catch (NBTException nbtexception)
                {
                    throw new CommandException("commands.scoreboard.players.tag.tagError", nbtexception.getMessage());
                }
            }

            if ("add".equals(s1))
            {
                if (!entity.addTag(s2))
                {
                    throw new CommandException("commands.scoreboard.players.tag.tooMany", Integer.valueOf(1024));
                }

                CommandBase.notifyCommandListener(sender, this, "commands.scoreboard.players.tag.success.add", s2);
            }
            else
            {
                if (!"remove".equals(s1))
                {
                    throw new WrongUsageException("commands.scoreboard.players.tag.usage");
                }

                if (!entity.removeTag(s2))
                {
                    throw new CommandException("commands.scoreboard.players.tag.notFound", s2);
                }

                CommandBase.notifyCommandListener(sender, this, "commands.scoreboard.players.tag.success.remove", s2);
            }
        }
    }

    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos)
    {
        if (args.length == 1)
        {
            return CommandBase.getListOfStringsMatchingLastWord(args, "objectives", "players", "teams");
        }
        else
        {
            if ("objectives".equalsIgnoreCase(args[0]))
            {
                if (args.length == 2)
                {
                    return CommandBase.getListOfStringsMatchingLastWord(args, "list", "add", "remove", "setdisplay");
                }

                if ("add".equalsIgnoreCase(args[1]))
                {
                    if (args.length == 4)
                    {
                        Set<String> set = IScoreCriteria.INSTANCES.keySet();
                        return CommandBase.getListOfStringsMatchingLastWord(args, set);
                    }
                }
                else if ("remove".equalsIgnoreCase(args[1]))
                {
                    if (args.length == 3)
                    {
                        return CommandBase.getListOfStringsMatchingLastWord(args, getObjectiveNames(false, server));
                    }
                }
                else if ("setdisplay".equalsIgnoreCase(args[1]))
                {
                    if (args.length == 3)
                    {
                        return CommandBase.getListOfStringsMatchingLastWord(args, Scoreboard.getDisplaySlotStrings());
                    }

                    if (args.length == 4)
                    {
                        return CommandBase.getListOfStringsMatchingLastWord(args, getObjectiveNames(false, server));
                    }
                }
            }
            else if ("players".equalsIgnoreCase(args[0]))
            {
                if (args.length == 2)
                {
                    return CommandBase.getListOfStringsMatchingLastWord(args, "set", "add", "remove", "reset", "list", "enable", "test", "operation", "tag");
                }

                if (!"set".equalsIgnoreCase(args[1]) && !"add".equalsIgnoreCase(args[1]) && !"remove".equalsIgnoreCase(args[1]) && !"reset".equalsIgnoreCase(args[1]))
                {
                    if ("enable".equalsIgnoreCase(args[1]))
                    {
                        if (args.length == 3)
                        {
                            return CommandBase.getListOfStringsMatchingLastWord(args, server.getAllUsernames());
                        }

                        if (args.length == 4)
                        {
                            return CommandBase.getListOfStringsMatchingLastWord(args, getTriggerNames(server));
                        }
                    }
                    else if (!"list".equalsIgnoreCase(args[1]) && !"test".equalsIgnoreCase(args[1]))
                    {
                        if ("operation".equalsIgnoreCase(args[1]))
                        {
                            if (args.length == 3)
                            {
                                return CommandBase.getListOfStringsMatchingLastWord(args, getScoreboard(server).getObjectiveNames());
                            }

                            if (args.length == 4)
                            {
                                return CommandBase.getListOfStringsMatchingLastWord(args, getObjectiveNames(true, server));
                            }

                            if (args.length == 5)
                            {
                                return CommandBase.getListOfStringsMatchingLastWord(args, "+=", "-=", "*=", "/=", "%=", "=", "<", ">", "><");
                            }

                            if (args.length == 6)
                            {
                                return CommandBase.getListOfStringsMatchingLastWord(args, server.getAllUsernames());
                            }

                            if (args.length == 7)
                            {
                                return CommandBase.getListOfStringsMatchingLastWord(args, getObjectiveNames(false, server));
                            }
                        }
                        else if ("tag".equalsIgnoreCase(args[1]))
                        {
                            if (args.length == 3)
                            {
                                return CommandBase.getListOfStringsMatchingLastWord(args, getScoreboard(server).getObjectiveNames());
                            }

                            if (args.length == 4)
                            {
                                return CommandBase.getListOfStringsMatchingLastWord(args, "add", "remove", "list");
                            }
                        }
                    }
                    else
                    {
                        if (args.length == 3)
                        {
                            return CommandBase.getListOfStringsMatchingLastWord(args, getScoreboard(server).getObjectiveNames());
                        }

                        if (args.length == 4 && "test".equalsIgnoreCase(args[1]))
                        {
                            return CommandBase.getListOfStringsMatchingLastWord(args, getObjectiveNames(false, server));
                        }
                    }
                }
                else
                {
                    if (args.length == 3)
                    {
                        return CommandBase.getListOfStringsMatchingLastWord(args, server.getAllUsernames());
                    }

                    if (args.length == 4)
                    {
                        return CommandBase.getListOfStringsMatchingLastWord(args, getObjectiveNames(true, server));
                    }
                }
            }
            else if ("teams".equalsIgnoreCase(args[0]))
            {
                if (args.length == 2)
                {
                    return CommandBase.getListOfStringsMatchingLastWord(args, "add", "remove", "join", "leave", "empty", "list", "option");
                }

                if ("join".equalsIgnoreCase(args[1]))
                {
                    if (args.length == 3)
                    {
                        return CommandBase.getListOfStringsMatchingLastWord(args, getScoreboard(server).getTeamNames());
                    }

                    if (args.length >= 4)
                    {
                        return CommandBase.getListOfStringsMatchingLastWord(args, server.getAllUsernames());
                    }
                }
                else
                {
                    if ("leave".equalsIgnoreCase(args[1]))
                    {
                        return CommandBase.getListOfStringsMatchingLastWord(args, server.getAllUsernames());
                    }

                    if (!"empty".equalsIgnoreCase(args[1]) && !"list".equalsIgnoreCase(args[1]) && !"remove".equalsIgnoreCase(args[1]))
                    {
                        if ("option".equalsIgnoreCase(args[1]))
                        {
                            if (args.length == 3)
                            {
                                return CommandBase.getListOfStringsMatchingLastWord(args, getScoreboard(server).getTeamNames());
                            }

                            if (args.length == 4)
                            {
                                return CommandBase.getListOfStringsMatchingLastWord(args, "color", "friendlyfire", "seeFriendlyInvisibles", "nametagVisibility", "deathMessageVisibility", "collisionRule");
                            }

                            if (args.length == 5)
                            {
                                if ("color".equalsIgnoreCase(args[3]))
                                {
                                    return CommandBase.getListOfStringsMatchingLastWord(args, Formatting.getValidValues(true, false));
                                }

                                if ("nametagVisibility".equalsIgnoreCase(args[3]) || "deathMessageVisibility".equalsIgnoreCase(args[3]))
                                {
                                    return CommandBase.getListOfStringsMatchingLastWord(args, Team.EnumVisible.getNames());
                                }

                                if ("collisionRule".equalsIgnoreCase(args[3]))
                                {
                                    return CommandBase.getListOfStringsMatchingLastWord(args, Team.CollisionRule.getNames());
                                }

                                if ("friendlyfire".equalsIgnoreCase(args[3]) || "seeFriendlyInvisibles".equalsIgnoreCase(args[3]))
                                {
                                    return CommandBase.getListOfStringsMatchingLastWord(args, "true", "false");
                                }
                            }
                        }
                    }
                    else if (args.length == 3)
                    {
                        return CommandBase.getListOfStringsMatchingLastWord(args, getScoreboard(server).getTeamNames());
                    }
                }
            }

            return Collections.emptyList();
        }
    }

    protected List<String> getObjectiveNames(boolean writableOnly, MinecraftServer server)
    {
        Collection<ScoreObjective> collection = getScoreboard(server).getScoreObjectives();
        List<String> list = Lists.newArrayList();

        for (ScoreObjective scoreobjective : collection)
        {
            if (!writableOnly || !scoreobjective.getCriteria().isReadOnly())
            {
                list.add(scoreobjective.getName());
            }
        }

        return list;
    }

    protected List<String> getTriggerNames(MinecraftServer server)
    {
        Collection<ScoreObjective> collection = getScoreboard(server).getScoreObjectives();
        List<String> list = Lists.newArrayList();

        for (ScoreObjective scoreobjective : collection)
        {
            if (scoreobjective.getCriteria() == IScoreCriteria.TRIGGER)
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
        if (!"players".equalsIgnoreCase(args[0]))
        {
            if ("teams".equalsIgnoreCase(args[0]))
            {
                return index == 2;
            }
            else
            {
                return false;
            }
        }
        else if (args.length > 1 && "operation".equalsIgnoreCase(args[1]))
        {
            return index == 2 || index == 5;
        }
        else
        {
            return index == 2;
        }
    }
}
