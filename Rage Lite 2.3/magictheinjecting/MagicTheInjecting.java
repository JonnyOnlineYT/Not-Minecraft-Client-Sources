package magictheinjecting;

import java.io.File;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Iterator;

public class MagicTheInjecting extends Thread {
      public static byte[][] classes;

      private static Class tryGetClass(PrintWriter var0, ClassLoader var1, String... var2) throws ClassNotFoundException {
            ClassNotFoundException var3 = null;
            String[] var4 = var2;
            int var5 = var2.length;
            int var6 = 0;

            while(var6 < var5) {
                  String var7 = var4[var6];

                  try {
                        return var1.loadClass(var7);
                  } catch (ClassNotFoundException var9) {
                        var9.printStackTrace(var0);
                        var3 = var9;
                        ++var6;
                  }
            }

            throw var3;
      }

      public void run() {
            try {
                  PrintWriter var1 = new PrintWriter(System.getProperty("user.home") + File.separator + "eloader-log.txt", "UTF-8");
                  var1.println("Starting!");
                  var1.flush();

                  try {
                        ClassLoader var2 = null;
                        Iterator var3 = Thread.getAllStackTraces().keySet().iterator();

                        while(var3.hasNext()) {
                              Thread var4 = (Thread)var3.next();
                              ClassLoader var5;
                              if (var4 != null && var4.getContextClassLoader() != null && (var5 = var4.getContextClassLoader()).getClass() != null && var5.getClass().getName() != null) {
                                    String var6 = var5.getClass().getName();
                                    var1.println("Thread: " + var4.getName() + " [" + var6 + "]");
                                    var1.flush();
                                    if (var6.contains("LaunchClassLoader") || var6.contains("RelaunchClassLoader")) {
                                          var2 = var5;
                                          break;
                                    }
                              }
                        }

                        if (var2 == null) {
                              throw new Exception("ClassLoader is null");
                        }

                        this.setContextClassLoader(var2);
                        Class var29 = tryGetClass(var1, var2, "cpw.mods.fml.common.Mod$EventHandler", "net.minecraftforge.fml.common.Mod$EventHandler");
                        Class var30 = tryGetClass(var1, var2, "cpw.mods.fml.common.Mod", "net.minecraftforge.fml.common.Mod");
                        Class var31 = tryGetClass(var1, var2, "cpw.mods.fml.common.event.FMLInitializationEvent", "net.minecraftforge.fml.common.event.FMLInitializationEvent");
                        Class var32 = tryGetClass(var1, var2, "cpw.mods.fml.common.event.FMLPreInitializationEvent", "net.minecraftforge.fml.common.event.FMLPreInitializationEvent");
                        Method var7 = ClassLoader.class.getDeclaredMethod("defineClass", String.class, byte[].class, Integer.TYPE, Integer.TYPE, ProtectionDomain.class);
                        var7.setAccessible(true);
                        var1.println("Loading " + classes.length + " classes");
                        var1.flush();
                        ArrayList var8 = new ArrayList();
                        byte[][] var9 = classes;
                        int var10 = var9.length;
                        int var11 = 0;

                        while(true) {
                              Object[] var14;
                              if (var11 >= var10) {
                                    var1.println(classes.length + " loaded successfully");
                                    var1.flush();
                                    Iterator var33 = var8.iterator();

                                    while(var33.hasNext()) {
                                          Object[] var34 = (Object[])var33.next();
                                          Class var35 = (Class)var34[0];
                                          ArrayList var36 = (ArrayList)var34[1];
                                          ArrayList var37 = (ArrayList)var34[2];
                                          var14 = null;

                                          Object var38;
                                          try {
                                                var1.println("Instancing " + var35.getName());
                                                var1.flush();
                                                var38 = var35.newInstance();
                                                var1.println("Instanced");
                                                var1.flush();
                                          } catch (Exception var25) {
                                                var1.println("Genexeption on instancing: " + var25);
                                                var25.printStackTrace(var1);
                                                var1.flush();
                                                throw new Exception("Exception on instancing", var25);
                                          }

                                          Iterator var39 = var36.iterator();

                                          Method var40;
                                          while(var39.hasNext()) {
                                                var40 = (Method)var39.next();

                                                try {
                                                      var1.println("Preiniting " + var40);
                                                      var1.flush();
                                                      var1.println("Preinited");
                                                      var1.flush();
                                                      var40.invoke(var38, null);
                                                } catch (InvocationTargetException var23) {
                                                      var1.println("InvocationTargetException on preiniting: " + var23);
                                                      var23.getCause().printStackTrace(var1);
                                                      var1.flush();
                                                      throw new Exception("Exception on preiniting (InvocationTargetException)", var23.getCause());
                                                } catch (Exception var24) {
                                                      var1.println("Genexeption on preiniting: " + var24);
                                                      var24.printStackTrace(var1);
                                                      var1.flush();
                                                      throw new Exception("Exception on preiniting", var24);
                                                }
                                          }

                                          var39 = var37.iterator();

                                          while(var39.hasNext()) {
                                                var40 = (Method)var39.next();

                                                try {
                                                      var1.println("Initing " + var40);
                                                      var1.flush();
                                                      var1.println("Inited");
                                                      var1.flush();
                                                      var40.invoke(var38, null);
                                                } catch (InvocationTargetException var21) {
                                                      var1.println("InvocationTargetException on initing: " + var21);
                                                      var21.getCause().printStackTrace(var1);
                                                      var1.flush();
                                                      throw new Exception("Exception on initing (InvocationTargetException)", var21.getCause());
                                                } catch (Exception var22) {
                                                      var1.println("Genexeption on initing: " + var22);
                                                      var22.printStackTrace(var1);
                                                      var1.flush();
                                                      throw new Exception("Exception on initing", var22);
                                                }
                                          }
                                    }

                                    var1.println("Successfully injected");
                                    var1.flush();
                                    break;
                              }

                              byte[] var12 = var9[var11];
                              if (var12 == null) {
                                    throw new Exception("classData is null");
                              }

                              if (var2.getClass() == null) {
                                    throw new Exception("getClass() is null");
                              }

                              try {
                                    Class var13 = (Class)var7.invoke(var2, null, var12, 0, var12.length, var2.getClass().getProtectionDomain());
                                    if (var13.getAnnotation(var30) != null) {
                                          var14 = new Object[]{var13, null, null};
                                          ArrayList var15 = new ArrayList();
                                          ArrayList var16 = new ArrayList();
                                          Method[] var17 = var13.getDeclaredMethods();
                                          int var18 = var17.length;

                                          for(int var19 = 0; var19 < var18; ++var19) {
                                                Method var20 = var17[var19];
                                                if (var20.getAnnotation(var29) != null && var20.getParameterCount() == 1 && var20.getParameterTypes()[0] == var31) {
                                                      var20.setAccessible(true);
                                                      var16.add(var20);
                                                }

                                                if (var20.getAnnotation(var29) != null && var20.getParameterCount() == 1 && var20.getParameterTypes()[0] == var32) {
                                                      var20.setAccessible(true);
                                                      var15.add(var20);
                                                }
                                          }

                                          var14[1] = var15;
                                          var14[2] = var16;
                                          var8.add(var14);
                                    }
                              } catch (Exception var26) {
                                    var26.printStackTrace();
                                    throw new Exception("Exception on defineClass", var26);
                              }

                              ++var11;
                        }
                  } catch (Throwable var27) {
                        var27.printStackTrace(var1);
                        var1.flush();
                  }

                  var1.close();
            } catch (Throwable var28) {
                  var28.printStackTrace();
            }

      }

      public static int injectCP(byte[][] var0) {
            try {
                  classes = var0;
                  MagicTheInjecting var1 = new MagicTheInjecting();
                  var1.start();
            } catch (Exception var2) {
            }

            return 0;
      }

      public static byte[][] getByteArray(int var0) {
            return new byte[var0][];
      }
}
