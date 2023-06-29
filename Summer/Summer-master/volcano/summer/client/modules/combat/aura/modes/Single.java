package volcano.summer.client.modules.combat.aura.modes;

import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import volcano.summer.base.Summer;
import volcano.summer.client.events.EventPostMotionUpdate;
import volcano.summer.client.events.EventPreMotionUpdate;
import volcano.summer.client.modules.combat.Criticals.AuraUtil;
import volcano.summer.client.modules.combat.aura.AuraMode;
import volcano.summer.client.modules.combat.aura.KillAura;
import volcano.summer.client.util.EntityUtils;

public class Single extends AuraMode {

	public Single(KillAura aura) {
		super("Single", aura);
	}

	@Override
	public void onEnable() {
		AuraUtil.reset();
	}

	@Override
	public void onDisable() {
		AuraUtil.reset();
	}

	@Override
	public void pre(EventPreMotionUpdate e) {
		float[] rotations = EntityUtils.getRotationsAura(this.aura.getCurrentEntity());
		e.yaw = rotations[0];
		e.pitch = rotations[1];
		if (aura.AutoBlock.getValue() && mc.thePlayer.getHeldItem() != null
				&& mc.thePlayer.getHeldItem().getItem() instanceof ItemSword && KillAura.isBlocking) {
			KillAura.isBlocking = false;
//			Summer.tellPlayer("a");
		}
	}

	@Override
	public void onPost(EventPostMotionUpdate e) {
		if (!AuraUtil.hasReach(this.aura.mil))
			return;
		this.aura.attack(this.aura.getCurrentEntity());
		AuraUtil.reset();
	}
}