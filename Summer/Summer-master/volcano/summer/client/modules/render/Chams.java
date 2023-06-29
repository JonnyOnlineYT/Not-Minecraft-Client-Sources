package volcano.summer.client.modules.render;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import volcano.summer.base.manager.event.Event;
import volcano.summer.base.manager.module.Module;
import volcano.summer.client.value.Value;

public class Chams extends Module {

	public static Value<Boolean> player;
	public static Value<Boolean> mob;

	public Chams() {
		super("Chams", 0, Category.RENDER);
		mob = new Value("Mob", "mob", Boolean.valueOf(false), this);
		player = new Value("Player", "player", Boolean.valueOf(true), this);
	}

	@Override
	public void onEnable() {
	}

	@Override
	public void onDisable() {
	}

	@Override
	public void onEvent(Event event) {

	}

	public static boolean isCorrect(EntityLivingBase entity) {
		if ((player.getValue().booleanValue()) && ((entity instanceof EntityPlayer)) && (entity != mc.thePlayer)) {
			return true;
		}
		if ((mob.getValue().booleanValue()) && ((entity instanceof EntityLivingBase))
				&& (!(entity instanceof EntityPlayer))) {
			return true;
		}
		return false;
	}
}