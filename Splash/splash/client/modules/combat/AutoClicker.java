package splash.client.modules.combat;

import org.lwjgl.input.Mouse;

import me.hippo.systems.lwjeb.annotation.Collect;
import me.hippo.systems.lwjeb.event.Stage;
import net.minecraft.network.play.client.C02PacketUseEntity;
import splash.api.module.Module;
import splash.api.module.category.ModuleCategory;
import splash.api.value.impl.ModeValue;
import splash.api.value.impl.NumberValue;
import splash.client.events.player.EventTick;
import splash.utilities.math.MathUtils;
import splash.utilities.time.Stopwatch;

public class AutoClicker extends Module {
	public NumberValue<Integer> CPS = new NumberValue<Integer>("CPS", 7, 2, 20, this);
	public Stopwatch timer;
	public int delay;
	public int offset;
	public boolean reverse;
	public int sprintTicks;
    public AutoClicker() {
        super("AutoClicker", "A autoclicker that should bypass alot of servers by default", ModuleCategory.COMBAT);
		timer = new Stopwatch();
		offset = 1;
    }
    
    @Collect
    public void onTick(EventTick e) {
		if (!isModuleActive() || mc.currentScreen != null) return;
		if (e.getStage().equals(Stage.PRE)) {
			if (Mouse.isButtonDown(0)) {
				if (timer.delay(delay)) {
					if (!mc.playerController.isHittingBlock && !mc.thePlayer.isUsingItem()) {
						mc.thePlayer.swingItem();
						if (mc.objectMouseOver.entityHit != null) {
							mc.playerController.attackEntity(mc.thePlayer, mc.objectMouseOver.entityHit);
							sprintTicks = 3;
						}
						if (sprintTicks-- > 0)  {
							mc.gameSettings.keyBindSprint.pressed = false;
							mc.thePlayer.setSprinting(false);
						}
						delay = Math.max(50, (1000 / CPS.getValue()) + offset);
						offset += reverse ? -MathUtils.getRandomInRange(1, 4) : -MathUtils.getRandomInRange(1, 4);
						if (offset > 15) {
							reverse = true;
						} else if (offset <= -12){
							reverse = false;
						}
						if (offset > 15 || offset < -8) {
							reverse = false;
							offset = 1;
						}
						timer.reset();
					} else {
						delay = 51;
						timer.reset();
					}
				}
			}
		}
    }

}
