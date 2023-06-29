package net.minecraft.client.gui;

import java.util.Random;

import com.google.common.base.Strings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.world.GameType;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.WorldInfo;
import org.lwjgl.input.Keyboard;

public class GuiCreateWorld extends Screen
{
    private final Screen parentScreen;
    private GuiTextField worldNameField;
    private GuiTextField worldSeedField;
    private String saveDirName;
    private String gameMode = "survival";

    /**
     * Used to save away the game mode when the current "debug" world type is chosen (forcing it to spectator mode)
     */
    private String savedGameMode;
    private boolean generateStructuresEnabled = true;

    /** If cheats are allowed */
    private boolean allowCheats;

    /**
     * User explicitly clicked "Allow Cheats" at some point
     * Prevents value changes due to changing game mode
     */
    private boolean allowCheatsWasSetByUser;
    private boolean bonusChestEnabled;

    /** Set to true when "hardcore" is the currently-selected gamemode */
    private boolean hardCoreMode;
    private boolean alreadyGenerated;
    private boolean inMoreWorldOptionsDisplay;
    private ButtonWidget btnGameMode;
    private ButtonWidget btnMoreOptions;
    private ButtonWidget btnMapFeatures;
    private ButtonWidget btnBonusItems;
    private ButtonWidget btnMapType;
    private ButtonWidget btnAllowCommands;
    private ButtonWidget btnCustomizeType;
    private String gameModeDesc1;
    private String gameModeDesc2;
    private String worldSeed;
    private String worldName;
    private int selectedIndex;
    public String chunkProviderSettingsJson = "";

    /** These filenames are known to be restricted on one or more OS's. */
    private static final String[] DISALLOWED_FILENAMES = {"CON", "COM", "PRN", "AUX", "CLOCK$", "NUL", "COM1", "COM2", "COM3", "COM4", "COM5", "COM6", "COM7", "COM8", "COM9", "LPT1", "LPT2", "LPT3", "LPT4", "LPT5", "LPT6", "LPT7", "LPT8", "LPT9"};

    public GuiCreateWorld(Screen p_i46320_1_)
    {
        parentScreen = p_i46320_1_;
        worldSeed = "";
        worldName = I18n.format("selectWorld.newWorld");
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void update()
    {
        worldNameField.update();
        worldSeedField.update();
    }

    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    public void init()
    {
        Keyboard.enableRepeatEvents(true);
        widgets.clear();
        widgets.add(new ButtonWidget(0, width / 2 - 155, height - 28, 150, 20, I18n.format("selectWorld.create")));
        widgets.add(new ButtonWidget(1, width / 2 + 5, height - 28, 150, 20, I18n.format("gui.cancel")));
        btnGameMode = addButton(new ButtonWidget(2, width / 2 - 75, 115, 150, 20, I18n.format("selectWorld.gameMode")));
        btnMoreOptions = addButton(new ButtonWidget(3, width / 2 - 75, 187, 150, 20, I18n.format("selectWorld.moreWorldOptions")));
        btnMapFeatures = addButton(new ButtonWidget(4, width / 2 - 155, 100, 150, 20, I18n.format("selectWorld.mapFeatures")));
        btnMapFeatures.visible = false;
        btnBonusItems = addButton(new ButtonWidget(7, width / 2 + 5, 151, 150, 20, I18n.format("selectWorld.bonusItems")));
        btnBonusItems.visible = false;
        btnMapType = addButton(new ButtonWidget(5, width / 2 + 5, 100, 150, 20, I18n.format("selectWorld.mapType")));
        btnMapType.visible = false;
        btnAllowCommands = addButton(new ButtonWidget(6, width / 2 - 155, 151, 150, 20, I18n.format("selectWorld.allowCommands")));
        btnAllowCommands.visible = false;
        btnCustomizeType = addButton(new ButtonWidget(8, width / 2 + 5, 120, 150, 20, I18n.format("selectWorld.customizeType")));
        btnCustomizeType.visible = false;
        worldNameField = new GuiTextField(9, font, width / 2 - 100, 60, 200, 20);
        worldNameField.setFocused(true);
        worldNameField.setText(worldName);
        worldSeedField = new GuiTextField(10, font, width / 2 - 100, 60, 200, 20);
        worldSeedField.setText(worldSeed);
        showMoreWorldOptions(inMoreWorldOptionsDisplay);
        calcSaveDirName();
        updateDisplayState();
    }

    /**
     * Determine a save-directory name from the world name
     */
    private void calcSaveDirName()
    {
        saveDirName = worldNameField.getText().trim();

        for (char c0 : ChatAllowedCharacters.ILLEGAL_FILE_CHARACTERS)
        {
            saveDirName = saveDirName.replace(c0, '_');
        }

        if (Strings.isNullOrEmpty(saveDirName))
        {
            saveDirName = "World";
        }

        saveDirName = getUncollidingSaveDirName(minecraft.getSaveLoader(), saveDirName);
    }

    /**
     * Sets displayed GUI elements according to the current settings state
     */
    private void updateDisplayState()
    {
        btnGameMode.text = I18n.format("selectWorld.gameMode") + ": " + I18n.format("selectWorld.gameMode." + gameMode);
        gameModeDesc1 = I18n.format("selectWorld.gameMode." + gameMode + ".line1");
        gameModeDesc2 = I18n.format("selectWorld.gameMode." + gameMode + ".line2");
        btnMapFeatures.text = I18n.format("selectWorld.mapFeatures") + " ";

        if (generateStructuresEnabled)
        {
            btnMapFeatures.text = btnMapFeatures.text + I18n.format("options.on");
        }
        else
        {
            btnMapFeatures.text = btnMapFeatures.text + I18n.format("options.off");
        }

        btnBonusItems.text = I18n.format("selectWorld.bonusItems") + " ";

        if (bonusChestEnabled && !hardCoreMode)
        {
            btnBonusItems.text = btnBonusItems.text + I18n.format("options.on");
        }
        else
        {
            btnBonusItems.text = btnBonusItems.text + I18n.format("options.off");
        }

        btnMapType.text = I18n.format("selectWorld.mapType") + " " + I18n.format(WorldType.WORLD_TYPES[selectedIndex].getTranslateName());
        btnAllowCommands.text = I18n.format("selectWorld.allowCommands") + " ";

        if (allowCheats && !hardCoreMode)
        {
            btnAllowCommands.text = btnAllowCommands.text + I18n.format("options.on");
        }
        else
        {
            btnAllowCommands.text = btnAllowCommands.text + I18n.format("options.off");
        }
    }

    /**
     * Ensures that a proposed directory name doesn't collide with existing names.
     * Returns the name, possibly modified to avoid collisions.
     */
    public static String getUncollidingSaveDirName(ISaveFormat saveLoader, String name)
    {
        name = name.replaceAll("[\\./\"]", "_");

        for (String s : DISALLOWED_FILENAMES)
        {
            if (name.equalsIgnoreCase(s))
            {
                name = "_" + name + "_";
            }
        }

        while (saveLoader.getWorldInfo(name) != null)
        {
            name = name + "-";
        }

        return name;
    }

    /**
     * Called when the screen is unloaded. Used to disable keyboard repeat events
     */
    public void onClosed()
    {
        Keyboard.enableRepeatEvents(false);
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    public void actionPerformed(ButtonWidget button)
    {
        if (button.enabled)
        {
            if (button.id == 1)
            {
                Minecraft.openScreen(parentScreen);
            }
            else if (button.id == 0)
            {
                Minecraft.openScreen(null);

                if (alreadyGenerated)
                {
                    return;
                }

                alreadyGenerated = true;
                long i = (new Random()).nextLong();
                String s = worldSeedField.getText();

                if (!Strings.isNullOrEmpty(s))
                {
                    try
                    {
                        long j = Long.parseLong(s);

                        if (j != 0L)
                        {
                            i = j;
                        }
                    }
                    catch (NumberFormatException var7)
                    {
                        i = s.hashCode();
                    }
                }

                WorldSettings worldsettings = new WorldSettings(i, GameType.getByName(gameMode), generateStructuresEnabled, hardCoreMode, WorldType.WORLD_TYPES[selectedIndex]);
                worldsettings.setGeneratorOptions(chunkProviderSettingsJson);

                if (bonusChestEnabled && !hardCoreMode)
                {
                    worldsettings.enableBonusChest();
                }

                if (allowCheats && !hardCoreMode)
                {
                    worldsettings.enableCommands();
                }

                minecraft.launchIntegratedServer(saveDirName, worldNameField.getText().trim(), worldsettings);
            }
            else if (button.id == 3)
            {
                toggleMoreWorldOptions();
            }
            else if (button.id == 2)
            {
                if ("survival".equals(gameMode))
                {
                    if (!allowCheatsWasSetByUser)
                    {
                        allowCheats = false;
                    }

                    hardCoreMode = false;
                    gameMode = "hardcore";
                    hardCoreMode = true;
                    btnAllowCommands.enabled = false;
                    btnBonusItems.enabled = false;
                    updateDisplayState();
                }
                else if ("hardcore".equals(gameMode))
                {
                    if (!allowCheatsWasSetByUser)
                    {
                        allowCheats = true;
                    }

                    hardCoreMode = false;
                    gameMode = "creative";
                    updateDisplayState();
                    hardCoreMode = false;
                    btnAllowCommands.enabled = true;
                    btnBonusItems.enabled = true;
                }
                else
                {
                    if (!allowCheatsWasSetByUser)
                    {
                        allowCheats = false;
                    }

                    gameMode = "survival";
                    updateDisplayState();
                    btnAllowCommands.enabled = true;
                    btnBonusItems.enabled = true;
                    hardCoreMode = false;
                }

                updateDisplayState();
            }
            else if (button.id == 4)
            {
                generateStructuresEnabled = !generateStructuresEnabled;
                updateDisplayState();
            }
            else if (button.id == 7)
            {
                bonusChestEnabled = !bonusChestEnabled;
                updateDisplayState();
            }
            else if (button.id == 5)
            {
                ++selectedIndex;

                if (selectedIndex >= WorldType.WORLD_TYPES.length)
                {
                    selectedIndex = 0;
                }

                while (!canSelectCurWorldType())
                {
                    ++selectedIndex;

                    if (selectedIndex >= WorldType.WORLD_TYPES.length)
                    {
                        selectedIndex = 0;
                    }
                }

                chunkProviderSettingsJson = "";
                updateDisplayState();
                showMoreWorldOptions(inMoreWorldOptionsDisplay);
            }
            else if (button.id == 6)
            {
                allowCheatsWasSetByUser = true;
                allowCheats = !allowCheats;
                updateDisplayState();
            }
            else if (button.id == 8)
            {
                if (WorldType.WORLD_TYPES[selectedIndex] == WorldType.FLAT)
                {
                    Minecraft.openScreen(new GuiCreateFlatWorld(this, chunkProviderSettingsJson));
                }
                else
                {
                    Minecraft.openScreen(new GuiCustomizeWorldScreen(this, chunkProviderSettingsJson));
                }
            }
        }
    }

    /**
     * Returns whether the currently-selected world type is actually acceptable for selection
     * Used to hide the "debug" world type unless the shift key is depressed.
     */
    private boolean canSelectCurWorldType()
    {
        WorldType worldtype = WorldType.WORLD_TYPES[selectedIndex];

        if (worldtype != null && worldtype.getCanBeCreated())
        {
            return worldtype != WorldType.DEBUG_WORLD || Screen.hasShiftDown();
        }
        else
        {
            return false;
        }
    }

    /**
     * Toggles between initial world-creation display, and "more options" display.
     * Called when user clicks "More World Options..." or "Done" (same button, different labels depending on current
     * display).
     */
    private void toggleMoreWorldOptions()
    {
        showMoreWorldOptions(!inMoreWorldOptionsDisplay);
    }

    /**
     * Shows additional world-creation options if toggle is true, otherwise shows main world-creation elements
     */
    private void showMoreWorldOptions(boolean toggle)
    {
        inMoreWorldOptionsDisplay = toggle;

        if (WorldType.WORLD_TYPES[selectedIndex] == WorldType.DEBUG_WORLD)
        {
            btnGameMode.visible = !inMoreWorldOptionsDisplay;
            btnGameMode.enabled = false;

            if (savedGameMode == null)
            {
                savedGameMode = gameMode;
            }

            gameMode = "spectator";
            btnMapFeatures.visible = false;
            btnBonusItems.visible = false;
            btnMapType.visible = inMoreWorldOptionsDisplay;
            btnAllowCommands.visible = false;
            btnCustomizeType.visible = false;
        }
        else
        {
            btnGameMode.visible = !inMoreWorldOptionsDisplay;
            btnGameMode.enabled = true;

            if (savedGameMode != null)
            {
                gameMode = savedGameMode;
                savedGameMode = null;
            }

            btnMapFeatures.visible = inMoreWorldOptionsDisplay && WorldType.WORLD_TYPES[selectedIndex] != WorldType.CUSTOMIZED;
            btnBonusItems.visible = inMoreWorldOptionsDisplay;
            btnMapType.visible = inMoreWorldOptionsDisplay;
            btnAllowCommands.visible = inMoreWorldOptionsDisplay;
            btnCustomizeType.visible = inMoreWorldOptionsDisplay && (WorldType.WORLD_TYPES[selectedIndex] == WorldType.FLAT || WorldType.WORLD_TYPES[selectedIndex] == WorldType.CUSTOMIZED);
        }

        updateDisplayState();

        if (inMoreWorldOptionsDisplay)
        {
            btnMoreOptions.text = I18n.format("gui.done");
        }
        else
        {
            btnMoreOptions.text = I18n.format("selectWorld.moreWorldOptions");
        }
    }

    /**
     * Fired when a key is typed (except F11 which toggles full screen). This is the equivalent of
     * KeyListener.keyTyped(KeyEvent e). Args : character (character on the key), keyCode (lwjgl Keyboard key code)
     */
    public void keyPressed(int key, char c)
    {
        if (worldNameField.isFocused() && !inMoreWorldOptionsDisplay)
        {
            worldNameField.textboxKeyTyped(c, key);
            worldName = worldNameField.getText();
        }
        else if (worldSeedField.isFocused() && inMoreWorldOptionsDisplay)
        {
            worldSeedField.textboxKeyTyped(c, key);
            worldSeed = worldSeedField.getText();
        }

        if (key == 28 || key == 156)
        {
            actionPerformed(widgets.get(0));
        }

        (widgets.get(0)).enabled = !worldNameField.getText().isEmpty();
        calcSaveDirName();
    }

    /**
     * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
     */
    public void mousePressed(int mouseX, int mouseY, int button)
    {
        super.mousePressed(mouseX, mouseY, button);

        if (inMoreWorldOptionsDisplay)
        {
            worldSeedField.mouseClicked(mouseX, mouseY, button);
        }
        else
        {
            worldNameField.mouseClicked(mouseX, mouseY, button);
        }
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void draw(int mouseX, int mouseY, float partialTick)
    {
        drawDefaultBackground();
        drawCenteredString(font, I18n.format("selectWorld.create"), width / 2, 20, -1);

        if (inMoreWorldOptionsDisplay)
        {
            drawString(font, I18n.format("selectWorld.enterSeed"), width / 2 - 100, 47, -6250336);
            drawString(font, I18n.format("selectWorld.seedInfo"), width / 2 - 100, 85, -6250336);

            if (btnMapFeatures.visible)
            {
                drawString(font, I18n.format("selectWorld.mapFeatures.info"), width / 2 - 150, 122, -6250336);
            }

            if (btnAllowCommands.visible)
            {
                drawString(font, I18n.format("selectWorld.allowCommands.info"), width / 2 - 150, 172, -6250336);
            }

            worldSeedField.drawTextBox();

            if (WorldType.WORLD_TYPES[selectedIndex].showWorldInfoNotice())
            {
                font.drawSplitString(I18n.format(WorldType.WORLD_TYPES[selectedIndex].getTranslatedInfo()), btnMapType.x + 2, btnMapType.y + 22, btnMapType.width, 10526880);
            }
        }
        else
        {
            drawString(font, I18n.format("selectWorld.enterName"), width / 2 - 100, 47, -6250336);
            drawString(font, I18n.format("selectWorld.resultFolder") + " " + saveDirName, width / 2 - 100, 85, -6250336);
            worldNameField.drawTextBox();
            drawString(font, gameModeDesc1, width / 2 - 100, 137, -6250336);
            drawString(font, gameModeDesc2, width / 2 - 100, 149, -6250336);
        }

        super.draw(mouseX, mouseY, partialTick);
    }

    /**
     * Set the initial values of a new world to create, from the values from an existing world.
     *  
     * Called after construction when a user selects the "Recreate" button.
     */
    public void recreateFromExistingWorld(WorldInfo original)
    {
        worldName = I18n.format("selectWorld.newWorld.copyOf", original.getWorldName());
        worldSeed = original.getSeed() + "";
        selectedIndex = original.getTerrainType().getWorldTypeID();
        chunkProviderSettingsJson = original.getGeneratorOptions();
        generateStructuresEnabled = original.isMapFeaturesEnabled();
        allowCheats = original.areCommandsAllowed();

        if (original.isHardcoreModeEnabled())
        {
            gameMode = "hardcore";
        }
        else if (original.getGameType().isSurvivalOrAdventure())
        {
            gameMode = "survival";
        }
        else if (original.getGameType().isCreative())
        {
            gameMode = "creative";
        }
    }
}
