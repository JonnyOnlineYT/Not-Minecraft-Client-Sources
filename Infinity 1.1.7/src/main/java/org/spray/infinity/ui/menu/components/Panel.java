package org.spray.infinity.ui.menu.components;

import java.util.ArrayList;
import java.util.List;

import org.spray.infinity.features.Category;
import org.spray.infinity.font.IFont;
import org.spray.infinity.main.Infinity;
import org.spray.infinity.ui.menu.ClickMenu;
import org.spray.infinity.ui.menu.components.buttons.CategoryButton;
import org.spray.infinity.ui.menu.components.config.ConfigPanel;
import org.spray.infinity.utils.Helper;
import org.spray.infinity.utils.render.Drawable;

import net.minecraft.client.gui.Element;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;

public class Panel {

	public ArrayList<CategoryButton> categoryButtons = new ArrayList<>();
	public ConfigPanel configPanel = new ConfigPanel("Config");
	public String ENABLED = "Enabled";

	private ClickMenu clickMenu;

	public double x;
	public double y;
	public double width;
	public double height;

	private double prevX;
	private double prevY;

	private boolean hovered;
	private boolean closeHovered;

	private boolean dragging;

	public Panel(ClickMenu clickMenu, double x, double y, double width, double height) {
		this.clickMenu = clickMenu;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		for (Category category : Category.values()) {
			if (category.equals(Category.HIDDEN))
				continue;

			categoryButtons.add(
					new CategoryButton(category.getName(), Infinity.getModuleManager().getByCategory(category), this));
		}
		categoryButtons.add(new CategoryButton(configPanel.getName(), null, this));
	}

	public void init() {
		configPanel.init();

		categoryButtons.forEach(CategoryButton::init);
	}

	public void addChildren(List<Element> children) {
		categoryButtons.forEach(categoryButton -> categoryButton.addChildren(children));
	}

	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		hovered = Drawable.isHovered(mouseX, mouseY, x + 92, y, (width - 92) - 50, 29);
		closeHovered = Drawable.isHovered(mouseX, mouseY, x + width - 20, y - 11, 20, 11);
		if (dragging) {
			x = prevX + mouseX;
			y = prevY + mouseY;
		}

		// drag borders
		Window window = Helper.MC.getWindow();
		double scaleWidth = window.getScaledWidth();
		double scaleHeight = window.getScaledHeight();
		if (x < 0) {
			x = 0;
		}
		if (x > scaleWidth) {
			x = scaleWidth - x;
		}
		if (y < 0) {
			y = 0;
		}
		if (y > scaleHeight) {
			y = scaleHeight - y;
		}

		// panel
		Drawable.drawRectWH(matrices, x, y + 1, width, height - 1, 0xFF181818);
		Drawable.verticalGradient(matrices, x + 92, y + 1, x + width - 2, y + height - 2, 0xFF20202F, 0xFF243670);

		// category panel
		Drawable.drawRectWH(matrices, x + 2, y + 2, 90, height - 4, 0xFF161621);
		IFont.legacy13.drawString(matrices, "v" + Infinity.VERSION, x + 2, y + height - 12, 0xFF464746);

		// header
		Drawable.drawRectWH(matrices, x + 92, y + 3, width - 92, 28 - 1, 0xFF161621);
		Drawable.drawRectWH(matrices, x + 92, y + 2, width - 92, 1, 0xFF1F1F1F);
		Drawable.verticalGradient(matrices, x + 92, y + 28, x + width - 1, y + 33, 0xFF8EA8E0, 0xFF2E3349);

		Drawable.drawRectWH(matrices, x + 6, y + 3, 80, 0.5, 0xFF4A4F65);
		

		double offsetY = 2;
		for (CategoryButton categoryButton : categoryButtons) {
			categoryButton.setX(x);
			categoryButton.setY(offsetY + y + 5);
			categoryButton.setWidth(90);
			categoryButton.setHeight(20);

			offsetY += 21;

			categoryButton.render(matrices, mouseX, mouseY, delta);

			if (categoryButton.isOpen() && categoryButton.getName().equalsIgnoreCase(configPanel.getName())) {
				configPanel.setX(x + 92);
				configPanel.setY(y + 34);
				configPanel.setWidth(width - 92);
				configPanel.setHeight(height);
				configPanel.render(matrices, mouseX, mouseY, delta);
			}
		}
	}

	public void tick() {
		categoryButtons.forEach(CategoryButton::tick);
		configPanel.tick();
	}

	public void mouseClicked(double mouseX, double mouseY, int button) {
		if (closeHovered && button == 0) {
			dragging = false;
			clickMenu.onClose();
		}

		categoryButtons.forEach(categoryButton -> {
			categoryButton.mouseClicked(mouseX, mouseY, button);
		});

		if (hovered && button == 0) {
			dragging = true;
			prevX = x - mouseX;
			prevY = y - mouseY;
		}
	}

	public void mouseScrolled(double d, double e, double amount) {
		categoryButtons.forEach(categoryButton -> {
			categoryButton.mouseScrolled(d, e, amount);
		});

		configPanel.mouseScrolled(d, e, amount);
	}

	public void mouseReleased(double mouseX, double mouseY, int button) {
		if (button == 0) {
			dragging = false;
		}
		categoryButtons.forEach(categoryButton -> {
			categoryButton.mouseReleased(mouseX, mouseY, button);
		});

	}

	public void charTyped(char chr, int keyCode) {
		categoryButtons.forEach(categoryButton -> {
			categoryButton.charTyped(chr, keyCode);
		});
	}

	public void keyPressed(int keyCode, int scanCode, int modifiers) {
		categoryButtons.forEach(categoryButton -> {
			categoryButton.keyPressed(keyCode, scanCode, modifiers);
		});
	}

	public void onClose() {
		categoryButtons.forEach(categoryButton -> {
			categoryButton.onClose();
		});
		configPanel.onClose();
		dragging = false;
	}

	public ArrayList<CategoryButton> getCategoryButtons() {
		return categoryButtons;
	}
}
