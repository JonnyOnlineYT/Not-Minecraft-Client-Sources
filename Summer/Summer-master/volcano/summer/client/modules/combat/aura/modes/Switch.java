package volcano.summer.client.modules.combat.aura.modes;

import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import volcano.summer.client.events.EventPostMotionUpdate;
import volcano.summer.client.events.EventPreMotionUpdate;
import volcano.summer.client.modules.combat.Criticals.AuraUtil;
import volcano.summer.client.modules.combat.aura.AuraMode;
import volcano.summer.client.modules.combat.aura.KillAura;
import volcano.summer.client.util.EntityUtils;

public class Switch extends AuraMode {

	public static int current;

	public Switch(KillAura aura) {
		super("Switch", aura);
		current = 0;
	}

	@Override
	public void onEnable() {
		AuraUtil.reset();
		AuraUtil.reset();
		current = 0;
	}

	@Override
	public void onDisable() {
		AuraUtil.reset();
		AuraUtil.reset();
		current = 0;
	}

	@Override
	public void pre(EventPreMotionUpdate e) {
		this.sortEntityOrder();
		float[] rotations = EntityUtils.getRotations(aura.getCurrentEntity());
		e.yaw = rotations[0];
		e.pitch = rotations[1];
				                if (aura.AutoBlock.getValue() && mc.thePlayer.getHeldItem() != null
                        && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword && aura.isBlocking) {
                    KillAura.isBlocking = false;
                }
	}

	@Override
	public void onPost(EventPostMotionUpdate e) {
		if (!AuraUtil.hasReach(aura.mil))
			return;
		aura.attack(aura.getCurrentEntity());
			                if (aura.AutoBlock.getValue() && mc.thePlayer.getHeldItem() != null
                        && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword) {
							mc.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent((new C08PacketPlayerBlockPlacement(new BlockPos(Integer.MAX_VALUE,Integer.MAX_VALUE,Integer.MAX_VALUE), Integer.MAX_VALUE, mc.thePlayer.getCurrentEquippedItem(), 0, 0, 0)));
                    mc.thePlayer.setItemInUse(mc.thePlayer.getHeldItem(),
                            mc.thePlayer.getHeldItem().getMaxItemUseDuration());
                    KillAura.isBlocking = true;
                }
		AuraUtil.reset();
	}

	private void sortEntityOrder() {
		if (current >= aura.targets.size())
			current = 0;
		if (current == aura.targets.size())
			current = 0;
		if (aura.targets.size() == 1)
			current = 0;
		/*if (AuraUtil.hasReach(this.aura.switchdelay.getValue().floatValue())) {
			current++;
			if (current >= aura.targets.size())
				current = 0;
			if (current == aura.targets.size())
				current = 0;
			if (aura.targets.size() == 1)
				current = 0;
			AuraUtil.reset();
		}*/
	}
}