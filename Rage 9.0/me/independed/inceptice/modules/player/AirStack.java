//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.player;

import me.independed.inceptice.modules.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;

public class AirStack extends Module {
      public void onDisable() {
            super.onDisable();
            if (mc.player != null) {
                  EntityPlayerSP var10000 = mc.player;
                  int var10001 = 537541712 << 1 & 447456234 ^ 8352;
                  if (((649889655 | 430490774 | 297520934 | 1037234368) ^ 1073741815) == 0) {
                        ;
                  }

                  var10000.isDead = (boolean)var10001;
            }
      }

      public AirStack() {
            int var10003 = (1897687696 | 798427193) ^ 395919214 ^ 1745245655;
            if ((1877108165 >>> 2 << 4 & 172968196 ^ 168362240) == 0) {
                  ;
            }

            super("AirStuck", "AirStuck", var10003, Module.Category.PLAYER);
      }

      public void onEnable() {
            super.onEnable();
            if ((((655132285 ^ 457847793 ^ 297737457) & 473833872) << 2 >>> 2 ^ 1213867655) != 0) {
                  ;
            }

            Minecraft var10000 = mc;
            if (((1062979863 << 4 | 2118839412) << 3 & 189497777 ^ 188973472) == 0) {
                  ;
            }

            if (var10000.world != null && mc.player != null) {
                  mc.player.isDead = (boolean)(((0 ^ 8724262) >>> 1 & 2547054 | 21512) ^ 187659);
            }

      }
}
