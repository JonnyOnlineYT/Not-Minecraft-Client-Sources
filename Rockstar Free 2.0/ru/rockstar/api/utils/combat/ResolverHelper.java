package ru.rockstar.api.utils.combat;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemShield;
import ru.rockstar.Main;
import ru.rockstar.api.utils.Helper;
import ru.rockstar.api.utils.world.InventoryHelper;
import ru.rockstar.client.features.impl.combat.KillAura;

public class ResolverHelper implements Helper {

	static boolean canAttack;
	
	public static boolean canAttack() {
		if (!((RotationHelper.isAimAtMe(KillAura.target, 65.0f)) || (KillAura.breakerMode.getOptions().equalsIgnoreCase("New") && Main.instance.featureDirector.getFeatureByClass(KillAura.class).isToggled() && InventoryHelper.doesHotbarHaveAxe() && KillAura.shieldBreak.getBoolValue() && timerHelper.hasReached(KillAura.breakerDelay.getNumberValue() * 10) && InventoryHelper.getAxe() >= 0 && KillAura.target instanceof EntityPlayer && KillAura.target.isHandActive() && KillAura.target.getActiveItemStack().getItem() instanceof ItemShield))) {
			canAttack = true;
		} else {
			canAttack = false;
		}
		return canAttack;
	}
}
