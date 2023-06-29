package org.spray.infinity.utils.file.config;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.spray.infinity.features.Category;
import org.spray.infinity.features.Module;
import org.spray.infinity.features.Setting;
import org.spray.infinity.main.Infinity;
import org.spray.infinity.utils.file.FileUtil;
import org.spray.infinity.utils.system.crypt.AES;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import net.minecraft.block.Block;

public class Config {

	private String name;
	private String date;
	private File configFile;

	public Config(File configFile, Module module) {
		this.name = "";
		this.configFile = configFile;
		loadConfig();
	}

	public Config(String name, String date) {
		this.name = name;
		this.date = date;
		this.configFile = new File(Infinity.getConfigManager().DIR, name + ".json");
	}

	public void save() {
		saveConfig();
	}

	public void add() {
		saveConfig();
	}

	public void load() {
		try {
			String text = FileUtil.readFile(configFile.getAbsolutePath(), StandardCharsets.UTF_8);

			if (text.isEmpty())
				return;

			JsonObject configurationObject = new GsonBuilder().create().fromJson(text, JsonObject.class);

			if (configurationObject == null)
				return;

			for (Map.Entry<String, JsonElement> entry : configurationObject.entrySet()) {
				if (entry.getValue() instanceof JsonObject) {

					JsonObject jsonObject = (JsonObject) entry.getValue();

					for (Module module : Infinity.getModuleManager().getModules()) {

						if (module.getCategory().equals(Category.HIDDEN))
							continue;

						if (!module.getName().equalsIgnoreCase(entry.getKey()))
							continue;

						boolean enabled = Boolean.valueOf(jsonObject.get("Enabled").getAsBoolean());
						if (enabled && !module.isEnabled())
							module.enable();
						else if (!enabled && module.isEnabled())
							module.disable();

						module.setVisible(jsonObject.get("Visible").getAsBoolean());
						module.setKey(jsonObject.get("Key").getAsInt());

						setSettings(jsonObject, module);
					}
				}
			}

		} catch (IOException | JsonSyntaxException e) {
		}
	}

	public void saveConfig() {
		try {
			if (!configFile.exists())
				configFile.createNewFile();
			JsonObject json = new JsonObject();
			JsonObject info = new JsonObject();

			String date = AES.encrypt(new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime()),
					AES.getKey());

			info.addProperty("Date", date);

			for (Module m : Infinity.getModuleManager().getModules()) {

				if (m.getCategory().equals(Category.HIDDEN))
					continue;

				JsonObject dataJson = new JsonObject();
				JsonArray jsonArray = new JsonArray();
				dataJson.addProperty("Enabled", Boolean.valueOf(m.isEnabled()));
				dataJson.addProperty("Visible", Boolean.valueOf(m.isVisible()));
				dataJson.addProperty("Key", Integer.valueOf(m.getKey()));
				saveSettings(m, dataJson, jsonArray);

				json.add("Config", info);
				json.add(m.getName(), dataJson);
			}
			FileUtil.saveJsonObjectToFile(json, configFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void loadConfig() {
		if (!configFile.getName().endsWith(".json"))
			return;
		name = configFile.getName().replace(".json", "");
		try {
			String text = FileUtil.readFile(configFile.getAbsolutePath(), StandardCharsets.UTF_8);

			if (text.isEmpty())
				return;

			JsonObject configurationObject = new GsonBuilder().create().fromJson(text, JsonObject.class);

			if (configurationObject == null)
				return;

			for (Map.Entry<String, JsonElement> entry : configurationObject.entrySet()) {
				if (entry.getValue() instanceof JsonObject) {

					JsonObject jsonObject = (JsonObject) entry.getValue();

					if (entry.getKey().equalsIgnoreCase("Config"))
						setDate(jsonObject.get("Date").isJsonNull() ? ""
								: AES.decrypt(jsonObject.get("Date").getAsString(), AES.getKey()));

				}
			}
		} catch (IOException | JsonSyntaxException e) {
		}
	}

	private void setSettings(JsonObject jsonObject, Module module) {
		for (Setting setting : module.getSettings()) {
			if (!setting.getModule().getName().equalsIgnoreCase(module.getName()))
				continue;

			if (jsonObject.get(setting.getName()) == null || jsonObject.get(setting.getName()).isJsonNull())
				continue;

			switch (setting.getCategory()) {
			case MODE:
				setting.setCurrentMode(jsonObject.get(setting.getName()).getAsString());
				break;
			case BOOLEAN:
				setting.setToggle(jsonObject.get(setting.getName()).getAsBoolean());
				if (setting.getColor() != null)
					if (!jsonObject.get(setting.getName()).getAsString().contains("true")
							&& !jsonObject.get(setting.getName()).getAsString().contains("false"))
						setting.setColor(jsonObject.get(setting.getName()).getAsInt());
				break;
			case VALUE_DOUBLE:
				setting.setCurrentValueDouble(jsonObject.get(setting.getName()).getAsDouble());
				break;
			case VALUE_FLOAT:
				setting.setCurrentValueFloat(jsonObject.get(setting.getName()).getAsFloat());
				break;
			case VALUE_INT:
				setting.setCurrentValueInt(jsonObject.get(setting.getName()).getAsInt());
				break;
			case COLOR:
				setting.setColor(jsonObject.get(setting.getName()).getAsInt());
				break;
			case BLOCKS:
				final JsonElement blockIds = jsonObject.get(setting.getName());
				JsonArray jsonArray = blockIds.getAsJsonArray();
				if (jsonArray == null)
					return;

				for (JsonElement jsonElement : jsonArray)
					setting.addBlockFromId(jsonElement.getAsInt());

				break;
			default:
				break;
			}
		}
	}

	private void saveSettings(Module m, JsonObject dataJson, JsonArray jsonArray) {
		List<Setting> settings = m.getSettings();

		if (settings == null)
			return;

		for (Setting setting : settings) {
			switch (setting.getCategory()) {
			case BOOLEAN:
				dataJson.addProperty(setting.getName(), setting.isToggle());
				if (setting.getColor() != null)
					dataJson.addProperty(setting.getName(), setting.getColor().getRGB());
				break;
			case VALUE_DOUBLE:
				dataJson.addProperty(setting.getName(), setting.getCurrentValueDouble());
				break;
			case VALUE_FLOAT:
				dataJson.addProperty(setting.getName(), setting.getCurrentValueFloat());
				break;
			case VALUE_INT:
				dataJson.addProperty(setting.getName(), setting.getCurrentValueInt());
				break;
			case MODE:
				dataJson.addProperty(setting.getName(), setting.getCurrentMode());
				break;
			case COLOR:
				dataJson.addProperty(setting.getName(), setting.getColor().getRGB());
				break;
			case BLOCKS:
				for (Block blocks : setting.getBlocks()) {
					jsonArray.add(Block.getRawIdFromState(blocks.getDefaultState()));
				}
				dataJson.add(setting.getName(), jsonArray);
				break;
			default:
				break;
			}
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void delete() {
		if (this.configFile.exists())
			this.configFile.delete();
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

}
