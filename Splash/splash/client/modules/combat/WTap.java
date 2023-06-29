package splash.client.modules.combat;

import org.lwjgl.input.Mouse;

import me.hippo.systems.lwjeb.annotation.Collect;
import me.hippo.systems.lwjeb.event.Stage;
import net.minecraft.network.play.client.C02PacketUseEntity;
import splash.Splash;
import splash.api.module.Module;
import splash.api.module.category.ModuleCategory;
import splash.api.value.impl.ModeValue;
import splash.api.value.impl.NumberValue;
import splash.client.events.player.EventTick;
import splash.utilities.math.MathUtils;
import splash.utilities.time.Stopwatch;

public class WTap extends Module {
	public NumberValue<Integer> tick = new NumberValue<Integer>("Ticks", 4, 2, 10, this); 
	public int ticks;
    public WTap() {
        super("WTap", "WTaps for you", ModuleCategory.COMBAT); 
        ticks = tick.getValue();
    }
    
    @Collect
    public void onTick(EventTick e) {
    	Aura ka = ((Aura)Splash.getInstance().getModuleManager().getModuleByClass(Aura.class));
    	
    	if (mc.thePlayer.isMoving() && (Mouse.isButtonDown(0) || ka.isModuleActive() && ka.getCurrentTarget() != null)) {
        	if (mc.thePlayer.ticksExisted % ticks == 0 ) {
        		mc.thePlayer.setSprinting(false);
        		ticks = tick.getValue() + MathUtils.getRandomInRange(-1, 1);
        	} else {
        		mc.thePlayer.setSprinting(true);
        	}
    	}
    }

}
