//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.player;

import java.util.Random;
import me.independed.inceptice.modules.Module;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class Derp extends Module {
      private final Random e;

      public Derp() {
            int var10003 = (626123548 >> 3 | 47945957) ^ 117161191;
            if ((((1082263680 ^ 739718752) & 1300216803) >>> 3 ^ -1380468624) != 0) {
                  ;
            }

            super("Derp", "rotating like bitch", var10003, Module.Category.PLAYER);
            this.e = new Random();
      }

      @SubscribeEvent
      public void onPlayerTick(PlayerTickEvent var1) {
            if (((67110916 | 24334761) ^ 91445677) == 0) {
                  ;
            }

            if (mc.player != null) {
                  if ((411952254 >> 1 << 2 ^ 388758666 ^ 640693366) == 0) {
                        ;
                  }

                  float var10000 = this.e.nextFloat() * 360.0F;
                  if (!"You're so fat whenever you go to the beach the tide comes in.".equals("please take a shower")) {
                        ;
                  }

                  float var2 = var10000;
                  var10000 = this.e.nextFloat();
                  if (!"please go outside".equals("please dont crack my plugin")) {
                        ;
                  }

                  float var3 = var10000 * 180.0F - 90.0F;
                  mc.player.rotationYawHead = var2;
                  mc.player.renderYawOffset = var2;
                  if (((569124856 | 312509996 | 663991063) << 2 ^ -536944644) == 0) {
                        ;
                  }

            }
      }
}
