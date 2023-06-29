package ru.wendoxd.modules.impl.combat;

import net.minecraft.item.ItemBow;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumHand;
import ru.wendoxd.events.Event;
import ru.wendoxd.events.impl.packet.EventSendPacket;
import ru.wendoxd.modules.Module;
import ru.wendoxd.ui.menu.elements.Frame;
import ru.wendoxd.ui.menu.elements.tabelements.CheckBox;
import ru.wendoxd.ui.menu.elements.tabelements.Slider;
import ru.wendoxd.ui.menu.utils.MenuAPI;

public class SuperBow extends Module {

	private Frame superbow_frame = new Frame("SuperBow");
	private final CheckBox superbow = new CheckBox("SuperBow").markArrayList("SuperBow");
	private final Slider power = new Slider("Power", 0, 1, 100, 0.8, () -> superbow.isEnabled(true));

	@Override
	protected void initSettings() {
		superbow.markSetting("SuperBow");
		superbow_frame.register(superbow, power);
		MenuAPI.combat.register(superbow_frame);
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventSendPacket && superbow.isEnabled(false)) {
			if (((EventSendPacket) event).getPacket() instanceof CPacketPlayerDigging) {
				CPacketPlayerDigging packet = (CPacketPlayerDigging) ((EventSendPacket) event).getPacket();
				if (packet.getAction() == CPacketPlayerDigging.Action.RELEASE_USE_ITEM
						&& mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemBow) {
					mc.player.connection
							.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SPRINTING));
					for (int i = 0; i < power.getFloatValue(); i++) {
						mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX,
								mc.player.posY + 1.0E-10, mc.player.posZ, false));
						mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX,
								mc.player.posY - 1.0E-10, mc.player.posZ, true));
					}
				}
			}
		}
	}
}
