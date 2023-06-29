package org.moonware.client.utils;

import it.unimi.dsi.fastutil.floats.Float2ObjectArrayMap;
import it.unimi.dsi.fastutil.floats.Float2ObjectMap;

import java.awt.*;
import java.io.InputStream;

// ВНИМАНИЕ: Эта хуйня подгружает из assets/moonware/font, не из assets/minecraft/moonware/font.
public enum MWFont {
    JETBRAINS_MONO_NL_REGULAR("jetbrains_mono.ttf"),
    MW_FONT("mw_font.ttf"),
    MONTSERRAT_LIGHT("montserrat_light.ttf"),
    MONTSERRAT_REGULAR("montserrat_regular.ttf"),
    MONTSERRAT_SEMIBOLD("montserrat_semibold.ttf"),
    MONTSERRAT_BOLD("montserrat_bold.ttf"),
    SF_UI_DISPLAY_REGULAR("sf_ui_display_regular.ttf"),
    SF_UI_TEXT_SEMIBOLD("sf_ui_text_semibold.ttf"),
    GREYCLIFFCF_MEDIUM("greycliffcf_medium.ttf"), //AKA Tenacity AKA MoonFont
    ELEGANT_ICONS("elegant_icons.ttf"),
    ICOMOON_ICONS("icomoon_icons.ttf"), //AKA Icons
    CHECK("check.ttf"), //AKA IconFont
    STYLO("stylo.ttf"),
    STYLO_BOLD("stylo_bold.ttf"),
    NEBORA_BLACK("nebora_black.ttf"),
    NEBORA_BOLD("nebora_bold.ttf");

    private final String file;
    private final Float2ObjectMap<CustomFont> fontMap = new Float2ObjectArrayMap<>();
    MWFont(String file) {
        this.file = file;
    }

    public CustomFont get(float size) {
        return fontMap.computeIfAbsent(size, k -> {
            try (InputStream in = MWFont.class.getResourceAsStream("/assets/moonware/font/" + file)) {
                return new CustomFont(Font.createFont(Font.TRUETYPE_FONT, in).deriveFont(size));
            } catch (Exception e) {
                throw new RuntimeException("Unable to load font: " + this, e);
            }
        });
    }
}
