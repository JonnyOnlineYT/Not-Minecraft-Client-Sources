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
      private transient List ENTITIES;
      private transient List TILE_ENTITIES;
      private transient List TILE_BOXES;

      public static void a(AxisAlignedBB var0, float var1, float var2, float var3, float var4) {
            if (!"Some babies were dropped on their heads but you were clearly thrown at a wall.".equals("please take a shower")) {
                  ;
            }

            Tessellator var5 = Tessellator.getInstance();
            if (((33757872 >>> 4 ^ 1584887 | 1214484) >> 4 ^ 647840500) != 0) {
                  ;
            }

            BufferBuilder var6 = var5.getBuffer();
            var6.begin((1 >>> 1 & 1030863554) >>> 1 ^ 7, DefaultVertexFormats.POSITION_TEX);
            if (((798198555 >>> 1 << 4 | 1357865725) ^ 399768270) != 0) {
                  ;
            }

            double var10001 = var0.minX;
            double var10002 = var0.minY;
            if (((356962402 >> 1 | 169853994) >> 1 ^ -1829187525) != 0) {
                  ;
            }

            BufferBuilder var10000 = var6.pos(var10001, var10002, var0.minZ);
            if (!"nefariousMoment".equals("intentMoment")) {
                  ;
            }

            var10000.color(var1, var2, var3, var4).endVertex();
            var10000 = var6.pos(var0.minX, var0.maxY, var0.minZ);
            if (!"stop skidding".equals("please go outside")) {
                  ;
            }

            var10000.color(var1, var2, var3, var4).endVertex();
            var6.pos(var0.maxX, var0.minY, var0.minZ).color(var1, var2, var3, var4).endVertex();
            if (((403309312 >> 1 << 3 | 942527563) ^ -1899329429) != 0) {
                  ;
            }

            var10001 = var0.maxX;
            var10002 = var0.maxY;
            if (((338629865 >>> 3 | 17114547) >>> 2 ^ 14776815) == 0) {
                  ;
            }

            var10000 = var6.pos(var10001, var10002, var0.minZ);
            if (!"You're so fat whenever you go to the beach the tide comes in.".equals("nefariousMoment")) {
                  ;
            }

            var10000.color(var1, var2, var3, var4).endVertex();
            var6.pos(var0.maxX, var0.minY, var0.maxZ).color(var1, var2, var3, var4).endVertex();
            var10000 = var6.pos(var0.maxX, var0.maxY, var0.maxZ);
            if ((1808468259 << 2 & 671460307 ^ 671359104) == 0) {
                  ;
            }

            var10000.color(var1, var2, var3, var4).endVertex();
            var10001 = var0.minX;
            if (!"please take a shower".equals("yo mama name maurice")) {
                  ;
            }

            var10000 = var6.pos(var10001, var0.minY, var0.maxZ);
            if (((51545504 >>> 4 | 1863089 | 1109195) ^ 934477726) != 0) {
                  ;
            }

            var10000.color(var1, var2, var3, var4).endVertex();
            var10000 = var6.pos(var0.minX, var0.maxY, var0.maxZ);
            if (((369088685 ^ 191206149) & 293527356 & 268918575 ^ -565847160) != 0) {
                  ;
            }

            var10000.color(var1, var2, var3, var4).endVertex();
            var5.draw();
            var6.begin(3 >>> 1 >>> 2 ^ 7, DefaultVertexFormats.POSITION_TEX);
            var6.pos(var0.maxX, var0.maxY, var0.minZ).color(var1, var2, var3, var4).endVertex();
            var10001 = var0.maxX;
            var10002 = var0.minY;
            if ((1577427294 << 4 << 1 << 3 ^ 1954804206) != 0) {
                  ;
            }

            var10000 = var6.pos(var10001, var10002, var0.minZ);
            if ((579413120 ^ 272161515 ^ 137293865 ^ -850658774) != 0) {
                  ;
            }

            var10000.color(var1, var2, var3, var4).endVertex();
            var10000 = var6.pos(var0.minX, var0.maxY, var0.minZ);
            if ((460177508 >> 2 >>> 2 << 1 ^ 2105000347) != 0) {
                  ;
            }

            if ((((820176883 ^ 233915268) >>> 1 >>> 4 | 8318870) >>> 1 ^ 16744411) == 0) {
                  ;
            }

            if (((1169379390 ^ 1099288254 | 9877112) << 1 >>> 4 ^ 9885663) == 0) {
                  ;
            }

            var10000 = var10000.color(var1, var2, var3, var4);
            if ((((1703927206 ^ 1485162772 ^ 1021829367) & 8653137 | 8102614) ^ 16753367) == 0) {
                  ;
            }

            var10000.endVertex();
            var6.pos(var0.minX, var0.minY, var0.minZ).color(var1, var2, var3, var4).endVertex();
            if (((100588907 >>> 4 | 488561 | 1035928) ^ 1897980967) != 0) {
                  ;
            }

            var6.pos(var0.minX, var0.maxY, var0.maxZ).color(var1, var2, var3, var4).endVertex();
            if (!"stringer is a good obfuscator".equals("your mom your dad the one you never had")) {
                  ;
            }

            if ((268516400 << 1 ^ 203551681) != 0) {
                  ;
            }

            var10000 = var6.pos(var0.minX, var0.minY, var0.maxZ).color(var1, var2, var3, var4);
            if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("intentMoment")) {
                  ;
            }

            var10000.endVertex();
            var6.pos(var0.maxX, var0.maxY, var0.maxZ).color(var1, var2, var3, var4).endVertex();
            var10001 = var0.maxX;
            if (!"stringer is a good obfuscator".equals("shitted on you harder than archybot")) {
                  ;
            }

            var10000 = var6.pos(var10001, var0.minY, var0.maxZ);
            if (((659541320 | 175340313) >> 3 ^ 23077775 ^ -211417535) != 0) {
                  ;
            }

            var10000.color(var1, var2, var3, var4).endVertex();
            var5.draw();
            int var7 = (6 & 1 | 65025923) >> 4 >>> 3 ^ 508008;
            if (!"buy a domain and everything else you need at namecheap.com".equals("please get a girlfriend and stop cracking plugins")) {
                  ;
            }

            VertexFormat var8 = DefaultVertexFormats.POSITION_TEX;
            if (((168035223 >> 2 & 22542730 | 27625) ^ 93161) == 0) {
                  ;
            }

            var6.begin(var7, var8);
            var10001 = var0.minX;
            var10002 = var0.maxY;
            if (((171647633 >>> 1 | 5909841) ^ 260393485) != 0) {
                  ;
            }

            var6.pos(var10001, var10002, var0.minZ).color(var1, var2, var3, var4).endVertex();
            if (!"buy a domain and everything else you need at namecheap.com".equals("intentMoment")) {
                  ;
            }

            var10000 = var6.pos(var0.maxX, var0.maxY, var0.minZ);
            if ((746566123 >>> 2 << 2 << 2 ^ 1753473042) != 0) {
                  ;
            }

            var10000.color(var1, var2, var3, var4).endVertex();
            if ((98340 << 4 ^ 1573440) == 0) {
                  ;
            }

            var6.pos(var0.maxX, var0.maxY, var0.maxZ).color(var1, var2, var3, var4).endVertex();
            var10001 = var0.minX;
            var10002 = var0.maxY;
            if (((564912598 >>> 4 >> 3 & 3949624) >> 4 & 627 ^ 64) == 0) {
                  ;
            }

            var10000 = var6.pos(var10001, var10002, var0.maxZ);
            if (!"shitted on you harder than archybot".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                  ;
            }

            var10000.color(var1, var2, var3, var4).endVertex();
            var10001 = var0.minX;
            var10002 = var0.maxY;
            double var10003 = var0.minZ;
            if (!"you're dogshit".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                  ;
            }

            var6.pos(var10001, var10002, var10003).color(var1, var2, var3, var4).endVertex();
            if ((1662660593 << 2 << 1 ^ -277172924) != 0) {
                  ;
            }

            var10000 = var6.pos(var0.minX, var0.maxY, var0.maxZ);
            if (((389414149 ^ 254902647) << 4 << 2 ^ 240892 ^ 1697449875) != 0) {
                  ;
            }

            var10000.color(var1, var2, var3, var4).endVertex();
            var10001 = var0.maxX;
            if (!"nefariousMoment".equals("please go outside")) {
                  ;
            }

            var10002 = var0.maxY;
            if (((524141797 >>> 2 | 42864220) ^ 131039101) == 0) {
                  ;
            }

            var6.pos(var10001, var10002, var0.maxZ).color(var1, var2, var3, var4).endVertex();
            var6.pos(var0.maxX, var0.maxY, var0.minZ).color(var1, var2, var3, var4).endVertex();
            var5.draw();
            var6.begin((1 & 0) << 2 ^ 1020176422 ^ 1020176417, DefaultVertexFormats.POSITION_TEX);
            var10001 = var0.minX;
            if (!"please get a girlfriend and stop cracking plugins".equals("please go outside")) {
                  ;
            }

            var10000 = var6.pos(var10001, var0.minY, var0.minZ);
            if ((1597093883 ^ 758292598 ^ 587806109 ^ 1359618576) == 0) {
                  ;
            }

            if (((1245380566 ^ 315596107 | 1154784969) << 2 ^ 1943534452) == 0) {
                  ;
            }

            var10000.color(var1, var2, var3, var4).endVertex();
            var10001 = var0.maxX;
            if (((1140431159 >>> 2 >> 3 >> 2 << 3 | 29724044) ^ 1688783408) != 0) {
                  ;
            }

            var10000 = var6.pos(var10001, var0.minY, var0.minZ);
            if (!"yo mama name maurice".equals("please take a shower")) {
                  ;
            }

            var10000.color(var1, var2, var3, var4).endVertex();
            var10000 = var6.pos(var0.maxX, var0.minY, var0.maxZ);
            if (!"intentMoment".equals("i hope you catch fire ngl")) {
                  ;
            }

            var10000.color(var1, var2, var3, var4).endVertex();
            var6.pos(var0.minX, var0.minY, var0.maxZ).color(var1, var2, var3, var4).endVertex();
            if ((134217728 >> 3 >>> 1 ^ -1881657159) != 0) {
                  ;
            }

            var6.pos(var0.minX, var0.minY, var0.minZ).color(var1, var2, var3, var4).endVertex();
            if (!"stop skidding".equals("stringer is a good obfuscator")) {
                  ;
            }

            var10001 = var0.minX;
            var10002 = var0.minY;
            var10003 = var0.maxZ;
            if (!"your mom your dad the one you never had".equals("intentMoment")) {
                  ;
            }

            var10000 = var6.pos(var10001, var10002, var10003);
            if ((574800504 << 1 << 2 >>> 3 ^ -45942302) != 0) {
                  ;
            }

            var10000.color(var1, var2, var3, var4).endVertex();
            var6.pos(var0.maxX, var0.minY, var0.maxZ).color(var1, var2, var3, var4).endVertex();
            var6.pos(var0.maxX, var0.minY, var0.minZ).color(var1, var2, var3, var4).endVertex();
            var5.draw();
            if (((1206432373 >>> 4 | 32098055) >>> 1 & 10251923 ^ 10249875) == 0) {
                  ;
            }

            var6.begin(2 >>> 2 >> 1 ^ 7, DefaultVertexFormats.POSITION_TEX);
            var10001 = var0.minX;
            var10002 = var0.minY;
            if (((1107508105 ^ 827635991) >>> 3 & 77968400 ^ 2111803280) != 0) {
                  ;
            }

            var6.pos(var10001, var10002, var0.minZ).color(var1, var2, var3, var4).endVertex();
            var6.pos(var0.minX, var0.maxY, var0.minZ).color(var1, var2, var3, var4).endVertex();
            var10001 = var0.minX;
            var10002 = var0.minY;
            var10003 = var0.maxZ;
            if (!"stop skidding".equals("buy a domain and everything else you need at namecheap.com")) {
                  ;
            }

            var6.pos(var10001, var10002, var10003).color(var1, var2, var3, var4).endVertex();
            var10001 = var0.minX;
            if (((622445324 >>> 2 | 72669370) << 1 ^ 447607286) == 0) {
                  ;
            }

            var6.pos(var10001, var0.maxY, var0.maxZ).color(var1, var2, var3, var4).endVertex();
            var10000 = var6.pos(var0.maxX, var0.minY, var0.maxZ);
            if (!"nefariousMoment".equals("you're dogshit")) {
                  ;
            }

            var10000.color(var1, var2, var3, var4).endVertex();
            if ((67584 ^ 67584) == 0) {
                  ;
            }

            var10001 = var0.maxX;
            if (!"stringer is a good obfuscator".equals("you probably spell youre as your")) {
                  ;
            }

            var10002 = var0.maxY;
            if (!"You're so fat whenever you go to the beach the tide comes in.".equals("your mom your dad the one you never had")) {
                  ;
            }

            var6.pos(var10001, var10002, var0.maxZ).color(var1, var2, var3, var4).endVertex();
            var10001 = var0.maxX;
            if (((77145737 ^ 18503079) >> 1 ^ 85672258) != 0) {
                  ;
            }

            var10000 = var6.pos(var10001, var0.minY, var0.minZ);
            if (((165860420 >>> 3 ^ 13181735) & 6779822 ^ 6648238) == 0) {
                  ;
            }

            var10000.color(var1, var2, var3, var4).endVertex();
            var6.pos(var0.maxX, var0.maxY, var0.minZ).color(var1, var2, var3, var4).endVertex();
            var5.draw();
            var7 = (2 | 1) >> 3 << 3 >> 4 >>> 4 ^ 7;
            var8 = DefaultVertexFormats.POSITION_TEX;
            if ((19402260 >>> 4 << 3 ^ -1639923050) != 0) {
                  ;
            }

            var6.begin(var7, var8);
            var6.pos(var0.minX, var0.maxY, var0.maxZ).color(var1, var2, var3, var4).endVertex();
            var10000 = var6.pos(var0.minX, var0.minY, var0.maxZ);
            if (!"intentMoment".equals("please get a girlfriend and stop cracking plugins")) {
                  ;
            }

            var10000.color(var1, var2, var3, var4).endVertex();
            var6.pos(var0.minX, var0.maxY, var0.minZ).color(var1, var2, var3, var4).endVertex();
            var6.pos(var0.minX, var0.minY, var0.minZ).color(var1, var2, var3, var4).endVertex();
            if (((740553613 ^ 167610798) << 4 ^ 184022731) != 0) {
                  ;
            }

            var10000 = var6.pos(var0.maxX, var0.maxY, var0.minZ);
            if (!"please get a girlfriend and stop cracking plugins".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                  ;
            }

            var10000.color(var1, var2, var3, var4).endVertex();
            if (((800713597 << 1 | 127874929) >>> 1 ^ 1563662522) != 0) {
                  ;
            }

            var10001 = var0.maxX;
            var10002 = var0.minY;
            if (!"buy a domain and everything else you need at namecheap.com".equals("shitted on you harder than archybot")) {
                  ;
            }

            var10000 = var6.pos(var10001, var10002, var0.minZ);
            if (!"i hope you catch fire ngl".equals("buy a domain and everything else you need at namecheap.com")) {
                  ;
            }

            var10000.color(var1, var2, var3, var4).endVertex();
            var10000 = var6.pos(var0.maxX, var0.maxY, var0.maxZ);
            if (!"please dont crack my plugin".equals("yo mama name maurice")) {
                  ;
            }

            var10000.color(var1, var2, var3, var4).endVertex();
            var10001 = var0.maxX;
            if (!"you're dogshit".equals("please go outside")) {
                  ;
            }

            var6.pos(var10001, var0.minY, var0.maxZ).color(var1, var2, var3, var4).endVertex();
            if (!"stringer is a good obfuscator".equals("stop skidding")) {
                  ;
            }

            var5.draw();
      }

      public void onRenderWorldLast(float var1) {
            GL11.glPushAttrib(18756 >> 2 << 4 ^ 83220);
            int var10000 = ((1426 & 81) << 3 >> 3 & 14) << 3 ^ 3553;
            if ((1374942739 << 4 >> 4 >>> 1 >> 1 >> 3 ^ 1023920) == 0) {
                  ;
            }

            GL11.glDisable(var10000);
            GL11.glDisable((585 | 207) << 2 ^ 77);
            GL11.glDisable(839 << 3 >> 1 & 958 & 192 ^ 2896);
            GL11.glEnable(((831 ^ 742) << 4 ^ 2022) >>> 3 ^ 2220);
            if ((1292419254 >> 1 & 183669402 & 32749423 ^ 8388618) == 0) {
                  ;
            }

            GL11.glBlendFunc((67 >>> 3 << 4 | 124) >> 2 ^ 829, 148 & 63 ^ 4 ^ 14 ^ 797);
            GL11.glEnable((981 >>> 2 << 2 | 391) ^ 2295);
            GL11.glLineWidth(3.0F);
            GL11.glPushMatrix();
            GL11.glTranslated(-Minecraft.getMinecraft().getRenderManager().viewerPosX, -Minecraft.getMinecraft().getRenderManager().viewerPosY, -Minecraft.getMinecraft().getRenderManager().viewerPosZ);
            if (!"Some babies were dropped on their heads but you were clearly thrown at a wall.".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                  ;
            }

            this.drawESPBoxes(var1);
            GL11.glPopMatrix();
            GL11.glPopAttrib();
      }

      @SubscribeEvent
      public void onLocalPlayerUpdate(PlayerTickEvent var1) {
            if (mc.player != null) {
                  this.TILE_BOXES.clear();
                  this.TILE_ENTITIES.clear();
                  Iterator var2 = Minecraft.getMinecraft().world.loadedTileEntityList.iterator();

                  while(true) {
                        TileEntity var3;
                        AxisAlignedBB var4;
                        while(true) {
                              WorldClient var13;
                              if (!var2.hasNext()) {
                                    this.ENTITIES.clear();
                                    var13 = Minecraft.getMinecraft().world;
                                    if (((((1803545276 | 402589773) ^ 1715376244) << 3 | 1154480633) & 1057492403 ^ -1229770922) != 0) {
                                          ;
                                    }

                                    var2 = var13.loadedEntityList.iterator();

                                    while(var2.hasNext()) {
                                          Entity var8 = (Entity)var2.next();
                                          if ((337692936 >>> 4 >>> 1 ^ -937956197) != 0) {
                                                ;
                                          }

                                          if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("stop skidding")) {
                                                ;
                                          }

                                          if (var8 instanceof EntityMinecartChest) {
                                                if ((1199677857 >>> 3 >>> 1 ^ 7077861 ^ 2093360047) != 0) {
                                                      ;
                                                }

                                                this.ENTITIES.add(var8);
                                          }
                                    }

                                    return;
                              }

                              var3 = (TileEntity)var2.next();
                              IBlockState var12;
                              Minecraft var10001;
                              if (var3 instanceof TileEntityChest) {
                                    if ((((136791715 | 134438802 | 466029) ^ 78033014 | 40838998) ^ 51996493) != 0) {
                                          ;
                                    }

                                    TileEntityChest var10 = (TileEntityChest)var3;
                                    if ((1285795363 << 4 & 2078320646 ^ -1916681672) != 0) {
                                          ;
                                    }

                                    if (var10.adjacentChestXPos == null && var10.adjacentChestZPos == null) {
                                          var13 = Minecraft.getMinecraft().world;
                                          if (((1784801062 | 960945449) ^ 1689840679 ^ -1002973975) != 0) {
                                                ;
                                          }

                                          var12 = var13.getBlockState(var10.getPos());
                                          if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                                                ;
                                          }

                                          AxisAlignedBB var14 = var12.getBoundingBox(Minecraft.getMinecraft().world, var10.getPos());
                                          if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("you probably spell youre as your")) {
                                                ;
                                          }

                                          var4 = var14.offset(var10.getPos());
                                          if (var4 != null) {
                                                if (!"stop skidding".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                                                      ;
                                                }

                                                BlockPos var6;
                                                AxisAlignedBB var7;
                                                if (var10.adjacentChestXNeg != null) {
                                                      var6 = var10.adjacentChestXNeg.getPos();
                                                      var12 = Minecraft.getMinecraft().world.getBlockState(var6);
                                                      var10001 = Minecraft.getMinecraft();
                                                      if (!"you probably spell youre as your".equals("buy a domain and everything else you need at namecheap.com")) {
                                                            ;
                                                      }

                                                      var7 = var12.getBoundingBox(var10001.world, var6).offset(var6);
                                                      var4 = var4.union(var7);
                                                      if ((33587456 << 1 << 3 >>> 4 ^ -166948037) != 0) {
                                                            ;
                                                      }
                                                } else if (var10.adjacentChestZNeg != null) {
                                                      TileEntityChest var15 = var10.adjacentChestZNeg;
                                                      if ((277611585 >>> 2 ^ 30803449 ^ 63159840 ^ 1129754511) != 0) {
                                                            ;
                                                      }

                                                      var6 = var15.getPos();
                                                      var7 = Minecraft.getMinecraft().world.getBlockState(var6).getBoundingBox(Minecraft.getMinecraft().world, var6).offset(var6);
                                                      if (((66167056 >> 2 | 12561975) >> 2 & 2457175 ^ 2456085) == 0) {
                                                            ;
                                                      }

                                                      var4 = var4.union(var7);
                                                }
                                                break;
                                          }
                                    }
                              } else {
                                    if (var3 instanceof TileEntityEnderChest) {
                                          TileEntityEnderChest var9 = (TileEntityEnderChest)var3;
                                          Minecraft var11 = Minecraft.getMinecraft();
                                          if ((1101824002 << 4 ^ 449314848) == 0) {
                                                ;
                                          }

                                          var12 = var11.world.getBlockState(var9.getPos());
                                          var10001 = Minecraft.getMinecraft();
                                          if ((135283715 ^ 43111610 ^ 176263353) == 0) {
                                                ;
                                          }

                                          var4 = var12.getBoundingBox(var10001.world, var9.getPos()).offset(var9.getPos());
                                          break;
                                    }

                                    if ((1902157024 >>> 2 << 2 ^ 1902157024) == 0) {
                                          ;
                                    }

                                    if (var3 instanceof TileEntityDropper) {
                                          TileEntityDropper var10000 = (TileEntityDropper)var3;
                                          if (!"please dont crack my plugin".equals("yo mama name maurice")) {
                                                ;
                                          }

                                          TileEntityDropper var5 = var10000;
                                          var4 = Minecraft.getMinecraft().world.getBlockState(var5.getPos()).getBoundingBox(Minecraft.getMinecraft().world, var5.getPos()).offset(var5.getPos());
                                          break;
                                    }
                              }
                        }

                        this.TILE_ENTITIES.add(var3);
                        List var16 = this.TILE_BOXES;
                        if ((289419448 >>> 2 << 3 ^ 578838896) == 0) {
                              ;
                        }

                        var16.add(var4);
                  }
            }
      }

      public ChestESP() {
            int var10003 = 403595148 >> 2 >>> 4 ^ 2223373 ^ 4314227;
            if (((1473507753 ^ 612228068) & 670817685 ^ -1095576757) != 0) {
                  ;
            }

            super("ChestESP", "see chest through the walls", var10003, Module.Category.RENDER);
            ArrayList var10001 = new ArrayList();
            if (!"you probably spell youre as your".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                  ;
            }

            this.TILE_ENTITIES = var10001;
            var10001 = new ArrayList;
            if (!"i hope you catch fire ngl".equals("shitted on you harder than archybot")) {
                  ;
            }

            var10001.<init>();
            this.ENTITIES = var10001;
            if (((539231283 ^ 67373720 | 200836431) >> 3 ^ 100602365) == 0) {
                  ;
            }

            this.TILE_BOXES = new ArrayList();
      }

      private void drawESPBoxes(float var1) {
            if ((208358903 >> 4 << 2 ^ 52089724) == 0) {
                  ;
            }

            GL11.glLineWidth(2.0F);
            int var2 = (1186484595 ^ 126611298) >>> 2 ^ 273477508;

            while(true) {
                  if ("yo mama name maurice".equals("your mom your dad the one you never had")) {
                  }

                  if (var2 >= this.TILE_ENTITIES.size()) {
                        AxisAlignedBB var6 = new AxisAlignedBB(-0.5D, 0.0D, -0.5D, 0.5D, 1.0D, 0.5D);
                        Iterator var7 = this.ENTITIES.iterator();

                        while(var7.hasNext()) {
                              Entity var4 = (Entity)var7.next();
                              GL11.glPushMatrix();
                              if (((664908666 >> 3 & 33512510) << 3 ^ -1219225863) != 0) {
                                    ;
                              }

                              Vec3d var5 = EntityUtil.getInterpolatedPos(var4, var1);
                              GL11.glTranslated(var5.x, var5.y + 0.38D, var5.z);
                              GL11.glScaled(0.66D, 0.66D, 0.66D);
                              if ((((1817125674 << 4 ^ 1434472812) >> 4 | 2085258386) ^ -44063042) == 0) {
                                    ;
                              }

                              GL11.glColor4f(0.0F, 1.0F, 0.0F, 0.3F);
                              RenderUtil.drawSolidBox(var6);
                              RenderUtil.drawOutlinedBox(var6);
                              GL11.glPopMatrix();
                        }

                        if ((152125552 >> 3 << 1 << 2 >> 3 ^ 19015694) == 0) {
                              ;
                        }

                        return;
                  }

                  TileEntity var10000 = (TileEntity)this.TILE_ENTITIES.get(var2);
                  if (((888139131 << 1 ^ 1260092636) >> 2 ^ 145828234) == 0) {
                        ;
                  }

                  label120: {
                        label121: {
                              TileEntity var3 = var10000;
                              GL11.glPushMatrix();
                              if (var3 instanceof TileEntityChest) {
                                    if ((1408026012 >>> 4 ^ 22253700 ^ 1376482394) != 0) {
                                          ;
                                    }

                                    if (((TileEntityChest)var3).getChestType() == Type.TRAP) {
                                          break label121;
                                    }
                              }

                              if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("nefariousMoment")) {
                                    ;
                              }

                              if (!(var3 instanceof TileEntityDropper)) {
                                    if (var3 instanceof TileEntityEnderChest) {
                                          GL11.glColor4f(0.0F, 1.0F, 1.0F, 0.3F);
                                    } else {
                                          if (((1122599965 >>> 1 & 540621722) << 1 & 490775637 ^ -338337279) != 0) {
                                                ;
                                          }

                                          if (var3 instanceof TileEntityChest) {
                                                if (!"buy a domain and everything else you need at namecheap.com".equals("nefariousMoment")) {
                                                      ;
                                                }

                                                GL11.glColor4f(0.0F, 1.0F, 0.0F, 0.3F);
                                          }
                                    }
                                    break label120;
                              }

                              if ((((808206 >>> 4 | 1457) ^ '隼' | 12592) ^ 29565) == 0) {
                                    ;
                              }
                        }

                        if (((345294356 | 298745642) >>> 3 ^ 45866983) == 0) {
                              ;
                        }

                        GL11.glColor4f(1.0F, 0.6F, 0.0F, 0.3F);
                  }

                  if (var2 == this.TILE_BOXES.size()) {
                        var2 = 557976096 << 2 >>> 2 ^ 503662415 ^ 1061637487;
                  }

                  if (((276247132 >>> 2 << 3 << 2 | 1475336472) ^ -1269332837) != 0) {
                        ;
                  }

                  if (var2 < this.TILE_BOXES.size()) {
                        List var8 = this.TILE_BOXES;
                        if (!"buy a domain and everything else you need at namecheap.com".equals("please get a girlfriend and stop cracking plugins")) {
                              ;
                        }

                        if (var8.get(var2) != null) {
                              if (((418786429 | 169922141) << 2 ^ 1809562100) == 0) {
                                    ;
                              }

                              RenderUtil.drawSolidBox((AxisAlignedBB)this.TILE_BOXES.get(var2));
                              if ((374810537 >>> 1 << 4 ^ -1632810636) != 0) {
                                    ;
                              }

                              RenderUtil.drawOutlinedBox((AxisAlignedBB)this.TILE_BOXES.get(var2));
                        }
                  }

                  if (((2061717519 | 497749343) & 666720600 ^ -1518459017) != 0) {
                        ;
                  }

                  GL11.glPopMatrix();
                  ++var2;
            }
      }
}
