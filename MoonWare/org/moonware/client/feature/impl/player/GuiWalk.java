package org.moonware.client.feature.impl.player;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.client.settings.KeyBinding;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.player.EventUpdate;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.lwjgl.input.Keyboard;

public class GuiWalk extends Feature {

    public GuiWalk() {
        super("GuiWalk", "Позволяет ходить в окрытых контейнерах", Type.Movement);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        KeyBinding[] keys = {Minecraft.gameSettings.keyBindForward, Minecraft.gameSettings.keyBindBack, Minecraft.gameSettings.keyBindLeft, Minecraft.gameSettings.keyBindRight, Minecraft.gameSettings.keyBindJump, Minecraft.gameSettings.keyBindSprint};

        if (Minecraft.screen instanceof ChatScreen || Minecraft.screen instanceof GuiEditSign)
            return;

        for (KeyBinding keyBinding : keys) {
            keyBinding.pressed = Keyboard.isKeyDown(keyBinding.getKeyCode());
        }
    }
}
