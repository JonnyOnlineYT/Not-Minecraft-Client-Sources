package net.minecraft.client.renderer.debug;

import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DebugRendererNeighborsUpdate implements DebugRenderer.IDebugRenderer {
   private final Minecraft field_191554_a;
   private final Map<Long, Map<BlockPos, Integer>> field_191555_b = Maps.newTreeMap(Ordering.natural().reverse());

   DebugRendererNeighborsUpdate(Minecraft p_i47365_1_) {
      this.field_191554_a = p_i47365_1_;
   }

   public void func_191553_a(long p_191553_1_, BlockPos p_191553_3_) {
      Map<BlockPos, Integer> map = (Map)this.field_191555_b.get(p_191553_1_);
      if (map == null) {
         map = Maps.newHashMap();
         this.field_191555_b.put(p_191553_1_, map);
      }

      Integer integer = (Integer)((Map)map).get(p_191553_3_);
      if (integer == null) {
         integer = 0;
      }

      ((Map)map).put(p_191553_3_, integer + 1);
   }

   public void render(float p_190060_1_, long p_190060_2_) {
      long i = this.field_191554_a.world.getTotalWorldTime();
      EntityPlayer entityplayer = this.field_191554_a.player;
      double d0 = entityplayer.lastTickPosX + (entityplayer.posX - entityplayer.lastTickPosX) * (double)p_190060_1_;
      double d1 = entityplayer.lastTickPosY + (entityplayer.posY - entityplayer.lastTickPosY) * (double)p_190060_1_;
      double d2 = entityplayer.lastTickPosZ + (entityplayer.posZ - entityplayer.lastTickPosZ) * (double)p_190060_1_;
      World world = this.field_191554_a.player.world;
      GlStateManager.enableBlend();
      GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
      GlStateManager.glLineWidth(2.0F);
      GlStateManager.disableTexture2D();
      GlStateManager.depthMask(false);
      int j = true;
      double d3 = 0.0025D;
      Set<BlockPos> set = Sets.newHashSet();
      Map<BlockPos, Integer> map = Maps.newHashMap();
      Iterator iterator = this.field_191555_b.entrySet().iterator();

      while(true) {
         while(iterator.hasNext()) {
            Entry<Long, Map<BlockPos, Integer>> entry = (Entry)iterator.next();
            Long olong = (Long)entry.getKey();
            Map<BlockPos, Integer> map1 = (Map)entry.getValue();
            long k = i - olong;
            if (k > 200L) {
               iterator.remove();
            } else {
               Iterator var25 = map1.entrySet().iterator();

               while(var25.hasNext()) {
                  Entry<BlockPos, Integer> entry1 = (Entry)var25.next();
                  BlockPos blockpos = (BlockPos)entry1.getKey();
                  Integer integer = (Integer)entry1.getValue();
                  if (set.add(blockpos)) {
                     RenderGlobal.drawSelectionBoundingBox((new AxisAlignedBB(BlockPos.ORIGIN)).expandXyz(0.002D).contract(0.0025D * (double)k).offset((double)blockpos.getX(), (double)blockpos.getY(), (double)blockpos.getZ()).offset(-d0, -d1, -d2), 1.0F, 1.0F, 1.0F, 1.0F);
                     map.put(blockpos, integer);
                  }
               }
            }
         }

         Iterator var32 = map.entrySet().iterator();

         while(var32.hasNext()) {
            Entry<BlockPos, Integer> entry2 = (Entry)var32.next();
            BlockPos blockpos1 = (BlockPos)entry2.getKey();
            Integer integer1 = (Integer)entry2.getValue();
            DebugRenderer.func_191556_a(String.valueOf(integer1), blockpos1.getX(), blockpos1.getY(), blockpos1.getZ(), p_190060_1_, -1);
         }

         GlStateManager.depthMask(true);
         GlStateManager.enableTexture2D();
         GlStateManager.disableBlend();
         return;
      }
   }
}
