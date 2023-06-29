package ru.wendoxd;

import net.minecraft.client.Minecraft;

import org.lwjgl.input.Keyboard;

import ru.wendoxd.managers.CommandManager;
import ru.wendoxd.managers.MacroManager;
import ru.wendoxd.models.overrides.OverrideModelCallback;
import ru.wendoxd.managers.FriendManager;
import ru.wendoxd.modules.Module;
import ru.wendoxd.profile.Profile;
import ru.wendoxd.ui.altmanager.AltManager;
import ru.wendoxd.ui.menu.MenuShell;
import ru.wendoxd.ui.menu.impl.windows.ColorWindow;
import ru.wendoxd.ui.menu.utils.MenuAPI;
import ru.wendoxd.utils.fonts.Fonts;
import ru.wendoxd.utils.visual.RenderUtils;
import ru.wendoxd.utils.visual.animation.AnimationThread;
import ru.wendoxd.utils.visual.shaders.ShaderShell;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WexSide {
	public static final String VERSION_TYPE = "VERSION-1.0";
	private static final List<Module> modules = new ArrayList<>();
	public static double animationSpeed = 0.15;
	private static Profile profile;
	public static AltManager altManager = new AltManager();
	public static FriendManager friendManager;
	public static CommandManager commandManager;
	public static MacroManager macroManager;
	public static int x, y;

	public static void initialization() {
		new AnimationThread().start();
		commandManager = new CommandManager();
		try {
			friendManager = new FriendManager();
			friendManager.init();
			macroManager = new MacroManager();
			macroManager.init();
			OverrideModelCallback.init();
		} catch (Exception e) {
			e.printStackTrace();
		}
		profile = new Profile("!dedinside", "1", "01.01.2049");
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			URL url = new URL(Profile.linkToPhoto("fucking.editor"));
			InputStream buffer = url.openConnection().getInputStream();
			int i;
			while ((i = buffer.read()) >= 0) {
				baos.write(i);
			}
			WexSide.getProfile().avatar(baos.toByteArray());
		} catch (Exception e) {
			e.printStackTrace();
		}
		profile.init();
		ColorWindow.callInitXD();
		ShaderShell.init();
		try {
			altManager.init();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void init() {

	}

	public static void drawBackground(double maxX, double maxY) {
		RenderUtils.drawRect(0, 0, maxX, maxY, RenderUtils.rgba(20, 20, 20, 255));
		Fonts.MNTSB_45.drawCenteredStringWithShadow("WEXSIDE", (float) (maxX / 2), (float) (maxY / 2) - 60,
				RenderUtils.rgba(200, 200, 200, 255));
	}

	public static void createProfile(String name, String uid, String licenseDate) {
		WexSide.profile = new Profile(name, uid, licenseDate);
	}

	public static void keyTyped(int key) {
		Minecraft mc = Minecraft.getMinecraft();
		if (mc.player != null && mc.currentScreen == null) {
			if (key == Keyboard.KEY_INSERT || key == Keyboard.KEY_RSHIFT) {
				mc.gameSettings.ofFastRender = false;
				mc.displayGuiScreen(new MenuShell());
				return;
			}
		}
		MenuAPI.tabs.forEach(tab -> tab.keyPress(key));
		if (macroManager != null) {
			macroManager.onKeyPressed(key);
		}
	}

	public static void registerModule(Module... modules) {
		WexSide.modules.addAll(Arrays.asList(modules));
	}

	public static List<Module> getModules() {
		return WexSide.modules;
	}

	public static Profile getProfile() {
		return WexSide.profile;
	}

	public static double createAnimation(double value) {
		return Math.sqrt(1 - Math.pow(value - 1, 2));
	}

	public static double dropAnimation(double value) {
		double c1 = 1.70158;
		double c3 = c1 + 1;
		return 1 + c3 * Math.pow(value - 1, 3) + c1 * Math.pow(value - 1, 2);
	}
}
