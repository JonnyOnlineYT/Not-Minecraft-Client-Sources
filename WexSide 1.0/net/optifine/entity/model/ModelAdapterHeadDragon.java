package net.optifine.entity.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelDragonHead;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySkullRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntitySkull;
import net.optifine.entity.model.anim.RefUtils;
import optifine.Config;
import optifine.ReflectorClass;
import optifine.ReflectorField;

public class ModelAdapterHeadDragon extends ModelAdapter
{
    public ModelAdapterHeadDragon()
    {
        super(TileEntitySkull.class, "head_dragon", 0.0F);
    }

    public ModelBase makeModel()
    {
        return new ModelDragonHead(0.0F);
    }

    public static ReflectorClass ModelDragonHead = new ReflectorClass(ModelDragonHead.class);
    public static ReflectorField ModelDragonHead_head = new ReflectorField(ModelDragonHead, ModelRenderer.class, 0);
    public static ReflectorField ModelDragonHead_jaw = new ReflectorField(ModelDragonHead, ModelRenderer.class, 1);

    public ModelRenderer getModelRenderer(ModelBase model, String modelPart)
    {
        if (!(model instanceof ModelDragonHead))
        {
            return null;
        }
        else
        {
            ModelDragonHead modeldragonhead = (ModelDragonHead)model;

            if (modelPart.equals("head"))
            {
                return (ModelRenderer) RefUtils.getFieldValue(modeldragonhead, ModelDragonHead_head);
            }
            else
            {
                return modelPart.equals("jaw") ? (ModelRenderer)RefUtils.getFieldValue(modeldragonhead, ModelDragonHead_jaw) : null;
            }
        }
    }

    public IEntityRenderer makeEntityRender(ModelBase modelBase, float shadowSize)
    {
        TileEntityRendererDispatcher tileentityrendererdispatcher = TileEntityRendererDispatcher.instance;
        TileEntitySpecialRenderer tileentityspecialrenderer = tileentityrendererdispatcher.getSpecialRendererByClass(TileEntitySkull.class);

        if (!(tileentityspecialrenderer instanceof TileEntitySkullRenderer))
        {
            return null;
        }
        else
        {
            if (tileentityspecialrenderer.getEntityClass() == null)
            {
                tileentityspecialrenderer = new TileEntitySkullRenderer();
                tileentityspecialrenderer.setRendererDispatcher(tileentityrendererdispatcher);
            }

            Config.warn("Field not found: TileEntitySkullRenderer.dragonHead");
            return null;
        }
    }
}
