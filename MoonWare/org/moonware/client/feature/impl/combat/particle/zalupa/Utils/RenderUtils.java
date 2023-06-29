package org.moonware.client.feature.impl.combat.particle.zalupa.Utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.projectile.EntityEgg;
import org.lwjgl.opengl.GL11;
import org.moonware.client.feature.impl.combat.particle.zalupa.Particle;
import org.moonware.client.helpers.misc.ClientHelper;

import java.awt.*;
import java.util.ConcurrentModificationException;
import java.util.List;

import static net.minecraft.client.renderer.entity.RenderEntityItem.mc;
import static org.moonware.client.helpers.render.rect.DrawHelper.isInViewFrustrum;

public class RenderUtils {
    public static int rgba(int r, int g, int b, int a) {
        return a << 24 | r << 16 | g << 8 | b;
    }
    public static void drawRect(double xStart, double yStart, double xEnd, double yEnd, int color) {
        drawRect((float) xStart, (float) yStart, (float) xEnd, (float) yEnd, color);
    }
    public static void drawFilledCircleNoGL(int x, int y, double r, int c, int quality) {
        float f = ((c >> 24) & 0xff) / 255F;
        float f1 = ((c >> 16) & 0xff) / 255F;
        float f2 = ((c >> 8) & 0xff) / 255F;
        float f3 = (c & 0xff) / 255F;

        GL11.glColor4f(f1, f2, f3, f);
        GL11.glBegin(GL11.GL_TRIANGLE_FAN);

        for (int i = 0; i <= 360 / quality; i++) {
            double x2 = Math.sin(((i * quality * Math.PI) / 180)) * r;
            double y2 = Math.cos(((i * quality * Math.PI) / 180)) * r;
            GL11.glVertex2d(x + x2, y + y2);
        }

        GL11.glEnd();
    }
    public static void renderParticles(List<Particle> particles) {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        int i = 0;
        try {
            for (Particle particle : particles) {
                i++;
                Vec3 v = particle.position;
                boolean draw = true;

                double x = v.xCoord - RenderManager.renderPosX;
                double y = v.yCoord - RenderManager.renderPosY;
                double z = v.zCoord - RenderManager.renderPosZ;

                double distanceFromPlayer = Minecraft.player.getDistance(v.xCoord, v.yCoord - 1, v.zCoord);
                int quality = (int) (distanceFromPlayer * 4 + 10);

                if (quality > 350)
                    quality = 350;

                if (!isInViewFrustrum(new EntityEgg(Minecraft.world, v.xCoord, v.yCoord, v.zCoord)))
                    draw = false;

                if (i % 10 != 0 && distanceFromPlayer > 25)
                    draw = false;

                if (i % 3 == 0 && distanceFromPlayer > 15)
                    draw = false;

                if (draw) {
                    GL11.glPushMatrix();
                    GL11.glTranslated(x, y, z);

                    final float scale = 0.04F;
                    GL11.glScalef(-scale, -scale, -scale);

                    GL11.glRotated(-Minecraft.getRenderManager().playerViewY, 0.0D, 1.0D, 0.0D);
                    GL11.glRotated(Minecraft.getRenderManager().playerViewX, Minecraft.gameSettings.thirdPersonView == 2 ? -1.0D : 1.0D, 0.0D, 0.0D);
                    Color c = new Color(ClientHelper.getColor(-(1 + 5 * 1.7f), 0.7f, 1).getRGB());
                    drawFilledCircleNoGL(0, 0, 0.7, c.hashCode(), quality);

                    if (distanceFromPlayer < 4)
                        drawFilledCircleNoGL(0, 0, 1.4, new Color(c.getRed(), c.getGreen(), c.getBlue(), 50).hashCode(), quality);

                    if (distanceFromPlayer < 20)
                        drawFilledCircleNoGL(0, 0, 2.3, new Color(c.getRed(), c.getGreen(), c.getBlue(), 30).hashCode(), quality);

                    GL11.glScalef(0.8F, 0.8F, 0.8F);
                    GL11.glPopMatrix();
                }
            }
        } catch (ConcurrentModificationException ignored) {
        }

        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);

        GL11.glColor3d(255, 255, 255);
    }
}
