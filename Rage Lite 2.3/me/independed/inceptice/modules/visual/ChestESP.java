//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.visual;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import me.independed.inceptice.modules.Module;
import me.independed.inceptice.util.EntityUtil;
import me.independed.inceptice.util.RenderUtil;
import net.minecraft.block.BlockChest.Type;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityMinecartChest;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityDropper;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import org.lwjgl.opengl.GL11;

public class ChestESP extends Module {
      private transient List TILE_BOXES;
      private transient List TILE_ENTITIES = new ArrayList();
      private transient List ENTITIES = new ArrayList();

      public static void a(AxisAlignedBB var0, float var1, float var2, float var3, float var4) {
            Tessellator var5 = Tessellator.getInstance();
            BufferBuilder var10000 = var5.getBuffer();
            if (((1633374056 | 237520668) >>> 2 & 80014477 ^ 12894349) == 0) {
                  ;
            }

            BufferBuilder var6 = var10000;
            int var10001 = (3 ^ 2) & 0 ^ 7;
            if (!"You're so fat whenever you go to the beach the tide comes in.".equals("stop skidding")) {
                  ;
            }

            var6.begin(var10001, DefaultVertexFormats.POSITION_TEX);
            var6.pos(var0.minX, var0.minY, var0.minZ).color(var1, var2, var3, var4).endVertex();
            double var7 = var0.minX;
            if ((387328137 >> 2 ^ 13104178 ^ 313118491) != 0) {
                  ;
            }

            var6.pos(var7, var0.maxY, var0.minZ).color(var1, var2, var3, var4).endVertex();
            var7 = var0.maxX;
            double var10002 = var0.minY;
            if (!"stop skidding".equals("ape covered in human flesh")) {
                  ;
            }

            var10000 = var6.pos(var7, var10002, var0.minZ);
            if ((((654561694 | 531434724) & 117841724 | 63655935) ^ 131031039) == 0) {
                  ;
            }

            var10000.color(var1, var2, var3, var4).endVertex();
            if ((1360912735 >>> 1 & 589439260 & 351168930 ^ 1535609319) != 0) {
                  ;
            }

            var10000 = var6.pos(var0.maxX, var0.maxY, var0.minZ);
            if ((1928325950 << 3 ^ 1386016198 ^ -974932938) == 0) {
                  ;
            }

            var10000 = var10000.color(var1, var2, var3, var4);
            if ((((1128836051 ^ 403519419) >>> 1 ^ 323813286) >>> 4 ^ -370478065) != 0) {
                  ;
            }

            var10000.endVertex();
            var6.pos(var0.maxX, var0.minY, var0.maxZ).color(var1, var2, var3, var4).endVertex();
            if (!"please get a girlfriend and stop cracking plugins".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                  ;
            }

            var6.pos(var0.maxX, var0.maxY, var0.maxZ).color(var1, var2, var3, var4).endVertex();
            if (!"minecraft".equals("please go outside")) {
                  ;
            }

            var7 = var0.minX;
            var10002 = var0.minY;
            double var10003 = var0.maxZ;
            if (((142949297 ^ 75851344) << 3 ^ 1428898432) != 0) {
                  ;
            }

            var6.pos(var7, var10002, var10003).color(var1, var2, var3, var4).endVertex();
            if (!"i hope you catch fire ngl".equals("ape covered in human flesh")) {
                  ;
            }

            var7 = var0.minX;
            if ((1542154976 >> 3 << 4 << 2 ^ -547662080) == 0) {
                  ;
            }

            var10002 = var0.maxY;
            if ((((681313111 ^ 285274141) & 53067663 & 10461997) << 4 >>> 3 ^ 1132048) == 0) {
                  ;
            }

            var10000 = var6.pos(var7, var10002, var0.maxZ);
            if (((356394581 ^ 167439603 | 405593208) >> 2 << 4 ^ 1941142512) == 0) {
                  ;
            }

            var10000.color(var1, var2, var3, var4).endVertex();
            var5.draw();
            var10001 = ((3 | 2) ^ 1) << 1 >> 1 ^ 5;
            VertexFormat var8 = DefaultVertexFormats.POSITION_TEX;
            if (!"shitted on you harder than archybot".equals("nefariousMoment")) {
                  ;
            }

            var6.begin(var10001, var8);
            var6.pos(var0.maxX, var0.maxY, var0.minZ).color(var1, var2, var3, var4).endVertex();
            if (!"You're so fat whenever you go to the beach the tide comes in.".equals("yo mama name maurice")) {
                  ;
            }

            if ((261200568 << 3 >> 2 >>> 2 ^ 130600284) == 0) {
                  ;
            }

            var7 = var0.maxX;
            if ((((856585874 | 429965840) >>> 3 | 39603885) & 34844008 & 32229937 ^ 1455993343) != 0) {
                  ;
            }

            var10000 = var6.pos(var7, var0.minY, var0.minZ);
            if (((622862537 >>> 2 ^ 53415236) >> 1 ^ 1274667317) != 0) {
                  ;
            }

            var10000.color(var1, var2, var3, var4).endVertex();
            if (((2094820819 >>> 3 << 3 | 649544559 | 1860337040) ^ 2130673663) == 0) {
                  ;
            }

            var6.pos(var0.minX, var0.maxY, var0.minZ).color(var1, var2, var3, var4).endVertex();
            var7 = var0.minX;
            var10002 = var0.minY;
            if ((408956931 >>> 4 ^ -566388827) != 0) {
                  ;
            }

            var6.pos(var7, var10002, var0.minZ).color(var1, var2, var3, var4).endVertex();
            var7 = var0.minX;
            var10002 = var0.maxY;
            var10003 = var0.maxZ;
            if ((1720547511 >> 2 ^ 381279408 ^ -1628846929) != 0) {
                  ;
            }

            var10000 = var6.pos(var7, var10002, var10003).color(var1, var2, var3, var4);
            if ((1993305949 >>> 1 >>> 4 ^ 62290810) == 0) {
                  ;
            }

            var10000.endVertex();
            var6.pos(var0.minX, var0.minY, var0.maxZ).color(var1, var2, var3, var4).endVertex();
            var6.pos(var0.maxX, var0.maxY, var0.maxZ).color(var1, var2, var3, var4).endVertex();
            var6.pos(var0.maxX, var0.minY, var0.maxZ).color(var1, var2, var3, var4).endVertex();
            var5.draw();
            if ((8388880 >> 1 << 4 ^ 1830231134) != 0) {
                  ;
            }

            var6.begin((1 >> 4 & 567285820) >>> 2 >>> 4 ^ 7, DefaultVertexFormats.POSITION_TEX);
            if (!"ape covered in human flesh".equals("please go outside")) {
                  ;
            }

            var7 = var0.minX;
            if (((((476337557 | 416562151) ^ 161404592) & 265613869) << 2 ^ -222343532) != 0) {
                  ;
            }

            var6.pos(var7, var0.maxY, var0.minZ).color(var1, var2, var3, var4).endVertex();
            if (!"nefariousMoment".equals("nefariousMoment")) {
                  ;
            }

            var7 = var0.maxX;
            var10002 = var0.maxY;
            if ((36055040 ^ 18762957 ^ 54027469) == 0) {
                  ;
            }

            var6.pos(var7, var10002, var0.minZ).color(var1, var2, var3, var4).endVertex();
            var10000 = var6.pos(var0.maxX, var0.maxY, var0.maxZ);
            if (((2109848346 << 4 & 38139781) >> 3 >>> 3 >>> 3 ^ -534456676) != 0) {
                  ;
            }

            var10000.color(var1, var2, var3, var4).endVertex();
            var7 = var0.minX;
            var10002 = var0.maxY;
            if ((9618256 << 2 >> 3 ^ 4809128) == 0) {
                  ;
            }

            var6.pos(var7, var10002, var0.maxZ).color(var1, var2, var3, var4).endVertex();
            var6.pos(var0.minX, var0.maxY, var0.minZ).color(var1, var2, var3, var4).endVertex();
            if (((845413563 | 69426232) << 4 << 1 & 1159924714 ^ -314013783) != 0) {
                  ;
            }

            if ((((650153754 ^ 366366218) & 132550969) >> 3 ^ -1709442973) != 0) {
                  ;
            }

            var7 = var0.minX;
            if ((568500768 >> 4 >> 2 ^ 4709374 ^ -1141923970) != 0) {
                  ;
            }

            var6.pos(var7, var0.maxY, var0.maxZ).color(var1, var2, var3, var4).endVertex();
            var10000 = var6.pos(var0.maxX, var0.maxY, var0.maxZ);
            if (((75122632 >> 3 >> 3 ^ 65802 | 507057) ^ 136724888) != 0) {
                  ;
            }

            var10000 = var10000.color(var1, var2, var3, var4);
            if ((((2018530137 | 768876663) >> 2 ^ 254779531) & 183462228 ^ 4859988) == 0) {
                  ;
            }

            var10000.endVertex();
            if ((1373636748 << 3 >>> 1 << 3 << 2 ^ -268155392) == 0) {
                  ;
            }

            if ((135331840 >> 1 ^ 67665920) == 0) {
                  ;
            }

            var7 = var0.maxX;
            var10002 = var0.maxY;
            var10003 = var0.minZ;
            if (((278166070 << 4 ^ 93040516) << 3 >> 2 ^ 429398472) == 0) {
                  ;
            }

            var6.pos(var7, var10002, var10003).color(var1, var2, var3, var4).endVertex();
            if (((516519765 >> 4 & 20794695) >>> 4 << 4 ^ 19662144) == 0) {
                  ;
            }

            if ((1285669190 >> 4 & 51630020 ^ 134148) == 0) {
                  ;
            }

            var5.draw();
            var6.begin((0 & 497916103 & 1643728821) >>> 2 >> 1 ^ 7, DefaultVertexFormats.POSITION_TEX);
            if ((1821014334 >>> 2 >> 4 & 2942210 ^ -796487155) != 0) {
                  ;
            }

            var6.pos(var0.minX, var0.minY, var0.minZ).color(var1, var2, var3, var4).endVertex();
            var7 = var0.maxX;
            var10002 = var0.minY;
            if (!"i hope you catch fire ngl".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                  ;
            }

            var6.pos(var7, var10002, var0.minZ).color(var1, var2, var3, var4).endVertex();
            if (((1825030869 | 206327859) << 1 & 484608092 ^ -2003778510) != 0) {
                  ;
            }

            if (((1647100782 >> 1 | 775301430) << 4 ^ -211379344) == 0) {
                  ;
            }

            var10000 = var6.pos(var0.maxX, var0.minY, var0.maxZ);
            if (!"buy a domain and everything else you need at namecheap.com".equals("nefariousMoment")) {
                  ;
            }

            var10000.color(var1, var2, var3, var4).endVertex();
            var10000 = var6.pos(var0.minX, var0.minY, var0.maxZ);
            if (((652716955 >>> 3 >> 3 | 9261274) ^ 10477310) == 0) {
                  ;
            }

            var10000.color(var1, var2, var3, var4).endVertex();
            var6.pos(var0.minX, var0.minY, var0.minZ).color(var1, var2, var3, var4).endVertex();
            var7 = var0.minX;
            if ((((1866584618 << 1 ^ 1023611980) >>> 2 | 540372634) ^ 955608734) == 0) {
                  ;
            }

            var6.pos(var7, var0.minY, var0.maxZ).color(var1, var2, var3, var4).endVertex();
            var6.pos(var0.maxX, var0.minY, var0.maxZ).color(var1, var2, var3, var4).endVertex();
            var10000 = var6.pos(var0.maxX, var0.minY, var0.minZ);
            if ((753165940 >> 1 << 4 >>> 3 ^ 1128932461) != 0) {
                  ;
            }

            var10000.color(var1, var2, var3, var4).endVertex();
            var5.draw();
            var6.begin((1 & 0) << 2 >>> 4 ^ 7, DefaultVertexFormats.POSITION_TEX);
            var10000 = var6.pos(var0.minX, var0.minY, var0.minZ);
            if (((1176506971 ^ 440908957 | 55632292) ^ 1601695718) == 0) {
                  ;
            }

            var10000.color(var1, var2, var3, var4).endVertex();
            var6.pos(var0.minX, var0.maxY, var0.minZ).color(var1, var2, var3, var4).endVertex();
            var10000 = var6.pos(var0.minX, var0.minY, var0.maxZ).color(var1, var2, var3, var4);
            if (!"ape covered in human flesh".equals("your mom your dad the one you never had")) {
                  ;
            }

            var10000.endVertex();
            var7 = var0.minX;
            var10002 = var0.maxY;
            var10003 = var0.maxZ;
            if ((559104 >> 4 << 1 ^ 19517961) != 0) {
                  ;
            }

            var6.pos(var7, var10002, var10003).color(var1, var2, var3, var4).endVertex();
            var7 = var0.maxX;
            var10002 = var0.minY;
            if ((((1495099350 << 2 | 785156911) >> 4 | 40252186) ^ 116390911) == 0) {
                  ;
            }

            var10000 = var6.pos(var7, var10002, var0.maxZ);
            if ((577066037 >>> 3 << 1 >>> 1 ^ 944912692) != 0) {
                  ;
            }

            var10000.color(var1, var2, var3, var4).endVertex();
            var7 = var0.maxX;
            if ((813070007 << 1 >> 1 >>> 4 ^ 1077301328) != 0) {
                  ;
            }

            var6.pos(var7, var0.maxY, var0.maxZ).color(var1, var2, var3, var4).endVertex();
            var10000 = var6.pos(var0.maxX, var0.minY, var0.minZ);
            if (!"i hope you catch fire ngl".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                  ;
            }

            var10000.color(var1, var2, var3, var4).endVertex();
            var7 = var0.maxX;
            if ((11542720 >>> 1 ^ -1636042229) != 0) {
                  ;
            }

            var10000 = var6.pos(var7, var0.maxY, var0.minZ);
            if ((31073635 >> 2 >>> 1 >>> 4 ^ 20300 ^ 47086234) != 0) {
                  ;
            }

            var10000.color(var1, var2, var3, var4).endVertex();
            if (((1055267484 >>> 2 | 245681403 | 229808793) ^ -2097231017) != 0) {
                  ;
            }

            var5.draw();
            if (((704665954 | 491745873) << 4 << 3 ^ 2087747853) != 0) {
                  ;
            }

            var6.begin((4 ^ 1) >> 4 ^ 7, DefaultVertexFormats.POSITION_TEX);
            var6.pos(var0.minX, var0.maxY, var0.maxZ).color(var1, var2, var3, var4).endVertex();
            var10000 = var6.pos(var0.minX, var0.minY, var0.maxZ).color(var1, var2, var3, var4);
            if (!"stringer is a good obfuscator".equals("you probably spell youre as your")) {
                  ;
            }

            var10000.endVertex();
            var10000 = var6.pos(var0.minX, var0.maxY, var0.minZ);
            if (((394264584 << 3 | 509656700) ^ -175586606) != 0) {
                  ;
            }

            var10000.color(var1, var2, var3, var4).endVertex();
            var6.pos(var0.minX, var0.minY, var0.minZ).color(var1, var2, var3, var4).endVertex();
            var6.pos(var0.maxX, var0.maxY, var0.minZ).color(var1, var2, var3, var4).endVertex();
            var7 = var0.maxX;
            if ((((2129826067 | 1467630060) << 2 | 154062230) ^ 1370732115) != 0) {
                  ;
            }

            var10002 = var0.minY;
            if (!"i hope you catch fire ngl".equals("i hope you catch fire ngl")) {
                  ;
            }

            var6.pos(var7, var10002, var0.minZ).color(var1, var2, var3, var4).endVertex();
            var10000 = var6.pos(var0.maxX, var0.maxY, var0.maxZ);
            if ((771050983 >>> 3 ^ 12394635 ^ -1513559113) != 0) {
                  ;
            }

            var10000.color(var1, var2, var3, var4).endVertex();
            var10000 = var6.pos(var0.maxX, var0.minY, var0.maxZ);
            if (!"minecraft".equals("shitted on you harder than archybot")) {
                  ;
            }

            var10000 = var10000.color(var1, var2, var3, var4);
            if (!"Some babies were dropped on their heads but you were clearly thrown at a wall.".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                  ;
            }

            var10000.endVertex();
            var5.draw();
      }

      private void drawESPBoxes(float var1) {
            if (!"yo mama name maurice".equals("You're so fat whenever you go to the beach the tide comes in.")) {
                  ;
            }

            GL11.glLineWidth(2.0F);

            for(int var2 = (449742144 >>> 2 | 79867055 | 91585647) ^ 133692927; var2 < this.TILE_ENTITIES.size(); ++var2) {
                  label90: {
                        label89: {
                              TileEntity var3 = (TileEntity)this.TILE_ENTITIES.get(var2);
                              GL11.glPushMatrix();
                              if (var3 instanceof TileEntityChest) {
                                    if ((1956140698 << 2 >>> 3 ^ 441199437) == 0) {
                                          ;
                                    }

                                    if (((TileEntityChest)var3).getChestType() == Type.TRAP) {
                                          break label89;
                                    }
                              }

                              if (!(var3 instanceof TileEntityDropper)) {
                                    if (var3 instanceof TileEntityEnderChest) {
                                          if ((7720128 >>> 2 >> 1 ^ 200056667) != 0) {
                                                ;
                                          }

                                          if (((1014936201 << 2 | 1872240881) ^ -333067) == 0) {
                                                ;
                                          }

                                          GL11.glColor4f(0.0F, 1.0F, 1.0F, 0.3F);
                                    } else if (var3 instanceof TileEntityChest) {
                                          GL11.glColor4f(0.0F, 1.0F, 0.0F, 0.3F);
                                    }
                                    break label90;
                              }
                        }

                        GL11.glColor4f(1.0F, 0.6F, 0.0F, 0.3F);
                  }

                  List var10001 = this.TILE_BOXES;
                  if (!"nefariousMoment".equals("idiot")) {
                        ;
                  }

                  if (var2 == var10001.size()) {
                        var2 = (1609589609 ^ 222140312) >> 4 ^ 86826831;
                  }

                  int var9 = this.TILE_BOXES.size();
                  if (((337394952 >>> 2 ^ 36364566) >> 1 ^ -2006513501) != 0) {
                        ;
                  }

                  if (var2 < var9 && this.TILE_BOXES.get(var2) != null) {
                        RenderUtil.drawSolidBox((AxisAlignedBB)this.TILE_BOXES.get(var2));
                        RenderUtil.drawOutlinedBox((AxisAlignedBB)this.TILE_BOXES.get(var2));
                  }

                  if (((381495565 >>> 3 ^ 20256336) & 7603125 ^ 6292273) == 0) {
                        ;
                  }

                  GL11.glPopMatrix();
            }

            if (!"please take a shower".equals("shitted on you harder than archybot")) {
                  ;
            }

            AxisAlignedBB var10000 = new AxisAlignedBB;
            if (((((252490913 >>> 2 | 5382526) ^ 54673445) << 4 | 15244697) ^ -1452248940) != 0) {
                  ;
            }

            var10000.<init>(-0.5D, 0.0D, -0.5D, 0.5D, 1.0D, 0.5D);
            AxisAlignedBB var6 = var10000;
            Iterator var7 = this.ENTITIES.iterator();

            while(var7.hasNext()) {
                  Entity var4 = (Entity)var7.next();
                  GL11.glPushMatrix();
                  Vec3d var5 = EntityUtil.getInterpolatedPos(var4, var1);
                  double var8 = var5.x;
                  double var10 = var5.y + 0.38D;
                  if (((470231564 ^ 67520014) >> 2 ^ 100686592) == 0) {
                        ;
                  }

                  if (((1215291374 >>> 2 & 213879579) >> 4 ^ 1825828167) != 0) {
                        ;
                  }

                  GL11.glTranslated(var8, var10, var5.z);
                  GL11.glScaled(0.66D, 0.66D, 0.66D);
                  GL11.glColor4f(0.0F, 1.0F, 0.0F, 0.3F);
                  if (!"i hope you catch fire ngl".equals("minecraft")) {
                        ;
                  }

                  RenderUtil.drawSolidBox(var6);
                  RenderUtil.drawOutlinedBox(var6);
                  GL11.glPopMatrix();
            }

      }

      public void onRenderWorldLast(float var1) {
            GL11.glPushAttrib((6402 ^ 1523) & 255 ^ 24821);
            GL11.glDisable(((2131 << 4 ^ 21511) & '酹' | 8816) ^ '뺐');
            GL11.glDisable(1167 >> 2 >> 1 ^ 128 ^ 2912);
            GL11.glDisable(267 << 1 & 506 ^ 2882);
            GL11.glEnable((2478 >>> 4 | 54 | 159) ^ 2909);
            GL11.glBlendFunc((501 & 43) << 2 ^ 902, (57 | 9) & 6 ^ 771);
            GL11.glEnable((2242 ^ 1454) << 2 ^ 16016);
            GL11.glLineWidth(3.0F);
            GL11.glPushMatrix();
            double var10000 = -Minecraft.getMinecraft().getRenderManager().viewerPosX;
            Minecraft var10001 = Minecraft.getMinecraft();
            if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("stringer is a good obfuscator")) {
                  ;
            }

            GL11.glTranslated(var10000, -var10001.getRenderManager().viewerPosY, -Minecraft.getMinecraft().getRenderManager().viewerPosZ);
            if (((652784838 >> 1 | 114374565) ^ 44056868 ^ -1760854325) != 0) {
                  ;
            }

            this.drawESPBoxes(var1);
            GL11.glPopMatrix();
            GL11.glPopAttrib();
      }

      @SubscribeEvent
      public void onLocalPlayerUpdate(PlayerTickEvent var1) {
            if (mc.player != null) {
                  if (((376965887 << 1 & 674987611) >>> 4 << 3 ^ -545412371) != 0) {
                        ;
                  }

                  this.TILE_BOXES.clear();
                  this.TILE_ENTITIES.clear();
                  Iterator var2 = Minecraft.getMinecraft().world.loadedTileEntityList.iterator();

                  while(true) {
                        TileEntity var3;
                        AxisAlignedBB var4;
                        while(true) {
                              boolean var10000 = var2.hasNext();
                              if ((((219266 | 137267) >>> 1 | 26843) ^ 87665 ^ '몪') == 0) {
                                    ;
                              }

                              if (!var10000) {
                                    if (((3178568 ^ 262319) >> 2 & 145059 ^ 8225) == 0) {
                                          ;
                                    }

                                    this.ENTITIES.clear();
                                    var2 = Minecraft.getMinecraft().world.loadedEntityList.iterator();

                                    while(true) {
                                          if (((4224 | 942) & 3516 ^ 428) != 0) {
                                          }

                                          if (!var2.hasNext()) {
                                                return;
                                          }

                                          Entity var8 = (Entity)var2.next();
                                          if (var8 instanceof EntityMinecartChest) {
                                                if (((1372216226 | 368807047) << 4 ^ 1606285936) == 0) {
                                                      ;
                                                }

                                                this.ENTITIES.add(var8);
                                                if (!"Some babies were dropped on their heads but you were clearly thrown at a wall.".equals("stringer is a good obfuscator")) {
                                                      ;
                                                }
                                          }
                                    }
                              }

                              var3 = (TileEntity)var2.next();
                              IBlockState var12;
                              AxisAlignedBB var14;
                              if (var3 instanceof TileEntityChest) {
                                    if (!"nefariousMoment".equals("idiot")) {
                                          ;
                                    }

                                    TileEntityChest var10 = (TileEntityChest)var3;
                                    if (var10.adjacentChestXPos == null && var10.adjacentChestZPos == null) {
                                          var12 = Minecraft.getMinecraft().world.getBlockState(var10.getPos());
                                          WorldClient var13 = Minecraft.getMinecraft().world;
                                          BlockPos var10002 = var10.getPos();
                                          if ((553658624 >>> 4 ^ 34603664) == 0) {
                                                ;
                                          }

                                          var4 = var12.getBoundingBox(var13, var10002).offset(var10.getPos());
                                          if (var4 != null) {
                                                BlockPos var6;
                                                AxisAlignedBB var7;
                                                if (var10.adjacentChestXNeg != null) {
                                                      var6 = var10.adjacentChestXNeg.getPos();
                                                      var7 = Minecraft.getMinecraft().world.getBlockState(var6).getBoundingBox(Minecraft.getMinecraft().world, var6).offset(var6);
                                                      var4 = var4.union(var7);
                                                } else {
                                                      TileEntityChest var15 = var10.adjacentChestZNeg;
                                                      if (((16929700 | 8475637) & 24769401 ^ 3410229 ^ 20273732) == 0) {
                                                            ;
                                                      }

                                                      if (var15 != null) {
                                                            var6 = var10.adjacentChestZNeg.getPos();
                                                            if (!"please get a girlfriend and stop cracking plugins".equals("ape covered in human flesh")) {
                                                                  ;
                                                            }

                                                            Minecraft var16 = Minecraft.getMinecraft();
                                                            if ((4194324 ^ 1240722 ^ -1087607271) != 0) {
                                                                  ;
                                                            }

                                                            var7 = var16.world.getBlockState(var6).getBoundingBox(Minecraft.getMinecraft().world, var6).offset(var6);
                                                            var14 = var4.union(var7);
                                                            if (((184800930 >> 4 | 5065627) ^ 16613371) == 0) {
                                                                  ;
                                                            }

                                                            var4 = var14;
                                                      }
                                                }
                                                break;
                                          }
                                    }
                              } else {
                                    if (var3 instanceof TileEntityEnderChest) {
                                          TileEntityEnderChest var9 = (TileEntityEnderChest)var3;
                                          WorldClient var11 = Minecraft.getMinecraft().world;
                                          BlockPos var10001 = var9.getPos();
                                          if (((((1259753738 | 854530541 | 1458760236) ^ 1610579914) & 106479115) >>> 3 ^ 4224) == 0) {
                                                ;
                                          }

                                          var12 = var11.getBlockState(var10001);
                                          if (((654748495 << 1 >> 1 << 1 | 484176322) ^ 1591736286) == 0) {
                                                ;
                                          }

                                          var14 = var12.getBoundingBox(Minecraft.getMinecraft().world, var9.getPos());
                                          if (((1003040008 >> 4 >> 2 & 5064965 | 1890141) ^ 6149981) == 0) {
                                                ;
                                          }

                                          var14 = var14.offset(var9.getPos());
                                          if (((628569100 << 3 ^ 596436256 | 25165547) ^ 159383531) == 0) {
                                                ;
                                          }

                                          var4 = var14;
                                          break;
                                    }

                                    var10000 = var3 instanceof TileEntityDropper;
                                    if (((41200653 ^ 39779661) >> 2 << 1 << 3 ^ 11093248) == 0) {
                                          ;
                                    }

                                    if (var10000) {
                                          TileEntityDropper var5 = (TileEntityDropper)var3;
                                          var4 = Minecraft.getMinecraft().world.getBlockState(var5.getPos()).getBoundingBox(Minecraft.getMinecraft().world, var5.getPos()).offset(var5.getPos());
                                          if (!"nefariousMoment".equals("You're so fat whenever you go to the beach the tide comes in.")) {
                                                ;
                                          }
                                          break;
                                    }
                              }
                        }

                        this.TILE_ENTITIES.add(var3);
                        if ((742463506 << 1 << 2 >>> 1 ^ -1839591937) != 0) {
                              ;
                        }

                        this.TILE_BOXES.add(var4);
                  }
            }
      }

      public ChestESP() {
            super("ChestESP", "see chest through the walls", ((335580422 ^ 223290637) >> 3 & 25453101 ^ 565330 | 9095817) ^ 25873115, Module.Category.RENDER);
            ArrayList var10001 = new ArrayList;
            if (!"please go outside".equals("your mom your dad the one you never had")) {
                  ;
            }

            var10001.<init>();
            this.TILE_BOXES = var10001;
      }
}
