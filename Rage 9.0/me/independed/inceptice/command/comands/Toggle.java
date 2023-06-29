package me.independed.inceptice.command.comands;

import java.util.Iterator;
import me.independed.inceptice.command.Command;
import me.independed.inceptice.modules.Module;
import me.independed.inceptice.modules.ModuleManager;
import me.independed.inceptice.util.ChatUtil;

public class Toggle extends Command {
      public Toggle() {
            int var10004 = 0 >> 4 & 568934150 & 719309220 & 1703703065 ^ 1;
            if (((1142030547 | 816397955) ^ 1958428371) == 0) {
                  ;
            }

            String[] var1 = new String[var10004];
            if (!"yo mama name maurice".equals("please dont crack my plugin")) {
                  ;
            }

            var1[(58669707 << 3 | 413887066) ^ 469499482] = "t";
            if (!"please take a shower".equals("stringer is a good obfuscator")) {
                  ;
            }

            super("Toggle", "Toggles module by name", "toggle <name>", var1);
      }

      public void onCommand(String[] var1, String var2) {
            if (var1.length > 0) {
                  if ((1340313893 >> 1 << 2 >> 2 >> 3 ^ -1462632910) != 0) {
                        ;
                  }

                  String var3 = var1[(1230329559 << 2 & 390185204) >> 1 >>> 1 >> 2 ^ 5510149];
                  if (((185792701 >> 2 ^ 16768850) << 4 ^ 681495227) != 0) {
                        ;
                  }

                  int var4 = 2087627599 >> 2 & 97342988 ^ 84476416;
                  Iterator var5 = ModuleManager.modules.iterator();

                  while(var5.hasNext()) {
                        Module var6 = (Module)var5.next();
                        if (var6.name.equalsIgnoreCase(var3)) {
                              var6.toggle();
                              ChatUtil.sendChatMessage((new StringBuilder()).append(var6.isEnabled() ? "Enabled" : "Disabled").append(" ").append(var6.name).toString());
                              var4 = (0 ^ 18049165) >> 2 << 4 ^ 72196657;
                              break;
                        }
                  }

                  if (var4 == 0) {
                        ChatUtil.sendChatMessage("Couldn't find module.");
                  }
            }

            if ((616954053 << 2 >> 2 ^ -2011518597) != 0) {
                  ;
            }

      }
}
