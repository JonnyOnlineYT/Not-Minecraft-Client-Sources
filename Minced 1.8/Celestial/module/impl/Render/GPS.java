package Celestial.module.impl.Render;

import Celestial.cmd.impl.GPSCommand;
import Celestial.event.EventTarget;
import Celestial.event.events.impl.render.EventRender2D;
import Celestial.module.Module;
import Celestial.module.ModuleCategory;
import Celestial.utils.Helper;
import Celestial.utils.math.AnimationHelper;
import Celestial.utils.math.MathematicHelper;
import Celestial.utils.math.RotationHelper;
import Celestial.utils.other.ChatUtils;
import Celestial.utils.render.RenderUtils;
import com.mojang.realmsclient.gui.ChatFormatting;
import Celestial.ui.notif.NotifModern;
import Celestial.ui.notif.NotifRender;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class GPS extends Module {
    public GPS() {
        super("GPS", "Прокладывает метку до координат", ModuleCategory.Render);
    }

    @EventTarget
    public void one(EventRender2D event2D) {
        if (GPSCommand.x == 0 && GPSCommand.z == 0) {
            return;
        }
        if ((int) Helper.mc.player.getDistance(GPSCommand.x, Helper.mc.player.posY, GPSCommand.z) <= 3) {
            toggle();
        }
        if ((int) Helper.mc.player.getDistance(GPSCommand.x, Helper.mc.player.posY, GPSCommand.z) <= 10) {
            int x = event2D.getResolution().getScaledWidth() / 2;
            int y = event2D.getResolution().getScaledHeight() / 2;
            Helper.mc.mntsb_16.drawString("Пооон!", x + 25, y, Color.GREEN.getRGB());

        }
        if ((int) Helper.mc.player.getDistance(GPSCommand.x, Helper.mc.player.posY, GPSCommand.z) <= 10) {
            return;
        }
        if (GPSCommand.mode.equalsIgnoreCase("on")) {
            GL11.glPushMatrix();
            int x = event2D.getResolution().getScaledWidth() / 2;
            int y = event2D.getResolution().getScaledHeight() / 2;
            Helper.mc.mntsb_18.drawString("Метров до цели ~ " + MathematicHelper.round((int) Helper.mc.player.getDistance(GPSCommand.x, Helper.mc.player.posY, GPSCommand.z), 0) + "", x + 25, y, -1);
            GL11.glTranslatef((float) x, (float) y, 0.0F);
            GL11.glRotatef(getAngle(new BlockPos(Integer.parseInt(String.valueOf(GPSCommand.x)), 0, Integer.parseInt(String.valueOf(GPSCommand.z)))) % 360.0F + 180.0F, 0.0F, 0.0F, 1.0F);
            GL11.glTranslatef((float) (-x), (float) (-y), 0.0F);
            RenderUtils.drawBlurredShadow((float) x - 3, (float) (y + 48), 5.0F, 10.0F, 15, new Color(255, 255, 255));
            RenderUtils.drawTriangle((float) x - 5, (float) (y + 50), 5.0F, 10.0F, new Color(255, 255, 255).darker().getRGB(), new Color(255, 255, 255).getRGB());
            GL11.glTranslatef((float) x, (float) y, 0.0F);
            GL11.glRotatef(-(getAngle(new BlockPos(Integer.parseInt(String.valueOf(GPSCommand.x)), 0, Integer.parseInt(String.valueOf(GPSCommand.z)))) % 360.0F + 180.0F), 0.0F, 0.0F, 1.0F);
            GL11.glTranslatef((float) (-x), (float) (-y), 0.0F);
            GL11.glPopMatrix();
        }
    }

    public static float getAngle(BlockPos entity) {
        return (float) (RotationHelper.getRotations(entity.getX(), 0, entity.getZ())[0] - AnimationHelper.Interpolate(Helper.mc.player.rotationYaw, Helper.mc.player.prevRotationYaw, 1.0D));
    }

    @Override
    public void onEnable() {
        ChatUtils.addChatMessage(ChatFormatting.GREEN + "Как использовать? .gps <x> <y> <on/off>");
        NotifRender.queue(TextFormatting.WHITE + "GPS Information", ChatFormatting.GREEN + "Как использовать? .gps <x> <y> <on/off>", 7, NotifModern.INFO);

        super.onEnable();
    }

    @Override
    public void onDisable() {
        GPSCommand.x = 0;
        GPSCommand.z = 0;
        super.onDisable();
    }
}
