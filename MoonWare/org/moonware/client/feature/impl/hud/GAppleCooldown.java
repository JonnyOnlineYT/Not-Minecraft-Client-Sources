package org.moonware.client.feature.impl.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.init.Items;
import net.minecraft.item.ItemAppleGold;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.game.EventItemEat;
import org.moonware.client.event.events.impl.game.GAppleEatEvent;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;

public class GAppleCooldown extends Feature {

    public GAppleCooldown() {
        super("GAppleCooldown", "Меняет кулдаун геплам", Type.Hud);
        addSettings();
    }

    @EventTarget
    public void onEatGapple(GAppleEatEvent event) {
        if (Minecraft.player.getHeldItemOffhand().getItem() == Items.GOLDEN_APPLE) {
            event.setCancelled(true);
            Minecraft.gameSettings.keyBindUseItem.pressed = false;
        }
    }
    @EventTarget
    public void onEat(EventItemEat eventItemEat) {
        if (Minecraft.player.getHeldItemOffhand().getItem() == Items.GOLDEN_APPLE && Minecraft.player.getCooldownTracker().hasCooldown(Items.GOLDEN_APPLE)) {
            Minecraft.gameSettings.keyBindUseItem.pressed = false;
            eventItemEat.setCancelled(true);
        }
        if (eventItemEat.itemStack.getItem() instanceof ItemAppleGold) {
            Minecraft.player.getCooldownTracker().setCooldown(Items.GOLDEN_APPLE, 62);
        }
    }

}
