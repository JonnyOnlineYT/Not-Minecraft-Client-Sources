package ua.apraxia.gui;

import ua.apraxia.Hexbyte;
import ua.apraxia.modules.Categories;
import ua.apraxia.modules.Module;
import ua.apraxia.modules.settings.Setting;
import ua.apraxia.modules.settings.impl.BooleanSetting;
import ua.apraxia.modules.settings.impl.ColorSetting;
import ua.apraxia.modules.settings.impl.ModeSetting;
import ua.apraxia.modules.settings.impl.SliderSetting;
import ua.apraxia.utility.animation.AnimationUtil;
import ua.apraxia.utility.font.Fonts;
import ua.apraxia.utility.render.RenderUtility;
import ua.apraxia.utility.render.RoundedUtility;
import ua.apraxia.utility.render.StencilUtility;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import static ua.apraxia.modules.impl.display.WindowGUI.blur;
import static ua.apraxia.modules.impl.display.WindowGUI.filter;


public class WindowGUI extends GuiScreen {
    public Categories selectedCategory = Categories.Combat;
    private int mousex, mousey;
    public Module components;
    private final HashMap<Module, Integer> hoveredModuleAnim = new HashMap<>();
    private final HashMap<Module, Float> checkboxAnim = new HashMap<>();
    private final HashMap<Setting, Integer> modeSettingAnim = new HashMap<>();
    private float scroll = 0, animScroll = 0;
    public static float x = 200, y = 100, width = 400, height = 340, prevX, prevY;
    private boolean dragging;
    private float scaleAnim = 0.005f;
    private float translateAnim = -500, openedCategoryAnim = 300;
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        ScaledResolution sr = new ScaledResolution(mc);
        mousex = mouseX;
        mousey = mouseY;
        if (scroll >= 0) {
            scroll = 0;
        }
        scaleAnim = AnimationUtil.animation(scaleAnim, 1, 0.005f);
        animScroll = AnimationUtil.animation(animScroll, scroll, 0.0000055f);
        translateAnim = AnimationUtil.animation(translateAnim, 0, 0.1f);
            if (blur.value) {
                RenderUtility.drawBlur(() -> RoundedUtility.drawRound(x +2, y +2, width -4, height -3, 5, new Color(3, 13, 26, 231)), 15);
            }
            if (filter.value) {
                RenderUtility.drawGrayFilterNoBlur(() -> RoundedUtility.drawRound(x +2, y +2, width -4, height -3, 5, new Color(3, 13, 26, 231)));
            }
            if (filter.value && blur.value) {
            RenderUtility.drawGrayFilter(() -> RoundedUtility.drawRound(x +2, y +2, width -4, height -3, 5, new Color(3, 13, 26, 231)), 10);
            }
            StencilUtility.initStencilToWrite();
            RoundedUtility.drawRound(x +2, y +2, width -4, height -3, 5, new Color(3, 13, 26, 231));
            StencilUtility.readStencilBuffer(1);
                 RenderUtility.drawBlurredShadow(x + 110, y, width -110, height, 0, new Color(0, 0, 0, 245));
            RenderUtility.drawBlurredShadow(x, y, 110, height, 0, new Color(0, 0, 0, 195));
            RenderUtility.drawBlurredShadow(x +110, y, 1, height, 0, new Color(8, 11, 20, 255));
            RenderUtility.drawBlurredShadow(x, y +310, 110, 1, 0,  new Color(8, 11, 20, 255));
            StencilUtility.initStencilToWrite();
            RenderUtility.drawFCircle(x + 19, y + 326, 0, 360, 9, true, new Color(26, 80, 67, 255));
            StencilUtility.readStencilBuffer(1);
            RenderUtility.drawImage(new ResourceLocation("stardust/images/ava.png"), x + 9, y + 315, 21, 21, new Color(255, 255, 255));
            RenderUtility.drawCircle(x + 19, y + 326, 0, 360, 10,  new Color(0, 4, 14, 73).getRGB(), 2);
            StencilUtility.initStencilToWrite();
            RoundedUtility.drawRound(x, y +2, width, height -3, 10, false, new Color(4, 6, 10, 255));
            StencilUtility.readStencilBuffer(1);
            Fonts.nlbolt30.drawString("HEXBYTE", x + 18.3F, y + 13, new Color(3, 167, 243, 255).getRGB());
            Fonts.nlbolt30.drawString("HEXBYTE", x + 19, y + 13, new Color(255, 255, 255, 255).getRGB());
            Fonts.sfsemib14.drawString("apraxia1337", x + 35, y + 320, new Color(255, 255, 255, 255).getRGB());
            Fonts.medium14.drawString("till: ", x + 35, y + 330, new Color(51, 68, 94, 255).getRGB());
            Fonts.medium14.drawString("unlimited", x + 48, y + 330, new Color(3, 167, 243, 255).getRGB());
            int offset1 = 0;
            openedCategoryAnim = (float) AnimationUtil.Interpolate(openedCategoryAnim, 0, 0.1f);
            drawModules(mouseX, mouseY, x, y + openedCategoryAnim);
            for (Categories category : Categories.values()) {
                    if(category == Categories.Display)
                        offset1 += 20;
                if(selectedCategory == category) {
                     RoundedUtility.drawRound(x +3, y + 51 + offset1, 102, 16, 3, new Color(255, 255, 255, 21));
                }
                    Fonts.icons20.drawCenteredString(category.getChar(), x + 13, y + 56.5f + offset1,
                            category == selectedCategory ? new Color(0, 140, 252, 255).getRGB()
                                    : new Color(0, 140, 252, 255).getRGB());
                Fonts.nl14.drawString(category.name(), x + 25, y + 57 + offset1,
                            category == selectedCategory ?  new Color(255, 255, 255, 242).getRGB()
                                    : new Color(255, 255, 255, 242).getRGB());
                offset1 += 20;
                }
            Fonts.nl14.drawString("Main", x + 8, y + 41, new Color(50, 66, 80, 255).getRGB());
            Fonts.nl14.drawString("Miscellaneous", x + 8, y + 182, new Color(50, 66, 80, 255).getRGB());
            StencilUtility.uninitStencilBuffer();
        closedGuiAnim(Keyboard.getEventKey());
        if (dragging && Mouse.isButtonDown(0)) {
            x = mouseX + prevX;
            y = mouseY + prevY;
        } else {
            dragging = false;
        }
    }
    private void drawModules(int mouseX, int mouseY, float x, float y) {
        int offset = 0;
        int offset1 = 0;
        for (Module m : Hexbyte.getInstance().moduleManagment.getModulesForCategory(selectedCategory)) {
            if (!hoveredModuleAnim.containsKey(m))
                hoveredModuleAnim.put(m, 0);
            if (!checkboxAnim.containsKey(m))
                checkboxAnim.put(m, 0f);
            float modeHeight = 0;
            for (Setting s : m.getSetting()) {
                if (s instanceof ModeSetting) {
                    ModeSetting modeSetting = (ModeSetting) s;
                    modeHeight += modeSetting.opened ? 8.5 * modeSetting.modes.size() + 16 : 5;
                }
                if (s instanceof ColorSetting) {
                    modeHeight += 10;
                }}
            float heightModule = 25 + 25 * m.getSetting().size() + modeHeight;
            RoundedUtility.drawRoundOutline(x + 125, y + 25 + offset + animScroll, 265, heightModule, 3, -0.5f,  new Color(0, 0, 0, 158),   new Color(17, 22, 42, 255));
            if(m.settingList.size() != 0) {
                RenderUtility.drawBlurredShadow(x + 130, y + 45 + offset + animScroll, 257, 1, 0, new Color(11, 15, 22, 179));
            }
            int y1 = 36;
            if(m.settingList.size() != 0) {
                y1 = 33;
            }
            if(m.settingList.size() == 0) {
                y1 = 35;
            }
            Fonts.sfsemib16.drawString("" + m.getModuleName(), x + 135, y + 35 + offset + animScroll, new Color(255, 255, 255, 255).getRGB());
            if (m.isModuleState()) {
                RoundedUtility.drawRoundOutline(x + 368, y + y1 + offset + animScroll -1, 13, 6, 2, -0.4f, new Color(3, 20, 46, 255), new Color(17, 21, 38, 255));
            } else {
                RoundedUtility.drawRoundOutline(x + 368, y + y1 + offset + animScroll -1, 13, 6, 2, -0.4f, new Color(5, 8, 15, 255), new Color(18, 23, 43, 255));
            }
            checkboxAnim.put(m, AnimationUtil.animation(checkboxAnim.get(m), m.isModuleState() ? 6 : -2, 0.1f));
            RoundedUtility.drawRoundCircle(x + 372 + checkboxAnim.get(m), y + y1 + 2 + offset + animScroll, 6, m.isModuleState() ? new Color(3, 167, 243, 255) : new Color(74, 87, 97, 255));
            for (Setting setting : m.getSetting()) {
                {
                    if(!modeSettingAnim.containsKey(setting))
                        modeSettingAnim.put(setting, 0);
                }
                if (setting instanceof BooleanSetting) {
                    BooleanSetting booleanSetting = (BooleanSetting) setting;
                    booleanSetting.animation = AnimationUtil.animation(booleanSetting.animation, booleanSetting.value ? 10 : 2, 0.3f);
                    Fonts.sfsemib14.drawString(booleanSetting.name, x + 135, y + 60 + offset1 + offset + animScroll, booleanSetting.value ? new Color(255, 255, 255, 255).getRGB() : new Color(136, 154, 167, 255).getRGB());
                    if (booleanSetting.value) {
                        RoundedUtility.drawRoundOutline(x + 368, y + 61 + offset1 + offset + animScroll, 13, 6, 2, -0.4f, new Color(3, 20, 46, 255), new Color(17, 21, 38, 255));
                    } else {
                        RoundedUtility.drawRoundOutline(x + 368, y + 61 + offset1 + offset + animScroll, 13, 6, 2, -0.4f, new Color(5, 8, 15, 255), new Color(18, 23, 43, 255));
                    }
                    RoundedUtility.drawRoundCircle((float) (x + 368 + booleanSetting.animation), y + 60 + 4f + offset1 + offset + animScroll, 6, booleanSetting.value ? new Color(3, 167, 243, 255) : new Color(74, 87, 97, 255));
                    offset += 25;
                }
                if (setting instanceof SliderSetting) {
                    SliderSetting sliderSetting = (SliderSetting) setting;
                    if (sliderSetting.pressed) {
                        sliderSetting.value = MathHelper.clamp((float) ((double) (mouseX - x - 377) * (sliderSetting.max - sliderSetting.min) / (double) 150 + sliderSetting.max), sliderSetting.min, sliderSetting.max);
                        sliderSetting.value = MathHelper.round(sliderSetting.value, sliderSetting.increment);
                    }
                    Fonts.sfsemib14.drawString(sliderSetting.name, x + 135, y + 59 + offset1 + offset + animScroll,new Color(153, 180, 189, 255).getRGB());
                    RoundedUtility.drawRound(x + 226, y + 60.5f + offset1 + offset + animScroll, 152, 1, 0, new Color(10, 20, 36, 255));
                    double widthFormule = ((((sliderSetting.value) - sliderSetting.min) / (sliderSetting.max - sliderSetting.min))) * 150;
                    sliderSetting.widthAnimated = MathHelper.lerp(sliderSetting.widthAnimated, widthFormule, 0.15);
                    sliderSetting.printAnimated = MathHelper.lerp(sliderSetting.printAnimated, sliderSetting.value, 0.15);
                    RoundedUtility.drawRound(x + 226, y + 60.5f + offset1 + offset + animScroll, (float) sliderSetting.widthAnimated, 1, 0, new Color(0, 101, 147, 255));
                    RenderUtility.drawFCircle(x + 225 + (float) sliderSetting.widthAnimated, y + 61 + offset1 + offset + animScroll + 0.5f,0,360,3,true, new Color(61, 132, 222, 255));
                    boolean da = sliderSetting.pressed;
                    sliderSetting.alphaText = (int) MathHelper.lerp(sliderSetting.alphaText, da ? 208 : 100, 0.05);
                    RoundedUtility.drawRoundOutline(x + 223 - Fonts.sfbolt14.getStringWidth(String.valueOf(MathHelper.round(sliderSetting.printAnimated, sliderSetting.increment))) / 2F + sliderSetting.widthAnimated, y + 46.5f + offset1 + offset + animScroll, Fonts.medium14.getStringWidth(String.valueOf(MathHelper.round(sliderSetting.printAnimated, sliderSetting.increment))) +4f, 11, 2, -0.5f,    new Color(5, 8, 15),  new Color(18, 23, 43, 255));
                    Fonts.sfbolt12.drawString(String.valueOf(MathHelper.round(sliderSetting.printAnimated, sliderSetting.increment)), x + 226 - Fonts.sfbolt14.getStringWidth(String.valueOf(MathHelper.round(sliderSetting.printAnimated, sliderSetting.increment))) / 2F + sliderSetting.widthAnimated, y + 51.5f + offset1 + offset + animScroll, new Color(114, 135, 144, 255).getRGB());
                    offset += 25;
                }
                if (setting instanceof ModeSetting) {
                    ModeSetting modeSetting = (ModeSetting) setting;
                    Fonts.sfsemib14.drawString(modeSetting.name, x + 135, y + 60 + offset1 + offset + animScroll, new Color(153, 180, 189, 255).getRGB());
                    RoundedUtility.drawRoundOutline(x + 326, y + 58 + offset1 + offset + animScroll, 52, 13, 2, -0.3f,
                            new Color(5, 8, 15),  new Color(18, 23, 43, 255));
                    String mode = modeSetting.modes.get(modeSetting.index);
                    Fonts.icons18.drawString("v", x + 366, y + 63 + offset1 + offset + animScroll, new Color(153, 180, 189, 255).getRGB());
                    Fonts.sfsemib14.drawString(mode, x + 329, y + 61 + offset1 + offset + animScroll + 2, new Color(153, 180, 189, 255).getRGB());
                    if (modeSetting.opened) {
                        RoundedUtility.drawRoundOutline(x + 326, y + 75 + offset1 + offset + animScroll -3, 52, modeSetting.modes.size() * 10.5f, 2, -0.3f,
                                new Color(5, 8, 15, modeSettingAnim.get(setting)), new Color(18, 23, 43, modeSettingAnim.get(setting)));
                        for (int i = 0; i < modeSetting.modes.size(); i++) {
                            String mode1 = modeSetting.modes.get(i);
                            Fonts.sfsemib14.drawString(mode1, x + 331, y + 75 + offset1 + offset + animScroll + 2 + i * 10,
                                    i == modeSetting.index
                                            ?   new Color(255, 255, 255, modeSettingAnim.get(setting)).getRGB()
                                            :   new Color(152, 175, 188, modeSettingAnim.get(setting)).getRGB());
                        }}
                    modeSettingAnim.put(setting, (int) AnimationUtil.animation(modeSettingAnim.get(setting),
                            modeSetting.opened ? 255 : 0, 0.03f));

                    offset += 25 + ( modeSetting.opened ? 8.5 * modeSetting.modes.size() + 10 : 0 );
                }
                if (setting instanceof ColorSetting) {
                    ColorSetting colorSetting = (ColorSetting) setting;
                    Fonts.sfsemib14.drawString(colorSetting.name, x + 135, y + 66 + offset1 + offset + animScroll, new Color(153, 180, 189, 255).getRGB());
                    RenderUtility.drawImage(new ResourceLocation("stardust/images/colorpicker.png"), x + 340, y + 41 + offset1 + offset + animScroll + 7, 34, 34, new Color(255, 255, 255));
                    double soX = mouseX - x - 358;
                    double soY = mouseY - y - 58 - offset1 - offset - animScroll - 10;
                    double dst = Math.sqrt(soX * soX + soY * soY);
                    if (dst < 15) {
                        if (Mouse.isButtonDown(0)) {
                            colorSetting.color = Color.HSBtoRGB((float) (Math.atan2(soX, soY) / (Math.PI * 2) -             1), (float) (dst / 15), 1);
                        }}
                    final float[] hsb = Color.RGBtoHSB(new Color(colorSetting.color).getRed(), new Color(colorSetting.color).getGreen(), new Color(colorSetting.color).getBlue(), null);
                    float poX =
                            (float) (hsb[1] * 15 * (Math.sin(Math.toRadians(hsb[0] * 360)) / Math
                                    .sin(Math.toRadians(90))));
                    float poY =
                            (float) (hsb[1] * 15 * (Math.sin(Math.toRadians(90 - (hsb[0] * 360))) / Math
                                    .sin(Math.toRadians(90))));
                    RoundedUtility.drawRoundCircle(x + 357 + + poX, y + 59 + offset1 + offset + animScroll + 5 + poY, 3, new Color(0, 0, 0, 255));
                    RoundedUtility.drawRoundCircle(x + 357 + + poX, y + 59 + offset1 + offset + animScroll + 5 + poY, 2, new Color(colorSetting.color));
                    RoundedUtility.drawRoundCircle(x + 330, y + 58 + offset1 + offset + animScroll + 7, 8, new Color(3, 20, 46, 255));
                    RoundedUtility.drawRoundCircle(x + 330, y + 58 + offset1 + offset + animScroll + 7, 7, new Color(colorSetting.color));
                    offset += 39;
                }
            }
            offset += 35;
        }
    }
    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        for (Module module : Hexbyte.getInstance().moduleManagment.getModulesForCategory(selectedCategory)) {
            for (Setting setting : module.getSetting()) {
                if (setting instanceof SliderSetting) {
                    SliderSetting sliderSetting = (SliderSetting) setting;
                    sliderSetting.pressed = false;
                }
            }
        }
    }
    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {

        if (isHovered(mouseX, mouseY, x, y, 80, 50) && mouseButton == 0) {
            dragging = true;
            prevX = x - mouseX;
            prevY = y - mouseY;
        }
        int offset = 0;
        for (Module module : Hexbyte.getInstance().moduleManagment.getModulesForCategory(selectedCategory)) {
            if (isHovered(mouseX, mouseY, x + 110, y + 23 + offset + animScroll, 300, 25)) {
                if(mouseButton == 0)
                    module.toggle();
                }
            for (Setting setting : module.getSetting()) {
                if (setting instanceof BooleanSetting) {
                    BooleanSetting booleanSetting = (BooleanSetting) setting;
                    if (isHovered(mouseX, mouseY, x + 110, y + 51 + offset + animScroll, 300, 24)) {
                        booleanSetting.value = !booleanSetting.value;
                    }
                    offset += 25;
                }
                if (setting instanceof SliderSetting) {
                    SliderSetting sliderSetting = (SliderSetting) setting;

                    if (isHovered(mouseX, mouseY, x + 226, y + 51 + offset + animScroll, 225, 24)) {
                        sliderSetting.pressed = true;
                    }

                    offset += 25;
                }
                if (setting instanceof ModeSetting) {
                    ModeSetting modeSetting = (ModeSetting) setting;
                    if (isHovered(mouseX, mouseY, x + 327, y + 51 + offset + animScroll, 50, 24)) {
                        modeSetting.opened = !modeSetting.opened;
                    }
                    if (modeSetting.opened) {
                        for (int i = 0; i < modeSetting.modes.size(); i++) {
                            String mode1 = modeSetting.modes.get(i);
                            if (isHovered(mouseX, mouseY, x + 327, y + 75 + offset + animScroll + i * 10, 50, 10)) {
                                modeSetting.index = i;
                            }
                        }
                    }
                    offset += 25 + ( modeSetting.opened ? 8.5 * modeSetting.modes.size() + 10 : 0 );
                }
                if (setting instanceof ColorSetting) {
                    offset += 39;
                }
            }
            offset += 35;
        }
        offset = 0;
        for (Categories category : Categories.values()) {
            if(category == Categories.Display)
                offset += 20;
            if (isHovered(mouseX, mouseY, x - 1, y + 52 + offset, 80, 17) && mouseButton == 0 && category != selectedCategory) {
                selectedCategory = category;

                openedCategoryAnim = 200;

                scroll = 0;
            }
            offset += 20;
        }
    }
    @Override
    public void handleMouseInput() throws IOException {
        if (Mouse.hasWheel() && isHovered(mousex, mousey, x +80, y, width -100, height)) {
            int mouse = Mouse.getDWheel();
            if (mouse > 0) {
                scroll += 50;
            } else {
                if (mouse < 0) {
                    scroll -= 50;
                }
            }
        }
        super.handleMouseInput();
    }
    private void closedGuiAnim(int callCode) {
        ScaledResolution sr = new ScaledResolution(mc);
        if (callCode == Keyboard.KEY_ESCAPE) {
            translateAnim = AnimationUtil.animation(translateAnim, sr.getScaledWidth() * 1.7f, 0.06F);
            if (translateAnim >= sr.getScaledWidth() - 200) mc.player.closeScreen();
        }
    }
    @Override
    public void keyTyped(char typedChar, int callCode) {
    }
    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
    @Override
    public void onGuiClosed() {
        scaleAnim = 0.00005f;
        translateAnim = -500;

        openedCategoryAnim = 200;
    }
    public boolean isHovered(final int mouseX, final int mouseY, final double x, final double y, final double width, final double height) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }
}