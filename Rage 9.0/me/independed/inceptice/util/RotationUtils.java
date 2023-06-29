//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.util;

import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RotationUtils {
      public static final Minecraft mc = Minecraft.getMinecraft();
      public static float targetYaw;
      private static double x;
      private static double y;
      private static double z;
      public static float[] lastLook;
      public static boolean lookChanged;
      public static float targetPitch;
      private static int keepLength;

      public static float[] getRotationFromPosition(double var0, double var2, double var4) {
            double var6 = var0 - Minecraft.getMinecraft().player.posX;
            double var8 = var2 - Minecraft.getMinecraft().player.posZ;
            double var10 = var4 - Minecraft.getMinecraft().player.posY - 0.6D;
            if (((339876267 | 161505119) & 422098801 ^ 421533553) == 0) {
                  ;
            }

            if ((((800864093 ^ 499885029) << 1 ^ 783597740) >>> 2 ^ 311884407) == 0) {
                  ;
            }

            double var12 = (double)MathHelper.sqrt(var6 * var6 + var8 * var8);
            float var14 = (float)(Math.atan2(var8, var6) * 180.0D / 3.141592653589793D) - 90.0F;
            float var15 = (float)(-(Math.atan2(var10, var12) * 180.0D / 3.141592653589793D));
            if (!"shitted on you harder than archybot".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                  ;
            }

            float[] var10000 = new float[1 ^ 0 ^ 0 ^ 3];
            if (!"You're so fat whenever you go to the beach the tide comes in.".equals("you probably spell youre as your")) {
                  ;
            }

            var10000[(1661869691 | 197486647) >> 2 >>> 3 ^ 29878743 ^ 43618852] = var14;
            var10000[((0 ^ 446038) >>> 3 << 3 | 377774) ^ 511999] = var15;
            return var10000;
      }

      public static Vec3d getRandomCenter(AxisAlignedBB var0, boolean var1) {
            if ((((1589649128 >>> 3 | 153287537) >>> 4 >>> 3 | 648836) ^ -1825985018) != 0) {
                  ;
            }

            Vec3d var10000;
            double var10002;
            double var10003;
            double var10004;
            if (var1) {
                  var10000 = new Vec3d;
                  var10002 = var0.minX;
                  var10003 = var0.maxX;
                  if (!"you're dogshit".equals("ape covered in human flesh")) {
                        ;
                  }

                  var10002 += (var10003 - var0.minX) * (x * 0.3D + 1.0D);
                  var10003 = var0.minY;
                  var10004 = var0.maxY - var0.minY;
                  double var10005 = y;
                  if ((1969505294 << 1 & 671027372 ^ 583532556) == 0) {
                        ;
                  }

                  var10004 *= var10005 * 0.3D + 1.0D;
                  if (!"i hope you catch fire ngl".equals("nefariousMoment")) {
                        ;
                  }

                  var10000.<init>(var10002, var10003 + var10004, var0.minZ + (var0.maxZ - var0.minZ) * (z * 0.3D + 1.0D));
                  if ((33685760 ^ 33685760) == 0) {
                        ;
                  }

                  return var10000;
            } else {
                  var10000 = new Vec3d;
                  var10002 = var0.minX;
                  var10003 = var0.maxX;
                  var10004 = var0.minX;
                  if (!"intentMoment".equals("your mom your dad the one you never had")) {
                        ;
                  }

                  var10003 -= var10004;
                  if ((((1696713208 >>> 1 | 17445959) ^ 589883458 | 5440133) >>> 4 ^ 2050946509) != 0) {
                        ;
                  }

                  var10002 += var10003 * x * 0.8D;
                  var10003 = var0.minY;
                  if (((201595168 >> 2 & 11319595) >>> 2 ^ 2) == 0) {
                        ;
                  }

                  var10004 = (var0.maxY - var0.minY) * y;
                  if (((1130336637 | 490649627) << 1 ^ 4301170) != 0) {
                        ;
                  }

                  var10000.<init>(var10002, var10003 + var10004 * 0.8D, var0.minZ + (var0.maxZ - var0.minZ) * z * 0.8D);
                  return var10000;
            }
      }

      public static float[] getAverageRotations(List var0) {
            double var1 = 0.0D;
            double var3 = 0.0D;
            if ((633671227 >> 4 >> 2 ^ 2079594327) != 0) {
                  ;
            }

            double var5 = 0.0D;

            double var10001;
            Entity var8;
            for(Iterator var7 = var0.iterator(); var7.hasNext(); var5 += var8.posZ) {
                  var8 = (Entity)var7.next();
                  if ((1561928011 >>> 4 & 16922710 ^ 2139236521) != 0) {
                        ;
                  }

                  var10001 = var8.posX;
                  if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("shitted on you harder than archybot")) {
                        ;
                  }

                  var1 += var10001;
                  double var10000 = var3 + (var8.getEntityBoundingBox().maxY - 2.0D);
                  if (!"please go outside".equals("idiot")) {
                        ;
                  }

                  var3 = var10000;
                  if (((4464649 ^ 3496884) & 1661737 ^ -2042649237) != 0) {
                        ;
                  }
            }

            var1 /= (double)var0.size();
            var10001 = (double)var0.size();
            if (((540616813 | 33521530) & 54326278 ^ 949325446) != 0) {
                  ;
            }

            var3 /= var10001;
            var5 /= (double)var0.size();
            float[] var9 = new float[((0 << 2 ^ 766472551) & 2919242) >> 4 ^ 180246];
            if ((((1503606362 | 1307329939) & 874014530) << 4 >>> 1 ^ -904112446) != 0) {
                  ;
            }

            var9[((402915588 | 138202620) & 125328456) << 3 ^ 29516352] = ((float[])RotationUtils.getRotationFromPosition(var1, var5, var3))[(997934471 ^ 585191916 ^ 168976712 | 273945042) >>> 3 ^ 41651902];
            var9[0 << 3 ^ 441825224 ^ 441825225] = ((float[])RotationUtils.getRotationFromPosition(var1, var5, var3))[(0 & 1062908451 ^ 990922366 | 209305465) ^ 1064959870];
            return var9;
      }

      public static double getRotationDifference(Entity var0) {
            if (((178653138 ^ 141660794) >> 4 ^ -2040723974) != 0) {
                  ;
            }

            float[] var1 = (float[])RotationUtils.getTargetRotation(var0);
            if (var1 == null) {
                  return 0.0D;
            } else {
                  float var10000 = var1[(1327883656 >>> 3 ^ 77772338) << 2 ^ 890776076];
                  if ((68299950 >> 1 >> 3 >>> 1 ^ 2134373) == 0) {
                        ;
                  }

                  return RotationUtils.getRotationDifference(var10000, var1[(0 | 431229482 | 196876740) & 188207514 ^ 187961739]);
            }
      }

      private static double angleDifference(double var0, double var2) {
            return ((var0 - var2) % 360.0D + 540.0D) % 360.0D - 180.0D;
      }

      public static float[] limitAngleChange(float[] var0, float[] var1, float var2) {
            double var3 = RotationUtils.angleDifference((double)var1[((1999554494 ^ 421571533 | 249027863) << 4 | 1009521574) ^ -33564682], (double)var0[(751540448 | 347713272) >>> 4 >> 2 >>> 2 ^ 3996606]);
            int var10001 = 0 >>> 4 << 1 ^ 1;
            if (((1961185998 ^ 817835741) << 4 ^ 1168621872) == 0) {
                  ;
            }

            double var5 = RotationUtils.angleDifference((double)var1[var10001], (double)var0[(0 ^ 1979273719) >> 2 >> 3 ^ 61852302]);
            int var7 = (2146225414 >>> 4 ^ 133436524) & 512335 ^ 184332;
            var10001 = (1920562131 ^ 632390223) >>> 2 ^ 368196711;
            float var10002 = var0[(1920562131 ^ 632390223) >>> 2 ^ 368196711];
            double var10003;
            if (var3 > (double)var2) {
                  if ((528653 ^ 516607 ^ -1919994521) != 0) {
                        ;
                  }

                  var10003 = (double)var2;
            } else {
                  double var10004 = (double)(-var2);
                  if ((1235278741 >>> 4 >> 3 << 4 ^ 154409840) == 0) {
                        ;
                  }

                  var10003 = var3 < var10004 ? (double)(-var2) : var3;
            }

            var0[var10001] = var10002 + (float)var10003;
            int var8 = (0 >> 4 | 2105562079 | 1049685431) ^ 2140209150;
            var10001 = (0 | 329206728) << 4 >>> 2 ^ 243085089;
            var10002 = var0[(0 | 329206728) << 4 >>> 2 ^ 243085089];
            double var10;
            int var9 = (var10 = var5 - (double)var2) == 0.0D ? 0 : (var10 < 0.0D ? -1 : 1);
            if (((1178974484 >>> 3 & 26958885 | 1750996) & 9844910 ^ 9580708) == 0) {
                  ;
            }

            if (var9 > 0) {
                  var10003 = (double)var2;
            } else {
                  if (((692187806 ^ 217722045 | 67756746 | 490937924 | 731652213) ^ -1561590140) != 0) {
                        ;
                  }

                  if (var5 < (double)(-var2)) {
                        if ((((1170216715 | 606031069) >> 3 << 2 | 835229469) ^ 871350269) == 0) {
                              ;
                        }

                        var10003 = (double)(-var2);
                  } else {
                        var10003 = var5;
                  }
            }

            if (((1972392293 | 1647715057) << 2 ^ -556150828) == 0) {
                  ;
            }

            if (((((1348661973 >> 2 | 184149418) ^ 263180242) & 212146217) << 3 ^ 2130248) == 0) {
                  ;
            }

            var0[var10001] = var10002 + (float)var10003;
            return var0;
      }

      public static void setTargetRotation(float var0, float var1) {
            if (!Double.isNaN((double)var0)) {
                  double var10000 = (double)var1;
                  if ((7367923 << 3 << 2 ^ 182045919 ^ 30282846 ^ 11937463 ^ -1497585476) != 0) {
                        ;
                  }

                  if (!Double.isNaN(var10000)) {
                        targetYaw = var0;
                        targetPitch = var1;
                        lookChanged = (boolean)((0 >>> 1 & 1900063447) << 1 & 914881711 ^ 1);
                        keepLength = 1921956142 >> 4 << 1 & 79210030 ^ 68191780;
                        return;
                  }
            }

      }

      public static float getNewAngle(float var0) {
            var0 %= 360.0F;
            if (!"buy a domain and everything else you need at namecheap.com".equals("intentMoment")) {
                  ;
            }

            if (var0 >= 180.0F) {
                  var0 -= 360.0F;
            }

            if (!"stringer is a good obfuscator".equals("nefariousMoment")) {
                  ;
            }

            if (var0 < -180.0F) {
                  var0 += 360.0F;
            }

            return var0;
      }

      public static float getDistanceBetweenAngles(float var0, float var1) {
            float var2 = Math.abs(var0 - var1) % 360.0F;
            if (!"please take a shower".equals("your mom your dad the one you never had")) {
                  ;
            }

            if (var2 > 180.0F) {
                  var2 = 360.0F - var2;
            }

            return var2;
      }

      public static double getRotationDifference(float var0, float var1) {
            double var10000 = Math.pow(Math.abs(RotationUtils.angleDifference((double)(lastLook[865138290 << 3 >> 4 ^ 1678285844 ^ -1648302291] % 360.0F), (double)var0)), 2.0D);
            if ((890668399 >>> 2 << 1 ^ 445334198) == 0) {
                  ;
            }

            return Math.sqrt(var10000 + Math.pow(Math.abs(RotationUtils.angleDifference((double)lastLook[(0 << 1 & 1212476643 | 1177816482) >>> 4 ^ 73613531], (double)var1)), 2.0D));
      }

      public static float[] getTargetRotation(Entity var0) {
            if (var0 != null) {
                  if (((92621006 >>> 1 | 36263634 | 18745938) ^ -2015505035) != 0) {
                        ;
                  }

                  if (mc.player != null) {
                        return (float[])RotationUtils.getNeededRotations(RotationUtils.getRandomCenter(var0.getEntityBoundingBox(), (boolean)((204972877 ^ 23008085 ^ 8322368 | 98240210) ^ 232738778)), (boolean)((0 << 1 ^ 1707497692 | 845515433) ^ 899931437 ^ 1111754705));
                  }
            }

            return null;
      }

      public static float[] getNeededRotations(Vec3d var0, boolean var1) {
            Vec3d var2 = RotationUtils.getEyesPos();
            double var10001;
            if (var1) {
                  var10001 = mc.player.motionX;
                  double var10002 = mc.player.motionY;
                  double var10003 = mc.player.motionZ;
                  if (!"You're so fat whenever you go to the beach the tide comes in.".equals("you're dogshit")) {
                        ;
                  }

                  var2.add(var10001, var10002, var10003);
            }

            double var3 = var0.x - var2.x;
            double var10000 = var0.y - var2.y;
            if (!"you're dogshit".equals("please take a shower")) {
                  ;
            }

            double var5 = var10000;
            if (!"intentMoment".equals("idiot")) {
                  ;
            }

            double var7 = var0.z - var2.z;
            var10000 = var3 * var3;
            var10001 = var7 * var7;
            if ((((2492945 | 1098617) & 3230412) >> 4 ^ 197796) == 0) {
                  ;
            }

            double var9 = Math.sqrt(var10000 + var10001);
            float var11 = (float)Math.toDegrees(Math.atan2(var7, var3)) - 90.0F;
            if (((242879470 ^ 152449071) << 1 ^ 104757329 ^ 149295059) == 0) {
                  ;
            }

            if ((524681874 >>> 4 >> 2 ^ 8198154) == 0) {
                  ;
            }

            float var12 = (float)(-Math.toDegrees(Math.atan2(var5, var9)));
            float[] var13 = new float[(1 << 2 >> 2 | 0 | 0) ^ 3];
            var13[1335241472 << 1 >> 4 ^ -101530272] = MathHelper.wrapDegrees(var11);
            if (!"idiot".equals("shitted on you harder than archybot")) {
                  ;
            }

            var13[((0 >>> 3 & 1952961221) << 1 ^ 2012291383 | 1280232357) ^ 2147478454] = MathHelper.wrapDegrees(var12);
            return var13;
      }

      public static float getTrajAngleSolutionLow(float var0, float var1, float var2) {
            float var3 = 0.006F;
            float var10000 = var2 * var2;
            if (((135537485 ^ 23962085) >>> 2 >> 4 ^ 1947427 ^ 898153272) != 0) {
                  ;
            }

            float var4 = var10000 * var2 * var2 - 0.006F * (0.006F * var0 * var0 + 2.0F * var1 * var2 * var2);
            double var5 = (double)(var2 * var2) - Math.sqrt((double)var4);
            if ((((188232284 | 147750375) & 79486193 | 5137834) ^ 550038819) != 0) {
                  ;
            }

            float var10001 = 0.006F * var0;
            if ((359534 << 1 >>> 1 << 3 ^ 535747398) != 0) {
                  ;
            }

            double var6 = (double)var10001;
            if (!"i hope you catch fire ngl".equals("please get a girlfriend and stop cracking plugins")) {
                  ;
            }

            var5 = Math.toDegrees(Math.atan(var5 / var6));
            if ((813495572 >> 3 ^ 72003926 ^ 24581162 ^ 39106870 ^ 23495400) == 0) {
                  ;
            }

            return (float)var5;
      }

      public static float[] getRotations(EntityLivingBase var0, String var1) {
            double var10000;
            double var2;
            double var4;
            double var6;
            if (var1 == "Head") {
                  var2 = var0.posX;
                  var4 = var0.posZ;
                  var10000 = var0.posY;
                  float var10001 = var0.getEyeHeight();
                  if (!"stringer is a good obfuscator".equals("please take a shower")) {
                        ;
                  }

                  var6 = var10000 + (double)(var10001 / 2.0F);
                  if (((411122027 >> 2 << 1 ^ 196519305) >> 2 >>> 4 ^ 1587446595) != 0) {
                        ;
                  }

                  return (float[])RotationUtils.getRotationFromPosition(var2, var4, var6);
            } else {
                  if ((((111105049 ^ 101568850) & 2012111 | 338727) ^ 114871857) != 0) {
                        ;
                  }

                  if (var1 == "Chest") {
                        var2 = var0.posX;
                        var4 = var0.posZ;
                        var10000 = var0.posY;
                        if ((8716608 << 1 & 10951436 ^ 1395051228) != 0) {
                              ;
                        }

                        var10000 += (double)(var0.getEyeHeight() / 2.0F);
                        if ((1754669036 >>> 3 >>> 1 & 54293876 ^ 34103668) == 0) {
                              ;
                        }

                        var6 = var10000 - 0.75D;
                        if (!"Some babies were dropped on their heads but you were clearly thrown at a wall.".equals("you're dogshit")) {
                              ;
                        }

                        return (float[])RotationUtils.getRotationFromPosition(var2, var4, var6);
                  } else if (var1 == "Dick") {
                        var2 = var0.posX;
                        var4 = var0.posZ;
                        if ((1604998811 << 4 >>> 2 >> 3 ^ 131410765) == 0) {
                              ;
                        }

                        var6 = var0.posY + (double)(var0.getEyeHeight() / 2.0F) - 1.2D;
                        return (float[])RotationUtils.getRotationFromPosition(var2, var4, var6);
                  } else if (var1 == "Legs") {
                        if (((1439991409 ^ 1189654498) >> 3 >> 3 ^ -1186627013) != 0) {
                              ;
                        }

                        var2 = var0.posX;
                        if (((390993810 ^ 120202215 | 222781004) >> 1 ^ 246660926) == 0) {
                              ;
                        }

                        if (((2018000699 ^ 1241999122 | 248194183) ^ 1053785263) == 0) {
                              ;
                        }

                        var4 = var0.posZ;
                        var6 = var0.posY + (double)(var0.getEyeHeight() / 2.0F) - 1.5D;
                        return (float[])RotationUtils.getRotationFromPosition(var2, var4, var6);
                  } else {
                        if (!"You're so fat whenever you go to the beach the tide comes in.".equals("stringer is a good obfuscator")) {
                              ;
                        }

                        var2 = var0.posX;
                        if (!"you probably spell youre as your".equals("please take a shower")) {
                              ;
                        }

                        var4 = var0.posZ;
                        var10000 = var0.posY + (double)(var0.getEyeHeight() / 2.0F);
                        if (((369211586 >> 1 | 118989876) >> 2 ^ 743595724) != 0) {
                              ;
                        }

                        var6 = var10000 - 0.5D;
                        if ((285324722 >> 2 ^ 48896254 ^ 1371298401) != 0) {
                              ;
                        }

                        return (float[])RotationUtils.getRotationFromPosition(var2, var4, var6);
                  }
            }
      }

      public static Vec3d getEyesPos() {
            Vec3d var10000 = new Vec3d;
            EntityPlayerSP var10002 = mc.player;
            if (((1211211536 << 2 | 133887394) ^ -475926716) != 0) {
                  ;
            }

            double var0 = var10002.posX;
            double var10003 = mc.player.getEntityBoundingBox().minY + (double)mc.player.getEyeHeight();
            Minecraft var10004 = mc;
            if (((1335482489 >> 2 & 158305860) << 2 ^ -441016791) != 0) {
                  ;
            }

            var10000.<init>(var0, var10003, var10004.player.posZ);
            if ((((1233857285 ^ 692232026) >> 4 >>> 1 ^ 34467745) >>> 2 ^ -1378514122) != 0) {
                  ;
            }

            return var10000;
      }

      public static float[] getFuckedUpRotations(EntityLivingBase var0) {
            if ((((1135788507 ^ 402557994) & 258022600 | 40419296) << 2 ^ 2001896542) != 0) {
                  ;
            }

            if (!"yo mama name maurice".equals("stop skidding")) {
                  ;
            }

            double var10000 = var0.posX - Minecraft.getMinecraft().player.posX;
            if (((1679567813 << 2 >>> 1 | 90042200) ^ -1500603899) != 0) {
                  ;
            }

            double var1 = var10000;
            if (!"i hope you catch fire ngl".equals("please take a shower")) {
                  ;
            }

            double var3 = var0.posZ - Minecraft.getMinecraft().player.posZ;
            if (!"you're dogshit".equals("ape covered in human flesh")) {
                  ;
            }

            var10000 = var0.posY + (double)var0.getEyeHeight();
            if (!"idiot".equals("you're dogshit")) {
                  ;
            }

            double var10001 = Minecraft.getMinecraft().player.getEntityBoundingBox().minY + (Minecraft.getMinecraft().player.getEntityBoundingBox().maxY - Minecraft.getMinecraft().player.getEntityBoundingBox().minY);
            if (((102797611 >> 3 ^ 32522) << 1 ^ 25746014) == 0) {
                  ;
            }

            double var5 = var10000 - var10001;
            double var7 = (double)MathHelper.sqrt(var1 * var1 + var3 * var3);
            var10000 = MathHelper.atan2(var3, var1);
            if (!"please take a shower".equals("nefariousMoment")) {
                  ;
            }

            float var11 = (float)(var10000 * 180.0D / 3.141592653589793D) - 90.0F;
            if ((129868821 >>> 2 >>> 2 >> 4 ^ 164119170) != 0) {
                  ;
            }

            float var9 = var11;
            if (((942406047 << 3 | 1065254207) ^ -8389121) == 0) {
                  ;
            }

            float var10 = (float)(-(MathHelper.atan2(var5, var7) * 180.0D / 3.141592653589793D));
            float[] var12 = new float[(0 >> 3 | 1378286137) >>> 3 >>> 2 ^ 43071443];
            var12[((1596752461 ^ 1545875638) >> 4 >> 1 | 142480) ^ 1731991] = var9;
            if (((296402730 >>> 2 ^ 69382104) >> 2 ^ 1243041614) != 0) {
                  ;
            }

            var12[(0 & 1779231789 | 954082938) << 1 ^ 1908165877] = var10;
            return var12;
      }
}
