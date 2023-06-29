package volcano.summer.client.modules.combat.aura.modes;

import java.util.Random;

import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import volcano.summer.client.events.EventPostMotionUpdate;
import volcano.summer.client.events.EventPreMotionUpdate;
import volcano.summer.client.modules.combat.Criticals.AuraUtil;
import volcano.summer.client.modules.combat.aura.AuraMode;
import volcano.summer.client.modules.combat.aura.KillAura;
import volcano.summer.client.util.EntityUtils;

public class Tick extends AuraMode {

	public Tick(KillAura aura) {
		super("Tick", aura);
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
		e.pitch = rotations[1] - (new Random().nextInt(8) + 1);
				                if (aura.AutoBlock.getValue() && mc.thePlayer.getHeldItem() != null
                        && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword && KillAura.isBlocking) {
                    KillAura.isBlocking = false;
                }
	}

	@Override
	public void onPost(EventPostMotionUpdate e) {
		if (!AuraUtil.hasReach(470))
			return;
			mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(
					C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.fromAngle(e.yaw)));
		aura.swap(9, mc.thePlayer.inventory.currentItem);
		aura.dura(aura.getCurrentEntity(), false);
		aura.dura(aura.getCurrentEntity(), false);
		aura.dura(aura.getCurrentEntity(), true);
		aura.swap(9, mc.thePlayer.inventory.currentItem);
		aura.dura(aura.getCurrentEntity(), false);
		aura.dura(aura.getCurrentEntity(), true);
		if (aura.AutoBlock.getValue() && mc.thePlayer.getHeldItem() != null
                        && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword) {
							mc.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent((new C08PacketPlayerBlockPlacement(new BlockPos(Integer.MAX_VALUE,Integer.MAX_VALUE,Integer.MAX_VALUE), Integer.MAX_VALUE, mc.thePlayer.getCurrentEquippedItem(), 0, 0, 0)));
                    mc.thePlayer.setItemInUse(mc.thePlayer.getHeldItem(),
                            mc.thePlayer.getHeldItem().getMaxItemUseDuration());
                    KillAura.isBlocking = true;
                }
		AuraUtil.reset();
	}
}