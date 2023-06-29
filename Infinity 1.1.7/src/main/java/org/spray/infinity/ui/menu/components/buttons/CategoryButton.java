package org.spray.infinity.ui.menu.components.buttons;

import java.util.ArrayList;
import java.util.List;

import org.spray.infinity.features.Category;
import org.spray.infinity.features.Module;
import org.spray.infinity.font.IFont;
import org.spray.infinity.main.Infinity;
import org.spray.infinity.ui.menu.components.Panel;
import org.spray.infinity.utils.Helper;
import org.spray.infinity.utils.Timer;
import org.spray.infinity.utils.render.Drawable;
import org.spray.infinity.utils.render.RenderUtil;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.Element;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class CategoryButton {

	private ArrayList<ModuleButton> moduleButtons = new ArrayList<>();
	private List<Module> modules = new ArrayList<>();

	private Timer scrollTimer = new Timer();

	private Panel panel;
	private String name;
	private boolean open;

	private boolean scrollHover;

	private double scrollSpeed;
	private double prevScrollProgress;
	private double scrollProgress;
	private int _cbuttonsHeight;

	private double x;
	private double y;
	private double width;
	private double height;

	private double hoverAnim;

	private float fadeAlpha;

	public CategoryButton(String name, List<Module> moduleList, Panel panel) {
		this.name = name;
		this.panel = panel;
		this.modules = moduleList;

		if (moduleList != null) {
			moduleList.forEach(module -> {
				if (module.getCategory() != Category.ENABLED)
					moduleButtons.add(new ModuleButton(module, moduleButtons, panel));
			});
		}
		enabledRefresh();
	}

	public void init() {
		moduleButtons.forEach(ModuleButton::init);
	}

	public void addChildren(List<Element> children) {
		moduleButtons.forEach(moduleButton -> moduleButton.addChildren(children));
	}

	public void enabledRefresh() {
		Infinity.getModuleManager().getEnableModules().clear();
		if (name == panel.ENABLED) {
			moduleButtons.clear();
			for (Module module : Infinity.getModuleManager().getEnableModules()) {
				ModuleButton enabledButton = new ModuleButton(module, moduleButtons, panel);
				moduleButtons.add(enabledButton);
			}
			init();
		}
	}

	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		scrollHover = Drawable.isHovered(mouseX, mouseY, panel.x + 90, panel.y + 37, width + 60, panel.height - 40);

		fadeAlpha = (float) (isOpen() ? Math.min(1, fadeAlpha + 0.16) : 0);
		if (isOpen()) {
			Drawable.drawRectWH(matrices, x, y, width, height, 0xFF2E375A);
			Drawable.verticalGradient(matrices, x, y, x + 2, Math.max(y + height, -1), 0xFF8CEDEB, 0xFF2D4780);
		}

		hoverAnim = Drawable.isHovered(mouseX, mouseY, x, y, width, height) ? RenderUtil.animate(hoverAnim, 1.24)
				: RenderUtil.animate(hoverAnim, 1);

		// icon
		matrices.push();

		GlStateManager._enableBlend();
		matrices.translate(x + 12.5, y + 10.5, 0);
		matrices.scale((float) hoverAnim, (float) hoverAnim, 1f);
		matrices.translate(-x - 12.5, -y - 10.5, 0);
		RenderUtil.drawTexture(matrices,
				new Identifier("infinity", "textures/icons/category/" + this.getName().toLowerCase() + ".png"), x + 7,
				y + 5, 11, 11);

		GlStateManager._disableBlend();
		matrices.pop();

		IFont.legacy16.drawString(matrices, getName(), (int) x + 25, (int) y + 6, 0xFFDBDBDB, false);

		double yMod = 2;

		if (isOpen()) {
			Drawable.startScissor(panel.x + 90, panel.y + 39, 150, panel.height - 40);
			if (scrollHover && _cbuttonsHeight > panel.height) {
				double hy = panel.y + 40;
				double hh = panel.height - 40;
				int k1 = (int) ((hh - hy) * (hh - hy) / _cbuttonsHeight);
				int l1 = (int) (getScrollProgress() * (hh - k1) / getHeightDifference() + hy);

				Drawable.drawVRoundedRect(matrices, panel.x + 235, l1, 1, k1, 0xff1F5A96);
			}
			Drawable.stopScissor();

			for (ModuleButton moduleButton : moduleButtons) {
				Drawable.startScissor(panel.x + 90, panel.y + 39, 150, panel.height - 40);
				_cbuttonsHeight = (int) (panel.y + 37 + yMod);
				moduleButton.setX(panel.x + 94);
				moduleButton.setY(yMod + panel.y + 34 - getScrollProgress());
				moduleButton.setWidth(140);
				moduleButton.setHeight(25);

				yMod += 28;

				RenderSystem.setShaderColor(1f, 1f, 1f, fadeAlpha);
				moduleButton.render(matrices, mouseX, mouseY, delta);
				RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
				Drawable.stopScissor();
			}
		}
	}

	public void tick() {
		if (isOpen()) {
			moduleButtons.forEach(ModuleButton::tick);

			if (_cbuttonsHeight < panel.height - 40) {
				setScrollProgress(0);
				return;
			}

			int difference = getHeightDifference();

			setScrollProgress(scrollProgress + scrollSpeed);
			scrollSpeed *= 0.54;

			if (scrollTimer.hasReached(100)) {
				if (scrollProgress < 0)
					scrollSpeed = scrollProgress * -0.45;
				else if (scrollProgress > difference)
					scrollSpeed = (scrollProgress - difference) * -0.45;
			}
		}
	}

	public void mouseClicked(double mouseX, double mouseY, int button) {
		if (Drawable.isHovered(mouseX, mouseY, x, y, width, height)) {
			if (button != 0)
				return;

			moduleButtons.forEach(ModuleButton::resetAnimation);
			fadeAlpha = -0.3f;
			panel.configPanel.fade = -0.3f;

			setOpen(!isOpen());

			if (name == panel.ENABLED) {
				enabledRefresh();
			}

			panel.getCategoryButtons().forEach(categoryButton -> {
				if (!categoryButton.name.equalsIgnoreCase(name))
					categoryButton.open = false;
			});
		}

		if (isOpen()) {
			if (getName().equalsIgnoreCase(panel.configPanel.getName()))
				panel.configPanel.mouseClicked(mouseX, mouseY, button);
			else
				moduleButtons.forEach(moduleButton -> moduleButton.mouseClicked(mouseX, mouseY, button));
		}
	}

	public void mouseScrolled(double d, double e, double amount) {
		if (isOpen())
			moduleButtons.forEach(moduleButton -> moduleButton.mouseScrolled(d, e, amount));

		if (amount != 0 && scrollHover && isOpen()) {
			double sa = amount < 0 ? amount - 10 : amount + 10;
			scrollTimer.reset();
			scrollSpeed -= sa;
		}
	}

	public void mouseReleased(double mouseX, double mouseY, int button) {
		if (isOpen()) {
			moduleButtons.forEach(moduleButton -> moduleButton.mouseReleased(mouseX, mouseY, button));
		}
	}

	public void charTyped(char chr, int keyCode) {
		if (isOpen()) {
			if (getName().equalsIgnoreCase(panel.configPanel.getName()))
				panel.configPanel.charTyped(chr, keyCode);
			else
				moduleButtons.forEach(moduleButton -> moduleButton.charTyped(chr, keyCode));
		}
	}

	public void keyPressed(int keyCode, int scanCode, int modifiers) {
		if (isOpen()) {
			if (getName().equalsIgnoreCase(panel.configPanel.getName()))
				panel.configPanel.keyPressed(keyCode, scanCode, modifiers);
			else
				moduleButtons.forEach(moduleButton -> moduleButton.keyPressed(keyCode, scanCode, modifiers));
		}
	}

	public void onClose() {
		if (isOpen()) {
			if (getName().equalsIgnoreCase(panel.configPanel.getName()))
				panel.configPanel.onClose();
			else
				moduleButtons.forEach(ModuleButton::onClose);
		}
		fadeAlpha = -0.3f;
	}

	public int getButtonsHeight() {
		int height = 0;
		for (ModuleButton button : moduleButtons) {
			if (isOpen())
				height += (button.getHeight() + 3);
		}
		return height;
	}

	public int getHeightDifference() {
		double diffHeight = panel.height - 40;
		return (int) (this.getButtonsHeight() - diffHeight);
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

	public List<Module> getModules() {
		return modules;
	}

	public ArrayList<ModuleButton> getModuleButtons() {
		return moduleButtons;
	}
}