package org.spray.heaven.features.module.modules.render;

import org.lwjgl.input.Keyboard;
import org.spray.heaven.features.module.Category;
import org.spray.heaven.features.module.Module;
import org.spray.heaven.features.module.ModuleInfo;
import org.spray.heaven.main.Wrapper;

@ModuleInfo(name = "Notifications", category = Category.DISPLAY, visible = true, key = Keyboard.KEY_NONE)
public class Notifications extends Module {

    @Override
    public void onRender(int width, int height, float tickDelta) {
        Wrapper.getNotification().render(height);
    }

}
