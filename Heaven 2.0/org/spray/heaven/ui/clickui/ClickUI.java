package org.spray.heaven.ui.clickui;

import java.awt.Color;
import java.io.IOException;
import java.util.List;

import net.minecraft.util.math.MathHelper;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.spray.heaven.features.module.Category;
import org.spray.heaven.features.module.modules.render.ClickUIMod;
import org.spray.heaven.main.Wrapper;
import org.spray.heaven.protect.events.ClientInitializeEvent;
import org.spray.heaven.ui.clickui.base.AbstractWindow;
import org.spray.heaven.ui.clickui.window.ModuleWindow;
import org.spray.heaven.ui.draggable.DragManager;
import org.spray.heaven.util.render.ColorUtil;
import org.spray.heaven.util.render.Drawable;
import org.spray.heaven.util.render.RenderUtil;
import org.spray.heaven.util.render.animation.Animation;
import org.spray.heaven.util.render.animation.Direction;
import org.spray.heaven.util.render.animation.impl.DecelerateAnimation;
import org.spray.heaven.util.render.animation.impl.EaseBackIn;

import com.google.common.collect.Lists;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;

/**
 * Created by sprayD on 06/09/2021. edited 15.05.2022
 */
public class ClickUI extends GuiScreen {

	private Animation openAnimation, bgAnimation, rAnimation;
	private final List<AbstractWindow> windows;

	private double scrollSpeed;
	private boolean firstOpen;
	private double dWheel;
	private double mamer;

	public ClickUI(ClientInitializeEvent event) {
		windows = Lists.newArrayList();
		firstOpen = true;
		event.check();
	}

	@Override
	public void initGui() {
		openAnimation = new EaseBackIn(270, .4f, 1.13f);
		rAnimation = new DecelerateAnimation(300, 1f);
		bgAnimation = new DecelerateAnimation(300, 1f);
		if (firstOpen) {
			double x = 20, y = 20;
			double offset = 0;
			int windowHeight = 18;
			ScaledResolution sr = new ScaledResolution(Wrapper.MC);
			int i = 0;
			for (Category category : Category.values()) {
				ModuleWindow window = new ModuleWindow(category.getName(), Wrapper.getModule().getByCategory(category),
						i, x + offset, y, 108, windowHeight);
				window.setOpen(true);
				windows.add(window);
				offset += 110;

				if (offset > sr.getScaledWidth()) {
					offset = 0;
				}
				i++;
			}
			firstOpen = false;
		}

		windows.forEach(AbstractWindow::init);

		super.initGui();
	}


	@Override
	public void drawScreen(int mouseX, int mouseY, float delta) {
		ClickUIMod clickUIMod = Wrapper.getModule().get("ClickGUI");
		int index = 0;

		if (clickUIMod.background.isToggle()) {
			Color bgColor = clickUIMod.coloredBackground.isToggle()
					? ColorUtil.applyOpacity(ColorUtil.darker(clickUIMod.getColor(1), 0.3f),
							(float) (clickUIMod.backgroundAlpha.getValue() * bgAnimation.getOutput()))
					: ColorUtil.applyOpacity(new Color(10, 10, 10, 255),
							(float) (clickUIMod.backgroundAlpha.getValue() * bgAnimation.getOutput()));
//			Drawable.drawRect(0, 0, width, height, new Color(0, 0, 0, 140).getRGB());
			Drawable.verticalGradient(0, 0, width, height,
					ColorUtil.applyOpacity(new Color(34, 34, 34, 155),
							(float) (clickUIMod.backgroundAlpha.getValue() * bgAnimation.getOutput())).getRGB(),
					new Color(0, 0, 0, 0).getRGB());
			Drawable.verticalGradient(0, 0, width, height, new Color(0, 0, 0, 0).getRGB(), bgColor.getRGB());
		}

		if (openAnimation.isDone() && openAnimation.getDirection().equals(Direction.BACKWARDS)) {
			windows.forEach(AbstractWindow::onClose);
			Wrapper.getConfig().saveDefault();
			DragManager.saveDragData();
			mc.currentScreen = null;
			mc.displayGuiScreen(null);
		}

		dWheel = Mouse.getDWheel();


		if (dWheel > 0)
			scrollSpeed += 14;
		else if (dWheel < 0)
			scrollSpeed -= 14;

		double anim = (openAnimation.getOutput() + .6f);

		GlStateManager.pushMatrix();

		double centerX = width >> 1;
		double centerY = height >> 1;

		GlStateManager.translate(centerX, centerY, 0);
//		GlStateManager.rotate((float) (-180 * (1 - rAnimation.getOutput())), 0, 0, 1);
		GlStateManager.scale(anim, anim, 1);
		GlStateManager.translate(-centerX, -centerY, 0);

		for (AbstractWindow window : windows) {
			if (Keyboard.isKeyDown(Keyboard.KEY_DOWN))
				window.setY(window.getY() + 2);
			else if (Keyboard.isKeyDown(Keyboard.KEY_UP))
				window.setY(window.getY() - 2);
			else if (Keyboard.isKeyDown(Keyboard.KEY_LEFT))
				window.setX(window.getX() - 2);
			else if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT))
				window.setX(window.getX() + 2);
			if (dWheel != 0)
				window.setY(window.getY() + scrollSpeed);
			else
				scrollSpeed = 0;

			window.render(mouseX, mouseY, delta, ClickUIMod.getColor((index++) * 20),
					openAnimation.isDone() && openAnimation.getDirection() == Direction.FORWARDS);
		}

		GlStateManager.popMatrix();

		super.drawScreen(mouseX, mouseY, delta);
	}

	@Override
	public void onGuiClosed() {

	}

	@Override
	public void updateScreen() {
		windows.forEach(AbstractWindow::tick);
		super.updateScreen();
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int button) throws IOException {
		windows.forEach(w -> {
			w.mouseClicked(mouseX, mouseY, button);

			windows.forEach(w1 -> {
				if (w.dragging && w != w1)
					w1.dragging = false;
			});
		});
		super.mouseClicked(mouseX, mouseY, button);
	}

	@Override
	public void mouseReleased(int mouseX, int mouseY, int button) {
		windows.forEach(w -> w.mouseReleased(mouseX, mouseY, button));
		super.mouseReleased(mouseX, mouseY, button);
	}

	@Override
	public void handleMouseInput() throws IOException {
		windows.forEach(w -> {
			try {
				w.handleMouseInput();
			} catch (IOException ignored) {

			}
		});
		super.handleMouseInput();
	}

	@Override
	public void keyTyped(char chr, int keyCode) throws IOException {
		windows.forEach(w -> {
			w.keyTyped(chr, keyCode);
		});

		if (keyCode == 1 || keyCode == Wrapper.getModule().get("ClickGUI").getKey()) {
			bgAnimation.setDirection(Direction.BACKWARDS);
			rAnimation.setDirection(Direction.BACKWARDS);
			openAnimation.setDirection(Direction.BACKWARDS);
		}
	}

}
