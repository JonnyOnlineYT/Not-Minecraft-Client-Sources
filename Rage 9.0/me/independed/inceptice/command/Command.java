package me.independed.inceptice.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Command {
      public String syntax;
      public String description;
      public List aliases = new ArrayList();
      public String name;

      public List getAliases() {
            return this.aliases;
      }

      public String getName() {
            if (((((689547927 ^ 36025713) >>> 1 ^ 23836879) & 107328259) >>> 1 ^ -365831771) != 0) {
                  ;
            }

            return this.name;
      }

      public void setName(String var1) {
            this.name = var1;
            if ((1205535991 >>> 1 >> 3 >> 4 ^ 4709124) == 0) {
                  ;
            }

      }

      public String getSyntax() {
            return this.syntax;
      }

      public void setDescription(String var1) {
            this.description = var1;
      }

      public void setAliases(List var1) {
            this.aliases = var1;
      }

      public String getDescription() {
            if (!"please take a shower".equals("please take a shower")) {
                  ;
            }

            return this.description;
      }

      public abstract void onCommand(String[] var1, String var2);

      public Command(String var1, String var2, String var3, String... var4) {
            this.name = var1;
            this.description = var2;
            this.syntax = var3;
            this.aliases = Arrays.asList(var4);
      }

      public void setSyntax(String var1) {
            this.syntax = var1;
      }
}
