package ru.wendoxd.modules.impl.movement;

import net.minecraft.init.Items;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.util.EnumHand;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import ru.wendoxd.events.Event;
import ru.wendoxd.events.impl.menu.EventSwapState;
import ru.wendoxd.events.impl.packet.EventSendPacket;
import ru.wendoxd.events.impl.player.EventUpdate;
import ru.wendoxd.modules.Module;
import ru.wendoxd.ui.menu.elements.Frame;
import ru.wendoxd.ui.menu.elements.tabelements.CheckBox;
import ru.wendoxd.ui.menu.elements.tabelements.SelectBox;
import ru.wendoxd.ui.menu.elements.tabelements.Slider;
import ru.wendoxd.ui.menu.utils.MenuAPI;
import ru.wendoxd.utils.misc.InventoryUtils;

public class Flight extends Module {

	private Frame flight_frame = new Frame("Flight");
	public static CheckBox flight = new CheckBox("Flight").markArrayList("Flight");
	public static SelectBox mode = new SelectBox("Mode",
			new String[] { "Vanilla", "Matrix Pearl", "JetMine", "ReallyWorld" }, () -> flight.isEnabled(true));
	public static CheckBox resetmotion = new CheckBox("Reset Motion", () -> flight.isEnabled(true) && mode.get() == 0);
	public static CheckBox autoPearl = new CheckBox("AutoPearl", () -> flight.isEnabled(true) && mode.get() == 1);
	public static Slider boost = new Slider("Boost", 2, 0.1, 6, 0.11, () -> flight.isEnabled(true) && mode.get() == 1);
	public static Slider motion = new Slider("Motion", 2, 0.1, 30, 0.07,
			() -> flight.isEnabled(true) && mode.get() != 3);
	public static Slider speed = new Slider("Speed", 2, 0.1, 2, 0.2,
			() -> flight.isEnabled(true) && (mode.get() == 0 || mode.get() == 2));
	private static long flyTime, teleportMaxTime;

	@Override
	protected void initSettings() {
		flight.markSetting("Flight");
		speed.modifyPath("Speed_3");
		motion.modifyPath("Motion");
		mode.modifyPath("Mode_3");
		flight_frame.register(flight, mode, autoPearl, resetmotion, boost, speed, motion);
		MenuAPI.movement.register(flight_frame);
	}

	@Override
	public void onEvent(Event event) {
		if (flight.isEnabled(false)) {
			if (event instanceof EventUpdate) {
				if (mode.get() == 1) {
					mc.player.capabilities.isFlying = false;
					if (mc.player.hurtTime > 0 && !mc.player.isBurning()) {
						mc.player.isAirBorne = true;
						mc.player.motionY = motion.getFloatValue();
						mc.player.jumpMovementFactor = boost.getFloatValue();
					}
				} else if (mode.get() == 0) {
					mc.player.capabilities.isFlying = true;
					mc.player.capabilities.setFlySpeed(speed.getFloatValue() / 5);
					if (mc.gameSettings.keyBindJump.pressed) {
						mc.player.motionY += motion.getFloatValue();
					} else if (mc.gameSettings.keyBindSneak.pressed) {
						mc.player.motionY -= motion.getFloatValue();
					}
				} else if (mode.get() == 2) {
					mc.player.connection
							.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SPRINTING));
					if (mc.player.onGround) {
						mc.player.jump();
					}
				}
			}
		}
		if (event instanceof EventUpdate && flight.isEnabled(false)) {
			if (mode.get() == 3) {
				if (mc.gameSettings.keyBindJump.isKeyDown()) {
					mc.player.jump();
				}
			}
		}
		if (event instanceof EventSendPacket && flight.isEnabled(false)) {
			if (mode.get() == 2) {
				double radians = Math.toRadians(mc.player.rotationYaw);
				if (((EventSendPacket) event).getPacket() instanceof CPacketPlayer && !mc.player.onGround) {
					CPacketPlayer packet = (CPacketPlayer) ((EventSendPacket) event).getPacket();
					packet.x = mc.player.posX + Math.sin(radians) * speed.getFloatValue();
					packet.y = mc.player.posY - motion.getFloatValue();
					packet.z = mc.player.posZ - Math.cos(radians) * speed.getFloatValue();
				}
			}
		}
		if (event instanceof EventSwapState) {
			if (((EventSwapState) event).getCheckBox() == flight) {
				if (!((EventSwapState) event).getState()) {
					mc.player.capabilities.isFlying = false;
					mc.player.capabilities.setFlySpeed(0.05F);
					if (resetmotion.isEnabled(false)) {
						mc.player.motionX = 0;
						mc.player.motionY = 0;
						mc.player.motionZ = 0;
					}
				} else if (mode.get() == 1 && autoPearl.isEnabled(false)) {
					if (mc.player.getCooldownTracker().getCooldown(Items.ENDER_PEARL, 1) == 0
							&& InventoryUtils.getPearls() >= 0) {
						mc.player.connection.sendPacket(new CPacketHeldItemChange(InventoryUtils.getPearls()));
						mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
						mc.player.connection.sendPacket(new CPacketHeldItemChange(mc.player.inventory.currentItem));
					}
				}
			}
		}
	}
}