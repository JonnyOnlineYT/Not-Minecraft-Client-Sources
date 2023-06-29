package org.moonware.client.feature.impl.player;

import net.minecraft.client.Minecraft;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.input.EventMouse;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;

public class MiddleClickPearl extends Feature {

    public MiddleClickPearl() {
        super("MiddleClickPearl", "Автоматически кидает эндер-перл при нажатии на колесо мыши", Type.Other);
    }

    @EventTarget
    public void onMouseEvent(EventMouse event) {
        if (event.getKey() == 2) {
            for (int i = 0; i < 9; i++) {
                ItemStack itemStack = Minecraft.player.inventory.getStackInSlot(i);
                if (itemStack.getItem() == Items.ENDER_PEARL) {
                    Minecraft.player.connection.sendPacket(new CPacketHeldItemChange(i));
                    Minecraft.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
                    Minecraft.player.connection.sendPacket(new CPacketHeldItemChange(Minecraft.player.inventory.currentItem));
                }
            }
        }
    }

    @Override
    public void onDisable() {
        Minecraft.player.connection.sendPacket(new CPacketHeldItemChange(Minecraft.player.inventory.currentItem));
        super.onDisable();
    }
}
