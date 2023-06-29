package me.wintware.client;

import me.wintware.client.clickgui.ClickGuiScreen;
import me.wintware.client.clickgui.settings.SettingsManager;
import me.wintware.client.command.CommandManager;
import me.wintware.client.config.Config;
import me.wintware.client.event.EventManager;
import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.EventKey;
import me.wintware.client.friendsystem.FriendManager;
import me.wintware.client.module.Module;
import me.wintware.client.module.ModuleManager;
import me.wintware.client.ui.configgui.ConfigManager;

public class Main {
   public static String name = "WintWare";
   public static String version = "0.2";
   public static String build = "000044";
   public static Main instance = new Main();
   public EventManager eventManager;
   public SettingsManager setmgr;
   public ModuleManager moduleManager;
   public FriendManager friendManager;
   public ConfigManager configManager;
   public ClickGuiScreen clickGui1;
   public CommandManager commandManager;
   public Config config;

   public void init() {
      this.setmgr = new SettingsManager();
      this.friendManager = new FriendManager();
      this.moduleManager = new ModuleManager();
      this.commandManager = new CommandManager();
      this.clickGui1 = new ClickGuiScreen();
      this.config = new Config();
      EventManager.register(this);
   }

   @EventTarget
   public void onKey(EventKey event) {
      this.moduleManager.getModules().stream().filter((module) -> {
         return module.getKey() == event.getKey();
      }).forEach(Module::toggle);
   }
}
