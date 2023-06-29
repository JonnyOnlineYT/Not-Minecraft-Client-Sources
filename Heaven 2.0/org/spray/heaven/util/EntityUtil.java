package org.spray.heaven.util;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.spray.heaven.features.module.modules.player.AntiBot;
import org.spray.heaven.main.Wrapper;

import com.google.common.base.Predicates;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameType;
import optifine.Reflector;

public class EntityUtil {

	public enum EnumPriority {
		CLOSEST, CROSSHAIR, HEALTH;
	}

	public static Comparator<Entity> getPriority(EnumPriority priority) {
		Comparator<Entity> comparator;

		if (priority.equals(EnumPriority.CROSSHAIR)) {
			comparator = Comparator.comparing(e -> {
				Vec3d center = e.getEntityBoundingBox().getCenter();

				double diffX = center.xCoord - Wrapper.getPlayer().posX;
				double diffY = center.yCoord - Wrapper.getPlayer().getEyeHeight();
				double diffZ = center.zCoord - Wrapper.getPlayer().posZ;

				double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);

				float yaw = (float) Math.toDegrees(Math.atan2(diffZ, diffX)) - 90F;
				float pitch = (float) -Math.toDegrees(Math.atan2(diffY, diffXZ));

				return -(Math.abs(MathHelper.wrapDegrees(yaw - Wrapper.getPlayer().rotationYaw))
						+ Math.abs(MathHelper.wrapDegrees(pitch - Wrapper.getPlayer().rotationPitch)));
			});
		} else if (priority.equals(EnumPriority.HEALTH)) {
			comparator = Comparator.comparing(e -> -((EntityLivingBase) e).getHealth());
		} else {
			comparator = Comparator.comparing(e -> -Wrapper.getPlayer().getDistanceToEntity(e));
		}

		return comparator;
	}

	public static boolean isBlockAboveHead() {
		AxisAlignedBB bb = new AxisAlignedBB(Wrapper.getPlayer().posX - 0.3,
				Wrapper.getPlayer().posY + (double) Wrapper.getPlayer().getEyeHeight(), Wrapper.getPlayer().posZ + 0.3,
				Wrapper.getPlayer().posX + 0.3, Wrapper.getPlayer().posY + 2.5, Wrapper.getPlayer().posZ - 0.3);
		return !Wrapper.MC.world.getCollisionBoxes(Wrapper.getPlayer(), bb).isEmpty();
	}

	public static boolean inTrapDoor() {
		return BlockUtil
				.getBlock(new BlockPos(Wrapper.getPlayer().posX,
						Wrapper.getPlayer().posY + Wrapper.getPlayer().getEyeHeight() + 0.1,
						Wrapper.getPlayer().posZ)) == Blocks.TRAPDOOR
				|| BlockUtil.getBlock(new BlockPos(Wrapper.getPlayer().posX, Wrapper.getPlayer().posY - 0.1,
						Wrapper.getPlayer().posZ)) == Blocks.TRAPDOOR;
	}

	public static Entity getTarget(double range, double fov, boolean players, boolean friends, boolean naked,
			boolean invisible, boolean mobs, boolean animals, boolean throughWalls) {
		return getTarget(EnumPriority.CLOSEST.name(), range, fov, players, friends, naked, invisible, mobs, animals,
				throughWalls);
	}

	public static Entity getTarget(String priority, double range, double fov, boolean players, boolean friends,
			boolean naked, boolean invisible, boolean mobs, boolean animals, boolean throughWalls) {
		Entity entity = null;
		for (Entity e : getTargets(priority, fov, players, friends, naked, invisible, mobs, animals, throughWalls)) {
			if (e instanceof EntityLivingBase) {
				if (Wrapper.getPlayer().getDistanceToEntity(e) <= range)
					entity = e;
			}
		}
		return entity;
	}

	public static List<Entity> getTargets(String priority, double fov, boolean players, boolean friends, boolean naked,
			boolean invisible, boolean mobs, boolean animals, boolean throughWalls) {
		return Wrapper.getWorld().loadedEntityList.stream()
				.filter(entity -> isValid(entity, fov, players, friends, naked, invisible, mobs, animals, throughWalls))
				.sorted(getPriority(EnumPriority.valueOf(priority.toUpperCase()))).collect(Collectors.toList());
	}

	public static boolean isValid(Entity entity, double fov, boolean players, boolean friends, boolean naked,
			boolean invisible, boolean mobs, boolean animals, boolean throughWalls) {
		if (entity instanceof EntityPlayer
				&& !((AntiBot) Wrapper.getModule().get("AntiBot")).isValid((EntityPlayer) entity))
			return false;

		if (!RotationUtil.isInFOV(entity, fov))
			return false;

		if (!throughWalls && !Wrapper.getPlayer().canEntityBeSeen(entity))
			return false;

		if (naked && entity != Wrapper.getPlayer() && !Wrapper.getFriend().contains(entity.getName())
				&& entity instanceof EntityPlayer) {
			return !isNaked((EntityPlayer) entity);
		}

		return isValid(entity, players, friends, invisible, mobs, animals);
	}

	public static boolean isValid(Entity entity, boolean players, boolean friends, boolean self, boolean invisible,
			boolean mobs, boolean animals) {
		if (isValid(entity, players, friends, invisible, mobs, animals))
			return true;

		return self && entity == Wrapper.getPlayer() && Wrapper.MC.gameSettings.thirdPersonView != 0;
	}

	public static boolean isValid(Entity entity, boolean players, boolean friends, boolean invisibles, boolean mobs,
			boolean animals) {
		if (!(entity instanceof EntityLivingBase) || entity == Wrapper.getPlayer()
				|| entity instanceof EntityArmorStand)
			return false;

		if (((EntityLivingBase) entity).getHealth() <= 0)
			return false;

		if (!invisibles && entity.isInvisible())
			return false;

		if (!friends && Wrapper.getFriend().contains(entity.getName()))
			return false;

		if (players && entity instanceof EntityPlayer)
			return true;

		if (mobs && isMonster(entity))
			return true;

		return animals && isAnimal(entity);
	}

	public static boolean isAnimal(Entity e) {
		return e instanceof IAnimals;
	}

	public static boolean isMonster(Entity e) {
		return e instanceof IMob;
	}

	public static void damage() {
		Objects.requireNonNull(Wrapper.MC.getConnection()).sendPacket(new CPacketAnimation(EnumHand.MAIN_HAND));
		Wrapper.MC.getConnection().sendPacket(new CPacketPlayer.Position(Wrapper.getPlayer().posX,
				Wrapper.getPlayer().posY, Wrapper.getPlayer().posZ, false));
		for (int i = 0; i < 8; i++) {
			Wrapper.MC.getConnection().sendPacket(new CPacketPlayer.Position(Wrapper.getPlayer().posX,
					Wrapper.getPlayer().posY + 0.3990, Wrapper.getPlayer().posZ, false));
			Wrapper.MC.getConnection().sendPacket(new CPacketPlayer.Position(Wrapper.getPlayer().posX,
					Wrapper.getPlayer().posY, Wrapper.getPlayer().posZ, false));
		}
		Wrapper.MC.getConnection().sendPacket(new CPacketPlayer.Position(Wrapper.getPlayer().posX,
				Wrapper.getPlayer().posY, Wrapper.getPlayer().posZ, true));
		Wrapper.MC.getConnection().sendPacket(new CPacketPlayer.Position(Wrapper.getPlayer().posX,
				Wrapper.getPlayer().posY, Wrapper.getPlayer().posZ, true));
	}

	public static Vec3d interpolateVec(Entity entity) {
		double[] i = interpolate(entity);
		return new Vec3d(i[0], i[1], i[2]);
	}

	public static double[] interpolate(Entity entity) {
		double partialTicks = Wrapper.MC.getRenderPartialTicks();
		return new double[] { entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks,
				entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks,
				entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks };
	}

	public static double getDistance(Vec3d pos, double x, double y, double z) {
		final double deltaX = pos.xCoord - x;
		final double deltaY = pos.yCoord - y;
		final double deltaZ = pos.zCoord - z;
		return MathHelper.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);
	}

	public static int getPing(EntityPlayer player) {
		if (player == null)
			return 0;

		NetworkPlayerInfo networkPlayer = Wrapper.MC.getConnection().getPlayerInfo(player.getUniqueID());

		if (networkPlayer != null)
			return networkPlayer.getResponseTime();

		return 0;
	}

	public static double getSpeedBPS(Entity entity) {
		double tX = Math.abs(entity.posX - entity.prevPosX);
		double tZ = Math.abs(entity.posZ - entity.prevPosZ);
		double length = Math.sqrt(tX * tX + tZ * tZ);

		length *= Wrapper.MC.getRenderPartialTicks();

		return length * 20;
	}

	public static boolean isFluid(double y) {
		return BlockUtil.getBlock(new BlockPos(Wrapper.getPlayer().posX, Wrapper.getPlayer().posY + y,
				Wrapper.getPlayer().posZ)) == Blocks.WATER;
	}

	public static boolean isBlocking(EntityPlayer player) {
		return player.isHandActive() && player.getActiveItemStack().getItem()
				.getItemUseAction(player.getActiveItemStack()) == EnumAction.BLOCK;
	}

	public static void throwPearl() {
		int pearlSlot = InvUtil.findItemFromHotbar(Items.ENDER_PEARL);

		if (pearlSlot != -1) {
			Wrapper.sendPacket(
					new CPacketPlayer.Rotation(Wrapper.getPlayer().rotationYaw, 90, Wrapper.getPlayer().onGround));
			Wrapper.getPlayer().connection.sendPacket(new CPacketHeldItemChange(pearlSlot));
			Wrapper.getPlayer().connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
			Wrapper.getPlayer().connection
					.sendPacket(new CPacketHeldItemChange(Wrapper.getPlayer().inventory.currentItem));

			Wrapper.sendPacket(new CPacketPlayer.Rotation(Wrapper.getPlayer().rotationYaw,
					Wrapper.getPlayer().rotationPitch, Wrapper.getPlayer().onGround));
		}
	}

	public static BlockPos getFloorPos(Entity entity) {
		return new BlockPos(Math.floor(entity.posX), Math.floor(entity.posY), Math.floor(entity.posZ));
	}

	public static BlockPos getFloorPos() {
		return getFloorPos(Wrapper.getPlayer());
	}

	public static boolean isNaked(EntityPlayer player) {
		ItemStack chest = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
		ItemStack legs = player.getItemStackFromSlot(EntityEquipmentSlot.LEGS);
		ItemStack feet = player.getItemStackFromSlot(EntityEquipmentSlot.FEET);
		ItemStack head = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);

		return chest.isEmpty() && legs.isEmpty() && feet.isEmpty() && head.isEmpty();
	}

	/**
	 * Checks if the player has armor, diamond armor and if he has enchants
	 *
	 * @param player
	 * @return
	 */
	public static boolean isToper(EntityPlayer player) {
		return player.inventory.armorInventory.size() >= 3 && player.inventory.armorInventory.stream().allMatch(
				armor -> ((ItemArmor) armor.getItem()).getArmorMaterial().equals(ItemArmor.ArmorMaterial.DIAMOND)
						&& EnchantmentHelper.getEnchantments(armor).size() > 0);
	}

	public static Entity raycastEntity(final double range, final float yaw, final float pitch,
			final IEntityFilter entityFilter) {
		final Entity renderViewEntity = Wrapper.MC.getRenderViewEntity();

		if (renderViewEntity != null && Wrapper.getWorld() != null) {
			double blockReachDistance = range;
			final Vec3d eyePosition = renderViewEntity.getPositionEyes(1F);

			final float yawCos = MathHelper.cos(-yaw * 0.017453292F - (float) Math.PI);
			final float yawSin = MathHelper.sin(-yaw * 0.017453292F - (float) Math.PI);
			final float pitchCos = -MathHelper.cos(-pitch * 0.017453292F);
			final float pitchSin = MathHelper.sin(-pitch * 0.017453292F);

			final Vec3d entityLook = new Vec3d(yawSin * pitchCos, pitchSin, yawCos * pitchCos);
			final Vec3d vector = eyePosition.addVector(entityLook.xCoord * blockReachDistance,
					entityLook.yCoord * blockReachDistance, entityLook.zCoord * blockReachDistance);
			final List<Entity> entityList = Wrapper.getWorld().getEntitiesInAABBexcluding(renderViewEntity,
					renderViewEntity.getEntityBoundingBox()
							.addCoord(entityLook.xCoord * blockReachDistance, entityLook.yCoord * blockReachDistance,
									entityLook.zCoord * blockReachDistance)
							.expand(1D, 1D, 1D),
					Predicates.and(EntitySelectors.NOT_SPECTATING, Entity::canBeCollidedWith));

			Entity pointedEntity = null;

			for (final Entity entity : entityList) {
				if (!entityFilter.canRaycast(entity))
					continue;

				final float collisionBorderSize = entity.getCollisionBorderSize();
				final AxisAlignedBB axisAlignedBB = entity.getEntityBoundingBox().expand(collisionBorderSize,
						collisionBorderSize, collisionBorderSize);
				final RayTraceResult movingObjectPosition = axisAlignedBB.calculateIntercept(eyePosition, vector);

				if (axisAlignedBB.isVecInside(eyePosition)) {
					if (blockReachDistance >= 0.0D) {
						pointedEntity = entity;
						blockReachDistance = 0.0D;
					}
				} else if (movingObjectPosition != null) {
					final double eyeDistance = eyePosition.distanceTo(movingObjectPosition.hitVec);

					boolean flag1 = false;

					if (Reflector.ForgeEntity_canRiderInteract.exists()) {
						flag1 = Reflector.callBoolean(entity, Reflector.ForgeEntity_canRiderInteract);
					}

					if (eyeDistance < blockReachDistance || blockReachDistance == 0.0D) {
						if (entity == renderViewEntity.getRidingEntity() && !flag1) {
							if (blockReachDistance == 0.0D)
								pointedEntity = entity;
						} else {
							pointedEntity = entity;
							blockReachDistance = eyeDistance;
						}
					}
				}
			}

			return pointedEntity;
		}

		return null;
	}

	public static void setReach(Entity entity, double range) {
		class RangePlayerController extends PlayerControllerMP {
			private float range = (float) Wrapper.MC.playerController.getBlockReachDistance();

			public RangePlayerController(Minecraft mcIn, NetHandlerPlayClient netHandler) {
				super(mcIn, netHandler);
			}

			@Override
			public float getBlockReachDistance() {
				return range;
			}

			public void setBlockReachDistance(float range) {
				this.range = range;
			}
		}
		Minecraft mc = Wrapper.MC;
		EntityPlayer player = Wrapper.getPlayer();
		if (player == entity) {
			if (!(mc.playerController instanceof RangePlayerController)) {
				GameType gameType = mc.playerController.getCurrentGameType();
				NetHandlerPlayClient netClient = mc.playerController.getConnection();
				RangePlayerController controller = new RangePlayerController(mc, netClient);
				boolean isFlying = player.capabilities.isFlying;
				boolean allowFlying = player.capabilities.allowFlying;
				controller.setGameType(gameType);
				player.capabilities.isFlying = isFlying;
				player.capabilities.allowFlying = allowFlying;
				mc.playerController = controller;
			}
			((RangePlayerController) mc.playerController).setBlockReachDistance((float) range);
		}
	}

	public interface IEntityFilter {
		boolean canRaycast(final Entity entity);
	}
}
