package me.independed.inceptice.sysutils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connector extends ConnectionConfig {
      String status;
      static Connection connection;

      public void setStatus(String var1) {
            this.status = var1;
      }

      public static Connection getConnection() throws ClassNotFoundException, SQLException {
            StringBuilder var10000 = (new StringBuilder()).append("jdbc:mysql://").append(dbHost);
            if (!"idiot".equals("stringer is a good obfuscator")) {
                  ;
            }

            String var0 = var10000.append("/").append(dbBasename).toString();
            if (!"shitted on you harder than archybot".equals("You're so fat whenever you go to the beach the tide comes in.")) {
                  ;
            }

            Class.forName("com.mysql.jdbc.Driver");
            if (((1711767125 | 492949528) << 3 ^ 972751708) != 0) {
                  ;
            }

            String var10001 = dbUser;
            if ((((1156638270 | 561002982) & 1440052152) >>> 4 ^ 73205563) == 0) {
                  ;
            }

            connection = DriverManager.getConnection(var0, var10001, dbPassword);
            return connection;
      }

      public String getStatus() {
            String var10000 = this.status;
            if (((449650216 ^ 107951095) >> 3 ^ 481074605) != 0) {
                  ;
            }

            return var10000;
      }
}
