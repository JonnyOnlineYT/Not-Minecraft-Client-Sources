package org.moonware.client.feature.impl.combat.KillAuraUtilsi;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import org.moonware.client.feature.impl.combat.KillAura;
import org.moonware.client.utils.MWUtils;
import org.moonware.client.helpers.Helper;
import org.moonware.client.helpers.math.GCDFix;
import org.moonware.client.helpers.render.AnimationHelper;

public class RotationHelper implements Helper {
    public RotationHelper() {
    }

    public static boolean isLookingAtEntity(float yaw, float pitch, float xExp, float yExp, float zExp, Entity entity, double range) {
        Vec3d src = Minecraft.player.getPositionEyes(1.0F);
        Vec3d vectorForRotation = Entity.getVectorForRotation(pitch, yaw);
        Vec3d dest = src.addVector(vectorForRotation.xCoord * range, vectorForRotation.yCoord * range, vectorForRotation.zCoord * range);
        RayTraceResult rayTraceResult = Minecraft.world.rayTraceBlocks(src, dest, false, false, true);
        if (rayTraceResult == null) {
            return false;
        } else {
            return entity.getEntityBoundingBox().expand(xExp, yExp, zExp).calculateIntercept(src, dest) != null;
        }
    }

    public static double getDistanceOfEntityToBlock(Entity entity, BlockPos pos) {
        return getDistance(entity.posX, entity.posY, entity.posZ, pos.getX(), pos.getY(), pos.getZ());
    }

    public static double getDistance(double x, double y, double z, double x1, double y1, double z1) {
        double posX = x - x1;
        double posY = y - y1;
        double posZ = z - z1;
        return MathHelper.sqrt(posX * posX + posY * posY + posZ * posZ);
    }

    public static Vec3d getEyesPos() {
        return new Vec3d(Minecraft.player.posX, Minecraft.player.getEntityBoundingBox().minY + (double) Minecraft.player.getEyeHeight(), Minecraft.player.posZ);
    }

    public static float[] getRotationVector(Vec3d vec) {
        Vec3d eyesPos = getEyesPos();
        double diffX = vec.xCoord - eyesPos.xCoord;
        double diffY = vec.yCoord - (Minecraft.player.posY + (double) Minecraft.player.getEyeHeight() + 0.5D);
        double diffZ = vec.zCoord - eyesPos.zCoord;
        double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
        float yaw = (float)(Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0D) + MWUtils.randomFloat(-2.0F, 2.0F);
        float pitch = (float)(-Math.toDegrees(Math.atan2(diffY, diffXZ))) + MWUtils.randomFloat(-2.0F, 2.0F);
        yaw = Minecraft.player.rotationYaw + GCDFix.getFixedRotation(MathHelper.wrapDegrees(yaw - Minecraft.player.rotationYaw));
        pitch = Minecraft.player.rotationPitch + GCDFix.getFixedRotation(MathHelper.wrapDegrees(pitch - Minecraft.player.rotationPitch));
        pitch = MathHelper.clamp(pitch, -90.0F, 90.0F);
        return new float[]{yaw, pitch};
    }

    public static boolean isLookingAtEntity(boolean blockCheck, float yaw, float pitch, float xExp, float yExp, float zExp, Entity entity, double range) {
        Vec3d src = Minecraft.player.getPositionEyes(1.0F);
        Vec3d vectorForRotation = Entity.getVectorForRotation(pitch, yaw);
        Vec3d dest = src.addVector(vectorForRotation.xCoord * range, vectorForRotation.yCoord * range, vectorForRotation.zCoord * range);
        RayTraceResult rayTraceResult = Minecraft.world.rayTraceBlocks(src, dest, false, false, true);
        if (rayTraceResult == null) {
            return false;
        } else if (blockCheck && rayTraceResult.typeOfHit == RayTraceResult.Type.BLOCK) {
            return false;
        } else {
            return entity.getEntityBoundingBox().expand(xExp, yExp, zExp).calculateIntercept(src, dest) != null;
        }
    }

    public static double getDistance(double x1, double z1, double x2, double z2) {
        double deltaX = x1 - x2;
        double deltaZ = z1 - z2;
        return Math.hypot(deltaX, deltaZ);
    }

    public static float getAngle(Entity entity) {
        double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double) Minecraft.getMinecraft().getRenderPartialTicks() - RenderManager.renderPosX;
        double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)Minecraft.getMinecraft().getRenderPartialTicks() - RenderManager.renderPosZ;
        float yaw = (float)(-Math.toDegrees(Math.atan2(x, z)));
        return (float)((double)yaw - AnimationHelper.Interpolate(Minecraft.player.rotationYaw, Minecraft.player.prevRotationYaw, 1.0D));
    }

    public static float[] getTargetRotations(Entity ent) {
        double x = ent.posX;
        double z = ent.posZ;
        double y = ent.posY + (double)(ent.getEyeHeight() / 2.0F);
        return getRotationFromPosition(x, z, y);
    }

    public static float[] getRotationFromPosition(double x, double z, double y) {
        double xDiff = x - Minecraft.player.posX;
        double zDiff = z - Minecraft.player.posZ;
        double yDiff = y - Minecraft.player.posY - 1.7D;
        double dist = MathHelper.sqrt(xDiff * xDiff + zDiff * zDiff);
        float yaw = (float)(Math.atan2(zDiff, xDiff) * 180.0D / 3.141592653589793D) - 90.0F;
        float pitch = (float)(-(Math.atan2(yDiff, dist) * 180.0D / 3.141592653589793D));
        return new float[]{yaw, pitch};
    }

    public static boolean isAimAtMe(Entity entity, float breakRadius) {
        float entityYaw = MathHelper.wrapDegrees(entity.rotationYaw);
        return Math.abs(MathHelper.wrapDegrees(getYawToEntity(entity, Minecraft.player) - entityYaw)) <= breakRadius;
    }

    public static boolean posCheck(Entity entity) {
        return checkPosition(Minecraft.player.posY, entity.posY - 1.5D, entity.posY + 1.5D);
    }

    public static boolean checkPosition(double pos1, double pos2, double pos3) {
        return pos1 <= pos3 && pos1 >= pos2;
    }

    public static float getYawToEntity(Entity mainEntity, Entity targetEntity) {
        double pX = mainEntity.posX;
        double pZ = mainEntity.posZ;
        double eX = targetEntity.posX;
        double eZ = targetEntity.posZ;
        double dX = pX - eX;
        double dZ = pZ - eZ;
        double yaw = Math.toDegrees(Math.atan2(dZ, dX)) + 90.0D;
        return (float)yaw;
    }

    public static float[] getRotations(double x, double y, double z) {
        double n = x + 0.5D;
        double diffX = n - Minecraft.player.posX;
        double n2 = (y + 0.5D) / 2.0D;
        double posY = Minecraft.player.posY;
        double diffY = n2 - (posY + (double) Minecraft.player.getEyeHeight());
        double n3 = z + 0.5D;
        double diffZ = n3 - Minecraft.player.posZ;
        double dist = MathHelper.sqrt(diffX * diffX + diffZ * diffZ);
        float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0D / 3.141592653589793D) - 90.0F;
        float pitch = (float)(-(Math.atan2(diffY, dist) * 180.0D / 3.141592653589793D));
        return new float[]{yaw, pitch};
    }

    public static float[] getRotations(Entity entityIn) {
        double diffX = entityIn.posX - Minecraft.player.posX;
        double diffZ = entityIn.posZ - Minecraft.player.posZ;
        double diffY;
        if (entityIn instanceof EntityLivingBase) {
            diffY = entityIn.posY + (double)entityIn.getEyeHeight() - (Minecraft.player.posY + (double) Minecraft.player.getEyeHeight()) - 0.20000000298023224D;
        } else {
            diffY = (entityIn.getEntityBoundingBox().minY + entityIn.getEntityBoundingBox().maxY) / 2.00000000002342346152471135D - (Minecraft.player.posY + (double) Minecraft.player.getEyeHeight());
        }

        if (!Minecraft.player.canEntityBeSeen(entityIn)) {
            diffY = entityIn.posY + entityIn.getEyeHeight () + (double)entityIn.height - (Minecraft.player.posY + (double) Minecraft.player.getEyeHeight());
        }

        double diffXZ = MathHelper.sqrt(diffX * diffX + diffZ * diffZ);
        float yaw = (float)(Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0D + (double)GCDFix.getFixedRotation(MWUtils.randomFloat(-1.75F, 1.75F)));
        float pitch = (float)(Math.toDegrees(-Math.atan2(diffY, diffXZ)) + (double)GCDFix.getFixedRotation(MWUtils.randomFloat(-1.8F, 1.75F)));
        yaw = Minecraft.player.rotationYaw + GCDFix.getFixedRotation(MathHelper.wrapDegrees(yaw - Minecraft.player.rotationYaw));
        pitch = Minecraft.player.rotationPitch + GCDFix.getFixedRotation(MathHelper.wrapDegrees(pitch - Minecraft.player.rotationPitch));
        pitch = MathHelper.clamp(pitch, -90.0F, 90.0F);
        return new float[]{yaw, pitch};
    }

    public static float[] getFakeRotations(Entity entityIn) {
        double diffX = entityIn.posX - Minecraft.player.posX;
        double diffZ = entityIn.posZ - Minecraft.player.posZ;
        double diffY;
        if (entityIn instanceof EntityLivingBase) {
            diffY = entityIn.posY + (double)entityIn.getEyeHeight() - (Minecraft.player.posY + (double) Minecraft.player.getEyeHeight()) - 0.20000000298023224D;
        } else {
            diffY = (entityIn.getEntityBoundingBox().minY + entityIn.getEntityBoundingBox().maxY) / 2.0D - (Minecraft.player.posY + (double) Minecraft.player.getEyeHeight());
        }

        double diffXZ = MathHelper.sqrt(diffX * diffX + diffZ * diffZ);
        float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0D / 3.141592653589793D - 90.0D);
        float pitch = (float)(-(Math.atan2(diffY, diffXZ) * 180.0D / 3.141592653589793D));
        yaw = Minecraft.player.rotationYaw + GCDFix.getFixedRotation(MathHelper.wrapDegrees(yaw - Minecraft.player.rotationYaw));
        pitch = Minecraft.player.rotationPitch + GCDFix.getFixedRotation(MathHelper.wrapDegrees(pitch - Minecraft.player.rotationPitch));
        pitch = MathHelper.clamp(pitch, -90.0F, 90.0F);
        return new float[]{yaw, pitch};
    }

    public static float[] getCustomRotations(Entity entityIn) {
        double diffX = entityIn.posX - Minecraft.player.posX;
        double diffZ = entityIn.posZ - Minecraft.player.posZ;
        double diffY;
        if (entityIn instanceof EntityLivingBase) {
            if (!KillAura.staticPitch.getBoolValue()) {
                diffY = entityIn.posY + (double)entityIn.getEyeHeight() - (Minecraft.player.posY + (double) Minecraft.player.getEyeHeight()) - (double) KillAura.pitchHead.getNumberValue();
            } else {
                diffY = entityIn.getEyeHeight() - Minecraft.player.getEyeHeight() - KillAura.pitchHead.getNumberValue();
            }
        } else {
            diffY = (entityIn.getEntityBoundingBox().minY + entityIn.getEntityBoundingBox().maxY) / 2.0D - (Minecraft.player.posY + (double) Minecraft.player.getEyeHeight());
        }

        if (!Minecraft.player.canEntityBeSeen(entityIn)) {
            diffY = entityIn.posY + (double)entityIn.height - (Minecraft.player.posY + (double) Minecraft.player.getEyeHeight());
        }

        double diffXZ = MathHelper.sqrt(diffX * diffX + diffZ * diffZ);
        float yaw = (float)(Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0D) + GCDFix.getFixedRotation(MWUtils.randomFloat(KillAura.yawrandom.getNumberValue(), -KillAura.yawrandom.getNumberValue()));
        float pitch = (float)Math.toDegrees(-Math.atan2(diffY, diffXZ)) + GCDFix.getFixedRotation(MWUtils.randomFloat(KillAura.pitchRandom.getNumberValue(), -KillAura.pitchRandom.getNumberValue()));
        yaw = Minecraft.player.rotationYaw + GCDFix.getFixedRotation(MathHelper.wrapDegrees(yaw - Minecraft.player.rotationYaw));
        pitch = Minecraft.player.rotationPitch + GCDFix.getFixedRotation(MathHelper.wrapDegrees(pitch - Minecraft.player.rotationPitch));
        pitch = MathHelper.clamp(pitch, -90.0F, 90.0F);
        return new float[]{yaw, pitch};
    }
}
