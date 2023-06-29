package ru.wendoxd.modules.impl.combat;

import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;

import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketEntityAction.Action;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import org.lwjgl.opengl.GL11;

import ru.wendoxd.WexSide;
import ru.wendoxd.events.Event;
import ru.wendoxd.events.impl.misc.EventAnimation;
import ru.wendoxd.events.impl.packet.EventAttackEntity;
import ru.wendoxd.events.impl.player.EventEntitySync;
import ru.wendoxd.events.impl.player.EventInteractWithEntity;
import ru.wendoxd.events.impl.player.EventUpdate;
import ru.wendoxd.events.impl.render.EventRender3D;
import ru.wendoxd.modules.Module;
import ru.wendoxd.modules.impl.combat.CrystalAura.ExplosionBuilder;
import ru.wendoxd.modules.impl.movement.Jesus;
import ru.wendoxd.ui.menu.elements.Frame;
import ru.wendoxd.ui.menu.elements.tabelements.CheckBox;
import ru.wendoxd.ui.menu.elements.tabelements.MultiSelectBox;
import ru.wendoxd.ui.menu.elements.tabelements.SelectBox;
import ru.wendoxd.ui.menu.elements.tabelements.Slider;
import ru.wendoxd.ui.menu.utils.MenuAPI;
import ru.wendoxd.utils.combat.AdvancedCast;
import ru.wendoxd.utils.combat.CastHelper;
import ru.wendoxd.utils.combat.CastHelper.EntityType;
import ru.wendoxd.utils.combat.RaycastHelper;
import ru.wendoxd.utils.player.MoveUtils;
import ru.wendoxd.utils.sound.SoundUtils;
import ru.wendoxd.utils.visual.RenderUtils;
import ru.wendoxd.utils.visual.pallete.PaletteHelper;

import javax.vecmath.Vector2f;

import java.util.*;
import java.util.Map.Entry;

public class Aura extends Module {
	public static Vector2f serverRotation = new Vector2f(), visibleRotation = new Vector2f();
	public Frame aura = new Frame("Aura").ignoreSort();
	public static CheckBox enabled = new CheckBox("Aura").markArrayList("Aura");
	public Slider fov = (Slider) new Slider("FOV", 0, 0, 180, 0.5, () -> enabled.isEnabled(true))
			.markDescription("Угол обзора");
	public Slider distance = (Slider) new Slider("Distance", 2, 0, 6, 0.5, () -> enabled.isEnabled(true))
			.markDescription("Дистанция на которой киллаура\nбудет бить игроков.");
	public Slider rotate = (Slider) new Slider("Rotate", 2, 0, 3, 0.5, () -> enabled.isEnabled(true)).markDescription(
			"Дистанция на которой киллаура\nбудет искать игроков\nдля последующего удара\n(прибавляется к обычной дистанции)\nРекомендуется поставить 0 на NexusGrief.");
	public SelectBox rotation = new SelectBox("Rotation", new String[] { "Matrix", "Nexus" },
			() -> enabled.isEnabled(true));
	public MultiSelectBox targets = new MultiSelectBox("Targets",
			new String[] { "Players", "Mobs", "Animals", "Villagers", "Friends", "Slimes", "Crystals" },
			() -> enabled.isEnabled(true));
	public CheckBox ignoreWalls = (CheckBox) new CheckBox("Ignore Walls", () -> enabled.isEnabled(true))
			.markDescription("Выключает проверку коллизии для ауры\nНе рекомендуется включать на постоянной основе");
	public CheckBox weaponOnly = (CheckBox) new CheckBox("Weapon Only", () -> enabled.isEnabled(true))
			.markDescription("Киллаура будет работать\nтолько с оружием в руке");
	public CheckBox clientLook = (CheckBox) new CheckBox("Client Look", () -> enabled.isEnabled(true))
			.markDescription("Вас будет наводить на игрока");
	public CheckBox shieldBreaker = new CheckBox("Shield Breaker", () -> enabled.isEnabled(true));
	public CheckBox ignoreNaked = (CheckBox) new CheckBox("Ignore Naked", () -> enabled.isEnabled(true))
			.markDescription("Не бьет игроков без брони");
	public CheckBox resolver = (CheckBox) new CheckBox("Resolve Position", () -> enabled.isEnabled(true))
			.markDescription(
					"Улучшает попадания по хитбоксу во всех случаях пвп\n(на суше/воде/тд).\nУвеличивает в некоторых случаях максимальную\nдистанцию удара на ~1 блок\nбез флагов.");
	public CheckBox ignoreInvisible = (CheckBox) new CheckBox("Ignore Invisibles", () -> enabled.isEnabled(true))
			.markDescription("Будет игнорировать невидимых игроков");
	public CheckBox criticals = new CheckBox("Criticals", () -> enabled.isEnabled(true));
	public CheckBox criticals_autojump = new CheckBox("Auto Jump", () -> criticals.isEnabled(true));
	public CheckBox hitsounds = new CheckBox("Hit Sound", () -> enabled.isEnabled(true));
	public CheckBox noInteract = (CheckBox) new CheckBox("NoInteract", () -> enabled.isEnabled(true))
			.markDescription("Убирает взаимодействие с сущностями\nво время пвп.");
	public CheckBox shieldDesync = (CheckBox) new CheckBox("Shield Desync", () -> enabled.isEnabled(true))
			.markArrayList("Shield Desync").markSetting("Shield Desync").markDescription("Рассинхрон вашего щита");
	public CheckBox shieldDesyncOnlyOnAura = (CheckBox) new CheckBox("Wait Target",
			() -> enabled.isEnabled(true) && shieldDesync.isEnabled(false))
					.markDescription("Шилд десинк будет работать\nтолько если у киллауры\nесть таргет.");
	public SelectBox sound = new SelectBox("Sound", new String[] { "NeverLose", "Skeet" },
			() -> enabled.isEnabled(true) && hitsounds.isEnabled(true));
	public Slider volume = new Slider("Volume", 1, 0.1, 6, 0.5,
			() -> enabled.isEnabled(true) && hitsounds.isEnabled(true));
	public CheckBox targetesp = new CheckBox("Target Esp", () -> enabled.isEnabled(true));
	public static EntityLivingBase target;
	public static double prevCircleStep, circleStep;
	public static boolean hitTick;
	public int nextShieldTime;
	public float prevAdditionYaw;
	public static long consumeTime;
	public static int minCPS;
	public int addition, swapBackAxe;
	public boolean thisContextRotatedBefore, thisContextAttackedBefore, requestForAttack, attackAllowed;
	public static Aura instance;

	public Aura() {
		instance = this;
	}

	@Override
	protected void initSettings() {
		MenuAPI.combat.register(aura);
		aura.register(enabled.markSetting("Aura"), fov, distance, rotate, targets, rotation,
				ignoreWalls.markSetting("Ignore Walls"), criticals.markSetting("Criticals"),
				criticals_autojump.markSetting("Auto Jump"), resolver, noInteract, weaponOnly, clientLook,
				shieldBreaker.markSetting("Shield Breaker"), shieldDesync, shieldDesyncOnlyOnAura,
				targetesp.markColorPicker(), ignoreNaked.markSetting("Ignore Naked"),
				ignoreInvisible.markSetting("Ignore Invisibles"), hitsounds, sound, volume);
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventInteractWithEntity && noInteract.isEnabled(false)) {
			EventInteractWithEntity eiwe = (EventInteractWithEntity) event;
			if (Aura.target != null) {
				eiwe.setCanceled();
			}
		}
		if (event instanceof EventAttackEntity) {
			if (enabled.isEnabled(false) && hitsounds.isEnabled(false) && target != null) {
				float volume = this.volume.getFloatValue();
				if (sound.get() == 0) {
					SoundUtils.playSound("neverlose.wav", volume);
				} else if (sound.get() == 1) {
					SoundUtils.playSound("skeet.wav", volume);
				}
			}
		}
		if (event instanceof EventUpdate) {
			if (this.resolver.isEnabled(false)) {
				this.resolvePlayers();
			}
			this.aura((EventUpdate) event);
			if (this.resolver.isEnabled(false)) {
				this.releaseResolver();
			}
			Jesus.jesusTick = false;
			if (mc.player.onGround && !MoveUtils.isInLiquid() && !mc.player.isOnLadder() && !mc.player.isInWeb
					&& !mc.player.isPotionActive(MobEffects.SLOWNESS) && enabled.isEnabled(false) && target != null
					&& criticals_autojump.isEnabled(false)) {
				mc.player.jump();
			}
		}
		if (event instanceof EventEntitySync) {
			if (enabled.isEnabled(false) && target != null) {
				mc.player.rotationYawHead = visibleRotation.x;
				mc.player.renderYawOffset = visibleRotation.x;
				mc.player.rotationPitchHead = visibleRotation.y;
				if (clientLook.isEnabled(false)) {
					mc.player.rotationYaw = visibleRotation.x;
					mc.player.rotationPitch = visibleRotation.y;
				}
				((EventEntitySync) event).setYaw(serverRotation.x);
				((EventEntitySync) event).setPitch(serverRotation.y);
			}
		}
		if (event instanceof EventRender3D) {
			if (targetesp.isEnabled(false)) {
				EntityLivingBase entity = Aura.target;
				if (entity != null) {
					double cs = prevCircleStep + (circleStep - prevCircleStep) * mc.getRenderPartialTicks();
					double prevSinAnim = RenderUtils.absSinAnimation(cs - 0.15);
					double sinAnim = RenderUtils.absSinAnimation(cs);
					double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * mc.getRenderPartialTicks()
							- mc.getRenderManager().renderPosX;
					double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * mc.getRenderPartialTicks()
							- mc.getRenderManager().renderPosY + prevSinAnim * Module.getEyeHeight(entity);
					double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * mc.getRenderPartialTicks()
							- mc.getRenderManager().renderPosZ;
					double nextY = entity.lastTickPosY
							+ (entity.posY - entity.lastTickPosY) * mc.getRenderPartialTicks()
							- mc.getRenderManager().renderPosY + sinAnim * Module.getEyeHeight(entity);
					float red = targetesp.getColor().getRed() / 255F;
					float green = targetesp.getColor().getGreen() / 255F;
					float blue = targetesp.getColor().getBlue() / 255F;
					GL11.glPushMatrix();
					GL11.glDisable(GL11.GL_CULL_FACE);
					GL11.glDisable(GL11.GL_TEXTURE_2D);
					GL11.glEnable(GL11.GL_BLEND);
					GL11.glDisable(GL11.GL_DEPTH_TEST);
					GL11.glDisable(GL11.GL_ALPHA_TEST);
					GL11.glShadeModel(GL11.GL_SMOOTH);
					GL11.glBegin(GL11.GL_QUAD_STRIP);
					for (int i = 0; i <= 360; i++) {
						if (targetesp.getColor().isRainbow()) {
							int rainbow = PaletteHelper.rainbow(i / 360F);
							red = ((rainbow >> 16) & 255) / 255F;
							green = ((rainbow >> 8) & 255) / 255F;
							blue = (rainbow & 255) / 255F;
						}
						GL11.glColor4f(red, green, blue, 0.6F);
						GL11.glVertex3d(x + Math.cos(Math.toRadians(i)) * entity.width * 0.8, nextY,
								z + Math.sin(Math.toRadians(i)) * entity.width * 0.8);
						GL11.glColor4f(red, green, blue, 0.01F);
						GL11.glVertex3d(x + Math.cos(Math.toRadians(i)) * entity.width * 0.8, y,
								z + Math.sin(Math.toRadians(i)) * entity.width * 0.8);
					}
					GL11.glEnd();
					GL11.glEnable(GL11.GL_LINE_SMOOTH);
					GL11.glBegin(GL11.GL_LINE_LOOP);
					for (int i = 0; i <= 360; i++) {
						if (targetesp.getColor().isRainbow()) {
							int rainbow = PaletteHelper.rainbow(i / 360F);
							red = ((rainbow >> 16) & 255) / 255F;
							green = ((rainbow >> 8) & 255) / 255F;
							blue = (rainbow & 255) / 255F;
						}
						GL11.glColor4f(red, green, blue, 0.8F);
						GL11.glVertex3d(x + Math.cos(Math.toRadians(i)) * entity.width * 0.8, nextY,
								z + Math.sin(Math.toRadians(i)) * entity.width * 0.8);
					}
					GL11.glEnd();
					GL11.glDisable(GL11.GL_LINE_SMOOTH);
					GL11.glEnable(GL11.GL_TEXTURE_2D);
					GL11.glEnable(GL11.GL_ALPHA_TEST);
					GL11.glEnable(GL11.GL_DEPTH_TEST);
					GL11.glShadeModel(GL11.GL_FLAT);
					GL11.glDisable(GL11.GL_BLEND);
					GL11.glEnable(GL11.GL_CULL_FACE);
					GL11.glPopMatrix();
					GlStateManager.resetColor();
				}
			}
		}
		if (event instanceof EventAnimation) {
			if (targetesp.isEnabled(false)) {
				prevCircleStep = circleStep;
				circleStep += 0.15;
			}
		}
	}

	public void resolvePlayers() {
		for (EntityPlayer player : mc.world.playerEntities) {
			if (player instanceof EntityOtherPlayerMP) {
				((EntityOtherPlayerMP) player).resolve();
			}
		}
	}

	public void releaseResolver() {
		for (EntityPlayer player : mc.world.playerEntities) {
			if (player instanceof EntityOtherPlayerMP) {
				((EntityOtherPlayerMP) player).releaseResolver();
			}
		}
	}

	public boolean weaponOnly() {
		if (!weaponOnly.isEnabled(false)) {
			return true;
		}
		return (mc.player.getHeldItemMainhand().getItem() instanceof ItemSword
				|| mc.player.getHeldItemMainhand().getItem() instanceof ItemAxe);
	}

	public void aura(EventUpdate event) {
		boolean shieldDesyncActive = shieldDesync.isEnabled(false);
		if (shieldDesyncOnlyOnAura.isEnabled(false) && target == null) {
			shieldDesyncActive = false;
		}
		if (isActiveItemStackBlocking(mc.player, 4 + new Random().nextInt(4)) && shieldDesyncActive
				&& mc.player.isHandActive()) {
			mc.playerController.onStoppedUsingItem(mc.player);
		}
		if (minCPS > 0) {
			minCPS--;
		}
		if (!enabled.isEnabled(false)) {
			target = null;
			serverRotation.x = mc.player.rotationYaw;
			serverRotation.y = mc.player.rotationPitch;
			visibleRotation.x = mc.player.rotationYaw;
			visibleRotation.y = mc.player.rotationPitch;
			return;
		}
		if (targets.get(6)) {
			for (Entity entity : mc.world.loadedEntityList) {
				if (entity instanceof EntityEnderCrystal) {
					if (getBestHitbox(entity, getDistanceToEntity(entity)) != null
							&& needExplosion(entity.getPositionVector())) {
						attack(entity, event);
						return;
					}
				}
			}
		}
		if (target != null) {
			if (!isEntityValid(target)) {
				target = null;
			}
		}
		if (target == null) {
			target = findTarget();
		}
		if (target == null) {
			serverRotation.x = mc.player.rotationYaw;
			serverRotation.y = mc.player.rotationPitch;
			visibleRotation.x = mc.player.rotationYaw;
			visibleRotation.y = mc.player.rotationPitch;
			return;
		}
		if (!weaponOnly()) {
			return;
		}
		this.thisContextRotatedBefore = false;
		attack(target, event);
		if (!this.thisContextRotatedBefore) {
			rotate(target, false);
		}
	}

	public void attack(Entity base, EventUpdate event) {
		if (base instanceof EntityEnderCrystal || (canAttack() && minCPS == 0)) {
			if (getBestHitbox(base, getDistanceToEntity(base)) != null) {
				boolean crystal = base instanceof EntityEnderCrystal;
				if (!crystal)
					rotate(base, true);
				if (AdvancedCast.instance.getMouseOver(base, serverRotation.x, serverRotation.y,
						distance.getDoubleValue(), ignoreWalls(base)) == base
						|| (crystal && mc.player.getDistanceToEntity(base) <= 4.5)) {
					boolean blocking = mc.player.isHandActive() && mc.player.getActiveItemStack().getItem()
							.getItemUseAction(mc.player.getActiveItemStack()) == EnumAction.BLOCK;
					if (blocking) {
						mc.playerController.onStoppedUsingItem(mc.player);
					}
					boolean needSwap = false;
					if (CPacketEntityAction.lastUpdatedSprint) {
						mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, Action.STOP_SPRINTING));
						needSwap = true;
					}
					minCPS = 10;
					hitTick = true;
					mc.playerController.attackEntity(mc.player, base);
					mc.player.swingArm(EnumHand.MAIN_HAND);
					if (getAxe() >= 0 && shieldBreaker.isEnabled(false) && base instanceof EntityPlayer
							&& isActiveItemStackBlocking((EntityPlayer) base, 1)) {
						mc.player.connection.sendPacket(new CPacketHeldItemChange(getAxe()));
						shieldBreaker((EntityPlayer) base);
						mc.player.connection.sendPacket(new CPacketHeldItemChange(mc.player.inventory.currentItem));
					}
					if (blocking) {
						mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(mc.player.getActiveHand()));
					}
					if (needSwap) {
						mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, Action.START_SPRINTING));
					}
				}
			}
		}
	}

	public double getDistanceToEntity(Entity entity) {
		double dstValue = distance.getDoubleValue();
		if (entity instanceof EntityEnderCrystal) {
			return rotation.get() == 0 ? 4.5 : dstValue;
		}
		return dstValue;
	}

	public void shieldBreaker(EntityPlayer base) {
		mc.playerController.attackEntityNoEvent(mc.player, base);
		mc.player.swingArm(EnumHand.MAIN_HAND);
	}

	public boolean isNakedPlayer(EntityLivingBase base) {
		if (!(base instanceof EntityOtherPlayerMP)) {
			return false;
		}
		return base.getTotalArmorValue() == 0;
	}

	public boolean isInvisible(EntityLivingBase base) {
		if (!(base instanceof EntityOtherPlayerMP)) {
			return false;
		}
		return base.isInvisible();
	}

	public static void init() {
		System.out.println("ez?");
	}

	public void rotate(Entity base, boolean attackContext) {
		this.thisContextRotatedBefore = true;
		Vec3d bestHitbox = getBestHitbox(base, rotate.getDoubleValue() + distance.getDoubleValue());
		if (bestHitbox == null) {
			bestHitbox = base.getPositionEyes(1);
		}
		float pitchToHead = 0;
		EntityPlayerSP client = Minecraft.getMinecraft().player;
		{
			double x = bestHitbox.xCoord - client.posX;
			double y = base.getPositionEyes(1).yCoord - client.getPositionEyes(1).yCoord;
			double z = bestHitbox.zCoord - client.posZ;
			double dst = Math.sqrt(Math.pow(x, 2) + Math.pow(z, 2));
			pitchToHead = (float) (-Math.toDegrees(Math.atan2(y, dst)));
		}
		float sensitivity = 1.0001f;
		double x = bestHitbox.xCoord - client.posX;
		double y = bestHitbox.yCoord - client.getPositionEyes(1).yCoord;
		double z = bestHitbox.zCoord - client.posZ;
		double dst = Math.sqrt(Math.pow(x, 2) + Math.pow(z, 2));
		float yawToTarget = (float) MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(z, x)) - 90);
		float pitchToTarget = (float) (-Math.toDegrees(Math.atan2(y, dst)));
		float yawDelta = MathHelper.wrapDegrees(yawToTarget - serverRotation.x) / sensitivity;
		float pitchDelta = (pitchToTarget - serverRotation.y) / sensitivity;
		if (yawDelta > 180) {
			yawDelta = yawDelta - 180;
		}
		int yawDeltaAbs = (int) Math.abs(yawDelta);
		if (yawDeltaAbs < fov.getDoubleValue()) {
			visibleRotation = new Vector2f(visibleRotation.x + MathHelper.wrapDegrees(yawToTarget - visibleRotation.x),
					pitchToHead);
			switch (rotation.get()) {
			case 0: {
				float pitchDeltaAbs = Math.abs(pitchDelta);
				float additionYaw = Math.min(Math.max(yawDeltaAbs, 1), 80);
				float additionPitch = Math.max(attackContext ? pitchDeltaAbs : 1, 2);
				if (Math.abs(additionYaw - this.prevAdditionYaw) <= 3.0f) {
					additionYaw = this.prevAdditionYaw + 3.1f;
				}
				float newYaw = serverRotation.x + (yawDelta > 0 ? additionYaw : -additionYaw) * sensitivity;
				float newPitch = MathHelper.clamp(
						serverRotation.y + (pitchDelta > 0 ? additionPitch : -additionPitch) * sensitivity, -90, 90);
				serverRotation.x = newYaw;
				serverRotation.y = newPitch;
				this.prevAdditionYaw = additionYaw;
				break;
			}
			case 1: {
				if (attackContext) {
					int pitchDeltaAbs = (int) Math.abs(pitchDelta);
					int additionYaw = (int) yawDeltaAbs;
					int additionPitch = (int) (pitchDeltaAbs);
					float newYaw = serverRotation.x + (yawDelta > 0 ? additionYaw : -additionYaw) * sensitivity;
					float newPitch = MathHelper.clamp(
							serverRotation.y + (pitchDelta > 0 ? additionPitch : -additionPitch) * sensitivity, -90,
							90);
					serverRotation.x = newYaw;
					serverRotation.y = newPitch;
					mc.player.rotationYaw = newYaw;
					mc.player.rotationPitch = newPitch;
				}
				break;
			}
			}
		} else {
			visibleRotation.x = mc.player.rotationYaw;
			visibleRotation.y = mc.player.rotationPitch;
		}
	}

	public boolean needExplosion(Vec3d position) {
		ExplosionBuilder builder = new ExplosionBuilder(mc.world, null, position.xCoord, position.yCoord,
				position.zCoord, 6, new ArrayList());
		boolean needExplosion = false;
		for (Entry<EntityPlayer, Float> entry : builder.damageMap.entrySet()) {
			if (entry.getKey().bot) {
				continue;
			}
			if (WexSide.friendManager.isFriend(entry.getKey().getName())
					&& entry.getValue().floatValue() > entry.getKey().getHealth()) {
				return false;
			}
			if (entry.getKey() == mc.player && entry.getValue().floatValue() > 25) {
				return false;
			}
			if (entry.getValue() > 35) {
				needExplosion = true;
			}
		}
		return needExplosion;
	}

	public boolean canAttack() {
		EntityPlayerSP client = mc.player;
		boolean reasonForCancelCritical = client.isPotionActive(MobEffects.SLOWNESS) || client.isOnLadder()
				|| MoveUtils.isInLiquid() || client.isInWeb;
		if (client.getCooledAttackStrength(1.5f) < 0.93) {
			return false;
		}
		if (Jesus.jesusTick) {
			Jesus.jesusTick = false;
			return true;
		}
		if (!reasonForCancelCritical && criticals.isEnabled(false)) {
			int r = (int) mc.player.posY;
			int c = (int) Math.ceil(mc.player.posY);
			if (r != c && mc.player.onGround && MoveUtils.isBlockAboveHead()) {
				return true;
			}
			return !client.onGround && client.fallDistance > 0;
		}
		return true;
	}

	public static int getAxe() {
		for (int i = 0; i < 9; i++) {
			ItemStack s = mc.player.inventory.getStackInSlot(i);
			if (s.getItem() instanceof ItemAxe) {
				return i;
			}
		}
		return -1;
	}

	public EntityLivingBase findTarget() {
		List<EntityLivingBase> targets = new ArrayList<>();
		for (Entity entity : mc.world.loadedEntityList) {
			if (entity instanceof EntityLivingBase && isEntityValid((EntityLivingBase) entity)) {
				targets.add((EntityLivingBase) entity);
			}
		}
		targets.sort((e1, e2) -> {
			int dst1 = (int) (mc.player.getDistanceToEntity(e1) * 1000);
			int dst2 = (int) (mc.player.getDistanceToEntity(e2) * 1000);
			return dst1 - dst2;
		});
		return targets.isEmpty() ? null : targets.get(0);
	}

	public boolean isEntityValid(EntityLivingBase entity) {
		if (ignoreNaked.isEnabled(false)) {
			if (isNakedPlayer(entity))
				return false;
		}
		if (ignoreInvisible.isEnabled(false)) {
			if (isInvisible(entity))
				return false;
		}
		if (entity.getHealth() <= 0) {
			return false;
		}
		if (entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entity;
			if (player.bot) {
				return false;
			}
		}
		if (!targetsCheck(entity)) {
			return false;
		}
		if (!ignoreWalls(entity)) {
			if (getBestHitbox(entity, rotate.getDoubleValue() + distance.getDoubleValue()) == null) {
				return false;
			}
		} else
			return !(entity.getDistanceToEntity(mc.player) > distance.getDoubleValue() + rotate.getDoubleValue());
		return true;
	}

	public Vec3d getBestHitbox(Entity target, double rotateDistance) {
		if (target.getDistanceSqToEntity(target) >= 36) {
			return null;
		}
		Vec3d head = findHitboxCoord(Hitbox.HEAD, target);
		Vec3d chest = findHitboxCoord(Hitbox.CHEST, target);
		Vec3d legs = findHitboxCoord(Hitbox.LEGS, target);
		ArrayList<Vec3d> points = new ArrayList<>(Arrays.asList(head, chest, legs));
		points.removeIf(point -> !isHitBoxVisible(target, point, rotateDistance));
		if (points.isEmpty()) {
			return null;
		}
		points.sort((d1, d2) -> {
			Vector2f r1 = getDeltaForCoord(serverRotation, d1);
			Vector2f r2 = getDeltaForCoord(serverRotation, d2);
			float y1 = Math.abs(r1.y);
			float y2 = Math.abs(r2.y);
			return (int) ((y1 - y2) * 1000);
		});
		return points.get(0);
	}

	public boolean targetsCheck(EntityLivingBase entity) {
		CastHelper castHelper = new CastHelper();
		if (targets.get(0)) {
			castHelper.apply(EntityType.PLAYERS);
		}
		if (targets.get(1)) {
			castHelper.apply(EntityType.MOBS);
		}
		if (targets.get(2)) {
			castHelper.apply(EntityType.ANIMALS);
		}
		if (targets.get(3)) {
			castHelper.apply(EntityType.VILLAGERS);
		}
		if (targets.get(4)) {
			castHelper.apply(EntityType.FRIENDS);
		}
		if (entity instanceof EntitySlime) {
			return targets.get(5);
		}
		return CastHelper.isInstanceof(entity, castHelper.build()) != null && !entity.isDead;
	}

	public boolean ignoreWalls(Entity input) {
		if (input instanceof EntityEnderCrystal) {
			return true;
		}
		BlockPos pos = new BlockPos(mc.player.lastReportedPosX, mc.player.lastReportedPosY, mc.player.lastReportedPosZ);
		if (mc.world.getBlockState(pos).getMaterial() != Material.AIR && rotation.get() == 0) {
			return true;
		}
		return ignoreWalls.isEnabled(false);
	}

	public static Vector2f getDeltaForCoord(Vector2f rot, Vec3d point) {
		EntityPlayerSP client = Minecraft.getMinecraft().player;
		double x = point.xCoord - client.posX;
		double y = point.yCoord - client.getPositionEyes(1).yCoord;
		double z = point.zCoord - client.posZ;
		double dst = Math.sqrt(Math.pow(x, 2) + Math.pow(z, 2));
		float yawToTarget = (float) MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(z, x)) - 90);
		float pitchToTarget = (float) (-Math.toDegrees(Math.atan2(y, dst)));
		float yawDelta = MathHelper.wrapDegrees(yawToTarget - rot.x);
		float pitchDelta = (pitchToTarget - rot.y);
		return new Vector2f(yawDelta, pitchDelta);
	}

	public boolean isHitBoxVisible(Entity target, Vec3d vector, double dst) {
		return RaycastHelper.getPointedEntity(getRotationForCoord(vector), dst, 1, !ignoreWalls(target),
				target) == target;
	}

	public static Vector2f getRotationForCoord(Vec3d point) {
		EntityPlayerSP client = Minecraft.getMinecraft().player;
		double x = point.xCoord - client.posX;
		double y = point.yCoord - client.getPositionEyes(1).yCoord;
		double z = point.zCoord - client.posZ;
		double dst = Math.sqrt(Math.pow(x, 2) + Math.pow(z, 2));
		float yawToTarget = (float) MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(z, x)) - 90);
		float pitchToTarget = (float) (-Math.toDegrees(Math.atan2(y, dst)));
		return new Vector2f(yawToTarget, pitchToTarget);
	}

	public static Vec3d findHitboxCoord(Hitbox box, Entity target) {
		double yCoord = 0;
		switch (box) {
		case HEAD:
			yCoord = target.getEyeHeight();
			break;
		case CHEST:
			yCoord = target.getEyeHeight() / 2;
			break;
		case LEGS:
			yCoord = 0.05;
			break;
		}
		return target.getPositionVector().addVector(0, yCoord, 0);
	}

	public static boolean isActiveItemStackBlocking(EntityPlayer other, int time) {
		if (other.isHandActive() && !other.activeItemStack.isEmpty()) {
			Item item = other.activeItemStack.getItem();
			if (item.getItemUseAction(other.activeItemStack) != EnumAction.BLOCK) {
				return false;
			} else {
				return item.getMaxItemUseDuration(other.activeItemStack) - other.activeItemStackUseCount >= time;
			}
		} else {
			return false;
		}
	}

	public enum Hitbox {
		HEAD, CHEST, LEGS
	}
}
