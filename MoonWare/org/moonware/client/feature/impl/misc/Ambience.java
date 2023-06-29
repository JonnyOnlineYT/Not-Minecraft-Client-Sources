package org.moonware.client.feature.impl.misc;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.server.SPacketTimeUpdate;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.packet.EventReceivePacket;
import org.moonware.client.event.events.impl.player.EventUpdate;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.settings.impl.ListSetting;
import org.moonware.client.settings.impl.NumberSetting;

public class Ambience extends Feature {

    private final NumberSetting time;
    private final ListSetting modeAmbri;
    private long spin;

    public Ambience() {
        super("Ambience", "Позволяет менять время суток", Type.Other);
        modeAmbri = new ListSetting("Ambience Mode", "Night", () -> true, "Day", "Night", "Morning", "Sunset", "Spin");
        time = new NumberSetting("TimeSpin Speed", 2, 1, 10, 1, () -> true);
        addSettings(modeAmbri, time);
    }

    @EventTarget
    public void onReceivePacket(EventReceivePacket event) {
        if (event.getPacket() instanceof SPacketTimeUpdate) {
            event.setCancelled(true);
        }
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        String mode = modeAmbri.getOptions();
        setSuffix(mode);
        if (mode.equalsIgnoreCase("Spin")) {
            Minecraft.world.setWorldTime(spin);
            spin = (long) (spin + time.getNumberValue() * 100);
        } else if (mode.equalsIgnoreCase("Day")) {
            Minecraft.world.setWorldTime(5000);
        } else if (mode.equalsIgnoreCase("Night")) {
            Minecraft.world.setWorldTime(17000);
        } else if (mode.equalsIgnoreCase("Morning")) {
            Minecraft.world.setWorldTime(0);
        } else if (mode.equalsIgnoreCase("Sunset")) {
            Minecraft.world.setWorldTime(13000);
        }
    }
}
