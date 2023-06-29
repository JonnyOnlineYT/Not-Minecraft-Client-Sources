//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.combat;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
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
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAppleGold;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.network.play.client.CPacketPlayer.Rotation;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class TestAura extends Module {
      public ModeSetting modeSetting;
      public BooleanSetting playersSet;
      private double prevY;
      public BooleanSetting mobsSet;
      private TimerUtil shieldAttack;
      public NumberSetting rangeSet;
      TimerUtil hit;
      public BooleanSetting invisibleTarget;
      public BooleanSetting yawTarget;
      public BooleanSetting animalsSet;
      private double prevZ;
      public NumberSetting delaySet;
      public BooleanSetting pitchTarget;
      public ModeSetting modePrior;
      public BooleanSetting autoDelaySet;
      public NumberSetting fovSet;
      public BooleanSetting noSwingSet;
      private Random random;
      public boolean swichable;
      private double prevX;
      public BooleanSetting swordOnly;

      public static void setPressed(KeyBinding var0, boolean var1) {
            try {
                  Class var10000 = var0.getClass();
                  if (((1784065955 >>> 1 ^ 798912873 | 244336038 | 163563172) >> 2 ^ 133167599) == 0) {
                        ;
                  }

                  Field var2 = var10000.getDeclaredField("pressed");
                  var2.setAccessible((boolean)(((0 & 1347418005) >>> 3 | 1901668725) ^ 1901668724));
                  if (!"intentMoment".equals("buy a domain and everything else you need at namecheap.com")) {
                        ;
                  }

                  var2.setBoolean(var0, var1);
                  if ((56372727 >> 1 >> 4 >> 3 >> 4 ^ 13762) == 0) {
                        ;
                  }

            } catch (ReflectiveOperationException var3) {
                  if (((770120788 << 2 ^ 900626673) << 2 ^ 25299356 ^ -1466394532) == 0) {
                  }

                  throw new RuntimeException(var3);
            }
      }

      public boolean swichfucked() {
            int var10001;
            if (!this.swichable) {
                  var10001 = ((0 & 870934286) >>> 1 | 700910666 | 653642444) ^ 804769487;
            } else {
                  if ((((285018793 ^ 171026321) >>> 4 ^ 7816203) & 1451809 ^ -796664645) != 0) {
                        ;
                  }

                  if (!"stop skidding".equals("i hope you catch fire ngl")) {
                        ;
                  }

                  var10001 = 1161235876 << 1 << 1 ^ 349976208;
            }

            this.swichable = (boolean)var10001;
            if (((1261352749 ^ 493553957 | 789667032) >> 4 << 4 & 13147060 ^ 4233872) == 0) {
                  ;
            }

            return this.swichable;
      }

      public static String getPlayerName(EntityPlayer var0) {
            return "null";
      }

      private static boolean isPlayerShielded(EntityPlayer var0) {
            int var1;
            label30: {
                  if (var0.getItemInUseCount() > 0) {
                        Item var10000 = var0.getHeldItemMainhand().getItem();
                        if (!"nefariousMoment".equals("please get a girlfriend and stop cracking plugins")) {
                              ;
                        }

                        if (var10000 instanceof ItemShield || var0.getHeldItemOffhand().getItem() instanceof ItemShield && !TestAura.isPlayerUsingMainhand(var0)) {
                              if ((1725180501 >> 3 & 191095452 ^ 2044316495) != 0) {
                                    ;
                              }

                              var1 = (0 ^ 477079352) >>> 1 ^ 238539677;
                              break label30;
                        }
                  }

                  var1 = (728876940 | 176409928) << 3 >> 4 ^ 100263910;
            }

            if (((388909472 | 161864352) >>> 4 >> 3 ^ 1182355975) != 0) {
                  ;
            }

            return (boolean)var1;
      }

      public List sortByHealth(List var1) {
            int var2 = var1.size();

            for(int var3 = 693352858 >> 1 ^ 36522513 ^ 120143558 ^ 296593434; var3 < var2; ++var3) {
                  int var10001 = 0 << 4 << 1 ^ 754423436 ^ 754423437;
                  if (((1886984305 >>> 3 >>> 2 | 37829559) ^ -1993642509) != 0) {
                        ;
                  }

                  for(int var4 = var3 + var10001; var4 < var2; ++var4) {
                        float var10000 = ((EntityPlayer)var1.get(var3)).getHealth();
                        Object var7 = var1.get(var4);
                        if (!"shitted on you harder than archybot".equals("i hope you catch fire ngl")) {
                              ;
                        }

                        float var8;
                        int var6 = (var8 = var10000 - ((EntityPlayer)var7).getHealth()) == 0.0F ? 0 : (var8 < 0.0F ? -1 : 1);
                        if (!"Some babies were dropped on their heads but you were clearly thrown at a wall.".equals("nefariousMoment")) {
                              ;
                        }

                        if (var6 > 0) {
                              Entity var5 = (Entity)var1.get(var3);
                              var1.set(var3, var1.get(var4));
                              var1.set(var4, var5);
                        }
                  }
            }

            return var1;
      }

      private void setRotation(float var1, float var2) {
            Minecraft var10000 = mc;
            if (((56016884 ^ 24542103) >> 4 & 1984107 ^ 2100996425) != 0) {
                  ;
            }

            EntityPlayerSP var3 = var10000.player;
            if ((1179391829 << 3 >> 4 >> 1 >> 4 ^ 1650781) == 0) {
                  ;
            }

            var3.renderYawOffset = var1;
            mc.player.rotationYawHead = var1;
            double var4;
            double var5;
            if (this.random.nextBoolean()) {
                  var4 = (double)var1;
                  int var10001 = this.random.nextInt(((3 ^ 0) & 2 | 0) ^ 102);
                  if (((291619164 | 41245692) ^ 74497835 ^ 386229463) == 0) {
                        ;
                  }

                  var5 = (double)var10001 * 0.02D;
                  if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("shitted on you harder than archybot")) {
                        ;
                  }

                  var1 = (float)(var4 + var5);
            } else {
                  var1 = (float)((double)var1 - (double)this.random.nextInt(18 >>> 4 & 0 ^ 100) * 0.02D);
            }

            if (((9175427 | 8786948) >>> 3 ^ 1163952) == 0) {
                  ;
            }

            if (this.random.nextBoolean()) {
                  var4 = (double)var2;
                  if ((43819140 >>> 4 ^ 2738696) == 0) {
                        ;
                  }

                  var2 = (float)(var4 + (double)this.random.nextInt(15 >> 3 >>> 2 >> 1 ^ 100) * 0.04D);
            } else {
                  var2 = (float)((double)var2 - (double)this.random.nextInt((45 >> 1 ^ 15 | 10) ^ 0 ^ 127) * 0.04D);
                  if ((11485248 >>> 2 << 4 ^ 44563053 ^ 27951816) != 0) {
                        ;
                  }
            }

            float var6;
            int var8;
            if (this.yawTarget.isEnabled()) {
                  if (var1 >= 0.0F) {
                        if (mc.player.rotationYaw < var1) {
                              while(true) {
                                    var6 = mc.player.rotationYaw;
                                    if (((1626918908 | 1230708671) >> 4 ^ 111132159) == 0) {
                                          ;
                                    }

                                    if (var6 >= var1) {
                                          break;
                                    }

                                    var10000 = mc;
                                    if (!"please take a shower".equals("you're dogshit")) {
                                          ;
                                    }

                                    var3 = var10000.player;
                                    var5 = (double)var3.rotationYaw;
                                    int var10002 = this.random.nextInt(73 >>> 3 << 3 ^ 60 ^ 16);
                                    if (!"please get a girlfriend and stop cracking plugins".equals("please get a girlfriend and stop cracking plugins")) {
                                          ;
                                    }

                                    double var7 = (double)var10002 * 0.001D;
                                    if (((260771227 << 1 | 391402894) ^ 872045445) != 0) {
                                          ;
                                    }

                                    var3.rotationYaw = (float)(var5 + var7);
                              }
                        } else {
                              for(; mc.player.rotationYaw > var1; var3.rotationYaw = (float)(var5 - (double)this.random.nextInt(64 << 3 & 78 ^ 100) * 0.001D)) {
                                    var3 = mc.player;
                                    var5 = (double)var3.rotationYaw;
                                    if (((1076052738 | 247849415) ^ 1323819975) == 0) {
                                          ;
                                    }
                              }
                        }
                  } else if (mc.player.rotationYaw < var1) {
                        while(true) {
                              var6 = mc.player.rotationYaw;
                              if ((25512208 ^ 4235361 ^ 29747569) == 0) {
                                    ;
                              }

                              if (var6 >= var1) {
                                    break;
                              }

                              if (!"please get a girlfriend and stop cracking plugins".equals("minecraft")) {
                                    ;
                              }

                              var3 = mc.player;
                              var3.rotationYaw = (float)((double)var3.rotationYaw + (double)this.random.nextInt((54 << 4 & 258) >> 1 ^ 228) * 0.001D);
                        }
                  } else {
                        while(true) {
                              float var10;
                              var8 = (var10 = mc.player.rotationYaw - var1) == 0.0F ? 0 : (var10 < 0.0F ? -1 : 1);
                              if ((2097152 << 3 << 4 ^ 205824501 ^ -1582068098) != 0) {
                                    ;
                              }

                              if (var8 <= 0) {
                                    break;
                              }

                              if (!"minecraft".equals("stringer is a good obfuscator")) {
                                    ;
                              }

                              var3 = mc.player;
                              var3.rotationYaw = (float)((double)var3.rotationYaw - (double)this.random.nextInt(34 & 2 & 1 & 2009400748 ^ 100) * 0.001D);
                        }
                  }
            }

            if (this.pitchTarget.isEnabled()) {
                  float var11;
                  var8 = (var11 = var2 - 0.0F) == 0.0F ? 0 : (var11 < 0.0F ? -1 : 1);
                  if (!"stringer is a good obfuscator".equals("ape covered in human flesh")) {
                        ;
                  }

                  Random var9;
                  int var10003;
                  if (var8 >= 0) {
                        if (mc.player.rotationPitch < var2) {
                              if ((1610878720 << 3 >>> 4 ^ 132992) == 0) {
                                    ;
                              }

                              while(true) {
                                    if (!"idiot".equals("you're dogshit")) {
                                          ;
                                    }

                                    if (mc.player.rotationPitch >= var2) {
                                          break;
                                    }

                                    var3 = mc.player;
                                    var5 = (double)var3.rotationPitch;
                                    if (!"Some babies were dropped on their heads but you were clearly thrown at a wall.".equals("you probably spell youre as your")) {
                                          ;
                                    }

                                    var3.rotationPitch = (float)(var5 + (double)this.random.nextInt((44 ^ 38) << 1 ^ 112) * 0.001D);
                              }
                        } else {
                              for(; mc.player.rotationPitch > var2; var3.rotationPitch = (float)(var5 - (double)var9.nextInt(var10003) * 0.001D)) {
                                    var3 = mc.player;
                                    var5 = (double)var3.rotationPitch;
                                    var9 = this.random;
                                    var10003 = (13 >>> 1 | 1) ^ 99;
                                    if ((435235865 << 4 << 4 ^ -248768256) == 0) {
                                          ;
                                    }
                              }
                        }
                  } else if (mc.player.rotationPitch < var2) {
                        for(; mc.player.rotationPitch < var2; var3.rotationPitch = (float)(var5 + (double)var9.nextInt(var10003) * 0.001D)) {
                              var3 = mc.player;
                              if (((3088310 | 848789) & 2502539 ^ 2502531) == 0) {
                                    ;
                              }

                              var5 = (double)var3.rotationPitch;
                              var9 = this.random;
                              var10003 = (49 & 6) >>> 1 >> 3 << 3 ^ 100;
                              if (((773887529 >> 1 & 186474458) >> 3 ^ 1933084421) != 0) {
                                    ;
                              }
                        }
                  } else {
                        while(true) {
                              var6 = mc.player.rotationPitch;
                              if (!"please take a shower".equals("please go outside")) {
                                    ;
                              }

                              if (var6 <= var2) {
                                    break;
                              }

                              var3 = mc.player;
                              if (((1531328781 | 632480442) >>> 2 ^ 536723183) == 0) {
                                    ;
                              }

                              var3.rotationPitch = (float)((double)var3.rotationPitch - (double)this.random.nextInt(6 >>> 1 ^ 2 ^ 101) * 0.001D);
                        }
                  }
            }

      }

      @SubscribeEvent
      public void onPlayerTick(PlayerTickEvent var1) {
            if (mc.player != null && !mc.player.isDead) {
                  List var2 = (List)mc.world.loadedEntityList.stream().filter((var0) -> {
                        int var10000;
                        if (var0 != mc.player) {
                              var10000 = 0 >>> 2 << 4 << 2 << 2 ^ 1;
                        } else {
                              if (!"minecraft".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                                    ;
                              }

                              var10000 = (268697856 >>> 4 >> 1 & 1969818 | 6) ^ 14;
                        }

                        return (boolean)var10000;
                  }).filter((var1x) -> {
                        double var10000 = (double)mc.player.getDistance(var1x);
                        if (!"please take a shower".equals("buy a domain and everything else you need at namecheap.com")) {
                              ;
                        }

                        int var2;
                        if (var10000 <= this.rangeSet.getValue()) {
                              var2 = (0 >>> 1 & 651699114) >> 3 ^ 2011139787 ^ 2011139786;
                        } else {
                              if (!"your mom your dad the one you never had".equals("stop skidding")) {
                                    ;
                              }

                              var2 = (1909760577 >> 1 | 54490869) << 3 ^ -537149528;
                        }

                        return (boolean)var2;
                  }).filter((var0) -> {
                        if ((((381581218 ^ 134124435 | 258740790) >> 3 | 58660477) ^ 511663476) != 0) {
                              ;
                        }

                        return (boolean)(!var0.isDead ? (0 << 1 >> 2 & 89744933) >>> 2 ^ 1 : (1594999006 >>> 2 & 61635231) >> 3 << 3 ^ 59011088);
                  }).filter((var1x) -> {
                        if (!"nefariousMoment".equals("stringer is a good obfuscator")) {
                              ;
                        }

                        return this.attackCheck(var1x);
                  }).filter((var0) -> {
                        int var10000;
                        if (var0.ticksExisted > (((12 & 11) >>> 4 >> 4 | 813183665) ^ 813183663)) {
                              var10000 = 0 >>> 4 << 3 >>> 4 & 1010773000 ^ 1;
                        } else {
                              if (!"please get a girlfriend and stop cracking plugins".equals("ape covered in human flesh")) {
                                    ;
                              }

                              var10000 = 1823768578 >>> 4 >> 1 >>> 1 ^ 28496384;
                        }

                        if (!"you probably spell youre as your".equals("intentMoment")) {
                              ;
                        }

                        return (boolean)var10000;
                  }).filter((var1x) -> {
                        float[] var10000 = (float[])TestAura.getRotations(var1x);
                        int var10001 = (288262 << 4 ^ 6415) & 2265414 ^ 135494;
                        if (((438011357 >> 2 >> 3 & 991597 | 40) ^ -2018194626) != 0) {
                              ;
                        }

                        float var2 = var10000[var10001];
                        if (!"please take a shower".equals("please go outside")) {
                              ;
                        }

                        int var3;
                        if ((double)(Math.abs(var2 - var1x.rotationYaw) % 180.0F) < this.fovSet.getValue() / 2.0D) {
                              var3 = 0 >>> 3 & 309914341 ^ 1;
                        } else {
                              if ((1899012362 ^ 687614383 ^ 1181157172 ^ -608427255) != 0) {
                                    ;
                              }

                              var3 = 668016 >> 3 >> 4 ^ 2662 ^ 7684;
                        }

                        return (boolean)var3;
                  }).collect(Collectors.toList());
                  if (this.modePrior.activeMode == "Distance") {
                        var2 = this.sortByDistance(var2);
                  } else if (this.modePrior.activeMode == "Health") {
                        if (!"please dont crack my plugin".equals("stop skidding")) {
                              ;
                        }

                        var2 = this.sortByHealth(var2);
                  } else {
                        if (!"buy a domain and everything else you need at namecheap.com".equals("idiot")) {
                              ;
                        }

                        var2 = this.sortByLivingTime(var2);
                  }

                  if ((((473672236 >>> 2 | 20999537) ^ 56527859 | 35898494 | 36711618) ^ 104066302) == 0) {
                        ;
                  }

                  if (var2.size() > 0) {
                        if (this.swordOnly.isEnabled()) {
                              Item var10000 = mc.player.getHeldItemMainhand().getItem();
                              if ((162835247 >> 3 >>> 3 >> 4 >>> 1 ^ 586644093) != 0) {
                                    ;
                              }

                              if (var10000 instanceof ItemSword) {
                                    this.attackTarget((Entity)var2.get(1620119626 ^ 73610418 ^ 1693590776));
                                    return;
                              }
                        }

                        if (!"ape covered in human flesh".equals("you probably spell youre as your")) {
                              ;
                        }

                        if (!this.swordOnly.isEnabled()) {
                              if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("intentMoment")) {
                                    ;
                              }

                              this.attackTarget((Entity)var2.get((346063495 ^ 55275965) >>> 4 ^ 25083699));
                        }
                  }

            }
      }

      private static boolean canShootBow(EntityPlayer var0) {
            if (var0.isCreative()) {
                  return (boolean)((0 >> 3 >>> 2 & 340368146) << 1 ^ 1);
            } else {
                  if (((305215895 << 4 ^ 383555966) << 3 & 1495798836 ^ 136839216) == 0) {
                        ;
                  }

                  Iterator var1 = var0.inventory.mainInventory.iterator();

                  ItemStack var2;
                  do {
                        if (!var1.hasNext()) {
                              return (boolean)(388050614 >>> 1 >> 3 << 3 ^ 194025304);
                        }

                        var2 = (ItemStack)var1.next();
                  } while(var2.getItem() != Items.ARROW);

                  return (boolean)((0 >>> 1 | 397232144) & 205667416 ^ 67110929);
            }
      }

      private static boolean isPlayerUsingMainhand(EntityPlayer var0) {
            if ((158229854 >>> 2 >> 3 ^ -106136108) != 0) {
                  ;
            }

            int var2;
            label70: {
                  ItemStack var1 = var0.getHeldItemMainhand();
                  if (var0.getItemInUseCount() > 0) {
                        if (!"ape covered in human flesh".equals("buy a domain and everything else you need at namecheap.com")) {
                              ;
                        }

                        if (var1.getItemUseAction() == EnumAction.EAT && !var0.isCreative()) {
                              if (var0.getFoodStats().needFood()) {
                                    break label70;
                              }

                              boolean var10000 = var1.getItem() instanceof ItemAppleGold;
                              if ((367574524 << 1 >> 2 ^ 183787262) == 0) {
                                    ;
                              }

                              if (var10000) {
                                    break label70;
                              }

                              if (((1918660314 >> 3 | 149313347) << 2 ^ -699377130) != 0) {
                                    ;
                              }
                        }

                        if (!"stringer is a good obfuscator".equals("you probably spell youre as your")) {
                              ;
                        }

                        if ((596357624 << 1 << 3 & 391461315 ^ 140877163 ^ 410289387) == 0) {
                              ;
                        }

                        if (var1.getItemUseAction() == EnumAction.BOW && TestAura.canShootBow(var0) || var1.getItemUseAction() == EnumAction.DRINK || var1.getItemUseAction() == EnumAction.BLOCK) {
                              break label70;
                        }
                  }

                  if (!"please get a girlfriend and stop cracking plugins".equals("stop skidding")) {
                        ;
                  }

                  var2 = (955452532 | 379809642) << 2 & 1421462243 ^ 1351190752;
                  return (boolean)var2;
            }

            var2 = (0 & 1807613123 ^ 1394125139) >>> 2 ^ 348531285;
            return (boolean)var2;
      }

      public static Vec3d getRandomCenter(AxisAlignedBB var0) {
            Vec3d var10000 = new Vec3d;
            double var10002 = var0.minX + (var0.maxX - var0.minX) * 0.8D * Math.random();
            double var10003 = var0.minY;
            double var10004 = var0.maxY;
            if (!"please go outside".equals("yo mama name maurice")) {
                  ;
            }

            var10003 += (var10004 - var0.minY) * Math.random();
            var10004 = 0.1D * Math.random();
            if ((33619969 << 3 ^ 101119794 ^ 370079546) == 0) {
                  ;
            }

            var10003 += var10004;
            var10004 = var0.minZ;
            double var10005 = (var0.maxZ - var0.minZ) * 0.8D;
            if (((8978768 ^ 8189557) >>> 2 >> 4 << 4 ^ 288101229) != 0) {
                  ;
            }

            var10000.<init>(var10002, var10003, var10004 + var10005 * Math.random());
            return var10000;
      }

      public static float[] getRotations(Entity var0) {
            double var10000 = var0.posX;
            double var10001 = var0.posX - var0.lastTickPosX;
            if (((2059388217 >> 1 ^ 1011251088) << 3 ^ 147630176) == 0) {
                  ;
            }

            double var1 = var10000 + var10001 - mc.player.posX;
            if (((1894954707 >> 3 ^ 208513250 | 8949880) >>> 3 ^ 6258639) == 0) {
                  ;
            }

            var10000 = var0.posY;
            if (!"stringer is a good obfuscator".equals("your mom your dad the one you never had")) {
                  ;
            }

            var10000 = var10000 + (double)var0.getEyeHeight() - mc.player.posY + (double)mc.player.getEyeHeight();
            if (((1112070211 | 509619477 | 1033317607) ^ 2147483127) == 0) {
                  ;
            }

            double var3 = var10000 - 3.5D;
            double var5 = var0.posZ + (var0.posZ - var0.lastTickPosZ) - mc.player.posZ;
            double var7 = Math.sqrt(Math.pow(var1, 2.0D) + Math.pow(var5, 2.0D));
            float var9 = (float)Math.toDegrees(-Math.atan(var1 / var5));
            if (((1222168227 << 2 & 422247903) >> 3 << 4 ^ -249065708) != 0) {
                  ;
            }

            float var11 = (float)(-Math.toDegrees(Math.atan(var3 / var7)));
            if (!"ape covered in human flesh".equals("nefariousMoment")) {
                  ;
            }

            float var10 = var11;
            if (var1 < 0.0D && var5 < 0.0D) {
                  var9 = (float)(90.0D + Math.toDegrees(Math.atan(var5 / var1)));
            } else if (var1 > 0.0D && var5 < 0.0D) {
                  var9 = (float)(-90.0D + Math.toDegrees(Math.atan(var5 / var1)));
            }

            if ((641494425 >> 2 >> 4 >> 4 & 619851 ^ -517945867) != 0) {
                  ;
            }

            float[] var12 = new float[(1 ^ 0) >>> 1 & 1576718484 ^ 2];
            int var10002 = (384 >> 4 | 21) ^ 29;
            if ((33562752 ^ 1847013 ^ 1035426 ^ 34849479) == 0) {
                  ;
            }

            var12[var10002] = var9;
            var12[0 << 4 << 4 >> 1 << 3 ^ 1] = var10;
            return var12;
      }

      public void attackTarget(Entity var1) {
            if ((1628543419 >>> 3 >> 4 ^ 12722995) == 0) {
                  ;
            }

            if (var1 instanceof EntityPlayer && TestAura.isPlayerShielded((EntityPlayer)var1) && mc.player.getHeldItemMainhand().getItem() instanceof ItemAxe && this.shieldAttack.hasReached(60.0D)) {
                  mc.playerController.attackEntity(mc.player, var1);
                  this.shieldAttack.reset();
            }

            if (((1411195643 | 107684611 | 933174829) ^ -575920736) != 0) {
                  ;
            }

            label301: {
                  if (this.autoDelaySet.isEnabled()) {
                        if ((((1733097523 ^ 402092722) & 528914371 | 172278296) << 3 ^ 304134645 ^ -1360410793) != 0) {
                              ;
                        }

                        double var10000 = (double)mc.player.getCooledAttackStrength(1.0F);
                        if (((1296621097 >> 1 ^ 333181284) >> 2 ^ -577490869) != 0) {
                              ;
                        }

                        if (var10000 < 1.0D) {
                              break label301;
                        }
                  } else {
                        if (!this.hit.hasReached(this.delaySet.getValue())) {
                              break label301;
                        }

                        if (!"yo mama name maurice".equals("You're so fat whenever you go to the beach the tide comes in.")) {
                              ;
                        }
                  }

                  float[] var3;
                  EntityPlayerSP var5;
                  PlayerControllerMP var6;
                  Minecraft var7;
                  float var10;
                  EntityPlayerSP var10001;
                  if (this.modeSetting.activeMode == "Bypass") {
                        if (ModuleManager.getModuleByName("Criticals").isToggled()) {
                              if (!Criticals.isRage) {
                                    if ((2228487 ^ 2228487) == 0) {
                                          ;
                                    }

                                    this.hit.reset();
                                    var3 = (float[])TestAura.getRotations(var1);
                                    if ((764423292 >> 4 >>> 2 >>> 1 ^ -612502378) != 0) {
                                          ;
                                    }

                                    this.setRotation(var3[1871095194 >> 1 >> 3 >> 1 ^ 58471724], var3[(0 >> 3 << 2 | 52885423) ^ 52885422]);
                                    var10001 = mc.player;
                                    if (((96432249 >>> 3 << 1 | 4995126) ^ 24116286) == 0) {
                                          ;
                                    }

                                    this.prevX = var10001.posX;
                                    this.prevY = mc.player.posY;
                                    this.prevZ = mc.player.posZ;
                                    if (!"yo mama name maurice".equals("please dont crack my plugin")) {
                                          ;
                                    }

                                    if (((271131571 ^ 227522522) >> 2 >>> 4 >>> 1 ^ 467388 ^ 318330198) != 0) {
                                          ;
                                    }

                                    var5 = mc.player;
                                    double var9 = var1.posX;
                                    double var10002 = var1.posY;
                                    double var10003 = var1.posZ;
                                    float var10004 = mc.player.rotationYaw;
                                    float var10005 = mc.player.rotationPitch;
                                    int var10006 = ((2 ^ 0) & 0) >>> 1 >> 2 ^ 24;
                                    int var10007 = (0 ^ 143719470 ^ 51843742) << 1 ^ 386915681;
                                    if (((547394746 | 360256005 | 535998456) >> 4 ^ 67091455) == 0) {
                                          ;
                                    }

                                    var5.setPositionAndRotationDirect(var9, var10002, var10003, var10004, var10005, var10006, (boolean)var10007);
                                    if (!"your mom your dad the one you never had".equals("stringer is a good obfuscator")) {
                                          ;
                                    }

                                    mc.player.setSprinting((boolean)(((1939988292 | 1470306380) >> 2 >> 2 | 35010910) ^ 125713918));
                                    if ((20972064 << 1 << 2 << 1 ^ -859305603) != 0) {
                                          ;
                                    }

                                    mc.playerController.attackEntity(mc.player, var1);
                                    if ((526094670 << 2 >> 3 >> 4 ^ 888456 ^ 16208386) == 0) {
                                          ;
                                    }

                                    if (!"please get a girlfriend and stop cracking plugins".equals("please take a shower")) {
                                          ;
                                    }

                                    if (!this.noSwingSet.enabled) {
                                          mc.player.swingArm(EnumHand.MAIN_HAND);
                                    }
                              } else if (!mc.player.onGround && (double)mc.player.fallDistance >= 0.15D) {
                                    if (!"idiot".equals("you probably spell youre as your")) {
                                          ;
                                    }

                                    this.hit.reset();
                                    var3 = (float[])TestAura.getRotations(var1);
                                    var10 = var3[(604071908 | 225295640) >> 3 ^ 95272959];
                                    int var14 = ((0 & 1490047210 | 389894277) >>> 4 ^ 15990965) << 4 ^ 410868689;
                                    if ((1735891844 >> 2 & 247646053 ^ 146851425) == 0) {
                                          ;
                                    }

                                    this.setRotation(var10, var3[var14]);
                                    if ((4195458 ^ 73470036) != 0) {
                                          ;
                                    }

                                    mc.player.setSprinting((boolean)(((369150272 | 172209011) ^ 209541590) >>> 4 ^ 19130922));
                                    mc.playerController.attackEntity(mc.player, var1);
                                    if (!this.noSwingSet.enabled) {
                                          mc.player.swingArm(EnumHand.MAIN_HAND);
                                    }
                              }
                        } else {
                              if (((369807888 >> 1 ^ 132328053) >>> 1 ^ -1473297367) != 0) {
                                    ;
                              }

                              if (!ModuleManager.getModuleByName("Criticals").isToggled()) {
                                    this.hit.reset();
                                    var3 = (float[])TestAura.getRotations(var1);
                                    this.setRotation(var3[(1537085423 ^ 36263694) >>> 4 & 713502 ^ 680206], var3[0 >> 4 << 3 >>> 1 & 579785836 ^ 1]);
                                    if ((546932878 ^ 221003394 ^ -916997281) != 0) {
                                          ;
                                    }

                                    var6 = mc.playerController;
                                    var10001 = mc.player;
                                    if (((783693499 << 4 ^ 1904707512) >>> 2 >>> 4 ^ 40604592) == 0) {
                                          ;
                                    }

                                    var6.attackEntity(var10001, var1);
                                    if (!this.noSwingSet.enabled) {
                                          var7 = mc;
                                          if ((16842752 >>> 4 ^ -1352865186) != 0) {
                                                ;
                                          }

                                          var7.player.swingArm(EnumHand.MAIN_HAND);
                                    }
                              }
                        }
                  } else {
                        float[] var4;
                        float[] var8;
                        int var13;
                        if (ModuleManager.getModuleByName("Criticals").isToggled() && !mc.player.onGround) {
                              if (Criticals.isRage) {
                                    this.hit.reset();
                                    var8 = new float[(1 & 0) >> 1 >>> 4 ^ 2];
                                    var8[(337678872 | 323674355 | 22634075) ^ 394002171] = mc.player.rotationYaw;
                                    var8[(0 & 1182556277) << 2 ^ 1] = mc.player.rotationPitch;
                                    var3 = var8;
                                    if (!"intentMoment".equals("please go outside")) {
                                          ;
                                    }

                                    var4 = (float[])TestAura.getRotations(var1);
                                    this.setRotation(var4[(461486412 ^ 338284287 ^ 110508169) >>> 4 ^ 9692403], var4[0 >>> 3 >>> 1 << 4 ^ 1]);
                                    if ((((1077367879 ^ 629714542) & 1389519257) >>> 1 >> 3 ^ 67708416) == 0) {
                                          ;
                                    }

                                    mc.player.setSprinting((boolean)(14373650 << 4 >>> 2 ^ 57494600));
                                    if (var4[1108394056 >>> 4 ^ 69274628] < 90.0F) {
                                          var13 = 1572048695 >> 3 >>> 3 >> 3 & 2450949 ^ 2375685;
                                          int var18 = ((1337880598 >>> 2 >>> 3 | 20331541) & 58021995) >> 1 ^ 29010944;
                                          if (((134299696 << 2 & 510887581) >>> 2 ^ 16416) == 0) {
                                                ;
                                          }

                                          float var17 = 90.0F - var4[var18];
                                          if (!"please dont crack my plugin".equals("you probably spell youre as your")) {
                                                ;
                                          }

                                          var4[var13] = 360.0F - var17;
                                    } else {
                                          var4[(1556211927 ^ 1328697366) >>> 1 ^ 51251051 ^ 184015627] -= 90.0F;
                                    }

                                    if (!"you probably spell youre as your".equals("stringer is a good obfuscator")) {
                                          ;
                                    }

                                    mc.player.connection.sendPacket(new Rotation(var4[(86070528 | 56855964) ^ 111131532 ^ 33318416], mc.player.rotationPitch, (boolean)(37492747 >> 3 ^ 4686593)));
                                    if (!"stringer is a good obfuscator".equals("intentMoment")) {
                                          ;
                                    }

                                    mc.playerController.attackEntity(mc.player, var1);
                                    if (!"Some babies were dropped on their heads but you were clearly thrown at a wall.".equals("please go outside")) {
                                          ;
                                    }

                                    if (!this.noSwingSet.enabled) {
                                          var5 = mc.player;
                                          if (!"minecraft".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                                                ;
                                          }

                                          var5.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
                                          mc.player.swingArm(EnumHand.MAIN_HAND);
                                    }

                                    mc.player.setSprinting((boolean)(142772965 >>> 2 ^ 35693241));
                                    if (((1667511485 ^ 1666155602) >>> 4 >>> 4 ^ 11184) == 0) {
                                          ;
                                    }

                                    this.setRotation(var3[1006341075 >>> 1 >>> 2 << 3 ^ 1006341072], var3[0 & 921751855 & 303717423 ^ 1]);
                                    if (((1644713876 ^ 578427380) >>> 2 >> 1 ^ -1604080686) != 0) {
                                          ;
                                    }
                              } else {
                                    var5 = mc.player;
                                    if ((((325513268 >> 1 | 72071715) << 4 | 1798660562) & 101547395 ^ 509047263) != 0) {
                                          ;
                                    }

                                    if (!var5.onGround && (double)mc.player.fallDistance >= 0.15D) {
                                          this.hit.reset();
                                          var3 = (float[])TestAura.getRotations(var1);
                                          if ((1257638320 >>> 2 >>> 2 >>> 4 ^ -2147426928) != 0) {
                                                ;
                                          }

                                          this.setRotation(var3[(454555931 | 392087121) & 184408079 ^ 162121136 ^ 66330043], var3[0 << 3 >>> 2 ^ 1]);
                                          mc.player.setSprinting((boolean)(91755021 >>> 3 << 3 >> 3 ^ 11469377));
                                          if (((2004302651 | 1931002992) >>> 2 >>> 1 >>> 1 >>> 4 ^ -537645137) != 0) {
                                                ;
                                          }

                                          mc.playerController.attackEntity(mc.player, var1);
                                          if (!this.noSwingSet.enabled) {
                                                mc.player.swingArm(EnumHand.MAIN_HAND);
                                          }
                                    } else {
                                          Module var16 = ModuleManager.getModuleByName("Criticals");
                                          if (!"i hope you catch fire ngl".equals("shitted on you harder than archybot")) {
                                                ;
                                          }

                                          if (!var16.isToggled()) {
                                                this.hit.reset();
                                                if (((798798847 >>> 3 << 2 | 158356746) ^ -2105302732) != 0) {
                                                      ;
                                                }

                                                var8 = (float[])TestAura.getRotations(var1);
                                                if (((512028102 ^ 294060624) >> 1 >> 1 ^ 62979045) == 0) {
                                                      ;
                                                }

                                                var3 = var8;
                                                if ((((52925961 << 4 | 85777555) >> 4 | 38084821) ^ 412859609) != 0) {
                                                      ;
                                                }

                                                this.setRotation(var3[1283305774 >> 4 << 3 ^ 641652880], var3[(0 >>> 4 >>> 2 << 3 ^ 1561339874) >> 4 ^ 97583743]);
                                                var6 = mc.playerController;
                                                Minecraft var15 = mc;
                                                if ((1712224388 >>> 3 >> 2 ^ 53507012) == 0) {
                                                      ;
                                                }

                                                var10001 = var15.player;
                                                if (!"stringer is a good obfuscator".equals("nefariousMoment")) {
                                                      ;
                                                }

                                                var6.attackEntity(var10001, var1);
                                                if (!this.noSwingSet.enabled) {
                                                      mc.player.swingArm(EnumHand.MAIN_HAND);
                                                }
                                          }
                                    }
                              }
                        } else if (!ModuleManager.getModuleByName("Criticals").isToggled()) {
                              this.hit.reset();
                              var8 = new float[(1 & 0) >> 4 ^ 290583684 ^ 290583686];
                              if (!"your mom your dad the one you never had".equals("buy a domain and everything else you need at namecheap.com")) {
                                    ;
                              }

                              var8[(1137969955 << 3 ^ 63449957) & 254335049 ^ 220745801] = mc.player.rotationYaw;
                              var8[(0 >> 4 >> 1 ^ 185913348) >>> 1 ^ 92956675] = mc.player.rotationPitch;
                              var3 = var8;
                              if (!"buy a domain and everything else you need at namecheap.com".equals("stringer is a good obfuscator")) {
                                    ;
                              }

                              var4 = (float[])TestAura.getRotations(var1);
                              var10 = var4[(53885789 >> 3 | 6361409) & 6624769 ^ 2641236 ^ 5069653];
                              if ((172746403 >> 4 << 2 & 16384049 ^ 9568288) == 0) {
                                    ;
                              }

                              this.setRotation(var10, var4[(0 | 2109642941) >> 2 ^ 527410734]);
                              mc.player.setSprinting((boolean)((1720988649 ^ 525336425) << 3 << 4 ^ -303677440));
                              if (var4[(1725291410 >> 4 | 60373647) >> 2 >> 4 ^ 2094590] < 90.0F) {
                                    var4[1258854620 << 3 ^ 1480902368] = 360.0F - (90.0F - var4[(1746658288 ^ 614353069) >>> 2 >> 2 ^ 80239285]);
                              } else {
                                    if ((((1737529023 | 1565443589) & 82884114 | 26280981) << 2 ^ -312185548) != 0) {
                                          ;
                                    }

                                    var13 = (909544511 | 513776951) & 620705865 ^ 616503305;
                                    float var12 = var4[(909544511 | 513776951) & 620705865 ^ 616503305] - 90.0F;
                                    if (((673290302 >> 1 | 335209667) ^ 1534797784) != 0) {
                                          ;
                                    }

                                    var4[var13] = var12;
                              }

                              var7 = mc;
                              if (!"Some babies were dropped on their heads but you were clearly thrown at a wall.".equals("buy a domain and everything else you need at namecheap.com")) {
                                    ;
                              }

                              var7.player.connection.sendPacket(new Rotation(var4[((1899154457 ^ 249352104) << 3 & 245917541) >>> 3 ^ 29622432], mc.player.rotationPitch, (boolean)((1437091077 | 594549169 | 107050010) ^ 2012839359)));
                              mc.playerController.attackEntity(mc.player, var1);
                              if (!"ape covered in human flesh".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                                    ;
                              }

                              boolean var11 = this.noSwingSet.enabled;
                              if ((1591916284 >> 3 >>> 1 >>> 2 ^ 24873691) == 0) {
                                    ;
                              }

                              if (!var11) {
                                    var5 = mc.player;
                                    if ((515005580 << 3 ^ 1141686365 ^ -1726735595) != 0) {
                                          ;
                                    }

                                    var5.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
                                    if ((558609665 << 4 << 4 ^ 1270153472) == 0) {
                                          ;
                                    }

                                    mc.player.swingArm(EnumHand.MAIN_HAND);
                              }

                              mc.player.setSprinting((boolean)((1067434203 << 4 | 2044417426) >>> 4 ^ 262141403));
                              if (((1124293806 >> 3 & 19806779) >> 4 ^ 131745) == 0) {
                                    ;
                              }

                              this.setRotation(var3[(2109292793 << 1 | 635297565) ^ 822434717 ^ -822442910], var3[(0 ^ 1352515908 | 41325004) << 3 ^ -1744925087]);
                        }
                  }
            }

            if (((1064806018 ^ 762473846 | 264779303) ^ -769593909) != 0) {
                  ;
            }

      }

      private boolean attackCheck(Entity var1) {
            Iterator var2 = FriendManager.friends.iterator();
            if ((((667307308 | 139810588 | 789343505) ^ 479650846) >>> 3 ^ 1360826214) != 0) {
                  ;
            }

            while(var2.hasNext()) {
                  Friend var3 = (Friend)var2.next();
                  if (var1.getName() == var3.name) {
                        return (boolean)((1334130773 >> 4 | 41331248) ^ 117373877);
                  }
            }

            if (!"You're so fat whenever you go to the beach the tide comes in.".equals("idiot")) {
                  ;
            }

            if (!this.invisibleTarget.isEnabled() && var1.isInvisible()) {
                  return (boolean)((1836619120 | 343861186) & 930129568 ^ 250481619 ^ 1000254835);
            } else {
                  if ((374386653 >> 1 << 1 << 4 >> 2 ^ 1300183020) != 0) {
                        ;
                  }

                  if (this.playersSet.isEnabled() && var1 instanceof EntityPlayer && ((EntityPlayer)var1).getHealth() > 0.0F) {
                        return (boolean)(0 >>> 1 >>> 2 << 3 ^ 1);
                  } else {
                        if (!"please take a shower".equals("idiot")) {
                              ;
                        }

                        int var4;
                        if (this.animalsSet.isEnabled() && var1 instanceof EntityAnimal) {
                              if (!(var1 instanceof EntityTameable)) {
                                    var4 = (0 >>> 2 | 752986965) & 67948599 ^ 67141652;
                                    if ((((1977897407 ^ 970148783) >> 3 | 46689685) ^ 1894265226) != 0) {
                                          ;
                                    }
                              } else {
                                    var4 = (855461364 >> 1 & 413754565 ^ 352655951) << 3 ^ 1768551544;
                              }

                              if (((51968113 >>> 1 ^ 20366626) >>> 1 >>> 1 ^ 328852970) != 0) {
                                    ;
                              }

                              return (boolean)var4;
                        } else {
                              boolean var10000 = this.mobsSet.isEnabled();
                              if (!"please get a girlfriend and stop cracking plugins".equals("You're so fat whenever you go to the beach the tide comes in.")) {
                                    ;
                              }

                              var4 = var10000 && var1 instanceof EntityMob ? (0 | 1560408667) >> 4 ^ 97525540 : ((1745163117 | 1371824825) >> 4 | 30085878) ^ 132077567;
                              if (((168050721 << 1 | 91744375 | 159716633) ^ 200738094) != 0) {
                                    ;
                              }

                              return (boolean)var4;
                        }
                  }
            }
      }

      public static void resetPressed(KeyBinding var0) {
            TestAura.setPressed(var0, GameSettings.isKeyDown(var0));
      }

      public TestAura() {
            if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("minecraft")) {
                  ;
            }

            super("TestAura", "automatically hits enemies", (774356848 ^ 267850689 | 476508377 | 453922679) >>> 3 >>> 4 ^ 8388095, Module.Category.COMBAT);
            Random var10001 = new Random;
            if ((((1226879908 ^ 8186480 | 513665701) & 235568843) << 2 ^ 941746948) == 0) {
                  ;
            }

            var10001.<init>();
            this.random = var10001;
            this.hit = new TimerUtil();
            if (((1904944420 >>> 1 ^ 1213285) >> 4 ^ 59601183) == 0) {
                  ;
            }

            this.swichable = (boolean)(0 << 2 >>> 2 << 1 ^ 1376340779 ^ 1376340778);
            if (((419934366 << 4 | 28244317) >>> 2 >> 2 ^ 153091999) == 0) {
                  ;
            }

            NumberSetting var1 = new NumberSetting;
            if (!"shitted on you harder than archybot".equals("please get a girlfriend and stop cracking plugins")) {
                  ;
            }

            if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("stringer is a good obfuscator")) {
                  ;
            }

            var1.<init>("Range", this, 3.5D, 3.0D, 10.0D, 0.1D);
            this.rangeSet = var1;
            this.fovSet = new NumberSetting("Fov", this, 360.0D, 0.0D, 360.0D, 2.0D);
            this.noSwingSet = new BooleanSetting("No Swing", this, (boolean)((1171559013 << 3 >>> 1 | 360089553 | 191901330) ^ 527888343));
            BooleanSetting var2 = new BooleanSetting;
            if ((((2036609013 ^ 961973232) & 899975056 | 1283274) & 2101735 ^ 2101442) == 0) {
                  ;
            }

            var2.<init>("Animals", this, (boolean)((1692108217 << 1 >> 1 ^ 733675131) >> 4 ^ -50980932));
            this.animalsSet = var2;
            this.playersSet = new BooleanSetting("Players", this, (boolean)((0 ^ 290957406) >>> 3 ^ 36369674));
            if (!"ape covered in human flesh".equals("nefariousMoment")) {
                  ;
            }

            this.mobsSet = new BooleanSetting("Mobs", this, (boolean)((((895357950 | 656156403) ^ 719750386) >>> 3 | 13464935) ^ 67073895));
            this.invisibleTarget = new BooleanSetting("Invisible", this, (boolean)((461006483 ^ 183754234) << 3 >>> 3 ^ 294239081));
            this.autoDelaySet = new BooleanSetting("AutoDelay", this, (boolean)((0 >>> 3 ^ 581634174) >>> 2 << 2 >> 1 ^ 290817087));
            this.delaySet = new NumberSetting("Delay", this, 590.0D, 1.0D, 1500.0D, 1.0D);
            this.yawTarget = new BooleanSetting("Yaw", this, (boolean)(0 >> 2 << 2 >> 3 ^ 1));
            var2 = new BooleanSetting("SwordOnly", this, (boolean)(143791187 >> 3 >>> 2 ^ 548207 ^ 5033421));
            if (!"please go outside".equals("shitted on you harder than archybot")) {
                  ;
            }

            this.swordOnly = var2;
            this.pitchTarget = new BooleanSetting("Pitch", this, (boolean)((90388020 ^ 10512083 | 87748909) >>> 4 ^ 6274846));
            ModeSetting var3 = new ModeSetting;
            String[] var10006 = new String[0 << 3 >> 4 ^ 2];
            var10006[(1502940959 | 639809853 | 2001187991) ^ 2146942911] = "Bypass";
            int var10008 = (0 | 1829434243 | 157012784) ^ 1834742706;
            if ((((694953606 ^ 343305920) << 1 | 400880538) ^ 647050462) != 0) {
                  ;
            }

            var10006[var10008] = "Simple";
            var3.<init>("Mode", this, "Bypass", var10006);
            this.modeSetting = var3;
            var10006 = new String[2 >> 1 << 2 >>> 1 ^ 1];
            var10006[((307679536 | 125396562) & 293969126) >>> 1 ^ 61630329 ^ 187608392] = "Distance";
            var10006[(0 << 4 | 1585372785) ^ 1585372784] = "Health";
            var10006[(1 ^ 0) >>> 2 ^ 2] = "LivingTime";
            this.modePrior = new ModeSetting("Priority", this, "Distance", var10006);
            this.shieldAttack = new TimerUtil();
            Setting[] var4 = new Setting[0 >> 1 << 3 >> 2 << 2 ^ 1];
            var4[(884635501 >>> 3 >>> 3 | 9535824) ^ 13887965] = this.rangeSet;
            this.addSettings(var4);
            if (!"please go outside".equals("You're so fat whenever you go to the beach the tide comes in.")) {
                  ;
            }

            var4 = new Setting[0 << 4 ^ 1148004326 ^ 1148004327];
            if (!"your mom your dad the one you never had".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                  ;
            }

            var4[(1851216531 | 28983499 | 1543399357) & 792804507 ^ 792802459] = this.noSwingSet;
            this.addSettings(var4);
            var4 = new Setting[0 >>> 1 << 1 >> 4 ^ 1];
            var4[2039060117 << 1 ^ 2052955993 ^ -1991331213] = this.animalsSet;
            this.addSettings(var4);
            var4 = new Setting[(0 & 1960416784) >> 4 << 1 ^ 1];
            var4[(347220 >> 1 ^ 157111 ^ '딓') >> 1 ^ 15175] = this.playersSet;
            this.addSettings(var4);
            var4 = new Setting[(1 ^ 0) << 3 << 3 ^ 66];
            if (!"shitted on you harder than archybot".equals("please get a girlfriend and stop cracking plugins")) {
                  ;
            }

            var4[(793950515 | 11912882) << 1 >>> 2 ^ 402391001] = this.mobsSet;
            var4[(0 & 845395469) >>> 1 ^ 1] = this.invisibleTarget;
            this.addSettings(var4);
            int var5 = ((0 << 2 & 166910535 | 1411119723) ^ 1180287133) >>> 3 ^ 38291295;
            if (((143359358 >>> 3 | 4916311) ^ 22769663) == 0) {
                  ;
            }

            var4 = new Setting[var5];
            var4[1607580004 << 3 >>> 1 & 1467646340 ^ 1463976320] = this.modeSetting;
            this.addSettings(var4);
            var4 = new Setting[0 << 2 >> 4 ^ 1];
            if (((1243111808 | 955697799) ^ 335644547 ^ 1862233092) == 0) {
                  ;
            }

            var4[(913156356 << 1 ^ 1372301067 | 523303163 | 448025912) ^ 1068826107] = this.fovSet;
            this.addSettings(var4);
            var4 = new Setting[(0 ^ 785106268) >>> 1 ^ 392553132];
            int var10003 = (886400135 | 337786087) >> 2 ^ 222158137;
            if ((1838984061 << 2 << 4 << 2 ^ 756049022 ^ -1313138306) == 0) {
                  ;
            }

            var4[var10003] = this.modePrior;
            var4[0 & 484883603 & 853727091 ^ 1] = this.swordOnly;
            this.addSettings(var4);
            var4 = new Setting[(1 ^ 0) >> 3 & 748105329 ^ 4];
            var4[(1887068146 >>> 4 & 7390071) << 1 ^ 66286] = this.yawTarget;
            var4[(0 ^ 1933976224) >>> 3 ^ 241747029] = this.pitchTarget;
            var10003 = (0 | 176339421) >>> 1 ^ 88169708;
            BooleanSetting var10004 = this.autoDelaySet;
            if (!"intentMoment".equals("intentMoment")) {
                  ;
            }

            var4[var10003] = var10004;
            var4[0 >> 3 >>> 1 & 471661545 ^ 3] = this.delaySet;
            this.addSettings(var4);
      }

      public List sortByDistance(List var1) {
            int var2 = var1.size();

            for(int var3 = ((355799681 | 31823950) ^ 297368099) & 41526539 ^ 4759560; var3 < var2; ++var3) {
                  for(int var4 = var3 + ((0 >>> 2 << 2 | 1370402410) >> 3 ^ 171300300); var4 < var2; ++var4) {
                        if (mc.player.getDistance((Entity)var1.get(var3)) > mc.player.getDistance((Entity)var1.get(var4))) {
                              Entity var5 = (Entity)var1.get(var3);
                              var1.set(var3, var1.get(var4));
                              var1.set(var4, var5);
                        }
                  }
            }

            return var1;
      }

      public List sortByLivingTime(List var1) {
            int var2 = var1.size();
            int var3 = 537157696 << 3 >> 1 ^ 1147136;

            while(true) {
                  if (((72582909 << 4 ^ 248104592 | 440283594) ^ 1215275220) != 0) {
                        ;
                  }

                  if (var3 >= var2) {
                        return var1;
                  }

                  for(int var4 = var3 + ((0 >>> 1 >> 1 | 261821850) >> 2 << 2 ^ 261821849); var4 < var2; ++var4) {
                        Entity var10000 = (Entity)var1.get(var4);
                        if (((1094629791 ^ 544275021) << 4 >>> 1 & 170328076 ^ -412491810) != 0) {
                              ;
                        }

                        if (var10000.ticksExisted > ((Entity)var1.get(var3)).ticksExisted) {
                              Entity var5 = (Entity)var1.get(var3);
                              if ((((356654213 | 227537679) & 234872393) << 1 >>> 4 ^ 28965825) == 0) {
                                    ;
                              }

                              var1.set(var3, var1.get(var4));
                              var1.set(var4, var5);
                        }
                  }

                  ++var3;
            }
      }
}
