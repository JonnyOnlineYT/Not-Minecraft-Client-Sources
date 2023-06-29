package volcano.summer.client.modules.player;

import net.minecraft.client.Minecraft;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.ItemStack;
import volcano.summer.base.manager.event.Event;
import volcano.summer.base.manager.module.Module;
import volcano.summer.client.events.EventPreMotionUpdate;
import volcano.summer.client.util.TimerUtil;
import volcano.summer.client.value.ClampedValue;
import volcano.summer.client.value.Value;

public class ChestSteal extends Module {

	TimerUtil timer = new TimerUtil();
	public static Value<Double> delay;
	public static Value<Boolean> silent;
	public static Value<Boolean> AutoDisable;

	public ChestSteal() {
		super("ChestSteal", 0, Category.PLAYER);
		delay = new ClampedValue<Double>("StealDealy", "delay", 60.0, 0.0, 1000.0, this);
		silent = new Value("Silent", "silent", Boolean.valueOf(true), this);
		AutoDisable = new Value<Boolean>("AutoDisable", "autodisable", true, this);
	}

	@Override
	public void onEnable() {

	}

	@Override
	public void onDisable() {

	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventPreMotionUpdate) {
			timer.updateMs();
			setDisplayName("" + this.delay.getValue());
			if ((mc.thePlayer.openContainer != null) && ((mc.thePlayer.openContainer instanceof ContainerChest))) {
				ContainerChest container = (ContainerChest) mc.thePlayer.openContainer;
				if (container.getInventory().isEmpty()) {
					mc.displayGuiScreen(null);
				}
				if (isContainerEmpty(mc.thePlayer.openContainer) || isInventoryFull()) {
					mc.thePlayer.closeScreen();
				}
				for (int i = 0; i < container.getLowerChestInventory().getSizeInventory(); i++) {
					if ((container.getLowerChestInventory().getStackInSlot(i) != null)
							&& (container.getLowerChestInventory().getName().contains("Chest"))
							&& (timer.hasTimePassedM(this.delay.getValue().longValue()))) {
						mc.playerController.windowClick(container.windowId, i, 0, 1, mc.thePlayer);
						timer.setLastMs();
					}
					if ((!this.isContainerEmpty(Minecraft.thePlayer.openContainer))
							&& ((Minecraft.thePlayer.openContainer instanceof ContainerChest))) {
						if (this.silent.getValue()) {
							mc.setIngameFocus();
						}
					}

				}
			}
		}
	}

	public boolean isContainerEmpty(Container container) {
		boolean temp = true;
		int i = 0;
		int slotAmount = container.inventorySlots.size() == 90 ? 54 : 27;
		while (i < slotAmount) {
			if (container.getSlot(i).getHasStack()) {
				temp = false;
			}
			i++;
		}
		return temp;
	}

	public boolean isInventoryFull() {
		for (int index = 9; index <= 44; index++) {
			ItemStack stack = this.mc.thePlayer.inventoryContainer.getSlot(index).getStack();
			if (stack == null) {
				return false;
			}
		}
		return true;
	}
}
