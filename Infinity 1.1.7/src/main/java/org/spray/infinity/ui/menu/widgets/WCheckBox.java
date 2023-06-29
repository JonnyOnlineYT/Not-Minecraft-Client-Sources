package org.spray.infinity.ui.menu.widgets;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.screen.narration.NarrationPart;
import net.minecraft.client.gui.widget.PressableWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

@Environment(EnvType.CLIENT)
public class WCheckBox extends PressableWidget {
	private static final Identifier TEXTURE = new Identifier("textures/gui/checkbox.png");
	private boolean checked;
	private final boolean showMessage;
	private int color;

	public WCheckBox(int x, int y, int width, int height, Text text, boolean checked) {
		this(x, y, width, height, text, checked, true);
	}

	public WCheckBox(int i, int j, int k, int l, Text text, boolean bl, boolean bl2) {
		super(i, j, k, l, text);
		this.checked = bl;
		this.showMessage = bl2;
	}

	public void onPress() {
		this.checked = !this.checked;
	}

	public boolean isChecked() {
		return this.checked;
	}

	public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		MinecraftClient minecraftClient = MinecraftClient.getInstance();
		RenderSystem.setShaderTexture(0, TEXTURE);
		RenderSystem.enableDepthTest();
		TextRenderer textRenderer = minecraftClient.textRenderer;
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.alpha);
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);
		drawTexture(matrices, this.x, this.y, this.isFocused() ? 20.0F : 0.0F, this.checked ? 20.0F : 0.0F, 20,
				this.height, 64, 64);
		this.renderBackground(matrices, minecraftClient, mouseX, mouseY);
		if (this.showMessage) {
			drawTextWithShadow(matrices, textRenderer, this.getMessage(), this.x + 24, this.y + (this.height - 8) / 2,
					14737632 | MathHelper.ceil(this.alpha * 255.0F) << 24);
		}
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setHeight(int height) {
		this.height = height;
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
