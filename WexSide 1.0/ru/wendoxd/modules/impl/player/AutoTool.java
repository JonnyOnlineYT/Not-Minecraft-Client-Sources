package ru.wendoxd.modules.impl.player;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import ru.wendoxd.events.Event;
import ru.wendoxd.events.impl.block.EventInteractBlock;
import ru.wendoxd.events.impl.player.EventUpdate;
import ru.wendoxd.modules.Module;
import ru.wendoxd.ui.menu.elements.Frame;
import ru.wendoxd.ui.menu.elements.tabelements.CheckBox;
import ru.wendoxd.ui.menu.utils.MenuAPI;

public class AutoTool extends Module {

	private Frame autotool_frame = new Frame("AutoTool");
	private final CheckBox autotool = new CheckBox("AutoTool").markArrayList("AutoTool");
	private int backUp = -1, delay;

	@Override
	protected void initSettings() {
		autotool.markSetting("AutoTool");
		autotool_frame.register(autotool);
		MenuAPI.player.register(autotool_frame);
		ItemRenderer.class.getClass();
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventInteractBlock && autotool.isEnabled(false)) {
			try {
				int bestSlot = -1;
				float bestSpeed = 1;
				Block block = mc.world.getBlockState(mc.objectMouseOver.getBlockPos()).getBlock();
				for (int i = 0; i < 9; i++) {
					ItemStack item = mc.player.inventory.getStackInSlot(i);
					float speed = item.getStrVsBlock(block.getDefaultState());
					if (speed > bestSpeed) {
						bestSpeed = speed;
						bestSlot = i;
					}
				}
				if (bestSlot != -1) {
					if (mc.player.inventory.currentItem != bestSlot) {
						this.delay = 10;
						if (backUp == -1)
							backUp = mc.player.inventory.currentItem;
						mc.player.inventory.currentItem = bestSlot;
					}
					return;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (event instanceof EventUpdate && autotool.isEnabled(false)) {
			try {
				if (delay > 0) {
					delay--;
					return;
				}
				if (mc.objectMouseOver != null && mc.objectMouseOver.getBlockPos() != null) {
					int bestSlot = -1;
					float bestSpeed = 1;
					Block block = mc.world.getBlockState(mc.objectMouseOver.getBlockPos()).getBlock();
					for (int i = 0; i < 9; i++) {
						ItemStack item = mc.player.inventory.getStackInSlot(i);
						float speed = item.getStrVsBlock(block.getDefaultState());
						if (speed > bestSpeed) {
							bestSpeed = speed;
							bestSlot = i;
						}
					}
					if (bestSlot != -1) {
						return;
					}
					if (backUp != -1) {
						mc.getConnection()
								.sendPacket(new CPacketHeldItemChange(mc.player.inventory.currentItem = backUp));
						backUp = -1;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
