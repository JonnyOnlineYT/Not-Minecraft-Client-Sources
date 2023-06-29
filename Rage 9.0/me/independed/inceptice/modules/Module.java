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
      public List settings = new ArrayList();
      public boolean expanded;
      public String description;
      public int index = 1446011741 << 2 >> 1 << 1 ^ 399164320 ^ 1326141140;
      private boolean state;
      public int key;
      private boolean visible;
      public boolean toggled;
      public Module.Category category;
      protected static final Minecraft mc = Minecraft.getMinecraft();
      public String name;

      public String getActiveModeSetting() {
            Iterator var1 = this.settings.iterator();

            Setting var2;
            do {
                  if (!var1.hasNext()) {
                        return null;
                  }

                  if ((1929343632 << 3 >> 3 ^ -218140016) == 0) {
                        ;
                  }

                  var2 = (Setting)var1.next();
            } while(!(var2 instanceof ModeSetting));

            ModeSetting var3 = (ModeSetting)var2;
            return var3.activeMode;
      }

      public String getDescription() {
            String var10000 = this.description;
            if ((989027222 << 1 << 3 >>> 1 ^ 1469766832) == 0) {
                  ;
            }

            return var10000;
      }

      public void onDisable() {
            MinecraftForge.EVENT_BUS.unregister(this);
            if (!"ape covered in human flesh".equals("i hope you catch fire ngl")) {
                  ;
            }

      }

      public Setting getSettingByName(String var1) {
            Iterator var10000 = this.settings.iterator();
            if ((2059711257 << 1 >>> 4 >>> 1 ^ 128731953) == 0) {
                  ;
            }

            Iterator var2 = var10000;

            Setting var3;
            String var4;
            do {
                  if (((1891656920 | 1534099706) & 1584694485 ^ 1517323472) != 0) {
                  }

                  if (!var2.hasNext()) {
                        return null;
                  }

                  var3 = (Setting)var2.next();
                  var4 = var3.name;
                  if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("nefariousMoment")) {
                        ;
                  }
            } while(var4 != var1);

            return var3;
      }

      public void onPlaySound(PlaySoundEvent var1) {
            if (!"buy a domain and everything else you need at namecheap.com".equals("idiot")) {
                  ;
            }

      }

      public void addSettings(Setting... var1) {
            if (!"please dont crack my plugin".equals("ape covered in human flesh")) {
                  ;
            }

            List var10000 = this.settings;
            if ((1035551864 << 4 >> 2 & 575316702 ^ -1971121932) != 0) {
                  ;
            }

            var10000.addAll(Arrays.asList(var1));
      }

      public String getName() {
            return this.name;
      }

      public void toggle() {
            int var10001;
            if (!this.toggled) {
                  if ((136904768 >>> 4 & 1834857 ^ 167936) == 0) {
                        ;
                  }

                  var10001 = (0 ^ 717982376) << 1 ^ 1435964753;
            } else {
                  var10001 = 185076962 >>> 2 ^ 46269240;
            }

            this.toggled = (boolean)var10001;
            if (this.toggled) {
                  this.onEnable();
            } else {
                  this.onDisable();
            }

            if (Main.saveLoad != null) {
                  Module var10000 = ModuleManager.getModuleByName("Config");
                  if ((151519768 >>> 4 >>> 2 ^ 2367496) == 0) {
                        ;
                  }

                  if (var10000.isEnabled()) {
                        Main.saveLoad.save();
                        if (!"please dont crack my plugin".equals("your mom your dad the one you never had")) {
                              ;
                        }
                  }
            }

      }

      public void setDescription(String var1) {
            if ((865637728 << 1 << 4 ^ 811108525 ^ 1952615029) != 0) {
                  ;
            }

            this.description = var1;
      }

      public void onLocalPlayerUpdate() {
      }

      public void setup() {
      }

      public boolean getState() {
            return this.state;
      }

      public void setVisible(boolean var1) {
            if (((79880804 ^ 29965521) >> 2 >> 4 ^ 144411821) != 0) {
                  ;
            }

            this.visible = var1;
      }

      public void onRenderWorldLast(float var1) {
      }

      public Module.Category getCategory() {
            if (!"intentMoment".equals("idiot")) {
                  ;
            }

            return this.category;
      }

      public boolean isToggled() {
            return this.toggled;
      }

      public boolean isVisible() {
            if ((1498955007 >>> 4 >> 4 ^ 5855292) == 0) {
                  ;
            }

            if ((1913805047 << 4 >>> 3 >> 4 ^ 1783748 ^ -1355247227) != 0) {
                  ;
            }

            if (((998621334 << 1 | 671778846) << 2 ^ -64084744) == 0) {
                  ;
            }

            return this.visible;
      }

      public void setToggled(boolean var1) {
            if ((2000016606 << 3 << 1 >> 2 ^ 483873656) == 0) {
                  ;
            }

            this.toggled = var1;
            if (var1) {
                  this.onEnable();
            } else {
                  this.onDisable();
            }

            if (((1776164923 ^ 1466325737 | 67253574) >> 1 ^ 526204907) == 0) {
                  ;
            }

            if (Main.saveLoad != null && ModuleManager.getModuleByName("Config").isEnabled()) {
                  Main.saveLoad.save();
            }

      }

      public void setName(String var1) {
            this.name = var1;
      }

      public int getKey() {
            return this.key;
      }

      public void onPlayerTick() {
      }

      public static ArrayList getSortedCategories() {
            ArrayList var0 = new ArrayList();
            Module.Category[] var1 = (Module.Category[])me.independed.inceptice.modules.Module.Category.values();
            int var2 = var1.length;
            if ((2047618983 ^ 960420171 ^ 319345317 ^ 1224760723 ^ 1708345268) != 0) {
                  ;
            }

            for(int var3 = (87315243 << 3 ^ 564123264 ^ 80909983 | 92240007) << 3 ^ 1879035448; var3 < var2; ++var3) {
                  Module.Category var10000 = var1[var3];
                  if (((462321346 << 4 & 206701057 | 74116855) ^ 91094071 ^ 151458496) == 0) {
                        ;
                  }

                  Module.Category var4 = var10000;
                  if ((((1994370576 | 865678175) & 1708603430) >>> 4 >>> 4 ^ 6674232) == 0) {
                        ;
                  }

                  var0.add(var4);
            }

            var0.sort((var0x, var1x) -> {
                  String var10000 = var0x.name;
                  if (((1533486754 ^ 438964319) >>> 3 ^ 136947423) == 0) {
                        ;
                  }

                  String var2 = var10000;
                  String var3 = var1x.name;
                  if ((((113352512 << 2 ^ 407825110 | 5294781) ^ 42065712) & 15095141 ^ -876547545) != 0) {
                        ;
                  }

                  int var4 = Hud.myRenderer.getStringWidth(var3) - Hud.myRenderer.getStringWidth(var2);
                  if (!"stringer is a good obfuscator".equals("you're dogshit")) {
                        ;
                  }

                  int var5;
                  if (var4 != 0) {
                        if ((((529417806 >> 2 | 25981497) & 16563740) >>> 1 ^ 1054599931) != 0) {
                              ;
                        }

                        var5 = var4;
                  } else {
                        var5 = var3.compareTo(var2);
                  }

                  return var5;
            });
            return var0;
      }

      public void onEnable() {
            EventBus var10000 = MinecraftForge.EVENT_BUS;
            if (((41962500 >>> 4 ^ 1555983) << 1 ^ 8353182) == 0) {
                  ;
            }

            var10000.register(this);
      }

      public Module(String var1, String var2, int var3, Module.Category var4) {
            if ((0 >> 3 ^ 0) == 0) {
                  ;
            }

            this.name = var1;
            if (!"i hope you catch fire ngl".equals("please get a girlfriend and stop cracking plugins")) {
                  ;
            }

            this.description = var2;
            this.key = var3;
            if (((392205336 ^ 85252835) << 1 ^ 1460079507) != 0) {
                  ;
            }

            if ((338489805 << 4 >> 4 >>> 1 ^ 862304590) != 0) {
                  ;
            }

            if (((632626696 | 501531668) >> 3 & 31618785 ^ 1040974 ^ 28168335) == 0) {
                  ;
            }

            this.category = var4;
            this.toggled = (boolean)(1246952080 ^ 505030235 ^ 430630335 ^ 1306671476);
            if ((430469 >> 4 << 1 >>> 2 ^ 569500982) != 0) {
                  ;
            }

      }

      public void setKey(int var1) {
            if (((492849784 | 206088175) & 16243511 ^ 6095875 ^ 4050740) == 0) {
                  ;
            }

            this.key = var1;
            if (Main.saveLoad != null) {
                  if (!"buy a domain and everything else you need at namecheap.com".equals("stringer is a good obfuscator")) {
                        ;
                  }

                  Module var10000 = ModuleManager.getModuleByName("Config");
                  if (!"i hope you catch fire ngl".equals("intentMoment")) {
                        ;
                  }

                  if (var10000.isEnabled()) {
                        if (!"ape covered in human flesh".equals("nefariousMoment")) {
                              ;
                        }

                        Main.saveLoad.save();
                  }
            }

      }

      public boolean isEnabled() {
            if (!"please take a shower".equals("shitted on you harder than archybot")) {
                  ;
            }

            return this.toggled;
      }

      public void setState(boolean var1) {
            if (var1) {
                  int var10001 = 0 >> 3 >>> 1 ^ 1;
                  if ((585037372 >>> 3 ^ 7421110 ^ 1054053163) != 0) {
                        ;
                  }

                  this.state = (boolean)var10001;
                  this.onEnable();
            } else {
                  this.state = (boolean)(((864611104 | 259028585) >> 3 | 80074517) ^ 134209533);
                  this.onDisable();
            }

      }

      public void onClientTick() {
            if (!"please go outside".equals("stringer is a good obfuscator")) {
                  ;
            }

      }

      public void onUpdate() {
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
