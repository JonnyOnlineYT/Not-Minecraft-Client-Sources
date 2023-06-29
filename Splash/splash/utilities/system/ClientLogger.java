package splash.utilities.system;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import splash.Splash;

/**
 * Author: Ice
 * Created: 00:37, 30-May-20
 * Project: Client
 */
public class ClientLogger {

    public static void printToConsole(String message) {
        System.out.println("[INFO] " + "[" + Splash.getInstance().getClientName() + "]" + " " + message);
    }

    public static void printErrorToConsole(String message) {
        System.out.println("[ERROR] " + "[" + Splash.getInstance().getClientName() + "]" + " " + message);
    }

    public static void printToMinecraft(String msg) {
        (Minecraft.getMinecraft()).thePlayer
                .addChatMessage((IChatComponent)new ChatComponentText(EnumChatFormatting.GRAY + "[" + EnumChatFormatting.AQUA + Splash.getInstance().getClientName() + EnumChatFormatting.GRAY + "]" + EnumChatFormatting.WHITE + " " + msg));
    }
}
