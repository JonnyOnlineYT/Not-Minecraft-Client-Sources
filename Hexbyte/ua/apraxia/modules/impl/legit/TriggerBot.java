package ua.apraxia.modules.impl.legit;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import ua.apraxia.eventapi.EventTarget;
import ua.apraxia.eventapi.events.impl.player.EventUpdate;
import ua.apraxia.modules.Categories;
import ua.apraxia.modules.Module;
import ua.apraxia.modules.settings.impl.BooleanSetting;
import ua.apraxia.modules.settings.impl.SliderSetting;
import ua.apraxia.utility.other.MoveUtility;

public class TriggerBot extends Module {

    public static BooleanSetting onlyCrit = new BooleanSetting("Only Crits", false);
    public static SliderSetting critFallDist = new SliderSetting("Fall Distance", 0.2F, 0.08F, 1, 0.01f);
    public static BooleanSetting players = new BooleanSetting("Players", true);
    public static BooleanSetting mobs = new BooleanSetting("Mobs", false);
    public static SliderSetting range = new SliderSetting("Trigger Range", 4, 1, 6, 0.1f);

    public TriggerBot() {
        super("TriggerBot", Categories.Legit);
        addSetting(range, players, mobs, onlyCrit, critFallDist);
    }

    public static boolean canTrigger(EntityLivingBase player) {

        if (player instanceof EntityPlayer || player instanceof EntityAnimal || player instanceof EntityMob || player instanceof EntityVillager) {
            if (player instanceof EntityPlayer && !players.value) {
                return false;
            }
            if (player instanceof EntityAnimal && !mobs.value) {
                return false;
            }
            if (player instanceof EntityMob && !mobs.value) {
                return false;
            }
            if (player instanceof EntityVillager && !mobs.value) {
                return false;
            }
        }
        return player != mc.player;
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        Entity entity = mc.objectMouseOver.entityHit;
        if (entity == null || mc.player.getDistance(entity) > range.value || entity instanceof EntityEnderCrystal || entity.isDead || ((EntityLivingBase) entity).getHealth() <= 0.0f) {
            return;
        }

        if (MoveUtility.isBlockAboveHead()) {
            if (!(mc.player.fallDistance >= critFallDist.value) && mc.player.getCooledAttackStrength(0.8F) == 1 && onlyCrit.value && !mc.player.isOnLadder() && !MoveUtility.isInLiquid() && !mc.player.isInWeb && mc.player.getRidingEntity() == null) {
                return;
            }
        } else {
            if (mc.player.fallDistance != 0 && onlyCrit.value && !mc.player.isOnLadder() && !MoveUtility.isInLiquid() && !mc.player.isInWeb && mc.player.getRidingEntity() == null) {
                return;
            }
        }

        if (canTrigger((EntityLivingBase) entity)) {
            if (mc.player.getCooledAttackStrength(0) == 1) {
                mc.playerController.attackEntity(mc.player, entity);
                mc.player.swingArm(EnumHand.MAIN_HAND);
            }
        }
    }
}

