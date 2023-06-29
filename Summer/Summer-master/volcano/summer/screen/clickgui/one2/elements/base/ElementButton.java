package volcano.summer.screen.clickgui.one2.elements.base;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import volcano.summer.base.Summer;
import volcano.summer.base.manager.module.Module;
import volcano.summer.client.util.GuiUtils;
import volcano.summer.client.value.ClampedValue;
import volcano.summer.client.value.ModeValue;
import volcano.summer.client.value.Value;
import volcano.summer.screen.clickgui.one2.elements.Element;
import volcano.summer.screen.clickgui.one2.elements.Panel;
import volcano.summer.screen.clickgui.one2.elements.theme.MiniElement;
import volcano.summer.screen.clickgui.one2.elements.theme.MiniMode;
import volcano.summer.screen.clickgui.one2.elements.theme.MiniSlider;
import volcano.summer.screen.clickgui.one2.elements.theme.MiniToggle;

public class ElementButton extends Element {
	public Module mod;
	private boolean expanded;
	private int openHeight;
	private List<MiniElement> minis;

	public ElementButton(final Module mod, final int posX, final int posY, final Panel parent) {
		super(posX, posY, 96, 20, parent);
		this.minis = new ArrayList<MiniElement>();
		this.mod = mod;
		mod.getName().equals("Search");
		this.initMiniElements();
	}

	private void initMiniElements() {
		if (Summer.valueManager.getModValues(mod).size() > 0) {
			final int x = 2;
			int y = 22;
			for (final Value property : Summer.valueManager.getModValues(mod)) {
				if (property instanceof ClampedValue) {
					final MiniSlider slider = new MiniSlider(this.getPosX() + x, this.getPosY() + y,
							(ClampedValue) property, this);
					this.minis.add(slider);
					y += slider.getHeight() + 2;
				} else {
					if (property instanceof ModeValue) {
						final MiniMode mode = new MiniMode(this.getPosX() + x, this.getPosY() + y, (ModeValue) property,
								this);
						this.minis.add(mode);
						y += mode.getHeight() + 2;
					} else {
						if (!(property.getValue() instanceof Boolean)) {
							continue;
						}
						final MiniToggle toggle = new MiniToggle(this.getPosX() + x, this.getPosY() + y, property,
								this);
						this.minis.add(toggle);
						y += toggle.getHeight() + 2;
					}
				}
			}
			this.setOpenHeight(y);
		}
	}

	public void updateMiniElements() {
		if (this.minis.size() > 0) {
			int y = 22;
			for (final MiniElement mini : this.minis) {
				mini.setPosY(this.getPosY() + y);
				y += mini.getHeight() + 2;
			}
			this.setOpenHeight(y);
		}
	}

	public List<Value> sortMiniElements(final List<Value> values) {
		final List<Value> sorted = new ArrayList<Value>();
		for (final Value v : values) {
			if (v instanceof Value) {
				sorted.add(v);
			}
		}
		for (final Value v : values) {
			if (v instanceof ClampedValue) {
				sorted.add(v);
			}
		}
		return sorted;
	}

	@Override
	public void draw(final int mouseX, final int mouseY) {
		final boolean isHover = mouseX >= this.getPosX() + this.dragX && mouseY >= this.getPosY() + this.dragY
				&& mouseX <= this.getPosX() + this.getWidth() + this.dragX
				&& mouseY <= this.getPosY() + this.getHeight() + this.dragY;
		GuiUtils.drawFineBorderedRect(this.getPosX() + this.dragX, this.getPosY() + this.dragY,
				this.getPosX() + this.getWidth() + this.dragX,
				this.getPosY() + (this.isExpanded() ? this.getOpenHeight() : this.getHeight()) + this.dragY, -12303292,
				-13882324);
		GuiUtils.drawFineBorderedRect(this.getPosX() + this.dragX, this.getPosY() + this.dragY,
				this.getPosX() + this.getWidth() + this.dragX, this.getPosY() + this.getHeight() + this.dragY,
				isHover ? -1316805 : -12303292, this.mod.getState() ? -1316805 : -13882324);
		GuiUtils.drawCentredStringWithShadow(this.mod.getName(), this.getPosX() + this.getWidth() / 2 + this.dragX,
				this.getPosY() + 7 + this.dragY, this.mod.getState() ? -1 : -7829368);
		if (this.minis.size() > 0) {
			Minecraft.getMinecraft().fontRendererObj.func_175063_a(this.isExpanded() ? "-" : "+",
					this.getPosX() + this.getWidth() - 8 + this.dragX,
					(float) (this.getPosY() + (this.isExpanded() ? 7.5 : 7.5) + this.dragY),
					this.mod.getState() ? -1 : -7829368);
			if (this.isExpanded()) {
				for (final MiniElement mini : this.minis) {
					mini.draw(mouseX, mouseY);
				}
			}
		}
	}

	@Override
	public void mouseClicked(final int mouseX, final int mouseY, final int button) {
		if (this.getParent().isExpanded()) {
			final boolean isHover = mouseX >= this.getPosX() + this.dragX && mouseY >= this.getPosY() + this.dragY
					&& mouseX <= this.getPosX() + this.getWidth() + this.dragX
					&& mouseY <= this.getPosY() + this.getHeight() + this.dragY;
			if (isHover) {
				if (button == 0) {
					this.mod.toggleMod();
					Minecraft.getMinecraft().thePlayer.playSound("random.click", 1.0f, 1.0f);
				} else if (button == 1 && this.minis.size() > 0) {
					this.setExpanded(!this.isExpanded());
					this.getParent().updatePanelsElements();
					this.updateMiniElements();
				}
			}
		}
		for (final MiniElement mini : this.minis) {
			mini.mouseClicked(mouseX, mouseY, button);
		}
	}

	public int getOpenHeight() {
		return this.openHeight;
	}

	public void setOpenHeight(final int openHeight) {
		this.openHeight = openHeight;
	}

	public boolean isExpanded() {
		return this.expanded;
	}

	public void setExpanded(final boolean expanded) {
		this.expanded = expanded;
	}

	@Override
	public void shiftX(final int ammount) {
		this.dragX = ammount;
		for (final MiniElement mini : this.minis) {
			mini.shiftX(ammount);
		}
	}

	@Override
	public void shiftY(final int ammount) {
		this.dragY = ammount;
		for (final MiniElement mini : this.minis) {
			mini.shiftY(ammount);
		}
	}

	@Override
	public void stopDragging() {
		this.setPosX(this.getPosX() + this.dragX);
		this.setPosY(this.getPosY() + this.dragY);
		this.dragX = 0;
		this.dragY = 0;
		for (final MiniElement mini : this.minis) {
			mini.stopDragging();
		}
	}
}