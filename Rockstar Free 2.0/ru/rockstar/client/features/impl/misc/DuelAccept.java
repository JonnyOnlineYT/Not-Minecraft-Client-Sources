package ru.rockstar.client.features.impl.misc;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.CPacketChatMessage;
import ru.rockstar.api.event.EventTarget;
import ru.rockstar.api.utils.friend.FriendManager;
import ru.rockstar.client.features.Category;
import ru.rockstar.client.features.Feature;
import ru.rockstar.client.ui.settings.impl.BooleanSetting;

public class DuelAccept extends Feature
{
	public BooleanSetting onlyFriends = new BooleanSetting("OnlyFriends", true, () -> true);
    public DuelAccept() {
        super("DuelAccept", "Автоматически принимает запросы на дуэли", 0, Category.MISC);
        addSettings(onlyFriends);
    }
    
    @EventTarget
	public void onReceiveChat(CPacketChatMessage event) {
		for (EntityPlayer entity : mc.world.playerEntities) {
			if(onlyFriends.getBoolValue()) {
				if ((event.getMessage().contains("shield") || event.getMessage().contains("cheat") && FriendManager.isFriend(entity.getName())))	 {
					if (timerHelper.check(500)) {
						mc.player.sendChatMessage("/duel accept");
						timerHelper.resetwatermark();
					}

			}
			} else {
				if ((event.getMessage().contains("shield") || event.getMessage().contains("cheat"))) {
					if (timerHelper.check(500)) {
						mc.player.sendChatMessage("/duel accept");
						timerHelper.resetwatermark();
					}
				}
			}
		}
	}
}
