//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.combat;

import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import me.independed.inceptice.modules.Module;
import me.independed.inceptice.util.RotationUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraftforge.client.event.EntityViewRenderEvent.CameraSetup;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PVPBot extends Module {
      private Random random;
      private int hitDelayTimer;
      private long curTimeRotate;
      private int rotateTimer;
      private double posX;
      private long curTimeHit;

      @SideOnly(Side.SERVER)
      @SubscribeEvent
      public void onCameraSetup(CameraSetup var1) {
            if (mc.player != null && !mc.player.isDead) {
                  Minecraft var10000 = mc;
                  if (((1065383134 | 670575388) >>> 1 >>> 3 ^ 32329553 ^ 34785452) == 0) {
                        ;
                  }

                  WorldClient var6 = var10000.world;
                  if ((1177657009 >> 3 >> 4 >> 3 ^ 1150055) == 0) {
                        ;
                  }

                  List var7 = var6.loadedEntityList;
                  if ((748296 >>> 1 >>> 4 ^ -470770924) != 0) {
                        ;
                  }

                  Stream var8 = var7.stream().filter((var0) -> {
                        return (boolean)(var0 != mc.player ? (0 & 1493180436) >>> 1 >> 3 & 1503104053 ^ 1 : 2071250966 >> 3 >> 4 ^ 16181648);
                  });
                  if (((1065856 ^ 725629) >> 2 ^ 315622198) != 0) {
                        ;
                  }

                  var8 = var8.filter((var0) -> {
                        EntityPlayerSP var10000 = mc.player;
                        if (!"please go outside".equals("please get a girlfriend and stop cracking plugins")) {
                              ;
                        }

                        if (((580226323 >>> 4 | 35370680 | 28894987) ^ 62652411) == 0) {
                              ;
                        }

                        float var1 = var10000.getDistance(var0);
                        if (!"you probably spell youre as your".equals("yo mama name maurice")) {
                              ;
                        }

                        double var3;
                        int var2 = (var3 = (double)var1 - 3.5D) == 0.0D ? 0 : (var3 < 0.0D ? -1 : 1);
                        if ((144194422 << 4 << 2 >>> 1 ^ 319254208) == 0) {
                              ;
                        }

                        return (boolean)(var2 <= 0 ? (0 | 1804498573) & 305969847 ^ 8682834 ^ 42487766 : (1831528463 ^ 776781319) << 1 ^ -2033406960);
                  }).filter((var0) -> {
                        int var10000 = !var0.isDead ? (0 | 388501171) & 279063376 ^ 138985215 ^ 409515246 : 66048 << 4 << 1 ^ 2113536;
                        if ((1803877350 >>> 3 << 1 ^ 450969336) == 0) {
                              ;
                        }

                        return (boolean)var10000;
                  });
                  Predicate var10001 = this::attackCheck;
                  if ((50862208 >> 3 ^ 159442 ^ 6516162) == 0) {
                        ;
                  }

                  if (((56022077 >>> 2 ^ 10083350 ^ 2448714) << 3 ^ 55157400) == 0) {
                        ;
                  }

                  List var2 = (List)var8.filter(var10001).filter((var0) -> {
                        if ((((1047740806 ^ 720505180) & 26588204 ^ 3125505 | 10690521) << 1 ^ -795012066) != 0) {
                              ;
                        }

                        int var10000;
                        if (!(var0 instanceof EntityArmorStand)) {
                              var10000 = (0 >> 4 ^ 285719657) & 51931402 ^ 16787465;
                        } else {
                              var10000 = (1976134985 >>> 2 | 175450437) ^ 527924055;
                              if (!"stop skidding".equals("please dont crack my plugin")) {
                                    ;
                              }
                        }

                        return (boolean)var10000;
                  }).sorted(Comparator.comparing((var0) -> {
                        return Float.valueOf(mc.player.getDistance(var0));
                  })).collect(Collectors.toList());
                  if (var2.size() > 0) {
                        int var9 = (762903426 >>> 4 | 3876308) ^ 50311164;
                        if (((649630552 >>> 2 << 3 ^ 1180817916) & 100361237 ^ 17851396) == 0) {
                              ;
                        }

                        float[] var3 = (float[])PVPBot.getRotations((EntityLivingBase)var2.get(var9));
                        var9 = 2448384 >> 2 << 1 ^ 1224192;
                        float var10002 = var3[2448384 >> 2 << 1 ^ 1224192];
                        if ((5654347 >> 2 >> 4 ^ 68829 ^ 662939470) != 0) {
                              ;
                        }

                        int var10003 = this.random.nextInt(9 << 4 << 1 & 173 & 7 ^ 30);
                        if (((76326652 ^ 12280557) & 59192972 ^ 78617224) != 0) {
                              ;
                        }

                        var3[var9] = var10002 + (float)var10003 * 0.1F;
                        var3[(0 & 2120405478 | 523882306) & 148885045 ^ 135908865] += (float)this.random.nextInt((51 | 29) << 1 & 59 ^ 6) * 0.1F;
                        float var4 = var3[401367249 >> 3 >>> 3 >>> 4 << 3 << 4 ^ 50170880] - 180.0F;
                        float var5 = var3[(0 | 1622941729) >>> 3 ^ 202867717];
                        EntityPlayerSP var10 = mc.player;
                        if ((1240932707 >> 1 << 3 ^ 2029270366) != 0) {
                              ;
                        }

                        var10.renderYawOffset = var4 - 180.0F;
                        if (!"idiot".equals("your mom your dad the one you never had")) {
                              ;
                        }

                        mc.player.rotationYawHead = var4 - 180.0F;
                        if ((124831242 >> 3 << 2 ^ 62415620) == 0) {
                              ;
                        }

                        float var11;
                        if (var4 >= 0.0F) {
                              if (var1.getYaw() < var4) {
                                    while(true) {
                                          if (!"Some babies were dropped on their heads but you were clearly thrown at a wall.".equals("please dont crack my plugin")) {
                                                ;
                                          }

                                          if (var1.getYaw() >= var4) {
                                                break;
                                          }

                                          var1.setYaw(var1.getYaw() + (float)this.random.nextInt((64 | 7) & 40 ^ 99) * 0.001F);
                                    }
                              } else {
                                    for(; var1.getYaw() > var4; var1.setYaw(var1.getYaw() - (float)this.random.nextInt(((63 & 44) >>> 3 | 1) ^ 102) * 0.001F)) {
                                          if ((1206931194 >>> 1 >> 2 << 1 ^ 10507770 ^ 291259972) == 0) {
                                                ;
                                          }
                                    }
                              }
                        } else {
                              if ((((1476913763 >>> 2 ^ 15260021) << 2 & 36521976 | 35309631) ^ -1330705965) != 0) {
                                    ;
                              }

                              if (var1.getYaw() < var4) {
                                    for(; var1.getYaw() < var4; var1.setYaw(var11)) {
                                          var11 = var1.getYaw();
                                          var10002 = (float)this.random.nextInt((17 | 5) ^ 17 ^ 103);
                                          if ((((215381916 ^ 67036023) & 40231589) << 3 ^ 285476104) == 0) {
                                                ;
                                          }

                                          if (((2123754815 | 952801364 | 22672241) >> 1 >>> 3 ^ 39980052 ^ 94368739) == 0) {
                                                ;
                                          }

                                          var11 += var10002 * 0.001F;
                                          if (((969805906 << 4 | 377061596) & 570346331 ^ 16286040) == 0) {
                                                ;
                                          }
                                    }
                              } else {
                                    while(var1.getYaw() > var4) {
                                          var1.setYaw(var1.getYaw() - (float)this.random.nextInt((66 >> 1 & 29) >>> 4 ^ 354002104 ^ 354002139) * 0.001F);
                                    }
                              }
                        }

                        if (var5 >= 0.0F) {
                              int var14;
                              if (var1.getPitch() >= var5) {
                                    for(; var1.getPitch() > var5; var1.setPitch(var11 - (float)var14 * 0.001F)) {
                                          if ((64253326 << 2 >>> 3 ^ 32126663) == 0) {
                                                ;
                                          }

                                          var11 = var1.getPitch();
                                          var14 = this.random.nextInt(1 << 2 << 2 << 2 >> 2 ^ 115);
                                          if ((939738073 >> 4 << 4 >>> 1 ^ 469869032) == 0) {
                                                ;
                                          }
                                    }
                              } else {
                                    while(true) {
                                          if ("Some babies were dropped on their heads but you were clearly thrown at a wall.".equals("nefariousMoment")) {
                                          }

                                          float var15;
                                          int var12 = (var15 = var1.getPitch() - var5) == 0.0F ? 0 : (var15 < 0.0F ? -1 : 1);
                                          if (((837967661 >>> 1 ^ 189385623) & 212789819 & 1967952 ^ 0) == 0) {
                                                ;
                                          }

                                          if (var12 >= 0) {
                                                break;
                                          }

                                          var11 = var1.getPitch();
                                          Random var13 = this.random;
                                          if ((1406520998 >> 4 << 3 & 432877035 ^ 1570639097) != 0) {
                                                ;
                                          }

                                          var10003 = (79 | 44) >>> 3 ^ 110;
                                          if (!"you're dogshit".equals("your mom your dad the one you never had")) {
                                                ;
                                          }

                                          var1.setPitch(var11 + (float)var13.nextInt(var10003) * 0.001F);
                                    }
                              }
                        } else if (var1.getPitch() < var5) {
                              for(; var1.getPitch() < var5; var1.setPitch(var1.getPitch() + (float)this.random.nextInt((0 << 1 >>> 1 | 367079463) >>> 4 ^ 22942561) * 0.001F)) {
                                    if ((2099473 >>> 1 & 336467 ^ 0) == 0) {
                                          ;
                                    }
                              }
                        } else {
                              while(var1.getPitch() > var5) {
                                    var1.setPitch(var1.getPitch() - (float)this.random.nextInt(49 >>> 4 << 1 >> 1 ^ 96) * 0.001F);
                              }
                        }
                  }

            }
      }

      public boolean attackCheck(Entity var1) {
            if (var1 instanceof EntityPlayer && ((EntityPlayer)var1).getHealth() > 0.0F) {
                  if (!"stringer is a good obfuscator".equals("please get a girlfriend and stop cracking plugins")) {
                        ;
                  }

                  if (Math.abs(mc.player.rotationYaw - ((float[])RotationUtils.getFuckedUpRotations((EntityLivingBase)var1))[691260181 ^ 255793290 ^ 458649050 ^ 710947003 ^ 389713662]) % 180.0F < 190.0F && !var1.isInvisible() && !var1.getUniqueID().equals(mc.player.getUniqueID())) {
                        return (boolean)(0 >>> 1 >> 1 >> 1 ^ 1);
                  }
            }

            return (boolean)((576709343 >>> 3 >>> 3 ^ 5513537) >>> 1 ^ 7253861);
      }

      public PVPBot() {
            if (!"intentMoment".equals("yo mama name maurice")) {
                  ;
            }

            super("PVPBot", "automatically pvp", (1095885915 >>> 2 | 223171218) >> 2 >>> 2 ^ 12675563 ^ 18136658, Module.Category.COMBAT);
            this.random = new Random();
            if ((26756224 >>> 4 << 3 ^ 13378112) == 0) {
                  ;
            }

            int var10001 = (418 & 104 ^ 3 ^ 23) >> 3 ^ 631;
            if (((425594591 >>> 4 & 21696018 | 7807511 | 5859839) >> 1 ^ 1345075905) != 0) {
                  ;
            }

            this.hitDelayTimer = var10001;
            if ((68198528 ^ 68198528) == 0) {
                  ;
            }

            this.rotateTimer = ((880 << 1 ^ 1524) >>> 1 | 126) & 334 ^ 1694;
            this.curTimeHit = System.currentTimeMillis();
            this.curTimeRotate = System.currentTimeMillis();
      }

      public void onEnable() {
            if (mc.player != null) {
                  super.onEnable();
                  if ((783666867 >> 1 & 58335309 ^ 56238153) == 0) {
                        ;
                  }

                  this.posX = mc.player.posX;
            }
      }

      public static float[] getRotations(Entity var0) {
            double var10000 = var0.posX;
            double var10001 = var0.posX;
            if (!"you probably spell youre as your".equals("buy a domain and everything else you need at namecheap.com")) {
                  ;
            }

            double var1 = var10000 + (var10001 - var0.lastTickPosX) - mc.player.posX;
            if (!"please take a shower".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                  ;
            }

            var10000 = var0.posY + (double)var0.getEyeHeight();
            EntityPlayerSP var12 = mc.player;
            if (((1642164253 >>> 2 & 404229158) >>> 4 << 3 ^ -2063512251) != 0) {
                  ;
            }

            double var3 = var10000 - var12.posY + (double)mc.player.getEyeHeight() - 3.5D;
            double var5 = var0.posZ + (var0.posZ - var0.lastTickPosZ) - mc.player.posZ;
            double var7 = Math.sqrt(Math.pow(var1, 2.0D) + Math.pow(var5, 2.0D));
            if ((625498129 >>> 1 << 4 >>> 1 >> 1 ^ 177254432) == 0) {
                  ;
            }

            float var9 = (float)Math.toDegrees(-Math.atan(var1 / var5));
            float var10 = (float)(-Math.toDegrees(Math.atan(var3 / var7)));
            if ((1700938705 >> 3 << 3 >>> 1 >>> 2 ^ 212617338) == 0) {
                  ;
            }

            if (var1 < 0.0D && var5 < 0.0D) {
                  var9 = (float)(90.0D + Math.toDegrees(Math.atan(var5 / var1)));
            } else if (var1 > 0.0D) {
                  if (((40718370 | 9132215) ^ 27743332 ^ 55053523) == 0) {
                        ;
                  }

                  if (var5 < 0.0D) {
                        var10000 = -90.0D + Math.toDegrees(Math.atan(var5 / var1));
                        if ((1555210693 << 2 << 3 ^ 35068140) != 0) {
                              ;
                        }

                        var9 = (float)var10000;
                  }
            }

            float[] var11 = new float[(1 << 2 & 3) >>> 3 ^ 2];
            int var10002 = ((2144540607 >>> 3 | 104814855) & 34174141) << 3 ^ 273384872;
            if (!"You're so fat whenever you go to the beach the tide comes in.".equals("You're so fat whenever you go to the beach the tide comes in.")) {
                  ;
            }

            var11[var10002] = var9;
            var11[(0 >>> 4 | 603259989) ^ 603259988] = var10;
            return var11;
      }

      private void setRotation(float var1, float var2, EntityPlayer var3) {
            mc.player.renderYawOffset = var1;
            mc.player.rotationYawHead = var1;
            EntityPlayerSP var4;
            if (var1 >= 0.0F) {
                  float var10001;
                  if (mc.player.rotationYaw < var1) {
                        for(; mc.player.rotationYaw < var1; var4.rotationYaw = (float)((double)var10001 + (double)this.random.nextInt((2 | 0) >>> 1 << 2 ^ 103) * 1.0E-4D)) {
                              var4 = mc.player;
                              var10001 = var4.rotationYaw;
                              if (!"idiot".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                                    ;
                              }
                        }
                  } else {
                        while(mc.player.rotationYaw > var1) {
                              Minecraft var10000 = mc;
                              if ((96505889 >>> 3 ^ 745367 ^ 11750803) == 0) {
                                    ;
                              }

                              var4 = var10000.player;
                              var4.rotationYaw = (float)((double)var4.rotationYaw - (double)this.random.nextInt(89 << 2 << 4 ^ 5667) * 1.0E-4D);
                        }
                  }
            } else {
                  if (!"stop skidding".equals("i hope you catch fire ngl")) {
                        ;
                  }

                  if ((493342409 >>> 1 >>> 4 ^ 1994942293) != 0) {
                        ;
                  }

                  if (mc.player.rotationYaw < var1) {
                        for(; mc.player.rotationYaw < var1; var4.rotationYaw = (float)((double)var4.rotationYaw + (double)this.random.nextInt((77 & 68) >> 3 & 4 ^ 99) * 1.0E-4D)) {
                              var4 = mc.player;
                              if (((87130868 >>> 2 >> 1 | 3337776) ^ 982634104) != 0) {
                                    ;
                              }

                              if ((2117341662 >> 1 >>> 1 >>> 3 ^ 66166926) == 0) {
                                    ;
                              }
                        }
                  } else {
                        while(true) {
                              float var7;
                              int var5 = (var7 = mc.player.rotationYaw - var1) == 0.0F ? 0 : (var7 < 0.0F ? -1 : 1);
                              if (((1015723144 | 32631999) & 987982143 ^ 954427455) == 0) {
                                    ;
                              }

                              if (var5 <= 0) {
                                    break;
                              }

                              var4 = mc.player;
                              double var6 = (double)var4.rotationYaw;
                              Random var10002 = this.random;
                              if (((1719678854 | 220200577) << 3 ^ 2097151032) == 0) {
                                    ;
                              }

                              var4.rotationYaw = (float)(var6 - (double)var10002.nextInt((81 >>> 3 & 0) >> 3 ^ 99) * 1.0E-4D);
                        }
                  }
            }

      }

      @SubscribeEvent
      public void onPlayerTick(PlayerTickEvent var1) {
            if (mc.player != null && !mc.player.isDead) {
                  KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), (boolean)((0 >> 3 & 404742437) >> 4 >>> 3 ^ 291512306 ^ 291512307));
                  boolean var10000 = mc.player.onGround;
                  if (!"ape covered in human flesh".equals("shitted on you harder than archybot")) {
                        ;
                  }

                  if (var10000) {
                        int var4 = mc.gameSettings.keyBindJump.getKeyCode();
                        int var10001 = ((0 << 1 ^ 1419845194) & 168279699) >>> 4 ^ 4577;
                        if (((572667157 ^ 206352498) >> 2 << 2 & 668059212 ^ 641762372) == 0) {
                              ;
                        }

                        KeyBinding.setKeyBindState(var4, (boolean)var10001);
                  }

                  if ((2063139912 >> 2 >> 1 ^ 257892489) == 0) {
                        ;
                  }

                  Stream var5 = mc.world.loadedEntityList.stream();
                  Predicate var6 = (var0) -> {
                        int var10000;
                        if (var0 != mc.player) {
                              var10000 = (0 | 723596541) << 3 << 2 ^ 1680252833;
                              if ((23070991 << 2 >>> 3 ^ 11535495) == 0) {
                                    ;
                              }
                        } else {
                              if (!"please get a girlfriend and stop cracking plugins".equals("you probably spell youre as your")) {
                                    ;
                              }

                              var10000 = 2031906537 >>> 2 ^ 103188767 ^ 409047717;
                        }

                        return (boolean)var10000;
                  };
                  if ((1474344837 >>> 4 >> 3 ^ 7820949 ^ 14194682) == 0) {
                        ;
                  }

                  var5 = var5.filter(var6).filter((var0) -> {
                        if ((84541449 ^ 9557951 ^ 16739723 ^ 1857906529) != 0) {
                              ;
                        }

                        EntityPlayerSP var10000 = mc.player;
                        if (((725242539 << 3 << 1 | 1841652669) & 1079200102 ^ 1795522349) != 0) {
                              ;
                        }

                        return (boolean)(var10000.getDistance(var0) <= 50.0F ? (0 | 343800564 | 76081644) ^ 213928094 ^ 406696803 : (1035169909 << 2 >> 1 | 668159814) ^ -528402);
                  }).filter((var0) -> {
                        int var10000;
                        if (!var0.isDead) {
                              var10000 = ((0 & 1149176661) << 3 & 1084037456) >>> 4 ^ 2039549163 ^ 2039549162;
                        } else {
                              if ((1103983457 << 4 ^ 268247956 ^ 58704931 ^ 274059687) == 0) {
                                    ;
                              }

                              var10000 = 41158912 >>> 2 >>> 4 ^ 643108;
                        }

                        return (boolean)var10000;
                  }).filter(this::attackCheck).filter((var0) -> {
                        return (boolean)(!(var0 instanceof EntityArmorStand) ? (0 | 39697192) >> 3 ^ 4962148 : (781150220 | 626304033) << 2 >> 3 << 4 ^ 2130665824);
                  });
                  if ((1227265942 >> 1 >>> 1 >> 3 << 1 ^ 76704120) == 0) {
                        ;
                  }

                  List var2 = (List)var5.sorted(Comparator.comparing((var0) -> {
                        return Float.valueOf(mc.player.getDistance(var0));
                  })).collect(Collectors.toList());
                  if (!"Some babies were dropped on their heads but you were clearly thrown at a wall.".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                        ;
                  }

                  if (var2.size() > 0) {
                        float[] var3 = (float[])PVPBot.getRotations((Entity)var2.get(97779722 ^ 64940121 ^ 101378131));
                        float var8;
                        if (mc.player.getDistance((Entity)var2.get(279764325 >> 3 << 3 << 4 ^ 181261824)) > 7.0F && System.currentTimeMillis() - this.curTimeRotate >= (long)this.rotateTimer) {
                              var8 = var3[(1544162400 >> 4 >>> 4 & 5768334 | 3798582) ^ 7992894];
                              if (((405978089 >>> 2 | 62766634) ^ 129875706) == 0) {
                                    ;
                              }

                              float var10002 = var3[((0 & 814430262 | 1143635036 | 207441846) ^ 1235859916) >>> 4 ^ 6122594];
                              if (!"nefariousMoment".equals("please take a shower")) {
                                    ;
                              }

                              int var10004 = (1881478667 ^ 593534852) >> 1 >>> 2 << 3 ^ 1397066120;
                              if (!"your mom your dad the one you never had".equals("please dont crack my plugin")) {
                                    ;
                              }

                              this.setRotation(var8, var10002, (EntityPlayer)var2.get(var10004));
                              this.curTimeRotate = System.currentTimeMillis();
                        }

                        double var7 = (double)mc.player.getDistance((Entity)var2.get(1082167509 << 1 & 585885209 ^ 73736));
                        if (!"shitted on you harder than archybot".equals("You're so fat whenever you go to the beach the tide comes in.")) {
                              ;
                        }

                        if (var7 <= 3.5D && System.currentTimeMillis() - this.curTimeHit >= (long)this.hitDelayTimer) {
                              var8 = var3[1315520199 >>> 2 & 204955347 ^ 1199761];
                              int var10003 = (0 ^ 1241781168) >> 4 >>> 4 >>> 3 ^ 144959 ^ 751292;
                              if (((957460584 >> 3 & 22513007) >> 1 >> 1 ^ 1944863982) != 0) {
                                    ;
                              }

                              this.setRotation(var8, var3[var10003], (EntityPlayer)var2.get((335937956 | 252193534) & 225636087 ^ 218245878));
                              mc.playerController.attackEntity(mc.player, (Entity)var2.get((((1237718615 ^ 284971245) & 287758977) >> 4 | 9436279 | 10642276) ^ 29359999));
                              mc.player.swingArm(EnumHand.MAIN_HAND);
                              this.curTimeHit = System.currentTimeMillis();
                              if (((80721632 | 11565972) ^ 10028824 ^ 190240544) != 0) {
                                    ;
                              }
                        }
                  }

                  if ((520616798 >>> 2 & 104994176 & 64834388 ^ 37748736) == 0) {
                        ;
                  }

            }
      }

      public void onDisable() {
            if (((((605321838 ^ 373296797) >>> 3 & 90225795) << 1 | 6162607) ^ 148838831) == 0) {
                  ;
            }

            Minecraft var10000 = mc;
            if (((164715699 << 1 >> 2 | 53670788) & 81945877 ^ 1296686497) != 0) {
                  ;
            }

            if (var10000.player != null) {
                  if (((1511784958 | 1138612317) >>> 1 >> 3 & 7515565 ^ -377616618) != 0) {
                        ;
                  }

                  super.onDisable();
                  KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), (boolean)((1711343610 ^ 1373390779) >> 3 << 1 ^ 234311696));
            }
      }
}
