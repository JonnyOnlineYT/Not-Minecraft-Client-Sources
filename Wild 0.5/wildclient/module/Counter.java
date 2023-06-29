// 
// Decompiled by Procyon v0.5.36
// 

package black.nigger.wildclient.module;

public class Counter
{
    private long lastMS;
    private long prevMS;
    
    public Counter() {
        this.lastMS = 0L;
        this.prevMS = 0L;
    }
    
    public boolean isDelay(final long delay) {
        return System.currentTimeMillis() - this.lastMS >= delay;
    }
    
    public long getCurrentMS() {
        return System.nanoTime() / 1000000L;
    }
    
    public void setLastMS(final long lastMS) {
        this.lastMS = lastMS;
    }
    
    public void setLastMS() {
        this.lastMS = System.currentTimeMillis();
    }
    
    public int convertToMS(final int d) {
        return 1000 / d;
    }
    
    public boolean hasReached(final float f) {
        return this.getCurrentMS() - this.lastMS >= f;
    }
    
    public void reset() {
        this.lastMS = this.getCurrentMS();
    }
    
    public boolean delay(final float milliSec) {
        return this.getTime() - this.prevMS >= milliSec;
    }
    
    public long getTime() {
        return System.nanoTime() / 1000000L;
    }
}
