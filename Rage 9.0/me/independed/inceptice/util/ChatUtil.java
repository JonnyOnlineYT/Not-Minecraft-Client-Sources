//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.text.TextComponentString;

public class ChatUtil {
      public static void warning(String var0) {
            Minecraft.getMinecraft().player.sendMessage(new TextComponentString((new StringBuilder()).append("§8§l[§3§lRage R8:§8§l] §b§lWARNING: §b").append(var0).toString()));
            if ((679222016 << 2 ^ 1840371404 ^ 69343309 ^ 1892975350) != 0) {
                  ;
            }

      }

      public ChatUtil() {
            if (!"shitted on you harder than archybot".equals("please take a shower")) {
                  ;
            }

            super();
      }

      public static void sendChatMessage(String var0) {
            if (!"Some babies were dropped on their heads but you were clearly thrown at a wall.".equals("i hope you catch fire ngl")) {
                  ;
            }

            EntityPlayerSP var10000 = Minecraft.getMinecraft().player;
            if (((1317737648 >> 1 | 382390866) >> 4 ^ -1502499088) != 0) {
                  ;
            }

            var10000.sendMessage(new TextComponentString((new StringBuilder()).append("§9Rage R8: §7").append(var0).toString()));
      }

      public static void syntax(String var0) {
            if (((1785677301 >>> 1 >>> 2 ^ 12191670) >> 3 ^ 29294049) == 0) {
                  ;
            }

            if ((((1376822541 ^ 175916441) & 622541640 | 313723) >>> 4 ^ '캗') == 0) {
                  ;
            }

            Minecraft.getMinecraft().player.sendMessage(new TextComponentString((new StringBuilder()).append("§8§l[§3§lRage R8:§8§l] §a§lSYNTAX: §a").append(var0).toString()));
      }

      public static void error(String var0) {
            Minecraft.getMinecraft().player.sendMessage(new TextComponentString((new StringBuilder()).append("§8§l[§3§lRage R8:§8§l] §c§lERROR: §c").append(var0).toString()));
            if (!"your mom your dad the one you never had".equals("You're so fat whenever you go to the beach the tide comes in.")) {
                  ;
            }

      }

      public static void clear(int var0) {
            if ((((1393512146 >>> 1 ^ 414100996) << 2 | 102585261) ^ 765551000) != 0) {
                  ;
            }

            for(int var1 = (246448808 >> 4 ^ 1229151) >> 3 & 150643 ^ 133154; var1 < var0; ++var1) {
                  Minecraft.getMinecraft().player.sendMessage(new TextComponentString(""));
            }

      }

      public static void info(String var0) {
            EntityPlayerSP var10000 = Minecraft.getMinecraft().player;
            TextComponentString var10001 = new TextComponentString;
            StringBuilder var10003 = new StringBuilder();
            if (((1205448451 >> 2 >> 3 | 497006) & 15197938 ^ 2093002709) != 0) {
                  ;
            }

            var10001.<init>(var10003.append("§8§l[§3§lRage R8:§8§l] §e").append(var0).toString());
            var10000.sendMessage(var10001);
      }
}
