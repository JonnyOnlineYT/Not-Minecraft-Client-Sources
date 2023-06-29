package splash.client.modules.visual;

import org.lwjgl.opengl.GL11;

import me.hippo.systems.lwjeb.annotation.Collect;
import me.hippo.systems.lwjeb.event.Stage;
import net.minecraft.entity.player.EntityPlayer;
import splash.api.module.Module;
import splash.api.module.category.ModuleCategory;
import splash.api.value.impl.BooleanValue;
import splash.client.events.render.EntityLivingRenderEvent;

public class Chams extends Module{

	public BooleanValue<Boolean> fill = new BooleanValue<Boolean>("Color Fill", true, this);
	public BooleanValue<Boolean> fillRainbow = new BooleanValue<Boolean>("Rainbow Fill", true, this);

	public Chams() {
		super("Chams", "What you think bro", ModuleCategory.VISUALS);
	}
	
	@Collect
	public void onRender(EntityLivingRenderEvent event) {
		if (event.getStage().equals(Stage.PRE) && event.getEntity() instanceof EntityPlayer) {
			GL11.glEnable(32823);
			GL11.glPolygonOffset(1.0f, -1100000.0f);
		} else if (event.getStage().equals(Stage.POST) && event.getEntity() instanceof EntityPlayer) {
            GL11.glDisable(32823);
            GL11.glPolygonOffset(1.0f, 1100000.0f);
		}
	}
}