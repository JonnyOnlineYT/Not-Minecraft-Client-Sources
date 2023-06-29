//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.misc;

import me.independed.inceptice.modules.Module;
import net.minecraft.world.GameType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class FakeCreative extends Module {
      public FakeCreative() {
            super("FakeCreative", "actually fake creative, you can beat further(reach)", ((1137368769 ^ 984624748) & 1082958858) >> 4 ^ 67144192, Module.Category.WORLD);
      }

      @SubscribeEvent
      public void onPlayerTick(PlayerTickEvent var1) {
            if (mc.player != null) {
                  GameType var10000 = mc.playerController.getCurrentGameType();
                  GameType var10001 = GameType.CREATIVE;
                  if (!"You're so fat whenever you go to the beach the tide comes in.".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                        ;
                  }

                  if (var10000 != var10001) {
                        mc.playerController.setGameType(GameType.CREATIVE);
                  }
            }
      }

      public void onDisable() {
            super.onDisable();
            if (mc.player != null) {
                  mc.playerController.setGameType(GameType.SURVIVAL);
            }
      }
}
