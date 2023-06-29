package org.moonware.client.feature.impl.movement;

import net.minecraft.client.Minecraft;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.network.play.client.CPacketEntityAction;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.player.EventPreMotion;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.helpers.player.MovementHelper;
import org.moonware.client.ui.shader.notification.NotificationManager;
import org.moonware.client.ui.shader.notification.NotificationType;

/*    */ public class ElytraStrafe extends Feature {
    /*    */   public ElytraStrafe() {
        /* 16 */     super("ElytraStrafe", "Позволяет стрейфить как ебанутый на элитрах", Type.Movement);
        /*    */   }
    /*    */
    /*    */   @EventTarget
    /*    */   public void onPreMotion(EventPreMotion eventPreMotion) {
        /* 21 */     if (Minecraft.player.onGround) {
            /*    */       return;
            /*    */     }
        /* 24 */     int eIndex = -1;
        /* 25 */     for (int i = 0; i < 45; i++) {
            /* 26 */       if (Minecraft.player.inventory.getStackInSlot(i).getItem() == Items.ELYTRA && eIndex == -1) {
                /* 27 */         eIndex = i;
                /*    */       }
            /*    */     }
        /* 30 */     if (Minecraft.player.ticksExisted % 7 == 0) {
            /* 31 */       Minecraft.playerController.windowClick(0, eIndex, 0, ClickType.PICKUP, Minecraft.player);
            /* 32 */       Minecraft.playerController.windowClick(0, 6, 1, ClickType.PICKUP, Minecraft.player);
            /*    */     }
        /* 34 */     if (!Minecraft.player.onGround) {
            /* 35 */       MovementHelper.strafePlayer();
            /*    */     }
        /* 37 */     if (Minecraft.player.ticksExisted % 7 == 0) {
            /* 38 */       Minecraft.player.motionX *= 0.8D;
            /* 39 */       Minecraft.player.motionZ *= 0.8D;
            /* 40 */       Minecraft.player.connection.sendPacket(new CPacketEntityAction(Minecraft.player, CPacketEntityAction.Action.START_FALL_FLYING));
            /*    */     }
        /* 42 */     Minecraft.player.motionX *= 1.1D;
        /* 43 */     Minecraft.player.motionZ *= 1.1D;
        /* 44 */     if (Minecraft.player.ticksExisted % 7 == 0) {
            /* 45 */       Minecraft.playerController.windowClick(0, 6, 0, ClickType.PICKUP, Minecraft.player);
            /* 46 */       Minecraft.playerController.windowClick(0, eIndex, 1, ClickType.PICKUP, Minecraft.player);
            /*    */     }
        /*    */
        /* 49 */     if (eIndex == -1 &&
                /* 50 */       Minecraft.player.getHeldItemOffhand().getItem() != Items.ELYTRA) {
            /* 51 */       NotificationManager.publicity("§6Matrix Exploit", "§cВозьмите элитры в инвентарь!", 6, NotificationType.WARNING);
            /* 52 */       toggle();
            /*    */     }
        /*    */   }
    /*    */ }