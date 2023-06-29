package net.optifine.entity.model;

import net.minecraft.util.Namespaced;

public class CustomEntityRenderer
{
    private String name;
    private String basePath;
    private Namespaced textureLocation;
    private CustomModelRenderer[] customModelRenderers;
    private float shadowSize;

    public CustomEntityRenderer(String name, String basePath, Namespaced textureLocation, CustomModelRenderer[] customModelRenderers, float shadowSize)
    {
        this.name = name;
        this.basePath = basePath;
        this.textureLocation = textureLocation;
        this.customModelRenderers = customModelRenderers;
        this.shadowSize = shadowSize;
    }

    public String getName()
    {
        return name;
    }

    public String getBasePath()
    {
        return basePath;
    }

    public Namespaced getTextureLocation()
    {
        return textureLocation;
    }

    public CustomModelRenderer[] getCustomModelRenderers()
    {
        return customModelRenderers;
    }

    public float getShadowSize()
    {
        return shadowSize;
    }
}
