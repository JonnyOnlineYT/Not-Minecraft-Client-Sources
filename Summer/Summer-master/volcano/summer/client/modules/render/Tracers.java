package volcano.summer.client.modules.render;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Timer;
import volcano.summer.base.Summer;
import volcano.summer.base.manager.event.Event;
import volcano.summer.base.manager.friend.FriendManager;
import volcano.summer.base.manager.module.Module;
import volcano.summer.client.events.EventRender3D;

public class Tracers extends Module {

	public Tracers() {
		super("Tracers", 0, Category.RENDER);
	}

	@Override
	public void onEnable() {

	}

	@Override
	public void onDisable() {

	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventRender3D) {
			WorldClient world = this.mc.theWorld;
			EntityPlayerSP player = this.mc.thePlayer;
			for (Object o : this.mc.theWorld.playerEntities) {
				EntityPlayer entity = (EntityPlayer) o;
				if ((entity != this.mc.thePlayer) && (!entity.isInvisible())) {
					if (shouldTrace(entity)) {
						Timer timer = this.mc.timer;
						float posX = (float) interpolate(entity.posX, entity.lastTickPosX, timer.renderPartialTicks)
								- (float) this.mc.getRenderManager().renderPosX;
						float posY = (float) interpolate(entity.posY, entity.lastTickPosY, timer.renderPartialTicks)
								- (float) this.mc.getRenderManager().renderPosY;
						float posZ = (float) interpolate(entity.posZ, entity.lastTickPosZ, timer.renderPartialTicks)
								- (float) this.mc.getRenderManager().renderPosZ;
						GL11.glPushMatrix();
						GL11.glDisable(2896);
						GL11.glDisable(3553);
						GL11.glEnable(3042);
						GL11.glDisable(2929);
						GL11.glEnable(2848);
						boolean spine = !Summer.moduleManager.getModule(PlayerESP.class).state;
						for (int time = 1; time < 3; time++) {
							GL11.glLineWidth(time == 1 ? 1.5F : 0.5F);
							setColor(entity, time == 1);
							if (spine) {
								GL11.glBegin(3);
								GL11.glVertex3d(0.0D, player.getEyeHeight(), 0.0D);
								GL11.glVertex3d(posX, posY, posZ);
								GL11.glVertex3d(posX, posY + entity.getEyeHeight(), posZ);
								GL11.glEnd();
							} else {
								GL11.glBegin(1);
								GL11.glVertex3d(0.0D, player.getEyeHeight(), 0.0D);
								GL11.glVertex3d(posX, posY + 0.1D, posZ);
								GL11.glEnd();
							}
							GL11.glLineWidth(time == 1 ? 2.5F : 0.5F);
						}
						GL11.glDisable(2848);
						GL11.glEnable(2929);
						GL11.glDisable(3042);
						GL11.glEnable(3553);
						GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
						GL11.glPopMatrix();
					}
				}
			}
		}
	}

	private void setColor(Entity entity, boolean alias) {
		if (FriendManager.isFriend(entity.getName())) {
			GL11.glColor4f(0.0F, 1.0F, 1.0F, alias ? 0.5F : 1.0F);
		} else {
			EntityPlayerSP player = this.mc.thePlayer;
			double distance = player.getDistanceToEntity(entity) / 120.0D;
			// r g b alpha
			GL11.glColor4d(1.0D, 1.0D + distance, 1.0D, alias ? 0.5D : 2.0D);
		}
	}

	private double interpolate(double now, double then, double percent) {
		return then + (now - then) * percent;
	}

	private boolean shouldTrace(Entity entity) {
		EntityPlayerSP player = this.mc.thePlayer;
		return ((entity instanceof EntityPlayer)) && (player != entity);
	}
}
