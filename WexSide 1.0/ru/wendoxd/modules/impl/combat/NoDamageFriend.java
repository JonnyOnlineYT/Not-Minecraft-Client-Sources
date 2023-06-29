package ru.wendoxd.modules.impl.combat;

import net.minecraft.entity.player.EntityPlayer;
import ru.wendoxd.WexSide;
import ru.wendoxd.events.Event;
import ru.wendoxd.events.impl.packet.EventAttackEntity;
import ru.wendoxd.modules.Module;
import ru.wendoxd.ui.menu.elements.Frame;
import ru.wendoxd.ui.menu.elements.tabelements.CheckBox;
import ru.wendoxd.ui.menu.utils.MenuAPI;

public class NoDamageFriend extends Module {
	public static Frame nodamagefriends = new Frame("NoDamageFriends");
	public static CheckBox noDamageFriends = new CheckBox("NoDamageFriends");

	public void initSettings() {
		nodamagefriends.register(noDamageFriends);
		MenuAPI.combat.register(nodamagefriends);
	}

	public void onEvent(Event event) {
		if (event instanceof EventAttackEntity) {
			if (((EventAttackEntity) event).getEntity() instanceof EntityPlayer && noDamageFriends.isEnabled(false)) {
				EntityPlayer player = (EntityPlayer) ((EventAttackEntity) event).getEntity();
				if (WexSide.friendManager.isFriend(player.getName())) {
					((EventAttackEntity) event).setCanceled();
				}
			}
		}
	}
}
