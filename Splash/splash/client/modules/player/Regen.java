package splash.client.modules.player;

import me.hippo.systems.lwjeb.annotation.Collect;
import net.minecraft.network.play.client.C03PacketPlayer;
import splash.api.module.Module;
import splash.api.module.category.ModuleCategory;
import splash.api.value.impl.NumberValue;
import splash.client.events.player.EventPlayerUpdate;

public class Regen extends Module {
	
	public NumberValue<Integer> health = new NumberValue<>("Health", 5, 1, 20, this);
	public NumberValue<Integer> packets = new NumberValue<>("Packets", 500, 5, 1000, this);

	public Regen() {
		super("Regen", "Heals you.", ModuleCategory.PLAYER);
	}
	
	@Collect
	public void onUpdate(EventPlayerUpdate eventPlayerUpdate) {
		if (mc.thePlayer.getHealth() < health.getValue()) {
			for (int i = 0; i < packets.getValue(); i++) {
				mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(
						mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
			
			}
			
		}
	}

}
