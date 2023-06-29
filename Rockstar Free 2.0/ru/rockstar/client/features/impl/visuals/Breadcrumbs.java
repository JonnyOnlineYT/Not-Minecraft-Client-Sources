package ru.rockstar.client.features.impl.visuals;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.text.TextFormatting;
import ru.rockstar.Main;
import ru.rockstar.api.event.EventTarget;
import ru.rockstar.api.event.event.Event3D;
import ru.rockstar.api.event.event.EventUpdate;
import ru.rockstar.api.utils.render.ClientHelper;
import ru.rockstar.api.utils.render.DrawHelper;
import ru.rockstar.client.features.Category;
import ru.rockstar.client.features.Feature;
import ru.rockstar.client.ui.settings.impl.BooleanSetting;
import ru.rockstar.client.ui.settings.impl.ColorSetting;
import ru.rockstar.client.ui.settings.impl.ListSetting;
import ru.rockstar.client.ui.settings.impl.NumberSetting;

import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.*;

import static net.minecraft.client.renderer.entity.RenderManager.*;

public class Breadcrumbs extends Feature {

    ArrayList<Point> points = new ArrayList<Point>();
    ListSetting colorMode = new ListSetting("Trails Color", "Astolfo", () -> true, "Astolfo", "Pulse", "Custom", "Client", "Static");
    ColorSetting onecolor = new ColorSetting("One Color", new Color(255, 255, 255).getRGB(), () -> colorMode.currentMode.equalsIgnoreCase("Static") || colorMode.currentMode.equalsIgnoreCase("Custom"));
    ColorSetting twocolor = new ColorSetting("Two Color", new Color(255, 255, 255).getRGB(), () -> colorMode.currentMode.equalsIgnoreCase("Custom"));
    NumberSetting removeticks = new NumberSetting("Remove Ticks", "Задержка после которой будут пропадать трейлы", 100, 10, 500, 1, () -> true);
    NumberSetting alpha = new NumberSetting("Alpha Trails", "Прозрачность", 255, 1, 255, 1, () -> true);
    BooleanSetting smoothending = new BooleanSetting("Smooth Ending", true, () -> true);

    public Breadcrumbs() {
        super("Trails", "Показывает линию взади вас", 0, Category.VISUALS);
        addSettings(colorMode, onecolor, twocolor, removeticks, alpha, smoothending);
    }

    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {
        this.setModuleName("Trails " + TextFormatting.GRAY + colorMode.currentMode);
    }

    @EventTarget
    public void onRender(Event3D event) {
        points.removeIf(p -> p.age >= removeticks.getNumberValue());

        float x = (float) (mc.player.lastTickPosX + (mc.player.posX - mc.player.lastTickPosX) * event.getPartialTicks());
        float y = (float) (mc.player.lastTickPosY + (mc.player.posY - mc.player.lastTickPosY) * event.getPartialTicks());
        float z = (float) (mc.player.lastTickPosZ + (mc.player.posZ - mc.player.lastTickPosZ) * event.getPartialTicks());

        points.add(new Point((float) (x), y, (float) (z)));


        GL11.glPushMatrix();
        GL11.glDisable(GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_CULL_FACE);
        for (final Point t : points) {
            if (points.indexOf(t) >= points.size() - 1) continue;

            Point temp = points.get(points.indexOf(t) + 1);

            float a = alpha.getNumberValue();
            if (smoothending.getBoolValue()) a = alpha.getNumberValue() * (points.indexOf(t) / (float) points.size());

            Color color = Color.WHITE;
            Color firstcolor = new Color(onecolor.getColorValue());
            switch (colorMode.currentMode) {
                case "Client":
                    color = ClientHelper.getClientColor(t.age / 16, 5, t.age, 5);
                    break;
                case "Astolfo":
                    color = DrawHelper.astolfoColors45(t.age - t.age + 1, t.age, 0.5f, 10);
                    break;
                case "Pulse":
                    color = DrawHelper.TwoColoreffect(new Color(255, 50, 50), new Color(79, 9, 9), Math.abs(System.currentTimeMillis() / 10L) / 100.0 + 6.0F * (t.age / 16) / 60);
                    break;

                case "Custom":
                    color = DrawHelper.TwoColoreffect(new Color(onecolor.getColorValue()), new Color(twocolor.getColorValue()), Math.abs(System.currentTimeMillis() / 10) / 100.0 + 3.0F * (t.age / 16) / 60);
                    break;
                case "Static":
                    color = firstcolor;
                    break;
            }

            Color c = DrawHelper.setAlpha(color, (int) a);

            glBegin(GL_QUAD_STRIP);
            final double x2 = t.x - RenderManager.renderPosX;
            final double y2 = t.y - RenderManager.renderPosY;
            final double z2 = t.z - RenderManager.renderPosZ;

            final double x1 = temp.x - RenderManager.renderPosX;
            final double y1 = temp.y - RenderManager.renderPosY;
            final double z1 = temp.z - RenderManager.renderPosZ;

            DrawHelper.glColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), 0).getRGB());
            glVertex3d(x2, y2 + mc.player.height - 0.1, z2);
            DrawHelper.glColor(c.getRGB());
            glVertex3d(x2, y2 + 0.2, z2);
            glVertex3d(x1, y1 + mc.player.height - 0.1, z1);
            glVertex3d(x1, y1 + 0.2, z1);
            glEnd();
            ++t.age;
        }
        GlStateManager.resetColor();
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }

    @Override
    public void onDisable() {
        points.clear();
        super.onDisable();
    }

    public static Color getGradientOffset(Color color1, Color color2, double offset, int alpha) {
        if (offset > 1) {
            double left = offset % 1;
            int off = (int) offset;
            offset = off % 2 == 0 ? left : 1 - left;

        }
        double inverse_percent = 1 - offset;
        int redPart = (int) (color1.getRed() * inverse_percent + color2.getRed() * offset);
        int greenPart = (int) (color1.getGreen() * inverse_percent + color2.getGreen() * offset);
        int bluePart = (int) (color1.getBlue() * inverse_percent + color2.getBlue() * offset);
        return new Color(redPart, greenPart, bluePart, alpha);
    }

    class Point {
        public final float x, y, z;

        public float age = 0;

        public Point(float x, float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }
}