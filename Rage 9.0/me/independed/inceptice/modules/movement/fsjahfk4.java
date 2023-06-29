//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.movement;

import me.independed.inceptice.modules.Module;
import me.independed.inceptice.settings.ModeSetting;
import me.independed.inceptice.settings.NumberSetting;
import me.independed.inceptice.settings.Setting;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockHopper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.network.play.client.CPacketPlayer.Position;
import net.minecraft.network.play.client.CPacketPlayer.PositionRotation;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.PlayerSPPushOutOfBlocksEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class fsjahfk4 extends Module {
      public ModeSetting modeSetting;
      public NumberSetting speedSet;

      public static void setMoveSpeedAris(double var0) {
            double var2 = (double)Minecraft.getMinecraft().player.movementInput.moveForward;
            double var4 = (double)Minecraft.getMinecraft().player.movementInput.moveStrafe;
            double var6 = (double)Minecraft.getMinecraft().player.rotationYaw;
            if (var2 == 0.0D) {
                  if (((90794524 >> 2 ^ 1084448) >> 4 ^ 148771672) != 0) {
                        ;
                  }

                  if (var4 == 0.0D) {
                        return;
                  }
            }

            double var10001;
            if (var2 != 0.0D) {
                  if (var4 > 0.0D) {
                        if (!"You're so fat whenever you go to the beach the tide comes in.".equals("yo mama name maurice")) {
                              ;
                        }

                        var6 += var2 > 0.0D ? -45.0D : 45.0D;
                  } else {
                        double var9;
                        int var10000 = (var9 = var4 - 0.0D) == 0.0D ? 0 : (var9 < 0.0D ? -1 : 1);
                        if (((533429010 >> 2 | 22555510) >> 2 >> 1 << 2 ^ 979735485) != 0) {
                              ;
                        }

                        if (var10000 < 0) {
                              if (var2 > 0.0D) {
                                    var10001 = 45.0D;
                              } else {
                                    var10001 = -45.0D;
                                    if (((218590497 >> 1 | 14896305) ^ 115605169) == 0) {
                                          ;
                                    }
                              }

                              var6 += var10001;
                        }
                  }

                  var4 = 0.0D;
                  if (var2 > 0.0D) {
                        var2 = 1.0D;
                  } else if (var2 < 0.0D) {
                        if (((1058 >>> 1 | 503) ^ 12291262) != 0) {
                              ;
                        }

                        var2 = -1.0D;
                  }
            }

            if (spidiki5.isMoving()) {
                  mc.player.motionX = var2 * var0 * Math.cos(Math.toRadians(var6 + 88.0D)) + var4 * var0 * Math.sin(Math.toRadians(var6 + 87.9000015258789D));
                  if (((766312120 >>> 2 >> 1 | 59574986) ^ 1097876269) != 0) {
                        ;
                  }

                  EntityPlayerSP var8 = mc.player;
                  var10001 = var2 * var0 * Math.sin(Math.toRadians(var6 + 88.0D));
                  double var10002 = var4 * var0;
                  if ((((2090376388 | 37644333) << 2 | 85958055) ^ -73) == 0) {
                        ;
                  }

                  double var10003 = var6 + 87.9000015258789D;
                  if (((1112554063 >>> 1 | 379609595) >> 1 ^ 390781460 ^ 211739883) == 0) {
                        ;
                  }

                  var8.motionZ = var10001 - var10002 * Math.cos(Math.toRadians(var10003));
                  if (((42655843 << 1 ^ 82519392) >> 2 ^ 51642973) != 0) {
                        ;
                  }
            }

      }

      public String getFacingWithProperCapitals() {
            if (!"Some babies were dropped on their heads but you were clearly thrown at a wall.".equals("buy a domain and everything else you need at namecheap.com")) {
                  ;
            }

            EnumFacing var10000 = mc.getRenderViewEntity().getHorizontalFacing();
            if ((('쁄' | 9360) ^ 509392287) != 0) {
                  ;
            }

            String var1 = var10000.getName();
            int var3 = (358204103 | 142182933) << 4 >>> 4 ^ -226085592;
            switch(var1.hashCode()) {
            case 3105789:
                  if (var1.equals("east")) {
                        if ((411505137 >>> 3 & 24413111 ^ 17859126) == 0) {
                              ;
                        }

                        var3 = (1 ^ 0) >>> 3 >>> 2 >>> 1 ^ 585007851 ^ 585007848;
                  }
                  break;
            case 3645871:
                  if (var1.equals("west")) {
                        var3 = (0 >>> 2 & 1542149514 | 242689086) ^ 242689084;
                        if (((((1829616446 ^ 1665386094) & 36887812) >>> 2 & 2246681 | 803778296) ^ 803778296) == 0) {
                              ;
                        }
                  }
                  break;
            case 105007365:
                  if ((604537160 >>> 2 >> 4 ^ 4360024 ^ -2035014249) != 0) {
                        ;
                  }

                  if ((((611108181 | 392240807) >> 1 >> 4 >> 2 | 5015916) ^ 7265279) == 0) {
                        ;
                  }

                  if (var1.equals("north")) {
                        int var5 = 2072528497 << 1 << 2 >> 1 >> 4 ^ -18738788;
                        if (((1090244614 ^ 67509120) >> 4 & 63391814 ^ 1262784418) != 0) {
                              ;
                        }

                        var3 = var5;
                  }
                  break;
            case 109627853:
                  boolean var4 = var1.equals("south");
                  if (((553711660 >>> 3 ^ 17741262 ^ 76607932) << 1 ^ 58504942) == 0) {
                        ;
                  }

                  if (var4) {
                        var3 = ((0 | 2079826330) >> 3 | 24195723) & 223047699 ^ 223047698;
                  }
            }

            if (((136474121 << 2 << 1 & 248794805) >> 4 ^ 439837362) != 0) {
                  ;
            }

            switch(var3) {
            case 0:
                  var1 = "North";
                  break;
            case 1:
                  var1 = "South";
                  break;
            case 2:
                  if ((570490882 << 2 & 602463686 ^ -2064484171) != 0) {
                        ;
                  }

                  var1 = "West";
                  break;
            case 3:
                  var1 = "East";
            }

            return var1;
      }

      public static boolean isInsideBlock() {
            if ((1595258215 << 1 >>> 1 >> 2 ^ -11281232) != 0) {
                  ;
            }

            AxisAlignedBB var10000 = Minecraft.getMinecraft().player.getEntityBoundingBox();
            if ((327156482 >> 1 >> 1 ^ 81789120) == 0) {
                  ;
            }

            int var0 = MathHelper.floor(var10000.minX);

            while(true) {
                  if (((604524934 ^ 266058173) >>> 3 & 84652225 ^ 84553729) == 0) {
                        ;
                  }

                  int var10001 = MathHelper.floor(Minecraft.getMinecraft().player.getEntityBoundingBox().maxX);
                  int var10002 = 0 << 3 ^ 840578463 ^ 840578462;
                  if (((1182885630 << 2 & 14911563 & 16991) << 2 ^ 288) == 0) {
                        ;
                  }

                  if (var0 >= var10001 + var10002) {
                        return (boolean)(((1943635667 ^ 284162101) & 280295155) >>> 2 ^ 859576);
                  }

                  if (((388838267 | 288605248 | 388513584) ^ 390070139) == 0) {
                        ;
                  }

                  for(int var1 = MathHelper.floor(Minecraft.getMinecraft().player.getEntityBoundingBox().minY); var1 < MathHelper.floor(Minecraft.getMinecraft().player.getEntityBoundingBox().maxY) + (0 & 777899974 & 1268124945 ^ 1); ++var1) {
                        int var5 = MathHelper.floor(Minecraft.getMinecraft().player.getEntityBoundingBox().minZ);
                        if ((4216330 << 3 ^ 33730640) == 0) {
                              ;
                        }

                        for(int var2 = var5; var2 < MathHelper.floor(Minecraft.getMinecraft().player.getEntityBoundingBox().maxZ) + ((0 >> 4 | 1264061846) >> 2 ^ 316015460); ++var2) {
                              Block var3 = Minecraft.getMinecraft().world.getBlockState(new BlockPos(var0, var1, var2)).getBlock();
                              if (var3 != null && !(var3 instanceof BlockAir)) {
                                    AxisAlignedBB var4 = var3.getCollisionBoundingBox(Minecraft.getMinecraft().world.getBlockState(new BlockPos(var0, var1, var2)), Minecraft.getMinecraft().world, new BlockPos(var0, var1, var2));
                                    if (var3 instanceof BlockHopper) {
                                          var10000 = new AxisAlignedBB;
                                          double var7 = (double)var0;
                                          if ((932811563 >>> 1 >> 2 ^ 116601445) == 0) {
                                                ;
                                          }

                                          double var10003 = (double)var1;
                                          double var10004 = (double)var2;
                                          double var10005 = (double)(var0 + ((0 << 1 >> 1 >>> 2 | 123901473) ^ 123901472));
                                          if (((1036219242 ^ 1000629896) >> 1 ^ 53709553) == 0) {
                                                ;
                                          }

                                          var10000.<init>(var7, var10003, var10004, var10005, (double)(var1 + ((0 << 3 | 627634619) ^ 627634618)), (double)(var2 + (0 >> 2 << 4 & 2054414972 ^ 1)));
                                          if (((268960096 | 137588537) >> 3 << 3 ^ -162170764) != 0) {
                                                ;
                                          }

                                          var4 = var10000;
                                          if ((((260063608 >>> 4 | 14239638) ^ 5563946) & 5374023 ^ 5) == 0) {
                                                ;
                                          }
                                    }

                                    if (var4 != null) {
                                          Minecraft var6 = Minecraft.getMinecraft();
                                          if (((183099243 ^ 87978088) << 2 & 242224347 ^ 859494134) != 0) {
                                                ;
                                          }

                                          if (var6.player.getEntityBoundingBox().intersects(var4)) {
                                                var5 = (0 ^ 461888812 | 265339364) >> 3 ^ 66780092;
                                                if (((1614754197 >> 2 | 233986440) & 260607519 ^ 227052557) == 0) {
                                                      ;
                                                }

                                                return (boolean)var5;
                                          }
                                    }
                              }
                        }

                        if (((517202852 ^ 373294276 | 50869663) ^ 194770943) == 0) {
                              ;
                        }
                  }

                  if (((1296345114 << 2 ^ 676409512) >>> 1 ^ 2037613851) != 0) {
                        ;
                  }

                  if (!"i hope you catch fire ngl".equals("please get a girlfriend and stop cracking plugins")) {
                        ;
                  }

                  ++var0;
            }
      }

      public fsjahfk4() {
            if ((((2004963040 >>> 2 | 105573673) & 190175329) << 1 ^ -1851190689) != 0) {
                  ;
            }

            super("NoClip", "no clips", (1103087656 | 955209451 | 1004889504) >> 3 ^ 260046845, Module.Category.MOVEMENT);
            if (!"shitted on you harder than archybot".equals("stringer is a good obfuscator")) {
                  ;
            }

            this.speedSet = new NumberSetting("Speed", this, 0.5D, 0.3D, 5.0D, 0.1D);
            ModeSetting var10001 = new ModeSetting;
            String[] var10006 = new String[((0 >>> 1 ^ 1498700343) >>> 3 | 107221398) ^ 258907090];
            var10006[889132580 >>> 3 << 1 ^ 222283144] = "Simple";
            var10006[0 >>> 2 << 1 >>> 2 ^ 1] = "Test";
            var10006[0 << 2 >>> 1 ^ 2] = "PostTest";
            if (((747119696 | 514405854 | 555687301) ^ -249094018) != 0) {
                  ;
            }

            if ((1773641121 >>> 2 >>> 3 & 20935879 ^ 17642693) == 0) {
                  ;
            }

            var10006[1 ^ 0 ^ 0 ^ 2] = "PreTest";
            var10001.<init>("Mode", this, "Simple", var10006);
            this.modeSetting = var10001;
            Setting[] var1 = new Setting[(0 >>> 4 | 6186297 | 1790159) >> 3 ^ 782013];
            int var10003 = 1565684199 << 3 & 1654816416 ^ 1652690464;
            NumberSetting var10004 = this.speedSet;
            if (!"your mom your dad the one you never had".equals("please get a girlfriend and stop cracking plugins")) {
                  ;
            }

            var1[var10003] = var10004;
            var1[(0 & 628479589) << 2 ^ 1] = this.modeSetting;
            this.addSettings(var1);
      }

      public float getDirection() {
            float var1 = mc.player.rotationYaw;
            if (((1024388396 >>> 2 >>> 3 | 3843826) ^ 33224699) == 0) {
                  ;
            }

            var1 = MathHelper.wrapDegrees(var1);
            float var10000 = var1 * 0.017453292F;
            if ((471859493 >>> 1 ^ 1514551390) != 0) {
                  ;
            }

            return var10000;
      }

      @SubscribeEvent
      public void onPushOutOfBlocks(PlayerSPPushOutOfBlocksEvent var1) {
            var1.setCanceled((boolean)(0 >>> 3 & 327966982 ^ 1));
      }

      public void onDisable() {
            if (mc.player != null) {
                  Minecraft var10000 = mc;
                  if ((((2016260037 | 143089791) & 290126496 ^ 262170714) << 1 ^ -733677845) != 0) {
                        ;
                  }

                  WorldClient var1 = var10000.world;
                  if (((1121539055 | 765979116) << 4 << 2 >> 2 ^ -16656) == 0) {
                        ;
                  }

                  if (var1 != null) {
                        EntityPlayerSP var2 = mc.player;
                        if (((224052135 | 122809630) << 1 << 1 ^ 1030733564) == 0) {
                              ;
                        }

                        var2.noClip = (boolean)(627146058 >>> 1 >>> 3 ^ 19058994 ^ 57984742);
                        if ((((285695755 << 3 | 1886480895) ^ 103238261 | 1731909940) ^ -8455234) == 0) {
                              ;
                        }

                        NetHandlerPlayClient var3 = mc.player.connection;
                        PositionRotation var10001 = new PositionRotation;
                        double var10003 = mc.player.posX;
                        double var10004 = mc.player.getEntityBoundingBox().minY;
                        double var10005 = mc.player.posZ;
                        float var10006 = mc.player.cameraYaw;
                        float var10007 = mc.player.cameraPitch;
                        EntityPlayerSP var10008 = mc.player;
                        if (!"shitted on you harder than archybot".equals("buy a domain and everything else you need at namecheap.com")) {
                              ;
                        }

                        var10001.<init>(var10003, var10004, var10005, var10006, var10007, var10008.onGround);
                        var3.sendPacket(var10001);
                        super.onDisable();
                        return;
                  }
            }

      }

      @SubscribeEvent
      public void onLivingUpdate(LivingUpdateEvent var1) {
            if (mc.player != null && mc.world != null) {
                  if (mc.player != null) {
                        mc.player.noClip = (boolean)(0 << 2 & 532104712 ^ 1);
                        mc.player.motionY = 0.0D;
                        mc.player.onGround = (boolean)((155344904 << 4 & 4338535) << 1 >> 4 ^ 16384);
                        if (!"please dont crack my plugin".equals("ape covered in human flesh")) {
                              ;
                        }

                        if (((2092289891 ^ 685278581) >> 1 << 1 << 2 ^ 1227331738 ^ -1275999955) != 0) {
                              ;
                        }

                        mc.player.capabilities.isFlying = (boolean)((298229056 >>> 1 ^ 76031836) >> 1 ^ 104183038);
                        if (!"stringer is a good obfuscator".equals("idiot")) {
                              ;
                        }

                        ModeSetting var10000 = this.modeSetting;
                        if ((925591687 >> 1 >>> 2 & 92510529 & 13979536 ^ 8456448) == 0) {
                              ;
                        }

                        EntityPlayerSP var3;
                        double var10001;
                        double var10002;
                        if (var10000.activeMode == "Simple") {
                              if (mc.gameSettings.keyBindJump.isKeyDown()) {
                                    var3 = mc.player;
                                    var3.motionY += this.speedSet.getValue();
                              }

                              if (mc.gameSettings.keyBindSneak.isKeyDown()) {
                                    var3 = mc.player;
                                    if (!"intentMoment".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                                          ;
                                    }

                                    var10001 = var3.motionY;
                                    var10002 = this.speedSet.getValue();
                                    if ((1829642985 >> 2 >>> 1 ^ 228705373) == 0) {
                                          ;
                                    }

                                    var3.motionY = var10001 - var10002;
                              }
                        } else {
                              float var2;
                              if (this.modeSetting.activeMode == "Test") {
                                    mc.player.noClip = (boolean)(0 >> 4 >>> 2 >>> 2 ^ 1);
                                    mc.player.fallDistance = 0.0F;
                                    var3 = mc.player;
                                    int var4 = (0 >>> 4 | 581947599) ^ 581947598;
                                    if (((596487353 | 403977047) ^ 441581582 ^ -439445771) != 0) {
                                          ;
                                    }

                                    var3.onGround = (boolean)var4;
                                    if (!"buy a domain and everything else you need at namecheap.com".equals("yo mama name maurice")) {
                                          ;
                                    }

                                    mc.player.capabilities.isFlying = (boolean)(134333978 ^ 134333978);
                                    mc.player.motionX = 0.0D;
                                    var3 = mc.player;
                                    if ((61654553 >>> 4 & 354630 ^ 18496) == 0) {
                                          ;
                                    }

                                    var3.motionY = 0.0D;
                                    mc.player.motionZ = 0.0D;
                                    var2 = 0.2F;
                                    mc.player.jumpMovementFactor = var2;
                                    if (mc.gameSettings.keyBindJump.isKeyDown()) {
                                          var3 = mc.player;
                                          if (((14900073 >>> 2 ^ 2157297) >> 3 ^ 198533) == 0) {
                                                ;
                                          }

                                          var3.motionY += (double)var2;
                                    }

                                    if (!"yo mama name maurice".equals("your mom your dad the one you never had")) {
                                          ;
                                    }

                                    if (mc.gameSettings.keyBindSneak.isKeyDown()) {
                                          var3 = mc.player;
                                          var3.motionY -= (double)var2;
                                          if (((1902851589 >>> 4 << 2 | 256888422) ^ -2012558669) != 0) {
                                                ;
                                          }
                                    }

                                    if ((((1838938406 >> 2 << 3 | 2033539530) ^ 2065342460) >> 1 ^ -1072254437) == 0) {
                                          ;
                                    }
                              } else {
                                    Position var5;
                                    NetHandlerPlayClient var6;
                                    Minecraft var8;
                                    double var10;
                                    double var10003;
                                    Minecraft var10004;
                                    double var10005;
                                    if (this.modeSetting.activeMode == "PostTest") {
                                          if ((((1227841113 ^ 982757496) & 102996935 ^ 17305567 | 11406141) ^ 61771775) == 0) {
                                                ;
                                          }

                                          label259: {
                                                if (mc.player.collidedHorizontally) {
                                                      if (!"you probably spell youre as your".equals("please dont crack my plugin")) {
                                                            ;
                                                      }

                                                      if (spidiki5.isMoving()) {
                                                            if (fsjahfk4.isInsideBlock()) {
                                                                  var2 = mc.player.rotationYaw;
                                                                  var6 = mc.player.connection;
                                                                  var5 = new Position;
                                                                  var10003 = mc.player.posX;
                                                                  var10004 = mc;
                                                                  if ((1766763634 >>> 3 << 2 >> 2 ^ 220845454) == 0) {
                                                                        ;
                                                                  }

                                                                  var10003 -= var10004.player.motionX * 0.0425D;
                                                                  var10 = mc.player.posY;
                                                                  var10005 = mc.player.posZ - mc.player.motionZ * 0.0425D;
                                                                  EntityPlayerSP var10006 = mc.player;
                                                                  if ((18645506 >> 3 ^ 1141043425) != 0) {
                                                                        ;
                                                                  }

                                                                  var5.<init>(var10003, var10, var10005, var10006.onGround);
                                                                  var6.sendPacket(var5);
                                                                  var3 = mc.player;
                                                                  var10001 = mc.player.posX;
                                                                  if (((1246710769 << 2 & 157011092 | 21645214) ^ 102368564 ^ 129864409 ^ 150788211) == 0) {
                                                                        ;
                                                                  }

                                                                  var10001 += 0.42D * Math.cos(Math.toRadians((double)(var2 + 90.0F)));
                                                                  var10002 = mc.player.posY;
                                                                  EntityPlayerSP var9 = mc.player;
                                                                  if (((1263751610 | 209111039) & 991000115 ^ 185685555) == 0) {
                                                                        ;
                                                                  }

                                                                  var3.setPositionAndUpdate(var10001, var10002, var9.posZ + 0.42D * Math.sin(Math.toRadians((double)(var2 + 90.0F))));
                                                                  var8 = mc;
                                                                  if (((1747649137 ^ 1379231801) << 1 << 4 ^ 364636613 ^ 94747097 ^ 1406343452) == 0) {
                                                                        ;
                                                                  }

                                                                  var8.player.motionZ = 0.0D;
                                                            }

                                                            var8 = mc;
                                                            if (((188688081 >>> 4 | 8192829) ^ 16774013) == 0) {
                                                                  ;
                                                            }

                                                            var8.player.noClip = (boolean)((0 >>> 1 | 2060235273) ^ 2060235272);
                                                            break label259;
                                                      }
                                                }

                                                mc.player.noClip = (boolean)((471263517 << 4 >> 1 | 1485356940) ^ -121634836);
                                          }

                                          if (mc.gameSettings.keyBindJump.isKeyDown()) {
                                                var3 = mc.player;
                                                var3.motionY += this.speedSet.getValue();
                                          }

                                          if (mc.gameSettings.keyBindSneak.isKeyDown()) {
                                                var3 = mc.player;
                                                var10001 = var3.motionY;
                                                NumberSetting var7 = this.speedSet;
                                                if (!"stringer is a good obfuscator".equals("You're so fat whenever you go to the beach the tide comes in.")) {
                                                      ;
                                                }

                                                var3.motionY = var10001 - var7.getValue();
                                          }
                                    } else {
                                          if (!"you probably spell youre as your".equals("yo mama name maurice")) {
                                                ;
                                          }

                                          var8 = mc;
                                          if (!"i hope you catch fire ngl".equals("intentMoment")) {
                                                ;
                                          }

                                          if (var8.player.collidedHorizontally) {
                                                if (((90429858 >>> 3 << 1 & 8852227 | 4206) ^ 4718) == 0) {
                                                      ;
                                                }

                                                if (!fsjahfk4.isInsideBlock()) {
                                                      if (this.getFacingWithProperCapitals().equalsIgnoreCase("EAST")) {
                                                            var6 = mc.player.connection;
                                                            var5 = new Position;
                                                            if ((17670 >> 3 >>> 2 >>> 3 ^ 0 ^ -966748490) != 0) {
                                                                  ;
                                                            }

                                                            var5.<init>(mc.player.posX + 0.5D, mc.player.posY, mc.player.posZ, mc.player.onGround);
                                                            var6.sendPacket(var5);
                                                            var3 = mc.player;
                                                            if (((463984351 >>> 1 | 162380073) >>> 1 << 4 >>> 1 ^ 939519416) == 0) {
                                                                  ;
                                                            }

                                                            var10001 = mc.player.posX + 1.0D;
                                                            var10002 = mc.player.posY;
                                                            var10003 = mc.player.posZ;
                                                            if ((((1250852995 | 244028040) >> 2 ^ 8103410) & 185154997 ^ 50856208) == 0) {
                                                                  ;
                                                            }

                                                            var3.setPosition(var10001, var10002, var10003);
                                                            var6 = mc.player.connection;
                                                            var5 = new Position;
                                                            EntityPlayerSP var11 = mc.player;
                                                            if (!"stop skidding".equals("ape covered in human flesh")) {
                                                                  ;
                                                            }

                                                            var10 = var11.posY;
                                                            EntityPlayerSP var12 = mc.player;
                                                            if (!"please dont crack my plugin".equals("idiot")) {
                                                                  ;
                                                            }

                                                            var5.<init>(Double.POSITIVE_INFINITY, var10, var12.posZ, mc.player.onGround);
                                                            var6.sendPacket(var5);
                                                      } else {
                                                            if ((((1758573367 ^ 1190844631) >> 1 | 79137142) ^ 397917686) == 0) {
                                                                  ;
                                                            }

                                                            if (this.getFacingWithProperCapitals().equalsIgnoreCase("WEST")) {
                                                                  mc.player.connection.sendPacket(new Position(mc.player.posX - 0.5D, mc.player.posY, mc.player.posZ, mc.player.onGround));
                                                                  var3 = mc.player;
                                                                  var10001 = mc.player.posX;
                                                                  if (((1453711111 ^ 676336362) << 4 << 3 << 4 ^ -763177820) != 0) {
                                                                        ;
                                                                  }

                                                                  var3.setPosition(var10001 - 1.0D, mc.player.posY, mc.player.posZ);
                                                                  mc.player.connection.sendPacket(new Position(Double.POSITIVE_INFINITY, mc.player.posY, mc.player.posZ, mc.player.onGround));
                                                            } else if (this.getFacingWithProperCapitals().equalsIgnoreCase("NORTH")) {
                                                                  mc.player.connection.sendPacket(new Position(mc.player.posX, mc.player.posY, mc.player.posZ - 0.5D, mc.player.onGround));
                                                                  if (!"ape covered in human flesh".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                                                                        ;
                                                                  }

                                                                  mc.player.setPosition(mc.player.posX, mc.player.posY, mc.player.posZ - 1.0D);
                                                                  var6 = mc.player.connection;
                                                                  var5 = new Position;
                                                                  if (((598321216 ^ 247528089) >> 4 << 3 >> 3 ^ 47617325) == 0) {
                                                                        ;
                                                                  }

                                                                  if (!"please dont crack my plugin".equals("shitted on you harder than archybot")) {
                                                                        ;
                                                                  }

                                                                  var5.<init>(Double.POSITIVE_INFINITY, mc.player.posY, mc.player.posZ, mc.player.onGround);
                                                                  var6.sendPacket(var5);
                                                            } else {
                                                                  String var13 = this.getFacingWithProperCapitals();
                                                                  if (((320433191 >> 4 | 14658372) ^ -724555874) != 0) {
                                                                        ;
                                                                  }

                                                                  if (var13.equalsIgnoreCase("SOUTH")) {
                                                                        var8 = mc;
                                                                        if (!"Some babies were dropped on their heads but you were clearly thrown at a wall.".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                                                                              ;
                                                                        }

                                                                        var6 = var8.player.connection;
                                                                        var5 = new Position;
                                                                        if ((740328961 >>> 3 << 2 ^ -2146379696) != 0) {
                                                                              ;
                                                                        }

                                                                        var10003 = mc.player.posX;
                                                                        var10004 = mc;
                                                                        if ((71303360 ^ 929278787) != 0) {
                                                                              ;
                                                                        }

                                                                        var10 = var10004.player.posY;
                                                                        var10005 = mc.player.posZ + 0.5D;
                                                                        boolean var14 = mc.player.onGround;
                                                                        if (((1847022266 >>> 2 ^ 279049555 ^ 77056481) >> 4 << 4 ^ -554871555) != 0) {
                                                                              ;
                                                                        }

                                                                        var5.<init>(var10003, var10, var10005, var14);
                                                                        var6.sendPacket(var5);
                                                                        if (((1274390371 >> 4 & 51085312 | 694490) ^ -520683793) != 0) {
                                                                              ;
                                                                        }

                                                                        mc.player.setPosition(mc.player.posX, mc.player.posY, mc.player.posZ + 1.0D);
                                                                        var6 = mc.player.connection;
                                                                        if (((601141577 | 562874035) >>> 1 >> 2 >> 2 ^ 624552443) != 0) {
                                                                              ;
                                                                        }

                                                                        var6.sendPacket(new Position(Double.POSITIVE_INFINITY, mc.player.posY, mc.player.posZ, mc.player.onGround));
                                                                  }
                                                            }
                                                      }
                                                }
                                          }

                                          if (((512645245 >> 2 >>> 1 ^ 44852143) & 22947909 ^ 22816768) == 0) {
                                                ;
                                          }

                                          var8 = mc;
                                          if (((872057460 >>> 3 | 50017015) >>> 1 << 1 ^ 117404670) == 0) {
                                                ;
                                          }

                                          GameSettings var15 = var8.gameSettings;
                                          if ((((423110851 | 376926684) >>> 2 | 86923058) ^ 134209399) == 0) {
                                                ;
                                          }

                                          if (var15.keyBindJump.isKeyDown()) {
                                                if (!"yo mama name maurice".equals("please dont crack my plugin")) {
                                                      ;
                                                }

                                                var3 = mc.player;
                                                var10001 = var3.motionY;
                                                var10002 = this.speedSet.getValue();
                                                if (((1687633437 >>> 4 >>> 2 | 8168248) << 3 ^ 60649235 ^ -666864181) != 0) {
                                                      ;
                                                }

                                                var3.motionY = var10001 + var10002;
                                          }

                                          var8 = mc;
                                          if (((168151611 >> 2 & 4276952) >> 1 >>> 1 ^ 20642) == 0) {
                                                ;
                                          }

                                          if (var8.gameSettings.keyBindSneak.isKeyDown()) {
                                                var3 = mc.player;
                                                var3.motionY -= this.speedSet.getValue();
                                                if (((1321288449 << 4 >>> 2 | 819297221 | 664406301) & 36293704 ^ -565991595) != 0) {
                                                      ;
                                                }
                                          }
                                    }
                              }
                        }
                  }

                  if (!"idiot".equals("please take a shower")) {
                        ;
                  }

            }
      }
}
