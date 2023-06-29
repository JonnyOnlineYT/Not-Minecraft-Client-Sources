package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import com.google.gson.JsonParseException;
import io.netty.buffer.Unpooled;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemWrittenBook;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.Namespaced;
import net.minecraft.util.OS;
import net.minecraft.util.text.Component;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.Formatting;
import net.minecraft.util.text.event.ClickEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;

public class GuiScreenBook extends Screen
{
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Namespaced BOOK_GUI_TEXTURES = new Namespaced("textures/gui/book.png");

    /** The player editing the book */
    private final EntityPlayer editingPlayer;
    private final ItemStack bookObj;

    /** Whether the book is signed or can still be edited */
    private final boolean bookIsUnsigned;

    /**
     * Whether the book's title or contents has been modified since being opened
     */
    private boolean bookIsModified;

    /** Determines if the signing screen is open */
    private boolean bookGettingSigned;

    /** Update ticks since the gui was opened */
    private int updateCount;
    private final int bookImageWidth = 192;
    private final int bookImageHeight = 192;
    private int bookTotalPages = 1;
    private int currPage;
    private NBTTagList bookPages;
    private String bookTitle = "";
    private List<Component> cachedComponents;
    private int cachedPage = -1;
    private GuiScreenBook.NextPageButton buttonNextPage;
    private GuiScreenBook.NextPageButton buttonPreviousPage;
    private ButtonWidget buttonDone;

    /** The GuiButton to sign this book. */
    private ButtonWidget buttonSign;
    private ButtonWidget buttonFinalize;
    private ButtonWidget buttonCancel;

    public GuiScreenBook(EntityPlayer player, ItemStack book, boolean isUnsigned)
    {
        editingPlayer = player;
        bookObj = book;
        bookIsUnsigned = isUnsigned;

        if (book.hasTagCompound())
        {
            NBTTagCompound nbttagcompound = book.getTagCompound();
            bookPages = nbttagcompound.getTagList("pages", 8).copy();
            bookTotalPages = bookPages.tagCount();

            if (bookTotalPages < 1)
            {
                bookTotalPages = 1;
            }
        }

        if (bookPages == null && isUnsigned)
        {
            bookPages = new NBTTagList();
            bookPages.appendTag(new NBTTagString(""));
            bookTotalPages = 1;
        }
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void update()
    {
        super.update();
        ++updateCount;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    public void init()
    {
        widgets.clear();
        Keyboard.enableRepeatEvents(true);

        if (bookIsUnsigned)
        {
            buttonSign = addButton(new ButtonWidget(3, width / 2 - 100, 196, 98, 20, I18n.format("book.signButton")));
            buttonDone = addButton(new ButtonWidget(0, width / 2 + 2, 196, 98, 20, I18n.format("gui.done")));
            buttonFinalize = addButton(new ButtonWidget(5, width / 2 - 100, 196, 98, 20, I18n.format("book.finalizeButton")));
            buttonCancel = addButton(new ButtonWidget(4, width / 2 + 2, 196, 98, 20, I18n.format("gui.cancel")));
        }
        else
        {
            buttonDone = addButton(new ButtonWidget(0, width / 2 - 100, 196, 200, 20, I18n.format("gui.done")));
        }

        int i = (width - 192) / 2;
        int j = 2;
        buttonNextPage = addButton(new NextPageButton(1, i + 120, 156, true));
        buttonPreviousPage = addButton(new NextPageButton(2, i + 38, 156, false));
        updateButtons();
    }

    /**
     * Called when the screen is unloaded. Used to disable keyboard repeat events
     */
    public void onClosed()
    {
        Keyboard.enableRepeatEvents(false);
    }

    private void updateButtons()
    {
        buttonNextPage.visible = !bookGettingSigned && (currPage < bookTotalPages - 1 || bookIsUnsigned);
        buttonPreviousPage.visible = !bookGettingSigned && currPage > 0;
        buttonDone.visible = !bookIsUnsigned || !bookGettingSigned;

        if (bookIsUnsigned)
        {
            buttonSign.visible = !bookGettingSigned;
            buttonCancel.visible = bookGettingSigned;
            buttonFinalize.visible = bookGettingSigned;
            buttonFinalize.enabled = !bookTitle.trim().isEmpty();
        }
    }

    private void sendBookToServer(boolean publish)
    {
        if (bookIsUnsigned && bookIsModified)
        {
            if (bookPages != null)
            {
                while (bookPages.tagCount() > 1)
                {
                    String s = bookPages.getStringTagAt(bookPages.tagCount() - 1);

                    if (!s.isEmpty())
                    {
                        break;
                    }

                    bookPages.removeTag(bookPages.tagCount() - 1);
                }

                if (bookObj.hasTagCompound())
                {
                    NBTTagCompound nbttagcompound = bookObj.getTagCompound();
                    nbttagcompound.setTag("pages", bookPages);
                }
                else
                {
                    bookObj.setTagInfo("pages", bookPages);
                }

                String s1 = "MC|BEdit";

                if (publish)
                {
                    s1 = "MC|BSign";
                    bookObj.setTagInfo("author", new NBTTagString(editingPlayer.getName()));
                    bookObj.setTagInfo("title", new NBTTagString(bookTitle.trim()));
                }

                PacketBuffer packetbuffer = new PacketBuffer(Unpooled.buffer());
                packetbuffer.writeItemStackToBuffer(bookObj);
                minecraft.getConnection().sendPacket(new CPacketCustomPayload(s1, packetbuffer));
            }
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
                Minecraft.openScreen(null);
                sendBookToServer(false);
            }
            else if (button.id == 3 && bookIsUnsigned)
            {
                bookGettingSigned = true;
            }
            else if (button.id == 1)
            {
                if (currPage < bookTotalPages - 1)
                {
                    ++currPage;
                }
                else if (bookIsUnsigned)
                {
                    addNewPage();

                    if (currPage < bookTotalPages - 1)
                    {
                        ++currPage;
                    }
                }
            }
            else if (button.id == 2)
            {
                if (currPage > 0)
                {
                    --currPage;
                }
            }
            else if (button.id == 5 && bookGettingSigned)
            {
                sendBookToServer(true);
                Minecraft.openScreen(null);
            }
            else if (button.id == 4 && bookGettingSigned)
            {
                bookGettingSigned = false;
            }

            updateButtons();
        }
    }

    private void addNewPage()
    {
        if (bookPages != null && bookPages.tagCount() < 50)
        {
            bookPages.appendTag(new NBTTagString(""));
            ++bookTotalPages;
            bookIsModified = true;
        }
    }

    /**
     * Fired when a key is typed (except F11 which toggles full screen). This is the equivalent of
     * KeyListener.keyTyped(KeyEvent e). Args : character (character on the key), keyCode (lwjgl Keyboard key code)
     */
    public void keyPressed(int key, char c)
    {
        super.keyPressed(key, c);

        if (bookIsUnsigned)
        {
            if (bookGettingSigned)
            {
                keyTypedInTitle(c, key);
            }
            else
            {
                keyTypedInBook(c, key);
            }
        }
    }

    /**
     * Processes keystrokes when editing the text of a book
     */
    private void keyTypedInBook(char typedChar, int keyCode)
    {
        if (Screen.isPaste(keyCode))
        {
            pageInsertIntoCurrent(OS.getClipboard());
        }
        else
        {
            switch (keyCode)
            {
                case 14:
                    String s = pageGetCurrent();

                    if (!s.isEmpty())
                    {
                        pageSetCurrent(s.substring(0, s.length() - 1));
                    }

                    return;

                case 28:
                case 156:
                    pageInsertIntoCurrent("\n");
                    return;

                default:
                    if (ChatAllowedCharacters.isAllowedCharacter(typedChar))
                    {
                        pageInsertIntoCurrent(Character.toString(typedChar));
                    }
            }
        }
    }

    /**
     * Processes keystrokes when editing the title of a book
     */
    private void keyTypedInTitle(char p_146460_1_, int p_146460_2_)
    {
        switch (p_146460_2_)
        {
            case 14:
                if (!bookTitle.isEmpty())
                {
                    bookTitle = bookTitle.substring(0, bookTitle.length() - 1);
                    updateButtons();
                }

                return;

            case 28:
            case 156:
                if (!bookTitle.isEmpty())
                {
                    sendBookToServer(true);
                    Minecraft.openScreen(null);
                }

                return;

            default:
                if (bookTitle.length() < 16 && ChatAllowedCharacters.isAllowedCharacter(p_146460_1_))
                {
                    bookTitle = bookTitle + p_146460_1_;
                    updateButtons();
                    bookIsModified = true;
                }
        }
    }

    /**
     * Returns the entire text of the current page as determined by currPage
     */
    private String pageGetCurrent()
    {
        return bookPages != null && currPage >= 0 && currPage < bookPages.tagCount() ? bookPages.getStringTagAt(currPage) : "";
    }

    /**
     * Sets the text of the current page as determined by currPage
     */
    private void pageSetCurrent(String p_146457_1_)
    {
        if (bookPages != null && currPage >= 0 && currPage < bookPages.tagCount())
        {
            bookPages.set(currPage, new NBTTagString(p_146457_1_));
            bookIsModified = true;
        }
    }

    /**
     * Processes any text getting inserted into the current page, enforcing the page size limit
     */
    private void pageInsertIntoCurrent(String p_146459_1_)
    {
        String s = pageGetCurrent();
        String s1 = s + p_146459_1_;
        int i = font.splitStringWidth(s1 + "" + Formatting.BLACK + "_", 118);

        if (i <= 128 && s1.length() < 256)
        {
            pageSetCurrent(s1);
        }
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void draw(int mouseX, int mouseY, float partialTick)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getTextureManager().bindTexture(BOOK_GUI_TEXTURES);
        int i = (width - 192) / 2;
        int j = 2;
        drawTexturedModalRect(i, 2, 0, 0, 192, 192);

        if (bookGettingSigned)
        {
            String s = bookTitle;

            if (bookIsUnsigned)
            {
                if (updateCount / 6 % 2 == 0)
                {
                    s = s + "" + Formatting.BLACK + "_";
                }
                else
                {
                    s = s + "" + Formatting.GRAY + "_";
                }
            }

            String s1 = I18n.format("book.editTitle");
            int k = font.getStringWidth(s1);
            font.drawString(s1, i + 36 + (116 - k) / 2, 34, 0);
            int l = font.getStringWidth(s);
            font.drawString(s, i + 36 + (116 - l) / 2, 50, 0);
            String s2 = I18n.format("book.byAuthor", editingPlayer.getName());
            int i1 = font.getStringWidth(s2);
            font.drawString(Formatting.DARK_GRAY + s2, i + 36 + (116 - i1) / 2, 60, 0);
            String s3 = I18n.format("book.finalizeWarning");
            font.drawSplitString(s3, i + 36, 82, 116, 0);
        }
        else
        {
            String s4 = I18n.format("book.pageIndicator", currPage + 1, bookTotalPages);
            String s5 = "";

            if (bookPages != null && currPage >= 0 && currPage < bookPages.tagCount())
            {
                s5 = bookPages.getStringTagAt(currPage);
            }

            if (bookIsUnsigned)
            {
                if (font.getBidiFlag())
                {
                    s5 = s5 + "_";
                }
                else if (updateCount / 6 % 2 == 0)
                {
                    s5 = s5 + "" + Formatting.BLACK + "_";
                }
                else
                {
                    s5 = s5 + "" + Formatting.GRAY + "_";
                }
            }
            else if (cachedPage != currPage)
            {
                if (ItemWrittenBook.validBookTagContents(bookObj.getTagCompound()))
                {
                    try
                    {
                        Component itextcomponent = Component.Serializer.jsonToComponent(s5);
                        cachedComponents = itextcomponent != null ? GuiUtilRenderComponents.splitText(itextcomponent, 116, font, true, true) : null;
                    }
                    catch (JsonParseException var13)
                    {
                        cachedComponents = null;
                    }
                }
                else
                {
                    TextComponent textcomponentstring = new TextComponent(Formatting.DARK_RED + "* Invalid book tag *");
                    cachedComponents = Lists.newArrayList(textcomponentstring);
                }

                cachedPage = currPage;
            }

            int j1 = font.getStringWidth(s4);
            font.drawString(s4, i - j1 + 192 - 44, 18, 0);

            if (cachedComponents == null)
            {
                font.drawSplitString(s5, i + 36, 34, 116, 0);
            }
            else
            {
                int k1 = Math.min(128 / font.height, cachedComponents.size());

                for (int l1 = 0; l1 < k1; ++l1)
                {
                    Component itextcomponent2 = cachedComponents.get(l1);
                    font.drawString(itextcomponent2.asString(), i + 36, 34 + l1 * font.height, 0);
                }

                Component itextcomponent1 = getClickedComponentAt(mouseX, mouseY);

                if (itextcomponent1 != null)
                {
                    drawTooltip(itextcomponent1, mouseX, mouseY);
                }
            }
        }

        super.draw(mouseX, mouseY, partialTick);
    }

    /**
     * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
     */
    public void mousePressed(int mouseX, int mouseY, int button)
    {
        if (button == 0)
        {
            Component itextcomponent = getClickedComponentAt(mouseX, mouseY);

            if (itextcomponent != null && handleComponentClick(itextcomponent))
            {
                return;
            }
        }

        super.mousePressed(mouseX, mouseY, button);
    }

    /**
     * Executes the click event specified by the given chat component
     */
    public boolean handleComponentClick(Component component)
    {
        ClickEvent clickevent = component.getStyle().getClickEvent();

        if (clickevent == null)
        {
            return false;
        }
        else if (clickevent.getAction() == ClickEvent.Action.CHANGE_PAGE)
        {
            String s = clickevent.getValue();

            try
            {
                int i = Integer.parseInt(s) - 1;

                if (i >= 0 && i < bookTotalPages && i != currPage)
                {
                    currPage = i;
                    updateButtons();
                    return true;
                }
            }
            catch (Throwable var5)
            {
            }

            return false;
        }
        else
        {
            boolean flag = super.handleComponentClick(component);

            if (flag && clickevent.getAction() == ClickEvent.Action.RUN_COMMAND)
            {
                Minecraft.openScreen(null);
            }

            return flag;
        }
    }

    @Nullable
    public Component getClickedComponentAt(int p_175385_1_, int p_175385_2_)
    {
        if (cachedComponents == null)
        {
            return null;
        }
        else
        {
            int i = p_175385_1_ - (width - 192) / 2 - 36;
            int j = p_175385_2_ - 2 - 16 - 16;

            if (i >= 0 && j >= 0)
            {
                int k = Math.min(128 / font.height, cachedComponents.size());

                if (i <= 116 && j < Minecraft.font.height * k + k)
                {
                    int l = j / Minecraft.font.height;

                    if (l >= 0 && l < cachedComponents.size())
                    {
                        Component itextcomponent = cachedComponents.get(l);
                        int i1 = 0;

                        for (Component itextcomponent1 : itextcomponent)
                        {
                            if (itextcomponent1 instanceof TextComponent)
                            {
                                i1 += Minecraft.font.getStringWidth(((TextComponent)itextcomponent1).getText());

                                if (i1 > i)
                                {
                                    return itextcomponent1;
                                }
                            }
                        }
                    }

                    return null;
                }
                else
                {
                    return null;
                }
            }
            else
            {
                return null;
            }
        }
    }

    static class NextPageButton extends ButtonWidget
    {
        private final boolean isForward;

        public NextPageButton(int p_i46316_1_, int p_i46316_2_, int p_i46316_3_, boolean p_i46316_4_)
        {
            super(p_i46316_1_, p_i46316_2_, p_i46316_3_, 23, 13, "");
            isForward = p_i46316_4_;
        }

        public void draw(Minecraft p_191745_1_, int p_191745_2_, int p_191745_3_, float partialTick)
        {
            if (visible)
            {
                boolean flag = p_191745_2_ >= x && p_191745_3_ >= y && p_191745_2_ < x + width && p_191745_3_ < y + height;
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                Minecraft.getTextureManager().bindTexture(BOOK_GUI_TEXTURES);
                int i = 0;
                int j = 192;

                if (flag)
                {
                    i += 23;
                }

                if (!isForward)
                {
                    j += 13;
                }

                drawTexturedModalRect(x, y, i, j, 23, 13);
            }
        }
    }
}
