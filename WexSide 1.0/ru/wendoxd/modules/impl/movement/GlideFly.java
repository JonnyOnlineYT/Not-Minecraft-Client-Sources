package ru.wendoxd.modules.impl.movement;

import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketEntityAction.Action;
import net.minecraft.util.math.Vec3d;
import ru.wendoxd.events.Event;
import ru.wendoxd.events.impl.player.EventMove;
import ru.wendoxd.modules.Module;
import ru.wendoxd.ui.menu.elements.Frame;
import ru.wendoxd.ui.menu.elements.tabelements.CheckBox;
import ru.wendoxd.ui.menu.elements.tabelements.Slider;
import ru.wendoxd.ui.menu.utils.MenuAPI;

public class GlideFly extends Module {
	public Frame frameGlideFly = new Frame("GlideFly");
	public CheckBox glideFly = (CheckBox) new CheckBox("GlideFly").markSetting("GlideFly").markArrayList("GlideFly")
			.markDescription(
					"Для использования этого флая\nвам нужно положить элитры в инвентарь.\nP.S не в слот брони, а в ИНВЕНТАРЬ.\nБесконечно работает на RW/MST.");
	public Slider motionY = new Slider("Y Offset", 2, 0.1, 0.42, 1, () -> glideFly.isEnabled(true));
	public static long lastStartFalling;

	public void initSettings() {
		frameGlideFly.register(glideFly, motionY);
		MenuAPI.movement.register(frameGlideFly);
	}

	public void onEvent(Event event) {
		if (event instanceof EventMove) {
			if (glideFly.isEnabled(false)) {
				int elytra = getSlotIDFromItem(Items.ELYTRA);
				if (elytra == -1) {
					return;
				}
				Vec3d motion = ((EventMove) event).motion();
				if (System.currentTimeMillis() - lastStartFalling > 1000) {
					disabler(elytra);
				}
				if (System.currentTimeMillis() - lastStartFalling < 800 && !mc.player.isSneaking()) {
					motion.yCoord = motionY.getDoubleValue();
				} else {
					motion.yCoord = -0.05;
				}
				mc.player.jump();
				mc.player.motionY = motion.yCoord;
			}
		}
	}

	public static void init() {
		System.out.println("hi");
	}

	public static void disabler(int elytra) {
		if (elytra != -2) {
			mc.playerController.windowClick(0, elytra, 1, ClickType.PICKUP, mc.player);
			mc.playerController.windowClick(0, 6, 1, ClickType.PICKUP, mc.player);
		}
		mc.getConnection().sendPacket(new CPacketEntityAction(mc.player, Action.START_FALL_FLYING));
		mc.getConnection().sendPacket(new CPacketEntityAction(mc.player, Action.START_FALL_FLYING));
		if (elytra != -2) {
			mc.playerController.windowClick(0, 6, 1, ClickType.PICKUP, mc.player);
			mc.playerController.windowClick(0, elytra, 1, ClickType.PICKUP, mc.player);
		}
		lastStartFalling = System.currentTimeMillis();
	}

	public static int getSlotIDFromItem(Item item) {
		for (ItemStack stack : mc.player.getArmorInventoryList()) {
			if (stack.getItem() == item) {
				return -2;
			}
		}
		int slot = -1;
		for (int i = 0; i < 36; i++) {
			ItemStack s = mc.player.inventory.getStackInSlot(i);
			if (s.getItem() == item) {
				slot = i;
				break;
			}
		}
		if (slot < 9 && slot != -1) {
			slot = slot + 36;
		}
		return slot;
	}
}
