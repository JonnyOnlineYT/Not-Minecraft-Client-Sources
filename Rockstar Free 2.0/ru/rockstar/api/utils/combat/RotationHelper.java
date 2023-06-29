package ru.rockstar.api.utils.combat;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import ru.rockstar.api.event.EventTarget;
import ru.rockstar.api.event.event.EventSendPacket;
import ru.rockstar.api.utils.math.MathematicHelper;
import ru.rockstar.api.utils.math.RandomHelper;
import ru.rockstar.api.utils.movement.MovementHelper;
import ru.rockstar.client.features.impl.combat.KillAura;

public class RotationHelper {
    private static Minecraft mc = Minecraft.getMinecraft();

    public static float[] getRotations(final double x, final double y, final double z) {
        final double n = x + 0.5;
        final double diffX = n - Minecraft.getMinecraft().player.posX;
        final double n2 = (y + 0.5) / 2.0;
        final double posY = Minecraft.getMinecraft().player.posY;
        final double diffY = n2 - (posY + Minecraft.getMinecraft().player.getEyeHeight());
        final double n3 = z + 0.5;
        final double diffZ = n3 - Minecraft.getMinecraft().player.posZ;
        final double dist = MathHelper.sqrt(diffX * diffX + diffZ * diffZ);
        final float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0 / 3.141592653589793) - 90.0f;
        final float pitch = (float) (-(Math.atan2(diffY, dist) * 180.0 / 3.141592653589793));
        return new float[]{yaw, pitch};
    }
    
    public static float[] getRotations(Entity ent, boolean b) {
        double x = ent.posX;
        double z = ent.posZ;
        double y = ent.posY + ent.getEyeHeight() / 2.0F;
        return getRotationFromPosition(x, z, y);
    }
    
    public static float[] getRotations2(Entity entity) {
    	final double x = entity.posX;
    	final double y = entity.posY;
    	final double z = entity.posZ;
        final double n = x + 0.5;
        final double diffX = n - Minecraft.getMinecraft().player.posX;
        final double n2 = (y + 0.5) / 2.0;
        final double posY = Minecraft.getMinecraft().player.posY;
        final double diffY = n2 - (posY + Minecraft.getMinecraft().player.getEyeHeight());
        final double n3 = z + 0.5;
        final double diffZ = n3 - Minecraft.getMinecraft().player.posZ;
        final double dist = MathHelper.sqrt(diffX * diffX + diffZ * diffZ);
        final float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0 / 3.141592653589793) - 90.0f;
        final float pitch = (float) (-(Math.atan2(diffY, dist) * 180.0 / 3.141592653589793));
        return new float[]{yaw, pitch};
    }
    
    public static float[] getRatations(Entity entity) {
        Vec3d eyesPos = new Vec3d(mc.player.posX + MathematicHelper.randomizeFloat(-1, 2) / 15, mc.player.posY + (double) mc.player.getEyeHeight() - 0.6, mc.player.posZ + MathematicHelper.randomizeFloat(-1, 2) / 15);
        double diffX = entity.getPositionVector().xCoord - eyesPos.xCoord;
        double diffY = entity.getPositionVector().yCoord - eyesPos.yCoord;
        double diffZ = entity.getPositionVector().zCoord - eyesPos.zCoord;
        double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
        float yaw = MathHelper.wrapDegrees((float) Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f);
        float pitch = MathHelper.wrapDegrees((float) (-Math.toDegrees(Math.atan2(diffY, diffXZ))) - 10.0f);
        float f = mc.gameSettings.mouseSensitivity * 0.6f + 0.2f;
        float gcd = f * f * f * 1.2f;
        yaw -= yaw % gcd;
        pitch -= pitch % gcd;
        return new float[]{yaw, pitch};
    }
    
    public static float getAngleEntity(final Entity entity) {
        return getYawBetween(RotationHelper.mc.player.rotationYaw, RotationHelper.mc.player.posX, RotationHelper.mc.player.posZ, entity.posX, entity.posZ);
    }
    
    public static float getYawBetween(final float yaw, final double srcX, final double srcZ, final double destX, final double destZ) {
        final double xDist = destX - srcX;
        final double zDist = destZ - srcZ;
        final float yaw2 = (float)(Math.atan2(zDist, xDist) * 180.0 / 3.141592653589793 - 90.0);
        return yaw + MathHelper.wrapDegrees(yaw2 - yaw);
    }

    public static float[] getRotationVector(Vec3d vec, boolean randomRotation, float yawRandom, float pitchRandom, float speedRotation) {
        Vec3d eyesPos = getEyesPos();
        double diffX = vec.xCoord - eyesPos.xCoord;
        double diffY = vec.yCoord - (mc.player.posY + mc.player.getEyeHeight() + 0.5);
        double diffZ = vec.zCoord - eyesPos.zCoord;
        double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);

        float randomYaw = 0;
        if (randomRotation) {
            randomYaw = MathematicHelper.randomizeFloat(-yawRandom, yawRandom);
        }
        float randomPitch = 0;
        if (randomRotation) {
            randomPitch = MathematicHelper.randomizeFloat(-pitchRandom, pitchRandom);
        }

        float yaw = (float) (Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f) + randomYaw;
        float pitch = (float) (-Math.toDegrees(Math.atan2(diffY, diffXZ))) + randomPitch;
        yaw = mc.player.rotationYaw + GCDFix.getFixedRotation(MathHelper.wrapDegrees(yaw - mc.player.rotationYaw));
        pitch = mc.player.rotationPitch + GCDFix.getFixedRotation(MathHelper.wrapDegrees(pitch - mc.player.rotationPitch));
        pitch = MathHelper.clamp(pitch, -90F, 90F);
        yaw = RotationHelper.updateRotation(mc.player.rotationYaw, yaw, speedRotation);
        pitch = RotationHelper.updateRotation(mc.player.rotationPitch, pitch, speedRotation);

        return new float[]{yaw, pitch};
    }
    
    public static float[] getFacePosEntityRemote(final EntityLivingBase facing, final Entity en) {
        return getFacePosRemote(new Vec3d(facing.posX, facing.posY, facing.posZ), new Vec3d(en.posX, en.posY, en.posZ));
    }
    
    public static float[] getFacePosRemote(final Vec3d src, final Vec3d dest) {
        final double diffX = dest.xCoord - src.xCoord;
        final double diffY = dest.yCoord - src.yCoord;
        final double diffZ = dest.zCoord - src.zCoord;
        final double dist = MathHelper.sqrt(diffX * diffX + diffZ * diffZ);
        final float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0 / 3.141592653589793) - 90.0f;
        final float pitch = (float)(-(Math.atan2(diffY, dist) * 180.0 / 3.141592653589793));
        return new float[] { MathHelper.wrapDegrees(yaw), MathHelper.wrapDegrees(pitch) };
    }

    public static float[] getSunriseRotations(Entity entityIn, boolean random, float maxSpeed, float minSpeed, float yawRandom, float pitchRandom) {
        double diffX = entityIn.posX + (entityIn.posX - entityIn.prevPosX) * KillAura.rotPredict.getNumberValue() - mc.player.posX - mc.player.motionX * KillAura.rotPredict.getNumberValue();
        double diffZ = entityIn.posZ + (entityIn.posZ - entityIn.prevPosZ) * KillAura.rotPredict.getNumberValue() - mc.player.posZ - mc.player.motionZ * KillAura.rotPredict.getNumberValue();
        double diffY;

        if (entityIn instanceof EntityLivingBase) {
            diffY = entityIn.posY + entityIn.getEyeHeight() - (mc.player.posY + mc.player.getEyeHeight()) - 0.6 - (KillAura.walls.getBoolValue() && !((EntityLivingBase) entityIn).canEntityBeSeen(mc.player) ? -0.38 : 0);
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

    public static float[] getMatrixRotations(Entity entityIn, boolean random, float yawRandom, float pitchRandom) {
        double diffX = entityIn.posX + ((entityIn.posX - entityIn.prevPosX)) * (KillAura.rotPredict.getNumberValue() * MovementHelper.getEntitySpeed(entityIn))  - mc.player.posX - mc.player.motionX * (KillAura.rotPredict.getNumberValue() * MovementHelper.getEntitySpeed(entityIn));
        double diffZ = entityIn.posZ + ((entityIn.posZ - entityIn.prevPosZ))  * (KillAura.rotPredict.getNumberValue() * MovementHelper.getEntitySpeed(entityIn)) - mc.player.posZ - mc.player.motionZ * (KillAura.rotPredict.getNumberValue() * MovementHelper.getEntitySpeed(entityIn));
        double diffY;

        if (entityIn instanceof EntityLivingBase) {
            diffY = entityIn.posY + entityIn.getEyeHeight() - 0.2 - (mc.player.posY + mc.player.getEyeHeight()) - (KillAura.walls.getBoolValue() && !((EntityLivingBase) entityIn).canEntityBeSeen(mc.player) ? -0.38 : 0);
        } else {
            diffY = (entityIn.getEntityBoundingBox().minY + entityIn.getEntityBoundingBox().maxY) / 2.0 - (mc.player.posY + mc.player.getEyeHeight());
        }
        float randomYaw = 0;
        if (random) {
            randomYaw = MathematicHelper.randomizeFloat(yawRandom, -yawRandom);
        }
        float randomPitch = 0;
        if (random) {
            randomPitch = MathematicHelper.randomizeFloat(pitchRandom, -pitchRandom);
        }

        double diffXZ = MathHelper.sqrt(diffX * diffX + diffZ * diffZ);
        diffY /= diffXZ;
        float yaw = (float) (((Math.atan2(diffZ, diffX) * 180 / 3.141592653589793) - 90)) + randomYaw;
        float pitch = (float) (-(Math.atan2(diffY, diffXZ) * 180 / Math.PI)) + randomPitch;


        yaw = (mc.player.rotationYaw + GCDFix.getFixedRotation(MathHelper.wrapDegrees(yaw - mc.player.rotationYaw)));
        pitch = mc.player.rotationPitch + GCDFix.getFixedRotation(MathHelper.wrapDegrees(pitch - mc.player.rotationPitch));
        pitch = MathHelper.clamp(pitch, -90F, 90F);
        return new float[]{yaw, pitch};
    }
    public static boolean isLookingAtEntity(float yaw, float pitch, float xExp, float yExp, float zExp, Entity entity, double range) {
        Vec3d src = mc.player.getPositionEyes(1.0F);
        Vec3d vectorForRotation = Entity.getVectorForRotation(pitch, yaw);
        Vec3d dest = src.addVector(vectorForRotation.xCoord * range, vectorForRotation.yCoord * range, vectorForRotation.zCoord * range);
        RayTraceResult rayTraceResult = mc.world.rayTraceBlocks(src, dest, false, false, true);
        if (rayTraceResult == null) {
            return false;
        }
        return (entity.getEntityBoundingBox().expand(xExp, yExp, zExp).calculateIntercept(src, dest) != null);
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

    public static float[] getNewFaceRotating(Entity entity) {
        double y, xDelta = entity.posX - entity.lastTickPosX;
        double zDelta = entity.posZ - entity.lastTickPosZ;
        double xMulti = 1.0D;
        double zMulti = 1.0D;
        boolean sprint = entity.isSprinting();
        xMulti = xDelta * (sprint ? 0.97D : 1.0D);
        zMulti = zDelta * (sprint ? 0.97D : 1.0D);
        double x = entity.posX + xMulti - mc.player.posX;
        double z = entity.posZ + zMulti - mc.player.posZ;
        if (entity instanceof EntityLivingBase) {
            EntityLivingBase entitylivingbase = (EntityLivingBase) entity;
            float randomed = RandomHelper.nextFloat((float) (entitylivingbase.posY + (entitylivingbase.getEyeHeight() / 1.5F)), (float) (entitylivingbase.posY + entitylivingbase.getEyeHeight() - (entitylivingbase.getEyeHeight() / 3.0F)));
            y = randomed - mc.player.posY + mc.player.getEyeHeight();
        } else {
            y = RandomHelper.nextFloat((float) (entity.getEntityBoundingBox()).minY, (float) (entity.getEntityBoundingBox()).maxY) - mc.player.posY + mc.player.getEyeHeight();
        }
        double distance = mc.player.getDistanceToEntity(entity);
        float yaw = (float) (Math.toDegrees(Math.atan2(z, x)) - 90.0D) + RandomHelper.nextFloat(-1.0F, 2.0F);
        float pitch = (float) (Math.toDegrees(Math.atan2(y, distance)) + RandomHelper.nextFloat(-1.0F, 2.0F));
        yaw = mc.player.rotationYaw + GCDFix.getFixedRotation(MathHelper.wrapDegrees(yaw - mc.player.rotationYaw));
        pitch = mc.player.rotationPitch + GCDFix.getFixedRotation(MathHelper.wrapDegrees(pitch - mc.player.rotationPitch));
        pitch = MathHelper.clamp(pitch, -90.0F, 90.0F);
        return new float[]{yaw, pitch};
    }

    public static boolean isAimAtMe(Entity entity, float breakRadius) {
        float entityYaw = MathHelper.wrapDegrees(entity.rotationYaw);
        return Math.abs((MathHelper.wrapDegrees(getYawToEntity(entity, Minecraft.getMinecraft().player)) - entityYaw)) <= breakRadius;
    }

    public static boolean isAimAtMe(Entity entity) {
        float entityYaw = getNormalizedYaw(entity.rotationYaw);
        float entityPitch = entity.rotationPitch;
        double pMinX = (mc.player.getEntityBoundingBox()).minX;
        double pMaxX = (mc.player.getEntityBoundingBox()).maxX;
        double pMaxY = mc.player.posY + mc.player.height;
        double pMinY = (mc.player.getEntityBoundingBox()).minY;
        double pMaxZ = (mc.player.getEntityBoundingBox()).maxZ;
        double pMinZ = (mc.player.getEntityBoundingBox()).minZ;
        double eX = entity.posX;
        double eY = entity.posY + (entity.height);
        double eZ = entity.posZ;
        double dMaxX = pMaxX - eX;
        double dMaxY = pMaxY - eY;
        double dMaxZ = pMaxZ - eZ;
        double dMinX = pMinX - eX;
        double dMinY = pMinY - eY;
        double dMinZ = pMinZ - eZ;
        double dMinH = Math.sqrt(Math.pow(dMinX, 2.0D) + Math.pow(dMinZ, 2.0D));
        double dMaxH = Math.sqrt(Math.pow(dMaxX, 2.0D) + Math.pow(dMaxZ, 2.0D));
        double maxPitch = 90.0D - Math.toDegrees(Math.atan2(dMaxH, dMaxY));
        double minPitch = 90.0D - Math.toDegrees(Math.atan2(dMinH, dMinY));
        boolean yawAt = (Math.abs(getNormalizedYaw(getYawToEntity(entity, (Entity) mc.player)) - entityYaw) <= 16.0F - mc.player.getDistanceToEntity(entity) / 2.0F);
        boolean pitchAt = ((maxPitch >= entityPitch && entityPitch >= minPitch) || (minPitch >= entityPitch && entityPitch >= maxPitch));
        return (yawAt && pitchAt);
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

    public static float getNormalizedYaw(float yaw) {
        float yawStageFirst = yaw % 360.0F;
        if (yawStageFirst > 180.0F) {
            yawStageFirst -= 360.0F;
            return yawStageFirst;
        }
        if (yawStageFirst < -180.0F) {
            yawStageFirst += 360.0F;
            return yawStageFirst;
        }
        return yawStageFirst;
    }

    public static float[] getRotations(Entity ent) {
        double x = ent.posX;
        double z = ent.posZ;
        double y = ent.posY + ent.getEyeHeight() / 2.0F;
        return getRotationFromPosition(x, z, y);
    }

    public static float[] getAACRotation(Entity entityIn, boolean random, float maxSpeed, float minSpeed, float yawRandom, float pitchRandom) {
        double diffX = entityIn.posX + (entityIn.posX - entityIn.prevPosX) * KillAura.rotPredict.getNumberValue() - mc.player.posX - mc.player.motionX * KillAura.rotPredict.getNumberValue();
        double diffZ = entityIn.posZ + (entityIn.posZ - entityIn.prevPosZ) * KillAura.rotPredict.getNumberValue() - mc.player.posZ - mc.player.motionZ * KillAura.rotPredict.getNumberValue();
        double diffY;

        diffY = entityIn.posY + entityIn.getEyeHeight() - (mc.player.posY + mc.player.getEyeHeight()) - 0.16 - (KillAura.walls.getBoolValue() && !((EntityLivingBase) entityIn).canEntityBeSeen(mc.player) ? -0.38 : 0);

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


        yaw = (mc.player.rotationYaw + GCDFix.getFixedRotation(MathHelper.wrapDegrees(yaw - mc.player.rotationYaw)));
        pitch = mc.player.rotationPitch + GCDFix.getFixedRotation(MathHelper.wrapDegrees(pitch - mc.player.rotationPitch));
        pitch = MathHelper.clamp(pitch, -90F, 90F);
        yaw = RotationHelper.updateRotation(mc.player.rotationYaw, yaw, MathematicHelper.randomizeFloat(minSpeed, maxSpeed));
        pitch = RotationHelper.updateRotation(mc.player.rotationPitch, pitch, MathematicHelper.randomizeFloat(minSpeed, maxSpeed));
        return new float[]{yaw, pitch};
    }
    
    public static float[] rotats(Entity entity) {
        double diffX = entity.posX - mc.player.posX;
        double diffZ = entity.posZ - mc.player.posZ;
        double diffY = entity.posY + entity.getEyeHeight() - (mc.player.posY + mc.player.getEyeHeight()) - 0.4;

        double dist = MathHelper.sqrt(diffX * diffX + diffZ * diffZ);

        float yaw = (float) (((Math.atan2(diffZ, diffX) * 180 / Math.PI) - 90)) + CountHelper.nextFloat(-1, 1);
        float yawBody = (float) (((Math.atan2(diffZ, diffX) * 180 / Math.PI) - 90));
        float pitch = (float) (-(Math.atan2(diffY, dist) * 180 / Math.PI)) + CountHelper.nextFloat(-1, 1);
        float pitch2 = (float) (-(Math.atan2(diffY, dist) * 180 / Math.PI));

        mc.player.setSprinting(false);

        yaw = mc.player.prevRotationYaw + GCDFix.getFixedRotation(MathHelper.wrapDegrees(yaw - mc.player.rotationYaw));
        yawBody = mc.player.prevRotationYaw + MathHelper.wrapDegrees(yawBody - mc.player.rotationYaw);
        pitch = mc.player.prevRotationPitch + GCDFix.getFixedRotation(MathHelper.wrapDegrees(pitch - mc.player.rotationPitch));
        pitch = MathHelper.clamp(pitch, -90, 90);
        return new float[]{yaw, pitch, yawBody, pitch2};
    }


    public static float[] getRotationFromPosition(double x, double z, double y) {
    	double xDiff = x - Minecraft.getMinecraft().player.posX;
        double zDiff = z - Minecraft.getMinecraft().player.posZ;
        double yDiff = y - Minecraft.getMinecraft().player.posY - 1.7;

        double dist = MathHelper.sqrt(xDiff * xDiff + zDiff * zDiff);
        float yaw = (float) (Math.atan2(zDiff, xDiff) * 180.0D / 3.141592653589793D) - 90.0F;
        float pitch = (float) -(Math.atan2(yDiff, dist) * 180.0D / 3.141592653589793D);
        return new float[] { yaw, pitch };
    }

    public static boolean canSeeEntityAtFov(Entity entityLiving, float scope) {
        Minecraft.getMinecraft();
        double diffX = entityLiving.posX - mc.player.posX;
        Minecraft.getMinecraft();
        double diffZ = entityLiving.posZ - mc.player.posZ;
        float newYaw = (float) (Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0);
        double d = newYaw;
        Minecraft.getMinecraft();
        double difference = angleDifference(d, mc.player.rotationYaw);
        return difference <= (double) scope;
    }

    public static double angleDifference(double a, double b) {
        float yaw360 = (float) (Math.abs(a - b) % 360.0);
        if (yaw360 > 180.0f) {
            yaw360 = 360.0f - yaw360;
        }
        return yaw360;
    }

    public static Vec3d getEyesPos() {
        return new Vec3d(mc.player.posX, mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ);
    }

    public static float[] getNeededFacing(Vec3d vec) {
        Vec3d eyesPos = getEyesPos();
        double diffX = vec.xCoord - eyesPos.xCoord;
        double diffY = vec.yCoord - mc.player.posY + mc.player.getEyeHeight() + 0.2D;
        double diffZ = vec.zCoord - eyesPos.zCoord;
        double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
        float yaw = (float) Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0F;
        float pitch = (float) -Math.toDegrees(Math.atan2(diffY, diffXZ));
        yaw = mc.player.rotationYaw + GCDFix.getFixedRotation(MathHelper.wrapDegrees(yaw - mc.player.rotationYaw));
        pitch = mc.player.rotationPitch + GCDFix.getFixedRotation(MathHelper.wrapDegrees(pitch - mc.player.rotationPitch));
        pitch = MathHelper.clamp(pitch, -90.0F, 90.0F);
        return new float[]{yaw, pitch};
    }

    public static float[] getLookAngles(Vec3d vec) {
        float[] angles = new float[2];
        angles[0] = (float) (Math.atan2(mc.player.posZ - vec.zCoord, mc.player.posX - vec.xCoord) / Math.PI * 180.0D) + 90.0F;
        float heights = (float) (mc.player.posY + mc.player.getEyeHeight() - vec.yCoord);
        float distance = (float) Math.sqrt((mc.player.posZ - vec.zCoord) * (mc.player.posZ - vec.zCoord) + (mc.player.posX - vec.xCoord) * (mc.player.posX - vec.xCoord));
        angles[1] = (float) (Math.atan2(heights, distance) / Math.PI * 180.0D);
        return angles;
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
        public static boolean isAiming;

        public static boolean isAiming() {
            return !isAiming;
        }

        public static double calcMove() {
            double x = mc.player.posX - mc.player.prevPosX;
            double z = mc.player.posZ - mc.player.prevPosZ;
            return Math.hypot(x, z);
        }


        @EventTarget
        public void onSendPacket(EventSendPacket eventSendPacket) {
            if (eventSendPacket.getPacket() instanceof CPacketAnimation) {
                rotationCounter = 10;
            }
        }
    }

}