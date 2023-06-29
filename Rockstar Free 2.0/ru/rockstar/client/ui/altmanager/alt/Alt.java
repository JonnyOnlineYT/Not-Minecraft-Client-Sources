package ru.rockstar.client.ui.altmanager.alt;

import net.minecraft.util.text.TextFormatting;

public class Alt {
   private final String username;
   private String mask;
   private String password;
   private Alt.Status status;

   public Alt(String username, String password) {
      this(username, password, Alt.Status.Unchecked);
   }

   public Alt(String username, String password, Alt.Status status) {
      this(username, password, "", status);
   }

   public Alt(String username, String password, String mask, Alt.Status status) {
      this.username = username;
      this.password = password;
      this.mask = mask;
      this.status = status;
   }

   public Alt.Status getStatus() {
      return this.status;
   }

   public void setStatus(Alt.Status status) {
      this.status = status;
   }

   public String getMask() {
      return this.mask;
   }

   public void setMask(String mask) {
      this.mask = mask;
   }

   public String getPassword() {
      return this.password;
   }

   public void setPassword(String password) {
      this.password = password;
   }

   public String getUsername() {
      return this.username;
   }

   public static enum Status {
      Working(TextFormatting.GREEN + "Working"),
      Banned(TextFormatting.RED + "Banned"),
      Unchecked(TextFormatting.YELLOW + "Unchecked"),
      NotWorking(TextFormatting.RED + "Not Working");

      private final String formatted;

      private Status(String string) {
         this.formatted = string;
      }

      public String toFormatted() {
         return this.formatted;
      }
   }
}
