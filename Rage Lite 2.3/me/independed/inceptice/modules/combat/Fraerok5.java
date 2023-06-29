//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.combat;

import java.awt.Color;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
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
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import org.lwjgl.opengl.GL11;

public class Fraerok5 extends Module {
      public BooleanSetting invisibleAttack;
      public NumberSetting speedRot;
      public static int direction = 1877284057 << 4 << 3 >> 3 ^ 28226159;
      public BooleanSetting Increasable;
      public NumberSetting distance;
      public BooleanSetting renderCircle;
      public NumberSetting Increase;
      public double increment;
      public boolean sideDirection;
      public TimerUtil timerUtil;

      public boolean attackCheck(Entity var1) {
            if (var1 instanceof EntityPlayer) {
                  Iterator var2 = FriendManager.friends.iterator();

                  Friend var3;
                  do {
                        if (!var2.hasNext()) {
                              if (var1.isInvisible() && this.invisibleAttack.isEnabled()) {
                                    return (boolean)(0 << 2 & 1559881175 ^ 1);
                              }

                              if (var1.isInvisible() && !this.invisibleAttack.isEnabled()) {
                                    return (boolean)(((1818493770 ^ 257623056) & 890950281) << 1 ^ 1110705168);
                              }

                              return (boolean)(0 >>> 2 << 2 >> 4 >> 3 ^ 1);
                        }

                        var3 = (Friend)var2.next();
                        if (!"stringer is a good obfuscator".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                              ;
                        }
                  } while(!var1.getName().equalsIgnoreCase(var3.name));

                  if ((975409795 << 3 << 3 << 1 ^ 298402176) == 0) {
                        ;
                  }

                  return (boolean)(3264 >> 4 >>> 4 & 9 ^ 8);
            } else {
                  if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("ape covered in human flesh")) {
                        ;
                  }

                  return (boolean)((4383290 | 994066) & 3734279 ^ 584450);
            }
      }

      private void drawRadius(Entity var1, double var2, double var4, double var6, double var8) {
            float var10 = 90.0F;
            GlStateManager.enableDepth();
            double var11 = 0.0D;
            if ((167777047 >>> 4 ^ 1207131574) != 0) {
                  ;
            }

            while(true) {
                  if ((1067323890 << 4 & 1469829010 ^ 1367412480) == 0) {
                        ;
                  }

                  if (var11 >= 0.01D) {
                        return;
                  }

                  GL11.glPushMatrix();
                  if (!"you probably spell youre as your".equals("intentMoment")) {
                        ;
                  }

                  GL11.glDisable((459 >>> 1 | 79) ^ 3342);
                  GL11.glEnable(1964 >>> 1 & 604 ^ 2420);
                  GL11.glEnable((((2113 ^ 2016) & 3110) >>> 1 | 215) >> 2 ^ 2804);
                  GL11.glEnable(((1933 | 1359) >> 2 ^ 338) & 133 & 120 ^ 2832);
                  GL11.glEnable((2079 ^ 652) << 1 >>> 3 ^ 2374);
                  GL11.glDepthMask((boolean)((1056899202 | 736764427) << 3 ^ -453544));
                  GL11.glBlendFunc((334 & 162) >> 2 ^ 770, ((124 | 18) ^ 73) << 1 >> 4 ^ 773);
                  GL11.glHint((1056 & 245) >>> 1 & 13 ^ 3154, (1478 >>> 3 ^ 92) << 3 & 893 ^ 643 ^ 4257);
                  int var10000 = (341 & 308 ^ 203) & 156 ^ 3279;
                  if ((174959366 >> 3 >>> 1 ^ 10934960) == 0) {
                        ;
                  }

                  GL11.glHint(var10000, 3032 << 2 >>> 1 ^ 1714);
                  if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("ape covered in human flesh")) {
                        ;
                  }

                  GL11.glHint(2175 & 881 ^ 58 ^ 3098, (2198 | 418) >>> 1 ^ 550 ^ 6143);
                  if ((2025449180 << 1 >> 1 & 2091630390 ^ -2050586679) != 0) {
                        ;
                  }

                  GL11.glDisable(1698 << 1 >>> 3 >>> 1 ^ 2981);
                  GL11.glLineWidth(4.0F);
                  if ((((441917828 | 228288044 | 350101653) & 289179752) >>> 3 ^ 35880965) == 0) {
                        ;
                  }

                  GL11.glBegin((0 << 2 | 1584869642) << 4 ^ -411889501);
                  double var13 = 6.283185307179586D;
                  if ((154146568 >>> 4 ^ 9634160) == 0) {
                        ;
                  }

                  float var15 = 1000.0F;
                  long var23 = System.currentTimeMillis();
                  int var10001 = (int)var15;
                  if (!"please dont crack my plugin".equals("please dont crack my plugin")) {
                        ;
                  }

                  float var24 = (float)(var23 % (long)var10001);
                  if ((((1701378166 | 508403170) >> 3 | 111259383) << 1 ^ 534478846) == 0) {
                        ;
                  }

                  float var16;
                  for(var16 = var24; var16 > var15; var16 -= var15) {
                  }

                  var16 /= var15;
                  int var17 = (1739388575 >> 1 ^ 24894234 ^ 198527463) << 3 ^ -876130928;

                  while(true) {
                        if (!"idiot".equals("nefariousMoment")) {
                              ;
                        }

                        if (var17 > ((21 ^ 19) >> 4 ^ 90)) {
                              GL11.glEnd();
                              GL11.glDepthMask((boolean)((0 & 1864104637 | 172047251) << 3 ^ 1376378009));
                              var10000 = (1935 << 1 & 2535 & 1337) >>> 4 << 1 ^ 2897;
                              if (((5712667 ^ 583146) >>> 4 >> 4 ^ -697211996) != 0) {
                                    ;
                              }

                              GL11.glEnable(var10000);
                              GL11.glDisable((1912 & 1012) << 1 ^ 3520);
                              GL11.glDisable((209 & 161) << 3 ^ 3913);
                              GL11.glEnable((2043 ^ 854) >> 3 >> 4 ^ 5 ^ 2844);
                              var10000 = (2989 >> 4 ^ 55 | 110) ^ 3342;
                              if (((490955257 | 351947548) & 7120347 ^ 6824409) == 0) {
                                    ;
                              }

                              GL11.glEnable(var10000);
                              GL11.glPopMatrix();
                              GlStateManager.color(255.0F, 255.0F, 255.0F);
                              var11 += 0.001D;
                              break;
                        }

                        var24 = (float)var17;
                        if (((587924351 << 2 | 701702270) >> 3 ^ -1011451126) != 0) {
                              ;
                        }

                        var24 += (float)(var11 * 8.0D);
                        if (((144091117 | 135394392) >> 2 >> 4 ^ 402978 ^ 167462280) != 0) {
                              ;
                        }

                        float var18 = var24 / var10;
                        float var19 = var18 + var16;

                        while(true) {
                              if ((1047422800 >> 2 >>> 4 << 1 >>> 1 ^ 1776182246) != 0) {
                                    ;
                              }

                              if (var19 <= 1.0F) {
                                    Color var25 = new Color;
                                    if (((1596573413 >> 3 ^ 49880099) & 103647591 ^ 786535) == 0) {
                                          ;
                                    }

                                    int var10003 = Color.HSBtoRGB(var19, 0.75F, 1.0F);
                                    if (!"idiot".equals("yo mama name maurice")) {
                                          ;
                                    }

                                    var25.<init>(var10003);
                                    float var26 = (float)var25.getRed();
                                    if ((1031871846 >> 2 >>> 3 ^ 2064282330) != 0) {
                                          ;
                                    }

                                    float var20 = 0.003921569F * var26;
                                    if (((428971629 << 1 << 3 ^ 1353040062) & 1305368914 ^ 460548972) != 0) {
                                          ;
                                    }

                                    var24 = 0.003921569F * (float)(new Color(Color.HSBtoRGB(var19, 0.75F, 1.0F))).getGreen();
                                    if ((((1695212687 ^ 625606064) & 210439912 | '\uf48f') << 4 ^ 4180720) == 0) {
                                          ;
                                    }

                                    float var21 = var24;
                                    if (!"nefariousMoment".equals("stop skidding")) {
                                          ;
                                    }

                                    float var22 = 0.003921569F * (float)(new Color(Color.HSBtoRGB(var19, 0.75F, 1.0F))).getBlue();
                                    if (((797851470 | 372700659) >>> 1 ^ 214962713 ^ -2072219071) != 0) {
                                          ;
                                    }

                                    if (!"your mom your dad the one you never had".equals("You're so fat whenever you go to the beach the tide comes in.")) {
                                          ;
                                    }

                                    GL11.glColor3f(var20, var21, var22);
                                    double var10002 = (double)var17;
                                    if ((554238427 << 3 >>> 2 >> 4 << 1 ^ -625189302) != 0) {
                                          ;
                                    }

                                    double var27 = var2 + var8 * Math.cos(var10002 * 6.283185307179586D / (double)var10);
                                    if (!"please take a shower".equals("minecraft")) {
                                          ;
                                    }

                                    double var28 = var4 + var11;
                                    double var10004 = (double)var17 * 6.283185307179586D;
                                    if (((491149930 ^ 145191970) >> 2 >> 1 ^ 45884873) == 0) {
                                          ;
                                    }

                                    GL11.glVertex3d(var27, var28, var6 + var8 * Math.sin(var10004 / (double)var10));
                                    ++var17;
                                    break;
                              }

                              --var19;
                        }
                  }
            }
      }

      public boolean checkArmor(Entity var1) {
            if (var1 instanceof EntityPlayer) {
                  List var2 = (List)var1.getArmorInventoryList();
                  boolean var10000 = ((ItemStack)var2.get(1524791041 >> 1 >>> 2 << 2 >>> 1 ^ 381197760)).getItem().equals(Items.DIAMOND_BOOTS);
                  if (!"please take a shower".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                        ;
                  }

                  if (!var10000) {
                        return (boolean)((1878314571 ^ 363775812 | 70583749) ^ 2122255823);
                  } else if (!((ItemStack)var2.get((0 ^ 405161426) >>> 4 >>> 3 ^ 3165322)).getItem().equals(Items.DIAMOND_LEGGINGS)) {
                        return (boolean)((1222690579 | 809737196) << 4 ^ -1908539408);
                  } else {
                        if (!"buy a domain and everything else you need at namecheap.com".equals("please dont crack my plugin")) {
                              ;
                        }

                        Item var3 = ((ItemStack)var2.get((0 >> 1 >> 2 >> 2 | 1840540530) << 1 ^ -613886234)).getItem();
                        ItemArmor var10001 = Items.DIAMOND_CHESTPLATE;
                        if (((((1309340905 >> 2 ^ 215886996) & 332197921) >>> 2 | 27370282) ^ 99854122) == 0) {
                              ;
                        }

                        if (!var3.equals(var10001)) {
                              return (boolean)((((435493586 | 263051750) << 1 | 577970529) ^ 8231364 | 717948899) ^ 1070542827);
                        } else {
                              return (boolean)(!((ItemStack)var2.get((2 << 4 | 12) ^ 28 ^ 51)).getItem().equals(Items.DIAMOND_HELMET) ? ((59320950 ^ 47388405) & 10279895 | 346613) ^ 1923575 : (0 >> 4 ^ 101619539) & 52359550 ^ 34509139);
                        }
                  }
            } else {
                  return (boolean)((601366318 >> 4 & 13656170) << 2 << 4 ^ 67115136);
            }
      }

      public static void setSpeed(double var0, float var2, double var3, double var5) {
            double var7 = var5;
            double var9 = var3;
            if (((626495237 ^ 62732231) >> 2 >> 2 ^ 40807308) == 0) {
                  ;
            }

            float var11 = var2;
            if (var5 == 0.0D && var3 == 0.0D) {
                  mc.player.motionZ = 0.0D;
                  if ((((1336850686 ^ 740254805) >>> 1 ^ 29347507 | 187259113) ^ 997161199) == 0) {
                        ;
                  }

                  mc.player.motionX = 0.0D;
                  if (!"You're so fat whenever you go to the beach the tide comes in.".equals("please dont crack my plugin")) {
                        ;
                  }
            } else {
                  if (var5 != 0.0D) {
                        if (var3 > 0.0D) {
                              int var10001;
                              if (var5 > 0.0D) {
                                    var10001 = 18738 ^ -18719;
                              } else {
                                    if ((243628673 << 1 << 4 >> 2 ^ 470269803) != 0) {
                                          ;
                                    }

                                    var10001 = ((32 ^ 27) & 11 | 9) ^ 38;
                              }

                              var11 = var2 + (float)var10001;
                        } else if (var3 < 0.0D) {
                              if ((((133905289 ^ 24186760) & 38796446) >> 3 ^ 471422023) != 0) {
                                    ;
                              }

                              var11 = var2 + (float)(var5 > 0.0D ? ((30 ^ 26) >> 3 | 177735368) ^ 177735397 : (1203314590 << 2 | 445460882 | 361309521) ^ -535658456);
                        }

                        var9 = 0.0D;
                        if (var5 > 0.0D) {
                              if (!"yo mama name maurice".equals("shitted on you harder than archybot")) {
                                    ;
                              }

                              if (((41013243 << 4 & 233331453) >>> 4 ^ 42567943) != 0) {
                                    ;
                              }

                              var7 = 1.0D;
                        } else if (var5 < 0.0D) {
                              if (((563795793 ^ 495212986 | 241750708) >>> 4 ^ 65531375) == 0) {
                                    ;
                              }

                              var7 = -1.0D;
                        }
                  }

                  double var12 = Math.cos(Math.toRadians((double)(var11 + 90.0F)));
                  double var14 = Math.sin(Math.toRadians((double)(var11 + 90.0F)));
                  if (!"idiot".equals("stringer is a good obfuscator")) {
                        ;
                  }

                  mc.player.motionX = var7 * var0 * var12 + var9 * var0 * var14;
                  if ((1844146723 >>> 2 & 34840530 ^ 34774144) == 0) {
                        ;
                  }

                  EntityPlayerSP var10000 = mc.player;
                  if ((1664715955 >> 2 >>> 3 & 16436276 ^ -220888501) != 0) {
                        ;
                  }

                  double var16 = var7 * var0;
                  if (!"your mom your dad the one you never had".equals("nefariousMoment")) {
                        ;
                  }

                  var16 *= var14;
                  double var10002 = var9 * var0;
                  if ((1456164261 << 4 ^ 367637917 ^ 2036166605) == 0) {
                        ;
                  }

                  var10000.motionZ = var16 - var10002 * var12;
            }

      }

      public static float[] getNeededRotations(Entity var0) {
            double var10000 = var0.posX;
            if (((262813930 >>> 1 << 2 | 412073883) ^ 534773727) == 0) {
                  ;
            }

            double var1 = var10000 - Minecraft.getMinecraft().player.posX;
            var10000 = var0.posZ;
            if (!"idiot".equals("you probably spell youre as your")) {
                  ;
            }

            double var3 = var10000 - Minecraft.getMinecraft().player.posZ;
            var10000 = var0.posY + (double)var0.getEyeHeight();
            if (((668530021 >> 2 | 74925171) << 1 >> 1 >>> 4 ^ 14645207) == 0) {
                  ;
            }

            double var5 = var10000 - (Minecraft.getMinecraft().player.getEntityBoundingBox().minY + (Minecraft.getMinecraft().player.getEntityBoundingBox().maxY - Minecraft.getMinecraft().player.getEntityBoundingBox().minY));
            if ((((526098162 ^ 432707589) >> 2 >>> 1 & 9723460) << 3 ^ 75498016) == 0) {
                  ;
            }

            var10000 = var1 * var1;
            if (!"please dont crack my plugin".equals("please go outside")) {
                  ;
            }

            var10000 += var3 * var3;
            if (!"yo mama name maurice".equals("ape covered in human flesh")) {
                  ;
            }

            float var11 = MathHelper.sqrt(var10000);
            if (((67503125 | 39802123) >> 4 << 3 ^ -701812040) != 0) {
                  ;
            }

            double var7 = (double)var11;
            if (!"shitted on you harder than archybot".equals("please go outside")) {
                  ;
            }

            var11 = (float)(MathHelper.atan2(var3, var1) * 180.0D / 3.141592653589793D);
            if (((687946752 >>> 4 | 37745575) ^ 1956107925) != 0) {
                  ;
            }

            float var9 = var11 - 90.0F;
            float var10 = (float)(-(MathHelper.atan2(var5, var7) * 180.0D / 3.141592653589793D));
            float[] var12 = new float[((0 & 128722224 | 1322343997) & 565970615 & 5919059 | 657521) ^ 1706099];
            var12[281149569 >>> 3 ^ 35143696] = var9;
            int var10002 = (0 & 723848480) >> 2 ^ 1182751907 ^ 1182751906;
            if ((265307224 << 4 >> 3 << 2 ^ 1370253399 ^ -1355920745) == 0) {
                  ;
            }

            var12[var10002] = var10;
            return var12;
      }

      public List sortByHealth(List var1) {
            if ((631832843 << 3 >>> 3 >>> 4 ^ 1770524 ^ -1661839134) != 0) {
                  ;
            }

            int var2 = var1.size();

            for(int var3 = 1824666526 << 4 >>> 4 >> 2 >>> 4 ^ 3344590; var3 < var2; ++var3) {
                  if (((301016302 >>> 3 >>> 3 ^ 1704863) >>> 3 ^ -1372905480) != 0) {
                        ;
                  }

                  if ((((982098823 ^ 924486794) & 70825447) << 2 >> 4 ^ 17041729) == 0) {
                        ;
                  }

                  int var10000 = var3 + (0 >> 3 ^ 1789982652 ^ 1789982653);
                  if (!"buy a domain and everything else you need at namecheap.com".equals("minecraft")) {
                        ;
                  }

                  for(int var4 = var10000; var4 < var2; ++var4) {
                        float var6 = ((EntityLivingBase)var1.get(var3)).getHealth();
                        if (((357984900 << 1 ^ 405990761) >> 4 ^ 722394534) != 0) {
                              ;
                        }

                        if (var6 > ((EntityLivingBase)var1.get(var4)).getHealth()) {
                              if (!"stop skidding".equals("buy a domain and everything else you need at namecheap.com")) {
                                    ;
                              }

                              Entity var5 = (Entity)var1.get(var3);
                              if (((1486624322 ^ 1368453981) >> 1 ^ 75920015) == 0) {
                                    ;
                              }

                              var1.set(var3, var1.get(var4));
                              if ((607001377 << 2 << 4 & 44926821 ^ 901796015) != 0) {
                                    ;
                              }

                              var1.set(var4, var5);
                        }

                        if ((98772387 >>> 1 >> 3 ^ 469549301) != 0) {
                              ;
                        }
                  }
            }

            if (!"i hope you catch fire ngl".equals("please go outside")) {
                  ;
            }

            return var1;
      }

      public void onRenderWorldLast(float var1) {
            if (mc.player != null && !mc.player.isDead) {
                  double var10000 = (double)mc.player.getHealth();
                  if ((1129465511 >>> 2 >> 2 ^ 70591594) == 0) {
                        ;
                  }

                  if (var10000 > 0.0D) {
                        if (this.renderCircle.isEnabled() && Fraerok2.currentTarget != null && Fraerok2.currentTarget.getHealth() > 0.0F) {
                              double var2 = this.distance.getValue();
                              if (!"minecraft".equals("stringer is a good obfuscator")) {
                                    ;
                              }

                              EntityLivingBase var4 = Fraerok2.currentTarget;
                              if (!"i hope you catch fire ngl".equals("You're so fat whenever you go to the beach the tide comes in.")) {
                                    ;
                              }

                              if (!"i hope you catch fire ngl".equals("you probably spell youre as your")) {
                                    ;
                              }

                              double var5 = var4.lastTickPosX + (var4.posX - var4.lastTickPosX) * (double)var1 - mc.getRenderManager().viewerPosX;
                              var10000 = var4.lastTickPosY;
                              double var10001 = var4.posY;
                              if (!"you're dogshit".equals("idiot")) {
                                    ;
                              }

                              if (!"Some babies were dropped on their heads but you were clearly thrown at a wall.".equals("your mom your dad the one you never had")) {
                                    ;
                              }

                              double var7 = var10000 + (var10001 - var4.lastTickPosY) * (double)var1 - mc.getRenderManager().viewerPosY;
                              var10000 = var4.lastTickPosZ;
                              var10001 = var4.posZ;
                              if (((1537348146 | 693933535) ^ 588305829 ^ 658292882 ^ 2144502472) == 0) {
                                    ;
                              }

                              var10001 -= var4.lastTickPosZ;
                              if (!"buy a domain and everything else you need at namecheap.com".equals("shitted on you harder than archybot")) {
                                    ;
                              }

                              double var9 = var10000 + var10001 * (double)var1 - mc.getRenderManager().viewerPosZ;
                              if (this.increment < 2.03D && this.sideDirection) {
                                    if (this.increment >= 2.0D) {
                                          this.sideDirection = (boolean)((268632064 ^ 211953098) << 4 ^ -904766304);
                                          if (((19431500 >>> 2 | 4366958) << 2 >>> 4 ^ -1885464891) != 0) {
                                                ;
                                          }

                                          this.increment = 2.0D;
                                          if ((((595977089 | 148669001) & 333568028) >>> 3 >> 2 ^ 1969664) == 0) {
                                                ;
                                          }

                                          var7 -= this.increment;
                                    }

                                    var10000 = var7 + this.increment;
                                    if ((750027944 << 2 ^ 737584600 ^ -661951937) != 0) {
                                          ;
                                    }

                                    var7 = var10000;
                                    if (!"ape covered in human flesh".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                                          ;
                                    }

                                    if (((813213782 >>> 2 << 3 | 629702957) ^ 1710870957) == 0) {
                                          ;
                                    }

                                    this.increment += 0.02D;
                                    this.drawRadius(Fraerok2.currentTarget, var5, var7, var9, var2);
                              }

                              if ((1078817 ^ 986348 ^ -1752909946) != 0) {
                                    ;
                              }

                              var10000 = this.increment;
                              if (((50371169 | 9483371) >> 3 ^ 1269321745) != 0) {
                                    ;
                              }

                              if (var10000 > 0.01D && !this.sideDirection) {
                                    if (this.increment <= 0.02D) {
                                          this.sideDirection = (boolean)((0 | 1590669719 | 642380416) ^ 2127557526);
                                          this.increment = 0.01D;
                                    }

                                    var7 += this.increment;
                                    this.increment -= 0.02D;
                                    EntityLivingBase var11 = Fraerok2.currentTarget;
                                    if (((1481627507 | 701366403) >>> 4 >> 3 ^ 15966127) == 0) {
                                          ;
                                    }

                                    this.drawRadius(var11, var5, var7, var9, var2);
                              }
                        }

                        return;
                  }
            }

            if (((1963245908 | 416238593 | 1444717211) ^ 2145382367) == 0) {
                  ;
            }

      }

      public List sortByDistance(List var1) {
            int var10000 = var1.size();
            if (!"You're so fat whenever you go to the beach the tide comes in.".equals("stop skidding")) {
                  ;
            }

            int var2 = var10000;

            for(int var3 = 204472480 ^ 77373908 ^ 79970982 ^ 208200658; var3 < var2; ++var3) {
                  int var10001 = 0 >> 1 ^ 1617573027 ^ 1617573026;
                  if ((419431104 >>> 3 >> 1 ^ 10106004 ^ 1599911724) != 0) {
                        ;
                  }

                  for(int var4 = var3 + var10001; var4 < var2; ++var4) {
                        EntityPlayerSP var6 = mc.player;
                        Object var8 = var1.get(var3);
                        if (!"minecraft".equals("buy a domain and everything else you need at namecheap.com")) {
                              ;
                        }

                        float var7 = var6.getDistance((Entity)var8);
                        if (!"shitted on you harder than archybot".equals("you're dogshit")) {
                              ;
                        }

                        EntityPlayerSP var9 = mc.player;
                        if ((12615937 >>> 2 ^ 427371318) != 0) {
                              ;
                        }

                        if (var7 > var9.getDistance((Entity)var1.get(var4))) {
                              Entity var5 = (Entity)var1.get(var3);
                              if (!"please go outside".equals("please take a shower")) {
                                    ;
                              }

                              var1.set(var3, var1.get(var4));
                              var1.set(var4, var5);
                              if ((((605938797 ^ 569497010 | 15524520) >> 2 | 10811713) ^ 33553919) == 0) {
                                    ;
                              }
                        }

                        if (!"intentMoment".equals("stop skidding")) {
                              ;
                        }
                  }
            }

            return var1;
      }

      public static boolean canStrafe() {
            return (boolean)((0 ^ 1774902276) & 1689250945 & 1091899716 ^ 1073741825);
      }

      public Fraerok5() {
            super("TargetStrafe", "targets closest player. note: use with kill_aura", 1502970442 << 4 >> 3 ^ -215284588, Module.Category.COMBAT);
            NumberSetting var10001 = new NumberSetting;
            if (!"you're dogshit".equals("buy a domain and everything else you need at namecheap.com")) {
                  ;
            }

            var10001.<init>("Radius", this, 2.0D, 0.0D, 20.0D, 0.1D);
            this.distance = var10001;
            var10001 = new NumberSetting;
            if (((1775431212 | 737486622) >>> 1 ^ -2011753939) != 0) {
                  ;
            }

            var10001.<init>("Speed", this, 4.0D, 1.0D, 100.0D, 1.0D);
            this.speedRot = var10001;
            if (!"idiot".equals("stop skidding")) {
                  ;
            }

            this.renderCircle = new BooleanSetting("Render", this, (boolean)((0 ^ 2063399064) >> 2 << 1 ^ 1031699533));
            this.Increase = new NumberSetting("Increase", this, 50.0D, 0.0D, 150.0D, 1.0D);
            this.Increasable = new BooleanSetting("Increasing", this, (boolean)(0 >> 4 << 3 >>> 2 ^ 1));
            if ((291236117 >>> 1 >>> 2 >> 4 ^ 2275282) == 0) {
                  ;
            }

            BooleanSetting var1 = new BooleanSetting;
            int var10005 = (2135732265 >> 1 ^ 1039583149) << 3 & 309486689 ^ 302015552;
            if (((885793 ^ 702962) << 1 ^ 949158) == 0) {
                  ;
            }

            var1.<init>("Invisible", this, (boolean)var10005);
            this.invisibleAttack = var1;
            if (((1423393997 ^ 389978582) >> 3 ^ 67048547 ^ 193081280) == 0) {
                  ;
            }

            this.timerUtil = new TimerUtil();
            this.increment = 0.05D;
            this.sideDirection = (boolean)(0 ^ 1313662469 ^ 117856887 ^ 1229634675);
            if ((((338224708 | 335095702) << 2 ^ 745408848) & 1533361950 ^ 1392580104) == 0) {
                  ;
            }

            Setting[] var2 = new Setting[2 >> 1 ^ 0 ^ 7];
            int var10003 = (1636263913 >> 4 | 52321625) ^ 119439231;
            if (!"stop skidding".equals("i hope you catch fire ngl")) {
                  ;
            }

            var2[var10003] = this.renderCircle;
            var2[(0 & 36325073) << 1 ^ 1] = this.speedRot;
            if (!"stringer is a good obfuscator".equals("You're so fat whenever you go to the beach the tide comes in.")) {
                  ;
            }

            var2[0 << 4 & 142891750 ^ 2] = this.distance;
            var2[(1 ^ 0) >>> 2 << 3 ^ 3] = this.Increasable;
            if ((19885288 >>> 3 >>> 1 ^ 1242830) == 0) {
                  ;
            }

            var2[(2 >> 4 ^ 1953260169) >>> 2 ^ 488315046] = this.Increase;
            var2[(2 ^ 1) & 0 ^ 5] = this.invisibleAttack;
            this.addSettings(var2);
            if (((437474768 >> 3 << 1 | 82269810) ^ -378864091) != 0) {
                  ;
            }

      }

      public final boolean doStrafeAtSpeed(double var1) {
            boolean var3 = Fraerok5.canStrafe();
            if (var3) {
                  Stream var10000 = mc.world.loadedEntityList.stream().filter((var0) -> {
                        if ((2012025408 >>> 3 >>> 4 ^ 15718948) == 0) {
                              ;
                        }

                        return (boolean)(var0 != mc.player ? 0 & 538353284 ^ 261222178 ^ 261222179 : 1605969 ^ 821640 ^ 1312985);
                  }).filter((var0) -> {
                        if ((949397048 >>> 1 >> 2 ^ 118674631) == 0) {
                              ;
                        }

                        return (boolean)(!var0.isDead ? (0 & 99985029) >> 2 ^ 1111222869 ^ 1111222868 : 5391417 << 4 ^ 86262672);
                  }).filter((var1x) -> {
                        return this.attackCheck(var1x);
                  });
                  if (!"You're so fat whenever you go to the beach the tide comes in.".equals("minecraft")) {
                        ;
                  }

                  List var4 = (List)var10000.filter((var0) -> {
                        return var0 instanceof EntityPlayer;
                  }).filter((var0) -> {
                        return (boolean)(var0.ticksExisted > ((25 ^ 8 ^ 5 ^ 7) << 2 ^ 82) ? (0 | 366365718 | 200743003 | 303886014) >>> 4 ^ 33554414 : ((1897145674 >>> 2 ^ 293816379) & 224508187 ^ 30781099) << 3 ^ 1689091344);
                  }).filter((var1x) -> {
                        EntityPlayerSP var10000 = mc.player;
                        if (((153871334 << 2 ^ 539437049) & 20612718 ^ 678120390) != 0) {
                              ;
                        }

                        double var2 = (double)var10000.getDistance(var1x);
                        if (((2039991009 ^ 1206427900) >>> 2 ^ -1361906716) != 0) {
                              ;
                        }

                        return (boolean)(var2 <= this.distance.getValue() + 8.0D ? (0 << 4 ^ 1783006105) >> 1 ^ 891503053 : (1485079087 ^ 19256755 ^ 461290915) & 1041315706 ^ 34677306);
                  }).filter((var1x) -> {
                        return (boolean)(Fraerok2.checkArmorita && !this.checkArmor(var1x) ? 1673168455 << 4 << 3 << 1 << 3 ^ -734906368 : 0 >>> 4 >> 3 ^ 1);
                  }).collect(Collectors.toList());
                  EntityPlayer var5 = null;
                  if (!var4.isEmpty()) {
                        if (Fraerok2.modeSorted.equalsIgnoreCase("LivingTime")) {
                              var4 = this.sortByLivingTime(var4);
                        } else {
                              String var8 = Fraerok2.modeSorted;
                              if (((1913810926 >>> 3 ^ 21427144) >>> 4 ^ 15748067) == 0) {
                                    ;
                              }

                              if (var8.equalsIgnoreCase("Distance")) {
                                    if (((1352309529 ^ 1327064208) & 523926131 ^ 520256001) == 0) {
                                          ;
                                    }

                                    if (!"please get a girlfriend and stop cracking plugins".equals("buy a domain and everything else you need at namecheap.com")) {
                                          ;
                                    }

                                    var4 = this.sortByDistance(var4);
                              } else {
                                    var4 = this.sortByHealth(var4);
                              }
                        }

                        var5 = (EntityPlayer)var4.get((731280075 << 1 ^ 964664566) << 2 ^ -1186077312);
                  }

                  if (((1114768689 >>> 3 >>> 2 << 4 | 378248490) ^ 935043002) == 0) {
                        ;
                  }

                  if (var5 != null) {
                        if (((1486511674 >> 2 | 159275069) ^ -151893121) != 0) {
                              ;
                        }

                        if (((1738629814 >>> 3 >>> 2 & 15896074 | 1930470) << 3 ^ 32241456) == 0) {
                              ;
                        }

                        if (var5.getHealth() > 0.0F && !var5.isDead) {
                              if ((((1088178840 | 1060311721) >> 2 | 277574671) ^ 536868527) == 0) {
                                    ;
                              }

                              float var16;
                              int var9 = (var16 = mc.player.getDistance(var5) - 7.0F) == 0.0F ? 0 : (var16 < 0.0F ? -1 : 1);
                              if (!"please go outside".equals("your mom your dad the one you never had")) {
                                    ;
                              }

                              if (var9 < 0) {
                                    Minecraft var10 = mc;
                                    if (((483664916 << 4 | 352973490) ^ -1301594874) != 0) {
                                          ;
                                    }

                                    boolean var11 = var10.player.onGround;
                                    if (((487883264 | 182039535 | 75950114) ^ 1058854867) != 0) {
                                          ;
                                    }

                                    if (var11) {
                                          mc.player.jump();
                                    }

                                    AxisAlignedBB var6 = var5.getEntityBoundingBox();
                                    float[] var7 = (float[])Fraerok5.getNeededRotations(var5);
                                    double var13 = (double)Minecraft.getMinecraft().player.getDistance(var5);
                                    NumberSetting var10001 = this.distance;
                                    if ((((1095772882 ^ 28142603) >> 4 & 34543304) >> 2 >> 1 ^ -234524352) != 0) {
                                          ;
                                    }

                                    float var12;
                                    EntityPlayerSP var14;
                                    if (var13 <= var10001.getValue()) {
                                          mc.player.renderYawOffset = var7[1424062791 << 3 >>> 2 >>> 3 >>> 1 ^ 43790120];
                                          mc.player.rotationYawHead = var7[(1826926055 | 840791689) & 2044567796 ^ 2027790564];
                                          if (!"your mom your dad the one you never had".equals("intentMoment")) {
                                                ;
                                          }

                                          var14 = mc.player;
                                          if (!"yo mama name maurice".equals("nefariousMoment")) {
                                                ;
                                          }

                                          if (var14.hurtTime > 0 && this.Increasable.isEnabled()) {
                                                Fraerok5.setSpeed(var1 - (0.1D - (this.speedRot.getValue() + this.Increase.getValue()) / 100.0D), var7[(1410723696 | 630682856) & 84842546 ^ 84318256], (double)direction, 0.0D);
                                          } else {
                                                if (!"your mom your dad the one you never had".equals("please get a girlfriend and stop cracking plugins")) {
                                                      ;
                                                }

                                                var13 = var1 - (0.2D - this.speedRot.getValue() / 100.0D);
                                                var12 = var7[(136921136 | 129499447) ^ 263799095];
                                                if (!"Some babies were dropped on their heads but you were clearly thrown at a wall.".equals("please dont crack my plugin")) {
                                                      ;
                                                }

                                                Fraerok5.setSpeed(var13, var12, (double)direction, 0.0D);
                                          }
                                    } else {
                                          if (!"buy a domain and everything else you need at namecheap.com".equals("please get a girlfriend and stop cracking plugins")) {
                                                ;
                                          }

                                          if (mc.player.hurtTime > 0 && this.Increasable.isEnabled()) {
                                                if (((53872144 | 25746691 | 6227386 | 22681986) ^ 67100603) == 0) {
                                                      ;
                                                }

                                                var13 = var1 - (0.1D - (this.speedRot.getValue() + this.Increase.getValue()) / 100.0D);
                                                if (!"please go outside".equals("intentMoment")) {
                                                      ;
                                                }

                                                Fraerok5.setSpeed(var13, var7[706800411 >> 2 >> 2 ^ 44175025], (double)direction, 1.0D);
                                          } else {
                                                var13 = var1 - (0.1D - this.speedRot.getValue() / 100.0D);
                                                var12 = var7[(686942552 << 4 >> 3 | 1320979684) ^ -8460];
                                                if (((((1029496806 ^ 736059118) & 207745320 & 55931895) << 2 | '\ud8da') ^ 1807600559) != 0) {
                                                      ;
                                                }

                                                double var10002 = (double)direction;
                                                if ((((523746013 >> 3 | 3594037) ^ 23434825) >>> 1 ^ 21606555) == 0) {
                                                      ;
                                                }

                                                Fraerok5.setSpeed(var13, var12, var10002, 1.0D);
                                          }

                                          var14 = mc.player;
                                          if (((((1220230757 | 1023225654) ^ 1780109715 | 81168894) & 269295908) >>> 2 ^ 67192905) == 0) {
                                                ;
                                          }

                                          int var15 = (1628343046 >> 4 ^ 61564007 ^ 80344659) & 7833492 ^ 7471748;
                                          if (((2022398182 >> 3 | 175774882 | 166902190) ^ 268173246) == 0) {
                                                ;
                                          }

                                          var14.renderYawOffset = var7[var15];
                                          var10 = mc;
                                          if (((523738414 >> 4 & 27138380 | 13136784) ^ 16857978 ^ 14368938) == 0) {
                                                ;
                                          }

                                          var10.player.rotationYawHead = var7[1565805008 << 3 >> 2 >>> 3 ^ 525668980];
                                    }
                              }
                        }
                  }
            }

            return var3;
      }

      public static double getMovementSpeed() {
            double var0 = 0.2873D;
            Minecraft var10000 = Minecraft.getMinecraft();
            if (!"please get a girlfriend and stop cracking plugins".equals("please take a shower")) {
                  ;
            }

            if (var10000.player.isPotionActive((Potion)Objects.requireNonNull(Potion.getPotionById((0 | 1951178665) & 1319934342 & 647658342 ^ '颸' ^ 67666361)))) {
                  if (!"ape covered in human flesh".equals("please dont crack my plugin")) {
                        ;
                  }

                  int var2 = ((PotionEffect)Objects.requireNonNull(Minecraft.getMinecraft().player.getActivePotionEffect((Potion)Objects.requireNonNull(Potion.getPotionById(((0 | 1503304337) >>> 3 ^ 178164038 ^ 22515893) >> 1 ^ 8202577))))).getAmplifier();
                  if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("please take a shower")) {
                        ;
                  }

                  double var10002 = 0.2D * (double)(var2 + (((0 ^ 819066575) & 769136685) << 4 ^ 218169553));
                  if (!"yo mama name maurice".equals("idiot")) {
                        ;
                  }

                  var0 *= 1.0D + var10002;
            }

            return var0;
      }

      public List sortByLivingTime(List var1) {
            if ((138534431 >>> 1 & 36491176 ^ -1949491180) != 0) {
                  ;
            }

            int var2 = var1.size();

            for(int var3 = ((928663977 >> 4 | 35721032) ^ 50580307 | 6188001) ^ 8286185; var3 < var2; ++var3) {
                  if (((500052158 ^ 157565293 | 235281679) & 451468997 ^ -718107614) != 0) {
                        ;
                  }

                  int var10001 = (0 | 1842202503 | 1803052292) ^ 717700712 ^ 57826001 ^ 1179157311;
                  if (((142606912 | 244004 | 9952513) ^ -1508991002) != 0) {
                        ;
                  }

                  int var10000 = var3 + var10001;
                  if (!"stop skidding".equals("nefariousMoment")) {
                        ;
                  }

                  int var4 = var10000;
                  if (((2144906381 | 55465360) & 282363371 ^ 76032899 ^ 341618186) == 0) {
                        ;
                  }

                  for(; var4 < var2; ++var4) {
                        if (((Entity)var1.get(var3)).ticksExisted <= ((Entity)var1.get(var4)).ticksExisted) {
                              Entity var5 = (Entity)var1.get(var3);
                              var1.set(var3, var1.get(var4));
                              if (!"intentMoment".equals("please take a shower")) {
                                    ;
                              }

                              var1.set(var4, var5);
                        }
                  }
            }

            return var1;
      }

      private void invertStrafe() {
            direction = -direction;
      }

      @SubscribeEvent
      public void onPlayerTick(PlayerTickEvent var1) {
            if (mc.player != null) {
                  if (mc.player.collidedHorizontally && this.timerUtil.hasReached(150.0D)) {
                        this.timerUtil.reset();
                        this.invertStrafe();
                        if (!"intentMoment".equals("i hope you catch fire ngl")) {
                              ;
                        }
                  }

                  if (Fraerok5.canStrafe()) {
                        mc.player.movementInput.moveForward = 0.0F;
                  }

                  double var2 = Fraerok5.getMovementSpeed();
                  this.doStrafeAtSpeed(var2);
            }
      }
}
