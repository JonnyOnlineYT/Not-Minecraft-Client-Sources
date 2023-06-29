package splash.client.themes.virtue.tab;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.text.WordUtils;
import org.lwjgl.input.Keyboard;
import splash.Splash;
import splash.api.module.Module;
import splash.api.module.category.ModuleCategory;
import splash.api.theme.type.FontType;
import splash.client.modules.visual.UI;
import splash.client.modules.visual.UI.FontMode;
import splash.client.modules.visual.UI.LogoMode;
import splash.client.modules.visual.UI.TabColor;
import splash.utilities.visual.RenderUtilities;

import java.awt.*;
import java.util.Arrays;
import java.util.Collections;

/**
 * Author: Ice
 * Created: 18:18, 14-Jun-20
 * Project: Client
 */
public class VirtueTabGUI {

    public static Minecraft mc = Minecraft.getMinecraft();
    private static int selected, moduleSelected, valueSelected;
    private static boolean isOpen;
    private static boolean isValueOpen;
    private static float extended;
    private static int bg = new Color(0, 0, 0, 150).getRGB();

    public static int length = 25;
    public static int color = Splash.getInstance().getClientColor();
    public static int spacing = 20;
    public static int ypos = 12;

    public static void drawTabGui() {
    	UI visuals = ((UI)Splash.INSTANCE.getModuleManager().getModuleByClass(UI.class));
    	if(visuals.getValue("TabGUI Color").getValue() == TabColor.SOLID) {
    		color = Splash.getInstance().getClientColor();
    	} else {
    		color = visuals.color;
    	}
  
        if (!mc.gameSettings.showDebugInfo) {
            for (int i = 0; i < ModuleCategory.values().length; i++) {
                ModuleCategory cat = ModuleCategory.values()[i];

                if(cat == ModuleCategory.CONFIGS) {

                } else {
                    if (Splash.getInstance().getValueManager().getValue(Splash.getInstance().getModuleManager().getModuleByClass(UI.class), "Font").getValue() == FontType.CUSTOM) {
                    	if (Splash.getInstance().getValueManager().getValue(Splash.getInstance().getModuleManager().getModuleByClass(UI.class), "Font Mode").getValue() == FontMode.ARIAL) {
                    		length = 23;
                    	}else {
                    		length = 25;
                    	}
                    }else {
                    	length = 22;
                    }
                    if (Splash.getInstance().getValueManager().getValue(Splash.getInstance().getModuleManager().getModuleByClass(UI.class), "Logo").getValue() == LogoMode.ASTOLFO){
                    	length = 8;
                    }
                    int y;
                   
                    if (Splash.getInstance().getValueManager().getValue(Splash.getInstance().getModuleManager().getModuleByClass(UI.class), "Logo").getValue() == LogoMode.SPLASHBOX) {
                    	y = 35;
                    } else if (Splash.getInstance().getValueManager().getValue(Splash.getInstance().getModuleManager().getModuleByClass(UI.class), "Logo").getValue() == LogoMode.SPLASH) {
                     y = 50;	
                    }else if (Splash.getInstance().getValueManager().getValue(Splash.getInstance().getModuleManager().getModuleByClass(UI.class), "Logo").getValue() == LogoMode.HELIUM) {
                    	y = 20;
                    } else if (Splash.getInstance().getValueManager().getValue(Splash.getInstance().getModuleManager().getModuleByClass(UI.class), "Logo").getValue() == LogoMode.SIGHT) {
                    	y = 40;
                    
                	}else {
                    	y =  12;
                	}
                    int index = 0;
                    long x = -6L;
                    int xPos = 5;
                    if (Splash.getInstance().getValueManager().getValue(Splash.INSTANCE.getModuleManager().getModuleByClass(UI.class), "Logo").getValue() == LogoMode.ASTOLFO) {
                        xPos = 1;
                     } else {
                         xPos = 5;

                     }
                    int yRain = 0;

                    bg = new Color(0, 0, 0, 170).getRGB();
                    yRain -= 11;

                    if (selected == i) {
                        RenderUtilities.INSTANCE.drawBorderedRect(xPos, y + i * 12, (7 + length + Splash.getInstance().getFontRenderer().getStringWidthCustom("Movement")), y + 12 + i * 12, 2, color, color);
                    } else {
                        RenderUtilities.drawRect(xPos, y + i * 12, 7 + length + Splash.getInstance().getFontRenderer().getStringWidthCustom("Movement"), y + 12 + i * 12, bg);
                    }

                    String name = "";

                    if(Splash.getInstance().getValueManager().getValue(Splash.getInstance().getModuleManager().getModuleByClass(UI.class), "Case").getValue() == UI.Case.LOWERCASE) {
                        name = cat.name().substring(0, 1).toLowerCase() + cat.name().substring(1).toLowerCase();
                    } else {
                        name = WordUtils.capitalizeFully(cat.name().substring(0, 1) + cat.name().substring(1));
                    }
                    //System.out.println(name);
                    int value = 12;
                    if (Splash.getInstance().getValueManager().getValue(Splash.INSTANCE.getModuleManager().getModuleByClass(UI.class), "Logo").getValue() == LogoMode.ASTOLFO) {
                    	value = 7;
                    }
                    	
                    if(selected == i) {
                        Splash.getInstance().getFontRenderer().drawStringWithShadow(name, (float) (value + 1 + spacing) - 23, y + i * 12 + 2, 0xFFFFFF);
                    } else {
                        Splash.getInstance().getFontRenderer().drawStringWithShadow(name, (float) (value + spacing) - 23, y + i * 12 + 2, 0xFFFFFF);
                    }
                    extended += 0.01F * (isOpen ? 1F : -1F);
                    extended = Math.max(0, extended);
                    extended = Math.min(1, extended);

                    if ((isOpen || extended > 0) && i == selected) {
                        for (int j = 0; j < Splash.getInstance().getModuleManager().getModulesInCategory(cat).size(); j++) {
                            Module mod = Splash.getInstance().getModuleManager().getModulesInCategory(cat).get(j);
                            if (moduleSelected == j) {
                                Gui.drawRect(6f + Splash.getInstance().getFontRenderer().getStringWidthCustom("Movement") + 2f + length, y + i * 12 + j * 12, 0 + Splash.getInstance().getFontRenderer().getStringWidthCustom("Movement") + 5 + length + Splash.getInstance().getFontRenderer().getStringWidthCustom("Inventory Manager") + 6, y + 12 + i * 12 + j * 12,  color);
                            } else {
                                RenderUtilities.drawRect(3D + Splash.getInstance().getFontRenderer().getStringWidthCustom("Movement") + 2 + length + 3, y + i * 12 + j * 12, 3 + Splash.getInstance().getFontRenderer().getStringWidthCustom("Movement") + 2 + length + Splash.getInstance().getFontRenderer().getStringWidthCustom("Inventory Manager") + 6, y + 12 + i * 12 + j * 12, bg);
                            }

                            if(moduleSelected == j) {
                                if(Splash.getInstance().getValueManager().getValue(Splash.getInstance().getModuleManager().getModuleByClass(UI.class), "Case").getValue() == UI.Case.LOWERCASE) {
                                    Splash.getInstance().getFontRenderer().drawStringWithShadow(mod.getModuleDisplayName().toLowerCase(), (float) (spacing + Splash.getInstance().getFontRenderer().getStringWidthCustom("Movement") + 5 + 1 + length + 2) -16, y + i * 12 + j * 12 + 2, Splash.getInstance().getModuleManager().getModuleByDisplayName(mod.getModuleDisplayName()).isModuleActive() ? color : new Color(195, 195, 195, 255).getRGB());
                                } else {
                                    Splash.getInstance().getFontRenderer().drawStringWithShadow(mod.getModuleDisplayName(), (float) (spacing + Splash.getInstance().getFontRenderer().getStringWidthCustom("Movement") + 5 + 1 + length + 2) -16, y + i * 12 + j * 12 + 2, Splash.getInstance().getModuleManager().getModuleByDisplayName(mod.getModuleDisplayName()).isModuleActive() ? color : new Color(195, 195, 195, 255).getRGB());

                                }
                            } else {
                                if(Splash.getInstance().getValueManager().getValue(Splash.getInstance().getModuleManager().getModuleByClass(UI.class), "Case").getValue() == UI.Case.LOWERCASE) {
                                    Splash.getInstance().getFontRenderer().drawStringWithShadow(mod.getModuleDisplayName().toLowerCase(), (float) (spacing + Splash.getInstance().getFontRenderer().getStringWidthCustom("Movement") + 3 + 1 + length + 2) -16, y + i * 12 + j * 12 + 2, Splash.getInstance().getModuleManager().getModuleByDisplayName(mod.getModuleDisplayName()).isModuleActive() ? color : new Color(195, 195, 195, 255).getRGB());
                                } else {
                                    Splash.getInstance().getFontRenderer().drawStringWithShadow(mod.getModuleDisplayName(), (float) (spacing + Splash.getInstance().getFontRenderer().getStringWidthCustom("Movement") + 3 + 1 + length + 2) -16, y + i * 12 + j * 12 + 2, Splash.getInstance().getModuleManager().getModuleByDisplayName(mod.getModuleDisplayName()).isModuleActive() ? color : new Color(195, 195, 195, 255).getRGB());

                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public static void onKey(int key) {
        if (key == Keyboard.KEY_UP) {
            if (!isOpen) {
                selected--;
                if (selected == -1) {
                    selected = 0;
                }
            } else {
                moduleSelected--;
                if (moduleSelected <= 0) {
                    moduleSelected = 0;
                }
            }
        }
        if (key == Keyboard.KEY_DOWN) {
            if (!isOpen) {
                selected++;
                if (selected == 5) {
                    selected = 4;
                }
            } else {
                moduleSelected++;
                if (moduleSelected >= Splash.getInstance().getModuleManager().getModulesInCategory(ModuleCategory.values()[selected]).size() - 1) {
                    moduleSelected = Splash.getInstance().getModuleManager().getModulesInCategory(ModuleCategory.values()[selected]).size() - 1;
                }
            }
        }
        if (key == Keyboard.KEY_RIGHT) {
            isOpen = true;
        }
        if (key == Keyboard.KEY_LEFT) {
            isOpen = false;
            moduleSelected = 0;
        }
        if (key == Keyboard.KEY_RETURN) {
            if (Splash.getInstance().getModuleManager().getModulesInCategory(ModuleCategory.values()[selected]).get(moduleSelected).isModuleActive()) {
                Splash.getInstance().getModuleManager().getModulesInCategory(ModuleCategory.values()[selected]).get(moduleSelected).activateModule();
            } else {
                Splash.getInstance().getModuleManager().getModulesInCategory(ModuleCategory.values()[selected]).get(moduleSelected).activateModule();
            }
        }
    }
}