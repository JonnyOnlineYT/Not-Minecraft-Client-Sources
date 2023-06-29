package org.spray.heaven.features.module.modules.render;

import java.nio.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.awt.*;
import com.darkmagician6.eventapi.EventTarget;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.gui.*;
import net.minecraft.client.*;
import javax.vecmath.*;
import javax.vecmath.Vector3d;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.*;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.item.*;
import net.minecraft.entity.effect.*;
import net.minecraft.entity.projectile.*;
import net.minecraft.entity.boss.*;
import net.minecraft.entity.monster.*;
import net.minecraft.util.math.AxisAlignedBB;
import org.apache.logging.log4j.core.pattern.AbstractStyleNameConverter;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.glu.*;
import org.lwjgl.opengl.*;
import org.spray.heaven.events.WorldRenderEvent;
import org.spray.heaven.features.misc.FriendManager;
import org.spray.heaven.features.module.Category;
import org.spray.heaven.features.module.Module;
import org.spray.heaven.features.module.ModuleInfo;
import org.spray.heaven.features.module.Setting;
import org.spray.heaven.font.IFont;
import org.spray.heaven.util.render.Drawable;
import org.spray.heaven.util.render.RenderUtil;

import static org.lwjgl.opengl.GL11.glVertex2d;

@ModuleInfo(name = "ESP", category = Category.RENDER, visible = true, key = Keyboard.KEY_NONE)
public class ESP extends Module{

	public final List<Entity> collectedEntities;
	private final IntBuffer viewport;
	private final FloatBuffer modelview;
	private final FloatBuffer projection;
	private final FloatBuffer vector;
	private int color;
	private final int backgroundColor;
	private final int black;

	private final Setting health = register(new Setting("Heath", false));
	private final Setting smooth = register(new Setting("Tags", false));
	public final Setting outline = register(new Setting("Outline", true));

	public ESP() {
		ArrayList<String> options = new ArrayList<>();
		options.add("2D");
		this.collectedEntities = new ArrayList<Entity>();
		this.viewport = GLAllocation.createDirectIntBuffer(16);
		this.modelview = GLAllocation.createDirectFloatBuffer(16);
		this.projection = GLAllocation.createDirectFloatBuffer(16);
		this.vector = GLAllocation.createDirectFloatBuffer(4);
		this.color = Color.WHITE.getRGB();
		this.backgroundColor = new Color(0, 0, 0, 120).getRGB();
		this.black = Color.BLACK.getRGB();
	}

	@Override
	public void onRender(int scaleWidth, int scaleHeight, float tickDelta) {
			GL11.glPushMatrix();
			this.collectEntities();
			final float partialTicks = tickDelta;
			final ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
			final int scaleFactor = ScaledResolution.getScaleFactor();
			final double scaling = scaleFactor / Math.pow(scaleFactor, 2.0);
			GL11.glScaled(scaling, scaling, scaling);
			final int black = this.black;



			final float scale = 1.0f;
			final RenderManager renderMng = mc.getRenderManager();
			final EntityRenderer entityRenderer = mc.entityRenderer;
			final List<Entity> collectedEntities = this.collectedEntities;
			for (int i = 0, collectedEntitiesSize = collectedEntities.size(); i < collectedEntitiesSize; ++i) {
				final Entity entity = collectedEntities.get(i);
				if (entity instanceof EntityPlayer) {
					final int color = -1;
					final double x = RenderUtil.interpolate(entity.posX, entity.lastTickPosX, partialTicks);
					final double y = RenderUtil.interpolate(entity.posY, entity.lastTickPosY, partialTicks);
					final double z = RenderUtil.interpolate(entity.posZ, entity.lastTickPosZ, partialTicks);
					final double width = entity.width / 1.5;
					final double n = entity.height;
					double n2 = 0.0;
					Label_0275: {
						Label_0266: {
							if (!entity.isSneaking()) {
								final Entity entity2 = entity;
								if (entity2 == Minecraft.getMinecraft().player) {
									final Minecraft mc2 = mc;
									if (Minecraft.getMinecraft().player.isSneaking()) {
										break Label_0266;
									}
								}
								n2 = 0.2;
								break Label_0275;
							}
						}
						n2 = -0.3;
					}
					final double height = n + n2;
					final AxisAlignedBB aabb = new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width);
					final Vector3d[] vectors = { new Vector3d(aabb.minX, aabb.minY, aabb.minZ), new Vector3d(aabb.minX, aabb.maxY, aabb.minZ), new Vector3d(aabb.maxX, aabb.minY, aabb.minZ), new Vector3d(aabb.maxX, aabb.maxY, aabb.minZ), new Vector3d(aabb.minX, aabb.minY, aabb.maxZ), new Vector3d(aabb.minX, aabb.maxY, aabb.maxZ), new Vector3d(aabb.maxX, aabb.minY, aabb.maxZ), new Vector3d(aabb.maxX, aabb.maxY, aabb.maxZ) };
					entityRenderer.setupCameraTransform(partialTicks, 0);
					Vector4d position = null;
					for (Vector3d vector : vectors) {
						vector = this.project2D(scaleFactor, vector.x - renderMng.viewerPosX, vector.y - renderMng.viewerPosY, vector.z - renderMng.viewerPosZ);
						if (vector != null && vector.z >= 0.0 && vector.z < 1.0) {
							if (position == null) {
								position = new Vector4d(vector.x, vector.y, vector.z, 0.0);
							}
							position.x = Math.min(vector.x, position.x);
							position.y = Math.min(vector.y, position.y);
							position.z = Math.max(vector.x, position.z);
							position.w = Math.max(vector.y, position.w);
						}
					}
					if (position != null) {
						entityRenderer.setupOverlayRendering();

						final double posX = position.x;
						final double posY = position.y;
						final double endPosX = position.z;
						final double endPosY = position.w;

						if (outline.isToggle()) {
							Drawable.drawRect(posX - 1.0D, posY, posX + 0.5D, endPosY + 0.5D, black);
							Drawable.drawRect(posX - 1.0D, posY - 0.5D, endPosX + 0.5D, posY + 0.5D + 0.5D, black);
							Drawable.drawRect(endPosX - 0.5D - 0.5D, posY, endPosX + 0.5D, endPosY + 0.5D, black);
							Drawable.drawRect(posX - 1.0D, endPosY - 0.5D - 0.5D, endPosX + 0.5D, endPosY + 0.5D, black);
						}
						Drawable.drawRect(posX - 0.5D, posY, posX + 0.5D - 0.5D, endPosY, color);
						Drawable.drawRect(posX, endPosY - 0.5D, endPosX, endPosY, color);
						Drawable.drawRect(posX - 0.5D, posY, endPosX, posY + 0.5D, color);
						Drawable.drawRect(endPosX - 0.5D, posY, endPosX, endPosY, color);

						final boolean living = entity instanceof EntityLivingBase;
						if (living) {
							final EntityLivingBase entityLivingBase = (EntityLivingBase)entity;

							final float hp2 = entityLivingBase.getHealth();
							final float maxHealth = entityLivingBase.getMaxHealth();
							final double hpPercentage = hp2 / maxHealth;
							final double hpHeight2 = (endPosY - posY) * hpPercentage;
							final double hpHeight3 = (endPosY - posY) * 1;
							float scaledHeight = 20.0F;

							double dif = (endPosX - posX) / 2.0D;
							double textWidth = (IFont.WEB_SMALL.getStringWidth(entityLivingBase.getName()));
							float tagX = (float) ((posX + dif - textWidth / 2.0D));
							float tagY = (float) (posY) - scaledHeight;


							if (hp2 > 0.0f) {



								if (smooth.isToggle()) {
									IFont.WEB_SMALL.drawStringWithShadow(ChatFormatting.RESET + entity.getName(), (float) (tagX - 1), (float) (tagY + 10), -1);
								}

								if (health.isToggle()) {
									Drawable.drawRect((float) (posX - 3.5), (float) (endPosY + 0.5), (float) (posX - 1.5), (float) (endPosY - hpHeight3 - 0.5), Color.BLACK.getRGB());
									drawVGradientRect((float) (posX - 3.0), (float) (endPosY), (float) (posX - 2.0), (float) (endPosY - hpHeight3), new Color(255, 73, 73).getRGB(), new Color(42, 231, 102).getRGB());
									Drawable.drawRect(posX - 3.5F, posY, posX - 1.5, (endPosY - hpHeight2), new Color(0, 0, 0, 255).getRGB());



								}
							}
						}





					}
				}
			}

			GL11.glPopMatrix();
			GL11.glEnable(2929);
			GlStateManager.enableBlend();
			entityRenderer.setupOverlayRendering();
	}

	public static void drawVGradientRect(final float left, final float top, final float right, final float bottom, final int startColor, final int endColor) {
		float f = (float) (startColor >> 24 & 0xFF) / 255.0f;
		float f1 = (float) (startColor >> 16 & 0xFF) / 255.0f;
		float f2 = (float) (startColor >> 8 & 0xFF) / 255.0f;
		float f3 = (float) (startColor & 0xFF) / 255.0f;
		float f4 = (float) (endColor >> 24 & 0xFF) / 255.0f;
		float f5 = (float) (endColor >> 16 & 0xFF) / 255.0f;
		float f6 = (float) (endColor >> 8 & 0xFF) / 255.0f;
		float f7 = (float) (endColor & 0xFF) / 255.0f;
		GlStateManager.disableTexture2D();
		GlStateManager.enableBlend();
		GlStateManager.disableAlpha();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		GlStateManager.shadeModel(7425);
		GL11.glPushMatrix();
		GL11.glBegin(7);
		GL11.glColor4f(f1, f2, f3, f);
		glVertex2d(left, top);
		glVertex2d(right, top);
		GL11.glColor4f(f5, f6, f7, f4);
		glVertex2d(right, bottom);
		glVertex2d(left, bottom);


		GL11.glEnd();
		GL11.glPopMatrix();
		GlStateManager.resetColor();
		GlStateManager.shadeModel(7424);
		GlStateManager.disableBlend();
		GlStateManager.enableAlpha();
		GlStateManager.enableTexture2D();


	}
	private boolean isValid(final Entity entity) {
		if (entity == mc.player) {
			return false;
		}

		if (entity.isDead) {
			return false;
		}

		if (entity instanceof EntityItem) {
			return true;
		}

		if (entity instanceof EntityAnimal) {
			return true;
		}
		if (entity instanceof EntityPlayer) {
			return true;
		}
		if (entity instanceof EntityArmorStand) {
			return false;
		}
		if (entity instanceof IAnimals) {
			return false;
		}
		if (entity instanceof EntityItemFrame) {
			return false;
		}
		if (entity instanceof EntityArrow) {
			return false;
		}
		if (entity instanceof EntityMinecart) {
			return false;
		}
		if (entity instanceof EntityBoat) {
			return false;
		}
		if (entity instanceof EntityXPOrb) {
			return false;
		}
		if (entity instanceof EntityMinecartChest) {
			return true;
		}
		if (entity instanceof EntityTNTPrimed) {
			return false;
		}
		if (entity instanceof EntityMinecartTNT) {
			return false;
		}
		if (entity instanceof EntityVillager) {
			return false;
		}
		if (entity instanceof EntityExpBottle) {
			return false;
		}

		if (entity instanceof EntityLightningBolt) {
			return false;
		}
		if (entity instanceof EntityPotion) {
			return false;
		}
		if (entity instanceof Entity) {
			return false;
		}
		if (entity instanceof EntityMob || entity instanceof EntitySlime || entity instanceof EntityDragon || entity instanceof EntityGolem) {
			return false;
		}

		return entity != Minecraft.getMinecraft().player;
	}

	private void collectEntities() {
		this.collectedEntities.clear();
		final List<Entity> playerEntities = mc.world.loadedEntityList;
		for (int i = 0, playerEntitiesSize = playerEntities.size(); i < playerEntitiesSize; ++i) {
			final Entity entity = playerEntities.get(i);
			if (this.isValid(entity)) {
				this.collectedEntities.add(entity);
			}
		}
	}

	private Vector3d project2D(final int scaleFactor, final double x, final double y, final double z) {
		GL11.glGetFloat(2982, this.modelview);
		GL11.glGetFloat(2983, this.projection);
		GL11.glGetInteger(2978, this.viewport);
		if (GLU.gluProject((float)x, (float)y, (float)z, this.modelview, this.projection, this.viewport, this.vector)) {
			return new Vector3d(this.vector.get(0) / scaleFactor, (Display.getHeight() - this.vector.get(1)) / scaleFactor, this.vector.get(2));
		}
		return null;
	}
}