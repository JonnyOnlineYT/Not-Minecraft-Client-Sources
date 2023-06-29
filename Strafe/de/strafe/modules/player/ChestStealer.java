package de.strafe.modules.player;

import com.eventapi.EventTarget;
import de.strafe.events.EventUpdate;
import de.strafe.modules.Category;
import de.strafe.modules.Module;
import de.strafe.utils.TimeUtil;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.Slot;

public class ChestStealer extends Module {
    private final TimeUtil time = new TimeUtil();

    public ChestStealer() {
        super("ChestStealer", 0, Category.PLAYER);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (mc.thePlayer.openContainer != null) {
            if (mc.thePlayer.openContainer instanceof ContainerChest) {
                ContainerChest chest = (ContainerChest) mc.thePlayer.openContainer;
                int i;
                for (i = 0; i < chest.numRows * 9; i++) {
                    if (mc.thePlayer.openContainer == null)
                        break;

                    Slot slot = chest.inventorySlots.get(i);

                    if (slot.getStack() == null)
                        continue;
                    if (!time.hasReached(80)) {
                        return;
                    }

                    mc.playerController.windowClick(chest.windowId, i, 0, 1, mc.thePlayer);
                }
            }
        }
    }
}