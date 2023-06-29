package net.minecraft.command;

import java.util.List;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public interface ICommand extends Comparable<ICommand> {
   String getCommandName();

   String getCommandUsage(ICommandSender var1);

   List<String> getCommandAliases();

   void execute(MinecraftServer var1, ICommandSender var2, String[] var3) throws CommandException;

   boolean checkPermission(MinecraftServer var1, ICommandSender var2);

   List<String> getTabCompletionOptions(MinecraftServer var1, ICommandSender var2, String[] var3, BlockPos var4);

   boolean isUsernameIndex(String[] var1, int var2);
}
