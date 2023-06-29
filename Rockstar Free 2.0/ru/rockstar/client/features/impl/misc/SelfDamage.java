package ru.rockstar.client.features.impl.misc;

import ru.rockstar.api.event.EventTarget;
import ru.rockstar.api.event.event.EventPreMotionUpdate;
import ru.rockstar.api.event.event.EventUpdate;
import ru.rockstar.client.features.Category;
import ru.rockstar.client.features.Feature;

public class SelfDamage extends Feature {

    private int jumps = 0;

    public SelfDamage() {
        super("SelfDamage", "Вы наносите себе дамаг", 0, Category.PLAYER);
    }

    boolean jumped = false;
    boolean aired = false;
    int ticks = 0;

    @EventTarget
    public void sdd(EventUpdate eventUpdate) {
    	if (mc.player.onGround) {
    		mc.player.jump();
    		jumped = true;
    	}
    	if (timerHelper.hasReached(530)) {
    		mc.player.onGround = true;
    			mc.player.jump();
    			ticks++;
    			jumped = false;
    			timerHelper.reset();
    	}
    	if (ticks >= 3) {
    		toggle();
    	}
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
        aired = false;
		jumped = false;
		ticks = 0;
    }
}