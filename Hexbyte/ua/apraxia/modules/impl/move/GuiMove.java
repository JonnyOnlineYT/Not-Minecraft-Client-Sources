package ua.apraxia.modules.impl.move;

import ua.apraxia.eventapi.EventTarget;
import ua.apraxia.eventapi.events.impl.player.EventUpdate;
import ua.apraxia.gui.WindowGUI;
import ua.apraxia.modules.Categories;
import ua.apraxia.modules.Module;
import ua.apraxia.utility.Utility;
import org.lwjgl.input.Keyboard;

public class GuiMove extends Module {
    public GuiMove() {
        super("GuiMove", Categories.Movement);
    }
    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (!(mc.currentScreen instanceof net.minecraft.client.gui.GuiChat)) {
              Utility.mc.gameSettings.keyBindJump.pressed = Keyboard.isKeyDown(Utility.mc.gameSettings.keyBindJump.getKeyCode());
              Utility.mc.gameSettings.keyBindForward.pressed = Keyboard.isKeyDown(Utility.mc.gameSettings.keyBindForward.getKeyCode());
              Utility.mc.gameSettings.keyBindBack.pressed = Keyboard.isKeyDown(Utility.mc.gameSettings.keyBindBack.getKeyCode());
              Utility.mc.gameSettings.keyBindLeft.pressed = Keyboard.isKeyDown(Utility.mc.gameSettings.keyBindLeft.getKeyCode());
              Utility.mc.gameSettings.keyBindRight.pressed = Keyboard.isKeyDown(Utility.mc.gameSettings.keyBindRight.getKeyCode());
              Utility.mc.gameSettings.keyBindSprint.pressed = Keyboard.isKeyDown(Utility.mc.gameSettings.keyBindSprint.getKeyCode());
          }
        }

    public void onDisable() {
        Utility.mc.gameSettings.keyBindJump.pressed = false;
        Utility.mc.gameSettings.keyBindForward.pressed = false;
        Utility.mc.gameSettings.keyBindBack.pressed = false;
        Utility.mc.gameSettings.keyBindLeft.pressed = false;
        Utility.mc.gameSettings.keyBindRight.pressed = false;
        Utility.mc.gameSettings.keyBindSprint.pressed = false;
        super.onDisable();
    }
        @Override
        public void onEnable () {
            super.onEnable();
        }
    }
