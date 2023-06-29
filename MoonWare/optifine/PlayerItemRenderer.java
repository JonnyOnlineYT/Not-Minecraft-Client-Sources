package optifine;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;

public class PlayerItemRenderer
{
    private int attachTo;
    private ModelRenderer modelRenderer;

    public PlayerItemRenderer(int p_i74_1_, ModelRenderer p_i74_2_)
    {
        attachTo = p_i74_1_;
        modelRenderer = p_i74_2_;
    }

    public ModelRenderer getModelRenderer()
    {
        return modelRenderer;
    }

    public void render(ModelBiped p_render_1_, float p_render_2_)
    {
        ModelRenderer modelrenderer = PlayerItemModel.getAttachModel(p_render_1_, attachTo);

        if (modelrenderer != null)
        {
            modelrenderer.postRender(p_render_2_);
        }

        modelRenderer.render(p_render_2_);
    }
}
