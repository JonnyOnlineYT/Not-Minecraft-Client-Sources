// 
// Decompiled by Procyon v0.5.36
// 

package black.nigger.wildclient.module;

public abstract class EventStoppable implements Event
{
    private boolean stopped;
    
    protected EventStoppable() {
    }
    
    public void stop() {
        this.stopped = true;
    }
    
    public boolean isStopped() {
        return this.stopped;
    }
}
