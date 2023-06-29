package net.minecraft.client.settings;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.AlertScreen;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.client.CPacketClientSettings;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TranslatableComponent;
import net.minecraft.world.EnumDifficulty;
import optifine.*;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import shadersmod.client.Shaders;

import java.io.*;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class GameSettings {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Gson GSON = new Gson();
    private static final Type TYPE_LIST_STRING = new ParameterizedType() {
        public Type[] getActualTypeArguments() {
            return new Type[]{String.class};
        }

        public Type getRawType() {
            return List.class;
        }

        public Type getOwnerType() {
            return null;
        }
    };
    public static final Splitter COLON_SPLITTER = Splitter.on(':');
    private static final String[] SCALES = {"options.guiScale.auto", "options.guiScale.small", "options.guiScale.normal", "options.guiScale.large"};
    private static final String[] PARTICLES = {"options.particles.all", "options.particles.decreased", "options.particles.minimal"};
    private static final String[] AMBIENT_OCCLUSIONS = {"options.ao.off", "options.ao.min", "options.ao.max"};
    private static final String[] CLOUDS_TYPES = {"options.off", "options.clouds.fast", "options.clouds.fancy"};
    private static final String[] ATTACK_INDICATORS = {"options.off", "options.attack.crosshair", "options.attack.hotbar"};
    public float mouseSensitivity = 0.5F;
    public boolean invertMouse;
    public int renderDistance = 8;
    public boolean viewBobbing = true;
    public boolean anaglyph;
    public boolean fboEnable = true;
    public int limitFramerate = 120;
    public int clouds = 2;
    public boolean fancyGraphics = true;
    public int ambientOcclusion = 2;
    public List<String> resourcePacks = Lists.newArrayList();
    public List<String> incompatibleResourcePacks = Lists.newArrayList();
    public EntityPlayer.EnumChatVisibility chatVisibility = EntityPlayer.EnumChatVisibility.FULL;
    public boolean chatColours = true;
    public boolean chatLinks = true;
    public boolean chatLinksPrompt = true;
    public float chatOpacity = 1.0F;
    public boolean fullScreen;
    public boolean enableVsync = true;
    public boolean useVbo = true;
    public boolean reducedDebugInfo;
    public boolean advancedItemTooltips;
    public boolean pauseOnLostFocus = true;
    private final Set<EnumPlayerModelParts> setModelParts = Sets.newHashSet(EnumPlayerModelParts.values());
    public EnumHandSide mainHand = EnumHandSide.RIGHT;
    public int overrideWidth;
    public int overrideHeight;
    public boolean heldItemTooltips = true;
    public float chatScale = 1.0F;
    public float chatWidth = 1.0F;
    public float chatHeightUnfocused = 0.44366196F;
    public float chatHeightFocused = 1.0F;
    public int mipmapLevels = 4;
    private final Map<SoundCategory, Float> soundLevels = Maps.newEnumMap(SoundCategory.class);
    public boolean useNativeTransport = true;
    public boolean entityShadows = true;
    public int attackIndicator = 1;
    public boolean showSubtitles;
    public boolean autoJump;
    public KeyBinding keyBindForward = new KeyBinding("key.forward", 17, "key.categories.movement");
    public KeyBinding keyBindLeft = new KeyBinding("key.left", 30, "key.categories.movement");
    public KeyBinding keyBindBack = new KeyBinding("key.back", 31, "key.categories.movement");
    public KeyBinding keyBindRight = new KeyBinding("key.right", 32, "key.categories.movement");
    public KeyBinding keyBindJump = new KeyBinding("key.jump", 57, "key.categories.movement");
    public KeyBinding keyBindSneak = new KeyBinding("key.sneak", 42, "key.categories.movement");
    public KeyBinding keyBindSprint = new KeyBinding("key.sprint", 29, "key.categories.movement");
    public KeyBinding keyBindInventory = new KeyBinding("key.inventory", 18, "key.categories.inventory");
    public KeyBinding keyBindSwapHands = new KeyBinding("key.swapHands", 33, "key.categories.inventory");
    public KeyBinding keyBindDrop = new KeyBinding("key.drop", 16, "key.categories.inventory");
    public KeyBinding keyBindUseItem = new KeyBinding("key.use", -99, "key.categories.gameplay");
    public KeyBinding keyBindAttack = new KeyBinding("key.attack", -100, "key.categories.gameplay");
    public KeyBinding keyBindPickBlock = new KeyBinding("key.pickItem", -98, "key.categories.gameplay");
    public KeyBinding keyBindChat = new KeyBinding("key.chat", 20, "key.categories.multiplayer");
    public KeyBinding keyBindPlayerList = new KeyBinding("key.playerlist", 15, "key.categories.multiplayer");
    public KeyBinding keyBindCommand = new KeyBinding("key.command", 53, "key.categories.multiplayer");
    public KeyBinding keyBindScreenshot = new KeyBinding("key.screenshot", 60, "key.categories.misc");
    public KeyBinding keyBindTogglePerspective = new KeyBinding("key.togglePerspective", 63, "key.categories.misc");
    public KeyBinding keyBindSmoothCamera = new KeyBinding("key.smoothCamera", 0, "key.categories.misc");
    public KeyBinding keyBindFullscreen = new KeyBinding("key.fullscreen", 87, "key.categories.misc");
    public KeyBinding keyBindSpectatorOutlines = new KeyBinding("key.spectatorOutlines", 0, "key.categories.misc");
    public KeyBinding field_194146_ao = new KeyBinding("key.advancements", 38, "key.categories.misc");
    public KeyBinding[] keyBindsHotbar = {new KeyBinding("key.hotbar.1", 2, "key.categories.inventory"), new KeyBinding("key.hotbar.2", 3, "key.categories.inventory"), new KeyBinding("key.hotbar.3", 4, "key.categories.inventory"), new KeyBinding("key.hotbar.4", 5, "key.categories.inventory"), new KeyBinding("key.hotbar.5", 6, "key.categories.inventory"), new KeyBinding("key.hotbar.6", 7, "key.categories.inventory"), new KeyBinding("key.hotbar.7", 8, "key.categories.inventory"), new KeyBinding("key.hotbar.8", 9, "key.categories.inventory"), new KeyBinding("key.hotbar.9", 10, "key.categories.inventory")};
    public KeyBinding field_193629_ap = new KeyBinding("key.saveToolbarActivator", 46, "key.categories.creative");
    public KeyBinding field_193630_aq = new KeyBinding("key.loadToolbarActivator", 45, "key.categories.creative");
    public KeyBinding[] keyBindings;
    private File optionsFile;
    public EnumDifficulty difficulty;
    public boolean hideGUI;
    public int thirdPersonView;
    public boolean showDebugInfo;
    public boolean showDebugProfilerChart;
    public boolean showLagometer;
    public String lastServer;
    public boolean smoothCamera;
    public float fov;
    public float gamma;
    public int scale;
    public int particles;
    public String language;
    public boolean forceUnicodeFont;
    public int ofFogType = 1;
    public float ofFogStart = 0.8F;
    public int ofMipmapType;
    public boolean ofOcclusionFancy;
    public boolean ofSmoothFps;
    public boolean ofSmoothWorld = Config.isSingleProcessor();
    public boolean ofLazyChunkLoading = Config.isSingleProcessor();
    public float ofAoLevel = 1.0F;
    public int ofAaLevel;
    public int ofAfLevel = 1;
    public int ofClouds;
    public float ofCloudsHeight;
    public int ofTrees;
    public int ofRain;
    public int ofDroppedItems;
    public int ofBetterGrass = 3;
    public int ofAutoSaveTicks = 4000;
    public boolean ofLagometer;
    public boolean ofProfiler;
    public boolean ofShowFps;
    public boolean ofWeather = true;
    public boolean ofSky = true;
    public boolean ofStars = true;
    public boolean ofSunMoon = true;
    public int ofVignette;
    public int ofChunkUpdates = 1;
    public boolean ofChunkUpdatesDynamic;
    public int ofTime;
    public boolean ofClearWater;
    public boolean ofBetterSnow;
    public String ofFullscreenMode = "Default";
    public boolean ofSwampColors = true;
    public boolean ofRandomMobs = true;
    public boolean ofSmoothBiomes = true;
    public boolean ofCustomFonts = true;
    public boolean ofCustomColors = true;
    public boolean ofCustomSky = true;
    public boolean ofShowCapes = true;
    public int ofConnectedTextures = 2;
    public boolean ofCustomItems = true;
    public boolean ofNaturalTextures;
    public boolean ofFastRender;
    public int ofTranslucentBlocks;
    public boolean ofDynamicFov = true;
    public boolean ofAlternateBlocks = true;
    public int ofDynamicLights = 3;
    public boolean ofCustomEntityModels = true;
    public boolean ofCustomGuis = true;
    public int ofScreenshotSize = 1;
    public int ofAnimatedWater;
    public int ofAnimatedLava;
    public boolean ofAnimatedFire = true;
    public boolean ofAnimatedPortal = true;
    public boolean ofAnimatedRedstone = true;
    public boolean ofAnimatedExplosion = true;
    public boolean ofAnimatedFlame = true;
    public boolean ofAnimatedSmoke = true;
    public boolean ofVoidParticles = true;
    public boolean ofWaterParticles = true;
    public boolean ofRainSplash = true;
    public boolean ofPortalParticles = true;
    public boolean ofPotionParticles = true;
    public boolean ofFireworkParticles = true;
    public boolean ofDrippingWaterLava = true;
    public boolean ofAnimatedTerrain = true;
    public boolean ofAnimatedTextures = true;
    public static final int DEFAULT = 0;
    public static final int FAST = 1;
    public static final int FANCY = 2;
    public static final int OFF = 3;
    public static final int SMART = 4;
    public static final int ANIM_ON = 0;
    public static final int ANIM_GENERATED = 1;
    public static final int ANIM_OFF = 2;
    public static final String DEFAULT_STR = "Default";
    private static final int[] OF_TREES_VALUES = {0, 1, 4, 2};
    private static final int[] OF_DYNAMIC_LIGHTS = {3, 1, 2};
    private static final String[] KEYS_DYNAMIC_LIGHTS = {"options.off", "options.graphics.fast", "options.graphics.fancy"};
    public KeyBinding ofKeyBindZoom;
    private File optionsFileOF;
    private boolean needsResourceRefresh;

    public GameSettings() {
        keyBindings = ArrayUtils.addAll(new KeyBinding[]{keyBindAttack, keyBindUseItem, keyBindForward, keyBindLeft, keyBindBack, keyBindRight, keyBindJump, keyBindSneak, keyBindSprint, keyBindDrop, keyBindInventory, keyBindChat, keyBindPlayerList, keyBindPickBlock, keyBindCommand, keyBindScreenshot, keyBindTogglePerspective, keyBindSmoothCamera, keyBindFullscreen, keyBindSpectatorOutlines, keyBindSwapHands, field_193629_ap, field_193630_aq, field_194146_ao}, keyBindsHotbar);
        difficulty = EnumDifficulty.NORMAL;
        lastServer = "";
        fov = 70.0F;
        language = "en_us";
        optionsFile = new File(Minecraft.gameDir, "options.txt");
        optionsFileOF = new File(Minecraft.gameDir, "optionsof.txt");
        limitFramerate = (int) GameSettings.Options.FRAMERATE_LIMIT.getValueMax();
        ofKeyBindZoom = new KeyBinding("of.key.zoom", 46, "key.categories.misc");
        keyBindings = ArrayUtils.add(keyBindings, ofKeyBindZoom);
        loadOptions();
        Config.initGameSettings(this);
    }

    /**
     * Gets the display name for a key.
     */
    public static String getKeyDisplayString(int key) {
        if (key < 0) {
            switch (key) {
                case -100:
                    return I18n.format("key.mouse.left");

                case -99:
                    return I18n.format("key.mouse.right");

                case -98:
                    return I18n.format("key.mouse.middle");

                default:
                    return I18n.format("key.mouseButton", key + 101);
            }
        } else {
            return key < 256 ? Keyboard.getKeyName(key) : String.format("%c", (char) (key - 256)).toUpperCase();
        }
    }

    /**
     * Returns whether the specified key binding is currently being pressed.
     */
    public static boolean isKeyDown(KeyBinding key) {
        int i = key.getKeyCode();

        if (i != 0 && i < 256) {
            return i < 0 ? Mouse.isButtonDown(i + 100) : Keyboard.isKeyDown(i);
        } else {
            return false;
        }
    }

    /**
     * Sets a key binding and then saves all settings.
     */
    public void setOptionKeyBinding(KeyBinding key, int keyCode) {
        key.setKeyCode(keyCode);
        saveOptions();
    }

    /**
     * If the specified option is controlled by a slider (float value), this will set the float value.
     */
    public void setOptionFloatValue(GameSettings.Options settingsOption, float value) {
        setOptionFloatValueOF(settingsOption, value);

        if (settingsOption == GameSettings.Options.SENSITIVITY) {
            mouseSensitivity = value;
        }

        if (settingsOption == GameSettings.Options.FOV) {
            fov = value;
        }

        if (settingsOption == GameSettings.Options.GAMMA) {
            gamma = value;
        }

        if (settingsOption == GameSettings.Options.FRAMERATE_LIMIT) {
            limitFramerate = (int) value;
            enableVsync = false;

            if (limitFramerate <= 0) {
                limitFramerate = (int) GameSettings.Options.FRAMERATE_LIMIT.getValueMax();
                enableVsync = true;
            }

            updateVSync();
        }

        if (settingsOption == GameSettings.Options.CHAT_OPACITY) {
            chatOpacity = value;
            Minecraft.ingameGUI.getChatGUI().refreshChat();
        }

        if (settingsOption == GameSettings.Options.CHAT_HEIGHT_FOCUSED) {
            chatHeightFocused = value;
            Minecraft.ingameGUI.getChatGUI().refreshChat();
        }

        if (settingsOption == GameSettings.Options.CHAT_HEIGHT_UNFOCUSED) {
            chatHeightUnfocused = value;
            Minecraft.ingameGUI.getChatGUI().refreshChat();
        }

        if (settingsOption == GameSettings.Options.CHAT_WIDTH) {
            chatWidth = value;
            Minecraft.ingameGUI.getChatGUI().refreshChat();
        }

        if (settingsOption == GameSettings.Options.CHAT_SCALE) {
            chatScale = value;
            Minecraft.ingameGUI.getChatGUI().refreshChat();
        }

        if (settingsOption == GameSettings.Options.MIPMAP_LEVELS) {
            int i = mipmapLevels;
            mipmapLevels = (int) value;

            if ((float) i != value) {
                Minecraft.getTextureMapBlocks().setMipmapLevels(mipmapLevels);
                Minecraft.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
                Minecraft.getTextureMapBlocks().setBlurMipmapDirect(false, mipmapLevels > 0);
                Minecraft.refreshResources();
            }
        }

        if (settingsOption == GameSettings.Options.RENDER_DISTANCE) {
            renderDistance = (int) value;
            Minecraft.renderGlobal.setDisplayListEntitiesDirty();
        }
    }

    /**
     * For non-float options. Toggles the option on/off, or cycles through the list i.e. render distances.
     */
    public void setOptionValue(GameSettings.Options settingsOption, int value) {
        setOptionValueOF(settingsOption, value);

        if (settingsOption == GameSettings.Options.RENDER_DISTANCE) {
            setOptionFloatValue(settingsOption, MathHelper.clamp((float) (renderDistance + value), settingsOption.getValueMin(), settingsOption.getValueMax()));
        }

        if (settingsOption == GameSettings.Options.MAIN_HAND) {
            mainHand = mainHand.opposite();
        }

        if (settingsOption == GameSettings.Options.INVERT_MOUSE) {
            invertMouse = !invertMouse;
        }

        if (settingsOption == GameSettings.Options.GUI_SCALE) {
            scale += value;

            if (Screen.hasShiftDown()) {
                scale = 0;
            }

            DisplayMode displaymode = Config.getLargestDisplayMode();
            int i = displaymode.getWidth() / 320;
            int j = displaymode.getHeight() / 240;
            int k = Math.min(i, j);

            if (scale < 0) {
                scale = k - 1;
            }

            if (scale < 0 || scale >= k) {
                scale = 0;
            }
        }

        if (settingsOption == GameSettings.Options.PARTICLES) {
            particles = (particles + value) % 3;
        }

        if (settingsOption == GameSettings.Options.VIEW_BOBBING) {
            viewBobbing = !viewBobbing;
        }

        if (settingsOption == GameSettings.Options.RENDER_CLOUDS) {
            clouds = (clouds + value) % 3;
        }

        if (settingsOption == GameSettings.Options.FORCE_UNICODE_FONT) {
            forceUnicodeFont = !forceUnicodeFont;
            Minecraft.font.setUnicodeFlag(Minecraft.getLanguageManager().isCurrentLocaleUnicode() || forceUnicodeFont);
        }

        if (settingsOption == GameSettings.Options.FBO_ENABLE) {
            fboEnable = !fboEnable;
        }

        if (settingsOption == GameSettings.Options.ANAGLYPH) {
            if (!anaglyph && Config.isShaders()) {
                Screen current = Minecraft.screen;
                Minecraft.openScreen(new AlertScreen(new TranslatableComponent("of.message.an.shaders1"), new TranslatableComponent("of.message.an.shaders2"), new TranslatableComponent("gui.cancel"), () -> Minecraft.openScreen(current)));
                return;
            }

            anaglyph = !anaglyph;
            Minecraft.refreshResources();
        }

        if (settingsOption == GameSettings.Options.GRAPHICS) {
            fancyGraphics = !fancyGraphics;
            updateRenderClouds();
            Minecraft.renderGlobal.loadRenderers();
        }

        if (settingsOption == GameSettings.Options.AMBIENT_OCCLUSION) {
            ambientOcclusion = (ambientOcclusion + value) % 3;
            Minecraft.renderGlobal.loadRenderers();
        }

        if (settingsOption == GameSettings.Options.CHAT_VISIBILITY) {
            chatVisibility = EntityPlayer.EnumChatVisibility.getEnumChatVisibility((chatVisibility.getChatVisibility() + value) % 3);
        }

        if (settingsOption == GameSettings.Options.CHAT_COLOR) {
            chatColours = !chatColours;
        }

        if (settingsOption == GameSettings.Options.CHAT_LINKS) {
            chatLinks = !chatLinks;
        }

        if (settingsOption == GameSettings.Options.CHAT_LINKS_PROMPT) {
            chatLinksPrompt = !chatLinksPrompt;
        }

        if (settingsOption == GameSettings.Options.USE_FULLSCREEN) {
            fullScreen = !fullScreen;

            if (Minecraft.isFullscreen() != fullScreen) {
                Minecraft.toggleFullscreen();
            }
        }

        if (settingsOption == GameSettings.Options.ENABLE_VSYNC) {
            enableVsync = !enableVsync;
            Display.setVSyncEnabled(enableVsync);
        }

        if (settingsOption == GameSettings.Options.USE_VBO) {
            useVbo = !useVbo;
            Minecraft.renderGlobal.loadRenderers();
        }

        if (settingsOption == GameSettings.Options.REDUCED_DEBUG_INFO) {
            reducedDebugInfo = !reducedDebugInfo;
        }

        if (settingsOption == GameSettings.Options.ENTITY_SHADOWS) {
            entityShadows = !entityShadows;
        }

        if (settingsOption == GameSettings.Options.ATTACK_INDICATOR) {
            attackIndicator = (attackIndicator + value) % 3;
        }

        if (settingsOption == GameSettings.Options.SHOW_SUBTITLES) {
            showSubtitles = !showSubtitles;
        }

        if (settingsOption == GameSettings.Options.AUTO_JUMP) {
            autoJump = !autoJump;
        }

        saveOptions();
    }

    public float getOptionFloatValue(GameSettings.Options settingOption) {
        float f = getOptionFloatValueOF(settingOption);

        if (f != Float.MAX_VALUE) {
            return f;
        } else if (settingOption == GameSettings.Options.FOV) {
            return fov;
        } else if (settingOption == GameSettings.Options.GAMMA) {
            return gamma;
        }else if (settingOption == GameSettings.Options.SENSITIVITY) {
            return mouseSensitivity;
        } else if (settingOption == GameSettings.Options.CHAT_OPACITY) {
            return chatOpacity;
        } else if (settingOption == GameSettings.Options.CHAT_HEIGHT_FOCUSED) {
            return chatHeightFocused;
        } else if (settingOption == GameSettings.Options.CHAT_HEIGHT_UNFOCUSED) {
            return chatHeightUnfocused;
        } else if (settingOption == GameSettings.Options.CHAT_SCALE) {
            return chatScale;
        } else if (settingOption == GameSettings.Options.CHAT_WIDTH) {
            return chatWidth;
        } else if (settingOption == GameSettings.Options.FRAMERATE_LIMIT) {
            return (float) limitFramerate;
        } else if (settingOption == GameSettings.Options.MIPMAP_LEVELS) {
            return (float) mipmapLevels;
        } else {
            return settingOption == GameSettings.Options.RENDER_DISTANCE ? (float) renderDistance : 0.0F;
        }
    }

    public boolean getOptionOrdinalValue(GameSettings.Options settingOption) {
        switch (settingOption) {
            case INVERT_MOUSE:
                return invertMouse;

            case VIEW_BOBBING:
                return viewBobbing;

            case ANAGLYPH:
                return anaglyph;

            case FBO_ENABLE:
                return fboEnable;

            case CHAT_COLOR:
                return chatColours;

            case CHAT_LINKS:
                return chatLinks;

            case CHAT_LINKS_PROMPT:
                return chatLinksPrompt;

            case USE_FULLSCREEN:
                return fullScreen;

            case ENABLE_VSYNC:
                return enableVsync;

            case USE_VBO:
                return useVbo;

            case FORCE_UNICODE_FONT:
                return forceUnicodeFont;

            case REDUCED_DEBUG_INFO:
                return reducedDebugInfo;

            case ENTITY_SHADOWS:
                return entityShadows;

            case SHOW_SUBTITLES:
                return showSubtitles;

            case AUTO_JUMP:
                return autoJump;

            default:
                return false;
        }
    }

    /**
     * Returns the translation of the given index in the given String array. If the index is smaller than 0 or greater
     * than/equal to the length of the String array, it is changed to 0.
     */
    private static String getTranslation(String[] strArray, int index) {
        if (index < 0 || index >= strArray.length) {
            index = 0;
        }

        return I18n.format(strArray[index]);
    }

    /**
     * Gets a key binding.
     */
    public String getKeyBinding(GameSettings.Options settingOption) {
        String s = getKeyBindingOF(settingOption);

        if (s != null) {
            return s;
        } else {
            String s1 = I18n.format(settingOption.getEnumString()) + ": ";

            if (settingOption.getEnumFloat()) {
                float f1 = getOptionFloatValue(settingOption);
                float f = settingOption.normalizeValue(f1);

                if (settingOption == GameSettings.Options.SENSITIVITY) {
                    if (f == 0.0F) {
                        return s1 + I18n.format("options.sensitivity.min");
                    } else {
                        return f == 1.0F ? s1 + I18n.format("options.sensitivity.max") : s1 + (int) (f * 200.0F) + "%";
                    }
                } else if (settingOption == GameSettings.Options.FOV) {
                    if (f1 == 70.0F) {
                        return s1 + I18n.format("options.fov.min");
                    } else {
                        return f1 == 110.0F ? s1 + I18n.format("options.fov.max") : s1 + (int) f1;
                    }
                } else if (settingOption == GameSettings.Options.FRAMERATE_LIMIT) {
                    return f1 == settingOption.valueMax ? s1 + I18n.format("options.framerateLimit.max") : s1 + I18n.format("options.framerate", (int) f1);
                } else if (settingOption == GameSettings.Options.RENDER_CLOUDS) {
                    return f1 == settingOption.valueMin ? s1 + I18n.format("options.cloudHeight.min") : s1 + ((int) f1 + 128);
                } else if (settingOption == GameSettings.Options.GAMMA) {
                    if (f == 0.0F) {
                        return s1 + I18n.format("options.gamma.min");
                    } else {
                        return f == 1.0F ? s1 + I18n.format("options.gamma.max") : s1 + "+" + (int) (f * 100.0F) + "%";
                    }
                } else if (settingOption == GameSettings.Options.CHAT_OPACITY) {
                    return s1 + (int) (f * 90.0F + 10.0F) + "%";
                } else if (settingOption == GameSettings.Options.CHAT_HEIGHT_UNFOCUSED) {
                    return s1 + ChatHud.calculateChatboxHeight(f) + "px";
                } else if (settingOption == GameSettings.Options.CHAT_HEIGHT_FOCUSED) {
                    return s1 + ChatHud.calculateChatboxHeight(f) + "px";
                } else if (settingOption == GameSettings.Options.CHAT_WIDTH) {
                    return s1 + ChatHud.calculateChatboxWidth(f) + "px";
                } else if (settingOption == GameSettings.Options.RENDER_DISTANCE) {
                    return s1 + I18n.format("options.chunks", (int) f1);
                } else if (settingOption == GameSettings.Options.MIPMAP_LEVELS) {
                    return f1 == 0.0F ? s1 + I18n.format("options.off") : s1 + (int) f1;
                } else {
                    return f == 0.0F ? s1 + I18n.format("options.off") : s1 + (int) (f * 100.0F) + "%";
                }
            } else if (settingOption.getEnumBoolean()) {
                boolean flag = getOptionOrdinalValue(settingOption);
                return flag ? s1 + I18n.format("options.on") : s1 + I18n.format("options.off");
            } else if (settingOption == GameSettings.Options.MAIN_HAND) {
                return s1 + mainHand;
            } else if (settingOption == GameSettings.Options.GUI_SCALE) {
                return scale >= SCALES.length ? s1 + scale + "x" : s1 + getTranslation(SCALES, scale);
            } else if (settingOption == GameSettings.Options.CHAT_VISIBILITY) {
                return s1 + I18n.format(chatVisibility.getResourceKey());
            } else if (settingOption == GameSettings.Options.PARTICLES) {
                return s1 + getTranslation(PARTICLES, particles);
            } else if (settingOption == GameSettings.Options.AMBIENT_OCCLUSION) {
                return s1 + getTranslation(AMBIENT_OCCLUSIONS, ambientOcclusion);
            } else if (settingOption == GameSettings.Options.RENDER_CLOUDS) {
                return s1 + getTranslation(CLOUDS_TYPES, clouds);
            } else if (settingOption == GameSettings.Options.GRAPHICS) {
                if (fancyGraphics) {
                    return s1 + I18n.format("options.graphics.fancy");
                } else {
                    String s2 = "options.graphics.fast";
                    return s1 + I18n.format("options.graphics.fast");
                }
            } else if (settingOption == GameSettings.Options.ATTACK_INDICATOR) {
                return s1 + getTranslation(ATTACK_INDICATORS, attackIndicator);
            } else {
                return s1;
            }
        }
    }

    /**
     * Loads the options from the options file. It appears that this has replaced the previous 'loadOptions'
     */
    public void loadOptions() {
        try {
            if (!optionsFile.exists()) {
                return;
            }

            soundLevels.clear();
            List<String> list = IOUtils.readLines(new FileInputStream(optionsFile), StandardCharsets.UTF_8);
            NBTTagCompound nbttagcompound = new NBTTagCompound();

            for (String s : list) {
                try {
                    Iterator<String> iterator = COLON_SPLITTER.omitEmptyStrings().limit(2).split(s).iterator();
                    nbttagcompound.setString(iterator.next(), iterator.next());
                } catch (Exception var12) {
                }
            }

            nbttagcompound = dataFix(nbttagcompound);

            for (String s1 : nbttagcompound.getKeySet()) {
                String s2 = nbttagcompound.getString(s1);

                try {
                    if ("mouseSensitivity".equals(s1)) {
                        mouseSensitivity = parseFloat(s2);
                    }

                    if ("fov".equals(s1)) {
                        fov = parseFloat(s2) * 40.0F + 70.0F;
                    }

                    if ("gamma".equals(s1)) {
                        gamma = parseFloat(s2);
                    }

                    if ("invertYMouse".equals(s1)) {
                        invertMouse = "true".equals(s2);
                    }

                    if ("renderDistance".equals(s1)) {
                        renderDistance = Integer.parseInt(s2);
                    }

                    if ("guiScale".equals(s1)) {
                        scale = Integer.parseInt(s2);
                    }

                    if ("particles".equals(s1)) {
                        particles = Integer.parseInt(s2);
                    }

                    if ("bobView".equals(s1)) {
                        viewBobbing = "true".equals(s2);
                    }

                    if ("anaglyph3d".equals(s1)) {
                        anaglyph = "true".equals(s2);
                    }

                    if ("maxFps".equals(s1)) {
                        limitFramerate = Integer.parseInt(s2);

                        if (enableVsync) {
                            limitFramerate = (int) GameSettings.Options.FRAMERATE_LIMIT.getValueMax();
                        }

                        if (limitFramerate <= 0) {
                            limitFramerate = (int) GameSettings.Options.FRAMERATE_LIMIT.getValueMax();
                        }
                    }

                    if ("fboEnable".equals(s1)) {
                        fboEnable = "true".equals(s2);
                    }

                    if ("difficulty".equals(s1)) {
                        difficulty = EnumDifficulty.getDifficultyEnum(Integer.parseInt(s2));
                    }

                    if ("fancyGraphics".equals(s1)) {
                        fancyGraphics = "true".equals(s2);
                        updateRenderClouds();
                    }

                    if ("ao".equals(s1)) {
                        if ("true".equals(s2)) {
                            ambientOcclusion = 2;
                        } else if ("false".equals(s2)) {
                            ambientOcclusion = 0;
                        } else {
                            ambientOcclusion = Integer.parseInt(s2);
                        }
                    }

                    if ("renderClouds".equals(s1)) {
                        if ("true".equals(s2)) {
                            clouds = 2;
                        } else if ("false".equals(s2)) {
                            clouds = 0;
                        } else if ("fast".equals(s2)) {
                            clouds = 1;
                        }
                    }

                    if ("attackIndicator".equals(s1)) {
                        if ("0".equals(s2)) {
                            attackIndicator = 0;
                        } else if ("1".equals(s2)) {
                            attackIndicator = 1;
                        } else if ("2".equals(s2)) {
                            attackIndicator = 2;
                        }
                    }

                    if ("resourcePacks".equals(s1)) {
                        resourcePacks = JsonUtils.func_193840_a(GSON, s2, TYPE_LIST_STRING);

                        if (resourcePacks == null) {
                            resourcePacks = Lists.newArrayList();
                        }
                    }

                    if ("incompatibleResourcePacks".equals(s1)) {
                        incompatibleResourcePacks = JsonUtils.func_193840_a(GSON, s2, TYPE_LIST_STRING);

                        if (incompatibleResourcePacks == null) {
                            incompatibleResourcePacks = Lists.newArrayList();
                        }
                    }

                    if ("lastServer".equals(s1)) {
                        lastServer = s2;
                    }

                    if ("lang".equals(s1)) {
                        language = s2;
                    }

                    if ("chatVisibility".equals(s1)) {
                        chatVisibility = EntityPlayer.EnumChatVisibility.getEnumChatVisibility(Integer.parseInt(s2));
                    }

                    if ("chatColors".equals(s1)) {
                        chatColours = "true".equals(s2);
                    }

                    if ("chatLinks".equals(s1)) {
                        chatLinks = "true".equals(s2);
                    }

                    if ("chatLinksPrompt".equals(s1)) {
                        chatLinksPrompt = "true".equals(s2);
                    }

                    if ("chatOpacity".equals(s1)) {
                        chatOpacity = parseFloat(s2);
                    }

                    if ("fullscreen".equals(s1)) {
                        fullScreen = "true".equals(s2);
                    }

                    if ("enableVsync".equals(s1)) {
                        enableVsync = "true".equals(s2);

                        if (enableVsync) {
                            limitFramerate = (int) GameSettings.Options.FRAMERATE_LIMIT.getValueMax();
                        }

                        updateVSync();
                    }

                    if ("useVbo".equals(s1)) {
                        useVbo = "true".equals(s2);
                    }

                    if ("advancedItemTooltips".equals(s1)) {
                        advancedItemTooltips = "true".equals(s2);
                    }

                    if ("pauseOnLostFocus".equals(s1)) {
                        pauseOnLostFocus = "true".equals(s2);
                    }

                    if ("overrideHeight".equals(s1)) {
                        overrideHeight = Integer.parseInt(s2);
                    }

                    if ("overrideWidth".equals(s1)) {
                        overrideWidth = Integer.parseInt(s2);
                    }

                    if ("heldItemTooltips".equals(s1)) {
                        heldItemTooltips = "true".equals(s2);
                    }

                    if ("chatHeightFocused".equals(s1)) {
                        chatHeightFocused = parseFloat(s2);
                    }

                    if ("chatHeightUnfocused".equals(s1)) {
                        chatHeightUnfocused = parseFloat(s2);
                    }

                    if ("chatScale".equals(s1)) {
                        chatScale = parseFloat(s2);
                    }

                    if ("chatWidth".equals(s1)) {
                        chatWidth = parseFloat(s2);
                    }

                    if ("mipmapLevels".equals(s1)) {
                        mipmapLevels = Integer.parseInt(s2);
                    }

                    if ("forceUnicodeFont".equals(s1)) {
                        forceUnicodeFont = "true".equals(s2);
                    }

                    if ("reducedDebugInfo".equals(s1)) {
                        reducedDebugInfo = "true".equals(s2);
                    }

                    if ("useNativeTransport".equals(s1)) {
                        useNativeTransport = "true".equals(s2);
                    }

                    if ("entityShadows".equals(s1)) {
                        entityShadows = "true".equals(s2);
                    }

                    if ("mainHand".equals(s1)) {
                        mainHand = "left".equals(s2) ? EnumHandSide.LEFT : EnumHandSide.RIGHT;
                    }

                    if ("showSubtitles".equals(s1)) {
                        showSubtitles = "true".equals(s2);
                    }

                    if ("autoJump".equals(s1)) {
                        autoJump = "true".equals(s2);
                    }

                    for (KeyBinding keybinding : keyBindings) {
                        if (s1.equals("key_" + keybinding.getKeyDescription())) {
                            if (Reflector.KeyModifier_valueFromString.exists()) {
                                if (s2.indexOf(58) != -1) {
                                    String[] astring = s2.split(":");
                                    Object object = Reflector.call(Reflector.KeyModifier_valueFromString, astring[1]);
                                    Reflector.call(keybinding, Reflector.ForgeKeyBinding_setKeyModifierAndCode, object, Integer.parseInt(astring[0]));
                                } else {
                                    Object object1 = Reflector.getFieldValue(Reflector.KeyModifier_NONE);
                                    Reflector.call(keybinding, Reflector.ForgeKeyBinding_setKeyModifierAndCode, object1, Integer.parseInt(s2));
                                }
                            } else {
                                keybinding.setKeyCode(Integer.parseInt(s2));
                            }
                        }
                    }

                    for (SoundCategory soundcategory : SoundCategory.values()) {
                        if (s1.equals("soundCategory_" + soundcategory.getName())) {
                            soundLevels.put(soundcategory, Float.valueOf(parseFloat(s2)));
                        }
                    }

                    for (EnumPlayerModelParts enumplayermodelparts : EnumPlayerModelParts.values()) {
                        if (s1.equals("modelPart_" + enumplayermodelparts.getPartName())) {
                            setModelPartEnabled(enumplayermodelparts, "true".equals(s2));
                        }
                    }
                } catch (Exception exception1) {
                }
            }

            KeyBinding.resetKeyBindingArrayAndHash();
        } catch (Exception exception1) {
        }

        loadOfOptions();
    }

    private NBTTagCompound dataFix(NBTTagCompound p_189988_1_) {
        int i = 0;

        try {
            i = Integer.parseInt(p_189988_1_.getString("version"));
        } catch (RuntimeException var4) {
        }

        return Minecraft.getDataFixer().process(FixTypes.OPTIONS, p_189988_1_, i);
    }

    /**
     * Parses a string into a float.
     */
    private float parseFloat(String str) {
        if ("true".equals(str)) {
            return 1.0F;
        } else {
            return "false".equals(str) ? 0.0F : Float.parseFloat(str);
        }
    }

    /**
     * Saves the options to the options file.
     */
    public void saveOptions() {
        try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(optionsFile), StandardCharsets.UTF_8))) {
            pw.println("version:1343");
            pw.println("invertYMouse:" + invertMouse);
            pw.println("mouseSensitivity:" + mouseSensitivity);
            pw.println("fov:" + (fov - 70.0F) / 40.0F);
            pw.println("gamma:" + gamma);
            pw.println("renderDistance:" + renderDistance);
            pw.println("guiScale:" + scale);
            pw.println("particles:" + particles);
            pw.println("bobView:" + viewBobbing);
            pw.println("anaglyph3d:" + anaglyph);
            pw.println("maxFps:" + limitFramerate);
            pw.println("fboEnable:" + fboEnable);
            pw.println("difficulty:" + difficulty.getDifficultyId());
            pw.println("fancyGraphics:" + fancyGraphics);
            pw.println("ao:" + ambientOcclusion);
            if (clouds == 0) pw.println("renderClouds:false");
            else if (clouds == 1) pw.println("renderClouds:fast");
            else pw.println("renderClouds:true");
            pw.println("resourcePacks:" + GSON.toJson(resourcePacks));
            pw.println("incompatibleResourcePacks:" + GSON.toJson(incompatibleResourcePacks));
            pw.println("lastServer:" + lastServer);
            pw.println("lang:" + language);
            pw.println("chatVisibility:" + chatVisibility.getChatVisibility());
            pw.println("chatColors:" + chatColours);
            pw.println("chatLinks:" + chatLinks);
            pw.println("chatLinksPrompt:" + chatLinksPrompt);
            pw.println("chatOpacity:" + chatOpacity);
            pw.println("fullscreen:" + fullScreen);
            pw.println("enableVsync:" + enableVsync);
            pw.println("useVbo:" + useVbo);
            pw.println("advancedItemTooltips:" + advancedItemTooltips);
            pw.println("pauseOnLostFocus:" + pauseOnLostFocus);
            pw.println("overrideWidth:" + overrideWidth);
            pw.println("overrideHeight:" + overrideHeight);
            pw.println("heldItemTooltips:" + heldItemTooltips);
            pw.println("chatHeightFocused:" + chatHeightFocused);
            pw.println("chatHeightUnfocused:" + chatHeightUnfocused);
            pw.println("chatScale:" + chatScale);
            pw.println("chatWidth:" + chatWidth);
            pw.println("mipmapLevels:" + mipmapLevels);
            pw.println("forceUnicodeFont:" + forceUnicodeFont);
            pw.println("reducedDebugInfo:" + reducedDebugInfo);
            pw.println("useNativeTransport:" + useNativeTransport);
            pw.println("entityShadows:" + entityShadows);
            pw.println("mainHand:" + (mainHand == EnumHandSide.LEFT ? "left" : "right"));
            pw.println("attackIndicator:" + attackIndicator);
            pw.println("showSubtitles:" + showSubtitles);
            pw.println("autoJump:" + autoJump);
            for (KeyBinding keybinding : keyBindings) {
                pw.println("key_" + keybinding.getKeyDescription() + ":" + keybinding.getKeyCode());
            }
            for (SoundCategory soundcategory : SoundCategory.values()) {
                pw.println("soundCategory_" + soundcategory.getName() + ":" + getSoundLevel(soundcategory));
            }
            for (EnumPlayerModelParts enumplayermodelparts : EnumPlayerModelParts.values()) {
                pw.println("modelPart_" + enumplayermodelparts.getPartName() + ":" + setModelParts.contains(enumplayermodelparts));
            }
        } catch (Exception e) {
            LOGGER.error("Failed to save options", e);
        }
        saveOfOptions();
        sendSettingsToServer();
    }

    public float getSoundLevel(SoundCategory category) {
        return soundLevels.getOrDefault(category, 1.0F);
    }

    public void setSoundLevel(SoundCategory category, float volume) {
        Minecraft.getSoundHandler().setSoundLevel(category, volume);
        soundLevels.put(category, volume);
    }

    /**
     * Send a client info packet with settings information to the server
     */
    public void sendSettingsToServer() {
        if (Minecraft.player != null) {
            int i = 0;
            for (EnumPlayerModelParts enumplayermodelparts : setModelParts) {
                i |= enumplayermodelparts.getPartMask();
            }
            Minecraft.player.connection.sendPacket(new CPacketClientSettings(language, renderDistance, chatVisibility, chatColours, i, mainHand));
        }
    }

    public Set<EnumPlayerModelParts> getModelParts() {
        return ImmutableSet.copyOf(setModelParts);
    }

    public void setModelPartEnabled(EnumPlayerModelParts modelPart, boolean enable) {
        if (enable) {
            setModelParts.add(modelPart);
        } else {
            setModelParts.remove(modelPart);
        }

        sendSettingsToServer();
    }

    public void switchModelPartEnabled(EnumPlayerModelParts modelPart) {
        if (getModelParts().contains(modelPart)) {
            setModelParts.remove(modelPart);
        } else {
            setModelParts.add(modelPart);
        }

        sendSettingsToServer();
    }

    /**
     * Return true if the client connect to a server using the native transport system
     */
    public boolean isUsingNativeTransport() {
        return useNativeTransport;
    }

    private void setOptionFloatValueOF(GameSettings.Options p_setOptionFloatValueOF_1_, float p_setOptionFloatValueOF_2_) {
        if (p_setOptionFloatValueOF_1_ == GameSettings.Options.CLOUD_HEIGHT) {
            ofCloudsHeight = p_setOptionFloatValueOF_2_;
            Minecraft.renderGlobal.resetClouds();
        }

        if (p_setOptionFloatValueOF_1_ == GameSettings.Options.AO_LEVEL) {
            ofAoLevel = p_setOptionFloatValueOF_2_;
            Minecraft.renderGlobal.loadRenderers();
        }

        if (p_setOptionFloatValueOF_1_ == GameSettings.Options.AA_LEVEL) {
            int i = (int) p_setOptionFloatValueOF_2_;

            if (i > 0 && Config.isShaders()) {
                Screen current = Minecraft.screen;
                Minecraft.openScreen(new AlertScreen(new TranslatableComponent("of.message.aa.shaders1"), new TranslatableComponent("of.message.aa.shaders2"), new TranslatableComponent("gui.cancel"), () -> Minecraft.openScreen(current)));
                return;
            }

            int[] aint = {0, 2, 4, 6, 8, 12, 16};
            ofAaLevel = 0;

            for (int j = 0; j < aint.length; ++j) {
                if (i >= aint[j]) {
                    ofAaLevel = aint[j];
                }
            }

            ofAaLevel = Config.limit(ofAaLevel, 0, 16);
        }

        if (p_setOptionFloatValueOF_1_ == GameSettings.Options.AF_LEVEL) {
            int k = (int) p_setOptionFloatValueOF_2_;

            if (k > 1 && Config.isShaders()) {
                Screen current = Minecraft.screen;
                Minecraft.openScreen(new AlertScreen(new TranslatableComponent("of.message.af.shaders1"), new TranslatableComponent("of.message.af.shaders2"), new TranslatableComponent("gui.cancel"), () -> Minecraft.openScreen(current)));
                return;
            }

            for (ofAfLevel = 1; ofAfLevel * 2 <= k; ofAfLevel *= 2) {
            }

            ofAfLevel = Config.limit(ofAfLevel, 1, 16);
            Minecraft.refreshResources();
        }

        if (p_setOptionFloatValueOF_1_ == GameSettings.Options.MIPMAP_TYPE) {
            int l = (int) p_setOptionFloatValueOF_2_;
            ofMipmapType = Config.limit(l, 0, 3);
            Minecraft.refreshResources();
        }

        if (p_setOptionFloatValueOF_1_ == GameSettings.Options.FULLSCREEN_MODE) {
            int i1 = (int) p_setOptionFloatValueOF_2_ - 1;
            String[] astring = Config.getDisplayModeNames();

            if (i1 < 0 || i1 >= astring.length) {
                ofFullscreenMode = "Default";
                return;
            }

            ofFullscreenMode = astring[i1];
        }
    }

    private float getOptionFloatValueOF(GameSettings.Options p_getOptionFloatValueOF_1_) {
        if (p_getOptionFloatValueOF_1_ == GameSettings.Options.CLOUD_HEIGHT) {
            return ofCloudsHeight;
        } else if (p_getOptionFloatValueOF_1_ == GameSettings.Options.AO_LEVEL) {
            return ofAoLevel;
        } else if (p_getOptionFloatValueOF_1_ == GameSettings.Options.AA_LEVEL) {
            return (float) ofAaLevel;
        } else if (p_getOptionFloatValueOF_1_ == GameSettings.Options.AF_LEVEL) {
            return (float) ofAfLevel;
        } else if (p_getOptionFloatValueOF_1_ == GameSettings.Options.MIPMAP_TYPE) {
            return (float) ofMipmapType;
        } else if (p_getOptionFloatValueOF_1_ == GameSettings.Options.FRAMERATE_LIMIT) {
            return (float) limitFramerate == GameSettings.Options.FRAMERATE_LIMIT.getValueMax() && enableVsync ? 0.0F : (float) limitFramerate;
        } else if (p_getOptionFloatValueOF_1_ == GameSettings.Options.FULLSCREEN_MODE) {
            if (ofFullscreenMode.equals("Default")) {
                return 0.0F;
            } else {
                List list = Arrays.asList(Config.getDisplayModeNames());
                int i = list.indexOf(ofFullscreenMode);
                return i < 0 ? 0.0F : (float) (i + 1);
            }
        } else {
            return Float.MAX_VALUE;
        }
    }

    private void setOptionValueOF(GameSettings.Options p_setOptionValueOF_1_, int p_setOptionValueOF_2_) {
        if (p_setOptionValueOF_1_ == GameSettings.Options.FOG_FANCY) {
            switch (ofFogType) {
                case 1:
                    ofFogType = 2;

                    if (!Config.isFancyFogAvailable()) {
                        ofFogType = 3;
                    }

                    break;

                case 2:
                    ofFogType = 3;
                    break;

                case 3:
                    ofFogType = 1;
                    break;

                default:
                    ofFogType = 1;
            }
        }

        if (p_setOptionValueOF_1_ == GameSettings.Options.FOG_START) {
            ofFogStart += 0.2F;

            if (ofFogStart > 0.81F) {
                ofFogStart = 0.2F;
            }
        }

        if (p_setOptionValueOF_1_ == GameSettings.Options.SMOOTH_FPS) {
            ofSmoothFps = !ofSmoothFps;
        }

        if (p_setOptionValueOF_1_ == GameSettings.Options.SMOOTH_WORLD) {
            ofSmoothWorld = !ofSmoothWorld;
            Config.updateThreadPriorities();
        }

        if (p_setOptionValueOF_1_ == GameSettings.Options.CLOUDS) {
            ++ofClouds;

            if (ofClouds > 3) {
                ofClouds = 0;
            }

            updateRenderClouds();
            Minecraft.renderGlobal.resetClouds();
        }

        if (p_setOptionValueOF_1_ == GameSettings.Options.TREES) {
            ofTrees = nextValue(ofTrees, OF_TREES_VALUES);
            Minecraft.renderGlobal.loadRenderers();
        }

        if (p_setOptionValueOF_1_ == GameSettings.Options.DROPPED_ITEMS) {
            ++ofDroppedItems;

            if (ofDroppedItems > 2) {
                ofDroppedItems = 0;
            }
        }

        if (p_setOptionValueOF_1_ == GameSettings.Options.RAIN) {
            ++ofRain;

            if (ofRain > 3) {
                ofRain = 0;
            }
        }

        if (p_setOptionValueOF_1_ == GameSettings.Options.ANIMATED_WATER) {
            ++ofAnimatedWater;

            if (ofAnimatedWater == 1) {
                ++ofAnimatedWater;
            }

            if (ofAnimatedWater > 2) {
                ofAnimatedWater = 0;
            }
        }

        if (p_setOptionValueOF_1_ == GameSettings.Options.ANIMATED_LAVA) {
            ++ofAnimatedLava;

            if (ofAnimatedLava == 1) {
                ++ofAnimatedLava;
            }

            if (ofAnimatedLava > 2) {
                ofAnimatedLava = 0;
            }
        }

        if (p_setOptionValueOF_1_ == GameSettings.Options.ANIMATED_FIRE) {
            ofAnimatedFire = !ofAnimatedFire;
        }

        if (p_setOptionValueOF_1_ == GameSettings.Options.ANIMATED_PORTAL) {
            ofAnimatedPortal = !ofAnimatedPortal;
        }

        if (p_setOptionValueOF_1_ == GameSettings.Options.ANIMATED_REDSTONE) {
            ofAnimatedRedstone = !ofAnimatedRedstone;
        }

        if (p_setOptionValueOF_1_ == GameSettings.Options.ANIMATED_EXPLOSION) {
            ofAnimatedExplosion = !ofAnimatedExplosion;
        }

        if (p_setOptionValueOF_1_ == GameSettings.Options.ANIMATED_FLAME) {
            ofAnimatedFlame = !ofAnimatedFlame;
        }

        if (p_setOptionValueOF_1_ == GameSettings.Options.ANIMATED_SMOKE) {
            ofAnimatedSmoke = !ofAnimatedSmoke;
        }

        if (p_setOptionValueOF_1_ == GameSettings.Options.VOID_PARTICLES) {
            ofVoidParticles = !ofVoidParticles;
        }

        if (p_setOptionValueOF_1_ == GameSettings.Options.WATER_PARTICLES) {
            ofWaterParticles = !ofWaterParticles;
        }

        if (p_setOptionValueOF_1_ == GameSettings.Options.PORTAL_PARTICLES) {
            ofPortalParticles = !ofPortalParticles;
        }

        if (p_setOptionValueOF_1_ == GameSettings.Options.POTION_PARTICLES) {
            ofPotionParticles = !ofPotionParticles;
        }

        if (p_setOptionValueOF_1_ == GameSettings.Options.FIREWORK_PARTICLES) {
            ofFireworkParticles = !ofFireworkParticles;
        }

        if (p_setOptionValueOF_1_ == GameSettings.Options.DRIPPING_WATER_LAVA) {
            ofDrippingWaterLava = !ofDrippingWaterLava;
        }

        if (p_setOptionValueOF_1_ == GameSettings.Options.ANIMATED_TERRAIN) {
            ofAnimatedTerrain = !ofAnimatedTerrain;
        }

        if (p_setOptionValueOF_1_ == GameSettings.Options.ANIMATED_TEXTURES) {
            ofAnimatedTextures = !ofAnimatedTextures;
        }

        if (p_setOptionValueOF_1_ == GameSettings.Options.RAIN_SPLASH) {
            ofRainSplash = !ofRainSplash;
        }

        if (p_setOptionValueOF_1_ == GameSettings.Options.LAGOMETER) {
            ofLagometer = !ofLagometer;
        }

        if (p_setOptionValueOF_1_ == GameSettings.Options.SHOW_FPS) {
            ofShowFps = !ofShowFps;
        }

        if (p_setOptionValueOF_1_ == GameSettings.Options.AUTOSAVE_TICKS) {
            ofAutoSaveTicks *= 10;

            if (ofAutoSaveTicks > 40000) {
                ofAutoSaveTicks = 40;
            }
        }

        if (p_setOptionValueOF_1_ == GameSettings.Options.BETTER_GRASS) {
            ++ofBetterGrass;

            if (ofBetterGrass > 3) {
                ofBetterGrass = 1;
            }

            Minecraft.renderGlobal.loadRenderers();
        }

        if (p_setOptionValueOF_1_ == GameSettings.Options.CONNECTED_TEXTURES) {
            ++ofConnectedTextures;

            if (ofConnectedTextures > 3) {
                ofConnectedTextures = 1;
            }

            if (ofConnectedTextures == 2) {
                Minecraft.renderGlobal.loadRenderers();
            } else {
                Minecraft.refreshResources();
            }
        }

        if (p_setOptionValueOF_1_ == GameSettings.Options.WEATHER) {
            ofWeather = !ofWeather;
        }

        if (p_setOptionValueOF_1_ == GameSettings.Options.SKY) {
            ofSky = !ofSky;
        }

        if (p_setOptionValueOF_1_ == GameSettings.Options.STARS) {
            ofStars = !ofStars;
        }

        if (p_setOptionValueOF_1_ == GameSettings.Options.SUN_MOON) {
            ofSunMoon = !ofSunMoon;
        }

        if (p_setOptionValueOF_1_ == GameSettings.Options.VIGNETTE) {
            ++ofVignette;

            if (ofVignette > 2) {
                ofVignette = 0;
            }
        }

        if (p_setOptionValueOF_1_ == GameSettings.Options.CHUNK_UPDATES) {
            ++ofChunkUpdates;

            if (ofChunkUpdates > 5) {
                ofChunkUpdates = 1;
            }
        }

        if (p_setOptionValueOF_1_ == GameSettings.Options.CHUNK_UPDATES_DYNAMIC) {
            ofChunkUpdatesDynamic = !ofChunkUpdatesDynamic;
        }

        if (p_setOptionValueOF_1_ == GameSettings.Options.TIME) {
            ++ofTime;

            if (ofTime > 2) {
                ofTime = 0;
            }
        }

        if (p_setOptionValueOF_1_ == GameSettings.Options.CLEAR_WATER) {
            ofClearWater = !ofClearWater;
            updateWaterOpacity();
        }

        if (p_setOptionValueOF_1_ == GameSettings.Options.PROFILER) {
            ofProfiler = !ofProfiler;
        }

        if (p_setOptionValueOF_1_ == GameSettings.Options.BETTER_SNOW) {
            ofBetterSnow = !ofBetterSnow;
            Minecraft.renderGlobal.loadRenderers();
        }

        if (p_setOptionValueOF_1_ == GameSettings.Options.SWAMP_COLORS) {
            ofSwampColors = !ofSwampColors;
            CustomColors.updateUseDefaultGrassFoliageColors();
            Minecraft.renderGlobal.loadRenderers();
        }

        if (p_setOptionValueOF_1_ == GameSettings.Options.RANDOM_MOBS) {
            ofRandomMobs = !ofRandomMobs;
            RandomMobs.resetTextures();
        }

        if (p_setOptionValueOF_1_ == GameSettings.Options.SMOOTH_BIOMES) {
            ofSmoothBiomes = !ofSmoothBiomes;
            CustomColors.updateUseDefaultGrassFoliageColors();
            Minecraft.renderGlobal.loadRenderers();
        }

        if (p_setOptionValueOF_1_ == GameSettings.Options.CUSTOM_FONTS) {
            ofCustomFonts = !ofCustomFonts;
            Minecraft.font.onResourceManagerReload(Config.getResourceManager());
            Minecraft.galacticFont.onResourceManagerReload(Config.getResourceManager());
        }

        if (p_setOptionValueOF_1_ == GameSettings.Options.CUSTOM_COLORS) {
            ofCustomColors = !ofCustomColors;
            CustomColors.update();
            Minecraft.renderGlobal.loadRenderers();
        }

        if (p_setOptionValueOF_1_ == GameSettings.Options.CUSTOM_ITEMS) {
            ofCustomItems = !ofCustomItems;
            Minecraft.refreshResources();
        }

        if (p_setOptionValueOF_1_ == GameSettings.Options.CUSTOM_SKY) {
            ofCustomSky = !ofCustomSky;
            CustomSky.update();
        }

        if (p_setOptionValueOF_1_ == GameSettings.Options.SHOW_CAPES) {
            ofShowCapes = !ofShowCapes;
        }

        if (p_setOptionValueOF_1_ == GameSettings.Options.NATURAL_TEXTURES) {
            ofNaturalTextures = !ofNaturalTextures;
            NaturalTextures.update();
            Minecraft.renderGlobal.loadRenderers();
        }

        if (p_setOptionValueOF_1_ == GameSettings.Options.FAST_RENDER) {
            if (!ofFastRender && Config.isShaders()) {
                Screen current = Minecraft.screen;
                Minecraft.openScreen(new AlertScreen(new TranslatableComponent("of.message.fr.shaders1"), new TranslatableComponent("of.message.fr.shaders2"), new TranslatableComponent("gui.cancel"), () -> Minecraft.openScreen(current)));
                return;
            }

            ofFastRender = !ofFastRender;

            if (ofFastRender) {
                Minecraft.gameRenderer.stopUseShader();
            }

            Config.updateFramebufferSize();
        }

        if (p_setOptionValueOF_1_ == GameSettings.Options.TRANSLUCENT_BLOCKS) {
            if (ofTranslucentBlocks == 0) {
                ofTranslucentBlocks = 1;
            } else if (ofTranslucentBlocks == 1) {
                ofTranslucentBlocks = 2;
            } else if (ofTranslucentBlocks == 2) {
                ofTranslucentBlocks = 0;
            } else {
                ofTranslucentBlocks = 0;
            }

            Minecraft.renderGlobal.loadRenderers();
        }

        if (p_setOptionValueOF_1_ == GameSettings.Options.LAZY_CHUNK_LOADING) {
            ofLazyChunkLoading = !ofLazyChunkLoading;
            Config.updateAvailableProcessors();

            if (!Config.isSingleProcessor()) {
                ofLazyChunkLoading = false;
            }

            Minecraft.renderGlobal.loadRenderers();
        }

        if (p_setOptionValueOF_1_ == GameSettings.Options.DYNAMIC_FOV) {
            ofDynamicFov = !ofDynamicFov;
        }

        if (p_setOptionValueOF_1_ == GameSettings.Options.ALTERNATE_BLOCKS) {
            ofAlternateBlocks = !ofAlternateBlocks;
            Minecraft.refreshResources();
        }

        if (p_setOptionValueOF_1_ == GameSettings.Options.DYNAMIC_LIGHTS) {
            ofDynamicLights = nextValue(ofDynamicLights, OF_DYNAMIC_LIGHTS);
            DynamicLights.removeLights(Minecraft.renderGlobal);
        }

        if (p_setOptionValueOF_1_ == GameSettings.Options.SCREENSHOT_SIZE) {
            ++ofScreenshotSize;

            if (ofScreenshotSize > 4) {
                ofScreenshotSize = 1;
            }

            if (!OpenGlHelper.isFramebufferEnabled()) {
                ofScreenshotSize = 1;
            }
        }

        if (p_setOptionValueOF_1_ == GameSettings.Options.CUSTOM_ENTITY_MODELS) {
            ofCustomEntityModels = !ofCustomEntityModels;
            Minecraft.refreshResources();
        }

        if (p_setOptionValueOF_1_ == GameSettings.Options.CUSTOM_GUIS) {
            ofCustomGuis = !ofCustomGuis;
            CustomGuis.update();
        }

        if (p_setOptionValueOF_1_ == GameSettings.Options.HELD_ITEM_TOOLTIPS) {
            heldItemTooltips = !heldItemTooltips;
        }

        if (p_setOptionValueOF_1_ == GameSettings.Options.ADVANCED_TOOLTIPS) {
            advancedItemTooltips = !advancedItemTooltips;
        }
    }

    private String getKeyBindingOF(GameSettings.Options p_getKeyBindingOF_1_) {
        String s = I18n.format(p_getKeyBindingOF_1_.getEnumString()) + ": ";

        if (s == null) {
            s = p_getKeyBindingOF_1_.getEnumString();
        }

        if (p_getKeyBindingOF_1_ == GameSettings.Options.RENDER_DISTANCE) {
            int l = (int) getOptionFloatValue(p_getKeyBindingOF_1_);
            String s1 = I18n.format("of.options.renderDistance.tiny");
            int i = 2;

            if (l >= 4) {
                s1 = I18n.format("of.options.renderDistance.short");
                i = 4;
            }

            if (l >= 8) {
                s1 = I18n.format("of.options.renderDistance.normal");
                i = 8;
            }

            if (l >= 16) {
                s1 = I18n.format("of.options.renderDistance.far");
                i = 16;
            }

            if (l >= 32) {
                s1 = Lang.get("of.options.renderDistance.extreme");
                i = 32;
            }

            int j = renderDistance - i;
            String s2 = s1;

            if (j > 0) {
                s2 = s1 + "+";
            }

            return s + l + " " + s2 + "";
        } else if (p_getKeyBindingOF_1_ == GameSettings.Options.FOG_FANCY) {
            switch (ofFogType) {
                case 1:
                    return s + Lang.getFast();

                case 2:
                    return s + Lang.getFancy();

                case 3:
                    return s + Lang.getOff();

                default:
                    return s + Lang.getOff();
            }
        } else if (p_getKeyBindingOF_1_ == GameSettings.Options.FOG_START) {
            return s + ofFogStart;
        } else if (p_getKeyBindingOF_1_ == GameSettings.Options.MIPMAP_TYPE) {
            switch (ofMipmapType) {
                case 0:
                    return s + Lang.get("of.options.mipmap.nearest");

                case 1:
                    return s + Lang.get("of.options.mipmap.linear");

                case 2:
                    return s + Lang.get("of.options.mipmap.bilinear");

                case 3:
                    return s + Lang.get("of.options.mipmap.trilinear");

                default:
                    return s + "of.options.mipmap.nearest";
            }
        } else if (p_getKeyBindingOF_1_ == GameSettings.Options.SMOOTH_FPS) {
            return ofSmoothFps ? s + Lang.getOn() : s + Lang.getOff();
        } else if (p_getKeyBindingOF_1_ == GameSettings.Options.SMOOTH_WORLD) {
            return ofSmoothWorld ? s + Lang.getOn() : s + Lang.getOff();
        } else if (p_getKeyBindingOF_1_ == GameSettings.Options.CLOUDS) {
            switch (ofClouds) {
                case 1:
                    return s + Lang.getFast();

                case 2:
                    return s + Lang.getFancy();

                case 3:
                    return s + Lang.getOff();

                default:
                    return s + Lang.getDefault();
            }
        } else if (p_getKeyBindingOF_1_ == GameSettings.Options.TREES) {
            switch (ofTrees) {
                case 1:
                    return s + Lang.getFast();

                case 2:
                    return s + Lang.getFancy();

                case 3:
                default:
                    return s + Lang.getDefault();

                case 4:
                    return s + Lang.get("of.general.smart");
            }
        } else if (p_getKeyBindingOF_1_ == GameSettings.Options.DROPPED_ITEMS) {
            switch (ofDroppedItems) {
                case 1:
                    return s + Lang.getFast();

                case 2:
                    return s + Lang.getFancy();

                default:
                    return s + Lang.getDefault();
            }
        } else if (p_getKeyBindingOF_1_ == GameSettings.Options.RAIN) {
            switch (ofRain) {
                case 1:
                    return s + Lang.getFast();

                case 2:
                    return s + Lang.getFancy();

                case 3:
                    return s + Lang.getOff();

                default:
                    return s + Lang.getDefault();
            }
        } else if (p_getKeyBindingOF_1_ == GameSettings.Options.ANIMATED_WATER) {
            switch (ofAnimatedWater) {
                case 1:
                    return s + Lang.get("of.options.animation.dynamic");

                case 2:
                    return s + Lang.getOff();

                default:
                    return s + Lang.getOn();
            }
        } else if (p_getKeyBindingOF_1_ == GameSettings.Options.ANIMATED_LAVA) {
            switch (ofAnimatedLava) {
                case 1:
                    return s + Lang.get("of.options.animation.dynamic");

                case 2:
                    return s + Lang.getOff();

                default:
                    return s + Lang.getOn();
            }
        } else if (p_getKeyBindingOF_1_ == GameSettings.Options.ANIMATED_FIRE) {
            return ofAnimatedFire ? s + Lang.getOn() : s + Lang.getOff();
        } else if (p_getKeyBindingOF_1_ == GameSettings.Options.ANIMATED_PORTAL) {
            return ofAnimatedPortal ? s + Lang.getOn() : s + Lang.getOff();
        } else if (p_getKeyBindingOF_1_ == GameSettings.Options.ANIMATED_REDSTONE) {
            return ofAnimatedRedstone ? s + Lang.getOn() : s + Lang.getOff();
        } else if (p_getKeyBindingOF_1_ == GameSettings.Options.ANIMATED_EXPLOSION) {
            return ofAnimatedExplosion ? s + Lang.getOn() : s + Lang.getOff();
        } else if (p_getKeyBindingOF_1_ == GameSettings.Options.ANIMATED_FLAME) {
            return ofAnimatedFlame ? s + Lang.getOn() : s + Lang.getOff();
        } else if (p_getKeyBindingOF_1_ == GameSettings.Options.ANIMATED_SMOKE) {
            return ofAnimatedSmoke ? s + Lang.getOn() : s + Lang.getOff();
        } else if (p_getKeyBindingOF_1_ == GameSettings.Options.VOID_PARTICLES) {
            return ofVoidParticles ? s + Lang.getOn() : s + Lang.getOff();
        } else if (p_getKeyBindingOF_1_ == GameSettings.Options.WATER_PARTICLES) {
            return ofWaterParticles ? s + Lang.getOn() : s + Lang.getOff();
        } else if (p_getKeyBindingOF_1_ == GameSettings.Options.PORTAL_PARTICLES) {
            return ofPortalParticles ? s + Lang.getOn() : s + Lang.getOff();
        } else if (p_getKeyBindingOF_1_ == GameSettings.Options.POTION_PARTICLES) {
            return ofPotionParticles ? s + Lang.getOn() : s + Lang.getOff();
        } else if (p_getKeyBindingOF_1_ == GameSettings.Options.FIREWORK_PARTICLES) {
            return ofFireworkParticles ? s + Lang.getOn() : s + Lang.getOff();
        } else if (p_getKeyBindingOF_1_ == GameSettings.Options.DRIPPING_WATER_LAVA) {
            return ofDrippingWaterLava ? s + Lang.getOn() : s + Lang.getOff();
        } else if (p_getKeyBindingOF_1_ == GameSettings.Options.ANIMATED_TERRAIN) {
            return ofAnimatedTerrain ? s + Lang.getOn() : s + Lang.getOff();
        } else if (p_getKeyBindingOF_1_ == GameSettings.Options.ANIMATED_TEXTURES) {
            return ofAnimatedTextures ? s + Lang.getOn() : s + Lang.getOff();
        } else if (p_getKeyBindingOF_1_ == GameSettings.Options.RAIN_SPLASH) {
            return ofRainSplash ? s + Lang.getOn() : s + Lang.getOff();
        } else if (p_getKeyBindingOF_1_ == GameSettings.Options.LAGOMETER) {
            return ofLagometer ? s + Lang.getOn() : s + Lang.getOff();
        } else if (p_getKeyBindingOF_1_ == GameSettings.Options.SHOW_FPS) {
            return ofShowFps ? s + Lang.getOn() : s + Lang.getOff();
        } else if (p_getKeyBindingOF_1_ == GameSettings.Options.AUTOSAVE_TICKS) {
            if (ofAutoSaveTicks <= 40) {
                return s + Lang.get("of.options.save.default");
            } else if (ofAutoSaveTicks <= 400) {
                return s + Lang.get("of.options.save.20s");
            } else {
                return ofAutoSaveTicks <= 4000 ? s + Lang.get("of.options.save.3min") : s + Lang.get("of.options.save.30min");
            }
        } else if (p_getKeyBindingOF_1_ == GameSettings.Options.BETTER_GRASS) {
            switch (ofBetterGrass) {
                case 1:
                    return s + Lang.getFast();

                case 2:
                    return s + Lang.getFancy();

                default:
                    return s + Lang.getOff();
            }
        } else if (p_getKeyBindingOF_1_ == GameSettings.Options.CONNECTED_TEXTURES) {
            switch (ofConnectedTextures) {
                case 1:
                    return s + Lang.getFast();

                case 2:
                    return s + Lang.getFancy();

                default:
                    return s + Lang.getOff();
            }
        } else if (p_getKeyBindingOF_1_ == GameSettings.Options.WEATHER) {
            return ofWeather ? s + Lang.getOn() : s + Lang.getOff();
        } else if (p_getKeyBindingOF_1_ == GameSettings.Options.SKY) {
            return ofSky ? s + Lang.getOn() : s + Lang.getOff();
        } else if (p_getKeyBindingOF_1_ == GameSettings.Options.STARS) {
            return ofStars ? s + Lang.getOn() : s + Lang.getOff();
        } else if (p_getKeyBindingOF_1_ == GameSettings.Options.SUN_MOON) {
            return ofSunMoon ? s + Lang.getOn() : s + Lang.getOff();
        } else if (p_getKeyBindingOF_1_ == GameSettings.Options.VIGNETTE) {
            switch (ofVignette) {
                case 1:
                    return s + Lang.getFast();

                case 2:
                    return s + Lang.getFancy();

                default:
                    return s + Lang.getDefault();
            }
        } else if (p_getKeyBindingOF_1_ == GameSettings.Options.CHUNK_UPDATES) {
            return s + ofChunkUpdates;
        } else if (p_getKeyBindingOF_1_ == GameSettings.Options.CHUNK_UPDATES_DYNAMIC) {
            return ofChunkUpdatesDynamic ? s + Lang.getOn() : s + Lang.getOff();
        } else if (p_getKeyBindingOF_1_ == GameSettings.Options.TIME) {
            if (ofTime == 1) {
                return s + Lang.get("of.options.time.dayOnly");
            } else {
                return ofTime == 2 ? s + Lang.get("of.options.time.nightOnly") : s + Lang.getDefault();
            }
        } else if (p_getKeyBindingOF_1_ == GameSettings.Options.CLEAR_WATER) {
            return ofClearWater ? s + Lang.getOn() : s + Lang.getOff();
        } else if (p_getKeyBindingOF_1_ == GameSettings.Options.AA_LEVEL) {
            String s3 = "";

            if (ofAaLevel != Config.getAntialiasingLevel()) {
                s3 = " (" + Lang.get("of.general.restart") + ")";
            }

            return ofAaLevel == 0 ? s + Lang.getOff() + s3 : s + ofAaLevel + s3;
        } else if (p_getKeyBindingOF_1_ == GameSettings.Options.AF_LEVEL) {
            return ofAfLevel == 1 ? s + Lang.getOff() : s + ofAfLevel;
        } else if (p_getKeyBindingOF_1_ == GameSettings.Options.PROFILER) {
            return ofProfiler ? s + Lang.getOn() : s + Lang.getOff();
        } else if (p_getKeyBindingOF_1_ == GameSettings.Options.BETTER_SNOW) {
            return ofBetterSnow ? s + Lang.getOn() : s + Lang.getOff();
        } else if (p_getKeyBindingOF_1_ == GameSettings.Options.SWAMP_COLORS) {
            return ofSwampColors ? s + Lang.getOn() : s + Lang.getOff();
        } else if (p_getKeyBindingOF_1_ == GameSettings.Options.RANDOM_MOBS) {
            return ofRandomMobs ? s + Lang.getOn() : s + Lang.getOff();
        } else if (p_getKeyBindingOF_1_ == GameSettings.Options.SMOOTH_BIOMES) {
            return ofSmoothBiomes ? s + Lang.getOn() : s + Lang.getOff();
        } else if (p_getKeyBindingOF_1_ == GameSettings.Options.CUSTOM_FONTS) {
            return ofCustomFonts ? s + Lang.getOn() : s + Lang.getOff();
        } else if (p_getKeyBindingOF_1_ == GameSettings.Options.CUSTOM_COLORS) {
            return ofCustomColors ? s + Lang.getOn() : s + Lang.getOff();
        } else if (p_getKeyBindingOF_1_ == GameSettings.Options.CUSTOM_SKY) {
            return ofCustomSky ? s + Lang.getOn() : s + Lang.getOff();
        } else if (p_getKeyBindingOF_1_ == GameSettings.Options.SHOW_CAPES) {
            return ofShowCapes ? s + Lang.getOn() : s + Lang.getOff();
        } else if (p_getKeyBindingOF_1_ == GameSettings.Options.CUSTOM_ITEMS) {
            return ofCustomItems ? s + Lang.getOn() : s + Lang.getOff();
        } else if (p_getKeyBindingOF_1_ == GameSettings.Options.NATURAL_TEXTURES) {
            return ofNaturalTextures ? s + Lang.getOn() : s + Lang.getOff();
        } else if (p_getKeyBindingOF_1_ == GameSettings.Options.FAST_RENDER) {
            return ofFastRender ? s + Lang.getOn() : s + Lang.getOff();
        } else if (p_getKeyBindingOF_1_ == GameSettings.Options.TRANSLUCENT_BLOCKS) {
            if (ofTranslucentBlocks == 1) {
                return s + Lang.getFast();
            } else {
                return ofTranslucentBlocks == 2 ? s + Lang.getFancy() : s + Lang.getDefault();
            }
        } else if (p_getKeyBindingOF_1_ == GameSettings.Options.LAZY_CHUNK_LOADING) {
            return ofLazyChunkLoading ? s + Lang.getOn() : s + Lang.getOff();
        } else if (p_getKeyBindingOF_1_ == GameSettings.Options.DYNAMIC_FOV) {
            return ofDynamicFov ? s + Lang.getOn() : s + Lang.getOff();
        } else if (p_getKeyBindingOF_1_ == GameSettings.Options.ALTERNATE_BLOCKS) {
            return ofAlternateBlocks ? s + Lang.getOn() : s + Lang.getOff();
        } else if (p_getKeyBindingOF_1_ == GameSettings.Options.DYNAMIC_LIGHTS) {
            int k = indexOf(ofDynamicLights, OF_DYNAMIC_LIGHTS);
            return s + getTranslation(KEYS_DYNAMIC_LIGHTS, k);
        } else if (p_getKeyBindingOF_1_ == GameSettings.Options.SCREENSHOT_SIZE) {
            return ofScreenshotSize <= 1 ? s + Lang.getDefault() : s + ofScreenshotSize + "x";
        } else if (p_getKeyBindingOF_1_ == GameSettings.Options.CUSTOM_ENTITY_MODELS) {
            return ofCustomEntityModels ? s + Lang.getOn() : s + Lang.getOff();
        } else if (p_getKeyBindingOF_1_ == GameSettings.Options.CUSTOM_GUIS) {
            return ofCustomGuis ? s + Lang.getOn() : s + Lang.getOff();
        } else if (p_getKeyBindingOF_1_ == GameSettings.Options.FULLSCREEN_MODE) {
            return ofFullscreenMode.equals("Default") ? s + Lang.getDefault() : s + ofFullscreenMode;
        } else if (p_getKeyBindingOF_1_ == GameSettings.Options.HELD_ITEM_TOOLTIPS) {
            return heldItemTooltips ? s + Lang.getOn() : s + Lang.getOff();
        } else if (p_getKeyBindingOF_1_ == GameSettings.Options.ADVANCED_TOOLTIPS) {
            return advancedItemTooltips ? s + Lang.getOn() : s + Lang.getOff();
        } else if (p_getKeyBindingOF_1_ == GameSettings.Options.FRAMERATE_LIMIT) {
            float f = getOptionFloatValue(p_getKeyBindingOF_1_);

            if (f == 0.0F) {
                return s + Lang.get("of.options.framerateLimit.vsync");
            } else {
                return f == p_getKeyBindingOF_1_.valueMax ? s + I18n.format("options.framerateLimit.max") : s + (int) f + " fps";
            }
        } else {
            return null;
        }
    }

    public void loadOfOptions() {
        try {
            File file1 = optionsFileOF;

            if (!file1.exists()) {
                file1 = optionsFile;
            }

            if (!file1.exists()) {
                return;
            }

            BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(new FileInputStream(file1), StandardCharsets.UTF_8));
            String s = "";

            while ((s = bufferedreader.readLine()) != null) {
                try {
                    String[] astring = s.split(":");

                    if (astring[0].equals("ofRenderDistanceChunks") && astring.length >= 2) {
                        renderDistance = Integer.valueOf(astring[1]).intValue();
                        renderDistance = Config.limit(renderDistance, 2, 1024);
                    }

                    if (astring[0].equals("ofFogType") && astring.length >= 2) {
                        ofFogType = Integer.valueOf(astring[1]).intValue();
                        ofFogType = Config.limit(ofFogType, 1, 3);
                    }

                    if (astring[0].equals("ofFogStart") && astring.length >= 2) {
                        ofFogStart = Float.valueOf(astring[1]).floatValue();

                        if (ofFogStart < 0.2F) {
                            ofFogStart = 0.2F;
                        }

                        if (ofFogStart > 0.81F) {
                            ofFogStart = 0.8F;
                        }
                    }

                    if (astring[0].equals("ofMipmapType") && astring.length >= 2) {
                        ofMipmapType = Integer.valueOf(astring[1]).intValue();
                        ofMipmapType = Config.limit(ofMipmapType, 0, 3);
                    }

                    if (astring[0].equals("ofOcclusionFancy") && astring.length >= 2) {
                        ofOcclusionFancy = Boolean.valueOf(astring[1]).booleanValue();
                    }

                    if (astring[0].equals("ofSmoothFps") && astring.length >= 2) {
                        ofSmoothFps = Boolean.valueOf(astring[1]).booleanValue();
                    }

                    if (astring[0].equals("ofSmoothWorld") && astring.length >= 2) {
                        ofSmoothWorld = Boolean.valueOf(astring[1]).booleanValue();
                    }

                    if (astring[0].equals("ofAoLevel") && astring.length >= 2) {
                        ofAoLevel = Float.valueOf(astring[1]).floatValue();
                        ofAoLevel = Config.limit(ofAoLevel, 0.0F, 1.0F);
                    }

                    if (astring[0].equals("ofClouds") && astring.length >= 2) {
                        ofClouds = Integer.valueOf(astring[1]).intValue();
                        ofClouds = Config.limit(ofClouds, 0, 3);
                        updateRenderClouds();
                    }

                    if (astring[0].equals("ofCloudsHeight") && astring.length >= 2) {
                        ofCloudsHeight = Float.valueOf(astring[1]).floatValue();
                        ofCloudsHeight = Config.limit(ofCloudsHeight, 0.0F, 1.0F);
                    }

                    if (astring[0].equals("ofTrees") && astring.length >= 2) {
                        ofTrees = Integer.valueOf(astring[1]).intValue();
                        ofTrees = limit(ofTrees, OF_TREES_VALUES);
                    }

                    if (astring[0].equals("ofDroppedItems") && astring.length >= 2) {
                        ofDroppedItems = Integer.valueOf(astring[1]).intValue();
                        ofDroppedItems = Config.limit(ofDroppedItems, 0, 2);
                    }

                    if (astring[0].equals("ofRain") && astring.length >= 2) {
                        ofRain = Integer.valueOf(astring[1]).intValue();
                        ofRain = Config.limit(ofRain, 0, 3);
                    }

                    if (astring[0].equals("ofAnimatedWater") && astring.length >= 2) {
                        ofAnimatedWater = Integer.valueOf(astring[1]).intValue();
                        ofAnimatedWater = Config.limit(ofAnimatedWater, 0, 2);
                    }

                    if (astring[0].equals("ofAnimatedLava") && astring.length >= 2) {
                        ofAnimatedLava = Integer.valueOf(astring[1]).intValue();
                        ofAnimatedLava = Config.limit(ofAnimatedLava, 0, 2);
                    }

                    if (astring[0].equals("ofAnimatedFire") && astring.length >= 2) {
                        ofAnimatedFire = Boolean.valueOf(astring[1]).booleanValue();
                    }

                    if (astring[0].equals("ofAnimatedPortal") && astring.length >= 2) {
                        ofAnimatedPortal = Boolean.valueOf(astring[1]).booleanValue();
                    }

                    if (astring[0].equals("ofAnimatedRedstone") && astring.length >= 2) {
                        ofAnimatedRedstone = Boolean.valueOf(astring[1]).booleanValue();
                    }

                    if (astring[0].equals("ofAnimatedExplosion") && astring.length >= 2) {
                        ofAnimatedExplosion = Boolean.valueOf(astring[1]).booleanValue();
                    }

                    if (astring[0].equals("ofAnimatedFlame") && astring.length >= 2) {
                        ofAnimatedFlame = Boolean.valueOf(astring[1]).booleanValue();
                    }

                    if (astring[0].equals("ofAnimatedSmoke") && astring.length >= 2) {
                        ofAnimatedSmoke = Boolean.valueOf(astring[1]).booleanValue();
                    }

                    if (astring[0].equals("ofVoidParticles") && astring.length >= 2) {
                        ofVoidParticles = Boolean.valueOf(astring[1]).booleanValue();
                    }

                    if (astring[0].equals("ofWaterParticles") && astring.length >= 2) {
                        ofWaterParticles = Boolean.valueOf(astring[1]).booleanValue();
                    }

                    if (astring[0].equals("ofPortalParticles") && astring.length >= 2) {
                        ofPortalParticles = Boolean.valueOf(astring[1]).booleanValue();
                    }

                    if (astring[0].equals("ofPotionParticles") && astring.length >= 2) {
                        ofPotionParticles = Boolean.valueOf(astring[1]).booleanValue();
                    }

                    if (astring[0].equals("ofFireworkParticles") && astring.length >= 2) {
                        ofFireworkParticles = Boolean.valueOf(astring[1]).booleanValue();
                    }

                    if (astring[0].equals("ofDrippingWaterLava") && astring.length >= 2) {
                        ofDrippingWaterLava = Boolean.valueOf(astring[1]).booleanValue();
                    }

                    if (astring[0].equals("ofAnimatedTerrain") && astring.length >= 2) {
                        ofAnimatedTerrain = Boolean.valueOf(astring[1]).booleanValue();
                    }

                    if (astring[0].equals("ofAnimatedTextures") && astring.length >= 2) {
                        ofAnimatedTextures = Boolean.valueOf(astring[1]).booleanValue();
                    }

                    if (astring[0].equals("ofRainSplash") && astring.length >= 2) {
                        ofRainSplash = Boolean.valueOf(astring[1]).booleanValue();
                    }

                    if (astring[0].equals("ofLagometer") && astring.length >= 2) {
                        ofLagometer = Boolean.valueOf(astring[1]).booleanValue();
                    }

                    if (astring[0].equals("ofShowFps") && astring.length >= 2) {
                        ofShowFps = Boolean.valueOf(astring[1]).booleanValue();
                    }

                    if (astring[0].equals("ofAutoSaveTicks") && astring.length >= 2) {
                        ofAutoSaveTicks = Integer.valueOf(astring[1]).intValue();
                        ofAutoSaveTicks = Config.limit(ofAutoSaveTicks, 40, 40000);
                    }

                    if (astring[0].equals("ofBetterGrass") && astring.length >= 2) {
                        ofBetterGrass = Integer.valueOf(astring[1]).intValue();
                        ofBetterGrass = Config.limit(ofBetterGrass, 1, 3);
                    }

                    if (astring[0].equals("ofConnectedTextures") && astring.length >= 2) {
                        ofConnectedTextures = Integer.valueOf(astring[1]).intValue();
                        ofConnectedTextures = Config.limit(ofConnectedTextures, 1, 3);
                    }

                    if (astring[0].equals("ofWeather") && astring.length >= 2) {
                        ofWeather = Boolean.valueOf(astring[1]).booleanValue();
                    }

                    if (astring[0].equals("ofSky") && astring.length >= 2) {
                        ofSky = Boolean.valueOf(astring[1]).booleanValue();
                    }

                    if (astring[0].equals("ofStars") && astring.length >= 2) {
                        ofStars = Boolean.valueOf(astring[1]).booleanValue();
                    }

                    if (astring[0].equals("ofSunMoon") && astring.length >= 2) {
                        ofSunMoon = Boolean.valueOf(astring[1]).booleanValue();
                    }

                    if (astring[0].equals("ofVignette") && astring.length >= 2) {
                        ofVignette = Integer.valueOf(astring[1]).intValue();
                        ofVignette = Config.limit(ofVignette, 0, 2);
                    }

                    if (astring[0].equals("ofChunkUpdates") && astring.length >= 2) {
                        ofChunkUpdates = Integer.valueOf(astring[1]).intValue();
                        ofChunkUpdates = Config.limit(ofChunkUpdates, 1, 5);
                    }

                    if (astring[0].equals("ofChunkUpdatesDynamic") && astring.length >= 2) {
                        ofChunkUpdatesDynamic = Boolean.valueOf(astring[1]).booleanValue();
                    }

                    if (astring[0].equals("ofTime") && astring.length >= 2) {
                        ofTime = Integer.valueOf(astring[1]).intValue();
                        ofTime = Config.limit(ofTime, 0, 2);
                    }

                    if (astring[0].equals("ofClearWater") && astring.length >= 2) {
                        ofClearWater = Boolean.valueOf(astring[1]).booleanValue();
                        updateWaterOpacity();
                    }

                    if (astring[0].equals("ofAaLevel") && astring.length >= 2) {
                        ofAaLevel = Integer.valueOf(astring[1]).intValue();
                        ofAaLevel = Config.limit(ofAaLevel, 0, 16);
                    }

                    if (astring[0].equals("ofAfLevel") && astring.length >= 2) {
                        ofAfLevel = Integer.valueOf(astring[1]).intValue();
                        ofAfLevel = Config.limit(ofAfLevel, 1, 16);
                    }

                    if (astring[0].equals("ofProfiler") && astring.length >= 2) {
                        ofProfiler = Boolean.valueOf(astring[1]).booleanValue();
                    }

                    if (astring[0].equals("ofBetterSnow") && astring.length >= 2) {
                        ofBetterSnow = Boolean.valueOf(astring[1]).booleanValue();
                    }

                    if (astring[0].equals("ofSwampColors") && astring.length >= 2) {
                        ofSwampColors = Boolean.valueOf(astring[1]).booleanValue();
                    }

                    if (astring[0].equals("ofRandomMobs") && astring.length >= 2) {
                        ofRandomMobs = Boolean.valueOf(astring[1]).booleanValue();
                    }

                    if (astring[0].equals("ofSmoothBiomes") && astring.length >= 2) {
                        ofSmoothBiomes = Boolean.valueOf(astring[1]).booleanValue();
                    }

                    if (astring[0].equals("ofCustomFonts") && astring.length >= 2) {
                        ofCustomFonts = Boolean.valueOf(astring[1]).booleanValue();
                    }

                    if (astring[0].equals("ofCustomColors") && astring.length >= 2) {
                        ofCustomColors = Boolean.valueOf(astring[1]).booleanValue();
                    }

                    if (astring[0].equals("ofCustomItems") && astring.length >= 2) {
                        ofCustomItems = Boolean.valueOf(astring[1]).booleanValue();
                    }

                    if (astring[0].equals("ofCustomSky") && astring.length >= 2) {
                        ofCustomSky = Boolean.valueOf(astring[1]).booleanValue();
                    }

                    if (astring[0].equals("ofShowCapes") && astring.length >= 2) {
                        ofShowCapes = Boolean.valueOf(astring[1]).booleanValue();
                    }

                    if (astring[0].equals("ofNaturalTextures") && astring.length >= 2) {
                        ofNaturalTextures = Boolean.valueOf(astring[1]).booleanValue();
                    }

                    if (astring[0].equals("ofLazyChunkLoading") && astring.length >= 2) {
                        ofLazyChunkLoading = Boolean.valueOf(astring[1]).booleanValue();
                    }

                    if (astring[0].equals("ofDynamicFov") && astring.length >= 2) {
                        ofDynamicFov = Boolean.valueOf(astring[1]).booleanValue();
                    }

                    if (astring[0].equals("ofAlternateBlocks") && astring.length >= 2) {
                        ofAlternateBlocks = Boolean.valueOf(astring[1]).booleanValue();
                    }

                    if (astring[0].equals("ofDynamicLights") && astring.length >= 2) {
                        ofDynamicLights = Integer.valueOf(astring[1]).intValue();
                        ofDynamicLights = limit(ofDynamicLights, OF_DYNAMIC_LIGHTS);
                    }

                    if (astring[0].equals("ofScreenshotSize") && astring.length >= 2) {
                        ofScreenshotSize = Integer.valueOf(astring[1]).intValue();
                        ofScreenshotSize = Config.limit(ofScreenshotSize, 1, 4);
                    }

                    if (astring[0].equals("ofCustomEntityModels") && astring.length >= 2) {
                        ofCustomEntityModels = Boolean.valueOf(astring[1]).booleanValue();
                    }

                    if (astring[0].equals("ofCustomGuis") && astring.length >= 2) {
                        ofCustomGuis = Boolean.valueOf(astring[1]).booleanValue();
                    }

                    if (astring[0].equals("ofFullscreenMode") && astring.length >= 2) {
                        ofFullscreenMode = astring[1];
                    }

                    if (astring[0].equals("ofFastRender") && astring.length >= 2) {
                        ofFastRender = Boolean.valueOf(astring[1]).booleanValue();
                    }

                    if (astring[0].equals("ofTranslucentBlocks") && astring.length >= 2) {
                        ofTranslucentBlocks = Integer.valueOf(astring[1]).intValue();
                        ofTranslucentBlocks = Config.limit(ofTranslucentBlocks, 0, 2);
                    }

                    if (astring[0].equals("key_" + ofKeyBindZoom.getKeyDescription())) {
                        ofKeyBindZoom.setKeyCode(Integer.parseInt(astring[1]));
                    }
                } catch (Exception exception1) {
                    exception1.printStackTrace();
                }
            }

            KeyBinding.resetKeyBindingArrayAndHash();
            bufferedreader.close();
        } catch (Exception exception11) {
            exception11.printStackTrace();
        }
    }

    public void saveOfOptions() {
        try {
            PrintWriter printwriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(optionsFileOF), StandardCharsets.UTF_8));
            printwriter.println("ofRenderDistanceChunks:" + renderDistance);
            printwriter.println("ofFogType:" + ofFogType);
            printwriter.println("ofFogStart:" + ofFogStart);
            printwriter.println("ofMipmapType:" + ofMipmapType);
            printwriter.println("ofOcclusionFancy:" + ofOcclusionFancy);
            printwriter.println("ofSmoothFps:" + ofSmoothFps);
            printwriter.println("ofSmoothWorld:" + ofSmoothWorld);
            printwriter.println("ofAoLevel:" + ofAoLevel);
            printwriter.println("ofClouds:" + ofClouds);
            printwriter.println("ofCloudsHeight:" + ofCloudsHeight);
            printwriter.println("ofTrees:" + ofTrees);
            printwriter.println("ofDroppedItems:" + ofDroppedItems);
            printwriter.println("ofRain:" + ofRain);
            printwriter.println("ofAnimatedWater:" + ofAnimatedWater);
            printwriter.println("ofAnimatedLava:" + ofAnimatedLava);
            printwriter.println("ofAnimatedFire:" + ofAnimatedFire);
            printwriter.println("ofAnimatedPortal:" + ofAnimatedPortal);
            printwriter.println("ofAnimatedRedstone:" + ofAnimatedRedstone);
            printwriter.println("ofAnimatedExplosion:" + ofAnimatedExplosion);
            printwriter.println("ofAnimatedFlame:" + ofAnimatedFlame);
            printwriter.println("ofAnimatedSmoke:" + ofAnimatedSmoke);
            printwriter.println("ofVoidParticles:" + ofVoidParticles);
            printwriter.println("ofWaterParticles:" + ofWaterParticles);
            printwriter.println("ofPortalParticles:" + ofPortalParticles);
            printwriter.println("ofPotionParticles:" + ofPotionParticles);
            printwriter.println("ofFireworkParticles:" + ofFireworkParticles);
            printwriter.println("ofDrippingWaterLava:" + ofDrippingWaterLava);
            printwriter.println("ofAnimatedTerrain:" + ofAnimatedTerrain);
            printwriter.println("ofAnimatedTextures:" + ofAnimatedTextures);
            printwriter.println("ofRainSplash:" + ofRainSplash);
            printwriter.println("ofLagometer:" + ofLagometer);
            printwriter.println("ofShowFps:" + ofShowFps);
            printwriter.println("ofAutoSaveTicks:" + ofAutoSaveTicks);
            printwriter.println("ofBetterGrass:" + ofBetterGrass);
            printwriter.println("ofConnectedTextures:" + ofConnectedTextures);
            printwriter.println("ofWeather:" + ofWeather);
            printwriter.println("ofSky:" + ofSky);
            printwriter.println("ofStars:" + ofStars);
            printwriter.println("ofSunMoon:" + ofSunMoon);
            printwriter.println("ofVignette:" + ofVignette);
            printwriter.println("ofChunkUpdates:" + ofChunkUpdates);
            printwriter.println("ofChunkUpdatesDynamic:" + ofChunkUpdatesDynamic);
            printwriter.println("ofTime:" + ofTime);
            printwriter.println("ofClearWater:" + ofClearWater);
            printwriter.println("ofAaLevel:" + ofAaLevel);
            printwriter.println("ofAfLevel:" + ofAfLevel);
            printwriter.println("ofProfiler:" + ofProfiler);
            printwriter.println("ofBetterSnow:" + ofBetterSnow);
            printwriter.println("ofSwampColors:" + ofSwampColors);
            printwriter.println("ofRandomMobs:" + ofRandomMobs);
            printwriter.println("ofSmoothBiomes:" + ofSmoothBiomes);
            printwriter.println("ofCustomFonts:" + ofCustomFonts);
            printwriter.println("ofCustomColors:" + ofCustomColors);
            printwriter.println("ofCustomItems:" + ofCustomItems);
            printwriter.println("ofCustomSky:" + ofCustomSky);
            printwriter.println("ofShowCapes:" + ofShowCapes);
            printwriter.println("ofNaturalTextures:" + ofNaturalTextures);
            printwriter.println("ofLazyChunkLoading:" + ofLazyChunkLoading);
            printwriter.println("ofDynamicFov:" + ofDynamicFov);
            printwriter.println("ofAlternateBlocks:" + ofAlternateBlocks);
            printwriter.println("ofDynamicLights:" + ofDynamicLights);
            printwriter.println("ofScreenshotSize:" + ofScreenshotSize);
            printwriter.println("ofCustomEntityModels:" + ofCustomEntityModels);
            printwriter.println("ofCustomGuis:" + ofCustomGuis);
            printwriter.println("ofFullscreenMode:" + ofFullscreenMode);
            printwriter.println("ofFastRender:" + ofFastRender);
            printwriter.println("ofTranslucentBlocks:" + ofTranslucentBlocks);
            printwriter.println("key_" + ofKeyBindZoom.getKeyDescription() + ":" + ofKeyBindZoom.getKeyCode());
            printwriter.close();
        } catch (Exception exception1) {
            Config.warn("Failed to save options");
            exception1.printStackTrace();
        }
    }

    private void updateRenderClouds() {
        switch (ofClouds) {
            case 1:
                clouds = 1;
                break;

            case 2:
                clouds = 2;
                break;

            case 3:
                clouds = 0;
                break;

            default:
                if (fancyGraphics) {
                    clouds = 2;
                } else {
                    clouds = 1;
                }
        }
    }

    public void resetSettings() {
        renderDistance = 8;
        viewBobbing = true;
        anaglyph = false;
        limitFramerate = (int) GameSettings.Options.FRAMERATE_LIMIT.getValueMax();
        enableVsync = false;
        updateVSync();
        mipmapLevels = 4;
        fancyGraphics = true;
        ambientOcclusion = 2;
        clouds = 2;
        fov = 70.0F;
        gamma = 0.0F;
        scale = 0;
        particles = 0;
        heldItemTooltips = true;
        useVbo = false;
        forceUnicodeFont = false;
        ofFogType = 1;
        ofFogStart = 0.8F;
        ofMipmapType = 0;
        ofOcclusionFancy = false;
        ofSmoothFps = false;
        Config.updateAvailableProcessors();
        ofSmoothWorld = Config.isSingleProcessor();
        ofLazyChunkLoading = Config.isSingleProcessor();
        ofFastRender = false;
        ofTranslucentBlocks = 0;
        ofDynamicFov = true;
        ofAlternateBlocks = true;
        ofDynamicLights = 3;
        ofScreenshotSize = 1;
        ofCustomEntityModels = true;
        ofCustomGuis = true;
        ofAoLevel = 1.0F;
        ofAaLevel = 0;
        ofAfLevel = 1;
        ofClouds = 0;
        ofCloudsHeight = 0.0F;
        ofTrees = 0;
        ofRain = 0;
        ofBetterGrass = 3;
        ofAutoSaveTicks = 4000;
        ofLagometer = false;
        ofShowFps = false;
        ofProfiler = false;
        ofWeather = true;
        ofSky = true;
        ofStars = true;
        ofSunMoon = true;
        ofVignette = 0;
        ofChunkUpdates = 1;
        ofChunkUpdatesDynamic = false;
        ofTime = 0;
        ofClearWater = false;
        ofBetterSnow = false;
        ofFullscreenMode = "Default";
        ofSwampColors = true;
        ofRandomMobs = true;
        ofSmoothBiomes = true;
        ofCustomFonts = true;
        ofCustomColors = true;
        ofCustomItems = true;
        ofCustomSky = true;
        ofShowCapes = true;
        ofConnectedTextures = 2;
        ofNaturalTextures = false;
        ofAnimatedWater = 0;
        ofAnimatedLava = 0;
        ofAnimatedFire = true;
        ofAnimatedPortal = true;
        ofAnimatedRedstone = true;
        ofAnimatedExplosion = true;
        ofAnimatedFlame = true;
        ofAnimatedSmoke = true;
        ofVoidParticles = true;
        ofWaterParticles = true;
        ofRainSplash = true;
        ofPortalParticles = true;
        ofPotionParticles = true;
        ofFireworkParticles = true;
        ofDrippingWaterLava = true;
        ofAnimatedTerrain = true;
        ofAnimatedTextures = true;
        Shaders.setShaderPack(Shaders.packNameNone);
        Shaders.configAntialiasingLevel = 0;
        Shaders.uninit();
        Shaders.storeConfig();
        updateWaterOpacity();
        Minecraft.refreshResources();
        saveOptions();
    }

    public void updateVSync() {
        Display.setVSyncEnabled(enableVsync);
    }

    private void updateWaterOpacity() {
        if (Minecraft.isIntegratedServerRunning() && Minecraft.getIntegratedServer() != null) {
            Config.waterOpacityChanged = true;
        }

        ClearWater.updateWaterOpacity(this, Minecraft.world);
    }

    public void setAllAnimations(boolean state) {
        int i = state ? 0 : 2;
        ofAnimatedWater = i;
        ofAnimatedLava = i;
        ofAnimatedFire = state;
        ofAnimatedPortal = state;
        ofAnimatedRedstone = state;
        ofAnimatedExplosion = state;
        ofAnimatedFlame = state;
        ofAnimatedSmoke = state;
        ofVoidParticles = state;
        ofWaterParticles = state;
        ofRainSplash = state;
        ofPortalParticles = state;
        ofPotionParticles = state;
        ofFireworkParticles = state;
        particles = i;
        ofDrippingWaterLava = state;
        ofAnimatedTerrain = state;
        ofAnimatedTextures = state;
    }

    private static int nextValue(int p_nextValue_0_, int[] p_nextValue_1_) {
        int i = indexOf(p_nextValue_0_, p_nextValue_1_);

        if (i < 0) {
            return p_nextValue_1_[0];
        } else {
            ++i;

            if (i >= p_nextValue_1_.length) {
                i = 0;
            }

            return p_nextValue_1_[i];
        }
    }

    private static int limit(int p_limit_0_, int[] p_limit_1_) {
        int i = indexOf(p_limit_0_, p_limit_1_);
        return i < 0 ? p_limit_1_[0] : p_limit_0_;
    }

    private static int indexOf(int p_indexOf_0_, int[] p_indexOf_1_) {
        for (int i = 0; i < p_indexOf_1_.length; ++i) {
            if (p_indexOf_1_[i] == p_indexOf_0_) {
                return i;
            }
        }

        return -1;
    }

    public void onGuiClosed() {
        if (needsResourceRefresh) {
            Minecraft.refreshResources();
            needsResourceRefresh = false;
        }
    }

    public enum Options {
        INVERT_MOUSE("options.invertMouse", false, true),
        SENSITIVITY("options.sensitivity", true, false),
        FOV("options.fov", true, false, 30.0F, 110.0F, 1.0F),
        GAMMA("options.gamma", true, false),
        RENDER_DISTANCE("options.renderDistance", true, false, 2.0F, 32.0F, 1.0F),
        VIEW_BOBBING("options.viewBobbing", false, true),
        ANAGLYPH("options.anaglyph", false, true),
        FRAMERATE_LIMIT("options.framerateLimit", true, false, 0.0F, 260.0F, 5.0F),
        FBO_ENABLE("options.fboEnable", false, true),
        RENDER_CLOUDS("options.renderClouds", false, false),
        GRAPHICS("options.graphics", false, false),
        AMBIENT_OCCLUSION("options.ao", false, false),
        GUI_SCALE("options.guiScale", false, false),
        PARTICLES("options.particles", false, false),
        CHAT_VISIBILITY("options.chat.visibility", false, false),
        CHAT_COLOR("options.chat.color", false, true),
        CHAT_LINKS("options.chat.links", false, true),
        CHAT_OPACITY("options.chat.opacity", true, false),
        CHAT_LINKS_PROMPT("options.chat.links.prompt", false, true),
        SNOOPER_ENABLED("options.snooper", false, true),
        USE_FULLSCREEN("options.fullscreen", false, true),
        ENABLE_VSYNC("options.vsync", false, true),
        USE_VBO("options.vbo", false, true),
        TOUCHSCREEN("options.touchscreen", false, true),
        CHAT_SCALE("options.chat.scale", true, false),
        CHAT_WIDTH("options.chat.width", true, false),
        CHAT_HEIGHT_FOCUSED("options.chat.height.focused", true, false),
        CHAT_HEIGHT_UNFOCUSED("options.chat.height.unfocused", true, false),
        MIPMAP_LEVELS("options.mipmapLevels", true, false, 0.0F, 4.0F, 1.0F),
        FORCE_UNICODE_FONT("options.forceUnicodeFont", false, true),
        REDUCED_DEBUG_INFO("options.reducedDebugInfo", false, true),
        ENTITY_SHADOWS("options.entityShadows", false, true),
        MAIN_HAND("options.mainHand", false, false),
        ATTACK_INDICATOR("options.attackIndicator", false, false),
        SHOW_SUBTITLES("options.showSubtitles", false, true),
        AUTO_JUMP("options.autoJump", false, true),
        FOG_FANCY("of.options.FOG_FANCY", false, false),
        FOG_START("of.options.FOG_START", false, false),
        MIPMAP_TYPE("of.options.MIPMAP_TYPE", true, false, 0.0F, 3.0F, 1.0F),
        SMOOTH_FPS("of.options.SMOOTH_FPS", false, false),
        CLOUDS("of.options.CLOUDS", false, false),
        CLOUD_HEIGHT("of.options.CLOUD_HEIGHT", true, false),
        TREES("of.options.TREES", false, false),
        RAIN("of.options.RAIN", false, false),
        ANIMATED_WATER("of.options.ANIMATED_WATER", false, false),
        ANIMATED_LAVA("of.options.ANIMATED_LAVA", false, false),
        ANIMATED_FIRE("of.options.ANIMATED_FIRE", false, false),
        ANIMATED_PORTAL("of.options.ANIMATED_PORTAL", false, false),
        AO_LEVEL("of.options.AO_LEVEL", true, false),
        LAGOMETER("of.options.LAGOMETER", false, false),
        SHOW_FPS("of.options.SHOW_FPS", false, false),
        AUTOSAVE_TICKS("of.options.AUTOSAVE_TICKS", false, false),
        BETTER_GRASS("of.options.BETTER_GRASS", false, false),
        ANIMATED_REDSTONE("of.options.ANIMATED_REDSTONE", false, false),
        ANIMATED_EXPLOSION("of.options.ANIMATED_EXPLOSION", false, false),
        ANIMATED_FLAME("of.options.ANIMATED_FLAME", false, false),
        ANIMATED_SMOKE("of.options.ANIMATED_SMOKE", false, false),
        WEATHER("of.options.WEATHER", false, false),
        SKY("of.options.SKY", false, false),
        STARS("of.options.STARS", false, false),
        SUN_MOON("of.options.SUN_MOON", false, false),
        VIGNETTE("of.options.VIGNETTE", false, false),
        CHUNK_UPDATES("of.options.CHUNK_UPDATES", false, false),
        CHUNK_UPDATES_DYNAMIC("of.options.CHUNK_UPDATES_DYNAMIC", false, false),
        TIME("of.options.TIME", false, false),
        CLEAR_WATER("of.options.CLEAR_WATER", false, false),
        SMOOTH_WORLD("of.options.SMOOTH_WORLD", false, false),
        VOID_PARTICLES("of.options.VOID_PARTICLES", false, false),
        WATER_PARTICLES("of.options.WATER_PARTICLES", false, false),
        RAIN_SPLASH("of.options.RAIN_SPLASH", false, false),
        PORTAL_PARTICLES("of.options.PORTAL_PARTICLES", false, false),
        POTION_PARTICLES("of.options.POTION_PARTICLES", false, false),
        FIREWORK_PARTICLES("of.options.FIREWORK_PARTICLES", false, false),
        PROFILER("of.options.PROFILER", false, false),
        DRIPPING_WATER_LAVA("of.options.DRIPPING_WATER_LAVA", false, false),
        BETTER_SNOW("of.options.BETTER_SNOW", false, false),
        FULLSCREEN_MODE("of.options.FULLSCREEN_MODE", true, false, 0.0F, (float) Config.getDisplayModes().length, 1.0F),
        ANIMATED_TERRAIN("of.options.ANIMATED_TERRAIN", false, false),
        SWAMP_COLORS("of.options.SWAMP_COLORS", false, false),
        RANDOM_MOBS("of.options.RANDOM_MOBS", false, false),
        SMOOTH_BIOMES("of.options.SMOOTH_BIOMES", false, false),
        CUSTOM_FONTS("of.options.CUSTOM_FONTS", false, false),
        CUSTOM_COLORS("of.options.CUSTOM_COLORS", false, false),
        SHOW_CAPES("of.options.SHOW_CAPES", false, false),
        CONNECTED_TEXTURES("of.options.CONNECTED_TEXTURES", false, false),
        CUSTOM_ITEMS("of.options.CUSTOM_ITEMS", false, false),
        AA_LEVEL("of.options.AA_LEVEL", true, false, 0.0F, 16.0F, 1.0F),
        AF_LEVEL("of.options.AF_LEVEL", true, false, 1.0F, 16.0F, 1.0F),
        ANIMATED_TEXTURES("of.options.ANIMATED_TEXTURES", false, false),
        NATURAL_TEXTURES("of.options.NATURAL_TEXTURES", false, false),
        HELD_ITEM_TOOLTIPS("of.options.HELD_ITEM_TOOLTIPS", false, false),
        DROPPED_ITEMS("of.options.DROPPED_ITEMS", false, false),
        LAZY_CHUNK_LOADING("of.options.LAZY_CHUNK_LOADING", false, false),
        CUSTOM_SKY("of.options.CUSTOM_SKY", false, false),
        FAST_RENDER("of.options.FAST_RENDER", false, false),
        TRANSLUCENT_BLOCKS("of.options.TRANSLUCENT_BLOCKS", false, false),
        DYNAMIC_FOV("of.options.DYNAMIC_FOV", false, false),
        DYNAMIC_LIGHTS("of.options.DYNAMIC_LIGHTS", false, false),
        ALTERNATE_BLOCKS("of.options.ALTERNATE_BLOCKS", false, false),
        CUSTOM_ENTITY_MODELS("of.options.CUSTOM_ENTITY_MODELS", false, false),
        ADVANCED_TOOLTIPS("of.options.ADVANCED_TOOLTIPS", false, false),
        SCREENSHOT_SIZE("of.options.SCREENSHOT_SIZE", false, false),
        CUSTOM_GUIS("of.options.CUSTOM_GUIS", false, false);

        private final boolean enumFloat;
        private final boolean enumBoolean;
        private final String enumString;
        private final float valueStep;
        private float valueMin;
        private float valueMax;

        public static GameSettings.Options getEnumOptions(int ordinal) {
            for (GameSettings.Options gamesettings$options : values()) {
                if (gamesettings$options.returnEnumOrdinal() == ordinal) {
                    return gamesettings$options;
                }
            }

            return null;
        }

        Options(String str, boolean isFloat, boolean isBoolean) {
            this(str, isFloat, isBoolean, 0.0F, 1.0F, 0.0F);
        }

        Options(String str, boolean isFloat, boolean isBoolean, float valMin, float valMax, float valStep) {
            enumString = str;
            enumFloat = isFloat;
            enumBoolean = isBoolean;
            valueMin = valMin;
            valueMax = valMax;
            valueStep = valStep;
        }

        public boolean getEnumFloat() {
            return enumFloat;
        }

        public boolean getEnumBoolean() {
            return enumBoolean;
        }

        public int returnEnumOrdinal() {
            return ordinal();
        }

        public String getEnumString() {
            return enumString;
        }

        public float getValueMin() {
            return valueMin;
        }

        public float getValueMax() {
            return valueMax;
        }

        public void setValueMax(float value) {
            valueMax = value;
        }

        public float normalizeValue(float value) {
            return MathHelper.clamp((snapToStepClamp(value) - valueMin) / (valueMax - valueMin), 0.0F, 1.0F);
        }

        public float denormalizeValue(float value) {
            return snapToStepClamp(valueMin + (valueMax - valueMin) * MathHelper.clamp(value, 0.0F, 1.0F));
        }

        public float snapToStepClamp(float value) {
            value = snapToStep(value);
            return MathHelper.clamp(value, valueMin, valueMax);
        }

        private float snapToStep(float value) {
            if (valueStep > 0.0F) {
                value = valueStep * (float) Math.round(value / valueStep);
            }

            return value;
        }
    }
}
