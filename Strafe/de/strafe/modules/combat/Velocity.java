package de.strafe.modules.combat;

import de.strafe.modules.Category;
import com.eventapi.EventTarget;
import de.strafe.events.EventPacket;
import de.strafe.modules.Module;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;
import org.lwjgl.input.Keyboard;

public class Velocity extends Module {
    public Velocity() {
        super("Velocity", 0, Category.COMBAT);
    }

    @EventTarget
    public void onUpdate(EventPacket event) {
        if (event.getPacket() instanceof S12PacketEntityVelocity || event.getPacket() instanceof S27PacketExplosion) {
            event.setCancelled(true);
        }
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

}
