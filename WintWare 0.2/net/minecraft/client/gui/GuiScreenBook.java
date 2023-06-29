package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import com.google.gson.JsonParseException;
import io.netty.buffer.Unpooled;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
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
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;

public class GuiScreenBook extends GuiScreen {
   private static final Logger LOGGER = LogManager.getLogger();
   private static final ResourceLocation BOOK_GUI_TEXTURES = new ResourceLocation("textures/gui/book.png");
   private final EntityPlayer editingPlayer;
   private final ItemStack bookObj;
   private final boolean bookIsUnsigned;
   private boolean bookIsModified;
   private boolean bookGettingSigned;
   private int updateCount;
   private final int bookImageWidth = 192;
   private final int bookImageHeight = 192;
   private int bookTotalPages = 1;
   private int currPage;
   private NBTTagList bookPages;
   private String bookTitle = "";
   private List<ITextComponent> cachedComponents;
   private int cachedPage = -1;
   private GuiScreenBook.NextPageButton buttonNextPage;
   private GuiScreenBook.NextPageButton buttonPreviousPage;
   private GuiButton buttonDone;
   private GuiButton buttonSign;
   private GuiButton buttonFinalize;
   private GuiButton buttonCancel;

   public GuiScreenBook(EntityPlayer player, ItemStack book, boolean isUnsigned) {
      this.editingPlayer = player;
      this.bookObj = book;
      this.bookIsUnsigned = isUnsigned;
      if (book.hasTagCompound()) {
         NBTTagCompound nbttagcompound = book.getTagCompound();
         this.bookPages = nbttagcompound.getTagList("pages", 8).copy();
         this.bookTotalPages = this.bookPages.tagCount();
         if (this.bookTotalPages < 1) {
            this.bookTotalPages = 1;
         }
      }

      if (this.bookPages == null && isUnsigned) {
         this.bookPages = new NBTTagList();
         this.bookPages.appendTag(new NBTTagString(""));
         this.bookTotalPages = 1;
      }

   }

   public void updateScreen() {
      super.updateScreen();
      ++this.updateCount;
   }

   public void initGui() {
      this.buttonList.clear();
      Keyboard.enableRepeatEvents(true);
      if (this.bookIsUnsigned) {
         this.buttonSign = this.addButton(new GuiButton(3, width / 2 - 100, 196, 98, 20, I18n.format("book.signButton")));
         this.buttonDone = this.addButton(new GuiButton(0, width / 2 + 2, 196, 98, 20, I18n.format("gui.done")));
         this.buttonFinalize = this.addButton(new GuiButton(5, width / 2 - 100, 196, 98, 20, I18n.format("book.finalizeButton")));
         this.buttonCancel = this.addButton(new GuiButton(4, width / 2 + 2, 196, 98, 20, I18n.format("gui.cancel")));
      } else {
         this.buttonDone = this.addButton(new GuiButton(0, width / 2 - 100, 196, 200, 20, I18n.format("gui.done")));
      }

      int i = (width - 192) / 2;
      int j = true;
      this.buttonNextPage = (GuiScreenBook.NextPageButton)this.addButton(new GuiScreenBook.NextPageButton(1, i + 120, 156, true));
      this.buttonPreviousPage = (GuiScreenBook.NextPageButton)this.addButton(new GuiScreenBook.NextPageButton(2, i + 38, 156, false));
      this.updateButtons();
   }

   public void onGuiClosed() {
      Keyboard.enableRepeatEvents(false);
   }

   private void updateButtons() {
      this.buttonNextPage.visible = !this.bookGettingSigned && (this.currPage < this.bookTotalPages - 1 || this.bookIsUnsigned);
      this.buttonPreviousPage.visible = !this.bookGettingSigned && this.currPage > 0;
      this.buttonDone.visible = !this.bookIsUnsigned || !this.bookGettingSigned;
      if (this.bookIsUnsigned) {
         this.buttonSign.visible = !this.bookGettingSigned;
         this.buttonCancel.visible = this.bookGettingSigned;
         this.buttonFinalize.visible = this.bookGettingSigned;
         this.buttonFinalize.enabled = !this.bookTitle.trim().isEmpty();
      }

   }

   private void sendBookToServer(boolean publish) throws IOException {
      if (this.bookIsUnsigned && this.bookIsModified && this.bookPages != null) {
         String s1;
         while(this.bookPages.tagCount() > 1) {
            s1 = this.bookPages.getStringTagAt(this.bookPages.tagCount() - 1);
            if (!s1.isEmpty()) {
               break;
            }

            this.bookPages.removeTag(this.bookPages.tagCount() - 1);
         }

         if (this.bookObj.hasTagCompound()) {
            NBTTagCompound nbttagcompound = this.bookObj.getTagCompound();
            nbttagcompound.setTag("pages", this.bookPages);
         } else {
            this.bookObj.setTagInfo("pages", this.bookPages);
         }

         s1 = "MC|BEdit";
         if (publish) {
            s1 = "MC|BSign";
            this.bookObj.setTagInfo("author", new NBTTagString(this.editingPlayer.getName()));
            this.bookObj.setTagInfo("title", new NBTTagString(this.bookTitle.trim()));
         }

         PacketBuffer packetbuffer = new PacketBuffer(Unpooled.buffer());
         packetbuffer.writeItemStackToBuffer(this.bookObj);
         this.mc.getConnection().sendPacket(new CPacketCustomPayload(s1, packetbuffer));
      }

   }

   protected void actionPerformed(GuiButton button) throws IOException {
      if (button.enabled) {
         if (button.id == 0) {
            this.mc.displayGuiScreen((GuiScreen)null);
            this.sendBookToServer(false);
         } else if (button.id == 3 && this.bookIsUnsigned) {
            this.bookGettingSigned = true;
         } else if (button.id == 1) {
            if (this.currPage < this.bookTotalPages - 1) {
               ++this.currPage;
            } else if (this.bookIsUnsigned) {
               this.addNewPage();
               if (this.currPage < this.bookTotalPages - 1) {
                  ++this.currPage;
               }
            }
         } else if (button.id == 2) {
            if (this.currPage > 0) {
               --this.currPage;
            }
         } else if (button.id == 5 && this.bookGettingSigned) {
            this.sendBookToServer(true);
            this.mc.displayGuiScreen((GuiScreen)null);
         } else if (button.id == 4 && this.bookGettingSigned) {
            this.bookGettingSigned = false;
         }

         this.updateButtons();
      }

   }

   private void addNewPage() {
      if (this.bookPages != null && this.bookPages.tagCount() < 50) {
         this.bookPages.appendTag(new NBTTagString(""));
         ++this.bookTotalPages;
         this.bookIsModified = true;
      }

   }

   protected void keyTyped(char typedChar, int keyCode) throws IOException {
      super.keyTyped(typedChar, keyCode);
      if (this.bookIsUnsigned) {
         if (this.bookGettingSigned) {
            this.keyTypedInTitle(typedChar, keyCode);
         } else {
            this.keyTypedInBook(typedChar, keyCode);
         }
      }

   }

   private void keyTypedInBook(char typedChar, int keyCode) {
      if (GuiScreen.isKeyComboCtrlV(keyCode)) {
         this.pageInsertIntoCurrent(GuiScreen.getClipboardString());
      } else {
         switch(keyCode) {
         case 14:
            String s = this.pageGetCurrent();
            if (!s.isEmpty()) {
               this.pageSetCurrent(s.substring(0, s.length() - 1));
            }

            return;
         case 28:
         case 156:
            this.pageInsertIntoCurrent("\n");
            return;
         default:
            if (ChatAllowedCharacters.isAllowedCharacter(typedChar)) {
               this.pageInsertIntoCurrent(Character.toString(typedChar));
            }
         }
      }

   }

   private void keyTypedInTitle(char p_146460_1_, int p_146460_2_) throws IOException {
      switch(p_146460_2_) {
      case 14:
         if (!this.bookTitle.isEmpty()) {
            this.bookTitle = this.bookTitle.substring(0, this.bookTitle.length() - 1);
            this.updateButtons();
         }

         return;
      case 28:
      case 156:
         if (!this.bookTitle.isEmpty()) {
            this.sendBookToServer(true);
            this.mc.displayGuiScreen((GuiScreen)null);
         }

         return;
      default:
         if (this.bookTitle.length() < 16 && ChatAllowedCharacters.isAllowedCharacter(p_146460_1_)) {
            this.bookTitle = this.bookTitle + Character.toString(p_146460_1_);
            this.updateButtons();
            this.bookIsModified = true;
         }

      }
   }

   private String pageGetCurrent() {
      return this.bookPages != null && this.currPage >= 0 && this.currPage < this.bookPages.tagCount() ? this.bookPages.getStringTagAt(this.currPage) : "";
   }

   private void pageSetCurrent(String p_146457_1_) {
      if (this.bookPages != null && this.currPage >= 0 && this.currPage < this.bookPages.tagCount()) {
         this.bookPages.set(this.currPage, new NBTTagString(p_146457_1_));
         this.bookIsModified = true;
      }

   }

   private void pageInsertIntoCurrent(String p_146459_1_) {
      String s = this.pageGetCurrent();
      String s1 = s + p_146459_1_;
      int i = this.fontRendererObj.splitStringWidth(s1 + "" + TextFormatting.BLACK + "_", 118);
      if (i <= 128 && s1.length() < 256) {
         this.pageSetCurrent(s1);
      }

   }

   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      this.mc.getTextureManager().bindTexture(BOOK_GUI_TEXTURES);
      int i = (width - 192) / 2;
      int j = true;
      this.drawTexturedModalRect(i, 2, 0, 0, 192, 192);
      String s4;
      String s5;
      int j1;
      int k1;
      if (this.bookGettingSigned) {
         s4 = this.bookTitle;
         if (this.bookIsUnsigned) {
            if (this.updateCount / 6 % 2 == 0) {
               s4 = s4 + "" + TextFormatting.BLACK + "_";
            } else {
               s4 = s4 + "" + TextFormatting.GRAY + "_";
            }
         }

         s5 = I18n.format("book.editTitle");
         j1 = this.fontRendererObj.getStringWidth(s5);
         this.fontRendererObj.drawString(s5, (float)(i + 36 + (116 - j1) / 2), 34.0F, 0);
         k1 = this.fontRendererObj.getStringWidth(s4);
         this.fontRendererObj.drawString(s4, (float)(i + 36 + (116 - k1) / 2), 50.0F, 0);
         String s2 = I18n.format("book.byAuthor", this.editingPlayer.getName());
         int i1 = this.fontRendererObj.getStringWidth(s2);
         this.fontRendererObj.drawString(TextFormatting.DARK_GRAY + s2, (float)(i + 36 + (116 - i1) / 2), 60.0F, 0);
         String s3 = I18n.format("book.finalizeWarning");
         this.fontRendererObj.drawSplitString(s3, i + 36, 82, 116, 0);
      } else {
         s4 = I18n.format("book.pageIndicator", this.currPage + 1, this.bookTotalPages);
         s5 = "";
         if (this.bookPages != null && this.currPage >= 0 && this.currPage < this.bookPages.tagCount()) {
            s5 = this.bookPages.getStringTagAt(this.currPage);
         }

         if (this.bookIsUnsigned) {
            if (this.fontRendererObj.getBidiFlag()) {
               s5 = s5 + "_";
            } else if (this.updateCount / 6 % 2 == 0) {
               s5 = s5 + "" + TextFormatting.BLACK + "_";
            } else {
               s5 = s5 + "" + TextFormatting.GRAY + "_";
            }
         } else if (this.cachedPage != this.currPage) {
            if (ItemWrittenBook.validBookTagContents(this.bookObj.getTagCompound())) {
               try {
                  ITextComponent itextcomponent = ITextComponent.Serializer.jsonToComponent(s5);
                  this.cachedComponents = itextcomponent != null ? GuiUtilRenderComponents.splitText(itextcomponent, 116, this.fontRendererObj, true, true) : null;
               } catch (JsonParseException var13) {
                  this.cachedComponents = null;
               }
            } else {
               TextComponentString textcomponentstring = new TextComponentString(TextFormatting.DARK_RED + "* Invalid book tag *");
               this.cachedComponents = Lists.newArrayList(textcomponentstring);
            }

            this.cachedPage = this.currPage;
         }

         j1 = this.fontRendererObj.getStringWidth(s4);
         this.fontRendererObj.drawString(s4, (float)(i - j1 + 192 - 44), 18.0F, 0);
         if (this.cachedComponents == null) {
            this.fontRendererObj.drawSplitString(s5, i + 36, 34, 116, 0);
         } else {
            k1 = Math.min(128 / this.fontRendererObj.FONT_HEIGHT, this.cachedComponents.size());

            for(int l1 = 0; l1 < k1; ++l1) {
               ITextComponent itextcomponent2 = (ITextComponent)this.cachedComponents.get(l1);
               this.fontRendererObj.drawString(itextcomponent2.getUnformattedText(), (float)(i + 36), (float)(34 + l1 * this.fontRendererObj.FONT_HEIGHT), 0);
            }

            ITextComponent itextcomponent1 = this.getClickedComponentAt(mouseX, mouseY);
            if (itextcomponent1 != null) {
               this.handleComponentHover(itextcomponent1, mouseX, mouseY);
            }
         }
      }

      super.drawScreen(mouseX, mouseY, partialTicks);
   }

   protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
      if (mouseButton == 0) {
         ITextComponent itextcomponent = this.getClickedComponentAt(mouseX, mouseY);
         if (itextcomponent != null && this.handleComponentClick(itextcomponent)) {
            return;
         }
      }

      super.mouseClicked(mouseX, mouseY, mouseButton);
   }

   public boolean handleComponentClick(ITextComponent component) {
      ClickEvent clickevent = component.getStyle().getClickEvent();
      if (clickevent == null) {
         return false;
      } else if (clickevent.getAction() == ClickEvent.Action.CHANGE_PAGE) {
         String s = clickevent.getValue();

         try {
            int i = Integer.parseInt(s) - 1;
            if (i >= 0 && i < this.bookTotalPages && i != this.currPage) {
               this.currPage = i;
               this.updateButtons();
               return true;
            }
         } catch (Throwable var5) {
         }

         return false;
      } else {
         boolean flag = super.handleComponentClick(component);
         if (flag && clickevent.getAction() == ClickEvent.Action.RUN_COMMAND) {
            this.mc.displayGuiScreen((GuiScreen)null);
         }

         return flag;
      }
   }

   @Nullable
   public ITextComponent getClickedComponentAt(int p_175385_1_, int p_175385_2_) {
      if (this.cachedComponents == null) {
         return null;
      } else {
         int i = p_175385_1_ - (width - 192) / 2 - 36;
         int j = p_175385_2_ - 2 - 16 - 16;
         if (i >= 0 && j >= 0) {
            int k = Math.min(128 / this.fontRendererObj.FONT_HEIGHT, this.cachedComponents.size());
            if (i <= 116 && j < this.mc.fontRendererObj.FONT_HEIGHT * k + k) {
               int l = j / this.mc.fontRendererObj.FONT_HEIGHT;
               if (l >= 0 && l < this.cachedComponents.size()) {
                  ITextComponent itextcomponent = (ITextComponent)this.cachedComponents.get(l);
                  int i1 = 0;
                  Iterator var9 = itextcomponent.iterator();

                  while(var9.hasNext()) {
                     ITextComponent itextcomponent1 = (ITextComponent)var9.next();
                     if (itextcomponent1 instanceof TextComponentString) {
                        i1 += this.mc.fontRendererObj.getStringWidth(((TextComponentString)itextcomponent1).getText());
                        if (i1 > i) {
                           return itextcomponent1;
                        }
                     }
                  }
               }

               return null;
            } else {
               return null;
            }
         } else {
            return null;
         }
      }
   }

   static class NextPageButton extends GuiButton {
      private final boolean isForward;

      public NextPageButton(int p_i46316_1_, int p_i46316_2_, int p_i46316_3_, boolean p_i46316_4_) {
         super(p_i46316_1_, p_i46316_2_, p_i46316_3_, 23, 13, "");
         this.isForward = p_i46316_4_;
      }

      public void func_191745_a(Minecraft p_191745_1_, int p_191745_2_, int p_191745_3_, float p_191745_4_) {
         if (this.visible) {
            boolean flag = p_191745_2_ >= this.xPosition && p_191745_3_ >= this.yPosition && p_191745_2_ < this.xPosition + this.width && p_191745_3_ < this.yPosition + this.height;
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            p_191745_1_.getTextureManager().bindTexture(GuiScreenBook.BOOK_GUI_TEXTURES);
            int i = 0;
            int j = 192;
            if (flag) {
               i += 23;
            }

            if (!this.isForward) {
               j += 13;
            }

            this.drawTexturedModalRect(this.xPosition, this.yPosition, i, j, 23, 13);
         }

      }
   }
}
