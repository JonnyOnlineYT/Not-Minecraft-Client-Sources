package net.optifine.entity.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelSlime;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSlime;
import net.minecraft.entity.monster.EntitySlime;
import net.optifine.entity.model.anim.RefUtils;
import optifine.ReflectorClass;
import optifine.ReflectorFields;

public class ModelAdapterSlime extends ModelAdapter
{
    public ModelAdapterSlime()
    {
        super(EntitySlime.class, "slime", 0.25F);
    }

    public ModelBase makeModel()
    {
        return new ModelSlime(16);
    }

    public static ReflectorClass ModelSlime = new ReflectorClass(ModelSlime.class);
    public static ReflectorFields ModelSlime_ModelRenderers = new ReflectorFields(ModelSlime, ModelRenderer.class, 4);

    public ModelRenderer getModelRenderer(ModelBase model, String modelPart)
    {
        if (!(model instanceof ModelSlime))
        {
            return null;
        }
        else
        {
            ModelSlime modelslime = (ModelSlime)model;

            if (modelPart.equals("body"))
            {
                return (ModelRenderer) RefUtils.getFieldValue(modelslime, ModelSlime_ModelRenderers, 0);
            }
            else if (modelPart.equals("left_eye"))
            {
                return (ModelRenderer)RefUtils.getFieldValue(modelslime, ModelSlime_ModelRenderers, 1);
            }
            else if (modelPart.equals("right_eye"))
            {
                return (ModelRenderer)RefUtils.getFieldValue(modelslime, ModelSlime_ModelRenderers, 2);
            }
            else
            {
                return modelPart.equals("mouth") ? (ModelRenderer)RefUtils.getFieldValue(modelslime, ModelSlime_ModelRenderers, 3) : null;
            }
        }
    }

    public IEntityRenderer makeEntityRender(ModelBase modelBase, float shadowSize)
    {
        RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        RenderSlime renderslime = new RenderSlime(rendermanager);
        renderslime.mainModel = modelBase;
        renderslime.shadowSize = shadowSize;
        return renderslime;
    }
}
