package ru.fluger.client;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;
import java.util.Random;
import javax.net.ssl.HttpsURLConnection;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.Display;
import ru.fluger.client.cmd.CommandManager;
import ru.fluger.client.cmd.macro.MacroManager;
import ru.fluger.client.event.EventManager;
import ru.fluger.client.event.EventTarget;
import ru.fluger.client.event.events.impl.input.EventInputKey;
import ru.fluger.client.feature.Feature;
import ru.fluger.client.feature.FeatureManager;
import ru.fluger.client.files.FileManager;
import ru.fluger.client.files.impl.FriendConfig;
import ru.fluger.client.files.impl.HudConfig;
import ru.fluger.client.files.impl.MacroConfig;
import ru.fluger.client.friend.FriendManager;
import ru.fluger.client.helpers.hwid.HwidUtil;
import ru.fluger.client.helpers.misc.TpsHelper;
import ru.fluger.client.helpers.request.RequstUtil;
import ru.fluger.client.settings.config.ConfigManager;
import ru.fluger.client.ui.clickgui.ClickGuiScreen;
import ru.fluger.client.ui.csgo.CSGOScreen;
import ru.fluger.client.ui.drag.DragManager;
import ru.fluger.client.ui.notifications.PreviewManager;
import ru.squad47.Client;
import ru.squad47.UserData;

public class Fluger {
   public static Fluger instance = new Fluger();
   public Long time;
   public static String name = "Fluger";
   public static String type = "Rage";
   public static String version = "270822";
   public static String status = "Client";
   public RequstUtil requst;
   public FeatureManager featureManager;
   public FriendManager friendManager;
   public MacroManager macroManager;
   public FileManager fileManager;
   public CommandManager commandManager;
   public ConfigManager configManager;
   public ClickGuiScreen newGui;
   public CSGOScreen csgoGUi;
   public DragManager dragmanager;
   public PreviewManager notifications;
   public HwidUtil hwidUtil;
   public String hwid;
   public static Client client = new Client();
   public static ScaleUtils scale = new ScaleUtils(2);

   public static double deltaTime() {
      return Minecraft.getDebugFPS() > 0 ? 1.0 / (double)Minecraft.getDebugFPS() : 1.0;
   }

   public void loadClient() {
      String login = "cracked";
      String uid = "0";
      String end = "unlimited";
      UserData var10000 = UserData.instance();
      String vk = "vk.com/1";
      var10000.vk = "vk.com/1";
      UserData.instance().setID(uid);
      UserData.instance().setLicenseDate(end);
      UserData.instance().setName(login);
      DiscordHelper.startRPC();
      Display.setTitle(name + " " + status + " - https://vk.com/fluger_client");
      this.time = System.currentTimeMillis();
      this.hwidUtil = new HwidUtil();
      this.requst = new RequstUtil();
      new TpsHelper();
      this.hwid = this.hwidUtil.getHwid();
      this.fileManager = new FileManager();
      this.fileManager.loadFiles();
      this.friendManager = new FriendManager();
      this.commandManager = new CommandManager();
      this.featureManager = new FeatureManager();
      this.macroManager = new MacroManager();
      this.configManager = new ConfigManager();
      this.newGui = new ClickGuiScreen();
      this.notifications = new PreviewManager();
      this.csgoGUi = new CSGOScreen();
      this.dragmanager = new DragManager();

      try {
         this.fileManager.getFile(FriendConfig.class).loadFile();
      } catch (IOException var12) {
         var12.printStackTrace();
      }

      try {
         this.fileManager.getFile(HudConfig.class).loadFile();
      } catch (IOException var11) {
         var11.printStackTrace();
      }

      try {
         this.fileManager.getFile(MacroConfig.class).loadFile();
      } catch (IOException var10) {
         var10.printStackTrace();
      }

      try {
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         URL url = new URL(linkToPhoto(UserData.instance().vk));
         InputStream buffer = url.openConnection().getInputStream();

         int i;
         while((i = buffer.read()) >= 0) {
            baos.write(i);
         }

         UserData.instance().buffer = baos.toByteArray();
      } catch (Exception var13) {
         var13.printStackTrace();
      }

      UserData.instance().init();
      EventManager.register(this);
   }

   public static String linkToPhoto(String vkid) {
      try {
         HttpsURLConnection url = (HttpsURLConnection)(new URL("https://api.vk.com/method/users.get?user_ids=" + vkid + "&fields=photo_100&access_token=3230fe4e6a879c5812d36eb5206828a0d301340b999c1b8ae0ca92c498ad3ff965900440e2fad1aae3c42&v=5.131")).openConnection();
         InputStream in = url.getInputStream();
         ByteArrayOutputStream baos = new ByteArrayOutputStream();

         int read;
         while((read = in.read()) >= 0) {
            baos.write(read);
         }

         String response = baos.toString();
         char[] responseChars = response.toCharArray();
         StringBuilder http = new StringBuilder();

         for(int offset = response.indexOf("https:\\"); offset <= 999 && responseChars[offset] != '"'; ++offset) {
            if (responseChars[offset] != '\\') {
               http.append(responseChars[offset]);
            }
         }

         return http.toString();
      } catch (Exception var9) {
         var9.printStackTrace();
         return null;
      }
   }

   public void shutDown() {
      DiscordHelper.stopRPC();
      instance.configManager.saveConfig("default");
      this.fileManager = new FileManager();
      this.fileManager.saveFiles();
      EventManager.unregister(this);
   }

   @EventTarget
   public void onInputKey(EventInputKey event) {
      Iterator var2 = this.featureManager.getFeatureList().iterator();

      while(var2.hasNext()) {
         Feature feature = (Feature)var2.next();
         if (feature.getBind() == event.getKey() && feature.getBind() != 0) {
            feature.toggle();
         }
      }

   }

   public int getRandomInRange(int min, int max) {
      Random rand = new Random();
      int randomNum = rand.nextInt(max - min + 1) + min;
      return randomNum;
   }
}
