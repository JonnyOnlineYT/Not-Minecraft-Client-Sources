package org.spray.heaven.features.module.modules.player;
import net.minecraft.inventory.ClickType;
import org.lwjgl.input.Keyboard;
import org.spray.heaven.features.module.Category;
import org.spray.heaven.features.module.Module;
import org.spray.heaven.features.module.ModuleInfo;

@ModuleInfo(name = "InventoryDroper", category = Category.PLAYER, visible = true, key = Keyboard.KEY_NONE)
public class InventoryDroper extends Module {

    @Override
    public void onEnable() {
        super.onEnable();
        for (int o = 0; o < 46; ++o) {
            mc.playerController.windowClick(mc.player.inventoryContainer.windowId, o, 1, ClickType.THROW, mc.player);
        }
    }
}

