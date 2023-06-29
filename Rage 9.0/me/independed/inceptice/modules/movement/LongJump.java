//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.movement;

import me.independed.inceptice.modules.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class LongJump extends Module {
      private double moveSpeed;
      private double lastDist;
      public static int stage;

      public static void setMoveSpeed(double var0) {
            EntityPlayerSP var10000 = mc.player;
            if (!"stringer is a good obfuscator".equals("shitted on you harder than archybot")) {
                  ;
            }

            label117: {
                  double var2 = (double)var10000.movementInput.moveForward;
                  double var4 = (double)mc.player.movementInput.moveStrafe;
                  float var6 = mc.player.rotationYaw;
                  int var7;
                  if (var2 == 0.0D) {
                        double var10;
                        var7 = (var10 = var4 - 0.0D) == 0.0D ? 0 : (var10 < 0.0D ? -1 : 1);
                        if ((5918864 << 4 & 74637809 ^ 69206272) == 0) {
                              ;
                        }

                        if (var7 == 0) {
                              mc.player.motionX = 0.0D;
                              var10000 = mc.player;
                              if (((353391313 | 40399236) >> 4 ^ -578580687) != 0) {
                                    ;
                              }

                              var10000.motionZ = 0.0D;
                              break label117;
                        }
                  }

                  if (!"please take a shower".equals("please get a girlfriend and stop cracking plugins")) {
                        ;
                  }

                  if (((423705958 >> 3 ^ 37005798 | 489200) ^ -796484247) != 0) {
                        ;
                  }

                  if (var2 != 0.0D) {
                        if (var4 > 0.0D) {
                              var6 += (float)(var2 > 0.0D ? ((1381348510 ^ 788726646) >> 2 >>> 2 | 129206770) ^ -133688275 : 23 >> 2 >> 1 ^ 1 ^ 0 ^ 46);
                        } else {
                              double var11;
                              var7 = (var11 = var4 - 0.0D) == 0.0D ? 0 : (var11 < 0.0D ? -1 : 1);
                              if ((17320073 << 4 >> 4 & 10600169 ^ 101688548) != 0) {
                                    ;
                              }

                              if (var7 < 0) {
                                    if ((475237174 << 4 >> 2 ^ -246534952) == 0) {
                                          ;
                                    }

                                    double var12;
                                    int var10001 = (var12 = var2 - 0.0D) == 0.0D ? 0 : (var12 < 0.0D ? -1 : 1);
                                    if (!"yo mama name maurice".equals("please go outside")) {
                                          ;
                                    }

                                    var6 += (float)(var10001 > 0 ? (41 ^ 11 | 32 | 19) ^ 30 : 570526472 >> 3 ^ -71315790);
                              }
                        }

                        var4 = 0.0D;
                        if (((785771560 << 2 & 492676692) >> 2 ^ 106260480) == 0) {
                              ;
                        }

                        if (var2 > 0.0D) {
                              if (!"you probably spell youre as your".equals("you probably spell youre as your")) {
                                    ;
                              }

                              var2 = 1.0D;
                              if ((((850867503 ^ 785158347) >>> 2 | 72829519) ^ 112259584 ^ 32446847) == 0) {
                                    ;
                              }
                        } else if (var2 < 0.0D) {
                              var2 = -1.0D;
                        }
                  }

                  var10000 = mc.player;
                  double var8 = var2 * var0 * Math.cos(Math.toRadians((double)(var6 + 90.0F)));
                  if (((488708857 ^ 219460582 | 108415423) >>> 2 & 19527043 ^ 932092841) != 0) {
                        ;
                  }

                  double var10002 = var4 * var0;
                  float var10003 = var6 + 90.0F;
                  if ((1686198274 >> 3 ^ 210774784) == 0) {
                        ;
                  }

                  double var9 = (double)var10003;
                  if (((979272374 >> 2 & 65346705) >> 4 & 1240122 ^ 16392) == 0) {
                        ;
                  }

                  var10000.motionX = var8 + var10002 * Math.sin(Math.toRadians(var9));
                  var10000 = mc.player;
                  if (!"nefariousMoment".equals("please get a girlfriend and stop cracking plugins")) {
                        ;
                  }

                  var8 = var2 * var0;
                  var10002 = Math.toRadians((double)(var6 + 90.0F));
                  if (((146966553 ^ 64016249 | 177800663) ^ 194725879) == 0) {
                        ;
                  }

                  var8 *= Math.sin(var10002);
                  if (((177401219 << 4 >>> 3 | 239572454 | 292896817) ^ 995329704) != 0) {
                        ;
                  }

                  var10002 = var4 * var0 * Math.cos(Math.toRadians((double)(var6 + 90.0F)));
                  if ((1970284916 << 3 << 1 ^ 1459787584) == 0) {
                        ;
                  }

                  var10000.motionZ = var8 - var10002;
            }

            if (((64450130 << 2 | 252789754) >> 2 ^ -1576358451) != 0) {
                  ;
            }

      }

      public LongJump() {
            if (((1256361525 ^ 681509710) >>> 1 << 2 ^ -990176524) == 0) {
                  ;
            }

            super("LongJump", "long jump okay", (407431410 << 1 ^ 147145103) & 337421904 ^ 269779008, Module.Category.MOVEMENT);
            this.lastDist = 0.0D;
      }

      @SubscribeEvent
      public void onPlayerTick(PlayerTickEvent var1) {
            if (mc.player != null) {
                  if (spidiki5.isMoving()) {
                        Minecraft var10000 = mc;
                        if (!"please dont crack my plugin".equals("idiot")) {
                              ;
                        }

                        if (var10000.player.moveForward == 0.0F && mc.player.moveStrafing == 0.0F) {
                              if (stage > (0 & 682088593 & 982653301 ^ 1)) {
                                    if (((1926296921 | 144790049) ^ 965623879 ^ 1132443454) == 0) {
                                          ;
                                    }

                                    stage = ((1073795670 | 329781379) & 1352588981) >> 2 >>> 4 ^ 21111626;
                              }
                        } else {
                              double var10001;
                              if (stage == 0) {
                                    double var10002 = spidiki5.getBaseMoveSpeed();
                                    if (!"nefariousMoment".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                                          ;
                                    }

                                    var10001 = 1.0D + var10002;
                                    if ((1910684264 >> 1 << 4 & 680965633 ^ 135565824) == 0) {
                                          ;
                                    }

                                    this.moveSpeed = var10001 - 0.05D;
                              } else if (stage == (0 << 2 >> 1 >>> 4 ^ 294027422 ^ 294027423)) {
                                    mc.player.motionY = 0.42D;
                                    this.moveSpeed *= 2.13D;
                              } else if (stage == ((0 >> 4 & 1772129204) >> 1 >>> 3 >>> 1 ^ 2)) {
                                    var10001 = this.lastDist - spidiki5.getBaseMoveSpeed();
                                    if ((((543440915 << 1 ^ 443132552) & 1269317292 & 1052001718) << 4 ^ -2009613710) != 0) {
                                          ;
                                    }

                                    double var2 = 0.66D * var10001;
                                    if (!"stringer is a good obfuscator".equals("please go outside")) {
                                          ;
                                    }

                                    if (((692728961 >> 1 | 221025888) >>> 2 >>> 4 ^ 7779961) == 0) {
                                          ;
                                    }

                                    var10001 = this.lastDist;
                                    if (((1114274204 >>> 3 & 86243189 | 19826) << 4 ^ -1355423558) != 0) {
                                          ;
                                    }

                                    this.moveSpeed = var10001 - var2;
                              } else {
                                    var10001 = this.lastDist;
                                    if (((438102255 | 83768634 | 190864962) >> 4 << 3 ^ 1614875514) != 0) {
                                          ;
                                    }

                                    this.moveSpeed = var10001 - this.lastDist / 159.0D;
                              }

                              this.moveSpeed = Math.max(spidiki5.getBaseMoveSpeed(), this.moveSpeed);
                              if ((1842140087 >> 3 >> 3 >> 4 ^ 1798964) == 0) {
                                    ;
                              }

                              LongJump.setMoveSpeed(this.moveSpeed);
                              int var4 = stage;
                              if (!"please go outside".equals("stop skidding")) {
                                    ;
                              }

                              stage = var4 + (((0 & 638355302 | 402114128) & 89713508 | 63652484) << 2 ^ 527371025);
                        }
                  }

            }
      }
}
