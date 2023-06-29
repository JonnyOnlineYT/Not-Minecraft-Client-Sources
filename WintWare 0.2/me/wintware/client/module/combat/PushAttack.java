package me.wintware.client.module.combat;

import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.EventUpdate;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;

public class PushAttack extends Module {
   public PushAttack() {
      super("AutoClicker", Category.Combat);
   }

   @EventTarget
   public void onPreMotion(EventUpdate event) {
      if (mc.player.getCooledAttackStrength(0.0F) == 1.0F && mc.gameSettings.keyBindAttack.pressed) {
         mc.clickMouse();
      }

   }
}
