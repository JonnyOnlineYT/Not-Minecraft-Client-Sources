package net.jodah.typetools;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

class ReifiedParameterizedType implements ParameterizedType {
      private int reified;
      private final boolean[] loop;
      private final ParameterizedType original;
      private final Type[] reifiedTypeArguments;

      public String toString() {
            if ((715465892 >> 1 >>> 1 ^ 1127431728) != 0) {
                  ;
            }

            Type var1 = this.getOwnerType();
            Type var2 = this.getRawType();
            Type[] var3 = (Type[])this.getActualTypeArguments();
            StringBuilder var10000 = new StringBuilder;
            if ((50332912 >>> 1 ^ 2425851 ^ 27591043) == 0) {
                  ;
            }

            var10000.<init>();
            StringBuilder var4 = var10000;
            if (var1 != null) {
                  String var10001;
                  if (var1 instanceof Class) {
                        var4.append(((Class)var1).getName());
                  } else {
                        var10001 = var1.toString();
                        if (((2139561387 >> 1 ^ 897048975 | 163583907) ^ -1267529289) != 0) {
                              ;
                        }

                        var4.append(var10001);
                  }

                  var4.append("$");
                  if (var1 instanceof ParameterizedType) {
                        var10001 = var2.getTypeName();
                        String var10002 = (new StringBuilder()).append(((ParameterizedType)var1).getRawType().getTypeName()).append("$").toString();
                        if ((((521469396 >> 1 & 207973109) >> 4 | 6824657) ^ 570066852) != 0) {
                              ;
                        }

                        var4.append(var10001.replace(var10002, ""));
                  } else if (var2 instanceof Class) {
                        var4.append(((Class)var2).getSimpleName());
                  } else {
                        if (((1867743591 << 2 >>> 2 >> 4 | 44336111) ^ 49658879) == 0) {
                              ;
                        }

                        var4.append(var2.getTypeName());
                  }
            } else {
                  var4.append(var2.getTypeName());
            }

            if (var3 != null && var3.length > 0) {
                  var4.append("<");
                  int var5 = (168200194 >> 1 | 64931546) << 1 ^ 264211894;

                  while(true) {
                        if (((605415310 >>> 1 ^ 292829151) << 4 ^ 1480292150) != 0) {
                              ;
                        }

                        if (var5 >= var3.length) {
                              var4.append(">");
                              break;
                        }

                        if (var5 != 0) {
                              if (!"i hope you catch fire ngl".equals("please get a girlfriend and stop cracking plugins")) {
                                    ;
                              }

                              var4.append(", ");
                              if (((940721164 << 3 | 1843015670) >> 3 ^ 625688511) != 0) {
                                    ;
                              }
                        }

                        Type var6 = var3[var5];
                        int var7 = this.reified;
                        if ((((1097206497 ^ 719439501) << 1 ^ 246755672 | 454887977) ^ 1598974004) != 0) {
                              ;
                        }

                        if (var5 >= var7) {
                              var4.append("?");
                              if (!"nefariousMoment".equals("please get a girlfriend and stop cracking plugins")) {
                                    ;
                              }
                        } else if (var6 == null) {
                              if ((170611549 >>> 3 >> 4 ^ -148839676) != 0) {
                                    ;
                              }

                              var4.append("null");
                        } else if (this.loop[var5]) {
                              var4.append("...");
                        } else {
                              var4.append(var6.getTypeName());
                        }

                        ++var5;
                  }
            }

            if ((((179955997 | 8449750) & 92203779 ^ '찠') & 2324724 ^ 589319 ^ 2678311) == 0) {
                  ;
            }

            return var4.toString();
      }

      public int hashCode() {
            int var1 = this.original.hashCode();

            for(int var2 = 652155245 << 3 << 1 << 1 ^ -605868640; var2 < this.reifiedTypeArguments.length; ++var2) {
                  if (!this.loop[var2] && this.reifiedTypeArguments[var2] instanceof ReifiedParameterizedType) {
                        var1 = (27 & 1 ^ 0 ^ 30) * var1 + this.reifiedTypeArguments[var2].hashCode();
                  }
            }

            if (((2108011039 ^ 1940776976) << 1 ^ 470856734) == 0) {
                  ;
            }

            return var1;
      }

      void addReifiedTypeArgument(Type var1) {
            if (this.reified < this.reifiedTypeArguments.length) {
                  if (var1 == this) {
                        if (!"minecraft".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                              ;
                        }

                        boolean[] var10000 = this.loop;
                        int var10001 = this.reified;
                        if ((2097160 >>> 3 ^ 262145) == 0) {
                              ;
                        }

                        int var10002 = (0 << 1 | 1384979846) ^ 1384979847;
                        if (!"you're dogshit".equals("intentMoment")) {
                              ;
                        }

                        var10000[var10001] = (boolean)var10002;
                  }

                  if ((321970718 >> 1 & 32501981 ^ 25718797) == 0) {
                        ;
                  }

                  Type[] var2 = this.reifiedTypeArguments;
                  int var10003 = this.reified;
                  this.reified = var10003 + (((0 ^ 1893348856) << 2 | 1137504415) ^ -1007880194);
                  var2[var10003] = var1;
            }
      }

      public Type getOwnerType() {
            Type var10000 = this.original.getOwnerType();
            if ((232340998 >>> 2 ^ 10496848 ^ 64382161) == 0) {
                  ;
            }

            return var10000;
      }

      ReifiedParameterizedType(ParameterizedType var1) {
            if ((246448208 >>> 3 >> 2 ^ 2026909324) != 0) {
                  ;
            }

            super();
            this.reified = (2142308614 >>> 4 & 99746) << 1 ^ 131328;
            if ((1427182953 << 3 ^ 230559461 ^ -1523145299) == 0) {
                  ;
            }

            this.original = var1;
            Type[] var10001 = new Type[((Type[])var1.getActualTypeArguments()).length];
            if (!"idiot".equals("minecraft")) {
                  ;
            }

            this.reifiedTypeArguments = var10001;
            if (!"buy a domain and everything else you need at namecheap.com".equals("please dont crack my plugin")) {
                  ;
            }

            if (!"You're so fat whenever you go to the beach the tide comes in.".equals("you probably spell youre as your")) {
                  ;
            }

            this.loop = new boolean[((Type[])var1.getActualTypeArguments()).length];
      }

      public Type getRawType() {
            return this.original.getRawType();
      }

      public Type[] getActualTypeArguments() {
            return this.reifiedTypeArguments;
      }

      public boolean equals(Object var1) {
            if (this == var1) {
                  if (((818166898 ^ 87336299 | 743446074) ^ 25727013 ^ 1163612335) != 0) {
                        ;
                  }

                  return (boolean)((0 | 385082893) >> 1 << 4 ^ -1214304159);
            } else {
                  if (!"idiot".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                        ;
                  }

                  if (var1 != null && this.getClass() == ((Object)var1).getClass()) {
                        ReifiedParameterizedType var2 = (ReifiedParameterizedType)var1;
                        if (!this.original.equals(var2.original)) {
                              return (boolean)((110857819 ^ 10405812) << 4 ^ 1350261379 ^ 808436851);
                        } else if (this.reifiedTypeArguments.length != var2.reifiedTypeArguments.length) {
                              return (boolean)((1215319639 >> 4 ^ 71209348) >> 4 ^ 760150);
                        } else {
                              if ((808958132 >> 1 >> 3 << 2 ^ 202239532) == 0) {
                                    ;
                              }

                              int var10000 = ((1077029318 >>> 2 ^ 80465206) >> 3 | 25342629) ^ 60488429;
                              if (!"you're dogshit".equals("You're so fat whenever you go to the beach the tide comes in.")) {
                                    ;
                              }

                              int var3 = var10000;

                              while(var3 < this.reifiedTypeArguments.length) {
                                    boolean[] var4 = this.loop;
                                    if ((57725091 >> 2 << 4 >>> 3 & 8280705 ^ 230194188) != 0) {
                                          ;
                                    }

                                    if (var4[var3] != var2.loop[var3]) {
                                          return (boolean)(((1277954380 | 687065354) & 1223765768) >>> 1 ^ 611878020);
                                    }

                                    if (this.loop[var3]) {
                                          if (!"please get a girlfriend and stop cracking plugins".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                                                ;
                                          }
                                    } else {
                                          Type var5 = this.reifiedTypeArguments[var3];
                                          if ((((18874500 | 5704408) << 2 >> 3 | 11589536) ^ 12310510) == 0) {
                                                ;
                                          }

                                          Type[] var10001 = var2.reifiedTypeArguments;
                                          if (!"You're so fat whenever you go to the beach the tide comes in.".equals("idiot")) {
                                                ;
                                          }

                                          if (var5 != var10001[var3]) {
                                                return (boolean)(1192041419 >> 4 >>> 1 & 11849937 ^ 3162320);
                                          }
                                    }

                                    if (((1428881965 >> 1 | 489037187) ^ 1068998039) == 0) {
                                          ;
                                    }

                                    ++var3;
                                    if ((1392541810 >>> 3 & 19020876 ^ 2101260) == 0) {
                                          ;
                                    }
                              }

                              return (boolean)((0 & 1347427163) >>> 4 >>> 4 >>> 4 ^ 1);
                        }
                  } else {
                        return (boolean)(256 >> 3 ^ 32);
                  }
            }
      }
}
