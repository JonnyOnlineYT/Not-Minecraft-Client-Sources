package net.minecraft.client.renderer;

import net.minecraft.client.renderer.block.model.ModelNamespaced;
import net.minecraft.item.ItemStack;

public interface ItemMeshDefinition
{
    ModelNamespaced getModelLocation(ItemStack stack);
}
