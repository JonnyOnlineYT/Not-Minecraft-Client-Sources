package org.spray.heaven.util.file;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.spray.heaven.main.Heaven;
import org.spray.heaven.main.Wrapper;

public class FileManager {

	public static File root = new File(Wrapper.MC.mcDataDir, Heaven.NAME.toLowerCase());
	public static File configDir = new File(root, "configs");
	public static File account = createJsonFile(root, "accounts");
	public static File friend = createJsonFile(root, "friends");
	public static File macros = createJsonFile(root, "macros");
	public static File profile = createJsonFile(root, "profile");
	public static File client = createJsonFile(root, "client");
//	public static File menu = createJsonFile(root, "menu");
//	public static File hud = createJsonFile(root, "hud");

	public static void initFiles() {
		if (!root.exists())
			root.mkdir();

		if (!configDir.exists())
			configDir.mkdir();

		if (!account.exists())
			createFile(account);

		if (!friend.exists())
			createFile(friend);

		if (!macros.exists())
			createFile(macros);

		if (!profile.exists())
			createFile(profile);

		if (!client.exists())
			createFile(client);
		/*
		if (!menu.exists())
			createFile(menu);

		if (!hud.exists())
			createFile(hud);*/
	}

	public static File createJsonFile(File dir, String name) {
		return new File(dir, name + ".json");
	}

	public static void saveJsonObjectToFile(JsonObject object, File file) {
		saveJsonFile(recreateFile(file), object);
	}

	public static void createFile(File file) {
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static File recreateFile(File file) {
		if (file.exists()) {
			file.delete();
		}

		try {
			file.createNewFile();
		} catch (IOException e) {
		}

		return file;
	}

	public static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}

	public static void saveJsonFile(File file, JsonObject jsonObject) {
		try {
			FileWriter writer = new FileWriter(file);
			Throwable throwable = null;
			try {
				writer.write(new GsonBuilder().setPrettyPrinting().create().toJson(jsonObject));
			} catch (Throwable var6_9) {
				throwable = var6_9;
				throw var6_9;
			} finally {
				if (throwable != null) {
					try {
						writer.close();
					} catch (Throwable var6_8) {
						throwable.addSuppressed(var6_8);
					}
				} else {
					writer.close();
				}
			}
		} catch (IOException e) {
			file.delete();
		}
	}

	public static void unzip(String zipFilePath, String destDirectory) throws IOException {
		File destDir = new File(destDirectory);
		if (!destDir.exists()) {
			destDir.mkdir();
		}
		ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath));
		ZipEntry entry = zipIn.getNextEntry();
		// iterates over entries in the zip file
		while (entry != null) {
			String filePath = destDirectory + File.separator + entry.getName();
			if (!entry.isDirectory()) {
				// if the entry is a file, extracts it
				extractFile(zipIn, filePath);
			} else {
				// if the entry is a directory, make the directory
				File dir = new File(filePath);
				dir.mkdirs();
			}
			zipIn.closeEntry();
			entry = zipIn.getNextEntry();
		}
		zipIn.close();
	}

	private static void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
		byte[] bytesIn = new byte[4096];
		int read = 0;
		while ((read = zipIn.read(bytesIn)) != -1) {
			bos.write(bytesIn, 0, read);
		}
		bos.close();
	}

	public static void deleteDir(File dir) throws IOException {
		Files.walk(dir.toPath()).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
	}
}
