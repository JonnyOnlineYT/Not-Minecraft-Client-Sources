package ru.wendoxd.ui.menu.elements.tabelements;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.util.math.MathHelper;
import ru.wendoxd.WexSide;
import ru.wendoxd.events.EventManager;
import ru.wendoxd.events.impl.menu.EventSwapState;
import ru.wendoxd.modules.BindMode;
import ru.wendoxd.modules.impl.visuals.ClientSounds;
import ru.wendoxd.modules.impl.visuals.Notifications;
import ru.wendoxd.ui.menu.impl.MenuWindow;
import ru.wendoxd.ui.menu.impl.windows.ColorWindow;
import ru.wendoxd.ui.menu.impl.windows.SettingWindow;
import ru.wendoxd.ui.menu.utils.Bound2D;
import ru.wendoxd.ui.menu.utils.MenuAPI;
import ru.wendoxd.utils.fonts.Fonts;
import ru.wendoxd.utils.sound.SoundUtils;
import ru.wendoxd.utils.visual.ColorShell;
import ru.wendoxd.utils.visual.RenderUtils;
import ru.wendoxd.utils.visual.animation.Animation;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.function.Supplier;

public class CheckBox extends PrimaryButton {

	private final Animation animation;
	private final Animation animationEnabled;
	private final Animation animationOutline;
	private final Animation settingOutline;
	private final Animation colorOutline;
	private final Animation animationEnabledKeybind;
	private Bound2D bound;
	private final Bound2D settingBound;
	private final Bound2D colorBound;
	private MenuWindow settingWindow, contextWindow;
	private ColorWindow colorWindow;
	private final ColorShell colorShell;
	private String keyBindName, arrayListName, desc;
	private BindMode bindMode;
	private int keybind, prevTick, tick;
	private boolean enabled, toggled, visibleInKeybinds;

	public CheckBox(String name) {
		this(name, () -> true);
	}

	public CheckBox(String name, MenuWindow contextWindow) {
		this(name, () -> true);
		this.contextWindow = contextWindow;
		this.bound = new Bound2D(0, 0, 0, 0, true);
	}

	public CheckBox(String name, Supplier<Boolean> condition) {
		super(name, condition);
		this.animation = new Animation();
		this.animationEnabled = new Animation();
		this.animationOutline = new Animation();
		this.settingOutline = new Animation();
		this.colorOutline = new Animation();
		this.animationEnabledKeybind = new Animation();
		this.bound = new Bound2D(0, 0, 0, 0);
		this.settingBound = new Bound2D(0, 0, 0, 0);
		this.colorBound = new Bound2D(0, 0, 0, 0);
		this.colorShell = new ColorShell();
		this.bindMode = BindMode.ALWAYS;
	}

	public CheckBox markDesc(String desc) {
		this.desc = desc;
		return this;
	}

	public CheckBox markSetting(String keyBindName) {
		this.settingWindow = new SettingWindow(this);
		MenuAPI.windows.add(this.settingWindow);
		this.keyBindName = keyBindName;
		return this;
	}

	public CheckBox markColorPicker() {
		this.colorWindow = new ColorWindow(this);
		MenuAPI.windows.add(this.colorWindow);
		return this;
	}

	public CheckBox markArrayList(String name) {
		this.arrayListName = name;
		return this;
	}

	public String getKeyBindName() {
		return this.keyBindName;
	}

	public BindMode getBindMode() {
		return this.bindMode;
	}

	public void setBindMode(BindMode bindMode) {
		this.bindMode = bindMode;
	}

	public void setKeybind(int keybind) {
		this.keybind = keybind;
	}

	public int getKey() {
		return this.keybind;
	}

	public void keyPress(int key) {
		if (this.keybind == key && this.bindMode == BindMode.TOGGLE) {
			this.toggled = !toggled;
			EventManager.call(new EventSwapState(this, toggled));
			if (ClientSounds.clientSounds.isEnabled(false) && ClientSounds.selectBox.get() == 0) {
				SoundUtils.playSound(Math.max(ClientSounds.pitch.getFloatValue() + (this.toggled ? -0.25f : 0), 0),
						ClientSounds.volume.getFloatValue());
			}
			if (this.visibleInKeybinds && this.enabled) {
				Notifications.notify("Modules",
						"Module \"" + this.keyBindName + "\" is " + (this.toggled ? "enabled" : "disabled") + ".", 0);
			}
		}
	}

	public boolean isVisibleInKeybinds() {
		return this.visibleInKeybinds;
	}

	public void setVisibleInKeybinds(boolean visible) {
		this.visibleInKeybinds = visible;
	}

	public void tick() {
		prevTick = tick;
		tick = MathHelper.clamp(tick + (this.isEnabled(false) ? 1 : -1), 0, 5);
	}

	public String arrayListName() {
		return this.arrayListName;
	}

	public float getAnimation() {
		return (float) WexSide.createAnimation(
				(this.prevTick + (this.tick - this.prevTick) * Minecraft.getMinecraft().getRenderPartialTicks()) / 5.f);
	}

	@Override
	public float draw(float x, float yOffset) {
		float anim = (float) animation.get();
		float outline = (float) animationOutline.get();
		float enabled = (float) animationEnabled.get();
		RenderUtils.drawRect(x, yOffset, x + 110, yOffset + 16 * anim, RenderUtils.rgba(23, 23, 23, 255));
		if (animation.get() > 0) {
			if (this.settingWindow != null && enabled > 0) {
				Fonts.ICONS_20.drawString("h", x + 84.5f, yOffset + (7.5F * anim),
						RenderUtils.rgba(170 + 45 * settingOutline.get(), 170 + 45 * settingOutline.get(),
								170 + 45 * settingOutline.get(), 255 * anim * enabled));
				this.settingBound.update(x + 82, yOffset + 2.5f, x + 94, yOffset + 17);
			}
			if (this.colorWindow != null && enabled > 0) {
				Fonts.ICONS_20.drawString("g", x + (this.settingWindow != null ? 75f : 84.5f), yOffset + 7f,
						RenderUtils.rgba(170 + 45 * colorOutline.get(), 170 + 45 * colorOutline.get(),
								170 + 45 * colorOutline.get(), 255 * anim * enabled));
				this.colorBound.update(x + 82 - (settingWindow != null ? 11 : 0), yOffset + 2.5f,
						x + 94 - (settingWindow != null ? 11 : 0), yOffset + 17);
			}
			Fonts.MNTSB_13.drawString(name, x + 7, yOffset + 7 * anim,
					RenderUtils.rgba(170 + (45 * outline), 170 + (45 * outline), 170 + (45 * outline), (255 * anim)));
			MenuAPI.drawCheckBoxCircleMenu(x + (this.contextWindow instanceof ColorWindow ? 50 : 90),
					yOffset + (5 * anim),
					RenderUtils.rgba(110 + (55 * enabled) + (45 * outline), 110 + (55 * enabled) + (45 * outline),
							110 + (55 * enabled) + (45 * outline), (255 * anim)),
					RenderUtils.rgba(90 + (55 * enabled) + (35 * outline), 90 + (55 * enabled) + (35 * outline),
							90 + (55 * enabled) + (35 * outline), (255 * anim)),
					anim * enabled, contextWindow == null ? MenuAPI.menuWindow : contextWindow);
			this.bound.update(x, yOffset, x + (this.contextWindow instanceof ColorWindow ? 70 : 110),
					yOffset + 17 * anim);
		}
		return 16 * anim;
	}

	public float getAnimationEnabledKeybind() {
		return (float) this.animationEnabledKeybind.get();
	}

	public void updateAnimationEnabled() {
		if (!this.enabled) {
			this.animationEnabledKeybind.update(false);
			return;
		}
		if (this.bindMode == bindMode.HOLD) {
			this.animationEnabledKeybind.update(Keyboard.isKeyDown(this.keybind));
		}
		if (this.bindMode == bindMode.ALWAYS) {
			this.animationEnabledKeybind.update(this.enabled);
		}
		if (this.bindMode == bindMode.TOGGLE) {
			this.animationEnabledKeybind.update(this.toggled);
		}
	}

	@Override
	public void animation() {
		if (this.contextWindow != null && this.contextWindow.isActive()) {
			this.bound.offset(this.contextWindow.getX(),
					this.contextWindow instanceof SettingWindow ? this.contextWindow.getY() + 50
							: this.contextWindow.getY() + 147);
		}
		this.settingOutline.update(this.settingBound.inBound());
		this.colorOutline.update(this.colorBound.inBound());
		boolean animationUpdate = this.contextWindow == null ? this.isVisible() : this.contextWindow.isActive();
		this.animation.update(animationUpdate);
		this.animationEnabled.update(this.isEnabled(true));
		this.animationOutline.update(this.bound.inBound() && MenuAPI.contextTab.isAnimationsAllowed());
	}

	public ColorShell getColor() {
		return this.colorShell;
	}

	public String getName() {
		return this.name;
	}

	public boolean isEnabled(boolean menu) {
		if (!this.enabled) {
			return false;
		}
		if (menu || this.bindMode == BindMode.ALWAYS) {
			return this.enabled && (this.isVisible());
		} else if (this.bindMode == BindMode.HOLD) {
			return Keyboard.isKeyDown(this.keybind);
		} else if (this.bindMode == BindMode.TOGGLE) {
			return this.toggled;
		}
		return false;
	}

	public boolean isEnabled() {
		return this.enabled;
	}

	public void setEnabled(boolean en) {
		this.enabled = en;
		EventManager.call(new EventSwapState(this, en));
	}

	public void setEnabledCFG(boolean en) {
		this.enabled = en;
	}

	@Override
	public boolean mouseClicked(int x, int y, int mb) {
		if (settingBound.inBound() && isEnabled(true)) {
			boolean prev = this.settingWindow.isActive();
			for (MenuWindow window : MenuAPI.windows) {
				if (window instanceof SettingWindow) {
					SettingWindow wnd = (SettingWindow) window;
					wnd.update(false);
				}
			}
			this.settingWindow.update(!prev);
			if (this.settingWindow.isActive()) {
				int yWindowOffset = 0;
				if (this.colorWindow != null && this.colorWindow.isActive()) {
					yWindowOffset += 67;
				}
				this.settingWindow.setXY(MenuAPI.menuWindow.getX() + MenuAPI.bound.getMaxX() + 5, y - yWindowOffset);
			}
			return true;
		}
		if (colorBound.inBound() && isEnabled(true)) {
			boolean prev = this.colorWindow.isActive();
			for (MenuWindow window : MenuAPI.windows) {
				if (window instanceof ColorWindow) {
					window.update(false);
				}
			}
			this.colorWindow.update(!prev);
			if (this.colorWindow.isActive()) {
				int yWindowOffset = 0;
				if (this.settingWindow != null && this.settingWindow.isActive()) {
					yWindowOffset += 160;
				}
				this.colorWindow.setXY(MenuAPI.menuWindow.getX() + MenuAPI.bound.getMaxX() + 5, y - yWindowOffset);
			}
			return true;
		}
		if (this.contextWindow != null)
			this.bound.offset(this.contextWindow.getX(),
					this.contextWindow instanceof SettingWindow ? this.contextWindow.getY() + 50
							: this.contextWindow.getY() + 147);
		boolean inBound = this.bound.inBound();
		if (inBound) {
			this.setEnabled(!this.enabled);
		}
		return inBound;
	}

	@Override
	public void reset() {
		this.animationEnabled.reset();
		this.animationOutline.reset();
	}

	@Override
	public void set(float animation) {
		this.animation.set(animation);
	}

	@Override
	public void load(DataInputStream dis) throws Exception {
		this.enabled = dis.readBoolean();
		this.setEnabledCFG(this.enabled);
		this.keybind = dis.readInt();
		this.bindMode = BindMode.values()[dis.readByte()];
		this.visibleInKeybinds = dis.readBoolean();
		this.colorShell.setRGBA(dis.readShort(), dis.readShort(), dis.readShort(), dis.readShort());
		this.colorShell.setRainbow(dis.readBoolean());
		this.reset();
	}

	@Override
	public void save(DataOutputStream dos) throws Exception {
		dos.writeUTF(this.getPath());
		dos.writeBoolean(this.enabled);
		dos.writeInt(this.getKey());
		dos.writeByte(this.bindMode.ordinal());
		dos.writeBoolean(this.visibleInKeybinds);
		dos.writeShort(this.colorShell.getRed());
		dos.writeShort(this.colorShell.getGreen());
		dos.writeShort(this.colorShell.getBlue());
		dos.writeShort(this.colorShell.getAlpha());
		dos.writeBoolean(this.colorShell.isRainbow());
	}

	@Override
	public double boundAnimation() {
		return this.animationOutline.get() * animation.get();
	}
}
