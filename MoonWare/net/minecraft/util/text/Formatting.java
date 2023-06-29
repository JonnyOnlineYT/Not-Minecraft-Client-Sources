package net.minecraft.util.text;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Getter;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

public enum Formatting {
    BLACK("BLACK", '0', 0),
    DARK_BLUE("DARK_BLUE", '1', 1),
    DARK_GREEN("DARK_GREEN", '2', 2),
    DARK_AQUA("DARK_AQUA", '3', 3),
    DARK_RED("DARK_RED", '4', 4),
    DARK_PURPLE("DARK_PURPLE", '5', 5),
    GOLD("GOLD", '6', 6),
    GRAY("GRAY", '7', 7),
    DARK_GRAY("DARK_GRAY", '8', 8),
    BLUE("BLUE", '9', 9),
    GREEN("GREEN", 'a', 10),
    AQUA("AQUA", 'b', 11),
    RED("RED", 'c', 12),
    LIGHT_PURPLE("LIGHT_PURPLE", 'd', 13),
    YELLOW("YELLOW", 'e', 14),
    WHITE("WHITE", 'f', 15),
    OBFUSCATED("OBFUSCATED", 'k', true),
    BOLD("BOLD", 'l', true),
    STRIKETHROUGH("STRIKETHROUGH", 'm', true),
    UNDERLINE("UNDERLINE", 'n', true),
    ITALIC("ITALIC", 'o', true),
    RESET("RESET", 'r', -1);

    private static final Map<String, Formatting> NAME_MAPPING = Maps.newHashMap();
    private static final Pattern FORMATTING_CODE_PATTERN = Pattern.compile("(?i)\u00a7[0-9A-FK-OR]");
    private final String name;
    @Getter private final boolean decoration;
    private final String formatString;
    @Getter private final int index;

    private static String lowercaseAlpha(String p_175745_0_) {
        return p_175745_0_.toLowerCase(Locale.ROOT).replaceAll("[^a-z]", "");
    }

    Formatting(String name, char code, int i) {
        this(name, code, false, i);
    }

    Formatting(String name, char code, boolean decoration) {
        this(name, code, decoration, -1);
    }

    Formatting(String name, char code, boolean decoration, int index) {
        this.name = name;
        this.decoration = decoration;
        this.index = index;
        formatString = "\u00a7" + code;
    }

    /**
     * Checks if this is a color code.
     */
    public boolean isColor() {
        return !decoration && this != RESET;
    }

    /**
     * Gets the friendly name of this value.
     */
    public String getName() {
        return name().toLowerCase(Locale.ROOT);
    }

    @Override
    public String toString() {
        return formatString;
    }

    @Nullable
    public static String strip(@Nullable String text) {
        return text == null ? null : FORMATTING_CODE_PATTERN.matcher(text).replaceAll("");
    }

    @Nullable
    public static Formatting getValueByName(@Nullable String friendlyName) {
        return friendlyName == null ? null : NAME_MAPPING.get(lowercaseAlpha(friendlyName));
    }

    @Nullable
    public static Formatting fromColorIndex(int index) {
        if (index < 0) {
            return RESET;
        } else {
            for (Formatting textformatting : values()) {
                if (textformatting.getIndex() == index) {
                    return textformatting;
                }
            }

            return null;
        }
    }

    public static Collection<String> getValidValues(boolean p_96296_0_, boolean p_96296_1_) {
        List<String> list = Lists.newArrayList();

        for (Formatting textformatting : values()) {
            if ((!textformatting.isColor() || p_96296_0_) && (!textformatting.isDecoration() || p_96296_1_)) {
                list.add(textformatting.getName());
            }
        }

        return list;
    }

    static {
        for (Formatting textformatting : values()) {
            NAME_MAPPING.put(lowercaseAlpha(textformatting.name), textformatting);
        }
    }
}
