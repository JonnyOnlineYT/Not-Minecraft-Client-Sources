package net.minecraft.util.datafix.fixes;

import com.google.gson.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.StringUtils;
import net.minecraft.util.datafix.IFixableData;
import net.minecraft.util.text.Component;
import net.minecraft.util.text.TextComponent;

import java.lang.reflect.Type;

public class SignStrictJSON implements IFixableData
{
    public static final Gson GSON_INSTANCE = (new GsonBuilder()).registerTypeAdapter(Component.class, new JsonDeserializer<Component>()
    {
        public Component deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_) throws JsonParseException
        {
            if (p_deserialize_1_.isJsonPrimitive())
            {
                return new TextComponent(p_deserialize_1_.getAsString());
            }
            else if (p_deserialize_1_.isJsonArray())
            {
                JsonArray jsonarray = p_deserialize_1_.getAsJsonArray();
                Component itextcomponent = null;

                for (JsonElement jsonelement : jsonarray)
                {
                    Component itextcomponent1 = deserialize(jsonelement, jsonelement.getClass(), p_deserialize_3_);

                    if (itextcomponent == null)
                    {
                        itextcomponent = itextcomponent1;
                    }
                    else
                    {
                        itextcomponent.append(itextcomponent1);
                    }
                }

                return itextcomponent;
            }
            else
            {
                throw new JsonParseException("Don't know how to turn " + p_deserialize_1_ + " into a Component");
            }
        }
    }).create();

    public int getFixVersion()
    {
        return 101;
    }

    public NBTTagCompound fixTagCompound(NBTTagCompound compound)
    {
        if ("Sign".equals(compound.getString("id")))
        {
            updateLine(compound, "Text1");
            updateLine(compound, "Text2");
            updateLine(compound, "Text3");
            updateLine(compound, "Text4");
        }

        return compound;
    }

    private void updateLine(NBTTagCompound compound, String key)
    {
        String s = compound.getString(key);
        Component itextcomponent = null;

        if (!"null".equals(s) && !StringUtils.isNullOrEmpty(s))
        {
            if (s.charAt(0) == '"' && s.charAt(s.length() - 1) == '"' || s.charAt(0) == '{' && s.charAt(s.length() - 1) == '}')
            {
                try
                {
                    itextcomponent = JsonUtils.gsonDeserialize(GSON_INSTANCE, s, Component.class, true);

                    if (itextcomponent == null)
                    {
                        itextcomponent = new TextComponent("");
                    }
                }
                catch (JsonParseException var8)
                {
                }

                if (itextcomponent == null)
                {
                    try
                    {
                        itextcomponent = Component.Serializer.jsonToComponent(s);
                    }
                    catch (JsonParseException var7)
                    {
                    }
                }

                if (itextcomponent == null)
                {
                    try
                    {
                        itextcomponent = Component.Serializer.fromJsonLenient(s);
                    }
                    catch (JsonParseException var6)
                    {
                    }
                }

                if (itextcomponent == null)
                {
                    itextcomponent = new TextComponent(s);
                }
            }
            else
            {
                itextcomponent = new TextComponent(s);
            }
        }
        else
        {
            itextcomponent = new TextComponent("");
        }

        compound.setString(key, Component.Serializer.componentToJson(itextcomponent));
    }
}
