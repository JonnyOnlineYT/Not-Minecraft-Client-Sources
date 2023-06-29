package org.spray.infinity.utils.render;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.HashMap;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.spray.infinity.features.component.cape.CapeProvider;
import org.spray.infinity.utils.Helper;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.texture.AbstractTexture;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.util.Identifier;

public class TextureUtil extends HashMap<String, AbstractTexture> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void bindTexture(String resourceLocation, boolean localDirectory) {
		if (!containsKey(resourceLocation)) {
			BufferedImage bufferedImage;
			AbstractTexture texture = null;
			try {
				if (localDirectory)
					bufferedImage = ImageIO.read(TextureUtil.class.getResourceAsStream(resourceLocation));
				else
					bufferedImage = ImageIO.read(new File(resourceLocation));

				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ImageIO.write(bufferedImage, "png", baos);
				byte[] bytes = baos.toByteArray();

				ByteBuffer data = BufferUtils.createByteBuffer(bytes.length).put(bytes);
				data.flip();

				texture = new NativeImageBackedTexture(NativeImage.read(data));
			} catch (IOException e) {
				e.printStackTrace();
			}
			assert texture != null;
			put(resourceLocation, texture);
		}

		RenderSystem.setShaderTexture(0, get(resourceLocation).getGlId());
	}

	public void bindTexture(String url) {
		if (!containsKey(url)) {
			downloadImageFromUrl(null, url);
		}
		RenderSystem.setShaderTexture(0, get(url).getGlId());

	}

	public void downloadImageFromUrl(Identifier id, String url) {
		try {
			AbstractTexture texture = null;

			if (!containsKey(url)) {

				if (url != null && !url.isEmpty()) {
					NativeImage image = NativeImage.read(new URL(url).openStream());

					texture = new NativeImageBackedTexture(image);
					if (id != null) {
						Helper.MC.getTextureManager().destroyTexture(id);
						Helper.MC.getTextureManager().registerTexture(id, texture);
					}
				}

				put(url, texture);
			}
		} catch (IOException ioexception) {
			ioexception.printStackTrace();
		}
	}


}
