package splash;

import java.awt.Color;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import javax.net.ssl.HttpsURLConnection;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import me.hippo.systems.lwjeb.EventBus;
import me.hippo.systems.lwjeb.annotation.Collect;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import splash.api.command.Command;
import splash.api.module.Module;
import splash.client.events.chat.ChatEvent;
import splash.client.events.key.EventKey;
import splash.client.managers.cfont.CFontRenderer;
import splash.client.managers.command.CommandManager;
import splash.client.managers.config.ConfigManager;
import splash.client.managers.friend.FriendManager;
import splash.client.managers.module.ModuleManager;
import splash.client.managers.notifications.NotificationManager;
import splash.client.managers.value.ValueManager;
import splash.client.modules.visual.GUI;
import splash.client.modules.visual.GUI.Mode;
import splash.client.modules.visual.UI.FontMode;
import splash.gui.ClickGui;
import splash.utilities.system.ClientLogger;

/**
 * Author: Ice Created: 00:19, 30-May-20 Project: Abyss B21 - CBT Edition // Abyss LOLOLOL
 */
public class Splash {

	public static final boolean debug = true;

	/*Instantiating class*/
	public static Splash INSTANCE = new Splash();
	
	/*Client stuffs*/ 
	public long lastFlag;
	public int timesOpened;
	public GAMEMODE gameMode;
	public String CLIENT_NAME;
	public Color CLIENT_COLOR;
	public ClickGui clickUI; 
	public CFontRenderer fontRenderer;
	
	/*Managers*/
	public final FriendManager FRIEND_MANAGER = new FriendManager();
	public final ModuleManager MODULE_MANAGER = new ModuleManager();
	public final ValueManager VALUE_MANAGER = new ValueManager();
	public final CommandManager COMMAND_MANAGER = new CommandManager();
	public final ConfigManager CONFIG_MANAGER = new ConfigManager();
	public final EventBus EVENT_BUS = new EventBus();
	public final NotificationManager NOTIFICATION_MANAGER = new NotificationManager();
	public static String sxAlts = "";

	private java.util.ArrayList<String> strings;
	/*Managers*/
	public final File splashDirectory = new File("C:/splash/");
	public final File splashConfigsDirectory = new File("C:/splash/configs/");
	
	public enum GAMEMODE {
		BEDWARS, SKYWARS, DUELS, PIT, UNSPECIFIED
	}

	public void hookClient() {
		CLIENT_NAME = "Splash";
		CLIENT_COLOR = new Color(255, 80, 80);
		
		getEventBus().build();
		getEventBus().register(this);
		
		if (!splashDirectory.exists()) {
			splashDirectory.mkdir();
		}
		if (!splashConfigsDirectory.exists()) {
			splashConfigsDirectory.mkdir();
		}
	      
		Display.setTitle(getClientName() + " v" + getClientVersion());
		getConfigManager().loadConfigs();
		getModuleManager().getContents().forEach(module -> getValueManager().loadValues(module));
		gameMode = GAMEMODE.UNSPECIFIED;
	}

	public void loadFontRenderer() {
		fontRenderer = new CFontRenderer(new Font("Segoe UI", Font.PLAIN, 18), true, false);
	}

	public void loadFontRenderer(FontMode fontMode) {
		if(fontMode == FontMode.ARIAL) {
			fontRenderer = new CFontRenderer(new Font("Arial", Font.PLAIN, 20), true, false);
		} else {
			fontRenderer = new CFontRenderer(new Font("SFUI Display Regular", Font.PLAIN, 18), true, false);
		}
	}

	public void unhookClient() {
		getEventBus().unregister(this);
		ClientLogger.printToConsole("Stopping Splash...");
	}

	public static Splash getInstance() {
		return INSTANCE;
	}

	public String getClientName() {
		return CLIENT_NAME;
	}
	
	public void setClientName(String clientName) {
		this.CLIENT_NAME = clientName;
	}

	public String getClientVersion() {
		return "1.0.2";
	}

	public File getSplashConfigsDirectory() {
		return splashConfigsDirectory;
	}
	
	public FriendManager getFriendManager() {
		return FRIEND_MANAGER;
	}

	public ModuleManager getModuleManager() {
		return MODULE_MANAGER;
	}

	public ValueManager getValueManager() {
		return VALUE_MANAGER;
	}

	public CommandManager getCommandManager() {
		return COMMAND_MANAGER;
	}

	public ConfigManager getConfigManager() {
		return CONFIG_MANAGER;
	}

	public EventBus getEventBus() {
		return EVENT_BUS;
	}

	public NotificationManager getNotificationManager() {
		return NOTIFICATION_MANAGER;
	}

	public int getClientColor() {
		return CLIENT_COLOR.getRGB();
	}

	public Color getClientColorNORGB() {
		return CLIENT_COLOR;
	}

	public void setClientColor(Color clientColor) {
		this.CLIENT_COLOR = clientColor;
	}

	public CFontRenderer getFontRenderer() {
		return fontRenderer;
	}
	
	public GAMEMODE getGameMode() {
		return gameMode;
	}

	public void setGameMode(GAMEMODE gameMode) {
		this.gameMode = gameMode;
	}

	@Collect
	public void onChat(ChatEvent eventChat) {
		for (Command command : getCommandManager().getContents()) {
			String chatMessage = eventChat.getChatMessage();
			String formattedMessage = chatMessage.replace(".", "");
			String[] regexFormattedMessage = formattedMessage.split(" ");
			if (regexFormattedMessage[0].equalsIgnoreCase(command.getCommandName())) {
				ArrayList<String> list = new ArrayList<>(Arrays.asList(regexFormattedMessage));
				list.remove(command.getCommandName());
				regexFormattedMessage = list.toArray(new String[0]);
				command.executeCommand(regexFormattedMessage);
			}
		}
	}

	@Collect
	public void onKey(EventKey eventKey) {
		boolean newUi = false;
		GUI cgui = (GUI)Splash.getInstance().getModuleManager().getModuleByClass(GUI.class);
		if (eventKey.getPressedKey() == cgui.getModuleMacro()) {
			if (Minecraft.getMinecraft().currentScreen != null) return;
			Splash.getInstance().timesOpened += 1;
			if (clickUI == null) {
				clickUI = new ClickGui();
			}
			Minecraft.getMinecraft().displayGuiScreen(clickUI);
		} else {
			for (Module module : getModuleManager().getContents()) {
				if (module.getModuleMacro() == eventKey.getPressedKey()) {
					module.activateModule();
				}
			}
		}
	}
}
