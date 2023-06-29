package net.minecraft.network;

import com.google.gson.*;
import com.mojang.authlib.GameProfile;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.text.Component;

import javax.annotation.Nullable;
import java.lang.reflect.Type;
import java.util.UUID;

public class ServerPingData {
    @Getter @Setter @Nullable private Component description;
    @Getter @Setter @Nullable private ServerPingData.Players players;
    @Getter @Setter @Nullable private ServerPingData.Version version;
    @Getter @Setter @Nullable private String favicon;

    public static class Serializer implements JsonDeserializer<ServerPingData>, JsonSerializer<ServerPingData> {
        @Override
        public ServerPingData deserialize(JsonElement json, Type type, JsonDeserializationContext ctx) throws JsonParseException {
            JsonObject jo = JsonUtils.getJsonObject(json, "status");
            ServerPingData obj = new ServerPingData();
            if (jo.has("description")) obj.setDescription(ctx.deserialize(jo.get("description"), Component.class));
            if (jo.has("players")) obj.setPlayers(ctx.deserialize(jo.get("players"), Players.class));
            if (jo.has("version")) obj.setVersion(ctx.deserialize(jo.get("version"), Version.class));
            if (jo.has("favicon")) obj.setFavicon(JsonUtils.getString(jo, "favicon"));
            return obj;
        }

        @Override
        public JsonElement serialize(ServerPingData obj, Type type, JsonSerializationContext ctx) {
            JsonObject jo = new JsonObject();
            if (obj.getDescription() != null) jo.add("description", ctx.serialize(obj.getDescription()));
            if (obj.getPlayers() != null) jo.add("players", ctx.serialize(obj.getPlayers()));
            if (obj.getVersion() != null) jo.add("version", ctx.serialize(obj.getVersion()));
            if (obj.getFavicon() != null) jo.addProperty("favicon", obj.getFavicon());
            return jo;
        }
    }

    @RequiredArgsConstructor
    public static class Players {
        @Getter private final int max;
        @Getter private final int online;
        @Getter @Setter @Nullable private GameProfile[] players;

        public static class Serializer implements JsonDeserializer<Players>, JsonSerializer<Players> {
            @Override
            public Players deserialize(JsonElement json, Type type, JsonDeserializationContext ctx) throws JsonParseException {
                JsonObject jo = JsonUtils.getJsonObject(json, "players");
                Players obj = new Players(JsonUtils.getInt(jo, "max"), JsonUtils.getInt(jo, "online"));
                if (!JsonUtils.isJsonArray(jo, "sample")) return obj;
                JsonArray ja = JsonUtils.getJsonArray(jo, "sample");
                if (ja.isEmpty()) return obj;
                GameProfile[] profile = new GameProfile[ja.size()];
                for (int i = 0; i < profile.length; ++i) {
                    JsonObject entry = JsonUtils.getJsonObject(ja.get(i), "player[" + i + "]");
                    String s = JsonUtils.getString(entry, "id");
                    profile[i] = new GameProfile(UUID.fromString(s), JsonUtils.getString(entry, "name"));
                }
                obj.setPlayers(profile);
                return obj;
            }

            @Override
            public JsonElement serialize(Players obj, Type type, JsonSerializationContext ctx) {
                JsonObject jo = new JsonObject();
                jo.addProperty("max", obj.getMax());
                jo.addProperty("online", obj.getOnline());
                if (obj.getPlayers() == null || obj.getPlayers().length <= 0) return jo;
                JsonArray ja = new JsonArray();
                for (GameProfile profile : obj.getPlayers()) {
                    JsonObject entry = new JsonObject();
                    entry.addProperty("id", profile.getId() == null ? "" : profile.getId().toString());
                    entry.addProperty("name", profile.getName());
                    ja.add(entry);
                }
                jo.add("sample", ja);
                return jo;
            }
        }
    }

    @RequiredArgsConstructor
    public static class Version {
        @Getter private final String version;
        @Getter private final int protocol;

        public static class Serializer implements JsonDeserializer<Version>, JsonSerializer<Version> {
            @Override
            public Version deserialize(JsonElement json, Type type, JsonDeserializationContext ctx) throws JsonParseException {
                JsonObject jo = JsonUtils.getJsonObject(json, "version");
                return new ServerPingData.Version(JsonUtils.getString(jo, "name"), JsonUtils.getInt(jo, "protocol"));
            }

            @Override
            public JsonElement serialize(Version obj, Type type, JsonSerializationContext ctx) {
                JsonObject jsonobject = new JsonObject();
                jsonobject.addProperty("name", obj.getVersion());
                jsonobject.addProperty("protocol", obj.getProtocol());
                return jsonobject;
            }
        }
    }
}
