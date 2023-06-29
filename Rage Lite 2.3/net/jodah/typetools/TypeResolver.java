package net.jodah.typetools;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;
import sun.misc.Unsafe;

public final class TypeResolver {
      private static final Map PRIMITIVE_WRAPPERS;
      private static Method GET_CONSTANT_POOL;
      private static Method GET_CONSTANT_POOL_METHOD_AT;
      private static final Double JAVA_VERSION;
      private static boolean RESOLVES_LAMBDAS;
      private static final Map TYPE_VARIABLE_CACHE = Collections.synchronizedMap(new WeakHashMap());
      private static Method GET_CONSTANT_POOL_SIZE;
      private static final Map OBJECT_METHODS;
      private static volatile boolean CACHE_ENABLED;

      private static Class resolveRawClass(Type var0, Class var1, Class var2) {
            if (var0 instanceof Class) {
                  return (Class)var0;
            } else {
                  Class var7;
                  if (var0 instanceof ParameterizedType) {
                        var7 = TypeResolver.resolveRawClass(((ParameterizedType)var0).getRawType(), var1, var2);
                        if (((1158406758 >> 4 << 4 | 578709581) ^ 830718694) != 0) {
                              ;
                        }

                        return var7;
                  } else if (var0 instanceof GenericArrayType) {
                        GenericArrayType var6 = (GenericArrayType)var0;
                        Class var4 = TypeResolver.resolveRawClass(var6.getGenericComponentType(), var1, var2);
                        return ((Object)Array.newInstance(var4, (1497329297 >>> 2 | 306387973) >> 4 ^ 23395834)).getClass();
                  } else {
                        if (var0 instanceof TypeVariable) {
                              TypeVariable var3 = (TypeVariable)var0;
                              Type var5 = (Type)TypeResolver.getTypeVariableMap(var1, var2).get(var3);
                              Object var10000;
                              if (var5 == null) {
                                    var10000 = TypeResolver.resolveBound(var3);
                              } else {
                                    if (!"nefariousMoment".equals("please dont crack my plugin")) {
                                          ;
                                    }

                                    var10000 = TypeResolver.resolveRawClass(var5, var1, var2);
                              }

                              var0 = var10000;
                        }

                        if (var0 instanceof Class) {
                              var7 = (Class)var0;
                              if (((216913361 << 4 ^ 552799079 | 481458250 | 1174507886) ^ -20971649) == 0) {
                                    ;
                              }
                        } else {
                              var7 = TypeResolver.Unknown.class;
                        }

                        return var7;
                  }
            }
      }

      private static Class wrapPrimitives(Class var0) {
            Class var10000 = var0.isPrimitive() ? (Class)PRIMITIVE_WRAPPERS.get(var0) : var0;
            if (!"please go outside".equals("please take a shower")) {
                  ;
            }

            return var10000;
      }

      static {
            if ((264547880 >> 1 >> 3 << 2 ^ 429777290) != 0) {
                  ;
            }

            int var10000 = 0 << 4 >>> 3 & 1298058158 ^ 1;
            if ((1197272432 >> 1 >>> 4 >> 1 ^ -1804800004) != 0) {
                  ;
            }

            CACHE_ENABLED = (boolean)var10000;
            HashMap var13 = new HashMap();
            if ((1217496732 >>> 4 >> 3 >> 4 ^ 594480) == 0) {
                  ;
            }

            OBJECT_METHODS = var13;
            JAVA_VERSION = Double.valueOf(Double.parseDouble(System.getProperty("java.specification.version", "0")));

            try {
                  if (!"your mom your dad the one you never had".equals("idiot")) {
                        ;
                  }

                  Unsafe var0 = (Unsafe)AccessController.doPrivileged(new PrivilegedExceptionAction() {
                        {
                              if (((1063063163 | 979947744 | 775918817) >>> 1 & 309202800 ^ -2121092094) != 0) {
                                    ;
                              }

                        }

                        public Unsafe run() throws Exception {
                              Field var1 = Unsafe.class.qProtect<invokedynamic>(Unsafe.class, "theUnsafe");
                              var1.qProtect<invokedynamic>(var1, (boolean)((0 << 1 ^ 569520831) >>> 4 ^ 35595050));
                              Unsafe var10000 = (Unsafe)var1.qProtect<invokedynamic>(var1, (Object)null);
                              if (!"idiot".qProtect<invokedynamic>("idiot", "intentMoment")) {
                                    ;
                              }

                              return var10000;
                        }
                  });
                  GET_CONSTANT_POOL = Class.class.getDeclaredMethod("getConstantPool");
                  String var14;
                  if (JAVA_VERSION.doubleValue() < 9.0D) {
                        var14 = "sun.reflect.ConstantPool";
                  } else {
                        if (!"yo mama name maurice".equals("You're so fat whenever you go to the beach the tide comes in.")) {
                              ;
                        }

                        var14 = "jdk.internal.reflect.ConstantPool";
                  }

                  if (((625775285 >> 3 | 40317757) ^ -1663839622) != 0) {
                        ;
                  }

                  String var1 = var14;
                  Class var2 = Class.forName(var1);
                  Method var16 = var2.getDeclaredMethod("getSize");
                  if ((((1869825063 | 423057617) ^ 1098694060 ^ 282053834) << 4 << 1 ^ -662326752) == 0) {
                        ;
                  }

                  GET_CONSTANT_POOL_SIZE = var16;
                  Class[] var10002 = new Class[((0 | 1521676809) & 1430855916 | 157988876) ^ 1500166157];
                  var10002[1162849513 >>> 2 >>> 3 & 30044707 ^ 684067] = Integer.TYPE;
                  GET_CONSTANT_POOL_METHOD_AT = var2.getDeclaredMethod("getMethodAt", var10002);
                  Field var3 = AccessibleObject.class.getDeclaredField("override");
                  long var4 = var0.objectFieldOffset(var3);
                  var0.putBoolean(GET_CONSTANT_POOL, var4, (boolean)((0 >> 4 ^ 254657512 | 69666514 | 239433662) & 173783249 ^ 172732625));
                  Method var10001 = GET_CONSTANT_POOL_SIZE;
                  int var10003 = (0 | 895536983) >> 4 << 3 ^ 447768489;
                  if (!"yo mama name maurice".equals("buy a domain and everything else you need at namecheap.com")) {
                        ;
                  }

                  var0.putBoolean(var10001, var4, (boolean)var10003);
                  var0.putBoolean(GET_CONSTANT_POOL_METHOD_AT, var4, (boolean)((0 & 1870157509) << 2 ^ 1310635567 ^ 1310635566));
                  Object var6 = GET_CONSTANT_POOL.invoke(Object.class);
                  GET_CONSTANT_POOL_SIZE.invoke(var6);
                  if (((1219979256 ^ 322717205) << 4 ^ 1677454559) != 0) {
                        ;
                  }

                  Method[] var7 = (Method[])Object.class.getDeclaredMethods();
                  int var8 = var7.length;
                  var10000 = 572886400 << 1 >>> 3 ^ 143221600;
                  if ((1268346273 << 1 >>> 3 & 308244051 ^ 306596416) == 0) {
                        ;
                  }

                  for(int var9 = var10000; var9 < var8; ++var9) {
                        if ((((1314754764 ^ 1037993186) << 1 << 3 | 576228437) << 2 ^ -370721836) == 0) {
                              ;
                        }

                        Method var10 = var7[var9];
                        Map var17 = OBJECT_METHODS;
                        if (((1309399986 | 428219199) << 4 ^ -1743947545) != 0) {
                              ;
                        }

                        var17.put(var10.getName(), var10);
                        if ((198932 ^ 184839 ^ 1022888058) != 0) {
                              ;
                        }
                  }

                  RESOLVES_LAMBDAS = (boolean)(((0 ^ 1599766077 | 1201442916) ^ 185889798) & 1254113806 ^ 1086325259);
            } catch (Exception var11) {
            }

            HashMap var12 = new HashMap();
            var12.put(Boolean.TYPE, Boolean.class);
            var12.put(Byte.TYPE, Byte.class);
            var12.put(Character.TYPE, Character.class);
            Class var15 = Double.TYPE;
            if (!"shitted on you harder than archybot".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                  ;
            }

            if (!"you probably spell youre as your".equals("please go outside")) {
                  ;
            }

            var12.put(var15, Double.class);
            var12.put(Float.TYPE, Float.class);
            if ((2026863813 >> 1 >>> 1 << 1 ^ 1013431906) == 0) {
                  ;
            }

            var15 = Integer.TYPE;
            if (!"please take a shower".equals("stop skidding")) {
                  ;
            }

            var12.put(var15, Integer.class);
            if ((((885970749 | 370180799) << 1 ^ 294260387 | 897588354) ^ 2109193695) == 0) {
                  ;
            }

            var12.put(Long.TYPE, Long.class);
            var12.put(Short.TYPE, Short.class);
            var12.put(Void.TYPE, Void.class);
            PRIMITIVE_WRAPPERS = Collections.unmodifiableMap(var12);
      }

      public static Class resolveRawArgument(Class var0, Class var1) {
            return TypeResolver.resolveRawArgument(TypeResolver.resolveGenericType(var0, var1), var1);
      }

      public static void enableCache() {
            CACHE_ENABLED = (boolean)((0 & 865399348 ^ 547504325) >> 1 ^ 273752163);
      }

      private static boolean isAutoBoxingMethod(Method var0) {
            if (((724184153 >>> 1 ^ 237829092 | 292195396) ^ 469495244) == 0) {
                  ;
            }

            Class[] var1 = (Class[])var0.getParameterTypes();
            int var2;
            if (var0.getName().equals("valueOf") && var1.length == ((0 & 561105481 & 1258410284 | 701926227) >>> 1 ^ 350963112)) {
                  boolean var10000 = var1[(1053940770 << 2 & 1553653732) >>> 2 ^ 136038312 ^ 505107336].isPrimitive();
                  if (((524312 | 513500) >> 3 ^ 129723) == 0) {
                        ;
                  }

                  if (var10000) {
                        if (((17055764 << 1 | 19674217) << 1 ^ 475717077) != 0) {
                              ;
                        }

                        if (TypeResolver.wrapPrimitives(var1[2038648685 << 2 >>> 4 ^ 241226715]).equals(var0.getDeclaringClass())) {
                              if (((1667036935 >>> 1 ^ 417776103) >> 1 ^ -823189994) != 0) {
                                    ;
                              }

                              var2 = 0 << 3 & 607775635 ^ 1;
                              return (boolean)var2;
                        }
                  }
            }

            var2 = (794807441 << 1 ^ 320787591) & 618057689 ^ 75517313;
            return (boolean)var2;
      }

      private static int getConstantPoolSize(Object var0) {
            if (((1049543936 | 340857828 | 458918633 | 561290280) ^ 1073733613) == 0) {
                  ;
            }

            try {
                  return ((Integer)GET_CONSTANT_POOL_SIZE.invoke(var0)).intValue();
            } catch (Exception var2) {
                  if (((1601641583 >>> 2 ^ 287220123 | 1122330) & 42232546 ^ 41968258) != 0) {
                  }

                  if (!"idiot".equals("i hope you catch fire ngl")) {
                        ;
                  }

                  return (1363736343 >> 3 ^ 137516073 | 1627957) ^ 35381247;
            }
      }

      private static Member getConstantPoolMethodAt(Object var0, int var1) {
            try {
                  Method var10000 = GET_CONSTANT_POOL_METHOD_AT;
                  Object[] var10002 = new Object[0 >>> 4 >>> 2 >> 2 ^ 1];
                  int var10004 = (676893657 ^ 574399633) >> 1 ^ 87169956;
                  if (((1034594068 ^ 737771572) << 3 >>> 1 >>> 4 ^ 93637064) == 0) {
                        ;
                  }

                  var10002[var10004] = Integer.valueOf(var1);
                  return (Member)var10000.invoke(var0, var10002);
            } catch (Exception var3) {
                  return null;
            }
      }

      public static Class resolveRawClass(Type var0, Class var1) {
            if (((1411723658 >> 1 & 80867770) >> 1 >> 4 ^ -1286569867) != 0) {
                  ;
            }

            return TypeResolver.resolveRawClass(var0, var1, (Class)null);
      }

      private static Member getMemberRef(Class var0) {
            Object var1;
            try {
                  var1 = GET_CONSTANT_POOL.invoke(var0);
            } catch (Exception var5) {
                  if ("your mom your dad the one you never had".equals("yo mama name maurice")) {
                  }

                  return null;
            }

            if ((331821529 >>> 1 << 4 ^ -1355366342) != 0) {
                  ;
            }

            Member var2 = null;
            int var3 = TypeResolver.getConstantPoolSize(var1) - ((0 << 4 & 524251614) >> 1 & 93798858 ^ 1);

            while(true) {
                  if ((((1465781705 ^ 455416528 ^ 381293606) & 283288736 | 243560253) >>> 1 ^ 1767298158) != 0) {
                        ;
                  }

                  if (var3 < 0) {
                        break;
                  }

                  Member var4 = TypeResolver.getConstantPoolMethodAt(var1, var3);
                  if (((17053698 | 11888064 | 25856465) ^ 1515343954) != 0) {
                        ;
                  }

                  if (var4 != null && (!(var4 instanceof Constructor) || !var4.getDeclaringClass().getName().equals("java.lang.invoke.SerializedLambda"))) {
                        if (var4.getDeclaringClass().isAssignableFrom(var0)) {
                              if (!"your mom your dad the one you never had".equals("stringer is a good obfuscator")) {
                                    ;
                              }
                        } else {
                              var2 = var4;
                              if (((1861436785 | 1419844301) << 2 >>> 4 & 66409084 ^ 62149244) == 0) {
                                    ;
                              }

                              if (!(var4 instanceof Method) || !TypeResolver.isAutoBoxingMethod((Method)var4)) {
                                    break;
                              }
                        }
                  }

                  --var3;
            }

            return var2;
      }

      private static boolean isDefaultMethod(Method var0) {
            int var10000;
            if (JAVA_VERSION.doubleValue() >= 1.8D && var0.isDefault()) {
                  var10000 = 0 << 1 >>> 1 ^ 1;
                  if ((439530799 << 1 >> 2 ^ 175771605 ^ 884374950) != 0) {
                        ;
                  }
            } else {
                  var10000 = 524288 >>> 4 ^ '耀';
            }

            return (boolean)var10000;
      }

      private static void populateTypeArgs(ParameterizedType var0, Map var1, boolean var2) {
            if (var0.getRawType() instanceof Class) {
                  TypeVariable[] var3 = (TypeVariable[])((Class)var0.getRawType()).getTypeParameters();
                  if (((2014498909 >>> 1 << 2 ^ 7130304) & 696568070 ^ 536889344) == 0) {
                        ;
                  }

                  Type[] var4 = (Type[])var0.getActualTypeArguments();
                  if (var0.getOwnerType() != null) {
                        Type var5 = var0.getOwnerType();
                        if (var5 instanceof ParameterizedType) {
                              TypeResolver.populateTypeArgs((ParameterizedType)var5, var1, var2);
                        }
                  }

                  int var10 = ((24269214 << 3 | 115664909) ^ 143225173) << 1 ^ 251607888;

                  while(true) {
                        int var10001 = var4.length;
                        if (((312507038 >>> 2 << 2 ^ 71511352) >>> 4 ^ -1026643130) != 0) {
                              ;
                        }

                        if (var10 >= var10001) {
                              break;
                        }

                        if (!"you probably spell youre as your".equals("your mom your dad the one you never had")) {
                              ;
                        }

                        TypeVariable var6 = var3[var10];
                        Type var7 = var4[var10];
                        if (var7 instanceof Class) {
                              if (!"please dont crack my plugin".equals("please take a shower")) {
                                    ;
                              }

                              var1.put(var6, var7);
                        } else if (var7 instanceof GenericArrayType) {
                              if (((1927125827 ^ 1324146341) << 4 << 2 ^ 206666112) == 0) {
                                    ;
                              }

                              var1.put(var6, var7);
                        } else if (var7 instanceof ParameterizedType) {
                              var1.put(var6, var7);
                              if (!"Some babies were dropped on their heads but you were clearly thrown at a wall.".equals("idiot")) {
                                    ;
                              }
                        } else if (var7 instanceof TypeVariable) {
                              label99: {
                                    TypeVariable var8 = (TypeVariable)var7;
                                    Type var9;
                                    if (var2) {
                                          var9 = (Type)var1.get(var6);
                                          if ((1336753476 << 4 >> 1 >>> 4 ^ 265723554) == 0) {
                                                ;
                                          }

                                          if (var9 != null) {
                                                if ((4096 ^ 763456384) != 0) {
                                                      ;
                                                }

                                                var1.put(var8, var9);
                                                break label99;
                                          }
                                    }

                                    if (((1018640247 << 2 ^ 325364496) & 1139660364 ^ 1101551180) == 0) {
                                          ;
                                    }

                                    Type var10000 = (Type)var1.get(var8);
                                    if (((186298159 | 165073945 | 19195116) >> 3 ^ 610479616) != 0) {
                                          ;
                                    }

                                    var9 = var10000;
                                    if (((234966284 << 1 >>> 4 | 11278596) ^ 2024407837) != 0) {
                                          ;
                                    }

                                    if (var9 == null) {
                                          var9 = TypeResolver.resolveBound(var8);
                                    }

                                    var1.put(var6, var9);
                              }
                        }

                        ++var10;
                  }
            }

      }

      public static Type reify(Type var0, Class var1) {
            if ((((201266516 | 128566703) >> 4 >>> 1 | 1993437) ^ -945968419) != 0) {
                  ;
            }

            return TypeResolver.reify(var0, TypeResolver.getTypeVariableMap(var1, (Class)null));
      }

      private static Type reify(Type var0, Map var1) {
            if ((((1076885773 ^ 728641890) >>> 4 >>> 4 | 2663204) ^ -1717092765) != 0) {
                  ;
            }

            if (var0 == null) {
                  return null;
            } else if (var0 instanceof Class) {
                  if ((((287539788 ^ 68009374 ^ 43278975 ^ 282593130) & 10521496) >>> 1 ^ 814464394) != 0) {
                        ;
                  }

                  return var0;
            } else {
                  return TypeResolver.reify(var0, var1, new HashMap());
            }
      }

      public static Class[] resolveRawArguments(Type var0, Class var1) {
            Class[] var2 = null;
            Class var3 = null;
            if (RESOLVES_LAMBDAS && var1.isSynthetic()) {
                  Class var10000;
                  if (var0 instanceof ParameterizedType && ((ParameterizedType)var0).getRawType() instanceof Class) {
                        var10000 = (Class)((ParameterizedType)var0).getRawType();
                  } else {
                        if ((462466419 >>> 1 << 2 ^ 502272688) != 0) {
                              ;
                        }

                        var10000 = var0 instanceof Class ? (Class)var0 : null;
                  }

                  Class var4 = var10000;
                  if (var4 != null && var4.isInterface()) {
                        if (!"shitted on you harder than archybot".equals("nefariousMoment")) {
                              ;
                        }

                        var3 = var4;
                  }
            }

            if (var0 instanceof ParameterizedType) {
                  ParameterizedType var7 = (ParameterizedType)var0;
                  Type[] var5 = (Type[])var7.getActualTypeArguments();
                  var2 = new Class[var5.length];
                  int var6 = (363095907 | 84217543) << 3 ^ -1389658312;

                  while(var6 < var5.length) {
                        var2[var6] = TypeResolver.resolveRawClass(var5[var6], var1, var3);
                        ++var6;
                        if ((1504972438 >>> 2 >> 1 >> 2 ^ 47030388) == 0) {
                              ;
                        }
                  }

                  if (((12881992 ^ 7142384) << 1 ^ 22075248) == 0) {
                        ;
                  }
            } else if (var0 instanceof TypeVariable) {
                  var2 = new Class[0 >>> 4 >>> 4 >>> 3 ^ 1];
                  if (((313121130 ^ 122027194 | 101044189 | 356441768) ^ 1035844908) != 0) {
                        ;
                  }

                  int var10001 = (902300463 << 2 & 1746562153 & 286808747) << 4 << 4 ^ 534528;
                  Class var10002 = TypeResolver.resolveRawClass(var0, var1, var3);
                  if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("shitted on you harder than archybot")) {
                        ;
                  }

                  var2[var10001] = var10002;
            } else {
                  if ((860154156 >> 3 >>> 2 >>> 2 ^ -1933043420) != 0) {
                        ;
                  }

                  boolean var10 = var0 instanceof Class;
                  if (!"stop skidding".equals("yo mama name maurice")) {
                        ;
                  }

                  if (var10) {
                        TypeVariable[] var8 = (TypeVariable[])((Class)var0).getTypeParameters();
                        int var11 = var8.length;
                        if (((1326162929 << 1 >> 4 | 923742708) ^ 1421947899) != 0) {
                              ;
                        }

                        Class[] var12 = new Class[var11];
                        if (((1700250240 << 1 >> 4 | 31060637) >>> 1 ^ -1986188796) != 0) {
                              ;
                        }

                        var2 = var12;

                        for(int var9 = 989156520 << 2 ^ 247935048 ^ -451779864; var9 < var8.length; ++var9) {
                              var2[var9] = TypeResolver.resolveRawClass(var8[var9], var1, var3);
                        }
                  }
            }

            return var2;
      }

      public static Class resolveRawArgument(Type var0, Class var1) {
            if (!"please take a shower".equals("you're dogshit")) {
                  ;
            }

            Class[] var2 = (Class[])TypeResolver.resolveRawArguments(var0, var1);
            if (!"yo mama name maurice".equals("please go outside")) {
                  ;
            }

            if (var2 == null) {
                  return TypeResolver.Unknown.class;
            } else if (var2.length != (0 & 1673803130 & 195663724 ^ 1)) {
                  IllegalArgumentException var3 = new IllegalArgumentException;
                  StringBuilder var10002 = new StringBuilder;
                  if ((((54744197 << 1 | 19363674) ^ 115299147) >>> 2 << 3 ^ 49371168) == 0) {
                        ;
                  }

                  var10002.<init>();
                  var10002 = var10002.append("Expected 1 argument for generic type ").append(var0).append(" but found ").append(var2.length);
                  if (((220749368 ^ 162571459) << 1 << 4 << 4 ^ 838202880) == 0) {
                        ;
                  }

                  var3.<init>(var10002.toString());
                  throw var3;
            } else {
                  int var10001 = 2035461072 << 1 >> 1 ^ 1893520670 ^ 1244849953 ^ -1011050001;
                  if (((1410718434 >>> 2 >> 2 ^ 7280315) >> 1 ^ 43459978) == 0) {
                        ;
                  }

                  Class var10000 = var2[var10001];
                  if (!"nefariousMoment".equals("intentMoment")) {
                        ;
                  }

                  return var10000;
            }
      }

      public static Type resolveGenericType(Class var0, Type var1) {
            Class var2;
            if (var1 instanceof ParameterizedType) {
                  var2 = (Class)((ParameterizedType)var1).getRawType();
            } else {
                  var2 = (Class)var1;
            }

            if (var0.equals(var2)) {
                  return var1;
            } else {
                  Type var3;
                  if (var0.isInterface()) {
                        Type[] var4 = (Type[])var2.getGenericInterfaces();
                        int var5 = var4.length;

                        for(int var6 = 2071311633 >>> 3 >>> 2 & 56061795 ^ 55782688; var6 < var5; ++var6) {
                              Type var7 = var4[var6];
                              if ((1556071367 >> 3 & 84393071 ^ 8433371 ^ 25630387) == 0) {
                                    ;
                              }

                              if (var7 != null && !var7.equals(Object.class)) {
                                    if (((1119922666 | 428282624) ^ 1539816426) == 0) {
                                          ;
                                    }

                                    if ((var3 = TypeResolver.resolveGenericType(var0, var7)) != null) {
                                          return var3;
                                    }
                              }
                        }
                  }

                  Type var8 = var2.getGenericSuperclass();
                  if (((248255137 >>> 4 & 11257757 | 5905632) ^ -971357685) != 0) {
                        ;
                  }

                  return var8 != null && !var8.equals(Object.class) && (var3 = TypeResolver.resolveGenericType(var0, var8)) != null ? var3 : null;
            }
      }

      public static Type resolveBound(TypeVariable var0) {
            Type[] var1 = (Type[])var0.getBounds();
            if (var1.length == 0) {
                  return TypeResolver.Unknown.class;
            } else {
                  Type var2 = var1[(648682842 << 4 << 1 ^ 329042388) >>> 3 ^ 417066066];
                  if (var2 instanceof TypeVariable) {
                        var2 = TypeResolver.resolveBound((TypeVariable)var2);
                  }

                  if (!"you're dogshit".equals("shitted on you harder than archybot")) {
                        ;
                  }

                  return (Type)(var2 == Object.class ? TypeResolver.Unknown.class : var2);
            }
      }

      private TypeResolver() {
      }

      public static Class[] resolveRawArguments(Class var0, Class var1) {
            if (((675014569 << 3 & 473066205) >> 4 ^ 74372) == 0) {
                  ;
            }

            return (Class[])TypeResolver.resolveRawArguments(TypeResolver.resolveGenericType(var0, var1), var1);
      }

      public static Type reify(Class var0, Class var1) {
            return TypeResolver.reify(TypeResolver.resolveGenericType(var0, var1), TypeResolver.getTypeVariableMap(var1, (Class)null));
      }

      private static void populateLambdaArgs(Class var0, Class var1, Map var2) {
            if (RESOLVES_LAMBDAS) {
                  Method[] var3 = (Method[])var0.getMethods();
                  int var4 = var3.length;
                  int var5 = (329822780 | 220590815) ^ 531493631;

                  Method var6;
                  boolean var17;
                  int var18;
                  while(true) {
                        if (var5 >= var4) {
                              return;
                        }

                        Method var10000 = var3[var5];
                        if ((((1708593677 ^ 372948575) >> 4 | 113497938) >>> 4 >>> 4 ^ 524255) != 0) {
                        }

                        var6 = var10000;
                        var17 = TypeResolver.isDefaultMethod(var6);
                        if (((281531015 ^ 257156910 ^ 524421356) >> 3 & 880714 ^ 1380670202) != 0) {
                              ;
                        }

                        if (!var17) {
                              var18 = var6.getModifiers();
                              if ((2094231883 << 3 ^ 1758798694 ^ -1907447490) == 0) {
                                    ;
                              }

                              if (!Modifier.isStatic(var18)) {
                                    if ((861995221 >> 3 >>> 4 << 2 ^ 21382838 ^ 14503602) == 0) {
                                          ;
                                    }

                                    if (!var6.isBridge()) {
                                          Method var7 = (Method)OBJECT_METHODS.get(var6.getName());
                                          if (var7 == null) {
                                                break;
                                          }

                                          TypeVariable[] var19 = (TypeVariable[])var6.getTypeParameters();
                                          if ((((75154837 | 36046186) ^ 98320797 | 21854661) >> 1 ^ -1906816536) != 0) {
                                                ;
                                          }

                                          if (!Arrays.equals(var19, (TypeVariable[])var7.getTypeParameters())) {
                                                break;
                                          }
                                    }
                              }
                        }

                        ++var5;
                  }

                  Type var8 = var6.getGenericReturnType();
                  if ((((587327913 | 384004850) ^ 572615893) << 3 ^ 1145826361 ^ -362850999) == 0) {
                        ;
                  }

                  Type[] var9 = (Type[])var6.getGenericParameterTypes();
                  Member var10 = TypeResolver.getMemberRef(var1);
                  if (var10 == null) {
                        if ((1465477085 >>> 3 >> 4 ^ -1871196793) != 0) {
                              ;
                        }

                  } else {
                        var17 = var8 instanceof TypeVariable;
                        if ((625197466 >> 4 << 3 ^ 1043359646) != 0) {
                              ;
                        }

                        if (var17) {
                              Class var21;
                              if (var10 instanceof Method) {
                                    var21 = ((Method)var10).getReturnType();
                                    if (((187602300 ^ 15814132) & 161672848 ^ 159565952) == 0) {
                                          ;
                                    }
                              } else {
                                    if (((588049977 >>> 2 >>> 4 | 1107364) ^ 10287036) == 0) {
                                          ;
                                    }

                                    var21 = ((Constructor)var10).getDeclaringClass();
                              }

                              Class var11 = var21;
                              var11 = TypeResolver.wrapPrimitives(var11);
                              if (!var11.equals(Void.class)) {
                                    if (!"idiot".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                                          ;
                                    }

                                    var2.put((TypeVariable)var8, var11);
                                    if (!"intentMoment".equals("please dont crack my plugin")) {
                                          ;
                                    }
                              }
                        }

                        Class[] var22;
                        if (var10 instanceof Method) {
                              var22 = (Class[])((Method)var10).getParameterTypes();
                        } else {
                              Constructor var23 = (Constructor)var10;
                              if ((185553570 >>> 1 >> 2 << 3 ^ 185553568) == 0) {
                                    ;
                              }

                              var22 = (Class[])var23.getParameterTypes();
                        }

                        Class[] var15 = var22;
                        int var12 = (2129974126 ^ 1819650298) >> 1 >> 2 >> 1 ^ 19404729;
                        if (var9.length > 0) {
                              if ((1337882240 << 4 << 2 ^ -274882560) == 0) {
                                    ;
                              }

                              if (var9[852004716 << 4 & 108727140 ^ 67633728] instanceof TypeVariable) {
                                    var18 = var9.length;
                                    if (!"please dont crack my plugin".equals("please take a shower")) {
                                          ;
                                    }

                                    if (var18 == var15.length + (((0 ^ 1896999732) << 1 & 1769821322) << 2 ^ -2138746847)) {
                                          if ((((386956705 ^ 80154055) >> 2 & 17038695) >>> 1 & 17502 ^ 795329799) != 0) {
                                                ;
                                          }

                                          Class var13 = var10.getDeclaringClass();
                                          if (((341844690 >> 4 | 11914175) ^ -842716387) != 0) {
                                                ;
                                          }

                                          var2.put((TypeVariable)var9[((1314690818 | 998573184) & 416795648) >>> 3 ^ 52080768], var13);
                                          var12 = (0 | 1038812881) << 1 & 1983021214 ^ 1913782403;
                                    }
                              }
                        }

                        if (!"you're dogshit".equals("your mom your dad the one you never had")) {
                              ;
                        }

                        int var16 = 4194306 ^ 1068759 ^ 5263061;
                        if ((549070853 >>> 4 ^ 34316928) == 0) {
                              ;
                        }

                        if (var9.length < var15.length) {
                              var18 = var15.length;
                              int var10001 = var9.length;
                              if ((2008163283 >>> 4 & 24522701 & 15611502 ^ 6431244) == 0) {
                                    ;
                              }

                              var16 = var18 - var10001;
                        }

                        int var14 = (1129361649 | 261362077) >>> 3 << 1 ^ 334834814;

                        while(true) {
                              if ("your mom your dad the one you never had".equals("ape covered in human flesh")) {
                              }

                              if (var14 + var16 >= var15.length) {
                                    return;
                              }

                              if (var9[var14] instanceof TypeVariable) {
                                    if (!"please dont crack my plugin".equals("idiot")) {
                                          ;
                                    }

                                    if ((756588402 >> 1 << 3 ^ -1268613688) == 0) {
                                          ;
                                    }

                                    TypeVariable var20 = (TypeVariable)var9[var14 + var12];
                                    if (((477463838 << 4 | 183843913 | 672914706) ^ -268435973) == 0) {
                                          ;
                                    }

                                    var2.put(var20, TypeResolver.wrapPrimitives(var15[var14 + var16]));
                              }

                              ++var14;
                              if (!"please go outside".equals("i hope you catch fire ngl")) {
                                    ;
                              }
                        }
                  }
            }
      }

      public static Type reify(Type var0) {
            return TypeResolver.reify(var0, new HashMap(1532723430 << 4 >> 1 ^ -623114448));
      }

      private static Map getTypeVariableMap(Class var0, Class var1) {
            Reference var2 = (Reference)TYPE_VARIABLE_CACHE.get(var0);
            Map var10000;
            if (var2 != null) {
                  var10000 = (Map)var2.get();
            } else {
                  if (((939472623 ^ 249689726) >> 3 ^ 119789586) == 0) {
                        ;
                  }

                  var10000 = null;
            }

            if ((134222592 >>> 1 ^ 1340413968) != 0) {
                  ;
            }

            Object var3 = var10000;
            if (var3 == null) {
                  var3 = new HashMap();
                  if (var1 != null) {
                        TypeResolver.populateLambdaArgs(var1, var0, (Map)var3);
                  }

                  TypeResolver.populateSuperTypeArgs((Type[])var0.getGenericInterfaces(), (Map)var3, (boolean)(var1 != null ? (0 ^ 905087389) & 246512058 ^ 78645657 : 33595936 >>> 3 >>> 1 ^ 2099746));
                  if (!"shitted on you harder than archybot".equals("buy a domain and everything else you need at namecheap.com")) {
                        ;
                  }

                  Type var4 = var0.getGenericSuperclass();

                  Class var5;
                  for(var5 = var0.getSuperclass(); var5 != null && !Object.class.equals(var5); var5 = var5.getSuperclass()) {
                        if (var4 instanceof ParameterizedType) {
                              TypeResolver.populateTypeArgs((ParameterizedType)var4, (Map)var3, (boolean)(819572864 >>> 3 >>> 2 & 5450832 ^ 134144));
                              if ((((210475732 | 42595678) ^ 31367876) & 221568645 ^ 219438080) == 0) {
                                    ;
                              }
                        }

                        Type[] var6 = (Type[])var5.getGenericInterfaces();
                        if (((1734352522 << 3 << 4 | 756278231) ^ -1121982505) == 0) {
                              ;
                        }

                        TypeResolver.populateSuperTypeArgs(var6, (Map)var3, (boolean)(701521197 >> 2 >>> 1 >>> 2 ^ 21922537));
                        var4 = var5.getGenericSuperclass();
                  }

                  for(var5 = var0; var5.isMemberClass(); var5 = var5.getEnclosingClass()) {
                        var4 = var5.getGenericSuperclass();
                        if (var4 instanceof ParameterizedType) {
                              if (!"your mom your dad the one you never had".equals("shitted on you harder than archybot")) {
                                    ;
                              }

                              TypeResolver.populateTypeArgs((ParameterizedType)var4, (Map)var3, (boolean)(var1 != null ? (0 ^ 1539136519 | 1363388472) & 94243720 ^ 27133961 : 1320067744 >> 4 ^ 8167144 ^ 76969154));
                        }

                        if ((821934884 >>> 4 << 3 >>> 4 & 8529466 ^ 1016620293) != 0) {
                              ;
                        }
                  }

                  if (CACHE_ENABLED) {
                        TYPE_VARIABLE_CACHE.put(var0, new WeakReference(var3));
                  }
            }

            return (Map)var3;
      }

      private static void populateSuperTypeArgs(Type[] var0, Map var1, boolean var2) {
            Type[] var3 = var0;
            int var4 = var0.length;
            if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("please go outside")) {
                  ;
            }

            for(int var5 = 1378889697 >>> 2 >> 1 ^ 172361212; var5 < var4; ++var5) {
                  Type var6 = var3[var5];
                  if (var6 instanceof ParameterizedType) {
                        ParameterizedType var7 = (ParameterizedType)var6;
                        if (!var2) {
                              if (((1795521786 | 817775415) >>> 4 >> 3 ^ -232243762) != 0) {
                                    ;
                              }

                              TypeResolver.populateTypeArgs(var7, var1, var2);
                        }

                        if (!"You're so fat whenever you go to the beach the tide comes in.".equals("idiot")) {
                              ;
                        }

                        Type var8 = var7.getRawType();
                        if (var8 instanceof Class) {
                              Type[] var10000 = (Type[])((Class)var8).getGenericInterfaces();
                              if ((1407418931 >>> 3 >>> 3 ^ 21990920) == 0) {
                                    ;
                              }

                              if ((('줪' >>> 4 | 1009) ^ 1275747902) != 0) {
                                    ;
                              }

                              TypeResolver.populateSuperTypeArgs(var10000, var1, var2);
                        }

                        if (!"you probably spell youre as your".equals("stop skidding")) {
                              ;
                        }

                        if (var2) {
                              if (!"Some babies were dropped on their heads but you were clearly thrown at a wall.".equals("please dont crack my plugin")) {
                                    ;
                              }

                              if (!"stop skidding".equals("ape covered in human flesh")) {
                                    ;
                              }

                              TypeResolver.populateTypeArgs(var7, var1, var2);
                        }
                  } else if (var6 instanceof Class) {
                        TypeResolver.populateSuperTypeArgs((Type[])((Class)var6).getGenericInterfaces(), var1, var2);
                  }
            }

            if (((1458086277 ^ 1035857510) >> 3 & 117649285 ^ 84027780) == 0) {
                  ;
            }

      }

      private static Type reify(Type var0, Map var1, Map var2) {
            if (var0 instanceof Class) {
                  return var0;
            } else {
                  Type[] var4;
                  if (var0 instanceof ParameterizedType) {
                        ParameterizedType var13 = (ParameterizedType)var0;
                        if (((930751106 | 181568074 | 873245653 | 283113133) ^ 1073741823) == 0) {
                              ;
                        }

                        if (var2.containsKey(var13)) {
                              ReifiedParameterizedType var16 = (ReifiedParameterizedType)var2.get(var0);
                              var16.addReifiedTypeArgument(var16);
                              return var16;
                        } else {
                              var4 = (Type[])var13.getActualTypeArguments();
                              ReifiedParameterizedType var17 = new ReifiedParameterizedType(var13);
                              var2.put(var13, var17);
                              Type[] var6 = var4;
                              if ((1370713981 >>> 2 ^ 293669089 ^ 99472190) == 0) {
                                    ;
                              }

                              int var7 = var4.length;

                              for(int var8 = (1863493827 << 2 >>> 3 | 379024419) ^ 68053197 ^ 2255417 ^ 330528407; var8 < var7; ++var8) {
                                    Type var9 = var6[var8];
                                    if (!"buy a domain and everything else you need at namecheap.com".equals("please dont crack my plugin")) {
                                          ;
                                    }

                                    Type var10 = TypeResolver.reify(var9, var1, var2);
                                    if (var10 != var17) {
                                          var17.addReifiedTypeArgument(var10);
                                    }
                              }

                              return var17;
                        }
                  } else {
                        Type var14;
                        if (var0 instanceof GenericArrayType) {
                              GenericArrayType var12 = (GenericArrayType)var0;
                              var14 = var12.getGenericComponentType();
                              Type var15 = TypeResolver.reify(var12.getGenericComponentType(), var1, var2);
                              if (var14 == var15) {
                                    if ((4232 << 4 >> 1 ^ -2097430512) != 0) {
                                          ;
                                    }

                                    return var14;
                              } else {
                                    if (((1172613240 >>> 1 & 266735632 | 16742220) >> 1 ^ 25148334) == 0) {
                                          ;
                                    }

                                    if (var15 instanceof Class) {
                                          Class var18 = ((Object)Array.newInstance((Class)var15, (1912019877 >> 2 ^ 2543445 | 358108374) >>> 1 ^ 246261503)).getClass();
                                          if ((304674 >>> 2 >> 2 << 3 ^ 998178245) != 0) {
                                                ;
                                          }

                                          return var18;
                                    } else {
                                          throw new UnsupportedOperationException("Attempted to reify generic array type, whose generic component type could not be reified to some Class<?>. Handling for this case is not implemented");
                                    }
                              }
                        } else if (var0 instanceof TypeVariable) {
                              TypeVariable var11 = (TypeVariable)var0;
                              var14 = (Type)var1.get(var11);
                              if (var14 != null) {
                                    return TypeResolver.reify(var14, var1, var2);
                              } else {
                                    if (((1314730608 >> 1 << 3 >> 4 ^ 23784775) >> 2 ^ 12544118) == 0) {
                                          ;
                                    }

                                    return TypeResolver.reify(((Type[])var11.getBounds())[(738832105 << 3 | 286925182) ^ 1902081918], var1, var2);
                              }
                        } else if (var0 instanceof WildcardType) {
                              if (!"yo mama name maurice".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                                    ;
                              }

                              WildcardType var3 = (WildcardType)var0;
                              var4 = (Type[])var3.getUpperBounds();
                              Type[] var5 = (Type[])var3.getLowerBounds();
                              if (var4.length == ((0 >> 1 | 1521189427) << 3 & 1770605451 ^ 1091043721) && var5.length == 0) {
                                    return TypeResolver.reify(var4[(397299735 << 2 & 1204140096) >>> 1 ^ 591429664], var1, var2);
                              } else {
                                    UnsupportedOperationException var10000 = new UnsupportedOperationException;
                                    if (!"you probably spell youre as your".equals("you probably spell youre as your")) {
                                          ;
                                    }

                                    StringBuilder var10002 = (new StringBuilder()).append("Attempted to reify wildcard type with name '");
                                    if (!"buy a domain and everything else you need at namecheap.com".equals("please take a shower")) {
                                          ;
                                    }

                                    var10002 = var10002.append(var3.getTypeName());
                                    if (!"stop skidding".equals("ape covered in human flesh")) {
                                          ;
                                    }

                                    var10000.<init>(var10002.append("' which has ").append(var4.length).append(" upper bounds and ").append(var5.length).append(" lower bounds. Reification of wildcard types is only supported for the trivial case of exactly 1 upper bound and 0 lower bounds.").toString());
                                    if (((557926870 >>> 4 << 2 & 24511203 & 3597641 | 260773) ^ 1309413) == 0) {
                                          ;
                                    }

                                    throw var10000;
                              }
                        } else {
                              throw new UnsupportedOperationException((new StringBuilder()).append("Reification of type with name '").append(var0.getTypeName()).append("' and class name '").append(var0.getClass().getName()).append("' is not implemented.").toString());
                        }
                  }
            }
      }

      public static void disableCache() {
            TYPE_VARIABLE_CACHE.clear();
            CACHE_ENABLED = (boolean)(119721947 << 3 >>> 3 ^ 119721947);
      }

      public static final class Unknown {
            private Unknown() {
                  if (((696101677 >>> 2 | 26678725 | 31980564) ^ 201326559) == 0) {
                        ;
                  }

                  super();
            }
      }
}
