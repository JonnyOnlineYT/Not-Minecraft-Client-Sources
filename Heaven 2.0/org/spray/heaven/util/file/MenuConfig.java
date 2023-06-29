package org.spray.heaven.util.file;

import org.lwjgl.input.Keyboard;
import org.spray.heaven.features.module.Setting;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MenuConfig {

	private static List<Setting> settings = new ArrayList<>();

	public static Setting ANIMATION_MODE = register(
			new Setting("Draw Animation", "Lower Scale", Arrays.asList("Lower Scale", "Release Scale", "None")));
	public static Setting FEATURES_SORT = register(
			new Setting("Features Sort Mode", "Alphabet", Arrays.asList("Alphabet", "Width", "Width Reverse")));
	public static Setting COLOR = register(new Setting("Color", new Color(255, 144, 0)));

	public static Setting BACKGROUND = register(new Setting("Background", true));
	public static Setting BLUR = register(new Setting("Background Blur", false));

	public static Setting WIDTH = register(new Setting("Width", 390, 390, 624));
	public static Setting HEIGHT = register(new Setting("Height", 220, 220, 420));

	public static Setting KEY = register(new Setting("Key", Keyboard.KEY_RSHIFT));

	private static Setting register(Setting setting) {
		settings.add(setting);
		return setting;
	}

	public static List<Setting> getSettings() {
		return settings;
	}

/*	public static void load() {
		try {
			String text = FileManager.readFile(FileManager.menu.getAbsolutePath(), StandardCharsets.UTF_8);
			if (text.isEmpty())
				return;

			JsonObject configurationObject = new GsonBuilder().create().fromJson(text, JsonObject.class);

			if (configurationObject == null)
				return;

			settings.forEach(setting -> setting.load(settings, configurationObject));

		} catch (IOException | JsonSyntaxException e) {
		}
	}

	public static void save() {
		JsonObject json = new JsonObject();
		JsonArray jsonArray = new JsonArray();

		settings.forEach(setting -> setting.save(settings, json, jsonArray));

		FileManager.saveJsonObjectToFile(json, FileManager.menu);
	}*/

}
