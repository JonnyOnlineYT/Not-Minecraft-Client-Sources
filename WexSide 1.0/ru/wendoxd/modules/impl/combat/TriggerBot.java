package ru.wendoxd.modules.impl.combat;

import net.minecraft.util.EnumHand;
import ru.wendoxd.WexSide;
import ru.wendoxd.events.Event;
import ru.wendoxd.events.impl.player.EventUpdate;
import ru.wendoxd.modules.Module;
import ru.wendoxd.ui.menu.elements.Frame;
import ru.wendoxd.ui.menu.elements.tabelements.CheckBox;
import ru.wendoxd.ui.menu.utils.MenuAPI;

public class TriggerBot extends Module {
	public static Frame frame = new Frame("TriggerBot");
	public static CheckBox triggerBot = new CheckBox("TriggerBot").markSetting("TriggerBot")
			.markArrayList("TriggerBot");
	public static CheckBox ignoreFriends = new CheckBox("Ignore Friends", () -> triggerBot.isEnabled(true));
	public static CheckBox onlyCriticals = new CheckBox("Only Criticals", () -> triggerBot.isEnabled(true));

	public void initSettings() {
		frame.register(triggerBot, ignoreFriends);
		MenuAPI.combat.register(frame);
	}

	public void onEvent(Event event) {
		if (event instanceof EventUpdate && triggerBot.isEnabled(false)) {
			if (mc.pointedEntity != null && mc.player.getCooledAttackStrength(1.5f) > 0.93) {
				if (ignoreFriends.isEnabled(false) && WexSide.friendManager.isFriend(mc.pointedEntity.getName())) {
					return;
				}
				if (onlyCriticals.isEnabled(false) && (mc.player.fallDistance == 0 || mc.player.onGround)) {
					return;
				}
				mc.playerController.attackEntity(mc.player, mc.pointedEntity);
				mc.player.swingArm(EnumHand.MAIN_HAND);
			}
		}
	}
}
