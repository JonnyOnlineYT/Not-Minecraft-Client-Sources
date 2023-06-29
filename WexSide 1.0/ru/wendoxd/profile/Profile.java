package ru.wendoxd.profile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

import javax.imageio.ImageIO;
import javax.net.ssl.HttpsURLConnection;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class Profile extends AbstractTexture {

	private final String name;
	private final String expiration;
	private final String uid;
	public ResourceLocation resourceLocation;
	private byte[] buffer;

	public Profile(String name, String uid, String expiration) {
		this.name = name;
		this.uid = uid;
		this.expiration = expiration;
	}

	public String getName() {
		return this.name;
	}

	public String getUID() {
		return this.uid;
	}

	public String getExpirationDate() {
		return this.expiration;
	}

	public ResourceLocation getResourceLocation() {
		return this.resourceLocation;
	}

	public void init() {
		resourceLocation = new ResourceLocation(String.valueOf(System.nanoTime()));
		Minecraft.getMinecraft().getTextureManager().loadTexture(resourceLocation, this);
	}

	public void avatar(byte[] buffer) {
		this.buffer = buffer;
	}

	@Override
	public void loadTexture(IResourceManager resourceManager) throws IOException {
		this.deleteGlTexture();
		TextureUtil.uploadTextureImageAllocate(this.getGlTextureId(), ImageIO.read(new ByteArrayInputStream(buffer)),
				false, false);
	}

	public static String linkToPhoto(String vkid) {
		try {
			HttpsURLConnection url = (HttpsURLConnection) new URL("https://api.vk.com/method/users.get?user_ids=" + vkid + "&fields=photo_100&access_token=3230fe4e6a879c5812d36eb5206828a0d301340b999c1b8ae0ca92c498ad3ff965900440e2fad1aae3c42&v=5.131").openConnection();
			InputStream in = url.getInputStream();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			int read;
			while ((read = in.read()) >= 0) {
				baos.write(read);
			}
			String response = baos.toString();
			char[] responseChars = response.toCharArray();
			int offset = response.indexOf("https:\\");
			StringBuilder http = new StringBuilder();
			while (offset <= 999 && responseChars[offset] != '"') {
				if (responseChars[offset] != '\\') {
					http.append(responseChars[offset]);
				}
				offset++;
			}
			return http.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
