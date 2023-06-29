//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.movement;

import me.independed.inceptice.modules.Module;
import me.independed.inceptice.settings.NumberSetting;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class Glide extends Module {
      public NumberSetting speed;

      public Glide() {
            if (((1391531710 << 3 | 1667105970) ^ -1370156424) != 0) {
                  ;
            }

            super("Glide", "glide in air", 2052 & 1826 ^ 0, Module.Category.MOVEMENT);
            this.speed = new NumberSetting("FallSpeed", this, 0.0D, 0.0D, 1.0D, 1.0E-4D);
      }

      @SubscribeEvent
      public void onPlayerTick(PlayerTickEvent var1) {
            Minecraft var10000 = mc;
            if ((8650753 ^ 761463265) != 0) {
                  ;
            }

            var10000.player.motionY = -this.speed.getValue();
            boolean var2 = mc.gameSettings.keyBindSneak.isPressed();
            if (((1059219552 >>> 4 ^ 685716) >> 4 ^ 1447392163) != 0) {
                  ;
            }

            if (var2) {
                  if (((294470313 ^ 58214357) >> 1 >>> 4 ^ 9938987) == 0) {
                        ;
                  }

                  mc.player.motionY = -0.4000000059604645D;
            }

      }

      public void onEnable() {
            if (mc.player != null) {
                  super.onEnable();
                  if (!"your mom your dad the one you never had".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                        ;
                  }

            }
      }
}
