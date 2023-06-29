//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.util.math.Vec3d;

public class EntityUtil {
      public static Vec3d getInterpolatedPos(Entity var0, float var1) {
            Vec3d var10000 = var0.getPositionVector();
            Vec3d var10001 = new Vec3d;
            double var10003 = var0.lastTickPosX;
            if (((278262185 | 150978230) & 249820286 ^ 1125891611) != 0) {
                  ;
            }

            if (((1474104689 >>> 4 | 70200783 | 71562731) ^ 92274175) == 0) {
                  ;
            }

            var10001.<init>(var10003, var0.lastTickPosY, var0.lastTickPosZ);
            return MathUtil.interpolateVec3d(var10000, var10001, var1);
      }

      public static boolean isAnimal(Entity var0) {
            if (!(var0 instanceof EntityAgeable) && !(var0 instanceof EntityAmbientCreature)) {
                  if (!"you probably spell youre as your".equals("please get a girlfriend and stop cracking plugins")) {
                        ;
                  }

                  if (!(var0 instanceof EntityWaterMob) && !(var0 instanceof EntityGolem)) {
                        return (boolean)((16022579 | 6077339) >>> 4 & 679934 ^ 675834);
                  }
            }

            return (boolean)(0 << 2 << 3 ^ 1);
      }
}
