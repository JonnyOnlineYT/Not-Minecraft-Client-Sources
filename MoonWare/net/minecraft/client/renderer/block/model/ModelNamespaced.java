package net.minecraft.client.renderer.block.model;

import java.util.Locale;

import com.google.common.base.Strings;
import net.minecraft.util.Namespaced;

public class ModelNamespaced extends Namespaced
{
    private final String variant;

    protected ModelNamespaced(String... resourceName)
    {
        super(resourceName[0], resourceName[1]);
        variant = Strings.isNullOrEmpty(resourceName[2]) ? "normal" : resourceName[2].toLowerCase(Locale.ROOT);
    }

    public ModelNamespaced(String pathIn)
    {
        this(parsePathString(pathIn));
    }

    public ModelNamespaced(Namespaced location, String variantIn)
    {
        this(location.toString(), variantIn);
    }

    public ModelNamespaced(String location, String variantIn)
    {
        this(parsePathString(location + '#' + (variantIn == null ? "normal" : variantIn)));
    }

    protected static String[] parsePathString(String pathIn)
    {
        String[] astring = {null, pathIn, null};
        int i = pathIn.indexOf(35);
        String s = pathIn;

        if (i >= 0)
        {
            astring[2] = pathIn.substring(i + 1);

            if (i > 1)
            {
                s = pathIn.substring(0, i);
            }
        }
        Namespaced n = new Namespaced(s);
        System.arraycopy(new String[] {n.getNamespace(), n.getPath()}, 0, astring, 0, 2);
        return astring;
    }

    public String getVariant()
    {
        return variant;
    }

    public boolean equals(Object p_equals_1_)
    {
        if (this == p_equals_1_)
        {
            return true;
        }
        else if (p_equals_1_ instanceof ModelNamespaced && super.equals(p_equals_1_))
        {
            ModelNamespaced modelresourcelocation = (ModelNamespaced)p_equals_1_;
            return variant.equals(modelresourcelocation.variant);
        }
        else
        {
            return false;
        }
    }

    public int hashCode()
    {
        return 31 * super.hashCode() + variant.hashCode();
    }

    public String toString()
    {
        return super.toString() + '#' + variant;
    }
}
