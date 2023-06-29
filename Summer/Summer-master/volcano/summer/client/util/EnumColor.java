package volcano.summer.client.util;

public enum EnumColor {
	Black, DarkBlue, DarkGreen, DarkAqua, DarkRed, DarkPurple, Gold, Gray, DarkGray, Blue, Green, Aqua, Red, Purple, Yellow, White;

	public static EnumColor nextColor(EnumColor color) {
		int curr = 0;
		EnumColor[] arrayOfEnumColor;
		int j = (arrayOfEnumColor = values()).length;
		for (int i = 0; i < j; i++) {
			EnumColor e = arrayOfEnumColor[i];
			if (e == color) {
				break;
			}
			curr++;
		}
		if (color == White) {
			color = Black;
			return color;
		}
		int change = curr + 1;
		if (change > values().length) {
			change = 1;
		}
		color = values()[change];
		return color;
	}
}
