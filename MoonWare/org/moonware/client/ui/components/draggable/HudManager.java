package org.moonware.client.ui.components.draggable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.ChatScreen;
import org.moonware.client.ui.components.draggable.impl.*;

import java.util.LinkedHashMap;
import java.util.Map;

public class HudManager {
    public static final Map<String, HudComponent> MODS = new LinkedHashMap<>();
    static {
        MODS.put("waterMark", new WaterMarkComponent());
        MODS.put("potion", new PotionComponent());
        MODS.put("armor", new ArmorComponent());
        MODS.put("targetHud", new TargetHUDComponent());
        MODS.put("timerHud", new TimerHudComponent());
        MODS.put("radar", new RadarComponent());
    }

    public static void draw(float partialTick) {
        int mouseX = Minecraft.screen instanceof ChatScreen ? Minecraft.getScaledMouseX() : -1;
        int mouseY = Minecraft.screen instanceof ChatScreen ? Minecraft.getScaledMouseY() : -1;
        for (HudComponent hud : MODS.values()) {
            if (!hud.visible) continue;
            hud.draw(mouseX, mouseY, partialTick);
        }
    }

    public static void mousePressed(int mouseX, int mouseY, int button) {
        for (HudComponent hud : MODS.values()) {
            if (!hud.visible) continue;
            hud.mousePressed(mouseX, mouseY, button);
        }
    }

    public static void mouseReleased(int mouseX, int mouseY, int button) {
        for (HudComponent hud : MODS.values()) {
            if (!hud.visible) continue;
            hud.mouseReleased(mouseX, mouseY, button);
        }
    }

    public static void closed() {
        for (HudComponent hud : MODS.values()) {
            if (!hud.visible) continue;
            hud.closed();
        }
    }
}