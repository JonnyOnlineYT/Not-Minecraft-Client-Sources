package Celestial.module.impl.Combat;

import Celestial.event.EventTarget;
import Celestial.event.events.impl.player.EventPreMotion;
import Celestial.event.events.impl.player.ObsidianPlaceEvent;
import Celestial.module.Module;
import Celestial.module.ModuleCategory;
import Celestial.utils.Helper;
import Celestial.utils.math.GCDFix;
import Celestial.utils.math.MathematicHelper;
import Celestial.utils.math.RotationHelper;
import Celestial.ui.settings.impl.NumberSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemEndCrystal;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class AutoCrystal extends Module {
    float yaw;
    float pitch;
    public static NumberSetting crystalSpeed;
    public static NumberSetting crystalRange;
    private long currentMS;
    private long lastMS;

    public AutoCrystal() {
        super("AutoCrystal", "Автоматически плейсит кристалл на обсидиан.", ModuleCategory.Combat);
        this.currentMS = 0L;
        this.lastMS = -1L;
        this.addSettings(AutoCrystal.crystalRange, AutoCrystal.crystalSpeed);
    }

    private void attackEntity(Entity entity) {
        if (entity == null || Helper.mc.player.getHealth() < 0.0f) {
            return;
        }
        if (Helper.mc.player.getDistanceToEntity(entity) > 3.5f) {
            return;
        }
        if (!entity.isDead) {
            Helper.mc.playerController.attackEntity(Helper.mc.player, entity);
            Helper.mc.player.swingArm(EnumHand.MAIN_HAND);
        }
    }

    @EventTarget
    public void onObsidianPlace(ObsidianPlaceEvent e) {
        final int oldSlot = Helper.mc.player.inventory.currentItem;
        final BlockPos pos = e.getPos();
        Helper.mc.player.inventory.currentItem = getSlotWithCrystal();
        Helper.mc.playerController.processRightClickBlock(Helper.mc.player, Helper.mc.world, pos, EnumFacing.UP, new Vec3d(pos.getX(), pos.getY(), pos.getZ()), EnumHand.MAIN_HAND);
        Helper.mc.player.swingArm(EnumHand.MAIN_HAND);
        Helper.mc.player.inventory.currentItem = oldSlot;
    }

    private float[] getRotationVector(final Vec3d vec, final boolean randomRotation, final float yawRandom, final float pitchRandom, final float speedRotation) {
        final Vec3d eyesPos = new Vec3d(Helper.mc.player.posX, Helper.mc.player.getEntityBoundingBox().minY + Helper.mc.player.getEyeHeight(), Helper.mc.player.posZ);
        final double diffX = vec.x - eyesPos.x;
        final double diffY = vec.y - (Helper.mc.player.posY + Helper.mc.player.getEyeHeight() + 0.5);
        final double diffZ = vec.z - eyesPos.z;
        final double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);

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
        yaw = Helper.mc.player.rotationYaw + GCDFix.getFixedRotation(MathHelper.wrapDegrees(yaw - Helper.mc.player.rotationYaw));
        pitch = Helper.mc.player.rotationPitch + GCDFix.getFixedRotation(MathHelper.wrapDegrees(pitch - Helper.mc.player.rotationPitch));
        pitch = MathHelper.clamp(pitch, -90F, 90F);
        yaw = RotationHelper.updateRotation(Helper.mc.player.rotationYaw, yaw, speedRotation);
        pitch = RotationHelper.updateRotation(Helper.mc.player.rotationPitch, pitch, speedRotation);

        return new float[]{yaw, pitch};
    }

    private int getSlotWithCrystal() {
        for (int i = 0; i < 9; i++) {
            if (Helper.mc.player.inventory.getStackInSlot(i).getItem() instanceof ItemEndCrystal) {
                return i;
            }
        }

        return -1;
    }
    @EventTarget
    public void onUpdate(final EntityPlayerSP player) {
    }

    @EventTarget
    public void EventPreMotion(final EventPreMotion event) {
        for (final Entity entity : Helper.mc.world.loadedEntityList) {
            if (entity instanceof EntityEnderCrystal && Helper.mc.player.getDistanceToEntity(entity) <= AutoCrystal.crystalRange.getNumberValue()) {
                final float[] rots2 = getRotations1(entity, true, 1.5f, 1.5f);
                if (Helper.mc.player.getCooledAttackStrength(0.0f) != 1.0f) {
                    continue;
                }
                Helper.mc.playerController.attackEntity(Helper.mc.player, entity);
                Helper.mc.player.swingArm(EnumHand.MAIN_HAND);
            }
        }
    }

    public static float[] getRotations1(final Entity entityIn, final boolean random, final float yawRandom, final float pitchRandom) {
        final double diffX = entityIn.posX + (entityIn.posX - entityIn.prevPosX) - Helper.mc.player.posX - Helper.mc.player.motionX;
        final double diffZ = entityIn.posZ + (entityIn.posZ - entityIn.prevPosZ) - Helper.mc.player.posZ - Helper.mc.player.motionZ;
        double diffY;
        if (entityIn instanceof EntityLivingBase) {
            diffY = entityIn.posY + entityIn.getEyeHeight() - (Helper.mc.player.posY + Helper.mc.player.getEyeHeight()) - 0.35;
        }
        else {
            diffY = (entityIn.getEntityBoundingBox().minY + entityIn.getEntityBoundingBox().maxY) / 2.0 - (Helper.mc.player.posY + Helper.mc.player.getEyeHeight());
        }
        if (!Helper.mc.player.canEntityBeSeen(entityIn)) {
            diffY = entityIn.posY + entityIn.height - (Helper.mc.player.posY + Helper.mc.player.getEyeHeight());
        }
        final double diffXZ = MathHelper.sqrt(diffX * diffX + diffZ * diffZ);
        float randomYaw = 0.0f;
        if (random) {
            randomYaw = MathematicHelper.randomizeFloat(yawRandom, -yawRandom);
        }
        float randomPitch = 0.0f;
        if (random) {
            randomPitch = MathematicHelper.randomizeFloat(pitchRandom, -pitchRandom);
        }
        float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0 / 3.141592653589793 - 90.0) + randomYaw;
        float pitch = (float)(-(Math.atan2(diffY, diffXZ) * 180.0 / 3.141592653589793)) + randomPitch;
        yaw = Helper.mc.player.rotationYaw + GCDFix.getFixedRotation(MathHelper.wrapDegrees(yaw - Helper.mc.player.rotationYaw));
        pitch = Helper.mc.player.rotationPitch + GCDFix.getFixedRotation(MathHelper.wrapDegrees(pitch - Helper.mc.player.rotationPitch));
        pitch = MathHelper.clamp(pitch, -90.0f, 90.0f);
        yaw = RotationHelper.updateRotation(Helper.mc.player.rotationYaw, yaw, MathematicHelper.randomizeFloat(360.0f, 360.0f));
        pitch = RotationHelper.updateRotation(Helper.mc.player.rotationPitch, pitch, MathematicHelper.randomizeFloat(360.0f, 360.0f));
        return new float[] { yaw, pitch };
    }

    public static final WorldClient getWorld() {
        return getMinecraft().world;
    }

    public static final Minecraft getMinecraft() {
        return Minecraft.getMinecraft();
    }

    public boolean hasDelayRun(final long time) {
        return this.currentMS - this.lastMS >= time;
    }

    static {
        AutoCrystal.crystalSpeed = new NumberSetting("Crystal Speed", 0.0f, 1.0f, 19.0f, 0.1f, () -> true);
        AutoCrystal.crystalRange = new NumberSetting("Crystal Range", 6.0f, 1.0f, 10.0f, 0.1f, () -> true);
    }
}