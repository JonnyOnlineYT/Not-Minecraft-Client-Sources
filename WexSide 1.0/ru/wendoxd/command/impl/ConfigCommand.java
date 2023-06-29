package ru.wendoxd.command.impl;

import java.awt.Desktop;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.util.ArrayList;

import com.mojang.realmsclient.gui.ChatFormatting;

import ru.wendoxd.command.Command;
import ru.wendoxd.command.CommandInfo;
import ru.wendoxd.modules.impl.visuals.HUD;
import ru.wendoxd.modules.impl.visuals.Indicators;
import ru.wendoxd.modules.impl.visuals.Keybinds;
import ru.wendoxd.modules.impl.visuals.Notifications;
import ru.wendoxd.modules.impl.visuals.TargetHUD;
import ru.wendoxd.ui.menu.elements.Frame;
import ru.wendoxd.ui.menu.elements.Tab;
import ru.wendoxd.ui.menu.elements.tabelements.PrimaryButton;
import ru.wendoxd.ui.menu.utils.MenuAPI;
import ru.wendoxd.utils.sound.SoundUtils;

@CommandInfo(name = "cfg", desc = "Взаимодействие с конфигами")
public class ConfigCommand extends Command {

	@Override
	public void execute(String[] args) throws Exception {
		File dir = new File("wex/");
		if (!dir.exists()) {
			dir.mkdir();
		}
		if (args[1].equals("dir")) {
			try {
				Desktop.getDesktop().browse(dir.toURI());
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (args[1].equals("list")) {
			sendMessage(ChatFormatting.GRAY + "Список конфигов:");
			int i = 0;
			for (File file : dir.listFiles()) {
				if (checkConfig(file) && file.getAbsolutePath().endsWith(".wex")) {
					sendMessage(ChatFormatting.GRAY + file.getName());
					i++;
				}
			}
			if (i == 0) {
				sendMessage(ChatFormatting.GRAY + "Конфигов нет");
			}
		} else {
			String name = args[2];
			File cfg = new File(dir, name.endsWith(".wex") ? name : name.concat(".wex"));
			if (args[1].equalsIgnoreCase("load")) {
				if (!checkConfig(cfg)) {
					configOutdated();
					return;
				}
				try {
					DataInputStream dis = new DataInputStream(new FileInputStream(cfg));
					int w = dis.readInt();
					try {
						TargetHUD.position.setX(dis.readInt());
						TargetHUD.position.setY(dis.readInt());
						Keybinds.keybinds.setX(dis.readInt());
						Keybinds.keybinds.setY(dis.readInt());
						if (w == 1639 || w == 1900) {
							Indicators.panel.setX(dis.readInt());
							Indicators.panel.setY(dis.readInt());
						}
						if (w == 1900) {
							HUD.de.setX(dis.readInt());
							HUD.de.setY(dis.readInt());
						}
						int cnt = dis.readInt();
						while (cnt > 0) {
							int sectionLength = dis.readInt();
							ByteArrayOutputStream baos = new ByteArrayOutputStream();
							for (int i = 0; i < sectionLength; i++) {
								baos.write(dis.readByte());
							}
							DataInputStream dis2 = new DataInputStream(new ByteArrayInputStream(baos.toByteArray()));
							String pathName = dis2.readUTF();
							try {
								for (Tab tab : MenuAPI.tabs) {
									for (Frame frame : tab.getFrames()) {
										for (PrimaryButton button : frame.getButtons()) {
											if (pathName.equals(button.getPath())) {
												button.load(dis2);
											}
										}
									}
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
							cnt--;
						}
						sendMessage(ChatFormatting.GRAY + "Конфиг успешно загружен");
						Notifications.notify("Config System",
								cfg.getName().replace(".wex", "") + " successfully loaded!", 1);
						SoundUtils.playSound();
					} catch (Exception e) {
						Notifications.notify("Config System", "Error", 1);
						e.printStackTrace();
					}
					dis.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (args[1].equalsIgnoreCase("save")) {
				try {
					if (!cfg.exists()) {
						cfg.createNewFile();
					}
					DataOutputStream dos = new DataOutputStream(new FileOutputStream(cfg));
					ArrayList<byte[]> bytes = new ArrayList();
					dos.writeInt(1900);
					dos.writeInt(TargetHUD.position.getX());
					dos.writeInt(TargetHUD.position.getY());
					dos.writeInt(Keybinds.keybinds.getX());
					dos.writeInt(Keybinds.keybinds.getY());
					dos.writeInt(Indicators.panel.getX());
					dos.writeInt(Indicators.panel.getY());
					dos.writeInt(HUD.de.getX());
					dos.writeInt(HUD.de.getY());
					for (Tab tab : MenuAPI.tabs) {
						for (Frame frame : tab.getFrames()) {
							for (PrimaryButton button : frame.getButtons()) {
								ByteArrayOutputStream contextBaos = new ByteArrayOutputStream();
								DataOutputStream contextDos = new DataOutputStream(contextBaos);
								button.save(new DataOutputStream(contextBaos));
								bytes.add(contextBaos.toByteArray());
								contextDos.close();
							}
						}
					}
					dos.writeInt(bytes.size());
					bytes.forEach(b -> {
						try {
							dos.writeInt(b.length);
							for (int i = 0; i < b.length; i++) {
								dos.writeByte(b[i]);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					});
					dos.close();
					Notifications.notify("Config System", cfg.getName().replace(".wex", "") + " saved successfully!",
							1);
					sendMessage(ChatFormatting.GRAY + "Конфиг успешно сохранен");
					SoundUtils.playSound();
				} catch (Exception e) {
					SoundUtils.playSound(1.2f);
					sendMessage(ChatFormatting.GRAY + "Ошибка при сохранении конфига");
					e.printStackTrace();
				}
			}
			if (args[1].equalsIgnoreCase("delete")) {
				Files.deleteIfExists(cfg.toPath());
				if (cfg.exists()) {
					sendMessage(ChatFormatting.GRAY + "Ошибка при удалении конфига");
					return;
				}
				sendMessage(ChatFormatting.GRAY + "Конфиг успешно удален");
				SoundUtils.playSound();
			}
		}
	}

	public void configOutdated() {
		sendMessage(ChatFormatting.GRAY + "Конфиг не найден либо устарел");
		SoundUtils.playSound(1.2f);
	}

	public static boolean checkConfig(File config) {
		try {
			if (!config.exists()) {
				return false;
			}
			DataInputStream dis = new DataInputStream(new FileInputStream(config));
			int read = dis.readInt();
			dis.close();
			if (read == 1000) {
				return true;
			}
			if (read == 1639) {
				return true;
			}
			if (read == 1900) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public void error() {
		sendMessage(ChatFormatting.GRAY + "Ошибка в использовании" + ChatFormatting.WHITE + ":");
		sendMessage(ChatFormatting.WHITE + PrefixCommand.prefix + "cfg load " + ChatFormatting.GRAY + "<name>");
		sendMessage(ChatFormatting.WHITE + PrefixCommand.prefix + "cfg save " + ChatFormatting.GRAY + "<name>");
		sendMessage(ChatFormatting.WHITE + PrefixCommand.prefix + "cfg delete " + ChatFormatting.GRAY + "<name>");
		sendMessage(ChatFormatting.WHITE + PrefixCommand.prefix + "cfg list" + ChatFormatting.GRAY
				+ " - показать список конфигов");
		sendMessage(ChatFormatting.WHITE + PrefixCommand.prefix + "cfg dir" + ChatFormatting.GRAY
				+ " - открыть папку с конфигами");
	}
}
