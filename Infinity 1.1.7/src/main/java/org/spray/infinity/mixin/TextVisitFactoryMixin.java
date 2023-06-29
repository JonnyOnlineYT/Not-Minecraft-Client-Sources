package org.spray.infinity.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spray.infinity.features.module.misc.NameProtect;
import org.spray.infinity.main.Infinity;

import net.minecraft.client.font.TextVisitFactory;

@Mixin(TextVisitFactory.class)
public class TextVisitFactoryMixin {

	@ModifyArg(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/font/TextVisitFactory;visitFormatted(Ljava/lang/String;ILnet/minecraft/text/Style;Lnet/minecraft/text/Style;Lnet/minecraft/text/CharacterVisitor;)Z", ordinal = 0), method = {
			"visitFormatted(Ljava/lang/String;ILnet/minecraft/text/Style;Lnet/minecraft/text/CharacterVisitor;)Z" }, index = 0)
	private static String adjustText(String text) {
		return ((NameProtect) Infinity.getModuleManager().get(NameProtect.class)).protect(text);
	}

}
