package org.spray.infinity.features.component.cape;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.mojang.authlib.GameProfile;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

public class CapeProvider {

	public static Map<String, Identifier> capes = new HashMap<String, Identifier>();

	public static void loadCape(GameProfile player, CapeTextureAvailableCallback callback) {
		Runnable runnable = () -> {
			try {
				URL url = new URL("https://whyuleet.ru/infinity/api/cape/profiles/" + player.getName() + ".png");
				NativeImage tex = uncrop(NativeImage.read(url.openStream()));
				NativeImageBackedTexture nIBT = new NativeImageBackedTexture(tex);
				Identifier id = MinecraftClient.getInstance().getTextureManager()
						.registerDynamicTexture("infinity" + player.getName(), nIBT);
				capes.put(player.getName(), id);
				callback.onTexAvail(id);
			} catch (Exception e) {
			}
		};
		Util.getMainWorkerExecutor().execute(runnable);
	}

	public static interface CapeTextureAvailableCallback {
		public void onTexAvail(Identifier id);
	}

	public static NativeImage uncrop(NativeImage capeImage) {
		NativeImage processed;

		if (capeImage.getWidth() % 46 == 0 && capeImage.getHeight() % 22 == 0) {
			int scale = capeImage.getWidth() / 46;
			processed = resizeCanvas(capeImage, scale * 64, scale * 32);
		} else if (capeImage.getWidth() % 22 == 0 && capeImage.getHeight() % 17 == 0) {
			int scale = capeImage.getWidth() / 22;
			processed = resizeCanvas(capeImage, scale * 64, scale * 32);
		} else if (capeImage.getWidth() % 355 == 0 && capeImage.getHeight() % 275 == 0) {
			int scale = capeImage.getWidth() / 355;
			processed = cropAndResizeCanvas(capeImage, scale * 1024, scale * 512, scale * 2, scale * 2, scale, scale);
		} else if (capeImage.getWidth() % 352 == 0 && capeImage.getHeight() % 275 == 0) {
			int scale = capeImage.getWidth() / 352;
			processed = cropAndResizeCanvas(capeImage, scale * 1024, scale * 512, 0, scale * 2, 0, scale);
		} else if (capeImage.getWidth() % 355 == 0 && capeImage.getHeight() % 272 == 0) {
			int scale = capeImage.getWidth() / 355;
			processed = cropAndResizeCanvas(capeImage, scale * 1024, scale * 512, scale * 2, 0, scale, 0);
		} else {
			processed = capeImage;
		}

		return processed;
	}

	public static NativeImage cropAndResizeCanvas(NativeImage nativeImage, int width, int height, int left, int top,
			int right, int bottom) {
		int minWidth = Math.min(nativeImage.getWidth() - left - right, width);
		int minHeight = Math.min(nativeImage.getHeight() - top - bottom, height);
		NativeImage resized = new NativeImage(width, height, true);
		for (int x = 0; x < minWidth; x++) {
			for (int y = 0; y < minHeight; y++) {
				resized.setPixelColor(x, y, nativeImage.getPixelColor(x + left, y + top));
			}
		}
		nativeImage.close();
		return resized;
	}

	public static NativeImage resizeCanvas(NativeImage nativeImage, int width, int height) {
		int minWidth = Math.min(nativeImage.getWidth(), width);
		int minHeight = Math.min(nativeImage.getHeight(), height);
		NativeImage resized = new NativeImage(width, height, true);
		for (int x = 0; x < minWidth; x++) {
			for (int y = 0; y < minHeight; y++) {
				resized.setPixelColor(x, y, nativeImage.getPixelColor(x, y));
			}
		}
		nativeImage.close();
		return resized;
	}
}
