package org.moonware.client.feature.impl.visual;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.player.EventUpdate;
import org.moonware.client.event.events.impl.render.EventRender3D;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.settings.impl.ColorSetting;
import org.moonware.client.settings.impl.ListSetting;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PotJumpCircles
        extends Feature {
    static final int TYPE = 0;
    static final byte MAX_JC_TIME = 20;
    static List<Circle> circles = new ArrayList<>();
    private ListSetting jumpcircleMode = new ListSetting("JumpCircle Mode", "Default", () -> Boolean.valueOf(true), "Default", "Disc");
    public static ColorSetting jumpCircleColor = new ColorSetting("JumpCircle Color", (new Color(16777215)).getRGB(), () -> Boolean.valueOf(true));
    static float pt;

    public PotJumpCircles() {
        super("JumpCircles", "Показывает круги после прыжка", Type.Visuals);
        addSettings(jumpcircleMode, jumpCircleColor);
    }

    @EventTarget
    public void onJump(EventUpdate event) {
        if (Minecraft.player.motionY == 0.33319999363422365D/* && !mc.player.otherCheck() */)
            handleEntityJump(Minecraft.player);
        onLocalPlayerUpdate(Minecraft.player);
    }

    @EventTarget
    public void onRender(EventRender3D event) {
        String mode = jumpcircleMode.getOptions();
        EntityPlayerSP client = Minecraft.player;
        Minecraft mc = Minecraft.getMinecraft();
        double ix = -(client.lastTickPosX + (client.posX - client.lastTickPosX) * mc.getRenderPartialTicks());
        double iy = -(client.lastTickPosY + (client.posY - client.lastTickPosY) * mc.getRenderPartialTicks());
        double iz = -(client.lastTickPosZ + (client.posZ - client.lastTickPosZ) * mc.getRenderPartialTicks());
        if (mode.equalsIgnoreCase("Disc")) {
            GL11.glPushMatrix();
            GL11.glTranslated(ix, iy, iz);
            GL11.glDisable(2884);
            GL11.glEnable(3042);
            GL11.glDisable(3553);
            GL11.glDisable(3008);
            GL11.glDisable(2929);
            GL11.glBlendFunc(770, 771);
            GL11.glShadeModel(7425);
            Collections.reverse(circles);
            try {
                for (Circle c : circles) {
                    float k = c.existed / 20.0F;
                    double x = (c.position()).xCoord;
                    double y = (c.position()).yCoord - k * 0.5D;
                    double z = (c.position()).zCoord;
                    float start = k;
                    float end = start + 1.0F - k;
                    GL11.glBegin(8); int i;
                    for (i = 0; i <= 360; i += 5) {
                        GL11.glColor4f((float)(c.color()).xCoord, (float)(c.color()).yCoord, (float)(c.color()).zCoord, 0.2F * (1.0F - c.existed / 20.0F));

                        GL11.glVertex3d(x + Math.cos(Math.toRadians((i * 4))) * start, y, z + Math.sin(Math.toRadians((i * 4))) * start);
                        GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.01F * (1.0F - c.existed / 20.0F));
                        GL11.glVertex3d(x + Math.cos(Math.toRadians(i)) * end, y + Math.sin((k * 8.0F)) * 0.5D, z +
                                Math.sin(Math.toRadians(i) * end));
                    }
                    GL11.glEnd();
                }
            } catch (Exception exception) {}

            Collections.reverse(circles);
            GL11.glEnable(3553);
            GL11.glDisable(3042);
            GL11.glShadeModel(7424);
            GL11.glEnable(2884);
            GL11.glEnable(2929);
            GL11.glEnable(3008);
            GlStateManager.resetColor();
            GL11.glPopMatrix();
        } else if (mode.equalsIgnoreCase("Default")) {
            GL11.glPushMatrix();
            GL11.glTranslated(ix, iy, iz);
            GL11.glDisable(2884);
            GL11.glEnable(3042);
            GL11.glDisable(3553);
            GL11.glDisable(3008);
            GL11.glBlendFunc(770, 771);
            GL11.glShadeModel(7425);
            Collections.reverse(circles);
            for (Circle c : circles) {
                int red = (int)((jumpCircleColor.getColorValue() >> 16 & 0xFF) / 100.0F);
                int green = (int)((jumpCircleColor.getColorValue() >> 8 & 0xFF) / 100.0F);
                int blue = (int)((jumpCircleColor.getColorValue() & 0xFF) / 100.0F);
                double x = (c.position()).xCoord;
                double y = (c.position()).yCoord;
                double z = (c.position()).zCoord;
                float k = c.existed / 20.0F;
                float start = k * 1.5F;
                float end = start + 0.5F - k;
                GL11.glBegin(8); int i;
                for (i = 0; i <= 360; i += 5) {
                    GL11.glColor4f((float)(c.color()).xCoord, (float)(c.color()).yCoord, (float)(c.color()).zCoord, 0.7F * (1.0F - c.existed / 20.0F));

                    GL11.glVertex3d(x + Math.cos(Math.toRadians(i)) * start, y, z +
                            Math.sin(Math.toRadians(i)) * start);
                    /*
                    switch (false) {
                        case false:
                            GL11.glVertex3d(x + Math.cos(Math.toRadians(i)) * start, y, z +
                                    Math.sin(Math.toRadians(i)) * start);
                            break;
                        case true:
                            GL11.glVertex3d(x + Math.cos(Math.toRadians((i * 2))) * start, y, z +
                                    Math.sin(Math.toRadians((i * 2))) * start);
                            break;
                    }

                     */
                    GL11.glColor4f(red, green, blue, 0.01F * (1.0F - c.existed / 20.0F));
                    GL11.glVertex3d(x + Math.cos(Math.toRadians(i)) * end, y, z + Math.sin(Math.toRadians(i)) * end);
                    /*
                    switch (false) {
                        case false:
                            GL11.glVertex3d(x + Math.cos(Math.toRadians(i)) * end, y, z + Math.sin(Math.toRadians(i)) * end);
                            break;
                        case true:
                            GL11.glVertex3d(x + Math.cos(Math.toRadians(-i)) * end, y, z + Math.sin(Math.toRadians(-i)) * end);
                            break;
                    }

                     */
                }
                GL11.glEnd();
            }
            Collections.reverse(circles);
            GL11.glEnable(3553);
            GL11.glDisable(3042);
            GL11.glShadeModel(7424);
            GL11.glEnable(2884);
            GL11.glEnable(3008);
            GlStateManager.resetColor();
            GL11.glPopMatrix();
        }
    }



    public static void onLocalPlayerUpdate(EntityPlayerSP instance) {
        circles.removeIf(Circle::update);
    }

    public static void handleEntityJump(Entity entity) {
        int red = (int)((jumpCircleColor.getColorValue() >> 16 & 0xFF) / 100.0F);
        int green = (int)((jumpCircleColor.getColorValue() >> 8 & 0xFF) / 100.0F);
        int blue = (int)((jumpCircleColor.getColorValue() & 0xFF) / 100.0F);

        Vec3d color = new Vec3d(red, green, blue);
        circles.add(new Circle(entity.getPositionVector(), color));
    }

    static class Circle {
        private final Vec3d vec;
        private final Vec3d color;
        byte existed;

        Circle(Vec3d vec, Vec3d color) {
            this.vec = vec;
            this.color = color;
        }

        Vec3d position() {
            return vec;
        }

        Vec3d color() {
            return color;
        }

        boolean update() {
            return ((existed = (byte)(existed + 1)) > 20);
        }
    }
}
