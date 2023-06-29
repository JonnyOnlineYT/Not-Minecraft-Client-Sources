package org.moonware.client.feature.impl.combat;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemAppleGold;
import net.minecraft.item.ItemStack;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.player.EventUpdate;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.helpers.Helper;
import org.moonware.client.settings.impl.NumberSetting;

public class AutoGapple extends Feature {

    public static NumberSetting health;
    private boolean isActive;

    public AutoGapple() {
        super("AutoGApple", "Автоматически ест яблоко при опредленном здоровье", Type.Combat);
        health = new NumberSetting("Health Amount", 15, 1, 20, 1, () -> true);
        addSettings(health);
    }

    @EventTarget
    public void onUpdate(EventUpdate e) {
        setSuffix("" + (int) health.getNumberValue());
        if (Minecraft.player == null || Minecraft.world == null)
            return;
        if (isGoldenApple(Minecraft.player.getHeldItemOffhand()) && Minecraft.player.getHealth() <= health.getNumberValue()) {
            isActive = true;
            Minecraft.gameSettings.keyBindUseItem.pressed = true;
        } else if (isActive) {
            Minecraft.gameSettings.keyBindUseItem.pressed = false;
            isActive = false;
        }
    }

    private boolean isGoldenApple(ItemStack itemStack) {
        return (itemStack != null && !itemStack.isEmpty() && itemStack.getItem() instanceof ItemAppleGold);
    }
}
