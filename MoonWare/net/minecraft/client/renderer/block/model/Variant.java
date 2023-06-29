package net.minecraft.client.renderer.block.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.Namespaced;

public class Variant
{
    private final Namespaced modelLocation;
    private final ModelRotation rotation;
    private final boolean uvLock;
    private final int weight;

    public Variant(Namespaced modelLocationIn, ModelRotation rotationIn, boolean uvLockIn, int weightIn)
    {
        modelLocation = modelLocationIn;
        rotation = rotationIn;
        uvLock = uvLockIn;
        weight = weightIn;
    }

    public Namespaced getModelLocation()
    {
        return modelLocation;
    }

    public ModelRotation getRotation()
    {
        return rotation;
    }

    public boolean isUvLock()
    {
        return uvLock;
    }

    public int getWeight()
    {
        return weight;
    }

    public String toString()
    {
        return "Variant{modelLocation=" + modelLocation + ", rotation=" + rotation + ", uvLock=" + uvLock + ", weight=" + weight + '}';
    }

    public boolean equals(Object p_equals_1_)
    {
        if (this == p_equals_1_)
        {
            return true;
        }
        else if (!(p_equals_1_ instanceof Variant))
        {
            return false;
        }
        else
        {
            Variant variant = (Variant)p_equals_1_;
            return modelLocation.equals(variant.modelLocation) && rotation == variant.rotation && uvLock == variant.uvLock && weight == variant.weight;
        }
    }

    public int hashCode()
    {
        int i = modelLocation.hashCode();
        i = 31 * i + rotation.hashCode();
        i = 31 * i + Boolean.valueOf(uvLock).hashCode();
        i = 31 * i + weight;
        return i;
    }

    public static class Deserializer implements JsonDeserializer<Variant>
    {
        public Variant deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_) throws JsonParseException
        {
            JsonObject jsonobject = p_deserialize_1_.getAsJsonObject();
            String s = getStringModel(jsonobject);
            ModelRotation modelrotation = parseModelRotation(jsonobject);
            boolean flag = parseUvLock(jsonobject);
            int i = parseWeight(jsonobject);
            return new Variant(getResourceLocationBlock(s), modelrotation, flag, i);
        }

        private Namespaced getResourceLocationBlock(String p_188041_1_)
        {
            Namespaced resourcelocation = new Namespaced(p_188041_1_);
            resourcelocation = new Namespaced(resourcelocation.getNamespace(), "block/" + resourcelocation.getPath());
            return resourcelocation;
        }

        private boolean parseUvLock(JsonObject json)
        {
            return JsonUtils.getBoolean(json, "uvlock", false);
        }

        protected ModelRotation parseModelRotation(JsonObject json)
        {
            int i = JsonUtils.getInt(json, "x", 0);
            int j = JsonUtils.getInt(json, "y", 0);
            ModelRotation modelrotation = ModelRotation.getModelRotation(i, j);

            if (modelrotation == null)
            {
                throw new JsonParseException("Invalid BlockModelRotation x: " + i + ", y: " + j);
            }
            else
            {
                return modelrotation;
            }
        }

        protected String getStringModel(JsonObject json)
        {
            return JsonUtils.getString(json, "model");
        }

        protected int parseWeight(JsonObject json)
        {
            int i = JsonUtils.getInt(json, "weight", 1);

            if (i < 1)
            {
                throw new JsonParseException("Invalid weight " + i + " found, expected integer >= 1");
            }
            else
            {
                return i;
            }
        }
    }
}
