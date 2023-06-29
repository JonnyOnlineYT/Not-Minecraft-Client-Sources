package net.optifine.entity.model.anim;

public class Token
{
    private EnumTokenType type;
    private String text;

    public Token(EnumTokenType type, String text)
    {
        this.type = type;
        this.text = text;
    }

    public EnumTokenType getType()
    {
        return type;
    }

    public String getText()
    {
        return text;
    }

    public String toString()
    {
        return text;
    }
}
