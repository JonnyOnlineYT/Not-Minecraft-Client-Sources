package org.spray.infinity.utils;

public class MathAssist {

	public static int clamp(int num, int min, int max) {
		return (num < min) ? min : ((num > max) ? max : num);
	}

	public static float clamp(float num, float min, float max) {
		return (num < min) ? min : ((num > max) ? max : num);
	}

	public static double clamp(double num, double min, double max) {
		return num < min ? min : Math.min(num, max);
	}

	public static double random(double min, double max) {
		return Math.random() * (max - min) + min;
	}

	public static double round(double wert, int stellen) {
		return Math.round(wert * Math.pow(10.0D, stellen)) / Math.pow(10.0D, stellen);
	}

	public static double interpolate(double current, double old, double scale) {
		return old + (current - old) * scale;
	}

}
