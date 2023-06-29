package volcano.summer.client.modules.player;

import net.minecraft.item.ItemBucketMilk;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import volcano.summer.base.Summer;
import volcano.summer.base.manager.event.Event;
import volcano.summer.base.manager.module.Module;
import volcano.summer.client.events.EventPacketSend;
import volcano.summer.client.events.EventTick;
import volcano.summer.client.value.ClampedValue;
import volcano.summer.client.value.Value;

public class FastUse extends Module {

	public static Value<Boolean> bow;
	public static Value<Double> delay;

	public FastUse() {
		super("FastUse", 0, Category.PLAYER);
		delay = new ClampedValue<Double>("UseDelay", "delay", 13.0, 0.0, 100.0, this);
	}

	@Override
	public void onEnable() {
		Summer.tellPlayer("Food/Potion/milk");
	}

	@Override
	public void onDisable() {

	}

	private boolean isUsable(ItemStack stack) {
		if ((stack.getItem() instanceof ItemFood)) {
			return true;
		}
		if ((stack.getItem() instanceof ItemPotion)) {
			return true;
		}
		if ((stack.getItem() instanceof ItemBucketMilk)) {
			return true;
		}
		return false;
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventTick) {
			if (mc.theWorld != null)
				if ((this.mc.thePlayer.getItemInUseDuration() >= this.delay.getValue().floatValue())
						&& (isUsable(this.mc.thePlayer.getCurrentEquippedItem()))) {
					for (int i = 0; i < 30 * (this.delay.getValue().floatValue() > 5 ? 1
							: this.delay.getValue().floatValue() + 1); i++) {
						this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer());
						mc.thePlayer.sendQueue.addToSendQueue(new C00PacketKeepAlive(i));
					}
					this.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(
							C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(-1, -1, -1), EnumFacing.UP));
					this.mc.thePlayer.stopUsingItem();
				}
		}
		if (event instanceof EventPacketSend) {
			if (mc.theWorld != null) {
				if ((this.mc.thePlayer.isUsingItem())
						&& ((((EventPacketSend) event).getPacket() instanceof C0APacketAnimation))
						&& (isUsable(this.mc.thePlayer.getCurrentEquippedItem()))
						&& (!this.mc.thePlayer.isSwingInProgress)) {
					((EventPacketSend) event).setCancelled(true);
				}
			}
		}
	}
}
