/*
 * Decompiled with CFR 0.150.
 */
package ru.fluger.client.helpers.misc;

import ru.fluger.client.helpers.Helper;

public class TimerHelper
implements Helper {
    private long ms = this.getCurrentMS();

    private long getCurrentMS() {
        return System.currentTimeMillis();
    }

    public boolean hasReached(double milliseconds) {
        return (double)(this.getCurrentMS() - this.ms) > milliseconds;
    }

    public void reset() {
        this.ms = this.getCurrentMS();
    }

    public long getTime() {
        return this.getCurrentMS() - this.ms;
    }
}

