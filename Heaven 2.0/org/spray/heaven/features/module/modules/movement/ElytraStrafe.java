package org.spray.heaven.features.module.modules.movement;

import org.lwjgl.input.Keyboard;
import org.spray.heaven.events.MotionEvent;
import org.spray.heaven.features.module.Category;
import org.spray.heaven.features.module.Module;
import org.spray.heaven.features.module.ModuleInfo;
import org.spray.heaven.main.Wrapper;
import org.spray.heaven.notifications.Notification.Type;
import org.spray.heaven.util.MovementUtil;

import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.types.EventType;
import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.network.play.client.CPacketEntityAction;

@ModuleInfo(name = "ElytraStrafe", category = Category.MOVEMENT, visible = true, key = Keyboard.KEY_NONE)
public class ElytraStrafe extends Module {

	@EventTarget
	public void onMotion(MotionEvent event) {
		if (event.getType().equals(EventType.PRE)) {
			if (mc.player.onGround) {
				return;
			}
			int eIndex = -1;
			for (int i = 0; i < 45; i++) {
				if (mc.player.inventory.getStackInSlot(i).getItem() == Items.ELYTRA) {
					eIndex = i;
					break;
				}
			}
			
			if (eIndex == -1) {
				if (mc.player.getHeldItemOffhand().getItem() != Items.ELYTRA) {
					Wrapper.notify(ChatFormatting.RED + "Возьмите элитры в инвентарь!", Type.WARNING);
					disable();
					return;
				}
			}
			
			if (mc.player.ticksExisted % 4 == 0) {
				mc.playerController.windowClick(0, eIndex, 0, ClickType.PICKUP, mc.player);
				mc.playerController.windowClick(0, 6, 1, ClickType.PICKUP, mc.player);
			}
			if (!mc.player.onGround) {
				MovementUtil.setMotion(MovementUtil.getSqrtSpeed());
			}

			if (mc.player.ticksExisted % 4 == 0) {
				mc.player.motionX *= 0.8D;
				mc.player.motionZ *= 0.8D;
				mc.player.connection
						.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
			}
			mc.player.motionX *= 1.1D;
			mc.player.motionZ *= 1.1D;
			if (mc.player.ticksExisted % 4 == 0) {
				mc.playerController.windowClick(0, 6, 0, ClickType.PICKUP, mc.player);
				mc.playerController.windowClick(0, eIndex, 1, ClickType.PICKUP, mc.player);
			}
		}
	}

}
