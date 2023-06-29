//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.visual;

import java.util.Iterator;
import java.util.List;
import me.independed.inceptice.modules.Module;
import me.independed.inceptice.util.BlockUtil;
import me.independed.inceptice.util.RenderUtil;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemEgg;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemLingeringPotion;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemSnowball;
import net.minecraft.item.ItemSplashPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

public class Trajectories extends Module {
      private transient int BOX;

      public void onEnable() {
            this.BOX = GL11.glGenLists((0 << 4 >> 1 >>> 4 | 1060071378) ^ 1060071379);
            int var10000 = this.BOX;
            int var10001 = 2749 >> 4 << 1 >>> 4 ^ 6 ^ 4883;
            if ((((826585102 ^ 520800232) >> 2 | 170539120) ^ 1632425769) != 0) {
                  ;
            }

            GL11.glNewList(var10000, var10001);
            if (((1659721900 ^ 28379769) & 1266320677 ^ 1129840645) == 0) {
                  ;
            }

            AxisAlignedBB var1 = new AxisAlignedBB;
            if (((1555902292 >> 1 ^ 394788564 ^ 23790998 | 122087820) ^ -1315516725) != 0) {
                  ;
            }

            var1.<init>(-0.5D, -0.5D, -0.5D, 0.5D, 0.5D, 0.5D);
            RenderUtil.drawSolidBox(var1);
            GL11.glEndList();
      }

      public void onDisable() {
            GL11.glDeleteLists(this.BOX, (0 >> 4 & 1665929339) >> 1 >> 2 ^ 1);
      }

      boolean predictHit(Vec3d var1, Vec3d var2, Vec3d var3, double var4) {
            int var6 = (93347832 | 79600991 | 15521861) >> 3 ^ 12573695;

            for(int var7 = 73165822 >> 4 << 2 ^ 18291452; var7 < (((145 | 113 | 97) ^ 151) >>> 3 ^ 246); ++var7) {
                  if (!"intentMoment".equals("i hope you catch fire ngl")) {
                        ;
                  }

                  var2 = var2.add(var3.scale(0.4D));
                  Vec3d var10000 = new Vec3d;
                  double var10002 = var3.x * 0.996D;
                  double var10003 = var3.y * 0.996D;
                  if ((1196891224 >>> 1 >>> 3 << 2 ^ 299222804) == 0) {
                        ;
                  }

                  var10003 -= var4 * 4.0D;
                  if (((36152362 | 14625664) << 4 << 3 >> 4 ^ 134053200) == 0) {
                        ;
                  }

                  var10000.<init>(var10002, var10003, var3.z * 0.996D);
                  var3 = var10000;
                  Iterator var8 = Minecraft.getMinecraft().world.loadedEntityList.iterator();
                  if (!"please take a shower".equals("please dont crack my plugin")) {
                        ;
                  }

                  while(var8.hasNext()) {
                        Entity var9 = (Entity)var8.next();
                        if (var9 instanceof EntityLiving && var9.getEntityBoundingBox().grow(0.35D, 0.35D, 0.35D).contains(var2)) {
                              var6 = (0 & 459665566) >>> 1 << 2 ^ 1;
                              break;
                        }
                  }

                  if (var6 != 0) {
                        break;
                  }

                  Minecraft var12 = Minecraft.getMinecraft();
                  if ((372047308 << 4 >>> 2 >> 1 >> 3 ^ -886194233) != 0) {
                        ;
                  }

                  var8 = var12.world.playerEntities.iterator();

                  while(var8.hasNext()) {
                        if (((137906784 | 25230963) << 1 ^ 1053927228) != 0) {
                              ;
                        }

                        EntityPlayer var11 = (EntityPlayer)var8.next();
                        if (var11 != Minecraft.getMinecraft().player) {
                              AxisAlignedBB var13 = var11.getEntityBoundingBox();
                              if (((1086289550 | 250590743) >> 3 ^ 130966798 ^ 236035037) == 0) {
                                    ;
                              }

                              if (var13.grow(0.35D, 0.35D, 0.35D).contains(var2)) {
                                    var6 = ((0 & 1036052411) >>> 1 ^ 1581152369 | 514120788) & 1322560503 ^ 1318365300;
                                    break;
                              }
                        }
                  }

                  if (var6 != 0) {
                        break;
                  }

                  WorldClient var14 = Minecraft.getMinecraft().world;
                  BlockPos var10001 = new BlockPos;
                  if (!"you're dogshit".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                        ;
                  }

                  var10001.<init>(var2);
                  Block var10 = var14.getBlockState(var10001).getBlock();
                  if (BlockUtil.isCollidable(var10)) {
                        break;
                  }
            }

            return (boolean)var6;
      }

      public void onRenderWorldLast(float var1) {
            if (((371688515 | 365858068) >> 3 ^ -1827528711) != 0) {
                  ;
            }

            Minecraft var10000 = mc;
            if ((((1940182631 ^ 476218511) & 222073609) >>> 1 ^ 2116210220) != 0) {
                  ;
            }

            if (var10000.player != null) {
                  if (!"your mom your dad the one you never had".equals("stringer is a good obfuscator")) {
                        ;
                  }

                  EntityPlayerSP var2 = Minecraft.getMinecraft().player;
                  ItemStack var3 = var2.inventory.getCurrentItem();
                  if (var3 != null) {
                        Item var4 = var3.getItem();
                        if (((640756798 | 167470713 | 290822659) ^ 235290790) != 0) {
                              ;
                        }

                        if ((((1487777003 ^ 556408834) << 4 ^ 564446654) << 1 ^ 74651256) != 0) {
                              ;
                        }

                        boolean var35;
                        if (!(var4 instanceof ItemBow) && !(var4 instanceof ItemSnowball) && !(var4 instanceof ItemEgg) && !(var4 instanceof ItemEnderPearl) && !(var4 instanceof ItemSplashPotion)) {
                              var35 = var4 instanceof ItemLingeringPotion;
                              if ((((1697934451 | 1444942768) ^ 24827956) >>> 2 ^ -49246078) != 0) {
                                    ;
                              }

                              if (!var35 && !(var4 instanceof ItemFishingRod)) {
                                    return;
                              }
                        }

                        boolean var5 = var3.getItem() instanceof ItemBow;
                        double var36 = var2.lastTickPosX;
                        if (((1695849568 ^ 1577386897) >>> 1 >> 1 >> 3 ^ 30969087) == 0) {
                              ;
                        }

                        var36 += (var2.posX - var2.lastTickPosX) * (double)var1;
                        double var10001 = Math.toRadians((double)var2.rotationYaw);
                        if ((((1396478978 ^ 724701744) >> 4 >>> 1 | 25953643) ^ 17759447 ^ 46303660) == 0) {
                              ;
                        }

                        double var6 = var36 - Math.cos((double)((float)var10001)) * 0.07999999821186066D;
                        var36 = var2.lastTickPosY;
                        if (((1032432023 ^ 559245292) << 3 & 2048110803 ^ 1644310736) == 0) {
                              ;
                        }

                        var36 += (var2.posY - var2.lastTickPosY) * (double)var1;
                        if (!"i hope you catch fire ngl".equals("please go outside")) {
                              ;
                        }

                        double var8 = var36 + (double)var2.getEyeHeight() - 0.04D;
                        var36 = var2.lastTickPosZ;
                        var10001 = var2.posZ;
                        double var10002 = var2.lastTickPosZ;
                        if ((746261389 << 3 >>> 3 ^ 87766713 ^ 1708438420) != 0) {
                              ;
                        }

                        var10001 -= var10002;
                        if ((1161342350 >> 4 << 1 >> 1 ^ -131846839) != 0) {
                              ;
                        }

                        if ((494260867 >>> 1 >>> 2 ^ 61782608) == 0) {
                              ;
                        }

                        var36 += var10001 * (double)var1;
                        float var37 = var2.rotationYaw;
                        if (((1248349981 >>> 1 ^ 190566038) & 686096532 ^ 677707792) == 0) {
                              ;
                        }

                        double var10 = var36 - Math.sin((double)((float)Math.toRadians((double)var37))) * 0.07999999821186066D;
                        float var38;
                        if (var5) {
                              if (!"please take a shower".equals("you're dogshit")) {
                                    ;
                              }

                              var38 = 1.0F;
                        } else {
                              var38 = 0.4F;
                        }

                        float var12 = var38;
                        float var13 = (float)Math.toRadians((double)var2.rotationYaw);
                        float var14 = (float)Math.toRadians((double)var2.rotationPitch);
                        var36 = -Math.sin((double)var13) * Math.cos((double)var14);
                        var10001 = (double)var12;
                        if (((548021116 << 1 << 4 | 3937759) ^ 360480735) == 0) {
                              ;
                        }

                        double var15 = var36 * var10001;
                        var36 = -Math.sin((double)var14) * (double)var12;
                        if (!"You're so fat whenever you go to the beach the tide comes in.".equals("please dont crack my plugin")) {
                              ;
                        }

                        double var17 = var36;
                        var36 = Math.cos((double)var13);
                        var10001 = Math.cos((double)var14);
                        if (!"i hope you catch fire ngl".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                              ;
                        }

                        double var19 = var36 * var10001 * (double)var12;
                        double var21 = Math.sqrt(var15 * var15 + var17 * var17 + var19 * var19);
                        var15 /= var21;
                        var17 /= var21;
                        var19 /= var21;
                        if (var5) {
                              float var23 = (float)(('ꀀ' ^ 5422 ^ 109678) - var2.getItemInUseCount()) / 20.0F;
                              if (((412403386 >> 3 & 37910296) >> 3 << 4 ^ -750007214) != 0) {
                                    ;
                              }

                              var38 = var23 * var23 + var23 * 2.0F;
                              if (!"Some babies were dropped on their heads but you were clearly thrown at a wall.".equals("stringer is a good obfuscator")) {
                                    ;
                              }

                              var23 = var38 / 3.0F;
                              if (var23 > 1.0F || var23 <= 0.1F) {
                                    var23 = 1.0F;
                              }

                              if (!"please dont crack my plugin".equals("buy a domain and everything else you need at namecheap.com")) {
                                    ;
                              }

                              var23 *= 3.0F;
                              if (((101207815 | 26243627) << 4 ^ 947695801) != 0) {
                                    ;
                              }

                              var15 *= (double)var23;
                              var17 *= (double)var23;
                              if (!"please get a girlfriend and stop cracking plugins".equals("nefariousMoment")) {
                                    ;
                              }

                              var19 *= (double)var23;
                        } else {
                              var15 *= 1.5D;
                              var17 *= 1.5D;
                              var19 *= 1.5D;
                        }

                        GL11.glPushAttrib(23397 << 4 >> 4 << 1 ^ 'ퟏ');
                        GL11.glDisable((151 >>> 4 & 2) << 3 >>> 3 ^ 2896);
                        GL11.glDisable(1152 << 4 & 16338 ^ 1505);
                        GL11.glEnable((2140 & 884 | 28) >> 1 << 2 ^ 2906);
                        GL11.glBlendFunc((68 >> 4 ^ 2 | 3) & 3 ^ 769, (105 >> 3 & 7) << 4 << 2 ^ 579);
                        GL11.glDisable((1732 ^ 890 | 1255 | 73) >> 3 ^ 3022);
                        GL11.glDepthMask((boolean)((1976852269 | 1509932716) << 3 ^ -268436120));
                        GL11.glEnable((2075 | 443) >>> 1 << 2 ^ 2622 ^ 4714);
                        GL11.glLineWidth(2.0F);
                        RenderManager var32 = Minecraft.getMinecraft().getRenderManager();
                        if (var5) {
                              var36 = 0.005D;
                        } else if (var4 instanceof ItemPotion) {
                              var36 = 0.04D;
                        } else if (var4 instanceof ItemFishingRod) {
                              if (((404328998 ^ 344140514) >>> 2 & 27346397 ^ 1445691122) != 0) {
                                    ;
                              }

                              var36 = 0.015D;
                        } else {
                              var36 = 0.003D;
                        }

                        if (((21102624 ^ 10935592) >> 3 ^ 3157724 ^ 553211 ^ 312710) == 0) {
                              ;
                        }

                        double var24 = var36;
                        Vec3d var43 = new Vec3d;
                        var10002 = var2.posX;
                        if ((((320907822 >>> 3 | 1234506) << 2 | 117726983) ^ 266297151) == 0) {
                              ;
                        }

                        double var10003 = var2.posY;
                        if ((1922972215 << 3 >> 1 >> 3 ^ 876997042) != 0) {
                              ;
                        }

                        double var10004 = (double)var2.getEyeHeight();
                        if (!"you probably spell youre as your".equals("your mom your dad the one you never had")) {
                              ;
                        }

                        var43.<init>(var10002, var10003 + var10004, var2.posZ);
                        Vec3d var26 = var43;
                        int var27 = 349666921 >>> 3 & 1392324 ^ 1060548;
                        if (((496205190 | 152058811) >> 1 >> 3 << 2 >>> 3 ^ 15506429) == 0) {
                              ;
                        }

                        Vec3d var39 = new Vec3d;
                        if (!"please go outside".equals("nefariousMoment")) {
                              ;
                        }

                        var39.<init>(var6, var8, var10);
                        Vec3d var40 = new Vec3d;
                        if (((781948564 >> 3 ^ 91954673) >> 2 ^ 2759624) == 0) {
                              ;
                        }

                        var40.<init>(var15, var17, var19);
                        boolean var28 = this.predictHit(var26, var39, var40, var24);
                        if (var28) {
                              GL11.glColor4f(0.9F, 0.2F, 0.1F, 0.5F);
                        } else {
                              GL11.glColor4f(0.0F, 0.9F, 0.8F, 0.5F);
                        }

                        GL11.glBegin(2 >> 1 << 3 >>> 1 ^ 7);

                        for(int var29 = (2037013727 ^ 1842694010) >> 1 >>> 4 >> 4 ^ 679821; var29 < ((214 ^ 125) >>> 4 >> 3 ^ 1001); ++var29) {
                              if (((2014383663 | 638168061) << 3 ^ 955378235) != 0) {
                                    ;
                              }

                              var10000 = Minecraft.getMinecraft();
                              if ((1283118973 >>> 3 & 63955766 ^ 3763518 ^ 28716824) == 0) {
                                    ;
                              }

                              WorldClient var44 = var10000.world;
                              if ((1551460040 << 1 & 1899199038 ^ 808601616) == 0) {
                                    ;
                              }

                              var39 = new Vec3d;
                              if (((423197823 ^ 337591700) << 1 >>> 1 ^ 220611051) == 0) {
                                    ;
                              }

                              var39.<init>(var6, var8, var10);
                              if (var44.rayTraceBlocks(var26, var39) != null) {
                                    if (var28) {
                                          if (((1824258037 ^ 85606459) << 1 ^ 303809951 ^ 502561289) != 0) {
                                                ;
                                          }

                                          GL11.glColor4f(0.3F, 0.1F, 0.1F, 0.5F);
                                          if (!"please take a shower".equals("nefariousMoment")) {
                                                ;
                                          }
                                    } else {
                                          if (!"buy a domain and everything else you need at namecheap.com".equals("minecraft")) {
                                                ;
                                          }

                                          if ((576405336 >>> 4 & 33055780 ^ 2105380) == 0) {
                                                ;
                                          }

                                          GL11.glColor4f(0.1F, 0.3F, 0.3F, 0.5F);
                                    }
                              } else if (var28) {
                                    GL11.glColor4f(0.9F, 0.2F, 0.1F, 0.5F);
                              } else {
                                    GL11.glColor4f(0.0F, 0.9F, 0.8F, 0.5F);
                              }

                              if (((887944476 | 849231736 | 726135021) ^ 554657946 ^ 2106070220) != 0) {
                                    ;
                              }

                              var36 = var6 - var32.viewerPosX;
                              var10001 = var8 - var32.viewerPosY;
                              if (((774496512 >>> 2 >>> 1 >>> 1 >>> 2 | 2664670) ^ 1199713708) != 0) {
                                    ;
                              }

                              GL11.glVertex3d(var36, var10001, var10 - var32.viewerPosZ);
                              var6 += var15 * 0.1D;
                              if (((587333774 >> 4 >>> 1 | 16592042) ^ 1478377704) != 0) {
                                    ;
                              }

                              if (!"idiot".equals("minecraft")) {
                                    ;
                              }

                              var8 += var17 * 0.1D;
                              var10 += var19 * 0.1D;
                              if (!"you're dogshit".equals("please go outside")) {
                                    ;
                              }

                              var15 *= 0.999D;
                              if ((731872 >>> 1 >>> 2 ^ 91484) == 0) {
                                    ;
                              }

                              var17 *= 0.999D;
                              var19 *= 0.999D;
                              if (((1195705288 << 1 >>> 2 ^ 332431575) & 8271156 ^ 7471408) == 0) {
                                    ;
                              }

                              var17 -= var24;
                              if (((347361668 << 3 << 3 ^ 417382262) >> 4 ^ 1584750119) != 0) {
                                    ;
                              }

                              List var45 = Minecraft.getMinecraft().world.loadedEntityList;
                              if (!"nefariousMoment".equals("please take a shower")) {
                                    ;
                              }

                              Iterator var30 = var45.iterator();

                              while(var30.hasNext()) {
                                    Entity var31 = (Entity)var30.next();
                                    if (!"please take a shower".equals("i hope you catch fire ngl")) {
                                          ;
                                    }

                                    if (var31 instanceof EntityLiving) {
                                          AxisAlignedBB var46 = var31.getEntityBoundingBox().grow(0.35D, 0.35D, 0.35D);
                                          Vec3d var41 = new Vec3d;
                                          if (((((1565833310 | 1182300920) ^ 64812654) >> 2 & 104435024) >> 1 ^ 51642496) == 0) {
                                                ;
                                          }

                                          var41.<init>(var6, var8, var10);
                                          if (var46.contains(var41)) {
                                                int var47 = ((0 << 1 << 1 ^ 2120314171) & 1017934914) >>> 1 ^ 504377856;
                                                if (((195329296 << 2 & 321362822) >> 1 & 8290264 ^ 846470263) != 0) {
                                                      ;
                                                }

                                                var27 = var47;
                                                break;
                                          }
                                    }
                              }

                              if (var27 != 0) {
                                    if ((519664043 >>> 2 << 1 ^ 259832020) == 0) {
                                          ;
                                    }
                                    break;
                              }

                              var10000 = Minecraft.getMinecraft();
                              if (!"idiot".equals("you probably spell youre as your")) {
                                    ;
                              }

                              var30 = var10000.world.playerEntities.iterator();

                              while(var30.hasNext()) {
                                    EntityPlayer var34 = (EntityPlayer)var30.next();
                                    EntityPlayerSP var42 = Minecraft.getMinecraft().player;
                                    if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                                          ;
                                    }

                                    if (var34 != var42 && var34.getEntityBoundingBox().grow(0.35D, 0.35D, 0.35D).contains(new Vec3d(var6, var8, var10))) {
                                          var27 = 0 & 917170552 & 1756889421 ^ 995525061 ^ 995525060;
                                          break;
                                    }
                              }

                              if (var27 != 0) {
                                    break;
                              }

                              Block var33 = Minecraft.getMinecraft().world.getBlockState(new BlockPos(new Vec3d(var6, var8, var10))).getBlock();
                              if (!"you probably spell youre as your".equals("minecraft")) {
                                    ;
                              }

                              var35 = BlockUtil.isCollidable(var33);
                              if ((((289538320 ^ 140993628) >> 4 | 9950983) ^ 26728279) == 0) {
                                    ;
                              }

                              if (var35) {
                                    break;
                              }
                        }

                        GL11.glEnd();
                        GL11.glPushMatrix();
                        if (!"You're so fat whenever you go to the beach the tide comes in.".equals("nefariousMoment")) {
                              ;
                        }

                        var36 = var6 - var32.viewerPosX;
                        if (((1298856309 >>> 1 ^ 318731775 ^ 585300361) >>> 2 ^ 56193949 ^ 116608942) == 0) {
                              ;
                        }

                        if (!"minecraft".equals("buy a domain and everything else you need at namecheap.com")) {
                              ;
                        }

                        GL11.glTranslated(var36, var8 - var32.viewerPosY, var10 - var32.viewerPosZ);
                        GL11.glCallList(this.BOX);
                        GL11.glPopMatrix();
                        GL11.glPopAttrib();
                  }
            }
      }

      public Trajectories() {
            int var10003 = (773933214 >>> 3 ^ 69050271) << 1 ^ 62090520;
            Module.Category var10004 = Module.Category.RENDER;
            if (((513779132 | 499123075) >> 1 ^ 267375583) == 0) {
                  ;
            }

            super("Trajectories", "show trajectories of different stuff", var10003, var10004);
            this.BOX = 1790644491 << 3 >>> 2 ^ 139225274 ^ 484276796 ^ 31928464;
      }
}
