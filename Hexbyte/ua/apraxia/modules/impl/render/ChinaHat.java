package ua.apraxia.modules.impl.render;

import ua.apraxia.eventapi.EventTarget;
import ua.apraxia.eventapi.events.impl.render.EventRender3D;
import ua.apraxia.modules.Categories;
import ua.apraxia.modules.Module;
import ua.apraxia.modules.settings.impl.ColorSetting;
import ua.apraxia.modules.settings.impl.ModeSetting;
import ua.apraxia.modules.settings.impl.SliderSetting;
import ua.apraxia.utility.Utility;
import ua.apraxia.utility.math.MathUtility;
import ua.apraxia.utility.other.ColorUtility;
import ua.apraxia.utility.render.RenderUtility;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

import java.awt.*;


public class ChinaHat extends Module {
    public ModeSetting mode = new ModeSetting("Color", "Fade", "Astolfo",  "Rainbow", "Custom");
    public static SliderSetting saturation = new SliderSetting("Saturation", 0.5F, 0.2F, 1.0F, 0.2F);
    public static SliderSetting index = new SliderSetting("Index", 1, 0.2F, 70, 0.2F);
    public static ColorSetting color = new ColorSetting("Hat Color", new Color(111, 90, 253).getRGB());
    public static ColorSetting coloralt = new ColorSetting("Alt Hat Color", new Color(251, 251, 255).getRGB());
    public ChinaHat() {
        super("ChinaHat", Categories.Render);
        addSetting(mode, color, saturation,coloralt, index);
    }

    @EventTarget
    public void asf(EventRender3D event) {
        if (Utility.mc.gameSettings.thirdPersonView == 0) {
            return;
        }
        if (mc.player == null || mc.world == null || mc.player.isInvisible() || mc.player.isDead) return;

        mc.getRenderManager(); double posX = mc.player.lastTickPosX + (mc.player.posX - mc.player.lastTickPosX) * mc.timer.renderPartialTicks - mc.getRenderManager().renderPosX;
        mc.getRenderManager(); double posY = mc.player.lastTickPosY + (mc.player.posY - mc.player.lastTickPosY) * mc.timer.renderPartialTicks - mc.getRenderManager().renderPosY;
        mc.getRenderManager(); double posZ = mc.player.lastTickPosZ + (mc.player.posZ - mc.player.lastTickPosZ) * mc.timer.renderPartialTicks - mc.getRenderManager().renderPosZ;

        AxisAlignedBB axisalignedbb = mc.player.getEntityBoundingBox();
        double height = axisalignedbb.maxY - axisalignedbb.minY + 0.02D;
        double radius = axisalignedbb.maxX - axisalignedbb.minX;

        GL11.glPushMatrix();
        GlStateManager.disableCull();
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glDisable(3553);
        GL11.glShadeModel(7425);
        GL11.glEnable(3042);
        GlStateManager.disableLighting();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);

        Double yaw =  MathUtility.interpolate1(mc.player.prevRotationYaw, mc.player.rotationYaw, mc.timer.renderPartialTicks);
        Double pitchInterpolate = MathUtility.interpolate1(mc.player.prevRenderArmPitch, mc.player.renderArmPitch, mc.timer.renderPartialTicks);

        GL11.glTranslated(posX, posY, posZ);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glRotated(yaw, 0.0D, -1.0D, 0.0D);
        GL11.glRotated(pitchInterpolate / 3.0D, 0.0D, 0.0D, 0.0D);
        GL11.glTranslatef(0.0F, 0.0F, (float) (pitchInterpolate / 270.0F));
        GL11.glLineWidth(2.0F);
        GL11.glBegin(2);


        for (int i = 0; i <= 180; i++) {
            int color1 = ColorUtility.fade(5, (int) (i * index.value), new Color(color.color), 0.9F).getRGB();
            if (mode.is("Rainbow"))
                color1 = ColorUtility.rainbow(5,  (int) (i * index.value), saturation.value, 1.0F, 2.5F).getRGB();
            if (mode.is("Astolfo"))
                color1 = RenderUtility.injectAlpha(ColorUtility.skyRainbow(5,  (int) (i * index.value), saturation.value), 250).getRGB();
            if (mode.is("Custom"))
                color1 = RenderUtility.injectAlpha(ColorUtility.interpolateColorsBackAndForth(5,  (int) (i * index.value),  new Color(color.color), new Color(coloralt.color), false), 150).getRGB();
            if (mode.is("Fade"))
                color1 = ColorUtility.fade(5,  (int) (i * index.value), new Color(color.color), 0.4F).getRGB();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            RenderUtility.color(color1);
            GL11.glVertex3d(posX -
                    Math.sin((i * 6.2831855F / 90.0F)) * radius, posY + height - (
                    mc.player.isSneaking() ? 0.23D : 0.0D) - 0.002D, posZ +
                    Math.cos((i * 6.2831855F / 90.0F)) * radius);
        }
        GL11.glEnd();
        GL11.glBegin(6);
        int color12 = new Color(color.color).getRGB();
        if (mode.is("Rainbow"))
            color12 = ColorUtility.rainbow(5, 4, saturation.value, 1.0F, 1.5f).getRGB();
        if (mode.is("Astolfo"))
            color12 = RenderUtility.injectAlpha(ColorUtility.skyRainbow(5, 4, saturation.value), 250).getRGB();
        if (mode.is("Custom"))
            color12 = RenderUtility.injectAlpha(ColorUtility.interpolateColorsBackAndForth(5, 4,  new Color(color.color), new Color(coloralt.color), false), 150).getRGB();
        if (mode.is("Fade"))
            color12 = ColorUtility.fade(5, 40, new Color(color.color), 150).getRGB();
        RenderUtility.color(color12);
        GL11.glVertex3d(posX, posY + height + 0.3D - (mc.player.isSneaking() ? 0.23D : 0.0D), posZ);


        for (int j = 0; j <= 180; j++) {
            int color1 = ColorUtility.fade(5,  (int) (j * index.value), new Color(color.color), 0.2f).getRGB();
            if (mode.is("Rainbow"))
                color1 = ColorUtility.rainbow(5,  (int) (j * index.value), saturation.value, 1.0F, 0.3f).getRGB();
            if (mode.is("Astolfo"))
                color1 =  RenderUtility.injectAlpha(ColorUtility.skyRainbow(5,  (int) (j * index.value), saturation.value), 50).getRGB();
            if (mode.is("Custom"))
                color1 = RenderUtility.injectAlpha(ColorUtility.interpolateColorsBackAndForth(5,  (int) (j * index.value), new Color(color.color), new Color(coloralt.color), false), 100).getRGB();
            if (mode.is("Fade"))
                color1 = ColorUtility.fade(5, (int) (j * index.value), new Color(color.color), 0.4F).getRGB();
            // ColorUtil.rainbow(35, j * 4, 1.0F, 1.0F, 0.2F).getRGB();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            RenderUtility.color(color1);
            GL11.glVertex3d(posX - Math.sin((j * 6.2831855F / 90.0F)) * radius, posY + height - (
                    mc.player.isSneaking() ? 0.23F : 0.0F), posZ +
                    Math.cos((j * 6.2831855F / 90.0F)) * radius);
        }

        GL11.glVertex3d(posX, posY + height + 0.3D - (mc.player.isSneaking() ? 0.23D : 0.0D), posZ);
        GL11.glEnd();


        GL11.glPopMatrix();

        GL11.glEnable(2884);
        GL11.glEnable(3553);
        GL11.glShadeModel(7424);
        GL11.glDepthMask(true);
        GL11.glEnable(2929);
    }
}