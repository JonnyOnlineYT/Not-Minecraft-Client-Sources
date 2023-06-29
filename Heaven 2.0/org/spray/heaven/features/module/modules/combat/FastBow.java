package org.spray.heaven.features.module.modules.combat;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemBow;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.input.Keyboard;

import org.spray.heaven.events.UpdateEvent;
import org.spray.heaven.features.module.Category;
import org.spray.heaven.features.module.Module;
import org.spray.heaven.features.module.ModuleInfo;
import org.spray.heaven.features.module.Setting;

@ModuleInfo(name = "FastBow", category = Category.COMBAT, visible = true, key = Keyboard.KEY_NONE)
public class FastBow extends Module {
	
    private Setting ticks = register(new Setting("Range", 1.5, 1.5, 10));
    
    @EventTarget
    public void onSendPacket(UpdateEvent event) {

        if (mc.player.inventory.getCurrentItem().getItem() instanceof ItemBow && mc.player.isHandActive() && mc.player.getActiveItemStack().getItem().getItemUseAction(mc.player.getActiveItemStack()) == EnumAction.BOW && mc.player.getItemInUseMaxCount() >= ticks.getValue()) {
            mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, mc.player.getHorizontalFacing()));
            mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
            mc.player.stopActiveHand();
        }
    }
}
