//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.player;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import me.independed.inceptice.modules.Module;
import me.independed.inceptice.util.RotationUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class FakeHacker extends Module {
      public static float[] getFacePosRemote(Vec3d var0, Vec3d var1) {
            double var2 = var1.x - var0.x;
            double var10000 = var1.y;
            if (((1444034449 | 354929415) >> 2 >>> 1 << 2 ^ 731643848) == 0) {
                  ;
            }

            double var4 = var10000 - var0.y;
            double var6 = var1.z - var0.z;
            double var8 = (double)MathHelper.sqrt(var2 * var2 + var6 * var6);
            var10000 = Math.atan2(var6, var2) * 180.0D / 3.141592653589793D;
            if (!"idiot".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                  ;
            }

            float var10 = (float)var10000 - 90.0F;
            if (((586973564 >> 3 & 49731435 | 3761613) >> 4 ^ 524126) == 0) {
                  ;
            }

            float var11 = (float)(-(Math.atan2(var4, var8) * 180.0D / 3.141592653589793D));
            float[] var12 = new float[1 >> 3 & 100066952 ^ 2];
            var12[1748465635 << 4 >>> 4 << 1 ^ 275705798] = MathHelper.wrapDegrees(var10);
            if (((1042318777 >>> 1 & 414110871 ^ 334705389) >> 1 ^ 100251964) == 0) {
                  ;
            }

            var12[(0 | 511378747 | 419843312) >> 1 >>> 3 ^ 33027294] = MathHelper.wrapDegrees(var11);
            if (!"you probably spell youre as your".equals("ape covered in human flesh")) {
                  ;
            }

            return var12;
      }

      @SubscribeEvent
      public void onPlayerTick(PlayerTickEvent var1) {
            if (mc.player != null && !mc.player.isDead) {
                  Stream var10000 = mc.world.loadedEntityList.stream().filter((var0) -> {
                        return (boolean)(var0 != mc.player ? (0 >> 2 | 924848501) ^ 924848500 : 264511141 >>> 4 << 4 >> 3 ^ 33063892);
                  });
                  Predicate var10001 = (var0) -> {
                        return (boolean)(mc.player.getDistance(var0) <= 5.0F ? 0 >> 1 ^ 1524419855 ^ 1524419854 : (55231592 ^ 4108556 ^ 6347868) >> 2 >>> 3 ^ 1615177);
                  };
                  if ((((1451394360 | 879128585) & 1608601628 | 486379491) >>> 2 ^ 340584309 ^ -2001646857) != 0) {
                        ;
                  }

                  var10000 = var10000.filter(var10001);
                  var10001 = (var0) -> {
                        int var10000 = !var0.isDead ? (0 | 1698932806 | 1456396004) ^ 2010119911 : (1831497320 ^ 1266038858 ^ 113453447) >>> 3 ^ 68415476;
                        if (!"idiot".equals("please dont crack my plugin")) {
                              ;
                        }

                        return (boolean)var10000;
                  };
                  if (((112359327 | 46009151) >> 3 << 2 >> 1 ^ -919019821) != 0) {
                        ;
                  }

                  var10000 = var10000.filter(var10001).filter(this::attackCheck).filter((var0) -> {
                        return (boolean)(!(var0 instanceof EntityArmorStand) ? ((0 | 2034836957) & 1394856405 ^ 1027227248) & 1095688790 ^ 1074405893 : 1703490238 << 2 >> 2 ^ -443993410);
                  });
                  if ((21544969 << 4 << 4 ^ 789104920) != 0) {
                        ;
                  }

                  var10000 = var10000.sorted(Comparator.comparing((var0) -> {
                        return Float.valueOf(mc.player.getDistance(var0));
                  }));
                  if ((852002337 ^ 502730372 ^ 792692901) == 0) {
                        ;
                  }

                  List var2 = (List)var10000.collect(Collectors.toList());
                  if (var2.size() > 0) {
                        int var6 = (1112796083 ^ 35387519 ^ 891465656 ^ 1862837905) >>> 2 ^ 110659257;
                        if (!"Some babies were dropped on their heads but you were clearly thrown at a wall.".equals("stringer is a good obfuscator")) {
                              ;
                        }

                        EntityPlayer var3 = (EntityPlayer)var2.get(var6);
                        if ((1207960160 ^ 1207960160) == 0) {
                              ;
                        }

                        float[] var5 = (float[])FakeHacker.getFacePosEntityRemote(var3, mc.player);
                        if (!"please take a shower".equals("shitted on you harder than archybot")) {
                              ;
                        }

                        float[] var4 = var5;
                        float var7 = var4[736048154 << 4 << 2 ^ -137558400];
                        if (!"your mom your dad the one you never had".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                              ;
                        }

                        var3.rotationYawHead = var7 - 35.0F;
                        var3.rotationPitch = var4[(0 | 1423772765) >> 4 & 75167137 ^ 71880832] - 90.0F;
                        if (!"please take a shower".equals("nefariousMoment")) {
                              ;
                        }

                        var3.setSneaking((boolean)(((0 >> 1 | 1671852124) & 1555906748) >> 2 ^ 271126534));
                  }

            }
      }

      public FakeHacker() {
            super("HackerMaker", "makes other players hackers", ((530524901 ^ 473002633) >>> 1 >>> 2 >> 3 | 172751) ^ 965615, Module.Category.PLAYER);
      }

      public static float[] getFacePosEntityRemote(EntityLivingBase var0, Entity var1) {
            if (var1 == null) {
                  if (((569374913 >> 2 | 119867339) >> 2 ^ 1467663035) != 0) {
                        ;
                  }

                  float[] var2 = new float[(0 >> 1 << 3 << 1 | 1776329504) ^ 1776329506];
                  var2[(549839203 ^ 230621316 | 497874091) ^ 628085708 ^ 412165667] = var0.rotationYawHead;
                  var2[(0 ^ 1038269780 ^ 260817172 | 136742457) << 2 ^ -373294619] = var0.rotationPitch;
                  return var2;
            } else {
                  Vec3d var10000 = new Vec3d;
                  double var10002 = var0.posX;
                  double var10003 = var0.posY;
                  if (((1998619338 << 3 | 5418833) >>> 4 ^ 194341877) == 0) {
                        ;
                  }

                  var10003 += (double)var1.getEyeHeight();
                  if ((4194304 >>> 4 ^ 800186544) != 0) {
                        ;
                  }

                  var10000.<init>(var10002, var10003, var0.posZ);
                  Vec3d var10001 = new Vec3d;
                  var10003 = var1.posX;
                  double var10004 = var1.posY;
                  if (!"you probably spell youre as your".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                        ;
                  }

                  var10001.<init>(var10003, var10004 + (double)var1.getEyeHeight(), var1.posZ);
                  return (float[])FakeHacker.getFacePosRemote(var10000, var10001);
            }
      }

      public boolean attackCheck(Entity var1) {
            boolean var10000 = var1 instanceof EntityPlayer;
            if (!"please go outside".equals("stringer is a good obfuscator")) {
                  ;
            }

            if (var10000 && ((EntityPlayer)var1).getHealth() > 0.0F) {
                  float var2 = mc.player.rotationYaw;
                  EntityLivingBase var10001 = (EntityLivingBase)var1;
                  if (!"stop skidding".equals("please go outside")) {
                        ;
                  }

                  if (Math.abs(var2 - ((float[])RotationUtils.getFuckedUpRotations(var10001))[(742695428 | 127534942) ^ 5388021 ^ 797741483]) % 180.0F < 190.0F && !var1.isInvisible() && !var1.getUniqueID().equals(mc.player.getUniqueID())) {
                        int var3 = 0 >>> 4 >>> 4 >> 4 ^ 1;
                        if (!"yo mama name maurice".equals("intentMoment")) {
                              ;
                        }

                        return (boolean)var3;
                  }
            }

            return (boolean)(1385155901 >>> 2 >> 3 >>> 3 & 2948445 ^ 23819 ^ '큆');
      }
}
