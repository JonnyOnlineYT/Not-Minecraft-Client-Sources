package me.independed.inceptice.util;

import net.minecraft.util.math.BlockPos;

public class XRayBlock {
      private XRayData xRayData;
      private BlockPos blockPos;

      public XRayBlock(BlockPos var1, XRayData var2) {
            this.blockPos = var1;
            this.xRayData = var2;
      }

      public XRayData getxRayData() {
            return this.xRayData;
      }

      public BlockPos getBlockPos() {
            return this.blockPos;
      }
}
