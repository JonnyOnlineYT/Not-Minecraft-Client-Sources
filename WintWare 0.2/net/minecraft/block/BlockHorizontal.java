package net.minecraft.block;

import com.google.common.base.Predicate;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.util.EnumFacing;

public abstract class BlockHorizontal extends Block {
   public static final PropertyDirection FACING;

   protected BlockHorizontal(Material materialIn) {
      super(materialIn);
   }

   protected BlockHorizontal(Material materialIn, MapColor colorIn) {
      super(materialIn, colorIn);
   }

   static {
      FACING = PropertyDirection.create("facing", (Predicate)EnumFacing.Plane.HORIZONTAL);
   }
}
