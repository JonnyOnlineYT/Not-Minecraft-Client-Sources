package org.moonware.client.feature.impl.player;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.CPacketPlayer;
import org.moonware.client.helpers.math.GCDFix;
import org.moonware.client.utils.MWUtils;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.player.EventPreMotion;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.helpers.Helper;
import org.moonware.client.helpers.player.MovementHelper;
import org.moonware.client.settings.impl.BooleanSetting;
import org.moonware.client.settings.impl.NumberSetting;

public class AntiAFK extends Feature {

    private final BooleanSetting sendMessage;
    private final BooleanSetting spin;
    private final BooleanSetting click = new BooleanSetting("Click", true, () -> true);
    public NumberSetting sendDelay;
    public BooleanSetting invalidRotation = new BooleanSetting("Invalid Rotation", true, () -> true);
    public BooleanSetting jump = new BooleanSetting("Jump", true, () -> true);
    public float rot;

    public AntiAFK() {
        super("AntiAFK", "Позволяет встать в афк режим, без риска кикнуться", Type.Other);
        spin = new BooleanSetting("Spin", true, () -> true);
        sendMessage = new BooleanSetting("Send Message", true, () -> true);
        sendDelay = new NumberSetting("Send Delay", 500, 100, 1000, 100, sendMessage::getBoolValue);
        addSettings(spin, sendMessage, click, sendDelay, invalidRotation, jump);
    }

    @EventTarget
    public void onPreMotion(EventPreMotion event) {
        if (!MovementHelper.isMoving()) {
            if (spin.getBoolValue()) {
                float yaw = GCDFix.getFixedRotation((float) (Math.floor(spinAim(1F)) + MWUtils.randomFloat(-4, 1)));
                event.setYaw(yaw);
                Minecraft.player.renderYawOffset = yaw;
                Minecraft.player.rotationPitchHead = 0;
                Minecraft.player.rotationYawHead = yaw;
            }

            if (Minecraft.player.ticksExisted % 60 == 0 && invalidRotation.getBoolValue()) {
                Minecraft.player.connection.sendPacket(new CPacketPlayer.PositionRotation(Minecraft.player.posX + 1, Minecraft.player.posY + 1, Minecraft.player.posZ + 1, Minecraft.player.rotationYaw, Minecraft.player.rotationPitch, true));
            }

            if (Minecraft.player.ticksExisted % 60 == 0 && click.getBoolValue()) {
                Helper.mc.clickMouse();
            }

            if (Minecraft.player.ticksExisted % 40 == 0 && jump.getBoolValue()) {
                Minecraft.player.jump();
            }

            if (Minecraft.player.ticksExisted % 400 == 0 && sendMessage.getBoolValue()) {
                Minecraft.player.sendChatMessage("/homehome");
            }
        }
    }

    public float spinAim(float rots) {
        rot += rots;
        return rot;
    }
}
