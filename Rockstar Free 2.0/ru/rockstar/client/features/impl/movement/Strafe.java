package ru.rockstar.client.features.impl.movement;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.ClickType;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.BlockPos;
import ru.rockstar.Main;
import ru.rockstar.api.event.EventTarget;
import ru.rockstar.api.event.event.EventAction;
import ru.rockstar.api.event.event.EventMove;
import ru.rockstar.api.event.event.EventPreMotionUpdate;
import ru.rockstar.api.event.event.EventStep;
import ru.rockstar.api.event.event.EventUpdate;
import ru.rockstar.api.utils.movement.MovementHelper;
import ru.rockstar.api.utils.world.TimerHelper;
import ru.rockstar.client.features.Category;
import ru.rockstar.client.features.Feature;
import ru.rockstar.client.features.impl.combat.KillAura;
import ru.rockstar.client.features.impl.combat.ShieldDesync;
import ru.rockstar.client.features.impl.player.NoClip;
import ru.rockstar.client.ui.settings.impl.BooleanSetting;
import ru.rockstar.client.ui.settings.impl.ListSetting;
import ru.rockstar.client.ui.settings.impl.NumberSetting;

public class Strafe extends Feature {
    public Strafe() {
        super("Strafe", "Стрейфиться", 0, Category.MOVEMENT);
    }


    
    
    
    
    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {
        if (!isToggled()) {
            return;
        }
        if (mc.player.onGround) {
            return;
        }
        if (mc.gameSettings.keyBindSneak.pressed) {
        	return;
        }
        if (mc.player.isInWater()) {
        	return;
        }
        if (ShieldDesync.mc.player.ticksExisted % 3 == 0) {
        	 if (!mc.player.isUsingItem()) {
        		 if (MovementHelper.isMoving() && MovementHelper.getSpeed() < 0.2177f) {
                     MovementHelper.strafe(0.2177f);
             	}
        	 }
        }
    }
    
}
