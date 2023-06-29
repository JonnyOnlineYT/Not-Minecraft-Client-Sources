//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.movement;

import me.independed.inceptice.modules.Module;
import me.independed.inceptice.settings.ModeSetting;
import me.independed.inceptice.settings.NumberSetting;
import me.independed.inceptice.settings.Setting;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class WaterSpeed extends Module {
      public int ticks;
      public ModeSetting modesWaterSpeed;
      public NumberSetting speedSetting;

      public void onDisable() {
            super.onDisable();
            this.ticks = (318255420 ^ 110454111) >>> 1 >>> 2 >> 4 ^ 2677416;
      }

      public static void setMoveSpeed(double var0) {
            Minecraft var10000 = mc;
            if (((813201092 >>> 3 | 10943742) ^ 112197374) == 0) {
                  ;
            }

            EntityPlayerSP var7 = var10000.player;
            if (!"your mom your dad the one you never had".equals("buy a domain and everything else you need at namecheap.com")) {
                  ;
            }

            double var2 = (double)var7.movementInput.moveForward;
            double var4 = (double)mc.player.movementInput.moveStrafe;
            float var8 = mc.player.rotationYaw;
            if (((1722278578 ^ 1098446053) >>> 1 >> 4 ^ 20904114) == 0) {
                  ;
            }

            float var6 = var8;
            if (var2 == 0.0D && var4 == 0.0D) {
                  mc.player.motionX = 0.0D;
                  mc.player.motionZ = 0.0D;
            } else {
                  if (((444753158 ^ 121079199) >>> 1 ^ -1802007781) != 0) {
                        ;
                  }

                  if (!"minecraft".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                        ;
                  }

                  if (var2 != 0.0D) {
                        if (var4 > 0.0D) {
                              var6 += (float)(var2 > 0.0D ? 198708279 >> 3 >>> 3 ^ 2534121 ^ -641782 : (40 << 3 ^ 195) << 2 ^ 1569);
                        } else {
                              if (!"You're so fat whenever you go to the beach the tide comes in.".equals("please take a shower")) {
                                    ;
                              }

                              double var11;
                              int var9 = (var11 = var4 - 0.0D) == 0.0D ? 0 : (var11 < 0.0D ? -1 : 1);
                              if ((1860889561 >> 1 >>> 1 & 195604718 ^ 195604710) == 0) {
                                    ;
                              }

                              if (var9 < 0) {
                                    int var10001;
                                    if (var2 > 0.0D) {
                                          var10001 = (1 | 0) << 4 << 1 << 2 ^ 173;
                                    } else {
                                          if (!"yo mama name maurice".equals("you're dogshit")) {
                                                ;
                                          }

                                          var10001 = (1340206281 ^ 360129324) & 775476105 ^ 30406240 ^ -199177678;
                                    }

                                    var6 += (float)var10001;
                              }
                        }

                        var4 = 0.0D;
                        if (var2 > 0.0D) {
                              var2 = 1.0D;
                        } else if (var2 < 0.0D) {
                              var2 = -1.0D;
                              if ((((926894364 ^ 42711564) >>> 2 ^ 133320740) >>> 3 ^ 2062058120) != 0) {
                                    ;
                              }
                        }
                  }

                  if (((2000261423 | 390567711) >> 4 >>> 4 ^ 7831447) == 0) {
                        ;
                  }

                  var7 = mc.player;
                  double var10 = var2 * var0;
                  if ((('趀' >> 4 ^ 1445) & 377 ^ 377) == 0) {
                        ;
                  }

                  double var10002 = Math.cos(Math.toRadians((double)(var6 + 90.0F)));
                  if (!"please get a girlfriend and stop cracking plugins".equals("your mom your dad the one you never had")) {
                        ;
                  }

                  var10 *= var10002;
                  var10002 = var4 * var0;
                  double var10003 = Math.toRadians((double)(var6 + 90.0F));
                  if (((1505998301 >> 4 & 58113637 | 13219222) ^ 31309783) == 0) {
                        ;
                  }

                  var7.motionX = var10 + var10002 * Math.sin(var10003);
                  var7 = mc.player;
                  var10 = var2 * var0 * Math.sin(Math.toRadians((double)(var6 + 90.0F)));
                  var10002 = var4 * var0;
                  if (((2108493851 >> 4 & 26862809) >> 2 >> 4 ^ 418560) == 0) {
                        ;
                  }

                  var7.motionZ = var10 - var10002 * Math.cos(Math.toRadians((double)(var6 + 90.0F)));
            }

            if (!"please take a shower".equals("intentMoment")) {
                  ;
            }

      }

      public static Block getBlock(BlockPos var0) {
            return mc.world.getBlockState(var0).getBlock();
      }

      public WaterSpeed() {
            if (!"You're so fat whenever you go to the beach the tide comes in.".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                  ;
            }

            if (!"minecraft".equals("please get a girlfriend and stop cracking plugins")) {
                  ;
            }

            super("WaterSpeed", "faster in water", ((1614921105 ^ 1604309611) & 262339254) >>> 3 ^ 32525654, Module.Category.MOVEMENT);
            NumberSetting var10001 = new NumberSetting;
            if (!"minecraft".equals("intentMoment")) {
                  ;
            }

            var10001.<init>("Speed", this, 1.0D, 0.1D, 5.0D, 0.1D);
            this.speedSetting = var10001;
            ModeSetting var1 = new ModeSetting;
            String[] var10006 = new String[(0 ^ 687796354) >>> 1 ^ 343898179];
            if ((918198956 >>> 1 >> 4 ^ 943455 ^ 29077898) == 0) {
                  ;
            }

            var10006[(1521828409 >>> 2 | 82015252) >> 1 ^ 192395855] = "Fly";
            var10006[(0 & 467394769) >> 4 ^ 1] = "Simple";
            var1.<init>("Mode", this, "Simple", var10006);
            this.modesWaterSpeed = var1;
            this.ticks = 1775259010 << 4 >> 2 >>> 1 >>> 4 ^ 121244080;
            Setting[] var2 = new Setting[((0 & 1817236327) >>> 3 | 404318178) ^ 195879605 ^ 330663253];
            int var10003 = (776286597 << 2 >> 4 & 824459889) << 4 ^ 268699152;
            if ((((955415251 ^ 736968888) & 262121757) >>> 1 ^ 26186756) == 0) {
                  ;
            }

            var2[var10003] = this.speedSetting;
            if (((388517241 | 180173241) >> 2 ^ -1418090385) != 0) {
                  ;
            }

            if (!"you're dogshit".equals("nefariousMoment")) {
                  ;
            }

            var2[(0 >>> 2 ^ 1625119645 | 994446832) >>> 4 ^ 129889790] = this.modesWaterSpeed;
            this.addSettings(var2);
      }

      @SubscribeEvent
      public void onPlayerTick(PlayerTickEvent var1) {
            if (mc.player != null) {
                  if (mc.player.isInWater()) {
                        String var10000 = this.modesWaterSpeed.activeMode;
                        if (!"you're dogshit".equals("nefariousMoment")) {
                              ;
                        }

                        if (var10000 == "Simple") {
                              this.ticks += (0 << 1 ^ 182004033 ^ 177544995) << 3 ^ 39957265;
                              if (this.ticks == (3 >> 1 << 4 ^ 20)) {
                                    WaterSpeed.setMoveSpeed(this.speedSetting.getValue());
                              }

                              if (((((96807949 ^ 81816595) & 1346285) >>> 4 | 2614) ^ 18998) == 0) {
                                    ;
                              }

                              if (this.ticks >= ((4 >> 3 | 1049464041) >>> 4 ^ 65591499)) {
                                    WaterSpeed.setMoveSpeed(this.speedSetting.getValue());
                                    this.ticks = (69217905 ^ 48556405) << 4 >>> 3 ^ 227118600;
                              }

                              WaterSpeed.setMoveSpeed(this.speedSetting.getValue());
                        } else {
                              if (!"i hope you catch fire ngl".equals("you probably spell youre as your")) {
                                    ;
                              }

                              if (WaterSpeed.getBlock(mc.player.getPosition()) instanceof BlockLiquid) {
                                    float var2 = (float)this.speedSetting.getValue();
                                    EntityPlayerSP var3 = mc.player;
                                    var3.motionX *= (double)var2;
                                    if (!"nefariousMoment".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                                          ;
                                    }

                                    EntityPlayerSP var4 = mc.player;
                                    var4.motionZ *= (double)var2;
                              }
                        }
                  }

                  if (!"stop skidding".equals("stringer is a good obfuscator")) {
                        ;
                  }

            }
      }
}
