package org.spray.infinity.utils.file;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.spray.infinity.features.component.macro.Macro;
import org.spray.infinity.main.Infinity;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

public class MacrosFile {

	private File macroFile = FileUtil.createJsonFile(Infinity.getDirection(), "macro");

	protected void loadMacro() {
		try {
			String text = FileUtil.readFile(macroFile.getAbsolutePath(), StandardCharsets.UTF_8);
			if (text.isEmpty())
				return;

			JsonObject configurationObject = new GsonBuilder().create().fromJson(text, JsonObject.class);

			if (configurationObject == null)
				return;

			for (Map.Entry<String, JsonElement> entry : configurationObject.entrySet()) {
				if (entry.getValue() instanceof JsonObject) {
					JsonObject jsonObject = (JsonObject) entry.getValue();
					Infinity.getMacroManager().getList()
							.add(new Macro(jsonObject.get("Message").getAsString(), jsonObject.get("Key").getAsInt()));
				}
			}
		} catch (IOException | JsonSyntaxException e) {
		}
	}

	protected void saveMacro() {
		JsonObject json = new JsonObject();
		for (Macro macro : Infinity.getMacroManager().getList()) {
			JsonObject macroJson = new JsonObject();
			macroJson.addProperty("Key", macro.getKey());
			macroJson.addProperty("Message", macro.getMessage());
			json.add("Macro", macroJson);
		}
		FileUtil.saveJsonObjectToFile(json, macroFile);
	}

}
