package me.wintware.client.clickgui.settings;

import java.util.ArrayList;
import java.util.Iterator;
import me.wintware.client.module.Module;

public class SettingsManager {
   private ArrayList<Setting> settings = new ArrayList();

   public void rSetting(Setting in) {
      this.settings.add(in);
   }

   public ArrayList<Setting> getSettings() {
      return this.settings;
   }

   public ArrayList<Setting> getSettingsByMod(Module mod) {
      ArrayList<Setting> out = new ArrayList();
      Iterator var3 = this.getSettings().iterator();

      while(var3.hasNext()) {
         Setting s = (Setting)var3.next();
         if (s.getParentMod().equals(mod)) {
            out.add(s);
         }
      }

      if (out.isEmpty()) {
         return null;
      } else {
         return out;
      }
   }

   public Setting getSettingByName(String name) {
      Iterator var2 = this.getSettings().iterator();

      Setting set;
      do {
         if (!var2.hasNext()) {
            System.err.println("123" + name);
            return null;
         }

         set = (Setting)var2.next();
      } while(!set.getName().equalsIgnoreCase(name));

      return set;
   }
}
