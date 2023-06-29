package Celestial.module.impl.Player;

import Celestial.event.EventTarget;
import Celestial.event.events.impl.input.EventMouse;
import Celestial.module.Module;
import Celestial.module.ModuleCategory;
import Celestial.utils.Helper;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;

public class MiddleClickPearl extends Module {

    public MiddleClickPearl() {
        super("Click Pearl","Позволяет кидать эндер-жемчуг с помощью колесика мыши", ModuleCategory.Player);
    }


    @EventTarget
    public void onMouseEvent(EventMouse event) {
        if (event.getKey() == 2) {
            for (int i = 0; i < 9; i++) {
                ItemStack itemStack = Helper.mc.player.inventory.getStackInSlot(i);
                if (itemStack.getItem() == Items.ENDER_PEARL) {
                    Helper.mc.player.connection.sendPacket(new CPacketHeldItemChange(i));
                    Helper.mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
                    Helper.mc.player.connection.sendPacket(new CPacketHeldItemChange(Helper.mc.player.inventory.currentItem));
                }
            }
        }
    }

    @Override
    public void onDisable() {
        Helper.mc.player.connection.sendPacket(new CPacketHeldItemChange(Helper.mc.player.inventory.currentItem));
        super.onDisable();
    }
}
