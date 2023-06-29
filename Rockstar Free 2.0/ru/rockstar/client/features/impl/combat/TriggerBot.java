package ru.rockstar.client.features.impl.combat;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import ru.rockstar.Main;
import ru.rockstar.api.event.EventTarget;
import ru.rockstar.api.event.event.EventUpdate;
import ru.rockstar.api.utils.friend.Friend;
import ru.rockstar.api.utils.movement.MovementHelper;
import ru.rockstar.client.features.Category;
import ru.rockstar.client.features.Feature;
import ru.rockstar.client.ui.settings.impl.BooleanSetting;
import ru.rockstar.client.ui.settings.impl.NumberSetting;

public class TriggerBot extends Feature {

    public static NumberSetting range;
    public static BooleanSetting players;
    public static BooleanSetting mobs;
    public static BooleanSetting onlyCrit = new BooleanSetting("Only Crits", false, () -> true);
    public static NumberSetting critFallDist = new NumberSetting("Fall Distance", 0.2F, 0.08F, 1, 0.01f, () -> onlyCrit.getBoolValue());

    public TriggerBot() {
        super("TriggerBot", "Автоматически наносит удар при наводке на сущность",0, Category.COMBAT);
        players = new BooleanSetting("Players", true, () -> true);
        mobs = new BooleanSetting("Mobs", false, () -> true);
        range = new NumberSetting("Trigger Range", 4, 1, 6, 0.1f, () -> true);
        addSettings(range, players, mobs, onlyCrit, critFallDist);
    }

    public static boolean canTrigger(EntityLivingBase player) {
        for (Friend friend : Main.instance.friendManager.getFriends()) {
            if (!player.getName().equals(friend.getName())) {
                continue;
            }
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
        return player != mc.player;
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        Entity entity = mc.objectMouseOver.entityHit;
        if (entity == null || mc.player.getDistanceToEntity(entity) > range.getNumberValue() || entity instanceof EntityEnderCrystal || entity.isDead || ((EntityLivingBase) entity).getHealth() <= 0.0f) {
            return;
        }

        if (MovementHelper.isBlockAboveHead()) {
            if (!(mc.player.fallDistance >= critFallDist.getNumberValue()) && mc.player.getCooledAttackStrength(0.8F) == 1 && onlyCrit.getBoolValue() && !mc.player.isOnLadder() && !mc.player.isInLiquid() && !mc.player.isInWeb && mc.player.getRidingEntity() == null) {
                return;
            }
        } else {
            if (mc.player.fallDistance != 0 && onlyCrit.getBoolValue() && !mc.player.isOnLadder() && !mc.player.isInLiquid() && !mc.player.isInWeb && mc.player.getRidingEntity() == null) {
                return;
            }
        }

        if (canTrigger((EntityLivingBase) entity)) {
        	if (onlyCrit.getBoolValue()) {
        		if (mc.player.fallDistance > critFallDist.getNumberValue()) {
        			if (mc.player.getCooledAttackStrength(0) == 1) {
                        mc.playerController.attackEntity(mc.player, entity);
                        mc.player.swingArm(EnumHand.MAIN_HAND);
                    }
        		}
        	} else {
        		if (mc.player.getCooledAttackStrength(0) == 1) {
                    mc.playerController.attackEntity(mc.player, entity);
                    mc.player.swingArm(EnumHand.MAIN_HAND);
                }
        	}
        }
    }
}

