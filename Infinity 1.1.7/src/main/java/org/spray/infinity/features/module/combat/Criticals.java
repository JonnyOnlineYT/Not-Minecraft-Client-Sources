package org.spray.infinity.features.module.combat;

import java.util.ArrayList;
import java.util.Arrays;

import org.spray.infinity.event.MotionEvent;
import org.spray.infinity.event.PacketEvent;
import org.spray.infinity.event.TickEvent;
import org.spray.infinity.features.Category;
import org.spray.infinity.features.Module;
import org.spray.infinity.features.ModuleInfo;
import org.spray.infinity.features.Setting;
import org.spray.infinity.main.Infinity;
import org.spray.infinity.utils.Helper;
import org.spray.infinity.utils.MoveUtil;
import org.spray.infinity.utils.PacketUtil;
import org.spray.infinity.utils.PacketUtil.InteractType;

import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.types.EventType;

import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

@ModuleInfo(category = Category.COMBAT, desc = "Critical hit", key = -2, name = "Criticals", visible = true)
public class Criticals extends Module {

	private Setting mode = new Setting(this, "Mode", "Packet",
			new ArrayList<>(Arrays.asList(new String[] { "Packet", "Jump", "Spoof", "Sentiel", "Mini Jump" })));

	private Setting falling = new Setting(this, "Falling", false)
			.setVisible(() -> mode.getCurrentMode().equalsIgnoreCase("Jump"));
	private Setting fallDistance = new Setting(this, "Min Distance", 0.26, 0.01, 0.41)
			.setVisible(() -> mode.getCurrentMode().equalsIgnoreCase("Jump") && falling.isToggle());

	private double y;
	private int stage;
	private int attackCount;

	// delay for attack
	private PlayerInteractEntityC2SPacket attackPacket;
	private HandSwingC2SPacket swingPacket;
	private boolean sendPackets;
	private int sendTimer;

	@Override
	public void onEnable() {
		sendPackets = false;
		swingPacket = null;
		attackPacket = null;
		sendTimer = 0;
		attackCount = 0;
	}

	@Override
	public void onDisable() {
		Helper.getPlayer().flyingSpeed = 0.02f;
		Infinity.resetTimer();
	}

	@Override
	public void onUpdate() {
		setSuffix(mode.getCurrentMode());
	}

	@EventTarget()
	public void onMotion(MotionEvent event) {
		if (event.getType().equals(EventType.PRE)) {
			if (this.mode.getCurrentMode().equalsIgnoreCase("Spoof")) {
				if (attackCount > 0) {
					double ypos = Helper.getPlayer().getY();
					if (Helper.getPlayer().isOnGround()) {
						event.setOnGround(false);
						if (this.stage == 0) {
							this.y = ypos + 0.1;

							event.setOnGround(true);

						} else if (this.stage == 1) {
							this.y -= 5.0E-15D;
						} else {
							this.y -= 4.0E-15D;
						}

						if (this.y <= Helper.getPlayer().getY()) {
							this.stage = 0;
							this.y = Helper.getPlayer().getY();
							event.setOnGround(true);
						}
						event.setY(this.y);

						this.stage++;
					} else {
						this.stage = 0;
					}
				}
			}
		}
	}

	@EventTarget
	public void onPacket(PacketEvent event) {
		if (event.getType().equals(EventType.SEND)) {
			if (mode.getCurrentMode().equalsIgnoreCase("Spoof")
					|| mode.getCurrentMode().equalsIgnoreCase("Jump") && !falling.isToggle()
					|| mode.getCurrentMode().equalsIgnoreCase("Mini Jump")) {
				if (event.getPacket() instanceof PlayerInteractEntityC2SPacket) {
					PlayerInteractEntityC2SPacket packet = (PlayerInteractEntityC2SPacket) event.getPacket();
					if (PacketUtil.getInteractType(packet) == InteractType.INTERACT_AT) {

						if (skipCrit() || attackCount > 0)
							return;

						attackCount++;
						doJumpMode(event);
					} else if (event.getPacket() instanceof HandSwingC2SPacket) {
						if (skipCrit())
							return;
						doJumpModeSwing(event);
					}
				}

			} else if (mode.getCurrentMode().equalsIgnoreCase("Packet")) {
				if (event.getPacket() instanceof PlayerInteractEntityC2SPacket) {
					PlayerInteractEntityC2SPacket packet = (PlayerInteractEntityC2SPacket) event.getPacket();
					if (PacketUtil.getInteractType(packet) == InteractType.INTERACT_AT) {

						if (!Helper.getPlayer().isOnGround())
							return;

						Helper.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(Helper.getPlayer().getX(),
								Helper.getPlayer().getY() + 0.0645, Helper.getPlayer().getZ(), false));
						Helper.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(Helper.getPlayer().getX(),
								Helper.getPlayer().getY(), Helper.getPlayer().getZ(), false));
					}
				}
			} else if (mode.getCurrentMode().equalsIgnoreCase("Sentiel")) {
				if (event.getPacket() instanceof PlayerInteractEntityC2SPacket) {
					PlayerInteractEntityC2SPacket packet = (PlayerInteractEntityC2SPacket) event.getPacket();
					if (PacketUtil.getInteractType(packet) == InteractType.INTERACT_AT) {
						if (!Helper.getPlayer().isOnGround())
							return;

						MoveUtil.setYVelocity(-1e-2);
						Helper.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(Helper.getPlayer().getX(),
								Helper.getPlayer().getY() + 1.28E-9D, Helper.getPlayer().getZ(), true));
						Helper.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(Helper.getPlayer().getX(),
								Helper.getPlayer().getY(), Helper.getPlayer().getZ(), false));
					}
				}
			}
		}
	}

	@EventTarget
	public void onTick(TickEvent event) {
		if (sendPackets) {
			if (sendTimer <= 0) {
				sendPackets = false;

				if (attackPacket == null || swingPacket == null) {
					attackCount = 0;
					return;
				}
				Helper.MC.getNetworkHandler().sendPacket(attackPacket);
				Helper.MC.getNetworkHandler().sendPacket(swingPacket);

				attackCount = 0;
				attackPacket = null;
				swingPacket = null;
			} else {
				sendTimer--;
			}
		}
	}

	private void doJumpMode(PacketEvent event) {
		if (!sendPackets) {
			sendPackets = true;
			sendTimer = this.mode.getCurrentMode().equalsIgnoreCase("Jump") && !falling.isToggle() ? 8
					: mode.getCurrentMode().equalsIgnoreCase("Mini Jump") ? 5 : 2;
			attackPacket = (PlayerInteractEntityC2SPacket) event.getPacket();

			if (this.mode.getCurrentMode().equalsIgnoreCase("Jump") && !falling.isToggle())
				Helper.getPlayer().jump();
			else if (mode.getCurrentMode().equalsIgnoreCase("Mini Jump"))
				MoveUtil.setYVelocity(0.25);

			event.cancel();
		}
	}

	private void doJumpModeSwing(PacketEvent event) {
		if (sendPackets && swingPacket == null) {
			swingPacket = (HandSwingC2SPacket) event.getPacket();

			event.cancel();
		}
	}

	private boolean skipCrit() {
		boolean a = !Helper.getPlayer().isSubmergedInWater() && !Helper.getPlayer().isInLava()
				&& !Helper.getPlayer().isClimbing();
		if (!Helper.getPlayer().isOnGround())
			return true;
		return !a;
	}

	public static boolean fall(net.minecraft.entity.Entity target) {
		Criticals criticals = ((Criticals) Infinity.getModuleManager().get(Criticals.class));
		if (criticals.isEnabled() && criticals.mode.getCurrentMode().equalsIgnoreCase("Jump")
				&& criticals.falling.isToggle()) {
			return Helper.getPlayer().fallDistance >= criticals.fallDistance.getCurrentValueDouble()
					&& !Helper.getPlayer().isOnGround() && Helper.getPlayer().fallDistance != 0
					|| Helper.getPlayer().isTouchingWater() || Helper.getPlayer().isInLava()
					|| Helper.getPlayer().noClip || Helper.getPlayer().isRiding();
		} else {
			return true;
		}
	}

}
