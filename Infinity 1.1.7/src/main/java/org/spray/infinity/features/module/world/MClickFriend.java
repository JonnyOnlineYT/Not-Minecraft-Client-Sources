package org.spray.infinity.features.module.world;

import org.spray.infinity.event.ClickButtonEvent;
import org.spray.infinity.features.Category;
import org.spray.infinity.features.Module;
import org.spray.infinity.features.ModuleInfo;
import org.spray.infinity.main.Infinity;
import org.spray.infinity.utils.Helper;

import com.darkmagician6.eventapi.EventTarget;

import net.minecraft.entity.player.PlayerEntity;

@ModuleInfo(category = Category.WORLD, desc = "When you hover over a player and click the middle mouse, he is added to friends", key = -2, name = "MClickFriend", visible = true)
public class MClickFriend extends Module {

	@EventTarget
	public void onMiddleClick(ClickButtonEvent event) {

		if (Helper.MC.currentScreen != null)
			return;

		if (event.getButton() == 2) {
			if (Helper.MC.targetedEntity != null
					&& Helper.MC.targetedEntity instanceof PlayerEntity) {
				Infinity.getFriend().switchFriend(Helper.MC.targetedEntity.getName().getString());
			}
		}
	}

}
