package volcano.summer.screen.altmanager;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.io.IOException;
import java.net.Proxy;

import org.lwjgl.input.Keyboard;

import com.mojang.authlib.Agent;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import com.thealtening.AltService;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.EnumChatFormatting;
import volcano.summer.base.Summer;
import volcano.summer.base.manager.file.files.Alts;
import volcano.summer.screen.changelog.GuiCustomButton;

public class GuiAddAlt extends GuiScreen {
	private final GuiAltManager manager;
	private PasswordField password;
	private String status;
	private GuiTextField username;
	private GuiTextField combined;

	public GuiAddAlt(GuiAltManager manager) {
		this.status = (EnumChatFormatting.GRAY + "Idle...");
		this.manager = manager;
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		switch (button.id) {
		case 0:
			AddAltThread login = new AddAltThread(this.username.getText(), this.password.getText());
			if (combined.getText().isEmpty())
				login = new AddAltThread(username.getText(), password.getText());
			else if (!combined.getText().isEmpty() && combined.getText().contains(":")) {
				String u = combined.getText().split(":")[0];
				String p = combined.getText().split(":")[1];
				login = new AddAltThread(u.replaceAll(" ", ""), p.replaceAll(" ", ""));
			} else
				login = new AddAltThread(username.getText(), password.getText());
			
			login.start();
			break;
		case 1:
			this.mc.displayGuiScreen(this.manager);

		case 2:
			String data = null;
			try {
				data = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
			} catch (Exception ignored) {
				break;
			}
			if (data.contains(":")) {
				String[] credentials = data.split(":");
				this.username.setText(credentials[0]);
				this.password.setText(credentials[1]);
			}
			break;
		}

	}


	@Override
	public void drawScreen(int i, int j, float f) {
		drawDefaultBackground();
		this.username.drawTextBox();
		this.password.drawTextBox();
		this.combined.drawTextBox();
		drawCenteredString(this.fontRendererObj, "Add Alt", this.width / 2, 20, -1);
		if (this.username.getText().isEmpty()) {
			drawString(this.mc.fontRendererObj, "Username / E-Mail", this.width / 2 - 96, 66, -7829368);
		}
		if (this.password.getText().isEmpty()) {
			drawString(this.mc.fontRendererObj, "Password", this.width / 2 - 96, 106, -7829368);
		}
		if (this.combined.getText().isEmpty()) {
			drawString(mc.fontRendererObj, "Email:Password", width / 2 - 96, 146, -7829368);
		}
		drawCenteredString(this.fontRendererObj, this.status, this.width / 2, 30, -1);
		super.drawScreen(i, j, f);
	}

	@Override
	public void initGui() {
		Keyboard.enableRepeatEvents(true);
		this.buttonList.clear();
		this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 92 + 12, "Login"));
		this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 116 + 12, "Back"));
		this.buttonList.add(new GuiButton(2, this.width / 2 - 100, this.height / 4 + 116 + 36, "Import user:pass"));
		this.username = new GuiTextField(this.eventButton, this.mc.fontRendererObj, this.width / 2 - 100, 60, 200, 20);
		this.password = new PasswordField(this.mc.fontRendererObj, this.width / 2 - 100, 100, 200, 20);
		this.combined = new GuiTextField(eventButton, mc.fontRendererObj, width / 2 - 100, 140, 200, 20);
		this.combined.setMaxStringLength(200);
	}

	@Override
	protected void keyTyped(char par1, int par2) {
		this.username.textboxKeyTyped(par1, par2);
		this.password.textboxKeyTyped(par1, par2);
		this.combined.textboxKeyTyped(par1, par2);
		if ((par1 == '\t') && ((this.username.isFocused()) || (this.password.isFocused()))) {
			this.username.setFocused(!this.username.isFocused());
			this.password.setFocused(!this.password.isFocused());
		}
		if (par1 == '\r') {
			actionPerformed((GuiButton) this.buttonList.get(0));
		}
	}

	@Override
	protected void mouseClicked(int par1, int par2, int par3) {
		try {
			super.mouseClicked(par1, par2, par3);
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.username.mouseClicked(par1, par2, par3);
		this.password.mouseClicked(par1, par2, par3);
		this.combined.mouseClicked(par1, par2, par3);
	}

	public class AddAltThread extends Thread {
		private final String password;
		private final String username;

		public AddAltThread(String username, String password) {
			this.username = username;
			this.password = password;
			GuiAddAlt.this.status = (EnumChatFormatting.GRAY + "Idle...");
		}

		private final void checkAndAddAlt(String username, String password) {
			try {
				Summer.altService.switchService(AltService.EnumAltService.MOJANG);
			} catch (NoSuchFieldException | IllegalAccessException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			YggdrasilAuthenticationService service = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
			YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication) service
					.createUserAuthentication(Agent.MINECRAFT);
			auth.setUsername(username);
			auth.setPassword(password);
			try {
				auth.logIn();
				AltManager altManager = Summer.altManager;
				AltManager.registry.add(new Alt(username, password, auth.getSelectedProfile().getName()));
				try {
					Summer.fileManager.getFile(Alts.class).saveFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
				GuiAddAlt.this.status = ("Alt added. (" + username + ")");
			} catch (AuthenticationException e) {
				GuiAddAlt.this.status = (EnumChatFormatting.RED + "Alt failed!");
				e.printStackTrace();
			}
		}

		@Override
		public void run() {
			if (this.password.equals("")) {
				AltManager altManager = Summer.altManager;
				AltManager.registry.add(new Alt(this.username, ""));
				GuiAddAlt.this.status = (EnumChatFormatting.GREEN + "Alt added. (" + this.username
						+ " - offline name)");
				return;
			}
			GuiAddAlt.this.status = (EnumChatFormatting.YELLOW + "Trying alt...");
			checkAndAddAlt(this.username, this.password);
		}
	}
}
