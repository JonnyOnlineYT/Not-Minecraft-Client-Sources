package net.minecraft.client.renderer.block.model;

import com.google.common.collect.Maps;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.Namespaced;
import net.minecraft.world.World;

public class ItemOverride
{
    private final Namespaced location;
    private final Map<Namespaced, Float> mapResourceValues;

    public ItemOverride(Namespaced locationIn, Map<Namespaced, Float> propertyValues)
    {
        location = locationIn;
        mapResourceValues = propertyValues;
    }

    /**
     * Get the location of the target model
     */
    public Namespaced getLocation()
    {
        return location;
    }

    boolean matchesItemStack(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase livingEntity)
    {
        Item item = stack.getItem();

        for (Map.Entry<Namespaced, Float> entry : mapResourceValues.entrySet())
        {
            IItemPropertyGetter iitempropertygetter = item.getPropertyGetter(entry.getKey());

            if (iitempropertygetter == null || iitempropertygetter.apply(stack, worldIn, livingEntity) < entry.getValue().floatValue())
            {
                return false;
            }
        }

        return true;
    }

    static class Deserializer implements JsonDeserializer<ItemOverride>
    {
        public ItemOverride deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_) throws JsonParseException
        {
            JsonObject jsonobject = p_deserialize_1_.getAsJsonObject();
            Namespaced resourcelocation = new Namespaced(JsonUtils.getString(jsonobject, "model"));
            Map<Namespaced, Float> map = makeMapResourceValues(jsonobject);
            return new ItemOverride(resourcelocation, map);
        }

        protected Map<Namespaced, Float> makeMapResourceValues(JsonObject p_188025_1_)
        {
            Map<Namespaced, Float> map = Maps.newLinkedHashMap();
            JsonObject jsonobject = JsonUtils.getJsonObject(p_188025_1_, "predicate");

            for (Map.Entry<String, JsonElement> entry : jsonobject.entrySet())
            {
                map.put(new Namespaced(entry.getKey()), Float.valueOf(JsonUtils.getFloat(entry.getValue(), entry.getKey())));
            }

            return map;
        }
    }
}
