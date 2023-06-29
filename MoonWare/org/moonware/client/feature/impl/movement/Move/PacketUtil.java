package org.moonware.client.feature.impl.movement.Move;

import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketCombatEvent;

public class PacketUtil {
    public static Minecraft mc = Minecraft.getMinecraft();
    public static void sendPacketWithoutEvent(Packet<?> packet) {
        Minecraft.player.connection.handleCombatEvent((SPacketCombatEvent) packet);
    }
    public static void sendPacket(Packet<?> packetIn) {
        Minecraft.player.connection.sendPacket(packetIn);
    }
}
