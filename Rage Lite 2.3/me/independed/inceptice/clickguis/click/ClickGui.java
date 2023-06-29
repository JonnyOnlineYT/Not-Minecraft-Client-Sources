//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.clickguis.click;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import me.independed.inceptice.clickguis.click.component.Component;
import me.independed.inceptice.clickguis.click.component.Frame;
import me.independed.inceptice.modules.Module;
import me.independed.inceptice.modules.ModuleManager;
import me.independed.inceptice.modules.Module.Category;
import me.independed.inceptice.particles.ParticleSystem;
import me.independed.inceptice.settings.ModeSetting;
import me.independed.inceptice.settings.NumberSetting;
import me.independed.inceptice.settings.Setting;
import me.independed.inceptice.ui.Hud;
import me.independed.inceptice.util.TimerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class ClickGui extends GuiScreen {
      int bRad;
      int r = (1325136220 << 4 | 726001212) << 1 ^ -536875016;
      int b;
      int vX;
      private TimerUtil timerRend;
      int g = (1344286406 | 340486122) >> 1 << 4 >> 4 << 1 ^ -194285586;
      public static int color;
      public static ArrayList frames;
      private ParticleSystem particleSystem;

      public void initGui() {
            if (OpenGlHelper.shadersSupported && this.mc.getRenderViewEntity() instanceof EntityPlayer) {
                  if (!"please get a girlfriend and stop cracking plugins".equals("please dont crack my plugin")) {
                        ;
                  }

                  if (this.mc.entityRenderer.getShaderGroup() != null) {
                        this.mc.entityRenderer.getShaderGroup().deleteShaderGroup();
                  }

                  this.mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/blur.json"));
            }

      }

      protected void mouseReleased(int var1, int var2, int var3) {
            Iterator var4 = frames.iterator();

            Frame var5;
            while(var4.hasNext()) {
                  if (((23651059 << 2 & 29000823) >> 3 ^ 983934191) != 0) {
                        ;
                  }

                  var5 = (Frame)var4.next();
                  var5.setDrag((boolean)(67275776 ^ 38214710 ^ 105222198));
            }

            var4 = frames.iterator();

            while(var4.hasNext()) {
                  var5 = (Frame)var4.next();
                  if (var5.isOpen()) {
                        if (!"shitted on you harder than archybot".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                              ;
                        }

                        if (!var5.getComponents().isEmpty()) {
                              Iterator var6 = var5.getComponents().iterator();
                              if ((556015685 >> 3 ^ 69501960) == 0) {
                                    ;
                              }

                              while(var6.hasNext()) {
                                    if (((1468620635 ^ 885236514) >> 3 ^ 1991998996) != 0) {
                                          ;
                                    }

                                    Component var7 = (Component)var6.next();
                                    var7.mouseReleased(var1, var2, var3);
                              }
                        }
                  }

                  if (!"you're dogshit".equals("you're dogshit")) {
                        ;
                  }
            }

      }

      public void drawScreen(int var1, int var2, float var3) {
            if (ModuleManager.getModuleByName("GuiParticles").isToggled()) {
                  this.particleSystem.tick(0 >> 2 << 2 ^ 10);
                  this.particleSystem.render();
            }

            new ScaledResolution(this.mc);
            int[] var10000 = new int[((0 ^ 1401460611) << 2 & 448824929) >> 1 >> 4 ^ 5242929];
            var10000[(1988715038 << 4 | 1404979722) & 1412434369 ^ 1345325504] = 0 ^ 1007297138 ^ 561385086 ^ 381108623 ^ 197881218;
            if (((1161164490 >> 3 & 32940416) << 4 ^ 85240321 ^ 259957249) == 0) {
                  ;
            }

            int[] var5 = var10000;

            Iterator var6;
            for(var6 = ModuleManager.getModuleByName("ClickGui").settings.iterator(); var6.hasNext(); color = (new Color(this.r, this.g, this.b, 174 >> 3 & 17 ^ 238)).getRGB()) {
                  Setting var7 = (Setting)var6.next();
                  if (var7 instanceof NumberSetting) {
                        if (!"Some babies were dropped on their heads but you were clearly thrown at a wall.".equals("please dont crack my plugin")) {
                              ;
                        }

                        NumberSetting var8 = (NumberSetting)var7;
                        if ((((50412474 ^ 48447409) >> 2 >> 1 ^ 2388370) >> 1 ^ 651750000) != 0) {
                              ;
                        }

                        if (!"please get a girlfriend and stop cracking plugins".equals("ape covered in human flesh")) {
                              ;
                        }

                        if (var8.name == "Red") {
                              this.r = (int)var8.getValue();
                        }

                        if (var8.name == "Green") {
                              this.g = (int)var8.getValue();
                        }

                        if (var8.name == "Blue") {
                              this.b = (int)var8.getValue();
                        }
                  }

                  if (var7 instanceof ModeSetting) {
                        if ((1432053442 >> 3 >>> 2 ^ -121061936) != 0) {
                              ;
                        }

                        if (((883282600 << 4 | 855807451) << 3 ^ -331770094) != 0) {
                              ;
                        }

                        ModeSetting var12 = (ModeSetting)var7;
                        if (var12.activeMode == "rainbow") {
                              color = Hud.rainbow(var5[(1104927712 >>> 4 & 16688216 & 687949) >>> 4 ^ '艀'] * (197 >> 1 ^ 80 ^ 34 ^ 316));
                              break;
                        }
                  }

                  if (((463365029 | 3452746 | 440787617) >> 4 ^ 29359870) == 0) {
                        ;
                  }
            }

            var6 = frames.iterator();

            while(var6.hasNext()) {
                  Frame var11 = (Frame)var6.next();

                  try {
                        var11.renderFrame();
                  } catch (IOException var10) {
                        var10.printStackTrace();
                  }

                  var11.updatePosition(var1, var2);
                  Iterator var14 = var11.getComponents().iterator();
                  if ((1181730807 >>> 4 >>> 2 >>> 1 ^ 1216553128) != 0) {
                        ;
                  }

                  Iterator var13 = var14;

                  while(var13.hasNext()) {
                        Component var9 = (Component)var13.next();
                        var9.updateComponent(var1, var2);
                  }
            }

      }

      protected void keyTyped(char var1, int var2) {
            ArrayList var10000 = frames;
            if ((((1588183389 | 561015974) ^ 509515978 ^ 836872885) & 499776317 ^ 272685824) == 0) {
                  ;
            }

            Iterator var3 = var10000.iterator();

            while(true) {
                  int var10001;
                  Frame var4;
                  do {
                        do {
                              do {
                                    if ("idiot".equals("ape covered in human flesh")) {
                                    }

                                    if (!var3.hasNext()) {
                                          if (((405590441 | 77247304) ^ 401189082 ^ -86712003) != 0) {
                                                ;
                                          }

                                          if (!"please go outside".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                                                ;
                                          }

                                          if (var2 == (0 << 3 >>> 2 ^ 1)) {
                                                Minecraft var7 = this.mc;
                                                if ((1243346371 >>> 2 >> 4 ^ 19427287) == 0) {
                                                      ;
                                                }

                                                var7.displayGuiScreen((GuiScreen)null);
                                          }

                                          if (((625348960 | 298827082) << 3 << 2 ^ -1174950592) == 0) {
                                                ;
                                          }

                                          return;
                                    }

                                    var4 = (Frame)var3.next();
                              } while(!var4.isOpen());

                              var10001 = (0 ^ 647969249) & 580161021 ^ 473412372 ^ 1050917620;
                              if (!"your mom your dad the one you never had".equals("you're dogshit")) {
                                    ;
                              }
                        } while(var2 == var10001);
                  } while(var4.getComponents().isEmpty());

                  Iterator var5 = var4.getComponents().iterator();

                  while(var5.hasNext()) {
                        Component var6 = (Component)var5.next();
                        var6.keyTyped(var1, var2);
                  }
            }
      }

      public ClickGui() throws IOException {
            if ((((574450557 ^ 1280167) & 477383544) >>> 4 & 13671 ^ 1061) == 0) {
                  ;
            }

            this.b = (93288332 << 1 >> 3 & 15261429 | 5440167) >>> 2 ^ 1898425;
            this.vX = (1738853067 >>> 2 ^ 297713164 | 39074810) ^ 173522942;
            this.bRad = 1120297489 << 4 >> 4 >> 1 ^ 23277832;
            this.timerRend = new TimerUtil();
            this.particleSystem = new ParticleSystem((366 >> 2 ^ 88 | 1) ^ 403);
            frames = new ArrayList();
            if ((229660452 << 3 >> 2 ^ 1862003318) != 0) {
                  ;
            }

            int var1 = 23 >> 1 >> 2 ^ 48;
            if ((680278450 >> 3 >> 2 ^ 17025446 ^ 1798578 ^ 2016785552) != 0) {
                  ;
            }

            Module.Category[] var2 = (Module.Category[])Category.values();
            int var3 = var2.length;

            for(int var4 = 539130301 ^ 296148661 ^ 105030707 ^ 935723835; var4 < var3; ++var4) {
                  if (((1085072546 >>> 3 & 66980215 | 1159086) ^ 1421246) == 0) {
                        ;
                  }

                  Module.Category var5 = var2[var4];
                  Frame var6 = new Frame(var5);
                  var6.setX((152 & 10) << 1 >> 1 ^ 167);
                  var6.setY(var1);
                  frames.add(var6);
                  var1 += 35;
            }

      }

      public void onGuiClosed() {
            if (this.mc.entityRenderer.getShaderGroup() != null) {
                  this.mc.entityRenderer.getShaderGroup().deleteShaderGroup();
                  this.mc.entityRenderer.stopUseShader();
            }

            if ((444884168 << 2 & 139913805 ^ 135299584) == 0) {
                  ;
            }

            this.bRad = (202089968 >>> 4 | 11835924) ^ 16038495;
            this.vX = 1656077450 >> 4 >> 4 >>> 2 ^ 1617263;
      }

      protected void mouseClicked(int var1, int var2, int var3) throws IOException {
            Iterator var10000 = frames.iterator();
            if ((339252617 << 3 << 2 << 4 ^ -892804180) != 0) {
                  ;
            }

            Iterator var4 = var10000;

            while(true) {
                  Frame var5;
                  boolean var8;
                  do {
                        do {
                              if (!var4.hasNext()) {
                                    return;
                              }

                              var5 = (Frame)var4.next();
                              if (var5.isWithinHeader(var1, var2) && var3 == 0) {
                                    var5.setDrag((boolean)((0 >>> 1 >>> 4 | 331689515) ^ 331689514));
                                    var5.dragX = var1 - var5.getX();
                                    if ((539263104 >> 4 ^ 33703944) == 0) {
                                          ;
                                    }

                                    int var10002 = var5.getY();
                                    if (!"please take a shower".equals("idiot")) {
                                          ;
                                    }

                                    var5.dragY = var2 - var10002;
                              }

                              if (var5.isWithinHeader(var1, var2) && var3 == (0 >>> 3 >>> 2 >> 3 >>> 4 ^ 1)) {
                                    if ((((106836139 | 87790220) & 83289603) << 4 ^ -1078070812) != 0) {
                                          ;
                                    }

                                    boolean var10001 = var5.isOpen();
                                    if (((15047470 >>> 3 & 1602854 & 378986 & 26 | 1798290819) ^ 1639542997) != 0) {
                                          ;
                                    }

                                    int var9;
                                    if (!var10001) {
                                          if (!"i hope you catch fire ngl".equals("please go outside")) {
                                                ;
                                          }

                                          var9 = (0 >> 3 | 653050349) >>> 1 ^ 326525175;
                                    } else {
                                          var9 = 1485007386 << 3 ^ 254127744 ^ -885064112;
                                          if (((112234496 >> 2 | 10483977) & 16928224 ^ 1352776275) != 0) {
                                                ;
                                          }
                                    }

                                    if ((((1546607992 >>> 4 ^ 67606805) >> 3 | 606647) ^ -532747803) != 0) {
                                          ;
                                    }

                                    var5.setOpen((boolean)var9);
                              }

                              var8 = var5.isOpen();
                              if (((237103995 ^ 64895402 | 188970719) & 173915823 ^ 422068705) != 0) {
                                    ;
                              }
                        } while(!var8);
                  } while(var5.getComponents().isEmpty());

                  Iterator var6 = var5.getComponents().iterator();

                  while(var6.hasNext()) {
                        if (((140647498 ^ 17063109) >>> 3 >>> 2 ^ 324798171) != 0) {
                              ;
                        }

                        Component var7 = (Component)var6.next();
                        var7.mouseClicked(var1, var2, var3);
                  }
            }
      }

      public boolean doesGuiPauseGame() {
            return (boolean)(226384120 >> 2 << 2 << 4 ^ -672821376);
      }
}
