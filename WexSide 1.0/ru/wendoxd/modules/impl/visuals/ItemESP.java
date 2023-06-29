package ru.wendoxd.modules.impl.visuals;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.math.AxisAlignedBB;
import org.lwjgl.opengl.GL11;
import ru.wendoxd.events.Event;
import ru.wendoxd.events.impl.render.EventRender2D;
import ru.wendoxd.modules.Module;
import ru.wendoxd.ui.menu.elements.Frame;
import ru.wendoxd.ui.menu.elements.tabelements.CheckBox;
import ru.wendoxd.ui.menu.utils.MenuAPI;
import ru.wendoxd.utils.fonts.Fonts;
import ru.wendoxd.utils.visual.RenderUtils;

import javax.vecmath.Vector3d;
import javax.vecmath.Vector4d;
import java.util.Arrays;
import java.util.List;

public class ItemESP extends Module {

	private Frame itemesp_frame = new Frame("ItemEsp");
	private final CheckBox itemesp = new CheckBox("ItemEsp").markArrayList("ItemESP");
	private final CheckBox box = new CheckBox("Box", () -> itemesp.isEnabled(true)).markColorPicker();

	@Override
	protected void initSettings() {
		itemesp.markSetting("ItemEsp");
		itemesp.markColorPicker().getColor().setRGB(255, 255, 255);
		itemesp_frame.register(itemesp, box);
		MenuAPI.visuals.register(itemesp_frame);
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventRender2D) {
			if (itemesp.isEnabled(false)) {
				for (Entity entity : mc.world.loadedEntityList) {
					if (entity instanceof EntityItem) {
						if (RenderUtils.isInViewFrustum(entity)) {
							double x = entity.lastTickPosX
									+ (entity.posX - entity.lastTickPosX) * mc.getRenderPartialTicks();
							double y = entity.lastTickPosY
									+ (entity.posY - entity.lastTickPosY) * mc.getRenderPartialTicks();
							double z = entity.lastTickPosZ
									+ (entity.posZ - entity.lastTickPosZ) * mc.getRenderPartialTicks();
							double width = entity.width / 1.5;
							double height = entity.height + 0.2F;
							AxisAlignedBB axisAlignedBB = new AxisAlignedBB(x - width, y, z - width, x + width,
									y + height, z + width);
							List<Vector3d> vectors = Arrays.asList(
									new Vector3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ),
									new Vector3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ),
									new Vector3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ),
									new Vector3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ),
									new Vector3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ),
									new Vector3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ),
									new Vector3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ),
									new Vector3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ));

							mc.entityRenderer.setupCameraTransform(mc.getRenderPartialTicks(), 0);

							ScaledResolution sr = ((EventRender2D) event).getScaledResolution();
							Vector4d position = null;
							for (Vector3d vector : vectors) {
								vector = RenderUtils.vectorTo2D(sr.getScaleFactor(),
										vector.x - mc.getRenderManager().renderPosX,
										vector.y - mc.getRenderManager().renderPosY,
										vector.z - mc.getRenderManager().renderPosZ);
								if (vector != null && vector.z > 0 && vector.z < 1) {

									if (position == null) {
										position = new Vector4d(vector.x, vector.y, vector.z, 0);
									}

									position.x = Math.min(vector.x, position.x);
									position.y = Math.min(vector.y, position.y);
									position.z = Math.max(vector.x, position.z);
									position.w = Math.max(vector.y, position.w);
								}
							}

							if (position != null) {
								mc.entityRenderer.setupOverlayRendering(2);
								double posX = position.x;
								double posY = position.y;
								double endPosX = position.z;
								double endPosY = position.w;
								int black = RenderUtils.rgba(25, 25, 25, 255);
								int build = itemesp.getColor().build();
								int boxcolor = box.getColor().build();
								if (box.isEnabled(false)) {
									//
									RenderUtils.drawRect(posX, posY - 1f, endPosX, posY + 0.5f, black);
									RenderUtils.drawRect(posX, posY - 0.5f, endPosX, posY, boxcolor);
									//
									RenderUtils.drawRect(posX - 0.5f, posY, posX + 1, endPosY, black);
									RenderUtils.drawRect(posX, posY - 0.5f, posX + 0.5f, endPosY, boxcolor);
									//
									RenderUtils.drawRect(endPosX - 0.5f, posY, endPosX + 1, endPosY, black);
									RenderUtils.drawRect(endPosX, posY - 0.5f, endPosX + 0.5f, endPosY, boxcolor);
									//
									RenderUtils.drawRect(posX + 0.5f, endPosY - 1f, endPosX, endPosY + 0.5f, black);
									RenderUtils.drawRect(posX + 0.5f, endPosY - 0.5f, endPosX, endPosY, boxcolor);
								}
								String tag = ((EntityItem) entity).getEntityItem().getDisplayName()
										+ (((EntityItem) entity).getEntityItem().stackSize < 1 ? ""
												: " x" + ((EntityItem) entity).getEntityItem().stackSize);
								Fonts.MNTSB_12.drawStringWithShadow(tag,
										(float) ((posX + ((endPosX - posX) / 2)
												- Fonts.MNTSB_12.getStringWidth(tag) / 2)),
										(float) (posY - 5.5), build);
							}

							GL11.glEnable(GL11.GL_DEPTH_TEST);
							GlStateManager.enableBlend();
							mc.entityRenderer.setupOverlayRendering(2);
						}
					}
				}
			}
		}
	}
}
