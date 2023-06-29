package ru.rockstar.client.features.impl.movement;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import ru.rockstar.api.event.EventTarget;
import ru.rockstar.api.event.event.EventPostMotionUpdate;
import ru.rockstar.api.event.event.EventUpdate;
import ru.rockstar.client.features.Category;
import ru.rockstar.client.features.Feature;
import ru.rockstar.client.ui.settings.impl.BooleanSetting;
import ru.rockstar.client.ui.settings.impl.ListSetting;
import ru.rockstar.client.ui.settings.impl.NumberSetting;

public class NoSlowDown extends Feature {
    private boolean isNotJump;
    public static NumberSetting percentage;
    public static ListSetting noSlowMode;
    public static int usingTicks;

    public NoSlowDown() {
        super("NoSlowDown", "Убирает замедление при использовании еды и других предметов", 0, Category.MOVEMENT);
        percentage = new NumberSetting("Percentage", 100.0f, 0.0f, 150.0f, 1.0f, () -> true);
        noSlowMode = new ListSetting("NoSlow Mode", "Default", () -> true, "Default", "Matrix", "AAC");
        this.addSettings(noSlowMode, percentage);
    }


    public void onDisable() {
        NoSlowDown.mc.timer.timerSpeed = 1.0f;
        super.onDisable();
    }

    @EventTarget
    public void onPlayerState(final EventPostMotionUpdate event) {
        if (!NoSlowDown.mc.player.isUsingItem()) {
            return;
        }
        if (NoSlowDown.noSlowMode.currentMode.equals("AAC")) {
            NoSlowDown.mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
        }
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        this.setModuleName("NoSlowDown " + TextFormatting.GRAY + noSlowMode.getCurrentMode());
        usingTicks = mc.player.isUsingItem() ? ++usingTicks : 0;
        if (!this.isToggled() || !mc.player.isUsingItem()) {
            return;
        }
        if (NoSlowDown.noSlowMode.currentMode.equals("Matrix")) {
            if (mc.player.isUsingItem()) {
                if (mc.player.onGround && !mc.gameSettings.keyBindJump.isKeyDown()) {
                    if (mc.player.ticksExisted % 2 == 0) {
                        mc.player.motionX *= 0.46;
                        mc.player.motionZ *= 0.46;
                    }
                } else if ((double) mc.player.fallDistance > 0.2) {
                    mc.player.motionX *= 0.9100000262260437;
                    mc.player.motionZ *= 0.9100000262260437;
                }
            }
        }
    }
}