package org.spray.infinity.mixin;

import java.util.Map;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spray.infinity.features.component.cape.CapeProvider;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;

import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.util.Identifier;

@Mixin(PlayerListEntry.class)
public class PlayerListEntryMixin {

	@Shadow
	@Final
	private GameProfile profile;
	@Shadow
	@Final
	private Map<MinecraftProfileTexture.Type, Identifier> textures;
	@Shadow
	private boolean texturesLoaded;

	@Inject(at = @At("HEAD"), method = "loadTextures()V")
	protected void onLoadTextures(CallbackInfo info) {
		if (texturesLoaded)
			return;
		CapeProvider.loadCape(this.profile, id -> {
			if (this.textures.get(MinecraftProfileTexture.Type.CAPE) == null) {
				this.textures.put(MinecraftProfileTexture.Type.CAPE, id);
			}
		});
	}

}
