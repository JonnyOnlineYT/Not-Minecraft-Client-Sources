package me.independed.inceptice.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import me.independed.inceptice.command.impl.Bind;
import me.independed.inceptice.command.impl.Friend;
import me.independed.inceptice.command.impl.LoadConfig;
import me.independed.inceptice.command.impl.SaveConfig;
import me.independed.inceptice.util.ChatUtil;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CommandManager {
      public String prefix = "-";
      public List commands = new ArrayList();

      @SubscribeEvent
      public void handleChat(ClientChatEvent var1) {
            String var2 = var1.getMessage();
            boolean var10000 = var2.startsWith(this.prefix);
            if ((122714168 >>> 4 & 3680127 ^ 1328923249) != 0) {
                  ;
            }

            if (var10000) {
                  if (!"stop skidding".equals("please dont crack my plugin")) {
                        ;
                  }

                  var1.setCanceled((boolean)(0 >>> 1 << 2 >>> 2 >>> 2 ^ 1));
                  var2 = var2.substring(this.prefix.length());
                  int var7 = 1813076174 << 2 << 4 ^ 72758144;
                  if ((((109815851 >>> 3 ^ 6684222) >> 1 | 1807693) ^ -1405639666) != 0) {
                        ;
                  }

                  int var3 = var7;
                  if (((String[])var2.split(" ")).length > 0) {
                        label53: {
                              String var4 = ((String[])var2.split(" "))[((606458316 | 576006167) & 24181437 ^ 3198619) << 2 ^ 16832536];
                              Iterator var5 = this.commands.iterator();

                              Command var6;
                              do {
                                    if (!var5.hasNext()) {
                                          break label53;
                                    }

                                    var6 = (Command)var5.next();
                              } while(!var6.aliases.contains(var4) && !var6.name.equalsIgnoreCase(var4));

                              if ((((1372656502 << 2 >> 2 | 149871768) & 32494431) >> 1 ^ 16247215) == 0) {
                                    ;
                              }

                              String[] var10001 = (String[])var2.split(" ");
                              int var10002 = (0 >> 4 | 1306836958) ^ 1306836959;
                              if (!"your mom your dad the one you never had".equals("stop skidding")) {
                                    ;
                              }

                              var6.onCommand((String[])((Object[])Arrays.copyOfRange(var10001, var10002, ((String[])var2.split(" ")).length)), var2);
                              var3 = ((0 | 730946874) ^ 221393380) & 101521996 ^ 100730957;
                        }
                  }

                  if (var3 == 0) {
                        ChatUtil.sendChatMessage("Couldn't find command.");
                  }

            }
      }

      public CommandManager() {
            this.setup();
      }

      public void setup() {
            if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("stringer is a good obfuscator")) {
                  ;
            }

            this.commands.add(new Friend());
            this.commands.add(new SaveConfig());
            this.commands.add(new LoadConfig());
            this.commands.add(new Bind());
      }
}
