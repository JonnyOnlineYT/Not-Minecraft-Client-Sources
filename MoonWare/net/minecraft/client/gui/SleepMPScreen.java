package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.resources.I18n;
import net.minecraft.network.play.client.CPacketEntityAction;

public class SleepMPScreen extends ChatScreen
{
    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    public void init()
    {
        super.init();
        widgets.add(new ButtonWidget(1, width / 2 - 100, height - 40, I18n.format("multiplayer.stopSleeping")));

    }

    /**
     * Fired when a key is typed (except F11 which toggles full screen). This is the equivalent of
     * KeyListener.keyTyped(KeyEvent e). Args : character (character on the key), keyCode (lwjgl Keyboard key code)
     */
    public void keyPressed(int key, char c)
    {
        if (key == 1)
        {
            wakeFromSleep();
        }
        else if (key != 28 && key != 156)
        {
            super.keyPressed(key, c);
        }
        else
        {
            String s = field.getText().trim();

            if (!s.isEmpty())
            {
                Minecraft.player.sendChatMessage(s);
            }

            field.setText("");
            Minecraft.ingameGUI.getChatGUI().resetScroll();
        }
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    public void actionPerformed(ButtonWidget button)
    {
        if (button.id == 1)
        {
            wakeFromSleep();
        }
        else
        {
            super.actionPerformed(button);
        }
    }

    private void wakeFromSleep()
    {
        NetHandlerPlayClient nethandlerplayclient = Minecraft.player.connection;
        nethandlerplayclient.sendPacket(new CPacketEntityAction(Minecraft.player, CPacketEntityAction.Action.STOP_SLEEPING));
    }
}
