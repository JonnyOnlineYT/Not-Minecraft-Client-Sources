package ua.apraxia.modules.impl.render;

import com.google.common.collect.Lists;
import ua.apraxia.eventapi.EventTarget;
import ua.apraxia.eventapi.events.impl.player.EventJump;
import ua.apraxia.eventapi.events.impl.render.EventRender3D;
import ua.apraxia.modules.Categories;
import ua.apraxia.modules.Module;
import ua.apraxia.modules.settings.impl.ColorSetting;
import ua.apraxia.modules.settings.impl.ModeSetting;
import ua.apraxia.modules.settings.impl.SliderSetting;
import ua.apraxia.utility.animation.Animation;
import ua.apraxia.utility.animation.Direction;
import ua.apraxia.utility.animation.impl.DecelerateAnimation;
import ua.apraxia.utility.other.ColorUtility;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.Collections;
import java.util.List;

import static org.lwjgl.opengl.GL11.GL_GREATER;
import static org.lwjgl.opengl.GL11.glAlphaFunc;

public class JumpCircles extends Module {
    private final List<Circle> circles = Lists.newArrayList();
    public ModeSetting mode = new ModeSetting("Color", "Fade", "Astolfo",  "Rainbow", "Custom");
    public static ColorSetting color = new ColorSetting("Circles Color", new Color(111, 90, 253).getRGB());
    public static ColorSetting coloralt = new ColorSetting("Alt Circles Color", new Color(251, 251, 255).getRGB());
    public static SliderSetting saturation = new SliderSetting("Saturation", 0.5F, 0.2F, 1.0F, 0.2F);
    public static SliderSetting delay1 = new SliderSetting("Delay", 2550, 500, 10000, 10);
    private float circleAlpha1;
    private float circleAlpha2;
    public JumpCircles() {
        super("JumpCircles", Categories.Render);
        addSetting(color, mode, saturation, coloralt, delay1);
    }
    @EventTarget
    public void onJump(EventJump event) {
        circles.add(new Circle(mc.player.getPositionVector(), getColor(0),
                new DecelerateAnimation((int) 1550, 1), new DecelerateAnimation((int) 1550 + 50000, 1000)));
    }
    @EventTarget
    public void onRender3d(EventRender3D event) {
        if (!this.isModuleState()) {
            return;
        }
        if (circles.isEmpty())
            return;
        EntityPlayerSP player = mc.player;
        int delay = (int) delay1.value;
        Minecraft mc = Minecraft.getMinecraft();
        double ix = -(player.lastTickPosX + (player.posX - player.lastTickPosX) * mc.getRenderPartialTicks());
        double iy = -(player.lastTickPosY + (player.posY - player.lastTickPosY) * mc.getRenderPartialTicks());
        double iz = -(player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * mc.getRenderPartialTicks());
        GL11.glPushMatrix();
        GL11.glTranslated(ix, iy, iz);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        glAlphaFunc(GL_GREATER, 0.1f);
        Collections.reverse(circles);
        for (Circle c : circles) {
            float range = (float) 0.4f;
            double x = c.getPosition().x;
            double y = c.getPosition().y - 0.3f;
            double z = c.getPosition().z;
            float k = (float) c.animation2.getTimePassed() / delay;
            float start = (float) (k * 1.5f) + 0.12f;
            float end = start + range - 0.09f;
            float end1 = start - 0.1f;
            GL11.glDisable(GL11.GL_POINT_SMOOTH);
            GL11.glBegin(GL11.GL_QUAD_STRIP);
            circleAlpha1 = 2 * (0.7f - ((float) c.animation2.getTimePassed() / (delay * 0.84f)));
            circleAlpha2 = 0.0000001f * (1 - ((float) c.animation2.getTimePassed() / (delay)));
            for (int i = 0; i <= 360; i = i + 1) {
                Color color = getColor(i);
                Color color1 = new Color(255, 255, 255);

                GL11.glColor4f((float) color.getRed() / 255F, (float) color.getGreen() / 255F,
                        (float) color.getBlue() / 255F,
                        circleAlpha1);
                GL11.glVertex3d(x + Math.cos(Math.toRadians(i)) * start * 2, y,
                        z + Math.sin(Math.toRadians(i)) * start * 2);
                GL11.glColor4f((float) color.getRed() / 255F, (float) color.getGreen() / 255F,
                        (float) color.getBlue() / 255F,
                        circleAlpha2);
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
        if (circles.size() == 15) {
              circles.clear();
          }
        //circles.removeIf(circle -> circles.size() >= 2000);
    }
    private Color getColor(int count) {
        int index = count;
        if (mode.is("Rainbow"))
            return ColorUtility.rainbow((int) 5, index * 2,  saturation.value, 1, 1);
        if (mode.is("Astolfo"))
            return ColorUtility.skyRainbow((int) 5, index * 1,  saturation.value);
        if (mode.is("Fade"))
            return ColorUtility.fade((int) 5, index * 5, color.getColorc(), 255);
        if (mode.is("Custom"))
            //return ColorUtilityAlt.TwoColoreffect(new Color(color.color), new Color(coloralt.color), 5);
            return ColorUtility.interpolateColorsBackAndForth((int) 5, index, new Color(color.color), new Color(coloralt.color), false);
        return ColorUtility.fade((int) 5, index, color.getColorc(), 255);
    }
    static class Circle {
        private final Vec3d vec;
        private final Color color;
        public final Animation animation1;
        public final Animation animation2;

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
            return animation2.finished(Direction.FORWARDS);
        }
    }
}