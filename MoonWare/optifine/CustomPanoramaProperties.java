package optifine;

import java.util.Properties;
import net.minecraft.util.Namespaced;

public class CustomPanoramaProperties
{
    private String path;
    private Namespaced[] panoramaLocations;
    private int weight = 1;
    private int blur1 = 64;
    private int blur2 = 3;
    private int blur3 = 3;
    private int overlay1Top = -2130706433;
    private int overlay1Bottom = 16777215;
    private int overlay2Top;
    private int overlay2Bottom = Integer.MIN_VALUE;

    public CustomPanoramaProperties(String p_i34_1_, Properties p_i34_2_)
    {
        ConnectedParser connectedparser = new ConnectedParser("CustomPanorama");
        path = p_i34_1_;
        panoramaLocations = new Namespaced[6];

        for (int i = 0; i < panoramaLocations.length; ++i)
        {
            panoramaLocations[i] = new Namespaced(p_i34_1_ + "/panorama_" + i + ".png");
        }

        weight = connectedparser.parseInt(p_i34_2_.getProperty("weight"), 1);
        blur1 = connectedparser.parseInt(p_i34_2_.getProperty("blur1"), 64);
        blur2 = connectedparser.parseInt(p_i34_2_.getProperty("blur2"), 3);
        blur3 = connectedparser.parseInt(p_i34_2_.getProperty("blur3"), 3);
        overlay1Top = ConnectedParser.parseColor4(p_i34_2_.getProperty("overlay1.top"), -2130706433);
        overlay1Bottom = ConnectedParser.parseColor4(p_i34_2_.getProperty("overlay1.bottom"), 16777215);
        overlay2Top = ConnectedParser.parseColor4(p_i34_2_.getProperty("overlay2.top"), 0);
        overlay2Bottom = ConnectedParser.parseColor4(p_i34_2_.getProperty("overlay2.bottom"), Integer.MIN_VALUE);
    }

    public Namespaced[] getPanoramaLocations()
    {
        return panoramaLocations;
    }

    public int getWeight()
    {
        return weight;
    }

    public int getBlur1()
    {
        return blur1;
    }

    public int getBlur2()
    {
        return blur2;
    }

    public int getBlur3()
    {
        return blur3;
    }

    public int getOverlay1Top()
    {
        return overlay1Top;
    }

    public int getOverlay1Bottom()
    {
        return overlay1Bottom;
    }

    public int getOverlay2Top()
    {
        return overlay2Top;
    }

    public int getOverlay2Bottom()
    {
        return overlay2Bottom;
    }

    public String toString()
    {
        return "" + path + ", weight: " + weight + ", blur: " + blur1 + " " + blur2 + " " + blur3 + ", overlay: " + overlay1Top + " " + overlay1Bottom + " " + overlay2Top + " " + overlay2Bottom;
    }
}
