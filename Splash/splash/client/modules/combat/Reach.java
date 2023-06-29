package splash.client.modules.combat;

import me.hippo.systems.lwjeb.annotation.Collect;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C02PacketUseEntity.Action;
import splash.api.module.Module;
import splash.api.module.category.ModuleCategory;
import splash.api.value.impl.BooleanValue;
import splash.api.value.impl.NumberValue;
import splash.client.events.network.EventPacketSend;

public class Reach extends Module {

	public NumberValue<Double> maxReach = new NumberValue<>("Max reach", 3.0, 3.0, 5.0, this);
	public NumberValue<Double> minReach = new NumberValue<>("Min reach", 3.0, 3.0, 5.0, this);
	public double currentReach;
	public boolean reverse;
    public Reach() {
        super("Reach", "Adds to your vanilla reach", ModuleCategory.COMBAT);
    }

    @Collect
    public void eventPacketSend(EventPacketSend e) {
    	if (e.getSentPacket() instanceof C02PacketUseEntity) {
    		C02PacketUseEntity packet = (C02PacketUseEntity)e.getSentPacket();
    		if (packet.getAction().equals(Action.ATTACK)) {
    		}
    	}
    	if (e.getSentPacket() instanceof C03PacketPlayer) {
			currentReach += reverse ? -.005 : .005;
        		
			if (currentReach >= maxReach.getValue()) 
				reverse = true;
			
			else if (currentReach <= minReach.getValue()) reverse = false;
    	}
    }
}
