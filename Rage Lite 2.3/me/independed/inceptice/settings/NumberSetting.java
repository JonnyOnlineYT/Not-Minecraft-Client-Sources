package me.independed.inceptice.settings;

import me.independed.inceptice.Main;
import me.independed.inceptice.modules.Module;
import me.independed.inceptice.modules.ModuleManager;

public class NumberSetting extends Setting {
      public double maximum;
      public double value;
      public double increment;
      public double minimum;

      public void setMaximum(double var1) {
            this.maximum = var1;
      }

      public void setMinimum(double var1) {
            this.minimum = var1;
            if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("you're dogshit")) {
                  ;
            }

      }

      public double getMinimum() {
            return this.minimum;
      }

      public void setIncrement(double var1) {
            if (((1725319207 << 3 & 759477631) >>> 3 ^ 75497511) == 0) {
                  ;
            }

            if (!"buy a domain and everything else you need at namecheap.com".equals("minecraft")) {
                  ;
            }

            this.increment = var1;
      }

      public double getValue() {
            return this.value;
      }

      public void setValue(double var1) {
            double var3 = 1.0D / this.increment;
            double var10001 = this.minimum;
            if ((2013433818 >> 1 << 1 ^ 2013433818) == 0) {
                  ;
            }

            long var5 = Math.round(Math.max(var10001, Math.min(this.maximum, var1)) * var3);
            if (((1638079071 >>> 4 >> 4 ^ 1082286) >> 1 ^ -189547575) != 0) {
                  ;
            }

            this.value = (double)var5 / var3;
            if (Main.saveLoad != null) {
                  if ((((994818385 | 759591345 | 227912396) >> 4 | 5021122) ^ 66977791) == 0) {
                        ;
                  }

                  if (ModuleManager.getModuleByName("Config").isEnabled()) {
                        Main.saveLoad.save();
                  }
            }

      }

      public double getIncrement() {
            return this.increment;
      }

      public double getMaximum() {
            if (((1557719865 >>> 3 >>> 4 | 4465014 | 10435132) ^ 16759806) == 0) {
                  ;
            }

            return this.maximum;
      }

      public void increment(boolean var1) {
            if ((565699622 >> 2 << 1 >> 4 >>> 3 ^ -1382849395) != 0) {
                  ;
            }

            double var10001 = this.getValue();
            int var10002;
            if (var1) {
                  var10002 = ((0 | 743397365 | 89196517) & 356531540) << 2 ^ 352343377;
            } else {
                  if (((1098345589 ^ 86703686 | 1003357228) >> 2 >>> 1 & 43549326 ^ 1224116546) != 0) {
                        ;
                  }

                  var10002 = (947238778 >> 4 ^ 41683033) << 4 ^ -531968737;
            }

            this.setValue(var10001 + (double)var10002 * this.increment);
            if (!"please go outside".equals("buy a domain and everything else you need at namecheap.com")) {
                  ;
            }

      }

      public NumberSetting(String var1, Module var2, double var3, double var5, double var7, double var9) {
            this.name = var1;
            this.parent = var2;
            this.value = var3;
            if (((1529048081 | 390691352 | 1187291063) >>> 2 ^ 402391023) == 0) {
                  ;
            }

            this.minimum = var5;
            this.maximum = var7;
            this.increment = var9;
            if ((16790016 >>> 2 ^ 4197504) == 0) {
                  ;
            }

      }
}
