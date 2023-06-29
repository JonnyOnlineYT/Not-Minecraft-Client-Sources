package net.minecraft.util.datafix.fixes;

import com.google.gson.JsonParseException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.StringUtils;
import net.minecraft.util.datafix.IFixableData;
import net.minecraft.util.text.Component;
import net.minecraft.util.text.TextComponent;

public class BookPagesStrictJSON implements IFixableData
{
    public int getFixVersion()
    {
        return 165;
    }

    public NBTTagCompound fixTagCompound(NBTTagCompound compound)
    {
        if ("minecraft:written_book".equals(compound.getString("id")))
        {
            NBTTagCompound nbttagcompound = compound.getCompoundTag("tag");

            if (nbttagcompound.hasKey("pages", 9))
            {
                NBTTagList nbttaglist = nbttagcompound.getTagList("pages", 8);

                for (int i = 0; i < nbttaglist.tagCount(); ++i)
                {
                    String s = nbttaglist.getStringTagAt(i);
                    Component itextcomponent = null;

                    if (!"null".equals(s) && !StringUtils.isNullOrEmpty(s))
                    {
                        if (s.charAt(0) == '"' && s.charAt(s.length() - 1) == '"' || s.charAt(0) == '{' && s.charAt(s.length() - 1) == '}')
                        {
                            try
                            {
                                itextcomponent = JsonUtils.gsonDeserialize(SignStrictJSON.GSON_INSTANCE, s, Component.class, true);

                                if (itextcomponent == null)
                                {
                                    itextcomponent = new TextComponent("");
                                }
                            }
                            catch (JsonParseException var10)
                            {
                            }

                            if (itextcomponent == null)
                            {
                                try
                                {
                                    itextcomponent = Component.Serializer.jsonToComponent(s);
                                }
                                catch (JsonParseException var9)
                                {
                                }
                            }

                            if (itextcomponent == null)
                            {
                                try
                                {
                                    itextcomponent = Component.Serializer.fromJsonLenient(s);
                                }
                                catch (JsonParseException var8)
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

                    nbttaglist.set(i, new NBTTagString(Component.Serializer.componentToJson(itextcomponent)));
                }

                nbttagcompound.setTag("pages", nbttaglist);
            }
        }

        return compound;
    }
}
