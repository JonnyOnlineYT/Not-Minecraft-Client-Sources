package org.spray.heaven.features.module.modules.combat;

import java.util.Arrays;

import org.lwjgl.input.Keyboard;
import org.spray.heaven.events.PacketEvent;
import org.spray.heaven.features.module.Category;
import org.spray.heaven.features.module.Module;
import org.spray.heaven.features.module.ModuleInfo;
import org.spray.heaven.features.module.Setting;
import org.spray.heaven.main.Wrapper;

import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.types.EventType;

import net.minecraft.init.Items;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerDigging.Action;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

@ModuleInfo(name = "SuperBow", category = Category.COMBAT, visible = true, key = Keyboard.KEY_NONE)
public class SuperBow extends Module {

	private final Setting mode = register(new Setting("Observer", "Packet", Arrays.asList("Packet", "Charge")));
	private final Setting bowCharge = register(
			new Setting("Bow Charge", 18, 3, 20).setVisible(() -> mode.getCurrentMode().equalsIgnoreCase("Charge")));
	private final Setting damageLevel = register(new Setting("Damage Level", 100, 10, 150));
	private final Setting reverseGround = register(new Setting("Reverse Ground", false));
	private long lastShootTime;

	@Override
	public void onEnable() {
		lastShootTime = System.currentTimeMillis();
	}

	@Override
	public void onUpdate() {
		setSuffix(mode.getCurrentMode());
		if (mode.getCurrentMode().equalsIgnoreCase("Charge")) {
			if (mc.player.isHandActive() && mc.player.getActiveItemStack().getItem() == Items.BOW) {

				if (mc.player.getItemInUseMaxCount() >= bowCharge.getValue()) {
					doSpoofs();
					mc.player.connection
							.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SPRINTING));
					Wrapper.sendPacket(new CPacketPlayerDigging(Action.RELEASE_USE_ITEM, BlockPos.ORIGIN,
							mc.player.getHorizontalFacing()));
					Wrapper.sendPacket(new CPacketPlayerTryUseItem(mc.player.getActiveHand()));
					mc.player.stopActiveHand();
				}
			}
		}
	}

	@EventTarget
	public void onPacketSend(PacketEvent event) {
		if (mode.getCurrentMode().equalsIgnoreCase("Packet")) {
			if (!event.getType().equals(EventType.SEND))
				return;

			if (event.getPacket() instanceof CPacketPlayerDigging) {
				CPacketPlayerDigging packet = (CPacketPlayerDigging) event.getPacket();
				if (packet.getAction() == CPacketPlayerDigging.Action.RELEASE_USE_ITEM) {
					ItemStack handStack = mc.player.getHeldItem(EnumHand.MAIN_HAND);
					if (!handStack.isEmpty()) {
						handStack.getItem();
						if (handStack.getItem() instanceof ItemBow && mc.player.getFoodStats().getFoodLevel() > 1)
							doSpoofs();
					}
				}
			}
		}
	}

	private void doSpoofs() {
		if (System.currentTimeMillis() - lastShootTime >= 0) {
			lastShootTime = System.currentTimeMillis();
			mc.player.connection
					.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SPRINTING));
			for (int i = 0; i < damageLevel.getValue(); ++i) {
				mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.0E-10D,
						mc.player.posZ, reverseGround.isToggle() ? true : false));
				mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY - 1.0E-10D,
						mc.player.posZ, reverseGround.isToggle() ? false : true));
			}
		}
	}

}
