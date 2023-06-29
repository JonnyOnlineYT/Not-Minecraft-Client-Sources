package volcano.summer.client.modules.movement;

import net.minecraft.network.play.client.C03PacketPlayer;
import volcano.summer.base.Summer;
import volcano.summer.base.manager.event.Event;
import volcano.summer.base.manager.module.Module;
import volcano.summer.client.events.EventPacketSend;
import volcano.summer.client.events.EventStep;
import volcano.summer.client.events.Property;
import volcano.summer.client.util.MovementUtils;
import volcano.summer.client.util.TimerUtil;
import volcano.summer.client.util.Wrapper;

public class StepTEST extends Module {

	private final Property<Boolean> cancelExtraPackets = new Property<>("No More Packets", true);

	private final double[] offsets = { 0.42f, 0.7532f };
	private final float timerWhenStepping = 1.0f / (offsets.length + 1);

	private boolean cancelMorePackets;

	private byte cancelledPackets;

	public static boolean cancelStep;

	public StepTEST() {
		super("StepTEST", 0, Category.MOVEMENT);
	}

	@Override
	public void onEnable() {

	}

	@Override
	public void onDisable() {
		mc.timer.timerSpeed = 1.0f;
	}

	@Override
	public void onEvent(Event event) {

		if (event instanceof EventStep) {
			EventStep eventStep = (EventStep) event;
			if (!Summer.moduleManager.getModule(Speed.class).getState() && !MovementUtils.isInLiquid()
					&& MovementUtils.isOnGround()) {
				if (eventStep.isPre()) {
					eventStep.setStepHeight(cancelStep ? 0.0f : 1.0f);
				} else {
					if (eventStep.getHeightStepped() > 0.6) {
						Summer.tellPlayer("Slowed");
						for (double offset : offsets) {
							Wrapper.sendPacketDirect(
									new C03PacketPlayer.C04PacketPlayerPosition(Wrapper.getPlayer().posX,
											Wrapper.getPlayer().posY + offset * eventStep.getHeightStepped(),
											Wrapper.getPlayer().posZ, Wrapper.getPlayer().onGround));
							

						}
						cancelMorePackets = true;
					}

				}

			}
		} else if (event instanceof EventPacketSend) {
			EventPacketSend e = (EventPacketSend) event;

			if (cancelExtraPackets.getValue() && e.getPacket() instanceof C03PacketPlayer) {
				if (cancelledPackets > 0) {
					cancelMorePackets = false;
					cancelledPackets = 0;
					mc.timer.timerSpeed = 1.0f;
				}

				if (cancelMorePackets) {
					mc.timer.timerSpeed = timerWhenStepping;
					cancelledPackets++;
				}

			}

		}

	}

}
