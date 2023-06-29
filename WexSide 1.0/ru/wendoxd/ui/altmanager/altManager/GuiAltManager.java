package ru.wendoxd.ui.altmanager.altManager;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.Session;
import net.minecraft.util.text.TextFormatting;
import ru.wendoxd.WexSide;
import ru.wendoxd.modules.impl.visuals.StreamerMode;
import ru.wendoxd.ui.altmanager.altManager.auth.AuthService;
import ru.wendoxd.ui.altmanager.altManager.window.AltWindow;
import ru.wendoxd.ui.altmanager.altManager.window.impl.AddWindow;
import ru.wendoxd.ui.altmanager.altManager.window.impl.ConfirmWindow;
import ru.wendoxd.ui.altmanager.altManager.window.impl.InfoWindow;
import ru.wendoxd.utils.fonts.Fonts;
import ru.wendoxd.utils.visual.RenderUtils;

import java.io.IOException;
import java.util.Random;

import org.lwjgl.input.Keyboard;

public class GuiAltManager extends GuiScreen {

	public static Alt selectedAlt;
	public static String status;
	private static AuthService authService = new AuthService();
	private static int statusTicks;
	public AltsList list;
	private AltWindow altWindow;

	@Override
	public void initGui() {
		Keyboard.enableRepeatEvents(true);
		buttonList.clear();

		buttonList.add(new GuiButton(0, width / 2 - 120, height - 28, 57, 20, "Log in"));
		buttonList.add(new GuiButton(1, width / 2 - 120, height - 52, 75, 20, "Microsoft Login"));
		buttonList.add(new GuiButton(2, width / 2 - 60, height - 28, 57, 20, "Add Alt"));
		buttonList.add(new GuiButton(3, width / 2, height - 28, 57, 20, "Generate"));
		buttonList.add(new GuiButton(4, width / 2 + 60, height - 52, 57, 20, "Remove Alt"));
		buttonList.add(new GuiButton(5, width / 2 + 60, height - 28, 57, 20, "Back"));
		buttonList.add(new GuiButton(6, width - 85, 5, 80, 20, "Clear All"));

		list = new AltsList((alt) -> selectedAlt = alt, (alt -> click(buttonList.get(0))));
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {

		if (altWindow != null && altWindow.exit) {
			altWindow = null;
		}
		WexSide.drawBackground(width, height);

		Fonts.MNTSB_16.drawCenteredString(status, width / 2F, 10, -1);

		if (list != null) {
			list.draw(width / 2F - 120, 40, 237, height - 100);
		}

		if (altWindow != null) {
			RenderUtils.drawRect(0, 0, width, height, Integer.MIN_VALUE);
			altWindow.draw(mouseX, mouseY);
		}

		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	public void updateScreen() {
		buttonList.stream().filter(button -> button.id != 2 && button.id != 5 && button.id != 3 && button.id != 6)
				.forEach(button -> button.enabled = selectedAlt != null && !WexSide.altManager.getAccounts().isEmpty());
		buttonList.get(6).enabled = !WexSide.altManager.getAccounts().isEmpty();

		if (selectedAlt != null) {
			if (!WexSide.altManager.getAccounts().contains(selectedAlt)) {
				selectedAlt = null;
			}
		}

		if (statusTicks == 0) {
			GuiAltManager.status = TextFormatting.GRAY
					+ (!StreamerMode.type.get(8) ? mc.getSession().getUsername() : "Protected");
		} else if (statusTicks >= 0) {
			statusTicks--;
		}
	}

	@Override
	public void handleMouseInput() throws IOException {
		if (altWindow == null) {
			list.mouseInput();
		}
		super.handleMouseInput();
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		if (altWindow == null) {
			buttonList.stream().filter(guiButton -> guiButton.enabled && guiButton.isMouseOver()).forEach(this::click);

			list.click(mouseX, mouseY);
		} else {
			altWindow.click(mouseX, mouseY, mouseButton);
		}
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	public void click(GuiButton button) {
		switch (button.id) {
		case 0:
			setStatus("Logging in...");

			if (!selectedAlt.isMojangAccount()) {
				mc.session = new Session(selectedAlt.getEmail(), "", "", "mojang");
				if (mc.getSession().getUsername().length() <= 16) {
					setStatus(
							ChatFormatting.GREEN + "Logged as " + ChatFormatting.GRAY + mc.getSession().getUsername());
				} else {
					setStatus("wtf");
				}
			} else {
				Session session = WexSide.altManager.login(selectedAlt.getEmail(), selectedAlt.getPassword());
				if (mc.getSession().getUsername().length() <= 16) {
					setStatus(ChatFormatting.GREEN + "Logged as " + ChatFormatting.GRAY
							+ (session == null ? "" : session.getUsername()));
				} else {
					setStatus("wtf");
				}
				mc.session = session;
				if (session == null) {
					setStatus(
							ChatFormatting.GREEN + "Logged as " + ChatFormatting.GRAY + mc.getSession().getUsername());
					selectedAlt.setChecked(mc.getSession().getUsername());
				}
			}
			break;
		case 1:
			altWindow = new MicrosoftLoginWindow();
			new Thread(() -> authService.authWithNoRefreshToken()).start();
			break;
		case 2:
			altWindow = new AddAltWindow();
			break;
		case 3:
			String nick = randomString(8);
			WexSide.altManager.addAccount(new Alt(nick, "empty", nick));
			mc.session = new Session(nick, "", "", "mojang");
			break;
		case 4:
			altWindow = new DeleteAltWindow(selectedAlt);
			break;
		case 5:
			mc.displayGuiScreen(new GuiMainMenu());
			break;
		case 6:
			altWindow = new ClearAllWindow();
			break;
		}
	}

	public static String randomString(int w) {
		StringBuilder builder = new StringBuilder();
		char[] buffer = "qwertyuiopasdfghjklzxcvbnm1234567890".toCharArray();
		for (int i = 0; i < w; i++) {
			Random rand = new Random();
			String s = new String(new char[] { buffer[rand.nextInt(buffer.length)] });
			builder.append(rand.nextBoolean() ? s : s.toUpperCase());
		}
		return builder.toString();
	}

	public static void setStatus(String status) {
		GuiAltManager.status = status;
		GuiAltManager.statusTicks = 25;
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if (altWindow != null) {
			altWindow.keyTyped(typedChar, keyCode);
		}
		super.keyTyped(typedChar, keyCode);
	}

	public static class AddAltWindow extends AddWindow {

		private AddAltWindow() {
			super(0, 0, 220, 100, "Добавление аккаунта");
		}

		@Override
		public void addAction() {
			new Thread(() -> {
				if (password.getText().isEmpty()) {
					if (!login.getText().isEmpty()) {
						WexSide.altManager.addAccount(new Alt(login.getText(), "", login.getText()));
						setStatus(ChatFormatting.GREEN + "Успешно добавил аккаунт " + ChatFormatting.GRAY
								+ login.getText());
						exit();
					}
				} else {
					if (!login.getText().isEmpty() && !password.getText().isEmpty()) {
						String emailText = login.getText().trim();
						String passwordText = password.getText().trim();

						if (!emailText.contains("@")) {
							setStatus(ChatFormatting.RED + "Гвоздь мне в кеды");
							return;
						}

						Session session = WexSide.altManager.login(emailText, passwordText);

						if (session != null) {
							if (!session.getUsername().isEmpty()) {
								WexSide.altManager.addAccount(new Alt(emailText, passwordText, session.getUsername()));
								setStatus(ChatFormatting.GREEN + "Успешно добавил аккаунт " + ChatFormatting.GRAY
										+ session.getUsername());
								exit();
							}
						} else {
							setStatus(ChatFormatting.RED + "Аккаунт не работает!");
						}
					}
				}
			}).start();
		}
	}

	public static class DeleteAltWindow extends ConfirmWindow {

		private Alt alt;

		private DeleteAltWindow(Alt alt) {
			super(0, 0, 200, 60, "Ты точно хочешь удалить этот аккаунт?", alt == null ? ""
					: TextFormatting.RED + alt.getNameOrEmail() + TextFormatting.GRAY + " будет удален!");
			this.alt = alt;
		}

		@Override
		public void confirmAction() {
			if (alt == null)
				return;
			WexSide.altManager.removeAccount(alt);
			super.confirmAction();
		}
	}

	public static class ClearAllWindow extends ConfirmWindow {

		private ClearAllWindow() {
			super(0, 0, 200, 60, "Ты точно хочешь удалить все свои аккаунты?",
					TextFormatting.RED + "Все аккаунты" + TextFormatting.GRAY + " будут удалены!");
		}

		@Override
		public void confirmAction() {
			WexSide.altManager.clearAccounts();
			super.confirmAction();
		}
	}

	public static class MicrosoftLoginWindow extends InfoWindow {

		private MicrosoftLoginWindow() {
			super(0, 0, 200, 80, "Microsoft Login", "Unknown State!");
		}

		@Override
		public void draw(int mouseX, int mouseY) {
			super.draw(mouseX, mouseY);
			message = authService.message;
			if (message.contains("Successfully logged in with account")) {
				buttonText = "OK";
			} else {
				buttonText = "Cancel";
			}
		}
	}
}
