//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.particles;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class ParticleSystem {
      private List particleList;
      private int dist = ((62 | 8) & 16 ^ 14) >> 3 ^ 103;
      private Random random;

      public ParticleSystem(int var1) {
            if ((293929160 << 3 ^ 1200961360 ^ 1243943469) != 0) {
                  ;
            }

            if (((504547621 | 221920513) << 4 ^ -206581168) == 0) {
                  ;
            }

            this.random = new Random();
            if ((((38686789 | 19431637) & 47392027) << 4 ^ 606077200) == 0) {
                  ;
            }

            if (!"You're so fat whenever you go to the beach the tide comes in.".equals("idiot")) {
                  ;
            }

            ArrayList var10001 = new ArrayList();
            if (!"please get a girlfriend and stop cracking plugins".equals("please take a shower")) {
                  ;
            }

            this.particleList = var10001;
            if (((434428636 ^ 421628369) >> 4 >>> 4 >> 3 ^ 968059681) != 0) {
                  ;
            }

            this.addParticles(var1);
      }

      public void tick(int var1) {
            if ((((1772422293 | 513329772) ^ 1664785944) << 1 ^ 957260234) == 0) {
                  ;
            }

            if (Mouse.isButtonDown(1401217974 >>> 4 >>> 2 ^ 21894030)) {
                  this.addParticles((0 ^ 1109122870) << 1 << 3 ^ 566096737);
            }

            this.particleList.forEach((var1x) -> {
                  var1x.tick(var1, 0.1F);
            });
      }

      public void render() {
            GL11.glPushMatrix();
            if ((1945143841 >>> 2 ^ 390112738 ^ -2135517009) != 0) {
                  ;
            }

            GL11.glEnable(809 << 1 << 3 ^ 3226 ^ 13800);
            if (((841750302 ^ 652947327) >>> 3 & 23822250 & 227033 ^ -1931481522) != 0) {
                  ;
            }

            GL11.glDisable((1532 & 1168) >>> 3 >> 1 ^ 3496);
            GL11.glBlendFunc((619 ^ 46 | 119) >> 1 ^ 569, (329 | 7 | 234) ^ 748);
            GL11.glDisable(2808 >>> 2 >>> 3 ^ 2835);
            GL11.glDisable(2411 >>> 2 << 1 & 1060 & 889 ^ 3521);
            GL11.glDisable(2120 >> 1 >> 3 >> 1 ^ 2867);
            if (!"idiot".equals("yo mama name maurice")) {
                  ;
            }

            if (!"ape covered in human flesh".equals("you probably spell youre as your")) {
                  ;
            }

            GL11.glDepthMask((boolean)(1988715989 << 4 << 3 >> 2 >>> 1 ^ 144072016));
            Iterator var1 = this.particleList.iterator();

            label108:
            while(var1.hasNext()) {
                  Particle var2 = (Particle)var1.next();
                  GL11.glColor4f(1.0F, 1.0F, 1.0F, var2.getAlpha() / 255.0F);
                  GL11.glPointSize(var2.getSize());
                  if ((((117928475 ^ 101348408) >> 4 >>> 1 | 151521) ^ 201283306) != 0) {
                        ;
                  }

                  GL11.glBegin((1692329218 << 1 & 2074774930) >> 3 ^ 154472704);
                  float var10000 = var2.getX();
                  float var10001 = var2.getY();
                  if (((2118416389 | 1716877228) >> 4 ^ 132470778) == 0) {
                        ;
                  }

                  GL11.glVertex2f(var10000, var10001);
                  GL11.glEnd();
                  int var3 = Mouse.getEventX() * Minecraft.getMinecraft().currentScreen.width / Minecraft.getMinecraft().displayWidth;
                  int var4 = Minecraft.getMinecraft().currentScreen.height - Mouse.getEventY() * Minecraft.getMinecraft().currentScreen.height / Minecraft.getMinecraft().displayHeight - ((0 << 3 & 1526512170 | 262147324) ^ 196462172 ^ 68536993);
                  if ((322680421 >>> 4 & 16897704 & 12353086 ^ 897875061) != 0) {
                        ;
                  }

                  float var5 = 0.0F;
                  Particle var6 = null;
                  Iterator var7 = this.particleList.iterator();

                  while(true) {
                        Particle var8;
                        float var9;
                        float var10002;
                        float var10003;
                        do {
                              do {
                                    do {
                                          if (!var7.hasNext()) {
                                                if (var6 != null) {
                                                      float var10004 = (float)this.dist;
                                                      if (((673662709 ^ 233761410 | 128502565) >> 2 ^ 867451557) != 0) {
                                                            ;
                                                      }

                                                      float var10 = Math.min(1.0F, Math.min(1.0F, 1.0F - var5 / var10004));
                                                      if (!"minecraft".equals("i hope you catch fire ngl")) {
                                                            ;
                                                      }

                                                      var10001 = var2.getX();
                                                      var10002 = var2.getY();
                                                      if (((67016743 << 3 >>> 3 ^ 51189821 | 800694) ^ 145674483) != 0) {
                                                            ;
                                                      }

                                                      this.drawLine(var10001, var10002, var6.getX(), var6.getY(), 1.0F, 1.0F, 1.0F, var10);
                                                }
                                                continue label108;
                                          }

                                          var8 = (Particle)var7.next();
                                          var9 = var2.getDistanceTo(var8);
                                    } while(var9 > (float)this.dist);

                                    var10001 = (float)var3;
                                    if (!"You're so fat whenever you go to the beach the tide comes in.".equals("buy a domain and everything else you need at namecheap.com")) {
                                          ;
                                    }

                                    var10002 = (float)var4;
                                    var10003 = var2.getX();
                                    if (((1310804334 >> 2 | 204992375) ^ 1336404669) != 0) {
                                          ;
                                    }
                              } while(this.distance(var10001, var10002, var10003, var2.getY()) > (double)this.dist && this.distance((float)var3, (float)var4, var8.getX(), var8.getY()) > (double)this.dist);
                        } while(var5 > 0.0F && var9 > var5);

                        var5 = var9;
                        var6 = var8;
                  }
            }

            GL11.glPushMatrix();
            GL11.glTranslatef(0.5F, 0.5F, 0.5F);
            if ((209166341 >>> 2 ^ 19222565 ^ 1774838690) != 0) {
                  ;
            }

            GL11.glNormal3f(0.0F, 1.0F, 0.0F);
            if (((1053726135 | 108319909) >>> 2 ^ 1285638515) != 0) {
                  ;
            }

            GL11.glEnable(744 >>> 4 ^ 0 ^ 41 ^ 3558);
            GL11.glPopMatrix();
            if (((150385000 << 3 & 238384167 ^ 64201611 | 54574199 | 49002602) ^ 399558886) != 0) {
                  ;
            }

            GL11.glDepthMask((boolean)((0 >> 3 | 1260289907) ^ 1260289906));
            if (!"yo mama name maurice".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                  ;
            }

            GL11.glEnable((650 & 600) >>> 2 >>> 2 ^ 2916);
            GL11.glEnable(((2076 << 3 ^ 4627) << 1 | '舜') ^ 'ꨟ');
            GL11.glEnable(((1735 | 177) & 1033) >>> 2 ^ 2673);
            GL11.glEnable(2001 << 4 >>> 3 >> 1 ^ 2608);
            GL11.glDisable((301 << 4 & 4451) >>> 1 ^ 962);
            GL11.glPopMatrix();
      }

      public void addParticles(int var1) {
            int var2 = (1437958662 << 1 | 1660882490) << 4 >> 3 ^ -134341508;
            if (!"ape covered in human flesh".equals("you're dogshit")) {
                  ;
            }

            while(var2 < var1) {
                  if (!"stop skidding".equals("You're so fat whenever you go to the beach the tide comes in.")) {
                        ;
                  }

                  this.particleList.add(Particle.generateParticle());
                  ++var2;
            }

      }

      private void drawLine(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8) {
            GL11.glColor4f(var5, var6, var7, var8);
            GL11.glLineWidth(0.5F);
            GL11.glBegin(0 >> 1 >> 2 ^ 1);
            GL11.glVertex2f(var1, var2);
            if (!"Some babies were dropped on their heads but you were clearly thrown at a wall.".equals("please dont crack my plugin")) {
                  ;
            }

            GL11.glVertex2f(var3, var4);
            GL11.glEnd();
      }

      public double distance(float var1, float var2, float var3, float var4) {
            if ((1772952432 << 4 >>> 2 >>> 3 ^ 81169848) == 0) {
                  ;
            }

            return Math.sqrt((double)((var1 - var3) * (var1 - var3) + (var2 - var4) * (var2 - var4)));
      }
}
