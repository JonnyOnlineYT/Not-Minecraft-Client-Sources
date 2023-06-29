package me.wintware.client.module.movement;

import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.EventUpdate;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;

public class AirJump extends Module {
   public AirJump() {
      super("AirJump", Category.Movement);
   }

   @EventTarget
   public void onUpdate(EventUpdate event) {
      mc.player.onGround = true;
   }

   public void onDisable() {
      super.onDisable();
      mc.player.onGround = false;
   }
}
