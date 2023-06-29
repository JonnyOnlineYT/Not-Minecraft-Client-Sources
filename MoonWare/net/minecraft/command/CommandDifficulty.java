package net.minecraft.command;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslatableComponent;
import net.minecraft.world.EnumDifficulty;

public class CommandDifficulty extends CommandBase
{
    /**
     * Gets the name of the command
     */
    public String getCommandName()
    {
        return "difficulty";
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
        return "commands.difficulty.usage";
    }

    /**
     * Callback for when the command is executed
     */
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        if (args.length <= 0)
        {
            throw new WrongUsageException("commands.difficulty.usage");
        }
        else
        {
            EnumDifficulty enumdifficulty = getDifficultyFromCommand(args[0]);
            server.setDifficultyForAllWorlds(enumdifficulty);
            CommandBase.notifyCommandListener(sender, this, "commands.difficulty.success", new TranslatableComponent(enumdifficulty.getDifficultyResourceKey()));
        }
    }

    protected EnumDifficulty getDifficultyFromCommand(String difficultyString) throws CommandException {
        if (!"peaceful".equalsIgnoreCase(difficultyString) && !"p".equalsIgnoreCase(difficultyString))
        {
            if (!"easy".equalsIgnoreCase(difficultyString) && !"e".equalsIgnoreCase(difficultyString))
            {
                if (!"normal".equalsIgnoreCase(difficultyString) && !"n".equalsIgnoreCase(difficultyString))
                {
                    return !"hard".equalsIgnoreCase(difficultyString) && !"h".equalsIgnoreCase(difficultyString) ? EnumDifficulty.getDifficultyEnum(CommandBase.parseInt(difficultyString, 0, 3)) : EnumDifficulty.HARD;
                }
                else
                {
                    return EnumDifficulty.NORMAL;
                }
            }
            else
            {
                return EnumDifficulty.EASY;
            }
        }
        else
        {
            return EnumDifficulty.PEACEFUL;
        }
    }

    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos)
    {
        return args.length == 1 ? CommandBase.getListOfStringsMatchingLastWord(args, "peaceful", "easy", "normal", "hard") : Collections.emptyList();
    }
}
