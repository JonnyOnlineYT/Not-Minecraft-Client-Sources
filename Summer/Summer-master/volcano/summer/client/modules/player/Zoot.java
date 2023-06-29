package volcano.summer.client.modules.player;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import volcano.summer.base.manager.event.Event;
import volcano.summer.base.manager.module.Module;
import volcano.summer.client.events.EventPreMotionUpdate;
import volcano.summer.client.util.BlockUtil;

public class Zoot extends Module {

	private boolean potion;

	public Zoot() {
		super("Zoot", 0, Category.PLAYER);
		this.potion = true;
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
			Potion[] potionTypes;
			int length = (potionTypes = Potion.potionTypes).length;
			for (int k = 0; k < length; k++) {
				Potion potion = potionTypes[k];
				if (potion != null) {
					PotionEffect effect = Minecraft.thePlayer.getActivePotionEffect(potion);
					if ((effect != null) && (potion.isBadEffect()) && (this.potion)) {
						for (int i = 0; i < effect.getDuration() / 20; i++) {
							Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
						}
					}
				}
			}
			if ((Minecraft.thePlayer.moveForward == 0.0F) && (Minecraft.thePlayer.moveStrafing == 0.0F)
					&& (Minecraft.thePlayer.isBurning()) && (!BlockUtil.isInLiquid()) && (!BlockUtil.isOnLiquid())) {
				for (int j = 0; j < 50; j++) {
					Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
				}
			}
		}
	}
}
