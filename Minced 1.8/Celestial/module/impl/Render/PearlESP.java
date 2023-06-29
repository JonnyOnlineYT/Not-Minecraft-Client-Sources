package Celestial.module.impl.Render;

import Celestial.event.EventTarget;
import Celestial.event.events.impl.render.EventRender2D;
import Celestial.event.events.impl.render.EventRender3D;
import Celestial.module.Module;
import Celestial.module.ModuleCategory;
import Celestial.utils.Helper;
import Celestial.utils.math.RotationHelper;
import Celestial.utils.render.ClientHelper;
import Celestial.utils.render.ColorUtils;
import Celestial.utils.render.RenderUtils;
import Celestial.ui.settings.impl.BooleanSetting;
import Celestial.ui.settings.impl.ColorSetting;
import Celestial.ui.settings.impl.ListSetting;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class PearlESP extends Module {
    public ColorSetting globalColor = new ColorSetting("Global Color", new Color(255,255,255).getRGB(), () -> true);
    public BooleanSetting tracers = new BooleanSetting("Tracers", true, () -> true);
    public BooleanSetting esp = new BooleanSetting("ESP", true, () -> true);
    public BooleanSetting triangleESP = new BooleanSetting("TriangleESP", true, () -> true);
    private final ListSetting triangleMode = new ListSetting("Triangle Mode", "Custom", () -> triangleESP.getCurrentValue(), "Astolfo", "Rainbow", "Client", "Custom");
    private final ColorSetting triangleColor;


    public PearlESP() {
        super("PearlESP", "Показывает куда полетит эндер-жемчуг", ModuleCategory.Render);
        triangleColor = new ColorSetting("Triangle Color", new Color(255,255,255).getRGB(), () -> triangleESP.getCurrentValue() && triangleMode.currentMode.equals("Custom"));

        addSettings(globalColor, triangleESP, triangleMode, triangleColor, esp, tracers);
    }

    @EventTarget
    public void onRender3D(EventRender3D event) {
        GlStateManager.pushMatrix();
        for (Entity entity : PearlESP.mc.world.loadedEntityList) {
            if (!(entity instanceof EntityEnderPearl)) continue;
            boolean viewBobbing = Helper.mc.gameSettings.viewBobbing;
            Helper.mc.gameSettings.viewBobbing = false;
            Helper.mc.entityRenderer.setupCameraTransform(event.getPartialTicks(), 0);
            Helper.mc.gameSettings.viewBobbing = viewBobbing;

            if (tracers.getCurrentValue()) {
                GL11.glPushMatrix();
                GL11.glEnable(GL11.GL_LINE_SMOOTH);
                GL11.glDisable(GL11.GL_DEPTH_TEST);
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glDepthMask(false);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glLineWidth(1);
                double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * event.getPartialTicks() - Helper.mc.getRenderManager().renderPosX;
                double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * event.getPartialTicks() - Helper.mc.getRenderManager().renderPosY - 1;
                double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * event.getPartialTicks() - Helper.mc.getRenderManager().renderPosZ;
                RenderUtils.setColor(globalColor.getColorValue());
                Vec3d vec = new Vec3d(0, 0, 1).rotatePitch((float) -(Math.toRadians(Helper.mc.player.rotationPitch))).rotateYaw((float) -Math.toRadians(Helper.mc.player.rotationYaw));
                GL11.glBegin(2);
                GL11.glVertex3d(vec.x, Helper.mc.player.getEyeHeight() + vec.y, vec.z);
                GL11.glVertex3d(x, y + 1.10, z);
                GL11.glEnd();
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glDepthMask(true);
                GL11.glEnable(GL11.GL_TEXTURE_2D);
                GL11.glEnable(GL11.GL_DEPTH_TEST);
                GL11.glDisable(GL11.GL_LINE_SMOOTH);
                GL11.glPopMatrix();
            }

            if (esp.getCurrentValue()) {
                RenderUtils.drawEntityBox(entity, new Color(globalColor.getColorValue()), true, 0.20F);
            }
        }
        GlStateManager.resetColor();
        GlStateManager.popMatrix();
    }

    @EventTarget
    public void onRender2D(EventRender2D event) {
        if (!this.triangleESP.getCurrentValue()) {
            return;
        }
        for (Entity entity : PearlESP.mc.world.loadedEntityList) {
            if (!(entity instanceof EntityEnderPearl)) continue;
            int color = 0;
            switch (triangleMode.currentMode) {
                case "Client":
                    color = ClientHelper.getClientColor().getRGB();
                    break;
                case "Custom":
                    color = triangleColor.getColorValue();
                    break;
                case "Astolfo":
                    color = ColorUtils.astolfo(false, 1).getRGB();
                    break;
                case "Rainbow":
                    color = ColorUtils.rainbow(300, 1, 1).getRGB();
                    break;
            }
            GL11.glPushMatrix();
            int x = event.getResolution().getScaledWidth() / 2;
            int y = event.getResolution().getScaledHeight() / 2;
            GL11.glTranslatef((float) x, (float) y, 0.0F);
            GL11.glRotatef(RotationHelper.getAngle(entity) % 360.0F + 180.0F, 0.0F, 0.0F, 1.0F);
            GL11.glTranslatef((float) (-x), (float) (-y), 0.0F);
            RenderUtils.drawBlurredShadow((float) x - 3, (float) (y + 48), 5.0F, 10.0F, 15, new Color(color));
            RenderUtils.drawTriangle((float) x - 5, (float) (y + 50), 5.0F, 10.0F, new Color(color).darker().getRGB(), color);
            GL11.glTranslatef((float) x, (float) y, 0.0F);
            GL11.glRotatef(-(RotationHelper.getAngle(entity) % 360.0F + 180.0F), 0.0F, 0.0F, 1.0F);
            GL11.glTranslatef((float) (-x), (float) (-y), 0.0F);
            GL11.glPopMatrix();
        }
    }
}

