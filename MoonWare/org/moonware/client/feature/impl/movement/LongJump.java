package org.moonware.client.feature.impl.movement;

import net.minecraft.client.Minecraft;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.player.EventPreMotion;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.settings.impl.BooleanSetting;
import org.moonware.client.settings.impl.ListSetting;
import org.moonware.client.settings.impl.NumberSetting;

public class LongJump extends Feature {

    public ListSetting mode;
    public NumberSetting boostMultiplier;
    public NumberSetting motionBoost;
    public BooleanSetting motionYBoost = new BooleanSetting("MotionY boost", false, () -> true);

    public LongJump() {
        super("Длинный прыжок", "Позволяет прыгать на большую длинну", Type.Movement);
        mode = new ListSetting("LongJump Mode", "Reallyworld Pearle", () -> true, "Redesky", "Reallyworld Pearle");
        boostMultiplier = new NumberSetting("Boost Speed", 0.3F, 0.1F, 15F, 0.1F, () -> mode.currentMode.equals("Reallyworld Pearle"));
        motionBoost = new NumberSetting("Motion Boost", 0.6F, 0.1F, 8F, 0.1F, () -> mode.currentMode.equals("Reallyworld Pearle") && motionYBoost.getBoolValue());
        addSettings(mode, boostMultiplier, motionYBoost, motionBoost);
    }

    @EventTarget
    public void onPreUpdate(EventPreMotion event) {
        String longMode = mode.getOptions();
        setSuffix(longMode);
        if (!getState())
            return;
        if (longMode.equalsIgnoreCase("Redesky")) {
            if (Minecraft.player.hurtTime > 0) {
                Minecraft.timer.timerSpeed = 1F;
                if (Minecraft.player.fallDistance != 0.0f) {
                    Minecraft.player.motionY += 0.039;
                }
                if (Minecraft.player.onGround) {
                    Minecraft.player.jump();
                } else {
                    Minecraft.timer.timerSpeed = 0.2f;
                    Minecraft.player.motionY += 0.075;
                    Minecraft.player.motionX *= 1.065f;
                    Minecraft.player.motionZ *= 1.065f;
                }
            }
        } else if (longMode.equalsIgnoreCase("Reallyworld Pearle")) {

        }
    }


    @EventTarget
    public void onPreMotion(EventPreMotion event) {
        if (mode.currentMode.equalsIgnoreCase("Reallyworld Pearle")) {
            for (int i = 0; i < 9; i++) {
                event.setPitch(90);
                Minecraft.player.rotationPitchHead = 90;
                ItemStack itemStack = Minecraft.player.inventory.getStackInSlot(i);
                if (itemStack.getItem() == Items.ENDER_PEARL) {
                    Minecraft.player.rotationPitchHead = 90;
                    event.setPitch(90);
                    if (Minecraft.player.ticksExisted % 2 == 0) {
                        Minecraft.player.connection.sendPacket(new CPacketHeldItemChange(i));
                        Minecraft.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
                        Minecraft.player.connection.sendPacket(new CPacketHeldItemChange(Minecraft.player.inventory.currentItem));
                    }

                }
                if (Minecraft.player.hurtTime > 0) {
                    Minecraft.player.isAirBorne = true;
                    if (motionYBoost.getBoolValue()) {
                        Minecraft.player.motionY = motionBoost.getNumberValue();
                    }
                    Minecraft.player.jumpMovementFactor = boostMultiplier.getNumberValue();
                }
                if (Minecraft.player.hurtTime >= 0.1f && Minecraft.player.hurtTime <= 0.11f) {
                    toggle();
                }
            }
        }
    }
    @Override
    public void onDisable() {
        Minecraft.timer.timerSpeed = 1;
        Minecraft.player.connection.sendPacket(new CPacketHeldItemChange(Minecraft.player.inventory.currentItem));
        super.onDisable();
    }
}
