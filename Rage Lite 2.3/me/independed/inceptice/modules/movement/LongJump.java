//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.movement;

import me.independed.inceptice.modules.Module;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class LongJump extends Module {
      private double lastDist = 0.0D;
      public static int stage;
      private double moveSpeed;

      @SubscribeEvent
      public void onPlayerTick(PlayerTickEvent var1) {
            if (mc.player != null) {
                  if (spidiki5.isMoving()) {
                        if ((((78072652 >> 2 & 17453682) >> 2 ^ 483033) >>> 4 ^ 1221185549) != 0) {
                              ;
                        }

                        if (mc.player.moveForward == 0.0F && mc.player.moveStrafing == 0.0F) {
                              if (((2085890342 << 2 ^ 381669458) & 841848579 ^ 573379586) == 0) {
                                    ;
                              }

                              if (stage > ((0 | 1354229100) & 807604034 ^ 270730561)) {
                                    stage = ((2035715920 ^ 1780487785) >>> 3 >>> 1 | 12155760 | 26982016) ^ 29360115;
                              }
                        } else {
                              if (stage == 0) {
                                    double var10001 = 1.0D + spidiki5.getBaseMoveSpeed();
                                    if (((499608662 >> 2 | 5705347) ^ 125296535) == 0) {
                                          ;
                                    }

                                    this.moveSpeed = var10001 - 0.05D;
                              } else if (stage == ((0 << 1 >>> 3 | 1011377422) << 3 ^ -498915215)) {
                                    mc.player.motionY = 0.42D;
                                    if (((778363255 >>> 3 | 13913388) ^ -306228419) != 0) {
                                          ;
                                    }

                                    this.moveSpeed *= 2.13D;
                              } else if (stage == ((0 | 2120016591) >> 1 ^ 1060008293)) {
                                    double var2 = 0.66D * (this.lastDist - spidiki5.getBaseMoveSpeed());
                                    this.moveSpeed = this.lastDist - var2;
                              } else {
                                    this.moveSpeed = this.lastDist - this.lastDist / 159.0D;
                              }

                              if (((660112875 | 248973019) << 1 >> 3 ^ 200794622) == 0) {
                                    ;
                              }

                              this.moveSpeed = Math.max(spidiki5.getBaseMoveSpeed(), this.moveSpeed);
                              LongJump.setMoveSpeed(this.moveSpeed);
                              if ((541065732 ^ 541065732) == 0) {
                                    ;
                              }

                              stage += ((0 | 2121455231) >> 2 & 441446645) >>> 1 ^ 218519627;
                        }
                  }

            }
      }

      public static void setMoveSpeed(double var0) {
            double var2 = (double)mc.player.movementInput.moveForward;
            double var10000 = (double)mc.player.movementInput.moveStrafe;
            if (((880290737 | 877036427) ^ 268388839 ^ 998367836) == 0) {
                  ;
            }

            double var4 = var10000;
            float var6 = mc.player.rotationYaw;
            if (var2 == 0.0D && var4 == 0.0D) {
                  mc.player.motionX = 0.0D;
                  mc.player.motionZ = 0.0D;
            } else {
                  if (var2 != 0.0D) {
                        if (var4 > 0.0D) {
                              if (((1206125195 | 488430792) ^ 119643292 ^ 1490895447) == 0) {
                                    ;
                              }

                              var6 += (float)(var2 > 0.0D ? ((512135085 | 108455668) >>> 2 & 18706879) >> 2 ^ -4672580 : (14 >> 1 ^ 5) >> 1 ^ 44);
                        } else {
                              if (((1695035616 | 5227496) >> 1 & 626567370 ^ 536914112) == 0) {
                                    ;
                              }

                              if (var4 < 0.0D) {
                                    double var9;
                                    int var10001 = (var9 = var2 - 0.0D) == 0.0D ? 0 : (var9 < 0.0D ? -1 : 1);
                                    if (((17372032 ^ 16370892) >> 2 ^ 326383828) != 0) {
                                          ;
                                    }

                                    var6 += (float)(var10001 > 0 ? (21 | 2) ^ 21 ^ 47 : 1703952076 << 4 ^ 954107759 ^ -1641900932);
                              }
                        }

                        var4 = 0.0D;
                        if (((1558941012 | 1257467522 | 1433502218) >>> 4 ^ 1907364 ^ 92895942 ^ -92996222) != 0) {
                              ;
                        }

                        if (var2 > 0.0D) {
                              var2 = 1.0D;
                        } else {
                              if (!"Some babies were dropped on their heads but you were clearly thrown at a wall.".equals("please dont crack my plugin")) {
                                    ;
                              }

                              if (var2 < 0.0D) {
                                    if ((390871597 << 3 >> 1 ^ 1138429952) != 0) {
                                          ;
                                    }

                                    var2 = -1.0D;
                              }
                        }
                  }

                  EntityPlayerSP var7 = mc.player;
                  if (!"you're dogshit".equals("intentMoment")) {
                        ;
                  }

                  double var8 = var2 * var0;
                  if (((782399081 << 4 | 103892059) ^ -298359077) == 0) {
                        ;
                  }

                  var8 *= Math.cos(Math.toRadians((double)(var6 + 90.0F)));
                  if (!"intentMoment".equals("idiot")) {
                        ;
                  }

                  double var10002 = var4 * var0;
                  float var10003 = var6 + 90.0F;
                  if ((((1999159703 ^ 656289648) & 360837565) << 1 >> 2 ^ 134246482) == 0) {
                        ;
                  }

                  var7.motionX = var8 + var10002 * Math.sin(Math.toRadians((double)var10003));
                  var7 = mc.player;
                  if (((1939691601 ^ 176054344) & 913799653 ^ 811808769) == 0) {
                        ;
                  }

                  var7.motionZ = var2 * var0 * Math.sin(Math.toRadians((double)(var6 + 90.0F))) - var4 * var0 * Math.cos(Math.toRadians((double)(var6 + 90.0F)));
            }

      }

      public LongJump() {
            super("LongJump", "long jump okay", 600192 >> 1 << 4 ^ 4801536, Module.Category.MOVEMENT);
      }
}
