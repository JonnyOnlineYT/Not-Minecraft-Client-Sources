//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.combat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
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
import net.minecraft.block.state.IBlockState;
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
import net.minecraft.entity.player.InventoryPlayer;
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
      private static boolean isSpoofingAngles;
      public BooleanSetting autoSwitch;
      private TimerUtil timer = new TimerUtil();
      public BooleanSetting raytrace;
      private int oldSlot;
      public NumberSetting maxSelfDmg;
      public BooleanSetting noGapSwitch;
      private int newSlot;
      public boolean isActive;
      private final ArrayList PlacedCrystals;
      private boolean switchCooldown = (1690445373 | 1610463745) ^ 693834347 ^ 1453632598;
      public NumberSetting minDmg;
      public BooleanSetting rotateSetting;
      private EnumFacing enumFacing;
      private BlockPos render;
      public NumberSetting enemyRange;
      public NumberSetting facePlaceValue;
      public NumberSetting breakRange;
      private static double pitch;
      public BooleanSetting placeCrystal;
      private boolean isAttacking = (1812528910 << 4 >> 2 & 538115865) >> 4 ^ 12534514 ^ 46099763;
      private static double yaw;
      private static boolean togglePitch = 167805952 << 3 ^ 1342447616;
      public NumberSetting attackSpeed;
      public NumberSetting placeRange;

      public CrystalAura() {
            super("CrystalAura", "auto place and attack crystals", 1345167578 >>> 4 ^ 40461549 ^ 115847699 ^ 25955571, Module.Category.COMBAT);
            if (((2044605691 ^ 915675392) >> 1 ^ 94889705 ^ 570617876) == 0) {
                  ;
            }

            this.isActive = (boolean)((1750957360 ^ 1216252218 ^ 455458237) & 896044784 & 546022892 ^ 536871072);
            if (((120785330 << 3 | 833103686) ^ -1362736671) != 0) {
                  ;
            }

            int var10001 = 37884440 >> 4 ^ -2367778;
            if (((1698093021 | 994680123 | 1931969773) >>> 4 ^ 1824372037) != 0) {
                  ;
            }

            this.oldSlot = var10001;
            this.PlacedCrystals = new ArrayList();
            NumberSetting var1 = new NumberSetting;
            if ((869437859 >> 4 >>> 4 >>> 1 ^ 930391714) != 0) {
                  ;
            }

            if ((2006520978 >> 4 >>> 1 >> 2 ^ 14186479 ^ -1551985294) != 0) {
                  ;
            }

            var1.<init>("BreakRan", this, 3.5D, 1.0D, 10.0D, 0.1D);
            this.breakRange = var1;
            this.placeRange = new NumberSetting("PlaceRan", this, 3.5D, 1.0D, 10.0D, 0.1D);
            var1 = new NumberSetting;
            if (((1569110639 >>> 4 & 14807982 ^ 6843566) >>> 2 ^ -1710729513) != 0) {
                  ;
            }

            var1.<init>("AttackSp", this, 0.0D, 0.0D, 1000.0D, 1.0D);
            this.attackSpeed = var1;
            this.enemyRange = new NumberSetting("EnemyRan", this, 6.0D, 0.0D, 16.0D, 0.1D);
            var1 = new NumberSetting;
            if (!"buy a domain and everything else you need at namecheap.com".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                  ;
            }

            var1.<init>("MinDmg", this, 6.0D, 0.0D, 36.0D, 1.0D);
            this.minDmg = var1;
            var1 = new NumberSetting;
            if (!"yo mama name maurice".equals("your mom your dad the one you never had")) {
                  ;
            }

            var1.<init>("MxSelfDmg", this, 10.0D, 0.0D, 36.0D, 1.0D);
            this.maxSelfDmg = var1;
            var1 = new NumberSetting;
            if ((61900987 >> 1 << 1 ^ 408324573) != 0) {
                  ;
            }

            var1.<init>("FacePlcHP", this, 8.0D, 0.0D, 36.0D, 1.0D);
            this.facePlaceValue = var1;
            if ((52445956 ^ 47972685 ^ 33309257) == 0) {
                  ;
            }

            this.rotateSetting = new BooleanSetting("Rotate", this, (boolean)(((0 >>> 1 & 1877220461) >>> 3 | 2095559595) ^ 2095559594));
            BooleanSetting var2 = new BooleanSetting;
            if (((667719140 | 646020314) & 273086585 & 4245386 ^ 1681401385) != 0) {
                  ;
            }

            var2.<init>("PlaceCryst", this, (boolean)((0 << 4 | 1494481373 | 404256863) >> 4 ^ 93437852));
            this.placeCrystal = var2;
            this.raytrace = new BooleanSetting("RayTr", this, (boolean)((10702135 | 7087454) << 1 ^ 9181486 ^ 22201296));
            this.autoSwitch = new BooleanSetting("AutoSwit", this, (boolean)(((0 & 446987106) << 4 << 3 | 1892670274) ^ 1892670275));
            this.noGapSwitch = new BooleanSetting("NoGapSwit", this, (boolean)(4850832 >>> 3 & 572086 ^ 524434));
            Setting[] var3 = new Setting[(7 >>> 1 >>> 4 | 115739066) ^ 115739062];
            var3[0 >> 3 ^ 0] = this.breakRange;
            int var10003 = (0 | 860874665 | 476266322) >> 4 ^ 66519038;
            if (!"stop skidding".equals("stop skidding")) {
                  ;
            }

            var3[var10003] = this.placeCrystal;
            var3[(1 >> 3 << 4 & 305970252) << 2 ^ 2] = this.placeRange;
            var3[(0 >> 2 ^ 1347768525) << 2 ^ 1096106807] = this.attackSpeed;
            var10003 = (2 ^ 0) << 4 << 4 & 293 ^ 4;
            if (!"yo mama name maurice".equals("please take a shower")) {
                  ;
            }

            var3[var10003] = this.rotateSetting;
            var3[4 << 4 >> 4 ^ 1] = this.raytrace;
            var3[(0 | 102633947) << 4 >> 2 ^ 410535786] = this.noGapSwitch;
            var3[(1 & 0) >>> 2 ^ 1075902308 ^ 942476274 ^ 2014182545] = this.enemyRange;
            var3[(5 << 1 | 9) ^ 3] = this.minDmg;
            var10003 = (5 ^ 4) >>> 1 & 1128894967 ^ 9;
            NumberSetting var10004 = this.maxSelfDmg;
            if ((((5505141 | 3538259) ^ 4000716) >>> 1 ^ -1460798697) != 0) {
                  ;
            }

            var3[var10003] = var10004;
            var3[(2 >>> 4 | 721643152) ^ 721643162] = this.facePlaceValue;
            var3[7 >>> 2 >> 4 & 1536022131 & 553045346 ^ 11] = this.autoSwitch;
            this.addSettings(var3);
      }

      @SubscribeEvent
      public void onPlayerTick(PlayerTickEvent var1) throws ConcurrentModificationException {
            Minecraft var10000 = mc;
            if (!"idiot".equals("please get a girlfriend and stop cracking plugins")) {
                  ;
            }

            if (var10000.player != null) {
                  var10000 = mc;
                  if (((1099121 | 507728 | 640160) << 4 >>> 4 ^ 1446524256) != 0) {
                        ;
                  }

                  if (var10000.world != null) {
                        var10000 = mc;
                        if ((1028739078 >>> 1 >> 3 >> 4 >> 4 ^ 251157) == 0) {
                              ;
                        }

                        Stream var32 = var10000.world.loadedEntityList.stream();
                        Predicate var10001 = (var0) -> {
                              return var0 instanceof EntityEnderCrystal;
                        };
                        if ((242853 << 2 & 17502 & 14525 ^ 8102948) != 0) {
                              ;
                        }

                        var32 = var32.filter(var10001).filter((var1x) -> {
                              int var10000;
                              if ((double)mc.player.getDistance(var1x) <= this.breakRange.getValue()) {
                                    var10000 = (0 >> 1 | 2069125822 | 1989825118) ^ 2145278719;
                                    if (!"stringer is a good obfuscator".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                                          ;
                                    }
                              } else {
                                    var10000 = (1041961322 >> 1 & 23671321) >>> 4 ^ 1085441;
                              }

                              return (boolean)var10000;
                        });
                        var10001 = (var1x) -> {
                              return this.crystalCheck(var1x);
                        };
                        if (!"i hope you catch fire ngl".equals("please take a shower")) {
                              ;
                        }

                        EntityEnderCrystal var2 = (EntityEnderCrystal)var32.filter(var10001).map((var0) -> {
                              return (EntityEnderCrystal)var0;
                        }).min(Comparator.comparing((var0) -> {
                              return Float.valueOf(mc.player.getDistance(var0));
                        })).orElse((Object)null);
                        int var3;
                        int var34;
                        if (var2 != null) {
                              if (!this.isAttacking) {
                                    InventoryPlayer var33 = mc.player.inventory;
                                    if ((37890577 >>> 1 & 1533015 ^ 66560) == 0) {
                                          ;
                                    }

                                    this.oldSlot = var33.currentItem;
                                    this.isAttacking = (boolean)(((0 | 463767864) >>> 2 | 13240553) & 113052432 ^ 111741441);
                              }

                              this.newSlot = (1122528876 | 244164335) << 2 ^ 157533724 ^ -852786593;
                              var3 = ((2092275393 ^ 804776472) << 4 | 58366425) ^ 931126745;

                              while(true) {
                                    if (((1512928487 ^ 989473142 | 970816547) ^ 2044689843) == 0) {
                                          ;
                                    }

                                    if (var3 >= ((6 ^ 4) >> 2 & 1763665448 ^ 9)) {
                                          break;
                                    }

                                    ItemStack var4 = mc.player.inventory.getStackInSlot(var3);
                                    if (var4 == ItemStack.EMPTY) {
                                          if (((1675686139 >> 3 & 19928428 ^ 183868) >>> 4 >>> 3 ^ 26036) == 0) {
                                                ;
                                          }
                                    } else {
                                          if (((2143265156 | 2067985739) & 1401260166 ^ -1619643006) != 0) {
                                                ;
                                          }

                                          if (var4.getItem() instanceof ItemSword) {
                                                this.newSlot = var3;
                                                break;
                                          }

                                          if (var4.getItem() instanceof ItemTool) {
                                                this.newSlot = var3;
                                                break;
                                          }
                                    }

                                    ++var3;
                              }

                              if ((822640703 >> 3 >>> 3 >> 2 ^ 3213440) == 0) {
                                    ;
                              }

                              if (this.newSlot != ((698751924 >> 1 & 19143571 & 1699) >>> 4 ^ -9)) {
                                    if (((1036633407 >>> 3 | 60981509) ^ 26092107 ^ -1879331135) != 0) {
                                          ;
                                    }

                                    mc.player.inventory.currentItem = this.newSlot;
                                    this.switchCooldown = (boolean)(0 << 4 ^ 224620390 ^ 224620391);
                              }

                              if (this.timer.hasReached(this.attackSpeed.getValue())) {
                                    this.timer.reset();
                                    if (this.rotateSetting.isEnabled()) {
                                          this.lookAtPacket(var2.posX, var2.posY, var2.posZ, mc.player);
                                    }

                                    this.isActive = (boolean)((1012329868 ^ 903497404) & 17705187 ^ 17573920);
                                    if (((1000943468 ^ 847406353 | 4500279) >>> 1 ^ -1503567950) != 0) {
                                          ;
                                    }
                              }
                        } else {
                              CrystalAura.resetRotation();
                              if (this.oldSlot != (1010588842 >>> 3 >> 3 & 7143991 ^ -6291507)) {
                                    if (!"please get a girlfriend and stop cracking plugins".equals("You're so fat whenever you go to the beach the tide comes in.")) {
                                          ;
                                    }

                                    mc.player.inventory.currentItem = this.oldSlot;
                                    this.oldSlot = 1142716223 >> 1 >>> 2 ^ -142839528;
                              }

                              var34 = 173444906 << 2 << 4 << 1 ^ 726111488;
                              if (!"Some babies were dropped on their heads but you were clearly thrown at a wall.".equals("you probably spell youre as your")) {
                                    ;
                              }

                              this.isAttacking = (boolean)var34;
                              if (((723371214 ^ 324017865) >>> 1 ^ 472313091) == 0) {
                                    ;
                              }

                              this.isActive = (boolean)(1037644789 >>> 3 & 70130485 ^ 28664697 ^ 94331213);
                        }

                        var10000 = mc;
                        if (!"please go outside".equals("You're so fat whenever you go to the beach the tide comes in.")) {
                              ;
                        }

                        int var35;
                        if (var10000.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL) {
                              var10000 = mc;
                              if (((427316305 | 398014970 | 60931918) & 437257041 ^ 2077945424) != 0) {
                                    ;
                              }

                              var35 = var10000.player.inventory.currentItem;
                        } else {
                              var35 = 128052189 >> 2 >> 3 >>> 3 ^ -500204;
                        }

                        var3 = var35;
                        int var29;
                        if (var3 == (((98924209 | 84551917) ^ 55768530) >>> 3 >> 2 ^ 1857982 ^ -2733544)) {
                              if (((282457339 ^ 265905355 | 265231573) & 240247430 ^ 239173764) == 0) {
                                    ;
                              }

                              var35 = 689161 << 3 & 4315543 ^ 4154646 ^ 8348950;
                              if (((67144740 | 40576591) << 4 ^ 1723524848) == 0) {
                                    ;
                              }

                              for(var29 = var35; var29 < (7 >> 4 & 763496618 ^ 1105322514 ^ 119178932 ^ 1190880431); ++var29) {
                                    if ((948198566 >> 2 >>> 2 & 12404005 ^ 8930560) == 0) {
                                          ;
                                    }

                                    ItemStack var40 = mc.player.inventory.getStackInSlot(var29);
                                    if (((229892 >>> 2 & 31651 | 6089) >>> 2 ^ 7666) == 0) {
                                          ;
                                    }

                                    if (var40.getItem() == Items.END_CRYSTAL) {
                                          if (!"buy a domain and everything else you need at namecheap.com".equals("please go outside")) {
                                                ;
                                          }

                                          if (mc.player.getHeldItem(EnumHand.OFF_HAND).getItem() != Items.END_CRYSTAL) {
                                                if ((((25690896 | 10052108) ^ 20567618) >> 2 ^ -1324984901) != 0) {
                                                      ;
                                                }

                                                var3 = var29;
                                                break;
                                          }
                                    }
                              }
                        }

                        var29 = (108032163 ^ 97527369) >> 3 << 2 ^ 30419828;
                        if (mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL) {
                              if (((1651977625 | 1141294383 | 100940226) ^ 1719139839) == 0) {
                                    ;
                              }

                              var29 = (0 >> 1 << 4 | 801257779) ^ 801257778;
                        } else {
                              if (((36013878 >> 4 << 2 >> 2 | 1063485) ^ 3308159) == 0) {
                                    ;
                              }

                              if (var3 == ((456283920 | 452467998 | 163199226) << 1 ^ -938803197)) {
                                    if (!"You're so fat whenever you go to the beach the tide comes in.".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                                          ;
                                    }

                                    return;
                              }
                        }

                        if (((1820992663 | 1643468830) >>> 2 ^ 461365031) == 0) {
                              ;
                        }

                        List var5 = this.findCrystalBlocks();
                        ArrayList var42 = new ArrayList;
                        if (!"nefariousMoment".equals("shitted on you harder than archybot")) {
                              ;
                        }

                        var42.<init>();
                        ArrayList var6 = var42;
                        if ((18301399 >>> 4 >> 4 << 2 ^ 46889433) != 0) {
                              ;
                        }

                        WorldClient var36 = mc.world;
                        if (!"please get a girlfriend and stop cracking plugins".equals("yo mama name maurice")) {
                              ;
                        }

                        var6.addAll((Collection)var36.playerEntities.stream().filter((var1x) -> {
                              if (((34780513 | 4701893) >>> 4 & 936433 ^ 169587458) != 0) {
                                    ;
                              }

                              return this.attackCheck(var1x);
                        }).sorted(Comparator.comparing((var0) -> {
                              return Float.valueOf(mc.player.getDistance(var0));
                        })).collect(Collectors.toList()));
                        if (((768004732 >>> 4 & 31907649 ^ 6462116) << 3 ^ 87473448) == 0) {
                              ;
                        }

                        BlockPos var7 = null;
                        double var8 = 0.5D;
                        Iterator var10 = var6.iterator();

                        label524:
                        while(true) {
                              EntityPlayer var11;
                              double var37;
                              double var38;
                              do {
                                    do {
                                          if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("you probably spell youre as your")) {
                                                ;
                                          }

                                          if (!var10.hasNext()) {
                                                if (var8 == 0.5D) {
                                                      this.render = null;
                                                      CrystalAura.resetRotation();
                                                      return;
                                                }

                                                if (((1485176984 | 173820120) ^ -1654141639) != 0) {
                                                      ;
                                                }

                                                this.render = var7;
                                                if (this.placeCrystal.isEnabled()) {
                                                      if (var29 == 0) {
                                                            var35 = mc.player.inventory.currentItem;
                                                            if (((54887433 ^ 7564516) >>> 3 ^ 143464420) != 0) {
                                                                  ;
                                                            }

                                                            if (var35 != var3) {
                                                                  if (!"yo mama name maurice".equals("You're so fat whenever you go to the beach the tide comes in.")) {
                                                                        ;
                                                                  }

                                                                  if (this.autoSwitch.isEnabled()) {
                                                                        if (!this.noGapSwitch.isEnabled() || mc.player.getHeldItemMainhand().getItem() == Items.GOLDEN_APPLE) {
                                                                              if ((273492392 >> 2 & 29119691 ^ 292464412) != 0) {
                                                                                    ;
                                                                              }

                                                                              if (this.noGapSwitch.isEnabled()) {
                                                                                    return;
                                                                              }
                                                                        }

                                                                        if (!"you're dogshit".equals("You're so fat whenever you go to the beach the tide comes in.")) {
                                                                              ;
                                                                        }

                                                                        mc.player.inventory.currentItem = var3;
                                                                        if (!"buy a domain and everything else you need at namecheap.com".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                                                                              ;
                                                                        }

                                                                        CrystalAura.resetRotation();
                                                                        this.switchCooldown = (boolean)(0 >> 2 << 3 ^ 157039270 ^ 157039271);
                                                                  }

                                                                  return;
                                                            }
                                                      }

                                                      double var10003;
                                                      if (this.rotateSetting.isEnabled()) {
                                                            var38 = (double)var7.getX() + 0.5D;
                                                            if (!"nefariousMoment".equals("You're so fat whenever you go to the beach the tide comes in.")) {
                                                                  ;
                                                            }

                                                            var37 = (double)var7.getY() - 0.5D;
                                                            var10003 = (double)var7.getZ() + 0.5D;
                                                            Minecraft var10004 = mc;
                                                            if ((((896532066 | 196232273) & 928313796) >> 2 ^ -2179233) != 0) {
                                                                  ;
                                                            }

                                                            this.lookAtPacket(var38, var37, var10003, var10004.player);
                                                      }

                                                      if (((537986112 ^ 188319173) >> 4 ^ -947537344) != 0) {
                                                            ;
                                                      }

                                                      WorldClient var51 = mc.world;
                                                      Vec3d var43 = new Vec3d;
                                                      if (!"Some babies were dropped on their heads but you were clearly thrown at a wall.".equals("please get a girlfriend and stop cracking plugins")) {
                                                            ;
                                                      }

                                                      var10003 = mc.player.posX;
                                                      double var44 = mc.player.posY;
                                                      EntityPlayerSP var10005 = mc.player;
                                                      if ((((1482999704 | 165377800) ^ 674420515 ^ 1689583227 | 247589403) ^ -1634045829) != 0) {
                                                            ;
                                                      }

                                                      var43.<init>(var10003, var44 + (double)var10005.getEyeHeight(), mc.player.posZ);
                                                      Vec3d var39 = new Vec3d;
                                                      var44 = (double)var7.getX() + 0.5D;
                                                      double var48 = (double)var7.getY() - 0.5D;
                                                      double var10006 = (double)var7.getZ();
                                                      if ((1728528856 << 1 >>> 2 ^ -118587563) != 0) {
                                                            ;
                                                      }

                                                      var39.<init>(var44, var48, var10006 + 0.5D);
                                                      RayTraceResult var30 = var51.rayTraceBlocks(var43, var39);
                                                      if (this.raytrace.isEnabled()) {
                                                            if (var30 == null || var30.sideHit == null) {
                                                                  var7 = null;
                                                                  if ((((993665040 | 465028160) ^ 837704650) >> 3 ^ -1890988505) != 0) {
                                                                        ;
                                                                  }

                                                                  this.enumFacing = null;
                                                                  this.render = null;
                                                                  if ((565557051 >>> 2 << 2 ^ 565557048) == 0) {
                                                                        ;
                                                                  }

                                                                  CrystalAura.resetRotation();
                                                                  if (((110922682 | 80392201) << 1 ^ 111891361 ^ 186000599) == 0) {
                                                                        ;
                                                                  }

                                                                  if (!"your mom your dad the one you never had".equals("stringer is a good obfuscator")) {
                                                                        ;
                                                                  }

                                                                  this.isActive = (boolean)((1934063155 >>> 2 & 478155215 | 151422952) ^ 495359980);
                                                                  return;
                                                            }

                                                            this.enumFacing = var30.sideHit;
                                                      }

                                                      if (this.switchCooldown) {
                                                            this.switchCooldown = (boolean)((913170908 | 577078739) & 175877998 & 38723024 ^ 38307136);
                                                            return;
                                                      }

                                                      if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("please get a girlfriend and stop cracking plugins")) {
                                                            ;
                                                      }

                                                      if (var7 != null && mc.player != null) {
                                                            this.isActive = (boolean)(((0 ^ 927427394) & 813570769) << 4 >> 3 ^ 9180289);
                                                            if ((67300 << 1 >>> 3 & 1299 ^ 41374927) != 0) {
                                                                  ;
                                                            }

                                                            label585: {
                                                                  CPacketPlayerTryUseItemOnBlock var46;
                                                                  EnumFacing var47;
                                                                  EnumHand var49;
                                                                  NetHandlerPlayClient var54;
                                                                  if (this.raytrace.isEnabled()) {
                                                                        if (((667021454 >>> 2 | 2990171) & 50190319 ^ -1577804687) != 0) {
                                                                              ;
                                                                        }

                                                                        EnumFacing var52 = this.enumFacing;
                                                                        if ((((1265465099 | 815409701) & 1818684040) << 2 << 1 ^ -858835562) != 0) {
                                                                              ;
                                                                        }

                                                                        if (var52 != null) {
                                                                              if (!"i hope you catch fire ngl".equals("your mom your dad the one you never had")) {
                                                                                    ;
                                                                              }

                                                                              var54 = mc.player.connection;
                                                                              var46 = new CPacketPlayerTryUseItemOnBlock;
                                                                              var47 = this.enumFacing;
                                                                              if (var29 != 0) {
                                                                                    var49 = EnumHand.OFF_HAND;
                                                                                    if (((40894497 | 38749425) >>> 4 ^ 2618447) == 0) {
                                                                                          ;
                                                                                    }
                                                                              } else {
                                                                                    var49 = EnumHand.MAIN_HAND;
                                                                                    if ((779748568 >>> 2 >> 2 >>> 1 ^ 24367142) == 0) {
                                                                                          ;
                                                                                    }
                                                                              }

                                                                              var46.<init>(var7, var47, var49, 0.0F, 0.0F, 0.0F);
                                                                              var54.sendPacket(var46);
                                                                              break label585;
                                                                        }
                                                                  }

                                                                  if (((663874643 >> 4 >> 4 | 340868 | 545093) ^ 3142637) == 0) {
                                                                        ;
                                                                  }

                                                                  if (var7.getY() == (((215 | 125) & 175 & 66) >> 3 ^ 255)) {
                                                                        if (!"please get a girlfriend and stop cracking plugins".equals("stringer is a good obfuscator")) {
                                                                              ;
                                                                        }

                                                                        EntityPlayerSP var53 = mc.player;
                                                                        if (!"your mom your dad the one you never had".equals("i hope you catch fire ngl")) {
                                                                              ;
                                                                        }

                                                                        var54 = var53.connection;
                                                                        var46 = new CPacketPlayerTryUseItemOnBlock;
                                                                        var47 = EnumFacing.DOWN;
                                                                        if (var29 != 0) {
                                                                              if (!"you're dogshit".equals("ape covered in human flesh")) {
                                                                                    ;
                                                                              }

                                                                              var49 = EnumHand.OFF_HAND;
                                                                        } else {
                                                                              var49 = EnumHand.MAIN_HAND;
                                                                        }

                                                                        var46.<init>(var7, var47, var49, 0.0F, 0.0F, 0.0F);
                                                                        var54.sendPacket(var46);
                                                                  } else {
                                                                        var10000 = mc;
                                                                        if ((1233131292 >> 1 ^ 482517296 ^ 939698878) == 0) {
                                                                              ;
                                                                        }

                                                                        var54 = var10000.player.connection;
                                                                        var46 = new CPacketPlayerTryUseItemOnBlock;
                                                                        var47 = EnumFacing.UP;
                                                                        var49 = var29 != 0 ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND;
                                                                        if (!"ape covered in human flesh".equals("please go outside")) {
                                                                              ;
                                                                        }

                                                                        var46.<init>(var7, var47, var49, 0.0F, 0.0F, 0.0F);
                                                                        var54.sendPacket(var46);
                                                                  }
                                                            }

                                                            mc.player.connection.sendPacket(new CPacketAnimation(EnumHand.MAIN_HAND));
                                                            if ((((195814326 ^ 43471495) & 76722603) >> 2 ^ 358385685) != 0) {
                                                                  ;
                                                            }

                                                            this.PlacedCrystals.add(var7);
                                                            if (((1050206322 ^ 421785890) << 4 ^ 792901054 ^ 1425252542) == 0) {
                                                                  ;
                                                            }
                                                      }

                                                      if (isSpoofingAngles) {
                                                            boolean var55 = togglePitch;
                                                            if (!"buy a domain and everything else you need at namecheap.com".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                                                                  ;
                                                            }

                                                            EntityPlayerSP var31;
                                                            if (var55) {
                                                                  var31 = mc.player;
                                                                  if (((240636425 | 114103632) >> 2 ^ 62387670) == 0) {
                                                                        ;
                                                                  }

                                                                  var31.rotationPitch = (float)((double)var31.rotationPitch + 4.0E-4D);
                                                                  togglePitch = (boolean)(94651400 ^ 54361052 ^ 110706644);
                                                            } else {
                                                                  var31 = mc.player;
                                                                  var38 = (double)var31.rotationPitch - 4.0E-4D;
                                                                  if (((9508473 ^ 7609098) >> 1 ^ -1838220678) != 0) {
                                                                        ;
                                                                  }

                                                                  var31.rotationPitch = (float)var38;
                                                                  togglePitch = (boolean)((0 & 1962070122) << 1 ^ 1);
                                                            }
                                                      }

                                                      return;
                                                }
                                          }

                                          var11 = (EntityPlayer)var10.next();
                                    } while(var11 == mc.player);
                              } while(var11.getHealth() <= 0.0F);

                              Iterator var12 = var5.iterator();

                              while(true) {
                                    BlockPos var13;
                                    double var14;
                                    double var45;
                                    while(true) {
                                          do {
                                                double var21;
                                                double var23;
                                                double var25;
                                                do {
                                                      if (!"please dont crack my plugin".equals("stringer is a good obfuscator")) {
                                                            ;
                                                      }

                                                      if (!var12.hasNext()) {
                                                            continue label524;
                                                      }

                                                      if (((1447069000 << 3 & 247612972) << 2 << 4 ^ -2147450880) != 0) {
                                                      }

                                                      if ((1415676737 << 2 & 1209252571 ^ 381068210) != 0) {
                                                            ;
                                                      }

                                                      var13 = (BlockPos)var12.next();
                                                      var11.getDistanceSq(var13);
                                                      var35 = var13.getX();
                                                      if (((999290064 | 251704601) >> 4 & 60058432 ^ 59796288) != 0) {
                                                      }

                                                      var21 = (double)var35 + 0.0D;
                                                      var45 = (double)var13.getY();
                                                      if (!"you probably spell youre as your".equals("shitted on you harder than archybot")) {
                                                            ;
                                                      }

                                                      var23 = var45 + 1.0D;
                                                      var35 = var13.getZ();
                                                      if ((1378896802 >> 3 >>> 4 ^ -1697273359) == 0) {
                                                      }

                                                      var25 = (double)var35 + 0.0D;
                                                      if (((792415448 >>> 3 & 31016839) >> 4 ^ 612530 ^ 1551520700) != 0) {
                                                            ;
                                                      }
                                                } while(var11.getDistanceSq(var21, var23, var25) >= this.enemyRange.getValue() * this.enemyRange.getValue());

                                                var45 = (double)var13.getX() + 0.5D;
                                                if (!"intentMoment".equals("you probably spell youre as your")) {
                                                      ;
                                                }

                                                var34 = var13.getY();
                                                int var10002 = (0 ^ 539080067) >> 1 << 4 << 3 & 32148976 ^ 6815873;
                                                if ((((517529419 << 3 ^ 20496644) & 1953553155 & 793811564) << 4 ^ 1157734400) != 0) {
                                                }

                                                var38 = (double)(var34 + var10002);
                                                var37 = (double)var13.getZ();
                                                if (((1399764731 >> 1 | 185673512) ^ -1656671948) != 0) {
                                                      ;
                                                }

                                                if ((1282605456 >> 3 >> 1 ^ -1234663336) == 0) {
                                                }

                                                var37 += 0.5D;
                                                if (((192182333 ^ 123886949) >> 2 >>> 3 ^ 415717545) != 0) {
                                                      ;
                                                }

                                                var14 = (double)CrystalAura.calculateDamage(var45, var38, var37, var11);
                                          } while(var14 <= var8);

                                          var45 = (double)var13.getX() + 0.5D;
                                          if (!"minecraft".equals("nefariousMoment")) {
                                                ;
                                          }

                                          double var18 = (double)CrystalAura.calculateDamage(var45, (double)(var13.getY() + ((0 | 1085155733) << 1 ^ -2124655829)), (double)var13.getZ() + 0.5D, var11);
                                          float var20 = var11.getHealth() + var11.getAbsorptionAmount();
                                          if (var18 >= this.minDmg.getValue()) {
                                                break;
                                          }

                                          if ((double)var20 <= this.facePlaceValue.getValue()) {
                                                if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("you probably spell youre as your")) {
                                                      ;
                                                }
                                                break;
                                          }
                                    }

                                    if (!"yo mama name maurice".equals("please get a girlfriend and stop cracking plugins")) {
                                          ;
                                    }

                                    var45 = (double)var13.getX() + 0.5D;
                                    var38 = (double)(var13.getY() + (0 >>> 3 & 1279102427 ^ 1));
                                    if ((111934434 << 1 << 3 >>> 1 ^ -187490965) != 0) {
                                          ;
                                    }

                                    float var50 = CrystalAura.calculateDamage(var45, var38, (double)var13.getZ() + 0.5D, mc.player);
                                    if (((975611260 | 549389990) ^ 691547296 ^ 327650142) == 0) {
                                          ;
                                    }

                                    double var16 = (double)var50;
                                    if (var16 < this.maxSelfDmg.getValue()) {
                                          if (((231843827 ^ 120567320) >> 1 << 4 >>> 3 ^ 277099804) != 0) {
                                                ;
                                          }

                                          EntityPlayerSP var41 = mc.player;
                                          if (((168116230 | 39981058) ^ -168173277) != 0) {
                                                ;
                                          }

                                          if (var16 < (double)(var41.getHealth() + mc.player.getAbsorptionAmount())) {
                                                if ((134521356 >> 4 ^ 7582643 ^ 15989139) == 0) {
                                                      ;
                                                }

                                                var8 = var14;
                                                var7 = var13;
                                          }
                                    }
                              }
                        }
                  }
            }

      }

      public static float calculateDamage(double var0, double var2, double var4, Entity var6) {
            float var7 = 12.0F;
            if (((726793223 << 4 | 1237316354) << 4 & 1189139467 ^ 1121981440) == 0) {
                  ;
            }

            double var8 = var6.getDistance(var0, var2, var4) / (double)var7;
            Vec3d var10000 = new Vec3d;
            if (!"please take a shower".equals("shitted on you harder than archybot")) {
                  ;
            }

            var10000.<init>(var0, var2, var4);
            Vec3d var10 = var10000;
            double var11 = (double)var6.world.getBlockDensity(var10, var6.getEntityBoundingBox());
            double var13 = (1.0D - var8) * var11;
            if (!"ape covered in human flesh".equals("nefariousMoment")) {
                  ;
            }

            double var18 = var13 * var13;
            if (((84231040 | 53349012) & 74316579 ^ 941817329) != 0) {
                  ;
            }

            var18 = (var18 + var13) / 2.0D * 7.0D;
            if (((270873868 | 232225060) << 4 ^ 683250214 ^ -558379177) != 0) {
                  ;
            }

            float var19 = (float)((int)(var18 * (double)var7 + 1.0D));
            if ((143330312 >> 4 >> 4 ^ 1624311222) != 0) {
                  ;
            }

            float var15 = var19;
            double var16 = 1.0D;
            if (var6 instanceof EntityLivingBase) {
                  if (((1361903526 | 95228614) >>> 1 ^ 53363809 ^ 704166802) == 0) {
                        ;
                  }

                  EntityLivingBase var20 = (EntityLivingBase)var6;
                  float var10001 = CrystalAura.getDamageMultiplied(var15);
                  Explosion var10002 = new Explosion;
                  WorldClient var10004 = mc.world;
                  if (((1662540322 ^ 273898358) >> 1 >> 1 & 206998194 ^ 206733328) == 0) {
                        ;
                  }

                  if ((543694917 >>> 2 << 2 ^ 543694916) == 0) {
                        ;
                  }

                  var10002.<init>(var10004, (Entity)null, var0, var2, var4, 6.0F, (boolean)(((1535631778 | 924301153 | 378626986) & 558880798) << 4 ^ 276627616), (boolean)(0 << 4 << 1 << 1 >>> 3 ^ 1));
                  var18 = (double)CrystalAura.getBlastReduction(var20, var10001, var10002);
                  if ((541106208 >> 2 ^ -494402211) != 0) {
                        ;
                  }

                  var16 = var18;
            }

            return (float)var16;
      }

      private boolean crystalCheck(Entity var1) {
            boolean var10000 = var1 instanceof EntityEnderCrystal;
            if (((1208068352 | 755128434) >>> 1 >> 4 ^ -1880659207) != 0) {
                  ;
            }

            if (!var10000) {
                  if ((1822953330 >>> 4 & 85706611 ^ 67797107) == 0) {
                        ;
                  }

                  return (boolean)(((2063732235 << 1 ^ 1291475457) & 1253435027) >> 2 >> 1 ^ 22448322);
            } else {
                  return (boolean)((0 & 328339589) >>> 3 & 1203675876 & 1729830255 ^ 1);
            }
      }

      private void lookAtPacket(double var1, double var3, double var5, EntityPlayer var7) {
            double[] var8 = (double[])CrystalAura.calculateLookAt(var1, var3, var5, var7);
            float var10000 = (float)var8[(268443648 >>> 3 | 4128514) ^ 37682946];
            if (((685587288 << 3 | 882907449) ^ 1995037689) == 0) {
                  ;
            }

            CrystalAura.setYawAndPitch(var10000, (float)var8[(0 ^ 2087050122 | 1302454669 | 745835288) ^ 2113272734]);
      }

      private void breakCrystal(EntityEnderCrystal var1) {
            mc.playerController.attackEntity(mc.player, var1);
            this.swingArm();
            if (!"you're dogshit".equals("idiot")) {
                  ;
            }

      }

      public static BlockPos getPlayerPos() {
            if (!"buy a domain and everything else you need at namecheap.com".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                  ;
            }

            return new BlockPos(Math.floor(mc.player.posX), Math.floor(mc.player.posY), Math.floor(mc.player.posZ));
      }

      private static void resetRotation() {
            if (isSpoofingAngles) {
                  if ((((985502920 ^ 491970931) << 4 & 123549403) >> 3 ^ 1221851837) != 0) {
                        ;
                  }

                  yaw = (double)mc.player.rotationYaw;
                  pitch = (double)mc.player.rotationPitch;
                  isSpoofingAngles = (boolean)((1300248478 | 410640797 | 1466697391) << 2 & 37896735 ^ 37896732);
            }

      }

      private boolean attackCheck(Entity var1) {
            Iterator var2 = FriendManager.friends.iterator();

            Friend var3;
            do {
                  if (!var2.hasNext()) {
                        return (boolean)((0 >>> 2 & 1163080462) >> 1 ^ 1505680340 ^ 1505680341);
                  }

                  var3 = (Friend)var2.next();
            } while(var1.getName() != var3.name);

            int var10000 = (1973105132 ^ 548243989) >> 4 >> 3 & 1138770 ^ 24658;
            if (!"please dont crack my plugin".equals("please go outside")) {
                  ;
            }

            return (boolean)var10000;
      }

      private static void setYawAndPitch(float var0, float var1) {
            double var10000 = (double)var0;
            if (((1462069365 ^ 807304909) << 3 >> 1 >>> 1 ^ 242625904) == 0) {
                  ;
            }

            yaw = var10000;
            if (!"idiot".equals("you probably spell youre as your")) {
                  ;
            }

            if ((2006431823 >>> 3 << 1 & 26874041 ^ 25165840) == 0) {
                  ;
            }

            pitch = (double)var1;
            isSpoofingAngles = (boolean)(((0 >>> 1 | 338876853) << 2 | 1026248140) ^ 2112581597);
      }

      public List getSphere(BlockPos var1, float var2, int var3, boolean var4, boolean var5, int var6) {
            ArrayList var7 = new ArrayList();
            int var10000 = var1.getX();
            if (((751142800 ^ 82259187) << 1 & 464995794 ^ 268780738) == 0) {
                  ;
            }

            int var8 = var10000;
            int var9 = var1.getY();
            if ((((2109644747 | 155546027) ^ 2064064322 ^ 52120792 | 13809278) ^ 99860095) == 0) {
                  ;
            }

            int var10 = var1.getZ();
            if (((25182256 | 10453113 | 8796076) >>> 3 ^ 3407615) == 0) {
                  ;
            }

            int var11 = var8 - (int)var2;

            while(true) {
                  if (!"buy a domain and everything else you need at namecheap.com".equals("nefariousMoment")) {
                        ;
                  }

                  float var17 = (float)var11;
                  float var10001 = (float)var8;
                  if (!"stop skidding".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                        ;
                  }

                  if (var17 > var10001 + var2) {
                        return var7;
                  }

                  int var18 = (int)var2;
                  if (((252134154 ^ 6341628 ^ 84672185) << 2 ^ 117977421 ^ -1607603394) != 0) {
                        ;
                  }

                  var10000 = var10 - var18;
                  if ((1992388599 << 2 >> 2 >> 1 ^ -77547525) == 0) {
                        ;
                  }

                  for(int var12 = var10000; (float)var12 <= (float)var10 + var2; ++var12) {
                        if (((163653701 >>> 3 | 13746266) ^ 26299209 ^ 6860307) == 0) {
                              ;
                        }

                        for(int var13 = var5 ? var9 - (int)var2 : var9; (float)var13 < (var5 ? (float)var9 + var2 : (float)(var9 + var3)); ++var13) {
                              var10000 = var8 - var11;
                              if (((9485944 >> 4 | 264380 | 732323) >> 2 ^ -616426130) != 0) {
                                    ;
                              }

                              var10000 *= var8 - var11;
                              var18 = var10 - var12;
                              if (((25917407 ^ 15149990) & 21521605 ^ 1789191248) == 0) {
                              }

                              var10000 += var18 * (var10 - var12);
                              if (var5) {
                                    if (!"intentMoment".equals("please get a girlfriend and stop cracking plugins")) {
                                          ;
                                    }

                                    var18 = (var9 - var13) * (var9 - var13);
                              } else {
                                    var18 = ((877164237 ^ 184007345) & 418992373 & 132998170 | 6135265) ^ 16637425;
                              }

                              double var14 = (double)(var10000 + var18);
                              if (var14 < (double)(var2 * var2)) {
                                    if (var4) {
                                          var10001 = var2 - 1.0F;
                                          if (!"you probably spell youre as your".equals("please get a girlfriend and stop cracking plugins")) {
                                                ;
                                          }

                                          if (var14 < (double)(var10001 * (var2 - 1.0F))) {
                                                continue;
                                          }
                                    }

                                    BlockPos var16 = new BlockPos(var11, var13 + var6, var12);
                                    var7.add(var16);
                              }
                        }
                  }

                  ++var11;
            }
      }

      private List findCrystalBlocks() {
            NonNullList var1 = NonNullList.create();
            if (!"idiot".equals("buy a domain and everything else you need at namecheap.com")) {
                  ;
            }

            BlockPos var10002 = CrystalAura.getPlayerPos();
            float var10003 = (float)this.placeRange.getValue();
            int var10004 = (int)this.placeRange.getValue();
            int var10005 = (1972133427 >> 4 ^ 23517849) << 4 ^ 1675753376;
            if ((938753308 >> 2 >> 4 << 1 ^ 387464 ^ 28985440) == 0) {
                  ;
            }

            var1.addAll((Collection)this.getSphere(var10002, var10003, var10004, (boolean)var10005, (boolean)(((0 | 1753070850 | 1133779260) & 1560134219 | 1163270490) << 3 ^ 1878837969), (2079789365 >>> 4 | 54364730) >>> 2 ^ 32505550).stream().filter(this::canPlaceCrystal).collect(Collectors.toList()));
            return var1;
      }

      public boolean canPlaceCrystal(BlockPos var1) {
            BlockPos var2 = var1.add(2137866897 >>> 3 >>> 1 ^ 133616681, (0 ^ 1987340647) >> 1 ^ 993670322, (2024116897 << 3 | 2083650206) ^ 943753900 ^ -981633742);
            BlockPos var3 = var1.add(987772445 << 3 >>> 3 << 4 ^ -1375510064, (1 ^ 0) >>> 3 ^ 2, (1238089397 >> 3 | 101475398 | 92191433) ^ 62761633 ^ 214062462);
            int var5;
            if (mc.world.getBlockState(var1).getBlock() == Blocks.BEDROCK || mc.world.getBlockState(var1).getBlock() == Blocks.OBSIDIAN) {
                  WorldClient var10000 = mc.world;
                  if (((1750133692 ^ 978298546) >>> 1 >> 3 ^ -246608104) != 0) {
                        ;
                  }

                  IBlockState var4 = var10000.getBlockState(var2);
                  if (!"minecraft".equals("nefariousMoment")) {
                        ;
                  }

                  if (var4.getBlock() == Blocks.AIR) {
                        var4 = mc.world.getBlockState(var3);
                        if ((((2011041512 | 1947328749) & 1440169954 | 1151320317) ^ -1609278717) != 0) {
                              ;
                        }

                        if (var4.getBlock() == Blocks.AIR) {
                              var10000 = mc.world;
                              if (!"buy a domain and everything else you need at namecheap.com".equals("ape covered in human flesh")) {
                                    ;
                              }

                              if (var10000.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(var2)).isEmpty()) {
                                    var10000 = mc.world;
                                    if (!"stop skidding".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                                          ;
                                    }

                                    if (var10000.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(var3)).isEmpty()) {
                                          var5 = (0 | 995870890) ^ 416330111 ^ 596343252;
                                          return (boolean)var5;
                                    }
                              }
                        }
                  }
            }

            var5 = ((1870627350 | 1205441430) & 760303644 ^ 200561064) << 2 ^ -1701509392;
            return (boolean)var5;
      }

      private static float getDamageMultiplied(float var0) {
            int var1 = mc.world.getDifficulty().getId();
            if (((536894596 ^ 32798447 | 440388) ^ 569817711) == 0) {
                  ;
            }

            float var10001;
            if (var1 == 0) {
                  var10001 = 0.0F;
            } else {
                  if (!"please go outside".equals("ape covered in human flesh")) {
                        ;
                  }

                  int var10002 = (1 ^ 0) << 4 ^ 18;
                  if ((1246812805 >> 2 ^ 267257131 ^ 494547850) == 0) {
                        ;
                  }

                  if (var1 == var10002) {
                        var10001 = 1.0F;
                        if ((557205114 >>> 4 >> 4 >>> 2 ^ 1533107275) != 0) {
                              ;
                        }
                  } else {
                        var10002 = 0 >> 4 & 435928481 ^ 1;
                        if ((259055109 >>> 4 >> 3 ^ 346254682) != 0) {
                              ;
                        }

                        var10001 = var1 == var10002 ? 0.5F : 1.5F;
                  }
            }

            return var0 * var10001;
      }

      public static float getBlastReduction(EntityLivingBase var0, float var1, Explosion var2) {
            float var10001;
            if (var0 instanceof EntityPlayer) {
                  if (((1820606703 << 4 ^ 126914329) >>> 2 ^ 871702650) == 0) {
                        ;
                  }

                  EntityPlayer var3 = (EntityPlayer)var0;
                  if (((94114804 | 79492412) >> 1 & 45341437 ^ 42252552 ^ -30966740) != 0) {
                        ;
                  }

                  if (((782908754 << 3 >> 3 | 88795836) & 197069329 ^ 1941202851) != 0) {
                        ;
                  }

                  DamageSource var4 = DamageSource.causeExplosionDamage(var2);
                  var10001 = (float)var3.getTotalArmorValue();
                  if (((625699897 >>> 2 >>> 3 ^ 18934726) & 647790 ^ 565286) == 0) {
                        ;
                  }

                  var1 = CombatRules.getDamageAfterAbsorb(var1, var10001, (float)var3.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
                  int var5 = EnchantmentHelper.getEnchantmentModifierDamage(var3.getArmorInventoryList(), var4);
                  float var6 = MathHelper.clamp((float)var5, 0.0F, 20.0F);
                  var1 *= 1.0F - var6 / 25.0F;
                  if (var0.isPotionActive(Potion.getPotionById((10 & 9) >>> 2 ^ 9))) {
                        var1 -= var1 / 4.0F;
                  }

                  var1 = Math.max(var1, 0.0F);
                  return var1;
            } else {
                  var10001 = (float)var0.getTotalArmorValue();
                  double var10002 = var0.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue();
                  if ((115304326 >>> 2 & 8433134 ^ 2133036 ^ 284348596) != 0) {
                        ;
                  }

                  var1 = CombatRules.getDamageAfterAbsorb(var1, var10001, (float)var10002);
                  return var1;
            }
      }

      private void swingArm() {
            mc.player.swingArm(EnumHand.MAIN_HAND);
      }

      public static double[] calculateLookAt(double var0, double var2, double var4, EntityPlayer var6) {
            double var10000 = var6.posX;
            if (!"i hope you catch fire ngl".equals("ape covered in human flesh")) {
                  ;
            }

            double var7 = var10000 - var0;
            double var9 = var6.posY - var2;
            double var11 = var6.posZ - var4;
            var10000 = var7 * var7;
            double var10001 = var9 * var9;
            if ((1388029334 >>> 1 >> 4 ^ 43375916) == 0) {
                  ;
            }

            double var13 = Math.sqrt(var10000 + var10001 + var11 * var11);
            var7 /= var13;
            if (!"you probably spell youre as your".equals("please go outside")) {
                  ;
            }

            var9 /= var13;
            var11 /= var13;
            double var15 = Math.asin(var9);
            double var17 = Math.atan2(var11, var7);
            var15 = var15 * 180.0D / 3.141592653589793D;
            var17 = var17 * 180.0D / 3.141592653589793D;
            if ((117533663 >>> 3 << 3 ^ 117533656) == 0) {
                  ;
            }

            var17 += 90.0D;
            double[] var19 = new double[(1 | 0) >> 4 ^ 1435293934 ^ 1435293932];
            var19[(269297900 >> 2 | 14990514 | 75387199) ^ 83886015] = var17;
            var19[(0 & 788982699 | 1539280214) ^ 1539280215] = var15;
            return var19;
      }
}
