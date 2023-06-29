package volcano.summer.client.modules.misc;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.MathHelper;
import volcano.summer.base.manager.event.Event;
import volcano.summer.base.manager.module.Module;
import volcano.summer.client.events.EventUpdate;
import volcano.summer.client.util.TimerUtil;
import volcano.summer.client.value.Value;

public class QuakeAura extends Module {

	public final Value<Boolean> lock = new Value("Lock", "lock", Boolean.valueOf(false), this);

	private TimerUtil timer;
	int delay;
	public static boolean cancelrotations = false;

	public QuakeAura() {
		super("QuakeAura", 0, Category.MISC);
		this.timer = new TimerUtil();
	}

	@Override
	public void onEnable() {
	}

	@Override
	public void onDisable() {
	}

	public static float[] getRotationsNeeded(Entity entity) {
		if (entity == null) {
			return null;
		}
		double diffX = entity.posX - Minecraft.getMinecraft().thePlayer.posX;
		double diffY;
		if ((entity instanceof EntityLivingBase)) {
			EntityLivingBase entityLivingBase = (EntityLivingBase) entity;
			diffY = entityLivingBase.posY + entityLivingBase.getEyeHeight() * 0.9D
					- (Minecraft.getMinecraft().thePlayer.posY + Minecraft.getMinecraft().thePlayer.getEyeHeight());
		} else {
			diffY = (entity.boundingBox.minY + entity.boundingBox.maxY) / 2.0D
					- (Minecraft.getMinecraft().thePlayer.posY + Minecraft.getMinecraft().thePlayer.getEyeHeight());
		}
		double diffZ = entity.posZ - Minecraft.getMinecraft().thePlayer.posZ;
		double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
		float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0D / 3.141592653589793D) - 90.0F;
		float pitch = (float) -(Math.atan2(diffY, dist) * 180.0D / 3.141592653589793D);
		return new float[] {
				Minecraft.getMinecraft().thePlayer.rotationYaw
						+ MathHelper.wrapAngleTo180_float(yaw - Minecraft.getMinecraft().thePlayer.rotationYaw),
				Minecraft.getMinecraft().thePlayer.rotationPitch
						+ MathHelper.wrapAngleTo180_float(pitch - Minecraft.getMinecraft().thePlayer.rotationPitch) };
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventUpdate) {
			List list = mc.theWorld.playerEntities;
			this.delay += 1;
			for (int k = 0; k < list.size(); k++) {
				if (((EntityPlayer) list.get(k)).getName() != mc.thePlayer.getName()) {
					EntityPlayer entityplayer = (EntityPlayer) list.get(1);
					if (mc.thePlayer.getDistanceToEntity(entityplayer) > mc.thePlayer
							.getDistanceToEntity((Entity) list.get(k))) {
						entityplayer = (EntityPlayer) list.get(k);
					}
					float f = mc.thePlayer.getDistanceToEntity(entityplayer);
					if ((f < 100.0F) && (mc.thePlayer.canEntityBeSeen(entityplayer)) && (!entityplayer.isInvisible())
							&& (mc.thePlayer.experience <= 0.0F) && (mc.thePlayer.getHeldItem() != null)
							&& (mc.thePlayer.getHeldItem().getItem() != null)) {
						if (this.delay > 6) {
							if ((mc.thePlayer.getHeldItem() != null)
									&& (mc.thePlayer.getHeldItem().getItem() != null)) {
								float[] rotations = this.getRotationsNeeded(entityplayer);
								if (!this.lock.getValue()) {
									this.cancelrotations = true;
									mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(
											rotations[0], rotations[1], mc.thePlayer.onGround));
								}
								if (this.lock.getValue()) {
									this.cancelrotations = false;
									Minecraft.getMinecraft().thePlayer.rotationYaw = rotations[0];
									Minecraft.getMinecraft().thePlayer.rotationPitch = rotations[1];
								}
								mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld,
										mc.thePlayer.getCurrentEquippedItem());
								this.delay = 0;
							} else {
								this.cancelrotations = false;
							}
						} else {
							this.cancelrotations = false;
						}
					} else {
						this.cancelrotations = false;
					}
				}
			}
		}
	}
}