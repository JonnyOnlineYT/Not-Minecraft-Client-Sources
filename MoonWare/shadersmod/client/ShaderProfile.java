package shadersmod.client;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ShaderProfile
{
    private String name;
    private Map<String, String> mapOptionValues = new HashMap<String, String>();
    private Set<String> disabledPrograms = new HashSet<String>();

    public ShaderProfile(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public void addOptionValue(String option, String value)
    {
        mapOptionValues.put(option, value);
    }

    public void addOptionValues(ShaderProfile prof)
    {
        if (prof != null)
        {
            mapOptionValues.putAll(prof.mapOptionValues);
        }
    }

    public void applyOptionValues(ShaderOption[] options)
    {
        for (int i = 0; i < options.length; ++i)
        {
            ShaderOption shaderoption = options[i];
            String s = shaderoption.getName();
            String s1 = mapOptionValues.get(s);

            if (s1 != null)
            {
                shaderoption.setValue(s1);
            }
        }
    }

    public String[] getOptions()
    {
        Set<String> set = mapOptionValues.keySet();
        String[] astring = set.toArray(new String[set.size()]);
        return astring;
    }

    public String getValue(String key)
    {
        return mapOptionValues.get(key);
    }

    public void addDisabledProgram(String program)
    {
        disabledPrograms.add(program);
    }

    public Collection<String> getDisabledPrograms()
    {
        return new HashSet<String>(disabledPrograms);
    }

    public void addDisabledPrograms(Collection<String> programs)
    {
        disabledPrograms.addAll(programs);
    }

    public boolean isProgramDisabled(String program)
    {
        return disabledPrograms.contains(program);
    }
}
