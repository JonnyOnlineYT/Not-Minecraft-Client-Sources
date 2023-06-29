package ru.rockstar.client.features.impl.player;

import org.lwjgl.input.Keyboard;

import ru.rockstar.api.event.EventTarget;
import ru.rockstar.api.event.event.EventUpdate;
import ru.rockstar.client.features.Category;
import ru.rockstar.client.features.Feature;


public class GuiMove extends Feature {

    public GuiMove() {
        super("GuiWalk", "Позволяет ходить в открытом контейнере (инвентарь,сундук и т.д)",Keyboard.KEY_NONE, Category.PLAYER);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (!(mc.currentScreen instanceof net.minecraft.client.gui.GuiChat)) {
            mc.gameSettings.keyBindJump.pressed = Keyboard.isKeyDown(mc.gameSettings.keyBindJump.getKeyCode());
            mc.gameSettings.keyBindForward.pressed = Keyboard.isKeyDown(mc.gameSettings.keyBindForward.getKeyCode());
            mc.gameSettings.keyBindBack.pressed = Keyboard.isKeyDown(mc.gameSettings.keyBindBack.getKeyCode());
            mc.gameSettings.keyBindLeft.pressed = Keyboard.isKeyDown(mc.gameSettings.keyBindLeft.getKeyCode());
            mc.gameSettings.keyBindRight.pressed = Keyboard.isKeyDown(mc.gameSettings.keyBindRight.getKeyCode());
            mc.gameSettings.keyBindSprint.pressed = Keyboard.isKeyDown(mc.gameSettings.keyBindSprint.getKeyCode());
        }
    }

    public void onDisable() {
        mc.gameSettings.keyBindJump.pressed = false;
        mc.gameSettings.keyBindForward.pressed = false;
        mc.gameSettings.keyBindBack.pressed = false;
        mc.gameSettings.keyBindLeft.pressed = false;
        mc.gameSettings.keyBindRight.pressed = false;
        mc.gameSettings.keyBindSprint.pressed = false;
        super.onDisable();
    }
    @Override
    public void onEnable() {
        super.onEnable();
    }
}