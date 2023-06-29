package org.moonware.client.cmd.impl.GpsRender;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import org.moonware.client.utils.MWFont;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.Event;
import org.moonware.client.event.events.impl.render.EventRender2D;
import org.moonware.client.feature.impl.combat.particle.zalupa.Utils.RenderUtils;
import org.moonware.client.helpers.misc.ClientHelper;

public class Crosshair {
    private static double x, z;
    private static boolean enabled;
    private static Minecraft mc = Minecraft.getMinecraft();

    @EventTarget
    public void onEvent(Event event) {
        if (event instanceof EventRender2D) {
            ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
            ScaledResolution scaledRes = res;
            int color = ClientHelper.getClientColor().getRGB();
            int alpha = RenderUtils.rgba(0, 0, 0, ClientHelper.getClientColor().getAlpha());
            double gap = 1.5;
            double width = 0.5;
            double size = 3.5;
            rectangleBordered(scaledRes.getScaledWidth() / 2 - width,
                    scaledRes.getScaledHeight() / 2 - gap - size - (isMoving() ? 2 : 0),
                    scaledRes.getScaledWidth() / 2 + 1.0f + width,
                    scaledRes.getScaledHeight() / 2 - gap - (isMoving() ? 2 : 0), 0.5, color, alpha);
            rectangleBordered(scaledRes.getScaledWidth() / 2 - width,
                    scaledRes.getScaledHeight() / 2 + gap + 1.0 + (isMoving() ? 2 : 0) - 0.15,
                    scaledRes.getScaledWidth() / 2 + 1.0f + width,
                    scaledRes.getScaledHeight() / 2 + 1 + gap + size + (isMoving() ? 2 : 0) - 0.15, 0.5, color,
                    alpha);
            rectangleBordered(scaledRes.getScaledWidth() / 2 - gap - size - (isMoving() ? 2 : 0) + 0.15,
                    scaledRes.getScaledHeight() / 2 - width,
                    scaledRes.getScaledWidth() / 2 - gap - (isMoving() ? 2 : 0) + 0.15,
                    scaledRes.getScaledHeight() / 2 + 1.0f + width, 0.5, color, alpha);
            rectangleBordered(scaledRes.getScaledWidth() / 2 + 1 + gap + (isMoving() ? 2 : 0),
                    scaledRes.getScaledHeight() / 2 - width,
                    scaledRes.getScaledWidth() / 2 + size + gap + 1.0 + (isMoving() ? 2 : 0),
                    scaledRes.getScaledHeight() / 2 + 1.0f + width, 0.5, color, alpha);
            if (enabled) {
                double yaw = Math.toDegrees(Math.atan2(z - Minecraft.player.posZ, x - Minecraft.player.posX)) - Minecraft.player.rotationYaw
                        - 90;
                double dst = Math.sqrt(Math.pow(x - Minecraft.player.posX, 2) + Math.pow(z - Minecraft.player.posZ, 2));
                GL11.glPushMatrix();
                GL11.glTranslated(res.getScaledWidth() / 2d + 0.5, res.getScaledHeight() / 2d - 90, 0);
                GL11.glTranslated(
                        (Math.cos(Math.toRadians(yaw - 90)) * 1.3)
                                * (MWFont.MONTSERRAT_BOLD.get(12).getWidth((int) dst + "m") / 2),
                        Math.sin(Math.toRadians(yaw - 90)) * 5, 0);
                GL11.glRotated(yaw, 0, 0, 1);
                drawTriangle();
                GL11.glPopMatrix();
                GL11.glPushMatrix();
                GL11.glTranslated(res.getScaledWidth() / 2d, res.getScaledHeight() / 2d - 90, 0);
                MWFont.MONTSERRAT_BOLD.get(12).drawCenter((int) dst + "m", 0, 0, RenderUtils.rgba(235, 235, 235, 255));
                GL11.glPopMatrix();
            }
        }
    }

    public boolean isMoving() {
        return !Minecraft.player.isCollidedHorizontally
                && (Minecraft.player.movementInput.moveForward != 0 || Minecraft.player.movementInput.moveStrafe != 0)
                && !Minecraft.player.isSneaking();
    }

    public static void setGPS(double x, double z) {
        setEnabled(true);
        Crosshair.x = x;
        Crosshair.z = z;
    }

    public static void setEnabled(boolean enabled) {
        Crosshair.enabled = enabled;
    }

    public void drawTriangle() {
        boolean needBlend = !GL11.glIsEnabled(GL11.GL_BLEND);
        if (needBlend)
            GL11.glEnable(GL11.GL_BLEND);
        int alpha = 255;
        int red_1 = 255;
        int green_1 = 255;
        int blue_1 = 255;
        int red_2 = Math.max(red_1 - 40, 0);
        int green_2 = Math.max(green_1 - 40, 0);
        int blue_2 = Math.max(blue_1 - 40, 0);
        float width = 6, height = 12;
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_POLYGON_SMOOTH);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        GL11.glBegin(GL11.GL_POLYGON);
        GL11.glColor4f(red_1 / 255f, green_1 / 255f, blue_1 / 255f, alpha / 255f);
        GL11.glVertex2d(0, 0 - height);
        GL11.glVertex2d(0 - width, 0);
        GL11.glVertex2d(0, 0 - 3);
        GL11.glEnd();
        GL11.glBegin(GL11.GL_POLYGON);
        GL11.glColor4f(red_2 / 255f, green_2 / 255f, blue_2 / 255f, alpha / 255f);
        GL11.glVertex2d(0, 0 - height);
        GL11.glVertex2d(0, 0 - 3);
        GL11.glVertex2d(0 + width, 0);
        GL11.glEnd();
        GL11.glShadeModel(GL11.GL_FLAT);
        GL11.glDisable(GL11.GL_POLYGON_SMOOTH);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        if (needBlend)
            GL11.glDisable(GL11.GL_BLEND);
    }

    public static void rectangleBordered(double x, double y, double x1, double y1,
                                         double width, int internalColor, int borderColor) {
        RenderUtils.drawRect(x + width, y + width, x1 - width, y1 - width, internalColor);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        RenderUtils.drawRect(x + width, y, x1 - width, y + width, borderColor);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        RenderUtils.drawRect(x, y, x + width, y1, borderColor);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        RenderUtils.drawRect(x1 - width, y, x1, y1, borderColor);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        RenderUtils.drawRect(x + width, y1 - width, x1 - width, y1, borderColor);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
    }
}
