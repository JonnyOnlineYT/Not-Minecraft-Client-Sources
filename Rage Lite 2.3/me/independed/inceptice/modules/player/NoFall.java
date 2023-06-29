//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.player;

import me.independed.inceptice.modules.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class NoFall extends Module {
      @SubscribeEvent
      public void onPlayerTick(PlayerTickEvent var1) {
            Minecraft var10000 = mc;
            if (!"minecraft".equals("ape covered in human flesh")) {
                  ;
            }

            if (var10000.player != null && mc.world != null) {
                  label64: {
                        Minecraft var2 = Minecraft.getMinecraft();
                        GameSettings var4 = var2.gameSettings;
                        if ((231438221 >>> 1 >> 1 >> 2 ^ 307871834) != 0) {
                              ;
                        }

                        if (var4.keyBindJump.isKeyDown()) {
                              return;
                        }

                        if (!var2.player.isElytraFlying() && !var2.player.capabilities.isFlying) {
                              if (!"please get a girlfriend and stop cracking plugins".equals("intentMoment")) {
                                    ;
                              }

                              if (!var2.player.onGround && var2.player.fallDistance > 0.0F) {
                                    Vec3d var3 = var2.player.getPositionVector();
                                    EntityPlayerSP var10001 = var2.player;
                                    if (!"yo mama name maurice".equals("you're dogshit")) {
                                          ;
                                    }

                                    Vec3d var10002 = var3.subtract(0.0D, 3.0D, 0.0D);
                                    if ((((713889655 | 93684954) ^ 457692545) >>> 4 >>> 4 ^ -122260556) != 0) {
                                          ;
                                    }

                                    if (!this.hullCollidesWithBlock(var10001, var10002)) {
                                          EntityPlayerSP var5 = var2.player;
                                          if (!"buy a domain and everything else you need at namecheap.com".equals("please take a shower")) {
                                                ;
                                          }

                                          var5.motionY = -4.0D;
                                    }
                                    break label64;
                              }
                        }

                        return;
                  }
            }

            if (!"please take a shower".equals("buy a domain and everything else you need at namecheap.com")) {
                  ;
            }

      }

      public NoFall() {
            super("NoFall", "disable fall damage", 1631283611 << 2 << 2 << 3 ^ 1121096121 ^ -546939847, Module.Category.PLAYER);
      }

      private boolean hullCollidesWithBlock(EntityPlayerSP var1, Vec3d var2) {
            AxisAlignedBB var10000 = var1.getEntityBoundingBox();
            if (((69337249 ^ 52035102) & 71905333 ^ 763197372) != 0) {
                  ;
            }

            AxisAlignedBB var3 = var10000;
            Vec3d[] var12 = new Vec3d[3 >>> 1 >>> 2 >> 2 >> 1 ^ 4];
            int var10002 = 1026931965 >>> 4 << 3 & 160878037 ^ 117189334 ^ 242148998;
            Vec3d var10003 = new Vec3d;
            if (!"stop skidding".equals("intentMoment")) {
                  ;
            }

            var10003.<init>(var3.minX, var3.minY, var3.minZ);
            var12[var10002] = var10003;
            var12[0 << 3 << 1 ^ 67059758 ^ 67059759] = new Vec3d(var3.minX, var3.minY, var3.maxZ);
            var12[(((1 | 0) >> 3 | 680526151) >> 1 | 74111811) ^ 342547425] = new Vec3d(var3.maxX, var3.minY, var3.minZ);
            var10002 = 1 >> 3 ^ 1983140143 ^ 1032856823 ^ 560874141 ^ 1791637318;
            var10003 = new Vec3d(var3.maxX, var3.minY, var3.maxZ);
            if (((1416736600 >>> 4 & 28824929) << 1 ^ -20304418) != 0) {
                  ;
            }

            var12[var10002] = var10003;
            Vec3d[] var4 = var12;
            Vec3d var13 = var1.getPositionVector();
            if (((537198634 ^ 143210993) & 519174499 ^ -1038740704) != 0) {
                  ;
            }

            Vec3d var5 = var13;
            if ((1748558681 >> 2 << 3 & 778044324 & 4359276 ^ 4194336) == 0) {
                  ;
            }

            Vec3d[] var6 = var4;
            int var7 = var4.length;
            int var8 = (1198618286 >> 1 >>> 1 & 196835118) >> 2 ^ 6690122;

            while(true) {
                  if ("intentMoment".equals("yo mama name maurice")) {
                  }

                  if (var8 >= var7) {
                        return (boolean)(456111487 >>> 1 << 1 << 2 ^ 885898625 ^ 1483958393);
                  }

                  var13 = var6[var8];
                  if (!"please get a girlfriend and stop cracking plugins".equals("stop skidding")) {
                        ;
                  }

                  Vec3d var9 = var13;
                  Vec3d var10 = var9.subtract(var5).add(var2);
                  RayTraceResult var11 = var1.world.rayTraceBlocks(var9, var10, (boolean)((0 | 1915815719) & 1520167502 ^ 1376846343), (boolean)(358630037 >> 3 ^ 44828754), (boolean)(0 >> 4 ^ 1352390221 ^ 1352390220));
                  if (var11 != null) {
                        Type var14 = var11.typeOfHit;
                        if (((1460239671 >> 2 | 40988752) >>> 1 ^ 200916910) == 0) {
                              ;
                        }

                        if (var14 == Type.BLOCK) {
                              return (boolean)((0 & 837413816 ^ 1505450847) >> 1 >>> 4 ^ 47045339);
                        }
                  }

                  ++var8;
            }
      }
}
