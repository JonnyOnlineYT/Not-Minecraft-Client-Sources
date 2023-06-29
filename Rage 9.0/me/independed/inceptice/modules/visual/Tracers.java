//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.visual;

import java.util.Iterator;
import me.independed.inceptice.modules.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

public class Tracers extends Module {
      public Tracers() {
            super("Tracers", "draw a line to an entity", 20495 << 4 ^ 327920, Module.Category.RENDER);
      }

      public void onRenderWorldLast(float var1) {
            if (mc.player != null) {
                  GL11.glPushMatrix();
                  if ((((497508103 | 350886201) >> 2 ^ 117360548) >>> 3 ^ 3187213) == 0) {
                        ;
                  }

                  GL11.glEnable(619 & 297 & 39 & 24 ^ 2848);
                  GL11.glDisable(((2074 ^ 1108 | 699) ^ 389) << 2 & 12314 ^ 15225);
                  GL11.glDisable(2193 >> 1 << 3 ^ 12193);
                  GL11.glDisable((768 ^ 194) << 2 ^ 2240 ^ 3224);
                  GL11.glDepthMask((boolean)((415482063 >>> 4 | 9813195) ^ 27114703));
                  GL11.glBlendFunc(732 ^ 642 ^ 1 ^ 861, (285 >>> 3 | 20 | 38 | 13) ^ 828);
                  GL11.glEnable(829 >>> 1 >> 4 >> 1 ^ 3054);
                  GL11.glLineWidth(1.0F);
                  Minecraft var10000 = mc;
                  if ((1867702539 >> 3 >>> 1 ^ 116731408) == 0) {
                        ;
                  }

                  Iterator var2 = var10000.world.loadedEntityList.iterator();

                  while(var2.hasNext()) {
                        Entity var3 = (Entity)var2.next();
                        Minecraft var4 = mc;
                        if (!"please dont crack my plugin".equals("you're dogshit")) {
                              ;
                        }

                        if (var3 != mc.player && var3 != null && var3 instanceof EntityPlayer) {
                              float var5 = mc.getRenderViewEntity().getDistance(var3);
                              double var20 = var3.lastTickPosX;
                              double var10001 = var3.posX;
                              if (((1209159933 >> 3 << 3 | 816653632) ^ 203957737) != 0) {
                                    ;
                              }

                              double var6 = var20 + (var10001 - var3.lastTickPosX) - mc.getRenderManager().viewerPosX;
                              if (((1062509961 << 4 | 605469260) ^ 1677125803) != 0) {
                                    ;
                              }

                              if ((24589 << 3 ^ 196712) == 0) {
                                    ;
                              }

                              double var8 = var3.lastTickPosY + (var3.posY - var3.lastTickPosY) - mc.getRenderManager().viewerPosY;
                              double var10 = var3.lastTickPosZ + (var3.posZ - var3.lastTickPosZ) - mc.getRenderManager().viewerPosZ;
                              float var12 = (float)(System.currentTimeMillis() % (((22L << 10 | 7901L) ^ 17444L) >> 23 ^ 2000L)) / 1000.0F;
                              float var13 = 0.5F + 0.5F * MathHelper.sin(var12 * 3.1415927F);
                              float var10002 = (var12 + 1.3333334F) * 3.1415927F;
                              if (!"stop skidding".equals("yo mama name maurice")) {
                                    ;
                              }

                              float var22 = 0.5F * MathHelper.sin(var10002);
                              if ((((140549606 | 2822403) >> 1 ^ 25390215) << 2 ^ 383420880) == 0) {
                                    ;
                              }

                              float var14 = 0.5F + var22;
                              float var15 = 0.5F + 0.5F * MathHelper.sin((var12 + 2.6666667F) * 3.1415927F);
                              Minecraft var17 = mc;
                              float var16 = mc.player.getDistance(var3) / 20.0F;
                              if ((466905115 << 2 << 3 >> 4 ^ 1096433254) != 0) {
                                    ;
                              }

                              GL11.glColor4f(0.4F, 0.6F, 1.0F, 1.71F);
                              if ((((1938695122 | 1589056269) & 2012655654) >>> 1 >> 3 << 1 ^ 251053568) == 0) {
                                    ;
                              }

                              Vec3d var21 = new Vec3d;
                              if (((((1656121552 ^ 1534024905) & 178031503) >> 2 | 7586720) << 4 ^ 662452768) == 0) {
                                    ;
                              }

                              var21.<init>(0.0D, 0.0D, 1.0D);
                              Vec3d var18 = var21;
                              if ((((1057861946 | 794825972) ^ 907147722) >>> 4 ^ -575493025) != 0) {
                                    ;
                              }

                              var4 = mc;
                              var18 = var18.rotatePitch(-((float)Math.toRadians((double)mc.player.rotationPitch)));
                              var4 = mc;
                              Minecraft var23 = mc;
                              if (!"i hope you catch fire ngl".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                                    ;
                              }

                              var10001 = (double)var23.player.rotationYaw;
                              if (((115808165 ^ 23668858) >>> 3 & 6965454 ^ 558513717) != 0) {
                                    ;
                              }

                              Vec3d var19 = var18.rotateYaw(-((float)Math.toRadians(var10001)));
                              GL11.glBegin((1 << 4 >>> 4 ^ 0) >> 4 ^ 2);
                              var4 = mc;
                              GL11.glVertex3d(var19.x, (double)mc.player.getEyeHeight() + var19.y, var19.z);
                              if ((16487921 >>> 3 ^ 1616477 ^ -1878787026) != 0) {
                                    ;
                              }

                              GL11.glVertex3d(var6 - 0.15D, var8 + 1.15D, var10);
                              GL11.glEnd();
                        }

                        if ((1011371431 >> 2 >>> 2 & 6382932 ^ 1211599791) != 0) {
                              ;
                        }
                  }

                  if ((571289919 >>> 4 << 2 ^ 142822476) == 0) {
                        ;
                  }

                  GL11.glDisable(2348 >> 3 << 4 & 2965 ^ 444 ^ 2126);
                  GL11.glDepthMask((boolean)(((0 | 287358292) >>> 2 | 8184490) ^ 75296766));
                  GL11.glEnable(((664 | 639) ^ 715) >>> 1 ^ 3579);
                  GL11.glEnable((1929 << 2 ^ 5209 ^ 2064) >> 2 ^ 3050);
                  if (!"please get a girlfriend and stop cracking plugins".equals("you're dogshit")) {
                        ;
                  }

                  GL11.glDisable(1256 << 4 >>> 4 >> 2 >>> 2 >>> 2 ^ 2867);
                  GL11.glPopMatrix();
            }
      }
}
