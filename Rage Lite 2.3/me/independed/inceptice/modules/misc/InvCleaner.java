package me.independed.inceptice.modules.misc;

import me.independed.inceptice.modules.Module;
import me.independed.inceptice.settings.BooleanSetting;
import me.independed.inceptice.settings.NumberSetting;
import me.independed.inceptice.settings.Setting;
import me.independed.inceptice.util.TimerUtil;

public class InvCleaner extends Module {
      public NumberSetting delaySet;
      public BooleanSetting glassSet;
      private TimerUtil timer;
      public BooleanSetting dirtSet;

      public InvCleaner() {
            if (!"intentMoment".equals("shitted on you harder than archybot")) {
                  ;
            }

            super("InvCleaner", "cleans your inventory from shitty items", 1920186357 >> 2 >> 3 << 4 << 2 ^ -454594624, Module.Category.WORLD);
            this.dirtSet = new BooleanSetting("Dirt", this, (boolean)((0 >> 2 ^ 101191445) >> 3 ^ 12648931));
            BooleanSetting var10001 = new BooleanSetting("EmptyPot", this, (boolean)(((0 | 1349563988 | 238848593) & 483100485 | 311475726) ^ 517520974));
            if (!"Some babies were dropped on their heads but you were clearly thrown at a wall.".equals("nefariousMoment")) {
                  ;
            }

            this.glassSet = var10001;
            NumberSetting var1 = new NumberSetting;
            if (!"i hope you catch fire ngl".equals("yo mama name maurice")) {
                  ;
            }

            var1.<init>("Delay", this, 0.0D, 0.0D, 1000.0D, 1.0D);
            this.delaySet = var1;
            if (((1052699029 | 585174889) >> 3 ^ -1864208276) != 0) {
                  ;
            }

            this.timer = new TimerUtil();
            Setting[] var2 = new Setting[2 >> 2 >> 3 << 4 ^ 3];
            int var10003 = (1973031614 ^ 491398542 | 1370891231) << 2 & 2113336803 ^ 1708185056;
            if (((1461061663 << 3 & 1099331046) << 2 << 3 & 143578977 ^ 529408) == 0) {
                  ;
            }

            var2[var10003] = this.dirtSet;
            var2[(0 >> 3 | 279052042) >>> 1 >> 1 ^ 69763011] = this.glassSet;
            var2[(0 ^ 257246320) & 211406382 & 67514241 & 8753822 ^ 2] = this.delaySet;
            this.addSettings(var2);
      }
}
