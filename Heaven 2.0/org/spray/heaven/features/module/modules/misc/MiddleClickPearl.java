package org.spray.heaven.features.module.modules.misc;

import net.minecraft.init.Items;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.spray.heaven.features.module.Category;
import org.spray.heaven.features.module.Module;
import org.spray.heaven.features.module.ModuleInfo;
import org.spray.heaven.util.InvUtil;

@ModuleInfo(name = "MiddleClickPearl", category = Category.MISC, visible = true, key = Keyboard.KEY_NONE)
public class MiddleClickPearl extends Module {

	@Override
	public void onUpdate() {
		int pearlSlot = InvUtil.findItemFromHotbar(Items.ENDER_PEARL);
		if (Mouse.isButtonDown(2) && mc.currentScreen == null) {
			if (pearlSlot != -1) {
				mc.player.connection.sendPacket(new CPacketHeldItemChange(pearlSlot));
				mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
				mc.player.connection.sendPacket(new CPacketHeldItemChange(mc.player.inventory.currentItem));
			}
		}
	}

}
