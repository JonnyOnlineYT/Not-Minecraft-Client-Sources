package net.minecraft.util;

public class Tuple<A, B>
{
    private A a;
    private B b;

    public Tuple(A aIn, B bIn)
    {
        a = aIn;
        b = bIn;
    }

    /**
     * Get the first Object in the Tuple
     */
    public A getFirst()
    {
        return a;
    }

    /**
     * Get the second Object in the Tuple
     */
    public B getSecond()
    {
        return b;
    }
}
