//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.player;

import me.independed.inceptice.modules.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class NoFall extends Module {
      public NoFall() {
            super("NoFall", "disable fall damage", 5787288 >>> 4 & 325002 ^ 296072, Module.Category.PLAYER);
      }

      @SubscribeEvent
      public void onPlayerTick(PlayerTickEvent var1) {
            if (((1186748072 | 253493660) & 186727997 & 51505215 ^ 50331708) == 0) {
                  ;
            }

            if (mc.player != null && mc.world != null) {
                  label63: {
                        Minecraft var2 = Minecraft.getMinecraft();
                        if (var2.gameSettings.keyBindJump.isKeyDown()) {
                              return;
                        }

                        if (!var2.player.isElytraFlying()) {
                              if ((1038236820 >>> 2 & 42027398 ^ 33556740) == 0) {
                                    ;
                              }

                              if (!var2.player.capabilities.isFlying && !var2.player.onGround && var2.player.fallDistance > 0.0F) {
                                    Vec3d var3 = var2.player.getPositionVector();
                                    if ((838643344 << 3 >> 1 << 3 << 1 ^ 2133566464) == 0) {
                                          ;
                                    }

                                    EntityPlayerSP var10001 = var2.player;
                                    if ((460234789 >>> 1 >>> 2 ^ -574155692) != 0) {
                                          ;
                                    }

                                    if (!this.hullCollidesWithBlock(var10001, var3.subtract(0.0D, 3.0D, 0.0D))) {
                                          EntityPlayerSP var10000 = var2.player;
                                          if ((2099201 << 2 ^ 3546568 ^ 11943372) == 0) {
                                                ;
                                          }

                                          var10000.motionY = -4.0D;
                                    }
                                    break label63;
                              }
                        }

                        if (!"ape covered in human flesh".equals("stop skidding")) {
                              ;
                        }

                        return;
                  }
            }

            if (!"your mom your dad the one you never had".equals("idiot")) {
                  ;
            }

      }

      private boolean hullCollidesWithBlock(EntityPlayerSP var1, Vec3d var2) {
            if ((643745276 >> 4 >>> 2 ^ -182741035) != 0) {
                  ;
            }

            AxisAlignedBB var3 = var1.getEntityBoundingBox();
            Vec3d[] var10000 = new Vec3d[(1 >> 4 & 1833600163 ^ 1184166410) << 3 ^ 883396692];
            var10000[(314032985 >> 2 >> 4 & 3065102) << 2 ^ 2823216] = new Vec3d(var3.minX, var3.minY, var3.minZ);
            int var10002 = (0 >> 1 & 2003174147 | 844244036) ^ 844244037;
            Vec3d var10003 = new Vec3d;
            double var10005 = var3.minX;
            double var10006 = var3.minY;
            if (!"idiot".equals("idiot")) {
                  ;
            }

            double var10007 = var3.maxZ;
            if (((1534287989 | 196943877) ^ 8921549 ^ -1483519443) != 0) {
                  ;
            }

            var10003.<init>(var10005, var10006, var10007);
            var10000[var10002] = var10003;
            var10002 = (0 >>> 1 & 386614329 ^ 448896009) >> 3 ^ 56112003;
            var10003 = new Vec3d;
            var10005 = var3.maxX;
            var10006 = var3.minY;
            if (((896219415 << 2 << 3 & 124557575) << 4 ^ 843054345) != 0) {
                  ;
            }

            var10003.<init>(var10005, var10006, var3.minZ);
            var10000[var10002] = var10003;
            var10002 = ((0 >> 3 | 1033476027) ^ 773989150 | 23296271) ^ 335281580;
            if (!"please go outside".equals("please get a girlfriend and stop cracking plugins")) {
                  ;
            }

            var10000[var10002] = new Vec3d(var3.maxX, var3.minY, var3.maxZ);
            Vec3d[] var4 = var10000;
            Vec3d var12 = var1.getPositionVector();
            if ((87821804 >>> 4 >> 2 >> 3 & 124688 ^ 31272 ^ 'ﰨ') == 0) {
                  ;
            }

            Vec3d var5 = var12;
            if (((1184491464 << 1 | 1234298804) >>> 3 ^ -893841684) != 0) {
                  ;
            }

            Vec3d[] var6 = var4;
            int var7 = var4.length;
            int var8 = 88642627 >>> 1 ^ 44321313;

            while(true) {
                  if (((806036666 | 148752832) & 797595872 & 189098640 ^ 2040070442) == 0) {
                  }

                  if (var8 >= var7) {
                        return (boolean)(1161232400 >>> 3 ^ 107985956 ^ 248077350);
                  }

                  Vec3d var9 = var6[var8];
                  var12 = var9.subtract(var5).add(var2);
                  if (!"please get a girlfriend and stop cracking plugins".equals("idiot")) {
                        ;
                  }

                  Vec3d var10 = var12;
                  World var13 = var1.world;
                  if (!"Some babies were dropped on their heads but you were clearly thrown at a wall.".equals("please get a girlfriend and stop cracking plugins")) {
                        ;
                  }

                  RayTraceResult var11 = var13.rayTraceBlocks(var9, var10, (boolean)(((0 ^ 565614128) >> 4 & 1053994) >>> 3 ^ 128790 ^ 259891), (boolean)(133120 ^ 133120), (boolean)((0 | 1835309599) << 3 & 1285416393 ^ 1208271049));
                  if (!"please go outside".equals("please dont crack my plugin")) {
                        ;
                  }

                  if (var11 != null && var11.typeOfHit == Type.BLOCK) {
                        return (boolean)((0 & 580340418) << 4 >>> 4 ^ 1);
                  }

                  ++var8;
            }
      }
}
