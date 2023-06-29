//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.movement;

import me.independed.inceptice.modules.Module;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.MovementInput;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class fraerok8 extends Module {
      public fraerok8() {
            super("NoSlowdown", "you can run using items", 166159315 << 2 >>> 1 >>> 4 ^ 20769914, Module.Category.MOVEMENT);
            if (((228066626 >>> 3 & 27097199) << 3 << 4 ^ -931130368) == 0) {
                  ;
            }

      }

      @SubscribeEvent
      public void onPushPlayer(InputUpdateEvent var1) {
            EntityPlayerSP var10000 = mc.player;
            if (!"you probably spell youre as your".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                  ;
            }

            if (var10000 != null) {
                  if ((1458702025 << 2 << 3 >>> 3 >>> 1 ^ 233049490) == 0) {
                        ;
                  }

                  if (mc.world != null && mc.player.isHandActive()) {
                        boolean var2 = mc.player.isRiding();
                        if (((767472095 << 1 & 86131447) >>> 1 ^ 1521814871) != 0) {
                              ;
                        }

                        if (!var2) {
                              if (!"buy a domain and everything else you need at namecheap.com".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                                    ;
                              }

                              MovementInput var3 = var1.getMovementInput();
                              var3.moveStrafe *= 5.0F;
                              var3 = var1.getMovementInput();
                              var3.moveForward *= 5.0F;
                        }
                  }
            }

      }
}
