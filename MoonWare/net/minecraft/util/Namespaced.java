package net.minecraft.util;

import com.google.gson.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.lang.reflect.Type;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class Namespaced implements Comparable<Namespaced> {
    protected final String namespace;
    protected final String path;

    public Namespaced(String namespaced) {
        int i = namespaced.indexOf(':');
        if (i < 0) {
            namespace = "minecraft";
            path = namespaced;
            return;
        }
        namespace = namespaced.substring(0, i);
        path = namespaced.substring(i + 1);
    }

    @Override
    public String toString() {
        return namespace + ':' + path;
    }

    @Override
    public int compareTo(Namespaced other) {
        int i = namespace.compareTo(other.namespace);
        if (i == 0) i = path.compareTo(other.path);
        return i;
    }

    public static class Serializer implements JsonDeserializer<Namespaced>, JsonSerializer<Namespaced> {
        public Namespaced deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
            return new Namespaced(JsonUtils.getString(json, "location"));
        }

        public JsonElement serialize(Namespaced object, Type type, JsonSerializationContext context) {
            return new JsonPrimitive(object.toString());
        }
    }
}
