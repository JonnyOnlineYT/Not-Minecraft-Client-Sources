package org.spray.heaven.util.file;

import com.google.gson.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.spray.heaven.main.Wrapper;

public class MacrosConfig {

	public void load() {
		try {
			String text = FileManager.readFile(FileManager.macros.getAbsolutePath(), StandardCharsets.UTF_8);
			if (text.isEmpty())
				return;

			JsonObject configurationObject = new GsonBuilder().create().fromJson(text, JsonObject.class);

			if (configurationObject == null)
				return;

			for (Map.Entry<String, JsonElement> entry : configurationObject.entrySet()) {
				if (entry.getValue() instanceof JsonObject) {
					JsonObject jsonObject = (JsonObject) entry.getValue();
						Wrapper.getMacros()
								.add(entry.getKey(), jsonObject.get("Key").getAsInt());
				}
			}
		} catch (IOException | JsonSyntaxException ignored) {
		}
	}

	public void save() {
		JsonObject json = new JsonObject();
		Wrapper.getMacros().getList().forEach(macro -> {
			JsonObject macros = new JsonObject();
			macros.addProperty("Key", macro.getKey());
			json.add(macro.getMessage(), macros);
		});
		FileManager.saveJsonObjectToFile(json, FileManager.macros);
	}

}
