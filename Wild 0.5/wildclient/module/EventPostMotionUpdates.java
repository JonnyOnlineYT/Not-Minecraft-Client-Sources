// 
// Decompiled by Procyon v0.5.36
// 

package black.nigger.wildclient.module;

public class EventPostMotionUpdates implements Event
{
    public float yaw;
    public float pitch;
    public double y;
    public boolean ground;
    
    public void UpdateEvent() {
    }
    
    public void UpdateEvent(final double y, final float yaw, final float pitch, final boolean ground) {
        this.yaw = yaw;
        this.pitch = pitch;
        this.y = y;
        this.ground = ground;
    }
}
