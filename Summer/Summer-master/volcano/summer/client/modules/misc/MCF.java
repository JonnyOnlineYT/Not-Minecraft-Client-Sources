package volcano.summer.client.modules.misc;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import volcano.summer.base.Summer;
import volcano.summer.base.manager.event.Event;
import volcano.summer.base.manager.friend.FriendManager;
import volcano.summer.base.manager.module.Module;
import volcano.summer.client.events.EventMouse;

public class MCF extends Module {

	public MCF() {
		super("MCF", 0, Category.MISC);
	}

	@Override
	public void onEnable() {

	}

	@Override
	public void onDisable() {

	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventMouse) {
			if (Minecraft.getMinecraft().objectMouseOver != null && mc.gameSettings.keyBindPickBlock.pressed
					&& Minecraft.getMinecraft().objectMouseOver.entityHit instanceof EntityPlayer) {
				String name = Minecraft.getMinecraft().objectMouseOver.entityHit.getName();
				if (!FriendManager.isFriend(name)) {
					FriendManager.addFriend(name, name);
					Summer.tellPlayer("Friend Has Been Added: " + name);
				} else {
					FriendManager.deleteFriend(name);
					Summer.tellPlayer("Friend Has Been Deleted: " + name);
				}
			}
		}
	}
}