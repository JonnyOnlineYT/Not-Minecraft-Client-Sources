//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.combat;

import java.awt.Color;
import java.util.Objects;
import me.independed.inceptice.modules.Module;
import me.independed.inceptice.settings.BooleanSetting;
import me.independed.inceptice.settings.NumberSetting;
import me.independed.inceptice.settings.Setting;
import me.independed.inceptice.util.RotationUtils;
import me.independed.inceptice.util.TimerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import org.lwjgl.opengl.GL11;

public class Fraerok5 extends Module {
      public static int direction = (726870812 >>> 3 >>> 4 | 3393815) ^ -7860056;
      public NumberSetting Increase;
      public NumberSetting distance;
      public BooleanSetting renderCircle;
      public TimerUtil timerUtil;
      public double increment;
      public NumberSetting speedRot;
      public boolean sideDirection;
      public BooleanSetting Increasable;

      public static boolean canStrafe() {
            return (boolean)((0 >>> 3 | 326153922) ^ 326153923);
      }

      public Fraerok5() {
            if (((8585354 ^ 2349375 | 3240874) ^ 11664319) == 0) {
                  ;
            }

            super("TargetStrafe", "targets closest player. note: use with kill_aura", ((298785566 | 92175557) << 1 ^ 665273 | 521453246) ^ 1073020863, Module.Category.COMBAT);
            this.distance = new NumberSetting("Radius", this, 2.0D, 0.0D, 20.0D, 0.1D);
            NumberSetting var10001 = new NumberSetting;
            if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("ape covered in human flesh")) {
                  ;
            }

            var10001.<init>("Speed", this, 4.0D, 1.0D, 100.0D, 1.0D);
            this.speedRot = var10001;
            BooleanSetting var1 = new BooleanSetting;
            int var10005 = (0 >> 3 ^ 1490301216) >> 3 ^ 186287653;
            if (!"Some babies were dropped on their heads but you were clearly thrown at a wall.".equals("yo mama name maurice")) {
                  ;
            }

            var1.<init>("Render", this, (boolean)var10005);
            this.renderCircle = var1;
            this.Increase = new NumberSetting("Increase", this, 50.0D, 0.0D, 150.0D, 1.0D);
            this.Increasable = new BooleanSetting("Increasing", this, (boolean)((0 & 310520360 | 1228330835) & 223074862 ^ 151179779));
            this.timerUtil = new TimerUtil();
            this.increment = 0.05D;
            this.sideDirection = (boolean)((0 & 649307169) >>> 4 << 1 ^ 1);
            Setting[] var2 = new Setting[(3 & 0 ^ 577280751 ^ 320896273) >> 3 ^ 103357690];
            if (((38125314 ^ 20949097) >> 2 & 9245695 ^ 1237691283) != 0) {
                  ;
            }

            var2[(49919937 << 3 >>> 3 ^ 24474870) >>> 4 ^ 3722291] = this.renderCircle;
            var2[0 >>> 3 << 4 >>> 3 >> 2 ^ 1] = this.speedRot;
            var2[1 >> 2 << 3 << 2 ^ 2] = this.distance;
            var2[(((2 | 0) ^ 1) & 2) >> 3 ^ 3] = this.Increasable;
            var2[(2 << 4 & 26 | 1664342254) >> 1 ^ 832171123] = this.Increase;
            this.addSettings(var2);
      }

      public static double getMovementSpeed() {
            double var0 = 0.2873D;
            if ((((910029808 | 103855356) >>> 1 | 321533382) >>> 2 ^ 1381455871) != 0) {
                  ;
            }

            if (Minecraft.getMinecraft().player.isPotionActive((Potion)Objects.requireNonNull(Potion.getPotionById((0 & 360032599) >> 2 ^ 1)))) {
                  int var2 = ((PotionEffect)Objects.requireNonNull(Minecraft.getMinecraft().player.getActivePotionEffect((Potion)Objects.requireNonNull(Potion.getPotionById((0 & 382298830 | 1878226286) << 3 >> 3 ^ 267613551))))).getAmplifier();
                  var0 *= 1.0D + 0.2D * (double)(var2 + ((0 << 3 | 455678473) >> 3 ^ 56959808));
            }

            if (((1418195963 >> 4 ^ 22501316) & 16320098 ^ 1850426628) != 0) {
                  ;
            }

            return var0;
      }

      private void drawRadius(Entity var1, double var2, double var4, double var6, double var8) {
            float var10 = 90.0F;
            GlStateManager.enableDepth();

            label96:
            for(double var11 = 0.0D; var11 < 0.01D; var11 += 0.001D) {
                  GL11.glPushMatrix();
                  GL11.glDisable((3208 >>> 4 | 20) ^ 119 ^ 3402);
                  GL11.glEnable(1010 >>> 4 & 1 & 0 ^ 2848);
                  GL11.glEnable(1441 >>> 4 & 76 ^ 2825);
                  GL11.glEnable(((922 << 3 | 5465) ^ 4842) >> 1 << 3 ^ 14296);
                  GL11.glEnable((1886 & 1769 ^ 427) >> 1 ^ 897 ^ 2962);
                  GL11.glDepthMask((boolean)(((1892634623 >>> 3 | 229655250) ^ 187045894) >> 1 ^ 181150 ^ 38615650));
                  GL11.glBlendFunc(702 & 312 ^ 24 ^ 802, 745 >>> 4 & 30 ^ 4 ^ 777);
                  GL11.glHint((2918 >> 4 & 10) << 1 ^ 3158, ((226 ^ 19) >> 3 ^ 12) >> 2 ^ 4358);
                  GL11.glHint((3112 >>> 4 | 172 | 192 | 236) ^ 3261, (333 >> 2 ^ 30 ^ 10) << 3 ^ 4922);
                  int var10000 = ((1146 | 354) ^ 886) << 2 << 2 ^ 27793;
                  if (!"please go outside".equals("You're so fat whenever you go to the beach the tide comes in.")) {
                        ;
                  }

                  GL11.glHint(var10000, (4091 & 994 & 119) >> 2 << 3 ^ 4546);
                  GL11.glDisable(923 & 685 & 11 & 5 ^ 2928);
                  GL11.glLineWidth(4.0F);
                  GL11.glBegin((0 & 1076149474) << 3 << 3 >> 4 ^ 3);
                  double var13 = 6.283185307179586D;
                  float var15 = 1000.0F;
                  if ((590349818 >> 2 >>> 2 ^ 36896863) == 0) {
                        ;
                  }

                  float var16 = (float)(System.currentTimeMillis() % (long)((int)var15));

                  while(true) {
                        if (((106494762 << 1 | 2904707) << 1 ^ 391644413 ^ 1502963456) != 0) {
                              ;
                        }

                        if (var16 <= var15) {
                              float var23 = var16 / var15;
                              if ((((1250956357 ^ 779700725) & 252976355) >>> 2 << 4 ^ 268464768) == 0) {
                                    ;
                              }

                              var16 = var23;
                              int var17 = (746762142 >>> 1 | 268408240) << 2 ^ 2147442684;

                              while(true) {
                                    if (!"idiot".equals("stringer is a good obfuscator")) {
                                          ;
                                    }

                                    if (var17 > (77 >> 2 << 2 << 2 ^ 362)) {
                                          GL11.glEnd();
                                          GL11.glDepthMask((boolean)((0 | 211523273) ^ 124515593 ^ 200303553));
                                          GL11.glEnable(846 >> 4 >>> 3 >>> 4 ^ 2929);
                                          GL11.glDisable((873 | 828 | 762) ^ 2271);
                                          GL11.glDisable((137 << 4 ^ 1247) >>> 1 << 1 ^ 1807);
                                          GL11.glEnable((866 & 686) >>> 4 ^ 2866);
                                          var10000 = (2907 << 4 | 12880) ^ '먑';
                                          if ((269925226 >>> 4 << 4 ^ -488871968) != 0) {
                                                ;
                                          }

                                          GL11.glEnable(var10000);
                                          GL11.glPopMatrix();
                                          GlStateManager.color(255.0F, 255.0F, 255.0F);
                                          continue label96;
                                    }

                                    var23 = (float)var17;
                                    double var10001 = var11 * 8.0D;
                                    if ((((134879474 ^ 19338872 | 12058231) ^ 146894640 | 10454284) ^ 470411077) != 0) {
                                          ;
                                    }

                                    float var18 = (var23 + (float)var10001) / var10;
                                    float var19 = var18 + var16;

                                    while(var19 > 1.0F) {
                                          --var19;
                                          if (!"stop skidding".equals("idiot")) {
                                                ;
                                          }
                                    }

                                    float var25 = (float)(new Color(Color.HSBtoRGB(var19, 0.75F, 1.0F))).getRed();
                                    if (((2102087387 ^ 1572119304) << 3 ^ 134180504) == 0) {
                                          ;
                                    }

                                    float var20 = 0.003921569F * var25;
                                    if (((489207386 >> 1 >>> 3 | 27641595) ^ -990817173) != 0) {
                                          ;
                                    }

                                    float var21 = 0.003921569F * (float)(new Color(Color.HSBtoRGB(var19, 0.75F, 1.0F))).getGreen();
                                    if ((1576848412 << 3 << 3 >> 2 & 422585906 ^ -1779402333) != 0) {
                                          ;
                                    }

                                    float var22 = 0.003921569F * (float)(new Color(Color.HSBtoRGB(var19, 0.75F, 1.0F))).getBlue();
                                    GL11.glColor3f(var20, var21, var22);
                                    if ((((1852874057 >>> 3 | 218450314) & 30684903) >>> 2 >>> 4 ^ 462922) == 0) {
                                          ;
                                    }

                                    if ((((2145164426 ^ 848476865) >>> 4 & 25268046) >>> 1 ^ 4210978) == 0) {
                                          ;
                                    }

                                    double var10002 = (double)var17 * 6.283185307179586D;
                                    if (!"buy a domain and everything else you need at namecheap.com".equals("please take a shower")) {
                                          ;
                                    }

                                    double var24 = var2 + var8 * Math.cos(var10002 / (double)var10);
                                    var10001 = var4 + var11;
                                    double var10003 = var8 * Math.sin((double)var17 * 6.283185307179586D / (double)var10);
                                    if ((((336286386 >>> 3 ^ 31883258) & 50153530) << 3 ^ -1977747297) != 0) {
                                          ;
                                    }

                                    GL11.glVertex3d(var24, var10001, var6 + var10003);
                                    ++var17;
                              }
                        }

                        var16 -= var15;
                  }
            }

      }

      private void invertStrafe() {
            direction = -direction;
            if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("nefariousMoment")) {
                  ;
            }

      }

      public final boolean doStrafeAtSpeed(double var1) {
            boolean var3 = Fraerok5.canStrafe();
            if (var3) {
                  EntityLivingBase var4 = null;
                  if (Fraerok2.currentTarget != null) {
                        EntityLivingBase var10000 = Fraerok2.currentTarget;
                        if (((542561842 >> 2 ^ 105655270) & 238927921 ^ -2015136097) != 0) {
                              ;
                        }

                        var4 = var10000;
                  }

                  if (var4 != null) {
                        EntityPlayer var7 = (EntityPlayer)var4;
                        if (!"please go outside".equals("intentMoment")) {
                              ;
                        }

                        if (var7.getHealth() > 0.0F && !var4.isDead && mc.player.getDistance(var4) < 7.0F) {
                              boolean var8 = mc.player.onGround;
                              if (!"please take a shower".equals("stop skidding")) {
                                    ;
                              }

                              if (var8) {
                                    mc.player.jump();
                              }

                              AxisAlignedBB var5 = var4.getEntityBoundingBox();
                              float[] var6 = (float[])RotationUtils.getNeededRotations(RotationUtils.getRandomCenter(var5, (boolean)((467062227 << 3 | 1997503148) & 1641136345 ^ 1636909208)), (boolean)(0 >>> 3 ^ 183803491 ^ 183803490));
                              double var10;
                              float var10001;
                              if ((double)Minecraft.getMinecraft().player.getDistance(var4) <= this.distance.getValue()) {
                                    Minecraft var9 = mc;
                                    if (((159598514 | 2254599) >> 4 ^ -1952406790) != 0) {
                                          ;
                                    }

                                    var9.player.renderYawOffset = var6[426240 << 4 ^ 6819840];
                                    mc.player.rotationYawHead = var6[9089488 ^ 7792936 ^ 16537848];
                                    if (mc.player.hurtTime > 0 && this.Increasable.isEnabled()) {
                                          Fraerok5.setSpeed(var1 - (0.1D - (this.speedRot.getValue() + this.Increase.getValue()) / 100.0D), var6[1408741132 << 4 >>> 4 ^ 66563852], (double)direction, 0.0D);
                                    } else {
                                          var10 = var1 - (0.1D - this.speedRot.getValue() / 100.0D);
                                          var10001 = var6[1156726008 << 1 >> 3 ^ 1058521927 ^ -836023175];
                                          if (((1688472126 ^ 997452935 | 101340033) >>> 4 ^ -846156994) != 0) {
                                                ;
                                          }

                                          Fraerok5.setSpeed(var10, var10001, (double)direction, 0.0D);
                                    }
                              } else {
                                    EntityPlayerSP var11 = mc.player;
                                    if (((1902576525 >> 4 >>> 3 & 12627396) >> 2 ^ 3154689) == 0) {
                                          ;
                                    }

                                    label105: {
                                          if (var11.hurtTime > 0) {
                                                var8 = this.Increasable.isEnabled();
                                                if (((1599388736 >> 2 & 199238535 | 60331043) ^ -2092957342) != 0) {
                                                      ;
                                                }

                                                if (var8) {
                                                      if (!"your mom your dad the one you never had".equals("i hope you catch fire ngl")) {
                                                            ;
                                                      }

                                                      double var10002 = this.speedRot.getValue();
                                                      NumberSetting var10003 = this.Increase;
                                                      if (!"stop skidding".equals("You're so fat whenever you go to the beach the tide comes in.")) {
                                                            ;
                                                      }

                                                      var10 = var1 - (0.1D - (var10002 + var10003.getValue()) / 100.0D);
                                                      var10001 = var6[(775043323 << 3 ^ 1690682068) >> 3 ^ 44728481];
                                                      var10002 = (double)direction;
                                                      if ((((1735493057 ^ 1733957078) >> 2 | 521797) ^ 1046085) == 0) {
                                                            ;
                                                      }

                                                      Fraerok5.setSpeed(var10, var10001, var10002, 1.0D);
                                                      break label105;
                                                }
                                          }

                                          if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("your mom your dad the one you never had")) {
                                                ;
                                          }

                                          Fraerok5.setSpeed(var1 - (0.1D - this.speedRot.getValue() / 100.0D), var6[((323841424 | 141779343) ^ 283690508) & 11341071 ^ 8718595], (double)direction, 1.0D);
                                    }

                                    var11 = mc.player;
                                    var10001 = var6[((466881219 ^ 389296350) << 2 ^ 56502822) >>> 4 ^ 51244069];
                                    if (((87111997 << 2 >>> 3 | 42670496) >>> 3 >> 2 ^ 889259245) != 0) {
                                          ;
                                    }

                                    var11.renderYawOffset = var10001;
                                    var11 = mc.player;
                                    if (((713983841 ^ 215118175 ^ 460173914) >>> 3 ^ 128329868) == 0) {
                                          ;
                                    }

                                    var11.rotationYawHead = var6[(1197238828 ^ 308764606) << 4 >> 3 >>> 4 << 1 ^ 21941604];
                              }
                        }
                  }
            }

            if (((431239108 | 386788833) >>> 2 ^ 133135353) == 0) {
                  ;
            }

            return var3;
      }

      public static void setSpeed(double var0, float var2, double var3, double var5) {
            double var7 = var5;
            double var9 = var3;
            float var11 = var2;
            if (var5 == 0.0D) {
                  double var16;
                  int var10000 = (var16 = var3 - 0.0D) == 0.0D ? 0 : (var16 < 0.0D ? -1 : 1);
                  if ((((2027238401 >>> 3 ^ 134116596) & 135565617) << 1 ^ 269033568) == 0) {
                        ;
                  }

                  if (var10000 == 0) {
                        mc.player.motionZ = 0.0D;
                        mc.player.motionX = 0.0D;
                        return;
                  }
            }

            if (var5 != 0.0D) {
                  if (var3 > 0.0D) {
                        var11 = var2 + (float)(var5 > 0.0D ? 769395114 >> 2 >> 1 ^ -96174362 : (8 ^ 5) >> 2 >>> 3 ^ 45);
                  } else if (var3 < 0.0D) {
                        var11 = var2 + (float)(var5 > 0.0D ? (38 >>> 1 ^ 10 | 13) >> 4 << 3 ^ 37 : (1479604165 >>> 3 ^ 82956730) >>> 2 & 32237722 & 9197568 ^ -8929325);
                        if (!"You're so fat whenever you go to the beach the tide comes in.".equals("please take a shower")) {
                              ;
                        }
                  }

                  var9 = 0.0D;
                  if (var5 > 0.0D) {
                        if (!"i hope you catch fire ngl".equals("please dont crack my plugin")) {
                              ;
                        }

                        var7 = 1.0D;
                  } else if (var5 < 0.0D) {
                        var7 = -1.0D;
                  }
            }

            if (((928262474 >>> 3 >>> 4 | 4256094) >> 3 ^ 909099) == 0) {
                  ;
            }

            double var12 = Math.cos(Math.toRadians((double)(var11 + 90.0F)));
            double var14 = Math.sin(Math.toRadians((double)(var11 + 90.0F)));
            mc.player.motionX = var7 * var0 * var12 + var9 * var0 * var14;
            if (((1898236783 >> 4 | 68147667 | 58071045) ^ 125820407) == 0) {
                  ;
            }

            mc.player.motionZ = var7 * var0 * var14 - var9 * var0 * var12;
      }

      public void onRenderWorldLast(float var1) {
            if (mc.player != null && !mc.player.isDead) {
                  if (!"please get a girlfriend and stop cracking plugins".equals("intentMoment")) {
                        ;
                  }

                  if ((double)mc.player.getHealth() > 0.0D) {
                        if (this.renderCircle.isEnabled() && Fraerok2.currentTarget != null) {
                              float var10000 = Fraerok2.currentTarget.getHealth();
                              if ((261124137 >> 4 >> 3 >> 4 ^ -299582331) != 0) {
                                    ;
                              }

                              if (var10000 > 0.0F) {
                                    double var11 = this.distance.getValue();
                                    if ((1593350313 >> 2 >> 3 ^ -295688051) != 0) {
                                          ;
                                    }

                                    double var2 = var11;
                                    EntityLivingBase var4 = Fraerok2.currentTarget;
                                    var11 = var4.lastTickPosX;
                                    double var10001 = var4.posX - var4.lastTickPosX;
                                    double var10002 = (double)var1;
                                    if ((1947393564 >> 2 >>> 4 << 4 ^ 486848384) == 0) {
                                          ;
                                    }

                                    var11 += var10001 * var10002;
                                    var10001 = mc.getRenderManager().viewerPosX;
                                    if ((((1836736619 | 240620859) >>> 3 & 76903920) << 2 ^ 303420032) == 0) {
                                          ;
                                    }

                                    double var5 = var11 - var10001;
                                    double var7 = var4.lastTickPosY + (var4.posY - var4.lastTickPosY) * (double)var1 - mc.getRenderManager().viewerPosY;
                                    if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("yo mama name maurice")) {
                                          ;
                                    }

                                    var11 = var4.lastTickPosZ + (var4.posZ - var4.lastTickPosZ) * (double)var1;
                                    var10001 = mc.getRenderManager().viewerPosZ;
                                    if ((603586872 << 2 << 1 ^ 1532965871) != 0) {
                                          ;
                                    }

                                    double var9 = var11 - var10001;
                                    if (this.increment < 2.03D && this.sideDirection) {
                                          if (((4296705 >>> 3 | 528539) ^ -1399607758) != 0) {
                                                ;
                                          }

                                          if ((210392585 << 1 >> 1 ^ 1839995596) != 0) {
                                                ;
                                          }

                                          var11 = this.increment;
                                          if ((269991516 << 2 << 1 >> 4 ^ -133439698) == 0) {
                                                ;
                                          }

                                          if (var11 >= 2.0D) {
                                                this.sideDirection = (boolean)((37487640 ^ 21662739) << 4 ^ 277516551 ^ 669127095);
                                                this.increment = 2.0D;
                                                var7 -= this.increment;
                                          }

                                          if (((1236762396 >>> 1 ^ 557431910) << 1 ^ 9056087 ^ 1830180995) != 0) {
                                                ;
                                          }

                                          var11 = var7 + this.increment;
                                          if ((1540895413 >>> 4 >> 1 ^ 48152981) == 0) {
                                                ;
                                          }

                                          var7 = var11;
                                          var10001 = this.increment;
                                          if (!"you probably spell youre as your".equals("you probably spell youre as your")) {
                                                ;
                                          }

                                          this.increment = var10001 + 0.02D;
                                          this.drawRadius(Fraerok2.currentTarget, var5, var7, var9, var2);
                                    }

                                    if (this.increment > 0.01D) {
                                          boolean var12 = this.sideDirection;
                                          if ((1029508213 >> 3 << 2 ^ 514754104) == 0) {
                                                ;
                                          }

                                          if (!var12) {
                                                if (this.increment <= 0.02D) {
                                                      this.sideDirection = (boolean)(0 << 3 ^ 1101718626 ^ 414254787 ^ 1494999712);
                                                      this.increment = 0.01D;
                                                      if ((1176098214 << 4 & 963728790 ^ -71563248) != 0) {
                                                            ;
                                                      }
                                                }

                                                var7 += this.increment;
                                                if (((35654416 ^ 35326375) << 1 >> 4 ^ -1971710996) != 0) {
                                                      ;
                                                }

                                                this.increment -= 0.02D;
                                                this.drawRadius(Fraerok2.currentTarget, var5, var7, var9, var2);
                                          }
                                    }
                              }
                        }

                        if (((304257112 << 3 ^ 1614712645) << 3 ^ -1991087064) == 0) {
                              ;
                        }

                        return;
                  }
            }

      }

      @SubscribeEvent
      public void onPlayerTick(PlayerTickEvent var1) {
            if (mc.player != null) {
                  if (mc.player.collidedHorizontally) {
                        if ((1022369873 >>> 1 >>> 4 ^ -1697030014) != 0) {
                              ;
                        }

                        if (this.timerUtil.hasReached(150.0D)) {
                              this.timerUtil.reset();
                              this.invertStrafe();
                        }
                  }

                  if (Fraerok5.canStrafe()) {
                        mc.player.movementInput.moveForward = 0.0F;
                  }

                  double var2 = Fraerok5.getMovementSpeed();
                  if (((796291119 ^ 217917045) & 410884893 ^ 594456) == 0) {
                        ;
                  }

                  this.doStrafeAtSpeed(var2);
            }
      }
}
