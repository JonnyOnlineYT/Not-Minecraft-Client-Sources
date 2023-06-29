package volcano.summer.client.commands;

import java.awt.Desktop;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ScreenShotHelper;
import volcano.summer.base.Summer;
import volcano.summer.base.manager.command.Command;

public class SS extends Command {

	public SS() {
		super("SS", "");
	}

	@Override
	public void run(String message) {
		ScreenShotHelper.saveScreenshot(Minecraft.getMinecraft().mcDataDir, Minecraft.getMinecraft().displayWidth,
				Minecraft.getMinecraft().displayHeight, Minecraft.getMinecraft().getFramebuffer());
		try {
			Thread thread = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						Summer.tellPlayer("Might Take Few Seconds.");
						String image = SS.uploadImageToImgur(SS.this.lastFileModified("screenshots").getAbsolutePath());
						Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
						if ((desktop != null) && (desktop.isSupported(Desktop.Action.BROWSE))) {
							try {
								desktop.browse(new URI("http://i.imgur.com/" + image.substring(15, 22) + ".png"));
							} catch (Exception e) {
								e.printStackTrace();
							}
							Summer.tellPlayer("Screenshot uploaded to link http://i.imgur.com/"
									+ image.substring(15, 22) + ".png");
						}
						return;
					} catch (Exception exc) {
						Summer.tellPlayer("Unable to upload image to imgur.");
					}
				}
			});
			thread.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String uploadImageToImgur(String fileLocation) throws Exception {
		URL imgurApi = new URL("https://api.imgur.com/3/image");
		HttpURLConnection connection = (HttpURLConnection) imgurApi.openConnection();
		BufferedImage image = null;
		File file = new File(fileLocation);
		image = ImageIO.read(file);
		ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
		ImageIO.write(image, "png", byteArray);
		byte[] byteImage = byteArray.toByteArray();
		String dataImage = Base64.encodeBase64String(byteImage);
		String data = URLEncoder.encode("image", "UTF-8") + "=" + URLEncoder.encode(dataImage, "UTF-8");
		connection.setDoOutput(true);
		connection.setDoInput(true);
		connection.setRequestMethod("POST");
		String secretKey = "8731c677af5b5a3188728f2f958f5bb0087f7128";
		String clientKey = "57e0280fe5e3a5e";
		connection.setRequestProperty("Authorization", "Client-ID " + clientKey);
		connection.setRequestMethod("POST");
		connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		connection.connect();
		StringBuilder stringBuilder = new StringBuilder();
		OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
		wr.write(data);
		wr.flush();

		BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String line;
		while ((line = rd.readLine()) != null) {
			stringBuilder.append(line).append(System.lineSeparator());
		}
		wr.close();
		rd.close();

		return stringBuilder.toString();
	}

	private File lastFileModified(String dir) {
		File fl = new File(dir);
		File[] files = fl.listFiles(new FileFilter() {
			@Override
			public boolean accept(File file) {
				return file.isFile();
			}
		});
		long lastMod = Long.MIN_VALUE;
		File choise = null;
		File[] arrayOfFile1;
		int j = (arrayOfFile1 = files).length;
		for (int i = 0; i < j; i++) {
			File file = arrayOfFile1[i];
			if (file.lastModified() > lastMod) {
				choise = file;
				lastMod = file.lastModified();
			}
		}
		return choise;
	}
}
