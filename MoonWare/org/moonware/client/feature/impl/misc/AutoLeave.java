package org.moonware.client.feature.impl.misc;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.player.EventPreMotion;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.settings.impl.ListSetting;
import org.moonware.client.settings.impl.NumberSetting;

public class AutoLeave extends Feature {
    public static ListSetting leavemode = new ListSetting("LeaveMode", "/spawn",() -> true, "/spawn", "/rtp", "/home", "/home home");
    public static NumberSetting range = new NumberSetting("Range",20,1,40,0.1F,() -> true);
    public AutoLeave() {
        super("AutoLeave", "Ливает если рядом с вами игрок", Type.Other);
        addSettings(leavemode, range);
    }

    @EventTarget
    public void eventPreMotion(EventPreMotion event) {
        for (Entity e : Minecraft.world.loadedEntityList) {
            if (e instanceof EntityPlayer) {
                if (Minecraft.player.getDistanceToEntity(e) <= range.getNumberValue()) {
                    if (leavemode.currentMode.equalsIgnoreCase("/spawn")) {
                        Minecraft.player.sendChatMessage("/spawn");
                    }else if (leavemode.currentMode.equalsIgnoreCase("/rtp")) {
                        Minecraft.player.sendChatMessage("/rtp");
                    }else if (leavemode.currentMode.equalsIgnoreCase("/home")) {
                        Minecraft.player.sendChatMessage("/home");
                    }else if (leavemode.currentMode.equalsIgnoreCase("/home home")) {
                        Minecraft.player.sendChatMessage("/home home");
                    }
                }
            }
        }
    }
}
