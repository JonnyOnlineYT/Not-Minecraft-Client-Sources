package org.moonware.client.feature.impl.misc;

import net.minecraft.client.Minecraft;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.packet.EventReceivePacket;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;

public class AutoFish extends Feature {

    public AutoFish() {
        super("Auto Fish", "Автоматически ловит рыбу за вас", Type.Other);
    }

    @EventTarget
    public void onReceivePacket(EventReceivePacket event) {
        if (event.getPacket() instanceof SPacketSoundEffect) {
            SPacketSoundEffect packet = (SPacketSoundEffect) event.getPacket();
            if (packet.getCategory() == SoundCategory.NEUTRAL && packet.getSound() == SoundEvents.ENTITY_BOBBER_SPLASH) {
                if (Minecraft.player.getHeldItemMainhand().getItem() instanceof ItemFishingRod || Minecraft.player.getHeldItemOffhand().getItem() instanceof ItemFishingRod) {
                    Minecraft.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
                    Minecraft.player.swingArm(EnumHand.MAIN_HAND);
                    Minecraft.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
                    Minecraft.player.swingArm(EnumHand.MAIN_HAND);
                }
            }
        }
    }
}