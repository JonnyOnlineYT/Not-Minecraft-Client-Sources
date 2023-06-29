package net.minecraft.client.resources.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.minecraft.util.EnumTypeAdapterFactory;
import net.minecraft.util.registry.IRegistry;
import net.minecraft.util.registry.RegistrySimple;
import net.minecraft.util.text.Component;
import net.minecraft.util.text.Style;

public class MetadataSerializer
{
    private final IRegistry < String, MetadataSerializer.Registration <? extends IMetadataSection >> metadataSectionSerializerRegistry = new RegistrySimple < String, MetadataSerializer.Registration <? extends IMetadataSection >> ();
    private final GsonBuilder gsonBuilder = new GsonBuilder();

    /**
     * Cached Gson instance. Set to null when more sections are registered, and then re-created from the builder.
     */
    private Gson gson;

    public MetadataSerializer()
    {
        gsonBuilder.registerTypeHierarchyAdapter(Component.class, new Component.Serializer());
        gsonBuilder.registerTypeHierarchyAdapter(Style.class, new Style.Serializer());
        gsonBuilder.registerTypeAdapterFactory(new EnumTypeAdapterFactory());
    }

    public <T extends IMetadataSection> void registerMetadataSectionType(IMetadataSectionSerializer<T> metadataSectionSerializer, Class<T> clazz)
    {
        metadataSectionSerializerRegistry.putObject(metadataSectionSerializer.getSectionName(), new MetadataSerializer.Registration(metadataSectionSerializer, clazz));
        gsonBuilder.registerTypeAdapter(clazz, metadataSectionSerializer);
        gson = null;
    }

    public <T extends IMetadataSection> T parseMetadataSection(String sectionName, JsonObject json)
    {
        if (sectionName == null)
        {
            throw new IllegalArgumentException("Metadata section name cannot be null");
        }
        else if (!json.has(sectionName))
        {
            return null;
        }
        else if (!json.get(sectionName).isJsonObject())
        {
            throw new IllegalArgumentException("Invalid metadata for '" + sectionName + "' - expected object, found " + json.get(sectionName));
        }
        else
        {
            MetadataSerializer.Registration<?> registration = metadataSectionSerializerRegistry.getObject(sectionName);

            if (registration == null)
            {
                throw new IllegalArgumentException("Don't know how to handle metadata section '" + sectionName + "'");
            }
            else
            {
                return (T)(getGson().fromJson(json.getAsJsonObject(sectionName), registration.clazz));
            }
        }
    }

    /**
     * Returns a Gson instance with type adapters registered for metadata sections.
     */
    private Gson getGson()
    {
        if (gson == null)
        {
            gson = gsonBuilder.create();
        }

        return gson;
    }

    class Registration<T extends IMetadataSection>
    {
        final IMetadataSectionSerializer<T> section;
        final Class<T> clazz;

        private Registration(IMetadataSectionSerializer<T> metadataSectionSerializer, Class<T> clazzToRegister)
        {
            section = metadataSectionSerializer;
            clazz = clazzToRegister;
        }
    }
}
