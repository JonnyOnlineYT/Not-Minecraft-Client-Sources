//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.combat;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import me.independed.inceptice.friends.Friend;
import me.independed.inceptice.friends.FriendManager;
import me.independed.inceptice.modules.Module;
import me.independed.inceptice.modules.ModuleManager;
import me.independed.inceptice.settings.BooleanSetting;
import me.independed.inceptice.settings.ModeSetting;
import me.independed.inceptice.settings.NumberSetting;
import me.independed.inceptice.settings.Setting;
import me.independed.inceptice.util.TimerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemAppleGold;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.network.play.client.CPacketPlayer.Rotation;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class TestAura extends Module {
      public BooleanSetting invisibleTarget;
      public ModeSetting modeSetting;
      public BooleanSetting yawTarget;
      public ModeSetting modePrior;
      public BooleanSetting pitchTarget;
      public BooleanSetting noSwingSet;
      public BooleanSetting animalsSet;
      public NumberSetting delaySet;
      public BooleanSetting mobsSet;
      public NumberSetting rangeSet;
      private Random random = new Random();
      private double prevZ;
      public BooleanSetting playersSet;
      TimerUtil hit;
      private double prevY;
      public BooleanSetting swordOnly;
      private double prevX;
      public BooleanSetting autoDelaySet;
      private TimerUtil shieldAttack;
      public NumberSetting fovSet;
      public boolean swichable;

      private static boolean canShootBow(EntityPlayer var0) {
            if (var0.isCreative()) {
                  return (boolean)(0 << 2 & 498484642 & 1567026850 ^ 1);
            } else {
                  if (!"you probably spell youre as your".equals("idiot")) {
                        ;
                  }

                  Iterator var10000 = var0.inventory.mainInventory.iterator();
                  if ((1896172128 >> 1 ^ -512864809) != 0) {
                        ;
                  }

                  Iterator var1 = var10000;

                  while(true) {
                        if ((((1742891511 << 1 & 1906994640) >>> 1 | 319513028) ^ 868966884) != 0) {
                        }

                        if (!var1.hasNext()) {
                              return (boolean)(((829459647 ^ 274233411) >>> 4 >>> 1 | 1770395) ^ 18565055);
                        }

                        ItemStack var2 = (ItemStack)var1.next();
                        if (var2.getItem() == Items.ARROW) {
                              return (boolean)((0 & 91245178) >>> 1 & 1232484511 ^ 1);
                        }

                        if (!"stop skidding".equals("idiot")) {
                              ;
                        }
                  }
            }
      }

      public static float[] getRotations(Entity var0) {
            double var10000 = var0.posX + (var0.posX - var0.lastTickPosX);
            Minecraft var10001 = mc;
            if ((((1025221131 ^ 75796480) & 660111636 | 550006654) ^ 567832446) == 0) {
                  ;
            }

            double var11 = var10001.player.posX;
            if (!"shitted on you harder than archybot".equals("yo mama name maurice")) {
                  ;
            }

            double var1 = var10000 - var11;
            double var3 = var0.posY + (double)var0.getEyeHeight() - mc.player.posY + (double)mc.player.getEyeHeight() - 3.5D;
            var10000 = var0.posZ + (var0.posZ - var0.lastTickPosZ);
            if ((303056596 >>> 4 >> 3 >> 3 >> 1 ^ 1101207537) != 0) {
                  ;
            }

            double var5 = var10000 - mc.player.posZ;
            var10000 = Math.sqrt(Math.pow(var1, 2.0D) + Math.pow(var5, 2.0D));
            if (((1863142608 ^ 170670687 | 1265621997) & 1480890540 ^ 1212455084) == 0) {
                  ;
            }

            double var7 = var10000;
            float var9 = (float)Math.toDegrees(-Math.atan(var1 / var5));
            if (((492183517 ^ 218580420 | 44129105) ^ 317807449) == 0) {
                  ;
            }

            var10000 = -Math.toDegrees(Math.atan(var3 / var7));
            if ((1270473102 << 3 << 4 >> 2 ^ -147050048) == 0) {
                  ;
            }

            float var10 = (float)var10000;
            if (var1 < 0.0D && var5 < 0.0D) {
                  if (((776537270 ^ 715110104 | 7212576) ^ 834289030) != 0) {
                        ;
                  }

                  var9 = (float)(90.0D + Math.toDegrees(Math.atan(var5 / var1)));
            } else {
                  if (!"i hope you catch fire ngl".equals("please go outside")) {
                        ;
                  }

                  if (var1 > 0.0D && var5 < 0.0D) {
                        var9 = (float)(-90.0D + Math.toDegrees(Math.atan(var5 / var1)));
                  }
            }

            float[] var12 = new float[(0 & 1008286626) >>> 2 >> 1 ^ 2];
            int var10002 = (718728134 | 579791968) >> 1 ^ 359626739;
            if ((((640201651 ^ 21845505) << 1 & 516074967) >>> 3 ^ -1209621251) != 0) {
                  ;
            }

            var12[var10002] = var9;
            var12[(0 ^ 1247359191) >> 1 >>> 4 ^ 38979975] = var10;
            return var12;
      }

      public TestAura() {
            super("TestAura", "automatically hits enemies", 2029080776 >>> 4 ^ 93325223 ^ 35590827, Module.Category.COMBAT);
            if ((407791913 >>> 1 << 3 >> 1 ^ 815583824) == 0) {
                  ;
            }

            this.hit = new TimerUtil();
            this.swichable = (boolean)(((0 << 1 ^ 1249225552) & 701762600 | 130130375) ^ 265660870);
            this.rangeSet = new NumberSetting("Range", this, 3.5D, 3.0D, 10.0D, 0.1D);
            NumberSetting var10001 = new NumberSetting;
            if ((364382949 << 1 << 3 >> 2 & 181641829 ^ 46146052) == 0) {
                  ;
            }

            var10001.<init>("Fov", this, 360.0D, 0.0D, 360.0D, 2.0D);
            this.fovSet = var10001;
            if (!"your mom your dad the one you never had".equals("intentMoment")) {
                  ;
            }

            BooleanSetting var1 = new BooleanSetting;
            if (((1268165210 << 4 << 3 | 621165559) ^ -279494665) == 0) {
                  ;
            }

            var1.<init>("No Swing", this, (boolean)(807539319 >> 2 << 4 ^ 1025207281 ^ -40689119));
            this.noSwingSet = var1;
            var1 = new BooleanSetting;
            if (!"you probably spell youre as your".equals("yo mama name maurice")) {
                  ;
            }

            var1.<init>("Animals", this, (boolean)(1087325650 >>> 3 ^ 933164 ^ 135779734));
            this.animalsSet = var1;
            this.playersSet = new BooleanSetting("Players", this, (boolean)(((0 >>> 1 | 766279503) << 1 | 415052220) ^ 1543372735));
            var1 = new BooleanSetting;
            if (!"stop skidding".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                  ;
            }

            int var10005 = (1092590072 >> 2 | 8703726) ^ 281538302;
            if ((1593877129 << 3 >>> 3 ^ 520135305) == 0) {
                  ;
            }

            var1.<init>("Mobs", this, (boolean)var10005);
            this.mobsSet = var1;
            if (!"ape covered in human flesh".equals("i hope you catch fire ngl")) {
                  ;
            }

            this.invisibleTarget = new BooleanSetting("Invisible", this, (boolean)(1025751775 >>> 2 & 223677662 ^ 222301334));
            this.autoDelaySet = new BooleanSetting("AutoDelay", this, (boolean)(((0 | 853755059) & 191002417) >>> 4 ^ 2499650));
            var10001 = new NumberSetting;
            if (!"please get a girlfriend and stop cracking plugins".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                  ;
            }

            if (!"Some babies were dropped on their heads but you were clearly thrown at a wall.".equals("i hope you catch fire ngl")) {
                  ;
            }

            var10001.<init>("Delay", this, 590.0D, 1.0D, 1500.0D, 1.0D);
            this.delaySet = var10001;
            if (((50575249 >>> 1 ^ 1556037) >> 4 >>> 4 ^ 80319310) != 0) {
                  ;
            }

            this.yawTarget = new BooleanSetting("Yaw", this, (boolean)((0 >>> 4 >> 3 ^ 1742003279) << 4 ^ 2102248689));
            this.swordOnly = new BooleanSetting("SwordOnly", this, (boolean)((36018825 | 4458034 | 15376317) << 4 ^ 788134896));
            var1 = new BooleanSetting;
            if (!"please get a girlfriend and stop cracking plugins".equals("you probably spell youre as your")) {
                  ;
            }

            var1.<init>("Pitch", this, (boolean)(2025321589 >> 3 & 155800905 ^ 151016456));
            this.pitchTarget = var1;
            ModeSetting var2 = new ModeSetting;
            if (((1209958542 | 116168321) >> 1 ^ 662653767) == 0) {
                  ;
            }

            String[] var10006 = new String[(0 & 2145115382 | 1510453740) >>> 3 ^ 188806719];
            if ((((1244339191 ^ 512347057 | 684166127) >>> 1 | 599098088) ^ -432400523) != 0) {
                  ;
            }

            var10006[(181267524 >>> 3 >> 2 | 1619708) ^ 6225918] = "Bypass";
            var10006[0 >>> 1 & 775730951 ^ 1] = "Simple";
            var2.<init>("Mode", this, "Bypass", var10006);
            this.modeSetting = var2;
            var2 = new ModeSetting;
            var10006 = new String[(1 & 0 ^ 2146633138 | 869292920) >> 2 ^ 536664061];
            var10006[(1082739976 << 1 & 730387558) >> 2 & 2618880 ^ 9216] = "Distance";
            int var10008 = (0 & 442755187) >>> 4 ^ 1;
            if ((((838278660 | 29748721) & 514993826) << 3 & 1962307770 ^ -1958984551) != 0) {
                  ;
            }

            var10006[var10008] = "Health";
            var10006[(1 << 1 >>> 4 | 723234184) ^ 723234186] = "LivingTime";
            var2.<init>("Priority", this, "Distance", var10006);
            this.modePrior = var2;
            this.shieldAttack = new TimerUtil();
            Setting[] var3 = new Setting[(0 & 2029737352) >> 2 >>> 1 & 1720047 ^ 1];
            var3[(1234199895 | 425448914) << 4 ^ -1648403088] = this.rangeSet;
            this.addSettings(var3);
            var3 = new Setting[0 << 1 ^ 2035260781 ^ 2035260780];
            var3[33624161 >> 1 ^ 16812080] = this.noSwingSet;
            this.addSettings(var3);
            var3 = new Setting[0 >>> 2 >> 1 ^ 1];
            if (((532899093 << 2 << 3 | 65222223) ^ 1334398961 ^ 1085537093 ^ -187366821) == 0) {
                  ;
            }

            if ((14897495 >>> 3 >> 4 << 4 & 1291661 ^ -1250583090) != 0) {
                  ;
            }

            var3[(243006887 << 1 ^ 451115778) << 3 ^ 769414865 ^ 494425265] = this.animalsSet;
            this.addSettings(var3);
            if (((1669846525 >> 4 | 64728378) ^ 133954943) == 0) {
                  ;
            }

            var3 = new Setting[(0 << 3 >>> 3 ^ 1994340986) >>> 3 ^ 249292622];
            if (!"yo mama name maurice".equals("nefariousMoment")) {
                  ;
            }

            var3[(428241174 >> 2 | 103635112) >>> 1 ^ 53931638] = this.playersSet;
            this.addSettings(var3);
            var3 = new Setting[0 << 4 >> 1 >>> 3 ^ 1717486345 ^ 1717486347];
            var3[(1139316391 ^ 404213103 | 171180700 | 261830298) ^ 1610579934] = this.mobsSet;
            var3[(0 | 98084038 | 72737274) ^ 98428415] = this.invisibleTarget;
            this.addSettings(var3);
            var3 = new Setting[0 & 1185529608 & 2028977496 ^ 1];
            var3[(18382880 | 7130089) ^ 24955881] = this.modeSetting;
            this.addSettings(var3);
            var3 = new Setting[(0 >> 2 | 97446445 | 6992492) ^ 99547756];
            if (!"please take a shower".equals("shitted on you harder than archybot")) {
                  ;
            }

            var3[(580190792 << 3 | 290739990) << 4 ^ 1606776160] = this.fovSet;
            if (!"shitted on you harder than archybot".equals("nefariousMoment")) {
                  ;
            }

            this.addSettings(var3);
            var3 = new Setting[(1 ^ 0) & 0 ^ 2];
            var3[(1001770711 << 1 & 1229477613) << 4 & 180631049 ^ 8389120] = this.modePrior;
            var3[(0 & 179023237) >>> 3 ^ 1] = this.swordOnly;
            this.addSettings(var3);
            var3 = new Setting[(3 & 1) >> 1 ^ 4];
            var3[(2008856211 | 692532512) >> 3 & 168830443 ^ 168830178] = this.yawTarget;
            int var10003 = 0 >>> 4 >>> 3 >> 2 >>> 1 ^ 1;
            BooleanSetting var10004 = this.pitchTarget;
            if (((1131798361 | 141952925) ^ 617739125 ^ 1873161384) == 0) {
                  ;
            }

            var3[var10003] = var10004;
            var10003 = 1 << 1 >>> 2 & 113835005 ^ 2;
            if ((1946467404 >>> 2 >> 1 ^ -712782236) != 0) {
                  ;
            }

            var3[var10003] = this.autoDelaySet;
            var3[0 >>> 4 << 3 >> 4 << 3 ^ 3] = this.delaySet;
            this.addSettings(var3);
            if ((1475183199 >> 1 ^ 112100644 ^ 132537708 ^ 717101415) == 0) {
                  ;
            }

      }

      private boolean attackCheck(Entity var1) {
            if (!"yo mama name maurice".equals("nefariousMoment")) {
                  ;
            }

            Iterator var2 = FriendManager.friends.iterator();

            while(var2.hasNext()) {
                  Friend var3 = (Friend)var2.next();
                  if (var1.getName() == var3.name) {
                        return (boolean)(((1461693221 ^ 1015351334) & 1068129027 | 455730085) ^ 368625357 ^ 777201002);
                  }
            }

            boolean var10000;
            if (!this.invisibleTarget.isEnabled()) {
                  var10000 = var1.isInvisible();
                  if ((((810387626 | 228596986) ^ 524072818) << 2 ^ -692503406) != 0) {
                        ;
                  }

                  if (var10000) {
                        return (boolean)((513712248 << 3 >> 1 | 1355393895) & 1499699265 ^ 1482917953);
                  }
            }

            if (this.playersSet.isEnabled()) {
                  var10000 = var1 instanceof EntityPlayer;
                  if (((278857096 | '肛') ^ 70768635 ^ 346575456) == 0) {
                        ;
                  }

                  if (var10000 && ((EntityPlayer)var1).getHealth() > 0.0F) {
                        return (boolean)(((0 ^ 1318668039) & 562764036) >> 3 ^ 1122337);
                  }
            }

            if (this.animalsSet.isEnabled() && var1 instanceof EntityAnimal) {
                  return (boolean)(!(var1 instanceof EntityTameable) ? ((0 & 1376651069) >> 3 | 1640199897) ^ 1640199896 : 1486921815 >> 2 & 153415735 & 1424399 & 5609 ^ 1);
            } else {
                  return (boolean)(this.mobsSet.isEnabled() && var1 instanceof EntityMob ? (0 & 744086426) << 3 >>> 4 ^ 1 : 1273193586 >> 2 >> 2 ^ 79574599);
            }
      }

      public void attackTarget(Entity var1) {
            if (var1 instanceof EntityPlayer) {
                  if (((747103555 << 3 & 1233878686) >>> 2 ^ 268618374) == 0) {
                        ;
                  }

                  if (TestAura.isPlayerShielded((EntityPlayer)var1) && mc.player.getHeldItemMainhand().getItem() instanceof ItemAxe && this.shieldAttack.hasReached(60.0D)) {
                        mc.playerController.attackEntity(mc.player, var1);
                        this.shieldAttack.reset();
                  }
            }

            if (this.autoDelaySet.isEnabled()) {
                  if ((285212696 << 2 ^ 1843912232) != 0) {
                        ;
                  }

                  if ((double)mc.player.getCooledAttackStrength(1.0F) < 1.0D) {
                        return;
                  }
            } else if (!this.hit.hasReached(this.delaySet.getValue())) {
                  return;
            }

            if (((164065184 ^ 69751023) >> 3 << 1 >> 1 ^ -81813392) != 0) {
                  ;
            }

            ModeSetting var10000 = this.modeSetting;
            if (!"Some babies were dropped on their heads but you were clearly thrown at a wall.".equals("you probably spell youre as your")) {
                  ;
            }

            float[] var3;
            float[] var5;
            EntityPlayerSP var6;
            Minecraft var7;
            EntityPlayerSP var10;
            PlayerControllerMP var13;
            float var10001;
            int var10003;
            if (var10000.activeMode == "Bypass") {
                  if (((1675214371 << 2 | 450530527) >> 2 << 2 ^ 469197212) != 0) {
                        ;
                  }

                  if (ModuleManager.getModuleByName("Criticals").isToggled()) {
                        if (!Criticals.isRage) {
                              this.hit.reset();
                              var5 = (float[])TestAura.getRotations(var1);
                              if (!"please get a girlfriend and stop cracking plugins".equals("nefariousMoment")) {
                                    ;
                              }

                              var3 = var5;
                              var10001 = var3[(1943296993 >>> 2 >> 1 | 200106042) << 1 ^ 536860412];
                              if (!"please get a girlfriend and stop cracking plugins".equals("you probably spell youre as your")) {
                                    ;
                              }

                              this.setRotation(var10001, var3[(0 >> 3 >>> 1 >>> 1 & 1608929455) >> 3 ^ 1]);
                              this.prevX = mc.player.posX;
                              if ((970879207 >> 4 >>> 2 ^ 1542401 ^ 15790274) == 0) {
                                    ;
                              }

                              this.prevY = mc.player.posY;
                              this.prevZ = mc.player.posZ;
                              var6 = mc.player;
                              double var8 = var1.posX;
                              if ((33702536 >> 3 ^ 3876204 ^ -629969342) != 0) {
                                    ;
                              }

                              var6.setPositionAndRotationDirect(var8, var1.posY, var1.posZ, mc.player.rotationYaw, mc.player.rotationPitch, (20 & 19 & 0) >> 2 & 270216509 ^ 24, (boolean)(((0 | 2064578358) >> 3 << 2 ^ 392098616) << 3 ^ 1456227585));
                              if (((6626237 << 1 << 2 | 47113289 | 11862687) << 1 ^ 774319516) != 0) {
                                    ;
                              }

                              mc.player.setSprinting((boolean)(1614968199 >> 4 << 2 & 280109807 ^ 269484128));
                              mc.playerController.attackEntity(mc.player, var1);
                              if (!this.noSwingSet.enabled) {
                                    if ((19005960 << 4 ^ 125129477 ^ 357921669) == 0) {
                                          ;
                                    }

                                    var6 = mc.player;
                                    if (!"buy a domain and everything else you need at namecheap.com".equals("please dont crack my plugin")) {
                                          ;
                                    }

                                    var6.swingArm(EnumHand.MAIN_HAND);
                              }
                        } else if (!mc.player.onGround) {
                              var7 = mc;
                              if (((60084747 ^ 48813113) >> 4 << 3 ^ 437171061) != 0) {
                                    ;
                              }

                              double var9 = (double)var7.player.fallDistance;
                              if (!"You're so fat whenever you go to the beach the tide comes in.".equals("intentMoment")) {
                                    ;
                              }

                              if (var9 >= 0.15D) {
                                    this.hit.reset();
                                    var3 = (float[])TestAura.getRotations(var1);
                                    if ((17072128 >> 1 >>> 1 ^ 1469847805) != 0) {
                                          ;
                                    }

                                    var10001 = var3[((1210510242 >> 2 ^ 274646935) >>> 2 | 5089397) ^ 14548863];
                                    var10003 = 0 >> 2 >>> 1 << 3 ^ 1;
                                    if (((537919684 ^ 457086563 | 201055200) ^ 1006624743) == 0) {
                                          ;
                                    }

                                    this.setRotation(var10001, var3[var10003]);
                                    if (((376201166 ^ 243426182) << 2 >>> 4 ^ -33562104) != 0) {
                                          ;
                                    }

                                    if ((201334928 << 3 >>> 4 ^ 1472876640) != 0) {
                                          ;
                                    }

                                    var7 = mc;
                                    if ((((1926386747 | 381146849) >> 3 ^ 141190956) >> 1 ^ 29669788 ^ 43972453) == 0) {
                                          ;
                                    }

                                    var7.player.setSprinting((boolean)((2067714434 | 66070310) >>> 1 ^ 1040153555));
                                    if (!"stop skidding".equals("buy a domain and everything else you need at namecheap.com")) {
                                          ;
                                    }

                                    var7 = mc;
                                    if (!"ape covered in human flesh".equals("shitted on you harder than archybot")) {
                                          ;
                                    }

                                    var13 = var7.playerController;
                                    var10 = mc.player;
                                    if (!"your mom your dad the one you never had".equals("your mom your dad the one you never had")) {
                                          ;
                                    }

                                    var13.attackEntity(var10, var1);
                                    if (!this.noSwingSet.enabled) {
                                          var6 = mc.player;
                                          if (((1268944913 | 919205960) >>> 2 ^ 536542742) == 0) {
                                                ;
                                          }

                                          var6.swingArm(EnumHand.MAIN_HAND);
                                    }
                              }
                        }
                  } else if (!ModuleManager.getModuleByName("Criticals").isToggled()) {
                        if (((162688308 >>> 4 >>> 4 | 17557 | 402796) ^ 1046525) == 0) {
                              ;
                        }

                        this.hit.reset();
                        var3 = (float[])TestAura.getRotations(var1);
                        this.setRotation(var3[((1525815431 >>> 3 | 143414804) ^ 104977440) & 116600366 ^ 76743204], var3[((0 & 1371878245 | 376725578) >>> 3 | 25372528) ^ 63942520]);
                        var7 = mc;
                        if (!"please take a shower".equals("You're so fat whenever you go to the beach the tide comes in.")) {
                              ;
                        }

                        var7.playerController.attackEntity(mc.player, var1);
                        if (!this.noSwingSet.enabled) {
                              mc.player.swingArm(EnumHand.MAIN_HAND);
                        }

                        if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                              ;
                        }
                  }
            } else {
                  float[] var4;
                  NetHandlerPlayClient var17;
                  boolean var19;
                  if (ModuleManager.getModuleByName("Criticals").isToggled() && !mc.player.onGround) {
                        if (Criticals.isRage) {
                              this.hit.reset();
                              int var20 = ((0 & 2101999779) >>> 1 ^ 1703734740) >>> 1 ^ 851867368;
                              if (((330266862 << 3 | 1144846129) ^ -578816143) == 0) {
                                    ;
                              }

                              var5 = new float[var20];
                              var5[16866461 << 4 >> 4 >>> 1 ^ 4172376 ^ 12518422] = mc.player.rotationYaw;
                              var5[(0 | 1446259278) << 4 ^ 1665311969] = mc.player.rotationPitch;
                              var3 = var5;
                              if (((870093247 | 319709587 | 246766008) >> 1 ^ 536870111) == 0) {
                                    ;
                              }

                              var4 = (float[])TestAura.getRotations(var1);
                              var10001 = var4[1306488783 >> 2 << 4 & 262300700 & 69306610 ^ 69206032];
                              float var11 = var4[0 >> 1 >>> 2 >>> 2 ^ 1];
                              if ((1870646223 << 2 >>> 3 >> 4 & 8809071 ^ -1041576489) != 0) {
                                    ;
                              }

                              this.setRotation(var10001, var11);
                              mc.player.setSprinting((boolean)(997004401 ^ 110868430 ^ 325230227 ^ 429695683 ^ 923312111));
                              float var21 = var4[1258484072 >> 2 >>> 1 & 14006245 & 1566806 ^ 4];
                              if ((((473949776 >> 1 >> 3 & 4605647) >>> 4 | 175889) ^ -2036094813) != 0) {
                                    ;
                              }

                              if (var21 < 90.0F) {
                                    var4[1771605375 >>> 2 & 321014016 & 60481117 ^ 26225774 ^ 59911278] = 360.0F - (90.0F - var4[1237528103 >> 4 >>> 3 & 7441521 ^ 1147984]);
                              } else {
                                    var4[(1918568703 | 1916459196) ^ 654796254 ^ 1434238241] -= 90.0F;
                              }

                              mc.player.connection.sendPacket(new Rotation(var4[5510767 << 3 & 24599936 ^ 2101504], mc.player.rotationPitch, (boolean)(((719042481 | 309510476) >> 4 & 799864) >> 3 ^ 99983)));
                              var7 = mc;
                              if (((6489216 >> 4 << 4 | 4250110) ^ 6544894) == 0) {
                                    ;
                              }

                              var7.playerController.attackEntity(mc.player, var1);
                              if (!"please go outside".equals("you're dogshit")) {
                                    ;
                              }

                              if (!this.noSwingSet.enabled) {
                                    var17 = mc.player.connection;
                                    if (((962439709 | 230963272) ^ 56178894 ^ 1048871571) == 0) {
                                          ;
                                    }

                                    var17.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
                                    var6 = mc.player;
                                    if (!"you're dogshit".equals("stop skidding")) {
                                          ;
                                    }

                                    var6.swingArm(EnumHand.MAIN_HAND);
                              }

                              mc.player.setSprinting((boolean)(459444052 << 2 ^ 1600971377 ^ 853991201));
                              if (!"stop skidding".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                                    ;
                              }

                              this.setRotation(var3[553796352 >>> 4 & 18552634 ^ 1049648], var3[0 >> 4 >>> 4 >>> 4 ^ 772787590 ^ 772787591]);
                        } else if (!mc.player.onGround && (double)mc.player.fallDistance >= 0.15D) {
                              this.hit.reset();
                              var3 = (float[])TestAura.getRotations(var1);
                              this.setRotation(var3[71385600 >>> 3 ^ 8923200], var3[0 << 2 >>> 2 << 4 ^ 1]);
                              mc.player.setSprinting((boolean)((2078662613 >>> 2 | 173592899) & 95010503 ^ 78231239));
                              mc.playerController.attackEntity(mc.player, var1);
                              if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("nefariousMoment")) {
                                    ;
                              }

                              if (!this.noSwingSet.enabled) {
                                    mc.player.swingArm(EnumHand.MAIN_HAND);
                              }

                              if ((352712718 >>> 2 ^ 30218559 ^ 1803844360) != 0) {
                                    ;
                              }
                        } else if (!ModuleManager.getModuleByName("Criticals").isToggled()) {
                              TimerUtil var22 = this.hit;
                              if (((1828242165 >>> 4 ^ 90610807) & 18486765 ^ 17305672) == 0) {
                                    ;
                              }

                              var22.reset();
                              var3 = (float[])TestAura.getRotations(var1);
                              this.setRotation(var3[(268448897 | 131417026) ^ 399865795], var3[(0 >> 2 ^ 1627961346) & 1180462446 ^ 1074276355]);
                              mc.playerController.attackEntity(mc.player, var1);
                              var19 = this.noSwingSet.enabled;
                              if ((((717857187 | 152655542) << 2 ^ 1044055858 | 1813913993) ^ 1023868206) != 0) {
                                    ;
                              }

                              if (!var19) {
                                    var7 = mc;
                                    if (((32286845 ^ 25680109) >> 4 >> 4 ^ 740814990) != 0) {
                                          ;
                                    }

                                    var7.player.swingArm(EnumHand.MAIN_HAND);
                                    if (!"ape covered in human flesh".equals("stop skidding")) {
                                          ;
                                    }
                              }
                        }
                  } else if (!ModuleManager.getModuleByName("Criticals").isToggled()) {
                        this.hit.reset();
                        var5 = new float[(0 << 4 | 438813114 | 13922644) ^ 99387165 ^ 521893601];
                        int var10002 = (1785167279 >>> 3 >> 2 >>> 3 | 867138) << 4 ^ 116914160;
                        float var15 = mc.player.rotationYaw;
                        if ((233336038 << 1 >>> 2 ^ -774760326) != 0) {
                              ;
                        }

                        var5[var10002] = var15;
                        if (!"ape covered in human flesh".equals("you're dogshit")) {
                              ;
                        }

                        var10002 = (0 & 1187607760) << 2 >> 2 >> 3 ^ 1;
                        var15 = mc.player.rotationPitch;
                        if (((354745109 >>> 3 & 41379237) >> 4 >> 2 >>> 4 ^ 768614423) != 0) {
                              ;
                        }

                        var5[var10002] = var15;
                        var3 = var5;
                        var4 = (float[])TestAura.getRotations(var1);
                        var10001 = var4[((878979307 | 742932432) ^ 375415395) << 4 << 2 ^ -2095618560];
                        var10003 = (0 | 1691893174) >> 3 ^ 211486647;
                        if (!"You're so fat whenever you go to the beach the tide comes in.".equals("stop skidding")) {
                              ;
                        }

                        this.setRotation(var10001, var4[var10003]);
                        mc.player.setSprinting((boolean)(715394564 >>> 3 >>> 4 ^ 5589020));
                        if (!"stop skidding".equals("nefariousMoment")) {
                              ;
                        }

                        if (var4[(854746605 | 727946214) >>> 3 >>> 1 ^ 62865278] < 90.0F) {
                              int var12 = (1086390422 | 793790501 | 615003183) & 378125728 ^ 109657248;
                              if (((1385914480 << 1 & 1375824804) >>> 2 ^ 1767 ^ 5839) == 0) {
                                    ;
                              }

                              var4[var12] = 360.0F - (90.0F - var4[780781905 << 2 >>> 3 >>> 2 << 3 ^ 780781904]);
                        } else {
                              var4[((560523282 ^ 116816880) >> 4 | 27185267 | 32754393) ^ 67107839] -= 90.0F;
                        }

                        var17 = mc.player.connection;
                        Rotation var14 = new Rotation;
                        if (!"please dont crack my plugin".equals("nefariousMoment")) {
                              ;
                        }

                        int var10004 = 1374222158 >> 1 & 384717265 ^ 14963073;
                        if (((2034301487 ^ 1633490518) >>> 3 ^ -1089751417) != 0) {
                              ;
                        }

                        var14.<init>(var4[var10004], mc.player.rotationPitch, (boolean)(983578918 << 3 >> 1 << 1 ^ -721303248));
                        var17.sendPacket(var14);
                        if (((1044195137 | 242210744) & 955049094 ^ -1736426404) != 0) {
                              ;
                        }

                        var13 = mc.playerController;
                        var10 = mc.player;
                        if ((15267927 >>> 1 ^ 5584327 ^ 167794 ^ 121793253) != 0) {
                              ;
                        }

                        var13.attackEntity(var10, var1);
                        var19 = this.noSwingSet.enabled;
                        if (!"please take a shower".equals("nefariousMoment")) {
                              ;
                        }

                        if (!var19) {
                              var6 = mc.player;
                              if (!"your mom your dad the one you never had".equals("please get a girlfriend and stop cracking plugins")) {
                                    ;
                              }

                              var17 = var6.connection;
                              CPacketPlayerTryUseItem var16 = new CPacketPlayerTryUseItem;
                              EnumHand var18 = EnumHand.MAIN_HAND;
                              if (((134484481 >> 3 >> 1 | 967099) ^ 1446304686) != 0) {
                                    ;
                              }

                              var16.<init>(var18);
                              var17.sendPacket(var16);
                              if (((327679402 ^ 98363561 | 351497775) >> 3 ^ 356090568) != 0) {
                                    ;
                              }

                              var6 = mc.player;
                              if (((1727455369 >> 3 << 1 | 288549205) ^ 432011639) == 0) {
                                    ;
                              }

                              var6.swingArm(EnumHand.MAIN_HAND);
                        }

                        mc.player.setSprinting((boolean)(629001307 << 3 << 4 >>> 4 ^ 200172248));
                        this.setRotation(var3[595598624 >>> 3 & 8583838 ^ 644], var3[0 >>> 3 >>> 1 << 3 >>> 1 ^ 1]);
                  }
            }

      }

      public static String getPlayerName(EntityPlayer var0) {
            return "null";
      }

      @SubscribeEvent
      public void onPlayerTick(PlayerTickEvent var1) {
            if (mc.player != null && !mc.player.isDead) {
                  Stream var10000 = mc.world.loadedEntityList.stream();
                  Predicate var10001 = (var0) -> {
                        if ((69669 << 3 >>> 4 ^ -1502779038) != 0) {
                              ;
                        }

                        int var10000;
                        if (var0 != mc.player) {
                              var10000 = ((0 ^ 220745945 | 9451208) & 50905606) << 1 ^ 34636801;
                              if (((243982163 ^ 152194007) >> 3 ^ 6591031 ^ -1717839070) != 0) {
                                    ;
                              }
                        } else {
                              var10000 = (1738534119 >> 2 << 1 | 571689886) ^ 870318078;
                        }

                        return (boolean)var10000;
                  };
                  if ((1948065183 >>> 2 << 2 & 370495091 & 50006402 ^ 1116160) == 0) {
                        ;
                  }

                  List var2 = (List)var10000.filter(var10001).filter((var1x) -> {
                        int var10000;
                        if ((double)mc.player.getDistance(var1x) <= this.rangeSet.getValue()) {
                              if (!"stringer is a good obfuscator".equals("intentMoment")) {
                                    ;
                              }

                              var10000 = 0 >> 4 >> 2 >> 2 >> 2 << 4 ^ 1;
                        } else {
                              var10000 = (2015031090 << 3 | 463515734 | 1560030981) & 179932984 ^ 179932944;
                        }

                        return (boolean)var10000;
                  }).filter((var0) -> {
                        return (boolean)(!var0.isDead ? (0 | 214173883) >>> 3 >>> 1 ^ 13385866 : 43024904 >> 3 ^ 5378113);
                  }).filter((var1x) -> {
                        return this.attackCheck(var1x);
                  }).filter((var0) -> {
                        if (((65951023 ^ 54826176) >> 2 >>> 3 ^ 305512 ^ 130919) == 0) {
                              ;
                        }

                        return (boolean)(var0.ticksExisted > (20 & 10 & 643000080 ^ 897791326 ^ 897791296) ? (0 & 661624406) << 3 ^ 1 : ((803642115 | 678118627) ^ 533146876 | 705679643) ^ 976220959);
                  }).filter((var1x) -> {
                        float var10000 = ((float[])TestAura.getRotations(var1x))[(2109852931 << 2 | 954622752) << 4 & 519727290 ^ 510812288] - var1x.rotationYaw;
                        if ((149857298 << 1 ^ 299714596) == 0) {
                              ;
                        }

                        int var2;
                        if ((double)(Math.abs(var10000) % 180.0F) < this.fovSet.getValue() / 2.0D) {
                              var2 = ((0 ^ 2088818860) & 807309727 | 685895808) ^ 954331277;
                        } else {
                              if ((((246075352 | 241451683) << 1 ^ 123570757 | 305257685) << 3 ^ -817541162) != 0) {
                                    ;
                              }

                              var2 = (2110033817 >>> 2 >>> 2 | 7680257) ^ 134052345;
                        }

                        return (boolean)var2;
                  }).collect(Collectors.toList());
                  if (this.modePrior.activeMode == "Distance") {
                        var2 = this.sortByDistance(var2);
                  } else {
                        if (((70315926 >> 3 | 5069248) & 4412763 ^ 326757079) != 0) {
                              ;
                        }

                        if (this.modePrior.activeMode == "Health") {
                              var2 = this.sortByHealth(var2);
                        } else {
                              var2 = this.sortByLivingTime(var2);
                        }
                  }

                  if ((8979008 << 2 ^ -1198609966) != 0) {
                        ;
                  }

                  if (var2.size() > 0) {
                        if (this.swordOnly.isEnabled()) {
                              boolean var3 = mc.player.getHeldItemMainhand().getItem() instanceof ItemSword;
                              if (!"please go outside".equals("please get a girlfriend and stop cracking plugins")) {
                                    ;
                              }

                              if (var3) {
                                    this.attackTarget((Entity)var2.get(((201376164 ^ 19317005) & 65234778 | 15844301) ^ 32752589));
                                    return;
                              }
                        }

                        if (!this.swordOnly.isEnabled()) {
                              this.attackTarget((Entity)var2.get((1023388843 >>> 2 & 174335873) << 1 ^ 340280832));
                        }
                  }

            }
      }

      private static boolean isPlayerShielded(EntityPlayer var0) {
            int var10000;
            if (var0.getItemInUseCount() <= 0 || !(var0.getHeldItemMainhand().getItem() instanceof ItemShield) && (!(var0.getHeldItemOffhand().getItem() instanceof ItemShield) || TestAura.isPlayerUsingMainhand(var0))) {
                  if ((((319424812 ^ 303323888) >>> 2 | 947573) ^ 419201655) != 0) {
                        ;
                  }

                  var10000 = 47861193 << 3 >> 2 ^ 95722386;
            } else {
                  var10000 = (0 >> 1 | 456494251) >>> 2 & 23586959 ^ 4546571;
            }

            return (boolean)var10000;
      }

      private void setRotation(float var1, float var2) {
            if (((294940 >>> 2 ^ 12659) << 1 ^ 410835656) != 0) {
                  ;
            }

            mc.player.renderYawOffset = var1;
            mc.player.rotationYawHead = var1;
            Random var10000 = this.random;
            if (((34083328 | 990111) >>> 3 ^ -1972355232) != 0) {
                  ;
            }

            if (var10000.nextBoolean()) {
                  var1 = (float)((double)var1 + (double)this.random.nextInt((25 ^ 17) >> 4 ^ 100) * 0.02D);
            } else {
                  var1 = (float)((double)var1 - (double)this.random.nextInt((52 | 16) ^ 45 ^ 11 ^ 118) * 0.02D);
            }

            double var3;
            double var10001;
            if (this.random.nextBoolean()) {
                  if (!"stringer is a good obfuscator".equals("shitted on you harder than archybot")) {
                        ;
                  }

                  var3 = (double)var2;
                  var10001 = (double)this.random.nextInt(71 << 1 >>> 4 ^ 108);
                  if ((((289297139 | 76691891) ^ 358335421) & 1932177 & 66586 ^ 65536) == 0) {
                        ;
                  }

                  var2 = (float)(var3 + var10001 * 0.04D);
                  if (((265809675 >>> 2 | 13843285) >> 2 ^ 16645877) == 0) {
                        ;
                  }
            } else {
                  if (((1081718179 ^ 1040421006) << 3 ^ 1571203118 ^ -1367899834) == 0) {
                        ;
                  }

                  var3 = (double)var2;
                  var10001 = (double)this.random.nextInt((35 << 3 & 188 ^ 19 | 0) ^ 111);
                  if (((1065482898 ^ 194714685) & 571619246 ^ 266037532 ^ 801722290) == 0) {
                        ;
                  }

                  var2 = (float)(var3 - var10001 * 0.04D);
            }

            if (!"intentMoment".equals("yo mama name maurice")) {
                  ;
            }

            EntityPlayerSP var4;
            Random var10002;
            if (this.yawTarget.isEnabled()) {
                  if (var1 >= 0.0F) {
                        if (mc.player.rotationYaw < var1) {
                              while(mc.player.rotationYaw < var1) {
                                    var4 = mc.player;
                                    var4.rotationYaw = (float)((double)var4.rotationYaw + (double)this.random.nextInt((14 >> 4 ^ 1505584707 | 386717499) ^ 1606285087) * 0.001D);
                              }
                        } else {
                              while(mc.player.rotationYaw > var1) {
                                    var4 = mc.player;
                                    var4.rotationYaw = (float)((double)var4.rotationYaw - (double)this.random.nextInt((94 >> 2 ^ 13) & 5 ^ 100) * 0.001D);
                              }
                        }
                  } else if (mc.player.rotationYaw < var1) {
                        while(true) {
                              if (((((1751094074 | 1118238231) ^ 1742759617) >> 3 | 100097) >>> 3 ^ 1782577456) != 0) {
                                    ;
                              }

                              if (mc.player.rotationYaw >= var1) {
                                    break;
                              }

                              var4 = mc.player;
                              var10001 = (double)var4.rotationYaw;
                              var10002 = this.random;
                              if ((((554690127 ^ 499138993) & 688120872) >>> 3 ^ 83915909) == 0) {
                                    ;
                              }

                              var4.rotationYaw = (float)(var10001 + (double)var10002.nextInt((49 >>> 2 & 2 | 886107837) ^ 886107865) * 0.001D);
                        }
                  } else {
                        while(mc.player.rotationYaw > var1) {
                              var4 = mc.player;
                              var4.rotationYaw = (float)((double)var4.rotationYaw - (double)this.random.nextInt(99 >>> 2 >> 4 >> 2 << 1 ^ 561144850 ^ 561144950) * 0.001D);
                        }
                  }
            }

            if (this.pitchTarget.isEnabled()) {
                  if (((1269555427 ^ 1161336133 | 68456894) ^ 244839870) == 0) {
                        ;
                  }

                  double var5;
                  if (var2 >= 0.0F) {
                        if (((1437199355 << 1 & 227789886) >> 2 ^ 22108047 ^ -2072087048) != 0) {
                              ;
                        }

                        if (mc.player.rotationPitch < var2) {
                              for(; mc.player.rotationPitch < var2; var4.rotationPitch = (float)(var10001 + (double)this.random.nextInt(((64 ^ 25) >> 1 | 30) ^ 90) * 0.001D)) {
                                    var4 = mc.player;
                                    var10001 = (double)var4.rotationPitch;
                                    if (('耀' >>> 1 ^ 16384) == 0) {
                                          ;
                                    }
                              }
                        } else {
                              while(true) {
                                    if ((761186379 << 3 << 1 ^ -705919824) == 0) {
                                          ;
                                    }

                                    if (mc.player.rotationPitch <= var2) {
                                          break;
                                    }

                                    var4 = mc.player;
                                    var10001 = (double)var4.rotationPitch;
                                    if ((1732909484 << 1 >> 3 ^ 1167913868) != 0) {
                                          ;
                                    }

                                    var10002 = this.random;
                                    int var10003 = 7 << 4 << 3 ^ 996;
                                    if ((104342534 << 4 ^ 1669480544) == 0) {
                                          ;
                                    }

                                    var5 = (double)var10002.nextInt(var10003);
                                    if ((((429075696 | 44066350 | 255317454) ^ 115649762 | 351708663) ^ 502790143) == 0) {
                                          ;
                                    }

                                    var4.rotationPitch = (float)(var10001 - var5 * 0.001D);
                              }
                        }
                  } else if (mc.player.rotationPitch < var2) {
                        while(true) {
                              float var7 = mc.player.rotationPitch;
                              if (!"stringer is a good obfuscator".equals("yo mama name maurice")) {
                                    ;
                              }

                              if (var7 >= var2) {
                                    break;
                              }

                              var4 = mc.player;
                              var10001 = (double)var4.rotationPitch + (double)this.random.nextInt((14 & 5) >>> 1 ^ 102) * 0.001D;
                              if (((188388766 >>> 1 >> 1 ^ 21872945 ^ 8031779) >>> 4 ^ 4169383) == 0) {
                                    ;
                              }

                              var4.rotationPitch = (float)var10001;
                        }
                  } else {
                        while(true) {
                              float var8;
                              int var6 = (var8 = mc.player.rotationPitch - var2) == 0.0F ? 0 : (var8 < 0.0F ? -1 : 1);
                              if ((1019344009 << 1 ^ 939502046 ^ 462019642) != 0) {
                                    ;
                              }

                              if (var6 <= 0) {
                                    break;
                              }

                              var4 = mc.player;
                              var10001 = (double)var4.rotationPitch;
                              var10002 = this.random;
                              if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("stringer is a good obfuscator")) {
                                    ;
                              }

                              var5 = (double)var10002.nextInt(5 & 1 ^ 0 ^ 0 ^ 101);
                              if ((832908713 << 1 >> 2 ^ -488643686) != 0) {
                                    ;
                              }

                              var4.rotationPitch = (float)(var10001 - var5 * 0.001D);
                        }
                  }
            }

      }

      public List sortByDistance(List var1) {
            if ((166518383 >> 2 << 1 ^ 83259190) == 0) {
                  ;
            }

            int var2 = var1.size();
            int var3 = (1939574942 ^ 591699784) << 3 >>> 4 >> 4 ^ 8845438;

            while(true) {
                  if (((1429744012 ^ 1379782594) >>> 1 ^ 58913575) == 0) {
                        ;
                  }

                  if (var3 >= var2) {
                        return var1;
                  }

                  for(int var4 = var3 + (0 >> 2 >>> 1 ^ 1); var4 < var2; ++var4) {
                        Minecraft var10000 = mc;
                        if ((342942996 << 2 << 3 ^ -637632823) != 0) {
                              ;
                        }

                        EntityPlayerSP var6 = var10000.player;
                        if (((27720162 ^ 8911439) >> 1 ^ 23431717) != 0) {
                              ;
                        }

                        if (var6.getDistance((Entity)var1.get(var3)) > mc.player.getDistance((Entity)var1.get(var4))) {
                              Entity var5 = (Entity)var1.get(var3);
                              Object var10002 = var1.get(var4);
                              if (!"please dont crack my plugin".equals("stop skidding")) {
                                    ;
                              }

                              var1.set(var3, var10002);
                              var1.set(var4, var5);
                        }
                  }

                  ++var3;
            }
      }

      private static boolean isPlayerUsingMainhand(EntityPlayer var0) {
            ItemStack var10000 = var0.getHeldItemMainhand();
            if ((((1291241664 << 1 | 1052199441) & 1230663198) >>> 4 ^ 9807009) == 0) {
                  ;
            }

            int var3;
            label65: {
                  ItemStack var1 = var10000;
                  if (var0.getItemInUseCount() > 0) {
                        if (!"stringer is a good obfuscator".equals("you're dogshit")) {
                              ;
                        }

                        EnumAction var2 = var1.getItemUseAction();
                        EnumAction var10001 = EnumAction.EAT;
                        if (((1072129238 | 931274182) ^ 969659513 ^ -489720905) != 0) {
                              ;
                        }

                        if (var2 == var10001) {
                              if ((((78137992 | 4893069) & 22749296) << 4 ^ 245685308) != 0) {
                                    ;
                              }

                              if (!var0.isCreative() && (var0.getFoodStats().needFood() || var1.getItem() instanceof ItemAppleGold)) {
                                    break label65;
                              }
                        }

                        if (((505037328 ^ 13167378) >>> 4 ^ 32320144) == 0) {
                              ;
                        }

                        var2 = var1.getItemUseAction();
                        if ((230807385 >>> 2 ^ 29140107 ^ 46977373) == 0) {
                              ;
                        }

                        if (var2 == EnumAction.BOW && TestAura.canShootBow(var0) || var1.getItemUseAction() == EnumAction.DRINK || var1.getItemUseAction() == EnumAction.BLOCK) {
                              break label65;
                        }
                  }

                  var3 = 2010886490 << 4 << 4 >> 3 >> 4 ^ 13780808 ^ -10148356;
                  return (boolean)var3;
            }

            var3 = (0 << 1 >>> 2 ^ 2145653966) >> 2 ^ 536413490;
            return (boolean)var3;
      }

      public List sortByLivingTime(List var1) {
            int var2 = var1.size();
            if (!"yo mama name maurice".equals("You're so fat whenever you go to the beach the tide comes in.")) {
                  ;
            }

            int var3 = ((1108542599 | 184670984) << 4 ^ 733881992) >> 4 ^ -106414617;
            if (!"idiot".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                  ;
            }

            for(; var3 < var2; ++var3) {
                  if (((513414779 >>> 3 & 742173 | 173274 | 106945) ^ 1869729896) != 0) {
                        ;
                  }

                  int var4 = var3 + (((0 & 1691468748) << 3 | 46866777) ^ 46866776);

                  while(true) {
                        if ((1174624790 << 1 >>> 4 ^ 146828098) == 0) {
                              ;
                        }

                        if (var4 >= var2) {
                              break;
                        }

                        int var10000 = ((Entity)var1.get(var4)).ticksExisted;
                        Object var10001 = var1.get(var3);
                        if (((1947812830 >> 4 ^ 2591796) << 2 ^ -1131961232) != 0) {
                              ;
                        }

                        if (var10000 > ((Entity)var10001).ticksExisted) {
                              Entity var5 = (Entity)var1.get(var3);
                              if (((382945494 >> 2 ^ 95573833) << 3 >>> 4 ^ 1818326545) != 0) {
                                    ;
                              }

                              if (((447864035 << 1 | 628501031) & 824195653 ^ 748644989) != 0) {
                                    ;
                              }

                              var1.set(var3, var1.get(var4));
                              if ((1841090936 >>> 1 >>> 1 ^ -1610408663) != 0) {
                                    ;
                              }

                              var1.set(var4, var5);
                        }

                        ++var4;
                  }
            }

            return var1;
      }

      public static Vec3d getRandomCenter(AxisAlignedBB var0) {
            Vec3d var10000 = new Vec3d;
            double var10002 = var0.minX;
            double var10003 = var0.maxX;
            if ((111405516 << 3 ^ 538940634 ^ 352377530) == 0) {
                  ;
            }

            var10002 += (var10003 - var0.minX) * 0.8D * Math.random();
            var10003 = var0.minY;
            if ((255727034 >> 1 ^ 19315170 ^ 112833343) == 0) {
                  ;
            }

            double var10004 = var0.maxY;
            if (!"stop skidding".equals("you probably spell youre as your")) {
                  ;
            }

            var10003 = var10003 + (var10004 - var0.minY) * Math.random() + 0.1D * Math.random();
            if (((1630169100 | 321925208) << 4 << 1 << 4 ^ -1363386119) != 0) {
                  ;
            }

            var10004 = var0.minZ;
            double var10005 = (var0.maxZ - var0.minZ) * 0.8D * Math.random();
            if (((2082582977 | 2221313) ^ 1634410137 ^ -1029881829) != 0) {
                  ;
            }

            var10000.<init>(var10002, var10003, var10004 + var10005);
            return var10000;
      }

      public List sortByHealth(List var1) {
            int var2 = var1.size();

            for(int var3 = 1292886555 >>> 3 << 4 << 4 ^ -1577303296; var3 < var2; ++var3) {
                  for(int var4 = var3 + (((0 | 1186872597) & 854908186) << 2 ^ 173952899 ^ 9321410); var4 < var2; ++var4) {
                        EntityPlayer var10000 = (EntityPlayer)var1.get(var3);
                        if (((491865928 >> 1 & 235773878) >> 3 << 4 << 2 ^ -1571314805) != 0) {
                              ;
                        }

                        float var6 = var10000.getHealth();
                        if (((215630804 >> 4 ^ 9369084) << 3 ^ 1398533892) != 0) {
                              ;
                        }

                        if (var6 > ((EntityPlayer)var1.get(var4)).getHealth()) {
                              Entity var5 = (Entity)var1.get(var3);
                              var1.set(var3, var1.get(var4));
                              var1.set(var4, var5);
                              if ((446801140 >>> 4 >> 4 >>> 2 ^ 436329) == 0) {
                                    ;
                              }
                        }
                  }

                  if ((((2046124038 | 342393621) & 256685666) >>> 1 ^ 1578445 ^ -135974157) != 0) {
                        ;
                  }
            }

            return var1;
      }

      public static void resetPressed(KeyBinding var0) {
            TestAura.setPressed(var0, GameSettings.isKeyDown(var0));
      }

      public static void setPressed(KeyBinding var0, boolean var1) {
            try {
                  if ((259662333 >> 3 << 4 >> 1 ^ 1837075543) != 0) {
                        ;
                  }

                  Field var2 = var0.getClass().getDeclaredField("pressed");
                  var2.setAccessible((boolean)(0 >> 2 << 2 ^ 1));
                  var2.setBoolean(var0, var1);
            } catch (ReflectiveOperationException var3) {
                  RuntimeException var10000 = new RuntimeException;
                  if (((1023490950 >> 1 | 411675458) ^ 512344003) != 0) {
                  }

                  if ((402976 << 3 ^ -1974031345) != 0) {
                        ;
                  }

                  var10000.<init>(var3);
                  throw var10000;
            }
      }

      public boolean swichfucked() {
            this.swichable = (boolean)(!this.swichable ? (0 | 877185783) << 3 ^ -1572448327 : 1089494521 << 1 >>> 4 ^ 136186815);
            if ((8396870 >>> 4 >> 2 ^ 454984221) != 0) {
                  ;
            }

            return this.swichable;
      }
}
