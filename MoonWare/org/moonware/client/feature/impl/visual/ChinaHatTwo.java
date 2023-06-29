package org.moonware.client.feature.impl.visual;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;
import org.moonware.client.MoonWare;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.render.EventRender3D;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.helpers.Utils.ColorUtil;
import org.moonware.client.helpers.Utils.MathUtils;
import org.moonware.client.helpers.Utils.render.RenderUtil;
import org.moonware.client.helpers.misc.ClientHelper;
import org.moonware.client.helpers.render.rect.DrawHelper;
import org.moonware.client.settings.impl.BooleanSetting;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;

public class ChinaHatTwo extends Feature {
    private final BooleanSetting firstPerson = new BooleanSetting("Show in first person", false);
    private BooleanSetting client = new BooleanSetting("Astolfo", true);
    private BooleanSetting astolfo = new BooleanSetting("Astolfo", false, () ->  (client.get()));

    public ChinaHatTwo() {
        super("ChinaHat", "epic hat" ,Type.Visuals);
        addSettings(firstPerson,astolfo,client);
    }
    @EventTarget
    public void onRender3D(EventRender3D e){
        if (Minecraft.player == null || Minecraft.world == null || Minecraft.player.isInvisible() || Minecraft.player.isDead) return;
        if (!firstPerson.isEnabled() && Minecraft.gameSettings.thirdPersonView == 0) return;

        double posX = Minecraft.player.lastTickPosX + (Minecraft.player.posX - Minecraft.player.lastTickPosX) * Minecraft.timer.renderPartialTicks - RenderManager.renderPosX,
                posY = Minecraft.player.lastTickPosY + (Minecraft.player.posY - Minecraft.player.lastTickPosY) * Minecraft.timer.renderPartialTicks - RenderManager.renderPosY,
                posZ = Minecraft.player.lastTickPosZ + (Minecraft.player.posZ - Minecraft.player.lastTickPosZ) * Minecraft.timer.renderPartialTicks - RenderManager.renderPosZ;

        AxisAlignedBB axisalignedbb = Minecraft.player.getEntityBoundingBox();
        double height = axisalignedbb.maxY - axisalignedbb.minY + 0.02,
                radius = axisalignedbb.maxX - axisalignedbb.minX;
        double crabwid  = MoonWare.featureManager.getFeatureByClass(CustomModel.class).getState() && (CustomModel.modelMode.currentMode.equalsIgnoreCase("Crab") || CustomModel.modelMode.currentMode.equalsIgnoreCase("Chinchilla") ) ? 0.91999998688697815D : 0;
        height = (false) ? (Minecraft.player.isSneaking() ? -0.1D : 0.12D) : ((MoonWare.featureManager.getFeatureByClass(CustomModel.class).getState() && CustomModel.modelMode.currentMode.equalsIgnoreCase("Amogus") && Minecraft.player.isSneaking()) ? 0.10000000149011612D : ((MoonWare.featureManager.getFeatureByClass(CustomModel.class).getState() && CustomModel.modelMode.currentMode.equalsIgnoreCase("Demon") && Minecraft.player.isSneaking()) ? 0.10000000149011612D : ((CustomModel.modelMode.currentMode.equalsIgnoreCase("Jeff Killer") && Minecraft.player.isSneaking()) ? 0.09000000357627869D : ((CustomModel.modelMode.currentMode.equalsIgnoreCase("Rabbit") && Minecraft.player.isSneaking()) ? -0.01D : (Minecraft.player.isSneaking() ? -0.22D : ((MoonWare.featureManager.getFeatureByClass(CustomModel.class).getState() && CustomModel.modelMode.currentMode.equalsIgnoreCase("Amogus")) ? 0.20000000149011612D : ((MoonWare.featureManager.getFeatureByClass(CustomModel.class).getState() && CustomModel.modelMode.currentMode.equalsIgnoreCase("Demon")) ? 0.20000000298023224D : (CustomModel.modelMode.currentMode.equalsIgnoreCase("Jeff Killer") ? 0.09000000357627869D : 0.0D)))))))) + Minecraft.player.height - crabwid + (mc.player.isSneaking() ? 0.19f : 0);
        /*  53 */
        glPushMatrix();
        GlStateManager.disableCull();
        glDisable(GL_DEPTH_TEST);
        glDepthMask(false);
        glDisable(GL_TEXTURE_2D);
        glShadeModel(GL_SMOOTH);
        glEnable(GL_BLEND);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.disableLighting();
        GlStateManager.color(1, 1, 1, 1);
        OpenGlHelper.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, GL_ONE, GL_ZERO);

        float yaw = MathUtils.interpolateFloat(Minecraft.player.prevRotationYaw, Minecraft.player.rotationYaw, Minecraft.timer.renderPartialTicks);
        float pitchInterpolate = MathUtils.interpolateFloat(Minecraft.player.prevRenderArmPitch, Minecraft.player.renderArmPitch, Minecraft.timer.renderPartialTicks);

        glTranslated(posX, posY, posZ);
        glEnable(GL_LINE_SMOOTH);
        glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
        glRotated(yaw, 0, -1, 0);
        glRotated(pitchInterpolate / 3.0, 0, 0, 0);
        glTranslatef(0, 0, pitchInterpolate / 270.0F);
        glLineWidth(2);
        glBegin(GL_LINE_LOOP);
        // outline/border or whatever you call it
        for (int i = 0; i <= 180; i++) {
            int color1 =!astolfo.get() ? ColorUtil.rainbow(7, i * 4, 1, 1, .5f).getRGB() : DrawHelper.getColorWithOpacity(ColorUtil.skyRainbow(7,i * 4),150).getRGB();
            color1 = ClientHelper.getColor( i * 4,180,7).getRGB();
            if (client.get())
                color1 = ColorUtil.applyOpacity(new Color(color1),.5f).getRGB();

            GlStateManager.color(1, 1, 1, 1);
            RenderUtil.color(color1);
            glVertex3d(
                    posX - Math.sin(i * MathHelper.PI2 / 90) * radius,
                    posY + height - (Minecraft.player.isSneaking() ? 0.23 : 0) - 0.002,
                    posZ + Math.cos(i * MathHelper.PI2 / 90) * radius
            );
        }
        glEnd();

        glBegin(GL_TRIANGLE_FAN);
        int color12 = !astolfo.get() ? ColorUtil.rainbow(7, 4, 1, 1, .2f).getRGB() : DrawHelper.getColorWithOpacity(ColorUtil.skyRainbow(7,4),80).getRGB();
        if (client.get())
            color12 = ClientHelper.getColor(System.currentTimeMillis(),180,7).getRGB();

        color12 = ColorUtil.applyOpacity(new Color(color12),.2f).getRGB();

        RenderUtil.color(color12);
        glVertex3d(posX, posY + height + 0.3 - (Minecraft.player.isSneaking() ? 0.23 : 0), posZ);

        // draw hat
        for (int i = 0; i <= 180; i++) {
            int color1 =!astolfo.get() ? ColorUtil.rainbow(7, i * 4, 1, 1, .2f).getRGB() : DrawHelper.getColorWithOpacity(ColorUtil.skyRainbow(7,i * 4),80).getRGB();
            if (client.get())
                color1 = ClientHelper.getColor( i * 4,180,7).getRGB();
            color1 = ColorUtil.applyOpacity(new Color(color1),.2f).getRGB();
            GlStateManager.color(1, 1, 1, 1);
            RenderUtil.color(color1);
            glVertex3d(posX - Math.sin(i * MathHelper.PI2 / 90) * radius,
                    posY + height - (Minecraft.player.isSneaking() ? 0.23F : 0),
                    posZ + Math.cos(i * MathHelper.PI2 / 90) * radius
            );

        }
        glVertex3d(posX, posY + height + 0.3 - (Minecraft.player.isSneaking() ? 0.23 : 0), posZ);
        glEnd();


        glPopMatrix();

        glEnable(GL_CULL_FACE);
        glEnable(GL_TEXTURE_2D);
        glShadeModel(GL_FLAT);
        glDepthMask(true);
        glEnable(GL_DEPTH_TEST);
    }

}
