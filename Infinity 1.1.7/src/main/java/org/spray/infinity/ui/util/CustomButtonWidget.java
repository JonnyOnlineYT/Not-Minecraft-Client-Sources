package org.spray.infinity.ui.util;

import org.spray.infinity.font.IFont;
import org.spray.infinity.utils.render.Drawable;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.screen.narration.NarrationPart;
import net.minecraft.client.gui.widget.PressableWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

@Environment(EnvType.CLIENT)
public class CustomButtonWidget extends PressableWidget {
	public static final CustomButtonWidget.TooltipSupplier EMPTY = (button, matrices, mouseX, mouseY) -> {
	};
	protected final CustomButtonWidget.PressAction onPress;
	protected final CustomButtonWidget.TooltipSupplier tooltipSupplier;

	private int color;
	private int hcolor;

	public CustomButtonWidget(int x, int y, int width, int height, Text message,
			CustomButtonWidget.PressAction onPress) {
		this(x, y, width, height, message, onPress, EMPTY);
	}

	public CustomButtonWidget(int x, int y, int width, int height, Text message, CustomButtonWidget.PressAction onPress,
			CustomButtonWidget.TooltipSupplier tooltipSupplier) {
		super(x, y, width, height, message);
		this.onPress = onPress;
		this.tooltipSupplier = tooltipSupplier;
		this.hcolor = 0xFF85C0BA;
		this.color = 0xFF95D3CD;
	}

	public void onPress() {
		this.onPress.onPress(this);
	}

	public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		int c1 = this.active && this.isHovered() ? hcolor
				: this.active ? color : !this.active && this.isHovered() ? 0xFF6F737B : 0xFF50545D;
		Drawable.drawHRoundedRect(matrices, x, y, width, height, c1);
		IFont.legacy15.drawCenteredString(matrices, this.getMessage().getString(), this.x + this.width / 2,
				this.y + (this.height - 8) / 2 - 1, -1);

		if (this.isHovered()) {
			this.renderToolTip(matrices, mouseX, mouseY);
		}
	}

	public void renderToolTip(MatrixStack matrices, int mouseX, int mouseY) {
		this.tooltipSupplier.onTooltip(this, matrices, mouseX, mouseY);
	}

	@Environment(EnvType.CLIENT)
	public interface TooltipSupplier {
		void onTooltip(CustomButtonWidget button, MatrixStack matrices, int mouseX, int mouseY);
	}

	@Environment(EnvType.CLIENT)
	public interface PressAction {
		void onPress(CustomButtonWidget button);
	}

	@Override
	public void appendNarrations(NarrationMessageBuilder builder) {
		builder.put(NarrationPart.TITLE, (Text) this.getNarrationMessage());
		if (this.active) {
			if (this.isFocused()) {
				builder.put(NarrationPart.USAGE, (Text) (new TranslatableText("narration.checkbox.usage.focused")));
			} else {
				builder.put(NarrationPart.USAGE, (Text) (new TranslatableText("narration.checkbox.usage.hovered")));
			}
		}
	}
}
