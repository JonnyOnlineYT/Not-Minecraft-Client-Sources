package ru.wendoxd.modules.impl.player;

import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.*;
import ru.wendoxd.events.Event;
import ru.wendoxd.events.impl.player.EventUpdate;
import ru.wendoxd.modules.Module;
import ru.wendoxd.ui.menu.elements.Frame;
import ru.wendoxd.ui.menu.elements.tabelements.CheckBox;
import ru.wendoxd.ui.menu.elements.tabelements.Slider;
import ru.wendoxd.ui.menu.utils.MenuAPI;
import ru.wendoxd.utils.misc.TimerUtils;

public class ChestStealer extends Module {

	private Frame cheststealer_frame = new Frame("ChestStealer");
	private final CheckBox cheststealer = new CheckBox("ChestStealer").markArrayList("ChestStealer");
	private final Slider delay = new Slider("Delay", 1, 1, 400, 0.248, () -> cheststealer.isEnabled(false));
	public TimerUtils timerUtils = new TimerUtils();

	@Override
	protected void initSettings() {
		cheststealer.markSetting("ChestStealer");
		delay.modifyPath("Delay_2");
		cheststealer_frame.register(cheststealer, delay);
		MenuAPI.player.register(cheststealer_frame);
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventUpdate && cheststealer.isEnabled(false)) {
			if (mc.player.openContainer instanceof ContainerChest) {
				ContainerChest container = (ContainerChest) mc.player.openContainer;
				for (int index = 0; index < container.inventorySlots.size(); ++index) {
					if (container.getLowerChestInventory().getStackInSlot(index).getItem() != Item.getItemById(0)
							&& timerUtils.hasReached(delay.getFloatValue())) {
						mc.playerController.windowClick(container.windowId, index, 0, ClickType.QUICK_MOVE, mc.player);
						timerUtils.reset();
						continue;
					}

					if (!isEmpty(container))
						continue;

					mc.player.closeScreen();
				}
			}
		}
	}

	public boolean isWhiteListItem(ItemStack itemStack) {
		return (itemStack.getItem() instanceof ItemArmor || itemStack.getItem() instanceof ItemEnderPearl
				|| itemStack.getItem() instanceof ItemSword || itemStack.getItem() instanceof ItemTool
				|| itemStack.getItem() instanceof ItemFood || itemStack.getItem() instanceof ItemPotion
				|| itemStack.getItem() instanceof ItemBlock || itemStack.getItem() instanceof ItemArrow
				|| itemStack.getItem() instanceof ItemCompass);
	}

	private boolean isEmpty(Container container) {
		for (int index = 0; index < container.inventorySlots.size(); index++) {
			if (isWhiteListItem(container.getSlot(index).getStack())) {
				return false;
			}
		}
		return true;
	}
}
