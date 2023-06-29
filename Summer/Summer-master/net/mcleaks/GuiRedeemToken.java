package net.mcleaks;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;

import org.lwjgl.input.Keyboard;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.Session;
import volcano.summer.client.util.Colors;

public class GuiRedeemToken extends GuiScreen {
	private final boolean sessionRestored;
	private final String message;
	private GuiTextField tokenField;
	private GuiButton restoreButton;

	public GuiRedeemToken(final boolean sessionRestored) {
		this(sessionRestored, null);
	}

	public GuiRedeemToken(final boolean sessionRestored, final String message) {
		this.sessionRestored = sessionRestored;
		this.message = message;
	}

	@Override
	public void updateScreen() {
		this.tokenField.updateCursorCounter();
	}

	@Override
	public void initGui() {
		Keyboard.enableRepeatEvents(true);
		this.restoreButton = new GuiButton(0, this.width / 2 - 150, this.height / 4 + 96 + 18, 128, 20,
				this.sessionRestored ? "Session restored!" : "Restore Session");
		this.restoreButton.enabled = (MCLeaks.savedSession != null);
		this.buttonList.add(this.restoreButton);
		this.buttonList.add(new GuiButton(1, this.width / 2 - 18, this.height / 4 + 96 + 18, 168, 20, "Redeem Token"));
		this.buttonList.add(new GuiButton(2, this.width / 2 - 150, this.height / 4 + 120 + 18, 158, 20, "Get Token"));
		this.buttonList.add(new GuiButton(3, this.width / 2 + 12, this.height / 4 + 120 + 18, 138, 20, "Back"));
		(this.tokenField = new GuiTextField(0, this.fontRendererObj, this.width / 2 - 100, 128, 200, 20))
				.setFocused(true);
	}

	@Override
	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
	}

	@Override
	protected void actionPerformed(final GuiButton button) throws IOException {
		if (button.enabled) {
			if (button.id == 0) {
				MCLeaks.remove();
				SessionManager.setSession(MCLeaks.savedSession);
				MCLeaks.savedSession = null;
				Minecraft.getMinecraft().displayGuiScreen(new GuiRedeemToken(true));
			} else if (button.id == 1) {
				if (this.tokenField.getText().length() != 16) {
					Minecraft.getMinecraft().displayGuiScreen(new GuiRedeemToken(false, Colors.RED + "Invalid token!"));
					return;
				}
				button.enabled = false;
				button.displayString = "Please wait ...";
				ModAPI.redeem(this.tokenField.getText(), new Callback<Object>() {
					@Override
					public void done(final Object o) {
						if (o instanceof String) {
							Minecraft.getMinecraft().displayGuiScreen(new GuiRedeemToken(false, Colors.RED + "" + o));
							return;
						}
						if (MCLeaks.savedSession == null) {
							MCLeaks.savedSession = Minecraft.getMinecraft().getSession();
						}
						final RedeemResponse response = (RedeemResponse) o;
						MCLeaks.refresh(response.getSession(), response.getMcName());
						Minecraft.getMinecraft().displayGuiScreen(
								new GuiRedeemToken(false, Colors.GREEN + "Your token was redeemed successfully!"));
						try {
							final JsonParser parser = new JsonParser();
							final JsonElement rawJson = parser.parse(new InputStreamReader(
									new URL("https://mcapi.ca/uuid/player/" + response.getMcName()).openConnection()
											.getInputStream()));
							if (!rawJson.isJsonArray()) {
								System.out.println("Invalid UUID.");
							}
							final JsonArray json = rawJson.getAsJsonArray();
							final String uuid = json.get(json.size() - 1).getAsJsonObject().get("uuid").getAsString();
							Minecraft.getMinecraft().setSession(
									new Session(response.getMcName(), uuid, response.getSession(), "mojang"));
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			} else if (button.id == 2) {
				try {
					final Class<?> oclass = Class.forName("java.awt.Desktop");
					final Object object = oclass.getMethod("getDesktop", new Class[0]).invoke(null, new Object[0]);
					oclass.getMethod("browse", URI.class).invoke(object, new URI("https://mcleaks.net/"));
				} catch (Throwable throwable) {
					throwable.printStackTrace();
				}
			} else if (button.id == 3) {
				Minecraft.getMinecraft().displayGuiScreen(new GuiMainMenu());
			}
		}
	}

	@Override
	protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
		this.tokenField.textboxKeyTyped(typedChar, keyCode);
		if (keyCode == 15) {
			this.tokenField.setFocused(!this.tokenField.isFocused());
		}
		if (keyCode == 28 || keyCode == 156) {
			this.actionPerformed((GuiButton) this.buttonList.get(1));
		}
	}

	@Override
	protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		this.tokenField.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
		this.drawDefaultBackground();
		this.drawCenteredString(this.fontRendererObj, Colors.WHITE + "- " + Colors.AQUA + "MCLeaks" + Colors.WHITE + "."
				+ Colors.AQUA + "net " + Colors.WHITE + "-", this.width / 2, 17, 16777215);
		this.drawCenteredString(this.fontRendererObj, "Free minecraft accounts", this.width / 2, 27, 16777215);
		this.drawCenteredString(this.fontRendererObj, "Status:", this.width / 2, 68, 16777215);
		this.drawCenteredString(this.fontRendererObj, MCLeaks.getStatus(), this.width / 2, 78, 16777215);
		this.drawString(this.fontRendererObj, "Token", this.width / 2 - 100, 115, 10526880);
		if (this.message != null) {
			this.drawCenteredString(this.fontRendererObj, this.message, this.width / 2, 158, 16777215);
		}
		this.tokenField.drawTextBox();
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
}