package me.wintware.client.clickgui.settings;

import java.util.ArrayList;
import java.util.function.Supplier;
import me.wintware.client.Main;
import me.wintware.client.module.Module;

public class Setting {
   private String name;
   private Module parent;
   public String mode;
   private String sval;
   private ArrayList<String> options;
   private boolean bval;
   private double dval;
   private double min;
   private double max;
   private boolean onlyint = false;
   public boolean percentage;
   private boolean comboExpand;
   private boolean visible;
   private Supplier<Boolean> visibility;

   public Setting(String name, Module parent, String sval, ArrayList<String> options) {
      this.name = name;
      this.parent = parent;
      this.sval = sval;
      this.options = options;
      this.comboExpand = false;
      this.mode = "Combo";
      this.visibility = () -> {
         return true;
      };
   }

   public Setting(String name, Module parent, String sval, ArrayList<String> options, Supplier<Boolean> visibility) {
      this.name = name;
      this.parent = parent;
      this.sval = sval;
      this.options = options;
      this.comboExpand = false;
      this.mode = "Combo";
      this.visibility = visibility;
   }

   public Setting(String name, Module parent, boolean bval) {
      this.name = name;
      this.parent = parent;
      this.bval = bval;
      this.mode = "Check";
      this.visibility = () -> {
         return true;
      };
   }

   public Setting(String name, Module parent, boolean bval, Supplier<Boolean> visibility) {
      this.name = name;
      this.parent = parent;
      this.bval = bval;
      this.mode = "Check";
      this.visibility = visibility;
   }

   public Setting(String name, Module parent, double dval, double min, double max, boolean onlyint) {
      this.name = name;
      this.parent = parent;
      this.dval = dval;
      this.min = min;
      this.max = max;
      this.onlyint = onlyint;
      this.mode = "Slider";
      this.visibility = () -> {
         return true;
      };
   }

   public Setting(String name, Module parent, double dval, double min, double max, boolean onlyint, Supplier<Boolean> visibility) {
      this.name = name;
      this.parent = parent;
      this.dval = dval;
      this.min = min;
      this.max = max;
      this.onlyint = onlyint;
      this.mode = "Slider";
      this.visibility = visibility;
   }

   public Setting(String name, Module parent, double dval, double min, double max, boolean onlyint, boolean perc) {
      this.name = name;
      this.parent = parent;
      this.dval = dval;
      this.min = min;
      this.max = max;
      this.onlyint = onlyint;
      this.mode = "Slider";
      this.percentage = perc;
      this.visibility = () -> {
         return true;
      };
   }

   public Setting(String name, Module parent, double dval, double min, double max, boolean onlyint, boolean perc, Supplier<Boolean> visibility) {
      this.name = name;
      this.parent = parent;
      this.dval = dval;
      this.min = min;
      this.max = max;
      this.onlyint = onlyint;
      this.mode = "Slider";
      this.percentage = perc;
      this.visibility = visibility;
   }

   public String getName() {
      return this.name;
   }

   public Module getParentMod() {
      return this.parent;
   }

   public String getValString() {
      return this.sval;
   }

   public void setValString(String in) {
      this.sval = in;
      if (Main.instance.config != null) {
         Main.instance.config.save();
      }

   }

   public boolean isVisible() {
      return (Boolean)this.visibility.get();
   }

   public ArrayList<String> getOptions() {
      return this.options;
   }

   public boolean getValue() {
      return this.bval;
   }

   public void setValue(boolean in) {
      this.bval = in;
      if (Main.instance.config != null) {
         Main.instance.config.save();
      }

   }

   public void setVisible(boolean visible) {
      this.visible = visible;
   }

   public double getValDouble() {
      if (this.onlyint) {
         this.dval = (double)((int)this.dval);
      }

      return this.dval;
   }

   public void setValDouble(double in) {
      this.dval = in;
      if (Main.instance.config != null) {
         Main.instance.config.save();
      }

   }

   public double getMin() {
      return this.min;
   }

   public double getMax() {
      return this.max;
   }

   public float getValFloat() {
      if (this.onlyint) {
         this.dval = (double)((int)this.dval);
      }

      return (float)this.dval;
   }

   public long getValLong() {
      if (this.onlyint) {
         this.dval = (double)((int)this.dval);
      }

      return (long)this.dval;
   }

   public int getValInt() {
      if (this.onlyint) {
         this.dval = (double)((int)this.dval);
      }

      return (int)this.dval;
   }

   public boolean isCombo() {
      return this.mode.equalsIgnoreCase("Combo");
   }

   public boolean isCheck() {
      return this.mode.equalsIgnoreCase("Check");
   }

   public boolean isSlider() {
      return this.mode.equalsIgnoreCase("Slider");
   }

   public boolean onlyInt() {
      return this.onlyint;
   }

   public boolean isComboExpand() {
      return this.comboExpand;
   }

   public void setComboExpand(boolean comboExpand) {
      this.comboExpand = comboExpand;
   }

   public String getMode() {
      return this.sval;
   }
}
