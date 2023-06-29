/* 3eLeHyy#0089 */

package org.moonware.client.feature.impl.misc;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Slot;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;
import net.minecraft.util.EnumHand;
import org.moonware.client.utils.MWUtils;
import org.moonware.client.MoonWare;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.packet.EventSendPacket;
import org.moonware.client.event.events.impl.player.EventPreMotionUpdate;
import org.moonware.client.event.events.impl.player.EventUpdate;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.feature.impl.movement.Speed;
import org.moonware.client.helpers.Helper;
import org.moonware.client.helpers.misc.TimerHelper;
import org.moonware.client.settings.impl.BooleanSetting;
import org.moonware.client.settings.impl.ListSetting;
import org.moonware.client.settings.impl.NumberSetting;
import org.moonware.client.ui.shader.notification.NotificationManager;
import org.moonware.client.ui.shader.notification.NotificationType;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Disabler
        extends Feature {
    private final List<Packet<?>> packets = new CopyOnWriteArrayList();
    private final TimerHelper timerHelper = new TimerHelper();
    public static ListSetting mode = new ListSetting("Disabler Mode", "Old MatrixVl", () -> true, "Old MatrixVl", "PingSpoof","MatrixElytra", "StormMovement", "Sunrise New", "Sunrise Old", "Mineplex Combat");
    private final NumberSetting delay = new NumberSetting("Delay", 5000.0f, 1000.0f, 20000.0f, 1000.0f, () -> mode.currentMode.equals("PingSpoof"));
    private final BooleanSetting noInventory = new BooleanSetting("No Inventory", true, () -> mode.currentMode.equals("PingSpoof"));

    public Disabler() {
        super("Disabler", "\u041e\u0441\u043b\u0430\u0431\u043b\u044f\u0435\u0442 \u0432\u043e\u0437\u0434\u0435\u0439\u0441\u0442\u0432\u0438\u0435 \u0430\u043d\u0442\u0438\u0447\u0438\u0442\u043e\u0432 \u043d\u0430 \u0432\u0430\u0441", Type.Other);
        addSettings(mode, delay, noInventory);
    }

    public static void init() {
    }

    @EventTarget
    public void onSendPacket1(EventSendPacket event) {
        if (!mode.currentMode.equals("PingSpoof")) {
            return;
        }
        if (Minecraft.screen instanceof GuiContainer && noInventory.getCurrentValue()) {
            return;
        }
        if (Helper.mc.isSingleplayer()) {
            return;
        }
        if (event.getPacket() instanceof CPacketKeepAlive) {
            if (packets.contains(event.getPacket())) {
                return;
            }
            event.setCancelled(true);
            packets.add(event.getPacket());
        }
        if (event.getPacket() instanceof CPacketConfirmTransaction) {
            if (packets.contains(event.getPacket())) {
                return;
            }
            event.setCancelled(true);
            packets.add(event.getPacket());
        }
    }

    @EventTarget
    public void onUpdate1(EventUpdate event) {
        if (!mode.currentMode.equals("PingSpoof")) {
            return;
        }
        if (timerHelper.hasReached(delay.getCurrentValue())) {
            for (Packet<?> packet : packets) {
                if (!(packet instanceof CPacketKeepAlive) && !(packet instanceof CPacketConfirmTransaction)) continue;
                Minecraft.player.connection.sendPacket(packet);
            }
            packets.clear();
            timerHelper.reset();
        }
    }


    public static boolean getHavePlayerElytra() {
            for (Slot slot :  Minecraft.player.inventoryContainer.inventorySlots){
                if ( slot.getStack().getItem () ==  Items.ELYTRA ){
                    return true;
                }
            }
            return false;
    }
    @Override
    public void onEnable() {
        if (mode.currentMode.equals("Sunrise New")) {
            for (int i = 0; i < 5099; ++i) {
                Minecraft.player.connection.sendPacket(new CPacketVehicleMove(Minecraft.player));
            }
            NotificationManager.publicity("Disabler", "Disabler was activated! :)", 8, NotificationType.SUCCESS);
        }
        super.onEnable();
    }
    @EventTarget
    public void onUpdate(EventUpdate event) {
        setSuffix(mode.currentMode);
        if (mode.currentMode.equals("Sunrise Old")) {
            Minecraft.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
        }
        if (mode.currentMode.equals("MatrixElytra")) {
            if (Minecraft.player.ticksExisted % 10F == 0) {
                for (Slot slot :  Minecraft.player.inventoryContainer.inventorySlots){
                    if ( slot.getStack().getItem () ==  Items.ELYTRA ){
                        Minecraft. playerController. windowClick ( 0 ,  slot . slotNumber ,  0 ,  ClickType. PICKUP , Minecraft. player);
                        Minecraft. playerController. windowClick ( 0 ,  6 ,  0 ,  ClickType . PICKUP , Minecraft. player);
                        Minecraft. player. connection . sendPacket ( new   CPacketEntityAction (Minecraft. player,  CPacketEntityAction . Action . START_FALL_FLYING ));
                        Minecraft. playerController. windowClick ( 0 ,  6 ,  0 ,  ClickType . PICKUP , Minecraft. player);
                        Minecraft. playerController. windowClick ( 0 ,  slot . slotNumber ,  0 ,  ClickType . PICKUP , Minecraft. player);
                        Minecraft. playerController. updateController ();
                        return;
                    }
                }
            }
        }
        if (timerHelper.hasReached(5000.0f) && mode.currentMode.equals("Old MatrixVl")) {
            for (Packet<?> packet : packets) {
                if (!(packet instanceof CPacketKeepAlive) && !(packet instanceof CPacketConfirmTransaction)) continue;
                Minecraft.player.connection.sendPacket(packet);
            }
            packets.clear();
            timerHelper.reset();
        }
    }

    @EventTarget
    public void onSendPacket(EventSendPacket event) {
        if (mode.currentMode.equals("Old MatrixVl")) {
            if (Minecraft.screen instanceof GuiContainer && !MoonWare.featureManager.getFeatureByClass(Speed.class).getState()) {
                return;
            }
            if (Helper.mc.isSingleplayer()) {
                return;
            }
            if (event.getPacket() instanceof CPacketKeepAlive) {
                if (packets.contains(event.getPacket())) {
                    return;
                }
                event.setCancelled(true);
                packets.add(event.getPacket());
            }
            if (event.getPacket() instanceof CPacketConfirmTransaction) {
                if (packets.contains(event.getPacket())) {
                    return;
                }
                event.setCancelled(true);
                packets.add(event.getPacket());
            }
        } else if (mode.currentMode.equals("Mineplex Combat") && event.getPacket() instanceof CPacketKeepAlive) {
            CPacketKeepAlive cPacketKeepAlive = (CPacketKeepAlive)event.packet;
            cPacketKeepAlive.key = (long)((float)cPacketKeepAlive.key - MWUtils.randomFloat(1000.0f, 2.14748365E9f));
        }

    }

    @EventTarget
    public void onPreUpdate(EventPreMotionUpdate event) {
        if (mode.currentMode.equals("StormMovement")) {
            event.setOnGround(false);
        }
    }
}


