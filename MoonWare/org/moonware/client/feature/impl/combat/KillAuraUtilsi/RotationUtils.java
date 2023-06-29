package org.moonware.client.feature.impl.combat.KillAuraUtilsi;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import org.moonware.client.utils.MWUtils;
import org.moonware.client.feature.impl.combat.AttackAura;
import org.moonware.client.helpers.math.GCDFix;

public class RotationUtils {
    /*     */   public static boolean isLookingAtEntity(boolean blockCheck, float yaw, float pitch, float xExp, float yExp, float zExp, Entity entity, double range) {
        /*  55 */     Vec3d src = Minecraft.player.getPositionEyes(1.0F);
        /*  56 */     Vec3d vectorForRotation = Entity.getVectorForRotation(pitch, yaw);
        /*  57 */     Vec3d dest = src.addVector(vectorForRotation.xCoord * range, vectorForRotation.yCoord * range, vectorForRotation.zCoord * range);
        /*  58 */     RayTraceResult rayTraceResult = Minecraft.world.rayTraceBlocks(src, dest, false, false, true);
        /*  59 */     if (rayTraceResult == null) {
            /*  60 */       return false;
            /*     */     }
        /*  62 */     if (blockCheck && rayTraceResult.typeOfHit == RayTraceResult.Type.BLOCK) {
            /*  63 */       return false;
            /*     */     }
        /*  65 */     return (entity.getEntityBoundingBox().expand(xExp, yExp, zExp).calculateIntercept(src, dest) != null);
        /*     */   }
    public static boolean isLookingAtEntity(float yaw, float pitch, float xExp, float yExp, float zExp, Entity entity, double range) {
        /*  15 */     Vec3d src = Minecraft.player.getPositionEyes(1.0F);
        /*  16 */     Vec3d vectorForRotation = Entity.getVectorForRotation(pitch, yaw);
        /*  17 */     Vec3d dest = src.addVector(vectorForRotation.xCoord * range, vectorForRotation.yCoord * range, vectorForRotation.zCoord * range);
        /*  18 */     RayTraceResult rayTraceResult = Minecraft.world.rayTraceBlocks(src, dest, false, false, true);
        /*  19 */     if (rayTraceResult == null) {
            /*  20 */       return false;
            /*     */     }
        /*  22 */     return (entity.getEntityBoundingBox().expand(xExp, yExp, zExp).calculateIntercept(src, dest) != null);
        /*     */   }

    /*     */   public static float[] getRotations(double x, double y, double z) {
        /* 125 */     double n = x + 0.5D;
        /* 126 */     double diffX = n - Minecraft.player.posX;
        /* 127 */     double n2 = (y + 0.5D) / 2.0D;
        /* 128 */     double posY = Minecraft.player.posY;
        /* 129 */     double diffY = n2 - posY + Minecraft.player.getEyeHeight();
        /* 130 */     double n3 = z + 0.5D;
        /* 131 */     double diffZ = n3 - Minecraft.player.posZ;
        /* 132 */     double dist = MathHelper.sqrt(diffX * diffX + diffZ * diffZ);
        /* 133 */     float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0D / Math.PI) - 90.0F;
        /* 134 */     float pitch = (float)-(Math.atan2(diffY, dist) * 180.0D / Math.PI);
        /* 135 */     return new float[] { yaw, pitch };
        /*     */   }
    /*     */
    /*     */   public static float[] getRotations(Entity entityIn) {
        /* 139 */     double d1, diffX = entityIn.posX - Minecraft.player.posX;
        /* 140 */     double diffZ = entityIn.posZ - Minecraft.player.posZ;
        /*     */
        /*     */
        /* 143 */     if (entityIn instanceof net.minecraft.entity.EntityLivingBase) {
            /* 144 */       d1 = entityIn.posY + entityIn.getEyeHeight() - Minecraft.player.posY + Minecraft.player.getEyeHeight() - 0.20000000298023224D;
            /*     */     } else {
            /* 146 */       d1 = ((entityIn.getEntityBoundingBox()).minY + (entityIn.getEntityBoundingBox()).maxY) / 2.0D - Minecraft.player.posY + Minecraft.player.getEyeHeight();
            /*     */     }
        /* 148 */     if (!Minecraft.player.canEntityBeSeen(entityIn)) {
            /* 149 */       d1 = entityIn.posY + entityIn.height - Minecraft.player.posY + Minecraft.player.getEyeHeight();
            /*     */     }
        /* 151 */     double diffXZ = MathHelper.sqrt(diffX * diffX + diffZ * diffZ);
        /*     */
        /* 153 */     float yaw = (float)(Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0D + GCDFix.getFixedRotation(MWUtils.randomFloat(-1.75F, 1.75F)));
        /* 154 */     float pitch = (float)(Math.toDegrees(-Math.atan2(d1, diffXZ)) + GCDFix.getFixedRotation(MWUtils.randomFloat(-1.8F, 1.75F)));
        /*     */
        /* 156 */     yaw = Minecraft.player.rotationYaw + GCDFix.getFixedRotation(MathHelper.wrapDegrees(yaw - Minecraft.player.rotationYaw));
        /* 157 */     pitch = Minecraft.player.rotationPitch + GCDFix.getFixedRotation(MathHelper.wrapDegrees(pitch - Minecraft.player.rotationPitch));
        /* 158 */     pitch = MathHelper.clamp(pitch, -90.0F, 90.0F);
        /*     */
        /* 160 */     return new float[] { yaw, pitch };
        /*     */   }
    /*     */
    /*     */   public static float[] getFakeRotations(Entity entityIn) {
        /* 164 */     double diffY, diffX = entityIn.posX - Minecraft.player.posX;
        /* 165 */     double diffZ = entityIn.posZ - Minecraft.player.posZ;
        /*     */
        /*     */
        /* 168 */     if (entityIn instanceof net.minecraft.entity.EntityLivingBase) {
            /* 169 */       diffY = entityIn.posY + entityIn.getEyeHeight() - Minecraft.player.posY + Minecraft.player.getEyeHeight() - 0.20000000298023224D;
            /*     */     } else {
            /* 171 */       diffY = ((entityIn.getEntityBoundingBox()).minY + (entityIn.getEntityBoundingBox()).maxY) / 2.0D - Minecraft.player.posY + Minecraft.player.getEyeHeight();
            /*     */     }
        /* 173 */     double diffXZ = MathHelper.sqrt(diffX * diffX + diffZ * diffZ);
        /*     */
        /* 175 */     float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0D / Math.PI - 90.0D);
        /* 176 */     float pitch = (float)-(Math.atan2(diffY, diffXZ) * 180.0D / Math.PI);
        /*     */
        /* 178 */     yaw = Minecraft.player.rotationYaw + GCDFix.getFixedRotation(MathHelper.wrapDegrees(yaw - Minecraft.player.rotationYaw));
        /* 179 */     pitch = Minecraft.player.rotationPitch + GCDFix.getFixedRotation(MathHelper.wrapDegrees(pitch - Minecraft.player.rotationPitch));
        /* 180 */     pitch = MathHelper.clamp(pitch, -90.0F, 90.0F);
        /*     */
        /* 182 */     return new float[] { yaw, pitch };
        /*     */   }
    /*     */
    /*     */   public static float[] getCustomRotations(Entity entityIn) {
        /* 186 */     double d1, diffX = entityIn.posX - Minecraft.player.posX;
        /* 187 */     double diffZ = entityIn.posZ - Minecraft.player.posZ;
        /*     */
        /*     */
        /* 190 */     if (entityIn instanceof net.minecraft.entity.EntityLivingBase) {
            /* 191 */       if (!AttackAura.staticPitch.getBoolValue()) {
                /* 192 */         d1 = entityIn.posY + entityIn.getEyeHeight() - Minecraft.player.posY + Minecraft.player.getEyeHeight() - AttackAura.pitchHead.getNumberValue();
                /*     */       } else {
                /* 194 */         d1 = (entityIn.getEyeHeight() - Minecraft.player.getEyeHeight() - AttackAura.pitchHead.getNumberValue());
                /*     */       }
            /*     */     } else {
            /* 197 */       d1 = ((entityIn.getEntityBoundingBox()).minY + (entityIn.getEntityBoundingBox()).maxY) / 2.0D - Minecraft.player.posY + Minecraft.player.getEyeHeight();
            /*     */     }
        /* 199 */     if (!Minecraft.player.canEntityBeSeen(entityIn)) {
            /* 200 */       d1 = entityIn.posY + entityIn.height - Minecraft.player.posY + Minecraft.player.getEyeHeight();
            /*     */     }
        /*     */
        /* 203 */     double diffXZ = MathHelper.sqrt(diffX * diffX + diffZ * diffZ);
        /*     */
        /* 205 */     float yaw = (float)(Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0D) + GCDFix.getFixedRotation(MWUtils.randomFloat(AttackAura.yawrandom.getNumberValue(), -AttackAura.yawrandom.getNumberValue()));
        /* 206 */     float pitch = (float)Math.toDegrees(-Math.atan2(d1, diffXZ)) + GCDFix.getFixedRotation(MWUtils.randomFloat(AttackAura.pitchRandom.getNumberValue(), -AttackAura.pitchRandom.getNumberValue()));
        /*     */
        /* 208 */     yaw = Minecraft.player.rotationYaw + GCDFix.getFixedRotation(MathHelper.wrapDegrees(yaw - Minecraft.player.rotationYaw));
        /* 209 */     pitch = Minecraft.player.rotationPitch + GCDFix.getFixedRotation(MathHelper.wrapDegrees(pitch - Minecraft.player.rotationPitch));
        /* 210 */     pitch = MathHelper.clamp(pitch, -90.0F, 90.0F);
        /*     */
        /* 212 */     return new float[] { yaw, pitch };
        /*     */   }
}
