package shadersmod.client;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import optifine.Config;
import optifine.Lang;

import org.lwjgl.Sys;

public class GuiShaders extends Screen
{
    protected Screen parentGui;
    protected String screenTitle = "Shaders";
    private int updateTimer = -1;
    private GuiSlotShaders shaderList;
    private boolean saved;
    private static float[] QUALITY_MULTIPLIERS = {0.5F, 0.70710677F, 1.0F, 1.4142135F, 2.0F};
    private static String[] QUALITY_MULTIPLIER_NAMES = {"0.5x", "0.7x", "1x", "1.5x", "2x"};
    private static float[] HAND_DEPTH_VALUES = {0.0625F, 0.125F, 0.25F};
    private static String[] HAND_DEPTH_NAMES = {"0.5x", "1x", "2x"};
    public static final int EnumOS_UNKNOWN = 0;
    public static final int EnumOS_WINDOWS = 1;
    public static final int EnumOS_OSX = 2;
    public static final int EnumOS_SOLARIS = 3;
    public static final int EnumOS_LINUX = 4;

    public GuiShaders(Screen par1Screen, GameSettings par2GameSettings)
    {
        parentGui = par1Screen;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    public void init()
    {
        screenTitle = I18n.format("of.options.shadersTitle");

        if (Shaders.shadersConfig == null)
        {
            Shaders.loadConfig();
        }

        int i = 120;
        int j = 20;
        int k = width - i - 10;
        int l = 30;
        int i1 = 20;
        int j1 = width - i - 20;
        shaderList = new GuiSlotShaders(this, j1, height, l, height - 50, 16);
        shaderList.registerScrollButtons(7, 8);
        widgets.add(new GuiButtonEnumShaderOption(EnumShaderOption.ANTIALIASING, k, 0 * i1 + l, i, j));
        widgets.add(new GuiButtonEnumShaderOption(EnumShaderOption.NORMAL_MAP, k, 1 * i1 + l, i, j));
        widgets.add(new GuiButtonEnumShaderOption(EnumShaderOption.SPECULAR_MAP, k, 2 * i1 + l, i, j));
        widgets.add(new GuiButtonEnumShaderOption(EnumShaderOption.RENDER_RES_MUL, k, 3 * i1 + l, i, j));
        widgets.add(new GuiButtonEnumShaderOption(EnumShaderOption.SHADOW_RES_MUL, k, 4 * i1 + l, i, j));
        widgets.add(new GuiButtonEnumShaderOption(EnumShaderOption.HAND_DEPTH_MUL, k, 5 * i1 + l, i, j));
        widgets.add(new GuiButtonEnumShaderOption(EnumShaderOption.OLD_HAND_LIGHT, k, 6 * i1 + l, i, j));
        widgets.add(new GuiButtonEnumShaderOption(EnumShaderOption.OLD_LIGHTING, k, 7 * i1 + l, i, j));
        int k1 = Math.min(150, j1 / 2 - 10);
        widgets.add(new ButtonWidget(201, j1 / 4 - k1 / 2, height - 25, k1, j, Lang.get("of.options.shaders.shadersFolder")));
        widgets.add(new ButtonWidget(202, j1 / 4 * 3 - k1 / 2, height - 25, k1, j, I18n.format("gui.done")));
        widgets.add(new ButtonWidget(203, k, height - 25, i, j, Lang.get("of.options.shaders.shaderOptions")));
        updateButtons();
    }

    public void updateButtons()
    {
        boolean flag = Config.isShaders();

        for (ButtonWidget guibutton : widgets)
        {
            if (guibutton.id != 201 && guibutton.id != 202 && guibutton.id != EnumShaderOption.ANTIALIASING.ordinal())
            {
                guibutton.enabled = flag;
            }
        }
    }

    /**
     * Handles mouse input.
     */
    public void handleMouseInput()
    {
        super.handleMouseInput();
        shaderList.handleMouseInput();
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    public void actionPerformed(ButtonWidget button)
    {
        if (button.enabled)
        {
            if (button instanceof GuiButtonEnumShaderOption)
            {
                GuiButtonEnumShaderOption guibuttonenumshaderoption = (GuiButtonEnumShaderOption)button;

                switch (guibuttonenumshaderoption.getEnumShaderOption())
                {
                    case ANTIALIASING:
                        Shaders.nextAntialiasingLevel();
                        Shaders.uninit();
                        break;

                    case NORMAL_MAP:
                        Shaders.configNormalMap = !Shaders.configNormalMap;
                        Shaders.uninit();
                        Minecraft.refreshResources();
                        break;

                    case SPECULAR_MAP:
                        Shaders.configSpecularMap = !Shaders.configSpecularMap;
                        Shaders.uninit();
                        Minecraft.refreshResources();
                        break;

                    case RENDER_RES_MUL:
                        float f2 = Shaders.configRenderResMul;
                        float[] afloat2 = QUALITY_MULTIPLIERS;
                        String[] astring2 = QUALITY_MULTIPLIER_NAMES;
                        int k = getValueIndex(f2, afloat2);

                        if (Screen.hasShiftDown())
                        {
                            --k;

                            if (k < 0)
                            {
                                k = afloat2.length - 1;
                            }
                        }
                        else
                        {
                            ++k;

                            if (k >= afloat2.length)
                            {
                                k = 0;
                            }
                        }

                        Shaders.configRenderResMul = afloat2[k];
                        Shaders.uninit();
                        Shaders.scheduleResize();
                        break;

                    case SHADOW_RES_MUL:
                        float f1 = Shaders.configShadowResMul;
                        float[] afloat1 = QUALITY_MULTIPLIERS;
                        String[] astring1 = QUALITY_MULTIPLIER_NAMES;
                        int j = getValueIndex(f1, afloat1);

                        if (Screen.hasShiftDown())
                        {
                            --j;

                            if (j < 0)
                            {
                                j = afloat1.length - 1;
                            }
                        }
                        else
                        {
                            ++j;

                            if (j >= afloat1.length)
                            {
                                j = 0;
                            }
                        }

                        Shaders.configShadowResMul = afloat1[j];
                        Shaders.uninit();
                        Shaders.scheduleResizeShadow();
                        break;

                    case HAND_DEPTH_MUL:
                        float f = Shaders.configHandDepthMul;
                        float[] afloat = HAND_DEPTH_VALUES;
                        String[] astring = HAND_DEPTH_NAMES;
                        int i = getValueIndex(f, afloat);

                        if (Screen.hasShiftDown())
                        {
                            --i;

                            if (i < 0)
                            {
                                i = afloat.length - 1;
                            }
                        }
                        else
                        {
                            ++i;

                            if (i >= afloat.length)
                            {
                                i = 0;
                            }
                        }

                        Shaders.configHandDepthMul = afloat[i];
                        Shaders.uninit();
                        break;

                    case OLD_HAND_LIGHT:
                        Shaders.configOldHandLight.nextValue();
                        Shaders.uninit();
                        break;

                    case OLD_LIGHTING:
                        Shaders.configOldLighting.nextValue();
                        Shaders.updateBlockLightLevel();
                        Shaders.uninit();
                        Minecraft.refreshResources();
                        break;

                    case TWEAK_BLOCK_DAMAGE:
                        Shaders.configTweakBlockDamage = !Shaders.configTweakBlockDamage;
                        break;

                    case CLOUD_SHADOW:
                        Shaders.configCloudShadow = !Shaders.configCloudShadow;
                        break;

                    case TEX_MIN_FIL_B:
                        Shaders.configTexMinFilB = (Shaders.configTexMinFilB + 1) % 3;
                        Shaders.configTexMinFilN = Shaders.configTexMinFilS = Shaders.configTexMinFilB;
                        button.text = "Tex Min: " + Shaders.texMinFilDesc[Shaders.configTexMinFilB];
                        ShadersTex.updateTextureMinMagFilter();
                        break;

                    case TEX_MAG_FIL_N:
                        Shaders.configTexMagFilN = (Shaders.configTexMagFilN + 1) % 2;
                        button.text = "Tex_n Mag: " + Shaders.texMagFilDesc[Shaders.configTexMagFilN];
                        ShadersTex.updateTextureMinMagFilter();
                        break;

                    case TEX_MAG_FIL_S:
                        Shaders.configTexMagFilS = (Shaders.configTexMagFilS + 1) % 2;
                        button.text = "Tex_s Mag: " + Shaders.texMagFilDesc[Shaders.configTexMagFilS];
                        ShadersTex.updateTextureMinMagFilter();
                        break;

                    case SHADOW_CLIP_FRUSTRUM:
                        Shaders.configShadowClipFrustrum = !Shaders.configShadowClipFrustrum;
                        button.text = "ShadowClipFrustrum: " + toStringOnOff(Shaders.configShadowClipFrustrum);
                        ShadersTex.updateTextureMinMagFilter();
                }

                guibuttonenumshaderoption.updateButtonText();
            }
            else
            {
                switch (button.id)
                {
                    case 201:
                        switch (getOSType())
                        {
                            case 1:
                                String s = String.format("cmd.exe /C start \"Open file\" \"%s\"", Shaders.shaderpacksdir.getAbsolutePath());

                                try
                                {
                                    Runtime.getRuntime().exec(s);
                                    return;
                                }
                                catch (IOException ioexception)
                                {
                                    ioexception.printStackTrace();
                                    break;
                                }

                            case 2:
                                try
                                {
                                    Runtime.getRuntime().exec(new String[] {"/usr/bin/open", Shaders.shaderpacksdir.getAbsolutePath()});
                                    return;
                                }
                                catch (IOException ioexception1)
                                {
                                    ioexception1.printStackTrace();
                                }
                        }

                        boolean flag = false;

                        try
                        {
                            Class oclass = Class.forName("java.awt.Desktop");
                            Object object = oclass.getMethod("getDesktop").invoke(null);
                            oclass.getMethod("browse", URI.class).invoke(object, (new File(Minecraft.gameDir, Shaders.shaderpacksdirname)).toURI());
                        }
                        catch (Throwable throwable)
                        {
                            throwable.printStackTrace();
                            flag = true;
                        }

                        if (flag)
                        {
                            Config.dbg("Opening via system class!");
                            Sys.openURL("file://" + Shaders.shaderpacksdir.getAbsolutePath());
                        }

                        break;

                    case 202:
                        new File(Shaders.shadersdir, "current.cfg");
                        Shaders.storeConfig();
                        saved = true;
                        Minecraft.openScreen(parentGui);
                        break;

                    case 203:
                        GuiShaderOptions guishaderoptions = new GuiShaderOptions(this, Config.getGameSettings());
                        Minecraft.openScreen(guishaderoptions);
                        break;

                    default:
                        shaderList.actionPerformed(button);
                }
            }
        }
    }

    /**
     * Called when the screen is unloaded. Used to disable keyboard repeat events
     */
    public void onClosed()
    {
        super.onClosed();

        if (!saved)
        {
            Shaders.storeConfig();
        }
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void draw(int mouseX, int mouseY, float partialTick)
    {
        drawDefaultBackground();
        shaderList.drawScreen(mouseX, mouseY, partialTick);

        if (updateTimer <= 0)
        {
            shaderList.updateList();
            updateTimer += 20;
        }

        drawCenteredString(font, screenTitle + " ", width / 2, 15, 16777215);
        String s = "OpenGL: " + Shaders.glVersionString + ", " + Shaders.glVendorString + ", " + Shaders.glRendererString;
        int i = font.getStringWidth(s);

        if (i < width - 5)
        {
            drawCenteredString(font, s, width / 2, height - 40, 8421504);
        }
        else
        {
            drawString(font, s, 5, height - 40, 8421504);
        }

        super.draw(mouseX, mouseY, partialTick);
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void update()
    {
        super.update();
        --updateTimer;
    }

    public Minecraft getMc()
    {
        return minecraft;
    }

    public void drawCenteredString(String text, int x, int y, int color)
    {
        drawCenteredString(font, text, x, y, color);
    }

    public static String toStringOnOff(boolean value)
    {
        String s = Lang.getOn();
        String s1 = Lang.getOff();
        return value ? s : s1;
    }

    public static String toStringAa(int value)
    {
        if (value == 2)
        {
            return "FXAA 2x";
        }
        else
        {
            return value == 4 ? "FXAA 4x" : Lang.getOff();
        }
    }

    public static String toStringValue(float val, float[] values, String[] names)
    {
        int i = getValueIndex(val, values);
        return names[i];
    }

    public static int getValueIndex(float val, float[] values)
    {
        for (int i = 0; i < values.length; ++i)
        {
            float f = values[i];

            if (f >= val)
            {
                return i;
            }
        }

        return values.length - 1;
    }

    public static String toStringQuality(float val)
    {
        return toStringValue(val, QUALITY_MULTIPLIERS, QUALITY_MULTIPLIER_NAMES);
    }

    public static String toStringHandDepth(float val)
    {
        return toStringValue(val, HAND_DEPTH_VALUES, HAND_DEPTH_NAMES);
    }

    public static int getOSType()
    {
        String s = System.getProperty("os.name").toLowerCase();

        if (s.contains("win"))
        {
            return 1;
        }
        else if (s.contains("mac"))
        {
            return 2;
        }
        else if (s.contains("solaris"))
        {
            return 3;
        }
        else if (s.contains("sunos"))
        {
            return 3;
        }
        else if (s.contains("linux"))
        {
            return 4;
        }
        else
        {
            return s.contains("unix") ? 4 : 0;
        }
    }
}
