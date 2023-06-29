package Celestial.module.impl.Combat;

import Celestial.event.EventTarget;
import Celestial.event.events.impl.player.EventUpdate;
import Celestial.friend.Friend;
import Celestial.module.Module;
import Celestial.module.ModuleCategory;
import Celestial.utils.Helper;
import Celestial.utils.movement.MovementUtils;
import Celestial.Smertnix;
import Celestial.ui.settings.impl.BooleanSetting;
import Celestial.ui.settings.impl.NumberSetting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;

public class TriggerBot extends Module {

    public static NumberSetting range;
    public static BooleanSetting players;
    public static BooleanSetting mobs;
    public static BooleanSetting onlyCrit = new BooleanSetting("Only Crits", false, () -> true);
    public static NumberSetting critFallDist = new NumberSetting("Fall Distance", 0.2F, 0.08F, 1, 0.01f, () -> onlyCrit.getCurrentValue());

    public TriggerBot() {
        super("TriggerBot", "Автоматически аттакует энтити(не наводится)", ModuleCategory.Combat);
        players = new BooleanSetting("Players", true, () -> true);
        mobs = new BooleanSetting("Mobs", false, () -> true);
        range = new NumberSetting("Trigger Range", 4, 1, 6, 0.1f, () -> true);
        addSettings(range, players, mobs, onlyCrit, critFallDist);
    }

    public static boolean canTrigger(EntityLivingBase player) {
        for (Friend friend : Smertnix.instance.friendManager.getFriends()) {
            if (!player.getName().equals(friend.getName())) {
                continue;
            }
            return false;
        }

        if (player instanceof EntityPlayer || player instanceof EntityAnimal || player instanceof EntityMob || player instanceof EntityVillager) {
            if (player instanceof EntityPlayer && !players.getCurrentValue()) {
                return false;
            }
            if (player instanceof EntityAnimal && !mobs.getCurrentValue()) {
                return false;
            }
            if (player instanceof EntityMob && !mobs.getCurrentValue()) {
                return false;
            }
            if (player instanceof EntityVillager && !mobs.getCurrentValue()) {
                return false;
            }
        }
        return player != Helper.mc.player;
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        Entity entity = Helper.mc.objectMouseOver.entityHit;
        if (entity == null || Helper.mc.player.getDistanceToEntity(entity) > range.getCurrentValue() || entity instanceof EntityEnderCrystal || entity.isDead || ((EntityLivingBase) entity).getHealth() <= 0.0f) {
            return;
        }

        if (MovementUtils.isBlockAboveHead()) {
            if (!(Helper.mc.player.fallDistance >= critFallDist.getCurrentValue()) && Helper.mc.player.getCooledAttackStrength(0.8F) == 1 && onlyCrit.getCurrentValue() && !Helper.mc.player.isOnLadder() && !Helper.mc.player.isInLiquid() && !Helper.mc.player.isInWeb && Helper.mc.player.getRidingEntity() == null) {
                return;
            }
        } else {
            if (Helper.mc.player.fallDistance != 0 && onlyCrit.getCurrentValue() && !Helper.mc.player.isOnLadder() && !Helper.mc.player.isInLiquid() && !Helper.mc.player.isInWeb && Helper.mc.player.getRidingEntity() == null) {
                return;
            }
        }

        if (canTrigger((EntityLivingBase) entity)) {
            if (Helper.mc.player.getCooledAttackStrength(0) == 1) {
                Helper.mc.playerController.attackEntity(Helper.mc.player, entity);
                Helper.mc.player.swingArm(EnumHand.MAIN_HAND);
            }
        }
    }
}

