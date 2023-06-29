package volcano.summer.client.modules.movement;

import net.minecraft.network.play.client.C0DPacketCloseWindow;
import volcano.summer.base.manager.event.Event;
import volcano.summer.base.manager.module.Module;
import volcano.summer.client.events.EventPacketSend;
import volcano.summer.client.value.Value;

public class InventoryMove extends Module {

	public final Value<Boolean> xcarry = new Value("XCarry", "XCarry", Boolean.valueOf(false), this);

	public InventoryMove() {
		super("Inventory", 0, Category.MOVEMENT);
	}

	@Override
	public void onEnable() {

	}

	@Override
	public void onDisable() {

	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventPacketSend) {
			if (this.xcarry.getValue()) {
				if (((EventPacketSend) event).getPacket() instanceof C0DPacketCloseWindow) {
					((EventPacketSend) event).setCancelled(true);
				}
			}
		}
	}
}
