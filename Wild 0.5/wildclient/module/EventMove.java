// 
// Decompiled by Procyon v0.5.36
// 

package black.nigger.wildclient.module;

import net.minecraft.util.MovementInput;
import com.darkmagician6.eventapi.events.callables.EventCancellable;

public class EventMove extends EventCancellable
{
    public MovementInput movementInput;
    public double motionX;
    public double motionY;
    public double motionZ;
    
    public EventMove(final MovementInput movementInput) {
        this.movementInput = movementInput;
    }
    
    public EventMove(final double x, final double y, final double z) {
        this.motionX = x;
        this.motionY = y;
        this.motionZ = z;
    }
    
    public double getMotionX() {
        return this.motionX;
    }
    
    public void setMotionX(final double d) {
        this.motionX = d;
    }
    
    public double getMotionY() {
        return this.motionY;
    }
    
    public void setMotionY(final double d) {
        this.motionY = d;
    }
    
    public double getMotionZ() {
        return this.motionZ;
    }
    
    public void setMotionZ(final double d) {
        this.motionZ = d;
    }
}
