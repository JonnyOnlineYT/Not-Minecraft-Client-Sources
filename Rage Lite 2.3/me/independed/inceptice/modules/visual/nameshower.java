//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.visual;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import me.independed.inceptice.modules.Module;
import me.independed.inceptice.modules.ModuleManager;
import me.independed.inceptice.settings.BooleanSetting;
import me.independed.inceptice.settings.Setting;
import me.independed.inceptice.ui.Hud;
import me.independed.inceptice.util.TimerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

public class nameshower extends Module {
      private Random random;
      public BooleanSetting animalSet = new BooleanSetting("Animals", this, (boolean)(307608824 >> 1 & 15627130 ^ 2773624));
      private TimerUtil stopRender;
      String whatStringR;
      private ArrayList hitStrings;
      private boolean changePosRender;
      private Entity target;
      int whereYR;
      private double zHit;
      private double xHit;
      Integer colorR;
      public BooleanSetting invisibleSet;
      public ArrayList colorHits;
      private double yHit;
      public BooleanSetting playerSet;
      public BooleanSetting mobSet = new BooleanSetting("Mobs", this, (boolean)(930493222 >>> 2 >> 4 >> 4 ^ 531628 ^ 377120));
      int whereXR;

      void renderNameTag(EntityLivingBase var1, String var2, double var3, double var5, double var7, boolean var9, int var10, int var11, String var12, int var13) {
            if (!(var1 instanceof EntityArmorStand) && var1 != mc.player) {
                  EntityPlayerSP var14 = mc.player;
                  FontRenderer var15 = mc.fontRenderer;
                  var5 += var1.isSneaking() ? 0.5D : 0.7D;
                  float var16 = var14.getDistance(var1) / 4.0F;
                  if (var16 < 1.6F) {
                        var16 = 1.6F;
                  }

                  int var17 = (int)var1.getHealth();
                  StringBuilder var10000 = (new StringBuilder()).append(String.valueOf(var2)).append(" §c§l");
                  if (((2025882567 | 953879276 | 1787399497) >>> 3 ^ 29832002 ^ -1008457139) != 0) {
                        ;
                  }

                  var2 = var10000.append(Math.round((float)var17)).append("§c").toString();
                  RenderManager var18 = mc.getRenderManager();
                  if (((1978503833 | 1963629301) >> 4 >> 4 << 1 ^ 15458100) == 0) {
                        ;
                  }

                  float var24 = var16 / 30.0F;
                  if (!"intentMoment".equals("you're dogshit")) {
                        ;
                  }

                  float var19 = var24;
                  double var25 = (double)var19 * 0.3D;
                  if ((20997282 >> 2 ^ 5249320) == 0) {
                        ;
                  }

                  var19 = (float)var25;
                  GL11.glPushMatrix();
                  GL11.glTranslatef((float)var3, (float)var5 + 1.4F, (float)var7);
                  if ((((650533178 ^ 122636212) & 117984495 & 9130562) >> 1 ^ 262145) == 0) {
                        ;
                  }

                  GL11.glNormal3f(1.0F, 1.0F, 1.0F);
                  GL11.glRotatef(-var18.playerViewY, 0.0F, 1.0F, 0.0F);
                  GL11.glRotatef(var18.playerViewX, 1.0F, 0.0F, 0.0F);
                  GL11.glScalef(-var19, -var19, var19);
                  GL11.glEnable(((1490 ^ 1391) << 4 << 3 | 1448) ^ 21640);
                  GL11.glDisable(((266 | 32) ^ 236 | 342) ^ 2727);
                  GL11.glDisable((1022 | 774) << 3 & 1533 ^ 2065);
                  GL11.glDisable((803 << 2 & 1274 ^ 247 ^ 867 | 885) ^ 3117);
                  GL11.glDepthMask((boolean)(((2031887568 | 79734119) & 926771366) << 2 ^ -730824040));
                  GL11.glBlendFunc(174 >>> 4 >> 1 >>> 4 ^ 770, (536 | 152 | 201) ^ 474);
                  GL11.glEnable(((283 << 3 & 654) >>> 1 << 1 | 2) ^ 2920);
                  Tessellator var20 = Tessellator.getInstance();
                  BufferBuilder var21 = var20.getBuffer();
                  if (!"minecraft".equals("nefariousMoment")) {
                        ;
                  }

                  int var22 = var15.getStringWidth(var2) / ((1 & 0) >> 4 ^ 2);
                  int var26 = (254 << 3 | 862) >>> 4 ^ 893;
                  if (!"buy a domain and everything else you need at namecheap.com".equals("you're dogshit")) {
                        ;
                  }

                  GL11.glBlendFunc(var26, 15 >> 1 >>> 2 ^ 770);
                  int var10002;
                  double var27;
                  if (var9) {
                        var26 = -var22 - ((1 & 0 | 1590294931) << 1 ^ 887799359 ^ -1988539109);
                        if (!"please dont crack my plugin".equals("buy a domain and everything else you need at namecheap.com")) {
                              ;
                        }

                        var25 = (double)var26;
                        int var10001 = var15.FONT_HEIGHT;
                        if (!"please go outside".equals("please dont crack my plugin")) {
                              ;
                        }

                        var27 = (double)(-(var10001 + ((3 >>> 1 | 0) ^ 7)));
                        var10002 = var22 * ((0 & 1337300173 | 1485138971) << 1 ^ -1324689356);
                        if (((((68738357 ^ 44696753) & 1134090) >>> 2 | 31433) << 4 ^ 281585479) != 0) {
                              ;
                        }

                        Hud.drawRoundedRect(var25, var27, (double)(var10002 + ((0 & 540094854 & 1660475311 ^ 787220800) >> 2 ^ 196805203)), 15.0D, 6.0D, (new Color((933048871 >> 2 | 198437988) >> 1 ^ 133953142, 1857390886 >>> 2 >>> 2 ^ 64711330 ^ 87044656, (1479515397 << 2 ^ 1330343176) >>> 2 ^ 201147975, (888922436 ^ 662782215) >>> 3 & 30990598 ^ 4734976)).getRGB());
                  } else {
                        var25 = (double)(-var22 - (1 >>> 1 >> 4 & 1971203281 ^ 2));
                        var27 = (double)(-(var15.FONT_HEIGHT + ((1 >> 1 << 2 | 731530366) >>> 4 ^ 45720641)));
                        int var10003 = (1 >> 1 | 1574719880) ^ 1574719882;
                        if (!"please dont crack my plugin".equals("yo mama name maurice")) {
                              ;
                        }

                        var10002 = var22 * var10003;
                        var10003 = 0 >> 2 << 2 << 2 ^ 3;
                        if ((4718656 << 3 ^ 684531180) != 0) {
                              ;
                        }

                        double var29 = (double)(var10002 + var10003);
                        int var10005 = (new Color((1461253282 >>> 2 & 19868339) << 1 ^ 34361408, (1111499276 | 986484808) >> 4 >>> 1 ^ 64382354, 1300846418 >> 2 >>> 2 ^ 81302901, (3 & 1 & 0) >>> 3 ^ 90)).getRGB();
                        if ((((2100449766 << 2 << 1 | 165705501) >>> 4 | 147923260) ^ 249507839) == 0) {
                              ;
                        }

                        Hud.drawRoundedRect(var25, var27, var29, 15.0D, 6.0D, var10005);
                  }

                  if (((1379834208 | 1103317100 | 253552897) >> 4 ^ 100663190) == 0) {
                        ;
                  }

                  Module var28 = ModuleManager.getModuleByName("HitEffects");
                  if (((1814264377 ^ 635613819) >> 3 >> 3 ^ 19334945) == 0) {
                        ;
                  }

                  float var31;
                  float var32;
                  if (var28.isToggled()) {
                        if ((2097536 >>> 4 ^ 1809371049) != 0) {
                              ;
                        }

                        if (var9) {
                              if (!"yo mama name maurice".equals("please dont crack my plugin")) {
                                    ;
                              }

                              if (this.random.nextBoolean()) {
                                    if (this.random.nextBoolean()) {
                                          var31 = (float)var10;
                                          var32 = (float)var11;
                                          if ((((604438592 << 4 | 943718800) >>> 2 | 215639076) ^ 517891428) == 0) {
                                                ;
                                          }

                                          var15.drawString(var12, var31, var32, var13, (boolean)(((0 ^ 832839424) >>> 3 >> 2 & 17254422) >>> 4 ^ 1069056));
                                    } else {
                                          var15.drawString(var12, (float)var10, (float)var11, var13, (boolean)(0 >> 4 >> 2 ^ 1));
                                          if (!"stringer is a good obfuscator".equals("shitted on you harder than archybot")) {
                                                ;
                                          }
                                    }
                              } else {
                                    if ((2113310872 >>> 3 >> 4 >>> 1 << 2 ^ 33020480) == 0) {
                                          ;
                                    }

                                    if (this.random.nextBoolean()) {
                                          if ((((898418269 | 434023559) & 769385008) >> 1 ^ 1116191420) != 0) {
                                                ;
                                          }

                                          if (((1538838960 << 2 ^ 525018244 | 1578543041) >>> 2 ^ -643272413) != 0) {
                                                ;
                                          }

                                          if (((1278417966 ^ 624719230) >> 2 ^ 440654420) == 0) {
                                                ;
                                          }

                                          var15.drawString(var12, (float)var10, (float)var11, var13, (boolean)((((0 | 187756037) ^ 108000178) << 3 | 171264153) ^ 1795026360));
                                    } else {
                                          var31 = (float)var10;
                                          var32 = (float)var11;
                                          if (!"intentMoment".equals("please dont crack my plugin")) {
                                                ;
                                          }

                                          var15.drawString(var12, var31, var32, var13, (boolean)((0 & 532866471) >> 2 ^ 1));
                                    }
                              }
                        }
                  }

                  if ((621858265 << 1 >>> 1 << 2 >> 3 ^ -225941780) == 0) {
                        ;
                  }

                  if (!var9) {
                        if ((((532475098 | 183294551) >> 1 | 243883362) >>> 3 ^ 33550317) == 0) {
                              ;
                        }

                        var10002 = -var22 - ((1 & 0 | 443394294) << 4 ^ -1495625886);
                        if (((339198320 >> 1 | 67323816) & 223554351 ^ 202580776) == 0) {
                              ;
                        }

                        var10002 = nameshower.getMiddle(var10002, var22 + ((0 >> 1 & 807678323 | 2003221663) ^ 2003221661));
                        if (((82837776 | 27861823) ^ 100213567) == 0) {
                              ;
                        }

                        var31 = (float)(var10002 - var22);
                        var32 = (float)(-(var15.FONT_HEIGHT + ((0 | 1869539081 | 277239733 | 1561240748) & 232939873 ^ 232939811)));
                        if ((2064765865 >> 1 << 2 >>> 1 ^ -90847458) != 0) {
                              ;
                        }

                        var15.drawString(var2, var31, var32, (new Color((153 ^ 26 ^ 93 ^ 189) << 1 ^ 57, (240 << 4 | 503) ^ 3848, (123 & 113 | 43) << 4 >>> 4 ^ 132, 1076103424 >> 3 ^ 134512928)).getRGB(), (boolean)((0 << 2 ^ 1109639991) >> 4 ^ 69352498));
                        if (!"you probably spell youre as your".equals("idiot")) {
                              ;
                        }

                        if (!"minecraft".equals("yo mama name maurice")) {
                              ;
                        }
                  }

                  boolean var30 = var1 instanceof EntityPlayer;
                  if (((820677744 >>> 2 & 14242525) >> 2 ^ 393351) == 0) {
                        ;
                  }

                  if (var30 && !var9) {
                        EntityPlayer var23 = (EntityPlayer)var1;
                        GlStateManager.translate(0.0F, 1.1F, 0.0F);
                        this.renderArmor(var23, (1208043648 | 351060356) >> 4 & 14109348 & 9368981 ^ 8798336, -(var15.FONT_HEIGHT + (((0 | 5434273) & 2294410) << 3 ^ 1053697)) - ((1 >>> 1 ^ 1577316670) & 311273169 ^ 302096388));
                        if (((1134744191 ^ 351971909) << 2 >>> 4 ^ 1583692504) != 0) {
                              ;
                        }

                        GlStateManager.translate(0.0F, -1.1F, 0.0F);
                  }

                  if ((1788087865 << 3 >> 1 >>> 1 ^ 354950258) == 0) {
                        ;
                  }

                  GL11.glDisable((256 & 203) >> 4 ^ 3042);
                  GL11.glDepthMask((boolean)((0 ^ 1710699053 | 1549204664) ^ 2113404604));
                  GL11.glEnable(1705 >> 4 >> 1 >>> 3 ^ 3559);
                  GL11.glEnable(500 << 2 & 954 ^ 357 ^ 2436);
                  GL11.glDisable((2804 >>> 4 & 67 | 2) >>> 3 ^ 2848);
                  GL11.glPopMatrix();
            }
      }

      public void renderArmor(EntityPlayer var1, int var2, int var3) {
            InventoryPlayer var4 = var1.inventory;
            ItemStack var5 = var1.getHeldItemMainhand();
            ItemStack var6 = var4.armorItemInSlot(141103896 ^ 110582028 ^ 148202912 ^ 103493556);
            ItemStack var7 = var4.armorItemInSlot(((0 | 523416343) & 6615839) >>> 4 ^ 133936);
            if (((677637007 | 232444187) >> 4 ^ 48218041) == 0) {
                  ;
            }

            if (!"idiot".equals("you're dogshit")) {
                  ;
            }

            ItemStack var8 = var4.armorItemInSlot(((0 >>> 2 & 222417140) << 1 ^ 1646310833) >> 2 ^ 411577710);
            ItemStack var9 = var4.armorItemInSlot(2 >> 4 >> 2 >> 1 << 4 ^ 3);
            if (((615909740 >> 1 << 3 & 1445094954 & 277054907 | 198740202) ^ -672038054) != 0) {
                  ;
            }

            ItemStack[] var10 = null;
            ItemStack[] var10000;
            if (var5 != null) {
                  var10000 = new ItemStack[(3 | 1) >>> 4 ^ 5];
                  var10000[(1751953825 << 4 ^ 1010865371 | 1362193596) ^ -71569665] = var5;
                  var10000[0 << 1 >> 1 << 1 ^ 1] = var9;
                  var10000[(0 | 971468220 | 243785154) & 349453147 ^ 348404056] = var8;
                  var10000[(2 & 1) << 3 >> 3 ^ 3] = var7;
                  var10000[(3 & 1 | 0) & 0 ^ 4] = var6;
                  var10 = var10000;
            } else {
                  var10000 = new ItemStack[((1 | 0) << 3 << 2 | 9) ^ 45];
                  var10000[(164612982 ^ 109158786) >>> 2 ^ 64199357] = var9;
                  var10000[(0 ^ 1609276369 | 841808106) ^ 2146434042] = var8;
                  var10000[(1 >> 3 ^ 1481025486) >> 3 ^ 185128187] = var7;
                  int var10002 = ((1 ^ 0) >> 4 & 926256419) >>> 4 ^ 3;
                  if ((41480136 ^ 20722553 ^ 33582264 ^ 13172698 ^ 25972691) == 0) {
                        ;
                  }

                  var10000[var10002] = var6;
                  var10 = var10000;
            }

            ArrayList var11 = new ArrayList();
            if ((40387585 >>> 2 ^ 1012182150) != 0) {
                  ;
            }

            ItemStack[] var12 = var10;
            int var18 = var10.length;
            if (((2105936437 ^ 1399417706 | 113642899) ^ 473054186 ^ 853366325) == 0) {
                  ;
            }

            int var13 = var18;
            int var14 = (8570016 ^ 7433501) >>> 1 ^ 7984350;

            while(true) {
                  if ("idiot".equals("yo mama name maurice")) {
                  }

                  if (var14 >= var13) {
                        var18 = (2 >> 3 >> 2 ^ 16) * var11.size() / ((1 ^ 0) >> 2 >>> 2 ^ 2);
                        if ((((1471399105 ^ 897747443) >> 2 | 87073481) >> 2 ^ -715127326) != 0) {
                              ;
                        }

                        var14 = var18;
                        var2 -= var14;
                        GlStateManager.disableDepth();
                        if (((2011494848 << 2 | 132523109) ^ -537401499) == 0) {
                              ;
                        }

                        for(Iterator var17 = var11.iterator(); var17.hasNext(); var2 += 16) {
                              ItemStack var16 = (ItemStack)var17.next();
                              if ((((202483700 | 26639147) & 45935491) >> 4 ^ 426322985) != 0) {
                                    ;
                              }

                              this.renderItem(var16, var2, var3);
                              if (!"your mom your dad the one you never had".equals("please take a shower")) {
                                    ;
                              }
                        }

                        if ((((331773122 >>> 4 ^ 17205913) & 2167410 & 1382749) << 4 ^ 1280) == 0) {
                              ;
                        }

                        GlStateManager.enableDepth();
                        if (((944863542 | 100045352) ^ 552765439 ^ 102118014 ^ 454250175) == 0) {
                              ;
                        }

                        return;
                  }

                  if ((((898992277 >> 2 | 170964320) & 131080643) << 4 & 886730778 ^ 1564057378) != 0) {
                        ;
                  }

                  ItemStack var15 = var12[var14];
                  if (((211035259 << 4 | 968827137) >> 1 ^ -1851281610) != 0) {
                        ;
                  }

                  if (var15 != null && var15.getItem() != null) {
                        var11.add(var15);
                  }

                  ++var14;
                  if (!"nefariousMoment".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                        ;
                  }
            }
      }

      @SubscribeEvent
      public void onAttackEvent(AttackEntityEvent var1) {
            Entity var10000 = var1.getTarget();
            if (((261932996 ^ 68944984) & 99122984 ^ 481273053) != 0) {
                  ;
            }

            if (var10000 != null) {
                  this.target = var1.getTarget();
                  if (((841575350 << 2 >> 1 ^ 879045378 | 917070407) ^ -155189649) == 0) {
                        ;
                  }

                  this.changePosRender = (boolean)(0 >>> 2 ^ 1127661660 ^ 1127661661);
                  this.stopRender.reset();
            }

            if (this.stopRender.hasReached(585.0D) && !this.changePosRender) {
                  this.target = null;
                  this.stopRender.reset();
            }

      }

      public static int getMiddle(int var0, int var1) {
            return (var0 + var1) / (0 >>> 1 ^ 839542795 ^ 839542793);
      }

      public void onRenderWorldLast(float var1) {
            if (mc.player != null && !mc.player.isDead) {
                  Stream var10000 = mc.world.loadedEntityList.stream();
                  Predicate var10001 = (var0) -> {
                        int var10000;
                        if (var0 != mc.player) {
                              var10000 = (0 ^ 277481636) >> 1 ^ 138740819;
                        } else {
                              var10000 = (1171490510 << 2 & 248333578) >>> 3 ^ 7082641 ^ 10850992;
                              if (((1815429771 << 3 ^ 996186442) << 3 ^ -1440743507) != 0) {
                                    ;
                              }
                        }

                        return (boolean)var10000;
                  };
                  if ((1037206327 << 3 << 3 & 166323113 ^ -2086290174) != 0) {
                        ;
                  }

                  List var17 = (List)var10000.filter(var10001).filter((var0) -> {
                        return (boolean)(!var0.isDead ? (0 ^ 880667173) >> 1 >> 2 >> 3 ^ 13760425 : (923958081 >> 1 ^ 269780381) & 4706553 & 161595 ^ 4153);
                  }).sorted(Comparator.comparing((var0) -> {
                        if ((643456741 >>> 1 >>> 1 ^ 160864185) == 0) {
                              ;
                        }

                        return Float.valueOf(mc.player.getDistance(var0));
                  })).collect(Collectors.toList());
                  if (!"nefariousMoment".equals("your mom your dad the one you never had")) {
                        ;
                  }

                  List var2 = var17;
                  Iterator var3 = var2.iterator();

                  while(true) {
                        Entity var4;
                        double var5;
                        double var7;
                        double var9;
                        double var11;
                        double var13;
                        double var15;
                        double var18;
                        EntityLivingBase var24;
                        String var27;
                        while(true) {
                              do {
                                    do {
                                          if (!var3.hasNext()) {
                                                return;
                                          }

                                          var4 = (Entity)var3.next();
                                          if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("ape covered in human flesh")) {
                                                ;
                                          }
                                    } while(var4 == null);

                                    if (((140804144 << 2 | 429458412) >> 1 >>> 4 ^ 30201887) == 0) {
                                          ;
                                    }
                              } while(var4 == mc.player);

                              double var10002;
                              double var20;
                              int var10006;
                              int var10007;
                              if (ModuleManager.getModuleByName("HitEffects").isToggled() && this.target != null && !this.stopRender.hasReached(585.0D)) {
                                    double var10003;
                                    if (this.changePosRender) {
                                          var18 = mc.getRenderManager().viewerPosX;
                                          if (!"idiot".equals("you probably spell youre as your")) {
                                                ;
                                          }

                                          var5 = var18;
                                          var7 = mc.getRenderManager().viewerPosY;
                                          var9 = mc.getRenderManager().viewerPosZ;
                                          var20 = this.target.lastTickPosX;
                                          var10002 = this.target.posX;
                                          if (!"you're dogshit".equals("nefariousMoment")) {
                                                ;
                                          }

                                          var10002 = (var10002 - this.target.lastTickPosX) * (double)var1;
                                          if (!"stringer is a good obfuscator".equals("intentMoment")) {
                                                ;
                                          }

                                          this.xHit = var20 + var10002 - var5;
                                          Entity var21 = this.target;
                                          if (((1650932314 << 3 << 2 ^ 1250191187) & 43731635 ^ 33753107) == 0) {
                                                ;
                                          }

                                          var20 = var21.lastTickPosY;
                                          var10002 = this.target.posY;
                                          var10003 = this.target.lastTickPosY;
                                          if (((1801616774 >> 1 ^ 893385784 | 4314932) << 1 ^ 27242494) == 0) {
                                                ;
                                          }

                                          this.yHit = var20 + (var10002 - var10003) * (double)var1 - var7;
                                          var20 = this.target.lastTickPosZ;
                                          var10002 = this.target.posZ - this.target.lastTickPosZ;
                                          if ((((1865859312 << 1 ^ 1008005519) >>> 2 >> 2 | 226205985) ^ 260028199) == 0) {
                                                ;
                                          }

                                          this.zHit = var20 + var10002 * (double)var1 - var9;
                                          if (((851802133 >>> 4 >> 2 & 9146097 | 5312552) ^ 11838691 ^ 7319563) == 0) {
                                                ;
                                          }

                                          this.whatStringR = (String)this.hitStrings.get(this.random.nextInt((3 >>> 4 >> 2 | 920975002) & 91244561 ^ 73416726));
                                          if (((1092960901 ^ 93726818) >> 2 >>> 2 & 20581385 ^ 1312065880) != 0) {
                                                ;
                                          }

                                          ArrayList var22 = this.colorHits;
                                          Random var25 = this.random;
                                          if (!"you probably spell youre as your".equals("please dont crack my plugin")) {
                                                ;
                                          }

                                          int var26 = var25.nextInt((((2 | 1) & 0) >> 1 | 893087665) ^ 893087669);
                                          if (!"yo mama name maurice".equals("you probably spell youre as your")) {
                                                ;
                                          }

                                          this.colorR = (Integer)var22.get(var26);
                                          boolean var19 = this.random.nextBoolean();
                                          if ((286352389 >>> 1 ^ -1376622393) != 0) {
                                                ;
                                          }

                                          if (var19) {
                                                if (this.random.nextBoolean()) {
                                                      this.whereXR = -this.random.nextInt((24 & 23 | 1) & 0 ^ 50) + (((0 & 744744705) << 4 | 565852775) ^ 565852798);
                                                      int var23 = -this.random.nextInt(8 >> 1 << 4 >> 2 << 3 ^ 138);
                                                      var26 = (2 | 0) >>> 2 >>> 2 ^ 983508997 ^ 983509007;
                                                      if (!"nefariousMoment".equals("you're dogshit")) {
                                                            ;
                                                      }

                                                      this.whereYR = var23 - var26;
                                                } else {
                                                      this.whereXR = this.random.nextInt(41 >> 2 >>> 2 ^ 48) + ((6 ^ 1 ^ 5) << 2 >> 1 ^ 2 ^ 31);
                                                      this.whereYR = this.random.nextInt(((10 ^ 4 | 10) >> 2 ^ 0) & 2 ^ 48) + (2 << 4 << 3 & 189 ^ 25);
                                                }
                                          } else if (this.random.nextBoolean()) {
                                                if ((((563341987 ^ 227260500) & 622866929 | 217502166) << 4 ^ -814932112) == 0) {
                                                      ;
                                                }

                                                this.whereXR = -this.random.nextInt(35 << 3 >>> 1 >> 1 ^ 39 ^ 83) - ((16 ^ 8 ^ 10 | 11) ^ 2);
                                                this.whereYR = this.random.nextInt(7 << 3 << 1 ^ 66) - (((15 ^ 10) >>> 2 ^ 0 | 0) ^ 24);
                                          } else {
                                                this.whereXR = this.random.nextInt(22 >>> 4 >> 2 ^ 50) + ((19 << 4 | 170) >>> 4 ^ 2);
                                                this.whereYR = -this.random.nextInt((6 & 1) >>> 1 >> 1 ^ 10) - ((7 >> 2 ^ 0) >> 3 ^ 10);
                                          }

                                          if (!"you probably spell youre as your".equals("please get a girlfriend and stop cracking plugins")) {
                                                ;
                                          }

                                          this.changePosRender = (boolean)((1454319321 ^ 637962432) >>> 2 & 284573623 ^ 270674822);
                                    }

                                    if (this.target != null) {
                                          var24 = (EntityLivingBase)this.target;
                                          var27 = this.target.getName();
                                          var10003 = this.xHit;
                                          if ((((138242451 ^ 86464064) & 206817052) >>> 2 ^ 50627012) == 0) {
                                                ;
                                          }

                                          double var10004 = this.yHit;
                                          double var10005 = this.zHit;
                                          var10006 = (0 >> 4 | 184219073) ^ 184219072;
                                          if (((1536452440 << 1 << 4 << 4 | 538730232) >> 3 ^ 85712863) == 0) {
                                                ;
                                          }

                                          var10007 = this.whereXR;
                                          int var10008 = this.whereYR;
                                          if ((189009176 << 4 ^ -528624548) != 0) {
                                                ;
                                          }

                                          this.renderNameTag(var24, var27, var10003, var10004, var10005, (boolean)var10006, var10007, var10008, this.whatStringR, this.colorR.intValue());
                                    }
                              }

                              if (this.invisibleSet.isEnabled() && var4.isInvisible()) {
                                    if (var4 instanceof EntityPlayer && this.playerSet.isEnabled() || var4 instanceof EntityMob && this.mobSet.isEnabled()) {
                                          break;
                                    }

                                    if (var4 instanceof EntityAnimal && this.animalSet.isEnabled()) {
                                          if ((((436542474 | 380325228) ^ 300092139) >> 3 ^ 32082672) == 0) {
                                                ;
                                          }
                                          break;
                                    }
                              }

                              if (this.mobSet.isEnabled() && var4 instanceof EntityMob) {
                                    var5 = mc.getRenderManager().viewerPosX;
                                    var7 = mc.getRenderManager().viewerPosY;
                                    var9 = mc.getRenderManager().viewerPosZ;
                                    var18 = var4.lastTickPosX;
                                    var20 = var4.posX;
                                    if (!"yo mama name maurice".equals("you're dogshit")) {
                                          ;
                                    }

                                    var10002 = var4.lastTickPosX;
                                    if ((((1300738712 ^ 905071029 | 881143532) >> 1 | 199477139) ^ 1073479671) == 0) {
                                          ;
                                    }

                                    var11 = var18 + (var20 - var10002) * (double)var1 - var5;
                                    if (((675354124 | 252639299) ^ 793771599) == 0) {
                                          ;
                                    }

                                    var18 = var4.lastTickPosY;
                                    if (((17170965 | 11202259) ^ 28241623) == 0) {
                                          ;
                                    }

                                    var20 = (var4.posY - var4.lastTickPosY) * (double)var1;
                                    if (!"i hope you catch fire ngl".equals("please take a shower")) {
                                          ;
                                    }

                                    var18 += var20;
                                    if (((101234994 ^ 18286590) & 43742979 ^ 35337728) == 0) {
                                          ;
                                    }

                                    var13 = var18 - var7;
                                    var18 = var4.lastTickPosZ;
                                    var20 = var4.posZ;
                                    var10002 = var4.lastTickPosZ;
                                    if (!"please dont crack my plugin".equals("you probably spell youre as your")) {
                                          ;
                                    }

                                    var18 += (var20 - var10002) * (double)var1;
                                    if (!"stringer is a good obfuscator".equals("please dont crack my plugin")) {
                                          ;
                                    }

                                    var15 = var18 - var9;
                                    var24 = (EntityLivingBase)var4;
                                    var27 = var4.getName();
                                    var10006 = 1065219931 << 2 >> 1 ^ -17043786;
                                    var10007 = (157810950 ^ 90038783 ^ 12870420) >>> 3 ^ 27144317;
                                    if ((1825929735 << 4 >> 1 ^ 716295631) != 0) {
                                          ;
                                    }

                                    this.renderNameTag(var24, var27, var11, var13, var15, (boolean)var10006, var10007, (2080274458 << 1 & 1704850389) << 3 ^ 753369248, "None", 679485456 >> 2 ^ 169871364);
                                    if (!"please go outside".equals("you're dogshit")) {
                                          ;
                                    }
                              } else if (this.animalSet.isEnabled() && var4 instanceof EntityAnimal) {
                                    if (!"your mom your dad the one you never had".equals("nefariousMoment")) {
                                          ;
                                    }

                                    var5 = mc.getRenderManager().viewerPosX;
                                    var7 = mc.getRenderManager().viewerPosY;
                                    var9 = mc.getRenderManager().viewerPosZ;
                                    var18 = var4.lastTickPosX;
                                    if (((700679717 ^ 31173660) << 3 & 812846342 ^ 3189798 ^ -821948857) != 0) {
                                          ;
                                    }

                                    var11 = var18 + (var4.posX - var4.lastTickPosX) * (double)var1 - var5;
                                    var13 = var4.lastTickPosY + (var4.posY - var4.lastTickPosY) * (double)var1 - var7;
                                    var15 = var4.lastTickPosZ + (var4.posZ - var4.lastTickPosZ) * (double)var1 - var9;
                                    var24 = (EntityLivingBase)var4;
                                    var27 = var4.getName();
                                    if (((1385209325 << 1 ^ 1293246895) >>> 3 << 4 >> 1 ^ -399222160) == 0) {
                                          ;
                                    }

                                    var10006 = (579406852 << 4 | 364171828) ^ 1035260532;
                                    var10007 = (638880902 >>> 4 >> 2 >>> 2 ^ 1213202) >>> 4 ^ 215417;
                                    if (((465577730 >>> 2 | 43274427 | 37235197) ^ 117209599) == 0) {
                                          ;
                                    }

                                    this.renderNameTag(var24, var27, var11, var13, var15, (boolean)var10006, var10007, (1361517834 | 1260057513 | 480318690 | 1001964589) ^ 2143289327, "None", (2112227067 ^ 1365365879) << 2 >>> 3 >> 1 << 2 ^ 747098764);
                              } else {
                                    if (((100663312 >> 2 | 6660031 | 29770947) ^ 31973375) == 0) {
                                          ;
                                    }

                                    if (!"please take a shower".equals("You're so fat whenever you go to the beach the tide comes in.")) {
                                          ;
                                    }

                                    if (this.playerSet.isEnabled() && var4 instanceof EntityPlayer) {
                                          var5 = mc.getRenderManager().viewerPosX;
                                          var7 = mc.getRenderManager().viewerPosY;
                                          var9 = mc.getRenderManager().viewerPosZ;
                                          var11 = var4.lastTickPosX + (var4.posX - var4.lastTickPosX) * (double)var1 - var5;
                                          var13 = var4.lastTickPosY + (var4.posY - var4.lastTickPosY) * (double)var1 - var7;
                                          var15 = var4.lastTickPosZ + (var4.posZ - var4.lastTickPosZ) * (double)var1 - var9;
                                          var24 = (EntityLivingBase)var4;
                                          if (!"intentMoment".equals("intentMoment")) {
                                                ;
                                          }

                                          this.renderNameTag(var24, var4.getName(), var11, var13, var15, (boolean)((1228841437 | 743161091) >> 3 >>> 1 << 4 ^ 1837088208), (343662211 >>> 1 & 54200312) >> 4 >>> 1 >>> 1 ^ 582685, (67146112 >>> 4 | 3574336) << 2 ^ 31075680, "None", 1893663285 >> 2 >>> 4 ^ 29588488);
                                          if (!"i hope you catch fire ngl".equals("ape covered in human flesh")) {
                                                ;
                                          }
                                    }
                              }
                        }

                        var5 = mc.getRenderManager().viewerPosX;
                        var7 = mc.getRenderManager().viewerPosY;
                        var9 = mc.getRenderManager().viewerPosZ;
                        var18 = var4.lastTickPosX + (var4.posX - var4.lastTickPosX) * (double)var1;
                        if (!"please get a girlfriend and stop cracking plugins".equals("minecraft")) {
                              ;
                        }

                        var11 = var18 - var5;
                        var18 = var4.lastTickPosY + (var4.posY - var4.lastTickPosY) * (double)var1;
                        if ((641654858 >> 4 << 1 ^ 80206856) == 0) {
                              ;
                        }

                        var13 = var18 - var7;
                        var18 = var4.lastTickPosZ;
                        if (((677507163 >>> 4 ^ 28809174) >> 3 << 2 >> 2 ^ 1439367112) != 0) {
                              ;
                        }

                        var15 = var18 + (var4.posZ - var4.lastTickPosZ) * (double)var1 - var9;
                        var24 = (EntityLivingBase)var4;
                        var27 = var4.getName();
                        if (!"you're dogshit".equals("You're so fat whenever you go to the beach the tide comes in.")) {
                              ;
                        }

                        this.renderNameTag(var24, var27, var11, var13, var15, (boolean)((173283303 >>> 4 | 449345) & 4991012 ^ 168944 ^ 433108), 524288 << 2 ^ 2097152, ((592558764 ^ 353716046) >> 1 << 3 | 1376736656) >>> 3 ^ 459538163, "None", (306221312 << 1 | 481374764) << 4 ^ -887938368);
                  }
            } else {
                  if ((236876752 << 1 & 376325068 ^ 249452783) != 0) {
                        ;
                  }

            }
      }

      public static void glColor(int var0) {
            float var10000 = (float)(var0 >> ((9 & 4 & 312559670 ^ 875528955 | 219663173) ^ 1027591151) & (((85 | 8) & 8 | 2) >> 2 ^ 253)) / 255.0F;
            float var10001 = (float)(var0 >> ((1 ^ 0) << 1 ^ 10) & (136 >>> 3 << 3 << 2 >> 1 ^ 495)) / 255.0F;
            float var10002 = (float)(var0 & ((92 ^ 31) & 32 ^ 255)) / 255.0F;
            int var10003 = var0 >> ((11 >> 4 & 1958478978 | 2085663539) & 1319539018 ^ 1275105562);
            if (!"shitted on you harder than archybot".equals("ape covered in human flesh")) {
                  ;
            }

            GlStateManager.color(var10000, var10001, var10002, (float)(var10003 & (131 >> 2 >> 2 >> 3 >> 4 ^ 255)) / 255.0F);
      }

      public nameshower() {
            super("NameTags", "shows info about other players", (155674362 << 3 | 1239237557) << 3 ^ 1610334120, Module.Category.RENDER);
            BooleanSetting var10001 = new BooleanSetting;
            if ((((1200102639 >> 3 ^ 78221427) & 56938154) << 4 ^ -1408073428) != 0) {
                  ;
            }

            var10001.<init>("Players", this, (boolean)(((0 | 1528129334) & 1026499775 | 399643932) << 1 ^ 1068395133));
            this.playerSet = var10001;
            this.invisibleSet = new BooleanSetting("Invisible", this, (boolean)((0 >>> 1 | 1940915511) ^ 1940915510));
            this.random = new Random();
            this.hitStrings = new ArrayList();
            ArrayList var1 = new ArrayList;
            if (((1943119211 >>> 1 << 2 | 4354555) ^ 1815808175) != 0) {
                  ;
            }

            var1.<init>();
            if (((1171983960 << 4 ^ 770754032) >>> 4 << 1 ^ 235406350) == 0) {
                  ;
            }

            this.colorHits = var1;
            TimerUtil var2 = new TimerUtil;
            if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                  ;
            }

            if (((129278458 | 11724375) << 3 ^ 1035419640) == 0) {
                  ;
            }

            var2.<init>();
            this.stopRender = var2;
            this.changePosRender = (boolean)(1013242210 >> 4 >>> 2 << 3 << 3 ^ 1013242176);
            if (!"intentMoment".equals("stringer is a good obfuscator")) {
                  ;
            }

            this.target = null;
            if (!"ape covered in human flesh".equals("you're dogshit")) {
                  ;
            }

            if ((509009901 << 2 << 1 << 2 ^ -421440236) != 0) {
                  ;
            }

            Setting[] var3 = new Setting[(0 & 1023324229) >>> 4 ^ 4];
            var3[(943793585 | 687805985 | 778921531 | 593406708) << 2 ^ -4] = this.mobSet;
            int var10003 = (0 >>> 2 ^ 521587911) >>> 4 << 3 >> 2 ^ 65198489;
            if (((1475081016 ^ 562680900) & 968459342 ^ 807404620) == 0) {
                  ;
            }

            var3[var10003] = this.animalSet;
            var10003 = 1 >>> 1 >>> 1 << 2 ^ 2;
            BooleanSetting var10004 = this.playerSet;
            if ((1703202974 >> 4 >> 1 & 267735 ^ '\uf7d6' ^ 324434) == 0) {
                  ;
            }

            var3[var10003] = var10004;
            var3[(1 & 0) << 1 >>> 3 << 2 >>> 1 ^ 3] = this.invisibleSet;
            this.addSettings(var3);
            if ((((703192736 >>> 2 | 172719618) ^ 47580185) >> 4 & 3042225 ^ 672689) == 0) {
                  ;
            }

            ArrayList var10000 = this.hitStrings;
            if (((716462080 | 184920933) >> 2 >>> 2 ^ -535697326) != 0) {
                  ;
            }

            var10000.add("SMASH!");
            this.hitStrings.add("KABOOM");
            this.hitStrings.add("WHAM");
            this.hitStrings.add("BOOM!");
            this.hitStrings.add("KAPOW!");
            this.hitStrings.add("RAGE!");
            var10000 = this.colorHits;
            Color var4 = new Color;
            var10003 = (172 << 1 ^ 214) & 62 ^ 241;
            int var6 = (1733304251 ^ 486403967) << 1 >>> 2 >> 3 ^ 129687308;
            int var10005 = 1284531598 << 1 >> 4 ^ -107869007;
            if (((660707290 >>> 2 | 145476040) >>> 3 ^ 20938175) == 0) {
                  ;
            }

            var4.<init>(var10003, var6, var10005, (214 >>> 4 & 1 | 0) ^ 254);
            var10000.add(Integer.valueOf(var4.getRGB()));
            if ((109609216 ^ 109609216) == 0) {
                  ;
            }

            var10000 = this.colorHits;
            var4 = new Color;
            if (!"your mom your dad the one you never had".equals("minecraft")) {
                  ;
            }

            var10003 = (1785151127 >>> 3 >>> 1 >> 3 | 11841179) ^ 16051967;
            if (!"you probably spell youre as your".equals("your mom your dad the one you never had")) {
                  ;
            }

            var6 = (43 << 3 >> 2 & 31 | 19) ^ 232;
            if ((((1160456979 >>> 4 << 3 & 164634470) << 1 | 14098634) ^ 1792587429) != 0) {
                  ;
            }

            var10005 = (1264929185 ^ 229139246) >>> 1 & 121675932 ^ 54558724;
            int var10006 = (96 ^ 54) >>> 2 << 3 ^ 87;
            if ((((1523224799 ^ 1138769991) & 115976420) >> 2 >> 1 ^ -492975181) != 0) {
                  ;
            }

            var4.<init>(var10003, var6, var10005, var10006);
            var10000.add(Integer.valueOf(var4.getRGB()));
            this.colorHits.add(Integer.valueOf((new Color(250 >>> 4 << 4 ^ 15, ((1088343622 >>> 4 & 57605321) << 4 | 6209760) ^ 14601440, ((13 | 1) & 2 & 1968804499 | 1273689645) ^ 1273689810, (9 & 6 | 466503006) >>> 4 >> 3 ^ 1644392 ^ 3050269)).getRGB()));
            var10000 = this.colorHits;
            var4 = new Color;
            var10003 = ((164 ^ 85) & 0) << 3 ^ 255;
            var6 = (140 | 120) >> 3 << 4 >> 1 ^ 7;
            var10005 = ((1328549932 ^ 722715440 | 990671771) ^ 453508134) & 545827820 ^ 537398184;
            if ((190441259 >> 1 << 3 & 394272558 ^ 83893288) == 0) {
                  ;
            }

            var10006 = 126 >>> 2 >> 3 ^ 0 ^ 252;
            if (!"your mom your dad the one you never had".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                  ;
            }

            var4.<init>(var10003, var6, var10005, var10006);
            Integer var5 = Integer.valueOf(var4.getRGB());
            if ((((1147490654 >> 4 ^ 5561470) & 16771348) << 2 ^ 4858880) == 0) {
                  ;
            }

            var10000.add(var5);
            var10000 = this.colorHits;
            var4 = new Color;
            if ((689794769 >>> 3 & 4827506 ^ 108882) == 0) {
                  ;
            }

            var4.<init>((81 >> 4 & 2 ^ 1047324524) >> 4 ^ 65457675, (75 ^ 46) >> 4 >> 3 >>> 4 ^ 125, (6 ^ 3 | 2) >>> 2 & 0 ^ 255, 223 << 3 >> 1 ^ 899);
            var5 = Integer.valueOf(var4.getRGB());
            if (((407425762 >> 4 | 22311428) ^ 30736174) == 0) {
                  ;
            }

            var10000.add(var5);
      }

      public static String getPlayerName(EntityPlayer var0) {
            return "null";
      }

      public static void drawRect(float var0, float var1, float var2, float var3, int var4) {
            float var5;
            if (var0 < var2) {
                  var5 = var0;
                  if (!"please dont crack my plugin".equals("nefariousMoment")) {
                        ;
                  }

                  if (((128101645 >>> 2 << 2 & 33169371) >> 1 ^ 13698692) == 0) {
                        ;
                  }

                  var0 = var2;
                  var2 = var5;
            }

            if (var1 < var3) {
                  var5 = var1;
                  if (((1551245334 >>> 2 & 202078475 | 31146466 | 7592982) ^ 100392439) == 0) {
                        ;
                  }

                  var1 = var3;
                  var3 = var5;
            }

            GL11.glEnable(853 << 3 ^ 6582 ^ 2300);
            GL11.glDisable((1358 & 411 ^ 224) << 2 >> 1 ^ 3637);
            GL11.glBlendFunc(154 >> 1 & 25 ^ 8 ^ 771, (391 | 59) << 1 ^ 125);
            GL11.glEnable(1990 >>> 3 >> 1 ^ 2908);
            GL11.glPushMatrix();
            nameshower.glColor(var4);
            GL11.glBegin(1 << 1 << 2 ^ 15);
            double var10000 = (double)var0;
            if ((((1522191097 ^ 906246079) >> 2 ^ 17945537) & 134955028 ^ 134889488) == 0) {
                  ;
            }

            GL11.glVertex2d(var10000, (double)var3);
            GL11.glVertex2d((double)var2, (double)var3);
            if (((197588106 >> 1 | 69485166) & 17267073 ^ 942708140) != 0) {
                  ;
            }

            GL11.glVertex2d((double)var2, (double)var1);
            GL11.glVertex2d((double)var0, (double)var1);
            GL11.glEnd();
            if ((179827387 << 3 >>> 1 >>> 3 << 1 << 1 ^ 359654772) == 0) {
                  ;
            }

            Color var6 = new Color;
            if ((413312105 >> 1 << 1 << 2 ^ 275634870 ^ 1927760662) == 0) {
                  ;
            }

            if (!"your mom your dad the one you never had".equals("nefariousMoment")) {
                  ;
            }

            int var10002 = 199 >> 1 << 1 ^ 57;
            if (!"You're so fat whenever you go to the beach the tide comes in.".equals("please dont crack my plugin")) {
                  ;
            }

            var6.<init>(var10002, 216 >>> 1 << 4 & 1475 ^ 1087, (196 >>> 1 >> 3 ^ 0) >> 3 ^ 0 ^ 254, (177 | 161) & 155 & 123 ^ 238);
            nameshower.glColor(var6.getRGB());
            GL11.glPopMatrix();
            GL11.glEnable(577 ^ 541 ^ 18 ^ 3503);
            GL11.glDisable((1092 ^ 210) & 465 ^ 2992);
      }

      public void renderItem(ItemStack var1, int var2, int var3) {
            if ((1502798941 << 1 >> 3 ^ 1025709137 ^ -884495034) == 0) {
                  ;
            }

            FontRenderer var4 = mc.fontRenderer;
            Minecraft var10000 = mc;
            if (((144429014 >> 4 | 1336823) ^ 3305381 ^ 11506266) == 0) {
                  ;
            }

            RenderItem var5 = var10000.getRenderItem();
            nameshower.EnchantEntry[] var13 = new nameshower.EnchantEntry[(3 >> 2 | 1054336635) >>> 2 & 50597734 ^ 50596106];
            int var10002 = (25777258 ^ 9026506) << 3 ^ 29124086 ^ 163256566;
            nameshower.EnchantEntry var10003 = new nameshower.EnchantEntry(Enchantments.PROTECTION, "Pr");
            if ((26180 >>> 2 >>> 4 ^ 409) == 0) {
                  ;
            }

            var13[var10002] = var10003;
            var13[(0 | 1508887510 | 1458467492) >>> 4 & 76733382 ^ 76733319] = new nameshower.EnchantEntry(Enchantments.THORNS, "Th");
            var13[(0 ^ 713874372) << 2 & 1607840455 ^ 147340227 ^ 47782337] = new nameshower.EnchantEntry(Enchantments.SHARPNESS, "Shar");
            var10002 = (1 << 2 >>> 3 | 485330144) ^ 485330147;
            var10003 = new nameshower.EnchantEntry;
            if (((1241645978 >> 3 >> 4 | 6392858) ^ 16092191) == 0) {
                  ;
            }

            Enchantment var10005 = Enchantments.FIRE_ASPECT;
            if (((632517056 | 523442759 | 526193068 | 947869823) >> 4 ^ 1647203257) != 0) {
                  ;
            }

            var10003.<init>(var10005, "Fire");
            var13[var10002] = var10003;
            var13[3 << 4 >>> 2 << 3 >>> 3 & 0 ^ 4] = new nameshower.EnchantEntry(Enchantments.KNOCKBACK, "Kb");
            var13[(3 >>> 1 ^ 0 | 0) >> 2 << 4 ^ 5] = new nameshower.EnchantEntry(Enchantments.UNBREAKING, "Unb");
            var13[4 >>> 1 >> 4 >>> 2 ^ 6] = new nameshower.EnchantEntry(Enchantments.POWER, "Pow");
            var13[2 >> 4 ^ 1270964719 ^ 1150995564 ^ 257666436] = new nameshower.EnchantEntry(Enchantments.INFINITY, "Inf");
            var13[((6 ^ 2) << 1 ^ 0 | 7 | 12) ^ 7] = new nameshower.EnchantEntry(Enchantments.PUNCH, "Pun");
            var13[(0 >> 2 | 861973233) & 23203021 ^ 23070920] = new nameshower.EnchantEntry(Enchantments.LOOTING, "Loot");
            var10002 = (6 >> 2 & 0) << 4 ^ 10;
            if (!"i hope you catch fire ngl".equals("yo mama name maurice")) {
                  ;
            }

            var13[var10002] = new nameshower.EnchantEntry(Enchantments.SILK_TOUCH, "Silk");
            var10002 = 4 ^ 0 ^ 3 ^ 12;
            var10003 = new nameshower.EnchantEntry;
            if (((1976401277 >>> 1 & 315812154 | 217634041) ^ 519765243) == 0) {
                  ;
            }

            var10003.<init>(Enchantments.FORTUNE, "Fort");
            var13[var10002] = var10003;
            nameshower.EnchantEntry[] var6 = var13;
            GlStateManager.pushMatrix();
            GlStateManager.pushMatrix();
            float var14 = (float)(var2 - (((1 | 0 | 0) ^ 0) & 0 ^ 3));
            if (((1569951050 | 313330399) >>> 4 >>> 1 ^ 50199678) == 0) {
                  ;
            }

            GlStateManager.translate(var14, (float)(var3 + ((4 >>> 1 | 0 | 1) ^ 9)), 0.0F);
            if ((((1225058760 | 285806908) >>> 1 & 648251744) >>> 4 ^ 38283398) == 0) {
                  ;
            }

            GlStateManager.scale(0.3F, 0.3F, 0.3F);
            GlStateManager.popMatrix();
            if ((8650752 ^ 688795332) != 0) {
                  ;
            }

            RenderHelper.enableGUIStandardItemLighting();
            var5.zLevel = -100.0F;
            GlStateManager.disableDepth();
            var5.renderItemIntoGUI(var1, var2, var3);
            if (!"please take a shower".equals("please go outside")) {
                  ;
            }

            if ((21078860 << 1 ^ 41002479 ^ 15917943) == 0) {
                  ;
            }

            var5.renderItemOverlayIntoGUI(var4, var1, var2, var3, (String)null);
            if ((757768580 >>> 2 << 2 ^ 757768580) == 0) {
                  ;
            }

            GlStateManager.enableDepth();
            if (((204043801 >>> 1 >> 3 | 7921364) << 2 ^ -34315811) != 0) {
                  ;
            }

            nameshower.EnchantEntry[] var7 = var6;
            if ((114692224 << 4 << 1 >> 1 ^ -1681782858) != 0) {
                  ;
            }

            int var8 = var6.length;

            for(int var9 = 284024248 >>> 3 << 3 ^ 163557606 ^ 424832350; var9 < var8; ++var9) {
                  nameshower.EnchantEntry var10 = var7[var9];
                  int var11 = EnchantmentHelper.getEnchantmentLevel(var10.getEnchant(), var1);
                  StringBuilder var15 = new StringBuilder();
                  if (((2073340688 << 4 ^ 287680498) >> 4 ^ 1222712780 ^ -1301812061) == 0) {
                        ;
                  }

                  var15 = var15.append("");
                  if ((1439051 >>> 3 >>> 2 ^ 1001 ^ '蜐' ^ 2260823) != 0) {
                        ;
                  }

                  String var12 = var15.append(var11).toString();
                  if (((922710707 >>> 3 & 24291729 & 2392247 | 701) << 4 ^ 535504) == 0) {
                        ;
                  }

                  if (var11 > ((8 << 3 | 7) >>> 1 ^ 41)) {
                        var12 = "10+";
                  }

                  if (var11 > 0) {
                        GlStateManager.translate((float)(var2 - ((0 | 1107366663) << 3 >> 4 ^ 16812417)), (float)(var3 - ((0 >>> 1 ^ 1734431349) >> 4 ^ 108401958)), 0.0F);
                        GlStateManager.scale(0.42F, 0.42F, 0.42F);
                        GlStateManager.disableDepth();
                        GlStateManager.disableLighting();
                        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                        StringBuilder var10001 = new StringBuilder();
                        if (((794830340 << 1 >> 1 & 669717888) << 4 ^ 1979727872) == 0) {
                              ;
                        }

                        String var16 = var10001.append(var10.getName()).append(" ").append(var12).toString();
                        var10002 = (12 ^ 3) >>> 2 ^ 23;
                        StringBuilder var10004 = new StringBuilder;
                        if (!"shitted on you harder than archybot".equals("shitted on you harder than archybot")) {
                              ;
                        }

                        var10004.<init>();
                        String var19 = var10.getName();
                        if (((509649544 >>> 4 | 21142408) ^ 31890344) == 0) {
                              ;
                        }

                        float var18 = (float)(var10002 - var4.getStringWidth(var10004.append(var19).append(" ").append(var12).toString()) / (((1 | 0) & 0) << 3 >> 2 >>> 2 ^ 2));
                        if ((((791085623 ^ 722206340) & 58617086 & 1225000 | 6156) ^ 14380) == 0) {
                              ;
                        }

                        var4.drawString(var16, var18, 0.0F, (new Color(((67 | 10) >>> 3 & 5) >>> 2 ^ 255, 101 << 3 >> 1 >> 2 >> 4 << 4 ^ 159, (248 | 26) << 4 >>> 1 ^ 907 ^ 1188, (92 << 4 ^ 944) << 4 ^ 26623)).getRGB(), (boolean)((0 ^ 1952247530) >>> 4 ^ 122015471));
                        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                        GlStateManager.enableLighting();
                        GlStateManager.enableDepth();
                        if (!"please go outside".equals("you're dogshit")) {
                              ;
                        }

                        GlStateManager.scale(2.42F, 2.42F, 2.42F);
                        GlStateManager.translate((float)(-var2), (float)(-var3), 0.0F);
                        float var17 = (float)(var4.FONT_HEIGHT + ((1 & 0 | 887280009) >> 2 ^ 139249160 ^ 91517545));
                        if ((((1049576721 ^ 458860237) >> 3 | 27593921) ^ 96460027) == 0) {
                              ;
                        }

                        var3 -= (int)(var17 * 0.401F);
                  }
            }

            var5.zLevel = 0.0F;
            RenderHelper.disableStandardItemLighting();
            GlStateManager.enableAlpha();
            GlStateManager.disableBlend();
            GlStateManager.disableLighting();
            GlStateManager.popMatrix();
            if (((306092590 | 295656635) << 1 & 349353384 ^ 72529192) == 0) {
                  ;
            }

      }

      public static class EnchantEntry {
            private String name;
            private Enchantment enchant;

            public Enchantment getEnchant() {
                  Enchantment var10000 = this.enchant;
                  if (!"your mom your dad the one you never had".qProtect<invokedynamic>("your mom your dad the one you never had", "stringer is a good obfuscator")) {
                        ;
                  }

                  return var10000;
            }

            public EnchantEntry(Enchantment var1, String var2) {
                  if ((((939675520 << 2 ^ 1960769812) << 2 ^ 1239641058 | 114167199) ^ 519995327) == 0) {
                        ;
                  }

                  if (((754182765 | 442066606) >> 3 ^ 14792508 ^ 121555681) == 0) {
                        ;
                  }

                  this.enchant = var1;
                  this.name = var2;
            }

            public String getName() {
                  return this.name;
            }
      }
}
