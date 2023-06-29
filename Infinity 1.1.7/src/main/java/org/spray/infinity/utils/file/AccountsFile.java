package org.spray.infinity.utils.file;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.spray.infinity.main.Infinity;
import org.spray.infinity.ui.account.main.AddThread;
import org.spray.infinity.utils.system.crypt.AES;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

public class AccountsFile {

	private File accountFile = FileUtil.createJsonFile(Infinity.getDirection(), "accounts");

	protected void loadAccounts() {
		try {
			String text = FileUtil.readFile(accountFile.getAbsolutePath(), StandardCharsets.UTF_8);
			if (text.isEmpty())
				return;

			JsonObject jsonReader = new GsonBuilder().create().fromJson(text, JsonObject.class);

			if (jsonReader == null)
				return;

			for (Map.Entry<String, JsonElement> entry : jsonReader.entrySet()) {
				String password = "";
				JsonArray jsonArray = entry.getValue().getAsJsonArray();

				if (jsonArray != null) {
					if (jsonArray.size() > 0) {
						if (jsonArray.get(1).isJsonNull()) {
							password = "";
						} else {
							String decryptedPassword = AES.decrypt(jsonArray.get(1).getAsString(), AES.getKey());
							if (decryptedPassword == null)
								password = "";
							password = decryptedPassword;
						}
					}
				}
				// System.out.println("do syuda dowel");
				String username = entry.getKey();
				AddThread addThread = new AddThread(username, password);
				addThread.start();
			}
		} catch (IOException | JsonSyntaxException e) {
		}
	}

	protected void saveAccounts() {
		JsonObject json = new JsonObject();
		Infinity.getAccountManager().getRegistry().forEach(account -> {
			JsonArray array = new JsonArray();
			array.add(account.getMask());
			String password = "";
			if (account.getPassword() != null) {
				String encryptedPassword = AES.encrypt(account.getPassword(), AES.getKey());
				if (encryptedPassword != null)
					password = encryptedPassword;
			}
			array.add(password);
			json.add(account.getUsername(), array);
		});
		FileUtil.saveJsonObjectToFile(json, accountFile);
	}

}
