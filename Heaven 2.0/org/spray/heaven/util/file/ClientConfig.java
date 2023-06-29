package org.spray.heaven.util.file;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.spray.heaven.features.module.Setting;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

public class ClientConfig {

	private static List<Setting> settings = new ArrayList<>();

	public static Setting GUI_BACKGROUND = register(new Setting("Main Menu Background Shader", true));

	private static Setting register(Setting setting) {
		settings.add(setting);
		return setting;
	}

	public static List<Setting> getSettings() {
		return settings;
	}

	public static void load() {
		try {
			String text = FileManager.readFile(FileManager.client.getAbsolutePath(), StandardCharsets.UTF_8);
			if (text.isEmpty())
				return;

			JsonObject jsonObject = new GsonBuilder().create().fromJson(text, JsonObject.class);

			if (jsonObject == null)
				return;

			settings.forEach(setting -> setting.load(settings, jsonObject));

		} catch (IOException | JsonSyntaxException e) {
		}
	}

	public static void save() {
		JsonObject json = new JsonObject();
		JsonArray jsonArray = new JsonArray();

		settings.forEach(setting -> setting.save(settings, json, jsonArray));

		FileManager.saveJsonObjectToFile(json, FileManager.client);
	}

}
