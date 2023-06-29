//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.combat;

import me.independed.inceptice.modules.Module;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class Fraerok4 extends Module {
      public Fraerok4() {
            if (!"please go outside".equals("yo mama name maurice")) {
                  ;
            }

            super("TriggerBot", "attack when your mouse on entity", ((738190074 | 328524829) >>> 2 >>> 4 | 7095471) ^ 11690585 ^ 6135270, Module.Category.COMBAT);
            if ((((113873046 | 83536895) ^ 47191521 | 67678965) ^ 13823947 ^ 83448116) == 0) {
                  ;
            }

      }

      private void setRotation(float var1, float var2) {
            EntityPlayerSP var10000 = mc.player;
            if ((1967662159 ^ 1274651002 ^ 406554594 ^ 646578903) == 0) {
                  ;
            }

            var10000.rotationYaw = var1 % 360.0F;
            var10000 = mc.player;
            float var10001 = var2 % 360.0F;
            if (!"your mom your dad the one you never had".equals("please get a girlfriend and stop cracking plugins")) {
                  ;
            }

            var10000.rotationPitch = var10001;
      }

      public float[] getRotations(Entity var1) {
            double var2 = var1.posX + (var1.posX - var1.lastTickPosX) - mc.player.posX;
            double var10000 = var1.posY + (double)var1.getEyeHeight() - mc.player.posY;
            EntityPlayerSP var10001 = mc.player;
            if ((1701730736 >> 4 >> 1 ^ 34312385 ^ 18932236) == 0) {
                  ;
            }

            var10000 += (double)var10001.getEyeHeight();
            if (!"Some babies were dropped on their heads but you were clearly thrown at a wall.".equals("shitted on you harder than archybot")) {
                  ;
            }

            double var4 = var10000 - 3.5D;
            var10000 = var1.posZ;
            double var12 = var1.posZ - var1.lastTickPosZ;
            if ((5133190 >>> 2 & 67531 & '\ue76a' ^ 536735964) != 0) {
                  ;
            }

            var10000 = var10000 + var12 - mc.player.posZ;
            if (((2098037422 << 2 & 697139309) >> 4 & 12634222 ^ 2141216800) != 0) {
                  ;
            }

            double var6 = var10000;
            double var8 = Math.sqrt(Math.pow(var2, 2.0D) + Math.pow(var6, 2.0D));
            float var10 = (float)Math.toDegrees(-Math.atan(var2 / var6));
            float var11 = (float)(-Math.toDegrees(Math.atan(var4 / var8)));
            if (var2 < 0.0D && var6 < 0.0D) {
                  float var13 = (float)(90.0D + Math.toDegrees(Math.atan(var6 / var2)));
                  if (((1212209238 >>> 1 >>> 2 | 139793840 | 107541919) ^ 1552337752) != 0) {
                        ;
                  }

                  var10 = var13;
            } else if (var2 > 0.0D) {
                  if ((2040701128 >>> 1 >> 4 ^ -275903277) != 0) {
                        ;
                  }

                  if (var6 < 0.0D) {
                        if (((1923548870 ^ 850050850) >> 2 ^ 83926831 ^ 1762144188) != 0) {
                              ;
                        }

                        var12 = Math.atan(var6 / var2);
                        if (!"please dont crack my plugin".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                              ;
                        }

                        var10 = (float)(-90.0D + Math.toDegrees(var12));
                  }
            }

            float[] var14 = new float[(0 << 3 | 654832992) ^ 654832994];
            var14[1181195 >> 1 << 2 ^ 2362388] = var10;
            var14[(0 & 1819826923) << 3 ^ 1] = var11;
            if (((172695762 | 139851015) >>> 2 ^ 455841095) != 0) {
                  ;
            }

            return var14;
      }

      @SubscribeEvent
      public void onPlayerTick(PlayerTickEvent var1) {
            if (mc.player != null) {
                  Entity var2 = mc.objectMouseOver.entityHit;
                  if (var2 != null) {
                        float var10000 = mc.player.getDistance(var2);
                        if (!"Some babies were dropped on their heads but you were clearly thrown at a wall.".equals("shitted on you harder than archybot")) {
                              ;
                        }

                        double var10;
                        int var7 = (var10 = (double)var10000 - 3.5D) == 0.0D ? 0 : (var10 < 0.0D ? -1 : 1);
                        if (((237117570 | 185516556) >> 3 >> 3 ^ 3980170) == 0) {
                              ;
                        }

                        if (var7 <= 0 && !(var2 instanceof EntityEnderCrystal) && !var2.isDead && ((EntityLivingBase)var2).getHealth() > 0.0F) {
                              if ((((1251882765 | 486007242) >>> 3 ^ 158492752) & 28647792 ^ 1517653009) != 0) {
                                    ;
                              }

                              if (var2 instanceof EntityPlayer) {
                                    double var3 = (double)((var2.rotationYaw + 180.0F) % 360.0F);
                                    double var5 = (double)(mc.player.rotationYaw % 360.0F);
                                    if (var3 < 0.0D) {
                                          if ((1619429179 >> 1 << 1 ^ -2012600521) != 0) {
                                                ;
                                          }

                                          var3 += 360.0D;
                                    }

                                    if (((933713284 >>> 3 << 1 << 4 | 1429272859) ^ -1628288709) != 0) {
                                          ;
                                    }

                                    if (var5 < 0.0D) {
                                          var5 += 360.0D;
                                    }

                                    if (mc.player.getCooledAttackStrength(0.0F) >= 1.0F) {
                                          mc.player.rotationYaw = ((float[])this.getRotations(var2))[(579783914 ^ 13980680 ^ 407120960 | 910703790) ^ 845812471 ^ 204844633];
                                          EntityPlayerSP var8 = mc.player;
                                          if (!"please dont crack my plugin".equals("You're so fat whenever you go to the beach the tide comes in.")) {
                                                ;
                                          }

                                          float var10001 = ((float[])this.getRotations(var2))[0 << 3 >> 3 >>> 3 >>> 1 ^ 1];
                                          if (!"idiot".equals("nefariousMoment")) {
                                                ;
                                          }

                                          var8.rotationPitch = var10001;
                                          PlayerControllerMP var9 = mc.playerController;
                                          if (!"please get a girlfriend and stop cracking plugins".equals("please dont crack my plugin")) {
                                                ;
                                          }

                                          var9.attackEntity(mc.player, var2);
                                          mc.player.swingArm(EnumHand.MAIN_HAND);
                                    }

                                    return;
                              }
                        }
                  }

            }
      }
}
