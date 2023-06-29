package volcano.summer.screen.altmanager;

import java.io.IOException;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.EnumChatFormatting;

public final class GuiAltLogin extends GuiScreen {

	private PasswordField password;

	private final GuiScreen previousScreen;

	private AltLoginThread thread;

	private GuiTextField username;

	private GuiTextField combined;

	public GuiAltLogin(GuiScreen previousScreen) {

		this.previousScreen = previousScreen;

	}

	@Override

	protected void actionPerformed(GuiButton button) {

		switch (button.id) {

		case 1:

			mc.displayGuiScreen(previousScreen);

			break;

		case 0:

			if (combined.getText().isEmpty()) {

				thread = new AltLoginThread(this.username.getText(), password.getText());

			} else if (!this.combined.getText().isEmpty() && this.combined.getText().contains(":")) {

				String u = this.combined.getText().split(":")[0];

				String p = this.combined.getText().split(":")[1];

				thread = new AltLoginThread(u.replaceAll(" ", ""), p.replaceAll(" ", ""));

			} else {

				thread = new AltLoginThread(this.username.getText(), this.password.getText());

			}

			thread.start();

		}

	}

	@Override

	public void drawScreen(int x, int y, float z) {

		drawDefaultBackground();

		this.username.drawTextBox();

		this.password.drawTextBox();

		this.combined.drawTextBox();

		drawCenteredString(this.mc.fontRendererObj, "Alt Login", this.width / 2, 20, -1);

		drawCenteredString(this.mc.fontRendererObj,

				this.thread == null ? EnumChatFormatting.GRAY + "Idle..." : this.thread.getStatus(), this.width / 2, 29,

				-1);

		if (this.username.getText().isEmpty()) {

			drawString(this.mc.fontRendererObj, "Username / E-Mail", this.width / 2 - 96, 66, -7829368);

		}

		if (this.password.getText().isEmpty()) {

			drawString(this.mc.fontRendererObj, "Password", this.width / 2 - 96, 106, -7829368);

		}

		if (this.combined.getText().isEmpty()) {

			drawString(this.mc.fontRendererObj, "Email:Password", this.width / 2 - 96, 146, -7829368);

		}

		super.drawScreen(x, y, z);

	}

	@Override

	public void initGui() {

		int var3 = height / 4 + 24;

		this.buttonList.add(new GuiButton(0, width / 2 - 100, var3 + 72 + 12, "Login"));

		this.buttonList.add(new GuiButton(1, width / 2 - 100, var3 + 72 + 12 + 24, "Back"));

		this.username = new GuiTextField(var3, mc.fontRendererObj, width / 2 - 100, 60, 200, 20);

		this.password = new PasswordField(this.mc.fontRendererObj, this.width / 2 - 100, 100, 200, 20);

		this.combined = new GuiTextField(eventButton, mc.fontRendererObj, width / 2 - 100, 140, 200, 20);

		this.username.setFocused(true);

		this.username.setMaxStringLength(200);

		this.password.setMaxStringLength(200);

		this.combined.setMaxStringLength(200);

		Keyboard.enableRepeatEvents(true);

	}

	@Override

	protected void keyTyped(char character, int key) {

		try {

			super.keyTyped(character, key);

		} catch (IOException e) {

			e.printStackTrace();

		}

		if ((character == '\t')

				&& ((this.username.isFocused()) || (this.combined.isFocused()) || (this.password.isFocused()))) {

			this.username.setFocused(!this.username.isFocused());

			this.password.setFocused(!this.password.isFocused());

			this.combined.setFocused(!this.combined.isFocused());

		}

		if (character == '\r') {

			actionPerformed((GuiButton) buttonList.get(0));

		}

		this.username.textboxKeyTyped(character, key);

		this.password.textboxKeyTyped(character, key);

		this.combined.textboxKeyTyped(character, key);

	}

	@Override

	protected void mouseClicked(int x, int y, int button) {

		try {

			super.mouseClicked(x, y, button);

		} catch (IOException e) {

			e.printStackTrace();

		}

		this.username.mouseClicked(x, y, button);

		this.password.mouseClicked(x, y, button);

		this.combined.mouseClicked(x, y, button);

	}

	@Override

	public void onGuiClosed() {

		Keyboard.enableRepeatEvents(false);

	}

	@Override

	public void updateScreen() {

		this.username.updateCursorCounter();

		this.password.updateCursorCounter();

		this.combined.updateCursorCounter();

	}

}