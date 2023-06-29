package Celestial.module.impl.Render;

import Celestial.event.EventTarget;
import Celestial.event.events.impl.render.EventRender3D;
import Celestial.module.Module;
import Celestial.module.ModuleCategory;
import Celestial.utils.Helper;
import Celestial.utils.otherutils.gayutil.ColorUtil;
import Celestial.utils.otherutils.gayutil.DrawHelper;
import Celestial.utils.otherutils.gayutil.MathUtil;
import Celestial.utils.render.RenderUtils;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.math.AxisAlignedBB;
import Celestial.ui.settings.impl.BooleanSetting;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

public class ChinaHat extends Module {

    final BooleanSetting astolfo = new BooleanSetting("Astolfo", false, () -> true);

    final BooleanSetting hideInFirstPerson = new BooleanSetting("шляпка ахуеена", true, () -> true);

    public ChinaHat() {
        super("ChinaHat", "epic hat", ModuleCategory.Render);
        addSettings(astolfo);
    }

    @EventTarget
    public void asf(EventRender3D event) {
        if (ChinaHat.mc.gameSettings.thirdPersonView == 0 && this.hideInFirstPerson.getCurrentValue()) {
            return;
        }
        if (Helper.mc.player == null || Helper.mc.world == null || Helper.mc.player.isInvisible() || Helper.mc.player.isDead) return;
        if (!hideInFirstPerson.getCurrentValue() && Helper.mc.gameSettings.thirdPersonView == 0) return;

        Helper.mc.getRenderManager(); double posX = Helper.mc.player.lastTickPosX + (Helper.mc.player.posX - Helper.mc.player.lastTickPosX) * Helper.mc.timer.renderPartialTicks - Helper.mc.getRenderManager().renderPosX;
        Helper.mc.getRenderManager(); double posY = Helper.mc.player.lastTickPosY + (Helper.mc.player.posY - Helper.mc.player.lastTickPosY) * Helper.mc.timer.renderPartialTicks - Helper.mc.getRenderManager().renderPosY;
        Helper.mc.getRenderManager(); double posZ = Helper.mc.player.lastTickPosZ + (Helper.mc.player.posZ - Helper.mc.player.lastTickPosZ) * Helper.mc.timer.renderPartialTicks - Helper.mc.getRenderManager().renderPosZ;

        AxisAlignedBB axisalignedbb = Helper.mc.player.getEntityBoundingBox();
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

        float yaw = MathUtil.interpolate(Helper.mc.player.prevRotationYaw, Helper.mc.player.rotationYaw, Helper.mc.timer.renderPartialTicks).floatValue();
        float pitchInterpolate = MathUtil.interpolate(Helper.mc.player.prevRenderArmPitch, Helper.mc.player.renderArmPitch, Helper.mc.timer.renderPartialTicks).floatValue();

        GL11.glTranslated(posX, posY, posZ);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glRotated(yaw, 0.0D, -1.0D, 0.0D);
        GL11.glRotated(pitchInterpolate / 3.0D, 0.0D, 0.0D, 0.0D);
        GL11.glTranslatef(0.0F, 0.0F, pitchInterpolate / 270.0F);
        GL11.glLineWidth(2.0F);
        GL11.glBegin(2);


        for (int i = 0; i <= 180; i++) {
            int color1 = !this.astolfo.getCurrentValue() ? ColorUtil.rainbow(35, i * 4, 1.0F, 1.0F, 0.5F).getRGB() : DrawHelper.getColorWithOpacity(ColorUtil.skyRainbow(7, i * 4), 150).getRGB();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            RenderUtils.color(color1);
            GL11.glVertex3d(posX -
                    Math.sin((i * 6.2831855F / 90.0F)) * radius, posY + height - (
                    Helper.mc.player.isSneaking() ? 0.23D : 0.0D) - 0.002D, posZ +
                    Math.cos((i * 6.2831855F / 90.0F)) * radius);
        }

        GL11.glEnd();

        GL11.glBegin(6);
        int color12 = !this.astolfo.getCurrentValue() ? ColorUtil.rainbow(35, 4, 1.0F, 1.0F, 0.2F).getRGB() : DrawHelper.getColorWithOpacity(ColorUtil.skyRainbow(7, 4), 80).getRGB();
        RenderUtils.color(color12);
        GL11.glVertex3d(posX, posY + height + 0.3D - (Helper.mc.player.isSneaking() ? 0.23D : 0.0D), posZ);


        for (int j = 0; j <= 180; j++) {
            int color1 = !this.astolfo.getCurrentValue() ? ColorUtil.rainbow(35, j * 4, 1.0F, 1.0F, 0.2F).getRGB() : DrawHelper.getColorWithOpacity(ColorUtil.skyRainbow(7, j * 4), 80).getRGB();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            RenderUtils.color(color1);
            GL11.glVertex3d(posX - Math.sin((j * 6.2831855F / 90.0F)) * radius, posY + height - (
                    Helper.mc.player.isSneaking() ? 0.23F : 0.0F), posZ +
                    Math.cos((j * 6.2831855F / 90.0F)) * radius);
        }

        //etc ne gey
        GL11.glVertex3d(posX, posY + height + 0.3D - (Helper.mc.player.isSneaking() ? 0.23D : 0.0D), posZ);
        GL11.glEnd();


        GL11.glPopMatrix();

        GL11.glEnable(2884);
        GL11.glEnable(3553);
        GL11.glShadeModel(7424);
        GL11.glDepthMask(true);
        GL11.glEnable(2929);
    }
}