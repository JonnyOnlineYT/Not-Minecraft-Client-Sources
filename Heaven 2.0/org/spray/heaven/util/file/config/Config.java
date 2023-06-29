package org.spray.heaven.util.file.config;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

import org.spray.heaven.features.module.Module;
import org.spray.heaven.main.Wrapper;
import org.spray.heaven.notifications.Notification.Type;
import org.spray.heaven.util.AES;
import org.spray.heaven.util.file.FileManager;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mojang.realmsclient.gui.ChatFormatting;

public class Config {

	private String name;
	private String author;
	private String date;
	private File configFile;

	public Config(File configFile) {
		this.name = "";
		this.configFile = configFile;
		loadConfig();
	}

	public Config(String name, String author, String date) {
		this.name = name;
		this.author = author;
		this.date = date;
		this.configFile = FileManager.createJsonFile(FileManager.configDir, name);
	}

	public void load() {
		try {
			String text = FileManager.readFile(configFile.getAbsolutePath(), StandardCharsets.UTF_8);

			if (text.isEmpty())
				return;

			JsonObject configurationObject = new GsonBuilder().create().fromJson(text, JsonObject.class);

			if (configurationObject == null)
				return;

			for (Map.Entry<String, JsonElement> entry : configurationObject.entrySet()) {
				if (entry.getValue() instanceof JsonObject) {

					JsonObject jsonObject = (JsonObject) entry.getValue();

					for (Module module : Wrapper.getModule().getModules().values()) {

						if (!module.getName().equalsIgnoreCase(entry.getKey()))
							continue;

						if (jsonObject.get("Enabled").getAsBoolean())
							module.enable();
						else if (module.isEnabled())
							module.disable();

						module.setVisible(jsonObject.get("Visible").getAsBoolean());
						module.setKey(jsonObject.get("Key").getAsInt());
						module.getSettings().forEach(setting -> setting.load(module.getSettings(), jsonObject));
					}
				}
			}

			Wrapper.getNotification().getNotifications().clear();

			Wrapper.notify(getName() + " config " + ChatFormatting.BLUE + "loaded", Type.SUCCESS);

		} catch (IOException | JsonSyntaxException ignored) {
		}
	}

	public void save() {
		try {
			if (!configFile.exists())
				configFile.createNewFile();
			JsonObject json = new JsonObject();
			JsonObject info = new JsonObject();

			String date = AES.encrypt(new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime()),
					AES.getKey());

			info.addProperty("Date", date);

			for (Module module : Wrapper.getModule().getModules().values()) {
				JsonObject dataJson = new JsonObject();
				JsonArray jsonArray = new JsonArray();
				dataJson.addProperty("Enabled", module.isEnabled());
				dataJson.addProperty("Visible", module.isVisible());
				dataJson.addProperty("Key", module.getKey());
				module.getSettings().forEach(setting -> setting.save(module.getSettings(), dataJson, jsonArray));

				json.add("Config", info);
				json.add(module.getName(), dataJson);
			}
			FileManager.saveJsonObjectToFile(json, configFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void loadConfig() {
		if (!configFile.getName().endsWith(".json"))
			return;
		name = configFile.getName().replace(".json", "");

		try {
			String text = FileManager.readFile(configFile.getAbsolutePath(), StandardCharsets.UTF_8);

			if (text.isEmpty())
				return;

			JsonObject configurationObject = new GsonBuilder().create().fromJson(text, JsonObject.class);

			if (configurationObject == null)
				return;

			for (Map.Entry<String, JsonElement> entry : configurationObject.entrySet()) {
				if (entry.getValue() instanceof JsonObject) {

					JsonObject jsonObject = (JsonObject) entry.getValue();

					if (entry.getKey().equalsIgnoreCase("Config")) {
						try {
							setDate(jsonObject.get("Date").isJsonNull() ? ""
									: AES.decrypt(jsonObject.get("Date").getAsString(), AES.getKey()));
						} catch (Exception ignored) {
						}
					}
				}
			}
		} catch (IOException | JsonSyntaxException ignored) {
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean delete() {
		if (this.configFile.exists())
			return this.configFile.delete();
		return false;
	}

	public void createFile() throws IOException {
		if (!configFile.exists())
			configFile.createNewFile();
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

}
