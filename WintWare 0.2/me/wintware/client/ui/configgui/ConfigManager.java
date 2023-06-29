package me.wintware.client.ui.configgui;

import java.util.ArrayList;
import me.wintware.client.ui.configgui.configs.Vanilla;
import me.wintware.client.ui.configgui.configs.WellMore;

public class ConfigManager {
   final ArrayList<Config> configs;

   public ConfigManager() {
      (this.configs = new ArrayList()).add(new WellMore());
      this.configs.add(new Vanilla());
      this.configs.add(new WellMore());
   }

   private void addConfig(Config config) {
      this.configs.add(config);
   }

   public ArrayList<Config> getConfigs() {
      return this.configs;
   }
}
