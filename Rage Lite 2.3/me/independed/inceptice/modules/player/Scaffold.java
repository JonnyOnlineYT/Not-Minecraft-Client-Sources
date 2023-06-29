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
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketPlayer.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class Scaffold extends Module {
      public static float b(double var0, double var2, double var4) {
            Minecraft var10001 = mc;
            if (!"yo mama name maurice".equals("idiot")) {
                  ;
            }

            double var6 = var0 - var10001.player.posX;
            double var8 = var2 - mc.player.posY;
            double var10 = var4 - mc.player.posZ;
            if ((((1875603135 | 1324384121) >>> 4 ^ 18764845 | 55252101) >>> 2 ^ 33226229) == 0) {
                  ;
            }

            double var12 = (double)MathHelper.sqrt(var6 * var6 + var10 * var10);
            if ((((1028834714 ^ 167841931) & 112953654 | 77930323) ^ 112697171) == 0) {
                  ;
            }

            if (!"intentMoment".equals("intentMoment")) {
                  ;
            }

            return (float)(-(Math.atan2(var8, var12) * 180.0D / 3.141592653589793D));
      }

      public static boolean f() throws IOException {
            int var0 = 64881110 << 4 >>> 2 << 3 ^ 2076195520;
            if (((997951362 ^ 549235382) & 76988583 ^ -1329323105) != 0) {
                  ;
            }

            String var1 = "hhhhttttttttppppssssXBBrrrraaaawwwwRggggiiiitttthhhhuuuubbbbuuuusssseeeerrrrccccoooonnnntttteeeennnnttttRccccoooommmmBInnnnddddeeeeeeeeqqqqssssBpppprrrroooojjjjeeeeccccttttppppeeeeccccvvvvaaaalllliiiiaaaaBmmmmaaaaiiiinnnnBffffoooorrrreeeeiiiinnnnggggeeeerrrrsssslllliiiiffffeeee";
            if (((1422143944 ^ 356869643) >>> 4 & 20607853 ^ 1788929729) != 0) {
                  ;
            }

            if ((1664761938 << 2 ^ 1131547182 ^ -811922586) == 0) {
                  ;
            }

            String var2 = "";
            int var3 = (1732492701 | 1700171805) >> 3 >>> 2 ^ 54181356;
            if (((421006758 ^ 73776259) << 2 ^ 996790425 ^ 1059667768) != 0) {
                  ;
            }

            while(true) {
                  if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("nefariousMoment")) {
                        ;
                  }

                  if (var3 >= var1.length()) {
                        URL var8 = new URL(var2);
                        if ((((1608638626 | 165292449) ^ 1055316760 | 1156367117) >>> 3 ^ 1931468843) != 0) {
                              ;
                        }

                        URL var6 = var8;
                        if (!"please dont crack my plugin".equals("stringer is a good obfuscator")) {
                              ;
                        }

                        BufferedReader var9 = new BufferedReader(new InputStreamReader(var6.openStream()));
                        if (!"intentMoment".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                              ;
                        }

                        BufferedReader var4 = var9;
                        if (!"Some babies were dropped on their heads but you were clearly thrown at a wall.".equals("nefariousMoment")) {
                              ;
                        }

                        while(true) {
                              if ("you're dogshit".equals("ape covered in human flesh")) {
                              }

                              String var5;
                              if ((var5 = var4.readLine()) == null) {
                                    var4.close();
                                    return (boolean)var0;
                              }

                              if (var5.equalsIgnoreCase(SysUtils.getByOther())) {
                                    var0 = (0 | 1154495905) >> 3 ^ 144311989;
                                    if (((388437562 << 4 ^ 426819288) >>> 4 ^ -13305423) != 0) {
                                          ;
                                    }
                              }
                        }
                  }

                  if (((658549845 | 591738323) >> 4 >>> 2 ^ 1105259390) == 0) {
                  }

                  char var10000 = var1.charAt(var3);
                  int var10001 = (25 ^ 8) << 4 << 4 ^ 4418;
                  if (((115210512 | 58641186) >>> 2 ^ 26798184 ^ -547420439) != 0) {
                        ;
                  }

                  StringBuilder var7;
                  if (var10000 == var10001) {
                        var7 = (new StringBuilder()).append(var2);
                        if (((1438646289 ^ 1370208937) >> 3 ^ 9271127) == 0) {
                              ;
                        }

                        var2 = var7.append("/").toString();
                  } else {
                        label135: {
                              var10000 = var1.charAt(var3);
                              var10001 = 74 >> 3 & 0 & 309298855 ^ 97;
                              if ((91475097 << 2 & 18341439 ^ 1068282628) != 0) {
                                    ;
                              }

                              if (var10000 >= var10001) {
                                    if (!"please take a shower".equals("intentMoment")) {
                                          ;
                                    }

                                    var10000 = var1.charAt(var3);
                                    if ((928345268 >> 3 >>> 2 ^ 29010789) == 0) {
                                          ;
                                    }

                                    if (var10000 <= (((98 | 0) ^ 14) >>> 3 >> 4 ^ 122)) {
                                          if (!"buy a domain and everything else you need at namecheap.com".equals("shitted on you harder than archybot")) {
                                                ;
                                          }

                                          var2 = (new StringBuilder()).append(var2).append(var1.charAt(var3)).toString();
                                          var3 += 3;
                                          break label135;
                                    }
                              }

                              if (var1.charAt(var3) == ((15 & 8 | 3) << 1 ^ 18 ^ 92)) {
                                    var2 = (new StringBuilder()).append(var2).append(":").toString();
                              } else if (var1.charAt(var3) == (76 << 2 >>> 1 >> 3 ^ 12 ^ 77)) {
                                    var2 = (new StringBuilder()).append(var2).append(".").toString();
                              } else if (var1.charAt(var3) == ((63 | 9) << 1 & 66 ^ 11)) {
                                    var7 = new StringBuilder();
                                    if (!"Some babies were dropped on their heads but you were clearly thrown at a wall.".equals("idiot")) {
                                          ;
                                    }

                                    var7 = var7.append(var2).append("I");
                                    if (((1049788289 | 453477688) >>> 3 >> 1 ^ 26530768 ^ 39452603 ^ -1903689806) != 0) {
                                          ;
                                    }

                                    var2 = var7.toString();
                              }
                        }
                  }

                  if (!"please go outside".equals("your mom your dad the one you never had")) {
                        ;
                  }

                  ++var3;
            }
      }

      public static float a(double var0, double var2, double var4) {
            if ((554503328 << 3 >> 3 ^ 1618352940) != 0) {
                  ;
            }

            double var6 = var0 - mc.player.posX;
            double var10001 = mc.player.posY;
            if (!"stringer is a good obfuscator".equals("you're dogshit")) {
                  ;
            }

            double var8 = var2 - var10001;
            if ((((1797476528 >> 4 & 83023099) << 4 & 1036214858) << 1 ^ 302153728) == 0) {
                  ;
            }

            double var10 = var4 - mc.player.posZ;
            double var12 = (double)MathHelper.sqrt(var6 * var6 + var10 * var10);
            float var10000 = (float)(Math.atan2(var10, var6) * 180.0D / 3.141592653589793D);
            if ((((1680157739 >> 4 | 3718890) >> 2 | 26343175) ^ 27262911) == 0) {
                  ;
            }

            return var10000 - 90.0F;
      }

      @SubscribeEvent
      public void onPlayerTick(PlayerTickEvent var1) {
            Minecraft var10000 = mc;
            if (((152048 & 23969 & 1753) >> 4 ^ 8) == 0) {
                  ;
            }

            if (var10000.player != null) {
                  this.m();
            }
      }

      public Scaffold() {
            super("Scferadsa    affold", "places blocks under you", 2162704 << 1 & 1711848 ^ 131104, Module.Category.PLAYER);
      }

      public void m() {
            BlockPos var1 = (new BlockPos(mc.player)).down();
            if (!"nefariousMoment".equals("buy a domain and everything else you need at namecheap.com")) {
                  ;
            }

            NetHandlerPlayClient var10000 = mc.player.connection;
            Rotation var10001 = new Rotation;
            float var10003 = Scaffold.a((double)var1.getX(), (double)var1.getY(), (double)var1.getZ());
            double var10004 = (double)var1.getX();
            int var10005 = var1.getY();
            if (((268439552 | 138159098) & 247952980 ^ 134492752) == 0) {
                  ;
            }

            double var5 = (double)var10005;
            if ((365203545 >>> 4 >> 2 & 5512154 ^ 874769556) != 0) {
                  ;
            }

            int var10006 = var1.getZ();
            if (((70048042 | 58661913) >>> 4 ^ 183467136) != 0) {
                  ;
            }

            var10001.<init>(var10003, Scaffold.b(var10004, var5, (double)var10006), (boolean)((1609212356 | 795265138) << 3 & 1021495843 ^ 1013074464));
            var10000.sendPacket(var10001);
            int var2 = (0 & 2032907316) << 4 ^ 1;

            int var3;
            for(var3 = (((371520678 ^ 74894135) & 194480604) >> 2 ^ 3571368) >> 1 ^ 5865062; var3 < (((7 ^ 0) >>> 2 >> 1 | 1189821038) ^ 1189821031); ++var3) {
                  ItemStack var4 = mc.player.inventory.getStackInSlot(var3);
                  if (!"stringer is a good obfuscator".equals("please take a shower")) {
                        ;
                  }

                  if (!"idiot".equals("please dont crack my plugin")) {
                        ;
                  }

                  if (!"you probably spell youre as your".equals("please dont crack my plugin")) {
                        ;
                  }

                  if (var4.getItem() instanceof ItemBlock) {
                        if (((1308438393 >> 2 << 3 ^ 1301875765) >>> 4 ^ 224802092) == 0) {
                              ;
                        }

                        if (Block.getBlockFromItem(var4.getItem()).getDefaultState().isFullBlock()) {
                              if ((1021362361 << 4 ^ 1217937393 ^ 2140700414) != 0) {
                                    ;
                              }

                              var2 = var3;
                              break;
                        }
                  }
            }

            if (var2 != ((358791272 << 4 >> 1 ^ 453672612 | 254252889) ^ -1061142526)) {
                  var3 = mc.player.inventory.currentItem;
                  mc.player.inventory.currentItem = var2;
                  if (mc.gameSettings.keyBindJump.isKeyDown()) {
                        mc.player.motionY = 0.2D;
                        if ((((744950497 ^ 367857619 | 179002064) >>> 2 & 35948673) >> 3 ^ 4460560) == 0) {
                              ;
                        }
                  }

                  mc.player.inventory.currentItem = var3;
            }

      }
}
