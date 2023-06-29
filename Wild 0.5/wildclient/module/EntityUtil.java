//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "Deb"!

// 
// Decompiled by Procyon v0.5.36
// 

package black.nigger.wildclient.module;

import net.minecraft.util.math.Vec3d;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.Entity;

public class EntityUtil
{
    public static boolean isAnimal(final Entity entity) {
        return entity instanceof EntityAgeable || entity instanceof EntityAmbientCreature || entity instanceof EntityWaterMob || entity instanceof EntityGolem;
    }
    
    public static Vec3d getInterpolatedPos(final Entity entity, final float partialTicks) {
        return MathUtil.interpolateVec3d(entity.getPositionVector(), new Vec3d(entity.lastTickPosX, entity.lastTickPosY, entity.lastTickPosZ), partialTicks);
    }
}
