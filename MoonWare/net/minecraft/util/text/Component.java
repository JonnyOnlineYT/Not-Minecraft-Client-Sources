package net.minecraft.util.text;

import com.google.gson.*;
import net.minecraft.util.EnumTypeAdapterFactory;
import net.minecraft.util.JsonUtils;

import javax.annotation.Nullable;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public interface Component extends Iterable<Component> {
    Component setStyle(Style style);
    Style getStyle();
    Component append(String text);
    Component append(Component component);
    String getString();
    String asString();
    String asFormattedString();
    List<Component> getSiblings();
    Component copy();

    class Serializer implements JsonDeserializer<Component>, JsonSerializer<Component> {
        public static final Gson GSON = new GsonBuilder()
                .registerTypeHierarchyAdapter(Component.class, new Component.Serializer())
                .registerTypeHierarchyAdapter(Style.class, new Style.Serializer())
                .registerTypeAdapterFactory(new EnumTypeAdapterFactory()).create();

        public Component deserialize(JsonElement json, Type type, JsonDeserializationContext ctx) throws JsonParseException {
            if (json.isJsonPrimitive()) return new TextComponent(json.getAsString());
            if (json.isJsonObject()) {
                JsonObject jo = json.getAsJsonObject();
                Component component;
                if (jo.has("text")) {
                    component = new TextComponent(jo.get("text").getAsString());
                } else if (jo.has("translate")) {
                    String s = jo.get("translate").getAsString();
                    if (jo.has("with")) {
                        JsonArray jsonarray = jo.getAsJsonArray("with");
                        Object[] args = new Object[jsonarray.size()];
                        for (int i = 0; i < args.length; ++i) {
                            args[i] = deserialize(jsonarray.get(i), type, ctx);
                            if (args[i] instanceof TextComponent) {
                                TextComponent c = (TextComponent) args[i];
                                if (c.getStyle().isEmpty() && c.getSiblings().isEmpty()) {
                                    args[i] = c.getText();
                                }
                            }
                        }
                        component = new TranslatableComponent(s, args);
                    } else {
                        component = new TranslatableComponent(s);
                    }
                } else if (jo.has("score")) {
                    JsonObject scoreJson = jo.getAsJsonObject("score");
                    if (!scoreJson.has("name") || !scoreJson.has("objective")) {
                        throw new JsonParseException("A score component needs a least a name and an objective");
                    }
                    component = new ScoreComponent(JsonUtils.getString(scoreJson, "name"), JsonUtils.getString(scoreJson, "objective"));
                    if (scoreJson.has("value")) {
                        ((ScoreComponent) component).setValue(JsonUtils.getString(scoreJson, "value"));
                    }
                } else if (jo.has("selector")) {
                    component = new SelectorComponent(JsonUtils.getString(jo, "selector"));
                } else if (jo.has("keybind")) {
                    component = new KeybindComponent(JsonUtils.getString(jo, "keybind"));
                } else {
                    throw new JsonParseException("Don't know how to turn " + json + " into a Component, expected one of 'text', 'translate', 'score', 'selector', 'keybind'.");
                }
                if (jo.has("extra")) {
                    JsonArray ja = jo.getAsJsonArray("extra");
                    if (ja.size() <= 0) throw new JsonParseException("Unexpected empty array of components: " + json);
                    for (JsonElement je : ja) {
                        component.append(deserialize(je, type, ctx));
                    }
                }
                component.setStyle(ctx.deserialize(json, Style.class));
                return component;
            }
            if (json.isJsonArray()) {
                JsonArray ja = json.getAsJsonArray();
                Component c = null;
                for (JsonElement je : ja) {
                    Component sc = deserialize(je, je.getClass(), ctx);
                    if (c == null) {
                        c = sc;
                    } else {
                        c.append(sc);
                    }
                }
                return c;
            }
            throw new JsonParseException("Don't know how to turn " + json + " into a Component, expected primitive, array or object.");
        }

        public JsonElement serialize(Component obj, Type type, JsonSerializationContext ctx) {
            JsonObject jo = new JsonObject();
            if (!obj.getStyle().isEmpty()) {
                JsonElement je = ctx.serialize(obj.getStyle());
                if (je.isJsonObject()) {
                    for (Map.Entry<String, JsonElement> entry : je.getAsJsonObject().entrySet()) {
                        jo.add(entry.getKey(), entry.getValue());
                    }
                }
            }
            if (!obj.getSiblings().isEmpty()) {
                JsonArray ja = new JsonArray();
                for (Component c : obj.getSiblings()) {
                    ja.add(serialize(c, c.getClass(), ctx));
                }
                jo.add("extra", ja);
            }
            if (obj instanceof TextComponent) {
                jo.addProperty("text", ((TextComponent) obj).getText());
            } else if (obj instanceof TranslatableComponent) {
                TranslatableComponent tc = (TranslatableComponent) obj;
                jo.addProperty("translate", tc.getKey());
                if (tc.getArgs().length > 0) {
                    JsonArray ja = new JsonArray();
                    for (Object object : tc.getArgs()) {
                        if (object instanceof Component) {
                            ja.add(serialize((Component) object, object.getClass(), ctx));
                        } else {
                            ja.add(new JsonPrimitive(String.valueOf(object)));
                        }
                    }
                    jo.add("with", ja);
                }
            } else if (obj instanceof ScoreComponent) {
                ScoreComponent sc = (ScoreComponent) obj;
                JsonObject score = new JsonObject();
                score.addProperty("name", sc.getName());
                score.addProperty("objective", sc.getObjective());
                score.addProperty("value", sc.asString());
                jo.add("score", score);
            } else if (obj instanceof SelectorComponent) {
                SelectorComponent sc = (SelectorComponent) obj;
                jo.addProperty("selector", sc.getSelector());
            } else if (obj instanceof KeybindComponent) {
                KeybindComponent kc = (KeybindComponent) obj;
                jo.addProperty("keybind", kc.getKey());
            } else {
                throw new IllegalArgumentException("Don't know how to serialize " + obj + " as a Component");
            }

            return jo;
        }

        public static String componentToJson(Component component) {
            return GSON.toJson(component);
        }

        @Nullable
        public static Component jsonToComponent(String json) {
            return JsonUtils.gsonDeserialize(GSON, json, Component.class, false);
        }

        @Nullable
        public static Component fromJsonLenient(String json) {
            return JsonUtils.gsonDeserialize(GSON, json, Component.class, true);
        }
    }
}
