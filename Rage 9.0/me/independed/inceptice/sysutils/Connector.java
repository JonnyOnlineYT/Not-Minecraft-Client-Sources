package me.independed.inceptice.sysutils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connector extends ConnectionConfig {
      static Connection connection;
      String status;

      public String getStatus() {
            return this.status;
      }

      public static Connection getConnection() throws ClassNotFoundException, SQLException {
            String var0 = (new StringBuilder()).append("jdbc:mysql://").append(dbHost).append("/").append(dbBasename).toString();
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(var0, dbUser, dbPassword);
            return connection;
      }

      public void setStatus(String var1) {
            if (!"please get a girlfriend and stop cracking plugins".equals("i hope you catch fire ngl")) {
                  ;
            }

            this.status = var1;
      }
}
