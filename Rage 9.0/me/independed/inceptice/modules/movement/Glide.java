//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.movement;

import me.independed.inceptice.modules.Module;
import me.independed.inceptice.settings.NumberSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.GameSettings;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class Glide extends Module {
      public NumberSetting speed;

      @SubscribeEvent
      public void onPlayerTick(PlayerTickEvent var1) {
            mc.player.motionY = -this.speed.getValue();
            Minecraft var10000 = mc;
            if ((395424043 << 4 & 1034626544 ^ 957022384) == 0) {
                  ;
            }

            GameSettings var2 = var10000.gameSettings;
            if (((523009874 >>> 4 | 20161638) << 3 ^ 262094776) == 0) {
                  ;
            }

            if (var2.keyBindSneak.isPressed()) {
                  if ((((410034946 | 285139584) ^ 5493405) & 77532688 ^ 1551577107) != 0) {
                        ;
                  }

                  EntityPlayerSP var3 = mc.player;
                  if (((1685478122 | 1630595400) >> 3 >>> 1 ^ 106393534) == 0) {
                        ;
                  }

                  var3.motionY = -0.4000000059604645D;
            }

      }

      public void onEnable() {
            if (mc.player != null) {
                  super.onEnable();
            }
      }

      public Glide() {
            super("Glide", "glide in air", (241238862 ^ 14084519) >> 3 ^ 30866781, Module.Category.MOVEMENT);
            NumberSetting var10001 = new NumberSetting;
            if (!"intentMoment".equals("idiot")) {
                  ;
            }

            var10001.<init>("FallSpeed", this, 0.0D, 0.0D, 1.0D, 1.0E-4D);
            this.speed = var10001;
            if (((41663127 >> 3 >> 4 | 270902) ^ 243125453) != 0) {
                  ;
            }

      }
}
