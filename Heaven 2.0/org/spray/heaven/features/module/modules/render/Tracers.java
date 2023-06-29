package org.spray.heaven.features.module.modules.render;

import com.darkmagician6.eventapi.EventTarget;

import net.minecraft.entity.Entity;
import org.lwjgl.input.Keyboard;
import org.spray.heaven.events.WorldRenderEvent;
import org.spray.heaven.features.module.Category;
import org.spray.heaven.features.module.Module;
import org.spray.heaven.features.module.ModuleInfo;
import org.spray.heaven.features.module.Setting;
import org.spray.heaven.util.EntityUtil;
import org.spray.heaven.util.render.ColorUtil;
import org.spray.heaven.util.render.WorldRender;

import java.awt.*;

@ModuleInfo(name = "Tracers", category = Category.RENDER, visible = true, key = Keyboard.KEY_NONE)
public class Tracers extends Module {

	private Setting pulsing = register(new Setting("Pulsing", true));

	private Setting players = register(new Setting("Players", true));
	private Setting friends = register(new Setting("Friends", false).setVisible(() -> players.isToggle()));
	private Setting invisibles = register(new Setting("Invisibles", false));
	private Setting mobs = register(new Setting("Mobs", true));
	private Setting animals = register(new Setting("Animals", true));

	private Setting playerscolor = register(new Setting("Players Color", new Color(0xFFEAE9E9)))
			.setVisible(() -> players.isToggle());
	private Setting friendscolor = register(new Setting("Friends Color", new Color(0xFF76F35D))
			.setVisible(() -> players.isToggle() && friends.isToggle()));
	private Setting mobscolor = register(new Setting("Mobs Color", new Color(0xFFF35D75)))
			.setVisible(() -> mobs.isToggle());
	private Setting animalscolor = register(new Setting("Animals Color", new Color(0xFF5DF3E9)))
			.setVisible(() -> animals.isToggle());

	@EventTarget
	public void onWorldRender(WorldRenderEvent event) {
		for (Entity entity : mc.world.loadedEntityList) {
			if (!EntityUtil.isValid(entity, players.isToggle(), friends.isToggle(), invisibles.isToggle(),
					mobs.isToggle(), animals.isToggle()))
				continue;
			Color color = ColorUtil.getEntitiesColor(entity, playerscolor.getColor(), friendscolor.getColor(),
					mobscolor.getColor(), animalscolor.getColor());

			WorldRender.drawTracer(entity, color);
		}
	}
}
