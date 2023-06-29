package ua.apraxia.modules.impl.render;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;
import ua.apraxia.eventapi.EventTarget;
import ua.apraxia.eventapi.events.impl.render.EventRender3D;
import ua.apraxia.modules.Categories;
import ua.apraxia.modules.Module;
import ua.apraxia.modules.settings.impl.BooleanSetting;
import ua.apraxia.modules.settings.impl.ColorSetting;
import ua.apraxia.modules.settings.impl.SliderSetting;
import ua.apraxia.utility.render.RenderUtility;

import java.awt.*;
public class Tracers extends Module {
    public BooleanSetting onlyplayers = new BooleanSetting("onlyplayers", true);
    public SliderSetting width = new SliderSetting("width", 0.5f, 0.5f, 1, 0.5f);
    public static ColorSetting color = new ColorSetting("Tracers Color", Color.WHITE.getRGB());

    public Tracers() {
        super("Tracers", Categories.Render);
        addSetting(width,onlyplayers,color);
    }

    public static double angleDifference(float oldYaw, float newYaw) {
        float yaw = Math.abs(oldYaw - newYaw) % 360;
        if (yaw > 180) {
            yaw = 360 - yaw;
        }
        return yaw;
    }

    @EventTarget
    public void onEvent3D(EventRender3D event) {
        for (Entity entity : mc.world.loadedEntityList) {
            if (entity != mc.player) {
                if (onlyplayers.value && !(entity instanceof EntityPlayer))
                    continue;
                boolean old = mc.gameSettings.viewBobbing;
                mc.gameSettings.viewBobbing = false;
                //mc.entityRenderer.setupCameraTransform(event.getPartialTicks(), 0);
                // mc.gameSettings.viewBobbing = old;

                double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * event.getPartialTicks() - mc.getRenderManager().renderPosX;
                double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * event.getPartialTicks() - mc.getRenderManager().renderPosY - 1;
                double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * event.getPartialTicks() - mc.getRenderManager().renderPosZ;

                GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glEnable(GL11.GL_LINE_SMOOTH);
                GlStateManager.glLineWidth(width.value);
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                GL11.glDisable(GL11.GL_DEPTH_TEST);
                GlStateManager.depthMask(false);
                RenderUtility.glColor(new Color(color.color));
                GlStateManager.glBegin(GL11.GL_LINE_STRIP);
                Vec3d vec = new Vec3d(0, 0, 1).rotatePitch((float) -(Math.toRadians(mc.player.rotationPitch))).rotateYaw((float) -Math.toRadians(mc.player.rotationYaw));
                GlStateManager.glVertex3f((float) vec.x, (float) (mc.player.getEyeHeight() + vec.y), (float) vec.z);
                GlStateManager.glVertex3f((float) x, (float) (y + 1.10), (float) z);
                GlStateManager.glEnd();
                GL11.glEnable(GL11.GL_TEXTURE_2D);
                GL11.glDisable(GL11.GL_LINE_SMOOTH);
                GL11.glEnable(GL11.GL_DEPTH_TEST);
                GlStateManager.depthMask(true);
                GL11.glEnable(GL11.GL_BLEND);
                GlStateManager.resetColor();
            }
        }
    }
}
