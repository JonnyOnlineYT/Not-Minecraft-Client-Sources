package org.spray.infinity.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spray.infinity.features.module.misc.NameProtect;
import org.spray.infinity.main.Infinity;
import org.spray.infinity.utils.Helper;

import net.minecraft.scoreboard.ScoreboardPlayerScore;

@Mixin(ScoreboardPlayerScore.class)
public class ScoreboardPlayerScoreMixin {

	@Inject(method = "getPlayerName", at = @At("HEAD"), cancellable = true)
	private void getPlayerName(CallbackInfoReturnable<String> ci) {
		NameProtect nameProtect = ((NameProtect) Infinity.getModuleManager().get(NameProtect.class));
		if (nameProtect.isEnabled() && ci.getReturnValue() != null
				&& Helper.MC.getSession().getUsername().contains(ci.getReturnValue()))
			ci.setReturnValue(nameProtect.name.getText());
	}

}
