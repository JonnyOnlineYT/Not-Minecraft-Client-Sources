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
      private transient ArrayList ENTITIES;
      public BooleanSetting animalSet;
      public BooleanSetting playerSet;
      public ModeSetting modeESP;
      public BooleanSetting mobSet;
      private transient int BOX;
      public BooleanSetting invisibleSet;

      private void ab(Entity var1, double var2, double var4, double var6) {
            GL11.glPushMatrix();
            if (((16672 | 10050) >>> 3 << 4 ^ 1466818913) != 0) {
                  ;
            }

            GL11.glTranslatef((float)var2, (float)var4, (float)var6);
            GL11.glNormal3f(0.0F, 1.0F, 0.0F);
            GL11.glRotatef(-mc.getRenderManager().playerViewX, 0.0F, 0.2F, 0.0F);
            GL11.glRotatef(mc.getRenderManager().playerViewY, 1.0F, 0.0F, 0.0F);
            GL11.glEnable((390 >> 2 ^ 3) & 12 ^ 3042);
            GL11.glBlendFunc(((194 & 79 | 2) << 2 | 207) ^ 717, (19 & 0 ^ 1531850264) >>> 2 ^ 382961797);
            GL11.glEnable((1756 | 222) & 961 ^ 2528);
            GL11.glLineWidth(2.0F);
            GL11.glDisable((2775 >> 3 >>> 2 | 23) >>> 1 ^ 3530);
            GL11.glDisable(((1533 & 221) >>> 4 ^ 9) >>> 4 ^ 2929);
            if (((EntityLivingBase)var1).getHealth() <= 20.0F) {
                  Esp.drawRect(1.1F, 0.5F, 1.1F, 0.5F, (1632481955 | 547350209) & 781679418 ^ -544504908);
            }

            if (((EntityLivingBase)var1).getHealth() <= 10.0F) {
                  Esp.drawRect(1.1F, 2.0F, 1.7F, 2.0F, 1580965495 >>> 3 << 2 >>> 3 ^ -98613685);
            }

            float var10000 = ((EntityLivingBase)var1).getHealth();
            if (((1662098650 >>> 2 << 3 | 1436287675) ^ -675333189) == 0) {
                  ;
            }

            if (var10000 <= 5.0F) {
                  Esp.drawRect(1.1F, 0.5F, 1.1F, 0.5F, (575490638 << 1 | 326562490) << 3 >>> 1 ^ -1610296328);
            }

            GL11.glDisable((1798 >>> 3 >>> 2 & 14) << 3 ^ 2978);
            GL11.glPopMatrix();
            int var8 = 575 >> 4 >>> 3 << 4 >> 3 ^ 2937;
            if ((21472352 << 3 >>> 3 >>> 4 ^ 1342022) == 0) {
                  ;
            }

            GL11.glEnable(var8);
            GL11.glEnable(2788 >> 3 >>> 3 >>> 3 >>> 2 ^ 3552);
            GL11.glDisable(1274 << 4 & 17694 & 12875 & 1094541115 ^ 3042);
            GL11.glDisable((1705 ^ 972) >> 2 << 4 >> 2 ^ 3652);
      }

      public static void a(double var0, double var2, double var4, double var6, float var8, int var9, int var10) {
            if ((781162345 >>> 3 >>> 2 >>> 1 ^ 12205661) == 0) {
                  ;
            }

            Esp.a((float)var0, (float)var2, (float)var4, (float)var6, var10);
            float var11 = (float)(var9 >> (6 & 0 & 939437608 ^ 1258086735 ^ 1258086743) & (91 >> 1 & 41 ^ 6 ^ 7 ^ 215)) / 255.0F;
            float var12 = (float)(var9 >> ((1 | 0 | 0) ^ 17) & (((153 | 37) ^ 144) << 3 >> 2 ^ 165)) / 255.0F;
            int var10000 = var9 >> ((0 >>> 1 >> 4 >> 1 & 510314425 | 687546201) ^ 687546193);
            if (!"buy a domain and everything else you need at namecheap.com".equals("your mom your dad the one you never had")) {
                  ;
            }

            float var13 = (float)(var10000 & (88 ^ 17 ^ 36 ^ 76 ^ 222)) / 255.0F;
            float var14 = (float)(var9 & ((41 & 33) >>> 3 << 1 << 4 ^ 127)) / 255.0F;
            GL11.glPushMatrix();
            if ((150053893 >> 3 << 2 ^ 75026944) == 0) {
                  ;
            }

            GL11.glEnable((2605 << 3 >> 1 | 4432) ^ 12822);
            if (!"please dont crack my plugin".equals("please get a girlfriend and stop cracking plugins")) {
                  ;
            }

            GL11.glDisable((456 << 4 & 4584) >> 4 >> 2 ^ 3491);
            GL11.glBlendFunc((302 << 2 & 162) >>> 1 ^ 850, 318 << 2 >>> 2 << 4 >>> 1 ^ 2803);
            GL11.glEnable(((2187 | 614) & 2033 | 135) ^ 2503);
            if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("intentMoment")) {
                  ;
            }

            GL11.glColor4f(var12, var13, var14, var11);
            GL11.glLineWidth(2.0F);
            GL11.glBegin(0 >> 3 << 3 << 2 << 3 ^ 1);
            GL11.glVertex2d(var0, var2);
            if (!"stop skidding".equals("shitted on you harder than archybot")) {
                  ;
            }

            GL11.glVertex2d(var0, var6);
            if (((1374436996 >> 1 >> 1 ^ 289585815) >>> 1 ^ -600653743) != 0) {
                  ;
            }

            GL11.glVertex2d(var4, var6);
            GL11.glVertex2d(var4, var2);
            GL11.glVertex2d(var0, var2);
            if (((1047693793 >> 2 | 74957122) ^ 268428154) == 0) {
                  ;
            }

            GL11.glVertex2d(var4, var2);
            if ((((217441317 | 77824956) ^ 78607598) << 1 ^ 280045222) == 0) {
                  ;
            }

            GL11.glVertex2d(var0, var6);
            if ((((1059541268 | 157537139) << 3 & 973334404) << 2 ^ -1661656173) != 0) {
                  ;
            }

            GL11.glVertex2d(var4, var6);
            GL11.glEnd();
            GL11.glEnable(((480 | 425) >>> 2 | 47) ^ 3486);
            GL11.glDisable(1605 >>> 2 >>> 3 >>> 1 ^ 9 ^ 3058);
            GL11.glDisable(1457 >>> 1 >> 1 & 257 ^ 2592);
            GL11.glPopMatrix();
      }

      public static void entityESPBox(Entity var0, int var1) {
            GL11.glBlendFunc(761 << 3 << 3 << 1 ^ 98178, ((262 | 21) >>> 4 >> 4 | 0) ^ 770);
            GL11.glEnable((1405 | 792) ^ 382 ^ 807 ^ 3782);
            if (((498353319 ^ 67626716) << 4 >> 3 ^ 1089792027 ^ -1282167571) == 0) {
                  ;
            }

            GL11.glLineWidth(2.0F);
            if ((941585468 << 1 >> 1 >>> 4 >>> 1 ^ 1080517185) != 0) {
                  ;
            }

            GL11.glDisable((929 ^ 728) >>> 4 >> 4 ^ 3552);
            GL11.glDisable(((844 | 830) >>> 1 | 352) >> 3 ^ 2894);
            if ((890482649 ^ 43866081 ^ 678612966 ^ 104120482 ^ -1496185437) != 0) {
                  ;
            }

            int var10000 = (1057668307 ^ 211700240 | 823032932) ^ 866057959;
            if ((((747610022 ^ 156804352) >> 1 & 244704023) >>> 1 ^ 21020809) == 0) {
                  ;
            }

            GL11.glDepthMask((boolean)var10000);
            if (var1 == 0) {
                  GL11.glColor4d(1.0D, 0.0D, 0.0D, 1.0D);
            }

            AxisAlignedBB var2 = new AxisAlignedBB;
            if ((1647582818 << 1 & 766081295 ^ 27255393 ^ 95935077) == 0) {
                  ;
            }

            double var10002 = var0.getEntityBoundingBox().minX - 0.05D;
            if (((1260521434 >>> 3 ^ 69377722) >>> 2 >>> 2 ^ 13921764) == 0) {
                  ;
            }

            var10002 -= var0.posX;
            if (((630978417 << 3 ^ 204160449 | 274442679) ^ 822063103) == 0) {
                  ;
            }

            double var10003 = var0.posX;
            double var10004 = Minecraft.getMinecraft().getRenderManager().viewerPosX;
            if (((1040618928 ^ 7683018) << 4 ^ -415570016) == 0) {
                  ;
            }

            var10002 += var10003 - var10004;
            var10003 = var0.getEntityBoundingBox().minY - var0.posY + (var0.posY - Minecraft.getMinecraft().getRenderManager().viewerPosY);
            var10004 = var0.getEntityBoundingBox().minZ - 0.05D - var0.posZ;
            double var10005 = var0.posZ - Minecraft.getMinecraft().getRenderManager().viewerPosZ;
            if (!"stop skidding".equals("minecraft")) {
                  ;
            }

            var10004 += var10005;
            var10005 = var0.getEntityBoundingBox().maxX + 0.05D;
            if ((1074684812 << 3 >>> 3 >>> 3 ^ 117873) == 0) {
                  ;
            }

            var10005 = var10005 - var0.posX + (var0.posX - Minecraft.getMinecraft().getRenderManager().viewerPosX);
            if (((1282965413 << 2 ^ 430827499) >>> 3 ^ 84509487) == 0) {
                  ;
            }

            double var10006 = var0.getEntityBoundingBox().maxY + 0.1D - var0.posY + (var0.posY - Minecraft.getMinecraft().getRenderManager().viewerPosY);
            double var10007 = var0.getEntityBoundingBox().maxZ;
            if (((130779653 | 45735059) & 125400277 ^ 125392021) == 0) {
                  ;
            }

            var2.<init>(var10002, var10003, var10004, var10005, var10006, var10007 + 0.05D - var0.posZ + (var0.posZ - Minecraft.getMinecraft().getRenderManager().viewerPosZ));
            RenderGlobal.drawSelectionBoundingBox(var2, 1.0F, 0.0F, 0.0F, 1.0F);
            if ((((375741962 | 165802476) ^ 19191968) >>> 2 >> 2 ^ 1361624474) != 0) {
                  ;
            }

            var10000 = (1414 & 224) >>> 1 ^ 3489;
            if (((1409442 >>> 1 >> 4 | '雸') << 4 ^ 1991002401) != 0) {
                  ;
            }

            GL11.glEnable(var10000);
            GL11.glEnable(2551 << 3 ^ 20147 ^ 2682);
            GL11.glDepthMask((boolean)((0 | 1202816324) >> 1 ^ 601408163));
            if (((46455843 >> 4 | 440331 | 767197) ^ 64885022) != 0) {
                  ;
            }

            var10000 = 240 << 3 >> 1 << 1 ^ 264 ^ 3434;
            if (!"idiot".equals("minecraft")) {
                  ;
            }

            GL11.glDisable(var10000);
      }

      public static void a(float var0, float var1, float var2, float var3, int var4) {
            float var5 = (float)(var4 >> (((0 | 438098226) ^ 202758682 | 15045771) ^ 384669619) & ((154 & 79) >>> 4 ^ 255)) / 255.0F;
            float var6 = (float)(var4 >> ((0 << 1 >> 3 & 155090089) >> 3 ^ 16) & ((195 & 152 | 63) << 3 ^ 1287)) / 255.0F;
            if (!"i hope you catch fire ngl".equals("yo mama name maurice")) {
                  ;
            }

            if ((133614352 << 2 >>> 4 ^ 28517225 ^ 5027284 ^ 859014637) != 0) {
                  ;
            }

            float var7 = (float)(var4 >> (0 >> 4 & 1289417568 ^ 8) & (179 << 3 >>> 4 << 3 >>> 3 ^ 166)) / 255.0F;
            if (((1476175515 | 200692165) >>> 1 << 3 & 2055930580 ^ -1011022101) != 0) {
                  ;
            }

            float var8 = (float)(var4 & ((195 >>> 1 >>> 1 >> 1 ^ 12) << 2 ^ 175)) / 255.0F;
            GL11.glPushMatrix();
            int var10000 = (1941 & 1192 ^ 295) & 1136 ^ 4034;
            if (!"please go outside".equals("stringer is a good obfuscator")) {
                  ;
            }

            GL11.glEnable(var10000);
            GL11.glDisable(3271 >> 1 << 3 ^ 16121);
            GL11.glBlendFunc(551 >>> 3 >>> 4 << 1 ^ 778, (((591 | 76) & 375) << 2 << 4 | 3023) ^ 6348);
            GL11.glEnable((2782 | 890 | 2023) ^ 1247);
            if ((((857504445 | 770498966) >>> 2 | 226734908) >> 1 ^ -1874746561) != 0) {
                  ;
            }

            GL11.glColor4f(var6, var7, var8, var5);
            GL11.glBegin(6 << 3 >>> 4 ^ 4);
            double var9 = (double)var2;
            if ((352322536 >> 1 & 83301186 ^ -416926339) != 0) {
                  ;
            }

            GL11.glVertex2d(var9, (double)var1);
            var9 = (double)var0;
            if (((1817248448 | 316357917) >>> 1 ^ -1258105002) != 0) {
                  ;
            }

            GL11.glVertex2d(var9, (double)var1);
            GL11.glVertex2d((double)var0, (double)var3);
            var9 = (double)var2;
            if ((((584883474 | 284718870) ^ 339493783) >> 1 ^ 325082688) == 0) {
                  ;
            }

            GL11.glVertex2d(var9, (double)var3);
            GL11.glEnd();
            GL11.glEnable((747 | 130) ^ 422 ^ 485 ^ 423 ^ 3822);
            GL11.glDisable(186 << 3 >> 3 >>> 1 ^ 47 ^ 2960);
            GL11.glDisable((345 & 115) >> 4 ^ 2853);
            GL11.glPopMatrix();
      }

      public void onRenderWorldLast(float var1) {
            if (mc.player != null && !mc.player.isDead) {
                  List var2 = (List)mc.world.loadedEntityList.stream().filter((var0) -> {
                        return (boolean)(var0 != mc.player ? ((0 | 543692009) ^ 44972361 | 58957816) ^ 600293369 : ((1429534858 << 1 | 1496620985) ^ 615442153) >> 1 & 1312442702 ^ 1311246346);
                  }).filter((var0) -> {
                        return (boolean)(!var0.isDead ? (0 << 4 >> 2 | 110251903) ^ 110251902 : 1522234595 >>> 4 << 2 >> 4 ^ 23784915);
                  }).sorted(Comparator.comparing((var0) -> {
                        Float var10000 = Float.valueOf(mc.player.getDistance(var0));
                        if (((282962722 ^ 128261134) << 1 >>> 1 ^ 116600399) != 0) {
                              ;
                        }

                        return var10000;
                  })).collect(Collectors.toList());
                  Iterator var3 = var2.iterator();

                  while(true) {
                        Entity var4;
                        double var5;
                        double var7;
                        double var9;
                        double var11;
                        double var12;
                        label212:
                        while(true) {
                              while(true) {
                                    do {
                                          do {
                                                if (!var3.hasNext()) {
                                                      return;
                                                }

                                                var4 = (Entity)var3.next();
                                          } while(var4 == null);
                                    } while(var4 == mc.player);

                                    if (!"i hope you catch fire ngl".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                                          ;
                                    }

                                    if (this.modeESP.activeMode == "2D") {
                                          BooleanSetting var10000 = this.invisibleSet;
                                          if (((519412498 >>> 4 << 1 & 15393265 | 3228396) ^ -1283493649) != 0) {
                                                ;
                                          }

                                          if (var10000.isEnabled() && var4.isInvisible()) {
                                                if (!"yo mama name maurice".equals("stop skidding")) {
                                                      ;
                                                }

                                                if (!"Some babies were dropped on their heads but you were clearly thrown at a wall.".equals("your mom your dad the one you never had")) {
                                                      ;
                                                }

                                                if (var4 instanceof EntityPlayer && this.playerSet.isEnabled() || var4 instanceof EntityMob && this.mobSet.isEnabled()) {
                                                      break label212;
                                                }

                                                if (var4 instanceof EntityAnimal) {
                                                      var10000 = this.animalSet;
                                                      if ((505678131 << 4 >>> 4 >>> 4 ^ 14827667) == 0) {
                                                            ;
                                                      }

                                                      if (var10000.isEnabled()) {
                                                            if ((((1081537637 >> 4 | 58305632) & 40394844) << 3 ^ 323158560) == 0) {
                                                                  ;
                                                            }
                                                            break label212;
                                                      }
                                                }
                                          }

                                          if (this.mobSet.isEnabled() && var4 instanceof EntityMob) {
                                                var5 = var4.lastTickPosX + (var4.posX - var4.lastTickPosX) - mc.getRenderManager().viewerPosX;
                                                if (((226582201 ^ 18325292 | 131524548) >>> 2 & 20493947 ^ 19968113) == 0) {
                                                      ;
                                                }

                                                var11 = var4.lastTickPosY + (var4.posY - var4.lastTickPosY);
                                                RenderManager var13 = mc.getRenderManager();
                                                if (!"i hope you catch fire ngl".equals("shitted on you harder than archybot")) {
                                                      ;
                                                }

                                                var7 = var11 - var13.viewerPosY;
                                                if (((1104676093 >> 2 << 3 | 530999621) ^ -1615435267) == 0) {
                                                      ;
                                                }

                                                var11 = var4.lastTickPosZ;
                                                var12 = var4.posZ;
                                                if ((4097 << 1 >> 2 ^ 2048) == 0) {
                                                      ;
                                                }

                                                var9 = var11 + (var12 - var4.lastTickPosZ) - mc.getRenderManager().viewerPosZ;
                                                this.a(var4, var5, var7, var9);
                                                this.ab(var4, var5, var7, var9);
                                          } else {
                                                Minecraft var10001;
                                                if (this.animalSet.isEnabled() && var4 instanceof EntityAnimal) {
                                                      var11 = var4.lastTickPosX + (var4.posX - var4.lastTickPosX);
                                                      var10001 = mc;
                                                      if ((((407825530 ^ 184139892) >>> 4 & 8804744) >> 1 ^ -1000817781) != 0) {
                                                            ;
                                                      }

                                                      var5 = var11 - var10001.getRenderManager().viewerPosX;
                                                      var11 = var4.lastTickPosY;
                                                      var12 = var4.posY;
                                                      double var10002 = var4.lastTickPosY;
                                                      if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("ape covered in human flesh")) {
                                                            ;
                                                      }

                                                      var11 += var12 - var10002;
                                                      var12 = mc.getRenderManager().viewerPosY;
                                                      if (((447085590 >>> 3 ^ 48732035 ^ 14957847) & 21817122 ^ 762525409) != 0) {
                                                            ;
                                                      }

                                                      var7 = var11 - var12;
                                                      if (((1868010385 >> 2 << 4 | 1912825009) ^ 357768650) != 0) {
                                                            ;
                                                      }

                                                      var11 = var4.lastTickPosZ + (var4.posZ - var4.lastTickPosZ) - mc.getRenderManager().viewerPosZ;
                                                      if (((2061950134 | 402077120) >> 2 ^ 536739709) == 0) {
                                                            ;
                                                      }

                                                      var9 = var11;
                                                      if ((330873493 << 1 << 4 >>> 4 ^ 425299741) != 0) {
                                                            ;
                                                      }

                                                      this.a(var4, var5, var7, var9);
                                                      this.ab(var4, var5, var7, var9);
                                                } else {
                                                      if (!"please dont crack my plugin".equals("stringer is a good obfuscator")) {
                                                            ;
                                                      }

                                                      var10000 = this.playerSet;
                                                      if (((138104846 | 19396752) << 2 ^ 437785106 ^ 1055387754) == 0) {
                                                            ;
                                                      }

                                                      if (var10000.isEnabled() && var4 instanceof EntityPlayer) {
                                                            var5 = var4.lastTickPosX + (var4.posX - var4.lastTickPosX) - mc.getRenderManager().viewerPosX;
                                                            var11 = var4.lastTickPosY + (var4.posY - var4.lastTickPosY);
                                                            var10001 = mc;
                                                            if ((10436171 >>> 3 >>> 4 << 4 << 1 ^ 122561172) != 0) {
                                                                  ;
                                                            }

                                                            var7 = var11 - var10001.getRenderManager().viewerPosY;
                                                            var11 = var4.lastTickPosZ;
                                                            var12 = var4.posZ;
                                                            if (((405346113 >>> 2 ^ 49435585 ^ 24727196 | 87953171) >> 3 ^ -291590236) != 0) {
                                                                  ;
                                                            }

                                                            var9 = var11 + (var12 - var4.lastTickPosZ) - mc.getRenderManager().viewerPosZ;
                                                            this.a(var4, var5, var7, var9);
                                                            this.ab(var4, var5, var7, var9);
                                                      }
                                                }
                                          }
                                    } else {
                                          if ((307335681 >>> 3 >>> 1 ^ 19208480) == 0) {
                                                ;
                                          }

                                          if (this.invisibleSet.isEnabled() && var4.isInvisible()) {
                                                label229: {
                                                      label223: {
                                                            if (var4 instanceof EntityPlayer) {
                                                                  if (this.playerSet.isEnabled()) {
                                                                        break label223;
                                                                  }

                                                                  if ((((1536785892 ^ 217544345) & 1142805672) << 3 ^ 543162688) == 0) {
                                                                        ;
                                                                  }
                                                            }

                                                            if ((!(var4 instanceof EntityMob) || !this.mobSet.isEnabled()) && (!(var4 instanceof EntityAnimal) || !this.animalSet.isEnabled())) {
                                                                  break label229;
                                                            }
                                                      }

                                                      Esp.entityESPBox(var4, (202384144 ^ 201965745) >> 2 ^ 424168);
                                                      continue;
                                                }
                                          }

                                          if (this.mobSet.isEnabled() && var4 instanceof EntityMob) {
                                                Esp.entityESPBox(var4, (3019428 | 88626) ^ 3103414);
                                          } else if (this.animalSet.isEnabled() && var4 instanceof EntityAnimal) {
                                                Esp.entityESPBox(var4, (1518669798 ^ 101952968 | 1287564729 | 842724086) ^ 2126446591);
                                          } else if (this.playerSet.isEnabled() && var4 instanceof EntityPlayer) {
                                                Esp.entityESPBox(var4, 311816843 >>> 4 << 2 >> 4 << 1 ^ 9744276);
                                          }
                                    }
                              }
                        }

                        var11 = var4.lastTickPosX;
                        var12 = var4.posX - var4.lastTickPosX;
                        if ((39600678 << 1 << 3 >> 1 >> 4 ^ -1352675303) != 0) {
                              ;
                        }

                        var5 = var11 + var12 - mc.getRenderManager().viewerPosX;
                        var7 = var4.lastTickPosY + (var4.posY - var4.lastTickPosY) - mc.getRenderManager().viewerPosY;
                        var9 = var4.lastTickPosZ + (var4.posZ - var4.lastTickPosZ) - mc.getRenderManager().viewerPosZ;
                        if (((82498692 >> 2 & 16458834 | 2035164) ^ 1600476844) != 0) {
                              ;
                        }

                        this.a(var4, var5, var7, var9);
                        if ((2109578 ^ -780017034) != 0) {
                              ;
                        }

                        this.ab(var4, var5, var7, var9);
                  }
            }
      }

      public Esp() {
            super("Esp", "draws entities boxes", (1708486400 | 1592878718) >> 1 ^ 763544314 ^ 309884229, Module.Category.RENDER);
            if ((1101733230 >> 1 ^ 27579174 ^ 561077649) == 0) {
                  ;
            }

            this.ENTITIES = new ArrayList();
            this.BOX = ((225685240 | 121116825) ^ 189038191) >> 2 ^ 17821733;
            BooleanSetting var10001 = new BooleanSetting;
            if ((16786692 ^ 16786692) == 0) {
                  ;
            }

            var10001.<init>("Mobs", this, (boolean)(2145439550 << 2 >> 1 ^ -4088196));
            this.mobSet = var10001;
            this.animalSet = new BooleanSetting("Animals", this, (boolean)((1706657419 ^ 1517980919) >> 3 ^ 29336284 ^ 105366419));
            this.playerSet = new BooleanSetting("Players", this, (boolean)((0 & 1513496435) >> 4 << 2 ^ 528503907 ^ 528503906));
            var10001 = new BooleanSetting;
            if (!"please take a shower".equals("minecraft")) {
                  ;
            }

            var10001.<init>("Invisible", this, (boolean)(((0 << 3 ^ 1939477349) >>> 1 | 859491899 | 977597611) ^ 1006632890));
            this.invisibleSet = var10001;
            if (((1707331485 ^ 688566491) << 3 ^ 1716255280) == 0) {
                  ;
            }

            ModeSetting var1 = new ModeSetting;
            if ((((216372863 | 108684472) & 101837461) >>> 4 ^ 887835929) != 0) {
                  ;
            }

            String[] var10006 = new String[(1 >>> 4 << 2 << 4 ^ 165289709) & 104427917 ^ 1577103];
            var10006[173709 >>> 2 ^ 'ꦣ'] = "2D";
            int var10008 = (0 >> 1 >> 1 ^ 1109837668) >> 4 ^ 69364855;
            if (((1758375521 << 3 ^ 1055688297) >>> 1 >> 4 ^ -998849205) != 0) {
                  ;
            }

            var10006[var10008] = "BOX";
            var1.<init>("Mode", this, "2D", var10006);
            this.modeESP = var1;
            this.settings.add(this.mobSet);
            if ((1528072874 >> 3 << 1 ^ 2065879493) != 0) {
                  ;
            }

            if ((((72887304 | 41831044) >>> 3 & 13370011) << 3 ^ 106955912) == 0) {
                  ;
            }

            this.settings.add(this.animalSet);
            if (!"minecraft".equals("idiot")) {
                  ;
            }

            this.settings.add(this.playerSet);
            this.settings.add(this.invisibleSet);
            this.settings.add(this.modeESP);
      }

      public static void drawRect(float var0, float var1, float var2, float var3, int var4) {
            float var5;
            if (var0 < var2) {
                  if ((561969827 >>> 1 >> 2 ^ 1814755244) != 0) {
                        ;
                  }

                  var5 = var0;
                  var0 = var2;
                  var2 = var5;
            }

            if (var1 < var3) {
                  var5 = var1;
                  var1 = var3;
                  if ((762336072 >> 3 >>> 3 ^ 11911501) == 0) {
                        ;
                  }

                  var3 = var5;
            }

            int var10000 = var4 >> ((7 << 3 ^ 28 | 22) << 2 ^ 192) & ((225 & 167) >> 2 ^ 215);
            if ((914587123 << 4 >>> 4 << 3 ^ 1694237023) != 0) {
                  ;
            }

            var5 = (float)var10000 / 255.0F;
            float var6 = (float)(var4 >> ((8 & 0 | 34007925) ^ 34007909) & (80 << 3 << 1 >>> 2 ^ 447)) / 255.0F;
            float var7 = (float)(var4 >> (((7 | 3) >>> 4 ^ 1061341346) >>> 3 ^ 132667676) & (141 >>> 1 >>> 1 ^ 220)) / 255.0F;
            float var11 = (float)(var4 & ((106 & 53) >> 4 & 0 ^ 255));
            if ((((824304361 | 323868305) & 267932413) >> 4 ^ -401564654) != 0) {
                  ;
            }

            float var8 = var11 / 255.0F;
            Tessellator var9 = Tessellator.getInstance();
            BufferBuilder var10 = var9.getBuffer();
            GlStateManager.enableBlend();
            GlStateManager.disableTexture2D();
            GlStateManager.tryBlendFuncSeparate(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA, SourceFactor.ONE, DestFactor.ZERO);
            if (((167227526 >>> 1 | 81770161 | 38195050) << 3 ^ 758477955) != 0) {
                  ;
            }

            GlStateManager.color(var6, var7, var8, var5);
            if (((713662029 | 169778236) >>> 1 << 3 ^ 284081445) != 0) {
                  ;
            }

            var10.begin(((1 >> 2 | 804079490) & 392939118 | 98026030) ^ 134202921, DefaultVertexFormats.POSITION);
            double var10001 = (double)var0;
            double var10002 = (double)var3;
            if ((((1267368980 ^ 84627832) & 999126115 & 77121543 | 3370668) ^ 11759276) == 0) {
                  ;
            }

            var10.pos(var10001, var10002, 0.0D).endVertex();
            if ((1530049254 ^ 1144243339 ^ 146607870 ^ 138793879 ^ -1844019559) != 0) {
                  ;
            }

            var10.pos((double)var2, (double)var3, 0.0D).endVertex();
            if (((791025250 ^ 711640477 | 36436279) << 3 ^ 998236152) == 0) {
                  ;
            }

            var10.pos((double)var2, (double)var1, 0.0D).endVertex();
            var10001 = (double)var0;
            if (!"yo mama name maurice".equals("You're so fat whenever you go to the beach the tide comes in.")) {
                  ;
            }

            var10.pos(var10001, (double)var1, 0.0D).endVertex();
            var9.draw();
            GlStateManager.enableTexture2D();
            GlStateManager.disableBlend();
      }

      private void a(Entity var1, double var2, double var4, double var6) {
            GL11.glPushMatrix();
            float var10000 = (float)var2;
            float var10001 = (float)var4;
            if ((17958912 << 2 >>> 4 ^ 4489728) == 0) {
                  ;
            }

            GL11.glTranslatef(var10000, var10001, (float)var6);
            GL11.glNormal3f(0.0F, 1.0F, 0.0F);
            GL11.glRotatef(-mc.getRenderManager().playerViewY, 0.0F, 0.2F, 0.0F);
            GL11.glRotatef(mc.getRenderManager().playerViewX, 1.0F, 0.0F, 0.0F);
            GL11.glEnable((1987 << 1 | 2760) >> 4 ^ 2846);
            GL11.glBlendFunc(553 << 2 & 519 ^ 774, 542 & 456 & 0 ^ 771);
            GL11.glEnable((1909 ^ 1437 | 380) ^ 2268);
            GL11.glLineWidth(2.0F);
            if (!"please get a girlfriend and stop cracking plugins".equals("intentMoment")) {
                  ;
            }

            if (!"ape covered in human flesh".equals("You're so fat whenever you go to the beach the tide comes in.")) {
                  ;
            }

            GL11.glDisable(2041 << 1 << 1 ^ 7647 ^ 2411);
            if (!"idiot".equals("minecraft")) {
                  ;
            }

            GL11.glDisable((2490 | 1630) >> 1 << 2 ^ 1087 ^ 4274);
            Tessellator var8 = Tessellator.getInstance();
            BufferBuilder var9 = var8.getBuffer();
            if (!"idiot".equals("shitted on you harder than archybot")) {
                  ;
            }

            if (((1231981694 ^ 378206990) >> 3 >>> 4 ^ 12568814) == 0) {
                  ;
            }

            var10000 = ((EntityLivingBase)var1).getHealth();
            if (!"please dont crack my plugin".equals("ape covered in human flesh")) {
                  ;
            }

            if (var10000 <= 20.0F) {
                  Esp.drawRect(-0.7F, ((EntityLivingBase)var1).getHealth() / 10.0F, -0.58F, 0.0F, (new Color(((48 << 3 | 328) & 445 ^ 285) >>> 1 ^ 181, 1048592 ^ 1048592, (1767518250 ^ 213644848) >>> 2 & 22588545 ^ 22552704, (20 | 15 | 16) ^ 224)).getRGB());
                  if (!"intentMoment".equals("ape covered in human flesh")) {
                        ;
                  }

                  Esp.drawRect(-0.71F, 2.01F, -0.58F, 2.0F, (new Color(((47 ^ 38) >> 1 >> 3 | 1914920212) ^ 1914920227, (102 ^ 31) >>> 2 & 22 ^ 233, ((4 | 3) >>> 2 ^ 0) >> 2 ^ 55, (87 >> 2 | 11 | 15) ^ 13 ^ 237)).getRGB());
                  if (((1936132285 ^ 903423181 ^ 1177402574 | 6059000) ^ 2709053 ^ -1731933843) != 0) {
                        ;
                  }

                  Esp.drawRect(-0.71F, 2.01F, -0.7F, 0.01F, (new Color(24 >>> 1 >> 2 ^ 52, (111 & 58) >>> 2 ^ 245, (23 & 12 ^ 2) >> 4 ^ 55, ((223 | 140) & 176 | 11) ^ 100)).getRGB());
                  if (((560491092 >>> 3 ^ 18746076) >> 1 ^ -1369813721) != 0) {
                        ;
                  }

                  Color var10004 = new Color;
                  if (((1026949100 ^ 5582976) << 4 >> 3 >>> 3 ^ -1217196619) != 0) {
                        ;
                  }

                  var10004.<init>(((15 | 11) >> 3 >>> 1 & 1540052093) << 2 ^ 55, (168 | 96) << 2 ^ 863, 45 >> 4 >> 4 >>> 4 ^ 55, (186 & 95 | 18 | 4) ^ 225);
                  Esp.drawRect(-0.59F, 0.01F, -0.58F, 2.0F, var10004.getRGB());
                  if (!"stop skidding".equals("intentMoment")) {
                        ;
                  }

                  Esp.drawRect(-0.58F, 0.01F, -0.71F, 0.0F, (new Color((14 | 3) ^ 2 ^ 58, (6 >>> 3 ^ 986981744) >> 4 ^ 61686440, 21 >> 4 << 2 ^ 51, (21 ^ 11) >>> 3 << 1 ^ 249)).getRGB());
                  if (!"intentMoment".equals("please go outside")) {
                        ;
                  }

                  int var10005 = (new Color((17 >> 2 >> 2 | 0) ^ 54, 78 & 66 & 21 ^ 659048215 ^ 659048424, 10 >> 4 & 836070703 & 408729922 ^ 55, 128 << 3 >>> 1 >>> 4 << 4 << 2 ^ 2303)).getRGB();
                  if (!"stop skidding".equals("please take a shower")) {
                        ;
                  }

                  Color var10006 = new Color;
                  int var10008 = (145 & 81) >>> 2 ^ 251;
                  int var10009 = (1513058604 >> 1 ^ 350838670) >>> 4 ^ 60812913;
                  int var10010 = 1754145002 >> 1 >> 1 >> 1 ^ 219268125;
                  if (!"minecraft".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                        ;
                  }

                  var10006.<init>(var10008, var10009, var10010, (38 | 25) >>> 3 << 1 & 13 ^ 243);
                  Esp.a(-0.5D, 2.0D, 0.5D, 0.0D, 2.5F, var10005, var10006.getRGB());
            }

            GL11.glDisable((244 ^ 26) >>> 1 & 118 ^ 2964);
            GL11.glPopMatrix();
            GL11.glEnable((714 >> 4 >>> 1 | 4) ^ 2919);
            GL11.glEnable(490 & 66 & 26 ^ 3555);
            GL11.glDisable((461 ^ 129) >>> 4 >>> 1 ^ 3048);
            GL11.glDisable((1897 & 412) >>> 3 << 4 ^ 2352);
      }
}
