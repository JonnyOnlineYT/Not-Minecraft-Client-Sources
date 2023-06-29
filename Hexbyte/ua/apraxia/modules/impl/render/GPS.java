//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package ua.apraxia.modules.impl.render;

import java.awt.Color;

import net.minecraft.util.math.BlockPos;
import net.optifine.util.MathUtils;
import org.lwjgl.opengl.GL11;
import ua.apraxia.cmd.impl.GPSCommand;
import ua.apraxia.eventapi.EventTarget;
import ua.apraxia.eventapi.events.impl.player.EventUpdate;
import ua.apraxia.eventapi.events.impl.render.EventRender2D;
import ua.apraxia.modules.Categories;
import ua.apraxia.modules.Module;
import ua.apraxia.modules.settings.impl.BooleanSetting;
import ua.apraxia.modules.settings.impl.ColorSetting;
import ua.apraxia.utility.animation.AnimationUtil;
import ua.apraxia.utility.font.Fonts;
import ua.apraxia.utility.math.MathUtility;
import ua.apraxia.utility.math.RotationsUtility;
import ua.apraxia.utility.render.RenderUtility;

public class GPS extends Module {
    public static ColorSetting color = new ColorSetting("GPS Color", Color.WHITE.getRGB());


    public GPS() {
        super("GPS", Categories.Render);
        addSetting(color);
    }

    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {
        if (GPSCommand.mode.equalsIgnoreCase("on")) {
        }

    }

    @EventTarget
    public void one(EventRender2D event2D) {
        if (GPSCommand.mode.equalsIgnoreCase("on")) {
            int x = event2D.getResolution().getScaledWidth() / 2;
            int y = event2D.getResolution().getScaledHeight() / 2;
            int colorgps = new Color(color.color).getRGB();
            GL11.glPushMatrix();
            Fonts.sfbolt16.drawStringWithShadow("" + MathUtility.round((double)((int)mc.player.getDistance((double)GPSCommand.x, mc.player.posY, (double)GPSCommand.z)), 0) + "m", (float)(x - 10), (float)(y + 23), -1);
            GL11.glTranslatef((float)x, (float)y, 0.0F);
            GL11.glRotatef(getAngle(new BlockPos(Integer.parseInt(String.valueOf(GPSCommand.x)), 0, Integer.parseInt(String.valueOf(GPSCommand.z)))) % 360.0F + 180.0F, 0.0F, 0.0F, 1.0F);
            GL11.glTranslatef((float)(-x), (float)(-y), 0.0F);
            //RenderUtil.drawBlurredShadow((float)(x - 2), (float)(y + 9), 5.0F, 10.0F, 15, new Color(globalColor.color));
            RenderUtility.drawTriangle2((float)(x - 5), (float)(y + 10), 5.0F, 10.0F, new Color(colorgps).darker().darker().getRGB(),  new Color(colorgps).getRGB());
            GL11.glTranslatef((float)x, (float)y, 0.0F);
            GL11.glRotatef(-(getAngle(new BlockPos(Integer.parseInt(String.valueOf(GPSCommand.x)), 0, Integer.parseInt(String.valueOf(GPSCommand.z)))) % 360.0F + 180.0F), 0.0F, 0.0F, 1.0F);
            GL11.glTranslatef((float)(-x), (float)(-y), 0.0F);
            GL11.glPopMatrix();
        }

    }

    public static float getAngle(BlockPos entity) {
        return (float)((double) RotationsUtility.getRotations((double)entity.getX(), 0.0, (double)entity.getZ())[0] - AnimationUtil.Interpolate((double)mc.player.rotationYaw, (double)mc.player.prevRotationYaw, 1.0));
    }

    public void onEnable() {
        super.onEnable();
    }
}
