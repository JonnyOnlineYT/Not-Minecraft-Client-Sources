package org.moonware.client.feature.impl.player;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.player.EventPreMotion;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.settings.impl.ListSetting;

public class FastEat extends Feature {

    private final ListSetting modeFastEat;

    public FastEat() {
        super("FastEat", "Позволяет быстро использовать еду", Type.Other);
        modeFastEat = new ListSetting("FastEat Mode", "Matrix", () -> true, "Matrix", "Vanilla");
        addSettings(modeFastEat);
    }

    @EventTarget
    public void onUpdate(EventPreMotion event) {
        String mode = modeFastEat.getOptions();
        setSuffix(mode);
        if (mode.equalsIgnoreCase("Matrix")) {
            if (Minecraft.player.getItemInUseMaxCount() >= 12 && (Minecraft.player.isEating() || Minecraft.player.isDrinking())) {
                for (int i = 0; i < 10; i++) {
                    Minecraft.player.connection.sendPacket(new CPacketPlayer(Minecraft.player.onGround));
                }
                Minecraft.player.stopActiveHand();
            }
        } else if (mode.equalsIgnoreCase("Vanilla")) {
            if (Minecraft.player.getItemInUseMaxCount() == 16 && (Minecraft.player.isEating() || Minecraft.player.isDrinking())) {
                for (int i = 0; i < 21; ++i) {
                    Minecraft.player.connection.sendPacket(new CPacketPlayer(true));
                }
                Minecraft.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
            }
        }
    }

    @Override
    public void onDisable() {
        Minecraft.timer.timerSpeed = 1f;
        super.onDisable();
    }
}
