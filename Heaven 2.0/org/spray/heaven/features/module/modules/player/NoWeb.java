package org.spray.heaven.features.module.modules.player;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.input.Keyboard;
import org.spray.heaven.events.MotionEvent;
import org.spray.heaven.features.module.Category;
import org.spray.heaven.features.module.Module;
import org.spray.heaven.features.module.ModuleInfo;
import org.spray.heaven.features.module.Setting;
import org.spray.heaven.util.MovementUtil;

import java.util.Arrays;

@ModuleInfo(name = "NoWeb", category = Category.PLAYER, visible = true, key = Keyboard.KEY_NONE)
    public class NoWeb extends Module {
    private Setting mode = register(new Setting("NoWeb Mode", "Matrix", Arrays.asList("NCP")));
   //private final Setting webSpeed = register(new Setting("Web Speed", 0.8, 0.1, 2));

@EventTarget
public void MotEvent(MotionEvent event) {
       if (mode.getCurrentMode().equalsIgnoreCase("NCP")) {
            if (mc.player.onGround && mc.player.isInWeb) {
                mc.player.isInWeb = true;
            } else {
                if (mc.gameSettings.keyBindJump.isKeyDown()) {
                    return;
                }
                mc.player.isInWeb = false;
            }
            if (mc.player.isInWeb && !mc.gameSettings.keyBindSneak.isKeyDown()) {
                MovementUtil.setMotion(0.29);
            }

        }
    }
}


