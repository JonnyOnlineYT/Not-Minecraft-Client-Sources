package Celestial.utils.math;

import Celestial.event.EventTarget;
import Celestial.event.events.impl.packet.EventSendPacket;
import Celestial.event.events.impl.player.EventPlayerState;
import Celestial.utils.Helper;
import Celestial.module.impl.Combat.KillAura;
import Celestial.utils.movement.MovementUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

public class RotationHelper implements Helper {
    public static boolean isLookingAtEntity(float yaw, float pitch, float xExp, float yExp, float zExp, Entity entity, double range) {
        Vec3d src = mc.player.getPositionEyes(1.0F);
        Vec3d vectorForRotation = Entity.getVectorForRotation(pitch, yaw);
        Vec3d dest = src.add(vectorForRotation.x * range, vectorForRotation.y * range, vectorForRotation.z * range);
        RayTraceResult rayTraceResult = mc.world.rayTraceBlocks(src, dest, false, false, true);
        if (rayTraceResult == null) {
            return false;
        }
        return (entity.getEntityBoundingBox().expand(xExp, yExp, zExp).calculateIntercept(src, dest) != null);
    }

    public static double getDistanceOfEntityToBlock(Entity entity, BlockPos pos) {
        return getDistance(entity.posX, entity.posY, entity.posZ, pos.getX(), pos.getY(), pos.getZ());
    }

    public static float[] getRotationsCustom(Entity entityIn, float speed) {
        String mode = KillAura.rotationMode.getOptions();

        float yawCurrent = mode.equalsIgnoreCase("Matrix") || mode.equalsIgnoreCase("Sunrise") ? Rotation.packetYaw : mc.player.rotationYaw;
        float pitchCurrent = mode.equalsIgnoreCase("Matrix") || mode.equalsIgnoreCase("Sunrise") ? Rotation.packetPitch : mc.player.rotationPitch;


        float yaw = updateRotation(yawCurrent, getRotationsA(entityIn)[0], KillAura.rotationMode.currentMode.equals("Matrix") ? Float.MAX_VALUE : (KillAura.rotationMode.currentMode.equals("Sunrise") ? 25.0f : speed));
        float pitch = updateRotation(pitchCurrent, getRotationsA(entityIn)[1], KillAura.rotationMode.currentMode.equals("Matrix") ? Float.MAX_VALUE : (KillAura.rotationMode.currentMode.equals("Sunrise") ? 25.0f : speed));
        return new float[]{yaw, pitch};
    }

    public static float[] getRotationsA(Entity entityIn) {
        double diffX = entityIn.posX + (entityIn.posX - entityIn.prevPosX) - mc.player.posX - mc.player.motionX;
        double diffZ = entityIn.posZ + (entityIn.posZ - entityIn.prevPosZ) - mc.player.posZ - mc.player.motionZ;
        double diffY = entityIn instanceof EntityLivingBase ? entityIn.posY + (double) entityIn.getEyeHeight() - (mc.player.posY + (double) mc.player.getEyeHeight()) - (KillAura.walls.getCurrentValue() && !((EntityLivingBase) entityIn).canEntityBeSeen(mc.player) ? -0.38 : (KillAura.rotationMode.currentMode.equals("Sunrise") && posCheck(entityIn) ? 1.4 : (KillAura.rotationMode.currentMode.equals("Matrix") ? 1 : mc.player.getDistanceToEntity(entityIn) >= 3.8 ? 0.6f : 0.25f))) : (entityIn.getEntityBoundingBox().minY + entityIn.getEntityBoundingBox().maxY) / 2.0 - (mc.player.posY + (double) mc.player.getEyeHeight());
        double diffXZ = MathHelper.sqrt(diffX * diffX + diffZ * diffZ);

        if (KillAura.rotationMode.currentMode.equals("Sunrise") && posCheck(entityIn)) {
            diffY /= diffXZ;
        }

        float yaw = (float) ((Math.toDegrees(Math.atan2(diffZ, diffX)) - 90)  + GCDFix.getFixedRotation((float) (Math.sin(System.currentTimeMillis() / 30) * 2)));
        float pitch = (float) (-(Math.atan2(diffY, diffXZ) * 180.0 / (KillAura.walls.getCurrentValue() && !((EntityLivingBase) entityIn).canEntityBeSeen(mc.player) ? 3.1 : Math.PI + (double) (KillAura.rotationMode.currentMode.equals("Sunrise") && posCheck(entityIn) ? 10 : 0)))) + GCDFix.getFixedRotation((float) (Math.cos(System.currentTimeMillis() / 30) * 2));
        if (KillAura.rotationMode.currentMode.equals("Sunrise") && posCheck(entityIn)) {
            pitch += 26.0f;
        }

        yaw = mc.player.rotationYaw + GCDFix.getFixedRotation(MathHelper.wrapDegrees(yaw - mc.player.rotationYaw));
        pitch = mc.player.rotationPitch + GCDFix.getFixedRotation(MathHelper.wrapDegrees(pitch - mc.player.rotationPitch));
        float minValue = -90.0f;
        float maxValue = 90.0f;
        pitch = MathHelper.clamp(pitch, minValue, maxValue);
        return new float[]{yaw, pitch};
    }

    public static float updateRotation(float current, float newValue, float speed) {
        float f = MathHelper.wrapDegrees(newValue - current);
        if (f > speed) {
            f = speed;
        }
        if (f < -speed) {
            f = -speed;
        }
        return current + f;
    }

    public static double getDistance(double x, double y, double z, double x1, double y1, double z1) {
        double posX = x - x1;
        double posY = y - y1;
        double posZ = z - z1;
        return MathHelper.sqrt(posX * posX + posY * posY + posZ * posZ);
    }

    public static Vec3d getEyesPos() {
        return new Vec3d(mc.player.posX, mc.player.getEntityBoundingBox().minY + (double) mc.player.getEyeHeight(), mc.player.posZ);
    }
    public static float[] getRotationVector(Vec3d vec) {
        Vec3d eyesPos = getEyesPos();
        double diffX = vec.x - eyesPos.x;
        double diffY = vec.y - (mc.player.posY + (double) mc.player.getEyeHeight() + 0.5);
        double diffZ = vec.z - eyesPos.z;
        double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
        float yaw = (float) (Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0) + MathematicHelper.randomizeFloat(-2.0f, 2.0f);
        float pitch = (float) (-Math.toDegrees(Math.atan2(diffY, diffXZ))) + MathematicHelper.randomizeFloat(-2.0f, 2.0f);
        yaw = mc.player.rotationYaw + GCDFix.getFixedRotation(MathHelper.wrapDegrees(yaw - mc.player.rotationYaw));
        pitch = mc.player.rotationPitch + GCDFix.getFixedRotation(MathHelper.wrapDegrees(pitch - mc.player.rotationPitch));
        pitch = MathHelper.clamp(pitch, -90.0f, 90.0f);
        return new float[]{yaw, pitch};
    }
    public static float[] getMagicRotations(Entity entityIn, boolean random, float maxSpeed, float minSpeed, float yawRandom, float pitchRandom) {
        double diffX = entityIn.posX + (entityIn.posX - entityIn.prevPosX) * KillAura.rotPredict.getNumberValue() - mc.player.posX - mc.player.motionX * KillAura.rotPredict.getNumberValue();
        double diffZ = entityIn.posZ + (entityIn.posZ - entityIn.prevPosZ) * KillAura.rotPredict.getNumberValue() - mc.player.posZ - mc.player.motionZ * KillAura.rotPredict.getNumberValue();
        double diffY;

        if (entityIn instanceof EntityLivingBase) {
            diffY = entityIn.posY + entityIn.getEyeHeight() - (mc.player.posY + mc.player.getEyeHeight()) - 0.6 - (KillAura.walls.getCurrentValue() && !((EntityLivingBase) entityIn).canEntityBeSeen(mc.player) ? -0.38 : 0);
        } else {
            diffY = (entityIn.getEntityBoundingBox().minY + entityIn.getEntityBoundingBox().maxY) / 2.0 - (mc.player.posY + mc.player.getEyeHeight());
        }
        double diffXZ = MathHelper.sqrt(diffX * diffX + diffZ * diffZ);
        diffY /= diffXZ;
        float randomYaw = 0;
        if (random) {
            randomYaw = MathematicHelper.randomizeFloat(yawRandom, -yawRandom);
        }
        float randomPitch = 0;
        if (random) {
            randomPitch = MathematicHelper.randomizeFloat(pitchRandom, -pitchRandom);
        }

        float yaw = (float) (((Math.atan2(diffZ, diffX) * 180 / Math.PI) - 90)) + randomYaw;
        float pitch = (float) (-(Math.atan2(diffY, diffXZ) * 180 / Math.PI)) + randomPitch;


        yaw = (mc.player.rotationYaw + GCDFix.getFixedRotation(MathHelper.wrapDegrees(yaw - mc.player.rotationYaw)));
        pitch = mc.player.rotationPitch + GCDFix.getFixedRotation(MathHelper.wrapDegrees(pitch - mc.player.rotationPitch));
        pitch = MathHelper.clamp(pitch, -90F, 90F);
        yaw = RotationHelper.updateRotation(mc.player.rotationYaw, yaw, MathematicHelper.randomizeFloat(minSpeed, maxSpeed));
        pitch = RotationHelper.updateRotation(mc.player.rotationPitch, pitch, MathematicHelper.randomizeFloat(minSpeed, maxSpeed));

        return new float[]{yaw, pitch};
    }
    public static boolean isLookingAtEntity(boolean blockCheck, float yaw, float pitch, float xExp, float yExp, float zExp, Entity entity, double range) {
        Vec3d src = mc.player.getPositionEyes(1.0f);
        Vec3d vectorForRotation = Entity.getVectorForRotation(pitch, yaw);
        Vec3d dest = src.add(vectorForRotation.x * range, vectorForRotation.y * range, vectorForRotation.z * range);
        RayTraceResult rayTraceResult = mc.world.rayTraceBlocks(src, dest, false, false, true);
        if (rayTraceResult == null) {
            return false;
        }
        if (blockCheck && rayTraceResult.typeOfHit == RayTraceResult.Type.BLOCK) {
            return false;
        }
        return entity.getEntityBoundingBox().expand(xExp, yExp, zExp).calculateIntercept(src, dest) != null;
    }


    public static double getDistance(double x1, double z1, double x2, double z2) {
        double deltaX = x1 - x2;
        double deltaZ = z1 - z2;
        return Math.hypot(deltaX, deltaZ);
    }

    public static double getDistance2(double x, double z, double x1, double z1) {
        double d0 = x - x1;
        double d1 = z - x1;
        return MathHelper.sqrt(d0 * d0 + d1 * d1);
    }

    public static float getAngle(Entity entity) {
        double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * mc.getRenderPartialTicks() - mc.getRenderManager().renderPosX;
        double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * mc.getRenderPartialTicks() - mc.getRenderManager().renderPosZ;
        float yaw = (float) -Math.toDegrees(Math.atan2(x, z));
        return (float) (yaw - AnimationHelper.Interpolate(mc.player.rotationYaw, mc.player.prevRotationYaw, 1.0D));
    }
    public static float getAngle(float x, float z) {
        float yaw = (float) -Math.toDegrees(Math.atan2(x, z));
        return (float) (yaw - AnimationHelper.Interpolate(mc.player.rotationYaw, mc.player.prevRotationYaw, 1.0D));
    }
    public static float[] getTargetRotations(Entity ent) {
        double x = ent.posX;
        double z = ent.posZ;
        double y = ent.posY + ent.getEyeHeight() / 2.0F;
        return getRotationFromPosition(x, z, y);
    }

    public static float[] getRotationFromPosition(double x, double z, double y) {
        double xDiff = x - mc.player.posX;
        double zDiff = z - mc.player.posZ;
        double yDiff = y - mc.player.posY - 1.7;

        double dist = MathHelper.sqrt(xDiff * xDiff + zDiff * zDiff);
        float yaw = (float) (Math.atan2(zDiff, xDiff) * 180.0D / 3.141592653589793D) - 90.0F;
        float pitch = (float) -(Math.atan2(yDiff, dist) * 180.0D / 3.141592653589793D);
        return new float[]{yaw, pitch};
    }

    public static boolean isAimAtMe(Entity entity, float breakRadius) {
        float entityYaw = MathHelper.wrapDegrees(entity.rotationYaw);
        return Math.abs(MathHelper.wrapDegrees(getYawToEntity(entity, mc.player) - entityYaw)) <= breakRadius;
    }

    public static boolean posCheck(Entity entity) {
        return checkPosition(mc.player.posY, entity.posY - 1.5, entity.posY + 1.5);
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
        double yaw = Math.toDegrees(Math.atan2(dZ, dX)) + 90.0;
        return (float) yaw;
    }

    public static float[] getRotations(final double x, final double y, final double z) {
        final double n = x + 0.5;
        final double diffX = n - mc.player.posX;
        final double n2 = (y + 0.5) / 2.0;
        final double posY = mc.player.posY;
        final double diffY = n2 - (posY + mc.player.getEyeHeight());
        final double n3 = z + 0.5;
        final double diffZ = n3 - mc.player.posZ;
        final double dist = MathHelper.sqrt(diffX * diffX + diffZ * diffZ);
        final float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0 / 3.141592653589793) - 90.0f;
        final float pitch = (float) (-(Math.atan2(diffY, dist) * 180.0 / 3.141592653589793));
        return new float[]{yaw, pitch};
    }

    public static float[] getRotations(Entity entityIn) {
        double diffX = entityIn.posX - mc.player.posX;
        double diffZ = entityIn.posZ - mc.player.posZ;
        double diffY;

        if (entityIn instanceof EntityLivingBase) {
            diffY = entityIn.posY + (double) entityIn.getEyeHeight() - (mc.player.posY + (double) mc.player.getEyeHeight()) - (KillAura.rotationMode.currentMode.equals("Sunrise") && posCheck(entityIn) ? 1.0 : KillAura.rotationMode.currentMode.equals("Matrix") ? 0.8 : 0.25D);
        } else {
            diffY = (entityIn.getEntityBoundingBox().minY + entityIn.getEntityBoundingBox().maxY) / 2 - (mc.player.posY + mc.player.getEyeHeight());
        }
        if (!mc.player.canEntityBeSeen(entityIn)) {
            diffY = entityIn.posY + entityIn.height - (mc.player.posY + mc.player.getEyeHeight());
        }
        final double diffXZ = MathHelper.sqrt(diffX * diffX + diffZ * diffZ);

        float yaw = (float) ((Math.toDegrees(Math.atan2(diffZ, diffX)) - 90) + GCDFix.getFixedRotation((float) (Math.sin(System.nanoTime() / 100000000) * 1.5)));
        float pitch = (float) (-(Math.atan2(diffY, diffXZ) * 180.0 / Math.PI + (double) (KillAura.rotationMode.currentMode.equals("Sunrise") && posCheck(entityIn) ? 10 : 0) + GCDFix.getFixedRotation((float) (Math.cos(System.nanoTime() / 100000000) * 1.5))));
        yaw = (mc.player.rotationYaw + GCDFix.getFixedRotation(MathHelper.wrapDegrees(yaw - mc.player.rotationYaw)));
        pitch = mc.player.rotationPitch + GCDFix.getFixedRotation(MathHelper.wrapDegrees(pitch - mc.player.rotationPitch));
        pitch = MathHelper.clamp(pitch, -90F, 90F);

        return new float[]{yaw, pitch};
    }


    public static float[] getFakeRotations(Entity entityIn) {
        double diffX = entityIn.posX - mc.player.posX;
        double diffZ = entityIn.posZ - mc.player.posZ;
        double diffY;

        if (entityIn instanceof EntityLivingBase) {
            diffY = entityIn.posY + entityIn.getEyeHeight() - (mc.player.posY + mc.player.getEyeHeight()) - 0.2f;
        } else {
            diffY = (entityIn.getEntityBoundingBox().minY + entityIn.getEntityBoundingBox().maxY) / 2 - (mc.player.posY + mc.player.getEyeHeight());
        }
        final double diffXZ = MathHelper.sqrt(diffX * diffX + diffZ * diffZ);

        float yaw = (float) ((Math.atan2(diffZ, diffX) * 180.0 / Math.PI - 90.0));
        float pitch = (float) (-(Math.atan2(diffY, diffXZ) * 180.0 / Math.PI));

        yaw = (mc.player.rotationYaw + GCDFix.getFixedRotation(MathHelper.wrapDegrees(yaw - mc.player.rotationYaw)));
        pitch = mc.player.rotationPitch + GCDFix.getFixedRotation(MathHelper.wrapDegrees(pitch - mc.player.rotationPitch));
        pitch = MathHelper.clamp(pitch, -90F, 90F);

        return new float[]{yaw, pitch};
    }

    public static float[] getCustomRotations(Entity entityIn) {
        double diffX = entityIn.posX - mc.player.posX;
        double diffZ = entityIn.posZ - mc.player.posZ;
        double diffY;

        if (entityIn instanceof EntityLivingBase) {
            if (!KillAura.staticPitch.getCurrentValue()) {
                diffY = entityIn.posY + entityIn.getEyeHeight() - (mc.player.posY + mc.player.getEyeHeight()) - KillAura.pitchHead.getCurrentValue();
            } else {
                diffY = entityIn.getEyeHeight() - mc.player.getEyeHeight() - KillAura.pitchHead.getCurrentValue();
            }
        } else {
            diffY = (entityIn.getEntityBoundingBox().minY + entityIn.getEntityBoundingBox().maxY) / 2 - (mc.player.posY + mc.player.getEyeHeight());
        }
        if (!mc.player.canEntityBeSeen(entityIn)) {
            diffY = entityIn.posY + entityIn.height - (mc.player.posY + mc.player.getEyeHeight());
        }

        final double diffXZ = MathHelper.sqrt(diffX * diffX + diffZ * diffZ);

        float yaw = (float) ((Math.toDegrees(Math.atan2(diffZ, diffX)) - 90)) + GCDFix.getFixedRotation(MathematicHelper.randomizeFloat(KillAura.yawrandom.getCurrentValue(), -KillAura.yawrandom.getCurrentValue()));
        float pitch = (float) Math.toDegrees(-(Math.atan2(diffY, diffXZ))) + GCDFix.getFixedRotation(MathematicHelper.randomizeFloat(KillAura.pitchRandom.getCurrentValue(), -KillAura.pitchRandom.getCurrentValue()));

        yaw = (mc.player.rotationYaw + GCDFix.getFixedRotation(MathHelper.wrapDegrees(yaw - mc.player.rotationYaw)));
        pitch = mc.player.rotationPitch + GCDFix.getFixedRotation(MathHelper.wrapDegrees(pitch - mc.player.rotationPitch));
        pitch = MathHelper.clamp(pitch, -90F, 90F);

        return new float[]{yaw, pitch};
    }

    public static class Rotation {
        public static boolean isReady = false;
        public static float packetPitch;
        public static float packetYaw;
        public static float lastPacketPitch;
        public static float lastPacketYaw;
        public static float renderPacketYaw;
        public static float lastRenderPacketYaw;
        public static float bodyYaw;
        public static float lastBodyYaw;
        public static int rotationCounter;
        private static boolean isAiming;

        public static boolean isAiming() {
            return isAiming;
        }

        public static void setAiming() {
            double x = Helper.mc.player.posX - Helper.mc.player.prevPosX;
            double z = Helper.mc.player.posZ - Helper.mc.player.prevPosZ;

            if (x * x + z * z > 2.500000277905201E-7) {
                bodyYaw = MathematicHelper.clamp(MovementUtils.getMoveDirection(), packetYaw, 50.0f);
                rotationCounter = 0;
            } else if (rotationCounter > 0) {
                --rotationCounter;
                bodyYaw = MathematicHelper.checkAngle(packetYaw, bodyYaw, 10.0f);
            }
        }

        @EventTarget
        public void onPlayerState(EventPlayerState eventPlayerState) {
            if (!Rotation.isAiming() && eventPlayerState.isPre()) {
                isReady = true;
            } else if (!Rotation.isAiming() && isReady && eventPlayerState.isPost()) {
                packetPitch = Helper.mc.player.rotationPitch;
                packetYaw = Helper.mc.player.rotationYaw;
                lastPacketPitch = Helper.mc.player.rotationPitch;
                lastPacketYaw = Helper.mc.player.rotationYaw;
                bodyYaw = Helper.mc.player.renderYawOffset;
                isReady = false;
            } else {
                isReady = false;
            }

            if (eventPlayerState.isPre()) {
                lastBodyYaw = bodyYaw;
                lastPacketPitch = packetPitch;
                lastPacketYaw = packetYaw;
                bodyYaw = MathematicHelper.clamp(bodyYaw, packetYaw, 50.0f);
                Rotation.setAiming();
                lastRenderPacketYaw = renderPacketYaw;
                renderPacketYaw = packetYaw;
            }
        }

        @EventTarget
        public void onSendPacket(EventSendPacket eventSendPacket) {
            if (eventSendPacket.getPacket() instanceof CPacketAnimation) {
                rotationCounter = 10;
            }
        }
    }

}
