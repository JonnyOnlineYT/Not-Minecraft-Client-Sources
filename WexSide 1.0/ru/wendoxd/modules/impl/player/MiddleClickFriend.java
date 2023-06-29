package ru.wendoxd.modules.impl.player;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.entity.EntityLivingBase;
import ru.wendoxd.WexSide;
import ru.wendoxd.events.Event;
import ru.wendoxd.events.impl.misc.EventMouseTick;
import ru.wendoxd.modules.Module;
import ru.wendoxd.ui.menu.elements.Frame;
import ru.wendoxd.ui.menu.elements.tabelements.CheckBox;
import ru.wendoxd.ui.menu.elements.tabelements.Slider;
import ru.wendoxd.ui.menu.utils.MenuAPI;
import ru.wendoxd.utils.combat.RaycastHelper;
import ru.wendoxd.utils.misc.ChatUtils;

import javax.vecmath.Vector2f;

public class MiddleClickFriend extends Module {

	private Frame middleClickFriend_frame = new Frame("MiddleClickFriend");
	private CheckBox middleClickFriend = new CheckBox("MiddleClickFriend").markArrayList("MiddleClickFriend");
	private CheckBox middleClickFriendWalls = new CheckBox("Ignore Walls", () -> middleClickFriend.isEnabled(true));
	private final Slider maxDistance = new Slider("Max Dist", 1, 1, 64, 0, () -> middleClickFriend.isEnabled(true));
	private boolean canAddFriend;

	@Override
	protected void initSettings() {
		middleClickFriend_frame.register(middleClickFriend.markSetting("MiddleClickFriend"), maxDistance);
		MenuAPI.player.register(middleClickFriend_frame);
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventMouseTick && middleClickFriend.isEnabled(false)) {
			if (((EventMouseTick) event).getButton() == 2) {
				EntityLivingBase entity = RaycastHelper.getPointedEntity(
						new Vector2f(mc.player.rotationYaw, mc.player.rotationPitch), maxDistance.getFloatValue(), 1,
						!middleClickFriendWalls.isEnabled(false));
				if (entity != null && entity != mc.player) {
					if (WexSide.friendManager.isFriend(entity.getName())) {
						WexSide.friendManager.removeFriend(entity.getName());
						ChatUtils.addChatMessage(ChatFormatting.RED + "Removed " + ChatFormatting.RESET
								+ entity.getName() + " as Friend!");
					} else {
						WexSide.friendManager.addFriend(entity.getName());
						ChatUtils.addChatMessage(ChatFormatting.GREEN + "Added " + ChatFormatting.RESET
								+ entity.getName() + " as Friend!");
					}
				}
			}
		}
	}
}
