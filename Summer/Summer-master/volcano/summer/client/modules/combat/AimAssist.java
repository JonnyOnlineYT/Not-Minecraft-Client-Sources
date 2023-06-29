package volcano.summer.client.modules.combat;

import java.util.Objects;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import volcano.summer.base.manager.event.Event;
import volcano.summer.base.manager.friend.FriendManager;
import volcano.summer.base.manager.module.Module;
import volcano.summer.client.events.EventPreMotionUpdate;
import volcano.summer.client.util.TimerUtil;
import volcano.summer.client.value.ClampedValue;
import volcano.summer.client.value.Value;

public class AimAssist extends Module {

	TimerUtil timer = new TimerUtil();
	public static Value<Boolean> pitch;
	public static Value<Double> range;
	public static Value<Double> speed;
	public static Value<Double> fov;

	public AimAssist() {
		super("AimAssist", 0, Category.COMBAT);
		pitch = new Value<Boolean>("Pitch", "pitch", false, this);
		range = new ClampedValue<Double>("Range", "range", 4.25, 1.0, 6.0, this);
		speed = new ClampedValue<Double>("AimSpeed", "AimSpeed", 8.0, 0.0, 15.0, this);
		fov = new ClampedValue<Double>("Fov", "fov", 80.0, 0.0, 360.0, this);
	}

	@Override
	public void onEnable() {
	}

	@Override
	public void onDisable() {
	}

	private EntityPlayer getClosestPlayerToCursor(float angle) {
		float distance = angle;
		EntityPlayer tempPlayer = null;
		for (Object player : this.mc.theWorld.loadedEntityList) {
			if ((player instanceof EntityPlayer)) {
				EntityPlayer entity = (EntityPlayer) player;
				if (checkValidity(entity)) {
					float yaw = getYawChange(player);
					float pitch = getPitchChange(player);
					if ((yaw <= angle) && (pitch <= angle)) {
						float currentDistance = (yaw + pitch) / 2.0F;
						if (currentDistance <= distance) {
							distance = currentDistance;
							tempPlayer = (EntityPlayer) player;
						}
					}
				}
			}
		}
		return tempPlayer;
	}

	public static float getYawChange(Object player) {
		EntityLivingBase playera = (EntityLivingBase) player;
		double deltaX = playera.posX - mc.thePlayer.posX;
		double deltaZ = playera.posZ - mc.thePlayer.posZ;
		double yawToEntity;
		if ((deltaZ < 0.0D) && (deltaX < 0.0D)) {
			yawToEntity = 90.0D + Math.toDegrees(Math.atan(deltaZ / deltaX));
		} else {
			if ((deltaZ < 0.0D) && (deltaX > 0.0D)) {
				yawToEntity = -90.0D + Math.toDegrees(Math.atan(deltaZ / deltaX));
			} else {
				yawToEntity = Math.toDegrees(-Math.atan(deltaX / deltaZ));
			}
		}
		return MathHelper.wrapAngleTo180_float(-(mc.thePlayer.rotationYaw - (float) yawToEntity));
	}

	public static float getPitchChange(Object entity) {
		EntityLivingBase entitya = (EntityLivingBase) entity;
		double deltaX = entitya.posX - mc.thePlayer.posX;
		double deltaZ = entitya.posZ - mc.thePlayer.posZ;
		double deltaY = entitya.posY - 2.2D + ((Entity) entity).getEyeHeight() - mc.thePlayer.posY;
		double distanceXZ = MathHelper.sqrt_double(deltaX * deltaX + deltaZ * deltaZ);
		double pitchToEntity = -Math.toDegrees(Math.atan(deltaY / distanceXZ));
		return -MathHelper.wrapAngleTo180_float(mc.thePlayer.rotationPitch - (float) pitchToEntity);
	}

	private boolean checkValidity(EntityPlayer player) {
		return (Objects.nonNull(player)) && (player.isEntityAlive())
				&& (player.getDistanceToEntity(this.mc.thePlayer) <= (this.range.getValue().floatValue()))
				&& (player.ticksExisted > 20) && (!player.isInvisibleToPlayer(this.mc.thePlayer))
				&& (!FriendManager.isFriend(player.getName()));
	}

	@Override
	public void onEvent(Event event) {
		if ((event instanceof EventPreMotionUpdate)) {
			EntityPlayer target = getClosestPlayerToCursor(this.fov.getValue().floatValue());
			if (Objects.nonNull(target)) {
				this.mc.thePlayer.rotationYaw += getYawChange(target) / (this.speed.getValue()).floatValue();
				if (this.pitch.getValue().booleanValue()) {
					this.mc.thePlayer.rotationPitch += getPitchChange(target) / (this.speed.getValue()).floatValue();
				}
			}
		}
	}
}