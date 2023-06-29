package org.moonware.client.feature.impl.movement;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.player.EventBlockInteract;
import org.moonware.client.event.events.impl.player.EventUpdate;
import org.moonware.client.event.events.impl.render.EventRender3D;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.helpers.Helper;
import org.moonware.client.helpers.render.RenderHelper;
import org.moonware.client.settings.impl.BooleanSetting;
import org.moonware.client.settings.impl.ColorSetting;
import org.moonware.client.settings.impl.NumberSetting;

import java.awt.*;

public class WebTP extends Feature {

    public static NumberSetting maxBlockReachValue;
    public static BooleanSetting webESP;
    public static BooleanSetting autoDisable;
    public static ColorSetting webESPColor;

    private int x, y, z;
    private boolean wasClick;

    public WebTP() {
        super("WebTPexploit", "Позволяет телепортироваться на большие расстояния с помощью паутины", Type.Movement);
        maxBlockReachValue = new NumberSetting("Max reach value", 120, 10, 500, 10, () -> true);
        autoDisable = new BooleanSetting("Auto Disable", true, () -> true);
        webESP = new BooleanSetting("Position ESP", true, () -> true);
        webESPColor = new ColorSetting("Color", new Color(0xFFFFFF).getRGB(), webESP::getBoolValue);
        addSettings(maxBlockReachValue, autoDisable, webESP, webESPColor);
    }

    @Override
    public void onDisable() {
        x = (int) Minecraft.player.posX;
        y = (int) Minecraft.player.posY;
        z = (int) Minecraft.player.posZ;
        wasClick = false;
        super.onDisable();
    }

    @EventTarget
    public void onRender3D(EventRender3D event) {
        if (Minecraft.player == null || Minecraft.world == null)
            return;
        if (Minecraft.objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK && Minecraft.player.isInWeb) {
            Color color = new Color(webESPColor.getColorValue());
            BlockPos pos = Minecraft.objectMouseOver.getBlockPos();
            if (webESP.getBoolValue()) {
                GlStateManager.pushMatrix();
                RenderHelper.blockEsp(pos, color, true);
                GlStateManager.popMatrix();
            }
        }
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (Minecraft.player == null || Minecraft.world == null)
            return;
        if (wasClick && Minecraft.player.isInWeb) {
            Minecraft.player.onGround = false;
            Minecraft.player.motionY *= -12;
            Minecraft.player.connection.sendPacket(new CPacketPlayer.Position(x, y + 3, z, true));
        } else if (Minecraft.player.posX == x && Minecraft.player.posY == y + 3 && Minecraft.player.posZ == z) {
            wasClick = false;
            if (autoDisable.getBoolValue()) {
                toggle();
            }
        }
    }

    @EventTarget
    public void onBlockInteract(EventBlockInteract event) {
        if (Minecraft.player == null || Minecraft.world == null)
            return;
        if (event.getPos() != null) {
            BlockPos pos = event.getPos();
            if (!wasClick) {
                x = pos.getX();
                y = pos.getY();
                z = pos.getZ();
                wasClick = true;
            }
        }
    }
}
