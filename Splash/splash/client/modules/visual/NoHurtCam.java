package splash.client.modules.visual;

import me.hippo.systems.lwjeb.annotation.Collect;
import me.hippo.systems.lwjeb.event.Stage;
import splash.api.module.Module;
import splash.api.module.category.ModuleCategory;
import splash.client.events.player.EventTick;

public class NoHurtCam extends Module {

	public NoHurtCam() {
		super("NoHurtcam", "Disables screen shake when damaged.", ModuleCategory.VISUALS);
	}
	
	@Collect
	public void onTick(EventTick e) {
		if (e.getStage().equals(Stage.PRE) && mc.thePlayer != null) {
			mc.thePlayer.maxHurtTime = 0;
		}
	}
}
