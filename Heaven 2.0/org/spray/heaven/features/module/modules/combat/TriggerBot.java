package org.spray.heaven.features.module.modules.combat;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.input.Keyboard;
import org.spray.heaven.features.module.Category;
import org.spray.heaven.features.module.Module;
import org.spray.heaven.features.module.ModuleInfo;
import org.spray.heaven.features.module.Setting;
import org.spray.heaven.main.Wrapper;
import org.spray.heaven.util.BlockUtil;
import org.spray.heaven.util.EntityUtil;
import org.spray.heaven.util.Timer;

@ModuleInfo(name = "TriggerBot", category = Category.COMBAT, visible = true, key = Keyboard.KEY_NONE)
public class TriggerBot extends Module {

	private Setting players = register(new Setting("Players", true));
	private Setting friends = register(new Setting("Friends", false).setVisible(() -> players.isToggle()));
	private Setting invisibles = register(new Setting("Invisibles", false));
	private Setting mobs = register(new Setting("Mobs", true));
	private Setting animals = register(new Setting("Animals", true));

	private Setting autoCrit = register(new Setting("Auto Crit", false));
	private Setting smartCrit = register(new Setting("Smart Crit", true)).setVisible(() -> autoCrit.isToggle());

	private Setting hitdelay = register(new Setting("Hit Delay", false));
	private Setting aps = register(new Setting("APS", 1.82D, 0D, 10D).setVisible(() -> !hitdelay.isToggle()));

	private Timer apsTimer = new Timer();

	@Override
	public void onUpdate() {
		Entity target = mc.objectMouseOver.entityHit;

		if (target != null && EntityUtil.isValid(target, players.isToggle(), friends.isToggle(), invisibles.isToggle(),
				mobs.isToggle(), animals.isToggle())) {
			boolean waitDelay = hitdelay.isToggle() ? Wrapper.getPlayer().getCooledAttackStrength(0.0F) >= 1F
					: apsTimer.hasReached(1000 / aps.getValue());

			if (waitDelay && whenFalling(target)) {
				Wrapper.MC.playerController.attackEntity(Wrapper.getPlayer(), target);
				Wrapper.getPlayer().swingArm(EnumHand.MAIN_HAND);
				apsTimer.reset();
			}
		}
	}

	private boolean whenFalling(Entity target) {
		if (autoCrit.isToggle()) {
			float distance = smartCrit.isToggle() && BlockUtil.getBlock(new BlockPos(Math.floor(mc.player.posX),
					mc.player.posY + 2, Math.floor(mc.player.posZ))) != Blocks.AIR ? 0.06f : 0.2f;
			
			return mc.player.fallDistance >= distance && !mc.player.onGround && mc.player.fallDistance != 0
					|| mc.player.isInWater() || mc.player.isInLava() || mc.player.isOnLadder() || mc.player.isInWeb
					|| smartCrit.isToggle() && ((EntityLivingBase) target).getHealth() <= 2.4;
		}
		return true;
	}


}
