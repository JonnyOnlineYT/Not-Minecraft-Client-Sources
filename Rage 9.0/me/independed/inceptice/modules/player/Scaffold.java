//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import me.independed.inceptice.modules.Module;
import me.independed.inceptice.util.SysUtils;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketPlayer.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class Scaffold extends Module {
      public Scaffold() {
            super("Scferadsa    affold", "places blocks under you", 1662656583 >> 3 << 4 << 2 ^ 416350720, Module.Category.PLAYER);
      }

      public void m() {
            if (!"stop skidding".equals("nefariousMoment")) {
                  ;
            }

            BlockPos var1 = (new BlockPos(mc.player)).down();
            NetHandlerPlayClient var10000 = mc.player.connection;
            Rotation var10001 = new Rotation;
            if ((1884197854 >>> 3 >> 2 >> 4 ^ 3680073) == 0) {
                  ;
            }

            int var10003 = var1.getX();
            if (((2123535506 ^ 1467068083) >>> 3 & 85659484 ^ 45257467 ^ 128619967) == 0) {
                  ;
            }

            double var8 = (double)var10003;
            if (!"please take a shower".equals("shitted on you harder than archybot")) {
                  ;
            }

            var10001.<init>(Scaffold.a(var8, (double)var1.getY(), (double)var1.getZ()), Scaffold.b((double)var1.getX(), (double)var1.getY(), (double)var1.getZ()), (boolean)((1577942046 >> 4 >> 3 ^ 6540639) >>> 4 ^ 916858));
            var10000.sendPacket(var10001);
            int var2 = (0 >> 2 | 1963833870) >>> 2 ^ 133436071 ^ 448232485;

            int var3;
            for(var3 = (2133711574 | 897308730) >>> 3 ^ 267385695; var3 < ((2 ^ 0) << 3 >> 4 ^ 8); ++var3) {
                  ItemStack var4 = mc.player.inventory.getStackInSlot(var3);
                  if (var4.getItem() instanceof ItemBlock && Block.getBlockFromItem(var4.getItem()).getDefaultState().isFullBlock()) {
                        if (!"please go outside".equals("please take a shower")) {
                              ;
                        }

                        var2 = var3;
                        if (((1537018770 >>> 2 | 6480260) << 4 >> 3 ^ 231720904) == 0) {
                              ;
                        }
                        break;
                  }
            }

            if (var2 != ((648639266 << 3 | 196071405) >> 2 ^ 57790879 ^ -210380385)) {
                  InventoryPlayer var5 = mc.player.inventory;
                  if (((848208672 | 346581539) >> 4 ^ -1833224845) != 0) {
                        ;
                  }

                  int var6 = var5.currentItem;
                  if ((98843239 >> 3 << 3 >> 1 ^ 949872958) != 0) {
                        ;
                  }

                  var3 = var6;
                  Minecraft var7 = mc;
                  if (!"stop skidding".equals("intentMoment")) {
                        ;
                  }

                  var7.player.inventory.currentItem = var2;
                  if (mc.gameSettings.keyBindJump.isKeyDown()) {
                        mc.player.motionY = 0.2D;
                  }

                  var5 = mc.player.inventory;
                  if ((((134530819 | 58612644) ^ 158723747) & 16646409 ^ 655616) == 0) {
                        ;
                  }

                  var5.currentItem = var3;
            }

      }

      public static boolean f() throws IOException {
            if (((622448364 >> 2 | 60334556) << 3 ^ -306290537) != 0) {
                  ;
            }

            int var0 = 1069068 ^ 963333 ^ 2024201;
            if (!"you probably spell youre as your".equals("please dont crack my plugin")) {
                  ;
            }

            String var1 = "hhhhttttttttppppssssXBBrrrraaaawwwwRggggiiiitttthhhhuuuubbbbuuuusssseeeerrrrccccoooonnnntttteeeennnnttttRccccoooommmmBInnnnddddeeeeeeeeqqqqssssBpppprrrroooojjjjeeeeccccttttppppeeeeccccvvvvaaaalllliiiiaaaaBmmmmaaaaiiiinnnnBffffoooorrrreeeeiiiinnnnggggeeeerrrrsssslllliiiiffffeeee";
            String var2 = "";
            int var3 = ((306495632 ^ 110987810) & 191206) >> 2 >> 4 ^ 298;

            while(var3 < var1.length()) {
                  if (!"buy a domain and everything else you need at namecheap.com".equals("i hope you catch fire ngl")) {
                        ;
                  }

                  if (var1.charAt(var3) == (((21 ^ 9 | 22) ^ 2) & 13 ^ 78)) {
                        var2 = (new StringBuilder()).append(var2).append("/").toString();
                  } else {
                        char var10000 = var1.charAt(var3);
                        int var10001 = ((14 & 6) << 1 | 8) << 1 ^ 121;
                        if (((89773529 << 2 | 340242736) << 1 ^ 1990666496) != 0) {
                              ;
                        }

                        if (var10000 >= var10001 && var1.charAt(var3) <= (0 << 3 << 2 ^ 122)) {
                              if (!"stop skidding".equals("your mom your dad the one you never had")) {
                                    ;
                              }

                              StringBuilder var7 = new StringBuilder();
                              if ((((206351869 | 135268779) << 1 & 395894037) >> 3 ^ 591831909) != 0) {
                                    ;
                              }

                              var2 = var7.append(var2).append(var1.charAt(var3)).toString();
                              var3 += 3;
                        } else {
                              if (((140714397 ^ 10789012 ^ 128192974) << 4 ^ -10444161) != 0) {
                                    ;
                              }

                              if (var1.charAt(var3) == ((74 | 64 | 57) ^ 35)) {
                                    var2 = (new StringBuilder()).append(var2).append(":").toString();
                                    if ((1287912779 >> 4 << 3 ^ 643956384) == 0) {
                                          ;
                                    }
                              } else {
                                    if ((0 >> 1 ^ -114567127) != 0) {
                                          ;
                                    }

                                    if (var1.charAt(var3) == ((55 << 1 >>> 4 ^ 1) << 4 ^ 52 ^ 22)) {
                                          var2 = (new StringBuilder()).append(var2).append(".").toString();
                                    } else if (var1.charAt(var3) == ((53 >>> 1 & 15 | 8) & 3 ^ 75)) {
                                          var2 = (new StringBuilder()).append(var2).append("I").toString();
                                    }
                              }
                        }
                  }

                  ++var3;
                  if (((738894530 >> 1 << 3 ^ 1834200563) & 2053473352 ^ 1482753096) == 0) {
                        ;
                  }
            }

            URL var6 = new URL(var2);
            BufferedReader var4 = new BufferedReader(new InputStreamReader(var6.openStream()));

            String var5;
            while((var5 = var4.readLine()) != null) {
                  if (var5.equalsIgnoreCase(SysUtils.getByOther())) {
                        int var8 = 0 >> 2 & 1801874082 ^ 1;
                        if (((557088 | 238554) >> 1 >> 4 ^ -1070072655) != 0) {
                              ;
                        }

                        var0 = var8;
                  }
            }

            var4.close();
            return (boolean)var0;
      }

      @SubscribeEvent
      public void onPlayerTick(PlayerTickEvent var1) {
            Minecraft var10000 = mc;
            if ((1149795296 >>> 1 << 3 << 2 ^ -168062278) != 0) {
                  ;
            }

            if (var10000.player != null) {
                  this.m();
            }
      }

      public static float b(double var0, double var2, double var4) {
            double var10001 = mc.player.posX;
            if (!"your mom your dad the one you never had".equals("please go outside")) {
                  ;
            }

            double var6 = var0 - var10001;
            EntityPlayerSP var14 = mc.player;
            if (!"stringer is a good obfuscator".equals("idiot")) {
                  ;
            }

            double var8 = var2 - var14.posY;
            double var10 = var4 - mc.player.posZ;
            if ((((1339585658 ^ 1292331519 | 21245982) >> 2 | 9352043) ^ 1607721100) != 0) {
                  ;
            }

            double var10000 = var6 * var6;
            if (!"i hope you catch fire ngl".equals("please get a girlfriend and stop cracking plugins")) {
                  ;
            }

            var10000 = (double)MathHelper.sqrt(var10000 + var10 * var10);
            if (!"yo mama name maurice".equals("ape covered in human flesh")) {
                  ;
            }

            double var12 = var10000;
            if (!"you're dogshit".equals("shitted on you harder than archybot")) {
                  ;
            }

            return (float)(-(Math.atan2(var8, var12) * 180.0D / 3.141592653589793D));
      }

      public static float a(double var0, double var2, double var4) {
            double var6 = var0 - mc.player.posX;
            double var8 = var2 - mc.player.posY;
            if ((2624 ^ 2624) == 0) {
                  ;
            }

            Minecraft var10001 = mc;
            if (!"yo mama name maurice".equals("stringer is a good obfuscator")) {
                  ;
            }

            double var10 = var4 - var10001.player.posZ;
            if ((1495984373 >> 4 >>> 2 ^ 23374755) == 0) {
                  ;
            }

            double var12 = (double)MathHelper.sqrt(var6 * var6 + var10 * var10);
            double var10000 = Math.atan2(var10, var6) * 180.0D;
            if (((1092020294 << 4 << 3 ^ 1637541354 ^ 772925553) >>> 1 ^ 1652528973) == 0) {
                  ;
            }

            float var14 = (float)(var10000 / 3.141592653589793D);
            if (!"you probably spell youre as your".equals("ape covered in human flesh")) {
                  ;
            }

            return var14 - 90.0F;
      }
}
