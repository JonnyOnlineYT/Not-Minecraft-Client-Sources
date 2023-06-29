package volcano.summer.client.modules.combat.aura;

import net.minecraft.client.Minecraft;
import volcano.summer.client.events.EventPostMotionUpdate;
import volcano.summer.client.events.EventPreMotionUpdate;

public abstract class AuraMode {

	public KillAura aura;
	protected Minecraft mc = Minecraft.getMinecraft();
	protected String name;

	public AuraMode(String name, KillAura aura) {
		this.aura = aura;
		this.name = name;
	}

	public abstract void onEnable();

	public abstract void onDisable();

	public abstract void pre(EventPreMotionUpdate e);

	public abstract void onPost(EventPostMotionUpdate e);

	public String getName() {
		return this.name;
	}
}
