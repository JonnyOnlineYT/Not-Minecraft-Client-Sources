package ua.apraxia.modules.impl.move;

import net.minecraft.client.Minecraft;
import ua.apraxia.eventapi.EventTarget;
import ua.apraxia.eventapi.events.impl.player.EventUpdate;
import ua.apraxia.modules.Categories;
import ua.apraxia.modules.Module;
import ua.apraxia.utility.Utility;
import ua.apraxia.utility.other.MoveUtility;

public class Sprint extends Module {
    public Sprint() {
        super("Sprint", Categories.Movement);
    }

    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {
        if (Minecraft.player.getFoodStats().getFoodLevel() / 2 > 3) {
            Minecraft.player.setSprinting(MoveUtility.isMoving());
        }
    }

    @Override
    public void onDisable() {
        Minecraft.player.setSprinting(false);
        super.onDisable();
    }
}
