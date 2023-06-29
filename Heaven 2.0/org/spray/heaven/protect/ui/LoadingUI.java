package org.spray.heaven.protect.ui;

import net.minecraft.client.gui.GuiScreen;

import java.awt.Color;

import org.spray.heaven.font.IFont;
import org.spray.heaven.ui.clickui.Colors;
import org.spray.heaven.util.MathUtil;
import org.spray.heaven.util.Timer;
import org.spray.heaven.util.render.Drawable;
import org.spray.heaven.util.render.shader.drawable.RoundedShader;

public class LoadingUI extends GuiScreen {

	private final GuiScreen transitionScreen;
	private final Timer timer = new Timer();
	
	private double randomTime = 0;

	public LoadingUI(GuiScreen transitionScreen, int max) {
		this.transitionScreen = transitionScreen;
		randomTime = 1;
	}

	@Override
	public void initGui() {
		super.initGui();
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float tickDelta) {
		this.drawDefaultBackground();
		
		Drawable.drawRectWH(0, 0, width, height, 0xFF161616);
		
		IFont.WEB_MIDDLE.drawCenteredString("Загрузка", width / 2, height / 2 - 30, 0xFFDADADA, 0.6f);
		
		float loadingWidth = (float) ((200f) * (timer.getTimePassed() / randomTime));
		loadingWidth = MathUtil.clamp(loadingWidth, 0, 200);
		
		RoundedShader.drawRound(width / 2 - 100, height / 2, 200, 4, 2, new Color(0xFF141414));
		RoundedShader.drawRound(width / 2 - 100, height / 2, loadingWidth, 4, 2, Colors.CLIENT_COLOR);
		
		if (timer.hasReached(randomTime))
			mc.displayGuiScreen(transitionScreen);

		super.drawScreen(mouseX, mouseY, tickDelta);
	}
	
	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
	}

}
