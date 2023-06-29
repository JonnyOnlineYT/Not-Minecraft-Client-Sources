package net.minecraft.command;

import net.minecraft.server.MinecraftServer;

public class CommandReload extends CommandBase {
   public String getCommandName() {
      return "reload";
   }

   public int getRequiredPermissionLevel() {
      return 3;
   }

   public String getCommandUsage(ICommandSender sender) {
      return "commands.reload.usage";
   }

   public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
      if (args.length > 0) {
         throw new WrongUsageException("commands.reload.usage", new Object[0]);
      } else {
         server.func_193031_aM();
         notifyCommandListener(sender, this, "commands.reload.success", new Object[0]);
      }
   }
}
