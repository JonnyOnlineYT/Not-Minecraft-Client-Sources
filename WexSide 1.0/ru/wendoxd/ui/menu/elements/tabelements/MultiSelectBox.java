package ru.wendoxd.ui.menu.elements.tabelements;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

import ru.wendoxd.ui.menu.utils.Bound2D;
import ru.wendoxd.ui.menu.utils.MenuAPI;
import ru.wendoxd.utils.fonts.Fonts;
import ru.wendoxd.utils.visual.RenderUtils;
import ru.wendoxd.utils.visual.animation.Animation;

public class MultiSelectBox extends PrimaryButton {
	private HashMap<Integer, Bound2D> bounds = new HashMap<>();
	private List<Animation> animations = new ArrayList<>();
	public List<Boolean> selectedValues = new ArrayList<>();
	private Animation animation, animationOutline, animationOpened;
	private Bound2D selectBound, bound;
	private String[] values;
	private boolean opened;
	private float x, yOffset;

	public MultiSelectBox(String name, String[] values) {
		this(name, values, null);
	}

	public MultiSelectBox(String name, String[] values, Supplier<Boolean> condition) {
		super(name, condition);
		this.animation = new Animation();
		this.animationOutline = new Animation();
		this.animationOpened = new Animation(0.01);
		this.values = values;
		this.bound = new Bound2D(0, 0, 0, 0);
		this.selectBound = new Bound2D(0, 0, 0, 0);
		this.bounds.clear();
		this.animations.clear();
		for (int i = 0; i < values.length; i++) {
			bounds.put(i, new Bound2D(0, 0, 0, 0));
			animations.add(new Animation(0.01));
			this.selectedValues.add(false);
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
					RenderUtils.rgba(170 + (int) (45 * animationOutline.get()),
							170 + (int) (45 * animationOutline.get()), 170 + (int) (45 * animationOutline.get()),
							(int) (255 * animation.get())));
			StringBuilder sb = new StringBuilder();
			int counter = (int) this.selectedValues.stream().filter(w -> w).count();
			if (counter > 0) {
				for (int i = 0; i < this.selectedValues.size(); i++) {
					if (this.selectedValues.get(i)) {
						sb.append(this.values[i]).append(", ");

					}
				}
			} else {
				sb.append("None ,");
			}
			Fonts.MNTSB_13.drawSubstring(sb.substring(0, sb.toString().length() - 2), x + 55f,
					yOffset + 6.7f * (float) animation.get(),
					RenderUtils.rgba(150 + (int) (45 * animationOutline.get()),
							150 + (int) (45 * animationOutline.get()), 150 + (int) (45 * animationOutline.get()),
							(int) (255 * animation.get())),
					33);
			RenderUtils.drawArrow(x + 98, yOffset + (float) (6.5f * animation.get()), opened,
					RenderUtils.rgba(170, 170, 170, (int) (255 * animation.get())), 0.7f);
			this.selectBound.update(x + 53, yOffset + 3.5f, x + 105, (float) (yOffset + 12.5f * animation.get()));
			this.bound.update(x, yOffset, x + 110, (float) (yOffset + 16 * animation.get()));
		}
		return (float) (16 * animation.get());
	}

	public void drawSelectBox() {
		double anim = this.animationOpened.get() * MenuAPI.transparencies.get();
		if (anim > 0 && this.animation.get() > 0) {
			float height = this.values.length * 8f;
			RenderUtils.drawRect(x + 53, yOffset + 13.5f, x + 105,
					(float) (yOffset + (14 + height) * animation.get() * anim), RenderUtils.rgba(27, 27, 27, 255));
			for (int i = 0; i < this.values.length; i++) {
				Fonts.MNTSB_13.drawString(this.values[i], x + 55, yOffset + (float) ((16f + (i * 8)) * anim),
						RenderUtils.rgba(150 + (int) (animations.get(i).get() * 60),
								150 + (int) (animations.get(i).get() * 60), 150 + (int) (animations.get(i).get() * 60),
								(int) (255 * animation.get() * anim)));
				float start = yOffset + (float) (17.5f + (i * 8f) * anim) - 4;
				float end = yOffset + (float) (17.5f + (i * 8f) * anim) + 4.5f;
				bounds.put(i, new Bound2D(x + 53, start, x + 105, end));
			}
		}
	}

	public boolean get(int id) {
		return this.selectedValues.get(id);
	}

	public boolean isOpened() {
		return this.opened;
	}

	public boolean isEnabled() {
		return this.isVisible();
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
		this.bounds
				.forEach((id, bound) -> this.animations.get(id).update(bound.inBound() || this.selectedValues.get(id)));
	}

	@Override
	public boolean mouseClicked(int x, int y, int mb) {
		boolean inBound = this.selectBound.inBound();
		boolean prev = this.opened;
		boolean result = inBound && !opened;
		if (result)
			MenuAPI.contextTab.closeSelectBoxes();
		this.opened = result;
		if (prev) {
			bounds.forEach((i, b) -> {
				if (b.inBound()) {
					this.opened = true;
					this.selectedValues.set(i, !this.selectedValues.get(i));
				}
			});
		}
		return inBound || this.opened;
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
		for (int i = 0; i < this.selectedValues.size(); i++) {
			this.selectedValues.set(i, false);
		}
		int i = 0;
		String[] strs = dis.readUTF().split("\n");
		for (String s : this.values) {
			for (String str : strs) {
				if (str.equalsIgnoreCase(s)) {
					this.selectedValues.set(i, true);
				}
			}
			i++;
		}
		this.reset();
	}

	@Override
	public void save(DataOutputStream dos) throws Exception {
		dos.writeUTF(this.getPath());
		StringBuilder builder = new StringBuilder();
		int i = 0;
		for (String s : this.values) {
			if (this.selectedValues.get(i))
				builder.append(s + "\n");
			i++;
		}
		dos.writeUTF(builder.toString());
	}

	@Override
	public double boundAnimation() {
		return this.animationOutline.get() * animation.get();
	}
}
