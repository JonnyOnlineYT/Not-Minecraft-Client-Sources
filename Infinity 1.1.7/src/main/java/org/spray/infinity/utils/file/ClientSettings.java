package org.spray.infinity.utils.file;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.spray.infinity.main.Infinity;
import org.spray.infinity.via.ViaFabric;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class ClientSettings {

	private File file = FileUtil.createJsonFile(Infinity.getDirection(), "client");

	public void save() {
		JsonObject json = new JsonObject();
		JsonObject jsonObject = new JsonObject();

		jsonObject.addProperty("CURRENT_CAPE", Infinity.getCape().CURRENT_NAME);
		jsonObject.addProperty("VIA", String.valueOf((int) ViaFabric.stateValue));
		json.add("ISettings", jsonObject);

		FileUtil.saveJsonObjectToFile(json, file);
	}

	public void load() {
		try {
			String text = FileUtil.readFile(file.getAbsolutePath(), StandardCharsets.UTF_8);
			if (text.isEmpty())
				return;

			JsonObject configurationObject = new GsonBuilder().create().fromJson(text, JsonObject.class);

			if (configurationObject == null)
				return;

			for (Map.Entry<String, JsonElement> entry : configurationObject.entrySet()) {
				if (entry.getValue() instanceof JsonObject) {
					JsonObject jsonObject = (JsonObject) entry.getValue();

					Infinity.getCape().setCurrent(jsonObject.get("CURRENT_CAPE").getAsString());
					ViaFabric.stateValue = jsonObject.get("VIA").getAsInt();
				}
			}

		} catch (Exception e) {

		}
	}

}
