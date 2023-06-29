package volcano.summer.client.modules.misc;

import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import volcano.summer.base.manager.event.Event;
import volcano.summer.base.manager.module.Module;
import volcano.summer.client.events.EventPreMotionUpdate;
import volcano.summer.client.events.EventUpdate;
import volcano.summer.client.util.BlockUtil;
import volcano.summer.client.value.ClampedValue;
import volcano.summer.client.value.ModeValue;
import volcano.summer.client.value.Value;

public class Regen extends Module {

	public static ModeValue regenMode;
	public static Value<Double> multiplier;

	public Regen() {
		super("Regen", 0, Category.MISC);
		regenMode = new ModeValue("RegenMode", "Mode", "Potion", new String[] { "Potion", "Mineplex" }, this);
		multiplier = new ClampedValue<Double>("Multiplier", "multiplier", 1.0, 0.0, 3.0, this);
	}

	@Override
	public void onEnable() {
	}

	@Override
	public void onDisable() {

	}

	@Override
	public void onEvent(Event event) {
		if (this.regenMode.getStringValue().equalsIgnoreCase("Mineplex")) {
			setDisplayName("Mineplex");
		}
		if (this.regenMode.getStringValue().equalsIgnoreCase("Potion")) {
			setDisplayName("Potion");
		} else {
			setDisplayName("");
		}
		if (event instanceof EventUpdate) {
			if (this.regenMode.getStringValue().equalsIgnoreCase("Mineplex")) {
				if ((!mc.thePlayer.capabilities.isCreativeMode) && (mc.thePlayer.getFoodStats().getFoodLevel() > 17)
						&& (mc.thePlayer.getHealth() < 20.0F) && (mc.thePlayer.getHealth() != 0.0F)
						&& (mc.thePlayer.onGround)) {
					for (int i = 0; i < 250; i++) {
						mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer());
					}
				}
			}
		}
		if ((event instanceof EventPreMotionUpdate)) {
			if ((this.mc.thePlayer.getActivePotionEffect(Potion.regeneration) != null)
					&& (this.regenMode.getStringValue().equalsIgnoreCase("Potion"))
					&& ((this.mc.thePlayer.onGround) || (BlockUtil.isOnLadder()) || (BlockUtil.isInLiquid())
							|| (BlockUtil.isOnLiquid()))
					&& (this.mc.thePlayer.getHealth() < this.mc.thePlayer.getMaxHealth())) {
				for (int i = 0; i < this.mc.thePlayer.getMaxHealth() * (this.multiplier.getValue().floatValue())
						- this.mc.thePlayer.getHealth() * (this.multiplier.getValue()).floatValue(); i++) {
					this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(this.mc.thePlayer.onGround));
				}
			}
		}
	}
}