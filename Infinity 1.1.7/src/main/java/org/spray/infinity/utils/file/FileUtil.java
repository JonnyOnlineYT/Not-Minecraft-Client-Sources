package org.spray.infinity.utils.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

public class FileUtil {

	public static File createJsonFile(File dir, String name) {
		return new File(dir, name + ".json");
	}

	public static void saveJsonObjectToFile(JsonObject object, File file) {
		FileUtil.saveJsonFile(FileUtil.recreateFile(file), object);
	}

	public static void createFile(File file) {
		if (file.exists()) {
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

	public static FileReader createReader(File file) {
		if (file.exists()) {
			try {
				return new FileReader(file);
			} catch (FileNotFoundException e) {
			}
		}
		return null;
	}

	public static void closeReader(FileReader reader) {
		try {
			reader.close();
		} catch (IOException e) {
		}
	}

	public static void copy(File from, File to) {
		try {
			InputStream in = new FileInputStream(from);
			OutputStream out = new FileOutputStream(to);

			copy(in, out);

			in.close();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}

	public static void copy(InputStream in, OutputStream out) {
		byte[] bytes = new byte[512];
		int read;

		try {
			while ((read = in.read(bytes)) != -1)
				out.write(bytes, 0, read);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
