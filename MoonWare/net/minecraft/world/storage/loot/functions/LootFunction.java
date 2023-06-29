package net.minecraft.world.storage.loot.functions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import java.util.Random;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Namespaced;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.LootCondition;

public abstract class LootFunction
{
    private final LootCondition[] conditions;

    protected LootFunction(LootCondition[] conditionsIn)
    {
        conditions = conditionsIn;
    }

    public abstract ItemStack apply(ItemStack stack, Random rand, LootContext context);

    public LootCondition[] getConditions()
    {
        return conditions;
    }

    public abstract static class Serializer<T extends LootFunction>
    {
        private final Namespaced lootTableLocation;
        private final Class<T> functionClass;

        protected Serializer(Namespaced location, Class<T> clazz)
        {
            lootTableLocation = location;
            functionClass = clazz;
        }

        public Namespaced getFunctionName()
        {
            return lootTableLocation;
        }

        public Class<T> getFunctionClass()
        {
            return functionClass;
        }

        public abstract void serialize(JsonObject object, T functionClazz, JsonSerializationContext serializationContext);

        public abstract T deserialize(JsonObject object, JsonDeserializationContext deserializationContext, LootCondition[] conditionsIn);
    }
}
