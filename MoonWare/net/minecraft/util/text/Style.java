package net.minecraft.util.text;

import com.google.gson.*;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;

import javax.annotation.Nullable;
import java.lang.reflect.Type;

public class Style {
    /**
     * The parent of this ChatStyle.  Used for looking up values that this instance does not override.
     */
    private Style parentStyle;
    private Formatting color;
    private Boolean bold;
    private Boolean italic;
    private Boolean underlined;
    private Boolean strikethrough;
    private Boolean obfuscated;
    private ClickEvent clickEvent;
    private HoverEvent hoverEvent;
    private String insertion;

    /**
     * The base of the ChatStyle hierarchy.  All ChatStyle instances are implicitly children of this.
     */
    private static final Style ROOT = new Style() {
        @Nullable
        public Formatting getColor() {
            return null;
        }

        public boolean getBold() {
            return false;
        }

        public boolean getItalic() {
            return false;
        }

        public boolean getStrikethrough() {
            return false;
        }

        public boolean getUnderlined() {
            return false;
        }

        public boolean getObfuscated() {
            return false;
        }

        @Nullable
        public ClickEvent getClickEvent() {
            return null;
        }

        @Nullable
        public HoverEvent getHoverEvent() {
            return null;
        }

        @Nullable
        public String getInsertion() {
            return null;
        }

        public Style setColor(Formatting color) {
            throw new UnsupportedOperationException();
        }

        public Style setBold(Boolean boldIn) {
            throw new UnsupportedOperationException();
        }

        public Style setItalic(Boolean italic) {
            throw new UnsupportedOperationException();
        }

        public Style setStrikethrough(Boolean strikethrough) {
            throw new UnsupportedOperationException();
        }

        public Style setUnderlined(Boolean underlined) {
            throw new UnsupportedOperationException();
        }

        public Style setObfuscated(Boolean obfuscated) {
            throw new UnsupportedOperationException();
        }

        public Style setClickEvent(ClickEvent event) {
            throw new UnsupportedOperationException();
        }

        public Style setHoverEvent(HoverEvent event) {
            throw new UnsupportedOperationException();
        }

        public Style setParentStyle(Style parent) {
            throw new UnsupportedOperationException();
        }

        public String toString() {
            return "Style.ROOT";
        }

        public Style createShallowCopy() {
            return this;
        }

        public Style createDeepCopy() {
            return this;
        }

        public String getFormattingCode() {
            return "";
        }
    };

    @Nullable

    /**
     * Gets the effective color of this ChatStyle.
     */ public Formatting getColor() {
        return color == null ? getParent().getColor() : color;
    }

    /**
     * Whether or not text of this ChatStyle should be in bold.
     */
    public boolean getBold() {
        return bold == null ? getParent().getBold() : bold.booleanValue();
    }

    /**
     * Whether or not text of this ChatStyle should be italicized.
     */
    public boolean getItalic() {
        return italic == null ? getParent().getItalic() : italic.booleanValue();
    }

    /**
     * Whether or not to format text of this ChatStyle using strikethrough.
     */
    public boolean getStrikethrough() {
        return strikethrough == null ? getParent().getStrikethrough() : strikethrough.booleanValue();
    }

    /**
     * Whether or not text of this ChatStyle should be underlined.
     */
    public boolean getUnderlined() {
        return underlined == null ? getParent().getUnderlined() : underlined.booleanValue();
    }

    /**
     * Whether or not text of this ChatStyle should be obfuscated.
     */
    public boolean getObfuscated() {
        return obfuscated == null ? getParent().getObfuscated() : obfuscated.booleanValue();
    }

    /**
     * Whether or not this style is empty (inherits everything from the parent).
     */
    public boolean isEmpty() {
        return bold == null && italic == null && strikethrough == null && underlined == null && obfuscated == null && color == null && clickEvent == null && hoverEvent == null && insertion == null;
    }

    @Nullable

    /**
     * The effective chat click event.
     */ public ClickEvent getClickEvent() {
        return clickEvent == null ? getParent().getClickEvent() : clickEvent;
    }

    @Nullable

    /**
     * The effective chat hover event.
     */ public HoverEvent getHoverEvent() {
        return hoverEvent == null ? getParent().getHoverEvent() : hoverEvent;
    }

    @Nullable

    /**
     * Get the text to be inserted into Chat when the component is shift-clicked
     */ public String getInsertion() {
        return insertion == null ? getParent().getInsertion() : insertion;
    }

    /**
     * Sets the color for this ChatStyle to the given value.  Only use color values for this; set other values using the
     * specific methods.
     */
    public Style setColor(Formatting color) {
        this.color = color;
        return this;
    }

    /**
     * Sets whether or not text of this ChatStyle should be in bold.  Set to false if, e.g., the parent style is bold
     * and you want text of this style to be unbolded.
     */
    public Style setBold(Boolean boldIn) {
        bold = boldIn;
        return this;
    }

    /**
     * Sets whether or not text of this ChatStyle should be italicized.  Set to false if, e.g., the parent style is
     * italicized and you want to override that for this style.
     */
    public Style setItalic(Boolean italic) {
        this.italic = italic;
        return this;
    }

    /**
     * Sets whether or not to format text of this ChatStyle using strikethrough.  Set to false if, e.g., the parent
     * style uses strikethrough and you want to override that for this style.
     */
    public Style setStrikethrough(Boolean strikethrough) {
        this.strikethrough = strikethrough;
        return this;
    }

    /**
     * Sets whether or not text of this ChatStyle should be underlined.  Set to false if, e.g., the parent style is
     * underlined and you want to override that for this style.
     */
    public Style setUnderlined(Boolean underlined) {
        this.underlined = underlined;
        return this;
    }

    /**
     * Sets whether or not text of this ChatStyle should be obfuscated.  Set to false if, e.g., the parent style is
     * obfuscated and you want to override that for this style.
     */
    public Style setObfuscated(Boolean obfuscated) {
        this.obfuscated = obfuscated;
        return this;
    }

    /**
     * Sets the event that should be run when text of this ChatStyle is clicked on.
     */
    public Style setClickEvent(ClickEvent event) {
        clickEvent = event;
        return this;
    }

    /**
     * Sets the event that should be run when text of this ChatStyle is hovered over.
     */
    public Style setHoverEvent(HoverEvent event) {
        hoverEvent = event;
        return this;
    }

    /**
     * Set a text to be inserted into Chat when the component is shift-clicked
     */
    public Style setInsertion(String insertion) {
        this.insertion = insertion;
        return this;
    }

    /**
     * Sets the fallback ChatStyle to use if this ChatStyle does not override some value.  Without a parent, obvious
     * defaults are used (bold: false, underlined: false, etc).
     */
    public Style setParentStyle(Style parent) {
        parentStyle = parent;
        return this;
    }

    /**
     * Gets the equivalent text formatting code for this style, without the initial section sign (U+00A7) character.
     */
    public String getFormattingCode() {
        if (isEmpty()) {
            return parentStyle != null ? parentStyle.getFormattingCode() : "";
        } else {
            StringBuilder stringbuilder = new StringBuilder();

            if (getColor() != null) {
                stringbuilder.append(getColor());
            }

            if (getBold()) {
                stringbuilder.append(Formatting.BOLD);
            }

            if (getItalic()) {
                stringbuilder.append(Formatting.ITALIC);
            }

            if (getUnderlined()) {
                stringbuilder.append(Formatting.UNDERLINE);
            }

            if (getObfuscated()) {
                stringbuilder.append(Formatting.OBFUSCATED);
            }

            if (getStrikethrough()) {
                stringbuilder.append(Formatting.STRIKETHROUGH);
            }

            return stringbuilder.toString();
        }
    }

    /**
     * Gets the immediate parent of this ChatStyle.
     */
    private Style getParent() {
        return parentStyle == null ? ROOT : parentStyle;
    }

    public String toString() {
        return "Style{hasParent=" + (parentStyle != null) + ", color=" + color + ", bold=" + bold + ", italic=" + italic + ", underlined=" + underlined + ", obfuscated=" + obfuscated + ", clickEvent=" + getClickEvent() + ", hoverEvent=" + getHoverEvent() + ", insertion=" + getInsertion() + '}';
    }

    public boolean equals(Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        } else if (!(p_equals_1_ instanceof Style)) {
            return false;
        } else {
            boolean flag;
            label77:
            {
                Style style = (Style) p_equals_1_;

                if (getBold() == style.getBold() && getColor() == style.getColor() && getItalic() == style.getItalic() && getObfuscated() == style.getObfuscated() && getStrikethrough() == style.getStrikethrough() && getUnderlined() == style.getUnderlined()) {
                    label71:
                    {
                        if (getClickEvent() != null) {
                            if (!getClickEvent().equals(style.getClickEvent())) {
                                break label71;
                            }
                        } else if (style.getClickEvent() != null) {
                            break label71;
                        }

                        if (getHoverEvent() != null) {
                            if (!getHoverEvent().equals(style.getHoverEvent())) {
                                break label71;
                            }
                        } else if (style.getHoverEvent() != null) {
                            break label71;
                        }

                        if (getInsertion() != null) {
                            if (getInsertion().equals(style.getInsertion())) {
                                break label77;
                            }
                        } else if (style.getInsertion() == null) {
                            break label77;
                        }
                    }
                }

                flag = false;
                return flag;
            }
            flag = true;
            return flag;
        }
    }

    public int hashCode() {
        int i = color.hashCode();
        i = 31 * i + bold.hashCode();
        i = 31 * i + italic.hashCode();
        i = 31 * i + underlined.hashCode();
        i = 31 * i + strikethrough.hashCode();
        i = 31 * i + obfuscated.hashCode();
        i = 31 * i + clickEvent.hashCode();
        i = 31 * i + hoverEvent.hashCode();
        i = 31 * i + insertion.hashCode();
        return i;
    }

    /**
     * Creates a shallow copy of this style.  Changes to this instance's values will not be reflected in the copy, but
     * changes to the parent style's values WILL be reflected in both this instance and the copy, wherever either does
     * not override a value.
     */
    public Style createShallowCopy() {
        Style style = new Style();
        style.bold = bold;
        style.italic = italic;
        style.strikethrough = strikethrough;
        style.underlined = underlined;
        style.obfuscated = obfuscated;
        style.color = color;
        style.clickEvent = clickEvent;
        style.hoverEvent = hoverEvent;
        style.parentStyle = parentStyle;
        style.insertion = insertion;
        return style;
    }

    /**
     * Creates a deep copy of this style.  No changes to this instance or its parent style will be reflected in the
     * copy.
     */
    public Style createDeepCopy() {
        Style style = new Style();
        style.setBold(Boolean.valueOf(getBold()));
        style.setItalic(Boolean.valueOf(getItalic()));
        style.setStrikethrough(Boolean.valueOf(getStrikethrough()));
        style.setUnderlined(Boolean.valueOf(getUnderlined()));
        style.setObfuscated(Boolean.valueOf(getObfuscated()));
        style.setColor(getColor());
        style.setClickEvent(getClickEvent());
        style.setHoverEvent(getHoverEvent());
        style.setInsertion(getInsertion());
        return style;
    }

    public static class Serializer implements JsonDeserializer<Style>, JsonSerializer<Style> {
        @Nullable
        public Style deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_) throws JsonParseException {
            if (p_deserialize_1_.isJsonObject()) {
                Style style = new Style();
                JsonObject jsonobject = p_deserialize_1_.getAsJsonObject();

                if (jsonobject == null) {
                    return null;
                } else {
                    if (jsonobject.has("bold")) {
                        style.bold = jsonobject.get("bold").getAsBoolean();
                    }

                    if (jsonobject.has("italic")) {
                        style.italic = jsonobject.get("italic").getAsBoolean();
                    }

                    if (jsonobject.has("underlined")) {
                        style.underlined = jsonobject.get("underlined").getAsBoolean();
                    }

                    if (jsonobject.has("strikethrough")) {
                        style.strikethrough = jsonobject.get("strikethrough").getAsBoolean();
                    }

                    if (jsonobject.has("obfuscated")) {
                        style.obfuscated = jsonobject.get("obfuscated").getAsBoolean();
                    }

                    if (jsonobject.has("color")) {
                        style.color = p_deserialize_3_.deserialize(jsonobject.get("color"), Formatting.class);
                    }

                    if (jsonobject.has("insertion")) {
                        style.insertion = jsonobject.get("insertion").getAsString();
                    }

                    if (jsonobject.has("clickEvent")) {
                        JsonObject jsonobject1 = jsonobject.getAsJsonObject("clickEvent");

                        if (jsonobject1 != null) {
                            JsonPrimitive jsonprimitive = jsonobject1.getAsJsonPrimitive("action");
                            ClickEvent.Action clickevent$action = jsonprimitive == null ? null : ClickEvent.Action.getValueByCanonicalName(jsonprimitive.getAsString());
                            JsonPrimitive jsonprimitive1 = jsonobject1.getAsJsonPrimitive("value");
                            String s = jsonprimitive1 == null ? null : jsonprimitive1.getAsString();

                            if (clickevent$action != null && s != null && clickevent$action.shouldAllowInChat()) {
                                style.clickEvent = new ClickEvent(clickevent$action, s);
                            }
                        }
                    }

                    if (jsonobject.has("hoverEvent")) {
                        JsonObject jsonobject2 = jsonobject.getAsJsonObject("hoverEvent");

                        if (jsonobject2 != null) {
                            JsonPrimitive jsonprimitive2 = jsonobject2.getAsJsonPrimitive("action");
                            HoverEvent.Action hoverevent$action = jsonprimitive2 == null ? null : HoverEvent.Action.getValueByCanonicalName(jsonprimitive2.getAsString());
                            Component itextcomponent = p_deserialize_3_.deserialize(jsonobject2.get("value"), Component.class);

                            if (hoverevent$action != null && itextcomponent != null && hoverevent$action.shouldAllowInChat()) {
                                style.hoverEvent = new HoverEvent(hoverevent$action, itextcomponent);
                            }
                        }
                    }

                    return style;
                }
            } else {
                return null;
            }
        }

        @Nullable
        public JsonElement serialize(Style p_serialize_1_, Type p_serialize_2_, JsonSerializationContext p_serialize_3_) {
            if (p_serialize_1_.isEmpty()) {
                return null;
            } else {
                JsonObject jsonobject = new JsonObject();

                if (p_serialize_1_.bold != null) {
                    jsonobject.addProperty("bold", p_serialize_1_.bold);
                }

                if (p_serialize_1_.italic != null) {
                    jsonobject.addProperty("italic", p_serialize_1_.italic);
                }

                if (p_serialize_1_.underlined != null) {
                    jsonobject.addProperty("underlined", p_serialize_1_.underlined);
                }

                if (p_serialize_1_.strikethrough != null) {
                    jsonobject.addProperty("strikethrough", p_serialize_1_.strikethrough);
                }

                if (p_serialize_1_.obfuscated != null) {
                    jsonobject.addProperty("obfuscated", p_serialize_1_.obfuscated);
                }

                if (p_serialize_1_.color != null) {
                    jsonobject.add("color", p_serialize_3_.serialize(p_serialize_1_.color));
                }

                if (p_serialize_1_.insertion != null) {
                    jsonobject.add("insertion", p_serialize_3_.serialize(p_serialize_1_.insertion));
                }

                if (p_serialize_1_.clickEvent != null) {
                    JsonObject jsonobject1 = new JsonObject();
                    jsonobject1.addProperty("action", p_serialize_1_.clickEvent.getAction().getCanonicalName());
                    jsonobject1.addProperty("value", p_serialize_1_.clickEvent.getValue());
                    jsonobject.add("clickEvent", jsonobject1);
                }

                if (p_serialize_1_.hoverEvent != null) {
                    JsonObject jsonobject2 = new JsonObject();
                    jsonobject2.addProperty("action", p_serialize_1_.hoverEvent.getAction().getCanonicalName());
                    jsonobject2.add("value", p_serialize_3_.serialize(p_serialize_1_.hoverEvent.getValue()));
                    jsonobject.add("hoverEvent", jsonobject2);
                }

                return jsonobject;
            }
        }
    }
}
