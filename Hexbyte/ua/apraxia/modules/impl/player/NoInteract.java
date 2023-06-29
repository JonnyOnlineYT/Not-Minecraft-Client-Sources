package ua.apraxia.modules.impl.player;

import net.minecraft.block.Block;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import ua.apraxia.modules.Categories;
import ua.apraxia.modules.Module;
import ua.apraxia.modules.settings.impl.BooleanSetting;

import java.util.ArrayList;
import java.util.List;

public class NoInteract extends Module {
    public static BooleanSetting armorStands = new BooleanSetting("Armor Stand", true);
    public static BooleanSetting chest = new BooleanSetting("Chest", false);
    public static BooleanSetting doors = new BooleanSetting("Doors", true);
    public static BooleanSetting hopper = new BooleanSetting("Hopper", true);
    public static BooleanSetting buttons = new BooleanSetting("Buttons", true);
    public static BooleanSetting dispenser = new BooleanSetting("Dispenser", true);
    public static BooleanSetting  noteBlock = new BooleanSetting("Note Block", true);
    public static BooleanSetting craftingTable = new BooleanSetting("Crafting Table", true);
    public static BooleanSetting trapDoor = new BooleanSetting("TrapDoor", true);
    public static BooleanSetting furnace = new BooleanSetting("Furnace", true);
    public static BooleanSetting gate = new BooleanSetting("Gate", true);
    public static BooleanSetting anvil = new BooleanSetting("Anvil", true);
    public static BooleanSetting lever = new BooleanSetting("Lever", true);


    public NoInteract() {
        super("NoInteract", Categories.Player);
        addSetting(armorStands, chest, doors, hopper, buttons, dispenser, noteBlock, craftingTable, trapDoor, furnace, gate, anvil, lever);
    }

    public static List<Block> getRightClickableBlocks() {
        ArrayList<Block> rightClickableBlocks = new ArrayList<Block>();
        if (doors.value) {
            rightClickableBlocks.add(Block.getBlockById(64));
            rightClickableBlocks.add(Block.getBlockById(71));
            rightClickableBlocks.add(Block.getBlockById(193));
            rightClickableBlocks.add(Block.getBlockById(194));
            rightClickableBlocks.add(Block.getBlockById(195));
            rightClickableBlocks.add(Block.getBlockById(196));
            rightClickableBlocks.add(Block.getBlockById(197));
        }
        if (hopper.value) {
            rightClickableBlocks.add(Block.getBlockById(154));
        }
        if (buttons.value) {
            rightClickableBlocks.add(Block.getBlockById(77));
            rightClickableBlocks.add(Block.getBlockById(143));
        }
        if (noteBlock.value) {
            rightClickableBlocks.add(Block.getBlockById(84));
            rightClickableBlocks.add(Block.getBlockById(25));
        }
        if (trapDoor.value) {
            rightClickableBlocks.add(Block.getBlockById(96));
            rightClickableBlocks.add(Block.getBlockById(167));
        }
        if (furnace.value) {
            rightClickableBlocks.add(Block.getBlockById(61));
            rightClickableBlocks.add(Block.getBlockById(62));
        }
        if (chest.value) {
            rightClickableBlocks.add(Block.getBlockById(130));
            rightClickableBlocks.add(Block.getBlockById(146));
            rightClickableBlocks.add(Block.getBlockById(54));
        }
        if (craftingTable.value) {
            rightClickableBlocks.add(Block.getBlockById(58));
        }
        if (gate.value) {
            rightClickableBlocks.add(Block.getBlockById(107));
            rightClickableBlocks.add(Block.getBlockById(183));
            rightClickableBlocks.add(Block.getBlockById(184));
            rightClickableBlocks.add(Block.getBlockById(185));
            rightClickableBlocks.add(Block.getBlockById(186));
            rightClickableBlocks.add(Block.getBlockById(187));
        }
        if (anvil.value) {
            rightClickableBlocks.add(Block.getBlockById(145));
        }
        if (dispenser.value) {
            rightClickableBlocks.add(Block.getBlockById(23));
        }
        if (lever.value) {
            rightClickableBlocks.add(Block.getBlockById(69));
        }
        return rightClickableBlocks;
    }
}