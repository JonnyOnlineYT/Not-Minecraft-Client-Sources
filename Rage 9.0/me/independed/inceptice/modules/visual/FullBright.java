//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.visual;

import me.independed.inceptice.modules.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;

public class FullBright extends Module {
      public float oldGamma;

      public FullBright() {
            if (!"You're so fat whenever you go to the beach the tide comes in.".equals("you probably spell youre as your")) {
                  ;
            }

            super("FullBright", "all bright", ((1032820512 | 1031333334) >> 2 ^ 78419665) >> 3 ^ 24800613, Module.Category.RENDER);
      }

      public void onDisable() {
            if (!"please take a shower".equals("idiot")) {
                  ;
            }

            super.onDisable();
            Minecraft var10000 = mc;
            if (((406626816 ^ 211198474) & 333444007 ^ 1950225930) != 0) {
                  ;
            }

            GameSettings var1 = var10000.gameSettings;
            float var10001 = this.oldGamma;
            if (((2028717466 >>> 4 << 3 & 918690565 ^ 602069198) >>> 3 ^ 49568601) == 0) {
                  ;
            }

            var1.gammaSetting = var10001;
      }

      public void onEnable() {
            if (!"i hope you catch fire ngl".equals("please go outside")) {
                  ;
            }

            super.onEnable();
            this.oldGamma = mc.gameSettings.gammaSetting;
            mc.gameSettings.gammaSetting = 10.0F;
      }
}
