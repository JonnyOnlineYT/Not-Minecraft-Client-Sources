//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.combat;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import me.independed.inceptice.friends.Friend;
import me.independed.inceptice.friends.FriendManager;
import me.independed.inceptice.modules.Module;
import me.independed.inceptice.modules.ModuleManager;
import me.independed.inceptice.settings.BooleanSetting;
import me.independed.inceptice.settings.ModeSetting;
import me.independed.inceptice.settings.NumberSetting;
import me.independed.inceptice.settings.Setting;
import me.independed.inceptice.util.TimerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemAppleGold;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class Fraerok2 extends Module {
      public ModeSetting modePrior;
      public NumberSetting fovSet;
      public BooleanSetting refreshTargets;
      public static EntityLivingBase currentTarget = null;
      public NumberSetting delaySet;
      public BooleanSetting swordOnly;
      public NumberSetting rangeSet;
      public BooleanSetting playersSet;
      TimerUtil hit = new TimerUtil();
      public BooleanSetting invisibleTarget;
      private TimerUtil shieldAttack;
      public BooleanSetting noSwingSet;
      public ModeSetting modeSetting;
      public BooleanSetting animalsSet;
      public BooleanSetting pitchTarget;
      private Random random = new Random();
      public boolean swichable;
      public BooleanSetting yawTarget;
      public BooleanSetting diamondArmor;
      public BooleanSetting mobsSet;
      List targets;
      public static String activeModeKiller = null;
      private boolean refreshed;
      public BooleanSetting autoDelaySet;

      public Fraerok2() {
            super("KiIlAura", "automatically hits enemies", ((454649987 | 206350912) >>> 2 | 16511290) ^ 134216634, Module.Category.COMBAT);
            int var10001 = (0 >>> 4 & 480316644 | 1771395915) << 2 >>> 1 ^ 1395308183;
            if ((((1085561296 | 35369212) & 54272448) >>> 1 ^ 1583874000) != 0) {
                  ;
            }

            this.swichable = (boolean)var10001;
            if ((0 ^ 0) == 0) {
                  ;
            }

            this.refreshed = (boolean)((954415486 << 4 | 1044891890) >>> 1 ^ 1597763577);
            if (!"ape covered in human flesh".equals("nefariousMoment")) {
                  ;
            }

            if (((463967664 ^ 46076638) >> 2 >> 1 ^ 1487478985) != 0) {
                  ;
            }

            NumberSetting var1 = new NumberSetting;
            if ((((1516724035 | 9310353) >>> 4 >>> 2 | 22976920) >> 3 ^ 3143615) == 0) {
                  ;
            }

            var1.<init>("Range", this, 3.5D, 3.0D, 10.0D, 0.1D);
            this.rangeSet = var1;
            var1 = new NumberSetting;
            if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("please go outside")) {
                  ;
            }

            var1.<init>("Fov", this, 360.0D, 0.0D, 360.0D, 2.0D);
            this.fovSet = var1;
            this.noSwingSet = new BooleanSetting("No Swing", this, (boolean)(((1814559377 ^ 1538074196) & 266480442) << 1 ^ 251924480));
            BooleanSetting var2 = new BooleanSetting;
            int var10005 = 778218189 >> 3 << 1 ^ 194554546;
            if ((((570596790 ^ 55089932) << 1 | 1087105272) ^ -564583296) != 0) {
                  ;
            }

            var2.<init>("Animals", this, (boolean)var10005);
            this.animalsSet = var2;
            var2 = new BooleanSetting;
            if (((131612464 >> 2 << 2 >> 4 | 8157849) ^ 1488426291) != 0) {
                  ;
            }

            var2.<init>("Players", this, (boolean)((0 | 1794237291 | 900698425) >> 4 ^ 134217526));
            this.playersSet = var2;
            this.mobsSet = new BooleanSetting("Mobs", this, (boolean)(((54637578 >>> 3 | 1949581) ^ 757710) >>> 2 ^ 1936400));
            this.invisibleTarget = new BooleanSetting("Invisible", this, (boolean)(1610618401 << 1 << 3 ^ 90640));
            this.autoDelaySet = new BooleanSetting("AutoDelay", this, (boolean)(0 << 1 ^ 1368850341 ^ 1327317503 ^ 512474715));
            if ((93323369 >>> 3 ^ 3238459 ^ 1850318449) != 0) {
                  ;
            }

            this.delaySet = new NumberSetting("Delay", this, 590.0D, 1.0D, 1500.0D, 1.0D);
            this.yawTarget = new BooleanSetting("Horizontal", this, (boolean)((0 ^ 2123995357) >>> 1 << 3 >>> 3 ^ 525126767));
            this.swordOnly = new BooleanSetting("SwordOnly", this, (boolean)(586042886 << 2 >>> 4 >> 4 ^ 9156920));
            if (!"buy a domain and everything else you need at namecheap.com".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                  ;
            }

            this.pitchTarget = new BooleanSetting("Vertical", this, (boolean)(((1277523475 | 149225161) >>> 2 & 186590921) >> 1 & 19682665 ^ 17568064));
            var2 = new BooleanSetting;
            if ((201778670 >> 4 >> 4 >> 2 ^ 197049) == 0) {
                  ;
            }

            var2.<init>("DiamondArm", this, (boolean)(((267276519 ^ 241561131) << 3 | 167650139) ^ 234878843));
            this.diamondArmor = var2;
            this.refreshTargets = new BooleanSetting("Refresh", this, (boolean)((1535200850 >>> 4 | 27663576) ^ 96345341));
            ModeSetting var3 = new ModeSetting;
            if ((((1401010553 | 1198716885) & 56623875 ^ 50976719) >> 1 ^ 1746289990) != 0) {
                  ;
            }

            String[] var10006 = new String[1 >>> 3 >> 1 >>> 2 << 3 >> 2 ^ 2];
            var10006[84967558 ^ 82593727 ^ 33343289] = "Bypass";
            var10006[(0 ^ 1090271943 ^ 492766258) & 1136202236 ^ 48103964 ^ 1132401385] = "Simple";
            var3.<init>("Mode", this, "Bypass", var10006);
            this.modeSetting = var3;
            var3 = new ModeSetting;
            var10006 = new String[0 >>> 1 >> 1 >> 4 ^ 3];
            if ((1095447200 ^ 699625340 ^ -1835808971) != 0) {
                  ;
            }

            var10006[(1073895060 | 1063265518) & 779593099 ^ 778183818] = "Distance";
            int var10008 = 0 << 1 & 933651715 ^ 1;
            if (!"you're dogshit".equals("please get a girlfriend and stop cracking plugins")) {
                  ;
            }

            var10006[var10008] = "Health";
            var10006[(1 ^ 0) >> 4 ^ 2] = "LivingTime";
            var3.<init>("Prior", this, "LivingTime", var10006);
            this.modePrior = var3;
            TimerUtil var4 = new TimerUtil;
            if (!"you're dogshit".equals("stringer is a good obfuscator")) {
                  ;
            }

            var4.<init>();
            this.shieldAttack = var4;
            Setting[] var5 = new Setting[((2 | 0) >> 4 | 782737343 | 694597168) ^ 803717040];
            if (((1334939933 | 915507176 | 238130120) ^ 2142345213) == 0) {
                  ;
            }

            var5[((313687964 | 100879040) ^ 320263268) >> 4 ^ 5921467] = this.rangeSet;
            var5[0 >>> 1 << 1 << 3 ^ 1] = this.fovSet;
            if (((165417185 ^ 109795174) >> 1 ^ 128689091) == 0) {
                  ;
            }

            var5[1 >>> 2 >> 1 << 3 >>> 4 & 798087209 ^ 2] = this.noSwingSet;
            var5[(2 | 0) << 1 >>> 2 >> 1 ^ 3] = this.animalsSet;
            int var10003 = ((0 | 898670667 | 111853721) ^ 188405434) >>> 3 ^ 126879432;
            if ((((540721139 | 10069559) << 4 ^ 42273981) >> 2 ^ 38780147) == 0) {
                  ;
            }

            var5[var10003] = this.mobsSet;
            if (((261245445 >> 4 | 6544188) ^ -2102387759) != 0) {
                  ;
            }

            if ((1833432102 << 2 >>> 3 ^ 379845139) == 0) {
                  ;
            }

            var5[(1 | 0) & 0 ^ 5] = this.invisibleTarget;
            var10003 = (0 | 293127234) & 111770974 ^ 2638916;
            if (!"Some babies were dropped on their heads but you were clearly thrown at a wall.".equals("stop skidding")) {
                  ;
            }

            var5[var10003] = this.autoDelaySet;
            if ((((52280834 | 24221826) ^ 49303603) >> 4 ^ -42163443) != 0) {
                  ;
            }

            var5[(1 << 4 ^ 11 | 9 | 12) ^ 24] = this.delaySet;
            var5[(6 ^ 5 ^ 1) >>> 1 ^ 9] = this.yawTarget;
            var5[(2 & 1) >>> 3 >>> 3 ^ 9] = this.swordOnly;
            var5[((6 | 3) >> 1 >>> 1 | 0) ^ 11] = this.pitchTarget;
            var5[((6 | 2) >>> 4 ^ 353096347) << 4 ^ 1354574267] = this.modeSetting;
            var5[((0 ^ 1141193611) >>> 3 | 137379284) ^ 107590259 ^ 249105802] = this.modePrior;
            var5[(11 | 8) >> 2 >> 4 ^ 1748375373 ^ 1748375360] = this.diamondArmor;
            var10003 = ((10 | 3) << 1 << 1 | 24) ^ 50;
            BooleanSetting var10004 = this.refreshTargets;
            if (((1601962906 ^ 1039771407) << 2 & 920520339 ^ 34996752) == 0) {
                  ;
            }

            var5[var10003] = var10004;
            this.addSettings(var5);
      }

      public static Vec3d getRandomCenter(AxisAlignedBB var0) {
            Vec3d var10000 = new Vec3d;
            double var10002 = var0.minX;
            double var10003 = (var0.maxX - var0.minX) * 0.8D;
            if (((438201881 >> 4 >>> 2 ^ 6355604) >> 1 ^ 278934) == 0) {
                  ;
            }

            var10002 += var10003 * Math.random();
            var10003 = var0.minY + (var0.maxY - var0.minY) * Math.random() + 0.1D * Math.random();
            if (!"your mom your dad the one you never had".equals("you probably spell youre as your")) {
                  ;
            }

            var10000.<init>(var10002, var10003, var0.minZ + (var0.maxZ - var0.minZ) * 0.8D * Math.random());
            return var10000;
      }

      public static void setPressed(KeyBinding var0, boolean var1) {
            try {
                  Class var10000 = var0.getClass();
                  if ((1339418706 << 4 << 1 ^ 403194379 ^ 1202269746) != 0) {
                        ;
                  }

                  Field var2 = var10000.getDeclaredField("pressed");
                  if (!"intentMoment".equals("idiot")) {
                        ;
                  }

                  var2.setAccessible((boolean)(0 >>> 3 >> 4 ^ 1));
                  var2.setBoolean(var0, var1);
            } catch (ReflectiveOperationException var3) {
                  throw new RuntimeException(var3);
            }
      }

      private static boolean isPlayerUsingMainhand(EntityPlayer var0) {
            int var10000;
            label66: {
                  ItemStack var1 = var0.getHeldItemMainhand();
                  if (var0.getItemInUseCount() > 0) {
                        if (((798984164 >> 2 | 190394459) >>> 4 ^ 8767912 ^ 1653898455) != 0) {
                              ;
                        }

                        if (var1.getItemUseAction() == EnumAction.EAT && !var0.isCreative()) {
                              if (!"stop skidding".equals("stop skidding")) {
                                    ;
                              }

                              if (var0.getFoodStats().needFood()) {
                                    break label66;
                              }

                              if ((788639315 >> 3 >>> 2 ^ 16530733 ^ 25440351) == 0) {
                                    ;
                              }

                              if (var1.getItem() instanceof ItemAppleGold) {
                                    break label66;
                              }
                        }

                        if ((((965679272 << 1 | 745063014) << 2 | 209754534) << 4 ^ -541204512) == 0) {
                              ;
                        }

                        if (var1.getItemUseAction() == EnumAction.BOW) {
                              if ((49696312 >>> 2 >>> 1 >>> 2 ^ 96171 ^ 119087049) != 0) {
                                    ;
                              }

                              if (Fraerok2.canShootBow(var0)) {
                                    break label66;
                              }
                        }

                        if (!"stop skidding".equals("you're dogshit")) {
                              ;
                        }

                        if (var1.getItemUseAction() == EnumAction.DRINK || var1.getItemUseAction() == EnumAction.BLOCK) {
                              break label66;
                        }
                  }

                  var10000 = 549449 >> 3 << 1 ^ 137362;
                  if (((16868166 >>> 2 | 3675567) << 3 ^ 124385471) != 0) {
                        ;
                  }

                  return (boolean)var10000;
            }

            var10000 = 0 >> 3 << 2 ^ 1;
            return (boolean)var10000;
      }

      public static String getPlayerName(EntityPlayer var0) {
            return "null";
      }

      public static float nextFloat(float var0, float var1) {
            if (var0 != var1 && var1 - var0 > 0.0F) {
                  double var10000 = (double)var0;
                  double var10001 = (double)(var1 - var0);
                  double var10002 = Math.random();
                  if ((670321873 >>> 1 >> 3 ^ -1294329) != 0) {
                        ;
                  }

                  var10001 *= var10002;
                  if (!"idiot".equals("please take a shower")) {
                        ;
                  }

                  var10000 += var10001;
                  if (((1539401804 ^ 612439535 ^ 943763021) >> 3 ^ 148904573) == 0) {
                        ;
                  }

                  return (float)var10000;
            } else {
                  return var0;
            }
      }

      private void setRotation(float var1, float var2) {
            mc.player.renderYawOffset = var1;
            EntityPlayerSP var10000 = mc.player;
            if (((123043688 >> 3 | 14116652) ^ 11872478 ^ -1471148358) != 0) {
                  ;
            }

            var10000.rotationYawHead = var1;
            double var3;
            double var10001;
            if (this.random.nextBoolean()) {
                  var3 = (double)var1;
                  var10001 = (double)this.random.nextInt((10 ^ 7) >> 3 >> 1 >> 1 ^ 100);
                  if (((214070788 >>> 2 | 10809987) >> 4 ^ 3887096) == 0) {
                        ;
                  }

                  var1 = (float)(var3 + var10001 * 0.03D);
            } else {
                  var1 = (float)((double)var1 - (double)this.random.nextInt((76 ^ 61 ^ 86) >> 3 ^ 96) * 0.05D);
            }

            if (this.random.nextBoolean()) {
                  var3 = (double)var2;
                  int var5 = this.random.nextInt((37 | 12) << 4 << 4 ^ 11620);
                  if (((2075076968 << 2 ^ 1573607677) & 1599553049 ^ -50520296) != 0) {
                        ;
                  }

                  var2 = (float)(var3 + (double)var5 * 0.05D);
            } else {
                  var2 = (float)((double)var2 - (double)this.random.nextInt(58 >>> 4 >> 3 ^ 100) * 0.05D);
            }

            double var10002;
            if (this.yawTarget.isEnabled()) {
                  if (var1 >= 0.0F) {
                        Minecraft var4 = mc;
                        if (((1805376975 >>> 2 >>> 3 | 52963547) ^ 58523391) == 0) {
                              ;
                        }

                        if (var4.player.rotationYaw < var1) {
                              for(; mc.player.rotationYaw < var1; var10000.rotationYaw = (float)(var10001 + var10002)) {
                                    var10000 = mc.player;
                                    var10001 = (double)var10000.rotationYaw;
                                    var10002 = (double)this.random.nextInt(6 << 2 >>> 2 ^ 98) * 0.001D % 360.0D;
                                    if (!"please get a girlfriend and stop cracking plugins".equals("stringer is a good obfuscator")) {
                                          ;
                                    }
                              }
                        } else {
                              for(; mc.player.rotationYaw > var1; var10000.rotationYaw = (float)(var10001 - (double)this.random.nextInt((95 >> 1 & 26) << 2 ^ 76) * 0.001D % 360.0D)) {
                                    var4 = mc;
                                    if (!"ape covered in human flesh".equals("stringer is a good obfuscator")) {
                                          ;
                                    }

                                    var10000 = var4.player;
                                    var10001 = (double)var10000.rotationYaw;
                                    if (!"yo mama name maurice".equals("you're dogshit")) {
                                          ;
                                    }
                              }
                        }
                  } else {
                        float var6 = mc.player.rotationYaw;
                        if (((556200030 | 199668262 | 52488132) & 377554446 ^ 752673635) != 0) {
                              ;
                        }

                        if ((2102454179 << 4 >> 4 >> 2 ^ -11257368) == 0) {
                              ;
                        }

                        if (var6 < var1) {
                              for(; mc.player.rotationYaw < var1; var10000.rotationYaw = (float)var10001) {
                                    var10000 = mc.player;
                                    var10001 = (double)var10000.rotationYaw;
                                    Random var9 = this.random;
                                    if ((((1617559140 | 1467084626) >> 4 & 85029963) >>> 4 ^ -739733829) != 0) {
                                          ;
                                    }

                                    int var10003 = 62 >>> 2 << 3 & 47 & 10 ^ 108;
                                    if ((((1622352159 ^ 656213641) >>> 2 | 85081936) & 8766722 ^ 8488192) == 0) {
                                          ;
                                    }

                                    var10002 = (double)var9.nextInt(var10003);
                                    if ((1535327173 >>> 4 >>> 4 >>> 1 ^ 1983031 ^ -84211901) != 0) {
                                          ;
                                    }

                                    var10001 += var10002 * 0.001D % 360.0D;
                                    if ((1922764276 >> 3 >> 2 << 4 ^ 961382128) == 0) {
                                          ;
                                    }
                              }
                        } else {
                              while(true) {
                                    var6 = mc.player.rotationYaw;
                                    if (!"yo mama name maurice".equals("your mom your dad the one you never had")) {
                                          ;
                                    }

                                    if (var6 <= var1) {
                                          break;
                                    }

                                    var10000 = mc.player;
                                    float var7 = var10000.rotationYaw;
                                    if ((681689383 << 1 >>> 1 ^ 681689383) == 0) {
                                          ;
                                    }

                                    var10001 = (double)var7;
                                    int var8 = this.random.nextInt(((92 | 5) ^ 53) << 4 ^ 1764);
                                    if ((606190169 >>> 2 ^ 101464496 ^ -1703552501) != 0) {
                                          ;
                                    }

                                    var10002 = (double)var8;
                                    if ((((615217709 ^ 469829378) & 917526731) << 2 ^ -1031708628) == 0) {
                                          ;
                                    }

                                    var10000.rotationYaw = (float)(var10001 - var10002 * 0.001D % 360.0D);
                              }
                        }
                  }
            }

            if (this.pitchTarget.isEnabled()) {
                  if (var2 >= 0.0F) {
                        if (mc.player.rotationPitch < var2) {
                              while(mc.player.rotationPitch < var2) {
                                    var10000 = mc.player;
                                    var10000.rotationPitch = (float)((double)var10000.rotationPitch + (double)this.random.nextInt(((16 & 0) << 4 | 1327108600) & 690557508 ^ 151523364) * 0.001D % 360.0D);
                              }
                        } else {
                              for(; mc.player.rotationPitch > var2; var10000.rotationPitch = (float)(var10001 - var10002 * 0.001D % 360.0D)) {
                                    var10000 = mc.player;
                                    var10001 = (double)var10000.rotationPitch;
                                    var10002 = (double)this.random.nextInt((64 | 44) << 2 << 1 & 636 ^ 516);
                                    if ((109121666 >> 3 ^ 13017070 ^ 1490430) == 0) {
                                          ;
                                    }
                              }
                        }
                  } else {
                        if ((2101 >>> 4 ^ 92 ^ 88 ^ 135) == 0) {
                              ;
                        }

                        if (mc.player.rotationPitch < var2) {
                              while(true) {
                                    var10000 = mc.player;
                                    if (((496759377 << 3 | 1342463008) ^ -52437336) == 0) {
                                          ;
                                    }

                                    if (var10000.rotationPitch >= var2) {
                                          break;
                                    }

                                    var10000 = mc.player;
                                    var10000.rotationPitch = (float)((double)var10000.rotationPitch + (double)this.random.nextInt((89 >>> 1 | 31) ^ 91) * 0.001D % 360.0D);
                                    if ((314397450 >> 1 >>> 1 ^ 78599362) == 0) {
                                          ;
                                    }
                              }
                        } else {
                              for(; mc.player.rotationPitch > var2; var10000.rotationPitch = (float)(var10001 - var10002 * 0.001D % 360.0D)) {
                                    var10000 = mc.player;
                                    var10001 = (double)var10000.rotationPitch;
                                    var10002 = (double)this.random.nextInt(((22 | 18) & 21 & 3 | 1499827301) ^ 1499827201);
                                    if (((1579830071 << 4 & 1539842469) >> 2 ^ 278923336) == 0) {
                                          ;
                                    }
                              }
                        }
                  }
            }

      }

      @SubscribeEvent
      public void onPlayerTick(PlayerTickEvent var1) {
            if (mc.player != null) {
                  Minecraft var10000 = mc;
                  if ((1142084244 >>> 4 >>> 4 >>> 2 ^ 1115316) == 0) {
                        ;
                  }

                  if (!var10000.player.isDead && mc.world != null) {
                        if ((136257928 >>> 3 << 1 >> 1 >>> 1 ^ 8516120) == 0) {
                              ;
                        }

                        activeModeKiller = this.modePrior.activeMode;
                        if (currentTarget != null) {
                              if ((((1867003651 ^ 1199041300) & 226170358) >> 2 << 4 ^ 552357968) == 0) {
                                    ;
                              }

                              if (mc.player.getDistance(currentTarget) > 7.0F) {
                                    currentTarget = null;
                                    if (((2017682277 ^ 1910500431) >> 4 << 1 ^ 20156900) == 0) {
                                          ;
                                    }
                              }
                        }

                        Stream var10001;
                        Predicate var10002;
                        List var5;
                        if (this.refreshTargets.isEnabled() && !this.refreshed) {
                              if (this.modePrior.activeMode == "LivingTime") {
                                    var10001 = mc.world.loadedEntityList.stream().filter((var0) -> {
                                          return (boolean)(var0 != mc.player ? (0 ^ 871728504) >> 1 ^ 435864253 : 1558616710 << 1 << 3 >> 4 ^ 974906370 ^ -956465532);
                                    });
                                    if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("stringer is a good obfuscator")) {
                                          ;
                                    }

                                    var10002 = (var0) -> {
                                          return (boolean)(!(var0 instanceof EntityEnderCrystal) ? 0 << 2 >> 4 & 1947669043 ^ 1 : (851788325 | 573078329) >> 1 & 173064490 ^ 139493642);
                                    };
                                    if (((1749980999 << 3 ^ 369989209 | 1388396071) >> 2 ^ 156302361 ^ -1232301575) != 0) {
                                          ;
                                    }

                                    var10001 = var10001.filter(var10002);
                                    if ((405555718 >>> 2 << 1 >>> 2 ^ 50694464) == 0) {
                                          ;
                                    }

                                    var10001 = var10001.filter((var0) -> {
                                          int var10000;
                                          if (!var0.isDead) {
                                                var10000 = (0 ^ 1394842172 | 1015707995) >>> 4 ^ 133873590;
                                          } else {
                                                var10000 = (17749597 >> 1 & 8832570) >> 4 ^ 549922;
                                                if (((150997043 | 134185858) ^ 1607845744) != 0) {
                                                      ;
                                                }
                                          }

                                          return (boolean)var10000;
                                    });
                                    var10002 = (var1x) -> {
                                          return this.attackCheck(var1x);
                                    };
                                    if ((321098729 >>> 2 & 25189214 & 5699405 ^ 1533302559) != 0) {
                                          ;
                                    }

                                    var10001 = var10001.filter(var10002).filter((var0) -> {
                                          return var0 instanceof EntityLivingBase;
                                    }).filter((var0) -> {
                                          int var10000;
                                          if (var0.ticksExisted > ((13 | 10) << 2 >>> 2 ^ 17)) {
                                                var10000 = (0 << 3 >> 1 ^ 695583998) << 4 ^ -1755557919;
                                          } else {
                                                var10000 = 1845063118 >> 1 >> 2 >> 3 >> 4 ^ 1801819;
                                                if (((470016684 ^ 408025418) << 2 >> 1 ^ 1580341487) != 0) {
                                                      ;
                                                }
                                          }

                                          return (boolean)var10000;
                                    }).filter((var1x) -> {
                                          double var10000 = (double)mc.player.getDistance(var1x);
                                          if ((41428192 << 1 >> 4 >> 3 ^ 647315) == 0) {
                                                ;
                                          }

                                          return (boolean)(var10000 <= this.rangeSet.getValue() + 200.0D ? (0 | 1991782242 | 1447021091) ^ 1992290146 : (1365114743 | 1312282630 | 784211943 | 326237127) << 4 ^ -144);
                                    });
                                    if (!"stringer is a good obfuscator".equals("You're so fat whenever you go to the beach the tide comes in.")) {
                                          ;
                                    }

                                    this.targets = (List)var10001.filter((var1x) -> {
                                          if (((944408361 << 3 | 1077018835) >> 2 ^ -258109834) == 0) {
                                                ;
                                          }

                                          return (boolean)((double)(Math.abs(((float[])Fraerok2.getRotations(var1x))[962084560 << 1 >> 3 ^ 240521140] - var1x.rotationYaw) % 180.0F) < this.fovSet.getValue() / 2.0D ? ((0 & 589962494) >>> 3 | 951754028) ^ 951754029 : (729655881 ^ 183009094) << 1 ^ 1126935070);
                                    }).collect(Collectors.toList());
                              } else {
                                    if ((562697345 >> 1 << 2 >> 1 ^ 390300290 ^ 919180802) == 0) {
                                          ;
                                    }

                                    Minecraft var3 = mc;
                                    if (!"please dont crack my plugin".equals("intentMoment")) {
                                          ;
                                    }

                                    var5 = var3.world.loadedEntityList;
                                    if ((495714349 << 4 << 3 ^ 654864318) != 0) {
                                          ;
                                    }

                                    var10001 = var5.stream().filter((var0) -> {
                                          if (((1827137331 >>> 4 ^ 57231389) << 1 >>> 3 ^ 23711131) == 0) {
                                                ;
                                          }

                                          return (boolean)(var0 != mc.player ? ((0 & 1623418055 | 911684014) >>> 3 | 49566162) ^ 117372918 : (1708304687 >>> 2 & 201290749) >> 4 ^ 9912868);
                                    });
                                    var10002 = (var0) -> {
                                          int var10000;
                                          if (!(var0 instanceof EntityEnderCrystal)) {
                                                var10000 = (0 | 1513517011) << 2 >> 4 ^ 109943797;
                                          } else {
                                                if (((910811304 | 758875200) << 3 & 1406884794 ^ 1406862080) == 0) {
                                                      ;
                                                }

                                                var10000 = 249428446 >>> 3 << 4 ^ 498856880;
                                          }

                                          return (boolean)var10000;
                                    };
                                    if ((1847230478 >>> 4 << 3 ^ 578681426 ^ 359709266) == 0) {
                                          ;
                                    }

                                    var10001 = var10001.filter(var10002).filter((var0) -> {
                                          int var10000;
                                          if (!var0.isDead) {
                                                if ((1811834545 << 1 & 100630239 ^ -486296217) != 0) {
                                                      ;
                                                }

                                                var10000 = (0 ^ 535480565 ^ 138408327) & 135715103 ^ 1317139;
                                          } else {
                                                var10000 = 829499746 >> 4 << 3 ^ 414749872;
                                          }

                                          if (((2114987600 | 1853461517) & 1001236114 ^ 379407298 ^ -710775832) != 0) {
                                                ;
                                          }

                                          return (boolean)var10000;
                                    }).filter((var1x) -> {
                                          return this.attackCheck(var1x);
                                    }).filter((var0) -> {
                                          if (!"you probably spell youre as your".equals("please get a girlfriend and stop cracking plugins")) {
                                                ;
                                          }

                                          return var0 instanceof EntityLivingBase;
                                    }).filter((var0) -> {
                                          int var10000;
                                          if (var0.ticksExisted > ((11 << 3 << 4 ^ 439) >>> 1 ^ 517)) {
                                                var10000 = 0 << 4 >>> 1 ^ 1954611212 ^ 1954611213;
                                          } else {
                                                var10000 = 1585255213 ^ 1533778666 ^ 79806390 ^ 30881393;
                                                if (!"i hope you catch fire ngl".equals("nefariousMoment")) {
                                                      ;
                                                }
                                          }

                                          return (boolean)var10000;
                                    }).filter((var1x) -> {
                                          float var10000 = mc.player.getDistance(var1x);
                                          if (!"you're dogshit".equals("you're dogshit")) {
                                                ;
                                          }

                                          double var2 = (double)var10000;
                                          if ((1085596585 >> 4 >>> 3 >>> 3 ^ 1453972225) != 0) {
                                                ;
                                          }

                                          int var3 = var2 <= this.rangeSet.getValue() + 200.0D ? (0 | 988988423) >> 3 ^ 123623553 : (421412403 | 70328945) >>> 4 ^ 7028245 ^ 28888562;
                                          if (!"i hope you catch fire ngl".equals("buy a domain and everything else you need at namecheap.com")) {
                                                ;
                                          }

                                          return (boolean)var3;
                                    }).filter((var1x) -> {
                                          float var10000 = Math.abs(((float[])Fraerok2.getRotations(var1x))[1485639432 >> 1 >> 3 ^ 92852464]) % 360.0F;
                                          if (!"your mom your dad the one you never had".equals("stop skidding")) {
                                                ;
                                          }

                                          float var10001 = mc.player.rotationYaw;
                                          if (!"idiot".equals("please go outside")) {
                                                ;
                                          }

                                          double var2 = (double)Math.abs(var10000 - Math.abs(var10001 % 360.0F));
                                          NumberSetting var4 = this.fovSet;
                                          if (!"stop skidding".equals("intentMoment")) {
                                                ;
                                          }

                                          int var3;
                                          if (var2 < var4.getValue() / 2.0D) {
                                                var3 = 0 & 1328466033 & 480197735 ^ 1;
                                          } else {
                                                if (!"i hope you catch fire ngl".equals("please take a shower")) {
                                                      ;
                                                }

                                                var3 = 632791861 >> 2 >> 3 ^ 19774745;
                                          }

                                          return (boolean)var3;
                                    });
                                    if (!"stop skidding".equals("stop skidding")) {
                                          ;
                                    }

                                    this.targets = (List)var10001.collect(Collectors.toList());
                              }

                              this.refreshed = (boolean)((0 | 1304265284 | 836256045) >>> 3 ^ 264222828);
                        } else {
                              if (((247260516 ^ 46872091) >>> 3 & 14729491 ^ 818407377) != 0) {
                                    ;
                              }

                              if (!this.refreshTargets.isEnabled()) {
                                    this.refreshed = (boolean)((((1619564376 ^ 368600704) & 894537300) << 4 | 1155611890) ^ 1441349106);
                                    if (this.modePrior.activeMode == "LivingTime") {
                                          if (!"ape covered in human flesh".equals("you probably spell youre as your")) {
                                                ;
                                          }

                                          var10001 = mc.world.loadedEntityList.stream().filter((var0) -> {
                                                Minecraft var10001 = mc;
                                                if (!"please get a girlfriend and stop cracking plugins".equals("stop skidding")) {
                                                      ;
                                                }

                                                EntityPlayerSP var1 = var10001.player;
                                                if (!"shitted on you harder than archybot".equals("nefariousMoment")) {
                                                      ;
                                                }

                                                return (boolean)(var0 != var1 ? 0 << 4 >> 3 ^ 33235355 ^ 33235354 : ((1328240381 ^ 867813282 | 1912407519) & 1560372745) >> 4 ^ 97521728);
                                          });
                                          var10002 = (var0) -> {
                                                return (boolean)(!(var0 instanceof EntityEnderCrystal) ? (0 << 3 >> 1 >>> 2 ^ 1997953546) << 1 ^ -299060203 : (1568299653 | 1393860287) << 3 << 4 ^ -1083220096);
                                          };
                                          if (!"your mom your dad the one you never had".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                                                ;
                                          }

                                          var10001 = var10001.filter(var10002).filter((var0) -> {
                                                int var10000;
                                                if (!var0.isDead) {
                                                      var10000 = (0 << 1 ^ 857808246) & 146601945 ^ 2102609;
                                                      if (((161937487 | 77474928) & 218753936 & 218283202 ^ 1301394095) != 0) {
                                                            ;
                                                      }
                                                } else {
                                                      var10000 = (992542374 >> 1 >> 4 >>> 1 >> 2 | 2159006) ^ 3930622;
                                                }

                                                if (((1695956174 | 1669181038) >> 4 >> 2 ^ 27131643) == 0) {
                                                      ;
                                                }

                                                return (boolean)var10000;
                                          }).filter((var1x) -> {
                                                return this.attackCheck(var1x);
                                          });
                                          var10002 = (var0) -> {
                                                boolean var10000 = var0 instanceof EntityLivingBase;
                                                if (!"buy a domain and everything else you need at namecheap.com".equals("buy a domain and everything else you need at namecheap.com")) {
                                                      ;
                                                }

                                                return var10000;
                                          };
                                          if (!"shitted on you harder than archybot".equals("stringer is a good obfuscator")) {
                                                ;
                                          }

                                          this.targets = (List)var10001.filter(var10002).filter((var0) -> {
                                                int var10000;
                                                if (var0.ticksExisted > (16 >>> 3 >> 3 >> 4 ^ 30)) {
                                                      var10000 = (0 >> 4 >> 1 << 2 | 214175841) ^ 214175840;
                                                } else {
                                                      var10000 = (388219138 ^ 262151263) & 234890392 & 93259851 ^ 8;
                                                      if (((222938239 ^ 29730819) << 1 & 21598887 ^ -974318991) != 0) {
                                                            ;
                                                      }
                                                }

                                                return (boolean)var10000;
                                          }).filter((var1x) -> {
                                                int var10000;
                                                if ((double)mc.player.getDistance(var1x) <= this.rangeSet.getValue() + 1.25D) {
                                                      var10000 = (0 & 1714777194 & 1251384141) << 1 >> 2 ^ 1;
                                                } else {
                                                      var10000 = (17326090 | 5629755) ^ 22931259;
                                                      if ((1547760811 >> 4 >>> 1 >> 2 >>> 3 ^ -1890276417) != 0) {
                                                            ;
                                                      }
                                                }

                                                return (boolean)var10000;
                                          }).filter((var1x) -> {
                                                int var10000;
                                                if ((double)(Math.abs(((float[])Fraerok2.getRotations(var1x))[(1506699761 | 47702465) >>> 2 >> 2 ^ 96337567] - var1x.rotationYaw) % 180.0F) < this.fovSet.getValue() / 2.0D) {
                                                      if (((1065163032 | 853588464) >> 1 & 53224411 ^ 53215448) == 0) {
                                                            ;
                                                      }

                                                      var10000 = (0 >>> 2 & 1914340434) >>> 4 << 4 ^ 1;
                                                } else {
                                                      var10000 = 2142529338 >>> 1 & 130789144 & 14574778 ^ 13246488;
                                                }

                                                return (boolean)var10000;
                                          }).collect(Collectors.toList());
                                    } else {
                                          if ((433178524 >> 3 << 1 & 19769335 ^ 2087510249) != 0) {
                                                ;
                                          }

                                          var10001 = mc.world.loadedEntityList.stream();
                                          if (!"your mom your dad the one you never had".equals("nefariousMoment")) {
                                                ;
                                          }

                                          var10001 = var10001.filter((var0) -> {
                                                EntityPlayerSP var10001 = mc.player;
                                                if (!"please dont crack my plugin".equals("nefariousMoment")) {
                                                      ;
                                                }

                                                int var10000 = var0 != var10001 ? 0 >>> 1 >>> 1 ^ 1 : (1428383458 >> 3 ^ 149981647 | 1303943) ^ 39314839;
                                                if (((463987007 << 2 & 1346659652) >>> 4 & 4888089 ^ -1179071943) != 0) {
                                                      ;
                                                }

                                                return (boolean)var10000;
                                          }).filter((var0) -> {
                                                boolean var10000 = var0 instanceof EntityEnderCrystal;
                                                if ((((1011226307 | 368127375) ^ 926569493) >>> 3 >> 4 ^ 1415895) == 0) {
                                                      ;
                                                }

                                                int var1;
                                                if (!var10000) {
                                                      var1 = (0 >>> 1 << 1 ^ 1172280152 | 1171916202) ^ 1172281339;
                                                } else {
                                                      if (!"stop skidding".equals("shitted on you harder than archybot")) {
                                                            ;
                                                      }

                                                      var1 = 743838965 >> 1 >>> 3 & 36847076 ^ 33562948;
                                                      if ((16777224 ^ 16777224) == 0) {
                                                            ;
                                                      }
                                                }

                                                return (boolean)var1;
                                          });
                                          if (((0 << 3 & 1119910101 | 1241836213) ^ 1241836213) == 0) {
                                                ;
                                          }

                                          var10001 = var10001.filter((var0) -> {
                                                return (boolean)(!var0.isDead ? (0 >> 2 ^ 1274415388) & 148905735 ^ 148900101 : 50334089 >>> 3 & 2462895 ^ 2097185);
                                          }).filter((var1x) -> {
                                                return this.attackCheck(var1x);
                                          });
                                          if (!"ape covered in human flesh".equals("i hope you catch fire ngl")) {
                                                ;
                                          }

                                          var10002 = (var0) -> {
                                                return var0 instanceof EntityLivingBase;
                                          };
                                          if (((5276832 ^ 3299436 ^ 362135) >>> 3 ^ -266259731) != 0) {
                                                ;
                                          }

                                          var10001 = var10001.filter(var10002).filter((var0) -> {
                                                int var10000;
                                                if (var0.ticksExisted > (((18 ^ 10) & 7 | 1337197767) ^ 1337197785)) {
                                                      var10000 = (0 & 1984188235) << 1 ^ 1;
                                                } else {
                                                      if (!"i hope you catch fire ngl".equals("shitted on you harder than archybot")) {
                                                            ;
                                                      }

                                                      var10000 = (113179522 | 36458951 | 2596004) >>> 1 & 25991073 ^ 17602465;
                                                }

                                                return (boolean)var10000;
                                          });
                                          var10002 = (var1x) -> {
                                                return (boolean)((double)mc.player.getDistance(var1x) <= this.rangeSet.getValue() ? (0 >>> 2 ^ 1049986691 | 1012308507) ^ 1054318234 : ((771907799 | 568232106) >>> 2 >> 1 ^ 37835189) << 3 ^ 1037329744);
                                          };
                                          if (!"you're dogshit".equals("yo mama name maurice")) {
                                                ;
                                          }

                                          this.targets = (List)var10001.filter(var10002).filter((var1x) -> {
                                                return (boolean)((double)Math.abs(Math.abs(((float[])Fraerok2.getRotations(var1x))[1675556366 << 4 & 133129329 ^ 99573856]) % 360.0F - Math.abs(mc.player.rotationYaw % 360.0F)) < this.fovSet.getValue() / 2.0D ? ((0 & 1811543230) << 1 << 1 | 180508280) ^ 180508281 : (646666372 ^ 418432185) & 950693254 ^ 942279684);
                                          }).collect(Collectors.toList());
                                    }
                              }
                        }

                        if (this.modePrior.activeMode == "Distance") {
                              this.targets = this.sortByDistance(this.targets);
                              if (!"yo mama name maurice".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                                    ;
                              }
                        } else {
                              if (((1711585588 | 623444474) << 3 ^ 1211773997) != 0) {
                                    ;
                              }

                              List var4;
                              if (this.modePrior.activeMode == "Health") {
                                    var4 = this.targets;
                                    if ((745602095 << 1 >>> 3 >>> 1 & 16351895 ^ -1103837891) != 0) {
                                          ;
                                    }

                                    var5 = this.sortByHealth(var4);
                                    if (((837983505 ^ 376656195 | 512152339) ^ -2104319568) != 0) {
                                          ;
                                    }

                                    this.targets = var5;
                                    if (!"please take a shower".equals("idiot")) {
                                          ;
                                    }
                              } else {
                                    var4 = this.targets;
                                    if ((803209706 << 2 >>> 4 ^ 166163565 ^ 35615767) == 0) {
                                          ;
                                    }

                                    this.targets = this.sortByLivingTime(var4);
                              }
                        }

                        if (((285285639 >>> 1 | 105935981) & 193366166 & 115943742 ^ 41945094) == 0) {
                              ;
                        }

                        if (this.diamondArmor.isEnabled()) {
                              this.targets.removeIf((var0) -> {
                                    return (boolean)(!AntiBot.checkArmor((EntityPlayer)var0) ? (0 | 1531304778 | 469653375) ^ 1264807685 ^ 278704251 : (271036296 >> 4 | 13091035) ^ 29883131);
                              });
                        }

                        if (this.targets.size() > 0) {
                              var10000 = mc;
                              if ((((1908181692 | 1593201053) << 4 & 826440550) << 3 << 3 ^ -1887013733) != 0) {
                                    ;
                              }

                              EntityPlayerSP var2 = var10000.player;
                              Object var7 = this.targets.get(((1273504003 ^ 826645747) << 4 << 4 | 50240596) ^ -1342178220);
                              if (!"idiot".equals("your mom your dad the one you never had")) {
                                    ;
                              }

                              if ((double)var2.getDistance((Entity)var7) <= this.rangeSet.getValue()) {
                                    if (this.swordOnly.isEnabled() && mc.player.getHeldItemMainhand().getItem() instanceof ItemSword) {
                                          currentTarget = (EntityLivingBase)this.targets.get(((1475763190 << 4 | 58057748) ^ 1568157030) << 2 << 4 ^ -2089515904);
                                          this.attackTarget((Entity)this.targets.get(1704967694 << 2 ^ 488923813 ^ -1956996451));
                                    } else {
                                          if (!"shitted on you harder than archybot".equals("your mom your dad the one you never had")) {
                                                ;
                                          }

                                          if ((1113367113 >> 1 << 4 >>> 1 ^ -528279221) != 0) {
                                                ;
                                          }

                                          if (!this.swordOnly.isEnabled()) {
                                                currentTarget = (EntityLivingBase)this.targets.get(1840200008 ^ 1487865553 ^ 834878083 ^ 79889690);
                                                if (!"stringer is a good obfuscator".equals("ape covered in human flesh")) {
                                                      ;
                                                }

                                                var5 = this.targets;
                                                int var6 = 1374397168 >> 4 >>> 3 << 1 ^ 21474954;
                                                if (((112491064 << 2 & 259993951) << 2 ^ 692355328) == 0) {
                                                      ;
                                                }

                                                var7 = var5.get(var6);
                                                if ((420409152 >>> 4 >>> 2 >> 3 ^ 821111) == 0) {
                                                      ;
                                                }

                                                this.attackTarget((Entity)var7);
                                          }
                                    }
                              }
                        } else {
                              currentTarget = null;
                        }

                        if (!"intentMoment".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                              ;
                        }

                        return;
                  }
            }

      }

      public void attackTarget(Entity var1) {
            if (var1 instanceof EntityPlayer && Fraerok2.isPlayerShielded((EntityPlayer)var1) && mc.player.getHeldItemMainhand().getItem() instanceof ItemAxe && this.shieldAttack.hasReached(60.0D)) {
                  mc.playerController.attackEntity(mc.player, var1);
                  if ((220063397 << 3 >>> 2 ^ 440126794) == 0) {
                        ;
                  }

                  this.shieldAttack.reset();
            }

            Entity var2 = var1;
            BooleanSetting var10000 = this.autoDelaySet;
            if (!"shitted on you harder than archybot".equals("yo mama name maurice")) {
                  ;
            }

            TimerUtil var7;
            if (var10000.isEnabled()) {
                  if ((double)mc.player.getCooledAttackStrength(1.0F) < 1.0D) {
                        return;
                  }
            } else {
                  var7 = this.hit;
                  if (((1805492604 | 480963481) >>> 3 << 1 << 4 >>> 1 ^ 2139084784) == 0) {
                        ;
                  }

                  if (!var7.hasReached(this.delaySet.getValue())) {
                        return;
                  }
            }

            if ((357339611 >>> 1 >> 3 & 3292711 ^ 1050629) == 0) {
                  ;
            }

            ModeSetting var8 = this.modeSetting;
            if ((426050 << 1 >> 4 << 3 ^ 426048) == 0) {
                  ;
            }

            float[] var3;
            PlayerControllerMP var9;
            Minecraft var11;
            boolean var12;
            EntityPlayerSP var13;
            if (var8.activeMode == "Bypass") {
                  if (ModuleManager.getModuleByName("Criticals").isToggled()) {
                        if (Criticals.isRage) {
                              if ((1826145694 >>> 2 >>> 1 >>> 3 ^ 28533526) == 0) {
                                    ;
                              }

                              this.hit.reset();
                              var3 = (float[])Fraerok2.getRotations(var1);
                              this.setRotation(var3[((1496551767 << 4 | 1709063749) >> 1 << 1 | 1642066712) ^ -134217860], var3[0 << 4 << 3 ^ 1]);
                              mc.player.setSprinting((boolean)(134744070 >>> 3 >>> 1 ^ 8421504));
                              mc.playerController.attackEntity(mc.player, var1);
                              if (!this.noSwingSet.enabled) {
                                    if (!"buy a domain and everything else you need at namecheap.com".equals("intentMoment")) {
                                          ;
                                    }

                                    if ((47260206 << 4 & 655236608 ^ 1611061065) != 0) {
                                          ;
                                    }

                                    mc.player.swingArm(EnumHand.MAIN_HAND);
                              }
                        } else if (!mc.player.onGround && (double)mc.player.fallDistance >= 0.18462145D) {
                              this.hit.reset();
                              var3 = (float[])Fraerok2.getRotations(var1);
                              float var10001 = var3[33572901 >>> 3 << 1 ^ 2470609 ^ 10854617];
                              if ((1405114510 << 1 >>> 4 >>> 2 ^ 2042944036) != 0) {
                                    ;
                              }

                              this.setRotation(var10001, var3[0 >> 1 ^ 523185258 ^ 523185259]);
                              var9 = mc.playerController;
                              if (!"Some babies were dropped on their heads but you were clearly thrown at a wall.".equals("your mom your dad the one you never had")) {
                                    ;
                              }

                              Minecraft var10 = mc;
                              if (((1246590466 >>> 4 | 14210323) & 60933291 & 10068247 ^ 8421379) == 0) {
                                    ;
                              }

                              var13 = var10.player;
                              if ((((387956370 | 273732738) >> 2 >> 4 | 2921552) ^ -1925063815) != 0) {
                                    ;
                              }

                              if (((587833067 << 4 & 199684535) >>> 1 << 3 ^ 753236711) != 0) {
                                    ;
                              }

                              var9.attackEntity(var13, var1);
                              if (!this.noSwingSet.enabled) {
                                    var11 = mc;
                                    if (((946999105 ^ 803752413) >> 4 & 14215435 ^ 5808137) == 0) {
                                          ;
                                    }

                                    var11.player.swingArm(EnumHand.MAIN_HAND);
                                    if (((1057284 ^ 830881) << 2 ^ 2067510493) != 0) {
                                          ;
                                    }
                              }
                        }
                  } else {
                        var12 = ModuleManager.getModuleByName("Criticals").isToggled();
                        if ((((557400409 << 1 >>> 2 | 278554328) >>> 2 | 10999364) ^ -1730828988) != 0) {
                              ;
                        }

                        if (!var12) {
                              var7 = this.hit;
                              if (((81320726 << 3 & 137047724) << 3 ^ 363072456) != 0) {
                                    ;
                              }

                              var7.reset();
                              var3 = (float[])Fraerok2.getRotations(var1);
                              this.setRotation(var3[((272879537 | 144705424) & 255604670) >>> 2 ^ 34079212], var3[0 << 3 & 813244398 ^ 1938305553 ^ 1938305552]);
                              mc.playerController.attackEntity(mc.player, var1);
                              if (!this.noSwingSet.enabled) {
                                    mc.player.swingArm(EnumHand.MAIN_HAND);
                              }

                              if ((646119612 >>> 2 ^ 90895495 ^ 210521107 ^ 1358512832) != 0) {
                                    ;
                              }
                        }
                  }
            } else if (ModuleManager.getModuleByName("Criticals").isToggled()) {
                  if ((((431956028 | 133916789) << 3 & 420393441) >> 3 ^ 418998663) != 0) {
                        ;
                  }

                  var12 = Criticals.isRage;
                  if (!"Some babies were dropped on their heads but you were clearly thrown at a wall.".equals("idiot")) {
                        ;
                  }

                  if (var12) {
                        this.hit.reset();
                        float var6 = mc.player.rotationYaw;
                        float[] var4 = (float[])Fraerok2.getRotations(var1);
                        this.setRotation(var4[308412420 >>> 2 ^ 60590721 ^ 117705344], var4[(0 & 1326226606) >> 4 & 227116033 ^ 147671798 ^ 147671799]);
                        mc.player.setSprinting((boolean)((1305054381 << 1 ^ 434313353) & 1601011717 ^ 39845889));
                        int var5 = 285466902 << 4 >>> 1 ^ 23808705 ^ 158614129;

                        while(true) {
                              if (((1892004084 >> 1 & 343713699) << 2 >>> 2 ^ 274759714) == 0) {
                                    ;
                              }

                              if (var5 >= ((1 >> 3 ^ 1443039877) & 156044656 & 6922 ^ 2050)) {
                                    mc.playerController.attackEntity(mc.player, var2);
                                    var5 = (438882327 ^ 148958670) >>> 2 ^ 78778230;

                                    while(var5 < ((1 >> 2 | 1023750425) & 656838063 ^ 621019403)) {
                                          this.setRotation(var6, var4[(0 ^ 544097025) & 217510506 ^ 6701569]);
                                          ++var5;
                                          if ((1052 >>> 1 ^ 526) == 0) {
                                                ;
                                          }
                                    }

                                    if (((153566952 | 35616092 | 3457264) << 1 ^ 947836095) != 0) {
                                          ;
                                    }

                                    if (!this.noSwingSet.enabled) {
                                          if (((167805232 | 27244346) << 3 ^ -1119952062) != 0) {
                                                ;
                                          }

                                          var11 = mc;
                                          if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("i hope you catch fire ngl")) {
                                                ;
                                          }

                                          var11.player.swingArm(EnumHand.MAIN_HAND);
                                    }

                                    if (((460122892 << 3 << 1 | 64985941) & 219399356 ^ 1676628490) != 0) {
                                          ;
                                    }
                                    break;
                              }

                              var4 = (float[])Fraerok2.getRotations(var2);
                              this.setRotation(var4[(237885580 << 4 | 2075312628) << 4 ^ -1073963200], var4[0 >> 1 >> 2 << 2 ^ 1]);
                              ++var5;
                        }
                  } else if (!mc.player.onGround && (double)mc.player.fallDistance >= 0.18462145D) {
                        this.hit.reset();
                        var3 = (float[])Fraerok2.getRotations(var1);
                        this.setRotation(var3[(693255105 | 398779534) ^ 904422955 ^ 171874788], var3[(0 << 1 << 3 ^ 1160964316) & 783806806 ^ 70443093]);
                        var9 = mc.playerController;
                        var13 = mc.player;
                        if (((594154846 | 379104603 | 541449880 | 227782505) ^ 1073741823) == 0) {
                              ;
                        }

                        var9.attackEntity(var13, var1);
                        if (!this.noSwingSet.enabled) {
                              mc.player.swingArm(EnumHand.MAIN_HAND);
                        }

                        if (!"i hope you catch fire ngl".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                              ;
                        }
                  }
            } else {
                  if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("nefariousMoment")) {
                        ;
                  }

                  if (((965463790 << 1 >> 4 | 47477031) >>> 4 ^ -1778566123) != 0) {
                        ;
                  }

                  if (((786210933 << 3 >> 1 >>> 4 ^ 40748297) & 175455 ^ 1217867021) != 0) {
                        ;
                  }

                  if (!ModuleManager.getModuleByName("Criticals").isToggled()) {
                        this.hit.reset();
                        var3 = (float[])Fraerok2.getRotations(var1);
                        this.setRotation(var3[((587464668 | 156065567) >>> 1 | 127768756) ^ 398458879], var3[0 >> 3 ^ 1007244016 ^ 765593042 ^ 296441635]);
                        mc.playerController.attackEntity(mc.player, var1);
                        if (!this.noSwingSet.enabled) {
                              EntityPlayerSP var15 = mc.player;
                              EnumHand var14 = EnumHand.MAIN_HAND;
                              if (((148445168 ^ 52773732) << 3 >> 3 ^ 1170164699) != 0) {
                                    ;
                              }

                              var15.swingArm(var14);
                        }
                  }
            }

      }

      public void module() {
      }

      public List sortByLivingTime(List var1) {
            int var2 = var1.size();

            for(int var3 = (156284169 ^ 11021820) >>> 1 >>> 2 ^ 20910878; var3 < var2; ++var3) {
                  if (!"stop skidding".equals("stringer is a good obfuscator")) {
                        ;
                  }

                  int var10001 = 0 >> 1 << 3 & 202799857 ^ 1;
                  if (!"stop skidding".equals("please go outside")) {
                        ;
                  }

                  int var4 = var3 + var10001;

                  while(true) {
                        if (((1521717089 << 4 | 1418567947) ^ 1441777334) != 0) {
                              ;
                        }

                        if (var4 >= var2) {
                              break;
                        }

                        int var10000 = ((Entity)var1.get(var4)).ticksExisted;
                        if (!"stop skidding".equals("intentMoment")) {
                              ;
                        }

                        if (var10000 > ((Entity)var1.get(var3)).ticksExisted) {
                              Object var6 = var1.get(var3);
                              if (((180959147 ^ 131403915 ^ 81781787 | 73833831) ^ 155395771 ^ 1373949 ^ 78647097) == 0) {
                                    ;
                              }

                              Entity var5 = (Entity)var6;
                              var1.set(var3, var1.get(var4));
                              var1.set(var4, var5);
                        }

                        if (((1764492195 | 574201945) >> 4 ^ 112450111) == 0) {
                              ;
                        }

                        ++var4;
                  }
            }

            return var1;
      }

      private static boolean canShootBow(EntityPlayer var0) {
            if (var0.isCreative()) {
                  return (boolean)((((0 | 1163904972) >>> 3 | 35192811) & 80075374 | 6935024) ^ 15326203);
            } else {
                  Iterator var1 = var0.inventory.mainInventory.iterator();

                  while(true) {
                        boolean var10000 = var1.hasNext();
                        if ("please go outside".equals("please dont crack my plugin")) {
                        }

                        if (!var10000) {
                              return (boolean)((649375748 | 249446672) << 3 ^ 2012711072);
                        }

                        ItemStack var2 = (ItemStack)var1.next();
                        if (var2.getItem() == Items.ARROW) {
                              return (boolean)(0 << 2 >> 1 & 1815704201 ^ 1);
                        }

                        if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("please dont crack my plugin")) {
                              ;
                        }
                  }
            }
      }

      private boolean attackCheck(Entity var1) {
            Iterator var2 = FriendManager.friends.iterator();

            while(var2.hasNext()) {
                  Friend var3 = (Friend)var2.next();
                  if (var1.getName().equals(var3.name)) {
                        return (boolean)((1970916626 | 98864666) >>> 2 ^ 494891974);
                  }
            }

            if (!this.invisibleTarget.isEnabled() && var1.isInvisible()) {
                  return (boolean)(((96616444 ^ 50211005) >>> 4 & 5198564 | 2234463) ^ 6510719);
            } else {
                  if (((1362899901 | 12337315 | 245880967) ^ -434169717) != 0) {
                        ;
                  }

                  if (this.playersSet.isEnabled() && var1 instanceof EntityPlayer && ((EntityLivingBase)var1).getHealth() > 0.0F) {
                        return (boolean)(0 >>> 3 >> 4 ^ 1854414494 ^ 1854414495);
                  } else {
                        int var4;
                        if (this.animalsSet.isEnabled()) {
                              if ((68507465 << 1 >> 3 ^ -51194778) != 0) {
                                    ;
                              }

                              if (var1 instanceof EntityAnimal) {
                                    var4 = !(var1 instanceof EntityTameable) ? 0 & 2094625614 ^ 1286860762 ^ 1286860763 : 572927448 << 4 >>> 1 >>> 2 << 4 << 4 ^ 1281077248;
                                    if (!"buy a domain and everything else you need at namecheap.com".equals("you probably spell youre as your")) {
                                          ;
                                    }

                                    return (boolean)var4;
                              }
                        }

                        if (this.mobsSet.isEnabled()) {
                              boolean var10000 = var1 instanceof EntityMob;
                              if (((55731596 | 30455320) >>> 4 & 348883 ^ 328273) == 0) {
                                    ;
                              }

                              if (var10000) {
                                    var4 = (0 ^ 156691299) >> 3 ^ 19586413;
                                    return (boolean)var4;
                              }
                        }

                        var4 = 548116599 >> 3 >> 2 ^ 17128643;
                        return (boolean)var4;
                  }
            }
      }

      public boolean swichfucked() {
            this.swichable = (boolean)(!this.swichable ? (0 & 700081437 ^ 723489049) >>> 4 ^ 45218064 : (462440720 >> 4 ^ 28292093) << 4 << 4 ^ 381119488);
            if (((606277376 | 52979811 | 441461766) ^ 1065054055) == 0) {
                  ;
            }

            return this.swichable;
      }

      public static float[] getRotations(Entity var0) {
            double var10000 = var0.posX + (var0.posX - var0.lastTickPosX);
            if (((1610661851 ^ 640486910) & 189908708 ^ -652768964) != 0) {
                  ;
            }

            Minecraft var10001 = mc;
            if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("stringer is a good obfuscator")) {
                  ;
            }

            double var1 = var10000 - var10001.player.posX;
            var10000 = var0.posY + (double)var0.getEyeHeight() - mc.player.posY;
            float var11 = mc.player.getEyeHeight();
            if (!"intentMoment".equals("i hope you catch fire ngl")) {
                  ;
            }

            double var3 = var10000 + (double)var11 - 3.5D;
            var10000 = var0.posZ;
            if (!"stop skidding".equals("your mom your dad the one you never had")) {
                  ;
            }

            var10000 += var0.posZ - var0.lastTickPosZ;
            var10001 = mc;
            if (!"nefariousMoment".equals("please get a girlfriend and stop cracking plugins")) {
                  ;
            }

            double var5 = var10000 - var10001.player.posZ;
            double var7 = Math.sqrt(Math.pow(var1, 2.0D) + Math.pow(var5, 2.0D));
            float var9 = (float)Math.toDegrees(-Math.atan(var1 / var5));
            float var10 = (float)(-Math.toDegrees(Math.atan(var3 / var7)));
            if (var1 < 0.0D && var5 < 0.0D) {
                  var10000 = 90.0D + Math.toDegrees(Math.atan(var5 / var1));
                  if (((948935919 | 419081779) ^ 341066843 ^ 749468324) == 0) {
                        ;
                  }

                  var9 = (float)var10000;
            } else if (var1 > 0.0D && var5 < 0.0D) {
                  if ((((2055393500 ^ 579314110) << 1 << 1 | 86222312 | 194036049) ^ -1552216549) != 0) {
                        ;
                  }

                  var9 = (float)(-90.0D + Math.toDegrees(Math.atan(var5 / var1)));
            }

            float[] var12 = new float[((1 & 0) >> 1 | 1233900080 | 359842355) & 1047298605 ^ 476611107];
            var12[(18087944 >> 4 & 729428) << 3 >>> 4 ^ '耀'] = var9;
            var12[(0 | 856010485) & 441500495 ^ 302023236] = var10;
            return var12;
      }

      public static void resetPressed(KeyBinding var0) {
            if (((77308411 ^ 65643098 | 77733227) & 29632923 ^ 29362571) == 0) {
                  ;
            }

            Fraerok2.setPressed(var0, GameSettings.isKeyDown(var0));
      }

      public List sortByDistance(List var1) {
            if (((984220367 | 56950395) >>> 2 << 1 ^ 502759294) == 0) {
                  ;
            }

            int var2 = var1.size();

            for(int var3 = (451384376 << 1 ^ 514991044) >>> 1 ^ 364807642; var3 < var2; ++var3) {
                  int var4 = var3 + (((0 << 3 & 83673830) << 2 | 1065341349) ^ 1065341348);

                  while(true) {
                        if ((526161926 >>> 4 << 3 ^ 263080960) == 0) {
                              ;
                        }

                        if (var4 >= var2) {
                              break;
                        }

                        EntityPlayerSP var10000 = mc.player;
                        if (((405181507 >> 3 ^ 10690372) >> 3 << 4 ^ 122657680) == 0) {
                              ;
                        }

                        float var6 = var10000.getDistance((Entity)var1.get(var3));
                        EntityPlayerSP var10001 = mc.player;
                        if ((95143945 << 3 << 2 >> 1 ^ -257914335) != 0) {
                              ;
                        }

                        if (var6 > var10001.getDistance((Entity)var1.get(var4))) {
                              if (((68158472 ^ 27418408) & 70273660 ^ 70273568) == 0) {
                                    ;
                              }

                              Entity var5 = (Entity)var1.get(var3);
                              var1.set(var3, var1.get(var4));
                              var1.set(var4, var5);
                        }

                        ++var4;
                  }
            }

            return var1;
      }

      private static boolean isPlayerShielded(EntityPlayer var0) {
            int var10000;
            label30: {
                  if (var0.getItemInUseCount() > 0) {
                        if (var0.getHeldItemMainhand().getItem() instanceof ItemShield) {
                              break label30;
                        }

                        if (!"you probably spell youre as your".equals("your mom your dad the one you never had")) {
                              ;
                        }

                        if (var0.getHeldItemOffhand().getItem() instanceof ItemShield && !Fraerok2.isPlayerUsingMainhand(var0)) {
                              break label30;
                        }
                  }

                  var10000 = (17318020 | 6032604) >> 4 ^ 1426637;
                  return (boolean)var10000;
            }

            var10000 = (0 ^ 2077346471) << 2 ^ -280548707;
            return (boolean)var10000;
      }

      public List sortByHealth(List var1) {
            int var2 = var1.size();

            for(int var3 = 1428190540 >>> 3 << 3 >>> 2 >> 2 ^ 89261908; var3 < var2; ++var3) {
                  int var4 = var3 + (0 << 4 & 965462271 ^ 1);

                  while(var4 < var2) {
                        if (((984681146 ^ 395596476 | 444609518) ^ 859231039 ^ 211003601) == 0) {
                              ;
                        }

                        if (((EntityLivingBase)var1.get(var3)).getHealth() > ((EntityLivingBase)var1.get(var4)).getHealth()) {
                              Entity var5 = (Entity)var1.get(var3);
                              var1.set(var3, var1.get(var4));
                              var1.set(var4, var5);
                              if (!"please get a girlfriend and stop cracking plugins".equals("buy a domain and everything else you need at namecheap.com")) {
                                    ;
                              }
                        }

                        ++var4;
                        if ((417589440 << 1 << 4 >>> 2 ^ -1104498788) != 0) {
                              ;
                        }
                  }
            }

            if (!"stringer is a good obfuscator".equals("ape covered in human flesh")) {
                  ;
            }

            return var1;
      }
}
