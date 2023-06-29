package ru.wendoxd.ui.menu.elements;

import ru.wendoxd.modules.Module;
import ru.wendoxd.ui.menu.elements.tabelements.CheckBox;
import ru.wendoxd.ui.menu.elements.tabelements.MultiSelectBox;
import ru.wendoxd.ui.menu.elements.tabelements.PrimaryButton;
import ru.wendoxd.ui.menu.elements.tabelements.SelectBox;
import ru.wendoxd.ui.menu.utils.MenuAPI;
import ru.wendoxd.utils.fonts.Fonts;
import ru.wendoxd.utils.visual.RenderUtils;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

public class Frame {
	private Tab tab;
	private final ArrayList<PrimaryButton> buttons = new ArrayList<>();
	private final String name;
	private int sort;

	public Frame(String name) {
		this.name = name;
	}

	public int getSortLevel() {
		return this.sort;
	}

	public Frame ignoreSort() {
		this.sort = 99999;
		return this;
	}

	public float drawFrame(float offsetX, float offsetY, double animation) {
		offsetY = offsetY - 8 + (float) (8 * animation);
		RenderUtils.drawRect(offsetX, offsetY, offsetX + 110, offsetY + 20, RenderUtils.rgba(25, 25, 25, 255));
		Fonts.MNTSB_14.drawString(name, offsetX + 7, offsetY + 9f, RenderUtils.rgba(198, 198, 198, 255));
		float total = 0;
		for (PrimaryButton button : this.buttons) {
			if (animation < 1 && button.isVisible()) {
				button.set((float) animation);
			}
			total = total + button.draw(offsetX, offsetY + total + 20);
		}
		return 25 + total;
	}

	public float predictFrameHeight() {
		float total = 0;
		for (PrimaryButton button : this.buttons) {
			if (button.isVisible())
				total = total + 16;
		}
		return 25 + total;
	}

	public void drawSelectBoxes() {
		for (PrimaryButton button : this.buttons) {
			if (button instanceof SelectBox) {
				SelectBox sb = (SelectBox) button;
				sb.drawSelectBox();
			}
			if (button instanceof MultiSelectBox) {
				MultiSelectBox sb = (MultiSelectBox) button;
				sb.drawSelectBox();
			}
			double x = button.boundAnimation() * MenuAPI.transparencies.get();
			if (x > 0 && button.getDescription() != null && Module.visuals_menu_desc.isEnabled(false)
					&& MenuAPI.menuWindow.getBound().inBound()) {
				GL11.glPushMatrix();
				GL11.glTranslated(-MenuAPI.menuWindow.getX(), -MenuAPI.menuWindow.getY(), 0);
				String[] desc = button.getDescription().split("\n");
				float startX = MenuAPI.mouseX + 10;
				float startY = MenuAPI.mouseY;
				float maxWidth = 0;
				for (String s : desc) {
					maxWidth = Math.max(maxWidth, Fonts.MNTSB_12.getStringWidth(s));
				}
				maxWidth = Math.max(maxWidth, Fonts.MNTSB_13.getStringWidth(button.getName()));
				float width = maxWidth + 8;
				float height = 11 + desc.length * 7 + 0.5f;
				int i = 0;
				RenderUtils.sizeAnimation(startX * 2 + width, startY * 2 + height, x);
				RenderUtils.drawRect(startX, startY, startX + width, startY + height,
						RenderUtils.rgba(23, 23, 23, 255));
				Fonts.MNTSB_13.drawString(button.getName(), startX + 4, startY + 4,
						RenderUtils.rgba(200, 200, 200, 255));
				for (String s : desc) {
					Fonts.MNTSB_12.drawString(s, startX + 4, startY + 12 + i * 7,
							RenderUtils.rgba(160, 160, 160, 255));
					i++;
				}
				GL11.glPopMatrix();
			}
		}
	}

	public boolean isAnimationsAllowed() {
		boolean allowed = true;
		for (PrimaryButton button : this.buttons) {
			if (button instanceof SelectBox) {
				SelectBox sb = (SelectBox) button;
				if (sb.isOpened()) {
					allowed = false;
				}
			}
			if (button instanceof MultiSelectBox) {
				MultiSelectBox msb = (MultiSelectBox) button;
				if (msb.isOpened()) {
					allowed = false;
				}
			}
		}
		return allowed;
	}

	public void keyTyped(char c, int keyCode) {
		for (PrimaryButton button : this.buttons) {
			button.keyTyped(c, keyCode);
		}
	}

	public Tab getTab() {
		return this.tab;
	}

	public void setTab(Tab tab) {
		this.tab = tab;
	}

	public void register(PrimaryButton button) {
		button.setFrame(this);
		this.buttons.add(button);
	}

	public void register(PrimaryButton... buttons) {
		for (PrimaryButton button : buttons) {
			register(button);
		}
	}

	public void keyPress(int key) {
		for (PrimaryButton button : this.buttons) {
			if (button instanceof CheckBox) {
				CheckBox cb = (CheckBox) button;
				cb.keyPress(key);
			}
		}
	}

	public void closeSelectBoxes() {
		for (PrimaryButton button : buttons) {
			if (button instanceof MultiSelectBox) {
				MultiSelectBox msb = (MultiSelectBox) button;
				msb.close();
			}
			if (button instanceof SelectBox) {
				SelectBox sb = (SelectBox) button;
				sb.close();
			}
		}
	}

	public void animation() {
		for (PrimaryButton button : this.buttons) {
			button.animation();
		}
	}

	public List<PrimaryButton> getButtons() {
		return this.buttons;
	}

	public boolean mouseClicked(int x, int y, int mb) {
		if (!MenuAPI.bound.inBound() && tab.isAnimationsAllowed()) {
			return false;
		}
		for (PrimaryButton button : this.buttons) {
			if (button.isVisible() && button.mouseClicked(x, y, mb)) {
				return true;
			}
		}
		return false;
	}

	public void reset() {
		for (PrimaryButton button : this.buttons) {
			button.reset();
		}
	}

	public String getName() {
		return this.name;
	}
}
