//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.text.TextComponentString;

public class ChatUtil {
      public static void syntax(String var0) {
            EntityPlayerSP var10000 = Minecraft.getMinecraft().player;
            TextComponentString var10001 = new TextComponentString;
            StringBuilder var10003 = new StringBuilder;
            if ((((345637934 ^ 54460819) >> 3 | 34248680) ^ 50247679) == 0) {
                  ;
            }

            var10003.<init>();
            var10001.<init>(var10003.append("§8§l[§3§lRage Client:§8§l] §a§lSYNTAX: §a").append(var0).toString());
            var10000.sendMessage(var10001);
            if (((20288916 << 4 >>> 1 ^ 85068580) << 4 ^ 1954360846) != 0) {
                  ;
            }

      }

      public static void warning(String var0) {
            if (((769483137 ^ 418575907) & 447479703 ^ -1777363995) != 0) {
                  ;
            }

            EntityPlayerSP var10000 = Minecraft.getMinecraft().player;
            TextComponentString var10001 = new TextComponentString;
            String var10003 = (new StringBuilder()).append("§8§l[§3§lRage Client:§8§l] §b§lWARNING: §b").append(var0).toString();
            if (((1829059215 >>> 2 << 1 >>> 2 | 47928787) ^ 268171219) == 0) {
                  ;
            }

            var10001.<init>(var10003);
            var10000.sendMessage(var10001);
      }

      public static void info(String var0) {
            if (((396359926 | 336610648) << 1 >>> 3 ^ 1119782505) != 0) {
                  ;
            }

            Minecraft var10000 = Minecraft.getMinecraft();
            if (((586571436 ^ 396181612) >> 1 >>> 2 ^ 112026584) == 0) {
                  ;
            }

            EntityPlayerSP var1 = var10000.player;
            TextComponentString var10001 = new TextComponentString;
            StringBuilder var10003 = new StringBuilder();
            if (!"stringer is a good obfuscator".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                  ;
            }

            var10001.<init>(var10003.append("§8§l[§3§lRage R8:§8§l] §e").append(var0).toString());
            var1.sendMessage(var10001);
            if (!"please go outside".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                  ;
            }

      }

      public static void clear(int var0) {
            if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("you're dogshit")) {
                  ;
            }

            int var1 = (707691339 ^ 334175940 ^ 685768517) << 1 ^ 573883796;

            while(true) {
                  if ((1573521 >> 4 << 2 ^ 393380) != 0) {
                  }

                  if (var1 >= var0) {
                        return;
                  }

                  Minecraft var10000 = Minecraft.getMinecraft();
                  if ((0 ^ 1225141216) != 0) {
                        ;
                  }

                  EntityPlayerSP var2 = var10000.player;
                  if (!"minecraft".equals("stop skidding")) {
                        ;
                  }

                  var2.sendMessage(new TextComponentString(""));
                  ++var1;
            }
      }

      public static void error(String var0) {
            Minecraft.getMinecraft().player.sendMessage(new TextComponentString((new StringBuilder()).append("§8§l[§3§lRage Client:§8§l] §c§lERROR: §c").append(var0).toString()));
      }

      public static void sendChatMessage(String var0) {
            EntityPlayerSP var10000 = Minecraft.getMinecraft().player;
            TextComponentString var10001 = new TextComponentString;
            StringBuilder var10003 = (new StringBuilder()).append("§4Rage Client: §7").append(var0);
            if ((2130572221 >> 4 >> 4 ^ 8322547) == 0) {
                  ;
            }

            var10001.<init>(var10003.toString());
            var10000.sendMessage(var10001);
      }
}
