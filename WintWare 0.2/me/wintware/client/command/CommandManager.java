package me.wintware.client.command;

import java.util.ArrayList;
import java.util.List;
import me.wintware.client.command.impl.BindCommand;
import me.wintware.client.command.impl.ClientNameCommand;
import me.wintware.client.event.EventManager;

public final class CommandManager {
   public static final String PREFIX = ".";
   private final List commands = new ArrayList();

   public CommandManager() {
      EventManager.register(new CommandHandler(this));
      this.commands.add(new ClientNameCommand());
      this.commands.add(new BindCommand());
   }

   public List getCommands() {
      return this.commands;
   }

   public final boolean execute(String args) {
      String noPrefix = args.substring(1);
      String[] split = noPrefix.split(" ");
      if (split.length > 0) {
         List commands = this.commands;
         int i = 0;

         for(int commandsSize = commands.size(); i < commandsSize; ++i) {
            AbstractCommand command = (AbstractCommand)commands.get(i);
            String[] var8 = command.getAliases();
            int var9 = var8.length;

            for(int var10 = 0; var10 < var9; ++var10) {
               String alias = var8[var10];
               if (split[0].equalsIgnoreCase(alias)) {
                  command.execute(split);
                  return true;
               }
            }
         }
      }

      return false;
   }
}
