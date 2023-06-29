package ru.wendoxd.ui.menu.impl.windows;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import ru.wendoxd.ui.menu.elements.tabelements.CheckBox;
import ru.wendoxd.ui.menu.elements.tabelements.SelectBox;
import ru.wendoxd.ui.menu.impl.MenuWindow;
import ru.wendoxd.ui.menu.utils.Bound2D;
import ru.wendoxd.ui.menu.utils.MenuAPI;
import ru.wendoxd.events.EventManager;
import ru.wendoxd.events.impl.menu.EventSwapState;
import ru.wendoxd.modules.BindMode;
import ru.wendoxd.utils.fonts.Fonts;
import ru.wendoxd.utils.visual.animation.Animation;
import ru.wendoxd.utils.visual.RenderUtils;

public class SettingWindow extends MenuWindow {
	private Animation animation, open, closeAnimation;
	private Bound2D bound, key, close;
	private CheckBox visible, checkBox;
	private SelectBox selectBox;
	private boolean binding, opened;

	public SettingWindow(CheckBox checkBox) {
		this.checkBox = checkBox;
		this.selectBox = new SelectBox("Mode", new String[] { "Always", "Hold", "Toggle" }, true, () -> true);
		this.visible = new CheckBox("Visible", this);
		this.bound = new Bound2D(0, 0, 110, 55, true);
		this.key = new Bound2D(77, 25.5f, 105, 34.5f, true);
		this.close = new Bound2D(0, 0, 15, 13.5f, true);
		this.closeAnimation = new Animation();
		this.animation = new Animation();
		this.open = new Animation();
	}

	@Override
	public void draw() {
		double anim = open.get();
		if (anim == 0)
			return;
		GL11.glPushMatrix();
		RenderUtils.sizeAnimation(width(), height(), anim * MenuAPI.transparencies.get());
		String key = Keyboard.getKeyName(this.checkBox.getKey());
		int width = Fonts.MNTSB_12.getStringWidth(key);
		RenderUtils.drawRect(0, 0, 110, 20, RenderUtils.rgba(25, 25, 25, 255));
		RenderUtils.drawRect(0, 20, 110, 67, RenderUtils.rgba(23, 23, 23, 255));
		if (anim == 1 && MenuAPI.transparencies.get() == 1) {
			Fonts.MNTSB_12.drawString(this.checkBox.getName(), 15, 8.5f, RenderUtils.rgba(180, 180, 180, 255));
			Fonts.ICONS_20.drawString("h", 5, 8.5f, RenderUtils.rgba(180, 180, 180, 255));
			Fonts.ICONS_25.drawString("i", 95, 7.5f, RenderUtils.rgba(180 + (40 * closeAnimation.get()),
					180 + (40 * closeAnimation.get()), 180 + (40 * closeAnimation.get()), 255));
			this.visible.draw(0, 49);
			this.selectBox.draw(0, 35);
			this.selectBox.drawSelectBox();
			Fonts.MNTSB_12.drawString("Key", 7.5f, 28f, RenderUtils.rgba(180, 180, 180, 255));
			RenderUtils.drawRect(105 - width - 5, 25.5f, 105, 34.5f, RenderUtils.rgba(27 + (7 * animation.get()),
					27 + (7 * animation.get()), 27 + (7 * animation.get()), 255));
			Fonts.MNTSB_12.drawString(key, 102 - width, 28.5f, RenderUtils.rgba(180, 180, 180, 255));
		}
		GL11.glPopMatrix();
	}

	public void update(boolean opened) {
		this.opened = opened;
	}

	@Override
	public boolean click(int x, int y, int mb) {
		if (!isActive())
			return false;
		if (this.close.inBound()) {
			update(false);
			return true;
		}
		this.binding = key.inBound() && mb == 0;
		boolean sb = selectBox.mouseClicked(getX() + 57, getY() + 40, mb);
		boolean v = this.visible.mouseClicked(getX(), getY() + 42, mb);
		if (sb) {
			boolean prevValue = this.checkBox.isEnabled(false);
			this.checkBox.setBindMode(BindMode.values()[this.selectBox.get()]);
			if (prevValue != this.checkBox.isEnabled(false)) {
				EventManager.call(new EventSwapState(this.checkBox, false));
			}
		}
		if (v && !sb) {
			this.checkBox.setVisibleInKeybinds(this.visible.isEnabled(true));
		}
		return sb || v;
	}

	@Override
	public Bound2D getBound() {
		return this.bound;
	}

	@Override
	public boolean isActive() {
		return this.opened;
	}

	@Override
	public void onAnimation() {
		this.open.update(this.opened);
		this.visible.animation();
		if (!isActive())
			return;
		if (!this.checkBox.isEnabled(true))
			this.update(false);
		this.close.offset(getX() + 91, getY() + 4f);
		this.closeAnimation.update(this.close.inBound());
		this.selectBox.offset(getX(), getY() + 35);
		this.selectBox.animation();
		String key = Keyboard.getKeyName(this.checkBox.getKey());
		int width = Fonts.MNTSB_12.getStringWidth(key);
		this.key.offset(getX() + 105 - width - 5, getY() + 25);
		this.animation.update(this.key.inBound());
		this.selectBox.setIndex(this.checkBox.getBindMode().ordinal());
		this.visible.setEnabled(this.checkBox.isVisibleInKeybinds());
		if (MenuAPI.transparencies.get() < 1) {
			this.binding = false;
		}
		if (this.binding) {
			if (Keyboard.getEventKey() == Keyboard.KEY_INSERT || Keyboard.getEventKey() == Keyboard.KEY_DELETE)
				this.checkBox.setKeybind(Keyboard.KEY_NONE);
			else if (Keyboard.isKeyDown(Keyboard.getEventKey()))
				this.checkBox.setKeybind(Keyboard.getEventKey());
		}
	}

	@Override
	public int width() {
		return 110;
	}

	@Override
	public int height() {
		return 67;
	}
}
