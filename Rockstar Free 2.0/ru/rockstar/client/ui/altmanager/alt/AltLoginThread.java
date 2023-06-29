package ru.rockstar.client.ui.altmanager.alt;

import com.mojang.authlib.Agent;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import com.mojang.realmsclient.gui.ChatFormatting;
import java.net.Proxy;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;
import net.minecraft.util.text.TextFormatting;
import ru.rockstar.client.ui.altmanager.GuiAltManager;
import ru.rockstar.client.ui.altmanager.althening.api.AltService;

public class AltLoginThread extends Thread {
   private final Alt alt;
   private final Minecraft mc = Minecraft.getMinecraft();
   private String status;

   public AltLoginThread(Alt alt) {
      this.alt = alt;
      this.status = "В§7Waiting...";
   }

   private Session createSession(String username, String password) {
      try {
         GuiAltManager.altService.switchService(AltService.EnumAltService.MOJANG);
         YggdrasilAuthenticationService service = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
         YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication)service.createUserAuthentication(Agent.MINECRAFT);
         auth.setUsername(username);
         auth.setPassword(password);

         try {
            auth.logIn();
            return new Session(auth.getSelectedProfile().getName(), auth.getSelectedProfile().getId().toString(), auth.getAuthenticatedToken(), "mojang");
         } catch (AuthenticationException var6) {
            return null;
         }
      } catch (Exception var7) {
         return null;
      }
   }

   public String getStatus() {
      return this.status;
   }

   public void setStatus(String status) {
      this.status = status;
   }

   public void run() {
      if (this.alt.getPassword().equals("")) {
         this.mc.session = new Session(this.alt.getUsername(), "", "", "mojang");
         this.status = TextFormatting.GREEN + "Logged in - " + ChatFormatting.RED + this.alt.getUsername() + ChatFormatting.BOLD + " (non license)";
      } else {
         this.status = "Logging in...";
         Session auth = this.createSession(this.alt.getUsername(), this.alt.getPassword());
         if (auth == null) {
            this.status = "Connect failed!";
            if (this.alt.getStatus().equals(Alt.Status.Unchecked)) {
               this.alt.setStatus(Alt.Status.NotWorking);
            }
         } else {
            AltManager.lastAlt = new Alt(this.alt.getUsername(), this.alt.getPassword());
            this.status = TextFormatting.GREEN + "Logged in - " + ChatFormatting.RED + auth.getUsername() + ChatFormatting.BOLD + " (license)";
            this.alt.setMask(auth.getUsername());
            this.mc.session = auth;
         }
      }

   }
}
