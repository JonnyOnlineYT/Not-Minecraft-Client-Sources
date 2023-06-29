package ru.wendoxd.ui.menu.elements;

import net.minecraft.client.Minecraft;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.input.Mouse;

import ru.wendoxd.ui.menu.Menu;
import ru.wendoxd.ui.menu.utils.MenuAPI;
import ru.wendoxd.utils.fonts.Fonts;
import ru.wendoxd.utils.visual.RenderUtils;
import ru.wendoxd.utils.visual.animation.Animation;
import ru.wendoxd.utils.visual.shaders.FramebufferShell;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Tab {
	private static Minecraft mc = Minecraft.getMinecraft();
	private final Animation outline, enabled;
	private final List<Frame> frames = new ArrayList<>();
	private final String prefix;
	private final String name;
	public double maxWhellOffset, prevWhellOffset, whellOffset, offset;

	public Tab(String prefix, String name) {
		this.outline = new Animation();
		this.enabled = new Animation();
		this.prefix = prefix;
		this.name = name;
	}

	public void draw(float offsetY) {
		double f_o = outline.get();
		double f_e = enabled.get();
		double factor = f_e + f_o * 0.5;
		if (f_e > 0)
			Fonts.ICONS_30.drawString(prefix, 12.3f, 47 + offsetY, RenderUtils.rgba((int) (20 + (70 * f_e)),
					(int) (140 + (70 * f_e)), (int) (140 + (70 * f_e)), 255 * f_e));
		Fonts.ICONS_30.drawString(prefix, 12, 47 + offsetY, RenderUtils.rgba((int) (140 + (70 * factor)),
				(int) (140 + (70 * factor)), (int) (140 + (70 * factor)), 255));
		Fonts.MNTSB_16.drawString(name, 30, name.equalsIgnoreCase("hud") ? 49 + offsetY : 48 + offsetY, RenderUtils
				.rgba((int) (140 + (70 * factor)), (int) (140 + (70 * factor)), (int) (140 + (70 * factor)), 255));
		if (Menu.contextTab == this) {
			FramebufferShell.setupScrollFramebuffer();
			double whellOff = this.prevWhellOffset
					+ (this.whellOffset - this.prevWhellOffset) * mc.getRenderPartialTicks();
			float maxValue = 0;
			float offsetLeftY = 7, offsetRightY = 7;
			int renderedCount = 0;
			for (Frame frame : this.frames) {
				renderedCount++;
				if (offsetLeftY <= offsetRightY) {
					if (f_e == 1)
						offsetLeftY += frame.drawFrame(107, offsetLeftY - (float) whellOff, enabled.get());
					else {
						frame.drawFrame(107, offsetLeftY - (float) whellOff, enabled.get());
						offsetLeftY += frame.predictFrameHeight();
					}
				} else {
					if (f_e == 1) {
						if (offsetRightY + frame.predictFrameHeight() > offsetLeftY
								&& renderedCount == this.frames.size()) {
							offsetLeftY += frame.drawFrame(107, offsetLeftY - (float) whellOff, enabled.get());
						} else {
							offsetRightY += frame.drawFrame(223, offsetRightY - (float) whellOff, enabled.get());
						}
					} else {
						if (offsetRightY + frame.predictFrameHeight() > offsetLeftY
								&& renderedCount == this.frames.size()) {
							offsetLeftY += frame.drawFrame(107, offsetLeftY - (float) whellOff, enabled.get());
						} else {
							frame.drawFrame(223, offsetRightY - (float) whellOff, enabled.get());
							offsetRightY += frame.predictFrameHeight();
						}
					}
				}
				maxValue = Math.max(Math.max(offsetRightY, offsetLeftY), maxValue);
			}
			FramebufferShell.renderScrollFramebuffer(MenuAPI.menuWindow.getY() + 199, MenuAPI.menuWindow.getY());
			for (Frame frame : this.frames) {
				frame.drawSelectBoxes();
			}
			if (enabled.get() == 1) {
				if (maxValue > 196) {
					maxWhellOffset = maxValue - 196;
				} else {
					maxWhellOffset = 0;
				}
			}
		}

	}

	public void keyPress(int key) {
		for (Frame frame : this.frames) {
			frame.keyPress(key);
		}
	}

	public void animation(float offsetY) {
		outline.update(MenuAPI.inBound(7, 40 + (int) offsetY, 93, 62 + (int) offsetY));
		if (this == Menu.contextTab) {
			enabled.update(true);
			for (Frame frame : this.frames) {
				frame.animation();
			}
			double prevOffset = this.offset;
			offset = MathHelper.clamp(offset - Math.signum(offset), -4, 4);
			offset += MathHelper.clamp(-Mouse.getDWheel() / 10f, -20, 20);
			if (offset != prevOffset) {
				this.closeSelectBoxes();
			}
			this.prevWhellOffset = this.whellOffset;
			this.whellOffset = MathHelper.clamp(whellOffset + offset, 0, maxWhellOffset);
		} else {
			enabled.reset();
			for (Frame frame : this.frames) {
				frame.reset();
			}
		}
	}

	public List<Frame> getFrames() {
		return this.frames;
	}

	public String getName() {
		return this.name;
	}

	public void reset() {
		enabled.reset();
	}

	public void register(Frame frame) {
		frame.setTab(this);
		this.frames.add(frame);
		this.frames.sort(new Comparator<Frame>() {
			@Override
			public int compare(Frame o1, Frame o2) {
				return o1.getSortLevel() == 0 && o2.getSortLevel() == 0 ? o1.getName().compareTo(o2.getName())
						: o2.getSortLevel() - o1.getSortLevel();
			}
		});
	}

	public void keyTyped(char c, int keyCode) {
		for (Frame frame : this.frames) {
			frame.keyTyped(c, keyCode);
		}
	}

	public void register(Frame... frames) {
		for (Frame frame : frames) {
			register(frame);
		}
	}

	public boolean isAnimationsAllowed() {
		boolean allowed = true;
		for (Frame frame : this.frames) {
			if (!frame.isAnimationsAllowed()) {
				allowed = false;
			}
		}
		return allowed;
	}

	public void closeSelectBoxes() {
		for (Frame frame : this.frames) {
			frame.closeSelectBoxes();
		}
	}

	public boolean mouseClicked(int x, int y, int mb, float offsetY) {
		boolean swaped = false;
		if (MenuAPI.inBound(6, 40 + (int) offsetY, 94, 62 + (int) offsetY) && mb == 0) {
			Menu.contextTab = this;
			swaped = true;
		}
		if (this == MenuAPI.contextTab) {
			for (Frame frame : this.frames) {
				if (frame.mouseClicked(x, y, mb)) {
					return true;
				}
			}
		}
		return swaped;
	}
}