//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.visual;

import me.independed.inceptice.modules.Module;

public class FullBright extends Module {
      public float oldGamma;

      public void onDisable() {
            super.onDisable();
            mc.gameSettings.gammaSetting = this.oldGamma;
      }

      public void onEnable() {
            super.onEnable();
            this.oldGamma = mc.gameSettings.gammaSetting;
            mc.gameSettings.gammaSetting = 10.0F;
      }

      public FullBright() {
            int var10003 = (1758010621 ^ 1745902991) >>> 3 ^ 1779758;
            if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("shitted on you harder than archybot")) {
                  ;
            }

            super("FullBright", "all bright", var10003, Module.Category.RENDER);
      }
}
