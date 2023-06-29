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
            if (!"Some babies were dropped on their heads but you were clearly thrown at a wall.".equals("your mom your dad the one you never had")) {
                  ;
            }

            Vec3d var10000 = var0.getPositionVector();
            Vec3d var10001 = new Vec3d;
            if (((1302080466 >> 1 & 510142642 | 12738479) ^ 113926063) == 0) {
                  ;
            }

            var10001.<init>(var0.lastTickPosX, var0.lastTickPosY, var0.lastTickPosZ);
            return MathUtil.interpolateVec3d(var10000, var10001, var1);
      }

      public static boolean isAnimal(Entity var0) {
            return (boolean)(!(var0 instanceof EntityAgeable) && !(var0 instanceof EntityAmbientCreature) && !(var0 instanceof EntityWaterMob) && !(var0 instanceof EntityGolem) ? ((2019178939 | 1378122840) >>> 4 | 41533204) ^ 134217695 : 0 << 4 << 2 & 1747646182 ^ 1);
      }
}
