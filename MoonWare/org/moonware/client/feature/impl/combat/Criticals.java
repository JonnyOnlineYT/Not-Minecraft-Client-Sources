/* 3eLeHyy#0089 */

package org.moonware.client.feature.impl.combat;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.CPacketPlayer;
import org.moonware.client.utils.MWUtils;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.packet.EventAttackSilent;
import org.moonware.client.event.events.impl.player.EventPreMotion;
import org.moonware.client.event.events.impl.player.EventUpdate;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.helpers.Helper;
import org.moonware.client.helpers.misc.TimerHelper;
import org.moonware.client.settings.impl.BooleanSetting;
import org.moonware.client.settings.impl.ListSetting;

public class Criticals extends Feature {

    private final BooleanSetting jump;
    public static ListSetting critMode = new ListSetting("Criticals Mode", "Packet", () -> true, "Packet", "WatchDog", "Ncp", "Matrix Packet 14", "Test");
    private final TimerHelper ncpTimer = new TimerHelper();

    public Criticals() {
        super("Criticals", "Автоматически наносит сущности критичиский урон при ударе", Type.Combat);
        critMode = new ListSetting("Criticals Mode", "Packet", () -> true, "Packet", "WatchDog", "Ncp", "Matrix Packet 14", "Test");
        jump = new BooleanSetting("Mini-Jump", false, () -> !critMode.currentMode.equals("Matrix Packet 14"));
        addSettings(critMode, jump);
    }

    @EventTarget
    public void onAttackSilent(EventAttackSilent event) {
        String mode = critMode.getOptions();
        double x = Minecraft.player.posX;
        double y = Minecraft.player.posY;
        double z = Minecraft.player.posZ;
        if (mode.equalsIgnoreCase("Packet")) {
            if (jump.getBoolValue()) {
                Minecraft.player.setPosition(x, y + 0.04, z);
            }
            sendPacket(new CPacketPlayer.Position(x, y + 0.11, z, false));
            sendPacket(new CPacketPlayer.Position(x, y + 0.1100013579, z, false));
            sendPacket(new CPacketPlayer.Position(x, y + 0.0000013579, z, false));
            Minecraft.player.onCriticalHit(event.getTargetEntity());
        } else if (mode.equalsIgnoreCase("Matrix Packet 14")) {
            Minecraft.player.onGround = false;
            double yMotion = 1.0E-12;
            Minecraft.player.fallDistance = (float) yMotion;
            Minecraft.player.motionY = yMotion;
            Minecraft.player.isCollidedVertically = true;
            sendPacket(new CPacketPlayer.Position(x, y + MWUtils.randomFloat(0.00000001F, 0.0000004F), z, false));
            sendPacket(new CPacketPlayer.Position(x, y + MWUtils.randomFloat(0.00000001F, 0.0000002F), z, false));

        } else if (mode.equalsIgnoreCase("Test")) {

            Minecraft.player.onGround = false;
            double yMotion = 1.0E-12;
            Minecraft.player.fallDistance = (float) yMotion;
            Minecraft.player.motionY = yMotion;
            Minecraft.player.isCollidedVertically = true;
        } else if (mode.equalsIgnoreCase("Ncp")) {
            if (jump.getBoolValue()) {
                Minecraft.player.setPosition(x, y + 0.04, z);
            }
            if (Helper.timerHelper.hasReached(150) && Minecraft.player.onGround) {
                sendPacket(new CPacketPlayer.Position(x, y + 0.11, z, false));
                sendPacket(new CPacketPlayer.Position(x, y + 0.1100013579, z, false));
                sendPacket(new CPacketPlayer.Position(x, y + 0.0000013579, z, false));
                Minecraft.player.onCriticalHit(event.getTargetEntity());
                Helper.timerHelper.reset();
            }
        } else if (mode.equalsIgnoreCase("WatchDog")) {
            if (jump.getBoolValue()) {
                Minecraft.player.setPosition(x, y + 0.04, z);
            }
            double random = MWUtils.randomFloat(4.0E-7f, 4.0E-5f);
            if (Helper.timerHelper.hasReached(100)) {
                for (double value : new double[]{0.007017625 + random, 0.007349825 + random, 0.006102874 + random}) {
                    Minecraft.player.fallDistance = (float) value;
                    Minecraft.player.isCollidedVertically = true;
                    Minecraft.player.connection.sendPacket(new CPacketPlayer.PositionRotation(Minecraft.player.posX, Minecraft.player.posY + value, Minecraft.player.posZ, Minecraft.player.rotationYaw, Minecraft.player.rotationPitch, false));
                    Helper.timerHelper.reset();
                }
            }
            Minecraft.player.onCriticalHit(event.getTargetEntity());
        }
    }

    @EventTarget
    public void onPreMotion(EventPreMotion event) {
        String mode = critMode.getOptions();
        if (mode.equalsIgnoreCase("Test")) {
            if (Minecraft.player.onGround) {
                Minecraft.player.onGround = false;
                Minecraft.player.setPositionAndUpdate(Minecraft.player.posX, Minecraft.player.posY + MWUtils.randomFloat(0.2F, 0.2F), Minecraft.player.posZ);
            }
        }
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        String mode = critMode.getOptions();
        setSuffix(mode);
    }
}