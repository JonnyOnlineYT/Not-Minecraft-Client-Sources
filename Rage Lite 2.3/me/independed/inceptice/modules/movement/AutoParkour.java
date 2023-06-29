//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.movement;

import me.independed.inceptice.modules.Module;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class AutoParkour extends Module {
      public AutoParkour() {
            if (!"you're dogshit".equals("ape covered in human flesh")) {
                  ;
            }

            super("AutoParkour", "automatically parkour", ((1233805526 ^ 553343841) >> 4 & 14153355) >> 1 ^ 2901474 ^ 6802599, Module.Category.MOVEMENT);
      }

      @SubscribeEvent
      public void onPlayerTick(PlayerTickEvent var1) {
            if (mc.player == null) {
                  if ((((77392192 >>> 1 & 28660932) << 1 | 375509) ^ 900053) == 0) {
                        ;
                  }

            } else {
                  if ((17306702 >> 1 ^ 645339384) != 0) {
                        ;
                  }

                  if (spidiki5.isMoving() && mc.player.onGround && !mc.player.isSneaking() && !mc.gameSettings.keyBindSneak.isKeyDown()) {
                        boolean var10000 = mc.gameSettings.keyBindJump.isKeyDown();
                        if (((1777977959 >> 3 | 32722345) << 4 << 3 ^ -4262272) == 0) {
                              ;
                        }

                        if (!var10000 && mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(0.0D, -0.5D, 0.0D).expand(-0.001D, 0.0D, -0.001D)).isEmpty() && mc.player.moveForward != 0.0F) {
                              mc.player.jump();
                        }
                  }

            }
      }
}
