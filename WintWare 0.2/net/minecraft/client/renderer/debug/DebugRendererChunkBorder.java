package net.minecraft.client.renderer.debug;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;

public class DebugRendererChunkBorder implements DebugRenderer.IDebugRenderer {
   private final Minecraft minecraft;

   public DebugRendererChunkBorder(Minecraft minecraftIn) {
      this.minecraft = minecraftIn;
   }

   public void render(float p_190060_1_, long p_190060_2_) {
      EntityPlayer entityplayer = this.minecraft.player;
      Tessellator tessellator = Tessellator.getInstance();
      BufferBuilder bufferbuilder = tessellator.getBuffer();
      double d0 = entityplayer.lastTickPosX + (entityplayer.posX - entityplayer.lastTickPosX) * (double)p_190060_1_;
      double d1 = entityplayer.lastTickPosY + (entityplayer.posY - entityplayer.lastTickPosY) * (double)p_190060_1_;
      double d2 = entityplayer.lastTickPosZ + (entityplayer.posZ - entityplayer.lastTickPosZ) * (double)p_190060_1_;
      double d3 = 0.0D - d1;
      double d4 = 256.0D - d1;
      GlStateManager.disableTexture2D();
      GlStateManager.disableBlend();
      double d5 = (double)(entityplayer.chunkCoordX << 4) - d0;
      double d6 = (double)(entityplayer.chunkCoordZ << 4) - d2;
      GlStateManager.glLineWidth(1.0F);
      bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);

      int k1;
      int l1;
      for(k1 = -16; k1 <= 32; k1 += 16) {
         for(l1 = -16; l1 <= 32; l1 += 16) {
            bufferbuilder.pos(d5 + (double)k1, d3, d6 + (double)l1).color(1.0F, 0.0F, 0.0F, 0.0F).endVertex();
            bufferbuilder.pos(d5 + (double)k1, d3, d6 + (double)l1).color(1.0F, 0.0F, 0.0F, 0.5F).endVertex();
            bufferbuilder.pos(d5 + (double)k1, d4, d6 + (double)l1).color(1.0F, 0.0F, 0.0F, 0.5F).endVertex();
            bufferbuilder.pos(d5 + (double)k1, d4, d6 + (double)l1).color(1.0F, 0.0F, 0.0F, 0.0F).endVertex();
         }
      }

      for(k1 = 2; k1 < 16; k1 += 2) {
         bufferbuilder.pos(d5 + (double)k1, d3, d6).color(1.0F, 1.0F, 0.0F, 0.0F).endVertex();
         bufferbuilder.pos(d5 + (double)k1, d3, d6).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();
         bufferbuilder.pos(d5 + (double)k1, d4, d6).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();
         bufferbuilder.pos(d5 + (double)k1, d4, d6).color(1.0F, 1.0F, 0.0F, 0.0F).endVertex();
         bufferbuilder.pos(d5 + (double)k1, d3, d6 + 16.0D).color(1.0F, 1.0F, 0.0F, 0.0F).endVertex();
         bufferbuilder.pos(d5 + (double)k1, d3, d6 + 16.0D).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();
         bufferbuilder.pos(d5 + (double)k1, d4, d6 + 16.0D).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();
         bufferbuilder.pos(d5 + (double)k1, d4, d6 + 16.0D).color(1.0F, 1.0F, 0.0F, 0.0F).endVertex();
      }

      for(k1 = 2; k1 < 16; k1 += 2) {
         bufferbuilder.pos(d5, d3, d6 + (double)k1).color(1.0F, 1.0F, 0.0F, 0.0F).endVertex();
         bufferbuilder.pos(d5, d3, d6 + (double)k1).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();
         bufferbuilder.pos(d5, d4, d6 + (double)k1).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();
         bufferbuilder.pos(d5, d4, d6 + (double)k1).color(1.0F, 1.0F, 0.0F, 0.0F).endVertex();
         bufferbuilder.pos(d5 + 16.0D, d3, d6 + (double)k1).color(1.0F, 1.0F, 0.0F, 0.0F).endVertex();
         bufferbuilder.pos(d5 + 16.0D, d3, d6 + (double)k1).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();
         bufferbuilder.pos(d5 + 16.0D, d4, d6 + (double)k1).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();
         bufferbuilder.pos(d5 + 16.0D, d4, d6 + (double)k1).color(1.0F, 1.0F, 0.0F, 0.0F).endVertex();
      }

      double d8;
      for(k1 = 0; k1 <= 256; k1 += 2) {
         d8 = (double)k1 - d1;
         bufferbuilder.pos(d5, d8, d6).color(1.0F, 1.0F, 0.0F, 0.0F).endVertex();
         bufferbuilder.pos(d5, d8, d6).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();
         bufferbuilder.pos(d5, d8, d6 + 16.0D).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();
         bufferbuilder.pos(d5 + 16.0D, d8, d6 + 16.0D).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();
         bufferbuilder.pos(d5 + 16.0D, d8, d6).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();
         bufferbuilder.pos(d5, d8, d6).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();
         bufferbuilder.pos(d5, d8, d6).color(1.0F, 1.0F, 0.0F, 0.0F).endVertex();
      }

      tessellator.draw();
      GlStateManager.glLineWidth(2.0F);
      bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);

      for(k1 = 0; k1 <= 16; k1 += 16) {
         for(l1 = 0; l1 <= 16; l1 += 16) {
            bufferbuilder.pos(d5 + (double)k1, d3, d6 + (double)l1).color(0.25F, 0.25F, 1.0F, 0.0F).endVertex();
            bufferbuilder.pos(d5 + (double)k1, d3, d6 + (double)l1).color(0.25F, 0.25F, 1.0F, 1.0F).endVertex();
            bufferbuilder.pos(d5 + (double)k1, d4, d6 + (double)l1).color(0.25F, 0.25F, 1.0F, 1.0F).endVertex();
            bufferbuilder.pos(d5 + (double)k1, d4, d6 + (double)l1).color(0.25F, 0.25F, 1.0F, 0.0F).endVertex();
         }
      }

      for(k1 = 0; k1 <= 256; k1 += 16) {
         d8 = (double)k1 - d1;
         bufferbuilder.pos(d5, d8, d6).color(0.25F, 0.25F, 1.0F, 0.0F).endVertex();
         bufferbuilder.pos(d5, d8, d6).color(0.25F, 0.25F, 1.0F, 1.0F).endVertex();
         bufferbuilder.pos(d5, d8, d6 + 16.0D).color(0.25F, 0.25F, 1.0F, 1.0F).endVertex();
         bufferbuilder.pos(d5 + 16.0D, d8, d6 + 16.0D).color(0.25F, 0.25F, 1.0F, 1.0F).endVertex();
         bufferbuilder.pos(d5 + 16.0D, d8, d6).color(0.25F, 0.25F, 1.0F, 1.0F).endVertex();
         bufferbuilder.pos(d5, d8, d6).color(0.25F, 0.25F, 1.0F, 1.0F).endVertex();
         bufferbuilder.pos(d5, d8, d6).color(0.25F, 0.25F, 1.0F, 0.0F).endVertex();
      }

      tessellator.draw();
      GlStateManager.glLineWidth(1.0F);
      GlStateManager.enableBlend();
      GlStateManager.enableTexture2D();
   }
}
