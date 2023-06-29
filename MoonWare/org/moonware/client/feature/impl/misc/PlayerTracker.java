package org.moonware.client.feature.impl.misc;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Formatting;
import org.moonware.client.utils.MWUtils;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.packet.EventReceivePacket;
import org.moonware.client.event.events.impl.player.EventPreMotion;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.settings.impl.NumberSetting;
import org.moonware.client.ui.shader.notification.NotificationManager;
import org.moonware.client.ui.shader.notification.NotificationType;

public class PlayerTracker extends Feature {
    public NumberSetting radius = new NumberSetting("Radius", 1000.0F, 100.0F, 5000.0F, 10.0F, () -> Boolean.valueOf(true));

    public PlayerTracker() {
        super("PlayerTracker", "Поиск игроков в мире.", Type.Other);
        addSettings(radius);
    }

    @EventTarget
    public void onFind(EventPreMotion eventPreMotionUpdate) {
        Minecraft.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, new BlockPos(
                MWUtils.randomFloat(-radius.getNumberValue(), radius.getNumberValue()), 0.0D,
                MWUtils.randomFloat(-radius.getNumberValue(), radius.getNumberValue())), EnumFacing.DOWN));
    }

    @EventTarget
    public void onFindReceive(EventReceivePacket eventReceivePacket) {
        SPacketBlockChange packetBlockChange = (SPacketBlockChange)eventReceivePacket.getPacket();
        if (eventReceivePacket.getPacket() instanceof SPacketBlockChange) {
            MWUtils.sendChat(Formatting.WHITE + "BLACK NIGGER замечен на кординатах > " + Formatting.RED + packetBlockChange
                    .getBlockPosition().getX() + " " + packetBlockChange.getBlockPosition().getZ());
            NotificationManager.publicity("Player Tracker", "Player Found on coords > " + Formatting.RED + packetBlockChange
                    .getBlockPosition().getX() + " " + packetBlockChange.getBlockPosition().getZ(), 2, NotificationType.INFO);
        }
    }
}
