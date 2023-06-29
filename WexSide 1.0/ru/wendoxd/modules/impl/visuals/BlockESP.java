package ru.wendoxd.modules.impl.visuals;

import net.minecraft.tileentity.*;
import net.minecraft.util.math.BlockPos;
import ru.wendoxd.events.Event;
import ru.wendoxd.events.impl.render.EventRender3D;
import ru.wendoxd.modules.Module;
import ru.wendoxd.ui.menu.elements.Frame;
import ru.wendoxd.ui.menu.elements.tabelements.CheckBox;
import ru.wendoxd.ui.menu.elements.tabelements.ColorPicker;
import ru.wendoxd.ui.menu.elements.tabelements.MultiSelectBox;
import ru.wendoxd.ui.menu.utils.MenuAPI;
import ru.wendoxd.utils.visual.RenderUtils;

public class BlockESP extends Module {

	private Frame blockesp_frame = new Frame("BlockESP");
	private final CheckBox blockesp = new CheckBox("BlockESP").markArrayList("BlockESP");
	private final MultiSelectBox blocks = new MultiSelectBox("Blocks",
			new String[] { "Chest", "Ender Chest", "Spawner", "Shulker", "Bed" }, () -> blockesp.isEnabled(true));
	private final ColorPicker chestColor = new ColorPicker("Chest Color",
			() -> blocks.get(0) && blockesp.isEnabled(true));
	private final ColorPicker enderChestColor = new ColorPicker("Ender Chest Color",
			() -> blocks.get(1) && blockesp.isEnabled(true));
	private final ColorPicker spawnerColor = new ColorPicker("Spawner Color",
			() -> blocks.get(2) && blockesp.isEnabled(true));
	private final ColorPicker shulkerColor = new ColorPicker("Shulker Color",
			() -> blocks.get(3) && blockesp.isEnabled(true));
	private final ColorPicker bedColor = new ColorPicker("Bed Color", () -> blocks.get(4) && blockesp.isEnabled(true));

	@Override
	protected void initSettings() {
		blockesp_frame.register(blockesp.markSetting("BlockESP"), blocks, chestColor, enderChestColor, spawnerColor,
				shulkerColor, bedColor);
		MenuAPI.visuals.register(blockesp_frame);
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventRender3D && blockesp.isEnabled(false)) {
			for (TileEntity entity : mc.world.loadedTileEntityList) {
				BlockPos pos = entity.getPos();
				if (entity instanceof TileEntityChest && blocks.get(0)) {
					RenderUtils.drawBlockBox(pos, chestColor.getColor().getRed(), chestColor.getColor().getGreen(),
							chestColor.getColor().getBlue(), chestColor.getColor().getAlpha());
				} else if (entity instanceof TileEntityEnderChest && blocks.get(1)) {
					RenderUtils.drawBlockBox(pos, enderChestColor.getColor().getRed(),
							enderChestColor.getColor().getGreen(), enderChestColor.getColor().getBlue(),
							enderChestColor.getColor().getAlpha());
				} else if (entity instanceof TileEntityBed && blocks.get(4)) {
					RenderUtils.drawBlockBox(pos, bedColor.getColor().getRed(), bedColor.getColor().getGreen(),
							bedColor.getColor().getBlue(), bedColor.getColor().getAlpha());
				} else if (entity instanceof TileEntityShulkerBox && blocks.get(3)) {
					RenderUtils.drawBlockBox(pos, shulkerColor.getColor().getRed(), shulkerColor.getColor().getGreen(),
							shulkerColor.getColor().getBlue(), shulkerColor.getColor().getAlpha());
				} else if (entity instanceof TileEntityMobSpawner && blocks.get(2)) {
					RenderUtils.drawBlockBox(pos, spawnerColor.getColor().getRed(), spawnerColor.getColor().getGreen(),
							spawnerColor.getColor().getBlue(), spawnerColor.getColor().getAlpha());
				}
			}
		}
	}
}
