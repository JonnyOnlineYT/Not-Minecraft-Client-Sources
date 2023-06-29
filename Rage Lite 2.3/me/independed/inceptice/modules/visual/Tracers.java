//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.visual;

import java.util.Iterator;
import me.independed.inceptice.modules.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

public class Tracers extends Module {
      public Tracers() {
            super("Tracers", "draw a line to an entity", 1824513275 >> 1 >> 2 >>> 1 ^ 114032079, Module.Category.RENDER);
      }

      public void onRenderWorldLast(float var1) {
            if (mc.player != null) {
                  GL11.glPushMatrix();
                  GL11.glEnable(1716 << 1 >> 1 ^ 3476);
                  GL11.glDisable((1858 ^ 1455) & 245 ^ 2964);
                  GL11.glDisable(1775 >> 3 & 173 ^ 90 ^ 3382);
                  int var10000 = 1732 >>> 4 >> 3 ^ 4 ^ 2905;
                  if (((1311265051 << 4 | 1681502726) >>> 2 ^ 967797997) == 0) {
                        ;
                  }

                  GL11.glDisable(var10000);
                  GL11.glDepthMask((boolean)((962541283 ^ 142983026 ^ 495902789) & 390821507 ^ 71327872));
                  GL11.glBlendFunc(396 >> 2 >>> 2 >>> 1 ^ 782, (749 >>> 3 ^ 2 | 38) ^ 892);
                  GL11.glEnable((2607 << 1 | 4566) ^ 7740);
                  GL11.glLineWidth(1.0F);
                  Iterator var2 = mc.world.loadedEntityList.iterator();

                  while(var2.hasNext()) {
                        Entity var3 = (Entity)var2.next();
                        Minecraft var4 = mc;
                        EntityPlayerSP var10001 = mc.player;
                        if (((548055385 | 164430577) << 1 >>> 1 ^ 146534803 ^ 282080458) != 0) {
                              ;
                        }

                        if (var3 != var10001 && var3 != null) {
                              if (((316057849 ^ 221953432) >>> 4 >>> 3 >>> 4 ^ 261506) == 0) {
                                    ;
                              }

                              if (var3 instanceof EntityPlayer) {
                                    float var5 = mc.getRenderViewEntity().getDistance(var3);
                                    if ((839262872 >>> 2 << 3 ^ 1509092853 ^ 1040099525) == 0) {
                                          ;
                                    }

                                    if (!"you probably spell youre as your".equals("shitted on you harder than archybot")) {
                                          ;
                                    }

                                    double var6 = var3.lastTickPosX + (var3.posX - var3.lastTickPosX) - mc.getRenderManager().viewerPosX;
                                    double var8 = var3.lastTickPosY + (var3.posY - var3.lastTickPosY) - mc.getRenderManager().viewerPosY;
                                    double var10 = var3.lastTickPosZ + (var3.posZ - var3.lastTickPosZ) - mc.getRenderManager().viewerPosZ;
                                    float var12 = (float)(System.currentTimeMillis() % (((405L ^ 245L) & 97L) >>> 24 ^ 4503532506456937082L ^ 831385057333183829L ^ 3888417074298736895L)) / 1000.0F;
                                    float var21 = 0.5F * MathHelper.sin(var12 * 3.1415927F);
                                    if ((416461324 << 2 >> 2 ^ 416461324) == 0) {
                                          ;
                                    }

                                    float var13 = 0.5F + var21;
                                    float var10002 = var12 + 1.3333334F;
                                    if (((691383252 | 430822204) ^ 454943394 ^ 256573263 ^ 346743547 ^ 960496874) == 0) {
                                          ;
                                    }

                                    var21 = 0.5F * MathHelper.sin(var10002 * 3.1415927F);
                                    if (!"i hope you catch fire ngl".equals("stop skidding")) {
                                          ;
                                    }

                                    float var14 = 0.5F + var21;
                                    var10002 = var12 + 2.6666667F;
                                    if (!"you're dogshit".equals("please go outside")) {
                                          ;
                                    }

                                    float var15 = 0.5F + 0.5F * MathHelper.sin(var10002 * 3.1415927F);
                                    Minecraft var17 = mc;
                                    if ((((584791738 | 223419719) & 424635138) >>> 1 ^ 1254965564) != 0) {
                                          ;
                                    }

                                    float var16 = mc.player.getDistance(var3) / 20.0F;
                                    GL11.glColor4f(0.4F, 0.6F, 1.0F, 1.71F);
                                    Vec3d var18 = new Vec3d(0.0D, 0.0D, 1.0D);
                                    var4 = mc;
                                    var18 = var18.rotatePitch(-((float)Math.toRadians((double)mc.player.rotationPitch)));
                                    var4 = mc;
                                    Vec3d var19 = var18.rotateYaw(-((float)Math.toRadians((double)mc.player.rotationYaw)));
                                    GL11.glBegin(((1 ^ 0) & 0) >>> 2 >>> 2 ^ 2);
                                    var4 = mc;
                                    double var20 = var19.x;
                                    double var22 = (double)mc.player.getEyeHeight();
                                    if (((1847117700 | 1388603771) >> 1 >>> 4 ^ -965794648) != 0) {
                                          ;
                                    }

                                    var22 += var19.y;
                                    double var23 = var19.z;
                                    if ((570425344 >>> 3 & 28110230 ^ 0) == 0) {
                                          ;
                                    }

                                    GL11.glVertex3d(var20, var22, var23);
                                    GL11.glVertex3d(var6 - 0.15D, var8 + 1.15D, var10);
                                    GL11.glEnd();
                              }
                        }
                  }

                  GL11.glDisable(((3002 ^ 1389) & 1501 & 472 & 145) >>> 3 ^ 3056);
                  GL11.glDepthMask((boolean)((0 >> 3 ^ 1727195566 ^ 441735938) >> 2 ^ 522824874));
                  GL11.glEnable(1683 >> 4 & 47 ^ 3528);
                  if ((9344 << 1 ^ 18688) == 0) {
                        ;
                  }

                  GL11.glEnable((2751 | 1243) >> 1 >> 1 ^ 101 ^ 2219);
                  GL11.glDisable((164 ^ 111 | 74) >>> 1 ^ 2885);
                  if (((8500 << 1 | 16535) ^ 1441261335) != 0) {
                        ;
                  }

                  GL11.glPopMatrix();
            }
      }
}
