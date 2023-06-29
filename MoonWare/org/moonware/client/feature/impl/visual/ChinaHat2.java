package org.moonware.client.feature.impl.visual;

import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.motion.EventJump;
import org.moonware.client.event.events.impl.player.EventUpdate;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.feature.impl.visual.anim.Animation;
import org.moonware.client.feature.impl.visual.anim.DecelerateAnimation;
import org.moonware.client.feature.impl.visual.anim.Direction;
import org.moonware.client.feature.impl.visual.anim.WorldRenderEvent;
import org.moonware.client.helpers.Utils.ColorUtil;
import org.moonware.client.settings.impl.ColorSetting;
import org.moonware.client.settings.impl.ListSetting;
import org.moonware.client.settings.impl.NumberSetting;
import org.moonware.client.ui.components.draggable.impl.Colors;

import java.awt.*;
import java.util.Collections;
import java.util.List;

import static org.lwjgl.opengl.GL11.GL_GREATER;
import static org.lwjgl.opengl.GL11.glAlphaFunc;

public class ChinaHat2
         extends Feature {

    public final ListSetting colorMode = new ListSetting("Color Mode", "Analogous", "Astolfo","Rainbow", "Fade","Analogous");
    public final NumberSetting colorSpeed = new NumberSetting("Color Speed", 12, 2, 30);
    public final ColorSetting color = new ColorSetting("Color", Colors.CLIENT_COLOR.getRGB(),() -> colorMode.getCurrentMode().equalsIgnoreCase("Fade")
            || colorMode.getCurrentMode().equalsIgnoreCase("Double Color")
            || colorMode.getCurrentMode().equalsIgnoreCase("Analogous"));

    public final ColorSetting analogColor = new ColorSetting("Analogous Color", Colors.CLIENT_COLOR.getRGB(),() -> colorMode.getCurrentMode().equalsIgnoreCase("Analogous"));

    private final ListSetting mode = new ListSetting("Mode", "Default", "Medusa", "Default");
    private final ListSetting jumpMode = new ListSetting("JumpMode", "Jump", "Jump");
    private final NumberSetting delay = new NumberSetting("Delay (ms)", 1550, 300, 10000,1);
    private final List<Circle> circles = Lists.newArrayList();
    private final NumberSetting startRange = new NumberSetting("Start multiplier", 1, 1, 3,0.01f,() -> mode.currentMode.equals("Default"));
    private final NumberSetting Range = new NumberSetting("Range", 0.2f, 0.05f, 1,0.001f,() -> mode.currentMode.equals("Default"));

    private boolean jumped;
    public ChinaHat2() {

        super("JumpCircles4362", "Тестовые джамп керклы,могут багаться", Type.Visuals);

        addSettings(colorMode,colorSpeed,color,Range, analogColor,mode,jumpMode,delay,startRange);

    }
    @Override
    public void onDisable() {
        circles.clear();
        jumped = false;
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (jumpMode.getCurrentMode().equalsIgnoreCase("Collide")) {
            if (!Minecraft.player.isCollidedVertically)
                jumped = true;

            if (Minecraft.player.onGround) {
                circles.add(new Circle(Minecraft.player.getPositionVector(), getColor(0),
                        new DecelerateAnimation((int) delay.getValue(), 1), new DecelerateAnimation((int) delay.getValue() + 50000, 1000)));
                jumped = false;
            }
        }

        if (Minecraft.player.jumpTicks >= 10) {
            circles.add(new Circle(Minecraft.player.getPositionVector(), getColor(0),
                    new DecelerateAnimation((int) delay.getValue(), 1), new DecelerateAnimation((int) delay.getValue() + 50000, 1000)));

        }
        System.out.println(circles.size());
    }

    @EventTarget
    public void onJump(EventJump e) {
        if (jumpMode.getCurrentMode().equalsIgnoreCase("Jump")) {

        }
    }
    @EventTarget
    public void onRender(WorldRenderEvent event) {
        circles.removeIf(Circle::update);
        startRange.setMinValue(0.1f);
        if (!this.getState()) {
            return;
        }
        if (circles.isEmpty())
            return;

        EntityPlayerSP player = mc.player;
        int delay = (int) this.delay.getValue();
        Minecraft mc = Minecraft.getMinecraft();
        double ix = -(player.lastTickPosX + (player.posX - player.lastTickPosX) * mc.getRenderPartialTicks());
        double iy = -(player.lastTickPosY + (player.posY - player.lastTickPosY) * mc.getRenderPartialTicks());
        double iz = -(player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * mc.getRenderPartialTicks());
        if (mode.getCurrentMode().equalsIgnoreCase("Medusa")) {
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
                for (Circle c : circles) {
                    float k = (float) c.animation1.getTimePassed() / delay;
                    double x = c.getPosition().xCoord;
                    double y = c.getPosition().yCoord - (k * 0.5) + 0.2;
                    double z = c.getPosition().zCoord;
                    float end = k + 1f - k;
                    GL11.glBegin(GL11.GL_QUAD_STRIP);
                    for (int i = 0; i <= 180; i = i + 10) {
                        Color color = getColor(i);

                        GL11.glColor4f((float) color.getRed() / 255F, (float) color.getGreen() / 255F,
                                (float) color.getBlue() / 255F,
                                0.2f * (1 - ((float) c.animation1.getTimePassed() / delay)));
                        GL11.glVertex3d(x + Math.cos(Math.toRadians(i * 4)) * k, y,
                                z + Math.sin(Math.toRadians(i * 4)) * k);
                        GL11.glColor4f(1, 1, 1, 0.01f * (1 - ((float) c.animation1.getTimePassed() / delay)));
                        GL11.glVertex3d(x + Math.cos(Math.toRadians(i)) * end, y + Math.sin(k * 8) * 0.5,
                                z + Math.sin(Math.toRadians(i) * end));
                    }
                    GL11.glEnd();
                }
            } catch (Exception ignored) {
            }
            Collections.reverse(circles);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glColor4f(1f, 1f, 1f, 1f);
            GL11.glShadeModel(GL11.GL_FLAT);
            GL11.glEnable(GL11.GL_CULL_FACE);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glEnable(GL11.GL_ALPHA_TEST);
            GL11.glPopMatrix();
        } else if (mode.getCurrentMode().equalsIgnoreCase("Default")) {
            GL11.glPushMatrix();
            GL11.glTranslated(ix, iy, iz);
            GL11.glDisable(GL11.GL_CULL_FACE);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glShadeModel(GL11.GL_SMOOTH);
            glAlphaFunc (GL_GREATER, 0.1f);
            Collections.reverse(circles);
            for (Circle c : circles) {
                float range = (float) Range.get();
                double x = c.getPosition().xCoord;
                double y = c.getPosition().yCoord - 0.3f;
                double z = c.getPosition().zCoord;
                float k = (float) c.animation2.getTimePassed() / delay;
                float start = (float) (k * startRange.get()) + 0.2f;
                float start1 = (float) (k * startRange.get())  + (0.1f * k * 2) - k;
                float end = start + range * 1.7f;
                float end1 = start -0.2f  - range * 0.3f;
                GL11.glDisable(GL11.GL_POINT_SMOOTH);
                GL11.glBegin(GL11.GL_QUAD_STRIP);
                for (int i = 0; i <= 360; i = i + 1) {
                    Color color = getColor(i);
                    GL11.glColor4f((float) color.getRed() / 255F, (float) color.getGreen() / 255F,
                            (float) color.getBlue() / 255F,
                            0.7f * (1 - ((float) c.animation2.getTimePassed() / (delay * 0.84f))));
                    GL11.glVertex3d(x + Math.cos(Math.toRadians(i)) * start * 2, y,
                            z + Math.sin(Math.toRadians(i)) * start * 2);
                    GL11.glColor4f((float) c.getColor().getRed() / 255F, (float) c.getColor().getGreen() / 255F,
                            (float) c.getColor().getBlue() / 255F,
                            0.00000000001f * (0.81f - ((float) c.animation2.getTimePassed() /  (delay * 0.14f))));
                    GL11.glVertex3d(x + Math.cos(Math.toRadians(i)) * end * 2, y, z + Math.sin(Math.toRadians(i)) * end * 2);
                }
                for (int i = 0; i <= 360; i = i + 1) {
                    Color color = getColor(i );

                    GL11.glColor4f((float) color.getRed() / 255F, (float) color.getGreen() / 255F,
                            (float) color.getBlue() / 255F,
                            0.7f * (1 - ((float) c.animation2.getTimePassed() / (delay * 0.84f))));
                    GL11.glVertex3d(x + Math.cos(Math.toRadians(i)) * start * 2, y,
                            z + Math.sin(Math.toRadians(i)) * start * 2);
                    GL11.glColor4f((float) c.getColor().getRed() / 255F, (float) c.getColor().getGreen() / 255F,
                            (float) c.getColor().getBlue() / 255F,
                            0.00000000001f * (0.81f - ((float) c.animation2.getTimePassed() /  (delay * 0.14f))));
                    GL11.glVertex3d(x + Math.cos(Math.toRadians(i)) * end1 * 2, y, z + Math.sin(Math.toRadians(i)) * end1 * 2);
                }

                GL11.glEnd();
            }
            Collections.reverse(circles);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glColor4f(1f, 1f, 1f, 1f);
            GL11.glShadeModel(GL11.GL_FLAT);
            GL11.glEnable(GL11.GL_CULL_FACE);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glEnable(GL11.GL_ALPHA_TEST);
            GL11.glPopMatrix();
        }
    }
    private Color getColor(int count) {
        int index = count;
        switch (colorMode.getCurrentMode()) {
            case "Astolfo":
                return ColorUtil.skyRainbow((int) colorSpeed.getValue(), index);

            case "Light Rainbow":
                return ColorUtil.rainbow((int) colorSpeed.getValue(), index, .6f, 1, 1);

            case "Rainbow":
                return ColorUtil.rainbow((int) colorSpeed.getValue(), index, 1f, 1, 1);

            case "Fade":
                return ColorUtil.fade((int) colorSpeed.getValue(), index, color.getColorc(), 1);

            case "Double Color":
                return ColorUtil.interpolateColorsBackAndForth((int) colorSpeed.getValue(), index, color.getColorc(),
                        Colors.ALTERNATE_COLOR, true);

            case "Analogous":
                int val = 1;
                Color analogous = ColorUtil.getAnalogousColor(analogColor.getColorc())[val];
                return ColorUtil.interpolateColorsBackAndForth((int) colorSpeed.getValue(), index, color.getColorc(),
                        analogous, false);
            default:
                return color.getColorc();
        }
    }

    static class Circle {
        private final Vec3d vec;
        private final Color color;
        public final org.moonware.client.feature.impl.visual.anim.Animation animation1;
        public final org.moonware.client.feature.impl.visual.anim.Animation animation2;

        Circle(Vec3d vec, Color color, Animation anim, Animation anim2) {
            this.vec = vec;
            this.color = color;
            animation1 = anim;
            animation2 = anim2;
        }


        public Vec3d getPosition() {
            return vec;
        }

        public Color getColor() {
            return color;
        }

        public boolean update() {
            return animation1.finished(Direction.FORWARDS);
        }
    }
}
