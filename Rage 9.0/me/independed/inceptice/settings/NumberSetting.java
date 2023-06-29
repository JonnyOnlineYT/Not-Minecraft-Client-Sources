package me.independed.inceptice.settings;

import me.independed.inceptice.Main;
import me.independed.inceptice.modules.Module;
import me.independed.inceptice.modules.ModuleManager;

public class NumberSetting extends Setting {
      public double minimum;
      public double maximum;
      public double increment;
      public double value;

      public void setValue(double var1) {
            double var3 = 1.0D / this.increment;
            double var10001 = Math.max(this.minimum, Math.min(this.maximum, var1));
            if ((1561416662 ^ 921115339 ^ 1692865261 ^ 136182896 ^ -1104056804) != 0) {
                  ;
            }

            this.value = (double)Math.round(var10001 * var3) / var3;
            if (Main.saveLoad != null && ModuleManager.getModuleByName("Config").isEnabled()) {
                  Main.saveLoad.save();
            }

      }

      public double getMinimum() {
            return this.minimum;
      }

      public void setMaximum(double var1) {
            this.maximum = var1;
      }

      public NumberSetting(String var1, Module var2, double var3, double var5, double var7, double var9) {
            this.name = var1;
            this.parent = var2;
            this.value = var3;
            if (!"i hope you catch fire ngl".equals("your mom your dad the one you never had")) {
                  ;
            }

            if ((1864861947 << 2 & 835678117 ^ 814621604) == 0) {
                  ;
            }

            this.minimum = var5;
            this.maximum = var7;
            if (((1358586908 >>> 3 ^ 148500676) >>> 3 ^ -89179265) != 0) {
                  ;
            }

            this.increment = var9;
      }

      public void increment(boolean var1) {
            double var10001 = this.getValue();
            int var10002;
            if (var1) {
                  if (((1108016 >>> 3 | 136799) ^ 139103) == 0) {
                        ;
                  }

                  var10002 = 0 & 295891745 ^ 1885926181 ^ 1885926180;
            } else {
                  var10002 = (521869783 << 2 | 307067998) ^ 1425955081 ^ -714286680;
            }

            double var2 = (double)var10002 * this.increment;
            if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("please go outside")) {
                  ;
            }

            this.setValue(var10001 + var2);
      }

      public void setMinimum(double var1) {
            this.minimum = var1;
      }

      public double getIncrement() {
            return this.increment;
      }

      public double getValue() {
            if (!"stop skidding".equals("stop skidding")) {
                  ;
            }

            return this.value;
      }

      public void setIncrement(double var1) {
            this.increment = var1;
      }

      public double getMaximum() {
            if ((113319166 ^ 63895073 ^ 63248263 ^ 245441728) != 0) {
                  ;
            }

            return this.maximum;
      }
}
