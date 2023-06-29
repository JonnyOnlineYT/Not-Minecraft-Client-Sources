package ru.rockstar.client.features.impl.movement;

import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.network.play.client.CPacketEntityAction;
import ru.rockstar.api.event.EventTarget;
import ru.rockstar.api.event.event.EventPreMotion;
import ru.rockstar.api.utils.movement.MovementHelper;
import ru.rockstar.api.utils.notifications.NotificationPublisher;
import ru.rockstar.api.utils.notifications.NotificationType;
import ru.rockstar.client.features.Category;

public class ElytraStrafe extends ru.rockstar.client.features.Feature {
    public ElytraStrafe() {
        super("ElytraStrafe", "Позволяет стрейфить с элитрами в инвентаре", 0, Category.MOVEMENT);
    }

    @EventTarget
    public void onPreMotion(EventPreMotion eventPreMotion) {
        if (mc.player.onGround) {
            return;
        }
        int eIndex = -1;
        for (int i = 0; i < 45; i++) {
            if (mc.player.inventory.getStackInSlot(i).getItem() == Items.ELYTRA && eIndex == -1) {
                eIndex = i;
            }
        }
        if (mc.player.ticksExisted % 7 == 0) {
            mc.playerController.windowClick(0, eIndex, 0, ClickType.PICKUP, mc.player);
            mc.playerController.windowClick(0, 6, 1, ClickType.PICKUP, mc.player);
        }
        if (!mc.player.onGround) {
            MovementHelper.strafe();
        }
        if (mc.player.ticksExisted % 7 == 0) {
            mc.player.motionX *= 0.8D;
            mc.player.motionZ *= 0.8D;
            mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
        }
        mc.player.motionX *= 1.1D;
        mc.player.motionZ *= 1.1D;
        if (mc.player.ticksExisted % 7 == 0) {
            mc.playerController.windowClick(0, 6, 0, ClickType.PICKUP, mc.player);
            mc.playerController.windowClick(0, eIndex, 1, ClickType.PICKUP, mc.player);
        }

        if (eIndex == -1) {
            if (mc.player.getHeldItemOffhand().getItem() != Items.ELYTRA) {
                NotificationPublisher.queue("§6Matrix Exploit", "§cВозьмите элитры в инвентарь!", NotificationType.WARNING);
                toggle();
            }
        }
    }

}
