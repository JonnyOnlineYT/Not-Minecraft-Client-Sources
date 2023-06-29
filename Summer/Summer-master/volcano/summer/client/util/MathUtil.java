package volcano.summer.client.util;

import java.awt.Desktop;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.security.SecureRandom;
import java.util.Random;
import java.util.regex.Pattern;

import org.apache.logging.log4j.core.Logger;

import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;

public class MathUtil {

	private static Random random = new Random();

	public static double roundToPlace(double value, int places) {
		if (places < 0) {
			throw new IllegalArgumentException();
		}
		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

	public static double square(double in) {
		return in * in;
	}

	public static boolean contains(float x, float y, float minX, float minY, float maxX, float maxY) {
		if ((x > minX) && (x < maxX) && (y > minY) && (y < maxY)) {
			return true;
		}
		return false;
	}

	public static double roundHover(double in, int places) {
		places = (int) MathHelper.clamp_double(places, 0.0D, 2.147483647E9D);
		return Double.parseDouble(String.format("%." + places + "f", new Object[] { Double.valueOf(in) }));
	}

	public static double round(double value, int places) {
		if (places < 0) {
			throw new IllegalArgumentException();
		}
		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

	public static int getMid(int x1, int x2) {
		return (x1 + x2) / 2;
	}

	private static final Logger logger = null;

	public static boolean openLink(String url) {
		try {
			Desktop.getDesktop().browse(new URI(url));
			return true;
		} catch (Exception e) {
			logger.error("Failed to open link", e);
		}
		return false;
	}

	public static class BlockData {
		public BlockPos position;
		public EnumFacing face;

		public BlockData(BlockPos position, EnumFacing face) {
			this.position = position;
			this.face = face;
		}
	}

	private long lastCheck = getSystemTime();

	public boolean hasReach(float mil) {
		return getTimePassed() >= (mil);
	}

	public boolean hasReach(double mil) {
		return getTimePassed() >= (mil);
	}

	public void reset() {
		lastCheck = getSystemTime();
	}

	private long getTimePassed() {
		return getSystemTime() - lastCheck;
	}

	private long getSystemTime() {
		return System.nanoTime() / (long) (1E6);
	}

	public static class StringUtil {
		private static final Pattern patternControlCode;
		private static final Pattern patternColorCode;
		private static final Pattern patternFormatCode;

		static {
			patternControlCode = Pattern.compile("(?i)\\u00A7[0-9A-FK-OR]");
			patternColorCode = Pattern.compile("(?i)\\u00A7[0-9A-F]");
			patternFormatCode = Pattern.compile("(?i)\\u00A7[K-O]");
		}

		public static String capitalize(String line) {
			return Character.toUpperCase(line.charAt(0)) + line.substring(1);
		}

		public static String stripControlCodes(final String s) {
			return StringUtil.patternControlCode.matcher(s).replaceAll("");
		}

		public static String stripColorCodes(final String s) {
			return StringUtil.patternColorCode.matcher(s).replaceAll("");
		}

		public static String stripFormatCodes(final String s) {
			return StringUtil.patternFormatCode.matcher(s).replaceAll("");
		}
	}

	public static float getAngleDifference(float direction, float rotationYaw) {
		float phi = Math.abs(rotationYaw - direction) % 360.0F;
		float distance = phi > 180.0F ? 360.0F - phi : phi;
		return distance;
	}

	public static Random getRandom() {
		return random;
	}

	public static int getRandomInRange(int min, int max) {
		Random rand = new Random();
		int randomNum = rand.nextInt(max - min + 1) + min;

		return randomNum;
	}
	public static double randDouble(double min, double max) {
        SecureRandom rand = new SecureRandom();
        return rand.nextDouble() * (max - min) + min;
    }
}
