package net.mcleaks;

import net.minecraft.util.*;
import net.minecraft.client.*;
import java.lang.reflect.*;

public class SessionManager {
	public static void setSession(final String mcName) {
		setSession(new Session(mcName, "", "", "mojang"));
	}

	public static void setSession(final Session session) {
		setFieldByClass(Minecraft.getMinecraft(), session);
	}

	public static void setFieldByClass(final Object parentObject, final Object newObject) {
		Field field = null;
		for (final Field f : parentObject.getClass().getDeclaredFields()) {
			if (f.getType().isInstance(newObject)) {
				field = f;
				break;
			}
		}
		if (field == null) {
			return;
		}
		try {
			final boolean accessible = field.isAccessible();
			field.setAccessible(true);
			field.set(parentObject, newObject);
			field.setAccessible(accessible);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
}
