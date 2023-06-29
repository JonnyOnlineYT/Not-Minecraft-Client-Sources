package ru.rockstar.client.features.impl.player;

import org.lwjgl.input.Mouse;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import ru.rockstar.api.event.EventTarget;
import ru.rockstar.api.event.event.EventMouseKey;
import ru.rockstar.api.event.event.EventUpdate;
import ru.rockstar.client.features.Category;
import ru.rockstar.client.features.Feature;

public class MiddleClickPearl extends Feature {

    public MiddleClickPearl() {
        super("MiddleClickPearl","Автоматически кидает эндер-перл при нажатии на колесо мыши", 0, Category.PLAYER);
    }


    @EventTarget
    public void onMouseEvent(EventMouseKey event) {
        if (event.getKey() == 2) {
            for (int i = 0; i < 9; i++) {
                ItemStack itemStack = mc.player.inventory.getStackInSlot(i);
                if (itemStack.getItem() == Items.ENDER_PEARL) {
                    mc.player.connection.sendPacket(new CPacketHeldItemChange(i));
                    mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
                    mc.player.connection.sendPacket(new CPacketHeldItemChange(mc.player.inventory.currentItem));
                }
            }
        }
    }

    @Override
    public void onDisable() {
        mc.player.connection.sendPacket(new CPacketHeldItemChange(mc.player.inventory.currentItem));
        super.onDisable();
    }
}
