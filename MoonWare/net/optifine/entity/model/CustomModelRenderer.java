package net.optifine.entity.model;

import net.minecraft.client.model.ModelRenderer;
import net.optifine.entity.model.anim.ModelUpdater;

public class CustomModelRenderer
{
    private String modelPart;
    private boolean attach;
    private ModelRenderer modelRenderer;
    private ModelUpdater modelUpdater;

    public CustomModelRenderer(String modelPart, boolean attach, ModelRenderer modelRenderer, ModelUpdater modelUpdater)
    {
        this.modelPart = modelPart;
        this.attach = attach;
        this.modelRenderer = modelRenderer;
        this.modelUpdater = modelUpdater;
    }

    public ModelRenderer getModelRenderer()
    {
        return modelRenderer;
    }

    public String getModelPart()
    {
        return modelPart;
    }

    public boolean isAttach()
    {
        return attach;
    }

    public ModelUpdater getModelUpdater()
    {
        return modelUpdater;
    }
}
