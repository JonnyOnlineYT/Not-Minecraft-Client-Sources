package org.moonware.client.feature.impl.movement;

import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.packet.EventSendPacket;
import org.moonware.client.event.events.impl.player.EventUpdate;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.helpers.Helper;
import org.moonware.client.helpers.misc.TimerHelper;
import org.moonware.client.settings.impl.NumberSetting;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class FakeLags extends Feature {

    public final LinkedList<double[]> positions = new LinkedList<>();
    public List<Packet<?>> packets = new ArrayList<>();
    public TimerHelper pulseTimer = new TimerHelper();
    public NumberSetting ticks = new NumberSetting("Ticks", 8, 1, 30, 1, () -> true);
    private boolean enableFakeLags;

    public FakeLags() {
        super("Fake Lags", "У других вы лагаете", Type.Movement);
        addSettings(ticks);
    }

    @Override
    public void onEnable() {
        synchronized (positions) {
            positions.add(new double[]{Minecraft.player.posX, Minecraft.player.getEntityBoundingBox().minY + Minecraft.player.getEyeHeight() / 2.0f, Minecraft.player.posZ});
            positions.add(new double[]{Minecraft.player.posX, Minecraft.player.getEntityBoundingBox().minY, Minecraft.player.posZ});
        }
        super.onEnable();
    }

    @Override
    public void onDisable() {
        packets.clear();
        positions.clear();
    }


    @EventTarget
    public void onUpdate(EventUpdate event) {

        synchronized (positions) {
            positions.add(new double[]{Minecraft.player.posX, Minecraft.player.getEntityBoundingBox().minY, Minecraft.player.posZ});
        }
        if (pulseTimer.hasReached(ticks.getNumberValue() * 50)) {
            try {
                enableFakeLags = true;
                Iterator<Packet<?>> packetIterator = packets.iterator();
                while (packetIterator.hasNext()) {
                    Minecraft.player.connection.sendPacket(packetIterator.next());
                    packetIterator.remove();
                }
                enableFakeLags = false;
            } catch (Exception e) {
                enableFakeLags = false;
            }
            synchronized (positions) {
                positions.clear();
            }
            pulseTimer.reset();
        }

    }

    @EventTarget
    public void onSendPacket(EventSendPacket event) {
        if (Minecraft.player == null || !(event.getPacket() instanceof CPacketPlayer) || enableFakeLags) {
            return;
        }
        event.setCancelled(true);
        if (!(event.getPacket() instanceof CPacketPlayer.Position) && !(event.getPacket() instanceof CPacketPlayer.PositionRotation)) {
            return;
        }
        packets.add(event.getPacket());

    }

}
