package net.minecraft.client.renderer.debug;

import java.util.Iterator;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class DebugRendererSolidFace implements DebugRenderer.IDebugRenderer {
   private final Minecraft field_193851_a;

   public DebugRendererSolidFace(Minecraft p_i47478_1_) {
      this.field_193851_a = p_i47478_1_;
   }

   public void render(float p_190060_1_, long p_190060_2_) {
      EntityPlayer entityplayer = this.field_193851_a.player;
      double d0 = entityplayer.lastTickPosX + (entityplayer.posX - entityplayer.lastTickPosX) * (double)p_190060_1_;
      double d1 = entityplayer.lastTickPosY + (entityplayer.posY - entityplayer.lastTickPosY) * (double)p_190060_1_;
      double d2 = entityplayer.lastTickPosZ + (entityplayer.posZ - entityplayer.lastTickPosZ) * (double)p_190060_1_;
      World world = this.field_193851_a.player.world;
      Iterable<BlockPos> iterable = BlockPos.func_191532_a(MathHelper.floor(entityplayer.posX - 6.0D), MathHelper.floor(entityplayer.posY - 6.0D), MathHelper.floor(entityplayer.posZ - 6.0D), MathHelper.floor(entityplayer.posX + 6.0D), MathHelper.floor(entityplayer.posY + 6.0D), MathHelper.floor(entityplayer.posZ + 6.0D));
      GlStateManager.enableBlend();
      GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
      GlStateManager.glLineWidth(2.0F);
      GlStateManager.disableTexture2D();
      GlStateManager.depthMask(false);
      Iterator var13 = iterable.iterator();

      while(var13.hasNext()) {
         BlockPos blockpos = (BlockPos)var13.next();
         IBlockState iblockstate = world.getBlockState(blockpos);
         if (iblockstate.getBlock() != Blocks.AIR) {
            AxisAlignedBB axisalignedbb = iblockstate.getSelectedBoundingBox(world, blockpos).expandXyz(0.002D).offset(-d0, -d1, -d2);
            double d3 = axisalignedbb.minX;
            double d4 = axisalignedbb.minY;
            double d5 = axisalignedbb.minZ;
            double d6 = axisalignedbb.maxX;
            double d7 = axisalignedbb.maxY;
            double d8 = axisalignedbb.maxZ;
            float f = 1.0F;
            float f1 = 0.0F;
            float f2 = 0.0F;
            float f3 = 0.5F;
            Tessellator tessellator5;
            BufferBuilder bufferbuilder5;
            if (iblockstate.func_193401_d(world, blockpos, EnumFacing.WEST) == BlockFaceShape.SOLID) {
               tessellator5 = Tessellator.getInstance();
               bufferbuilder5 = tessellator5.getBuffer();
               bufferbuilder5.begin(5, DefaultVertexFormats.POSITION_COLOR);
               bufferbuilder5.pos(d3, d4, d5).color(1.0F, 0.0F, 0.0F, 0.5F).endVertex();
               bufferbuilder5.pos(d3, d4, d8).color(1.0F, 0.0F, 0.0F, 0.5F).endVertex();
               bufferbuilder5.pos(d3, d7, d5).color(1.0F, 0.0F, 0.0F, 0.5F).endVertex();
               bufferbuilder5.pos(d3, d7, d8).color(1.0F, 0.0F, 0.0F, 0.5F).endVertex();
               tessellator5.draw();
            }

            if (iblockstate.func_193401_d(world, blockpos, EnumFacing.SOUTH) == BlockFaceShape.SOLID) {
               tessellator5 = Tessellator.getInstance();
               bufferbuilder5 = tessellator5.getBuffer();
               bufferbuilder5.begin(5, DefaultVertexFormats.POSITION_COLOR);
               bufferbuilder5.pos(d3, d7, d8).color(1.0F, 0.0F, 0.0F, 0.5F).endVertex();
               bufferbuilder5.pos(d3, d4, d8).color(1.0F, 0.0F, 0.0F, 0.5F).endVertex();
               bufferbuilder5.pos(d6, d7, d8).color(1.0F, 0.0F, 0.0F, 0.5F).endVertex();
               bufferbuilder5.pos(d6, d4, d8).color(1.0F, 0.0F, 0.0F, 0.5F).endVertex();
               tessellator5.draw();
            }

            if (iblockstate.func_193401_d(world, blockpos, EnumFacing.EAST) == BlockFaceShape.SOLID) {
               tessellator5 = Tessellator.getInstance();
               bufferbuilder5 = tessellator5.getBuffer();
               bufferbuilder5.begin(5, DefaultVertexFormats.POSITION_COLOR);
               bufferbuilder5.pos(d6, d4, d8).color(1.0F, 0.0F, 0.0F, 0.5F).endVertex();
               bufferbuilder5.pos(d6, d4, d5).color(1.0F, 0.0F, 0.0F, 0.5F).endVertex();
               bufferbuilder5.pos(d6, d7, d8).color(1.0F, 0.0F, 0.0F, 0.5F).endVertex();
               bufferbuilder5.pos(d6, d7, d5).color(1.0F, 0.0F, 0.0F, 0.5F).endVertex();
               tessellator5.draw();
            }

            if (iblockstate.func_193401_d(world, blockpos, EnumFacing.NORTH) == BlockFaceShape.SOLID) {
               tessellator5 = Tessellator.getInstance();
               bufferbuilder5 = tessellator5.getBuffer();
               bufferbuilder5.begin(5, DefaultVertexFormats.POSITION_COLOR);
               bufferbuilder5.pos(d6, d7, d5).color(1.0F, 0.0F, 0.0F, 0.5F).endVertex();
               bufferbuilder5.pos(d6, d4, d5).color(1.0F, 0.0F, 0.0F, 0.5F).endVertex();
               bufferbuilder5.pos(d3, d7, d5).color(1.0F, 0.0F, 0.0F, 0.5F).endVertex();
               bufferbuilder5.pos(d3, d4, d5).color(1.0F, 0.0F, 0.0F, 0.5F).endVertex();
               tessellator5.draw();
            }

            if (iblockstate.func_193401_d(world, blockpos, EnumFacing.DOWN) == BlockFaceShape.SOLID) {
               tessellator5 = Tessellator.getInstance();
               bufferbuilder5 = tessellator5.getBuffer();
               bufferbuilder5.begin(5, DefaultVertexFormats.POSITION_COLOR);
               bufferbuilder5.pos(d3, d4, d5).color(1.0F, 0.0F, 0.0F, 0.5F).endVertex();
               bufferbuilder5.pos(d6, d4, d5).color(1.0F, 0.0F, 0.0F, 0.5F).endVertex();
               bufferbuilder5.pos(d3, d4, d8).color(1.0F, 0.0F, 0.0F, 0.5F).endVertex();
               bufferbuilder5.pos(d6, d4, d8).color(1.0F, 0.0F, 0.0F, 0.5F).endVertex();
               tessellator5.draw();
            }

            if (iblockstate.func_193401_d(world, blockpos, EnumFacing.UP) == BlockFaceShape.SOLID) {
               tessellator5 = Tessellator.getInstance();
               bufferbuilder5 = tessellator5.getBuffer();
               bufferbuilder5.begin(5, DefaultVertexFormats.POSITION_COLOR);
               bufferbuilder5.pos(d3, d7, d5).color(1.0F, 0.0F, 0.0F, 0.5F).endVertex();
               bufferbuilder5.pos(d3, d7, d8).color(1.0F, 0.0F, 0.0F, 0.5F).endVertex();
               bufferbuilder5.pos(d6, d7, d5).color(1.0F, 0.0F, 0.0F, 0.5F).endVertex();
               bufferbuilder5.pos(d6, d7, d8).color(1.0F, 0.0F, 0.0F, 0.5F).endVertex();
               tessellator5.draw();
            }
         }
      }

      GlStateManager.depthMask(true);
      GlStateManager.enableTexture2D();
      GlStateManager.disableBlend();
   }
}
