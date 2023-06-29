package me.wintware.client.module.movement;

import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.EventUpdate;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

public class InvWalk extends Module {
   public InvWalk() {
      super("InventoryWalk", Category.Player);
   }

   @EventTarget
   public void o(EventUpdate e) {
      KeyBinding[] keys = new KeyBinding[]{mc.gameSettings.keyBindForward, mc.gameSettings.keyBindBack, mc.gameSettings.keyBindLeft, mc.gameSettings.keyBindRight, mc.gameSettings.keyBindJump, mc.gameSettings.keyBindSprint};
      if (mc.currentScreen != null && !(mc.currentScreen instanceof GuiChat)) {
         KeyBinding[] arrayOfKeyBinding = keys;
         int i = keys.length;

         for(byte b = 0; b < i; ++b) {
            KeyBinding bind = arrayOfKeyBinding[b];
            bind.pressed = Keyboard.isKeyDown(bind.getKeyCode());
         }
      }

   }
}
