package org.moonware.client.feature.impl.player;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.CPacketPlayer;
import org.moonware.client.MoonWare;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.player.EventUpdate;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.feature.impl.misc.FreeCam;

public class HurtClip extends Feature {

    public boolean damageToggle;

    public HurtClip() {
        super("HurtClip", "Клипает вас под бедрок", Type.Other);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (!damageToggle) {
            for (int i = 0; i < 9; i++) {
                Minecraft.player.connection.sendPacket(new CPacketPlayer.Position(Minecraft.player.posX, Minecraft.player.posY, Minecraft.player.posZ, false));
                Minecraft.player.connection.sendPacket(new CPacketPlayer.Position(Minecraft.player.posX, Minecraft.player.posY + 0.4, Minecraft.player.posZ, false));
            }
            Minecraft.player.connection.sendPacket(new CPacketPlayer.Position(Minecraft.player.posX, Minecraft.player.posY, Minecraft.player.posZ, true));
            Minecraft.player.connection.sendPacket(new CPacketPlayer.Position(Minecraft.player.posX, Minecraft.player.posY, Minecraft.player.posZ, true));
        }
        if (Minecraft.player.hurtTime > 0) {
            Minecraft.player.setPositionAndUpdate(Minecraft.player.posX, -2, Minecraft.player.posZ);
            damageToggle = true;
            toggle();
            MoonWare.featureManager.getFeatureByClass(FreeCam.class).setState(true);
        }
    }
}
