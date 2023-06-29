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
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class ClickGui extends GuiScreen {
      int r;
      public static int color;
      private TimerUtil timerRend;
      int g;
      public static ArrayList frames;
      int vX;
      int b;
      private ParticleSystem particleSystem;
      int bRad;

      protected void mouseClicked(int var1, int var2, int var3) throws IOException {
            Iterator var4 = frames.iterator();

            while(true) {
                  Frame var5;
                  do {
                        do {
                              if (!var4.hasNext()) {
                                    return;
                              }

                              var5 = (Frame)var4.next();
                              if (((((1683958140 | 1668586373) & 1690751759) << 3 | 417930809) ^ 989699705) == 0) {
                                    ;
                              }

                              if (var5.isWithinHeader(var1, var2) && var3 == 0) {
                                    var5.setDrag((boolean)(((0 | 459587825) ^ 260993292) >>> 1 & 106342069 & 36911507 ^ 34603153));
                                    var5.dragX = var1 - var5.getX();
                                    var5.dragY = var2 - var5.getY();
                              }

                              if (var5.isWithinHeader(var1, var2) && var3 == ((0 >> 3 >>> 3 | 1522404862) ^ 1522404863)) {
                                    var5.setOpen((boolean)(!var5.isOpen() ? ((0 | 2075673336) >>> 3 | 212540888) ^ 268377566 : ((2076075923 | 1740227174) >> 3 ^ 43601823) << 4 ^ -689228272));
                                    if ((1854108627 >> 3 << 3 ^ 409053615 ^ 69302863) != 0) {
                                          ;
                                    }
                              }

                              if (!"i hope you catch fire ngl".equals("please take a shower")) {
                                    ;
                              }
                        } while(!var5.isOpen());
                  } while(var5.getComponents().isEmpty());

                  Iterator var10000 = var5.getComponents().iterator();
                  if ((((439888365 | 193424839) << 3 >>> 2 | 353304504 | 76134776) ^ 939524094) == 0) {
                        ;
                  }

                  Iterator var6 = var10000;

                  while(true) {
                        if (!"yo mama name maurice".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                              ;
                        }

                        if (!var6.hasNext()) {
                              break;
                        }

                        Component var7 = (Component)var6.next();
                        var7.mouseClicked(var1, var2, var3);
                  }
            }
      }

      public ClickGui() throws IOException {
            if (!"shitted on you harder than archybot".equals("ape covered in human flesh")) {
                  ;
            }

            super();
            if (!"please dont crack my plugin".equals("ape covered in human flesh")) {
                  ;
            }

            int var10001 = 1891668288 << 3 >>> 4 << 3 ^ 1124222208;
            if (!"stop skidding".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                  ;
            }

            this.r = var10001;
            if (((569345101 >> 4 & 32679278 ^ 313571) & 718097 ^ 155649) == 0) {
                  ;
            }

            this.g = (849404677 >>> 4 << 2 | 52453740) ^ 262699500;
            this.b = (436130008 >> 3 & 50813643 ^ 22405609) & 17432543 ^ '믂';
            var10001 = ((1280133774 ^ 270145022) & 1305040249) >>> 4 ^ 79959383;
            if (((14880773 >> 2 >>> 3 ^ '\uf234' | 321602) ^ 518902) == 0) {
                  ;
            }

            this.vX = var10001;
            this.bRad = (1595623629 ^ 885472180) << 1 ^ -675748110;
            this.timerRend = new TimerUtil();
            ParticleSystem var7 = new ParticleSystem;
            if (((151099800 | 59944528 | 161688911) ^ 730655081) != 0) {
                  ;
            }

            var7.<init>(351 >>> 1 ^ 11 ^ 308);
            this.particleSystem = var7;
            if (!"shitted on you harder than archybot".equals("please dont crack my plugin")) {
                  ;
            }

            frames = new ArrayList();
            int var1 = (28 >> 1 >>> 2 | 1 | 2) ^ 49;
            Module.Category[] var2 = (Module.Category[])Category.values();
            int var3 = var2.length;
            int var4 = (1149474288 ^ 430418992 ^ 873108255) & 647377751 & 53510790 ^ 518;

            while(true) {
                  if ("ape covered in human flesh".equals("nefariousMoment")) {
                  }

                  if (var4 >= var3) {
                        return;
                  }

                  Module.Category var5 = var2[var4];
                  Frame var6 = new Frame(var5);
                  var6.setX(((90 | 54) >> 1 ^ 16) >> 2 ^ 164);
                  var6.setY(var1);
                  frames.add(var6);
                  var1 += 35;
                  ++var4;
            }
      }

      public boolean doesGuiPauseGame() {
            return (boolean)(60 ^ 21 ^ 41);
      }

      public void drawScreen(int var1, int var2, float var3) {
            int var10001;
            if (ModuleManager.getModuleByName("GuiParticles").isToggled()) {
                  ParticleSystem var10000 = this.particleSystem;
                  var10001 = (8 >> 3 >>> 3 ^ 1301346800) >> 2 ^ 325336694;
                  if (!"idiot".equals("idiot")) {
                        ;
                  }

                  var10000.tick(var10001);
                  this.particleSystem.render();
            }

            if ((1547889646 >> 1 & 132481998 & 64881908 ^ 16731392 ^ 50286020) == 0) {
                  ;
            }

            new ScaledResolution(this.mc);
            if (((1259074501 ^ 1110358792) >>> 4 << 3 ^ 2052627916) != 0) {
                  ;
            }

            int[] var14 = new int[((0 | 1798369869) & 443233266) >> 1 ^ 84941089];
            var14[((949341999 | 609920761) & 591571385) >> 3 ^ 67654967] = 0 >>> 4 ^ 758343862 ^ 758343863;
            int[] var5 = var14;
            Iterator var6 = ModuleManager.getModuleByName("ClickGui").settings.iterator();

            while(true) {
                  boolean var15 = var6.hasNext();
                  if ((1291297694 << 4 ^ 1029702609 ^ 1542529534 ^ 242079221) != 0) {
                        ;
                  }

                  if (!var15) {
                        break;
                  }

                  Setting var7 = (Setting)var6.next();
                  if (!"shitted on you harder than archybot".equals("idiot")) {
                        ;
                  }

                  if (var7 instanceof NumberSetting) {
                        NumberSetting var8 = (NumberSetting)var7;
                        if (var8.name == "Red") {
                              var10001 = (int)var8.getValue();
                              if (((242856651 >> 1 | 71886479) ^ -1758854346) != 0) {
                                    ;
                              }

                              this.r = var10001;
                        }

                        if (var8.name == "Green") {
                              var10001 = (int)var8.getValue();
                              if (!"stringer is a good obfuscator".equals("please get a girlfriend and stop cracking plugins")) {
                                    ;
                              }

                              this.g = var10001;
                        }

                        if (var8.name == "Blue") {
                              this.b = (int)var8.getValue();
                        }
                  }

                  var15 = var7 instanceof ModeSetting;
                  if (!"you're dogshit".equals("nefariousMoment")) {
                        ;
                  }

                  if (var15) {
                        if (((1743428736 ^ 1036605300 | 587654174) ^ 209060319 ^ -1234156181) != 0) {
                              ;
                        }

                        ModeSetting var12 = (ModeSetting)var7;
                        if (var12.activeMode == "rainbow") {
                              color = Hud.rainbow(var5[(1572632016 ^ 1515922373 ^ 83641910) >>> 2 ^ 13026312] * ((271 & 139) >> 3 ^ 301));
                              break;
                        }
                  }

                  Color var16 = new Color;
                  if (((18997824 ^ 13532130) >> 2 ^ 186495922) != 0) {
                        ;
                  }

                  int var10002 = this.r;
                  int var10003 = this.g;
                  if ((77873436 << 4 >> 1 ^ 315202162 ^ 1137998595) != 0) {
                        ;
                  }

                  var16.<init>(var10002, var10003, this.b, ((229 & 153) >>> 4 | 6) ^ 241);
                  color = var16.getRGB();
            }

            var6 = frames.iterator();

            while(true) {
                  if ("stringer is a good obfuscator".equals("buy a domain and everything else you need at namecheap.com")) {
                  }

                  if (!var6.hasNext()) {
                        return;
                  }

                  if (((2884897 | 2010706) ^ 776286239) == 0) {
                  }

                  Frame var11 = (Frame)var6.next();

                  try {
                        var11.renderFrame();
                  } catch (IOException var10) {
                        if ("buy a domain and everything else you need at namecheap.com".equals("i hope you catch fire ngl")) {
                        }

                        if (!"You're so fat whenever you go to the beach the tide comes in.".equals("stop skidding")) {
                              ;
                        }

                        if (((512 | 81) & 526 ^ -1814659524) != 0) {
                              ;
                        }

                        var10.printStackTrace();
                        if (((175406716 ^ 142186829) << 1 >> 4 ^ 4308198) == 0) {
                              ;
                        }
                  }

                  var11.updatePosition(var1, var2);

                  Component var9;
                  for(Iterator var13 = var11.getComponents().iterator(); var13.hasNext(); var9.updateComponent(var1, var2)) {
                        if (!"please get a girlfriend and stop cracking plugins".equals("please dont crack my plugin")) {
                              ;
                        }

                        if ((1814045543 >> 3 & 76156167 ^ 70707686 ^ -807456489) != 0) {
                              ;
                        }

                        var9 = (Component)var13.next();
                        if (((1993044295 ^ 837320312 | 632225261) << 1 ^ -815792130) == 0) {
                              ;
                        }
                  }
            }
      }

      protected void keyTyped(char var1, int var2) {
            Iterator var3 = frames.iterator();

            while(true) {
                  Frame var4;
                  do {
                        do {
                              do {
                                    if (!var3.hasNext()) {
                                          if (var2 == ((0 << 3 ^ 1899395227) >> 1 << 4 >>> 4 ^ 144391244)) {
                                                this.mc.displayGuiScreen((GuiScreen)null);
                                          }

                                          if (((282930774 >>> 1 & 10734601) >> 3 ^ 557954487) != 0) {
                                                ;
                                          }

                                          return;
                                    }

                                    var4 = (Frame)var3.next();
                              } while(!var4.isOpen());
                        } while(var2 == ((0 ^ 1433266193) << 3 << 3 ^ 1534723137));

                        if (((1656059954 ^ 243811847 | 416412468) ^ 2097020725) == 0) {
                              ;
                        }
                  } while(var4.getComponents().isEmpty());

                  Iterator var5 = var4.getComponents().iterator();

                  while(var5.hasNext()) {
                        Component var6 = (Component)var5.next();
                        var6.keyTyped(var1, var2);
                        if ((((1356272112 >> 1 ^ 492031599) >>> 3 | 83558220) ^ 117440478) == 0) {
                              ;
                        }
                  }
            }
      }

      protected void mouseReleased(int var1, int var2, int var3) {
            if (((643298631 << 3 & 295136072) >>> 2 ^ 1540841979) != 0) {
                  ;
            }

            Iterator var4;
            Frame var5;
            for(var4 = frames.iterator(); var4.hasNext(); var5.setDrag((boolean)(951954814 >> 3 >> 3 & 7825770 ^ 6447136))) {
                  var5 = (Frame)var4.next();
                  if (!"please take a shower".equals("you probably spell youre as your")) {
                        ;
                  }
            }

            if ((134218240 >> 2 << 4 >>> 2 ^ 1338727090) != 0) {
                  ;
            }

            var4 = frames.iterator();

            while(true) {
                  do {
                        do {
                              if (!var4.hasNext()) {
                                    if (!"please take a shower".equals("your mom your dad the one you never had")) {
                                          ;
                                    }

                                    return;
                              }

                              Frame var10000 = (Frame)var4.next();
                              if (!"i hope you catch fire ngl".equals("stop skidding")) {
                                    ;
                              }

                              var5 = var10000;
                              if (((725609735 | 81180207) << 2 << 3 >>> 1 ^ 2147480304) == 0) {
                                    ;
                              }
                        } while(!var5.isOpen());
                  } while(var5.getComponents().isEmpty());

                  Iterator var6 = var5.getComponents().iterator();

                  while(var6.hasNext()) {
                        if ((184943712 << 1 ^ 670336284) != 0) {
                              ;
                        }

                        Component var7 = (Component)var6.next();
                        var7.mouseReleased(var1, var2, var3);
                  }
            }
      }

      public void initGui() {
            if (OpenGlHelper.shadersSupported && this.mc.getRenderViewEntity() instanceof EntityPlayer) {
                  if (this.mc.entityRenderer.getShaderGroup() != null) {
                        this.mc.entityRenderer.getShaderGroup().deleteShaderGroup();
                  }

                  if (!"please get a girlfriend and stop cracking plugins".equals("idiot")) {
                        ;
                  }

                  this.mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/blur.json"));
            }

      }

      public void onGuiClosed() {
            if (this.mc.entityRenderer.getShaderGroup() != null) {
                  if (((1298924616 | 1039220793) >> 1 >>> 2 ^ -137875845) != 0) {
                        ;
                  }

                  this.mc.entityRenderer.getShaderGroup().deleteShaderGroup();
                  this.mc.entityRenderer.stopUseShader();
            }

            this.bRad = (1145065473 >> 1 ^ 480252633) & 444073085 ^ 436207705;
            if ((1718572403 << 4 >> 3 << 4 ^ 1538801089 ^ -474306248) != 0) {
                  ;
            }

            this.vX = (1125588304 ^ 425254512) & 719572018 ^ 43172033 ^ 147914977;
      }
}
