package org.spray.infinity.ui.menu.components.config;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import org.spray.infinity.font.IFont;
import org.spray.infinity.main.Infinity;
import org.spray.infinity.ui.menu.widgets.WTextField;
import org.spray.infinity.utils.Helper;
import org.spray.infinity.utils.Timer;
import org.spray.infinity.utils.file.config.Config;
import org.spray.infinity.utils.render.Drawable;
import org.spray.infinity.utils.render.RenderUtil;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3f;

public class ConfigPanel {

	public ArrayList<ConfigElement> configList = new ArrayList<>();
	public WTextField textField;
	private String name;

	private Timer scrollTimer = new Timer();

	public float fade;

	private double scrollSpeed;
	private double prevScrollProgress;
	private double scrollProgress;
	private double _cbuttonHeight;

	private double x;
	private double y;
	private double width;
	private double height;

	private float refreshHover;

	public ConfigPanel(String name) {
		this.name = name;
	}

	public void init() {
		Helper.MC.keyboard.setRepeatEvents(true);
		textField = new WTextField(0xFF0B1427, false);

		textField.setMaxLength(45);
		textField.setText("New Config");
		refresh();
	}

	public void refresh() {
		configList.clear();
		for (Config config : Infinity.getConfigManager().getConfigList()) {
			configList.add(new ConfigElement(config));
		}
		refreshHover = 0;
		fade = -0.5f;
		setScrollProgress(0);
	}

	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {

		if (refreshHover != 360)
			refreshHover = Math.min(360, refreshHover + 37);

		Drawable.drawRectWH(matrices, x, y, width - 2, 40, 0xFF1F5A96);
		Drawable.drawRectWH(matrices, x + 1, y, width - 4, 40, 0xFF0D121D);

		Drawable.drawRectWH(matrices, x + width - 90, y + 11, 60, 18,
				Drawable.isHovered(mouseX, mouseY, x + width - 90, y + 11, 60, 18) ? 0xFFC9C9C9 : 0xFFE3E3E4);
		IFont.legacy15.drawStringWithShadow(matrices, "Add", x + width - 67, y + 15, -1);

		matrices.push();
		double xt = x + width - 23;
		double yt = y + 16;

		GlStateManager._enableBlend();
		matrices.translate(xt + 5, yt + 5, 0);
		matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion((float) refreshHover));
		matrices.translate(-xt - 5, -yt - 5, 0);

		RenderUtil.drawTexture(matrices, new Identifier("infinity", "/textures/icons/refresh.png"), xt, yt, 10, 10);
		GlStateManager._disableBlend();
		matrices.pop();

		textField.setX((int) (x + 12));
		textField.setY((int) (y + 10));
		textField.setWidth(100);
		textField.setHeight(22);
		textField.render(matrices, mouseX, mouseY, delta);

		double yOffset = 2;

		if (fade != 1)
			fade = (float) RenderUtil.animate(fade, 1);

		Drawable.startScissor(x, y + 43, width - 2, height - 80);

		// scroll
		if (_cbuttonHeight > this.height - 80) {
			Drawable.drawRectWH(matrices, x + width - 5, y + 43, 2, height - 80, 0x90000000);
			Drawable.drawRectWH(matrices, x + width - 5, y + 43 + getScrollProgress(), 2,
					height - 80 - getHeightDifference(), 0xFF1F5A96);
		}

		for (ConfigElement configButton : configList) {
			_cbuttonHeight = (int) (y + yOffset);
			configButton.setX(x + 2);
			configButton.setY(y + yOffset + 43 - getScrollProgress());
			configButton.setWidth(width - 10);
			configButton.setHeight(30);

			yOffset += 35;

			RenderSystem.setShaderColor(1f, 1f, 1f, fade);
			configButton.render(matrices, mouseX, mouseY);
		}
		Drawable.stopScissor();
	}

	public void tick() {
		if (_cbuttonHeight < this.height - 80) {
			setScrollProgress(0);
			return;
		}

		int difference = getHeightDifference();

		setScrollProgress(scrollProgress + scrollSpeed);
		scrollSpeed *= 0.54;

		if (scrollTimer.hasReached(100)) {
			if (scrollProgress < 0)
				scrollSpeed = scrollProgress * -0.35;
			else if (scrollProgress > difference)
				scrollSpeed = (scrollProgress - difference) * -0.35;
		}
	}

	public void mouseClicked(double mouseX, double mouseY, int button) {
		if (Drawable.isHovered(mouseX, mouseY, x + width - 90, y + 11, 60, 18) && button == 0) {

			if (!textField.getText().isEmpty()) {
				if (Infinity.getConfigManager().fromName((textField.getText())) == null) {
					Infinity.getConfigManager().loadConfigs();
					Config config = new Config(textField.getText(),
							new SimpleDateFormat("MM/dd/yyyy").format(Calendar.getInstance().getTime()));
					config.add();
					Infinity.getConfigManager().add(config);
					textField.setText("");
					refresh();
					Helper.message(Formatting.GRAY + "New config" + Formatting.WHITE + textField.getText()
							+ Formatting.AQUA + " added");
				}
			}
		}
		if (Drawable.isHovered(mouseX, mouseY, x + width - 23, y + 16, 10, 10) && button == 0) {
			Infinity.getConfigManager().loadConfigs();
			refresh();
		}

		configList.forEach(configButton -> {
			if (configButton.mouseClicked(mouseX, mouseY, button)) {

			}
		});

		textField.mouseClicked(mouseX, mouseY, button);

	}

	public void mouseScrolled(double d, double e, double amount) {
		if (amount != 0) {
			double sa = amount < 0 ? amount - 10 : amount + 10;
			scrollTimer.reset();
			scrollSpeed -= sa;
		}
	}

	public void charTyped(char chr, int keyCode) {
		textField.charTyped(chr, keyCode);
	}

	public void keyPressed(int keyCode, int scanCode, int modifiers) {
		textField.keyPressed(keyCode, scanCode, modifiers);
	}

	public void onClose() {
		fade = -0.3f;
		textField.onClose();
	}

	public int getHeightDifference() {
		double diffHeight = this.height - 80;
		return (int) (this.getListHeight() - diffHeight);
	}

	private int getListHeight() {
		int height = 0;
		for (ConfigElement configButton : configList)
			height += (configButton.getHeight() + 5);
		return height;
	}

	private double getScrollProgress() {
		return prevScrollProgress + (scrollProgress - prevScrollProgress) * Helper.MC.getLastFrameDuration();
	}

	private void setScrollProgress(double value) {
		prevScrollProgress = scrollProgress;
		scrollProgress = value;
	}

	public String getName() {
		return name;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

}
