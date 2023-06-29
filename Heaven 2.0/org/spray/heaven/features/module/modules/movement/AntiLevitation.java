package org.spray.heaven.features.module.modules.movement;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.init.MobEffects;
import org.lwjgl.input.Keyboard;
import org.spray.heaven.events.UpdateEvent;
import org.spray.heaven.features.module.Category;
import org.spray.heaven.features.module.Module;
import org.spray.heaven.features.module.ModuleInfo;

@ModuleInfo(name = "AntiLevitation", category = Category.MOVEMENT, visible = true, key = Keyboard.KEY_NONE)
public class AntiLevitation extends Module {
    @EventTarget
    public void onUpdate(UpdateEvent event){
        if (mc.player.isPotionActive(MobEffects.LEVITATION)) {
            mc.player.removeActivePotionEffect(MobEffects.LEVITATION);
        }
    }
}

