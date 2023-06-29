package net.optifine.entity.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelSilverfish;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSilverfish;
import net.minecraft.entity.monster.EntitySilverfish;
import net.optifine.entity.model.anim.RefUtils;
import optifine.Config;
import optifine.ReflectorClass;
import optifine.ReflectorField;

public class ModelAdapterSilverfish extends ModelAdapter
{
    public ModelAdapterSilverfish()
    {
        super(EntitySilverfish.class, "silverfish", 0.3F);
    }

    public ModelBase makeModel()
    {
        return new ModelSilverfish();
    }

    public static ReflectorClass ModelSilverfish = new ReflectorClass(ModelSilverfish.class);
    public static ReflectorField ModelSilverfish_bodyParts = new ReflectorField(ModelSilverfish, ModelRenderer[].class, 0);
    public static ReflectorField ModelSilverfish_wingParts = new ReflectorField(ModelSilverfish, ModelRenderer[].class, 1);

    public ModelRenderer getModelRenderer(ModelBase model, String modelPart)
    {
        if (!(model instanceof ModelSilverfish))
        {
            return null;
        }
        else
        {
            ModelSilverfish modelsilverfish = (ModelSilverfish)model;
            String s = "body";

            if (modelPart.startsWith(s))
            {
                ModelRenderer[] amodelrenderer1 = (ModelRenderer[]) RefUtils.getFieldValue(modelsilverfish, ModelSilverfish_bodyParts);

                if (amodelrenderer1 == null)
                {
                    return null;
                }
                else
                {
                    String s3 = modelPart.substring(s.length());
                    int j = Config.parseInt(s3, -1);
                    --j;
                    return j >= 0 && j < amodelrenderer1.length ? amodelrenderer1[j] : null;
                }
            }
            else
            {
                String s1 = "wing";

                if (modelPart.startsWith(s1))
                {
                    ModelRenderer[] amodelrenderer = (ModelRenderer[])RefUtils.getFieldValue(modelsilverfish, ModelSilverfish_wingParts);

                    if (amodelrenderer == null)
                    {
                        return null;
                    }
                    else
                    {
                        String s2 = modelPart.substring(s1.length());
                        int i = Config.parseInt(s2, -1);
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
        RenderSilverfish rendersilverfish = new RenderSilverfish(rendermanager);
        rendersilverfish.mainModel = modelBase;
        rendersilverfish.shadowSize = shadowSize;
        return rendersilverfish;
    }
}
