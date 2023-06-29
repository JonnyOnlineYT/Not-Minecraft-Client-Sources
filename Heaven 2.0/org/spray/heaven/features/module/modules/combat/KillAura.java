package org.spray.heaven.features.module.modules.combat;

import java.awt.Color;
import java.util.Arrays;
import java.util.Objects;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.spray.heaven.events.MotionEvent;
import org.spray.heaven.events.PacketEvent;
import org.spray.heaven.events.VectorRotationEvent;
import org.spray.heaven.events.WorldRenderEvent;
import org.spray.heaven.features.module.Category;
import org.spray.heaven.features.module.Module;
import org.spray.heaven.features.module.ModuleInfo;
import org.spray.heaven.features.module.Setting;
import org.spray.heaven.features.module.modules.display.ArrayListMod;
import org.spray.heaven.main.Wrapper;
import org.spray.heaven.ui.clickui.Colors;
import org.spray.heaven.util.*;

import com.darkmagician6.eventapi.EventTarget;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemShield;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.network.play.server.SPacketCloseWindow;
import net.minecraft.network.play.server.SPacketOpenWindow;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

@ModuleInfo(name = "KillAura", category = Category.COMBAT, visible = true, key = Keyboard.KEY_NONE)
public class KillAura extends Module {

	public final Setting mode = register(
			new Setting("Mode", "Matrix2", Arrays.asList("Matrix", "Matrix2", "Sunrise", "Snap")));
	public final Setting priority = register(
			new Setting("Priority", "Closest", Arrays.asList("Closest", "Health", "Crosshair")));

	private final Setting players = register(new Setting("Players", true));
	private final Setting friends = register(new Setting("Friends", false).setVisible(players::isToggle));
	private final Setting invisibles = register(new Setting("Invisible", false));
	private final Setting mobs = register(new Setting("Mobs", true));
	private final Setting animals = register(new Setting("Animals", true));
	private final Setting ignoreNaked = register(new Setting("Ignore Naked", false));
	private final Setting ignoreWalls = register(new Setting("Ignore Walls", true));

	private final Setting rayTrace = register(new Setting("Ray Trace", false));
	private final Setting resolvePosition = register(new Setting("Resolve Position", true));
	private final Setting screenLock = register(new Setting("Screen Lock", false));
	private final Setting dropShield = register(new Setting("AxeBreaker Flicker", true));

	private final Setting breakShield = register(new Setting("Break Shield", true));

	// TODO
	private final Setting shieldDesync = register(new Setting("Shield Desync", false));

	// PlayerControllerMP.java | 534 line
	private final Setting throughShield = register(new Setting("Through the Shield", false));
	private final Setting autoCrit = register(new Setting("Falling Criticals", true));
	private final Setting onlyWithSpace = register(
			new Setting("Only Space Crit", false).setVisible(autoCrit::isToggle));
	public final Setting stopSprint = register(new Setting("Stop sprinting on impact", true));
	private final Setting lockview = register(new Setting("Lock View", false));
	private final Setting hitdelay = register(new Setting("Hit Delay", false));
	private final Setting aps = register(new Setting("APS", 1.95D, 0D, 10D).setVisible(() -> !hitdelay.isToggle()));
	private final Setting fov = register(new Setting("FOV", 360D, 0D, 360D));
	private final Setting range = register(new Setting("Range", 3.6D, 0D, 6D));
	private final Setting preRange = register(new Setting("Pre Range", 1D, 0D, 6D));

	private final Setting targetESP = register(new Setting("Target ESP", true));
	private final Setting color = register(
			new Setting("ESP Color", Colors.CLIENT_COLOR).setVisible(targetESP::isToggle));

	private final Timer apsTimer = new Timer();
	private Entity target;
	private float yaw, pitch, random;

	private boolean timerReset = true;

	private double apsTime;

	private long lastMS = System.currentTimeMillis();
	private long lastDeltaMS = 0L;
	private double direction = 1, yPos, progress = 0;
	private float delta = 0;

	private double sideMultiplier;
	private double upMultiplier;
	private Vec3d predict;

	@Override
	public void onDisable() {
		if (mc.player.getHeldItemOffhand().getItem() instanceof ItemShield && mc.player.isHandActive())
			mc.gameSettings.keyBindUseItem.setPressed(false);

		target = null;
	}

	@Override
	public void onUpdate() {
		setSuffix(mode.getCurrentMode());

		target = EntityUtil.getTarget(priority.getCurrentMode(), range.getValue() + preRange.getValue(), fov.getValue(),
				players.isToggle(), friends.isToggle(), ignoreNaked.isToggle(), invisibles.isToggle(), mobs.isToggle(),
				animals.isToggle(), ignoreWalls.isToggle());

		if (shieldDesync.isToggle() && mc.player.isActiveItemStackBlocking() && target != null
				&& mc.player.ticksExisted % 8 == 0) {
			Wrapper.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM,
					new BlockPos(0, 0, 0), EnumFacing.DOWN));
			mc.playerController.processRightClick(mc.player, mc.world, EnumHand.OFF_HAND);
		}

		if (target instanceof EntityPlayer) {
			if (dropShield.isToggle() && ((EntityPlayer) target).getHeldItemMainhand().getItem() instanceof ItemAxe
					&& mc.player.isActiveItemStackBlocking())
				mc.playerController.onStoppedUsingItem(mc.player);
		}

		if (target == null) {
			timerReset = true;
			return;
		}
	}

	@EventTarget
	public void onMotionUpdate(MotionEvent event) {
		if (target == null) {
			return;
		}

		if (shieldDesync.isToggle() && mc.player.isHandActive()
				&& mc.player.getHeldItemOffhand().getItem() == Items.SHIELD && target != null
				&& mc.player.ticksExisted % 6 == 0) {
			mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM,
					new BlockPos(900, 900, 900), EnumFacing.DOWN));
			mc.playerController.processRightClick(mc.player, mc.world, EnumHand.OFF_HAND);
		}

		rotation(target, event);
		attack(target, event);

		if (resolvePosition.isToggle()) {
			if (target.posY < 0) {
				pitch = 1;
			} else if (target.posY > 255) {
				pitch = 90;
			}

			if (Math.abs(target.posX - target.lastTickPosX) > 0.50
					|| Math.abs(target.posZ - target.lastTickPosZ) > 0.50) {
				target.setEntityBoundingBox(new AxisAlignedBB(target.posX, target.posY, target.posZ,
						target.lastTickPosX, target.lastTickPosY, target.lastTickPosZ));
			}
		}
	}

	@EventTarget
	public void onRotation(VectorRotationEvent event) {
		if (target == null)
			return;

//		event.setYaw(yaw);
//		event.setPitch(pitch);
	}

	private void rotation(Entity target, MotionEvent event) {
		mc.player.rotationYawHead = RotationUtil.lookAtEntity(target)[0];
		mc.player.renderYawOffset = RotationUtil.lookAtEntity(target)[0];

		float[] rotation = RotationUtil.getMatrixRotations(target);
		if ("Matrix".equals(mode.getCurrentMode())) {
			yaw = rotation[0];
			pitch = rotation[1];
			event.setRotation(yaw, pitch, lockview.isToggle());
		}

		if ("Matrix2".equals(mode.getCurrentMode())) {
			yaw = RotationUtil.getMatrixRotations(target)[0];
			pitch = RotationUtil.getMatrixRotations(target)[1];
			event.setRotation(yaw, pitch, lockview.isToggle());
		}

		float turnSpeed = 0;
		if ("Sunrise".equals(mode.getCurrentMode())) {
			//вырезано :)
		}
	}

	@EventTarget
	public void onWorldRender(WorldRenderEvent event) {
		if (targetESP.isToggle() && target != null) {

			delta += (target != null ? 0.1F : -0.1F);
			if (delta > 2 / 255f) {
				delta = 2 / 255f;
			}
			if (delta < 0) {
				delta = 0;
			}

			ArrayListMod listMod = Wrapper.getModule().get("Arraylist");

			double lastY = yPos;

			if (delta > 0F) {
				if (System.currentTimeMillis() - lastMS >= 1000) {
					direction = -direction;
					lastMS = System.currentTimeMillis();
				}
				long weird = (direction > 0 ? System.currentTimeMillis() - lastMS
						: 1000L - (System.currentTimeMillis() - lastMS));
				progress = (double) weird / 1000;
				lastDeltaMS = System.currentTimeMillis() - lastMS;
			} else {
				lastMS = System.currentTimeMillis() - lastDeltaMS;
			}

			AxisAlignedBB axisAlignedBB = target.getEntityBoundingBox();

			double radius = axisAlignedBB.maxX - axisAlignedBB.minX;
			double height = axisAlignedBB.maxY - axisAlignedBB.minY;
			double posX = target.lastTickPosX + (target.posX - target.lastTickPosX) * mc.getRenderPartialTicks();
			double posY = target.lastTickPosY + (target.posY - target.lastTickPosY) * mc.getRenderPartialTicks();
			double posZ = target.lastTickPosZ + (target.posZ - target.lastTickPosZ) * mc.getRenderPartialTicks();

			yPos = MathUtil.easeInOutQuart(progress) * height;

			double deltaY = (direction > 0 ? yPos - lastY : lastY - yPos) * -direction * 2.5;

			Color color = this.color.getColor();
			float r = color.getRed() / 255F;
			float g = color.getGreen() / 255F;
			float b = color.getBlue() / 255F;

			GL11.glPushMatrix();
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glShadeModel(GL11.GL_SMOOTH);
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glEnable(GL11.GL_LINE_SMOOTH);
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glDepthMask(false);
			GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
			GL11.glDisable(GL11.GL_CULL_FACE);
			GL11.glTranslated(-mc.getRenderManager().getRenderPosX(), -mc.getRenderManager().getRenderPosY(),
					-mc.getRenderManager().getRenderPosZ());

			GL11.glBegin(GL11.GL_QUAD_STRIP);

			for (int i = 0; i <= 360; i++) {
				double calc = i * Math.PI / 180;
				double posX2 = posX - Math.sin(calc) * radius;
				double posZ2 = posZ + Math.cos(calc) * radius;

				GL11.glColor4f(r, g, b, 0F);
				GL11.glVertex3d(posX2, posY + yPos + deltaY, posZ2);

				GL11.glColor4f(r, g, b, delta * 255);
				GL11.glVertex3d(posX2, posY + yPos, posZ2);
			}

			GL11.glEnd();

			drawCircle(posX, posY + yPos, posZ, radius, r, g, b, delta * 130);

			GL11.glDepthMask(true);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			GL11.glDisable(GL11.GL_LINE_SMOOTH);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glPopMatrix();
			GL11.glColor4f(1, 1, 1, 1);
		}
	}

	private void attack(Entity target, MotionEvent event) {
		apsTime = 1000 / (aps.getValue() + MathUtil.random(-0.02, 0.02));
		boolean waitDelay = hitdelay.isToggle() ? mc.player.getCooledAttackStrength(1) >= 0.92
				: apsTimer.hasReached(apsTime);

		if (waitDelay || timerReset) {

			if (rayTrace.isToggle() && !isFaced(event, target)) {
				return;
			}

			if (whenFalling()) {
				if (mode.getCurrentMode().equalsIgnoreCase("Snap"))
					event.setRotation(yaw, pitch, lockview.isToggle());

				if (stopSprint.isToggle())
					Objects.requireNonNull(mc.getConnection())
							.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SPRINTING));
				attackEntity(event);
				apsTimer.reset();
			}
		}
	}

	private boolean whenFalling() {
		if (autoCrit.isToggle()) {
			float distance = EntityUtil.isBlockAboveHead() ? 0.06f : 0.15f;

			boolean onSpace = !onlyWithSpace.isToggle()
					|| (mc.gameSettings.keyBindJump.isKeyDown() || !mc.player.onGround);
			boolean onFall = EntityUtil.isBlockAboveHead() ? mc.player.fallDistance > 0
					: mc.player.fallDistance >= distance;
			return !onSpace || onFall && !mc.player.onGround || mc.player.isInWater() || mc.player.isInLava()
					|| mc.player.isOnLadder() || mc.player.isInWeb;
		}
		return true;
	}
	private void attackEntity(MotionEvent event) {
		if (mc.player.getDistanceToEntity(target) <= range.getValue()) {
			if (mode.getCurrentMode().equalsIgnoreCase("Sunrise2") || mode.getCurrentMode().equalsIgnoreCase("Snap")) {
				yaw = RotationUtil.getMatrixRotations(target)[0];
				pitch = RotationUtil.getMatrixRotations(target)[1];

				event.setRotation(yaw, pitch, lockview.isToggle());
			}

			if (throughShield.isToggle() && mc.player.getActiveHand() == EnumHand.OFF_HAND
					&& mc.player.getHeldItemOffhand().getItem() == Items.SHIELD) {
				if (Wrapper.getPlayer().isActiveItemStackBlocking())
					Wrapper.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM,
							new BlockPos(0, 0, 0), EnumFacing.DOWN));
			}

			float distance = EntityUtil.isBlockAboveHead() ? 0.06f : 0.15f;

			if (EntityUtil.isBlockAboveHead()) {
				Wrapper.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
				Wrapper.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SPRINTING));
			}

			mc.playerController.attackEntity(Wrapper.getPlayer(), target);
			mc.player.swingArm(EnumHand.MAIN_HAND);

			if (breakShield.isToggle())
				shieldBreakerNew(target);

			if (throughShield.isToggle() && mc.player.isActiveItemStackBlocking()
					&& mc.player.getHeldItemOffhand().getItem() == Items.SHIELD) {
				Wrapper.sendPacket(new CPacketPlayerTryUseItem(EnumHand.OFF_HAND));
			}

			if (EntityUtil.isBlockAboveHead())
				Wrapper.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));

			timerReset = false;
		} else
			timerReset = true;
	}

	private boolean isFaced(MotionEvent event, Entity entity) {
		float turnSpeed = (float) (MathUtil.random(190, 320) * (0.1812676 * Math.random()));
		float lyaw = RotationUtil.sunriseRotate(event.getYaw(), yaw, 40, 58);
		float lpitch = RotationUtil.sunriseRotate(event.getPitch(), pitch, 0.35f, 2.1f);
		return RotationUtil.isFaced(target, lyaw, lpitch, (float) (range.getValue() + preRange.getValue()));
	}

	private void shieldBreakerNew(Entity entity) {
		if (entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entity;

			if (player.isActiveItemStackBlocking()) {
				int slotAxe = InvUtil.findAxe();
				if (slotAxe != -2) {
					Wrapper.sendPacket(new CPacketHeldItemChange(slotAxe));
					mc.playerController.attackEntity(mc.player, player);
					mc.player.swingArm(EnumHand.MAIN_HAND);
					Wrapper.sendPacket(new CPacketHeldItemChange(mc.player.inventory.currentItem));
				}
			}
		}
	}

	@EventTarget
	public void onPacket(PacketEvent event) {
		if (screenLock.isToggle()) {
			if (event.getPacket() instanceof SPacketCloseWindow || event.getPacket() instanceof SPacketOpenWindow) {
				event.cancel();
			}
		}
	}

	private void drawCircle(double x, double y, double z, double radius, float red, float green, float blue,
			float alp) {
		GL11.glLineWidth(3);
		GL11.glBegin(GL11.GL_LINE_LOOP);
		GL11.glColor4f(red, green, blue, alp);

		for (int i = 0; i <= 360; i += 1) {
			double posX = x - Math.sin(i * Math.PI / 180) * radius;
			double posZ = z + Math.cos(i * Math.PI / 180) * radius;
			GL11.glVertex3d(posX, y, posZ);
		}

		GL11.glEnd();
	}

	public Entity getTarget() {
		return target;
	}

	public float getYaw() {
		return yaw;
	}
}
