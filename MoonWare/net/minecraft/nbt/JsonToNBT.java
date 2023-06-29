package net.minecraft.nbt;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.regex.Pattern;

public class JsonToNBT
{
    private static final Pattern field_193615_a = Pattern.compile("[-+]?(?:[0-9]+[.]|[0-9]*[.][0-9]+)(?:e[-+]?[0-9]+)?", 2);
    private static final Pattern field_193616_b = Pattern.compile("[-+]?(?:[0-9]+[.]?|[0-9]*[.][0-9]+)(?:e[-+]?[0-9]+)?d", 2);
    private static final Pattern field_193617_c = Pattern.compile("[-+]?(?:[0-9]+[.]?|[0-9]*[.][0-9]+)(?:e[-+]?[0-9]+)?f", 2);
    private static final Pattern field_193618_d = Pattern.compile("[-+]?(?:0|[1-9][0-9]*)b", 2);
    private static final Pattern field_193619_e = Pattern.compile("[-+]?(?:0|[1-9][0-9]*)l", 2);
    private static final Pattern field_193620_f = Pattern.compile("[-+]?(?:0|[1-9][0-9]*)s", 2);
    private static final Pattern field_193621_g = Pattern.compile("[-+]?(?:0|[1-9][0-9]*)");
    private final String field_193622_h;
    private int field_193623_i;

    public static NBTTagCompound getTagFromJson(String jsonString) throws NBTException
    {
        return (new JsonToNBT(jsonString)).func_193609_a();
    }

    @VisibleForTesting
    NBTTagCompound func_193609_a() throws NBTException
    {
        NBTTagCompound nbttagcompound = func_193593_f();
        func_193607_l();

        if (func_193612_g())
        {
            ++field_193623_i;
            throw func_193602_b("Trailing data found");
        }
        else
        {
            return nbttagcompound;
        }
    }

    @VisibleForTesting
    JsonToNBT(String p_i47522_1_)
    {
        field_193622_h = p_i47522_1_;
    }

    protected String func_193601_b() throws NBTException
    {
        func_193607_l();

        if (!func_193612_g())
        {
            throw func_193602_b("Expected key");
        }
        else
        {
            return func_193598_n() == '"' ? func_193595_h() : func_193614_i();
        }
    }

    private NBTException func_193602_b(String p_193602_1_)
    {
        return new NBTException(p_193602_1_, field_193622_h, field_193623_i);
    }

    protected NBTBase func_193611_c() throws NBTException
    {
        func_193607_l();

        if (func_193598_n() == '"')
        {
            return new NBTTagString(func_193595_h());
        }
        else
        {
            String s = func_193614_i();

            if (s.isEmpty())
            {
                throw func_193602_b("Expected value");
            }
            else
            {
                return func_193596_c(s);
            }
        }
    }

    private NBTBase func_193596_c(String p_193596_1_)
    {
        try
        {
            if (field_193617_c.matcher(p_193596_1_).matches())
            {
                return new NBTTagFloat(Float.parseFloat(p_193596_1_.substring(0, p_193596_1_.length() - 1)));
            }

            if (field_193618_d.matcher(p_193596_1_).matches())
            {
                return new NBTTagByte(Byte.parseByte(p_193596_1_.substring(0, p_193596_1_.length() - 1)));
            }

            if (field_193619_e.matcher(p_193596_1_).matches())
            {
                return new NBTTagLong(Long.parseLong(p_193596_1_.substring(0, p_193596_1_.length() - 1)));
            }

            if (field_193620_f.matcher(p_193596_1_).matches())
            {
                return new NBTTagShort(Short.parseShort(p_193596_1_.substring(0, p_193596_1_.length() - 1)));
            }

            if (field_193621_g.matcher(p_193596_1_).matches())
            {
                return new NBTTagInt(Integer.parseInt(p_193596_1_));
            }

            if (field_193616_b.matcher(p_193596_1_).matches())
            {
                return new NBTTagDouble(Double.parseDouble(p_193596_1_.substring(0, p_193596_1_.length() - 1)));
            }

            if (field_193615_a.matcher(p_193596_1_).matches())
            {
                return new NBTTagDouble(Double.parseDouble(p_193596_1_));
            }

            if ("true".equalsIgnoreCase(p_193596_1_))
            {
                return new NBTTagByte((byte)1);
            }

            if ("false".equalsIgnoreCase(p_193596_1_))
            {
                return new NBTTagByte((byte)0);
            }
        }
        catch (NumberFormatException var3)
        {
        }

        return new NBTTagString(p_193596_1_);
    }

    private String func_193595_h() throws NBTException
    {
        int i = ++field_193623_i;
        StringBuilder stringbuilder = null;
        boolean flag = false;

        while (func_193612_g())
        {
            char c0 = func_193594_o();

            if (flag)
            {
                if (c0 != '\\' && c0 != '"')
                {
                    throw func_193602_b("Invalid escape of '" + c0 + "'");
                }

                flag = false;
            }
            else
            {
                if (c0 == '\\')
                {
                    flag = true;

                    if (stringbuilder == null)
                    {
                        stringbuilder = new StringBuilder(field_193622_h.substring(i, field_193623_i - 1));
                    }

                    continue;
                }

                if (c0 == '"')
                {
                    return stringbuilder == null ? field_193622_h.substring(i, field_193623_i - 1) : stringbuilder.toString();
                }
            }

            if (stringbuilder != null)
            {
                stringbuilder.append(c0);
            }
        }

        throw func_193602_b("Missing termination quote");
    }

    private String func_193614_i()
    {
        int i;

        for (i = field_193623_i; func_193612_g() && func_193599_a(func_193598_n()); ++field_193623_i)
        {
        }

        return field_193622_h.substring(i, field_193623_i);
    }

    protected NBTBase func_193610_d() throws NBTException
    {
        func_193607_l();

        if (!func_193612_g())
        {
            throw func_193602_b("Expected value");
        }
        else
        {
            char c0 = func_193598_n();

            if (c0 == '{')
            {
                return func_193593_f();
            }
            else
            {
                return c0 == '[' ? func_193605_e() : func_193611_c();
            }
        }
    }

    protected NBTBase func_193605_e() throws NBTException
    {
        return func_193608_a(2) && func_193597_b(1) != '"' && func_193597_b(2) == ';' ? func_193606_k() : func_193600_j();
    }

    protected NBTTagCompound func_193593_f() throws NBTException
    {
        func_193604_b('{');
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        func_193607_l();

        while (func_193612_g() && func_193598_n() != '}')
        {
            String s = func_193601_b();

            if (s.isEmpty())
            {
                throw func_193602_b("Expected non-empty key");
            }

            func_193604_b(':');
            nbttagcompound.setTag(s, func_193610_d());

            if (!func_193613_m())
            {
                break;
            }

            if (!func_193612_g())
            {
                throw func_193602_b("Expected key");
            }
        }

        func_193604_b('}');
        return nbttagcompound;
    }

    private NBTBase func_193600_j() throws NBTException
    {
        func_193604_b('[');
        func_193607_l();

        if (!func_193612_g())
        {
            throw func_193602_b("Expected value");
        }
        else
        {
            NBTTagList nbttaglist = new NBTTagList();
            int i = -1;

            while (func_193598_n() != ']')
            {
                NBTBase nbtbase = func_193610_d();
                int j = nbtbase.getId();

                if (i < 0)
                {
                    i = j;
                }
                else if (j != i)
                {
                    throw func_193602_b("Unable to insert " + NBTBase.func_193581_j(j) + " into ListTag of type " + NBTBase.func_193581_j(i));
                }

                nbttaglist.appendTag(nbtbase);

                if (!func_193613_m())
                {
                    break;
                }

                if (!func_193612_g())
                {
                    throw func_193602_b("Expected value");
                }
            }

            func_193604_b(']');
            return nbttaglist;
        }
    }

    private NBTBase func_193606_k() throws NBTException
    {
        func_193604_b('[');
        char c0 = func_193594_o();
        func_193594_o();
        func_193607_l();

        if (!func_193612_g())
        {
            throw func_193602_b("Expected value");
        }
        else if (c0 == 'B')
        {
            return new NBTTagByteArray(func_193603_a((byte)7, (byte)1));
        }
        else if (c0 == 'L')
        {
            return new NBTTagLongArray(func_193603_a((byte)12, (byte)4));
        }
        else if (c0 == 'I')
        {
            return new NBTTagIntArray(func_193603_a((byte)11, (byte)3));
        }
        else
        {
            throw func_193602_b("Invalid array type '" + c0 + "' found");
        }
    }

    private <T extends Number> List<T> func_193603_a(byte p_193603_1_, byte p_193603_2_) throws NBTException
    {
        List<T> list = Lists.newArrayList();

        while (true)
        {
            if (func_193598_n() != ']')
            {
                NBTBase nbtbase = func_193610_d();
                int i = nbtbase.getId();

                if (i != p_193603_2_)
                {
                    throw func_193602_b("Unable to insert " + NBTBase.func_193581_j(i) + " into " + NBTBase.func_193581_j(p_193603_1_));
                }

                if (p_193603_2_ == 1)
                {
                    list.add((T)Byte.valueOf(((NBTPrimitive)nbtbase).getByte()));
                }
                else if (p_193603_2_ == 4)
                {
                    list.add((T)Long.valueOf(((NBTPrimitive)nbtbase).getLong()));
                }
                else
                {
                    list.add((T)Integer.valueOf(((NBTPrimitive)nbtbase).getInt()));
                }

                if (func_193613_m())
                {
                    if (!func_193612_g())
                    {
                        throw func_193602_b("Expected value");
                    }

                    continue;
                }
            }

            func_193604_b(']');
            return list;
        }
    }

    private void func_193607_l()
    {
        while (func_193612_g() && Character.isWhitespace(func_193598_n()))
        {
            ++field_193623_i;
        }
    }

    private boolean func_193613_m()
    {
        func_193607_l();

        if (func_193612_g() && func_193598_n() == ',')
        {
            ++field_193623_i;
            func_193607_l();
            return true;
        }
        else
        {
            return false;
        }
    }

    private void func_193604_b(char p_193604_1_) throws NBTException
    {
        func_193607_l();
        boolean flag = func_193612_g();

        if (flag && func_193598_n() == p_193604_1_)
        {
            ++field_193623_i;
        }
        else
        {
            throw new NBTException("Expected '" + p_193604_1_ + "' but got '" + (flag ? func_193598_n() : "<EOF>") + "'", field_193622_h, field_193623_i + 1);
        }
    }

    protected boolean func_193599_a(char p_193599_1_)
    {
        return p_193599_1_ >= '0' && p_193599_1_ <= '9' || p_193599_1_ >= 'A' && p_193599_1_ <= 'Z' || p_193599_1_ >= 'a' && p_193599_1_ <= 'z' || p_193599_1_ == '_' || p_193599_1_ == '-' || p_193599_1_ == '.' || p_193599_1_ == '+';
    }

    private boolean func_193608_a(int p_193608_1_)
    {
        return field_193623_i + p_193608_1_ < field_193622_h.length();
    }

    boolean func_193612_g()
    {
        return func_193608_a(0);
    }

    private char func_193597_b(int p_193597_1_)
    {
        return field_193622_h.charAt(field_193623_i + p_193597_1_);
    }

    private char func_193598_n()
    {
        return func_193597_b(0);
    }

    private char func_193594_o()
    {
        return field_193622_h.charAt(field_193623_i++);
    }
}
