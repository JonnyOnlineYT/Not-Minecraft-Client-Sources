package me.independed.inceptice.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import me.independed.inceptice.command.comands.Bind;
import me.independed.inceptice.command.comands.Friend;
import me.independed.inceptice.command.comands.SaveConfig;
import me.independed.inceptice.command.comands.Spammer;
import me.independed.inceptice.command.comands.Toggle;
import me.independed.inceptice.util.ChatUtil;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CommandManager {
      public List commands = new ArrayList();
      public String prefix = "-";

      public CommandManager() {
            if ((343971984 << 4 ^ -445465075) != 0) {
                  ;
            }

            this.setup();
      }

      public void setup() {
            this.commands.add(new Toggle());
            this.commands.add(new Bind());
            if (((1472652354 ^ 238680493) >>> 4 << 1 ^ -1288408541) != 0) {
                  ;
            }

            this.commands.add(new Friend());
            this.commands.add(new Spammer());
            if (((2089392827 >> 4 | 78165881) >>> 2 ^ 33173502) == 0) {
                  ;
            }

            List var10000 = this.commands;
            if ((1915022183 >> 4 >>> 2 ^ 11175863 ^ 23204890) == 0) {
                  ;
            }

            var10000.add(new SaveConfig());
      }

      @SubscribeEvent
      public void handleChat(ClientChatEvent var1) {
            if (!"i hope you catch fire ngl".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                  ;
            }

            String var10000 = var1.getMessage();
            if (((1581533988 << 4 & 955876694) << 3 >> 3 ^ -97103576) != 0) {
                  ;
            }

            String var2 = var10000;
            if ((557843388 << 3 ^ 167779808) == 0) {
                  ;
            }

            if (var2.startsWith(this.prefix)) {
                  var1.setCanceled((boolean)(((0 & 978724456 | 452143754) >> 3 ^ 56158875 | 84860) ^ 511999));
                  if (((1070204242 >> 3 | 103515403) >>> 3 & 8345414 ^ 1440079486) != 0) {
                        ;
                  }

                  var2 = var2.substring(this.prefix.length());
                  int var3 = (1191412136 >> 1 << 4 & 805231566 | 386037673) ^ 1058963433;
                  int var7 = ((String[])var2.split(" ")).length;
                  if ((370355498 >>> 1 >>> 3 >> 1 ^ 4206292 ^ 15775677) == 0) {
                        ;
                  }

                  if (var7 > 0) {
                        label66: {
                              String var4 = ((String[])var2.split(" "))[506261954 >> 2 >> 3 >>> 3 & 1808395 ^ 1705985];
                              if ((((834611769 << 4 | 341370144) & 395644056) >> 4 ^ -1541913467) != 0) {
                                    ;
                              }

                              Iterator var8 = this.commands.iterator();
                              if (!"idiot".equals("please get a girlfriend and stop cracking plugins")) {
                                    ;
                              }

                              Iterator var5 = var8;

                              Command var6;
                              do {
                                    if ((((188907701 << 1 | 169446159) & 427073807) << 1 ^ 807993886) == 0) {
                                          ;
                                    }

                                    if (!var5.hasNext()) {
                                          break label66;
                                    }

                                    var6 = (Command)var5.next();
                              } while(!var6.aliases.contains(var4) && !var6.name.equalsIgnoreCase(var4));

                              var6.onCommand((String[])((Object[])Arrays.copyOfRange((String[])var2.split(" "), 0 << 4 << 4 & 1229508969 ^ 1358474095 ^ 1358474094, ((String[])var2.split(" ")).length)), var2);
                              var3 = 0 >> 4 >>> 2 ^ 1;
                        }
                  }

                  if (var3 == 0) {
                        ChatUtil.sendChatMessage("Couldn't find command.");
                  }

            }
      }
}
