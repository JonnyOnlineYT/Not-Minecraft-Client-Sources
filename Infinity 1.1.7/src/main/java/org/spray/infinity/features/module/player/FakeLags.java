package org.spray.infinity.features.module.player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.spray.infinity.event.PacketEvent;
import org.spray.infinity.features.Category;
import org.spray.infinity.features.Module;
import org.spray.infinity.features.ModuleInfo;
import org.spray.infinity.features.Setting;
import org.spray.infinity.utils.Helper;

import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.types.EventType;

import net.minecraft.network.Packet;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

@ModuleInfo(category = Category.PLAYER, desc = "Creates fake lags in movements", key = -2, name = "FakeLags", visible = true)
public class FakeLags extends Module {

	private Setting mode = new Setting(this, "Mode", "Pulse",
			new ArrayList<>(Arrays.asList("Pulse", "Always", "Legit")));

	private Setting pulseDelay = new Setting(this, "Pulse Delay", 8, 0.0, 30.0)
			.setVisible(() -> mode.getCurrentMode().equalsIgnoreCase("Pulse"));

	private Setting delay = new Setting(this, "Delay", 0.2, 0.0, 30.0)
			.setVisible(() -> mode.getCurrentMode().equalsIgnoreCase("Always"));

	private Setting legitDelay = new Setting(this, "Delay", 2.1, 0.0, 30.0)
			.setVisible(() -> mode.getCurrentMode().equalsIgnoreCase("Legit"));

	// legit
	private double legitTicks;

	// pulse
	private List<Packet<?>> packetList;
	private double pulseTicks;

	// always
	private PlayerMoveC2SPacket.Full bothPacket;
	private double sendTimer;
	private boolean sendPackets;
	private double ticks;

	@Override
	public void onDisable() {
		sendPackets();
	}

	@Override
	public void onUpdate() {
		setSuffix(mode.getCurrentMode());

		if (mode.getCurrentMode().equalsIgnoreCase("Pulse")) {
			if (Helper.getPlayer().isDead())
				return;

			if (pulseTicks >= pulseDelay.getCurrentValueDouble()) {
				try {
					for (Packet<?> unsentPacket : this.packetList)
						Helper.sendPacket(unsentPacket);
					packetList.clear();
					pulseTicks = 0;
				} catch (Exception exception) {
				}
			}
		} else if (mode.getCurrentMode().equalsIgnoreCase("Legit")) {
			if (Helper.getPlayer().isDead() || Helper.MC.isInSingleplayer())
				return;

			if (legitTicks >= legitDelay.getCurrentValueDouble()) {
				try {
					for (Packet<?> unsentPacket : this.packetList)
						Helper.sendPacket(unsentPacket);
				} catch (Exception exception) {
				}
				packetList.clear();
				legitTicks = 0;
			}
		}

		if (sendPackets) {
			if (sendTimer <= 0) {
				sendPackets = false;

				if (bothPacket == null) {
					ticks = 0;
					return;
				}
				Helper.MC.getNetworkHandler().sendPacket(bothPacket);

				ticks = 0;
				bothPacket = null;
			} else {
				sendTimer--;
			}
		}
	}

	@EventTarget
	public void onPacket(PacketEvent event) {
		if (event.getType().equals(EventType.SEND)) {
			if (mode.getCurrentMode().equalsIgnoreCase("Legit")) {
				if (event.getPacket() instanceof PlayerMoveC2SPacket) {

					if (Helper.getPlayer().isDead())
						return;

					packetList.add(event.getPacket());
					event.cancel();
					legitTicks++;
				}

			} else if (mode.getCurrentMode().equalsIgnoreCase("Pulse")) {
				if (event.getPacket() instanceof PlayerMoveC2SPacket) {

					if (!Helper.getPlayer().isDead() || pulseTicks == 0)
					packetList.add(event.getPacket());
					
					if (pulseTicks > 0)
					event.cancel();
					pulseTicks++;
				}

			} else if (mode.getCurrentMode().equalsIgnoreCase("Always")) {
				if (event.getPacket() instanceof PlayerMoveC2SPacket.Full) {

					if (ticks > 0)
						return;

					ticks++;
					bothDelay(event);
				}
			}
		}
	}

	public void sendPackets() {
		if (packetList != null) {
			try {
				for (Packet<?> unsentPacket : this.packetList)
					Helper.MC.getNetworkHandler().sendPacket(unsentPacket);
			} catch (Exception exception) {
			}
			this.packetList.clear();
		}
		ticks = 0;
		pulseTicks = 0;
		legitTicks = 0;

	}

	private void bothDelay(PacketEvent event) {
		if (!sendPackets) {
			sendPackets = true;
			sendTimer = this.delay.getCurrentValueDouble();
			bothPacket = (PlayerMoveC2SPacket.Full) event.getPacket();

			event.cancel();
		}
	}

}
