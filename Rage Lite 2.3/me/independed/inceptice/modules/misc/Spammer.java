//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.misc;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import me.independed.inceptice.modules.Module;
import me.independed.inceptice.settings.ModeSetting;
import me.independed.inceptice.util.TimerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class Spammer extends Module {
      Random random;
      int cnt;
      public List targets = null;
      public ModeSetting mode;
      TimerUtil timerUtil;
      public static ArrayList messages;

      public void sendingFor(Entity var1) {
            mc.player.sendChatMessage((new StringBuilder()).append("/msg ").append(var1.getName()).append(" get best HACK, get 'RAGE R8' vk - 'vk.com/rage_r8'").toString());
            if (((67255483 >> 2 >>> 3 | 232857) >>> 2 << 3 ^ -270884490) != 0) {
                  ;
            }

      }

      @SubscribeEvent
      public void onPlayerTick(PlayerTickEvent var1) {
            EntityPlayerSP var10000 = mc.player;
            if (((376824959 | 59822340) ^ 155707149 ^ 443926148 ^ -386727524) != 0) {
                  ;
            }

            if (var10000 != null && !mc.player.isDead) {
                  TimerUtil var2;
                  int var10001;
                  Random var10002;
                  int var10003;
                  if (this.mode.activeMode == "MSG") {
                        var2 = this.timerUtil;
                        if (((187845632 | 68162005) << 2 << 4 ^ -862489280) == 0) {
                              ;
                        }

                        if (var2.hasReached(9000.0D)) {
                              this.targets = this.getTargets();
                              this.timerUtil.reset();
                        }

                        if (this.targets.size() > 0) {
                              var2 = this.timerUtil;
                              var10001 = ((396 ^ 312) << 4 | 2360) & 2413 ^ 2688;
                              var10002 = this.random;
                              var10003 = (267 | 235) << 3 >>> 4 ^ 389;
                              if (!"stringer is a good obfuscator".equals("buy a domain and everything else you need at namecheap.com")) {
                                    ;
                              }

                              double var3 = (double)(var10001 + var10002.nextInt(var10003));
                              if ((926951682 << 3 ^ 1295545527 ^ -147243865) == 0) {
                                    ;
                              }

                              if (var2.hasReached(var3)) {
                                    if (this.cnt == this.targets.size()) {
                                          this.cnt = 2574654 ^ 1942580 ^ 3861770;
                                    }

                                    List var4 = this.targets;
                                    int var10004 = this.cnt;
                                    this.cnt = var10004 + (0 >> 2 & 978546450 ^ 1);
                                    Entity var5 = (Entity)var4.get(var10004);
                                    if ((750098581 << 3 & 213106741 & 77322079 ^ 75546624) == 0) {
                                          ;
                                    }

                                    this.sendingFor(var5);
                                    this.timerUtil.reset();
                              }
                        }
                  } else {
                        var2 = this.timerUtil;
                        var10001 = this.random.nextInt(((175 | 166) >> 2 | 22) ^ 459);
                        int var6 = 3106 << 4 & 5488 ^ 9 ^ 4992;
                        if (((268452928 | 34897443) << 1 & 429430880 ^ -1254976803) != 0) {
                              ;
                        }

                        if (var2.hasReached((double)(var10001 + var6))) {
                              if (((894566466 | 17175632) >> 4 >>> 4 ^ -489383763) != 0) {
                                    ;
                              }

                              if (this.cnt == messages.size()) {
                                    this.cnt = (467219712 >>> 3 ^ 21677021) >> 1 ^ 18412222;
                              }

                              if ((((1340657029 | 615377034) ^ 1758588727) >>> 4 ^ 1507342084) != 0) {
                                    ;
                              }

                              StringBuilder var7 = new StringBuilder();
                              if (((940007498 >>> 2 ^ 150714661) >> 4 ^ 7317011) == 0) {
                                    ;
                              }

                              var7 = var7.append("[");
                              var6 = this.random.nextInt(5 & 4 & 3 ^ 9) + ((1 >> 3 << 2 | 2024683677) ^ 2024683693);
                              if (!"idiot".equals("please get a girlfriend and stop cracking plugins")) {
                                    ;
                              }

                              var7 = var7.append((char)var6);
                              var10002 = this.random;
                              if (!"shitted on you harder than archybot".equals("buy a domain and everything else you need at namecheap.com")) {
                                    ;
                              }

                              var7 = var7.append((char)(var10002.nextInt((2 >> 2 << 3 & 1976485739) << 4 ^ 9) + (((8 ^ 0) >> 4 & 319281995 | 1881524674) ^ 1881524722)));
                              var10002 = this.random;
                              var10003 = (18 ^ 14 | 9) ^ 3;
                              if ((((891094576 >> 3 | 76981473 | 100477333) << 3 | 203813590) ^ 1073610750) == 0) {
                                    ;
                              }

                              var7 = var7.append((char)(var10002.nextInt(var10003) + ((18 ^ 14) << 3 ^ 161)));
                              if (((73984 >> 1 | 8632) ^ 23658 ^ '\uedd2') == 0) {
                                    ;
                              }

                              var10002 = this.random;
                              if (((458786 ^ '鈸') << 2 >> 1 ^ 992308) == 0) {
                                    ;
                              }

                              var7 = var7.append((char)(var10002.nextInt(7 << 3 >> 2 >>> 3 ^ 0 ^ 31) + (14 >> 2 << 2 >> 4 ^ 97)));
                              var6 = this.random.nextInt((23 ^ 20) >> 4 ^ 30);
                              if ((((324246225 | 34350094) ^ 58287841) >>> 2 ^ 944993794) != 0) {
                                    ;
                              }

                              var10003 = ((35 & 17) << 2 >>> 4 | 1335273462) ^ 1335273399;
                              if (((703906852 | 311234829 | 942211504 | 769491170 | 305463101) ^ 277567216) != 0) {
                                    ;
                              }

                              var7 = var7.append((char)(var6 + var10003));
                              var10002 = this.random;
                              if (((1445227464 >>> 4 | 87908847) & 17406128 ^ -1345492121) != 0) {
                                    ;
                              }

                              var7 = var7.append((char)(var10002.nextInt(18 << 1 << 1 ^ 2 ^ 84) + (9 >>> 3 >> 3 & 1360469068 ^ 1727757323 ^ 1727757418)));
                              var6 = this.random.nextInt((28 | 16) & 12 ^ 0 ^ 7 ^ 21);
                              if (((1725427586 << 3 >>> 1 | 137473582) ^ 99330704 ^ -375844131) != 0) {
                                    ;
                              }

                              var7 = var7.append((char)(var6 + ((28 & 23) >> 3 ^ 99)));
                              if (((8388736 ^ 7176068) & 7479736 ^ 672171951) != 0) {
                                    ;
                              }

                              var10002 = this.random;
                              var10003 = (8 ^ 1) >>> 2 >> 2 ^ 9;
                              if ((1255177292 >> 2 >> 4 ^ 19612145) == 0) {
                                    ;
                              }

                              var6 = var10002.nextInt(var10003) + (((8 ^ 3) & 5 | 0) & 0 ^ 48);
                              if (!"you probably spell youre as your".equals("you're dogshit")) {
                                    ;
                              }

                              var7 = var7.append((char)var6);
                              if ((17912704 >>> 4 ^ -1026966957) != 0) {
                                    ;
                              }

                              var7 = var7.append("]");
                              ArrayList var8 = messages;
                              int var10005 = this.cnt;
                              this.cnt = var10005 + ((0 << 1 >> 2 | 1848481651 | 1614073567 | 148182208) ^ 1862131710);
                              var7 = var7.append((String)var8.get(var10005));
                              if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("you're dogshit")) {
                                    ;
                              }

                              var7 = var7.append("[");
                              var6 = this.random.nextInt((5 << 1 >>> 4 ^ 956605 | 414890) ^ 670872 ^ 319534);
                              if (!"nefariousMoment".equals("minecraft")) {
                                    ;
                              }

                              var7 = var7.append((char)(var6 + ((10 << 1 | 14 | 25) ^ 47))).append((char)(this.random.nextInt((6 | 1 | 3) >>> 3 >>> 4 ^ 9) + ((30 << 4 | 5) >> 2 ^ 73)));
                              char var9 = (char)(this.random.nextInt(23 >> 2 >> 2 << 1 ^ 28) + ((18 << 2 & 24 ^ 2 | 6) ^ 79));
                              if (!"please go outside".equals("please get a girlfriend and stop cracking plugins")) {
                                    ;
                              }

                              var7 = var7.append(var9);
                              if ((((68227548 | 62610464) >> 4 | 2545426) & 1908648 ^ 743955517) != 0) {
                                    ;
                              }

                              var7 = var7.append((char)(this.random.nextInt((8 >>> 2 >> 3 | 811000486) ^ 811000504) + ((52 ^ 30 ^ 36) >> 4 << 1 ^ 97))).append((char)(this.random.nextInt((9 ^ 7 ^ 3) >>> 2 ^ 29) + ((39 | 24) & 18 & 15 ^ 1 ^ 66)));
                              var10002 = this.random;
                              var10003 = (14 ^ 8 ^ 5) << 4 ^ 5 ^ 43;
                              if (((301321804 << 1 & 123065190) << 3 & 315526612 ^ 12370881) != 0) {
                                    ;
                              }

                              var6 = var10002.nextInt(var10003) + (73 >> 3 & 0 ^ 97);
                              if (!"you probably spell youre as your".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                                    ;
                              }

                              this.sending(var7.append((char)var6).append((char)(this.random.nextInt((6 & 4) >>> 1 >>> 4 >> 2 ^ 30) + (61 << 3 >> 1 >>> 2 ^ 57 ^ 101))).append((char)(this.random.nextInt(((6 | 4) ^ 5) & 0 ^ 9) + (18 >>> 4 << 4 & 6 ^ 48))).append("]").toString());
                              this.timerUtil.reset();
                        }
                  }

            }
      }

      static {
            ArrayList var10000 = new ArrayList;
            if (((925164383 | 54596826) << 2 ^ -1843873770) != 0) {
                  ;
            }

            var10000.<init>();
            messages = var10000;
      }

      public void sending(String var1) {
            Minecraft var10000 = mc;
            if ((93719361 << 2 >> 4 << 4 ^ 462853004) != 0) {
                  ;
            }

            var10000.player.sendChatMessage((new StringBuilder()).append("!").append(var1).toString());
      }

      public List getTargets() {
            Stream var10000 = mc.world.loadedEntityList.stream();
            Predicate var10001 = (var0) -> {
                  if (!"please dont crack my plugin".equals("shitted on you harder than archybot")) {
                        ;
                  }

                  int var10000;
                  if (var0 != mc.player) {
                        if (((855332472 >>> 4 ^ 44935029) << 1 & 4649898 ^ 1094188307) != 0) {
                              ;
                        }

                        var10000 = 0 >> 4 << 4 ^ 1;
                  } else {
                        var10000 = 1125278080 << 2 ^ 206145024;
                  }

                  return (boolean)var10000;
            };
            if (((591247595 | 117779800) << 2 & 350788489 ^ -1752618114) != 0) {
                  ;
            }

            var10000 = var10000.filter(var10001).filter((var0) -> {
                  return (boolean)(mc.player.getDistance(var0) <= 1.0E8F ? 0 >> 4 << 3 ^ 1 : ((647923016 | 540982748) & 618710759 | 456734731) ^ 1069137103);
            });
            var10001 = (var0) -> {
                  return var0 instanceof EntityPlayer;
            };
            if ((201883922 ^ 154059827 ^ 1521906889) != 0) {
                  ;
            }

            Object var1 = var10000.filter(var10001).collect(Collectors.toList());
            if (((69550133 ^ 18012378) << 3 >> 3 ^ 87529711) == 0) {
                  ;
            }

            return (List)var1;
      }

      public Spammer() {
            super("Spammer", "spams messages in chat", (1022921201 ^ 511630246) >> 2 >> 1 >>> 3 ^ 9050441, Module.Category.WORLD);
            if ((130536393 >>> 3 >>> 3 << 1 << 1 >> 2 ^ 2039631) == 0) {
                  ;
            }

            if (((1403507746 << 2 | 1002245376) >> 1 >>> 3 ^ 133952984) == 0) {
                  ;
            }

            this.timerUtil = new TimerUtil();
            if (!"buy a domain and everything else you need at namecheap.com".equals("please take a shower")) {
                  ;
            }

            this.random = new Random();
            this.cnt = 789766681 >>> 1 >>> 4 >>> 4 << 2 ^ 6170052;
            ModeSetting var10001 = new ModeSetting;
            if ((((865454458 ^ 151973142) >>> 3 | 26229946) ^ 130409962 ^ -1343824463) != 0) {
                  ;
            }

            String[] var10006 = new String[(1 >>> 3 ^ 192163552) >> 2 ^ 48040890];
            if (!"i hope you catch fire ngl".equals("you're dogshit")) {
                  ;
            }

            var10006[(956846105 | 94768397) >>> 2 & 22489226 ^ 21168130] = "Global";
            var10006[((0 ^ 864552511) >>> 1 | 139250917) & 178887865 ^ 107558849 ^ 249674617] = "MSG";
            var10001.<init>("Mode", this, "Global", var10006);
            this.mode = var10001;
            if ((4198464 ^ 4198464) == 0) {
                  ;
            }

            messages.add("[Rage R9] - The Best Hack 'vk*com/rage_r8'");
            messages.add("[Rage R9] - The Strongest Hack 'vk*com/rage_r8'");
      }
}
