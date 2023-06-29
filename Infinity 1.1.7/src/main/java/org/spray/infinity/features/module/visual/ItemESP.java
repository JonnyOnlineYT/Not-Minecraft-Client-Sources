package org.spray.infinity.features.module.visual;

import java.awt.Color;

import org.spray.infinity.event.RenderEvent;
import org.spray.infinity.features.Category;
import org.spray.infinity.features.Module;
import org.spray.infinity.features.ModuleInfo;
import org.spray.infinity.features.Setting;
import org.spray.infinity.utils.Helper;
import org.spray.infinity.utils.render.WorldRender;

import com.darkmagician6.eventapi.EventTarget;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;

@ModuleInfo(category = Category.VISUAL, desc = "Draw item esp", key = -2, name = "ItemESP", visible = true)
public class ItemESP extends Module {

	private Setting color = new Setting(this, "Color", new Color(143, 124, 241));
	private Setting width = new Setting(this, "Width", 1.1f, 0.5f, 3.0f);

	@EventTarget
	public void onWorldRender(RenderEvent event) {
		for (Entity e : Helper.getWorld().getEntities()) {

			if (e instanceof ItemEntity) {
				WorldRender.drawBox(e.getBoundingBox(), width.getCurrentValueFloat(), color.getColor().getRGB());

			}
		}

	}

}
