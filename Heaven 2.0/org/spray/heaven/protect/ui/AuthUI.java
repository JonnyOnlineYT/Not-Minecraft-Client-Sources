package org.spray.heaven.protect.ui;

import java.awt.Color;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.lwjgl.input.Keyboard;
import org.spray.heaven.font.IFont;
import org.spray.heaven.protect.Provider;
import org.spray.heaven.protect.events.LoginActionEvent;
import org.spray.heaven.ui.widgets.TextFieldWidget;
import org.spray.heaven.util.AES;
import org.spray.heaven.util.Timer;
import org.spray.heaven.util.file.FileManager;
import org.spray.heaven.util.render.shader.drawable.RoundedShader;
import org.spray.keyauth.api.KeyAuth.Status;

import com.darkmagician6.eventapi.EventManager;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import by.radioegor146.nativeobfuscator.Native;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

@Native
public class AuthUI extends GuiScreen {

	private TextFieldWidget keyField;

	private String key = "";

	private Timer statusTime = new Timer();

	public AuthUI() {
		load();
	}

	@Override
	public void initGui() {
		super.initGui();
		Keyboard.enableRepeatEvents(true);

		keyField = new TextFieldWidget(IFont.WEB_SETTINGS).setBorders(false).setColor(0xFF141414);

		if (key != null && !key.isEmpty()) {
			login(key);
			keyField.setText(key);
		}

		keyField.setFillText("Ключ");

		this.buttonList.add(new GuiButton(1, width / 2 - 30, height / 2 + 14, 60, 21, "Войти"));
	}

	@Override
	public void updateScreen() {
		super.updateScreen();
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float tickDelta) {
		this.drawDefaultBackground();
		int containerWidth = 200;
		int containerHeight = 110;

		RoundedShader.drawRound((width - containerWidth) / 2, (height - containerHeight) / 2 - 10, containerWidth,
				containerHeight, 2, new Color(0xFF100F0F));

		IFont.WEB_MIDDLE.drawCenteredString("Войти в клиент", width / 2, (height / 2 - containerHeight + 52), -1,
				0.64f);
		
		keyField.setX(width / 2 - 70);
		keyField.setY(height / 2 - containerHeight / 4 + 3);
		keyField.setWidth(140);
		keyField.setHeight(26);
		keyField.render(mouseX, mouseY, tickDelta);

		if (statusTime.getTimePassed() < 2000)
			IFont.WEB_SETTINGS.drawCenteredString(Provider.getKeyAuth().getApi().getStatus(), width / 2,
					height / 2 - 36, Provider.getKeyAuth().getApi().getStatusType().getColor());

		super.drawScreen(mouseX, mouseY, tickDelta);
	}

	@Override
	protected void keyTyped(char chr, int keyCode) {
		keyField.keyTyped(chr, keyCode);

		if (keyCode == 28)
			login(keyField.getText());

	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int button) {
		try {
			super.mouseClicked(mouseX, mouseY, button);
		} catch (IOException e) {
			e.printStackTrace();
		}
		keyField.mouseClicked(mouseX, mouseY, button);

	}

	@Override
	public void actionPerformed(GuiButton button) throws IOException {
		switch (button.id) {
		case 0:
			this.mc.shutdown();
			break;
		case 1:
			login(keyField.getText());
			break;
		}
	}

	private void login(String key) {
		if (key.isEmpty())
			return;

		Provider.getKeyAuth().getApi().setStatus("Connecting...", Status.INFO);
		if (Provider.getKeyAuth().getApi().license(key.replaceAll(" ", "")) != null) {
			EventManager.call(new LoginActionEvent());
//			save();
		}

		statusTime.reset();
	}

	public void load() {
		try {
			String text = FileManager.readFile(FileManager.profile.getAbsolutePath(), StandardCharsets.UTF_8);
			if (text.isEmpty())
				return;

			JsonObject jsonReader = new GsonBuilder().create().fromJson(text, JsonObject.class);

			if (jsonReader == null)
				return;

			String decrypt = AES.decrypt(jsonReader.get("LicenseKey").getAsString(), AES.getKey());
			this.key = decrypt != null ? decrypt : "";

		} catch (IOException | JsonSyntaxException ignored) {
		}
	}

	public void save() {
		JsonObject json = new JsonObject();

		String key = Provider.getKeyAuth().getApi().getUserData().getKey();
		if (key != null) {
			String encryptedKey = AES.encrypt(Provider.getKeyAuth().getApi().getUserData().getKey(), AES.getKey());
			if (encryptedKey != null)
				key = encryptedKey;
		}

		json.addProperty("LicenseKey", key);
		FileManager.saveJsonObjectToFile(json, FileManager.profile);
	}

}
