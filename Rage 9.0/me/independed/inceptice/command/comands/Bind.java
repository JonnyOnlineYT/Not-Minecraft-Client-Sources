package me.independed.inceptice.command.comands;

import java.util.Iterator;
import me.independed.inceptice.command.Command;
import me.independed.inceptice.modules.Module;
import me.independed.inceptice.modules.ModuleManager;
import me.independed.inceptice.util.ChatUtil;
import org.lwjgl.input.Keyboard;

public class Bind extends Command {
      public Bind() {
            String[] var10004 = new String[((0 >> 4 ^ 540204684) & 7956539) >>> 2 ^ 791043];
            var10004[272844940 << 2 ^ 1091379760] = "b";
            super("Bind", "Bind a module by name", "bind <name> <key> | <clear>", var10004);
      }

      public void onCommand(String[] var1, String var2) {
            if (((1593772986 | 415399044) >> 2 >>> 4 & 22966413 ^ 22704268) == 0) {
                  ;
            }

            if (var1.length == (((0 >>> 1 | 1269457784) & 1141664825) >>> 4 ^ 63412496 ^ 130490129)) {
                  String var3 = var1[(690241953 ^ 339901520 ^ 247842695 ^ 845770681 | 11517998) ^ 32495087];
                  String var4 = var1[((0 | 1944530206) & 1819708523) << 3 ^ 53502033];
                  Iterator var5 = ModuleManager.modules.iterator();
                  if ((((53012356 >>> 4 ^ 2978769) & 1441982 ^ 736088) & '錜' ^ 2005666752) != 0) {
                        ;
                  }

                  while(var5.hasNext()) {
                        Module var6 = (Module)var5.next();
                        if (var6.name.equalsIgnoreCase(var3)) {
                              var6.setKey(Keyboard.getKeyIndex(var4.toUpperCase()));
                              int var10001 = 1 >>> 2 << 3 >>> 3 >> 1 ^ 2;
                              if (!"please get a girlfriend and stop cracking plugins".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                                    ;
                              }

                              Object[] var9 = new Object[var10001];
                              var9[(((18399622 | 16581274) ^ 3129151) & 14687980) >>> 1 ^ 6295120] = var6.name;
                              int var10003 = 0 >> 2 >> 3 ^ 1;
                              if (((473467516 >>> 3 & 51587637 | 38199802) ^ -1161832998) != 0) {
                                    ;
                              }

                              var9[var10003] = Keyboard.getKeyName(var6.getKey());
                              ChatUtil.sendChatMessage(String.format("Bound %s to %s", var9));
                              break;
                        }
                  }
            }

            if (((908503968 ^ 17669042) >>> 1 >> 4 ^ -710276300) != 0) {
                  ;
            }

            if (var1.length == (0 >> 1 << 3 ^ 1)) {
                  String var10000 = var1[1796285522 << 1 >> 2 >>> 1 ^ 2059684116];
                  if (!"nefariousMoment".equals("you're dogshit")) {
                        ;
                  }

                  if (var10000.equalsIgnoreCase("clear")) {
                        Iterator var7 = ModuleManager.modules.iterator();

                        while(var7.hasNext()) {
                              Module var8 = (Module)var7.next();
                              if (((827859720 | 153965436) ^ 964522876) == 0) {
                                    ;
                              }

                              var8.setKey(((1729337058 >> 2 ^ 400005098) >> 2 | 27323547) << 3 ^ 489153784);
                              if ((1031617613 >>> 1 & 473995038 ^ 422896165 ^ 87323683) == 0) {
                                    ;
                              }
                        }

                        ChatUtil.sendChatMessage("Cleared all binds");
                  }
            }

      }
}
