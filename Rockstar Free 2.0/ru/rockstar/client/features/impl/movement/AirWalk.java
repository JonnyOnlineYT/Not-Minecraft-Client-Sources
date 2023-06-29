package ru.rockstar.client.features.impl.movement;

import net.minecraft.block.BlockAir;
import net.minecraft.inventory.ClickType;
import net.minecraft.util.math.BlockPos;
import ru.rockstar.api.event.EventTarget;
import ru.rockstar.api.event.event.EventUpdate;
import ru.rockstar.api.utils.world.InventoryHelper;
import ru.rockstar.client.features.Category;
import ru.rockstar.client.features.Feature;

public class AirWalk extends Feature {

    public AirWalk() {
        super("AirWalk", "Позволяет ходить и прыгать в воздухе", 0, Category.MOVEMENT);
    }
    boolean jumped = false;
    boolean aired = false;

    @EventTarget
    public void sdd(EventUpdate eventUpdate) {
    	if (mc.player.onGround) {
    		mc.player.jump();
    		jumped = true;
    	}
    	if (timerHelper.hasReached(540)) {
    		mc.player.onGround = true;
    			mc.player.jump();
    			jumped = false;
    			timerHelper.reset();
    	}
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
        aired = false;
		jumped = false;
    }
}
