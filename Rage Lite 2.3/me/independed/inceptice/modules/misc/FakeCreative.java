//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.misc;

import me.independed.inceptice.modules.Module;
import net.minecraft.world.GameType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class FakeCreative extends Module {
      @SubscribeEvent
      public void onPlayerTick(PlayerTickEvent var1) {
            if (mc.player != null) {
                  if (mc.playerController.getCurrentGameType() == GameType.CREATIVE) {
                        if (((1757715112 << 1 & 1707207927) >>> 1 ^ 96328446 ^ 628967638) == 0) {
                              ;
                        }

                  } else {
                        mc.playerController.setGameType(GameType.CREATIVE);
                  }
            }
      }

      public FakeCreative() {
            if (((1409606474 << 1 | 2017313742) >>> 1 ^ 2082399215) == 0) {
                  ;
            }

            super("FakeCreative", "actually fake creative, you can beat further(reach)", ((1012968333 >> 1 | 366021175) >>> 1 & 182078817 | 43322538) ^ 182259179, Module.Category.WORLD);
      }

      public void onDisable() {
            if (!"i hope you catch fire ngl".equals("please take a shower")) {
                  ;
            }

            super.onDisable();
            if (mc.player != null) {
                  if ((((814935293 ^ 362813532) >> 3 | 3166854) ^ -234629239) != 0) {
                        ;
                  }

                  mc.playerController.setGameType(GameType.SURVIVAL);
            }
      }
}
