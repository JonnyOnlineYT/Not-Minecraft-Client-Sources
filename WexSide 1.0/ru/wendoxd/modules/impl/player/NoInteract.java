package ru.wendoxd.modules.impl.player;

import net.minecraft.block.Block;
import ru.wendoxd.events.Event;
import ru.wendoxd.events.impl.block.EventCollideBlock;
import ru.wendoxd.modules.Module;
import ru.wendoxd.modules.impl.movement.Spider;
import ru.wendoxd.ui.menu.elements.Frame;
import ru.wendoxd.ui.menu.elements.tabelements.CheckBox;
import ru.wendoxd.ui.menu.elements.tabelements.MultiSelectBox;
import ru.wendoxd.ui.menu.utils.MenuAPI;

import java.util.ArrayList;

public class NoInteract extends Module {

	private Frame nointeract_frame = new Frame("NoInteract");
	private final CheckBox nointeract = new CheckBox("NoInteract").markArrayList("NoInteract");
	private final MultiSelectBox blocks = new MultiSelectBox(
			"Blocks", new String[] { "CraftTable", "Stand", "Door", "Hopper", "Buttons", "NoteBlocks", "TrapDoor",
					"Furnace", "Chest", "Gate", "Anvil", "Dispenser", "Lever", "Sign" },
			() -> nointeract.isEnabled(true));

	@Override
	protected void initSettings() {
		nointeract.markSetting("NoInteract");
		nointeract_frame.register(nointeract, blocks);
		MenuAPI.player.register(nointeract_frame);
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventCollideBlock && nointeract.isEnabled(false) && !Spider.spider.isEnabled(false)) {
			Block state = ((EventCollideBlock) event).getBlock();
			for (Block block : getBlocks()) {
				if (state == block) {
					((EventCollideBlock) event).setCanceled();
				}
			}
		}
	}

	private ArrayList<Block> getBlocks() {
		ArrayList<Block> blockList = new ArrayList<>();
		if (blocks.get(0)) {
			blockList.add(Block.getBlockById(58));
		}
		if (blocks.get(1)) {
			blockList.add(Block.getBlockById(63));
		}
		if (blocks.get(2)) {
			blockList.add(Block.getBlockById(64));
			blockList.add(Block.getBlockById(71));
			blockList.add(Block.getBlockById(193));
			blockList.add(Block.getBlockById(194));
			blockList.add(Block.getBlockById(195));
			blockList.add(Block.getBlockById(196));
			blockList.add(Block.getBlockById(197));
		}
		if (blocks.get(3)) {
			blockList.add(Block.getBlockById(154));
		}
		if (blocks.get(4)) {
			blockList.add(Block.getBlockById(77));
			blockList.add(Block.getBlockById(143));
		}
		if (blocks.get(5)) {
			blockList.add(Block.getBlockById(84));
			blockList.add(Block.getBlockById(25));
		}
		if (blocks.get(6)) {
			blockList.add(Block.getBlockById(96));
			blockList.add(Block.getBlockById(167));
		}
		if (blocks.get(7)) {
			blockList.add(Block.getBlockById(61));
			blockList.add(Block.getBlockById(62));
		}
		if (blocks.get(8)) {
			blockList.add(Block.getBlockById(130));
			blockList.add(Block.getBlockById(146));
			blockList.add(Block.getBlockById(54));
		}
		if (blocks.get(9)) {
			blockList.add(Block.getBlockById(107));
			blockList.add(Block.getBlockById(183));
			blockList.add(Block.getBlockById(184));
			blockList.add(Block.getBlockById(185));
			blockList.add(Block.getBlockById(186));
			blockList.add(Block.getBlockById(187));
		}
		if (blocks.get(10)) {
			blockList.add(Block.getBlockById(145));
		}
		if (blocks.get(11)) {
			blockList.add(Block.getBlockById(23));
		}
		if (blocks.get(12)) {
			blockList.add(Block.getBlockById(69));
		}
		if (blocks.get(13)) {
			blockList.add(Block.getBlockById(63));
			blockList.add(Block.getBlockById(436));
			blockList.add(Block.getBlockById(441));
			blockList.add(Block.getBlockById(443));
			blockList.add(Block.getBlockById(445));
			blockList.add(Block.getBlockById(447));
		}
		return blockList;
	}

}
