package ru.rockstar.client.features.impl.movement;

import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import ru.rockstar.api.event.EventTarget;
import ru.rockstar.api.event.event.EventSafeWalk;
import ru.rockstar.api.event.event.EventUpdate;
import ru.rockstar.client.features.Category;
import ru.rockstar.client.features.Feature;
import ru.rockstar.client.ui.settings.impl.BooleanSetting;
import ru.rockstar.client.ui.settings.impl.ListSetting;
import ru.rockstar.client.ui.settings.impl.NumberSetting;

public class SafeWalk extends Feature {
	private final ListSetting smode;
    public SafeWalk() {
        super("SafeWalk", "Не дает сойти с блока", 0, Category.MOVEMENT);
        this.smode = new ListSetting("SafeWalk Mode", "Client", () -> true,  "Client", "Packet",  "Cancel");
        this.addSettings(this.smode);
    }
    
    @EventTarget
    public void onUpdate(EventUpdate event) {
    	/*
    	mc.gameSettings.keyBindJump.pressed = (mc.player.ticksExisted) % 2 == 0;
    	mc.gameSettings.keyBindForward.pressed = true;*/
        BlockPos pos = new BlockPos(mc.player.posX, mc.player.posY - 1, mc.player.posZ);
        if (!mc.player.onGround) 
        return;
        switch (smode.getOptions()) {
        case "Client":
        	mc.gameSettings.keyBindSneak.pressed = mc.world.getBlockState(pos).getBlock() == Blocks.AIR;
        	break;
        }
    }
    
    @EventTarget
    public void onSafe(EventSafeWalk eventSafeWalk) {
    	 switch (smode.getOptions()) {
         case "Packet":
        	 eventSafeWalk.setCancelled(mc.player.onGround);
         	break;
         case "Cancel":
        	 eventSafeWalk.setCancelled(mc.player.onGround);
         	break;
         }
    }
}
