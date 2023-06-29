package volcano.summer.screen.tabgui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import net.minecraft.client.Minecraft;
import volcano.summer.base.Summer;
import volcano.summer.base.manager.module.Module;
import volcano.summer.base.manager.module.Module.Category;
import volcano.summer.client.util.R2DUtils;

public class TabGui {
	private static int baseCategoryWidth;
	private static int baseCategoryHeight;
	static int ymod;
	private static int baseModWidth;
	private static int baseModHeight;
	private static Section section = Section.CATEGORY;
	private static int categoryTab = 0;
	private static int modTab = 0;
	private static int categoryPosition = 22;
	private static int categoryTargetPosition = 22;
	private static int modPosition = 22;
	private static int modTargetPosition = 22;
	private static long lastUpdateTime;
	private static boolean transitionQuickly;
	private static float hue = 0.0F;
	private static ArrayList<Category> categoryArrayList;
    private static int currentCategorySelection;
	private static int currentModSelection;
	private static int currentSettingSelection;
	private int screen;
    private static boolean editMode;

	public static void init() {
		categoryArrayList = new ArrayList<Category>();
        currentCategorySelection = 0;
        currentModSelection = 0;
        currentSettingSelection = 0;
        editMode = false;
        categoryArrayList.addAll(Arrays.asList(Category.values()));
    }

	public static void render() {
		ymod = 1;
		int highestWidth = 50;
		baseCategoryWidth = highestWidth;
		baseCategoryHeight = Category.values().length * 14 + 2;
		updateBars();
		R2DUtils.drawBorderedRectZ(1.0D, 20 + ymod + 0.5D, 2 + baseCategoryWidth,
				21 + baseCategoryHeight - 3 + ymod - 11 + 2 - 0.5D, 1.5F, Integer.MIN_VALUE,
				new Color(20, 20, 20, 220).getRGB());
		R2DUtils.drawBorderedGradientRect(1.0D, categoryPosition - 2 + ymod + 0.5D, 2 + baseCategoryWidth,
				categoryPosition + 14 - 2 + ymod - 1 - 0.5D, 1.5F, Integer.MIN_VALUE, 0xFFDDF032,
				new Color(200, 200, 200, 200).getRGB());
		int yPos = 22;
		int yPosBottom = 29;
		for (int i = 0; i < Category.values().length; i++) {
			Category category = Category.values()[i];
			String name = Character.toUpperCase(category.name().toLowerCase().charAt(0))
					+ category.name().toLowerCase().substring(1);
			Minecraft.getMinecraft().fontRendererObj.func_175063_a(name,
					baseCategoryWidth / 2 - Minecraft.getMinecraft().fontRendererObj.getStringWidth(name) / 2 + 1.5F,
					yPos + 3 - 2 + ymod - 0.5F, -1);
			yPos += 12;
			yPosBottom += 14;
		}
		if (section == Section.MODS) {
			R2DUtils.drawBorderedRectZ(baseCategoryWidth + 4, categoryPosition - 1 + ymod - 2 + 1 + 0.5D,
					baseCategoryWidth + 2 + baseModWidth - 13, categoryPosition
							+ getModsInCategory(Category.values()[categoryTab]).size() * 12 + 1 + ymod - 2 - 0.5D,
					1.5F, Integer.MIN_VALUE, new Color(20, 20, 20, 220).getRGB());
			R2DUtils.drawBorderedGradientRect(baseCategoryWidth + 4, modPosition + ymod - 2 + 0.5D,
					baseCategoryWidth + baseModWidth + 1 - 12, modPosition + 14 + ymod - 2 - 1 - 0.5D, 1.5F,
					Integer.MIN_VALUE, 0xFFDDF032, new Color(200, 200, 200, 200).getRGB());
			yPos = categoryPosition;
			yPosBottom = categoryPosition + 14;
			for (int i = 0; i < getModsInCategory(Category.values()[categoryTab]).size(); i++) {
				Module mod = getModsInCategory(Category.values()[categoryTab]).get(i);
				String name = mod.name;
				Minecraft.getMinecraft().fontRendererObj.func_175063_a(name,
						baseCategoryWidth + baseModWidth / 2
								- Minecraft.getMinecraft().fontRendererObj.getStringWidth(name) / 2 - 4,
						yPos + 3 - 2 + ymod - 0.5F, mod.state ? 0x90FFFFFF : 0x908B8C82);
				yPos += 12;
				yPosBottom += 14;
			}
		}
	}

	public static void keyPress(int key) {
		if (section == Section.CATEGORY) {
			switch (key) {
			case 205:
				int highestWidth = 0;
				for (Module module : getModsInCategory(Category.values()[categoryTab])) {
					String name = String.valueOf(String.valueOf(Character.toUpperCase(module.name.charAt(0))))
							+ module.name.substring(1);
					int stringWidth = Minecraft.getMinecraft().fontRendererObj.getStringWidth(name);
					highestWidth = Math.max(stringWidth, highestWidth);
				}
				baseModWidth = highestWidth + 25;
				baseModHeight = getModsInCategory(Category.values()[categoryTab]).size() * 14 + 2;
				modTargetPosition = modPosition = categoryTargetPosition;
				modTab = 0;
				section = Section.MODS;
				break;
			case 28:
				break;
			case 208:
				categoryTab += 1;
				categoryTargetPosition += 12;
				if (categoryTab <= Category.values().length - 1) {
					break;
				}
				transitionQuickly = true;
				categoryTargetPosition = 22;
				categoryTab = 0;
				break;
			case 200:
				categoryTab -= 1;
				categoryTargetPosition -= 12;
				if (categoryTab >= 0) {
					break;
				}
				transitionQuickly = true;
				categoryTargetPosition = 22 + (Category.values().length - 1) * 12;
				categoryTab = Category.values().length - 1;
			}
		} else if (section == Section.MODS) {
			switch (key) {
			case 203:
				section = Section.CATEGORY;
				break;
			case 205:
				Module mod = getModsInCategory(Category.values()[categoryTab]).get(modTab);
				mod.toggleMod();
				Summer.fileManager.saveFiles();
				break;
			case 28:
				Summer.fileManager.saveFiles();
				break;
			case 208:
				modTab += 1;
				modTargetPosition += 12;
				if (modTab > getModsInCategory(Category.values()[categoryTab]).size() - 1) {
					transitionQuickly = true;
					modTargetPosition = categoryTargetPosition;
					modTab = 0;
				}
				break;
			case 200:
				modTab -= 1;
				modTargetPosition -= 12;
				if (modTab < 0) {
					transitionQuickly = true;
					modTargetPosition = categoryTargetPosition
							+ (getModsInCategory(Category.values()[categoryTab]).size() - 1) * 12;
					modTab = getModsInCategory(Category.values()[categoryTab]).size() - 1;
				}
				break;
			}
		}
	}

	private static void updateBars() {
		long timeDifference = System.nanoTime() / 1000000L - lastUpdateTime;
		lastUpdateTime = System.nanoTime() / 1000000L;
		int increment = transitionQuickly ? 100 : 20;
		increment = Math.max(1, Math.round(increment * timeDifference / 100L));
		if (categoryPosition < categoryTargetPosition) {
			categoryPosition += increment;
			if (categoryPosition >= categoryTargetPosition) {
				categoryPosition = categoryTargetPosition;
				transitionQuickly = false;
			}
		} else if (categoryPosition > categoryTargetPosition) {
			categoryPosition -= increment;
			if (categoryPosition <= categoryTargetPosition) {
				categoryPosition = categoryTargetPosition;
				transitionQuickly = false;
			}
		}
		if (modPosition < modTargetPosition) {
			modPosition += increment;
			if (modPosition >= modTargetPosition) {
				modPosition = modTargetPosition;
				transitionQuickly = false;
			}
		} else if (modPosition > modTargetPosition) {
			modPosition -= increment;
			if (modPosition <= modTargetPosition) {
				modPosition = modTargetPosition;
				transitionQuickly = false;
			}
		}
	}

	private static List<Module> getModsInCategory(Category Category) {
		List<Module> modList = new ArrayList<Module>();
		for (Module mod : getSortedModuleArray()) {
			if (mod.category == Category) {
				modList.add(mod);
			}
		}
		return modList;
	}

	private static List<Module> getSortedModuleArray() {
		final List<Module> list = new ArrayList<Module>();
		for (final Module mod : Summer.moduleManager.mods) {
			list.add(mod);
		}
		list.sort(new Comparator<Module>() {
			@Override
			public int compare(final Module m1, final Module m2) {
				final String s1 = m1.name;
				final String s2 = m2.name;
				final int cmp = Minecraft.getMinecraft().fontRendererObj.getStringWidth(s2)
						- Minecraft.getMinecraft().fontRendererObj.getStringWidth(s1);
				return (cmp != 0) ? cmp : s2.compareTo(s1);
			}
		});
		return list;
	}

	private enum Section {
		CATEGORY("CATEGORY", 0), MODS("MODS", 1);
		private Section(String s, int n) {
		}
	}
}