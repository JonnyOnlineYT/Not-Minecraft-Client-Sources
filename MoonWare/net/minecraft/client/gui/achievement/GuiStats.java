package net.minecraft.client.gui.achievement;

import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.gui.IProgressMeter;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityList;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketClientStatus;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatCrafting;
import net.minecraft.stats.StatList;
import net.minecraft.stats.StatisticsManager;
import org.lwjgl.input.Mouse;

public class GuiStats extends Screen implements IProgressMeter
{
    protected Screen parentScreen;
    protected String screenTitle = "Select world";
    private GuiStats.StatsGeneral generalStats;
    private GuiStats.StatsItem itemStats;
    private GuiStats.StatsBlock blockStats;
    private GuiStats.StatsMobsList mobStats;
    private final StatisticsManager stats;
    private GuiSlot displaySlot;

    /** When true, the game will be paused when the gui is shown */
    private boolean doesGuiPauseGame = true;

    public GuiStats(Screen p_i1071_1_, StatisticsManager p_i1071_2_)
    {
        parentScreen = p_i1071_1_;
        stats = p_i1071_2_;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    public void init()
    {
        screenTitle = I18n.format("gui.stats");
        doesGuiPauseGame = true;
        minecraft.getConnection().sendPacket(new CPacketClientStatus(CPacketClientStatus.State.REQUEST_STATS));
    }

    /**
     * Handles mouse input.
     */
    public void handleMouseInput()
    {
        super.handleMouseInput();

        if (displaySlot != null)
        {
            displaySlot.handleMouseInput();
        }
    }

    public void func_193028_a()
    {
        generalStats = new GuiStats.StatsGeneral(minecraft);
        generalStats.registerScrollButtons(1, 1);
        itemStats = new GuiStats.StatsItem(minecraft);
        itemStats.registerScrollButtons(1, 1);
        blockStats = new GuiStats.StatsBlock(minecraft);
        blockStats.registerScrollButtons(1, 1);
        mobStats = new GuiStats.StatsMobsList(minecraft);
        mobStats.registerScrollButtons(1, 1);
    }

    public void func_193029_f()
    {
        widgets.add(new ButtonWidget(0, width / 2 + 4, height - 28, 150, 20, I18n.format("gui.done")));
        widgets.add(new ButtonWidget(1, width / 2 - 160, height - 52, 80, 20, I18n.format("stat.generalButton")));
        ButtonWidget guibutton = addButton(new ButtonWidget(2, width / 2 - 80, height - 52, 80, 20, I18n.format("stat.blocksButton")));
        ButtonWidget guibutton1 = addButton(new ButtonWidget(3, width / 2, height - 52, 80, 20, I18n.format("stat.itemsButton")));
        ButtonWidget guibutton2 = addButton(new ButtonWidget(4, width / 2 + 80, height - 52, 80, 20, I18n.format("stat.mobsButton")));

        if (blockStats.getSize() == 0)
        {
            guibutton.enabled = false;
        }

        if (itemStats.getSize() == 0)
        {
            guibutton1.enabled = false;
        }

        if (mobStats.getSize() == 0)
        {
            guibutton2.enabled = false;
        }
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    public void actionPerformed(ButtonWidget button)
    {
        if (button.enabled)
        {
            if (button.id == 0)
            {
                Minecraft.openScreen(parentScreen);
            }
            else if (button.id == 1)
            {
                displaySlot = generalStats;
            }
            else if (button.id == 3)
            {
                displaySlot = itemStats;
            }
            else if (button.id == 2)
            {
                displaySlot = blockStats;
            }
            else if (button.id == 4)
            {
                displaySlot = mobStats;
            }
            else
            {
                displaySlot.actionPerformed(button);
            }
        }
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void draw(int mouseX, int mouseY, float partialTick)
    {
        if (doesGuiPauseGame)
        {
            drawDefaultBackground();
            drawCenteredString(font, I18n.format("multiplayer.downloadingStats"), width / 2, height / 2, 16777215);
            drawCenteredString(font, IProgressMeter.LOADING_STRINGS[(int)(Minecraft.getSystemTime() / 150L % (long) IProgressMeter.LOADING_STRINGS.length)], width / 2, height / 2 + font.height * 2, 16777215);
        }
        else
        {
            displaySlot.drawScreen(mouseX, mouseY, partialTick);
            drawCenteredString(font, screenTitle, width / 2, 20, 16777215);
            super.draw(mouseX, mouseY, partialTick);
        }
    }

    public void func_193026_g()
    {
        if (doesGuiPauseGame)
        {
            func_193028_a();
            func_193029_f();
            displaySlot = generalStats;
            doesGuiPauseGame = false;
        }
    }

    /**
     * Returns true if this GUI should pause the game when it is displayed in single-player
     */
    public boolean pauses()
    {
        return !doesGuiPauseGame;
    }

    private void drawStatsScreen(int p_146521_1_, int p_146521_2_, Item p_146521_3_)
    {
        drawButtonBackground(p_146521_1_ + 1, p_146521_2_ + 1);
        GlStateManager.enableRescaleNormal();
        RenderHelper.enableGUIStandardItemLighting();
        renderItem.renderItemIntoGUI(p_146521_3_.func_190903_i(), p_146521_1_ + 2, p_146521_2_ + 2);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
    }

    /**
     * Draws a gray box that serves as a button background.
     */
    private void drawButtonBackground(int p_146531_1_, int p_146531_2_)
    {
        drawSprite(p_146531_1_, p_146531_2_, 0, 0);
    }

    /**
     * Draws a sprite from assets/textures/gui/container/stats_icons.png
     */
    private void drawSprite(int p_146527_1_, int p_146527_2_, int p_146527_3_, int p_146527_4_)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getTextureManager().bindTexture(Gui.STAT_ICONS);
        float f = 0.0078125F;
        float f1 = 0.0078125F;
        int i = 18;
        int j = 18;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(p_146527_1_ + 0, p_146527_2_ + 18, zLevel).tex((float)(p_146527_3_ + 0) * 0.0078125F, (float)(p_146527_4_ + 18) * 0.0078125F).endVertex();
        bufferbuilder.pos(p_146527_1_ + 18, p_146527_2_ + 18, zLevel).tex((float)(p_146527_3_ + 18) * 0.0078125F, (float)(p_146527_4_ + 18) * 0.0078125F).endVertex();
        bufferbuilder.pos(p_146527_1_ + 18, p_146527_2_ + 0, zLevel).tex((float)(p_146527_3_ + 18) * 0.0078125F, (float)(p_146527_4_ + 0) * 0.0078125F).endVertex();
        bufferbuilder.pos(p_146527_1_ + 0, p_146527_2_ + 0, zLevel).tex((float)(p_146527_3_ + 0) * 0.0078125F, (float)(p_146527_4_ + 0) * 0.0078125F).endVertex();
        tessellator.draw();
    }

    abstract class Stats extends GuiSlot
    {
        protected int headerPressed = -1;
        protected List<StatCrafting> statsHolder;
        protected Comparator<StatCrafting> statSorter;
        protected int sortColumn = -1;
        protected int sortOrder;

        protected Stats(Minecraft p_i47550_2_)
        {
            super(p_i47550_2_, GuiStats.this.width, GuiStats.this.height, 32, GuiStats.this.height - 64, 20);
            setShowSelectionBox(false);
            setHasListHeader(true, 20);
        }

        protected void elementClicked(int i, boolean doubleClick, int mouseX, int mouseY)
        {
        }

        protected boolean isSelected(int slotIndex)
        {
            return false;
        }

        public int getListWidth()
        {
            return 375;
        }

        protected int getScrollBarX()
        {
            return width / 2 + 140;
        }

        protected void drawBackground()
        {
            drawDefaultBackground();
        }

        protected void drawListHeader(int insideLeft, int insideTop, Tessellator tessellatorIn)
        {
            if (!Mouse.isButtonDown(0))
            {
                headerPressed = -1;
            }

            if (headerPressed == 0)
            {
                drawSprite(insideLeft + 115 - 18, insideTop + 1, 0, 0);
            }
            else
            {
                drawSprite(insideLeft + 115 - 18, insideTop + 1, 0, 18);
            }

            if (headerPressed == 1)
            {
                drawSprite(insideLeft + 165 - 18, insideTop + 1, 0, 0);
            }
            else
            {
                drawSprite(insideLeft + 165 - 18, insideTop + 1, 0, 18);
            }

            if (headerPressed == 2)
            {
                drawSprite(insideLeft + 215 - 18, insideTop + 1, 0, 0);
            }
            else
            {
                drawSprite(insideLeft + 215 - 18, insideTop + 1, 0, 18);
            }

            if (headerPressed == 3)
            {
                drawSprite(insideLeft + 265 - 18, insideTop + 1, 0, 0);
            }
            else
            {
                drawSprite(insideLeft + 265 - 18, insideTop + 1, 0, 18);
            }

            if (headerPressed == 4)
            {
                drawSprite(insideLeft + 315 - 18, insideTop + 1, 0, 0);
            }
            else
            {
                drawSprite(insideLeft + 315 - 18, insideTop + 1, 0, 18);
            }

            if (sortColumn != -1)
            {
                int i = 79;
                int j = 18;

                if (sortColumn == 1)
                {
                    i = 129;
                }
                else if (sortColumn == 2)
                {
                    i = 179;
                }
                else if (sortColumn == 3)
                {
                    i = 229;
                }
                else if (sortColumn == 4)
                {
                    i = 279;
                }

                if (sortOrder == 1)
                {
                    j = 36;
                }

                drawSprite(insideLeft + i, insideTop + 1, j, 0);
            }
        }

        protected void clickedHeader(int p_148132_1_, int p_148132_2_)
        {
            headerPressed = -1;

            if (p_148132_1_ >= 79 && p_148132_1_ < 115)
            {
                headerPressed = 0;
            }
            else if (p_148132_1_ >= 129 && p_148132_1_ < 165)
            {
                headerPressed = 1;
            }
            else if (p_148132_1_ >= 179 && p_148132_1_ < 215)
            {
                headerPressed = 2;
            }
            else if (p_148132_1_ >= 229 && p_148132_1_ < 265)
            {
                headerPressed = 3;
            }
            else if (p_148132_1_ >= 279 && p_148132_1_ < 315)
            {
                headerPressed = 4;
            }

            if (headerPressed >= 0)
            {
                sortByColumn(headerPressed);
                Minecraft.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            }
        }

        protected final int getSize()
        {
            return statsHolder.size();
        }

        protected final StatCrafting getSlotStat(int p_148211_1_)
        {
            return statsHolder.get(p_148211_1_);
        }

        protected abstract String getHeaderDescriptionId(int p_148210_1_);

        protected void renderStat(StatBase p_148209_1_, int p_148209_2_, int p_148209_3_, boolean p_148209_4_)
        {
            if (p_148209_1_ != null)
            {
                String s = p_148209_1_.format(stats.readStat(p_148209_1_));
                drawString(font, s, p_148209_2_ - font.getStringWidth(s), p_148209_3_ + 5, p_148209_4_ ? 16777215 : 9474192);
            }
            else
            {
                String s1 = "-";
                drawString(font, "-", p_148209_2_ - font.getStringWidth("-"), p_148209_3_ + 5, p_148209_4_ ? 16777215 : 9474192);
            }
        }

        protected void renderDecorations(int mouseXIn, int mouseYIn)
        {
            if (mouseYIn >= top && mouseYIn <= bottom)
            {
                int i = getSlotIndexFromScreenCoords(mouseXIn, mouseYIn);
                int j = (width - getListWidth()) / 2;

                if (i >= 0)
                {
                    if (mouseXIn < j + 40 || mouseXIn > j + 40 + 20)
                    {
                        return;
                    }

                    StatCrafting statcrafting = getSlotStat(i);
                    renderMouseHoverToolTip(statcrafting, mouseXIn, mouseYIn);
                }
                else
                {
                    String s;

                    if (mouseXIn >= j + 115 - 18 && mouseXIn <= j + 115)
                    {
                        s = getHeaderDescriptionId(0);
                    }
                    else if (mouseXIn >= j + 165 - 18 && mouseXIn <= j + 165)
                    {
                        s = getHeaderDescriptionId(1);
                    }
                    else if (mouseXIn >= j + 215 - 18 && mouseXIn <= j + 215)
                    {
                        s = getHeaderDescriptionId(2);
                    }
                    else if (mouseXIn >= j + 265 - 18 && mouseXIn <= j + 265)
                    {
                        s = getHeaderDescriptionId(3);
                    }
                    else
                    {
                        if (mouseXIn < j + 315 - 18 || mouseXIn > j + 315)
                        {
                            return;
                        }

                        s = getHeaderDescriptionId(4);
                    }

                    s = ("" + I18n.format(s)).trim();

                    if (!s.isEmpty())
                    {
                        int k = mouseXIn + 12;
                        int l = mouseYIn - 12;
                        int i1 = font.getStringWidth(s);
                        drawGradientRect(k - 3, l - 3, k + i1 + 3, l + 8 + 3, -1073741824, -1073741824);
                        font.drawStringWithShadow(s, (float)k, (float)l, -1);
                    }
                }
            }
        }

        protected void renderMouseHoverToolTip(StatCrafting p_148213_1_, int p_148213_2_, int p_148213_3_)
        {
            if (p_148213_1_ != null)
            {
                Item item = p_148213_1_.getItem();
                ItemStack itemstack = new ItemStack(item);
                String s = itemstack.getUnlocalizedName();
                String s1 = ("" + I18n.format(s + ".name")).trim();

                if (!s1.isEmpty())
                {
                    int i = p_148213_2_ + 12;
                    int j = p_148213_3_ - 12;
                    int k = font.getStringWidth(s1);
                    drawGradientRect(i - 3, j - 3, i + k + 3, j + 8 + 3, -1073741824, -1073741824);
                    font.drawStringWithShadow(s1, (float)i, (float)j, -1);
                }
            }
        }

        protected void sortByColumn(int p_148212_1_)
        {
            if (p_148212_1_ != sortColumn)
            {
                sortColumn = p_148212_1_;
                sortOrder = -1;
            }
            else if (sortOrder == -1)
            {
                sortOrder = 1;
            }
            else
            {
                sortColumn = -1;
                sortOrder = 0;
            }

            Collections.sort(statsHolder, statSorter);
        }
    }

    class StatsBlock extends GuiStats.Stats
    {
        public StatsBlock(Minecraft p_i47554_2_)
        {
            super(p_i47554_2_);
            statsHolder = Lists.newArrayList();

            for (StatCrafting statcrafting : StatList.MINE_BLOCK_STATS)
            {
                boolean flag = false;
                Item item = statcrafting.getItem();

                if (stats.readStat(statcrafting) > 0)
                {
                    flag = true;
                }
                else if (StatList.getObjectUseStats(item) != null && stats.readStat(StatList.getObjectUseStats(item)) > 0)
                {
                    flag = true;
                }
                else if (StatList.getCraftStats(item) != null && stats.readStat(StatList.getCraftStats(item)) > 0)
                {
                    flag = true;
                }
                else if (StatList.getObjectsPickedUpStats(item) != null && stats.readStat(StatList.getObjectsPickedUpStats(item)) > 0)
                {
                    flag = true;
                }
                else if (StatList.getDroppedObjectStats(item) != null && stats.readStat(StatList.getDroppedObjectStats(item)) > 0)
                {
                    flag = true;
                }

                if (flag)
                {
                    statsHolder.add(statcrafting);
                }
            }

            statSorter = new Comparator<StatCrafting>()
            {
                public int compare(StatCrafting p_compare_1_, StatCrafting p_compare_2_)
                {
                    Item item1 = p_compare_1_.getItem();
                    Item item2 = p_compare_2_.getItem();
                    StatBase statbase = null;
                    StatBase statbase1 = null;

                    if (sortColumn == 2)
                    {
                        statbase = StatList.getBlockStats(Block.getBlockFromItem(item1));
                        statbase1 = StatList.getBlockStats(Block.getBlockFromItem(item2));
                    }
                    else if (sortColumn == 0)
                    {
                        statbase = StatList.getCraftStats(item1);
                        statbase1 = StatList.getCraftStats(item2);
                    }
                    else if (sortColumn == 1)
                    {
                        statbase = StatList.getObjectUseStats(item1);
                        statbase1 = StatList.getObjectUseStats(item2);
                    }
                    else if (sortColumn == 3)
                    {
                        statbase = StatList.getObjectsPickedUpStats(item1);
                        statbase1 = StatList.getObjectsPickedUpStats(item2);
                    }
                    else if (sortColumn == 4)
                    {
                        statbase = StatList.getDroppedObjectStats(item1);
                        statbase1 = StatList.getDroppedObjectStats(item2);
                    }

                    if (statbase != null || statbase1 != null)
                    {
                        if (statbase == null)
                        {
                            return 1;
                        }

                        if (statbase1 == null)
                        {
                            return -1;
                        }

                        int i = stats.readStat(statbase);
                        int j = stats.readStat(statbase1);

                        if (i != j)
                        {
                            return (i - j) * sortOrder;
                        }
                    }

                    return Item.getIdFromItem(item1) - Item.getIdFromItem(item2);
                }
            };
        }

        protected void drawListHeader(int insideLeft, int insideTop, Tessellator tessellatorIn)
        {
            super.drawListHeader(insideLeft, insideTop, tessellatorIn);

            if (headerPressed == 0)
            {
                drawSprite(insideLeft + 115 - 18 + 1, insideTop + 1 + 1, 18, 18);
            }
            else
            {
                drawSprite(insideLeft + 115 - 18, insideTop + 1, 18, 18);
            }

            if (headerPressed == 1)
            {
                drawSprite(insideLeft + 165 - 18 + 1, insideTop + 1 + 1, 36, 18);
            }
            else
            {
                drawSprite(insideLeft + 165 - 18, insideTop + 1, 36, 18);
            }

            if (headerPressed == 2)
            {
                drawSprite(insideLeft + 215 - 18 + 1, insideTop + 1 + 1, 54, 18);
            }
            else
            {
                drawSprite(insideLeft + 215 - 18, insideTop + 1, 54, 18);
            }

            if (headerPressed == 3)
            {
                drawSprite(insideLeft + 265 - 18 + 1, insideTop + 1 + 1, 90, 18);
            }
            else
            {
                drawSprite(insideLeft + 265 - 18, insideTop + 1, 90, 18);
            }

            if (headerPressed == 4)
            {
                drawSprite(insideLeft + 315 - 18 + 1, insideTop + 1 + 1, 108, 18);
            }
            else
            {
                drawSprite(insideLeft + 315 - 18, insideTop + 1, 108, 18);
            }
        }

        protected void func_192637_a(int p_192637_1_, int p_192637_2_, int p_192637_3_, int p_192637_4_, int p_192637_5_, int p_192637_6_, float p_192637_7_)
        {
            StatCrafting statcrafting = getSlotStat(p_192637_1_);
            Item item = statcrafting.getItem();
            drawStatsScreen(p_192637_2_ + 40, p_192637_3_, item);
            renderStat(StatList.getCraftStats(item), p_192637_2_ + 115, p_192637_3_, p_192637_1_ % 2 == 0);
            renderStat(StatList.getObjectUseStats(item), p_192637_2_ + 165, p_192637_3_, p_192637_1_ % 2 == 0);
            renderStat(statcrafting, p_192637_2_ + 215, p_192637_3_, p_192637_1_ % 2 == 0);
            renderStat(StatList.getObjectsPickedUpStats(item), p_192637_2_ + 265, p_192637_3_, p_192637_1_ % 2 == 0);
            renderStat(StatList.getDroppedObjectStats(item), p_192637_2_ + 315, p_192637_3_, p_192637_1_ % 2 == 0);
        }

        protected String getHeaderDescriptionId(int p_148210_1_)
        {
            if (p_148210_1_ == 0)
            {
                return "stat.crafted";
            }
            else if (p_148210_1_ == 1)
            {
                return "stat.used";
            }
            else if (p_148210_1_ == 3)
            {
                return "stat.pickup";
            }
            else
            {
                return p_148210_1_ == 4 ? "stat.dropped" : "stat.mined";
            }
        }
    }

    class StatsGeneral extends GuiSlot
    {
        public StatsGeneral(Minecraft p_i47553_2_)
        {
            super(p_i47553_2_, GuiStats.this.width, GuiStats.this.height, 32, GuiStats.this.height - 64, 10);
            setShowSelectionBox(false);
        }

        protected int getSize()
        {
            return StatList.BASIC_STATS.size();
        }

        protected void elementClicked(int i, boolean doubleClick, int mouseX, int mouseY)
        {
        }

        protected boolean isSelected(int slotIndex)
        {
            return false;
        }

        protected int getContentHeight()
        {
            return getSize() * 10;
        }

        protected void drawBackground()
        {
            drawDefaultBackground();
        }

        protected void func_192637_a(int p_192637_1_, int p_192637_2_, int p_192637_3_, int p_192637_4_, int p_192637_5_, int p_192637_6_, float p_192637_7_)
        {
            StatBase statbase = StatList.BASIC_STATS.get(p_192637_1_);
            drawString(font, statbase.getStatName().asString(), p_192637_2_ + 2, p_192637_3_ + 1, p_192637_1_ % 2 == 0 ? 16777215 : 9474192);
            String s = statbase.format(stats.readStat(statbase));
            drawString(font, s, p_192637_2_ + 2 + 213 - font.getStringWidth(s), p_192637_3_ + 1, p_192637_1_ % 2 == 0 ? 16777215 : 9474192);
        }
    }

    class StatsItem extends GuiStats.Stats
    {
        public StatsItem(Minecraft p_i47552_2_)
        {
            super(p_i47552_2_);
            statsHolder = Lists.newArrayList();

            for (StatCrafting statcrafting : StatList.USE_ITEM_STATS)
            {
                boolean flag = false;
                Item item = statcrafting.getItem();

                if (stats.readStat(statcrafting) > 0)
                {
                    flag = true;
                }
                else if (StatList.getObjectBreakStats(item) != null && stats.readStat(StatList.getObjectBreakStats(item)) > 0)
                {
                    flag = true;
                }
                else if (StatList.getCraftStats(item) != null && stats.readStat(StatList.getCraftStats(item)) > 0)
                {
                    flag = true;
                }
                else if (StatList.getObjectsPickedUpStats(item) != null && stats.readStat(StatList.getObjectsPickedUpStats(item)) > 0)
                {
                    flag = true;
                }
                else if (StatList.getDroppedObjectStats(item) != null && stats.readStat(StatList.getDroppedObjectStats(item)) > 0)
                {
                    flag = true;
                }

                if (flag)
                {
                    statsHolder.add(statcrafting);
                }
            }

            statSorter = new Comparator<StatCrafting>()
            {
                public int compare(StatCrafting p_compare_1_, StatCrafting p_compare_2_)
                {
                    Item item1 = p_compare_1_.getItem();
                    Item item2 = p_compare_2_.getItem();
                    int i = Item.getIdFromItem(item1);
                    int j = Item.getIdFromItem(item2);
                    StatBase statbase = null;
                    StatBase statbase1 = null;

                    if (sortColumn == 0)
                    {
                        statbase = StatList.getObjectBreakStats(item1);
                        statbase1 = StatList.getObjectBreakStats(item2);
                    }
                    else if (sortColumn == 1)
                    {
                        statbase = StatList.getCraftStats(item1);
                        statbase1 = StatList.getCraftStats(item2);
                    }
                    else if (sortColumn == 2)
                    {
                        statbase = StatList.getObjectUseStats(item1);
                        statbase1 = StatList.getObjectUseStats(item2);
                    }
                    else if (sortColumn == 3)
                    {
                        statbase = StatList.getObjectsPickedUpStats(item1);
                        statbase1 = StatList.getObjectsPickedUpStats(item2);
                    }
                    else if (sortColumn == 4)
                    {
                        statbase = StatList.getDroppedObjectStats(item1);
                        statbase1 = StatList.getDroppedObjectStats(item2);
                    }

                    if (statbase != null || statbase1 != null)
                    {
                        if (statbase == null)
                        {
                            return 1;
                        }

                        if (statbase1 == null)
                        {
                            return -1;
                        }

                        int k = stats.readStat(statbase);
                        int l = stats.readStat(statbase1);

                        if (k != l)
                        {
                            return (k - l) * sortOrder;
                        }
                    }

                    return i - j;
                }
            };
        }

        protected void drawListHeader(int insideLeft, int insideTop, Tessellator tessellatorIn)
        {
            super.drawListHeader(insideLeft, insideTop, tessellatorIn);

            if (headerPressed == 0)
            {
                drawSprite(insideLeft + 115 - 18 + 1, insideTop + 1 + 1, 72, 18);
            }
            else
            {
                drawSprite(insideLeft + 115 - 18, insideTop + 1, 72, 18);
            }

            if (headerPressed == 1)
            {
                drawSprite(insideLeft + 165 - 18 + 1, insideTop + 1 + 1, 18, 18);
            }
            else
            {
                drawSprite(insideLeft + 165 - 18, insideTop + 1, 18, 18);
            }

            if (headerPressed == 2)
            {
                drawSprite(insideLeft + 215 - 18 + 1, insideTop + 1 + 1, 36, 18);
            }
            else
            {
                drawSprite(insideLeft + 215 - 18, insideTop + 1, 36, 18);
            }

            if (headerPressed == 3)
            {
                drawSprite(insideLeft + 265 - 18 + 1, insideTop + 1 + 1, 90, 18);
            }
            else
            {
                drawSprite(insideLeft + 265 - 18, insideTop + 1, 90, 18);
            }

            if (headerPressed == 4)
            {
                drawSprite(insideLeft + 315 - 18 + 1, insideTop + 1 + 1, 108, 18);
            }
            else
            {
                drawSprite(insideLeft + 315 - 18, insideTop + 1, 108, 18);
            }
        }

        protected void func_192637_a(int p_192637_1_, int p_192637_2_, int p_192637_3_, int p_192637_4_, int p_192637_5_, int p_192637_6_, float p_192637_7_)
        {
            StatCrafting statcrafting = getSlotStat(p_192637_1_);
            Item item = statcrafting.getItem();
            drawStatsScreen(p_192637_2_ + 40, p_192637_3_, item);
            renderStat(StatList.getObjectBreakStats(item), p_192637_2_ + 115, p_192637_3_, p_192637_1_ % 2 == 0);
            renderStat(StatList.getCraftStats(item), p_192637_2_ + 165, p_192637_3_, p_192637_1_ % 2 == 0);
            renderStat(statcrafting, p_192637_2_ + 215, p_192637_3_, p_192637_1_ % 2 == 0);
            renderStat(StatList.getObjectsPickedUpStats(item), p_192637_2_ + 265, p_192637_3_, p_192637_1_ % 2 == 0);
            renderStat(StatList.getDroppedObjectStats(item), p_192637_2_ + 315, p_192637_3_, p_192637_1_ % 2 == 0);
        }

        protected String getHeaderDescriptionId(int p_148210_1_)
        {
            if (p_148210_1_ == 1)
            {
                return "stat.crafted";
            }
            else if (p_148210_1_ == 2)
            {
                return "stat.used";
            }
            else if (p_148210_1_ == 3)
            {
                return "stat.pickup";
            }
            else
            {
                return p_148210_1_ == 4 ? "stat.dropped" : "stat.depleted";
            }
        }
    }

    class StatsMobsList extends GuiSlot
    {
        private final List<EntityList.EntityEggInfo> mobs = Lists.newArrayList();

        public StatsMobsList(Minecraft p_i47551_2_)
        {
            super(p_i47551_2_, GuiStats.this.width, GuiStats.this.height, 32, GuiStats.this.height - 64, font.height * 4);
            setShowSelectionBox(false);

            for (EntityList.EntityEggInfo entitylist$entityegginfo : EntityList.ENTITY_EGGS.values())
            {
                if (stats.readStat(entitylist$entityegginfo.killEntityStat) > 0 || stats.readStat(entitylist$entityegginfo.entityKilledByStat) > 0)
                {
                    mobs.add(entitylist$entityegginfo);
                }
            }
        }

        protected int getSize()
        {
            return mobs.size();
        }

        protected void elementClicked(int i, boolean doubleClick, int mouseX, int mouseY)
        {
        }

        protected boolean isSelected(int slotIndex)
        {
            return false;
        }

        protected int getContentHeight()
        {
            return getSize() * font.height * 4;
        }

        protected void drawBackground()
        {
            drawDefaultBackground();
        }

        protected void func_192637_a(int p_192637_1_, int p_192637_2_, int p_192637_3_, int p_192637_4_, int p_192637_5_, int p_192637_6_, float p_192637_7_)
        {
            EntityList.EntityEggInfo entitylist$entityegginfo = mobs.get(p_192637_1_);
            String s = I18n.format("entity." + EntityList.func_191302_a(entitylist$entityegginfo.spawnedID) + ".name");
            int i = stats.readStat(entitylist$entityegginfo.killEntityStat);
            int j = stats.readStat(entitylist$entityegginfo.entityKilledByStat);
            String s1 = I18n.format("stat.entityKills", i, s);
            String s2 = I18n.format("stat.entityKilledBy", s, j);

            if (i == 0)
            {
                s1 = I18n.format("stat.entityKills.none", s);
            }

            if (j == 0)
            {
                s2 = I18n.format("stat.entityKilledBy.none", s);
            }

            drawString(font, s, p_192637_2_ + 2 - 10, p_192637_3_ + 1, 16777215);
            drawString(font, s1, p_192637_2_ + 2, p_192637_3_ + 1 + font.height, i == 0 ? 6316128 : 9474192);
            drawString(font, s2, p_192637_2_ + 2, p_192637_3_ + 1 + font.height * 2, j == 0 ? 6316128 : 9474192);
        }
    }
}
