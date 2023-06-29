//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.visual;

import java.util.Iterator;
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
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

public class Trajectories extends Module {
      private transient int BOX;

      public void onDisable() {
            GL11.glDeleteLists(this.BOX, (0 & 1370698560) >> 1 << 3 ^ 1);
      }

      public void onRenderWorldLast(float var1) {
            if (mc.player != null) {
                  EntityPlayerSP var2 = Minecraft.getMinecraft().player;
                  if ((148658560 >>> 1 >> 1 ^ 37164640) == 0) {
                        ;
                  }

                  ItemStack var3 = var2.inventory.getCurrentItem();
                  if (var3 == null) {
                        if ((1627780738 ^ 99175284 ^ 590600130 ^ 888472908) != 0) {
                              ;
                        }

                  } else {
                        Item var4 = var3.getItem();
                        if (((1540704179 << 2 >>> 1 >>> 1 ^ 356870202) >> 4 ^ 15270648) == 0) {
                              ;
                        }

                        if ((450248820 << 4 >> 1 >>> 2 ^ 900497640) == 0) {
                              ;
                        }

                        if (!(var4 instanceof ItemBow)) {
                              if ((((1061524009 << 3 & 1777639006) << 4 | 1030149880) ^ -1083189512) == 0) {
                                    ;
                              }

                              if (!(var4 instanceof ItemSnowball)) {
                                    if ((662727073 << 2 >>> 4 & 134949682 ^ -339362423) != 0) {
                                          ;
                                    }

                                    if (!(var4 instanceof ItemEgg) && !(var4 instanceof ItemEnderPearl) && !(var4 instanceof ItemSplashPotion) && !(var4 instanceof ItemLingeringPotion) && !(var4 instanceof ItemFishingRod)) {
                                          return;
                                    }
                              }
                        }

                        boolean var5 = var3.getItem() instanceof ItemBow;
                        double var10000 = var2.lastTickPosX + (var2.posX - var2.lastTickPosX) * (double)var1;
                        float var10001 = var2.rotationYaw;
                        if (((205534786 ^ 143951782) >>> 2 << 4 >>> 3 ^ 40524018) == 0) {
                              ;
                        }

                        double var36 = Math.cos((double)((float)Math.toRadians((double)var10001))) * 0.07999999821186066D;
                        if (!"intentMoment".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                              ;
                        }

                        double var6 = var10000 - var36;
                        double var8 = var2.lastTickPosY + (var2.posY - var2.lastTickPosY) * (double)var1 + (double)var2.getEyeHeight() - 0.04D;
                        if ((112470503 ^ 53247009 ^ 62640222 ^ 102991256) == 0) {
                              ;
                        }

                        var10000 = var2.lastTickPosZ;
                        var36 = var2.posZ - var2.lastTickPosZ;
                        double var10002 = (double)var1;
                        if ((25104 ^ -479430251) != 0) {
                              ;
                        }

                        double var10 = var10000 + var36 * var10002 - Math.sin((double)((float)Math.toRadians((double)var2.rotationYaw))) * 0.07999999821186066D;
                        if (!"you probably spell youre as your".equals("you probably spell youre as your")) {
                              ;
                        }

                        float var35;
                        if (var5) {
                              var35 = 1.0F;
                        } else {
                              var35 = 0.4F;
                              if (((618839590 | 499711559) << 3 >>> 3 << 2 >> 2 ^ 1655641515) != 0) {
                                    ;
                              }
                        }

                        float var12 = var35;
                        var10000 = Math.toRadians((double)var2.rotationYaw);
                        if (((639457634 | 513936182) >>> 3 & 55216755 ^ 1354942329) != 0) {
                              ;
                        }

                        float var13 = (float)var10000;
                        if ((((1763925740 >>> 4 ^ 102780103) >>> 4 | 636091) ^ 2122151227) != 0) {
                              ;
                        }

                        float var14 = (float)Math.toRadians((double)var2.rotationPitch);
                        var10000 = -Math.sin((double)var13);
                        if ((98331123 >>> 1 >> 2 >>> 2 ^ 3072847) == 0) {
                              ;
                        }

                        double var15 = var10000 * Math.cos((double)var14) * (double)var12;
                        var10000 = -Math.sin((double)var14);
                        var36 = (double)var12;
                        if (!"You're so fat whenever you go to the beach the tide comes in.".equals("your mom your dad the one you never had")) {
                              ;
                        }

                        double var17 = var10000 * var36;
                        var10000 = Math.cos((double)var13);
                        if (!"Some babies were dropped on their heads but you were clearly thrown at a wall.".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                              ;
                        }

                        double var19 = var10000 * Math.cos((double)var14) * (double)var12;
                        double var21 = Math.sqrt(var15 * var15 + var17 * var17 + var19 * var19);
                        var15 /= var21;
                        var17 /= var21;
                        if (!"i hope you catch fire ngl".equals("You're so fat whenever you go to the beach the tide comes in.")) {
                              ;
                        }

                        var19 /= var21;
                        if (var5) {
                              int var38 = (('遄' | 20608) ^ '鞠' ^ 335) << 1 ^ 103702;
                              if (((595399469 ^ 217438669) >> 2 >>> 2 >> 4 ^ 3115210) == 0) {
                                    ;
                              }

                              float var23 = (float)(var38 - var2.getItemInUseCount()) / 20.0F;
                              if (((604795566 ^ 586444603) >> 4 >>> 4 ^ 456733) == 0) {
                                    ;
                              }

                              var35 = var23 * var23;
                              if ((42148761 >>> 3 << 4 >>> 1 & 36701841 ^ 1324480315) != 0) {
                                    ;
                              }

                              var23 = (var35 + var23 * 2.0F) / 3.0F;
                              if (var23 > 1.0F || var23 <= 0.1F) {
                                    var23 = 1.0F;
                              }

                              var23 *= 3.0F;
                              var15 *= (double)var23;
                              var17 *= (double)var23;
                              var19 *= (double)var23;
                        } else {
                              var15 *= 1.5D;
                              var17 *= 1.5D;
                              var19 *= 1.5D;
                        }

                        if (((253888323 ^ 38347200) >>> 1 >>> 1 ^ 502308806) != 0) {
                              ;
                        }

                        if ((286853612 >>> 3 >> 1 ^ 326417676) != 0) {
                              ;
                        }

                        GL11.glPushAttrib(((21204 ^ 6777) >> 4 | 331) ^ 25806);
                        GL11.glDisable(1358 >> 3 << 3 >>> 4 ^ 2820);
                        GL11.glDisable(((1872 | 1056) ^ 1521) << 4 ^ 9713);
                        GL11.glEnable(((2345 | 1587) ^ 3862) & 8 ^ 0 ^ 3050);
                        GL11.glBlendFunc((391 << 3 ^ 2004 | 2800) ^ 2302, (498 << 1 | 187 | 331) << 3 ^ 7419);
                        GL11.glDisable(629 >> 1 << 2 >> 1 ^ 2309);
                        GL11.glDepthMask((boolean)((341739585 << 3 & 285994819) >> 1 ^ 8448));
                        GL11.glEnable((1100 << 3 ^ 2187) >> 1 << 3 ^ 'ꂈ');
                        GL11.glLineWidth(2.0F);
                        RenderManager var32 = Minecraft.getMinecraft().getRenderManager();
                        if (var5) {
                              var10000 = 0.005D;
                        } else {
                              if ((2044466708 >>> 2 ^ 24859448 ^ 520902333) == 0) {
                                    ;
                              }

                              if (var4 instanceof ItemPotion) {
                                    var10000 = 0.04D;
                              } else if (var4 instanceof ItemFishingRod) {
                                    if (!"stringer is a good obfuscator".equals("you probably spell youre as your")) {
                                          ;
                                    }

                                    var10000 = 0.015D;
                              } else {
                                    var10000 = 0.003D;
                              }
                        }

                        double var24 = var10000;
                        if ((762495726 >>> 1 >>> 4 << 4 ^ 381247856) == 0) {
                              ;
                        }

                        Vec3d var41 = new Vec3d;
                        var10002 = var2.posX;
                        double var10003 = var2.posY + (double)var2.getEyeHeight();
                        if ((1107304453 << 2 ^ 134250516) == 0) {
                              ;
                        }

                        var41.<init>(var10002, var10003, var2.posZ);
                        if (((369672198 | 260003222) ^ -699115961) != 0) {
                              ;
                        }

                        Vec3d var26 = var41;
                        int var27 = 669071068 >> 2 >> 3 << 4 ^ 334535520;
                        if (((1122443927 ^ 960913430) >> 1 & 203829161 ^ 201335040) == 0) {
                              ;
                        }

                        boolean var28 = this.predictHit(var26, new Vec3d(var6, var8, var10), new Vec3d(var15, var17, var19), var24);
                        if (var28) {
                              GL11.glColor4f(0.9F, 0.2F, 0.1F, 0.5F);
                        } else {
                              GL11.glColor4f(0.0F, 0.9F, 0.8F, 0.5F);
                        }

                        GL11.glBegin((0 << 1 | 1071470699) ^ 1071470696);

                        for(int var29 = 1626854813 >>> 4 >> 3 << 1 >> 4 >> 2 ^ 397181; var29 < ((373 ^ 20 | 12) >> 1 ^ 862); ++var29) {
                              RayTraceResult var42 = Minecraft.getMinecraft().world.rayTraceBlocks(var26, new Vec3d(var6, var8, var10));
                              if ((146383344 << 3 >> 2 << 3 ^ -1717221915) != 0) {
                                    ;
                              }

                              if (var42 != null) {
                                    if (var28) {
                                          GL11.glColor4f(0.3F, 0.1F, 0.1F, 0.5F);
                                    } else {
                                          GL11.glColor4f(0.1F, 0.3F, 0.3F, 0.5F);
                                    }
                              } else if (var28) {
                                    if ((1081768951 << 3 << 2 >> 2 >>> 2 ^ -1367254941) != 0) {
                                          ;
                                    }

                                    GL11.glColor4f(0.9F, 0.2F, 0.1F, 0.5F);
                              } else {
                                    if (!"ape covered in human flesh".equals("please dont crack my plugin")) {
                                          ;
                                    }

                                    GL11.glColor4f(0.0F, 0.9F, 0.8F, 0.5F);
                              }

                              GL11.glVertex3d(var6 - var32.viewerPosX, var8 - var32.viewerPosY, var10 - var32.viewerPosZ);
                              var6 += var15 * 0.1D;
                              if (!"i hope you catch fire ngl".equals("stringer is a good obfuscator")) {
                                    ;
                              }

                              var8 += var17 * 0.1D;
                              var10 += var19 * 0.1D;
                              var15 *= 0.999D;
                              var17 *= 0.999D;
                              if (((1921717365 ^ 1005280105 ^ 126516574 ^ 573509466 ^ 1414016140) << 3 ^ -999781216) == 0) {
                                    ;
                              }

                              var19 *= 0.999D;
                              if (((696014774 ^ 656848056) >> 4 ^ 15052336) == 0) {
                                    ;
                              }

                              var17 -= var24;
                              Iterator var43 = Minecraft.getMinecraft().world.loadedEntityList.iterator();
                              if (((186334994 ^ 169307193) << 1 ^ 615792296) != 0) {
                                    ;
                              }

                              Iterator var30 = var43;

                              Vec3d var37;
                              AxisAlignedBB var45;
                              while(var30.hasNext()) {
                                    Object var44 = var30.next();
                                    if ((((542408418 | 437848834) & 306203052) >> 1 ^ 56032607 ^ 778366848) != 0) {
                                          ;
                                    }

                                    Entity var31 = (Entity)var44;
                                    if (var31 instanceof EntityLiving) {
                                          var45 = var31.getEntityBoundingBox().grow(0.35D, 0.35D, 0.35D);
                                          var37 = new Vec3d;
                                          if (((1237768522 | 245527806) << 3 >>> 1 & 658236477 ^ 656139320) == 0) {
                                                ;
                                          }

                                          var37.<init>(var6, var8, var10);
                                          if (var45.contains(var37)) {
                                                var27 = (0 << 2 << 3 ^ 1912318950) << 4 ^ 372732325 ^ 160288708;
                                                break;
                                          }
                                    }

                                    if (((798100979 << 1 << 4 ^ 1969366161) << 3 ^ 2136497946) != 0) {
                                          ;
                                    }
                              }

                              if (((93624776 ^ 8013615 ^ 50823969) >> 4 << 1 ^ 1037109014) != 0) {
                                    ;
                              }

                              if (var27 != 0) {
                                    if ((17318016 >>> 4 ^ 886692167) != 0) {
                                          ;
                                    }
                                    break;
                              }

                              var30 = Minecraft.getMinecraft().world.playerEntities.iterator();

                              while(var30.hasNext()) {
                                    EntityPlayer var34 = (EntityPlayer)var30.next();
                                    if (var34 != Minecraft.getMinecraft().player) {
                                          var45 = var34.getEntityBoundingBox().grow(0.35D, 0.35D, 0.35D);
                                          var37 = new Vec3d;
                                          if (((671304601 >> 3 >>> 1 >> 3 & 3356111) >>> 1 ^ -1932903018) != 0) {
                                                ;
                                          }

                                          var37.<init>(var6, var8, var10);
                                          if (var45.contains(var37)) {
                                                var27 = (0 | 1603870362 | 1512778188) ^ 1606107103;
                                                break;
                                          }
                                    }
                              }

                              if (var27 != 0) {
                                    break;
                              }

                              WorldClient var46 = Minecraft.getMinecraft().world;
                              BlockPos var39 = new BlockPos;
                              Vec3d var40 = new Vec3d;
                              if (!"stop skidding".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                                    ;
                              }

                              var40.<init>(var6, var8, var10);
                              var39.<init>(var40);
                              Block var33 = var46.getBlockState(var39).getBlock();
                              if (BlockUtil.isCollidable(var33)) {
                                    break;
                              }

                              if ((2047299424 << 1 & 2048237594 ^ 251673581) != 0) {
                                    ;
                              }
                        }

                        GL11.glEnd();
                        GL11.glPushMatrix();
                        if (!"Some babies were dropped on their heads but you were clearly thrown at a wall.".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                              ;
                        }

                        var10000 = var6 - var32.viewerPosX;
                        var36 = var8 - var32.viewerPosY;
                        var10003 = var32.viewerPosZ;
                        if (!"stop skidding".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                              ;
                        }

                        GL11.glTranslated(var10000, var36, var10 - var10003);
                        if (!"stop skidding".equals("You're so fat whenever you go to the beach the tide comes in.")) {
                              ;
                        }

                        GL11.glCallList(this.BOX);
                        GL11.glPopMatrix();
                        GL11.glPopAttrib();
                  }
            }
      }

      public void onEnable() {
            if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("please dont crack my plugin")) {
                  ;
            }

            this.BOX = GL11.glGenLists((0 << 1 | 1305613096) & 882726915 ^ 76550145);
            GL11.glNewList(this.BOX, (885 | 307 | 844) << 1 ^ 5630);
            RenderUtil.drawSolidBox(new AxisAlignedBB(-0.5D, -0.5D, -0.5D, 0.5D, 0.5D, 0.5D));
            GL11.glEndList();
      }

      boolean predictHit(Vec3d var1, Vec3d var2, Vec3d var3, double var4) {
            if (((1266325423 | 213997831) >> 3 ^ -1983072100) != 0) {
                  ;
            }

            int var6 = (124547324 | 32701258) << 4 ^ 2146418656;

            for(int var7 = 523015511 >>> 1 & 166766629 ^ 160434209; var7 < ((218 | 66) >> 3 ^ 19 ^ 242); ++var7) {
                  var2 = var2.add(var3.scale(0.4D));
                  Vec3d var10000 = new Vec3d;
                  double var10002 = var3.x * 0.996D;
                  double var10003 = var3.y * 0.996D;
                  if ((((406999044 | 70501876) & 383627354 | 241340244) ^ -1621187150) != 0) {
                        ;
                  }

                  var10000.<init>(var10002, var10003 - var4 * 4.0D, var3.z * 0.996D);
                  var3 = var10000;
                  if (((1891973545 ^ 175307393) >> 1 >> 3 >>> 4 ^ 8042435) == 0) {
                        ;
                  }

                  Iterator var8 = Minecraft.getMinecraft().world.loadedEntityList.iterator();

                  while(var8.hasNext()) {
                        Entity var12 = (Entity)var8.next();
                        if (((63183718 >> 4 >>> 2 | 226138) >>> 3 ^ 580486849) != 0) {
                              ;
                        }

                        Entity var9 = var12;
                        if (var9 instanceof EntityLiving) {
                              AxisAlignedBB var13 = var9.getEntityBoundingBox();
                              if (!"shitted on you harder than archybot".equals("stringer is a good obfuscator")) {
                                    ;
                              }

                              if (var13.grow(0.35D, 0.35D, 0.35D).contains(var2)) {
                                    if (!"stringer is a good obfuscator".equals("intentMoment")) {
                                          ;
                                    }

                                    var6 = (0 << 3 ^ 263931172) >> 1 ^ 131965587;
                                    break;
                              }
                        }
                  }

                  if (var6 != 0) {
                        break;
                  }

                  if ((521159992 << 3 >>> 2 >> 1 ^ -93469012) != 0) {
                        ;
                  }

                  var8 = Minecraft.getMinecraft().world.playerEntities.iterator();

                  while(var8.hasNext()) {
                        EntityPlayer var11 = (EntityPlayer)var8.next();
                        if (var11 != Minecraft.getMinecraft().player && var11.getEntityBoundingBox().grow(0.35D, 0.35D, 0.35D).contains(var2)) {
                              if (((2025818300 >> 1 ^ 224107810) << 4 ^ 272996288) == 0) {
                                    ;
                              }

                              var6 = 0 << 4 >> 1 ^ 362228764 ^ 362228765;
                              break;
                        }
                  }

                  if (var6 != 0) {
                        if ((738871058 << 4 & 239302080 ^ 17969 ^ 778848693) != 0) {
                              ;
                        }
                        break;
                  }

                  Block var10 = Minecraft.getMinecraft().world.getBlockState(new BlockPos(var2)).getBlock();
                  if (BlockUtil.isCollidable(var10)) {
                        break;
                  }
            }

            return (boolean)var6;
      }

      public Trajectories() {
            if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("your mom your dad the one you never had")) {
                  ;
            }

            super("Trajectories", "show trajectories of different stuff", 1896344836 >>> 2 >>> 3 ^ 59260776, Module.Category.RENDER);
            if (!"yo mama name maurice".equals("please dont crack my plugin")) {
                  ;
            }

            this.BOX = (285497131 | 142712532 | 248111620) << 2 & 480412254 ^ 472023644;
            if (!"buy a domain and everything else you need at namecheap.com".equals("idiot")) {
                  ;
            }

      }
}
