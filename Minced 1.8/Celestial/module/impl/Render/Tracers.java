package Celestial.module.impl.Render;

import Celestial.event.EventTarget;
import Celestial.event.events.impl.render.EventRender3D;
import Celestial.module.Module;
import Celestial.module.ModuleCategory;
import Celestial.utils.Helper;
import Celestial.utils.render.RenderUtils;
import Celestial.Smertnix;
import Celestial.ui.settings.impl.BooleanSetting;
import Celestial.ui.settings.impl.ColorSetting;
import Celestial.ui.settings.impl.NumberSetting;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class Tracers extends Module {

    private final ColorSetting colorGlobal;
    private final ColorSetting friendColor;

    private final BooleanSetting friend;
    private final BooleanSetting onlyPlayer = new BooleanSetting("Only Player", true, () -> true);
    private final NumberSetting width;
    private final BooleanSetting seeOnly = new BooleanSetting("See Only", false, () -> true);

    public Tracers() {
        super("Tracers", "Рисует линии к игрокам", ModuleCategory.Render);
        friend = new BooleanSetting("Friend Highlight", true, () -> true);
        friendColor = new ColorSetting("Friend Color", new Color(0, 255, 0).getRGB(), friend::getCurrentValue);

        colorGlobal = new ColorSetting("Tracers Color", new Color(0xFFFFFF).getRGB(), () -> true);
        width = new NumberSetting("Tracers Width", 1.5F, 0.1F, 5F, 0.1F, () -> true);
        addSettings(colorGlobal, friend, friendColor, seeOnly, onlyPlayer, width);
    }

    public static boolean canSeeEntityAtFov(Entity entityLiving, float scope) {
        double diffX = entityLiving.posX - Helper.mc.player.posX;
        double diffZ = entityLiving.posZ - Helper.mc.player.posZ;
        float yaw = (float) (Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0);
        double difference = angleDifference(yaw, Helper.mc.player.rotationYaw);
        return difference <= scope;
    }

    public static double angleDifference(float oldYaw, float newYaw) {
        float yaw = Math.abs(oldYaw - newYaw) % 360;
        if (yaw > 180) {
            yaw = 360 - yaw;
        }
        return yaw;
    }

    @EventTarget
    public void onEvent3D(EventRender3D event) {
        for (Entity entity : Helper.mc.world.loadedEntityList) {
            if (entity != Helper.mc.player) {
                if (onlyPlayer.getCurrentValue() && !(entity instanceof EntityPlayer))
                    continue;

                if (seeOnly.getCurrentValue() && !canSeeEntityAtFov(entity, 150))
                    return;

                boolean old = Helper.mc.gameSettings.viewBobbing;
                Helper.mc.gameSettings.viewBobbing = false;
                Helper.mc.entityRenderer.setupCameraTransform(event.getPartialTicks(), 0);
                Helper.mc.gameSettings.viewBobbing = old;

                double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * event.getPartialTicks() - Helper.mc.getRenderManager().renderPosX;
                double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * event.getPartialTicks() - Helper.mc.getRenderManager().renderPosY - 1;
                double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * event.getPartialTicks() - Helper.mc.getRenderManager().renderPosZ;
                GlStateManager.pushMatrix();
                GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glEnable(GL11.GL_LINE_SMOOTH);
                GlStateManager.glLineWidth(width.getCurrentValue());
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                GL11.glDisable(GL11.GL_DEPTH_TEST);
                GlStateManager.depthMask(false);
                if (Smertnix.instance.friendManager.isFriend(entity.getName()) && friend.getCurrentValue()) {
                    RenderUtils.glColor(new Color(friendColor.getColorValue()));
                } else {
                    RenderUtils.glColor(new Color(colorGlobal.getColorValue()));
                }
                GlStateManager.glBegin(GL11.GL_LINE_STRIP);
                Vec3d vec = new Vec3d(0, 0, 1).rotatePitch((float) -(Math.toRadians(Helper.mc.player.rotationPitch))).rotateYaw((float) -Math.toRadians(Helper.mc.player.rotationYaw));
                GL11.glVertex3d(vec.x, Helper.mc.player.getEyeHeight() + vec.y, vec.z);
                GL11.glVertex3d(x, y + 1.10, z);
                GlStateManager.glEnd();
                GL11.glEnable(GL11.GL_TEXTURE_2D);
                GL11.glDisable(GL11.GL_LINE_SMOOTH);
                GL11.glEnable(GL11.GL_DEPTH_TEST);
                GlStateManager.depthMask(true);
                GL11.glDisable(GL11.GL_BLEND);
                GlStateManager.resetColor();
                GlStateManager.popMatrix();

            }
        }
    }

}
