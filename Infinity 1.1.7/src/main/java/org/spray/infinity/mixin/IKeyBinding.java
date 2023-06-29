package org.spray.infinity.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

@Mixin(KeyBinding.class)
public interface IKeyBinding {

	@Accessor
	InputUtil.Key getBoundKey();

	@Accessor
	void setPressed(boolean pressed);

}
