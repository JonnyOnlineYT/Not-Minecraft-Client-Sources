//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.visual;

import java.util.Iterator;
import me.independed.inceptice.modules.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

public class ChestTracers extends Module {
      public ChestTracers() {
            super("ChestTracers", "draws lines to chests", (8224 ^ 5526 ^ 10789) >>> 4 ^ 505, Module.Category.RENDER);
      }

      public void onRenderWorldLast(float var1) {
            if (!"please dont crack my plugin".equals("please get a girlfriend and stop cracking plugins")) {
                  ;
            }

            EntityPlayerSP var10000 = mc.player;
            if (((380013480 ^ 21087498) >>> 4 >>> 1 >>> 1 ^ 818427287) != 0) {
                  ;
            }

            if (var10000 != null) {
                  GL11.glPushMatrix();
                  GL11.glEnable((1218 << 3 >> 4 | 94) ^ 2399);
                  GL11.glDisable(1666 << 1 << 4 ^ '\udb31');
                  GL11.glDisable(((419 & 91) >> 2 | 1144038792) ^ 1144039529);
                  GL11.glDisable(2579 << 2 >> 3 ^ 3673);
                  int var20 = (1687515903 | 199069016) ^ 372639523 ^ 2045507804;
                  if (!"please take a shower".equals("shitted on you harder than archybot")) {
                        ;
                  }

                  GL11.glDepthMask((boolean)var20);
                  GL11.glBlendFunc((195 & 14 | 1) >>> 4 & 1840219636 ^ 770, 516 >> 1 >> 1 ^ 77 ^ 975);
                  GL11.glEnable((2133 << 1 << 1 >>> 3 ^ 587 | 372) ^ 3223);
                  GL11.glLineWidth(1.0F);
                  Minecraft var21 = Minecraft.getMinecraft();
                  if (((202637314 | 160031921) ^ -1727277063) != 0) {
                        ;
                  }

                  Iterator var2 = var21.world.loadedTileEntityList.iterator();

                  while(var2.hasNext()) {
                        TileEntity var22 = (TileEntity)var2.next();
                        if (((72681572 | 377413) ^ 60324270 ^ 915724063) != 0) {
                              ;
                        }

                        TileEntity var3 = var22;
                        TileEntityChest var5 = null;
                        if (var3 instanceof TileEntityChest) {
                              var5 = (TileEntityChest)var3;
                        }

                        if (var5 != null) {
                              if (((1791602151 >> 2 | 233281564) & 109789696 ^ 109265408) == 0) {
                                    ;
                              }

                              double var23 = (double)(var5.getPos().getX() + (var5.getPos().getX() - var5.getPos().getX()));
                              if ((2080462887 << 4 >> 3 >> 4 ^ -8377596) == 0) {
                                    ;
                              }

                              var23 -= mc.getRenderManager().viewerPosX;
                              if (!"your mom your dad the one you never had".equals("shitted on you harder than archybot")) {
                                    ;
                              }

                              double var6 = var23;
                              var23 = (double)(var5.getPos().getY() + (var5.getPos().getY() - var5.getPos().getY()));
                              double var10001 = mc.getRenderManager().viewerPosY;
                              if ((776206588 << 1 ^ 1355801145 ^ 206050241) == 0) {
                                    ;
                              }

                              double var8 = var23 - var10001;
                              var20 = var5.getPos().getZ();
                              int var24 = var5.getPos().getZ();
                              int var10002 = var5.getPos().getZ();
                              if (!"idiot".equals("please take a shower")) {
                                    ;
                              }

                              var20 += var24 - var10002;
                              if ((1530921256 << 2 >>> 2 ^ 81607296 ^ 530398120) == 0) {
                                    ;
                              }

                              var23 = (double)var20;
                              Minecraft var25 = mc;
                              if (((180493793 | 58264989) << 1 << 3 ^ 297110670 ^ -1375448226) == 0) {
                                    ;
                              }

                              double var10 = var23 - var25.getRenderManager().viewerPosZ;
                              float var12 = (float)(System.currentTimeMillis() % ((1811L >> 5 & 34L) << 29 & 3881975285L ^ 2000L)) / 1000.0F;
                              float var13 = 0.5F + 0.5F * MathHelper.sin(var12 * 3.1415927F);
                              float var26 = MathHelper.sin((var12 + 1.3333334F) * 3.1415927F);
                              if (((1580512319 >>> 4 << 1 >>> 2 | 27393751) ^ -2748136) != 0) {
                                    ;
                              }

                              float var14 = 0.5F + 0.5F * var26;
                              float var28 = 0.5F + 0.5F * MathHelper.sin((var12 + 2.6666667F) * 3.1415927F);
                              if ((((1000488882 | 77837945) << 3 & 2140947805) << 3 ^ -387831104) == 0) {
                                    ;
                              }

                              Minecraft var17 = mc;
                              GL11.glColor4f(0.4F, 0.6F, 1.0F, 1.71F);
                              Vec3d var18 = new Vec3d(0.0D, 0.0D, 1.0D);
                              var25 = mc;
                              if ((((1628661308 | 1116850716) & 699119965 | 109247633) ^ 662961309) == 0) {
                                    ;
                              }

                              var18 = var18.rotatePitch(-((float)Math.toRadians((double)var25.player.rotationPitch)));
                              if (((197493481 ^ 50792127) & 21824420 ^ 4021506 ^ 8215814) == 0) {
                                    ;
                              }

                              float var27 = mc.player.rotationYaw;
                              if ((1634532477 << 3 & 28140419 ^ 19227520) == 0) {
                                    ;
                              }

                              Vec3d var19 = var18.rotateYaw(-((float)Math.toRadians((double)var27)));
                              GL11.glBegin((0 | 829305598) >> 2 << 2 & 534300703 ^ 289931294);
                              GL11.glVertex3d(var19.x, (double)mc.player.getEyeHeight() + var19.y, var19.z);
                              GL11.glVertex3d(var6, var8 + 0.5D, var10);
                              GL11.glEnd();
                        }
                  }

                  var20 = (2238 | 891) & 2135 ^ 949;
                  if (((1412092613 ^ 1207485923) >> 4 ^ 2119805 ^ 9226908 ^ -277210836) != 0) {
                        ;
                  }

                  GL11.glDisable(var20);
                  GL11.glDepthMask((boolean)(0 >>> 4 >>> 2 & 472963177 & 1276788805 ^ 1));
                  GL11.glEnable(((1690 ^ 1482) & 746) >> 1 ^ 3265);
                  if (((33554448 | 4028931) ^ 37583379) == 0) {
                        ;
                  }

                  GL11.glEnable(1058 << 3 & 8233 ^ 11121);
                  GL11.glDisable(((1332 | 882) >> 3 ^ 100) >> 4 ^ 2856);
                  GL11.glPopMatrix();
                  if ((507958418 << 1 >> 1 >>> 3 >> 2 & 15825125 ^ 15741092) == 0) {
                        ;
                  }

            }
      }
}
