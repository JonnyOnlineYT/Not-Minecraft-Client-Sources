package ru.rockstar.client.features.impl.player;

import net.minecraft.init.MobEffects;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.*;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;
import ru.rockstar.api.event.EventTarget;
import ru.rockstar.api.event.event.EventBlockInteract;
import ru.rockstar.api.event.event.EventPreMotionUpdate;
import ru.rockstar.api.event.event.EventUpdate;
import ru.rockstar.api.utils.world.TimerHelper;
import ru.rockstar.client.features.Category;
import ru.rockstar.client.features.Feature;
import ru.rockstar.client.ui.settings.impl.ListSetting;
import ru.rockstar.client.ui.settings.impl.NumberSetting;

public class SpeedMine extends Feature {

    public ListSetting mode;
    public NumberSetting damageValue = new NumberSetting("Damage Value", 0.8F, 0.7F, 4, 0.1F, () -> mode.currentMode.equals("Damage"));

    public SpeedMine() {
        super("SpeedMine", "Ускоряет скорость ломания блоков",0, Category.PLAYER);
        mode = new ListSetting("SpeedMine Mode", "Packet", () -> true, "Packet", "Damage", "Potion");
        addSettings(mode, damageValue);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        this.setSuffix(mode.currentMode, true);
    }

    @EventTarget
    public void onBlockInteract(EventBlockInteract event) {
        switch (mode.currentMode) {
            case "Potion":
                mc.player.addPotionEffect(new PotionEffect(MobEffects.HASTE, 817, 1));
                break;
            case "Damage":
                if (mc.playerController.curBlockDamageMP >= 0.7) {
                    mc.playerController.curBlockDamageMP = damageValue.getNumberValue();
                }
                break;
            case "Packet":
                mc.player.swingArm(EnumHand.MAIN_HAND);
                mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, event.getPos(), event.getFace()));
                mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, event.getPos(), event.getFace()));
                event.setCancelled(true);
                break;
        }
    }

    @Override
    public void onDisable() {
        mc.player.removePotionEffect(MobEffects.HASTE);
        super.onDisable();
    }
}