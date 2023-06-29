package me.wintware.client.module.player;

import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.EventUpdate;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;

public class AutoSprint extends Module {
   public AutoSprint() {
      super("AutoSprint", Category.Player);
   }

   @EventTarget
   public void onUpdate(EventUpdate event) {
      mc.player.setSprinting(true);
   }

   public void onDisable() {
      super.onDisable();
      mc.player.setSprinting(mc.player.moveForward > 0.0F && !mc.player.isCollidedHorizontally ? true : mc.player.isSprinting());
   }
}
