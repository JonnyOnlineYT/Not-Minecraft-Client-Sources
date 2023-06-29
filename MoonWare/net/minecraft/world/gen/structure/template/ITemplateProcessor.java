package net.minecraft.world.gen.structure.template;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public interface ITemplateProcessor
{
    @Nullable
    Template.BlockInfo processBlock(World worldIn, BlockPos pos, Template.BlockInfo blockInfoIn);
}
