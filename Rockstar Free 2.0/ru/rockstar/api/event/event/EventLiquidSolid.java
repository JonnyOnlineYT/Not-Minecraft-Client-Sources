package ru.rockstar.api.event.event;

import net.minecraft.block.BlockLiquid;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import ru.rockstar.api.event.Event;

public class EventLiquidSolid extends Event {
   private final BlockLiquid blockLiquid;
   private final BlockPos pos;
   private AxisAlignedBB collision;
   
   public EventLiquidSolid(BlockLiquid blockLiquid, BlockPos pos) {
      this.blockLiquid = blockLiquid;
      this.pos = pos;
   }

   public BlockLiquid getBlock() {
      return this.blockLiquid;
   }

   public BlockPos getPos() {
      return this.pos;
   }
   
   public AxisAlignedBB setColision(AxisAlignedBB expand) {
       return this.collision;
   }
}
