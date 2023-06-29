package Celestial.module.impl.Util;

import Celestial.event.EventTarget;
import Celestial.event.events.impl.player.EventPreMotion;
import Celestial.event.events.impl.render.EventRender2D;
import Celestial.module.Module;
import Celestial.module.ModuleCategory;
import Celestial.utils.Helper;
import Celestial.ui.settings.impl.BooleanSetting;
import Celestial.ui.settings.impl.NumberSetting;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;

public class MotionGraph extends Module {

    public static BooleanSetting smooth;
    public static BooleanSetting background;
    public static NumberSetting limitheight;
    private float width = 140;
    private ArrayList<Double> motionSpeed = new ArrayList<>();
    public double bestSpeed;

    public MotionGraph() {
        super("MotionGraph", "Рисует граф по вашей движимости", ModuleCategory.Util);
        limitheight = new NumberSetting("Limit Height", 20, 0, 100, 5, () -> true);
        smooth = new BooleanSetting("Smooth", true, () -> true);
        background = new BooleanSetting("Background", true, () -> true);
        this.addSettings(smooth, background, limitheight);
    }

    @EventTarget
    public void onPreUpdate(EventPreMotion eventPreMotionUpdate) {
        motionSpeed.add(Math.hypot(Helper.mc.player.motionX, Helper.mc.player.motionZ) * limitheight.getCurrentValue());
        if (motionSpeed.size() > width / 2) {
            motionSpeed.remove(((motionSpeed.size() - ((int) width / 2 + 1))));
        }
        bestSpeed = Collections.max(motionSpeed);
    }
    @EventTarget
    public void onEvent(EventRender2D event) {
        final ScaledResolution resolution = new ScaledResolution(Helper.mc);
        if (background.getCurrentValue()) {
            Gui.drawRect(resolution.getScaledWidth() / 2F - width / 2F, (float) (resolution.getScaledHeight() - 75), resolution.getScaledWidth() / 2F + width - 72, (float) (resolution.getScaledHeight() - 75 - bestSpeed), new Color(0, 43, 77, 128).getRGB());
        }
        glPushMatrix();
        glDisable(GL_TEXTURE_2D);
        if (smooth.getCurrentValue()) {
            glEnable(GL_LINE_SMOOTH);
        }
        glEnable(GL_BLEND);
        GL11.glBlendFunc(770, 771);
        GL11.glColor3f(1, 1, 1);
        glLineWidth(1.2f);
        glBegin(GL_LINE_STRIP);
        double add = 0;
        for (Double aDouble : motionSpeed) {
            glVertex2d(resolution.getScaledWidth() / 2F - width / 2F + add, resolution.getScaledHeight() - 75 - aDouble);
            add += 2;
        }
        glEnd();
        glEnable(GL_TEXTURE_2D);
        if (smooth.getCurrentValue()) {
            glDisable(GL_LINE_SMOOTH);
        }
        glDisable(GL_BLEND);
        glPopMatrix();
    }
    public static void drawRect(final float left, final float top, final float right, final float bottom, final int startColor) {
        {
            float f = (float) (startColor >> 24 & 0xFF) / 255.0f;
            float f1 = (float) (startColor >> 16 & 0xFF) / 255.0f;
            float f2 = (float) (startColor >> 8 & 0xFF) / 255.0f;
            float f3 = (float) (startColor & 0xFF) / 255.0f;
            GL11.glPushMatrix();
            GL11.glDisable(GL_TEXTURE_2D);
            GL11.glBlendFunc(770, 771);
            glBegin(GL_QUAD_STRIP);
            GL11.glColor4f(f1, f2, f3, f);
            glVertex2d(left, top);
            glVertex2d(right, top);
            glVertex2d(right, bottom);
            glVertex2d(left, bottom);
            glEnd();
            GL11.glEnable(GL_TEXTURE_2D);
            GL11.glPopMatrix();
        }
        {
            float f = (float) (startColor >> 24 & 0xFF) / 255.0f;
            float f1 = (float) (startColor >> 16 & 0xFF) / 255.0f;
            float f2 = (float) (startColor >> 8 & 0xFF) / 255.0f;
            float f3 = (float) (startColor & 0xFF) / 255.0f;
            GL11.glPushMatrix();
            GL11.glDisable(GL_TEXTURE_2D);
            GL11.glRotatef(-45, 0, 0, 1);
            GL11.glBlendFunc(770, 771);
            glBegin(GL_QUAD_STRIP);
            GL11.glColor4f(f1, f2, f3, f);
            glVertex2d(left, top);
            glVertex2d(right, top);
            glVertex2d(right, bottom);
            glVertex2d(left, bottom);
            glEnd();
            GL11.glEnable(GL_TEXTURE_2D);
            GL11.glPopMatrix();
        }

    }
}