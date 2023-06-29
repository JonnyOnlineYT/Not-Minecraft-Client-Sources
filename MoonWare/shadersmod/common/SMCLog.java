package shadersmod.common;

import java.util.logging.Logger;

public abstract class SMCLog {
    private static final Logger LOGGER = Logger.getLogger("Shaders");

    public static void severe(String message) {
        LOGGER.severe(message);
    }

    public static void warning(String message) {
        LOGGER.warning(message);
    }

    public static void info(String message) {
        LOGGER.info(message);
    }

    public static void severe(String message, Object... args) {
        LOGGER.severe(String.format(message, args));
    }

    public static void warning(String message, Object... args) {
        LOGGER.warning(String.format(message, args));
    }

    public static void info(String message, Object... args) {
        LOGGER.info(String.format(message, args));
    }
}
