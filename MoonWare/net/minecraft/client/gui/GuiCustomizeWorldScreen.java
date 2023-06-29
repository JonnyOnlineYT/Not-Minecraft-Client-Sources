package net.minecraft.client.gui;

import com.google.common.base.Predicate;
import com.google.common.primitives.Floats;

import java.util.Random;
import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Biomes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGeneratorSettings;

public class GuiCustomizeWorldScreen extends Screen implements GuiSlider.FormatHelper, GuiPageButtonList.GuiResponder
{
    private final GuiCreateWorld parent;
    protected String title = "Customize World Settings";
    protected String subtitle = "Page 1 of 3";
    protected String pageTitle = "Basic Settings";
    protected String[] pageNames = new String[4];
    private GuiPageButtonList list;
    private ButtonWidget done;
    private ButtonWidget randomize;
    private ButtonWidget defaults;
    private ButtonWidget previousPage;
    private ButtonWidget nextPage;
    private ButtonWidget confirm;
    private ButtonWidget cancel;
    private ButtonWidget presets;
    private boolean settingsModified;
    private int confirmMode;
    private boolean confirmDismissed;
    private final Predicate<String> numberFilter = new Predicate<String>()
    {
        public boolean apply(@Nullable String p_apply_1_)
        {
            Float f = Floats.tryParse(p_apply_1_);
            return p_apply_1_.isEmpty() || f != null && Floats.isFinite(f.floatValue()) && f.floatValue() >= 0.0F;
        }
    };
    private final ChunkGeneratorSettings.Factory defaultSettings = new ChunkGeneratorSettings.Factory();
    private ChunkGeneratorSettings.Factory settings;

    /** A Random instance for this world customization */
    private final Random random = new Random();

    public GuiCustomizeWorldScreen(Screen p_i45521_1_, String p_i45521_2_)
    {
        parent = (GuiCreateWorld)p_i45521_1_;
        loadValues(p_i45521_2_);
    }

    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    public void init()
    {
        int i = 0;
        int j = 0;

        if (list != null)
        {
            i = list.getPage();
            j = list.getAmountScrolled();
        }

        title = I18n.format("options.customizeTitle");
        widgets.clear();
        previousPage = addButton(new ButtonWidget(302, 20, 5, 80, 20, I18n.format("createWorld.customize.custom.prev")));
        nextPage = addButton(new ButtonWidget(303, width - 100, 5, 80, 20, I18n.format("createWorld.customize.custom.next")));
        defaults = addButton(new ButtonWidget(304, width / 2 - 187, height - 27, 90, 20, I18n.format("createWorld.customize.custom.defaults")));
        randomize = addButton(new ButtonWidget(301, width / 2 - 92, height - 27, 90, 20, I18n.format("createWorld.customize.custom.randomize")));
        presets = addButton(new ButtonWidget(305, width / 2 + 3, height - 27, 90, 20, I18n.format("createWorld.customize.custom.presets")));
        done = addButton(new ButtonWidget(300, width / 2 + 98, height - 27, 90, 20, I18n.format("gui.done")));
        defaults.enabled = settingsModified;
        confirm = new ButtonWidget(306, width / 2 - 55, 160, 50, 20, I18n.format("gui.yes"));
        confirm.visible = false;
        widgets.add(confirm);
        cancel = new ButtonWidget(307, width / 2 + 5, 160, 50, 20, I18n.format("gui.no"));
        cancel.visible = false;
        widgets.add(cancel);

        if (confirmMode != 0)
        {
            confirm.visible = true;
            cancel.visible = true;
        }

        createPagedList();

        if (i != 0)
        {
            list.setPage(i);
            list.scrollBy(j);
            updatePageControls();
        }
    }

    /**
     * Handles mouse input.
     */
    public void handleMouseInput()
    {
        super.handleMouseInput();
        list.handleMouseInput();
    }

    private void createPagedList()
    {
        GuiPageButtonList.GuiListEntry[] aguipagebuttonlist$guilistentry = {new GuiPageButtonList.GuiSlideEntry(160, I18n.format("createWorld.customize.custom.seaLevel"), true, this, 1.0F, 255.0F, (float) settings.seaLevel), new GuiPageButtonList.GuiButtonEntry(148, I18n.format("createWorld.customize.custom.useCaves"), true, settings.useCaves), new GuiPageButtonList.GuiButtonEntry(150, I18n.format("createWorld.customize.custom.useStrongholds"), true, settings.useStrongholds), new GuiPageButtonList.GuiButtonEntry(151, I18n.format("createWorld.customize.custom.useVillages"), true, settings.useVillages), new GuiPageButtonList.GuiButtonEntry(152, I18n.format("createWorld.customize.custom.useMineShafts"), true, settings.useMineShafts), new GuiPageButtonList.GuiButtonEntry(153, I18n.format("createWorld.customize.custom.useTemples"), true, settings.useTemples), new GuiPageButtonList.GuiButtonEntry(210, I18n.format("createWorld.customize.custom.useMonuments"), true, settings.useMonuments), new GuiPageButtonList.GuiButtonEntry(211, I18n.format("createWorld.customize.custom.useMansions"), true, settings.field_191076_A), new GuiPageButtonList.GuiButtonEntry(154, I18n.format("createWorld.customize.custom.useRavines"), true, settings.useRavines), new GuiPageButtonList.GuiButtonEntry(149, I18n.format("createWorld.customize.custom.useDungeons"), true, settings.useDungeons), new GuiPageButtonList.GuiSlideEntry(157, I18n.format("createWorld.customize.custom.dungeonChance"), true, this, 1.0F, 100.0F, (float) settings.dungeonChance), new GuiPageButtonList.GuiButtonEntry(155, I18n.format("createWorld.customize.custom.useWaterLakes"), true, settings.useWaterLakes), new GuiPageButtonList.GuiSlideEntry(158, I18n.format("createWorld.customize.custom.waterLakeChance"), true, this, 1.0F, 100.0F, (float) settings.waterLakeChance), new GuiPageButtonList.GuiButtonEntry(156, I18n.format("createWorld.customize.custom.useLavaLakes"), true, settings.useLavaLakes), new GuiPageButtonList.GuiSlideEntry(159, I18n.format("createWorld.customize.custom.lavaLakeChance"), true, this, 10.0F, 100.0F, (float) settings.lavaLakeChance), new GuiPageButtonList.GuiButtonEntry(161, I18n.format("createWorld.customize.custom.useLavaOceans"), true, settings.useLavaOceans), new GuiPageButtonList.GuiSlideEntry(162, I18n.format("createWorld.customize.custom.fixedBiome"), true, this, -1.0F, 37.0F, (float) settings.fixedBiome), new GuiPageButtonList.GuiSlideEntry(163, I18n.format("createWorld.customize.custom.biomeSize"), true, this, 1.0F, 8.0F, (float) settings.biomeSize), new GuiPageButtonList.GuiSlideEntry(164, I18n.format("createWorld.customize.custom.riverSize"), true, this, 1.0F, 5.0F, (float) settings.riverSize)};
        GuiPageButtonList.GuiListEntry[] aguipagebuttonlist$guilistentry1 = {new GuiPageButtonList.GuiLabelEntry(416, I18n.format("tile.dirt.name"), false), null, new GuiPageButtonList.GuiSlideEntry(165, I18n.format("createWorld.customize.custom.size"), false, this, 1.0F, 50.0F, (float) settings.dirtSize), new GuiPageButtonList.GuiSlideEntry(166, I18n.format("createWorld.customize.custom.count"), false, this, 0.0F, 40.0F, (float) settings.dirtCount), new GuiPageButtonList.GuiSlideEntry(167, I18n.format("createWorld.customize.custom.minHeight"), false, this, 0.0F, 255.0F, (float) settings.dirtMinHeight), new GuiPageButtonList.GuiSlideEntry(168, I18n.format("createWorld.customize.custom.maxHeight"), false, this, 0.0F, 255.0F, (float) settings.dirtMaxHeight), new GuiPageButtonList.GuiLabelEntry(417, I18n.format("tile.gravel.name"), false), null, new GuiPageButtonList.GuiSlideEntry(169, I18n.format("createWorld.customize.custom.size"), false, this, 1.0F, 50.0F, (float) settings.gravelSize), new GuiPageButtonList.GuiSlideEntry(170, I18n.format("createWorld.customize.custom.count"), false, this, 0.0F, 40.0F, (float) settings.gravelCount), new GuiPageButtonList.GuiSlideEntry(171, I18n.format("createWorld.customize.custom.minHeight"), false, this, 0.0F, 255.0F, (float) settings.gravelMinHeight), new GuiPageButtonList.GuiSlideEntry(172, I18n.format("createWorld.customize.custom.maxHeight"), false, this, 0.0F, 255.0F, (float) settings.gravelMaxHeight), new GuiPageButtonList.GuiLabelEntry(418, I18n.format("tile.stone.granite.name"), false), null, new GuiPageButtonList.GuiSlideEntry(173, I18n.format("createWorld.customize.custom.size"), false, this, 1.0F, 50.0F, (float) settings.graniteSize), new GuiPageButtonList.GuiSlideEntry(174, I18n.format("createWorld.customize.custom.count"), false, this, 0.0F, 40.0F, (float) settings.graniteCount), new GuiPageButtonList.GuiSlideEntry(175, I18n.format("createWorld.customize.custom.minHeight"), false, this, 0.0F, 255.0F, (float) settings.graniteMinHeight), new GuiPageButtonList.GuiSlideEntry(176, I18n.format("createWorld.customize.custom.maxHeight"), false, this, 0.0F, 255.0F, (float) settings.graniteMaxHeight), new GuiPageButtonList.GuiLabelEntry(419, I18n.format("tile.stone.diorite.name"), false), null, new GuiPageButtonList.GuiSlideEntry(177, I18n.format("createWorld.customize.custom.size"), false, this, 1.0F, 50.0F, (float) settings.dioriteSize), new GuiPageButtonList.GuiSlideEntry(178, I18n.format("createWorld.customize.custom.count"), false, this, 0.0F, 40.0F, (float) settings.dioriteCount), new GuiPageButtonList.GuiSlideEntry(179, I18n.format("createWorld.customize.custom.minHeight"), false, this, 0.0F, 255.0F, (float) settings.dioriteMinHeight), new GuiPageButtonList.GuiSlideEntry(180, I18n.format("createWorld.customize.custom.maxHeight"), false, this, 0.0F, 255.0F, (float) settings.dioriteMaxHeight), new GuiPageButtonList.GuiLabelEntry(420, I18n.format("tile.stone.andesite.name"), false), null, new GuiPageButtonList.GuiSlideEntry(181, I18n.format("createWorld.customize.custom.size"), false, this, 1.0F, 50.0F, (float) settings.andesiteSize), new GuiPageButtonList.GuiSlideEntry(182, I18n.format("createWorld.customize.custom.count"), false, this, 0.0F, 40.0F, (float) settings.andesiteCount), new GuiPageButtonList.GuiSlideEntry(183, I18n.format("createWorld.customize.custom.minHeight"), false, this, 0.0F, 255.0F, (float) settings.andesiteMinHeight), new GuiPageButtonList.GuiSlideEntry(184, I18n.format("createWorld.customize.custom.maxHeight"), false, this, 0.0F, 255.0F, (float) settings.andesiteMaxHeight), new GuiPageButtonList.GuiLabelEntry(421, I18n.format("tile.oreCoal.name"), false), null, new GuiPageButtonList.GuiSlideEntry(185, I18n.format("createWorld.customize.custom.size"), false, this, 1.0F, 50.0F, (float) settings.coalSize), new GuiPageButtonList.GuiSlideEntry(186, I18n.format("createWorld.customize.custom.count"), false, this, 0.0F, 40.0F, (float) settings.coalCount), new GuiPageButtonList.GuiSlideEntry(187, I18n.format("createWorld.customize.custom.minHeight"), false, this, 0.0F, 255.0F, (float) settings.coalMinHeight), new GuiPageButtonList.GuiSlideEntry(189, I18n.format("createWorld.customize.custom.maxHeight"), false, this, 0.0F, 255.0F, (float) settings.coalMaxHeight), new GuiPageButtonList.GuiLabelEntry(422, I18n.format("tile.oreIron.name"), false), null, new GuiPageButtonList.GuiSlideEntry(190, I18n.format("createWorld.customize.custom.size"), false, this, 1.0F, 50.0F, (float) settings.ironSize), new GuiPageButtonList.GuiSlideEntry(191, I18n.format("createWorld.customize.custom.count"), false, this, 0.0F, 40.0F, (float) settings.ironCount), new GuiPageButtonList.GuiSlideEntry(192, I18n.format("createWorld.customize.custom.minHeight"), false, this, 0.0F, 255.0F, (float) settings.ironMinHeight), new GuiPageButtonList.GuiSlideEntry(193, I18n.format("createWorld.customize.custom.maxHeight"), false, this, 0.0F, 255.0F, (float) settings.ironMaxHeight), new GuiPageButtonList.GuiLabelEntry(423, I18n.format("tile.oreGold.name"), false), null, new GuiPageButtonList.GuiSlideEntry(194, I18n.format("createWorld.customize.custom.size"), false, this, 1.0F, 50.0F, (float) settings.goldSize), new GuiPageButtonList.GuiSlideEntry(195, I18n.format("createWorld.customize.custom.count"), false, this, 0.0F, 40.0F, (float) settings.goldCount), new GuiPageButtonList.GuiSlideEntry(196, I18n.format("createWorld.customize.custom.minHeight"), false, this, 0.0F, 255.0F, (float) settings.goldMinHeight), new GuiPageButtonList.GuiSlideEntry(197, I18n.format("createWorld.customize.custom.maxHeight"), false, this, 0.0F, 255.0F, (float) settings.goldMaxHeight), new GuiPageButtonList.GuiLabelEntry(424, I18n.format("tile.oreRedstone.name"), false), null, new GuiPageButtonList.GuiSlideEntry(198, I18n.format("createWorld.customize.custom.size"), false, this, 1.0F, 50.0F, (float) settings.redstoneSize), new GuiPageButtonList.GuiSlideEntry(199, I18n.format("createWorld.customize.custom.count"), false, this, 0.0F, 40.0F, (float) settings.redstoneCount), new GuiPageButtonList.GuiSlideEntry(200, I18n.format("createWorld.customize.custom.minHeight"), false, this, 0.0F, 255.0F, (float) settings.redstoneMinHeight), new GuiPageButtonList.GuiSlideEntry(201, I18n.format("createWorld.customize.custom.maxHeight"), false, this, 0.0F, 255.0F, (float) settings.redstoneMaxHeight), new GuiPageButtonList.GuiLabelEntry(425, I18n.format("tile.oreDiamond.name"), false), null, new GuiPageButtonList.GuiSlideEntry(202, I18n.format("createWorld.customize.custom.size"), false, this, 1.0F, 50.0F, (float) settings.diamondSize), new GuiPageButtonList.GuiSlideEntry(203, I18n.format("createWorld.customize.custom.count"), false, this, 0.0F, 40.0F, (float) settings.diamondCount), new GuiPageButtonList.GuiSlideEntry(204, I18n.format("createWorld.customize.custom.minHeight"), false, this, 0.0F, 255.0F, (float) settings.diamondMinHeight), new GuiPageButtonList.GuiSlideEntry(205, I18n.format("createWorld.customize.custom.maxHeight"), false, this, 0.0F, 255.0F, (float) settings.diamondMaxHeight), new GuiPageButtonList.GuiLabelEntry(426, I18n.format("tile.oreLapis.name"), false), null, new GuiPageButtonList.GuiSlideEntry(206, I18n.format("createWorld.customize.custom.size"), false, this, 1.0F, 50.0F, (float) settings.lapisSize), new GuiPageButtonList.GuiSlideEntry(207, I18n.format("createWorld.customize.custom.count"), false, this, 0.0F, 40.0F, (float) settings.lapisCount), new GuiPageButtonList.GuiSlideEntry(208, I18n.format("createWorld.customize.custom.center"), false, this, 0.0F, 255.0F, (float) settings.lapisCenterHeight), new GuiPageButtonList.GuiSlideEntry(209, I18n.format("createWorld.customize.custom.spread"), false, this, 0.0F, 255.0F, (float) settings.lapisSpread)};
        GuiPageButtonList.GuiListEntry[] aguipagebuttonlist$guilistentry2 = {new GuiPageButtonList.GuiSlideEntry(100, I18n.format("createWorld.customize.custom.mainNoiseScaleX"), false, this, 1.0F, 5000.0F, settings.mainNoiseScaleX), new GuiPageButtonList.GuiSlideEntry(101, I18n.format("createWorld.customize.custom.mainNoiseScaleY"), false, this, 1.0F, 5000.0F, settings.mainNoiseScaleY), new GuiPageButtonList.GuiSlideEntry(102, I18n.format("createWorld.customize.custom.mainNoiseScaleZ"), false, this, 1.0F, 5000.0F, settings.mainNoiseScaleZ), new GuiPageButtonList.GuiSlideEntry(103, I18n.format("createWorld.customize.custom.depthNoiseScaleX"), false, this, 1.0F, 2000.0F, settings.depthNoiseScaleX), new GuiPageButtonList.GuiSlideEntry(104, I18n.format("createWorld.customize.custom.depthNoiseScaleZ"), false, this, 1.0F, 2000.0F, settings.depthNoiseScaleZ), new GuiPageButtonList.GuiSlideEntry(105, I18n.format("createWorld.customize.custom.depthNoiseScaleExponent"), false, this, 0.01F, 20.0F, settings.depthNoiseScaleExponent), new GuiPageButtonList.GuiSlideEntry(106, I18n.format("createWorld.customize.custom.baseSize"), false, this, 1.0F, 25.0F, settings.baseSize), new GuiPageButtonList.GuiSlideEntry(107, I18n.format("createWorld.customize.custom.coordinateScale"), false, this, 1.0F, 6000.0F, settings.coordinateScale), new GuiPageButtonList.GuiSlideEntry(108, I18n.format("createWorld.customize.custom.heightScale"), false, this, 1.0F, 6000.0F, settings.heightScale), new GuiPageButtonList.GuiSlideEntry(109, I18n.format("createWorld.customize.custom.stretchY"), false, this, 0.01F, 50.0F, settings.stretchY), new GuiPageButtonList.GuiSlideEntry(110, I18n.format("createWorld.customize.custom.upperLimitScale"), false, this, 1.0F, 5000.0F, settings.upperLimitScale), new GuiPageButtonList.GuiSlideEntry(111, I18n.format("createWorld.customize.custom.lowerLimitScale"), false, this, 1.0F, 5000.0F, settings.lowerLimitScale), new GuiPageButtonList.GuiSlideEntry(112, I18n.format("createWorld.customize.custom.biomeDepthWeight"), false, this, 1.0F, 20.0F, settings.biomeDepthWeight), new GuiPageButtonList.GuiSlideEntry(113, I18n.format("createWorld.customize.custom.biomeDepthOffset"), false, this, 0.0F, 20.0F, settings.biomeDepthOffset), new GuiPageButtonList.GuiSlideEntry(114, I18n.format("createWorld.customize.custom.biomeScaleWeight"), false, this, 1.0F, 20.0F, settings.biomeScaleWeight), new GuiPageButtonList.GuiSlideEntry(115, I18n.format("createWorld.customize.custom.biomeScaleOffset"), false, this, 0.0F, 20.0F, settings.biomeScaleOffset)};
        GuiPageButtonList.GuiListEntry[] aguipagebuttonlist$guilistentry3 = {new GuiPageButtonList.GuiLabelEntry(400, I18n.format("createWorld.customize.custom.mainNoiseScaleX") + ":", false), new GuiPageButtonList.EditBoxEntry(132, String.format("%5.3f", settings.mainNoiseScaleX), false, numberFilter), new GuiPageButtonList.GuiLabelEntry(401, I18n.format("createWorld.customize.custom.mainNoiseScaleY") + ":", false), new GuiPageButtonList.EditBoxEntry(133, String.format("%5.3f", settings.mainNoiseScaleY), false, numberFilter), new GuiPageButtonList.GuiLabelEntry(402, I18n.format("createWorld.customize.custom.mainNoiseScaleZ") + ":", false), new GuiPageButtonList.EditBoxEntry(134, String.format("%5.3f", settings.mainNoiseScaleZ), false, numberFilter), new GuiPageButtonList.GuiLabelEntry(403, I18n.format("createWorld.customize.custom.depthNoiseScaleX") + ":", false), new GuiPageButtonList.EditBoxEntry(135, String.format("%5.3f", settings.depthNoiseScaleX), false, numberFilter), new GuiPageButtonList.GuiLabelEntry(404, I18n.format("createWorld.customize.custom.depthNoiseScaleZ") + ":", false), new GuiPageButtonList.EditBoxEntry(136, String.format("%5.3f", settings.depthNoiseScaleZ), false, numberFilter), new GuiPageButtonList.GuiLabelEntry(405, I18n.format("createWorld.customize.custom.depthNoiseScaleExponent") + ":", false), new GuiPageButtonList.EditBoxEntry(137, String.format("%2.3f", settings.depthNoiseScaleExponent), false, numberFilter), new GuiPageButtonList.GuiLabelEntry(406, I18n.format("createWorld.customize.custom.baseSize") + ":", false), new GuiPageButtonList.EditBoxEntry(138, String.format("%2.3f", settings.baseSize), false, numberFilter), new GuiPageButtonList.GuiLabelEntry(407, I18n.format("createWorld.customize.custom.coordinateScale") + ":", false), new GuiPageButtonList.EditBoxEntry(139, String.format("%5.3f", settings.coordinateScale), false, numberFilter), new GuiPageButtonList.GuiLabelEntry(408, I18n.format("createWorld.customize.custom.heightScale") + ":", false), new GuiPageButtonList.EditBoxEntry(140, String.format("%5.3f", settings.heightScale), false, numberFilter), new GuiPageButtonList.GuiLabelEntry(409, I18n.format("createWorld.customize.custom.stretchY") + ":", false), new GuiPageButtonList.EditBoxEntry(141, String.format("%2.3f", settings.stretchY), false, numberFilter), new GuiPageButtonList.GuiLabelEntry(410, I18n.format("createWorld.customize.custom.upperLimitScale") + ":", false), new GuiPageButtonList.EditBoxEntry(142, String.format("%5.3f", settings.upperLimitScale), false, numberFilter), new GuiPageButtonList.GuiLabelEntry(411, I18n.format("createWorld.customize.custom.lowerLimitScale") + ":", false), new GuiPageButtonList.EditBoxEntry(143, String.format("%5.3f", settings.lowerLimitScale), false, numberFilter), new GuiPageButtonList.GuiLabelEntry(412, I18n.format("createWorld.customize.custom.biomeDepthWeight") + ":", false), new GuiPageButtonList.EditBoxEntry(144, String.format("%2.3f", settings.biomeDepthWeight), false, numberFilter), new GuiPageButtonList.GuiLabelEntry(413, I18n.format("createWorld.customize.custom.biomeDepthOffset") + ":", false), new GuiPageButtonList.EditBoxEntry(145, String.format("%2.3f", settings.biomeDepthOffset), false, numberFilter), new GuiPageButtonList.GuiLabelEntry(414, I18n.format("createWorld.customize.custom.biomeScaleWeight") + ":", false), new GuiPageButtonList.EditBoxEntry(146, String.format("%2.3f", settings.biomeScaleWeight), false, numberFilter), new GuiPageButtonList.GuiLabelEntry(415, I18n.format("createWorld.customize.custom.biomeScaleOffset") + ":", false), new GuiPageButtonList.EditBoxEntry(147, String.format("%2.3f", settings.biomeScaleOffset), false, numberFilter)};
        list = new GuiPageButtonList(minecraft, width, height, 32, height - 32, 25, this, aguipagebuttonlist$guilistentry, aguipagebuttonlist$guilistentry1, aguipagebuttonlist$guilistentry2, aguipagebuttonlist$guilistentry3);

        for (int i = 0; i < 4; ++i)
        {
            pageNames[i] = I18n.format("createWorld.customize.custom.page" + i);
        }

        updatePageControls();
    }

    public String saveValues()
    {
        return settings.toString().replace("\n", "");
    }

    public void loadValues(String p_175324_1_)
    {
        if (p_175324_1_ != null && !p_175324_1_.isEmpty())
        {
            settings = ChunkGeneratorSettings.Factory.jsonToFactory(p_175324_1_);
        }
        else
        {
            settings = new ChunkGeneratorSettings.Factory();
        }
    }

    public void setEntryValue(int id, String value)
    {
        float f = 0.0F;

        try
        {
            f = Float.parseFloat(value);
        }
        catch (NumberFormatException var5)
        {
        }

        float f1 = 0.0F;

        switch (id)
        {
            case 132:
                settings.mainNoiseScaleX = MathHelper.clamp(f, 1.0F, 5000.0F);
                f1 = settings.mainNoiseScaleX;
                break;

            case 133:
                settings.mainNoiseScaleY = MathHelper.clamp(f, 1.0F, 5000.0F);
                f1 = settings.mainNoiseScaleY;
                break;

            case 134:
                settings.mainNoiseScaleZ = MathHelper.clamp(f, 1.0F, 5000.0F);
                f1 = settings.mainNoiseScaleZ;
                break;

            case 135:
                settings.depthNoiseScaleX = MathHelper.clamp(f, 1.0F, 2000.0F);
                f1 = settings.depthNoiseScaleX;
                break;

            case 136:
                settings.depthNoiseScaleZ = MathHelper.clamp(f, 1.0F, 2000.0F);
                f1 = settings.depthNoiseScaleZ;
                break;

            case 137:
                settings.depthNoiseScaleExponent = MathHelper.clamp(f, 0.01F, 20.0F);
                f1 = settings.depthNoiseScaleExponent;
                break;

            case 138:
                settings.baseSize = MathHelper.clamp(f, 1.0F, 25.0F);
                f1 = settings.baseSize;
                break;

            case 139:
                settings.coordinateScale = MathHelper.clamp(f, 1.0F, 6000.0F);
                f1 = settings.coordinateScale;
                break;

            case 140:
                settings.heightScale = MathHelper.clamp(f, 1.0F, 6000.0F);
                f1 = settings.heightScale;
                break;

            case 141:
                settings.stretchY = MathHelper.clamp(f, 0.01F, 50.0F);
                f1 = settings.stretchY;
                break;

            case 142:
                settings.upperLimitScale = MathHelper.clamp(f, 1.0F, 5000.0F);
                f1 = settings.upperLimitScale;
                break;

            case 143:
                settings.lowerLimitScale = MathHelper.clamp(f, 1.0F, 5000.0F);
                f1 = settings.lowerLimitScale;
                break;

            case 144:
                settings.biomeDepthWeight = MathHelper.clamp(f, 1.0F, 20.0F);
                f1 = settings.biomeDepthWeight;
                break;

            case 145:
                settings.biomeDepthOffset = MathHelper.clamp(f, 0.0F, 20.0F);
                f1 = settings.biomeDepthOffset;
                break;

            case 146:
                settings.biomeScaleWeight = MathHelper.clamp(f, 1.0F, 20.0F);
                f1 = settings.biomeScaleWeight;
                break;

            case 147:
                settings.biomeScaleOffset = MathHelper.clamp(f, 0.0F, 20.0F);
                f1 = settings.biomeScaleOffset;
        }

        if (f1 != f && f != 0.0F)
        {
            ((GuiTextField) list.getComponent(id)).setText(getFormattedValue(id, f1));
        }

        ((GuiSlider) list.getComponent(id - 132 + 100)).setSliderValue(f1, false);

        if (!settings.equals(defaultSettings))
        {
            setSettingsModified(true);
        }
    }

    private void setSettingsModified(boolean p_181031_1_)
    {
        settingsModified = p_181031_1_;
        defaults.enabled = p_181031_1_;
    }

    public String getText(int id, String name, float value)
    {
        return name + ": " + getFormattedValue(id, value);
    }

    private String getFormattedValue(int p_175330_1_, float p_175330_2_)
    {
        switch (p_175330_1_)
        {
            case 100:
            case 101:
            case 102:
            case 103:
            case 104:
            case 107:
            case 108:
            case 110:
            case 111:
            case 132:
            case 133:
            case 134:
            case 135:
            case 136:
            case 139:
            case 140:
            case 142:
            case 143:
                return String.format("%5.3f", p_175330_2_);

            case 105:
            case 106:
            case 109:
            case 112:
            case 113:
            case 114:
            case 115:
            case 137:
            case 138:
            case 141:
            case 144:
            case 145:
            case 146:
            case 147:
                return String.format("%2.3f", p_175330_2_);

            case 116:
            case 117:
            case 118:
            case 119:
            case 120:
            case 121:
            case 122:
            case 123:
            case 124:
            case 125:
            case 126:
            case 127:
            case 128:
            case 129:
            case 130:
            case 131:
            case 148:
            case 149:
            case 150:
            case 151:
            case 152:
            case 153:
            case 154:
            case 155:
            case 156:
            case 157:
            case 158:
            case 159:
            case 160:
            case 161:
            default:
                return String.format("%d", (int)p_175330_2_);

            case 162:
                if (p_175330_2_ < 0.0F)
                {
                    return I18n.format("gui.all");
                }
                else if ((int)p_175330_2_ >= Biome.getIdForBiome(Biomes.HELL))
                {
                    Biome biome1 = Biome.getBiomeForId((int)p_175330_2_ + 2);
                    return biome1 != null ? biome1.getBiomeName() : "?";
                }
                else
                {
                    Biome biome = Biome.getBiomeForId((int)p_175330_2_);
                    return biome != null ? biome.getBiomeName() : "?";
                }
        }
    }

    public void setEntryValue(int id, boolean value)
    {
        switch (id)
        {
            case 148:
                settings.useCaves = value;
                break;

            case 149:
                settings.useDungeons = value;
                break;

            case 150:
                settings.useStrongholds = value;
                break;

            case 151:
                settings.useVillages = value;
                break;

            case 152:
                settings.useMineShafts = value;
                break;

            case 153:
                settings.useTemples = value;
                break;

            case 154:
                settings.useRavines = value;
                break;

            case 155:
                settings.useWaterLakes = value;
                break;

            case 156:
                settings.useLavaLakes = value;
                break;

            case 161:
                settings.useLavaOceans = value;
                break;

            case 210:
                settings.useMonuments = value;
                break;

            case 211:
                settings.field_191076_A = value;
        }

        if (!settings.equals(defaultSettings))
        {
            setSettingsModified(true);
        }
    }

    public void setEntryValue(int id, float value)
    {
        switch (id)
        {
            case 100:
                settings.mainNoiseScaleX = value;
                break;

            case 101:
                settings.mainNoiseScaleY = value;
                break;

            case 102:
                settings.mainNoiseScaleZ = value;
                break;

            case 103:
                settings.depthNoiseScaleX = value;
                break;

            case 104:
                settings.depthNoiseScaleZ = value;
                break;

            case 105:
                settings.depthNoiseScaleExponent = value;
                break;

            case 106:
                settings.baseSize = value;
                break;

            case 107:
                settings.coordinateScale = value;
                break;

            case 108:
                settings.heightScale = value;
                break;

            case 109:
                settings.stretchY = value;
                break;

            case 110:
                settings.upperLimitScale = value;
                break;

            case 111:
                settings.lowerLimitScale = value;
                break;

            case 112:
                settings.biomeDepthWeight = value;
                break;

            case 113:
                settings.biomeDepthOffset = value;
                break;

            case 114:
                settings.biomeScaleWeight = value;
                break;

            case 115:
                settings.biomeScaleOffset = value;

            case 116:
            case 117:
            case 118:
            case 119:
            case 120:
            case 121:
            case 122:
            case 123:
            case 124:
            case 125:
            case 126:
            case 127:
            case 128:
            case 129:
            case 130:
            case 131:
            case 132:
            case 133:
            case 134:
            case 135:
            case 136:
            case 137:
            case 138:
            case 139:
            case 140:
            case 141:
            case 142:
            case 143:
            case 144:
            case 145:
            case 146:
            case 147:
            case 148:
            case 149:
            case 150:
            case 151:
            case 152:
            case 153:
            case 154:
            case 155:
            case 156:
            case 161:
            case 188:
            default:
                break;

            case 157:
                settings.dungeonChance = (int)value;
                break;

            case 158:
                settings.waterLakeChance = (int)value;
                break;

            case 159:
                settings.lavaLakeChance = (int)value;
                break;

            case 160:
                settings.seaLevel = (int)value;
                break;

            case 162:
                settings.fixedBiome = (int)value;
                break;

            case 163:
                settings.biomeSize = (int)value;
                break;

            case 164:
                settings.riverSize = (int)value;
                break;

            case 165:
                settings.dirtSize = (int)value;
                break;

            case 166:
                settings.dirtCount = (int)value;
                break;

            case 167:
                settings.dirtMinHeight = (int)value;
                break;

            case 168:
                settings.dirtMaxHeight = (int)value;
                break;

            case 169:
                settings.gravelSize = (int)value;
                break;

            case 170:
                settings.gravelCount = (int)value;
                break;

            case 171:
                settings.gravelMinHeight = (int)value;
                break;

            case 172:
                settings.gravelMaxHeight = (int)value;
                break;

            case 173:
                settings.graniteSize = (int)value;
                break;

            case 174:
                settings.graniteCount = (int)value;
                break;

            case 175:
                settings.graniteMinHeight = (int)value;
                break;

            case 176:
                settings.graniteMaxHeight = (int)value;
                break;

            case 177:
                settings.dioriteSize = (int)value;
                break;

            case 178:
                settings.dioriteCount = (int)value;
                break;

            case 179:
                settings.dioriteMinHeight = (int)value;
                break;

            case 180:
                settings.dioriteMaxHeight = (int)value;
                break;

            case 181:
                settings.andesiteSize = (int)value;
                break;

            case 182:
                settings.andesiteCount = (int)value;
                break;

            case 183:
                settings.andesiteMinHeight = (int)value;
                break;

            case 184:
                settings.andesiteMaxHeight = (int)value;
                break;

            case 185:
                settings.coalSize = (int)value;
                break;

            case 186:
                settings.coalCount = (int)value;
                break;

            case 187:
                settings.coalMinHeight = (int)value;
                break;

            case 189:
                settings.coalMaxHeight = (int)value;
                break;

            case 190:
                settings.ironSize = (int)value;
                break;

            case 191:
                settings.ironCount = (int)value;
                break;

            case 192:
                settings.ironMinHeight = (int)value;
                break;

            case 193:
                settings.ironMaxHeight = (int)value;
                break;

            case 194:
                settings.goldSize = (int)value;
                break;

            case 195:
                settings.goldCount = (int)value;
                break;

            case 196:
                settings.goldMinHeight = (int)value;
                break;

            case 197:
                settings.goldMaxHeight = (int)value;
                break;

            case 198:
                settings.redstoneSize = (int)value;
                break;

            case 199:
                settings.redstoneCount = (int)value;
                break;

            case 200:
                settings.redstoneMinHeight = (int)value;
                break;

            case 201:
                settings.redstoneMaxHeight = (int)value;
                break;

            case 202:
                settings.diamondSize = (int)value;
                break;

            case 203:
                settings.diamondCount = (int)value;
                break;

            case 204:
                settings.diamondMinHeight = (int)value;
                break;

            case 205:
                settings.diamondMaxHeight = (int)value;
                break;

            case 206:
                settings.lapisSize = (int)value;
                break;

            case 207:
                settings.lapisCount = (int)value;
                break;

            case 208:
                settings.lapisCenterHeight = (int)value;
                break;

            case 209:
                settings.lapisSpread = (int)value;
        }

        if (id >= 100 && id < 116)
        {
            Gui gui = list.getComponent(id - 100 + 132);

            if (gui != null)
            {
                ((GuiTextField)gui).setText(getFormattedValue(id, value));
            }
        }

        if (!settings.equals(defaultSettings))
        {
            setSettingsModified(true);
        }
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    public void actionPerformed(ButtonWidget button)
    {
        if (button.enabled)
        {
            switch (button.id)
            {
                case 300:
                    parent.chunkProviderSettingsJson = settings.toString();
                    Minecraft.openScreen(parent);
                    break;

                case 301:
                    for (int i = 0; i < list.getSize(); ++i)
                    {
                        GuiPageButtonList.GuiEntry guipagebuttonlist$guientry = list.getListEntry(i);
                        Gui gui = guipagebuttonlist$guientry.getComponent1();

                        if (gui instanceof ButtonWidget)
                        {
                            ButtonWidget guibutton = (ButtonWidget)gui;

                            if (guibutton instanceof GuiSlider)
                            {
                                float f = ((GuiSlider)guibutton).getSliderPosition() * (0.75F + random.nextFloat() * 0.5F) + (random.nextFloat() * 0.1F - 0.05F);
                                ((GuiSlider)guibutton).setSliderPosition(MathHelper.clamp(f, 0.0F, 1.0F));
                            }
                            else if (guibutton instanceof GuiListButton)
                            {
                                ((GuiListButton)guibutton).setValue(random.nextBoolean());
                            }
                        }

                        Gui gui1 = guipagebuttonlist$guientry.getComponent2();

                        if (gui1 instanceof ButtonWidget)
                        {
                            ButtonWidget guibutton1 = (ButtonWidget)gui1;

                            if (guibutton1 instanceof GuiSlider)
                            {
                                float f1 = ((GuiSlider)guibutton1).getSliderPosition() * (0.75F + random.nextFloat() * 0.5F) + (random.nextFloat() * 0.1F - 0.05F);
                                ((GuiSlider)guibutton1).setSliderPosition(MathHelper.clamp(f1, 0.0F, 1.0F));
                            }
                            else if (guibutton1 instanceof GuiListButton)
                            {
                                ((GuiListButton)guibutton1).setValue(random.nextBoolean());
                            }
                        }
                    }

                    return;

                case 302:
                    list.previousPage();
                    updatePageControls();
                    break;

                case 303:
                    list.nextPage();
                    updatePageControls();
                    break;

                case 304:
                    if (settingsModified)
                    {
                        enterConfirmation(304);
                    }

                    break;

                case 305:
                    Minecraft.openScreen(new GuiScreenCustomizePresets(this));
                    break;

                case 306:
                    exitConfirmation();
                    break;

                case 307:
                    confirmMode = 0;
                    exitConfirmation();
            }
        }
    }

    private void restoreDefaults()
    {
        settings.setDefaults();
        createPagedList();
        setSettingsModified(false);
    }

    private void enterConfirmation(int p_175322_1_)
    {
        confirmMode = p_175322_1_;
        setConfirmationControls(true);
    }

    private void exitConfirmation()
    {
        switch (confirmMode)
        {
            case 300:
                actionPerformed((GuiListButton) list.getComponent(300));
                break;

            case 304:
                restoreDefaults();
        }

        confirmMode = 0;
        confirmDismissed = true;
        setConfirmationControls(false);
    }

    private void setConfirmationControls(boolean p_175329_1_)
    {
        confirm.visible = p_175329_1_;
        cancel.visible = p_175329_1_;
        randomize.enabled = !p_175329_1_;
        done.enabled = !p_175329_1_;
        previousPage.enabled = !p_175329_1_;
        nextPage.enabled = !p_175329_1_;
        defaults.enabled = settingsModified && !p_175329_1_;
        presets.enabled = !p_175329_1_;
        list.setActive(!p_175329_1_);
    }

    private void updatePageControls()
    {
        previousPage.enabled = list.getPage() != 0;
        nextPage.enabled = list.getPage() != list.getPageCount() - 1;
        subtitle = I18n.format("book.pageIndicator", list.getPage() + 1, list.getPageCount());
        pageTitle = pageNames[list.getPage()];
        randomize.enabled = list.getPage() != list.getPageCount() - 1;
    }

    /**
     * Fired when a key is typed (except F11 which toggles full screen). This is the equivalent of
     * KeyListener.keyTyped(KeyEvent e). Args : character (character on the key), keyCode (lwjgl Keyboard key code)
     */
    public void keyPressed(int key, char c)
    {
        super.keyPressed(key, c);

        if (confirmMode == 0)
        {
            switch (key)
            {
                case 200:
                    modifyFocusValue(1.0F);
                    break;

                case 208:
                    modifyFocusValue(-1.0F);
                    break;

                default:
                    list.onKeyPressed(c, key);
            }
        }
    }

    private void modifyFocusValue(float p_175327_1_)
    {
        Gui gui = list.getFocusedControl();

        if (gui instanceof GuiTextField)
        {
            float f = p_175327_1_;

            if (Screen.hasShiftDown())
            {
                f = p_175327_1_ * 0.1F;

                if (Screen.hasControlDown())
                {
                    f *= 0.1F;
                }
            }
            else if (Screen.hasControlDown())
            {
                f = p_175327_1_ * 10.0F;

                if (Screen.hasAltDown())
                {
                    f *= 10.0F;
                }
            }

            GuiTextField guitextfield = (GuiTextField)gui;
            Float f1 = Floats.tryParse(guitextfield.getText());

            if (f1 != null)
            {
                f1 = f1.floatValue() + f;
                int i = guitextfield.getId();
                String s = getFormattedValue(guitextfield.getId(), f1.floatValue());
                guitextfield.setText(s);
                setEntryValue(i, s);
            }
        }
    }

    /**
     * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
     */
    public void mousePressed(int mouseX, int mouseY, int button)
    {
        super.mousePressed(mouseX, mouseY, button);

        if (confirmMode == 0 && !confirmDismissed)
        {
            list.mouseClicked(mouseX, mouseY, button);
        }
    }

    /**
     * Called when a mouse button is released.
     */
    public void mouseReleased(int mouseX, int mouseY, int button)
    {
        super.mouseReleased(mouseX, mouseY, button);

        if (confirmDismissed)
        {
            confirmDismissed = false;
        }
        else if (confirmMode == 0)
        {
            list.mouseReleased(mouseX, mouseY, button);
        }
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void draw(int mouseX, int mouseY, float partialTick)
    {
        drawDefaultBackground();
        list.drawScreen(mouseX, mouseY, partialTick);
        drawCenteredString(font, title, width / 2, 2, 16777215);
        drawCenteredString(font, subtitle, width / 2, 12, 16777215);
        drawCenteredString(font, pageTitle, width / 2, 22, 16777215);
        super.draw(mouseX, mouseY, partialTick);

        if (confirmMode != 0)
        {
            Gui.drawRect(0, 0, width, height, Integer.MIN_VALUE);
            drawHorizontalLine(width / 2 - 91, width / 2 + 90, 99, -2039584);
            drawHorizontalLine(width / 2 - 91, width / 2 + 90, 185, -6250336);
            drawVerticalLine(width / 2 - 91, 99, 185, -2039584);
            drawVerticalLine(width / 2 + 90, 99, 185, -6250336);
            float f = 85.0F;
            float f1 = 180.0F;
            GlStateManager.disableLighting();
            GlStateManager.disableFog();
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuffer();
            Minecraft.getTextureManager().bindTexture(Gui.OPTIONS_BACKGROUND);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            float f2 = 32.0F;
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            bufferbuilder.pos(width / 2 - 90, 185.0D, 0.0D).tex(0.0D, 2.65625D).color(64, 64, 64, 64).endVertex();
            bufferbuilder.pos(width / 2 + 90, 185.0D, 0.0D).tex(5.625D, 2.65625D).color(64, 64, 64, 64).endVertex();
            bufferbuilder.pos(width / 2 + 90, 100.0D, 0.0D).tex(5.625D, 0.0D).color(64, 64, 64, 64).endVertex();
            bufferbuilder.pos(width / 2 - 90, 100.0D, 0.0D).tex(0.0D, 0.0D).color(64, 64, 64, 64).endVertex();
            tessellator.draw();
            drawCenteredString(font, I18n.format("createWorld.customize.custom.confirmTitle"), width / 2, 105, 16777215);
            drawCenteredString(font, I18n.format("createWorld.customize.custom.confirm1"), width / 2, 125, 16777215);
            drawCenteredString(font, I18n.format("createWorld.customize.custom.confirm2"), width / 2, 135, 16777215);
            confirm.draw(minecraft, mouseX, mouseY, partialTick);
            cancel.draw(minecraft, mouseX, mouseY, partialTick);
        }
    }
}
