package ru.wendoxd.ui.altmanager.altManager;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import ru.wendoxd.WexSide;
import ru.wendoxd.ui.menu.utils.Bound2D;
import ru.wendoxd.utils.fonts.Fonts;
import ru.wendoxd.utils.lib.RectHelper;
import ru.wendoxd.utils.visual.RenderUtils;
import ru.wendoxd.utils.visual.animation.DynamicAnimation;
import ru.wendoxd.utils.visual.shaders.ShaderShell;

import java.util.ConcurrentModificationException;
import java.util.function.Consumer;

public class AltsList {

	private final Bound2D scrollBound, loginBound;
	private float x, y, width, scrollAnim = 0, scrollY = 0, minY = -100;
	private long lastClicked;
	private boolean clicked = false;
	private Consumer<Alt> selected, doubleClicked;
	private Alt selectedAlt;
	private DynamicAnimation dynamicAnimation = new DynamicAnimation();

	public AltsList(Consumer<Alt> selected, Consumer<Alt> doubleClicked) {
		this.selected = selected;
		this.doubleClicked = doubleClicked;
		this.scrollBound = new Bound2D(0, 0, 0, 0, true);
		this.loginBound = new Bound2D(0, 0, 0, 0, true);
	}

	public void draw(float x, float y, float width, float height) {
		this.x = x;
		this.y = y;
		this.width = width;

		RectHelper.renderShadow(x, y, width, height, RenderUtils.rgba(19, 19, 19, 140), 5);

		scrollBound.update(x, y, x + width, y + height);

		if (scrollBound.inBound()) {
			minY = height - 4;
		}

		dynamicAnimation.setValue(scrollY);
		dynamicAnimation.update();
		scrollAnim = (float) dynamicAnimation.getValue();

		GL11.glEnable(GL11.GL_SCISSOR_TEST);
		RenderUtils.scissorRect(x + 2, y + 4, x + width - 4, y + height - 4);
		float startY = y + 4 + scrollAnim;
		float buttonHeight = 30;
		float totalY = 0;

		try {
			for (Alt alt : WexSide.altManager.getAccounts()) {
				if (alt == null)
					return;

				if (y < startY + buttonHeight + 5 && startY < y + height) {
					boolean selected = selectedAlt == alt;
					if (selected) {
						if (clicked) {
							RectHelper.renderShadow(x + 4, startY, width - 6, buttonHeight,
									RenderUtils.rgba(25, 25, 25, 220), 3);
						} else {
							RectHelper.renderShadow(x + 4, startY, width - 6, buttonHeight,
									RenderUtils.rgba(22, 22, 22, 220), 3);
						}
					} else {
						RectHelper.renderShadow(x + 4, startY, width - 6, buttonHeight,
								RenderUtils.rgba(20, 20, 20, 220), 3);
					}

					drawFace(alt.getName(), x + 8, startY + 4);
					ShaderShell.CIRCLE_TEXTURE_SHADER.detach();
					Fonts.MNTSB_16.drawSubstring(alt.getNameOrEmail(), 1,x + 34, startY + 6, RenderUtils.rgba(200, 200, 200, 255), 300);
					Fonts.MNTSB_12.drawString(
							alt.isMojangAccount() ? ChatFormatting.GREEN + "Premium" : ChatFormatting.RED + "Cracked",
							x + 34, startY + 15, -1);
					RenderUtils.drawRoundedRect(x + 4, startY, width - 8, buttonHeight, RenderUtils.rgba(0, 0, 0, 200),
							6);
				}
				startY += buttonHeight + 5;
				totalY += buttonHeight + 5;
			}
		} catch (ConcurrentModificationException ignored) {

		}
		GL11.glDisable(GL11.GL_SCISSOR_TEST);

		if (scrollBound.inBound()) {
			minY -= totalY;
		}

		if (totalY > height - 8) {
			float maxHeight = Math.max((height / totalY) * height, 20);
			float position = MathHelper.clamp(-scrollY / -minY, 0, 1) * (height - maxHeight);

			RenderUtils.drawRoundedRect(x + width + 4, y, 4, height, RenderUtils.rgba(46, 51, 56, 255), 2);
			RenderUtils.drawRoundedRect(x + width + 4, y + position, 4, maxHeight, RenderUtils.rgba(32, 34, 37, 255),
					2);
		}
	}

	public void mouseInput() {
		if (scrollBound.inBound()) {
			scrollY += Mouse.getEventDWheel() / 5F;

			if (scrollY <= minY) {
				scrollY = minY + 2;
			}
			if (scrollY >= 0) {
				scrollY = 0;
			}
		}
	}

	public void click(int mouseX, int mouseY) {
		float startY = y + 4 + scrollAnim;
		float buttonHeight = 30;
		for (Alt alt : WexSide.altManager.getAccounts()) {
			if (alt == null)
				return;

			loginBound.update(x + 4, startY, x + 1 + width - 8, startY + buttonHeight);

			if (loginBound.inBound()) {

				selected.accept(alt);

				clicked = true;

				if (alt == selectedAlt && System.currentTimeMillis() - lastClicked < 250L) {
					doubleClicked.accept(alt);
				}

				selectedAlt = alt;
				lastClicked = System.currentTimeMillis();
				break;
			} else {
				GuiAltManager.selectedAlt = null;
				clicked = false;
			}
			startY += buttonHeight + 5;
		}
	}

	private void drawFace(String name, float x, float y) {
		try {
			AbstractClientPlayer.getDownloadImageSkin(AbstractClientPlayer.getLocationSkin(name), name)
					.loadTexture(Minecraft.getMinecraft().getResourceManager());

			Minecraft.getMinecraft().getTextureManager().bindTexture(AbstractClientPlayer.getLocationSkin(name));
			GlStateManager.color(1, 1, 1, 1);
			float pixel = 1F / 8;
			GL11.glPushMatrix();
			GL11.glBegin(GL11.GL_QUADS);

			GL11.glTexCoord2f(pixel, pixel);
			GL11.glVertex2d(x, y);

			GL11.glTexCoord2f(pixel, pixel * 2);
			GL11.glVertex2d(x, y + 22);

			GL11.glTexCoord2f(pixel * 2, pixel * 2);
			GL11.glVertex2d(x + 22, y + 22);

			GL11.glTexCoord2f(pixel * 2, pixel);
			GL11.glVertex2d(x + 22, y);

			GL11.glEnd();
			GL11.glPopMatrix();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
