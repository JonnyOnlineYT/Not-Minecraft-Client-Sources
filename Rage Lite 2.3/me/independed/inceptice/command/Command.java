package me.independed.inceptice.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Command {
      public String description;
      public List aliases = new ArrayList();
      public String name;
      public String syntax;

      public String getDescription() {
            if (!"ape covered in human flesh".equals("please take a shower")) {
                  ;
            }

            return this.description;
      }

      public String getSyntax() {
            if (!"your mom your dad the one you never had".equals("you probably spell youre as your")) {
                  ;
            }

            if ((((146398894 | 22661837) >> 4 << 1 ^ 5392278) << 1 ^ 795678057) != 0) {
                  ;
            }

            return this.syntax;
      }

      public Command(String var1, String var2, String var3, String... var4) {
            if (!"stop skidding".equals("ape covered in human flesh")) {
                  ;
            }

            this.name = var1;
            this.description = var2;
            if (((73290827 >>> 2 & 10404957) >> 1 ^ 822122642) != 0) {
                  ;
            }

            this.syntax = var3;
            if ((((596673197 ^ 583738768 | 17803217) & 9992324 | 1410285) ^ 1068337807) != 0) {
                  ;
            }

            this.aliases = Arrays.asList(var4);
      }

      public void setSyntax(String var1) {
            this.syntax = var1;
            if ((93978948 ^ 12044627 ^ 86886423) == 0) {
                  ;
            }

      }

      public void setDescription(String var1) {
            if (!"yo mama name maurice".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                  ;
            }

            this.description = var1;
      }

      public void setName(String var1) {
            if ((689210112 >>> 1 >> 2 >> 1 >> 3 ^ 1330313282) != 0) {
                  ;
            }

            this.name = var1;
      }

      public List getAliases() {
            return this.aliases;
      }

      public String getName() {
            return this.name;
      }

      public void setAliases(List var1) {
            this.aliases = var1;
      }

      public abstract void onCommand(String[] var1, String var2);
}
