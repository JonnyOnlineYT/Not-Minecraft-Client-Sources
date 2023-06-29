//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.misc;

import me.independed.inceptice.modules.Module;
import me.independed.inceptice.settings.NumberSetting;
import me.independed.inceptice.settings.Setting;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class Ambience extends Module {
      private long time;
      public NumberSetting speed;

      public void onPlayerTick(PlayerTickEvent var1) {
            mc.world.setWorldTime(this.time);
            double var10001 = (double)this.time;
            if ((667713046 >> 1 >>> 2 ^ 83464130) == 0) {
                  ;
            }

            var10001 += this.speed.getValue();
            if ((((557831753 ^ 392063461) & 780422128) >>> 1 ^ 318768592) == 0) {
                  ;
            }

            this.time = (long)var10001;
      }

      public Ambience() {
            if ((532010782 << 3 & 115975792 ^ 619980312) != 0) {
                  ;
            }

            super("Ambience", "change world time", 1044654652 >> 2 & 131761342 ^ 126877838, Module.Category.WORLD);
            this.speed = new NumberSetting("Speed", this, 20.0D, 0.0D, 220.0D, 1.0D);
            if (((432974626 << 2 | 1676500014) >>> 2 >> 3 ^ 54525925) == 0) {
                  ;
            }

            this.time = ((1923455466985722028L & 587966818435525279L | 545141381630642334L) ^ 993330298487482269L) & 16345993338553650L ^ 15782458691814658L;
            Setting[] var10001 = new Setting[((0 & 2026258962 | 708829071) >> 1 & 258824698) << 3 ^ 678055441];
            if ((((1991940128 | 473579089) & 85410404 ^ 30168421) >>> 3 ^ 12307168) == 0) {
                  ;
            }

            var10001[((2114603278 ^ 1048160448) & 523263522 | 2713537) ^ 3762115] = this.speed;
            this.addSettings(var10001);
      }
}
