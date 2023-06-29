package net.minecraft.command;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class CommandShowSeed extends CommandBase {
   public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
      return server.isSinglePlayer() || super.checkPermission(server, sender);
   }

   public String getCommandName() {
      return "seed";
   }

   public int getRequiredPermissionLevel() {
      return 2;
   }

   public String getCommandUsage(ICommandSender sender) {
      return "commands.seed.usage";
   }

   public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
      World world = sender instanceof EntityPlayer ? ((EntityPlayer)sender).world : server.worldServerForDimension(0);
      sender.addChatMessage(new TextComponentTranslation("commands.seed.success", new Object[]{((World)world).getSeed()}));
   }
}
