package net.minecraft.world.storage.loot.properties;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import java.util.Random;
import net.minecraft.entity.Entity;
import net.minecraft.util.Namespaced;

public interface EntityProperty
{
    boolean testProperty(Random random, Entity entityIn);

    abstract class Serializer<T extends EntityProperty>
    {
        private final Namespaced name;
        private final Class<T> propertyClass;

        protected Serializer(Namespaced nameIn, Class<T> propertyClassIn)
        {
            name = nameIn;
            propertyClass = propertyClassIn;
        }

        public Namespaced getName()
        {
            return name;
        }

        public Class<T> getPropertyClass()
        {
            return propertyClass;
        }

        public abstract JsonElement serialize(T property, JsonSerializationContext serializationContext);

        public abstract T deserialize(JsonElement element, JsonDeserializationContext deserializationContext);
    }
}
