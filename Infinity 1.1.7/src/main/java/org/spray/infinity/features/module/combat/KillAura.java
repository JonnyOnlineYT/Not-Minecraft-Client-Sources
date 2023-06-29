package org.spray.infinity.features.module.combat;

import java.util.ArrayList;
import java.util.Arrays;

import org.spray.infinity.event.MotionEvent;
import org.spray.infinity.event.RotationEvent;
import org.spray.infinity.features.Category;
import org.spray.infinity.features.Module;
import org.spray.infinity.features.ModuleInfo;
import org.spray.infinity.features.Setting;
import org.spray.infinity.features.module.player.FakeLags;
import org.spray.infinity.main.Infinity;
import org.spray.infinity.utils.Helper;
import org.spray.infinity.utils.InvUtil;
import org.spray.infinity.utils.RotationUtil;
import org.spray.infinity.utils.Timer;
import org.spray.infinity.utils.entity.EntityUtil;

import com.darkmagician6.eventapi.EventTarget;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

@ModuleInfo(category = Category.COMBAT, desc = "Attack entities on range", key = -2, name = "KillAura", visible = true)
public class KillAura extends Module {

	public Setting rotation = new Setting(this, "Rotation", "Matrix", new ArrayList<>(Arrays.asList("Matrix", "Snap")));

	// targets
	private Setting players = new Setting(this, "Players", true);
	private Setting friends = new Setting(this, "Friends", false).setVisible(() -> players.isToggle());
	private Setting invisibles = new Setting(this, "Invisibles", true);
	private Setting mobs = new Setting(this, "Mobs", true);
	private Setting animals = new Setting(this, "Animals", false);
	private Setting throughWalls = new Setting(this, "Through Walls", true);

	private Setting fov = new Setting(this, "FOV", 240D, 0D, 360D);

	private Setting destroyShield = new Setting(this, "Destroy Shield (Axe)", true);

	private Setting releaseShield = new Setting(this, "Let go of the shield on impact", false);

	private Setting keepSprint = new Setting(this, "Keep Sprint", true);

	private Setting lockView = new Setting(this, "Look View", false);

	// raycasting target
	private Setting rayCast = new Setting(this, "RayCast", true);

	private Setting noSwing = new Setting(this, "No Swing", false);
	private Setting coolDown = new Setting(this, "CoolDown", true);
	private Setting aps = new Setting(this, "APS", 1.8D, 0.1D, 15.0D).setVisible(() -> !coolDown.isToggle());

	private Setting range = new Setting(this, "Range", 3.7D, 0.1D, 6.0D);

	// static mmmmmm
	public static Entity target;

	// rotations
	public float yaw, pitch;

	private float lastYaw = 999f;
	private float lastPitch = 999f;

	private int time;

	private int preSlot = -2;

	private Timer timer = new Timer();

	@Override
	public void onDisable() {
		target = null;
		time = 0;
		super.onDisable();
	}

	@EventTarget
	public void onMotionTick(MotionEvent event) {
		setSuffix(rotation.getCurrentMode());

		target = EntityUtil.getTarget(range.getCurrentValueDouble(), fov.getCurrentValueDouble(), players.isToggle(),
				friends.isToggle(), invisibles.isToggle(), mobs.isToggle(), animals.isToggle(),
				throughWalls.isToggle());

		if (target == null)
			return;
		
		yaw = RotationUtil.lookAtEntity(target)[0];
		pitch = RotationUtil.lookAtEntity(target)[1];

		if (rotation.getCurrentMode().equalsIgnoreCase("Matrix")) {
			event.setRotation(yaw, pitch, lockView.isToggle());
		} else if (rotation.getCurrentMode().equalsIgnoreCase("Reset")) {
			if (lastYaw != 999 || lastPitch != 999) {
				event.setRotation(lastYaw, lastPitch, lockView.isToggle());
			}
		}

		destroyShield();
		attack(event);

		if (time > 0) {
			time--;
		}

		if (time <= 0) {
			lastYaw = 999;
			lastPitch = 999;
		}

		if (rayCast.isToggle())
			EntityUtil.updateTargetRaycast(target, range.getCurrentValueDouble(), event.getYaw(),
					event.getPitch());
	}

	@EventTarget
	public void onRotation(RotationEvent event) {
		if (target == null)
			return;

		if (rayCast.isToggle()) {
			if (rotation.getCurrentMode().equalsIgnoreCase("Focus")) {
				event.setYaw(yaw);
				event.setPitch(pitch);
			} else if (rotation.getCurrentMode().equalsIgnoreCase("Reset")) {
				event.setYaw(lastYaw);
				event.setPitch(lastPitch);
			}
			event.cancel();
		}
	}

	private void destroyShield() {
		if (!destroyShield.isToggle())
			return;

		if (target instanceof PlayerEntity) {
			int slotAxe = InvUtil.findAxe();
			if (((PlayerEntity) target).isBlocking()) {
				if (slotAxe != -2) {

					if (!(Helper.getPlayer().getMainHandStack().getItem() instanceof AxeItem))
						preSlot = Helper.getPlayer().getInventory().selectedSlot;
					Helper.getPlayer().getInventory().selectedSlot = slotAxe;
				}
			} else {
				if (preSlot != -2) {
					Helper.getPlayer().getInventory().selectedSlot = preSlot;
					preSlot = -2;
				}
			}
		}
	}

	public void attack(MotionEvent event) {
		if (coolDown.isToggle() ? Helper.getPlayer().getAttackCooldownProgress(0.0f) >= 1
				: timer.hasReached(1000 / aps.getCurrentValueDouble())) {
			if (Criticals.fall(target)) {

				if (releaseShield.isToggle() && Helper.getPlayer().isBlocking())
					Helper.sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.RELEASE_USE_ITEM,
							new BlockPos(0, 0, 0), Direction.DOWN));

				if (rotation.getCurrentMode().equalsIgnoreCase("Snap")) {
					float[] snap = RotationUtil.lookAtEntity(target);

					lastYaw = snap[0];
					lastPitch = snap[1];
					event.setRotation(snap[0], snap[1], lockView.isToggle());
					Helper.getPlayer().bodyYaw = snap[0];
					Helper.getPlayer().headYaw = snap[0];
					time = 4;
				}

				if (Infinity.getModuleManager().get(FakeLags.class).isEnabled())
					((FakeLags) Infinity.getModuleManager().get(FakeLags.class)).sendPackets();

				Helper.sendPacket(PlayerInteractEntityC2SPacket.attack(target, Helper.getPlayer().isSneaking()));
				EntityUtil.swing(!noSwing.isToggle());

				if (keepSprint.isToggle()) {
					if (Helper.getPlayer().fallDistance > 0.0F && !Helper.getPlayer().isOnGround()
							&& !Helper.getPlayer().isClimbing() && !Helper.getPlayer().isTouchingWater()
							&& !Helper.getPlayer().hasStatusEffect(StatusEffects.BLINDNESS)
							&& !Helper.getPlayer().hasVehicle() && target instanceof LivingEntity) {
						Helper.getPlayer().addCritParticles(target);
					}

					if (EnchantmentHelper.getAttackDamage(Helper.getPlayer().getMainHandStack(),
							((LivingEntity) target).getGroup()) > 0) {
						Helper.getPlayer().addEnchantedHitParticles(target);
					}

					Helper.getPlayer().resetLastAttackedTicks();
				} else {
					Helper.getPlayer().attack(target);
				}
				timer.reset();
				if (releaseShield.isToggle() && Helper.getPlayer().isBlocking())
					Helper.sendPacket(new PlayerInteractItemC2SPacket(Hand.OFF_HAND));

			}
		}
	}
}
