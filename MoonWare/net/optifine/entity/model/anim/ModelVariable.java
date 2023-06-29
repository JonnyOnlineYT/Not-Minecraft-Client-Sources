package net.optifine.entity.model.anim;

import net.minecraft.client.model.ModelRenderer;

public class ModelVariable implements IExpression
{
    private String name;
    private ModelRenderer modelRenderer;
    private EnumModelVariable enumModelVariable;

    public ModelVariable(String name, ModelRenderer modelRenderer, EnumModelVariable enumModelVariable)
    {
        this.name = name;
        this.modelRenderer = modelRenderer;
        this.enumModelVariable = enumModelVariable;
    }

    public float eval()
    {
        return getValue();
    }

    public float getValue()
    {
        return enumModelVariable.getFloat(modelRenderer);
    }

    public void setValue(float value)
    {
        enumModelVariable.setFloat(modelRenderer, value);
    }

    public String toString()
    {
        return name;
    }
}
