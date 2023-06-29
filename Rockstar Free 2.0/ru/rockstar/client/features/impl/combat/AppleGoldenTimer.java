package ru.rockstar.client.features.impl.combat;

import net.minecraft.client.gui.GuiBossOverlay;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.Items;
import net.minecraft.item.ItemAppleGold;
import net.minecraft.item.ItemStack;
import ru.rockstar.api.event.EventTarget;
import ru.rockstar.api.event.event.EventPreMotion;
import ru.rockstar.api.event.event.EventPreMotionUpdate;
import ru.rockstar.client.features.Category;
import ru.rockstar.client.features.Feature;

import org.lwjgl.input.Mouse;

import java.awt.*;

public class AppleGoldenTimer extends Feature {
    public static boolean cooldown;
    private boolean isEated;

    public AppleGoldenTimer() {
        super("GAppleTimer", "Добавляет задержку на поедание гепла", 0, Category.COMBAT);
    }

    @EventTarget
    public void onUpdate(EventPreMotionUpdate eventUpdate) {
    	if (!GuiBossOverlay.pot) {
    		return;
    	}
        if (mc.player.getHeldItemOffhand().isOnFinish() || mc.player.getHeldItemMainhand().isOnFinish() && mc.player.getActiveItemStack().getItem() == Items.GOLDEN_APPLE) {
            isEated = true;
        }
        if (isEated) {
            mc.player.getCooldownTracker().setCooldown(Items.GOLDEN_APPLE, 55);
            isEated = false;
        }
        if (mc.player.getCooldownTracker().hasCooldown(Items.GOLDEN_APPLE) && mc.player.getActiveItemStack().getItem() == Items.GOLDEN_APPLE) {
            mc.gameSettings.keyBindUseItem.setPressed(false);
        } else if (Mouse.isButtonDown(1) && !(mc.currentScreen instanceof GuiContainer)) {
            mc.gameSettings.keyBindUseItem.setPressed(true);
        }
    }

    private boolean isGoldenApple(ItemStack itemStack) {
        return (itemStack != null && !itemStack.func_190926_b() && itemStack.getItem() instanceof ItemAppleGold);
    }
}
