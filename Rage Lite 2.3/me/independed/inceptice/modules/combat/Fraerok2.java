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
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemAppleGold;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.EnumHand;
import net.minecraft.util.FoodStats;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class Fraerok2 extends Module {
      public BooleanSetting swordOnly;
      public static boolean checkArmorita;
      TimerUtil hit;
      public NumberSetting delaySet;
      public BooleanSetting playersSet;
      public BooleanSetting animalsSet;
      public BooleanSetting refreshTargets;
      public ModeSetting modePrior;
      public BooleanSetting noSwingSet;
      public static String activeModeKiller = null;
      public BooleanSetting mobsSet;
      private TimerUtil shieldAttack;
      public BooleanSetting pitchTarget;
      public static String modeSorted;
      private boolean refreshed;
      public BooleanSetting autoDelaySet;
      public BooleanSetting yawTarget;
      public NumberSetting fovSet;
      public static EntityLivingBase currentTarget = null;
      public ModeSetting modeSetting;
      public boolean swichable;
      public BooleanSetting invisibleTarget;
      public NumberSetting rangeSet;
      List targets;
      public BooleanSetting diamondArmor;
      private Random random = new Random();

      public boolean swichfucked() {
            int var10001;
            if (!this.swichable) {
                  var10001 = 0 << 3 >>> 2 << 3 ^ 1;
            } else {
                  var10001 = 1950391704 << 4 & 145015446 ^ '삀';
                  if ((472784224 >>> 3 >>> 4 >> 2 & 600833 ^ 607225846) != 0) {
                        ;
                  }
            }

            this.swichable = (boolean)var10001;
            return this.swichable;
      }

      public void module() {
      }

      public Fraerok2() {
            super("KiIlAura", "automatically hits enemies", (1152718613 ^ 1087679892) << 4 ^ 1176217616, Module.Category.COMBAT);
            if ((740347257 >> 4 & 31589412 ^ 12715012) == 0) {
                  ;
            }

            TimerUtil var10001 = new TimerUtil;
            if (((1424798825 >> 3 | 68654661) ^ 76831770 ^ -922245371) != 0) {
                  ;
            }

            var10001.<init>();
            this.hit = var10001;
            this.swichable = (boolean)((0 ^ 218841818) >>> 1 >>> 1 ^ 54710455);
            this.refreshed = (boolean)(((1994372931 ^ 971964360) & 802820352) << 2 ^ 1010827264);
            if (((141050118 >> 2 >> 1 | 14271321) >> 1 ^ 873006341) != 0) {
                  ;
            }

            NumberSetting var1 = new NumberSetting;
            if (((62380494 ^ 34092106 ^ 3085476) >>> 4 ^ 1401022 ^ 111844951) != 0) {
                  ;
            }

            var1.<init>("Range", this, 3.5D, 3.0D, 10.0D, 0.1D);
            this.rangeSet = var1;
            var1 = new NumberSetting;
            if (!"shitted on you harder than archybot".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                  ;
            }

            var1.<init>("Fov", this, 360.0D, 0.0D, 360.0D, 2.0D);
            this.fovSet = var1;
            BooleanSetting var2 = new BooleanSetting;
            if (((1001555736 | 366798819) & 10524652 & 7907956 ^ -715294058) != 0) {
                  ;
            }

            var2.<init>("No Swing", this, (boolean)((1594698056 ^ 978896666 ^ 540944658) & 408926044 ^ 4949824));
            this.noSwingSet = var2;
            if ((392195082 << 3 << 4 ^ 490280795 ^ -1391521189) == 0) {
                  ;
            }

            this.animalsSet = new BooleanSetting("Animals", this, (boolean)(((1410982038 ^ 323415462) >> 4 | 29058192) ^ 100660435));
            var2 = new BooleanSetting;
            if (((507336616 | 367891622) << 1 >>> 3 ^ 134182379) == 0) {
                  ;
            }

            var2.<init>("Players", this, (boolean)(0 >>> 4 >>> 1 >>> 1 ^ 238993608 ^ 238993609));
            this.playersSet = var2;
            var2 = new BooleanSetting;
            if (((844069079 >> 2 & 176668163 | 62866621) ^ 197123773) == 0) {
                  ;
            }

            int var10005 = (1527536818 | 1295199332) >>> 4 >> 2 << 2 ^ 99874764;
            if (!"please go outside".equals("intentMoment")) {
                  ;
            }

            var2.<init>("Mobs", this, (boolean)var10005);
            this.mobsSet = var2;
            this.invisibleTarget = new BooleanSetting("Invisible", this, (boolean)(268771333 >>> 3 << 4 ^ 537542656));
            var2 = new BooleanSetting;
            if (!"intentMoment".equals("you probably spell youre as your")) {
                  ;
            }

            var2.<init>("AutoDelay", this, (boolean)((((0 | 525694439) & 420981468) << 1 | 557089502) ^ 859751391));
            this.autoDelaySet = var2;
            this.delaySet = new NumberSetting("Delay", this, 590.0D, 1.0D, 1500.0D, 1.0D);
            var2 = new BooleanSetting;
            if ((518871140 >> 1 >> 4 & 12118223 ^ -39735824) != 0) {
                  ;
            }

            var2.<init>("Horizontal", this, (boolean)(0 << 4 & 1647691799 ^ 1));
            this.yawTarget = var2;
            this.swordOnly = new BooleanSetting("SwordOnly", this, (boolean)((181988219 | 113900310) & 138031842 ^ 135803490));
            this.pitchTarget = new BooleanSetting("Vertical", this, (boolean)((1800847175 | 270283226) >>> 4 << 4 ^ 788134645 ^ 1437025573));
            this.diamondArmor = new BooleanSetting("DiamondArm", this, (boolean)(494829549 << 1 ^ 399938166 ^ 400340937 ^ 989256293));
            if ((1690491981 >> 3 << 3 ^ -1008278836) != 0) {
                  ;
            }

            if ((4329729 ^ -579138492) != 0) {
                  ;
            }

            var2 = new BooleanSetting;
            var10005 = 447864840 >> 1 << 4 ^ -712048576;
            if ((((932315595 ^ 241076540) << 2 & 1130253825 | 1039000957) ^ 2147477373) == 0) {
                  ;
            }

            var2.<init>("Refresh", this, (boolean)var10005);
            this.refreshTargets = var2;
            ModeSetting var3 = new ModeSetting;
            String[] var10006 = new String[(0 | 1307304381 | 261648201 | 313236286) ^ 1610350589];
            var10006[(1276759475 >>> 2 | 120872422) >> 1 & 61593160 ^ 59446848] = "Bypass";
            if ((((1687090919 | 1664493332) << 1 | 1914595710) ^ -8392706) == 0) {
                  ;
            }

            var10006[0 & 817674330 ^ 1272024436 ^ 856193882 ^ 1675544015 ^ 453456864] = "Simple";
            var3.<init>("Mode", this, "Bypass", var10006);
            this.modeSetting = var3;
            var3 = new ModeSetting;
            var10006 = new String[(0 | 28377409) << 4 >>> 1 ^ 164245837 ^ 71449414];
            var10006[(1402618344 | 929138687 | 878524931) ^ 637810536 ^ 49945070 ^ 1392628089] = "Distance";
            var10006[(0 ^ 1838183080 ^ 567936203 ^ 1191113957 ^ 130328277) >>> 1 ^ 112726568] = "Health";
            if (((447707061 << 1 & 730014224 | 3220222) ^ 556999422) == 0) {
                  ;
            }

            var10006[1 << 4 >>> 3 ^ 0] = "LivingTime";
            var3.<init>("Prior", this, "LivingTime", var10006);
            this.modePrior = var3;
            var10001 = new TimerUtil;
            if ((19025028 >> 2 >>> 3 >>> 2 ^ 661969708) != 0) {
                  ;
            }

            var10001.<init>();
            this.shieldAttack = var10001;
            if ((896131132 << 3 >> 3 << 2 ^ 1660234870) != 0) {
                  ;
            }

            Setting[] var4 = new Setting[(2 ^ 1) & 0 ^ 2126944742 ^ 2126944745];
            var4[1291225520 >> 2 >> 2 ^ 19498660 ^ 13864781 ^ 87384434] = this.rangeSet;
            int var10003 = 0 >> 3 >> 4 ^ 1;
            if (((1634136405 << 2 | 278842015) ^ -1784688673) == 0) {
                  ;
            }

            var4[var10003] = this.fovSet;
            var4[(1 >>> 4 ^ 473244347 ^ 460977695) >> 3 ^ 2686787 ^ 12674517] = this.noSwingSet;
            var4[((0 | 183652579) >> 4 & 7755319) >> 3 ^ 311427] = this.animalsSet;
            var4[((0 ^ 806400104) >>> 1 | 230016050) ^ 498981938] = this.mobsSet;
            var4[(2 ^ 1) << 1 ^ 2 ^ 1] = this.invisibleTarget;
            var4[((2 | 0) ^ 0) >> 4 ^ 6] = this.autoDelaySet;
            if ((((599051590 ^ 376751475 | 144909327) & 108050157) << 2 ^ 293638324) == 0) {
                  ;
            }

            var10003 = (3 >> 4 | 535685627) << 1 ^ 1010175331 ^ 65920658;
            if (((1323673470 ^ 84441017) & 1127783194 & 956700706 & 8745069 ^ 4096) == 0) {
                  ;
            }

            var4[var10003] = this.delaySet;
            var10003 = (6 >>> 1 & 2 | 0) ^ 10;
            if (!"you probably spell youre as your".equals("shitted on you harder than archybot")) {
                  ;
            }

            var4[var10003] = this.yawTarget;
            var4[7 >>> 4 << 2 >>> 4 >> 3 ^ 9] = this.swordOnly;
            var4[6 >> 1 >> 4 ^ 10] = this.pitchTarget;
            var4[7 << 3 ^ 29 ^ 46] = this.modeSetting;
            if ((((1019795481 ^ 425320837) >>> 2 | 89593951) ^ 1746508257) != 0) {
                  ;
            }

            var4[(5 << 1 | 4) >> 3 ^ 13] = this.modePrior;
            if ((80552516 >>> 1 << 3 ^ 1474774 ^ -349572289) != 0) {
                  ;
            }

            var10003 = (8 >>> 1 ^ 2 | 2) >>> 4 >> 3 ^ 13;
            if ((((98519924 << 4 | 1101726079) ^ 1364282521 | 212505604) ^ 212795366) == 0) {
                  ;
            }

            var4[var10003] = this.diamondArmor;
            var4[(4 ^ 0) << 2 >> 3 >>> 3 ^ 14] = this.refreshTargets;
            this.addSettings(var4);
      }

      public static void resetPressed(KeyBinding var0) {
            Fraerok2.setPressed(var0, GameSettings.isKeyDown(var0));
      }

      public List sortByHealth(List var1) {
            int var2 = var1.size();
            int var3 = (896210037 << 3 & 198959999 | 28984094) ^ 200991550;

            while(true) {
                  if ((119581898 >>> 3 >> 2 >>> 2 ^ 331857133) != 0) {
                        ;
                  }

                  if (var3 >= var2) {
                        if (!"minecraft".equals("you're dogshit")) {
                              ;
                        }

                        if (((134512640 | 115889944 | 112994586) ^ 358271483) != 0) {
                              ;
                        }

                        return var1;
                  }

                  if (!"ape covered in human flesh".equals("buy a domain and everything else you need at namecheap.com")) {
                        ;
                  }

                  for(int var4 = var3 + ((0 >>> 1 | 597535447) & 313685076 ^ 42999893); var4 < var2; ++var4) {
                        if (((EntityLivingBase)var1.get(var3)).getHealth() > ((EntityLivingBase)var1.get(var4)).getHealth()) {
                              Entity var5 = (Entity)var1.get(var3);
                              var1.set(var3, var1.get(var4));
                              var1.set(var4, var5);
                        }
                  }

                  if (!"You're so fat whenever you go to the beach the tide comes in.".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                        ;
                  }

                  ++var3;
            }
      }

      public static void setPressed(KeyBinding var0, boolean var1) {
            try {
                  if (!"stringer is a good obfuscator".equals("please get a girlfriend and stop cracking plugins")) {
                        ;
                  }

                  Field var2 = var0.getClass().getDeclaredField("pressed");
                  if (((931166878 << 3 ^ 1497032148 | 979044274) ^ -8702026) == 0) {
                        ;
                  }

                  if ((186675275 << 1 >> 3 >>> 4 ^ 311535353) != 0) {
                        ;
                  }

                  var2.setAccessible((boolean)(((0 ^ 1079041396 ^ 154987260) & 460725696) >> 2 ^ 39388513));
                  var2.setBoolean(var0, var1);
            } catch (ReflectiveOperationException var3) {
                  throw new RuntimeException(var3);
            }
      }

      private static boolean canShootBow(EntityPlayer var0) {
            if (var0.isCreative()) {
                  return (boolean)(((0 | 1898432879) ^ 1762872100) >>> 1 ^ 203047204);
            } else {
                  InventoryPlayer var10000 = var0.inventory;
                  if ((139108802 >> 4 >>> 4 >> 2 ^ 135848) == 0) {
                        ;
                  }

                  Iterator var1 = var10000.mainInventory.iterator();

                  while(var1.hasNext()) {
                        ItemStack var2 = (ItemStack)var1.next();
                        if (var2.getItem() == Items.ARROW) {
                              return (boolean)(((0 ^ 1370985800 | 33310895) & 290689160) >> 3 ^ 36336144);
                        }

                        if (((742932480 >>> 3 ^ 11001922) >>> 2 ^ 21740048) == 0) {
                              ;
                        }
                  }

                  return (boolean)(((441737797 ^ 160375429) << 3 >> 3 | 74549040) ^ -134494224);
            }
      }

      static {
            if (((96733313 >>> 4 ^ 2061141) << 1 >>> 3 ^ 1101047) == 0) {
                  ;
            }

            checkArmorita = (boolean)(((699558048 ^ 16919265) & 492893193 & 4699528) >> 2 ^ 0);
      }

      private static boolean isPlayerShielded(EntityPlayer var0) {
            int var10000;
            label27: {
                  label26: {
                        if (var0.getItemInUseCount() > 0) {
                              if (var0.getHeldItemMainhand().getItem() instanceof ItemShield) {
                                    break label26;
                              }

                              if (((658916685 << 4 ^ 1207842426 | 542801593) ^ -608893007) != 0) {
                                    ;
                              }

                              if (var0.getHeldItemOffhand().getItem() instanceof ItemShield && !Fraerok2.isPlayerUsingMainhand(var0)) {
                                    break label26;
                              }
                        }

                        var10000 = 1369285333 << 4 >> 4 ^ 27108053;
                        break label27;
                  }

                  var10000 = 0 << 4 << 3 >> 3 ^ 1;
            }

            if (((1898634140 << 4 ^ 63979651) >>> 4 ^ 18341780) == 0) {
                  ;
            }

            return (boolean)var10000;
      }

      public static float nextFloat(float var0, float var1) {
            if (var0 != var1) {
                  if (((83035666 | 18876218) >> 1 ^ 72635325) != 0) {
                        ;
                  }

                  if (var1 - var0 > 0.0F) {
                        if ((42025391 << 1 >> 2 >> 2 ^ 62463861) != 0) {
                              ;
                        }

                        return (float)((double)var0 + (double)(var1 - var0) * Math.random());
                  }
            }

            return var0;
      }

      private void setRotation(float var1, float var2) {
            mc.player.renderYawOffset = var1;
            mc.player.rotationYawHead = var1;
            if (!"You're so fat whenever you go to the beach the tide comes in.".equals("nefariousMoment")) {
                  ;
            }

            double var5;
            double var10000;
            int var10001;
            if (this.random.nextBoolean()) {
                  var10000 = (double)var1;
                  var10001 = this.random.nextInt(50 >>> 3 << 1 << 1 ^ 124);
                  if ((16782380 << 1 ^ 1766324201) != 0) {
                        ;
                  }

                  var1 = (float)(var10000 + (double)var10001 * 0.03D);
            } else {
                  var10000 = (double)var1;
                  var10001 = this.random.nextInt((77 ^ 2) >>> 4 << 1 >>> 1 ^ 96);
                  if ((((1377960017 | 1321123786) >>> 4 & 71744478 | 28187871) ^ -466271969) != 0) {
                        ;
                  }

                  var5 = (double)var10001 * 0.05D;
                  if (!"nefariousMoment".equals("i hope you catch fire ngl")) {
                        ;
                  }

                  var1 = (float)(var10000 - var5);
            }

            if (this.random.nextBoolean()) {
                  Random var6 = this.random;
                  if (((1452580033 ^ 1404383747) << 4 >>> 3 ^ 172187012) == 0) {
                        ;
                  }

                  int var10002 = 5 ^ 2 ^ 4 ^ 5;
                  if ((2013691467 << 3 ^ 1257254377 ^ 1928847624) != 0) {
                        ;
                  }

                  var2 += (float)var6.nextInt(var10002);
            } else {
                  var2 -= (float)this.random.nextInt((10 ^ 0) >>> 2 >>> 1 ^ 12);
            }

            int var3;
            EntityPlayerSP var4;
            Random var10;
            float var11;
            if (this.pitchTarget.isEnabled()) {
                  if (var2 >= 0.0F) {
                        if (var2 < mc.player.rotationPitch) {
                              while(var2 < mc.player.rotationPitch) {
                                    var4 = mc.player;
                                    var4.rotationPitch -= (float)this.random.nextInt((2 & 0) >> 1 >> 3 >> 4 ^ 5);
                              }
                        } else {
                              while(true) {
                                    if (((1791357995 << 2 | 1428562649) ^ 202422510 ^ 567085769) == 0) {
                                    }

                                    float var14;
                                    var3 = (var14 = var2 - mc.player.rotationPitch) == 0.0F ? 0 : (var14 < 0.0F ? -1 : 1);
                                    if (!"idiot".equals("idiot")) {
                                          ;
                                    }

                                    if (var3 <= 0) {
                                          break;
                                    }

                                    var4 = mc.player;
                                    var4.rotationPitch += (float)this.random.nextInt((2 >>> 1 >>> 3 & 2116512777) << 2 ^ 5);
                              }
                        }
                  } else {
                        EntityPlayerSP var7 = mc.player;
                        if (((1670043940 << 1 ^ 1539675326) >>> 3 >> 2 ^ 82215223) == 0) {
                              ;
                        }

                        float var9;
                        if (var2 < var7.rotationPitch) {
                              for(; var2 < mc.player.rotationPitch; var4.rotationPitch = var11 - (float)var10.nextInt((4 & 3) >>> 2 ^ 22940013 ^ 22940008)) {
                                    if ((654031241 >>> 4 >>> 4 ^ 25807 ^ -703168440) != 0) {
                                          ;
                                    }

                                    var4 = mc.player;
                                    var11 = var4.rotationPitch;
                                    var10 = this.random;
                                    if (!"stop skidding".equals("stringer is a good obfuscator")) {
                                          ;
                                    }
                              }
                        } else {
                              for(; var2 > mc.player.rotationPitch; var4.rotationPitch = var11 + var9) {
                                    if ((269500704 << 4 & 4261931 & 686131083 ^ 467961823) != 0) {
                                          ;
                                    }

                                    var4 = mc.player;
                                    var11 = var4.rotationPitch;
                                    var9 = (float)this.random.nextInt(2 >> 1 >> 4 ^ 5);
                                    if (((576474611 | 171057306) >> 3 ^ 89116095) == 0) {
                                          ;
                                    }
                              }
                        }
                  }
            }

            if (!"shitted on you harder than archybot".equals("shitted on you harder than archybot")) {
                  ;
            }

            BooleanSetting var8 = this.yawTarget;
            if ((1017505410 >> 2 ^ 225091099 ^ 38004667) == 0) {
                  ;
            }

            if (var8.isEnabled()) {
                  float var15;
                  var3 = (var15 = var1 - 0.0F) == 0.0F ? 0 : (var15 < 0.0F ? -1 : 1);
                  if (~(1056866260 << 3 | 983981919 | 1744542893) == 0) {
                        ;
                  }

                  Minecraft var12;
                  if (var3 >= 0) {
                        double var13;
                        int var10003;
                        if (mc.player.rotationYaw < var1) {
                              while(true) {
                                    var12 = mc;
                                    if (((849985794 >> 1 | 196874103) >>> 3 ^ 53390605 ^ 5320947) == 0) {
                                          ;
                                    }

                                    if (var12.player.rotationYaw >= var1) {
                                          break;
                                    }

                                    var4 = mc.player;
                                    var5 = (double)var4.rotationYaw;
                                    var10 = this.random;
                                    var10003 = 93 << 4 >> 4 ^ 57;
                                    if ((1157890892 >>> 3 ^ -1141940386) != 0) {
                                          ;
                                    }

                                    var13 = (double)var10.nextInt(var10003) * 0.001D % 360.0D;
                                    if (((124241075 | 65298683) << 4 >>> 2 >>> 3 ^ 894311630) != 0) {
                                          ;
                                    }

                                    var4.rotationYaw = (float)(var5 + var13);
                              }
                        } else {
                              while(mc.player.rotationYaw > var1) {
                                    if (!"please take a shower".equals("your mom your dad the one you never had")) {
                                          ;
                                    }

                                    var4 = mc.player;
                                    var5 = (double)var4.rotationYaw;
                                    var10 = this.random;
                                    var10003 = (19 | 12) ^ 16 ^ 107;
                                    if (((406738448 >> 1 ^ 106516080) << 3 >> 2 ^ -1706963177) != 0) {
                                          ;
                                    }

                                    var13 = (double)var10.nextInt(var10003);
                                    if (!"intentMoment".equals("please dont crack my plugin")) {
                                          ;
                                    }

                                    var5 -= var13 * 0.001D % 360.0D;
                                    if (!"you probably spell youre as your".equals("please dont crack my plugin")) {
                                          ;
                                    }

                                    var4.rotationYaw = (float)var5;
                                    if (((365409011 ^ 160287965) >>> 4 << 2 >> 2 ^ 1613620079) != 0) {
                                          ;
                                    }
                              }
                        }
                  } else {
                        float var16;
                        var3 = (var16 = mc.player.rotationYaw - var1) == 0.0F ? 0 : (var16 < 0.0F ? -1 : 1);
                        if (!"please go outside".equals("please take a shower")) {
                              ;
                        }

                        if (var3 < 0) {
                              while(mc.player.rotationYaw < var1) {
                                    var12 = mc;
                                    if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("shitted on you harder than archybot")) {
                                          ;
                                    }

                                    var4 = var12.player;
                                    var4.rotationYaw = (float)((double)var4.rotationYaw + (double)this.random.nextInt(((70 & 55) >> 2 | 0) ^ 101) * 0.001D % 360.0D);
                              }
                        } else {
                              for(; mc.player.rotationYaw > var1; var4.rotationYaw = var11) {
                                    var4 = mc.player;
                                    var11 = (float)((double)var4.rotationYaw - (double)this.random.nextInt(((99 << 3 | 657) ^ 189) << 2 >> 3 ^ 502) * 0.001D % 360.0D);
                                    if (!"minecraft".equals("minecraft")) {
                                          ;
                                    }
                              }
                        }
                  }
            }

      }

      public void attackTarget(Entity var1) {
            if (var1 instanceof EntityPlayer) {
                  EntityPlayer var10000 = (EntityPlayer)var1;
                  if (((737027780 << 4 << 3 ^ 2079546895) >> 2 << 4 ^ -996198719) != 0) {
                        ;
                  }

                  if (Fraerok2.isPlayerShielded(var10000) && mc.player.getHeldItemMainhand().getItem() instanceof ItemAxe) {
                        if (!"please dont crack my plugin".equals("please dont crack my plugin")) {
                              ;
                        }

                        if (this.shieldAttack.hasReached(60.0D)) {
                              if ((((278722634 ^ 190763971) & 395609806 | 138083022) ^ 465238734) == 0) {
                                    ;
                              }

                              mc.playerController.attackEntity(mc.player, var1);
                              if ((635584311 << 3 << 2 << 2 ^ -249586816) == 0) {
                                    ;
                              }

                              this.shieldAttack.reset();
                        }
                  }
            }

            Entity var2 = var1;
            if (this.autoDelaySet.isEnabled()) {
                  if ((double)mc.player.getCooledAttackStrength(1.0F) < 1.0D) {
                        return;
                  }
            } else {
                  TimerUtil var7 = this.hit;
                  NumberSetting var10001 = this.delaySet;
                  if ((((1698491293 >> 1 | 838827118) & 523430449 | 87375575 | 61201146) ^ 397934335) == 0) {
                        ;
                  }

                  if (!var7.hasReached(var10001.getValue())) {
                        return;
                  }
            }

            float[] var3;
            float var10;
            boolean var12;
            if (this.modeSetting.activeMode == "Bypass") {
                  if (ModuleManager.getModuleByName("Criticals").isToggled()) {
                        Minecraft var8;
                        if (Criticals.isRage) {
                              this.hit.reset();
                              var3 = (float[])Fraerok2.getRotations(var1);
                              var10 = var3[(1450401817 | 548795576) ^ 322763576 ^ 1707806593];
                              if (((1426010640 | 1025789961) >>> 1 ^ 1056946444) == 0) {
                                    ;
                              }

                              this.setRotation(var10, var3[((0 << 2 | 1690848108) << 2 | 2030808087) ^ -81023562]);
                              var8 = mc;
                              if (!"please go outside".equals("you're dogshit")) {
                                    ;
                              }

                              var8.player.setSprinting((boolean)((1060006408 >>> 4 | 24710483) & 46322377 ^ 46318273));
                              PlayerControllerMP var9 = mc.playerController;
                              if (((2129340758 ^ 1290747751) << 4 << 2 >>> 3 ^ 270578056) == 0) {
                                    ;
                              }

                              var9.attackEntity(mc.player, var1);
                              if (!"stop skidding".equals("buy a domain and everything else you need at namecheap.com")) {
                                    ;
                              }

                              if (!this.noSwingSet.enabled) {
                                    mc.player.swingArm(EnumHand.MAIN_HAND);
                              }
                        } else if (!mc.player.onGround && (double)mc.player.fallDistance >= 0.18462145D) {
                              if (!"intentMoment".equals("nefariousMoment")) {
                                    ;
                              }

                              this.hit.reset();
                              var3 = (float[])Fraerok2.getRotations(var1);
                              if (!"you're dogshit".equals("your mom your dad the one you never had")) {
                                    ;
                              }

                              if (!"idiot".equals("you probably spell youre as your")) {
                                    ;
                              }

                              this.setRotation(var3[(770879190 ^ 159436701 ^ 355433423 | 384346494) >> 3 ^ 117421951], var3[0 & 1515859487 & 1978685458 ^ 1]);
                              var8 = mc;
                              if ((330249686 << 2 >>> 2 ^ 190163928 ^ 68026142) != 0) {
                                    ;
                              }

                              var8.playerController.attackEntity(mc.player, var1);
                              if (!this.noSwingSet.enabled) {
                                    mc.player.swingArm(EnumHand.MAIN_HAND);
                              }
                        }
                  } else {
                        if (((237068822 >>> 4 | 2038220) ^ 454359612) != 0) {
                              ;
                        }

                        Module var11 = ModuleManager.getModuleByName("Criticals");
                        if ((((1684978584 ^ 1052690952) << 4 | 30492040) ^ -619867747) != 0) {
                              ;
                        }

                        if (!var11.isToggled()) {
                              if (((1873183877 >> 3 << 1 | 223982928) ^ 536458608) == 0) {
                                    ;
                              }

                              this.hit.reset();
                              if ((295234 >> 2 & 20169 ^ 64) == 0) {
                                    ;
                              }

                              var3 = (float[])Fraerok2.getRotations(var1);
                              this.setRotation(var3[(71371227 << 3 ^ 159235201) >>> 3 << 2 ^ 364575020], var3[(0 << 4 >> 2 | 391633487) ^ 391633486]);
                              mc.playerController.attackEntity(mc.player, var1);
                              var12 = this.noSwingSet.enabled;
                              if ((1021789400 >>> 4 << 3 ^ 510894696) == 0) {
                                    ;
                              }

                              if (!var12) {
                                    mc.player.swingArm(EnumHand.MAIN_HAND);
                              }
                        }
                  }
            } else if (ModuleManager.getModuleByName("Criticals").isToggled()) {
                  if ((819737091 << 2 & 841788976 ^ 1378171758) != 0) {
                        ;
                  }

                  if (Criticals.isRage) {
                        this.hit.reset();
                        float var6 = mc.player.rotationYaw;
                        float[] var4 = (float[])Fraerok2.getRotations(var1);
                        var10 = var4[(1293613154 | 1051745218 | 1986667179 | 1782911436) ^ 2147482607];
                        if ((((1078224124 << 1 ^ 707221445) >> 4 | 118409811) ^ -5309453) == 0) {
                              ;
                        }

                        this.setRotation(var10, var4[((0 | 705826139) ^ 19944568) >> 4 ^ 45229459]);
                        if ((562555568 << 3 << 1 & 267598700 & 30502132 ^ 5253120) == 0) {
                              ;
                        }

                        mc.player.setSprinting((boolean)(2041528011 >>> 4 & 108126652 ^ 101769644));

                        int var5;
                        for(var5 = (448835298 >> 1 ^ 25775733) >> 3 ^ 27075360; var5 < (1 >>> 2 ^ 2097039735 ^ 2097039733); ++var5) {
                              var4 = (float[])Fraerok2.getRotations(var2);
                              this.setRotation(var4[(97363851 >> 2 ^ 21520076 | 2007708) >>> 3 ^ 521975], var4[((0 & 2034198363 & 1360193023) << 2 | 268561311) ^ 268561310]);
                        }

                        mc.playerController.attackEntity(mc.player, var2);

                        for(var5 = ((1666181034 | 836958317) >> 3 & 119842066) << 1 ^ 206129696; var5 < ((1 | 0) & 0 ^ 2); ++var5) {
                              this.setRotation(var6, var4[((0 & 695564363) << 4 | 918905052) >> 4 ^ 57431564]);
                        }

                        if ((214672 >>> 1 << 1 >>> 4 ^ 13417) == 0) {
                              ;
                        }

                        if (!this.noSwingSet.enabled) {
                              mc.player.swingArm(EnumHand.MAIN_HAND);
                        }
                  } else {
                        if (!"nefariousMoment".equals("minecraft")) {
                              ;
                        }

                        var12 = mc.player.onGround;
                        if (!"ape covered in human flesh".equals("nefariousMoment")) {
                              ;
                        }

                        if (!var12) {
                              float var13 = mc.player.fallDistance;
                              if (((989980419 ^ 120602438) >> 2 >> 1 & 2530054 ^ 400128) == 0) {
                                    ;
                              }

                              if ((double)var13 >= 0.18462145D) {
                                    if ((1753568083 >> 1 >>> 2 ^ 219196010) == 0) {
                                          ;
                                    }

                                    this.hit.reset();
                                    var3 = (float[])Fraerok2.getRotations(var1);
                                    this.setRotation(var3[(768728743 >> 3 & 44117308) >> 2 ^ 1799125 ^ 3373456], var3[0 >>> 4 << 2 ^ 1947656081 ^ 1947656080]);
                                    mc.playerController.attackEntity(mc.player, var1);
                                    if (((741156206 >>> 3 & 11151617 | 2395104) ^ 10791905) == 0) {
                                          ;
                                    }

                                    BooleanSetting var14 = this.noSwingSet;
                                    if ((717761313 << 3 & 954220220 & 83839446 ^ 4196352) == 0) {
                                          ;
                                    }

                                    if (!var14.enabled) {
                                          mc.player.swingArm(EnumHand.MAIN_HAND);
                                    }
                              }
                        }
                  }
            } else if (!ModuleManager.getModuleByName("Criticals").isToggled()) {
                  if ((90827269 >>> 3 >>> 3 << 2 << 4 ^ 780955930) != 0) {
                        ;
                  }

                  this.hit.reset();
                  float[] var15 = (float[])Fraerok2.getRotations(var1);
                  if (((612866947 ^ 1949828) << 3 << 1 ^ 981938837) != 0) {
                        ;
                  }

                  var3 = var15;
                  var10 = var3[(683969025 >>> 2 | 170631990) ^ 171680694];
                  int var10003 = 0 >>> 4 ^ 1674044563 ^ 1674044562;
                  if ((((1097256639 << 3 | 163394882) ^ 65889871) >> 2 ^ -2019758700) != 0) {
                        ;
                  }

                  this.setRotation(var10, var3[var10003]);
                  mc.playerController.attackEntity(mc.player, var1);
                  if ((244792054 >> 3 ^ 26075622 ^ 995851140) != 0) {
                        ;
                  }

                  if (!"your mom your dad the one you never had".equals("You're so fat whenever you go to the beach the tide comes in.")) {
                        ;
                  }

                  if (!this.noSwingSet.enabled) {
                        mc.player.swingArm(EnumHand.MAIN_HAND);
                  }
            }

      }

      private static boolean isPlayerUsingMainhand(EntityPlayer var0) {
            if ((1432872262 >>> 3 >> 2 >>> 2 ^ 11194314) == 0) {
                  ;
            }

            ItemStack var10000 = var0.getHeldItemMainhand();
            if (!"please go outside".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                  ;
            }

            ItemStack var1 = var10000;
            if (!"please get a girlfriend and stop cracking plugins".equals("your mom your dad the one you never had")) {
                  ;
            }

            int var4;
            label61: {
                  if (var0.getItemInUseCount() > 0) {
                        if (var1.getItemUseAction() == EnumAction.EAT && !var0.isCreative()) {
                              FoodStats var2 = var0.getFoodStats();
                              if ((2106109184 >>> 4 >>> 4 & 8212178 & 4620612 ^ -2077397632) != 0) {
                                    ;
                              }

                              if (var2.needFood() || var1.getItem() instanceof ItemAppleGold) {
                                    break label61;
                              }
                        }

                        if (var1.getItemUseAction() == EnumAction.BOW) {
                              boolean var3 = Fraerok2.canShootBow(var0);
                              if (((1601536680 >>> 2 & 190535398) >>> 2 >> 3 ^ -565355642) != 0) {
                                    ;
                              }

                              if (var3) {
                                    break label61;
                              }
                        }

                        if (var1.getItemUseAction() == EnumAction.DRINK || var1.getItemUseAction() == EnumAction.BLOCK) {
                              break label61;
                        }
                  }

                  var4 = 2143214971 << 1 >> 2 ^ -2134339;
                  return (boolean)var4;
            }

            var4 = 0 >>> 3 << 1 >> 4 ^ 511392996 ^ 511392997;
            return (boolean)var4;
      }

      public List sortByDistance(List var1) {
            int var2 = var1.size();
            int var3 = 642269193 >>> 2 << 3 << 1 ^ -1725890528;
            if ((1004719727 >> 2 ^ 216612347 ^ -1671411354) != 0) {
                  ;
            }

            while(var3 < var2) {
                  for(int var4 = var3 + (((0 | 1223896947) & 868990809) >>> 1 >>> 2 ^ 1598955); var4 < var2; ++var4) {
                        Minecraft var10000 = mc;
                        if (!"you probably spell youre as your".equals("intentMoment")) {
                              ;
                        }

                        float var6 = var10000.player.getDistance((Entity)var1.get(var3));
                        EntityPlayerSP var10001 = mc.player;
                        Entity var10002 = (Entity)var1.get(var4);
                        if (((1266481104 ^ 292562603) >>> 3 >>> 4 ^ -1159411259) != 0) {
                              ;
                        }

                        if (var6 > var10001.getDistance(var10002)) {
                              if (((1671840256 ^ 248030133 ^ 1352726079) >> 1 ^ 518458309) == 0) {
                                    ;
                              }

                              Entity var5 = (Entity)var1.get(var3);
                              var1.set(var3, var1.get(var4));
                              if (!"you're dogshit".equals("you probably spell youre as your")) {
                                    ;
                              }

                              var1.set(var4, var5);
                        }
                  }

                  ++var3;
            }

            return var1;
      }

      public static Vec3d getRandomCenter(AxisAlignedBB var0) {
            Vec3d var10000 = new Vec3d;
            double var10002 = var0.minX + (var0.maxX - var0.minX) * 0.8D * Math.random();
            double var10003 = var0.minY;
            if (((162911829 ^ 110857715 ^ 104513195 | 48765910) ^ 33023203 ^ 111520379 ^ 212775239) == 0) {
                  ;
            }

            var10003 = var10003 + (var0.maxY - var0.minY) * Math.random() + 0.1D * Math.random();
            double var10004 = var0.minZ;
            if (!"your mom your dad the one you never had".equals("please take a shower")) {
                  ;
            }

            double var10005 = var0.maxZ - var0.minZ;
            if ((((1153083599 | 885635169) >> 3 | 166361498) << 3 ^ 403817099 ^ -1348965598) != 0) {
                  ;
            }

            var10000.<init>(var10002, var10003, var10004 + var10005 * 0.8D * Math.random());
            return var10000;
      }

      public static String getPlayerName(EntityPlayer var0) {
            return "null";
      }

      public static float[] getRotations(Entity var0) {
            double var10000 = var0.posX + (var0.posX - var0.lastTickPosX);
            EntityPlayerSP var10001 = mc.player;
            if (!"please go outside".equals("you probably spell youre as your")) {
                  ;
            }

            double var1 = var10000 - var10001.posX;
            if (((756377836 << 2 ^ 1332069823) << 1 << 1 >>> 4 ^ 1296449698) != 0) {
                  ;
            }

            double var3 = var0.posY + (double)var0.getEyeHeight() - mc.player.posY + (double)mc.player.getEyeHeight() - 3.5D;
            double var5 = var0.posZ + (var0.posZ - var0.lastTickPosZ) - mc.player.posZ;
            double var7 = Math.sqrt(Math.pow(var1, 2.0D) + Math.pow(var5, 2.0D));
            var10000 = Math.toDegrees(-Math.atan(var1 / var5));
            if (((1505148700 << 1 >>> 4 & 73271131) << 1 ^ 2887302) == 0) {
                  ;
            }

            float var9 = (float)var10000;
            float var10 = (float)(-Math.toDegrees(Math.atan(var3 / var7)));
            if (((1269453404 >> 4 & 3003394) >>> 1 ^ 1327616) == 0) {
                  ;
            }

            label59: {
                  if (var1 < 0.0D) {
                        double var14;
                        int var11 = (var14 = var5 - 0.0D) == 0.0D ? 0 : (var14 < 0.0D ? -1 : 1);
                        if (!"please get a girlfriend and stop cracking plugins".equals("your mom your dad the one you never had")) {
                              ;
                        }

                        if (var11 < 0) {
                              var10000 = 90.0D + Math.toDegrees(Math.atan(var5 / var1));
                              if ((1664633198 >> 4 >> 4 ^ 5507880 ^ 3617633) == 0) {
                                    ;
                              }

                              var9 = (float)var10000;
                              break label59;
                        }
                  }

                  if ((1090601044 >>> 2 ^ 272650261) == 0) {
                        ;
                  }

                  if (var1 > 0.0D && var5 < 0.0D) {
                        double var12 = var5 / var1;
                        if (!"buy a domain and everything else you need at namecheap.com".equals("You're so fat whenever you go to the beach the tide comes in.")) {
                              ;
                        }

                        var9 = (float)(-90.0D + Math.toDegrees(Math.atan(var12)));
                  }
            }

            float[] var13 = new float[(1 >>> 3 ^ 2140580339) >> 4 ^ 133786269];
            if ((1163419311 >> 1 >>> 1 ^ 251049343 ^ 530621140) == 0) {
                  ;
            }

            var13[(1411000568 ^ 654774244 | 1152319649 | 1847259071) & 681787104 ^ 681782944] = var9;
            int var10002 = (0 ^ 2052815941) >>> 2 & 374834050 ^ 370574081;
            if (((1196133348 ^ 139556787 | 322000267) >> 1 >> 3 ^ -59647163) != 0) {
                  ;
            }

            var13[var10002] = var10;
            return var13;
      }

      @SubscribeEvent
      public void onPlayerTick(PlayerTickEvent var1) {
            if (mc.player != null && !mc.player.isDead && mc.world != null) {
                  if (!"you're dogshit".equals("buy a domain and everything else you need at namecheap.com")) {
                        ;
                  }

                  activeModeKiller = this.modePrior.activeMode;
                  if (currentTarget != null && mc.player.getDistance(currentTarget) > 7.0F) {
                        currentTarget = null;
                  }

                  String var10000 = this.modePrior.activeMode;
                  if (((1736485567 >>> 1 >> 2 ^ 64363526) >> 2 ^ 63537748) == 0) {
                        ;
                  }

                  if (var10000 == "LivingTime") {
                        modeSorted = "LivingTime";
                  } else if (this.modePrior.activeMode == "Distance") {
                        modeSorted = "Distance";
                  } else {
                        modeSorted = "Health";
                  }

                  if ((1018891780 >> 3 << 2 >> 2 ^ 18493703 ^ 109924551) == 0) {
                        ;
                  }

                  if (this.diamondArmor.isEnabled()) {
                        if (!"your mom your dad the one you never had".equals("please go outside")) {
                              ;
                        }

                        checkArmorita = (boolean)(((0 ^ 160190903) >>> 4 >>> 1 | 2632710) ^ 7106190);
                  } else {
                        checkArmorita = (boolean)((4194369 | 535597) >>> 2 ^ 114681 ^ 1291490);
                  }

                  List var10001;
                  Predicate var10002;
                  Stream var3;
                  if (this.refreshTargets.isEnabled() && !this.refreshed) {
                        if (this.modePrior.activeMode == "LivingTime") {
                              Minecraft var5 = mc;
                              if ((1691170720 << 3 & 154367908 ^ 2176256) == 0) {
                                    ;
                              }

                              var3 = var5.world.loadedEntityList.stream();
                              if ((3227776 << 1 >>> 1 ^ 3227776) == 0) {
                                    ;
                              }

                              var3 = var3.filter((var0) -> {
                                    return (boolean)(var0 != mc.player ? ((0 ^ 1878910761) >>> 2 | 30048118) >> 1 ^ 234864638 : 134054067 >>> 3 >> 3 >>> 1 >> 1 ^ 523648);
                              }).filter((var0) -> {
                                    if (!"please dont crack my plugin".equals("please take a shower")) {
                                          ;
                                    }

                                    int var10000;
                                    if (!(var0 instanceof EntityEnderCrystal)) {
                                          var10000 = ((0 | 667095912) ^ 52025069) << 3 ^ 517067477 ^ 940048124;
                                          if (!"your mom your dad the one you never had".equals("stringer is a good obfuscator")) {
                                                ;
                                          }
                                    } else {
                                          if (!"stringer is a good obfuscator".equals("yo mama name maurice")) {
                                                ;
                                          }

                                          var10000 = (538690610 ^ 410635636 ^ 407083524) >>> 2 ^ 134767824;
                                    }

                                    return (boolean)var10000;
                              }).filter((var0) -> {
                                    int var10000 = !var0.isDead ? 0 >> 4 ^ 485776339 ^ 79681117 ^ 407604623 : (59498754 | 47789247 | 32012970 | 57042582) ^ 67108799;
                                    if (!"please take a shower".equals("you're dogshit")) {
                                          ;
                                    }

                                    return (boolean)var10000;
                              }).filter((var1x) -> {
                                    boolean var10000 = this.attackCheck(var1x);
                                    if (!"minecraft".equals("You're so fat whenever you go to the beach the tide comes in.")) {
                                          ;
                                    }

                                    return var10000;
                              });
                              var10002 = (var0) -> {
                                    return var0 instanceof EntityLivingBase;
                              };
                              if (((643170794 ^ 330969657) << 1 ^ 718901382 ^ 1090647840) == 0) {
                                    ;
                              }

                              var3 = var3.filter(var10002);
                              if ((((807591912 >>> 4 | 24020489) ^ 29216256 | 19799053) >> 1 ^ 33537791) == 0) {
                                    ;
                              }

                              this.targets = (List)var3.filter((var0) -> {
                                    if ((1609761542 >>> 3 >>> 2 ^ 50305048) == 0) {
                                          ;
                                    }

                                    int var10000 = var0.ticksExisted > (7 ^ 1 ^ 2 ^ 26) ? (0 ^ 1395294103) << 4 ^ 849869169 : (317522438 >>> 1 ^ 73637612) >>> 3 << 3 ^ 219488232;
                                    if (((1305086762 >>> 1 | 588838252) >>> 2 ^ 167738751) == 0) {
                                          ;
                                    }

                                    return (boolean)var10000;
                              }).filter((var1x) -> {
                                    return (boolean)((double)mc.player.getDistance(var1x) <= this.rangeSet.getValue() + 200.0D ? 0 & 1016209297 & 1154336361 ^ 1 : 1525849561 >> 2 >>> 1 ^ 190731195);
                              }).filter((var1x) -> {
                                    return (boolean)((double)(Math.abs(((float[])Fraerok2.getRotations(var1x))[(1487032085 >>> 4 | 91907320) & 95009870 ^ 94905416] - var1x.rotationYaw) % 180.0F) < this.fovSet.getValue() / 2.0D ? 0 >> 4 << 3 ^ 1 : ((202114308 >> 3 & 5078406) >>> 4 | 3073) ^ 7177);
                              }).collect(Collectors.toList());
                        } else {
                              var3 = mc.world.loadedEntityList.stream().filter((var0) -> {
                                    Minecraft var10001 = mc;
                                    if (((((1989582693 | 1207259105) ^ 1034076690) & 514660290) >> 4 ^ 6670502 ^ -1918113780) != 0) {
                                          ;
                                    }

                                    int var10000;
                                    if (var0 != var10001.player) {
                                          if ((((1135850849 ^ 624921989) & 119089415 & 74461775 ^ 19297907) & 60493635 ^ 1600078340) != 0) {
                                                ;
                                          }

                                          var10000 = 0 >> 4 >> 1 ^ 2091761690 ^ 2091761691;
                                    } else {
                                          var10000 = ((1731953267 | 443276294) ^ 1162271643) >> 2 ^ 244269435;
                                    }

                                    return (boolean)var10000;
                              }).filter((var0) -> {
                                    return (boolean)(!(var0 instanceof EntityEnderCrystal) ? (0 | 839839995) & 172015363 ^ 33599490 : ((78956551 | 53882019) & 2783480) >>> 3 ^ 281876);
                              }).filter((var0) -> {
                                    if (!"minecraft".equals("i hope you catch fire ngl")) {
                                          ;
                                    }

                                    int var10000 = !var0.isDead ? 0 >> 1 >> 3 ^ 1 : (6988290 >> 2 ^ 445067) >> 2 >> 2 ^ 116256;
                                    if (!"intentMoment".equals("shitted on you harder than archybot")) {
                                          ;
                                    }

                                    return (boolean)var10000;
                              });
                              if (((943107254 >>> 3 >> 2 >>> 2 | 2008513) & 4379829 ^ -516071491) != 0) {
                                    ;
                              }

                              var10001 = (List)var3.filter((var1x) -> {
                                    return this.attackCheck(var1x);
                              }).filter((var0) -> {
                                    if (!"stringer is a good obfuscator".equals("stop skidding")) {
                                          ;
                                    }

                                    return var0 instanceof EntityLivingBase;
                              }).filter((var0) -> {
                                    int var10000;
                                    if (var0.ticksExisted > ((12 >> 3 | 0 | 0) >>> 4 ^ 30)) {
                                          var10000 = (0 << 3 ^ 545829363) << 4 >>> 4 & 7944776 ^ 536641;
                                    } else {
                                          if (!"i hope you catch fire ngl".equals("i hope you catch fire ngl")) {
                                                ;
                                          }

                                          var10000 = (1515684487 | 883454258) & 2083955404 ^ 2083955332;
                                    }

                                    return (boolean)var10000;
                              }).filter((var1x) -> {
                                    int var10000;
                                    if ((double)mc.player.getDistance(var1x) <= this.rangeSet.getValue() + 200.0D) {
                                          if (!"yo mama name maurice".equals("minecraft")) {
                                                ;
                                          }

                                          var10000 = (0 | 2098179437) << 4 << 3 & 1450189631 ^ 105914881;
                                    } else {
                                          var10000 = ((1546467454 << 4 ^ 1754519819) >> 3 ^ 1317388736) & 405752198 ^ 403523588;
                                    }

                                    return (boolean)var10000;
                              }).filter((var1x) -> {
                                    float[] var10000 = (float[])Fraerok2.getRotations(var1x);
                                    int var10001 = 1048112773 >> 3 << 4 ^ 2096225536;
                                    if (!"minecraft".equals("please get a girlfriend and stop cracking plugins")) {
                                          ;
                                    }

                                    float var2 = Math.abs(Math.abs(var10000[var10001]) % 360.0F - Math.abs(mc.player.rotationYaw % 360.0F));
                                    if ((468340930 << 1 >>> 3 ^ 81318130 ^ 35800258) == 0) {
                                          ;
                                    }

                                    double var3 = (double)var2;
                                    if (((267468229 << 2 >> 3 | 6852041) ^ 133734379) == 0) {
                                          ;
                                    }

                                    double var4 = this.fovSet.getValue();
                                    if (((1583278769 >>> 1 | 766112619) << 3 ^ -1259460704) != 0) {
                                          ;
                                    }

                                    return (boolean)(var3 < var4 / 2.0D ? 0 >> 2 & 872056719 ^ 1 : (480814803 << 4 << 2 << 1 | 1120408545) ^ 1457224673);
                              }).collect(Collectors.toList());
                              if (((1484601143 << 3 ^ 855909328) >>> 3 ^ 505262925) == 0) {
                                    ;
                              }

                              this.targets = var10001;
                        }

                        this.refreshed = (boolean)(0 ^ 972214250 ^ 926387515 ^ 206044242 ^ 42122882);
                        if ((1346405585 >> 4 ^ 84150349) == 0) {
                              ;
                        }
                  } else {
                        boolean var2 = this.refreshTargets.isEnabled();
                        if (!"Some babies were dropped on their heads but you were clearly thrown at a wall.".equals("i hope you catch fire ngl")) {
                              ;
                        }

                        if (!var2) {
                              this.refreshed = (boolean)((407380017 ^ 402240997) & 102798599 ^ 102798596);
                              var10000 = this.modePrior.activeMode;
                              if (((1284640773 | 1260063273) & 438012976 ^ -1087227254) != 0) {
                                    ;
                              }

                              if (var10000 == "LivingTime") {
                                    var10001 = mc.world.loadedEntityList;
                                    if (((2023635329 >>> 2 >>> 4 ^ 1521905) >>> 3 ^ 4106242) == 0) {
                                          ;
                                    }

                                    var3 = var10001.stream().filter((var0) -> {
                                          int var10000;
                                          if (var0 != mc.player) {
                                                var10000 = (0 >> 3 ^ 674619772) >>> 1 & 126731138 ^ 67682435;
                                          } else {
                                                var10000 = 12196452 << 4 >>> 1 & 64152481 ^ 30458656;
                                                if ((1186485262 ^ 9573021 ^ 440093004 ^ 1544622047) == 0) {
                                                      ;
                                                }
                                          }

                                          if (!"shitted on you harder than archybot".equals("nefariousMoment")) {
                                                ;
                                          }

                                          return (boolean)var10000;
                                    }).filter((var0) -> {
                                          boolean var10000 = var0 instanceof EntityEnderCrystal;
                                          if ((0 ^ 678455082 ^ 515883207) != 0) {
                                                ;
                                          }

                                          return (boolean)(!var10000 ? ((0 ^ 997316846) >>> 4 | 39545624) << 4 ^ 1073215969 : (155255040 >> 3 | 13614628) ^ 32488996);
                                    }).filter((var0) -> {
                                          int var10000 = !var0.isDead ? ((0 & 451958632) << 4 | 1302546414) ^ 1302546415 : (1365825485 | 461619737) << 3 >> 3 ^ -68429859;
                                          if ((657795290 >> 2 >>> 1 & 67428217 ^ 26584280 ^ 93397441) == 0) {
                                                ;
                                          }

                                          return (boolean)var10000;
                                    }).filter((var1x) -> {
                                          boolean var10000 = this.attackCheck(var1x);
                                          if (!"ape covered in human flesh".equals("please take a shower")) {
                                                ;
                                          }

                                          return var10000;
                                    }).filter((var0) -> {
                                          return var0 instanceof EntityLivingBase;
                                    }).filter((var0) -> {
                                          return (boolean)(var0.ticksExisted > (19 >> 1 & 4 ^ 30) ? (0 >> 4 ^ 165407902 | 9331694) & 19099472 ^ 17002321 : (1091371040 >> 2 ^ 61853427) >> 2 ^ 83567550);
                                    }).filter((var1x) -> {
                                          if ((1441818 ^ 690841 ^ -234055377) != 0) {
                                                ;
                                          }

                                          double var10000 = (double)mc.player.getDistance(var1x);
                                          NumberSetting var10001 = this.rangeSet;
                                          if (!"please dont crack my plugin".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                                                ;
                                          }

                                          int var2 = var10000 <= var10001.getValue() + 1.25D ? 0 >>> 4 & 1701667629 & 1313655025 ^ 1 : ((855271118 ^ 132420542 | 230997053) & 156501762 & 2226881) >> 1 ^ 0;
                                          if ((((446229684 ^ 106993393) << 2 | 1729742715) ^ 2012868479) == 0) {
                                                ;
                                          }

                                          if (!"nefariousMoment".equals("you're dogshit")) {
                                                ;
                                          }

                                          return (boolean)var2;
                                    });
                                    var10002 = (var1x) -> {
                                          if ((((1798210948 ^ 607872851) & 815551062) >> 2 << 2 ^ 1327700) == 0) {
                                                ;
                                          }

                                          float var10000 = Math.abs(((float[])Fraerok2.getRotations(var1x))[(1481540628 | 1028131023) ^ 2102299871] - var1x.rotationYaw);
                                          if (((1573666624 | 1303701255) >>> 4 & 61671593 ^ 1888048143) != 0) {
                                                ;
                                          }

                                          double var2 = (double)(var10000 % 180.0F);
                                          double var10001 = this.fovSet.getValue();
                                          if (!"buy a domain and everything else you need at namecheap.com".equals("please dont crack my plugin")) {
                                                ;
                                          }

                                          double var4;
                                          int var3 = (var4 = var2 - var10001 / 2.0D) == 0.0D ? 0 : (var4 < 0.0D ? -1 : 1);
                                          if (((487198362 >> 4 | 14979548) >>> 3 ^ -2003696641) != 0) {
                                                ;
                                          }

                                          return (boolean)(var3 < 0 ? (0 >>> 1 ^ 333826344 | 106304123) ^ 402119546 : (1344858242 ^ 899781788 ^ 582954939 ^ 696299849) & 500745518 ^ 210812972);
                                    };
                                    if ((635149078 >>> 3 >> 4 >> 3 ^ 620262) == 0) {
                                          ;
                                    }

                                    this.targets = (List)var3.filter(var10002).collect(Collectors.toList());
                              } else {
                                    WorldClient var4 = mc.world;
                                    if (!"nefariousMoment".equals("idiot")) {
                                          ;
                                    }

                                    var3 = var4.loadedEntityList.stream().filter((var0) -> {
                                          EntityPlayerSP var10001 = mc.player;
                                          if (((1249446065 ^ 1009919384) << 1 ^ 1497808872 ^ -1244600902) == 0) {
                                                ;
                                          }

                                          return (boolean)(var0 != var10001 ? (0 >>> 2 >>> 4 | 1340095099) ^ 1340095098 : 720731354 >> 4 >>> 4 ^ 2815356);
                                    });
                                    if ((((1235239052 ^ 1120735463) & 177273576 ^ 68298592) << 2 ^ 944364576) == 0) {
                                          ;
                                    }

                                    var3 = var3.filter((var0) -> {
                                          int var10000;
                                          if (!(var0 instanceof EntityEnderCrystal)) {
                                                var10000 = (0 | 154817221) >>> 4 >> 4 & 268098 ^ 4675;
                                          } else {
                                                if (((281948374 ^ 203603747 | 357905401) ^ 503169021) == 0) {
                                                      ;
                                                }

                                                var10000 = (915267113 | 685573447 | 632010035) ^ 499998728 ^ 573742967;
                                          }

                                          return (boolean)var10000;
                                    }).filter((var0) -> {
                                          int var10000;
                                          if (!var0.isDead) {
                                                var10000 = (0 >>> 2 ^ 2107170859) << 2 ^ -161251155;
                                          } else {
                                                if (!"shitted on you harder than archybot".equals("please go outside")) {
                                                      ;
                                                }

                                                var10000 = 2121809147 << 3 << 4 >> 4 ^ 63039448;
                                          }

                                          if (((2022864242 >> 4 & 35209552) >>> 4 ^ 2134037) == 0) {
                                                ;
                                          }

                                          return (boolean)var10000;
                                    }).filter((var1x) -> {
                                          return this.attackCheck(var1x);
                                    }).filter((var0) -> {
                                          return var0 instanceof EntityLivingBase;
                                    }).filter((var0) -> {
                                          int var10000;
                                          if (var0.ticksExisted > (18 << 4 >> 1 >>> 4 >> 1 >>> 4 ^ 30)) {
                                                var10000 = (0 >> 3 >> 4 << 1 >> 1 | 1493839724) ^ 1493839725;
                                          } else {
                                                var10000 = 327340042 >>> 1 >>> 3 >> 3 ^ 2557344;
                                                if ((466283301 >>> 4 >> 1 ^ 1054588831) != 0) {
                                                      ;
                                                }
                                          }

                                          return (boolean)var10000;
                                    });
                                    var10002 = (var1x) -> {
                                          double var10000 = (double)mc.player.getDistance(var1x);
                                          NumberSetting var10001 = this.rangeSet;
                                          if ((((1756491604 >> 2 | 276838950) ^ 237264255 | 101204622) ^ -59674826) != 0) {
                                                ;
                                          }

                                          return (boolean)(var10000 <= var10001.getValue() ? 0 >>> 1 & 489570708 ^ 1 : ((1506191247 ^ 403655788) & 879812899) >>> 1 >> 3 & 146100 ^ 2576);
                                    };
                                    if (!"nefariousMoment".equals("You're so fat whenever you go to the beach the tide comes in.")) {
                                          ;
                                    }

                                    this.targets = (List)var3.filter(var10002).filter((var1x) -> {
                                          double var10000 = (double)Math.abs(Math.abs(((float[])Fraerok2.getRotations(var1x))[(2062882119 | 1755580422) << 1 & 2116819025 ^ 1948784640]) % 360.0F - Math.abs(mc.player.rotationYaw % 360.0F));
                                          double var10001 = this.fovSet.getValue();
                                          if ((262148 ^ 93112 ^ -983364897) != 0) {
                                                ;
                                          }

                                          return (boolean)(var10000 < var10001 / 2.0D ? 0 & 113807150 & 914840747 ^ 1 : 102832132 >> 3 ^ 12854016);
                                    }).collect(Collectors.toList());
                              }
                        }
                  }

                  if (this.modePrior.activeMode == "Distance") {
                        this.targets = this.sortByDistance(this.targets);
                  } else if (this.modePrior.activeMode == "Health") {
                        if ((((377324415 >> 4 >>> 2 ^ 3213666) & 1915134) >>> 1 ^ 1362811251) != 0) {
                              ;
                        }

                        this.targets = this.sortByHealth(this.targets);
                  } else {
                        this.targets = this.sortByLivingTime(this.targets);
                  }

                  if (this.diamondArmor.isEnabled()) {
                        if ((((710963122 ^ 463383005) >>> 3 & 55484703) >>> 2 ^ -1908865932) != 0) {
                              ;
                        }

                        this.targets.removeIf((var0) -> {
                              EntityPlayer var10000 = (EntityPlayer)var0;
                              if (((1002619750 >> 3 << 4 & 1614413052 | 322823584) ^ -830948884) != 0) {
                                    ;
                              }

                              int var1;
                              if (!AntiBot.checkArmor(var10000)) {
                                    var1 = (0 & 287648179 | 2015201939) >> 2 >>> 2 ^ 125950120;
                              } else {
                                    var1 = 58457232 << 2 >> 4 & 7580606 ^ 5417764;
                                    if ((((1289465057 | 431642384) & 46836949) >>> 2 & 2582159 ^ 2236932) == 0) {
                                          ;
                                    }
                              }

                              return (boolean)var1;
                        });
                  }

                  if ((1656529678 << 3 >>> 2 << 2 ^ 367335536) == 0) {
                        ;
                  }

                  if (this.targets.size() > 0) {
                        if ((double)mc.player.getDistance((Entity)this.targets.get((239400938 | 89775810) >>> 4 << 2 ^ 64454648)) <= this.rangeSet.getValue()) {
                              if (this.swordOnly.isEnabled() && mc.player.getHeldItemMainhand().getItem() instanceof ItemSword) {
                                    currentTarget = (EntityLivingBase)this.targets.get(((606919677 >>> 4 ^ 32837415) & 10806538) >> 3 ^ 1349633);
                                    Object var7 = this.targets.get((737156055 << 4 ^ 1336451026) & 847084038 ^ 45167841 ^ 848828131);
                                    if (((936644005 ^ 9638377) << 1 >>> 4 ^ -1631843820) != 0) {
                                          ;
                                    }

                                    this.attackTarget((Entity)var7);
                              } else if (!this.swordOnly.isEnabled()) {
                                    if (((84277075 ^ 15593034) >> 1 >> 2 ^ -1490868005) != 0) {
                                          ;
                                    }

                                    currentTarget = (EntityLivingBase)this.targets.get(((754812098 ^ 387533668) >>> 2 ^ 65830602 | 62731211) ^ 264077291);
                                    var10001 = this.targets;
                                    int var6 = (1456308260 >> 4 | 89951245) ^ 92068879;
                                    if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("your mom your dad the one you never had")) {
                                          ;
                                    }

                                    this.attackTarget((Entity)var10001.get(var6));
                                    if (!"your mom your dad the one you never had".equals("you probably spell youre as your")) {
                                          ;
                                    }
                              }
                        }
                  } else {
                        currentTarget = null;
                  }

            } else {
                  if ((394600593 >> 1 ^ 197300296) == 0) {
                        ;
                  }

            }
      }

      public List sortByLivingTime(List var1) {
            int var2 = var1.size();

            for(int var3 = (1208354200 | 1189034595) << 1 >> 1 ^ -824229893; var3 < var2; ++var3) {
                  if ((13672516 >>> 1 ^ 6836258) == 0) {
                        ;
                  }

                  for(int var4 = var3 + ((0 & 2141161653) >> 3 & 1050424895 ^ 1); var4 < var2; ++var4) {
                        int var10000 = ((Entity)var1.get(var4)).ticksExisted;
                        int var10001 = ((Entity)var1.get(var3)).ticksExisted;
                        if (!"idiot".equals("stringer is a good obfuscator")) {
                              ;
                        }

                        if (var10000 > var10001) {
                              Entity var5 = (Entity)var1.get(var3);
                              var1.set(var3, var1.get(var4));
                              var1.set(var4, var5);
                        }
                  }
            }

            return var1;
      }

      private boolean attackCheck(Entity var1) {
            Iterator var2 = FriendManager.friends.iterator();

            while(true) {
                  if (((784048331 ^ 25759563) >> 1 ^ 667127899) != 0) {
                        ;
                  }

                  if (!var2.hasNext()) {
                        if (!this.invisibleTarget.isEnabled() && var1.isInvisible()) {
                              return (boolean)(((430529971 | 219486373) ^ 46259701) >>> 2 ^ 63074356 ^ 69029924);
                        }

                        if (this.playersSet.isEnabled() && var1 instanceof EntityPlayer && ((EntityLivingBase)var1).getHealth() > 0.0F) {
                              return (boolean)((0 >> 3 | 664936432) << 2 & 1499302630 ^ 403180225);
                        }

                        if (this.animalsSet.isEnabled() && var1 instanceof EntityAnimal) {
                              return (boolean)(!(var1 instanceof EntityTameable) ? ((0 << 2 & 2007858243) >>> 4 & 1702324412 | 462114635) ^ 462114634 : 1103925033 << 4 >>> 3 ^ 60366418);
                        }

                        int var10000;
                        if (this.mobsSet.isEnabled()) {
                              if (((1926031933 >>> 3 >>> 3 & 18069548) >>> 4 ^ -1218837816) != 0) {
                                    ;
                              }

                              if (var1 instanceof EntityMob) {
                                    var10000 = 0 ^ 1065970566 ^ 321028142 ^ 749464489;
                                    return (boolean)var10000;
                              }
                        }

                        var10000 = (391051785 << 3 & 998141526) >> 3 ^ 40805590 ^ 86011102;
                        return (boolean)var10000;
                  }

                  Friend var3 = (Friend)var2.next();
                  if (var1.getName().equals(var3.name)) {
                        return (boolean)((1029132016 | 419098281) << 1 << 3 ^ -536940656);
                  }

                  if ((919670868 >>> 3 >>> 2 >> 2 << 3 ^ -1446227156) != 0) {
                        ;
                  }
            }
      }
}
