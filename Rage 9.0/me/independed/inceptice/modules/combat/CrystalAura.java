//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.combat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import me.independed.inceptice.friends.Friend;
import me.independed.inceptice.friends.FriendManager;
import me.independed.inceptice.modules.Module;
import me.independed.inceptice.settings.BooleanSetting;
import me.independed.inceptice.settings.NumberSetting;
import me.independed.inceptice.settings.Setting;
import me.independed.inceptice.util.TimerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.potion.Potion;
import net.minecraft.util.CombatRules;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class CrystalAura extends Module {
      public BooleanSetting rotateSetting;
      public NumberSetting minDmg;
      private BlockPos render;
      private int oldSlot;
      private final ArrayList PlacedCrystals;
      public BooleanSetting autoSwitch;
      private static boolean isSpoofingAngles;
      public boolean isActive;
      public NumberSetting attackSpeed;
      public NumberSetting placeRange;
      public NumberSetting maxSelfDmg;
      private static boolean togglePitch;
      public NumberSetting breakRange;
      private boolean switchCooldown;
      private boolean isAttacking;
      private TimerUtil timer;
      public BooleanSetting placeCrystal;
      public BooleanSetting raytrace;
      private static double yaw;
      public BooleanSetting noGapSwitch;
      public NumberSetting facePlaceValue;
      public NumberSetting enemyRange;
      private static double pitch;
      private EnumFacing enumFacing;
      private int newSlot;

      private boolean crystalCheck(Entity var1) {
            boolean var10000 = var1 instanceof EntityEnderCrystal;
            if ((((457210549 | 219439993) ^ 263550723 | 82504384) ^ 1049879753) != 0) {
                  ;
            }

            if (!var10000) {
                  return (boolean)((1669490762 | 1146225387) ^ 1096536375 ^ 646560732);
            } else {
                  if (!"you're dogshit".equals("please take a shower")) {
                        ;
                  }

                  int var2 = (0 >>> 3 | 1799408496) << 2 ^ -1392300607;
                  if (((94371091 >>> 2 | 2172960) & 8927222 ^ 14180) == 0) {
                        ;
                  }

                  return (boolean)var2;
            }
      }

      private boolean attackCheck(Entity var1) {
            Iterator var10000 = FriendManager.friends.iterator();
            if (!"stringer is a good obfuscator".equals("please get a girlfriend and stop cracking plugins")) {
                  ;
            }

            Iterator var2 = var10000;

            Friend var3;
            String var4;
            do {
                  if (!var2.hasNext()) {
                        if (((8128533 >>> 3 | 876541) << 1 ^ 2080766) == 0) {
                              ;
                        }

                        return (boolean)(0 << 2 & 2118151518 & 1005761791 & 1907687001 ^ 1);
                  }

                  var3 = (Friend)var2.next();
                  var4 = var1.getName();
                  if (!"please go outside".equals("buy a domain and everything else you need at namecheap.com")) {
                        ;
                  }
            } while(var4 != var3.name);

            if (((391286474 >> 4 | 12268893) << 2 ^ 35777447 ^ -1315744393) != 0) {
                  ;
            }

            return (boolean)(0 ^ 0);
      }

      public boolean canPlaceCrystal(BlockPos var1) {
            if (((613172246 ^ 587876816 | 17688188) ^ -196253129) != 0) {
                  ;
            }

            int var10001 = 1073744949 >> 2 << 3 & 1827134649 & 3557 ^ 2080;
            int var10002 = ((0 | 472856576) << 4 | 78723243) ^ -957106006;
            if ((1485192972 >> 4 << 2 << 1 >>> 2 >>> 2 ^ 46412280) == 0) {
                  ;
            }

            BlockPos var10000 = var1.add(var10001, var10002, ((1593253290 | 963001461) ^ 443218457 | 661540427) ^ 1744818159);
            if (((1678199251 ^ 1294819365 | 530407795) ^ 1069509111) == 0) {
                  ;
            }

            int var5;
            label41: {
                  BlockPos var2 = var10000;
                  BlockPos var3 = var1.add((1996043973 << 4 >>> 1 | 243802966 | 771277491) ^ 1073346559, ((1 | 0) >>> 1 & 223128383 | 235142663) ^ 235142661, ((147728 ^ 'щ?В') & 65768) >> 4 ^ 0);
                  if (mc.world.getBlockState(var1).getBlock() != Blocks.BEDROCK) {
                        if (mc.world.getBlockState(var1).getBlock() != Blocks.OBSIDIAN) {
                              break label41;
                        }

                        if (!"stop skidding".equals("ape covered in human flesh")) {
                              ;
                        }
                  }

                  if (mc.world.getBlockState(var2).getBlock() == Blocks.AIR && mc.world.getBlockState(var3).getBlock() == Blocks.AIR && mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(var2)).isEmpty()) {
                        WorldClient var4 = mc.world;
                        AxisAlignedBB var6 = new AxisAlignedBB(var3);
                        if ((((1994195147 | 89181014) ^ 952934231) >> 4 ^ 82908488) == 0) {
                              ;
                        }

                        if (var4.getEntitiesWithinAABB(Entity.class, var6).isEmpty()) {
                              var5 = (0 | 1175652140) & 1019791718 ^ 67109157;
                              return (boolean)var5;
                        }
                  }
            }

            if (((2062345501 ^ 1514385129) & 120834206 ^ 2312340) == 0) {
                  ;
            }

            var5 = (1605780407 | 300390800 | 702372147) ^ 2147474359;
            return (boolean)var5;
      }

      private void breakCrystal(EntityEnderCrystal var1) {
            mc.playerController.attackEntity(mc.player, var1);
            if (!"You're so fat whenever you go to the beach the tide comes in.".equals("please get a girlfriend and stop cracking plugins")) {
                  ;
            }

            this.swingArm();
      }

      private static float getDamageMultiplied(float var0) {
            if ((((82750350 | 77636797) >>> 2 << 1 & 5232862) << 4 ^ 25392524) != 0) {
                  ;
            }

            int var1 = mc.world.getDifficulty().getId();
            return var0 * (var1 == 0 ? 0.0F : (var1 == (1 << 3 >>> 1 & 1 ^ 2) ? 1.0F : (var1 == (0 << 3 >>> 3 >>> 1 ^ 1674062683 ^ 1674062682) ? 0.5F : 1.5F)));
      }

      @SubscribeEvent
      public void onPlayerTick(PlayerTickEvent var1) throws ConcurrentModificationException {
            EntityPlayerSP var10000 = mc.player;
            if ((1429797969 >>> 1 >>> 2 ^ 178724746) == 0) {
                  ;
            }

            if (var10000 != null) {
                  if (mc.world != null) {
                        Stream var32 = mc.world.loadedEntityList.stream();
                        Predicate var10001 = (var0) -> {
                              return var0 instanceof EntityEnderCrystal;
                        };
                        if (((780536907 | 305579409 | 877862870) & 662828828 ^ 646043420) == 0) {
                              ;
                        }

                        var32 = var32.filter(var10001).filter((var1x) -> {
                              double var10000 = (double)mc.player.getDistance(var1x);
                              double var10001 = this.breakRange.getValue();
                              if ((309506475 << 1 << 4 >> 3 ^ 571754147) != 0) {
                                    ;
                              }

                              return (boolean)(var10000 <= var10001 ? ((0 >>> 1 ^ 222383153) >>> 4 | 9935400) ^ 14130922 : 1373598285 >>> 4 >> 1 << 4 >> 2 >> 4 ^ 10731236);
                        }).filter((var1x) -> {
                              if ((((3686402 ^ 1833710) >> 4 & 7067) >> 1 ^ 3525) == 0) {
                                    ;
                              }

                              return this.crystalCheck(var1x);
                        });
                        Function var34 = (var0) -> {
                              return (EntityEnderCrystal)var0;
                        };
                        if (((1329626012 ^ 1250489168) & 44606542 & 5495102 ^ -2002702360) != 0) {
                              ;
                        }

                        Optional var33 = var32.map(var34).min(Comparator.comparing((var0) -> {
                              float var10000 = mc.player.getDistance(var0);
                              if (!"ape covered in human flesh".equals("please take a shower")) {
                                    ;
                              }

                              return Float.valueOf(var10000);
                        }));
                        if (((1708649744 >>> 2 >>> 4 | 535343) >> 3 ^ -938387043) != 0) {
                              ;
                        }

                        EntityEnderCrystal var2 = (EntityEnderCrystal)var33.orElse((Object)null);
                        int var3;
                        Minecraft var36;
                        int var38;
                        double var41;
                        if (var2 != null) {
                              if (!this.isAttacking) {
                                    var36 = mc;
                                    if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("ape covered in human flesh")) {
                                          ;
                                    }

                                    this.oldSlot = var36.player.inventory.currentItem;
                                    this.isAttacking = (boolean)((0 >> 3 ^ 1065291313) >> 2 ^ 266322829);
                              }

                              this.newSlot = (802014150 ^ 360890390) << 4 ^ 30914553 ^ 1524135174;
                              var3 = (10510410 ^ 9013600) >>> 2 << 3 & 4405734 ^ 4395072;

                              while(true) {
                                    var38 = (6 & 2) >>> 1 >>> 1 >>> 3 ^ 401313089 ^ 401313096;
                                    if (((489106442 ^ 338057478 | 98599573) ^ 1015748971) != 0) {
                                          ;
                                    }

                                    if (var3 >= var38) {
                                          break;
                                    }

                                    ItemStack var4 = mc.player.inventory.getStackInSlot(var3);
                                    if (var4 != ItemStack.EMPTY) {
                                          if (var4.getItem() instanceof ItemSword) {
                                                this.newSlot = var3;
                                                break;
                                          }

                                          if (var4.getItem() instanceof ItemTool) {
                                                this.newSlot = var3;
                                                if ((315062287 >>> 3 << 2 & 70806079 & 1762184 ^ 8192) == 0) {
                                                      ;
                                                }
                                                break;
                                          }
                                    }

                                    ++var3;
                              }

                              if (this.newSlot != (1180814496 << 3 << 1 >> 3 >>> 1 ^ -107072673)) {
                                    mc.player.inventory.currentItem = this.newSlot;
                                    this.switchCooldown = (boolean)((0 | 1414411964) << 1 >>> 4 ^ 176801494);
                              }

                              if (this.timer.hasReached(this.attackSpeed.getValue())) {
                                    this.timer.reset();
                                    if ((306244465 >>> 1 >> 4 >>> 2 ^ 2392534) == 0) {
                                          ;
                                    }

                                    if (((1085988905 >> 3 | 12190008 | 54229036) ^ 197098813) == 0) {
                                          ;
                                    }

                                    if (this.rotateSetting.isEnabled()) {
                                          var41 = var2.posX;
                                          double var10002 = var2.posY;
                                          double var10003 = var2.posZ;
                                          Minecraft var10004 = mc;
                                          if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("please go outside")) {
                                                ;
                                          }

                                          EntityPlayerSP var50 = var10004.player;
                                          if (((158623126 >>> 1 | 30366089) ^ 46430132 ^ 121310335) == 0) {
                                                ;
                                          }

                                          this.lookAtPacket(var41, var10002, var10003, var50);
                                    }

                                    var38 = 428104658 >>> 2 << 2 ^ 285128119 ^ 159051367;
                                    if ((600932936 << 3 >>> 1 ^ 256248096) == 0) {
                                          ;
                                    }

                                    this.isActive = (boolean)var38;
                                    if ((1262769834 >>> 4 & 29772572 ^ -2074518133) != 0) {
                                          ;
                                    }
                              }
                        } else {
                              if (((1302579402 | 1101942652) << 4 ^ 193540718 ^ -780808818) == 0) {
                                    ;
                              }

                              CrystalAura.resetRotation();
                              if (this.oldSlot != ((1770976089 >>> 3 | 128666071) ^ -263970816)) {
                                    mc.player.inventory.currentItem = this.oldSlot;
                                    this.oldSlot = ((1131216674 ^ 373254589) & 110334653) << 4 ^ 810183624 ^ -1903378969;
                              }

                              this.isAttacking = (boolean)(((2083711861 | 973749496) >> 1 ^ 154780441 | 444999578) ^ 1051115519);
                              this.isActive = (boolean)((1922537932 << 2 & 1882533386) >>> 4 ^ 67191392);
                        }

                        var3 = mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL ? mc.player.inventory.currentItem : 278027524 >> 3 ^ -34753441;
                        int var29;
                        Minecraft var35;
                        if (var3 == (2048829634 << 3 ^ 1328417456 ^ 1613017951)) {
                              var29 = (1320143435 ^ 809049577 | 153786320) << 3 ^ -34160752;

                              while(true) {
                                    var38 = (8 & 6 | 1684526178) ^ 1684526187;
                                    if ((((2113078664 | 1137173277) >> 1 & 248877641) >>> 1 & 83890678 ^ 83886372) == 0) {
                                          ;
                                    }

                                    if (var29 >= var38) {
                                          break;
                                    }

                                    if (mc.player.inventory.getStackInSlot(var29).getItem() == Items.END_CRYSTAL) {
                                          var35 = mc;
                                          if (!"you're dogshit".equals("please take a shower")) {
                                                ;
                                          }

                                          if (var35.player.getHeldItem(EnumHand.OFF_HAND).getItem() != Items.END_CRYSTAL) {
                                                var3 = var29;
                                                break;
                                          }
                                    }

                                    ++var29;
                              }
                        }

                        var29 = (1740622111 << 3 >>> 1 | 278303717) << 2 ^ 1261148580 ^ 819283536;
                        if (mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL) {
                              var29 = (0 << 4 & 878166976) >>> 2 ^ 1;
                        } else if (var3 == (4031170 >> 3 & 451403 ^ -434249)) {
                              return;
                        }

                        List var5 = this.findCrystalBlocks();
                        ArrayList var37 = new ArrayList();
                        if ((((60660402 | 53344722) ^ 19485013) & 19345365 ^ 273029) == 0) {
                              ;
                        }

                        ArrayList var6 = var37;
                        var36 = mc;
                        if (((142090622 >>> 2 ^ 4556594 ^ 24082834 | 10397228) ^ -246581627) != 0) {
                              ;
                        }

                        Stream var47 = var36.world.playerEntities.stream();
                        Predicate var40 = (var1x) -> {
                              if ((52607451 << 2 << 3 ^ 230841599) != 0) {
                                    ;
                              }

                              return this.attackCheck(var1x);
                        };
                        if (((75498908 >> 2 ^ 3018531) << 3 ^ 141586976) == 0) {
                              ;
                        }

                        var6.addAll((Collection)var47.filter(var40).sorted(Comparator.comparing((var0) -> {
                              EntityPlayerSP var10000 = mc.player;
                              if (!"idiot".equals("please get a girlfriend and stop cracking plugins")) {
                                    ;
                              }

                              return Float.valueOf(var10000.getDistance(var0));
                        })).collect(Collectors.toList()));
                        if (((1998060269 | 805088382) >>> 1 ^ -804937253) != 0) {
                              ;
                        }

                        BlockPos var7 = null;
                        double var8 = 0.5D;
                        Iterator var10 = var6.iterator();

                        label474:
                        while(true) {
                              EntityPlayer var11;
                              do {
                                    do {
                                          if (((1784639689 >>> 2 >> 3 ^ 23029839) << 3 ^ 1693368898) == 0) {
                                          }

                                          if (!"please take a shower".equals("idiot")) {
                                                ;
                                          }

                                          if (!var10.hasNext()) {
                                                if (var8 == 0.5D) {
                                                      if (!"please get a girlfriend and stop cracking plugins".equals("please go outside")) {
                                                            ;
                                                      }

                                                      this.render = null;
                                                      CrystalAura.resetRotation();
                                                      return;
                                                }

                                                if ((1084332136 ^ 304442333 ^ 1384444853) == 0) {
                                                      ;
                                                }

                                                this.render = var7;
                                                if (this.placeCrystal.isEnabled()) {
                                                      if (((215400154 | 13448616 | 163616063) & 75736272 ^ 75736272) == 0) {
                                                            ;
                                                      }

                                                      if (var29 == 0 && mc.player.inventory.currentItem != var3) {
                                                            if (this.autoSwitch.isEnabled()) {
                                                                  label551: {
                                                                        if (!this.noGapSwitch.isEnabled() || mc.player.getHeldItemMainhand().getItem() == Items.GOLDEN_APPLE) {
                                                                              if (!"please get a girlfriend and stop cracking plugins".equals("shitted on you harder than archybot")) {
                                                                                    ;
                                                                              }

                                                                              if (this.noGapSwitch.isEnabled()) {
                                                                                    break label551;
                                                                              }
                                                                        }

                                                                        var35 = mc;
                                                                        if (((1027004499 >> 2 ^ 189592911) >> 3 ^ 8398955) == 0) {
                                                                              ;
                                                                        }

                                                                        var10000 = var35.player;
                                                                        if ((161646086 >> 1 << 4 << 1 ^ -1708629920) == 0) {
                                                                              ;
                                                                        }

                                                                        var10000.inventory.currentItem = var3;
                                                                        CrystalAura.resetRotation();
                                                                        this.switchCooldown = (boolean)((0 | 335687564 | 134479836) ^ 262525674 ^ 329657655);
                                                                  }
                                                            }

                                                            if ((((885114213 | 542215960 | 647130228) >> 1 ^ 130798835) >> 1 ^ 1414109941) != 0) {
                                                                  ;
                                                            }

                                                            return;
                                                      }

                                                      boolean var48 = this.rotateSetting.isEnabled();
                                                      if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("shitted on you harder than archybot")) {
                                                            ;
                                                      }

                                                      if (var48) {
                                                            var38 = var7.getX();
                                                            if ((4298112 >> 4 >> 4 ^ 1387564378) != 0) {
                                                                  ;
                                                            }

                                                            var41 = (double)var38 + 0.5D;
                                                            int var43 = var7.getY();
                                                            if ((609260092 << 2 >>> 2 & 573399934 & 499436780 ^ 44) == 0) {
                                                                  ;
                                                            }

                                                            this.lookAtPacket(var41, (double)var43 - 0.5D, (double)var7.getZ() + 0.5D, mc.player);
                                                      }

                                                      WorldClient var49 = mc.world;
                                                      Vec3d var55 = new Vec3d;
                                                      EntityPlayerSP var46 = mc.player;
                                                      if ((1921292820 << 2 << 2 ^ -2092300617) != 0) {
                                                            ;
                                                      }

                                                      var55.<init>(var46.posX, mc.player.posY + (double)mc.player.getEyeHeight(), mc.player.posZ);
                                                      if (((652382957 >> 2 >> 2 | 684856 | 31139152) ^ 67075966) == 0) {
                                                            ;
                                                      }

                                                      Vec3d var44 = new Vec3d;
                                                      double var51 = (double)var7.getX() + 0.5D;
                                                      double var10005 = (double)var7.getY() - 0.5D;
                                                      if ((1870483180 >> 1 >>> 4 ^ -845174261) != 0) {
                                                            ;
                                                      }

                                                      var44.<init>(var51, var10005, (double)var7.getZ() + 0.5D);
                                                      RayTraceResult var30 = var49.rayTraceBlocks(var55, var44);
                                                      if (this.raytrace.isEnabled()) {
                                                            if (!"ape covered in human flesh".equals("idiot")) {
                                                                  ;
                                                            }

                                                            if (((703268233 ^ 359541535 ^ 14062308 ^ 149312496) << 2 >> 3 ^ 1718618006) != 0) {
                                                                  ;
                                                            }

                                                            if (var30 == null || var30.sideHit == null) {
                                                                  var7 = null;
                                                                  this.enumFacing = null;
                                                                  this.render = null;
                                                                  CrystalAura.resetRotation();
                                                                  var38 = 1339200073 >>> 1 ^ 594567735 ^ 77146899;
                                                                  if (!"stop skidding".equals("you're dogshit")) {
                                                                        ;
                                                                  }

                                                                  this.isActive = (boolean)var38;
                                                                  return;
                                                            }

                                                            if (((282199058 | 153086972) ^ 92194849 ^ 480323551) == 0) {
                                                                  ;
                                                            }

                                                            this.enumFacing = var30.sideHit;
                                                      }

                                                      if (this.switchCooldown) {
                                                            this.switchCooldown = (boolean)((227523562 >>> 4 >>> 3 ^ 725100) << 4 & 6766313 ^ 12448);
                                                            if ((403232322 >>> 4 ^ 25202020) == 0) {
                                                                  ;
                                                            }

                                                            return;
                                                      }

                                                      if (var7 != null && mc.player != null) {
                                                            var38 = (0 ^ 1022573319 ^ 239853203 | 770588468) & 856241779 & 295658073 ^ 285745681;
                                                            if (!"idiot".equals("shitted on you harder than archybot")) {
                                                                  ;
                                                            }

                                                            this.isActive = (boolean)var38;
                                                            NetHandlerPlayClient var52;
                                                            EnumFacing var53;
                                                            EnumHand var54;
                                                            CPacketPlayerTryUseItemOnBlock var56;
                                                            if (this.raytrace.isEnabled() && this.enumFacing != null) {
                                                                  var52 = mc.player.connection;
                                                                  var56 = new CPacketPlayerTryUseItemOnBlock;
                                                                  if (!"please get a girlfriend and stop cracking plugins".equals("You're so fat whenever you go to the beach the tide comes in.")) {
                                                                        ;
                                                                  }

                                                                  if ((1637626899 >>> 1 >>> 3 ^ -612145761) != 0) {
                                                                        ;
                                                                  }

                                                                  var53 = this.enumFacing;
                                                                  if (var29 != 0) {
                                                                        var54 = EnumHand.OFF_HAND;
                                                                        if (!"buy a domain and everything else you need at namecheap.com".equals("i hope you catch fire ngl")) {
                                                                              ;
                                                                        }
                                                                  } else {
                                                                        if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("please go outside")) {
                                                                              ;
                                                                        }

                                                                        var54 = EnumHand.MAIN_HAND;
                                                                  }

                                                                  var56.<init>(var7, var53, var54, 0.0F, 0.0F, 0.0F);
                                                                  var52.sendPacket(var56);
                                                            } else {
                                                                  if (((656146993 >>> 3 ^ 3975509) << 1 ^ -267195933) != 0) {
                                                                        ;
                                                                  }

                                                                  if (var7.getY() == (236 >> 4 >>> 2 << 1 ^ 249)) {
                                                                        mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(var7, EnumFacing.DOWN, var29 != 0 ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.0F, 0.0F, 0.0F));
                                                                  } else {
                                                                        var10000 = mc.player;
                                                                        if (!"yo mama name maurice".equals("yo mama name maurice")) {
                                                                              ;
                                                                        }

                                                                        var52 = var10000.connection;
                                                                        var56 = new CPacketPlayerTryUseItemOnBlock;
                                                                        var53 = EnumFacing.UP;
                                                                        if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("stop skidding")) {
                                                                              ;
                                                                        }

                                                                        if (var29 != 0) {
                                                                              var54 = EnumHand.OFF_HAND;
                                                                        } else {
                                                                              var54 = EnumHand.MAIN_HAND;
                                                                              if ((((40686303 | 1985799) & 18559284) >>> 2 ^ 312466005) != 0) {
                                                                                    ;
                                                                              }
                                                                        }

                                                                        var56.<init>(var7, var53, var54, 0.0F, 0.0F, 0.0F);
                                                                        var52.sendPacket(var56);
                                                                  }
                                                            }

                                                            mc.player.connection.sendPacket(new CPacketAnimation(EnumHand.MAIN_HAND));
                                                            this.PlacedCrystals.add(var7);
                                                      }

                                                      if ((2023118835 >> 4 & 125339998 ^ -2097346097) != 0) {
                                                            ;
                                                      }

                                                      if (isSpoofingAngles) {
                                                            EntityPlayerSP var31;
                                                            if (togglePitch) {
                                                                  var35 = mc;
                                                                  if (((1065983865 << 3 << 1 ^ 290550269) >>> 4 ^ -1713875330) != 0) {
                                                                        ;
                                                                  }

                                                                  var31 = var35.player;
                                                                  var31.rotationPitch = (float)((double)var31.rotationPitch + 4.0E-4D);
                                                                  togglePitch = (boolean)(2092081564 >> 3 << 3 ^ 2092081560);
                                                            } else {
                                                                  var31 = mc.player;
                                                                  var31.rotationPitch = (float)((double)var31.rotationPitch - 4.0E-4D);
                                                                  if ((1276990175 >>> 3 << 3 & 625818794 ^ 61597457 ^ 128382873) == 0) {
                                                                        ;
                                                                  }

                                                                  togglePitch = (boolean)((0 >> 1 << 1 | 1158951662) >> 4 ^ 40659305 ^ 104671814);
                                                            }
                                                      }

                                                      return;
                                                }
                                          }

                                          var11 = (EntityPlayer)var10.next();
                                          if (!"shitted on you harder than archybot".equals("your mom your dad the one you never had")) {
                                                ;
                                          }
                                    } while(var11 == mc.player);
                              } while(var11.getHealth() <= 0.0F);

                              if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("ape covered in human flesh")) {
                                    ;
                              }

                              Iterator var12 = var5.iterator();

                              while(true) {
                                    BlockPos var13;
                                    double var14;
                                    double var18;
                                    float var20;
                                    do {
                                          do {
                                                double var42;
                                                do {
                                                      if (!var12.hasNext()) {
                                                            continue label474;
                                                      }

                                                      Object var39 = var12.next();
                                                      if (!"intentMoment".equals("you're dogshit")) {
                                                            ;
                                                      }

                                                      var13 = (BlockPos)var39;
                                                      var11.getDistanceSq(var13);
                                                      if (((3080117 >>> 1 & 1510167) >> 4 >>> 1 ^ 'ыб?') != 0) {
                                                      }

                                                      double var21 = (double)var13.getX() + 0.0D;
                                                      if ((((324236990 | 87163519) & 47513791) >> 4 ^ 467055398) != 0) {
                                                            ;
                                                      }

                                                      var42 = (double)var13.getY();
                                                      if ((((631237452 | 117728695) << 1 | 828179241) ^ 139251438 ^ 1056285591 ^ 1237758598) != 0) {
                                                      }

                                                      double var23 = var42 + 1.0D;
                                                      if (((308691505 ^ 15019100 | 152349484) ^ -1123298673) != 0) {
                                                            ;
                                                      }

                                                      double var25 = (double)var13.getZ() + 0.0D;
                                                      if (((866500263 >> 4 ^ 3401904 | 10639246) ^ 61601758) != 0) {
                                                      }

                                                      var42 = var11.getDistanceSq(var21, var23, var25);
                                                      var41 = this.enemyRange.getValue() * this.enemyRange.getValue();
                                                      if (((1519803990 | 550374590 | 1460967597) ^ -561083934) != 0) {
                                                            ;
                                                      }
                                                } while(var42 >= var41);

                                                int var45 = var13.getX();
                                                if ("idiot".equals("idiot")) {
                                                }

                                                var42 = (double)var45 + 0.5D;
                                                if (((801970335 << 2 << 4 & 1859814990 | 1315140581) ^ 8114544) != 0) {
                                                      ;
                                                }

                                                var41 = (double)(var13.getY() + ((0 >>> 4 | 238809530) & 59969189 ^ 34799777));
                                                if (((1321765030 >>> 1 << 2 >>> 4 | 12057477) ^ 1858234848) == 0) {
                                                }

                                                var14 = (double)CrystalAura.calculateDamage(var42, var41, (double)var13.getZ() + 0.5D, var11);
                                                if ((541082582 >>> 4 ^ 24518090 ^ 57809399) == 0) {
                                                      ;
                                                }
                                          } while(var14 <= var8);

                                          if (((1037117069 ^ 740912704 ^ 56842096 | 135606454) >> 3 ^ 55830519) == 0) {
                                                ;
                                          }

                                          var18 = (double)CrystalAura.calculateDamage((double)var13.getX() + 0.5D, (double)(var13.getY() + ((0 | 236230709) & 89467015 ^ 68421636)), (double)var13.getZ() + 0.5D, var11);
                                          var20 = var11.getHealth() + var11.getAbsorptionAmount();
                                    } while(var18 < this.minDmg.getValue() && (double)var20 > this.facePlaceValue.getValue());

                                    if (((102237744 << 3 & 787663641 | 230826408) ^ 767697320) == 0) {
                                          ;
                                    }

                                    double var16 = (double)CrystalAura.calculateDamage((double)var13.getX() + 0.5D, (double)(var13.getY() + (0 >> 4 >> 1 >> 3 ^ 942730998 ^ 942730999)), (double)var13.getZ() + 0.5D, mc.player);
                                    var41 = this.maxSelfDmg.getValue();
                                    if (((877950685 ^ 171632755 | 486683640) ^ 1737268161) != 0) {
                                          ;
                                    }

                                    if (var16 < var41 && var16 < (double)(mc.player.getHealth() + mc.player.getAbsorptionAmount())) {
                                          if ((1033479491 >>> 3 << 3 ^ 180490758 ^ 928757574) == 0) {
                                                ;
                                          }

                                          var8 = var14;
                                          if (((1995763506 ^ 1000855839 ^ 144661336) >> 2 & 79359849 ^ 3279689) == 0) {
                                                ;
                                          }

                                          var7 = var13;
                                    }
                              }
                        }
                  }

                  if ((1538529338 >>> 2 >> 2 >>> 1 ^ 48079041) == 0) {
                        ;
                  }
            }

      }

      private static void resetRotation() {
            if (isSpoofingAngles) {
                  yaw = (double)mc.player.rotationYaw;
                  pitch = (double)mc.player.rotationPitch;
                  isSpoofingAngles = (boolean)((2094373841 | 1440464305) >>> 2 ^ 371163875 ^ 33208560 ^ 143840751);
            }

            if (!"stop skidding".equals("buy a domain and everything else you need at namecheap.com")) {
                  ;
            }

      }

      private static void setYawAndPitch(float var0, float var1) {
            yaw = (double)var0;
            pitch = (double)var1;
            if ((((796646130 | 549999744) ^ 520965967 | 250376190) ^ 1056899071) == 0) {
                  ;
            }

            isSpoofingAngles = (boolean)((0 >>> 3 & 2078905350) >> 4 ^ 1);
      }

      private void lookAtPacket(double var1, double var3, double var5, EntityPlayer var7) {
            if (!"your mom your dad the one you never had".equals("please dont crack my plugin")) {
                  ;
            }

            double[] var8 = (double[])CrystalAura.calculateLookAt(var1, var3, var5, var7);
            float var10000 = (float)var8[((610823454 | 333128148 | 787228800) & 353573915 | 278074002) ^ 361963162];
            if (!"please take a shower".equals("please dont crack my plugin")) {
                  ;
            }

            CrystalAura.setYawAndPitch(var10000, (float)var8[0 >> 4 << 4 ^ 1]);
      }

      public static BlockPos getPlayerPos() {
            BlockPos var10000 = new BlockPos;
            double var10002 = mc.player.posX;
            if (((49842182 >>> 4 ^ 1993728 | 1511181) ^ 260902404) != 0) {
                  ;
            }

            var10002 = Math.floor(var10002);
            double var10003 = mc.player.posY;
            if (!"idiot".equals("nefariousMoment")) {
                  ;
            }

            var10000.<init>(var10002, Math.floor(var10003), Math.floor(mc.player.posZ));
            return var10000;
      }

      public CrystalAura() {
            if (!"your mom your dad the one you never had".equals("you're dogshit")) {
                  ;
            }

            super("CrystalAura", "auto place and attack crystals", (1376920217 | 282938967) >>> 4 >>> 4 ^ 5431150, Module.Category.COMBAT);
            TimerUtil var10001 = new TimerUtil();
            if (!"idiot".equals("You're so fat whenever you go to the beach the tide comes in.")) {
                  ;
            }

            this.timer = var10001;
            this.switchCooldown = (boolean)((794672 ^ 417384) >> 3 ^ 17035 ^ 68928);
            this.isAttacking = (boolean)(476183704 >>> 3 << 4 >> 4 ^ 59522963);
            int var1 = (564666069 ^ 268020769) >> 1 ^ 267888243 ^ 417300233;
            if (((167776360 ^ 15021410 | 60485416) ^ -2041654069) != 0) {
                  ;
            }

            this.isActive = (boolean)var1;
            this.oldSlot = ((910133995 ^ 371761863) & 504559329 ^ 679051) >> 1 ^ -802390;
            this.PlacedCrystals = new ArrayList();
            NumberSetting var2 = new NumberSetting;
            if (((418291946 >> 4 ^ 9478097) & 6467811 & 16214 ^ -1738025) != 0) {
                  ;
            }

            var2.<init>("BreakRan", this, 3.5D, 1.0D, 10.0D, 0.1D);
            if ((((1946776542 ^ 1296821441) << 2 | 1576550578) >>> 4 ^ 266315727) == 0) {
                  ;
            }

            this.breakRange = var2;
            if (((2040841377 ^ 1822405883) >>> 1 ^ -2082751912) != 0) {
                  ;
            }

            this.placeRange = new NumberSetting("PlaceRan", this, 3.5D, 1.0D, 10.0D, 0.1D);
            if (((356201795 | 342130980) & 82266027 ^ -1322777094) != 0) {
                  ;
            }

            if (!"idiot".equals("i hope you catch fire ngl")) {
                  ;
            }

            var2 = new NumberSetting;
            if ((21168882 ^ 4964504 ^ 1470490821) != 0) {
                  ;
            }

            if (!"idiot".equals("shitted on you harder than archybot")) {
                  ;
            }

            var2.<init>("AttackSp", this, 0.0D, 0.0D, 1000.0D, 1.0D);
            this.attackSpeed = var2;
            if (((345545764 >> 2 << 4 >> 4 & 16585003) >> 2 ^ -1292190317) != 0) {
                  ;
            }

            if (((17482197 << 3 & 42287016 | 212521) ^ 214816 ^ 1703629615) != 0) {
                  ;
            }

            this.enemyRange = new NumberSetting("EnemyRan", this, 6.0D, 0.0D, 16.0D, 0.1D);
            this.minDmg = new NumberSetting("MinDmg", this, 6.0D, 0.0D, 36.0D, 1.0D);
            var2 = new NumberSetting;
            if (!"idiot".equals("stringer is a good obfuscator")) {
                  ;
            }

            var2.<init>("MxSelfDmg", this, 10.0D, 0.0D, 36.0D, 1.0D);
            this.maxSelfDmg = var2;
            var2 = new NumberSetting;
            if ((((29774771 >> 2 | 2269916) & 535880) >>> 3 ^ 1161) == 0) {
                  ;
            }

            if (((604111940 << 4 >> 3 | 36610314) ^ 170830218) == 0) {
                  ;
            }

            var2.<init>("FacePlcHP", this, 8.0D, 0.0D, 36.0D, 1.0D);
            this.facePlaceValue = var2;
            this.rotateSetting = new BooleanSetting("Rotate", this, (boolean)((0 << 1 | 1143047589) << 1 >> 2 ^ -502218029));
            this.placeCrystal = new BooleanSetting("PlaceCryst", this, (boolean)((0 | 931298262) ^ 497527174 ^ 707121745));
            this.raytrace = new BooleanSetting("RayTr", this, (boolean)((1263985148 | 565372450) ^ 843858295 ^ 1505404553));
            BooleanSetting var3 = new BooleanSetting;
            int var10005 = (0 | 348323802) >>> 3 >>> 1 ^ 13589195 ^ 25392439;
            if (!"stringer is a good obfuscator".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                  ;
            }

            var3.<init>("AutoSwit", this, (boolean)var10005);
            this.autoSwitch = var3;
            var3 = new BooleanSetting("NoGapSwit", this, (boolean)((557249 << 1 & 692366) >> 4 ^ 8));
            if (((192123233 ^ 14804691) >>> 4 >>> 4 ^ 423078454) != 0) {
                  ;
            }

            this.noGapSwitch = var3;
            Setting[] var4 = new Setting[(8 | 2) ^ 7 ^ 1];
            int var10003 = (1705264682 ^ 1674649273 | 40140079 | 5718394 | 43797303) ^ 117407743;
            if (((2121549813 >>> 2 >> 1 & 6266733) >>> 3 ^ 643341) == 0) {
                  ;
            }

            var4[var10003] = this.breakRange;
            var4[0 >> 2 & 232889441 ^ 1] = this.placeCrystal;
            var4[(1 & 0 ^ 375395513) >> 4 ^ 16357842 ^ 27236507] = this.placeRange;
            var4[(1 & 0) >>> 1 ^ 3] = this.attackSpeed;
            if (((2096747646 << 4 ^ 289416398) << 1 >> 4 ^ 799002234 ^ 142051548) != 0) {
                  ;
            }

            var4[(1 | 0 | 0) ^ 5] = this.rotateSetting;
            var4[4 << 2 >>> 2 << 2 ^ 21] = this.raytrace;
            var4[2 & 0 & 398790768 & 1998611778 ^ 6] = this.noGapSwitch;
            var4[(2 << 3 ^ 1) << 1 ^ 37] = this.enemyRange;
            var4[((3 ^ 0) >>> 1 | 0) << 3 ^ 0] = this.minDmg;
            if (!"idiot".equals("you're dogshit")) {
                  ;
            }

            var4[0 << 3 & 917385718 & 705271080 ^ 9] = this.maxSelfDmg;
            var10003 = (4 & 3) << 2 ^ 10;
            if ((((230610358 >>> 3 ^ 5428467) & 24692956) >> 3 ^ 285337019) != 0) {
                  ;
            }

            var4[var10003] = this.facePlaceValue;
            if (((140397597 | 121105427) >>> 4 >> 2 << 1 & 6661006 ^ 326249500) != 0) {
                  ;
            }

            var4[(1 >>> 4 ^ 1197176581) >> 3 ^ 149647083] = this.autoSwitch;
            this.addSettings(var4);
      }

      private void swingArm() {
            mc.player.swingArm(EnumHand.MAIN_HAND);
      }

      private List findCrystalBlocks() {
            NonNullList var1 = NonNullList.create();
            if (((910504102 >>> 2 & 218891806) >> 3 ^ -93864648) != 0) {
                  ;
            }

            BlockPos var10002 = CrystalAura.getPlayerPos();
            double var10003 = this.placeRange.getValue();
            if (!"intentMoment".equals("you're dogshit")) {
                  ;
            }

            float var2 = (float)var10003;
            if (((2113570781 >>> 3 >> 2 | 47874283) ^ 18643046 ^ 48475289) == 0) {
                  ;
            }

            var1.addAll((Collection)this.getSphere(var10002, var2, (int)this.placeRange.getValue(), (boolean)(278265859 >> 4 & 1098970 ^ 16384), (boolean)((0 << 4 & 1221212179) >> 3 >>> 2 << 4 ^ 1), 209781768 >> 4 ^ 13111360).stream().filter(this::canPlaceCrystal).collect(Collectors.toList()));
            if ((578532088 >>> 1 << 3 << 4 >> 3 << 1 ^ -407163008) == 0) {
                  ;
            }

            return var1;
      }

      public List getSphere(BlockPos var1, float var2, int var3, boolean var4, boolean var5, int var6) {
            ArrayList var7 = new ArrayList();
            int var8 = var1.getX();
            int var10000 = var1.getY();
            if (!"stop skidding".equals("yo mama name maurice")) {
                  ;
            }

            int var9 = var10000;
            int var10 = var1.getZ();
            int var11 = var8 - (int)var2;

            while(true) {
                  if (!"nefariousMoment".equals("nefariousMoment")) {
                        ;
                  }

                  float var17 = (float)var11;
                  if (((1532416400 ^ 181873349) >>> 1 << 2 << 3 ^ 404731200) == 0) {
                        ;
                  }

                  if (var17 > (float)var8 + var2) {
                        if (!"You're so fat whenever you go to the beach the tide comes in.".equals("yo mama name maurice")) {
                              ;
                        }

                        return var7;
                  }

                  if (((1889892114 ^ 596614709) >> 1 ^ -705730934) != 0) {
                        ;
                  }

                  if (((134434996 ^ 37255391) >>> 2 << 3 ^ 343298256) == 0) {
                        ;
                  }

                  var10000 = var10 - (int)var2;
                  if (!"stringer is a good obfuscator".equals("ape covered in human flesh")) {
                        ;
                  }

                  for(int var12 = var10000; (float)var12 <= (float)var10 + var2; ++var12) {
                        for(int var13 = var5 ? var9 - (int)var2 : var9; (float)var13 < (var5 ? (float)var9 + var2 : (float)(var9 + var3)); ++var13) {
                              if ("nefariousMoment".equals("ape covered in human flesh")) {
                              }

                              var10000 = (var8 - var11) * (var8 - var11) + (var10 - var12) * (var10 - var12);
                              int var10001;
                              if (var5) {
                                    var10001 = (var9 - var13) * (var9 - var13);
                              } else {
                                    if (((1827539865 ^ 686134355 ^ 64036350) << 3 ^ -309678113) != 0) {
                                          ;
                                    }

                                    var10001 = ((1200567022 | 898540909) >>> 3 | 240631365) ^ 251133693;
                              }

                              double var14 = (double)(var10000 + var10001);
                              double var18;
                              var10000 = (var18 = var14 - (double)(var2 * var2)) == 0.0D ? 0 : (var18 < 0.0D ? -1 : 1);
                              if (!"your mom your dad the one you never had".equals("your mom your dad the one you never had")) {
                                    ;
                              }

                              if (var10000 < 0 && (!var4 || var14 >= (double)((var2 - 1.0F) * (var2 - 1.0F)))) {
                                    if (((2023376143 ^ 1469019922) >>> 2 >> 4 ^ 12342472) == 0) {
                                          ;
                                    }

                                    BlockPos var16 = new BlockPos(var11, var13 + var6, var12);
                                    if (!"i hope you catch fire ngl".equals("You're so fat whenever you go to the beach the tide comes in.")) {
                                          ;
                                    }

                                    if ((1427375774 >>> 2 ^ 334356217 ^ 111730526) == 0) {
                                          ;
                                    }

                                    var7.add(var16);
                              }

                              if (((695361836 << 2 | 1798378968) << 2 ^ 1905720080 ^ -831476496) == 0) {
                                    ;
                              }
                        }
                  }

                  if ((1751518794 >> 4 >> 3 ^ 13683740) == 0) {
                        ;
                  }

                  ++var11;
            }
      }

      static {
            if (!"intentMoment".equals("you're dogshit")) {
                  ;
            }

            togglePitch = (boolean)(1969645935 >> 2 >> 1 >> 2 ^ 61551435);
      }

      public static double[] calculateLookAt(double var0, double var2, double var4, EntityPlayer var6) {
            if ((349495220 >>> 2 >>> 1 ^ 1315163063) != 0) {
                  ;
            }

            double var7 = var6.posX - var0;
            double var9 = var6.posY - var2;
            double var11 = var6.posZ - var4;
            if (!"intentMoment".equals("please get a girlfriend and stop cracking plugins")) {
                  ;
            }

            double var10000 = var7 * var7 + var9 * var9;
            if (!"shitted on you harder than archybot".equals("intentMoment")) {
                  ;
            }

            var10000 += var11 * var11;
            if (!"your mom your dad the one you never had".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                  ;
            }

            var10000 = Math.sqrt(var10000);
            if ((1146183061 << 3 >>> 1 ^ 289764948) == 0) {
                  ;
            }

            double var13 = var10000;
            var7 /= var13;
            var9 /= var13;
            if (((1970281022 << 1 ^ 1864133017) & 1157135406 ^ 83385380) == 0) {
                  ;
            }

            var11 /= var13;
            if ((1675239012 >>> 1 & 585595237 ^ 551880992) == 0) {
                  ;
            }

            double var15 = Math.asin(var9);
            double var17 = Math.atan2(var11, var7);
            if (!"yo mama name maurice".equals("yo mama name maurice")) {
                  ;
            }

            var15 = var15 * 180.0D / 3.141592653589793D;
            if ((28315355 >> 1 >>> 3 << 4 << 4 ^ 212996918) != 0) {
                  ;
            }

            var10000 = var17 * 180.0D;
            if (((677603800 ^ 509969442 ^ 430633557 ^ 707216680) & 65331116 ^ -2061382762) != 0) {
                  ;
            }

            var17 = var10000 / 3.141592653589793D;
            var17 += 90.0D;
            int var19 = ((0 | 1898139060) ^ 1803385960) >> 1 ^ 221190380;
            if ((((1531267444 | 953690420) & 1373798966) >>> 3 ^ 121142510 ^ -1215251859) != 0) {
                  ;
            }

            double[] var20 = new double[var19];
            if ((2129832485 << 1 >>> 3 ^ 532458121) == 0) {
                  ;
            }

            var20[(2072290037 | 417479728) >> 1 << 2 >>> 3 >> 1 ^ 259839966] = var17;
            int var10002 = (0 >> 2 | 1302091155) ^ 1302091154;
            if (!"i hope you catch fire ngl".equals("buy a domain and everything else you need at namecheap.com")) {
                  ;
            }

            var20[var10002] = var15;
            return var20;
      }

      public static float calculateDamage(double var0, double var2, double var4, Entity var6) {
            if (!"please dont crack my plugin".equals("please dont crack my plugin")) {
                  ;
            }

            float var7 = 12.0F;
            if (((523526631 << 2 | 1667977342) & 141046257 ^ 1470204199) != 0) {
                  ;
            }

            double var10000 = var6.getDistance(var0, var2, var4);
            if (((1426872950 >>> 4 ^ 86823631 | 578852) ^ -1867314381) != 0) {
                  ;
            }

            double var8 = var10000 / (double)var7;
            Vec3d var10 = new Vec3d(var0, var2, var4);
            double var11 = (double)var6.world.getBlockDensity(var10, var6.getEntityBoundingBox());
            var10000 = 1.0D - var8;
            if (((755446890 | 373589875) << 4 >>> 3 & 61042311 ^ 1780018661) != 0) {
                  ;
            }

            var10000 *= var11;
            if ((1030371015 << 2 & 1816837382 ^ 1678285060) == 0) {
                  ;
            }

            double var13 = var10000;
            var10000 = (var13 * var13 + var13) / 2.0D * 7.0D;
            if (((289504287 >>> 1 >> 2 << 2 ^ 23482411 | 2538955) ^ 166133743) == 0) {
                  ;
            }

            var10000 *= (double)var7;
            if ((33596312 ^ 11369465 ^ 398858386) != 0) {
                  ;
            }

            float var15 = (float)((int)(var10000 + 1.0D));
            double var16 = 1.0D;
            if (var6 instanceof EntityLivingBase) {
                  if (((911827138 >>> 4 | 35718050) >>> 4 ^ -1363991826) != 0) {
                        ;
                  }

                  if ((((1770860225 ^ 488699894) << 4 | 781087878) ^ -1757248424) != 0) {
                        ;
                  }

                  var16 = (double)CrystalAura.getBlastReduction((EntityLivingBase)var6, CrystalAura.getDamageMultiplied(var15), new Explosion(mc.world, (Entity)null, var0, var2, var4, 6.0F, (boolean)(306315652 >>> 2 >> 1 << 3 ^ 306315648), (boolean)(0 << 1 << 1 ^ 1)));
            }

            return (float)var16;
      }

      public static float getBlastReduction(EntityLivingBase var0, float var1, Explosion var2) {
            if (var0 instanceof EntityPlayer) {
                  if (!"yo mama name maurice".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                        ;
                  }

                  EntityPlayer var3 = (EntityPlayer)var0;
                  if (!"idiot".equals("yo mama name maurice")) {
                        ;
                  }

                  DamageSource var4 = DamageSource.causeExplosionDamage(var2);
                  if (((1873516738 | 1497936239) >>> 4 ^ -1717314262) != 0) {
                        ;
                  }

                  var1 = CombatRules.getDamageAfterAbsorb(var1, (float)var3.getTotalArmorValue(), (float)var3.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
                  if (!"your mom your dad the one you never had".equals("you probably spell youre as your")) {
                        ;
                  }

                  Iterable var10000 = var3.getArmorInventoryList();
                  if ((((18368520 | 10413619) & 15269364) >> 1 ^ -2134918688) != 0) {
                        ;
                  }

                  if (!"You're so fat whenever you go to the beach the tide comes in.".equals("stop skidding")) {
                        ;
                  }

                  int var5 = EnchantmentHelper.getEnchantmentModifierDamage(var10000, var4);
                  float var7 = MathHelper.clamp((float)var5, 0.0F, 20.0F);
                  if (((1202853770 ^ 754016511) & 164912266 & 100661855 ^ 20992000) == 0) {
                        ;
                  }

                  float var6 = var7;
                  var1 *= 1.0F - var6 / 25.0F;
                  if (var0.isPotionActive(Potion.getPotionById((8 >> 4 ^ 879666784 | 538401972) >> 4 >> 1 ^ 27525116))) {
                        var1 -= var1 / 4.0F;
                  }

                  var7 = Math.max(var1, 0.0F);
                  if (((168910532 ^ 82452488 ^ 177285527) >>> 3 >>> 1 ^ 4629877) == 0) {
                        ;
                  }

                  var1 = var7;
                  return var1;
            } else {
                  if (!"intentMoment".equals("ape covered in human flesh")) {
                        ;
                  }

                  int var10001 = var0.getTotalArmorValue();
                  if (((67689504 << 1 ^ 93908717 ^ 77294998 | 123460428) ^ -959268006) != 0) {
                        ;
                  }

                  var1 = CombatRules.getDamageAfterAbsorb(var1, (float)var10001, (float)var0.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
                  return var1;
            }
      }
}
