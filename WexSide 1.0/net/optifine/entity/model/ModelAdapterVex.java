package net.optifine.entity.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelVex;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderVex;
import net.minecraft.entity.monster.EntityVex;
import net.optifine.entity.model.anim.RefUtils;
import optifine.ReflectorClass;
import optifine.ReflectorField;

public class ModelAdapterVex extends ModelAdapterBiped
{
    public ModelAdapterVex()
    {
        super(EntityVex.class, "vex", 0.3F);
    }

    public static ReflectorClass ModelVex = new ReflectorClass(ModelVex.class);
    public static ReflectorField ModelVex_leftWing = new ReflectorField(ModelVex, ModelRenderer.class, 0);
    public static ReflectorField ModelVex_rightWing = new ReflectorField(ModelVex, ModelRenderer.class, 1);

    public ModelRenderer getModelRenderer(ModelBase model, String modelPart)
    {
        if (!(model instanceof ModelVex))
        {
            return null;
        }
        else
        {
            ModelRenderer modelrenderer = super.getModelRenderer(model, modelPart);

            if (modelrenderer != null)
            {
                return modelrenderer;
            }
            else
            {
                ModelVex modelvex = (ModelVex)model;

                if (modelPart.equals("left_wing"))
                {
                    return (ModelRenderer) RefUtils.getFieldValue(modelvex, ModelVex_leftWing);
                }
                else
                {
                    return modelPart.equals("right_wing") ? (ModelRenderer)RefUtils.getFieldValue(modelvex, ModelVex_rightWing) : null;
                }
            }
        }
    }

    public ModelBase makeModel()
    {
        return new ModelVex();
    }

    public IEntityRenderer makeEntityRender(ModelBase modelBase, float shadowSize)
    {
        RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        RenderVex rendervex = new RenderVex(rendermanager);
        rendervex.mainModel = modelBase;
        rendervex.shadowSize = shadowSize;
        return rendervex;
    }
}
