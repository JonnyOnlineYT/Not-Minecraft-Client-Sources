package org.spray.infinity.ui.account;

import org.spray.infinity.ui.account.main.Account;
import org.spray.infinity.ui.account.main.AccountThread;
import org.spray.infinity.utils.Helper;
import org.spray.infinity.utils.render.FontUtils;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;

public class GuiDirect extends Screen {

	private Screen prev;
	private TextFieldWidget accountName;
	private TextFieldWidget password;
	private ButtonWidget buttonAdd;

	public GuiDirect(Screen prev) {
		super(new LiteralText(""));
		this.prev = prev;
	}

	@Override
	public void init() {
		this.client.keyboard.setRepeatEvents(true);
		this.accountName = new TextFieldWidget(this.textRenderer, this.width / 2 - 100, 66, 200, 20,
				new TranslatableText("Login"));
		this.accountName.setMaxLength(30);
		this.addSelectableChild(accountName);
		this.password = new TextFieldWidget(this.textRenderer, this.width / 2 - 100, 106, 200, 20,
				new TranslatableText("Password"));
		this.password.setMaxLength(128);
		this.addSelectableChild(password);

		this.buttonAdd = (ButtonWidget) this.addDrawableChild(new ButtonWidget(this.width / 2 - 100,
				this.height / 4 + 96, 200, 20, new TranslatableText("Login"), (buttonWidget) -> {
					if (!accountName.getText().isEmpty()) {
						Account account = new Account(accountName.getText(), password.getText());
						(new AccountThread(account)).start();
						Helper.MC.openScreen(prev);
					}
				}));

		this.addDrawableChild(new ButtonWidget(this.width / 2 - 100, this.height / 4 + 96 + 28, 200, 20,
				new TranslatableText("Cancel"), (buttonWidget) -> {
					Helper.MC.openScreen(prev);
				}));

	}

	@Override
	public void tick() {
		boolean notEmpty = !accountName.getText().isEmpty();
		buttonAdd.active = notEmpty;
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		this.renderBackground(matrices);
		drawStringWithShadow(matrices, this.textRenderer, "Login or Nickname", this.width / 2 - 100, 53, 10526880);
		drawStringWithShadow(matrices, this.textRenderer, "Password", this.width / 2 - 100, 94, 10526880);
		this.accountName.render(matrices, mouseX, mouseY, delta);
		this.password.render(matrices, mouseX, mouseY, delta);
		FontUtils.drawHVCenteredString(matrices, "Direct Login", this.width / 2, 27, -1);
		super.render(matrices, mouseX, mouseY, delta);
	}

}
