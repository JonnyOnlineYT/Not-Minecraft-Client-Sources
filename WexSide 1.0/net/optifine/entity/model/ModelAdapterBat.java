package net.optifine.entity.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBat;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.RenderBat;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.passive.EntityBat;
import optifine.ReflectorClass;
import optifine.ReflectorField;
import optifine.ReflectorFields;

import java.lang.reflect.Field;

public class ModelAdapterBat extends ModelAdapter
{
    public ModelAdapterBat()
    {
        super(EntityBat.class, "bat", 0.25F);
    }

    public ModelBase makeModel()
    {
        return new ModelBat();
    }

    public static ReflectorClass ModelBat = new ReflectorClass(ModelBat.class);
    public static ReflectorFields ModelBat_ModelRenderers = new ReflectorFields(ModelBat, ModelRenderer.class, 6);

    public static Object getFieldValue(ReflectorField p_getFieldValue_0_)
    {
        return getFieldValue((Object)null, p_getFieldValue_0_);
    }

    public static Object getFieldValue(Object p_getFieldValue_0_, ReflectorField p_getFieldValue_1_)
    {
        try
        {
            Field field = p_getFieldValue_1_.getTargetField();

            if (field == null)
            {
                return null;
            }
            else
            {
                Object object = field.get(p_getFieldValue_0_);
                return object;
            }
        }
        catch (Throwable throwable)
        {
            throwable.printStackTrace();
            return null;
        }
    }

    public static Object getFieldValue(ReflectorFields p_getFieldValue_0_, int p_getFieldValue_1_)
    {
        ReflectorField reflectorfield = p_getFieldValue_0_.getReflectorField(p_getFieldValue_1_);
        return reflectorfield == null ? null : getFieldValue(reflectorfield);
    }

    public static Object getFieldValue(Object p_getFieldValue_0_, ReflectorFields p_getFieldValue_1_, int p_getFieldValue_2_)
    {
        ReflectorField reflectorfield = p_getFieldValue_1_.getReflectorField(p_getFieldValue_2_);
        return reflectorfield == null ? null : getFieldValue(p_getFieldValue_0_, reflectorfield);
    }

    public static float getFieldValueFloat(Object p_getFieldValueFloat_0_, ReflectorField p_getFieldValueFloat_1_, float p_getFieldValueFloat_2_)
    {
        Object object = getFieldValue(p_getFieldValueFloat_0_, p_getFieldValueFloat_1_);

        if (!(object instanceof Float))
        {
            return p_getFieldValueFloat_2_;
        }
        else
        {
            Float f = (Float)object;
            return f.floatValue();
        }
    }

    public ModelRenderer getModelRenderer(ModelBase model, String modelPart)
    {
        if (!(model instanceof ModelBat))
        {
            return null;
        }
        else
        {
            ModelBat modelbat = (ModelBat)model;

            if (modelPart.equals("head"))
            {
                return (ModelRenderer)getFieldValue(modelbat, ModelBat_ModelRenderers, 0);
            }
            else if (modelPart.equals("body"))
            {
                return (ModelRenderer)getFieldValue(modelbat, ModelBat_ModelRenderers, 1);
            }
            else if (modelPart.equals("right_wing"))
            {
                return (ModelRenderer)getFieldValue(modelbat, ModelBat_ModelRenderers, 2);
            }
            else if (modelPart.equals("left_wing"))
            {
                return (ModelRenderer)getFieldValue(modelbat, ModelBat_ModelRenderers, 3);
            }
            else if (modelPart.equals("outer_right_wing"))
            {
                return (ModelRenderer)getFieldValue(modelbat, ModelBat_ModelRenderers, 4);
            }
            else
            {
                return modelPart.equals("outer_left_wing") ? (ModelRenderer)getFieldValue(modelbat, ModelBat_ModelRenderers, 5) : null;
            }
        }
    }

    public IEntityRenderer makeEntityRender(ModelBase modelBase, float shadowSize)
    {
        RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        RenderBat renderbat = new RenderBat(rendermanager);
        renderbat.mainModel = modelBase;
        renderbat.shadowSize = shadowSize;
        return renderbat;
    }
}
