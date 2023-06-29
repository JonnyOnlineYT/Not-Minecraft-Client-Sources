package org.moonware.client.feature.impl.player;

import net.minecraft.client.Minecraft;
import net.minecraft.init.MobEffects;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.player.EventBlockInteract;
import org.moonware.client.event.events.impl.player.EventUpdate;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.settings.impl.ListSetting;
import org.moonware.client.settings.impl.NumberSetting;

public class SpeedMine extends Feature {

    public ListSetting mode;
    public NumberSetting damageValue = new NumberSetting("Damage Value", 0.8F, 0.7F, 4, 0.1F, () -> mode.currentMode.equals("Damage"));

    public SpeedMine() {
        super("SpeedMine", "Позволяет быстро ломать блоки", Type.Other);
        mode = new ListSetting("SpeedMine Mode", "Packet", () -> true, "Packet", "Damage", "Potion");
        addSettings(mode, damageValue);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        setSuffix(mode.currentMode);
    }

    @EventTarget
    public void onBlockInteract(EventBlockInteract event) {
        switch (mode.currentMode) {
            case "Potion":
                Minecraft.player.addPotionEffect(new PotionEffect(MobEffects.HASTE, 817, 1));
                break;
            case "Damage":
                if (Minecraft.playerController.curBlockDamageMP >= 0.7) {
                    Minecraft.playerController.curBlockDamageMP = damageValue.getNumberValue();
                }
                break;
            case "Packet":
                Minecraft.player.swingArm(EnumHand.MAIN_HAND);
                Minecraft.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, event.getPos(), event.getFace()));
                Minecraft.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, event.getPos(), event.getFace()));
                event.setCancelled(true);
                break;
        }
    }

    @Override
    public void onDisable() {
        Minecraft.player.removePotionEffect(MobEffects.HASTE);
        super.onDisable();
    }
}