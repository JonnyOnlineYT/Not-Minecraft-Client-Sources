//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.movement;

import java.lang.reflect.Field;
import me.independed.inceptice.modules.Module;
import me.independed.inceptice.settings.ModeSetting;
import me.independed.inceptice.settings.NumberSetting;
import me.independed.inceptice.settings.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class spidiki5 extends Module {
      public NumberSetting speedBlocksPerSec;
      public ModeSetting modeSetting;
      public NumberSetting multiplier;
      public NumberSetting bounceheight;

      public static boolean isMoving() {
            int var10000;
            if (mc.player != null) {
                  float var0;
                  var10000 = (var0 = mc.player.movementInput.moveForward - 0.0F) == 0.0F ? 0 : (var0 < 0.0F ? -1 : 1);
                  if (!"please take a shower".equals("intentMoment")) {
                        ;
                  }

                  if (var10000 != 0 || mc.player.movementInput.moveStrafe != 0.0F) {
                        if ((1222571175 << 3 >> 4 >> 1 ^ -1721536595) != 0) {
                              ;
                        }

                        var10000 = (0 >> 4 & 244436805) >>> 4 ^ 1;
                        return (boolean)var10000;
                  }
            }

            var10000 = 1581530508 >> 4 ^ 44833275 ^ 122182179;
            return (boolean)var10000;
      }

      public static float getSpeed() {
            return (float)Math.sqrt(mc.player.motionX * mc.player.motionX + mc.player.motionZ * mc.player.motionZ);
      }

      @SubscribeEvent
      public void onPlayerTick(PlayerTickEvent var1) {
            if (mc.player != null) {
                  if (((909923254 | 592247660) ^ 684699117 ^ 531846675) == 0) {
                        ;
                  }

                  label159: {
                        double var19;
                        if (this.modeSetting.activeMode == "BHop") {
                              boolean var10000 = mc.player.onGround;
                              if ((536327904 >> 4 >>> 2 >>> 4 ^ -1373938244) != 0) {
                                    ;
                              }

                              if (var10000 && mc.player.moveForward != 0.0F) {
                                    NetHandlerPlayClient var13 = mc.player.connection;
                                    CPacketPlayer var10001 = new CPacketPlayer;
                                    if (!"please dont crack my plugin".equals("please dont crack my plugin")) {
                                          ;
                                    }

                                    var10001.<init>((boolean)(0 << 4 & 265962750 & 447337167 ^ 689432832 ^ 689432833));
                                    var13.sendPacket(var10001);
                                    if ((582237483 << 1 >> 1 & 562946146 ^ 545529890) == 0) {
                                          ;
                                    }

                                    mc.player.jump();
                              }

                              float var2 = mc.player.rotationYaw;
                              if (((1087751499 >>> 3 >>> 1 | 28260241) ^ -1464440153) != 0) {
                                    ;
                              }

                              Vec3d var3 = Vec3d.fromPitchYaw(0.0F, var2);
                              float var16 = var2 + 90.0F;
                              if (((1470920836 | 408293262) >>> 4 << 2 ^ 211720592 ^ 459343472) == 0) {
                                    ;
                              }

                              Vec3d var4 = Vec3d.fromPitchYaw(0.0F, var16);
                              double var5 = 0.0D;
                              if (!"buy a domain and everything else you need at namecheap.com".equals("please take a shower")) {
                                    ;
                              }

                              double var7 = 0.0D;
                              int var9 = 265747894 >>> 1 << 3 ^ 1062991576;
                              if (((785658213 ^ 445915927 | 395356977 | 322587360) << 3 ^ -1076371560) == 0) {
                                    ;
                              }

                              if (mc.player.movementInput.forwardKeyDown) {
                                    var19 = var3.x / 20.0D * this.speedBlocksPerSec.getValue();
                                    if ((1764532010 << 2 << 4 >> 2 >>> 3 ^ 39403092) == 0) {
                                          ;
                                    }

                                    var5 += var19;
                                    var19 = var3.z;
                                    if (((649185086 ^ 587450508) << 4 ^ 1836610588) != 0) {
                                          ;
                                    }

                                    if (!"nefariousMoment".equals("You're so fat whenever you go to the beach the tide comes in.")) {
                                          ;
                                    }

                                    var19 /= 20.0D;
                                    if (!"yo mama name maurice".equals("please dont crack my plugin")) {
                                          ;
                                    }

                                    var7 += var19 * this.speedBlocksPerSec.getValue();
                                    var9 = (0 & 64674575) >>> 1 ^ 540342306 ^ 540342307;
                              }

                              if (mc.player.movementInput.backKeyDown) {
                                    var5 -= var3.x / 20.0D * this.speedBlocksPerSec.getValue();
                                    var7 -= var3.z / 20.0D * this.speedBlocksPerSec.getValue();
                                    var9 = 0 << 1 >> 1 >>> 3 << 2 >>> 3 ^ 1;
                              }

                              if (((829558918 >> 3 & 18439684) >>> 2 ^ -2001157628) != 0) {
                                    ;
                              }

                              int var10 = (980475235 | 15167537) << 4 << 4 ^ -134384896;
                              if (mc.player.movementInput.rightKeyDown) {
                                    if ((((74975263 ^ 70771515) & 3575368 ^ 376938) >>> 1 ^ 107829) == 0) {
                                          ;
                                    }

                                    var5 += var4.x / 20.0D * this.speedBlocksPerSec.getValue();
                                    var7 += var4.z / 20.0D * this.speedBlocksPerSec.getValue();
                                    var10 = 0 >> 1 ^ 841968706 ^ 841968707;
                              }

                              if (mc.player.movementInput.leftKeyDown) {
                                    var19 = var4.x / 20.0D;
                                    if (((593277715 ^ 435104070 | 145907492) >> 1 ^ 634429322) != 0) {
                                          ;
                                    }

                                    var5 -= var19 * this.speedBlocksPerSec.getValue();
                                    var7 -= var4.z / 20.0D * this.speedBlocksPerSec.getValue();
                                    if ((758422282 >> 2 >> 2 ^ 458583 ^ 47560423) == 0) {
                                          ;
                                    }

                                    int var14 = 0 & 1613949855 & 1993368005 ^ 1;
                                    if (((1885325010 ^ 1130350295 ^ 730554176) >> 1 ^ -2076337295) != 0) {
                                          ;
                                    }

                                    var10 = var14;
                              }

                              if (var9 != 0 && var10 != 0) {
                                    double var11 = 1.0D / Math.sqrt(2.0D);
                                    var5 *= var11;
                                    var7 *= var11;
                              }

                              mc.player.motionX = var5;
                              mc.player.motionZ = var7;
                              if (((1525479411 >>> 2 ^ 289777684) >>> 3 ^ 1851946139) != 0) {
                                    ;
                              }
                        } else {
                              if (Minecraft.getMinecraft().player.collidedHorizontally) {
                                    break label159;
                              }

                              EntityPlayerSP var15 = Minecraft.getMinecraft().player;
                              if ((1313093460 << 4 >> 4 ^ -1321054889) != 0) {
                                    ;
                              }

                              if (var15.isSneaking()) {
                                    break label159;
                              }

                              if (((589422256 << 3 >> 4 >> 3 | 1857864) ^ 4087275) == 0) {
                                    ;
                              }

                              float var17 = Minecraft.getMinecraft().player.moveForward;
                              if (((660339807 | 43275817 | 148707349) >> 3 ^ 100401103) == 0) {
                                    ;
                              }

                              if (var17 != 0.0F && Minecraft.getMinecraft().player.onGround || Minecraft.getMinecraft().player.moveStrafing != 0.0F && Minecraft.getMinecraft().player.onGround) {
                                    Minecraft var18 = Minecraft.getMinecraft();
                                    if ((((1050022518 | 940214432) << 4 | 1077953384) >>> 4 ^ 1345964368) != 0) {
                                          ;
                                    }

                                    var18.player.jump();
                                    Minecraft.getMinecraft().player.motionY = (double)((float)this.bounceheight.value);
                                    var15 = Minecraft.getMinecraft().player;
                                    var19 = var15.motionX;
                                    NumberSetting var10002 = this.multiplier;
                                    if (((2037875483 | 420256315 | 319407195) >>> 4 & 110099637 ^ -1528724376) != 0) {
                                          ;
                                    }

                                    var15.motionX = var19 * var10002.value;
                                    var15 = Minecraft.getMinecraft().player;
                                    var19 = var15.motionZ;
                                    double var20 = this.multiplier.value;
                                    if (!"please get a girlfriend and stop cracking plugins".equals("please get a girlfriend and stop cracking plugins")) {
                                          ;
                                    }

                                    var15.motionZ = var19 * var20;
                              }
                        }

                        return;
                  }

                  if ((160088382 >> 1 >> 2 << 1 & 12226069 ^ 2260996) == 0) {
                        ;
                  }

            }
      }

      public static double getDirection() {
            float var0 = mc.player.rotationYaw;
            if (((1345408750 >>> 1 >>> 2 | 133005650) ^ 267364831) == 0) {
                  ;
            }

            float var10000;
            if (mc.player.moveForward < 0.0F) {
                  var10000 = var0 + 180.0F;
                  if ((35653892 >> 2 & 6828086 ^ 524288) == 0) {
                        ;
                  }

                  var0 = var10000;
            }

            float var1 = 1.0F;
            if (mc.player.moveForward < 0.0F) {
                  var1 = -0.5F;
            } else {
                  if (((1138201566 >> 4 ^ 12112795 ^ 32989490) >> 1 ^ 45704650) == 0) {
                        ;
                  }

                  if (mc.player.moveForward > 0.0F) {
                        if (!"stop skidding".equals("please go outside")) {
                              ;
                        }

                        var1 = 0.5F;
                  }
            }

            if (mc.player.moveStrafing > 0.0F) {
                  var0 -= 90.0F * var1;
            }

            if (mc.player.moveStrafing < 0.0F) {
                  var10000 = var0 + 90.0F * var1;
                  if ((1657783190 >> 4 >> 3 << 4 ^ 207222896) == 0) {
                        ;
                  }

                  var0 = var10000;
            }

            double var2 = (double)var0;
            if ((247955448 >> 1 << 3 & 516937501 ^ 373504902 ^ -1346318324) != 0) {
                  ;
            }

            return Math.toRadians(var2);
      }

      public static void setTimerSpeed(float var0) {
            try {
                  Class var1 = Minecraft.class;
                  Field var2 = var1.getDeclaredField("timer");
                  var2.setAccessible((boolean)((0 >> 1 | 194593741) ^ 194593740));

                  try {
                        if (((232957126 << 2 & 760027321 | 158178845) ^ 81780801) != 0) {
                              ;
                        }

                        Object var3 = var2.get(Minecraft.getMinecraft());
                        Class var4 = ((Object)var3).getClass();
                        Field var5 = var4.getDeclaredField("timerSpeed");
                        var5.setAccessible((boolean)((0 << 4 ^ 1930919740 ^ 681623291 ^ 1242256629) << 3 ^ -1914394223));
                        if ((((482513684 ^ 38418224) & 309544467) << 4 ^ -435281564) != 0) {
                              ;
                        }

                        var5.setFloat(var3, var0);
                  } catch (IllegalAccessException var6) {
                        var6.printStackTrace();
                  }
            } catch (NoSuchFieldException var7) {
                  var7.printStackTrace();
            }

      }

      public static double[] forward(double var0) {
            float var2 = Minecraft.getMinecraft().player.movementInput.moveForward;
            float var3 = Minecraft.getMinecraft().player.movementInput.moveStrafe;
            Minecraft var10000 = Minecraft.getMinecraft();
            if (!"please take a shower".equals("please go outside")) {
                  ;
            }

            float var13 = var10000.player.prevRotationYaw;
            float var10001 = Minecraft.getMinecraft().player.rotationYaw;
            if (!"intentMoment".equals("please go outside")) {
                  ;
            }

            float var4 = var13 + (var10001 - Minecraft.getMinecraft().player.prevRotationYaw) * Minecraft.getMinecraft().getRenderPartialTicks();
            if (var2 != 0.0F) {
                  if ((285293736 >>> 4 >>> 1 << 3 ^ 511022995) != 0) {
                        ;
                  }

                  if (var3 > 0.0F) {
                        if ((((1746485100 | 1330879909) & 180918073) >>> 1 ^ 86264724) == 0) {
                              ;
                        }

                        var4 += (float)(var2 > 0.0F ? 1683847894 >> 3 >>> 1 >> 1 & 5688002 ^ -183023 : 34 << 4 ^ 179 ^ 702);
                  } else if (var3 < 0.0F) {
                        if ((1595572180 >> 1 & 382129495 ^ -1370858483) != 0) {
                              ;
                        }

                        int var15;
                        if (var2 > 0.0F) {
                              var15 = (8 | 7) >>> 4 & 456233587 ^ 45;
                        } else {
                              if ((71312896 >> 2 ^ 8017393 ^ 4870438 ^ 360318633) != 0) {
                                    ;
                              }

                              var15 = (420532925 ^ 361203903) >>> 1 ^ 57808071 ^ -87669227;
                        }

                        var4 += (float)var15;
                  }

                  var3 = 0.0F;
                  if (var2 > 0.0F) {
                        if (((631626658 | 350677129) ^ 575371063 ^ 397184156) == 0) {
                              ;
                        }

                        var2 = 1.0F;
                  } else if (var2 < 0.0F) {
                        var2 = -1.0F;
                        if ((((1044999988 << 3 ^ 80224924) & 1042207604) << 3 ^ -1340958304) == 0) {
                              ;
                        }
                  }
            }

            double var5 = Math.sin(Math.toRadians((double)(var4 + 90.0F)));
            double var14 = (double)(var4 + 90.0F);
            if ((1068570288 ^ 1024259030 ^ 26317459 ^ -882099567) != 0) {
                  ;
            }

            double var7 = Math.cos(Math.toRadians(var14));
            if (((1016680194 >> 3 | 119003948) << 1 ^ -304515589) != 0) {
                  ;
            }

            var14 = (double)var2;
            if ((451391210 >> 4 >>> 1 & 2638458 & 26 ^ 18) == 0) {
                  ;
            }

            double var9 = var14 * var0 * var7 + (double)var3 * var0 * var5;
            var14 = (double)var2 * var0;
            if (!"You're so fat whenever you go to the beach the tide comes in.".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                  ;
            }

            double var11 = var14 * var5 - (double)var3 * var0 * var7;
            int var16 = 0 << 4 & 1425251105 ^ 2;
            if (!"you probably spell youre as your".equals("please dont crack my plugin")) {
                  ;
            }

            double[] var17 = new double[var16];
            if ((((847200344 | 368691026 | 569306213) & 361272157 | 163175349) ^ -2005558581) != 0) {
                  ;
            }

            var17[(1715683164 >>> 4 & 60423750) << 4 ^ 537076800] = var9;
            var17[((0 | 1938696406 | 1888606558) >> 2 | 389395140) ^ 536346358] = var11;
            return var17;
      }

      public spidiki5() {
            int var10003 = 1741729419 << 4 & 654128328 ^ 604512384;
            if (!"please dont crack my plugin".equals("shitted on you harder than archybot")) {
                  ;
            }

            Module.Category var10004 = Module.Category.MOVEMENT;
            if (((1749079947 << 4 | 1453402325) ^ 106346014) != 0) {
                  ;
            }

            super("Speed", "faster moving", var10003, var10004);
            this.speedBlocksPerSec = new NumberSetting("Speed", this, 5.6D, 4.0D, 100.0D, 0.1D);
            String[] var10006 = new String[((0 & 692581118) << 1 ^ 1607766334) & 299777482 ^ 299110664];
            var10006[(16818314 | 1996239 | 5148054) ^ 23002591] = "BHop";
            var10006[(0 | 97179384) >> 4 ^ 6073710] = "Bounce";
            this.modeSetting = new ModeSetting("Mode", this, "BHop", var10006);
            this.multiplier = new NumberSetting("Multiplier", this, 1.0D, 0.5D, 8.0D, 0.1D);
            this.bounceheight = new NumberSetting("JumpHeight", this, 0.2D, 0.1D, 7.0D, 0.1D);
            Setting[] var10001 = new Setting[1 >>> 1 & 2090165357 & 604390880 ^ 4];
            var10001[(1011161613 >>> 1 | 105712546) & 394184475 ^ 376342274] = this.speedBlocksPerSec;
            var10001[0 << 4 >> 3 ^ 1] = this.modeSetting;
            var10001[(1 >> 3 | 619323639) ^ 619323637] = this.multiplier;
            var10001[0 << 3 >> 3 >>> 2 ^ 3] = this.bounceheight;
            this.addSettings(var10001);
      }

      public static void setMoveSpeed(double var0) {
            double var2 = (double)mc.player.movementInput.moveForward;
            double var4 = (double)mc.player.movementInput.moveStrafe;
            Minecraft var10000 = mc;
            if (!"yo mama name maurice".equals("ape covered in human flesh")) {
                  ;
            }

            float var6 = var10000.player.rotationYaw;
            if (var2 == 0.0D && var4 == 0.0D) {
                  mc.player.motionX = 0.0D;
                  mc.player.motionZ = 0.0D;
            } else {
                  if (var2 != 0.0D) {
                        int var10001;
                        if (var4 > 0.0D) {
                              if (var2 > 0.0D) {
                                    var10001 = ((1441497179 ^ 1354650199) & 57258203) >>> 2 ^ 1226712 ^ -4379639;
                                    if (((1037190338 ^ 645678411 ^ 427547205 | 25883027) << 4 ^ 59521707) != 0) {
                                          ;
                                    }
                              } else {
                                    if (((872825728 | 32818213 | 800820762) & 236152768 ^ 236144512) == 0) {
                                          ;
                                    }

                                    var10001 = ((40 & 11) >> 2 | 1) ^ 46;
                                    if (!"yo mama name maurice".equals("stringer is a good obfuscator")) {
                                          ;
                                    }
                              }

                              var6 += (float)var10001;
                        } else {
                              double var11;
                              int var7 = (var11 = var4 - 0.0D) == 0.0D ? 0 : (var11 < 0.0D ? -1 : 1);
                              if (((1457020544 ^ 205600688) & 199741986 & 16438537 ^ 8404992) == 0) {
                                    ;
                              }

                              if (var7 < 0) {
                                    var10001 = var2 > 0.0D ? (31 | 8 | 12) & 20 ^ 18 ^ 43 : (959433566 >> 2 >> 3 >> 4 >>> 2 | '픶') ^ -521684;
                                    if (!"please go outside".equals("you probably spell youre as your")) {
                                          ;
                                    }

                                    var6 += (float)var10001;
                              }
                        }

                        if ((1583241430 >> 2 >>> 1 << 2 ^ 791620712) == 0) {
                              ;
                        }

                        var4 = 0.0D;
                        if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("you probably spell youre as your")) {
                              ;
                        }

                        if (var2 > 0.0D) {
                              var2 = 1.0D;
                        } else {
                              if (((1178072986 | 576871210 | 388943129 | 1239033798) ^ -420864720) != 0) {
                                    ;
                              }

                              if (var2 < 0.0D) {
                                    var2 = -1.0D;
                              }
                        }
                  }

                  EntityPlayerSP var8 = mc.player;
                  double var9 = var2 * var0 * Math.cos(Math.toRadians((double)(var6 + 90.0F)));
                  double var10002 = var4 * var0;
                  float var10003 = var6 + 90.0F;
                  if (((1436796635 | 1376497900) << 2 >> 2 ^ -1056876897) != 0) {
                        ;
                  }

                  var8.motionX = var9 + var10002 * Math.sin(Math.toRadians((double)var10003));
                  var8 = mc.player;
                  if (!"please dont crack my plugin".equals("nefariousMoment")) {
                        ;
                  }

                  var9 = var2 * var0 * Math.sin(Math.toRadians((double)(var6 + 90.0F)));
                  var10002 = var4 * var0;
                  double var10 = Math.toRadians((double)(var6 + 90.0F));
                  if ((775172833 << 3 ^ 885217158 ^ -1389007055) != 0) {
                        ;
                  }

                  var8.motionZ = var9 - var10002 * Math.cos(var10);
            }

      }

      public static double getBaseMoveSpeed() {
            if (((118288961 ^ 103755648) & 8549774 ^ 152960) == 0) {
                  ;
            }

            double var0 = 0.2873D;
            if (((1076956163 << 1 & 612150037) >>> 3 ^ -945191656) != 0) {
                  ;
            }

            if (Minecraft.getMinecraft().player != null && Minecraft.getMinecraft().player.isPotionActive(Potion.getPotionById((0 & 401934815) >> 1 ^ 1))) {
                  EntityPlayerSP var10000 = Minecraft.getMinecraft().player;
                  Potion var10001 = Potion.getPotionById((0 >>> 4 | 344950388) ^ 344950389);
                  if (((91495792 | 59077524) >> 4 >>> 4 >>> 4 >> 3 ^ 615472625) != 0) {
                        ;
                  }

                  int var3 = var10000.getActivePotionEffect(var10001).getAmplifier();
                  if (((129483231 << 1 ^ 44673948 | 71637923 | 152665098) >> 3 ^ 29093877) == 0) {
                        ;
                  }

                  int var2 = var3;
                  if ((438993088 >>> 1 >> 2 ^ 1403405348) != 0) {
                        ;
                  }

                  double var10002 = 0.2D * (double)(var2 + (((0 | 1049781512) ^ 416573832) << 1 ^ 541122219 ^ 1825359786));
                  if (((142639179 ^ 14494953 | 4290379) << 3 ^ -1147727813) != 0) {
                        ;
                  }

                  var0 *= 1.0D + var10002;
                  if (((905677894 >>> 4 ^ 22738872) >>> 4 & 1677344 ^ 4128) == 0) {
                        ;
                  }
            }

            return var0;
      }

      public static void setSpeed(EntityLivingBase var0, double var1) {
            double[] var3 = (double[])spidiki5.forward(var1);
            var0.motionX = var3[((1495719197 | 944394715) & 1353427421 ^ 1048260371) >>> 1 ^ 925422439];
            if ((133435512 >> 2 ^ 27056607 ^ -1822789075) != 0) {
                  ;
            }

            var0.motionZ = var3[0 << 4 >> 1 ^ 1];
            if (((337117728 | 101385569) << 4 >>> 4 ^ 102434657) == 0) {
                  ;
            }

      }
}
