package org.spray.infinity.ui.menu.components.window;

import java.awt.Color;
import java.awt.Rectangle;

import org.spray.infinity.features.Setting;
import org.spray.infinity.font.IFont;
import org.spray.infinity.ui.menu.widgets.WTextField;
import org.spray.infinity.utils.Helper;
import org.spray.infinity.utils.render.FontUtils;
import org.spray.infinity.utils.render.Drawable;
import org.spray.infinity.utils.render.RenderUtil;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;

public class ColorPickerWindow extends Screen {

	private Screen prev;

	public static final Identifier PICKER = new Identifier("infinity", "textures/game/screen/picker.png");

	private WTextField colorField;

	private static final int H = 0, S = 1, B = 2;
	private float[] hsb;
	private int rgb;

	private boolean draggingHS, draggingB;

	private Rectangle rectHSArea, rectBArea;

	private int xPosition = 20;
	private int yPosition = 20;

	private Setting setting;

	private double anim;
	private double hover;

	public ColorPickerWindow(Screen prev, Setting setting) {
		super(new LiteralText(""));
		this.prev = prev;
		this.setting = setting;
		anim = 0.4;

		this.xPosition = (width / 2) - 90;
		this.yPosition = (height / 2) - 110;

		this.rectHSArea = new Rectangle(this.xPosition + 10, this.yPosition + 10, 128, 128);
		this.rectBArea = new Rectangle(this.xPosition + 148, this.yPosition + 10, 7, 128);

		Color color = setting.getColor();
		this.hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
	}

	@Override
	public void init() {
		Helper.MC.keyboard.setRepeatEvents(true);
		colorField = new WTextField(0xFF0B1427, false);
		colorField.setMaxLength(6);

		this.updateColor();
	}

	public int getColor() {
		int rgb = (0xFFFFFF & Color.HSBtoRGB(this.hsb[H], this.hsb[S], this.hsb[B]));
		return rgb;
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		prev.render(matrices, -1, -1, delta);

		anim = anim > 0 ? Math.max(0, anim - 0.11) : 0;

		matrices.push();

		if (anim > 0) {
			matrices.translate(this.xPosition + 97.5, this.yPosition + 135, 0);
			matrices.scale((float) (1f + anim), (float) (1f + anim), 1f);
			matrices.translate(-this.xPosition - 97.5, -this.yPosition - 135, 0);
		}

		renderPicker(matrices, mouseX, mouseY, delta);

		matrices.pop();

	}

	public void renderPicker(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		this.xPosition = (width / 2) - 80;
		this.yPosition = (height / 2) - 95;

		this.rectHSArea = new Rectangle(this.xPosition + 10, this.yPosition + 10, 128, 128);
		this.rectBArea = new Rectangle(this.xPosition + 148, this.yPosition + 10, 7, 128);

		int hPos = this.xPosition + 10 + (int) (128F * this.hsb[H]);
		int sPos = this.yPosition + 10 + (128 - (int) (128F * this.hsb[S]));
		int bPos = this.yPosition + 10 + (128 - (int) (128F * this.hsb[B]));
		int brightness = Color.HSBtoRGB(this.hsb[H], this.hsb[S], 1.0F) | 0xFF000000;

		Drawable.drawBorderedRect(matrices, this.xPosition - 4, this.yPosition - 15, 172, 185, 1, 0xFF080629,
				0xFF161621);
		IFont.legacy14.drawCenteredString(matrices, setting.getName(), xPosition + 158 / 2, yPosition - 11, -1);

		hover = Drawable.isHovered(mouseX, mouseY, xPosition + 154, yPosition - 14, 12, 12) ? Math.min(1.2, hover + 0.1)
				: Math.max(1, hover - 0.1);

		double hw = xPosition + 160;
		double hh = yPosition - 14 / 2;

		matrices.push();
		matrices.translate(hw, hh, 0);
		matrices.scale((float) hover, (float) hover, 1f);
		matrices.translate(-hw, -hh, 0);

		RenderUtil.drawTexture(matrices, new Identifier("infinity", "textures/icons/exit.png"), xPosition + 157,
				yPosition - 11, 6, 6);

		matrices.pop();

		Drawable.drawBorderedRect(matrices, this.xPosition, this.yPosition, 164, 165, 2, 0xFF131D4C, 0xFF121D39);

		fill(matrices, this.xPosition + 9, this.yPosition + 9, this.xPosition + 139, this.yPosition + 139, 0xFF2E4354);
		RenderUtil.drawTexture(matrices, PICKER, this.xPosition + 10, this.yPosition + 10, 128, 128);

		fillGradient(matrices, this.xPosition + 148, this.yPosition + 10, this.xPosition + 155, this.yPosition + 138,
				brightness, 0xFF000000);

		Drawable.drawTriangle(matrices, xPosition + 146, bPos, 5, 90, 0xFFE4E4E4);
		Drawable.drawTriangle(matrices, xPosition + 157, bPos, 5, -90, 0xFFE4E4E4);

		fill(matrices, this.xPosition + 10, this.yPosition + 143, this.xPosition + 50, this.yPosition + 157,
				0xFF000000 | this.rgb);

		FontUtils.drawString(matrices, "#", this.xPosition + 70, this.yPosition + 147, -1);

		colorField.setX(xPosition + 81);
		colorField.setY(yPosition + 143);
		colorField.setWidth(57);
		colorField.setHeight(14);
		colorField.render(matrices, mouseX, mouseY, delta);

		Drawable.drawBorderedCircle(matrices, hPos, sPos, 7, 2, 0xFFFFFFFF, 0xFF000000 | this.rgb);

		this.rgb = (0xFFFFFF & Color.HSBtoRGB(this.hsb[H], this.hsb[S], this.hsb[B]));
		String hex = String.format("%06x", 0xFFFFFF & Color.HSBtoRGB(this.hsb[H], this.hsb[S], this.hsb[B]));
		if (colorField.getText() != hex)
			updateColorFromTextEntry();
	}

	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
		if (this.draggingHS) {
			this.hsb[H] = clamp((float) (mouseX - this.xPosition - 10), 0, 128) / 128F;
			this.hsb[S] = (128F - clamp((float) (mouseY - this.yPosition - 10), 0, 128)) / 128F;
			this.updateColor();
		}

		if (this.draggingB) {
			this.hsb[B] = (128F - clamp((float) (mouseY - this.yPosition - 10), 0, 128)) / 128F;
			this.updateColor();
		}

		return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (Drawable.isHovered(mouseX, mouseY, xPosition + 154, yPosition - 14, 12, 12))
			onClose();

		if (this.rectHSArea.contains(mouseX, mouseY))
			this.draggingHS = true;

		if (this.rectBArea.contains(mouseX, mouseY))
			this.draggingB = true;

		colorField.mouseClicked(mouseX, mouseY, button);
		return super.mouseClicked(mouseX, mouseY, button);
	}

	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		this.draggingHS = false;
		this.draggingB = false;
		return super.mouseReleased(mouseX, mouseY, button);
	}

	@Override
	public boolean mouseScrolled(double d, double e, double amount) {
		return super.mouseScrolled(e, e, amount);
	}

	@Override
	public void onClose() {
		anim = 0.4;
		Helper.openScreen(prev);
	}

	@Override
	public void tick() {
		super.tick();
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		colorField.keyPressed(keyCode, scanCode, modifiers);
		return super.keyPressed(keyCode, scanCode, modifiers);
	}

	@Override
	public boolean charTyped(char chr, int keyCode) {
		colorField.charTyped(chr, keyCode);
		return super.charTyped(chr, keyCode);
	}

	protected void updateColor() {
		this.rgb = (0xFFFFFF & Color.HSBtoRGB(this.hsb[H], this.hsb[S], this.hsb[B]));
		String hex = String.format("%06x", 0xFFFFFF & Color.HSBtoRGB(this.hsb[H], this.hsb[S], this.hsb[B]));
		colorField.setText(hex.toUpperCase());

		setting.setColor((0xFFFFFF & Color.HSBtoRGB(this.hsb[H], this.hsb[S], this.hsb[B])));
	}

	protected void updateColorFromTextEntry() {
		if (colorField.getText().length() < 6)
			return;

		int currentRed = (this.rgb >> 16) & 0xFF;
		int currentGreen = (this.rgb >> 8) & 0xFF;
		int currentBlue = this.rgb & 0xFF;

		String hex = "#" + colorField.getText();
		currentRed = Integer.valueOf(hex.substring(1, 3), 16);
		currentGreen = Integer.valueOf(hex.substring(3, 5), 16);
		currentBlue = Integer.valueOf(hex.substring(5, 7), 16);

		try {
			this.hsb = Color.RGBtoHSB(currentRed, currentGreen, currentBlue, null);
			this.updateColor();
		} catch (Exception e) {

		}
	}

	public static float clamp(float value, float min, float max) {
		return Math.min(Math.max(value, min), max);
	}
}
