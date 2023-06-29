package Celestial.module.impl.Player;

import Celestial.event.EventTarget;
import Celestial.event.events.impl.player.EventUpdate;
import Celestial.module.Module;
import Celestial.module.ModuleCategory;
import Celestial.utils.Helper;
import org.lwjgl.input.Keyboard;

public class GuiWalk extends Module {
    public GuiWalk() {
        super("GuiWalk", "Позволяет ходить с открытым инвентарем, сундуком и т.п", ModuleCategory.Player);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (!(Helper.mc.currentScreen instanceof net.minecraft.client.gui.GuiChat)) {
            Helper.mc.gameSettings.keyBindJump.pressed = Keyboard.isKeyDown(Helper.mc.gameSettings.keyBindJump.getKeyCode());
            Helper.mc.gameSettings.keyBindForward.pressed = Keyboard.isKeyDown(Helper.mc.gameSettings.keyBindForward.getKeyCode());
            Helper.mc.gameSettings.keyBindBack.pressed = Keyboard.isKeyDown(Helper.mc.gameSettings.keyBindBack.getKeyCode());
            Helper.mc.gameSettings.keyBindLeft.pressed = Keyboard.isKeyDown(Helper.mc.gameSettings.keyBindLeft.getKeyCode());
            Helper.mc.gameSettings.keyBindRight.pressed = Keyboard.isKeyDown(Helper.mc.gameSettings.keyBindRight.getKeyCode());
            Helper.mc.gameSettings.keyBindSprint.pressed = Keyboard.isKeyDown(Helper.mc.gameSettings.keyBindSprint.getKeyCode());
        }
    }

    public void onDisable() {
        Helper.mc.gameSettings.keyBindJump.pressed = false;
        Helper.mc.gameSettings.keyBindForward.pressed = false;
        Helper.mc.gameSettings.keyBindBack.pressed = false;
        Helper.mc.gameSettings.keyBindLeft.pressed = false;
        Helper.mc.gameSettings.keyBindRight.pressed = false;
        Helper.mc.gameSettings.keyBindSprint.pressed = false;
        super.onDisable();
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }
}