package org.spray.infinity.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.item.CrossbowItem;

@Mixin(CrossbowItem.class)
public interface ICrossbowItem {
	
	@Accessor("loaded")
	boolean isLoaded();

}
