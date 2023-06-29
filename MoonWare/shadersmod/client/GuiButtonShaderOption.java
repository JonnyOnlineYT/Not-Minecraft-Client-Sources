package shadersmod.client;

import net.minecraft.client.gui.widget.ButtonWidget;

public class GuiButtonShaderOption extends ButtonWidget
{
    private ShaderOption shaderOption;

    public GuiButtonShaderOption(int buttonId, int x, int y, int widthIn, int heightIn, ShaderOption shaderOption, String text)
    {
        super(buttonId, x, y, widthIn, heightIn, text);
        this.shaderOption = shaderOption;
    }

    public ShaderOption getShaderOption()
    {
        return shaderOption;
    }

    public void valueChanged()
    {
    }
}
