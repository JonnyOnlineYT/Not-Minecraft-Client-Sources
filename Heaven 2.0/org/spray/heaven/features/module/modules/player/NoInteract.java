package org.spray.heaven.features.module.modules.player;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.spray.heaven.features.module.Category;
import org.spray.heaven.features.module.Module;
import org.spray.heaven.features.module.ModuleInfo;
import org.spray.heaven.features.module.Setting;

import net.minecraft.block.Block;

@ModuleInfo(name = "NoInteract", category = Category.PLAYER, visible = true, key = Keyboard.KEY_NONE)
public class NoInteract extends Module {

	public final Setting armorStands = register(new Setting("Armor Stand", true));
	private final Setting chest = register(new Setting("Chest", false));
	private final Setting doors = register(new Setting("Doors", true));
	private final Setting hopper = register(new Setting("Hopper", true));
	private final Setting buttons = register(new Setting("Buttons", true));
	private final Setting dispenser = register(new Setting("Dispenser", true));
	private final Setting noteBlock = register(new Setting("NoteBlock", false));
	private final Setting craftingTable = register(new Setting("Crafting Table", true));
	private final Setting trapDoor = register(new Setting("Trap Door", true));
	private final Setting furnace = register(new Setting("Furnace", true));
	private final Setting gate = register(new Setting("Gate", true));
	private final Setting anvil = register(new Setting("Anvil", true));
	private final Setting lever = register(new Setting("Lever", true));

	public List<Block> getRightClickableBlocks() {
		ArrayList<Block> rightClickableBlocks = new ArrayList<Block>();
		if (doors.isToggle()) {
			rightClickableBlocks.add(Block.getBlockById(64));
			rightClickableBlocks.add(Block.getBlockById(71));
			rightClickableBlocks.add(Block.getBlockById(193));
			rightClickableBlocks.add(Block.getBlockById(194));
			rightClickableBlocks.add(Block.getBlockById(195));
			rightClickableBlocks.add(Block.getBlockById(196));
			rightClickableBlocks.add(Block.getBlockById(197));
		}
		if (hopper.isToggle()) {
			rightClickableBlocks.add(Block.getBlockById(154));
		}
		if (buttons.isToggle()) {
			rightClickableBlocks.add(Block.getBlockById(77));
			rightClickableBlocks.add(Block.getBlockById(143));
		}
		if (noteBlock.isToggle()) {
			rightClickableBlocks.add(Block.getBlockById(84));
			rightClickableBlocks.add(Block.getBlockById(25));
		}
		if (trapDoor.isToggle()) {
			rightClickableBlocks.add(Block.getBlockById(96));
			rightClickableBlocks.add(Block.getBlockById(167));
		}
		if (furnace.isToggle()) {
			rightClickableBlocks.add(Block.getBlockById(61));
			rightClickableBlocks.add(Block.getBlockById(62));
		}
		if (chest.isToggle()) {
			rightClickableBlocks.add(Block.getBlockById(130));
			rightClickableBlocks.add(Block.getBlockById(146));
			rightClickableBlocks.add(Block.getBlockById(54));
		}
		if (craftingTable.isToggle()) {
			rightClickableBlocks.add(Block.getBlockById(58));
		}
		if (gate.isToggle()) {
			rightClickableBlocks.add(Block.getBlockById(107));
			rightClickableBlocks.add(Block.getBlockById(183));
			rightClickableBlocks.add(Block.getBlockById(184));
			rightClickableBlocks.add(Block.getBlockById(185));
			rightClickableBlocks.add(Block.getBlockById(186));
			rightClickableBlocks.add(Block.getBlockById(187));
		}
		if (anvil.isToggle()) {
			rightClickableBlocks.add(Block.getBlockById(145));
		}
		if (dispenser.isToggle()) {
			rightClickableBlocks.add(Block.getBlockById(23));
		}
		if (lever.isToggle()) {
			rightClickableBlocks.add(Block.getBlockById(69));
		}
		return rightClickableBlocks;
	}

}