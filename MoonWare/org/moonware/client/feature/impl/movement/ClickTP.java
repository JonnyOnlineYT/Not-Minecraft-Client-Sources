package org.moonware.client.feature.impl.movement;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.Formatting;
import org.lwjgl.input.Mouse;
import org.moonware.client.event.EventManager;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.packet.EventSendPacket;
import org.moonware.client.event.events.impl.player.EventBlockInteract;
import org.moonware.client.event.events.impl.render.EventRender3D;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.feature.impl.combat.KillAura;
import org.moonware.client.settings.impl.BooleanSetting;
import org.moonware.client.settings.impl.ColorSetting;
import org.moonware.client.settings.impl.NumberSetting;
import org.moonware.client.ui.shader.notification.NotificationManager;
import org.moonware.client.ui.shader.notification.NotificationType;

import java.awt.*;

public class ClickTP
        extends Feature {
    public static NumberSetting maxBlockReachValue;
    public static BooleanSetting posESP;
    public static BooleanSetting autoDisable;
    public static ColorSetting posESPColor;
    private int x;
    private int y;
    private int z;
    private boolean wasClick;

    public ClickTP() {
        super("ClickTP", "\u0422\u0435\u043b\u0435\u043f\u043e\u0440\u0442\u0438\u0440\u0443\u0435\u0442 \u0432\u0430\u0441 \u043d\u0430 \u0442\u043e\u0447\u043a\u0443 \u043a\u0443\u0434\u0430 \u0432\u044b \u0437\u0430\u0436\u0430\u043b\u0438 \u041b\u041a\u041c", Type.Movement);
        maxBlockReachValue = new NumberSetting("Max reach value", 120.0f, 10.0f, 500.0f, 10.0f, () -> true);
        autoDisable = new BooleanSetting("Auto Disable", true, () -> true);
        posESP = new BooleanSetting("Position ESP", true, () -> true);
        posESPColor = new ColorSetting("Color", Color.WHITE.getRGB(), posESP::getCurrentValue);
        addSettings(maxBlockReachValue, autoDisable, posESP, posESPColor);
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
        if (Minecraft.player == null || Minecraft.world == null) {
            return;
        }
        if (Minecraft.objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK) {
            Color color = new Color(posESPColor.getColor());
            BlockPos pos = Minecraft.objectMouseOver.getBlockPos();
            if (posESP.getCurrentValue()) {
                GlStateManager.pushMatrix();
                //RenderHelper2.blockEsp(pos, color, true, 1.0, 1.0);
                GlStateManager.popMatrix();
            }
        }
    }

    @EventTarget
    public void onSendPacket(EventSendPacket event) {
        if (Minecraft.player == null || Minecraft.world == null) {
            return;
        }
        if (event.getPacket() instanceof CPacketPlayer) {
            CPacketPlayer packet = (CPacketPlayer)event.getPacket();
            if (Mouse.isButtonDown(0) && KillAura.target == null) {
                mc.player.motionY = 0.0201F;
                for(int i = 0 ; i < 30 ; i++) {
                    packet.x = x;
                    packet.y = y;
                    packet.z = z;
                }
                if (Minecraft.player.posX == (double) x && Minecraft.player.posY == (double)(y - 1) && Minecraft.player.posZ == (double) z) {
                    NotificationManager.publicity("Click TP", Formatting.GREEN + "Successfully " + Formatting.WHITE + "teleported to " + Formatting.RED + "X: " + (int) Minecraft.player.posX + " Y: " + (int) Minecraft.player.posY + " Z: " + (int) Minecraft.player.posZ, 4, NotificationType.SUCCESS);
                    if (autoDisable.getCurrentValue()) {
                        toggle();
                        EventManager.unregister(this);
                    }
                }
            }
        }
    }

    @EventTarget
    public void onBlockInteract(EventBlockInteract event) {
        if (Minecraft.player == null || Minecraft.world == null) {
            return;
        }
        if (event.getPos() != null) {
            BlockPos pos = event.getPos();
            x = pos.getX();
            y = pos.getY();
            z = pos.getZ();
            wasClick = true;
        }
    }
}
