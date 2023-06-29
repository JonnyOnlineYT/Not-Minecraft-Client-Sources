//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.player;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import me.independed.inceptice.modules.Module;
import me.independed.inceptice.util.RotationUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class FakeHacker extends Module {
      public FakeHacker() {
            if (((268452913 | 100515136) >>> 3 ^ -1882411936) != 0) {
                  ;
            }

            super("HackerMaker", "makes other players hackers", (419438592 >> 2 >>> 3 | 1456305) ^ 14563761, Module.Category.PLAYER);
      }

      public boolean attackCheck(Entity var1) {
            if ((((1026329570 | 468757342) << 4 & 1496274372) >>> 1 ^ 746916576) == 0) {
                  ;
            }

            if (var1 instanceof EntityPlayer && ((EntityPlayer)var1).getHealth() > 0.0F) {
                  if (!"please dont crack my plugin".equals("you probably spell youre as your")) {
                        ;
                  }

                  if (Math.abs(mc.player.rotationYaw - ((float[])RotationUtils.getFuckedUpRotations((EntityLivingBase)var1))[((1422525742 ^ 1328615932) & 225027080) >> 4 ^ 9867328]) % 180.0F < 190.0F && !var1.isInvisible()) {
                        boolean var10000 = var1.getUniqueID().equals(mc.player.getUniqueID());
                        if (!"nefariousMoment".equals("please get a girlfriend and stop cracking plugins")) {
                              ;
                        }

                        if (!var10000) {
                              return (boolean)((0 << 3 & 303336941) >> 2 ^ 1);
                        }
                  }
            }

            int var2 = (1311998863 | 416292126) >> 1 ^ 796512207;
            if (((149515 | 110056) >> 1 ^ '魧' ^ 93586) == 0) {
                  ;
            }

            return (boolean)var2;
      }

      public static float[] getFacePosEntityRemote(EntityLivingBase var0, Entity var1) {
            if (var1 == null) {
                  float[] var2 = new float[((0 >>> 4 ^ 935644870) & 33868648 | 12920274) << 1 ^ 93052838];
                  var2[(2002123216 >>> 3 << 3 >>> 4 << 2 | 317076158) ^ 536313598] = var0.rotationYawHead;
                  var2[((0 >> 4 | 1260162010) ^ 351976160) << 3 & 1166098967 ^ 1157693457] = var0.rotationPitch;
                  if (!"ape covered in human flesh".equals("yo mama name maurice")) {
                        ;
                  }

                  return var2;
            } else {
                  Vec3d var10000 = new Vec3d;
                  double var10002 = var0.posX;
                  double var10003 = var0.posY;
                  double var10004 = (double)var1.getEyeHeight();
                  if (((2638930 | 556503) >>> 1 & 701865 ^ 13481) == 0) {
                        ;
                  }

                  var10000.<init>(var10002, var10003 + var10004, var0.posZ);
                  Vec3d var10001 = new Vec3d;
                  var10003 = var1.posX;
                  var10004 = var1.posY;
                  if ((((363595945 << 2 | 478444608) >>> 3 | 186101385) ^ 777160510) != 0) {
                        ;
                  }

                  var10001.<init>(var10003, var10004 + (double)var1.getEyeHeight(), var1.posZ);
                  return (float[])FakeHacker.getFacePosRemote(var10000, var10001);
            }
      }

      public static float[] getFacePosRemote(Vec3d var0, Vec3d var1) {
            double var2 = var1.x - var0.x;
            double var4 = var1.y - var0.y;
            double var10000 = var1.z;
            if (!"stringer is a good obfuscator".equals("shitted on you harder than archybot")) {
                  ;
            }

            double var10001 = var0.z;
            if (!"stop skidding".equals("nefariousMoment")) {
                  ;
            }

            double var6 = var10000 - var10001;
            var10000 = (double)MathHelper.sqrt(var2 * var2 + var6 * var6);
            if (!"buy a domain and everything else you need at namecheap.com".equals("stringer is a good obfuscator")) {
                  ;
            }

            double var8 = var10000;
            float var12 = (float)(Math.atan2(var6, var2) * 180.0D / 3.141592653589793D);
            if (((1686113234 << 3 ^ 161974901) << 2 ^ 1796677163 ^ -578269761) == 0) {
                  ;
            }

            if ((1439034312 << 2 >>> 1 ^ 730584976) == 0) {
                  ;
            }

            float var10 = var12 - 90.0F;
            float var11 = (float)(-(Math.atan2(var4, var8) * 180.0D / 3.141592653589793D));
            float[] var13 = new float[(0 ^ 489669631) >>> 3 ^ 61208701];
            int var10002 = ((1243201165 >>> 3 | 45686704) << 4 | 1414082342 | 724495677) ^ -193;
            float var10003 = MathHelper.wrapDegrees(var10);
            if (!"You're so fat whenever you go to the beach the tide comes in.".equals("yo mama name maurice")) {
                  ;
            }

            var13[var10002] = var10003;
            var13[0 >> 1 << 3 >> 3 ^ 31711858 ^ 31711859] = MathHelper.wrapDegrees(var11);
            return var13;
      }

      @SubscribeEvent
      public void onPlayerTick(PlayerTickEvent var1) {
            if (mc.player != null && !mc.player.isDead) {
                  Stream var10000 = mc.world.loadedEntityList.stream().filter((var0) -> {
                        int var10000;
                        if (var0 != mc.player) {
                              var10000 = (0 & 1450330252 | 1361240175) ^ 1361240174;
                        } else {
                              if ((102409685 >> 3 ^ 3010009 ^ 15645027) == 0) {
                                    ;
                              }

                              if (((138412040 | 95412628) ^ 233824668) == 0) {
                                    ;
                              }

                              var10000 = 1470029357 >> 1 >> 3 ^ 91876834;
                        }

                        return (boolean)var10000;
                  });
                  if (((402919161 | 265183360 | 518556147) & 3572778 ^ 123542 ^ -1089519459) != 0) {
                        ;
                  }

                  var10000 = var10000.filter((var0) -> {
                        return (boolean)(mc.player.getDistance(var0) <= 5.0F ? (0 & 1543644909 | 758252897) & 134298259 ^ 65383317 ^ 199601045 : (1328970639 >> 3 & 142683337 | 5307867 | 72365605) ^ 215023615);
                  });
                  Predicate var10001 = (var0) -> {
                        boolean var10000 = var0.isDead;
                        if (!"your mom your dad the one you never had".equals("please take a shower")) {
                              ;
                        }

                        int var1;
                        if (!var10000) {
                              if (!"stop skidding".equals("you probably spell youre as your")) {
                                    ;
                              }

                              var1 = (0 >> 2 | 1820784012) << 2 ^ 202010854 ^ -1106139433;
                        } else {
                              var1 = (1381793277 ^ 1155378347) >>> 2 >>> 1 & 45984618 ^ 43002410;
                        }

                        return (boolean)var1;
                  };
                  if ((288771007 >> 1 ^ 57681361 ^ 135574627 ^ 67089517) == 0) {
                        ;
                  }

                  List var2 = (List)var10000.filter(var10001).filter(this::attackCheck).filter((var0) -> {
                        return (boolean)(!(var0 instanceof EntityArmorStand) ? (0 | 28054249) >>> 4 ^ 1753391 : (322933713 | 72670702) >> 2 >>> 1 ^ 49282047);
                  }).sorted(Comparator.comparing((var0) -> {
                        Minecraft var10000 = mc;
                        if ((137481120 >>> 3 >> 2 & 4086054 ^ 343409115) != 0) {
                              ;
                        }

                        float var1 = var10000.player.getDistance(var0);
                        if (((1414206997 >> 4 << 2 | 180502670) >> 2 ^ 133476707) == 0) {
                              ;
                        }

                        return Float.valueOf(var1);
                  })).collect(Collectors.toList());
                  if (var2.size() > 0) {
                        EntityPlayer var3 = (EntityPlayer)var2.get((131090 | 115772) ^ 246846);
                        float[] var4 = (float[])FakeHacker.getFacePosEntityRemote(var3, mc.player);
                        if (((272522135 >>> 3 ^ 1912743) >>> 1 ^ 17658474) == 0) {
                              ;
                        }

                        var3.rotationYawHead = var4[4195364 >>> 4 ^ 262210] - 35.0F;
                        if (!"please go outside".equals("yo mama name maurice")) {
                              ;
                        }

                        var3.rotationPitch = var4[(0 >>> 4 << 1 & 232383835 ^ 753795892) >> 4 ^ 47112242] - 90.0F;
                        var3.setSneaking((boolean)(0 >> 4 << 3 << 2 & 853808436 ^ 1));
                  }

            } else {
                  if ((30408968 >> 1 >>> 2 ^ -1571556502) != 0) {
                        ;
                  }

            }
      }
}
