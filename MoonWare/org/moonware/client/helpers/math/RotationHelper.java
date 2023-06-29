package org.moonware.client.helpers.math;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import org.moonware.client.utils.MWUtils;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.packet.EventSendPacket;
import org.moonware.client.event.events.impl.player.EventPlayerState;
import org.moonware.client.helpers.player.MovementHelper;
import org.moonware.client.feature.impl.combat.KillAura;
import org.moonware.client.helpers.Helper;

public class RotationHelper implements Helper {

    public static float[] getRotations1(Entity entityIn, boolean random, float yawRandom, float pitchRandom) {
        double diffX = entityIn.posX + (entityIn.posX - entityIn.prevPosX) - Minecraft.player.posX - Minecraft.player.motionX;
        double diffZ = entityIn.posZ + (entityIn.posZ - entityIn.prevPosZ) - Minecraft.player.posZ - Minecraft.player.motionZ;
        double diffY;


        if (entityIn instanceof EntityLivingBase) {
            diffY = entityIn.posY + entityIn.getEyeHeight() - (Minecraft.player.posY + Minecraft.player.getEyeHeight()) - 0.35;
        } else {
            diffY = (entityIn.getEntityBoundingBox().minY + entityIn.getEntityBoundingBox().maxY) / 2 - (Minecraft.player.posY + Minecraft.player.getEyeHeight());
        }
        if (!Minecraft.player.canEntityBeSeen(entityIn)) {
            diffY = entityIn.posY + entityIn.height - (Minecraft.player.posY + Minecraft.player.getEyeHeight());
        }
        double diffXZ = MathHelper.sqrt(diffX * diffX + diffZ * diffZ);

        float randomYaw = 0.0f;
        if (random) {
            randomYaw = MWUtils.randomFloat(yawRandom, -yawRandom);
        }
        float randomPitch = 0.0f;
        if (random) {
            randomPitch = MWUtils.randomFloat(pitchRandom, -pitchRandom);
        }

        float yaw = (float) (((Math.atan2(diffZ, diffX) * 180 / Math.PI) - 90)) + randomYaw;
        float pitch = (float) (-(Math.atan2(diffY, diffXZ) * 180 / Math.PI)) + randomPitch;

        yaw = (Minecraft.player.rotationYaw + GCDFix.getFixedRotation(MathHelper.wrapDegrees(yaw - Minecraft.player.rotationYaw)));
        pitch = Minecraft.player.rotationPitch + GCDFix.getFixedRotation(MathHelper.wrapDegrees(pitch - Minecraft.player.rotationPitch));
        pitch = MathHelper.clamp(pitch, -90F, 90F);
        yaw = updateRotation(Minecraft.player.rotationYaw, yaw, MWUtils.randomFloat(360, 360));
        pitch = updateRotation(Minecraft.player.rotationPitch, pitch, MWUtils.randomFloat(360, 360));
        return new float[]{yaw, pitch};
    }
    public static float[] getRotationsCustom(Entity entityIn, float speed, boolean random) {
        String mode = KillAura.rotationMode.getOptions();
        float yawCurrent = mode.equalsIgnoreCase("Packet") || mode.equalsIgnoreCase("Sunrise") || mode.equalsIgnoreCase("AAC") ? Rotation.packetYaw : Minecraft.player.rotationYaw;
        float pitchCurrent = mode.equalsIgnoreCase("Packet") || mode.equalsIgnoreCase("Sunrise") || mode.equalsIgnoreCase("AAC") ? Rotation.packetPitch : Minecraft.player.rotationPitch;
        float randomYaw = 0.0f;
        if (random) {
            //randomYaw = MathematicHelper.randomizeFloat(KillAura.rotationMode.currentMode.equals("ReallyWorld") ? -2.0f : -KillAura.rotYawRandom.getCurrentValue(), KillAura.rotationMode.currentMode.equals("ReallyWorld") ? -2.0f : -KillAura.rotYawRandom.getCurrentValue());
        }
        float randomPitch = 0.0f;
        if (random) {
           //randomPitch = MathematicHelper.randomizeFloat(KillAura.rotationMode.currentMode.equals("ReallyWorld") ? -2.0f : -KillAura.rotPitchRandom.getCurrentValue(), KillAura.rotationMode.currentMode.equals("ReallyWorld") ? -2.0f : -KillAura.rotPitchRandom.getCurrentValue());
        }
        //float yaw = RotationHelper.updateRotation(yawCurrent + randomYaw, RotationHelper.getRotations(entityIn, random)[0], KillAura.rotSpeed.getCurrentValue() >= 5.0f && !KillAura.rotationMode.currentMode.equals("Sunrise") || KillAura.rotationMode.currentMode.equals("ReallyWorld") ? Float.MAX_VALUE : (KillAura.rotationMode.currentMode.equals("Sunrise") ? 25.0f : speed));
        //float pitch = RotationHelper.updateRotation(pitchCurrent + randomPitch, RotationHelper.getRotations(entityIn, random)[1], KillAura.rotSpeed.getCurrentValue() >= 5.0f && !KillAura.rotationMode.currentMode.equals("Sunrise") || KillAura.rotationMode.currentMode.equals("ReallyWorld") ? Float.MAX_VALUE : (KillAura.rotationMode.currentMode.equals("Sunrise") ? 25.0f : speed));
        float yaw = 0;
        float pitch = 0;
        return new float[]{yaw, pitch};
    }
    public static boolean isLookingAtEntity(boolean blockCheck, float yaw, float pitch, float xExp, float yExp, float zExp, Entity entity, double range) {
        Vec3d src = Minecraft.player.getPositionEyes(1.0f);
        Vec3d vectorForRotation = Entity.getVectorForRotation(pitch, yaw);
        Vec3d dest = src.add(vectorForRotation.xCoord * range, vectorForRotation.yCoord * range, vectorForRotation.zCoord * range);
        RayTraceResult rayTraceResult = Minecraft.world.rayTraceBlocks(src, dest, false, false, true);
        if (rayTraceResult == null) {
            return false;
        }
        if (blockCheck && rayTraceResult.typeOfHit == RayTraceResult.Type.BLOCK) {
            return false;
        }
        return entity.getEntityBoundingBox().expand(xExp, yExp, zExp).calculateIntercept(src, dest) != null;
    }
    public static boolean isAimAtMe(Entity entity, float breakRadius) {
        float entityYaw = MathHelper.wrapDegrees(entity.rotationYaw);
        return Math.abs(MathHelper.wrapDegrees(getYawToEntity(entity, Minecraft.player) - entityYaw)) <= breakRadius;
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
    public static Vec3d getEyesPos() {
        return new Vec3d(Minecraft.player.posX, Minecraft.player.getEntityBoundingBox().minY + Minecraft.player.getEyeHeight(), Minecraft.player.posZ);
    }
    public static float[] rotats(EntityLivingBase entity) {

        double diffX = entity.posX - Minecraft.player.posX;
        double diffZ = entity.posZ - Minecraft.player.posZ;
        double diffY = entity.posY + entity.getEyeHeight() * 0.7 - (Minecraft.player.posY + Minecraft.player.getEyeHeight());
        if (!Minecraft.player.canEntityBeSeen(entity)) {
            diffY = entity.posY + entity.height - (Minecraft.player.posY + Minecraft.player.getEyeHeight());
        }

        double dist = MathHelper.sqrt(diffX * diffX + diffZ * diffZ);


        float yaw = (float) (Math.toDegrees(Math.atan2(diffZ, diffX)) - 90) + MWUtils.randomFloat(-2.5f, 2.5f);
        float pitch = (float) (Math.toDegrees(-Math.atan2(diffY, dist))) + MWUtils.randomFloat(-2.5f, 2.5f);

        yaw = Minecraft.player.prevRotationYaw + GCDFix.getFixedRotation(MathHelper.wrapDegrees(yaw - Minecraft.player.rotationYaw));
        pitch = Minecraft.player.prevRotationPitch + GCDFix.getFixedRotation(pitch - Minecraft.player.rotationPitch);
        pitch = MathHelper.clamp(pitch, -80, 70);
        return new float[]{yaw, pitch};
    }

    public static boolean isLookingAtEntity(float yaw, float pitch, float xExp, float yExp, float zExp, Entity entity, double range) {
        Vec3d src = Minecraft.player.getPositionEyes(1.0F);
        Vec3d vectorForRotation = Entity.getVectorForRotation(pitch, yaw);
        Vec3d dest = src.addVector(vectorForRotation.xCoord * range, vectorForRotation.yCoord * range, vectorForRotation.zCoord * range);
        RayTraceResult rayTraceResult = Minecraft.world.rayTraceBlocks(src, dest, false, false, true);
        if (rayTraceResult == null) {
            return false;
        }
        return (entity.getEntityBoundingBox().expand(xExp, yExp, zExp).calculateIntercept(src, dest) != null);
    }

    public static boolean checkPosition(double pos1, double pos2, double pos3) {
        return pos1 <= pos3 && pos1 >= pos2;
    }
    public static boolean posCheck(Entity entity) {
        return checkPosition(Minecraft.player.posY, entity.posY - 1.5, entity.posY + 1.5);
    }
    public static float[] getRotations(Entity entityIn, boolean random) {
        //double diffX = entityIn.posX + (entityIn.posX - entityIn.prevPosX) * (double)KillAura.rotPredict.getCurrentValue() - RotationHelper.mc.player.posX - RotationHelper.mc.player.motionX * (double)KillAura.rotPredict.getCurrentValue();
        //double diffZ = entityIn.posZ + (entityIn.posZ - entityIn.prevPosZ) * (double)KillAura.rotPredict.getCurrentValue() - RotationHelper.mc.player.posZ - RotationHelper.mc.player.motionZ * (double)KillAura.rotPredict.getCurrentValue();
        //double diffY = entityIn instanceof EntityLivingBase ? entityIn.posY + (double)entityIn.getEyeHeight() - (RotationHelper.mc.player.posY + (double)RotationHelper.mc.player.getEyeHeight()) - (KillAura.walls.getCurrentValue() && KillAura.wallsBypass.getCurrentValue() && !((EntityLivingBase)entityIn).canEntityBeSeen(RotationHelper.mc.player) ? -0.38 : (KillAura.rotationMode.currentMode.equals("Sunrise") && RotationHelper.posCheck(entityIn) ? 1.0 : (KillAura.rotationMode.currentMode.equals("ReallyWorld") ? 0.2 : (double)KillAura.rotPitchDown.getCurrentValue()))) : (entityIn.getEntityBoundingBox().minY + entityIn.getEntityBoundingBox().maxY) / 2.0 - (RotationHelper.mc.player.posY + (double)RotationHelper.mc.player.getEyeHeight());
        //double diffXZ = MathHelper.sqrt(diffX * diffX + diffZ * diffZ);
        double diffY  = 0;
        double diffXZ = 4;
        if (KillAura.rotationMode.currentMode.equals("Sunrise") && posCheck(entityIn)) {
            diffY /= diffXZ;
        }
        float randomYaw = 0.0f;
        if (random) {
            //randomYaw = MathematicHelper.randomizeFloat(-KillAura.rotYawRandom.getCurrentValue(), KillAura.rotYawRandom.getCurrentValue());
        }
        float randomPitch = 0.0f;
        if (random) {
            //randomPitch = MathematicHelper.randomizeFloat(-KillAura.rotPitchRandom.getCurrentValue(), KillAura.rotPitchRandom.getCurrentValue());
        }
        //float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0 / Math.PI - 90.0) + randomYaw;
        //float pitch = (float)(-(Math.atan2(diffY, diffXZ) * 180.0 / (KillAura.walls.getCurrentValue() && KillAura.wallsBypass.getCurrentValue() && !((EntityLivingBase)entityIn).canEntityBeSeen(RotationHelper.mc.player) ? 3.1 : Math.PI + (double)(KillAura.rotationMode.currentMode.equals("Sunrise") && RotationHelper.posCheck(entityIn) ? 10 : 0)))) + randomPitch;
        float yaw = 0;
        float pitch = 0;
        if (KillAura.rotationMode.currentMode.equals("Sunrise") && posCheck(entityIn)) {
            pitch += 26.0f;
        }
        yaw = Minecraft.player.rotationYaw + GCDFix.getFixedRotation(MathHelper.wrapDegrees(yaw - Minecraft.player.rotationYaw));
        pitch = Minecraft.player.rotationPitch + GCDFix.getFixedRotation(MathHelper.wrapDegrees(pitch - Minecraft.player.rotationPitch));
        float minValue = -90.0f;
        float maxValue = 90.0f;
        pitch = MathHelper.clamp(pitch, minValue, maxValue);
        return new float[]{yaw, pitch};
    }
    /*
    public static float[] getRotations(Entity entityIn, boolean random, float maxSpeed, float minSpeed, float yawRandom, float pitchRandom) {
        double diffX = entityIn.posX + (entityIn.posX - entityIn.prevPosX) * KillAura.prerange.getNumberValue() - mc.player.posX - mc.player.motionX * KillAura.prerange.getNumberValue();
        double diffZ = entityIn.posZ + (entityIn.posZ - entityIn.prevPosZ) * KillAura.prerange.getNumberValue() - mc.player.posZ - mc.player.motionZ * KillAura.prerange.getNumberValue();
        double diffY;

        diffY = entityIn.posY + entityIn.getEyeHeight() - (mc.player.posY + mc.player.getEyeHeight()) - KillAura.fov.getNumberValue() - (KillAura.walls.getBoolValue() && KillAura.walls.getBoolValue() && !((EntityLivingBase) entityIn).canEntityBeSeen(mc.player) ? -0.38 : 0);

        float randomYaw = 0;
        if (random) {
            randomYaw = MathematicHelper.randomizeFloat(yawRandom, -yawRandom);
        }
        float randomPitch = 0;
        if (random) {
            randomPitch = MathematicHelper.randomizeFloat(pitchRandom, -pitchRandom);
        }

        double diffXZ = MathHelper.sqrt(diffX * diffX + diffZ * diffZ);

        float yaw = (float) (((Math.atan2(diffZ, diffX) * 180 / Math.PI) - 90)) + randomYaw;
        float pitch = (float) (-(Math.atan2(diffY, diffXZ) * 180 / Math.PI)) + randomPitch;

        yaw = (mc.player.rotationYaw + GCDCalcHelper.getFixedRotation(MathHelper.wrapDegrees(yaw - mc.player.rotationYaw)));
        pitch = mc.player.rotationPitch + GCDCalcHelper.getFixedRotation(MathHelper.wrapDegrees(pitch - mc.player.rotationPitch));
        pitch = MathHelper.clamp(pitch, -90F, 90F);
        yaw = RotationHelper.updateRotation(mc.player.rotationYaw, yaw, MathematicHelper.randomizeFloat(minSpeed, maxSpeed));
        pitch = RotationHelper.updateRotation(mc.player.rotationPitch, pitch, MathematicHelper.randomizeFloat(minSpeed, maxSpeed));

        return new float[]{yaw, pitch};
    }

     */

    public static float[] getRotationVector(Vec3d vec, boolean randomRotation, float yawRandom, float pitchRandom, float speedRotation) {
        Vec3d eyesPos = getEyesPos();
        double diffX = vec.xCoord - eyesPos.xCoord;
        double diffY = vec.yCoord - (Minecraft.player.posY + Minecraft.player.getEyeHeight() + 0.5);
        double diffZ = vec.zCoord - eyesPos.zCoord;
        double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);

        float randomYaw = 0;
        if (randomRotation) {
            randomYaw = MWUtils.randomFloat(-yawRandom, yawRandom);
        }
        float randomPitch = 0;
        if (randomRotation) {
            randomPitch = MWUtils.randomFloat(-pitchRandom, pitchRandom);
        }

        float yaw = (float) (Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f) + randomYaw;
        float pitch = (float) (-Math.toDegrees(Math.atan2(diffY, diffXZ))) + randomPitch;
        yaw = Minecraft.player.rotationYaw + GCDFix.getFixedRotation(MathHelper.wrapDegrees(yaw - Minecraft.player.rotationYaw));
        pitch = Minecraft.player.rotationPitch + GCDFix.getFixedRotation(MathHelper.wrapDegrees(pitch - Minecraft.player.rotationPitch));
        pitch = MathHelper.clamp(pitch, -90F, 90F);
        yaw = updateRotation(Minecraft.player.rotationYaw, yaw, speedRotation);
        pitch = updateRotation(Minecraft.player.rotationPitch, pitch, speedRotation);

        return new float[]{yaw, pitch};
    }

    public static float getAngleEntity(Entity entity) {
        float yaw = Minecraft.player.rotationYaw;
        double xDist = entity.posX - Minecraft.player.posX;
        double zDist = entity.posZ - Minecraft.player.posZ;
        float yaw1 = (float) ((Math.atan2(zDist, xDist) * 180 / Math.PI) - 90);
        return yaw + MathHelper.wrapDegrees(yaw1 - yaw);
    }

    public static float[] getFacePosRemote(EntityLivingBase facing, Entity entity, boolean random) {
        Vec3d src = new Vec3d(facing.posX, facing.posY, facing.posZ);
        Vec3d dest = new Vec3d(entity.posX, entity.posY, entity.posZ);
        double diffX = dest.xCoord - src.xCoord;
        double diffY = dest.yCoord - (src.yCoord);
        double diffZ = dest.zCoord - src.zCoord;
        float randomYaw = 0;
        if (random) {
            randomYaw = MWUtils.randomFloat(-2, 2);
        }
        float randomPitch = 0;
        if (random) {
            randomPitch = MWUtils.randomFloat(-2, 2);
        }
        double dist = MathHelper.sqrt(diffX * diffX + diffZ * diffZ);
        float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0F / Math.PI) - 90.0F + randomYaw;
        float pitch = (float) -(Math.atan2(diffY, dist) * 180.0F / Math.PI) + randomPitch;
        return new float[]{MathHelper.wrapDegrees(yaw), MathHelper.wrapDegrees(pitch)};
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

    public static class Rotation {

        public static boolean isReady;
        public static float packetPitch;
        public static float packetYaw;
        public static float lastPacketPitch;
        public static float lastPacketYaw;
        public static float renderPacketYaw;
        public static float lastRenderPacketYaw;
        public static float bodyYaw;
        public static float lastBodyYaw;
        public static int rotationCounter;
        public static boolean isAiming;

        public static boolean isAiming() {
            return !isAiming;
        }

        public static double calcMove() {
            double x = Minecraft.player.posX - Minecraft.player.prevPosX;
            double z = Minecraft.player.posZ - Minecraft.player.prevPosZ;
            return Math.hypot(x, z);
        }

        @EventTarget
        public void onPlayerState(EventPlayerState event) {
            if (isAiming() && event.isPre()) {
                isReady = true;
            } else if (isAiming() && isReady && event.isPost()) {
                packetPitch = Minecraft.player.rotationPitch;
                packetYaw = Minecraft.player.rotationYaw;
                lastPacketPitch = Minecraft.player.rotationPitch;
                lastPacketYaw = Minecraft.player.rotationYaw;
                bodyYaw = Minecraft.player.renderYawOffset;
                isReady = false;
            } else {
                isReady = false;
            }
            if (event.isPre()) {
                lastBodyYaw = bodyYaw;
                lastPacketPitch = packetPitch;
                lastPacketYaw = packetYaw;
                bodyYaw = MathHelper.clamp(bodyYaw, packetYaw, 50F);
                if (calcMove() > 2.500000277905201E-7D) {
                    bodyYaw = MathHelper.clamp(MovementHelper.getMoveDirection(), packetYaw, 50F);
                    rotationCounter = 0;
                } else if (rotationCounter > 0) {
                    rotationCounter--;
                    bodyYaw = MathematicHelper.checkAngle(packetYaw, bodyYaw, 10F);
                }
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
