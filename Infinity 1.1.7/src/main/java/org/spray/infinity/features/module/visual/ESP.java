package org.spray.infinity.features.module.visual;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;

import org.spray.infinity.event.RenderEvent;
import org.spray.infinity.features.Category;
import org.spray.infinity.features.Module;
import org.spray.infinity.features.ModuleInfo;
import org.spray.infinity.features.Setting;
import org.spray.infinity.features.module.combat.KillAura;
import org.spray.infinity.utils.entity.EntityUtil;
import org.spray.infinity.utils.render.WorldRender;

import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.types.EventType;

import net.minecraft.entity.Entity;

@ModuleInfo(category = Category.VISUAL, desc = "Draw entity esp", key = -2, name = "ESP", visible = true)
public class ESP extends Module {

	private Setting mode = new Setting(this, "Mode", "Box", new ArrayList<>(Arrays.asList("Fill", "Box")));

	private Setting width = new Setting(this, "Width", 1.1f, 0.5f, 3.0f)
			.setVisible(() -> mode.getCurrentMode().equalsIgnoreCase("Box"));
	private Setting alpha = new Setting(this, "Opacity", 50D, 1D, 100D)
			.setVisible(() -> mode.getCurrentMode().equalsIgnoreCase("Fill"));

	// targets
	private Setting players = new Setting(this, "Players", true).setColor(new Color(247, 251, 247));
	private Setting friends = new Setting(this, "Friends", true).setVisible(() -> players.isToggle())
			.setColor(new Color(247, 251, 247));

	private Setting invisibles = new Setting(this, "Invisibles", true);
	private Setting mobs = new Setting(this, "Mobs", true).setColor(new Color(236, 173, 24));
	private Setting animals = new Setting(this, "Animals", false).setColor(new Color(108, 234, 42));

	@Override
	public void onUpdate() {
		setSuffix(mode.getCurrentMode());
	}

	@EventTarget
	public void onWorldRender(RenderEvent event) {
		if (event.getType().equals(EventType.POST)) {
			for (Entity e : EntityUtil.getRenderTargets(players.isToggle(), friends.isToggle(), invisibles.isToggle(),
					mobs.isToggle(), animals.isToggle())) {

				int color = EntityUtil.getEntitiesColor(e, players.getColor().getRGB(),
						friends.getColor().getRGB(), mobs.getColor().getRGB(),
						animals.getColor().getRGB());

				if (e == KillAura.target)
					color = Color.RED.getRGB();

				switch (mode.getCurrentMode()) {
				case "Fill":
					WorldRender.drawFill(e.getBoundingBox(), alpha.getCurrentValueDouble(), color);
					break;
				case "Box":
					WorldRender.drawBox(e.getBoundingBox(), width.getCurrentValueFloat(), color);
					break;
				}
			}
		}
	}

}
