package org.spray.heaven.features.module.modules.movement;

import org.lwjgl.input.Keyboard;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.init.MobEffects;
import org.spray.heaven.events.MotionEvent;
import org.spray.heaven.features.module.Category;
import org.spray.heaven.features.module.Module;
import org.spray.heaven.features.module.ModuleInfo;
import org.spray.heaven.features.module.Setting;
import org.spray.heaven.util.MovementUtil;

@ModuleInfo(name = "WaterSpeed", category = Category.MOVEMENT, visible = true, key = Keyboard.KEY_NONE)
public class WaterSpeed extends Module {

    private Setting potionCheck = register(new Setting("PotionCheck", true));
    private Setting speed = register(new Setting("Speed", 0.45, 0.1, 5));

    @EventTarget
    public void onMotion(MotionEvent event) {
        if (!mc.player.isPotionActive(MobEffects.SPEED) && potionCheck.isToggle())
            return;

        if (mc.player.isInWater())
            MovementUtil.setMotion(speed.getValue());

    }
}