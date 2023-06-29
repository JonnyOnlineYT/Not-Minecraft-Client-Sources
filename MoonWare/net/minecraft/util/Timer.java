package net.minecraft.util;

import net.minecraft.client.Minecraft;

public class Timer
{
    /**
     * How many full ticks have turned over since the last call to updateTimer(), capped at 10.
     */
    public int elapsedTicks;
    public float renderPartialTicks;
    public float field_194148_c;

    public float timerSpeed = 1.0F;

    /**
     * The time reported by the system clock at the last sync, in milliseconds
     */
    private long lastSyncSysClock;
    private final float field_194149_e;

    public Timer(float tps)
    {
        field_194149_e = 1000.0F / tps;
        lastSyncSysClock = Minecraft.getSystemTime();
    }

    /**
     * Updates all fields of the Timer using the current time
     */
    public void updateTimer()
    {
        long i = Minecraft.getSystemTime();
        field_194148_c = (float)(i - lastSyncSysClock) * timerSpeed / field_194149_e;
        lastSyncSysClock = i;
        renderPartialTicks += field_194148_c;
        elapsedTicks = (int) renderPartialTicks;
        renderPartialTicks -= (float) elapsedTicks;
    }
}
