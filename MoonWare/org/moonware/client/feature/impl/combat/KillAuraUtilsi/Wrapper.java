package org.moonware.client.feature.impl.combat.KillAuraUtilsi;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;

public class Wrapper {
    public static Minecraft MC = Minecraft.getMinecraft();
    public static EntityPlayerSP getPlayer() {
        return Minecraft.player;
    }
    public static Minecraft MC() {
        return Minecraft.getMinecraft();
    }
}
