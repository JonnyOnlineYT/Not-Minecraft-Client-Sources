package me.wintware.client.module.player;

import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.EventUpdate;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;

public class Parkour extends Module {
   public Parkour() {
      super("Parkour", Category.Player);
   }

   @EventTarget
   public void onLocalPlayerUpdate(EventUpdate e) {
      if (mc.player.onGround && !mc.player.isSneaking() && !mc.gameSettings.keyBindJump.isPressed() && mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(0.0D, -0.5D, 0.0D).expand(-0.001D, 0.0D, -0.001D)).isEmpty()) {
         mc.player.jump();
      }

   }
}
