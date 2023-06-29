package ru.rockstar.client.features.impl.player;

import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import ru.rockstar.api.event.EventTarget;
import ru.rockstar.api.event.event.EventMouse;
import ru.rockstar.client.features.Category;
import ru.rockstar.client.features.Feature;

public class AntiBlocker extends Feature
{
    public AntiBlocker() {
        super("AntiBlocker", "ѕозвл€ет кидать перлы и т.д. в верстаки, сундуки, печки, но при этом открывать их", 0, Category.PLAYER);
    }
    
    @EventTarget
    public void onMouse(final EventMouse event) {
        if (event.key == 1) {
            AntiBlocker.mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
        }
    }
}
