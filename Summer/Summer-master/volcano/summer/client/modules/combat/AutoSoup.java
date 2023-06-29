package volcano.summer.client.modules.combat;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import volcano.summer.base.manager.event.Event;
import volcano.summer.base.manager.module.Module;
import volcano.summer.client.events.EventPostMotionUpdate;
import volcano.summer.client.util.TimerUtil;
import volcano.summer.client.value.ClampedValue;
import volcano.summer.client.value.Value;

public class AutoSoup extends Module {
	private TimerUtil time;
	public Value<Double> delay;
	public Value<Double> health;

	public AutoSoup() {
		super("AutoSoup", 0, Category.COMBAT);
		this.delay = new ClampedValue<Double>("SoupDelay", "delay", 500.0, 0.0, 1000.0, this);
		this.health = new ClampedValue<Double>("Health", "health", 3.5, 1.0, 10.0, this);
		this.time = new TimerUtil();
	}

	@Override
	public void onEnable() {

	}

	@Override
	public void onDisable() {

	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventPostMotionUpdate) {
			int soupSlot = getSoupFromInventory();
			if ((this.mc.thePlayer.getHealth() < this.health.getValue().floatValue() * 2.0D)
					&& (this.time.delay(this.delay.getValue().floatValue())) && (soupSlot != -1)) {
				int prevSlot = this.mc.thePlayer.inventory.currentItem;
				if (soupSlot < 9) {
					this.mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(soupSlot));
					this.mc.thePlayer.sendQueue.addToSendQueue(
							new C08PacketPlayerBlockPlacement(this.mc.thePlayer.inventory.getCurrentItem()));
					this.mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(prevSlot));
					this.mc.playerController.syncCurrentPlayItem();
					this.mc.thePlayer.inventory.currentItem = prevSlot;
				} else {
					swap(soupSlot, this.mc.thePlayer.inventory.currentItem
							+ (this.mc.thePlayer.inventory.currentItem < 8 ? 1 : -1));
					this.mc.thePlayer.sendQueue
							.addToSendQueue(new C09PacketHeldItemChange(this.mc.thePlayer.inventory.currentItem
									+ (this.mc.thePlayer.inventory.currentItem < 8 ? 1 : -1)));
					this.mc.thePlayer.sendQueue.addToSendQueue(
							new C08PacketPlayerBlockPlacement(this.mc.thePlayer.inventory.getCurrentItem()));
					this.mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(prevSlot));
				}
				this.time.reset();
			}
		}
	}

	protected void swap(int slot, int hotbarNum) {
		this.mc.playerController.windowClick(this.mc.thePlayer.inventoryContainer.windowId, slot, hotbarNum, 2,
				this.mc.thePlayer);
	}

	private int getSoupFromInventory() {
		for (int i = 0; i < 36; i++) {
			if (this.mc.thePlayer.inventory.mainInventory[i] != null) {
				ItemStack is = this.mc.thePlayer.inventory.mainInventory[i];
				Item item = is.getItem();
				if (Item.getIdFromItem(item) == 282) {
					return i;
				}
			}
		}
		return -1;
	}
}
