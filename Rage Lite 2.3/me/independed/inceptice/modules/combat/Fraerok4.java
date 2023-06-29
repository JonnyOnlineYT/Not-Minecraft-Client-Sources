//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.combat;

import me.independed.inceptice.modules.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class Fraerok4 extends Module {
      public Fraerok4() {
            super("TriggerBot", "attack when your mouse on entity", 214329528 >>> 1 >>> 3 ^ 13395595, Module.Category.COMBAT);
      }

      @SubscribeEvent
      public void onPlayerTick(PlayerTickEvent var1) {
            if (mc.player == null) {
                  if (!"please take a shower".equals("intentMoment")) {
                        ;
                  }

            } else {
                  Entity var2 = mc.objectMouseOver.entityHit;
                  if (var2 != null) {
                        double var10000 = (double)mc.player.getDistance(var2);
                        if (((614275698 | 293724223 | 43262030 | 680468820) ^ 1067449215) == 0) {
                              ;
                        }

                        if (var10000 <= 3.5D && !(var2 instanceof EntityEnderCrystal) && !var2.isDead && ((EntityLivingBase)var2).getHealth() > 0.0F && var2 instanceof EntityPlayer) {
                              float var7 = var2.rotationYaw;
                              if (!"stringer is a good obfuscator".equals("you probably spell youre as your")) {
                                    ;
                              }

                              double var3 = (double)((var7 + 180.0F) % 360.0F);
                              var7 = mc.player.rotationYaw;
                              if (((557312 << 3 & '뫌') >>> 1 & 458 ^ 0) == 0) {
                                    ;
                              }

                              double var5 = (double)(var7 % 360.0F);
                              if (var3 < 0.0D) {
                                    var3 += 360.0D;
                              }

                              if (var5 < 0.0D) {
                                    var5 += 360.0D;
                              }

                              if (!"shitted on you harder than archybot".equals("nefariousMoment")) {
                                    ;
                              }

                              var7 = mc.player.getCooledAttackStrength(0.0F);
                              if (!"nefariousMoment".equals("i hope you catch fire ngl")) {
                                    ;
                              }

                              if (var7 >= 1.0F) {
                                    if (!"i hope you catch fire ngl".equals("buy a domain and everything else you need at namecheap.com")) {
                                          ;
                                    }

                                    mc.player.rotationYaw = ((float[])this.getRotations(var2))[(1217246911 ^ 183419743 | 754258986) ^ 1861711338];
                                    mc.player.rotationPitch = ((float[])this.getRotations(var2))[(0 >> 3 >>> 2 & 1217841762) >> 4 ^ 1];
                                    mc.playerController.attackEntity(mc.player, var2);
                                    Minecraft var8 = mc;
                                    if ((249949175 >>> 2 >>> 1 ^ 1913607408) != 0) {
                                          ;
                                    }

                                    var8.player.swingArm(EnumHand.MAIN_HAND);
                              }

                              return;
                        }
                  }

            }
      }

      private void setRotation(float var1, float var2) {
            mc.player.rotationYaw = var1 % 360.0F;
            mc.player.rotationPitch = var2 % 360.0F;
      }

      public float[] getRotations(Entity var1) {
            double var10000 = var1.posX + (var1.posX - var1.lastTickPosX);
            Minecraft var10001 = mc;
            if ((1888069627 >> 3 >> 3 ^ 29501087) == 0) {
                  ;
            }

            double var2 = var10000 - var10001.player.posX;
            if (((855904128 | 97177679) >> 4 >> 1 >>> 4 ^ 1623454120) != 0) {
                  ;
            }

            double var4 = var1.posY + (double)var1.getEyeHeight() - mc.player.posY + (double)mc.player.getEyeHeight() - 3.5D;
            double var6 = var1.posZ + (var1.posZ - var1.lastTickPosZ) - mc.player.posZ;
            if ((617469121 >> 4 >>> 4 ^ 1143579 ^ 747971954) != 0) {
                  ;
            }

            double var8 = Math.sqrt(Math.pow(var2, 2.0D) + Math.pow(var6, 2.0D));
            float var10 = (float)Math.toDegrees(-Math.atan(var2 / var6));
            if ((293441160 >>> 2 >>> 4 ^ 571363126) != 0) {
                  ;
            }

            float var12 = (float)(-Math.toDegrees(Math.atan(var4 / var8)));
            if (((1253215943 >>> 3 | 61366819) >> 2 ^ -574357448) != 0) {
                  ;
            }

            float var11 = var12;
            if (var2 < 0.0D && var6 < 0.0D) {
                  double var14 = Math.toDegrees(Math.atan(var6 / var2));
                  if ((123141588 >>> 3 ^ 2157727 ^ 568060912) != 0) {
                        ;
                  }

                  var10 = (float)(90.0D + var14);
            } else if (var2 > 0.0D && var6 < 0.0D) {
                  var10 = (float)(-90.0D + Math.toDegrees(Math.atan(var6 / var2)));
            }

            float[] var13 = new float[0 & 144145889 & 152141456 ^ 2];
            if (!"i hope you catch fire ngl".equals("shitted on you harder than archybot")) {
                  ;
            }

            var13[(1441856945 >> 1 | 686482585 | 588900508 | 7499591) & 496956558 ^ 161150094] = var10;
            var13[0 ^ 1153280466 ^ 319076689 ^ 1471746178] = var11;
            return var13;
      }
}
