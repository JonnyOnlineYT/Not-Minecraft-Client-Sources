package me.wintware.client.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import me.wintware.client.Main;
import me.wintware.client.clickgui.settings.Setting;
import me.wintware.client.module.Module;
import net.minecraft.client.Minecraft;

public class Config {
   public File dir;
   public File configs;
   public File dataFile;

   public Config() {
      this.dir = new File(Minecraft.getMinecraft().mcDataDir, "WintWare");
      if (!this.dir.exists()) {
         this.dir.mkdir();
      }

      this.dataFile = new File(this.dir, "config.txt");
      if (!this.dataFile.exists()) {
         try {
            this.dataFile.createNewFile();
         } catch (IOException var2) {
            var2.printStackTrace();
         }
      }

      this.load();
   }

   public void save() {
      ArrayList<String> toSave = new ArrayList();
      Iterator var2 = Main.instance.moduleManager.getModules().iterator();

      while(var2.hasNext()) {
         Module mod = (Module)var2.next();
         toSave.add("Module:" + mod.getName() + ":" + mod.getState() + ":" + mod.getKey());
      }

      var2 = Main.instance.setmgr.getSettings().iterator();

      while(var2.hasNext()) {
         Setting set = (Setting)var2.next();
         if (set.isCheck()) {
            toSave.add("Setting:" + set.getName() + ":" + set.getParentMod().getName() + ":" + set.getValue());
         }

         if (set.isCombo()) {
            toSave.add("Setting:" + set.getName() + ":" + set.getParentMod().getName() + ":" + set.getValString());
         }

         if (set.isSlider()) {
            toSave.add("Setting:" + set.getName() + ":" + set.getParentMod().getName() + ":" + set.getValDouble());
         }
      }

      try {
         PrintWriter pw = new PrintWriter(this.dataFile);
         Iterator var8 = toSave.iterator();

         while(var8.hasNext()) {
            String str = (String)var8.next();
            pw.println(str);
         }

         pw.close();
      } catch (FileNotFoundException var5) {
         var5.printStackTrace();
      }

   }

   public void load() {
      ArrayList lines = new ArrayList();

      String s;
      try {
         BufferedReader reader = new BufferedReader(new FileReader(this.dataFile));

         for(s = reader.readLine(); s != null; s = reader.readLine()) {
            lines.add(s);
         }

         reader.close();
      } catch (Exception var7) {
         var7.printStackTrace();
      }

      Iterator var8 = lines.iterator();

      while(var8.hasNext()) {
         s = (String)var8.next();
         String[] args = s.split(":");
         Module m;
         if (s.toLowerCase().startsWith("module:")) {
            m = Main.instance.moduleManager.getModuleByName(args[1]);
            if (m != null) {
               m.setEnabled(Boolean.parseBoolean(args[2]));
               m.setKey(Integer.parseInt(args[3]));
            }
         } else if (s.toLowerCase().startsWith("setting:")) {
            m = Main.instance.moduleManager.getModuleByName(args[2]);
            if (m != null) {
               Setting set = Main.instance.setmgr.getSettingByName(args[1]);
               if (set != null) {
                  if (set.isCheck()) {
                     set.setValue(Boolean.parseBoolean(args[3]));
                  }

                  if (set.isCombo()) {
                     set.setValString(args[3]);
                  }

                  if (set.isSlider()) {
                     set.setValDouble(Double.parseDouble(args[3]));
                  }
               }
            }
         }
      }

   }
}
