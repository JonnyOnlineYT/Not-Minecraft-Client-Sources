package volcano.summer.client.modules.misc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import volcano.summer.base.manager.event.Event;
import volcano.summer.base.manager.module.Module;
import volcano.summer.client.events.EventChatSent;
import volcano.summer.client.events.EventRender2D;
import volcano.summer.client.events.EventTick;
import volcano.summer.client.strokes.Key;
import volcano.summer.client.strokes.Location;

public class Keystrokes extends Module {

	private static Minecraft mc;
	public static int location;
	public static List<Key> keys;
	public static int width;
	public static int height;
	private static File locationFile;

	static {
		Keystrokes.mc = Minecraft.getMinecraft();
		Keystrokes.location = 0;
		Keystrokes.keys = new ArrayList<Key>();
		Keystrokes.width = 0;
		Keystrokes.height = 0;
	}

	public Keystrokes() {
		super("Keystrokes", 0, Category.MISC);
	}

	@Override
	public void onEnable() {
		reloadKeys();
		reloadLocation();
	}

	@Override
	public void onDisable() {
	}

	public static void reloadKeys() {
		Keystrokes.keys.clear();
		int i = -1;
		try {
			final File file1 = new File(Keystrokes.mc.mcDataDir + File.separator + "Keystrokes", "keys.cfg");
			if (!file1.exists()) {
				file1.getParentFile().mkdirs();
				file1.createNewFile();
				if (Keystrokes.mc.thePlayer != null) {
					Keystrokes.mc.thePlayer
							.addChatMessage(new ChatComponentText("Created a new empty Key configuration file."));
				}
				final PrintWriter printwriter = new PrintWriter(new FileOutputStream(file1));
				printwriter.println("# Format: KEY:X-OFFSET:Y-OFFSET");
				printwriter.println("# Auf Deusch: TASTE:NACH RECHTS:NACH UNTEN");
				printwriter.println("");
				printwriter.println("W:18:0");
				printwriter.println("A:0:18");
				printwriter.println("S:18:18");
				printwriter.println("D:36:18");
				printwriter.println("LMB:0:38");
				printwriter.println("RMB:28:38");
				printwriter.flush();
				printwriter.close();
			}
			final BufferedReader bufferedreader = new BufferedReader(new FileReader(file1));
			i = 0;
			String s;
			while ((s = bufferedreader.readLine()) != null) {
				++i;
				if (!s.isEmpty() && !s.replace(" ", "").isEmpty() && !s.startsWith("#")) {
					final String[] astring = s.split(":");
					final String s2 = astring[0];
					final int j = Integer.valueOf(astring[1]);
					final int k = Integer.valueOf(astring[2]);
					Keystrokes.keys.add(new Key(s2, j, k));
				}
			}
			i = -1;
			bufferedreader.close();
			for (final Key key : Keystrokes.keys) {
				Keystrokes.width = Math.max(Keystrokes.width, key.getX() + key.getWidth() + 10);
				Keystrokes.height = Math.max(Keystrokes.height, key.getY() + key.getHeight() + 10);
			}
			if (Keystrokes.mc.thePlayer != null) {
				Keystrokes.mc.thePlayer.addChatMessage(new ChatComponentText("Key configuration has been reloaded."));
			}
		} catch (Exception exception) {
			exception.printStackTrace();
			Keystrokes.keys.clear();
			if (Keystrokes.mc.thePlayer != null) {
				Keystrokes.mc.thePlayer.addChatMessage(
						new ChatComponentText("An error occured while loading the Key configuration file:")
								.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
				Keystrokes.mc.thePlayer.addChatMessage(new ChatComponentText(" - " + exception.toString())
						.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
				if (i != -1) {
					Keystrokes.mc.thePlayer.addChatMessage(new ChatComponentText(" - At line #" + i)
							.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
				}
			}
		}
	}

	public static void reloadLocation() {
		Keystrokes.locationFile = new File(Keystrokes.mc.mcDataDir + File.separator + "Keystrokes", "location.txt");
		try {
			if (!Keystrokes.locationFile.exists()) {
				Keystrokes.locationFile.createNewFile();
				final FileWriter filewriter = new FileWriter(Keystrokes.locationFile);
				filewriter.append(String.valueOf(Keystrokes.location) + "\n");
				filewriter.flush();
				filewriter.close();
			}
			final BufferedReader bufferedreader = new BufferedReader(
					new InputStreamReader(new FileInputStream(Keystrokes.locationFile)));
			String s = null;
			if ((s = bufferedreader.readLine()) != null) {
				try {
					Keystrokes.location = Integer.parseInt(s);
				} catch (NumberFormatException var3) {
					if (Keystrokes.mc.thePlayer != null) {
						Keystrokes.mc.thePlayer.addChatMessage(new ChatComponentText(
								"[Keystrokes] The location in the location.txt is not an integer!"));
					}
				}
				bufferedreader.close();
				return;
			}
			if (Keystrokes.mc.thePlayer != null) {
				Keystrokes.mc.thePlayer
						.addChatMessage(new ChatComponentText("[Keystrokes] Couldn't load location from location.txt!")
								.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
			}
			bufferedreader.close();
		} catch (IOException ioexception) {
			ioexception.printStackTrace();
		}
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventRender2D) {
			if (mc.currentScreen == null && !this.mc.gameSettings.showDebugInfo) {
				final ScaledResolution scaledresolution = new ScaledResolution(this.mc, this.mc.displayWidth,
						this.mc.displayHeight);
				int i = 5;
				int j = 5;
				switch (Location.values()[Keystrokes.location]) {
				case TOP_LEFT: {
					break;
				}
				case TOP_RIGHT: {
					i += scaledresolution.getScaledWidth() - Keystrokes.width;
					break;
				}
				case BOTTOM_LEFT: {
					j += scaledresolution.getScaledHeight() - Keystrokes.height;
					break;
				}
				case BOTTOM_RIGHT: {
					i += scaledresolution.getScaledWidth() - Keystrokes.width;
					j += scaledresolution.getScaledHeight() - Keystrokes.height;
					break;
				}
				default: {
					return;
				}
				}
				for (final Key key : Keystrokes.keys) {
					final int k = this.mc.fontRendererObj.getStringWidth(key.getName());
					Gui.drawRect(i + key.getX(), j + key.getY(), i + key.getX() + key.getWidth(),
							j + key.getY() + key.getHeight(), key.isPressed() ? 1728053247 : 1711276032);
					this.mc.fontRendererObj.drawString(key.getName(), i + key.getX() + key.getWidth() / 2 - k / 2,
							j + key.getY() + key.getHeight() / 2 - 4, key.isPressed() ? -16777216 : -1);
				}
			}
		}
		if (event instanceof EventTick) {
			for (final Key key : Keystrokes.keys) {
				if (!key.isMouseKey()) {
					key.setPressed(Keyboard.isKeyDown(key.getKey()));
				}
			}
			for (final Key key2 : Keystrokes.keys) {
				if (key2.isMouseKey()) {
					key2.setPressed(Mouse.isButtonDown(key2.getKey()));
				}
			}
		}
		if (event instanceof EventChatSent) {
			final String s = ((EventChatSent) event).getMessage();
			if (s.toLowerCase().startsWith("/keystrokes")) {
				((EventChatSent) event).setCancelled(true);
				final String[] astring = s.split(" ");
				if (astring.length == 1) {
					Minecraft.getMinecraft().thePlayer
							.addChatComponentMessage(new ChatComponentText("/keystrokes <setlocation|reload>"));
				} else {
					final String[] astring2 = new String[astring.length - 1];
					System.arraycopy(astring, 1, astring2, 0, astring.length - 1);
					if (astring2[0].equalsIgnoreCase("setlocation")) {
						String s2 = "";
						for (int i = 0; i < Location.values().length; ++i) {
							s2 = String.valueOf(s2) + (s2.equals("") ? "" : ", ") + Location.values()[i].name();
						}
						if (astring2.length < 2) {
							Minecraft.getMinecraft().thePlayer.addChatComponentMessage(
									new ChatComponentText("Invalid location. Possible locations: " + s2.toString()));
						} else {
							for (int j = 0; j < Location.values().length; ++j) {
								final Location location = Location.values()[j];
								if (location.name().equalsIgnoreCase(astring2[1])) {
									Keystrokes.location = j;
									try {
										final PrintWriter printwriter = new PrintWriter(
												new FileWriter(Keystrokes.locationFile));
										printwriter.println(j);
										printwriter.flush();
										printwriter.close();
									} catch (IOException ioexception) {
										ioexception.printStackTrace();
									}
									Minecraft.getMinecraft().thePlayer.addChatComponentMessage(
											new ChatComponentText("Location set to " + location + "."));
									return;
								}
							}
							Minecraft.getMinecraft().thePlayer.addChatComponentMessage(
									new ChatComponentText("Invalid location. Possible locations: " + s2.toString()));
						}
					} else if (astring2[0].equalsIgnoreCase("reload")) {
						reloadKeys();
						reloadLocation();
					} else {
						Minecraft.getMinecraft().thePlayer
								.addChatComponentMessage(new ChatComponentText("/keystrokes <setlocation|reload>"));
					}
				}
			}
		}
	}
}
