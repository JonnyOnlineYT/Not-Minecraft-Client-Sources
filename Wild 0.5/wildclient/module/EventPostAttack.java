// 
// Decompiled by Procyon v0.5.36
// 

package black.nigger.wildclient.module;

import net.minecraft.entity.Entity;

public class EventPostAttack implements Event
{
    private Entity attacker;
    private Entity target;
    
    public EventPostAttack(final Entity attacker, final Entity target) {
        this.attacker = attacker;
        this.target = target;
    }
    
    public Entity getAttacker() {
        return this.attacker;
    }
    
    public Entity getTarget() {
        return this.target;
    }
}
