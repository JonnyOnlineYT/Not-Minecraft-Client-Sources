package optifine;

import org.lwjgl.opengl.DisplayMode;

import java.util.Comparator;

public class DisplayModeComparator implements Comparator
{
    public int compare(Object p_compare_1_, Object p_compare_2_)
    {
        DisplayMode displaymode = (DisplayMode)p_compare_1_;
        DisplayMode displaymode1 = (DisplayMode)p_compare_2_;

        if (displaymode.getWidth() != displaymode1.getWidth())
        {
            return displaymode.getWidth() - displaymode1.getWidth();
        }
        else if (displaymode.getHeight() != displaymode1.getHeight())
        {
            return displaymode.getHeight() - displaymode1.getHeight();
        }
        else if (displaymode.getBitsPerPixel() != displaymode1.getBitsPerPixel())
        {
            return displaymode.getBitsPerPixel() - displaymode1.getBitsPerPixel();
        }
        else
        {
            return displaymode.getFrequency() != displaymode1.getFrequency() ? displaymode.getFrequency() - displaymode1.getFrequency() : 0;
        }
    }
}
