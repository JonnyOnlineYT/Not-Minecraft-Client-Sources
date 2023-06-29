package volcano.summer.screen.clickgui.exeter;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import volcano.summer.base.Summer;
import volcano.summer.base.manager.module.Module;
import volcano.summer.client.util.CustomFont;
import volcano.summer.client.value.ClampedValue;
import volcano.summer.client.value.ModeValue;
import volcano.summer.client.value.Value;

public class ModuleButton extends Button {
	private final Module module;
	private List<Item> items;
	private boolean subOpen;

	public ModuleButton(final Module module) {
		super(module.getName());
		this.items = new ArrayList<Item>();
		this.module = module;
		for (final Value property : Summer.valueManager.getModValues(module)) {
			if (property.getValue() instanceof Boolean) {
				this.items.add(new BooleanButton(property));
			}
			if (property instanceof ModeValue) {
				this.items.add(new EnumButton((ModeValue) property));
			}
			if (property.getValue() instanceof ClampedValue) {
				this.items.add(new NumberSlider((ClampedValue) property));
			}
		}
	}

	@Override
	public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
		if (!this.items.isEmpty()) {
			ClickGui.getClickGui().guiFont.drawString("...", this.x - 1.0f + this.width - 8.0f, this.y - 2.0f,
					CustomFont.FontType.SHADOW_THIN, -1);
			if (this.subOpen) {
				float height = 1.0f;
				for (final Item item : this.items) {
					height += 15.0f;
					item.setLocation(this.x + 1.0f, this.y + height);
					item.setHeight(15);
					item.setWidth(this.width - 9);
					item.drawScreen(mouseX, mouseY, partialTicks);
				}
			}
		}
	}

	@Override
	public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		if (!this.items.isEmpty()) {
			if (mouseButton == 1 && this.isHovering(mouseX, mouseY)) {
				this.subOpen = !this.subOpen;
				Minecraft.getMinecraft().getSoundHandler().playSound(
						PositionedSoundRecord.createPositionedSoundRecord(new ResourceLocation("random.click"), 1.0f));
			}
			if (this.subOpen) {
				for (final Item item : this.items) {
					item.mouseClicked(mouseX, mouseY, mouseButton);
				}
			}
		}
	}

	@Override
	public int getHeight() {
		if (this.subOpen) {
			int height = 14;
			for (final Item item : this.items) {
				height += item.getHeight() + 1;
			}
			return height + 2;
		}
		return 14;
	}

	@Override
	public void toggle() {
		if (this.module instanceof Module) {
			this.module.toggleMod();
		}
	}

	@Override
	public boolean getState() {
		return !(this.module instanceof Module) || this.module.getState();
	}
}
