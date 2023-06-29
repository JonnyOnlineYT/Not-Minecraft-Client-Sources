//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.visual;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
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
      private double xHit;
      private ArrayList hitStrings;
      public BooleanSetting mobSet;
      public ArrayList colorHits;
      public BooleanSetting invisibleSet;
      String whatStringR;
      public BooleanSetting animalSet;
      private double zHit;
      int whereXR;
      int whereYR;
      public BooleanSetting playerSet;
      Integer colorR;
      private TimerUtil stopRender;
      private Entity target;
      private boolean changePosRender;
      private double yHit;
      private Random random;

      public void onRenderWorldLast(float var1) {
            if (mc.player != null && !mc.player.isDead) {
                  List var2 = (List)mc.world.loadedEntityList.stream().filter((var0) -> {
                        EntityPlayerSP var10001 = mc.player;
                        if (!"stringer is a good obfuscator".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                              ;
                        }

                        int var10000 = var0 != var10001 ? ((0 | 980690935) >>> 4 | 61024741) ^ 61303806 : (2065944697 >> 2 ^ 204384523 | 67247017) << 4 ^ 1852177360;
                        if (!"yo mama name maurice".equals("shitted on you harder than archybot")) {
                              ;
                        }

                        return (boolean)var10000;
                  }).filter((var0) -> {
                        return (boolean)(!var0.isDead ? (0 ^ 1530319081) >>> 1 ^ 321452764 ^ 1051955881 : 1886993434 >> 3 & 174690460 ^ 168362112);
                  }).sorted(Comparator.comparing((var0) -> {
                        Float var10000 = Float.valueOf(mc.player.getDistance(var0));
                        if (((952019283 ^ 202349476 ^ 318790225) & 397180205 ^ -166887741) != 0) {
                              ;
                        }

                        return var10000;
                  })).collect(Collectors.toList());
                  Iterator var3 = var2.iterator();

                  while(true) {
                        while(var3.hasNext()) {
                              Entity var4 = (Entity)var3.next();
                              if ((389156073 >> 2 >> 2 ^ 3270496 ^ 21153710) == 0) {
                                    ;
                              }

                              if (var4 != null && var4 != mc.player) {
                                    double var5;
                                    double var7;
                                    double var9;
                                    double var10001;
                                    if (ModuleManager.getModuleByName("HitEffects").isToggled() && this.target != null && !this.stopRender.hasReached(585.0D)) {
                                          if (this.changePosRender) {
                                                if (!"shitted on you harder than archybot".equals("shitted on you harder than archybot")) {
                                                      ;
                                                }

                                                if (!"idiot".equals("stringer is a good obfuscator")) {
                                                      ;
                                                }

                                                var5 = mc.getRenderManager().viewerPosX;
                                                if (((1168031410 << 3 & 133819284 | 47189659) ^ 1475751805) != 0) {
                                                      ;
                                                }

                                                var7 = mc.getRenderManager().viewerPosY;
                                                var9 = mc.getRenderManager().viewerPosZ;
                                                if ((865193254 << 2 >> 1 ^ -417097140) == 0) {
                                                      ;
                                                }

                                                if (!"intentMoment".equals("please go outside")) {
                                                      ;
                                                }

                                                this.xHit = this.target.lastTickPosX + (this.target.posX - this.target.lastTickPosX) * (double)var1 - var5;
                                                if ((((1049027294 >> 3 & 39811274) >>> 1 ^ 12843637) & 14181768 ^ 13124608) == 0) {
                                                      ;
                                                }

                                                var10001 = this.target.lastTickPosY;
                                                double var10002 = this.target.posY;
                                                Entity var10003 = this.target;
                                                if ((937982999 >>> 3 >> 4 ^ 7327992) == 0) {
                                                      ;
                                                }

                                                this.yHit = var10001 + (var10002 - var10003.lastTickPosY) * (double)var1 - var7;
                                                var10001 = this.target.lastTickPosZ;
                                                Entity var21 = this.target;
                                                if ((232036175 >>> 4 << 1 ^ 422946635) != 0) {
                                                      ;
                                                }

                                                var10002 = var21.posZ;
                                                if ((1941506504 >> 3 >> 4 ^ -1279194173) != 0) {
                                                      ;
                                                }

                                                this.zHit = var10001 + (var10002 - this.target.lastTickPosZ) * (double)var1 - var9;
                                                this.whatStringR = (String)this.hitStrings.get(this.random.nextInt(0 << 2 << 2 ^ 1745381384 ^ 1745381390));
                                                ArrayList var19 = this.colorHits;
                                                if ((1490078412 >>> 2 << 1 & 409146807 ^ 140510502) == 0) {
                                                      ;
                                                }

                                                this.colorR = (Integer)var19.get(this.random.nextInt((1 << 2 | 2) & 2 & 1 ^ 4));
                                                Random var20;
                                                int var24;
                                                if (this.random.nextBoolean()) {
                                                      if ((163318307 << 3 >> 1 & 321705386 ^ 35651720) == 0) {
                                                            ;
                                                      }

                                                      if (this.random.nextBoolean()) {
                                                            this.whereXR = -this.random.nextInt((5 | 4) >>> 1 << 4 << 3 ^ 306) + ((1 << 2 & 1) >> 4 ^ 25);
                                                            if (!"yo mama name maurice".equals("nefariousMoment")) {
                                                                  ;
                                                            }

                                                            var20 = this.random;
                                                            var24 = 5 << 1 ^ 7 ^ 10 ^ 13;
                                                            if (!"nefariousMoment".equals("i hope you catch fire ngl")) {
                                                                  ;
                                                            }

                                                            this.whereYR = -var20.nextInt(var24) - ((0 >>> 1 ^ 1679455829) << 1 << 2 ^ 550744738);
                                                      } else {
                                                            if (!"your mom your dad the one you never had".equals("i hope you catch fire ngl")) {
                                                                  ;
                                                            }

                                                            this.whereXR = this.random.nextInt((2 | 1 | 1 | 0 | 1) ^ 49) + (19 << 3 ^ 47 ^ 32 ^ 142);
                                                            this.whereYR = this.random.nextInt((34 >>> 4 | 0) ^ 48) + ((4 << 3 >> 3 | 1) >>> 4 ^ 25);
                                                      }
                                                } else if (this.random.nextBoolean()) {
                                                      if (((55750193 >> 2 >>> 3 & 705315) << 1 ^ 1376834) == 0) {
                                                            ;
                                                      }

                                                      int var22 = -this.random.nextInt((22 >> 1 ^ 6) >>> 1 >>> 2 ^ 51);
                                                      var24 = ((20 & 14) << 1 >>> 4 | 270041550) ^ 270041559;
                                                      if (!"your mom your dad the one you never had".equals("please go outside")) {
                                                            ;
                                                      }

                                                      this.whereXR = var22 - var24;
                                                      var22 = this.random.nextInt((49 >>> 3 >> 4 ^ 1825240275) >> 3 ^ 228155048);
                                                      var24 = 19 << 2 >> 2 ^ 10;
                                                      if (!"i hope you catch fire ngl".equals("stop skidding")) {
                                                            ;
                                                      }

                                                      this.whereYR = var22 - var24;
                                                      if ((2104770718 >> 1 >>> 1 >> 3 ^ 65774084) == 0) {
                                                            ;
                                                      }
                                                } else {
                                                      this.whereXR = this.random.nextInt((2 | 0) << 4 >>> 4 ^ 48) + ((18 | 4) >> 3 >>> 4 >>> 1 & 1552873131 ^ 25);
                                                      var20 = this.random;
                                                      if (!"you probably spell youre as your".equals("stringer is a good obfuscator")) {
                                                            ;
                                                      }

                                                      this.whereYR = -var20.nextInt((5 & 1 | 0) >>> 1 >>> 4 & 1451922309 ^ 10) - ((8 << 4 & 19) >> 1 >>> 4 ^ 10);
                                                }

                                                this.changePosRender = (boolean)((423269819 ^ 404612291) << 1 ^ 6145258 ^ 34809370);
                                          }

                                          if (this.target != null) {
                                                this.renderNameTag((EntityLivingBase)this.target, this.target.getName(), this.xHit, this.yHit, this.zHit, (boolean)(0 >> 1 >> 3 >> 3 ^ 1), this.whereXR, this.whereYR, this.whatStringR, this.colorR.intValue());
                                          }
                                    }

                                    double var11;
                                    double var13;
                                    double var15;
                                    double var18;
                                    EntityLivingBase var25;
                                    if (this.invisibleSet.isEnabled() && var4.isInvisible()) {
                                          if ((1770318573 >> 3 << 3 >> 4 ^ 110644910) == 0) {
                                                ;
                                          }

                                          if (var4 instanceof EntityPlayer && this.playerSet.isEnabled() || var4 instanceof EntityMob && this.mobSet.isEnabled() || var4 instanceof EntityAnimal && this.animalSet.isEnabled()) {
                                                var5 = mc.getRenderManager().viewerPosX;
                                                var7 = mc.getRenderManager().viewerPosY;
                                                RenderManager var23 = mc.getRenderManager();
                                                if (((617508462 << 3 ^ 337727177) >>> 3 << 3 ^ 844250552) == 0) {
                                                      ;
                                                }

                                                var9 = var23.viewerPosZ;
                                                var11 = var4.lastTickPosX + (var4.posX - var4.lastTickPosX) * (double)var1 - var5;
                                                var13 = var4.lastTickPosY + (var4.posY - var4.lastTickPosY) * (double)var1 - var7;
                                                if (((76753224 ^ 34304447) >> 1 ^ 1134040195) != 0) {
                                                      ;
                                                }

                                                var18 = var4.lastTickPosZ;
                                                if (((1725088517 << 3 & 444343253) >> 4 ^ 18956288) == 0) {
                                                      ;
                                                }

                                                var18 = var18 + (var4.posZ - var4.lastTickPosZ) * (double)var1 - var9;
                                                if (((((1502582305 | 1055659268) ^ 1647548254) << 4 >>> 4 | 77379327) ^ -1740640025) != 0) {
                                                      ;
                                                }

                                                var15 = var18;
                                                var25 = (EntityLivingBase)var4;
                                                if ((58916933 >> 1 ^ 915908961) != 0) {
                                                      ;
                                                }

                                                this.renderNameTag(var25, var4.getName(), var11, var13, var15, (boolean)((59851297 ^ 55012544) << 1 ^ 28073410), (1960573972 | 767676428) >>> 1 ^ 1055784462, (716924129 << 4 | 1003385317) >>> 2 ^ 788504573, "None", (1140998760 | 219597896) >>> 1 >> 2 ^ 161667533);
                                                continue;
                                          }
                                    }

                                    if (this.mobSet.isEnabled()) {
                                          boolean var10000 = var4 instanceof EntityMob;
                                          if (!"stringer is a good obfuscator".equals("please get a girlfriend and stop cracking plugins")) {
                                                ;
                                          }

                                          if (var10000) {
                                                var5 = mc.getRenderManager().viewerPosX;
                                                if (((621281800 ^ 212214864 | 426838164) ^ 973024988) == 0) {
                                                      ;
                                                }

                                                var7 = mc.getRenderManager().viewerPosY;
                                                var18 = mc.getRenderManager().viewerPosZ;
                                                if ((((1693411371 ^ 112408750 ^ 334231189) & 991853307) << 1 & 972786611 ^ 539492384) == 0) {
                                                      ;
                                                }

                                                var9 = var18;
                                                var18 = var4.lastTickPosX;
                                                var10001 = var4.posX - var4.lastTickPosX;
                                                if (((2013266434 >>> 1 >> 2 ^ 172895077) >> 3 ^ 11126116) == 0) {
                                                      ;
                                                }

                                                var11 = var18 + var10001 * (double)var1 - var5;
                                                if (((1291738314 | 1088061528) << 2 >>> 4 ^ 26646323 ^ 44638213) == 0) {
                                                      ;
                                                }

                                                var18 = var4.lastTickPosY;
                                                var10001 = var4.posY;
                                                if ((16790209 ^ 8381220 ^ 25154021) == 0) {
                                                      ;
                                                }

                                                var13 = var18 + (var10001 - var4.lastTickPosY) * (double)var1 - var7;
                                                if (((1927623174 >> 2 >> 2 ^ 40720711 | 10328310) ^ 98540279) == 0) {
                                                      ;
                                                }

                                                var15 = var4.lastTickPosZ + (var4.posZ - var4.lastTickPosZ) * (double)var1 - var9;
                                                this.renderNameTag((EntityLivingBase)var4, var4.getName(), var11, var13, var15, (boolean)(((574430428 | 72372128 | 49351348) >> 2 | 24396670) ^ 167729151), ((1277042057 | 1233344202) << 3 ^ 795039482 ^ 1021499883) >> 4 ^ 133674772, 210537624 << 3 << 1 >> 2 ^ -231591328, "None", (679608647 >> 2 ^ 145322151) << 3 ^ 340756400);
                                                continue;
                                          }
                                    }

                                    String var26;
                                    if (this.animalSet.isEnabled() && var4 instanceof EntityAnimal) {
                                          var5 = mc.getRenderManager().viewerPosX;
                                          var7 = mc.getRenderManager().viewerPosY;
                                          if (((37493010 ^ 23853703) & 46307696 ^ 786342328) != 0) {
                                                ;
                                          }

                                          var9 = mc.getRenderManager().viewerPosZ;
                                          var18 = var4.lastTickPosX + (var4.posX - var4.lastTickPosX) * (double)var1 - var5;
                                          if ((527510510 >> 4 >> 4 & 593657 ^ 589865) == 0) {
                                                ;
                                          }

                                          var11 = var18;
                                          var13 = var4.lastTickPosY + (var4.posY - var4.lastTickPosY) * (double)var1 - var7;
                                          if (((9834735 << 1 ^ 4844059) >> 1 ^ 10729396 ^ 1135190) == 0) {
                                                ;
                                          }

                                          if (((1070630288 | 286512957) >> 4 >>> 1 ^ 33463997) == 0) {
                                                ;
                                          }

                                          var18 = var4.lastTickPosZ;
                                          var10001 = (var4.posZ - var4.lastTickPosZ) * (double)var1;
                                          if ((((12566998 << 3 & 6035870) >> 2 | 122173) & 1105254 ^ 969945441) != 0) {
                                                ;
                                          }

                                          var15 = var18 + var10001 - var9;
                                          var25 = (EntityLivingBase)var4;
                                          var26 = var4.getName();
                                          int var10006 = ((1282224617 | 750548421) ^ 1764779754) >> 2 ^ 24331457;
                                          int var10007 = (1556149015 << 1 & 1850529415 | 669028460) ^ 803264110;
                                          if (((1408 | 1085) ^ 1469) == 0) {
                                                ;
                                          }

                                          this.renderNameTag(var25, var26, var11, var13, var15, (boolean)var10006, var10007, ((1778128572 ^ 1371647085) >> 3 ^ 72392524) & 43076003 ^ 34670594, "None", (14726672 >>> 1 & 3875273) >> 1 ^ 1572996);
                                          continue;
                                    }

                                    BooleanSetting var17 = this.playerSet;
                                    if ((1388445114 >>> 1 >>> 4 ^ -2113001080) != 0) {
                                          ;
                                    }

                                    if (var17.isEnabled() && var4 instanceof EntityPlayer) {
                                          var5 = mc.getRenderManager().viewerPosX;
                                          var7 = mc.getRenderManager().viewerPosY;
                                          var9 = mc.getRenderManager().viewerPosZ;
                                          if ((1150521101 >>> 4 >> 1 >> 1 >>> 1 ^ -1351708406) != 0) {
                                                ;
                                          }

                                          var11 = var4.lastTickPosX + (var4.posX - var4.lastTickPosX) * (double)var1 - var5;
                                          var18 = var4.lastTickPosY;
                                          var10001 = var4.posY - var4.lastTickPosY;
                                          if (((368386245 << 3 | 99331250) ^ -1343508806) == 0) {
                                                ;
                                          }

                                          var13 = var18 + var10001 * (double)var1 - var7;
                                          var18 = var4.lastTickPosZ;
                                          var10001 = var4.posZ;
                                          if (!"please dont crack my plugin".equals("yo mama name maurice")) {
                                                ;
                                          }

                                          var15 = var18 + (var10001 - var4.lastTickPosZ) * (double)var1 - var9;
                                          var25 = (EntityLivingBase)var4;
                                          if (!"i hope you catch fire ngl".equals("i hope you catch fire ngl")) {
                                                ;
                                          }

                                          var26 = var4.getName();
                                          if (((1035248693 >> 1 & 133590441) >>> 2 ^ -1354022429) != 0) {
                                                ;
                                          }

                                          this.renderNameTag(var25, var26, var11, var13, var15, (boolean)(105381890 >> 1 ^ 52690945), ((88948833 | 78041403) & 64504123) << 4 ^ 478417840, 300192 >>> 3 & 29456 ^ 1357 ^ 5981, "None", (1235321915 | 560196813) >> 4 & 26258273 & 2118698 ^ 0);
                                    }
                              }

                              if (!"your mom your dad the one you never had".equals("nefariousMoment")) {
                                    ;
                              }
                        }

                        if (!"nefariousMoment".equals("shitted on you harder than archybot")) {
                              ;
                        }

                        return;
                  }
            }
      }

      public nameshower() {
            super("NameTags", "shows info about other players", 439288190 >> 1 & 17652755 & 6096541 ^ 327697, Module.Category.RENDER);
            BooleanSetting var10001 = new BooleanSetting("Mobs", this, (boolean)(686044861 >> 3 >> 2 >>> 1 ^ 10719450));
            if (!"you probably spell youre as your".equals("shitted on you harder than archybot")) {
                  ;
            }

            this.mobSet = var10001;
            this.animalSet = new BooleanSetting("Animals", this, (boolean)((289473552 << 4 ^ 216537647) << 3 ^ -943531656));
            var10001 = new BooleanSetting;
            if ((((25952386 | 12107960) ^ 18773012) >> 4 ^ -307893200) != 0) {
                  ;
            }

            var10001.<init>("Players", this, (boolean)((0 ^ 2116273944) << 2 >>> 1 ^ 2085064241));
            this.playerSet = var10001;
            this.invisibleSet = new BooleanSetting("Invisible", this, (boolean)(((0 << 3 ^ 1132977625) & 244915629 & 19636804 | 26850) ^ 92387));
            this.random = new Random();
            ArrayList var1 = new ArrayList;
            if ((49638704 << 4 >>> 1 ^ 211062927 ^ 457177359) == 0) {
                  ;
            }

            var1.<init>();
            this.hitStrings = var1;
            this.colorHits = new ArrayList();
            this.stopRender = new TimerUtil();
            this.changePosRender = (boolean)((76423201 | 47783959) >> 4 >> 2 ^ 343612 ^ 1983164);
            this.target = null;
            Setting[] var2 = new Setting[(1 >> 4 | 1251004917) ^ 1251004913];
            var2[1227887302 >> 3 >>> 1 ^ 76742956] = this.mobSet;
            var2[0 >> 1 >>> 4 << 1 ^ 1] = this.animalSet;
            var2[(1 << 2 & 2) >> 1 ^ 2026765160 ^ 2026765162] = this.playerSet;
            var2[(1 ^ 0 | 0 | 0 | 0) << 3 ^ 11] = this.invisibleSet;
            if (((269800456 | 196841553 | 249716307 | 39062792) ^ -1497991784) != 0) {
                  ;
            }

            this.addSettings(var2);
            if (((836088178 >>> 4 & 25506971) >>> 4 ^ 1843107476) != 0) {
                  ;
            }

            this.hitStrings.add("SMASH!");
            this.hitStrings.add("KABOOM");
            this.hitStrings.add("WHAM");
            if (((1056737901 ^ 340323570 ^ 486017530) << 3 ^ 1433693950 ^ -417917482) == 0) {
                  ;
            }

            if ((902896978 << 4 << 4 ^ 1195349902 ^ -1776106098) == 0) {
                  ;
            }

            this.hitStrings.add("BOOM!");
            this.hitStrings.add("KAPOW!");
            this.hitStrings.add("RAGE!");
            ArrayList var10000 = this.colorHits;
            Color var3 = new Color;
            int var10003 = (241 ^ 178) >> 4 ^ 251;
            int var10004 = (700742501 | 579088881) >> 4 >> 3 ^ 5736695;
            int var10005 = 2001677539 >>> 1 << 1 << 3 ^ 276907785 ^ -1426579431;
            int var10006 = (167 << 1 | 265 | 180) ^ 101 ^ 357;
            if (((31669515 ^ 29554147) & 1651870 ^ 172206498) != 0) {
                  ;
            }

            var3.<init>(var10003, var10004, var10005, var10006);
            var10000.add(Integer.valueOf(var3.getRGB()));
            this.colorHits.add(Integer.valueOf((new Color(972988305 >>> 2 & 43324232 ^ 34931520, 137 >> 2 >>> 3 ^ 251, (921311883 >> 1 ^ 228150873) >> 3 >>> 2 ^ 12018200, (145 | 58) >> 1 ^ 18 ^ 176)).getRGB()));
            this.colorHits.add(Integer.valueOf((new Color((128 << 1 | 187 | 417) ^ 324, 1158208009 >>> 4 ^ 27519341 ^ 99837133, (120 | 63) ^ 22 ^ 150, (12 & 6 | 0) ^ 251)).getRGB()));
            if ((((1555131976 ^ 807626669 | 1378077673) ^ 356180039) >> 1 ^ 902075861) == 0) {
                  ;
            }

            var10000 = this.colorHits;
            var3 = new Color;
            var10003 = 174 >> 2 >>> 1 << 2 ^ 171;
            var10004 = 148 << 3 << 1 & 762 ^ 191;
            if ((1615137633 >> 1 & 312996754 ^ 270696848) == 0) {
                  ;
            }

            var3.<init>(var10003, var10004, 827042790 << 2 << 3 ^ 695565504, (11 >> 2 | 0) & 0 ^ 255);
            var10000.add(Integer.valueOf(var3.getRGB()));
            if ((438443700 >>> 1 >>> 3 >> 1 ^ 1706357942) != 0) {
                  ;
            }

            var10000 = this.colorHits;
            var3 = new Color;
            var10003 = (91 | 23) >> 1 << 3 ^ 261;
            if (((1701416296 >> 1 ^ 44164922) << 3 ^ 532106095) != 0) {
                  ;
            }

            var3.<init>(var10003, (75 >> 3 | 2) << 4 ^ 205, ((196 | 162) >>> 1 ^ 62 | 71) ^ 176, 112 >>> 4 << 2 & 23 ^ 235);
            var10000.add(Integer.valueOf(var3.getRGB()));
      }

      public static String getPlayerName(EntityPlayer var0) {
            if (((1611980734 >> 1 & 403201290) << 1 ^ 300454384 ^ -2111764317) != 0) {
                  ;
            }

            return "null";
      }

      @SubscribeEvent
      public void onAttackEvent(AttackEntityEvent var1) {
            if (var1.getTarget() != null) {
                  if (!"Some babies were dropped on their heads but you were clearly thrown at a wall.".equals("please take a shower")) {
                        ;
                  }

                  this.target = var1.getTarget();
                  int var10001 = (0 << 2 ^ 1526317171) & 417463052 ^ 417447937;
                  if (!"You're so fat whenever you go to the beach the tide comes in.".equals("you probably spell youre as your")) {
                        ;
                  }

                  this.changePosRender = (boolean)var10001;
                  this.stopRender.reset();
            }

            TimerUtil var10000 = this.stopRender;
            if ((((1054345940 ^ 206588384) >>> 3 ^ 97062965) & 19308114 ^ 16783442) == 0) {
                  ;
            }

            if (var10000.hasReached(585.0D) && !this.changePosRender) {
                  this.target = null;
                  this.stopRender.reset();
            }

      }

      public static int getMiddle(int var0, int var1) {
            if ((35741712 ^ 24013525 ^ -963912488) != 0) {
                  ;
            }

            return (var0 + var1) / ((0 & 1269686658) >>> 4 >>> 2 ^ 2);
      }

      public static void drawRect(float var0, float var1, float var2, float var3, int var4) {
            float var5;
            if (var0 < var2) {
                  if (((1746370372 | 771599767) >> 3 ^ 230686714) == 0) {
                        ;
                  }

                  var5 = var0;
                  var0 = var2;
                  var2 = var5;
            }

            if (var1 < var3) {
                  var5 = var1;
                  if (((1402462588 >> 3 | 40412722) << 1 & 131733696 ^ 80746560) == 0) {
                        ;
                  }

                  var1 = var3;
                  var3 = var5;
            }

            GL11.glEnable(507 << 2 << 1 ^ 1082);
            GL11.glDisable(2202 >> 1 << 4 & 13785 & 1097 ^ 2465);
            GL11.glBlendFunc((70 ^ 38) >> 4 >>> 1 ^ 769, (172 | 34) >>> 2 >> 4 ^ 769);
            GL11.glEnable(729 >>> 1 >> 4 ^ 2870);
            GL11.glPushMatrix();
            nameshower.glColor(var4);
            GL11.glBegin(4 << 2 >>> 2 >> 1 ^ 5);
            if (((1825313501 ^ 1374231627 | 675691578) << 3 ^ -348523024) == 0) {
                  ;
            }

            GL11.glVertex2d((double)var0, (double)var3);
            GL11.glVertex2d((double)var2, (double)var3);
            GL11.glVertex2d((double)var2, (double)var1);
            GL11.glVertex2d((double)var0, (double)var1);
            GL11.glEnd();
            nameshower.glColor((new Color((49 >>> 4 ^ 2 | 0) ^ 254, (165 >>> 4 ^ 2 | 6) ^ 4 ^ 245, (226 & 80) << 2 << 4 ^ 4351, (13 & 8) << 3 ^ 191)).getRGB());
            GL11.glPopMatrix();
            GL11.glEnable((2477 | 2299) >> 3 >>> 2 ^ 3502);
            GL11.glDisable(((679 << 4 ^ 1117) & 665 | 386) ^ 2219);
      }

      public void renderItem(ItemStack var1, int var2, int var3) {
            Minecraft var10000 = mc;
            if (((6310937 ^ 243967) & 4192107 ^ 2356322) == 0) {
                  ;
            }

            FontRenderer var4 = var10000.fontRenderer;
            RenderItem var5 = mc.getRenderItem();
            nameshower.EnchantEntry[] var13 = new nameshower.EnchantEntry[3 >> 2 << 2 >>> 2 >>> 1 ^ 12];
            int var10002 = (616114163 ^ 104961711) >>> 4 >> 2 ^ 9167557;
            nameshower.EnchantEntry var10003 = new nameshower.EnchantEntry;
            Enchantment var10005 = Enchantments.PROTECTION;
            if ((974945459 >> 1 << 3 >>> 4 ^ 243736364) == 0) {
                  ;
            }

            var10003.<init>(var10005, "Pr");
            var13[var10002] = var10003;
            var10002 = ((0 | 1178942161) >> 2 ^ 56224996 ^ 314673555 | 477100) ^ 1011694;
            var10003 = new nameshower.EnchantEntry;
            var10005 = Enchantments.THORNS;
            if ((788049745 >> 2 << 4 ^ 1831105858 ^ 817841803) != 0) {
                  ;
            }

            var10003.<init>(var10005, "Th");
            var13[var10002] = var10003;
            var13[1 << 3 >> 1 ^ 6] = new nameshower.EnchantEntry(Enchantments.SHARPNESS, "Shar");
            if ((8 ^ 8) == 0) {
                  ;
            }

            var13[(1 << 2 ^ 1 | 2) & 2 ^ 1] = new nameshower.EnchantEntry(Enchantments.FIRE_ASPECT, "Fire");
            var13[(1 >> 2 | 617992217) ^ 617992221] = new nameshower.EnchantEntry(Enchantments.KNOCKBACK, "Kb");
            var10002 = ((3 ^ 2) & 0) >>> 4 >> 4 ^ 5;
            if (((33576648 ^ 23132595) >> 4 & 1601397 ^ 1782414584) != 0) {
                  ;
            }

            var13[var10002] = new nameshower.EnchantEntry(Enchantments.UNBREAKING, "Unb");
            var13[(4 >> 4 ^ 1603084152 | 963842147) ^ 2147426173] = new nameshower.EnchantEntry(Enchantments.POWER, "Pow");
            var10002 = (6 << 1 | 9) & 5 ^ 2;
            var10003 = new nameshower.EnchantEntry(Enchantments.INFINITY, "Inf");
            if ((((2075543573 >> 2 ^ 347970699) & 30099164 | 2542982) ^ 6737806) == 0) {
                  ;
            }

            var13[var10002] = var10003;
            var13[6 >>> 1 >> 3 << 4 ^ 8] = new nameshower.EnchantEntry(Enchantments.PUNCH, "Pun");
            var13[((2 | 0) >> 4 ^ 194872451 | 105001671) ^ 266319566] = new nameshower.EnchantEntry(Enchantments.LOOTING, "Loot");
            var10002 = 2 >> 2 << 4 & 1199862533 ^ 1400170410 ^ 1400170400;
            if (((1056425525 | 350294624 | 781798561 | 444309337) >>> 4 ^ 66059775) == 0) {
                  ;
            }

            var13[var10002] = new nameshower.EnchantEntry(Enchantments.SILK_TOUCH, "Silk");
            var10002 = (1 | 0) >> 3 ^ 11;
            var10003 = new nameshower.EnchantEntry;
            var10005 = Enchantments.FORTUNE;
            if ((((860134216 | 493498941) >> 3 ^ 97636338) >>> 2 ^ 1320257871) != 0) {
                  ;
            }

            var10003.<init>(var10005, "Fort");
            var13[var10002] = var10003;
            nameshower.EnchantEntry[] var6 = var13;
            GlStateManager.pushMatrix();
            GlStateManager.pushMatrix();
            float var14 = (float)(var2 - ((2 ^ 1 | 0) >>> 2 >> 1 ^ 187642475 ^ 187642472));
            float var10001 = (float)(var3 + ((6 >>> 4 & 1169337346) << 4 ^ 10));
            if (!"i hope you catch fire ngl".equals("please dont crack my plugin")) {
                  ;
            }

            GlStateManager.translate(var14, var10001, 0.0F);
            GlStateManager.scale(0.3F, 0.3F, 0.3F);
            GlStateManager.popMatrix();
            RenderHelper.enableGUIStandardItemLighting();
            var5.zLevel = -100.0F;
            GlStateManager.disableDepth();
            if (!"your mom your dad the one you never had".equals("you're dogshit")) {
                  ;
            }

            var5.renderItemIntoGUI(var1, var2, var3);
            var5.renderItemOverlayIntoGUI(var4, var1, var2, var3, (String)null);
            GlStateManager.enableDepth();
            nameshower.EnchantEntry[] var7 = var6;
            int var8 = var6.length;

            for(int var9 = (270671042 | 74113049) & 15934702 ^ 6431946; var9 < var8; ++var9) {
                  nameshower.EnchantEntry var10 = var7[var9];
                  if (!"ape covered in human flesh".equals("please take a shower")) {
                        ;
                  }

                  if (((2038467916 ^ 8188232) >> 1 << 2 ^ 1145913907) != 0) {
                        ;
                  }

                  int var11 = EnchantmentHelper.getEnchantmentLevel(var10.getEnchant(), var1);
                  String var12 = (new StringBuilder()).append("").append(var11).toString();
                  if ((1541309461 >>> 4 ^ 70958079 ^ -1054741762) != 0) {
                        ;
                  }

                  if (var11 > (1 << 3 << 3 >>> 4 ^ 14)) {
                        var12 = "10+";
                  }

                  if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("you're dogshit")) {
                        ;
                  }

                  if (var11 > 0) {
                        if ((213288679 >>> 2 & 650513 ^ 778615182) != 0) {
                              ;
                        }

                        int var15 = 0 >>> 4 >> 4 >>> 2 & 1923255316 ^ 2;
                        if (!"ape covered in human flesh".equals("please dont crack my plugin")) {
                              ;
                        }

                        var14 = (float)(var2 - var15);
                        if ((1418107265 >>> 3 >>> 2 >> 2 ^ 1556823569) != 0) {
                              ;
                        }

                        var15 = var3 - ((0 ^ 978313682 ^ 974771748) & 1479361 ^ 1446593);
                        if ((((688131778 >>> 3 ^ 12570910) << 1 | 137021600) ^ 188672428) == 0) {
                              ;
                        }

                        GlStateManager.translate(var14, (float)var15, 0.0F);
                        if (!"buy a domain and everything else you need at namecheap.com".equals("nefariousMoment")) {
                              ;
                        }

                        GlStateManager.scale(0.42F, 0.42F, 0.42F);
                        GlStateManager.disableDepth();
                        GlStateManager.disableLighting();
                        if (((1430325379 | 433060435) >>> 4 ^ 98369453) == 0) {
                              ;
                        }

                        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                        if (((1746691632 >>> 4 & 105470894) << 3 & 694826212 ^ 537530368) == 0) {
                              ;
                        }

                        String var16 = (new StringBuilder()).append(var10.getName()).append(" ").append(var12).toString();
                        float var17 = (float)(((6 >> 4 | 1143555833 | 1071106586) ^ 2147471087) - var4.getStringWidth((new StringBuilder()).append(var10.getName()).append(" ").append(var12).toString()) / ((1 ^ 0) >> 1 << 2 ^ 2));
                        int var10004 = (new Color(9 << 3 >> 1 >>> 2 ^ 246, 70 >> 1 >>> 2 << 2 ^ 223, (76 & 45) >> 3 ^ 0 ^ 254, (122 >>> 1 >>> 2 | 13) << 3 ^ 135)).getRGB();
                        if ((442713319 >> 1 >>> 1 >>> 3 ^ -487487588) != 0) {
                              ;
                        }

                        var4.drawString(var16, var17, 0.0F, var10004, (boolean)(0 >>> 4 >>> 2 << 4 ^ 1));
                        if (((2056037854 ^ 1343167471 | 13845153) & 37229689 ^ -1872973612) != 0) {
                              ;
                        }

                        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                        GlStateManager.enableLighting();
                        GlStateManager.enableDepth();
                        GlStateManager.scale(2.42F, 2.42F, 2.42F);
                        if (((2132442625 ^ 1270989742) >> 2 ^ 221704043) == 0) {
                              ;
                        }

                        var14 = (float)(-var2);
                        if (((268472450 << 2 >> 1 | 333022693) ^ 869901797) == 0) {
                              ;
                        }

                        GlStateManager.translate(var14, (float)(-var3), 0.0F);
                        var3 -= (int)((float)(var4.FONT_HEIGHT + ((2 | 1) >>> 1 << 3 & 7 ^ 3)) * 0.401F);
                  }
            }

            var5.zLevel = 0.0F;
            RenderHelper.disableStandardItemLighting();
            if (((43068776 >> 2 | 5402814) ^ 79328843) != 0) {
                  ;
            }

            GlStateManager.enableAlpha();
            GlStateManager.disableBlend();
            GlStateManager.disableLighting();
            if ((1008714288 >>> 1 ^ 75779007 ^ -1857843551) != 0) {
                  ;
            }

            GlStateManager.popMatrix();
      }

      public void renderArmor(EntityPlayer var1, int var2, int var3) {
            InventoryPlayer var4 = var1.inventory;
            ItemStack var5 = var1.getHeldItemMainhand();
            ItemStack var6 = var4.armorItemInSlot((1387112685 >> 2 >>> 4 & 4909637) >>> 3 ^ 611392);
            ItemStack var7 = var4.armorItemInSlot((0 << 2 | 2047360064) << 2 << 4 & 1664787184 ^ 34209793);
            ItemStack var8 = var4.armorItemInSlot(((0 & 1028327143 | 1607150835) & 1245030083) >>> 4 ^ 77599310);
            if ((75249237 >> 2 >>> 2 << 2 >>> 4 ^ 1175769) == 0) {
                  ;
            }

            ItemStack var9 = var4.armorItemInSlot(1 >>> 2 << 3 << 4 ^ 3);
            ItemStack[] var10 = null;
            ItemStack[] var10000;
            if (var5 != null) {
                  var10000 = new ItemStack[((1 & 0 | 830701276) >> 4 >> 4 | 5799) ^ 3250170];
                  var10000[1477553454 >>> 2 >> 1 ^ 184694181] = var5;
                  var10000[0 >>> 3 >>> 4 >> 4 >>> 3 ^ 1] = var9;
                  if (((341246555 | 322694973) << 4 ^ 2013198320) == 0) {
                        ;
                  }

                  var10000[1 & 0 & 750504043 ^ 2] = var8;
                  var10000[(1 | 0) << 2 >> 4 >>> 1 ^ 3] = var7;
                  var10000[1 >> 2 >>> 4 ^ 4] = var6;
                  var10 = var10000;
            } else {
                  var10000 = new ItemStack[((3 | 1) & 1 | 0) ^ 5];
                  if (((1379034132 ^ 1056081260) & 1653690660 ^ 1619018016) == 0) {
                        ;
                  }

                  var10000[(4416 << 1 | 7551) << 4 ^ 262128] = var9;
                  var10000[0 >>> 2 & 991461216 ^ 1901470 ^ 1901471] = var8;
                  var10000[(0 << 1 & 552268287) >> 1 >>> 2 ^ 2] = var7;
                  var10000[(2 ^ 1) >> 4 ^ 3] = var6;
                  if ((1985326645 << 2 ^ 1412578560 ^ -1922791980) == 0) {
                        ;
                  }

                  var10 = var10000;
            }

            if ((713269408 << 1 ^ 124762641 ^ 1382611793) == 0) {
                  ;
            }

            ArrayList var11 = new ArrayList();
            ItemStack[] var12 = var10;
            int var13 = var10.length;
            int var14 = (623735474 >> 1 >> 3 | 17565427) >>> 3 ^ 7068415;

            while(var14 < var13) {
                  if (((139631400 | 120755561) ^ 258337595 ^ -1013587292) != 0) {
                        ;
                  }

                  ItemStack var15 = var12[var14];
                  if (!"please take a shower".equals("yo mama name maurice")) {
                        ;
                  }

                  if (var15 != null && var15.getItem() != null) {
                        var11.add(var15);
                        if (((1817908226 | 451462258) ^ 2036599115 ^ 127917369) == 0) {
                              ;
                        }
                  }

                  ++var14;
                  if (!"you probably spell youre as your".equals("please take a shower")) {
                        ;
                  }
            }

            int var18 = (9 >> 4 >> 2 >>> 1 & 888889721 ^ 16) * var11.size();
            if (((1756449228 | 390309140) & 1507057161 ^ 627838449 ^ 2092941817) == 0) {
                  ;
            }

            var14 = var18 / ((1 & 0 | 1853865201) >> 1 ^ 926932602);
            if (((1340970872 | 747504678) << 3 & 1096540487 ^ 353270306 ^ 1414978402) == 0) {
                  ;
            }

            var2 -= var14;
            GlStateManager.disableDepth();

            for(Iterator var17 = var11.iterator(); var17.hasNext(); var2 += 16) {
                  ItemStack var16 = (ItemStack)var17.next();
                  this.renderItem(var16, var2, var3);
            }

            GlStateManager.enableDepth();
      }

      public static void glColor(int var0) {
            float var10000 = (float)(var0 >> (((0 ^ 315779429 ^ 64100111) >>> 2 ^ 38778845) << 1 ^ 203378846) & (((116 | 9) & 27 | 0) ^ 230)) / 255.0F;
            int var10001 = var0 >> (((6 << 2 ^ 14) >> 4 | 0) ^ 9);
            if ((((1703845696 ^ 991384298) >>> 4 & 27889400) >>> 3 ^ 3486107) == 0) {
                  ;
            }

            GlStateManager.color(var10000, (float)(var10001 & (0 >>> 4 << 1 ^ 255)) / 255.0F, (float)(var0 & (((182 ^ 66) & 139) >>> 4 ^ 247)) / 255.0F, (float)(var0 >> ((5 & 3) >> 4 ^ 1350002064 ^ 1350002056) & ((238 ^ 53) << 1 >>> 1 ^ 36)) / 255.0F);
      }

      void renderNameTag(EntityLivingBase var1, String var2, double var3, double var5, double var7, boolean var9, int var10, int var11, String var12, int var13) {
            if (((838831819 >> 4 >>> 3 & 123963) >>> 2 ^ 30982) == 0) {
                  ;
            }

            if (!(var1 instanceof EntityArmorStand)) {
                  EntityPlayerSP var10001 = mc.player;
                  if (!"you're dogshit".equals("You're so fat whenever you go to the beach the tide comes in.")) {
                        ;
                  }

                  if (var1 != var10001) {
                        EntityPlayerSP var14 = mc.player;
                        FontRenderer var15 = mc.fontRenderer;
                        boolean var26 = var1.isSneaking();
                        if ((((733734372 | 611830959) << 2 | 555529480) ^ -111153718) != 0) {
                              ;
                        }

                        var5 += var26 ? 0.5D : 0.7D;
                        if (((158466080 << 2 ^ 38257344) >> 1 >>> 4 ^ 20741650) == 0) {
                              ;
                        }

                        float var10000 = var14.getDistance(var1) / 4.0F;
                        if (!"yo mama name maurice".equals("you probably spell youre as your")) {
                              ;
                        }

                        float var16 = var10000;
                        if ((137565568 >> 4 >>> 2 >> 1 ^ 1074731) == 0) {
                              ;
                        }

                        if (var16 < 1.6F) {
                              var16 = 1.6F;
                        }

                        int var17 = (int)var1.getHealth();
                        var2 = (new StringBuilder()).append(String.valueOf(var2)).append(" §c§l").append(Math.round((float)var17)).append("§c").toString();
                        RenderManager var18 = mc.getRenderManager();
                        if (((162730151 ^ 9283690 ^ 66340867 ^ 70284838 | 51351503) & 248934157 ^ 248909581) == 0) {
                              ;
                        }

                        float var19 = var16 / 30.0F;
                        var19 = (float)((double)var19 * 0.3D);
                        GL11.glPushMatrix();
                        GL11.glTranslatef((float)var3, (float)var5 + 1.4F, (float)var7);
                        GL11.glNormal3f(1.0F, 1.0F, 1.0F);
                        GL11.glRotatef(-var18.playerViewY, 0.0F, 1.0F, 0.0F);
                        GL11.glRotatef(var18.playerViewX, 1.0F, 0.0F, 0.0F);
                        if (!"your mom your dad the one you never had".equals("please dont crack my plugin")) {
                              ;
                        }

                        GL11.glScalef(-var19, -var19, var19);
                        GL11.glEnable(1090 << 2 << 3 << 1 ^ 72608);
                        if (!"stop skidding".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                              ;
                        }

                        GL11.glDisable((1607 & 1010) >> 1 >>> 1 ^ 3041);
                        GL11.glDisable((3512 ^ 2993) << 1 ^ 499);
                        int var24 = 2066 & 1470 & 11 ^ 2898;
                        if (((58784427 ^ 43499446 ^ 17730619 | 427969) ^ 1671486390) != 0) {
                              ;
                        }

                        GL11.glDisable(var24);
                        GL11.glDepthMask((boolean)(95568547 >> 4 >> 3 >>> 2 ^ 186657));
                        GL11.glBlendFunc(326 >>> 2 >> 4 >> 4 ^ 770, ((707 ^ 215) & 232 & 591881474) << 1 ^ 771);
                        GL11.glEnable((598 >> 3 ^ 45 ^ 60) >> 2 ^ 3060);
                        Tessellator var20 = Tessellator.getInstance();
                        BufferBuilder var21 = var20.getBuffer();
                        if ((1272290052 >>> 2 >> 3 ^ 1683287716) != 0) {
                              ;
                        }

                        int var22 = var15.getStringWidth(var2) / ((1 ^ 0 ^ 0 | 0) ^ 3);
                        GL11.glBlendFunc(270 & 3 & 1 ^ 770, 315 & 174 ^ 21 ^ 39 ^ 795);
                        double var10002;
                        Color var10005;
                        double var25;
                        int var28;
                        double var29;
                        if (var9) {
                              var25 = (double)(-var22 - ((0 | 365203230) >>> 1 & 138704603 ^ 17906027 ^ 156336098));
                              var28 = var15.FONT_HEIGHT;
                              if (!"ape covered in human flesh".equals("buy a domain and everything else you need at namecheap.com")) {
                                    ;
                              }

                              var29 = (double)(-(var28 + (5 >> 2 >> 3 & 2058721293 ^ 6)));
                              int var10003 = 0 << 1 >> 4 & 676194091 ^ 2;
                              if (!"ape covered in human flesh".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                                    ;
                              }

                              var10002 = (double)(var22 * var10003 + ((1 << 3 | 6) >> 4 >>> 4 ^ 3));
                              var10005 = new Color;
                              if (!"your mom your dad the one you never had".equals("ape covered in human flesh")) {
                                    ;
                              }

                              var10005.<init>((1478468996 ^ 1342458842) >> 1 ^ 68024111, (4198528 ^ 2665276 | 1157991) ^ 7977983, 13132396 >> 4 >> 2 & '첥' ^ 129, 2021445787 << 1 >>> 3 << 4 ^ -504151456);
                              Hud.drawRoundedRect(var25, var29, var10002, 15.0D, 6.0D, var10005.getRGB());
                        } else {
                              var25 = (double)(-var22 - (((1 & 0) << 4 << 2 | 858553266) ^ 858553264));
                              if (((151039186 ^ 150815824) >>> 2 >>> 4 ^ 1633822897) != 0) {
                                    ;
                              }

                              var28 = var15.FONT_HEIGHT + ((2 ^ 0) >> 1 << 1 ^ 4);
                              if (((481223871 | 151823179 | 62263453) >> 4 ^ 33288063) == 0) {
                                    ;
                              }

                              var29 = (double)(-var28);
                              var10002 = (double)(var22 * ((0 >>> 4 | 1221877075) ^ 1221877073) + ((0 | 1073409209 | 924035661) & 731487822 ^ 19581996 ^ 716391011));
                              var10005 = new Color;
                              if ((1891970933 >>> 3 >>> 2 >>> 1 >>> 2 ^ 7390511) == 0) {
                                    ;
                              }

                              var10005.<init>(1229031054 << 2 ^ 591482836 ^ 105338348, (1914530888 | 1393306303 | 1930177988) >>> 2 >>> 4 ^ 30176703, (395686949 | 126517662) >>> 4 << 1 ^ 49542838, ((34 ^ 8 ^ 24) >>> 3 & 0 | 694168574) ^ 694168484);
                              Hud.drawRoundedRect(var25, var29, var10002, 15.0D, 6.0D, var10005.getRGB());
                        }

                        if (!"You're so fat whenever you go to the beach the tide comes in.".equals("idiot")) {
                              ;
                        }

                        if (ModuleManager.getModuleByName("HitEffects").isToggled() && var9) {
                              if (this.random.nextBoolean()) {
                                    if (this.random.nextBoolean()) {
                                          var15.drawString(var12, (float)var10, (float)var11, var13, (boolean)((0 >>> 3 | 1687511994) >>> 3 << 2 & 333101831 ^ 306884869));
                                    } else {
                                          if (!"please go outside".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                                                ;
                                          }

                                          if (((2130476486 | 1548272582) << 4 ^ 1093469652) != 0) {
                                                ;
                                          }

                                          var15.drawString(var12, (float)var10, (float)var11, var13, (boolean)((0 ^ 726073821) >>> 4 ^ 45379612));
                                    }
                              } else if (this.random.nextBoolean()) {
                                    var15.drawString(var12, (float)var10, (float)var11, var13, (boolean)((0 & 618299328 | 379154794) << 2 >>> 3 ^ 189577396));
                              } else {
                                    if (((((1232368026 | 115651784) & 1073853838) >>> 2 >> 4 | 697949) ^ -423701268) != 0) {
                                          ;
                                    }

                                    float var30 = (float)var10;
                                    float var32 = (float)var11;
                                    if (((386693161 | 184852888) << 4 << 4 ^ 217692416) == 0) {
                                          ;
                                    }

                                    var15.drawString(var12, var30, var32, var13, (boolean)((0 | 336499463 | 68353122) >>> 4 ^ 21098487));
                              }
                        }

                        if (!var9) {
                              var15.drawString(var2, (float)(nameshower.getMiddle(-var22 - (((1 ^ 0) & 0 & 1226520380) >> 3 ^ 2), var22 + ((0 & 236941432) >>> 1 ^ 2)) - var22), (float)(-(var15.FONT_HEIGHT + ((1 ^ 0 ^ 0) << 1 ^ 0 ^ 0))), (new Color((68 ^ 19 | 56) ^ 128, ((55 & 52) >>> 4 | 1) >> 2 >>> 2 ^ 255, (84 >> 1 ^ 10 | 8 | 19) ^ 196, (359593485 >> 2 >> 4 & 406035) >>> 1 ^ 91049 ^ 228001)).getRGB(), (boolean)((0 | 112088838) << 1 ^ 224177677));
                        }

                        if (var1 instanceof EntityPlayer && !var9) {
                              EntityPlayer var27 = (EntityPlayer)var1;
                              if (!"You're so fat whenever you go to the beach the tide comes in.".equals("please go outside")) {
                                    ;
                              }

                              EntityPlayer var23 = var27;
                              if ((500529551 << 2 << 2 >> 3 ^ 155494525 ^ -219260061) == 0) {
                                    ;
                              }

                              GlStateManager.translate(0.0F, 1.1F, 0.0F);
                              if (((964839687 ^ 424043568) >>> 3 >> 4 ^ -1972918446) != 0) {
                                    ;
                              }

                              int var31 = (799192014 ^ 83351100) << 4 >>> 1 ^ 1521168272;
                              if (((671914050 ^ 26343164) << 4 ^ -1713968160) == 0) {
                                    ;
                              }

                              this.renderArmor(var23, var31, -(var15.FONT_HEIGHT + (0 >>> 1 << 3 ^ 435862333 ^ 435862332)) - (((1 ^ 0) >> 1 | 1019630574) & 963080519 ^ 944132434));
                              GlStateManager.translate(0.0F, -1.1F, 0.0F);
                        }

                        GL11.glDisable((1279 ^ 79) & 1118 ^ 4082);
                        GL11.glDepthMask((boolean)((0 ^ 1207060198 | 787845560) ^ 1128594256 ^ 749939887));
                        if (((643147838 | 3518548 | 557473632) >>> 4 << 1 ^ -140391087) != 0) {
                              ;
                        }

                        var24 = (3116 & 1806) >>> 3 >> 2 ^ 3521;
                        if ((696996917 >> 4 >> 1 >>> 2 ^ -483818196) != 0) {
                              ;
                        }

                        GL11.glEnable(var24);
                        GL11.glEnable((2846 << 2 >>> 4 | 2) & 81 ^ 2864);
                        GL11.glDisable((424 | 323) >>> 3 ^ 2845);
                        GL11.glPopMatrix();
                        if (((555859603 | 174428484) << 2 << 3 ^ 1823996640) == 0) {
                              ;
                        }

                        if (!"stringer is a good obfuscator".equals("You're so fat whenever you go to the beach the tide comes in.")) {
                              ;
                        }

                        return;
                  }
            }

      }

      public static class EnchantEntry {
            private String name;
            private Enchantment enchant;

            public String getName() {
                  return this.name;
            }

            public EnchantEntry(Enchantment var1, String var2) {
                  this.enchant = var1;
                  this.name = var2;
            }

            public Enchantment getEnchant() {
                  if (((661614225 | 521545648 | 865211467) ^ -1075257522) != 0) {
                        ;
                  }

                  return this.enchant;
            }
      }
}
