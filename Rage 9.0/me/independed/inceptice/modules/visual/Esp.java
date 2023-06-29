//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.visual;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import me.independed.inceptice.modules.Module;
import me.independed.inceptice.settings.BooleanSetting;
import me.independed.inceptice.settings.ModeSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import org.lwjgl.opengl.GL11;

public class Esp extends Module {
      public BooleanSetting animalSet;
      public BooleanSetting playerSet;
      private transient ArrayList ENTITIES;
      public BooleanSetting invisibleSet;
      public BooleanSetting mobSet;
      private transient int BOX;
      public ModeSetting modeESP;

      public void onRenderWorldLast(float var1) {
            if (mc.player != null) {
                  boolean var10000 = mc.player.isDead;
                  if (!"buy a domain and everything else you need at namecheap.com".equals("yo mama name maurice")) {
                        ;
                  }

                  if (!var10000) {
                        Object var11 = mc.world.loadedEntityList.stream().filter((var0) -> {
                              return (boolean)(var0 != mc.player ? ((0 | 1756858404) & 550944207) >> 3 & 49718288 ^ 1212417 : (1508276360 >>> 1 >> 2 | 156570012 | 85255327) ^ 229327365 ^ 47627674);
                        }).filter((var0) -> {
                              return (boolean)(!var0.isDead ? (0 << 1 >>> 2 | 185842336) ^ 185842337 : 266080694 >>> 2 >> 3 & 1104725 ^ 1097733);
                        }).sorted(Comparator.comparing((var0) -> {
                              return Float.valueOf(mc.player.getDistance(var0));
                        })).collect(Collectors.toList());
                        if (!"stringer is a good obfuscator".equals("buy a domain and everything else you need at namecheap.com")) {
                              ;
                        }

                        List var2 = (List)var11;
                        Iterator var3 = var2.iterator();

                        while(true) {
                              Entity var4;
                              double var5;
                              double var7;
                              double var9;
                              double var13;
                              double var14;
                              label249:
                              while(true) {
                                    while(true) {
                                          Minecraft var10001;
                                          do {
                                                do {
                                                      if (!var3.hasNext()) {
                                                            if ((((1660264079 | 550315464) << 3 ^ 54245552) >>> 3 ^ 43698393) == 0) {
                                                                  ;
                                                            }

                                                            return;
                                                      }

                                                      var4 = (Entity)var3.next();
                                                } while(var4 == null);

                                                var10001 = mc;
                                                if (!"please go outside".equals("please get a girlfriend and stop cracking plugins")) {
                                                      ;
                                                }
                                          } while(var4 == var10001.player);

                                          if ((((493647476 | 441612165) & 449394648) << 3 ^ 1186975313 ^ -1795264303) == 0) {
                                                ;
                                          }

                                          BooleanSetting var12;
                                          if (this.modeESP.activeMode == "2D") {
                                                if (this.invisibleSet.isEnabled()) {
                                                      var10000 = var4.isInvisible();
                                                      if ((((715577248 ^ 689414088) & 40839605 ^ 6578543) >>> 1 ^ 12247132 ^ 26760443) == 0) {
                                                            ;
                                                      }

                                                      if (var10000) {
                                                            if (var4 instanceof EntityPlayer) {
                                                                  if (((1619519668 >> 4 | 90199582) & 65377766 ^ 56628358) == 0) {
                                                                        ;
                                                                  }

                                                                  if (this.playerSet.isEnabled()) {
                                                                        break label249;
                                                                  }
                                                            }

                                                            if (((1799588646 | 45195144) << 2 & 386653021 ^ -1833064843) != 0) {
                                                                  ;
                                                            }

                                                            if (var4 instanceof EntityMob && this.mobSet.isEnabled() || var4 instanceof EntityAnimal && this.animalSet.isEnabled()) {
                                                                  break label249;
                                                            }
                                                      }
                                                }

                                                if (((723518592 ^ 57505818) >>> 3 << 4 ^ -565950075) != 0) {
                                                      ;
                                                }

                                                if (this.mobSet.isEnabled() && var4 instanceof EntityMob) {
                                                      var5 = var4.lastTickPosX + (var4.posX - var4.lastTickPosX) - mc.getRenderManager().viewerPosX;
                                                      var7 = var4.lastTickPosY + (var4.posY - var4.lastTickPosY) - mc.getRenderManager().viewerPosY;
                                                      var9 = var4.lastTickPosZ + (var4.posZ - var4.lastTickPosZ) - mc.getRenderManager().viewerPosZ;
                                                      if (((517588624 | 461531118) & 378323663 ^ 378061518) == 0) {
                                                            ;
                                                      }

                                                      this.a(var4, var5, var7, var9);
                                                      this.ab(var4, var5, var7, var9);
                                                } else {
                                                      var12 = this.animalSet;
                                                      if (((835755899 | 636714359) >> 1 & 43949310 ^ 2331033 ^ 45813031) == 0) {
                                                            ;
                                                      }

                                                      if (var12.isEnabled() && var4 instanceof EntityAnimal) {
                                                            if (((1253470771 << 2 | 117973335) >>> 1 ^ 401405167) == 0) {
                                                                  ;
                                                            }

                                                            var14 = var4.lastTickPosX;
                                                            if ((385875974 ^ 79948115 ^ 273452157 ^ -1230887559) != 0) {
                                                                  ;
                                                            }

                                                            var14 = var14 + (var4.posX - var4.lastTickPosX) - mc.getRenderManager().viewerPosX;
                                                            if (!"intentMoment".equals("nefariousMoment")) {
                                                                  ;
                                                            }

                                                            var5 = var14;
                                                            var14 = var4.lastTickPosY;
                                                            var13 = var4.posY;
                                                            if (((1649282610 ^ 881129005) << 2 >> 3 ^ 191200271) == 0) {
                                                                  ;
                                                            }

                                                            var7 = var14 + (var13 - var4.lastTickPosY) - mc.getRenderManager().viewerPosY;
                                                            var9 = var4.lastTickPosZ + (var4.posZ - var4.lastTickPosZ) - mc.getRenderManager().viewerPosZ;
                                                            this.a(var4, var5, var7, var9);
                                                            if (((1173275887 ^ 790843231) & 212560058 ^ 143198384) == 0) {
                                                                  ;
                                                            }

                                                            this.ab(var4, var5, var7, var9);
                                                      } else if (this.playerSet.isEnabled() && var4 instanceof EntityPlayer) {
                                                            if ((((233964018 | 164643779) & 147211071) >>> 4 ^ 9184307) == 0) {
                                                                  ;
                                                            }

                                                            var14 = var4.lastTickPosX + (var4.posX - var4.lastTickPosX);
                                                            var13 = mc.getRenderManager().viewerPosX;
                                                            if (!"Some babies were dropped on their heads but you were clearly thrown at a wall.".equals("your mom your dad the one you never had")) {
                                                                  ;
                                                            }

                                                            var14 -= var13;
                                                            if ((1476820356 << 3 << 1 ^ 1223539584) != 0) {
                                                                  ;
                                                            }

                                                            var5 = var14;
                                                            var7 = var4.lastTickPosY + (var4.posY - var4.lastTickPosY) - mc.getRenderManager().viewerPosY;
                                                            var14 = var4.lastTickPosZ + (var4.posZ - var4.lastTickPosZ);
                                                            if (((164598773 | 147940903 | 77087475 | 157371372) ^ 753435107) != 0) {
                                                                  ;
                                                            }

                                                            var9 = var14 - mc.getRenderManager().viewerPosZ;
                                                            if (((1050332819 ^ 268447260) >> 3 << 4 ^ 1563803920) == 0) {
                                                                  ;
                                                            }

                                                            this.a(var4, var5, var7, var9);
                                                            if (((140196612 >> 1 << 3 | 320404720 | 182393212) ^ 855767517) != 0) {
                                                                  ;
                                                            }

                                                            this.ab(var4, var5, var7, var9);
                                                      }
                                                }
                                          } else {
                                                var10000 = this.invisibleSet.isEnabled();
                                                if (((823002098 << 4 & 172256231) >> 2 >> 4 ^ -1078488608) != 0) {
                                                      ;
                                                }

                                                label262: {
                                                      if (var10000 && var4.isInvisible()) {
                                                            if (!"idiot".equals("stringer is a good obfuscator")) {
                                                                  ;
                                                            }

                                                            if (var4 instanceof EntityPlayer) {
                                                                  if (!"yo mama name maurice".equals("you're dogshit")) {
                                                                        ;
                                                                  }

                                                                  if (this.playerSet.isEnabled()) {
                                                                        break label262;
                                                                  }
                                                            }

                                                            if (var4 instanceof EntityMob) {
                                                                  var12 = this.mobSet;
                                                                  if ((1049544509 << 3 >> 1 << 3 ^ 736288767) != 0) {
                                                                        ;
                                                                  }

                                                                  if (var12.isEnabled()) {
                                                                        break label262;
                                                                  }
                                                            }

                                                            if ((521204620 << 1 << 2 << 3 & 352903917 ^ -134849364) != 0) {
                                                                  ;
                                                            }

                                                            if (var4 instanceof EntityAnimal && this.animalSet.isEnabled()) {
                                                                  break label262;
                                                            }
                                                      }

                                                      if ((1283531008 >>> 3 ^ 160441376) == 0) {
                                                            ;
                                                      }

                                                      if (this.mobSet.isEnabled() && var4 instanceof EntityMob) {
                                                            Esp.entityESPBox(var4, 1769777727 << 4 << 3 ^ -1101717632);
                                                            continue;
                                                      }

                                                      if ((9446538 >> 4 >> 3 ^ 73801) == 0) {
                                                            ;
                                                      }

                                                      if (this.animalSet.isEnabled() && var4 instanceof EntityAnimal) {
                                                            Esp.entityESPBox(var4, (1814715883 >>> 2 << 2 >> 1 >> 1 | 68790648) ^ 521912186);
                                                            continue;
                                                      }

                                                      if (this.playerSet.isEnabled()) {
                                                            var10000 = var4 instanceof EntityPlayer;
                                                            if (((1032173562 >> 4 ^ 48586234 ^ 14064329 | 12018012) ^ 33553756) == 0) {
                                                                  ;
                                                            }

                                                            if (var10000) {
                                                                  if (!"stop skidding".equals("buy a domain and everything else you need at namecheap.com")) {
                                                                        ;
                                                                  }

                                                                  Esp.entityESPBox(var4, ((249091989 ^ 192230701 ^ 42357118) >> 4 ^ 4803744 | 3789141) ^ 3930077);
                                                            }
                                                      }
                                                      continue;
                                                }

                                                Esp.entityESPBox(var4, (1628297622 ^ 16499352) << 2 ^ -2015859656);
                                          }
                                    }
                              }

                              var5 = var4.lastTickPosX + (var4.posX - var4.lastTickPosX) - mc.getRenderManager().viewerPosX;
                              var14 = var4.lastTickPosY;
                              if (((955889766 >> 3 | 90568123) ^ 43667768 ^ 98936455) == 0) {
                                    ;
                              }

                              var13 = var4.posY;
                              if (!"please go outside".equals("stop skidding")) {
                                    ;
                              }

                              var13 -= var4.lastTickPosY;
                              if ((879127549 ^ 642317700 ^ 6045871 ^ 65049226 ^ 294798428) == 0) {
                                    ;
                              }

                              var14 += var13;
                              RenderManager var15 = mc.getRenderManager();
                              if (!"i hope you catch fire ngl".equals("i hope you catch fire ngl")) {
                                    ;
                              }

                              var7 = var14 - var15.viewerPosY;
                              var9 = var4.lastTickPosZ + (var4.posZ - var4.lastTickPosZ) - mc.getRenderManager().viewerPosZ;
                              this.a(var4, var5, var7, var9);
                              this.ab(var4, var5, var7, var9);
                              if ((29433860 >> 4 >>> 3 ^ 259848886) != 0) {
                                    ;
                              }
                        }
                  }
            }

      }

      public static void a(double var0, double var2, double var4, double var6, float var8, int var9, int var10) {
            float var10000 = (float)var0;
            float var10001 = (float)var2;
            if ((521319034 >>> 3 >> 2 ^ 16291219) == 0) {
                  ;
            }

            Esp.a(var10000, var10001, (float)var4, (float)var6, var10);
            float var11 = (float)(var9 >> ((9 ^ 6) << 3 & 0 ^ 24) & ((76 >>> 4 << 1 | 6) ^ 241)) / 255.0F;
            float var12 = (float)(var9 >> (4 >>> 3 >>> 4 & 906663372 ^ 16) & ((31 ^ 23) >> 2 >>> 1 ^ 254)) / 255.0F;
            int var15 = var9 >> ((1 ^ 0 ^ 0 | 0) ^ 9);
            int var16 = (122 ^ 23) << 2 & 77 ^ 1 ^ 2 ^ 248;
            if (((1975539413 | 202378212) << 3 ^ 360501781 ^ 338337692 ^ 195724418) != 0) {
                  ;
            }

            float var13 = (float)(var15 & var16) / 255.0F;
            float var14 = (float)(var9 & (174 >> 3 << 4 >> 4 & 0 ^ 255)) / 255.0F;
            if (((1823451914 ^ 1681644305 | 27833998) >> 1 ^ 81689423) == 0) {
                  ;
            }

            GL11.glPushMatrix();
            GL11.glEnable((1095 & 858) >> 1 >>> 3 ^ 3046);
            GL11.glDisable((2322 & 345 ^ 256) >> 4 << 3 ^ 3561);
            GL11.glBlendFunc(646 >>> 4 >>> 3 >>> 3 >> 4 ^ 770, 708 >> 4 & 8 ^ 779);
            if (!"i hope you catch fire ngl".equals("you probably spell youre as your")) {
                  ;
            }

            GL11.glEnable((2186 >> 4 | 60) >> 3 & 20 ^ 2868);
            GL11.glColor4f(var12, var13, var14, var11);
            if (((369369355 >> 2 & 71303132) << 1 ^ 134353024) == 0) {
                  ;
            }

            GL11.glLineWidth(2.0F);
            if (!"please go outside".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                  ;
            }

            GL11.glBegin((0 ^ 98251085) << 3 ^ 786008681);
            GL11.glVertex2d(var0, var2);
            GL11.glVertex2d(var0, var6);
            GL11.glVertex2d(var4, var6);
            GL11.glVertex2d(var4, var2);
            if ((976441876 ^ 902453085 ^ 78883110 ^ 189443183) == 0) {
                  ;
            }

            if (((774919480 ^ 475594324) << 4 >> 4 ^ -1179781709) != 0) {
                  ;
            }

            GL11.glVertex2d(var0, var2);
            GL11.glVertex2d(var4, var2);
            GL11.glVertex2d(var0, var6);
            if (!"intentMoment".equals("you probably spell youre as your")) {
                  ;
            }

            GL11.glVertex2d(var4, var6);
            GL11.glEnd();
            GL11.glEnable((3048 & 2194) >>> 1 ^ 2465);
            GL11.glDisable((586 ^ 100 ^ 478) & 730 ^ 119 ^ 2373);
            GL11.glDisable((2151 >> 1 ^ 634) << 3 ^ 14696);
            GL11.glPopMatrix();
      }

      public static void drawRect(float var0, float var1, float var2, float var3, int var4) {
            float var5;
            if (var0 < var2) {
                  if ((1310074305 << 3 << 3 ^ 8903250) != 0) {
                        ;
                  }

                  if (!"please dont crack my plugin".equals("i hope you catch fire ngl")) {
                        ;
                  }

                  var5 = var0;
                  var0 = var2;
                  var2 = var5;
            }

            float var13;
            int var10000 = (var13 = var1 - var3) == 0.0F ? 0 : (var13 < 0.0F ? -1 : 1);
            if ((159907869 >> 3 >> 3 ^ 910050110) != 0) {
                  ;
            }

            if (var10000 < 0) {
                  if (!"ape covered in human flesh".equals("idiot")) {
                        ;
                  }

                  var5 = var1;
                  var1 = var3;
                  if (!"please dont crack my plugin".equals("idiot")) {
                        ;
                  }

                  var3 = var5;
            }

            int var10001 = (20 | 17) >> 3 << 4 ^ 56;
            if (!"yo mama name maurice".equals("i hope you catch fire ngl")) {
                  ;
            }

            var5 = (float)(var4 >> var10001 & (((91 ^ 63) >> 1 >>> 2 | 1) ^ 242)) / 255.0F;
            float var6 = (float)(var4 >> (((8 ^ 0 | 1) & 7) >> 1 ^ 16) & (214 >>> 1 >>> 1 & 41 ^ 222)) / 255.0F;
            var10000 = var4 >> ((1 | 0) ^ 0 ^ 9) & (((64 ^ 13) >> 3 | 7) & 4 ^ 251);
            if (((78786984 << 1 | 31440515) ^ 52706214 ^ 733710197) != 0) {
                  ;
            }

            float var7 = (float)var10000 / 255.0F;
            float var11 = (float)(var4 & ((249 ^ 95) & 3 ^ 1 ^ 252));
            if (!"stop skidding".equals("nefariousMoment")) {
                  ;
            }

            float var8 = var11 / 255.0F;
            Tessellator var9 = Tessellator.getInstance();
            BufferBuilder var10 = var9.getBuffer();
            GlStateManager.enableBlend();
            GlStateManager.disableTexture2D();
            GlStateManager.tryBlendFuncSeparate(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA, SourceFactor.ONE, DestFactor.ZERO);
            if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("You're so fat whenever you go to the beach the tide comes in.")) {
                  ;
            }

            GlStateManager.color(var6, var7, var8, var5);
            var10.begin(((0 | 1676921405) >>> 1 | 648013072) << 2 & 71457688 ^ 71438367, DefaultVertexFormats.POSITION);
            var10.pos((double)var0, (double)var3, 0.0D).endVertex();
            var10.pos((double)var2, (double)var3, 0.0D).endVertex();
            BufferBuilder var12 = var10.pos((double)var2, (double)var1, 0.0D);
            if (!"ape covered in human flesh".equals("nefariousMoment")) {
                  ;
            }

            var12.endVertex();
            if (((672211728 ^ 341781710 | 526489933) ^ -150154653) != 0) {
                  ;
            }

            var10.pos((double)var0, (double)var1, 0.0D).endVertex();
            var9.draw();
            GlStateManager.enableTexture2D();
            GlStateManager.disableBlend();
      }

      private void a(Entity var1, double var2, double var4, double var6) {
            GL11.glPushMatrix();
            if (((410678569 >>> 1 ^ 185575393 ^ 74301845) << 2 ^ 226325376) == 0) {
                  ;
            }

            float var10000 = (float)var2;
            float var10001 = (float)var4;
            if ((258715119 >> 4 & 11704396 ^ 1942992 ^ 11484572) == 0) {
                  ;
            }

            GL11.glTranslatef(var10000, var10001, (float)var6);
            GL11.glNormal3f(0.0F, 1.0F, 0.0F);
            if (((1456978673 >>> 2 ^ 132351049) >> 1 & 51965290 ^ 17311082) == 0) {
                  ;
            }

            GL11.glRotatef(-mc.getRenderManager().playerViewY, 0.0F, 0.2F, 0.0F);
            var10000 = mc.getRenderManager().playerViewX;
            if ((((2068351178 >>> 1 & 838961368) >> 1 | 179089990) ^ 447525478) == 0) {
                  ;
            }

            GL11.glRotatef(var10000, 1.0F, 0.0F, 0.0F);
            GL11.glEnable((190 | 10) >>> 2 ^ 3021);
            if (!"You're so fat whenever you go to the beach the tide comes in.".equals("yo mama name maurice")) {
                  ;
            }

            GL11.glBlendFunc(108 << 2 >> 4 >>> 2 >> 3 ^ 770, (((610 | 377) ^ 103) & 561) << 3 ^ 4995);
            GL11.glEnable(((23 | 0) & 18 ^ 13) >>> 2 ^ 2855);
            GL11.glLineWidth(2.0F);
            if (((1661311203 | 1243190209) << 4 & 360323914 ^ -1571177836) != 0) {
                  ;
            }

            GL11.glDisable(((980 | 629) & 117) >> 4 ^ 2903);
            GL11.glDisable((1081 ^ 114) >>> 4 << 4 ^ 3889);
            Tessellator var8 = Tessellator.getInstance();
            if (!"please go outside".equals("ape covered in human flesh")) {
                  ;
            }

            BufferBuilder var9 = var8.getBuffer();
            if (((EntityLivingBase)var1).getHealth() <= 20.0F) {
                  Esp.drawRect(-0.7F, ((EntityLivingBase)var1).getHealth() / 10.0F, -0.58F, 0.0F, (new Color(35 << 2 & 119 ^ 251, (269812872 | 124420597) & 207592476 ^ 73368604, (1538092845 ^ 315671375) >> 4 ^ 77060806, (83 >>> 2 << 3 | 119) >>> 4 ^ 240)).getRGB());
                  Color var10004 = new Color;
                  if (((1566719999 | 8388454) << 1 & 2017909585 ^ -1793281298) != 0) {
                        ;
                  }

                  var10004.<init>((42 >> 1 & 20 | 10) ^ 41, (118 >> 2 >> 3 & 0) >> 3 >> 3 ^ 255, 44 << 1 >>> 3 << 4 ^ 135, (146 | 82) >> 4 ^ 242);
                  Esp.drawRect(-0.71F, 2.01F, -0.58F, 2.0F, var10004.getRGB());
                  if (((933102012 >>> 4 ^ 7032992 | 3357391) >>> 3 ^ 6715231) == 0) {
                        ;
                  }

                  if (!"idiot".equals("your mom your dad the one you never had")) {
                        ;
                  }

                  Esp.drawRect(-0.71F, 2.01F, -0.7F, 0.01F, (new Color((27 | 23) << 3 ^ 207, 222 >>> 3 >> 3 ^ 1 ^ 253, (1 << 1 | 1) ^ 52, 57 >> 4 << 3 >> 1 >>> 2 ^ 252)).getRGB());
                  var10004 = new Color;
                  if (!"shitted on you harder than archybot".equals("stringer is a good obfuscator")) {
                        ;
                  }

                  var10004.<init>(((7 | 0 | 0) & 0 | 1717038322 | 663901325) ^ 1742208200, (142 & 0) << 4 >>> 3 ^ 2064084585 ^ 2064084630, (32 ^ 26 ^ 23) >>> 4 ^ 53, (233 >>> 1 & 49) << 4 ^ 1023);
                  Esp.drawRect(-0.59F, 0.01F, -0.58F, 2.0F, var10004.getRGB());
                  var10004 = new Color;
                  int var10006 = ((28 & 1) >> 1 | 1157995985) & 920269749 ^ 67113382;
                  int var10007 = (240 ^ 140 ^ 72) >>> 3 ^ 4 ^ 253;
                  if (((954530170 >>> 3 >> 2 ^ 1804602) >>> 3 & 2649617 ^ 2622480) == 0) {
                        ;
                  }

                  var10004.<init>(var10006, var10007, 48 ^ 23 ^ 24 ^ 8, 151 >>> 4 << 2 >>> 1 >>> 1 ^ 246);
                  Esp.drawRect(-0.58F, 0.01F, -0.71F, 0.0F, var10004.getRGB());
                  int var10005 = (new Color((53 ^ 6) >> 2 << 4 ^ 134 ^ 113, (147 & 83 & 11) >> 3 ^ 255, 14 & 13 ^ 8 ^ 3 ^ 48, ((96 ^ 20) & 70) >>> 2 >> 2 ^ 251)).getRGB();
                  Color var11 = new Color((249 ^ 37 | 50) ^ 1, 1337519813 >>> 1 & 662147166 ^ 659820610, (1807630107 << 1 ^ 1225998743 | 1215307849) ^ -562085911, ((221 << 3 & 715 ^ 250) & 44) >>> 2 ^ 247);
                  if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("please dont crack my plugin")) {
                        ;
                  }

                  Esp.a(-0.5D, 2.0D, 0.5D, 0.0D, 2.5F, var10005, var11.getRGB());
            }

            int var10 = 425 >> 4 >> 2 << 1 ^ 3054;
            if (((1817095111 ^ 1322989272) << 4 >>> 2 ^ 173438076) == 0) {
                  ;
            }

            GL11.glDisable(var10);
            GL11.glPopMatrix();
            GL11.glEnable((1373 ^ 1282) >>> 4 ^ 2932);
            GL11.glEnable(1084 & 570 ^ 15 ^ 3542);
            if ((219683816 << 1 << 4 >>> 3 ^ 440694296) != 0) {
                  ;
            }

            GL11.glDisable((2242 >>> 3 & 32) >>> 3 << 2 >>> 2 ^ 3042);
            GL11.glDisable((638 | 138) << 4 ^ 9408);
      }

      private void ab(Entity var1, double var2, double var4, double var6) {
            GL11.glPushMatrix();
            float var10000 = (float)var2;
            float var10001 = (float)var4;
            if ((607988764 >>> 4 >> 3 << 2 ^ -1886451051) != 0) {
                  ;
            }

            float var10002 = (float)var6;
            if (!"please get a girlfriend and stop cracking plugins".equals("you're dogshit")) {
                  ;
            }

            GL11.glTranslatef(var10000, var10001, var10002);
            GL11.glNormal3f(0.0F, 1.0F, 0.0F);
            var10000 = -mc.getRenderManager().playerViewX;
            if (!"yo mama name maurice".equals("nefariousMoment")) {
                  ;
            }

            if ((416511196 << 3 >>> 3 ^ 416511196) == 0) {
                  ;
            }

            GL11.glRotatef(var10000, 0.0F, 0.2F, 0.0F);
            GL11.glRotatef(mc.getRenderManager().playerViewY, 1.0F, 0.0F, 0.0F);
            GL11.glEnable(907 >>> 2 ^ 50 ^ 2866);
            int var8 = 485 & 414 ^ 28 ^ 666;
            if ((190508637 << 1 << 4 ^ -492265973) != 0) {
                  ;
            }

            GL11.glBlendFunc(var8, 4 << 4 << 2 ^ 515);
            GL11.glEnable(2533 >> 2 & 136 & 4 & 1677730655 ^ 2848);
            GL11.glLineWidth(2.0F);
            if (!"please dont crack my plugin".equals("nefariousMoment")) {
                  ;
            }

            GL11.glDisable((1212 & 1177) >>> 1 ^ 197 ^ 162 ^ 4042);
            GL11.glDisable(((2811 | 368) << 3 >> 2 | 4706) ^ 7303);
            if (((1426592000 >> 2 >> 4 ^ 11373404) >> 2 ^ 8284870) == 0) {
                  ;
            }

            if ((1742833835 ^ 1259341943 ^ 536993003 ^ 127333630 ^ 191277257) == 0) {
                  ;
            }

            if (((EntityLivingBase)var1).getHealth() <= 20.0F) {
                  Esp.drawRect(1.1F, 0.5F, 1.1F, 0.5F, (850354124 << 2 >> 4 & 269783063) >> 4 ^ -31655785);
            }

            if (((EntityLivingBase)var1).getHealth() <= 10.0F) {
                  Esp.drawRect(1.1F, 2.0F, 1.7F, 2.0F, (363037489 << 1 | 17469654) >>> 2 ^ -181462511);
            }

            float var9;
            var8 = (var9 = ((EntityLivingBase)var1).getHealth() - 5.0F) == 0.0F ? 0 : (var9 < 0.0F ? -1 : 1);
            if ((1407511888 >> 1 ^ 240188267 ^ 462575143 ^ 1009834980) == 0) {
                  ;
            }

            if (var8 <= 0) {
                  Esp.drawRect(1.1F, 0.5F, 1.1F, 0.5F, 476530885 << 1 >>> 3 ^ -119088335);
            }

            var8 = (189 >>> 4 ^ 9) >>> 1 ^ 3043;
            if (((1270941861 >>> 1 | 103704978 | 287353041) ^ 938405331) == 0) {
                  ;
            }

            GL11.glDisable(var8);
            if (((7674108 ^ 650479 | 5694004) ^ 8320567) == 0) {
                  ;
            }

            GL11.glPopMatrix();
            GL11.glEnable((1062 >>> 2 ^ 227) >>> 1 & 134 ^ 3061);
            if (((1374041872 | 1229244754) >> 4 >> 1 ^ -1868104184) != 0) {
                  ;
            }

            GL11.glEnable((((1020 | 941) ^ 956) >>> 3 | 6) ^ 3567);
            GL11.glDisable(2210 >> 1 >> 4 ^ 36 ^ 2947);
            if ((846319185 >>> 4 >>> 1 ^ -454076876) != 0) {
                  ;
            }

            GL11.glDisable(631 ^ 339 ^ 414 ^ 2458);
      }

      public static void a(float var0, float var1, float var2, float var3, int var4) {
            float var5 = (float)(var4 >> (((21 & 8 | 958225385) & 597117556) << 4 ^ 290760216) & (51 >> 4 << 1 >>> 2 ^ 254)) / 255.0F;
            if (((11025933 | 5721618) & 13909721 ^ 9744465 ^ -868712649) != 0) {
                  ;
            }

            if ((444797620 << 2 >>> 4 ^ -2112311519) != 0) {
                  ;
            }

            int var10000 = var4 >> ((12 & 5 | 1) >>> 1 ^ 18) & ((43 | 30 | 15) & 24 ^ 231);
            if (!"your mom your dad the one you never had".equals("stop skidding")) {
                  ;
            }

            float var6 = (float)var10000 / 255.0F;
            float var7 = (float)(var4 >> ((1 >>> 4 & 914896886 & 1644469370) >> 3 ^ 8) & ((215 | 47) ^ 243 ^ 11 ^ 248)) / 255.0F;
            float var8 = (float)(var4 & (119 << 3 << 1 << 2 ^ 1119 ^ 1802 ^ 7786)) / 255.0F;
            if (((1438107026 ^ 337379079 ^ 610781302) & 1010709816 ^ -702566714) != 0) {
                  ;
            }

            GL11.glPushMatrix();
            if ((335962145 >> 3 >>> 4 << 3 ^ -1760673443) != 0) {
                  ;
            }

            GL11.glEnable((2525 ^ 1170 | 3183) ^ 1677);
            GL11.glDisable(2395 >>> 1 >> 3 & 61 ^ 3572);
            GL11.glBlendFunc((673 >>> 3 ^ 23 | 45) ^ 877, (262 ^ 112) >>> 2 ^ 41 ^ 887);
            GL11.glEnable(325 >>> 4 & 6 ^ 2852);
            if ((1513683607 >>> 3 >>> 2 & 21621457 & 394718 ^ 208) == 0) {
                  ;
            }

            GL11.glColor4f(var6, var7, var8, var5);
            GL11.glBegin((0 >>> 4 >>> 4 | 694154025) >>> 1 ^ 347077011);
            GL11.glVertex2d((double)var2, (double)var1);
            double var9 = (double)var0;
            if ((1114368 ^ 1114368) == 0) {
                  ;
            }

            GL11.glVertex2d(var9, (double)var1);
            var9 = (double)var0;
            if (((1069631928 >> 4 ^ 47784393 | 2186883 | 17151869) ^ 19267583) == 0) {
                  ;
            }

            GL11.glVertex2d(var9, (double)var3);
            GL11.glVertex2d((double)var2, (double)var3);
            GL11.glEnd();
            var10000 = (671 >>> 4 >>> 4 ^ 0) >>> 2 ^ 3553;
            if (!"ape covered in human flesh".equals("nefariousMoment")) {
                  ;
            }

            GL11.glEnable(var10000);
            GL11.glDisable((1166 << 2 >>> 3 ^ 316) >>> 1 ^ 2655);
            GL11.glDisable((1640 | 1018 | 1476) ^ 274 ^ 3532);
            if ((964569656 >> 2 >>> 4 ^ 1908343 ^ 16312031) == 0) {
                  ;
            }

            GL11.glPopMatrix();
            if (!"yo mama name maurice".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                  ;
            }

      }

      public Esp() {
            super("Esp", "draws entities boxes", 1862638256 >> 2 ^ 381642423 ^ 226361371, Module.Category.RENDER);
            ArrayList var10001 = new ArrayList;
            if (((302477332 | 275980752) << 2 & 472000020 ^ 134219280) == 0) {
                  ;
            }

            var10001.<init>();
            if (!"your mom your dad the one you never had".equals("intentMoment")) {
                  ;
            }

            this.ENTITIES = var10001;
            this.BOX = (1909822757 | 1395091952) << 1 ^ -403702806;
            this.mobSet = new BooleanSetting("Mobs", this, (boolean)((891114926 ^ 595029491) >>> 1 << 3 ^ 1504239984));
            this.animalSet = new BooleanSetting("Animals", this, (boolean)(2117346642 >> 3 >>> 3 & 14995588 ^ 14733444));
            BooleanSetting var1 = new BooleanSetting;
            if ((((395387733 << 3 | 666972156) ^ 322253985) >>> 3 ^ 362774891) == 0) {
                  ;
            }

            var1.<init>("Players", this, (boolean)((0 ^ 1938564388) & 1820173596 & 79821672 ^ 8449));
            this.playerSet = var1;
            this.invisibleSet = new BooleanSetting("Invisible", this, (boolean)(((0 | 1324511058) >> 2 << 2 & 990757865) >> 4 ^ 10486837));
            ModeSetting var2 = new ModeSetting;
            if (((936795619 | 597372151) >>> 3 ^ 117174206) == 0) {
                  ;
            }

            String[] var10006 = new String[0 << 4 >> 3 ^ 1079715055 ^ 1079715053];
            var10006[(2143272736 << 1 >> 3 | 1938299231) >>> 1 ^ 2146959343] = "2D";
            if ((16880642 >> 2 & 1345177 ^ 1024) == 0) {
                  ;
            }

            var10006[0 >>> 3 << 3 ^ 1] = "BOX";
            var2.<init>("Mode", this, "2D", var10006);
            this.modeESP = var2;
            this.settings.add(this.mobSet);
            this.settings.add(this.animalSet);
            this.settings.add(this.playerSet);
            List var10000 = this.settings;
            if (!"please get a girlfriend and stop cracking plugins".equals("please take a shower")) {
                  ;
            }

            var1 = this.invisibleSet;
            if ((((1905767295 ^ 1841643530) & 268649227) >>> 3 ^ -1984418006) != 0) {
                  ;
            }

            var10000.add(var1);
            if (!"your mom your dad the one you never had".equals("please get a girlfriend and stop cracking plugins")) {
                  ;
            }

            this.settings.add(this.modeESP);
      }

      public static void entityESPBox(Entity var0, int var1) {
            if (((1851198911 ^ 1592059015) >> 1 & 128378704 ^ '\ue410') == 0) {
                  ;
            }

            GL11.glBlendFunc((733 >>> 3 ^ 35) << 2 >> 3 ^ 830, 263 >>> 1 ^ 10 ^ 906);
            if (((1769656455 | 825298637 | 339174292) ^ -1176996744) != 0) {
                  ;
            }

            int var10000 = ((776 | 357) << 1 ^ 951 | 278) ^ 3741;
            if (!"intentMoment".equals("shitted on you harder than archybot")) {
                  ;
            }

            GL11.glEnable(var10000);
            GL11.glLineWidth(2.0F);
            GL11.glDisable(3279 << 3 << 4 << 3 << 2 ^ 13434337);
            var10000 = 1616 >> 1 & 2 ^ 2929;
            if ((1535094411 >>> 2 & 318554124 ^ 316456960) == 0) {
                  ;
            }

            GL11.glDisable(var10000);
            GL11.glDepthMask((boolean)(1043566951 << 1 >>> 4 >> 3 ^ 16305733));
            if (var1 == 0) {
                  GL11.glColor4d(1.0D, 0.0D, 0.0D, 1.0D);
            }

            AxisAlignedBB var2 = new AxisAlignedBB;
            if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("nefariousMoment")) {
                  ;
            }

            AxisAlignedBB var10002 = var0.getEntityBoundingBox();
            if (!"buy a domain and everything else you need at namecheap.com".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                  ;
            }

            double var3 = var10002.minX;
            if (!"please take a shower".equals("buy a domain and everything else you need at namecheap.com")) {
                  ;
            }

            var3 = var3 - 0.05D - var0.posX;
            double var10003 = var0.posX;
            Minecraft var10004 = Minecraft.getMinecraft();
            if (!"you probably spell youre as your".equals("please go outside")) {
                  ;
            }

            var3 += var10003 - var10004.getRenderManager().viewerPosX;
            if ((((1470907688 ^ 622808188) << 2 ^ 2002211462 ^ 1546344852) << 1 ^ -1017095036) == 0) {
                  ;
            }

            var10003 = var0.getEntityBoundingBox().minY - var0.posY + (var0.posY - Minecraft.getMinecraft().getRenderManager().viewerPosY);
            double var4 = var0.getEntityBoundingBox().minZ - 0.05D - var0.posZ;
            double var10005 = var0.posZ;
            double var10006 = Minecraft.getMinecraft().getRenderManager().viewerPosZ;
            if ((56156161 >>> 1 & 2214835 ^ 1630861980) != 0) {
                  ;
            }

            var4 += var10005 - var10006;
            var10005 = var0.getEntityBoundingBox().maxX + 0.05D - var0.posX;
            var10006 = var0.posX;
            Minecraft var10007 = Minecraft.getMinecraft();
            if ((1487011872 << 1 ^ 1813449065) != 0) {
                  ;
            }

            var10005 += var10006 - var10007.getRenderManager().viewerPosX;
            var10006 = var0.getEntityBoundingBox().maxY + 0.1D;
            if (((12247684 << 2 & 11435878 | 10220461) & 11895987 ^ -475850154) != 0) {
                  ;
            }

            var10006 = var10006 - var0.posY + (var0.posY - Minecraft.getMinecraft().getRenderManager().viewerPosY);
            double var5 = var0.getEntityBoundingBox().maxZ + 0.05D;
            if (((8392704 | 7253021) ^ 1427057 ^ 16480876) == 0) {
                  ;
            }

            var2.<init>(var3, var10003, var4, var10005, var10006, var5 - var0.posZ + (var0.posZ - Minecraft.getMinecraft().getRenderManager().viewerPosZ));
            RenderGlobal.drawSelectionBoundingBox(var2, 1.0F, 0.0F, 0.0F, 1.0F);
            GL11.glEnable(((498 & 481) >> 3 | 57) ^ 46 ^ 3570);
            GL11.glEnable((365 >> 2 >> 4 | 1) & 0 ^ 2929);
            if (!"i hope you catch fire ngl".equals("you probably spell youre as your")) {
                  ;
            }

            GL11.glDepthMask((boolean)((0 >>> 2 & 1770597294) << 4 ^ 1));
            GL11.glDisable((1982 | 180) << 1 << 1 ^ 5402);
      }
}
