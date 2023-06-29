package org.moonware.client.feature.impl.combat;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import org.moonware.client.utils.MWUtils;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.player.EventPreMotion;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.helpers.math.GCDFix;
import org.moonware.client.helpers.math.RotationHelper;
import org.moonware.client.settings.impl.NumberSetting;

public class CrystalAura extends Feature {
    float yaw;
    float pitch;
    public CrystalAura() {
        super("AutoPlaceExplosion", "Automatically hits nearby end crystals.", Type.Combat);
        addSettings(CrystalAura.crystalRange, CrystalAura.crystalSpeed);
    }

    //public static Value crystalSpeed = new Value("Crystal Speed");
    //public static Value crystalRange = new Value("Crystal Range");
    public static NumberSetting crystalSpeed = new NumberSetting("Crystal Speed", 0, 1,19,0.1f, () -> true);
    public static NumberSetting crystalRange = new NumberSetting("Crystal Range", 6, 1,10,0.1f, () -> true);

    private long currentMS;
    private long lastMS = -1L;

    @EventTarget
    public void onUpdate(EntityPlayerSP player) {
        if(getState()) {

        }
    }

    @EventTarget
    public void EventPreMotion(EventPreMotion event) {
        for (Entity entity : Minecraft.world.loadedEntityList) {
            if ((entity instanceof EntityEnderCrystal && Minecraft.player.getDistanceToEntity(entity) <= crystalRange.getNumberValue())) {
                float[] rots2 = getRotations1(entity, true, 1.5f, 1.5f);
                    if (Minecraft.player.getCooledAttackStrength(0) == 1) {
                        Minecraft.playerController.attackEntity(Minecraft.player, entity);
                        Minecraft.player.swingArm(EnumHand.MAIN_HAND);
                    }
            }
        }
    }

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
        yaw = RotationHelper.updateRotation(Minecraft.player.rotationYaw, yaw, MWUtils.randomFloat(360, 360));
        pitch = RotationHelper.updateRotation(Minecraft.player.rotationPitch, pitch, MWUtils.randomFloat(360, 360));
        return new float[]{yaw, pitch};
    }

    public static final ClientLevel getWorld() {
        return Minecraft.world;
    }
    public static final Minecraft getMinecraft() {
        return Minecraft.getMinecraft();
    }
    public boolean hasDelayRun(long time) {
        return (currentMS - lastMS) >= time;
    }

}
