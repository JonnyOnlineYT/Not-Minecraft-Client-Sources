package net.mcleaks;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;
import volcano.summer.client.util.Colors;

public class MCLeaks {
	public static Session savedSession;
	private static String mcLeaksSession;
	private static String mcName;

	public static boolean isAltActive() {
		return MCLeaks.mcLeaksSession != null;
	}

	public static String getMCLeaksSession() {
		return MCLeaks.mcLeaksSession;
	}

	public static String getMCName() {
		return MCLeaks.mcName;
	}

	public static void refresh(final String session, final String name) {
		MCLeaks.mcLeaksSession = session;
		MCLeaks.mcName = name;
	}

	public static void remove() {
		MCLeaks.mcLeaksSession = null;
		MCLeaks.mcName = null;
	}

	public static String getStatus() {
		String status = Colors.GOLD + "No token active. Using account " + Colors.YELLOW
				+ Minecraft.getMinecraft().getSession().getUsername() + Colors.GOLD + " to play!";
		if (MCLeaks.mcLeaksSession != null) {
			status = Colors.GREEN + "Token active. Using account " + Colors.AQUA + MCLeaks.mcName + Colors.GREEN
					+ " to play!";
		}
		return status;
	}

	static {
		MCLeaks.savedSession = null;
	}
}
