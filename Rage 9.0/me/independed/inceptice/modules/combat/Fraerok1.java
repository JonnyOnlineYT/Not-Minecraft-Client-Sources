//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.combat;

import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import me.independed.inceptice.modules.Module;
import me.independed.inceptice.settings.BooleanSetting;
import me.independed.inceptice.settings.NumberSetting;
import me.independed.inceptice.settings.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class Fraerok1 extends Module {
      private Random rand;
      public NumberSetting range;
      public BooleanSetting smoothAim;
      public static double smoothAimSpeed = 2.5D;

      public Fraerok1() {
            if ((((1890785341 | 167445431) ^ 703847450 | 957680555) ^ 194675618 ^ 1921469453) == 0) {
                  ;
            }

            super("AimAssist", "aims at entity", (1799042015 ^ 1229002838) & 213186789 ^ 3156097, Module.Category.COMBAT);
            NumberSetting var10001 = new NumberSetting;
            if ((4160 ^ -1815828209) != 0) {
                  ;
            }

            var10001.<init>("Range", this, 4.0D, 0.5D, 10.0D, 0.1D);
            this.range = var10001;
            if ((141371672 << 4 << 2 >> 4 & 28477548 ^ 1445287772) != 0) {
                  ;
            }

            this.smoothAim = new BooleanSetting("Smooth", this, (boolean)((0 >> 1 ^ 1773310777) >> 3 ^ 221663846));
            this.rand = new Random();
            int var1 = (1 | 0) << 2 >>> 2 ^ 3;
            if (!"yo mama name maurice".equals("ape covered in human flesh")) {
                  ;
            }

            Setting[] var2 = new Setting[var1];
            var2[92293664 >>> 2 >>> 2 & 2536340 ^ 128] = this.smoothAim;
            int var10003 = (0 >>> 1 | 1411107266) ^ 545572701 ^ 1956581022;
            if (!"please get a girlfriend and stop cracking plugins".equals("please get a girlfriend and stop cracking plugins")) {
                  ;
            }

            if (((920245065 | 309128225) << 3 ^ -1209009336) == 0) {
                  ;
            }

            var2[var10003] = this.range;
            this.addSettings(var2);
      }

      public void smoothFacePos(Vec3d var1) {
            double var10000 = var1.x;
            if (!"stop skidding".equals("nefariousMoment")) {
                  ;
            }

            double var2 = var10000 + 0.5D - Minecraft.getMinecraft().player.posX;
            var10000 = var1.y + 0.5D;
            double var10001 = Minecraft.getMinecraft().player.posY;
            if (!"idiot".equals("i hope you catch fire ngl")) {
                  ;
            }

            var10000 -= var10001 + (double)Minecraft.getMinecraft().player.getEyeHeight();
            if (((482685070 ^ 431396733 ^ 28438151) >> 1 >>> 2 ^ -1115546430) != 0) {
                  ;
            }

            double var4 = var10000;
            double var6 = var1.z + 0.5D - Minecraft.getMinecraft().player.posZ;
            if (((307626086 ^ 119454979) << 1 ^ 714177226) == 0) {
                  ;
            }

            float var16 = MathHelper.sqrt(var2 * var2 + var6 * var6);
            if (((306449846 | 184810151) << 3 << 1 ^ -1344155478) != 0) {
                  ;
            }

            double var8 = (double)var16;
            if (((1335898894 >> 4 & 61353957 ^ 8741985) << 2 ^ -886984535) != 0) {
                  ;
            }

            var10000 = Math.atan2(var6, var2);
            if (!"you probably spell youre as your".equals("stringer is a good obfuscator")) {
                  ;
            }

            float var10 = (float)(var10000 * 180.0D / 3.141592653589793D) - 90.0F;
            var10000 = Math.atan2(var4, var8);
            if ((((1942635327 >>> 1 ^ 670098743) >> 2 | 107203855) ^ 132644143) == 0) {
                  ;
            }

            var10000 *= 180.0D;
            if ((953617066 >> 2 >> 2 >> 4 ^ 3725066) == 0) {
                  ;
            }

            var10000 /= 3.141592653589793D;
            if ((1019274020 << 4 >> 3 >> 3 ^ 446913146 ^ 1898619176) != 0) {
                  ;
            }

            float var11 = (float)(-var10000);
            int var12 = ((321773824 ^ 230402760 | 336253676) ^ 265962025) >>> 4 ^ 18106780;
            float var13 = 5.0F;
            float var14 = 0.0F;
            if (((601247103 | 535833798) >> 2 >> 4 ^ 16767415) == 0) {
                  ;
            }

            var16 = MathHelper.wrapDegrees(var10 - Minecraft.getMinecraft().player.rotationYaw);
            if (((2052748795 | 492430282) >>> 4 ^ 9658849 ^ 124182174) == 0) {
                  ;
            }

            float var17 = var13 * 2.0F;
            if (((389540906 >>> 2 | 53529089 | 47746522) ^ 1195810374) != 0) {
                  ;
            }

            float var21;
            int var18 = (var21 = var16 - var17) == 0.0F ? 0 : (var21 < 0.0F ? -1 : 1);
            if ((538970640 >> 3 << 1 ^ 52400455 ^ 186094531) == 0) {
                  ;
            }

            if (var18 > 0) {
                  var12 = (0 & 1050196931 ^ 1817796264 | 6453193) ^ 1820033000;
            } else {
                  var16 = var10 - Minecraft.getMinecraft().player.rotationYaw;
                  if (((526175004 << 3 & 1688548220 | 1508287355) ^ 2045162363) == 0) {
                        ;
                  }

                  var16 = MathHelper.wrapDegrees(var16);
                  var17 = -var13;
                  if ((879895913 << 4 >>> 3 >> 4 ^ 9323693) == 0) {
                        ;
                  }

                  if (var16 < var17 * 2.0F) {
                        var12 = (0 >> 1 | 716738801) & 694249841 & 545026588 ^ 538968081;
                        if ((((317073224 ^ 91260541) >> 1 | 190096089) ^ 795279961) != 0) {
                              ;
                        }

                        var14 = -var13;
                  }
            }

            float var15 = 0.0F;
            if (MathHelper.wrapDegrees(var11 - Minecraft.getMinecraft().player.rotationPitch) > var13 * 4.0F) {
                  var12 = (0 | 1256948082) >> 1 ^ 163690553 ^ 750024833;
            } else if (MathHelper.wrapDegrees(var11 - Minecraft.getMinecraft().player.rotationPitch) < -var13 * 4.0F) {
                  var12 = (0 ^ 1076730799) >> 2 ^ 196540739 ^ 465412009;
                  var15 = -var13;
            }

            if (var12 != 0) {
                  EntityPlayerSP var20 = Minecraft.getMinecraft().player;
                  var10001 = (double)var20.rotationYaw;
                  double var10002 = (double)MathHelper.wrapDegrees(var10 - Minecraft.getMinecraft().player.rotationYaw);
                  double var10003 = Fraerok1.getSmoothAimSpeed();
                  double var10004 = this.rand.nextDouble() * 2.0D;
                  if (((1510060880 | 944062457) << 1 ^ -192184334) == 0) {
                        ;
                  }

                  ++var10004;
                  if ((728614788 >> 1 >> 1 & 137347870 ^ 134955264) == 0) {
                        ;
                  }

                  var20.rotationYaw = (float)(var10001 + var10002 / (var10003 * var10004));
                  var20 = Minecraft.getMinecraft().player;
                  var10001 = (double)var20.rotationPitch;
                  EntityPlayerSP var19 = Minecraft.getMinecraft().player;
                  if (((134217744 | 132452611 | 57427505) & 254464469 ^ 254300433) == 0) {
                        ;
                  }

                  var10002 = (double)MathHelper.wrapDegrees(var11 - var19.rotationPitch);
                  var10003 = Fraerok1.getSmoothAimSpeed();
                  if (!"stringer is a good obfuscator".equals("stop skidding")) {
                        ;
                  }

                  var20.rotationPitch = (float)(var10001 + var10002 / (var10003 * (this.rand.nextDouble() * 2.0D + 1.0D)));
            }

      }

      public float[] getRotations(EntityPlayer var1) {
            double var2 = var1.posX + (var1.posX - var1.lastTickPosX) - mc.player.posX;
            if (!"please dont crack my plugin".equals("please dont crack my plugin")) {
                  ;
            }

            double var4 = var1.posY + (double)var1.getEyeHeight() - mc.player.posY + (double)mc.player.getEyeHeight() - 3.5D;
            double var10000 = var1.posZ + (var1.posZ - var1.lastTickPosZ) - mc.player.posZ;
            if ((1552643300 << 3 >>> 4 ^ 1777169772) != 0) {
                  ;
            }

            double var6 = var10000;
            if (((1052682 ^ 121556 | 541820) ^ 1699979340) != 0) {
                  ;
            }

            if (!"stringer is a good obfuscator".equals("please dont crack my plugin")) {
                  ;
            }

            double var8 = Math.sqrt(Math.pow(var2, 2.0D) + Math.pow(var6, 2.0D));
            float var10 = (float)Math.toDegrees(-Math.atan(var2 / var6));
            var10000 = Math.toDegrees(Math.atan(var4 / var8));
            if (((1886701737 | 307867241) << 4 << 4 ^ 2112809216) == 0) {
                  ;
            }

            float var11 = (float)(-var10000);
            int var12;
            double var10001;
            if (var2 < 0.0D && var6 < 0.0D) {
                  var10001 = var6 / var2;
                  if ((18743030 >> 4 >> 1 & 154483 ^ 19315) == 0) {
                        ;
                  }

                  var10 = (float)(90.0D + Math.toDegrees(Math.atan(var10001)));
            } else {
                  if ((1362282427 >>> 2 & 231937096 ^ 36269864 ^ 107577184) == 0) {
                        ;
                  }

                  double var14;
                  var12 = (var14 = var2 - 0.0D) == 0.0D ? 0 : (var14 < 0.0D ? -1 : 1);
                  if (((1082755330 | 1042409864) ^ 2125064586) == 0) {
                        ;
                  }

                  if (var12 > 0 && var6 < 0.0D) {
                        if (((1276862458 << 1 >>> 4 | 34677165) ^ 194210815) == 0) {
                              ;
                        }

                        var10001 = Math.toDegrees(Math.atan(var6 / var2));
                        if (((1377664838 | 28989869) & 416071740 ^ -396336935) != 0) {
                              ;
                        }

                        var10 = (float)(-90.0D + var10001);
                  }
            }

            if (((174077187 ^ 82803044) >>> 3 ^ 391530540) != 0) {
                  ;
            }

            var12 = (1 >> 2 & 107430288) >>> 3 ^ 2;
            if ((((631510469 | 435645348) << 1 | 1449297077) ^ 2146369535) == 0) {
                  ;
            }

            float[] var13 = new float[var12];
            var13[(400481479 >> 3 << 3 ^ 157616615 | 73340789 | 438121147) ^ 520091647] = var10;
            int var10002 = (0 & 1231081183 ^ 2055950833) >>> 2 ^ 513987709;
            if (((8274207 | 4683021) ^ 4420284 ^ 1233745591) != 0) {
                  ;
            }

            var13[var10002] = var11;
            return var13;
      }

      static {
            if (((799395140 | 491846992) ^ 936300364 ^ 138097176) == 0) {
                  ;
            }

      }

      @SubscribeEvent
      public void onPlayerTick(PlayerTickEvent var1) {
            if (mc.player != null) {
                  Stream var10000 = mc.world.loadedEntityList.stream();
                  Predicate var10001 = (var0) -> {
                        if (!"stop skidding".equals("please dont crack my plugin")) {
                              ;
                        }

                        return (boolean)(var0 != mc.player ? (0 ^ 88647569) << 4 << 2 ^ 1378477121 : (2112732657 >> 3 ^ 255561640) << 2 >>> 2 ^ 8796182);
                  };
                  if (((40770400 >>> 1 ^ 3984448) << 4 ^ -132499847) != 0) {
                        ;
                  }

                  var10000 = var10000.filter(var10001);
                  var10001 = (var1x) -> {
                        float var10000 = mc.player.getDistance(var1x);
                        if (!"idiot".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                              ;
                        }

                        double var2 = (double)var10000;
                        if (!"you're dogshit".equals("nefariousMoment")) {
                              ;
                        }

                        int var3;
                        if (var2 <= this.range.getValue()) {
                              var3 = (0 >>> 4 ^ 216773715) >> 4 ^ 13548356;
                        } else {
                              if (((251472805 >> 4 >> 4 | 450304 | 855693) & 22312 ^ 22312) == 0) {
                                    ;
                              }

                              var3 = (710877132 >>> 2 & 119205942 | 11581799) ^ 45283703;
                        }

                        return (boolean)var3;
                  };
                  if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("buy a domain and everything else you need at namecheap.com")) {
                        ;
                  }

                  var10000 = var10000.filter(var10001).filter((var0) -> {
                        if (!"your mom your dad the one you never had".equals("please get a girlfriend and stop cracking plugins")) {
                              ;
                        }

                        return (boolean)(!var0.isDead ? 0 >>> 4 << 1 >> 4 ^ 1 : (1851226015 >>> 1 | 30533832) ^ 939260879);
                  });
                  var10001 = (var0) -> {
                        if (((1474987810 << 3 & 1142764110 | 61504171) ^ 129923755) == 0) {
                              ;
                        }

                        return var0 instanceof EntityPlayer;
                  };
                  if (((1334464614 << 3 | 582980473 | 1204116500) << 3 ^ -66584) == 0) {
                        ;
                  }

                  var10000 = var10000.filter(var10001);
                  if (!"stringer is a good obfuscator".equals("please dont crack my plugin")) {
                        ;
                  }

                  var10000 = var10000.filter((var0) -> {
                        return (boolean)(!var0.isInvisible() ? ((0 << 1 ^ 1062386981) << 4 | 1959764807) ^ -168823978 : (1674691220 ^ 1438540598 | 419377277) ^ 1056944639);
                  }).filter((var0) -> {
                        int var10000;
                        if (((EntityPlayer)var0).getHealth() > 0.0F) {
                              var10000 = 0 >> 4 >> 4 ^ 1;
                        } else {
                              if (((1884597671 | 1343620966) << 4 ^ 90898032) == 0) {
                                    ;
                              }

                              var10000 = 1960534843 >>> 3 >> 1 << 4 ^ 1960534832;
                        }

                        return (boolean)var10000;
                  }).sorted(Comparator.comparing((var0) -> {
                        EntityPlayerSP var10000 = mc.player;
                        if (((557933012 << 4 << 4 | 457124477) ^ 1535114877) == 0) {
                              ;
                        }

                        return Float.valueOf(var10000.getDistance(var0));
                  }));
                  Collector var12 = Collectors.toList();
                  if ((((1062084457 ^ 450642083) & 366199696 ^ 77025018) & 3596179 ^ 265490) == 0) {
                        ;
                  }

                  List var2 = (List)var10000.collect(var12);
                  int var3 = var2.size();
                  if (var3 > 0) {
                        EntityPlayer var4 = (EntityPlayer)var2.get(440443396 << 4 & 1572038310 & 66137913 ^ 7538 ^ 15730);
                        double var5 = var4.posX - 0.5D + (var4.posX - var4.lastTickPosX) * 2.5D;
                        if (((122756438 ^ 121109597 ^ 3094462) << 2 >>> 1 ^ 1999796808) != 0) {
                              ;
                        }

                        double var11 = var4.posY + ((double)var4.getEyeHeight() - (double)var4.height / 1.5D);
                        double var13 = var4.posY;
                        if (((1114612866 | 1009665216) ^ 563138249 ^ 1610557963) == 0) {
                              ;
                        }

                        var13 = (var13 - var4.lastTickPosY) * 1.5D;
                        if ((279640086 >> 3 << 2 ^ 99992062 ^ -1555511838) != 0) {
                              ;
                        }

                        double var7 = var11 - var13;
                        double var9 = var4.posZ - 0.5D + (var4.posZ - var4.lastTickPosZ) * 2.5D;
                        this.facePos(new Vec3d(var5, var7, var9));
                  }

            }
      }

      public void facePos(Vec3d var1) {
            if (this.smoothAim.isEnabled()) {
                  this.smoothFacePos(var1);
            } else {
                  if (!"please go outside".equals("intentMoment")) {
                        ;
                  }

                  double var10000 = var1.x + 0.5D;
                  if (((1496487182 >>> 4 ^ 51038826 | 55297396) >>> 3 ^ -385598345) != 0) {
                        ;
                  }

                  double var2 = var10000 - Minecraft.getMinecraft().player.posX;
                  var10000 = var1.y + 0.5D;
                  if (((1243053721 >> 1 | 413560971 | 565311876) ^ 929271171 ^ 182219340) == 0) {
                        ;
                  }

                  if (((1166294631 >>> 2 << 3 | 56344817) ^ -1956905735) == 0) {
                        ;
                  }

                  double var10001 = Minecraft.getMinecraft().player.posY;
                  double var10002 = (double)Minecraft.getMinecraft().player.getEyeHeight();
                  if (!"You're so fat whenever you go to the beach the tide comes in.".equals("You're so fat whenever you go to the beach the tide comes in.")) {
                        ;
                  }

                  double var4 = var10000 - (var10001 + var10002);
                  double var6 = var1.z + 0.5D - Minecraft.getMinecraft().player.posZ;
                  double var8 = (double)MathHelper.sqrt(var2 * var2 + var6 * var6);
                  var10000 = Math.atan2(var6, var2) * 180.0D;
                  if (!"stop skidding".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                        ;
                  }

                  float var10 = (float)(var10000 / 3.141592653589793D) - 90.0F;
                  float var11 = (float)(-(Math.atan2(var4, var8) * 180.0D / 3.141592653589793D));
                  Minecraft.getMinecraft().player.rotationYaw += MathHelper.wrapDegrees(var10 - Minecraft.getMinecraft().player.rotationYaw);
                  Minecraft.getMinecraft().player.rotationPitch += MathHelper.wrapDegrees(var11 - Minecraft.getMinecraft().player.rotationPitch);
            }
      }

      public static double getSmoothAimSpeed() {
            return smoothAimSpeed;
      }
}
