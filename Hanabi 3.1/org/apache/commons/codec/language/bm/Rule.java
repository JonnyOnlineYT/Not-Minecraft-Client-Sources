package org.apache.commons.codec.language.bm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.codec.Resources;

public class Rule {
   public static final RPattern ALL_STRINGS_RMATCHER = new RPattern() {
      public boolean isMatch(CharSequence input) {
         return true;
      }
   };
   public static final String ALL = "ALL";
   private static final String DOUBLE_QUOTE = "\"";
   private static final String HASH_INCLUDE = "#include";
   private static final Map RULES = new EnumMap(NameType.class);
   private final RPattern lContext;
   private final String pattern;
   private final PhonemeExpr phoneme;
   private final RPattern rContext;

   private static boolean contains(CharSequence chars, char input) {
      for(int i = 0; i < chars.length(); ++i) {
         if (chars.charAt(i) == input) {
            return true;
         }
      }

      return false;
   }

   private static String createResourceName(NameType nameType, RuleType rt, String lang) {
      return String.format("org/apache/commons/codec/language/bm/%s_%s_%s.txt", nameType.getName(), rt.getName(), lang);
   }

   private static Scanner createScanner(NameType nameType, RuleType rt, String lang) {
      String resName = createResourceName(nameType, rt, lang);
      return new Scanner(Resources.getInputStream(resName), "UTF-8");
   }

   private static Scanner createScanner(String lang) {
      String resName = String.format("org/apache/commons/codec/language/bm/%s.txt", lang);
      return new Scanner(Resources.getInputStream(resName), "UTF-8");
   }

   private static boolean endsWith(CharSequence input, CharSequence suffix) {
      if (suffix.length() > input.length()) {
         return false;
      } else {
         int i = input.length() - 1;

         for(int j = suffix.length() - 1; j >= 0; --j) {
            if (input.charAt(i) != suffix.charAt(j)) {
               return false;
            }

            --i;
         }

         return true;
      }
   }

   public static List getInstance(NameType nameType, RuleType rt, Languages.LanguageSet langs) {
      Map ruleMap = getInstanceMap(nameType, rt, langs);
      List allRules = new ArrayList();
      Iterator var5 = ruleMap.values().iterator();

      while(var5.hasNext()) {
         List rules = (List)var5.next();
         allRules.addAll(rules);
      }

      return allRules;
   }

   public static List getInstance(NameType nameType, RuleType rt, String lang) {
      return getInstance(nameType, rt, Languages.LanguageSet.from(new HashSet(Arrays.asList(lang))));
   }

   public static Map getInstanceMap(NameType nameType, RuleType rt, Languages.LanguageSet langs) {
      return langs.isSingleton() ? getInstanceMap(nameType, rt, langs.getAny()) : getInstanceMap(nameType, rt, "any");
   }

   public static Map getInstanceMap(NameType nameType, RuleType rt, String lang) {
      Map rules = (Map)((Map)((Map)RULES.get(nameType)).get(rt)).get(lang);
      if (rules == null) {
         throw new IllegalArgumentException(String.format("No rules found for %s, %s, %s.", nameType.getName(), rt.getName(), lang));
      } else {
         return rules;
      }
   }

   private static Phoneme parsePhoneme(String ph) {
      int open = ph.indexOf("[");
      if (open >= 0) {
         if (!ph.endsWith("]")) {
            throw new IllegalArgumentException("Phoneme expression contains a '[' but does not end in ']'");
         } else {
            String before = ph.substring(0, open);
            String in = ph.substring(open + 1, ph.length() - 1);
            Set langs = new HashSet(Arrays.asList(in.split("[+]")));
            return new Phoneme(before, Languages.LanguageSet.from(langs));
         }
      } else {
         return new Phoneme(ph, Languages.ANY_LANGUAGE);
      }
   }

   private static PhonemeExpr parsePhonemeExpr(String ph) {
      if (!ph.startsWith("(")) {
         return parsePhoneme(ph);
      } else if (!ph.endsWith(")")) {
         throw new IllegalArgumentException("Phoneme starts with '(' so must end with ')'");
      } else {
         List phs = new ArrayList();
         String body = ph.substring(1, ph.length() - 1);
         String[] var3 = body.split("[|]");
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            String part = var3[var5];
            phs.add(parsePhoneme(part));
         }

         if (body.startsWith("|") || body.endsWith("|")) {
            phs.add(new Phoneme("", Languages.ANY_LANGUAGE));
         }

         return new PhonemeList(phs);
      }
   }

   private static Map parseRules(Scanner scanner, final String location) {
      Map lines = new HashMap();
      final int currentLine = 0;
      boolean inMultilineComment = false;

      while(scanner.hasNextLine()) {
         ++currentLine;
         String rawLine = scanner.nextLine();
         String line = rawLine;
         if (inMultilineComment) {
            if (rawLine.endsWith("*/")) {
               inMultilineComment = false;
            }
         } else if (rawLine.startsWith("/*")) {
            inMultilineComment = true;
         } else {
            int cmtI = rawLine.indexOf("//");
            if (cmtI >= 0) {
               line = rawLine.substring(0, cmtI);
            }

            line = line.trim();
            if (line.length() != 0) {
               if (line.startsWith("#include")) {
                  String incl = line.substring("#include".length()).trim();
                  if (incl.contains(" ")) {
                     throw new IllegalArgumentException("Malformed import statement '" + rawLine + "' in " + location);
                  }

                  Scanner hashIncludeScanner = createScanner(incl);
                  Throwable var10 = null;

                  try {
                     lines.putAll(parseRules(hashIncludeScanner, location + "->" + incl));
                  } catch (Throwable var24) {
                     var10 = var24;
                     throw var24;
                  } finally {
                     if (hashIncludeScanner != null) {
                        if (var10 != null) {
                           try {
                              hashIncludeScanner.close();
                           } catch (Throwable var22) {
                              var10.addSuppressed(var22);
                           }
                        } else {
                           hashIncludeScanner.close();
                        }
                     }

                  }
               } else {
                  String[] parts = line.split("\\s+");
                  if (parts.length != 4) {
                     throw new IllegalArgumentException("Malformed rule statement split into " + parts.length + " parts: " + rawLine + " in " + location);
                  }

                  try {
                     final String pat = stripQuotes(parts[0]);
                     final String lCon = stripQuotes(parts[1]);
                     final String rCon = stripQuotes(parts[2]);
                     PhonemeExpr ph = parsePhonemeExpr(stripQuotes(parts[3]));
                     Rule r = new Rule(pat, lCon, rCon, ph) {
                        private final int myLine = currentLine;
                        private final String loc = location;

                        public String toString() {
                           StringBuilder sb = new StringBuilder();
                           sb.append("Rule");
                           sb.append("{line=").append(this.myLine);
                           sb.append(", loc='").append(this.loc).append('\'');
                           sb.append(", pat='").append(pat).append('\'');
                           sb.append(", lcon='").append(lCon).append('\'');
                           sb.append(", rcon='").append(rCon).append('\'');
                           sb.append('}');
                           return sb.toString();
                        }
                     };
                     String patternKey = r.pattern.substring(0, 1);
                     List rules = (List)lines.get(patternKey);
                     if (rules == null) {
                        rules = new ArrayList();
                        lines.put(patternKey, rules);
                     }

                     ((List)rules).add(r);
                  } catch (IllegalArgumentException var23) {
                     throw new IllegalStateException("Problem parsing line '" + currentLine + "' in " + location, var23);
                  }
               }
            }
         }
      }

      return lines;
   }

   private static RPattern pattern(final String regex) {
      boolean startsWith = regex.startsWith("^");
      boolean endsWith = regex.endsWith("$");
      final String content = regex.substring(startsWith ? 1 : 0, endsWith ? regex.length() - 1 : regex.length());
      boolean boxes = content.contains("[");
      if (!boxes) {
         if (startsWith && endsWith) {
            if (content.length() == 0) {
               return new RPattern() {
                  public boolean isMatch(CharSequence input) {
                     return input.length() == 0;
                  }
               };
            }

            return new RPattern() {
               public boolean isMatch(CharSequence input) {
                  return input.equals(content);
               }
            };
         }

         if ((startsWith || endsWith) && content.length() == 0) {
            return ALL_STRINGS_RMATCHER;
         }

         if (startsWith) {
            return new RPattern() {
               public boolean isMatch(CharSequence input) {
                  return Rule.startsWith(input, content);
               }
            };
         }

         if (endsWith) {
            return new RPattern() {
               public boolean isMatch(CharSequence input) {
                  return Rule.endsWith(input, content);
               }
            };
         }
      } else {
         boolean startsWithBox = content.startsWith("[");
         boolean endsWithBox = content.endsWith("]");
         if (startsWithBox && endsWithBox) {
            final String boxContent = content.substring(1, content.length() - 1);
            if (!boxContent.contains("[")) {
               boolean negate = boxContent.startsWith("^");
               if (negate) {
                  boxContent = boxContent.substring(1);
               }

               final boolean shouldMatch = !negate;
               if (startsWith && endsWith) {
                  return new RPattern() {
                     public boolean isMatch(CharSequence input) {
                        return input.length() == 1 && Rule.contains(boxContent, input.charAt(0)) == shouldMatch;
                     }
                  };
               }

               if (startsWith) {
                  return new RPattern() {
                     public boolean isMatch(CharSequence input) {
                        return input.length() > 0 && Rule.contains(boxContent, input.charAt(0)) == shouldMatch;
                     }
                  };
               }

               if (endsWith) {
                  return new RPattern() {
                     public boolean isMatch(CharSequence input) {
                        return input.length() > 0 && Rule.contains(boxContent, input.charAt(input.length() - 1)) == shouldMatch;
                     }
                  };
               }
            }
         }
      }

      return new RPattern() {
         Pattern pattern = Pattern.compile(regex);

         public boolean isMatch(CharSequence input) {
            Matcher matcher = this.pattern.matcher(input);
            return matcher.find();
         }
      };
   }

   private static boolean startsWith(CharSequence input, CharSequence prefix) {
      if (prefix.length() > input.length()) {
         return false;
      } else {
         for(int i = 0; i < prefix.length(); ++i) {
            if (input.charAt(i) != prefix.charAt(i)) {
               return false;
            }
         }

         return true;
      }
   }

   private static String stripQuotes(String str) {
      if (str.startsWith("\"")) {
         str = str.substring(1);
      }

      if (str.endsWith("\"")) {
         str = str.substring(0, str.length() - 1);
      }

      return str;
   }

   public Rule(String pattern, String lContext, String rContext, PhonemeExpr phoneme) {
      this.pattern = pattern;
      this.lContext = pattern(lContext + "$");
      this.rContext = pattern("^" + rContext);
      this.phoneme = phoneme;
   }

   public RPattern getLContext() {
      return this.lContext;
   }

   public String getPattern() {
      return this.pattern;
   }

   public PhonemeExpr getPhoneme() {
      return this.phoneme;
   }

   public RPattern getRContext() {
      return this.rContext;
   }

   public boolean patternAndContextMatches(CharSequence input, int i) {
      if (i < 0) {
         throw new IndexOutOfBoundsException("Can not match pattern at negative indexes");
      } else {
         int patternLength = this.pattern.length();
         int ipl = i + patternLength;
         if (ipl > input.length()) {
            return false;
         } else if (!input.subSequence(i, ipl).equals(this.pattern)) {
            return false;
         } else {
            return !this.rContext.isMatch(input.subSequence(ipl, input.length())) ? false : this.lContext.isMatch(input.subSequence(0, i));
         }
      }
   }

   static {
      NameType[] var0 = NameType.values();
      int var1 = var0.length;

      for(int var2 = 0; var2 < var1; ++var2) {
         NameType s = var0[var2];
         Map rts = new EnumMap(RuleType.class);
         RuleType[] var5 = RuleType.values();
         int var6 = var5.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            RuleType rt = var5[var7];
            Map rs = new HashMap();
            Languages ls = Languages.getInstance(s);
            Iterator var11 = ls.getLanguages().iterator();

            while(var11.hasNext()) {
               String l = (String)var11.next();

               try {
                  Scanner scanner = createScanner(s, rt, l);
                  Throwable var14 = null;

                  try {
                     rs.put(l, parseRules(scanner, createResourceName(s, rt, l)));
                  } catch (Throwable var39) {
                     var14 = var39;
                     throw var39;
                  } finally {
                     if (scanner != null) {
                        if (var14 != null) {
                           try {
                              scanner.close();
                           } catch (Throwable var38) {
                              var14.addSuppressed(var38);
                           }
                        } else {
                           scanner.close();
                        }
                     }

                  }
               } catch (IllegalStateException var43) {
                  throw new IllegalStateException("Problem processing " + createResourceName(s, rt, l), var43);
               }
            }

            if (!rt.equals(RuleType.RULES)) {
               Scanner scanner = createScanner(s, rt, "common");
               Throwable var45 = null;

               try {
                  rs.put("common", parseRules(scanner, createResourceName(s, rt, "common")));
               } catch (Throwable var40) {
                  var45 = var40;
                  throw var40;
               } finally {
                  if (scanner != null) {
                     if (var45 != null) {
                        try {
                           scanner.close();
                        } catch (Throwable var37) {
                           var45.addSuppressed(var37);
                        }
                     } else {
                        scanner.close();
                     }
                  }

               }
            }

            rts.put(rt, Collections.unmodifiableMap(rs));
         }

         RULES.put(s, Collections.unmodifiableMap(rts));
      }

   }

   public interface RPattern {
      boolean isMatch(CharSequence var1);
   }

   public static final class PhonemeList implements PhonemeExpr {
      private final List phonemes;

      public PhonemeList(List phonemes) {
         this.phonemes = phonemes;
      }

      public List getPhonemes() {
         return this.phonemes;
      }
   }

   public interface PhonemeExpr {
      Iterable getPhonemes();
   }

   public static final class Phoneme implements PhonemeExpr {
      public static final Comparator COMPARATOR = new Comparator() {
         public int compare(Phoneme o1, Phoneme o2) {
            for(int i = 0; i < o1.phonemeText.length(); ++i) {
               if (i >= o2.phonemeText.length()) {
                  return 1;
               }

               int c = o1.phonemeText.charAt(i) - o2.phonemeText.charAt(i);
               if (c != 0) {
                  return c;
               }
            }

            if (o1.phonemeText.length() < o2.phonemeText.length()) {
               return -1;
            } else {
               return 0;
            }
         }
      };
      private final StringBuilder phonemeText;
      private final Languages.LanguageSet languages;

      public Phoneme(CharSequence phonemeText, Languages.LanguageSet languages) {
         this.phonemeText = new StringBuilder(phonemeText);
         this.languages = languages;
      }

      public Phoneme(Phoneme phonemeLeft, Phoneme phonemeRight) {
         this((CharSequence)phonemeLeft.phonemeText, (Languages.LanguageSet)phonemeLeft.languages);
         this.phonemeText.append(phonemeRight.phonemeText);
      }

      public Phoneme(Phoneme phonemeLeft, Phoneme phonemeRight, Languages.LanguageSet languages) {
         this((CharSequence)phonemeLeft.phonemeText, (Languages.LanguageSet)languages);
         this.phonemeText.append(phonemeRight.phonemeText);
      }

      public Phoneme append(CharSequence str) {
         this.phonemeText.append(str);
         return this;
      }

      public Languages.LanguageSet getLanguages() {
         return this.languages;
      }

      public Iterable getPhonemes() {
         return Collections.singleton(this);
      }

      public CharSequence getPhonemeText() {
         return this.phonemeText;
      }

      /** @deprecated */
      @Deprecated
      public Phoneme join(Phoneme right) {
         return new Phoneme(this.phonemeText.toString() + right.phonemeText.toString(), this.languages.restrictTo(right.languages));
      }

      public Phoneme mergeWithLanguage(Languages.LanguageSet lang) {
         return new Phoneme(this.phonemeText.toString(), this.languages.merge(lang));
      }

      public String toString() {
         return this.phonemeText.toString() + "[" + this.languages + "]";
      }
   }
}
