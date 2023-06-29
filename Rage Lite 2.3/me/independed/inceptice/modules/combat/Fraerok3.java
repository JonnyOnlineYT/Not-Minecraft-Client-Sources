//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.combat;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import me.independed.inceptice.modules.Module;
import me.independed.inceptice.settings.NumberSetting;
import me.independed.inceptice.settings.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class Fraerok3 extends Module {
      public NumberSetting widthSet;
      public NumberSetting heightSet;

      public void setEntityBoundingBoxSize(EntityPlayer var1, float var2, float var3) {
            if (!"You're so fat whenever you go to the beach the tide comes in.".equals("please take a shower")) {
                  ;
            }

            EntitySize var4 = this.getEntitySize(var1);
            var1.width = var4.width;
            if (((338259970 | 223252875) << 2 >>> 4 >> 2 ^ 30867224) == 0) {
                  ;
            }

            var1.height = var4.height;
            if (((1507080634 ^ 1176311525) << 3 >>> 3 & 107535994 ^ -612591258) != 0) {
                  ;
            }

            double var5 = (double)var2 / 2.0D;
            AxisAlignedBB var10001 = new AxisAlignedBB;
            double var10003 = var1.posX - var5;
            if (!"Some babies were dropped on their heads but you were clearly thrown at a wall.".equals("you probably spell youre as your")) {
                  ;
            }

            double var10004 = var1.posY;
            double var10005 = var1.posZ - var5;
            double var10006 = var1.posX + var5;
            if ((553912064 >>> 3 >> 1 >>> 3 ^ 1007308997) != 0) {
                  ;
            }

            var10001.<init>(var10003, var10004, var10005, var10006, var1.posY + (double)var3, var1.posZ + var5);
            var1.setEntityBoundingBox(var10001);
      }

      @SubscribeEvent
      public void onClientTick(ClientTickEvent var1) {
            this.setHitBoxForEntities();
      }

      public boolean check(EntityLivingBase var1) {
            if (var1 instanceof EntityPlayerSP) {
                  return (boolean)(1499300553 >> 3 >> 1 >> 3 ^ 11713285);
            } else if (var1 == mc.player) {
                  return (boolean)((171061474 | 148309451) >> 2 ^ 45993338);
            } else if (var1.isDead) {
                  return (boolean)(2056204210 >>> 2 & 139458239 ^ 134464172);
            } else if (var1.getHealth() < 0.0F) {
                  return (boolean)((638389761 << 2 >>> 4 | 53523291) ^ 196342747);
            } else {
                  return (boolean)(!(var1 instanceof EntityPlayer) ? 1389469551 >>> 2 >>> 2 >> 3 ^ 10855230 : ((0 & 425209741 | 1188344465) & 1064023617 & 58260743) >> 2 ^ 9448449);
            }
      }

      public Fraerok3() {
            if (((1327742791 << 3 >>> 3 & 181868605) >>> 2 ^ 1298663774) != 0) {
                  ;
            }

            super("H1tBox", "bigger players range attack", (1422353289 | 1029897997) << 2 & 1423282104 ^ 1419087408, Module.Category.COMBAT);
            NumberSetting var10001 = new NumberSetting;
            if (((1627550491 | 1413196664) ^ 1141455373 ^ 242391623 ^ -278666630) != 0) {
                  ;
            }

            var10001.<init>("Width", this, 1.0D, 1.0D, 10.0D, 0.1D);
            this.widthSet = var10001;
            if (((8812425 ^ 6133472) << 2 ^ 57640356) == 0) {
                  ;
            }

            this.heightSet = new NumberSetting("Height", this, 2.0D, 1.0D, 10.0D, 0.1D);
            Setting[] var1 = new Setting[1 >> 3 >> 4 ^ 2];
            if (((1021366239 | 866389157 | 280129617) >> 3 & 110667389 ^ 1252736436) != 0) {
                  ;
            }

            int var10003 = ((1692251248 | 800154684) >> 4 | 52706576) ^ 134217559;
            NumberSetting var10004 = this.widthSet;
            if (((1209531045 << 4 >> 1 | 1044221766) >> 2 ^ 772155633 ^ 1299718071) != 0) {
                  ;
            }

            var1[var10003] = var10004;
            var1[(0 & 119890991 | 1221722021) ^ 385553339 ^ 1579871263] = this.heightSet;
            this.addSettings(var1);
      }

      public void setHitBoxForEntities() {
            if (mc.player != null && !mc.player.isDead && mc.world != null) {
                  Minecraft var10000 = mc;
                  if (!"please take a shower".equals("ape covered in human flesh")) {
                        ;
                  }

                  Stream var6 = var10000.world.loadedEntityList.stream().filter((var0) -> {
                        if (((2122267205 | 362124875) >> 3 ^ -575351315) != 0) {
                              ;
                        }

                        int var10000;
                        if (var0 != mc.player) {
                              var10000 = 0 >>> 2 >> 1 >>> 2 ^ 1;
                        } else {
                              if (((1278739552 ^ 406321723 | 970589473 | 1931345527) ^ -2109983216) != 0) {
                                    ;
                              }

                              var10000 = 1072823864 << 1 << 4 >>> 2 & 663756955 ^ 662708352;
                        }

                        return (boolean)var10000;
                  }).filter((var0) -> {
                        return (boolean)(mc.player.getDistance(var0) <= 200.0F ? (0 << 1 >> 1 | 1655015090) ^ 1655015091 : (192010466 ^ 181794622 | 18063108) << 4 << 3 << 4 ^ -1107369984);
                  }).filter((var0) -> {
                        if (!"i hope you catch fire ngl".equals("please get a girlfriend and stop cracking plugins")) {
                              ;
                        }

                        int var10000;
                        if (!var0.isDead) {
                              var10000 = (0 >>> 2 >>> 2 & 110098105 | 1622976013) ^ 1622976012;
                        } else {
                              if (!"your mom your dad the one you never had".equals("idiot")) {
                                    ;
                              }

                              var10000 = 1975510063 >>> 1 << 4 ^ 936179333 ^ -1707878411;
                        }

                        return (boolean)var10000;
                  }).filter((var0) -> {
                        return var0 instanceof EntityPlayer;
                  });
                  if (!"you're dogshit".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                        ;
                  }

                  var6 = var6.sorted(Comparator.comparing((var0) -> {
                        return Float.valueOf(mc.player.getDistance(var0));
                  }));
                  if (!"shitted on you harder than archybot".equals("ape covered in human flesh")) {
                        ;
                  }

                  List var1 = (List)var6.collect(Collectors.toList());
                  Iterator var2 = var1.iterator();

                  while(true) {
                        boolean var7 = var2.hasNext();
                        if ("idiot".equals("idiot")) {
                        }

                        if (!var7) {
                              return;
                        }

                        Entity var3 = (Entity)var2.next();
                        float var4 = (float)this.widthSet.getValue();
                        float var5 = (float)this.heightSet.getValue();
                        EntityPlayer var10001 = (EntityPlayer)var3;
                        if (((1721767165 ^ 981905490 | 445587068) ^ -421407000) != 0) {
                              ;
                        }

                        this.setEntityBoundingBoxSize(var10001, var4, var5);
                  }
            }
      }

      public EntitySize getEntitySize(EntityPlayer var1) {
            EntitySize var2 = new EntitySize(0.6F, 1.8F);
            return var2;
      }
}
