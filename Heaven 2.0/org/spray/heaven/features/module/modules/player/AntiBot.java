package org.spray.heaven.features.module.modules.player;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.lwjgl.input.Keyboard;
import org.spray.heaven.features.module.Category;
import org.spray.heaven.features.module.Module;
import org.spray.heaven.features.module.ModuleInfo;
import org.spray.heaven.features.module.Setting;
import org.spray.heaven.main.Wrapper;
import org.spray.heaven.notifications.Notification;

import net.minecraft.entity.player.EntityPlayer;

@ModuleInfo(name = "AntiBot", category = Category.PLAYER, visible = true, key = Keyboard.KEY_NONE)
public class AntiBot extends Module {


	private List<Integer> bots = new ArrayList<>();

	@Override
	public void onDisable() {
		bots.clear();
	}

	@Override
	public void onUpdate() {
		for (EntityPlayer bot : mc.world.playerEntities) {
			if (bot == mc.player || bot.isDead || bot.getEntityId() == -1234) // -1234 -> FakePlayer id
				continue;

			UUID uuid = bot.getGameProfile().getId();
			UUID generatedUUID = UUID
					.nameUUIDFromBytes(("OfflinePlayer:" + bot.getName()).getBytes(StandardCharsets.UTF_8));

			if (!uuid.equals(generatedUUID)) {
				bots.add(bot.getEntityId());
			}
		}
	}

	public boolean isValid(EntityPlayer entity) {
		if (isEnabled() && bots.contains(entity.getEntityId()))
			return false;
		return true;
	}

}