//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.particles;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class ParticleSystem {
      private int dist;
      private Random random;
      private List particleList;

      public void tick(int var1) {
            if (Mouse.isButtonDown(2032177535 << 4 << 3 >>> 2 ^ 605171680)) {
                  this.addParticles(0 << 3 ^ 888457995 ^ 888457994);
            }

            if (((85528592 >>> 3 >> 3 | 315418) ^ -610785239) != 0) {
                  ;
            }

            this.particleList.forEach((var1x) -> {
                  if (!"stringer is a good obfuscator".equals("ape covered in human flesh")) {
                        ;
                  }

                  var1x.tick(var1, 0.1F);
            });
      }

      public ParticleSystem(int var1) {
            if (((73992192 | 14376027) & 51461390 ^ 98269722) != 0) {
                  ;
            }

            super();
            this.dist = (57 << 4 & 83 & 5 & 827034790 | 739261850) ^ 739261950;
            this.random = new Random();
            ArrayList var10001 = new ArrayList;
            if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("please go outside")) {
                  ;
            }

            var10001.<init>();
            this.particleList = var10001;
            this.addParticles(var1);
      }

      public double distance(float var1, float var2, float var3, float var4) {
            float var10000 = var1 - var3;
            if (((1174886432 >>> 4 | 11086805) ^ 82411479) == 0) {
                  ;
            }

            return Math.sqrt((double)(var10000 * (var1 - var3) + (var2 - var4) * (var2 - var4)));
      }

      private void drawLine(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8) {
            GL11.glColor4f(var5, var6, var7, var8);
            GL11.glLineWidth(0.5F);
            if (((184308105 << 1 >>> 1 << 2 | 586180088) ^ 737241084) == 0) {
                  ;
            }

            GL11.glBegin((0 ^ 2105982322 | 1792753877 | 142821625) ^ 2145385982);
            GL11.glVertex2f(var1, var2);
            GL11.glVertex2f(var3, var4);
            GL11.glEnd();
      }

      public void addParticles(int var1) {
            for(int var2 = 134237201 << 4 ^ -2147172080; var2 < var1; ++var2) {
                  this.particleList.add(Particle.generateParticle());
            }

      }

      public void render() {
            if (((288167592 >> 1 & 69834382) << 1 >> 4 >>> 4 ^ 1730578521) != 0) {
                  ;
            }

            GL11.glPushMatrix();
            GL11.glEnable((948 >>> 3 | 0) << 2 ^ 2618);
            GL11.glDisable((1116 & 1068) << 3 << 1 << 4 ^ 262625);
            GL11.glBlendFunc(((86 & 34 | 0) ^ 0) >>> 2 ^ 770, (554 & 475) >>> 3 ^ 770);
            GL11.glDisable(((646 << 4 | 10011) & 8306) >> 3 ^ 3914);
            GL11.glDisable((304 >> 1 | 122) ^ 3355);
            GL11.glDisable((1016 | 648) & 713 ^ 2489);
            int var10000 = ((1145586718 | 789751105) & 691000606) >>> 1 >> 1 & 47041844 ^ 37781764;
            if ((((895857488 ^ 661781587) & 128475078 | 4033284) & 21777695 ^ '頏' ^ 1917654324) != 0) {
                  ;
            }

            GL11.glDepthMask((boolean)var10000);
            Iterator var1 = this.particleList.iterator();

            label113:
            while(true) {
                  if (!"Some babies were dropped on their heads but you were clearly thrown at a wall.".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                        ;
                  }

                  if (!var1.hasNext()) {
                        GL11.glPushMatrix();
                        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
                        GL11.glNormal3f(0.0F, 1.0F, 0.0F);
                        GL11.glEnable(2736 >> 4 & 105 ^ 33 ^ 3561);
                        GL11.glPopMatrix();
                        var10000 = 0 << 2 >> 4 >> 1 ^ 1;
                        if ((1558557533 >> 2 ^ 36090924 ^ -1013429320) != 0) {
                              ;
                        }

                        GL11.glDepthMask((boolean)var10000);
                        GL11.glEnable((1403 & 645) >>> 4 >> 3 << 2 >> 2 ^ 2884);
                        GL11.glEnable(1445 << 4 ^ 23035 ^ 3658);
                        GL11.glEnable(820 << 2 << 3 >> 1 & 6879 ^ 6449);
                        GL11.glEnable(257 >>> 4 & 8 ^ 3553);
                        GL11.glDisable((1198 ^ 781) >> 4 << 4 & 1715 & 1335 ^ 4034);
                        GL11.glPopMatrix();
                        return;
                  }

                  Particle var2 = (Particle)var1.next();
                  GL11.glColor4f(1.0F, 1.0F, 1.0F, var2.getAlpha() / 255.0F);
                  GL11.glPointSize(var2.getSize());
                  if (!"please take a shower".equals("ape covered in human flesh")) {
                        ;
                  }

                  GL11.glBegin((1824232069 << 4 | 1581593436) ^ -537052324);
                  float var11 = var2.getX();
                  if (!"please dont crack my plugin".equals("your mom your dad the one you never had")) {
                        ;
                  }

                  GL11.glVertex2f(var11, var2.getY());
                  GL11.glEnd();
                  int var3 = Mouse.getEventX() * Minecraft.getMinecraft().currentScreen.width / Minecraft.getMinecraft().displayWidth;
                  if ((((1142370939 | 204025850) ^ 446784819 ^ 970247076) << 1 ^ 1055592844 ^ -528600236) == 0) {
                        ;
                  }

                  GuiScreen var12 = Minecraft.getMinecraft().currentScreen;
                  if (((43589761 >> 3 | 1601834) >> 2 ^ 1178825884) != 0) {
                        ;
                  }

                  var10000 = var12.height;
                  int var10001 = Mouse.getEventY() * Minecraft.getMinecraft().currentScreen.height / Minecraft.getMinecraft().displayHeight;
                  if (!"stringer is a good obfuscator".equals("nefariousMoment")) {
                        ;
                  }

                  int var4 = var10000 - var10001 - ((0 | 1809153019) >> 3 ^ 226144126);
                  float var5 = 0.0F;
                  if (!"please go outside".equals("ape covered in human flesh")) {
                        ;
                  }

                  Particle var6 = null;
                  Iterator var7 = this.particleList.iterator();

                  while(true) {
                        while(true) {
                              Particle var8;
                              float var9;
                              double var14;
                              do {
                                    float var13;
                                    do {
                                          if (!var7.hasNext()) {
                                                if (var6 != null) {
                                                      float var10 = Math.min(1.0F, Math.min(1.0F, 1.0F - var5 / (float)this.dist));
                                                      var13 = var2.getX();
                                                      float var10002 = var2.getY();
                                                      if (((155204611 ^ 97385186) >> 4 >>> 3 ^ 1645453) == 0) {
                                                            ;
                                                      }

                                                      float var10003 = var6.getX();
                                                      float var10004 = var6.getY();
                                                      if ((881806757 ^ 581401758 ^ 321501093 ^ -256796807) != 0) {
                                                            ;
                                                      }

                                                      this.drawLine(var13, var10002, var10003, var10004, 1.0F, 1.0F, 1.0F, var10);
                                                }
                                                continue label113;
                                          }

                                          if (!"stop skidding".equals("yo mama name maurice")) {
                                                ;
                                          }

                                          var8 = (Particle)var7.next();
                                          var9 = var2.getDistanceTo(var8);
                                    } while(var9 > (float)this.dist);

                                    if (this.distance((float)var3, (float)var4, var2.getX(), var2.getY()) <= (double)this.dist) {
                                          break;
                                    }

                                    var13 = (float)var3;
                                    if ((((200428139 ^ 50101796) & 45027951) >>> 3 ^ 97180 ^ '릕') == 0) {
                                          ;
                                    }

                                    var14 = this.distance(var13, (float)var4, var8.getX(), var8.getY());
                                    var10001 = this.dist;
                                    if (((1562467590 ^ 544986903 ^ 1584525145 ^ 257908859) >>> 4 >>> 2 ^ 11653140) == 0) {
                                          ;
                                    }
                              } while(var14 > (double)var10001);

                              if ((16842777 >> 2 & 1079938 ^ 16386) == 0) {
                                    ;
                              }

                              if (var5 > 0.0F && var9 > var5) {
                                    if (((1319860614 | 415684382) << 2 ^ -898954129) != 0) {
                                          ;
                                    }
                              } else {
                                    var5 = var9;
                                    var6 = var8;
                              }
                        }
                  }
            }
      }
}
