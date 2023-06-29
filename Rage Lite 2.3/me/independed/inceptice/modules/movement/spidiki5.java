//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.movement;

import java.lang.reflect.Field;
import me.independed.inceptice.modules.Module;
import me.independed.inceptice.settings.ModeSetting;
import me.independed.inceptice.settings.NumberSetting;
import me.independed.inceptice.settings.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class spidiki5 extends Module {
      public ModeSetting modeSetting;
      public NumberSetting speedBlocksPerSec;
      public NumberSetting bounceheight;
      public NumberSetting multiplier;

      public static float getSpeed() {
            double var10000 = mc.player.motionX;
            double var10001 = mc.player.motionX;
            if (!"idiot".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                  ;
            }

            var10000 = Math.sqrt(var10000 * var10001 + mc.player.motionZ * mc.player.motionZ);
            if (((144420718 >> 2 ^ 13238255) >>> 2 ^ 532025256) != 0) {
                  ;
            }

            float var0 = (float)var10000;
            if (((1244529727 >>> 2 ^ 283404352) >> 1 ^ 20444327) == 0) {
                  ;
            }

            return var0;
      }

      public static double getDirection() {
            EntityPlayerSP var10000 = mc.player;
            if (((886157906 | 40861090) ^ 54763748 ^ -760703478) != 0) {
                  ;
            }

            float var0 = var10000.rotationYaw;
            float var2 = mc.player.moveForward;
            if (!"ape covered in human flesh".equals("buy a domain and everything else you need at namecheap.com")) {
                  ;
            }

            if (var2 < 0.0F) {
                  if (((807512246 << 3 & 1854399258 & 6341) >> 2 ^ 0) == 0) {
                        ;
                  }

                  var0 += 180.0F;
            }

            float var1 = 1.0F;
            if (!"stringer is a good obfuscator".equals("ape covered in human flesh")) {
                  ;
            }

            if (mc.player.moveForward < 0.0F) {
                  var1 = -0.5F;
                  if (((1258485800 | 407054433) >> 4 << 1 >> 2 ^ 47849411) == 0) {
                        ;
                  }
            } else if (mc.player.moveForward > 0.0F) {
                  var1 = 0.5F;
            }

            var2 = mc.player.moveStrafing;
            if (!"buy a domain and everything else you need at namecheap.com".equals("you're dogshit")) {
                  ;
            }

            if (var2 > 0.0F) {
                  var0 -= 90.0F * var1;
            }

            if (mc.player.moveStrafing < 0.0F) {
                  var0 += 90.0F * var1;
                  if ((1843544604 >>> 4 << 1 >>> 3 ^ 28805384) == 0) {
                        ;
                  }
            }

            return Math.toRadians((double)var0);
      }

      public static void setSpeed(EntityLivingBase var0, double var1) {
            double[] var3 = (double[])spidiki5.forward(var1);
            int var10002 = 1491162048 >> 2 << 4 >>> 1 ^ 834840448;
            if (((508156759 << 3 ^ 1614887431) << 1 ^ -1334516414) != 0) {
                  ;
            }

            var0.motionX = var3[var10002];
            var0.motionZ = var3[0 >> 3 >> 2 ^ 1];
      }

      public static void setTimerSpeed(float var0) {
            try {
                  Class var1 = Minecraft.class;
                  Field var2 = var1.getDeclaredField("timer");
                  if ((1024 >>> 2 ^ 806816716) != 0) {
                        ;
                  }

                  var2.setAccessible((boolean)((0 ^ 1604319257 | 197705056) ^ 1608514936));

                  try {
                        if (((1848474047 >>> 1 | 906504401) ^ 924761311) == 0) {
                              ;
                        }

                        if ((135224719 << 1 >> 1 & 84956588 & 6996 & 1457 ^ 917231330) != 0) {
                              ;
                        }

                        Object var10000 = var2.get(Minecraft.getMinecraft());
                        if (((613913574 << 1 & 1160341928) << 4 ^ -305133974) != 0) {
                              ;
                        }

                        Object var3 = var10000;
                        if (!"shitted on you harder than archybot".equals("please get a girlfriend and stop cracking plugins")) {
                              ;
                        }

                        Class var4 = ((Object)var3).getClass();
                        Field var5 = var4.getDeclaredField("timerSpeed");
                        var5.setAccessible((boolean)(((0 | 1382144154) & 277511290) >> 2 & 2226729 ^ 4609));
                        var5.setFloat(var3, var0);
                  } catch (IllegalAccessException var6) {
                        var6.printStackTrace();
                  }
            } catch (NoSuchFieldException var7) {
                  var7.printStackTrace();
            }

      }

      public static boolean isMoving() {
            int var10000;
            if (mc.player == null || mc.player.movementInput.moveForward == 0.0F && mc.player.movementInput.moveStrafe == 0.0F) {
                  var10000 = (861361060 ^ 832191246) >> 1 ^ 23508053;
                  if ((33685504 >>> 3 ^ 137566 ^ 4348254) == 0) {
                        ;
                  }
            } else {
                  var10000 = ((0 >> 2 ^ 1006612837) >>> 4 | 1713177) ^ 62914334;
            }

            return (boolean)var10000;
      }

      public spidiki5() {
            super("Speed", "faster moving", (1104486922 >>> 3 | 19484986) >> 1 ^ 77462205, Module.Category.MOVEMENT);
            NumberSetting var10001 = new NumberSetting;
            if (((437915274 ^ 360666009) >>> 3 << 4 >>> 4 ^ 1807779380) != 0) {
                  ;
            }

            if ((613199145 << 3 ^ 512316675 ^ 331975673) != 0) {
                  ;
            }

            var10001.<init>("Speed", this, 5.6D, 4.0D, 100.0D, 0.1D);
            this.speedBlocksPerSec = var10001;
            String[] var10006 = new String[(1 >>> 4 >>> 1 | 253103858) ^ 253103856];
            var10006[(570166330 >>> 3 | 17124561) >>> 3 ^ 11008442] = "BHop";
            var10006[0 << 2 >> 1 ^ 1448650968 ^ 1342297379 ^ 106528250] = "Bounce";
            this.modeSetting = new ModeSetting("Mode", this, "BHop", var10006);
            if (!"buy a domain and everything else you need at namecheap.com".equals("yo mama name maurice")) {
                  ;
            }

            var10001 = new NumberSetting;
            if (((132170240 << 2 | 352925920) ^ 529217760) == 0) {
                  ;
            }

            var10001.<init>("Multiplier", this, 1.0D, 0.5D, 8.0D, 0.1D);
            this.multiplier = var10001;
            this.bounceheight = new NumberSetting("JumpHeight", this, 0.2D, 0.1D, 7.0D, 0.1D);
            Setting[] var1 = new Setting[2 >>> 3 << 3 & 352774407 ^ 4];
            var1[((2068175224 | 1185061850) & 816232997) >>> 2 & 39881095 ^ 2130048] = this.speedBlocksPerSec;
            var1[(0 | 1421160068) >>> 3 ^ 177645009] = this.modeSetting;
            int var10003 = (1 >> 2 & 1404681849) >> 2 ^ 2;
            NumberSetting var10004 = this.multiplier;
            if (((1020982658 << 1 | 172607880) & 1648225600 ^ 1648224512) == 0) {
                  ;
            }

            var1[var10003] = var10004;
            var10003 = (1 << 2 & 1 | 1526770945) >>> 4 ^ 95423187;
            if ((1988253212 >>> 3 >> 1 >>> 1 ^ 62132912) == 0) {
                  ;
            }

            var1[var10003] = this.bounceheight;
            this.addSettings(var1);
      }

      public static void setMoveSpeed(double var0) {
            double var2 = (double)mc.player.movementInput.moveForward;
            double var4 = (double)mc.player.movementInput.moveStrafe;
            float var6 = mc.player.rotationYaw;
            EntityPlayerSP var10000;
            if (var2 == 0.0D && var4 == 0.0D) {
                  var10000 = mc.player;
                  if (!"stringer is a good obfuscator".equals("You're so fat whenever you go to the beach the tide comes in.")) {
                        ;
                  }

                  var10000.motionX = 0.0D;
                  mc.player.motionZ = 0.0D;
            } else {
                  if (var2 != 0.0D) {
                        if (!"buy a domain and everything else you need at namecheap.com".equals("stringer is a good obfuscator")) {
                              ;
                        }

                        int var10001;
                        if (var4 > 0.0D) {
                              if (var2 > 0.0D) {
                                    var10001 = (2024155881 ^ 1514214280) << 3 ^ -389663525;
                              } else {
                                    if ((393529623 << 1 << 4 ^ -291953952) == 0) {
                                          ;
                                    }

                                    var10001 = (13 | 7) ^ 12 ^ 46;
                              }

                              var6 += (float)var10001;
                        } else {
                              if ((1641329532 >>> 3 & 19127738 & 2227951 ^ 2135082) == 0) {
                                    ;
                              }

                              if (var4 < 0.0D) {
                                    if ((1121153 << 3 >>> 2 << 3 ^ 17938448) == 0) {
                                          ;
                                    }

                                    if ((10781250 << 4 ^ 107221167 ^ 204223631) == 0) {
                                          ;
                                    }

                                    if (var2 > 0.0D) {
                                          var10001 = ((25 | 11) & 17) >>> 1 ^ 37;
                                    } else {
                                          if ((7080202 << 3 ^ 8031771 ^ 62449436) != 0) {
                                                ;
                                          }

                                          var10001 = 424897332 << 4 >> 4 << 3 ^ 895788659;
                                    }

                                    var6 += (float)var10001;
                              }
                        }

                        if (((2043975210 ^ 1330298804 | 532196087) & 707583124 ^ 707561620) == 0) {
                              ;
                        }

                        var4 = 0.0D;
                        if (var2 > 0.0D) {
                              if ((183804382 >> 4 ^ 1843226 ^ 11758087) == 0) {
                                    ;
                              }

                              var2 = 1.0D;
                        } else if (var2 < 0.0D) {
                              var2 = -1.0D;
                        }
                  }

                  var10000 = mc.player;
                  double var7 = var2 * var0 * Math.cos(Math.toRadians((double)(var6 + 90.0F)));
                  double var10002 = var4 * var0;
                  double var10003 = Math.sin(Math.toRadians((double)(var6 + 90.0F)));
                  if ((((1294608831 ^ 1104365212) << 1 | 206821660) ^ -231827926) != 0) {
                        ;
                  }

                  var10000.motionX = var7 + var10002 * var10003;
                  if (((226216725 | 73649013) >> 4 << 4 ^ 226216816) == 0) {
                        ;
                  }

                  mc.player.motionZ = var2 * var0 * Math.sin(Math.toRadians((double)(var6 + 90.0F))) - var4 * var0 * Math.cos(Math.toRadians((double)(var6 + 90.0F)));
            }

      }

      @SubscribeEvent
      public void onPlayerTick(PlayerTickEvent var1) {
            if (((21746653 ^ 13833015 ^ 3556516) << 4 >> 3 ^ 336756057) != 0) {
                  ;
            }

            if (mc.player == null) {
                  if (((870424383 << 1 | 914000424) & 1986483900 ^ 1986205244) == 0) {
                        ;
                  }

            } else {
                  EntityPlayerSP var15;
                  if (this.modeSetting.activeMode == "BHop") {
                        if (mc.player.onGround && mc.player.moveForward != 0.0F) {
                              mc.player.connection.sendPacket(new CPacketPlayer((boolean)(((0 ^ 82732546) >> 4 & 1251736) >>> 4 ^ 8193)));
                              mc.player.jump();
                        }

                        float var2 = mc.player.rotationYaw;
                        Vec3d var3 = Vec3d.fromPitchYaw(0.0F, var2);
                        Vec3d var10000 = Vec3d.fromPitchYaw(0.0F, var2 + 90.0F);
                        if ((1740486141 >>> 3 >>> 4 ^ -130924443) != 0) {
                              ;
                        }

                        Vec3d var4 = var10000;
                        if ((79712832 << 3 ^ 637702656) == 0) {
                              ;
                        }

                        double var5 = 0.0D;
                        double var7 = 0.0D;
                        int var13 = (1221404048 ^ 2005608) << 2 ^ 592379872;
                        if (((973660411 | 205805112) ^ 919458349 ^ -197639384) != 0) {
                              ;
                        }

                        int var9 = var13;
                        if (!"stop skidding".equals("nefariousMoment")) {
                              ;
                        }

                        double var10001;
                        if (mc.player.movementInput.forwardKeyDown) {
                              if (((557072985 << 4 >>> 4 | 11608099) ^ -141266122) != 0) {
                                    ;
                              }

                              var10001 = var3.x;
                              if (((159944779 << 2 | 499551958) << 3 ^ 501020159) != 0) {
                                    ;
                              }

                              var5 += var10001 / 20.0D * this.speedBlocksPerSec.getValue();
                              var7 += var3.z / 20.0D * this.speedBlocksPerSec.getValue();
                              var9 = (0 >>> 2 ^ 2016084827 | 280768952) << 3 ^ -973496359;
                        }

                        if (mc.player.movementInput.backKeyDown) {
                              var5 -= var3.x / 20.0D * this.speedBlocksPerSec.getValue();
                              var7 -= var3.z / 20.0D * this.speedBlocksPerSec.getValue();
                              if (((202915300 | 133784406) >>> 1 & 83800758 ^ 145514908) != 0) {
                                    ;
                              }

                              var9 = (0 >> 2 >> 1 >>> 4 | 154521412) ^ 154521413;
                        }

                        int var10 = (1785839439 << 1 | 2006922533 | 664342654) ^ 1287190151 ^ -1152939656;
                        double var10002;
                        if (mc.player.movementInput.rightKeyDown) {
                              var10001 = var4.x / 20.0D;
                              var10002 = this.speedBlocksPerSec.getValue();
                              if (((439648612 | 404693536 | 403384148) ^ 1662004359) != 0) {
                                    ;
                              }

                              var5 += var10001 * var10002;
                              var7 += var4.z / 20.0D * this.speedBlocksPerSec.getValue();
                              var10 = (0 | 7698823) ^ 170830 ^ 7856840;
                        }

                        if (mc.player.movementInput.leftKeyDown) {
                              var10001 = var4.x / 20.0D;
                              var10002 = this.speedBlocksPerSec.getValue();
                              if ((((1826174585 ^ 802277448) & 1012145316) >> 1 ^ -2012483242) != 0) {
                                    ;
                              }

                              var5 -= var10001 * var10002;
                              if ((985090129 >> 1 << 4 ^ -1553130149) != 0) {
                                    ;
                              }

                              var7 -= var4.z / 20.0D * this.speedBlocksPerSec.getValue();
                              var10 = ((0 | 1811409440) & 1371185360 | 834162561) ^ 1908076416;
                        }

                        if (var9 != 0 && var10 != 0) {
                              if (!"please dont crack my plugin".equals("yo mama name maurice")) {
                                    ;
                              }

                              double var11 = 1.0D / Math.sqrt(2.0D);
                              if ((150016146 >>> 3 & 15084478 & 71854 ^ -1276206202) != 0) {
                                    ;
                              }

                              var5 *= var11;
                              var7 *= var11;
                        }

                        Minecraft var14 = mc;
                        if ((301747353 << 2 & 302736263 ^ -1589501619) != 0) {
                              ;
                        }

                        var15 = var14.player;
                        if (((520475166 >> 3 | 7786095) << 3 & 288307638 ^ 287781168) == 0) {
                              ;
                        }

                        var15.motionX = var5;
                        mc.player.motionZ = var7;
                        if (!"intentMoment".equals("your mom your dad the one you never had")) {
                              ;
                        }
                  } else {
                        label154: {
                              if (Minecraft.getMinecraft().player.collidedHorizontally || Minecraft.getMinecraft().player.isSneaking()) {
                                    return;
                              }

                              if (Minecraft.getMinecraft().player.moveForward == 0.0F || !Minecraft.getMinecraft().player.onGround) {
                                    if (Minecraft.getMinecraft().player.moveStrafing == 0.0F || !Minecraft.getMinecraft().player.onGround) {
                                          break label154;
                                    }

                                    if ((((450083578 | 15037069) & 75515147) << 3 ^ 13733305 ^ 1234353340) != 0) {
                                          ;
                                    }
                              }

                              var15 = Minecraft.getMinecraft().player;
                              if (((1783330957 << 1 | 329093915) >> 1 ^ -338691699) == 0) {
                                    ;
                              }

                              var15.jump();
                              Minecraft.getMinecraft().player.motionY = (double)((float)this.bounceheight.value);
                              var15 = Minecraft.getMinecraft().player;
                              var15.motionX *= this.multiplier.value;
                              var15 = Minecraft.getMinecraft().player;
                              var15.motionZ *= this.multiplier.value;
                        }
                  }

                  if (((665746706 ^ 100900112) >> 1 ^ 1638438909) != 0) {
                        ;
                  }

            }
      }

      public static double[] forward(double var0) {
            float var2 = Minecraft.getMinecraft().player.movementInput.moveForward;
            float var10000 = Minecraft.getMinecraft().player.movementInput.moveStrafe;
            if (!"shitted on you harder than archybot".equals("i hope you catch fire ngl")) {
                  ;
            }

            float var3 = var10000;
            if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("please get a girlfriend and stop cracking plugins")) {
                  ;
            }

            var10000 = Minecraft.getMinecraft().player.prevRotationYaw;
            Minecraft var10001 = Minecraft.getMinecraft();
            if (!"buy a domain and everything else you need at namecheap.com".equals("please take a shower")) {
                  ;
            }

            float var14 = var10001.player.rotationYaw - Minecraft.getMinecraft().player.prevRotationYaw;
            float var10002 = Minecraft.getMinecraft().getRenderPartialTicks();
            if (((1173634515 << 1 & 64152573 & 4228297) >> 2 ^ 1775315903) != 0) {
                  ;
            }

            float var4 = var10000 + var14 * var10002;
            if (((1112011587 >> 1 | 8072275 | 158953121) ^ 695992307) == 0) {
                  ;
            }

            if (var2 != 0.0F) {
                  if (var3 > 0.0F) {
                        int var15;
                        if (var2 > 0.0F) {
                              if ((((1377581861 | 1107387291) << 4 >>> 3 | 33808974) ^ 104593278) == 0) {
                                    ;
                              }

                              var15 = ((601516037 | 250814939) & 386148738) >>> 2 ^ -29362765;
                        } else {
                              var15 = (42 ^ 9) << 4 >> 3 ^ 6 ^ 109;
                        }

                        var4 += (float)var15;
                  } else if (var3 < 0.0F) {
                        var4 += (float)(var2 > 0.0F ? (40 & 9 | 5) & 7 ^ 3 ^ 43 : (890460470 << 4 ^ 1347578602 | 7885687 | 1392454) ^ -25165780);
                  }

                  if (((1857023711 >>> 4 | 24145949) & 91524529 ^ 28069835 ^ -217426165) != 0) {
                        ;
                  }

                  var3 = 0.0F;
                  if (var2 > 0.0F) {
                        var2 = 1.0F;
                  } else if (var2 < 0.0F) {
                        var2 = -1.0F;
                        if (((1711731514 << 3 | 453416134 | 861914393) ^ 998235615) == 0) {
                              ;
                        }
                  }
            }

            double var5 = Math.sin(Math.toRadians((double)(var4 + 90.0F)));
            if (!"yo mama name maurice".equals("stop skidding")) {
                  ;
            }

            var10000 = var4 + 90.0F;
            if ((1627718916 >> 2 >>> 1 ^ 203464864) == 0) {
                  ;
            }

            double var7 = Math.cos(Math.toRadians((double)var10000));
            double var13 = (double)var2 * var0 * var7;
            double var18 = (double)var3 * var0 * var5;
            if (!"you probably spell youre as your".equals("intentMoment")) {
                  ;
            }

            double var9 = var13 + var18;
            var13 = (double)var2 * var0;
            if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("shitted on you harder than archybot")) {
                  ;
            }

            double var11 = var13 * var5 - (double)var3 * var0 * var7;
            double[] var16 = new double[1 >>> 3 << 1 ^ 2];
            if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("intentMoment")) {
                  ;
            }

            var16[(601920684 >>> 1 ^ 284501944) >> 1 << 3 ^ 68532152] = var9;
            if (((1797228185 >>> 3 >> 2 & 43449749 | 32236496) ^ 2081223676) != 0) {
                  ;
            }

            int var17 = (0 >>> 2 << 3 | 619577424) ^ 619577425;
            if ((1517744178 << 3 >>> 2 >> 3 ^ 5484828 ^ -1128368373) != 0) {
                  ;
            }

            var16[var17] = var11;
            return var16;
      }

      public static double getBaseMoveSpeed() {
            double var0 = 0.2873D;
            if (Minecraft.getMinecraft().player != null && Minecraft.getMinecraft().player.isPotionActive(Potion.getPotionById(((0 & 808654723 ^ 1401283450) & 237536878) >> 2 & 1073622 ^ 8339))) {
                  if ((((1669359830 ^ 1194091987) >> 1 & 254130527 ^ 3318185) >> 3 ^ 1752106540) != 0) {
                        ;
                  }

                  int var10000 = Minecraft.getMinecraft().player.getActivePotionEffect(Potion.getPotionById(0 >>> 2 >>> 2 ^ 285433958 ^ 285433959)).getAmplifier();
                  if (((346058566 << 1 << 1 ^ 533667403) >> 3 ^ -1528240972) != 0) {
                        ;
                  }

                  int var2 = var10000;
                  var0 *= 1.0D + 0.2D * (double)(var2 + ((0 >>> 2 | 648556392 | 127726865) & 478504154 ^ 75785305));
            }

            if (((1499331273 >>> 2 | 291972055) ^ -257535263) != 0) {
                  ;
            }

            return var0;
      }
}
