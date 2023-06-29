package org.spray.heaven.font;

public class TextProperties {
	public static final TextProperties INSTANCE;

	private TextProperties() {
	}

	static {
		INSTANCE = new TextProperties();
	}

	public enum Style {
		REGULAR("�r", 'r', "/assets/kamiblue/fonts/Source_Sans_Pro/SourceSansPro-SemiBold.ttf", 0),
		BOLD("�l", 'l', "/assets/kamiblue/fonts/Source_Sans_Pro/SourceSansPro-Black.ttf", 1),
		ITALIC("�o", 'o', "/assets/kamiblue/fonts/Source_Sans_Pro/SourceSansPro-SemiBoldItalic.ttf", 2);

		private final String code;
		private final char codeChar;

		private final String fontPath;
		private final int styleConst;

		public final String getCode() {
			return this.code;
		}

		public final char getCodeChar() {
			return this.codeChar;
		}

		public final String getFontPath() {
			return this.fontPath;
		}

		public final int getStyleConst() {
			return this.styleConst;
		}

		private Style(final String code, final char codeChar, final String fontPath, final int styleConst) {
			this.code = code;
			this.codeChar = codeChar;
			this.fontPath = fontPath;
			this.styleConst = styleConst;
		}
	}

	public enum HAlign {
		LEFT(0.0f), CENTER(0.5f), RIGHT(1.0f);

		private final float multiplier;

		public final float getMultiplier() {
			return this.multiplier;
		}

		private HAlign(final float multiplier) {
			this.multiplier = multiplier;
		}
	}

	public enum VAlign {
		TOP(0.0f), CENTER(0.5f), BOTTOM(1.0f);

		private final float multiplier;

		public final float getMultiplier() {
			return this.multiplier;
		}

		private VAlign(final float multiplier) {
			this.multiplier = multiplier;
		}
	}

}
