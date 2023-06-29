package me.wintware.client.command.impl;

import java.util.Arrays;
import java.util.List;
import me.wintware.client.command.AbstractCommand;
import me.wintware.client.module.hud.HUD;
import me.wintware.client.utils.other.ChatUtils;

public final class ClientNameCommand extends AbstractCommand {
   private final List chars = Arrays.asList('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'k', 'm', 'o', 'l', 'n', 'r');

   public ClientNameCommand() {
      super("clientname", "Change text displayed on watermark.", "clientname <name>", "clientname", "name", "rename");
   }

   public void execute(String... arguments) {
      if (arguments.length >= 2) {
         StringBuilder string = new StringBuilder();

         for(int i = 1; i < arguments.length; ++i) {
            String tempString = arguments[i];
            tempString = tempString.replace('&', '§');
            string.append(tempString).append(" ");
         }

         ChatUtils.addChatMessage(String.format("Changed client name to '%s§7' was '%s§7'.", string.toString().trim(), HUD.clientName));
         HUD.clientName = string.toString().trim();
      } else {
         this.usage();
      }

   }
}
