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
      private boolean open;
      public double cntComp = 0.0D;
      public int dragY;
      public int dragX;
      public Module.Category category;
      private int heightY = (419463240 | 320447529 | 71532753) ^ 526099705;
      private int x;
      private int y;
      public int scrollStart = (1415979890 ^ 1403133969) >> 2 >> 4 << 2 ^ 8142900;
      public ArrayList components = new ArrayList();
      public int scrollEnd = (3 ^ 0) >>> 1 ^ 7;
      private int width;
      private int barHeight;
      private boolean isDragging;

      public void setOpen(boolean var1) {
            if (!"stop skidding".equals("idiot")) {
                  ;
            }

            this.open = var1;
            if ((22668378 << 4 ^ 197107832 ^ 269739741) != 0) {
                  ;
            }

      }

      public void setDrag(boolean var1) {
            if (!"you probably spell youre as your".equals("intentMoment")) {
                  ;
            }

            if (!"please take a shower".equals("please go outside")) {
                  ;
            }

            this.isDragging = var1;
            if (((1201137294 | 356049556) & 139297475 ^ 566971 ^ 336953) == 0) {
                  ;
            }

      }

      public ArrayList getComponents() {
            return this.components;
      }

      public Frame(Module.Category var1) {
            this.category = var1;
            if (!"please take a shower".equals("buy a domain and everything else you need at namecheap.com")) {
                  ;
            }

            this.width = ((61 | 49) ^ 50) >>> 4 >> 4 ^ 88;
            if (((177318990 >> 4 ^ 9534160) >>> 4 ^ -270950477) != 0) {
                  ;
            }

            if (!"buy a domain and everything else you need at namecheap.com".equals("please go outside")) {
                  ;
            }

            this.x = (3 << 2 | 4) ^ 9;
            this.y = (4 >>> 4 >> 3 & 478888155 ^ 825656601) << 3 ^ -1984681779;
            this.barHeight = 10 << 2 >>> 4 << 1 >>> 2 ^ 21;
            this.dragX = 1375740946 << 3 >> 4 & 1224526904 ^ 1207959560;
            this.open = (boolean)((632956920 ^ 292336613) >> 1 & 289450080 ^ 272631808);
            this.isDragging = (boolean)(9453865 >> 1 ^ 3354975 ^ 8065483);
            int var2 = this.barHeight;
            Iterator var3 = ModuleManager.getModulesByCategory(var1).iterator();

            while(true) {
                  if ("please dont crack my plugin".equals("please take a shower")) {
                  }

                  if (!var3.hasNext()) {
                        if ((1377686888 << 1 >> 4 ^ 2139257875) != 0) {
                              ;
                        }

                        return;
                  }

                  Module var4 = (Module)var3.next();
                  if (!"please go outside".equals("yo mama name maurice")) {
                        ;
                  }

                  Button var5 = new Button(var4, this, var2);
                  ArrayList var10000 = this.components;
                  if ((301999615 >> 3 << 1 ^ 2086657192) != 0) {
                        ;
                  }

                  var10000.add(var5);
                  var2 += 14;
                  this.heightY += (8 >> 1 >> 3 & 187798895) >>> 1 ^ 14;
            }
      }

      public void refresh() {
            int var1 = this.barHeight;

            Component var3;
            for(Iterator var2 = this.components.iterator(); var2.hasNext(); var1 += var3.getHeight()) {
                  var3 = (Component)var2.next();
                  if ((100800576 << 1 ^ 1138800798) != 0) {
                        ;
                  }

                  var3.setOff(var1);
            }

      }

      public void updatePosition(int var1, int var2) {
            if (this.isDragging) {
                  this.setX(var1 - this.dragX);
                  int var10002 = this.dragY;
                  if (((1677591578 ^ 1058145830) >> 4 & 77264617 ^ 1993756775) != 0) {
                        ;
                  }

                  this.setY(var2 - var10002);
            }

      }

      public boolean isOpen() {
            return this.open;
      }

      public int getY() {
            return this.y;
      }

      public int getWidth() {
            return this.width;
      }

      public boolean isWithinHeader(int var1, int var2) {
            if (var1 >= this.x) {
                  int var10001 = this.x;
                  int var10002 = this.width;
                  if (((538182180 | 163368448) ^ 700239396) == 0) {
                        ;
                  }

                  if (var1 <= var10001 + var10002 && var2 >= this.y && var2 <= this.y + this.barHeight) {
                        return (boolean)(((0 ^ 1133643361) & 231681155) >> 1 >>> 4 ^ 790561);
                  }
            }

            return (boolean)((135268576 | 29117788) & 59805499 & 2677216 ^ 2336);
      }

      public int getX() {
            return this.x;
      }

      public void updateScrollOpen() {
            int var1 = Mouse.getEventDWheel();
            if ((1844352688 >> 1 & 219696429 ^ 66462430 ^ 132541398) == 0) {
                  ;
            }

            if (((1897267780 >>> 1 | 804417287) ^ -676001348) != 0) {
                  ;
            }

            if (var1 != 0) {
                  if (var1 > 0) {
                        var1 = (1642006171 | 1248822146) << 2 ^ 1342300563;
                  } else {
                        var1 = (0 & 2110114642) >>> 2 & 240423736 & 224735467 ^ 1;
                  }

                  if ((843055152 >> 2 << 4 ^ -922746688) == 0) {
                        ;
                  }

                  int var10000;
                  int var10001;
                  if (var1 > 0) {
                        var10000 = Mouse.getEventX();
                        if ((((807384701 | 35250154) >> 3 ^ 77859117) & 28536423 ^ 10708546) == 0) {
                              ;
                        }

                        if (var10000 >= this.x && Mouse.getEventX() <= this.x + this.width && Mouse.getEventY() >= this.y + this.barHeight) {
                              var10000 = Mouse.getEventY();
                              var10001 = this.heightY;
                              if (!"stop skidding".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                                    ;
                              }

                              if (var10000 <= var10001) {
                                    if (this.scrollStart > 0) {
                                          this.scrollStart -= 0 >> 4 >> 4 ^ 1247551504 ^ 1247551505;
                                          var10001 = this.scrollEnd;
                                          int var10002 = ((0 | 1628908209 | 970819653) & 1148713457 | 586723608) ^ 1660926456;
                                          if (!"i hope you catch fire ngl".equals("please get a girlfriend and stop cracking plugins")) {
                                                ;
                                          }

                                          this.scrollEnd = var10001 + var10002;
                                    } else {
                                          if ((((1839234563 | 239416976) >>> 1 | 845924044) ^ 939261901) == 0) {
                                                ;
                                          }

                                          if (this.scrollStart == 0) {
                                                this.scrollStart = (326562679 | 168877607 | 164647513) ^ 469172095;
                                          }
                                    }
                              }
                        }

                        int var3 = this.x + this.width;
                        if (((1769444975 >> 1 | 530271880) ^ 1069273023) == 0) {
                              ;
                        }

                        PrintStream var5 = System.out;
                        StringBuilder var6 = (new StringBuilder()).append(this.x).append("   ").append(Mouse.getDY()).append(" ").append(Mouse.getEventDY()).append("   ");
                        if ((((848280696 >> 1 ^ 269491587) >>> 4 | 2343147) ^ 1173635586) != 0) {
                              ;
                        }

                        var5.println(var6.append(var3).toString());
                        int var4 = this.heightY;
                        var5 = System.out;
                        var6 = (new StringBuilder()).append(this.y + this.barHeight).append("   ").append(Mouse.getEventY());
                        if (((423188509 >>> 4 ^ 7684171) << 2 ^ -702983209) != 0) {
                              ;
                        }

                        var6 = var6.append("   ");
                        if ((760155200 >> 3 << 4 ^ 184540715 ^ 1348585131) == 0) {
                              ;
                        }

                        var5.println(var6.append(var4).toString());
                  } else if (Mouse.getEventX() >= this.x) {
                        if ((((291337963 ^ 9685023) << 3 ^ 580173327) << 1 ^ 1505923934) == 0) {
                              ;
                        }

                        var10000 = Mouse.getEventX();
                        var10001 = this.x;
                        if (((1924114960 | 703856146) ^ 140042334 ^ -890086910) != 0) {
                              ;
                        }

                        if (var10000 <= var10001 + this.width) {
                              var10000 = Mouse.getEventY();
                              var10001 = this.y;
                              if (((1927689158 << 1 | 282304631 | 1086477324) >> 2 ^ 1303732558) != 0) {
                                    ;
                              }

                              if (var10000 >= var10001 + this.barHeight && Mouse.getEventY() <= this.heightY) {
                                    if (this.scrollEnd != this.components.size()) {
                                          this.scrollEnd -= 0 >> 1 >>> 1 >>> 2 ^ 1;
                                          this.scrollStart += ((0 | 201145626) & 55627311 ^ 9296865 | 2736996) ^ 66967534;
                                    } else {
                                          var10000 = this.scrollEnd;
                                          if (!"you probably spell youre as your".equals("please take a shower")) {
                                                ;
                                          }

                                          if (var10000 == this.components.size()) {
                                                if (((209036111 >> 1 & 22840523) << 2 >> 1 ^ 3211526) == 0) {
                                                      ;
                                                }

                                                this.scrollEnd = this.components.size();
                                          }
                                    }
                              }
                        }
                  }
            }

      }

      public void renderFrame() throws IOException {
            int var10000 = this.x;
            if (!"Some babies were dropped on their heads but you were clearly thrown at a wall.".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                  ;
            }

            double var2 = (double)var10000;
            double var10001 = (double)this.y;
            if (!"shitted on you harder than archybot".equals("You're so fat whenever you go to the beach the tide comes in.")) {
                  ;
            }

            int var10002 = this.width;
            if (!"your mom your dad the one you never had".equals("yo mama name maurice")) {
                  ;
            }

            double var5 = (double)var10002;
            double var10003 = (double)this.barHeight;
            if (((1552256683 ^ 1073278256 | 1208217724) ^ 1803547647) == 0) {
                  ;
            }

            Hud.drawRoundedRect(var2, var10001, var5, var10003, 8.0D, ClickGui.color);
            GL11.glPushMatrix();
            GL11.glScalef(0.5F, 0.5F, 0.5F);
            if (((774143322 >> 4 | 1324624 | 30864665) >> 3 ^ 1220905688) != 0) {
                  ;
            }

            GlyphPageFontRenderer var3 = Hud.renderer;
            String var4 = this.category.name();
            var10002 = this.x * (((0 | 511197318) >> 3 | 26121400 | 35843706) ^ 66060024) + this.getWidth();
            GlyphPageFontRenderer var7 = Hud.renderer;
            String var10004 = this.category.name();
            if (!"ape covered in human flesh".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                  ;
            }

            float var6 = (float)(var10002 - var7.getStringWidth(var10004) / (((1 ^ 0) & 0 ^ 2066225025) >>> 4 ^ 129139066)) - 5.0F;
            float var8 = ((float)this.y + 2.5F) * 2.0F + 1.0F;
            Color var9 = new Color;
            int var10006 = (244 >> 1 << 4 & 928 | 163) ^ 860;
            int var10007 = ((5 ^ 2) & 0) >>> 3 ^ 255;
            if ((((1371533335 >>> 3 ^ 115300176) << 3 | 384786359) ^ 2012176311) == 0) {
                  ;
            }

            var9.<init>(var10006, var10007, ((249 | 110) << 2 & 353 & 194) >> 3 ^ 247, (207 | 37 | 49) ^ 0);
            var3.drawString(var4, var6, var8, var9.getRGB(), (boolean)(0 >> 1 & 242124976 ^ 1));
            var3 = Hud.renderer;
            var4 = this.open ? ">" : "^";
            var6 = (float)((this.x + this.width - (9 >>> 1 << 3 & 31 ^ 10)) * ((0 >>> 3 ^ 629097037) >> 4 ^ 39318566) + ((0 | 1563334871) << 1 & 499525669 & 79411158 ^ 8198));
            var8 = ((float)this.y + 2.5F) * 2.0F + 1.0F;
            var9 = new Color;
            if ((1737055674 >> 1 << 4 & 19414031 ^ 535552) == 0) {
                  ;
            }

            var9.<init>(148 << 3 >>> 2 ^ 471, 103 << 4 >>> 1 >>> 1 ^ 355, (183 ^ 101) >> 2 >>> 3 & 5 ^ 251, (177 | 164) >> 4 ^ 244);
            var3.drawString(var4, var6, var8, var9.getRGB(), (boolean)((0 << 4 << 2 | 1228573244) >> 4 ^ 76785826));
            GL11.glPopMatrix();
            if ((((2031785128 ^ 1292035669) << 2 << 3 | 992241631) ^ 652686189) != 0) {
                  ;
            }

            if (this.open) {
                  if ((552061146 >>> 2 >>> 2 >> 1 >>> 2 >>> 1 ^ -645314941) != 0) {
                        ;
                  }

                  if (!this.components.isEmpty()) {
                        int var1;
                        if (this.components.size() > (int)this.cntComp) {
                              for(var1 = 787259734 << 1 & 1430679567 ^ 1430274060; (double)var1 < this.cntComp; ++var1) {
                                    if ((((2097298767 << 2 | 1035446155) >>> 2 ^ 741592791 | 221015473) ^ 528252857) == 0) {
                                          ;
                                    }

                                    ((Component)this.components.get(var1)).renderComponent();
                              }

                              if (((650499399 >> 3 | 25600281) ^ 1079215028) != 0) {
                                    ;
                              }

                              this.cntComp += 0.14D;
                        } else {
                              for(var1 = (1486451542 ^ 868420254 ^ 243535426) >> 3 << 1 ^ 427269602; var1 < this.components.size(); ++var1) {
                                    ((Component)this.components.get(var1)).renderComponent();
                                    if ((((1141386464 >> 3 | 80500000) << 3 | 1185946980) ^ 1727708644) == 0) {
                                          ;
                                    }
                              }
                        }
                  }
            }

      }

      public void setY(int var1) {
            this.y = var1;
      }

      public void setX(int var1) {
            this.x = var1;
      }
}
