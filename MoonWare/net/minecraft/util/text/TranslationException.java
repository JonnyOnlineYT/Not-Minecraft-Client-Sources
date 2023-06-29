package net.minecraft.util.text;

public class TranslationException extends IllegalArgumentException {
    public TranslationException(TranslatableComponent component, String message) {
        super(String.format("Error parsing: %s: %s", component, message));
    }

    public TranslationException(TranslatableComponent component, int index) {
        super(String.format("Invalid index %d requested for %s", index, component));
    }

    public TranslationException(TranslatableComponent component, Throwable cause) {
        super(String.format("Error while parsing: %s", component), cause);
    }
}
