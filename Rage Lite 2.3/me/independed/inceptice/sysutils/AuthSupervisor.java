package me.independed.inceptice.sysutils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthSupervisor {
      ConnectionConfig connection;

      public ResultSet getUser(String var1) {
            ResultSet var2 = null;
            String var3 = "SELECT * FROM hwids WHERE Hwid=?";

            try {
                  PreparedStatement var4 = Connector.getConnection().prepareStatement(var3);
                  var4.setString((0 >>> 2 >> 2 | 847425525) ^ 847425524, var1);
                  var2 = var4.executeQuery();
            } catch (ClassNotFoundException | SQLException var5) {
                  var5.printStackTrace();
            }

            return var2;
      }

      public void register(String var1, String var2, String var3) {
            String var4 = "INSERT INTO hwids(login,hwid,password)VALUES(?,?,?)";

            try {
                  PreparedStatement var10000 = Connector.getConnection().prepareStatement(var4);
                  if (((699533714 ^ 157534165) << 1 >>> 1 >> 1 ^ 275310371) == 0) {
                        ;
                  }

                  PreparedStatement var5 = var10000;
                  var5.setString(0 ^ 2108689241 ^ 975853167 ^ 1201298743, var1);
                  var5.setString(1 << 2 >> 3 << 1 << 1 ^ 2, var3);
                  var5.setString(((2 >> 4 ^ 1282409003) >> 3 << 3 | 954737849) ^ 2096637626, var2);
                  var5.executeUpdate();
            } catch (SQLException var6) {
                  var6.printStackTrace();
            } catch (ClassNotFoundException var7) {
                  if ("please get a girlfriend and stop cracking plugins".equals("stringer is a good obfuscator")) {
                  }

                  var7.printStackTrace();
            }

      }

      public AuthSupervisor() {
            if ((37753867 ^ 37753867) == 0) {
                  ;
            }

      }
}
