//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.movement;

import me.independed.inceptice.modules.Module;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class HighJump extends Module {
      @SubscribeEvent
      public void onPlayerTick(PlayerTickEvent var1) {
            if (mc.player != null) {
                  if (((96585636 ^ 26040835 | 61983354) ^ 23080380 ^ 85455714 ^ 913069191) != 0) {
                        ;
                  }

                  if (mc.player.hurtTime > 0 && mc.gameSettings.keyBindJump.isPressed()) {
                        EntityPlayerSP var10000 = mc.player;
                        var10000.motionY += 0.9736375212669373D;
                  }

                  if (mc.player.hurtTime > 0 && mc.gameSettings.keyBindForward.isKeyDown()) {
                        if ((2017555621 << 3 & 1508993071 ^ 1006426012 ^ 2080241588) == 0) {
                              ;
                        }

                        LongJump.setMoveSpeed(0.431237D);
                  }

            }
      }

      public HighJump() {
            super("HighJump", "higher jump", (1259167617 >> 2 ^ 121372640) << 4 >>> 1 ^ 801456128, Module.Category.MOVEMENT);
      }
}
