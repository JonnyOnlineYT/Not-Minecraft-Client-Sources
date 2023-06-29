package org.moonware.client.helpers;

import net.minecraft.network.play.server.SPacketTimeUpdate;
import net.minecraft.util.math.MathHelper;
import org.moonware.client.event.EventManager;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.packet.EventReceivePacket;

import java.util.Arrays;

public class TpsHelper
        implements Helper {
    private static float[] tickRates = new float[20];
    private int nextIndex;
    private long timeLastTimeUpdate = -1L;

    public TpsHelper() {
        Arrays.fill(tickRates, 0.0f);
        EventManager.register(this);
    }

    public static float getTickRate() {
        float numTicks = 0.0f;
        float sumTickRates = 0.0f;
        for (float tickRate : tickRates) {
            if (!(tickRate > 0.0f)) continue;
            sumTickRates += tickRate;
            numTicks += 1.0f;
        }
        return MathHelper.clamp(sumTickRates / numTicks, 0.0f, 20.0f);
    }

    private void onTimeUpdate() {
        if (timeLastTimeUpdate != -1L) {
            float timeElapsed = (float)(System.currentTimeMillis() - timeLastTimeUpdate) / 1000.0f;
            tickRates[nextIndex % tickRates.length] = MathHelper.clamp(20.0f / timeElapsed, 0.0f, 20.0f);
            ++nextIndex;
        }
        timeLastTimeUpdate = System.currentTimeMillis();
    }

    @EventTarget
    public void onReceivePacket(EventReceivePacket event) {
        if (event.getPacket() instanceof SPacketTimeUpdate) {
            onTimeUpdate();
        }
    }
}
