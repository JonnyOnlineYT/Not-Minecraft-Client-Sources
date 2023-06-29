package org.spray.heaven.ui.clickui.button;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.spray.heaven.features.module.Module;
import org.spray.heaven.features.module.Setting;
import org.spray.heaven.features.module.modules.render.ClickUIMod;
import org.spray.heaven.font.IFont;
import org.spray.heaven.main.Wrapper;
import org.spray.heaven.notifications.Notification.Type;
import org.spray.heaven.ui.clickui.Colors;
import org.spray.heaven.ui.clickui.base.AbstractElement;
import org.spray.heaven.ui.clickui.elements.CheckBoxElement;
import org.spray.heaven.ui.clickui.elements.ColorPickerElement;
import org.spray.heaven.ui.clickui.elements.ComboBoxElement;
import org.spray.heaven.ui.clickui.elements.SliderElement;
import org.spray.heaven.ui.clickui.utils.RippleEffect;
import org.spray.heaven.util.render.ColorUtil;
import org.spray.heaven.util.render.Drawable;
import org.spray.heaven.util.render.animation.Animation;
import org.spray.heaven.util.render.animation.Direction;
import org.spray.heaven.util.render.animation.impl.DecelerateAnimation;

import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.client.renderer.GlStateManager;

public class ModuleButton {

	private final RippleEffect ripple = new RippleEffect();
	private final Animation animation = new DecelerateAnimation(260, 1f, Direction.BACKWARDS);
	public final Animation openAnimation = new DecelerateAnimation(200, 1f, Direction.BACKWARDS);
	private final Animation enableAnimation = new DecelerateAnimation(180, 1f, Direction.BACKWARDS);

	private final List<AbstractElement> elements;
	private final Module module;
	private double x, y, width, height;
	private double offsetY;
	private boolean open;
	private boolean hovered;

	private boolean binding = false;

	public ModuleButton(Module module) {
		this.module = module;
		elements = new ArrayList<>();

		for (Setting setting : module.getSettings()) {
			switch (setting.getCategory()) {
			case BOOLEAN:
				elements.add(new CheckBoxElement(setting));
				break;
			case COLOR:
				elements.add(new ColorPickerElement(setting));
				break;
			case NUMBER:
				elements.add(new SliderElement(setting));
				break;
			case MODE:
				elements.add(new ComboBoxElement(setting));
				break;
			default:
				break;
			}
		}
	}

	public void init() {
		elements.forEach(AbstractElement::init);
	}

	public void tick() {
		if (isOpen())
			elements.forEach(AbstractElement::tick);
	}

	public void render(int mouseX, int mouseY, float delta, Color color, boolean finished) {
		hovered = Drawable.isHovered(mouseX, mouseY, x, y, width, height);
		float fontSize = 0.9f;
		double ix = x + 5;
		double iy = y + height / 2 - (IFont.WEB_SMALL.getFontHeight(fontSize) / 2);

		ClickUIMod clickUI = Wrapper.getModule().get("ClickGUI");

		enableAnimation.setDirection(module.isEnabled() ? Direction.BACKWARDS : Direction.FORWARDS);

		animation.setDirection(hovered ? Direction.FORWARDS : Direction.BACKWARDS);

		if (isOpen()) {

			int sbg = new Color(24, 24, 27).getRGB();
			Drawable.horizontalGradient(x, y + height + 2, (x + width) * (1 - enableAnimation.getOutput()),
					(y + height + 2) + getElementsHeight() * (1 - enableAnimation.getOutput()),
					module.isEnabled() ? ColorUtil.applyOpacity(ClickUIMod.getColor(200), 0.7f).getRGB() : sbg,
					module.isEnabled() ? ColorUtil.applyOpacity(ClickUIMod.getColor(0), 0.7f).getRGB() : sbg);

			double offsetY = 0;
			for (AbstractElement element : elements) {
				if (!element.isVisible())
					continue;

				element.setOffsetY(offsetY);
				element.setX(x);
				element.setY(y + height + 2);
				element.setWidth(width);
				element.setHeight(15);

				if (element instanceof ColorPickerElement)
					element.setHeight(56);

				else if (element instanceof SliderElement)
					element.setHeight(18);

				if (element instanceof ComboBoxElement) {
					ComboBoxElement combobox = (ComboBoxElement) element;
					combobox.setWHeight(17);

					if (combobox.isOpen()) {
						offsetY += (combobox.getSetting().getModes().size() * 12);
					element.setHeight(element.getHeight() + (combobox.getSetting().getModes().size() * 12) + 3);

				}
					else
						element.setHeight(17);
				}

				element.render(mouseX, mouseY, delta);

				offsetY += element.getHeight();
			}

			Drawable.drawBlurredShadow((int) x, (int) (y + height), (int) width, 3, 9, new Color(0, 0, 0, 190));
		}

		Drawable.drawRectWH(x, y, width, isOpen() ? height + 2 : height, new Color(32, 32, 35, 255).getRGB());

		if (!enableAnimation.finished(Direction.FORWARDS)) {
			Drawable.horizontalGradient(x, y, (x + width) * (1 - enableAnimation.getOutput()),
					y + ((isOpen() ? height + 2 : height)),
					ColorUtil.applyOpacity(ClickUIMod.getColor(200), 0.9f).getRGB(),
					ColorUtil.applyOpacity(ClickUIMod.getColor(0), 0.9f).getRGB());
		}

		if (finished) {
			Drawable.startScissor(x, y, width, height + getElementsHeight());

			ripple.render((float) (width + getElementsHeight()) * 0.7f, Colors.RIPPLE_COLOR.getRGB());

			Drawable.stopScissor();
		}

		if (!module.getSettings().isEmpty())
			IFont.WEB_SMALL.drawVCenteredString(isOpen() ? "-" : "+", x + width - 8, y, height,
					clickUI.textShadow.isToggle(), -1);

		GlStateManager.pushMatrix();

		float scale = (float) (1f - (0.03f * animation.getOutput()));
		GlStateManager.translate(ix, iy, 0);
		GlStateManager.scale(scale, scale, 1);
		GlStateManager.translate(-ix, -iy, 0);

		if (binding) {
			String key = Keyboard.getKeyName(module.getKey());
			IFont.WEB_SMALL.drawString("Keybind: " + (key != null ? key : "none"), ix, iy,
					clickUI.textShadow.isToggle(), 0xFFEAEAEA, fontSize);
		} else
			IFont.WEB_SMALL.drawString(module.getName(), ix, iy, clickUI.textShadow.isToggle(), 0xFFEAEAEA, fontSize);

		GlStateManager.popMatrix();

	}

	public void mouseClicked(int mouseX, int mouseY, int button) {
		if (hovered) {
			if (button == 0) {
				module.updateState();
			} else if (button == 1 && !module.getSettings().isEmpty()) {
				setOpen(!isOpen());
			}
			if (button == 2)
				binding = !binding;

			ripple.start(mouseX, mouseY);
		}

		if (open)
			elements.forEach(element -> {
				if (element.isVisible())
					element.mouseClicked(mouseX, mouseY, button);
			});
		else
			resetAnimation();
	}

	public void mouseReleased(int mouseX, int mouseY, int button) {
		if (isOpen())
			elements.forEach(element -> element.mouseReleased(mouseX, mouseY, button));
	}

	public void handleMouseInput() throws IOException {
		if (!isOpen())
			return;
		for (AbstractElement element : elements)
			element.handleMouseInput();
	}

	public void keyTyped(char chr, int keyCode) {
		if (isOpen()) {
			for (AbstractElement element : elements)
				element.keyTyped(chr, keyCode);
		}

		if (binding) {
			if (keyCode == Keyboard.KEY_ESCAPE || keyCode == Keyboard.KEY_SPACE) {
				module.setKey(Keyboard.KEY_NONE);
				Wrapper.notify("Keybind removed from " + ChatFormatting.LIGHT_PURPLE + module.getName(), Type.SUCCESS);
			} else {
				module.setKey(keyCode);
				Wrapper.notify(ChatFormatting.LIGHT_PURPLE + module.getName() + ChatFormatting.WHITE
						+ " keybind has changed to: " + ChatFormatting.GREEN + Keyboard.getKeyName(module.getKey()),
						Type.SUCCESS);
			}
			binding = false;
		}
	}

	public void onGuiClosed() {
		elements.forEach(AbstractElement::onClose);
		ripple.clear();
		resetAnimation();
	}

	public void resetAnimation() {
		elements.forEach(AbstractElement::resetAnimation);
	}

	public List<AbstractElement> getElements() {
		return elements;
	}

	public double getElementsHeight() {
		double offsetY = 0;
		double openedY = 0;
		if (isOpen()) {
			for (AbstractElement element : getElements()) {
				if (element.isVisible())
					offsetY += element.getHeight();
			}
		}
		return offsetY + openedY;
	}

	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
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
		this.y = y + offsetY;
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

	public void setOffsetY(double offsetY) {
		this.offsetY = offsetY;
	}

}