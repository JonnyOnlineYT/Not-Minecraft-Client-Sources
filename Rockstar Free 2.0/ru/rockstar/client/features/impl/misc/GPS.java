package ru.rockstar.client.features.impl.misc;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import ru.rockstar.api.command.impl.GPSCommand;
import ru.rockstar.api.event.EventTarget;
import ru.rockstar.api.event.event.EventRender2D;
import ru.rockstar.api.utils.combat.RotationHelper;
import ru.rockstar.api.utils.notifications.NotificationPublisher;
import ru.rockstar.api.utils.notifications.NotificationType;
import ru.rockstar.api.utils.render.AnimationHelper;
import ru.rockstar.api.utils.render.DrawHelper;
import ru.rockstar.client.features.Category;
import ru.rockstar.client.features.Feature;
import ru.rockstar.client.features.impl.combat.KillAura;

public class GPS extends Feature {
	public GPS() {
        super("GPS", "Проводит путь к определенной точке", 0, Category.MISC);
    }


    @EventTarget
    public void one(EventRender2D event2D) {
    	
        if (GPSCommand.mode.equalsIgnoreCase("on")) {
            GL11.glPushMatrix();
            int x = event2D.getResolution().getScaledWidth() / 2;
            int y = event2D.getResolution().getScaledHeight() / 2;
            
            mc.mntsb.drawCenteredString("GPS: " + (int) mc.player.getDistance(GPSCommand.x, mc.player.posY, GPSCommand.z), x, y + 20, -1);
            
            
            GL11.glTranslatef((float) x, (float) y, 0.0F);
            GL11.glRotatef(getAngle(new BlockPos(Integer.parseInt(String.valueOf(GPSCommand.x)), 0, Integer.parseInt(String.valueOf(GPSCommand.z)))) % 360.0F + 180.0F, 0.0F, 0.0F, 1.0F);
            GL11.glTranslatef((float) (-x), (float) (-y), 0.0F);
            DrawHelper.drawTriangle((float) x - 5, (float) (y + 50), 5.0F, 10.0F, new Color(255, 255, 255).darker().getRGB(), new Color(255, 255, 255).getRGB());
            
            GL11.glTranslatef((float) x, (float) y, 0.0F);
            GL11.glRotatef(-(getAngle(new BlockPos(Integer.parseInt(String.valueOf(GPSCommand.x)), 0, Integer.parseInt(String.valueOf(GPSCommand.z)))) % 360.0F + 180.0F), 0.0F, 0.0F, 1.0F);
            
            
            GL11.glTranslatef((float) (-x), (float) (-y), 0.0F);
            GL11.glPopMatrix();
        }
    }

    public static float getAngle(BlockPos entity) {
        return (float) (RotationHelper.getRotations(entity.getX(), 0, entity.getZ())[0] - AnimationHelper.animation(mc.player.rotationYaw, mc.player.prevRotationYaw, (float) 1.0D));
    }

    @Override
    public void onEnable() {
        NotificationPublisher.queue(TextFormatting.WHITE + "GPS", ChatFormatting.GREEN + ".gps <x> <y> <on/off>", NotificationType.INFO);
        super.onEnable();
    }
}
