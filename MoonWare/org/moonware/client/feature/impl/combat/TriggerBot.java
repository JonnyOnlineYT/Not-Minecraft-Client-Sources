package org.moonware.client.feature.impl.combat;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import org.moonware.client.MoonWare;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.player.EventUpdate;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.friend.Friend;
import org.moonware.client.helpers.world.EntityHelper;
import org.moonware.client.settings.impl.BooleanSetting;
import org.moonware.client.settings.impl.NumberSetting;

public class TriggerBot extends Feature {

    public static NumberSetting range;
    public static BooleanSetting players;
    public static BooleanSetting mobs;
    public static BooleanSetting team;
    public static BooleanSetting onlyCrit = new BooleanSetting("Only Crits", false, () -> true);
    public static NumberSetting critFallDist = new NumberSetting("Fall Distance", 0.2F, 0.08F, 1, 0.01f, () -> onlyCrit.getBoolValue());

    public TriggerBot() {
        super("TriggerBot", "Автоматически наносит удар при наводке на сущность", Type.Combat);
        players = new BooleanSetting("Players", true, () -> true);
        mobs = new BooleanSetting("Mobs", false, () -> true);
        team = new BooleanSetting("Team Check", false, () -> true);
        range = new NumberSetting("Trigger Range", 4, 1, 6, 0.1f, () -> true);
        addSettings(range, players, mobs, team, onlyCrit, critFallDist);
    }

    public static boolean canTrigger(EntityLivingBase player) {
        for (Friend friend : MoonWare.friendManager.getFriends()) {
            if (!player.getName().equals(friend.getName())) {
                continue;
            }
            return false;
        }

        if (team.getBoolValue() && EntityHelper.isTeamWithYou(player)) {
            return false;
        }

        if (player instanceof EntityPlayer || player instanceof EntityAnimal || player instanceof EntityMob || player instanceof EntityVillager) {
            if (player instanceof EntityPlayer && !players.getBoolValue()) {
                return false;
            }
            if (player instanceof EntityAnimal && !mobs.getBoolValue()) {
                return false;
            }
            if (player instanceof EntityMob && !mobs.getBoolValue()) {
                return false;
            }
            if (player instanceof EntityVillager && !mobs.getBoolValue()) {
                return false;
            }
        }
        return player != Minecraft.player;
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        Entity entity = Minecraft.objectMouseOver.entityHit != null ? Minecraft.objectMouseOver.entityHit : mc.player;
        if (Minecraft.player.getCooledAttackStrength(0) == 1) {
            if (canTrigger((EntityLivingBase) entity)) {
                Minecraft.playerController.attackEntity(Minecraft.player, entity);
                Minecraft.player.swingArm(EnumHand.MAIN_HAND);
            }else if (entity ==  Minecraft.player && mc.player.ticksExisted % 12 == 0) {
                Minecraft.player.swingArm(EnumHand.MAIN_HAND);
            }

        }
        /*
        if (entity == null || Minecraft.player.getDistanceToEntity(entity) > range.getNumberValue() || entity instanceof EntityEnderCrystal || entity.isDead || ((EntityLivingBase) entity).getHealth() <= 0.0f) {
            return;
        }

        if (MovementHelper.isBlockAboveHead()) {
            if (!(Minecraft.player.fallDistance >= critFallDist.getNumberValue()) && Minecraft.player.getCooledAttackStrength(0.8F) == 1 && onlyCrit.getBoolValue() && !Minecraft.player.isOnLadder() && !Minecraft.player.isInLiquid() && !Minecraft.player.isInWeb && Minecraft.player.getRidingEntity() == null) {
                return;
            }
        } else {
            if (Minecraft.player.fallDistance != 0 && onlyCrit.getBoolValue() && !Minecraft.player.isOnLadder() && !Minecraft.player.isInLiquid() && !Minecraft.player.isInWeb && Minecraft.player.getRidingEntity() == null) {
                return;
            }
        }

        if (canTrigger((EntityLivingBase) entity)) {
            if (Minecraft.player.getCooledAttackStrength(0) == 1) {
                Minecraft.playerController.attackEntity(Minecraft.player, entity);
                Minecraft.player.swingArm(EnumHand.MAIN_HAND);
            }
        }*/
    }
}
