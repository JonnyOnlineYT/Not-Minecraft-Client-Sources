package volcano.summer.base;

import java.awt.Color;
import java.io.File;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.thealtening.AltService;
import com.thealtening.utilities.SSLVerification;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ChatComponentText;
import volcano.summer.base.manager.command.CommandManager;
import volcano.summer.base.manager.config.ConfigManager;
import volcano.summer.base.manager.event.EventManager;
import volcano.summer.base.manager.file.FileManager;
import volcano.summer.base.manager.friend.FriendManager;
import volcano.summer.base.manager.module.ModuleManager;
import volcano.summer.client.value.ValueManager;
import volcano.summer.screen.altmanager.AltManager;
import volcano.summer.screen.clickgui.one2.ClickGui;
import volcano.summer.screen.tabgui.OriginalTabGui;

public class Summer {

	public static String name = "Summer";
	public static String version = "13";
	public static Minecraft mc = Minecraft.getMinecraft();
	public static FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;
	public static ModuleManager moduleManager = new ModuleManager();
	public static EventManager eventManager = new EventManager();
	public static FileManager fileManager = new FileManager();
	public static ValueManager valueManager = new ValueManager();
	public static ConfigManager configManager = new ConfigManager();
	public static CommandManager commandManager = new CommandManager();
	public static AltManager altManager = new AltManager();
	public static FriendManager friendManager = new FriendManager();
	private static ClickGui gui = new ClickGui();
	private static File directory = new File(Minecraft.getMinecraft().mcDataDir, name);
	public static Color color = Color.YELLOW;
	private static OriginalTabGui tabGui;

	public static AltService altService;
	private static SSLVerification sslVerification = new SSLVerification();

	public static void startSummer() {
		altService = new AltService();
		moduleManager.addModules();
		fileManager.loadFiles();
		valueManager.organizeValues();
		commandManager.loadCommands();
		configManager.loadConfigs();
		sslVerification.verify();
		altService = new AltService();
		main();
	}

	public static void main() {
		DiscordRPC lib = DiscordRPC.INSTANCE;
		String applicationId = "430278145715863552";
		String steamId = "";
		DiscordEventHandlers handlers = new DiscordEventHandlers();
		handlers.ready = () -> System.out.println("Ready!");
		lib.Discord_Initialize(applicationId, handlers, true, steamId);
		DiscordRichPresence presence = new DiscordRichPresence();
		presence.startTimestamp = System.currentTimeMillis() / 1000;
		presence.details = "Using Summer b13";
		presence.largeImageKey = "summer_icon";
		presence.state = "By Volcano & Matt";
		lib.Discord_UpdatePresence(presence);
		// in a worker thread
		new Thread(() -> {
			while (!Thread.currentThread().isInterrupted()) {
				lib.Discord_RunCallbacks();
				try {
					Thread.sleep(2000);
				} catch (InterruptedException ignored) {
				}
			}
		}, "RPC-Callback-Handler").start();
	}

	public static void tellPlayer(String message) {
		mc.ingameGUI.getChatGUI()
				.printChatMessage(new ChatComponentText(ChatFormatting.GRAY + "> " + ChatFormatting.GOLD + message));
	}

	public static void tellPlayerHelpCommand(String message) {
		mc.ingameGUI.getChatGUI().printChatMessage(new ChatComponentText(ChatFormatting.WHITE + message));
	}

	public static File getDir() {
		return directory;
	}

	public static Color getColor() {
		return color;
	}

	public static ClickGui getClickGui() {
		if (gui == null) {
			gui = new ClickGui();
		}
		return gui;
	}

	public static OriginalTabGui getTabGui() {
		if (tabGui == null) {
			tabGui = new OriginalTabGui();
		}
		return tabGui;
	}

}

// TODO: isInLava : func_180799_ab