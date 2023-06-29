package org.spray.heaven.font;

public class IFont {

	private static String WEB_PATH = "/assets/minecraft/heaven/fonts/test/glyph_sizes.bin";
	private static String COMFORTAA_LIGHT_PATH = "/assets/minecraft/heaven/fonts/Comfortaa-Regular.ttf";
	private static String POPPIN_PATH = "/assets/minecraft/heaven/fonts/Poppin.ttf";

	private static String rubik = "/assets/minecraft/heaven/fonts/rubik.ttf";

	public static FontRenderer DEFAULT_SMALL;

	public static FontRenderer WEB_SMALL;
	public static FontRenderer WEB_LIST;
	public static FontRenderer WEB_SETTINGS;
	public static FontRenderer WEB_SETTINGS_MINI;
	public static FontRenderer WEB_MIDDLE;

	public static FontRenderer WEB_SEMI_SMALL;
	public static FontRenderer WEB_BOLD_SMALL;

	public static FontRenderer ROBOTO_SMALL;

	public static FontRenderer COMFORTAA_LIST;
	public static FontRenderer POPPIN_LIST;
	public static FontRenderer rubik1, rubik2;

	public static void initFonts() {
		rubik1 = new FontRenderer(rubik, 0.36f, 0, true);
		rubik2 = new FontRenderer(rubik, 0.3f, 0, true);
		WEB_SEMI_SMALL = new FontRenderer(WEB_PATH, 0.26f, 0f, true);
		WEB_BOLD_SMALL = new FontRenderer(WEB_PATH, 0.26f, 0f, true);
		WEB_SMALL = new FontRenderer(WEB_PATH, 0.25f, 0f, true);
		WEB_SETTINGS_MINI = new FontRenderer(WEB_PATH, 0.18f, 0f, true);
		WEB_SETTINGS = new FontRenderer(WEB_PATH, 0.20f, 0f, true);
		WEB_MIDDLE = new FontRenderer(WEB_PATH, 0.45f, 0f, true);

		WEB_LIST = new FontRenderer(WEB_PATH, 0.22f, 0f, true);
		COMFORTAA_LIST = new FontRenderer(COMFORTAA_LIGHT_PATH, 0.22f, 0f, true);
		POPPIN_LIST = new FontRenderer(POPPIN_PATH, 0.22f, 0f, true);

		DEFAULT_SMALL = new FontRenderer("Default", 0.23f, 0f, false);

		ROBOTO_SMALL = new FontRenderer(WEB_PATH, 0.26f, 0f, true);
	}

}
