package net.optifine.entity.model.anim;

import optifine.Config;

public class ModelVariableUpdater
{
    private String modelVariableName;
    private String expressionText;
    private ModelVariable modelVariable;
    private IExpression expression;

    public boolean initialize(IModelResolver mr)
    {
        modelVariable = mr.getModelVariable(modelVariableName);

        if (modelVariable == null)
        {
            Config.warn("Model variable not found: " + modelVariableName);
            return false;
        }
        else
        {
            try
            {
                ExpressionParser expressionparser = new ExpressionParser(mr);
                expression = expressionparser.parse(expressionText);
                return true;
            }
            catch (ParseException parseexception)
            {
                Config.warn("Error parsing expression: " + expressionText);
                Config.warn(parseexception.getClass().getName() + ": " + parseexception.getMessage());
                return false;
            }
        }
    }

    public ModelVariableUpdater(String modelVariableName, String expressionText)
    {
        this.modelVariableName = modelVariableName;
        this.expressionText = expressionText;
    }

    public void update()
    {
        float f = expression.eval();
        modelVariable.setValue(f);
    }
}
