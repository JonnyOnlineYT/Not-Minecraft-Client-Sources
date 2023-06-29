package org.spray.heaven.util;

public class CryptEffect {

	private Timer timerIn = new Timer();
	private Timer timerFadeBuffer = new Timer();
	private Timer timerCycle = new Timer();

	private String[] messages;
	private String codeletters;
	private int message;
	private int current_length;

	public String text;
	private boolean fadeBuffer;
	private int[] fade;

	private boolean inUnlock;
	private boolean fadeUnlock;
	private boolean cycleUnlock;
	private boolean time;

	public CryptEffect(String[] messages) {
		this.messages = messages;
		this.message = 0;
		this.current_length = 0;
		codeletters = "&#*+%?@$/@}{[]";
	//	inUnlock = true;
		setTimeout(() -> animateIn(), 100);
	}

	private String generateRandomString(int length) {
		String random_text = "";
		while (random_text.length() < length) {
			random_text += codeletters.charAt((int) Math.floor(Math.random() * codeletters.length()));
		}
		return random_text;
	}

	private void animate() {
	//	if (timerIn.hasReached(2000) && current_length < messages[message].length() && !inUnlock)
	//		inUnlock = true;
			if (current_length < messages[message].length() && inUnlock) {
				current_length = messages[message].length();
				if (current_length > messages[message].length()) {
					current_length = messages[message].length();
				}

				text = generateRandomString(current_length);
				//System.out.println(inUnlock);
			} else {
				inUnlock = false;
				fadeUnlock = true;
			}

		if (fadeUnlock) {
			for (int i = 0; i < messages[message].length(); i++) {
				fade = new int[] { (int) (Math.floor(Math.random() * 12) + 1), messages[message].charAt(i) };
			}
			boolean do_cycles = false;
			String message = "";

			for (int i = 0; i < fade.length; i++) {
				int c = fade[0];
				int k = fade[1];
				if (c > 0) {
					do_cycles = true;
					c--;
					message += codeletters.charAt((int) Math.floor(Math.random() * codeletters.length()));
				} else {
					message += k;
				}
			}
			if (!do_cycles) {
				fadeUnlock = false;
				cycleUnlock = true;
			}
		}
		if (cycleUnlock) {
			message += 1;
			if (message >= messages.length) {
				message = 0;
			}

			current_length = 0;
			fadeBuffer = false;
			
			if (timerCycle.hasReached(2000)) {
				cycleUnlock = false;
				timerIn.reset();
				timerCycle.reset();
			}
		}
	}

	private void animateIn() {
		if (current_length < messages[message].length()) {
			current_length += 2;
			if (current_length > messages[message].length()) {
				current_length = messages[message].length();
			}

			text = generateRandomString(current_length);
			// System.out.println(current_length);

			setTimeout(() -> animateIn(), 20);
		} else {
			setTimeout(() -> animateFadeBuffer(), 20);

		}
	}

	private void animateFadeBuffer() {
		if (!fadeBuffer) {
			for (int i = 0; i < messages[message].length(); i++) {
				fade = new int[] { (int) (Math.floor(Math.random() * 12) + 1), messages[message].charAt(i) };
			}
			boolean do_cycles = false;
			String message = "";

			for (int i = 0; i < fade.length; i++) {
				int c = fade[0];
				int k = fade[1];
				if (c > 0) {
					do_cycles = true;
					c--;
					message += codeletters.charAt((int) Math.floor(Math.random() * codeletters.length()));
				} else {
					message += k;
				}
			}
			System.out.println("fade");
			
			text = message;

			if (do_cycles) {
				setTimeout(() -> animateFadeBuffer(), 50);
			} else {
				setTimeout(() -> cycleText(), 2000);
			}
		}
	}

	private void cycleText() {
		System.out.println("cycle");
		message += 1;
		if (message >= messages.length) {
			message = 0;
		}

		current_length = 0;
		fadeBuffer = false;

		setTimeout(() -> animateIn(), 200);
	}

	public String getText() {
		return text;
	}

	public static void setTimeout(Runnable runnable, int delay) {
		new Thread(() -> {
			try {
				Thread.sleep(delay);
				runnable.run();
			} catch (Exception e) {
				System.err.println(e);
			}
		}).start();
	}
}
