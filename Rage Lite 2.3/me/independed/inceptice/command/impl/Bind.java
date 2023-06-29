package me.independed.inceptice.command.impl;

import java.util.ArrayList;
import java.util.Iterator;
import me.independed.inceptice.command.Command;
import me.independed.inceptice.modules.Module;
import me.independed.inceptice.modules.ModuleManager;
import me.independed.inceptice.util.ChatUtil;
import org.lwjgl.input.Keyboard;

public class Bind extends Command {
      public Bind() {
            String[] var10004 = new String[(0 << 1 | 1252284938) & 700317778 ^ 144965635];
            var10004[5374384 << 4 >> 1 >>> 3 ^ 5374384] = "b";
            super("Bind", "Bind a module by name", "bind <name> <key> | <clear>", var10004);
      }

      public void onCommand(String[] var1, String var2) {
            if (((1192454237 ^ 485935782) >>> 1 & 490629900 ^ 221397260) == 0) {
                  ;
            }

            if (((63012155 ^ 3023084) << 1 ^ 37200654 ^ 187344154) != 0) {
                  ;
            }

            if (var1.length == (((0 >>> 3 | 1551007346) & 65272553 ^ 2250825) >> 3 ^ 525383)) {
                  if (!"your mom your dad the one you never had".equals("your mom your dad the one you never had")) {
                        ;
                  }

                  String var3 = var1[(2136208942 << 4 >> 1 | 501219831) ^ -19977];
                  String var4 = var1[0 >> 1 >>> 1 ^ 1392983931 ^ 1392983930];
                  Iterator var5 = ModuleManager.modules.iterator();

                  while(var5.hasNext()) {
                        Module var6 = (Module)var5.next();
                        if (var6.name.equalsIgnoreCase(var3)) {
                              var6.setKey(Keyboard.getKeyIndex(var4.toUpperCase()));
                              Object[] var10001 = new Object[(0 >>> 1 | 178038633) & 34699832 ^ 34611754];
                              if ((((1360551172 | 1349611026) ^ 687303543) >> 2 ^ -1011492387) != 0) {
                                    ;
                              }

                              var10001[(201393184 | 140600520 | 42029178) ^ 249656570] = var6.name;
                              var10001[0 >>> 4 >> 3 ^ 1] = Keyboard.getKeyName(var6.getKey());
                              ChatUtil.sendChatMessage(String.format("Bound %s to %s", var10001));
                              break;
                        }
                  }
            }

            if (var1.length == (((0 | 918897528) & 786631605 | 444351586) << 3 >> 1 ^ -67822135) && var1[4738692 ^ 4738692].equalsIgnoreCase("clear")) {
                  ArrayList var10000 = ModuleManager.modules;
                  if (((139651731 << 2 | 299508344) ^ 744856181) != 0) {
                        ;
                  }

                  Iterator var7 = var10000.iterator();

                  while(true) {
                        boolean var9 = var7.hasNext();
                        if (((464651871 | 324200601) >>> 4 >> 2 ^ -1440415323) != 0) {
                              ;
                        }

                        if (!var9) {
                              ChatUtil.sendChatMessage("Cleared all binds");
                              break;
                        }

                        Module var8 = (Module)var7.next();
                        var8.setKey((340137012 << 3 & 1839810638) >> 1 << 1 ^ 538968064);
                        if (((276840963 << 1 | 508935314) ^ 1062583446) == 0) {
                              ;
                        }
                  }
            }

      }
}
