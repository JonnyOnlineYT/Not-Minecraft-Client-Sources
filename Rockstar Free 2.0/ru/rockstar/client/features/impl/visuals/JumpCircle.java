package ru.rockstar.client.features.impl.visuals;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import ru.rockstar.api.event.EventTarget;
import ru.rockstar.api.event.event.Event3D;
import ru.rockstar.api.event.event.EventUpdate;
import ru.rockstar.api.utils.math.MathHelper;
import ru.rockstar.api.utils.render.ClientHelper;
import ru.rockstar.api.utils.render.ColorUtil;
import ru.rockstar.api.utils.render.DrawHelper;
import ru.rockstar.client.features.Category;
import ru.rockstar.client.features.Feature;
import ru.rockstar.client.ui.settings.impl.ColorSetting;
import ru.rockstar.client.ui.settings.impl.ListSetting;
import ru.rockstar.client.ui.settings.impl.NumberSetting;

import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JumpCircle extends Feature {
    static final int TYPE = 0;
    static final byte MAX_JC_TIME = 20;
    static List<Circle> circles = new ArrayList();
    private ListSetting jumpcircleMode = new ListSetting("JumpCircle Mode", "Default", () -> true, "Default", "Rockstar", "Disc");
    public static ColorSetting jumpCircleColor = new ColorSetting("JumpCircle Color",new Color(0xFFFFFF).getRGB(), () ->true);
    static float pt;
    public static NumberSetting size;
    public static NumberSetting scale;
    public static NumberSetting speed;
    public JumpCircle() {
        super("JumpCircles", "Показывает круги после прыжка", 0, Category.VISUALS);
        size = new NumberSetting("Circles Size", 1f, 0.1f, 10f, 0.1f, () -> jumpcircleMode.getCurrentMode().equalsIgnoreCase("Rockstar"));
        scale = new NumberSetting("Circles Scale", 0.3f, 0.01f, 10f, 0.01f, () -> jumpcircleMode.getCurrentMode().equalsIgnoreCase("Rockstar"));
        speed = new NumberSetting("Circles Speed", 1f, 0.1f, 5f, 0.1f, () -> jumpcircleMode.getCurrentMode().equalsIgnoreCase("Rockstar"));
        addSettings(jumpcircleMode,jumpCircleColor, size, scale, speed);
    }
    @EventTarget
    public void onJump(EventUpdate event) {
        if (mc.player.motionY == 0.33319999363422365 && !mc.player.killaurachecks())
            handleEntityJump(mc.player);
        onLocalPlayerUpdate(mc.player);
    }
    @EventTarget
    public void onRender(Event3D event) {
        String mode = jumpcircleMode.getOptions();
        EntityPlayerSP client = Minecraft.getMinecraft().player;
        Minecraft mc = Minecraft.getMinecraft();
        double ix = -(client.lastTickPosX + (client.posX - client.lastTickPosX) * mc.getRenderPartialTicks());
        double iy = -(client.lastTickPosY + (client.posY - client.lastTickPosY) * mc.getRenderPartialTicks());
        double iz = -(client.lastTickPosZ + (client.posZ - client.lastTickPosZ) * mc.getRenderPartialTicks());
        if (mode.equalsIgnoreCase("Rockstar")) {
        	float scale = this.scale.getNumberValue();
        	float size = this.size.getNumberValue();
        	float speed = this.speed.getNumberValue();
        	int n = 0;
            int angle = (int)((System.currentTimeMillis() / (long)2 + (long)4) % 360L);
            float hue = (float)angle / 360.0f;
            angle = (int)((double)angle % 360.0);
        	Color color = Color.getHSBColor((double)((float)((double)n / 360.0)) < 0.5 ? -((float)((double)angle / 360.0)) : (float)((double)angle / 360.0), 0.5f, 1.0f);
        	GL11.glPushMatrix();
            GL11.glTranslated(ix, iy, iz);
            GL11.glDisable(GL11.GL_CULL_FACE);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glShadeModel(GL11.GL_SMOOTH);
            Collections.reverse(circles);
            for (Circle c : JumpCircle.circles) {
                int red = (int) (((JumpCircle.jumpCircleColor.getColorValue() >> 16) & 255) / 100.f);
                int green = (int) (((JumpCircle.jumpCircleColor.getColorValue() >> 8) & 255) / 100.f);
                int blue = (int) ((JumpCircle.jumpCircleColor.getColorValue() & 255) / 100.f);
                double x = c.position().xCoord;
                double y = c.position().yCoord - MathHelper.randomNumber(0.3f, 0.31f);
                double z = c.position().zCoord;
                float k = ((float) c.existed / MAX_JC_TIME) * speed;
                float start = (float) (k * size + scale);
                float end = start + scale;
                float end2 = start - scale;
                GL11.glBegin(GL11.GL_QUAD_STRIP);
                for (int i = 0; i <= 360; i = i + 5) {
                    GL11.glColor4f((float) c.color().xCoord, (float) c.color().yCoord, (float) c.color().zCoord,
                            0.7f * (1 - ((float) c.existed / MAX_JC_TIME)));
                    switch (TYPE) {
                        case 0:
                            GL11.glVertex3d(x + Math.cos(Math.toRadians(i)) * start, y,
                                    z + Math.sin(Math.toRadians(i)) * start);
                            break;
                        case 1:
                            GL11.glVertex3d(x + Math.cos(Math.toRadians(i * 2)) * start, y,
                                    z + Math.sin(Math.toRadians(i * 2)) * start);
                            break;
                    }
                    GL11.glColor4f(red, green, blue, 0.01f * (1 - ((float) c.existed / MAX_JC_TIME)));
                    switch (TYPE) {
                        case 0:
                            GL11.glVertex3d(x + Math.cos(Math.toRadians(i)) * end, y, z + Math.sin(Math.toRadians(i)) * end);
                            break;
                        case 1:
                            GL11.glVertex3d(x + Math.cos(Math.toRadians(-i)) * end, y, z + Math.sin(Math.toRadians(-i)) * end);
                            break;
                    }
                }
                for (int i = 0; i <= 360; i = i + 5) {
                    GL11.glColor4f((float) c.color().xCoord, (float) c.color().yCoord, (float) c.color().zCoord,
                            0.7f * (1 - ((float) c.existed / MAX_JC_TIME)));
                    switch (TYPE) {
                        case 0:
                            GL11.glVertex3d(x + Math.cos(Math.toRadians(i)) * start, y,
                                    z + Math.sin(Math.toRadians(i)) * start);
                            break;
                        case 1:
                            GL11.glVertex3d(x + Math.cos(Math.toRadians(i * 10)) * start, y,
                                    z + Math.sin(Math.toRadians(i * 10)) * start);
                            break;
                    }
                    GL11.glColor4f(red, green, blue, 0.01f * ((1 - ((float) c.existed / MAX_JC_TIME)))  );
                    switch (TYPE) {
                        case 0:
                            GL11.glVertex3d(x + Math.cos(Math.toRadians(i)) * end2, y, z + Math.sin(Math.toRadians(i)) * end2);
                            break;
                        case 1:
                            GL11.glVertex3d(x + Math.cos(Math.toRadians(-i)) * end2, y, z + Math.sin(Math.toRadians(-i)) * end2);
                            break;
                    }
                }
                GL11.glEnd();
            }
            Collections.reverse(circles);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glShadeModel(GL11.GL_FLAT);
            GL11.glEnable(GL11.GL_CULL_FACE);
            GL11.glEnable(GL11.GL_ALPHA_TEST);
            GlStateManager.resetColor();
            GL11.glPopMatrix();
            
        }
        if (mode.equalsIgnoreCase("Disc")) {
            GL11.glPushMatrix();
            GL11.glTranslated(ix, iy, iz);
            GL11.glDisable(GL11.GL_CULL_FACE);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glShadeModel(GL11.GL_SMOOTH);
            Collections.reverse(circles);
            try {
                for (Circle c : JumpCircle.circles) {
                    float k = (float) c.existed / MAX_JC_TIME;
                    double x = c.position().xCoord;
                    double y = c.position().yCoord - k * 0.5;
                    double z = c.position().zCoord;
                    float start = k;
                    float end = start + 1f - k;
                    GL11.glBegin(GL11.GL_QUAD_STRIP);
                    for (int i = 0; i <= 360; i = i + 5) {
                        GL11.glColor4f((float) c.color().xCoord, (float) c.color().yCoord, (float) c.color().zCoord,
                                0.2f * (1 - ((float) c.existed / MAX_JC_TIME)));
                        GL11.glVertex3d(x + Math.cos(Math.toRadians(i * 4)) * start, y, z + Math.sin(Math.toRadians(i * 4)) * start);
                        GL11.glColor4f(1, 1, 1, 0.01f * (1 - ((float) c.existed / MAX_JC_TIME)));
                        GL11.glVertex3d(x + Math.cos(Math.toRadians(i)) * end, y + Math.sin(k * 8) * 0.5,
                                z + Math.sin(Math.toRadians(i) * end));
                    }
                    GL11.glEnd();
                }
            } catch (Exception e) {
            }
            Collections.reverse(circles);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glShadeModel(GL11.GL_FLAT);
            GL11.glEnable(GL11.GL_CULL_FACE);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glEnable(GL11.GL_ALPHA_TEST);
            GL11.glPopMatrix();
        }

        else if (mode.equalsIgnoreCase("Default")) {
            GL11.glPushMatrix();
            GL11.glTranslated(ix, iy, iz);
            GL11.glDisable(GL11.GL_CULL_FACE);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glShadeModel(GL11.GL_SMOOTH);
            Collections.reverse(circles);
            for (Circle c : JumpCircle.circles) {
                double x = c.position().xCoord;
                double y = c.position().yCoord;
                double z = c.position().zCoord;
                float k = (float) c.existed / MAX_JC_TIME;
                float start = k * 2.5f;
                float end = start + 1f - k;
                GL11.glBegin(GL11.GL_QUAD_STRIP);
                for (int i = 0; i <= 360; i = i + 5) {
                    GL11.glColor4f((float) c.color().xCoord, (float) c.color().yCoord, (float) c.color().zCoord,
                            0.7f * (1 - ((float) c.existed / MAX_JC_TIME)));
                    switch (TYPE) {
                        case 0:
                            GL11.glVertex3d(x + Math.cos(Math.toRadians(i)) * start, y,
                                    z + Math.sin(Math.toRadians(i)) * start);
                            break;
                        case 1:
                            GL11.glVertex3d(x + Math.cos(Math.toRadians(i * 2)) * start, y,
                                    z + Math.sin(Math.toRadians(i * 2)) * start);
                            break;
                    }
                    GL11.glColor4f(1, 1, 1, 0.01f * (1 - ((float) c.existed / MAX_JC_TIME)));
                    switch (TYPE) {
                        case 0:
                            GL11.glVertex3d(x + Math.cos(Math.toRadians(i)) * end, y, z + Math.sin(Math.toRadians(i)) * end);
                            break;
                        case 1:
                            GL11.glVertex3d(x + Math.cos(Math.toRadians(-i)) * end, y, z + Math.sin(Math.toRadians(-i)) * end);
                            break;
                    }
                }
                GL11.glEnd();
            }
            Collections.reverse(circles);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glShadeModel(GL11.GL_FLAT);
            GL11.glEnable(GL11.GL_CULL_FACE);
            GL11.glEnable(GL11.GL_ALPHA_TEST);
            GL11.glPopMatrix();
        }
    }
    // EntityPlayerSP.onUpdate
    public static void onLocalPlayerUpdate(EntityPlayerSP instance) {
        circles.removeIf(Circle::update);
    }

    public static void handleEntityJump(Entity entity) {
        int red = (int) (((JumpCircle.jumpCircleColor.getColorValue() >> 16) & 255) / 100.f);
        int green = (int) (((JumpCircle.jumpCircleColor.getColorValue() >> 8) & 255) / 100.f);
        int blue = (int) ((JumpCircle.jumpCircleColor.getColorValue() & 255) / 100.f);

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
            return this.vec;
        }

        Vec3d color() {
            return this.color;
        }

        boolean update() {
            return ++existed > MAX_JC_TIME;
        }
    }
}