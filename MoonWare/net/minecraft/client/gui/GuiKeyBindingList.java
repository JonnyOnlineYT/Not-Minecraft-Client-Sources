package net.minecraft.client.gui;

import java.util.Arrays;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.text.Formatting;

public class GuiKeyBindingList extends GuiListExtended
{
    private final GuiControls controlsScreen;
    private final Minecraft mc;
    private final GuiListExtended.IGuiListEntry[] listEntries;
    private int maxListLabelWidth;

    public GuiKeyBindingList(GuiControls controls, Minecraft mcIn)
    {
        super(mcIn, controls.width + 45, controls.height, 63, controls.height - 32, 20);
        controlsScreen = controls;
        mc = mcIn;
        KeyBinding[] akeybinding = Minecraft.gameSettings.keyBindings.clone();
        listEntries = new GuiListExtended.IGuiListEntry[akeybinding.length + KeyBinding.getKeybinds().size()];
        Arrays.sort(akeybinding);
        int i = 0;
        String s = null;

        for (KeyBinding keybinding : akeybinding)
        {
            String s1 = keybinding.getKeyCategory();

            if (!s1.equals(s))
            {
                s = s1;
                listEntries[i++] = new GuiKeyBindingList.CategoryEntry(s1);
            }

            int j = Minecraft.font.getStringWidth(I18n.format(keybinding.getKeyDescription()));

            if (j > maxListLabelWidth)
            {
                maxListLabelWidth = j;
            }

            listEntries[i++] = new GuiKeyBindingList.KeyEntry(keybinding);
        }
    }

    protected int getSize()
    {
        return listEntries.length;
    }

    /**
     * Gets the IGuiListEntry object for the given index
     */
    public GuiListExtended.IGuiListEntry getListEntry(int index)
    {
        return listEntries[index];
    }

    protected int getScrollBarX()
    {
        return super.getScrollBarX() + 15;
    }

    /**
     * Gets the width of the list
     */
    public int getListWidth()
    {
        return super.getListWidth() + 32;
    }

    public class CategoryEntry implements GuiListExtended.IGuiListEntry
    {
        private final String labelText;
        private final int labelWidth;

        public CategoryEntry(String name)
        {
            labelText = I18n.format(name);
            labelWidth = Minecraft.font.getStringWidth(labelText);
        }

        public void draw(int p_192634_1_, int p_192634_2_, int p_192634_3_, int p_192634_4_, int p_192634_5_, int p_192634_6_, int p_192634_7_, boolean p_192634_8_, float p_192634_9_)
        {
            Minecraft.font.drawString(labelText, Minecraft.screen.width / 2 - labelWidth / 2, p_192634_3_ + p_192634_5_ - Minecraft.font.height - 1, 16777215);
        }

        public boolean mousePressed(int slotIndex, int mouseX, int mouseY, int mouseEvent, int relativeX, int relativeY)
        {
            return false;
        }

        public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY)
        {
        }

        public void func_192633_a(int p_192633_1_, int p_192633_2_, int p_192633_3_, float p_192633_4_)
        {
        }
    }

    public class KeyEntry implements GuiListExtended.IGuiListEntry
    {
        private final KeyBinding keybinding;
        private final String keyDesc;
        private final ButtonWidget btnChangeKeyBinding;
        private final ButtonWidget btnReset;

        private KeyEntry(KeyBinding name)
        {
            keybinding = name;
            keyDesc = I18n.format(name.getKeyDescription());
            btnChangeKeyBinding = new ButtonWidget(0, 0, 0, 75, 20, I18n.format(name.getKeyDescription()));
            btnReset = new ButtonWidget(0, 0, 0, 50, 20, I18n.format("controls.reset"));
        }

        public void draw(int p_192634_1_, int p_192634_2_, int p_192634_3_, int p_192634_4_, int p_192634_5_, int p_192634_6_, int p_192634_7_, boolean p_192634_8_, float p_192634_9_)
        {
            boolean flag = controlsScreen.buttonId == keybinding;
            Minecraft.font.drawString(keyDesc, p_192634_2_ + 90 - maxListLabelWidth, p_192634_3_ + p_192634_5_ / 2 - Minecraft.font.height / 2, 16777215);
            btnReset.x = p_192634_2_ + 190;
            btnReset.y = p_192634_3_;
            btnReset.enabled = keybinding.getKeyCode() != keybinding.getKeyCodeDefault();
            btnReset.draw(mc, p_192634_6_, p_192634_7_, p_192634_9_);
            btnChangeKeyBinding.x = p_192634_2_ + 105;
            btnChangeKeyBinding.y = p_192634_3_;
            btnChangeKeyBinding.text = GameSettings.getKeyDisplayString(keybinding.getKeyCode());
            boolean flag1 = false;

            if (keybinding.getKeyCode() != 0)
            {
                for (KeyBinding keybinding : Minecraft.gameSettings.keyBindings)
                {
                    if (keybinding != this.keybinding && keybinding.getKeyCode() == this.keybinding.getKeyCode())
                    {
                        flag1 = true;
                        break;
                    }
                }
            }

            if (flag)
            {
                btnChangeKeyBinding.text = Formatting.WHITE + "> " + Formatting.YELLOW + btnChangeKeyBinding.text + Formatting.WHITE + " <";
            }
            else if (flag1)
            {
                btnChangeKeyBinding.text = Formatting.RED + btnChangeKeyBinding.text;
            }

            btnChangeKeyBinding.draw(mc, p_192634_6_, p_192634_7_, p_192634_9_);
        }

        public boolean mousePressed(int slotIndex, int mouseX, int mouseY, int mouseEvent, int relativeX, int relativeY)
        {
            if (btnChangeKeyBinding.mousePressed(mc, mouseX, mouseY))
            {
                controlsScreen.buttonId = keybinding;
                return true;
            }
            else if (btnReset.mousePressed(mc, mouseX, mouseY))
            {
                Minecraft.gameSettings.setOptionKeyBinding(keybinding, keybinding.getKeyCodeDefault());
                KeyBinding.resetKeyBindingArrayAndHash();
                return true;
            }
            else
            {
                return false;
            }
        }

        public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY)
        {
            btnChangeKeyBinding.mouseReleased(x, y);
            btnReset.mouseReleased(x, y);
        }

        public void func_192633_a(int p_192633_1_, int p_192633_2_, int p_192633_3_, float p_192633_4_)
        {
        }
    }
}
