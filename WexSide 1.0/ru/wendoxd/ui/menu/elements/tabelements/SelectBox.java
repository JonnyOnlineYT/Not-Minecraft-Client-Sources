package ru.wendoxd.ui.menu.elements.tabelements;

import ru.wendoxd.ui.menu.utils.Bound2D;
import ru.wendoxd.ui.menu.utils.MenuAPI;
import ru.wendoxd.utils.fonts.Fonts;
import ru.wendoxd.utils.visual.RenderUtils;
import ru.wendoxd.utils.visual.animation.Animation;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class SelectBox extends PrimaryButton {
	private Map<Integer, Bound2D> bounds = new HashMap<>();
	private List<Animation> animations = new ArrayList<>();
	private Animation animation, animationOutline, animationOpened;
	private Bound2D selectBound, bound;
	private String[] values;
	private boolean opened;
	private boolean interp;
	public int selected, offsetX, offsetY;
	private float x, yOffset;

	public SelectBox(String name, String[] values) {
		this(name, values, false, null);
	}

	public SelectBox(String name, boolean interp, String[] values) {
		this(name, values, interp, null);
	}

	public SelectBox(String name, String[] values, Supplier<Boolean> condition) {
		this(name, values, false, condition);
	}

	public SelectBox(String name, String[] values, boolean interp, Supplier<Boolean> condition) {
		super(name, condition);
		this.animation = new Animation();
		this.animationOutline = new Animation(0.01);
		this.animationOpened = new Animation(0.01);
		this.values = values;
		this.interp = interp;
		this.selectBound = new Bound2D(0, 0, 0, 0, interp);
		this.bound = new Bound2D(0, 0, 0, 0, interp);
		this.bounds.clear();
		this.animations.clear();
		for (int i = 0; i < values.length; i++) {
			bounds.put(i, new Bound2D(0, 0, 0, 0, interp));
			animations.add(new Animation());
		}
	}

	@Override
	public float draw(float x, float yOffset) {
		this.x = x;
		this.yOffset = yOffset;
		RenderUtils.drawRect(x, yOffset, x + 110, (float) (yOffset + 16 * animation.get()),
				RenderUtils.rgba(23, 23, 23, 255));
		if (animation.get() > 0) {
			RenderUtils.drawRect(x + 53, yOffset + 3.5f, x + 105, (float) (yOffset + 12.5f * animation.get()),
					RenderUtils.rgba(27, 27, 27, 255));
			Fonts.MNTSB_13.drawString(name, x + 7, yOffset + 7f * (float) animation.get(),
					RenderUtils.rgba(170 + (45 * animationOutline.get()), 170 + (45 * animationOutline.get()),
							170 + (45 * animationOutline.get()), (255 * animation.get())));
			if (!this.interp) {
				Fonts.MNTSB_13.drawSubstring(values[selected], x + 55f, yOffset + 6.7f * (float) animation.get(),
						RenderUtils.rgba(150 + 45 * animationOutline.get(), 150 + 45 * animationOutline.get(),
								150 + 45 * animationOutline.get(), (255 * animation.get())),
						34);
			} else {
				Fonts.MNTSB_13.drawString(values[selected], x + 55f, yOffset + 6.7f * (float) animation.get(),
						RenderUtils.rgba(150 + 45 * animationOutline.get(), 150 + 45 * animationOutline.get(),
								150 + 45 * animationOutline.get(), (255 * animation.get())));
			}
			RenderUtils.drawArrow(x + 98, yOffset + (float) (6.5f * animation.get()), opened,
					RenderUtils.rgba(170, 170, 170, (int) (255 * animation.get())), 0.7f);
			this.selectBound.update(x + 53, yOffset + 3.5f, x + 105, (float) (yOffset + 12.5f * animation.get()));
			this.bound.update(x, yOffset, x + 110, (float) (yOffset + 16 * animation.get()));
		}
		return (float) (16 * animation.get());
	}

	public void setIndex(int index) {
		if (index > this.values.length - 1) {
			System.out.println("Oops, old config.");
		} else {
			this.selected = index;
		}
	}

	public void drawSelectBox() {
		double anim = this.animationOpened.get() * MenuAPI.transparencies.get();
		if (anim > 0 && this.animation.get() > 0) {
			float height = this.values.length * 8f;
			RenderUtils.drawRect(x + 53, yOffset + 13.5f, x + 105,
					(float) (yOffset + (14 + height) * animation.get() * anim), RenderUtils.rgba(27, 27, 27, 255));
			for (int i = 0; i < this.values.length; i++) {
				Fonts.MNTSB_13.drawString(values[i], x + 55, yOffset + (float) ((16 + (i * 8f)) * anim),
						RenderUtils.rgba(150 + (animations.get(i).get() * 60), 150 + (animations.get(i).get() * 60),
								150 + (animations.get(i).get() * 60), (255 * animation.get() * anim)));
				float start = yOffset + (float) (17.5f + (i * 8f) * anim) - 4 + offsetY;
				float end = yOffset + (float) (17.5f + (i * 8f) * anim) + 4.5f + offsetY;
				bounds.put(i, new Bound2D(x + 53 + offsetX, start, x + 105 + offsetX, end, interp));
			}
		}
	}

	public void offset(int x, int y) {
		this.bound.offset(x, y);
		this.offsetX = x;
		this.offsetY = y - 35;
	}

	public boolean isOpened() {
		return this.opened;
	}

	public int get() {
		if (isIgnoreVisible()) {
			return this.selected;
		}
		return this.isVisible() ? this.selected : -1;
	}

	public void close() {
		this.opened = false;
	}

	@Override
	public void animation() {
		if (!isVisible()) {
			this.opened = false;
		}
		this.animation.update(this.isVisible());
		this.animationOutline.update(bound.inBound() && MenuAPI.contextTab.isAnimationsAllowed());
		this.animationOpened.update(this.opened);
		this.bounds.forEach((id, bound) -> this.animations.get(id).update(bound.inBound()));
	}

	@Override
	public boolean mouseClicked(int x, int y, int mb) {
		if (this.interp) {
			this.selectBound.offset(x, y);
		}
		boolean inBound = this.selectBound.inBound();
		if (this.opened) {
			bounds.forEach((id, bound) -> {
				if (bound.inBound()) {
					this.opened = false;
					this.selected = id;
				}
			});
			close();
			return true;
		} else {
			if (inBound) {
				MenuAPI.contextTab.closeSelectBoxes();
				this.opened = true;
				return true;
			}
			return false;
		}
	}

	@Override
	public void reset() {
		this.animation.reset();
		this.animationOutline.reset();
		this.animationOpened.reset();
		this.animations.forEach(Animation::reset);
	}

	@Override
	public void set(float animation) {
		this.animation.set(animation);
	}

	@Override
	public void load(DataInputStream dis) throws Exception {
		String s = dis.readUTF();
		int i = 0;
		for (String v : this.values) {
			if (s.equalsIgnoreCase(v)) {
				this.selected = i;
				break;
			}
			i++;
		}
		this.reset();
	}

	@Override
	public void save(DataOutputStream dos) throws Exception {
		dos.writeUTF(this.getPath());
		dos.writeUTF(this.values[this.selected]);
	}

	@Override
	public double boundAnimation() {
		return this.animationOpened.get() * animation.get();
	}
}
