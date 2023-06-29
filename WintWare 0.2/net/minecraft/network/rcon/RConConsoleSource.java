package net.minecraft.network.rcon;

import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

public class RConConsoleSource implements ICommandSender {
   private final StringBuffer buffer = new StringBuffer();
   private final MinecraftServer server;

   public RConConsoleSource(MinecraftServer serverIn) {
      this.server = serverIn;
   }

   public String getName() {
      return "Rcon";
   }

   public void addChatMessage(ITextComponent component) {
      this.buffer.append(component.getUnformattedText());
   }

   public boolean canCommandSenderUseCommand(int permLevel, String commandName) {
      return true;
   }

   public World getEntityWorld() {
      return this.server.getEntityWorld();
   }

   public boolean sendCommandFeedback() {
      return true;
   }

   public MinecraftServer getServer() {
      return this.server;
   }
}
