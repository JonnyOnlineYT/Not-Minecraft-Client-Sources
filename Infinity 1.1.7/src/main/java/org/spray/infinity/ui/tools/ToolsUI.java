package org.spray.infinity.ui.tools;

import org.spray.infinity.features.module.hidden.AntiFabric;
import org.spray.infinity.main.Infinity;
import org.spray.infinity.ui.cape.CapeUI;
import org.spray.infinity.utils.Helper;
import org.spray.infinity.utils.render.FontUtils;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;

public class ToolsUI extends Screen {

	private Screen parent;

	private ButtonWidget fSpoofWidget;

	public ToolsUI(Screen parent) {
		super(new LiteralText("Tools"));
		this.parent = parent;
	}

	@Override
	public void init() {
		// height / 2 - lastY - 15;

		AntiFabric antiFabric = ((AntiFabric) Infinity.getModuleManager().get(AntiFabric.class));
		addDrawableChild(fSpoofWidget = new ButtonWidget(this.width / 2 - 80, this.height / 2 - 90, 160, 20,
				new LiteralText("AntiFabric Spoof: " + onOrOff(antiFabric.isEnabled())), (buttonWidget) -> {
					antiFabric.enable();
					fSpoofWidget.setMessage(new LiteralText("AntiFabric Spoof: " + onOrOff(antiFabric.isEnabled())));
				}));

		addDrawableChild(fSpoofWidget = new ButtonWidget(this.width / 2 - 80, this.height / 2 - 65, 160, 20,
				new LiteralText("Menu"), (buttonWidget) -> {
					Helper.openScreen(Infinity.MENU);
				}));

		addDrawableChild(fSpoofWidget = new ButtonWidget(this.width / 2 - 80, this.height / 2 - 40, 160, 20,
				new LiteralText("Capes"), (buttonWidget) -> {
					Helper.openScreen(new CapeUI());
				}));

		addDrawableChild(new ButtonWidget(this.width / 2 - 50, this.height / 2 + 60, 100, 20, ScreenTexts.BACK,
				(buttonWidget) -> {
					this.client.openScreen(parent);
				}));
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		this.renderBackground(matrices);
		FontUtils.drawHVCenteredString(matrices, "Tools", this.width / 2, 27, -1);
		super.render(matrices, mouseX, mouseY, delta);
	}

	private String onOrOff(boolean on) {
		return on ? Formatting.GREEN + "ON" : Formatting.GRAY + "OFF";
	}

}
