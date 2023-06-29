package ua.apraxia.modules.impl.render;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;
import ua.apraxia.eventapi.EventTarget;
import ua.apraxia.eventapi.events.impl.render.EventRender2D;
import ua.apraxia.eventapi.events.impl.render.EventRender3D;
import ua.apraxia.modules.Categories;
import ua.apraxia.modules.Module;
import ua.apraxia.modules.settings.impl.BooleanSetting;
import ua.apraxia.modules.settings.impl.ColorSetting;
import ua.apraxia.utility.Utility;
import ua.apraxia.utility.render.RenderUtility;

import java.awt.*;

public class PearlTracers extends Module {
    public ColorSetting globalColor = new ColorSetting("Tracers Color", new Color(255,255,255).getRGB());
  //  public BooleanSetting triangleESP = new BooleanSetting("TriangleESP", true);


    public PearlTracers() {
        super("PearlTracers", Categories.Render);

        addSetting(globalColor);
    }

    @EventTarget
    public void onRender3D(EventRender3D event) {
        GlStateManager.pushMatrix();
        for (Entity entity : PearlTracers.mc.world.loadedEntityList) {
            if (!(entity instanceof EntityEnderPearl)) continue;
            boolean viewBobbing = Utility.mc.gameSettings.viewBobbing;
            Utility.mc.gameSettings.viewBobbing = false;
            Utility.mc.entityRenderer.setupCameraTransform(event.getPartialTicks(), 0);
            Utility.mc.gameSettings.viewBobbing = viewBobbing;

                GL11.glPushMatrix();
                GL11.glEnable(GL11.GL_LINE_SMOOTH);
                GL11.glDisable(GL11.GL_DEPTH_TEST);
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glDepthMask(false);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glLineWidth(1);
                double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * event.getPartialTicks() - Utility.mc.getRenderManager().renderPosX;
                double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * event.getPartialTicks() - Utility.mc.getRenderManager().renderPosY - 1;
                double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * event.getPartialTicks() - Utility.mc.getRenderManager().renderPosZ;
                RenderUtility.setColor(globalColor.getColorValue());
                Vec3d vec = new Vec3d(0, 0, 1).rotatePitch((float) -(Math.toRadians(Utility.mc.player.rotationPitch))).rotateYaw((float) -Math.toRadians(Utility.mc.player.rotationYaw));
                GL11.glBegin(2);
                GL11.glVertex3d(vec.x, Utility.mc.player.getEyeHeight() + vec.y, vec.z);
                GL11.glVertex3d(x, y + 1.10, z);
                GL11.glEnd();
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glDepthMask(true);
                GL11.glEnable(GL11.GL_TEXTURE_2D);
                GL11.glEnable(GL11.GL_DEPTH_TEST);
                GL11.glDisable(GL11.GL_LINE_SMOOTH);
                GL11.glPopMatrix();
        }
        GlStateManager.resetColor();
        GlStateManager.popMatrix();
    }

/*    @EventTarget
    public void onRender2D(EventRender2D event) {
        for (Entity entity : PearlTracers.mc.world.loadedEntityList) {
            if (!(entity instanceof EntityEnderPearl)) continue;
            GL11.glPushMatrix();
            int x = event.getResolution().getScaledWidth() / 2;
            int y = event.getResolution().getScaledHeight() / 2;
            GL11.glTranslatef((float) x, (float) y, 0.0F);
            GL11.glRotatef(RotationHelper.getAngle(entity) % 360.0F + 180.0F, 0.0F, 0.0F, 1.0F);
            GL11.glTranslatef((float) (-x), (float) (-y), 0.0F);
            RenderUtility.drawBlurredShadow((float) x - 3, (float) (y + 48), 5.0F, 10.0F, 15, new Color(color));
            RenderUtilsi.drawTriangle((float) x - 5, (float) (y + 50), 5.0F, 10.0F, new Color(color).darker().getRGB(), color);
            GL11.glTranslatef((float) x, (float) y, 0.0F);
            GL11.glRotatef(-(RotationHelper.getAngle(entity) % 360.0F + 180.0F), 0.0F, 0.0F, 1.0F);
            GL11.glTranslatef((float) (-x), (float) (-y), 0.0F);
            GL11.glPopMatrix();
        }
    } */
}

