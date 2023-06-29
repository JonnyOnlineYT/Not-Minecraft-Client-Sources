package net.optifine.entity.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBlaze;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.RenderBlaze;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.monster.EntityBlaze;
import net.optifine.entity.model.anim.RefUtils;
import optifine.Config;
import optifine.ReflectorClass;
import optifine.ReflectorField;

public class ModelAdapterBlaze extends ModelAdapter
{
    public ModelAdapterBlaze()
    {
        super(EntityBlaze.class, "blaze", 0.5F);
    }

    public ModelBase makeModel()
    {
        return new ModelBlaze();
    }

    public static ReflectorClass ModelBlaze = new ReflectorClass(ModelBlaze.class);
    public static ReflectorField ModelBlaze_blazeHead = new ReflectorField(ModelBlaze, ModelRenderer.class);
    public static ReflectorField ModelBlaze_blazeSticks = new ReflectorField(ModelBlaze, ModelRenderer[].class);

    public ModelRenderer getModelRenderer(ModelBase model, String modelPart)
    {
        if (!(model instanceof ModelBlaze))
        {
            return null;
        }
        else
        {
            ModelBlaze modelblaze = (ModelBlaze)model;

            if (modelPart.equals("head"))
            {
                return (ModelRenderer) RefUtils.getFieldValue(modelblaze, ModelBlaze_blazeHead);
            }
            else
            {
                String s = "stick";

                if (modelPart.startsWith(s))
                {
                    ModelRenderer[] amodelrenderer = (ModelRenderer[])RefUtils.getFieldValue(modelblaze, ModelBlaze_blazeSticks);

                    if (amodelrenderer == null)
                    {
                        return null;
                    }
                    else
                    {
                        String s1 = modelPart.substring(s.length());
                        int i = Config.parseInt(s1, -1);
                        --i;
                        return i >= 0 && i < amodelrenderer.length ? amodelrenderer[i] : null;
                    }
                }
                else
                {
                    return null;
                }
            }
        }
    }

    public IEntityRenderer makeEntityRender(ModelBase modelBase, float shadowSize)
    {
        RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        RenderBlaze renderblaze = new RenderBlaze(rendermanager);
        renderblaze.mainModel = modelBase;
        renderblaze.shadowSize = shadowSize;
        return renderblaze;
    }
}
