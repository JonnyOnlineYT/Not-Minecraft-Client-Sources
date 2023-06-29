package net.minecraft.client.resources.data;

import net.minecraft.util.text.Component;

public class PackMetadataSection implements IMetadataSection
{
    private final Component packDescription;
    private final int packFormat;

    public PackMetadataSection(Component packDescriptionIn, int packFormatIn)
    {
        packDescription = packDescriptionIn;
        packFormat = packFormatIn;
    }

    public Component getPackDescription()
    {
        return packDescription;
    }

    public int getPackFormat()
    {
        return packFormat;
    }
}
