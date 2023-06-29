package org.spray.infinity.features.module.visual;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;

import org.spray.infinity.event.RenderEvent;
import org.spray.infinity.features.Category;
import org.spray.infinity.features.Module;
import org.spray.infinity.features.ModuleInfo;
import org.spray.infinity.features.Setting;
import org.spray.infinity.utils.block.BlockUtil;
import org.spray.infinity.utils.render.WorldRender;

import com.darkmagician6.eventapi.EventTarget;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.Box;

@ModuleInfo(category = Category.VISUAL, desc = "Draw tile entity esp", key = -2, name = "StorageESP", visible = true)
public class StorageESP extends Module {

	private Setting mode = new Setting(this, "Mode", "Fill", new ArrayList<>(Arrays.asList("Fill", "Box")));

	private Setting alpha = new Setting(this, "Opacity", 50D, 1D, 100D)
			.setVisible(() -> mode.getCurrentMode().equalsIgnoreCase("Fill"));

	private Setting chest = new Setting(this, "Chests", true).setColor(new Color(239, 244, 126));

	private Setting enderChest = new Setting(this, "EnderChest", true).setColor(new Color(229, 143, 223));
	private Setting shulkers = new Setting(this, "Shulkers", true).setColor(new Color(143, 229, 209));
	private Setting spawners = new Setting(this, "Spawners", false).setColor(new Color(143, 243, 123));

	@EventTarget
	public void onWorldRender(RenderEvent event) {
		for (BlockEntity blockEntity : BlockUtil.getRenderBlocks(chest.isToggle(), enderChest.isToggle(),
				spawners.isToggle(), shulkers.isToggle())) {

			int color = BlockUtil.getBlockEntitiesColor(blockEntity, chest.getColor().getRGB(),
					enderChest.getColor().getRGB(), shulkers.getColor().getRGB(), spawners.getColor().getRGB());

			Box box = new Box(blockEntity.getPos());

			if (mode.getCurrentMode().equalsIgnoreCase("Fill")) {
				WorldRender.drawFill(box, alpha.getCurrentValueDouble(), color);
			} else if (mode.getCurrentMode().equalsIgnoreCase("Box")) {
				WorldRender.drawBox(box, 1.5f, color);
			}

		}
	}

}
