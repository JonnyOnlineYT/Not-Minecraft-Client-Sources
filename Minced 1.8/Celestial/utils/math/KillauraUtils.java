package Celestial.utils.math;

import Celestial.utils.Helper;
import Celestial.Smertnix;
import Celestial.module.impl.Combat.AntiBot;
import Celestial.module.impl.Combat.AutoTotem;
import Celestial.module.impl.Combat.KillAura;
import Celestial.friend.Friend;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.*;

import java.util.ArrayList;
import java.util.Comparator;

public class KillauraUtils implements Helper {
    public static TimerHelper timerHelper = new TimerHelper();
    public static TimerHelper oldVersionTimer = new TimerHelper();

    public static boolean canAttack(EntityLivingBase player) {
        for (Friend friend : Smertnix.instance.friendManager.getFriends()) {
            if (!player.getName().equals(friend.getName())) {
                continue;
            }
            return false;
        }
        if (player instanceof EntitySlime && !KillAura.targetsSetting.getSetting("Mobs").getCurrentValue()) {
            return false;
        }
        if (player instanceof EntityMagmaCube && !KillAura.targetsSetting.getSetting("Mobs").getCurrentValue()) {
            return false;
        }
        if (player instanceof EntityDragon && !KillAura.targetsSetting.getSetting("Mobs").getCurrentValue()) {
            return false;
        }

        if (player instanceof EntityArmorStand) {
            return false;
        }
        if (player instanceof EntitySquid && !KillAura.targetsSetting.getSetting("Animals").getCurrentValue()) {
            return false;
        }
        if ((Smertnix.instance.featureManager.getFeature(AntiBot.class).isEnabled() && AntiBot.isBotPlayer.contains(player))) {
            return false;
        }
        if (player instanceof EntityPlayer || player instanceof EntityAnimal || player instanceof EntityMob || player instanceof EntityVillager) {
            if (player instanceof EntityPlayer && !KillAura.targetsSetting.getSetting("Players").getCurrentValue()) {
                return false;
            }

            if (player instanceof EntityAnimal && !KillAura.targetsSetting.getSetting("Animals").getCurrentValue()) {
                return false;
            }
            if (player instanceof EntityMob && !KillAura.targetsSetting.getSetting("Mobs").getCurrentValue()) {
                return false;
            }
            if (player instanceof EntityVillager && !KillAura.targetsSetting.getSetting("Villagers").getCurrentValue()) {
                return false;
            }
            if (player instanceof EntityOcelot && !KillAura.targetsSetting.getSetting("Animals").getCurrentValue()) {
                return false;
            }
            if (player instanceof EntityWolf && !KillAura.targetsSetting.getSetting("Animals").getCurrentValue()) {
                return false;
            }

            if (player instanceof EntityEnderman && !KillAura.targetsSetting.getSetting("Mobs").getCurrentValue()) {
                return false;
            }
            if (player.isInvisible() && !KillAura.targetsSetting.getSetting("Invisibles").getCurrentValue()) {
                return false;
            }
        }
        if (!canSeeEntityAtFov(player, KillAura.fov.getCurrentValue() * 2)) {
            return false;
        }
        if (!range(player, KillAura.range.getCurrentValue() + KillAura.preAimRange.getCurrentValue())) {
            return false;
        }

        if (!player.canEntityBeSeen(mc.player)) {
            return KillAura.walls.getCurrentValue();
        }
        return player != mc.player;
    }

    public static boolean isBot(EntityLivingBase entity) {
        if (entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entity;
            return Smertnix.instance.featureManager.getFeature(AntiBot.class).isEnabled() && AntiBot.isBotPlayer.contains(player);
        }
        return false;
    }

    public static boolean checkCrystal() {
        if (!KillAura.checkCrystals.getCurrentValue()) {
            return false;
        }
        for (Entity entity : AutoTotem.mc.world.loadedEntityList) {
            if (!(entity instanceof EntityEnderCrystal) || !(AutoTotem.mc.player.getDistanceToEntity(entity) <= KillAura.radiusCrystals.getCurrentValue()))
                continue;
            return true;
        }
        return false;
    }


    public static boolean canSeeEntityAtFov(Entity entityLiving, float scope) {
        double diffX = entityLiving.posX - mc.player.posX;
        double diffZ = entityLiving.posZ - mc.player.posZ;
        float yaw = (float) (Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0);
        double difference = angleDifference(yaw, mc.player.rotationYaw);
        return difference <= scope;
    }

    public static double angleDifference(float oldYaw, float newYaw) {
        float yaw = Math.abs(oldYaw - newYaw) % 360;
        if (yaw > 180) {
            yaw = 360 - yaw;
        }
        return yaw;
    }

    private static boolean range(EntityLivingBase entity, float range) {
        return mc.player.getDistanceToEntity(entity) <= range;
    }

    public static void attackEntity(EntityLivingBase target) {
        if (target == null || mc.player.getHealth() < 0.0f) {
            return;
        }
        if (KillAura.preAimRange.getCurrentValue() > 0) {
            if (mc.player.getDistanceToEntity(target) > KillAura.range.getCurrentValueInt()) {
                return;
            }
        }

        if (!target.isDead) {
            if (KillAura.clickMode.currentMode.equalsIgnoreCase("1.9")) {
                float attackDelay = KillAura.attackCoolDown.getCurrentValue();
                if (mc.player.getCooledAttackStrength(0.5f) >= attackDelay) {
                    mc.playerController.attackEntity(mc.player, target);
                    mc.player.swingArm(EnumHand.MAIN_HAND);

                    KillAura.BreakShield(target);
                }
            } else if (KillAura.clickMode.currentMode.equalsIgnoreCase("1.8")) {
                if (!canApsAttack()) {
                    return;
                }
                mc.playerController.attackEntity(mc.player, target);
                mc.player.swingArm(EnumHand.MAIN_HAND);
            }
        }

    }

    public static boolean canApsAttack() {
        int apsMultiplier = 14 / MathematicHelper.intRandom(KillAura.maxAPS.getCurrentValueInt(), KillAura.minAPS.getCurrentValueInt());

        if (oldVersionTimer.hasReached(50 * apsMultiplier)) {
            oldVersionTimer.reset();
            return true;
        }

        return false;
    }

    public static EntityLivingBase getSortEntities() {
        ArrayList<EntityLivingBase> entity = new ArrayList<EntityLivingBase>();
        for (Entity e : mc.world.loadedEntityList) {
            if (e == null || !(e instanceof EntityLivingBase)) {
                continue;
            }
            EntityLivingBase player = (EntityLivingBase) e;
            if (player.getHealth() > 0.0f && !player.isDead) {
                if (!(mc.player.getDistanceToEntity(player) <= KillAura.range.getCurrentValue() + KillAura.preAimRange.getCurrentValue()) || !canAttack(player)) {
                    continue;
                }
                entity.add(player);
                continue;
            }
            entity.remove(player);
        }
        String mode = KillAura.sortMode.getOptions();
        if (mode.equalsIgnoreCase("Distance")) {
            entity.sort(Comparator.comparingDouble(mc.player::getDistanceToEntity));
        } else if (mode.equalsIgnoreCase("Crosshair")) {
            entity.sort(Comparator.comparingDouble(KillauraUtils::Angle));
        } else if (mode.equalsIgnoreCase("Health")) {
            entity.sort((o1, o2) -> (int) (o1.getHealth() - o2.getHealth()));
        } else if (mode.equalsIgnoreCase("Higher Armor")) {
            entity.sort(Comparator.comparing(EntityLivingBase::getTotalArmorValue).reversed());
        } else if (mode.equalsIgnoreCase("Lowest Armor")) {
            entity.sort(Comparator.comparing(EntityLivingBase::getTotalArmorValue));
        }
        if (KillAura.typeMode.currentMode.equalsIgnoreCase("Single") && KillAura.target != null && entity.contains(KillAura.target)) {
            entity.removeIf(x -> x != KillAura.target);
        }
        if (entity.isEmpty())
            return null;

        return entity.get(0);
    }

    public static float Angle(EntityLivingBase entity) {
        double diffX = entity.posX - mc.player.posX;
        double diffZ = entity.posZ - mc.player.posZ;
        return (float) Math.abs(MathHelper.wrapDegrees((Math.toDegrees(Math.atan2(diffZ, diffX)) - 90) - mc.player.rotationYaw));
    }

    public static Entity rayCast(Entity entityIn, double range) {
        Vec3d vec = entityIn.getPositionVector().add(new Vec3d(0, entityIn.getEyeHeight(), 0));
        Vec3d vecPositionVector = mc.player.getPositionVector().add(new Vec3d(0, mc.player.getEyeHeight(), 0));
        AxisAlignedBB axis = mc.player.getEntityBoundingBox().addCoord(vec.x - vecPositionVector.x, vec.y - vecPositionVector.y, vec.z - vecPositionVector.z).expand(1, 1, 1);
        Entity entityRayCast = null;
        for (Entity entity : mc.world.getEntitiesWithinAABBExcludingEntity(mc.player, axis)) {
            if (entity.canBeCollidedWith() && entity instanceof EntityLivingBase) {
                float size = entity.getCollisionBorderSize();
                AxisAlignedBB axis1 = entity.getEntityBoundingBox().expand(size, size, size);
                RayTraceResult rayTrace = axis1.calculateIntercept(vecPositionVector, vec);
                if (axis1.isVecInside(vecPositionVector)) {
                    if (range >= 0) {
                        entityRayCast = entity;
                        range = 0;
                    }
                } else if (rayTrace != null) {
                    double dist = vecPositionVector.distanceTo(rayTrace.hitVec);
                    if (range == 0 || dist < range) {
                        entityRayCast = entity;
                        range = dist;
                    }
                }
            }
        }

        return entityRayCast;
    }

}
