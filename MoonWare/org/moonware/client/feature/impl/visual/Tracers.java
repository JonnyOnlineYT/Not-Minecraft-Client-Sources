package org.moonware.client.feature.impl.visual;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import org.moonware.client.MoonWare;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.render.EventRender3D;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.helpers.misc.ClientHelper;
import org.moonware.client.settings.impl.BooleanSetting;
import org.moonware.client.settings.impl.ColorSetting;
import org.moonware.client.settings.impl.NumberSetting;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class Tracers extends Feature {

    public static BooleanSetting clientColor;
    public static ColorSetting colorGlobal;
    public static BooleanSetting friend;
    public static BooleanSetting onlyPlayer = new BooleanSetting("Only Player", true, () -> true);
    public static NumberSetting width;
    public static BooleanSetting seeOnly = new BooleanSetting("See Only", false, () -> true);
    public static BooleanSetting onlyFriend = new BooleanSetting("Only Friend", false, () -> true);

    public Tracers() {
        super("Tracers", "Рисует линию к игрокам", Type.Visuals);
        clientColor = new BooleanSetting("Client Colored", false, () -> true);
        friend = new BooleanSetting("Friend Highlight", true, () -> true);
        colorGlobal = new ColorSetting("Tracers Color", new Color(0xFFFFFF).getRGB(), () -> !clientColor.getBoolValue());
        width = new NumberSetting("Tracers Width", 1.5F, 0.1F, 5F, 0.1F, () -> true);
        addSettings(colorGlobal, friend, seeOnly, onlyPlayer, width, clientColor);
    }

    public static boolean canSeeEntityAtFov(Entity entityLiving, float scope) {
        double diffX = entityLiving.posX - Minecraft.player.posX;
        double diffZ = entityLiving.posZ - Minecraft.player.posZ;
        float yaw = (float) (Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0);
        double difference = angleDifference(yaw, Minecraft.player.rotationYaw);
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
        Color color = clientColor.getBoolValue() ? ClientHelper.getClientColor() : new Color(colorGlobal.getColorValue());
        for (Entity entity : Minecraft.world.loadedEntityList) {
            if (entity != Minecraft.player) {
                if (onlyPlayer.getBoolValue() && !(entity instanceof EntityPlayer))
                    continue;

                if (seeOnly.getBoolValue() && !canSeeEntityAtFov(entity, 150))
                    return;

                boolean old = Minecraft.gameSettings.viewBobbing;
                Minecraft.gameSettings.viewBobbing = false;
                Minecraft.gameRenderer.setupCameraTransform(event.getPartialTicks(), 0);
                Minecraft.gameSettings.viewBobbing = old;

                double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * event.getPartialTicks() - RenderManager.renderPosX;
                double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * event.getPartialTicks() - RenderManager.renderPosY - 1;
                double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * event.getPartialTicks() - RenderManager.renderPosZ;

                GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                GlStateManager.enable(GL11.GL_BLEND);
                GlStateManager.enable(GL11.GL_LINE_SMOOTH);
                GlStateManager.glLineWidth(width.getNumberValue());
                GlStateManager.disable(GL11.GL_TEXTURE_2D);
                GlStateManager.disable(GL11.GL_DEPTH_TEST);
                GlStateManager.depthMask(false);
                if (MoonWare.friendManager.isFriend(entity.getName()) && friend.getBoolValue()) {
                    GlStateManager.color(0f, 255, 0, 255);
                } else {
                    GlStateManager.color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
                }
                GlStateManager.glBegin(GL11.GL_LINE_STRIP);
                Vec3d vec = new Vec3d(0, 0, 1).rotatePitch((float) -(Math.toRadians(Minecraft.player.rotationPitch))).rotateYaw((float) -Math.toRadians(Minecraft.player.rotationYaw));
                GlStateManager.glVertex3d(vec.xCoord, Minecraft.player.getEyeHeight() + vec.yCoord, vec.zCoord);
                GlStateManager.glVertex3d(x, y + 1.10, z);
                GlStateManager.glEnd();
                GlStateManager.enable(GL11.GL_TEXTURE_2D);
                GlStateManager.disable(GL11.GL_LINE_SMOOTH);
                GlStateManager.enable(GL11.GL_DEPTH_TEST);
                GlStateManager.depthMask(true);
                GlStateManager.disable(GL11.GL_BLEND);
                GlStateManager.resetColor();
            }
        }
    }

}
