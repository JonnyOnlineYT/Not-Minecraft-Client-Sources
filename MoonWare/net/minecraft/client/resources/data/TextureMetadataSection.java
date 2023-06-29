package net.minecraft.client.resources.data;

public class TextureMetadataSection implements IMetadataSection
{
    private final boolean textureBlur;
    private final boolean textureClamp;

    public TextureMetadataSection(boolean textureBlurIn, boolean textureClampIn)
    {
        textureBlur = textureBlurIn;
        textureClamp = textureClampIn;
    }

    public boolean getTextureBlur()
    {
        return textureBlur;
    }

    public boolean getTextureClamp()
    {
        return textureClamp;
    }
}
