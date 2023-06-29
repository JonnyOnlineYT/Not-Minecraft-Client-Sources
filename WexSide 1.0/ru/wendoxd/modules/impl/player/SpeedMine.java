package ru.wendoxd.modules.impl.player;

import net.minecraft.init.MobEffects;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;
import ru.wendoxd.events.Event;
import ru.wendoxd.events.impl.block.EventInteractBlock;
import ru.wendoxd.events.impl.menu.EventSwapState;
import ru.wendoxd.modules.Module;
import ru.wendoxd.ui.menu.elements.Frame;
import ru.wendoxd.ui.menu.elements.tabelements.CheckBox;
import ru.wendoxd.ui.menu.elements.tabelements.SelectBox;
import ru.wendoxd.ui.menu.elements.tabelements.Slider;
import ru.wendoxd.ui.menu.utils.MenuAPI;

public class SpeedMine extends Module {

	private Frame speedmine_frame = new Frame("SpeedMine");
	private final CheckBox speedmine = new CheckBox("SpeedMine").markArrayList("SpeedMine");
	private final SelectBox speedmine_mode = new SelectBox("Mode", new String[] { "Packet", "Damage", "Potion" },
			() -> speedmine.isEnabled(true));
	private final Slider speedmine_damage = new Slider("Damage", 2, 0.7F, 4, 0.2,
			() -> speedmine.isEnabled(true) && speedmine_mode.get() == 1);

	@Override
	protected void initSettings() {
		speedmine.markSetting("SpeedMine");
		speedmine_frame.register(speedmine, speedmine_mode, speedmine_damage);
		MenuAPI.player.register(speedmine_frame);
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventInteractBlock && speedmine.isEnabled(false)) {
			if (speedmine_mode.get() == 2) {
				mc.player.addPotionEffect(new PotionEffect(MobEffects.HASTE, 817, 1));
			} else if (speedmine_mode.get() == 1) {
				if (mc.playerController.curBlockDamageMP >= 0.7) {
					mc.playerController.curBlockDamageMP = speedmine_damage.getFloatValue();
				}
			} else if (speedmine_mode.get() == 0) {
				mc.player.swingArm(EnumHand.MAIN_HAND);
				mc.player.connection
						.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK,
								((EventInteractBlock) event).getPos(), ((EventInteractBlock) event).getFace()));
				mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK,
						((EventInteractBlock) event).getPos(), ((EventInteractBlock) event).getFace()));
				((EventInteractBlock) event).setCanceled();
			}
		}
		if (event instanceof EventSwapState) {
			if (((EventSwapState) event).getCheckBox() == speedmine) {
				if (!((EventSwapState) event).getState()) {
					mc.player.removeActivePotionEffect(MobEffects.HASTE);
				}
			}
		}
	}
}
