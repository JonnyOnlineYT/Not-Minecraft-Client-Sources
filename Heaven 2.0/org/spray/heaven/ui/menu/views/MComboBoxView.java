package org.spray.heaven.ui.menu.views;

import java.util.List;

import org.spray.heaven.font.IFont;
import org.spray.heaven.ui.avx.view.View;
import org.spray.heaven.util.render.ColorUtil;
import org.spray.heaven.util.render.Drawable;
import org.spray.heaven.util.render.animation.Animation;
import org.spray.heaven.util.render.animation.Direction;
import org.spray.heaven.util.render.animation.impl.DecelerateAnimation;
import org.spray.heaven.util.render.shader.drawable.RoundedShader;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class MComboBoxView extends View {

	private final Animation animation = new DecelerateAnimation(250, 1);
	private final List<String> items;
	private int current;

	private OnSelectListener listener;

	private boolean opened;

	private int counter;

	public MComboBoxView(List<String> items) {
		this.items = items;
	}

	@Override
	protected void renderView(int mouseX, int mouseY, float delta) {
		animation.setDirection(opened ? Direction.FORWARDS : Direction.BACKWARDS);
		int multiply = opened ? items.size() + 1 : 1;
		float ch = IFont.WEB_SETTINGS.getFontHeight() + 4;

		GlStateManager.pushMatrix();

		float cy = ((y + (height / 2)) + ch / 2);
		GlStateManager.translate(x + width / 2, cy, 0);
		GlStateManager.scale(1f + (0.03f * animation.getOutput()), 1f + (0.03f * animation.getOutput()), 1);
		GlStateManager.translate(-(x + width / 2), -cy, 0);

		if (!animation.finished(Direction.BACKWARDS)) {
			int shadowMargin = 1;
			Drawable.drawBlurredShadow((int) x + shadowMargin, (int) (y + (height - ch) / 2) + shadowMargin,
					(int) width - shadowMargin, (int) (ch * multiply) - shadowMargin, 10, ColorUtil
							.applyOpacity(context.getTheme().getShadowColor(), (float) (0.4f * animation.getOutput())));
		}

		RoundedShader.drawRound(x, y + (height - ch) / 2, width, ch * multiply,
				(float) (2 + (2 * animation.getOutput())), context.getTheme().getInBackground());

		GlStateManager.popMatrix();
		
		float tx = (float) (x + width - 7);
		float ty = (float) (y + (height / 2));
		
		float thetaRotation = (float) (-180f * (1f - animation.getOutput()));
		GlStateManager.pushMatrix();

		GlStateManager.translate(tx, ty, 0);
		GlStateManager.rotate(thetaRotation, 0, 0, 1);
		GlStateManager.translate(-tx, -ty, 0);
		
		Drawable.drawTexture(new ResourceLocation("heaven/textures/icons/clickui/up-arrow.png"), x + width - 10,
				y + (height - 6) / 2, 6, 6, context.getTheme().getTextColor());
		GlStateManager.popMatrix();

		IFont.WEB_SETTINGS.drawVCenteredString(items.get(current), x + 2, y, height,
				context.getTheme().getTextColor().getRGB());
		if (opened) {
			int offset = 1;
			for (String item : items) {
				IFont.WEB_SETTINGS.drawVCenteredString(item, x + 2, y + offset * ch, height,
						items.get(current) == item ? context.getTheme().getColorPrimary().getRGB()
								: context.getTheme().getTextColor().getRGB());
				offset++;
			}
		}
	}

	@Override
	public boolean mouseClicked(int mouseX, int mouseY, int button) {
		if (hidden)
			return false;

		if (isHovered(mouseX, mouseY)) {
			if (button == 1) {
				opened = !opened;
				return true;
			} else if (button == 0 && !opened) {
				if (counter + 1 > items.size() - 1)
					counter = 0;
				else
					counter++;

				if (listener != null)
					listener.onSelect(counter);

				setCurrent(counter);
				return true;
			}
		}

		if (opened) {
			float ch = IFont.WEB_SETTINGS.getFontHeight() + 4;
			int offset = 1;
			for (String item : items) {
				if (Drawable.isHovered(mouseX, mouseY, x, y + ch / 2 + (offset * ch), width, ch)) {
					if (button == 0) {
						if (listener != null)
							listener.onSelect(items.indexOf(item));
						setCurrent(items.indexOf(item));
					}
					return true;
				}

				offset++;
			}
		}
		return false;
	}

	public void setOnSelectListener(OnSelectListener listener) {
		this.listener = listener;
	}

	public void setCurrent(int index) {
		this.current = index;

		counter = index;
	}

	public interface OnSelectListener {
		void onSelect(int pos);
	}

}
