package net.minecraft.world.storage;

import net.minecraft.util.math.MathHelper;

public class MapDecoration
{
    private final MapDecoration.Type field_191181_a;
    private byte x;
    private byte y;
    private byte rotation;

    public MapDecoration(MapDecoration.Type p_i47236_1_, byte p_i47236_2_, byte p_i47236_3_, byte p_i47236_4_)
    {
        field_191181_a = p_i47236_1_;
        x = p_i47236_2_;
        y = p_i47236_3_;
        rotation = p_i47236_4_;
    }

    public byte getType()
    {
        return field_191181_a.func_191163_a();
    }

    public MapDecoration.Type func_191179_b()
    {
        return field_191181_a;
    }

    public byte getX()
    {
        return x;
    }

    public byte getY()
    {
        return y;
    }

    public byte getRotation()
    {
        return rotation;
    }

    public boolean func_191180_f()
    {
        return field_191181_a.func_191160_b();
    }

    public boolean equals(Object p_equals_1_)
    {
        if (this == p_equals_1_)
        {
            return true;
        }
        else if (!(p_equals_1_ instanceof MapDecoration))
        {
            return false;
        }
        else
        {
            MapDecoration mapdecoration = (MapDecoration)p_equals_1_;

            if (field_191181_a != mapdecoration.field_191181_a)
            {
                return false;
            }
            else if (rotation != mapdecoration.rotation)
            {
                return false;
            }
            else if (x != mapdecoration.x)
            {
                return false;
            }
            else
            {
                return y == mapdecoration.y;
            }
        }
    }

    public int hashCode()
    {
        int i = field_191181_a.func_191163_a();
        i = 31 * i + x;
        i = 31 * i + y;
        i = 31 * i + rotation;
        return i;
    }

    public enum Type
    {
        PLAYER(false),
        FRAME(true),
        RED_MARKER(false),
        BLUE_MARKER(false),
        TARGET_X(true),
        TARGET_POINT(true),
        PLAYER_OFF_MAP(false),
        PLAYER_OFF_LIMITS(false),
        MANSION(true, 5393476),
        MONUMENT(true, 3830373);

        private final byte field_191175_k;
        private final boolean field_191176_l;
        private final int field_191177_m;

        Type(boolean p_i47343_3_)
        {
            this(p_i47343_3_, -1);
        }

        Type(boolean p_i47344_3_, int p_i47344_4_)
        {
            field_191175_k = (byte) ordinal();
            field_191176_l = p_i47344_3_;
            field_191177_m = p_i47344_4_;
        }

        public byte func_191163_a()
        {
            return field_191175_k;
        }

        public boolean func_191160_b()
        {
            return field_191176_l;
        }

        public boolean func_191162_c()
        {
            return field_191177_m >= 0;
        }

        public int func_191161_d()
        {
            return field_191177_m;
        }

        public static MapDecoration.Type func_191159_a(byte p_191159_0_)
        {
            return values()[MathHelper.clamp(p_191159_0_, 0, values().length - 1)];
        }
    }
}
