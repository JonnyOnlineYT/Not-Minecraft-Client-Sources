package net.minecraft.command;

import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.GameType;

public class CommandDefaultGameMode extends CommandGameMode {
   public String getCommandName() {
      return "defaultgamemode";
   }

   public String getCommandUsage(ICommandSender sender) {
      return "commands.defaultgamemode.usage";
   }

   public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
      if (args.length <= 0) {
         throw new WrongUsageException("commands.defaultgamemode.usage", new Object[0]);
      } else {
         GameType gametype = this.getGameModeFromCommand(sender, args[0]);
         this.setDefaultGameType(gametype, server);
         notifyCommandListener(sender, this, "commands.defaultgamemode.success", new Object[]{new TextComponentTranslation("gameMode." + gametype.getName(), new Object[0])});
      }
   }

   protected void setDefaultGameType(GameType gameType, MinecraftServer server) {
      server.setGameType(gameType);
      if (server.getForceGamemode()) {
         Iterator var3 = server.getPlayerList().getPlayerList().iterator();

         while(var3.hasNext()) {
            EntityPlayerMP entityplayermp = (EntityPlayerMP)var3.next();
            entityplayermp.setGameType(gameType);
         }
      }

   }
}
