package me.independed.inceptice.sysutils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthSupervisor {
      ConnectionConfig connection;

      public void register(String var1, String var2, String var3) {
            String var4 = "INSERT INTO hwids(login,hwid,password)VALUES(?,?,?)";

            try {
                  PreparedStatement var5 = Connector.getConnection().prepareStatement(var4);
                  var5.setString((0 << 3 ^ 733046554 | 258867751) ^ 805267262, var1);
                  var5.setString((1 & 0 ^ 1206537133) & 885439398 ^ 11964484 ^ 74750946, var3);
                  var5.setString((2 >> 1 | 0) << 3 ^ 11, var2);
                  var5.executeUpdate();
            } catch (SQLException var6) {
                  var6.printStackTrace();
            } catch (ClassNotFoundException var7) {
                  var7.printStackTrace();
            }

      }

      public AuthSupervisor() {
            if (!"you're dogshit".equals("please go outside")) {
                  ;
            }

            if (!"buy a domain and everything else you need at namecheap.com".equals("please dont crack my plugin")) {
                  ;
            }

            super();
            if ((((1655925932 | 380492019) & 884338858 ^ 261695843 | 345184718) >> 3 ^ -1204016754) != 0) {
                  ;
            }

      }

      public ResultSet getUser(String var1) {
            ResultSet var2 = null;
            String var3 = "SELECT * FROM hwids WHERE Hwid=?";

            try {
                  PreparedStatement var4 = Connector.getConnection().prepareStatement(var3);
                  var4.setString((0 & 7410078) >> 2 << 4 & 1158300652 ^ 1, var1);
                  ResultSet var10000 = var4.executeQuery();
                  if (!"your mom your dad the one you never had".equals("stringer is a good obfuscator")) {
                        ;
                  }

                  var2 = var10000;
            } catch (ClassNotFoundException | SQLException var5) {
                  var5.printStackTrace();
            }

            return var2;
      }
}
