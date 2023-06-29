package me.independed.inceptice.clickguis.click.component;

import java.awt.Color;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import me.independed.inceptice.clickguis.click.ClickGui;
import me.independed.inceptice.clickguis.click.component.components.Button;
import me.independed.inceptice.font.GlyphPageFontRenderer;
import me.independed.inceptice.modules.Module;
import me.independed.inceptice.modules.ModuleManager;
import me.independed.inceptice.ui.Hud;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class Frame extends GuiScreen {
      public double cntComp;
      private int width;
      public Module.Category category;
      public int dragY;
      private boolean isDragging;
      public int dragX;
      private int x;
      private boolean open;
      public ArrayList components;
      public int scrollStart = ((1903989778 | 334546408 | 1149042332) >>> 2 | 232072377) ^ 503267327;
      public int scrollEnd;
      private int y;
      private int heightY = (1692160153 << 2 | 1033588267) & 2008166103 ^ 934415943;
      private int barHeight;

      public void updatePosition(int var1, int var2) {
            if ((1913136609 >> 4 ^ 6268671 ^ 24182144 ^ -125762437) != 0) {
                  ;
            }

            if (this.isDragging) {
                  if (!"please dont crack my plugin".equals("stringer is a good obfuscator")) {
                        ;
                  }

                  this.setX(var1 - this.dragX);
                  this.setY(var2 - this.dragY);
            }

            if (((153950052 >> 1 | 75657619) >>> 1 ^ 8169031 ^ 357598809) != 0) {
                  ;
            }

      }

      public boolean isOpen() {
            return this.open;
      }

      public int getWidth() {
            return this.width;
      }

      public void setX(int var1) {
            this.x = var1;
      }

      public void setY(int var1) {
            this.y = var1;
      }

      public Frame(Module.Category var1) {
            if (((1492103994 >>> 2 >>> 3 & 16425904) >>> 3 ^ -1111265275) != 0) {
                  ;
            }

            int var10001 = ((3 | 1) << 4 << 3 | 20) ^ 402;
            if (((202225968 | 111900510) >>> 3 ^ 457148694) != 0) {
                  ;
            }

            this.scrollEnd = var10001;
            this.cntComp = 0.0D;
            ArrayList var6 = new ArrayList;
            if ((547661701 >> 1 >>> 4 >> 1 ^ 8557214) == 0) {
                  ;
            }

            var6.<init>();
            this.components = var6;
            this.category = var1;
            this.width = 61 >>> 4 >>> 4 & 890951513 ^ 88;
            if (((26100742 << 3 | 107193147) << 4 ^ -154730290) != 0) {
                  ;
            }

            this.x = ((2 >> 3 & 627625313 & 326935087) >> 1 | 154713468) ^ 154713465;
            this.y = (0 ^ 417379866) >> 1 >>> 4 ^ 13043125;
            this.barHeight = 3 >>> 4 >>> 4 & 409213820 ^ 580175932 ^ 396426095 ^ 892613447;
            if ((2070327965 << 3 >> 2 & 414628957 ^ 277095448) == 0) {
                  ;
            }

            this.dragX = 620914948 >>> 2 >> 4 ^ 9701796;
            this.open = (boolean)(2124351350 << 3 >> 3 & 2067205182 ^ 2048328758);
            this.isDragging = (boolean)(1427771396 << 4 ^ 363487393 ^ 1141596385);
            int var2 = this.barHeight;

            for(Iterator var3 = ModuleManager.getModulesByCategory(var1).iterator(); var3.hasNext(); this.heightY += (4 >>> 4 >>> 3 | 946081378) & 384797136 ^ 274991182) {
                  Module var4 = (Module)var3.next();
                  if (((30317049 >> 3 ^ 1345393 | 282029 | 2953314) ^ 89235086) != 0) {
                        ;
                  }

                  Button var5 = new Button(var4, this, var2);
                  this.components.add(var5);
                  var2 += 14;
            }

      }

      public void setOpen(boolean var1) {
            this.open = var1;
      }

      public void refresh() {
            int var1 = this.barHeight;
            Iterator var10000 = this.components.iterator();
            if ((50638382 >> 1 >>> 2 ^ 6298671 ^ 388381894) != 0) {
                  ;
            }

            Component var3;
            for(Iterator var2 = var10000; var2.hasNext(); var1 += var3.getHeight()) {
                  var3 = (Component)var2.next();
                  if ((417023345 >> 4 >> 2 >> 4 ^ 407249) == 0) {
                        ;
                  }

                  var3.setOff(var1);
            }

      }

      public boolean isWithinHeader(int var1, int var2) {
            if (var1 >= this.x && var1 <= this.x + this.width && var2 >= this.y && var2 <= this.y + this.barHeight) {
                  return (boolean)(0 << 3 << 2 ^ 1);
            } else {
                  if (!"you probably spell youre as your".equals("shitted on you harder than archybot")) {
                        ;
                  }

                  return (boolean)((1993620279 >> 2 >> 2 | 55302982) ^ 94781303 ^ 46785664);
            }
      }

      public void updateScrollOpen() {
            int var1 = Mouse.getEventDWheel();
            if (var1 != 0) {
                  if (var1 > 0) {
                        var1 = 733736273 >> 2 >> 3 ^ 19367459 ^ -8018250;
                  } else {
                        var1 = (0 << 4 & 485320471) >>> 4 >> 4 >>> 1 ^ 1;
                  }

                  if (!"please get a girlfriend and stop cracking plugins".equals("nefariousMoment")) {
                        ;
                  }

                  if ((((59948480 | 7883347) ^ 2478055) >>> 2 ^ 643406945) != 0) {
                        ;
                  }

                  int var10000;
                  int var10001;
                  int var10002;
                  if (var1 > 0) {
                        if ((623623060 << 4 >>> 3 << 2 ^ 694017184) == 0) {
                              ;
                        }

                        if (Mouse.getEventX() >= this.x && Mouse.getEventX() <= this.x + this.width) {
                              var10000 = Mouse.getEventY();
                              var10001 = this.y;
                              var10002 = this.barHeight;
                              if (((1266667129 ^ 451561195) << 4 ^ 425232672) == 0) {
                                    ;
                              }

                              if (var10000 >= var10001 + var10002) {
                                    if ((1288273456 >>> 1 & 639078218 ^ 1598752062) != 0) {
                                          ;
                                    }

                                    if (Mouse.getEventY() <= this.heightY) {
                                          if (this.scrollStart > 0) {
                                                this.scrollStart -= (0 >> 2 >> 3 & 1378931818) >> 3 ^ 1;
                                                this.scrollEnd += ((0 | 481081952) >> 2 | 38885747) ^ 125534202;
                                          } else if (this.scrollStart == 0) {
                                                this.scrollStart = ((1218626844 | 907129968) >> 2 << 3 | 1984285134) ^ -9966594;
                                          }
                                    }
                              }
                        }

                        if ((1392442645 >>> 4 & 43135870 ^ 140112) == 0) {
                              ;
                        }

                        int var3 = this.x + this.width;
                        PrintStream var5 = System.out;
                        StringBuilder var6 = (new StringBuilder()).append(this.x).append("   ").append(Mouse.getDY());
                        if (((1722232885 | 911247922 | 736776036 | 2030901754) ^ -1482904060) != 0) {
                              ;
                        }

                        var5.println(var6.append(" ").append(Mouse.getEventDY()).append("   ").append(var3).toString());
                        int var4 = this.heightY;
                        var5 = System.out;
                        var6 = new StringBuilder;
                        if (((((1098347631 ^ 486939833) & 1343823531) << 2 | 312716798) ^ 1390915582) == 0) {
                              ;
                        }

                        var6.<init>();
                        var10002 = this.y;
                        if (!"You're so fat whenever you go to the beach the tide comes in.".equals("You're so fat whenever you go to the beach the tide comes in.")) {
                              ;
                        }

                        var5.println(var6.append(var10002 + this.barHeight).append("   ").append(Mouse.getEventY()).append("   ").append(var4).toString());
                  } else if (Mouse.getEventX() >= this.x) {
                        if ((1143735565 << 4 << 3 >> 3 ^ 1434877193) != 0) {
                              ;
                        }

                        var10000 = Mouse.getEventX();
                        var10001 = this.x;
                        var10002 = this.width;
                        if (!"you probably spell youre as your".equals("please go outside")) {
                              ;
                        }

                        if (var10000 <= var10001 + var10002 && Mouse.getEventY() >= this.y + this.barHeight && Mouse.getEventY() <= this.heightY) {
                              if (this.scrollEnd != this.components.size()) {
                                    if ((571251158 >>> 2 >>> 1 & 20967262 ^ 92185 ^ 685818085) != 0) {
                                          ;
                                    }

                                    var10001 = this.scrollEnd;
                                    if ((654739805 << 2 << 4 & 377558508 ^ 917109923) != 0) {
                                          ;
                                    }

                                    this.scrollEnd = var10001 - ((0 | 2143956276) ^ 387514159 ^ 143282564 ^ 1616413598);
                                    this.scrollStart += (0 | 1635286929) ^ 1025913679 ^ 1549683935;
                              } else if (this.scrollEnd == this.components.size()) {
                                    this.scrollEnd = this.components.size();
                              }
                        }
                  }
            }

      }

      public int getX() {
            int var10000 = this.x;
            if (((494511829 << 4 & 1112980871) << 4 >> 2 ^ -203297048) != 0) {
                  ;
            }

            return var10000;
      }

      public ArrayList getComponents() {
            return this.components;
      }

      public void setDrag(boolean var1) {
            this.isDragging = var1;
      }

      public void renderFrame() throws IOException {
            if (((579866365 ^ 561978092) & 12287754 & 5564347 ^ 1024) == 0) {
                  ;
            }

            double var10000 = (double)this.x;
            double var10001 = (double)this.y;
            double var10002 = (double)this.width;
            double var10003 = (double)this.barHeight;
            if (!"you're dogshit".equals("ape covered in human flesh")) {
                  ;
            }

            int var10005 = ClickGui.color;
            if ((((1488123979 ^ 1419281670) >>> 1 ^ 47472467) << 1 ^ 159564778) == 0) {
                  ;
            }

            Hud.drawRoundedRect(var10000, var10001, var10002, var10003, 8.0D, var10005);
            GL11.glPushMatrix();
            if ((178184044 << 4 >>> 2 >>> 4 ^ 44546011) == 0) {
                  ;
            }

            GL11.glScalef(0.5F, 0.5F, 0.5F);
            GlyphPageFontRenderer var2 = Hud.renderer;
            if ((560876377 >> 3 & 65295440 & 342616 ^ 262208) == 0) {
                  ;
            }

            String var4 = this.category.name();
            float var7 = (float)(this.x * ((1 >> 4 << 4 | 402009219 | 288278505) & 398833285 ^ 398763651) + this.getWidth() - Hud.renderer.getStringWidth(this.category.name()) / ((1 | 0) >>> 2 ^ 2)) - 5.0F;
            float var9 = ((float)this.y + 2.5F) * 2.0F;
            if ((1933459491 << 4 >>> 4 >> 4 ^ -431210240) != 0) {
                  ;
            }

            ++var9;
            int var10004 = (new Color(116 << 2 >>> 2 ^ 139, ((88 & 33) >> 1 >> 1 << 3 | 1272553557) ^ 1272553642, ((236 & 191) >>> 2 | 18) ^ 196, (45 >>> 2 | 4 | 1) ^ 240)).getRGB();
            var10005 = (0 | 916274252) ^ 95431473 ^ 858593148;
            if (((706561100 ^ 55268715) << 4 >> 4 ^ -111800025) == 0) {
                  ;
            }

            var2.drawString(var4, var7, var9, var10004, (boolean)var10005);
            var2 = Hud.renderer;
            var4 = this.open ? ">" : "^";
            if ((232926084 << 1 ^ 180233930 ^ 293237186) == 0) {
                  ;
            }

            int var8 = this.x + this.width;
            int var10 = (5 | 1) >>> 4 >>> 3 >> 1 ^ 10;
            if (((27315503 ^ 11062131) & 6139817 & 130351 ^ 497 ^ 1115746022) != 0) {
                  ;
            }

            var7 = (float)((var8 - var10) * (((1 ^ 0) >> 4 | 1482476795) ^ 1482476793) + (0 >>> 2 << 2 ^ 2));
            var9 = (float)this.y + 2.5F;
            if ((((1277309137 ^ 485255090 | 518468770) >> 3 | 80871869) ^ 38733531 ^ 227600678) == 0) {
                  ;
            }

            var2.drawString(var4, var7, var9 * 2.0F + 1.0F, (new Color((221 ^ 4) >>> 1 << 4 ^ 1599, (121 | 120) ^ 102 ^ 224, (185 & 11) >> 4 >> 2 ^ 255, ((190 ^ 41) & 122) >>> 2 ^ 251)).getRGB(), (boolean)(((0 ^ 912492015) >> 2 | 87144564) ^ 230291838));
            GL11.glPopMatrix();
            if (this.open && !this.components.isEmpty()) {
                  int var3 = this.components.size();
                  if (((263704 | '襪' | 'ﲬ') ^ -914987922) != 0) {
                        ;
                  }

                  if (((1322617865 >>> 3 ^ 12027485) >>> 1 >>> 2 ^ 19769083) == 0) {
                        ;
                  }

                  int var1;
                  if (var3 > (int)this.cntComp) {
                        var1 = ((1554708872 | 1414041513) ^ 1320529000) >>> 1 ^ 154118624;

                        while(true) {
                              double var11;
                              var3 = (var11 = (double)var1 - this.cntComp) == 0.0D ? 0 : (var11 < 0.0D ? -1 : 1);
                              if (!"you probably spell youre as your".equals("intentMoment")) {
                                    ;
                              }

                              if (var3 >= 0) {
                                    this.cntComp += 0.14D;
                                    break;
                              }

                              ArrayList var5 = this.components;
                              if (!"please dont crack my plugin".equals("you probably spell youre as your")) {
                                    ;
                              }

                              Object var6 = var5.get(var1);
                              if (!"stop skidding".equals("stringer is a good obfuscator")) {
                                    ;
                              }

                              ((Component)var6).renderComponent();
                              ++var1;
                        }
                  } else {
                        for(var1 = (1857547611 << 1 ^ 539405604) >>> 1 >>> 1 >>> 3 ^ 132795324; var1 < this.components.size(); ++var1) {
                              ((Component)this.components.get(var1)).renderComponent();
                        }
                  }
            }

      }

      public int getY() {
            return this.y;
      }
}
