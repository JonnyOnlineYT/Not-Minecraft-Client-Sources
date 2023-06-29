//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.visual;

import java.awt.Color;
import java.util.Iterator;
import java.util.LinkedList;
import me.independed.inceptice.modules.Module;
import me.independed.inceptice.settings.BooleanSetting;
import me.independed.inceptice.settings.NumberSetting;
import me.independed.inceptice.settings.Setting;
import me.independed.inceptice.util.TimerUtil;
import me.independed.inceptice.util.XRayBlock;
import me.independed.inceptice.util.XRayData;
import me.independed.inceptice.util.XRayManager;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import org.lwjgl.opengl.GL11;

public class XRay extends Module {
      public BooleanSetting gold;
      public TimerUtil timer;
      public BooleanSetting iron;
      public BooleanSetting lapis;
      public BooleanSetting diamond;
      public NumberSetting distance = new NumberSetting("Distance", this, 21.0D, 4.0D, 100.0D, 1.0D);
      public BooleanSetting ugolek;
      public NumberSetting delay;
      public BooleanSetting spawner;
      LinkedList blocks = new LinkedList();

      @SubscribeEvent
      public void onClientTick(ClientTickEvent var1) {
            if (mc.world != null && mc.player != null) {
                  XRayData var10000;
                  int var10002;
                  int var10003;
                  if (this.diamond.isEnabled()) {
                        var10000 = new XRayData;
                        if ((1200440 >> 1 ^ 600220) == 0) {
                              ;
                        }

                        if ((1056069096 >> 2 ^ 179858340 ^ 51352049 ^ 101412655) == 0) {
                              ;
                        }

                        var10002 = Integer.parseInt("56");
                        var10003 = 533220294 >> 3 >> 3 ^ 8331567;
                        int var10004 = Integer.parseInt("0");
                        int var10005 = Integer.parseInt("155");
                        if (!"stringer is a good obfuscator".equals("you probably spell youre as your")) {
                              ;
                        }

                        if ((629732779 >> 4 << 1 >>> 3 ^ 9839574) == 0) {
                              ;
                        }

                        var10000.<init>(var10002, var10003, var10004, var10005, Integer.parseInt("255"));
                        XRayManager.add(var10000);
                        if (!"please take a shower".equals("please take a shower")) {
                              ;
                        }
                  }

                  if (this.gold.isEnabled()) {
                        var10000 = new XRayData;
                        if ((((21291129 >> 4 | 1284693 | 1288852) ^ 1167392) >>> 2 ^ -1743339334) != 0) {
                              ;
                        }

                        if (((790031000 << 2 & 171251437) << 1 ^ 697530491) != 0) {
                              ;
                        }

                        var10000.<init>(Integer.parseInt("14"), (1276273783 >> 4 ^ 36989931) >>> 1 >> 2 ^ 14592437, Integer.parseInt("0"), Integer.parseInt("255"), Integer.parseInt("0"));
                        if (((1159893909 >> 3 & 43415326 ^ 1489978) >> 2 ^ 333648 ^ 2206938) == 0) {
                              ;
                        }

                        XRayManager.add(var10000);
                  }

                  boolean var9 = this.iron.isEnabled();
                  if (((((611421834 | 197141048) ^ 422788792) & 686085641) << 2 ^ 2024105001) != 0) {
                        ;
                  }

                  if (var9) {
                        var10000 = new XRayData;
                        var10002 = Integer.parseInt("15");
                        if ((389956154 << 4 ^ 1677693097 ^ 270250761) == 0) {
                              ;
                        }

                        var10003 = 1003120884 >> 1 << 2 ^ 2006241768;
                        if (!"please take a shower".equals("your mom your dad the one you never had")) {
                              ;
                        }

                        var10000.<init>(var10002, var10003, Integer.parseInt("155"), Integer.parseInt("155"), Integer.parseInt("155"));
                        XRayManager.add(var10000);
                  }

                  var9 = this.lapis.isEnabled();
                  if ((((2131190056 | 1969151419) >>> 3 | 155175064) >> 4 ^ 16777211) == 0) {
                        ;
                  }

                  if (var9) {
                        var10000 = new XRayData;
                        var10002 = Integer.parseInt("21");
                        var10003 = (42800261 | 15454366) << 2 ^ 197087868;
                        if (!"yo mama name maurice".equals("you probably spell youre as your")) {
                              ;
                        }

                        var10000.<init>(var10002, var10003, Integer.parseInt("0"), Integer.parseInt("0"), Integer.parseInt("255"));
                        XRayManager.add(var10000);
                  }

                  if (this.ugolek.isEnabled()) {
                        XRayManager.add(new XRayData(Integer.parseInt("16"), (2021222251 | 1517198898) ^ 702142959 ^ 1403397780, Integer.parseInt("25"), Integer.parseInt("25"), Integer.parseInt("25")));
                  }

                  var9 = this.spawner.isEnabled();
                  if ((1801457380 << 1 >>> 1 ^ -521159096) != 0) {
                        ;
                  }

                  if (var9) {
                        XRayManager.add(new XRayData(Integer.parseInt("52"), (611023145 >> 3 & 61993238) << 3 ^ 67831840, Integer.parseInt("155"), Integer.parseInt("155"), Integer.parseInt("25")));
                  }

                  double var2 = this.distance.getValue();
                  TimerUtil var10 = this.timer;
                  if (!"shitted on you harder than archybot".equals("i hope you catch fire ngl")) {
                        ;
                  }

                  if (var10.hasReached(this.delay.getValue())) {
                        this.blocks.clear();
                        LinkedList var11 = XRayManager.xrayList;
                        if ((((1936200219 ^ 655745344) << 1 | 1488809592) ^ 563427111) != 0) {
                              ;
                        }

                        Iterator var4 = var11.iterator();

                        while(var4.hasNext()) {
                              XRayData var5 = (XRayData)var4.next();

                              XRayBlock var8;
                              for(Iterator var6 = XRay.findBlocksNearEntity(mc.player, var5.getId(), var5.getMeta(), (int)var2).iterator(); var6.hasNext(); this.blocks.add(var8)) {
                                    BlockPos var7 = (BlockPos)var6.next();
                                    var8 = new XRayBlock(var7, var5);
                                    if ((903966502 >>> 4 & 35048924 ^ 27107393 ^ -240794494) != 0) {
                                          ;
                                    }
                              }
                        }

                        this.timer.reset();
                        if (((16484282 << 2 & 27257776 & 9612971 | 4848590) ^ 12229187 ^ 1900126225) != 0) {
                              ;
                        }
                  }

            }
      }

      @SubscribeEvent
      public void onRenderWorldLast(RenderWorldLastEvent var1) {
            if (mc.world != null && mc.player != null) {
                  XRay.drawXRayBlocks(this.blocks, var1.getPartialTicks());
            }
      }

      public static LinkedList findBlocksNearEntity(EntityLivingBase var0, int var1, int var2, int var3) {
            LinkedList var4 = new LinkedList();
            if (!"minecraft".equals("please dont crack my plugin")) {
                  ;
            }

            int var10000 = (int)mc.player.posX - var3;
            if (((1031130018 ^ 538503653) >>> 2 ^ -983772707) != 0) {
                  ;
            }

            int var5 = var10000;

            while(true) {
                  double var10001 = mc.player.posX;
                  if (!"you're dogshit".equals("you probably spell youre as your")) {
                        ;
                  }

                  if (var5 > (int)var10001 + var3) {
                        if (!"You're so fat whenever you go to the beach the tide comes in.".equals("intentMoment")) {
                              ;
                        }

                        return var4;
                  }

                  for(int var6 = (int)mc.player.posZ - var3; var6 <= (int)mc.player.posZ + var3; ++var6) {
                        int var7 = mc.world.getHeight(var5, var6);
                        int var8 = 763963382 >>> 1 ^ 368255111 ^ 53982076;

                        while(true) {
                              if ((((1451199663 >>> 2 >> 3 | 33647621) ^ 4282679) >>> 1 ^ 24728233) == 0) {
                                    ;
                              }

                              if (var8 > var7) {
                                    break;
                              }

                              BlockPos var13 = new BlockPos;
                              if (((1220212699 >> 1 | 155147518) >>> 4 ^ 47708159) != 0) {
                              }

                              var13.<init>(var5, var8, var6);
                              BlockPos var9 = var13;
                              Minecraft var14 = mc;
                              if ((832351877 << 2 & 291320003 ^ 5255168) == 0) {
                                    ;
                              }

                              IBlockState var10 = var14.world.getBlockState(var9);
                              if (var1 != ((1163997400 << 4 & 813671132 ^ 26098088) >> 1 ^ -147758229) && var2 != ((100931602 >>> 4 << 3 & 15662966) << 4 ^ 815904 ^ -2929441)) {
                                    int var11 = Block.getIdFromBlock(var10.getBlock());
                                    if ((1677721824 >> 2 & 138305900 & 93669297 ^ 32) == 0) {
                                          ;
                                    }

                                    int var12 = var10.getBlock().getMetaFromState(var10);
                                    if (((832573427 << 1 << 1 | 914519160) ^ -159219716) == 0) {
                                          ;
                                    }

                                    if (var11 == var1 && var12 == var2) {
                                          var4.add(var9);
                                          if ((939187078 >>> 1 >>> 4 & 17274079 ^ 963918064) != 0) {
                                                ;
                                          }
                                    }
                              } else {
                                    if ((1958539309 >>> 3 << 3 >>> 2 ^ 489634826) == 0) {
                                          ;
                                    }

                                    var4.add(var9);
                              }

                              ++var8;
                        }
                  }

                  ++var5;
            }
      }

      public XRay() {
            super("XRay", "show ores through the blocks.", (1347590129 >> 4 | 43762296) << 2 >>> 2 >> 4 ^ 7995063, Module.Category.RENDER);
            NumberSetting var10001 = new NumberSetting;
            if ((1318249007 >>> 3 << 2 & 12278880 ^ 610304) == 0) {
                  ;
            }

            if (((293702793 << 1 | 565629295) << 1 ^ 1198502654) == 0) {
                  ;
            }

            var10001.<init>("Delay", this, 5000.0D, 0.0D, 30000.0D, 1000.0D);
            this.delay = var10001;
            this.diamond = new BooleanSetting("Diamond", this, (boolean)(0 >>> 1 >> 4 ^ 1277595625 ^ 1277595624));
            BooleanSetting var1 = new BooleanSetting;
            if (!"stringer is a good obfuscator".equals("please get a girlfriend and stop cracking plugins")) {
                  ;
            }

            var1.<init>("Gold", this, (boolean)(((923736224 << 3 ^ 491080427 | 133721979) & 1126350998) >> 4 ^ 3279881));
            if ((((943706639 | 453363830) >> 4 | 39499420) >> 2 & 13233134 ^ -1694829761) != 0) {
                  ;
            }

            this.gold = var1;
            var1 = new BooleanSetting;
            if (((814971058 ^ 294595505) << 3 ^ 130293125 ^ -138383418) != 0) {
                  ;
            }

            var1.<init>("Iron", this, (boolean)(853776741 >> 1 ^ 242795099 ^ 386469609));
            this.iron = var1;
            var1 = new BooleanSetting("Coal", this, (boolean)((1887755166 | 1114389898) >>> 2 << 3 >>> 4 ^ 241016819));
            if (!"ape covered in human flesh".equals("nefariousMoment")) {
                  ;
            }

            this.ugolek = var1;
            var1 = new BooleanSetting;
            if (((1141329140 | 709752860) >>> 1 << 2 << 4 ^ -905994368) == 0) {
                  ;
            }

            if (!"stringer is a good obfuscator".equals("you're dogshit")) {
                  ;
            }

            var1.<init>("Lazurite", this, (boolean)(((27185470 | 15669862 | 3224741) ^ 4927778) << 1 ^ 57253306));
            this.lapis = var1;
            if (((555065467 >> 3 >>> 1 | 11567264) >> 3 ^ 889884010) != 0) {
                  ;
            }

            if (((719890157 >>> 3 & 54097953) >>> 4 ^ 1151232) == 0) {
                  ;
            }

            var1 = new BooleanSetting;
            if (!"you probably spell youre as your".equals("shitted on you harder than archybot")) {
                  ;
            }

            var1.<init>("Spawner", this, (boolean)((0 ^ 1168656702 | 863746765) >>> 1 >> 2 ^ 251625342));
            this.spawner = var1;
            this.timer = new TimerUtil();
            Setting[] var2 = new Setting[(1 | 0) << 2 ^ 12];
            var2[(1808649529 | 611345789) & 2922052 ^ 2917444] = this.distance;
            if (!"yo mama name maurice".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                  ;
            }

            var2[0 >> 1 >>> 1 ^ 1] = this.delay;
            var2[0 >> 3 << 4 ^ 2] = this.diamond;
            var2[(2 | 0 | 0) ^ 1 ^ 0] = this.gold;
            var2[(1 >> 4 ^ 849911452) >> 3 << 2 ^ 424955720] = this.iron;
            var2[2 >>> 4 ^ 1239573568 ^ 1239573573] = this.ugolek;
            int var10003 = (2 | 1) ^ 1 ^ 1 ^ 5;
            BooleanSetting var10004 = this.lapis;
            if (((334306281 ^ 330655625) >>> 3 ^ -1266663509) != 0) {
                  ;
            }

            var2[var10003] = var10004;
            var2[(0 >> 4 & 1559264287 | 1874902952) ^ 1874902959] = this.spawner;
            this.addSettings(var2);
            if ((((892094179 | 524910863 | 244129363) >> 2 | 170874677) ^ 268400639) == 0) {
                  ;
            }

      }

      public static void drawOutlinedBox(AxisAlignedBB var0) {
            GL11.glBegin((0 ^ 686293336) << 4 << 4 >>> 3 ^ 486550273);
            GL11.glVertex3d(var0.minX, var0.maxY, var0.minZ);
            GL11.glVertex3d(var0.maxX, var0.maxY, var0.minZ);
            if (((1857958485 ^ 830020362 | 1441546013) ^ 1609514847) == 0) {
                  ;
            }

            GL11.glVertex3d(var0.maxX, var0.maxY, var0.minZ);
            GL11.glVertex3d(var0.maxX, var0.maxY, var0.maxZ);
            GL11.glVertex3d(var0.maxX, var0.maxY, var0.maxZ);
            if (!"yo mama name maurice".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                  ;
            }

            GL11.glVertex3d(var0.minX, var0.maxY, var0.maxZ);
            GL11.glVertex3d(var0.minX, var0.maxY, var0.maxZ);
            GL11.glVertex3d(var0.minX, var0.maxY, var0.minZ);
            GL11.glVertex3d(var0.minX, var0.minY, var0.minZ);
            GL11.glVertex3d(var0.maxX, var0.minY, var0.minZ);
            if (!"stringer is a good obfuscator".equals("idiot")) {
                  ;
            }

            double var10000 = var0.maxX;
            if (((551536400 | 253724377) & 114559521 & 98475188 ^ -992790848) != 0) {
                  ;
            }

            GL11.glVertex3d(var10000, var0.minY, var0.minZ);
            GL11.glVertex3d(var0.maxX, var0.minY, var0.maxZ);
            if ((119882551 >> 1 << 4 ^ 173661333) != 0) {
                  ;
            }

            var10000 = var0.maxX;
            double var10001 = var0.minY;
            if (!"you're dogshit".equals("yo mama name maurice")) {
                  ;
            }

            GL11.glVertex3d(var10000, var10001, var0.maxZ);
            if ((4851300 ^ 474667 ^ 5060687) == 0) {
                  ;
            }

            var10000 = var0.minX;
            var10001 = var0.minY;
            if (((1454228683 | 1188464656) >> 3 >>> 3 ^ 22806323) == 0) {
                  ;
            }

            double var10002 = var0.maxZ;
            if (!"please get a girlfriend and stop cracking plugins".equals("please get a girlfriend and stop cracking plugins")) {
                  ;
            }

            GL11.glVertex3d(var10000, var10001, var10002);
            GL11.glVertex3d(var0.minX, var0.minY, var0.maxZ);
            GL11.glVertex3d(var0.minX, var0.minY, var0.minZ);
            GL11.glVertex3d(var0.minX, var0.minY, var0.minZ);
            GL11.glVertex3d(var0.minX, var0.maxY, var0.minZ);
            GL11.glVertex3d(var0.maxX, var0.minY, var0.minZ);
            GL11.glVertex3d(var0.maxX, var0.maxY, var0.minZ);
            GL11.glVertex3d(var0.maxX, var0.minY, var0.maxZ);
            if (((1108276714 ^ 360687109) >> 2 ^ 366761211) == 0) {
                  ;
            }

            var10000 = var0.maxX;
            var10001 = var0.maxY;
            var10002 = var0.maxZ;
            if (((((1430471748 | 1223117641) & 1342872518) >> 4 ^ 83439296) >>> 1 ^ -426284450) != 0) {
                  ;
            }

            GL11.glVertex3d(var10000, var10001, var10002);
            GL11.glVertex3d(var0.minX, var0.minY, var0.maxZ);
            GL11.glVertex3d(var0.minX, var0.maxY, var0.maxZ);
            GL11.glEnd();
            if ((2064981278 >>> 3 ^ 68521474 ^ 192359841) == 0) {
                  ;
            }

      }

      public void onEnable() {
            super.onEnable();
            this.blocks.clear();
      }

      public static void drawXRayBlocks(LinkedList var0, float var1) {
            GL11.glPushMatrix();
            if (((3607436 << 4 ^ 17697432) >>> 2 >>> 4 ^ 6612319) != 0) {
                  ;
            }

            int var10000 = 1677 >> 3 >> 2 & 8 ^ 176469736 ^ 157027397 ^ 64532815;
            if (((657527373 ^ 119971427) << 4 >> 2 >>> 2 ^ 1543214) == 0) {
                  ;
            }

            GL11.glEnable(var10000);
            var10000 = ((186 >> 3 | 11) << 4 | 454) ^ 756;
            int var10001 = (488 ^ 445) >>> 3 >>> 4 ^ 771;
            if (((766018817 ^ 207612558) & 488267043 ^ 1662022536) != 0) {
                  ;
            }

            GL11.glBlendFunc(var10000, var10001);
            if ((1880269463 << 4 >> 4 ^ 1221271) == 0) {
                  ;
            }

            GL11.glEnable((1898 | 1237) >>> 2 >>> 1 & 198 ^ 3046);
            if (((2053205410 >> 2 | 101966861 | 306100701) ^ 84687233 ^ 464766588) == 0) {
                  ;
            }

            GL11.glLineWidth(1.0F);
            GL11.glDisable((2992 ^ 1122) >>> 4 >> 1 ^ 100 ^ 3579);
            GL11.glEnable((1304 << 1 << 4 | 19228) ^ '\ue058');
            GL11.glDisable(694 >> 4 >> 2 ^ 2939);
            GL11.glDisable((346 << 1 & 275) >> 3 ^ 2898);
            WorldClient var2 = mc.world;
            EntityPlayerSP var3 = mc.player;
            if ((1887807591 << 1 >>> 4 >> 3 ^ 29496993) == 0) {
                  ;
            }

            Iterator var4 = var0.iterator();

            while(var4.hasNext()) {
                  XRayBlock var5 = (XRayBlock)var4.next();
                  BlockPos var6 = var5.getBlockPos();
                  XRayData var17 = var5.getxRayData();
                  if ((((495971718 ^ 170887116) << 1 >> 2 | 71728276) << 2 ^ -1700402355) != 0) {
                        ;
                  }

                  XRayData var7 = var17;
                  IBlockState var8 = var2.getBlockState(var6);
                  double var9 = var3.lastTickPosX + (var3.posX - var3.lastTickPosX) * (double)var1;
                  if (((606284341 >>> 2 >>> 1 ^ 5443235 | 18990326) ^ -1430267298) != 0) {
                        ;
                  }

                  double var18 = var3.lastTickPosY;
                  double var19 = var3.posY - var3.lastTickPosY;
                  double var10002 = (double)var1;
                  if (!"please take a shower".equals("you're dogshit")) {
                        ;
                  }

                  var18 += var19 * var10002;
                  if (((50463482 >> 3 ^ 1769827) >> 4 >> 3 ^ '\uf682') == 0) {
                        ;
                  }

                  double var11 = var18;
                  double var13 = var3.lastTickPosZ + (var3.posZ - var3.lastTickPosZ) * (double)var1;
                  Color var20 = new Color;
                  if ((264328 >>> 1 << 2 >>> 1 ^ 1379032127) != 0) {
                        ;
                  }

                  var20.<init>(var7.getRed(), var7.getGreen(), var7.getBlue(), 154 >>> 4 & 2 ^ 255);
                  int var15 = var20.getRGB();
                  XRay.glColor(var15);
                  if ((1053940911 >> 2 ^ 195704890 ^ -293179569) != 0) {
                        ;
                  }

                  AxisAlignedBB var16 = var8.getSelectedBoundingBox(var2, var6).grow(0.0020000000949949026D).offset(-var9, -var11, -var13);
                  XRay.drawOutlinedBox(var16);
            }

            GL11.glEnable((442 >> 2 >>> 4 & 3) >>> 3 ^ 2896);
            GL11.glEnable((210 | 81) ^ 204 ^ 2926);
            var10000 = (2626 << 3 ^ 2924) >> 1 >>> 3 ^ 2166;
            if (((13437572 >> 1 ^ 2165319) << 2 ^ -1874204526) != 0) {
                  ;
            }

            GL11.glEnable(var10000);
            GL11.glDisable((1783 << 1 ^ 2900) & 419 ^ 2880);
            GL11.glDisable((1832 >> 4 | 16) & 90 ^ 2930);
            if (!"shitted on you harder than archybot".equals("you probably spell youre as your")) {
                  ;
            }

            GL11.glPopMatrix();
      }

      public static void glColor(int var0) {
            float var10000 = (float)(var0 >> ((10 >> 3 >>> 1 | 496863072) ^ 496863088) & ((114 ^ 24) << 4 << 3 ^ 13823));
            if (!"yo mama name maurice".equals("please take a shower")) {
                  ;
            }

            var10000 /= 255.0F;
            int var10001 = var0 >> ((7 << 3 ^ 43 | 5) & 1 ^ 9);
            if ((((1377956078 >> 2 >> 2 | 62254262) ^ 113081437) >>> 2 ^ 4366584) == 0) {
                  ;
            }

            float var1 = (float)(var10001 & ((83 & 14) >> 3 >>> 4 << 2 ^ 255)) / 255.0F;
            float var10002 = (float)(var0 & ((92 & 33 | 1189837284) ^ 1189837083)) / 255.0F;
            int var10003 = var0 >> (19 >>> 1 >> 2 & 0 ^ 24);
            if (!"please dont crack my plugin".equals("shitted on you harder than archybot")) {
                  ;
            }

            GlStateManager.color(var10000, var1, var10002, (float)(var10003 & ((211 & 118 ^ 53 ^ 0 | 2) ^ 152)) / 255.0F);
      }

      public static void drawSelectionBoundingBox(AxisAlignedBB var0) {
            if (((528761973 << 1 << 3 & 411734796 | 86254706) << 4 ^ -767396064) == 0) {
                  ;
            }

            Tessellator var1 = Tessellator.getInstance();
            BufferBuilder var2 = var1.getBuffer();
            var2.begin((0 >> 4 << 3 | 80092050) >>> 4 << 1 ^ 10011505, DefaultVertexFormats.POSITION);
            double var10001 = var0.minX;
            double var10002 = var0.minY;
            if ((1679922530 >>> 4 << 4 ^ 1679922528) == 0) {
                  ;
            }

            var2.pos(var10001, var10002, var0.minZ).endVertex();
            var2.pos(var0.maxX, var0.minY, var0.minZ).endVertex();
            if (!"You're so fat whenever you go to the beach the tide comes in.".equals("please take a shower")) {
                  ;
            }

            if (((1216699805 ^ 59023167) >> 1 & 18402567 ^ 16826625) == 0) {
                  ;
            }

            if (((134643757 ^ 7415815) << 1 & 179764419 ^ -1989556998) != 0) {
                  ;
            }

            var2.pos(var0.maxX, var0.minY, var0.maxZ).endVertex();
            var10001 = var0.minX;
            if (((1977345882 >> 3 | 246027102 | 184353996) ^ 251625471) == 0) {
                  ;
            }

            var2.pos(var10001, var0.minY, var0.maxZ).endVertex();
            var10001 = var0.minX;
            var10002 = var0.minY;
            double var10003 = var0.minZ;
            if (((918321335 | 702966567) >>> 3 ^ 1219929873) != 0) {
                  ;
            }

            var2.pos(var10001, var10002, var10003).endVertex();
            var1.draw();
            var2.begin((2 >> 3 >> 2 | 594008678) ^ 594008677, DefaultVertexFormats.POSITION);
            if ((10584610 >>> 2 << 3 ^ 117197776) != 0) {
                  ;
            }

            if ((1129878979 ^ 693703258 ^ 1501753108 ^ 355177254 ^ 1210935852) != 0) {
                  ;
            }

            var2.pos(var0.minX, var0.maxY, var0.minZ).endVertex();
            if (((1456938930 ^ 210918390) >>> 1 >>> 4 ^ 47327778) == 0) {
                  ;
            }

            var10001 = var0.maxX;
            var10002 = var0.maxY;
            if (!"buy a domain and everything else you need at namecheap.com".equals("i hope you catch fire ngl")) {
                  ;
            }

            var2.pos(var10001, var10002, var0.minZ).endVertex();
            var2.pos(var0.maxX, var0.maxY, var0.maxZ).endVertex();
            BufferBuilder var10000 = var2.pos(var0.minX, var0.maxY, var0.maxZ);
            if (((1022682461 ^ 464675956) >> 1 >> 2 ^ 1562254018) != 0) {
                  ;
            }

            var10000.endVertex();
            if ((134221163 >>> 1 & 19899911 ^ 1541) == 0) {
                  ;
            }

            var2.pos(var0.minX, var0.maxY, var0.minZ).endVertex();
            if (!"stringer is a good obfuscator".equals("i hope you catch fire ngl")) {
                  ;
            }

            var1.draw();
            var2.begin(0 << 1 << 4 >> 2 ^ 1731749440 ^ 1731749441, DefaultVertexFormats.POSITION);
            var10001 = var0.minX;
            var10002 = var0.minY;
            var10003 = var0.minZ;
            if ((621035680 << 3 ^ 417681475 ^ 818369859) == 0) {
                  ;
            }

            var2.pos(var10001, var10002, var10003).endVertex();
            var2.pos(var0.minX, var0.maxY, var0.minZ).endVertex();
            var2.pos(var0.maxX, var0.minY, var0.minZ).endVertex();
            var2.pos(var0.maxX, var0.maxY, var0.minZ).endVertex();
            var10001 = var0.maxX;
            var10002 = var0.minY;
            var10003 = var0.maxZ;
            if ((1618670106 << 3 >> 2 ^ -1862333988) != 0) {
                  ;
            }

            var2.pos(var10001, var10002, var10003).endVertex();
            var2.pos(var0.maxX, var0.maxY, var0.maxZ).endVertex();
            var10001 = var0.minX;
            var10002 = var0.minY;
            var10003 = var0.maxZ;
            if (((34751648 | 25490614) ^ 783742685) != 0) {
                  ;
            }

            var2.pos(var10001, var10002, var10003).endVertex();
            var2.pos(var0.minX, var0.maxY, var0.maxZ).endVertex();
            var1.draw();
            if (!"shitted on you harder than archybot".equals("minecraft")) {
                  ;
            }

      }
}
