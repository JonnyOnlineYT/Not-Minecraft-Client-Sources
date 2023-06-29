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
      LinkedList blocks;
      public BooleanSetting ugolek;
      public BooleanSetting lapis;
      public BooleanSetting spawner;
      public TimerUtil timer;
      public NumberSetting delay;
      public BooleanSetting gold;
      public BooleanSetting iron;
      public BooleanSetting diamond;
      public NumberSetting distance;

      public static void drawXRayBlocks(LinkedList var0, float var1) {
            GL11.glPushMatrix();
            if (((51258443 ^ 26793546) >>> 3 ^ 344918208) != 0) {
                  ;
            }

            GL11.glEnable(((1251 | 1137) ^ 1187) & 48 ^ 3058);
            GL11.glBlendFunc(((505 & 48) << 4 | 131 | 697) ^ 185, 71 >>> 3 << 4 >> 3 << 3 ^ 899);
            int var10000 = (827 & 598) >> 4 ^ 32 ^ 2849;
            if ((0 ^ 0) == 0) {
                  ;
            }

            GL11.glEnable(var10000);
            if (!"stop skidding".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                  ;
            }

            GL11.glLineWidth(1.0F);
            GL11.glDisable(3049 >>> 4 << 4 ^ 1537);
            GL11.glEnable(((52 ^ 4 | 13) << 3 | 340) << 3 ^ 1188);
            GL11.glDisable((2249 & 793 & 6) >>> 4 ^ 2929);
            GL11.glDisable((550 | 61) & 356 ^ 2932);
            WorldClient var17 = mc.world;
            if (((((1068066428 | 968949676) >>> 1 | 84703872) & 12277754) << 4 ^ 193298336) == 0) {
                  ;
            }

            WorldClient var2 = var17;
            EntityPlayerSP var18 = mc.player;
            if (((487738440 >> 4 >> 4 | 1189584) ^ -51249938) != 0) {
                  ;
            }

            EntityPlayerSP var3 = var18;
            Iterator var4 = var0.iterator();

            while(var4.hasNext()) {
                  XRayBlock var5 = (XRayBlock)var4.next();
                  if ((((1050124865 >> 4 ^ 24714984) >> 2 | 8155241) >>> 1 ^ -1619156699) != 0) {
                        ;
                  }

                  BlockPos var6 = var5.getBlockPos();
                  XRayData var7 = var5.getxRayData();
                  IBlockState var8 = var2.getBlockState(var6);
                  double var9 = var3.lastTickPosX + (var3.posX - var3.lastTickPosX) * (double)var1;
                  double var11 = var3.lastTickPosY + (var3.posY - var3.lastTickPosY) * (double)var1;
                  if (!"Some babies were dropped on their heads but you were clearly thrown at a wall.".equals("idiot")) {
                        ;
                  }

                  double var19 = var3.lastTickPosZ;
                  double var10001 = var3.posZ - var3.lastTickPosZ;
                  if ((((2057915887 | 2027270891) ^ 455798718 | 1044398839) >> 2 ^ 536204477) == 0) {
                        ;
                  }

                  double var13 = var19 + var10001 * (double)var1;
                  int var15 = (new Color(var7.getRed(), var7.getGreen(), var7.getBlue(), (216 | 146) >> 2 ^ 201)).getRGB();
                  XRay.glColor(var15);
                  AxisAlignedBB var16 = var8.getSelectedBoundingBox(var2, var6).grow(0.0020000000949949026D).offset(-var9, -var11, -var13);
                  XRay.drawOutlinedBox(var16);
            }

            if (((1369973063 >>> 2 ^ 289864436) & 4606826 ^ 272000 ^ 25760) == 0) {
                  ;
            }

            GL11.glEnable(1256 >>> 2 ^ 171 ^ 2753);
            if (!"your mom your dad the one you never had".equals("shitted on you harder than archybot")) {
                  ;
            }

            var10000 = (((497 | 18) ^ 425) << 3 & 176) >>> 1 ^ 2873;
            if (((742457384 | 582689433) ^ 788210361) == 0) {
                  ;
            }

            GL11.glEnable(var10000);
            GL11.glEnable((1133 | 185) << 2 ^ 7701);
            GL11.glDisable((1329 >>> 3 & 116) << 4 ^ 2466);
            GL11.glDisable(333 << 3 ^ 1007 ^ 1524 ^ 1875);
            GL11.glPopMatrix();
      }

      @SubscribeEvent
      public void onRenderWorldLast(RenderWorldLastEvent var1) {
            if (mc.world != null && mc.player != null) {
                  XRay.drawXRayBlocks(this.blocks, var1.getPartialTicks());
            }
      }

      public void onEnable() {
            if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("shitted on you harder than archybot")) {
                  ;
            }

            super.onEnable();
            LinkedList var10000 = this.blocks;
            if (((624238624 ^ 97989735) >> 4 >> 4 ^ 2155028) == 0) {
                  ;
            }

            var10000.clear();
      }

      public XRay() {
            super("XRay", "show ores through the blocks.", (1362314141 >>> 4 ^ 64466968) & 11417722 ^ 8650784, Module.Category.RENDER);
            if ((1222131225 >>> 4 << 3 >> 2 << 3 ^ 714129027) != 0) {
                  ;
            }

            this.blocks = new LinkedList();
            this.distance = new NumberSetting("Distance", this, 21.0D, 4.0D, 100.0D, 1.0D);
            this.delay = new NumberSetting("Delay", this, 5000.0D, 0.0D, 30000.0D, 1000.0D);
            if (((1532924188 ^ 1066474351) >>> 2 & 315566567 ^ 268640388) == 0) {
                  ;
            }

            BooleanSetting var10001 = new BooleanSetting;
            if ((339353002 >>> 2 >> 2 & 6398425 ^ 674314655) != 0) {
                  ;
            }

            var10001.<init>("Diamond", this, (boolean)((0 << 2 << 3 | 1978375319) ^ 1978375318));
            this.diamond = var10001;
            if (!"shitted on you harder than archybot".equals("please go outside")) {
                  ;
            }

            var10001 = new BooleanSetting;
            if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("please take a shower")) {
                  ;
            }

            var10001.<init>("Gold", this, (boolean)(370999699 >>> 2 >> 3 & 11435739 ^ 10512392));
            if (((41948194 << 1 | 38710100) ^ 85510708 ^ 75986676) != 0) {
                  ;
            }

            this.gold = var10001;
            this.iron = new BooleanSetting("Iron", this, (boolean)((798072467 << 3 ^ 139761497) >> 2 & 5379349 ^ 1183760));
            var10001 = new BooleanSetting;
            if (((320098586 | 6154907) >>> 2 ^ 654481234) != 0) {
                  ;
            }

            var10001.<init>("Coal", this, (boolean)(((324715911 | 167896702) >>> 4 >>> 1 & 12756227 | 2028108) ^ 14612303));
            this.ugolek = var10001;
            this.lapis = new BooleanSetting("Lazurite", this, (boolean)((168167456 | 88048881) ^ 255823089));
            var10001 = new BooleanSetting;
            if ((((1914852732 ^ 1245539958) >>> 4 | 23751678) ^ 65797118) == 0) {
                  ;
            }

            var10001.<init>("Spawner", this, (boolean)(0 >>> 3 >> 4 ^ 1));
            this.spawner = var10001;
            TimerUtil var1 = new TimerUtil;
            if (((557832 | 169667) ^ -859506490) != 0) {
                  ;
            }

            var1.<init>();
            this.timer = var1;
            int var2 = 5 >>> 3 >>> 1 ^ 8;
            if (!"You're so fat whenever you go to the beach the tide comes in.".equals("your mom your dad the one you never had")) {
                  ;
            }

            Setting[] var3 = new Setting[var2];
            var3[(113664388 | 87888671 | 5547006) ^ 134214655] = this.distance;
            int var10003 = (0 << 3 | 145559899) << 3 ^ 1164479193;
            if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("please go outside")) {
                  ;
            }

            var3[var10003] = this.delay;
            var3[(1 << 2 | 3) ^ 5] = this.diamond;
            var3[((1 << 2 >> 3 ^ 849196670) & 213238869) << 1 ^ 19597483] = this.gold;
            if (((300916644 | 203093960 | 115755637) << 4 >>> 3 & 271378991 ^ -349333700) != 0) {
                  ;
            }

            var3[(1 << 3 ^ 0) << 3 ^ 68] = this.iron;
            var3[4 >>> 2 & 0 ^ 5] = this.ugolek;
            var3[(1 | 0) >>> 3 >> 3 ^ 6] = this.lapis;
            var10003 = (2 | 0) ^ 0 ^ 5;
            if ((216098414 >> 2 >> 1 ^ -645562258) != 0) {
                  ;
            }

            var3[var10003] = this.spawner;
            this.addSettings(var3);
      }

      @SubscribeEvent
      public void onClientTick(ClientTickEvent var1) {
            Minecraft var10000 = mc;
            if (!"you probably spell youre as your".equals("intentMoment")) {
                  ;
            }

            if (var10000.world != null && mc.player != null) {
                  XRayData var9;
                  int var10002;
                  int var10003;
                  if (this.diamond.isEnabled()) {
                        var9 = new XRayData;
                        var10002 = Integer.parseInt("56");
                        var10003 = (273228162 >> 3 & 3694048 | 267817) ^ 792105;
                        if ((1714027971 >>> 4 & 35930263 ^ 35651732) == 0) {
                              ;
                        }

                        var9.<init>(var10002, var10003, Integer.parseInt("0"), Integer.parseInt("155"), Integer.parseInt("255"));
                        XRayManager.add(var9);
                  }

                  if (this.gold.isEnabled()) {
                        XRayManager.add(new XRayData(Integer.parseInt("14"), (2101745297 >>> 1 & 620258607 & 21605722 | 78) ^ 334, Integer.parseInt("0"), Integer.parseInt("255"), Integer.parseInt("0")));
                  }

                  if (this.iron.isEnabled()) {
                        XRayManager.add(new XRayData(Integer.parseInt("15"), (1979777789 << 4 >>> 4 | 22932101) ^ 123595517, Integer.parseInt("155"), Integer.parseInt("155"), Integer.parseInt("155")));
                  }

                  int var10004;
                  int var10005;
                  if (this.lapis.isEnabled()) {
                        var9 = new XRayData;
                        var10002 = Integer.parseInt("21");
                        var10003 = 524992 << 1 >>> 3 ^ 107137 ^ 174677 ^ 67684;
                        if (((849869806 >> 4 >>> 4 & 2433561) >> 1 << 3 ^ -649556848) != 0) {
                              ;
                        }

                        if (((640721726 >>> 4 ^ 31362496) << 3 ^ 502022040) == 0) {
                              ;
                        }

                        var10004 = Integer.parseInt("0");
                        var10005 = Integer.parseInt("0");
                        if (((380042041 << 3 >> 4 | 151037148) ^ -794593202) != 0) {
                              ;
                        }

                        var9.<init>(var10002, var10003, var10004, var10005, Integer.parseInt("255"));
                        if (!"ape covered in human flesh".equals("your mom your dad the one you never had")) {
                              ;
                        }

                        XRayManager.add(var9);
                  }

                  if (this.ugolek.isEnabled()) {
                        var9 = new XRayData;
                        var10002 = Integer.parseInt("16");
                        var10003 = (1590963575 ^ 291388423 | 1193840940) & 298270001 ^ 25301296;
                        if ((1157764513 << 3 << 2 ^ 1815221058) != 0) {
                              ;
                        }

                        if ((((276898840 | 114304791) >>> 2 & 78318764 | 16310745) ^ 83421661) == 0) {
                              ;
                        }

                        var9.<init>(var10002, var10003, Integer.parseInt("25"), Integer.parseInt("25"), Integer.parseInt("25"));
                        XRayManager.add(var9);
                  }

                  if (this.spawner.isEnabled()) {
                        var9 = new XRayData;
                        var10002 = Integer.parseInt("52");
                        var10003 = (360653948 ^ 206224051 | 351593925) >> 3 ^ 62832633;
                        var10004 = Integer.parseInt("155");
                        var10005 = Integer.parseInt("155");
                        if (((1986555221 ^ 1080879207) >>> 3 >>> 3 ^ 14160404) == 0) {
                              ;
                        }

                        if (((204629360 | 189923610) >> 2 & 12994250 ^ 12845130) == 0) {
                              ;
                        }

                        var9.<init>(var10002, var10003, var10004, var10005, Integer.parseInt("25"));
                        XRayManager.add(var9);
                  }

                  double var2 = this.distance.getValue();
                  if (this.timer.hasReached(this.delay.getValue())) {
                        this.blocks.clear();
                        Iterator var4 = XRayManager.xrayList.iterator();

                        while(var4.hasNext()) {
                              XRayData var5 = (XRayData)var4.next();
                              EntityPlayerSP var10 = mc.player;
                              int var10001 = var5.getId();
                              var10002 = var5.getMeta();
                              if ((143671392 ^ 3695107 ^ 33927172) != 0) {
                                    ;
                              }

                              XRayBlock var8;
                              for(Iterator var6 = XRay.findBlocksNearEntity(var10, var10001, var10002, (int)var2).iterator(); var6.hasNext(); this.blocks.add(var8)) {
                                    BlockPos var7 = (BlockPos)var6.next();
                                    var8 = new XRayBlock(var7, var5);
                                    if ((((1085712509 | 790755962) << 2 ^ 726500395) & 1701489027 ^ 84027779) == 0) {
                                          ;
                                    }
                              }
                        }

                        this.timer.reset();
                  }

            }
      }

      public static void glColor(int var0) {
            int var10000 = var0 >> (14 & 2 & 0 ^ 16);
            if (!"intentMoment".equals("please dont crack my plugin")) {
                  ;
            }

            GlStateManager.color((float)(var10000 & (127 << 2 << 2 >> 4 & 38 ^ 217)) / 255.0F, (float)(var0 >> (((3 << 1 | 1) ^ 5) & 0 ^ 8) & ((12 ^ 0 ^ 6 ^ 6 | 4) ^ 243)) / 255.0F, (float)(var0 & ((91 >> 2 | 2) ^ 15 ^ 230)) / 255.0F, (float)(var0 >> (((22 ^ 21) & 2) >> 3 ^ 24) & (193 >>> 2 & 25 & 4 ^ 255)) / 255.0F);
            if (((1932499863 | 1471903149 | 1935899628) << 1 ^ -268435458) == 0) {
                  ;
            }

      }

      public static void drawOutlinedBox(AxisAlignedBB var0) {
            GL11.glBegin((0 | 1600345540) << 4 ^ -164275135);
            double var10000 = var0.minX;
            if (!"shitted on you harder than archybot".equals("intentMoment")) {
                  ;
            }

            if ((546077573 << 2 << 4 >>> 2 << 3 << 4 ^ 1675372544) == 0) {
                  ;
            }

            GL11.glVertex3d(var10000, var0.maxY, var0.minZ);
            GL11.glVertex3d(var0.maxX, var0.maxY, var0.minZ);
            GL11.glVertex3d(var0.maxX, var0.maxY, var0.minZ);
            if ((1148738318 >>> 4 ^ 11939070 ^ 82946382) == 0) {
                  ;
            }

            GL11.glVertex3d(var0.maxX, var0.maxY, var0.maxZ);
            GL11.glVertex3d(var0.maxX, var0.maxY, var0.maxZ);
            GL11.glVertex3d(var0.minX, var0.maxY, var0.maxZ);
            GL11.glVertex3d(var0.minX, var0.maxY, var0.maxZ);
            var10000 = var0.minX;
            double var10001 = var0.maxY;
            if (!"your mom your dad the one you never had".equals("please take a shower")) {
                  ;
            }

            GL11.glVertex3d(var10000, var10001, var0.minZ);
            if (((1371621006 << 2 & 485306583) << 4 & 569754915 ^ 5308672) == 0) {
                  ;
            }

            var10000 = var0.minX;
            if (!"please take a shower".equals("please take a shower")) {
                  ;
            }

            GL11.glVertex3d(var10000, var0.minY, var0.minZ);
            if (!"your mom your dad the one you never had".equals("your mom your dad the one you never had")) {
                  ;
            }

            GL11.glVertex3d(var0.maxX, var0.minY, var0.minZ);
            if (!"please get a girlfriend and stop cracking plugins".equals("nefariousMoment")) {
                  ;
            }

            var10000 = var0.maxX;
            var10001 = var0.minY;
            if (((1805026483 | 244872242) >> 4 >>> 4 << 3 ^ 58521504) == 0) {
                  ;
            }

            GL11.glVertex3d(var10000, var10001, var0.minZ);
            GL11.glVertex3d(var0.maxX, var0.minY, var0.maxZ);
            var10000 = var0.maxX;
            var10001 = var0.minY;
            double var10002 = var0.maxZ;
            if (((2097637144 | 1412264691) >>> 1 ^ 1050131453) == 0) {
                  ;
            }

            GL11.glVertex3d(var10000, var10001, var10002);
            GL11.glVertex3d(var0.minX, var0.minY, var0.maxZ);
            if (((1158418978 >>> 4 ^ 10865008) >> 2 ^ 20791828) == 0) {
                  ;
            }

            if (!"ape covered in human flesh".equals("stringer is a good obfuscator")) {
                  ;
            }

            var10000 = var0.minX;
            if (!"nefariousMoment".equals("stringer is a good obfuscator")) {
                  ;
            }

            GL11.glVertex3d(var10000, var0.minY, var0.maxZ);
            if ((1346346677 << 1 >> 4 & 1827555058 ^ 1745240786) == 0) {
                  ;
            }

            GL11.glVertex3d(var0.minX, var0.minY, var0.minZ);
            GL11.glVertex3d(var0.minX, var0.minY, var0.minZ);
            GL11.glVertex3d(var0.minX, var0.maxY, var0.minZ);
            GL11.glVertex3d(var0.maxX, var0.minY, var0.minZ);
            if (!"i hope you catch fire ngl".equals("you probably spell youre as your")) {
                  ;
            }

            var10000 = var0.maxX;
            if ((1255006189 << 3 >> 3 >> 3 ^ 22658045) == 0) {
                  ;
            }

            GL11.glVertex3d(var10000, var0.maxY, var0.minZ);
            GL11.glVertex3d(var0.maxX, var0.minY, var0.maxZ);
            GL11.glVertex3d(var0.maxX, var0.maxY, var0.maxZ);
            if (((((370822988 ^ 450646) & 162301142 ^ 313227) >> 1 | 112967) ^ -405693905) != 0) {
                  ;
            }

            GL11.glVertex3d(var0.minX, var0.minY, var0.maxZ);
            GL11.glVertex3d(var0.minX, var0.maxY, var0.maxZ);
            GL11.glEnd();
      }

      public static LinkedList findBlocksNearEntity(EntityLivingBase var0, int var1, int var2, int var3) {
            LinkedList var10000 = new LinkedList;
            if (!"i hope you catch fire ngl".equals("ape covered in human flesh")) {
                  ;
            }

            var10000.<init>();
            LinkedList var4 = var10000;
            int var5 = (int)mc.player.posX - var3;

            while(true) {
                  if ((271720549 >> 2 >>> 3 ^ 8491267) == 0) {
                        ;
                  }

                  int var10001 = (int)mc.player.posX + var3;
                  if ((263837986 ^ 189271320 ^ 4469907 ^ 482440909) != 0) {
                        ;
                  }

                  if (var5 > var10001) {
                        return var4;
                  }

                  int var6 = (int)mc.player.posZ - var3;

                  while(true) {
                        if ((((1385376998 | 237521886) >> 4 | 80767913) >>> 3 ^ 1468437438) != 0) {
                              ;
                        }

                        if (var6 > (int)mc.player.posZ + var3) {
                              ++var5;
                              break;
                        }

                        int var7 = mc.world.getHeight(var5, var6);
                        if (((1169489022 ^ 534140290) << 1 >> 1 >> 3 ^ 533705851) != 0) {
                              ;
                        }

                        for(int var8 = (1133317004 | 924378548) ^ 951089449 ^ 1328387733; var8 <= var7; ++var8) {
                              BlockPos var9 = new BlockPos(var5, var8, var6);
                              IBlockState var10 = mc.world.getBlockState(var9);
                              if (var1 != ((1115971600 | 400881533) & 696102678 ^ -23373589) && var2 != (51546068 ^ 10172804 ^ 49085058 ^ -23413971)) {
                                    Block var13 = var10.getBlock();
                                    if ((1048580 ^ 204336183) != 0) {
                                          ;
                                    }

                                    int var11 = Block.getIdFromBlock(var13);
                                    int var12 = var10.getBlock().getMetaFromState(var10);
                                    if (var11 == var1 && var12 == var2) {
                                          var4.add(var9);
                                    }
                              } else {
                                    var4.add(var9);
                              }
                        }

                        ++var6;
                        if (!"your mom your dad the one you never had".equals("buy a domain and everything else you need at namecheap.com")) {
                              ;
                        }
                  }
            }
      }

      public static void drawSelectionBoundingBox(AxisAlignedBB var0) {
            Tessellator var1 = Tessellator.getInstance();
            BufferBuilder var2 = var1.getBuffer();
            var2.begin(1 >> 1 ^ 1456208006 ^ 1456208005, DefaultVertexFormats.POSITION);
            if (((643358680 << 1 & 144620660) >> 4 ^ -1666235419) != 0) {
                  ;
            }

            var2.pos(var0.minX, var0.minY, var0.minZ).endVertex();
            double var10001 = var0.maxX;
            double var10002 = var0.minY;
            double var10003 = var0.minZ;
            if ((685881121 >>> 2 >> 2 >> 2 ^ 3032112 ^ 9290476) == 0) {
                  ;
            }

            var2.pos(var10001, var10002, var10003).endVertex();
            var2.pos(var0.maxX, var0.minY, var0.maxZ).endVertex();
            var10001 = var0.minX;
            if ((1316729737 >> 1 >>> 4 ^ 41147804) == 0) {
                  ;
            }

            var2.pos(var10001, var0.minY, var0.maxZ).endVertex();
            var2.pos(var0.minX, var0.minY, var0.minZ).endVertex();
            var1.draw();
            var2.begin(0 >>> 4 & 558715586 ^ 3, DefaultVertexFormats.POSITION);
            if (!"nefariousMoment".equals("shitted on you harder than archybot")) {
                  ;
            }

            var10001 = var0.minX;
            if (((972402235 ^ 40484548) >>> 3 >> 1 ^ -1625201683) != 0) {
                  ;
            }

            var10002 = var0.maxY;
            var10003 = var0.minZ;
            if (!"You're so fat whenever you go to the beach the tide comes in.".equals("stringer is a good obfuscator")) {
                  ;
            }

            var2.pos(var10001, var10002, var10003).endVertex();
            var10001 = var0.maxX;
            if ((1101827987 << 2 << 4 ^ 1797514432) == 0) {
                  ;
            }

            var2.pos(var10001, var0.maxY, var0.minZ).endVertex();
            var2.pos(var0.maxX, var0.maxY, var0.maxZ).endVertex();
            if (!"please get a girlfriend and stop cracking plugins".equals("stop skidding")) {
                  ;
            }

            var2.pos(var0.minX, var0.maxY, var0.maxZ).endVertex();
            var10001 = var0.minX;
            var10002 = var0.maxY;
            var10003 = var0.minZ;
            if (((1065603152 >>> 2 & 131609622 | 92883630) ^ 130645694) == 0) {
                  ;
            }

            var2.pos(var10001, var10002, var10003).endVertex();
            var1.draw();
            if (!"i hope you catch fire ngl".equals("please go outside")) {
                  ;
            }

            var2.begin((0 | 1451071858) >> 4 ^ 90691990, DefaultVertexFormats.POSITION);
            if (!"yo mama name maurice".equals("nefariousMoment")) {
                  ;
            }

            var2.pos(var0.minX, var0.minY, var0.minZ).endVertex();
            var2.pos(var0.minX, var0.maxY, var0.minZ).endVertex();
            var10001 = var0.maxX;
            var10002 = var0.minY;
            var10003 = var0.minZ;
            if (!"yo mama name maurice".equals("shitted on you harder than archybot")) {
                  ;
            }

            var2.pos(var10001, var10002, var10003).endVertex();
            var2.pos(var0.maxX, var0.maxY, var0.minZ).endVertex();
            var2.pos(var0.maxX, var0.minY, var0.maxZ).endVertex();
            if ((137052625 >> 4 >> 3 ^ 1070723) == 0) {
                  ;
            }

            var10001 = var0.maxX;
            var10002 = var0.maxY;
            var10003 = var0.maxZ;
            if (((861707269 ^ 58550029) >> 3 & 75162510 ^ 11516596 ^ -834364820) != 0) {
                  ;
            }

            var2.pos(var10001, var10002, var10003).endVertex();
            if ((((109270298 >> 2 ^ 14238424) << 4 ^ 105649418) >> 2 ^ -1292127871) != 0) {
                  ;
            }

            if (!"Some babies were dropped on their heads but you were clearly thrown at a wall.".equals("please get a girlfriend and stop cracking plugins")) {
                  ;
            }

            var2.pos(var0.minX, var0.minY, var0.maxZ).endVertex();
            var2.pos(var0.minX, var0.maxY, var0.maxZ).endVertex();
            var1.draw();
      }
}
