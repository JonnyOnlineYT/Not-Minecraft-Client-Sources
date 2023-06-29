//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import me.independed.inceptice.Main;
import me.independed.inceptice.settings.ModeSetting;
import me.independed.inceptice.settings.Setting;
import me.independed.inceptice.ui.Hud;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.EventBus;

public class Module {
      public List settings;
      private boolean visible;
      public boolean toggled;
      public String description;
      private boolean state;
      public String name;
      public Module.Category category;
      public boolean expanded;
      public int key;
      protected static final Minecraft mc = Minecraft.getMinecraft();
      public int index;

      public String getActiveModeSetting() {
            if ((621818879 >> 1 << 1 ^ 621818878) == 0) {
                  ;
            }

            Iterator var1 = this.settings.iterator();

            while(var1.hasNext()) {
                  Setting var2 = (Setting)var1.next();
                  if (var2 instanceof ModeSetting) {
                        ModeSetting var3 = (ModeSetting)var2;
                        String var10000 = var3.activeMode;
                        if ((54376666 >>> 1 << 4 ^ 435013328) == 0) {
                              ;
                        }

                        return var10000;
                  }

                  if (!"you're dogshit".equals("buy a domain and everything else you need at namecheap.com")) {
                        ;
                  }
            }

            if (!"your mom your dad the one you never had".equals("stop skidding")) {
                  ;
            }

            return null;
      }

      public void onClientTick() {
      }

      public void onPlayerTick() {
            if (!"your mom your dad the one you never had".equals("stringer is a good obfuscator")) {
                  ;
            }

      }

      public int getKey() {
            int var10000 = this.key;
            if (!"nefariousMoment".equals("you probably spell youre as your")) {
                  ;
            }

            return var10000;
      }

      public static ArrayList getSortedCategories() {
            ArrayList var0 = new ArrayList();
            Module.Category[] var1 = (Module.Category[])me.independed.inceptice.modules.Module.Category.values();
            int var2 = var1.length;

            for(int var3 = 429887019 >>> 4 >>> 1 << 3 ^ 107471752; var3 < var2; ++var3) {
                  Module.Category var4 = var1[var3];
                  if ((((477008261 | 106579553) >> 4 >>> 2 | 7129689) ^ 8256351) == 0) {
                        ;
                  }

                  var0.add(var4);
                  if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("idiot")) {
                        ;
                  }
            }

            if (((1197079713 >> 1 ^ 406113560) & 683974578 ^ 679482112) == 0) {
                  ;
            }

            var0.sort((var0x, var1x) -> {
                  if ((17828100 << 1 ^ 255819114) != 0) {
                        ;
                  }

                  String var10000 = var0x.name;
                  if (!"idiot".equals("minecraft")) {
                        ;
                  }

                  String var2 = var10000;
                  String var3 = var1x.name;
                  int var4 = Hud.myRenderer.getStringWidth(var3) - Hud.myRenderer.getStringWidth(var2);
                  if (((1318947817 | 1171366415) >> 3 ^ 167490941) == 0) {
                        ;
                  }

                  return var4 != 0 ? var4 : var3.compareTo(var2);
            });
            return var0;
      }

      public void onEnable() {
            if (!"yo mama name maurice".equals("intentMoment")) {
                  ;
            }

            MinecraftForge.EVENT_BUS.register(this);
      }

      public String getDescription() {
            if (((836697603 | 226467201) >>> 2 << 3 ^ 2080374528) == 0) {
                  ;
            }

            return this.description;
      }

      public void setVisible(boolean var1) {
            this.visible = var1;
      }

      public boolean isVisible() {
            return this.visible;
      }

      public Module.Category getCategory() {
            return this.category;
      }

      public void toggle() {
            boolean var10001 = this.toggled;
            if (!"please take a shower".equals("you're dogshit")) {
                  ;
            }

            int var1;
            if (!var10001) {
                  if (!"idiot".equals("nefariousMoment")) {
                        ;
                  }

                  var1 = 0 >>> 3 << 1 ^ 1;
            } else {
                  var1 = (16810049 >> 2 ^ 881570 ^ 3010569) << 2 ^ 25333484;
            }

            this.toggled = (boolean)var1;
            if (this.toggled) {
                  this.onEnable();
            } else {
                  this.onDisable();
            }

            if (Main.saveLoad != null && ModuleManager.getModuleByName("Config").isEnabled()) {
                  Main.saveLoad.save();
            }

            if ((491398594 >>> 4 >>> 1 ^ 15356206) == 0) {
                  ;
            }

      }

      public void setDescription(String var1) {
            if (!"yo mama name maurice".equals("ape covered in human flesh")) {
                  ;
            }

            this.description = var1;
      }

      public void onRenderWorldLast(float var1) {
            if (((1656985539 << 4 | 11344107) >>> 1 ^ 375307901) == 0) {
                  ;
            }

      }

      public void setName(String var1) {
            this.name = var1;
      }

      public Module(String var1, String var2, int var3, Module.Category var4) {
            if (!"buy a domain and everything else you need at namecheap.com".equals("stringer is a good obfuscator")) {
                  ;
            }

            super();
            this.index = 1882141963 << 3 & 1383173786 ^ 5214662 ^ 4069854;
            this.settings = new ArrayList();
            this.name = var1;
            if (((985949034 | 449455625) >>> 1 ^ -1003673040) != 0) {
                  ;
            }

            this.description = var2;
            this.key = var3;
            this.category = var4;
            this.toggled = (boolean)(341836294 << 4 << 2 ^ 402686336);
      }

      static {
            if ((1754037884 >> 1 >> 1 ^ -1533419564) != 0) {
                  ;
            }

      }

      public boolean getState() {
            return this.state;
      }

      public void onDisable() {
            EventBus var10000 = MinecraftForge.EVENT_BUS;
            if (((1141530722 | 820090233) & 1182089359 ^ 1147224075) == 0) {
                  ;
            }

            var10000.unregister(this);
            if (((317020195 >> 4 | 6111391) ^ 25122207) == 0) {
                  ;
            }

      }

      public void setKey(int var1) {
            this.key = var1;
            if (Main.saveLoad != null && ModuleManager.getModuleByName("Config").isEnabled()) {
                  Main.saveLoad.save();
            }

      }

      public void addSettings(Setting... var1) {
            this.settings.addAll(Arrays.asList(var1));
      }

      public Setting getSettingByName(String var1) {
            Iterator var2 = this.settings.iterator();

            Setting var3;
            do {
                  if (!var2.hasNext()) {
                        return null;
                  }

                  Object var10000 = var2.next();
                  if (!"idiot".equals("you probably spell youre as your")) {
                        ;
                  }

                  var3 = (Setting)var10000;
            } while(var3.name != var1);

            return var3;
      }

      public void setup() {
      }

      public void onUpdate() {
      }

      public void onLocalPlayerUpdate() {
            if (!"please go outside".equals("please take a shower")) {
                  ;
            }

      }

      public void setToggled(boolean var1) {
            if (!"minecraft".equals("idiot")) {
                  ;
            }

            this.toggled = var1;
            if (var1) {
                  this.onEnable();
            } else {
                  this.onDisable();
            }

            if (Main.saveLoad != null && ModuleManager.getModuleByName("Config").isEnabled()) {
                  Main.saveLoad.save();
            }

      }

      public void setState(boolean var1) {
            if (var1) {
                  this.state = (boolean)((0 & 1556667616 ^ 561495159) >>> 3 >> 4 ^ 4386681);
                  this.onEnable();
            } else {
                  this.state = (boolean)((1698069439 | 604807809) & 933864295 ^ 505975203 ^ 989868676);
                  if (!"ape covered in human flesh".equals("please go outside")) {
                        ;
                  }

                  this.onDisable();
            }

      }

      public boolean isEnabled() {
            boolean var10000 = this.toggled;
            if ((2021020320 << 2 >> 3 << 1 ^ 2134711053) != 0) {
                  ;
            }

            return var10000;
      }

      public boolean isToggled() {
            return this.toggled;
      }

      public void onPlaySound(PlaySoundEvent var1) {
      }

      public String getName() {
            return this.name;
      }

      public static enum Category {
            MOVEMENT("MOVEMENT"),
            COMBAT("COMBAT"),
            RENDER("RENDER"),
            PLAYER("PLAYER"),
            WORLD("MISC"),
            GHOST("GHOST"),
            HUD("HUD");

            public String name;
            public int moduleIndex;

            private Category(String name) {
                  this.name = name;
            }

            public static int size(Module.Category c) {
                  int i = 0;
                  ModuleManager var10000 = Main.moduleManager;
                  Iterator var2 = ModuleManager.getModuleList().iterator();

                  while(var2.hasNext()) {
                        Module m = (Module)var2.next();
                        if (m.getCategory() == c) {
                              ++i;
                        }
                  }

                  return i;
            }

            public static int placeInListRender(Module m) {
                  int i = 2;
                  ModuleManager var10000 = Main.moduleManager;
                  Iterator var2 = ModuleManager.getModuleList().iterator();

                  while(true) {
                        while(var2.hasNext()) {
                              Module module = (Module)var2.next();
                              if (module.getCategory().equals(RENDER) && !module.equals(m)) {
                                    ++i;
                              } else if (module.getCategory().equals(RENDER) && module.equals(m)) {
                                    return i;
                              }
                        }

                        return 2;
                  }
            }

            public static int placeInListMovement(Module m) {
                  int i = 2;
                  ModuleManager var10000 = Main.moduleManager;
                  Iterator var2 = ModuleManager.getModuleList().iterator();

                  while(true) {
                        while(var2.hasNext()) {
                              Module module = (Module)var2.next();
                              if (module.getCategory().equals(MOVEMENT) && !module.equals(m)) {
                                    ++i;
                              } else if (module.getCategory().equals(MOVEMENT) && module.equals(m)) {
                                    return i;
                              }
                        }

                        return 2;
                  }
            }

            public static int placeInListCombat(Module m) {
                  int i = 2;
                  ModuleManager var10000 = Main.moduleManager;
                  Iterator var2 = ModuleManager.getModuleList().iterator();

                  while(true) {
                        while(var2.hasNext()) {
                              Module module = (Module)var2.next();
                              if (module.getCategory().equals(COMBAT) && !module.equals(m)) {
                                    ++i;
                              } else if (module.getCategory().equals(COMBAT) && module.equals(m)) {
                                    return i;
                              }
                        }

                        return 2;
                  }
            }

            public static int placeInListPlayer(Module m) {
                  int i = 2;
                  ModuleManager var10000 = Main.moduleManager;
                  Iterator var2 = ModuleManager.getModuleList().iterator();

                  while(true) {
                        while(var2.hasNext()) {
                              Module module = (Module)var2.next();
                              if (module.getCategory().equals(PLAYER) && !module.equals(m)) {
                                    ++i;
                              } else if (module.getCategory().equals(PLAYER) && module.equals(m)) {
                                    return i;
                              }
                        }

                        return 2;
                  }
            }
      }
}
