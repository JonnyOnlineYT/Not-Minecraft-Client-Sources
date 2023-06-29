package org.spray.heaven.util.file;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.spray.heaven.main.Wrapper;

public class FriendConfig {

	public void load() {
		try {
			String text = FileManager.readFile(FileManager.friend.getAbsolutePath(), StandardCharsets.UTF_8);

			if (text.isEmpty())
				return;

			JsonObject configurationObject = new GsonBuilder().create().fromJson(text, JsonObject.class);

			if (configurationObject == null)
				return;

			for (Map.Entry<String, JsonElement> entry : configurationObject.entrySet()) {
				if (entry.getValue() instanceof JsonObject) {
					JsonObject jsonObject = (JsonObject) entry.getValue();
					if (jsonObject.get("Date").isJsonNull())
						Wrapper.getFriend().add(entry.getKey());
					else
						Wrapper.getFriend().add(entry.getKey(), jsonObject.get("Date").getAsString());
				}
			}
		} catch (IOException | JsonSyntaxException ignored) {
		}
	}

	public void save() {
		JsonObject json = new JsonObject();
		Wrapper.getFriend().getFriends().values().forEach(friend -> {
			JsonObject friendJson = new JsonObject();
			friendJson.addProperty("Date", friend.getDate());
			json.add(friend.getName(), friendJson);
		});

		FileManager.saveJsonObjectToFile(json, FileManager.friend);
	}

}
