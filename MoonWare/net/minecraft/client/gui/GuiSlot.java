package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.Namespaced;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.moonware.client.feature.impl.visual.util.RenderUtils2;
import org.moonware.client.helpers.render.RenderHelper;
import org.moonware.client.helpers.render.rect.DrawHelper;
import org.moonware.client.helpers.render.rect.RectHelper;

import java.awt.*;

public abstract class GuiSlot {
    protected final Minecraft mc;
    protected int width;
    protected int height;
    protected int top;
    protected int bottom;
    protected int right;
    protected int left;
    protected final int entryHeight;
    private int scrollUpButtonID;
    private int scrollDownButtonID;
    protected int mouseX;
    protected int mouseY;
    protected boolean centerListVertically = true;
    protected int initialClickY = -2;
    protected float scrollMultiplier;
    protected float amountScrolled;
    protected int selectedElement = -1;
    protected long lastClicked;
    protected boolean visible = true;
    protected boolean showSelectionBox = true;
    protected boolean hasListHeader;
    protected int headerPadding;
    private boolean enabled = true;

    public GuiSlot(Minecraft mcIn, int width, int height, int topIn, int bottomIn, int slotHeightIn) {
        mc = mcIn;
        this.width = width;
        this.height = height;
        top = topIn;
        bottom = bottomIn;
        entryHeight = slotHeightIn;
        left = 0;
        right = width;
    }

    public void setDimensions(int widthIn, int heightIn, int topIn, int bottomIn) {
        width = widthIn;
        height = heightIn;
        top = topIn;
        bottom = bottomIn;
        left = 0;
        right = widthIn;
    }

    public void setShowSelectionBox(boolean showSelectionBox) {
        this.showSelectionBox = showSelectionBox;
    }

    /**
     * Sets hasListHeader and headerHeight. Params: hasListHeader, headerHeight. If hasListHeader is false headerHeight
     * is set to 0.
     */
    protected void setHasListHeader(boolean hasListHeaderIn, int headerPaddingIn) {
        hasListHeader = hasListHeaderIn;
        headerPadding = headerPaddingIn;

        if (!hasListHeaderIn) {
            headerPadding = 0;
        }
    }

    protected abstract int getSize();

    /**
     * The element in the slot that was clicked, boolean for whether it was double clicked or not
     */
    protected abstract void elementClicked(int i, boolean doubleClick, int mouseX, int mouseY);

    /**
     * Returns true if the element passed in is currently selected
     */
    protected abstract boolean isSelected(int slotIndex);

    /**
     * Return the height of the content being scrolled
     */
    protected int getContentHeight() {
        return getSize() * entryHeight + headerPadding;
    }

    protected abstract void drawBackground();

    protected void func_192639_a(int p_192639_1_, int p_192639_2_, int p_192639_3_, float p_192639_4_) {
    }

    protected abstract void func_192637_a(int p_192637_1_, int p_192637_2_, int p_192637_3_, int p_192637_4_, int p_192637_5_, int p_192637_6_, float p_192637_7_);

    /**
     * Handles drawing a list's header row.
     */
    protected void drawListHeader(int insideLeft, int insideTop, Tessellator tessellatorIn) {
    }

    protected void clickedHeader(int p_148132_1_, int p_148132_2_) {
    }

    protected void renderDecorations(int mouseXIn, int mouseYIn) {
    }

    public int getSlotIndexFromScreenCoords(int posX, int posY) {
        int i = left + width / 2 - getListWidth() / 2;
        int j = left + width / 2 + getListWidth() / 2;
        int k = posY - top - headerPadding + (int) amountScrolled - 4;
        int l = k / entryHeight;
        return posX < getScrollBarX() && posX >= i && posX <= j && l >= 0 && k >= 0 && l < getSize() ? l : -1;
    }

    /**
     * Registers the IDs that can be used for the scrollbar's up/down buttons.
     */
    public void registerScrollButtons(int scrollUpButtonIDIn, int scrollDownButtonIDIn) {
        scrollUpButtonID = scrollUpButtonIDIn;
        scrollDownButtonID = scrollDownButtonIDIn;
    }

    /**
     * Stop the thing from scrolling out of bounds
     */
    protected void bindAmountScrolled() {
        amountScrolled = MathHelper.clamp(amountScrolled, 0.0F, (float) getMaxScroll());
    }

    public int getMaxScroll() {
        return Math.max(0, getContentHeight() - (bottom - top - 4));
    }

    /**
     * Returns the amountScrolled field as an integer.
     */
    public int getAmountScrolled() {
        return (int) amountScrolled;
    }

    public boolean isMouseYWithinSlotBounds(int p_148141_1_) {
        return p_148141_1_ >= top && p_148141_1_ <= bottom && mouseX >= left && mouseX <= right;
    }

    /**
     * Scrolls the slot by the given amount. A positive value scrolls down, and a negative value scrolls up.
     */
    public void scrollBy(int amount) {
        amountScrolled += (float) amount;
        bindAmountScrolled();
        initialClickY = -2;
    }

    public void actionPerformed(ButtonWidget button) {
        if (button.enabled) {
            if (button.id == scrollUpButtonID) {
                amountScrolled -= (float) (entryHeight * 2 / 3);
                initialClickY = -2;
                bindAmountScrolled();
            } else if (button.id == scrollDownButtonID) {
                amountScrolled += (float) (entryHeight * 2 / 3);
                initialClickY = -2;
                bindAmountScrolled();
            }
        }
    }

    public void drawScreenSkeet(int mouseXIn, int mouseYIn, float partialTicks) {
        if (visible) {
            mouseX = mouseXIn;
            mouseY = mouseYIn;
            int i = getScrollBarX();
            int j = i + 6;
            bindAmountScrolled();
            GlStateManager.disableLighting();
            GlStateManager.disableFog();
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuffer();
            ScaledResolution sr = new ScaledResolution(mc);
            RectHelper.drawBorderedRect(-5, 0, sr.getScaledWidth() - -6, sr.getScaledHeight(), 0.5F, (new Color(44, 44, 44, 255)).getRGB(), (new Color(33, 33, 33, 255)).getRGB(), true);
            RectHelper.drawBorderedRect(1, 1.4F, sr.getScaledWidth() - 1, sr.getScaledHeight() - 1.7F, 0.5F, (new Color(17, 17, 17, 255)).getRGB(), (new Color(33, 33, 33, 255)).getRGB(), true);
            RenderHelper.drawImage(new Namespaced("moonware/skeet.png"), 1, 1, sr.getScaledWidth(), 1, Color.white);
            int k = left + width / 2 - getListWidth() / 2 + 2;
            int l = top + 4 - (int) amountScrolled;

            if (hasListHeader) {
                drawListHeader(k, l, tessellator);
            }

            func_192638_a(k, l, mouseXIn, mouseYIn, partialTicks);
            GlStateManager.disableDepth();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE);
            GlStateManager.disableAlpha();
            GlStateManager.shadeModel(7425);
            GlStateManager.disableTexture2D();
            int j1 = getMaxScroll();

            if (j1 > 0) {
                int k1 = (bottom - top) * (bottom - top) / getContentHeight();
                k1 = MathHelper.clamp(k1, 32, bottom - top - 8);
                int l1 = (int) amountScrolled * (bottom - top - k1) / j1 + top;

                if (l1 < top) {
                    l1 = top;
                }

                bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
                bufferbuilder.pos(i, bottom, 0.0D).tex(0.0D, 1.0D).color(0, 0, 0, 255).endVertex();
                bufferbuilder.pos(j, bottom, 0.0D).tex(1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
                bufferbuilder.pos(j, top, 0.0D).tex(1.0D, 0.0D).color(0, 0, 0, 255).endVertex();
                bufferbuilder.pos(i, top, 0.0D).tex(0.0D, 0.0D).color(0, 0, 0, 255).endVertex();
                tessellator.draw();
                bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
                bufferbuilder.pos(i, l1 + k1, 0.0D).tex(0.0D, 1.0D).color(128, 128, 128, 255).endVertex();
                bufferbuilder.pos(j, l1 + k1, 0.0D).tex(1.0D, 1.0D).color(128, 128, 128, 255).endVertex();
                bufferbuilder.pos(j, l1, 0.0D).tex(1.0D, 0.0D).color(128, 128, 128, 255).endVertex();
                bufferbuilder.pos(i, l1, 0.0D).tex(0.0D, 0.0D).color(128, 128, 128, 255).endVertex();
                tessellator.draw();
                bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
                bufferbuilder.pos(i, l1 + k1 - 1, 0.0D).tex(0.0D, 1.0D).color(192, 192, 192, 255).endVertex();
                bufferbuilder.pos(j - 1, l1 + k1 - 1, 0.0D).tex(1.0D, 1.0D).color(192, 192, 192, 255).endVertex();
                bufferbuilder.pos(j - 1, l1, 0.0D).tex(1.0D, 0.0D).color(192, 192, 192, 255).endVertex();
                bufferbuilder.pos(i, l1, 0.0D).tex(0.0D, 0.0D).color(192, 192, 192, 255).endVertex();
                tessellator.draw();
            }

            renderDecorations(mouseXIn, mouseYIn);
            GlStateManager.enableTexture2D();
            GlStateManager.shadeModel(7424);
            GlStateManager.enableAlpha();
            GlStateManager.disableBlend();
        }
    }

    public void drawMultiScreen(int mouseXIn, int mouseYIn, float partialTicks) {
        if (visible) {
            mouseX = mouseXIn;
            mouseY = mouseYIn;
            //this.drawBackground();
            ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
            //GradientUtil.drawGradient(0,0,sr.getScaledWidth(),sr.getScaledHeight(),255, new Color(211, 176, 218),new Color(789231234),new Color(129, 232, 202),new Color(255, 148, 148));
            //drawMoonWareBackrdound(mouseX, mouseY, partialTicks);
            RenderUtils2.drawBlur(15, () -> RectHelper.drawRectWithGlow(sr.getScaledWidth() / 2 - sr.getScaledWidth() / 4, -4, sr.getScaledWidth() / 2 + sr.getScaledWidth() / 4, sr.getScaledHeight(), 5, 7, DrawHelper.setAlpha(new Color(31, 31, 31), 20)));
            RenderUtils2.drawShadow(2,8, () -> RectHelper.drawRectWithGlow(sr.getScaledWidth() / 2 - sr.getScaledWidth() / 4, -4, sr.getScaledWidth() / 2 + sr.getScaledWidth() / 4, sr.getScaledHeight(), 5, 7, DrawHelper.setAlpha(new Color(31, 31, 31), 20)));

            GlStateManager.pushMatrix();
            GL11.glColor4f(Color.WHITE.getRed(), Color.WHITE.getGreen(),Color.WHITE.getBlue(),95);
            RectHelper.drawRectWithGlow(sr.getScaledWidth() / 2 - sr.getScaledWidth() / 4, -4, sr.getScaledWidth() / 2 + sr.getScaledWidth() / 4, sr.getScaledHeight(), 5, 7, DrawHelper.setAlpha(new Color(31, 31, 31), 20));
            GlStateManager.popMatrix();
            Gui.drawRect(0, 0, 0, 0, 0);
            int i = getScrollBarX();
            int j = i + 6;
            bindAmountScrolled();
            GlStateManager.disableLighting();
            GlStateManager.disableFog();
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuffer();
            //this.drawContainerBackground(tessellator);
            //GradientUtil.drawGradient(0,0,sr.getScaledWidth(),sr.getScaledHeight(),255, new Color(211, 176, 218),new Color(789231234),new Color(129, 232, 202),new Color(255, 148, 148));

            int k = left + width / 2 - getListWidth() / 2 + 2;
            int l = top + 4 - (int) amountScrolled;

            if (hasListHeader) {
                drawListHeader(k, l, tessellator);
            }

            func_192638_a(k, l, mouseXIn, mouseYIn, partialTicks);
            GlStateManager.disableDepth();
            //this.overlayBackground(0, this.top, 255, 255);
            //this.overlayBackground(this.bottom, this.height, 255, 255);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE);
            GlStateManager.disableAlpha();
            GlStateManager.shadeModel(7425);
            GlStateManager.disableTexture2D();
            int i1 = 4;
            /*
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            bufferbuilder.pos((double) this.left, (double) (this.top + 4), 0.0D).tex(0.0D, 1.0D).color(0, 0, 0, 0).endVertex();
            bufferbuilder.pos((double) this.right, (double) (this.top + 4), 0.0D).tex(1.0D, 1.0D).color(0, 0, 0, 0).endVertex();
            bufferbuilder.pos((double) this.right, (double) this.top, 0.0D).tex(1.0D, 0.0D).color(0, 0, 0, 255).endVertex();
            bufferbuilder.pos((double) this.left, (double) this.top, 0.0D).tex(0.0D, 0.0D).color(0, 0, 0, 255).endVertex();
            tessellator.draw();
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            bufferbuilder.pos((double) this.left, (double) this.bottom, 0.0D).tex(0.0D, 1.0D).color(0, 0, 0, 255).endVertex();
            bufferbuilder.pos((double) this.right, (double) this.bottom, 0.0D).tex(1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
            bufferbuilder.pos((double) this.right, (double) (this.bottom - 4), 0.0D).tex(1.0D, 0.0D).color(0, 0, 0, 0).endVertex();
            bufferbuilder.pos((double) this.left, (double) (this.bottom - 4), 0.0D).tex(0.0D, 0.0D).color(0, 0, 0, 0).endVertex();
            tessellator.draw();


             */
            int j1 = getMaxScroll();

            if (j1 > 0) {
                int k1 = (bottom - top) * (bottom - top) / getContentHeight();
                k1 = MathHelper.clamp(k1, 32, bottom - top - 8);
                int l1 = (int) amountScrolled * (bottom - top - k1) / j1 + top;

                if (l1 < top) {
                    l1 = top;
                }

                /*
                bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
                bufferbuilder.pos((double) i, (double) this.bottom, 0.0D).tex(0.0D, 1.0D).color(0, 0, 0, 255).endVertex();
                bufferbuilder.pos((double) j, (double) this.bottom, 0.0D).tex(1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
                bufferbuilder.pos((double) j, (double) this.top, 0.0D).tex(1.0D, 0.0D).color(0, 0, 0, 255).endVertex();
                bufferbuilder.pos((double) i, (double) this.top, 0.0D).tex(0.0D, 0.0D).color(0, 0, 0, 255).endVertex();
                tessellator.draw();
                bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
                bufferbuilder.pos((double) i, (double) (l1 + k1), 0.0D).tex(0.0D, 1.0D).color(128, 128, 128, 255).endVertex();
                bufferbuilder.pos((double) j, (double) (l1 + k1), 0.0D).tex(1.0D, 1.0D).color(128, 128, 128, 255).endVertex();
                bufferbuilder.pos((double) j, (double) l1, 0.0D).tex(1.0D, 0.0D).color(128, 128, 128, 255).endVertex();
                bufferbuilder.pos((double) i, (double) l1, 0.0D).tex(0.0D, 0.0D).color(128, 128, 128, 255).endVertex();
                tessellator.draw();
                bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
                bufferbuilder.pos((double) i, (double) (l1 + k1 - 1), 0.0D).tex(0.0D, 1.0D).color(192, 192, 192, 255).endVertex();
                bufferbuilder.pos((double) (j - 1), (double) (l1 + k1 - 1), 0.0D).tex(1.0D, 1.0D).color(192, 192, 192, 255).endVertex();
                bufferbuilder.pos((double) (j - 1), (double) l1, 0.0D).tex(1.0D, 0.0D).color(192, 192, 192, 255).endVertex();
                bufferbuilder.pos((double) i, (double) l1, 0.0D).tex(0.0D, 0.0D).color(192, 192, 192, 255).endVertex();
                tessellator.draw();


                 */
            }

            renderDecorations(mouseXIn, mouseYIn);
            GlStateManager.enableTexture2D();
            GlStateManager.shadeModel(7424);
            GlStateManager.enableAlpha();
            GlStateManager.disableBlend();
        }
    }

    public void drawScreen(int mouseXIn, int mouseYIn, float partialTicks) {
        if (visible) {
            mouseX = mouseXIn;
            mouseY = mouseYIn;
            //this.drawBackground();
            ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
            //GradientUtil.drawGradient(0,0,sr.getScaledWidth(),sr.getScaledHeight(),255, new Color(211, 176, 218),new Color(789231234),new Color(129, 232, 202),new Color(255, 148, 148));
            int i = getScrollBarX();
            int j = i + 6;
            bindAmountScrolled();
            GlStateManager.disableLighting();
            GlStateManager.disableFog();
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuffer();
            //this.drawContainerBackground(tessellator);
            //GradientUtil.drawGradient(0,0,sr.getScaledWidth(),sr.getScaledHeight(),255, new Color(211, 176, 218),new Color(789231234),new Color(129, 232, 202),new Color(255, 148, 148));

            int k = left + width / 2 - getListWidth() / 2 + 2;
            int l = top + 4 - (int) amountScrolled;

            if (hasListHeader) {
                drawListHeader(k, l, tessellator);
            }

            func_192638_a(k, l, mouseXIn, mouseYIn, partialTicks);
            GlStateManager.disableDepth();
            //this.overlayBackground(0, this.top, 255, 255);
            //this.overlayBackground(this.bottom, this.height, 255, 255);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE);
            GlStateManager.disableAlpha();
            GlStateManager.shadeModel(7425);
            GlStateManager.disableTexture2D();
            int i1 = 4;
            /*
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            bufferbuilder.pos((double) this.left, (double) (this.top + 4), 0.0D).tex(0.0D, 1.0D).color(0, 0, 0, 0).endVertex();
            bufferbuilder.pos((double) this.right, (double) (this.top + 4), 0.0D).tex(1.0D, 1.0D).color(0, 0, 0, 0).endVertex();
            bufferbuilder.pos((double) this.right, (double) this.top, 0.0D).tex(1.0D, 0.0D).color(0, 0, 0, 255).endVertex();
            bufferbuilder.pos((double) this.left, (double) this.top, 0.0D).tex(0.0D, 0.0D).color(0, 0, 0, 255).endVertex();
            tessellator.draw();
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            bufferbuilder.pos((double) this.left, (double) this.bottom, 0.0D).tex(0.0D, 1.0D).color(0, 0, 0, 255).endVertex();
            bufferbuilder.pos((double) this.right, (double) this.bottom, 0.0D).tex(1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
            bufferbuilder.pos((double) this.right, (double) (this.bottom - 4), 0.0D).tex(1.0D, 0.0D).color(0, 0, 0, 0).endVertex();
            bufferbuilder.pos((double) this.left, (double) (this.bottom - 4), 0.0D).tex(0.0D, 0.0D).color(0, 0, 0, 0).endVertex();
            tessellator.draw();


             */
            int j1 = getMaxScroll();

            if (j1 > 0) {
                int k1 = (bottom - top) * (bottom - top) / getContentHeight();
                k1 = MathHelper.clamp(k1, 32, bottom - top - 8);
                int l1 = (int) amountScrolled * (bottom - top - k1) / j1 + top;

                if (l1 < top) {
                    l1 = top;
                }

                /*
                bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
                bufferbuilder.pos((double) i, (double) this.bottom, 0.0D).tex(0.0D, 1.0D).color(0, 0, 0, 255).endVertex();
                bufferbuilder.pos((double) j, (double) this.bottom, 0.0D).tex(1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
                bufferbuilder.pos((double) j, (double) this.top, 0.0D).tex(1.0D, 0.0D).color(0, 0, 0, 255).endVertex();
                bufferbuilder.pos((double) i, (double) this.top, 0.0D).tex(0.0D, 0.0D).color(0, 0, 0, 255).endVertex();
                tessellator.draw();
                bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
                bufferbuilder.pos((double) i, (double) (l1 + k1), 0.0D).tex(0.0D, 1.0D).color(128, 128, 128, 255).endVertex();
                bufferbuilder.pos((double) j, (double) (l1 + k1), 0.0D).tex(1.0D, 1.0D).color(128, 128, 128, 255).endVertex();
                bufferbuilder.pos((double) j, (double) l1, 0.0D).tex(1.0D, 0.0D).color(128, 128, 128, 255).endVertex();
                bufferbuilder.pos((double) i, (double) l1, 0.0D).tex(0.0D, 0.0D).color(128, 128, 128, 255).endVertex();
                tessellator.draw();
                bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
                bufferbuilder.pos((double) i, (double) (l1 + k1 - 1), 0.0D).tex(0.0D, 1.0D).color(192, 192, 192, 255).endVertex();
                bufferbuilder.pos((double) (j - 1), (double) (l1 + k1 - 1), 0.0D).tex(1.0D, 1.0D).color(192, 192, 192, 255).endVertex();
                bufferbuilder.pos((double) (j - 1), (double) l1, 0.0D).tex(1.0D, 0.0D).color(192, 192, 192, 255).endVertex();
                bufferbuilder.pos((double) i, (double) l1, 0.0D).tex(0.0D, 0.0D).color(192, 192, 192, 255).endVertex();
                tessellator.draw();


                 */
            }

            renderDecorations(mouseXIn, mouseYIn);
            GlStateManager.enableTexture2D();
            GlStateManager.shadeModel(7424);
            GlStateManager.enableAlpha();
            GlStateManager.disableBlend();
        }
    }

    public void drawScreenSinglePlayer(int mouseXIn, int mouseYIn, float partialTicks) {
        if (visible) {
            mouseX = mouseXIn;
            mouseY = mouseYIn;
            //this.drawBackground();
            ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
            //GradientUtil.drawGradient(0,0,sr.getScaledWidth(),sr.getScaledHeight(),255, new Color(211, 176, 218),new Color(789231234),new Color(129, 232, 202),new Color(255, 148, 148));
//            if (Minecraft.world == null)
//                drawMoonWareBackrdound(mouseX, mouseY, partialTicks);
            RenderUtils2.drawBlur(15, () -> RectHelper.drawRectWithGlow(sr.getScaledWidth() / 2 - sr.getScaledWidth() / 4, -4, sr.getScaledWidth() / 2 + sr.getScaledWidth() / 4, sr.getScaledHeight(), 5, 7, DrawHelper.setAlpha(new Color(31, 31, 31), 20)));
            RenderUtils2.drawShadow(2,8, () -> RectHelper.drawRectWithGlow(sr.getScaledWidth() / 2 - sr.getScaledWidth() / 4, -4, sr.getScaledWidth() / 2 + sr.getScaledWidth() / 4, sr.getScaledHeight(), 5, 7, DrawHelper.setAlpha(new Color(31, 31, 31), 20)));

            GlStateManager.pushMatrix();
            GL11.glColor4f(Color.WHITE.getRed(), Color.WHITE.getGreen(),Color.WHITE.getBlue(),95);
            RectHelper.drawRectWithGlow(sr.getScaledWidth() / 2 - sr.getScaledWidth() / 4, -4, sr.getScaledWidth() / 2 + sr.getScaledWidth() / 4, sr.getScaledHeight(), 5, 7, DrawHelper.setAlpha(new Color(31, 31, 31), 20));
           GlStateManager.popMatrix();
            int i = getScrollBarX();
            int j = i + 6;
            bindAmountScrolled();
            GlStateManager.disableLighting();
            GlStateManager.disableFog();
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuffer();
            //this.drawContainerBackground(tessellator);
            //GradientUtil.drawGradient(0,0,sr.getScaledWidth(),sr.getScaledHeight(),255, new Color(211, 176, 218),new Color(789231234),new Color(129, 232, 202),new Color(255, 148, 148));

            int k = left + width / 2 - getListWidth() / 2 + 2;
            int l = top + 4 - (int) amountScrolled;

            if (hasListHeader) {
                drawListHeader(k, l, tessellator);
            }

            func_192638_a(k, l, mouseXIn, mouseYIn, partialTicks);
            GlStateManager.disableDepth();
            //this.overlayBackground(0, this.top, 255, 255);
            //this.overlayBackground(this.bottom, this.height, 255, 255);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE);
            GlStateManager.disableAlpha();
            GlStateManager.shadeModel(7425);
            GlStateManager.disableTexture2D();
            int i1 = 4;
            /*
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            bufferbuilder.pos((double) this.left, (double) (this.top + 4), 0.0D).tex(0.0D, 1.0D).color(0, 0, 0, 0).endVertex();
            bufferbuilder.pos((double) this.right, (double) (this.top + 4), 0.0D).tex(1.0D, 1.0D).color(0, 0, 0, 0).endVertex();
            bufferbuilder.pos((double) this.right, (double) this.top, 0.0D).tex(1.0D, 0.0D).color(0, 0, 0, 255).endVertex();
            bufferbuilder.pos((double) this.left, (double) this.top, 0.0D).tex(0.0D, 0.0D).color(0, 0, 0, 255).endVertex();
            tessellator.draw();
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            bufferbuilder.pos((double) this.left, (double) this.bottom, 0.0D).tex(0.0D, 1.0D).color(0, 0, 0, 255).endVertex();
            bufferbuilder.pos((double) this.right, (double) this.bottom, 0.0D).tex(1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
            bufferbuilder.pos((double) this.right, (double) (this.bottom - 4), 0.0D).tex(1.0D, 0.0D).color(0, 0, 0, 0).endVertex();
            bufferbuilder.pos((double) this.left, (double) (this.bottom - 4), 0.0D).tex(0.0D, 0.0D).color(0, 0, 0, 0).endVertex();
            tessellator.draw();


             */
            int j1 = getMaxScroll();

            if (j1 > 0) {
                int k1 = (bottom - top) * (bottom - top) / getContentHeight();
                k1 = MathHelper.clamp(k1, 32, bottom - top - 8);
                int l1 = (int) amountScrolled * (bottom - top - k1) / j1 + top;

                if (l1 < top) {
                    l1 = top;
                }

                /*
                bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
                bufferbuilder.pos((double) i, (double) this.bottom, 0.0D).tex(0.0D, 1.0D).color(0, 0, 0, 255).endVertex();
                bufferbuilder.pos((double) j, (double) this.bottom, 0.0D).tex(1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
                bufferbuilder.pos((double) j, (double) this.top, 0.0D).tex(1.0D, 0.0D).color(0, 0, 0, 255).endVertex();
                bufferbuilder.pos((double) i, (double) this.top, 0.0D).tex(0.0D, 0.0D).color(0, 0, 0, 255).endVertex();
                tessellator.draw();
                bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
                bufferbuilder.pos((double) i, (double) (l1 + k1), 0.0D).tex(0.0D, 1.0D).color(128, 128, 128, 255).endVertex();
                bufferbuilder.pos((double) j, (double) (l1 + k1), 0.0D).tex(1.0D, 1.0D).color(128, 128, 128, 255).endVertex();
                bufferbuilder.pos((double) j, (double) l1, 0.0D).tex(1.0D, 0.0D).color(128, 128, 128, 255).endVertex();
                bufferbuilder.pos((double) i, (double) l1, 0.0D).tex(0.0D, 0.0D).color(128, 128, 128, 255).endVertex();
                tessellator.draw();
                bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
                bufferbuilder.pos((double) i, (double) (l1 + k1 - 1), 0.0D).tex(0.0D, 1.0D).color(192, 192, 192, 255).endVertex();
                bufferbuilder.pos((double) (j - 1), (double) (l1 + k1 - 1), 0.0D).tex(1.0D, 1.0D).color(192, 192, 192, 255).endVertex();
                bufferbuilder.pos((double) (j - 1), (double) l1, 0.0D).tex(1.0D, 0.0D).color(192, 192, 192, 255).endVertex();
                bufferbuilder.pos((double) i, (double) l1, 0.0D).tex(0.0D, 0.0D).color(192, 192, 192, 255).endVertex();
                tessellator.draw();


                 */
            }

            renderDecorations(mouseXIn, mouseYIn);
            GlStateManager.enableTexture2D();
            GlStateManager.shadeModel(7424);
            GlStateManager.enableAlpha();
            GlStateManager.disableBlend();
        }
    }

    public void drawScreenB(int mouseXIn, int mouseYIn, float partialTicks) {
        if (visible) {
            mouseX = mouseXIn;
            mouseY = mouseYIn;
            //this.drawBackground();
            ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
            //GradientUtil.drawGradient(0,0,sr.getScaledWidth(),sr.getScaledHeight(),255, new Color(211, 176, 218),new Color(789231234),new Color(129, 232, 202),new Color(255, 148, 148));

            //this.drawMoonWareBackrdound(mouseX,mouseY,partialTicks);
            int i = getScrollBarX();
            int j = i + 6;
            bindAmountScrolled();
            GlStateManager.disableLighting();
            GlStateManager.disableFog();
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuffer();
            //this.drawContainerBackground(tessellator);
            //GradientUtil.drawGradient(0,0,sr.getScaledWidth(),sr.getScaledHeight(),255, new Color(211, 176, 218),new Color(789231234),new Color(129, 232, 202),new Color(255, 148, 148));

            int k = left + width / 2 - getListWidth() / 2 + 2;
            int l = top + 4 - (int) amountScrolled;

            if (hasListHeader) {
                drawListHeader(k, l, tessellator);
            }

            func_192638_a(k, l, mouseXIn, mouseYIn, partialTicks);
            GlStateManager.disableDepth();
            //this.overlayBackground(0, this.top, 255, 255);
            //this.overlayBackground(this.bottom, this.height, 255, 255);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE);
            GlStateManager.disableAlpha();
            GlStateManager.shadeModel(7425);
            GlStateManager.disableTexture2D();
            int i1 = 4;
            /*
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            bufferbuilder.pos((double) this.left, (double) (this.top + 4), 0.0D).tex(0.0D, 1.0D).color(0, 0, 0, 0).endVertex();
            bufferbuilder.pos((double) this.right, (double) (this.top + 4), 0.0D).tex(1.0D, 1.0D).color(0, 0, 0, 0).endVertex();
            bufferbuilder.pos((double) this.right, (double) this.top, 0.0D).tex(1.0D, 0.0D).color(0, 0, 0, 255).endVertex();
            bufferbuilder.pos((double) this.left, (double) this.top, 0.0D).tex(0.0D, 0.0D).color(0, 0, 0, 255).endVertex();
            tessellator.draw();
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            bufferbuilder.pos((double) this.left, (double) this.bottom, 0.0D).tex(0.0D, 1.0D).color(0, 0, 0, 255).endVertex();
            bufferbuilder.pos((double) this.right, (double) this.bottom, 0.0D).tex(1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
            bufferbuilder.pos((double) this.right, (double) (this.bottom - 4), 0.0D).tex(1.0D, 0.0D).color(0, 0, 0, 0).endVertex();
            bufferbuilder.pos((double) this.left, (double) (this.bottom - 4), 0.0D).tex(0.0D, 0.0D).color(0, 0, 0, 0).endVertex();
            tessellator.draw();


             */
            int j1 = getMaxScroll();

            if (j1 > 0) {
                int k1 = (bottom - top) * (bottom - top) / getContentHeight();
                k1 = MathHelper.clamp(k1, 32, bottom - top - 8);
                int l1 = (int) amountScrolled * (bottom - top - k1) / j1 + top;

                if (l1 < top) {
                    l1 = top;
                }

                /*
                bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
                bufferbuilder.pos((double) i, (double) this.bottom, 0.0D).tex(0.0D, 1.0D).color(0, 0, 0, 255).endVertex();
                bufferbuilder.pos((double) j, (double) this.bottom, 0.0D).tex(1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
                bufferbuilder.pos((double) j, (double) this.top, 0.0D).tex(1.0D, 0.0D).color(0, 0, 0, 255).endVertex();
                bufferbuilder.pos((double) i, (double) this.top, 0.0D).tex(0.0D, 0.0D).color(0, 0, 0, 255).endVertex();
                tessellator.draw();
                bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
                bufferbuilder.pos((double) i, (double) (l1 + k1), 0.0D).tex(0.0D, 1.0D).color(128, 128, 128, 255).endVertex();
                bufferbuilder.pos((double) j, (double) (l1 + k1), 0.0D).tex(1.0D, 1.0D).color(128, 128, 128, 255).endVertex();
                bufferbuilder.pos((double) j, (double) l1, 0.0D).tex(1.0D, 0.0D).color(128, 128, 128, 255).endVertex();
                bufferbuilder.pos((double) i, (double) l1, 0.0D).tex(0.0D, 0.0D).color(128, 128, 128, 255).endVertex();
                tessellator.draw();
                bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
                bufferbuilder.pos((double) i, (double) (l1 + k1 - 1), 0.0D).tex(0.0D, 1.0D).color(192, 192, 192, 255).endVertex();
                bufferbuilder.pos((double) (j - 1), (double) (l1 + k1 - 1), 0.0D).tex(1.0D, 1.0D).color(192, 192, 192, 255).endVertex();
                bufferbuilder.pos((double) (j - 1), (double) l1, 0.0D).tex(1.0D, 0.0D).color(192, 192, 192, 255).endVertex();
                bufferbuilder.pos((double) i, (double) l1, 0.0D).tex(0.0D, 0.0D).color(192, 192, 192, 255).endVertex();
                tessellator.draw();


                 */
            }

            renderDecorations(mouseXIn, mouseYIn);
            GlStateManager.enableTexture2D();
            GlStateManager.shadeModel(7424);
            GlStateManager.enableAlpha();
            GlStateManager.disableBlend();
        }
    }

    public void handleMouseInput() {
        if (isMouseYWithinSlotBounds(mouseY)) {
            if (Mouse.getEventButton() == 0 && Mouse.getEventButtonState() && mouseY >= top && mouseY <= bottom) {
                int i = (width - getListWidth()) / 2;
                int j = (width + getListWidth()) / 2;
                int k = mouseY - top - headerPadding + (int) amountScrolled - 4;
                int l = k / entryHeight;

                if (l < getSize() && mouseX >= i && mouseX <= j && l >= 0 && k >= 0) {
                    elementClicked(l, false, mouseX, mouseY);
                    selectedElement = l;
                } else if (mouseX >= i && mouseX <= j && k < 0) {
                    clickedHeader(mouseX - i, mouseY - top + (int) amountScrolled - 4);
                }
            }

            if (Mouse.isButtonDown(0) && getEnabled()) {
                if (initialClickY != -1) {
                    if (initialClickY >= 0) {
                        amountScrolled -= (float) (mouseY - initialClickY) * scrollMultiplier;
                        initialClickY = mouseY;
                    }
                } else {
                    boolean flag1 = true;

                    if (mouseY >= top && mouseY <= bottom) {
                        int j2 = (width - getListWidth()) / 2;
                        int k2 = (width + getListWidth()) / 2;
                        int l2 = mouseY - top - headerPadding + (int) amountScrolled - 4;
                        int i1 = l2 / entryHeight;

                        if (i1 < getSize() && mouseX >= j2 && mouseX <= k2 && i1 >= 0 && l2 >= 0) {
                            boolean flag = i1 == selectedElement && Minecraft.getSystemTime() - lastClicked < 250L;
                            elementClicked(i1, flag, mouseX, mouseY);
                            selectedElement = i1;
                            lastClicked = Minecraft.getSystemTime();
                        } else if (mouseX >= j2 && mouseX <= k2 && l2 < 0) {
                            clickedHeader(mouseX - j2, mouseY - top + (int) amountScrolled - 4);
                            flag1 = false;
                        }

                        int i3 = getScrollBarX();
                        int j1 = i3 + 6;

                        if (mouseX >= i3 && mouseX <= j1) {
                            scrollMultiplier = -1.0F;
                            int k1 = getMaxScroll();

                            if (k1 < 1) {
                                k1 = 1;
                            }

                            int l1 = (int) ((float) ((bottom - top) * (bottom - top)) / (float) getContentHeight());
                            l1 = MathHelper.clamp(l1, 32, bottom - top - 8);
                            scrollMultiplier /= (float) (bottom - top - l1) / (float) k1;
                        } else {
                            scrollMultiplier = 1.0F;
                        }

                        if (flag1) {
                            initialClickY = mouseY;
                        } else {
                            initialClickY = -2;
                        }
                    } else {
                        initialClickY = -2;
                    }
                }
            } else {
                initialClickY = -1;
            }

            int i2 = Mouse.getEventDWheel();

            if (i2 != 0) {
                if (i2 > 0) {
                    i2 = -1;
                } else if (i2 < 0) {
                    i2 = 1;
                }

                amountScrolled += (float) (i2 * entryHeight / 2);
            }
        }
    }

    public void setEnabled(boolean enabledIn) {
        enabled = enabledIn;
    }

    public boolean getEnabled() {
        return enabled;
    }

    /**
     * Gets the width of the list
     */
    public int getListWidth() {
        return 220;
    }

    protected void func_192638_a(int p_192638_1_, int p_192638_2_, int p_192638_3_, int p_192638_4_,
                                 float p_192638_5_) {
        int i = getSize();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();

        for (int j = 0; j < i; ++j) {
            int k = p_192638_2_ + j * entryHeight + headerPadding;
            int l = entryHeight - 4;

            if (k > bottom || k + l < top) {
                func_192639_a(j, p_192638_1_, k, p_192638_5_);
            }

            if (showSelectionBox && isSelected(j)) {
                int i1 = left + (width / 2 - getListWidth() / 2);
                int j1 = left + width / 2 + getListWidth() / 2;
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                GlStateManager.disableTexture2D();
                bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
                bufferbuilder.pos(i1, k + l + 2, 0.0D).tex(0.0D, 1.0D).color(128, 128, 128, 255).endVertex();
                bufferbuilder.pos(j1, k + l + 2, 0.0D).tex(1.0D, 1.0D).color(128, 128, 128, 255).endVertex();
                bufferbuilder.pos(j1, k - 2, 0.0D).tex(1.0D, 0.0D).color(128, 128, 128, 255).endVertex();
                bufferbuilder.pos(i1, k - 2, 0.0D).tex(0.0D, 0.0D).color(128, 128, 128, 255).endVertex();
                bufferbuilder.pos(i1 + 1, k + l + 1, 0.0D).tex(0.0D, 1.0D).color(0, 0, 0, 255).endVertex();
                bufferbuilder.pos(j1 - 1, k + l + 1, 0.0D).tex(1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
                bufferbuilder.pos(j1 - 1, k - 1, 0.0D).tex(1.0D, 0.0D).color(0, 0, 0, 255).endVertex();
                bufferbuilder.pos(i1 + 1, k - 1, 0.0D).tex(0.0D, 0.0D).color(0, 0, 0, 255).endVertex();
                tessellator.draw();
                GlStateManager.enableTexture2D();
            }

            if (k >= top - entryHeight && k <= bottom) {
                func_192637_a(j, p_192638_1_, k, l, p_192638_3_, p_192638_4_, p_192638_5_);
            }
        }
    }

    protected int getScrollBarX() {
        return width / 2 + 124;
    }

    /**
     * Overlays the background to hide scrolled items
     */
    protected void overlayBackground(int startY, int endY, int startAlpha, int endAlpha) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        Minecraft.getTextureManager().bindTexture(Gui.OPTIONS_BACKGROUND);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        float f = 32.0F;
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        bufferbuilder.pos(left, endY, 0.0D).tex(0.0D, (float) endY / 32.0F).color(64, 64, 64, endAlpha).endVertex();
        bufferbuilder.pos(left + width, endY, 0.0D).tex((float) width / 32.0F, (float) endY / 32.0F).color(64, 64, 64, endAlpha).endVertex();
        bufferbuilder.pos(left + width, startY, 0.0D).tex((float) width / 32.0F, (float) startY / 32.0F).color(64, 64, 64, startAlpha).endVertex();
        bufferbuilder.pos(left, startY, 0.0D).tex(0.0D, (float) startY / 32.0F).color(64, 64, 64, startAlpha).endVertex();
        tessellator.draw();
    }

    /**
     * Sets the left and right bounds of the slot. Param is the left bound, right is calculated as left + width.
     */
    public void setSlotXBoundsFromLeft(int leftIn) {
        left = leftIn;
        right = leftIn + width;
    }

    public int getEntryHeight() {
        return entryHeight;
    }

    protected void drawContainerBackground(Tessellator p_drawContainerBackground_1_) {
        BufferBuilder bufferbuilder = p_drawContainerBackground_1_.getBuffer();
        Minecraft.getTextureManager().bindTexture(Gui.OPTIONS_BACKGROUND);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        float f = 32.0F;
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        bufferbuilder.pos(left, bottom, 0.0D).tex((float) left / 32.0F, (float) (bottom + (int) amountScrolled) / 32.0F).color(32, 32, 32, 255).endVertex();
        bufferbuilder.pos(right, bottom, 0.0D).tex((float) right / 32.0F, (float) (bottom + (int) amountScrolled) / 32.0F).color(32, 32, 32, 255).endVertex();
        bufferbuilder.pos(right, top, 0.0D).tex((float) right / 32.0F, (float) (top + (int) amountScrolled) / 32.0F).color(32, 32, 32, 255).endVertex();
        bufferbuilder.pos(left, top, 0.0D).tex((float) left / 32.0F, (float) (top + (int) amountScrolled) / 32.0F).color(32, 32, 32, 255).endVertex();
        p_drawContainerBackground_1_.draw();
    }
}
