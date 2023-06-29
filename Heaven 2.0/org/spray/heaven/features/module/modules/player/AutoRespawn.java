package org.spray.heaven.features.module.modules.player;

import net.minecraft.client.gui.GuiGameOver;
import org.lwjgl.input.Keyboard;
import org.spray.heaven.features.module.Category;
import org.spray.heaven.features.module.Module;
import org.spray.heaven.features.module.ModuleInfo;

@ModuleInfo(name = "AutoRespawn", category = Category.MOVEMENT, visible = true, key = Keyboard.KEY_NONE)
public class AutoRespawn extends Module {
    public void onUpdate() {
        if (mc.player != null && mc.world != null) {
            if (mc.currentScreen instanceof GuiGameOver) {
                mc.player.respawnPlayer();
            }

        }
    }
}
