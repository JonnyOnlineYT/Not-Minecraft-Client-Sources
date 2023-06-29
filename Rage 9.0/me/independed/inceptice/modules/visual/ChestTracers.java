//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.visual;

import java.util.Iterator;
import me.independed.inceptice.modules.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

public class ChestTracers extends Module {
      public void onRenderWorldLast(float var1) {
            if (((674805253 >>> 1 ^ 140518112) >>> 1 ^ 190396431 ^ 90637566) == 0) {
                  ;
            }

            if (mc.player != null) {
                  GL11.glPushMatrix();
                  int var10000 = ((55 ^ 12) << 2 | 68) & 124 & 74 ^ 2920;
                  if ((1802729445 >> 4 << 2 >> 4 ^ 28167647) == 0) {
                        ;
                  }

                  GL11.glEnable(var10000);
                  GL11.glDisable((768 & 560 ^ 431 ^ 83) << 2 ^ 1153);
                  GL11.glDisable((2618 << 3 ^ 18078) << 3 ^ 10519 ^ '麆');
                  GL11.glDisable(((1064 | 798) ^ 1124 | 274) ^ 2058);
                  if (!"ape covered in human flesh".equals("your mom your dad the one you never had")) {
                        ;
                  }

                  GL11.glDepthMask((boolean)((908152714 ^ 894472351) >> 1 >>> 3 >>> 2 ^ 903036));
                  if (((1044293490 ^ 526624961) >>> 1 ^ -1676245046) != 0) {
                        ;
                  }

                  GL11.glBlendFunc(((253 & 230 | 156) >>> 1 | 84) ^ 892, (555 >>> 4 | 22) ^ 821);
                  if ((((1804089510 | 565549760) >>> 3 ^ 213548541) >> 3 ^ -1939321017) != 0) {
                        ;
                  }

                  GL11.glEnable(1653 << 2 << 4 >> 1 >> 2 ^ 14410);
                  if (('ꈀ' ^ 22136 ^ '\uf478') == 0) {
                        ;
                  }

                  GL11.glLineWidth(1.0F);
                  if (!"Some babies were dropped on their heads but you were clearly thrown at a wall.".equals("please dont crack my plugin")) {
                        ;
                  }

                  Iterator var2 = Minecraft.getMinecraft().world.loadedTileEntityList.iterator();

                  while(var2.hasNext()) {
                        TileEntity var3 = (TileEntity)var2.next();
                        TileEntityChest var5 = null;
                        if (((1737980971 ^ 916119146 | 432069346) >>> 1 ^ -729528701) != 0) {
                              ;
                        }

                        if (var3 instanceof TileEntityChest) {
                              var5 = (TileEntityChest)var3;
                        }

                        if (var5 != null) {
                              BlockPos var20 = var5.getPos();
                              if ((((1439683420 | 102408566) & 1072600445 ^ 322700809 ^ 49818417) << 3 ^ 810934816) == 0) {
                                    ;
                              }

                              double var21 = (double)(var20.getX() + (var5.getPos().getX() - var5.getPos().getX()));
                              RenderManager var10001 = mc.getRenderManager();
                              if ((((1350370971 | 654988093) >>> 3 | 9988740) << 2 ^ 1006611420) == 0) {
                                    ;
                              }

                              double var6 = var21 - var10001.viewerPosX;
                              var21 = (double)(var5.getPos().getY() + (var5.getPos().getY() - var5.getPos().getY()));
                              var10001 = mc.getRenderManager();
                              if (((674592777 << 2 >>> 2 & 180419177) >> 3 ^ -895718853) != 0) {
                                    ;
                              }

                              double var8 = var21 - var10001.viewerPosY;
                              double var10 = (double)(var5.getPos().getZ() + (var5.getPos().getZ() - var5.getPos().getZ())) - mc.getRenderManager().viewerPosZ;
                              if ((8714 & 7819 ^ 645724535) != 0) {
                                    ;
                              }

                              if (!"nefariousMoment".equals("your mom your dad the one you never had")) {
                                    ;
                              }

                              float var12 = (float)(System.currentTimeMillis() % ((1783L >>> 2 ^ 84L) >>> 11 ^ 2000L)) / 1000.0F;
                              float var22 = 0.5F + 0.5F * MathHelper.sin(var12 * 3.1415927F);
                              if (!"you probably spell youre as your".equals("your mom your dad the one you never had")) {
                                    ;
                              }

                              float var23 = 0.5F * MathHelper.sin((var12 + 1.3333334F) * 3.1415927F);
                              if (!"you're dogshit".equals("please take a shower")) {
                                    ;
                              }

                              float var14 = 0.5F + var23;
                              float var15 = 0.5F + 0.5F * MathHelper.sin((var12 + 2.6666667F) * 3.1415927F);
                              if (!"please take a shower".equals("i hope you catch fire ngl")) {
                                    ;
                              }

                              Minecraft var17 = mc;
                              if (!"please take a shower".equals("please dont crack my plugin")) {
                                    ;
                              }

                              GL11.glColor4f(0.4F, 0.6F, 1.0F, 1.71F);
                              Vec3d var24 = new Vec3d;
                              if (((1158884289 ^ 965851704) >> 2 & 52518845 ^ 52429884) == 0) {
                                    ;
                              }

                              var24.<init>(0.0D, 0.0D, 1.0D);
                              Vec3d var18 = var24;
                              var18 = var18.rotatePitch(-((float)Math.toRadians((double)mc.player.rotationPitch)));
                              double var25 = Math.toRadians((double)mc.player.rotationYaw);
                              if (((158839233 ^ 96981698) >> 4 & 948236 ^ 655360) == 0) {
                                    ;
                              }

                              Vec3d var19 = var18.rotateYaw(-((float)var25));
                              var10000 = (1 >> 3 | 1673261635) ^ 980928545 ^ 1506556512;
                              if (((979249422 ^ 79542510) >>> 2 << 1 & 285088254 ^ 275777776) == 0) {
                                    ;
                              }

                              GL11.glBegin(var10000);
                              if (((1085971250 >>> 2 << 2 | 350466109) ^ 1425783613) == 0) {
                                    ;
                              }

                              GL11.glVertex3d(var19.x, (double)mc.player.getEyeHeight() + var19.y, var19.z);
                              GL11.glVertex3d(var6, var8 + 0.7D, var10);
                              GL11.glEnd();
                        }
                  }

                  var10000 = (826 >> 2 >>> 2 | 46) ^ 43 ^ 3062;
                  if ((525042808 >> 2 & 14131409 & 2173035 & 6826 ^ 0) == 0) {
                        ;
                  }

                  GL11.glDisable(var10000);
                  if (((1048473132 | 409232416 | 578110122) & 514621737 ^ 1962832688) != 0) {
                        ;
                  }

                  GL11.glDepthMask((boolean)((0 >> 2 & 2121332309 | 1363547501) >>> 4 ^ 85221719));
                  GL11.glEnable((1470 << 4 >> 1 & 6083) >> 3 ^ 3417);
                  GL11.glEnable(118 << 1 << 3 ^ 3089);
                  GL11.glDisable(((951 ^ 930) >> 4 ^ 0 | 0) ^ 2849);
                  GL11.glPopMatrix();
            }
      }

      public ChestTracers() {
            if (!"nefariousMoment".equals("yo mama name maurice")) {
                  ;
            }

            if ((((415685358 | 91147683) >> 4 | 21220883) ^ 31453183) == 0) {
                  ;
            }

            int var10003 = (1362150986 >>> 2 ^ 228078809 | 319112817) ^ 466962299;
            if (((496124626 ^ 222533764) >>> 4 << 1 & 3653804 ^ -285895460) != 0) {
                  ;
            }

            super("ChestTracers", "draws lines to chests", var10003, Module.Category.RENDER);
      }
}
