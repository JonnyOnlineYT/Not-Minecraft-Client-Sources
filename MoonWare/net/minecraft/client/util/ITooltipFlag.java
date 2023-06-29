package net.minecraft.client.util;

public interface ITooltipFlag
{
    boolean func_194127_a();

    enum TooltipFlags implements ITooltipFlag
    {
        NORMAL(false),
        ADVANCED(true);

        final boolean field_194131_c;

        TooltipFlags(boolean p_i47611_3_)
        {
            field_194131_c = p_i47611_3_;
        }

        public boolean func_194127_a()
        {
            return field_194131_c;
        }
    }
}
