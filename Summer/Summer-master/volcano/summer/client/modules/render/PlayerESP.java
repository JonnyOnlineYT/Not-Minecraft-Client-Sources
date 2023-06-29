package volcano.summer.client.modules.render;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Vec3;
import volcano.summer.base.Summer;
import volcano.summer.base.manager.event.Event;
import volcano.summer.base.manager.friend.FriendManager;
import volcano.summer.base.manager.module.Module;
import volcano.summer.client.events.EventRender3D;
import volcano.summer.client.util.GuiUtils;
import volcano.summer.client.util.R3DUtils;
import volcano.summer.client.value.ClampedValue;
import volcano.summer.client.value.ModeValue;
import volcano.summer.client.value.Value;

public class PlayerESP extends Module {

	public static ModeValue playerESPMode;
	public static Value<Boolean> rainbow;
	public static Value<Boolean> skeletal;
	public static Value<Double> skeletalwidth;
	private int ticks = 0;
	private static Map<EntityPlayer, float[][]> entities;

	public PlayerESP() {
		super("PlayerESP", 0, Category.RENDER);
		entities = new HashMap<>();
		playerESPMode = new ModeValue("ESPMode", "Mode", "Outline", new String[] { "Outline", "Box", "2D", "CSGO" }, this);
		rainbow = new Value("Rainbow", "rainbow", Boolean.valueOf(false), this);
		skeletal = new Value("Skeletal", "skeletal", false, this);
		skeletalwidth = new ClampedValue<Double>("SkeletalWidth", "skeletalwidth", 1.0, 0.5, 10.0, this);
	}

	@Override
	public void onEnable() {
		if (this.playerESPMode.getStringValue().equalsIgnoreCase("Outline")) {
			Summer.tellPlayer("Please Turn 'FastRender' Off.");
		}
		if (this.playerESPMode.getStringValue().equalsIgnoreCase("2D")) {
			if (mc.theWorld != null) {
				this.ticks = 0;
			}
		}
	}

	@Override
	public void onDisable() {

	}
	
	public static void addEntity(final EntityPlayer e, final ModelPlayer model) {
        entities.put(e, new float[][] { { model.bipedHead.rotateAngleX, model.bipedHead.rotateAngleY, model.bipedHead.rotateAngleZ }, { model.bipedRightArm.rotateAngleX, model.bipedRightArm.rotateAngleY, model.bipedRightArm.rotateAngleZ }, { model.bipedLeftArm.rotateAngleX, model.bipedLeftArm.rotateAngleY, model.bipedLeftArm.rotateAngleZ }, { model.bipedRightLeg.rotateAngleX, model.bipedRightLeg.rotateAngleY, model.bipedRightLeg.rotateAngleZ }, { model.bipedLeftLeg.rotateAngleX, model.bipedLeftLeg.rotateAngleY, model.bipedLeftLeg.rotateAngleZ } });
    }

	public void draw2DCorner(Entity e, double posX, double posY, double posZ, int color) {
		GlStateManager.pushMatrix();
		GlStateManager.translate(posX, posY, posZ);
		GL11.glNormal3f(0.0F, 0.0F, 0.0F);
		GlStateManager.rotate(-RenderManager.playerViewY, 0.0F, 1.0F, 0.0F);
		GlStateManager.scale(-0.1D, -0.1D, 0.1D);
		GL11.glDisable(2896);
		GL11.glDisable(2929);
		GL11.glEnable(3042);
		GL11.glBlendFunc(770, 771);
		GlStateManager.func_179098_w();
		GlStateManager.depthMask(true);
		GuiUtils.drawRectM(4.0F, -20.0F, 7.0F, -19.0F, color);
		GuiUtils.drawRectM(-7.0F, -20.0F, -4.0F, -19.0F, color);
		GuiUtils.drawRectM(6.0F, -20.0F, 7.0F, -17.5F, color);
		GuiUtils.drawRectM(-7.0F, -20.0F, -6.0F, -17.5F, color);
		GuiUtils.drawRectM(-7.0F, 2.0F, -4.0F, 3.0F, color);
		GuiUtils.drawRectM(4.0F, 2.0F, 7.0F, 3.0F, color);
		GuiUtils.drawRectM(-7.0F, 0.5F, -6.0F, 3.0F, color);
		GuiUtils.drawRectM(6.0F, 0.5F, 7.0F, 3.0F, color);
		GuiUtils.drawRectM(7.0F, -20.0F, 7.3F, -17.5F, -16777216);
		GuiUtils.drawRectM(-7.3F, -20.0F, -7.0F, -17.5F, -16777216);
		GuiUtils.drawRectM(4.0F, -20.3F, 7.3F, -20.0F, -16777216);
		GuiUtils.drawRectM(-7.3F, -20.3F, -4.0F, -20.0F, -16777216);
		GuiUtils.drawRectM(-7.0F, 3.0F, -4.0F, 3.3F, -16777216);
		GuiUtils.drawRectM(4.0F, 3.0F, 7.0F, 3.3F, -16777216);
		GuiUtils.drawRectM(-7.3F, 0.5F, -7.0F, 3.3F, -16777216);
		GuiUtils.drawRectM(7.0F, 0.5F, 7.3F, 3.3F, -16777216);
		GL11.glDisable(3042);
		GL11.glEnable(2929);
		GL11.glEnable(2896);
		GlStateManager.popMatrix();
	}

	public boolean isValidTarget(Entity e) {
		return ((e instanceof EntityPlayer) && (!(e instanceof EntityPlayerSP)));
	}

	public static Color rainbow(long offset, float fade) {
		float hue = (System.nanoTime() + offset) / 1.0E10F % 1.0F;
		long color = Long.parseLong(Integer.toHexString(Integer.valueOf(Color.HSBtoRGB(hue, 1.0F, 1.0F)).intValue()),
				16);
		Color c = new Color((int) color);
		return new Color(c.getRed() / 255.0F * fade, c.getGreen() / 255.0F * fade, c.getBlue() / 255.0F * fade,
				c.getAlpha() / 255.0F);
	}

	public static int getFriendColor() {
		return new Color(0, 0, 255).getRGB();
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventRender3D) {
			if (this.playerESPMode.getStringValue().equalsIgnoreCase("Box")) {
				setDisplayName("Box");
				for (Object entity : this.mc.theWorld.loadedEntityList) {
					boolean distance = this.mc.thePlayer.getDistanceToEntity((Entity) entity) < 16.0F;
					boolean othershit = !(entity instanceof EntityPlayerSP);
					Entity ent = (Entity) entity;
					double posX = ent.lastTickPosX
							+ (ent.posX - ent.lastTickPosX) * ((EventRender3D) event).getPartialTicks()
							- RenderManager.renderPosX;
					double posY = ent.lastTickPosY
							+ (ent.posY - ent.lastTickPosY) * ((EventRender3D) event).getPartialTicks()
							- RenderManager.renderPosY;
					double posZ = ent.lastTickPosZ
							+ (ent.posZ - ent.lastTickPosZ) * ((EventRender3D) event).getPartialTicks()
							- RenderManager.renderPosZ;
					if ((isValidTarget(ent)) && (this.playerESPMode.getStringValue().equalsIgnoreCase("Box"))) {
						GL11.glPushMatrix();
						GL11.glTranslated(posX, posY, posZ);
						GL11.glRotatef(-ent.rotationYaw, 0.0F, ent.height, 0.0F);
						GL11.glTranslated(-posX, -posY, -posZ);
						R3DUtils.RenderLivingEntityBox((Entity) entity, ((EventRender3D) event).getPartialTicks(),
								false);
						GL11.glPopMatrix();
					}
				}
			}
			
			if (skeletal.getValue()) {
				this.startEnd(true);
				entities.keySet().removeIf(player -> !mc.theWorld.playerEntities.contains(player));
				GL11.glEnable(2903);
		        GL11.glDisable(2848);
				for (EntityPlayer e : entities.keySet()) {
					drawSkeleton((EventRender3D) event, e);
				}
				this.startEnd(false);
			}
		}
		if (this.playerESPMode.getStringValue().equalsIgnoreCase("2D")) {
			if (event instanceof EventRender3D) {
				setDisplayName("2D");
				int color = -1;
				Minecraft.getMinecraft();
				for (Object o : mc.theWorld.loadedEntityList) {
					if ((o instanceof EntityLivingBase)) {
						if (o != Minecraft.thePlayer) {
							EntityLivingBase entity = (EntityLivingBase) o;
							if (entity != null) {
								Entity entity2 = entity;
								if ((entity2 != Minecraft.thePlayer) && ((entity instanceof EntityPlayer))
										&& (!((EntityPlayer) entity).isInvisible())) {
									if (FriendManager.isFriend(entity.getName())) {
										color = -11184641;
									} else if (this.rainbow.getValue()) {
										color = rainbow(2000000L, 1.0F).getRGB();
									} else if (entity.hurtTime != 0) {
										color = new Color(255, 0, 0).getRGB();
									} else {
										color = -1316805;
									}
									float posX = (float) ((float) entity.lastTickPosX
											+ (entity.posX - entity.lastTickPosX) * this.mc.timer.renderPartialTicks);
									float posY = (float) ((float) entity.lastTickPosY
											+ (entity.posY - entity.lastTickPosY) * this.mc.timer.renderPartialTicks);
									float posZ = (float) ((float) entity.lastTickPosZ
											+ (entity.posZ - entity.lastTickPosZ) * this.mc.timer.renderPartialTicks);
									draw2DCorner(entity, posX - RenderManager.renderPosX,
											posY - RenderManager.renderPosY, posZ - RenderManager.renderPosZ, color);
								}
							}
						}
					}
				}
			}
		}
		if (this.playerESPMode.getStringValue().equalsIgnoreCase("CSGO")) {
			setDisplayName("CSGO");
			if (event instanceof EventRender3D) {
				for (Object f : mc.theWorld.loadedEntityList) {
					Entity o = (Entity) f;
					if ((o instanceof EntityLivingBase)) {
						if (o != Minecraft.thePlayer) {
							EntityLivingBase entity = (EntityLivingBase) o;
							if (entity != null) {
								Entity entity2 = entity;
								if (entity != Minecraft.thePlayer && (entity instanceof EntityPlayer)) {
									GlStateManager.pushMatrix();
									GlStateManager.translate(
											(float) entity.lastTickPosX
													+ (entity.posX - entity.lastTickPosX)
															* Minecraft.getMinecraft().timer.renderPartialTicks
													- RenderManager.renderPosX,
											entity.lastTickPosY
													+ (entity.posY - entity.lastTickPosY)
															* Minecraft.getMinecraft().timer.renderPartialTicks
													- RenderManager.renderPosY,
											entity.lastTickPosZ
													+ (entity.posZ - entity.lastTickPosZ)
															* Minecraft.getMinecraft().timer.renderPartialTicks
													- RenderManager.renderPosZ);
									GlStateManager.rotate(-Minecraft.getMinecraft().renderManager.playerViewY, 0.0F,
											1.0F, 0.0F);
									GlStateManager.scale(-0.02666667F, -0.02666667F, 0.02666667F);
									GlStateManager.disableDepth();
									GlStateManager.disableLighting();
									if (entity.hurtTime != 0) {
										Gui.drawRect((int) 31.0D, (int) -79.0D, (int) 26.0D, (int) 11.0D, -16777216);
										Gui.drawRect((int) 0.0D, (int) 0.0D, (int) 0.0D, (int) 0.0D, -1);
										Gui.drawRect((int) -31.0D, (int) -79.0D, (int) -26.0D, (int) 11.0D, -16777216);
										Gui.drawRect((int) 0.0D, (int) 0.0D, (int) 0.0D, (int) 0.0D, -1);
										Gui.drawRect((int) -30.0D, (int) 6.0D, (int) 30.0D, (int) 11.0D, -16777216);
										Gui.drawRect((int) 0.0D, (int) 0.0D, (int) 0.0D, (int) 0.0D, -1);
										Gui.drawRect((int) -30.0D, (int) -79.0D, (int) 30.0D, (int) -74.0D, -16777216);
										Gui.drawRect((int) 0.0D, (int) 0.0D, (int) 0.0D, (int) 0.0D, -1);

										Gui.drawRect((int) 30.0D, (int) -75.0D, (int) 27.0D, (int) 10.0D,
												new Color(255, 0, 0).getRGB());
										Gui.drawRect((int) 0.0D, (int) 0.0D, (int) 0.0D, (int) 0.0D, -1);
										Gui.drawRect((int) -30.0D, (int) -75.0D, (int) -27.0D, (int) 10.0D,
												new Color(255, 0, 0).getRGB());
										Gui.drawRect((int) 0.0D, (int) 0.0D, (int) 0.0D, (int) 0.0D, -1);
										Gui.drawRect((int) -30.0D, (int) 7.0D, (int) 30.0D, (int) 10.0D,
												new Color(255, 0, 0).getRGB());
										Gui.drawRect((int) 0.0D, (int) 0.0D, (int) 0.0D, (int) 0.0D, -1);
										Gui.drawRect((int) -30.0D, (int) -78.0D, (int) 30.0D, (int) -75.0D,
												new Color(255, 0, 0).getRGB());
										Gui.drawRect((int) 0.0D, (int) 0.0D, (int) 0.0D, (int) 0.0D, -1);
									} else if (FriendManager.isFriend(entity.getName())) {
										Gui.drawRect((int) 31.0D, (int) -79.0D, (int) 26.0D, (int) 11.0D, -16777216);
										Gui.drawRect((int) 0.0D, (int) 0.0D, (int) 0.0D, (int) 0.0D, -1);
										Gui.drawRect((int) -31.0D, (int) -79.0D, (int) -26.0D, (int) 11.0D, -16777216);
										Gui.drawRect((int) 0.0D, (int) 0.0D, (int) 0.0D, (int) 0.0D, -1);
										Gui.drawRect((int) -30.0D, (int) 6.0D, (int) 30.0D, (int) 11.0D, -16777216);
										Gui.drawRect((int) 0.0D, (int) 0.0D, (int) 0.0D, (int) 0.0D, -1);
										Gui.drawRect((int) -30.0D, (int) -79.0D, (int) 30.0D, (int) -74.0D, -16777216);
										Gui.drawRect((int) 0.0D, (int) 0.0D, (int) 0.0D, (int) 0.0D, -1);

										Gui.drawRect((int) 30.0D, (int) -75.0D, (int) 27.0D, (int) 10.0D,
												getFriendColor());
										Gui.drawRect((int) 0.0D, (int) 0.0D, (int) 0.0D, (int) 0.0D, -1);
										Gui.drawRect((int) -30.0D, (int) -75.0D, (int) -27.0D, (int) 10.0D,
												getFriendColor());
										Gui.drawRect((int) 0.0D, (int) 0.0D, (int) 0.0D, (int) 0.0D, -1);
										Gui.drawRect((int) -30.0D, (int) 7.0D, (int) 30.0D, (int) 10.0D,
												getFriendColor());
										Gui.drawRect((int) 0.0D, (int) 0.0D, (int) 0.0D, (int) 0.0D, -1);
										Gui.drawRect((int) -30.0D, (int) -78.0D, (int) 30.0D, (int) -75.0D,
												getFriendColor());
										Gui.drawRect((int) 0.0D, (int) 0.0D, (int) 0.0D, (int) 0.0D, -1);

									} else if (this.rainbow.getValue()) {
										Gui.drawRect((int) 31.0D, (int) -79.0D, (int) 26.0D, (int) 11.0D, -16777216);
										Gui.drawRect((int) 0.0D, (int) 0.0D, (int) 0.0D, (int) 0.0D, -1);
										Gui.drawRect((int) -31.0D, (int) -79.0D, (int) -26.0D, (int) 11.0D, -16777216);
										Gui.drawRect((int) 0.0D, (int) 0.0D, (int) 0.0D, (int) 0.0D, -1);
										Gui.drawRect((int) -30.0D, (int) 6.0D, (int) 30.0D, (int) 11.0D, -16777216);
										Gui.drawRect((int) 0.0D, (int) 0.0D, (int) 0.0D, (int) 0.0D, -1);
										Gui.drawRect((int) -30.0D, (int) -79.0D, (int) 30.0D, (int) -74.0D, -16777216);
										Gui.drawRect((int) 0.0D, (int) 0.0D, (int) 0.0D, (int) 0.0D, -1);

										Gui.drawRect((int) 30.0D, (int) -75.0D, (int) 27.0D, (int) 10.0D,
												rainbow(2000000L, 1.0F).getRGB());
										Gui.drawRect((int) 0.0D, (int) 0.0D, (int) 0.0D, (int) 0.0D, -1);
										Gui.drawRect((int) -30.0D, (int) -75.0D, (int) -27.0D, (int) 10.0D,
												rainbow(2000000L, 1.0F).getRGB());
										Gui.drawRect((int) 0.0D, (int) 0.0D, (int) 0.0D, (int) 0.0D, -1);
										Gui.drawRect((int) -30.0D, (int) 7.0D, (int) 30.0D, (int) 10.0D,
												rainbow(2000000L, 1.0F).getRGB());
										Gui.drawRect((int) 0.0D, (int) 0.0D, (int) 0.0D, (int) 0.0D, -1);
										Gui.drawRect((int) -30.0D, (int) -78.0D, (int) 30.0D, (int) -75.0D,
												rainbow(2000000L, 1.0F).getRGB());
										Gui.drawRect((int) 0.0D, (int) 0.0D, (int) 0.0D, (int) 0.0D, -1);

									} else {
										Gui.drawRect((int) 31.0D, (int) -79.0D, (int) 26.0D, (int) 11.0D, -16777216);
										Gui.drawRect((int) 0.0D, (int) 0.0D, (int) 0.0D, (int) 0.0D, -1);
										Gui.drawRect((int) -31.0D, (int) -79.0D, (int) -26.0D, (int) 11.0D, -16777216);
										Gui.drawRect((int) 0.0D, (int) 0.0D, (int) 0.0D, (int) 0.0D, -1);
										Gui.drawRect((int) -30.0D, (int) 6.0D, (int) 30.0D, (int) 11.0D, -16777216);
										Gui.drawRect((int) 0.0D, (int) 0.0D, (int) 0.0D, (int) 0.0D, -1);
										Gui.drawRect((int) -30.0D, (int) -79.0D, (int) 30.0D, (int) -74.0D, -16777216);
										Gui.drawRect((int) 0.0D, (int) 0.0D, (int) 0.0D, (int) 0.0D, -1);

										Gui.drawRect((int) 30.0D, (int) -75.0D, (int) 27.0D, (int) 10.0D, -1316805);
										Gui.drawRect((int) 0.0D, (int) 0.0D, (int) 0.0D, (int) 0.0D, -1);
										Gui.drawRect((int) -30.0D, (int) -75.0D, (int) -27.0D, (int) 10.0D, -1316805);
										Gui.drawRect((int) 0.0D, (int) 0.0D, (int) 0.0D, (int) 0.0D, -1);
										Gui.drawRect((int) -30.0D, (int) 7.0D, (int) 30.0D, (int) 10.0D, -1316805);
										Gui.drawRect((int) 0.0D, (int) 0.0D, (int) 0.0D, (int) 0.0D, -1);
										Gui.drawRect((int) -30.0D, (int) -78.0D, (int) 30.0D, (int) -75.0D, -1316805);
										Gui.drawRect((int) 0.0D, (int) 0.0D, (int) 0.0D, (int) 0.0D, -1);
									}
									GlStateManager.enableDepth();
									GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
									GlStateManager.popMatrix();
								}
							}
						}
					}
				}
			}
		}
		if (this.playerESPMode.getStringValue().equalsIgnoreCase("Outline")) {
			setDisplayName("Outline");
		}

//		if (this.playerESPMode.getStringValue().equalsIgnoreCase("Shader")) {
//			setDisplayName("Shader");
//		}
	}

	private void drawSkeleton(final EventRender3D event, final EntityPlayer e) {
		final Color color = Color.WHITE;
		if (!e.isInvisible()) {
			final float[][] entPos = entities.get(e);
			if (entPos != null && e.isEntityAlive() && !e.isDead && e != this.mc.thePlayer && !e.isPlayerSleeping()) {
				GL11.glPushMatrix();
				GL11.glLineWidth(skeletalwidth.getValue().floatValue());
				GlStateManager.color((float) (color.getRed() / 255), (float) (color.getGreen() / 255),
						(float) (color.getBlue() / 255), 1.0f);
				final Vec3 vec = this.getVec3(e);
				final double x = vec.xCoord - RenderManager.renderPosX;
				final double y = vec.yCoord - RenderManager.renderPosY;
				final double z = vec.zCoord - RenderManager.renderPosZ;
				GL11.glTranslated(x, y, z);
				final float xOff = e.prevRenderYawOffset
						+ (e.renderYawOffset - e.prevRenderYawOffset) * event.getPartialTicks();
				GL11.glRotatef(-xOff, 0.0f, 1.0f, 0.0f);
				GL11.glTranslated(0.0, 0.0, e.isSneaking() ? -0.235 : 0.0);
				final float yOff = e.isSneaking() ? 0.6f : 0.75f;
				GL11.glPushMatrix();
				GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
				GL11.glTranslated(-0.125, yOff, 0.0);
				if (entPos[3][0] != 0.0f) {
					GL11.glRotatef(entPos[3][0] * 57.295776f, 1.0f, 0.0f, 0.0f);
				}
				if (entPos[3][1] != 0.0f) {
					GL11.glRotatef(entPos[3][1] * 57.295776f, 0.0f, 1.0f, 0.0f);
				}
				if (entPos[3][2] != 0.0f) {
					GL11.glRotatef(entPos[3][2] * 57.295776f, 0.0f, 0.0f, 1.0f);
				}
				GL11.glBegin(3);
				GL11.glVertex3d(0.0, 0.0, 0.0);
				GL11.glVertex3d(0.0, -yOff, 0.0);
				GL11.glEnd();
				GL11.glPopMatrix();
				GL11.glPushMatrix();
				GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
				GL11.glTranslated(0.125, yOff, 0.0);
				if (entPos[4][0] != 0.0f) {
					GL11.glRotatef(entPos[4][0] * 57.295776f, 1.0f, 0.0f, 0.0f);
				}
				if (entPos[4][1] != 0.0f) {
					GL11.glRotatef(entPos[4][1] * 57.295776f, 0.0f, 1.0f, 0.0f);
				}
				if (entPos[4][2] != 0.0f) {
					GL11.glRotatef(entPos[4][2] * 57.295776f, 0.0f, 0.0f, 1.0f);
				}
				GL11.glBegin(3);
				GL11.glVertex3d(0.0, 0.0, 0.0);
				GL11.glVertex3d(0.0, -yOff, 0.0);
				GL11.glEnd();
				GL11.glPopMatrix();
				GL11.glTranslated(0.0, 0.0, e.isSneaking() ? 0.25 : 0.0);
				GL11.glPushMatrix();
				GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
				GL11.glTranslated(0.0, e.isSneaking() ? -0.05 : 0.0, e.isSneaking() ? -0.01725 : 0.0);
				GL11.glPushMatrix();
				GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
				GL11.glTranslated(-0.375, yOff + 0.55, 0.0);
				if (entPos[1][0] != 0.0f) {
					GL11.glRotatef(entPos[1][0] * 57.295776f, 1.0f, 0.0f, 0.0f);
				}
				if (entPos[1][1] != 0.0f) {
					GL11.glRotatef(entPos[1][1] * 57.295776f, 0.0f, 1.0f, 0.0f);
				}
				if (entPos[1][2] != 0.0f) {
					GL11.glRotatef(-entPos[1][2] * 57.295776f, 0.0f, 0.0f, 1.0f);
				}
				GL11.glBegin(3);
				GL11.glVertex3d(0.0, 0.0, 0.0);
				GL11.glVertex3d(0.0, -0.5, 0.0);
				GL11.glEnd();
				GL11.glPopMatrix();
				GL11.glPushMatrix();
				GL11.glTranslated(0.375, yOff + 0.55, 0.0);
				if (entPos[2][0] != 0.0f) {
					GL11.glRotatef(entPos[2][0] * 57.295776f, 1.0f, 0.0f, 0.0f);
				}
				if (entPos[2][1] != 0.0f) {
					GL11.glRotatef(entPos[2][1] * 57.295776f, 0.0f, 1.0f, 0.0f);
				}
				if (entPos[2][2] != 0.0f) {
					GL11.glRotatef(-entPos[2][2] * 57.295776f, 0.0f, 0.0f, 1.0f);
				}
				GL11.glBegin(3);
				GL11.glVertex3d(0.0, 0.0, 0.0);
				GL11.glVertex3d(0.0, -0.5, 0.0);
				GL11.glEnd();
				GL11.glPopMatrix();
				GL11.glRotatef(xOff - e.rotationYawHead, 0.0f, 1.0f, 0.0f);
				GL11.glPushMatrix();
				GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
				GL11.glTranslated(0.0, yOff + 0.55, 0.0);
				if (entPos[0][0] != 0.0f) {
					GL11.glRotatef(entPos[0][0] * 57.295776f, 1.0f, 0.0f, 0.0f);
				}
				GL11.glBegin(3);
				GL11.glVertex3d(0.0, 0.0, 0.0);
				GL11.glVertex3d(0.0, 0.3, 0.0);
				GL11.glEnd();
				GL11.glPopMatrix();
				GL11.glPopMatrix();
				GL11.glRotatef(e.isSneaking() ? 25.0f : 0.0f, 1.0f, 0.0f, 0.0f);
				GL11.glTranslated(0.0, e.isSneaking() ? -0.16175 : 0.0, e.isSneaking() ? -0.48025 : 0.0);
				GL11.glPushMatrix();
				GL11.glTranslated(0.0, yOff, 0.0);
				GL11.glBegin(3);
				GL11.glVertex3d(-0.125, 0.0, 0.0);
				GL11.glVertex3d(0.125, 0.0, 0.0);
				GL11.glEnd();
				GL11.glPopMatrix();
				GL11.glPushMatrix();
				GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
				GL11.glTranslated(0.0, yOff, 0.0);
				GL11.glBegin(3);
				GL11.glVertex3d(0.0, 0.0, 0.0);
				GL11.glVertex3d(0.0, 0.55, 0.0);
				GL11.glEnd();
				GL11.glPopMatrix();
				GL11.glPushMatrix();
				GL11.glTranslated(0.0, yOff + 0.55, 0.0);
				GL11.glBegin(3);
				GL11.glVertex3d(-0.375, 0.0, 0.0);
				GL11.glVertex3d(0.375, 0.0, 0.0);
				GL11.glEnd();
				GL11.glPopMatrix();
				GL11.glPopMatrix();
				GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
			}
		}
	}

	private Vec3 getVec3(final EntityPlayer var0) {
		final float timer = mc.timer.renderPartialTicks;
		final double x = var0.lastTickPosX + (var0.posX - var0.lastTickPosX) * timer;
		final double y = var0.lastTickPosY + (var0.posY - var0.lastTickPosY) * timer;
		final double z = var0.lastTickPosZ + (var0.posZ - var0.lastTickPosZ) * timer;
		return new Vec3(x, y, z);
	}
	
	private void startEnd(final boolean revert) {
        if (revert) {
            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            GL11.glEnable(2848);
            GlStateManager.disableDepth();
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GlStateManager.blendFunc(770, 771);
            GL11.glHint(3154, 4354);
        }
        else {
            GlStateManager.disableBlend();
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(2848);
            GlStateManager.enableDepth();
            GlStateManager.popMatrix();
        }
        GlStateManager.depthMask(!revert);
    }
}