//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.util;

import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RotationUtils {
      public static boolean lookChanged;
      private static double x;
      private static int keepLength;
      public static float[] lastLook;
      private static double y;
      public static final Minecraft mc = Minecraft.getMinecraft();
      private static double z;
      public static float targetPitch;
      public static float targetYaw;

      public static float getDistanceBetweenAngles(float var0, float var1) {
            float var2 = Math.abs(var0 - var1) % 360.0F;
            if (var2 > 180.0F) {
                  if ((((1210563121 >> 4 ^ 50878254) & 104782433) << 4 ^ 1621131280) == 0) {
                        ;
                  }

                  var2 = 360.0F - var2;
            }

            return var2;
      }

      private static double angleDifference(double var0, double var2) {
            return ((var0 - var2) % 360.0D + 540.0D) % 360.0D - 180.0D;
      }

      public static float[] getAverageRotations(List var0) {
            double var1 = 0.0D;
            double var3 = 0.0D;
            double var5 = 0.0D;
            Iterator var7 = var0.iterator();

            while(true) {
                  if ((((527106849 ^ 383990212 | 45897567) >>> 1 | 25054936) ^ -1283341336) == 0) {
                  }

                  if (!var7.hasNext()) {
                        if (((386978389 << 2 | 1442232663) >> 2 >>> 4 << 1 ^ 49266634) == 0) {
                              ;
                        }

                        if (!"intentMoment".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                              ;
                        }

                        var1 /= (double)var0.size();
                        int var10001 = var0.size();
                        if (((811987823 >>> 2 & 34005324 | 584 | 12001) ^ 1625135613) != 0) {
                              ;
                        }

                        double var9 = (double)var10001;
                        if (!"buy a domain and everything else you need at namecheap.com".equals("you probably spell youre as your")) {
                              ;
                        }

                        var3 /= var9;
                        var5 /= (double)var0.size();
                        float[] var10000 = new float[1 >>> 2 << 2 ^ 2];
                        if (((1644785083 << 3 ^ 194258453 | 365004841) ^ 534760429) == 0) {
                              ;
                        }

                        var10000[906753601 >>> 3 & 41476358 & 121958 ^ 16384] = ((float[])RotationUtils.getRotationFromPosition(var1, var5, var3))[((924873751 << 1 | 24711642) ^ 1430609363) & 582176663 ^ 573572101];
                        var10000[((0 & 169834009) >>> 2 | 1151415728) ^ 1066837029 ^ 2067239316] = ((float[])RotationUtils.getRotationFromPosition(var1, var5, var3))[0 >> 2 << 2 >> 1 ^ 1];
                        return var10000;
                  }

                  Entity var8 = (Entity)var7.next();
                  if ((197351697 << 3 << 1 >> 1 ^ -568670072) == 0) {
                        ;
                  }

                  if (((1074991792 ^ 876367937 ^ 648518175 | 702605167 | 849006888) ^ -375823607) != 0) {
                        ;
                  }

                  var1 += var8.posX;
                  if (!"please dont crack my plugin".equals("minecraft")) {
                        ;
                  }

                  var3 += var8.getEntityBoundingBox().maxY - 2.0D;
                  var5 += var8.posZ;
                  if ((82140052 << 2 >> 3 ^ 20869532 ^ -329434479) != 0) {
                        ;
                  }
            }
      }

      public static float[] getTargetRotation(Entity var0) {
            return var0 != null && mc.player != null ? (float[])RotationUtils.getNeededRotations(RotationUtils.getRandomCenter(var0.getEntityBoundingBox(), (boolean)((574989081 << 4 & 17200998 ^ '鑪') >>> 3 << 2 ^ 86708)), (boolean)((0 << 2 >>> 2 | 1360873762) ^ 1360873763)) : null;
      }

      public static void setTargetRotation(float var0, float var1) {
            if (!Double.isNaN((double)var0) && !Double.isNaN((double)var1)) {
                  targetYaw = var0;
                  targetPitch = var1;
                  lookChanged = (boolean)((0 << 2 ^ 2014672720) >>> 4 ^ 47025554 ^ 88922854);
                  keepLength = (842402039 >>> 4 ^ 6486410) >> 1 ^ 27315330;
            }
      }

      public static float[] getNeededRotations(Vec3d var0, boolean var1) {
            Vec3d var2 = RotationUtils.getEyesPos();
            if (var1) {
                  double var10001 = mc.player.motionX;
                  double var10002 = mc.player.motionY;
                  Minecraft var10003 = mc;
                  if ((1166108829 >> 2 >> 4 ^ 1534134784) != 0) {
                        ;
                  }

                  var2.add(var10001, var10002, var10003.player.motionZ);
            }

            if (((889192712 >>> 4 | 11891862) << 3 ^ -438055214) != 0) {
                  ;
            }

            double var3 = var0.x - var2.x;
            double var5 = var0.y - var2.y;
            if ((355515575 ^ 166865612 ^ 477170522 ^ 882485746) != 0) {
                  ;
            }

            double var7 = var0.z - var2.z;
            double var9 = Math.sqrt(var3 * var3 + var7 * var7);
            float var11 = (float)Math.toDegrees(Math.atan2(var7, var3)) - 90.0F;
            if (((1324104948 << 3 & 1842487230) >> 4 >>> 2 ^ -300213040) != 0) {
                  ;
            }

            if ((654018118 << 3 ^ 742868541 ^ 463172621) == 0) {
                  ;
            }

            float var12 = (float)(-Math.toDegrees(Math.atan2(var5, var9)));
            float[] var10000 = new float[((1 ^ 0 ^ 0) >>> 4 & 1493400405 | 1176910116) ^ 1176910118];
            var10000[13402368 << 4 >>> 2 ^ 53609472] = MathHelper.wrapDegrees(var11);
            int var13 = 0 >>> 2 >>> 3 ^ 1;
            if ((425752944 >>> 3 >>> 3 ^ -1985345665) != 0) {
                  ;
            }

            var10000[var13] = MathHelper.wrapDegrees(var12);
            return var10000;
      }

      public static float[] getRotationFromPosition(double var0, double var2, double var4) {
            double var6 = var0 - Minecraft.getMinecraft().player.posX;
            if (!"please go outside".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                  ;
            }

            double var8 = var2 - Minecraft.getMinecraft().player.posZ;
            double var10 = var4 - Minecraft.getMinecraft().player.posY - 0.6D;
            double var10000 = var6 * var6;
            double var10001 = var8 * var8;
            if (((526920159 >>> 1 | 248529159) >> 2 ^ 62341385 ^ 4871282) == 0) {
                  ;
            }

            double var12 = (double)MathHelper.sqrt(var10000 + var10001);
            float var14 = (float)(Math.atan2(var8, var6) * 180.0D / 3.141592653589793D) - 90.0F;
            var10000 = Math.atan2(var10, var12) * 180.0D;
            if ((((1350628915 >> 2 & 25793851) << 4 | '\ued4c') ^ 130508) == 0) {
                  ;
            }

            float var15 = (float)(-(var10000 / 3.141592653589793D));
            float[] var16 = new float[(1 << 2 >> 4 << 1 ^ 96175981) >> 3 ^ 12021999];
            if (!"please get a girlfriend and stop cracking plugins".equals("please get a girlfriend and stop cracking plugins")) {
                  ;
            }

            if (!"please get a girlfriend and stop cracking plugins".equals("your mom your dad the one you never had")) {
                  ;
            }

            var16[((1475280864 ^ 343023354) & 396594876) >> 4 ^ 3672065] = var14;
            var16[((0 | 1131517740) >>> 2 | 47232113) << 4 ^ 768565169] = var15;
            return var16;
      }

      public static double getRotationDifference(float var0, float var1) {
            float[] var10000 = lastLook;
            if ((709731356 >>> 4 & 32185398 ^ 6536113 ^ 12821425) == 0) {
                  ;
            }

            double var2 = (double)(var10000[1105666929 >> 1 & 420740323 & 495731 & 221508 ^ 196608] % 360.0F);
            if ((((2001953367 ^ 756202922) >> 3 & 47408720) << 2 ^ 151030080) == 0) {
                  ;
            }

            var2 = Math.pow(Math.abs(RotationUtils.angleDifference(var2, (double)var0)), 2.0D);
            double var10001 = (double)lastLook[0 >>> 2 >>> 2 >> 3 >>> 1 >>> 4 ^ 1];
            double var10002 = (double)var1;
            if ((33897031 >> 2 >>> 4 ^ -855996131) != 0) {
                  ;
            }

            return Math.sqrt(var2 + Math.pow(Math.abs(RotationUtils.angleDifference(var10001, var10002)), 2.0D));
      }

      public static float[] getFuckedUpRotations(EntityLivingBase var0) {
            double var1 = var0.posX - Minecraft.getMinecraft().player.posX;
            if ((1005691992 << 2 << 1 ^ 1496500018) != 0) {
                  ;
            }

            double var3 = var0.posZ - Minecraft.getMinecraft().player.posZ;
            double var10000 = var0.posY + (double)var0.getEyeHeight();
            double var10001 = Minecraft.getMinecraft().player.getEntityBoundingBox().minY;
            if (!"Some babies were dropped on their heads but you were clearly thrown at a wall.".equals("ape covered in human flesh")) {
                  ;
            }

            double var5 = var10000 - (var10001 + (Minecraft.getMinecraft().player.getEntityBoundingBox().maxY - Minecraft.getMinecraft().player.getEntityBoundingBox().minY));
            var10000 = var1 * var1;
            if (!"please dont crack my plugin".equals("shitted on you harder than archybot")) {
                  ;
            }

            double var7 = (double)MathHelper.sqrt(var10000 + var3 * var3);
            float var11 = (float)(MathHelper.atan2(var3, var1) * 180.0D / 3.141592653589793D) - 90.0F;
            if (((352048133 >> 4 ^ 15467810) >> 3 ^ 3438348) == 0) {
                  ;
            }

            float var9 = var11;
            float var10 = (float)(-(MathHelper.atan2(var5, var7) * 180.0D / 3.141592653589793D));
            float[] var12 = new float[((0 ^ 436387911 | 71192027) & 251608548) >> 4 ^ 14934238];
            int var10002 = 491374761 >> 3 & 25082257 ^ 19413265;
            if ((246625387 >> 4 >>> 4 << 2 ^ 532665962) != 0) {
                  ;
            }

            var12[var10002] = var9;
            var12[0 >>> 2 << 3 << 4 ^ 1] = var10;
            return var12;
      }

      public static float[] limitAngleChange(float[] var0, float[] var1, float var2) {
            if (((761276298 | 656094609) << 1 >>> 4 >> 2 ^ 24893884) == 0) {
                  ;
            }

            double var3 = RotationUtils.angleDifference((double)var1[(65854188 | 15602217) & 39512730 ^ 38455944], (double)var0[1003950404 << 2 >> 3 >>> 4 ^ 266254474]);
            double var5 = RotationUtils.angleDifference((double)var1[((0 | 609287456) & 23441372) >> 1 ^ 2119297], (double)var0[(0 & 831361332) >> 1 ^ 1]);
            int var7 = 1446445296 >> 2 & 331120892 ^ 294420540;
            int var10001 = (1230907593 | 858117277) ^ 700025386 ^ 1388738295;
            float var10002 = var0[(1230907593 | 858117277) ^ 700025386 ^ 1388738295];
            if ((((1170163819 | 116183800) >> 2 & 84418267 | 2525056) ^ 1967646103) != 0) {
                  ;
            }

            double var10003;
            if (var3 > (double)var2) {
                  var10003 = (double)var2;
            } else {
                  if (!"you probably spell youre as your".equals("You're so fat whenever you go to the beach the tide comes in.")) {
                        ;
                  }

                  var10003 = var3 < (double)(-var2) ? (double)(-var2) : var3;
            }

            var0[var10001] = var10002 + (float)var10003;
            int var8 = (0 | 2112358652) >>> 2 >> 2 << 1 ^ 264044831;
            var10001 = 0 >> 3 & 2121361249 ^ 1;
            var10002 = var0[0 >> 3 & 2121361249 ^ 1];
            if (var5 > (double)var2) {
                  var10003 = (double)var2;
            } else {
                  if ((42272896 >> 1 << 2 ^ 84545792) == 0) {
                        ;
                  }

                  var10003 = var5 < (double)(-var2) ? (double)(-var2) : var5;
            }

            if ((420545553 >> 4 >>> 4 ^ 1642756) == 0) {
                  ;
            }

            var0[var10001] = var10002 + (float)var10003;
            if (((2043657558 ^ 1847408621) & 43977924 & 5699345 ^ 1179648) == 0) {
                  ;
            }

            return var0;
      }

      public static Vec3d getRandomCenter(AxisAlignedBB var0, boolean var1) {
            Vec3d var10000;
            double var10002;
            double var10003;
            double var10004;
            if (var1) {
                  var10000 = new Vec3d;
                  var10002 = var0.minX + (var0.maxX - var0.minX) * (x * 0.3D + 1.0D);
                  var10003 = var0.minY;
                  var10004 = var0.maxY;
                  if (((2018332694 << 1 & 28462815 ^ 4608745) >> 4 ^ 424904 ^ 736998) == 0) {
                        ;
                  }

                  var10000.<init>(var10002, var10003 + (var10004 - var0.minY) * (y * 0.3D + 1.0D), var0.minZ + (var0.maxZ - var0.minZ) * (z * 0.3D + 1.0D));
                  return var10000;
            } else {
                  var10000 = new Vec3d;
                  if ((74712441 << 4 >> 4 >>> 1 ^ 138740464) != 0) {
                        ;
                  }

                  var10002 = var0.minX + (var0.maxX - var0.minX) * x * 0.8D;
                  var10003 = var0.minY + (var0.maxY - var0.minY) * y * 0.8D;
                  if (((371847953 ^ 241739669) >> 3 ^ 42851944 ^ 25556216) == 0) {
                        ;
                  }

                  var10004 = var0.minZ;
                  double var10005 = var0.maxZ;
                  if ((((1098909713 ^ 674366262) >> 4 | 86142699) ^ 87266550 ^ 42531341) == 0) {
                        ;
                  }

                  var10000.<init>(var10002, var10003, var10004 + (var10005 - var0.minZ) * z * 0.8D);
                  return var10000;
            }
      }

      public static Vec3d getEyesPos() {
            if ((681558186 >>> 4 ^ 6445699 ^ 36675243 ^ 1938547821) != 0) {
                  ;
            }

            Vec3d var10000 = new Vec3d;
            double var10002 = mc.player.posX;
            double var10003 = mc.player.getEntityBoundingBox().minY;
            double var10004 = (double)mc.player.getEyeHeight();
            if ((((1452446002 ^ 1325559717) & 292288930 | 11912483) ^ 297139619) == 0) {
                  ;
            }

            var10000.<init>(var10002, var10003 + var10004, mc.player.posZ);
            return var10000;
      }

      public static float[] getRotations(EntityLivingBase var0, String var1) {
            double var2;
            double var4;
            double var6;
            if (var1 == "Head") {
                  var2 = var0.posX;
                  var4 = var0.posZ;
                  var6 = var0.posY + (double)(var0.getEyeHeight() / 2.0F);
                  return (float[])RotationUtils.getRotationFromPosition(var2, var4, var6);
            } else {
                  if ((185114912 << 4 ^ -1333128704) == 0) {
                        ;
                  }

                  if (var1 == "Chest") {
                        if (((848010551 >>> 3 | 72755730) ^ 106396598) == 0) {
                              ;
                        }

                        var2 = var0.posX;
                        var4 = var0.posZ;
                        var6 = var0.posY + (double)(var0.getEyeHeight() / 2.0F) - 0.75D;
                        return (float[])RotationUtils.getRotationFromPosition(var2, var4, var6);
                  } else if (var1 == "Dick") {
                        var2 = var0.posX;
                        var4 = var0.posZ;
                        if ((2089788523 << 3 >> 1 ^ 1529864721 ^ 1904035992) != 0) {
                              ;
                        }

                        var6 = var0.posY + (double)(var0.getEyeHeight() / 2.0F) - 1.2D;
                        return (float[])RotationUtils.getRotationFromPosition(var2, var4, var6);
                  } else if (var1 == "Legs") {
                        var2 = var0.posX;
                        var4 = var0.posZ;
                        if (((1220083063 << 1 ^ 712555722) >> 1 ^ -578510062) == 0) {
                              ;
                        }

                        double var10000 = var0.posY;
                        if (((1044744936 >>> 4 ^ 22654201 ^ 4551570) >>> 3 ^ 1104782949) != 0) {
                              ;
                        }

                        var10000 += (double)(var0.getEyeHeight() / 2.0F);
                        if (((1335727330 ^ 495543161) & 789364814 ^ 33865738) == 0) {
                              ;
                        }

                        var6 = var10000 - 1.5D;
                        float[] var8 = (float[])RotationUtils.getRotationFromPosition(var2, var4, var6);
                        if (((488998655 | 212081959 | 224524585) ^ -675031679) != 0) {
                              ;
                        }

                        return var8;
                  } else {
                        if ((((546826696 | 53498162 | 420609321) & 169109279) >> 4 << 3 ^ 84554376) == 0) {
                              ;
                        }

                        var2 = var0.posX;
                        var4 = var0.posZ;
                        if (((693714369 >>> 1 ^ 287983245) >> 2 ^ 12859618 ^ 27627257) == 0) {
                              ;
                        }

                        var6 = var0.posY + (double)(var0.getEyeHeight() / 2.0F) - 0.5D;
                        return (float[])RotationUtils.getRotationFromPosition(var2, var4, var6);
                  }
            }
      }

      public static double getRotationDifference(Entity var0) {
            if ((1600391515 >> 1 ^ 389249009 ^ 1172985350) != 0) {
                  ;
            }

            float[] var1 = (float[])RotationUtils.getTargetRotation(var0);
            if (var1 == null) {
                  return 0.0D;
            } else {
                  float var10000 = var1[(((1004386248 | 917675214) & 758271950) << 1 ^ 873671897) << 1 ^ -588355958];
                  int var10002 = (0 >>> 3 | 1935137403) ^ 1935137402;
                  if ((1100562773 >>> 2 << 4 ^ 1731110210) != 0) {
                        ;
                  }

                  return RotationUtils.getRotationDifference(var10000, var1[var10002]);
            }
      }

      public static float getTrajAngleSolutionLow(float var0, float var1, float var2) {
            if ((14688816 << 4 << 1 ^ 862260963) != 0) {
                  ;
            }

            float var3 = 0.006F;
            if (!"please take a shower".equals("please go outside")) {
                  ;
            }

            float var4 = var2 * var2 * var2 * var2 - 0.006F * (0.006F * var0 * var0 + 2.0F * var1 * var2 * var2);
            return (float)Math.toDegrees(Math.atan(((double)(var2 * var2) - Math.sqrt((double)var4)) / (double)(0.006F * var0)));
      }

      public static float getNewAngle(float var0) {
            var0 %= 360.0F;
            if (var0 >= 180.0F) {
                  var0 -= 360.0F;
            }

            if (var0 < -180.0F) {
                  var0 += 360.0F;
            }

            return var0;
      }
}
