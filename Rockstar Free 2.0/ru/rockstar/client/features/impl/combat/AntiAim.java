package ru.rockstar.client.features.impl.combat;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketConfirmTransaction;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketKeepAlive;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketCustomPayload;
import net.minecraft.network.play.server.SPacketKeepAlive;
import ru.rockstar.api.event.EventTarget;
import ru.rockstar.api.event.event.EventPreMotionUpdate;
import ru.rockstar.api.event.event.EventSendPacket;
import ru.rockstar.api.event.event.EventUpdate;
import ru.rockstar.api.utils.world.TimerHelper;
import ru.rockstar.client.features.Category;
import ru.rockstar.client.features.Feature;
import ru.rockstar.client.ui.settings.impl.BooleanSetting;
import ru.rockstar.client.ui.settings.impl.ListSetting;
import ru.rockstar.client.ui.settings.impl.NumberSetting;

public class AntiAim extends Feature {

    public final LinkedList<double[]> positions = new LinkedList<>();
    public List<Packet<?>> packets = new ArrayList<>();
    public TimerHelper pulseTimer = new TimerHelper();
    public NumberSetting ticks = new NumberSetting("Ticks", 8, 1, 30, 1, () -> true);
    private boolean enableFakeLags;

    public AntiAim() {
        super("AntiAim", "Уменьшает шанс попадания по вам", 0, Category.PLAYER);
        addSettings(ticks);
    }

    @Override
    public void onEnable() {
        synchronized (this.positions) {
            this.positions.add(new double[]{mc.player.posX, mc.player.getEntityBoundingBox().minY + mc.player.getEyeHeight() / 2.0f, mc.player.posZ});
            this.positions.add(new double[]{mc.player.posX, mc.player.getEntityBoundingBox().minY, mc.player.posZ});
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
    	if (mc.player == null || mc.world == null) {
    		return;
    	}
        synchronized (this.positions) {
            this.positions.add(new double[]{mc.player.posX, mc.player.getEntityBoundingBox().minY, mc.player.posZ});
        }
        if (this.pulseTimer.hasReached(ticks.getNumberValue() * 50)) {
            try {
                this.enableFakeLags = true;
                Iterator<Packet<?>> packetIterator = this.packets.iterator();
                while (packetIterator.hasNext()) {
                    mc.player.connection.sendPacket(packetIterator.next());
                    packetIterator.remove();
                }
                this.enableFakeLags = false;
            } catch (Exception e) {
                this.enableFakeLags = false;
            }
            synchronized (this.positions) {
                this.positions.clear();
            }
            this.pulseTimer.reset();
        }
    }

    @EventTarget
    public void onSendPacket(EventSendPacket event) {
        if (mc.world == null ||mc.player == null || !(event.getPacket() instanceof CPacketPlayer) || enableFakeLags) {
            return;
        }
        event.setCancelled(true);
        if (!(event.getPacket() instanceof CPacketPlayer.Position) && !(event.getPacket() instanceof CPacketPlayer.PositionRotation)) {
            return;
        }
        this.packets.add(event.getPacket());
    }

}