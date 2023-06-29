package net.minecraft.client.renderer.debug;

import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;

public class DebugRendererPathfinding implements DebugRenderer.IDebugRenderer {
   private final Minecraft minecraft;
   private final Map<Integer, Path> pathMap = Maps.newHashMap();
   private final Map<Integer, Float> pathMaxDistance = Maps.newHashMap();
   private final Map<Integer, Long> creationMap = Maps.newHashMap();
   private EntityPlayer player;
   private double xo;
   private double yo;
   private double zo;

   public DebugRendererPathfinding(Minecraft minecraftIn) {
      this.minecraft = minecraftIn;
   }

   public void addPath(int p_188289_1_, Path p_188289_2_, float p_188289_3_) {
      this.pathMap.put(p_188289_1_, p_188289_2_);
      this.creationMap.put(p_188289_1_, System.currentTimeMillis());
      this.pathMaxDistance.put(p_188289_1_, p_188289_3_);
   }

   public void render(float p_190060_1_, long p_190060_2_) {
      if (!this.pathMap.isEmpty()) {
         long i = System.currentTimeMillis();
         this.player = this.minecraft.player;
         this.xo = this.player.lastTickPosX + (this.player.posX - this.player.lastTickPosX) * (double)p_190060_1_;
         this.yo = this.player.lastTickPosY + (this.player.posY - this.player.lastTickPosY) * (double)p_190060_1_;
         this.zo = this.player.lastTickPosZ + (this.player.posZ - this.player.lastTickPosZ) * (double)p_190060_1_;
         GlStateManager.pushMatrix();
         GlStateManager.enableBlend();
         GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
         GlStateManager.color(0.0F, 1.0F, 0.0F, 0.75F);
         GlStateManager.disableTexture2D();
         GlStateManager.glLineWidth(6.0F);
         Iterator var6 = this.pathMap.keySet().iterator();

         while(true) {
            Path path1;
            float f;
            PathPoint pathpoint2;
            int j;
            PathPoint pathpoint4;
            do {
               Integer integer1;
               if (!var6.hasNext()) {
                  var6 = this.pathMap.keySet().iterator();

                  while(var6.hasNext()) {
                     integer1 = (Integer)var6.next();
                     path1 = (Path)this.pathMap.get(integer1);
                     PathPoint[] var18 = path1.getClosedSet();
                     int var20 = var18.length;

                     for(j = 0; j < var20; ++j) {
                        pathpoint4 = var18[j];
                        if (this.addDistanceToPlayer(pathpoint4) <= 40.0F) {
                           DebugRenderer.renderDebugText(String.format("%s", pathpoint4.nodeType), (double)pathpoint4.xCoord + 0.5D, (double)pathpoint4.yCoord + 0.75D, (double)pathpoint4.zCoord + 0.5D, p_190060_1_, -65536);
                           DebugRenderer.renderDebugText(String.format("%.2f", pathpoint4.costMalus), (double)pathpoint4.xCoord + 0.5D, (double)pathpoint4.yCoord + 0.25D, (double)pathpoint4.zCoord + 0.5D, p_190060_1_, -65536);
                        }
                     }

                     var18 = path1.getOpenSet();
                     var20 = var18.length;

                     for(j = 0; j < var20; ++j) {
                        pathpoint4 = var18[j];
                        if (this.addDistanceToPlayer(pathpoint4) <= 40.0F) {
                           DebugRenderer.renderDebugText(String.format("%s", pathpoint4.nodeType), (double)pathpoint4.xCoord + 0.5D, (double)pathpoint4.yCoord + 0.75D, (double)pathpoint4.zCoord + 0.5D, p_190060_1_, -16776961);
                           DebugRenderer.renderDebugText(String.format("%.2f", pathpoint4.costMalus), (double)pathpoint4.xCoord + 0.5D, (double)pathpoint4.yCoord + 0.25D, (double)pathpoint4.zCoord + 0.5D, p_190060_1_, -16776961);
                        }
                     }

                     for(int k = 0; k < path1.getCurrentPathLength(); ++k) {
                        pathpoint2 = path1.getPathPointFromIndex(k);
                        if (this.addDistanceToPlayer(pathpoint2) <= 40.0F) {
                           DebugRenderer.renderDebugText(String.format("%s", pathpoint2.nodeType), (double)pathpoint2.xCoord + 0.5D, (double)pathpoint2.yCoord + 0.75D, (double)pathpoint2.zCoord + 0.5D, p_190060_1_, -1);
                           DebugRenderer.renderDebugText(String.format("%.2f", pathpoint2.costMalus), (double)pathpoint2.xCoord + 0.5D, (double)pathpoint2.yCoord + 0.25D, (double)pathpoint2.zCoord + 0.5D, p_190060_1_, -1);
                        }
                     }
                  }

                  Integer[] var15 = (Integer[])((Integer[])this.creationMap.keySet().toArray(new Integer[0]));
                  int var16 = var15.length;

                  for(int var17 = 0; var17 < var16; ++var17) {
                     Integer integer2 = var15[var17];
                     if (i - (Long)this.creationMap.get(integer2) > 20000L) {
                        this.pathMap.remove(integer2);
                        this.creationMap.remove(integer2);
                     }
                  }

                  GlStateManager.enableTexture2D();
                  GlStateManager.disableBlend();
                  GlStateManager.popMatrix();
                  return;
               }

               integer1 = (Integer)var6.next();
               path1 = (Path)this.pathMap.get(integer1);
               f = (Float)this.pathMaxDistance.get(integer1);
               this.renderPathLine(p_190060_1_, path1);
               pathpoint2 = path1.getTarget();
            } while(!(this.addDistanceToPlayer(pathpoint2) <= 40.0F));

            RenderGlobal.renderFilledBox((new AxisAlignedBB((double)((float)pathpoint2.xCoord + 0.25F), (double)((float)pathpoint2.yCoord + 0.25F), (double)pathpoint2.zCoord + 0.25D, (double)((float)pathpoint2.xCoord + 0.75F), (double)((float)pathpoint2.yCoord + 0.75F), (double)((float)pathpoint2.zCoord + 0.75F))).offset(-this.xo, -this.yo, -this.zo), 0.0F, 1.0F, 0.0F, 0.5F);

            for(j = 0; j < path1.getCurrentPathLength(); ++j) {
               pathpoint4 = path1.getPathPointFromIndex(j);
               if (this.addDistanceToPlayer(pathpoint4) <= 40.0F) {
                  float f1 = j == path1.getCurrentPathIndex() ? 1.0F : 0.0F;
                  float f2 = j == path1.getCurrentPathIndex() ? 0.0F : 1.0F;
                  RenderGlobal.renderFilledBox((new AxisAlignedBB((double)((float)pathpoint4.xCoord + 0.5F - f), (double)((float)pathpoint4.yCoord + 0.01F * (float)j), (double)((float)pathpoint4.zCoord + 0.5F - f), (double)((float)pathpoint4.xCoord + 0.5F + f), (double)((float)pathpoint4.yCoord + 0.25F + 0.01F * (float)j), (double)((float)pathpoint4.zCoord + 0.5F + f))).offset(-this.xo, -this.yo, -this.zo), f1, 0.0F, f2, 0.5F);
               }
            }
         }
      }
   }

   public void renderPathLine(float p_190067_1_, Path p_190067_2_) {
      Tessellator tessellator = Tessellator.getInstance();
      BufferBuilder bufferbuilder = tessellator.getBuffer();
      bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);

      for(int i = 0; i < p_190067_2_.getCurrentPathLength(); ++i) {
         PathPoint pathpoint = p_190067_2_.getPathPointFromIndex(i);
         if (this.addDistanceToPlayer(pathpoint) <= 40.0F) {
            float f = (float)i / (float)p_190067_2_.getCurrentPathLength() * 0.33F;
            int j = i == 0 ? 0 : MathHelper.hsvToRGB(f, 0.9F, 0.9F);
            int k = j >> 16 & 255;
            int l = j >> 8 & 255;
            int i1 = j & 255;
            bufferbuilder.pos((double)pathpoint.xCoord - this.xo + 0.5D, (double)pathpoint.yCoord - this.yo + 0.5D, (double)pathpoint.zCoord - this.zo + 0.5D).color(k, l, i1, 255).endVertex();
         }
      }

      tessellator.draw();
   }

   private float addDistanceToPlayer(PathPoint p_190066_1_) {
      return (float)(Math.abs((double)p_190066_1_.xCoord - this.player.posX) + Math.abs((double)p_190066_1_.yCoord - this.player.posY) + Math.abs((double)p_190066_1_.zCoord - this.player.posZ));
   }
}
