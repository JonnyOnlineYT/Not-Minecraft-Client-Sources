package ru.rockstar.api.utils.combat;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import ru.rockstar.Main;
import ru.rockstar.api.utils.Helper;
import ru.rockstar.api.utils.friend.Friend;
import ru.rockstar.api.utils.math.MathematicHelper;
import ru.rockstar.api.utils.world.InventoryHelper;
import ru.rockstar.client.features.impl.combat.AntiBot;
import ru.rockstar.client.features.impl.combat.KillAura;
import ru.rockstar.client.features.impl.movement.Jesus;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class KillAuraHelper implements Helper {
    public static boolean canAttack(EntityLivingBase player) {
        if (player instanceof EntityPlayer || player instanceof EntityAnimal || player instanceof EntityMob
                || player instanceof EntityVillager) {
            if (player instanceof EntityPlayer && !KillAura.auraplayers.getBoolValue()) {
                return false;
            }
            if (player instanceof EntityAnimal && !KillAura.auramobs.getBoolValue()) {
                return false;
            }
            if (player instanceof EntityMob && !KillAura.auramobs.getBoolValue()) {
                return false;
            }

            if (player instanceof EntityVillager && !KillAura.auramobs.getBoolValue()) {
                return false;
            }
            if (KillAura.ignoreNakedPlayer.getBoolValue() && !InventoryHelper.isArmorPlayer(player))
                return false;
        }
        if (player.isInvisible() && !KillAura.invisiblecheck.getBoolValue()) {
            return false;
        }
        if (player instanceof EntityArmorStand) {
            return false;
        }

        for (Friend friend : Main.instance.friendManager.getFriends()) {
            if (!player.getName().equals(friend.getName())) {
            	continue;
            } else {
            	return false;
            }
        }

        if (Main.instance.featureDirector.getFeatureByClass(AntiBot.class).isToggled() && AntiBot.isBotPlayer.contains(player)) {
            return false;
        }
        if (KillAura.teamCheck.getBoolValue() && isOnSameTeam(player)) {
            return false;
        }
        if (!RotationHelper.canSeeEntityAtFov(player,
                (float) KillAura.fov.getNumberValue())
                && !canSeeEntityAtFov(player,
                (float) KillAura.fov.getNumberValue())) {
            return false;
        }
        if (!range(player, ru.rockstar.api.utils.combat.RangeHelper.getRange())) {
            return false;
        }
        if (!player.canEntityBeSeen(mc.player)) {
            return KillAura.walls.getBoolValue();
        }
        return player != mc.player;
    }
    
    public static boolean canAim(EntityLivingBase player) {
        if (player instanceof EntityPlayer || player instanceof EntityAnimal || player instanceof EntityMob
                || player instanceof EntityVillager) {
            if (player instanceof EntityPlayer && !KillAura.auraplayers.getBoolValue()) {
                return false;
            }
            if (player instanceof EntityAnimal && !KillAura.auramobs.getBoolValue()) {
                return false;
            }
            if (player instanceof EntityMob && !KillAura.auramobs.getBoolValue()) {
                return false;
            }

            if (player instanceof EntityVillager && !KillAura.auramobs.getBoolValue()) {
                return false;
            }
            if (KillAura.ignoreNakedPlayer.getBoolValue() && !InventoryHelper.isArmorPlayer(player))
                return false;
        }
        if (player.isInvisible() && !KillAura.invisiblecheck.getBoolValue()) {
            return false;
        }
        if (player instanceof EntityArmorStand) {
            return false;
        }

        for (Friend friend : Main.instance.friendManager.getFriends()) {
            if (!player.getName().equals(friend.getName())) {
            	continue;
            } else {
            	return false;
            }
        }

        if (Main.instance.featureDirector.getFeatureByClass(AntiBot.class).isToggled() && AntiBot.isBotPlayer.contains(player)) {
            return false;
        }
        if (KillAura.teamCheck.getBoolValue() && isOnSameTeam(player)) {
            return false;
        }
        if (!RotationHelper.canSeeEntityAtFov(player,
                (float) KillAura.fov.getNumberValue())
                && !canSeeEntityAtFov(player,
                (float) KillAura.fov.getNumberValue())) {
            return false;
        }
        if (!range(player, ru.rockstar.api.utils.combat.RangeHelper.getRange() + KillAura.addrange.getNumberValue())) {
            return false;
        }
        if (!player.canEntityBeSeen(mc.player)) {
            return KillAura.walls.getBoolValue();
        }
        return player != mc.player;
    }
    
    private static boolean range(EntityLivingBase entity, double range) {
        mc.player.getDistanceToEntity(entity);
        return (double) mc.player.getDistanceToEntity(entity) <= range;
    }
    public static boolean isOnSameTeam(Entity entityIn) {
        return (mc.player.getDisplayName().getUnformattedText().substring(0, 2).equals(entityIn.getDisplayName().getUnformattedText().substring(0, 2))) || mc.player.isOnSameTeam(entityIn);
    }
    public static boolean canSeeEntityAtFov(Entity entityLiving, float scope) {
        double diffX = entityLiving.posX - mc.player.posX;
        double diffZ = entityLiving.posZ - mc.player.posZ;
        float newYaw = (float) (Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0);
        double d = newYaw;
        double difference = angleDifference(d, mc.player.rotationYaw);
        return difference <= (double) scope;
    }

    public static EntityLivingBase getSortEntities() {
        final List<EntityLivingBase> e2 = new ArrayList<EntityLivingBase>();
        for (final Entity e3 : KillAuraHelper.mc.world.loadedEntityList) {
            if (e3 instanceof EntityLivingBase) {
                final EntityLivingBase player = (EntityLivingBase)e3;
                if (KillAuraHelper.mc.player.getDistanceToEntity(player) >= ru.rockstar.api.utils.combat.RangeHelper.getRange() + KillAura.addrange.getNumberValue() || !canAim(player)) {
                    continue;
                }
                e2.add(player);
            }
        }

        String sort = KillAura.sortMode.getOptions();
        if (sort.equalsIgnoreCase("Health")) {
            e2.sort((o1, o2) -> (int) (o1.getHealth() - o2.getHealth()));
        } else if (sort.equalsIgnoreCase("Distance")) {
            e2.sort(Comparator.comparingDouble(mc.player::getDistanceToEntity));
        }

        if (e2.isEmpty())
            return null;

        return e2.get(0);
    }

    public static boolean canApsAttack() {
        int apsMultiplier = (int) (14 / MathematicHelper.randomizeFloat((int) KillAura.maxAps.getNumberValue(), (int) KillAura.minAps.getNumberValue()));
        if (timerHelper.hasReached(50 * apsMultiplier)) {
            timerHelper.reset();
            return true;
        }
        return false;
    }

    public static double angleDifference(double a, double b) {
        float yaw360 = (float) (Math.abs(a - b) % 360.0);
        if (yaw360 > 180.0f) {
            yaw360 = 360.0f - yaw360;
        }
        return yaw360;
    }
}