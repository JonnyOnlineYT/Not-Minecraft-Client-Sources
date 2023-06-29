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
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.PlayerSPPushOutOfBlocksEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class fsjahfk4 extends Module {
      public NumberSetting speedSet = new NumberSetting("Speed", this, 0.5D, 0.3D, 5.0D, 0.1D);
      public ModeSetting modeSetting;

      public String getFacingWithProperCapitals() {
            String var1 = mc.getRenderViewEntity().getHorizontalFacing().getName();
            int var3 = 447319264 >> 2 >> 3 ^ -13978728;
            switch(var1.hashCode()) {
            case 3105789:
                  if (var1.equals("east")) {
                        var3 = (1 << 1 >> 1 | 0) & 0 ^ 3;
                  }
                  break;
            case 3645871:
                  if (!"you're dogshit".equals("buy a domain and everything else you need at namecheap.com")) {
                        ;
                  }

                  if (var1.equals("west")) {
                        var3 = ((0 >> 3 | 175813402 | 130540365) & 151279756 | 138705753) ^ 155482975;
                  }
                  break;
            case 105007365:
                  if (var1.equals("north")) {
                        var3 = 33619980 << 1 >> 3 ^ 8404995;
                  }
                  break;
            case 109627853:
                  if (((1455579965 ^ 1173982130) >>> 4 >>> 3 ^ 2521041) == 0) {
                        ;
                  }

                  if (var1.equals("south")) {
                        var3 = ((0 ^ 64716565) >> 2 | 9315044) ^ 16711652;
                  }
            }

            switch(var3) {
            case 0:
                  var1 = "North";
                  break;
            case 1:
                  var1 = "South";
                  break;
            case 2:
                  var1 = "West";
                  break;
            case 3:
                  var1 = "East";
                  if (!"yo mama name maurice".equals("your mom your dad the one you never had")) {
                        ;
                  }
            }

            return var1;
      }

      public static void setMoveSpeedAris(double var0) {
            double var2 = (double)Minecraft.getMinecraft().player.movementInput.moveForward;
            if ((((562012382 | 53169535) & 415409541) >> 2 & 256334 ^ 1161443970) != 0) {
                  ;
            }

            Minecraft var10000 = Minecraft.getMinecraft();
            if ((76005454 >> 2 ^ 19001363) == 0) {
                  ;
            }

            EntityPlayerSP var8 = var10000.player;
            if ((36693928 >> 2 << 1 ^ 13343924 ^ -1124557327) != 0) {
                  ;
            }

            double var4 = (double)var8.movementInput.moveStrafe;
            double var6 = (double)Minecraft.getMinecraft().player.rotationYaw;
            double var10;
            int var9 = (var10 = var2 - 0.0D) == 0.0D ? 0 : (var10 < 0.0D ? -1 : 1);
            if ((((1355992883 ^ 461439669 ^ 865314709) & 1296427539) >>> 3 ^ -556124692) != 0) {
                  ;
            }

            if (var9 != 0 || var4 != 0.0D) {
                  double var10001;
                  if (var2 != 0.0D) {
                        if (var4 > 0.0D) {
                              if (var2 > 0.0D) {
                                    var10001 = -45.0D;
                              } else {
                                    if (((190763879 >>> 2 | 38577221) ^ 388831110) != 0) {
                                          ;
                                    }

                                    var10001 = 45.0D;
                              }

                              var6 += var10001;
                        } else if (var4 < 0.0D) {
                              var6 += var2 > 0.0D ? 45.0D : -45.0D;
                        }

                        var4 = 0.0D;
                        if (var2 > 0.0D) {
                              var2 = 1.0D;
                        } else if (var2 < 0.0D) {
                              var2 = -1.0D;
                        }
                  }

                  if (spidiki5.isMoving()) {
                        var8 = mc.player;
                        var10001 = var2 * var0 * Math.cos(Math.toRadians(var6 + 88.0D));
                        double var10002 = var4 * var0;
                        if (!"minecraft".equals("please take a shower")) {
                              ;
                        }

                        double var10003 = var6 + 87.9000015258789D;
                        if (((1247685750 >> 4 | 14057847 | 48807695) ^ -1643967673) != 0) {
                              ;
                        }

                        var8.motionX = var10001 + var10002 * Math.sin(Math.toRadians(var10003));
                        var8 = mc.player;
                        var10001 = var2 * var0;
                        var10002 = Math.sin(Math.toRadians(var6 + 88.0D));
                        if ((((42342414 ^ 2442321) >> 1 | 3767932) ^ 24772223) == 0) {
                              ;
                        }

                        var10001 *= var10002;
                        if (((1456225386 >>> 4 >> 3 | 8959307) ^ 11386235) == 0) {
                              ;
                        }

                        var10002 = var4 * var0;
                        if (!"you're dogshit".equals("intentMoment")) {
                              ;
                        }

                        var8.motionZ = var10001 - var10002 * Math.cos(Math.toRadians(var6 + 87.9000015258789D));
                  }
            }

            if (((939787600 << 1 | 1124051679) >>> 3 ^ 241169759) == 0) {
                  ;
            }

      }

      public float getDirection() {
            float var1 = mc.player.rotationYaw;
            var1 = MathHelper.wrapDegrees(var1);
            if ((78861180 << 4 << 4 >> 1 ^ -643187200) == 0) {
                  ;
            }

            return var1 * 0.017453292F;
      }

      public static boolean isInsideBlock() {
            EntityPlayerSP var10000 = Minecraft.getMinecraft().player;
            if (((545261824 | 84090822) ^ 265682464 ^ -375683826) != 0) {
                  ;
            }

            for(int var0 = MathHelper.floor(var10000.getEntityBoundingBox().minX); var0 < MathHelper.floor(Minecraft.getMinecraft().player.getEntityBoundingBox().maxX) + ((0 >> 1 & 388868224) >> 4 ^ 1); ++var0) {
                  int var1 = MathHelper.floor(Minecraft.getMinecraft().player.getEntityBoundingBox().minY);

                  while(true) {
                        int var10001 = MathHelper.floor(Minecraft.getMinecraft().player.getEntityBoundingBox().maxY);
                        int var10002 = (0 & 1918243344 | 1426197838) ^ 1426197839;
                        if (!"ape covered in human flesh".equals("stringer is a good obfuscator")) {
                              ;
                        }

                        if (var1 >= var10001 + var10002) {
                              break;
                        }

                        int var2 = MathHelper.floor(Minecraft.getMinecraft().player.getEntityBoundingBox().minZ);

                        while(true) {
                              if ((1742362662 << 2 << 2 << 2 ^ 481691627 ^ -1611313787) != 0) {
                                    ;
                              }

                              var10001 = MathHelper.floor(Minecraft.getMinecraft().player.getEntityBoundingBox().maxZ);
                              var10002 = ((0 | 276134566) ^ 243664980 ^ 38139154) << 4 ^ -882344447;
                              if ((((628268378 ^ 562668262 | 30134688) & 76074722) >>> 3 ^ 9509332) == 0) {
                                    ;
                              }

                              if (var2 >= var10001 + var10002) {
                                    ++var1;
                                    break;
                              }

                              Block var3 = Minecraft.getMinecraft().world.getBlockState(new BlockPos(var0, var1, var2)).getBlock();
                              if (var3 != null && !(var3 instanceof BlockAir)) {
                                    WorldClient var8 = Minecraft.getMinecraft().world;
                                    BlockPos var9 = new BlockPos;
                                    if ((936649589 >> 3 >> 1 >>> 3 & 1184011 ^ 131074) == 0) {
                                          ;
                                    }

                                    var9.<init>(var0, var1, var2);
                                    AxisAlignedBB var4 = var3.getCollisionBoundingBox(var8.getBlockState(var9), Minecraft.getMinecraft().world, new BlockPos(var0, var1, var2));
                                    if ((1297813545 >> 2 >>> 4 << 4 ^ 324453376) == 0) {
                                          ;
                                    }

                                    if (((672312357 >>> 2 << 1 | 10620822) ^ 346775446) == 0) {
                                          ;
                                    }

                                    boolean var5 = var3 instanceof BlockHopper;
                                    if (((33642672 ^ 12671656) >>> 1 >> 3 ^ -1549697602) != 0) {
                                          ;
                                    }

                                    if (var5) {
                                          AxisAlignedBB var6 = new AxisAlignedBB;
                                          double var10 = (double)var0;
                                          double var10003 = (double)var1;
                                          double var10004 = (double)var2;
                                          int var10005 = var0 + ((0 >>> 3 >> 3 << 2 | 148592204) ^ 148592205);
                                          if (((4198678 ^ 4077520) >>> 4 ^ 1179493826) != 0) {
                                                ;
                                          }

                                          double var11 = (double)var10005;
                                          double var10006 = (double)(var1 + (0 >> 4 << 2 ^ 1));
                                          if (((578762686 ^ 91664875) >> 4 >>> 3 ^ 705815239) != 0) {
                                                ;
                                          }

                                          var6.<init>(var10, var10003, var10004, var11, var10006, (double)(var2 + ((((0 & 1322779971) >>> 4 ^ 1192478998) & 204207024) << 2 ^ 269419585)));
                                          var4 = var6;
                                    }

                                    if (var4 != null) {
                                          Minecraft var7 = Minecraft.getMinecraft();
                                          if (!"Some babies were dropped on their heads but you were clearly thrown at a wall.".equals("stringer is a good obfuscator")) {
                                                ;
                                          }

                                          if (var7.player.getEntityBoundingBox().intersects(var4)) {
                                                return (boolean)(0 << 3 << 3 >> 3 ^ 1);
                                          }
                                    }
                              }

                              ++var2;
                        }
                  }
            }

            return (boolean)((783712065 ^ 77520051) << 2 ^ -1465741368);
      }

      @SubscribeEvent
      public void onPushOutOfBlocks(PlayerSPPushOutOfBlocksEvent var1) {
            var1.setCanceled((boolean)((0 >> 4 >>> 3 & 2031868865 | 835443637) << 2 ^ -953192747));
      }

      public void onDisable() {
            EntityPlayerSP var10000 = mc.player;
            if ((((268925929 ^ 79104803) >> 2 | 64446336) ^ 134176690) == 0) {
                  ;
            }

            if (var10000 != null && mc.world != null) {
                  if ((((702316311 ^ 455075998) >>> 4 & 16072608) >>> 1 ^ 1186960) == 0) {
                        ;
                  }

                  var10000 = mc.player;
                  int var10001 = (1793647252 >> 4 ^ 98205682 | 50044786) ^ 42979627 ^ 24129232;
                  if (!"please dont crack my plugin".equals("yo mama name maurice")) {
                        ;
                  }

                  var10000.noClip = (boolean)var10001;
                  NetHandlerPlayClient var1 = mc.player.connection;
                  PositionRotation var2 = new PositionRotation;
                  Minecraft var10003 = mc;
                  if (!"please take a shower".equals("you're dogshit")) {
                        ;
                  }

                  EntityPlayerSP var3 = var10003.player;
                  if (!"intentMoment".equals("minecraft")) {
                        ;
                  }

                  double var4 = var3.posX;
                  double var10004 = mc.player.getEntityBoundingBox().minY;
                  double var10005 = mc.player.posZ;
                  EntityPlayerSP var10006 = mc.player;
                  if (((1305575609 ^ 281406121 ^ 50429102) << 1 ^ -1138086532) == 0) {
                        ;
                  }

                  float var5 = var10006.cameraYaw;
                  Minecraft var10007 = mc;
                  if ((593703709 >>> 3 & 54869626 ^ 251686 ^ 4714820) == 0) {
                        ;
                  }

                  var2.<init>(var4, var10004, var10005, var5, var10007.player.cameraPitch, mc.player.onGround);
                  var1.sendPacket(var2);
                  super.onDisable();
            } else {
                  if (((1517918058 >> 2 ^ 40516635) << 2 ^ 1406232324) == 0) {
                        ;
                  }

            }
      }

      public fsjahfk4() {
            super("NoClip", "no clips", (603917728 >>> 4 | 23312653) ^ 58718687, Module.Category.MOVEMENT);
            String[] var10006 = new String[0 >>> 2 >> 1 >> 4 ^ 4];
            var10006[(1260794698 | 194641971 | 902489115) << 4 ^ 1781073000 ^ -1782156328] = "Simple";
            var10006[(0 << 3 << 1 & 1571320035) >> 3 ^ 1] = "Test";
            var10006[(0 | 1328709314) >> 1 >>> 2 ^ 166088666] = "PostTest";
            var10006[((1 ^ 0) & 0) << 1 >> 2 ^ 3] = "PreTest";
            this.modeSetting = new ModeSetting("Mode", this, "Simple", var10006);
            Setting[] var10001 = new Setting[1 << 4 & 9 & 1892097249 ^ 2];
            int var10003 = 1028253286 >> 2 ^ 9889885 ^ 43834051 ^ 223890183;
            if (((716080884 | 701743560) << 4 & 945232616 ^ 945035968) == 0) {
                  ;
            }

            var10001[var10003] = this.speedSet;
            var10001[(0 | 910409709 | 718803699) ^ 1054588926] = this.modeSetting;
            this.addSettings(var10001);
      }

      @SubscribeEvent
      public void onLivingUpdate(LivingUpdateEvent var1) {
            EntityPlayerSP var10000 = mc.player;
            if (!"ape covered in human flesh".equals("stop skidding")) {
                  ;
            }

            if (var10000 != null && mc.world != null) {
                  if (!"nefariousMoment".equals("minecraft")) {
                        ;
                  }

                  if (!"please dont crack my plugin".equals("your mom your dad the one you never had")) {
                        ;
                  }

                  if (mc.player != null) {
                        mc.player.noClip = (boolean)((0 ^ 862681420) >>> 3 << 2 ^ 431340709);
                        mc.player.motionY = 0.0D;
                        mc.player.onGround = (boolean)((471736114 ^ 35542030 | 257673346) >> 3 ^ 65763703);
                        mc.player.capabilities.isFlying = (boolean)(1090818627 << 3 << 2 << 2 ^ 1053470275 ^ -1132228669);
                        double var10001;
                        if (this.modeSetting.activeMode == "Simple") {
                              GameSettings var3 = mc.gameSettings;
                              if (!"shitted on you harder than archybot".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                                    ;
                              }

                              if (var3.keyBindJump.isKeyDown()) {
                                    var10000 = mc.player;
                                    var10001 = var10000.motionY + this.speedSet.getValue();
                                    if (!"minecraft".equals("please get a girlfriend and stop cracking plugins")) {
                                          ;
                                    }

                                    var10000.motionY = var10001;
                              }

                              if (((500643019 >>> 4 ^ 12750153) << 2 ^ -429225457) != 0) {
                                    ;
                              }

                              if (mc.gameSettings.keyBindSneak.isKeyDown()) {
                                    if ((((1341157690 ^ 58926372) << 3 >>> 4 & 46909625) << 4 ^ 546865296) == 0) {
                                          ;
                                    }

                                    var10000 = mc.player;
                                    if (!"nefariousMoment".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                                          ;
                                    }

                                    var10000.motionY -= this.speedSet.getValue();
                              }
                        } else {
                              float var2;
                              Minecraft var4;
                              if (this.modeSetting.activeMode == "Test") {
                                    var4 = mc;
                                    if (!"idiot".equals("please go outside")) {
                                          ;
                                    }

                                    var4.player.noClip = (boolean)(0 & 259204308 ^ 1451520529 ^ 1289645231 ^ 218433934 ^ 392107825);
                                    mc.player.fallDistance = 0.0F;
                                    mc.player.onGround = (boolean)((0 & 1907735662 & 1436061812) >> 1 >> 4 ^ 1);
                                    mc.player.capabilities.isFlying = (boolean)((210650124 ^ 32302105) >> 2 >> 4 ^ 3508864);
                                    mc.player.motionX = 0.0D;
                                    mc.player.motionY = 0.0D;
                                    mc.player.motionZ = 0.0D;
                                    if ((1348764314 >>> 2 >> 1 >>> 4 ^ 10537221) == 0) {
                                          ;
                                    }

                                    var2 = 0.2F;
                                    mc.player.jumpMovementFactor = var2;
                                    if (mc.gameSettings.keyBindJump.isKeyDown()) {
                                          var10000 = mc.player;
                                          if (!"idiot".equals("i hope you catch fire ngl")) {
                                                ;
                                          }

                                          var10000.motionY += (double)var2;
                                    }

                                    if (mc.gameSettings.keyBindSneak.isKeyDown()) {
                                          if (((2131979441 ^ 1131993535 | 246207433) ^ 1055913935) == 0) {
                                                ;
                                          }

                                          if (!"please dont crack my plugin".equals("please take a shower")) {
                                                ;
                                          }

                                          var10000 = mc.player;
                                          var10001 = var10000.motionY - (double)var2;
                                          if (((1316990949 >>> 2 >> 1 | 10213167) ^ -1772731143) != 0) {
                                                ;
                                          }

                                          var10000.motionY = var10001;
                                    }
                              } else {
                                    ModeSetting var7 = this.modeSetting;
                                    if (((659563586 >> 3 >>> 4 | 4814897) << 1 & 3689555 ^ 1589330) == 0) {
                                          ;
                                    }

                                    Position var5;
                                    NetHandlerPlayClient var9;
                                    double var10002;
                                    double var10003;
                                    double var10004;
                                    double var10005;
                                    if (var7.activeMode == "PostTest") {
                                          if (mc.player.collidedHorizontally && spidiki5.isMoving()) {
                                                if (fsjahfk4.isInsideBlock()) {
                                                      var2 = mc.player.rotationYaw;
                                                      var9 = mc.player.connection;
                                                      var5 = new Position;
                                                      if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("stringer is a good obfuscator")) {
                                                            ;
                                                      }

                                                      var10003 = mc.player.posX;
                                                      var10004 = mc.player.motionX * 0.0425D;
                                                      if (!"stringer is a good obfuscator".equals("stop skidding")) {
                                                            ;
                                                      }

                                                      var10003 -= var10004;
                                                      var10004 = mc.player.posY;
                                                      var10005 = mc.player.posZ;
                                                      double var10006 = mc.player.motionZ;
                                                      if ((((1053099556 | 11447326) >> 1 | 462851425) << 1 ^ 1811447216) != 0) {
                                                            ;
                                                      }

                                                      if (((103175994 >>> 4 | 2998511) ^ 5580529 ^ -457840746) != 0) {
                                                            ;
                                                      }

                                                      var5.<init>(var10003, var10004, var10005 - var10006 * 0.0425D, mc.player.onGround);
                                                      var9.sendPacket(var5);
                                                      var10000 = mc.player;
                                                      Minecraft var6 = mc;
                                                      if (((91442277 | 12419171 | 11923972) ^ 2194890) != 0) {
                                                            ;
                                                      }

                                                      var10001 = var6.player.posX;
                                                      var10003 = (double)(var2 + 90.0F);
                                                      if (((295204756 | 68214522 | 103614956) >> 2 ^ 99581951) == 0) {
                                                            ;
                                                      }

                                                      var10001 += 0.42D * Math.cos(Math.toRadians(var10003));
                                                      var10002 = mc.player.posY;
                                                      var10003 = mc.player.posZ;
                                                      if (!"minecraft".equals("shitted on you harder than archybot")) {
                                                            ;
                                                      }

                                                      var10000.setPositionAndUpdate(var10001, var10002, var10003 + 0.42D * Math.sin(Math.toRadians((double)(var2 + 90.0F))));
                                                      if ((405054474 ^ 66300796 ^ 467079030) == 0) {
                                                            ;
                                                      }

                                                      mc.player.motionZ = 0.0D;
                                                }

                                                var4 = mc;
                                                if (!"stringer is a good obfuscator".equals("your mom your dad the one you never had")) {
                                                      ;
                                                }

                                                var4.player.noClip = (boolean)(((0 >> 2 & 1043988432) >>> 4 ^ 69362591) << 2 ^ 277450365);
                                          } else {
                                                mc.player.noClip = (boolean)(1922219356 << 4 >>> 3 & 54742186 ^ 16861352);
                                          }

                                          if (mc.gameSettings.keyBindJump.isKeyDown()) {
                                                var10000 = mc.player;
                                                var10000.motionY += this.speedSet.getValue();
                                          }

                                          boolean var11 = mc.gameSettings.keyBindSneak.isKeyDown();
                                          if (((138306017 << 3 & 472157736) >>> 3 ^ 649768232) != 0) {
                                                ;
                                          }

                                          if (var11) {
                                                var10000 = mc.player;
                                                var10000.motionY -= this.speedSet.getValue();
                                          }
                                    } else {
                                          if (mc.player.collidedHorizontally && !fsjahfk4.isInsideBlock()) {
                                                if ((606286470 >> 4 << 2 >> 4 & 6454034 & 538 ^ 2) == 0) {
                                                      ;
                                                }

                                                String var12 = this.getFacingWithProperCapitals();
                                                if (((1646010672 >>> 1 | 429466069) & 795677655 ^ -99756397) != 0) {
                                                      ;
                                                }

                                                if (var12.equalsIgnoreCase("EAST")) {
                                                      mc.player.connection.sendPacket(new Position(mc.player.posX + 0.5D, mc.player.posY, mc.player.posZ, mc.player.onGround));
                                                      var10000 = mc.player;
                                                      EntityPlayerSP var10 = mc.player;
                                                      if (!"you probably spell youre as your".equals("shitted on you harder than archybot")) {
                                                            ;
                                                      }

                                                      var10001 = var10.posX + 1.0D;
                                                      EntityPlayerSP var8 = mc.player;
                                                      if ((268937134 >>> 1 & 108488819 ^ 213075) == 0) {
                                                            ;
                                                      }

                                                      var10000.setPosition(var10001, var8.posY, mc.player.posZ);
                                                      var9 = mc.player.connection;
                                                      var5 = new Position;
                                                      if (((((503121598 | 304838471) & 219366292 | 207103388) >>> 4 | 1620554) ^ -1893813589) != 0) {
                                                            ;
                                                      }

                                                      var5.<init>(Double.POSITIVE_INFINITY, mc.player.posY, mc.player.posZ, mc.player.onGround);
                                                      var9.sendPacket(var5);
                                                } else {
                                                      if ((1660623331 << 1 ^ 1518029463 ^ -1618118319) == 0) {
                                                            ;
                                                      }

                                                      if (this.getFacingWithProperCapitals().equalsIgnoreCase("WEST")) {
                                                            if (((2014933763 | 71626463 | 702332996) ^ 2111700959) == 0) {
                                                                  ;
                                                            }

                                                            var9 = mc.player.connection;
                                                            var5 = new Position;
                                                            var10003 = mc.player.posX;
                                                            if (!"please take a shower".equals("You're so fat whenever you go to the beach the tide comes in.")) {
                                                                  ;
                                                            }

                                                            var5.<init>(var10003 - 0.5D, mc.player.posY, mc.player.posZ, mc.player.onGround);
                                                            var9.sendPacket(var5);
                                                            mc.player.setPosition(mc.player.posX - 1.0D, mc.player.posY, mc.player.posZ);
                                                            var10000 = mc.player;
                                                            if (((67969036 << 3 >>> 1 ^ 42380210) & 170252791 ^ 35654018) == 0) {
                                                                  ;
                                                            }

                                                            var9 = var10000.connection;
                                                            var5 = new Position;
                                                            if ((618774688 >>> 3 >> 1 >> 4 ^ -84784383) != 0) {
                                                                  ;
                                                            }

                                                            var10004 = mc.player.posY;
                                                            Minecraft var13 = mc;
                                                            if (!"shitted on you harder than archybot".equals("idiot")) {
                                                                  ;
                                                            }

                                                            EntityPlayerSP var14 = var13.player;
                                                            if (((1093562015 >> 3 ^ 83066829) >> 1 ^ 107698447) == 0) {
                                                                  ;
                                                            }

                                                            var5.<init>(Double.POSITIVE_INFINITY, var10004, var14.posZ, mc.player.onGround);
                                                            var9.sendPacket(var5);
                                                            if (!"idiot".equals("intentMoment")) {
                                                                  ;
                                                            }
                                                      } else {
                                                            var12 = this.getFacingWithProperCapitals();
                                                            if (((1216105847 | 525479411) >>> 3 & 146354393 ^ 145293464) == 0) {
                                                                  ;
                                                            }

                                                            if (var12.equalsIgnoreCase("NORTH")) {
                                                                  var9 = mc.player.connection;
                                                                  if (((157952200 | 129516293 | 10597489) & 246255391 & 49079728 ^ 44598544) == 0) {
                                                                        ;
                                                                  }

                                                                  var5 = new Position;
                                                                  var10003 = mc.player.posX;
                                                                  var10004 = mc.player.posY;
                                                                  var10005 = mc.player.posZ - 0.5D;
                                                                  Minecraft var15 = mc;
                                                                  if ((1590162227 >>> 4 & 38598534 ^ 1044404 ^ 4430006) == 0) {
                                                                        ;
                                                                  }

                                                                  var5.<init>(var10003, var10004, var10005, var15.player.onGround);
                                                                  var9.sendPacket(var5);
                                                                  if (((70050274 | 46953144) ^ -281258850) != 0) {
                                                                        ;
                                                                  }

                                                                  if (!"idiot".equals("you probably spell youre as your")) {
                                                                        ;
                                                                  }

                                                                  mc.player.setPosition(mc.player.posX, mc.player.posY, mc.player.posZ - 1.0D);
                                                                  var9 = mc.player.connection;
                                                                  var5 = new Position;
                                                                  var10004 = mc.player.posY;
                                                                  var10005 = mc.player.posZ;
                                                                  var15 = mc;
                                                                  if (((1431467572 >> 1 | 173215738) ^ -1833611810) != 0) {
                                                                        ;
                                                                  }

                                                                  var5.<init>(Double.POSITIVE_INFINITY, var10004, var10005, var15.player.onGround);
                                                                  var9.sendPacket(var5);
                                                            } else {
                                                                  if (((1926433269 >> 1 | 129278194) << 1 ^ 136034316) != 0) {
                                                                        ;
                                                                  }

                                                                  var12 = this.getFacingWithProperCapitals();
                                                                  if (((134230656 >> 1 | 36437713) ^ 103546833) == 0) {
                                                                        ;
                                                                  }

                                                                  if (var12.equalsIgnoreCase("SOUTH")) {
                                                                        if (((939847444 << 3 >>> 4 ^ 116205210) >> 2 ^ 45864004) == 0) {
                                                                              ;
                                                                        }

                                                                        mc.player.connection.sendPacket(new Position(mc.player.posX, mc.player.posY, mc.player.posZ + 0.5D, mc.player.onGround));
                                                                        var4 = mc;
                                                                        if ((512061656 << 2 ^ 635400676 ^ 588009636) != 0) {
                                                                              ;
                                                                        }

                                                                        var4.player.setPosition(mc.player.posX, mc.player.posY, mc.player.posZ + 1.0D);
                                                                        var9 = mc.player.connection;
                                                                        var5 = new Position;
                                                                        var10004 = mc.player.posY;
                                                                        if (((330711119 >> 1 >>> 2 | 10829595) ^ 1535092915) != 0) {
                                                                              ;
                                                                        }

                                                                        var5.<init>(Double.POSITIVE_INFINITY, var10004, mc.player.posZ, mc.player.onGround);
                                                                        var9.sendPacket(var5);
                                                                  }
                                                            }
                                                      }
                                                }
                                          }

                                          if (mc.gameSettings.keyBindJump.isKeyDown()) {
                                                if ((1049412 >> 2 >>> 4 << 4 << 2 ^ 1049408) == 0) {
                                                      ;
                                                }

                                                var10000 = mc.player;
                                                var10001 = var10000.motionY;
                                                var10002 = this.speedSet.getValue();
                                                if (((1147420488 >>> 3 << 4 | 727500840) ^ -1411580232) == 0) {
                                                      ;
                                                }

                                                var10000.motionY = var10001 + var10002;
                                          }

                                          if (mc.gameSettings.keyBindSneak.isKeyDown()) {
                                                var10000 = mc.player;
                                                var10000.motionY -= this.speedSet.getValue();
                                          }
                                    }
                              }
                        }
                  }

            }
      }
}
