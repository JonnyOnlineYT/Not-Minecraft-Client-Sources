package org.spray.infinity.utils.file;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.spray.infinity.main.Infinity;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

public class FriendsFile {

	private File friendFile = FileUtil.createJsonFile(Infinity.getDirection(), "friend");

	protected void loadFriends() {
		try {
			String text = FileUtil.readFile(friendFile.getAbsolutePath(), StandardCharsets.UTF_8);

			if (text.isEmpty())
				return;

			JsonObject configurationObject = new GsonBuilder().create().fromJson(text, JsonObject.class);

			if (configurationObject == null)
				return;

			for (Map.Entry<String, JsonElement> entry : configurationObject.entrySet()) {
				if (entry.getValue() instanceof JsonObject) {
					Infinity.getFriend().add(entry.getKey());
				}
			}

		} catch (IOException | JsonSyntaxException e) {
		}
	}

	protected void saveFriends() {
		JsonObject json = new JsonObject();
		for (String friends : Infinity.getFriend().getFriendList()) {
			JsonArray array = new JsonArray();
			json.add(friends, array);
		}

		FileUtil.saveJsonObjectToFile(json, friendFile);
	}

}
