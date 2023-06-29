package net.minecraft.command;

import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Namespaced;
import net.minecraft.util.math.BlockPos;

public class RecipeCommand extends CommandBase
{
    /**
     * Gets the name of the command
     */
    public String getCommandName()
    {
        return "recipe";
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
        return "commands.recipe.usage";
    }

    /**
     * Callback for when the command is executed
     */
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        if (args.length < 2)
        {
            throw new WrongUsageException("commands.recipe.usage");
        }
        else
        {
            boolean flag = "give".equalsIgnoreCase(args[0]);
            boolean flag1 = "take".equalsIgnoreCase(args[0]);

            if (!flag && !flag1)
            {
                throw new WrongUsageException("commands.recipe.usage");
            }
            else
            {
                for (EntityPlayerMP entityplayermp : CommandBase.func_193513_a(server, sender, args[1]))
                {
                    if ("*".equals(args[2]))
                    {
                        if (flag)
                        {
                            entityplayermp.func_192021_a(func_192556_d());
                            CommandBase.notifyCommandListener(sender, this, "commands.recipe.give.success.all", entityplayermp.getName());
                        }
                        else
                        {
                            entityplayermp.func_192022_b(func_192556_d());
                            CommandBase.notifyCommandListener(sender, this, "commands.recipe.take.success.all", entityplayermp.getName());
                        }
                    }
                    else
                    {
                        IRecipe irecipe = CraftingManager.func_193373_a(new Namespaced(args[2]));

                        if (irecipe == null)
                        {
                            throw new CommandException("commands.recipe.unknownrecipe", args[2]);
                        }

                        if (irecipe.func_192399_d())
                        {
                            throw new CommandException("commands.recipe.unsupported", args[2]);
                        }

                        List<IRecipe> list = Lists.newArrayList(irecipe);

                        if (flag == entityplayermp.func_192037_E().func_193830_f(irecipe))
                        {
                            String s = flag ? "commands.recipe.alreadyHave" : "commands.recipe.dontHave";
                            throw new CommandException(s, entityplayermp.getName(), irecipe.getRecipeOutput().getDisplayName());
                        }

                        if (flag)
                        {
                            entityplayermp.func_192021_a(list);
                            CommandBase.notifyCommandListener(sender, this, "commands.recipe.give.success.one", entityplayermp.getName(), irecipe.getRecipeOutput().getDisplayName());
                        }
                        else
                        {
                            entityplayermp.func_192022_b(list);
                            CommandBase.notifyCommandListener(sender, this, "commands.recipe.take.success.one", irecipe.getRecipeOutput().getDisplayName(), entityplayermp.getName());
                        }
                    }
                }
            }
        }
    }

    private List<IRecipe> func_192556_d()
    {
        return Lists.newArrayList(CraftingManager.field_193380_a);
    }

    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos)
    {
        if (args.length == 1)
        {
            return CommandBase.getListOfStringsMatchingLastWord(args, "give", "take");
        }
        else if (args.length == 2)
        {
            return CommandBase.getListOfStringsMatchingLastWord(args, server.getAllUsernames());
        }
        else
        {
            return args.length == 3 ? CommandBase.getListOfStringsMatchingLastWord(args, CraftingManager.field_193380_a.getKeys()) : Collections.emptyList();
        }
    }
}
