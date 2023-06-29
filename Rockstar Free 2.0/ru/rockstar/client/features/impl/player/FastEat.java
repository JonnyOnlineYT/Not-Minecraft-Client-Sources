package ru.rockstar.client.features.impl.player;

import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import ru.rockstar.api.event.EventTarget;
import ru.rockstar.api.event.event.EventPreMotion;
import ru.rockstar.client.features.Category;
import ru.rockstar.client.features.Feature;
import ru.rockstar.client.ui.settings.impl.ListSetting;

public class FastEat extends Feature
{
    private final ListSetting modeFastEat;
    
    public FastEat() {
        super("FastEat", "\u041f\u043e\u0437\u0432\u043e\u043b\u044f\u0435\u0442 \u0431\u044b\u0441\u0442\u0440\u043e \u0438\u0441\u043f\u043e\u043b\u044c\u0437\u043e\u0432\u0430\u0442\u044c \u0435\u0434\u0443", 0, Category.PLAYER);
        this.modeFastEat = new ListSetting("FastEat Mode", "Matrix", () -> true, new String[] { "Matrix", "Vanilla" });
        this.addSettings(this.modeFastEat);
    }
    
    @EventTarget
    public void onUpdate(final EventPreMotion event) {
        final String mode = this.modeFastEat.getOptions();
        this.setSuffix(mode, true);
        if (mode.equalsIgnoreCase("Matrix")) {
            if (FastEat.mc.player.getItemInUseMaxCount() >= 12 && (FastEat.mc.player.isEating() || FastEat.mc.player.isDrinking())) {
                for (int i = 0; i < 10; ++i) {
                    FastEat.mc.player.connection.sendPacket(new CPacketPlayer(FastEat.mc.player.onGround));
                }
                FastEat.mc.player.stopActiveHand();
            }
        }
        else if (mode.equalsIgnoreCase("Vanilla") && FastEat.mc.player.getItemInUseMaxCount() == 16 && (FastEat.mc.player.isEating() || FastEat.mc.player.isDrinking())) {
            for (int i = 0; i < 21; ++i) {
                FastEat.mc.player.connection.sendPacket(new CPacketPlayer(true));
            }
            FastEat.mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
        }
    }
    
    @Override
    public void onDisable() {
        FastEat.mc.timer.timerSpeed = 1.0f;
        super.onDisable();
    }
}
