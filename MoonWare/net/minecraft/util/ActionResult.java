package net.minecraft.util;

public class ActionResult<T>
{
    private final EnumActionResult type;
    private final T result;

    public ActionResult(EnumActionResult typeIn, T resultIn)
    {
        type = typeIn;
        result = resultIn;
    }

    public EnumActionResult getType()
    {
        return type;
    }

    public T getResult()
    {
        return result;
    }
}
