//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.misc;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import me.independed.inceptice.modules.Module;
import me.independed.inceptice.settings.ModeSetting;
import me.independed.inceptice.util.TimerUtil;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class Spammer extends Module {
      public ModeSetting mode;
      public List targets = null;
      Random random;
      int cnt;
      TimerUtil timerUtil = new TimerUtil();
      public static ArrayList messages = new ArrayList();

      public List getTargets() {
            return (List)mc.world.loadedEntityList.stream().filter((var0) -> {
                  return (boolean)(var0 != mc.player ? (0 & 740146153) << 2 & 927541787 ^ 1 : 1229593748 << 1 & 1393553271 ^ 302262560);
            }).filter((var0) -> {
                  return (boolean)(mc.player.getDistance(var0) <= 1.0E8F ? (0 << 3 | 722067956) >> 2 ^ 37434305 ^ 150555581 : (1581741941 | 573388305) >>> 1 & 15369187 ^ 2261922);
            }).filter((var0) -> {
                  return var0 instanceof EntityPlayer;
            }).collect(Collectors.toList());
      }

      @SubscribeEvent
      public void onPlayerTick(PlayerTickEvent var1) {
            if (mc.player != null) {
                  boolean var10000 = mc.player.isDead;
                  if ((1594983589 >> 4 << 2 ^ 398745896) == 0) {
                        ;
                  }

                  if (!var10000) {
                        ModeSetting var2 = this.mode;
                        if (!"idiot".equals("you're dogshit")) {
                              ;
                        }

                        int var10001;
                        if (var2.activeMode == "MSG") {
                              if (this.timerUtil.hasReached(9000.0D)) {
                                    this.targets = this.getTargets();
                                    this.timerUtil.reset();
                              }

                              if (!"stop skidding".equals("please dont crack my plugin")) {
                                    ;
                              }

                              if (this.targets.size() > 0) {
                                    if (((134290507 | 74232276) & 208449893 ^ -1692229265) != 0) {
                                          ;
                                    }

                                    var10000 = this.timerUtil.hasReached((double)((412 << 4 ^ 3304 ^ 5824) + this.random.nextInt((24 | 14 | 7) >>> 4 ^ 369)));
                                    if ((1377145721 ^ 315294368 ^ 744409364 ^ -926289232) != 0) {
                                          ;
                                    }

                                    if (var10000) {
                                          int var3 = this.cnt;
                                          var10001 = this.targets.size();
                                          if (((310397345 >>> 1 >>> 4 >>> 3 | 287949) ^ 1930759952) != 0) {
                                                ;
                                          }

                                          if (var3 == var10001) {
                                                if ((270130924 << 2 >>> 1 >>> 2 ^ 132013050 ^ 868918303) != 0) {
                                                      ;
                                                }

                                                this.cnt = ((1703992040 ^ 1055356956) >> 2 ^ 287170098) >>> 1 ^ 65018695;
                                          }

                                          List var5 = this.targets;
                                          int var10004 = this.cnt;
                                          this.cnt = var10004 + ((0 | 871779117) << 3 ^ -1615701655);
                                          this.sendingFor((Entity)var5.get(var10004));
                                          this.timerUtil.reset();
                                    }
                              }
                        } else {
                              TimerUtil var4 = this.timerUtil;
                              if (!"please go outside".equals("stop skidding")) {
                                    ;
                              }

                              var10001 = this.random.nextInt((214 ^ 1) >> 2 >> 2 & 5 ^ 497) + (((4516 | 3565) & 686) << 3 ^ 5833);
                              if ((599716314 >>> 1 << 1 << 3 ^ 815057030) != 0) {
                                    ;
                              }

                              if (var4.hasReached((double)var10001)) {
                                    if (this.cnt == messages.size()) {
                                          if ((((1014640874 | 975612316) << 1 | 394154893) ^ -1089500195) != 0) {
                                                ;
                                          }

                                          this.cnt = 1566744591 >>> 4 ^ 13056371 ^ 85005171;
                                    }

                                    StringBuilder var6 = (new StringBuilder()).append("[");
                                    char var10002 = (char)(this.random.nextInt(2 >>> 1 << 2 ^ 13) + (((33 | 13) ^ 29) >>> 2 ^ 60));
                                    if (((1364739623 | 1256965291) >>> 3 ^ 192903381) == 0) {
                                          ;
                                    }

                                    var6 = var6.append(var10002).append((char)(this.random.nextInt(((6 | 0) << 1 ^ 8 | 3) ^ 14) + ((2 >>> 3 >>> 3 ^ 1340354541) << 3 ^ 2132901720)));
                                    Random var7 = this.random;
                                    int var10003 = (18 >>> 2 >> 4 >>> 1 | 1697148216) ^ 1697148198;
                                    if (((745672958 >>> 1 | 61460389) << 3 >>> 4 ^ 199026687) == 0) {
                                          ;
                                    }

                                    var6 = var6.append((char)(var7.nextInt(var10003) + (((50 ^ 9) >> 2 | 5) << 1 ^ 23 ^ 72))).append((char)(this.random.nextInt((26 >> 4 | 0 | 0) >>> 2 ^ 30) + ((23 | 20) ^ 14 ^ 120)));
                                    var7 = this.random;
                                    var10003 = (19 << 1 | 16) & 14 ^ 24;
                                    if (!"nefariousMoment".equals("please take a shower")) {
                                          ;
                                    }

                                    int var8 = var7.nextInt(var10003) + (46 << 2 << 1 ^ 305);
                                    if ((1125687863 << 1 << 3 << 2 ^ 892920673) != 0) {
                                          ;
                                    }

                                    var6 = var6.append((char)var8);
                                    var8 = this.random.nextInt((28 | 5 | 5) >> 2 << 3 ^ 38);
                                    if (((1328973294 | 410039760 | 71294350) >> 4 ^ -1024099811) != 0) {
                                          ;
                                    }

                                    var6 = var6.append((char)(var8 + ((81 ^ 28 ^ 65) << 3 ^ 1)));
                                    if (((261994789 ^ 165351610) >> 4 >>> 1 ^ 3290444) == 0) {
                                          ;
                                    }

                                    var6 = var6.append((char)(this.random.nextInt((11 >>> 1 | 2) & 1 ^ 0 ^ 31) + ((17 ^ 7 | 4) >>> 4 ^ 96)));
                                    var7 = this.random;
                                    if (((497465662 << 3 >> 1 ^ 602107326) << 3 >>> 4 ^ 180143267) == 0) {
                                          ;
                                    }

                                    var6 = var6.append((char)(var7.nextInt(((2 ^ 1) & 1) >>> 2 ^ 1170958249 ^ 1170958240) + (((3 ^ 2) & 0) << 4 ^ 48))).append("]");
                                    ArrayList var9 = messages;
                                    int var10005 = this.cnt;
                                    this.cnt = var10005 + (0 ^ 125715686 ^ 106059301 ^ 11001582 ^ 25938988);
                                    var6 = var6.append((String)var9.get(var10005)).append("[").append((char)(this.random.nextInt((2 | 1) << 3 ^ 17) + ((1 >>> 1 | 725373839 | 661518243) >>> 2 ^ 199196123))).append((char)(this.random.nextInt(3 >>> 1 & 0 ^ 113743564 ^ 113743557) + ((32 | 10) ^ 19 ^ 9)));
                                    if (!"i hope you catch fire ngl".equals("please get a girlfriend and stop cracking plugins")) {
                                          ;
                                    }

                                    var7 = this.random;
                                    var10003 = (8 | 6) >> 4 ^ 30;
                                    if (!"please go outside".equals("idiot")) {
                                          ;
                                    }

                                    this.sending(var6.append((char)(var7.nextInt(var10003) + ((18 >> 4 << 2 << 4 & 7 | 1595311541) ^ 1595311604))).append((char)(this.random.nextInt(7 << 4 >> 4 >>> 4 & 130199019 ^ 30) + (89 >>> 4 >> 1 & 0 ^ 97))).append((char)(this.random.nextInt((16 & 9 ^ 262904951) >> 4 ^ 16431577) + (((5 ^ 2) << 1 ^ 3 | 2) ^ 78))).append((char)(this.random.nextInt(((0 ^ 1313880270) << 4 & 1902563478 | 1511321441) ^ 2065100799) + ((42 | 26) << 4 >> 1 ^ 433))).append((char)(this.random.nextInt(((4 ^ 0) >> 4 | 52845606) ^ 52845624) + (53 << 1 ^ 79 ^ 31 ^ 91))).append((char)(this.random.nextInt(2 >>> 3 >> 4 >> 3 >> 2 ^ 9) + ((47 ^ 13) & 25 ^ 48))).append("]").toString());
                                    this.timerUtil.reset();
                              }
                        }

                        return;
                  }
            }

      }

      public Spammer() {
            super("Spammer", "spams messages in chat", (1106219607 | 32820525) >> 2 ^ 276821983, Module.Category.WORLD);
            Random var10001 = new Random();
            if ((1744752479 >>> 1 & 270854682 & 270582422 ^ 270549506) == 0) {
                  ;
            }

            this.random = var10001;
            this.cnt = 1295182521 >>> 4 >> 3 >> 3 << 2 ^ 5059304;
            String[] var10006 = new String[(1 ^ 0) >> 2 & 1960095189 ^ 2];
            var10006[(1727366665 >>> 3 | 144889918) >> 2 ^ 54509119] = "Global";
            var10006[0 << 1 >>> 4 & 51338688 ^ 1] = "MSG";
            this.mode = new ModeSetting("Mode", this, "Global", var10006);
            messages.add("[Rage R9] - skidded nigga shit 'noom#0681'");
            messages.add("[Rage R9] - The shittest Hack 'noom#0681'");
      }

      public void sendingFor(Entity var1) {
            if (((231613942 | 87485126) & 183098722 ^ 641382766) != 0) {
                  ;
            }

            mc.player.sendChatMessage((new StringBuilder()).append("/msg ").append(var1.getName()).append(" get best HACK, get 'RAGE R8' vk - 'vk.com/rage_r8'").toString());
      }

      public void sending(String var1) {
            EntityPlayerSP var10000 = mc.player;
            StringBuilder var10001 = new StringBuilder();
            if (((700158687 << 4 & 2102576986) << 1 ^ -486231332) != 0) {
                  ;
            }

            var10001 = var10001.append("!");
            if ((8919115 >> 3 >>> 1 & 8955 ^ 41 ^ 169) == 0) {
                  ;
            }

            var10000.sendChatMessage(var10001.append(var1).toString());
      }
}
