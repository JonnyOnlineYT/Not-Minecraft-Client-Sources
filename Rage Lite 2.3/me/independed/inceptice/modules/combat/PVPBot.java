//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.combat;

import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import me.independed.inceptice.modules.Module;
import me.independed.inceptice.util.RotationUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
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
      private long curTimeRotate;
      private int rotateTimer;
      private int hitDelayTimer;
      private double posX;
      private Random random;
      private long curTimeHit;

      public PVPBot() {
            super("PVPBot", "automatically pvp", ((1541749109 ^ 912469690) << 4 | 1624327283) >>> 2 ^ 1044143932, Module.Category.COMBAT);
            Random var10001 = new Random;
            if (!"idiot".equals("please get a girlfriend and stop cracking plugins")) {
                  ;
            }

            var10001.<init>();
            this.random = var10001;
            if (((458791409 >>> 4 | 19477780) & 27664493 ^ 1483601642) != 0) {
                  ;
            }

            this.hitDelayTimer = ((101 >>> 1 ^ 4) >>> 1 & 13) << 2 ^ 597;
            this.rotateTimer = (1048 | 673) >>> 3 ^ 1799;
            this.curTimeHit = System.currentTimeMillis();
            if (((1231400184 << 1 | 239534914 | 1328417429) ^ -537961481) == 0) {
                  ;
            }

            this.curTimeRotate = System.currentTimeMillis();
      }

      public static float[] getRotations(Entity var0) {
            double var10000 = var0.posX;
            double var10001 = var0.posX;
            double var10002 = var0.lastTickPosX;
            if (((80937005 ^ 64087540 ^ 62319393) >>> 2 & 19437237 ^ 757651746) != 0) {
                  ;
            }

            double var1 = var10000 + (var10001 - var10002) - mc.player.posX;
            var10000 = var0.posY + (double)var0.getEyeHeight();
            if (((589321516 << 2 & 815096639) << 4 ^ 1945341542) != 0) {
                  ;
            }

            Minecraft var11 = mc;
            if (!"you probably spell youre as your".equals("please get a girlfriend and stop cracking plugins")) {
                  ;
            }

            var10000 = var10000 - var11.player.posY + (double)mc.player.getEyeHeight() - 3.5D;
            if (((1887085641 ^ 603294763) & 785171151 & 2742502 ^ 524354) == 0) {
                  ;
            }

            double var3 = var10000;
            double var5 = var0.posZ + (var0.posZ - var0.lastTickPosZ) - mc.player.posZ;
            double var7 = Math.sqrt(Math.pow(var1, 2.0D) + Math.pow(var5, 2.0D));
            float var9 = (float)Math.toDegrees(-Math.atan(var1 / var5));
            float var10 = (float)(-Math.toDegrees(Math.atan(var3 / var7)));
            if (var1 < 0.0D && var5 < 0.0D) {
                  if ((1660981311 >> 1 ^ 726639969 ^ 1628359443) != 0) {
                        ;
                  }

                  var10001 = var5 / var1;
                  if ((((2147316401 ^ 643984535 | 435899135) & 932917726 | 69361530) << 1 ^ -1035302043) != 0) {
                        ;
                  }

                  var9 = (float)(90.0D + Math.toDegrees(Math.atan(var10001)));
            } else {
                  if ((1893574170 >>> 3 >>> 3 ^ 19041436 ^ 14809604) == 0) {
                        ;
                  }

                  if (var1 > 0.0D) {
                        if (!"you're dogshit".equals("you probably spell youre as your")) {
                              ;
                        }

                        if (var5 < 0.0D) {
                              if (((715446875 | 226116779) >> 3 >>> 3 ^ -590500871) != 0) {
                                    ;
                              }

                              var10001 = var5 / var1;
                              if (((264699066 >>> 2 | 35393974 | 62718977) ^ 66961343) == 0) {
                                    ;
                              }

                              var10000 = -90.0D + Math.toDegrees(Math.atan(var10001));
                              if ((1512697989 >> 1 ^ 473243155 ^ 824305233) == 0) {
                                    ;
                              }

                              var9 = (float)var10000;
                        }
                  }
            }

            float[] var12 = new float[(0 ^ 1877347574 ^ 505962745) >> 4 ^ 119334594];
            var12[((578392415 ^ 229718767) & 421676485) << 1 >> 4 ^ 10814547 ^ 25494627] = var9;
            var12[(0 ^ 1802866807) & 1756606749 ^ 1748078612] = var10;
            return var12;
      }

      @SubscribeEvent
      public void onPlayerTick(PlayerTickEvent var1) {
            if (mc.player != null && !mc.player.isDead) {
                  KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), (boolean)(0 >>> 3 >> 2 ^ 1));
                  if (mc.player.onGround) {
                        KeyBinding.setKeyBindState(mc.gameSettings.keyBindJump.getKeyCode(), (boolean)((0 ^ 1790090730) << 2 & 1797453264 ^ 704804225));
                  }

                  Stream var10000 = mc.world.loadedEntityList.stream().filter((var0) -> {
                        return (boolean)(var0 != mc.player ? (0 | 730172075) << 3 ^ 1546409305 : 1294652096 >>> 3 >> 3 >>> 4 ^ 1264308);
                  }).filter((var0) -> {
                        if (((1933569415 << 2 & 1722857523 | 701288366 | 527161468) ^ -830210811) != 0) {
                              ;
                        }

                        float var1;
                        int var10000 = (var1 = mc.player.getDistance(var0) - 50.0F) == 0.0F ? 0 : (var1 < 0.0F ? -1 : 1);
                        if ((393504 ^ -917324112) != 0) {
                              ;
                        }

                        return (boolean)(var10000 <= 0 ? 0 << 4 >> 4 >> 1 ^ 1 : 306928833 >>> 4 & 8055632 ^ 2138432);
                  }).filter((var0) -> {
                        return (boolean)(!var0.isDead ? ((0 | 700737881) ^ 446133132 | 391972534) ^ 417793150 ^ 800594568 : (280936233 ^ 39260435 ^ 91653428 | 281972079) ^ 400535407);
                  }).filter(this::attackCheck).filter((var0) -> {
                        return (boolean)(!(var0 instanceof EntityArmorStand) ? (0 << 4 >>> 4 & 1193985274) >>> 1 & 921638135 ^ 1 : 529 >>> 1 & 78 ^ 4 ^ 12);
                  });
                  Function var10001 = (var0) -> {
                        return Float.valueOf(mc.player.getDistance(var0));
                  };
                  if (!"you're dogshit".equals("i hope you catch fire ngl")) {
                        ;
                  }

                  var10000 = var10000.sorted(Comparator.comparing(var10001));
                  if ((((446297281 | 291412824) & 414912076) >>> 2 ^ 88273653 ^ -1693005698) != 0) {
                        ;
                  }

                  List var2 = (List)var10000.collect(Collectors.toList());
                  if (var2.size() > 0) {
                        int var6 = ((159926134 >>> 3 ^ 19889640) & 422943) >> 1 ^ 211459;
                        if (((1342462682 ^ 179262262) << 1 << 1 >>> 4 ^ 111854203) == 0) {
                              ;
                        }

                        float[] var3 = (float[])PVPBot.getRotations((Entity)var2.get(var6));
                        if (mc.player.getDistance((Entity)var2.get((726107057 >> 4 >> 1 | 3888371) ^ 24870143)) > 7.0F) {
                              long var4 = System.currentTimeMillis() - this.curTimeRotate;
                              long var7 = (long)this.rotateTimer;
                              if (!"stop skidding".equals("please take a shower")) {
                                    ;
                              }

                              if (var4 >= var7) {
                                    float var8 = var3[501436914 >> 4 << 3 & 5244642 ^ 5243104];
                                    int var10003 = (0 ^ 1199684759 | 925028779) ^ 2007236030;
                                    if ((647037574 >> 3 << 3 ^ 647037568) == 0) {
                                          ;
                                    }

                                    float var10002 = var3[var10003];
                                    EntityPlayer var9 = (EntityPlayer)var2.get(756032000 >> 3 >>> 3 ^ 11813000);
                                    if ((576725401 ^ 21612973 ^ -1650359475) != 0) {
                                          ;
                                    }

                                    this.setRotation(var8, var10002, var9);
                                    if ((((1159688815 | 824987810 | 432830588) << 2 | 1293985530) ^ 1505030011) != 0) {
                                          ;
                                    }

                                    if ((1256096212 >> 1 >> 2 >>> 3 >>> 2 ^ -593993607) != 0) {
                                          ;
                                    }

                                    this.curTimeRotate = System.currentTimeMillis();
                                    if (((299180170 << 3 & 329847672) >>> 3 ^ 4552217 ^ 500301890) != 0) {
                                          ;
                                    }
                              }
                        }

                        if ((double)mc.player.getDistance((Entity)var2.get(((2089076680 | 1231483253) ^ 799066276 | 654152209) ^ 615348610 ^ 1381156571)) <= 3.5D && System.currentTimeMillis() - this.curTimeHit >= (long)this.hitDelayTimer) {
                              this.setRotation(var3[(889281834 >> 3 ^ 61980325 | 775966) & 15224346 ^ 542234], var3[0 & 233691236 & 1132351934 & 1025615261 ^ 1], (EntityPlayer)var2.get((2041525347 ^ 326598764 ^ 889742448) >> 2 & 19421299 ^ 18878483));
                              mc.playerController.attackEntity(mc.player, (Entity)var2.get(((969424896 ^ 462617615) & 20891990 | 1207122) ^ 1764182));
                              EntityPlayerSP var5 = mc.player;
                              if (!"Some babies were dropped on their heads but you were clearly thrown at a wall.".equals("please take a shower")) {
                                    ;
                              }

                              var5.swingArm(EnumHand.MAIN_HAND);
                              this.curTimeHit = System.currentTimeMillis();
                        }
                  }

            } else {
                  if (((1490332082 | 1186864382) >> 2 ^ -1711771606) != 0) {
                        ;
                  }

            }
      }

      @SubscribeEvent
      @SideOnly(Side.SERVER)
      public void onCameraSetup(CameraSetup var1) {
            if (mc.player != null && !mc.player.isDead) {
                  if (((113241868 >> 4 | 5476132) ^ 236834306) != 0) {
                        ;
                  }

                  Stream var10000 = mc.world.loadedEntityList.stream().filter((var0) -> {
                        int var10000;
                        if (var0 != mc.player) {
                              var10000 = ((0 ^ 1639538962) >> 2 & 314818239 | 209076628) ^ 477518229;
                        } else {
                              if (!"i hope you catch fire ngl".equals("shitted on you harder than archybot")) {
                                    ;
                              }

                              var10000 = (889982603 >> 4 << 3 ^ 233051238) & 153917890 ^ 18878722;
                        }

                        if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("yo mama name maurice")) {
                              ;
                        }

                        return (boolean)var10000;
                  }).filter((var0) -> {
                        int var10000;
                        if ((double)mc.player.getDistance(var0) <= 3.5D) {
                              var10000 = 0 >>> 2 >>> 3 << 1 << 4 >>> 3 ^ 1;
                              if (!"your mom your dad the one you never had".equals("please dont crack my plugin")) {
                                    ;
                              }
                        } else {
                              if ((420614528 >>> 2 << 1 ^ 210307264) == 0) {
                                    ;
                              }

                              var10000 = ((248420934 ^ 95493253) >> 4 ^ 9327646) & 326466 & 22954 ^ 2050;
                              if ((134292524 >>> 3 & 3730548 ^ 874052733) != 0) {
                                    ;
                              }
                        }

                        return (boolean)var10000;
                  });
                  Predicate var10001 = (var0) -> {
                        int var10000;
                        if (!var0.isDead) {
                              var10000 = ((0 ^ 1854906318) & 1401804648 | 870570070) >>> 4 ^ 121568756;
                              if (((494697561 ^ 260001485) >> 3 & 24460233 ^ 1436799514) != 0) {
                                    ;
                              }
                        } else {
                              var10000 = (671245637 >>> 1 ^ 34953952) >> 4 ^ 23152260;
                        }

                        return (boolean)var10000;
                  };
                  if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("you probably spell youre as your")) {
                        ;
                  }

                  List var2 = (List)var10000.filter(var10001).filter(this::attackCheck).filter((var0) -> {
                        boolean var10000 = var0 instanceof EntityArmorStand;
                        if (!"stop skidding".equals("yo mama name maurice")) {
                              ;
                        }

                        int var1;
                        if (!var10000) {
                              var1 = (0 & 1987741609 & 1861546488) >> 2 ^ 1;
                              if ((1701224349 >> 3 << 1 >> 2 ^ -1670131486) != 0) {
                                    ;
                              }
                        } else {
                              if ((262147 >> 4 >> 4 ^ 94959510) != 0) {
                                    ;
                              }

                              var1 = 284248 << 4 >> 1 ^ 2273984;
                              if (((5364647 | 3401905) >> 2 >> 3 ^ -496138882) != 0) {
                                    ;
                              }
                        }

                        return (boolean)var1;
                  }).sorted(Comparator.comparing((var0) -> {
                        return Float.valueOf(mc.player.getDistance(var0));
                  })).collect(Collectors.toList());
                  if (var2.size() > 0) {
                        if (!"you probably spell youre as your".equals("minecraft")) {
                              ;
                        }

                        float[] var3 = (float[])PVPBot.getRotations((EntityLivingBase)var2.get((345129090 | 54497367 | 358577754) >>> 4 ^ 25165677));
                        var3[1249491740 << 4 >> 4 & 1617299188 ^ 1616904724] += (float)this.random.nextInt((10 >> 4 >>> 3 & 1648269651) >> 3 << 1 ^ 30) * 0.1F;
                        int var8 = 0 >> 1 & 711107697 ^ 1;
                        float var10002 = var3[0 >> 1 & 711107697 ^ 1];
                        if ((((1892780193 << 4 ^ 118505322) & 114359718) >>> 3 >> 4 ^ 262506) == 0) {
                              ;
                        }

                        Random var10003 = this.random;
                        if (!"i hope you catch fire ngl".equals("your mom your dad the one you never had")) {
                              ;
                        }

                        var3[var8] = var10002 + (float)var10003.nextInt(((54 | 18) & 5) >> 3 & 1008807191 ^ 60) * 0.1F;
                        float var4 = var3[691003176 >> 2 >>> 2 >>> 1 >>> 1 ^ 10796924] - 180.0F;
                        float var5 = var3[(0 ^ 810725791 | 42114166) ^ 852671998];
                        if (((372001881 ^ 303505691 | 58944487 | 40778184) ^ 19744773 ^ 441731380) != 0) {
                              ;
                        }

                        Minecraft var6 = mc;
                        if ((679881572 >> 3 >>> 2 >> 3 << 1 ^ -88118779) != 0) {
                              ;
                        }

                        var6.player.renderYawOffset = var4 - 180.0F;
                        mc.player.rotationYawHead = var4 - 180.0F;
                        float var7;
                        float var9;
                        Random var10;
                        if (var4 >= 0.0F) {
                              if (var1.getYaw() < var4) {
                                    for(; var1.getYaw() < var4; var1.setYaw(var9)) {
                                          var9 = var1.getYaw() + (float)this.random.nextInt(46 >>> 4 << 3 >>> 3 ^ 97) * 0.001F;
                                          if ((311197703 >>> 3 << 1 >>> 3 ^ 9724928) == 0) {
                                                ;
                                          }
                                    }
                              } else {
                                    for(; var1.getYaw() > var4; var1.setYaw(var9 - (float)var10.nextInt((98 >> 3 ^ 8) >> 3 >> 4 ^ 114101125 ^ 114101222) * 0.001F)) {
                                          var9 = var1.getYaw();
                                          if (!"yo mama name maurice".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                                                ;
                                          }

                                          var10 = this.random;
                                          if ((1214251394 >>> 3 ^ 151781424) == 0) {
                                                ;
                                          }
                                    }
                              }
                        } else {
                              if (((789054613 >> 4 & 31442099) << 4 ^ -1787418077) != 0) {
                                    ;
                              }

                              if (var1.getYaw() < var4) {
                                    while(var1.getYaw() < var4) {
                                          var1.setYaw(var1.getYaw() + (float)this.random.nextInt((89 << 1 | 83) ^ 144) * 0.001F);
                                    }
                              } else {
                                    while(true) {
                                          var7 = var1.getYaw();
                                          if (!"please get a girlfriend and stop cracking plugins".equals("please dont crack my plugin")) {
                                                ;
                                          }

                                          if (var7 <= var4) {
                                                break;
                                          }

                                          var9 = var1.getYaw();
                                          var10 = this.random;
                                          if ((67442347 << 1 >>> 2 << 2 ^ 565321015) != 0) {
                                                ;
                                          }

                                          var1.setYaw(var9 - (float)var10.nextInt((72 & 42) >>> 2 << 2 ^ 107) * 0.001F);
                                          if ((16386 >>> 1 ^ 1436 ^ 9629) == 0) {
                                                ;
                                          }
                                    }
                              }
                        }

                        if (var5 >= 0.0F) {
                              var7 = var1.getPitch();
                              if (!"intentMoment".equals("please dont crack my plugin")) {
                                    ;
                              }

                              if (var7 < var5) {
                                    while(true) {
                                          var7 = var1.getPitch();
                                          if ((799347242 >> 1 ^ 26301154 ^ 373545975) == 0) {
                                                ;
                                          }

                                          if (var7 >= var5) {
                                                break;
                                          }

                                          var9 = var1.getPitch();
                                          var10002 = (float)this.random.nextInt((64 >> 2 | 10) ^ 25 ^ 96) * 0.001F;
                                          if ((((756621122 >>> 1 & 136225101) << 2 | 2102362) ^ 3282014) == 0) {
                                                ;
                                          }

                                          var9 += var10002;
                                          if ((134228224 >>> 3 ^ 1680712997) != 0) {
                                                ;
                                          }

                                          var1.setPitch(var9);
                                    }
                              } else {
                                    while(true) {
                                          if (((1142118537 ^ 1053515373) >>> 4 ^ -2049792294) == 0) {
                                          }

                                          var7 = var1.getPitch();
                                          if (!"You're so fat whenever you go to the beach the tide comes in.".equals("please go outside")) {
                                                ;
                                          }

                                          if (var7 <= var5) {
                                                break;
                                          }

                                          var9 = var1.getPitch();
                                          if (((1062384997 | 894251269) << 1 << 3 ^ -168077744) == 0) {
                                                ;
                                          }

                                          if ((69206144 >>> 3 ^ 8650768) == 0) {
                                                ;
                                          }

                                          var1.setPitch(var9 - (float)this.random.nextInt((91 ^ 62) & 77 ^ 20 ^ 50) * 0.001F);
                                    }
                              }
                        } else if (var1.getPitch() < var5) {
                              for(; var1.getPitch() < var5; var1.setPitch(var9 + (float)this.random.nextInt(82 >> 3 >> 3 << 2 ^ 103) * 0.001F)) {
                                    var9 = var1.getPitch();
                                    if ((((1865936294 ^ 31844605) >>> 3 ^ 54229462 ^ 90218508) >> 4 ^ 12065175) == 0) {
                                          ;
                                    }
                              }
                        } else {
                              while(var1.getPitch() > var5) {
                                    var1.setPitch(var1.getPitch() - (float)this.random.nextInt((34 << 2 | 13) & 2 ^ 99) * 0.001F);
                              }
                        }
                  }

            }
      }

      public void onDisable() {
            if ((((1196429275 ^ 296158923) & 34984178) >> 3 ^ -882984280) != 0) {
                  ;
            }

            if (mc.player != null) {
                  super.onDisable();
                  KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), (boolean)((1559679091 ^ 275943157) << 4 & 312465840 ^ 378912));
            }
      }

      public boolean attackCheck(Entity var1) {
            if (var1 instanceof EntityPlayer) {
                  if (((546208968 ^ 13026055) >> 1 ^ 266504960 ^ -1634731305) != 0) {
                        ;
                  }

                  EntityPlayer var10000 = (EntityPlayer)var1;
                  if (((2112922201 >>> 2 | 415792700) ^ 536641214) == 0) {
                        ;
                  }

                  if (var10000.getHealth() > 0.0F && Math.abs(mc.player.rotationYaw - ((float[])RotationUtils.getFuckedUpRotations((EntityLivingBase)var1))[(333044224 ^ 251169207 | 114295904) << 4 ^ -15368336]) % 180.0F < 190.0F && !var1.isInvisible()) {
                        UUID var2 = var1.getUniqueID();
                        Minecraft var10001 = mc;
                        if (((1794834026 | 989872536) << 3 ^ -539492400) == 0) {
                              ;
                        }

                        if (!var2.equals(var10001.player.getUniqueID())) {
                              return (boolean)(((0 ^ 829811396 | 327241674) ^ 204951184) << 2 ^ -16087687);
                        }
                  }
            }

            if (((2079491808 | 1374383255) >> 4 << 3 ^ 1040054136) == 0) {
                  ;
            }

            return (boolean)(453057501 >>> 1 >> 2 >>> 1 ^ 28316093);
      }

      private void setRotation(float var1, float var2, EntityPlayer var3) {
            mc.player.renderYawOffset = var1;
            mc.player.rotationYawHead = var1;
            if ((((598765811 << 1 ^ 654178489) >> 3 | 23842878) ^ -453990401) != 0) {
                  ;
            }

            if ((((115217205 << 3 ^ 394039419) & 372160918 | 413486) ^ 1647063341) != 0) {
                  ;
            }

            EntityPlayerSP var10000;
            double var10001;
            if (var1 >= 0.0F) {
                  var10000 = mc.player;
                  if (!"your mom your dad the one you never had".equals("stringer is a good obfuscator")) {
                        ;
                  }

                  if (var10000.rotationYaw < var1) {
                        while(mc.player.rotationYaw < var1) {
                              var10000 = mc.player;
                              var10000.rotationYaw = (float)((double)var10000.rotationYaw + (double)this.random.nextInt(73 >> 1 ^ 18 ^ 85) * 1.0E-4D);
                        }
                  } else {
                        for(; mc.player.rotationYaw > var1; var10000.rotationYaw = (float)(var10001 - (double)this.random.nextInt((88 & 44) << 1 ^ 6 ^ 117) * 1.0E-4D)) {
                              var10000 = mc.player;
                              var10001 = (double)var10000.rotationYaw;
                              if (!"You're so fat whenever you go to the beach the tide comes in.".equals("please get a girlfriend and stop cracking plugins")) {
                                    ;
                              }
                        }
                  }
            } else {
                  if ((((797754624 ^ 20191969) & 185176886 | 121322791) ^ 255573799) == 0) {
                        ;
                  }

                  if (mc.player.rotationYaw < var1) {
                        while(true) {
                              float var4 = mc.player.rotationYaw;
                              if (!"idiot".equals("please get a girlfriend and stop cracking plugins")) {
                                    ;
                              }

                              if (var4 >= var1) {
                                    break;
                              }

                              var10000 = mc.player;
                              var10001 = (double)var10000.rotationYaw;
                              Random var10002 = this.random;
                              int var10003 = (89 ^ 5 | 17) ^ 66 ^ 124;
                              if (((420578522 ^ 46154746 | 443722291 | 396430198) << 3 >>> 1 ^ 583505296) != 0) {
                                    ;
                              }

                              var10000.rotationYaw = (float)(var10001 + (double)var10002.nextInt(var10003) * 1.0E-4D);
                        }
                  } else {
                        while(mc.player.rotationYaw > var1) {
                              var10000 = mc.player;
                              var10000.rotationYaw = (float)((double)var10000.rotationYaw - (double)this.random.nextInt((85 >>> 3 ^ 7) << 2 & 40 ^ 67) * 1.0E-4D);
                        }
                  }
            }

      }

      public void onEnable() {
            if (mc.player != null) {
                  super.onEnable();
                  this.posX = mc.player.posX;
            }
      }
}
