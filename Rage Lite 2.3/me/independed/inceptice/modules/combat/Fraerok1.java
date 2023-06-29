//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.combat;

import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import me.independed.inceptice.modules.Module;
import me.independed.inceptice.settings.BooleanSetting;
import me.independed.inceptice.settings.NumberSetting;
import me.independed.inceptice.settings.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class Fraerok1 extends Module {
      private Random rand;
      public NumberSetting range = new NumberSetting("Range", this, 4.0D, 0.5D, 10.0D, 0.1D);
      public static double smoothAimSpeed = 2.5D;
      public BooleanSetting smoothAim = new BooleanSetting("Smooth", this, (boolean)(0 >>> 3 >>> 1 ^ 1));

      public static double getSmoothAimSpeed() {
            return smoothAimSpeed;
      }

      public void facePos(Vec3d var1) {
            if (!"please take a shower".equals("shitted on you harder than archybot")) {
                  ;
            }

            boolean var10000 = this.smoothAim.isEnabled();
            if ((662066001 >> 2 & 29406292 ^ -1018845980) != 0) {
                  ;
            }

            if (var10000) {
                  this.smoothFacePos(var1);
            } else {
                  double var12 = var1.x + 0.5D;
                  if ((1279123091 << 3 >>> 2 & 388241032 ^ 97862342 ^ 368400070) == 0) {
                        ;
                  }

                  double var2 = var12 - Minecraft.getMinecraft().player.posX;
                  var12 = var1.y;
                  if (((363484444 ^ 150213470 | 300688840) ^ 1664481643) != 0) {
                        ;
                  }

                  var12 += 0.5D;
                  double var10001 = Minecraft.getMinecraft().player.posY + (double)Minecraft.getMinecraft().player.getEyeHeight();
                  if (((568001045 | 186964029) ^ 471785948 ^ -1982001789) != 0) {
                        ;
                  }

                  double var4 = var12 - var10001;
                  var12 = var1.z;
                  if (((1206218934 << 2 & 360024277 | 26045327) ^ 362671071) == 0) {
                        ;
                  }

                  var12 += 0.5D;
                  var10001 = Minecraft.getMinecraft().player.posZ;
                  if (((1697093224 << 2 & 1840856808) >>> 3 ^ 9634580) == 0) {
                        ;
                  }

                  var12 -= var10001;
                  if (((106082342 >>> 4 & 2006899) << 1 ^ 615377802) != 0) {
                        ;
                  }

                  double var6 = var12;
                  double var8 = (double)MathHelper.sqrt(var2 * var2 + var6 * var6);
                  if (!"buy a domain and everything else you need at namecheap.com".equals("stringer is a good obfuscator")) {
                        ;
                  }

                  float var10 = (float)(Math.atan2(var6, var2) * 180.0D / 3.141592653589793D) - 90.0F;
                  float var11 = (float)(-(Math.atan2(var4, var8) * 180.0D / 3.141592653589793D));
                  Minecraft.getMinecraft().player.rotationYaw += MathHelper.wrapDegrees(var10 - Minecraft.getMinecraft().player.rotationYaw);
                  Minecraft.getMinecraft().player.rotationPitch += MathHelper.wrapDegrees(var11 - Minecraft.getMinecraft().player.rotationPitch);
            }
      }

      public Fraerok1() {
            super("AimAssist", "aims at entity", (1593814185 ^ 183720493) << 4 ^ 246869313 ^ 1316586753, Module.Category.COMBAT);
            if (((1335861932 | 537564639) >> 1 ^ -1058396368) != 0) {
                  ;
            }

            this.rand = new Random();
            Setting[] var10001 = new Setting[0 << 2 >> 2 ^ 2];
            var10001[(1888803749 | 1762878877) ^ 472564125 ^ 1266814607 ^ 775893167] = this.smoothAim;
            var10001[0 & 475858701 ^ 560874722 ^ 278889451 ^ 561034289 ^ 276919097] = this.range;
            this.addSettings(var10001);
      }

      public float[] getRotations(EntityPlayer var1) {
            double var10000 = var1.posX;
            double var10001 = var1.posX;
            if ((284795866 >> 1 << 1 ^ 284795866) == 0) {
                  ;
            }

            double var10002 = var1.lastTickPosX;
            if (((303421739 << 3 >> 1 | 417540928) ^ -66245062) != 0) {
                  ;
            }

            double var2 = var10000 + (var10001 - var10002) - mc.player.posX;
            double var4 = var1.posY + (double)var1.getEyeHeight() - mc.player.posY + (double)mc.player.getEyeHeight() - 3.5D;
            var10000 = var1.posZ;
            var10001 = var1.posZ - var1.lastTickPosZ;
            if (!"you probably spell youre as your".equals("please get a girlfriend and stop cracking plugins")) {
                  ;
            }

            var10000 = var10000 + var10001 - mc.player.posZ;
            if ((867470592 >> 2 >>> 1 ^ -1670878862) != 0) {
                  ;
            }

            double var6 = var10000;
            double var8 = Math.sqrt(Math.pow(var2, 2.0D) + Math.pow(var6, 2.0D));
            float var12 = (float)Math.toDegrees(-Math.atan(var2 / var6));
            if (!"yo mama name maurice".equals("yo mama name maurice")) {
                  ;
            }

            float var10;
            float var11;
            label59: {
                  var10 = var12;
                  var11 = (float)(-Math.toDegrees(Math.atan(var4 / var8)));
                  if (var2 < 0.0D) {
                        if (((1249770174 ^ 1018670981 | 114531112) >>> 4 ^ 124632563) == 0) {
                              ;
                        }

                        if (var6 < 0.0D) {
                              var10 = (float)(90.0D + Math.toDegrees(Math.atan(var6 / var2)));
                              break label59;
                        }
                  }

                  if (!"shitted on you harder than archybot".equals("You're so fat whenever you go to the beach the tide comes in.")) {
                        ;
                  }

                  if (var2 > 0.0D) {
                        if (!"ape covered in human flesh".equals("please go outside")) {
                              ;
                        }

                        if (var6 < 0.0D) {
                              if ((134341573 >> 1 << 1 & 131364295 ^ 177240334) != 0) {
                                    ;
                              }

                              var10 = (float)(-90.0D + Math.toDegrees(Math.atan(var6 / var2)));
                        }
                  }
            }

            float[] var13 = new float[(0 >> 2 | 1471300368 | 1106773303) & 706166117 ^ 34735399];
            var13[(1395337023 ^ 1237647086) << 1 ^ 903852962] = var10;
            if (!"You're so fat whenever you go to the beach the tide comes in.".equals("you probably spell youre as your")) {
                  ;
            }

            var13[0 >> 4 >> 4 << 2 ^ 1] = var11;
            return var13;
      }

      @SubscribeEvent
      public void onPlayerTick(PlayerTickEvent var1) {
            if (mc.player != null) {
                  Stream var10000 = mc.world.loadedEntityList.stream().filter((var0) -> {
                        if (!"ape covered in human flesh".equals("stringer is a good obfuscator")) {
                              ;
                        }

                        int var10000;
                        if (var0 != mc.player) {
                              var10000 = (0 & 533605661) >>> 4 ^ 1803939024 ^ 1803939025;
                        } else {
                              var10000 = (4245092 >> 3 | 101138) ^ 629726;
                              if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("please dont crack my plugin")) {
                                    ;
                              }
                        }

                        return (boolean)var10000;
                  }).filter((var1x) -> {
                        float var10000 = mc.player.getDistance(var1x);
                        if (!"yo mama name maurice".equals("i hope you catch fire ngl")) {
                              ;
                        }

                        return (boolean)((double)var10000 <= this.range.getValue() ? (0 & 868295738) << 3 & 602840764 ^ 1 : 18874884 ^ 18874884);
                  }).filter((var0) -> {
                        int var10000;
                        if (!var0.isDead) {
                              if (!"intentMoment".equals("you're dogshit")) {
                                    ;
                              }

                              var10000 = (((0 & 979237411 | 1069536553) ^ 416995286) & 313215509) >> 3 ^ 4464707;
                        } else {
                              var10000 = 739979427 << 2 >> 1 >> 3 ^ -83440600;
                        }

                        return (boolean)var10000;
                  }).filter((var0) -> {
                        return var0 instanceof EntityPlayer;
                  }).filter((var0) -> {
                        int var10000;
                        if (!var0.isInvisible()) {
                              if (!"stringer is a good obfuscator".equals("buy a domain and everything else you need at namecheap.com")) {
                                    ;
                              }

                              var10000 = (0 >>> 2 | 1292098034 | 965145664) << 2 ^ -165677111;
                        } else {
                              var10000 = 398004204 << 1 << 4 ^ -148767360;
                        }

                        return (boolean)var10000;
                  }).filter((var0) -> {
                        return (boolean)(((EntityPlayer)var0).getHealth() > 0.0F ? 0 >> 4 >>> 3 >>> 1 << 1 ^ 1 : 9439744 >>> 1 ^ 4719872);
                  });
                  Comparator var10001 = Comparator.comparing((var0) -> {
                        EntityPlayerSP var10000 = mc.player;
                        if (((1817324935 >> 4 | 37819336) ^ -500529866) != 0) {
                              ;
                        }

                        return Float.valueOf(var10000.getDistance(var0));
                  });
                  if ((1358971280 >> 3 >> 4 << 3 ^ 2144679747) != 0) {
                        ;
                  }

                  Object var11 = var10000.sorted(var10001).collect(Collectors.toList());
                  if ((19923201 >>> 4 ^ -516450485) != 0) {
                        ;
                  }

                  List var2 = (List)var11;
                  if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("shitted on you harder than archybot")) {
                        ;
                  }

                  if ((949725120 >>> 1 & 368150846 & 258074054 ^ 71401728) == 0) {
                        ;
                  }

                  int var3 = var2.size();
                  if (var3 > 0) {
                        EntityPlayer var4 = (EntityPlayer)var2.get(752337059 << 4 << 4 ^ -675241216);
                        double var12 = var4.posX - 0.5D;
                        double var13 = (var4.posX - var4.lastTickPosX) * 2.5D;
                        if (!"nefariousMoment".equals("please go outside")) {
                              ;
                        }

                        double var5 = var12 + var13;
                        double var7 = var4.posY + ((double)var4.getEyeHeight() - (double)var4.height / 1.5D) - (var4.posY - var4.lastTickPosY) * 1.5D;
                        double var9 = var4.posZ - 0.5D + (var4.posZ - var4.lastTickPosZ) * 2.5D;
                        this.facePos(new Vec3d(var5, var7, var9));
                  }

            }
      }

      public void smoothFacePos(Vec3d var1) {
            double var2 = var1.x + 0.5D - Minecraft.getMinecraft().player.posX;
            double var10000 = var1.y + 0.5D;
            double var10001 = Minecraft.getMinecraft().player.posY;
            EntityPlayerSP var10002 = Minecraft.getMinecraft().player;
            if (((1712845811 ^ 893074211 | 1182187176) ^ 578682672 ^ -1586299372) != 0) {
                  ;
            }

            double var4 = var10000 - (var10001 + (double)var10002.getEyeHeight());
            if (!"minecraft".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                  ;
            }

            double var6 = var1.z + 0.5D - Minecraft.getMinecraft().player.posZ;
            var10000 = var2 * var2;
            var10001 = var6 * var6;
            if (!"shitted on you harder than archybot".equals("intentMoment")) {
                  ;
            }

            double var8 = (double)MathHelper.sqrt(var10000 + var10001);
            float var10 = (float)(Math.atan2(var6, var2) * 180.0D / 3.141592653589793D) - 90.0F;
            if (!"stop skidding".equals("minecraft")) {
                  ;
            }

            float var11 = (float)(-(Math.atan2(var4, var8) * 180.0D / 3.141592653589793D));
            int var12 = (217109 >> 1 ^ 'è¸—') & 'è?ª' ^ 1544;
            float var13 = 5.0F;
            if (((327578175 << 4 ^ 735548208) << 4 ^ 990579712) == 0) {
                  ;
            }

            float var14 = 0.0F;
            if ((1395776892 >>> 1 << 1 ^ 1395776892) == 0) {
                  ;
            }

            if ((448722796 << 3 << 4 & 1128027933 ^ 1127749120) == 0) {
                  ;
            }

            float var17;
            if (MathHelper.wrapDegrees(var10 - Minecraft.getMinecraft().player.rotationYaw) > var13 * 2.0F) {
                  int var16 = (0 >> 2 & 1984686655 & 1263367408) << 1 ^ 1;
                  if ((799504969 >> 4 >>> 3 ^ 6246132) == 0) {
                        ;
                  }

                  var12 = var16;
            } else {
                  float var18 = Minecraft.getMinecraft().player.rotationYaw;
                  if (((1805461806 ^ 388049650) >> 4 ^ 130793533) == 0) {
                        ;
                  }

                  var17 = MathHelper.wrapDegrees(var10 - var18);
                  if ((492904576 ^ 68134214 ^ 426673606) == 0) {
                        ;
                  }

                  if (var17 < -var13 * 2.0F) {
                        var12 = 0 >>> 4 >> 2 & 1039479519 ^ 1;
                        var14 = -var13;
                        if (!"i hope you catch fire ngl".equals("stringer is a good obfuscator")) {
                              ;
                        }
                  }
            }

            float var15 = 0.0F;
            var17 = MathHelper.wrapDegrees(var11 - Minecraft.getMinecraft().player.rotationPitch);
            if (((558907536 >> 1 | 244396223) ^ -1756488337) != 0) {
                  ;
            }

            if (var17 > var13 * 4.0F) {
                  var12 = (0 ^ 827927452) >> 2 ^ 206981862;
            } else {
                  EntityPlayerSP var19 = Minecraft.getMinecraft().player;
                  if ((((805578480 | 392125895) & 524415285) << 2 << 2 ^ -1391673390) != 0) {
                        ;
                  }

                  var17 = var11 - var19.rotationPitch;
                  if (!"minecraft".equals("yo mama name maurice")) {
                        ;
                  }

                  if (MathHelper.wrapDegrees(var17) < -var13 * 4.0F) {
                        var12 = (0 ^ 1967731411) >>> 1 >> 1 ^ 491932853;
                        var15 = -var13;
                        if (((1914342718 ^ 598398086) >>> 4 >>> 2 ^ 21414246) == 0) {
                              ;
                        }
                  }
            }

            if ((1839216220 >>> 3 << 3 ^ 1839216216) == 0) {
                  ;
            }

            if (var12 != 0) {
                  if (((809824547 >>> 1 | 78290033) << 2 ^ 1923871684) == 0) {
                        ;
                  }

                  EntityPlayerSP var20 = Minecraft.getMinecraft().player;
                  var10001 = (double)var20.rotationYaw;
                  if (!"you probably spell youre as your".equals("buy a domain and everything else you need at namecheap.com")) {
                        ;
                  }

                  float var10003 = Minecraft.getMinecraft().player.rotationYaw;
                  if (((17040392 >>> 2 << 3 | 2463343) ^ -1474733989) != 0) {
                        ;
                  }

                  double var21 = (double)MathHelper.wrapDegrees(var10 - var10003);
                  if ((700971691 << 2 ^ 1517996495 ^ -43703453) == 0) {
                        ;
                  }

                  var20.rotationYaw = (float)(var10001 + var21 / (Fraerok1.getSmoothAimSpeed() * (this.rand.nextDouble() * 2.0D + 1.0D)));
                  Minecraft var22 = Minecraft.getMinecraft();
                  if ((1795479131 >>> 4 >>> 2 & 26097063 ^ -1601204831) != 0) {
                        ;
                  }

                  var20 = var22.player;
                  var10001 = (double)var20.rotationPitch;
                  var21 = (double)MathHelper.wrapDegrees(var11 - Minecraft.getMinecraft().player.rotationPitch) / (Fraerok1.getSmoothAimSpeed() * (this.rand.nextDouble() * 2.0D + 1.0D));
                  if ((((378991795 >> 1 ^ 102705073) & 102188816) >>> 1 ^ 34218624) == 0) {
                        ;
                  }

                  var20.rotationPitch = (float)(var10001 + var21);
            }

      }
}
