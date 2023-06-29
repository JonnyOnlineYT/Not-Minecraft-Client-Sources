package volcano.summer.client.util;

public enum AnimationUtils {

	INSTANCE;

	/**
	 * Return's a value between 0-1 depending how far animation is gone through
	 *
	 * @return
	 */

	public float value(long startTime) {
		return Math.min(1.0F, (float) Math.pow((double) (System.currentTimeMillis() - startTime) / 10.0D, 1.4D));
	}

}