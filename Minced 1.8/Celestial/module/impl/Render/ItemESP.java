package Celestial.module.impl.Render;

import Celestial.event.EventTarget;
import Celestial.event.events.impl.render.EventRender2D;
import Celestial.module.Module;
import Celestial.module.ModuleCategory;
import Celestial.utils.Helper;
import Celestial.utils.render.RenderUtils;
import Celestial.ui.settings.impl.ColorSetting;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.math.AxisAlignedBB;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import javax.vecmath.Vector3d;
import javax.vecmath.Vector4d;
import java.awt.*;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class ItemESP extends Module {
    public ColorSetting itemESPColor = new ColorSetting("ItemESP Color", new Color(255, 255, 255).getRGB(), () -> true);

    public ItemESP() {
        super("ItemESP", "Показывает название предмета который лежит на земле", ModuleCategory.Render);
        addSettings(itemESPColor);
    }

    @EventTarget
    public void onRender2D(EventRender2D event) {
        float partialTicks = Helper.mc.timer.renderPartialTicks;
        int scaleFactor = event.getResolution().getScaleFactor();
        double scaling = scaleFactor / Math.pow(scaleFactor, 2);
        GlStateManager.scale(scaling, scaling, scaling);
        float scale = 1;
        for (Entity entity : Helper.mc.world.loadedEntityList) {
            if (isValid(entity) && RenderUtils.isInViewFrustum(entity)) {
                EntityItem entityItem = (EntityItem) entity;
                double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * Helper.mc.getRenderPartialTicks();
                double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * Helper.mc.getRenderPartialTicks();
                double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * Helper.mc.getRenderPartialTicks();
                AxisAlignedBB axisAlignedBB2 = entity.getEntityBoundingBox();
                AxisAlignedBB axisAlignedBB = new AxisAlignedBB(axisAlignedBB2.minX - entity.posX + x - 0.05, axisAlignedBB2.minY - entity.posY + y, axisAlignedBB2.minZ - entity.posZ + z - 0.05, axisAlignedBB2.maxX - entity.posX + x + 0.05, axisAlignedBB2.maxY - entity.posY + y + 0.15, axisAlignedBB2.maxZ - entity.posZ + z + 0.05);
                Vector3d[] vectors = new Vector3d[]{new Vector3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ), new Vector3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ), new Vector3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ), new Vector3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ), new Vector3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ), new Vector3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ), new Vector3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ), new Vector3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ)};
                Helper.mc.entityRenderer.setupCameraTransform(partialTicks, 0);

                Vector4d position = null;
                for (Vector3d vector : vectors) {
                    vector = project2D(scaleFactor, vector.x - Helper.mc.getRenderManager().renderPosX, vector.y - Helper.mc.getRenderManager().renderPosY, vector.z - Helper.mc.getRenderManager().renderPosZ);
                    if (vector != null && vector.z > 0 && vector.z < 1) {
                        if (position == null)
                            position = new Vector4d(vector.x, vector.y, vector.z, 0);
                        position.x = Math.min(vector.x, position.x);
                        position.y = Math.min(vector.y, position.y);
                        position.z = Math.max(vector.x, position.z);
                        position.w = Math.max(vector.y, position.w);
                    }
                }

                if (position != null) {
                    Helper.mc.entityRenderer.setupOverlayRendering();
                    double posX = position.x;
                    double posY = position.y;
                    double endPosX = position.z;
                    double endPosY = position.w;

                    float diff = (float) (endPosX - posX) / 2;
                    float textWidth = (Helper.mc.tenacity.getStringWidth(entityItem.getEntityItem().getDisplayName()) * scale);
                    float tagX = (float) ((posX + diff - textWidth / 2) * scale);
                    Helper.mc.tenacity.drawStringWithShadow(entityItem.getEntityItem().getDisplayName(), tagX + 5, (float) posY - 10, itemESPColor.getColorValue());
                }
            }
        }
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        Helper.mc.entityRenderer.setupOverlayRendering();
    }

    private Vector3d project2D(int scaleFactor, double x, double y, double z) {
        float xPos = (float) x;
        float yPos = (float) y;
        float zPos = (float) z;
        IntBuffer viewport = GLAllocation.createDirectIntBuffer(16);
        FloatBuffer modelview = GLAllocation.createDirectFloatBuffer(16);
        FloatBuffer projection = GLAllocation.createDirectFloatBuffer(16);
        FloatBuffer vector = GLAllocation.createDirectFloatBuffer(4);
        GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, modelview);
        GL11.glGetFloat(GL11.GL_PROJECTION_MATRIX, projection);
        GL11.glGetInteger(GL11.GL_VIEWPORT, viewport);
        if (GLU.gluProject(xPos, yPos, zPos, modelview, projection, viewport, vector))
            return new Vector3d((vector.get(0) / scaleFactor), ((Display.getHeight() - vector.get(1)) / scaleFactor), vector.get(2));
        return null;
    }

    private boolean isValid(Entity entity) {
        return entity instanceof EntityItem;
    }

}
