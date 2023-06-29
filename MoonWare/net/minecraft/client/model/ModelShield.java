package net.minecraft.client.model;

public class ModelShield extends ModelBase
{
    public ModelRenderer plate;
    public ModelRenderer handle;

    public ModelShield()
    {
        textureWidth = 64;
        textureHeight = 64;
        plate = new ModelRenderer(this, 0, 0);
        plate.addBox(-6.0F, -11.0F, -2.0F, 12, 22, 1, 0.0F);
        handle = new ModelRenderer(this, 26, 0);
        handle.addBox(-1.0F, -3.0F, -1.0F, 2, 6, 6, 0.0F);
    }

    public void render()
    {
        plate.render(0.0625F);
        handle.render(0.0625F);
    }
}
